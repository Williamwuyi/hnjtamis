package cn.com.ite.eap2.module.organization.dept;

import java.util.List;

import cn.com.ite.eap2.core.hibernate.DefaultDAO;
import cn.com.ite.eap2.core.service.TreeNode;

public interface DeptDao extends DefaultDAO{

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
	 * 获取机构部门树
	 * @author 朱健
	 * @return
	 * @modified
	 */
	public List<TreeNode> findOrganDeptTree(String topOrganId,String deptName)throws Exception;
	
	
	
	/**
	 * 机构部门切换树的数据提取
	 * @param topOrganId 项级机构ID
	 * @param deptName 部门名称(模糊匹配）
	 * @return 树结点
	 */
	@SuppressWarnings("unchecked")
	public List<TreeNode> findOrganDeptSwitchTree(String topOrganId,String topDeptId,String deptName)throws Exception;
}
