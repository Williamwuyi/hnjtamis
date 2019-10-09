package cn.com.ite.hnjtamis.exam.base.themebank.dao;




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
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;
import org.springframework.orm.hibernate4.HibernateTemplate;

import cn.com.ite.eap2.core.hibernate.HibernateDefaultDAOImpl;
import cn.com.ite.eap2.core.service.TreeNode;
import cn.com.ite.hnjtamis.common.StaticVariable;
import cn.com.ite.hnjtamis.exam.hibernatemap.Theme;
import cn.com.ite.hnjtamis.exam.hibernatemap.ThemeBank;


public class ThemeBankDaoImpl extends HibernateDefaultDAOImpl implements ThemeBankDao{
	
	
	/**
	 * 查询专业题库（顶级）
	 * @description
	 * @return
	 * @modified
	 */
	public List<ThemeBank> getProfessionTopThemeBanks(){
		List<ThemeBank> list = new ArrayList<ThemeBank>();
		String hql = " from ThemeBank b where  b.bankType = '20' "+
						" and b.isL = '10' order by b.themeBankCode ";
		List<ThemeBank> themeBanklist = this.queryHql(hql, null, null, ThemeBank.class);
		Map<String,List<ThemeBank>> themeBankMap = new HashMap();
		for(int i=0;i<themeBanklist.size();i++){
			ThemeBank themeBank = themeBanklist.get(i);
			if(themeBank.getThemeBank()==null){
				list.add(themeBank);
			}else{
				List<ThemeBank> tmplist = themeBankMap.get(themeBank.getThemeBank().getThemeBankId());
				if(tmplist == null) tmplist = new ArrayList<ThemeBank>();
				tmplist.add(themeBank);
				themeBankMap.put(themeBank.getThemeBank().getThemeBankId(),tmplist);
			}
		}
		for(int i=0;i<list.size();i++){
			ThemeBank themeBank = list.get(i);
			List<ThemeBank> themeBanks = themeBankMap.get(themeBank.getThemeBankId());
			if(themeBanks!=null){
				themeBank.setThemeBanks(themeBanks);
			}
		}
		return list;
	}

	
	/**
	 * 更新关联表对应题库的一些信息
	 * @description
	 * @param theme_bank_id
	 * @param bankOrganId
	 * @param bankOrganName
	 * @param bank_public
	 * @param bank_type
	 * @modified
	 */
	public void updateBankInRelation(String theme_bank_id,String bankOrganId,
			String bankOrganName,String bank_public,String bank_type){
		String sql = "update THEME_IN_BANK t "+
		   "set t.bank_organ_id = '"+bankOrganId+"', "+
		       "t.bank_organ_name = '"+bankOrganName+"', "+
		       "t.bank_public = '"+bank_public+"', "+
		       "t.bank_type = '"+bank_type+"' where t.theme_bank_id = '"+theme_bank_id+"'";
		this.saveSQL(sql);
	}
	
	/**
	 * 删除题库对应的试题关联
	 * @description
	 * @param theme_bank_id
	 * @modified
	 */
	public void deleteThemeInBank(String theme_bank_id){
		String sql2 = "delete TRAIN_IMPLEMENT_THEME_BANK t where t.theme_bank_id= '"+theme_bank_id+"'";
		this.saveSQL(sql2);
		String sql = "delete THEME_IN_BANK t where theme_bank_id = '"+theme_bank_id+"'";
		this.saveSQL(sql);
	}
	
	/**
	 * 获取题库对应有效的试题数目
	 * @description
	 * @param theme_bank_id
	 * @return
	 * @modified
	 */
	public int getThemeNumInBank(String theme_bank_id){
		int len = 999;
		try{
			Session session = template.getSessionFactory().getCurrentSession();
			String sql = "select count(*) as sumvalue "+
					  "from (select ts.theme_id "+
					          "from THEME_IN_BANK ts "+
					         "where ts.theme_bank_id = ?) tt, "+
					       "THEME t "+
					 "where tt.theme_id = t.theme_id "+
					   "and t.IS_USE = '5'";
			SQLQuery sqlQuery=session.createSQLQuery(sql);  
			sqlQuery.setString(0, theme_bank_id);
		    List<Integer> tmplist = sqlQuery.addScalar("sumvalue", IntegerType.INSTANCE)
				.list();
		    if(tmplist!=null && tmplist.size()>0){
		    	len = tmplist.get(0);
		    }
		}catch(Exception e){
			e.printStackTrace();
		}
		return len;
	}
	/**
	 * 获取专业与题库树
	 * @description
	 * @return
	 * @throws Exception
	 * @modified
	 */
	public List<TreeNode> specialityThemeBankTree(Map paramMap)throws Exception{
		List list = new ArrayList();
		try{
			Session session = template.getSessionFactory().getCurrentSession();
			String sql = "select distinct decode(sp.specialityid,null,'otherNode',sp.specialityid) as specialityid, "+
						       "decode(sp.specialityid,null,'其它',sp.specialityname) as newspecialityname, "+
						       "tb.theme_bank_id, "+
						       "tb.theme_bank_name,sp.orderno, sp.specialityname, tb.theme_bank_code, tb.sort_num "+
						  "from (select bs.specialityid, "+
						               "'(' || bst.typename || ')' || bs.specialityname as specialityname, "+
						               "js.theme_bank_id, "+
						               "bs.orderno "+
						          "from BASE_SPECIALITY                bs, "+
						               "BASE_SPECIALITY_TYPE           bst, "+
						               "BASE_SPECIALITY_STANDARD_TYPES t, "+
						               "JOBS_STANDARD                  js "+
						         "where bs.bstid = bst.bstid "+
						           "and bs.specialityid = t.specialityid "+
						           "and t.types_id = js.standardid) sp, "+
						       "(select * from theme_bank tt where tt.is_l = '10' ";
			String notHaveIds = (String)paramMap.get("notHaveIds");
			if(notHaveIds!=null && !"".equals(notHaveIds)  && !"null".equals(notHaveIds) && !"undefined".equals(notHaveIds)){
				String[] ids = notHaveIds.split(",");
				String tmpIds = "";
				for(int i=0;i<ids.length;i++){
					tmpIds+="'"+ids[i]+"',";
				}
				if(tmpIds.length()>0){
					sql+=" and tt.theme_bank_id not in("+tmpIds.substring(0,tmpIds.length()-1)+") ";
				}
			}
			String organId = (String)paramMap.get("organId");
			String op = (String)paramMap.get("op");
			if("onlydcsf".equals(op)){//仅电厂私有
				sql+=" and tt.bank_public = '20' and tt.organ_id ='"+organId+"' ";
			}else if("onlyall".equals(op)){//仅电厂公有
				sql+=" and tt.bank_public = '10' ";
			}else if("all".equals(op)){//公有与电厂私有
				
			}else{//公有与电厂私有
				sql+=" and (tt.bank_public = '10' or (tt.bank_public = '20' and tt.organ_id ='"+organId+"')) ";
			}
			String bankType = (String)paramMap.get("bankType");
			if(bankType!=null){
				sql+=" and tt.bank_Type = '"+bankType+"' ";
			}
			
			String notZero = (String)paramMap.get("notZero");
			if(notZero!=null && "true".equals(notZero)){
				sql+=" and exists(select 1 from THEME_IN_BANK vv,theme te where vv.theme_bank_id = tt.theme_bank_id and vv.theme_id=te.theme_id and te.is_Use = 5 and te.state = 15)  ";
			}
			sql+=" ) tb "
					+ " where sp.theme_bank_id(+) = tb.theme_bank_id "
			  + " order by sp.orderno, sp.specialityname,  tb.theme_bank_code ,tb.sort_num ";
			SQLQuery sqlQuery=session.createSQLQuery(sql);   
		    List<Object[]> tmplist = sqlQuery.addScalar("theme_bank_id", StringType.INSTANCE)
				.addScalar("theme_bank_name", StringType.INSTANCE)
				.addScalar("specialityid", StringType.INSTANCE)
				.addScalar("newspecialityname", StringType.INSTANCE)
				.list();
		    Map childMap = new HashMap();
		    Map<String,TreeNode> parentMap = new HashMap<String,TreeNode>();
		    Map<String,String> themeBankIdsMap = new HashMap<>();
		    for(int i=0;i<tmplist.size();i++){
		    	Object[] object = (Object[])tmplist.get(i);
		    	String theme_bank_id = (String)object[0];
		    	String theme_bank_name = (String)object[1];
		    	String specialityid = (String)object[2];
		    	String specialityname = (String)object[3];
		    	//String organid = (String)object[4];
		    	if(themeBankIdsMap.get(theme_bank_id) == null){
		    		themeBankIdsMap.put(theme_bank_id,"a"); 
			    	TreeNode parentNode = parentMap.get(specialityid);
			    	if(parentNode == null){
			    		parentNode = new TreeNode();
				    	parentNode.setId(specialityid);
				    	parentNode.setTitle(specialityname);
				    	parentNode.setParentId(null);
				    	parentNode.setType("speciality");
				    	parentNode.setChildren(new ArrayList());
				    	
				    	list.add(parentNode);
				    	parentMap.put(parentNode.getId(), parentNode);
			    	}
			    	
			    	
			    	TreeNode node = new TreeNode();
			    	node.setId(theme_bank_id);
			    	node.setTitle(theme_bank_name);
			    	node.setParentId(parentNode.getId());
			    	node.setType("bank");
			    	node.setChildren(new ArrayList());
			    	node.setLeaf(true);
			    	parentNode.getChildren().add(node);
		    	}
		    	
		    }
		    TreeNode.putTypeIncon("speciality", "resources/icons/fam/theme.gif", "");
			TreeNode.putTypeIncon("specialitytype", "resources/icons/fam/plugin_add.gif", "");
			TreeNode.putTypeIncon("bank", "resources/icons/fam/organ.gif", "");
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}
	
	
	/**
	 * 查询专业题库
	 * @description
	 * @return
	 * @modified
	 */
	public TreeNode getProThemeBankTree(){
		List<TreeNode> trees = new ArrayList<TreeNode>();
		try{
			Session session = template.getSessionFactory().getCurrentSession();
			String  sql = " select d.theme_bank_id as id, "+
				               "d.theme_bank_name as title, "+
				               "d.THE_THEME_BANK_ID as parentId, "+
				               "'bank' as type, "+
				               "d.SORT_NUM "+
				          " from (select * from THEME_BANK dd "+
				         " where dd.is_L = '10' and dd.bank_Type = '20'  ) d start with d.THE_THEME_BANK_ID is null ";
			sql+=" connect by prior d.theme_bank_id = d.THE_THEME_BANK_ID "
				 + " order SIBLINGS BY d.theme_bank_code,d.SORT_NUM ";
			SQLQuery sqlQuery=session.createSQLQuery(sql);   
		    List<Object[]> tmplist = sqlQuery.addScalar("id", StringType.INSTANCE)
				.addScalar("title", StringType.INSTANCE)
				.addScalar("parentId", StringType.INSTANCE)
				.addScalar("type", StringType.INSTANCE)
				.list();
		    Map<String,List<TreeNode>> childMap = new HashMap<String,List<TreeNode>>();
		    for(int i=0;i<tmplist.size();i++){
		    	Object[] object = (Object[])tmplist.get(i);
		    	String id = (String)object[0];
		    	String title = (String)object[1];
		    	String pid = (String)object[2];
		    	String type = (String)object[3];

		    	if(pid==null || "".equals(pid)){
		    		TreeNode node = new TreeNode();
			    	node.setId(id);
			    	node.setTitle(title);
			    	node.setParentId(null);
			    	node.setType(type);
			    	node.setChildren(new ArrayList<TreeNode>());
			    	
			    	trees.add(node);
		    	}else{
		    		TreeNode node = new TreeNode();
			    	node.setId(id);
			    	node.setTitle(title);
			    	node.setParentId(pid);
			    	node.setType(type);
			    	node.setChildren(new ArrayList<TreeNode>());
			    	
		    		List<TreeNode> ls = (List<TreeNode>)childMap.get(pid);
		    		if(ls == null)ls = new ArrayList<TreeNode>();
		    		ls.add(node);
		    		childMap.put(pid,ls);
		    	}
		    }
		    setChildTreeMap(trees,childMap);
		}catch(Exception e){
			e.printStackTrace();
		}
		TreeNode parentNode = new TreeNode();
    	parentNode.setId("myproNode");
    	parentNode.setTitle("专业题库");
    	parentNode.setParentId(null);
    	parentNode.setType("probank");
    	parentNode.setChildren(trees);
    	for(int i=0;i<trees.size();i++){
    		trees.get(i).setParentId(parentNode.getId());
    	}
    	TreeNode.putTypeIncon("probank", "resources/icons/fam/theme.gif", "");
    	TreeNode.putTypeIncon("bank", "resources/icons/fam/organ.gif", "");
		return parentNode;
	}
	
	/**
	 * 获取题库树
	 * @description
	 * @param topId
	 * @param paramMap
	 * @return
	 * @modified
	 */
	public List<TreeNode> getThemeBankTree(String topId,Map paramMap){
		List<TreeNode> trees = new ArrayList<TreeNode>();
		try{
			TreeNode.putTypeIncon("bank", "resources/icons/fam/organ.gif", "");
			Session session = template.getSessionFactory().getCurrentSession();
			String  sql = " select d.theme_bank_id as id, "+
				               "d.theme_bank_name as title, "+
				               "d.THE_THEME_BANK_ID as parentId, "+
				               "'bank' as type, "+
				               "d.SORT_NUM "+
				          " from (select * from THEME_BANK dd "+
				         " where dd.is_L = '10' ";
			String notHaveIds = (String)paramMap.get("notHaveIds");
			if(notHaveIds!=null && !"".equals(notHaveIds)  && !"null".equals(notHaveIds) && !"undefined".equals(notHaveIds)){
				String[] ids = notHaveIds.split(",");
				String tmpIds = "";
				for(int i=0;i<ids.length;i++){
					tmpIds+="'"+ids[i]+"',";
				}
				if(tmpIds.length()>0){
					sql+=" and dd.theme_bank_id not in("+tmpIds.substring(0,tmpIds.length()-1)+") ";
				}
			}
			String op = (String)paramMap.get("op");
			if(op!=null && !"".equals(op)  && !"null".equals(op) && !"undefined".equals(op)){
				String organId = (String)paramMap.get("organId");
				sql+=" and (dd.bank_public = '10' or (dd.bank_public = '20' and dd.organ_id ='"+organId+"')) ";
			}
			String bankType = (String)paramMap.get("bankType");
			if(bankType!=null && !"".equals(bankType)  && !"null".equals(bankType) && !"undefined".equals(bankType)){
				sql+=" and dd.bank_Type = '"+bankType+"'  ";
			}
			if(topId!=null && !"".equals(topId)&& !"null".equals(topId)){
				sql+=") d start with d.THE_THEME_BANK_ID = '"+topId+"' ";
			}else{
				sql+=") d start with d.THE_THEME_BANK_ID is null ";
			}
			sql+=" connect by prior d.theme_bank_id = d.THE_THEME_BANK_ID "
				 + " order SIBLINGS BY d.theme_bank_code,d.SORT_NUM ";
			SQLQuery sqlQuery=session.createSQLQuery(sql);   
		    List<Object[]> tmplist = sqlQuery.addScalar("id", StringType.INSTANCE)
				.addScalar("title", StringType.INSTANCE)
				.addScalar("parentId", StringType.INSTANCE)
				.addScalar("type", StringType.INSTANCE)
				.list();
		    Map<String,List<TreeNode>> childMap = new HashMap<String,List<TreeNode>>();
		    for(int i=0;i<tmplist.size();i++){
		    	Object[] object = (Object[])tmplist.get(i);
		    	String id = (String)object[0];
		    	String title = (String)object[1];
		    	String pid = (String)object[2];
		    	String type = (String)object[3];

		    	if((topId!=null && topId.equals(id)) || (pid==null || "".equals(pid))){
		    		TreeNode node = new TreeNode();
			    	node.setId(id);
			    	node.setTitle(title);
			    	node.setParentId(null);
			    	node.setType(type);
			    	node.setChildren(new ArrayList<TreeNode>());
			    	
			    	trees.add(node);
		    	}else{
		    		TreeNode node = new TreeNode();
			    	node.setId(id);
			    	node.setTitle(title);
			    	node.setParentId(pid);
			    	node.setType(type);
			    	node.setChildren(new ArrayList<TreeNode>());
			    	
		    		List<TreeNode> ls = (List<TreeNode>)childMap.get(pid);
		    		if(ls == null)ls = new ArrayList<TreeNode>();
		    		ls.add(node);
		    		childMap.put(pid,ls);
		    	}
		    }
		    setChildTreeMap(trees,childMap);
		}catch(Exception e){
			e.printStackTrace();
		}
		return trees;
	}
	
	private void setChildTreeMap(List<TreeNode> list,Map<String,List<TreeNode>> childMap){
		if(list!=null){
			for(int i=0;i<list.size();i++){
				TreeNode node = (TreeNode)list.get(i);
				List<TreeNode> childList = (List<TreeNode>)childMap.get(node.getId());
				if(childList!=null && childList.size()>0){
					node.setChildren(childList);
					setChildTreeMap(childList,childMap);
				}else{
					node.setLeaf(true);
				}
			}
		}
	}
}
