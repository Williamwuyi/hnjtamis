package cn.com.ite.eap2.common.thread;

/**
 * <p>Title cn.com.ite.eap2.core.thread.Run</p>
 * <p>Description 线程执行体接口</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2014</p>
 * @author 宋文科
 * @create time: 2014-6-20 下午02:26:06
 * @version 2.0
 * 
 * @modified records:
 */
public interface Run {
	/**
	 * 具体执行内容
	 * @modified
	 */
	void hander();
	/**
	 * 停止处理
	 * @return
	 * @modified
	 */
	boolean stop();
	/**
	 * 挂起处理
	 * @return
	 * @modified
	 */
	boolean suspend();
	/**
	 * 启动处理，与suspend成对使用
	 * @return
	 * @modified
	 */
	boolean start();
}
