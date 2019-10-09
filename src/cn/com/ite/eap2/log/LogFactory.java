package cn.com.ite.eap2.log;

import cn.com.ite.eap2.core.spring.SpringContextUtil;


/**
 * <p>Title:com.ite.log.LogFactory.java</p>
 * <p>Description:日志工厂</p>
 * <p>Copyright:(c)2011</p>
 * <p>Company:ITE</p>
 * @author songwenke
 * @version 1.0
 * @date 2011-7-7
 * @modify
 */
public class LogFactory {
	private LogFactory(){}
	/**
	 * 得到日志实例
	 * @return
	 * @modified
	 */
	public static Log getLog(){
		return (Log)SpringContextUtil.getBean("logImpl");
	}
}
