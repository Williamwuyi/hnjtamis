package cn.com.ite.hnjtamis.baseinfo.domain;

/**
 * RoleObjectRelation entity. @author MyEclipse Persistence Tools
 */

public class RoleObjectRelation implements java.io.Serializable {

	// Fields

	private String relationId;
	private RoleAssobject roleAssobject;
	private String roleName;
	private String roleId;
	private Integer orderno;

	// Constructors

	/** default constructor */
	public RoleObjectRelation() {
	}

	/** full constructor */
	public RoleObjectRelation(RoleAssobject roleAssobject, String roleName,
			String roleId, Integer orderno) {
		this.roleAssobject = roleAssobject;
		this.roleName = roleName;
		this.roleId = roleId;
		this.orderno = orderno;
	}

	// Property accessors

	public String getRelationId() {
		return this.relationId;
	}

	public void setRelationId(String relationId) {
		this.relationId = relationId;
	}

	public RoleAssobject getRoleAssobject() {
		return this.roleAssobject;
	}

	public void setRoleAssobject(RoleAssobject roleAssobject) {
		this.roleAssobject = roleAssobject;
	}

	public String getRoleName() {
		return this.roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleId() {
		return this.roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public Integer getOrderno() {
		return this.orderno;
	}

	public void setOrderno(Integer orderno) {
		this.orderno = orderno;
	}

}