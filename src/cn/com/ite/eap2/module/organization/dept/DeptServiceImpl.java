package cn.com.ite.eap2.module.organization.dept;

import java.util.*;

import cn.com.ite.eap2.common.utils.StringUtils;
import cn.com.ite.eap2.core.service.DefaultServiceImpl;
import cn.com.ite.eap2.core.service.TreeNode;
import cn.com.ite.eap2.domain.organization.Dept;
import cn.com.ite.eap2.domain.organization.Organ;

/**
 * <p>Title cn.com.ite.eap2.module.organization.organ.OrganServiceImpl</p>
 * <p>Description 机构部门服务实现</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2014</p>
 * @author 宋文科
 * @create time: 2014-6-13 下午01:51:37
 * @version 2.0
 * 
 * @modified records:
 */
public class DeptServiceImpl extends DefaultServiceImpl implements DeptService {

	/**
	 * 根据部门进行查询
	 * @author 朱健
	 * @param deptId
	 * @return
	 * @modified
	 */
	public List<TreeNode> queryOwnerDeptTree(String deptId){
		DeptDao deptDao = (DeptDao)this.getDao();
		return deptDao.queryOwnerDeptTree(deptId);
	}
	
	/**
	 * 获取机构部门树
	 * @author 朱健
	 * @return
	 * @modified
	 */
	public List<TreeNode> findOwerOrganTree(String organId,String deptName)throws Exception{
		DeptDao deptDao = (DeptDao)this.getDao();
		return deptDao.findOwerOrganTree(organId, deptName);
	}
	/**
	 * 机构部门树的数据提取
	 * @param topOrganId 项级机构ID
	 * @param deptName 部门名称(模糊匹配）
	 * @return 树结点
	 */
	@SuppressWarnings("unchecked")
	public List<TreeNode> findOrganDeptTree(String topOrganId,String deptName)throws Exception{
		DeptDao deptDao = (DeptDao)this.getDao();
		return deptDao.findOrganDeptTree(topOrganId, deptName);
		/*
		
		Map allDeptMap = new HashMap();
		List<Dept> allDeptList = this.queryAllDate(Dept.class);
		if(allDeptList!=null){
			for(Dept dept : allDeptList){
				allDeptMap.put(dept.getDeptId(), dept);
			}
		}
		
		Map allOrganMap = new HashMap();
		List<Organ> allOrganList = this.queryAllDate(Organ.class);
		if(allOrganList!=null){
			for(Organ organ : allOrganList){
				allOrganMap.put(organ.getOrganId(), organ);
			}
		}
		
		Map term = new HashMap();
		term.put("nameTerm", deptName);
		List<TreeNode> list = (List<TreeNode>)getDao().queryConfigQl("queryTreeHql", term, null, TreeNode.class);
		Map<String,TreeNode> ms = new LinkedHashMap<String,TreeNode>();
		Map<String,TreeNode> organs = new LinkedHashMap<String,TreeNode>();
		for(TreeNode node:list){
			TreeNode f = node;
			if(!ms.containsKey(f.getId())) ms.put(f.getId(), f);
			String organId = node.getTagName();
			if(StringUtils.isEmpty(f.getParentId())) f.setParentId(organId);
			//加上级部门数据
			while(!StringUtils.isEmpty(f.getParentId())){
				Dept dept = (Dept)allDeptMap.get(f.getParentId());////getDao().findEntityBykey(Dept.class, f.getParentId());
				if(dept!=null){
					TreeNode newNode = TreeNode.objectToTree(dept, "deptId", "dept.deptId", "deptName");
					newNode.setType("dept");
					newNode.setOrderNo(dept.getOrderNo());
					if(!ms.containsKey(newNode.getId()))
					  ms.put(newNode.getId(), newNode);
					f = newNode;
					if(StringUtils.isEmpty(newNode.getParentId())) newNode.setParentId(organId);
				}else break;
			}
			node.setType("dept");
			//boolean top = false;
			//加机构数据
			while(!StringUtils.isEmpty(organId)){
				Organ organ = (Organ)allOrganMap.get(organId);//getDao().findEntityBykey(Organ.class, organId);
				if(organ!=null){
					TreeNode newNode = TreeNode.objectToTree(organ, "organId", "organ.organId", "organName");
					newNode.setType("organ");
					newNode.setOrderNo(organ.getOrderNo());
					if(!organs.containsKey(newNode.getId()))
						organs.put(newNode.getId(), newNode);
					if(topOrganId!=null&&topOrganId.equals(organId)){
						//top = true;
						newNode.setParentId(null);
						break;
					}
					if(organ.getOrgan()!=null){
						organId = organ.getOrgan().getOrganId();
					}else
						organId = null;
				}else break;
			}
			//if(top) break;
		}
		list.clear();
		list.addAll(organs.values());
		java.util.Collections.sort(list, new Comparator(){
			@Override
			public int compare(Object o1, Object o2) {
				TreeNode t1 = (TreeNode)o1;
				TreeNode t2 = (TreeNode)o2;
				if(t1.getOrderNo()==null||t2.getOrderNo()==null)
					return 0;
				return t1.getOrderNo().compareTo(t2.getOrderNo());
			}
		});
		List depts = new ArrayList();
		depts.addAll(ms.values());
		java.util.Collections.sort(depts, new Comparator(){
			@Override
			public int compare(Object o1, Object o2) {
				TreeNode t1 = (TreeNode)o1;
				TreeNode t2 = (TreeNode)o2;
				if(t1.getOrderNo()==null||t2.getOrderNo()==null)
					return 0;
				return t1.getOrderNo().compareTo(t2.getOrderNo());
			}
		});
		list.addAll(depts);		
		TreeNode.putTypeIncon("dept", "resources/icons/fam/dept.gif", "");
		TreeNode.putTypeIncon("organ", "resources/icons/fam/organ.gif", "");
		return TreeNode.toTree(list,true,null,topOrganId);*/
	}
	
	/**
	 * 机构部门切换树的数据提取
	 * @param topOrganId 项级机构ID
	 * @param deptName 部门名称(模糊匹配）
	 * @return 树结点
	 */
	@SuppressWarnings("unchecked")
	public List<TreeNode> findOrganDeptSwitchTree(String topOrganId,String topDeptId)throws Exception{
		DeptDao deptDao = (DeptDao)this.getDao();
		return deptDao.findOrganDeptSwitchTree(topOrganId, topDeptId,null);
		
		/*List<TreeNode> list = (List<TreeNode>)getDao().queryConfigQl("queryTreeHql", new HashMap(), null, TreeNode.class);
		list = TreeNode.toTree(list,true,null,topDeptId);
		for(TreeNode node:list){
			if(!StringUtils.isEmpty(topDeptId)){
				if(node.getId().equals(topDeptId)){
					node.setParentId(node.getTagName());
				}
			}else{
				if(StringUtils.isEmpty(node.getParentId()))
					node.setParentId(node.getTagName());
			}
		}
		List<TreeNode> olist = (List<TreeNode>)getDao().queryConfigQl("queryOrganTreeHql", new HashMap(), null, TreeNode.class);
		olist = TreeNode.toTree(olist,true,null,topOrganId);
		for(TreeNode node:olist){
			if(node.getId().equals(topOrganId)){
				node.setParentId(null);
			}
		}
		olist.addAll(list);
		list = TreeNode.toTree(olist,true,null);
		return list;*/
	}
}
