package cn.com.ite.eap2.module.organization.employee;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.type.StringType;

import cn.com.ite.eap2.common.utils.StringUtils;
import cn.com.ite.eap2.core.hibernate.HibernateDefaultDAOImpl;
import cn.com.ite.eap2.core.service.TreeNode;
import cn.com.ite.eap2.domain.organization.Organ;

public class EmployeeDaoImpl  extends HibernateDefaultDAOImpl implements EmployeeDao{
	
	/**
	 * 根据deptId包括的子部门
	 * @author 朱健
	 * @param deptId
	 * @return
	 * @modified
	 */
	public String queryOwnerDeptIds(String deptId){
		String ids = "";
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
		    List<String> tmplist = sqlQuery.addScalar("id", StringType.INSTANCE)
				.list();
		    for(int i=0;i<tmplist.size();i++){
		    	String did = (String)tmplist.get(i);
		    	ids+=did+",";
		    }
		}catch(Exception e){
			e.printStackTrace();
		}
		return ids;
	}

	/**
	 * 本树的岗位采用的培训岗位编码来连接的
	 * 查询机构部门员工树，用于分级下载
	 * @param organId 机构ID
	 * @param employeeName 员工名称
	 * @param parentId 父部门ID
	 * @param parentType 父结点类型
	 * @return
	 * @throws Exception
	 * @modified
	 */
	public List<TreeNode> findPxOrganDeptEmployeeTree(String organId,String employeeName,String parentId,String parentType)throws Exception{
		List<TreeNode> trees = new ArrayList<TreeNode>();
		try{
			TreeNode.putTypeIncon("organ", "resources/icons/fam/organ.gif", "");
			TreeNode.putTypeIncon("employee", "resources/icons/fam/cog.gif", "");
			TreeNode.putTypeIncon("quarter", "resources/icons/fam/grid.png", "");
			TreeNode.putTypeIncon("dept", "resources/icons/fam/dept.gif", "");
			Session session = template.getSessionFactory().getCurrentSession();
			if(parentId==null){
				String sql = null;
				if(StringUtils.isEmpty(organId)){
					sql = "select id, title, pid as parentId, type,organ_id as organid "+
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
							         + " and q.quarter_id in(select ee.quarter_id from employee ee)  "
							         + "UNION ALL "+
								        "select e.employee_id as id, "+
									       "e.employee_name as title,  "+
									       "e.quarter_train_id || '@' || e.dept_id as pid, "+
									       "'employee' as type, "+
									       "4 as typeorder, "+
									       "e.order_no, "+
									       "null as level_code, "+
									       "d.organ_id as organ_id "+
									  "from employee e, dept d "+
									 "where e.isvalidation = 1 "+
									   "and e.dept_id = d.dept_id  and e.quarter_id is not null "
									   + " and e.dept_id is not null and e.quarter_train_id is not null ";
							sql+= " ) t ";
							if(employeeName!=null && !"".equals(employeeName) && !"".equals(employeeName)){
								sql+= " start with t.title = '"+employeeName+"'  connect by prior t.pid = t.id ";
							}
							sql+=" order by t.typeorder, t.order_no, t.level_code ";
				}else{
					sql = "select id, title, pid as parentId, type,organ_id as organid "+
							  "from (select o.organ_id as id, "+
							               "o.organ_name as title, "+
							               "o.parent_organ_id as pid, "+
							               "'organ' as type, "+
							               "1 as typeorder, "+
							               "o.order_no, "+
							               "o.level_code, "+
							               "o.organ_id as organ_id "+
							          "from organ o "+
							         "where o.is_validation = 1 and o.organ_id = '"+organId+"' "+
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
								         " connect by prior dd.parent_dept_id = dd.dept_id) d "+
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
							         + " and q.quarter_train_id is not null  "
							         + " and q.quarter_id in(select ee.quarter_id from employee ee)  "
							         + "UNION ALL "+
								        "select e.employee_id as id, "+
									       "e.employee_name as title,  "+
									       "e.quarter_train_id || '@' || e.dept_id as pid, "+
									       "'employee' as type, "+
									       "4 as typeorder, "+
									       "e.order_no, "+
									       "null as level_code, "+
									       "d.organ_id as organ_id "+
									  "from employee e, dept d "+
									 "where e.isvalidation = 1 "+
									   "and e.dept_id = d.dept_id  and e.quarter_id is not null "
									   + " and e.dept_id is not null and e.quarter_train_id is not null ";
							sql+= " ) t ";
							if(employeeName!=null && !"".equals(employeeName) && !"".equals(employeeName)){
								sql+= " start with t.title = '"+employeeName+"'  connect by prior t.pid = t.id ";
							}
							sql+=" order by t.typeorder, t.order_no, t.level_code ";
					
					
				}
					
				SQLQuery sqlQuery=session.createSQLQuery(sql);   
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
			    	String pid = (String)object[2];
			    	String type = (String)object[3];
	
			    	if(organId!=null && organId.equals(id) && "organ".equals(type)){
			    		TreeNode node = new TreeNode();
				    	node.setId(id);
				    	node.setTitle(title);
				    	node.setParentId(null);
				    	node.setType(type);
				    	node.setChildren(new ArrayList<TreeNode>());
				    	
				    	trees.add(node);
			    	}else if((pid==null || "".equals(pid)) && "organ".equals(type)){
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
				trees = TreeNode.toTree(trees,false,null);
				//this.setLeafNullParent(trees);
			}else{
				String sql = null;
				if(parentType.equals("organ")){
					sql = " select id, title, pid as parentId, type,organ_id as organid "+
					  " from (select o.organ_id as id, "+
					               "o.organ_name as title, "+
					               "o.parent_organ_id as pid, "+
					               "'organ' as type, "+
					               "1 as typeorder, "+
					               "o.order_no, "+
					               "o.level_code, "+
					               "o.organ_id as organ_id "+
					          "from organ o "+
					         "where o.is_validation = 1 and o.organ_id = '"+organId+"' "+
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
								         " and dd.organ_id is not null and dd.organ_id = '"+organId+"' "+
								         " start with dd.dept_id in (select qq.dept_id from employee qq "
								         + " where qq.isvalidation = 1 and qq.quarter_id is not null "
									   + " and qq.dept_id is not null and qq.quarter_train_id is not null ) "+
								         " connect by prior dd.parent_dept_id = dd.dept_id)  d "+
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
					         + " and q.quarter_train_id is not null  "
					         + " and q.quarter_id in(select ee.quarter_id from employee ee) "
					         + "UNION ALL "+
						        "select e.employee_id as id, "+
							       "e.employee_name as title,  "+
							       "e.quarter_train_id || '@' || e.dept_id as pid, "+
							       "'employee' as type, "+
							       "4 as typeorder, "+
							       "e.order_no, "+
							       "null as level_code, "+
							       "d.organ_id as organ_id "+
							  "from employee e, dept d "+
							 "where e.isvalidation = 1 "+
							   "and e.dept_id = d.dept_id  and e.quarter_id is not null "
							   + " and e.dept_id is not null and e.quarter_train_id is not null ";
					sql+= " ) t ";
					if(employeeName!=null && !"".equals(employeeName) && !"".equals(employeeName)){
						sql+= " start with t.title = '"+employeeName+"'  connect by prior t.pid = t.id ";
					}
					sql+=" order by t.typeorder, t.order_no, t.level_code ";
					
							SQLQuery sqlQuery=session.createSQLQuery(sql);   
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
				}else{
					 sql = "select id, title, pid as parentId, type,organ_id as organid "+
							  "from ( select d.dept_id as id, "+
							               "d.dept_name as title, "+
							               "d.parent_dept_id as pid, "+
							               "'dept' as type, "+
							               "2 as typeorder, "+
							               "d.order_no, "+
							               "d.level_code, "+
							               "d.organ_id as organ_id "+
							          "from (select * from dept dd  where dd.is_validation = 1 "+
								         " and dd.organ_id is not null   "+
								         " start with dd.dept_id in (select qq.dept_id from employee qq "
								         + " where qq.isvalidation = 1 and qq.quarter_id is not null "
									   + " and qq.dept_id is not null and qq.quarter_train_id is not null ) "+
								         " connect by prior dd.parent_dept_id = dd.dept_id) d "+
							          " start with d.parent_dept_id = '"+parentId+"' "+
							            " connect by prior d.dept_id = d.parent_dept_id  " + 
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
							         "where q.is_validation = 1 and q.dept_id is not null "
							         + " and q.quarter_train_id is not null "
							         + " and q.quarter_id in(select ee.quarter_id from employee ee) "
					         + "UNION ALL "+
						        "select e.employee_id as id, "+
							       "e.employee_name as title,  "+
							       "e.quarter_train_id || '@' || e.dept_id as pid, "+
							       "'employee' as type, "+
							       "4 as typeorder, "+
							       "e.order_no, "+
							       "null as level_code, "+
							       "d.organ_id as organ_id "+
							  "from employee e, dept d "+
							 "where e.isvalidation = 1 "+
							   "and e.dept_id = d.dept_id  and e.quarter_id is not null "
							   + " and e.dept_id is not null and e.quarter_train_id is not null";
							sql+= " ) t ";
							if(employeeName!=null && !"".equals(employeeName) && !"".equals(employeeName)){
								sql+= " start with t.title = '"+employeeName+"'  connect by prior t.pid = t.id ";
							}
							sql+=" order by t.typeorder, t.order_no, t.level_code ";
							SQLQuery sqlQuery=session.createSQLQuery(sql);   
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
						    	String pid = (String)object[2];
						    	String type = (String)object[3];
				
						    	if((pid==null || "".equals(pid)) && "dept".equals(type)){
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
				}
			}
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
	public List<TreeNode> findOrganDeptEmployeeTree(String parentId,String employeeName)throws Exception{
		List<TreeNode> trees = new ArrayList<TreeNode>();
		try{
			Session session = template.getSessionFactory().getCurrentSession();
			Organ organ = (Organ)this.findEntityBykey(Organ.class, parentId);
			Map<String,List<TreeNode>> childMap = new HashMap<String,List<TreeNode>>();
			if(organ!=null){
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
						         "where o.is_validation = 1 and o.organ_id = '"+parentId+"' "+
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
						         "where d.is_validation = 1 and d.organ_id = '"+parentId+"' "+
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
						         "where q.is_validation = 1 and q.dept_id = dd.dept_id    "
							         + "UNION ALL "+
								        "select e.employee_id as id, "+
									       "e.employee_name as title,  "+
									       "decode(e.quarter_id, null, e.dept_id, e.quarter_id) as pid, "+
									       "'employee' as type, "+
									       "4 as typeorder, "+
									       "e.order_no, "+
									       "null as level_code, "+
									       "d.organ_id as organ_id "+
									  "from employee e, dept d "+
									 "where e.isvalidation = 1 "+
									   "and e.dept_id = d.dept_id ";
						sql+= " ) t ";
						if(employeeName!=null && !"".equals(employeeName) && !"".equals(employeeName)){
							sql+= " start with t.title = '"+employeeName+"'  connect by prior t.pid = t.id ";
						}
						sql+=" order by t.typeorder, t.order_no, t.level_code ";
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

			    	if((pid==null || "".equals(pid)) && "organ".equals(type)){
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
			}else{
				String sql = "select id, title, pid as parentId, type,organ_id as organid "+
						  "from ( select d.dept_id as id, "+
							               "d.dept_name as title, "+
							               "d.parent_dept_id as pid, "+
							               "'dept' as type, "+
							               "2 as typeorder, "+
							               "d.order_no, "+
							               "d.level_code, "+
							               "d.organ_id as organ_id "+
							          "from dept d "+
							         "where d.is_validation = 1 start with d.parent_dept_id = '"+parentId+"' "+
							            " connect by prior d.dept_id = d.parent_dept_id  "+
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
						         "where q.is_validation = 1 and q.dept_id = dd.dept_id   "
							         + "UNION ALL "+
								        "select e.employee_id as id, "+
									       "e.employee_name as title,  "+
									       "decode(e.quarter_id, null, e.dept_id, e.quarter_id) as pid, "+
									       "'employee' as type, "+
									       "4 as typeorder, "+
									       "e.order_no, "+
									       "null as level_code, "+
									       "d.organ_id as organ_id "+
									  "from employee e, dept d "+
									 "where e.isvalidation = 1 "+
									   "and e.dept_id = d.dept_id ";
						sql+= " ) t ";
						if(employeeName!=null && !"".equals(employeeName) && !"".equals(employeeName)){
							sql+= " start with t.title = '"+employeeName+"'  connect by prior t.pid = t.id ";
						}
						sql+=" order by t.typeorder, t.order_no, t.level_code ";
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

			    	if((pid==null || "".equals(pid)) && "dept".equals(type)){
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
	 * 查询机构部门员工树，用于分级下载
	 * @param organId 机构ID
	 * @param employeeName 员工名称
	 * @param parentId 父部门ID
	 * @param parentType 父结点类型
	 * @return
	 * @throws Exception
	 * @modified
	 */
	public List<TreeNode> findOrganDeptEmployeeTree(String organId,String employeeName,String parentId,String parentType)throws Exception{
		List<TreeNode> trees = new ArrayList<TreeNode>();
		try{
			TreeNode.putTypeIncon("organ", "resources/icons/fam/organ.gif", "");
			TreeNode.putTypeIncon("employee", "resources/icons/fam/cog.gif", "");
			TreeNode.putTypeIncon("quarter", "resources/icons/fam/grid.png", "");
			TreeNode.putTypeIncon("dept", "resources/icons/fam/dept.gif", "");
			Session session = template.getSessionFactory().getCurrentSession();
			if(parentId==null){
				String sql = null;
				if(StringUtils.isEmpty(organId)){
					sql = "select id, title, pid as parentId, type,organ_id as organid "+
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
							         "where q.is_validation = 1 and q.dept_id = dd.dept_id    "
							         + "UNION ALL "+
								        "select e.employee_id as id, "+
									       "e.employee_name as title,  "+
									       "decode(e.quarter_id, null, e.dept_id, e.quarter_id) as pid, "+
									       "'employee' as type, "+
									       "4 as typeorder, "+
									       "e.order_no, "+
									       "null as level_code, "+
									       "d.organ_id as organ_id "+
									  "from employee e, dept d "+
									 "where e.isvalidation = 1 "+
									   "and e.dept_id = d.dept_id ";
							sql+= " ) t ";
							if(employeeName!=null && !"".equals(employeeName) && !"".equals(employeeName)){
								sql+= " start with t.title = '"+employeeName+"'  connect by prior t.pid = t.id ";
							}
							sql+=" order by t.typeorder, t.order_no, t.level_code ";
				}else{
					sql = "select id, title, pid as parentId, type,organ_id as organid "+
							  "from (select o.organ_id as id, "+
							               "o.organ_name as title, "+
							               "o.parent_organ_id as pid, "+
							               "'organ' as type, "+
							               "1 as typeorder, "+
							               "o.order_no, "+
							               "o.level_code, "+
							               "o.organ_id as organ_id "+
							          "from organ o "+
							         "where o.is_validation = 1 and o.organ_id = '"+organId+"' "+
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
							         "where d.is_validation = 1 and d.organ_id = '"+organId+"' "+
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
							         "where q.is_validation = 1 and q.dept_id = dd.dept_id  "
							         + "UNION ALL "+
								        "select e.employee_id as id, "+
									       "e.employee_name as title,  "+
									       "decode(e.quarter_id, null, e.dept_id, e.quarter_id) as pid, "+
									       "'employee' as type, "+
									       "4 as typeorder, "+
									       "e.order_no, "+
									       "null as level_code, "+
									       "d.organ_id as organ_id "+
									  "from employee e, dept d "+
									 "where e.isvalidation = 1 "+
									   "and e.dept_id = d.dept_id ";
							sql+= " ) t ";
							if(employeeName!=null && !"".equals(employeeName) && !"".equals(employeeName)){
								sql+= " start with t.title = '"+employeeName+"'  connect by prior t.pid = t.id ";
							}
							sql+=" order by t.typeorder, t.order_no, t.level_code ";
					
					
				}
					
				SQLQuery sqlQuery=session.createSQLQuery(sql);   
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
			    	String pid = (String)object[2];
			    	String type = (String)object[3];
	
			    	if(organId!=null && organId.equals(id) && "organ".equals(type)){
			    		TreeNode node = new TreeNode();
				    	node.setId(id);
				    	node.setTitle(title);
				    	node.setParentId(null);
				    	node.setType(type);
				    	node.setChildren(new ArrayList<TreeNode>());
				    	
				    	trees.add(node);
			    	}else if((pid==null || "".equals(pid)) && "organ".equals(type)){
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
				trees = TreeNode.toTree(trees,false,null);
				//this.setLeafNullParent(trees);
			}else{
				String sql = null;
				if(parentType.equals("organ")){
					sql = " select id, title, pid as parentId, type,organ_id as organid "+
					  " from (select o.organ_id as id, "+
					               "o.organ_name as title, "+
					               "o.parent_organ_id as pid, "+
					               "'organ' as type, "+
					               "1 as typeorder, "+
					               "o.order_no, "+
					               "o.level_code, "+
					               "o.organ_id as organ_id "+
					          "from organ o "+
					         "where o.is_validation = 1 and o.organ_id = '"+organId+"' "+
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
					         "where d.is_validation = 1 and d.organ_id = '"+organId+"' "+
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
					         "where q.is_validation = 1 and q.dept_id = dd.dept_id  "
					         + "UNION ALL "+
						        "select e.employee_id as id, "+
							       "e.employee_name as title,  "+
							       "decode(e.quarter_id, null, e.dept_id, e.quarter_id) as pid, "+
							       "'employee' as type, "+
							       "4 as typeorder, "+
							       "e.order_no, "+
							       "null as level_code, "+
							       "d.organ_id as organ_id "+
							  "from employee e, dept d "+
							 "where e.isvalidation = 1 "+
							   "and e.dept_id = d.dept_id ";
					sql+= " ) t ";
					if(employeeName!=null && !"".equals(employeeName) && !"".equals(employeeName)){
						sql+= " start with t.title = '"+employeeName+"'  connect by prior t.pid = t.id ";
					}
					sql+=" order by t.typeorder, t.order_no, t.level_code ";
					
							SQLQuery sqlQuery=session.createSQLQuery(sql);   
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
				}else{
					 sql = "select id, title, pid as parentId, type,organ_id as organid "+
							  "from ( select d.dept_id as id, "+
							               "d.dept_name as title, "+
							               "d.parent_dept_id as pid, "+
							               "'dept' as type, "+
							               "2 as typeorder, "+
							               "d.order_no, "+
							               "d.level_code, "+
							               "d.organ_id as organ_id "+
							          "from dept d "+
							         "where d.is_validation = 1 start with d.parent_dept_id = '"+parentId+"' "+
							            " connect by prior d.dept_id = d.parent_dept_id  " + 
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
							         "where q.is_validation = 1   "
					         + "UNION ALL "+
						        "select e.employee_id as id, "+
							       "e.employee_name as title,  "+
							       "decode(e.quarter_id, null, e.dept_id, e.quarter_id) as pid, "+
							       "'employee' as type, "+
							       "4 as typeorder, "+
							       "e.order_no, "+
							       "null as level_code, "+
							       "d.organ_id as organ_id "+
							  "from employee e, dept d "+
							 "where e.isvalidation = 1 "+
							   "and e.dept_id = d.dept_id ";
							sql+= " ) t ";
							if(employeeName!=null && !"".equals(employeeName) && !"".equals(employeeName)){
								sql+= " start with t.title = '"+employeeName+"'  connect by prior t.pid = t.id ";
							}
							sql+=" order by t.typeorder, t.order_no, t.level_code ";
							SQLQuery sqlQuery=session.createSQLQuery(sql);   
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
						    	String pid = (String)object[2];
						    	String type = (String)object[3];
				
						    	if((pid==null || "".equals(pid)) && "dept".equals(type)){
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
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return trees;
	}
	
	
	/*private void setLeafNullParent(List<TreeNode> trees){
		if(trees!=null)
		for(TreeNode node:trees){
			if(node.getChildren()==null||node.getChildren().size()==0){
				node.setLeaf(false);
				node.setChildren(null);
			}else
				this.setLeafNullParent(node.getChildren());
		}
	}*/
	
	private void setChildTreeMap(List<TreeNode> list,Map<String,List<TreeNode>> childMap){
		if(list!=null){
			for(int i=0;i<list.size();i++){
				TreeNode node = (TreeNode)list.get(i);
				List<TreeNode> childList = (List<TreeNode>)childMap.get(node.getId());
				if(childList!=null && childList.size()>0){
					node.setChildren(childList);
					setChildTreeMap(childList,childMap);
				}else{
					node.setLeaf(true);
				}
			}
		}
	}

}
