package cn.com.ite.hnjtamis.jobstandard.jobunionstandardEx;

import java.util.Date;

import org.hibernate.Session;

import cn.com.ite.eap2.common.utils.DateUtils;
import cn.com.ite.eap2.core.hibernate.HibernateDefaultDAOImpl;

/**
 * 岗位对应的标准管理
 * @author 朱健
 * @create time: 2016年3月7日 下午1:29:29
 * @version 1.0
 * 
 * @modified records:
 */
public class JobUnionStandardExDaoImpl extends HibernateDefaultDAOImpl implements JobUnionStandardExDao{
	
	
	/**
	 * 复制并插入新的标准
	 * @description
	 * @param sourceQid
	 * @param targetQid
	 * @modified
	 */
	public void insertAndCopyJobsStandardQuarter(String sourceQid,String targetQid){
		try{
			String instr = "insert into JOBS_STANDARD_QUARTER t "+
					  "(RELATION_ID, "+
					   "STANDARDID, "+
					   "QUARTER_TRAIN_CODE, "+
					   "QUARTER_TRAIN_NAME, "+
					   "ORDERNO, "+
					   "LAST_UPDATE_DATE, "+
					   "LAST_UPDATED_BY, "+
					   "CREATION_DATE, "+
					   "CREATED_BY, "+
					   "DEPT_NAME, "+
					   "DEPT_ID, "+
					   "SPECIALITY_NAME, "+
					   "SPECIALITY_CODE, "+
					   "DC_TYPE, "+
					   "QUARTER_ID, "+
					   "THEME_BANK_ID, "+
					   "THEME_BANK_NAME, "+
					   "THEME_BANK_CODE, "+
					   "PARENT_TRAIN_QUARTER_ID, "+
					   "PARENT_TRAIN_QUARTER_NAME, "+
					   "PARENT_TRAIN_QUARTER_CODE) "+
					  "(select to_char(SYSDATE, 'yyyymmddhh24miss') || REPLACE(TRIM(DBMS_RANDOM.VALUE), '.', '') as RELATION_ID, "+
					          "STANDARDID, "+
					          "q.QUARTER_CODE as QUARTER_TRAIN_CODE, "+
					          "q.QUARTER_NAME as QUARTER_TRAIN_NAME, "+
					          "j.ORDERNO + 100 as ORDERNO, "+
					          "'' as LAST_UPDATE_DATE, "+
					          "'' as LAST_UPDATED_BY, "+
					          "to_char(sysdate, 'yyyy-MM-dd hh24:mi:ss') as CREATION_DATE, "+
					          "'系统复制' as CREATED_BY, "+
					          "q.DEPT_NAME, "+
					          "q.DEPT_ID, "+
					          "j.SPECIALITY_NAME, "+
					          "j.SPECIALITY_CODE, "+
					          "q.DC_TYPE as DC_TYPE, "+
					          "q.QUARTER_ID as QUARTER_ID, "+
					          "j.THEME_BANK_ID, "+
					          "j.THEME_BANK_NAME, "+
					          "j.THEME_BANK_CODE, "+
					          "j.QUARTER_ID as PARENT_TRAIN_QUARTER_ID, "+
					          "j.QUARTER_TRAIN_NAME as PARENT_TRAIN_QUARTER_NAME, "+
					          "j.QUARTER_TRAIN_CODE as PARENT_TRAIN_QUARTER_CODE "+
					     "from (select * "+
					             "from JOBS_STANDARD_QUARTER jq "+
					            "where QUARTER_ID = '"+sourceQid+"' "+
					              "and jq.STANDARDID not in "+
					                  "(select qss.STANDARDID "+
					                     "from JOBS_STANDARD_QUARTER qss "+
					                    "where qss.QUARTER_ID = '"+targetQid+"')) j, "+
					          "(select * "+
					             "from QUARTER_STANDARD qs "+
					            "where qs.QUARTER_ID = '"+targetQid+"') q)";
			Session session=template.getSessionFactory().getCurrentSession();
			session.createSQLQuery(instr).executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}

	
	/**
	 * 删除标准源于父岗位不存在的
	 * @description
	 * @param sourceQid
	 * @param targetQid
	 * @modified
	 */
	public void deleteJobsStandardQuarterNotInParent(String sourceQid,String targetQid){
		try{
			String delStr = " delete JOBS_STANDARD_QUARTER jq "+
						 " where jq.QUARTER_ID = '"+targetQid+"' "+
						   " and jq.parent_train_quarter_id = '"+sourceQid+"' "+
						   " and jq.STANDARDID not in "+
						       " (select qss.STANDARDID "+
						          " from JOBS_STANDARD_QUARTER qss "+
						         " where qss.QUARTER_ID = '"+sourceQid+"')";
			Session session=template.getSessionFactory().getCurrentSession();
			session.createSQLQuery(delStr).executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 删除标准源于父岗位,并排除父岗位存在notDelParentQuarterIds里面的数据
	 * @description
	 * @param sourceQid
	 * @param targetQid
	 * @modified
	 */
	public void deleteJobsStandardQuarterHaveParentQuarter(String quarterId,String[] notDelParentQuarterIds){
		try{
			String delStr = " delete JOBS_STANDARD_QUARTER jq "+
					 " where jq.QUARTER_ID = '"+quarterId+"' "+
					   " and jq.parent_train_quarter_id is not null ";
			if(notDelParentQuarterIds!=null && notDelParentQuarterIds.length>0){
				String pids = "";
				for(int i=0;i<notDelParentQuarterIds.length;i++){
					if(notDelParentQuarterIds[i]!=null && !"".equals(notDelParentQuarterIds[i])){
						pids += "'"+notDelParentQuarterIds[i]+"',";
					}
				}
				if(pids.length()>0){
					pids = pids.substring(0,pids.length()-1);
					delStr += " and jq.parent_train_quarter_id not in ("+pids+") ";
				}
			}
			Session session=template.getSessionFactory().getCurrentSession();
			session.createSQLQuery(delStr).executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	
	
	/**
	 * 更新个人学习题库信息
	 * @description
	 * @param quarterId
	 * @param notDelParentQuarterIds
	 * @modified
	 */
	public void updatePersonalBankLearning(){
		try{
			Date  now1 = new Date();
			String delStr1 = "delete PERSONAL_BANK";
			String insertStr1 = "insert into PERSONAL_BANK "+
								  "(BANK_LEARNING_ID,EMPLOYEE_ID, EMPLOYEE_NAME, THEME_BANK_ID,THEME_NUM,CREATION_DATE ) "+
								  "(select to_char(SYSDATE, 'yyyymmddhh24miss') || REPLACE(TRIM(DBMS_RANDOM.VALUE), '.', '') as BANK_LEARNING_ID, "+
								          "e.EMPLOYEE_ID as EMPLOYEE_ID, "+
								          "e.employee_name as EMPLOYEE_NAME, "+
								          "sq.theme_bank_id as THEME_BANK_ID, "+
								          "0 as THEME_NUM,"+ 
								          "to_char(sysdate, 'yyyy-MM-dd hh24:mi:ss') as CREATION_DATE "+
								     "from employee e, quarter q, jobs_standard_quarter sq "+
								    "where e.quarter_id = q.quarter_id "+
								      "and q.quarter_train_code = sq.quarter_train_code "+
								      "and q.quarter_train_code is not null "+
								      "and sq.standardid is not null)";
			String updateStr1 = "update PERSONAL_BANK t "+
							   "set t.theme_num = "+
							       "(select THEMENUM "+
							          "from (select b.theme_bank_id, count(*) THEMENUM "+
							                  "from theme_in_bank b, theme v, theme_type tt "+
							                 "where b.theme_id = v.theme_id "+
							                   "and tt.theme_type_id = v.theme_type_id "+
							                   "and v.state = 15 "+
							                   "and v.IS_USE = 5 "+
							                   "and tt.theme_type in (5, 10, 25) "+
							                 "group by b.theme_bank_id) vv "+
							         "where vv.theme_bank_id = t.theme_bank_id)";
			
			
			String delStr2 = "delete PERSONAL_BANK_FIN t";
			String updateStr2 = "insert into PERSONAL_BANK_FIN "+
							  "(BANK_FIN_ID, EMPLOYEE_ID, THEME_BANK_ID, THEME_FIN_NUM,CREATION_DATE) "+
							  "(select to_char(SYSDATE, 'yyyymmddhh24miss') || REPLACE(TRIM(DBMS_RANDOM.VALUE), '.', '') as BANK_FIN_ID, "+
							          "EMPLOYEE_ID, "+
							          "theme_bank_id, "+
							          "count(*) THEME_FIN_NUM,"+ 
								      "to_char(sysdate, 'yyyy-MM-dd hh24:mi:ss') as CREATION_DATE "+
							     "from (select b.theme_bank_id, b.theme_id, t.employee_id "+
							             /*"from (select ett.theme_id, etp.employee_id "+
							                     "from exam_testpaper_theme ett, "+
							                         " (select eut.testpaper_id, eut.employee_id "+
							                             "from exam_user_testpaper eut "+
							                            "where eut.relation_type like 'mo%' "+
							                              "and eut.relation_type is not null "+
							                             "and eut.sub_time is not null) etp "+
							                   " where ett.exam_testpaper_id = etp.testpaper_id "+
							                      "and ett.theme_type_id in "+
							                         " (select tty.theme_type_id "+
							                             "from theme_type tty "+
							                            "where tty.theme_type in (5, 10, 25))) t, "+*/
							                             
										 "from (select t.theme_id, t.created_id_by as employee_id "+
										         "from study_user_answerkey t "+
										        "where t.answerkey_value <> 'null' "+
										          "and t.answerkey_value is not null "+
										          "and t.relation_type like 'mo%' "+
										          "and t.relation_type is not null "+ 
										          "and t.theme_type_id in "+
							                         " (select tty.theme_type_id "+
							                             "from theme_type tty "+
							                            "where tty.theme_type in (5, 10, 25)) "+
										       "union all "+
										       "select t.theme_id, t.created_id_by as employee_id "+
										         "from study_user_answerkey_his t "+
										        "where t.answerkey_value <> 'null' "+
										          "and t.answerkey_value is not null "+
										          "and t.sub_time is not null "+
										          "and t.theme_type_id in "+
							                         " (select tty.theme_type_id "+
							                             "from theme_type tty "+
							                            "where tty.theme_type in (5, 10, 25))) t, "+    
							                 " (select te.theme_id "+
							                    " from theme te "+
							                    "where te.type in (20, 40) "+
							                      "and te.state = 15 "+
							                      "and te.IS_USE = 5 "+
							                      "and te.theme_type_id in "+
							                          "(select tty.theme_type_id "+
							                             "from theme_type tty "+
							                            "where tty.theme_type in (5, 10, 25))) v, "+
							                 " theme_in_bank b "+
							            "where v.theme_id = t.theme_id "+
							             " and v.theme_id = b.theme_id "+
							            "group by b.theme_bank_id, b.theme_id, t.theme_id, t.employee_id) "+
							    "group by theme_bank_id, employee_id)";
			
			
			String updateStr3 = "update PERSONAL_BANK t set t.theme_num = 0 where t.theme_num is null";

			String updateStr4 = "update PERSONAL_BANK_FIN t set t.theme_fin_num = 0 where t.theme_fin_num is null";
			
			Session session=template.getSessionFactory().getCurrentSession();
			session.createSQLQuery(delStr1).executeUpdate();
			session.createSQLQuery(insertStr1).executeUpdate();
			session.createSQLQuery(updateStr1).executeUpdate();
			session.createSQLQuery(delStr2).executeUpdate();
			session.createSQLQuery(updateStr2).executeUpdate();
			session.createSQLQuery(updateStr3).executeUpdate();
			session.createSQLQuery(updateStr4).executeUpdate();
			
			
			Date  now2 = new Date();
			System.out.println("["+DateUtils.convertDateToStr(new Date(),"yyyy-MM-dd HH:mm:ss")+"][更新个人学习题库信息][处理时间："+(now2.getTime()-now1.getTime())/1000+"秒]");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
