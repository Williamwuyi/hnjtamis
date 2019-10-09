package cn.com.ite.hnjtamis.baseinfo.roleConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.type.StringType;

import cn.com.ite.eap2.core.hibernate.HibernateDefaultDAOImpl;
import cn.com.ite.eap2.core.service.TreeNode;

/**
 *
 * <p>Title cn.com.ite.hnjtamis.baseinfo.roleConfig.RoleConfigDaoImpl</p>
 * <p>Description 角色配置管理</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2016</p>
 * @create time: 2016年11月18日 上午9:22:04
 * @version 1.0
 * 
 * @modified records:
 */
public class RoleConfigDaoImpl extends HibernateDefaultDAOImpl implements RoleConfigDao{

	
	

	/**
	 * 获取角色配置树
	 * @description
	 * @param configId
	 * @return
	 * @throws Exception
	 * @modified
	 */
	public List<TreeNode> findRoleConfigTree(String configId)throws Exception{
		List<TreeNode> trees = new ArrayList<TreeNode>();
		try{
			Map<String,String> roleConfigMap = getRoleConfigMap(configId);
			Map<String,List<TreeNode>> childMap = new HashMap<String,List<TreeNode>>();
			Session session = template.getSessionFactory().getCurrentSession();
			String sql = " select id, "+
                          "title, "+
                          "pid      as parentId, "+
                          "type  "+
                     "from (select rt.rt_id as id, "+
                                  "rt.role_typename as title, "+
                                  "rt.parent_rt_id as pid, "+
                                  "'roleType' as type, "+
                                  "1 as typeorder, "+
                                  "rt.sort_no as order_no "+
                             "from ROLE_TYPE rt "+
                           "UNION ALL "+
                           "select sr.role_id as id, "+
                                  "sr.role_name as title, "+
                                  "sr.roletype_id as pid, "+
                                  "'role' as type, "+
                                  "2 as typeorder, "+
                                  "sr.order_no as order_no "+
                             "from SYS_ROLE sr) t "+
                    "order by t.typeorder, t.order_no ";
			SQLQuery sqlQuery=session.createSQLQuery(sql);   
		    List<Object[]> tmplist = sqlQuery.addScalar("id", StringType.INSTANCE)
				.addScalar("title", StringType.INSTANCE)
				.addScalar("parentId", StringType.INSTANCE)
				.addScalar("type", StringType.INSTANCE)
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
			    	node.setChecked(roleConfigMap.get(id)!=null?true:false);
			    	trees.add(node);
		    	}else{
		    		TreeNode node = new TreeNode();
			    	node.setId(id);
			    	node.setTitle(title);
			    	node.setParentId(pid);
			    	node.setType(type);
			    	node.setChecked(roleConfigMap.get(id)!=null?true:false);
			    	node.setChildren(new ArrayList<TreeNode>());
			    	
		    		List<TreeNode> ls = (List<TreeNode>)childMap.get(pid);
		    		if(ls == null)ls = new ArrayList<TreeNode>();
		    		ls.add(node);
		    		childMap.put(pid,ls);
		    	}
		    }
		    setChildTreeMap(trees,childMap);
		    TreeNode.putTypeIncon("roleType", "resources/icons/fam/cog.gif", "");
			TreeNode.putTypeIncon("role", "resources/icons/fam/grid.png", "");
		}catch(Exception e){
			e.printStackTrace();
		}
		return trees;
	}
	
	private Map<String,String> getRoleConfigMap(String configId){
		Map<String,String> roleConfigMap = new HashMap<String,String>();
		try{
			Session session = template.getSessionFactory().getCurrentSession();
			String sql = " select t.role_id from ROLE_OBJECT_RELATION t where t.role_object_id = ? ";
			SQLQuery sqlQuery=session.createSQLQuery(sql); 
			sqlQuery.setString(0, configId);
		    List<String> tmplist = sqlQuery.addScalar("role_id", StringType.INSTANCE) .list();
		    if(tmplist!=null){
			    for(int i=0;i<tmplist.size();i++){
			    	String role_id = tmplist.get(i);
			    	roleConfigMap.put(role_id, role_id);
			    }
		    }
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		return roleConfigMap;
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
