package cn.com.ite.hnjtamis.jobstandard.terms;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.type.DoubleType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;
import org.springframework.orm.hibernate4.HibernateTemplate;

import cn.com.ite.eap2.common.utils.DateUtils;
import cn.com.ite.eap2.common.utils.NumericUtils;
import cn.com.ite.eap2.core.hibernate.HibernateDefaultDAOImpl;
import cn.com.ite.hnjtamis.jobstandard.domain.JobsStandardQuarter;
import cn.com.ite.hnjtamis.jobstandard.domain.QuarterStandard;
import cn.com.ite.hnjtamis.jobstandard.domain.StandardTerms;


/**
 *
 * @author 朱健
 * @create time: 2015年11月3日 上午11:48:37
 * @version 1.0
 * 
 * @modified records:
 */
public class StandardTermsDaoImpl extends HibernateDefaultDAOImpl implements StandardTermsDao{
	
	/**
	 *
	 * @author 朱健
	 * @param employeeId
	 * @param relationType
	 * @return 查询题库的题目数量与当前模拟考试（或对应relationType类型）的题目完成数量
	 * @modified
	 */
	public Map<String,String> getThemeNumInBank(String employeeId,String relationType){
		Map<String,String> value = new HashMap<String,String>();
		try{
			HibernateTemplate template = (HibernateTemplate)  this.template;
			Session session = template.getSessionFactory().getCurrentSession();
			String sqlStr = "select tty.theme_type_id from theme_type tty  where tty.theme_type in (5, 10, 25)";
			SQLQuery sqlQuery=session.createSQLQuery(sqlStr);
			List list =sqlQuery.addScalar("theme_type_id", StringType.INSTANCE).list();
			String themeTypeIds = "";
			for(int i=0;i<list.size();i++){
				String obj = (String)list.get(i);
				themeTypeIds+="'"+obj+"',";
			}
			themeTypeIds+="'all'";
			
			
			/*sqlStr = " select count(*) THEMENUM, THEMEBANKID "+
						 " from (select b.theme_bank_id THEMEBANKID, b.theme_id "+
						          " from (select * from exam_testpaper_theme ett where ett.theme_type_id in("+themeTypeIds+")) t, "+
						             " (select * from theme te where te.theme_type_id in("+themeTypeIds+")  te.type in (20, 40) and te.state = 15 and te.IS_USE = 5 )   v, "+
						             " (select * from exam_testpaper ss where ss.employee_id = ? and ss.relation_type like 'mo%') et, "+
						             " theme_in_bank  b, "+
						             " (select * from exam_user_testpaper eut where eut.sub_time is not null)  u "+
						             " where t.theme_id = v.theme_id "+
						             " and et.exam_testpaper_id = t.exam_testpaper_id "+
						             " and t.theme_id = b.theme_id "+
						             " and u.testpaper_id = et.exam_testpaper_id "+
						             " group by b.theme_bank_id, b.theme_id) "+
						             " group by THEMEBANKID ";*/
		/*	sqlStr = "select count(*) THEMENUM, THEMEBANKID "+
					  "from (select b.theme_bank_id THEMEBANKID, b.theme_id "+
					          "from (select ett.theme_id from exam_testpaper_theme ett, ( "+
						                 " select * "+
						                  "  from exam_user_answerkey t "+
						                  " where  dbms_lob.instr(t.answerkey_value, 'null', 1, 1) = 0 "+
						                   "  and t.answerkey_value is not null) usa,"+//注意本处用于排除未填写答案的
					                       "(select eut.testpaper_id from exam_user_testpaper eut "+
					                                 " where eut.employee_id = ? and eut.relation_type like 'mo%' and eut.relation_type is not null "
					                                 + " and eut.sub_time is not null) etp "+
					                 "where ett.exam_testpaper_id = etp.testpaper_id "
					                 + "  and ett.exam_testpaper_theme_id = usa.exam_testpaper_theme_id "+
					                 "and ett.theme_type_id in ("+themeTypeIds+")) t, "+
					               "(select te.theme_id from theme te "+
					                 " where te.type in (20, 40) and te.state = 15 and te.IS_USE = 5 "+
					                   " and te.theme_type_id in ("+themeTypeIds+")) v,theme_in_bank b "+
					         " where t.theme_id = v.theme_id and t.theme_id = b.theme_id "+
					         " group by b.theme_bank_id, b.theme_id) group by THEMEBANKID";*/
			sqlStr = "select count(*) THEMENUM, THEMEBANKID "+
					  "from (select b.theme_bank_id THEMEBANKID, b.theme_id "+
					          "from (select ett.theme_id "+
					                  "from study_testpaper_theme ett, "+
					                       "(select t.study_testpaper_theme_id "+
					                          "from study_user_answerkey t "+
					                         "where t.answerkey_value <> 'null' "+
					                           "and t.answerkey_value is not null "+
					                           // "and t.sub_time is not null "+
					                           "and t.created_id_by = ? "+
					                         "union all "+
					                          "select t.study_testpaper_theme_id "+
					                          "from study_user_answerkey_his t "+
					                         "where t.answerkey_value <> 'null' "+
					                           "and t.answerkey_value is not null "+
					                           " and t.sub_time is not null "+
					                          " and t.created_id_by = ?) usa "+
					                 "where ett.study_testpaper_theme_id = usa.study_testpaper_theme_id "+
					                   " and ett.relation_type like 'mo%' "+
			                           " and ett.relation_type is not null "+
					                   " and ett.theme_type_id in ("+themeTypeIds+")) t, "+
					              " (select te.theme_id "+
					                  "from theme te "+
					                 "where te.type in (20, 40) "+
					                   "and te.state = 15 "+
					                   "and te.IS_USE = 5 "+
					                   "and te.theme_type_id in ("+themeTypeIds+")) v, "+
					               "theme_in_bank b "+
					         "where t.theme_id = v.theme_id "+
					           "and t.theme_id = b.theme_id "+
					        " group by b.theme_bank_id, b.theme_id) "+
					 "group by THEMEBANKID";
			sqlQuery=session.createSQLQuery(sqlStr);
			sqlQuery.setString(0, employeeId);
			sqlQuery.setString(1, employeeId);
		    //sqlQuery.setString(1, "moni");
			list =sqlQuery.addScalar("THEMENUM", IntegerType.INSTANCE).addScalar("THEMEBANKID",StringType.INSTANCE).list();
			for(int i=0;i<list.size();i++){
				Object[] obj = (Object[])list.get(i);
				int themeNum = (int)obj[0];
				String themeBankId = (String)obj[1];
				value.put(themeBankId+"_FINNUM", themeNum+"");
			}
			
			sqlStr  = "select b.theme_bank_id THEMEBANKID, count(*) THEMENUM "+
					  " from theme_in_bank b,theme v,theme_type tt  "
					  + " where b.theme_id = v.theme_id and tt.theme_type_id=v.theme_type_id"
					  + " and v.state = 15 and v.IS_USE = 5 and tt.theme_type in(5,10,25) "
					  + "group by b.theme_bank_id";
			sqlQuery=session.createSQLQuery(sqlStr);
			list =sqlQuery.addScalar("THEMENUM", IntegerType.INSTANCE).addScalar("THEMEBANKID",StringType.INSTANCE).list();
			for(int i=0;i<list.size();i++){
				Object[] obj = (Object[])list.get(i);
				int themeNum = (int)obj[0];
				String themeBankId = (String)obj[1];
				value.put(themeBankId, themeNum+"");
			}
		}catch(Exception e ){
			e.printStackTrace();
		}
		return value;
	}
	
	
	/**
	 * 获取模拟试题
	 * @author 朱健
	 * @param employee_id
	 * @return
	 * @modified
	 */
	public Map<String,String> getMoniExamScore(String employee_id,String relationType){
		Map<String,String> value = new HashMap<String,String>();
		try{
			String sqlStr  = "select t.relation_id, t.total_score, u.frist_scote "+
							  " from exam_testpaper t, EXAM_USER_TESTPAPER u "+
							 " where t.exam_testpaper_id = u.testpaper_id "+
							   " and t.relation_type like '%' || ? ||'%' "+
							   " and u.relation_type like '%' || ? ||'%' "+
							   " and t.employee_id = ? "+
							   " and u.employee_id = ? "+
							   " and u.frist_scote is not null "+
							 " order by t.creation_date ";
			HibernateTemplate template = (HibernateTemplate)  this.template;
			Session session = template.getSessionFactory().getCurrentSession();
			SQLQuery sqlQuery=session.createSQLQuery(sqlStr);
			sqlQuery.setString(0, relationType);
			sqlQuery.setString(1, relationType);
			sqlQuery.setString(2, employee_id);
			sqlQuery.setString(3, employee_id);
			List list =sqlQuery.addScalar("relation_id", StringType.INSTANCE)
					.addScalar("total_score",DoubleType.INSTANCE)
					.addScalar("frist_scote",DoubleType.INSTANCE).list();
			for(int i=0;i<list.size();i++){
				Object[] obj = (Object[])list.get(i);
				String themeBankId = (String)obj[0];
				Double total_score = (Double)obj[1];
				Double frist_scote = (Double)obj[2];
				value.put(themeBankId, total_score!=0 ? NumericUtils.round(frist_scote/total_score*100,2)+"" : "0.00");
				value.put(themeBankId+"_total_score", total_score+"");
				value.put(themeBankId+"_frist_scote", frist_scote+"");
			}
		}catch(Exception e ){
			e.printStackTrace();
		}
		return value;
	}
	
	
	/**
	 * 获取岗位培训得分情况 
	 * @author 朱健
	 * @param employee_id
	 * @param exam_type_id
	 * @param startTime
	 * @param endTime
	 * @return
	 * @modified
	 */
	public Map<String,String> getGwpxExamScore(String employee_id,String exam_property,String startTime,String endTime){
		Map<String,String> value = new HashMap<String,String>();
		try{
			String nowDay = DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd");
			String sqlStr  = "select k.theme_bank_id, t.total_score, u.frist_scote ,e.pass_score "+
						 " from exam_testpaper      t, "+
						      " EXAM_USER_TESTPAPER u, "+
						      " testpaper_skey      k, "+
						      " exam                e, "+
						      " exam_arrange        a "+
						      " where t.exam_testpaper_id = u.testpaper_id "+
						      " and t.testpaper_id = k.testpaper_id "+
						      " and t.relation_type is null "+
						      " and u.relation_type is null "+
						      " and t.exam_id = e.exam_id "+
						      " and a.exam_arrange_id = e.exam_arrange_id "+
						      " and a.exam_property = ? "+
						      " and t.employee_id = ? "+
						      " and u.employee_id = ? "+ 
						      " and u.score_start_time <= '"+nowDay+"' "+
						      " and u.score_end_time >= '"+nowDay+"' "+
						      " and u.score_start_time is not null and u.score_end_time is not null "+
						      " order by t.creation_date";
			HibernateTemplate template = (HibernateTemplate) this.template;
			Session session = template.getSessionFactory().getCurrentSession();
			SQLQuery sqlQuery=session.createSQLQuery(sqlStr);
			sqlQuery.setString(0, exam_property);
			sqlQuery.setString(1, employee_id);
			sqlQuery.setString(2, employee_id);
			List list =sqlQuery.addScalar("theme_bank_id", StringType.INSTANCE)
					.addScalar("total_score",DoubleType.INSTANCE)
					.addScalar("frist_scote",DoubleType.INSTANCE)
					.addScalar("pass_score",DoubleType.INSTANCE).list();
			for(int i=0;i<list.size();i++){
				Object[] obj = (Object[])list.get(i);
				String themeBankId = (String)obj[0];
				Double total_score = (Double)obj[1];
				Double frist_scote = (Double)obj[2];
				Double pass_score = (Double)obj[3];
				Double dfl = total_score!=0 ? frist_scote/total_score*100 : 0.00;
				value.put(themeBankId, dfl!=null && pass_score!=null && dfl >=pass_score ? "合格" : (pass_score==null ? null : "不合格"));
				value.put(themeBankId+"_dfl", NumericUtils.round(dfl,2)+"");
				value.put(themeBankId+"_total_score", total_score+"");
				value.put(themeBankId+"_frist_scote", frist_scote+"");
				value.put(themeBankId+"_pass_score", pass_score+"");
			}
		}catch(Exception e ){
			e.printStackTrace();
		}
		return value;
	}
	
	
	
	/**
	 * 在jobs_standard_quarter表没有数据的时候进行初始化
	 * @author 朱健
	 * @modified
	 */
	public void saveInitAllJobStandardQuarter(){
		String sql = "select count(*) QNUM from jobs_standard_quarter t" ;
		HibernateTemplate template = (HibernateTemplate) this.template;		   
		Session session = template.getSessionFactory().getCurrentSession();
		SQLQuery sqlQuery=session.createSQLQuery(sql); 
		List<Integer> countlist = sqlQuery
				.addScalar("QNUM", IntegerType.INSTANCE).list();
		if(countlist!=null && countlist.size()>0){
			Integer QNUM = countlist.get(0);
			if(QNUM!=null && QNUM.intValue() == 0){
				List<QuarterStandard> quarterStandardList = this.queryHql("from QuarterStandard t",null,null,QuarterStandard.class);
				Map<String,List<QuarterStandard>> quarterStandardMap = new HashMap<String,List<QuarterStandard>>();
				for(int i=0;i<quarterStandardList.size();i++){
					QuarterStandard quarterStandard = quarterStandardList.get(i);
					List list = quarterStandardMap.get(quarterStandard.getQuarterCode());//发现编码有重复的，因此用list
					if(list == null){ list = new ArrayList();}
					list.add(quarterStandard);
					quarterStandardMap.put(quarterStandard.getQuarterCode(),list);
				}
				
				
				List<StandardTerms> standardTermsList = this.queryHql("from StandardTerms t",null,null,StandardTerms.class);
				Map<String,StandardTerms> standardTermsMap = new HashMap<String,StandardTerms>();
				for(int i=0;i<standardTermsList.size();i++){
					StandardTerms standardTerms = standardTermsList.get(i);
					standardTermsMap.put(standardTerms.getStandardid(),standardTerms);
				}
				
				try{
					List saveList = new ArrayList();
					sql = " select t.quarter_train_code quartertraincode,t.standardid from jobs_union_standard t "
							+ " group by t.quarter_train_code,t.standardid ";
					sqlQuery=session.createSQLQuery(sql); 
					List<Object[]> list = sqlQuery
							.addScalar("quartertraincode", StringType.INSTANCE)
							.addScalar("standardid", StringType.INSTANCE).list();
					Map value = new HashMap();
					for(int i=0;i<list.size();i++){
						 Object[] tmp= (Object[])list.get(i);
						 String quartertraincode = (String)tmp[0];
						 String standardid = (String)tmp[1];
						 StandardTerms standardTerms = standardTermsMap.get(standardid);
						 List<QuarterStandard> quarterStandardlist = quarterStandardMap.get(quartertraincode);
						 if(value.get(standardid+"_"+quartertraincode)==null
								 && standardTerms!=null && quarterStandardlist!=null && quarterStandardlist.size()>0){
							 for(int k=0;k<quarterStandardlist.size();k++){
								 QuarterStandard quarterStandard = quarterStandardlist.get(k);
								 
								 JobsStandardQuarter jobsStandardQuarter = new JobsStandardQuarter();
								 jobsStandardQuarter.setQuarterTrainCode(quarterStandard.getQuarterCode());
								 jobsStandardQuarter.setQuarterTrainName(quarterStandard.getQuarterName());
								 jobsStandardQuarter.setStandardTerms(standardTerms);
								 jobsStandardQuarter.setOrderno(k);
								 jobsStandardQuarter.setCreatedBy("系统初始化");
								 jobsStandardQuarter.setCreationDate(DateUtils.convertDateToStr(new Date(),"yyyy-MM-dd HH:mm:ss"));
								 jobsStandardQuarter.setDeptName(quarterStandard.getDeptName());
								 jobsStandardQuarter.setDeptId(quarterStandard.getDeptId());
								 jobsStandardQuarter.setSpecialityName(quarterStandard.getSpecialityName());
								 jobsStandardQuarter.setSpecialityCode(quarterStandard.getSpecialityCode());
								 jobsStandardQuarter.setDcType(quarterStandard.getDcType());
								 jobsStandardQuarter.setQuarterId(quarterStandard.getQuarterId());
								 
								 saveList.add(jobsStandardQuarter);
							 }
							 value.put(standardid+"_"+quartertraincode,standardid+"_"+quartertraincode);
						 }
					}
					if(saveList.size()>0)this.saveBatchEntity(saveList);
				}catch(Exception e){
					e.printStackTrace();
				}
				
			}
		}
	}
	
	
	/**
	 * 根据标准条款中对应的系统岗位信息，处理不存在的并保存到标准条款中的标准岗位信息里面
	 * @author 朱健
	 * @modified
	 */
	public void updateStandardQuarterNotInSysQuarter(){
		try{
			Session session=template.getSessionFactory().getCurrentSession();
			//根据标准条款中对应的系统岗位信息，处理不存在的并保存到标准条款中的标准岗位信息里面
			String instr = "insert into jobs_standard_quarter "+
							  "(RELATION_ID, "+
							   "STANDARDID, "+
							   "QUARTER_TRAIN_CODE, "+
							   "QUARTER_TRAIN_NAME, "+
							   "ORDERNO, "+
							   "CREATION_DATE, "+
							   "CREATED_BY, "+
							   "DEPT_NAME, "+
							   "DEPT_ID, "+
							   "SPECIALITY_NAME, "+
							   "SPECIALITY_CODE, "+
							   "DC_TYPE, "+
							   "QUARTER_ID) "+
							  "(select to_char(SYSDATE, 'yyyymmddhh24miss') ||  REPLACE(TRIM(DBMS_RANDOM.VALUE), '.', ''), "+
							          "s.standardid, "+
							          "t.quarter_code, "+
							          "t.quarter_name, "+
							          "rownum, "+
							          "to_char(sysdate, 'yyyy-MM-dd hh24:mi:ss'), "+
							          "'系统', "+
							          "t.dept_name, "+
							          "t.dept_id, "+
							          "t.speciality_name, "+
							          "t.speciality_code, "+
							          "t.dc_type, "+
							          "t.quarter_id "+
							     "from jobs_union_standard s, quarter_standard t "+
							    "where s.quarter_train_code = t.quarter_code "+
							      "and not exists "+
							    "(select 1 "+
							             "from jobs_standard_quarter sq "+
							            "where sq.standardid = s.standardid "+
							              "and sq.quarter_train_code = s.quarter_train_code)) ";		   
			
			session.createSQLQuery(instr).executeUpdate();
			
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 根据标准条款中对应的标准岗位信息更新标准条款对应的系统岗位信息
	 * @author 朱健
	 * @modified
	 */
	public void updateUnionStandardByStandard(){
		try{
			Session session=template.getSessionFactory().getCurrentSession();
			String delStr = " delete jobs_union_standard s where not exists(select 1 from jobs_standard_quarter sq "+
					" where sq.standardid = s.standardid and sq.quarter_train_code = sq.quarter_train_code) ";
			session.createSQLQuery(delStr).executeUpdate();
			
			
			String instr = "insert into jobs_union_standard "+
							  "(JUSDID, "+
							   "STANDARDID, "+
							   "JOBSCODE, "+
							   "STANDARD_MODE, "+
							   "STANDARD_SCORE, "+
							   "STANDARD_GRADE, "+
							   "PROPORTITION, "+
							   "PERIODWEEKS, "+
							   "JOBSNAME, "+
							   "REMARKS, "+
							   "ORDERNO, "+
							   "ISAVAILABLE, "+
							   "STATUS, "+
							   "CREATION_DATE, "+
							   "CREATED_BY, "+
							   "ORGANID, "+
							   "EFFICIENT, "+
							   "REF_SCORE, "+
							   "UPSTANDARD_SCORE, "+
							   "EXAM_TYPE_NAME, "+
							   "QUARTER_TRAIN_CODE, "+
							   "QUARTER_TRAIN_NAME) "+
							  "(select to_char(SYSDATE, 'yyyymmddhh24miss') ||  REPLACE(TRIM(DBMS_RANDOM.VALUE), '.', ''), "+
							          "s.standardid, "+
							          "q.quarter_id, "+
							          "0, "+
							          "null, "+
							          "null, "+
							          "null, "+
							          "null, "+
							          "q.quarter_name, "+
							          "'系统', "+
							          "rownum, "+
							          "1, "+
							          "1, "+
							          "to_char(sysdate, 'yyyy-MM-dd hh24:mi:ss'), "+
							          "'系统', "+
							          "d.organ_id, "+
							          "null, "+
							          "null, "+
							          "null, "+
							          "null, "+
							          "s.quarter_train_code, "+
							          "s.quarter_train_name "+
							     "from jobs_standard_quarter s, quarter q, dept d "+
							    "where s.quarter_train_code = q.quarter_train_code "+
							      "and q.dept_id = d.dept_id and not exists(select 1 from jobs_union_standard ss where ss.standardid = s.standardid  "+
							      "and ss.quarter_train_code = s.quarter_train_code)) ";
			session.createSQLQuery(instr).executeUpdate();
			
			
			String sql1="update JOBS_STANDARD_QUARTER t "+
				   "set t.theme_bank_id = "+
				       "(select v.theme_bank_id from JOBS_STANDARD v where v.standardid = t.standardid and v.theme_bank_id is not null) "+
				 "where t.theme_bank_id is null";
			session.createSQLQuery(sql1).executeUpdate();
 
 
			String sql2=" update JOBS_STANDARD_QUARTER t "+
				   " set t.theme_bank_code = "+
				       "(select v.theme_bank_code from JOBS_STANDARD v where v.standardid = t.standardid and v.theme_bank_code is not null) "+
				" where t.theme_bank_code is null";
			session.createSQLQuery(sql2).executeUpdate();


			String sql3 = " update JOBS_STANDARD_QUARTER t "+
			   " set t.theme_bank_name = "+
			       " (select v.theme_bank_name from JOBS_STANDARD v where v.standardid = t.standardid and v.theme_bank_name is not null) "+
			 " where t.theme_bank_name is null";
			session.createSQLQuery(sql3).executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
