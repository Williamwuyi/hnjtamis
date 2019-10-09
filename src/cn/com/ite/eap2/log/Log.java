package cn.com.ite.eap2.log;

import java.util.*;

import cn.com.ite.eap2.domain.baseinfo.LogMain;

/**
 * <p>Title:com.ite.log.service.LogControlExt.java</p>
 * <p>Description:日志控制服务对外接口</p>
 * <p>Copyright:(c)2011</p>
 * <p>Company:ITE</p>
 * @author songwenke
 * @version 1.0
 * @date 2014-6-7
 * @modify
 */
public interface Log {
	/**
	 * 操作日志
	 * @param logContent 日志内容
	 */
	void info(String logContent);
	/**
	 * 登录日志
	 * @param logContent 日志内容
	 * @modified
	 */
	void login(String logContent);
	/**
	 * 注销日志
	 * @param logContent 日志内容
	 * @modified
	 */
	void quit(String logContent);
	/**
	 * 异常日志
	 * @param logContent 日志内容
	 * @modified
	 */
	void error(String logContent);
	/**
	 * 警告日志
	 * @param logContent 日志内容
	 * @modified
	 */
	void warning(String logContent);
	public static int LOG_TYPE_LOGIN = 1;
	public static int LOG_TYPE_QUIT = 2;
	public static int LOG_TYPE_INFO = 3;
	public static int LOG_TYPE_ERROR = 4;
	public static int LOG_TYPE_WARING = 5;
	/**
	 * 日志查询
	 * @param user 账号或姓名
	 * @param app 系统
	 * @param module 模块
	 * @param oper 操作
	 * @param content 内容
	 * @param type 类型 1登录2退出3数据操作4异常5警告
	 * @param year 年
	 * @param month 月
	 * @param startDay 开始日
	 * @param endDay 结束日
	 * @return
	 */
	List<LogMain> findLog(String user,String app,String module,String oper,
			String content,int type,int year,int month,int startDay,int endDay);
}
