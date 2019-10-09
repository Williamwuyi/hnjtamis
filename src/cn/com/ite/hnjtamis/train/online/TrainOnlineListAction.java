package cn.com.ite.hnjtamis.train.online;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.interceptor.ServletRequestAware;

import cn.com.ite.eap2.core.service.TreeNode;
import cn.com.ite.eap2.core.struts2.AbstractListAction;
import cn.com.ite.eap2.module.power.login.LoginAction;
import cn.com.ite.eap2.module.power.login.UserSession;
import cn.com.ite.hnjtamis.exam.exampaper.TrainImplementMoniService;
import cn.com.ite.hnjtamis.exam.hibernatemap.Exam;
import cn.com.ite.hnjtamis.exam.hibernatemap.ExamTestpaper;
import cn.com.ite.hnjtamis.exam.hibernatemap.ExamUserTestpaper;
import cn.com.ite.hnjtamis.train.domain.AccessoryFileTransform;
import cn.com.ite.hnjtamis.train.domain.TrainImplement;
import cn.com.ite.hnjtamis.train.domain.TrainOnline;
import cn.com.ite.hnjtamis.train.domain.TrainOnlineRecord;
import cn.com.ite.hnjtamis.train.impl.TrainImplementService;

/**
 * <p>
 * Title cn.com.ite.hnjtamis.train.online.TrainOnlineListAction
 * </p>
 * <p>
 * Description 在线培训ListAction
 * </p>
 * <p>
 * Company ITE
 * </p>
 * <p>
 * Copyright Copyright(c)2014
 * </p>
 * 
 * @author 李奉学
 * @create time 2015-4-8
 * @version 1.0
 * 
 * @modified
 */
public class TrainOnlineListAction extends AbstractListAction implements
		ServletRequestAware {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5934334813909318768L;

	private TrainImplementService trainImplementService;
	private TrainImplementMoniService trainImplementMoniService;
	private HttpServletRequest request;
	/**
	 * 查询对象
	 */
	private List<TrainOnline> list = new ArrayList<TrainOnline>();

	private List<TreeNode> courseAcces;
	private List<StudyLeaf> preCourse;

	private String studentTerm;// 学员ID查询条件
	private String startDateTerm;// 日期查询条件
	private String endDateTerm;

	private String courseId;// 课程安排ID
	private String trainId;// 在线学习ID
	private String currentLeafId;// 当前学习节点ID

	// 与考试交互的参数
	private String userTestPaperId;// 试卷ID
	private int isHidden;
	private int addExam;
	private int addExamType;
	private int clean;

	private String acceId;
	private AccessoryFileTransform accessory;

	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	/**
	 * 查询方法
	 * 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String list() throws Exception {
		// 显示列表之前先将新的课程安排添加到学院在线培训记录表中
		UserSession session = LoginAction.getUserSessionInfo();
		List<TrainImplement> trains = trainImplementService
				.queryEmployeerCourse(session.getQuarterId(), session
						.getEmployeeId());
		List<TrainOnline> onlineList = new ArrayList<TrainOnline>();
		for (TrainImplement train : trains) {
			TrainOnline online = new TrainOnline();
			online.setTrainImplement(train);
			online.setStudentId(session.getEmployeeId());
			online.setStudentName(session.getEmployeeName());
			online.setStatus(0);
			online.setOrganId(session.getOrganId());
			onlineList.add(online);
		}
		if (onlineList.size() > 0) {
			service.saves(onlineList);
		}

		try {
			list = (List<TrainOnline>) service.queryData("queryHql", this, this
					.getSortMap(), this.getStart(), this.getLimit());
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.setTotal(service.countData("queryHql", this));
		return "list";
	}

	/**
	 * 删除
	 * 
	 * @return
	 * @modified
	 */
	public String delete() throws Exception {
		String[] ids = this.getId().split(",");
		for (int i = 0; i < ids.length; i++) {
			TrainOnline online = (TrainOnline) service.findDataByKey(ids[i],
					TrainOnline.class);
			online.setIsDel(1);
			online.setSyncStatus(3);
			service.saveOld(online);
		}
		this.setMsg("删除成功！");
		return "delete";
	}

	/**
	 * 显示课程信息
	 * 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String showCourseInfo() throws Exception {
		// 查询课程信息
		TrainOnline online = (TrainOnline) service.findDataByKey(courseId,
				TrainOnline.class);
		request.setAttribute("course", online);

		// 分析学习时间
		Set<TrainOnlineRecord> records = online.getTrainOnlineRecords();
		String startTime = "";// 课程学习开始时间
		String endTime = "";// 课程学习结束时间
		int studyCount = 0;// 已经完成学习的节点数
		Map<String, TrainOnlineRecord> map = new HashMap<String, TrainOnlineRecord>();
		if (records.size() > 0) {
			List<TrainOnlineRecord> list = new ArrayList(records);
			startTime = list.get(0).getStartTime();
			endTime = list.get(list.size() - 1).getEndTime();
			for (int i = 0; i < list.size(); i++) {
				TrainOnlineRecord record = list.get(i);
				if (record.getSyncStatus() == 1)
					map.put(record.getLeafId(), record);
			}
		}
		studyCount = map.size();

		// 应完成学习的节点数
		int allCount = ((TrainOnlineService) service).getCourseCount(courseId);
		request.setAttribute("allCount", allCount);
		request.setAttribute("startTime", startTime);
		request.setAttribute("endTime", endTime);
		request.setAttribute("studyCount", studyCount);

		// 课前评测情况
		Map<String, String> param1 = new HashMap<String, String>();
		param1.put("relationId", online.getTrainImplement().getId());
		param1.put("relationType", "TEST_BEFORE");
		param1.put("employeeId", LoginAction.getUserSessionInfo()
				.getEmployeeId());
		Map<String, Boolean> sortMap = new HashMap<String, Boolean>();
		sortMap.put("examScore", true);// 按得分倒序排列，取最好成绩
		List<ExamTestpaper> beforeList = service.queryData("queryExamRecord",
				param1, sortMap);
		if (beforeList != null && beforeList.size() > 0) {
			ExamTestpaper before = beforeList.get(0);
			request.setAttribute("before", before);
		}

		// 课后考试情况
		Map<String, String> param2 = new HashMap<String, String>();
		param2.put("relationId", online.getTrainImplement().getId());
		param2.put("relationType", "TEST_AFTER");
		param2.put("employeeId", LoginAction.getUserSessionInfo()
				.getEmployeeId());
		List<ExamTestpaper> afterList = service.queryData("queryExamRecord",
				param2, sortMap);
		if (afterList != null && afterList.size() > 0) {
			ExamTestpaper after = afterList.get(0);
			request.setAttribute("after", after);

			// 查询合格分数线
			Exam exam = (Exam) service.findDataByKey(after.getExamId(),
					Exam.class);
			request.setAttribute("afterPassScore", exam.getPassScore());
		}

		return "showCourseInfo";
	}

	/**
	 * 显示学习页面
	 * 
	 * @return
	 * @throws Exception
	 */
	public String showStudy() throws Exception {
		// 查询课程信息
		TrainOnline online = (TrainOnline) service.findDataByKey(courseId,
				TrainOnline.class);
		// 判断是否进行了课前评测
		if (online.getTestBeforeStudies().size() == 0)
			request.setAttribute("canStudy", 0);
		else
			request.setAttribute("canStudy", 1);
		return "showStudy";
	}

	/**
	 * 课前评测
	 * 
	 * @return
	 * @throws Exception
	 */
	public String testBefore() throws Exception {
		UserSession session = LoginAction.getUserSessionInfo();
		try {
			// 查询课程信息
			TrainOnline online = (TrainOnline) service.findDataByKey(courseId,
					TrainOnline.class);
			String testPaperId = trainImplementMoniService
					.addOrGetExamPaperByImplId(online.getTrainImplement()
							.getId(), "TEST_BEFORE", session.getEmployeeId(),
							null, null, session.getEmployeeName(), false,null);
			// 判断当前试卷是否已评卷，已评卷则产生新的试卷，未评卷则清除答案
			ExamUserTestpaper paper = (ExamUserTestpaper) trainImplementMoniService
					.findDataByKey(testPaperId, ExamUserTestpaper.class);
			if (StringUtils.isEmpty(paper.getExamTestpaper().getSubTime())) {
				this.setClean(1);
			} else {
				this.setAddExam(1);
				this.setAddExamType(20);
			}
			this.setUserTestPaperId(testPaperId);
			this.setIsHidden(1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "test";
	}

	/**
	 * 课后考试
	 * 
	 * @return
	 * @throws Exception
	 */
	public String testAfter() throws Exception {
		UserSession session = LoginAction.getUserSessionInfo();
		try {
			// 查询课程信息
			TrainOnline online = (TrainOnline) service.findDataByKey(courseId,
					TrainOnline.class);
			String testPaperId = trainImplementMoniService
					.addOrGetExamPaperByImplId(online.getTrainImplement()
							.getId(), "TEST_AFTER", session.getEmployeeId(),
							null, null, session.getEmployeeName(), false,null);
			// 判断当前试卷是否已评卷，已评卷则产生新的试卷，未评卷则清除答案
			ExamUserTestpaper paper = (ExamUserTestpaper) trainImplementMoniService
					.findDataByKey(testPaperId, ExamUserTestpaper.class);
			if (StringUtils.isEmpty(paper.getExamTestpaper().getSubTime())) {
				this.setClean(1);
			} else {
				this.setAddExam(1);
				this.setAddExamType(20);
			}
			this.setUserTestPaperId(testPaperId);
			this.setIsHidden(1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "test";
	}

	/**
	 * 查询课程对应的课件信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public String findEmployeeCourseTree() throws Exception {
		courseAcces = ((TrainOnlineService) service).findCourseTree(courseId);
		return "findEmployeeCourseTree";
	}

	/**
	 * 查询应先学习完成的课程
	 * 
	 * @return
	 * @throws Exception
	 */
	public String findPreCourse() throws Exception {
		preCourse = ((TrainOnlineService) service).findPreCourseLeaf(courseId,
				currentLeafId);
		return "findPreCourse";
	}

	/**
	 * 查询学习记录
	 * 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String findStudyRecord() throws Exception {
		TrainOnline online = (TrainOnline) service.findDataByKey(courseId,
				TrainOnline.class);
		// 学习记录统计
		List<StudyLeaf> list = ((TrainOnlineService) service)
				.findStudyRecords(courseId);
		request.setAttribute("recordList", list);

		// 课前评测情况
		Map<String, String> param1 = new HashMap<String, String>();
		param1.put("relationId", online.getTrainImplement().getId());
		param1.put("relationType", "TEST_BEFORE");
		param1.put("employeeId", LoginAction.getUserSessionInfo()
				.getEmployeeId());
		Map<String, Boolean> sortMap = new HashMap<String, Boolean>();
		sortMap.put("creationDate", false);
		// List<ExamTestpaper> beforeList = exampaperMoniService.queryData(
		// "queryExamTestpaperInUseEndHql", param1, sortMap);
		List<ExamTestpaper> beforeList = service.queryData("queryExamRecord",
				param1, sortMap);
		request.setAttribute("beforeList", beforeList);

		// 课后考试情况
		Map<String, String> param2 = new HashMap<String, String>();
		param2.put("relationId", online.getTrainImplement().getId());
		param2.put("relationType", "TEST_AFTER");
		param2.put("employeeId", LoginAction.getUserSessionInfo()
				.getEmployeeId());
		// List<ExamTestpaper> afterList = exampaperMoniService.queryData(
		// "queryExamTestpaperInUseEndHql", param2, sortMap);
		List<ExamTestpaper> afterList = service.queryData("queryExamRecord",
				param2, sortMap);
		request.setAttribute("afterList", afterList);

		// 查询合格分数线
		if (beforeList != null && beforeList.size() > 0) {
			Exam exam = (Exam) service.findDataByKey(beforeList.get(0)
					.getExamId(), Exam.class);
			request.setAttribute("passScore", exam.getPassScore());
		} else if (afterList != null && afterList.size() > 0) {
			Exam exam = (Exam) service.findDataByKey(afterList.get(0)
					.getExamId(), Exam.class);
			request.setAttribute("passScore", exam.getPassScore());
		} else {
			request.setAttribute("passScore", 60);
		}

		return "findStudyRecord";
	}

	/**
	 * 查询附件转换信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public String findAccessory() throws Exception {
		accessory = (AccessoryFileTransform) service.findDataByKey(this
				.getAcceId(), AccessoryFileTransform.class);
		return "findAccessory";
	}

	public String study() throws Exception {
		return "study";
	}

	public List<TrainOnline> getList() {
		return list;
	}

	public void setList(List<TrainOnline> list) {
		this.list = list;
	}

	public String getStudentTerm() {
		return studentTerm;
	}

	public void setStudentTerm(String studentTerm) {
		this.studentTerm = studentTerm;
	}

	public String getStartDateTerm() {
		return startDateTerm;
	}

	public void setStartDateTerm(String startDateTerm) {
		this.startDateTerm = startDateTerm;
	}

	public String getEndDateTerm() {
		return endDateTerm;
	}

	public void setEndDateTerm(String endDateTerm) {
		this.endDateTerm = endDateTerm;
	}

	public void setTrainImplementService(
			TrainImplementService trainImplementService) {
		this.trainImplementService = trainImplementService;
	}

	public List<TreeNode> getCourseAcces() {
		return courseAcces;
	}

	public void setCourseAcces(List<TreeNode> courseAcces) {
		this.courseAcces = courseAcces;
	}

	public String getCourseId() {
		return courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}

	public String getCurrentLeafId() {
		return currentLeafId;
	}

	public void setCurrentLeafId(String currentLeafId) {
		this.currentLeafId = currentLeafId;
	}

	public List<StudyLeaf> getPreCourse() {
		return preCourse;
	}

	public void setPreCourse(List<StudyLeaf> preCourse) {
		this.preCourse = preCourse;
	}

	public String getTrainId() {
		return trainId;
	}

	public void setTrainId(String trainId) {
		this.trainId = trainId;
	}

	public int getIsHidden() {
		return isHidden;
	}

	public void setIsHidden(int isHidden) {
		this.isHidden = isHidden;
	}

	public String getUserTestPaperId() {
		return userTestPaperId;
	}

	public void setUserTestPaperId(String userTestPaperId) {
		this.userTestPaperId = userTestPaperId;
	}

	public AccessoryFileTransform getAccessory() {
		return accessory;
	}

	public void setAccessory(AccessoryFileTransform accessory) {
		this.accessory = accessory;
	}

	public String getAcceId() {
		return acceId;
	}

	public void setAcceId(String acceId) {
		this.acceId = acceId;
	}

	public int getAddExam() {
		return addExam;
	}

	public void setAddExam(int addExam) {
		this.addExam = addExam;
	}

	public int getAddExamType() {
		return addExamType;
	}

	public void setAddExamType(int addExamType) {
		this.addExamType = addExamType;
	}

	public int getClean() {
		return clean;
	}

	public void setClean(int clean) {
		this.clean = clean;
	}

	public TrainImplementMoniService getTrainImplementMoniService() {
		return trainImplementMoniService;
	}

	public void setTrainImplementMoniService(
			TrainImplementMoniService trainImplementMoniService) {
		this.trainImplementMoniService = trainImplementMoniService;
	}
	
	

}
