package cn.com.ite.hnjtamis.exam.testpaper;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;
import org.springframework.orm.hibernate4.HibernateTemplate;

import cn.com.ite.hnjtamis.core.hibernate.HibernateDefaultExDAOImpl;
import cn.com.ite.hnjtamis.exam.hibernatemap.Theme;
import cn.com.ite.hnjtamis.exam.hibernatemap.ThemeAnswerkey;
import cn.com.ite.eap2.domain.baseinfo.Dictionary;

/**
 *
 * <p>Title cn.com.ite.hnjtamis.exam.testpaper.TestpaperDaoImpl</p>
 * <p>Description 试卷模版生成 </p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2015</p>
 * @author 朱健
 * @create time: 2015年3月25日 上午10:11:49
 * @version 1.0
 * 
 * @modified records:
 */
public class TestpaperDaoImpl extends HibernateDefaultExDAOImpl implements TestpaperDao{

	/**
	 * 获取一个顶级部门下所有部门ID
	 * @description
	 * @param deptId
	 * @return
	 * @modified
	 */
	public List<String> getAllDeptInDeptId(String deptId){
		List<String> deptIdslist = null;
		try{
			Session session = template.getSessionFactory().getCurrentSession();
			SQLQuery sqlQuery=session.createSQLQuery("select d.dept_id as DEPTID from dept d where d.is_validation = 1 "
					+ " start with d.dept_id = ? connect by prior d.dept_id = d.parent_dept_id");   
			sqlQuery.setString(0, deptId);
		    deptIdslist = (List<String>)sqlQuery.addScalar("DEPTID", StringType.INSTANCE).list();
		}catch(Exception e){
			e.printStackTrace();
		}
		return deptIdslist;
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
	 * 删除无用的试题
	 * @author zhujian
	 * @description
	 * @modified
	 */
	public void deleteNullTestpaper(){
		try{
			String sql1 = " delete testpaper_theme_answerkey a where a.testpaper_theme_id in(select t.testpaper_theme_id from testpaper_theme t where t.testpaper_id  is null) ";
			String sql2 = " delete testpaper_theme t where t.testpaper_id  is null";
			Session session=template.getSessionFactory().getCurrentSession();
			session.createSQLQuery(sql1).executeUpdate();
			session.createSQLQuery(sql2).executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	
	/**
	 * 根据参数查询考题
	 * @author 朱健
	 * @param term
	 * @return
	 * @modified
	 */
	public List<Theme> getThemeByParam(Map term){
		List<Theme> list = new ArrayList<Theme>();
		String hql = "";
		try{
			hql = "  from  Theme a where  a.isUse = 5 and a.state = 15 ";
			String themeTypeId = (String)term.get("themeTypeId");
			if(themeTypeId!=null){
				hql+=" and a.themeType.themeTypeId = :themeTypeId  ";
			}
			String specialityid = (String)term.get("specialityid");
			if(specialityid!=null && !"".equals(specialityid)){
				hql+=" and exists (  "+
		           " select 1 from ThemeInBank b where b.theme.themeId = a.themeId   "+
		           " and exists(select 1 from ThemeBankProfession p  "+
		           " where p.themeBank.themeBankId = b.themeBank.themeBankId and p.speciality.specialityid = :specialityid)) ";
			}
			String themeBankIds = (String)term.get("themeBankIds");
			if(themeBankIds!=null && !"".equals(themeBankIds)){
					hql+=" and exists(select 1 from ThemeInBank b  "+
		           		 " where b.theme.themeId = a.themeId "+ 
		           		 " and ',' || :themeBankIds || ',' like '%,' || b.themeBank.themeBankId || ',%') ";
			}
			String examType = (String)term.get("examType");
			if(examType!=null){
				if(examType.indexOf(",")!=-1){
					String[] tmp = examType.split(",");
					String examTypeStr = "";
					for(int i=0;i<tmp.length;i++){
						examTypeStr+="'"+tmp[i]+"',";
					}
					if(examTypeStr.length()>0){
						examTypeStr = examTypeStr.substring(0,examTypeStr.length()-1);
					}
					//hql+=" and instr(',' || :examType || ',' ,',' || a.type || ',') > 0 ";
					hql+=" and a.type in("+examTypeStr+") ";
				}else{
					hql+=" and a.type = :examType ";
				}
			}
			String employeeId = (String)term.get("employeeId");
			if(employeeId!=null){
						hql+=" and not exists(select 1 from ExamTestpaperTheme k  "+
		           		         " where k.themeId = a.themeId and exists( select 1 from ExamUserTestpaper et   "+
		           		         		" where  k.examTestpaper.examTestpaperId = et.examTestpaper.examTestpaperId  "+
		           		         		" and et.employeeId = :employeeId ) "+
		           		         	" ) ";
			}
			String relationType = (String)term.get("relationType");
			if(relationType!=null && !"mocs".equals(relationType)){
				hql+=" and not exists(select 1 from TestpaperTheme k  "+
           		         " where k.theme.themeId = a.themeId  and k.testpaper.relationType like '%"+relationType+"%')  ";
			}
			hql+=" order by a.themeSetNum ";
			list = this.queryHql(hql, term, null, Theme.class);
		}catch(Exception e){
			System.out.println(hql);
			e.printStackTrace();
		}
		return list;
		
	}
	
	/**
	 * 根据主键获取试题Map
	 * @author 朱健
	 * @param term
	 * @return
	 * @modified
	 */
	public Map<String,Theme> getThemeInIds(String[] ids){
		Map<String,Theme> themeMap= new HashMap<String,Theme>();
		try{
			String idsStr = "";
			for(int i=0;i<ids.length;i++){
				idsStr+="'"+ids[i]+"',";
				if((i>0 && i%100==0) || i==ids.length-1){
					idsStr = idsStr.substring(0,idsStr.length()-1);
					String hql = "  from  Theme a where a.themeId in ("+idsStr+")";
					List<Theme> list = this.queryHql(hql, null, null, Theme.class);
					for(int j=0;j<list.size();j++){
						Theme theme = list.get(j);
						themeMap.put(theme.getThemeId(), theme);
					}
				}
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return themeMap;
		
	}
	
	/**
	 *
	 * @description
	 * @param selectThemeIds
	 * @param paramMap
	 * @return
	 * @modified
	 */
	public List<Theme> getHandAddThemeList(String selectThemeIds,Map paramMap){
		List<Theme> list = new ArrayList<Theme>();
		try{
			
			String xz = (String)paramMap.get("xz");
			
			String hql = "from  Theme a where 1=1 ";
			if(selectThemeIds!=null && !"".equals(selectThemeIds) && !"null".equals(selectThemeIds)){
				String[] tmplist =  selectThemeIds.split(",");
				String selectThemeIdsStr = "";
				for(int i=0;i<tmplist.length;i++){
					selectThemeIdsStr+="'"+tmplist[i]+"',";
				}
				selectThemeIdsStr = selectThemeIdsStr.substring(0,selectThemeIdsStr.length()-1);
				//hql+=" and  instr(',' || :selectThemeIds || ',' , ',' || a.themeId || ',') = 0 ";
				//paramMap.put("selectThemeIds", selectThemeIds);
				hql+=" and a.themeId not in("+selectThemeIdsStr+") ";
			}
			String themeBankIds = (String)paramMap.get("themeBankIds");
			if(themeBankIds!=null && !"".equals(themeBankIds) && !"null".equals(themeBankIds)){
				hql+=" and exists(select 1 from ThemeInBank b   "+
              	      "where b.theme.themeId = a.themeId   "+
              	      	"and instr(',' || :themeBankIds || ',' , ',' || b.themeBank.themeBankId || ',') > 0  "+
              	      		"  and b.theme.themeId is not null)  ";
			}
			String qthemeBankIds = (String)paramMap.get("qthemeBankIds");
			if(qthemeBankIds!=null && !"".equals(qthemeBankIds) && !"null".equals(qthemeBankIds)){
				hql+=" and exists(select 1 from ThemeInBank b   "+
	              	      "where b.theme.themeId = a.themeId   "+
	              	      	"and instr(',' || :qthemeBankIds || ',' , ',' || b.themeBank.themeBankId || ',') > 0  "+
	              	   "  and b.theme.themeId is not null)  ";
			}
			String qthemeType = (String)paramMap.get("qthemeType");
			if(qthemeType!=null && !"".equals(qthemeType) && !"null".equals(qthemeType)){
				hql+=" and a.themeType.themeTypeId = :qthemeType ";
			}
			String qgjzName = (String)paramMap.get("qgjzName");
			if(qgjzName!=null && !"".equals(qgjzName) && !"null".equals(qgjzName)){
				hql+=" and  a.themeName  like  '%' || :qgjzName || '%'  ";
			}
			String examType = (String)paramMap.get("examType");
			if(examType!=null){
				if(examType.indexOf(",")!=-1){
					String[] tmp = examType.split(",");
					String examTypeStr = "";
					for(int i=0;i<tmp.length;i++){
						examTypeStr+="'"+tmp[i]+"',";
					}
					if(examTypeStr.length()>0){
						examTypeStr = examTypeStr.substring(0,examTypeStr.length()-1);
					}
					//hql+=" and instr(',' || :examType || ',' ,',' || a.type || ',') > 0 ";
					hql+=" and a.type in("+examTypeStr+") ";
				}else{
					hql+=" and a.type = :examType ";
				}
			}
			String showSign =  (String)paramMap.get("showSign");
			String employeeId = (String)paramMap.get("employeeId");
			if("1".equals(showSign) && employeeId!=null){	
				hql+=" and a.themeId in(select et.themeId from StudyTestpaperThemeSign et "
						+ "  where et.createdIdBy =:employeeId and et.signInTheme ='10' and et.signInTheme is not null)";
			}
			hql+="and  a.isUse = 5 and a.state = 15 order by a.themeType.sortNum,a.creationDate desc";
			if("page".equals(xz)){
				int row_start = (int)paramMap.get("row_start");
				int row_limit  = (int)paramMap.get("row_limit");
				list = this.queryHql(hql, paramMap, null, Theme.class,row_start,row_limit);
			}else{
				list = this.queryHql(hql, paramMap, null, Theme.class);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}
	
	
	public int getHandAddThemeListCount(String selectThemeIds,Map paramMap){
		int count = 0;
		try{
			String hql = "select count(*) as sumvalue from  Theme a where 1=1 ";
			if(selectThemeIds!=null && !"".equals(selectThemeIds) && !"null".equals(selectThemeIds)){
				String[] tmplist =  selectThemeIds.split(",");
				String selectThemeIdsStr = "";
				for(int i=0;i<tmplist.length;i++){
					selectThemeIdsStr+="'"+tmplist[i]+"',";
				}
				selectThemeIdsStr = selectThemeIdsStr.substring(0,selectThemeIdsStr.length()-1);
				//hql+=" and  instr(',' || :selectThemeIds || ',' , ',' || a.themeId || ',') = 0 ";
				//paramMap.put("selectThemeIds", selectThemeIds);
				hql+=" and a.themeId not in("+selectThemeIdsStr+") ";
			}
			String themeBankIds = (String)paramMap.get("themeBankIds");
			if(themeBankIds!=null && !"".equals(themeBankIds) && !"null".equals(themeBankIds)){
				hql+=" and exists(select 1 from ThemeInBank b   "+
              	      "where b.theme.themeId = a.themeId   "+
              	      	"and instr(',' || :themeBankIds || ',' , ',' || b.themeBank.themeBankId || ',') > 0  "+
              	      		"  and b.theme.themeId is not null)  ";
			}
			String qthemeBankIds = (String)paramMap.get("qthemeBankIds");
			if(qthemeBankIds!=null && !"".equals(qthemeBankIds) && !"null".equals(qthemeBankIds)){
				hql+=" and exists(select 1 from ThemeInBank b   "+
	              	      "where b.theme.themeId = a.themeId   "+
	              	      	"and instr(',' || :qthemeBankIds || ',' , ',' || b.themeBank.themeBankId || ',') > 0  "+
	              	   "  and b.theme.themeId is not null)  ";
			}
			String qthemeType = (String)paramMap.get("qthemeType");
			if(qthemeType!=null && !"".equals(qthemeType) && !"null".equals(qthemeType)){
				hql+=" and a.themeType.themeTypeId = :qthemeType ";
			}
			String qgjzName = (String)paramMap.get("qgjzName");
			if(qgjzName!=null && !"".equals(qgjzName) && !"null".equals(qgjzName)){
				hql+=" and  a.themeName  like  '%' || :qgjzName || '%'  ";
			}
			String examType = (String)paramMap.get("examType");
			if(examType!=null){
				if(examType.indexOf(",")!=-1){
					String[] tmp = examType.split(",");
					String examTypeStr = "";
					for(int i=0;i<tmp.length;i++){
						examTypeStr+="'"+tmp[i]+"',";
					}
					if(examTypeStr.length()>0){
						examTypeStr = examTypeStr.substring(0,examTypeStr.length()-1);
					}
					//hql+=" and instr(',' || :examType || ',' ,',' || a.type || ',') > 0 ";
					hql+=" and a.type in("+examTypeStr+") ";
				}else{
					hql+=" and a.type = :examType ";
				}
			}
			String showSign =  (String)paramMap.get("showSign");
			String employeeId = (String)paramMap.get("employeeId");
			if("1".equals(showSign) && employeeId!=null){	
				hql+=" and a.themeId in(select et.themeId from StudyTestpaperThemeSign et "
						+ "  where et.createdIdBy =:employeeId and et.signInTheme ='10' and et.signInTheme is not null)";
			}
			hql+="and  a.isUse = 5 and a.state = 15 ";
			
			//Session session = template.getSessionFactory().getCurrentSession();
			//SQLQuery sqlQuery=session.createSQLQuery(hql);  
			//List<Integer> tmplist = sqlQuery.addScalar("sumvalue", IntegerType.INSTANCE).list();
			//if(tmplist!=null && tmplist.size()>0)count = (Integer)tmplist.get(0);
			List<Long> tmplist = this.queryHql(hql, paramMap, null, null);
			if(tmplist!=null && tmplist.size()>0)count = ((Long)tmplist.get(0)).intValue();
		}catch(Exception e){
			e.printStackTrace();
		}
		return count;
	}
	
	
	
	/**
	 * 根据试题Id获取答案
	 * @description
	 * @param themeIds
	 * @return
	 * @modified
	 */
	public Map<String,List<ThemeAnswerkey>> getThemeAnswerkeyByThemeIds(String themeIds){
		Map<String,List<ThemeAnswerkey>> ansMap = new HashMap<String,List<ThemeAnswerkey>>();
		Map param = new HashMap();
		String hql="from  ThemeAnswerkey a where   a.theme.themeId in ("+themeIds+") order by a.theme.themeId,a.sortNum";
		List<ThemeAnswerkey> ansList = this.queryHql(hql);
		if(ansList!=null){
			for(int i=0;i<ansList.size();i++){
				ThemeAnswerkey themeAnswerkey = (ThemeAnswerkey)ansList.get(i);
				String ansKey = themeAnswerkey.getTheme().getThemeId();
				List<ThemeAnswerkey> tmplist = ansMap.get(ansKey);
				if(tmplist==null)tmplist = new ArrayList<ThemeAnswerkey>();
				tmplist.add(themeAnswerkey);
				ansMap.put(ansKey,tmplist);
			}
		}
		return ansMap;
	}
	
	/**
	 * 同步试卷存在的题库与选择试题的题库一致
	 * @description
	 * @param testpaperId
	 * @param organId
	 * @param organName
	 * @modified
	 */
	public void saveAndSyncThemeBank(String testpaperId,String organId,String organName){
		Session session=template.getSessionFactory().getCurrentSession();
		try{
			String sql = "delete TESTPAPER_SKEY t where t.testpaper_id = '"+testpaperId+"' and t.theme_bank_id "+
				" not in(select tb.theme_bank_id from TESTPAPER_THEME t,theme_in_bank tb  where t.theme_id = tb.theme_id "+
				" and t.testpaper_id = '"+testpaperId+"' group by  tb.theme_bank_id)";
			SQLQuery query = session.createSQLQuery(sql);
			query.executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
		}
		try{
			String sql = "insert into TESTPAPER_SKEY "+
						  "(search_key_id, "+
						   "testpaper_id, "+
						   "theme_bank_name, "+
						   "theme_bank_id, "+
						   "organ_id, "+
						   "organ_name, "+
						   "creation_date, "+
						   "created_by, "+
						   "sort_num) "+
						  "(select to_char(SYSDATE, 'yyyymmddhh24miss') || REPLACE(TRIM(DBMS_RANDOM.VALUE), '.', '') as search_key_id, "+
						         "'"+testpaperId+"' as testpaper_id, "+
						         " b.theme_bank_name, "+
						         " tb.theme_bank_id, "+
						         " '"+organId+"' as organ_id, "+
						         " '"+organName+"' as organ_name, "+
						         " to_char(sysdate, 'yyyy-MM-dd hh24:mi:ss') as creation_date, "+
						         " '系统' as created_by, "+
						          " 999 as sort_num "+
						     "from TESTPAPER_THEME t, theme_in_bank tb, theme_bank b "+
						    "where t.theme_id = tb.theme_id "+
						      "and tb.theme_bank_id = b.theme_bank_id "+
						      "and t.testpaper_id = '"+testpaperId+"' "+
						      "and tb.theme_bank_id not in "+
						          "(select tt.theme_bank_id "+
						             "from TESTPAPER_SKEY tt "+
						            "where tt.testpaper_id = '"+testpaperId+"') "+
						    "group by tb.theme_bank_id, b.theme_bank_name)";
			SQLQuery query = session.createSQLQuery(sql);
			query.executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	

}
