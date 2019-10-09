package cn.com.ite.hnjtamis.exam.base.exammarkpeopleinfo.service;

import java.util.List;

import cn.com.ite.eap2.core.service.DefaultService;
import cn.com.ite.hnjtamis.exam.base.exammarkpeopleinfo.form.ExamReviewerForm;
import cn.com.ite.hnjtamis.exam.hibernatemap.Exam;
import cn.com.ite.hnjtamis.exam.hibernatemap.ExamMarkpeople;

public interface ExamMarkPeopleService extends DefaultService{
	public List<String> save(ExamReviewerForm reviewerForm,Exam exam,List<ExamMarkpeople> deletes) throws Exception;
}
