package cn.com.ite.hnjtamis.exam.study;

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

import cn.com.ite.eap2.common.utils.DateUtils;
import cn.com.ite.hnjtamis.core.hibernate.HibernateDefaultExDAOImpl;
import cn.com.ite.hnjtamis.exam.hibernatemap.StudyTestpaperAnswerkey;
import cn.com.ite.hnjtamis.exam.hibernatemap.StudyTestpaperTheme;
import cn.com.ite.hnjtamis.exam.hibernatemap.StudyUserAnswerkey;

/**
 *
 * <p>Title cn.com.ite.hnjtamis.exam.study.StudyThemeDaoImpl</p>
 * <p>Description 在线学习试题</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2017</p>
 * @create time: 2017年9月1日 下午2:18:36
 * @version 1.0
 * 
 * @modified records:
 */
public class StudyThemeDaoImpl extends HibernateDefaultExDAOImpl implements StudyThemeDao{
	
	/**
	 * 查询试卷的试题数、总分、考生正确题数、正确得分
	 * @description
	 * @param studyTestpaperId
	 * @return
	 * @modified
	 */
	public Object[] getStudyTestpaperThemeCount(String studyTestpaperId,String employeeId){
		Session session=template.getSessionFactory().getCurrentSession();
		Integer count = new Integer(0);
		Double fraction = new Double(0.0d); 
		Integer usercount = new Integer(0);
		Double userfraction = new Double(0.0d); 
		try{
			String sql = "select count(*) sumvalue,sum(t.default_score) default_score from STUDY_TESTPAPER_THEME t "+
					 "where t.STUDY_TESTPAPER_ID = ? ";
			SQLQuery sqlQuery=session.createSQLQuery(sql); 
			sqlQuery.setString(0, studyTestpaperId);
			List<Object[]> tlist = sqlQuery.addScalar("sumvalue",IntegerType.INSTANCE)
					.addScalar("default_score",DoubleType.INSTANCE).list();
			if(tlist!=null && tlist.size()>0){
				Object[] value = tlist.get(0);
				count = (Integer)value[0];
				fraction = (Double)value[1];
			}
			sql = "select count(*) sumvalue,sum(t.score) default_score from STUDY_USER_ANSWERKEY t where t.score > 0  "
					+ " and t.STUDY_TESTPAPER_ID = ? and t.created_id_by = ?";
			sqlQuery=session.createSQLQuery(sql); 
			sqlQuery.setString(0, studyTestpaperId);
			sqlQuery.setString(1, employeeId);
			tlist = sqlQuery.addScalar("sumvalue",IntegerType.INSTANCE)
					.addScalar("default_score",DoubleType.INSTANCE).list();
			if(tlist!=null && tlist.size()>0){
				Object[] value = tlist.get(0);
				usercount = (Integer)value[0];
				userfraction = (Double)value[1];
				
			}
			count = count==null?new Integer(0):count;
			fraction = fraction==null?new Double(0.0d):fraction;
			usercount = usercount==null?new Integer(0):usercount;
			userfraction = userfraction==null?new Double(0.0d):userfraction;
		}catch(Exception e){
			e.printStackTrace();
		}
		return new Object[]{count,fraction,usercount,userfraction};
	}
	
	/**
	 * 查询试卷的试题
	 * @description
	 * @param studyTestpaperId
	 * @return
	 * @modified
	 */
	public List<StudyTestpaperTheme> getStudyTestpaperThemeList(String studyTestpaperId,Integer startIndex,Integer endIndex){
		List<StudyTestpaperTheme> list = new ArrayList<StudyTestpaperTheme>();
		Session session=template.getSessionFactory().getCurrentSession();
		try{
			String sql = "select t.study_testpaper_theme_id,"
					+ "t.theme_type_id,"
					+ "t.score_Type,"
					+ "t.default_Score,"
					+ "t.eachline,"
					+ "t.explain,"
					+ "t.theme_Name from STUDY_TESTPAPER_THEME t  inner join theme_type t2 on t.theme_type_id=t2.theme_type_id  "+
					 "where t.STUDY_TESTPAPER_ID = ? "+
					 "order by t2.sort_num,t.CREATION_DATE desc, "+
					          "t.SORT_NUM, "+
					          "t.STUDY_TESTPAPER_THEME_ID";
			if(endIndex!=null){
				sql =" select tt.* from ("+sql+") tt where rownum<="+endIndex.intValue();
			}
			SQLQuery sqlQuery=session.createSQLQuery(sql); 
			sqlQuery.setString(0, studyTestpaperId);
			List<Object[]> tlist = sqlQuery.addScalar("study_testpaper_theme_id",StringType.INSTANCE)
					.addScalar("theme_type_id",StringType.INSTANCE)
					.addScalar("score_Type",StringType.INSTANCE)
					.addScalar("default_Score",DoubleType.INSTANCE)
					.addScalar("eachline",IntegerType.INSTANCE)
					.addScalar("explain",StringType.INSTANCE)
					.addScalar("theme_Name",StringType.INSTANCE).list();
			for(int i=startIndex==null?0:startIndex.intValue()-1;i<tlist.size();i++){
				try{
					Object[] obj = tlist.get(i);
					StudyTestpaperTheme studyTestpaperTheme = new StudyTestpaperTheme();
					String studyTestpaperThemeId = (String)obj[0];
					String themeTypeId = (String)obj[1];
					String scoreType = (String)obj[2];
					Double defaultScore = (Double)obj[3];
					Integer eachline = (Integer)obj[4];
					String explain = (String)obj[5];
					String themeName = (String)obj[6];
					
					studyTestpaperTheme.setStudyTestpaperThemeId(studyTestpaperThemeId);
					studyTestpaperTheme.setThemeTypeId(themeTypeId);
					studyTestpaperTheme.setScoreType(scoreType);
					studyTestpaperTheme.setDefaultScore(defaultScore);
					studyTestpaperTheme.setEachline(eachline);
					studyTestpaperTheme.setExplain(explain);
					studyTestpaperTheme.setThemeName(themeName);
					list.add(studyTestpaperTheme);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 查询试卷的试题答案
	 * @description
	 * @param studyTestpaperId
	 * @return
	 * @modified
	 */
	public List<StudyTestpaperAnswerkey> getStudyTestpaperAnswerkeyList(String studyTestpaperId){
		List<StudyTestpaperAnswerkey> list = new ArrayList<StudyTestpaperAnswerkey>();
		Session session=template.getSessionFactory().getCurrentSession();
		try{
			String sql = "select t.answerkey_value,t.is_right,t.study_testpaper_theme_id "+
				  "from STUDY_TESTPAPER_ANSWERKEY t "+
				 "where t.STUDY_TESTPAPER_ID = ? "+
				 "order by t.SORT_NUM, t.THEME_ID";
			SQLQuery sqlQuery=session.createSQLQuery(sql); 
			sqlQuery.setString(0, studyTestpaperId);
			List<Object[]> tlist = sqlQuery.addScalar("answerkey_value",StringType.INSTANCE)
					.addScalar("is_right",IntegerType.INSTANCE)
					.addScalar("study_testpaper_theme_id",StringType.INSTANCE).list();
			for(int i=0;i<tlist.size();i++){
				try{
					Object[] obj = tlist.get(i);
					StudyTestpaperAnswerkey studyTestpaperAnswerkey = new StudyTestpaperAnswerkey();
					String answerkey_value = (String)obj[0];
					Integer is_right = (Integer)obj[1];
					String study_testpaper_theme_id = (String)obj[2];
					studyTestpaperAnswerkey.setAnswerkeyValue(answerkey_value);
					studyTestpaperAnswerkey.setIsRight(is_right);
					
					StudyTestpaperTheme studyTestpaperTheme = new StudyTestpaperTheme();
					studyTestpaperTheme.setStudyTestpaperThemeId(study_testpaper_theme_id);
					studyTestpaperAnswerkey.setStudyTestpaperTheme(studyTestpaperTheme);
					list.add(studyTestpaperAnswerkey);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}
	
	
	/**
	 * 查询考生答案
	 * @description
	 * @param studyTestpaperId
	 * @param employeeId
	 * @return
	 * @modified
	 */
	public List<StudyUserAnswerkey> getUserAnswerkeyList(String studyTestpaperId,String employeeId){
		List<StudyUserAnswerkey> list = new ArrayList<StudyUserAnswerkey>();
		Session session=template.getSessionFactory().getCurrentSession();
		try{
			String sql = "select a.study_testpaper_theme_id,a.answerkey_value,a.score,a.creation_date "+
				  "from Study_User_Answerkey a where  "+
				" a.study_Testpaper_Id = ? "+
				" and a.created_Id_By = ? "+
				" order by a.study_Testpaper_Id, a.study_Testpaper_Theme_Id,a.sort_Num";
			SQLQuery sqlQuery=session.createSQLQuery(sql); 
			sqlQuery.setString(0, studyTestpaperId);
			sqlQuery.setString(1, employeeId);
			List<Object[]> tlist = sqlQuery.addScalar("study_testpaper_theme_id",StringType.INSTANCE)
					.addScalar("answerkey_value",StringType.INSTANCE)
					.addScalar("score",DoubleType.INSTANCE)
					.addScalar("creation_date",StringType.INSTANCE).list();
			for(int i=0;i<tlist.size();i++){
				try{
					Object[] obj = tlist.get(i);
					StudyUserAnswerkey studyUserAnswerkey = new StudyUserAnswerkey();
					String study_testpaper_theme_id = (String)obj[0];
					String answerkey_value = (String)obj[1];
					Double score = (Double)obj[2];
					String creation_date = (String)obj[3];
					
					studyUserAnswerkey.setStudyTestpaperThemeId(study_testpaper_theme_id);
					studyUserAnswerkey.setAnswerkeyValue(answerkey_value);
					studyUserAnswerkey.setScore(score);
					studyUserAnswerkey.setCreationDate(creation_date);
					
					list.add(studyUserAnswerkey);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 添加数据库中没有题库的试卷
	 * @description
	 * @param relationType
	 * @param employeeId
	 * @param employeeName
	 * @modified
	 */
	public void addStudyTestpaper(String relationType,
			String employeeId,String employeeName){
		Session session=template.getSessionFactory().getCurrentSession();
		try{
			System.out.println("["+DateUtils.convertDateToStr(new Date(),"yyyy-MM-dd HH:mm:ss")+"][随堂练习][添加试卷]开始");
			String sql = "insert into STUDY_TESTPAPER "+
						  "(study_testpaper_id, "+
						   "study_testpaper_name, "+
						   "total_theme, "+
						   "total_score, "+
						   "is_use, "+
						   "remark, "+
						   "state, "+
						   "organ_name, "+
						   "organ_id, "+
						   "sync_flag, "+
						   "last_update_date, "+
						   "last_updated_by, "+
						   "last_updated_id_by, "+
						   "creation_date, "+
						   "created_by, "+
						   "created_id_by, "+
						   "relation_id, "+
						   "relation_type) "+
						  "(select to_char(SYSDATE, 'yyyymmddhh24miss') || "+
						          "REPLACE(TRIM(DBMS_RANDOM.VALUE), '.', '') as study_testpaper_id, "+
						          "b.theme_bank_name as study_testpaper_name, "+
						          "0 as total_theme, "+
						          "0 as total_score, "+
						          "'10' as is_use, "+
						          "'' as remark, "+
						          "'5' as state, "+
						          "'' as organ_name, "+
						          "'' as organ_id, "+
						          "'' as sync_flag, "+
						          "'' as last_update_date, "+
						          "'' as last_updated_by, "+
						          "'' as last_updated_id_by, "+
						          "to_char(sysdate, 'yyyy-MM-dd hh24:mi:ss') as creation_date, "+
						          "'"+employeeName+"' as created_by, "+
						          "'"+employeeId+"' as created_id_by, "+
						          "b.theme_bank_id as relation_id, "+
						          "'"+relationType+"' as relation_type "+
						     "from theme_bank b "+
						    "where b.theme_bank_id not in "+
						          "(select tt.relation_id "+
						             "from STUDY_TESTPAPER tt "+
						           " where tt.relation_type = '"+relationType+"') "+
						      "and b.IS_L = '10')";
			session.createSQLQuery(sql).executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		try{
			String sql = "update STUDY_TESTPAPER t set t.total_theme "+
						" = (select count(*)from Study_Testpaper_Theme tt where tt.study_testpaper_id = t.study_testpaper_id), "+
						" t.total_score "+
						" = (select sum(tt.default_score)from Study_Testpaper_Theme tt where tt.study_testpaper_id = t.study_testpaper_id) ";
			session.createSQLQuery(sql).executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		String sql = "select t.study_testpaper_id,t.relation_Id,t.relation_type from STUDY_TESTPAPER t";
		SQLQuery sqlQuery=session.createSQLQuery(sql); 
		List<Object[]> list = sqlQuery.addScalar("study_testpaper_id", StringType.INSTANCE)
				.addScalar("relation_Id", StringType.INSTANCE)
				.addScalar("relation_type", StringType.INSTANCE).list();
		for(int i=0;i<list.size();i++){
			try{
				Object[] obj = list.get(i);
				String study_testpaper_id = (String)obj[0];
				String _relationId = (String)obj[1];
				String _relationType = (String)obj[2];
				this.addThemeInStudyTestpaper(study_testpaper_id, _relationId, _relationType, employeeId, employeeName);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 获取原试题的人员信息
	 * @description
	 * @return
	 * @modified
	 */
	public List<String> getEmployeeIdInExamUserTestpaper(String relation_type){
		Session session=template.getSessionFactory().getCurrentSession();
		String sql = "select t.employee_id  from EXAM_USER_TESTPAPER t "+
				 "  where t.relation_type = '"+relation_type+"'  "
				 		+ "and t.testpaper_id in  (select tt.exam_testpaper_id "+
				          "from EXAM_USER_ANSWERKEY ant, EXAM_TESTPAPER_THEME tt "+
				        " where ant.exam_testpaper_theme_id = tt.exam_testpaper_theme_id "+
				          " and dbms_lob.instr(ant.answerkey_value, 'null', 1, 1) = 0 "+
				        " group by tt.exam_testpaper_id) "+
				         "group by t.employee_id order by t.employee_id  ";
		SQLQuery sqlQuery=session.createSQLQuery(sql); 
		List<String> list = sqlQuery.addScalar("employee_id", StringType.INSTANCE).list();
		return list;
	}
	/**
	 * 根据旧的数据
	 * @description
	 * @modified
	 */
	public void updateUserAnsInOld(String employeeId,String relationType){
		Session session=template.getSessionFactory().getCurrentSession();
		try{
			System.out.println("["+DateUtils.convertDateToStr(new Date(),"yyyy-MM-dd HH:mm:ss")+"][随堂练习][更新旧的考生答案]开始");
			String sql11 = "select count(*) as ansnum from STUDY_USER_ANSWERKEY t where t.created_id_by = '"+employeeId+"'"
					+ "and t.relation_type = '"+relationType+"'";
			SQLQuery sqlQuery=session.createSQLQuery(sql11); 
			List<Integer> list1 = sqlQuery.addScalar("ansnum", IntegerType.INSTANCE).list();
			int ansnum = 0;
			if(list1!=null && list1.size()>0){
				ansnum = list1.get(0).intValue();
			}
			if(ansnum == 0){
				String sql1 = "select t.study_testpaper_id,t.relation_id from STUDY_TESTPAPER t  where t.relation_type = '"+relationType+"' ";
				sqlQuery=session.createSQLQuery(sql1); 
				List<Object[]> list = sqlQuery.addScalar("study_testpaper_id", StringType.INSTANCE)
						.addScalar("relation_id", StringType.INSTANCE).list();
				Map<String,String> studyTestpaperIdRelationMap = new HashMap<String,String>();
				for(int i=0;i<list.size();i++){
					Object[] obj = list.get(i);
					String study_testpaper_id = (String)obj[0];
					String relation_id = (String)obj[1];
					studyTestpaperIdRelationMap.put(relation_id, study_testpaper_id);
				}
				
				
				String sql = "select t.employee_id,t.testpaper_id as exam_testpaper_id,t.relation_id,t.relation_type,"
						+ " t.creation_date  from EXAM_USER_TESTPAPER t "+
						 "  where t.relation_type = '"+relationType+"' and t.employee_id ='"+employeeId+"' "
						 		+ "order by t.employee_id,t.relation_id, t.creation_date desc ";
				sqlQuery=session.createSQLQuery(sql); 
				list = sqlQuery.addScalar("employee_id", StringType.INSTANCE)
						.addScalar("exam_testpaper_id", StringType.INSTANCE)
						.addScalar("relation_id", StringType.INSTANCE)
						.addScalar("relation_type", StringType.INSTANCE)
						.addScalar("creation_date", StringType.INSTANCE).list();
				Map<String,String> ansValue = new HashMap<String,String>();//是否已写一次答案
				for(int i=0;i<list.size();i++){
					try{
						Object[] obj = list.get(i);
						String employee_id = (String)obj[0];
						String exam_testpaper_id = (String)obj[1];
						String relation_id = (String)obj[2];
						String relation_type = (String)obj[3];
						String creation_date = (String)obj[4];
						String study_testpaper_id = studyTestpaperIdRelationMap.get(relation_id);
						
						if(ansValue.get(relation_id+"_"+employee_id)==null){
							this.updateOneTestpaperAnsInOld(study_testpaper_id, exam_testpaper_id, employee_id, relation_id, relation_type, creation_date,i);
						}else{
							this.updateOneTestpaperAnsHisInOld(study_testpaper_id, exam_testpaper_id, employee_id, relation_id, relation_type, creation_date,i);
							this.deleteMoreHis(study_testpaper_id, employee_id);
						}
						ansValue.put(relation_id+"_"+employee_id,"a");
					}catch(Exception e){
						e.printStackTrace();
					}
				}
				
			}
			System.out.println("["+DateUtils.convertDateToStr(new Date(),"yyyy-MM-dd HH:mm:ss")+"][随堂练习][更新旧的考生答案]结束");
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	private void updateOneTestpaperAnsInOld(String study_testpaper_id,String exam_testpaper_id,String employee_id,
			String relation_id,String relation_type,String creation_date,int index){
		Session session=template.getSessionFactory().getCurrentSession();
		try{
			String batchNo = creation_date.replaceAll(" ", "").replaceAll("-", "").replaceAll(":", "")+index;
			if(batchNo.length()==16){
				batchNo+="0";
			}else if(batchNo.length()==15){
				batchNo+="00";
			}else if(batchNo.length()==14){
				batchNo+="000";
			}else if(batchNo.length()==13){
				batchNo+="0000";
			}else if(batchNo.length()==12){
				batchNo+="00000";
			}
			String sql = "insert into STUDY_USER_ANSWERKEY "+
						  "(user_answerkey_id, "+
						   "study_testpaper_theme_id, "+
						   "study_testpaper_id, "+
						   "theme_id, "+
						   "exam_answerkey, "+
						   "answerkey_value, "+
						   "sort_num, "+
						   "score, "+
						   "answerkey_id, "+
						   "organ_name, "+
						   "organ_id, "+
						   "sync_flag, "+
						   "state, "+
						   "in_time, "+
						   "out_time, "+
						   "sub_time, "+
						   "last_update_date, "+
						   "last_updated_by, "+
						   "last_updated_id_by, "+
						   "creation_date, "+
						   "created_by, "+
						   "created_id_by, "+
						   "batch_no, "+
						   "relation_id, "+
						   "relation_type, "+
						   "theme_type_id, "+
						   "theme_type_name) "+
						  "(select ant.user_answerkey_id as user_answerkey_id, "+
						          "'' as study_testpaper_theme_id, "+
						          "'"+study_testpaper_id+"' as study_testpaper_id, "+
						          "tt.theme_id as theme_id, "+
						          "ant.exam_answerkey as exam_answerkey, "+
						          "ant.answerkey_value as answerkey_value, "+
						          "ant.sort_num as sort_num, "+
						          "tt.score as score, "+
						          "'' as answerkey_id, "+
						          "ant.organ_name as organ_name, "+
						          "ant.organ_id as organ_id, "+
						          "ant.sync_flag as sync_flag, "+
						          "ant.state as state, "+
						          "'' as in_time, "+
						          "'' as out_time, "+
						          "ant.creation_date as sub_time, "+
						          "'' as last_update_date, "+
						          "'' as last_updated_by, "+
						          "'' as last_updated_id_by, "+
						          "tt.creation_date as creation_date, "+
						          "tt.created_by as created_by, "+
						          "'"+employee_id+"' as created_id_by, "+
						          "'"+batchNo+"' as batch_no, "+
						          "'"+relation_id+"' as relation_id, "+
						          "'"+relation_type+"' as relation_type, "+
						          "tt.theme_type_id as theme_type_id, "+
						          "tt.theme_type_name as theme_type_name "+
						     "from EXAM_USER_ANSWERKEY ant, EXAM_TESTPAPER_THEME tt "+
						    "where ant.exam_testpaper_theme_id = tt.exam_testpaper_theme_id "+
						      " and  dbms_lob.instr( ant.answerkey_value, 'null', 1, 1) = 0 and tt.exam_testpaper_id = '"+exam_testpaper_id+"')";
			session.createSQLQuery(sql).executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
		}
		try{
			String sql2 = " update STUDY_USER_ANSWERKEY t set t.study_testpaper_theme_id =( "+
					"  select tt.study_testpaper_theme_id from study_testpaper_theme tt where  "+
					" tt.theme_id = t.theme_id and tt.study_testpaper_id = '"+study_testpaper_id+"') "+
					" where t.study_testpaper_id = '"+study_testpaper_id+"' and t.study_testpaper_theme_id is null";
					session.createSQLQuery(sql2).executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private void updateOneTestpaperAnsHisInOld(String study_testpaper_id,String exam_testpaper_id,String employee_id,
			String relation_id,String relation_type,String creation_date,int index){
		Session session=template.getSessionFactory().getCurrentSession();
		try{
			String batchNo = creation_date.replaceAll(" ", "").replaceAll("-", "").replaceAll(":", "")+index;
			if(batchNo.length()==16){
				batchNo+="0";
			}else if(batchNo.length()==15){
				batchNo+="00";
			}else if(batchNo.length()==14){
				batchNo+="000";
			}else if(batchNo.length()==13){
				batchNo+="0000";
			}else if(batchNo.length()==12){
				batchNo+="00000";
			}
			String sql = "insert into STUDY_USER_ANSWERKEY_HIS "+
						  "(user_answerkey_id, "+
						   "study_testpaper_theme_id, "+
						   "study_testpaper_id, "+
						   "theme_id, "+
						   "exam_answerkey, "+
						   "answerkey_value, "+
						   "sort_num, "+
						   "score, "+
						   "answerkey_id, "+
						   "organ_name, "+
						   "organ_id, "+
						   "sync_flag, "+
						   "in_time, "+
						   "out_time, "+
						   "sub_time, "+
						   "last_update_date, "+
						   "last_updated_by, "+
						   "last_updated_id_by, "+
						   "creation_date, "+
						   "created_by, "+
						   "created_id_by, "+
						   "batch_no, "+
						   "relation_id, "+
						   "relation_type, "+
						   "theme_type_id, "+
						   "theme_type_name) "+
						  "(select ant.user_answerkey_id as user_answerkey_id, "+
						          "'' as study_testpaper_theme_id, "+
						          "'"+study_testpaper_id+"' as study_testpaper_id, "+
						          "tt.theme_id as theme_id, "+
						          "ant.exam_answerkey as exam_answerkey, "+
						          "ant.answerkey_value as answerkey_value, "+
						          "ant.sort_num as sort_num, "+
						          "tt.score as score, "+
						          "'' as answerkey_id, "+
						          "ant.organ_name as organ_name, "+
						          "ant.organ_id as organ_id, "+
						          "ant.sync_flag as sync_flag, "+
						          "'' as in_time, "+
						          "'' as out_time, "+
						          "ant.creation_date as sub_time, "+
						          "'' as last_update_date, "+
						          "'' as last_updated_by, "+
						          "'' as last_updated_id_by, "+
						          "tt.creation_date as creation_date, "+
						          "tt.created_by as created_by, "+
						          "'"+employee_id+"' as created_id_by, "+
						          "'"+batchNo+"' as batch_no, "+
						          "'"+relation_id+"' as relation_id, "+
						          "'"+relation_type+"' as relation_type, "+
						          "tt.theme_type_id as theme_type_id, "+
						          "tt.theme_type_name as theme_type_name "+
						     "from EXAM_USER_ANSWERKEY ant, EXAM_TESTPAPER_THEME tt "+
						    "where ant.exam_testpaper_theme_id = tt.exam_testpaper_theme_id "+
						      " and  dbms_lob.instr( ant.answerkey_value, 'null', 1, 1) = 0   and tt.exam_testpaper_id = '"+exam_testpaper_id+"')";
			session.createSQLQuery(sql).executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
		}
		try{
			String sql2 = " update STUDY_USER_ANSWERKEY t set t.study_testpaper_theme_id =( "+
					"  select tt.study_testpaper_theme_id from study_testpaper_theme tt where  "+
					" tt.theme_id = t.theme_id and tt.study_testpaper_id = '"+study_testpaper_id+"') "+
					" where t.study_testpaper_id = '"+study_testpaper_id+"' and t.study_testpaper_theme_id is null";
					session.createSQLQuery(sql2).executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * 试卷添加试题
	 * @description
	 * @param relationId
	 * @param relationType
	 * @param employeeId
	 * @param employeeName
	 * @throws Exception
	 * @modified
	 */
	public void addThemeInStudyTestpaper(String study_testpaper_id,String relationId,String relationType,
			String employeeId,String employeeName)throws Exception{
		Session session=template.getSessionFactory().getCurrentSession();
		try{	
			System.out.println("["+DateUtils.convertDateToStr(new Date(),"yyyy-MM-dd HH:mm:ss")+"][随堂练习][添加试题] 题库ID =  "+relationId +" ... 开始 ...");
			String sql0 = "insert into STUDY_TESTPAPER_THEME "+
							  "(study_testpaper_theme_id, "+ 
							  "study_testpaper_id,"+
							   "theme_id, "+
							   "theme_type_id, "+
							   "theme_type_name, "+
							   "theme_name, "+
							   "knowledge_point, "+
							   "degree, "+
							   "eachline, "+
							   "explain, "+
							   "default_score, "+
							   "score_type, "+
							   "sort_num, "+
							   "remark, "+
							   "state, "+
							   "is_use, "+
							   "sync_flag, "+
							   "last_update_date, "+
							   "last_updated_by, "+
							   "last_updated_id_by, "+
							   "creation_date, "+
							   "created_by, "+
							   "created_id_by, "+
							   "relation_id, "+
							   "relation_type) "+
							  "(select to_char(SYSDATE, 'yyyymmddhh24miss') || REPLACE(TRIM(DBMS_RANDOM.VALUE), '.', '') as study_testpaper_theme_id, "+
							          "'"+study_testpaper_id+"' as study_testpaper_id,"
							          + "t.theme_id, "+
							          "t.theme_type_id, "+
							          "t.theme_type_name, "+
							          "t.theme_name, "+
							          "t.knowledge_point, "+
							          "t.degree, "+
							          "t.eachline, "+
							          "t.explain, "+
							          "t.default_score, "+
							          "t.score_type, "+
							          "t.sort_num, "+
							          "t.remark, "+
							          "5 as state, "+
							          "'5' as is_use, "+
							          "'-1' as sync_flag, "+
							          "'' as last_update_date, "+
							          "'' as last_updated_by, "+
							          "'' as last_updated_id_by, "+
							          "t.creation_date as creation_date, "+
							          "'"+employeeName+"' as created_by, "+
							          "'"+employeeId+"' as created_id_by, "+
							          "'"+relationId+"' as relation_id, "+
							          "'"+relationType+"' as relation_type "+
							     "from theme t,(select tt.theme_type_id from THEME_TYPE tt where tt.is_use = '10' and tt.theme_type in('5','10','25')) ttt "+
							    "where exists (select 1 "+
							             "from theme_in_bank tb "+
							            "where tb.theme_id = t.theme_id "+
							              "and tb.theme_bank_id = '"+relationId+"') "+
							      "and not exists (select 1 "+
							             "from STUDY_TESTPAPER_THEME stt "+
							            "where stt.study_testpaper_id = '"+study_testpaper_id+"'  "+
							              "and t.theme_id = stt.theme_id ) "+ 
							           "and t.theme_type_id = ttt.theme_type_id "+
							      "and t.is_use = 5 and t.type in('20','40') "+
							      "and t.state = '15' "+
							   ")";
				session.createSQLQuery(sql0).executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
		}
		try{
				String sql1 = "insert into STUDY_TESTPAPER_ANSWERKEY "+
						  "(answerkey_id, "+
						   "study_testpaper_theme_id, "+
						   "theme_id, "+
						   "study_testpaper_id, "+
						   "answerkey_value, "+
						   "is_right, "+
						   "sort_num, "+
						   "remark, "+
						   "theme_type_id, "+
						   "theme_type_name, "+
						   "organ_name, "+
						   "organ_id, "+
						   "sync_flag, "+
						   "last_update_date, "+
						   "last_updated_by, "+
						   "last_updated_id_by, "+
						   "creation_date, "+
						   "created_by, "+
						   "created_id_by, "+
						   "relation_id, "+
						   "relation_type) "+
						  "(select to_char(SYSDATE, 'yyyymmddhh24miss') ||  REPLACE(TRIM(DBMS_RANDOM.VALUE), '.', '') as answerkey_id, "+
						          "st.study_testpaper_theme_id, "+
						          "ta.theme_id, "+
						          "st.study_testpaper_id, "+
						          "ta.answerkey_value, "+
						          "ta.is_right, "+
						          "ta.sort_num, "+
						          "ta.remark, "+
						          "ta.theme_type_id, "+
						          "ta.theme_type_name, "+
						          "'' as organ_name, "+
						          "'' as organ_id, "+
						          "'0' as sync_flag, "+
						          "'' as last_update_date, "+
						          "'' as last_updated_by, "+
						          "'' as last_updated_id_by, "+
						          "st.creation_date as creation_date, "+
						          "st.created_by as created_by, "+
						          "st.created_id_by as created_id_by, "+
						          "st.relation_id as relation_id, "+
						          "st.relation_type as relation_type "+
						     "from theme_answerkey ta, study_testpaper_theme st "+
						    "where ta.theme_id = st.theme_id "+
						      "and st.sync_flag = -1 "+
						      "and st.study_testpaper_id = '"+study_testpaper_id+"' )";
				session.createSQLQuery(sql1).executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
		}
		try{		
			String sql2 = "update study_testpaper_theme st  set st.sync_flag = '0' "+
					" where st.study_testpaper_id = '"+study_testpaper_id+"'  and st.sync_flag = '-1' ";
			session.createSQLQuery(sql2).executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
		}
		System.out.println("["+DateUtils.convertDateToStr(new Date(),"yyyy-MM-dd HH:mm:ss")+"][随堂练习][添加试题] 题库ID =  "+relationId +" ... 结束 ...");
		
	}

	
	/**
	 * 根据试题Id获取答案
	 * @description
	 * @param themeIds
	 * @return
	 * @modified
	 */
	public Map<String,List<StudyTestpaperAnswerkey>> getAnswerkeyByStThemeIds(String studyTestpaperThemeIds){
		Map<String,List<StudyTestpaperAnswerkey>> ansMap = new HashMap<String,List<StudyTestpaperAnswerkey>>();
		try{
			String hql="from  StudyTestpaperAnswerkey a where   a.studyTestpaperTheme.studyTestpaperThemeId in ("+studyTestpaperThemeIds+") "
					+ " order by a.studyTestpaperTheme.studyTestpaperThemeId,a.sortNum";
			List<StudyTestpaperAnswerkey> ansList = this.queryHql(hql);
			if(ansList!=null){
				for(int i=0;i<ansList.size();i++){
					StudyTestpaperAnswerkey studyTestpaperAnswerkey = (StudyTestpaperAnswerkey)ansList.get(i);
					String ansKey = studyTestpaperAnswerkey.getStudyTestpaperTheme().getStudyTestpaperThemeId();
					List<StudyTestpaperAnswerkey> tmplist = ansMap.get(ansKey);
					if(tmplist==null)tmplist = new ArrayList<StudyTestpaperAnswerkey>();
					tmplist.add(studyTestpaperAnswerkey);
					ansMap.put(ansKey,tmplist);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return ansMap;
	}
	
	
	
	/**
	 * 更新到答案历史表，清理答案表的内容
	 * @description
	 * @param studyTestpaperId
	 * @param employeeId
	 * @modified
	 */
	public void updateAnsHis(String studyTestpaperId,String employeeId){
		Session session=template.getSessionFactory().getCurrentSession();
		try{	
			String sql0 = "insert into STUDY_USER_ANSWERKEY_HIS "+
					  "(user_answerkey_id, "+
					   "study_testpaper_theme_id, "+
					   "study_testpaper_id, "+
					   "theme_id, "+
					   "exam_answerkey, "+
					   "answerkey_value, "+
					   "sort_num, "+
					   "score, "+
					   "answerkey_id, "+
					   "organ_name, "+
					   "organ_id, "+
					   "sync_flag, "+
					   "in_time, "+
					   "out_time, "+
					   "sub_time, "+
					   "last_update_date, "+
					   "last_updated_by, "+
					   "last_updated_id_by, "+
					   "creation_date, "+
					   "created_by, "+
					   "created_id_by, "+
					   "batch_no, "+
					   "relation_id, "+
					   "relation_type," + 
					   "THEME_TYPE_ID,"+ 
					  "THEME_TYPE_NAME) "+
					  "(select ut.user_answerkey_id, "+
					          "ut.study_testpaper_theme_id, "+
					          "ut.study_testpaper_id, "+
					          "ut.theme_id, "+
					          "ut.exam_answerkey, "+
					          "ut.answerkey_value, "+
					          "ut.sort_num, "+
					          "ut.score, "+
					          "ut.answerkey_id, "+
					          "ut.organ_name, "+
					          "ut.organ_id, "+
					          "ut.sync_flag, "+
					          "ut.in_time, "+
					          "ut.out_time, "+
					          "ut.sub_time, "+
					          "ut.last_update_date, "+
					          "ut.last_updated_by, "+
					          "ut.last_updated_id_by, "+
					          "ut.creation_date, "+
					          "ut.created_by, "+
					          "ut.created_id_by, "+
					          "ut.batch_no, "+
					          "ut.relation_id, "+
					          "ut.relation_type,ut.THEME_TYPE_ID,ut.THEME_TYPE_NAME "+
					     "from STUDY_USER_ANSWERKEY ut "+
					    "where  ut.study_testpaper_id = '"+studyTestpaperId+"' "+
					      "and ut.created_id_by = '"+employeeId+"')";
				session.createSQLQuery(sql0).executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
		}
		try{	
			String sql1 = "delete STUDY_USER_ANSWERKEY ut "+
					    "where ut.study_testpaper_id = '"+studyTestpaperId+"' "+
					      "and ut.created_id_by = '"+employeeId+"'";
				session.createSQLQuery(sql1).executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
		}
		this.deleteMoreHis(studyTestpaperId, employeeId);
		
	}
	
	
	private void deleteMoreHis(String studyTestpaperId,String employeeId){
		Session session=template.getSessionFactory().getCurrentSession();
		try{
			String sql = "select t.batch_no from STUDY_USER_ANSWERKEY_HIS t "
				+ " where t.study_testpaper_id = '"+studyTestpaperId+"' and t.created_id_by = '"+employeeId+"' "
				+ "  group by t.batch_no  order by t.batch_no desc ";
			SQLQuery sqlQuery=session.createSQLQuery(sql); 
			List<String> list = sqlQuery.addScalar("batch_no", StringType.INSTANCE).list();
			if(list.size()>4){
				for(int i=5;i<list.size();i++){
					try{
						String batch_no = list.get(i);
						String sql1 = " delete STUDY_USER_ANSWERKEY_HIS t where t.batch_no = '"+batch_no+"' "
								+ " and t.study_testpaper_id = '"+studyTestpaperId+"' and t.created_id_by = '"+employeeId+"' ";
						session.createSQLQuery(sql1).executeUpdate();
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
