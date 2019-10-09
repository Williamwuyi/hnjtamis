package cn.com.ite.hnjtamis.personal.mainpage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.ite.eap2.core.service.DefaultServiceImpl;
import cn.com.ite.hnjtamis.exam.base.exampublic.service.ExamPublicService;
import cn.com.ite.hnjtamis.exam.base.exampublicuser.service.ExamPublicUserService;
import cn.com.ite.hnjtamis.exam.hibernatemap.ExamPublic;
import cn.com.ite.hnjtamis.exam.hibernatemap.ExamUserTestpaper;
import cn.com.ite.hnjtamis.kb.course.CoursewareService;
import cn.com.ite.hnjtamis.kb.domain.Courseware;
import cn.com.ite.hnjtamis.personal.domain.PersonalRateProgress;
import cn.com.ite.hnjtamis.personal.rateprogress.PersonalRateProgressService;
import cn.com.ite.hnjtamis.train.domain.TrainImplement;
import cn.com.ite.hnjtamis.train.impl.TrainImplementService;
/**
 * 
 * <p>Title 岗位达标培训信息系统-个人学习管理模块</p>
 * <p>Description 个人首页展示service实现</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2015</p>
 * @author wangyong
 * @create Apr 15, 2015  10:45:27 AM
 * @version 1.0
 * 
 * @modified records:
 */
public class PersonalMainPageServiceImpl extends DefaultServiceImpl implements
		PersonalMainPageService {
	private PersonalRateProgressService personalrateprogressServer; // 个人达标情况
	private TrainImplementService trainImplementService; /// 岗位安排课程
	private CoursewareService coursewareService; /// 课件
	private ExamPublicService examPublicServer; // 信息发布（发布的考试信息）
	private ExamPublicUserService examPublicUserServer; // 考生 信息（考生报名后信息与考试结果）
	
	/**
	 * 查询个人岗位达标记录
	 * @param personcode 员工ID
	 * @param reachtype  达标类型 1：学时达标 2：考试达标 其他值则查询所有
	 * @return
	 */
	public List<PersonalRateProgressEx> findPersonalRateProgressRec(String personcode,String reachtype){
//		Map<String, Boolean> sortmap = new HashMap<String, Boolean>();
//		sortmap.put("creationDate", false);
		Map<String,Object> term = new HashMap<String, Object>();
		term.put("PERSONCODE", personcode);
		
		List list = this.queryData("querySqlPersonalRateProgress", term, null, PersonalRateProgressEx.class, 0, 10);
		//List<PersonalRateProgress> list = getPersonalrateprogressServer().queryData("queryHql", term, null);
		
		return list;
	}
//	
//	// 查询考试得分
//	public List<ExamUserTestpaper> findPersonExams(String personcode){
//		return null;
//	}
//	
	/// 查询考试通知提醒
	/**
	 * 根据岗位ID查询考试发布通知
	 */
	public List<ExamPublic> findExamPublic(String quarterid){
//		Map<String, Boolean> sortmap = new HashMap<String, Boolean>();
//		sortmap.put("creationDate", false);
		Map<String,Object> term = new HashMap<String, Object>();
		term.put("postId", quarterid);
		List<ExamPublic> list = queryData("queryExamPublic", term, null);
		
		return list;
	}
	
	/**
	 * 根据员工ID查询考试结果信息
	 * @param employeeid
	 * @return
	 */
	public List<ExamUserTestpaper> findExamUserTestpapaer(String employeeid){
		Map<String,Object> term = new HashMap<String, Object>();
		term.put("employeeid", employeeid);
		List<ExamUserTestpaper> list=this.queryData("queryUserTestpager", term, null);
		return list;
	}
	
	/**
	 * 查询岗位安排课程
	 * @param quarterId
	 * @return
	 */
	public List<TrainImplement> findTrainImplement(String quarterId){
		List<TrainImplement> list = getTrainImplementService().queryQuarterCourse(quarterId);
		return list;
	}

//	// 查询课件信息
	/**
	 * 查询最新有效课件信息
	 */
	public List<Courseware> findCourseware(){
//		Map<String, Boolean> sortmap = new HashMap<String, Boolean>();
//		sortmap.put("creationDate", false);
		Map<String,Object> term = new HashMap<String, Object>();
		term.put("organId", "");
		term.put("titleTerm", "");
		term.put("uploaderTerm", "");
		term.put("typeTerm", null);
		List<Courseware> list = getCoursewareService().queryData("queryHql", term,null, 0, 20);
		return list;
	}
	public TrainImplementService getTrainImplementService() {
		return trainImplementService;
	}
	public void setTrainImplementService(TrainImplementService trainImplementService) {
		this.trainImplementService = trainImplementService;
	}
	public CoursewareService getCoursewareService() {
		return coursewareService;
	}
	public void setCoursewareService(CoursewareService coursewareService) {
		this.coursewareService = coursewareService;
	}
	public ExamPublicService getExamPublicServer() {
		return examPublicServer;
	}
	public void setExamPublicServer(ExamPublicService examPublicServer) {
		this.examPublicServer = examPublicServer;
	}
	public ExamPublicUserService getExamPublicUserServer() {
		return examPublicUserServer;
	}
	public void setExamPublicUserServer(ExamPublicUserService examPublicUserServer) {
		this.examPublicUserServer = examPublicUserServer;
	}
	public PersonalRateProgressService getPersonalrateprogressServer() {
		return personalrateprogressServer;
	}
	public void setPersonalrateprogressServer(
			PersonalRateProgressService personalrateprogressServer) {
		this.personalrateprogressServer = personalrateprogressServer;
	}
}
