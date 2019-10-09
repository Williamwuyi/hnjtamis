package cn.com.ite.eap2.core.hibernate;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.Property;
import org.hibernate.mapping.ToOne;
import org.hibernate.mapping.Value;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.orm.hibernate4.HibernateTemplate;

import cn.com.ite.eap2.common.utils.BeanUtils;
import cn.com.ite.eap2.common.utils.DataStreamUtils;
import cn.com.ite.eap2.common.utils.StringUtils;
import cn.com.ite.eap2.core.hibernate.ref.HibernateConfigurationHelper;

/**
 * 
 * <p>Title cn.com.ite.eap2.core.hibernate.HibernateDefaultDAOImpl</p>
 * <p>Description 缺省DAO实现</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2014</p>
 * @author 宋文科
 * @create time: May 19, 2014 11:51:27 AM
 * @version 2.0
 * 
 * @modified records:
 */
public class HibernateDefaultDAOImpl implements DefaultDAO{
	/**
	 * 日志
	 */
	@SuppressWarnings("unused")
	private Log log = LogFactory.getLog(HibernateDefaultDAOImpl.class);
	
	/**
	 * 具体数据库实现接口
	 */
	private PagingHandler pagingHandler;
	
	/**
	 * 实体
	 */
	@SuppressWarnings("unchecked")
	private Class entityClass;
	
	/**
	 * SQL语句映射
	 */
	@SuppressWarnings("unchecked")
	private Map sqlMap = new HashMap();
	
	/**
	 * HQL语句映射
	 */
	@SuppressWarnings("unchecked")
	private Map hqlMap = new HashMap();
	
	/**
	 * 设置实体类型
	 * @param className
	 * @modified
	 */
	@SuppressWarnings("unchecked")
	public void setEntityClass(String className){
		try {
			entityClass = Class.forName(className);
			String default_hql="from "+entityClass;
			hqlMap.put("default_hql", default_hql);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 查询所有数据
	 * @param entityClass
	 * @return
	 * @modified
	 */
	@SuppressWarnings("unchecked")
	public List findAll(Class entityClass){
		return template.loadAll(entityClass);
	}

	/**
	 * HIBERNATE模板
	 */
	protected HibernateTemplate template;
	/**
	 * 设置模板
	 * @param template 模板
	 * @modified
	 */
	public void setTemplate(HibernateTemplate template) {
		this.template = template;
	}

	/**
	 * 增加实体
	 * @param entity 实体
	 */
	public void addEntity(Serializable bo) {
		template.save(bo);
	}

	/**
	 * 清除缓存
	 * @param entity 实体
	 */
	public void clearCache(Serializable entity) {
		template.getSessionFactory().getCurrentSession().evict(entity);
	}
	
	/**
	 * 复制
	 * @param entity 实体对象
	 * @return  复制对象
	 */
	public Serializable copy(Serializable entity) {
		entity = this.entityToPo(entity);
		byte[] bytes = DataStreamUtils.objectToBytes(entity);
		return (Serializable)DataStreamUtils.bytesToObject(bytes);
	}

	/**
	 * 批量删除实体
	 * @param entitys 实体
	 */
	@SuppressWarnings("unchecked")
	public void deleteBatchEntity(Collection entitys) {
		template.deleteAll(entitys);
	}

	/**
	 * 批量删除实体，根据主健组
	 * @param keys 主健数组
	 */
	public void deleteBatchEntityByKeys(Serializable[] keys) {
		this.deleteBatchEntityByKeys(keys, this.getEntityClass());
	}

	/**
	 * 批量删除实体，根据主健组和类型
	 * @param keys 主健数组
	 * @param entityClass 实体类型
	 */
	@SuppressWarnings("unchecked")
	public void deleteBatchEntityByKeys(Serializable[] keys, Class entityClass) {
		List entitys=new ArrayList();
		for(int i=0;i<keys.length;i++){
			Object entity=template.get(entityClass, keys[i]);
			template.delete(entity);
		}
	}

	/**
	 * 删除实体
	 * @param entity 实体
	 */
	public void deleteEntity(Serializable entity) {
		template.delete(entity);
	}

	/**
	 * 根据主健删除实体
	 * @param key 主健
	 */
	@SuppressWarnings("unchecked")
	public void deleteEntityByKey(Serializable key) {
		Object entity = template.get(this.getEntityClass(), key);
		template.delete(entity);
	}

	/**
	 * 实体转成PO对象
	 * @param entity 实体
	 * @return PO对象
	 * @modified
	 */
	public Serializable entityToPo(Serializable entity) {
		if(entity instanceof HibernateProxy){
			entity = (Serializable) ((HibernateProxy) entity).getHibernateLazyInitializer()
            .getImplementation();
		}
		return entity;
	}

	/**
	 * 主健查询
	 * @param key 主健
	 * @return  实体对象
	 */
	public Serializable findEntityBykey(Serializable key) {
		return this.findEntityBykey(this.getEntityClass(), key);
	}

	/**
	 * 根据类型及主健查询实体
	 * @param entityClass 类型
	 * @param key 主健
	 * @return 实体对象
	 * @modified
	 */
	@SuppressWarnings("unchecked")
	public Serializable findEntityBykey(Class entityClass, Serializable key) {
		return (Serializable)template.get(entityClass, key);
	}

	/**
	 * 获得所有数据
	 * @param entityClass 实体类型
	 * @return 实体数据
	 */
	@SuppressWarnings("unchecked")
	public List getAll(Class entityClass) {
		template.setCacheQueries(true);
		if(entityClass!=null)
		   return template.loadAll(entityClass);
		else
		   return template.loadAll(getEntityClass());
	}

	/**
	 * 获得实体类型
	 * @param entity 实体
	 * @return 实体类型
	 * @modified 
	 */
	@SuppressWarnings("unchecked")
	public Class getEntityClass(Serializable entity) {
		entity = this.entityToPo(entity);
		return entity.getClass();
	}

	/**
	 * 获得当前实体类型
	 * @return 当前实体类型
	 */
	@SuppressWarnings("unchecked")
	public Class getEntityClass() {
		return this.entityClass;
	}

	/**
	 * 获得实体主健
	 * @param entity 实体
	 * @return:主健
	 */
	public Serializable getIdentifier(Object entity) {
		return template.getSessionFactory().getCurrentSession().getIdentifier(entity);
	}

	/**
	 * QL语句的动态处理
	 * @param ql ���
	 * @param term ����
	 * @return
	 * @modified
	 */
	private  static VelocityEngine ve = null;
	private VelocityEngine getVelocityEngine(){
		try{
			if(ve==null){
				ve = new VelocityEngine(); 
				 //ve.setProperty(Velocity.FILE_RESOURCE_LOADER_PATH, path); 
				 //设置编码
				 ve.setProperty(Velocity.INPUT_ENCODING,"UTF-8");
				 ve.setProperty(Velocity.OUTPUT_ENCODING,"UTF-8");
				 
				//初始
		        ve.init();
		}
		}catch(Exception e){
			ve = null;
			e.printStackTrace();
		}
		return ve;
	}
	private String qlHandler(String ql,Object term){
		 try{ 
			 VelocityEngine ve = getVelocityEngine();
			 if(ve!=null){
	            //VELOCITY环境
	            VelocityContext context = new VelocityContext(); 
	            //设置参数 
	            context.put("term", term);
	            java.io.StringWriter writer=new java.io.StringWriter();
				ve.evaluate(context, writer, null, ql);
				return writer.toString(); 
			 }
         }catch (Exception e){ 
            e.printStackTrace(); 
         }
         return ql;
	}
	
	/**
	 * SQL参数处理
	 * @param sql 语句
	 * @param term 条件
	 * @param termHander 条件
	 * @return
	 * @modified
	 */
	@SuppressWarnings("unchecked")
	private String sqlParameterHandler(String sql,Object term,List termHander){
		String regEx = ":[A-Za-z][A-Za-z0-9_]{1,}";
		Pattern pat = Pattern.compile(regEx);  
		Matcher mat = pat.matcher(sql);  
		if(termHander==null)
			termHander = new ArrayList();
		termHander.clear();
		boolean result = mat.find();
		while(result) { 
			String param = mat.group().substring(1);
			Object paramValue;
			try {
				paramValue = PropertyUtils.getProperty(term, param);
			} catch (Exception e) {
				throw new RuntimeException(param+"参数设置不对应！");
			}
			sql = sql.replaceAll(":"+param, "?");
			termHander.add(paramValue);
			result = mat.find(); 
	    }
		return sql;
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
	
	/**
	 * QL语句排序处理
	 * @param ql 语句
	 * @param sortMap 排序
	 * @return  处理排序后的QL语句
	 */
	@SuppressWarnings("unchecked")
	public String qlSortHander(String ql, Map sortMap) {
		if(sortMap!=null){
			Iterator iterator = sortMap.keySet().iterator();
			boolean start = true;
			if(ql.indexOf("order by")!=-1){
				start = false;
			}
			if(sortMap.size()>0&&ql.indexOf(" order by ")<0)
				ql += " order by ";
			
			while(iterator.hasNext()){
				String columnName = (String)iterator.next();
				Boolean sorted = (Boolean)sortMap.get(columnName);
				if(!start)
					ql += ",";
				start = false;
				ql+=" nlssort("+columnName+")"+(sorted?" asc":" desc");
			}
		}
		return ql;
	}
	/**
	 * 执行SQL的插入与修改操作
	 * @param sql 语句
	 * @param params 批量参数
	 */
	protected void excuteSql(String sql,List params){
		Session session=template.getSessionFactory().getCurrentSession();
        try {
        	List termHander = null;
        	String dempSql = sql;
        	if(params!=null&&params.size()>0)
        	   sql = sqlParameterHandler(sql,params.get(0),termHander);
        	SQLQuery sq = session.createSQLQuery(sql);
        	if(params!=null&&params.size()>0){
	        	for(int i=0;i<params.size();i++){
	        		Object param = params.get(i);
	        		termHander.clear();
		            sqlParameterHandler(dempSql,param,termHander);
		            int parameterIndex=1;
		            for(Object para:termHander){
		            	  sq.setParameter(parameterIndex, para);
		                  parameterIndex++;
		            }
		            sq.executeUpdate();
	        	}
        	}else
        		 sq.executeUpdate();
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
	}
	
	/**
	 * 执行HQL的插入与修改操作
	 * @param sql 语句
	 * @param params 批量参数
	 */
	protected void excuteHql(String hql,List params){
		if(hql==null||"".equals(hql.trim())) return;
		if(params!=null&&params.size()>0){
			Iterator iterator = params.iterator();
			Query query = null;
			while (iterator.hasNext()) {
				Object value =  iterator.next();
				query = (Query) template.getSessionFactory().getCurrentSession().createQuery(hql);
				Map termHander = new HashMap();
				hqlParameterHandler(hql,value,termHander);
				Iterator iteratorTerm = termHander.keySet().iterator();
				while (iteratorTerm.hasNext()) {
					String key = (String) iteratorTerm.next();
					Object para = termHander.get(key);
					if(para instanceof Collection)
						query.setParameterList(key, (Collection)para);
					else
					    query.setParameter(key, para);
				}
				query.executeUpdate();
			}
		}else{
			Query query = (Query) template.getSessionFactory().getCurrentSession().createQuery(hql);
			query.executeUpdate();
		}
	}
	/**
	 * 执行配置的QL语句（批量处理）
	 * @param configQlName 配置名称
	 * @param params 参数集合
	 * @modified
	 */
	public void excuteQl(String configQlName,List params){
		String sql = (String)sqlMap.get(configQlName);
		if(sql!=null&&!"".equals(sql.trim())){
			this.excuteSql(sql, params);
		}else{
			String hql = (String)hqlMap.get(configQlName);
			this.excuteHql(hql, params);
		}
	}
	/**
	 * SQL语句查询
	 * @param sql
	 * @param term
	 * @param sortMap
	 * @param entityClass
	 * @param startNo
	 * @param rowSize
	 * @return
	 * @modified
	 */
	@SuppressWarnings("unchecked")
	protected List querySql(String sql,Object term,Map<String,Boolean> sortMap,Class entityClass,int startNo,int rowSize){
		Session session=template.getSessionFactory().getCurrentSession();
        List returns=new ArrayList();
        try {
        	sql = this.qlHandler(sql, term);//动态处理
        	List termHander = new ArrayList();
        	sql = this.sqlParameterHandler(sql, term, termHander);
        	if(pagingHandler!=null){//参数处理
        	  sql = pagingHandler.sqlPagingHandler(sql, startNo, rowSize).trim();
        	  sql = pagingHandler.sqlSortHandler(sql, sortMap);
        	}else
	          throw new RuntimeException("未定义具体的数据库参数处理！");
            if(entityClass==null||entityClass.newInstance() instanceof Map
               ||!HibernateConfigurationHelper.getInstance().isExites(entityClass)){
            	ConnectionProvider cp =((SessionFactoryImplementor)session.getSessionFactory()).getConnectionProvider();   
            	Connection con = cp.getConnection();  
            	CallableStatement psm=null;
                ResultSet re=null;
                psm = con.prepareCall(sql);
                for(int i=0;i<termHander.size();i++){
                	Object t = termHander.get(i);                	
                	if(t!=null){
                		if(t instanceof Date){
                			psm.setDate(i+1, new java.sql.Date(((Date)t).getTime()));
                		}else
                		psm.setObject(i+1, termHander.get(i));
                	}else
                		psm.setString(i+1,null);
                }
                re=psm.executeQuery();
                ResultSetMetaData metData=re.getMetaData();
                int columnCount=metData.getColumnCount();
                while(re.next()){
                  Map v=new HashMap();
                  Object entity=null;
                  if(entityClass!=null){
                	  entity = entityClass.newInstance();
            	  }
                  for(int i=0;i<columnCount;i++){
                	  if(entity!=null){
                		  String field = metData.getColumnName(i+1);
                		  boolean find = false;
                		  Field[] fields = entityClass.getDeclaredFields();
                		  for (int j = 0; j < fields.length; j++) {
              		        String fieldname = fields[j].getName();
              		        if(field.equals(fieldname.toUpperCase())){
              		        	field = fieldname;
              		        	find = true;
              		        	break;
              		        }
              		      }
                		  if(!find) continue;
                		  Object object = re.getObject(i+1);
                		  if(object!=null){
	                		  if(object instanceof BigDecimal){
	                			  BigDecimal bd = (BigDecimal)object;
	                			  Class fieldClass = PropertyUtils.getPropertyType(entity, field);
	                			  if(fieldClass.getName().equals("java.lang.Double"))
	                				  PropertyUtils.setProperty(entity, field, bd.doubleValue());
	                			  else if(fieldClass.getName().equals("java.lang.Float"))
	                				  PropertyUtils.setProperty(entity, field, bd.floatValue());
	                			  else if(fieldClass.getName().equals("java.lang.Long"))
	                				  PropertyUtils.setProperty(entity, field, bd.longValue());
	                			  else
	                				  PropertyUtils.setProperty(entity, field, bd.intValue());
	                		  }else
	                		  if(object instanceof Timestamp){
	                			  Timestamp ts = (Timestamp)object;
	                			  Class fieldClass = PropertyUtils.getPropertyType(entity, field);
	                			  if(fieldClass.getName().endsWith(".Date"))
	                				  PropertyUtils.setProperty(entity, field, new java.util.Date(ts.getTime()));
	                			  else
	                				  PropertyUtils.setProperty(entity, field, ts);
	                		  }else
	                		  if(object instanceof java.sql.Date){
	                			  Timestamp ts = re.getTimestamp(i+1);
	                			  Class fieldClass = PropertyUtils.getPropertyType(entity, field);
	                			  if(fieldClass.getName().endsWith(".Date"))
	                				  PropertyUtils.setProperty(entity, field, new java.util.Date(ts.getTime()));
	                			  else
	                				  PropertyUtils.setProperty(entity, field, ts);
	                		  }else
	                			  PropertyUtils.setProperty(entity, field, object);
                		  }
                	  }else
                        v.put(metData.getColumnName(i+1),re.getObject(i+1));
                  }
                  if(entity!=null)
                	  returns.add(entity);
                  else
                      returns.add(v);
                }
            }else{
            	SQLQuery sqlQery = session.createSQLQuery(sql);
                for(int i=0;i<termHander.size();i++){
                	Object t = termHander.get(i);
                	if(t!=null)
                	  sqlQery.setParameter(i, termHander.get(i));
                	else
                	  sqlQery.setString(i,null);
                }
            	sqlQery.addEntity(entityClass);
            	returns = sqlQery.list();
            }
        } catch (Exception ex) {
        	ex.printStackTrace();
            throw new RuntimeException(ex.getMessage());
        }
        return returns;
	}
	
	protected List queryHql(String hql,Object term,Map<String,Boolean> sortMap,Class entityClass,int startNo,int rowSize){
		if(hql==null||"".equals(hql.trim()))
			hql = "from "+entityClass.getName();
		hql = this.qlHandler(hql, term).trim();//动态处理
		hql = this.qlSortHander(hql, sortMap);//排序处理
		Map termHander = new HashMap();
		this.hqlParameterHandler(hql, term, termHander);//参数处理
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
		if(rowSize!=0){
		  query.setFirstResult(startNo);
		  query.setMaxResults(rowSize);
		}
		List returns = query.list();
		if(returns.size()==0)
			return new ArrayList();
		else if((returns.get(0) instanceof Object[])&&entityClass!=null){
			String[] aliases = query.getReturnAliases();
			for(int x=0;x<returns.size();x++){
				Object[] item = (Object[])returns.get(x);
				try {
					Object newOb = entityClass.newInstance();
					for(int i=0;i<aliases.length;i++){
						String alias = aliases[i];
						PropertyUtils.setProperty(newOb, alias, item[i]);
					}
					returns.set(x, newOb);
				} catch (Exception e) {
					e.printStackTrace();
				} 
			}
		}
		return returns;
	}
	/**
	 * SQL语句查询
	 * @param sql
	 * @param term
	 * @param sortMap
	 * @param entityClass
	 * @return
	 * @modified
	 */
	protected List querySql(String sql,Object term,Map<String,Boolean> sortMap,Class entityClass) {
		return this.querySql(sql, term, sortMap, entityClass, 0, 0);
	}
	
	/**
	 * 
	 * 执行SQL
	 * @param sql
	 * @return
	 * @modified
	 */
	protected int saveSQL(String sql) {
		Session session=template.getSessionFactory().getCurrentSession();
		int len = session.createSQLQuery(sql).executeUpdate();
        return len;
	}
	
	
	/**
	 *  
	 * 查询HQL
	 * @param hql
	 * @return
	 * @modified
	 */
	protected List queryHql(String hql) {
		Query query = (Query) template.getSessionFactory().getCurrentSession().createQuery(hql);
		return query.list();
	}
	/**
	 * HQL语句查询
	 * @param hql
	 * @param term
	 * @param sortMap
	 * @param entityClass
	 * @return
	 * @modified
	 */
	protected List queryHql(String hql,Object term,Map<String,Boolean> sortMap,Class entityClass) {
		return this.queryHql(hql, term, sortMap, entityClass, 0, 0);
	}
	
	/**
	 * 查询配置的QL语句
	 * @param configQlName 配置名称
	 * @param term 条件
	 * @param sortMap 排序
	 * @param entityClass 实体类型,为空时缺省为MAP类型
	 * @param startNo 开始行号
	 * @param rowSize 页记录数
	 * @return  查询结果
	 * @modified
	 */
	@SuppressWarnings("unchecked")
	public List queryConfigQl(String configQlName,Object term,Map<String,Boolean> sortMap,Class entityClass,int startNo,int rowSize) {
		String sql = (String)sqlMap.get(configQlName);
		if(sql!=null&&!"".equals(sql.trim())){
			return this.querySql(sql, term, sortMap, entityClass, startNo, rowSize);
		}else{
			String hql = (String)hqlMap.get(configQlName);
			return this.queryHql(hql, term, sortMap, entityClass, startNo, rowSize);
		}
	}
	
	/**
	 * 查询配置的QL语句
	 * @param configQlName 配置名称
	 * @param term 条件
	 * @param sortMap 排序
	 * @param entityClass 实体类型,为空时缺省为MAP类型
	 * @return 查询结果
	 * @modified
	 */
	@SuppressWarnings("unchecked")
	public List queryConfigQl(String configQlName,Object term,Map<String,Boolean> sortMap,Class entityClass) {
		return this.queryConfigQl(configQlName, term,sortMap,entityClass, 0, 0);
	}
	
	/**
	 * 查询配置的QL语句的数量
	 * @param configQlName 配置名称
	 * @param term 条件
	 * @return 结果数量
	 * @modified
	 */
	@SuppressWarnings("unchecked")
	public int countConfigQl(String configQlName,Object term) {
		String sql = (String)sqlMap.get(configQlName);
		if(sql!=null&&!"".equals(sql.trim())){
			Session session=template.getSessionFactory().getCurrentSession();
			SQLQuery sqlQery=null;
	        @SuppressWarnings("unused")
			List returns=new ArrayList();
	        try {
	        	sql = this.qlHandler(sql, term);
	        	List termHander = new ArrayList();
	        	sql = this.sqlParameterHandler(sql, term, termHander);
	        	if(pagingHandler!=null)//分页处理
	        	  sql = pagingHandler.sqlCountHandler(sql).trim();
	        	else
	        	  throw new RuntimeException("未定义具体的数据库求量处理！");
	        	sqlQery = session.createSQLQuery(sql);
                for(int i=0;i<termHander.size();i++){
                	Object t = termHander.get(i);
                	if(t!=null)
                	   sqlQery.setParameter(i, termHander.get(i));
                	else
                	   sqlQery.setString(i, null);
                }
                return ((java.math.BigDecimal)sqlQery.list().get(0)).intValue();
	        } catch (Exception ex) {
	            throw new RuntimeException(ex.getMessage());
	        }
		}else{
			String hql = (String)hqlMap.get(configQlName);
			if(hql==null||"".equals(hql.trim()))
				return 0;
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
			return ((Long)query.uniqueResult()).intValue();
		}
	}
	/**
	 * 查询字段最大值
	 * @param entityClass 实体类型
	 * @param fieldName 字段名
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public int getFieldMax(Class entityClass,String fieldName){
		return this.getFieldMax(entityClass, fieldName, null, null);
	}
	/**
	 * 查询字段最大值
	 * @param entityClass 实体类型
	 * @param fieldName 字段名
	 * @param parentFieldName 父字段名
	 * @param parentFieldValue 父字段值
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public int getFieldMax(Class entityClass,String fieldName,String parentFieldName,Object parentFieldValue){
		String hql = "select max(t."+fieldName+") from "+entityClass.getName()+ " t"+
		      (parentFieldName!=null?(" where t."+parentFieldName+"=:parentFieldName or :parentFieldName is null"):"");
		Query query = (Query) template.getSessionFactory().getCurrentSession().createQuery(hql);
		if(parentFieldName!=null)
		query.setParameter("parentFieldName", parentFieldValue);
		Object count = query.uniqueResult();
		if(count==null)
			return 0;
		if(count.getClass().getName().equals("int")||
			count.getClass().isAssignableFrom(Integer.class))
			return ((Integer)count).intValue();
		else
		    return ((Long)count).intValue();
	}
	/**
	 * 查询指定主健的实体的子对象
	 * @param clazz 实体类型
	 * @param keyColumnName 主健字段名
	 * @param keyValue 主健值
	 * @param childColumnName 子对象字段名
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Collection findEntitySub(String clazz,String keyColumnName,String keyValue,String childColumnName){
		String hql = "select t."+childColumnName+" from "+clazz+ " t where t."+keyColumnName+"=:keyColumnName";
		Query query = (Query) template.getSessionFactory().getCurrentSession().createQuery(hql);
		query.setParameter("keyColumnName", keyValue);
		return query.list();
	}
	/**
	 * 根据指定属性查询数据
	 * @param entityClass 类型
	 * @param field 属性名
	 * @param fieldValue 属性值
	 * @return
	 * @modified
	 */
	@SuppressWarnings("unchecked")
	public List findEntityByField(Class entityClass,String field,String fieldValue){
		String hql = "from "+entityClass.getName()+ " t where t."+field+"=:keyColumnName";
		Query query = (Query) template.getSessionFactory().getCurrentSession().createQuery(hql);
		query.setParameter("keyColumnName", fieldValue);
		return query.list();
	}
	/**
	 * 删除无关联的附件
	 * @param accessIds 附件ID数组
	 * @param busId 业务ID
	 */
	@Override
	public void deleteNoLinkAccessory(String[] accessIds, String busId) throws Exception{
		String sql = "delete accessory a where a.item_id='"+busId+"'";
		if(accessIds!=null&&accessIds.length>0){
			sql += " and a.acce_id not in (";
			boolean start = true;
			for(String id:accessIds){
				if(!start) sql += ",";
				sql += "'"+id+"'";
				start = false;
			}
			sql += ")";
		}
		Session session=template.getSessionFactory().getCurrentSession();
		session.createSQLQuery(sql).executeUpdate();
	}

	/**
	 * 刷新对象
	 * @param entity 实体
	 */
	public void refreshObject(Serializable entity) {
		template.refresh(entity);
	}

	/**
	 * 批量保存实体
	 * @param entitys 实体集合
	 */
	@SuppressWarnings("unchecked")
	public void saveBatchEntity(Collection entitys) {
		Iterator iterator = entitys.iterator();
		while(iterator.hasNext()){
			Serializable po = (Serializable)iterator.next();
			template.saveOrUpdate(po);
		}
	}
	
	/**
	 * 保存实体
	 * @param entity 实体
	 */
	public void saveEntityOld(Serializable entity) {
		template.save(entity);
	}

	/**
	 * 保存实体
	 * @param entity 实体
	 */
	public void saveEntity(Serializable entity) {
		this.clearCache(entity);
		String pkProtery = HibernateConfigurationHelper.getInstance().getPkProperyName(entity.getClass());
		try {
			Object key = PropertyUtils.getProperty(entity, pkProtery);
			this.entityReset(entity);
			if((key instanceof String&&StringUtils.isEmpty((String)key))||key == null)
				template.save(entity);
			else{
				Serializable src = findEntityBykey(entity.getClass(), (Serializable)key);
				BeanUtils.copyProperties(src, entity);
				template.merge(src);
			}
		} catch (Exception e) {
			e.printStackTrace();
			//template.save(entity);
			throw new RuntimeException(e.getMessage());
		}
	}
	
	@SuppressWarnings("unchecked")
	private void entityReset(Serializable entity) throws Exception{
		HibernateConfigurationHelper helper = HibernateConfigurationHelper.getInstance();
		PersistentClass persistentClass = helper.getPersistentClass(entity.getClass());
		Iterator<Property> iterator = persistentClass.getPropertyIterator();
		while(iterator.hasNext()){
			Property property = (Property)iterator.next();
			Value v = property.getValue();
			if(v instanceof ToOne){//多对一
				Object bean = PropertyUtils.getProperty(entity, property.getName());
				if(bean==null)
					continue;
				String refEntityName = ((ToOne) v).getReferencedEntityName();
				String refKeyName = helper.getPkProperyName(Class.forName(refEntityName));
				Serializable refKey = (Serializable)PropertyUtils.getProperty(bean, refKeyName);
				if(refKey!=null){
				    Object persistentBean = this.findEntityBykey(Class.forName(refEntityName), refKey);
				    PropertyUtils.setProperty(entity, property.getName(), persistentBean);
				}else
				    PropertyUtils.setProperty(entity, property.getName(), null);
			}else if( v instanceof org.hibernate.mapping.Collection){//集合对象
				String cascade = property.getCascade();
				Collection collection = (Collection)PropertyUtils.getProperty(entity, property.getName());
				if(cascade.startsWith("all")||cascade.startsWith("save")){
					if(collection==null)
						continue;
					Iterator collIter = collection.iterator();
					while(collIter.hasNext()){
						Serializable bean = (Serializable)collIter.next();
						this.entityReset(bean);
					}
				}
			}
		}
	}

	/**
	 * 更新实体对象
	 * @param entity 实体
	 */
	public void updateEntity(Serializable entity) {
		template.update(entity);
	}

	@SuppressWarnings("unchecked")
	public Map getSqlMap() {
		return sqlMap;
	}

	@SuppressWarnings("unchecked")
	public void setSqlMap(Map sqlMap) {
		this.sqlMap = sqlMap;
	}

	@SuppressWarnings("unchecked")
	public Map getHqlMap() {
		return hqlMap;
	}

	@SuppressWarnings("unchecked")
	public void setHqlMap(Map hqlMap) {
		this.hqlMap = hqlMap;
	}

	public void setPagingHandler(PagingHandler pagingHandler) {
		this.pagingHandler = pagingHandler;
	}

	public PagingHandler getPagingHandler() {
		return pagingHandler;
	}
}