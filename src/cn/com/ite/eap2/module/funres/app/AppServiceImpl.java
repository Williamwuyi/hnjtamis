package cn.com.ite.eap2.module.funres.app;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.ite.eap2.common.utils.XlsUtils;
import cn.com.ite.eap2.core.service.DefaultServiceImpl;
import cn.com.ite.eap2.domain.baseinfo.Dictionary;
import cn.com.ite.eap2.domain.baseinfo.DictionaryType;
import cn.com.ite.eap2.domain.funres.AppMenu;
import cn.com.ite.eap2.domain.funres.AppModule;
import cn.com.ite.eap2.domain.funres.AppSystem;
import cn.com.ite.eap2.domain.funres.ModuleResource;


/**
 * <p>Title cn.com.ite.eap2.module.funres.app.AppServiceImpl</p>
 * <p>Description 系统服务实现类</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2014</p>
 * @author 宋文科
 * @create time: 2014-7-23 下午03:21:07
 * @version 2.0
 * 
 * @modified records:
 */
public class AppServiceImpl extends DefaultServiceImpl implements AppService {
	//系统的输出字段名
	private String[] appCols = new String[]{"appId","appName","appCode","hasorgan",
			"haspopedom","hasswaraj","appUrl","smallPic","theme","orderNo"};
	private String[] moduleCols = new String[]{"moduleId","appModule.moduleId",
			"appSystem.appId","moduleCode","moduleName","description","orderNo"};
	private String[] resourceCols = new String[]{"resourceId","appModule.moduleId","resourceCode",
			"resourceName","resourceType","resourceUrl","orderNo","popedomType","bigIcon","icon"};
	private String[] menuCols = new String[]{"menuId","appSystem.appId","moduleResource.resourceId",
			"appMenu.menuId","menuName","tabName","target","otherUrl","hidden","menuType","orderNo","description"};
	private String[] dictionaryTypeCols = new String[]{"dtId","dictionaryType.dtId","dtName",
			"dtCode","sysType","orderNo","remark"};
	private String[] dictionaryCols = new String[]{"dicId","dictionaryType.dtId","dataName",
			"dataKey","description"};
	
	/**
	 * 导出系统模块资源菜单数据字典
	 * @param appId
	 * @return
	 * @modified
	 */
	@SuppressWarnings("unchecked")
	public File exportDate(String appId) throws Exception {
		AppSystem app = (AppSystem)getDao().findEntityBykey(appId);
		List apps = new ArrayList();
		apps.add(app);
		File xls = File.createTempFile("system", "xls");
		XlsUtils utils = XlsUtils.createWrite(xls);
		String[] appTitles = new String[]{"系统ID","系统名称","系统编码","是否独立机构",
				"是否独立权限","是否独立系统","系统地址","大图标","主题","排序"};
		utils.write(0,"系统", apps, appCols, appTitles);
		String[] moduleTitles = new String[]{"模块ID","父模块ID",
				"系统ID","模块编码","模块名称","模块描述","模块顺序"};
		utils.write(1,"模块", app.getAppModules(), moduleCols, moduleTitles);
		List resources = new ArrayList();
		for(AppModule mr:app.getAppModules()){
			resources.addAll(mr.getModuleResources());
		}
		String[] resourceTitles = new String[]{"资源ID","模块ID","资源编码",
				"资源名称","资源类型","资源地址","资源顺序","权限类型","大图标","图标"};
		utils.write(2,"功能资源", resources, resourceCols, resourceTitles);
		String[] menuTitles = new String[]{"菜单ID","系统ID","资源ID",
				"父菜单ID","菜单名称","标签名称","访问方式","外部地址","是否隐藏","菜单类型","菜单排序","菜单描述"};
		utils.write(3,"菜单", app.getAppMenus(), menuCols, menuTitles);
		String[] dictionaryTypeTitles = new String[]{"字典类型ID","父字典类型ID","字典类型名称",
				"字典类型编码","系统类型","字典类型排序","字典类型描述"};
		List dts = app.getDictionaryTypes();
		List cascadeDts = new ArrayList();
		this.cascadeAddDicType(cascadeDts, dts);
		utils.write(4,"字典类型", cascadeDts, dictionaryTypeCols, dictionaryTypeTitles);
		String[] dictionaryTitles = new String[]{"字典数据ID","字典类型ID","字典数据名称",
				"字典数据编码","字典数据描述"};
		List dics = new ArrayList();
		for(DictionaryType dt:(List<DictionaryType>)cascadeDts){
			dics.addAll(dt.getDictionaries());
		}
		utils.write(5,"字典数据", dics, dictionaryCols, dictionaryTitles);
		utils.closeWrite();
		return xls;
	}
	/**
	 * 把字典类型下的所有类型加到数组中
	 * @param cascadeDts 要加的数组
	 * @param dts 子字典类型集
	 * @modified
	 */
	@SuppressWarnings("unchecked")
	private void cascadeAddDicType(List cascadeDts,List dts){
		cascadeDts.addAll(dts);
		for(int i=0;i<dts.size();i++){
			DictionaryType dt = (DictionaryType)dts.get(i);
			this.cascadeAddDicType(cascadeDts, dt.getDictionaryTypes());
		}
	}

	/**
	 * 导入系统模块资源菜单数据字典
	 * @param xls EXCEL文件
	 * @modified
	 */
	@SuppressWarnings("unchecked")
	public void importDate(File xls) throws Exception {
		List apps = XlsUtils.read(xls, "系统", AppSystem.class, appCols);
		Map<String,Object> oldIdToObject = new HashMap();//原文件中的主健对对象的映射
		for(AppSystem app:(List<AppSystem>)apps){
			String oldId = app.getAppId();
			app.setAppId(null);
			app.setAppName(app.getAppName());
		    getDao().addEntity(app);//增加系统
		    oldIdToObject.put("app_"+oldId, app);
		}
		List modules = XlsUtils.read(xls, "模块", AppModule.class, moduleCols);//取模块数据
		List resources = XlsUtils.read(xls, "功能资源", ModuleResource.class, resourceCols);//取资源数据
		Map<String,Object> parentToChild = new HashMap<String,Object>();
		for(AppModule module:(List<AppModule>)modules){//处理模块
			String oldId = module.getModuleId();
			if(module.getAppSystem()==null) 
				throw new Exception("\\\""+module.getModuleName()+"\\\"此模块无关联系统！");
			module.getModuleResources().clear();
			AppSystem linkApp = (AppSystem)oldIdToObject.get("app_"+module.getAppSystem().getAppId());
			if(linkApp==null)
				throw new Exception("\\\""+module.getModuleName()+"\\\"此模块关联系统不在导入文件中！");
			module.setAppSystem(linkApp);
		    AppModule parentModule = module.getAppModule();
		    String key = "";
		    if(parentModule!=null)
		    	key = parentModule.getModuleId();
		    List childModules = (List)parentToChild.get("module_"+key);
		    if(childModules==null){
		    	childModules = new ArrayList();
		    	parentToChild.put("module_"+key, childModules);
		    }
		    oldIdToObject.put("module_"+oldId, module);
		    childModules.add(module);
		}
		for(ModuleResource resource:(List<ModuleResource>)resources){//处理资源
			String oldId = resource.getResourceId();
			AppModule module = resource.getAppModule();
			if(module!=null){
				AppModule moduleAct = (AppModule)oldIdToObject.get("module_"+module.getModuleId());
				if(moduleAct==null)
					throw new Exception("\\\""+resource.getResourceName()+"\\\"此资源关联模块\\\""
							+module.getModuleId()+"\\\"在文件中找不到！");
				resource.setResourceId(null);
				resource.setAppModule(moduleAct);
				moduleAct.getModuleResources().add(resource);
				oldIdToObject.put("resource_"+oldId, resource);
			}else throw new Exception("\\\""+resource.getResourceName()+"\\\"此资源无关联模块！");
		}
		//从上到下保存模块
		this.iterateImportModule((List)parentToChild.get("module_"), oldIdToObject,parentToChild,null);
		List menus = XlsUtils.read(xls, "菜单", AppMenu.class, menuCols);//取菜单数据
		for(AppMenu menu:(List<AppMenu>)menus){//处理菜单
			if(menu.getAppSystem()==null) 
				throw new Exception("\\\""+menu.getMenuName()+"\\\"此菜单无关联系统！");
			AppSystem linkApp = (AppSystem)oldIdToObject.get("app_"+menu.getAppSystem().getAppId());
			if(linkApp==null)
				throw new Exception("\\\""+menu.getMenuName()+"\\\"此菜单关联系统不在此文件中！");
			menu.setAppSystem(linkApp);
			ModuleResource resource = menu.getModuleResource();
			if(resource!=null){
				ModuleResource resourceAct = (ModuleResource)oldIdToObject.get("resource_"+resource.getResourceId());
				if(resourceAct==null)
					throw new Exception("\\\""+menu.getMenuName()+"\\\"此菜单关联资源不在此文件中！");
				menu.setModuleResource(resourceAct);
			}
			AppMenu parentMenu = menu.getAppMenu();
		    String key = "";
		    if(parentMenu!=null)
		    	key = parentMenu.getMenuId();
		    List childMenus = (List)parentToChild.get("menu_"+key);
		    if(childMenus==null){
		    	childMenus = new ArrayList();
		    	parentToChild.put("menu_"+key, childMenus);
		    }
		    childMenus.add(menu);
		}
		//从上到下保存菜单
		this.iterateImportMenu((List)parentToChild.get("menu_"), oldIdToObject,parentToChild,null);
		List dictionaryTypes = XlsUtils.read(xls, "字典类型", DictionaryType.class, dictionaryTypeCols);//取字典类型
		List dictionarys = XlsUtils.read(xls, "字典数据", Dictionary.class, dictionaryCols);//取字典数据
		for(DictionaryType dt:(List<DictionaryType>)dictionaryTypes){//处理字典类型
			dt.getDictionaryTypes().clear();
			DictionaryType parentDt = dt.getDictionaryType();
			oldIdToObject.put("dt_"+dt.getDtId(), dt);
		    String key = "";
		    if(parentDt!=null)
		    	key = parentDt.getDtId();
		    List childMenus = (List)parentToChild.get("dt_"+key);
		    if(childMenus==null){
		    	childMenus = new ArrayList();
		    	parentToChild.put("dt_"+key, childMenus);
		    }
		    childMenus.add(dt);
		}
		for(Dictionary dic:(List<Dictionary>)dictionarys){//处理字典数据
			DictionaryType dt = dic.getDictionaryType();
			if(dt!=null){
				DictionaryType dtAct = (DictionaryType)oldIdToObject.get("dt_"+dt.getDtId());
				if(dtAct==null) 
					throw new Exception("此数据字典类型\\\""+dt.getDtId()+"\\\"在导入文件中找不到！");
				dic.setDicId(null);
				dic.setDictionaryType(dtAct);
				dtAct.getDictionaries().add(dic);
			}else throw new Exception("\\\""+dic.getDataName()+"\\\"找不到对应的字典类型！");
		}
		//从上到下保存字典类型
		this.iterateImportDic((List)parentToChild.get("dt_"), oldIdToObject,parentToChild,null);
	}
	/**
	 * 迭代处理模块
	 * @param modules
	 * @param oldIdToObject
	 * @param parentToChild
	 * @param parentModule
	 * @modified
	 */
	@SuppressWarnings("unchecked")
	private void iterateImportModule(List modules,Map<String,Object> oldIdToObject,
			Map<String,Object> parentToChild,AppModule parentModule){
		if(modules!=null)
		for(AppModule module:(List<AppModule>)modules){
			String oldId = module.getModuleId();
			module.setAppModule(parentModule);//设置父模块
			getDao().addEntity(module);//增加模块
			oldIdToObject.put("module_"+oldId, module);
			this.iterateImportModule((List)parentToChild.get("module_"+oldId), oldIdToObject,parentToChild, module);
	    }
	}
	/**
	 * 迭代处理菜单
	 * @param menus
	 * @param oldIdToObject
	 * @param parentToChild
	 * @param parentMenu
	 * @modified
	 */
	@SuppressWarnings("unchecked")
	private void iterateImportMenu(List menus,Map<String,Object> oldIdToObject,
			Map<String,Object> parentToChild,AppMenu parentMenu){
		if(menus!=null)
		for(AppMenu menu:(List<AppMenu>)menus){
			String oldId = menu.getMenuId();
			menu.setAppMenu(parentMenu);//设置父菜单
			getDao().addEntity(menu);//增加菜单
			oldIdToObject.put("menu_"+oldId, menu);
			this.iterateImportMenu((List)parentToChild.get("menu_"+oldId), oldIdToObject,parentToChild, menu);
	    }
	}
	/**
	 * 迭代处理数据字典
	 * @param dts
	 * @param oldIdToObject
	 * @param parentToChild
	 * @param parentDt
	 * @modified
	 */
	@SuppressWarnings("unchecked")
	private void iterateImportDic(List dts,Map<String,Object> oldIdToObject,
			Map<String,Object> parentToChild,DictionaryType parentDt){
		if(dts!=null)
		for(DictionaryType dt:(List<DictionaryType>)dts){
			String oldId = dt.getDtId();
			dt.setDictionaryType(parentDt);
			getDao().addEntity(dt);//增加字典
			this.iterateImportDic((List)parentToChild.get("dt_"+oldId), oldIdToObject,parentToChild, dt);
	    }
	}
}