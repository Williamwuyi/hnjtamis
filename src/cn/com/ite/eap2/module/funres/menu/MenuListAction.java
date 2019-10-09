package cn.com.ite.eap2.module.funres.menu;

import java.util.*;

import cn.com.ite.eap2.Config;
import cn.com.ite.eap2.core.struts2.AbstractListAction;
import cn.com.ite.eap2.domain.funres.AppMenu;
import cn.com.ite.eap2.domain.funres.MenuRecord;
import cn.com.ite.eap2.domain.funres.ModuleResource;
import cn.com.ite.eap2.module.power.login.LoginAction;
import cn.com.ite.eap2.module.power.login.UserSession;

/**
 * <p>Title cn.com.ite.eap2.module.funres.menu.MenuListAction</p>
 * <p>Description 应用系统ListAction</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2014</p>
 * @author 宋文科
 * @create time: 2014-6-24 下午01:27:22
 * @version 2.0
 * 
 * @modified records:
 */
public class MenuListAction extends AbstractListAction{
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
	 * 菜单类型条件
	 */
	private Integer meneTypeTerm;
	/**
	 * 树形数据
	 */
	private List<AppMenu> appMenus;
	private List<MenuRecord> quickList;
	@SuppressWarnings("unchecked")
	public String list() throws Exception{
		List querys = (List<AppMenu>)service.queryData("queryHql", this,this.getSortMap());
		Map copyFieldMap = new HashMap();
		copyFieldMap.put(new String[]{"moduleResource","resourceId","resourceName"}, ModuleResource.class);
		//按层次进行封装
		appMenus = service.childObjectHandler(querys, "menuId", "appMenu", "appMenus", 
				new String[]{"appSystem"},copyFieldMap,this.getFilterIds(),"orderNo",null);
		return "list";
	}
	/**
	 * 管理页面的查询快捷菜单
	 * @return
	 * @throws Exception
	 * @modified
	 */
	public String quickList() throws Exception{
		UserSession us = LoginAction.getUserSessionInfo();
		Map term = new HashMap();
	    term.put("userId", us.getUserId());
		quickList = (List<MenuRecord>)service.queryData("queryQuickMenuHql", term,null);
		return "quickList";
	}
	/**
	 * 子查询
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String subList()throws Exception{
		appMenus = (List<AppMenu>)service.queryData("querySubHql", this,null);
		return "subList";
	}
	/**
	 * 查询菜单记录
	 * @return
	 * @throws Exception
	 * @modified
	 */
	public String findMenuRecord()throws Exception{
		UserSession us = LoginAction.getUserSessionInfo();
		if(us!=null){
		    Map term = new HashMap();
		    term.put("userId", us.getUserId());
		    term.put("appId", us.getAppId());
		    List menuRecords = service.queryData("queryRecordHql", term, null);
		    Map maps = new LinkedHashMap();
		    int max = Integer.parseInt(Config.getPropertyValue("grabl_quick_menu_max"));
		    //先放10个最近访问的菜单
		    for(int i=0;i<(menuRecords.size()<max?menuRecords.size():max);i++){
		    	MenuRecord mr = (MenuRecord)menuRecords.get(i);
		    	maps.put(mr.getAppMenu().getMenuId(), mr.getAppMenu());
		    }
		    appMenus = new ArrayList();
		    appMenus.addAll(maps.values());
		}
		return "findMenuRecord";
	}
	/**
	 * 删除
	 * @return
	 * @modified
	 */
	public String delete() throws Exception{		
		List params = new ArrayList();
		String[] ids = this.getId().split(",");
		for(int i=0;i<ids.length;i++){
		   Map map = new HashMap();
		   map.put("menuId", ids[i]);
		   params.add(map);
		}
		service.excuteQl("deleteMenuRecordHql", params);
		service.deleteByKeys(this.getId().split(","), AppMenu.class);
		this.setMsg("菜单删除成功！");
		return "delete";
	}
	/**
	 * 快捷菜单删除
	 * @return
	 * @throws Exception
	 * @modified
	 */
	public String deleteQuick() throws Exception{
		service.deleteByKeys(this.getId().split(","), MenuRecord.class);
		this.setMsg("快捷菜单删除成功！");
		return "delete";
	}
	public String getNameTerm() {
		return nameTerm;
	}
	public void setNameTerm(String nameTerm) {
		this.nameTerm = nameTerm;
	}
	public String getAppTerm() {
		return appTerm;
	}
	public void setAppTerm(String appTerm) {
		this.appTerm = appTerm;
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
	public List<AppMenu> getAppMenus() {
		return appMenus;
	}
	public void setAppMenus(List<AppMenu> appMenus) {
		this.appMenus = appMenus;
	}
	public Integer getMeneTypeTerm() {
		return meneTypeTerm;
	}
	public void setMeneTypeTerm(Integer meneTypeTerm) {
		this.meneTypeTerm = meneTypeTerm;
	}
	public List<MenuRecord> getQuickList() {
		return quickList;
	}
	public void setQuickList(List<MenuRecord> quickList) {
		this.quickList = quickList;
	}
}