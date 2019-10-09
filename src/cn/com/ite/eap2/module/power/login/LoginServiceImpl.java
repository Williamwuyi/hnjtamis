package cn.com.ite.eap2.module.power.login;

import java.util.*;

import cn.com.ite.eap2.common.utils.CharsetSwitchUtil;
import cn.com.ite.eap2.common.utils.StringUtils;
import cn.com.ite.eap2.core.service.DefaultServiceImpl;
import cn.com.ite.eap2.core.service.TreeNode;
import cn.com.ite.eap2.domain.funres.AppMenu;
import cn.com.ite.eap2.domain.funres.AppSystem;
import cn.com.ite.eap2.domain.funres.ModuleResource;
import cn.com.ite.eap2.domain.organization.Dept;
import cn.com.ite.eap2.domain.organization.Employee;
import cn.com.ite.eap2.domain.organization.Organ;
import cn.com.ite.eap2.domain.organization.Quarter;
import cn.com.ite.eap2.domain.power.SysRole;
import cn.com.ite.eap2.domain.power.SysUser;

/**
 * <p>Title cn.com.ite.eap2.module.power.login.LoginServiceImpl</p>
 * <p>Description 登录服务实现</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2014</p>
 * @author 宋文科
 * @create time: 2014-7-14 上午09:39:32
 * @version 2.0
 * 
 * @modified records:
 */
public class LoginServiceImpl extends DefaultServiceImpl implements LoginService {
	/**
	 * 判断账号和密码
	 * @param account 账号
	 * @param password 密码
	 * @return 正确返回用户对象，否则报错
	 * @modified
	 */
	@SuppressWarnings("unchecked")
	public SysUser judgeUserPass(String account,String password){
		Map<String,Object> term = new HashMap<String,Object>();
		term.put("account", account);
		term.put("password", CharsetSwitchUtil.encryptPassword(password));
		List<SysUser> users = (List<SysUser>)getDao().queryConfigQl("judgeLoginHql", term, null, SysUser.class);
		if(users.size()==0)
			return null;
		return (SysUser)users.get(0);
	}
	/**
	 * 判断账号和密码,其中密码已经加密
	 * @param account 账号
	 * @param password 密码
	 * @return 正确返回用户对象，否则报错
	 * @modified
	 */
	@SuppressWarnings("unchecked")
	public SysUser judgeUserPassForEncrypt(String account,String password){
		Map<String,Object> term = new HashMap<String,Object>();
		term.put("account", account);
		term.put("password", password);
		List<SysUser> users = (List<SysUser>)getDao().queryConfigQl("judgeLoginHql", term, null, SysUser.class);
		if(users.size()==0)
			return null;
		return (SysUser)users.get(0);
	}
	/**
	 * 获得被代理的用户
	 * @param userId
	 * @return
	 * @modified
	 */
	@SuppressWarnings("unchecked")
	public List<SysUser> getByProxyUser(String userId){
		Map<String,Object> term = new HashMap<String,Object>();
		term.put("userId", userId);
		return (List<SysUser>)getDao().queryConfigQl("byProxyUserHql", term, null, SysUser.class);
	}
	/**
	 * 获取SESSION信息
	 * @param user 用户
	 * @param proxy 是否代理模式
	 * @param appId 系统ID
	 * @return
	 * @modified
	 */
    @SuppressWarnings("unchecked")
	public UserSession getSession(SysUser user,boolean proxy,String appId){
    	UserSession userSession = new UserSession();
    	userSession.setAccount(user.getAccount());
    	userSession.setUserId(user.getUserId());
    	Map<String,Object> term = new HashMap<String,Object>();
		term.put("userId", user.getUserId());
		String quarterId = null;
		if(user.getEmployee()!=null){
			Employee employee = user.getEmployee();
			userSession.setEmployeeId(employee.getEmployeeId());
			userSession.setEmployeeCode(employee.getEmployeeCode());
			userSession.setEmployeeName(employee.getEmployeeName());
			if(employee.getQuarter()!=null){
				Quarter quarter = employee.getQuarter();
				quarterId = quarter.getQuarterId();
				userSession.setQuarterId(quarterId);
				userSession.setQuarterName(quarter.getQuarterName());
			}
		}
		String deptId = null;
		if(user.getDept()!=null){
			Dept dept = user.getDept();
			deptId = dept.getDeptId();
			userSession.setDeptId(dept.getDeptId());
			userSession.setDeptName(dept.getDeptName());
			userSession.setCurrentDeptId(dept.getDeptId());
			userSession.setCurrentDeptName(dept.getDeptName());
		}
		Organ organ = user.getOrgan();
		if(organ==null){
			List os = getDao().findAll(Organ.class);
			if(os.size()>0){
				organ = (Organ)os.get(0);
				userSession.setOrganId("");
				userSession.setOrganName("");
				userSession.setOrganCode("");
				userSession.setCurrentOrganId(organ.getOrganId());
				userSession.setCurrentOrganName(organ.getOrganName());
				userSession.setCurrentOrganCode(organ.getOrganCode());
				userSession.setCurrentOrganAlias(organ.getOrganAlias());
			}
		}else{
			userSession.setOrganId(organ.getOrganId());
			userSession.setOrganName(organ.getOrganName());
			userSession.setOrganAlias(organ.getOrganAlias());
			userSession.setCurrentOrganId(organ.getOrganId());
			userSession.setCurrentOrganName(organ.getOrganName());
			userSession.setCurrentOrganAlias(organ.getOrganAlias());
			userSession.setOrganCode(organ.getOrganCode());
			userSession.setCurrentOrganCode(organ.getOrganCode());
		}
		if(user.getDept()==null){
			List depts = organ.getDepts();
			if(depts.size()>0){
				int index = 0;
				Dept dept = (Dept)depts.get(index++);
				while(dept!=null&&dept.getDept()!=null){
					dept = (Dept)dept.getDept();
				}
				deptId = dept.getDeptId();
				userSession.setDeptId("");
				userSession.setDeptName("");
				userSession.setCurrentDeptId(dept.getDeptId());
				userSession.setCurrentDeptName(dept.getDeptName());
			}else{
				userSession.setDeptId("");
				userSession.setDeptName("");
				userSession.setCurrentDeptId("");
				userSession.setCurrentDeptName("");
			}
		}
		term.put("deptId", deptId);
		term.put("quarterId", quarterId);
		if(appId==null){
			if(user.getApp()!=null){
				userSession.setAppName(user.getApp().getAppName());
				userSession.setAppId(user.getApp().getAppId());
				userSession.setTheme(user.getApp().getTheme());
				userSession.setBasePath(user.getApp().getAppUrl());
				userSession.setIndexUrl(user.getApp().getIndexUrl());
			}else{
				List apps = this.getAppList(userSession.getUserId());
				if(apps.size()>0){
					AppSystem  as = (AppSystem)apps.get(0);
					userSession.setAppId(as.getAppId());
					userSession.setAppName(as.getAppName());
					userSession.setTheme(as.getTheme());
					userSession.setBasePath(as.getAppUrl());
					userSession.setIndexUrl(as.getIndexUrl());
				}else{
					userSession.setTheme("access");
				}
			}
		}else{
			AppSystem app = (AppSystem)getDao().findEntityBykey(AppSystem.class, appId);
			userSession.setAppName(app.getAppName());
			userSession.setAppId(appId);
			userSession.setTheme(app.getTheme());
			userSession.setBasePath(app.getAppUrl());
			userSession.setIndexUrl(app.getIndexUrl());
		}		
		List<ModuleResource> mrs = (List<ModuleResource>)getDao().queryConfigQl((proxy?"queryProxyResourceHql":"queryResourceHql"), term, null, null);
		List<String> mrIds = new ArrayList();
		for(ModuleResource mr:mrs){
			mrIds.add(mr.getResourceId());
			AppSystem as = mr.getAppModule().getAppSystem();
			String url = null;
			for(String u:mr.getResourceUrl().split("\\|")){
				if(url==null)
					url = as.getAppUrl()+"/"+u;
				else
					url += "|"+as.getAppUrl()+"/"+u;
			}
			if(url.length()>7)
			url = url.substring(0,8)+url.substring(8).replaceAll("/+", "/");
			userSession.getAuthoritys().add(mr.getResourceCode());
			userSession.getAuthorityNames().add(mr.getResourceName());
			userSession.getAuthUrls().add(url);
		}
		List<ModuleResource> allMrs = getDao().getAll(ModuleResource.class);
		for(ModuleResource mr:allMrs){
			if(!mrIds.contains(mr.getResourceId())){
				AppSystem as = mr.getAppModule().getAppSystem();
				String url = null;
				for(String u:mr.getResourceUrl().split("\\|")){
					if(url==null)
						url = as.getAppUrl()+"/"+u;
					else
						url += "|"+as.getAppUrl()+"/"+u;
				}
				url = url.substring(0,8)+url.substring(8).replaceAll("/+", "/");
				userSession.getNoAuthUrls().add(url);
			}
		}
		Set<SysRole> roleSets = user.getUserRoles();
		if(roleSets!=null && roleSets.size()>0){
			Iterator<SysRole> it= roleSets.iterator();
			String roleid="";
			String rolecode="";
			String rolename="";
			while(it.hasNext()){
				SysRole tmp = it.next();
				roleid += tmp.getRoleId()+",";
				rolecode += tmp.getRoleCode()+",";
				rolename +=tmp.getRoleName()+",";
			}
			roleid = roleid.substring(0, roleid.length()-1);
			rolecode = rolecode.substring(0, rolecode.length()-1);
			rolename = rolename.substring(0, rolename.length()-1);
			userSession.setRoleIds(roleid); 
			userSession.setRoleCodes(rolecode);
			userSession.setRoleNames(rolename);
			 
		}
		if(!StringUtils.isEmpty(user.getTheme()))
			userSession.setTheme(user.getTheme());
		if(userSession.getUserId().equals("admin")||user.getSysMangers().size()>0)
			userSession.setManger(true);
		else
			userSession.setManger(false);
		return userSession;
    }
    
    /**
     * 查询主菜单
     * @param user 用户,代理模式下对应代理用户
     * @param proxy 是否代理模式
     * @param appId 系统ID
     * @return 树结构
     * @modified
     */
    @SuppressWarnings("unchecked")
	public List<TreeNode> findMainMenu(SysUser user,boolean proxy,String appId){
    	Map<String,Object> term = new HashMap<String,Object>();
		term.put("userId", user.getUserId());
		String quarterId = null;
		if(user.getEmployee()!=null){
			Employee employee = user.getEmployee();
			if(employee.getQuarter()!=null){
				Quarter quarter = employee.getQuarter();
				quarterId = quarter.getQuarterId();
			}
		}
		String deptId = null;
		if(user.getDept()!=null){
			Dept dept = user.getDept();
			deptId = dept.getDeptId();
		}
		term.put("deptId", deptId);
		term.put("quarterId", quarterId);
		if(appId==null&&user.getApp()!=null) 
			appId = user.getApp().getAppId();
		term.put("appId", appId);
		List<AppMenu> menus =  (List<AppMenu>)getDao().queryConfigQl((proxy?"proxyMenuHql":"menuHql"), term, null, AppMenu.class);
		List deleteParas = new ArrayList();
		Map<String,TreeNode> nodes = new LinkedHashMap<String,TreeNode>();
		List menuIds = new ArrayList();
		for(AppMenu menu:menus){
			menuIds.add(menu.getMenuId());
			if(nodes.containsKey(menu.getMenuId()))
				continue;
			TreeNode tree = new TreeNode();
			tree.setId(menu.getMenuId());
			tree.setTitle(menu.getMenuName());
			tree.setTagName(menu.getTabName());
			tree.setType(menu.getMenuType()==2?"menu":(menu.getMenuType()==1?"menugroup":"separator"));
			tree.setTemp(menu.getTarget()+"");
			AppMenu parent = menu.getAppMenu();
			if(parent==null){
				if(menu.getAppSystem().getAppId().equals("system"))
					tree.setTemp("system");
			}else
				tree.setParentId(parent.getMenuId());
			if(menu.getTarget()==1){
				ModuleResource mr = menu.getModuleResource();
				if(mr!=null){
				    tree.setIcon(mr.getIcon());
					AppSystem as = mr.getAppModule().getAppSystem();
					String url = as.getAppUrl()+"/"+mr.getResourceUrl();
					if(url.length()>7)
					url = url.substring(0,7)+url.substring(7).replaceAll("/+", "/");
					tree.setUrl(url);
				}
			}else
				tree.setUrl(menu.getOtherUrl());
			TreeNode.putTypeIncon("menugroup", "resources/images/accordian.gif", "");
			TreeNode.putTypeIncon("menu", "resources/icons/fam/grid.png", "");
			nodes.put(tree.getId(), tree);
		}
		//删除没有权限的快捷菜单
		Map deletePara = new HashMap();
		deletePara.put("userId", user.getUserId());
		deletePara.put("menus", menuIds);
		deleteParas.add(deletePara);
		try{
		getDao().excuteQl("deleteNoPopdemShuckMenu", deleteParas);
		}catch(Exception e){
			e.printStackTrace();
		}
		List<TreeNode> menuGroups = new ArrayList<TreeNode>();
		if(proxy)
			menuGroups = (List<TreeNode>)getDao().queryConfigQl("menuGroupHql", term, null, TreeNode.class);
		menuGroups.addAll(nodes.values());
		boolean addSystemMenu = false;
		for(int i=0;i<menuGroups.size();i++){
			TreeNode tree = menuGroups.get(i);
			if(StringUtils.isEmpty(tree.getParentId())&&
					"system".equals(tree.getTemp())){
				tree.setParentId("system");
				if(!addSystemMenu){
					TreeNode systemNode = new TreeNode();
					systemNode.setTitle("系统管理");
					systemNode.setId("system");
					systemNode.setType("menugroup");
					systemNode.setIcon("resources/icons/fam/cog.png");
					menuGroups.add(systemNode);
					addSystemMenu = true;
				}
			}
		}
		List leafTypes = new ArrayList();
		leafTypes.add("separator");
		leafTypes.add("menu");
		List<TreeNode> trees = TreeNode.toTree(menuGroups, true, leafTypes);
		//处理只有一个分隔条时，要删除父菜单
		this.handleDecollator(trees);
		return trees;
    }
    /**
     * 处理只有一个分隔条时，要删除父菜单
     * @param trees
     * @return
     */
    private boolean handleDecollator(List<TreeNode> trees){
    	boolean find = false;
    	for(int i=0;i<trees.size();i++){
    		TreeNode tree = trees.get(i);
    		boolean h;
    		if(tree.getChildren()!=null&&tree.getChildren().size()>0){
    		   h = handleDecollator(tree.getChildren());
    		   if(!h)
       			trees.remove(i--);
    		}else
    		   h = tree.getType().equals("menu");
    		if(h)
    			find = true;
    	}
    	return find;
    }
    /**
     * 获取系统切换下拉数据
     * @param userId
     * @return
     * @modified
     */
    @SuppressWarnings("unchecked")
	public List<AppSystem> getAppList(String userId){
    	SysUser user = (SysUser)getDao().findEntityBykey(SysUser.class,userId);
    	Map<String,Object> term = new HashMap<String,Object>();
		term.put("userId", user.getUserId());
		String quarterId = null;
		if(user.getEmployee()!=null){
			Employee employee = user.getEmployee();
			if(employee.getQuarter()!=null){
				Quarter quarter = employee.getQuarter();
				quarterId = quarter.getQuarterId();
			}
		}
		String deptId = null;
		if(user.getDept()!=null){
			Dept dept = user.getDept();
			deptId = dept.getDeptId();
		}
		term.put("deptId", deptId);
		term.put("quarterId", quarterId);
		List<AppSystem> ass = (List<AppSystem>)getDao().queryConfigQl("userAppHql", term, null, AppMenu.class);
		if(ass.size()>1)
		for(AppSystem as:ass){//去掉系统管理这个系统
			if(as.getAppId().equals("system")){
				ass.remove(as);
				break;
			}
		}
		java.util.Collections.sort(ass, new Comparator(){
			public int compare(Object o1, Object o2) {
				AppSystem as1 = (AppSystem)o1;
				AppSystem as2 = (AppSystem)o2;
				return as1.getOrderNo().compareTo(as2.getOrderNo());
			}});
		return ass;
    }
}