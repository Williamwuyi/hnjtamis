package cn.com.ite.hnjtamis.common.action.employee;

import java.util.*;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;

import cn.com.ite.eap2.core.service.TreeNode;
import cn.com.ite.eap2.core.struts2.AbstractListAction;

/*
 * 考生信息维护 - list
 */
public class EmployeeTreeListAction extends AbstractListAction implements ServletRequestAware{

 
	private static final long serialVersionUID = -7825026513641953284L;


	private HttpServletRequest request;
	
	private String organId;
	
	private String id;//选择节点的id
	
	private String type;//选择节点的类型 organ-机构 dept-部门 quarter-岗位
	
	private String selectEmpIds;//已经选中的员工ID
	 
	private List<TreeNode> children;
	
	private List unSelectEmpList;
	
	private int unSelectEmpTotal;
	
	
	private List selectEmpList;
	
	private int selectEmpTotal;
	
	
	private String deptNameTerm;
	
	private String employeeNameTerm;
	
	
	 
	public String queryEmployeeDeptTree(){
		EmployeeTreeService employeeTreeService = (EmployeeTreeService)this.service;
		children = employeeTreeService.queryEmployeeDeptTree(organId);
		return "employeeDeptTreelist";
	}
	
	public String queryPxEmployeeDeptTree(){
		EmployeeTreeService employeeTreeService = (EmployeeTreeService)this.service;
		Map paramMap = new HashMap();
		if(deptNameTerm!=null && !"".equals(deptNameTerm) && !"null".equals(deptNameTerm)){
			paramMap.put("deptNameTerm", deptNameTerm);
		}
		if(employeeNameTerm!=null && !"".equals(employeeNameTerm) && !"null".equals(employeeNameTerm)){
			paramMap.put("employeeNameTerm", employeeNameTerm);
		}
		children = employeeTreeService.queryPxEmployeeDeptTree(organId,paramMap);
		return "employeeDeptTreelist";
	}
	
	
	/**
	 * 查询未选的人员
	 * @author 朱健
	 * @return
	 * @modified
	 */
	public String queryUnSelectEmployee(){
		EmployeeTreeService employeeTreeService = (EmployeeTreeService)this.service;
		if(id!=null && !"".equals(id)){
			Map paramMap = new HashMap();
			if(employeeNameTerm!=null && !"".equals(employeeNameTerm) && !"null".equals(employeeNameTerm)){
				paramMap.put("employeeNameTerm", employeeNameTerm);
			}
			unSelectEmpList = employeeTreeService.getSelectEmp(id,type,selectEmpIds,"notIn",paramMap);
		}else{
			unSelectEmpList = new ArrayList();
		}
		unSelectEmpTotal = unSelectEmpList.size();
		return "unSelectEmployee";
	}
	
	/**
	 * 查询已选的人员
	 * @author 朱健
	 * @return
	 * @modified
	 */
	public String querySelectEmployee(){
		EmployeeTreeService employeeTreeService = (EmployeeTreeService)this.service;
		if(selectEmpIds!=null && !"".equals(selectEmpIds)){
			Map paramMap = new HashMap();
			selectEmpList = employeeTreeService.getSelectEmp(null,null,selectEmpIds,"in",paramMap);
		}else{
			selectEmpList = new ArrayList();
		}
		selectEmpTotal = selectEmpList.size();
		return "selectEmployee";
	}
	
	
	
	public List<TreeNode> getChildren() {
		return children;
	}
	public void setChildren(List<TreeNode> children) {
		this.children = children;
	}



	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}


	public List getUnSelectEmpList() {
		return unSelectEmpList;
	}


	public void setUnSelectEmpList(List unSelectEmpList) {
		this.unSelectEmpList = unSelectEmpList;
	}


	public int getUnSelectEmpTotal() {
		return unSelectEmpTotal;
	}


	public void setUnSelectEmpTotal(int unSelectEmpTotal) {
		this.unSelectEmpTotal = unSelectEmpTotal;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public String getSelectEmpIds() {
		return selectEmpIds;
	}


	public void setSelectEmpIds(String selectEmpIds) {
		this.selectEmpIds = selectEmpIds;
	}


	public List getSelectEmpList() {
		return selectEmpList;
	}


	public void setSelectEmpList(List selectEmpList) {
		this.selectEmpList = selectEmpList;
	}


	public int getSelectEmpTotal() {
		return selectEmpTotal;
	}


	public void setSelectEmpTotal(int selectEmpTotal) {
		this.selectEmpTotal = selectEmpTotal;
	}


	public String getOrganId() {
		return organId;
	}


	public void setOrganId(String organId) {
		this.organId = organId;
	}

	public String getDeptNameTerm() {
		return deptNameTerm;
	}

	public void setDeptNameTerm(String deptNameTerm) {
		this.deptNameTerm = deptNameTerm;
	}

	public String getEmployeeNameTerm() {
		return employeeNameTerm;
	}

	public void setEmployeeNameTerm(String employeeNameTerm) {
		this.employeeNameTerm = employeeNameTerm;
	}
	
	
 
}
