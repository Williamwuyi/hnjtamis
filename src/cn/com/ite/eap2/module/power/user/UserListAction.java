package cn.com.ite.eap2.module.power.user;

import java.util.*;

import org.apache.commons.lang3.StringUtils;

import cn.com.ite.eap2.common.utils.CharsetSwitchUtil;
import cn.com.ite.eap2.core.service.TreeNode;
import cn.com.ite.eap2.core.struts2.AbstractListAction;
import cn.com.ite.eap2.domain.power.SysUser;
import cn.com.ite.eap2.log.Log;
import cn.com.ite.eap2.log.LogFactory;
import cn.com.ite.eap2.module.power.login.LoginAction;
import cn.com.ite.eap2.module.power.login.UserSession;

/**
 * <p>Title cn.com.ite.eap2.module.power.user.UserListAction</p>
 * <p>Description 用户ListAction</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2014</p>
 * @author 宋文科
 * @create time: 2014-7-9 上午09:16:11
 * @version 2.0
 * 
 * @modified records:
 */
public class UserListAction extends AbstractListAction{
	private static final long serialVersionUID = -7244753779816555403L;
	/**
	 * 账号条件
	 */
	private String accountTerm;
	/**
	 * 员工条件 
	 */
	private String employeeTerm;
	/**
	 * 部门条件
	 */
	private String deptTerm;
	/**
	 * 机构条件
	 */
	private String organTerm;
	/**
	 * 角色条件
	 */
	private String roleTerm;
	/**
	 * 资源条件
	 */
	private String resourceTerm;
	/**
	 * 是否授权条件
	 */
	private String grantTerm;
	/**
	 * 是否有效条件
	 */
	private Boolean valid;
	private String validStr;
	/**
	 * 查询结果
	 */
	private List<SysUser> list = new ArrayList<SysUser>();
	private List<TreeNode> children;
	/**
	 * 列表查询方法
	 * @return
	 * @modified
	 */
	@SuppressWarnings("unchecked")
	public String list()throws Exception{
		if(!StringUtils.isEmpty(this.getValidStr()))
			this.setValid(this.getValidStr().equals("1"));
		list = (List<SysUser>)service.queryData("queryHql", this,null,this.getStart(),this.getLimit());
		this.setTotal(service.countData("queryHql", this));
		return "list";
	}
	public List<TreeNode> getChildren() {
		return children;
	}
	public void setChildren(List<TreeNode> children) {
		this.children = children;
	}
	/**
	 * 重置密码
	 * @return
	 * @throws Exception
	 */
	public String resetPassword()throws Exception{
		SysUser user = (SysUser)service.findDataByKey(this.getId(), SysUser.class);
		user.setPassword(CharsetSwitchUtil.encryptPassword("888888"));
		Log log = LogFactory.getLog();
		log.info("对用户\""+user.getAccount()+"\"进行密码重置！");
		service.update(user);		
		return "save";
	}
	/**
	 * 用户资源树
	 * @return
	 * @throws Exception
	 */
	public String userResourceTree()throws Exception{
		UserService userService = (UserService)service;
		children = userService.findUserResource(this.getId());
		return "userResourceTree";
	}
	/**
	 * 缺少查询条件
	 */
	private String nameTerm;
	public void setNameTerm(String nameTerm) {
		this.nameTerm = nameTerm;
	}
	/**
	 * 用户树
	 * @return
	 * @throws Exception
	 * @modified
	 */
	public String userTree() throws Exception{
		UserService userService = (UserService)service;
		children = userService.findUserTree(organTerm, nameTerm);
		return "userResourceTree";
	}
	/**
	 * 删除
	 * @return
	 * @modified
	 */
	public String delete() throws Exception{
		UserSession us = LoginAction.getUserSessionInfo();
		for(String userId:this.getId().split(","))
			if(us.getUserId().equals(userId))
				throw new Exception("不能删除自己的用户信息！");
		service.deleteByKeys(this.getId().split(","), SysUser.class);
		this.setMsg("用户删除成功！");
		return "delete";
	}
	public String getAccountTerm() {
		return accountTerm;
	}
	public void setAccountTerm(String accountTerm) {
		this.accountTerm = accountTerm;
	}
	public String getEmployeeTerm() {
		return employeeTerm;
	}
	public void setEmployeeTerm(String employeeTerm) {
		this.employeeTerm = employeeTerm;
	}
	public String getDeptTerm() {
		return deptTerm;
	}
	public void setDeptTerm(String deptTerm) {
		this.deptTerm = deptTerm;
	}
	public String getOrganTerm() {
		return organTerm;
	}
	public void setOrganTerm(String organTerm) {
		this.organTerm = organTerm;
	}
	public List<SysUser> getList() {
		return list;
	}
	public void setList(List<SysUser> list) {
		this.list = list;
	}

	public Boolean isValid() {
		return valid;
	}
	public Boolean getValid() {
		return valid;
	}
	public void setValid(Boolean valid) {
		this.valid = valid;
	}
	public String getValidStr() {
		return validStr;
	}
	public void setValidStr(String validStr) {
		this.validStr = validStr;
	}
	public void setRoleTerm(String roleTerm) {
		this.roleTerm = roleTerm;
	}
	public void setResourceTerm(String resourceTerm) {
		this.resourceTerm = resourceTerm;
	}
	public void setGrantTerm(String grantTerm) {
		this.grantTerm = grantTerm;
	}
	public String getRoleTerm() {
		return roleTerm;
	}
	public String getResourceTerm() {
		return resourceTerm;
	}
	public String getGrantTerm() {
		return grantTerm;
	}
	public String getNameTerm() {
		return nameTerm;
	}
}