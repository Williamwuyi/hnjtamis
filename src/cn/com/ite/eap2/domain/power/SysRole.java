package cn.com.ite.eap2.domain.power;

import java.util.HashSet;
import java.util.Set;

import cn.com.ite.eap2.domain.funres.ModuleResource;

/**
 * <p>Title cn.com.ite.eap2.domain.organization.SysRole</p>
 * <p>Description 系统角色</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2014</p>
 * @author 宋文科
 * @create time: 2014-7-2 下午03:57:21
 * @version 2.0
 * 
 * @modified records:
 */
public class SysRole implements java.io.Serializable {
	private static final long serialVersionUID = 7039717509127545645L;
	private String roleId;
	private RoleType roleType;
	private String roleName;
	private String roleCode;
	private String description;
	private String userId;
	private String userName;
	private Integer orderNo;
	private Set<ModuleResource> roleResources = new HashSet<ModuleResource>(0);
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	public RoleType getRoleType() {
		return roleType;
	}
	public void setRoleType(RoleType roleType) {
		this.roleType = roleType;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public Integer getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}
	public Set<ModuleResource> getRoleResources() {
		return roleResources;
	}
	public void setRoleResources(Set<ModuleResource> roleResources) {
		this.roleResources = roleResources;
	}
	public String getRoleCode() {
		return roleCode;
	}
	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}
}