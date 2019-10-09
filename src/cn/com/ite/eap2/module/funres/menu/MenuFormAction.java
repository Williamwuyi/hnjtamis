package cn.com.ite.eap2.module.funres.menu;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.ite.eap2.common.utils.StringUtils;
import cn.com.ite.eap2.core.struts2.AbstractFormAction;
import cn.com.ite.eap2.domain.funres.AppMenu;
import cn.com.ite.eap2.domain.funres.MenuRecord;
import cn.com.ite.eap2.module.power.login.LoginAction;
import cn.com.ite.eap2.module.power.login.UserSession;

/**
 * <p>Title cn.com.ite.eap2.module.funres.menu.MenuFormAction</p>
 * <p>Description 模块FormAction</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2014</p>
 * @author 宋文科
 * @create time: 2014-6-24 下午01:14:34
 * @version 2.0
 * 
 * @modified records:
 */
public class MenuFormAction extends AbstractFormAction{
	private static final long serialVersionUID = 5927931667526549415L;
	/**
	 * 表单数据
	 */
	private AppMenu form;
	private MenuRecord quick;
	
	/**
	 * 查询结果
	 * @return
	 * @modified
	 */
	public String find(){
		form = (AppMenu)service.findDataByKey(this.getId(), AppMenu.class);
		return "find";
	}
	/**
	 * 增加菜单记录
	 * @return
	 * @modified
	 */
	public String addMenuRecord()throws Exception{
		UserSession us = LoginAction.getUserSessionInfo();
		String menuId = this.getId();
		AppMenu menu = (AppMenu)service.findDataByKey(menuId, AppMenu.class);
		if(us!=null&&menu!=null){
		    String userId = LoginAction.getUserSessionInfo().getUserId();
		    Map term = new HashMap();
		    term.put("userId", userId);
		    term.put("menuId",menuId);
		    MenuRecord mr = new MenuRecord();
		    List ms = service.queryData("queryRecordByMenuHql", term, null);
		    if(ms.size()>0)
		    	mr = (MenuRecord)ms.get(0);
		    else{
		    	mr.setUserId(userId);
		    	mr.setAppMenu(menu);
		    }
		    mr.setUserSize((mr.getUserSize()==null?0:mr.getUserSize())+1);
		    mr.setOpenTime(new Date(System.currentTimeMillis()));
		    service.save(mr);
		}
		return "save";
	}
	/**
	 * 保存
	 * @return
	 * @throws Exception
	 * @modified
	 */
	public String save() throws Exception{
		form = (AppMenu)this.jsonToObject(AppMenu.class);
		if(StringUtils.isEmpty(form.getMenuId()))
		form.setOrderNo(1+service.getFieldMax(AppMenu.class, 
				"orderNo","appMenu.menuId",
				form.getAppMenu()!=null?form.getAppMenu().getMenuId():null));
		service.save(form);
		return "save";
	}
	public String saveQuick() throws Exception{
		quick = (MenuRecord)this.jsonToObject(MenuRecord.class);
		if(StringUtils.isEmpty(quick.getMrId()))
		quick.setUserSize(1);
		service.save(quick);
		return "save";
	}
	/**
	 * 排序保存
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String saveSort() throws Exception{
		List saves = new ArrayList();
		int index = 1;
		for(String id:this.getSortIds()){
			AppMenu am = (AppMenu)service.findDataByKey(id, AppMenu.class);
			am.setOrderNo(index++);
			saves.add(am);
		}
		service.saves(saves);
		return "save";
	}
	/**
	 * 快捷菜单保存排序
	 * @return
	 * @throws Exception
	 * @modified
	 */
	public String saveQuickSort() throws Exception{
		List saves = new ArrayList();
		int index = 1;
		for(String id:this.getSortIds()){
			MenuRecord am = (MenuRecord)service.findDataByKey(id, MenuRecord.class);
			am.setOrderNo(index++);
			saves.add(am);
		}
		service.saves(saves);
		return "save";
	}
	public AppMenu getForm() {
		return form;
	}
	public void setForm(AppMenu form) {
		this.form = form;
	}
	public MenuRecord getQuick() {
		return quick;
	}
	public void setQuick(MenuRecord quick) {
		this.quick = quick;
	}
}