package cn.com.ite.hnjtamis.baseinfo.roleConfig;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;

import cn.com.ite.eap2.core.struts2.AbstractFormAction;
import cn.com.ite.eap2.domain.power.SysRole;
import cn.com.ite.eap2.domain.power.SysUser;
import cn.com.ite.eap2.module.power.login.LoginAction;
import cn.com.ite.eap2.module.power.login.UserSession;
import cn.com.ite.hnjtamis.baseinfo.domain.RoleAssobject;
import cn.com.ite.hnjtamis.baseinfo.domain.RoleObjectRelation;

/**
 *
 * <p>Title cn.com.ite.hnjtamis.baseinfo.roleConfig.RoleConfigFormAction</p>
 * <p>Description 角色配置管理</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2016</p>
 * @create time: 2016年11月18日 上午9:22:10
 * @version 1.0
 * 
 * @modified records:
 */
public class RoleConfigFormAction extends AbstractFormAction implements ServletRequestAware{

	private static final long serialVersionUID = 824598996450928951L;
	
	private HttpServletRequest request;
	
	private String roleIds;
	
	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
		
	}
	
	/**
	 * 分配权限
	 * @return
	 * @throws Exception
	 */
	public String saveRole() throws Exception{
		String[] roleIdArray = this.getRoleIds().split(",");
		RoleAssobject roleAssobject = (RoleAssobject)service.findDataByKey(this.getId(), RoleAssobject.class);
		roleAssobject.getRoleObjectRelations().clear();
		int i = 0;
		String roleNames = "";
		Map<String,String> roleNamesMap = new HashMap<String,String>();
		List<SysRole> sysRoleList = service.queryAllDate(SysRole.class);
		for(SysRole role:sysRoleList){
			roleNamesMap.put(role.getRoleId(), role.getRoleName());
		}
		
		for(String roleId:roleIdArray){
			//SysRole role = (SysRole)service.findDataByKey(roleId, SysRole.class);
			RoleObjectRelation roleObjectRelation = new RoleObjectRelation();
			String roleName = roleNamesMap.get(roleId);
			roleObjectRelation.setRoleName(roleName);
			roleNames+=roleName+"，";
			roleObjectRelation.setRoleId(roleId);
			roleObjectRelation.setRoleAssobject(roleAssobject);
			roleObjectRelation.setOrderno(i++);
			roleAssobject.getRoleObjectRelations().add(roleObjectRelation);
		}
		if(roleNames.length()>0){
			roleNames = roleNames.substring(0,roleNames.length()-1);
		}
		roleAssobject.setRoleNames(roleNames);
		service.update(roleAssobject);
		this.setMsg("保存成功！");
		return "save";
	}

	public String getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(String roleIds) {
		this.roleIds = roleIds;
	}
	
}