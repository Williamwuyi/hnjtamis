package cn.com.ite.eap2.module.organization.dept;

import java.util.*;

import cn.com.ite.eap2.core.service.DefaultService;
import cn.com.ite.eap2.core.service.TreeNode;

/**
 * <p>Title cn.com.ite.eap2.module.organization.organ.OrganService</p>
 * <p>Description 机构部门服务接口</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2014</p>
 * @author 宋文科
 * @create time: 2014-6-13 下午01:51:37
 * @version 2.0
 * 
 * @modified records:
 */
public interface DeptService extends DefaultService{
	/**
	 * 根据部门进行查询
	 * @author 朱健
	 * @param deptId
	 * @return
	 * @modified
	 */
	public List<TreeNode> queryOwnerDeptTree(String deptId);
	
	/**
	 * 获取机构部门树
	 * @author 朱健
	 * @return
	 * @modified
	 */
	public List<TreeNode> findOwerOrganTree(String organId,String deptName)throws Exception;
	/**
	 * 机构部门树的数据提取
	 * @param topOrganId 项级机构ID
	 * @param deptName 部门名称(模糊匹配）
	 * @return 树结点
	 */
	List<TreeNode> findOrganDeptTree(String topOrganId,String deptName)throws Exception;
	/**
	 * 机构部门切换树的数据提取
	 * @param topOrganId 项级机构ID
	 * @param deptName 部门名称(模糊匹配）
	 * @return 树结点
	 */
	List<TreeNode> findOrganDeptSwitchTree(String topOrganId,String topDeptId)throws Exception;
}
