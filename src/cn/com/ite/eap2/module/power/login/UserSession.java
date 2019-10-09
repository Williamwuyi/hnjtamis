package cn.com.ite.eap2.module.power.login;

import java.util.*;

/**
 * <p>Title cn.com.ite.eap2.module.power.login.UserSession</p>
 * <p>Description 用户session</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2014</p>
 * @author 宋文科
 * @create time: 2014-7-14 上午09:01:44
 * @version 2.0
 * 
 * @modified records:
 */
public class UserSession {
	/**
	 * 用户ID
	 */
	private String userId;
	/**
	 * 账号
	 */
	private String account;
	/**
	 * 员工ID
	 */
	private String employeeId;
	/**
	 * 员工名称
	 */
	private String employeeName;
	/**
	 * 员工编码
	 */
	private String employeeCode;
	/**
	 * 岗位ID
	 */
	private String quarterId;
	/**
	 * 岗位名称
	 */
	private String quarterName;
	/**
	 * 部门ID
	 */
	private String deptId;
	private String currentDeptId;
	/**
	 * 部门名称
	 */
	private String deptName;
	private String currentDeptName;
	/**
	 * 机构ID
	 */
	private String organId;
	private String currentOrganId;
	/**
	 * 机构CODE
	 */
	private String organCode;
	private String currentOrganCode;
	/**
	 * 机构名称
	 */
	private String organName;
	private String currentOrganName;
	private String organAlias;
	private String currentOrganAlias;
	public String getOrganAlias() {
		return organAlias;
	}
	public void setOrganAlias(String organAlias) {
		this.organAlias = organAlias;
	}
	public String getCurrentOrganAlias() {
		return currentOrganAlias;
	}
	public void setCurrentOrganAlias(String currentOrganAlias) {
		this.currentOrganAlias = currentOrganAlias;
	}
	/**
	 * 角色id
	 */
	private String roleIds;
	/**
	 * 角色编码
	 */
	private String roleCodes;
	/**
	 * 角色名称
	 */
	private String roleNames;
	
	
	/**
	 * 模块ID
	 */
	private String moduleId;
	/**
	 * 模块名称
	 */
	private String moduleName;
	/**
	 * 系统ID
	 */
	private String appId;
	/**
	 * 系统名称
	 */
	private String appName;
	/**
	 * 权限<资源编码>
	 */
	private List<String> authoritys = new ArrayList<String>();
	/**
	 * 权限名称<模块名称>
	 */
	private List<String> authorityNames = new ArrayList<String>();
	/**
	 * 权限<资源地址>
	 */
	private List<String> authUrls = new ArrayList<String>();
	/**
	 * 没有权限<资源地址>
	 */
	private List<String> noAuthUrls = new ArrayList<String>();
	/**
	 * 代理用户ID
	 */
	private String proxyUserId;
	/**
	 * 代理用户名称
	 */
	private String proxyUserName;
	/**
	 * 是否重复登录
	 */
	private boolean rppeatLogin = false;
	/**
	 * 是否缺省密码修改提示！
	 */
	private boolean passwordUpdateTip = false;
	/**
	 * 主题
	 */
	private String theme;
	/**
	 * 系统地址
	 */
	private String basePath;
	private String indexUrl;
	private String sessionId;
	/**
	 * 是否管理员
	 */
	private boolean manger;
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public String getBasePath() {
		return basePath;
	}
	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}
	public String getTheme() {
		return theme;
	}
	public void setTheme(String theme) {
		this.theme = theme;
	}
	public List<String> getNoAuthUrls() {
		return noAuthUrls;
	}
	public void setNoAuthUrls(List<String> noAuthUrls) {
		this.noAuthUrls = noAuthUrls;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	public String getEmployeeName() {
		return employeeName;
	}
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
	public String getIndexUrl() {
		return indexUrl;
	}
	public void setIndexUrl(String indexUrl) {
		this.indexUrl = indexUrl;
	}
	public String getQuarterId() {
		return quarterId;
	}
	public void setQuarterId(String quarterId) {
		this.quarterId = quarterId;
	}
	public String getQuarterName() {
		return quarterName;
	}
	public void setQuarterName(String quarterName) {
		this.quarterName = quarterName;
	}
	public String getDeptId() {
		return deptId;
	}
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getOrganId() {
		return organId;
	}
	public void setOrganId(String organId) {
		this.organId = organId;
	}
	public String getOrganName() {
		return organName;
	}
	public void setOrganName(String organName) {
		this.organName = organName;
	}
	public String getModuleId() {
		return moduleId;
	}
	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}
	public String getModuleName() {
		return moduleName;
	}
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public List<String> getAuthoritys() {
		return authoritys;
	}
	public void setAuthoritys(List<String> authoritys) {
		this.authoritys = authoritys;
	}
	public String getProxyUserId() {
		return proxyUserId;
	}
	public void setProxyUserId(String proxyUserId) {
		this.proxyUserId = proxyUserId;
	}
	public String getProxyUserName() {
		return proxyUserName;
	}
	public void setProxyUserName(String proxyUserName) {
		this.proxyUserName = proxyUserName;
	}
	public List<String> getAuthUrls() {
		return authUrls;
	}
	public void setAuthUrls(List<String> authUrls) {
		this.authUrls = authUrls;
	}
	public String getCurrentDeptId() {
		return currentDeptId;
	}
	public void setCurrentDeptId(String currentDeptId) {
		this.currentDeptId = currentDeptId;
	}
	public String getCurrentDeptName() {
		return currentDeptName;
	}
	public void setCurrentDeptName(String currentDeptName) {
		this.currentDeptName = currentDeptName;
	}
	public String getCurrentOrganId() {
		return currentOrganId;
	}
	public void setCurrentOrganId(String currentOrganId) {
		this.currentOrganId = currentOrganId;
	}
	public String getCurrentOrganName() {
		return currentOrganName;
	}
	public void setCurrentOrganName(String currentOrganName) {
		this.currentOrganName = currentOrganName;
	}
	public List<String> getAuthorityNames() {
		return authorityNames;
	}
	public void setAuthorityNames(List<String> authorityNames) {
		this.authorityNames = authorityNames;
	}
	public boolean isRppeatLogin() {
		return rppeatLogin;
	}
	public boolean getRppeatLogin() {
		return rppeatLogin;
	}
	public void setRppeatLogin(boolean rppeatLogin) {
		this.rppeatLogin = rppeatLogin;
	}
	public boolean isPasswordUpdateTip() {
		return passwordUpdateTip;
	}
	public boolean getPasswordUpdateTip() {
		return passwordUpdateTip;
	}
	public void setPasswordUpdateTip(boolean passwordUpdateTip) {
		this.passwordUpdateTip = passwordUpdateTip;
	}
	public String getOrganCode() {
		return organCode;
	}
	public void setOrganCode(String organCode) {
		this.organCode = organCode;
	}
	public String getCurrentOrganCode() {
		return currentOrganCode;
	}
	public void setCurrentOrganCode(String currentOrganCode) {
		this.currentOrganCode = currentOrganCode;
	}
	public String getEmployeeCode() {
		return employeeCode;
	}
	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}
	public String getRoleIds() {
		return roleIds;
	}
	public void setRoleIds(String roleIds) {
		this.roleIds = roleIds;
	}
	public String getRoleCodes() {
		return roleCodes;
	}
	public void setRoleCodes(String roleCodes) {
		this.roleCodes = roleCodes;
	}
	public String getRoleNames() {
		return roleNames;
	}
	public void setRoleNames(String roleNames) {
		this.roleNames = roleNames;
	}
	public boolean isManger() {
		return manger;
	}
	public void setManger(boolean manger) {
		this.manger = manger;
	}	
}