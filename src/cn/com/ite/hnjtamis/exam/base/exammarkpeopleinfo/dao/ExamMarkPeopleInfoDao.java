package cn.com.ite.hnjtamis.exam.base.exammarkpeopleinfo.dao;


import java.util.List;

import cn.com.ite.eap2.core.hibernate.DefaultDAO;

/**
 *
 * @author 朱健
 * @create time: 2016年1月13日 上午9:09:51
 * @version 1.0
 * 
 * @modified records:
 */
public interface ExamMarkPeopleInfoDao extends DefaultDAO {
	 
	
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
