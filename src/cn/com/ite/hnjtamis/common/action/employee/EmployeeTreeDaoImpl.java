package cn.com.ite.hnjtamis.common.action.employee;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.type.StringType;

import cn.com.ite.eap2.common.utils.DateUtils;
import cn.com.ite.eap2.common.utils.StringUtils;
import cn.com.ite.eap2.core.hibernate.HibernateDefaultDAOImpl;
import cn.com.ite.eap2.core.service.TreeNode;
import cn.com.ite.eap2.domain.organization.Dept;
import cn.com.ite.eap2.domain.organization.Employee;
import cn.com.ite.eap2.domain.organization.Organ;
import cn.com.ite.eap2.domain.organization.Quarter;
import cn.com.ite.hnjtamis.common.action.employee.form.EmployeeForm;

public class EmployeeTreeDaoImpl extends HibernateDefaultDAOImpl implements EmployeeTreeDao {
	
	
	/**
	 *  获取机构部门信息，最多两层部门
	 * @author 朱健
	 * @param organId
	 * @param deptLevel 获取部门的层次
	 * @return
	 * @modified
	 */
	public List queryDeptInOwnerOrgan(String ownerOrganId,int deptLevel){
		List list = new ArrayList();
		try{
			Session session = template.getSessionFactory().getCurrentSession();
			Map organIdsMap = new HashMap();
			String sql = "select id, title, pid as parentId, type,organ_id as organid "+
					  "from (select o.organ_id as id, "+
					               "o.organ_name as title, "+
					               "o.parent_organ_id as pid, "+
					               "'organ' as type, "+
					               "1 as typeorder, "+
					               "o.order_no, "+
					               "o.level_code, "+
					               "o.organ_id as organ_id "+
					          "from organ o "+
					         "where o.is_validation = 1 "+
					        "UNION ALL "+
					        "select d.dept_id as id, "+
					               "d.dept_name as title, "+
					               "decode(d.parent_dept_id, null, d.organ_id, d.parent_dept_id) as pid, "+
					               "'dept' as type, "+
					               "2 as typeorder, "+
					               "d.order_no, "+
					               "d.level_code, "+
					               "d.organ_id as organ_id "+
					          "from dept d "+
					         "where d.is_validation = 1) t ";
					 if(ownerOrganId!=null && !"".equals(ownerOrganId)){
						 sql+=" where organ_id  = '"+ownerOrganId+"' "; 
					 }
					sql+=" order by t.typeorder, t.order_no, t.level_code ";
			SQLQuery sqlQuery=session.createSQLQuery(sql);   
		    List<Object[]> tmplist = sqlQuery.addScalar("id", StringType.INSTANCE)
				.addScalar("title", StringType.INSTANCE)
				.addScalar("parentId", StringType.INSTANCE)
				.addScalar("type", StringType.INSTANCE)
				.addScalar("organid", StringType.INSTANCE)
				.list();
		    Map childMap = new HashMap();
		    for(int i=0;i<tmplist.size();i++){
		    	Object[] object = (Object[])tmplist.get(i);
		    	String id = (String)object[0];
		    	String title = (String)object[1];
		    	String parentId = (String)object[2];
		    	String type = (String)object[3];
		    	if((parentId==null || "".equals(parentId) || id.equals(ownerOrganId)) && "organ".equals(type)){
			    	TreeNode node = new TreeNode();
			    	node.setId(id);
			    	node.setTitle(title);
			    	node.setParentId(null);
			    	node.setType(type);
			    	node.setChildren(new ArrayList());
			    	
			    	list.add(node);
		    	}else{
		    		TreeNode node = new TreeNode();
			    	node.setId(id);
			    	node.setTitle(title);
			    	node.setParentId(parentId);
			    	node.setType(type);
			    	node.setChildren(new ArrayList());
			    	node.setLeaf(true);
		    		List ls = (List)childMap.get(parentId);
		    		if(ls == null)ls = new ArrayList();
		    		ls.add(node);
		    		childMap.put(parentId,ls);
		    	}
		    }
		    List newlist = new ArrayList();
		    for(int i=0;i<list.size();i++){
		    	TreeNode node = (TreeNode)list.get(i);
		    	newlist.add(node);
		    	if(deptLevel > 0){
		    		List<TreeNode> child1  = (List<TreeNode>)childMap.get(node.getId());
		    		if(child1!=null && child1.size()>0){
			    		newlist.addAll(child1);
			    		if(deptLevel > 1){
			    			for(int k=0;k<child1.size();k++){
			    				TreeNode childNode = (TreeNode)child1.get(k);
			    				List<TreeNode> child2  = (List<TreeNode>)childMap.get(childNode.getId());
			    				if(child2!=null && child2.size()>0){
			    					newlist.addAll(child2);
			    				}
			    			}
			    		}
		    		}
		    	}
		    }
		    list = newlist;
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 *  获取机构部门信息，最多两层部门
	 * @author 朱健
	 * @param organId
	 * @param deptLevel 获取部门的层次
	 * @return
	 * @modified
	 */
	public List queryDeptInOrgan(String organId,int deptLevel){
		List list = new ArrayList();
		try{
			Session session = template.getSessionFactory().getCurrentSession();
			String organIds = "";
			Map organIdsMap = new HashMap();
			if(organId!=null && !"".equals(organId) && !"null".equals(organId)){
				String organSql = "select o.organ_id as organid,o.parent_organ_id as parentorganid from organ o "+
							" start with o.organ_id = ? "+
							" connect by prior o.organ_id = o.parent_organ_id  "+
							 "order SIBLINGS BY o.order_no,o.level_code  ";
				
				SQLQuery sqlQuery=session.createSQLQuery(organSql);   
				sqlQuery.setString(0, organId);
			    List<Object[]> tmplist = (List<Object[]>)sqlQuery.addScalar("organid", StringType.INSTANCE)
			    		.addScalar("parentorganid", StringType.INSTANCE).list();
			    for(int i=0;i<tmplist.size();i++){
			    	Object[] object = (Object[])tmplist.get(i);
			    	String t_organid = (String)object[0];
			    	String t_parentorganid = (String)object[1];
			    	organIds += "'"+t_organid+"',";
			    	organIdsMap.put(tmplist.get(i), tmplist.get(i));
			    }
			    if(organIds.length()>0){
			    	organIds = organIds.substring(0,organIds.length()-1);
			    }
			}
			String sql = "select id, title, pid as parentId, type,organ_id as organid "+
					  "from (select o.organ_id as id, "+
					               "o.organ_name as title, "+
					               "o.parent_organ_id as pid, "+
					               "'organ' as type, "+
					               "1 as typeorder, "+
					               "o.order_no, "+
					               "o.level_code, "+
					               "o.organ_id as organ_id "+
					          "from organ o "+
					         "where o.is_validation = 1 "+
					        "UNION ALL "+
					        "select d.dept_id as id, "+
					               "d.dept_name as title, "+
					               "decode(d.parent_dept_id, null, d.organ_id, d.parent_dept_id) as pid, "+
					               "'dept' as type, "+
					               "2 as typeorder, "+
					               "d.order_no, "+
					               "d.level_code, "+
					               "d.organ_id as organ_id "+
					          "from dept d "+
					         "where d.is_validation = 1) t ";
					 if(organIds!=null && !"".equals(organIds)){
						 sql+=" where organ_id in("+organIds+") "; 
					 }
					sql+=" order by t.typeorder, t.order_no, t.level_code ";
			SQLQuery sqlQuery=session.createSQLQuery(sql);   
		    List<Object[]> tmplist = sqlQuery.addScalar("id", StringType.INSTANCE)
				.addScalar("title", StringType.INSTANCE)
				.addScalar("parentId", StringType.INSTANCE)
				.addScalar("type", StringType.INSTANCE)
				.addScalar("organid", StringType.INSTANCE)
				.list();
		    Map childMap = new HashMap();
		    for(int i=0;i<tmplist.size();i++){
		    	Object[] object = (Object[])tmplist.get(i);
		    	String id = (String)object[0];
		    	String title = (String)object[1];
		    	String parentId = (String)object[2];
		    	String type = (String)object[3];
		    	if((parentId==null || "".equals(parentId) || id.equals(organId)) && "organ".equals(type)){
			    	TreeNode node = new TreeNode();
			    	node.setId(id);
			    	node.setTitle(title);
			    	node.setParentId(null);
			    	node.setType(type);
			    	node.setChildren(new ArrayList());
			    	
			    	list.add(node);
		    	}else{
		    		TreeNode node = new TreeNode();
			    	node.setId(id);
			    	node.setTitle(title);
			    	node.setParentId(parentId);
			    	node.setType(type);
			    	node.setChildren(new ArrayList());
			    	node.setLeaf(true);
		    		List ls = (List)childMap.get(parentId);
		    		if(ls == null)ls = new ArrayList();
		    		ls.add(node);
		    		childMap.put(parentId,ls);
		    	}
		    }
		    List newlist = new ArrayList();
		    for(int i=0;i<list.size();i++){
		    	TreeNode node = (TreeNode)list.get(i);
		    	newlist.add(node);
		    	if(deptLevel > 0){
		    		List<TreeNode> child1  = (List<TreeNode>)childMap.get(node.getId());
		    		if(child1!=null && child1.size()>0){
			    		newlist.addAll(child1);
			    		if(deptLevel > 1){
			    			for(int k=0;k<child1.size();k++){
			    				TreeNode childNode = (TreeNode)child1.get(k);
			    				List<TreeNode> child2  = (List<TreeNode>)childMap.get(childNode.getId());
			    				if(child2!=null && child2.size()>0){
			    					newlist.addAll(child2);
			    				}
			    			}
			    		}
		    		}
		    	}
		    }
		    list = newlist;
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 获取机构人员树
	 * @author 朱健
	 * @return
	 * @modified
	 */
	public List queryEmployeeDeptTree(String organId){
		List list = new ArrayList();
		try{
			Session session = template.getSessionFactory().getCurrentSession();
			String organIds = "";
			Map organIdsMap = new HashMap();
			if(organId!=null && !"".equals(organId) && !"null".equals(organId)){
				String organSql = "select o.organ_id as organid,o.parent_organ_id as parentorganid from organ o "+
							" start with o.organ_id = ? "+
							" connect by prior o.organ_id = o.parent_organ_id  "+
							 "order SIBLINGS BY o.order_no,o.level_code  ";
				
				SQLQuery sqlQuery=session.createSQLQuery(organSql);   
				sqlQuery.setString(0, organId);
			    List<Object[]> tmplist = (List<Object[]>)sqlQuery.addScalar("organid", StringType.INSTANCE)
			    		.addScalar("parentorganid", StringType.INSTANCE).list();
			    for(int i=0;i<tmplist.size();i++){
			    	Object[] object = (Object[])tmplist.get(i);
			    	String t_organid = (String)object[0];
			    	String t_parentorganid = (String)object[1];
			    	organIds += "'"+t_organid+"',";
			    	organIdsMap.put(tmplist.get(i), tmplist.get(i));
			    }
			    if(organIds.length()>0){
			    	organIds = organIds.substring(0,organIds.length()-1);
			    }
			}
			/*String sql = "select id,title,pid as parentId ,type "+
				" from (  "+
				" select o.organ_id as id,o.organ_name as title,o.parent_organ_id as pid,'organ' as type,1 as typeorder,o.order_no,o.level_code  "+
				"   from organ o where o.is_validation = 1  "+
				" UNION  ALL  "+
				"  select d.dept_id as id,d.dept_name as title,decode(d.parent_dept_id,null,d.organ_id,d.parent_dept_id) as pid ,  "+
				"  	  'dept' as type,2 as typeorder,d.order_no,d.level_code  "+
				"     from dept d where d.is_validation = 1  "+
				" UNION  ALL  "+
				" select q.quarter_id as id,q.quarter_name as title,decode(q.parent_quarter_id,null,q.dept_id,q.parent_quarter_id) as pid,  "+
				"     'quarter' as type,3 as typeorder,q.order_no,q.level_code  "+
				"     from quarter q where q.is_validation = 1  "+
				//" UNION  ALL  "+
				//" select e.employee_id as id,e.employee_name as title,decode(e.quarter_id,null,e.dept_id,e.quarter_id) as pid,  "+
				//"     'employee' as type,4 as typeorder,e.order_no,e.employee_code  as level_code   "+
				//"     from employee e where e.isvalidation = 1
				" ) t  "+
				" order by t.typeorder,t.order_no,t.level_code ";*/
			String sql = "select id, title, pid as parentId, type,organ_id as organid "+
					  "from (select o.organ_id as id, "+
					               "o.organ_name as title, "+
					               "o.parent_organ_id as pid, "+
					               "'organ' as type, "+
					               "1 as typeorder, "+
					               "o.order_no, "+
					               "o.level_code, "+
					               "o.organ_id as organ_id "+
					          "from organ o "+
					         "where o.is_validation = 1 "+
					        "UNION ALL "+
					        "select d.dept_id as id, "+
					               "d.dept_name as title, "+
					               "decode(d.parent_dept_id, null, d.organ_id, d.parent_dept_id) as pid, "+
					               "'dept' as type, "+
					               "2 as typeorder, "+
					               "d.order_no, "+
					               "d.level_code, "+
					               "d.organ_id as organ_id "+
					          "from dept d "+
					         "where d.is_validation = 1 "+
					        "UNION ALL "+
					        "select q.quarter_id as id, "+
					               "q.quarter_name as title, "+
					               "decode(q.parent_quarter_id, "+
					                      "null, "+
					                      "q.dept_id, "+
					                      "q.parent_quarter_id) as pid, "+
					               "'quarter' as type, "+
					               "3 as typeorder, "+
					               "q.order_no, "+
					               "q.level_code, "+
					               "dd.organ_id as organ_id "+
					          "from quarter q,dept dd "+
					         "where q.is_validation = 1 and q.dept_id = dd.dept_id) t ";
					 if(organIds!=null && !"".equals(organIds)){
						 sql+=" where organ_id in("+organIds+") "; 
					 }
					sql+=" order by t.typeorder, t.order_no, t.level_code ";
			SQLQuery sqlQuery=session.createSQLQuery(sql);   
		    List<Object[]> tmplist = sqlQuery.addScalar("id", StringType.INSTANCE)
				.addScalar("title", StringType.INSTANCE)
				.addScalar("parentId", StringType.INSTANCE)
				.addScalar("type", StringType.INSTANCE)
				.addScalar("organid", StringType.INSTANCE)
				.list();
		    Map childMap = new HashMap();
		    for(int i=0;i<tmplist.size();i++){
		    	Object[] object = (Object[])tmplist.get(i);
		    	String id = (String)object[0];
		    	String title = (String)object[1];
		    	String parentId = (String)object[2];
		    	String type = (String)object[3];
		    	//String organid = (String)object[4];
		    	
		    	//System.out.println(parentId);
		    	//System.out.println(organIdsMap.get(parentId));
		    	if((parentId==null || "".equals(parentId) || id.equals(organId)) && "organ".equals(type)){
			    	TreeNode node = new TreeNode();
			    	node.setId(id);
			    	node.setTitle(title);
			    	node.setParentId(null);
			    	node.setType(type);
			    	node.setChildren(new ArrayList());
			    	
			    	list.add(node);
		    	}else{
		    		TreeNode node = new TreeNode();
			    	node.setId(id);
			    	node.setTitle(title);
			    	node.setParentId(parentId);
			    	node.setType(type);
			    	node.setChildren(new ArrayList());
			    	if("employee".equals(type)){
			    		node.setLeaf(true);
			    	}
			    	
		    		List ls = (List)childMap.get(parentId);
		    		if(ls == null)ls = new ArrayList();
		    		ls.add(node);
		    		childMap.put(parentId,ls);
		    	}
		    }
		    setChildTreeMap(list,childMap);
		    TreeNode.putTypeIncon("organ", "resources/icons/fam/organ.gif", "");
		    TreeNode.putTypeIncon("dept", "resources/icons/fam/dept.gif", "");
		    TreeNode.putTypeIncon("quarter", "resources/icons/fam/user_gray.png", "");
			TreeNode.putTypeIncon("employee", "resources/icons/fam/user_green.gif", "");
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 按培训岗位进行显示
	 * 获取机构人员树
	 * @author 朱健
	 * @return
	 * @modified
	 */
	public List queryPxEmployeeDeptTree(String organId,Map paramMap){
		List list = new ArrayList();
		try{
			String deptNameTerm = (String)paramMap.get("deptNameTerm");
			String employeeNameTerm = (String)paramMap.get("employeeNameTerm");
			
			Session session = template.getSessionFactory().getCurrentSession();
			String organIds = "";
			Map organIdsMap = new HashMap();
			if(organId!=null && !"".equals(organId) && !"null".equals(organId)){
				String organSql = "select o.organ_id as organid,o.parent_organ_id as parentorganid from organ o "+
							" start with o.organ_id = ? "+
							" connect by prior o.organ_id = o.parent_organ_id  "+
							 "order SIBLINGS BY o.order_no,o.level_code  ";
				
				SQLQuery sqlQuery=session.createSQLQuery(organSql);   
				sqlQuery.setString(0, organId);
			    List<Object[]> tmplist = (List<Object[]>)sqlQuery.addScalar("organid", StringType.INSTANCE)
			    		.addScalar("parentorganid", StringType.INSTANCE).list();
			    for(int i=0;i<tmplist.size();i++){
			    	Object[] object = (Object[])tmplist.get(i);
			    	String t_organid = (String)object[0];
			    	String t_parentorganid = (String)object[1];
			    	organIds += "'"+t_organid+"',";
			    	organIdsMap.put(tmplist.get(i), tmplist.get(i));
			    }
			    if(organIds.length()>0){
			    	organIds = organIds.substring(0,organIds.length()-1);
			    }
			}
			/*String sql = "select id,title,pid as parentId ,type "+
				" from (  "+
				" select o.organ_id as id,o.organ_name as title,o.parent_organ_id as pid,'organ' as type,1 as typeorder,o.order_no,o.level_code  "+
				"   from organ o where o.is_validation = 1  "+
				" UNION  ALL  "+
				"  select d.dept_id as id,d.dept_name as title,decode(d.parent_dept_id,null,d.organ_id,d.parent_dept_id) as pid ,  "+
				"  	  'dept' as type,2 as typeorder,d.order_no,d.level_code  "+
				"     from dept d where d.is_validation = 1  "+
				" UNION  ALL  "+
				" select q.quarter_id as id,q.quarter_name as title,decode(q.parent_quarter_id,null,q.dept_id,q.parent_quarter_id) as pid,  "+
				"     'quarter' as type,3 as typeorder,q.order_no,q.level_code  "+
				"     from quarter q where q.is_validation = 1  "+
				//" UNION  ALL  "+
				//" select e.employee_id as id,e.employee_name as title,decode(e.quarter_id,null,e.dept_id,e.quarter_id) as pid,  "+
				//"     'employee' as type,4 as typeorder,e.order_no,e.employee_code  as level_code   "+
				//"     from employee e where e.isvalidation = 1
				" ) t  "+
				" order by t.typeorder,t.order_no,t.level_code ";*/
			String sql = "select id, title, pid as parentId, type,organ_id as organid "+
					  "from (select o.organ_id as id, "+
					               "o.organ_name as title, "+
					               "o.parent_organ_id as pid, "+
					               "'organ' as type, "+
					               "1 as typeorder, "+
					               "o.order_no, "+
					               "o.level_code, "+
					               "o.organ_id as organ_id "+
					          "from organ o "+
					         "where o.is_validation = 1 "+
					        "UNION ALL "+
					        "select distinct d.dept_id as id, "+
				               "d.dept_name as title, "+
				               "decode(d.parent_dept_id, null, d.organ_id, d.parent_dept_id) as pid, "+
				               "'dept' as type, "+
				               "2 as typeorder, "+
				               "d.order_no, "+
				               "d.level_code, "+
				               "d.organ_id as organ_id "+
				          "from (select * from dept dd  where dd.is_validation = 1 "+
					         " and dd.organ_id is not null "+
					         " start with dd.dept_id in (select qq.dept_id from employee qq "
					         + " where qq.isvalidation = 1 and qq.quarter_id is not null "
						   + " and qq.dept_id is not null and qq.quarter_train_id is not null ) "+
					         " connect by prior dd.parent_dept_id = dd.dept_id) d   "+
					        "UNION ALL "+
					        "select distinct q.quarter_train_id || '@' || q.dept_id as id, "+
				               "q.quarter_train_name as title, "+
				               "q.dept_id as pid, "+
				               "'quarter' as type, "+
				               "3 as typeorder, "+
				               "q.order_no, "+
				               "q.level_code, "+
				               "dd.organ_id as organ_id "+
				          "from quarter q,dept dd "+
				         "where q.is_validation = 1 and q.dept_id = dd.dept_id and q.dept_id is not null "
				         + " and q.quarter_train_id is not null "
				         + " and q.quarter_id in(select ee.quarter_id from employee ee where 1=1 ";
					 if(employeeNameTerm!=null && !"".equals(employeeNameTerm)){
						 sql+=" and ee.employee_name like '%' || ? || '%' "; 
				     }    
						 sql+=")) t ";
					 if(organIds!=null && !"".equals(organIds)){
						 sql+=" where organ_id in("+organIds+") "; 
					 }
					sql+=" order by t.typeorder, t.order_no, t.level_code ";
			SQLQuery sqlQuery=session.createSQLQuery(sql);   
			if(employeeNameTerm!=null && !"".equals(employeeNameTerm)){
				sqlQuery.setString(0, employeeNameTerm);
			}
		    List<Object[]> tmplist = sqlQuery.addScalar("id", StringType.INSTANCE)
				.addScalar("title", StringType.INSTANCE)
				.addScalar("parentId", StringType.INSTANCE)
				.addScalar("type", StringType.INSTANCE)
				.addScalar("organid", StringType.INSTANCE)
				.list();
		    Map childMap = new HashMap();
		    Map idMap = new HashMap();
		    for(int i=0;i<tmplist.size();i++){
		    	Object[] object = (Object[])tmplist.get(i);
		    	String id = (String)object[0];
		    	if(idMap.get(id)!=null){
		    		continue;
		    	}
		    	idMap.put(id,"a");
		    	String title = (String)object[1];
		    	String parentId = (String)object[2];
		    	String type = (String)object[3];
		    	if(title == null) title ="其它";
		    	//String organid = (String)object[4];
		    	
		    	//System.out.println(parentId);
		    	//System.out.println(organIdsMap.get(parentId));
		    	if((parentId==null || "".equals(parentId) || id.equals(organId)) && "organ".equals(type)){
			    	TreeNode node = new TreeNode();
			    	node.setId(id);
			    	node.setTitle(title);
			    	node.setParentId(null);
			    	node.setType(type);
			    	node.setChildren(new ArrayList());
			    	
			    	list.add(node);
		    	}else{
		    		TreeNode node = new TreeNode();
			    	node.setId(id);
			    	node.setTitle(title);
			    	node.setParentId(parentId);
			    	node.setType(type);
			    	node.setChildren(new ArrayList());
			    	if("employee".equals(type)){
			    		node.setLeaf(true);
			    	}
			    	
		    		List ls = (List)childMap.get(parentId);
		    		if(ls == null)ls = new ArrayList();
		    		ls.add(node);
		    		childMap.put(parentId,ls);
		    	}
		    }
		    setChildTreeMap(list,childMap);
		    list = removeIsNullNode(list,"quarter");
		    TreeNode.putTypeIncon("organ", "resources/icons/fam/organ.gif", "");
		    TreeNode.putTypeIncon("dept", "resources/icons/fam/dept.gif", "");
		    TreeNode.putTypeIncon("quarter", "resources/icons/fam/user_gray.png", "");
			TreeNode.putTypeIncon("employee", "resources/icons/fam/user_green.gif", "");
			
			if(!StringUtils.isEmpty(deptNameTerm)){//条件过滤
				TreeNode.select(list, deptNameTerm);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}
	
	public static List<TreeNode> removeIsNullNode(List<TreeNode> childs,String notType){
		if(childs==null || childs.size()==0)return childs;
		for(int i=childs.size()-1;i>=0;i--){
			TreeNode node = childs.get(i);
			if(node.getType().indexOf(notType)!=-1){
				continue;
			}
			if(node.getChildren()!=null&&node.getChildren().size()>0){
				node.setChildren(removeIsNullNode(node.getChildren(),notType));
			}
			if(node.getChildren()==null||node.getChildren().size()==0){
				childs.remove(i);
			}
		}
		return childs;
	}
	
	
	private void setChildTreeMap(List<TreeNode> list,Map childMap){
		if(list!=null){
			for(int i=0;i<list.size();i++){
				TreeNode node = (TreeNode)list.get(i);
				List childList = (List)childMap.get(node.getId());
				if(childList!=null){
					node.setChildren(childList);
					setChildTreeMap(childList,childMap);
				}else{
					node.setLeaf(true);
				}
			}
		}
	}
	

	/**
	 * 获取未选中人员的信息
	 * @author 朱健
	 * @param id 选择节点的id
	 * @param type 选择节点的类型 organ-机构 dept-部门 quarter-岗位
	 * @param selectEmpIds 选择的人员
	 * @param inSelectEmpIdsType notIn-排除selectEmpIds  in-在selectEmpIds内 空则不处理
	 * @return
	 * @modified
	 */
	public List getSelectEmp(String id,String type,String selectEmpIds,String inSelectEmpIdsType,Map paramMap){
		List list = new ArrayList();
		try{
			String employeeNameTerm = (String)paramMap.get("employeeNameTerm");

			String sql = null;
			if("dept".equals(type)){
				sql = " select {e.*},{q.*},{d.*},{o.*} from employee e, quarter q, dept d, organ o "+
					   " where e.quarter_id = q.quarter_id "+
					   " and e.dept_id = d.dept_id "+
					   " and d.organ_id = o.organ_id "+ 
					   " and e.isvalidation = 1 "
					+ " and (e.dept_id = ? or d.parent_dept_id = ?) ";
			}else if("quarter".equals(type) && id.indexOf("@")!=-1){
				sql = " select {e.*},{q.*},{d.*},{o.*} from employee e, quarter q, dept d, organ o "
						+ " where e.quarter_id = q.quarter_id "+
					   " and e.dept_id = d.dept_id "+
					   " and d.organ_id = o.organ_id "
					   + " and e.isvalidation = 1 "
						+ " and e.quarter_train_id = ? and e.dept_id = ? ";
			}else if("quarter".equals(type)){
				sql = " select {e.*},{q.*},{d.*},{o.*} from employee e, quarter q, dept d, organ o "
						+ " where e.quarter_id = q.quarter_id "+
					   " and e.dept_id = d.dept_id "+
					   " and d.organ_id = o.organ_id "
					   + " and e.isvalidation = 1 "
						+ " and e.quarter_id = ? ";
			}else if(selectEmpIds!=null && !"".equals(selectEmpIds) && !"null".equals(selectEmpIds)){
				sql = " select {e.*},{q.*},{d.*},{o.*} from employee e, quarter q, dept d, organ o "
						+ " where e.quarter_id = q.quarter_id "+
					   " and e.dept_id = d.dept_id "+
					   " and d.organ_id = o.organ_id "
					   + " and e.isvalidation = 1   ";
			}else{
				return list;
			}
			 if(employeeNameTerm!=null && !"".equals(employeeNameTerm)){
				 sql+=" and e.employee_name like '%' || ? || '%' "; 
		     }
			if(selectEmpIds!=null && !"".equals(selectEmpIds) && !"null".equals(selectEmpIds)){
				String[] selectEmpIdlist = selectEmpIds.split(",");
				if(selectEmpIdlist.length<30){
					if("notIn".equals(inSelectEmpIdsType)){
						sql+=" and instr(',"+selectEmpIds+",' , ',' || e.employee_id || ',') = 0 ";
					}else if("in".equals(inSelectEmpIdsType)){
						sql+=" and instr(',"+selectEmpIds+",' , ',' || e.employee_id || ',') > 0 ";
					}
				}else{
					if("notIn".equals(inSelectEmpIdsType)){
						String empstr = "";
						for(int i=0;i<selectEmpIdlist.length;i++){
							empstr+="'"+selectEmpIdlist[i]+"',";
							if((i>0 && i%30==0) || i==selectEmpIdlist.length-1){
								empstr = empstr.substring(0,empstr.length()-1);
								sql+=" and e.employee_id not in("+empstr+") ";
								empstr="";
							}
						}
						//sql+=" and instr(',"+selectEmpIds+",' , ',' || e.employee_id || ',') = 0 ";
					}else if("in".equals(inSelectEmpIdsType)){
						sql+=" and ( 1=2 ";
						String empstr = "";
						for(int i=0;i<selectEmpIdlist.length;i++){
							empstr+="'"+selectEmpIdlist[i]+"',";
							if((i>0 && i%30==0) || i==selectEmpIdlist.length-1){
								empstr = empstr.substring(0,empstr.length()-1);
								sql+=" or  e.employee_id in("+empstr+") ";
								empstr="";
							}
						}
						sql+=" ) ";
						//sql+=" and instr(',"+selectEmpIds+",' , ',' || e.employee_id || ',') > 0 ";
					}
				}
			}
			
			sql+=" order by e.order_no,e.employee_code ";
			if(sql!=null){
				Session session = template.getSessionFactory().getCurrentSession();
				SQLQuery sqlQuery=session.createSQLQuery(sql);   
				int p = 0;
				if("dept".equals(type)){
					sqlQuery.setString(p++, id);
					sqlQuery.setString(p++, id);
				}else if("quarter".equals(type) && id.indexOf("@")!=-1){
					String[] ids = id.split("@");
					sqlQuery.setString(p++, ids[0]);
					sqlQuery.setString(p++, ids[1]);
				}else if("quarter".equals(type)){
					sqlQuery.setString(p++, id);
				}
				if(employeeNameTerm!=null && !"".equals(employeeNameTerm)){
					sqlQuery.setString(p++, employeeNameTerm);
				}
			    List querylist = sqlQuery.addEntity("e", Employee.class).addEntity("q", Quarter.class)
			    		.addEntity("d", Dept.class).addEntity("o", Organ.class).list();
			    if(querylist!=null && querylist.size()>0){
			    	for(int i=0;i<querylist.size();i++){
			    		Object[] obj = (Object[])querylist.get(i);
			    		Employee employee = (Employee)obj[0];
			    		Quarter quarter = (Quarter)obj[1];
			    		Dept dept = (Dept)obj[2];
			    		Organ organ = (Organ)obj[3];
			    		EmployeeForm employeeForm = new EmployeeForm();
			    		employeeForm.setEmployeeId(employee.getEmployeeId());
			    		employeeForm.setEmployeeName(employee.getEmployeeName());
			    		employeeForm.setQuarterName(employee.getQuarterTrainName()!=null?quarter.getQuarterTrainName() : quarter.getQuarterName());
			    		employeeForm.setQuarterId(quarter.getQuarterId());
			    		employeeForm.setDeptName(dept.getDeptName());
			    		employeeForm.setDeptId(dept.getDeptId());
			    		employeeForm.setOrganName(organ.getOrganName());
			    		employeeForm.setOrganId(organ.getOrganId());
			    		if(employee.getSex() == 0){
			    			employeeForm.setUserSex("1");//['1','男'],['2','女']
			    		}else if(employee.getSex() == 1){
			    			employeeForm.setUserSex("2");//['1','男'],['2','女']
			    		}
			    		
			    		if(employee.getBirthday()!=null){
			    			employeeForm.setUserBirthday(DateUtils.convertDateToStr(employee.getBirthday(),"yyyy-MM-dd"));//生日
			    		}
			    		employeeForm.setIdNumber(employee.getIdentityCard()!=null?employee.getIdentityCard().trim():employee.getIdentityCard());//身份证号
			    		employeeForm.setUserNation(employee.getNationality());////民族
			    		if(employee.getMovePhone()!=null){
			    			employeeForm.setUserPhone(employee.getMovePhone());//手机号
			    		}else{
			    			employeeForm.setUserPhone(employee.getOfficePhone());//手机号
			    		}
			    		
			    		employeeForm.setUserAddr(employee.getAddress());//地址
			    		list.add(employeeForm);
			    	}
			    }
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}
}
