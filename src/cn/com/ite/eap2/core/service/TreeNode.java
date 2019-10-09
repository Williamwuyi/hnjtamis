package cn.com.ite.eap2.core.service;

import java.util.*;

import org.apache.commons.beanutils.PropertyUtils;

import cn.com.ite.eap2.common.utils.StringUtils;

/**
 * <p>Title cn.com.ite.eap2.core.service.TreeNode</p>
 * <p>Description 树结点类</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2014</p>
 * @author 宋文科
 * @create time: 2014-6-24 下午01:27:22
 * @version 2.0
 * 
 * @modified records:
 */
public class TreeNode {
	/**
	 * 标识符
	 */
	private String id;
	/**
	 * 父标识
	 */
	private String parentId;
	/**
	 * 标题
	 */
	private String title;
	/**
	 * 扩展使用，标签名称
	 */
	private String tagName;
	/**
	 * 浮动提示
	 */
	private String qtip;
	/**
	 * 结点类型
	 */
	private String type;
	/**
	 * 收缩（关闭）图标
	 */
	private String closeIcon;
	/**
	 * 展开（打开 ）图标
	 */
	private String icon;
	/**
	 * 子结点
	 */
	private List<TreeNode> children;
	/**
	 * 是否叶子
	 */
	private boolean leaf;
	/**
	 * 复选框，为null表示不带复选框，真假表示复选状态
	 */
	private Boolean checked;
	/**
	 * 链接地址
	 */
	private String url;
	/**
	 * 临时数据，方便在查询时存储其它有用属性
	 */
	private String temp;
	private Integer orderNo;
	public Integer getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}
	/**
	 * 存储各种类型对应的打开关闭的图标
	 */
	private static Map<String,String[]> typeToIconMap = new HashMap<String,String[]>();
	/**
	 * 增加类型对应图标
	 * @param type 类型
	 * @param openIcon 打开图标
	 * @param closeIncon 关闭图标
	 */
	public static void putTypeIncon(String type,String openIcon,String closeIcon){
		typeToIconMap.put(type, new String[]{openIcon,closeIcon});
	}
	/**
	 * 对象转变成TREE类型
	 * @param bean
	 * @param idField
	 * @param parentField
	 * @param titleField
	 * @return
	 * @throws Exception
	 */
	public static TreeNode objectToTree(Object bean,String idField,String parentField,String titleField) throws Exception{
		String id = PropertyUtils.getProperty(bean, idField).toString();
		String parentId = null;
		try{
			parentId= PropertyUtils.getProperty(bean, parentField).toString();
		}catch(Exception e){}
		String title = PropertyUtils.getProperty(bean, titleField).toString();
		TreeNode node = new TreeNode();
		node.setId(id);
		node.setParentId(parentId);
		node.setTitle(title);
		return node;
	}
	/**
	 * 线性结构转变成树形结构
	 * @param nodes 原树数据
	 * @param isHandlerLeaf 是否标示叶子结点
	 * @param leafType 叶子类型不相符时删除处理，为空不处理
	 * @return
	 */
	public static List<TreeNode> toTree(List<TreeNode> nodes,boolean isHandlerLeaf,List<String> leafType){
		return toTree(nodes,isHandlerLeaf,leafType,null);
	}
	/**
	 * 线性结构转变成树形结构
	 * @param nodes 原树数据
	 * @param isHandlerLeaf 是否标示叶子结点
	 * @param leafType 叶子类型不相符时删除处理，为空不处理
	 * @param topId 只显示此结点的子树
	 * @return
	 */
	public static List<TreeNode> toTree(List<TreeNode> nodes,boolean isHandlerLeaf,List<String> leafType,String topId){
		List<TreeNode> tops = new ArrayList<TreeNode>();
		Map<String,List<TreeNode>> parentToChilds = new HashMap<String,List<TreeNode>>();
		for(TreeNode node:nodes){
			if((StringUtils.isEmpty(topId)&&StringUtils.isEmpty(node.getParentId()))||
					(topId!=null&&topId.equals(node.getId())))
				tops.add(node);
			else{
				List<TreeNode> childs = parentToChilds.get(node.getParentId());
				if(childs==null){
					childs = new ArrayList<TreeNode>();
					parentToChilds.put(node.getParentId(), childs);
				}
				childs.add(node);
			}
		}
		toTreeFor(tops,parentToChilds,isHandlerLeaf,leafType);
		return tops;
	}
	/**
	 * 线性结构转变成树形结构的迭代方法
	 * @param childs
	 * @param parentToChilds
	 */
	@SuppressWarnings("unchecked")
	private static boolean toTreeFor(List<TreeNode> childs,Map<String,
			List<TreeNode>> parentToChilds,boolean isHandlerLeaf,List<String> leafType){
		boolean selected = false;
		for(int i=0;i<childs.size();i++){
			TreeNode node = childs.get(i);
			boolean cSelected = false;
			List<TreeNode> child2s = parentToChilds.get(node.getId());
			if(child2s!=null&&child2s.size()>0){
				if(node.getChildren()==null)
					node.setChildren(new ArrayList());
				node.setLeaf(false);
				node.getChildren().addAll(child2s);
				cSelected = toTreeFor(node.getChildren(),parentToChilds,isHandlerLeaf,leafType);
			}else if(node.getChildren()==null
					||node.getChildren().size()==0){
				if(isHandlerLeaf)
				  node.setLeaf(true);
				if(leafType!=null&&leafType.contains(node.getType()))
					cSelected = true;
			}else{
				cSelected = toTreeFor(node.getChildren(),parentToChilds,isHandlerLeaf,leafType);
			}
			if(leafType!=null&&!cSelected)
				childs.remove(i--);
			else
				selected = true;
		}
		return selected;
	}
	/**
	 * 树数据迭代查询过滤
	 * @param childs
	 * @param term
	 * @return
	 * @modified
	 */
	public static boolean select(List<TreeNode> childs,String term){
		if(StringUtils.isEmpty(term)) return true;
		boolean selected = false;
		for(int i=0;i<childs.size();i++){
			boolean cSelected = false;
			TreeNode node = childs.get(i);
			if(node.getChildren()!=null&&node.getChildren().size()>0)//支条
			   if(select(node.getChildren(),term))
				   cSelected = true;
			if(node.getTitle().indexOf(term)>-1)//匹配的
				cSelected = true;
			if(!cSelected)
				childs.remove(i--);
			else
				selected = true;
		}
		return selected;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * 如果结点中没有设置图标，则从全局映射中取
	 * @return
	 */
	public String getCloseIcon() {
		if(StringUtils.isEmpty(closeIcon)){
			String[] icons = typeToIconMap.get(type);
			if(icons!=null)
			return typeToIconMap.get(type)[1];
		}
		return closeIcon;
	}
	public void setCloseIcon(String closeIcon) {
		this.closeIcon = closeIcon;
	}
	/**
	 * 如果结点中没有设置图标，则从全局映射中取
	 * @return
	 */
	public String getIcon() {
		if(StringUtils.isEmpty(icon)){
			String[] icons = typeToIconMap.get(type);
			if(icons!=null)
			return typeToIconMap.get(type)[0];
		}
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public List<TreeNode> getChildren() {
		return children;
	}
	public void setChildren(List<TreeNode> children) {
		this.children = children;
	}
	public boolean isLeaf() {
		return leaf;
	}
	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}
	public Boolean getChecked() {
		return checked;
	}
	public void setChecked(Boolean checked) {
		this.checked = checked;
	}
	public String getTemp() {
		return temp;
	}
	public void setTemp(String temp) {
		this.temp = temp;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getTagName() {
		return tagName;
	}
	public void setTagName(String tagName) {
		this.tagName = tagName;
	}
	public String getQtip() {
		return qtip;
	}
	public void setQtip(String qtip) {
		this.qtip = qtip;
	}
}
 