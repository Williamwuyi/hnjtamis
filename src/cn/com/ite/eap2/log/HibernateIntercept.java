package cn.com.ite.eap2.log;

import java.io.Serializable;
import java.util.*;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.CallbackException;
import org.hibernate.EmptyInterceptor;
import org.hibernate.FlushMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.Property;
import org.hibernate.mapping.Value;
import org.hibernate.type.Type;
import org.springframework.orm.hibernate4.HibernateTemplate;

import cn.com.ite.eap2.common.utils.FileUtils;
import cn.com.ite.eap2.core.hibernate.ref.HibernateConfigurationHelper;
import cn.com.ite.eap2.core.spring.SpringContextUtil;
import cn.com.ite.eap2.domain.baseinfo.Accessory;


/**
 * <p>Title cn.com.ite.eap2.log.HibernateIntercept</p>
 * <p>Description hibernate拦截器</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2014</p>
 * @author 宋文科
 * @create time: 2014-6-13 下午12:29:41
 * @version 2.0
 * 
 * @modified records:
 */
public class HibernateIntercept extends EmptyInterceptor {
	private static final long serialVersionUID = 3437276433297903480L;
	/**
	 * 不保存日志的实体路径
	 */
	private List<String> noLogEntityUrls = new ArrayList<String>();
	private Map<String,Long> buessinesToTime = new HashMap<String,Long>();

	public void setNoLogEntityUrls(List<String> noLogEntityUrls) {
		this.noLogEntityUrls = noLogEntityUrls;
	}
	/**
	 * 数据增加
	 */
	@SuppressWarnings("unchecked")
	public boolean onSave(Object entity, Serializable id, Object[] state,
			String[] propertyNames, Type[] types) throws CallbackException{
		//唯一验证处理
		HibernateConfigurationHelper helper = HibernateConfigurationHelper.getInstance();
		Map config = helper.getEntityProteryUnique(entity.getClass());
		Map entityConfig = helper.getEntityConfig(entity.getClass());
		HibernateTemplate template = (HibernateTemplate)SpringContextUtil.getBean("template");
		if(entityConfig!=null&&entityConfig.size()>0){
			Map uniqueComment = (Map)entityConfig.get("unique");
			if(config!=null&&config.size()>0&&uniqueComment!=null&&uniqueComment.size()>0){
				Iterator iterator = config.keySet().iterator();
			    while(iterator.hasNext()){
			    	String key = (String)iterator.next();
			    	List list= (List)config.get(key);
			    	String comment = (String)uniqueComment.get(key);
			    	if(StringUtils.isEmpty(comment)) continue;
			    	String hql = "select count(t) from "+entity.getClass().getName()+" t where";
			    	boolean start = true;
			    	for(String field:(List<String>)list){
			    		String[] fields = field.split("\\$");
			    		if(!start){
			    			hql += " and ";
			    		}
			    		try{
				    		if(PropertyUtils.getProperty(entity, fields[0])!=null){
					    		hql += " t."+fields[0]+"=:"+fields[0];
				    		}else{
				    			if(fields[1].equals("true"))
				    			   hql += " 1<>1";//t."+field+" is null";
				    			else
				    			   hql += " t."+fields[0]+" is null";
				    		}
			    		}catch(Exception e){
			    			throw new RuntimeException(entity.getClass()+"唯一配置有问题！");
			    		}
			    		start = false;
			    	}
			    	Session session = template.getSessionFactory().getCurrentSession();
			    	FlushMode oldFlushMode = session.getFlushMode();
			    	session.setFlushMode(FlushMode.COMMIT);
			    	Query query = session.createQuery(hql);
			    	for(String field:(List<String>)list){
			    		String[] fields = field.split("\\$");
			    		try {
			    			if(PropertyUtils.getProperty(entity, fields[0])!=null)
							  query.setParameter(fields[0], PropertyUtils.getProperty(entity, fields[0]));
						} catch (Exception e) {
							throw new RuntimeException(entity.getClass()+"唯一配置有问题！");
						}
			    	}
			    	long size = (Long)query.uniqueResult();
			    	session.setFlushMode(oldFlushMode);
			    	if(size>0)
			    		throw new RuntimeException(comment);
			    }
			}
		}
		//日志处理
		try {
			if(!noLogEntityUrls.contains(entity.getClass().getName()))
			EapLogImpl.makeLog(entity, getTransactionId(),1,null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return super.onSave(entity, id, state, propertyNames, types);
	}
	/**
	 * 数据修改
	 */
	@SuppressWarnings("unchecked")
	public boolean onFlushDirty(Object entity,Serializable id,Object[] currentState,
	        Object[] previousState,String[] propertyNames,Type[] types) throws CallbackException{
		if(previousState!=null){
			//唯一验证处理
			HibernateConfigurationHelper helper = HibernateConfigurationHelper.getInstance();
			String keyField = helper.getPkProperyName(entity.getClass());//主健字段
			Map config = helper.getEntityProteryUnique(entity.getClass());
			Map entityConfig = helper.getEntityConfig(entity.getClass());
			HibernateTemplate template = (HibernateTemplate)SpringContextUtil.getBean("template");
			if(entityConfig!=null&&entityConfig.size()>0){
				Map uniqueComment = (Map)entityConfig.get("unique");
				if(config!=null&&config.size()>0&&uniqueComment!=null&&uniqueComment.size()>0){
					Iterator iterator = config.keySet().iterator();
				    while(iterator.hasNext()){
				    	String key = (String)iterator.next();
				    	List list= (List)config.get(key);
				    	String comment = (String)uniqueComment.get(key);
				    	if(StringUtils.isEmpty(comment)) continue;
				    	String hql = "select count(t) from "+entity.getClass().getName()+" t where t."+keyField+"<>:"+keyField;
				    	for(String field:(List<String>)list){
				    		String[] fields = field.split("\\$");
				    		try{
					    		if(PropertyUtils.getProperty(entity, fields[0])!=null)
					    		    hql += " and t."+fields[0]+"=:"+fields[0];
					    		else{
					    			if(fields[1].equals("true"))
					    			   hql += " and 1<>1";
					    			else
					    			   hql += " and t."+fields[0]+" is null";
					    		}
				    		}catch(Exception e){
				    			e.printStackTrace();
				    			throw new RuntimeException(entity.getClass()+"唯一配置有问题！");
				    		}
				    	}
				    	Session session = template.getSessionFactory().getCurrentSession();
				    	FlushMode oldFlushMode = session.getFlushMode();
				    	session.setFlushMode(FlushMode.COMMIT);
				    	Query query = session.createQuery(hql);
				    	query.setParameter(keyField, id);
				    	for(String field:(List<String>)list){
				    		String[] fields = field.split("\\$");
				    		try {
				    			if(PropertyUtils.getProperty(entity, fields[0])!=null)
								  query.setParameter(fields[0], PropertyUtils.getProperty(entity, fields[0]));
							} catch (Exception e) {
								throw new RuntimeException(entity.getClass()+"唯一配置有问题！");
							}
				    	}
				    	long size = (Long)query.uniqueResult();
				    	session.setFlushMode(oldFlushMode);
				    	if(size>0){
				    		throw new RuntimeException(comment);
				    	}
				    }
				}
			}
			//日志处理
			try {
				if(!noLogEntityUrls.contains(entity.getClass().getName())){
					Map old = new HashMap();
					for(int i=0;i<propertyNames.length;i++){
						old.put(propertyNames[i], previousState!=null?previousState[i]:null);
					}
				    EapLogImpl.makeLog(entity, getTransactionId(),2,old);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return super.onFlushDirty(entity, id, currentState, previousState, propertyNames, types);
	}
	/**
	 * 数据删除
	 */
	@SuppressWarnings("unchecked")
	public void onDelete(Object entity, Serializable id, Object state[], 
			String propertyNames[], Type types[]) throws CallbackException{
		//引用判断开始---->
		HibernateConfigurationHelper helper = HibernateConfigurationHelper.getInstance();
		String keyField = helper.getPkProperyName(entity.getClass());
		PersistentClass persistentClass = helper.getPersistentClass(entity.getClass());
		Iterator<Property> iterator = persistentClass.getPropertyIterator();
		while(iterator.hasNext()){
			Property property = (Property)iterator.next();
			Value v = property.getValue();
			if( v instanceof org.hibernate.mapping.Collection){//判断集合
				if(((org.hibernate.mapping.Collection)v).getElement() 
						instanceof org.hibernate.mapping.ManyToOne){
					//多对多不判断
				}else{//一对多要判断
					String cascade = property.getCascade();
					if(cascade.startsWith("all")||cascade.startsWith("delete"))
						continue;//级联删除不判断
					String comment = helper.getEntityAttributeDesc(entity.getClass(), property.getName());
					Collection coll = null;
					try{
					    coll = (Collection)PropertyUtils.getProperty(entity, property.getName());
					}catch(Exception e){}
					if(coll!=null&&coll.size()>0)
						throw new RuntimeException(comment+"已经引用，不能删除！");
				}
			}
		}
		//处理隐式引用
		Map config = helper.getEntityConfig(entity.getClass());
		HibernateTemplate template = (HibernateTemplate)SpringContextUtil.getBean("template");
		if(config!=null&&config.size()>0){
			List foreigns = (List)config.get("foreign");
			Serializable key = null;
			try {
				key = (Serializable)PropertyUtils.getProperty(entity,keyField);
			} catch (Exception e) {} 
			if(key!=null&&foreigns!=null)
		    for(String entityField:(List<String>)foreigns){
		    	String[] ef = entityField.split("#");
		    	Class refClass = null;
		    	try {
					refClass = Class.forName(ef[0]);
				} catch (Exception e) {} 
				if(refClass==null) continue;
		    	String hql = "select count(t) from "+ef[0]+" t where t."+ef[1]+"."+keyField+"=:id";
		    	String comment = helper.getTableComment(refClass);
		    	Query query = template.getSessionFactory().getCurrentSession().createQuery(hql);
		    	query.setParameter("id", key);
		    	if(((Long)query.list().get(0))>0)
		    		throw new RuntimeException(comment+"已经使用，不能删除！");
		    }
		}
		//<-----引用判断开始
		try {
			if(!noLogEntityUrls.contains(entity.getClass().getName()))
			EapLogImpl.makeLog(entity, getTransactionId(),3,null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//附件处理
		if(entity instanceof Accessory){
			try {
				FileUtils.delete(((Accessory)entity).getFilePath());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		super.onDelete(entity, id, state, propertyNames, types);
    }
	/**
	 * 存放事务号
	 */
	@SuppressWarnings("unchecked")
	private static ThreadLocal machineLocal=new ThreadLocal();
	/**
	 * 获得事务ID
	 * @return
	 * @modified
	 */
	public static String getTransactionId(){
		return (String)machineLocal.get();
	}
	/**
	 * 当一个事务启动时，会立刻调用这个方法，
	 * 这个方法可以改变这个事务的状态，例如：回滚事务
	 * @param transaction
	 * @modified
	 */
	@SuppressWarnings("unchecked")
	public void afterTransactionBegin(Transaction transaction){
		String uuid = UUID.randomUUID().toString();
		machineLocal.set(uuid);
		buessinesToTime.put(uuid, System.currentTimeMillis());
    }
	/**
	 * 当完成一个事务之后，立刻调用此方法
	 * @param transaction 事务
	 * @modified
	 */
	@SuppressWarnings("unchecked")
	public void afterTransactionCompletion(Transaction transaction){
		Long millis = buessinesToTime.get((String)machineLocal.get());
		EapLogImpl.commin((String)machineLocal.get(),System.currentTimeMillis()-millis);
		machineLocal.set(null);
    }
}