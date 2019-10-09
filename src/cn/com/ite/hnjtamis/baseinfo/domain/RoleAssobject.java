package cn.com.ite.hnjtamis.baseinfo.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * RoleAssobject entity. @author MyEclipse Persistence Tools
 */

public class RoleAssobject implements java.io.Serializable {

	// Fields

	private String roleAssobjectId;
	private String organId;
	private String organName;
	private String deptId;
	private String deptName;
	private String employeeName;
	private String employeeId;
	private String roleNames;
	private String obType;
	private Integer orderno;
	private List<RoleObjectRelation> roleObjectRelations = new ArrayList<RoleObjectRelation>(0);

	// Constructors

	/** default constructor */
	public RoleAssobject() {
	}

	/** full constructor */
	public RoleAssobject(String organId, String organName, String deptId,
			String deptName, String employeeName, String employeeId,
			String roleNames, String obType, Integer orderno,
			List<RoleObjectRelation> roleObjectRelations) {
		this.organId = organId;
		this.organName = organName;
		this.deptId = deptId;
		this.deptName = deptName;
		this.employeeName = employeeName;
		this.employeeId = employeeId;
		this.roleNames = roleNames;
		this.obType = obType;
		this.orderno = orderno;
		this.roleObjectRelations = roleObjectRelations;
	}

	// Property accessors

	public String getRoleAssobjectId() {
		return this.roleAssobjectId;
	}

	public void setRoleAssobjectId(String roleAssobjectId) {
		this.roleAssobjectId = roleAssobjectId;
	}

	public String getOrganId() {
		return this.organId;
	}

	public void setOrganId(String organId) {
		this.organId = organId;
	}

	public String getOrganName() {
		return this.organName;
	}

	public void setOrganName(String organName) {
		this.organName = organName;
	}

	public String getDeptId() {
		return this.deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public String getDeptName() {
		return this.deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getEmployeeName() {
		return this.employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getEmployeeId() {
		return this.employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getRoleNames() {
		return this.roleNames;
	}

	public void setRoleNames(String roleNames) {
		this.roleNames = roleNames;
	}

	public String getObType() {
		return this.obType;
	}

	public void setObType(String obType) {
		this.obType = obType;
	}

	public Integer getOrderno() {
		return this.orderno;
	}

	public void setOrderno(Integer orderno) {
		this.orderno = orderno;
	}

	public List<RoleObjectRelation> getRoleObjectRelations() {
		return this.roleObjectRelations;
	}

	public void setRoleObjectRelations(List<RoleObjectRelation> roleObjectRelations) {
		this.roleObjectRelations = roleObjectRelations;
	}

}