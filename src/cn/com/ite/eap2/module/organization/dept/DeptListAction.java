package cn.com.ite.eap2.module.organization.dept;

import java.util.*;

import org.apache.commons.lang3.StringUtils;

import cn.com.ite.eap2.core.service.TreeNode;
import cn.com.ite.eap2.core.struts2.AbstractListAction;
import cn.com.ite.eap2.core.struts2.ServletContent;
import cn.com.ite.eap2.domain.organization.Dept;
import cn.com.ite.eap2.domain.organization.Organ;
import cn.com.ite.eap2.module.power.login.UserSession;
import cn.com.ite.hnjtamis.common.StaticVariable;

/**
 * <p>Title cn.com.ite.eap2.module.organization.organ.OrganListAction</p>
 * <p>Description 数据字典ListAction</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2014</p>
 * @author 宋文科
 * @create time: 2014-6-13 下午01:51:37
 * @version 2.0
 * 
 * @modified records:
 */
public class DeptListAction extends AbstractListAction{
	private static final long serialVersionUID = -7244753779816555403L;
	/**
	 * 名称查询条件
	 */
	private String nameTerm;
	/**
	 * 编码条件 
	 */
	private String codeTerm;
	/**
	 * 类型条件
	 */
	private String typeTerm;
	/**
	 * 是否有效条件
	 */
	private Boolean valid;
	private String validStr;
	/**
	 * 机构条件
	 */
	private String organTerm;
	/**
	 * 查询结果(树形数据）
	 */
	private List<Dept> depts = new ArrayList<Dept>();
	/**
	 * TreeNode类型树形数据
	 */
	private List<TreeNode> children;
	/**
	 * 顶级机构条件,用于只显示当前机构及以下
	 */
	private String topDeptTerm;
	/**
	 * 列表查询方法
	 * @return
	 * @modified
	 */
	@SuppressWarnings("unchecked")
	public String list()throws Exception{
		if(!StringUtils.isEmpty(this.getValidStr()))
			this.setValid(this.getValidStr().equals("1"));
		List querys = (List<Dept>)service.queryData("queryHql", this,null);
		Map<String[],Class> copyFieldMap = new HashMap<String[],Class>();
		copyFieldMap.put(new String[]{"organ","organId","organName"}, Organ.class);
	    //把线性结构转成树形结构
		depts = service.childObjectHandler(querys, "deptId", "dept", 
				"depts",new String[]{"deptRoles","quarters","employees"},copyFieldMap,this.getFilterIds(),"orderNo",topDeptTerm);
		return "list";
	}
	/**
	 * 子查询
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String subList()throws Exception{
		depts = (List<Dept>)service.queryData("querySubHql", this,null);
		return "subList";
	}
	/**
	 * 机构部门树
	 * @return
	 * @throws Exception
	 * @modified
	 */
	public String odtree() throws Exception{
		DeptService ds = (DeptService)service;
		children = (List<TreeNode>)ds.findOrganDeptTree(organTerm, nameTerm);
		return "odtree";
	}
	
	/**
	 * 机构部门树
	 * @return
	 * @throws Exception
	 * @modified
	 */
	public String ownerOrganTree() throws Exception{
		DeptService ds = (DeptService)service;
		children = (List<TreeNode>)ds.findOwerOrganTree(organTerm, nameTerm);
		return "odtree";
	}
	
	/**
	 * 查询部门树
	 * @return
	 * @throws Exception
	 * @modified
	 */
	public String ownerDeptTree(){
		UserSession usersess = (UserSession)ServletContent.getSession().get(StaticVariable.USERSESSION);
		if(usersess == null){
			this.setMsg("SESSION　信息已失效，请关闭浏览器并重新登陆！");
			return "odtree";
	 	}
		String deptId = this.getId();
		if(deptId==null || "".equals(deptId) || "null".equals(deptId)){
			deptId = usersess.getCurrentDeptId();
		} 
		DeptService deptService = (DeptService)this.getService();
		Dept dept = (Dept)this.getService().findDataByKey(deptId, Dept.class);
		while(dept.getDept() !=null){
			dept = dept.getDept();
		}
		children = deptService.queryOwnerDeptTree(dept.getDeptId());
		return "odtree";
	}
	/**
	 * 机构部门切换树
	 * @return
	 * @throws Exception
	 * @modified
	 */
	public String odSwitchtree() throws Exception{
		DeptService ds = (DeptService)service;
		children = (List<TreeNode>)ds.findOrganDeptSwitchTree(organTerm, this.getId());
		return "odtree";
	}
	
	public String getAllShowDeptName(){
		DeptService ds = (DeptService)service;
		Dept dept = (Dept)ds.findDataByKey(this.getId(), Dept.class);
		if(dept!=null){
			String deptName = dept.getDeptName();
			while(dept.getDept()!=null){
				dept = dept.getDept();
				deptName = dept.getDeptName()+" - "+deptName;
			}
			this.setMsg(deptName);
		}
		return "delete";
	}
	/**
	 * 删除
	 * @return
	 * @modified
	 */
	public String delete() throws Exception{
		service.deleteByKeys(this.getId().split(","), Dept.class);
		this.setMsg("机构删除成功！");
		return "delete";
	}
	public String getNameTerm() {
		return nameTerm;
	}
	public List<Dept> getDepts() {
		return depts;
	}
	public void setDepts(List<Dept> depts) {
		this.depts = depts;
	}
	public void setNameTerm(String nameTerm) {
		this.nameTerm = nameTerm;
	}
	public String getCodeTerm() {
		return codeTerm;
	}
	public void setCodeTerm(String codeTerm) {
		this.codeTerm = codeTerm;
	}
	public String getTypeTerm() {
		return typeTerm;
	}
	public void setTypeTerm(String typeTerm) {
		this.typeTerm = typeTerm;
	}
	public Boolean isValid() {
		return valid;
	}
	public Boolean getValid() {
		return valid;
	}
	public void setValid(Boolean valid) {
		this.valid = valid;
	}
	public String getOrganTerm() {
		return organTerm;
	}
	public void setOrganTerm(String organTerm) {
		this.organTerm = organTerm;
	}
	public List<TreeNode> getChildren() {
		return children;
	}
	public void setChildren(List<TreeNode> children) {
		this.children = children;
	}
	public String getTopDeptTerm() {
		return topDeptTerm;
	}
	public void setTopDeptTerm(String topDeptTerm) {
		this.topDeptTerm = topDeptTerm;
	}
	public String getValidStr() {
		return validStr;
	}
	public void setValidStr(String validStr) {
		this.validStr = validStr;
	}
}
