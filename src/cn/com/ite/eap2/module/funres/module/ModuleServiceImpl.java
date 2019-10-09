package cn.com.ite.eap2.module.funres.module;

import java.util.*;

import cn.com.ite.eap2.common.utils.StringUtils;
import cn.com.ite.eap2.core.service.DefaultServiceImpl;
import cn.com.ite.eap2.core.service.TreeNode;
import cn.com.ite.eap2.domain.funres.AppMenu;
import cn.com.ite.eap2.domain.funres.AppModule;
import cn.com.ite.eap2.domain.funres.ModuleResource;
import cn.com.ite.eap2.domain.organization.Dept;
import cn.com.ite.eap2.domain.organization.Employee;
import cn.com.ite.eap2.domain.organization.Quarter;
import cn.com.ite.eap2.domain.power.SysRole;
import cn.com.ite.eap2.domain.power.SysUser;

/**
 * <p>Title cn.com.ite.eap2.module.funres.module.ModuleServiceImpl</p>
 * <p>Description 模块服务实现类</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2014</p>
 * @author 宋文科
 * @create time: 2014-7-7 下午03:02:09
 * @version 2.0
 * 
 * @modified records:
 */
public class ModuleServiceImpl extends DefaultServiceImpl implements ModuleService {

	/**
	 * 模块资源树的数据提取方步
	 * @param appId 系统ID
	 * @param resourceName 资源名称
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<TreeNode> findMRTree(String appId,
			String resourceName) throws Exception{
		Map term = new HashMap();
		term.put("appTerm", appId);
		term.put("nameTerm", resourceName);
		List<TreeNode> list = (List<TreeNode>)getDao().queryConfigQl("queryResourceHql", term, null, TreeNode.class);
		Map<String,TreeNode> ms = new HashMap<String,TreeNode>();
		for(TreeNode node:list){
			node.setType("resource");
			if(!ms.containsKey(node.getId())) ms.put(node.getId(), node);
			TreeNode f = node;
			while(!StringUtils.isEmpty(f.getParentId())){
				AppModule module = (AppModule)getDao().findEntityBykey(AppModule.class, f.getParentId());
				if(module!=null){
					TreeNode newNode = TreeNode.objectToTree(module, "moduleId", "appModule.moduleId", "moduleName");
					newNode.setType("module");
					if(!ms.containsKey(newNode.getId()))
					   ms.put(newNode.getId(), newNode);
					f = newNode;
				}else break;
			}
		}
		list.clear();
		list.addAll(ms.values());
		TreeNode.putTypeIncon("resource", "resources/icons/fam/cog.gif", "");
		TreeNode.putTypeIncon("module", "resources/icons/fam/grid.png", "");
		List leafTypes = new ArrayList();
		leafTypes.add("resource");
		return TreeNode.toTree(list,true,leafTypes);
	}
	
	/**
	 * 系统模块资源树
	 * @param userId 当前用户
	 * @param roleId 角色分配资源中的角色ID
	 * @param proxy 是否在代理模式下
	 * @param resourceName 资源名称查询条件
	 * @return 树结点
	 * @throws Exception
	 * @modified
	 */
	@SuppressWarnings("unchecked")
	public List<TreeNode> findAMRTree(String userId,String roleId,boolean proxy,
			String resourceName)throws Exception{
		Map term = new HashMap();
		term.put("userId", userId);
		List<TreeNode> appList = (List<TreeNode>)getDao().queryConfigQl("queryAppHql", null, null, TreeNode.class);
		List<TreeNode> moduleList = (List<TreeNode>)getDao().queryConfigQl("queryModuleHql", null, null, TreeNode.class);
		for(TreeNode node:moduleList){
			if(StringUtils.isEmpty(node.getParentId())){
				node.setParentId(node.getTemp());
			}
		}
		String userDeptId = null;
		String userQuarterId = null;
		SysUser user = (SysUser)this.findDataByKey(userId, SysUser.class);
		if(user==null) throw new Exception(userId+"此用户找不到！");
		Employee userEmployee = user.getEmployee();
		if(userEmployee!=null){
			Dept dept = userEmployee.getDept();
			if(dept!=null)
				userDeptId = dept.getDeptId();
			Quarter userQuarter = userEmployee.getQuarter();
			if(userQuarter!=null)
				userQuarterId = userQuarter.getQuarterId();
		}
		term.put("deptId", userDeptId);
		term.put("quarterId", userQuarterId);
		List<TreeNode> resList = (List<TreeNode>)getDao().queryConfigQl("queryPepdomResourceHql", term, null, TreeNode.class);
		//处理复选框的状态
		for(TreeNode node:resList){
			boolean checked = false;
			if(roleId!=null){
				SysRole role = (SysRole)this.findDataByKey(roleId, SysRole.class);
				for(ModuleResource mr : role.getRoleResources()){
					if(mr.getResourceId().equals(node.getId())){
						checked = true;
						break;
					}
				}
			}
			if(proxy&&!checked){
				SysUser sysUser = (SysUser)this.findDataByKey(userId, SysUser.class);
				for(ModuleResource mr : sysUser.getProxyResources()){
					if(mr.getResourceId().equals(node.getId())){
						checked = true;
						break;
					}
					
				}
			}
			node.setChecked(checked);
		}
		List list = new ArrayList();
		list.addAll(appList);
		list.addAll(moduleList);
		list.addAll(resList);
		TreeNode.putTypeIncon("app", "resources/icons/fam/cog.gif", "");
		TreeNode.putTypeIncon("module", "resources/icons/fam/grid.png", "");
		TreeNode.putTypeIncon("resource", "resources/icons/fam/connect.gif", "");
		TreeNode.select(list, resourceName);//查询
		List leafTypes = new ArrayList();
		leafTypes.add("resource");
		return TreeNode.toTree(list,true,leafTypes);
	}
	/**
	 * 保存模块
	 */
	public void saveModule(AppModule module){
		boolean add = StringUtils.isEmpty(module.getModuleId());
		getDao().saveEntity(module);
		this.makeMenu(module);
	}
	
	/**
	 * 生成菜单 
	 * @param module
	 * @modified
	 */
	@SuppressWarnings("unchecked")
	public void makeMenu(AppModule module){
		Map term = new HashMap();
		term.put("name", module.getModuleName());
		term.put("appId", module.getAppSystem().getAppId());
		/*if(module.getAppModule()!=null){
			term.put("pname", module.getAppModule().getModuleName());
		}*/
		List<AppMenu> menus = (List<AppMenu>)getDao().queryConfigQl("queryMenuForModule", term, null, AppMenu.class);
		AppMenu menu = null;
		if(menus.size()>0){
			menu = menus.get(0);
		}else{
			menu = new AppMenu();
			menu.setMenuName(module.getModuleName());
			menu.setMenuType(1);
			menu.setAppSystem(module.getAppSystem());
	    	menu.setExpand(true);
	    	menu.setHidden(false);
	    	menu.setOrderNo(1);
	    	menu.setTabName(module.getModuleName());
	    	menu.setTarget(1);
	    	//找父菜单
	    	AppModule parent = module.getAppModule();
	    	if(parent!=null){
	    		List aps = getDao().findEntityByField(AppMenu.class, "menuName", parent.getModuleName());
	    		if(aps.size()>0){
	    			for(AppMenu ap:(List<AppMenu>)aps){
	    			   if(ap.getAppSystem().getAppId().equals(module.getAppSystem().getAppId())){
	    				   menu.setAppMenu(ap);
	    			       break;
	    			   }
	    			}
	    		}
	    	}
	    	this.getDao().saveEntity(menu);
		}
		for(ModuleResource mr:module.getModuleResources()){
			boolean find = false;
			if(mr.getResourceType()==2) continue;
		    for(AppMenu am:menu.getAppMenus()){
		    	if(am.getModuleResource()==null)
		    		continue;
		    	if(am.getMenuName().equals(mr.getResourceName())||
		    	   am.getModuleResource().getResourceId().equals(mr.getResourceId())){
		    		find=true;
		    		break;
		    	}
		    }
		    if(!find){
		    	AppMenu newMenu = new AppMenu();
		    	newMenu.setMenuName(mr.getResourceName());
		    	newMenu.setTabName(mr.getResourceName());
		    	newMenu.setMenuType(2);
		    	newMenu.setAppSystem(module.getAppSystem());
		    	newMenu.setExpand(true);
		    	newMenu.setHidden(false);
		    	List dbs = getDao().findEntityByField(ModuleResource.class, 
		    			"resourceCode", mr.getResourceCode());
		    	if(dbs.size()>0){
		    		ModuleResource dbOb = (ModuleResource)dbs.get(0);
		    		newMenu.setModuleResource(dbOb);
		    	}		    	
		    	newMenu.setOrderNo(1+getDao().getFieldMax(AppMenu.class, 
						"orderNo","appMenu.menuId",menu.getMenuId()));
		    	newMenu.setAppMenu(menu);
		    	newMenu.setTarget(1);
		    	this.getDao().saveEntity(newMenu);
		    }
		}
	}
}