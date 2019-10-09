package cn.com.ite.hnjtamis.exam.base.exampublic.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.orm.hibernate4.HibernateTemplate;

import cn.com.ite.eap2.core.hibernate.HibernateDefaultDAOImpl;
import cn.com.ite.eap2.domain.organization.Employee;

public class ExamPublicDaoImpl extends HibernateDefaultDAOImpl implements ExamPublicDao{

	/**
	 * 获取考生信息
	 * @author 朱健
	 * @return
	 * @modified
	 */
	/*private static String queryEmployeeNotHgAndQuarter_SQL = "  select {ey.*} from employee ey,(select kk2.EMPLOYEE_ID, "+
		       " decode(kk1.passnum, null, 0, kk1.passnum) passnum, "+
		       " kk2.themebanknum "+
		 " from (select k1.EMPLOYEE_ID, count(*) passnum "+
		      "  from v_employee_pass_themebank k1 "+
		      "  where not exists (select 1 "+
		      " from v_employee_pass_themebank ka "+
		      " where ka.score_end_time < ? "+
		      " or ka.score_start_time > ? "+
		      " and k1.EMPLOYEE_ID = ka.EMPLOYEE_ID "+
		      " and k1.theme_bank_id = ka.theme_bank_id) "+
		      " group by k1.EMPLOYEE_ID) kk1, "+
		      " (select k2.EMPLOYEE_ID, count(*) themebanknum "+
		      " from v_employee_themebank k2 "+
		      " group by k2.EMPLOYEE_ID) kk2 "+
		      " where kk2.EMPLOYEE_ID = kk1.EMPLOYEE_ID(+)) y  "+
		      " where ey.employee_id = y.EMPLOYEE_ID   "+
		      " and ? like '%,'|| ey.quarter_id || ',%'"+
		      " and y.passnum < y.themebanknum ";*/
	private static String queryEmployeeNotHgAndQuarter_SQL = "  select {ey.*} from employee ey "+
		      " where   ? like '%,'|| ey.quarter_id || ',%' and ey.isvalidation = '1' ";
	public List<Employee> getExamEmployeeInQuarter(String scoreStartTime,String scoreEndTime,Map<String,String> gwMap){
		List<Employee> emplist = new ArrayList<Employee>();
		HibernateTemplate template = (HibernateTemplate) this.template;		   
		Session session = template.getSessionFactory().getCurrentSession();
    	Iterator its = gwMap.keySet().iterator();
    	String qids ="";
    	int len=0;
    	while(its.hasNext()){
    		qids+=((String)its.next())+",";
    		len++;
    		if(len == 12){
    			//param.put("quarterIds", qids.substring(0, qids.length()-1));
		    	//List tmplist = this.getDao().queryConfigQl("queryEmployeeNotHgAndQuarter", param, null, Employee.class);
    			SQLQuery sqlQuery=session.createSQLQuery(queryEmployeeNotHgAndQuarter_SQL);
    			//sqlQuery.setString(0, scoreStartTime);
    			//sqlQuery.setString(1, scoreEndTime);
    			sqlQuery.setString(0, ","+qids+",");
    			List<Employee> list = sqlQuery.addEntity("ey", Employee.class).list();
		    	emplist.addAll(list);
		    	len = 0;
		    	qids = "";
    		}	
    	}
    	if(len>0){
    		SQLQuery sqlQuery=session.createSQLQuery(queryEmployeeNotHgAndQuarter_SQL);
			//sqlQuery.setString(0, scoreStartTime);
			//sqlQuery.setString(1, scoreEndTime);
			sqlQuery.setString(0, ","+qids+",");
			List<Employee> list = sqlQuery.addEntity("ey", Employee.class).list();
	    	emplist.addAll(list);
	    	len = 0;
	    	qids = "";
    	}
		return emplist;
	}
	
	/**
	 * 获取考生信息
	 * @author 朱健
	 * @return
	 * @modified
	 */
	private static String queryEmployeeNotHg_SQL = "select {ey.*} from employee ey,(select kk2.EMPLOYEE_ID, "+
		       " decode(kk1.passnum, null, 0, kk1.passnum) passnum, "+
		       " kk2.themebanknum "+
		  " from (select k1.EMPLOYEE_ID, count(*) passnum "+
		          " from v_employee_pass_themebank k1 "+
		         " where not exists (select 1 "+
		                 " from v_employee_pass_themebank ka "+
		                 " where ka.score_end_time < ? "+
		                   " or ka.score_start_time > ? "+
		                   " and k1.EMPLOYEE_ID = ka.EMPLOYEE_ID "+
		                   " and k1.theme_bank_id = ka.theme_bank_id) "+
		         " group by k1.EMPLOYEE_ID) kk1, "+
		       " (select k2.EMPLOYEE_ID, count(*) themebanknum "+
		         " from v_employee_themebank k2 "+
		         " group by k2.EMPLOYEE_ID) kk2 "+
		 " where kk2.EMPLOYEE_ID = kk1.EMPLOYEE_ID(+)) y  "+
		  " where ey.employee_id = y.EMPLOYEE_ID   and y.passnum < y.themebanknum ";
	public List<Employee> getExamEmployee(String scoreStartTime,String scoreEndTime){
		HibernateTemplate template = (HibernateTemplate) this.template;		   
		Session session = template.getSessionFactory().getCurrentSession();
		SQLQuery sqlQuery=session.createSQLQuery(queryEmployeeNotHg_SQL);
		sqlQuery.setString(0, scoreStartTime);
		sqlQuery.setString(1, scoreEndTime);
		List<Employee> emplist = sqlQuery.addEntity("ey", Employee.class).list();
		return emplist;
	}
	
}
