package cn.com.ite.eap2.module.organization.dept;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.type.StringType;

import cn.com.ite.eap2.core.hibernate.HibernateDefaultDAOImpl;
import cn.com.ite.eap2.core.service.TreeNode;

public class DeptDaoImpl  extends HibernateDefaultDAOImpl implements DeptDao{

	/**
	 * 根据部门进行查询
	 * @author 朱健
	 * @param deptId
	 * @return
	 * @modified
	 */
	public List<TreeNode> queryOwnerDeptTree(String deptId){
		List<TreeNode> list = new ArrayList<TreeNode>();
		try{
			Session session = template.getSessionFactory().getCurrentSession();
			String sql = "";
			boolean deptIdIsNotNull = false;
			if(deptId == null || "".equals(deptId) || "null".equals(deptId)){
				sql = " select d.dept_id as id, "+
			               "d.dept_name as title, "+
			               "d.parent_dept_id as parentId, "+
			               "'dept' as type, "+
			               "d.order_no, "+
			               "d.level_code, "+
			               "d.organ_id as organid "+
			          " from dept d "+
			         " where d.is_validation = 1 "
			         + " start with d.parent_dept_id is null "
			         + " connect by prior d.dept_id = d.parent_dept_id "
			         + " order SIBLINGS BY d.order_no, d.level_code ";
				
			}else{
				sql = " select d.dept_id as id, "+
					               " d.dept_name as title, "+
					               " d.parent_dept_id as parentId, "+
					               "'dept' as type, "+
					               "d.order_no, "+
					               "d.level_code, "+
					               "d.organ_id as organid "+
					          " from dept d "+
					         " where d.is_validation = 1 "
					         + " start with d.dept_id = ? "
					         + " connect by prior d.dept_id = d.parent_dept_id "
					         + " order SIBLINGS BY d.order_no, d.level_code ";
				deptIdIsNotNull = true;
			}
			SQLQuery sqlQuery=session.createSQLQuery(sql);  
			if(deptIdIsNotNull)sqlQuery.setString(0, deptId);
		    List<Object[]> tmplist = sqlQuery.addScalar("id", StringType.INSTANCE)
				.addScalar("title", StringType.INSTANCE)
				.addScalar("parentId", StringType.INSTANCE)
				.addScalar("type", StringType.INSTANCE)
				.addScalar("organid", StringType.INSTANCE)
				.list();
		    Map<String,List<TreeNode>> childMap = new HashMap<String,List<TreeNode>>();
		    for(int i=0;i<tmplist.size();i++){
		    	Object[] object = (Object[])tmplist.get(i);
		    	String id = (String)object[0];
		    	String title = (String)object[1];
		    	String parentId = (String)object[2];
		    	String type = (String)object[3];
		    	if(parentId==null || "".equals(parentId) || id.equals(deptId)){
			    	TreeNode node = new TreeNode();
			    	node.setId(id);
			    	node.setTitle(title);
			    	node.setParentId(null);
			    	node.setType(type);
			    	node.setChildren(new ArrayList<TreeNode>());
			    	list.add(node);
		    	}else{
		    		TreeNode node = new TreeNode();
			    	node.setId(id);
			    	node.setTitle(title);
			    	node.setParentId(parentId);
			    	node.setType(type);
			    	node.setChildren(new ArrayList<TreeNode>());
		    		List<TreeNode> ls = (List<TreeNode>)childMap.get(parentId);
		    		if(ls == null)ls = new ArrayList<TreeNode>();
		    		ls.add(node);
		    		childMap.put(parentId,ls);
		    	}
		    }
		    setChildTreeMap(list,childMap);
		    TreeNode.putTypeIncon("organ", "resources/icons/fam/organ.gif", "");
			TreeNode.putTypeIncon("employee", "resources/icons/fam/cog.gif", "");
			TreeNode.putTypeIncon("quarter", "resources/icons/fam/grid.png", "");
			TreeNode.putTypeIncon("dept", "resources/icons/fam/dept.gif", "");
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 获取机构部门树
	 * @author 朱健
	 * @return
	 * @modified
	 */
	public List<TreeNode> findOwerOrganTree(String organId,String deptName)throws Exception{
		List<TreeNode> trees = new ArrayList<TreeNode>();
		try{
			Map<String,List<TreeNode>> childMap = new HashMap<String,List<TreeNode>>();
			Session session = template.getSessionFactory().getCurrentSession();
			String sql = "select id, title, pid as parentId, type,organ_id as organid "+
					  "from (select d.dept_id as id, "+
						               "d.dept_name as title, "+
						               "d.parent_dept_id as pid, "+
						               "'dept' as type, "+
						               "2 as typeorder, "+
						               "d.order_no, "+
						               "d.level_code, "+
						               "d.organ_id as organ_id "+
						          "from dept d where d.is_validation = 1 ";
							if(organId!=null && !"".equals(organId) && !"".equals(organId)){
								sql+= " and d.organ_id = '"+organId+"'   ";
					        }
							sql+=" ) t  order by t.typeorder, t.order_no, t.level_code ";
			SQLQuery sqlQuery=session.createSQLQuery(sql);   
		    List<Object[]> tmplist = sqlQuery.addScalar("id", StringType.INSTANCE)
				.addScalar("title", StringType.INSTANCE)
				.addScalar("parentId", StringType.INSTANCE)
				.addScalar("type", StringType.INSTANCE)
				.addScalar("organid", StringType.INSTANCE)
				.list();
		    
		    for(int i=0;i<tmplist.size();i++){
		    	Object[] object = (Object[])tmplist.get(i);
		    	String id = (String)object[0];
		    	String title = (String)object[1];
		    	String pid = (String)object[2];
		    	String type = (String)object[3];

		    	if(pid==null || "".equals(pid)){
			    	TreeNode node = new TreeNode();
			    	node.setId(id);
			    	node.setTitle(title);
			    	node.setParentId(null);
			    	node.setType(type);
			    	node.setChildren(new ArrayList<TreeNode>());
			    	
			    	trees.add(node);
		    	}else{
		    		TreeNode node = new TreeNode();
			    	node.setId(id);
			    	node.setTitle(title);
			    	node.setParentId(pid);
			    	node.setType(type);
			    	node.setChildren(new ArrayList<TreeNode>());
			    	if("employee".equals(type)){
			    		node.setLeaf(true);
			    	}
			    	
		    		List<TreeNode> ls = (List<TreeNode>)childMap.get(pid);
		    		if(ls == null)ls = new ArrayList<TreeNode>();
		    		ls.add(node);
		    		childMap.put(pid,ls);
		    	}
		    }
		    setChildTreeMap(trees,childMap);
		    TreeNode.putTypeIncon("organ", "resources/icons/fam/organ.gif", "");
			TreeNode.putTypeIncon("employee", "resources/icons/fam/cog.gif", "");
			TreeNode.putTypeIncon("quarter", "resources/icons/fam/grid.png", "");
			TreeNode.putTypeIncon("dept", "resources/icons/fam/dept.gif", "");
		}catch(Exception e){
			e.printStackTrace();
		}
		return trees;
	}
	
	/**
	 * 获取机构部门树
	 * @author 朱健
	 * @return
	 * @modified
	 */
	public List<TreeNode> findOrganDeptTree(String topOrganId,String deptName)throws Exception{
		List<TreeNode> trees = new ArrayList<TreeNode>();
		try{
			Map<String,List<TreeNode>> childMap = new HashMap<String,List<TreeNode>>();
			Session session = template.getSessionFactory().getCurrentSession();
			String sql = "select id, title, pid as parentId, type,organ_id as organid "+
					  "from ( select o.organ_id as id, "+
							               "o.organ_name as title, "+
							               "o.parent_organ_id as pid, "+
							               "'organ' as type, "+
							               "1 as typeorder, "+
							               "o.order_no, "+
							               "o.level_code, "+
							               "o.organ_id as organ_id "+
							          "from organ o "+
							         "where o.is_validation = 1 ";
						if(topOrganId!=null && !"".equals(topOrganId) && !"".equals(topOrganId)){
							sql+= " start with o.organ_id = '"+topOrganId+"'  connect by prior o.organ_id = o.parent_organ_id  ";
				        }
							sql+=" UNION ALL "+ 
							  "select d.dept_id as id, "+
						               "d.dept_name as title, "+
						               "decode(d.parent_dept_id, null, d.organ_id, d.parent_dept_id) as pid, "+
						               "'dept' as type, "+
						               "2 as typeorder, "+
						               "d.order_no, "+
						               "d.level_code, "+
						               "d.organ_id as organ_id "+
						          "from dept d "+
						         "where d.is_validation = 1) t  order by t.typeorder, t.order_no, t.level_code ";
			SQLQuery sqlQuery=session.createSQLQuery(sql);   
		    List<Object[]> tmplist = sqlQuery.addScalar("id", StringType.INSTANCE)
				.addScalar("title", StringType.INSTANCE)
				.addScalar("parentId", StringType.INSTANCE)
				.addScalar("type", StringType.INSTANCE)
				.addScalar("organid", StringType.INSTANCE)
				.list();
		    
		    for(int i=0;i<tmplist.size();i++){
		    	Object[] object = (Object[])tmplist.get(i);
		    	String id = (String)object[0];
		    	String title = (String)object[1];
		    	String pid = (String)object[2];
		    	String type = (String)object[3];

		    	if((pid==null || "".equals(pid)  || id.equals(topOrganId)) && "organ".equals(type)){
			    	TreeNode node = new TreeNode();
			    	node.setId(id);
			    	node.setTitle(title);
			    	node.setParentId(null);
			    	node.setType(type);
			    	node.setChildren(new ArrayList<TreeNode>());
			    	
			    	trees.add(node);
		    	}else{
		    		TreeNode node = new TreeNode();
			    	node.setId(id);
			    	node.setTitle(title);
			    	node.setParentId(pid);
			    	node.setType(type);
			    	node.setChildren(new ArrayList<TreeNode>());
			    	if("employee".equals(type)){
			    		node.setLeaf(true);
			    	}
			    	
		    		List<TreeNode> ls = (List<TreeNode>)childMap.get(pid);
		    		if(ls == null)ls = new ArrayList<TreeNode>();
		    		ls.add(node);
		    		childMap.put(pid,ls);
		    	}
		    }
		    setChildTreeMap(trees,childMap);
		    TreeNode.putTypeIncon("organ", "resources/icons/fam/organ.gif", "");
			TreeNode.putTypeIncon("employee", "resources/icons/fam/cog.gif", "");
			TreeNode.putTypeIncon("quarter", "resources/icons/fam/grid.png", "");
			TreeNode.putTypeIncon("dept", "resources/icons/fam/dept.gif", "");
		}catch(Exception e){
			e.printStackTrace();
		}
		return trees;
	}
	
	
	
	public List<TreeNode> findOrganDeptSwitchTree(String topOrganId,String topDeptId,String deptName)throws Exception{
		List<TreeNode> trees = new ArrayList<TreeNode>();
		try{
			Map<String,List<TreeNode>> childMap = new HashMap<String,List<TreeNode>>();
			Session session = template.getSessionFactory().getCurrentSession();
			String sql = "select id, title, pid as parentId, type,organ_id as organid,tagName "+
					  "from ( select o.organ_id as id, "+
							               "o.organ_name as title, "+
							               "o.parent_organ_id as pid, "+
							               "'organ' as type, "+
							               "1 as typeorder, "+
							               "o.order_no, "+
							               "o.level_code, "+
							               "o.organ_id as organ_id,"+ 
							               "null as tagName "+
							          "from organ o "+
							         "where o.is_validation = 1 ";
						if(topOrganId!=null && !"".equals(topOrganId) && !"".equals(topOrganId)){
							sql+= " start with o.organ_id = '"+topOrganId+"'  connect by prior o.organ_id = o.parent_organ_id  ";
				        }
						sql+="UNION ALL "+ 
							  "select d.dept_id as id, "+
						               "d.dept_name as title, "+
						               "decode(d.parent_dept_id, null, d.organ_id, d.parent_dept_id) as pid, "+
						               "'dept' as type, "+
						               "2 as typeorder, "+
						               "d.order_no, "+
						               "d.level_code, "+
						               "d.organ_id as organ_id,"+ 
							           "d.organ_id as tagName "+
						          "from dept d "+
						         "where d.is_validation = 1  ";
					if(topDeptId!=null && !"".equals(topDeptId) && !"".equals(topDeptId)){
						sql+= " start with d.dept_id = '"+topDeptId+"'  connect by prior d.dept_id = d.parent_dept_id ";
			        }
					sql+= " ) t ";
					if(deptName!=null && !"".equals(deptName) && !"".equals(deptName)){
						sql+= " start with t.title = '"+deptName+"'  connect by prior t.pid = t.id ";
					}
					sql+=" order by t.typeorder, t.order_no, t.level_code ";
			SQLQuery sqlQuery=session.createSQLQuery(sql);   
		    List<Object[]> tmplist = sqlQuery.addScalar("id", StringType.INSTANCE)
				.addScalar("title", StringType.INSTANCE)
				.addScalar("parentId", StringType.INSTANCE)
				.addScalar("type", StringType.INSTANCE)
				.addScalar("organid", StringType.INSTANCE)
				.addScalar("tagName", StringType.INSTANCE)
				.list();
		    
		    for(int i=0;i<tmplist.size();i++){
		    	Object[] object = (Object[])tmplist.get(i);
		    	String id = (String)object[0];
		    	String title = (String)object[1];
		    	String pid = (String)object[2];
		    	String type = (String)object[3];
		    	String tagName = (String)object[4];

		    	if((pid==null || "".equals(pid) || id.equals(topOrganId))  && "organ".equals(type)){
			    	TreeNode node = new TreeNode();
			    	node.setId(id);
			    	node.setTitle(title);
			    	node.setParentId(null);
			    	node.setType(type);
			    	node.setTagName(tagName);
			    	node.setChildren(new ArrayList<TreeNode>());
			    	
			    	trees.add(node);
		    	}else{
		    		TreeNode node = new TreeNode();
			    	node.setId(id);
			    	node.setTitle(title);
			    	node.setParentId(pid);
			    	node.setType(type);
			    	node.setTagName(tagName);
			    	node.setChildren(new ArrayList<TreeNode>());
			    	if("employee".equals(type)){
			    		node.setLeaf(true);
			    	}
			    	
		    		List<TreeNode> ls = (List<TreeNode>)childMap.get(pid);
		    		if(ls == null)ls = new ArrayList<TreeNode>();
		    		ls.add(node);
		    		childMap.put(pid,ls);
		    	}
		    }
		    setChildTreeMap(trees,childMap);
		    TreeNode.putTypeIncon("organ", "resources/icons/fam/organ.gif", "");
			TreeNode.putTypeIncon("employee", "resources/icons/fam/cog.gif", "");
			TreeNode.putTypeIncon("quarter", "resources/icons/fam/grid.png", "");
			TreeNode.putTypeIncon("dept", "resources/icons/fam/dept.gif", "");
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return trees;
	}
	
	private void setChildTreeMap(List<TreeNode> list,Map<String,List<TreeNode>> childMap){
		if(list!=null){
			for(int i=0;i<list.size();i++){
				TreeNode node = (TreeNode)list.get(i);
				List<TreeNode> childList = (List<TreeNode>)childMap.get(node.getId());
				if(childList!=null){
					node.setChildren(childList);
					setChildTreeMap(childList,childMap);
				}else{
					node.setLeaf(true);
				}
			}
		}
	}
}
