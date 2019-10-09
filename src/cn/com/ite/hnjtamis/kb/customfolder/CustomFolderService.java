package cn.com.ite.hnjtamis.kb.customfolder;

import java.util.List;

import cn.com.ite.eap2.core.service.DefaultService;
import cn.com.ite.eap2.core.service.TreeNode;

/**
 * <p>
 * Title cn.com.ite.hnjtamis.kb.customfolder.CustomFolderService
 * </p>
 * <p>
 * Description 自定义文件夹服务接口
 * </p>
 * <p>
 * Company ITE
 * </p>
 * <p>
 * Copyright Copyright(c)2014
 * </p>
 * 
 * @author 李奉学
 * @create time 2015-3-30
 * @version 1.0
 * 
 * @modified
 */
public interface CustomFolderService extends DefaultService {
	/**
	 * 自定义文件夹树
	 * @param createUserId
	 * @return
	 * @throws Exception
	 */
	public List<TreeNode> findFolderTree(String createUserId) throws Exception;
}
