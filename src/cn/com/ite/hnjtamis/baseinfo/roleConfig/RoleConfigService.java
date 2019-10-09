package cn.com.ite.hnjtamis.baseinfo.roleConfig;

import java.util.List;

import cn.com.ite.eap2.core.service.DefaultService;
import cn.com.ite.eap2.core.service.TreeNode;

/**
 *
 * <p>Title cn.com.ite.hnjtamis.baseinfo.roleConfig.RoleConfigService</p>
 * <p>Description 角色配置管理</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2016</p>
 * @create time: 2016年11月18日 上午9:22:48
 * @version 1.0
 * 
 * @modified records:
 */
public interface RoleConfigService extends DefaultService{


	/**
	 * 获取角色配置树
	 * @description
	 * @param configId
	 * @return
	 * @throws Exception
	 * @modified
	 */
	public List<TreeNode> findRoleConfigTree(String configId)throws Exception;
}
