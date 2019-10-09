package cn.com.ite.eap2.domain.power;

import java.util.*;

/**
 * <p>Title cn.com.ite.eap2.domain.power.RoleType</p>
 * <p>Description 角色类型</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2014</p>
 * @author 宋文科
 * @create time: 2014-7-2 下午03:47:29
 * @version 2.0
 * 
 * @modified records:
 */
public class RoleType implements java.io.Serializable {
	private static final long serialVersionUID = -7068097709137279167L;
	private String rtId;
	private RoleType roleType;
	private String roleTypename;
	private String roleTypeCode;
	private String desc;
	private int sortNo;
	private List<SysRole> sysRoles = new ArrayList<SysRole>(0);
	private List<RoleType> roleTypes = new ArrayList<RoleType>(0);
	//不保存，用于列表显示角色资源
	private String resourceName;
	public String getRtId() {
		return rtId;
	}
	public void setRtId(String rtId) {
		this.rtId = rtId;
	}
	public RoleType getRoleType() {
		return roleType;
	}
	public void setRoleType(RoleType roleType) {
		this.roleType = roleType;
	}
	public String getRoleTypename() {
		return roleTypename;
	}
	public void setRoleTypename(String roleTypename) {
		this.roleTypename = roleTypename;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public int getSortNo() {
		return sortNo;
	}
	public void setSortNo(int sortNo) {
		this.sortNo = sortNo;
	}
	public List<SysRole> getSysRoles() {
		return sysRoles;
	}
	public void setSysRoles(List<SysRole> sysRoles) {
		this.sysRoles = sysRoles;
	}
	public List<RoleType> getRoleTypes() {
		return roleTypes;
	}
	public void setRoleTypes(List<RoleType> roleTypes) {
		this.roleTypes = roleTypes;
	}
	public String getRoleTypeCode() {
		return roleTypeCode;
	}
	public void setRoleTypeCode(String roleTypeCode) {
		this.roleTypeCode = roleTypeCode;
	}
	public String getResourceName() {
		return resourceName;
	}
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}
}