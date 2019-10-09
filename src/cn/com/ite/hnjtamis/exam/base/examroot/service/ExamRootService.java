package cn.com.ite.hnjtamis.exam.base.examroot.service;

import java.util.List;

import cn.com.ite.eap2.core.service.DefaultService;
import cn.com.ite.hnjtamis.exam.hibernatemap.ExamExamroot;

public interface ExamRootService extends DefaultService{
	public void deleteExamRoots(List<String> deleteList);
}
