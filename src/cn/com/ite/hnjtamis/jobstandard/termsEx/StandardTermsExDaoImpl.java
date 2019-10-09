package cn.com.ite.hnjtamis.jobstandard.termsEx;

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
import cn.com.ite.eap2.domain.organization.Organ;
import cn.com.ite.hnjtamis.exam.hibernatemap.ThemeBank;
import cn.com.ite.hnjtamis.exam.hibernatemap.ThemeType;
import cn.com.ite.hnjtamis.jobstandard.domain.StandardTerms;
import cn.com.ite.hnjtamis.jobstandard.termsEx.form.EmployeeLearningForm;


/**
 * 岗位标准管理
 * @author 朱健
 * @create time: 2016年3月4日 上午8:59:07
 * @version 1.0
 * 
 * @modified records:
 */
public class StandardTermsExDaoImpl extends HibernateDefaultDAOImpl implements StandardTermsExDao{
	
	/**
	 * 获取模块与子模块Tree
	 * @description
	 * @return
	 * @modified
	 */
	public List<TreeNode> getStandardModelTree(){
		List<TreeNode> trees = new ArrayList<TreeNode>();
		try{
			Session session = template.getSessionFactory().getCurrentSession();
			String sql = "select t.standardid   as id, "+
					       "t.standardname as title, "+
					       "t.jstypeid     as parentId, "+
					       "ty.typename    as pname, "+
					       "tty.jstypeid as pparentId, "+
					       "tty.typename   as ppname "+
					  "from JOBS_STANDARD t, JOBS_STANDARD_TYPES ty, JOBS_STANDARD_TYPES tty "+
					 "where t.jstypeid = ty.jstypeid "+
					   "and ty.parentid = tty.jstypeid "+
					   "and t.isavailable = 1 "+
					   "and ty.isavailable = 1 "+
					"order by tty.orderno,tty.typename,ty.orderno, ty.typename, t.orderno, t.standardname";
			SQLQuery sqlQuery=session.createSQLQuery(sql);   
		    List<Object[]> tmplist = sqlQuery.addScalar("id", StringType.INSTANCE)
				.addScalar("title", StringType.INSTANCE)
				.addScalar("parentId", StringType.INSTANCE)
				.addScalar("pname", StringType.INSTANCE)
				.addScalar("pparentId", StringType.INSTANCE)
				.addScalar("ppname", StringType.INSTANCE)
				.list();
		    Map<String,TreeNode> parentNodeMap = new HashMap<String,TreeNode> ();
		    for(int i=0;i<tmplist.size();i++){
		    	Object[] object = (Object[])tmplist.get(i);
		    	String id = (String)object[0];
		    	String title = (String)object[1];
		    	String pid = (String)object[2];
		    	String pname = (String)object[3];
		    	String ppid = (String)object[4];
		    	String ppname = (String)object[5];
		    	
		    	TreeNode pparentNode = parentNodeMap.get(ppid);
		    	if(pparentNode == null){
		    		pparentNode = new TreeNode();
		    		pparentNode.setId(ppid);
		    		pparentNode.setTitle(ppname);
		    		pparentNode.setParentId(null);
		    		pparentNode.setType("parentModel");
		    		pparentNode.setChildren(new ArrayList<TreeNode>());
			    	
			    	trees.add(pparentNode);
			    	parentNodeMap.put(ppid, pparentNode);
		    	}
		    	
		    	TreeNode parentNode = parentNodeMap.get(pid);
		    	if(parentNode == null){
		    		parentNode = new TreeNode();
		    		parentNode.setId(pid);
		    		parentNode.setTitle(pname);
			    	parentNode.setParentId(pparentNode.getId());
			    	parentNode.setType("parentModel");
			    	parentNode.setChildren(new ArrayList<TreeNode>());
			    	
			    	pparentNode.getChildren().add(parentNode);
			    	parentNodeMap.put(pid, parentNode);
		    	}
		    	
		    	TreeNode node = new TreeNode();
		    	node.setId(id);
		    	node.setTitle(title);
		    	node.setParentId(parentNode.getId());
		    	node.setLeaf(true);
		    	node.setType("stmodel");
		    	node.setChildren(new ArrayList<TreeNode>());
		    	
		    	parentNode.getChildren().add(node);
		    }
		}catch(Exception e){
			e.printStackTrace();
		}
		return trees;
			 
	}
	
	/**
	 * 根据老的题库ID更新为新的题库ID
	 * @author 朱健
	 * @param standardid
	 * @param newBankId
	 * @param oldBankId
	 * @modified
	 */
	public void updateStandardQuarterBank(String standardid,String newBankId,String oldBankId){
		try{
			Session session=template.getSessionFactory().getCurrentSession();
			
			if(newBankId==null){
				String sql1="update JOBS_STANDARD_QUARTER t "+
						   " set t.theme_bank_id = null  where t.theme_bank_id = '"+oldBankId+"'"
						   		+ " and t.standardid ='"+standardid+"' ";
				session.createSQLQuery(sql1).executeUpdate();
			}else{
				String sql1="update JOBS_STANDARD_QUARTER t "+
						   " set t.theme_bank_id = '"+newBankId+"'  where t.theme_bank_id = '"+oldBankId+"'"
						   		+ " and t.standardid ='"+standardid+"' ";
				session.createSQLQuery(sql1).executeUpdate();
			}
			
 
 
			String sql2=" update JOBS_STANDARD_QUARTER t  "+
		       " set t.theme_bank_code =   "+
		       " (select v.theme_bank_code from theme_bank v where v.theme_bank_id = t.theme_bank_id ) ,  "+
		       " t.theme_bank_name =  "+
		       "  (select v.theme_bank_name from theme_bank v where v.theme_bank_id = t.theme_bank_id ) ";
			session.createSQLQuery(sql2).executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 根据老的题库ID更新为新的题库ID
	 * @author 朱健
	 * @param standardid
	 * @param newBankId
	 * @param oldBankId
	 * @modified
	 */
	public void updateMoreStandardQuarterBank(String standardid){
		try{
			Session session=template.getSessionFactory().getCurrentSession();
			
			/*if(newBankId==null){
				String sql1="update JOBS_STANDARD_QUARTER t "+
						   " set t.theme_bank_id = null  where t.theme_bank_id = '"+oldBankId+"'"
						   		+ " and t.standardid ='"+standardid+"' ";
				session.createSQLQuery(sql1).executeUpdate();
			}else{
				String sql1="update JOBS_STANDARD_QUARTER t "+
						   " set t.theme_bank_id = '"+newBankId+"'  where t.theme_bank_id = '"+oldBankId+"'"
						   		+ " and t.standardid ='"+standardid+"' ";
				session.createSQLQuery(sql1).executeUpdate();
			}*/
			String sql0 = "update JOBS_STANDARD_THEMEBANK t  set t.theme_bank_code = "+
                  " (select v.theme_bank_code from theme_bank v  where v.theme_bank_id = t.theme_bank_id) "+
                  		" where t.standardid = '"+standardid+"' ";
            session.createSQLQuery(sql0).executeUpdate();
            
            String sql00 = "delete  JOBS_STANDARD_THEMEBANK t where t.theme_bank_id not in("
            		+ " select b.theme_bank_id from theme_bank b where b.is_l = '10')";
            session.createSQLQuery(sql00).executeUpdate();
			
			String sql1 = "update JOBS_STANDARD_QUARTER t set t.last_updated_by = 'SYSDEL' where t.standardid = '"+standardid+"'";
			session.createSQLQuery(sql1).executeUpdate();
			
			
			String sql2 = "insert into JOBS_STANDARD_QUARTER "+
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
							 " (select to_char(SYSDATE, 'yyyymmddhh24miss') || "+
							          "REPLACE(TRIM(DBMS_RANDOM.VALUE), '.', '') as RELATION_ID, "+
							          "t1.STANDARDID as STANDARDID, "+
							          "t1.QUARTER_TRAIN_CODE, "+
							          "t1.QUARTER_TRAIN_NAME, "+
							          "t1.ORDERNO, "+
							          "'' as LAST_UPDATE_DATE, "+
							          "'' as LAST_UPDATED_BY, "+
							          "t1.CREATION_DATE, "+
							          "t1.CREATED_BY, "+
							          "t1.DEPT_NAME, "+
							          "t1.DEPT_ID, "+
							          "t1.SPECIALITY_NAME, "+
							          "t1.SPECIALITY_CODE, "+
							          "t1.DC_TYPE, "+
							          "t1.QUARTER_ID, "+
							          "t2.THEME_BANK_ID, "+
							          "t2.THEME_BANK_NAME, "+
							          "t2.THEME_BANK_CODE, "+
							          "t1.PARENT_TRAIN_QUARTER_ID, "+
							          "t1.PARENT_TRAIN_QUARTER_NAME, "+
							          "t1.PARENT_TRAIN_QUARTER_CODE "+
							     "from (select STANDARDID, "+
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
							                  "PARENT_TRAIN_QUARTER_ID, "+
							                  "PARENT_TRAIN_QUARTER_NAME, "+
							                  "PARENT_TRAIN_QUARTER_CODE "+
							             "from JOBS_STANDARD_QUARTER q "+
							            "where q.STANDARDID = '"+standardid+"' "+
							              "and q.LAST_UPDATED_BY = 'SYSDEL' "+
								               "  group by STANDARDID, "+
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
								                  "PARENT_TRAIN_QUARTER_ID, "+
								                  "PARENT_TRAIN_QUARTER_NAME, "+
								                  "PARENT_TRAIN_QUARTER_CODE "
							              + ") t1, "+
							          "(select distinct t.theme_bank_id, "+
							                  "t.theme_bank_name, "+
							                  "t.theme_bank_code, "+
							                  "t.standardid "+
							             "from JOBS_STANDARD_THEMEBANK t "+
							            "where t.standardid = '"+standardid+"') t2 "+
							    "where t1.standardid = t2.standardid)";
			session.createSQLQuery(sql2).executeUpdate();
 
			
			String sql3 = "delete JOBS_STANDARD_QUARTER t where t.standardid = '"+standardid+"' and t.last_updated_by = 'SYSDEL'";
			session.createSQLQuery(sql3).executeUpdate();
			
			
			/*String sql2=" update JOBS_STANDARD_QUARTER t  "+
		       " set t.theme_bank_code =   "+
		       " (select v.theme_bank_code from theme_bank v where v.theme_bank_id = t.theme_bank_id ) ,  "+
		       " t.theme_bank_name =  "+
		       "  (select v.theme_bank_name from theme_bank v where v.theme_bank_id = t.theme_bank_id ) ";
			session.createSQLQuery(sql2).executeUpdate();*/
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/** 
	 * 获取标准岗位对应的题库
	 * @author 朱健
	 * @param standardid
	 * @return
	 * @modified
	 */
	public Map<String,List<ThemeBank>> queryBankIdByStandardQuarter(String standardid){
		Map<String,List<ThemeBank>> value = new HashMap<String,List<ThemeBank>>();
		try{
			String sql = "select {tb.*},t.quarter_train_code,t.dept_name  "
					+ " from JOBS_STANDARD_QUARTER t,theme_bank tb where t.standardid = ? "
					+ " and t.theme_bank_id = tb.theme_bank_id  ";
			HibernateTemplate template = (HibernateTemplate) this.template;		   
			Session session = template.getSessionFactory().getCurrentSession();
			SQLQuery sqlQuery=session.createSQLQuery(sql);
			sqlQuery.setString(0, standardid);
			List querylist = sqlQuery.addEntity("tb", ThemeBank.class)
					.addScalar("quarter_train_code", StringType.INSTANCE)
					.addScalar("dept_name", StringType.INSTANCE).list();
			for(int i=0;i<querylist.size();i++){
				Object[] vl = (Object[])querylist.get(i);
				ThemeBank themeBank = (ThemeBank)vl[0];
				String quarter_train_code = (String)vl[1];
				String dept_name = (String)vl[2];
				List tmplist = value.get(dept_name+"_"+quarter_train_code);
				if(tmplist == null){
					tmplist = new ArrayList();
				}
				tmplist.add(themeBank);
				value.put(dept_name+"_"+quarter_train_code, tmplist);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return value;
	}
	
	/**
	 * 根据类型查询标准信息
	 * @author 朱健
	 * @param typeId
	 * @return
	 * @modified
	 */
	public List<StandardTerms> queryStandardTermsByTypeId(String typeId,Map paramMap){
		List<StandardTerms> list = new ArrayList<StandardTerms>();
		try{
			String sql = "select {t.*},v.typename typename,v.jstypeid stypeid,p.typename parentname,p.jstypeid ptypeid "+
					  " from JOBS_STANDARD t,jobs_standard_types v,jobs_standard_types p "+
					 " where t.JSTYPEID = ? "+
					   " and t.jstypeid = v.jstypeid "+
					   " and v.parentid = p.jstypeid "+
					   " and t.ISAVAILABLE <> 0 ";
			String standardnameTerm = (String)paramMap.get("standardnameTerm");
			if(standardnameTerm!=null && !"".equals(standardnameTerm) && !"null".equals(standardnameTerm)){
				sql+=" and t.standardname like '%' || ? || '%' ";
			}
			sql+=" order by t.ORDERNO ";
			HibernateTemplate template = (HibernateTemplate) this.template;		   
			Session session = template.getSessionFactory().getCurrentSession();
			SQLQuery sqlQuery=session.createSQLQuery(sql);
			sqlQuery.setString(0, typeId);
			if(standardnameTerm!=null && !"".equals(standardnameTerm) && !"null".equals(standardnameTerm)){
				sqlQuery.setString(1, standardnameTerm);
			}
			List querylist = sqlQuery.addEntity("t", StandardTerms.class)
					.addScalar("typename", StringType.INSTANCE)
					.addScalar("stypeid", StringType.INSTANCE)
					.addScalar("parentname", StringType.INSTANCE)
					.addScalar("ptypeid", StringType.INSTANCE)
					.list();
			for(int i=0;i<querylist.size();i++){
				Object[] object = (Object[])querylist.get(i);
				StandardTerms standardTerms = (StandardTerms)object[0];
				String typename = (String)object[1];
				String stypeid = (String)object[2];
				String parentname = (String)object[3];
				String ptypeid = (String)object[4];
				standardTerms.setParentTypeName(parentname);
				standardTerms.setTypename(typename);
				list.add(standardTerms);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
		
	}
	
	
	/**
	 * 根据父类型查询标准信息
	 * @author 朱健
	 * @param typeId
	 * @return
	 * @modified
	 */
	public List<StandardTerms> queryStandardTermsByParentTypeId(String typeId,Map paramMap){
		List<StandardTerms> list = new ArrayList<StandardTerms>();
		try{
			String sql = "select {t.*},v.typename typename,v.jstypeid stypeid,p.typename parentname,p.jstypeid ptypeid "+
					  " from JOBS_STANDARD t,jobs_standard_types v,jobs_standard_types p "+
					 " where p.jstypeid = ? "+
					   " and t.jstypeid = v.jstypeid "+
					   " and v.parentid = p.jstypeid "+
					   " and t.ISAVAILABLE <> 0 ";
			String standardnameTerm = (String)paramMap.get("standardnameTerm");
			if(standardnameTerm!=null && !"".equals(standardnameTerm) && !"null".equals(standardnameTerm)){
				sql+=" and t.standardname like '%' || ? || '%' ";
			}
			sql+=" order by t.ORDERNO ";
			HibernateTemplate template = (HibernateTemplate) this.template;		   
			Session session = template.getSessionFactory().getCurrentSession();
			SQLQuery sqlQuery=session.createSQLQuery(sql);
			sqlQuery.setString(0, typeId); 
			if(standardnameTerm!=null && !"".equals(standardnameTerm) && !"null".equals(standardnameTerm)){
				sqlQuery.setString(1, standardnameTerm);
			}
			List querylist = sqlQuery.addEntity("t", StandardTerms.class)
					.addScalar("typename", StringType.INSTANCE)
					.addScalar("stypeid", StringType.INSTANCE)
					.addScalar("parentname", StringType.INSTANCE)
					.addScalar("ptypeid", StringType.INSTANCE)
					.list();
			for(int i=0;i<querylist.size();i++){
				Object[] object = (Object[])querylist.get(i);
				StandardTerms standardTerms = (StandardTerms)object[0];
				String typename = (String)object[1];
				String stypeid = (String)object[2];
				String parentname = (String)object[3];
				String ptypeid = (String)object[4];
				standardTerms.setParentTypeName(parentname);
				standardTerms.setTypename(typename);
				list.add(standardTerms);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
		
	}
	
	
	/**
	 * 获取顶级的节点
	 * @description
	 * @return
	 * @modified
	 */
	public List<StandardTerms> getStandardTopTypeList(){
		List<StandardTerms> list = new ArrayList<StandardTerms>();
		try{
			String sql = "select t.jstypeid,t.typename from jobs_standard_types t "
				+ " where t.parentname is null and t.isavailable = 1 order by t.orderno,t.jstypeid ";
			HibernateTemplate template = (HibernateTemplate) this.template;		   
			Session session = template.getSessionFactory().getCurrentSession();
			SQLQuery sqlQuery=session.createSQLQuery(sql);
			List querylist = sqlQuery.addScalar("jstypeid", StringType.INSTANCE)
					.addScalar("typename", StringType.INSTANCE).list();
			for(int i=0;i<querylist.size();i++){
				Object[] object = (Object[])querylist.get(i);
				StandardTerms standardTerms = new StandardTerms();
				String jstypeid = (String)object[0];
				String typename = (String)object[1];
				standardTerms.setTypename(typename);
				standardTerms.setTypeId(jstypeid);
				list.add(standardTerms);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 根据岗位查询标准信息
	 * @author 朱健
	 * @param typeId
	 * @return
	 * @modified
	 */
	public List<StandardTerms> queryStandardTermsByQuarterId(String typeId,Map paramMap){
		List<StandardTerms> list = new ArrayList<StandardTerms>();
		try{
			String sql = "select distinct {t.*},v.typename typename,v.jstypeid stypeid,p.typename parentname,p.jstypeid ptypeid,q.parent_train_quarter_id "+
					  " from JOBS_STANDARD t,jobs_standard_types v,jobs_standard_types p,"
					  + " (select * from jobs_standard_quarter qq  where qq.quarter_train_code = ? ) q "+
					 " where t.jstypeid = v.jstypeid "+
					   " and v.parentid = p.jstypeid "+
					   " and t.standardid = q.standardid and t.ISAVAILABLE <> 0 ";
			String standardnameTerm = (String)paramMap.get("standardnameTerm");
			if(standardnameTerm!=null && !"".equals(standardnameTerm) && !"null".equals(standardnameTerm)){
				sql+=" and t.standardname like '%' || ? || '%' ";
			}
			sql+=" order by t.ORDERNO ";
			HibernateTemplate template = (HibernateTemplate) this.template;		   
			Session session = template.getSessionFactory().getCurrentSession();
			SQLQuery sqlQuery=session.createSQLQuery(sql);
			sqlQuery.setString(0, typeId); 
			if(standardnameTerm!=null && !"".equals(standardnameTerm) && !"null".equals(standardnameTerm)){
				sqlQuery.setString(1, standardnameTerm);
			}
			List querylist = sqlQuery.addEntity("t", StandardTerms.class)
					.addScalar("typename", StringType.INSTANCE)
					.addScalar("stypeid", StringType.INSTANCE)
					.addScalar("parentname", StringType.INSTANCE)
					.addScalar("ptypeid", StringType.INSTANCE)
					.addScalar("parent_train_quarter_id", StringType.INSTANCE)
					.list();
			for(int i=0;i<querylist.size();i++){
				Object[] object = (Object[])querylist.get(i);
				StandardTerms standardTerms = (StandardTerms)object[0];
				String typename = (String)object[1];
				String stypeid = (String)object[2];
				String parentname = (String)object[3];
				String ptypeid = (String)object[4];
				String parentTrainQuarterId = (String)object[5];
				standardTerms.setParentTypeName(parentname);
				standardTerms.setParentTypeId(ptypeid);
				standardTerms.setTypename(typename);
				standardTerms.setTypeId(stypeid);
				standardTerms.setParentTrainQuarterId(parentTrainQuarterId);
				list.add(standardTerms);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
		
	}
	
	/**
	 * 根据不属于该岗位查询标准信息
	 * @author 朱健
	 * @param typeId
	 * @return
	 * @modified
	 */
	public List<StandardTerms> queryUnStandardTermsByQuarterId(String typeId,Map paramMap){
		List<StandardTerms> list = new ArrayList<StandardTerms>();
		try{
			String sql = "select {t.*},v.typename typename,v.jstypeid stypeid,p.typename parentname,p.jstypeid ptypeid "+
					  " from JOBS_STANDARD t,jobs_standard_types v,jobs_standard_types p "+
					 " where t.jstypeid = v.jstypeid "+
					   " and v.parentid = p.jstypeid "+
					   " and t.ISAVAILABLE <> 0 "
					   + " and not exists(select 1 from jobs_standard_quarter q where q.quarter_train_code = ?  and t.standardid = q.standardid) ";
			String standardnameTerm = (String)paramMap.get("standardnameTerm");
			if(standardnameTerm!=null && !"".equals(standardnameTerm) && !"null".equals(standardnameTerm)){
				sql+=" and t.standardname like '%' || ? || '%' ";
			}
			String modelIdTerm = (String)paramMap.get("modelIdTerm");
			if(modelIdTerm!=null && !"".equals(modelIdTerm) && !"null".equals(modelIdTerm)){
				sql+=" and t.jstypeid = ? ";
			}
			sql+=" order by t.ORDERNO ";
			HibernateTemplate template = (HibernateTemplate) this.template;		   
			Session session = template.getSessionFactory().getCurrentSession();
			SQLQuery sqlQuery=session.createSQLQuery(sql);
			sqlQuery.setString(0, typeId); 
			int indexTerm = 1;
			if(standardnameTerm!=null && !"".equals(standardnameTerm) && !"null".equals(standardnameTerm)){
				sqlQuery.setString(indexTerm, standardnameTerm);
				indexTerm++;
			}
			if(modelIdTerm!=null && !"".equals(modelIdTerm) && !"null".equals(modelIdTerm)){
				sqlQuery.setString(indexTerm, modelIdTerm);
				indexTerm++;
			}
			List querylist = sqlQuery.addEntity("t", StandardTerms.class)
					.addScalar("typename", StringType.INSTANCE)
					.addScalar("stypeid", StringType.INSTANCE)
					.addScalar("parentname", StringType.INSTANCE)
					.addScalar("ptypeid", StringType.INSTANCE)
					.list();
			for(int i=0;i<querylist.size();i++){
				Object[] object = (Object[])querylist.get(i);
				StandardTerms standardTerms = (StandardTerms)object[0];
				String typename = (String)object[1];
				String stypeid = (String)object[2];
				String parentname = (String)object[3];
				String ptypeid = (String)object[4];
				standardTerms.setParentTypeName(parentname);
				standardTerms.setTypename(typename);
				list.add(standardTerms);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
		
	}

	/**
	 * 查询全部标准信息
	 * @author 朱健
	 * @param paramMap
	 * @return
	 * @modified
	 */
	public List<StandardTerms> queryAllStandardTerms(Map paramMap){
		List<StandardTerms> list = new ArrayList<StandardTerms>();
		try{
			String sql = "select {t.*},v.typename typename,v.jstypeid stypeid,p.typename parentname,p.jstypeid ptypeid "+
					  " from JOBS_STANDARD t,jobs_standard_types v,jobs_standard_types p "+
					 " where t.jstypeid = v.jstypeid "+
					   " and v.parentid = p.jstypeid "+
					   " and t.ISAVAILABLE <> 0 ";
			String standardnameTerm = (String)paramMap.get("standardnameTerm");
			if(standardnameTerm!=null && !"".equals(standardnameTerm) && !"null".equals(standardnameTerm)){
				sql+=" and t.standardname like '%' || ? || '%' ";
			}
			String modelIdTerm = (String)paramMap.get("modelIdTerm");
			if(modelIdTerm!=null && !"".equals(modelIdTerm) && !"null".equals(modelIdTerm)){
				sql+=" and t.jstypeid = ? ";
			}
			sql+=" order by t.ORDERNO ";
			HibernateTemplate template = (HibernateTemplate) this.template;		   
			Session session = template.getSessionFactory().getCurrentSession();
			SQLQuery sqlQuery=session.createSQLQuery(sql);
			int pindex = 0;
			if(standardnameTerm!=null && !"".equals(standardnameTerm) && !"null".equals(standardnameTerm)){
				sqlQuery.setString(pindex++, standardnameTerm);
			}
			if(modelIdTerm!=null && !"".equals(modelIdTerm) && !"null".equals(modelIdTerm)){
				sqlQuery.setString(pindex++, modelIdTerm);
			}
			List querylist = sqlQuery.addEntity("t", StandardTerms.class)
					.addScalar("typename", StringType.INSTANCE)
					.addScalar("stypeid", StringType.INSTANCE)
					.addScalar("parentname", StringType.INSTANCE)
					.addScalar("ptypeid", StringType.INSTANCE)
					.list();
			for(int i=0;i<querylist.size();i++){
				Object[] object = (Object[])querylist.get(i);
				StandardTerms standardTerms = (StandardTerms)object[0];
				String typename = (String)object[1];
				String stypeid = (String)object[2];
				String parentname = (String)object[3];
				String ptypeid = (String)object[4];
				standardTerms.setParentTypeName(parentname);
				standardTerms.setTypename(typename);
				list.add(standardTerms);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
		
	}
	
	
	/**
	 * 查询个人的岗位达标情况
	 * @author 朱健
	 * @param employeeId
	 * @return
	 * @modified
	 */
	public EmployeeLearningForm getEmployeeStandardLearning(String employeeId){
		EmployeeLearningForm employeeLearningForm = new EmployeeLearningForm();
		employeeLearningForm.setEmployeeId(employeeId);
		try{
			HibernateTemplate template = (HibernateTemplate) this.template;		   
			Session session = template.getSessionFactory().getCurrentSession();
			//查询岗位信息
			String sql1 = " select qs.quarter_name quartername from employee e,quarter q ,quarter_standard qs "
					+ " where e.quarter_id = q.quarter_id and q.quarter_train_code = qs.quarter_code"
					+ " and e.employee_id = ? ";
			SQLQuery sqlQuery=session.createSQLQuery(sql1);
			sqlQuery.setString(0, employeeId);
			List querylist = sqlQuery.addScalar("quartername", StringType.INSTANCE).list();
			if(querylist!=null && querylist.size()>0){
				employeeLearningForm.setQuartername((String)querylist.get(0));
			}
					
			//查询是否合格，合格题库数量		
			String sql2 = "select kk2.EMPLOYEE_ID EMPLOYEEID, "+
					       "decode(kk1.passnum, null, 0, kk1.passnum) passnum, "+
					       "kk2.themebanknum "+
					  "from (select k1.EMPLOYEE_ID, count(*) passnum "+
					          "from v_employee_pass_themebank k1 "+
					         "where not exists (select 1 "+
					                  "from v_employee_pass_themebank ka "+
					                 "where ka.score_end_time < ? "+
					                    "or ka.score_start_time > ? "+
					                   "and k1.EMPLOYEE_ID = ka.EMPLOYEE_ID "+
					                   "and k1.theme_bank_id = ka.theme_bank_id "+
					                   "and k1.EMPLOYEE_ID = ?) "+
					         "group by k1.EMPLOYEE_ID) kk1, "+
					       "(select k2.EMPLOYEE_ID, count(*) themebanknum "+
					          "from v_employee_themebank k2 "+
					          "where k2.EMPLOYEE_ID = ? "+
					         "group by k2.EMPLOYEE_ID) kk2 "+
					 "where kk2.EMPLOYEE_ID = kk1.EMPLOYEE_ID(+) "+
					 "and kk2.EMPLOYEE_ID = ?";
			sqlQuery=session.createSQLQuery(sql2);
			String nowDay = DateUtils.convertDateToStr(new Date(),"yyyy-MM-dd");
			sqlQuery.setString(0, nowDay);
			sqlQuery.setString(1, nowDay);
			sqlQuery.setString(2, employeeId);
			sqlQuery.setString(3, employeeId);
			sqlQuery.setString(4, employeeId);
			querylist = sqlQuery
					.addScalar("EMPLOYEEID", StringType.INSTANCE)
					.addScalar("passnum", IntegerType.INSTANCE)
					.addScalar("themebanknum", IntegerType.INSTANCE).list();
			if(querylist!=null && querylist.size()>0){
				Object[] values = (Object[])querylist.get(0);
				employeeLearningForm.setPassnum(values[1]!=null? ((Integer)values[1]).intValue() : -1);
				employeeLearningForm.setLengrningnum(values[2]!=null? ((Integer)values[2]).intValue() : -1);
				if(employeeLearningForm.getPassnum() == -1 || employeeLearningForm.getLengrningnum() == -1){
					employeeLearningForm.setLengrningPass("未达标");
				}else if(employeeLearningForm.getPassnum() >= employeeLearningForm.getLengrningnum()){
					employeeLearningForm.setLengrningPass("已达标");
				}else if(employeeLearningForm.getPassnum() >= employeeLearningForm.getLengrningnum()){
					employeeLearningForm.setLengrningPass("未达标");
				}else{
					employeeLearningForm.setLengrningPass("未达标");
				}
			}else{
				employeeLearningForm.setLengrningPass("未达标");
			}
			
			//查询有效期
			String sql3 = "select min(k1.score_end_time) scoreEndTime "+
				    " from v_employee_pass_themebank k1 "+
				 "  where not exists (select 1 from v_employee_pass_themebank ka "+
					  " where ka.score_end_time < ? "+
					  "   or ka.score_start_time > ? "+
					  " and k1.EMPLOYEE_ID = ka.EMPLOYEE_ID "+
					  " and k1.theme_bank_id = ka.theme_bank_id "+
					   " and ka.EMPLOYEE_ID = ?) and k1.EMPLOYEE_ID = ? ";
			sqlQuery=session.createSQLQuery(sql3);
			sqlQuery.setString(0, nowDay);
			sqlQuery.setString(1, nowDay);
			sqlQuery.setString(2, employeeId);
			sqlQuery.setString(3, employeeId);
			querylist = sqlQuery
					.addScalar("scoreEndTime", StringType.INSTANCE).list();
			if(querylist!=null && querylist.size()>0){
				String scoreEndTime = (String)querylist.get(0);
				if(scoreEndTime!=null && !"".equals(scoreEndTime) && !"null".equals(scoreEndTime)){
					employeeLearningForm.setPassEndDay(scoreEndTime);
				}else{
					employeeLearningForm.setPassEndDay("--");
				}
				
			}else{
				employeeLearningForm.setPassEndDay("--");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return employeeLearningForm;
	}
}
