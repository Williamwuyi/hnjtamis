package cn.com.ite.eap2.core.hibernate;

import java.util.*;
import org.hibernate.SessionFactory;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.*;
import org.hibernate.internal.SessionFactoryImpl;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;

import cn.com.ite.eap2.core.hibernate.ref.HibernateConfigurationHelper;

/**
 * <p>Title cn.com.ite.eap2.core.hibernate.EapLocalSessionFactoryBean</p>
 * <p>Description hibernate的session工厂的spring生成方法</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2009</p>
 * @author 宋文科
 * @create time: 2009-7-29 时间08:55:37
 * @version 1.0
 * 
 * @modified records:
 */
public class EapLocalSessionFactoryBean extends LocalSessionFactoryBean {
	/**
	 * 事件监听
	 */
	@SuppressWarnings("unchecked")
	private Map<String,List> listeners = new HashMap<String,List>();
	
	@SuppressWarnings("unchecked")
	public Map<String, List> getListeners() {
		return listeners;
	}

	@SuppressWarnings("unchecked")
	public void setListeners(Map<String, List> listeners) {
		this.listeners = listeners;
	}

	/**
	 * 获取对象
	 */
	@SuppressWarnings("unchecked")
	public SessionFactory getObject(){
		//设置hibernagte配置
		HibernateConfigurationHelper.setConfiguration(super.getConfiguration());
		SessionFactory sessionFactory = super.getObject();
		EventListenerRegistry registry = ((SessionFactoryImpl) sessionFactory).getServiceRegistry().getService(
	            EventListenerRegistry.class);
		//注册监听器事件
		Iterator iterator = listeners.keySet().iterator();
		while(iterator.hasNext()){
			String key = (String)iterator.next();
			List list = listeners.get(key);
			for(Object listener:list){
				if(key.equals("POST_INSERT"))
				    registry.getEventListenerGroup(EventType.POST_INSERT).appendListener((PostInsertEventListener)listener);
				if(key.equals("POST_UPDATE"))
			        registry.getEventListenerGroup(EventType.POST_UPDATE).appendListener((PostUpdateEventListener)listener);
				if(key.equals("POST_DELETE"))
			        registry.getEventListenerGroup(EventType.POST_DELETE).appendListener((PostDeleteEventListener)listener);
				if(key.equals("AUTO_FLUSH"))
				    registry.getEventListenerGroup(EventType.AUTO_FLUSH).appendListener((AutoFlushEventListener)listener);
				if(key.equals("CLEAR"))
				    registry.getEventListenerGroup(EventType.CLEAR).appendListener((ClearEventListener)listener);
				if(key.equals("DELETE"))
				    registry.getEventListenerGroup(EventType.DELETE).appendListener((DeleteEventListener)listener);
				if(key.equals("DIRTY_CHECK"))
				    registry.getEventListenerGroup(EventType.DIRTY_CHECK).appendListener((DirtyCheckEventListener)listener);
				if(key.equals("EVICT"))
				    registry.getEventListenerGroup(EventType.EVICT).appendListener((EvictEventListener)listener);
				if(key.equals("FLUSH"))
				    registry.getEventListenerGroup(EventType.FLUSH).appendListener((FlushEventListener)listener);
				if(key.equals("FLUSH_ENTITY"))
				    registry.getEventListenerGroup(EventType.FLUSH_ENTITY).appendListener((FlushEntityEventListener)listener);
				if(key.equals("INIT_COLLECTION"))
				    registry.getEventListenerGroup(EventType.INIT_COLLECTION).appendListener((InitializeCollectionEventListener)listener);
				if(key.equals("LOAD"))
				    registry.getEventListenerGroup(EventType.LOAD).appendListener((LoadEventListener)listener);
				if(key.equals("LOCK"))
				    registry.getEventListenerGroup(EventType.LOCK).appendListener((LockEventListener)listener);
				if(key.equals("MERGE"))
				    registry.getEventListenerGroup(EventType.MERGE).appendListener((MergeEventListener)listener);
				if(key.equals("PERSIST"))
				    registry.getEventListenerGroup(EventType.PERSIST).appendListener((PersistEventListener)listener);
				if(key.equals("PERSIST_ONFLUSH"))
				    registry.getEventListenerGroup(EventType.PERSIST_ONFLUSH).appendListener((PersistEventListener)listener);
				if(key.equals("POST_COLLECTION_RECREATE"))
				    registry.getEventListenerGroup(EventType.POST_COLLECTION_RECREATE).appendListener((PostCollectionRecreateEventListener)listener);
				if(key.equals("POST_COLLECTION_REMOVE"))
				    registry.getEventListenerGroup(EventType.POST_COLLECTION_REMOVE).appendListener((PostCollectionRemoveEventListener)listener);
				if(key.equals("POST_COLLECTION_UPDATE"))
				    registry.getEventListenerGroup(EventType.POST_COLLECTION_UPDATE).appendListener((PostCollectionUpdateEventListener)listener);
				if(key.equals("POST_COMMIT_DELETE"))
				    registry.getEventListenerGroup(EventType.POST_COMMIT_DELETE).appendListener((PostDeleteEventListener)listener);
				if(key.equals("POST_COMMIT_INSERT"))
				    registry.getEventListenerGroup(EventType.POST_COMMIT_INSERT).appendListener((PostInsertEventListener)listener);
				if(key.equals("POST_COMMIT_UPDATE"))
				    registry.getEventListenerGroup(EventType.POST_COMMIT_UPDATE).appendListener((PostUpdateEventListener)listener);
				if(key.equals("POST_LOAD"))
				    registry.getEventListenerGroup(EventType.POST_LOAD).appendListener((PostLoadEventListener)listener);
				if(key.equals("PRE_COLLECTION_RECREATE"))
				    registry.getEventListenerGroup(EventType.PRE_COLLECTION_RECREATE).appendListener((PreCollectionRecreateEventListener)listener);
				if(key.equals("PRE_COLLECTION_REMOVE"))
				    registry.getEventListenerGroup(EventType.PRE_COLLECTION_REMOVE).appendListener((PreCollectionRemoveEventListener)listener);
				if(key.equals("PRE_COLLECTION_UPDATE"))
				    registry.getEventListenerGroup(EventType.PRE_COLLECTION_UPDATE).appendListener((PreCollectionUpdateEventListener)listener);
				if(key.equals("PRE_DELETE"))
				    registry.getEventListenerGroup(EventType.PRE_DELETE).appendListener((PreDeleteEventListener)listener);
				if(key.equals("PRE_INSERT"))
				    registry.getEventListenerGroup(EventType.PRE_INSERT).appendListener((PreInsertEventListener)listener);
				if(key.equals("PRE_LOAD"))
				    registry.getEventListenerGroup(EventType.PRE_LOAD).appendListener((PreLoadEventListener)listener);
				if(key.equals("PRE_UPDATE"))
				    registry.getEventListenerGroup(EventType.PRE_UPDATE).appendListener((PreUpdateEventListener)listener);
				if(key.equals("REFRESH"))
				    registry.getEventListenerGroup(EventType.REFRESH).appendListener((RefreshEventListener)listener);
				if(key.equals("REPLICATE"))
				    registry.getEventListenerGroup(EventType.REPLICATE).appendListener((ReplicateEventListener)listener);
				if(key.equals("RESOLVE_NATURAL_ID"))
				    registry.getEventListenerGroup(EventType.RESOLVE_NATURAL_ID).appendListener((ResolveNaturalIdEventListener)listener);
				if(key.equals("SAVE"))
				    registry.getEventListenerGroup(EventType.SAVE).appendListener((SaveOrUpdateEventListener)listener);
				if(key.equals("SAVE_UPDATE"))
				    registry.getEventListenerGroup(EventType.SAVE_UPDATE).appendListener((SaveOrUpdateEventListener)listener);
				if(key.equals("UPDATE"))
				    registry.getEventListenerGroup(EventType.UPDATE).appendListener((SaveOrUpdateEventListener)listener);
			}
		}
        return sessionFactory;
    }
}
