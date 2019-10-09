package cn.com.ite.eap2.log;

import org.hibernate.event.spi.PostDeleteEvent;
import org.hibernate.event.spi.PostDeleteEventListener;
import org.hibernate.event.spi.PostInsertEvent;
import org.hibernate.event.spi.PostInsertEventListener;
import org.hibernate.event.spi.PostUpdateEvent;
import org.hibernate.event.spi.PostUpdateEventListener;
import org.hibernate.persister.entity.EntityPersister;

/**
 * <p>Title cn.com.ite.eap2.log.HibernateListener</p>
 * <p>Description hibernate监听器</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2014</p>
 * @author 宋文科
 * @create time: 2014-6-13 下午12:30:06
 * @version 2.0
 * 
 * @modified records:
 */
public class HibernateListener implements PostInsertEventListener,PostUpdateEventListener, PostDeleteEventListener {
	private static final long serialVersionUID = -27021776593747296L;

	@Override
	public void onPostUpdate(PostUpdateEvent event) {
		System.out.println(event.getEntity().getClass().getName()+":更新完毕");  
		for (int i = 0; i < event.getState().length; i++) {  // 更新前的值  
			Object oldValue = event.getOldState()[i];  // 更新后的新值  
			Object newValue = event.getState()[i];  //更新的属性名  
			String propertyName = event.getPersister().getPropertyNames()[i];  
			System.out.println(propertyName);
			System.out.println("oldValue="+oldValue);
			System.out.println("newValue="+newValue);
		}
	}

	@Override
	public boolean requiresPostCommitHanding(EntityPersister entitypersister) {
		System.out.println("requiresPostCommitHanding="+entitypersister.getEntityName());
		return false;
	}

	@Override
	public void onPostDelete(PostDeleteEvent postdeleteevent) {
		System.out.println("delete"+postdeleteevent.getEntity().getClass());
	}

	@Override
	public void onPostInsert(PostInsertEvent postinsertevent) {
		System.out.println("add"+postinsertevent.getEntity().getClass());
	}
}
