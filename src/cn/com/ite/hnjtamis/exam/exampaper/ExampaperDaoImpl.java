package cn.com.ite.hnjtamis.exam.exampaper;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;
import org.springframework.orm.hibernate4.HibernateTemplate;

import cn.com.ite.eap2.common.utils.DateUtils;
import cn.com.ite.eap2.core.hibernate.HibernateDefaultDAOImpl;
import cn.com.ite.eap2.core.service.TreeNode;
import cn.com.ite.eap2.domain.baseinfo.Dictionary;
import cn.com.ite.eap2.domain.organization.Dept;
import cn.com.ite.eap2.domain.organization.Employee;
import cn.com.ite.eap2.domain.organization.Organ;
import cn.com.ite.eap2.domain.organization.Quarter;
import cn.com.ite.eap2.domain.power.SysUser;
import cn.com.ite.hnjtamis.common.ThemeMakeCode;
import cn.com.ite.hnjtamis.exam.base.theme.ThemeCheckOption;
import cn.com.ite.hnjtamis.exam.exampaper.form.ExamDeptPassForm;
import cn.com.ite.hnjtamis.exam.exampaper.form.ExamUserInticketForm;
import cn.com.ite.hnjtamis.exam.exampaper.form.ExamUserPassForm;
import cn.com.ite.hnjtamis.exam.hibernatemap.Exam;
import cn.com.ite.hnjtamis.exam.hibernatemap.ExamUserTestpaper;
import cn.com.ite.hnjtamis.exam.hibernatemap.Theme;
import cn.com.ite.hnjtamis.exam.hibernatemap.ThemeType;

/**
 *
 * <p>Title cn.com.ite.hnjtamis.exam.exampaper.ExampaperDaoImpl</p>
 * <p>Description 考试安排与试卷生成 </p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2015</p>
 * @author 朱健
 * @create time: 2015年4月7日 下午2:55:29
 * @version 1.0
 * 
 * @modified records:
 */
public class ExampaperDaoImpl extends HibernateDefaultDAOImpl implements ExampaperDao{
	
	/**
	 * 发布试卷
	 * @description
	 * @param exam_arrange_id
	 * @modified
	 */
	public void saveAndPublicInExamArrange(String exam_arrange_id,String employeeId,String employeeName)throws Exception{
		try{
			Session session = template.getSessionFactory().getCurrentSession();
			
			String publicTime = DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss");
			String sql = "select ea.score_start_time, ea.score_end_time, ee.exam_id "+
						  " from exam_arrange ea, exam ee "+
						 " where ea.exam_arrange_id = ee.exam_arrange_id "+
						   " and ea.exam_arrange_id = ?";
			SQLQuery sqlQuery=session.createSQLQuery(sql); 
			sqlQuery.setString(0, exam_arrange_id);
			List<Object[]> querylist = sqlQuery
					.addScalar("score_start_time", StringType.INSTANCE)
					.addScalar("score_end_time", StringType.INSTANCE)
					.addScalar("exam_id", StringType.INSTANCE).list();
			for(int i=0;i<querylist.size();i++){
				Object[] object = querylist.get(i);
				String scoreStartTime = (String)object[0];
				String scoreEndTime =  (String)object[1];
				String examId =  (String)object[2];
				if(scoreStartTime!=null && !"".equals(scoreStartTime) && !"null".equals(scoreStartTime)
						&& scoreEndTime!=null && !"".equals(scoreEndTime) && !"null".equals(scoreEndTime)){
					
				}else{
					String stateTime = DateUtils.getDateAddDay(publicTime.substring(0,10),"yyyy-MM-dd",1);
					
					String year = stateTime.substring(0,4);
					String endTime = (Integer.parseInt(year)+1)+(stateTime.substring(4,10));
					endTime = DateUtils.getDateAddDay(endTime,"yyyy-MM-dd",-1);
					
					scoreStartTime = stateTime;
					scoreEndTime = endTime;
				}
				this.saveAndPublicExam(examId, scoreStartTime, scoreEndTime,
						employeeId, employeeName, publicTime);
			}
			
			String updateStr = " update exam_arrange  e set e.state = '30', "
					+ " e.public_time = '"+publicTime+"',e.public_user = '"+employeeName+"',"
							+ " e.public_user_id = '"+employeeId+"' "
					+ " where e.exam_arrange_id not in(select t.exam_arrange_id from exam t "
					+ " where t.exam_arrange_id = '"+exam_arrange_id+"' "
					+ " and (t.state <> '30' or t.state is null)) and e.exam_arrange_id = '"+exam_arrange_id+"' ";
			 this.saveSQL(updateStr); 
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	/**
	 *
	 * @description
	 * @param examId
	 * @param scoreStartTime
	 * @param scoreEndTime
	 * @modified
	 */
	private void saveAndPublicExam(String examId,String scoreStartTime,String scoreEndTime,
			String employeeId,String employeeName,String publicTime)throws Exception{
		try{
			Session session = template.getSessionFactory().getCurrentSession();
			String sql = "select t.user_testpaper_id,t.employee_name "+
					  "from exam_user_testpaper t "+
					 "where t.exam_id = ? "+
					   "and not exists "+
					       " (select 1"+
					          " from exam_testpaper_theme tt "+
					         " where  tt.exam_testpaper_id = t.testpaper_id and tt.state not in ('20', '30') and tt.exam_id = ? ) ";
			SQLQuery sqlQuery=session.createSQLQuery(sql); 
			sqlQuery.setString(0, examId);
			sqlQuery.setString(1, examId);
			List<Object[]> querylist = sqlQuery
					.addScalar("user_testpaper_id", StringType.INSTANCE)
					.addScalar("employee_name", StringType.INSTANCE).list();
			int MaxIds = 20;
			String[] user_testpaper_ids = new String[MaxIds]; 
			int addIds = 0;
			for(int k=0;k<querylist.size();k++){
				Object[] object = querylist.get(k);
				String user_testpaper_id = (String)object[0];
				String employee_name = (String)object[1];
				if(k == 0){
					String updateStr = "update exam e set e.score = (select sum(tt.default_score) "+
							"	from exam_testpaper_theme tt, exam_user_testpaper t "+
					   " where tt.exam_testpaper_id = t.testpaper_id and t.user_testpaper_id = '"+user_testpaper_id+"') "
						 + " where e.exam_id = '"+examId+"' ";
					this.saveSQL(updateStr);
				}
				user_testpaper_ids[addIds] = user_testpaper_id;
				addIds++;
				if(addIds == MaxIds || k==querylist.size()-1){
					String str = "";
					for(int t=0;t<user_testpaper_ids.length;t++){
						if(user_testpaper_ids[t]!=null && !"".equals(user_testpaper_ids[t])
								&& !"null".equals(user_testpaper_ids[t])){
							str+="'"+user_testpaper_ids[t]+"',";
						}
					}
					str = str.substring(0,str.length()-1);
					String updateStr = "update exam_user_testpaper t "+
					   "set t.frist_scote = "+
					      " (select sum(tt.score) "+
					         " from exam_testpaper_theme tt "+
					         " where tt.exam_testpaper_id = t.testpaper_id), "+
					       " t.state   = '30', "+ 
					       " t.score_start_time = '"+scoreStartTime+"', "+ 
					       " t.score_end_time ='"+scoreEndTime+"' "+
					 " where t.user_testpaper_id in ("+str+") ";
					this.saveSQL(updateStr);      
					addIds = 0;
				}
			}
			String updateStr = " update exam_user_testpaper t "+
				 " set t.scote  =  t.frist_scote, "+
				" t.pass_state = (select decode(sign(t.frist_scote - (e.score * e.pass_score / 100)),1,'T',0,'T','F')  "+
				 " from exam e where e.exam_id = t.exam_id) "+
				" where  t.state = '30' "+
				" and t.exam_id = '"+examId+"' ";
			this.saveSQL(updateStr);  
			
			 
			 updateStr = " update exam  e set e.state = '30',"
			 		+ "e.public_time = '"+publicTime+"',"
			 		+ "e.public_user = '"+employeeName+"',"
			 		+ "e.public_user_id = '"+employeeId+"' "+
				" where e.exam_id not in(select t.exam_id from exam_user_testpaper t where t.exam_id = '"+examId+"' "+
				" and t.state <> '30') and e.exam_id = '"+examId+"' ";
			 this.saveSQL(updateStr); 
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	
	/**
	 * 获取当天的用户信息与准考证密码之间的映射Map，可以用于用户名密码以及身份证密码登录用
	 * @author 朱健
	 * @return
	 * @modified
	 */
	public List<ExamUserInticketForm> getExamUserInticketRelation(){
		String nowTime = DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss");
		String nowDay = nowTime.substring(0,10);
		List<ExamUserInticketForm> list = new ArrayList<ExamUserInticketForm>();
		String sql = "select u.inticket, u.exam_password, u.employee_id,su.account,su.password,u.exam_id,ee.identity_card,e.exam_name "+
				  " from  (select ea.exam_id,ea.exam_name ,ea.exam_starttime " + 
				  " from exam ea where substr(ea.exam_starttime, 0, 10) = ? "+
				  	" and ea.exam_endtime >= ?) e, "
				  	+ " (select * from  exam_user_testpaper uu where uu.exam_password is not null "+
				  	" and uu.employee_id is not null) u,sys_user su,employee ee "+
				 " where e.exam_id = u.exam_id and u.employee_id = su.employee_id "+
				   " and ee.employee_id = su.employee_id "+
				    " order by e.exam_starttime desc";
		HibernateTemplate template = (HibernateTemplate) this.template;		   
		Session session = template.getSessionFactory().getCurrentSession();
		SQLQuery sqlQuery=session.createSQLQuery(sql); 
		sqlQuery.setString(0, nowDay);
		sqlQuery.setString(1, nowTime);
		List querylist = sqlQuery
				.addScalar("inticket", StringType.INSTANCE)
				.addScalar("exam_password", StringType.INSTANCE)
				.addScalar("employee_id", StringType.INSTANCE)
				.addScalar("account", StringType.INSTANCE)
				.addScalar("password", StringType.INSTANCE)
				.addScalar("exam_id", StringType.INSTANCE)
				.addScalar("identity_card", StringType.INSTANCE)
				.addScalar("exam_name", StringType.INSTANCE).list();
		for(int i=0;i<querylist.size();i++){
			 Object[] tmp= (Object[])querylist.get(i);
			 ExamUserInticketForm form = new ExamUserInticketForm();
			 form.setInticket((String)tmp[0]);//准考证
			 form.setExamPassword((String)tmp[1]);//准考证密码
			 form.setEmployee_id((String)tmp[2]);//员工ID
			 form.setAccount((String)tmp[3]);//登录账户
			 form.setPassword((String)tmp[4]);//登录密码
			 form.setExamId((String)tmp[5]);//考试信息ID
			 form.setIdentityCard((String)tmp[6]);//身份证号
			 form.setExamName((String)tmp[7]);//考试信息
			 list.add(form);
		}
		return list;
	}

	/**
	 * 获取有效的员工对应用户信息
	 * @author 朱健
	 * @return
	 * @modified
	 */
	public Map<String,SysUser> getSysUserMap(){
		Map<String,SysUser> sysUserMap = new HashMap<String,SysUser>();
		String sql = "select t.EMPLOYEE_ID as EMPLOYEEID, t.ACCOUNT, t.PASSWORD from SYS_USER t "
				+ " where regexp_replace(t.ACCOUNT,'\\d','') is  null "//(t.ACCOUNT not like '%admin%') "
				+ "and (t.EMPLOYEE_ID is not null)  and t.user_id in(select distinct u.user_id from USER_ROLE u) "
				+ " and t.ISVALIDATION = 1 order by t.ACCOUNT desc ";
		HibernateTemplate template = (HibernateTemplate) this.template;		   
		Session session = template.getSessionFactory().getCurrentSession();
		SQLQuery sqlQuery=session.createSQLQuery(sql); 
		List list = sqlQuery
				.addScalar("EMPLOYEEID", StringType.INSTANCE)
				.addScalar("ACCOUNT", StringType.INSTANCE)
				.addScalar("PASSWORD", StringType.INSTANCE).list();
		for(int i=0;i<list.size();i++){
			 Object[] tmp= (Object[])list.get(i);
			 SysUser sysUser = new SysUser();
			 String employeeId = (String)tmp[0];
			 String account = (String)tmp[1];
			 sysUser.setAccount(account!=null?account.trim():account);
			 sysUser.setPassword((String)tmp[2]);
			 sysUserMap.put(employeeId, sysUser);
		}
		return sysUserMap;
	}
	
	/**
	 * 根据题库获取题型的数目
	 * @author 朱健
	 * @param themeBankId 题库ID
	 * @param relationType 已生成试题关联类型
	 * @param themeTypes 试题类型
	 * @param  chouti_theme_type 考试类型  10- 达标考试 20-竞赛考试 30-培训测试 40-模拟考试
	 * @param maxRowNum 获取题目的最大值
	 * @return
	 * @modified
	 */
	public List<Object[]> getThemeTypeByBankId(String themeBankId,String relationType,
			String themeTypes,int chouti_theme_type,int maxRowNum){
		List list = new ArrayList();
		try{
			// chouti_theme_type //考试类型  10- 达标考试 20-竞赛考试 30-培训测试 40-模拟考试
			String examType ="";
			if(chouti_theme_type == 10 || chouti_theme_type==20){
				examType = "10,40";
			}else if(chouti_theme_type == 30){
				examType = "20,30,40";
			}else if(chouti_theme_type == 40){
				examType = "20,40";
			}
			String themeBankIds = "";
			if(themeBankId.indexOf(",")!=-1){
				String[] tmps = themeBankId.split(",");
				for(int i=0;i<tmps.length;i++){
					themeBankIds+="'"+tmps[i]+"',";
				}
				if(themeBankIds.length()>0){
					themeBankIds = themeBankIds.substring(0,themeBankIds.length()-1);
				}
			}else{
				themeBankIds = "'"+themeBankId+"'";
			}
			
			String sql = "select {pp.*}, vv.num themenum "+
							  " from theme_type pp, "+
							  " (select p.theme_type_id, count(*) num "+
							  " from theme_in_bank i, theme t, theme_type p "+
							  " where i.theme_id = t.theme_id "+
							  " and p.theme_type_id = t.theme_type_id "+
							  " and i.theme_bank_id in ("+themeBankIds+") "+
							  " and not exists (select 1 "+
							  " from testpaper_theme tt,testpaper tp "+
							  " where t.theme_id = tt.THEME_ID "
							  + " and tt.testpaper_id = tp.testpaper_id "
							  + " and tp.relation_type like '%' || ? ||'%') "
							  + " and p.theme_type in ("+themeTypes+") "
							  + " and t.type in("+examType+") "
							  + " and t.state = 15 and t.IS_USE = 5 "
							  + " and p.IS_USE = 10 "+
							  " and rownum <= ? "+
							  " group by p.theme_type_id) vv "+
							  " where pp.theme_type_id = vv.theme_type_id "+
							  " order by pp.sort_num";
			HibernateTemplate template = (HibernateTemplate) this.template;		   
			Session session = template.getSessionFactory().getCurrentSession();
			SQLQuery sqlQuery=session.createSQLQuery(sql);
			//sqlQuery.setString(0, themeBankId);
			sqlQuery.setString(0, relationType);   
			//sqlQuery.setString(2, themeTypes);   
			sqlQuery.setInteger(1, maxRowNum);   
			list = sqlQuery.addEntity("pp", ThemeType.class).addScalar("themenum", IntegerType.INSTANCE).list();
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}
	
	
	/**
	 * 根据题库获取题型的数目
	 * @author 朱健
	 * @param themeBankId 题库ID
	 * @param relationType 已生成试题关联类型
	 * @param themeTypes 试题类型
	 * @param  chouti_theme_type 考试类型  10- 达标考试 20-竞赛考试 30-培训测试 40-模拟考试
	 * @param maxRowNum 获取题目的最大值
	 * @return
	 * @modified
	 */
	public List<Object[]> getThemeTypeAndThemeNumsByBankId(String themeBankId,String relationType,
			String themeTypes,int chouti_theme_type){
		List list = new ArrayList();
		try{
			if(themeBankId==null || "".equals(themeBankId)){
				return list;
			}
			// chouti_theme_type //考试类型  10- 达标考试 20-竞赛考试 30-培训测试 40-模拟考试
			String examType ="";
			if(chouti_theme_type == 10 || chouti_theme_type==20){
				examType = "10,40";
			}else if(chouti_theme_type == 30){
				examType = "20,30,40";
			}else if(chouti_theme_type == 40){
				examType = "20,40";
			}
			String themeBankIds = "";
			if(themeBankId.indexOf(",")!=-1){
				String[] tmps = themeBankId.split(",");
				for(int i=0;i<tmps.length;i++){
					themeBankIds+="'"+tmps[i]+"',";
				}
				if(themeBankIds.length()>0){
					themeBankIds = themeBankIds.substring(0,themeBankIds.length()-1);
				}
			}else{
				themeBankIds = "'"+themeBankId+"'";
			}
			
			String sql = "";
			if("mocs".equals(relationType)){
				sql = "select {pp.*}, vv.num themenum "+
						  " from theme_type pp, "+
						  " (select p.theme_type_id, count(*) num "+
						  " from theme_in_bank i, theme t, theme_type p "+
						  " where i.theme_id = t.theme_id "+
						  " and p.theme_type_id = t.theme_type_id "+
						  " and i.theme_bank_id in ("+themeBankIds+") "
						  + " and p.theme_type in ("+themeTypes+") "
						  + " and t.type in("+examType+") "
						  + " and t.state = 15 and t.IS_USE = 5 "
						  + " and p.IS_USE = 10 "+
						  " group by p.theme_type_id) vv "+
						  " where pp.theme_type_id = vv.theme_type_id "+
						  " order by pp.sort_num";
			}else{
				sql = "select {pp.*}, vv.num themenum "+
						  " from theme_type pp, "+
						  " (select p.theme_type_id, count(*) num "+
						  " from theme_in_bank i, theme t, theme_type p "+
						  " where i.theme_id = t.theme_id "+
						  " and p.theme_type_id = t.theme_type_id "+
						  " and i.theme_bank_id in ("+themeBankIds+") "+
						  " and not exists (select 1 "+
						  " from testpaper_theme tt,testpaper tp "+
						  " where t.theme_id = tt.THEME_ID "
						  + " and tt.testpaper_id = tp.testpaper_id "
						  + " and tp.relation_type like '%' || ? ||'%') "
						  + " and p.theme_type in ("+themeTypes+") "
						  + " and t.type in("+examType+") "
						  + " and t.state = 15 and t.IS_USE = 5 "
						  + " and p.IS_USE = 10 "+
						 // " and rownum <= ? "+
						  " group by p.theme_type_id) vv "+
						  " where pp.theme_type_id = vv.theme_type_id "+
						  " order by pp.sort_num";
			}
			HibernateTemplate template = (HibernateTemplate) this.template;		   
			Session session = template.getSessionFactory().getCurrentSession();
			SQLQuery sqlQuery=session.createSQLQuery(sql);
			//sqlQuery.setString(0, themeBankId);
			if(!"mocs".equals(relationType)){
				sqlQuery.setString(0, relationType);  
			}
			//sqlQuery.setString(2, themeTypes);   
			//sqlQuery.setInteger(1, maxRowNum);   
			list = sqlQuery.addEntity("pp", ThemeType.class).addScalar("themenum", IntegerType.INSTANCE).list();
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 更新标准编码
	 * @author 朱健
	 * @modified
	 */
	public void updateQuarterTrainInfo(){
		try{
			String sql = "update jobs_union_standard t "
					+ " set t.quarter_train_code = "
					+ " (select q.quarter_train_code from quarter q "
					+ " where q.quarter_id = t.jobscode) "
					+ " where t.quarter_train_code is null";
			Session session=template.getSessionFactory().getCurrentSession();
			session.createSQLQuery(sql).executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		try{
			String sql = "update jobs_union_standard t "
					+ " set t.quarter_train_name = (select q.quarter_name from "
					+ " (select qq.quarter_code,qq.quarter_name "
					+ " from quarter_standard qq group by qq.quarter_code,qq.quarter_name) q "
					+ " where q.quarter_code = t.quarter_train_code) "
					+ " where t.quarter_train_name is null";
			Session session=template.getSessionFactory().getCurrentSession();
			session.createSQLQuery(sql).executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 删除题库已经删除，但是题库与试题关系没有删除的
	 * @author 朱健
	 * @modified
	 */
	private void deleteThemeInBank(){
		try{
			String sql = "delete theme_in_bank v where v.theme_bank_id not in(select k.theme_bank_id from theme_bank k)";
			Session session=template.getSessionFactory().getCurrentSession();
			session.createSQLQuery(sql).executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 更新试题校验码
	 * @author 朱健
	 * @modified
	 */
	public void updateThemeCrc(){
		//删除题库已经删除，但是题库与试题关系没有删除的
		deleteThemeInBank();
		
		HibernateTemplate template = (HibernateTemplate) this.template;		   
		Session session = template.getSessionFactory().getCurrentSession();
		
		String sql =" select distinct v.theme_bank_id THEMEBANKID "
				+ " from theme_in_bank v where exists "
				+ " (select 1 from theme tt where tt.theme_crc is null and tt.theme_id = v.theme_id and tt.is_use = 5) ";
		SQLQuery sqlQuery=session.createSQLQuery(sql);  
		List<String> list = sqlQuery.addScalar("THEMEBANKID", StringType.INSTANCE).list();
		if(list!=null && list.size()>0){
			for(int i=0;i<list.size();i++){
				String theme_bank_id = list.get(i);
				this.updateThemeCrcByThemeBank(theme_bank_id);
			}
		}
		
		
		//处理余下来的试题
		sql =" select {t.*} from theme t where t.theme_crc is null and t.is_use = 5 ";
		sqlQuery=session.createSQLQuery(sql);  
		List<Theme> themelist = sqlQuery.addEntity("t", Theme.class).list();
		if(themelist!=null && themelist.size()>0){
			for(int i=0;i<themelist.size();i++){
				Theme theme = themelist.get(i);
				theme.setThemeCrc(ThemeCheckOption.getCrcInTheme(theme));
				if(theme.getThemeCode() == null || "".equals(theme.getThemeCode())){
					theme.setThemeCode(ThemeMakeCode.getCode(i));//试题编码
				}
				this.updateEntity(theme);
			}
		}
	}
	
	private void updateThemeCrcByThemeBank(String theme_bank_id){
		HibernateTemplate template = (HibernateTemplate) this.template;		   
		Session session = template.getSessionFactory().getCurrentSession();
		String sql =" select {t.*} from theme t where t.theme_crc is null  and exists "
				+ " ( select * from theme_in_bank v where v.theme_id = t.theme_id  and v.theme_bank_id = ?) "
				+ " and t.is_use = 5 ";
		SQLQuery sqlQuery=session.createSQLQuery(sql);  
		sqlQuery.setString(0, theme_bank_id);
		List<Theme> list = sqlQuery.addEntity("t", Theme.class).list();
		if(list!=null && list.size()>0){
			for(int i=0;i<list.size();i++){
				Theme theme = list.get(i);
				theme.setThemeCrc(ThemeCheckOption.getCrcInTheme(theme));
				if(theme.getThemeCode() == null || "".equals(theme.getThemeCode())){
					theme.setThemeCode(ThemeMakeCode.getCode(i));//试题编码
				}
				this.updateEntity(theme);
			}
		}
	}
	
	
	/**
	 * 更新试题编码
	 * @author 朱健
	 * @modified
	 */
	public void updateThemeCode(){
		//删除题库已经删除，但是题库与试题关系没有删除的
		deleteThemeInBank();
		
		HibernateTemplate template = (HibernateTemplate) this.template;		   
		Session session = template.getSessionFactory().getCurrentSession();
		
		String sql =" select distinct v.theme_bank_id THEMEBANKID "
				+ " from theme_in_bank v where exists "
				+ " (select 1 from theme tt where tt.theme_code is null and tt.theme_id = v.theme_id and tt.is_use = 5) ";
		SQLQuery sqlQuery=session.createSQLQuery(sql);  
		List<String> list = sqlQuery.addScalar("THEMEBANKID", StringType.INSTANCE).list();
		if(list!=null && list.size()>0){
			for(int i=0;i<list.size();i++){
				String theme_bank_id = list.get(i);
				this.updateThemeCodeByThemeBank(theme_bank_id);
			}
		}
		
		//处理余下来的试题
		sql =" select {t.*} from theme t where t.theme_code is null and t.is_use = 5 ";
		sqlQuery=session.createSQLQuery(sql);  
		List<Theme> themelist = sqlQuery.addEntity("t", Theme.class).list();
		if(themelist!=null && themelist.size()>0){
			for(int i=0;i<themelist.size();i++){
				Theme theme = themelist.get(i);
				theme.setThemeCode(ThemeMakeCode.getCode(i));//试题编码
				this.updateEntity(theme);
			}
		}
	}
	
	private void updateThemeCodeByThemeBank(String theme_bank_id){
		HibernateTemplate template = (HibernateTemplate) this.template;		   
		Session session = template.getSessionFactory().getCurrentSession();
		String sql =" select {t.*} from theme t where t.theme_code is null  and exists "
				+ " ( select * from theme_in_bank v where v.theme_id = t.theme_id  and v.theme_bank_id = ?) "
				+ " and t.is_use = 5 ";
		SQLQuery sqlQuery=session.createSQLQuery(sql);  
		sqlQuery.setString(0, theme_bank_id);
		List<Theme> list = sqlQuery.addEntity("t", Theme.class).list();
		if(list!=null && list.size()>0){
			for(int i=0;i<list.size();i++){
				Theme theme = list.get(i);
				theme.setThemeCode(ThemeMakeCode.getCode(i));//试题编码
				this.updateEntity(theme);
			}
		}
	}
	
	/**
	 * 关闭到考试结束时间但未提交答案的考生的考试时间
	 * @author zhujian
	 * @description
	 * @modified
	 */
	public void saveAndsubInExamUserTestpaper(){
		try{
			String sql = "update EXAM_USER_TESTPAPER v set v.sub_time = (select e.exam_endtime from exam e where e.exam_id = v.exam_id) "+
				   " where v.user_testpaper_id in(select t1.user_testpaper_id "+
				   " from EXAM_USER_TESTPAPER t1, exam t2 "+
				   " where t1.exam_id = t2.exam_id "+
				   " and ROUND(TO_NUMBER(to_date('"+DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss")+"', 'yyyy-MM-dd hh24:mi:ss') -  to_date(t2.exam_endtime, 'yyyy-MM-dd hh24:mi:ss')) * 24 * 60) > 0 and t1.sub_time is null "+
				   " and t2.exam_endtime is not null) "+
				   " and v.sub_time is null and (v.relation_type not in('moni') or v.relation_type is null)";
			Session session=template.getSessionFactory().getCurrentSession();
			session.createSQLQuery(sql).executeUpdate();
			
			
			String sql1 = "update exam_testpaper_theme t set t.state = 15 "+
					" where t.state in ('5','10') "
					//+ "and t.score_type in(0,1) "
					+ " and t.exam_testpaper_id "+
					" in(select v.testpaper_id from exam_user_testpaper v where v.sub_time is not null and (v.relation_type not in('moni') or v.relation_type is null))";
			session.createSQLQuery(sql1).executeUpdate();
			
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 更新试卷的分数、试题数量
	 * @author 朱健
	 * @modified
	 */
	public void updateExamTestPaperTotal(){
		try{
			String sql2 = "update exam_testpaper t set  "+
					" t.total_theme = (select count(*) from exam_testpaper_theme v where v.exam_testpaper_id = t.exam_testpaper_id),  "+
					" t.total_score = (select sum(v.default_score) from exam_testpaper_theme v where v.exam_testpaper_id = t.exam_testpaper_id)  "+
					" where t.state <> 30 and t.exam_testpaper_id "+
					" in (select v.testpaper_id from exam_user_testpaper v where v.sub_time is not null) "
					+ " and t.relation_type is null";
			Session session=template.getSessionFactory().getCurrentSession();
			session.createSQLQuery(sql2).executeUpdate();
			
			
			String sql3 = "update exam_testpaper t set t.total_score =( "+
				" select sum(v.default_score) from exam_testpaper_theme v where v.exam_testpaper_id=t.exam_testpaper_id "+
				" group by v.exam_testpaper_id), "+
				" t.total_theme =(select count(*) from exam_testpaper_theme v where v.exam_testpaper_id=t.exam_testpaper_id "+
				" group by v.exam_testpaper_id) where t.exam_testpaper_id "+
				" in (select v.testpaper_id from exam_user_testpaper v where v.sub_time is not null) "
				+ " and t.relation_type is null";
			session.createSQLQuery(sql3).executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	
	/**
	 *
	 * @author zhujian
	 * @description 获取有考试结束时间还未阅卷的考生
	 * @return
	 * @modified
	 */
	public List<ExamUserTestpaper> getNotSubmitAnsExamUserTestpaperList(){
		String sql =" select {t.*} from exam_user_testpaper t where t.testpaper_id "+
				" in(select v.exam_testpaper_id from exam_testpaper_theme v where v.state in(5,10,15) and v.score_type = 0) "+
				" and t.sub_time is not null and (t.relation_type not in('moni') or t.relation_type is null) order by t.last_update_date ";
		HibernateTemplate template = (HibernateTemplate) this.template;		   
		Session session = template.getSessionFactory().getCurrentSession();
		SQLQuery sqlQuery=session.createSQLQuery(sql);  
		List<ExamUserTestpaper> list = sqlQuery.addEntity("t", ExamUserTestpaper.class).list();
		return list;
	}
	
	/**
	 * 清理考试安排中未选中考生的准考证
	 * @author zhujian
	 * @description
	 * @modified
	 */
	public void saveAndCleanPublicUserInticket(){
		try{
			String sql = "update exam_public_user t  set t.inticket = null, t.exam_password = null "
					+ " where t.user_id not in "
					+ " (select v.user_id  from exam_user_testpaper v where v.user_id is not null) "
					+ " and t.inticket is not null";
			Session session=template.getSessionFactory().getCurrentSession();
			int len = session.createSQLQuery(sql).executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 查询数据字典
	 * @author zhujian
	 * @description
	 * @param dtType
	 * @return
	 * @modified
	 */
	public List<Dictionary> getDictionaryTypeList(String dtType){
		String sql =" select {t.*} from dictionary t,dictionary_type a  where t.dt_id = a.dt_id and a.dt_code = ? order by t.sort_no ";
		HibernateTemplate template = (HibernateTemplate) this.template;		   
		Session session = template.getSessionFactory().getCurrentSession();
		SQLQuery sqlQuery=session.createSQLQuery(sql);
		sqlQuery.setString(0, dtType);   
		List list = sqlQuery.addEntity("t", Dictionary.class).list();
		return list;
	}
	
	
	/**
	 * 获取考试安排编码
	 * @author zhujian
	 * @description
	 * @param exam_arrangeid
	 * @return
	 * @modified
	 */
	public String  getPrefix(String exam_arrangeid){
		String sqlStr = " select t.exam_code EXAMCODE from exam_arrange t  where t.exam_arrange_id = :exam_arrangeid ";
		Map paramMap = new HashMap();
		paramMap.put("exam_arrangeid", exam_arrangeid);
		List list = this.querySql(sqlStr, paramMap,null,null);
		if(list!=null && list.size()>0){
			Map value = (Map)list.get(0);
			String code = (String)value.get("EXAMCODE");
			return code;
		}else{
			return DateUtils.convertDateToStr(new Date(),"yyyyMMddHH");
		}
	}
	
	/**
	 * 获取准考证编码的后几位的最大值
	 * @author zhujian
	 * @description
	 * @param exam_arrangeid
	 * @param prefix
	 * @return
	 * @modified
	 */
	public int getPostfixMaxNum(String exam_arrangeid,String prefix){
		String sqlStr1 = " select max(to_number(substr(t.inticket, "+(prefix.length()+1+8)+", 5))) INTICKET " +
		"from exam_user_testpaper t,exam b where  substr(t.inticket, 0, "+prefix.length()+") = :prefix "
		+ " and t.exam_id = b.exam_id and b.exam_arrange_id= :exam_arrangeid and length(t.inticket) > " +(prefix.length()+1+8) ;
		Map paramMap = new HashMap();
		paramMap.put("prefix", prefix);
		paramMap.put("exam_arrangeid", exam_arrangeid);
		List list1 = this.querySql(sqlStr1, paramMap,null,null);
		Map value = (Map)list1.get(0);
		BigDecimal inticket=(BigDecimal) value.get("INTICKET");
		if(inticket == null){
			return 0;
		}else{
			return inticket.intValue();
		}
	}
	
	
	/**
	 * 删除空的考试数据
	 * @author zhujian
	 * @description
	 * @modified
	 */
	public void deleteNullExam(){
		try{
			String sql1 = "delete exam t where t.exam_arrange_id is null";
			Session session=template.getSessionFactory().getCurrentSession();
			int len = session.createSQLQuery(sql1).executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 删除没有分配考生科目的考生
	 * @author zhujian
	 * @description
	 * @modified
	 */
	public void deleteNullexamUserTestpaper(){
		try{
			String sql1 = "delete  exam_user_testpaper t where t.exam_id is null";
			Session session=template.getSessionFactory().getCurrentSession();
			int len = session.createSQLQuery(sql1).executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	
	/**
	 * 删除没有考生使用的试卷
	 * @author zhujian
	 * @description
	 * @modified
	 */
	public void deleteNullTestpaperInExamUserTestpaper(String examId){
		try{
			
			Session session=template.getSessionFactory().getCurrentSession();
			String sql1 = "delete Exam_Testpaper_Answerkey tttt "+
						 " where tttt.exam_testpaper_theme_id not in "+
						 " (select aa.exam_testpaper_theme_id from Exam_Testpaper_Theme aa "+
						 " where aa.exam_testpaper_id in "+
						       " (select tt.testpaper_id  from EXAM_USER_TESTPAPER tt where tt.exam_id = '"+examId+"') "
						       		+ " and aa.exam_id = '"+examId+"') "+
						           " and tttt.exam_id = '"+examId+"'";
			int len = session.createSQLQuery(sql1).executeUpdate();
			
			String sql2 = " delete EXAM_TESTPAPER_SEARCHKEY tttt "+
							 " where tttt.exam_testpaper_id not in "+
					 " (select tt.testpaper_id from EXAM_USER_TESTPAPER tt "
					 + "  where tt.exam_id = '"+examId+"')";
			int len2 = session.createSQLQuery(sql2).executeUpdate();
			
			String sql3 = " delete Exam_Testpaper_Theme t "+
							 " where t.exam_testpaper_id not in "+
							       " (select tt.testpaper_id "+
							          " from EXAM_USER_TESTPAPER tt "+
							         " where tt.exam_id = '"+examId+"') "+
							   " and t.exam_id = '"+examId+"'";
			int len3 = session.createSQLQuery(sql3).executeUpdate();
			
			String sql4 = "delete EXAM_TESTPAPER t "+
					" where t.exam_testpaper_id not in "+
					       "(select tt.testpaper_id "+
					          " from EXAM_USER_TESTPAPER tt "+
					        " where tt.exam_id = '"+examId+"') "+
					   " and t.exam_id = '"+examId+"'";
			int len4 = session.createSQLQuery(sql4).executeUpdate();
		}catch(Exception e){
			System.out.println("处理deleteNullTestpaperInExamUserTestpaper异常，examId="+examId);
			e.printStackTrace();
		}
		
	}
	
	
	/**
	 * 汇总出题数量与用题数量
	 * @author zhujian
	 * @description
	 * @modified
	 */
	public void saveUseTestpaperNum(){
		try{
			String sql1 = " update testpaper t set "
					+ "	t.use_num = (select count(*) from exam e where e.testpaper_id = t.testpaper_id group by e.testpaper_id) ";
			String sql2 = " update testpaper t "
					+ " set t.use_num = 0 where t.use_num is null";
			String sql3 = " update theme t  set "+
				" t.theme_set_num = (select count(*) from testpaper_theme tt "
				+ " where tt.theme_id = t.theme_id group by tt.theme_id), "+
				" t.theme_people_num = (select count(*) from testpaper_theme v where v.theme_id = t.theme_id group by v.theme_id)"
				+ " where t.is_use = 5 ";	
			String sql4 = " update theme t set t.theme_set_num = 0 where t.theme_set_num is null and t.is_use = 5 ";
			String sql5 = " update theme t set t.theme_people_num = 0 where t.theme_people_num is null and t.is_use = 5 ";	
			String sql6= " update theme t set t.theme_right_num = 0 where t.theme_right_num is null and t.is_use = 5 ";	
			Session session=template.getSessionFactory().getCurrentSession();
			session.createSQLQuery(sql1).executeUpdate();
			session.createSQLQuery(sql2).executeUpdate();
			session.createSQLQuery(sql3).executeUpdate();
			session.createSQLQuery(sql4).executeUpdate();
			session.createSQLQuery(sql5).executeUpdate();
			session.createSQLQuery(sql6).executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	/**
	 *
	 * @author zhujian
	 * @description 获取三天内要进行考试的考生
	 * @return
	 * @modified
	 */
	public List<ExamUserTestpaper> getExeExamUserList(){
		List<ExamUserTestpaper> list = new ArrayList<ExamUserTestpaper>();
		try{
			String sqlStr = " select {v.*} "+
						    " from exam_user_testpaper v "+
						 " where v.exam_id in "+
						      " (select t.exam_id "+
						         " from exam t "+
						      " where t.exam_starttime is not null  and t.exam_endtime is not null "+
						      " and (sysdate - ?) < to_date(t.exam_starttime, 'yyyy-MM-dd hh24:mi:ss') "+
						      " and (sysdate + ?) > to_date(t.exam_endtime, 'yyyy-MM-dd hh24:mi:ss')) "+ 
						      " order by v.exam_id ";
			HibernateTemplate template = (HibernateTemplate)  this.template;
			Session session = template.getSessionFactory().getCurrentSession();
			SQLQuery sqlQuery=session.createSQLQuery(sqlStr);
			sqlQuery.setInteger(0, 2);
		    sqlQuery.setInteger(1, 2);
			list =sqlQuery.addEntity("v",ExamUserTestpaper.class).list();
		}catch(Exception e ){
			e.printStackTrace();
		}
		return list;
	}
	
	
	/**
	 * 获取存在需要手工阅卷的科目
	 * @author 朱健
	 * @return
	 * @modified
	 */
	public Map<String,String> getHaveUserReviewExam(){
		Map<String,String> value = new HashMap<String,String>();
		try{
			String sql = " select distinct t.exam_id examId"+
			  " from EXAM_TESTPAPER_THEME t,(select v.THEME_TYPE_ID from THEME_TYPE v where v.JUDGE = 5) tt,"
			  + " (select et.exam_testpaper_id from EXAM_TESTPAPER et where (et.relation_type not in('moni') or et.relation_type is null)) vv "+
			 " where t.THEME_TYPE_ID  = tt.THEME_TYPE_ID and vv.exam_testpaper_id = t.exam_testpaper_id ";
			HibernateTemplate template = (HibernateTemplate)  this.template;
			Session session = template.getSessionFactory().getCurrentSession();
			SQLQuery sqlQuery=session.createSQLQuery(sql);
			List list =sqlQuery.addScalar("examId", StringType.INSTANCE).list();
			for(int i=0;i<list.size();i++){
				String examId = (String)list.get(i);
				value.put(examId, examId);
			}
		}catch(Exception e ){
			e.printStackTrace();
		}
		return value;
	}
	
	
	/**
	 * 获取存在阅卷人
	 * @author 朱健
	 * @return
	 * @modified
	 */
	public Map<String,String> getHaveMarkpeople(){
		Map<String,String> value = new HashMap<String,String>();
		try{
			String sql = " select distinct t.exam_id examId from exam_markpeople t  ";
			HibernateTemplate template = (HibernateTemplate)  this.template;
			Session session = template.getSessionFactory().getCurrentSession();
			SQLQuery sqlQuery=session.createSQLQuery(sql);
			List list =sqlQuery.addScalar("examId", StringType.INSTANCE).list();
			for(int i=0;i<list.size();i++){
				String examId = (String)list.get(i);
				value.put(examId, examId);
			}
		}catch(Exception e ){
			e.printStackTrace();
		}
		return value;
	}
	
	private Map getEmployeeInfoMap(){
		Map value = new HashMap();
		try{
			String sql = " select {e.*},{q.*},{d.*},{o.*} from employee e, quarter q, dept d, organ o "+
					   " where e.quarter_id = q.quarter_id "+
					   " and e.dept_id = d.dept_id "+
					   " and d.organ_id = o.organ_id "+ 
					   " and e.isvalidation = 1 ";
			Session session = template.getSessionFactory().getCurrentSession();
			SQLQuery sqlQuery=session.createSQLQuery(sql);   
			List querylist = sqlQuery.addEntity("e", Employee.class).addEntity("q", Quarter.class)
		    		.addEntity("d", Dept.class).addEntity("o", Organ.class).list();
		    if(querylist!=null && querylist.size()>0){
		    	for(int i=0;i<querylist.size();i++){
		    		Object[] obj = (Object[])querylist.get(i);
		    		Employee employee = (Employee)obj[0];
		    		Quarter quarter = (Quarter)obj[1];
		    		Dept dept = (Dept)obj[2];
		    		Organ organ = (Organ)obj[3];
		    		value.put(employee.getEmployeeId()+"_employee", employee);
		    		value.put(employee.getEmployeeId()+"_quarter", quarter);
		    		value.put(employee.getEmployeeId()+"_dept", dept);
		    		value.put(employee.getEmployeeId()+"_organ", organ);
		    	}
		    }
		}catch(Exception e){
			e.printStackTrace();
		}
		return value;
	}
	
	
	
	/**
	 * 获取机构的统计信息
	 * @author 朱健
	 * @param organId
	 * @param examId
	 * @param startDay
	 * @param endDay
	 * @return
	 * @modified
	 */
	public List getExamTjxxByOrgan(String organId,String examId,String startDay,String endDay,String exam_property){
		List list = new ArrayList();
		try{
			//获取部门的顶级部门
			Map parentDeptMap = new HashMap();
			Map<String,List<String>> childDeptMap = new HashMap<String,List<String>>();
			//String sql ="select d.dept_id,d.parent_dept_id,{d.*} "
				//	+ " from dept d where d.organ_id = ? ";
		    //sql+=" start with d.parent_dept_id is null  connect by prior d.dept_id = d.parent_dept_id ";
		  //  sql+= " order SIBLINGS BY d.order_no,d.level_code ";
		    String sql = "select {o.*}, count(*) employeenum "+
						  "from organ o, "+
						       "(select * from employee ee where ee.isvalidation = '1') e, "+
						       "dept d "+
						 "where o.parent_organ_id = ? and o.IS_VALIDATION = '1' "+
						   "and o.organ_id = d.organ_id "+
						   "and d.dept_id = e.dept_id "+
						 "group by o.ORGAN_ID,o.PARENT_ORGAN_ID,o.LINK_ORGAN_ID, "+
						          "o.ORGAN_CODE, o.ORGAN_NAME, o.ORGAN_ALIAS,o.ORGAN_TYPE, "+
						          "o.ORDER_NO,o.POSTCODE, o.ADDRESS,o.TELEPHONE,o.FAX,o.IS_VALIDATION, "+
						          "o.REMARK,o.LEVEL_CODE, o.AREA,o.SYS_PARAMETER,o.bank_map_code  "+
						     "order by o.ORDER_NO,o.LEVEL_CODE";
			Session session = template.getSessionFactory().getCurrentSession();
			SQLQuery sqlQuery=session.createSQLQuery(sql); 
			sqlQuery.setString(0, organId);
			List queryDeptlist = sqlQuery.addEntity("o", Organ.class)
					.addScalar("employeenum", IntegerType.INSTANCE).list();
			List organList = new ArrayList();
			Map<String,Integer> deptCountNumMap = new HashMap<String,Integer>();
			for(int i=0;i<queryDeptlist.size();i++){
				Object[] obj = (Object[])queryDeptlist.get(i);
				
				Organ organ = (Organ)obj[0];
				//if(dept.getDept()==null){
					organList.add(organ);
				//}
				Integer employeenum = (Integer)obj[1];
				deptCountNumMap.put(organ.getOrganId()+"_count", employeenum);
			}
			
			
			/*sql="select e.dept_id, tt.employee_id,count(*) themenum "+
			  "from (select t1.employee_id, "+
			               "t1.theme_bank_id, "+
			               "t1.theme_num, "+
			               "decode(t2.theme_fin_num,null,0,t2.theme_fin_num) as theme_fin_num "+
			          "from personal_bank t1 "+
			          "left join personal_bank_fin t2 "+
			            "on t1.employee_id = t2.employee_id "+
			           "and t1.theme_bank_id = t2.theme_bank_id "+
			           "where  t1.theme_num = t2.theme_fin_num and t1.theme_num>0  and t1.theme_num  is not null and t2.theme_fin_num is not null) tt, "+
			       "employee e "+
			 "where tt.employee_id = e.employee_id "+
			 "group by  e.dept_id, tt.employee_id ";
			sqlQuery=session.createSQLQuery(sql);
			List queryDeptXxrslist = sqlQuery.addScalar("dept_id", StringType.INSTANCE)
					.addScalar("themenum", IntegerType.INSTANCE).list();
			for(int i=0;i<queryDeptXxrslist.size();i++){
				Object[] obj = (Object[])queryDeptXxrslist.get(i);
				String dept_id = (String)obj[0];
				Integer themenum = (Integer)obj[1];
				deptCountNumMap.put(dept_id+"_xxrs", themenum);
			}*/
			
			
			
			sql = " select {t.*},{ex.*},{ee.*},{dd.*},{o.*}  from exam_user_testpaper t,exam ex,exam_arrange ea,employee ee,dept dd,organ o "
					+ " where  t.exam_id = ex.exam_id and ex.exam_arrange_id = ea.exam_arrange_id "
					+ " and ee.employee_id = t.employee_id and ee.dept_id = dd.dept_id and dd.organ_id = o.organ_id "
					+ " and dd.organ_id = ?  and ee.isvalidation = 1  ";
			sql+=" and ea.exam_property = ? " ;
			if(examId!=null && !"".equals(examId)){
				sql+=" and instr(',' || ? || ',' , ',' || t.exam_id || ',') > 0 ";
			}
			if(startDay!=null && !"".equals(startDay) && !"null".equals(startDay)
					&& endDay!=null && !"".equals(endDay) && !"null".equals(endDay)){
				sql+=" and not exists ( select 1 from exam_user_testpaper vv where vv.user_testpaper_id = t.user_testpaper_id "
						+ " and vv.score_end_time < '"+startDay+"' or vv.score_start_time > '"+endDay+"') ";
			}else if(startDay!=null && !"".equals(startDay) && !"null".equals(startDay)){
				sql+=" and not exists ( select 1 from exam_user_testpaper vv where vv.user_testpaper_id = t.user_testpaper_id "
						+ " and vv.score_end_time < '"+startDay+"') ";
			}else if(endDay!=null && !"".equals(endDay) && !"null".equals(endDay)){
				sql+=" and not exists  (select 1 from exam_user_testpaper vv where vv.user_testpaper_id = t.user_testpaper_id "
						+ " and vv.score_start_time > '"+endDay+"') ";
			}
			sql+=" and t.pass_state is not null and t.score_start_time is not null and t.score_end_time is not null "
					+ " order by ex.creation_date desc,t.creation_date desc ";
			sqlQuery=session.createSQLQuery(sql); 
			sqlQuery.setString(0, organId);
			sqlQuery.setString(1, exam_property);
			if(examId!=null && !"".equals(examId)){
				sqlQuery.setString(2, examId);
			}
			List querylist = sqlQuery.addEntity("t", ExamUserTestpaper.class).addEntity("ex", Exam.class)
					.addEntity("ee", Employee.class).addEntity("dd", Dept.class).addEntity("o", Organ.class).list();
			Map deptExamMap = new HashMap();
		    if(querylist!=null && querylist.size()>0){
		    	for(int i=0;i<querylist.size();i++){
		    		Object[] obj = (Object[])querylist.get(i);
		    		ExamUserTestpaper examUserTestpaper = (ExamUserTestpaper)obj[0];
		    		Exam exam = (Exam)obj[1];
		    		Employee employee = (Employee)obj[2];
		    		Dept dept = (Dept)obj[3];
		    		Organ organ= (Organ)obj[4];
		    		String oid = organ.getOrganId();
		    		
		    		
		    		ExamUserPassForm examUserPassForm = new ExamUserPassForm();
		    		//examUserPassForm.setSortIndex((i+1)+"");
		    		examUserPassForm.setUserTestpaperId(examUserTestpaper.getUserTestpaperId());
		    		examUserPassForm.setExamId(exam.getExamId());
		    		examUserPassForm.setExamName(exam.getExamName());
		    		examUserPassForm.setUserName(examUserTestpaper.getUserName());//姓名
		    		examUserPassForm.setEmployeeId(employee.getEmployeeId());//人员ID
		    		examUserPassForm.setEmployeeName(employee.getEmployeeName());//人员
		    		examUserPassForm.setUserDeptId(dept.getDeptId());//所属部门ID
		    		examUserPassForm.setUserDeptName(dept.getDeptName());//所属部门
		    		examUserPassForm.setUserGroupId(examUserTestpaper.getUserGroupId());//所属班组ID
		    		examUserPassForm.setUserGroupName(examUserTestpaper.getUserGroupName());//所属班组
		    		examUserPassForm.setPassState(examUserTestpaper.getPassState());//是否合格
		    		examUserPassForm.setScoreStartTime(examUserTestpaper.getScoreStartTime());//成绩有效期（开始时间）
		    		examUserPassForm.setScoreEndTime(examUserTestpaper.getScoreEndTime());//成绩有效期（结束时间）
		    		examUserPassForm.setPublicTime(examUserTestpaper.getPublicTime());//成绩发布时间
		    		
		    		//while(deptId!=null){
			    		/*List deptExamList = (List)deptExamMap.get(deptId);
			    		if(deptExamList==null){
			    			deptExamList = new ArrayList();
			    		}
			    		examUserPassForm.setSortIndex((deptExamList.size()+1)+"");
			    		deptExamList.add(examUserPassForm);
			    		deptExamMap.put(deptId,deptExamList);*/
			    		
			    		if("T".equals(examUserTestpaper.getPassState())){//T -合格  F-不合格
			    			Integer c = (Integer)deptExamMap.get(oid+"_PASS_COUNT");
			    			if(c==null)c = new Integer(0);
			    			deptExamMap.put(oid+"_PASS_COUNT",new Integer(c.intValue()+1));
			    		}else if("F".equals(examUserTestpaper.getPassState())){//T -合格  F-不合格
			    			Integer c = (Integer)deptExamMap.get(oid+"_UNPASS_COUNT");
			    			if(c==null)c = new Integer(0);
			    			deptExamMap.put(oid+"_UNPASS_COUNT",new Integer(c.intValue()+1));
			    		}
			    		Integer c = (Integer)deptExamMap.get(oid+"_COUNT");
		    			if(c==null)c = new Integer(0);
			    		deptExamMap.put(oid+"_COUNT",new Integer(c.intValue()+1));
			    		
			    		
			    		//deptId = (String)parentDeptMap.get(deptId);
		    		//}
		    	}
		    	
		    	
		    	
		 }
		    for(int i=0;i<organList.size();i++){
		    	Organ organ = (Organ)organList.get(i);
	    		
	    		ExamDeptPassForm examDeptPassForm =new ExamDeptPassForm();
	    		examDeptPassForm.setSortIndex((i+1)+"");//序号
	    		//examDeptPassForm.setOrganId();//所属机构ID
	    		//examDeptPassForm.setOrganName();//所属机构
	    		examDeptPassForm.setOrganId(organ.getOrganId());//所属部门ID
	    		examDeptPassForm.setOrganName(organ.getOrganAlias()!=null && !"".equals(organ.getOrganAlias())? organ.getOrganAlias():organ.getOrganName());
	    		
	    		String oid = organ.getOrganId();
	    		
	    		Integer c = (Integer)deptExamMap.get(oid+"_PASS_COUNT");
	    		examDeptPassForm.setPassCount(c!=null?c.intValue()+"" : "0");//合格的数量
	    		c = (Integer)deptExamMap.get(oid+"_UNPASS_COUNT");
	    		examDeptPassForm.setUnPassCount(c!=null?c.intValue()+"" : "0");//不合格的数量
	    		
	    		c = (Integer)deptCountNumMap.get(organ.getOrganId()+"_count");
    			if(c==null)c = new Integer(0);
    			examDeptPassForm.setPassStateCount(c!=null?c.intValue()+"" : "0");
	    		
	    		list.add(examDeptPassForm);
	    	}
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 获取部门的统计信息
	 * @author 朱健
	 * @param organId
	 * @param examId
	 * @param startDay
	 * @param endDay
	 * @return
	 * @modified
	 */
	public List getExamTjxxByUser(String organId,String examId,String startDay,String endDay,String exam_property){
		List list = new ArrayList();
		try{
			//获取部门的顶级部门
			Map parentDeptMap = new HashMap();
			Map<String,List<String>> childDeptMap = new HashMap<String,List<String>>();
			//String sql ="select d.dept_id,d.parent_dept_id,{d.*} "
				//	+ " from dept d where d.organ_id = ? ";
		    //sql+=" start with d.parent_dept_id is null  connect by prior d.dept_id = d.parent_dept_id ";
		  //  sql+= " order SIBLINGS BY d.order_no,d.level_code ";
		    String sql = "select dd.dept_id,dd.parent_dept_id,{dd.*}, "+
					       "sum(decode(e.employee_id, null, 0, 1)) employeenum "+
					  " from (select d.*,rownum as rowsort from dept d "+
					         " where d.organ_id = ?  and d.is_validation = '1' and d.dept_name <> '不在岗' "+
					         " start with d.parent_dept_id is null "+
					        " connect by prior d.dept_id = d.parent_dept_id "+
					         " order SIBLINGS BY d.order_no, d.level_code) dd, "
					         + " (select * from employee ee where ee.isvalidation = '1') e "+
					" where dd.dept_id = e.dept_id(+) "+
					 " group by dd.DEPT_ID, dd.PARENT_DEPT_ID, dd.ORGAN_ID,dd.DEPT_CODE, "+
					          " dd.DEPT_NAME, dd.DEPT_ALIAS,dd.DEPT_TYPE,dd.ORDER_NO, dd.IS_VALIDATION, "+
					          " dd.LEVEL_CODE,dd.DEPT_CHARACTER,dd.REMARK,dd.rowsort "+
					 " order by dd.rowsort";
			Session session = template.getSessionFactory().getCurrentSession();
			SQLQuery sqlQuery=session.createSQLQuery(sql); 
			sqlQuery.setString(0, organId);
			List queryDeptlist = sqlQuery.addScalar("dept_id", StringType.INSTANCE)
					.addScalar("parent_dept_id", StringType.INSTANCE).addEntity("dd", Dept.class)
					.addScalar("employeenum", IntegerType.INSTANCE).list();
			List deptList = new ArrayList();
			Map<String,Integer> deptCountNumMap = new HashMap<String,Integer>();
			for(int i=0;i<queryDeptlist.size();i++){
				Object[] obj = (Object[])queryDeptlist.get(i);
				String dept_id = (String)obj[0];
				String parent_dept_id = (String)obj[1];
				parentDeptMap.put(dept_id, parent_dept_id);	
				
				List childDeptList = (List)childDeptMap.get(parent_dept_id);
				if(childDeptList == null){
					childDeptList = new ArrayList();
				}
				childDeptList.add(dept_id);
				childDeptMap.put(parent_dept_id, childDeptList);
				
				Dept dept = (Dept)obj[2];
				//if(dept.getDept()==null){
					deptList.add(dept);
				//}
				Integer employeenum = (Integer)obj[3];
				deptCountNumMap.put(dept.getDeptId()+"_count", employeenum);
			}
			
			
			/*sql="select e.dept_id, tt.employee_id,count(*) themenum "+
			  "from (select t1.employee_id, "+
			               "t1.theme_bank_id, "+
			               "t1.theme_num, "+
			               "decode(t2.theme_fin_num,null,0,t2.theme_fin_num) as theme_fin_num "+
			          "from personal_bank t1 "+
			          "left join personal_bank_fin t2 "+
			            "on t1.employee_id = t2.employee_id "+
			           "and t1.theme_bank_id = t2.theme_bank_id "+
			           "where  t1.theme_num = t2.theme_fin_num and t1.theme_num>0  and t1.theme_num  is not null and t2.theme_fin_num is not null) tt, "+
			       "employee e "+
			 "where tt.employee_id = e.employee_id "+
			 "group by  e.dept_id, tt.employee_id ";*/
			sql= "select e.dept_id, tt.employee_id, sum(theme_num) as themenum,sum(theme_fin_num) as themefinnum "+
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
					 "where tt.employee_id = e.employee_id and e.isvalidation = 1  "+
					 "and theme_num = theme_fin_num and theme_num>0 "+
					 "group by e.dept_id, tt.employee_id ";
					sqlQuery=session.createSQLQuery(sql);
					List queryDeptXxrslist = sqlQuery.addScalar("dept_id", StringType.INSTANCE)
							.addScalar("themenum", IntegerType.INSTANCE).list();
					for(int i=0;i<queryDeptXxrslist.size();i++){
						Object[] obj = (Object[])queryDeptXxrslist.get(i);
						String dept_id = (String)obj[0];
						//Integer themenum = (Integer)obj[1];
						Integer num = deptCountNumMap.get(dept_id+"_xxrs");
						if(num == null){
							deptCountNumMap.put(dept_id+"_xxrs", 1);
						}else{
							deptCountNumMap.put(dept_id+"_xxrs", num.intValue()+1);
						}
						
					}
					
				sql= "select e.dept_id, tt.employee_id, sum(theme_num) as themenum,sum(theme_fin_num) as themefinnum "+
							 " from (select t1.employee_id, "+
							               "sum(t1.theme_num) as theme_num, "+
							               "sum(decode(t2.theme_fin_num, null, 0, t2.theme_fin_num)) as theme_fin_num "+
							          "from (select pp.* from personal_bank pp,theme_bank tb where   pp.theme_bank_id = tb.theme_bank_id "+
							          	" and (tb.bank_public = '10' or (tb.bank_public = '20' and tb.organ_id ='"+organId+"')) "+
						                 " and tb.is_L = '10')  t1 "+
							          "left join personal_bank_fin t2 "+
							            "on t1.employee_id = t2.employee_id "+
							           "and t1.theme_bank_id = t2.theme_bank_id "+
							         "group by t1.employee_id) tt, "+
							       "employee e "+
							 "where tt.employee_id = e.employee_id and e.isvalidation = 1  "+
							 "and theme_num > theme_fin_num and theme_num>0 and theme_fin_num > 0 "+
							 "group by e.dept_id, tt.employee_id ";
							sqlQuery=session.createSQLQuery(sql);
							queryDeptXxrslist = sqlQuery.addScalar("dept_id", StringType.INSTANCE)
									.addScalar("themenum", IntegerType.INSTANCE).list();
							for(int i=0;i<queryDeptXxrslist.size();i++){
								Object[] obj = (Object[])queryDeptXxrslist.get(i);
								String dept_id = (String)obj[0];
								//Integer themenum = (Integer)obj[1];
								Integer num = deptCountNumMap.get(dept_id+"_zzxxrs");
								if(num == null){
									deptCountNumMap.put(dept_id+"_zzxxrs", 1);
								}else{
									deptCountNumMap.put(dept_id+"_zzxxrs", num.intValue()+1);
								}
								
							}
			
			
			
							//查询个人学习的题库
							Map<String,Integer> employeeXxThemeBankNumMap = new HashMap<String,Integer>();//学员学习对应的题库数目
							Map<String,List<String[]>> employeeXxThemeBankMap = new HashMap<String,List<String[]>>();//学员学习对应的题库数目
							Map<String,String> employeeXxPassThemeBankCodeMap = new HashMap<String,String>();//学员学习通过对应的题库(员工ID+题库ID)
							Map<String,Integer> employeeXxPassThemeBankNumMap = new HashMap<String,Integer>();//学员学习通过对应的题库(员工ID对应通过数目)
							sql = "select b.employee_id,b.theme_bank_id "+
								  " from (select pp.* from personal_bank pp,theme_bank tb where   pp.theme_bank_id = tb.theme_bank_id "+
							          	" and (tb.bank_public = '10' or (tb.bank_public = '20' and tb.organ_id ='"+organId+"')) "+
						                 " and tb.is_L = '10') b, employee e, dept d "+
								  "  where b.employee_id = e.employee_id and e.isvalidation = 1  "+
								  " and e.dept_id = d.dept_id and b.theme_bank_id is not null "+
								  " and d.organ_id = ? ";
							sqlQuery=session.createSQLQuery(sql); 
							sqlQuery.setString(0, organId);
							List querylist1 = sqlQuery.addScalar("employee_id", StringType.INSTANCE)
									.addScalar("theme_bank_id", StringType.INSTANCE).list();
							 if(querylist1!=null && querylist1.size()>0){
							    	for(int i=0;i<querylist1.size();i++){
							    		Object[] obj = (Object[])querylist1.get(i);
							    		String employeeId = (String)obj[0];
							    		String themeBankId = (String)obj[1];
							    		Integer themeBankNum = employeeXxThemeBankNumMap.get(employeeId);
							    		if(themeBankNum == null) {
							    			themeBankNum = new Integer(1);
							    		}else{
							    			themeBankNum = themeBankNum+1;
							    		}
							    		employeeXxThemeBankNumMap.put(employeeId,themeBankNum);
							    		
							    		
							    		List tlist = employeeXxThemeBankMap.get(employeeId);
							    		if(tlist == null) {
							    			tlist = new ArrayList();
							    		}
							    		tlist.add(new String[]{themeBankId,null});
							    		employeeXxThemeBankMap.put(employeeId, tlist);
							    	}
							 }
							 
							 //查询对应的题库信息
							 Map<String,List> examToThemeBankListMap = new HashMap<String,List>();
							 sql = "select t.exam_id, p.theme_bank_id "+
									  " from EXAM t, TESTPAPER_SKEY p "+
									 " where t.testpaper_id = p.testpaper_id "+
									   " and (t.relation_type is null or t.relation_type not like 'mo%') " + 
									   "  and exists(select 1 from EXAM_USER_TESTPAPER tt "
									   + " where tt.exam_id = t.exam_id and tt.organ_id = ?)";
								sqlQuery=session.createSQLQuery(sql); 
								sqlQuery.setString(0, organId);
								List querylist2 = sqlQuery.addScalar("exam_id", StringType.INSTANCE)
										.addScalar("theme_bank_id", StringType.INSTANCE).list();
								 if(querylist2!=null && querylist2.size()>0){
								    	for(int i=0;i<querylist2.size();i++){
								    		Object[] obj = (Object[])querylist2.get(i);
								    		String exam_id = (String)obj[0];
								    		String themeBankId = (String)obj[1];
								    		List tlist = examToThemeBankListMap.get(exam_id);
								    		if(tlist == null) {
								    			tlist = new ArrayList();
								    		}
								    		tlist.add(themeBankId);
								    		examToThemeBankListMap.put(exam_id, tlist);
								    	}
								 }
							
											
							sql = " select {t.*},{ex.*},{ee.*},{dd.*} from exam_user_testpaper t,exam ex,exam_arrange ea,employee ee,dept dd "
									+ " where  t.exam_id = ex.exam_id and ex.exam_arrange_id = ea.exam_arrange_id "
									+ " and ee.employee_id = t.employee_id and ee.dept_id = dd.dept_id and ee.isvalidation = 1  "
									+ " and dd.organ_id = ?  ";
							sql+=" and ea.exam_property = ? " ;
							if(examId!=null && !"".equals(examId)){
								sql+=" and instr(',' || ? || ',' , ',' || t.exam_id || ',') > 0 ";
							}
							if(startDay!=null && !"".equals(startDay) && !"null".equals(startDay)
									&& endDay!=null && !"".equals(endDay) && !"null".equals(endDay)){
								sql+=" and not exists ( select 1 from exam_user_testpaper vv where vv.user_testpaper_id = t.user_testpaper_id "
										+ " and vv.score_end_time < '"+startDay+"' or vv.score_start_time > '"+endDay+"') ";
							}else if(startDay!=null && !"".equals(startDay) && !"null".equals(startDay)){
								sql+=" and not exists ( select 1 from exam_user_testpaper vv where vv.user_testpaper_id = t.user_testpaper_id "
										+ " and vv.score_end_time < '"+startDay+"') ";
							}else if(endDay!=null && !"".equals(endDay) && !"null".equals(endDay)){
								sql+=" and not exists  (select 1 from exam_user_testpaper vv where vv.user_testpaper_id = t.user_testpaper_id "
										+ " and vv.score_start_time > '"+endDay+"') ";
							}
							sql+=" and t.pass_state is not null and t.score_start_time is not null and t.score_end_time is not null "
									+ " order by ex.creation_date desc,t.creation_date desc ";
							sqlQuery=session.createSQLQuery(sql); 
							sqlQuery.setString(0, organId);
							sqlQuery.setString(1, exam_property);
							if(examId!=null && !"".equals(examId)){
								sqlQuery.setString(2, examId);
							}
							List querylist = sqlQuery.addEntity("t", ExamUserTestpaper.class).addEntity("ex", Exam.class)
									.addEntity("ee", Employee.class).addEntity("dd", Dept.class).list();
							Map deptExamMap = new HashMap();
						    if(querylist!=null && querylist.size()>0){
						    	Map employeeIdMap = new HashMap();
						    	for(int i=0;i<querylist.size();i++){
						    		Object[] obj = (Object[])querylist.get(i);
						    		ExamUserTestpaper examUserTestpaper = (ExamUserTestpaper)obj[0];
						    		Exam exam = (Exam)obj[1];
						    		Employee employee = (Employee)obj[2];
						    		Dept dept = (Dept)obj[3];
						    		String deptId = dept.getDeptId();
						    		
						    		
						    		ExamUserPassForm examUserPassForm = new ExamUserPassForm();
						    		//examUserPassForm.setSortIndex((i+1)+"");
						    		examUserPassForm.setUserTestpaperId(examUserTestpaper.getUserTestpaperId());
						    		examUserPassForm.setExamId(exam.getExamId());
						    		examUserPassForm.setExamName(exam.getExamName());
						    		examUserPassForm.setUserName(examUserTestpaper.getUserName());//姓名
						    		examUserPassForm.setEmployeeId(employee.getEmployeeId());//人员ID
						    		examUserPassForm.setEmployeeName(employee.getEmployeeName());//人员
						    		examUserPassForm.setUserDeptId(dept.getDeptId());//所属部门ID
						    		examUserPassForm.setUserDeptName(dept.getDeptName());//所属部门
						    		examUserPassForm.setUserGroupId(examUserTestpaper.getUserGroupId());//所属班组ID
						    		examUserPassForm.setUserGroupName(examUserTestpaper.getUserGroupName());//所属班组
						    		examUserPassForm.setPassState(examUserTestpaper.getPassState());//是否合格
						    		examUserPassForm.setScoreStartTime(examUserTestpaper.getScoreStartTime());//成绩有效期（开始时间）
						    		examUserPassForm.setScoreEndTime(examUserTestpaper.getScoreEndTime());//成绩有效期（结束时间）
						    		examUserPassForm.setPublicTime(examUserTestpaper.getPublicTime());//成绩发布时间
						    		
						    		if("T".equals(examUserTestpaper.getPassState())){//T -合格  F-不合格
						    			List<String> themebanks = examToThemeBankListMap.get(exam.getExamId());
						    			List<String[]> xxThemebanks = employeeXxThemeBankMap.get(employee.getEmployeeId());
						    			if(themebanks!=null && xxThemebanks!=null){
							    			for(int k=0;k<themebanks.size();k++){
							    				for(int t=0;t<xxThemebanks.size();t++){
							    					String[] xxThemeBank = xxThemebanks.get(t);
							    					if(xxThemeBank[0].equals(themebanks.get(k)) 
							    							&& xxThemeBank[1] == null){
							    						xxThemeBank[1] = "T";
							    						xxThemebanks.set(t, xxThemeBank);
							    						employeeXxPassThemeBankCodeMap.put(employee.getEmployeeId()+"_"+themebanks.get(k), "T");
							    						Integer vv = employeeXxPassThemeBankNumMap.get(employee.getEmployeeId());
							    						if(vv == null) {
							    			    			vv = new Integer(1);
							    			    		}else{
							    			    			vv = vv+1;
							    			    		}
							    						employeeXxPassThemeBankNumMap.put(employee.getEmployeeId(),vv);
							    						break;
							    					}
							    				}
							    			}
							    			employeeXxThemeBankMap.put(employee.getEmployeeId(),xxThemebanks);
						    			}
						    		}
						    		
						    		Integer passThemeBankNum = employeeXxPassThemeBankNumMap.get(employee.getEmployeeId());
						    		Integer xxThemeBankNum = employeeXxThemeBankNumMap.get(employee.getEmployeeId());
						    		if(xxThemeBankNum!=null && passThemeBankNum!=null && xxThemeBankNum.intValue()>0
						    				 && xxThemeBankNum.intValue() == passThemeBankNum.intValue()
						    				 && employeeIdMap.get(employee.getEmployeeId())==null){
						    			employeeIdMap.put(employee.getEmployeeId(), "a");
						    			//System.out.println(employee.getEmployeeName());
						    			//System.out.println(xxThemeBankNum.intValue() +"  " + passThemeBankNum.intValue());
						    			//System.out.println(" =================== ");
							    		while(deptId!=null){
								    		/*List deptExamList = (List)deptExamMap.get(deptId);
								    		if(deptExamList==null){
								    			deptExamList = new ArrayList();
								    		}
								    		examUserPassForm.setSortIndex((deptExamList.size()+1)+"");
								    		deptExamList.add(examUserPassForm);
								    		deptExamMap.put(deptId,deptExamList);*/
								    		
								    		/*if("T".equals(examUserTestpaper.getPassState())){//T -合格  F-不合格
								    			Integer c = (Integer)deptExamMap.get(deptId+"_PASS_COUNT");
								    			if(c==null)c = new Integer(0);
								    			deptExamMap.put(deptId+"_PASS_COUNT",new Integer(c.intValue()+1));
								    		}else if("F".equals(examUserTestpaper.getPassState())){//T -合格  F-不合格
								    			Integer c = (Integer)deptExamMap.get(deptId+"_UNPASS_COUNT");
								    			if(c==null)c = new Integer(0);
								    			deptExamMap.put(deptId+"_UNPASS_COUNT",new Integer(c.intValue()+1));
								    		}
								    		Integer c = (Integer)deptExamMap.get(deptId+"_COUNT");
							    			if(c==null)c = new Integer(0);
								    		deptExamMap.put(deptId+"_COUNT",new Integer(c.intValue()+1));*/
							    			Integer c = (Integer)deptExamMap.get(deptId+"_PASS_COUNT");
							    			if(c==null)c = new Integer(0);
							    			deptExamMap.put(deptId+"_PASS_COUNT",new Integer(c.intValue()+1));
								    		
								    		deptId = (String)parentDeptMap.get(deptId);
							    		}
						    		}
						    	} 	
						 }
						    
			ExamDeptPassForm sumExamDeptPassForm =new ExamDeptPassForm();
			sumExamDeptPassForm.setSortIndex("");
			sumExamDeptPassForm.setDeptName("合计");
			sumExamDeptPassForm.setShowDeptName("合计");
		    for(int i=0;i<deptList.size();i++){
	    		Dept dept = (Dept)deptList.get(i);
	    		if(dept.getDeptName() == null){
	    			continue;
	    		}
	    		ExamDeptPassForm examDeptPassForm =new ExamDeptPassForm();
	    		examDeptPassForm.setSortIndex((i+1)+"");//序号
	    		//examDeptPassForm.setOrganId();//所属机构ID
	    		//examDeptPassForm.setOrganName();//所属机构
	    		examDeptPassForm.setDeptId(dept.getDeptId());//所属部门ID
	    		examDeptPassForm.setParentDeptId((String)parentDeptMap.get(dept.getDeptId()));
	    		
	    		String tmp_deptId = dept.getDeptId();
	    		int len = 0;
	    		String kg = "";
	    		while(parentDeptMap.get(tmp_deptId)!=null && len<10){
	    			len++;
	    			kg += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
	    			tmp_deptId = (String)parentDeptMap.get(tmp_deptId);
	    		}
	    		if(len<3){
		    		examDeptPassForm.setDeptName(dept.getDeptName());//所属部门
		    		examDeptPassForm.setShowDeptName(kg+dept.getDeptName());//所属部门
		    		
		    		int sumc = this.countDeptNum(dept.getDeptId(), childDeptMap, deptCountNumMap,"_count");
		    		examDeptPassForm.setPassStateCount(sumc+"");//总数量
		    		
		    		
		    		String deptId = dept.getDeptId();
		    		Integer c = (Integer)deptExamMap.get(deptId+"_PASS_COUNT");
		    		examDeptPassForm.setPassCount(c!=null?c.intValue()+"" : "0");//合格的数量
		    		
		    		//c = (Integer)deptExamMap.get(deptId+"_UNPASS_COUNT");
		    		c = sumc - (c!=null?c.intValue() : 0);
		    		examDeptPassForm.setUnPassCount(c!=null?c.intValue()+"" : "0");//不合格的数量
		    		//c = (Integer)deptExamMap.get(deptId+"_COUNT");
		    		//examDeptPassForm.setPassStateCount(c!=null?c.intValue()+"" : "0");//总数量
		    		
		    		
		    		
		    		if(len == 0){
			    		int xxrs = this.countDeptNum(dept.getDeptId(), childDeptMap, deptCountNumMap,"_xxrs");
			    		examDeptPassForm.setXxrsCount(xxrs+"");//学习人数
			    		
			    		int zzxxrs = this.countDeptNum(dept.getDeptId(), childDeptMap, deptCountNumMap,"_zzxxrs");
			    		examDeptPassForm.setZzxxrsCount(zzxxrs+"");//在学习人数
			    		list.add(examDeptPassForm);
			    		
			    		int ss = sumExamDeptPassForm.getPassStateCount()!=null?Integer.parseInt(sumExamDeptPassForm.getPassStateCount()):0;
			    		sumExamDeptPassForm.setPassStateCount((ss+sumc)+"");
			    		ss = sumExamDeptPassForm.getPassCount()!=null?Integer.parseInt(sumExamDeptPassForm.getPassCount()):0;
			    		sumExamDeptPassForm.setPassCount((ss+Integer.parseInt(examDeptPassForm.getPassCount()))+"");
			    		ss = sumExamDeptPassForm.getUnPassCount()!=null?Integer.parseInt(sumExamDeptPassForm.getUnPassCount()):0;
			    		sumExamDeptPassForm.setUnPassCount((ss+Integer.parseInt(examDeptPassForm.getUnPassCount()))+"");
			    		ss = sumExamDeptPassForm.getXxrsCount()!=null?Integer.parseInt(sumExamDeptPassForm.getXxrsCount()):0;
			    		sumExamDeptPassForm.setXxrsCount((ss+Integer.parseInt(examDeptPassForm.getXxrsCount()))+"");	
			    		ss = sumExamDeptPassForm.getZzxxrsCount()!=null?Integer.parseInt(sumExamDeptPassForm.getZzxxrsCount()):0;
			    		sumExamDeptPassForm.setZzxxrsCount((ss+Integer.parseInt(examDeptPassForm.getZzxxrsCount()))+"");
		    		}
	    		}
	    	}
		    
		    list.add(sumExamDeptPassForm);
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}
	
	
	private int countDeptNum(String deptId,Map<String,List<String>> childDeptMap,Map<String,Integer> deptCountNumMap,String mapCs){
		int deptNum = 0;
		Integer cc = (Integer)deptCountNumMap.get(deptId+mapCs);//deptExamMap.get(deptId+"_COUNT");
		if(cc==null)cc = new Integer(0);
		deptNum = cc.intValue();
		List<String> childList = childDeptMap.get(deptId);
		if(childList != null && childList.size()>0){
			for(int i=0;i<childList.size();i++){
				String childDeptId = childList.get(i);
				deptNum+=countDeptNum(childDeptId,childDeptMap,deptCountNumMap,mapCs);
			}
		}
		return deptNum;
	}
	
	
	
	/**
	 * 获取部门的统计信息
	 * @author 朱健
	 * @param organId
	 * @param examId
	 * @param startDay
	 * @param endDay
	 * @return
	 * @modified
	 */
	public List getExamDeptTjxxByUser(String topDeptId,String examId,String startDay,String endDay,String exam_property){
		List list = new ArrayList();
		try{
			//获取部门的顶级部门
			Map parentDeptMap = new HashMap();
			Map<String,List<String>> childDeptMap = new HashMap<String,List<String>>();
			//String sql ="select d.dept_id,d.parent_dept_id,{d.*} "
				//	+ " from dept d  ";
		  //  sql+=" start with d.dept_id = ?  connect by prior d.dept_id = d.parent_dept_id ";
		   // sql+= " order SIBLINGS BY d.order_no,d.level_code ";
			 String sql = "select dd.dept_id,dd.parent_dept_id,{dd.*}, "+
				       "sum(decode(e.employee_id, null, 0, 1)) employeenum "+
				  " from (select d.*,rownum as rowsort from dept d "+
				         " where d.is_validation = '1' "+
				         " start with d.dept_id = ?  "+
				        " connect by prior d.dept_id = d.parent_dept_id "+
				         " order SIBLINGS BY d.order_no, d.level_code) dd, "
				         + " (select * from employee ee where ee.isvalidation = '1') e "+
				" where dd.dept_id = e.dept_id(+) "+
				 " group by dd.DEPT_ID, dd.PARENT_DEPT_ID, dd.ORGAN_ID,dd.DEPT_CODE, "+
				          " dd.DEPT_NAME, dd.DEPT_ALIAS,dd.DEPT_TYPE,dd.ORDER_NO, dd.IS_VALIDATION, "+
				          " dd.LEVEL_CODE,dd.DEPT_CHARACTER,dd.REMARK,dd.rowsort "+
				 " order by dd.rowsort";
			Session session = template.getSessionFactory().getCurrentSession();
			SQLQuery sqlQuery=session.createSQLQuery(sql); 
			sqlQuery.setString(0, topDeptId);
			List queryDeptlist = sqlQuery.addScalar("dept_id", StringType.INSTANCE)
					.addScalar("parent_dept_id", StringType.INSTANCE).addEntity("dd", Dept.class)
					.addScalar("employeenum", IntegerType.INSTANCE).list();
			List deptList = new ArrayList();
			String organId = null;
			Map<String,Integer> deptCountNumMap = new HashMap<String,Integer>();
			for(int i=0;i<queryDeptlist.size();i++){
				Object[] obj = (Object[])queryDeptlist.get(i);
				String dept_id = (String)obj[0];
				String parent_dept_id = (String)obj[1];
				parentDeptMap.put(dept_id, parent_dept_id);	
				
				List childDeptList = (List)childDeptMap.get(parent_dept_id);
				if(childDeptList == null){
					childDeptList = new ArrayList();
				}
				childDeptList.add(dept_id);
				childDeptMap.put(parent_dept_id, childDeptList);
				
				Dept dept = (Dept)obj[2];
				if(organId == null)organId = dept.getOrgan().getOrganId();
				//if(dept.getDept()==null){
					deptList.add(dept);
				//}
				Integer employeenum = (Integer)obj[3];
				deptCountNumMap.put(dept.getDeptId()+"_count", employeenum);
			}
			
			
			/*sql="select e.dept_id, tt.employee_id,count(*) themenum "+
					  "from (select t1.employee_id, "+
					               "t1.theme_bank_id, "+
					               "t1.theme_num, "+
					               "decode(t2.theme_fin_num,null,0,t2.theme_fin_num) as theme_fin_num "+
					          "from personal_bank t1 "+
					          "left join personal_bank_fin t2 "+
					            "on t1.employee_id = t2.employee_id "+
					           "and t1.theme_bank_id = t2.theme_bank_id "+
					           "where  t1.theme_num = t2.theme_fin_num and t1.theme_num>0  and t1.theme_num  is not null and t2.theme_fin_num is not null) tt, "+
					       "employee e "+
					 "where tt.employee_id = e.employee_id "+
					 "group by  e.dept_id, tt.employee_id ";*/
			sql= "select e.dept_id, tt.employee_id, sum(theme_num) as themenum,sum(theme_fin_num) as themefinnum "+
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
					 "and theme_num = theme_fin_num and theme_num>0 "+
					 "group by e.dept_id, tt.employee_id ";
					sqlQuery=session.createSQLQuery(sql);
					List queryDeptXxrslist = sqlQuery.addScalar("dept_id", StringType.INSTANCE)
							.addScalar("themenum", IntegerType.INSTANCE).list();
					for(int i=0;i<queryDeptXxrslist.size();i++){
						Object[] obj = (Object[])queryDeptXxrslist.get(i);
						String dept_id = (String)obj[0];
						//Integer themenum = (Integer)obj[1];
						Integer num = deptCountNumMap.get(dept_id+"_xxrs");
						if(num == null){
							deptCountNumMap.put(dept_id+"_xxrs", 1);
						}else{
							deptCountNumMap.put(dept_id+"_xxrs", num.intValue()+1);
						}
						
					}
					
				sql= "select e.dept_id, tt.employee_id, sum(theme_num) as themenum,sum(theme_fin_num) as themefinnum "+
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
							 "and theme_num > theme_fin_num and theme_num>0 and theme_fin_num > 0 "+
							 "group by e.dept_id, tt.employee_id ";
							sqlQuery=session.createSQLQuery(sql);
							queryDeptXxrslist = sqlQuery.addScalar("dept_id", StringType.INSTANCE)
									.addScalar("themenum", IntegerType.INSTANCE).list();
							for(int i=0;i<queryDeptXxrslist.size();i++){
								Object[] obj = (Object[])queryDeptXxrslist.get(i);
								String dept_id = (String)obj[0];
								//Integer themenum = (Integer)obj[1];
								Integer num = deptCountNumMap.get(dept_id+"_zzxxrs");
								if(num == null){
									deptCountNumMap.put(dept_id+"_zzxxrs", 1);
								}else{
									deptCountNumMap.put(dept_id+"_zzxxrs", num.intValue()+1);
								}
								
							}
							
			//查询个人学习的题库
			Map<String,Integer> employeeXxThemeBankNumMap = new HashMap<String,Integer>();//学员学习对应的题库数目
			Map<String,List<String[]>> employeeXxThemeBankMap = new HashMap<String,List<String[]>>();//学员学习对应的题库数目
			Map<String,String> employeeXxPassThemeBankCodeMap = new HashMap<String,String>();//学员学习通过对应的题库(员工ID+题库ID)
			Map<String,Integer> employeeXxPassThemeBankNumMap = new HashMap<String,Integer>();//学员学习通过对应的题库(员工ID对应通过数目)
			sql = "select b.employee_id,b.theme_bank_id "+
				  " from (select pp.* from personal_bank pp,theme_bank tb where   pp.theme_bank_id = tb.theme_bank_id "+
							          	" and (tb.bank_public = '10' or (tb.bank_public = '20' and tb.organ_id ='"+organId+"')) "+
						                 " and tb.is_L = '10') b, employee e, dept d "+
				  "  where b.employee_id = e.employee_id "+
				  " and e.dept_id = d.dept_id and b.theme_bank_id is not null "+
				  " and d.organ_id = ? ";
			sqlQuery=session.createSQLQuery(sql); 
			sqlQuery.setString(0, organId);
			List querylist1 = sqlQuery.addScalar("employee_id", StringType.INSTANCE)
					.addScalar("theme_bank_id", StringType.INSTANCE).list();
			 if(querylist1!=null && querylist1.size()>0){
			    	for(int i=0;i<querylist1.size();i++){
			    		Object[] obj = (Object[])querylist1.get(i);
			    		String employeeId = (String)obj[0];
			    		String themeBankId = (String)obj[1];
			    		Integer themeBankNum = employeeXxThemeBankNumMap.get(employeeId);
			    		if(themeBankNum == null) {
			    			themeBankNum = new Integer(1);
			    		}else{
			    			themeBankNum = themeBankNum+1;
			    		}
			    		employeeXxThemeBankNumMap.put(employeeId,themeBankNum);
			    		
			    		
			    		List tlist = employeeXxThemeBankMap.get(employeeId);
			    		if(tlist == null) {
			    			tlist = new ArrayList();
			    		}
			    		tlist.add(new String[]{themeBankId,null});
			    		employeeXxThemeBankMap.put(employeeId, tlist);
			    	}
			 }
			 
			 //查询对应的题库信息
			 Map<String,List> examToThemeBankListMap = new HashMap<String,List>();
			 sql = "select t.exam_id, p.theme_bank_id "+
					  " from EXAM t, TESTPAPER_SKEY p "+
					 " where t.testpaper_id = p.testpaper_id "+
					   " and (t.relation_type is null or t.relation_type not like 'mo%') " + 
					   "  and exists(select 1 from EXAM_USER_TESTPAPER tt "
					   + " where tt.exam_id = t.exam_id and tt.organ_id = ?)";
				sqlQuery=session.createSQLQuery(sql); 
				sqlQuery.setString(0, organId);
				List querylist2 = sqlQuery.addScalar("exam_id", StringType.INSTANCE)
						.addScalar("theme_bank_id", StringType.INSTANCE).list();
				 if(querylist2!=null && querylist2.size()>0){
				    	for(int i=0;i<querylist2.size();i++){
				    		Object[] obj = (Object[])querylist2.get(i);
				    		String exam_id = (String)obj[0];
				    		String themeBankId = (String)obj[1];
				    		List tlist = examToThemeBankListMap.get(exam_id);
				    		if(tlist == null) {
				    			tlist = new ArrayList();
				    		}
				    		tlist.add(themeBankId);
				    		examToThemeBankListMap.put(exam_id, tlist);
				    	}
				 }
			
							
			sql = " select {t.*},{ex.*},{ee.*},{dd.*} from exam_user_testpaper t,exam ex,exam_arrange ea,employee ee,dept dd "
					+ " where  t.exam_id = ex.exam_id and ex.exam_arrange_id = ea.exam_arrange_id "
					+ " and ee.employee_id = t.employee_id and ee.dept_id = dd.dept_id "
					+ " and dd.organ_id = ?  ";
			sql+=" and ea.exam_property = ? " ;
			if(examId!=null && !"".equals(examId)){
				sql+=" and instr(',' || ? || ',' , ',' || t.exam_id || ',') > 0 ";
			}
			if(startDay!=null && !"".equals(startDay) && !"null".equals(startDay)
					&& endDay!=null && !"".equals(endDay) && !"null".equals(endDay)){
				sql+=" and not exists ( select 1 from exam_user_testpaper vv where vv.user_testpaper_id = t.user_testpaper_id "
						+ " and vv.score_end_time < '"+startDay+"' or vv.score_start_time > '"+endDay+"') ";
			}else if(startDay!=null && !"".equals(startDay) && !"null".equals(startDay)){
				sql+=" and not exists ( select 1 from exam_user_testpaper vv where vv.user_testpaper_id = t.user_testpaper_id "
						+ " and vv.score_end_time < '"+startDay+"') ";
			}else if(endDay!=null && !"".equals(endDay) && !"null".equals(endDay)){
				sql+=" and not exists  (select 1 from exam_user_testpaper vv where vv.user_testpaper_id = t.user_testpaper_id "
						+ " and vv.score_start_time > '"+endDay+"') ";
			}
			sql+=" and t.pass_state is not null and t.score_start_time is not null and t.score_end_time is not null "
					+ " order by ex.creation_date desc,t.creation_date desc ";
			sqlQuery=session.createSQLQuery(sql); 
			sqlQuery.setString(0, organId);
			sqlQuery.setString(1, exam_property);
			if(examId!=null && !"".equals(examId)){
				sqlQuery.setString(2, examId);
			}
			List querylist = sqlQuery.addEntity("t", ExamUserTestpaper.class).addEntity("ex", Exam.class)
					.addEntity("ee", Employee.class).addEntity("dd", Dept.class).list();
			Map deptExamMap = new HashMap();
		    if(querylist!=null && querylist.size()>0){
		    	Map employeeIdMap = new HashMap();
		    	for(int i=0;i<querylist.size();i++){
		    		Object[] obj = (Object[])querylist.get(i);
		    		ExamUserTestpaper examUserTestpaper = (ExamUserTestpaper)obj[0];
		    		Exam exam = (Exam)obj[1];
		    		Employee employee = (Employee)obj[2];
		    		Dept dept = (Dept)obj[3];
		    		String deptId = dept.getDeptId();
		    		
		    		
		    		ExamUserPassForm examUserPassForm = new ExamUserPassForm();
		    		//examUserPassForm.setSortIndex((i+1)+"");
		    		examUserPassForm.setUserTestpaperId(examUserTestpaper.getUserTestpaperId());
		    		examUserPassForm.setExamId(exam.getExamId());
		    		examUserPassForm.setExamName(exam.getExamName());
		    		examUserPassForm.setUserName(examUserTestpaper.getUserName());//姓名
		    		examUserPassForm.setEmployeeId(employee.getEmployeeId());//人员ID
		    		examUserPassForm.setEmployeeName(employee.getEmployeeName());//人员
		    		examUserPassForm.setUserDeptId(dept.getDeptId());//所属部门ID
		    		examUserPassForm.setUserDeptName(dept.getDeptName());//所属部门
		    		examUserPassForm.setUserGroupId(examUserTestpaper.getUserGroupId());//所属班组ID
		    		examUserPassForm.setUserGroupName(examUserTestpaper.getUserGroupName());//所属班组
		    		examUserPassForm.setPassState(examUserTestpaper.getPassState());//是否合格
		    		examUserPassForm.setScoreStartTime(examUserTestpaper.getScoreStartTime());//成绩有效期（开始时间）
		    		examUserPassForm.setScoreEndTime(examUserTestpaper.getScoreEndTime());//成绩有效期（结束时间）
		    		examUserPassForm.setPublicTime(examUserTestpaper.getPublicTime());//成绩发布时间
		    		
		    		if("T".equals(examUserTestpaper.getPassState())){//T -合格  F-不合格
		    			List<String> themebanks = examToThemeBankListMap.get(exam.getExamId());
		    			List<String[]> xxThemebanks = employeeXxThemeBankMap.get(employee.getEmployeeId());
		    			if(themebanks!=null && xxThemebanks!=null){
			    			for(int k=0;k<themebanks.size();k++){
			    				for(int t=0;t<xxThemebanks.size();t++){
			    					String[] xxThemeBank = xxThemebanks.get(t);
			    					if(xxThemeBank[0].equals(themebanks.get(k)) 
			    							&& xxThemeBank[1] == null){
			    						xxThemeBank[1] = "T";
			    						xxThemebanks.set(t, xxThemeBank);
			    						employeeXxPassThemeBankCodeMap.put(employee.getEmployeeId()+"_"+themebanks.get(k), "T");
			    						Integer vv = employeeXxPassThemeBankNumMap.get(employee.getEmployeeId());
			    						if(vv == null) {
			    			    			vv = new Integer(1);
			    			    		}else{
			    			    			vv = vv+1;
			    			    		}
			    						employeeXxPassThemeBankNumMap.put(employee.getEmployeeId(),vv);
			    						break;
			    					}
			    				}
			    			}
			    			employeeXxThemeBankMap.put(employee.getEmployeeId(),xxThemebanks);
		    			}
		    		}
		    		
		    		Integer passThemeBankNum = employeeXxPassThemeBankNumMap.get(employee.getEmployeeId());
		    		Integer xxThemeBankNum = employeeXxThemeBankNumMap.get(employee.getEmployeeId());
		    		if(xxThemeBankNum!=null && passThemeBankNum!=null && xxThemeBankNum.intValue()>0
		    				 && xxThemeBankNum.intValue() == passThemeBankNum.intValue()
		    				 && employeeIdMap.get(employee.getEmployeeId())==null){
		    			employeeIdMap.put(employee.getEmployeeId(), "a");
		    			//System.out.println(employee.getEmployeeName());
		    			//System.out.println(xxThemeBankNum.intValue() +"  " + passThemeBankNum.intValue());
		    			//System.out.println(" =================== ");
			    		while(deptId!=null){
				    		/*List deptExamList = (List)deptExamMap.get(deptId);
				    		if(deptExamList==null){
				    			deptExamList = new ArrayList();
				    		}
				    		examUserPassForm.setSortIndex((deptExamList.size()+1)+"");
				    		deptExamList.add(examUserPassForm);
				    		deptExamMap.put(deptId,deptExamList);*/
				    		
				    		/*if("T".equals(examUserTestpaper.getPassState())){//T -合格  F-不合格
				    			Integer c = (Integer)deptExamMap.get(deptId+"_PASS_COUNT");
				    			if(c==null)c = new Integer(0);
				    			deptExamMap.put(deptId+"_PASS_COUNT",new Integer(c.intValue()+1));
				    		}else if("F".equals(examUserTestpaper.getPassState())){//T -合格  F-不合格
				    			Integer c = (Integer)deptExamMap.get(deptId+"_UNPASS_COUNT");
				    			if(c==null)c = new Integer(0);
				    			deptExamMap.put(deptId+"_UNPASS_COUNT",new Integer(c.intValue()+1));
				    		}
				    		Integer c = (Integer)deptExamMap.get(deptId+"_COUNT");
			    			if(c==null)c = new Integer(0);
				    		deptExamMap.put(deptId+"_COUNT",new Integer(c.intValue()+1));*/
			    			Integer c = (Integer)deptExamMap.get(deptId+"_PASS_COUNT");
			    			if(c==null)c = new Integer(0);
			    			deptExamMap.put(deptId+"_PASS_COUNT",new Integer(c.intValue()+1));
				    		
				    		deptId = (String)parentDeptMap.get(deptId);
			    		}
		    		}
		    	}
		    	
		    	
		    	
		 }
		    
		    
			ExamDeptPassForm sumExamDeptPassForm =new ExamDeptPassForm();
			sumExamDeptPassForm.setSortIndex("");
			sumExamDeptPassForm.setDeptName("合计");
			sumExamDeptPassForm.setShowDeptName("合计");
		    for(int i=0;i<deptList.size();i++){
	    		Dept dept = (Dept)deptList.get(i);
	    		ExamDeptPassForm examDeptPassForm =new ExamDeptPassForm();
	    		examDeptPassForm.setSortIndex((i+1)+"");//序号
	    		//examDeptPassForm.setOrganId();//所属机构ID
	    		//examDeptPassForm.setOrganName();//所属机构
	    		examDeptPassForm.setDeptId(dept.getDeptId());//所属部门ID
	    		
	    		
	    		String tmp_deptId = dept.getDeptId();
	    		int len = 0;
	    		String kg = "";
	    		while(parentDeptMap.get(tmp_deptId)!=null && len<10){
	    			len++;
	    			kg += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
	    			tmp_deptId = (String)parentDeptMap.get(tmp_deptId);
	    		}
	    		if(len<3){
		    		examDeptPassForm.setDeptName(dept.getDeptName());//所属部门
		    		examDeptPassForm.setShowDeptName(kg+dept.getDeptName());//所属部门
		    		
		    		int sumc = this.countDeptNum(dept.getDeptId(), childDeptMap, deptCountNumMap,"_count");
		    		examDeptPassForm.setPassStateCount(sumc+"");//总数量
		    		
		    		
		    		String deptId = dept.getDeptId();
		    		Integer c = (Integer)deptExamMap.get(deptId+"_PASS_COUNT");
		    		examDeptPassForm.setPassCount(c!=null?c.intValue()+"" : "0");//合格的数量
		    		
		    		//c = (Integer)deptExamMap.get(deptId+"_UNPASS_COUNT");
		    		c = sumc - (c!=null?c.intValue() : 0);
		    		examDeptPassForm.setUnPassCount(c!=null?c.intValue()+"" : "0");//不合格的数量
		    		//c = (Integer)deptExamMap.get(deptId+"_COUNT");
		    		//examDeptPassForm.setPassStateCount(c!=null?c.intValue()+"" : "0");//总数量
		    		
		    		
		    		
		    		
		    		int xxrs = this.countDeptNum(dept.getDeptId(), childDeptMap, deptCountNumMap,"_xxrs");
		    		examDeptPassForm.setXxrsCount(xxrs+"");//学习人数
		    		
		    		int zzxxrs = this.countDeptNum(dept.getDeptId(), childDeptMap, deptCountNumMap,"_zzxxrs");
		    		examDeptPassForm.setZzxxrsCount(zzxxrs+"");//在学习人数
		    		list.add(examDeptPassForm);
		    		
		    		if(len == 0){
			    		int ss = sumExamDeptPassForm.getPassStateCount()!=null?Integer.parseInt(sumExamDeptPassForm.getPassStateCount()):0;
			    		sumExamDeptPassForm.setPassStateCount((ss+sumc)+"");
			    		ss = sumExamDeptPassForm.getPassCount()!=null?Integer.parseInt(sumExamDeptPassForm.getPassCount()):0;
			    		sumExamDeptPassForm.setPassCount((ss+Integer.parseInt(examDeptPassForm.getPassCount()))+"");
			    		ss = sumExamDeptPassForm.getUnPassCount()!=null?Integer.parseInt(sumExamDeptPassForm.getUnPassCount()):0;
			    		sumExamDeptPassForm.setUnPassCount((ss+Integer.parseInt(examDeptPassForm.getUnPassCount()))+"");
			    		ss = sumExamDeptPassForm.getXxrsCount()!=null?Integer.parseInt(sumExamDeptPassForm.getXxrsCount()):0;
			    		sumExamDeptPassForm.setXxrsCount((ss+Integer.parseInt(examDeptPassForm.getXxrsCount()))+"");	
			    		ss = sumExamDeptPassForm.getZzxxrsCount()!=null?Integer.parseInt(sumExamDeptPassForm.getZzxxrsCount()):0;
			    		sumExamDeptPassForm.setZzxxrsCount((ss+Integer.parseInt(examDeptPassForm.getZzxxrsCount()))+"");
		    		}
		    		
	    		}
	    	}
		    list.add(sumExamDeptPassForm);
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}
	
	private void setDeptChild(List allChild,Dept dept){
		List child = dept.getDepts();
		if(child!=null && child.size()>0){
			allChild.addAll(child);
			for(int i=0;i<child.size();i++){
				Dept childDept = (Dept)child.get(i);
				setDeptChild(allChild,childDept);
			}
		}
	}
	
	/**
	 * 获取考试成绩等信息
	 * @author 朱健
	 * @param id
	 * @param type
	 * @param examId
	 * @return
	 * @modified
	 */
	public List getExamUserInfos(String id,String type,String examId,String startDay,String endDay,String exam_property){
		List list = new ArrayList();
		try{
			Map employeeInfos = null;
			String sql = null;
			if("dept".equals(type)){
				Dept dept =  (Dept)this.findEntityBykey(Dept.class, id);
				List deptAll = new ArrayList();
				deptAll.add(dept);
				setDeptChild(deptAll,dept);
				String deptIds = "";
				for(int i=0;i<deptAll.size();i++){
					dept = (Dept)deptAll.get(i);
					deptIds+="'"+dept.getDeptId()+"',";
				}
				deptIds=deptIds.substring(0,deptIds.length()-1);
				sql = " select {t.*},{ex.*} from exam_user_testpaper t,exam ex,exam_arrange ea "
						+ " where  t.exam_id = ex.exam_id and ex.exam_arrange_id = ea.exam_arrange_id "
						+ " and t.employee_id in ("+ 
					   "select e.employee_id from employee e, quarter q, dept d, organ o "+
					   " where e.quarter_id = q.quarter_id "+
					   " and e.dept_id = d.dept_id "+
					   " and d.organ_id = o.organ_id "+ 
					   " and e.isvalidation = 1 "
					+ " and e.dept_id in ("+deptIds+") ) ";
			}else if("quarter".equals(type) && id.indexOf("@")!=-1){
				String[] ss = id.split("@");
				sql = " select {t.*},{ex.*} from exam_user_testpaper t,exam ex,exam_arrange ea "
						+ " where  t.exam_id = ex.exam_id and ex.exam_arrange_id = ea.exam_arrange_id "
						+ " and  t.employee_id in ("+ 
			          " select e.employee_id from employee e, quarter q, dept d, organ o "
						+ " where e.quarter_id = q.quarter_id "+
					   " and e.dept_id = d.dept_id "+
					   " and d.organ_id = o.organ_id "
					   + " and e.isvalidation = 1 "
						+ " and e.quarter_train_id = '"+ss[0]+"' and e.dept_id = '"+ss[1]+"' )";
			}else if("quarter".equals(type)){
				sql = " select {t.*},{ex.*} from exam_user_testpaper t,exam ex,exam_arrange ea "
						+ " where  t.exam_id = ex.exam_id and ex.exam_arrange_id = ea.exam_arrange_id "
						+ " and  t.employee_id in ("+ 
			          " select e.employee_id from employee e, quarter q, dept d, organ o "
						+ " where e.quarter_id = q.quarter_id "+
					   " and e.dept_id = d.dept_id "+
					   " and d.organ_id = o.organ_id "
					   + " and e.isvalidation = 1 "
						+ " and e.quarter_id = '"+id+"' )";
			}else if("organ".equals(type)){
				sql = " select {t.*},{ex.*} from exam_user_testpaper t,exam ex,exam_arrange ea "
						+ " where  t.exam_id = ex.exam_id and ex.exam_arrange_id = ea.exam_arrange_id "
						+ " and t.employee_id in ("+ 
					   "select e.employee_id from employee e, dept d  "+
					   " where  e.dept_id = d.dept_id   "+ 
					   " and e.isvalidation = 1 "
					+ " and d.organ_id = '"+id+"' ) ";
			}else{
				return list;
			}
			sql+=" and ea.exam_property = ? " ;
			if(examId!=null && !"".equals(examId)){
				sql+=" and instr(',' || ? || ',' , ',' || t.exam_id || ',') > 0 ";
			}
			if(startDay!=null && !"".equals(startDay) && !"null".equals(startDay)
					&& endDay!=null && !"".equals(endDay) && !"null".equals(endDay)){
				sql+=" and not exists ( select 1 from exam_user_testpaper vv where vv.user_testpaper_id = t.user_testpaper_id "
						+ " and vv.score_end_time < '"+startDay+"' or vv.score_start_time > '"+endDay+"') ";
			}else if(startDay!=null && !"".equals(startDay) && !"null".equals(startDay)){
				sql+=" and not exists ( select 1 from exam_user_testpaper vv where vv.user_testpaper_id = t.user_testpaper_id "
						+ " and vv.score_end_time < '"+startDay+"') ";
			}else if(endDay!=null && !"".equals(endDay) && !"null".equals(endDay)){
				sql+=" and not exists  (select 1 from exam_user_testpaper vv where vv.user_testpaper_id = t.user_testpaper_id "
						+ " and vv.score_start_time > '"+endDay+"') ";
			}
			sql+=" and t.pass_state is not null and t.score_start_time is not null and t.score_end_time is not null "
					+ " order by ex.creation_date desc,t.creation_date desc ";
			if(sql!=null){
				Session session = template.getSessionFactory().getCurrentSession();
				SQLQuery sqlQuery=session.createSQLQuery(sql);   
				sqlQuery.setString(0, exam_property);
				if(examId!=null && !"".equals(examId)){
					sqlQuery.setString(1, examId);
				}
			    List querylist = sqlQuery.addEntity("t", ExamUserTestpaper.class).addEntity("ex", Exam.class).list();
			    if(querylist!=null && querylist.size()>0){
			    	for(int i=0;i<querylist.size();i++){
			    		if(employeeInfos == null)employeeInfos = this.getEmployeeInfoMap();
			    		Object[] obj = (Object[])querylist.get(i);
			    		ExamUserTestpaper examUserTestpaper = (ExamUserTestpaper)obj[0];
			    		Exam exam = (Exam)obj[1];
			    		ExamUserPassForm examUserPassForm = new ExamUserPassForm();
			    		examUserPassForm.setSortIndex((i+1)+"");
			    		Employee employee = (Employee)employeeInfos.get(examUserTestpaper.getEmployeeId()+"_employee");
			    		Quarter quarter = (Quarter)employeeInfos.get(examUserTestpaper.getEmployeeId()+"_quarter");
			    		Dept dept = (Dept)employeeInfos.get(examUserTestpaper.getEmployeeId()+"_dept");
			    		Organ organ = (Organ)employeeInfos.get(examUserTestpaper.getEmployeeId()+"_organ");
			    		
			    		examUserPassForm.setUserTestpaperId(examUserTestpaper.getUserTestpaperId());
			    		examUserPassForm.setExamId(exam.getExamId());
			    		examUserPassForm.setExamName(exam.getExamName());
			    		examUserPassForm.setUserName(examUserTestpaper.getUserName());//姓名
			    		examUserPassForm.setEmployeeId(examUserTestpaper.getEmployeeId());//人员ID
			    		examUserPassForm.setEmployeeName(examUserTestpaper.getEmployeeName());//人员
			    		examUserPassForm.setUserOrganId(organ.getOrganId());//所属机构ID
			    		examUserPassForm.setUserOrganName(organ.getOrganName());//所属机构
			    		examUserPassForm.setUserDeptId(dept.getDeptId());//所属部门ID
			    		examUserPassForm.setUserDeptName(dept.getDeptName());//所属部门
			    		examUserPassForm.setQuarterId(quarter.getQuarterId());
			    		examUserPassForm.setQuarterName(quarter.getQuarterName());
			    		examUserPassForm.setUserGroupId(examUserTestpaper.getUserGroupId());//所属班组ID
			    		examUserPassForm.setUserGroupName(examUserTestpaper.getUserGroupName());//所属班组
			    		examUserPassForm.setPassState(examUserTestpaper.getPassState());//是否合格
			    		examUserPassForm.setScoreStartTime(examUserTestpaper.getScoreStartTime());//成绩有效期（开始时间）
			    		examUserPassForm.setScoreEndTime(examUserTestpaper.getScoreEndTime());//成绩有效期（结束时间）
			    		examUserPassForm.setPublicTime(examUserTestpaper.getPublicTime());//成绩发布时间
			    		list.add(examUserPassForm);
			    	}
			    }
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}
	
	
	
	/**
	 * 获取考试管理的考生信息树
	 * @description
	 * @param publicId
	 * @param examId
	 * @return
	 * @modified
	 */
	public List<TreeNode> getExamineeTree(String publicId,String examId){
		List<TreeNode> list = new ArrayList<TreeNode>();
		try{
			Session session = template.getSessionFactory().getCurrentSession();
			String sql = "select t.user_id,t.user_name,o.organ_id,o.organ_name,qs.quarter_id,"+
					 " qs.quarter_name,v.user_testpaper_id "+
						  "from (select tt.* from EXAM_PUBLIC_USER tt where tt.public_id = ?) t, "+
						       "employee e, "+
						       "dept d, "+
						       "organ o, "+
						       "quarter_standard qs, "+
						       "(select * from EXAM_USER_TESTPAPER et where et.exam_id = ?) v "+
						 "where t.employee_id = v.employee_id(+) "+
						   "and t.employee_id = e.employee_id "+
						   "and e.dept_id = d.dept_id "+
						   "and d.organ_id = o.organ_id "+
						   "and e.quarter_train_id = qs.quarter_id "+
						   "order by o.order_no,o.organ_code,qs.sort_num,qs.quarter_code, "+
						   "d.order_no,d.dept_code,e.order_no,e.employee_code ";
			SQLQuery sqlQuery=session.createSQLQuery(sql);
			sqlQuery.setString(0, publicId);
			sqlQuery.setString(1, examId);
		    List<Object[]> tmplist = sqlQuery.addScalar("user_id", StringType.INSTANCE)
				.addScalar("user_name", StringType.INSTANCE)
				.addScalar("organ_id", StringType.INSTANCE)
				.addScalar("organ_name", StringType.INSTANCE)
				.addScalar("quarter_id", StringType.INSTANCE)
				.addScalar("quarter_name", StringType.INSTANCE)
				.addScalar("user_testpaper_id", StringType.INSTANCE)
				.list();
		    Map<String,TreeNode> treeNodeMap = new HashMap<String,TreeNode>();
		    for(int i=0;i<tmplist.size();i++){
		    	Object[] object = (Object[])tmplist.get(i);
		    	String user_id = (String)object[0];
		    	String user_name = (String)object[1];
		    	String organ_id = (String)object[2];
		    	String organ_name = (String)object[3];
		    	String quarter_id = (String)object[4];
		    	String quarter_name = (String)object[5];
		    	String user_testpaper_id = (String)object[6];
		    	
		    	TreeNode parentNode = null;
		    	String pnodeId = "";
		    	if(user_testpaper_id == null || "".equals(user_testpaper_id)){
		    		pnodeId = "unExaminee";
		    		parentNode = treeNodeMap.get(pnodeId);
		    		if(parentNode == null){
			    		parentNode = new TreeNode();
						parentNode.setId(pnodeId);
						parentNode.setTitle("未选择的考生");
						parentNode.setType("parentN");
						//parentNode.setParentId(dept.getDeptId());
						parentNode.setChildren(new ArrayList());
						list.add(parentNode);
						treeNodeMap.put(pnodeId, parentNode);
		    		}
		    	}else{
		    		pnodeId = "examinee";
		    		parentNode = treeNodeMap.get(pnodeId);
		    		if(parentNode == null){
			    		parentNode = new TreeNode();
						parentNode.setId(pnodeId);
						parentNode.setTitle("已选择的考生");
						parentNode.setType("parentN");
						//parentNode.setParentId(dept.getDeptId());
						parentNode.setChildren(new ArrayList());
						list.add(parentNode);
						treeNodeMap.put(pnodeId, parentNode);
		    		}
		    	}
		    	
		    	
		    	TreeNode parentNode2 = treeNodeMap.get(pnodeId+"@"+organ_id);
		    	if(parentNode2 == null){
		    		parentNode2 = new TreeNode();
		    		parentNode2.setId(pnodeId+"@"+organ_id);
		    		parentNode2.setTitle(organ_name);
		    		parentNode2.setType("organ");
		    		parentNode2.setParentId(parentNode.getId());
		    		parentNode2.setChildren(new ArrayList());
					parentNode.getChildren().add(parentNode2);
					treeNodeMap.put(pnodeId+"@"+organ_id, parentNode2);
		    	}
		    	
		    	
		    	TreeNode parentNode3 = treeNodeMap.get(pnodeId+"@"+quarter_id);
		    	if(parentNode3 == null){
		    		parentNode3 = new TreeNode();
		    		parentNode3.setId(pnodeId+"@"+quarter_id);
		    		parentNode3.setTitle(quarter_name);
		    		parentNode3.setType("quarter");
		    		parentNode3.setParentId(parentNode2.getId());
		    		parentNode3.setChildren(new ArrayList());
					parentNode2.getChildren().add(parentNode3);
					treeNodeMap.put(pnodeId+"@"+quarter_id, parentNode3);
		    	}
		    	
		    	
		    	TreeNode parentNode4 = treeNodeMap.get(user_id);
		    	if(parentNode4 == null){
		    		parentNode4 = new TreeNode();
		    		parentNode4.setId(user_id);
		    		parentNode4.setTitle(user_name);
		    		parentNode4.setType("exam");
		    		parentNode4.setParentId(parentNode3.getId());
		    		parentNode4.setChildren(new ArrayList());
		    		parentNode4.setLeaf(true);
					parentNode3.getChildren().add(parentNode4);
					treeNodeMap.put(user_id, parentNode4);
		    	}
		    }
		    TreeNode.putTypeIncon("parentN", "resources/icons/fam/application_view_list.png", "");
		    TreeNode.putTypeIncon("organ", "resources/icons/fam/organ.gif", "");
		    TreeNode.putTypeIncon("quarter", "resources/icons/fam/user_gray.png", "");
			TreeNode.putTypeIncon("exam", "resources/icons/fam/user_green.gif", "");
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}
	
	
	
	/**
	 * 获取考试管理的父节点
	 * @description
	 * @param publicId
	 * @param examId
	 * @return
	 * @modified
	 */
	public List<TreeNode> getExamineeOrganTree(String publicId,String examId){
		List<TreeNode> list = new ArrayList<TreeNode>();

		try{
			Session session = template.getSessionFactory().getCurrentSession();
			String sql = "select distinct o.organ_id,o.organ_name,qs.quarter_id,qs.quarter_name, " 
					+ "	o.order_no,o.organ_code,qs.sort_num,qs.quarter_code "+
						  "from EXAM_PUBLIC_USER t, "+
						       "employee e, "+
						       "dept d, "+
						       "organ o, "+
						       "quarter_standard qs, "+
						       "(select * from EXAM_USER_TESTPAPER et where et.exam_id = ?) v "+
						 "where t.employee_id = v.employee_id "+
						   "and t.employee_id = e.employee_id "+
						   "and e.dept_id = d.dept_id "+
						   "and d.organ_id = o.organ_id "+
						   "and e.quarter_train_id = qs.quarter_id "+
						   "and t.public_id = ? "+
						   "order by o.order_no,o.organ_code,qs.sort_num,qs.quarter_code ";
			SQLQuery sqlQuery=session.createSQLQuery(sql);
			sqlQuery.setString(0, examId);
			sqlQuery.setString(1, publicId);
		    List<Object[]> tmplist = sqlQuery.addScalar("organ_id", StringType.INSTANCE)
				.addScalar("organ_name", StringType.INSTANCE)
				.addScalar("quarter_id", StringType.INSTANCE)
				.addScalar("quarter_name", StringType.INSTANCE)
				.list();
		    Map<String,TreeNode> treeNodeMap = new HashMap<String,TreeNode>();
		    
		    TreeNode allParentNode = null;
		    String old_organ_id = null;
		    if(tmplist!=null && tmplist.size()>0){
		    	Object[] object = (Object[])tmplist.get(0);
		    	old_organ_id = (String)object[0];
		    }
		    for(int i=1;i<tmplist.size();i++){
		    	Object[] object = (Object[])tmplist.get(i);
		    	String organ_id = (String)object[0];
		    	
		    	if(!old_organ_id.equals(organ_id)){
		    		allParentNode = new TreeNode();
		    		allParentNode.setId("allOrgan");
		    		allParentNode.setTitle("所有人员");
		    		allParentNode.setType("organ");
		    		allParentNode.setParentId(null);
		    		allParentNode.setChildren(new ArrayList());
		    		list.add(allParentNode);
		    		break;
		    	}
		    }
		    for(int i=0;i<tmplist.size();i++){
		    	Object[] object = (Object[])tmplist.get(i);
		    	String organ_id = (String)object[0];
		    	String organ_name = (String)object[1];
		    	String quarter_id = (String)object[2];
		    	String quarter_name = (String)object[3];

		    	TreeNode parentNode = treeNodeMap.get(organ_id);
		    	if(parentNode == null){
		    		parentNode = new TreeNode();
		    		parentNode.setId(organ_id);
		    		parentNode.setTitle(organ_name);
		    		parentNode.setType("organ");
		    		
		    		parentNode.setChildren(new ArrayList());
		    		if(allParentNode!=null){
		    			parentNode.setParentId(allParentNode.getId());
		    			allParentNode.getChildren().add(parentNode);
		    		}else{
		    			parentNode.setParentId(null);
		    			list.add(parentNode);
		    		}
		    		treeNodeMap.put(organ_id,parentNode);
		    	}
		    	
		    	
		    	TreeNode parentNode3 = treeNodeMap.get(organ_id+"@"+quarter_id);
		    	if(parentNode3 == null){
		    		parentNode3 = new TreeNode();
		    		parentNode3.setId(organ_id+"@"+quarter_id);
		    		parentNode3.setTitle(quarter_name);
		    		parentNode3.setType("quarter");
		    		parentNode3.setParentId(parentNode.getId());
		    		parentNode3.setChildren(new ArrayList());
		    		parentNode3.setLeaf(true);
		    		parentNode.getChildren().add(parentNode3);
		    		treeNodeMap.put(organ_id+"@"+quarter_id,parentNode3);
		    	}
		    }
		    TreeNode.putTypeIncon("organ", "resources/icons/fam/organ.gif", "");
		    TreeNode.putTypeIncon("quarter", "resources/icons/fam/user_gray.png", "");
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}
	
	
	/**
	 * 根据查询条件查询考生信息ID
	 * @description
	 * @param examId
	 * @param organId
	 * @param quarter_train_id
	 * @return
	 * @modified
	 */
	public List<ExamUserTestpaper> getExamUserTestpaperList(String examId,String organId,String quarter_train_id){
		List list = new ArrayList();
		try{
			String sql = "select t.* from (select * from EXAM_USER_TESTPAPER v where v.exam_id = :examId) t "; 
			if(organId!=null && !"".equals(organId) && !"null".equals(organId) 
					&& quarter_train_id!=null && !"".equals(quarter_train_id) && !"null".equals(quarter_train_id)){
				sql+=" where t.employee_id in (select e.employee_id "+
                        " from employee e, (select dd.* from dept dd where dd.organ_id = '"+organId+"') d, "
                     + " (select qq.* from quarter qq where qq.quarter_train_id = '"+quarter_train_id+"' ) q "+
                       " where e.dept_id = d.dept_id "+
                         " and e.quarter_id = q.quarter_id) ";
			}else if(organId!=null && !"".equals(organId) && !"null".equals(organId)){
				sql+=" where t.employee_id in (select e.employee_id "+
                        " from employee e, (select dd.* from dept dd where dd.organ_id = '"+organId+"') d "+
                       " where e.dept_id = d.dept_id )";
			}else if(quarter_train_id!=null && !"".equals(quarter_train_id) && !"null".equals(quarter_train_id)){
				sql+=" where t.employee_id in (select e.employee_id "+
                        " from employee e, (select qq.* from quarter qq where qq.quarter_train_id = '"+quarter_train_id+"' ) q "+
                       " where e.quarter_id = q.quarter_id)";
			}
			sql+=" order by t.employee_name, t.inticket";
			Map term = new HashMap();
			term.put("examId", examId);
			list = this.querySql(sql, term, null, ExamUserTestpaper.class);
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}

}
