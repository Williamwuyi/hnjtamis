package cn.com.ite.hnjtamis.exam.base.theme;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.type.DoubleType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;
import org.springframework.orm.hibernate4.HibernateTemplate;

import cn.com.ite.eap2.core.hibernate.HibernateDefaultDAOImpl;
import cn.com.ite.hnjtamis.common.StaticVariable;
import cn.com.ite.hnjtamis.exam.base.theme.form.XxbItemFrom;
import cn.com.ite.hnjtamis.exam.hibernatemap.Theme;

/**
 *
 * <p>Title cn.com.ite.hnjtamis.exam.testpaper.TestpaperDaoImpl</p>
 * <p>Description 试题管理 </p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2015</p>
 * @author 朱健
 * @create time: 2015年3月25日 上午10:11:49
 * @version 1.0
 * 
 * @modified records:
 */
public class ThemeDaoImpl extends HibernateDefaultDAOImpl implements ThemeDao{

	/**
	 * 获取员工的学习币
	 * @description
	 * @param employeeId
	 * @return
	 * @modified
	 */
	public double getEmployeeXxb(String employeeId){
		double xxb = 0.0d;
		try{
			Session session = template.getSessionFactory().getCurrentSession();
			String sql =" select sum(t.award_center) XXB from THEME_FKAUDIT t where t.award_center is not null "
					+ " and t.created_id_by = ? ";
			SQLQuery sqlQuery=session.createSQLQuery(sql);
			sqlQuery.setString(0, employeeId);
			List<Double> list = sqlQuery.addScalar("XXB", DoubleType.INSTANCE).list();
			if(list!=null && list.size()>0){
				Double _xxb = (Double)list.get(0);
				if(_xxb!=null){
					xxb = _xxb.doubleValue();
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return xxb;
	}
	
	/**
	 * 获取员工的学习币(详细)
	 * @description
	 * @param employeeId
	 * @return
	 * @modified
	 */
	public List<XxbItemFrom> getEmployeeXxbItemList(String employeeId){
		List<XxbItemFrom> xxblist = new ArrayList<XxbItemFrom>();
		try{
			Session session = template.getSessionFactory().getCurrentSession();
			String sql =" select t.award_center as XXB, t.award_time as AWARDTIME,t.fk_type as FKTYPE,"
					+ "t.theme_id as THEMEID,tt.theme_name as THEMENAME,t.creation_date as CREATIONDATE "+
				  " from THEME_FKAUDIT t,theme tt  "+
				 " where t.theme_id = tt.theme_id and t.award_center > 0 "+
				 " and  t.award_center is not null "+
				   " and t.created_id_by = ? "+
				 " order by t.creation_date desc,t.award_time desc ";
			SQLQuery sqlQuery=session.createSQLQuery(sql);
			sqlQuery.setString(0, employeeId);
			List list = sqlQuery.addScalar("XXB", DoubleType.INSTANCE)
					.addScalar("AWARDTIME", StringType.INSTANCE)
					.addScalar("FKTYPE", StringType.INSTANCE)
					.addScalar("THEMEID", StringType.INSTANCE)
					.addScalar("THEMENAME", StringType.INSTANCE)
					.addScalar("CREATIONDATE", StringType.INSTANCE).list();
			if(list!=null && list.size()>0){
				for(int i=0;i<list.size();i++){
					Object[] obj = (Object[])list.get(i);
					Double xxb = (Double)obj[0];
					String awardTime = (String)obj[1];
					String fkType = (String)obj[2];
					String themeId = (String)obj[3];
					String themeName = (String)obj[4];
					String creationDate = (String)obj[5];
					XxbItemFrom xxbItemFrom = new XxbItemFrom();
					xxbItemFrom.setXxb(xxb);
					xxbItemFrom.setAwardTime(awardTime);
					xxbItemFrom.setFkType(fkType);
					xxbItemFrom.setThemeId(themeId);
					xxbItemFrom.setThemeName(themeName);
					xxbItemFrom.setCreationDate(creationDate);
					xxblist.add(xxbItemFrom);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return xxblist;
	}
	
	/**
	 *
	 * @author 朱健
	 * @return
	 * @modified
	 */
	public Map getThemeCrcMap(){
		Map value = new HashMap();
		HibernateTemplate template = (HibernateTemplate) this.template;		   
		Session session = template.getSessionFactory().getCurrentSession();
		String sql =" select t.theme_crc THEMECRC from theme t where t.theme_crc is not null and t.is_use = 5 ";
		SQLQuery sqlQuery=session.createSQLQuery(sql);
		List<String> list = sqlQuery.addScalar("THEMECRC", StringType.INSTANCE).list();
		if(list!=null && list.size()>0){
			for(int i=0;i<list.size();i++){
				String themeCrc = list.get(i);
				value.put(themeCrc, themeCrc);
			}
		}
		return value;
	}
	
	
	/**
	 * 查询公有的题目校验码
	 * @author 朱健
	 * @return
	 * @modified
	 */
	public Map getGyThemeCrcMap(){
		Map value = new HashMap();
		HibernateTemplate template = (HibernateTemplate) this.template;		   
		Session session = template.getSessionFactory().getCurrentSession();
		String sql =" select t.theme_crc THEMECRC "+
						 " from theme t, "+
						 "  (select tt.theme_id "+
						 "   from theme_in_bank tt, theme_bank b "+
						 "  where tt.theme_bank_id = b.theme_bank_id "+
						 "   and b.bank_public = '10' "+
						 "   and b.is_l = '10') ttt "+
						 " where t.theme_id = ttt.theme_id "+
						 "  and t.theme_crc is not null "+
						 "  and t.is_use = 5   ";
		SQLQuery sqlQuery=session.createSQLQuery(sql);
		List<String> list = sqlQuery.addScalar("THEMECRC", StringType.INSTANCE).list();
		if(list!=null && list.size()>0){
			for(int i=0;i<list.size();i++){
				String themeCrc = list.get(i);
				value.put(themeCrc, themeCrc);
			}
		}
		return value;
	}
	
	/**
	 * 按题库+试题校验码进行校验，一个题库只能存在一个试题
	 * @author 朱健
	 * @return theme_bank_id+"@"+themeCrc
	 * @modified
	 */
	public Map getThemeBankAndThemeCrcMap(String themeBankId){
		Map value = new HashMap();
		HibernateTemplate template = (HibernateTemplate) this.template;		   
		Session session = template.getSessionFactory().getCurrentSession();
		String sql =" select t.theme_crc THEMECRC,b.theme_bank_id from theme t,"
				+ "(select bb.theme_id,bb.theme_bank_id "
				+ " from theme_in_bank bb where bb.theme_bank_id ='"+themeBankId+"') b where "
				+ " t.theme_id = b.theme_id and t.theme_crc is not null and t.is_use = 5 ";
		SQLQuery sqlQuery=session.createSQLQuery(sql);
		List<Object[]> list = sqlQuery.addScalar("THEMECRC", StringType.INSTANCE).addScalar("theme_bank_id", StringType.INSTANCE).list();
		if(list!=null && list.size()>0){
			for(int i=0;i<list.size();i++){
				Object[] object= list.get(i);
				String themeCrc = (String)object[0];
				String theme_bank_id = (String)object[1];
				value.put(theme_bank_id+"@"+themeCrc, theme_bank_id+"@"+themeCrc);
			}
		}
		return value;
	}
	
	/**
	 * 查询试题列表
	 * @author 朱健
	 * @param param
	 * @return
	 * @modified
	 */
	public List<Theme> getThemeList(Map param,int start,int limit){
		List<Theme> list = new ArrayList<Theme>();
		try{
			//** 原来判断如果可以多人反馈，只有进入审核人分配才关闭，现在修改为只要进行了反馈，则关闭不再让其它人反馈，如果分配了审核人，试题自动修改为保存状态
			/*String op = (String)param.get("op");//input录入 audit审核 admin管理员
			if("fp".equals(op)){
				updateThemeFkState();
			}*/
			list = this.queryHql(getQueryHql(param), param, null, Theme.class,start,limit);
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}
	
	
	private void updateThemeFkState(){
		try{
			String updateStr = " update theme t "+
				   " set t.last_fk_state = '20'"+
			 "  where exists (select 1"+
			 		" from Theme_Fkaudit tf"+
			 		" where tf.theme_Id = t.theme_Id"+
			 			"  and tf.state = 10)"+
			 	 " and (t.last_fk_state is null or t.last_fk_state = '10')";
			this.saveSQL(updateStr);  
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * 查询试题列表的数量
	 * @author 朱健
	 * @param param
	 * @return
	 * @modified
	 */
	public int getThemeCount(Map param){
		int count = 0;
		try{
			Object term = param;
			String hql = getQueryHql(param);
			hql = this.qlHandler(hql, term).trim();
			Map termHander = new HashMap();
			this.hqlParameterHandler(hql, term, termHander);
			if(hql.trim().toLowerCase().startsWith("from"))
				hql = "select count(*) "+hql;
			else{
				hql = hql.replaceAll("\\s+", " ");
				hql = hql.replaceFirst("select", "select count(");
				hql = hql.replaceFirst("from", ") from");
			}
			Query query = (Query) template.getSessionFactory().getCurrentSession().createQuery(hql);
			query.setCacheable(true);
			Iterator iterator = termHander.keySet().iterator();
			while (iterator.hasNext()) {
				String key = (String) iterator.next();
				Object value = termHander.get(key);
				if (value instanceof Collection)
					query.setParameterList(key, (Collection) value);
				else if (value instanceof Object[])
					query.setParameterList(key, (Object[]) value);
				else if(value!=null)
					query.setParameter(key, value);
				else
					query.setString(key, null);
			}
			count = ((Long)query.uniqueResult()).intValue();
		}catch(Exception e){
			e.printStackTrace();
		}
		return count;
	}
	
	/**
	 * QL语句的动态处理
	 * @param ql 
	 * @param term  
	 * @return
	 * @modified
	 */
	private String qlHandler(String ql,Object term){
		 VelocityEngine ve = new VelocityEngine(); 
		 //ve.setProperty(Velocity.FILE_RESOURCE_LOADER_PATH, path); 
		 //设置编码
		 ve.setProperty(Velocity.INPUT_ENCODING,"UTF-8");
		 ve.setProperty(Velocity.OUTPUT_ENCODING,"UTF-8");
		 try{ 
            //初始
            ve.init();
            //VELOCITY环境
            VelocityContext context = new VelocityContext(); 
            //设置参数 
            context.put("term", term);
            java.io.StringWriter writer=new java.io.StringWriter();
			ve.evaluate(context, writer, null, ql);
			return writer.toString();   
         }catch (Exception e){ 
            e.printStackTrace(); 
         }
         return ql;
	}
	
	/**
	 * HQL参数处理
	 * @param hql 语句
	 * @param term 条件
	 * @param termHander 处理后条件
	 * @return
	 * @modified
	 */
	@SuppressWarnings("unchecked")
	private void hqlParameterHandler(String hql,Object term,Map termHander){
		String regEx = ":[A-Za-z][A-Za-z0-9_]{1,}";
		Pattern pat = Pattern.compile(regEx);  
		Matcher mat = pat.matcher(hql);  
		if(termHander==null)
			termHander = new HashMap();
		termHander.clear();
		boolean result = mat.find();
		while(result) {
			String param = mat.group().substring(1);
			Object paramValue;
			try {
				if(!termHander.containsKey(param)){
				   if(term instanceof Map)
					   paramValue = ((Map)term).get(param);
				   else
				       paramValue = PropertyUtils.getProperty(term, param);
				   //System.out.println(param+"="+paramValue);
				   termHander.put(param,paramValue);
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(param+"参数设置不对应！");
			}
			result = mat.find(); 
	    }
	}
	

	private String getQueryHql(Map param){
		String hql = " from Theme a where a.isUse = 5 ";
		String themeNameTerm = (String)param.get("themeNameTerm");
		if(themeNameTerm!=null && !"".equals(themeNameTerm) && !"null".equals(themeNameTerm)){
			hql+=" and a.themeName like '%' || :themeNameTerm || '%' ";
		}
		Integer stateTerm = (Integer)param.get("stateTerm");
		if(stateTerm!=null){
			hql+=" and a.state = :stateTerm ";
		}
		String themeTypeIdTerm = (String)param.get("themeTypeIdTerm");
		if(themeTypeIdTerm!=null && !"".equals(themeTypeIdTerm) && !"null".equals(themeTypeIdTerm)){
			hql+=" and a.themeType.themeTypeId = :themeTypeIdTerm ";
		}
		String specialityIdTerm = (String)param.get("specialityIdTerm");
		if(specialityIdTerm!=null && !"".equals(specialityIdTerm) && !"null".equals(specialityIdTerm)){
			hql+= " and exists (select 1 from ThemeBank tb,ThemeInBank b,ThemeBankProfession p "
			  + " where b.theme.themeId = a.themeId "
	          + " and tb.themeBankId = b.themeBank.themeBankId "
	          + " and tb.themeBankId = p.themeBank.themeBankId "
	          + " and p.speciality.specialityid = :specialityIdTerm) ";
		}
		String writeUserTerm = (String)param.get("writeUserTerm");
		if(writeUserTerm!=null && !"".equals(writeUserTerm) && !"null".equals(writeUserTerm)){
			hql+= " and a.writeUser like '%' || :writeUserTerm || '%' ";
		}
		Integer degreeTerm = (Integer)param.get("degreeTerm");
		if(degreeTerm!=null){
			hql+= " and a.degree = :degreeTerm  ";
		}
		String knowledgePointTerm = (String)param.get("knowledgePointTerm");
		if(knowledgePointTerm!=null && !"null".equals(knowledgePointTerm) && !"".equals(knowledgePointTerm)){
			hql+= " and a.knowledgePoint like '%' || :knowledgePointTerm || '%'   "; 
		}
		String themeBankIdTerm = (String)param.get("themeBankIdTerm");
		if(themeBankIdTerm!=null && !"".equals(themeBankIdTerm) && !"null".equals(themeBankIdTerm)){
			hql+= " and exists(select 1 from ThemeInBank b  "+
		       " where b.theme.themeId = a.themeId "
		       + " and ',' || :themeBankIdTerm || ',' like '%,' || b.themeBank.themeBankId || ',%') ";
		}
		String bankType = (String)param.get("bankType");
		if(bankType!=null && !"".equals(bankType) && !"null".equals(bankType)){
			hql+= " and exists(select 1 from ThemeInBank b  "+
		       " where b.theme.themeId = a.themeId "
		       + " and b.bankType = '"+bankType+"') ";
		}
		
		String op = (String)param.get("op");//input录入 audit审核 admin管理员
		String sf = (String)param.get("sf");//pro-个人  all-全部
		String employeeId = (String)param.get("employeeId");
		String organId = (String)param.get("organId");
		
		if("fk".equals(op)){
			hql+= " and a.state = '15' and a.allowFk = '10' and (a.lastFkState in ('10','50','-50') or a.lastFkState  is null)";
		}else if("fp".equals(op)){//反馈分配
			hql+= " and  a.lastFkState = '20' and a.lastFkOrganId = '"+organId+"' ";
		}else if("audit".equals(op)){
			hql+= " and a.lastFkAuditId = '"+employeeId+"' and a.lastFkState in ('30','-40') ";
		}/*else  if("audit".equals(op)){//审核
			hql+= " and exists(select 1 from ThemeInBank b  "+
			" where b.theme.themeId = a.themeId "
			 + " and ',' || b.themeBank.themeAuditId || ',' like '%,' || '"+employeeId+"' || ',%') "
			 + " and a.state in (10,15,20) ";
		}*/else  if("qr".equals(op)){//审核再次确认
			hql+= " and a.lastFkState = '40' ";
		}else if("input".equals(op) && "pro".equals(sf)){//个人试题录入
			hql+= " and   a.createdIdBy = '"+employeeId+"' ";
		}else if("dc".equals(sf)){//电厂录入
			hql+= " and a.organId = '"+organId+"' ";
		}else if("admin".equals(op) || "all".equals(sf)){//管理员或显示全部
			hql+= " and a.organId = '"+organId+"' ";
		}else if("fkquery".equals(op) && "pro".equals(sf)){
			hql+= " and  a.lastFkState in ('10','20','30','40','50','-40','-50') and a.lastFkEmployeeId = '"+employeeId+"'  ";
		}else if("fkquery".equals(op) && "dcadmin".equals(sf)){
			hql+= " and  a.lastFkState in ('10','20','30','40','50','-40','-50') and a.lastFkOrganId = '"+organId+"'  ";
		}else{//其他参数均不显示
			hql+=" and 1=2 ";
		}
		
		String queryTypeTerm = (String)param.get("queryTypeTerm");//查询方式 'SAMETHEME' - '存在相同试题'
		if("SAMETHEME".equals(queryTypeTerm)){
			hql+= " and a.themeCrc is not null and exists( select 1 from  Theme v where v.themeCrc = a.themeCrc and "
					+ " a.themeId<> v.themeId and v.themeCrc is not null and v.isUse = 5 ) ";
			hql+=  " order by a.themeCrc,a.creationDate desc,a.createdBy desc ";
		}else if("PROBTHEME".equals(queryTypeTerm)){
			hql+=  " and  a.checkRemark is not null  order by  a.checkRemark,a.creationDate desc,a.createdBy desc,a.themeCode,a.themeId ";
		}else if("fkquery".equals(queryTypeTerm)){
			hql+=  " order by a.lastFkAuditTime desc,a.lastUpdateDate desc,a.sortNum,a.creationDate desc,a.createdBy desc,a.themeCode,a.themeId ";
		}else{
			hql+=  " order by a.sortNum,a.creationDate desc,a.createdBy desc,a.themeCode,a.themeId ";
		}
		
		return hql;
	}
	
	
	/**
	 * 检查试题
	 * @description
	 * @modified
	 */
	private static int addCheckThemeNum = 10000;
	public int[] saveAndcheckTheme(){
		int checkNum = 0;
		int checkNewNum = 0;
		int probNum = 0;
		try{
			HibernateTemplate template = (HibernateTemplate) this.template;		   
			Session session = template.getSessionFactory().getCurrentSession();
			String sql =" select count(*) themeCount from THEME t where t.is_use = 5 ";
			SQLQuery sqlQuery=session.createSQLQuery(sql);  
			List list = sqlQuery.addScalar("themeCount", IntegerType.INSTANCE).list();
			StaticVariable.CHECK_SCHEDULE = new int[]{0,0};//检查执行进度
			if(list!=null && list.size()>0){
				int themeCount = ((Integer)list.get(0)).intValue();
				StaticVariable.CHECK_SCHEDULE[1] = themeCount;
				System.out.println("开始检查试题.......");
				
				if(themeCount>0){
					int startIndex = 0;
					int endIndex = startIndex+addCheckThemeNum;
					while(true){
						System.out.println("开始检查"+startIndex+" 到 "+(endIndex+1)+"");
						sql =" select {t.*} from (select ttt.*, rownum rownum_ "+
							" from (select tt.* from THEME tt where tt.is_use = 5 order by tt.creation_date desc,tt.theme_id) ttt) t "+
						" where t.rownum_ > "+startIndex+" and t.rownum_ < "+(endIndex+1);
						sqlQuery=session.createSQLQuery(sql);  
						List<Theme> themelist = sqlQuery.addEntity("t", Theme.class).list();
						List saveList = new ArrayList();
						for(int i=0;i<themelist.size();i++){
							Theme theme = themelist.get(i);
							String checkRemark = ThemeCheckOption.checkTheme(theme);
							if((checkRemark==null  && theme.getCheckRemark()!=null)
									|| (checkRemark!=null && !checkRemark.equals(theme.getCheckRemark()))){
								theme.setCheckRemark(checkRemark);
								saveList.add(theme);
								checkNewNum++;
							}
							if(theme.getCheckRemark()!=null && !"".equals(theme.getCheckRemark())
									&& !"null".equals(theme.getCheckRemark())){
								probNum++;
							}
							checkNum++;
							StaticVariable.CHECK_SCHEDULE[0]++;
						}
						if(saveList.size()>0){
							this.saveBatchEntity(saveList);
						}
						if(endIndex<=themeCount){
							startIndex = startIndex+addCheckThemeNum;
							endIndex = startIndex+addCheckThemeNum;
						}else{
							break;
						}
						
					}
				}
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return new int[]{checkNum,checkNewNum,probNum};
	}
	
}
