package cn.com.ite.eap2.module.organization.example;

import java.util.List;

import cn.com.ite.eap2.core.service.DefaultService;
import cn.com.ite.eap2.core.service.TreeNode;

public interface ExampleService extends DefaultService{
	List<TreeNode> findOrganDeptTree(String topOrganId,String deptTerm,String employeeTerm,String postTerm)throws Exception;
	public List getPerson(String selectId, String deptId,String deptTerm,String employeeTerm,String postTerm);
}
