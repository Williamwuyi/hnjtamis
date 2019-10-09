package cn.com.ite.eap2.module.organization.example;

import java.util.List;

import cn.com.ite.eap2.core.service.DefaultServiceImpl;
import cn.com.ite.eap2.core.service.TreeNode;

public class ExampleServiceImpl extends DefaultServiceImpl implements ExampleService {

	@Override
	public List getPerson(String selectId, String deptId,String deptTerm,String employeeTerm,String postTerm) {
		ExampleDao e = (ExampleDao) this.getDao();
		return e.getPerson(selectId, deptId,deptTerm,employeeTerm,postTerm);
	}

	@Override
	public List<TreeNode> findOrganDeptTree(String topOrganId, String deptTerm,String employeeTerm,String postTerm)throws Exception {
		ExampleDao e = (ExampleDao) this.getDao();
		return e.findOrganDeptTree(topOrganId, deptTerm,employeeTerm, postTerm);
	}

}
