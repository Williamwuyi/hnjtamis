package cn.com.ite.eap2.domain.power;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.struts2.json.annotations.JSON;

import cn.com.ite.eap2.domain.funres.AppSystem;
import cn.com.ite.eap2.domain.funres.ModuleResource;
import cn.com.ite.eap2.domain.organization.Dept;
import cn.com.ite.eap2.domain.organization.Employee;
import cn.com.ite.eap2.domain.organization.Organ;

/**
 * <p>Title cn.com.ite.eap2.domain.power.SysUser</p>
 * <p>Description 系统用户</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2014</p>
 * @author 宋文科
 * @create time: 2014-7-2 下午04:10:12
 * @version 2.0
 * 
 * @modified records:
 */
public class SysUser implements java.io.Serializable {
	private static final long serialVersionUID = -5109382324272808989L;
	private String userId;
	private Employee employee;
	private AppSystem app;
	private String account;
	private String password;
	private Integer orderNo;
	private boolean allowRepeatLogin;
	private boolean tig;
	private boolean validation;
	private Organ organ;
	private Employee proxyEmployee;
	private Date proxyDate;
	private String theme;
	
	private Set<SysRole> userRoles = new HashSet<SysRole>(0);
	private Set<UserMapp> userMapps = new HashSet<UserMapp>(0);
	private Set<AppSystem> sysMangers = new HashSet<AppSystem>(0);
	private Set<ModuleResource> proxyResources = new HashSet<ModuleResource>(0);
	/** default constructor */
	public SysUser() {
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public Employee getEmployee() {
		return employee;
	}
	public Dept getDept(){
		if(getEmployee()!=null)
			return getEmployee().getDept();
		else
			return null;
	}
	/**
	 * 是否授权
	 * @return
	 * @modified
	 */
	public boolean getGrant(){
		return this.getSysMangers().size()>0||this.getUserRoles().size()>0;
	}
	public void setEmployee(Employee employee) {
		this.employee = employee;
	}
	public AppSystem getApp() {
		return app;
	}
	public void setApp(AppSystem app) {
		this.app = app;
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
	public Integer getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}
	public boolean isAllowRepeatLogin() {
		return allowRepeatLogin;
	}
	public boolean getAllowRepeatLogin() {
		return allowRepeatLogin;
	}
	public void setAllowRepeatLogin(boolean allowRepeatLogin) {
		this.allowRepeatLogin = allowRepeatLogin;
	}
	public boolean isTig() {
		return tig;
	}
	public boolean getTig() {
		return tig;
	}
	public void setTig(boolean tig) {
		this.tig = tig;
	}
	public Organ getOrgan() {
		return organ;
	}
	public void setOrgan(Organ organ) {
		this.organ = organ;
	}
	public Set<SysRole> getUserRoles() {
		return userRoles;
	}
	public void setUserRoles(Set<SysRole> userRoles) {
		this.userRoles = userRoles;
	}
	public Set<UserMapp> getUserMapps() {
		return userMapps;
	}
	public void setUserMapps(Set<UserMapp> userMapps) {
		this.userMapps = userMapps;
	}
	public Set<AppSystem> getSysMangers() {
		return sysMangers;
	}
	public void setSysMangers(Set<AppSystem> sysMangers) {
		this.sysMangers = sysMangers;
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
	public Employee getProxyEmployee() {
		return proxyEmployee;
	}
	public void setProxyEmployee(Employee proxyEmployee) {
		this.proxyEmployee = proxyEmployee;
	}
	@JSON(format="yyyy-MM-dd")
	public Date getProxyDate() {
		return proxyDate;
	}
	public void setProxyDate(Date proxyDate) {
		this.proxyDate = proxyDate;
	}
	public Set<ModuleResource> getProxyResources() {
		return proxyResources;
	}
	public void setProxyResources(Set<ModuleResource> proxyResources) {
		this.proxyResources = proxyResources;
	}
	public String getTheme() {
		return theme;
	}
	public void setTheme(String theme) {
		this.theme = theme;
	}
}