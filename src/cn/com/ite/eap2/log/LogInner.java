package cn.com.ite.eap2.log;

import java.util.List;
import java.util.Map;

import cn.com.ite.eap2.core.service.DefaultService;

/**
 * <p>Title:com.ite.log.service.LogControlExt.java</p>
 * <p>Description:日志控制服务对内接口</p>
 * <p>Copyright:(c)2011</p>
 * <p>Company:ITE</p>
 * @author 宋文科
 * @version 1.0
 * @date 2014-6-7
 * @modify
 */
interface LogInner extends Log,DefaultService{
	/**
	 * 查询日志操作，对内提供
	 * @param id 标识
	 * @return
	 */
	@SuppressWarnings("unchecked")
	List<Map> findLogHandleInfo(String id);
	void saveLogThreadFun() throws Exception;
}
