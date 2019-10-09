package cn.com.ite.eap2.module.organization.employee;

import java.util.*;

import org.apache.commons.lang3.StringUtils;

import cn.com.ite.eap2.core.service.TreeNode;
import cn.com.ite.eap2.core.struts2.AbstractListAction;
import cn.com.ite.eap2.core.struts2.ServletContent;
import cn.com.ite.eap2.domain.organization.Dept;
import cn.com.ite.eap2.domain.organization.Employee;
import cn.com.ite.eap2.module.power.login.UserSession;
import cn.com.ite.hnjtamis.common.StaticVariable;

/**
 * <p>Title cn.com.ite.eap2.module.organization.quarter.QuarterListAction</p>
 * <p>Description 岗位ListAction</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2014</p>
 * @author 宋文科
 * @create time: 2014-7-7 上午08:52:08
 * @version 2.0
 * 
 * @modified records:
 */
public class EmployeeListAction extends AbstractListAction{
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
	 * 是否有效条件
	 */
	private Boolean valid;
	private String validStr;
	/**
	 * 部门条件
	 */
	private String deptTerm;
	/**
	 * 岗位条件
	 */
	private String quarterTerm;
	/**
	 * 顶级机构条件
	 */
	private String organTerm;
	
	private String quarterTrainTerm;
	
	private String stTypeTerm;
	/**
	 * 查询结果
	 */
	private List<Employee> list = new ArrayList<Employee>();
	
	private List<TreeNode> children;
	
	private String op;
	
	private String deptIds;
	
	/**
	 * 列表查询方法
	 * @return
	 * @modified
	 */
	@SuppressWarnings("unchecked")
	public String list()throws Exception{
		UserSession usersess = (UserSession)ServletContent.getSession().get(StaticVariable.USERSESSION);
		if(usersess == null){
			this.setMsg("SESSION　信息已失效，请关闭浏览器并重新登陆！");
			return "list";
	 	}
		if(!StringUtils.isEmpty(this.getValidStr()))
			this.setValid(this.getValidStr().equals("1"));
		EmployeeService employeeService = (EmployeeService)service;
		if("dept".equals(this.getOp())){
			String deptId = this.getId();
			if(deptId==null || "".equals(deptId) || "null".equals(deptId)){
				deptId = usersess.getCurrentDeptId();
			} 
			Dept dept = (Dept)this.getService().findDataByKey(deptId, Dept.class);
			while(dept.getDept() !=null){
				dept = dept.getDept();
			}
			deptIds = employeeService.queryOwnerDeptIds(dept.getDeptId());
		}else{
			deptIds = null;
		}
		String queryHql = "queryHql";
		if("haveSt".equals(stTypeTerm)){
			queryHql = "queryHql1";
		}else if("noneSt".equals(stTypeTerm)){
			queryHql = "queryHql2";
		}
		list = (List<Employee>)service.queryData(queryHql, this,null,this.getStart(),this.getLimit());
		this.setTotal(service.countData(queryHql, this));
		return "list";
	}
	/**
	 * 机构部门员工树
	 * @return
	 * @throws Exception
	 * @modified
	 */
	public String odetree() throws Exception{
		EmployeeService qs = (EmployeeService)service;
		try{
		children=(List<TreeNode>)qs.findOrganDeptEmployeeTree(organTerm, nameTerm,
				this.getParentId(),this.getParentType());
		}catch(Exception e){
			e.printStackTrace();
		}
		return "odetree";
	}
	/**
	 * 机构部门员工树
	 * @return
	 * @throws Exception
	 * @modified
	 */
	public String opxdetree() throws Exception{
		EmployeeService qs = (EmployeeService)service;
		try{
		children=(List<TreeNode>)qs.findPxOrganDeptEmployeeTree(organTerm, nameTerm,
				this.getParentId(),this.getParentType());
		}catch(Exception e){
			e.printStackTrace();
		}
		return "odetree";
	}
	/**
	 * 删除
	 * @return
	 * @modified
	 */
	public String delete() throws Exception{
		String[] ids = this.getId().split(",");
		int delNum = 0;
		int delNum2 = 0;
		for(int i=0;i<ids.length;i++){
			Employee employee = (Employee)service.findDataByKey(ids[i], Employee.class);
			if(employee!=null){
				
				try{
					
					
					Map term = new HashMap();
					term.put("employeeId", ids[i]);
					
					List userlist = service.queryData("queryUserByEmployee", term, null);
					if(userlist == null || userlist.size()==0){
						List templist = service.queryData("queryTalentByEmployee", term, null);
						if(templist.size()>0){
							service.deletes(templist);
						}
						service.delete(employee);
						delNum++;
					}else{
						delNum2++;
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
		//service.deleteByKeys(, Employee.class);
		if(delNum2 == 0){
			this.setMsg("成功删除"+delNum+"条记录！");
		}else{
			this.setMsg("成功删除"+delNum+"条记录，其中"+delNum2+"存在用户信息，不能被删除！");
		}
		return "delete";
	}
	public String getNameTerm() {
		return nameTerm;
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
	public Boolean isValid() {
		return valid;
	}
	public Boolean getValid() {
		return valid;
	}
	public void setValid(Boolean valid) {
		this.valid = valid;
	}
	public String getQuarterTerm() {
		return quarterTerm;
	}
	public void setQuarterTerm(String quarterTerm) {
		this.quarterTerm = quarterTerm;
	}
	public List<Employee> getList() {
		return list;
	}
	public void setList(List<Employee> list) {
		this.list = list;
	}
	public String getDeptTerm() {
		return deptTerm;
	}
	public void setDeptTerm(String deptTerm) {
		this.deptTerm = deptTerm;
	}
	public String getOrganTerm() {
		return organTerm;
	}
	public void setOrganTerm(String organTerm) {
		this.organTerm = organTerm;
	}
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
	public String getQuarterTrainTerm() {
		return quarterTrainTerm;
	}
	public void setQuarterTrainTerm(String quarterTrainTerm) {
		this.quarterTrainTerm = quarterTrainTerm;
	}
	public String getStTypeTerm() {
		return stTypeTerm;
	}
	public void setStTypeTerm(String stTypeTerm) {
		this.stTypeTerm = stTypeTerm;
	}
	public String getOp() {
		return op;
	}
	public void setOp(String op) {
		this.op = op;
	}
	public String getDeptIds() {
		return deptIds;
	}
	public void setDeptIds(String deptIds) {
		this.deptIds = deptIds;
	}
	
	
}
