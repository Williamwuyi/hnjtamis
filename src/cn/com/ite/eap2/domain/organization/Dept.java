package cn.com.ite.eap2.domain.organization;

import java.util.*;

import cn.com.ite.eap2.domain.power.SysRole;

/**
 * <p>Title cn.com.ite.eap2.domain.organization.Dept</p>
 * <p>Description 部门</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2014</p>
 * @author 宋文科
 * @create time: 2014-7-2 下午03:32:31
 * @version 2.0
 * 
 * @modified records:
 */
public class Dept implements java.io.Serializable {
	private static final long serialVersionUID = 5885263368826874075L;
	private String deptId;
	private Dept dept;
	private Organ organ;
	private String deptCode;
	private String deptName;
	private String deptAlias;
	private String deptType;
	private String deptCharacter;
	private Integer orderNo;
	private boolean validation;
	private String levelCode;
	private String remark;
	private List<SysRole> deptRoles = new ArrayList<SysRole>(0);
	private List<Quarter> quarters = new ArrayList<Quarter>(0);
	private List<Dept> depts = new ArrayList<Dept>(0);
	private List<Employee> employees = new ArrayList<Employee>(0);

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public Dept getDept() {
		return dept;
	}

	public void setDept(Dept dept) {
		this.dept = dept;
	}

	public Organ getOrgan() {
		return organ;
	}

	public void setOrgan(Organ organ) {
		this.organ = organ;
	}

	public String getDeptCode() {
		return deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getDeptAlias() {
		return deptAlias;
	}

	public void setDeptAlias(String deptAlias) {
		this.deptAlias = deptAlias;
	}

	public String getDeptType() {
		return deptType;
	}

	public void setDeptType(String deptType) {
		this.deptType = deptType;
	}

	public String getDeptCharacter() {
		return deptCharacter;
	}

	public void setDeptCharacter(String deptCharacter) {
		this.deptCharacter = deptCharacter;
	}

	public Integer getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}

	public boolean isValidation() {
		return validation;
	}
	
	public boolean getValidation() {
		return validation;
	}

	public void setValidation(boolean validation) {
		this.validation = validation;
	}

	public String getLevelCode() {
		return levelCode;
	}

	public void setLevelCode(String levelCode) {
		this.levelCode = levelCode;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public List<SysRole> getDeptRoles() {
		return deptRoles;
	}

	public void setDeptRoles(List<SysRole> deptRoles) {
		this.deptRoles = deptRoles;
	}

	public List<Quarter> getQuarters() {
		return quarters;
	}

	public void setQuarters(List<Quarter> quarters) {
		this.quarters = quarters;
	}

	public List<Dept> getDepts() {
		return depts;
	}

	public void setDepts(List<Dept> depts) {
		this.depts = depts;
	}

	public List<Employee> getEmployees() {
		return employees;
	}

	public void setEmployees(List<Employee> employees) {
		this.employees = employees;
	}

	public Dept() {
	}
}