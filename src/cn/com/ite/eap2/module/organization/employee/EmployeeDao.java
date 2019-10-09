package cn.com.ite.eap2.module.organization.employee;

import java.util.List;

import cn.com.ite.eap2.core.hibernate.DefaultDAO;
import cn.com.ite.eap2.core.service.TreeNode;

/**
 *
 * <p>Title cn.com.ite.eap2.module.organization.employee.EmployeeDao</p>
 * <p>Description 人员信息Dao</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2016</p>
 * @create time: 2016年9月13日 上午9:31:00
 * @version 1.0
 * 
 * @modified records:
 */
public interface EmployeeDao extends DefaultDAO{
	
	/**
	 * 根据deptId包括的子部门
	 * @author 朱健
	 * @param deptId
	 * @return
	 * @modified
	 */
	public String queryOwnerDeptIds(String deptId);
	/**
	 * 获取机构部门树
	 * @author 朱健
	 * @return
	 * @modified
	 */
	public List<TreeNode> findOrganDeptEmployeeTree(String parentId,String employeeName)throws Exception;
	
	/**
	 * 查询机构部门员工树，用于分级下载
	 * @param organId 机构ID
	 * @param employeeName 员工名称
	 * @param parentId 父部门ID
	 * @param parentType 父结点类型
	 * @return
	 * @throws Exception
	 * @modified
	 */
	public List<TreeNode> findPxOrganDeptEmployeeTree(String organId,String employeeName,String parentId,String parentType)throws Exception;
	/**
	 * 查询机构部门员工树，用于分级下载
	 * @param organId 机构ID
	 * @param employeeName 员工名称
	 * @param parentId 父部门ID
	 * @param parentType 父结点类型
	 * @return
	 * @throws Exception
	 * @modified
	 */
	public List<TreeNode> findOrganDeptEmployeeTree(String organId,String employeeName,String parentId,String parentType)throws Exception;

}
