package cn.com.ite.hnjtamis.jobstandard.termstype;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import cn.com.ite.eap2.core.service.TreeNode;
import cn.com.ite.eap2.core.struts2.AbstractListAction;
import cn.com.ite.hnjtamis.baseinfo.common.DicDefine;
import cn.com.ite.hnjtamis.baseinfo.domain.AbstractDomain;
import cn.com.ite.hnjtamis.jobstandard.domain.StandardTypes;
/**
 * <p>Title 岗位达标培训信息系统-基础信息模块</p>
 * <p>Description 专业类型ListAction</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2015</p>
 * @author wangyong
 * @create Mar 24, 2015 9:14:48 AM
 * @version 1.0
 * 
 * @modified records:
 */
public class StandardTypeListAction extends AbstractListAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8061559691203248709L;
	
	/**
	 * TreeNode类型树形数据
	 */
	private List<TreeNode> children;
	// 查询条件
	private String toptypeidTerm;
	private String typenameTerm="";  //
	private String validStr;
	private String nameTerm="";  //
	private String isavailableTermChk;
	private Integer isavailableTerm;
	
	private List<StandardTypes> list = new ArrayList<StandardTypes>();
	
	public String getValidStr() {
		return validStr;
	}
	public void setValidStr(String validStr) {
		this.validStr = validStr;
	}
	public List<TreeNode> getChildren() {
		return children;
	}
	public void setChildren(List<TreeNode> children) {
		this.children = children;
	}
	public String list()throws Exception{
		if("true".equals(isavailableTermChk)){
			isavailableTerm = null;
		}else{
			isavailableTerm = 1;
		}
//		if(!StringUtils.isEmpty(this.getValidStr()))
//			this.setValid(this.getValidStr().equals("1"));
		list = (List<StandardTypes>)service.queryData("queryHql", this,null,this.getStart(),this.getLimit());
		//把线性结构转成树形结构
//		baseSpecialities = service.childObjectHandler(baseSpecialities, "bstid", "parentspeciltype", 
//				"baseSpecialities",new String[]{},null,this.getFilterIds(),"orderno",null);
		this.setTotal(service.countData("queryHql", this));
		return "list";
	}
	
//	/**
//	 * 子查询
//	 * @return
//	 * @throws Exception
//	 */
//	public String subList()throws Exception{
//		subStandardTypess = (List<StandardTypes>)service.queryData("querySubHql", this,null);
//		return "subList";
//	}
	/**
	 * 删除
	 * @return
	 * @modified
	 */
	public String delete() throws Exception{
		String[] ids =this.getId().split(",");
		int[] delNum = {0,0};//{总数 ,成功数}
		for(int i=0;i<ids.length;i++){
			StandardTypes vo = (StandardTypes)service.findDataByKey(ids[i], StandardTypes.class);
			if(vo.getJobsStandardterms()==null || vo.getJobsStandardterms().size()==0){
				AbstractDomain.updateCommonFieldValue(vo);
				vo.setStatus(DicDefine.DATA_DELETE);
				vo.setIsavailable(DicDefine.NOT_VALIDATE);
				vo.setTypename(vo.getTypename()+DicDefine.DEL_SUFFIX);
				
				service.save(vo);
				delNum[1]++;
			}
			delNum[0]++;
		}
		//service.deleteByKeys(this.getId().split(","), StandardTypes.class);
		if(delNum[1]<delNum[0]){
			this.setMsg("本次需删除"+delNum[0]+"个条款类型，成功删除"+delNum[1]+"个条款类型，请检查条款类型是否被引用！");
		}else{
			this.setMsg("本次需删除"+delNum[0]+"个条款类型，成功删除"+delNum[1]+"个条款类型！");
		}
		
		return "delete";
	}
	
	public String tree() throws Exception {
		StandardTypeService ds = (StandardTypeService)service;
		children = (List<TreeNode>)ds.findStandardTypeTree(toptypeidTerm, typenameTerm);
		return "tree";
	}
	
	public String standardTypeListTree(){
		children = new ArrayList();
		StandardTypeService ds = (StandardTypeService)service;
		Map term = new HashMap();
		if(this.getId()!=null && !"".equals(this.getId())&& !"null".equals(this.getId())){
			term.put("id",this.getId());
		}else{
			term.put("id","1111111111111111111111111");
		}
		List<StandardTypes> list = ds.queryData("queryParentTreeHql", term, new HashMap());
		Map<String,TreeNode> standardTypesMap = new HashMap<String,TreeNode>();
		for(int i=0;i<list.size();i++){
			StandardTypes standardTypes = list.get(i);
			if(standardTypesMap.get(standardTypes.getJstypeid()) == null){
				this.setStandardTypeNode(standardTypes, standardTypesMap);
			}
			
		}
		if(!StringUtils.isEmpty(nameTerm)){//条件过滤
			TreeNode.select(children, nameTerm);
		}
		return "tree";
	}
	
	private TreeNode setStandardTypeNode(StandardTypes standardTypes,Map<String,TreeNode> standardTypesMap){
		TreeNode node = new TreeNode();
		node.setId(standardTypes.getJstypeid());
		node.setTitle(standardTypes.getTypename());
		node.setType("standardType");
		node.setChildren(new ArrayList());
		if(standardTypes.getParentSpeciltype()!=null){
			node.setParentId(standardTypes.getParentSpeciltype().getJstypeid());
			TreeNode parentNode = standardTypesMap.get(standardTypes.getParentSpeciltype().getJstypeid());
			if(parentNode==null){
				parentNode = this.setStandardTypeNode(standardTypes.getParentSpeciltype(), standardTypesMap);
				standardTypesMap.put(standardTypes.getParentSpeciltype().getJstypeid(),parentNode);
			}
			parentNode.getChildren().add(node);
		}else{
			children.add(node);
		}
		return node;
	}
	
	
	public String getToptypeidTerm() {
		return toptypeidTerm;
	}
	public void setToptypeidTerm(String toptypeidTerm) {
		this.toptypeidTerm = toptypeidTerm;
	}
	public String getTypenameTerm() {
		//setTypenameTerm("3333");
		return typenameTerm;
	}
	public void setTypenameTerm(String typenameTerm) {
		this.typenameTerm = typenameTerm;
	}
	public List<StandardTypes> getList() {
		return list;
	}
	public void setList(List<StandardTypes> list) {
		this.list = list;
	}
	public String getNameTerm() {
		return nameTerm;
	}
	public void setNameTerm(String nameTerm) {
		this.nameTerm = nameTerm;
	}
	public Integer getIsavailableTerm() {
		return isavailableTerm;
	}
	public void setIsavailableTerm(Integer isavailableTerm) {
		this.isavailableTerm = isavailableTerm;
	}
	public String getIsavailableTermChk() {
		return isavailableTermChk;
	}
	public void setIsavailableTermChk(String isavailableTermChk) {
		this.isavailableTermChk = isavailableTermChk;
	}
}
