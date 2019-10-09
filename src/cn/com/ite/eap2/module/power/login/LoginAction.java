package cn.com.ite.eap2.module.power.login;

import java.util.*;

import org.apache.commons.lang3.StringUtils;

import cn.com.ite.eap2.Config;
import cn.com.ite.eap2.common.utils.MakePwd;
import cn.com.ite.eap2.core.service.TreeNode;
import cn.com.ite.eap2.core.struts2.AbstractAction;
import cn.com.ite.eap2.core.struts2.ServletContent;
import cn.com.ite.eap2.domain.funres.AppSystem;
import cn.com.ite.eap2.domain.organization.Dept;
import cn.com.ite.eap2.domain.organization.Organ;
import cn.com.ite.eap2.domain.power.SysUser;
import cn.com.ite.eap2.log.Log;
import cn.com.ite.eap2.log.LogFactory;

/**
 * <p>Title cn.com.ite.eap2.module.power.login.LoginAction</p>
 * <p>Description 登录Action</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2014</p>
 * @author 宋文科
 * @create time: 2014-7-14 上午08:59:39
 * @version 2.0
 * 
 * @modified records:
 */
public class LoginAction extends AbstractAction{
	private static final long serialVersionUID = 2550161004882545340L;
	/**
	 * 用户session
	 */
	private UserSession userSession;
	/**
	 * 系统菜单
	 */
	private List<TreeNode> appMenus;
	/**
	 * 系统下拉数据
	 */
	private List<AppSystem> apps;
	/**
	 * 系统ID
	 */
	private String appId;
	/**
	 * 被代理用户下拉数据
	 */
	@SuppressWarnings("unchecked")
	private List<Map> proxyEmployee = new ArrayList<Map>();
	/**
	 * 是否代理模式
	 */
	private Boolean proxy;
	/**
	 * 账号
	 */
	private String account;
	/**
	 * 密码
	 */
	private String password;
	
	/**
	 * 外部来源账号
	 */
	private String uid;
	
	/**
	 * 外部来源密码
	 */
	private String pwd;//
	/**
	 * 被代理用户
	 */
	private String byProxyUserId;
	private boolean encrypt=false;
	public void setEncrypt(boolean encrypt) {
		this.encrypt = encrypt;
	}
	/**
	 * 平台session仓库
	 */
	private static Map<String,UserSession> userSessionMap = new HashMap<String,UserSession>();
	private String type;//机构还是部门类型
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	public String queryPwd(){
		pwd = MakePwd.getPwd(appId,password);//来自外部的一种加密方式，并进行验证
		return "queryPwd";
	}
	
	
	/**
	 * 系统登录
	 * @return
	 * @throws Exception
	 * @modified
	 */
	@SuppressWarnings("unchecked")
	public String login() throws Exception{
		LoginService loginService = (LoginService)this.getService();
		SysUser user = null;
		if(encrypt)
			user = loginService.judgeUserPassForEncrypt(account, password);
		else
			user = loginService.judgeUserPass(account, password);
		SysUser proxyUser = user;//如果是代理模式，则登录者为代理人
		if(user==null)
			throw new Exception("账号或密码不正确！");
		if(proxy==null) proxy = false;
		if(proxy){
		   if(StringUtils.isEmpty(byProxyUserId)){
			   List<SysUser> proxyUsers = loginService.getByProxyUser(user.getUserId());
			   if(proxyUsers.size()==0)
				   throw new Exception("你不存在被代理的对象！");
			   if(proxyUsers.size()==1)
				   user = (SysUser)proxyUsers.get(0);
			   else{
				   for(SysUser su:proxyUsers){
					   Map map = new HashMap();
					   map.put("userId", su.getUserId());
					   map.put("userName", su.getEmployee().getEmployeeName());
					   proxyEmployee.add(map);
				   }
				   return "proxy_select";//存在多个被代理对象，则需要选择
			   }
		   }else{
			   user = (SysUser)loginService.findDataByKey(byProxyUserId, SysUser.class);
			   if(user==null)
				   throw new Exception("此被代理用户不存在！");
		   }
		}
		userSession = loginService.getSession(user, proxy, appId);
		if(proxy){
			userSession.setProxyUserId(proxyUser.getUserId());
			userSession.setProxyUserName(proxyUser.getAccount());
			userSession.setEmployeeName(userSession.getEmployeeName()+
					"(由\""+userSession.getProxyUserName()+"\"代理)");
		}
		if(userSession.getAuthoritys().size()==0)
			throw new Exception("此用户\""+userSession.getAccount()+"\"无任何权限！");
		userSession.setPasswordUpdateTip(user.getTig()&&password.equals("888888"));
		userSession.setSessionId(ServletContent.getSessionId());
		ServletContent.putSession("USER_SESSION", userSession);
		if(!user.getAllowRepeatLogin()){//重复登录判断
			for(String sessionId:userSessionMap.keySet()){
				UserSession oldUs = userSessionMap.get(sessionId);
				if(oldUs.getUserId().equals(user.getUserId())){
					oldUs.setRppeatLogin(true);
				}					
			}
		}
		userSessionMap.put(ServletContent.getSessionId(), userSession);
		Log log = LogFactory.getLog();
		log.login("用户\""+userSession.getAccount()+"\"登录系统！");
		return "login";
	}
	public String ssoLogin() throws Exception{
		LoginService loginService = (LoginService)this.getService();
		SysUser user = null;
		account = uid;
		Map term = new HashMap();
		term.put("account", account);
		List<SysUser> userlist = loginService.queryData("queryUserByAccount", term, null);
		if(userlist!=null && userlist.size()>0){
			user = userlist.get(0);
			password = user.getPassword();
			String sysKey = "";
			try{
				sysKey = user.getOrgan().getSysParemeter();
			}catch(Exception e){
				sysKey = "";
				e.printStackTrace();
			}
			if(sysKey==null)sysKey = "";
			String newPwd = MakePwd.getPwd(sysKey,password);//来自外部的一种加密方式，并进行验证
			if(newPwd==null || !newPwd.equals(pwd)){
				throw new Exception("账号或密码不正确！");
			}
		}else{
			throw new Exception("账号或密码不正确！");
		}

		SysUser proxyUser = user;//如果是代理模式，则登录者为代理人
		if(user==null)
			throw new Exception("账号或密码不正确！");
		if(proxy==null) proxy = false;
		if(proxy){
		   if(StringUtils.isEmpty(byProxyUserId)){
			   List<SysUser> proxyUsers = loginService.getByProxyUser(user.getUserId());
			   if(proxyUsers.size()==0)
				   throw new Exception("你不存在被代理的对象！");
			   if(proxyUsers.size()==1)
				   user = (SysUser)proxyUsers.get(0);
			   else{
				   for(SysUser su:proxyUsers){
					   Map map = new HashMap();
					   map.put("userId", su.getUserId());
					   map.put("userName", su.getEmployee().getEmployeeName());
					   proxyEmployee.add(map);
				   }
				   return "proxy_select";//存在多个被代理对象，则需要选择
			   }
		   }else{
			   user = (SysUser)loginService.findDataByKey(byProxyUserId, SysUser.class);
			   if(user==null)
				   throw new Exception("此被代理用户不存在！");
		   }
		}
		userSession = loginService.getSession(user, proxy, appId);
		if(proxy){
			userSession.setProxyUserId(proxyUser.getUserId());
			userSession.setProxyUserName(proxyUser.getAccount());
			userSession.setEmployeeName(userSession.getEmployeeName()+
					"(由\""+userSession.getProxyUserName()+"\"代理)");
		}
		if(userSession.getAuthoritys().size()==0)
			throw new Exception("此用户\""+userSession.getAccount()+"\"无任何权限！");
		userSession.setPasswordUpdateTip(user.getTig()&&password.equals("888888"));
		userSession.setSessionId(ServletContent.getSessionId());
		ServletContent.putSession("USER_SESSION", userSession);
		if(!user.getAllowRepeatLogin()){//重复登录判断
			for(String sessionId:userSessionMap.keySet()){
				UserSession oldUs = userSessionMap.get(sessionId);
				if(oldUs.getUserId().equals(user.getUserId())){
					oldUs.setRppeatLogin(true);
				}					
			}
		}
		userSessionMap.put(ServletContent.getSessionId(), userSession);
		Log log = LogFactory.getLog();
		log.login("用户\""+userSession.getAccount()+"\"登录系统！");
		return "login";
	}
	
	public String clientLogin() throws Exception{
		LoginService loginService = (LoginService)this.getService();
		SysUser user = null;
		if(encrypt)
			user = loginService.judgeUserPassForEncrypt(account, password);
		else
			user = loginService.judgeUserPass(account, password);
		if(user==null)
			throw new Exception("账号或密码不正确！");
		if(user.getApp()==null)
			throw new Exception("此用户不存在对应的系统可登录！");
		userSession = loginService.getSession(user, false, user.getApp().getAppId());
		if(userSession.getAuthoritys().size()==0)
			throw new Exception("此用户\""+userSession.getAccount()+"\"无任何权限！");
		userSession.setSessionId(ServletContent.getSessionId());
		return "clientLogin";
	}
	/**
	 * 查询用户session,平台使用
	 * @return
	 * @modified
	 */
	public String findUserSession(){
		String loginEnable = Config.getPropertyValue("login.enable");
		LoginService loginService = (LoginService)this.getService();
		userSession = getUserSessionInfo();
		if(userSession==null&&"false".equals(loginEnable)&&ServletContent.getSession().get("quit")==null){//以guest登录
			SysUser user = (SysUser)loginService.findDataByKey("guest", SysUser.class);
			if(user!=null){
			  userSession = loginService.getSession(user, false, (user.getApp()!=null?user.getApp().getAppId():null));
			  ServletContent.putSession("USER_SESSION", userSession);
			}
		}
		ServletContent.getSession().remove("quit");
		return "findUserSession";
	}
	/**
	 * 平台sessionID
	 */
	private String sessionId;
	/**
	 * 获得平台SESSION，应用使用，需要传入参数sessionId
	 */
	public String findUSOfEap(){
		userSession = userSessionMap.get(sessionId);
		return "findUSOfEap";
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	/**
	 * 获取菜单
	 * @return
	 * @modified
	 */
	public String findMenu()throws Exception{
		UserSession us = getUserSessionInfo();
		LoginService loginService = (LoginService)this.getService();
		if(us==null)
			throw new Exception("用户未登录！");
		SysUser user = (SysUser)loginService.findDataByKey(us.getUserId(), SysUser.class);
		try{
		   appMenus = loginService.findMainMenu(user, us.getProxyUserId()!=null, us.getAppId());
		}catch(Exception e){
			e.printStackTrace();
		}
		return "findMenu";
	}
	/**
	 * 切换系统
	 * @return
	 * @throws Exception
	 */
	public String switchSystem() throws Exception{
		UserSession us = getUserSessionInfo();
		if(us==null)
			throw new Exception("在线用户信息失效！请重新登录！");
		LoginService loginService = (LoginService)this.getService();
		AppSystem app = (AppSystem)loginService.findDataByKey(appId, AppSystem.class);
		us.setAppId(app.getAppId());
		us.setAppName(app.getAppName());
		us.setTheme(app.getTheme());
		us.setBasePath(app.getAppUrl());
		return "login";
	}
	@SuppressWarnings("unused")
	private String deptId;//切换的部门ID
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
	/**
	 * 机构部门切换
	 * @return
	 * @throws Exception
	 */
	public String switchOt() throws Exception{
		UserSession us = getUserSessionInfo();
		if(us==null)
			throw new Exception("在线用户信息失效！请重新登录！");
		LoginService loginService = (LoginService)this.getService();
		if(this.type.equals("dept")){
			Dept dept = (Dept)loginService.findDataByKey(this.getId(), Dept.class);
			Organ organ = dept.getOrgan();
			us.setCurrentDeptId(dept.getDeptId());
			us.setCurrentDeptName(dept.getDeptName());
			us.setCurrentOrganId(organ.getOrganId());
			us.setCurrentOrganName(organ.getOrganName());
			us.setCurrentOrganCode(organ.getOrganCode());
		}else{
			Organ organ = (Organ)loginService.findDataByKey(this.getId(), Organ.class);
			Dept dept = null;
			for(Dept d:organ.getDepts()){
				if(d.getDept()==null){
					dept = d;
					break;
				}
			}
			if(dept!=null){
			   us.setCurrentDeptId(dept.getDeptId());
			   us.setCurrentDeptName(dept.getDeptName());
			}else{
			   us.setCurrentDeptId("");
			   us.setCurrentDeptName("");
			}
			us.setCurrentOrganId(organ.getOrganId());
			us.setCurrentOrganName(organ.getOrganName());
			us.setCurrentOrganCode(organ.getOrganCode());
		}
		return "login";
	}
	/**
	 * 注销或退出
	 * @return
	 * @modified
	 */
	public String quit(){
		UserSession us = getUserSessionInfo();
		userSessionMap.remove(ServletContent.getSessionId());
		Log log = LogFactory.getLog();
		log.quit("用户\""+us.getAccount()+"\"退出系统！");
		ServletContent.getSession().remove("USER_SESSION");
		ServletContent.getSession().put("quit", true);
		return "quit";
	}
	/**
	 * 获取系统切换数据
	 * @return
	 * @modified
	 */
	public String appList(){
		UserSession us = getUserSessionInfo();
		LoginService loginService = (LoginService)this.getService();
		if(us!=null){
			apps = loginService.getAppList(us.getUserId());
			if(StringUtils.isEmpty(us.getAppId())&&apps.size()>0){
				AppSystem  as = (AppSystem)apps.get(0);
				us.setAppId(as.getAppId());
				us.setAppName(as.getAppName());
				us.setTheme(as.getTheme());
			}
		}
		return "appList";
	}
	/**
	 * 获得用户session
	 * @return
	 * @modified
	 */
	public static UserSession getUserSessionInfo(){
		UserSession us =  (UserSession)ServletContent.getSession().get("USER_SESSION");
		return us;
	}
	/**
	 * 获得用户session
	 * @return
	 * @modified
	 */
	public static UserSession getUserSessionInfo(String sessionId){
		return userSessionMap.get(sessionId);
	}
	public UserSession getUserSession() {
		return userSession;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public void setUserSession(UserSession userSession) {
		this.userSession = userSession;
	}
	public List<AppSystem> getApps() {
		return apps;
	}
	public void setApps(List<AppSystem> apps) {
		this.apps = apps;
	}
	@SuppressWarnings("unchecked")
	public List<Map> getProxyEmployee() {
		return proxyEmployee;
	}
	@SuppressWarnings("unchecked")
	public void setProxyEmployee(List<Map> proxyEmployee) {
		this.proxyEmployee = proxyEmployee;
	}
	public Boolean getProxy() {
		return proxy;
	}
	public void setProxy(Boolean proxy) {
		this.proxy = proxy;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public List<TreeNode> getAppMenus() {
		return appMenus;
	}
	public void setAppMenus(List<TreeNode> appMenus) {
		this.appMenus = appMenus;
	}
	public String getByProxyUserId() {
		return byProxyUserId;
	}
	public void setByProxyUserId(String byProxyUserId) {
		this.byProxyUserId = byProxyUserId;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	
}