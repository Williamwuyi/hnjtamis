package cn.com.ite.hnjtamis.exam.base.exammarkpeopleinfo.dao;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.type.StringType;

import cn.com.ite.eap2.core.hibernate.HibernateDefaultDAOImpl;
import cn.com.ite.eap2.core.service.TreeNode;
import cn.com.ite.eap2.domain.organization.Dept;
import cn.com.ite.eap2.domain.organization.Employee;
import cn.com.ite.eap2.domain.organization.Organ;
import cn.com.ite.eap2.domain.organization.Quarter;
import cn.com.ite.hnjtamis.baseinfo.domain.Speciality;


/**
 *
 * @author 朱健
 * @create time: 2016年1月13日 上午9:09:47
 * @version 1.0
 * 
 * @modified records:
 */
public class ExamMarkPeopleInfoDaoImpl extends HibernateDefaultDAOImpl implements
	ExamMarkPeopleInfoDao {
	
	
	/**
	 * 查询阅卷老师
	 * @author 朱健
	 * @return
	 * @modified
	 */
	public List getExaminationUserList(String organId){
		List list = new ArrayList();
		try{
			String sql = " select {e.*},{q.*},{d.*},{o.*} from employee e, quarter q, dept d, organ o "+
					   " where e.quarter_id = q.quarter_id "+
					   " and e.dept_id = d.dept_id "+
					   " and d.organ_id = o.organ_id "+ 
					   " and e.isvalidation = 1 " + 
					  " and e.employee_id in(select t.employee_id from talent_registration t where t.is_del = 0 )";
			if(organId!=null && !"".equals(organId) && !"null".equals(organId)){
				sql+=" and o.organ_id = '"+organId+"' ";
			}
			sql+=" order by o.order_no,o.level_code,d.order_no,d.level_code,"
					  + " q.order_no,q.level_code,e.order_no,e.employee_name ";
			Session session = template.getSessionFactory().getCurrentSession();
			SQLQuery sqlQuery=session.createSQLQuery(sql);   
			List querylist = sqlQuery.addEntity("e", Employee.class).addEntity("q", Quarter.class)
		    		.addEntity("d", Dept.class).addEntity("o", Organ.class).list();
			if(querylist!=null && querylist.size()>0){
				Map<String,TreeNode> treeNodeValue = new HashMap<String,TreeNode>();
				Map baseMap = new HashMap();
				//Map employeeInfoMap = getEmployeeInfoMap();
		    	for(int i=0;i<querylist.size();i++){
		    		Object[] obj = (Object[])querylist.get(i);
		    		Employee employee = (Employee)obj[0];
		    		Quarter quarter = (Quarter)obj[1];
		    		Dept dept = (Dept)obj[2];
		    		Organ organ = (Organ)obj[3];
		    		
		    		TreeNode organNode = treeNodeValue.get(organ.getOrganId());
		    		if(organNode == null){
		    			organNode = this.setOrganNodeInList(list, organ, baseMap, treeNodeValue);
				    	
				    	treeNodeValue.put(organ.getOrganId(),organNode);
		    		}
		    		TreeNode deptNode = treeNodeValue.get(dept.getDeptId());
		    		if(deptNode == null){
		    			deptNode = setDeptTreeNode(list,dept,baseMap,treeNodeValue);
		    		}
		    		
		    		
		    		TreeNode quarterNode = treeNodeValue.get(quarter.getQuarterId());
		    		if(quarterNode == null){
		    			quarterNode = setQuarterTreeNode(list,quarter,baseMap,treeNodeValue);
		    		}
		    		
		    		
		    		TreeNode employeeNode = new TreeNode();
		    		employeeNode.setId(employee.getEmployeeId());
		    		employeeNode.setTitle(employee.getEmployeeName());
		    		
		    		employeeNode.setType("employee");
		    		employeeNode.setChildren(new ArrayList());
		    		employeeNode.setLeaf(true);
		    		if(employee.getQuarter()!=null){
		    			employeeNode.setParentId(quarterNode.getId());
		    			quarterNode.getChildren().add(employeeNode);
		    		}else{
		    			employeeNode.setParentId(deptNode.getId());
		    			deptNode.getChildren().add(employeeNode);
		    		}
		    	}
			}else{
				TreeNode organNode = new TreeNode();
				organNode.setId("aaa");
				organNode.setTitle("本机构没有对应专家信息");
				organNode.setParentId(null);
				organNode.setType("organ");
				organNode.setChildren(new ArrayList());
				list.add(organNode);
			}
			
			
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
	 * 查询阅卷老师
	 * @author 朱健
	 * @return
	 * @modified
	 */
	public List getExaminationUserInExamList(String examId){
		List list = new ArrayList();
		//建议安排的阅卷人，阅卷人对应试卷的题库的
		TreeNode td1 = getExaminationUserHaveInBank(examId);
		if(td1!=null){
			list.add(td1);
		}
		//建议安排的阅卷人，阅卷人没有对应试卷的题库的
		TreeNode td2 = getExaminationUserNotHaveInBank(examId);
		if(td2!=null){
			list.add(td2);
		}
		return list;
	}
	
	/**
	 * 查询阅卷老师(还包括本机构的其它人员)
	 * @author 朱健
	 * @return
	 * @modified
	 */
	public List getExaminationUserInExamListEx(String examId,String organId,String nameTerm){
		List list = new ArrayList();
		//建议安排的阅卷人，阅卷人对应试卷的题库的
		TreeNode td1 = getExaminationUserHaveInBank(examId);
		if(td1!=null){
			list.add(td1);
		}
		//建议安排的阅卷人，阅卷人没有对应试卷的题库的
		TreeNode td2 = getExaminationUserNotHaveInBank(examId);
		if(td2!=null){
			list.add(td2);
		}
		
		TreeNode td3 = getOtherNotInExaminationUser(examId,organId,nameTerm);
		if(td3!=null){
			list.add(td3);
		}
		
		return list;
	}
	
	
	private TreeNode getExaminationUserHaveInBank(String examId){
		TreeNode parentNode = null;
		try{

			String sql = " select {e.*},{o.*}  from employee e, quarter q, dept d, organ o "+
					   " where e.quarter_id = q.quarter_id "+
					   " and e.dept_id = d.dept_id "+
					   " and d.organ_id = o.organ_id "+ 
					   " and e.isvalidation = 1 "+
					  " and e.employee_id in(select t.employee_id from talent_registration t where  " +
					  "   t.talent_id in "+
				" (select rb.talent_id "+
			  " from (select tss.theme_bank_id "+
			          " from testpaper_skey tss "+
			         " where tss.testpaper_id in "+
			               " (select et.testpaper_id from EXAM et where et.exam_id = ?) "+
			           " and tss.theme_bank_id is not null) p, "+
			       " talent_registration_bank rb "+
			 " where rb.bank_id = p.theme_bank_id ) and t.is_audited = 1 and t.is_del = 0)"+
				" order by o.order_no,o.level_code,d.order_no,d.level_code,"
					  + " q.order_no,q.level_code,e.order_no,e.employee_name ";
			Session session = template.getSessionFactory().getCurrentSession();
			SQLQuery sqlQuery=session.createSQLQuery(sql);   
			sqlQuery.setString(0, examId);
			List querylist = sqlQuery.addEntity("e", Employee.class).addEntity("o", Organ.class)
					.list();//.addEntity("b",Speciality.class)
			if(querylist!=null && querylist.size()>0){
				Map<String,TreeNode> treeNodeValue = new HashMap<String,TreeNode>();
				Map baseMap = new HashMap();
				//Map employeeInfoMap = getEmployeeInfoMap();
		    	for(int i=0;i<querylist.size();i++){
		    		Object[] obj = (Object[])querylist.get(i);
		    		Employee employee = (Employee)obj[0];
		    		//Quarter quarter = (Quarter)obj[1];
		    		//Dept dept = (Dept)obj[2];
		    		Organ organ = (Organ)obj[1];
		    		//Speciality speciality = (Speciality)obj[2];
		    		
		    		String oid = "jy"+organ.getOrganId();
		    		TreeNode organNode = treeNodeValue.get(oid);
		    		if(organNode == null){
		    			if(parentNode == null){
		    				parentNode = new TreeNode();
		    				parentNode.setId("haveInBank");
		    				parentNode.setTitle("建议安排的阅卷人（专家）");
		    				parentNode.setParentId(null);
		    				parentNode.setType("organ");
		    				parentNode.setChildren(new ArrayList());
		    			}
		    			organNode = this.setOrganNode(parentNode, organ, baseMap, treeNodeValue);
		    			organNode.setId(oid);
				    	treeNodeValue.put(oid,organNode);
		    		}
		    		
		    		TreeNode employeeNode = new TreeNode();
		    		employeeNode.setId(employee.getEmployeeId());
		    		employeeNode.setTitle(employee.getEmployeeName());
		    		employeeNode.setParentId(oid);
		    		employeeNode.setType("employee");
		    		employeeNode.setChildren(new ArrayList());
		    		employeeNode.setLeaf(true);
		    		organNode.getChildren().add(employeeNode);
	
		    	}
			}
			
			
			TreeNode.putTypeIncon("organ", "resources/icons/fam/organ.gif", "");
		    TreeNode.putTypeIncon("dept", "resources/icons/fam/dept.gif", "");
		    TreeNode.putTypeIncon("quarter", "resources/icons/fam/user_gray.png", "");
			TreeNode.putTypeIncon("employee", "resources/icons/fam/user_green.gif", "");
		}catch(Exception e){
			e.printStackTrace();
		}
		return parentNode;
	}
	private TreeNode getExaminationUserNotHaveInBank(String examId){
		TreeNode parentNode = null;
		try{
			/*String sql = " select {e.*},{q.*},{d.*},{o.*} from employee e, quarter q, dept d, organ o "+
					   " where e.quarter_id = q.quarter_id "+
					   " and e.dept_id = d.dept_id "+
					   " and d.organ_id = o.organ_id "+ 
					   " and e.isvalidation = 1 " + 
					  " and e.employee_id in(select t.employee_id from talent_registration t where  " +
					  "   t.talent_id in "+
				" (select rb.talent_id "+
			  " from (select tss.theme_bank_id "+
			          " from testpaper_skey tss "+
			         " where tss.testpaper_id in "+
			               " (select et.testpaper_id from EXAM et where et.exam_id = ?) "+
			           " and tss.theme_bank_id is not null) p, "+
			       " talent_registration_bank rb "+
			 " where rb.bank_id = p.theme_bank_id ) and t.is_audited = 1 and t.is_del = 0)"+
				" order by o.order_no,o.level_code,d.order_no,d.level_code,"
					  + " q.order_no,q.level_code,e.order_no,e.employee_name ";*/
			String sql = " select {e.*},{o.*}  from employee e, quarter q, dept d, organ o,"
					+ " (select t.employee_id from talent_registration t where  " +
					  "   t.talent_id not in "+
				" (select rb.talent_id "+
			  " from (select tss.theme_bank_id "+
			          " from testpaper_skey tss "+
			         " where tss.testpaper_id in "+
			               " (select et.testpaper_id from EXAM et where et.exam_id = ?) "+
			           " and tss.theme_bank_id is not null) p, "+
			       " talent_registration_bank rb "+
			 " where rb.bank_id = p.theme_bank_id ) and t.is_audited = 1 and t.is_del = 0) ttt "+
					   " where e.quarter_id = q.quarter_id "+
					   " and e.dept_id = d.dept_id "+
					   " and d.organ_id = o.organ_id "+ 
					   " and e.isvalidation = 1 "+
					  " and e.employee_id  = ttt.employee_id "+
				" order by o.order_no,o.level_code,d.order_no,d.level_code,"
					  + " q.order_no,q.level_code,e.order_no,e.employee_name ";
			Session session = template.getSessionFactory().getCurrentSession();
			SQLQuery sqlQuery=session.createSQLQuery(sql);   
			sqlQuery.setString(0, examId);
			List querylist = sqlQuery.addEntity("e", Employee.class).addEntity("o", Organ.class)
					.list();//.addEntity("b",Speciality.class)
			if(querylist!=null && querylist.size()>0){
				Map<String,TreeNode> treeNodeValue = new HashMap<String,TreeNode>();
				Map baseMap = new HashMap();
				//Map employeeInfoMap = getEmployeeInfoMap();
		    	for(int i=0;i<querylist.size();i++){
		    		Object[] obj = (Object[])querylist.get(i);
		    		Employee employee = (Employee)obj[0];
		    		//Quarter quarter = (Quarter)obj[1];
		    		//Dept dept = (Dept)obj[2];
		    		Organ organ = (Organ)obj[1];
		    		//Speciality speciality = (Speciality)obj[2];
		    		
		    		String oid = "qt"+organ.getOrganId();
		    		TreeNode organNode = treeNodeValue.get(oid);
		    		if(organNode == null){
		    			if(parentNode == null){
		    				parentNode = new TreeNode();
		    				parentNode.setId("unhavethBank");
		    				parentNode.setTitle("其它阅卷人（专家）");
		    				parentNode.setParentId(null);
		    				parentNode.setType("organ");
		    				parentNode.setChildren(new ArrayList());
		    			}
		    			organNode = this.setOrganNode(parentNode, organ, baseMap, treeNodeValue);
		    			organNode.setId(oid);
				    	treeNodeValue.put(oid,organNode);
		    		}
		    		/*TreeNode deptNode = treeNodeValue.get(dept.getDeptId());
		    		if(deptNode == null){
		    			deptNode = setDeptTreeNode(list,dept,baseMap,treeNodeValue);
		    		}
		    		
		    		
		    		TreeNode quarterNode = treeNodeValue.get(quarter.getQuarterId());
		    		if(quarterNode == null){
		    			quarterNode = setQuarterTreeNode(list,quarter,baseMap,treeNodeValue);
		    		}*/
		    		//TreeNode specialityNode = treeNodeValue.get(speciality.getSpecialityid());
		    		//if(specialityNode == null){
		    			//specialityNode = setSpecialityTreeNode(list,speciality,organ,baseMap,treeNodeValue);
		    		//}
		    		
		    		
		    		
		    		TreeNode employeeNode = new TreeNode();
		    		employeeNode.setId(employee.getEmployeeId());
		    		employeeNode.setTitle(employee.getEmployeeName());
		    		employeeNode.setParentId(oid);
		    		employeeNode.setType("employee");
		    		employeeNode.setChildren(new ArrayList());
		    		employeeNode.setLeaf(true);
		    		organNode.getChildren().add(employeeNode);
		    		/*if(employee.getQuarter()!=null){
		    			employeeNode.setParentId(quarterNode.getId());
		    			quarterNode.getChildren().add(employeeNode);
		    		}else{
		    			employeeNode.setParentId(deptNode.getId());
		    			deptNode.getChildren().add(employeeNode);
		    		}*/
		    	}
			}
			
			
			TreeNode.putTypeIncon("organ", "resources/icons/fam/organ.gif", "");
		    TreeNode.putTypeIncon("dept", "resources/icons/fam/dept.gif", "");
		    TreeNode.putTypeIncon("quarter", "resources/icons/fam/user_gray.png", "");
			TreeNode.putTypeIncon("employee", "resources/icons/fam/user_green.gif", "");
		}catch(Exception e){
			e.printStackTrace();
		}
		return parentNode;
	}
	
	private TreeNode getOtherNotInExaminationUser(String examId,String organId,String nameTerm){
		TreeNode parentNode = null;
		try{
			Organ organ = (Organ)this.findEntityBykey(Organ.class, organId);
			String sql = "";
			if(organ!=null && organ.getOrgan()==null){
				sql = "select id, title, pid as parentId, type,organ_id as organid "+
						  "from (select 'organs' || o.organ_id as id, "+
						               "o.organ_name as title, "+
						               " 'organs' || o.parent_organ_id as pid, "+
						               "'organ' as type, "+
						               "1 as typeorder, "+
						               "o.order_no, "+
						               "o.level_code, "+
						               "o.organ_id as organ_id "+
						          "from organ o "+
						         "where o.is_validation = 1 "+
						        "UNION ALL "+
						        "select 'depts' || d.dept_id as id, "+
						               "d.dept_name as title, "+
						               "decode(d.parent_dept_id, null, 'organs' || d.organ_id, 'depts' || d.parent_dept_id) as pid, "+
						               "'dept' as type, "+
						               "2 as typeorder, "+
						               "d.order_no, "+
						               "d.level_code, "+
						               "d.organ_id as organ_id "+
						          "from dept d "+
						         "where d.is_validation = 1 "+
						        "UNION ALL "+
						        "select 'quarters' || q.quarter_id as id, "+
						               "q.quarter_name as title, "+
						               " decode(q.parent_quarter_id, "+
						                      "null, "+
						                      "'depts' || q.dept_id, "+
						                      "'quarters' || q.parent_quarter_id) as pid, "+
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
								       "'oo' || decode(e.quarter_id, null, 'depts' || e.dept_id, 'quarters' || e.quarter_id) as pid, "+
								       "'employee' as type, "+
								       "4 as typeorder, "+
								       "e.order_no, "+
								       "null as level_code, "+
								       "d.organ_id as organ_id "+
								  "from employee e, dept d "+
								 "where e.isvalidation = 1 "+
								   "and e.dept_id = d.dept_id and e.employee_id not in"
								   + " (select ttt.employee_id from talent_registration ttt where ttt.is_audited = 1 and ttt.is_del = 0 )";
						sql+= " ) t ";
						/*if(nameTerm!=null && !"".equals(nameTerm) && !"".equals(nameTerm)){
							sql+= " start with t.title like '%"+nameTerm+"%'  connect by prior t.pid = t.id ";
						}*/
						sql+=" order by t.typeorder, t.order_no, t.level_code ";
			}else{
				sql = "select id, title, pid as parentId, type,organ_id as organid "+
						  "from (select 'organs' ||  o.organ_id as id, "+
						               "o.organ_name as title, "+
						               "'organs' ||  o.parent_organ_id as pid, "+
						               "'organ' as type, "+
						               "1 as typeorder, "+
						               "o.order_no, "+
						               "o.level_code, "+
						               "o.organ_id as organ_id "+
						          "from organ o "+
						         "where o.is_validation = 1 and o.organ_id = '"+organId+"' "+
						        "UNION ALL "+
						        "select 'depts' ||  d.dept_id as id, "+
						               "d.dept_name as title, "+
						               "decode(d.parent_dept_id, null, 'organs' ||d.organ_id,'depts' || d.parent_dept_id) as pid, "+
						               "'dept' as type, "+
						               "2 as typeorder, "+
						               "d.order_no, "+
						               "d.level_code, "+
						               "d.organ_id as organ_id "+
						          "from dept d "+
						         "where d.is_validation = 1 and d.organ_id = '"+organId+"' "+
						     /*   "UNION ALL "+
						        "select 'quarters' || q.quarter_id as id, "+
						               "q.quarter_name as title, "+
						               "decode(q.parent_quarter_id, "+
						                      "null, "+
						                      "'depts' || q.dept_id, "+
						                      "'quarters' || q.parent_quarter_id) as pid, "+
						               "'quarter' as type, "+
						               "3 as typeorder, "+
						               "q.order_no, "+
						               "q.level_code, "+
						               "dd.organ_id as organ_id "+
						          "from quarter q,dept dd "+
						         "where q.is_validation = 1 and q.dept_id = dd.dept_id  "+ */
						         "UNION ALL "+
							        "select e.employee_id as id, "+
								       "e.employee_name as title,  "+
								       "'depts' || e.dept_id as pid, "+
								       "'employee' as type, "+
								       "4 as typeorder, "+
								       "e.order_no, "+
								       "null as level_code, "+
								       "d.organ_id as organ_id "+
								  "from employee e, dept d "+
								 "where e.isvalidation = 1 "+
								   "and e.dept_id = d.dept_id and e.employee_id not in"
								   + " (select ttt.employee_id from talent_registration ttt where ttt.is_audited = 1 and ttt.is_del = 0 ) and d.organ_id = '"+organId+"' ";
						sql+= " ) t ";
						/*if(nameTerm!=null && !"".equals(nameTerm) && !"".equals(nameTerm)){
							sql+= " start with t.title like '%"+nameTerm+"%'  connect by prior t.pid = t.id ";
						}*/
						sql+=" order by t.typeorder, t.order_no, t.level_code ";
			}
			Session session = template.getSessionFactory().getCurrentSession();
			SQLQuery sqlQuery=session.createSQLQuery(sql);   
		    List<Object[]> tmplist = sqlQuery.addScalar("id", StringType.INSTANCE)
				.addScalar("title", StringType.INSTANCE)
				.addScalar("parentId", StringType.INSTANCE)
				.addScalar("type", StringType.INSTANCE)
				.list();
		    Map<String,List<TreeNode>> childMap = new HashMap<String,List<TreeNode>>();
		    for(int i=0;i<tmplist.size();i++){
		    	Object[] object = (Object[])tmplist.get(i);
		    	String id = (String)object[0];
		    	String title = (String)object[1];
		    	String pid = (String)object[2];
		    	String type = (String)object[3];
		    	
		    	if(parentNode == null && id.equals("organs"+organId)){
		    		parentNode = new TreeNode();
		    		parentNode.setId(id);
		    		parentNode.setTitle(title);
		    		parentNode.setParentId(null);
		    		parentNode.setType(type);
		    		parentNode.setChildren(new ArrayList<TreeNode>());
    			}/*
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
		    	}*/else{
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
		    if(parentNode!=null){
			    List<TreeNode> trees = new ArrayList<TreeNode>();
			    trees.add(parentNode);
			    setChildTreeMap(trees,childMap);		
				trees = TreeNode.toTree(trees,false,null);
				this.setLeafNullParent(trees);
				
				parentNode = trees.get(0);
		    }
			TreeNode.putTypeIncon("organ", "resources/icons/fam/organ.gif", "");
		    TreeNode.putTypeIncon("dept", "resources/icons/fam/dept.gif", "");
		    TreeNode.putTypeIncon("quarter", "resources/icons/fam/user_gray.png", "");
			TreeNode.putTypeIncon("employee", "resources/icons/fam/user_green.gif", "");
		}catch(Exception e){
			e.printStackTrace();
		}
		return parentNode;
	}
	private void setLeafNullParent(List<TreeNode> trees){
		if(trees!=null){
			Iterator its = trees.iterator();
			while(its.hasNext()){
				TreeNode node = (TreeNode)its.next();
				if(!"employee".equals(node.getType()) && (node.getChildren()==null||node.getChildren().size()==0)){
					//node.setLeaf(true);
					//node.setChildren(new ArrayList());
					its.remove();
				}else
					this.setLeafNullParent(node.getChildren());
			}
		}
	}
	
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
	
	private TreeNode setOrganNodeInList(List nodeList,Organ organ,Map baseInfoMap,Map<String,TreeNode> treeNodeValue){
		TreeNode organNode = new TreeNode();
		organNode.setId(organ.getOrganId());
		organNode.setTitle(organ.getOrganName());
		organNode.setParentId(null);
		organNode.setType("organ");
		organNode.setChildren(new ArrayList());
		nodeList.add(organNode);
    	
    	treeNodeValue.put(organ.getOrganId(),organNode);
    	return organNode;
	}
	private TreeNode setOrganNode(TreeNode parentNode,Organ organ,Map baseInfoMap,Map<String,TreeNode> treeNodeValue){
		TreeNode organNode = new TreeNode();
		organNode.setId(organ.getOrganId());
		organNode.setTitle(organ.getOrganName());
		organNode.setParentId(parentNode.getId());
		organNode.setType("organ");
		organNode.setChildren(new ArrayList());
		parentNode.getChildren().add(organNode);
    	
    	treeNodeValue.put(organ.getOrganId(),organNode);
    	return organNode;
	}
	
	private TreeNode setSpecialityTreeNode(List nodeList,Speciality speciality,Organ organ,Map baseInfoMap,Map<String,TreeNode> treeNodeValue){
		TreeNode node = new TreeNode();
	    node.setId(speciality.getSpecialityid());
	    node.setTitle(speciality.getSpecialityname());
	    node.setParentId(organ.getOrganId());
	    node.setType("speciality");
	    node.setChildren(new ArrayList());
	    treeNodeValue.put(node.getId(), node);
	    
	    TreeNode parentNode = treeNodeValue.get(node.getParentId());
	    parentNode.getChildren().add(node);
	    return node;
	}
	
	private TreeNode setDeptTreeNode(List nodeList,Dept dept,Map baseInfoMap,Map<String,TreeNode> treeNodeValue){
		TreeNode node = new TreeNode();
	    node.setId(dept.getDeptId());
	    node.setTitle(dept.getDeptName());
	    node.setParentId(dept.getDept() != null ? dept.getDept().getDeptId() : dept.getOrgan().getOrganId());
	    node.setType("dept");
	    node.setChildren(new ArrayList());
	    treeNodeValue.put(node.getId(), node);
	    
	    TreeNode parentNode = treeNodeValue.get(node.getParentId());
	    if(parentNode==null){
		    if(dept.getDept()!=null){
		    	Dept parentDept = (Dept)baseInfoMap.get(dept.getDept().getDeptId());
		    	if(parentDept == null)parentDept = dept.getDept();
		    	parentNode = setDeptTreeNode(nodeList,parentDept,baseInfoMap,treeNodeValue);	
		    }else if(dept.getOrgan()!=null){
		    	parentNode = this.setOrganNodeInList(nodeList, dept.getOrgan(), baseInfoMap, treeNodeValue);
		    }
	    }
	    parentNode.getChildren().add(node);
	    return node;
	}
	
	private TreeNode setQuarterTreeNode(List nodeList,Quarter quarter,Map baseInfoMap,Map<String,TreeNode> treeNodeValue){
		TreeNode node = new TreeNode();
	    node.setId(quarter.getQuarterId());
	    node.setTitle(quarter.getQuarterName());
	    node.setParentId(quarter.getQuarter() != null ? quarter.getQuarter().getQuarterId() : quarter.getDept().getDeptId());
	    node.setType("quarter");
	    node.setChildren(new ArrayList());
	    treeNodeValue.put(node.getId(), node);
	    
	    TreeNode parentNode = treeNodeValue.get(node.getParentId());
	    if(parentNode==null){
		    if(quarter.getQuarter()!=null){
		    	Quarter parentQuarter = (Quarter)baseInfoMap.get(quarter.getQuarter().getQuarterId());
		    	if(parentQuarter == null)parentQuarter = quarter.getQuarter();
		    	parentNode = setQuarterTreeNode(nodeList,parentQuarter,baseInfoMap,treeNodeValue);	
		    }else if(quarter.getDept()!=null){
		    	parentNode = setDeptTreeNode(nodeList,quarter.getDept(),baseInfoMap,treeNodeValue);	
		    }
	    }
	    parentNode.getChildren().add(node);
	    return node;
	}

}
