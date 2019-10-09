package cn.com.ite.eap2.module.organization.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.type.StringType;

import cn.com.ite.eap2.core.hibernate.HibernateDefaultDAOImpl;
import cn.com.ite.eap2.core.service.TreeNode;
import cn.com.ite.eap2.domain.organization.Dept;
import cn.com.ite.eap2.domain.organization.Employee;

public class ExampleDaoImpl extends HibernateDefaultDAOImpl implements ExampleDao{
	
	@Override
	public List<TreeNode> findOrganDeptTree(String topOrganId,String deptTerm,String employeeTerm,String postTerm)throws Exception{
		List<TreeNode> trees = new ArrayList<TreeNode>();
		try{
			Map<String,List<TreeNode>> childMap = new HashMap<String,List<TreeNode>>();
			Session session = template.getSessionFactory().getCurrentSession();
			String sql = "select distinct id, title, pid as parentId, type,organ_id as organid,t.typeorder, t.order_no, t.level_code  "+
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
						         "where d.is_validation = 1 ";
							sql+=" ) t";
							
							sql+=" start with 1=1";
							if(deptTerm!=null &&!"".equals(deptTerm)){
								sql+=" and t.title like '%"+deptTerm+"%'  ";
							}
							if(employeeTerm!=null&&!"".equals(employeeTerm)){
								sql+=" and t.id in(select e.dept_id from employee e where e.employee_name like '%"+employeeTerm+"%')  ";
							}
							if(postTerm!=null&&!"".equals(postTerm)){
								sql+=" and t.id in(select e.dept_id from employee e where e.quarter_train_name like '%"+postTerm+"%')  ";
							}
							sql+=" connect by prior t.pid = t.id  ";
						    sql+= " order by t.typeorder, t.order_no, t.level_code ";
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
	@Override
	public List getPerson(String selectId, String deptId,String deptTerm,String employeeTerm,String postTerm) {
		String sql ="select e.employee_id as employeeId,e.dept_id as deptId,e.employee_name as employeeName,d.dept_name as deptName,e.quarter_train_name as querterTrainName"
				+ " from EMPLOYEE e,dept d  where e.dept_id=d.dept_id";
				if(deptTerm!=null&&!"".equals(deptTerm)){
					sql+=" and d.dept_name like '%"+deptTerm+"%'";
				}
				if(employeeTerm!=null&&!"".equals(employeeTerm)){
					sql+=" and e.employee_name like '%"+employeeTerm+"%'";
				}
				if(postTerm!=null&&!"".equals(postTerm)){
					sql+=" and e.quarter_train_name like '%"+postTerm+"%'";
				}
				if(deptId!=null&&!deptId.equals("")){
				sql+= " and e.dept_id in (select t.dept_id from dept t start with t.dept_id = '"+deptId+"'"
						+ " connect by prior t.dept_id = t.parent_dept_id)";
				}
				if(selectId!=null&&!selectId.equals("")){
					sql+= " and instr(',' || '"+selectId+"' || ',',',' || e.employee_id || ',') = 0";
				}
				sql+=" order by deptName desc";
		 List lists = new ArrayList<>();
		 List list = this.querySql(sql, null, null, null);
		 for (int i = 0; i < list.size(); i++) {
			Map m = (Map) list.get(i);
			Employee info = new Employee();
			info.setEmployeeId((String) m.get("EMPLOYEEID"));
			info.setEmployeeName((String) m.get("EMPLOYEENAME"));
			info.setQuarterTrainName((String) m.get("QUERTERTRAINNAME"));
			Dept d = new Dept();
			d.setDeptId((String) m.get("DEPTID"));
			d.setDeptName((String) m.get("DEPTNAME"));
			info.setDept(d);
			lists.add(info);
		}
		return lists;
	}
}
