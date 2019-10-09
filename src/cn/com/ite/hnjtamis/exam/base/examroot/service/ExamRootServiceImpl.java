package cn.com.ite.hnjtamis.exam.base.examroot.service;

import java.util.*;

import cn.com.ite.eap2.core.service.DefaultServiceImpl;
import cn.com.ite.hnjtamis.exam.hibernatemap.ExamExamroot;

public class ExamRootServiceImpl extends DefaultServiceImpl implements ExamRootService{
	public void deleteExamRoots(List<String> deleteList){
		if(deleteList!=null && deleteList.size()>0){
			List params = new ArrayList();
			Map term = new HashMap();
			term.put("deList", deleteList);
			params.add(term);
			this.excuteQl("deleteExamExamrootHql", params);
		}
	}
}
