package cn.com.ite.hnjtamis.query.employeeQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;

import cn.com.ite.eap2.core.hibernate.HibernateDefaultDAOImpl;

/**
 *
 * <p>Title cn.com.ite.hnjtamis.query.employeeQuery.EmployeeThemeBankDaoImpl</p>
 * <p>Description </p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2017</p>
 * @create time: 2017年5月16日 下午1:35:11
 * @version 1.0
 * 
 * @modified records:
 */
public class EmployeeThemeBankDaoImpl extends HibernateDefaultDAOImpl implements EmployeeThemeBankDao{

	/************************************ 员工汇总 **********************************************/
	
	/**
	 * 根据机构查询员工完成题库的学习数
	 * @description
	 * @param organId
	 * @return
	 * @modified
	 */
	public List<EmployeeThemeBankForm> queryEmployeeXxQkInOrgan(String organId,int start,int limit,Map paramMap){
		List<EmployeeThemeBankForm> list = new ArrayList<EmployeeThemeBankForm>();
		try{
			String sql = "select * from (select * from (select aaa.dept_id, "+
						       "aaa.dept_name, "+
						       "aaa.employee_id, "+
						       "aaa.employee_name, "+
						       "aaa.employee_code, "+
						       "sum(decode(aaa.theme_num,null,0, 1)) themebanknum, "+
						       "sum(decode(aaa.theme_num,null,0, aaa.theme_fin_num, 1, 0)) xxthemebanknum,"+
						       "sum(decode(aaa.theme_num,null,0,decode(aaa.theme_fin_num,null,1, 0, 1, 0))) wxxthemebanknum, "+//未学习
						       "sum(decode(aaa.theme_num,null,0,decode(aaa.theme_fin_num,null,0, 0, 0, decode(floor(aaa.theme_fin_num/aaa.theme_num),0,1,0)))) yxxthemebanknum, "+//已学习
						       "sum(decode(aaa.theme_num,null,0,decode(aaa.theme_fin_num,null,0, 0, 0, decode(floor(aaa.theme_fin_num/aaa.theme_num),0,0,1)))) yxwthemebanknum,"+ 
						       "sum(aaa.theme_num) as theme_num," + //总数
						       "sum(aaa.theme_fin_num) as theme_fin_num "+//已学完
						  "from (select dd.dept_id, "+
						               "dd.dept_name, "+
						               "e.employee_id, "+
						               "e.employee_name, "+
						               "e.employee_code, "+
						               "tt.theme_bank_id, "+
						               "tt.theme_bank_name, "+
						               "tt.theme_num, "+
						               "tt.theme_fin_num "+
						          "from (select tt1.*, tb.theme_bank_name, tb.theme_bank_code "+
						                  "from (select t1.employee_id, "+
						                               "t1.theme_bank_id, "+
						                               "t1.theme_num, "+
						                               "decode(t2.theme_fin_num, "+
						                                      "null, "+
						                                      "0, "+
						                                      "t2.theme_fin_num) as theme_fin_num "+
						                          "from (select * "+
						                                  "from personal_bank pp "+
						                                 "where pp.theme_num > 0 "+
						                                   "and pp.theme_num is not null) t1 "+
						                          "left join personal_bank_fin t2 "+
						                            "on t1.employee_id = t2.employee_id "+
						                           "and t1.theme_bank_id = t2.theme_bank_id) tt1, "+
						                       "theme_bank tb "+
						                 "where tt1.theme_bank_id = tb.theme_bank_id "+
						                   "and tb.theme_bank_name is not null and (tb.bank_public = '10' or (tb.bank_public = '20' and tb.organ_id ='"+organId+"')) "+
						                   "and tb.is_L = '10') tt, "+
						               "(select * "+
						                  "from employee ee "+
						                 "where ee.isvalidation = '1' and ee.dept_id in "+
						                       "(select d.dept_id "+
						                          "from dept d "+
						                         "where d.is_validation = '1' "+
						                           "and d.organ_id = ?) ";
			String employeeTerm = (String)paramMap.get("employeeTerm");
			if(employeeTerm!=null && !"undefined".equals(employeeTerm) && !"".equals(employeeTerm)&& !"null".equals(employeeTerm)){
				 sql+=" and ee.employee_name like '%' || '"+employeeTerm+"' ||'%' "; 			
			}
			String deptTerm = (String)paramMap.get("deptTerm");
			if(deptTerm!=null && !"undefined".equals(deptTerm) && !"".equals(deptTerm)&& !"null".equals(deptTerm)){
				 sql+=" and ee.dept_id in(select d.dept_id "+
					                  "from dept d "+
					                 "where d.is_validation = '1' "+
					                 "start with d.dept_id = '"+deptTerm+"' "+
					                "connect by prior d.dept_id = d.parent_dept_id) "; 			
			} 
										sql+= " ) e, "+
						               "dept dd "+
						         "where e.employee_id = tt.employee_id(+) and e.isvalidation = '1' "+
						           "and e.dept_id = dd.dept_id "+
						         "order by e.employee_code) aaa "+
						 "group by aaa.dept_id, "+
						          "aaa.dept_name, "+
						          "aaa.employee_id, "+
						          "aaa.employee_name, "+
						          "aaa.employee_code) ";
				String learnTerm = (String)paramMap.get("learnTerm");
				if("unLearn".equals(learnTerm)){
					sql+=" where  employee_id not in( select tt.employee_id  "+
							 " from (select t1.employee_id, "+
							               "sum(t1.theme_num) as theme_num, "+
							               "sum(decode(t2.theme_fin_num, null, 0, t2.theme_fin_num)) as theme_fin_num "+
							          "from (select pp.* from personal_bank pp,theme_bank tb where   pp.theme_bank_id = tb.theme_bank_id "+
							          	" and (tb.bank_public = '10' or (tb.bank_public = '20' and tb.organ_id ='"+organId+"')) "+
						                 " and tb.is_L = '10') t1 "+
							          "left join personal_bank_fin t2 "+
							            "on t1.employee_id = t2.employee_id "+
							           "and t1.theme_bank_id = t2.theme_bank_id "+
							         "group by t1.employee_id) tt, "+
							       "employee e "+
							 "where tt.employee_id = e.employee_id and  e.isvalidation = '1' "+
							   "and theme_num >= theme_fin_num "+
							   "and theme_num > 0 "+
							   "and theme_fin_num > 0 "+
							   "and e.isvalidation = 1 )  ";
				}else if("exLearn".equals(learnTerm)){
					sql+=" where  employee_id in( select tt.employee_id  "+
						 " from (select t1.employee_id, "+
						               "sum(t1.theme_num) as theme_num, "+
						               "sum(decode(t2.theme_fin_num, null, 0, t2.theme_fin_num)) as theme_fin_num "+
						          "from (select pp.* from personal_bank pp,theme_bank tb where   pp.theme_bank_id = tb.theme_bank_id "+
							          	" and (tb.bank_public = '10' or (tb.bank_public = '20' and tb.organ_id ='"+organId+"')) "+
						                 " and tb.is_L = '10') t1 "+
						          "left join personal_bank_fin t2 "+
						            "on t1.employee_id = t2.employee_id "+
						           "and t1.theme_bank_id = t2.theme_bank_id "+
						         "group by t1.employee_id) tt, "+
						       "employee e "+
						 "where tt.employee_id = e.employee_id and  e.isvalidation = '1' "+
						   "and theme_num > theme_fin_num "+
						   "and theme_num > 0 "+
						   "and theme_fin_num > 0 "+
						   "and e.isvalidation = 1 )  "; 	 	 			
				}else if("finLearn".equals(learnTerm)){
					sql+=" where  employee_id in "+ 
							"  (select tttt.employee_id "+
		                  "from (select tt.employee_id,sum(theme_num) as theme_num,sum(theme_fin_num) as theme_fin_num "+
		                          "from (select t1.employee_id, "+
		                                       "sum(t1.theme_num) as theme_num, "+
		                                       "sum(decode(t2.theme_fin_num, "+
		                                                  "null, "+
		                                                  "0, "+
		                                                  "t2.theme_fin_num)) as theme_fin_num "+
		                                  "from (select pp.* from personal_bank pp,theme_bank tb where   pp.theme_bank_id = tb.theme_bank_id "+
							          	" and (tb.bank_public = '10' or (tb.bank_public = '20' and tb.organ_id ='"+organId+"')) "+
						                 " and tb.is_L = '10') t1 "+
		                                  "left join personal_bank_fin t2 "+
		                                    "on t1.employee_id = t2.employee_id "+
		                                   "and t1.theme_bank_id = t2.theme_bank_id "+
		                                 "group by t1.employee_id) tt, "+
		                               "employee e "+
		                         "where tt.employee_id = e.employee_id and  e.isvalidation = '1' "+
		                           "and e.isvalidation = 1 group by tt.employee_id) tttt "+
		                 "where tttt.theme_num = tttt.theme_fin_num "+
		                   "and tttt.theme_num > 0 "+
		                   "and tttt.theme_fin_num > 0) ";
				}
				sql+=") where rownum<=? ";
			Session session = template.getSessionFactory().getCurrentSession();
			SQLQuery sqlQuery=session.createSQLQuery(sql); 
			sqlQuery.setString(0, organId);
			//sqlQuery.setInteger(1, start);
			sqlQuery.setInteger(1, start+limit);
			List tmplist = sqlQuery
					.addScalar("dept_id", StringType.INSTANCE)
					.addScalar("dept_name", StringType.INSTANCE)
					.addScalar("employee_id", StringType.INSTANCE)
					.addScalar("employee_name", StringType.INSTANCE)
					.addScalar("employee_code", StringType.INSTANCE)
					.addScalar("themebanknum", IntegerType.INSTANCE)
					.addScalar("xxthemebanknum", IntegerType.INSTANCE)
					.addScalar("wxxthemebanknum", IntegerType.INSTANCE)
					.addScalar("yxxthemebanknum", IntegerType.INSTANCE)
					.addScalar("yxwthemebanknum", IntegerType.INSTANCE)
					.addScalar("theme_num", IntegerType.INSTANCE)
					.addScalar("theme_fin_num", IntegerType.INSTANCE).list();
			for(int i=start;i<tmplist.size();i++){
				Object[] v = (Object[])tmplist.get(i);
				String deptId = (String)v[0];
				String deptName = (String)v[1];
				String employeeId = (String)v[2];
				String employeeName = (String)v[3];
				String employeeCode = (String)v[4];
				Integer themebanknum = (Integer)v[5];
				Integer xxthemebanknum = (Integer)v[6];
				Integer wxxthemebanknum = (Integer)v[7];
				Integer yxxthemebanknum = (Integer)v[8];
				Integer yxwthemebanknum = (Integer)v[9];
				Integer themeNum = (Integer)v[10];
				Integer themeFinNum = (Integer)v[11];
				
				EmployeeThemeBankForm employeeThemeBankForm = new EmployeeThemeBankForm();
				employeeThemeBankForm.setDeptId(deptId);
				employeeThemeBankForm.setDeptName(deptName);
				employeeThemeBankForm.setEmployeeId(employeeId);
				employeeThemeBankForm.setEmployeeName(employeeName);
				employeeThemeBankForm.setEmployeeCode(employeeCode);
				employeeThemeBankForm.setThemebanknum(themebanknum);
				employeeThemeBankForm.setXxthemebanknum(xxthemebanknum);
				employeeThemeBankForm.setWxxthemebanknum(wxxthemebanknum);
				employeeThemeBankForm.setYxxthemebanknum(yxxthemebanknum);
				employeeThemeBankForm.setYxwthemebanknum(yxwthemebanknum);
				employeeThemeBankForm.setThemeNum(themeNum==null?0:themeNum);
				employeeThemeBankForm.setThemeFinNum(themeFinNum==null?0:themeFinNum);
				list.add(employeeThemeBankForm);
				 
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}

	
	/**
	 * 根据机构查询员工完成题库的学习数
	 * @description
	 * @param organId
	 * @return
	 * @modified
	 */
	public int countEmployeeXxQkInOrgan(String organId,Map paramMap){
		int count = 0;
		try{
			String sql = "select count(*) as count from (select * from (select aaa.dept_id, "+
						       "aaa.dept_name, "+
						       "aaa.employee_id, "+
						       "aaa.employee_name, "+
						       "aaa.employee_code, "+
						       "sum(decode(aaa.theme_num,null,0, 1)) themebanknum, "+
						       "sum(decode(aaa.theme_num,null,0, aaa.theme_fin_num, 1, 0)) xxthemebanknum "+
						  "from (select dd.dept_id, "+
						               "dd.dept_name, "+
						               "e.employee_id, "+
						               "e.employee_name, "+
						               "e.employee_code, "+
						               "tt.theme_bank_id, "+
						               "tt.theme_bank_name, "+
						               "tt.theme_num, "+
						               "tt.theme_fin_num "+
						          "from (select tt1.*, tb.theme_bank_name, tb.theme_bank_code "+
						                  "from (select t1.employee_id, "+
						                               "t1.theme_bank_id, "+
						                               "t1.theme_num, "+
						                               "decode(t2.theme_fin_num, "+
						                                      "null, "+
						                                      "0, "+
						                                      "t2.theme_fin_num) as theme_fin_num "+
						                          "from (select * "+
						                                  "from personal_bank pp "+
						                                 "where pp.theme_num > 0 "+
						                                   "and pp.theme_num is not null) t1 "+
						                          "left join personal_bank_fin t2 "+
						                            "on t1.employee_id = t2.employee_id "+
						                           "and t1.theme_bank_id = t2.theme_bank_id) tt1, "+
						                       "theme_bank tb "+
						                 "where tt1.theme_bank_id = tb.theme_bank_id "+
						                   "and tb.theme_bank_name is not null and (tb.bank_public = '10' or (tb.bank_public = '20' and tb.organ_id ='"+organId+"')) "+
						                   "and tb.is_L = '10') tt, "+
						               "(select * "+
						                  "from employee ee "+
						                 "where ee.isvalidation = '1' and ee.dept_id in "+
						                       "(select d.dept_id "+
						                          "from dept d "+
						                         "where d.is_validation = '1' "+
						                           "and d.organ_id = ?) ";
			String employeeTerm = (String)paramMap.get("employeeTerm");
			if(employeeTerm!=null && !"undefined".equals(employeeTerm) && !"".equals(employeeTerm)&& !"null".equals(employeeTerm)){
				 sql+=" and ee.employee_name like '%' || '"+employeeTerm+"' ||'%' "; 			
			}   
			String deptTerm = (String)paramMap.get("deptTerm");
			if(deptTerm!=null && !"undefined".equals(deptTerm) && !"".equals(deptTerm)&& !"null".equals(deptTerm)){
				 sql+=" and ee.dept_id in(select d.dept_id "+
					                  "from dept d "+
					                 "where d.is_validation = '1' "+
					                 "start with d.dept_id = '"+deptTerm+"' "+
					                "connect by prior d.dept_id = d.parent_dept_id) "; 			
			} 
										sql+= " ) e, "+
						               "dept dd "+
						         "where  e.isvalidation = '1' and e.employee_id = tt.employee_id(+) "+
						           "and e.dept_id = dd.dept_id "+
						         "order by e.employee_code) aaa "+
						 "group by aaa.dept_id, "+
						          "aaa.dept_name, "+
						          "aaa.employee_id, "+
						          "aaa.employee_name, "+
						          "aaa.employee_code) ";
				String learnTerm = (String)paramMap.get("learnTerm");
				if("unLearn".equals(learnTerm)){
					sql+=" where  employee_id not in( select tt.employee_id  "+
							 " from (select t1.employee_id, "+
							               "sum(t1.theme_num) as theme_num, "+
							               "sum(decode(t2.theme_fin_num, null, 0, t2.theme_fin_num)) as theme_fin_num "+
							          "from (select pp.* from personal_bank pp,theme_bank tb where   pp.theme_bank_id = tb.theme_bank_id "+
							          	" and (tb.bank_public = '10' or (tb.bank_public = '20' and tb.organ_id ='"+organId+"')) "+
						                 " and tb.is_L = '10') t1 "+
							          "left join personal_bank_fin t2 "+
							            "on t1.employee_id = t2.employee_id "+
							           "and t1.theme_bank_id = t2.theme_bank_id "+
							         "group by t1.employee_id) tt, "+
							       "employee e "+
							 "where tt.employee_id = e.employee_id and e.isvalidation = '1' "+
							   "and theme_num >= theme_fin_num "+
							   "and theme_num > 0 "+
							   "and theme_fin_num > 0 "+
							   "and e.isvalidation = 1 )  ";
				}else if("exLearn".equals(learnTerm)){
					sql+=" where  employee_id in( select tt.employee_id  "+
						 " from (select t1.employee_id, "+
						               "sum(t1.theme_num) as theme_num, "+
						               "sum(decode(t2.theme_fin_num, null, 0, t2.theme_fin_num)) as theme_fin_num "+
						          "from (select pp.* from personal_bank pp,theme_bank tb where   pp.theme_bank_id = tb.theme_bank_id "+
							          	" and (tb.bank_public = '10' or (tb.bank_public = '20' and tb.organ_id ='"+organId+"')) "+
						                 " and tb.is_L = '10') t1 "+
						          "left join personal_bank_fin t2 "+
						            "on t1.employee_id = t2.employee_id "+
						           "and t1.theme_bank_id = t2.theme_bank_id "+
						         "group by t1.employee_id) tt, "+
						       "employee e "+
						 "where tt.employee_id = e.employee_id and e.isvalidation = '1' "+
						   "and theme_num > theme_fin_num "+
						   "and theme_num > 0 "+
						   "and theme_fin_num > 0 "+
						   "and e.isvalidation = 1 )  "; 	 	 			
				}else if("finLearn".equals(learnTerm)){
					sql+=" where  employee_id in "+ 
							"  (select tttt.employee_id "+
		                  "from (select tt.employee_id,sum(theme_num) as theme_num,sum(theme_fin_num) as theme_fin_num "+
		                          "from (select t1.employee_id, "+
		                                       "sum(t1.theme_num) as theme_num, "+
		                                       "sum(decode(t2.theme_fin_num, "+
		                                                  "null, "+
		                                                  "0, "+
		                                                  "t2.theme_fin_num)) as theme_fin_num "+
		                                  "from (select pp.* from personal_bank pp,theme_bank tb where   pp.theme_bank_id = tb.theme_bank_id "+
							          	" and (tb.bank_public = '10' or (tb.bank_public = '20' and tb.organ_id ='"+organId+"')) "+
						                 " and tb.is_L = '10') t1 "+
		                                  "left join personal_bank_fin t2 "+
		                                    "on t1.employee_id = t2.employee_id "+
		                                   "and t1.theme_bank_id = t2.theme_bank_id "+
		                                 "group by t1.employee_id) tt, "+
		                               "employee e "+
		                         "where tt.employee_id = e.employee_id and e.isvalidation = '1' "+
		                           "and e.isvalidation = 1 group by tt.employee_id) tttt "+
		                 "where tttt.theme_num = tttt.theme_fin_num "+
		                   "and tttt.theme_num > 0 "+
		                   "and tttt.theme_fin_num > 0) ";
				}
				sql+=") ";
			Session session = template.getSessionFactory().getCurrentSession();
			SQLQuery sqlQuery=session.createSQLQuery(sql); 
			sqlQuery.setString(0, organId);
			List<Integer> tmplist = sqlQuery.addScalar("count", IntegerType.INSTANCE).list();
			if(tmplist!=null && tmplist.size()>0){
				count = tmplist.get(0)!=null ? tmplist.get(0).intValue() : 0;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return count;
	}
	
	
	/**
	 * 根据部门查询员工完成题库的学习数
	 * @description
	 * @param qdeptId
	 * @return
	 * @modified
	 */
	public List<EmployeeThemeBankForm> queryEmployeeXxQkInDept(String qdeptId,int start,int limit,Map paramMap){
		String organId = (String)paramMap.get("organId");
		List<EmployeeThemeBankForm> list = new ArrayList<EmployeeThemeBankForm>();
		try{
			String sql = "select * from (select * from (select aaa.dept_id, "+
						       "aaa.dept_name, "+
						       "aaa.employee_id, "+
						       "aaa.employee_name, "+
						       "aaa.employee_code, "+
						       "sum(decode(aaa.theme_num,null,0, 1)) themebanknum, "+
						       "sum(decode(aaa.theme_num,null,0, aaa.theme_fin_num, 1, 0)) xxthemebanknum,"+
						       "sum(decode(aaa.theme_num,null,0,decode(aaa.theme_fin_num,null,1, 0, 1, 0))) wxxthemebanknum, "+//未学习
						       "sum(decode(aaa.theme_num,null,0,decode(aaa.theme_fin_num,null,0, 0, 0, decode(floor(aaa.theme_fin_num/aaa.theme_num),0,1,0)))) yxxthemebanknum, "+//已学习
						       "sum(decode(aaa.theme_num,null,0,decode(aaa.theme_fin_num,null,0, 0, 0, decode(floor(aaa.theme_fin_num/aaa.theme_num),0,0,1)))) yxwthemebanknum,  "+//已学完
						       "sum(aaa.theme_num) as theme_num," + //总数
						       "sum(aaa.theme_fin_num) as theme_fin_num "+//已学完
						  "from (select dd.dept_id, "+
						               "dd.dept_name, "+
						               "e.employee_id, "+
						               "e.employee_name, "+
						               "e.employee_code, "+
						               "tt.theme_bank_id, "+
						               "tt.theme_bank_name, "+
						               "tt.theme_num, "+
						               "tt.theme_fin_num "+
						          "from (select tt1.*, tb.theme_bank_name, tb.theme_bank_code "+
						                  "from (select t1.employee_id, "+
						                               "t1.theme_bank_id, "+
						                               "t1.theme_num, "+
						                               "decode(t2.theme_fin_num, "+
						                                      "null, "+
						                                      "0, "+
						                                      "t2.theme_fin_num) as theme_fin_num "+
						                          "from (select * "+
						                                  "from personal_bank pp "+
						                                 "where pp.theme_num > 0 "+
						                                   "and pp.theme_num is not null) t1 "+
						                          "left join personal_bank_fin t2 "+
						                            "on t1.employee_id = t2.employee_id "+
						                           "and t1.theme_bank_id = t2.theme_bank_id) tt1, "+
						                       "theme_bank tb "+
						                 "where tt1.theme_bank_id = tb.theme_bank_id "+
						                   "and tb.theme_bank_name is not null and (tb.bank_public = '10' or (tb.bank_public = '20' and tb.organ_id ='"+organId+"')) "+
						                   "and tb.is_L = '10') tt, "+
						               " (select * "+
						                  "from employee ee "+
						                 "where  ee.isvalidation = '1' and ee.dept_id in(select d.dept_id "+
					                  "from dept d "+
					                 "where d.is_validation = '1' "+
					                 "start with d.dept_id = ? "+
					                "connect by prior d.dept_id = d.parent_dept_id)";
			String employeeTerm = (String)paramMap.get("employeeTerm");
			if(employeeTerm!=null && !"undefined".equals(employeeTerm) && !"".equals(employeeTerm)&& !"null".equals(employeeTerm)){
				 sql+=" and ee.employee_name like '%' || '"+employeeTerm+"' ||'%' "; 			
			}
									sql+=" ) e, "+
						               "dept dd "+
						         "where e.employee_id = tt.employee_id(+) "+
						           "and e.dept_id = dd.dept_id and e.isvalidation = '1'  "+
						         "order by e.employee_code) aaa "+
						 "group by aaa.dept_id, "+
						          "aaa.dept_name, "+
						          "aaa.employee_id, "+
						          "aaa.employee_name, "+
						          "aaa.employee_code) ";
				String learnTerm = (String)paramMap.get("learnTerm");
				if("unLearn".equals(learnTerm)){
					sql+=" where  employee_id not in( select tt.employee_id  "+
							 " from (select t1.employee_id, "+
							               "sum(t1.theme_num) as theme_num, "+
							               "sum(decode(t2.theme_fin_num, null, 0, t2.theme_fin_num)) as theme_fin_num "+
							          "from (select pp.* from personal_bank pp,theme_bank tb where   pp.theme_bank_id = tb.theme_bank_id "+
							          	" and (tb.bank_public = '10' or (tb.bank_public = '20' and tb.organ_id ='"+organId+"')) "+
						                 " and tb.is_L = '10') t1 "+
							          "left join personal_bank_fin t2 "+
							            "on t1.employee_id = t2.employee_id "+
							           "and t1.theme_bank_id = t2.theme_bank_id "+
							         "group by t1.employee_id) tt, "+
							       "employee e "+
							 "where tt.employee_id = e.employee_id  and e.isvalidation = '1' "+
							   "and theme_num >= theme_fin_num "+
							   "and theme_num > 0 "+
							   "and theme_fin_num > 0 "+
							   "and e.isvalidation = 1 )  ";
				}else if("exLearn".equals(learnTerm)){
					sql+=" where  employee_id in( select tt.employee_id  "+
						 " from (select t1.employee_id, "+
						               "sum(t1.theme_num) as theme_num, "+
						               "sum(decode(t2.theme_fin_num, null, 0, t2.theme_fin_num)) as theme_fin_num "+
						          "from (select pp.* from personal_bank pp,theme_bank tb where   pp.theme_bank_id = tb.theme_bank_id "+
							          	" and (tb.bank_public = '10' or (tb.bank_public = '20' and tb.organ_id ='"+organId+"')) "+
						                 " and tb.is_L = '10') t1 "+
						          "left join personal_bank_fin t2 "+
						            "on t1.employee_id = t2.employee_id "+
						           "and t1.theme_bank_id = t2.theme_bank_id "+
						         "group by t1.employee_id) tt, "+
						       "employee e "+
						 "where tt.employee_id = e.employee_id  and e.isvalidation = '1' "+
						   "and theme_num > theme_fin_num "+
						   "and theme_num > 0 "+
						   "and theme_fin_num > 0 "+
						   "and e.isvalidation = 1 )  "; 	 	 			
				}else if("finLearn".equals(learnTerm)){
					sql+=" where  employee_id in "+ 
							"  (select tttt.employee_id "+
		                  "from (select tt.employee_id,sum(theme_num) as theme_num,sum(theme_fin_num) as theme_fin_num "+
		                          "from (select t1.employee_id, "+
		                                       "sum(t1.theme_num) as theme_num, "+
		                                       "sum(decode(t2.theme_fin_num, "+
		                                                  "null, "+
		                                                  "0, "+
		                                                  "t2.theme_fin_num)) as theme_fin_num "+
		                                  "from (select pp.* from personal_bank pp,theme_bank tb where   pp.theme_bank_id = tb.theme_bank_id "+
							          	" and (tb.bank_public = '10' or (tb.bank_public = '20' and tb.organ_id ='"+organId+"')) "+
						                 " and tb.is_L = '10') t1 "+
		                                  "left join personal_bank_fin t2 "+
		                                    "on t1.employee_id = t2.employee_id "+
		                                   "and t1.theme_bank_id = t2.theme_bank_id "+
		                                 "group by t1.employee_id) tt, "+
		                               "employee e "+
		                         "where tt.employee_id = e.employee_id  and e.isvalidation = '1' "+
		                           "and e.isvalidation = 1 group by tt.employee_id) tttt "+
		                 "where tttt.theme_num = tttt.theme_fin_num "+
		                   "and tttt.theme_num > 0 "+
		                   "and tttt.theme_fin_num > 0) ";
				}
				sql+=") where rownum<=? ";
			Session session = template.getSessionFactory().getCurrentSession();
			SQLQuery sqlQuery=session.createSQLQuery(sql); 
			sqlQuery.setString(0, qdeptId);
			//sqlQuery.setInteger(1, start);
			sqlQuery.setInteger(1, start+limit);
			List tmplist = sqlQuery
					.addScalar("dept_id", StringType.INSTANCE)
					.addScalar("dept_name", StringType.INSTANCE)
					.addScalar("employee_id", StringType.INSTANCE)
					.addScalar("employee_name", StringType.INSTANCE)
					.addScalar("employee_code", StringType.INSTANCE)
					.addScalar("themebanknum", IntegerType.INSTANCE)
					.addScalar("xxthemebanknum", IntegerType.INSTANCE)
					.addScalar("wxxthemebanknum", IntegerType.INSTANCE)
					.addScalar("yxxthemebanknum", IntegerType.INSTANCE)
					.addScalar("yxwthemebanknum", IntegerType.INSTANCE)
					.addScalar("theme_num", IntegerType.INSTANCE)
					.addScalar("theme_fin_num", IntegerType.INSTANCE).list();
			for(int i=start;i<tmplist.size();i++){
				Object[] v = (Object[])tmplist.get(i);
				String deptId = (String)v[0];
				String deptName = (String)v[1];
				String employeeId = (String)v[2];
				String employeeName = (String)v[3];
				String employeeCode = (String)v[4];
				Integer themebanknum = (Integer)v[5];
				Integer xxthemebanknum = (Integer)v[6];
				Integer wxxthemebanknum = (Integer)v[7];
				Integer yxxthemebanknum = (Integer)v[8];
				Integer yxwthemebanknum = (Integer)v[9];
				Integer themeNum = (Integer)v[10];
				Integer themeFinNum = (Integer)v[11];
				
				EmployeeThemeBankForm employeeThemeBankForm = new EmployeeThemeBankForm();
				employeeThemeBankForm.setDeptId(deptId);
				employeeThemeBankForm.setDeptName(deptName);
				employeeThemeBankForm.setEmployeeId(employeeId);
				employeeThemeBankForm.setEmployeeName(employeeName);
				employeeThemeBankForm.setEmployeeCode(employeeCode);
				employeeThemeBankForm.setThemebanknum(themebanknum);
				employeeThemeBankForm.setXxthemebanknum(xxthemebanknum);
				employeeThemeBankForm.setWxxthemebanknum(wxxthemebanknum);
				employeeThemeBankForm.setYxxthemebanknum(yxxthemebanknum);
				employeeThemeBankForm.setYxwthemebanknum(yxwthemebanknum);
				employeeThemeBankForm.setThemeNum(themeNum==null?0:themeNum);
				employeeThemeBankForm.setThemeFinNum(themeFinNum==null?0:themeFinNum);
				list.add(employeeThemeBankForm);
				 
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}

	
	
	
	/**
	 * 根据部门查询员工完成题库的学习数
	 * @description
	 * @param qdeptId
	 * @return
	 * @modified
	 */
	public int countEmployeeXxQkInDept(String qdeptId,Map paramMap){
		int count = 0;
		try{
			String organId = (String)paramMap.get("organId");
			String sql = "select count(*) as count from (select * from (select aaa.dept_id, "+
						       "aaa.dept_name, "+
						       "aaa.employee_id, "+
						       "aaa.employee_name, "+
						       "aaa.employee_code, "+
						       "sum(decode(aaa.theme_num,null,0, 1)) themebanknum, "+
						       "sum(decode(aaa.theme_num,null,0, aaa.theme_fin_num, 1, 0)) xxthemebanknum "+
						  "from (select dd.dept_id, "+
						               "dd.dept_name, "+
						               "e.employee_id, "+
						               "e.employee_name, "+
						               "e.employee_code, "+
						               "tt.theme_bank_id, "+
						               "tt.theme_bank_name, "+
						               "tt.theme_num, "+
						               "tt.theme_fin_num "+
						          "from (select tt1.*, tb.theme_bank_name, tb.theme_bank_code "+
						                  "from (select t1.employee_id, "+
						                               "t1.theme_bank_id, "+
						                               "t1.theme_num, "+
						                               "decode(t2.theme_fin_num, "+
						                                      "null, "+
						                                      "0, "+
						                                      "t2.theme_fin_num) as theme_fin_num "+
						                          "from (select * "+
						                                  "from personal_bank pp "+
						                                 "where pp.theme_num > 0 "+
						                                   "and pp.theme_num is not null) t1 "+
						                          "left join personal_bank_fin t2 "+
						                            "on t1.employee_id = t2.employee_id "+
						                           "and t1.theme_bank_id = t2.theme_bank_id) tt1, "+
						                       "theme_bank tb "+
						                 "where tt1.theme_bank_id = tb.theme_bank_id "+
						                   "and tb.theme_bank_name is not null and (tb.bank_public = '10' or (tb.bank_public = '20' and tb.organ_id ='"+organId+"')) "+
						                   "and tb.is_L = '10') tt, "+
						               " (select * "+
						                  "from employee ee "+
						                 "where  ee.isvalidation = '1' and ee.dept_id in(select d.dept_id "+
					                  "from dept d "+
					                 "where d.is_validation = '1' "+
					                 "start with d.dept_id = ? "+
					                "connect by prior d.dept_id = d.parent_dept_id)";
			String employeeTerm = (String)paramMap.get("employeeTerm");
			if(employeeTerm!=null && !"undefined".equals(employeeTerm) && !"".equals(employeeTerm)&& !"null".equals(employeeTerm)){
				 sql+=" and ee.employee_name like '%' || '"+employeeTerm+"' ||'%' "; 			
			}
									sql+=" ) e, "+
						               "dept dd "+
						         "where e.employee_id = tt.employee_id(+) "+
						           "and e.dept_id = dd.dept_id "+
						         "order by e.employee_code) aaa "+
						 "group by aaa.dept_id, "+
						          "aaa.dept_name, "+
						          "aaa.employee_id, "+
						          "aaa.employee_name, "+
						          "aaa.employee_code) ";
				String learnTerm = (String)paramMap.get("learnTerm");
				if("unLearn".equals(learnTerm)){
					sql+=" where  employee_id not in( select tt.employee_id  "+
							 " from (select t1.employee_id, "+
							               "sum(t1.theme_num) as theme_num, "+
							               "sum(decode(t2.theme_fin_num, null, 0, t2.theme_fin_num)) as theme_fin_num "+
							          "from (select pp.* from personal_bank pp,theme_bank tb where   pp.theme_bank_id = tb.theme_bank_id "+
							          	" and (tb.bank_public = '10' or (tb.bank_public = '20' and tb.organ_id ='"+organId+"')) "+
						                 " and tb.is_L = '10') t1 "+
							          "left join personal_bank_fin t2 "+
							            "on t1.employee_id = t2.employee_id "+
							           "and t1.theme_bank_id = t2.theme_bank_id "+
							         "group by t1.employee_id) tt, "+
							       "employee e "+
							 "where tt.employee_id = e.employee_id  and e.isvalidation = '1' "+
							   "and theme_num >= theme_fin_num "+
							   "and theme_num > 0 "+
							   "and theme_fin_num > 0 "+
							   "and e.isvalidation = 1 )  ";
				}else if("exLearn".equals(learnTerm)){
					sql+=" where  employee_id in( select tt.employee_id  "+
						 " from (select t1.employee_id, "+
						               "sum(t1.theme_num) as theme_num, "+
						               "sum(decode(t2.theme_fin_num, null, 0, t2.theme_fin_num)) as theme_fin_num "+
						          "from (select pp.* from personal_bank pp,theme_bank tb where   pp.theme_bank_id = tb.theme_bank_id "+
							          	" and (tb.bank_public = '10' or (tb.bank_public = '20' and tb.organ_id ='"+organId+"')) "+
						                 " and tb.is_L = '10') t1 "+
						          "left join personal_bank_fin t2 "+
						            "on t1.employee_id = t2.employee_id "+
						           "and t1.theme_bank_id = t2.theme_bank_id "+
						         "group by t1.employee_id) tt, "+
						       "employee e "+
						 "where tt.employee_id = e.employee_id  and e.isvalidation = '1' "+
						   "and theme_num > theme_fin_num "+
						   "and theme_num > 0 "+
						   "and theme_fin_num > 0 "+
						   "and e.isvalidation = 1 )  "; 	 	 			
				}else if("finLearn".equals(learnTerm)){
					sql+=" where  employee_id in "+ 
							"  (select tttt.employee_id "+
		                  "from (select tt.employee_id,sum(theme_num) as theme_num,sum(theme_fin_num) as theme_fin_num "+
		                          "from (select t1.employee_id, "+
		                                       "sum(t1.theme_num) as theme_num, "+
		                                       "sum(decode(t2.theme_fin_num, "+
		                                                  "null, "+
		                                                  "0, "+
		                                                  "t2.theme_fin_num)) as theme_fin_num "+
		                                  "from (select pp.* from personal_bank pp,theme_bank tb where   pp.theme_bank_id = tb.theme_bank_id "+
							          	" and (tb.bank_public = '10' or (tb.bank_public = '20' and tb.organ_id ='"+organId+"')) "+
						                 " and tb.is_L = '10') t1 "+
		                                  "left join personal_bank_fin t2 "+
		                                    "on t1.employee_id = t2.employee_id "+
		                                   "and t1.theme_bank_id = t2.theme_bank_id "+
		                                 "group by t1.employee_id) tt, "+
		                               "employee e "+
		                         "where tt.employee_id = e.employee_id  and e.isvalidation = '1' "+
		                           "and e.isvalidation = 1 group by tt.employee_id) tttt "+
		                 "where tttt.theme_num = tttt.theme_fin_num "+
		                   "and tttt.theme_num > 0 "+
		                   "and tttt.theme_fin_num > 0) ";
				}
				sql+=") ";
			Session session = template.getSessionFactory().getCurrentSession();
			SQLQuery sqlQuery=session.createSQLQuery(sql); 
			sqlQuery.setString(0, qdeptId);
			List<Integer> tmplist = sqlQuery.addScalar("count", IntegerType.INSTANCE).list();
			if(tmplist!=null && tmplist.size()>0){
				count = tmplist.get(0)!=null ? tmplist.get(0).intValue() : 0;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return count;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/*****************************************员工明细*****************************************/
	/**
	 * 根据机构查询员工题库的学习情况
	 * @description
	 * @param organId
	 * @return
	 * @modified
	 */
	public List<EmployeeThemeBankForm> queryEmployeeThemeBankInOrgan(String organId,int start,int limit,Map paramMap){
		List<EmployeeThemeBankForm> list = new ArrayList<EmployeeThemeBankForm>();
		try{
			String sql = "select * from (select dd.dept_id, "+
					       "dd.dept_name, "+
					       "e.employee_id, "+
					       "e.employee_name, "+
					       "e.employee_code, "+
					       "tt.theme_bank_id, "+
					       "tt.theme_bank_name, "+
					       "tt.theme_num, "+
					       "tt.theme_fin_num "+
					  "from (select tt1.*, tb.theme_bank_name, tb.theme_bank_code "+
					          "from (select t1.employee_id, "+
					                       "t1.theme_bank_id, "+
					                       "t1.theme_num, "+
					                       "decode(t2.theme_fin_num, null, 0, t2.theme_fin_num) as theme_fin_num "+
					                  "from (select * "+
					                          "from personal_bank pp "+
					                         "where pp.theme_num > 0 "+
					                           "and pp.theme_num is not null) t1 "+
					                  "left join personal_bank_fin t2 "+
					                    "on t1.employee_id = t2.employee_id "+
					                   "and t1.theme_bank_id = t2.theme_bank_id) tt1, "+
					               "theme_bank tb "+
					         "where tt1.theme_bank_id = tb.theme_bank_id and tb.theme_bank_name is not null and (tb.bank_public = '10' or (tb.bank_public = '20' and tb.organ_id ='"+organId+"')) and tb.is_L = '10') tt, "+
					       "(select * "+
					          "from employee ee "+
					         "where  ee.isvalidation = '1' and ee.dept_id in "+
					               "(select d.dept_id "+
					                  "from dept d "+
					                 "where d.is_validation = '1'  and d.organ_id = ?)";
		String employeeTerm = (String)paramMap.get("employeeTerm");
		if(employeeTerm!=null && !"undefined".equals(employeeTerm) && !"".equals(employeeTerm)&& !"null".equals(employeeTerm)){
			 sql+=" and ee.employee_name like '%' || '"+employeeTerm+"' ||'%' "; 			
		}        
		String deptTerm = (String)paramMap.get("deptTerm");
		if(deptTerm!=null && !"undefined".equals(deptTerm) && !"".equals(deptTerm)&& !"null".equals(deptTerm)){
			 sql+=" and ee.dept_id in(select d.dept_id "+
				                  "from dept d "+
				                 "where d.is_validation = '1' "+
				                 "start with d.dept_id = '"+deptTerm+"' "+
				                "connect by prior d.dept_id = d.parent_dept_id) "; 			
		} 			                 
					                 sql+= " ) e, "+
					       "dept dd "+
					 "where e.employee_id = tt.employee_id(+)  and e.isvalidation = '1' "+
					   "and e.dept_id = dd.dept_id ";
		String learnTerm = (String)paramMap.get("learnTerm");
		if("unLearn".equals(learnTerm)){
			sql+=" and  e.employee_id not in( select tt.employee_id  "+
					 " from (select t1.employee_id, "+
					               "sum(t1.theme_num) as theme_num, "+
					               "sum(decode(t2.theme_fin_num, null, 0, t2.theme_fin_num)) as theme_fin_num "+
					          "from (select pp.* from personal_bank pp,theme_bank tb where   pp.theme_bank_id = tb.theme_bank_id "+
							          	" and (tb.bank_public = '10' or (tb.bank_public = '20' and tb.organ_id ='"+organId+"')) "+
						                 " and tb.is_L = '10') t1 "+
					          "left join personal_bank_fin t2 "+
					            "on t1.employee_id = t2.employee_id "+
					           "and t1.theme_bank_id = t2.theme_bank_id "+
					         "group by t1.employee_id) tt, "+
					       "employee e "+
					 "where tt.employee_id = e.employee_id  and e.isvalidation = '1' "+
					   "and theme_num >= theme_fin_num "+
					   "and theme_num > 0 "+
					   "and theme_fin_num > 0 "+
					   "and e.isvalidation = 1 )  ";
		}else if("exLearn".equals(learnTerm)){
			sql+=" and  e.employee_id in( select tt.employee_id  "+
				 " from (select t1.employee_id, "+
				               "sum(t1.theme_num) as theme_num, "+
				               "sum(decode(t2.theme_fin_num, null, 0, t2.theme_fin_num)) as theme_fin_num "+
				          "from (select pp.* from personal_bank pp,theme_bank tb where   pp.theme_bank_id = tb.theme_bank_id "+
							          	" and (tb.bank_public = '10' or (tb.bank_public = '20' and tb.organ_id ='"+organId+"')) "+
						                 " and tb.is_L = '10') t1 "+
				          "left join personal_bank_fin t2 "+
				            "on t1.employee_id = t2.employee_id "+
				           "and t1.theme_bank_id = t2.theme_bank_id "+
				         "group by t1.employee_id) tt, "+
				       "employee e "+
				 "where tt.employee_id = e.employee_id  and e.isvalidation = '1' "+
				   "and theme_num > theme_fin_num "+
				   "and theme_num > 0 "+
				   "and theme_fin_num > 0 "+
				   "and e.isvalidation = 1 )  "; 	 	 			
		}else if("finLearn".equals(learnTerm)){
			sql+=" and  e.employee_id in "+ 
					"  (select tttt.employee_id "+
                  "from (select tt.employee_id,sum(theme_num) as theme_num,sum(theme_fin_num) as theme_fin_num "+
                          "from (select t1.employee_id, "+
                                       "sum(t1.theme_num) as theme_num, "+
                                       "sum(decode(t2.theme_fin_num, "+
                                                  "null, "+
                                                  "0, "+
                                                  "t2.theme_fin_num)) as theme_fin_num "+
                                  "from (select pp.* from personal_bank pp,theme_bank tb where   pp.theme_bank_id = tb.theme_bank_id "+
							          	" and (tb.bank_public = '10' or (tb.bank_public = '20' and tb.organ_id ='"+organId+"')) "+
						                 " and tb.is_L = '10') t1 "+
                                  "left join personal_bank_fin t2 "+
                                    "on t1.employee_id = t2.employee_id "+
                                   "and t1.theme_bank_id = t2.theme_bank_id "+
                                 "group by t1.employee_id) tt, "+
                               "employee e "+
                         "where tt.employee_id = e.employee_id  and e.isvalidation = '1' "+
                           "and e.isvalidation = 1 group by tt.employee_id) tttt "+
                 "where tttt.theme_num = tttt.theme_fin_num "+
                   "and tttt.theme_num > 0 "+
                   "and tttt.theme_fin_num > 0) ";
		}
			sql+="order by e.employee_code, tt.theme_bank_code)  where rownum <= ? ";
			Session session = template.getSessionFactory().getCurrentSession();
			SQLQuery sqlQuery=session.createSQLQuery(sql); 
			sqlQuery.setString(0, organId);
			//sqlQuery.setInteger(1, start);
			sqlQuery.setInteger(1, start+limit);
			List tmplist = sqlQuery
					.addScalar("dept_id", StringType.INSTANCE)
					.addScalar("dept_name", StringType.INSTANCE)
					.addScalar("employee_id", StringType.INSTANCE)
					.addScalar("employee_name", StringType.INSTANCE)
					.addScalar("employee_code", StringType.INSTANCE)
					.addScalar("theme_bank_id", StringType.INSTANCE)
					.addScalar("theme_bank_name", StringType.INSTANCE)
					.addScalar("theme_num", IntegerType.INSTANCE)
					.addScalar("theme_fin_num", IntegerType.INSTANCE).list();
			for(int i=start;i<tmplist.size();i++){
				Object[] v = (Object[])tmplist.get(i);
				String deptId = (String)v[0];
				String deptName = (String)v[1];
				String employeeId = (String)v[2];
				String employeeName = (String)v[3];
				String employeeCode = (String)v[4];
				String themeBankId = (String)v[5];
				String themeBankName = (String)v[6];
				Integer themeNum = (Integer)v[7];
				Integer themeFinNum = (Integer)v[8];
				
				EmployeeThemeBankForm employeeThemeBankForm = new EmployeeThemeBankForm();
				employeeThemeBankForm.setDeptId(deptId);
				employeeThemeBankForm.setDeptName(deptName);
				employeeThemeBankForm.setEmployeeId(employeeId);
				employeeThemeBankForm.setEmployeeName(employeeName);
				employeeThemeBankForm.setEmployeeCode(employeeCode);
				employeeThemeBankForm.setThemeBankId(themeBankId);
				employeeThemeBankForm.setThemeBankName(themeBankName);
				employeeThemeBankForm.setThemeNum(themeNum);
				employeeThemeBankForm.setThemeFinNum(themeFinNum);
				list.add(employeeThemeBankForm);
				 
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}

	
	/**
	 * 根据机构查询员工题库的学习情况
	 * @description
	 * @param organId
	 * @return
	 * @modified
	 */
	public int countEmployeeThemeBankInOrgan(String organId,Map paramMap){
		int count = 0;
		try{
			String sql = "select count(*) as count from (select dd.dept_id, "+
					       "dd.dept_name, "+
					       "e.employee_id, "+
					       "e.employee_name, "+
					       "e.employee_code, "+
					       "tt.theme_bank_id, "+
					       "tt.theme_bank_name, "+
					       "tt.theme_num, "+
					       "tt.theme_fin_num "+
					  "from (select tt1.*, tb.theme_bank_name, tb.theme_bank_code "+
					          "from (select t1.employee_id, "+
					                       "t1.theme_bank_id, "+
					                       "t1.theme_num, "+
					                       "decode(t2.theme_fin_num, null, 0, t2.theme_fin_num) as theme_fin_num "+
					                  "from (select * "+
					                          "from personal_bank pp "+
					                         "where pp.theme_num > 0 "+
					                           "and pp.theme_num is not null) t1 "+
					                  "left join personal_bank_fin t2 "+
					                    "on t1.employee_id = t2.employee_id "+
					                   "and t1.theme_bank_id = t2.theme_bank_id) tt1, "+
					               "theme_bank tb "+
					         "where tt1.theme_bank_id = tb.theme_bank_id and tb.theme_bank_name is not null and (tb.bank_public = '10' or (tb.bank_public = '20' and tb.organ_id ='"+organId+"')) and tb.is_L = '10') tt, "+
					       "(select * "+
					          "from employee ee "+
					         "where  ee.isvalidation = '1' and ee.dept_id in "+
					               "(select d.dept_id "+
					                  "from dept d "+
					                 "where d.is_validation = '1'  and d.organ_id = ?)";
		String employeeTerm = (String)paramMap.get("employeeTerm");
		if(employeeTerm!=null && !"undefined".equals(employeeTerm) && !"".equals(employeeTerm)&& !"null".equals(employeeTerm)){
			 sql+=" and ee.employee_name like '%' || '"+employeeTerm+"' ||'%' "; 			
		} 
		String deptTerm = (String)paramMap.get("deptTerm");
		if(deptTerm!=null && !"undefined".equals(deptTerm) && !"".equals(deptTerm)&& !"null".equals(deptTerm)){
			 sql+=" and ee.dept_id in(select d.dept_id "+
				                  "from dept d "+
				                 "where d.is_validation = '1' "+
				                 "start with d.dept_id = '"+deptTerm+"' "+
				                "connect by prior d.dept_id = d.parent_dept_id) "; 			
		} 
					                 
					                 sql+= " ) e, "+
					       "dept dd "+
					 "where e.employee_id = tt.employee_id(+)  and e.isvalidation = '1' "+
					   "and e.dept_id = dd.dept_id ";
		String learnTerm = (String)paramMap.get("learnTerm");
		if("unLearn".equals(learnTerm)){
			sql+=" and  e.employee_id not in( select tt.employee_id  "+
					 " from (select t1.employee_id, "+
					               "sum(t1.theme_num) as theme_num, "+
					               "sum(decode(t2.theme_fin_num, null, 0, t2.theme_fin_num)) as theme_fin_num "+
					          "from (select pp.* from personal_bank pp,theme_bank tb where   pp.theme_bank_id = tb.theme_bank_id "+
							          	" and (tb.bank_public = '10' or (tb.bank_public = '20' and tb.organ_id ='"+organId+"')) "+
						                 " and tb.is_L = '10') t1 "+
					          "left join personal_bank_fin t2 "+
					            "on t1.employee_id = t2.employee_id "+
					           "and t1.theme_bank_id = t2.theme_bank_id "+
					         "group by t1.employee_id) tt, "+
					       "employee e "+
					 "where tt.employee_id = e.employee_id "+
					   "and theme_num >= theme_fin_num "+
					   "and theme_num > 0 "+
					   "and theme_fin_num > 0 "+
					   "and e.isvalidation = 1 )  ";
		}else if("exLearn".equals(learnTerm)){
			sql+=" and  e.employee_id in( select tt.employee_id  "+
				 " from (select t1.employee_id, "+
				               "sum(t1.theme_num) as theme_num, "+
				               "sum(decode(t2.theme_fin_num, null, 0, t2.theme_fin_num)) as theme_fin_num "+
				          "from (select pp.* from personal_bank pp,theme_bank tb where   pp.theme_bank_id = tb.theme_bank_id "+
							          	" and (tb.bank_public = '10' or (tb.bank_public = '20' and tb.organ_id ='"+organId+"')) "+
						                 " and tb.is_L = '10') t1 "+
				          "left join personal_bank_fin t2 "+
				            "on t1.employee_id = t2.employee_id "+
				           "and t1.theme_bank_id = t2.theme_bank_id "+
				         "group by t1.employee_id) tt, "+
				       "employee e "+
				 "where tt.employee_id = e.employee_id  and e.isvalidation = '1' "+
				   "and theme_num > theme_fin_num "+
				   "and theme_num > 0 "+
				   "and theme_fin_num > 0 "+
				   "and e.isvalidation = 1 )  "; 	 	 			
		}else if("finLearn".equals(learnTerm)){
			sql+=" and  e.employee_id in "+ 
					"  (select tttt.employee_id "+
                  "from (select tt.employee_id,sum(theme_num) as theme_num,sum(theme_fin_num) as theme_fin_num "+
                          "from (select t1.employee_id, "+
                                       "sum(t1.theme_num) as theme_num, "+
                                       "sum(decode(t2.theme_fin_num, "+
                                                  "null, "+
                                                  "0, "+
                                                  "t2.theme_fin_num)) as theme_fin_num "+
                                  "from (select pp.* from personal_bank pp,theme_bank tb where   pp.theme_bank_id = tb.theme_bank_id "+
							          	" and (tb.bank_public = '10' or (tb.bank_public = '20' and tb.organ_id ='"+organId+"')) "+
						                 " and tb.is_L = '10') t1 "+
                                  "left join personal_bank_fin t2 "+
                                    "on t1.employee_id = t2.employee_id "+
                                   "and t1.theme_bank_id = t2.theme_bank_id "+
                                 "group by t1.employee_id) tt, "+
                               "employee e "+
                         "where tt.employee_id = e.employee_id  and e.isvalidation = '1' "+
                           "and e.isvalidation = 1 group by tt.employee_id) tttt "+
                 "where tttt.theme_num = tttt.theme_fin_num "+
                   "and tttt.theme_num > 0 "+
                   "and tttt.theme_fin_num > 0) ";
		}
			sql+=" order by e.employee_code, tt.theme_bank_code) ";
			Session session = template.getSessionFactory().getCurrentSession();
			SQLQuery sqlQuery=session.createSQLQuery(sql); 
			sqlQuery.setString(0, organId);
			List<Integer> tmplist = sqlQuery.addScalar("count", IntegerType.INSTANCE).list();
			if(tmplist!=null && tmplist.size()>0){
				count = tmplist.get(0)!=null ? tmplist.get(0).intValue() : 0;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return count;
	}
	/**
	 * 根据部门查询员工题库的学习情况
	 * @description
	 * @param organId
	 * @return
	 * @modified
	 */
	public List<EmployeeThemeBankForm> queryEmployeeThemeBankInDept(String qdeptId,int start,int limit,Map paramMap){
		List<EmployeeThemeBankForm> list = new ArrayList<EmployeeThemeBankForm>();
		try{
			String organId = (String)paramMap.get("organId");
			String sql = "select * from (select dd.dept_id, "+
					       "dd.dept_name, "+
					       "e.employee_id, "+
					       "e.employee_name, "+
					       "e.employee_code, "+
					       "tt.theme_bank_id, "+
					       "tt.theme_bank_name, "+
					       "tt.theme_num, "+
					       "tt.theme_fin_num "+
					  "from (select tt1.*, tb.theme_bank_name, tb.theme_bank_code "+
					          "from (select t1.employee_id, "+
					                       "t1.theme_bank_id, "+
					                       "t1.theme_num, "+
					                       "decode(t2.theme_fin_num, null, 0, t2.theme_fin_num) as theme_fin_num "+
					                  "from (select * "+
					                          "from personal_bank pp "+
					                         "where pp.theme_num > 0 "+
					                           "and pp.theme_num is not null) t1 "+
					                  "left join personal_bank_fin t2 "+
					                    "on t1.employee_id = t2.employee_id "+
					                   "and t1.theme_bank_id = t2.theme_bank_id) tt1, "+
					               "theme_bank tb "+
					         "where tt1.theme_bank_id = tb.theme_bank_id and tb.theme_bank_name is not null and (tb.bank_public = '10' or (tb.bank_public = '20' and tb.organ_id ='"+organId+"')) and tb.is_L = '10') tt, "+
					       "(select * "+
					          "from employee ee "+
					         "where  ee.isvalidation = '1' and ee.dept_id in "+
					               "(select d.dept_id "+
					                  "from dept d "+
					                 "where d.is_validation = '1' "+
					                 "start with d.dept_id = ? "+
					                "connect by prior d.dept_id = d.parent_dept_id)";
			String employeeTerm = (String)paramMap.get("employeeTerm");
			if(employeeTerm!=null && !"undefined".equals(employeeTerm) && !"".equals(employeeTerm)&& !"null".equals(employeeTerm)){
				 sql+=" and ee.employee_name like '%' || '"+employeeTerm+"' ||'%' "; 			
			}
									sql+=" ) e, "+
					       "dept dd "+
					 "where e.employee_id = tt.employee_id(+)  and e.isvalidation = '1' "+
					   "and e.dept_id = dd.dept_id ";
			String learnTerm = (String)paramMap.get("learnTerm");
			if("unLearn".equals(learnTerm)){
				sql+=" and  e.employee_id not in( select tt.employee_id  "+
						 " from (select t1.employee_id, "+
						               "sum(t1.theme_num) as theme_num, "+
						               "sum(decode(t2.theme_fin_num, null, 0, t2.theme_fin_num)) as theme_fin_num "+
						          "from (select pp.* from personal_bank pp,theme_bank tb where   pp.theme_bank_id = tb.theme_bank_id "+
							          	" and (tb.bank_public = '10' or (tb.bank_public = '20' and tb.organ_id ='"+organId+"')) "+
						                 " and tb.is_L = '10') t1 "+
						          "left join personal_bank_fin t2 "+
						            "on t1.employee_id = t2.employee_id "+
						           "and t1.theme_bank_id = t2.theme_bank_id "+
						         "group by t1.employee_id) tt, "+
						       "employee e "+
						 "where tt.employee_id = e.employee_id  and e.isvalidation = '1' "+
						   "and theme_num >= theme_fin_num "+
						   "and theme_num > 0 "+
						   "and theme_fin_num > 0 "+
						   "and e.isvalidation = 1 )  ";
			}else if("exLearn".equals(learnTerm)){
				sql+=" and  e.employee_id in( select tt.employee_id  "+
					 " from (select t1.employee_id, "+
					               "sum(t1.theme_num) as theme_num, "+
					               "sum(decode(t2.theme_fin_num, null, 0, t2.theme_fin_num)) as theme_fin_num "+
					          "from (select pp.* from personal_bank pp,theme_bank tb where   pp.theme_bank_id = tb.theme_bank_id "+
							          	" and (tb.bank_public = '10' or (tb.bank_public = '20' and tb.organ_id ='"+organId+"')) "+
						                 " and tb.is_L = '10') t1 "+
					          "left join personal_bank_fin t2 "+
					            "on t1.employee_id = t2.employee_id "+
					           "and t1.theme_bank_id = t2.theme_bank_id "+
					         "group by t1.employee_id) tt, "+
					       "employee e "+
					 "where tt.employee_id = e.employee_id  and e.isvalidation = '1' "+
					   "and theme_num > theme_fin_num "+
					   "and theme_num > 0 "+
					   "and theme_fin_num > 0 "+
					   "and e.isvalidation = 1 )  "; 	 	 			
			}else if("finLearn".equals(learnTerm)){
				sql+=" and  e.employee_id in "+ 
						"  (select tttt.employee_id "+
	                  "from (select tt.employee_id,sum(theme_num) as theme_num,sum(theme_fin_num) as theme_fin_num "+
	                          "from (select t1.employee_id, "+
	                                       "sum(t1.theme_num) as theme_num, "+
	                                       "sum(decode(t2.theme_fin_num, "+
	                                                  "null, "+
	                                                  "0, "+
	                                                  "t2.theme_fin_num)) as theme_fin_num "+
	                                  "from (select pp.* from personal_bank pp,theme_bank tb where   pp.theme_bank_id = tb.theme_bank_id "+
							          	" and (tb.bank_public = '10' or (tb.bank_public = '20' and tb.organ_id ='"+organId+"')) "+
						                 " and tb.is_L = '10') t1 "+
	                                  "left join personal_bank_fin t2 "+
	                                    "on t1.employee_id = t2.employee_id "+
	                                   "and t1.theme_bank_id = t2.theme_bank_id "+
	                                 "group by t1.employee_id) tt, "+
	                               "employee e "+
	                         "where tt.employee_id = e.employee_id  and e.isvalidation = '1' "+
	                           "and e.isvalidation = 1 group by tt.employee_id) tttt "+
	                 "where tttt.theme_num = tttt.theme_fin_num "+
	                   "and tttt.theme_num > 0 "+
	                   "and tttt.theme_fin_num > 0) ";
			}
			sql+=" order by e.employee_code, tt.theme_bank_code ) where   rownum <= ? ";
			Session session = template.getSessionFactory().getCurrentSession();
			SQLQuery sqlQuery=session.createSQLQuery(sql); 
			sqlQuery.setString(0, qdeptId);
			//sqlQuery.setInteger(1, start);
			sqlQuery.setInteger(1, start+limit);
			List tmplist = sqlQuery
					.addScalar("dept_id", StringType.INSTANCE)
					.addScalar("dept_name", StringType.INSTANCE)
					.addScalar("employee_id", StringType.INSTANCE)
					.addScalar("employee_name", StringType.INSTANCE)
					.addScalar("employee_code", StringType.INSTANCE)
					.addScalar("theme_bank_id", StringType.INSTANCE)
					.addScalar("theme_bank_name", StringType.INSTANCE)
					.addScalar("theme_num", IntegerType.INSTANCE)
					.addScalar("theme_fin_num", IntegerType.INSTANCE).list();
			for(int i=start;i<tmplist.size();i++){
				Object[] v = (Object[])tmplist.get(i);
				String deptId = (String)v[0];
				String deptName = (String)v[1];
				String employeeId = (String)v[2];
				String employeeName = (String)v[3];
				String employeeCode = (String)v[4];
				String themeBankId = (String)v[5];
				String themeBankName = (String)v[6];
				Integer themeNum = (Integer)v[7];
				Integer themeFinNum = (Integer)v[8];
				
				EmployeeThemeBankForm employeeThemeBankForm = new EmployeeThemeBankForm();
				employeeThemeBankForm.setDeptId(deptId);
				employeeThemeBankForm.setDeptName(deptName);
				employeeThemeBankForm.setEmployeeId(employeeId);
				employeeThemeBankForm.setEmployeeName(employeeName);
				employeeThemeBankForm.setEmployeeCode(employeeCode);
				employeeThemeBankForm.setThemeBankId(themeBankId);
				employeeThemeBankForm.setThemeBankName(themeBankName);
				employeeThemeBankForm.setThemeNum(themeNum);
				employeeThemeBankForm.setThemeFinNum(themeFinNum);
				list.add(employeeThemeBankForm);
				 
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 根据部门查询员工题库的学习情况
	 * @description
	 * @param organId
	 * @return
	 * @modified
	 */
	public int countEmployeeThemeBankInDept(String qdeptId,Map paramMap){
		int count = 0;
		try{
			String organId = (String)paramMap.get("organId");
			String sql = "select count(*) count from (select dd.dept_id, "+
					       "dd.dept_name, "+
					       "e.employee_id, "+
					       "e.employee_name, "+
					       "e.employee_code, "+
					       "tt.theme_bank_id, "+
					       "tt.theme_bank_name, "+
					       "tt.theme_num, "+
					       "tt.theme_fin_num "+
					  "from (select tt1.*, tb.theme_bank_name, tb.theme_bank_code "+
					          "from (select t1.employee_id, "+
					                       "t1.theme_bank_id, "+
					                       "t1.theme_num, "+
					                       "decode(t2.theme_fin_num, null, 0, t2.theme_fin_num) as theme_fin_num "+
					                  "from (select * "+
					                          "from personal_bank pp "+
					                         "where pp.theme_num > 0 "+
					                           "and pp.theme_num is not null) t1 "+
					                  "left join personal_bank_fin t2 "+
					                    "on t1.employee_id = t2.employee_id "+
					                   "and t1.theme_bank_id = t2.theme_bank_id) tt1, "+
					               "theme_bank tb "+
					         "where tt1.theme_bank_id = tb.theme_bank_id and tb.theme_bank_name is not null and (tb.bank_public = '10' or (tb.bank_public = '20' and tb.organ_id ='"+organId+"')) and tb.is_L = '10') tt, "+
					       "(select * "+
					          "from employee ee "+
					         "where  ee.isvalidation = '1' and ee.dept_id in "+
					               "(select d.dept_id "+
					                  "from dept d "+
					                 "where d.is_validation = '1' "+
					                 "start with d.dept_id = ? "+
					                "connect by prior d.dept_id = d.parent_dept_id)";
			String employeeTerm = (String)paramMap.get("employeeTerm");
			if(employeeTerm!=null && !"undefined".equals(employeeTerm) && !"".equals(employeeTerm)&& !"null".equals(employeeTerm)){
				 sql+=" and ee.employee_name like '%' || '"+employeeTerm+"' ||'%' "; 			
			}
									sql+=" ) e, "+
					       "dept dd "+
					 "where e.employee_id = tt.employee_id(+)  and e.isvalidation = '1' "+
					   "and e.dept_id = dd.dept_id ";
			String learnTerm = (String)paramMap.get("learnTerm");
			if("unLearn".equals(learnTerm)){
				sql+=" and  e.employee_id not in( select tt.employee_id  "+
						 " from (select t1.employee_id, "+
						               "sum(t1.theme_num) as theme_num, "+
						               "sum(decode(t2.theme_fin_num, null, 0, t2.theme_fin_num)) as theme_fin_num "+
						          "from (select pp.* from personal_bank pp,theme_bank tb where   pp.theme_bank_id = tb.theme_bank_id "+
							          	" and (tb.bank_public = '10' or (tb.bank_public = '20' and tb.organ_id ='"+organId+"')) "+
						                 " and tb.is_L = '10') t1 "+
						          "left join personal_bank_fin t2 "+
						            "on t1.employee_id = t2.employee_id "+
						           "and t1.theme_bank_id = t2.theme_bank_id "+
						         "group by t1.employee_id) tt, "+
						       "employee e "+
						 "where tt.employee_id = e.employee_id  and e.isvalidation = '1' "+
						   "and theme_num >= theme_fin_num "+
						   "and theme_num > 0 "+
						   "and theme_fin_num > 0 "+
						   "and e.isvalidation = 1 )  ";
			}else if("exLearn".equals(learnTerm)){
				sql+=" and  e.employee_id in( select tt.employee_id  "+
					 " from (select t1.employee_id, "+
					               "sum(t1.theme_num) as theme_num, "+
					               "sum(decode(t2.theme_fin_num, null, 0, t2.theme_fin_num)) as theme_fin_num "+
					          "from (select pp.* from personal_bank pp,theme_bank tb where   pp.theme_bank_id = tb.theme_bank_id "+
							          	" and (tb.bank_public = '10' or (tb.bank_public = '20' and tb.organ_id ='"+organId+"')) "+
						                 " and tb.is_L = '10') t1 "+
					          "left join personal_bank_fin t2 "+
					            "on t1.employee_id = t2.employee_id "+
					           "and t1.theme_bank_id = t2.theme_bank_id "+
					         "group by t1.employee_id) tt, "+
					       "employee e "+
					 "where tt.employee_id = e.employee_id  and e.isvalidation = '1' "+
					   "and theme_num > theme_fin_num "+
					   "and theme_num > 0 "+
					   "and theme_fin_num > 0 "+
					   "and e.isvalidation = 1 )  "; 	 	 			
			}else if("finLearn".equals(learnTerm)){
				sql+=" and  e.employee_id in "+ 
						"  (select tttt.employee_id "+
	                  "from (select tt.employee_id,sum(theme_num) as theme_num,sum(theme_fin_num) as theme_fin_num "+
	                          "from (select t1.employee_id, "+
	                                       "sum(t1.theme_num) as theme_num, "+
	                                       "sum(decode(t2.theme_fin_num, "+
	                                                  "null, "+
	                                                  "0, "+
	                                                  "t2.theme_fin_num)) as theme_fin_num "+
	                                  "from (select pp.* from personal_bank pp,theme_bank tb where   pp.theme_bank_id = tb.theme_bank_id "+
							          	" and (tb.bank_public = '10' or (tb.bank_public = '20' and tb.organ_id ='"+organId+"')) "+
						                 " and tb.is_L = '10') t1 "+
	                                  "left join personal_bank_fin t2 "+
	                                    "on t1.employee_id = t2.employee_id "+
	                                   "and t1.theme_bank_id = t2.theme_bank_id "+
	                                 "group by t1.employee_id) tt, "+
	                               "employee e "+
	                         "where tt.employee_id = e.employee_id  and e.isvalidation = '1' "+
	                           "and e.isvalidation = 1 group by tt.employee_id) tttt "+
	                 "where tttt.theme_num = tttt.theme_fin_num "+
	                   "and tttt.theme_num > 0 "+
	                   "and tttt.theme_fin_num > 0) ";
			}
			sql+=" order by e.employee_code, tt.theme_bank_code)";
			Session session = template.getSessionFactory().getCurrentSession();
			SQLQuery sqlQuery=session.createSQLQuery(sql); 
			sqlQuery.setString(0, qdeptId);
			List<Integer> tmplist = sqlQuery.addScalar("count", IntegerType.INSTANCE).list();
			if(tmplist!=null && tmplist.size()>0){
				count = tmplist.get(0)!=null ? tmplist.get(0).intValue() : 0;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return count;
	}
	
	
	
	/**
	 *
	 * @description
	 * @return
	 * @modified
	 */
	public String getEmployeeBankCreateTime(){
		String time = null;
		try{
			String sql = "select max(t.creation_date) creation_date from PERSONAL_BANK t";
			Session session = template.getSessionFactory().getCurrentSession();
			SQLQuery sqlQuery=session.createSQLQuery(sql); 
			List<String> tmplist = sqlQuery.addScalar("creation_date", StringType.INSTANCE).list();
			if(tmplist!=null && tmplist.size()>0){
				time = tmplist.get(0);
			}
		}catch(Exception e){
			
		}
		return time;
	}
}
