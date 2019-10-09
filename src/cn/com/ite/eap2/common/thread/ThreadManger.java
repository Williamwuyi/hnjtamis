package cn.com.ite.eap2.common.thread;

import java.util.*;

import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate4.SessionFactoryUtils;
import org.springframework.orm.hibernate4.SessionHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import cn.com.ite.eap2.Config;
import cn.com.ite.eap2.core.spring.SpringContextUtil;

/**
 * <p>Title cn.com.ite.eap2.core.thread.ThreadManger</p>
 * <p>Description 线程管理器</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2014</p>
 * @author 宋文科
 * @create time: 2014-6-20 下午02:27:16
 * @version 2.0
 * 
 * @modified records:
 */
public final class ThreadManger {
	private ThreadManger(){}
	/**
	 * 最大线程
	 */
	private static int max = Integer.parseInt(Config.getPropertyValue("MAX_THREAD_SIZE"));
	/**
	 * 线程池
	 */
	private static List<Thread> threads = new ArrayList<Thread>();
	/**
	 * 打开一个线程
	 * @param run 执行方法
	 * @param cycleTime 循环等待时间（毫秒）
	 * @param initTime 初始时间（毫秒）
	 * @modified
	 */
	public static void openThread(final Run run,final long cycleTime,final long initTime) throws Exception{
		if(threads.size() == max)
			throw new Exception("打开的线程超过最大数"+max);
		Thread thread = new Thread(){
			@SuppressWarnings("static-access")
			public void run() {
				try {
					this.sleep(initTime);
					SessionFactory sessionFactory = SpringContextUtil.getApplicationContext().
					            getBean("sessionFactory",SessionFactory.class);
					Session session = sessionFactory.openSession();
				    session.setFlushMode(FlushMode.AUTO);
					SessionHolder sessionHolder = new SessionHolder(session);
		            TransactionSynchronizationManager.bindResource(sessionFactory, sessionHolder);
					while(true){
						if(run.stop())
							break;
						if(run.suspend()){
							if(!run.start())
								this.sleep(1000);
						}
						run.hander();
						session.flush();
						this.sleep(cycleTime);
					}
					threads.remove(this);
					TransactionSynchronizationManager.unbindResource(sessionFactory);
					SessionFactoryUtils.closeSession(session);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		thread.start();
		threads.add(thread);
	}
}