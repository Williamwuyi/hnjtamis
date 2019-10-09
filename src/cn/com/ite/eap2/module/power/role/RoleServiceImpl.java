package cn.com.ite.eap2.module.power.role;

import java.util.*;

import org.apache.commons.lang3.StringUtils;

import cn.com.ite.eap2.core.service.DefaultServiceImpl;
import cn.com.ite.eap2.core.service.TreeNode;
import cn.com.ite.eap2.domain.organization.Dept;
import cn.com.ite.eap2.domain.organization.Employee;
import cn.com.ite.eap2.domain.organization.Quarter;
import cn.com.ite.eap2.domain.power.*;

/**
 * <p>Title cn.com.ite.eap2.module.power.role.RoleServiceImpl</p>
 * <p>Description 角色服务实现</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2014</p>
 * @author 宋文科
 * @create time: 2014-7-10 上午10:09:10
 * @version 2.0
 * 
 * @modified records:
 */
public class RoleServiceImpl extends DefaultServiceImpl implements RoleService {

	/**
	 * 角色树的数据提取方步，只取用户条件中拥有的角色，
	 * 用户ID为admin表示超级管理员，不受限制
	 * @param userId 管理者用户ID
	 * @param allotUserId 要分配角色的用户ID
	 * @param deptId 要分配角色的部门ID
	 * @param quarterId 要分配角色的岗位ID
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<TreeNode> findMRTree(String userId,String allotUserid,String deptId,String quarterId)throws Exception{
		if(userId==null) throw new Exception("当前用户不存在！");
		Map term = new HashMap();
		term.put("userId", userId);
		List<TreeNode> listType = (List<TreeNode>)getDao().queryConfigQl("queryTypeTreeHql", term, null, TreeNode.class);
		SysUser user = (SysUser)this.findDataByKey(userId, SysUser.class);
		String userDeptId = null;
		String userQuarterId = null;
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
		List<TreeNode> listRole = (List<TreeNode>)getDao().queryConfigQl("queryTreeHql", term, null, TreeNode.class);
		//处理复选框的状态
		for(TreeNode node:listRole){
			boolean checked = false;
			if(!StringUtils.isEmpty(allotUserid)){
				SysUser allotUser = (SysUser)this.findDataByKey(allotUserid, SysUser.class);
				for(SysRole role : allotUser.getUserRoles()){
					if(role.getRoleId().equals(node.getId())){
						checked = true;
						break;
					}
				}
			}
			if(deptId!=null&&!checked){
				Dept dept = (Dept)this.findDataByKey(deptId, Dept.class);
				for(SysRole role : dept.getDeptRoles()){
					if(role.getRoleId().equals(node.getId())){
						checked = true;
						break;
					}
				}
			}
			if(quarterId!=null&&!checked){
				Quarter quarter = (Quarter)this.findDataByKey(quarterId, Quarter.class);
				for(SysRole role : quarter.getQuarterRoles()){
					if(role.getRoleId().equals(node.getId())){
						checked = true;
						break;
					}
				}
			}
			//查询对应资源名称
			Map resourceterm = new HashMap();
			resourceterm.put("id", node.getId());
			List res = this.queryData("queryResoureHql", resourceterm, null);
			String qtip = null;
			for(String re:(List<String>)res){
				if(qtip == null)
					qtip = re;
				else
					qtip += ","+re;
			}
			node.setQtip(qtip);
			node.setChecked(checked);
		}
		List list = new ArrayList();
		list.addAll(listType);
		list.addAll(listRole);
		TreeNode.putTypeIncon("roleType", "resources/icons/fam/cog.gif", "");
		TreeNode.putTypeIncon("role", "resources/icons/fam/grid.png", "");
		List leafTypes = new ArrayList();
		leafTypes.add("role");
		return TreeNode.toTree(list,true,leafTypes);
	}
	
	/**
	 * 角色树的数据提取方步，只取用户条件中拥有的角色，
	 * 用户ID为admin表示超级管理员，不受限制
	 * @param userId 管理者用户ID
	 * @param allotUserId 要分配角色的用户ID
	 * @param deptId 要分配角色的部门ID
	 * @param quarterId 要分配角色的岗位ID
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<TreeNode> findMRTreeFilteByOran(String userId,String allotUserid,String deptId,String quarterId,String organId)throws Exception{
		if(userId==null) throw new Exception("当前用户不存在！");
		Map term = new HashMap();
		term.put("userId", userId);
		List<TreeNode> listType = (List<TreeNode>)getDao().queryConfigQl("queryTypeTreeHql", term, null, TreeNode.class);
		SysUser user = (SysUser)this.findDataByKey(userId, SysUser.class);
		String userDeptId = null;
		String userQuarterId = null;
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
		term.put("organId", organId);
		List<TreeNode> listRole = (List<TreeNode>)getDao().queryConfigQl("queryTreeAndFOrganHql", term, null, TreeNode.class);
		//处理复选框的状态
		for(TreeNode node:listRole){
			boolean checked = false;
			if(!StringUtils.isEmpty(allotUserid)){
				SysUser allotUser = (SysUser)this.findDataByKey(allotUserid, SysUser.class);
				for(SysRole role : allotUser.getUserRoles()){
					if(role.getRoleId().equals(node.getId())){
						checked = true;
						break;
					}
				}
			}
			if(deptId!=null&&!checked){
				Dept dept = (Dept)this.findDataByKey(deptId, Dept.class);
				for(SysRole role : dept.getDeptRoles()){
					if(role.getRoleId().equals(node.getId())){
						checked = true;
						break;
					}
				}
			}
			if(quarterId!=null&&!checked){
				Quarter quarter = (Quarter)this.findDataByKey(quarterId, Quarter.class);
				for(SysRole role : quarter.getQuarterRoles()){
					if(role.getRoleId().equals(node.getId())){
						checked = true;
						break;
					}
				}
			}
			//查询对应资源名称
			Map resourceterm = new HashMap();
			resourceterm.put("id", node.getId());
			List res = this.queryData("queryResoureHql", resourceterm, null);
			String qtip = null;
			for(String re:(List<String>)res){
				if(qtip == null)
					qtip = re;
				else
					qtip += ","+re;
			}
			node.setQtip(qtip);
			node.setChecked(checked);
		}
		List list = new ArrayList();
		list.addAll(listType);
		list.addAll(listRole);
		TreeNode.putTypeIncon("roleType", "resources/icons/fam/cog.gif", "");
		TreeNode.putTypeIncon("role", "resources/icons/fam/grid.png", "");
		List leafTypes = new ArrayList();
		leafTypes.add("role");
		return TreeNode.toTree(list,true,leafTypes);
	}

}
