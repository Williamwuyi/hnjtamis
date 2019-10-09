package cn.com.ite.eap2.module.funres.module;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.ite.eap2.common.utils.StringUtils;
import cn.com.ite.eap2.core.struts2.AbstractFormAction;
import cn.com.ite.eap2.domain.funres.AppModule;
import cn.com.ite.eap2.domain.funres.ModuleResource;
import cn.com.ite.eap2.module.power.PopedomUtils;
import cn.com.ite.eap2.module.power.login.LoginAction;

/**
 * <p>Title cn.com.ite.eap2.module.funres.module.ModuleFormAction</p>
 * <p>Description 模块FormAction</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2014</p>
 * @author 宋文科
 * @create time: 2014-6-24 下午01:14:34
 * @version 2.0
 * 
 * @modified records:
 */
public class ModuleFormAction extends AbstractFormAction{
	private static final long serialVersionUID = 5927931667526549415L;
	/**
	 * 表单数据
	 */
	private AppModule form;
	
	/**
	 * 查询结果
	 * @return
	 * @modified
	 */
	public String find(){
		form = (AppModule)service.findDataByKey(this.getId(), AppModule.class);
		return "find";
	}
	/**
	 * 保存
	 * @return
	 * @throws Exception
	 * @modified
	 */
	public String save() throws Exception{
		form = (AppModule)this.jsonToObject(AppModule.class);
		String userId = LoginAction.getUserSessionInfo().getUserId();
		int index = 0;
		//当前保存的地址，也不能出现左相似
		for(int i=0;i<form.getModuleResources().size();i++){
			ModuleResource mr = form.getModuleResources().get(i);
			for(int j=0;j<form.getModuleResources().size();j++){
				ModuleResource mr2 = form.getModuleResources().get(j);
				if(i==j) continue;
				if(PopedomUtils.compareLike(mr.getResourceUrl(),mr2.getResourceUrl())||
				   PopedomUtils.compareLike(mr2.getResourceUrl(),mr.getResourceUrl()))
					throw new Exception("'"+mr.getResourceUrl()+"'与'"
							+mr2.getResourceUrl()+"'存在左相似的错误！");
			}
		}
		//地址与数据库中的地址进行检验，不能出现左相似
		for(ModuleResource mr : form.getModuleResources()){
			mr.setAppModule(form);
			if(!userId.equals("admin"))//只有超级管理员才有权限改这个值
			   mr.setPopedomType(2);
			mr.setOrderNo(index++);
			Map term = new HashMap();
			term.put("appId", form.getAppSystem().getAppId());
			for(String url:mr.getResourceUrl().split("\\|")){
				term.put("url", url);
				int c = service.queryData("urlUnique", term, null).size();
				if((StringUtils.isEmpty(mr.getResourceId())&&c>0)||
				  (!StringUtils.isEmpty(mr.getResourceId())&&c>1))
					throw new Exception(mr.getResourceUrl()+"此地址在本系统已经存在或左相似！");
			}
		}
		if(StringUtils.isEmpty(form.getModuleId()))
		form.setOrderNo(1+service.getFieldMax(AppModule.class, 
				"orderNo","appModule.moduleId",
				form.getAppModule()==null?null:form.getAppModule().getModuleId()));
		try{
		   ((ModuleService)service).saveModule(form);
		}catch(Exception e){
			e.printStackTrace();
			throw e;
		}
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
		int index = 0;
		for(String id:this.getSortIds()){
			AppModule am = (AppModule)service.findDataByKey(id, AppModule.class);
			am.setOrderNo(index++);
			saves.add(am);
		}
		service.saves(saves);
		return "save";
	}
	public AppModule getForm() {
		return form;
	}
	public void setForm(AppModule form) {
		this.form = form;
	}
}