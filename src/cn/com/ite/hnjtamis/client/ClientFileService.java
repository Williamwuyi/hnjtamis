package cn.com.ite.hnjtamis.client;

import cn.com.ite.eap2.core.service.DefaultService;

public interface ClientFileService extends DefaultService {
	/**
	 * 设置指定文件名指定恢复版本
	 * 
	 * @param fileName
	 * @param id 不包括的ID
	 */
	public void saveRecoverVersion(String fileName, String id);
}
