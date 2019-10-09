package cn.com.ite.eap2.module.organization.example;

import java.util.List;

import cn.com.ite.eap2.core.hibernate.DefaultDAO;
import cn.com.ite.eap2.core.service.TreeNode;

public interface ExampleDao extends DefaultDAO{
	/**
	 * 获取机构部门树
	 * @author 吴毅
	 * @return
	 * @modified
	 */
	public List<TreeNode> findOrganDeptTree(String topOrganId,String deptTerm,String employeeTerm,String postTerm)throws Exception;
	/**
	 * 根据部门查询部门下员工
	 * @param selectId
	 * @author 吴毅
	 * @param deptId
	 * @return
	 */
	public List getPerson(String selectId, String deptId,String deptTerm,String employeeTerm,String postTerm);
}
