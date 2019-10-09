package cn.com.ite.eap2.module.organization.employee;

import java.util.List;

import cn.com.ite.eap2.core.service.DefaultService;
import cn.com.ite.eap2.core.service.TreeNode;

/**
 * <p>Title cn.com.ite.eap2.module.organization.organ.OrganService</p>
 * <p>Description 员工服务接口</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2014</p>
 * @author 宋文科
 * @create time: 2014-6-13 下午01:51:37
 * @version 2.0
 * 
 * @modified records:
 */
public interface EmployeeService extends DefaultService{
	
	/**
	 * 根据deptId包括的子部门
	 * @author 朱健
	 * @param deptId
	 * @return
	 * @modified
	 */
	public String queryOwnerDeptIds(String deptId);
	/**
	 * 查询机构部门员工树
	 * @param organId 机构ID
	 * @param employeeName 员工名称
	 * @return
	 * @throws Exception
	 * @modified
	 */
	List<TreeNode> findOrganDeptEmployeeTree(String organId,String employeeName)throws Exception;
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
	List<TreeNode> findPxOrganDeptEmployeeTree(String organId,String employeeName,String parentId,String parentType)throws Exception;
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
	List<TreeNode> findOrganDeptEmployeeTree(String organId,String employeeName,String parentId,String parentType)throws Exception;
}
