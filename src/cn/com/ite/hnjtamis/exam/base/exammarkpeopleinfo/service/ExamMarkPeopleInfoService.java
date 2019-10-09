package cn.com.ite.hnjtamis.exam.base.exammarkpeopleinfo.service;

import java.util.List;

import cn.com.ite.eap2.core.service.DefaultService;

public interface ExamMarkPeopleInfoService extends DefaultService{

	/**
	 * 查询阅卷老师
	 * @author 朱健
	 * @return
	 * @modified
	 */
	public List getExaminationUserList(String organId);
	
	/**
	 * 查询阅卷老师
	 * @author 朱健
	 * @return
	 * @modified
	 */
	public List getExaminationUserInExamList(String examId);
	
	/**
	 * 查询阅卷老师(还包括本机构的其它人员)
	 * @author 朱健
	 * @return
	 * @modified
	 */
	public List getExaminationUserInExamListEx(String examId,String organId,String nameTerm);
}
