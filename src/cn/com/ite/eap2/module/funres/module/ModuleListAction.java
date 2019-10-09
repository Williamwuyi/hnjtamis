package cn.com.ite.eap2.module.funres.module;

import java.util.*;

import cn.com.ite.eap2.core.service.TreeNode;
import cn.com.ite.eap2.core.struts2.AbstractListAction;
import cn.com.ite.eap2.domain.funres.AppModule;
import cn.com.ite.eap2.domain.funres.AppSystem;

/**
 * <p>Title cn.com.ite.eap2.module.funres.module.ModuleListAction</p>
 * <p>Description 应用系统ListAction</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2014</p>
 * @author 宋文科
 * @create time: 2014-6-24 下午01:27:22
 * @version 2.0
 * 
 * @modified records:
 */
public class ModuleListAction extends AbstractListAction{
	private static final long serialVersionUID = -2652717947349804498L;
	/**
	 * 名称条件
	 */
	private String nameTerm;
	/**
	 * 系统条件
	 */
	private String appTerm;
	/**
	 * 资源编码条件
	 */
	private String resourceCodeTerm;
	/**
	 * 资源名称条件
	 */
	private String resourceNameTerm;
	/**
	 * 编码条件
	 */
	private String codeTerm;
	/**
	 * 树形数据，
	 */
	private List<AppModule> appModules;
	/**
	 * 
	 */
	private List<TreeNode> children;
	@SuppressWarnings("unchecked")
	public String list() throws Exception{
		List querys = (List<AppModule>)service.queryData("queryHql", this,this.getSortMap(),this.getStart(), this.getLimit());
		Map<String[],Class> copyFieldMap = new HashMap<String[],Class>();
		copyFieldMap.put(new String[]{"appSystem","appId","appName"}, AppSystem.class);
		//按层次进行封装
		String id = "moduleId";
		String parent = "appModule";
		String child = "appModules";
		appModules = service.childObjectHandler(querys, id, parent, child,
				new String[]{"moduleResources"},copyFieldMap,this.getFilterIds(),"orderNo",null);
		return "list";
	}
	/**
	 * 子查询
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String subList()throws Exception{
		appModules = (List<AppModule>)service.queryData("querySubHql", this,null);
		return "subList";
	}
	/**
	 * 显示模块资源树
	 * @return
	 */
	public String findMRTree() throws Exception{
		ModuleService moduleService = (ModuleService)service;
		children = moduleService.findMRTree(this.appTerm, this.nameTerm);
		return "findMRTree";
	}
	private String userId;
	private String roleId;
	private boolean proxy=false;
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	public boolean isProxy() {
		return proxy;
	}
	public void setProxy(boolean proxy) {
		this.proxy = proxy;
	}
	/**
	 * 显示系统模块资源树
	 * @return
	 */
	public String findAMRTree() throws Exception{
		ModuleService moduleService = (ModuleService)service;
		children = moduleService.findAMRTree(userId, roleId, proxy, nameTerm);
		return "findMRTree";
	}
	
	/**
	 * 删除
	 * @return
	 * @modified
	 */
	public String delete() throws Exception{
		service.deleteByKeys(this.getId().split(","), AppModule.class);
		this.setMsg("模块删除成功！");
		return "delete";
	}
	public String getNameTerm() {
		return nameTerm;
	}
	public void setNameTerm(String nameTerm) {
		this.nameTerm = nameTerm;
	}
	public List<AppModule> getAppModules() {
		return appModules;
	}
	public void setAppModules(List<AppModule> appModules) {
		this.appModules = appModules;
	}
	public String getAppTerm() {
		return appTerm;
	}
	public void setAppTerm(String appTerm) {
		this.appTerm = appTerm;
	}
	public String getCodeTerm() {
		return codeTerm;
	}
	public void setCodeTerm(String codeTerm) {
		this.codeTerm = codeTerm;
	}
	public String getResourceCodeTerm() {
		return resourceCodeTerm;
	}
	public void setResourceCodeTerm(String resourceCodeTerm) {
		this.resourceCodeTerm = resourceCodeTerm;
	}
	public String getResourceNameTerm() {
		return resourceNameTerm;
	}
	public void setResourceNameTerm(String resourceNameTerm) {
		this.resourceNameTerm = resourceNameTerm;
	}
	public List<TreeNode> getChildren() {
		return children;
	}
	public void setChildren(List<TreeNode> children) {
		this.children = children;
	}
}