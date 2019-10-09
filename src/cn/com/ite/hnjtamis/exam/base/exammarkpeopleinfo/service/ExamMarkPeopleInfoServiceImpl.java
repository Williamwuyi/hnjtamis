package cn.com.ite.hnjtamis.exam.base.exammarkpeopleinfo.service;

import java.util.List;

import cn.com.ite.eap2.core.service.DefaultServiceImpl;
import cn.com.ite.hnjtamis.exam.base.exammarkpeopleinfo.dao.ExamMarkPeopleInfoDao;

public class ExamMarkPeopleInfoServiceImpl extends DefaultServiceImpl implements ExamMarkPeopleInfoService{

	/**
	 * 查询阅卷老师
	 * @author 朱健
	 * @return
	 * @modified
	 */
	public List getExaminationUserList(String organId){
		ExamMarkPeopleInfoDao examMarkPeopleInfoDao = (ExamMarkPeopleInfoDao)this.getDao();
		return examMarkPeopleInfoDao.getExaminationUserList(organId);
	}
	
	/**
	 * 查询阅卷老师(还包括本机构的其它人员)
	 * @author 朱健
	 * @return
	 * @modified
	 */
	public List getExaminationUserInExamList(String examId){
		ExamMarkPeopleInfoDao examMarkPeopleInfoDao = (ExamMarkPeopleInfoDao)this.getDao();
		return examMarkPeopleInfoDao.getExaminationUserInExamList(examId);
	}
	
	/**
	 * 查询阅卷老师(还包括本机构的其它人员)
	 * @author 朱健
	 * @return
	 * @modified
	 */
	public List getExaminationUserInExamListEx(String examId,String organId,String nameTerm){
		ExamMarkPeopleInfoDao examMarkPeopleInfoDao = (ExamMarkPeopleInfoDao)this.getDao();
		return examMarkPeopleInfoDao.getExaminationUserInExamListEx(examId,organId,nameTerm);
	}
}
