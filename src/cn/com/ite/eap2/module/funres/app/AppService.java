package cn.com.ite.eap2.module.funres.app;

import java.io.File;

import cn.com.ite.eap2.core.service.DefaultService;

/**
 * <p>Title cn.com.ite.eap2.module.funres.app.AppService</p>
 * <p>Description 系统的服务接口</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2014</p>
 * @author 宋文科
 * @create time: 2014-7-23 下午03:20:45
 * @version 2.0
 * 
 * @modified records:
 */
public interface AppService extends DefaultService{
	/**
	 * 导入系统模块资源菜单数据字典
	 * @param xls EXCEL文件
	 * @modified
	 */
	void importDate(File xls) throws Exception;
	/**
	 * 导出系统模块资源菜单数据字典
	 * @param appId
	 * @return
	 * @modified
	 */
	File exportDate(String appId) throws Exception;
}
