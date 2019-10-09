package cn.com.ite.eap2.module.funres.module;

import java.util.*;
import cn.com.ite.eap2.core.service.DefaultService;
import cn.com.ite.eap2.core.service.TreeNode;
import cn.com.ite.eap2.domain.funres.AppModule;

/**
 * <p>Title cn.com.ite.eap2.module.funres.module.ModuleListAction</p>
 * <p>Description 模块的服务接口</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2014</p>
 * @author 宋文科
 * @create time: 2014-6-24 下午01:27:22
 * @version 2.0
 * 
 * @modified records:
 */
public interface ModuleService extends DefaultService{
	/**
	 * 模块资源树的数据提取方步
	 * @param appId 系统ID
	 * @param resourceName 资源名称
	 * @return
	 */
	List<TreeNode> findMRTree(String appId,String resourceName)throws Exception;
	/**
	 * 系统模块资源树
	 * @param userId 当前用户
	 * @param roleId 角色分配资源中的角色ID
	 * @param proxy 是否在代理模式下
	 * @param resourceName 资源名称查询条件
	 * @return 树结点
	 * @throws Exception
	 * @modified
	 */
	List<TreeNode> findAMRTree(String userId,String roleId,boolean proxy,String resourceName)throws Exception;
	/**
	 * 生成菜单 
	 * @param module
	 * @modified
	 */
	void makeMenu(AppModule module);
	void saveModule(AppModule module);
}
