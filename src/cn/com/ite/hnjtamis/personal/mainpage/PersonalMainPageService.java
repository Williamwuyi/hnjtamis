package cn.com.ite.hnjtamis.personal.mainpage;

import java.util.List;

import cn.com.ite.eap2.core.service.DefaultService;
import cn.com.ite.hnjtamis.exam.hibernatemap.ExamPublic;
import cn.com.ite.hnjtamis.exam.hibernatemap.ExamUserTestpaper;
import cn.com.ite.hnjtamis.kb.domain.Courseware;
import cn.com.ite.hnjtamis.personal.domain.PersonalRateProgress;
import cn.com.ite.hnjtamis.train.domain.TrainImplement;
/**
 * 
 * <p>Title 岗位达标培训信息系统-个人学习管理模块</p>
 * <p>Description 个人首页展示service实现
 * 包括学习进度、培训积分、达标情况、考试信息，报名信息，达标过期信息等与个人相关的内容，
 * 还包括本岗位最新的教材、资料、考试通知提醒等
 * </p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2015</p>
 * @author wangyong
 * @create Apr 15, 2015  10:45:27 AM
 * @version 1.0
 * 
 * @modified records:
 */
public interface PersonalMainPageService extends DefaultService {
	/**
	 * 查询个人岗位达标记录
	 * @param personcode 员工ID
	 * @param reachtype  达标类型 1：学时达标 2：考试达标 其他值则查询所有
	 * @return
	 */
	public List<PersonalRateProgressEx> findPersonalRateProgressRec(String personcode,String reachtype);
//	
//	// 查询考试得分
//	public List<ExamUserTestpaper> findPersonExams(String personcode);
//	
	/// 查询考试通知提醒
	/**
	 * 根据岗位ID查询考试发布通知
	 */
	public List<ExamPublic> findExamPublic(String quarterid);
	
	/**
	 * 根据员工ID查询考试结果信息
	 * @param employeeid
	 * @return
	 */
	public List<ExamUserTestpaper> findExamUserTestpapaer(String employeeid);
	
	/**
	 * 查询岗位安排课程
	 * @param quarterId
	 * @return
	 */
	public List<TrainImplement> findTrainImplement(String quarterId);

//	// 查询课件信息
	/**
	 * 查询最新有效课件信息
	 */
	public List<Courseware> findCourseware();
}
