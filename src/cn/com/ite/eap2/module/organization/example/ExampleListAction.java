package cn.com.ite.eap2.module.organization.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.ite.eap2.common.utils.StringUtils;
import cn.com.ite.eap2.core.service.TreeNode;
import cn.com.ite.eap2.core.struts2.AbstractListAction;
import cn.com.ite.eap2.domain.organization.Employee;
import cn.com.ite.eap2.module.organization.dept.DeptService;

public class ExampleListAction extends AbstractListAction{
	
	private List<Employee> list = new ArrayList<Employee>();
	private String selectIds;
	private String deptId;
	private String organId;
	private String deptTerm;
	private String employeeTerm;
	private String postTerm;
	private List<TreeNode> children;
	public String list() throws Exception{
		try {
			ExampleService e = (ExampleService) this.getService();
			list = e.getPerson(selectIds, deptId,deptTerm,employeeTerm,postTerm);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "list";
	}
	public String odtree() throws Exception{
		ExampleService e = (ExampleService) this.getService();
		children = (List<TreeNode>)e.findOrganDeptTree(organId, deptTerm,employeeTerm,postTerm);
		return "odtree";
	}
	public List<Employee> getList() {
		return list;
	}

	public void setList(List<Employee> list) {
		this.list = list;
	}
	public String getSelectIds() {
		return selectIds;
	}

	public void setSelectIds(String selectIds) {
		this.selectIds = selectIds;
	}

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
	public String getOrganId() {
		return organId;
	}
	public void setOrganId(String organId) {
		this.organId = organId;
	}
	public String getDeptTerm() {
		return deptTerm;
	}
	public void setDeptTerm(String deptTerm) {
		this.deptTerm = deptTerm;
	}
	public List<TreeNode> getChildren() {
		return children;
	}
	public void setChildren(List<TreeNode> children) {
		this.children = children;
	}
	public String getEmployeeTerm() {
		return employeeTerm;
	}
	public void setEmployeeTerm(String employeeTerm) {
		this.employeeTerm = employeeTerm;
	}
	public String getPostTerm() {
		return postTerm;
	}
	public void setPostTerm(String postTerm) {
		this.postTerm = postTerm;
	}

}
