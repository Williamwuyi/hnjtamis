package cn.com.ite.hnjtamis.baseinfo.speciality;

import org.hibernate.Session;

import cn.com.ite.eap2.core.hibernate.HibernateDefaultDAOImpl;

/**
 *
 * <p>Title cn.com.ite.hnjtamis.baseinfo.speciality.SpecialityDaoImpl</p>
 * <p>Description </p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2016</p>
 * @create time: 2016年12月7日 下午3:56:10
 * @version 1.0
 * 
 * @modified records:
 */
public class SpecialityDaoImpl extends HibernateDefaultDAOImpl implements SpecialityDao{
	
	/**
	 * 同步岗位对应专业
	 * @description
	 * @modified
	 */
	public void saveSyncJobsUnionSpeciality()throws Exception{
			Session session=template.getSessionFactory().getCurrentSession();
			String delStr = "delete JOBS_UNION_SPECIALITY";
			session.createSQLQuery(delStr).executeUpdate();
			
			
			
			String instr = " insert into JOBS_UNION_SPECIALITY "+
						    "(JUSID, "+
						     "SPECIALITYID, "+
						     "JOBSCODE, "+
						     "JOBSNAME, "+
						     "REMARKS, "+
						     "ORDERNO, "+
						     "ISAVAILABLE, "+
						     "STATUS, "+
						     "LAST_UPDATE_DATE, "+
						     "LAST_UPDATED_BY, "+
						     "CREATION_DATE, "+
						     "CREATED_BY, "+
						     "ORGANID," + 
						     "quarter_id) "+
						    "(select to_char(SYSDATE, 'yyyymmddhh24miss') || REPLACE(TRIM(DBMS_RANDOM.VALUE), '.', '') as JUSID, "+
						            "sst.specialityid as SPECIALITYID, "+
						            "t.quarter_train_code as JOBSCODE, "+
						            "t.quarter_train_name as JOBSNAME, "+
						            "'' as REMARKS, "+
						            "0 as ORDERNO, "+
						            "1 as ISAVAILABLE, "+
						            "1 as STATUS, "+
						            "to_char(sysdate, 'yyyy-MM-dd hh24:mi:ss') as LAST_UPDATE_DATE, "+
						            "'' as LAST_UPDATED_BY, "+
						            "to_char(sysdate, 'yyyy-MM-dd hh24:mi:ss') as CREATION_DATE, "+
						            "'系统' as CREATED_BY, "+
						            "ss.organ_name as ORGANID,"+ 
						            "t.quarter_id as quarter_id "+
						       "from JOBS_STANDARD_QUARTER            t, "+
						            "BASE_SPECIALITY_STANDARD_TYPES sst,"
						            + "quarter_standard ss    "+
						      "where t.standardid = sst.types_id and t.quarter_id = ss.quarter_id  )";
			session.createSQLQuery(instr).executeUpdate();
			
			
			
			//同步岗位对应题库
			/*String delStr2 = "delete THEME_BANK_POST t";
			session.createSQLQuery(delStr2).executeUpdate();
			
			String instr2 = " insert into THEME_BANK_POST( "+
					"bank_post_id, "+
					"theme_bank_id, "+
					"organ_id, "+
					"organ_name, "+
					"dept_id, "+
					"dept_name, "+
					"post_id, "+
					"post_name, "+
					"creation_date, "+
					"created_by "+
					") "+
					"( "+
					"select to_char(SYSDATE, 'yyyymmddhh24miss') || REPLACE(TRIM(DBMS_RANDOM.VALUE), '.', '') as bank_post_id, "+
					"v.theme_bank_id as theme_bank_id,  "+
					"d.organ_id as organ_id, "+
					"o.organ_name as organ_name, "+
					"d.dept_id, "+
					"d.dept_name, "+
					"t.jobscode, "+
					"q.quarter_name, "+
					"to_char(sysdate, 'yyyy-MM-dd hh24:mi:ss') as CREATION_DATE, "+
					"'系统' as CREATED_BY "+
					  "from JOBS_UNION_STANDARD t, JOBS_STANDARD v, quarter q,dept d,organ o "+
					 "where t.standardid = v.standardid and t.jobscode = q.quarter_id and q.dept_id = d.dept_id and d.organ_id = o.organ_id "+
					   "and v.theme_bank_id is not null "+
					   "and d.is_validation = 1 and q.is_validation = 1 and o.is_validation = 1)";
			   
			session.createSQLQuery(instr2).executeUpdate();*/

	}
	
}
