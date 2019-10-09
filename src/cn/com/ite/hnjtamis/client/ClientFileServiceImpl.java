package cn.com.ite.hnjtamis.client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.ite.eap2.core.service.DefaultServiceImpl;
import cn.com.ite.hnjtamis.client.domain.ClientFile;

public class ClientFileServiceImpl extends DefaultServiceImpl implements
		ClientFileService {
	/**
	 * 设置指定文件名指定恢复版本
	 * 
	 * @param fileName
	 */
	@SuppressWarnings("unchecked")
	public void saveRecoverVersion(String fileName, String id) {
		Map<String, String> param = new HashMap<String, String>();
		param.put("fileName", fileName);
		List<ClientFile> list = getDao().queryConfigQl("queryByFileName", param, null, ClientFile.class);
		//设置指定恢复版本状态为0
		for (ClientFile file : list) {
			if (id.equals(file.getId())) 
				continue;
			file.setIsRecoverVer(0);
			getDao().saveEntityOld(file);
		}
	}
}
