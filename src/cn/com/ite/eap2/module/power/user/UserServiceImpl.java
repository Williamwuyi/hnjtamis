package cn.com.ite.eap2.module.power.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cn.com.ite.eap2.core.service.DefaultServiceImpl;
import cn.com.ite.eap2.core.service.TreeNode;
import cn.com.ite.eap2.domain.funres.AppMenu;
import cn.com.ite.eap2.domain.funres.AppModule;
import cn.com.ite.eap2.domain.funres.AppSystem;
import cn.com.ite.eap2.domain.funres.ModuleResource;
import cn.com.ite.eap2.module.organization.dept.DeptService;

/**
 * <p>Title cn.com.ite.eap2.module.power.user.UserServiceImpl</p>
 * <p>Description 用户服务实现</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2014</p>
 * @author 宋文科
 * @create time: 2014-7-9 上午09:30:23
 * @version 2.0
 * 
 * @modified records:
 */
public class UserServiceImpl extends DefaultServiceImpl implements UserService {
	/**
	 * 部门服务
	 */
	private DeptService deptServcie;
	public void setDeptServcie(DeptService deptServcie) {
		this.deptServcie = deptServcie;
	}
	/**
     * 查询机构部门用户树
     * @param topOrganId
     * @param nameTerm
     * @return
     * @modified
     */
    @SuppressWarnings("unchecked")
	public List<TreeNode> findUserTree(String topOrganId,String nameTerm)  throws Exception{
    	List<TreeNode> odTree = deptServcie.findOrganDeptTree(topOrganId, "");
    	Map term = new HashMap();
    	term.put("nameTerm", nameTerm);
    	List<Map> users = getDao().queryConfigQl("userTreeHql", term, null, HashMap.class);
    	for(Map map:users){
    		TreeNode node = new TreeNode();
    		node.setType("user");
    		node.setChecked(false);
    		node.setId((String)map.get("userId"));
    		node.setTitle(map.get("account")+(map.get("employeeName")==null?"":("("+map.get("employeeName")+")")));
    		String deptId = (String)map.get("deptId");
    		if(deptId==null)
    			node.setParentId((String)map.get("organId"));
    		else
    			node.setParentId(deptId);
    		odTree.add(node);
    	}
    	TreeNode.putTypeIncon("user", "resources/icons/fam/user.gif", "");
    	List leafTypes = new ArrayList();
    	leafTypes.add("user");
    	return TreeNode.toTree(odTree, true, leafTypes);
    }
	/**
     * 查询用户资源
     * @param userId 用户ID
     * @return 树结构
     * @modified
     */
    @SuppressWarnings("unchecked")
	public List<TreeNode> findUserResource(String userId){
    	Map<String,Object> term = new HashMap<String,Object>();
		term.put("userId", userId);
		List<ModuleResource> resources =  (List<ModuleResource>)getDao().queryConfigQl("queryResourceHql", 
				term, null, AppMenu.class);
		Map<String,TreeNode> nodes = new LinkedHashMap<String,TreeNode>();
		for(ModuleResource resource:resources){
			if(nodes.containsKey(resource.getResourceId()))
				continue;
			TreeNode tree = new TreeNode();
			tree.setId(resource.getResourceId());
			tree.setTitle(resource.getResourceName());
			tree.setType("resource");
			tree.setParentId(resource.getAppModule().getModuleId());
			AppModule am = resource.getAppModule();
			AppSystem as = am.getAppSystem();
			while(am!=null){
				TreeNode atree = new TreeNode();
				atree.setId(am.getModuleId());
				atree.setTitle(am.getModuleName());
				atree.setType("module");
				if(!nodes.containsKey(atree.getId()))
				  nodes.put(atree.getId(), atree);
				am = am.getAppModule();
				if(am!=null)
					atree.setParentId(am.getModuleId());
				else{
					atree.setParentId(as.getAppId());
					TreeNode stree = new TreeNode();
					stree.setId(as.getAppId());
					stree.setTitle(as.getAppName());
					stree.setType("app");
					if(!nodes.containsKey(stree.getId()))
						nodes.put(stree.getId(), stree);
				}
			}
			TreeNode.putTypeIncon("app", "resources/images/accordian.gif", "");
			TreeNode.putTypeIncon("module", "resources/icons/fam/grid.png", "");
			TreeNode.putTypeIncon("resource", "resources/icons/fam/connect.gif", "");
			nodes.put(tree.getId(), tree);
		}
		List leafTypes = new ArrayList();
		leafTypes.add("resource");
		List trees = new ArrayList();
		trees.addAll(nodes.values());
		return TreeNode.toTree(trees, true, leafTypes);
    }
}
