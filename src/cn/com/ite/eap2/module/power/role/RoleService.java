package cn.com.ite.eap2.module.power.role;

import java.util.*;
import cn.com.ite.eap2.core.service.DefaultService;
import cn.com.ite.eap2.core.service.TreeNode;

/**
 * <p>Title cn.com.ite.eap2.module.power.role.RoleService</p>
 * <p>Description 角色服务接口</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2014</p>
 * @author 宋文科
 * @create time: 2014-7-10 上午10:08:54
 * @version 2.0
 * 
 * @modified records:
 */
public interface RoleService extends DefaultService{
	/**
	 * 角色树的数据提取方步，只取用户条件中拥有的角色，
	 * 用户ID为admin表示超级管理员，不受限制
	 * @param userId 管理者用户ID
	 * @param allotUserId 要分配角色的用户ID
	 * @param deptId 要分配角色的部门ID
	 * @param quarterId 要分配角色的岗位ID
	 * @return
	 */
	List<TreeNode> findMRTree(String userId,String allotUserid,String deptId,String quarterId)throws Exception;

	/**
	 * 角色树的数据提取方步，只取用户条件中拥有的角色，
	 * 用户ID为admin表示超级管理员，不受限制
	 * @param userId 管理者用户ID
	 * @param allotUserId 要分配角色的用户ID
	 * @param deptId 要分配角色的部门ID
	 * @param quarterId 要分配角色的岗位ID
	 * @return
	 */
	List<TreeNode> findMRTreeFilteByOran(String userId,String allotUserid,String deptId,String quarterId,String organId)throws Exception;
}
