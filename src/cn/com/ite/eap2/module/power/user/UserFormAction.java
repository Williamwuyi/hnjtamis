package cn.com.ite.eap2.module.power.user;


import org.apache.commons.lang3.StringUtils;

import cn.com.ite.eap2.common.utils.CharsetSwitchUtil;
import cn.com.ite.eap2.core.struts2.AbstractFormAction;
import cn.com.ite.eap2.domain.funres.AppSystem;
import cn.com.ite.eap2.domain.organization.Employee;
import cn.com.ite.eap2.domain.power.SysRole;
import cn.com.ite.eap2.domain.power.SysUser;
import cn.com.ite.eap2.module.power.login.LoginAction;
import cn.com.ite.eap2.module.power.login.UserSession;

/**
 * <p>Title cn.com.ite.eap2.module.power.user.UserFormAction</p>
 * <p>Description 用户FormAction</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2014</p>
 * @author 宋文科
 * @create time: 2014-7-9 上午09:13:05
 * @version 2.0
 * 
 * @modified records:
 */
public class UserFormAction extends AbstractFormAction{
	private static final long serialVersionUID = 6061990327425264872L;
	/**
	 * 用户
	 */
	private SysUser form;
	
	private String password1;
	private String password2;
	private String userIds;
	private String roleIds;
	/**
	 * 查询数据
	 * @return
	 * @modified
	 */
	public String find(){
		form = (SysUser)service.findDataByKey(this.getId(), SysUser.class);
		return "find";
	}
	/**
	 * 保存主对象
	 * @return
	 * @throws Exception
	 */
	public String save() throws Exception{
		UserSession us = LoginAction.getUserSessionInfo();
		form = (SysUser)this.jsonToObject(SysUser.class);
		if(!StringUtils.isEmpty(form.getUserId())){
			if(us.getUserId().equals(form.getUserId()))
				throw new Exception("不能修改自己的用户信息！");
		   SysUser oldUser = (SysUser)service.findDataByKey(form.getUserId(), SysUser.class);
		   form.setPassword(oldUser.getPassword());
		}else
		   form.setPassword(CharsetSwitchUtil.encryptPassword("888888"));
		service.save(form);
		return "save";
	}
	/**
	 * 保存主题
	 * @return
	 * @throws Exception
	 */
	public String saveTheme() throws Exception{
		SysUser user = (SysUser)service.findDataByKey(this.getId(), SysUser.class);
		user.setTheme(this.getRoleIds());
		service.update(user);
		return "save";
	}
	/**
	 * 分配权限
	 * @return
	 * @throws Exception
	 */
	public String savePopdom() throws Exception{
		UserSession us = LoginAction.getUserSessionInfo();
		String[] userIdArray = this.getUserIds().split(",");
		String[] roleIdArray = this.getRoleIds().split(",");
		for(String userId:userIdArray){
			if(userId.equals(us.getUserId()))
				throw new Exception("不能修改自己的权限！");
			SysUser user = (SysUser)service.findDataByKey(userId, SysUser.class);
			user.getUserRoles().clear();
			for(String roleId:roleIdArray){
				SysRole role = (SysRole)service.findDataByKey(roleId, SysRole.class);
				user.getUserRoles().add(role);
			}
			service.update(user);
		}
		return "save";
	}
	/**
	 * 保存我的权限
	 * @return
	 * @throws Exception
	 */
	public String saveMy() throws Exception{
		form = (SysUser)this.jsonToObject(SysUser.class);
		SysUser user = (SysUser)this.getService().findDataByKey(form.getUserId(), SysUser.class);
	    if(!this.password1.equals(this.password2))
	    	throw new Exception("两次录入的密码不一致！");
	    else if(!this.password1.equals("")){
	    	form.setPassword(CharsetSwitchUtil.encryptPassword(password1));
	        user.setPassword(form.getPassword());
	    }
	    if(form.getApp()!=null)
	    user.setApp((AppSystem)this.getService().findDataByKey(form.getApp().getAppId(), AppSystem.class));
	    if(form.getProxyEmployee()!=null && form.getProxyEmployee().getEmployeeId()!=null)
	    user.setProxyEmployee((Employee)this.getService().findDataByKey(form.getProxyEmployee().getEmployeeId(), Employee.class));
	    user.setProxyDate(form.getProxyDate());
	    if(form.getProxyResources()!=null && form.getProxyResources()!=null)
	    user.setProxyResources(form.getProxyResources());
	    user.setTheme(form.getTheme());
		service.update(user);
		return "save";
	}
	public SysUser getForm() {
		return form;
	}

	public void setForm(SysUser form) {
		this.form = form;
	}
	public String getPassword1() {
		return password1;
	}
	public void setPassword1(String password1) {
		this.password1 = password1;
	}
	public String getPassword2() {
		return password2;
	}
	public void setPassword2(String password2) {
		this.password2 = password2;
	}
	public String getUserIds() {
		return userIds;
	}
	public void setUserIds(String userIds) {
		this.userIds = userIds;
	}
	public String getRoleIds() {
		return roleIds;
	}
	public void setRoleIds(String roleIds) {
		this.roleIds = roleIds;
	}
}