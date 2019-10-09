package cn.com.ite.hnjtamis.exam.base.exampublicuser.form;

import java.util.List;

import cn.com.ite.hnjtamis.common.action.employee.form.EmployeeForm;

public class ExamPublicUserListForm {

	
	
	private String  examPublicId;
	
	private List<EmployeeForm> employeeList;

	public String getExamPublicId() {
		return examPublicId;
	}

	public void setExamPublicId(String examPublicId) {
		this.examPublicId = examPublicId;
	}

	public List<EmployeeForm> getEmployeeList() {
		return employeeList;
	}

	public void setEmployeeList(List<EmployeeForm> employeeList) {
		this.employeeList = employeeList;
	}

	
	
}
