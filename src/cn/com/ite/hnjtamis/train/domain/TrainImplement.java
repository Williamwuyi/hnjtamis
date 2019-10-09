package cn.com.ite.hnjtamis.train.domain;

import java.util.HashSet;
import java.util.Set;

import cn.com.ite.hnjtamis.exam.hibernatemap.ThemeBank;

/**
 * TrainImplement entity. @author MyEclipse Persistence Tools
 */

public class TrainImplement implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -8045285061459711828L;
	private String id;
	private MonthPlan monthPlan;
	private String subject;
	private Integer duration;
	private Integer credit;
	private String category;
	private String quarterType;
	private String timeRange;
	private String startDate;
	private String endDate;
	private String trainMode;
	private String place;
	private String teacher;
	private String trainManager;
	private String trainManagerName;
	private String examineManager;
	private String examineManagerName;
	private String examineMode;
	private String examineTime;
	private String examineResult;
	private Integer isAnnounced;
	private Integer isAudited;
	private String auditer;
	private String auditerName;
	private String auditTime;
	private String distributor;
	private String distributorName;
	private String distributeTime;
	private Integer finishStatus;
	private Integer isDel;
	private Integer syncStatus;
	private String lastUpdateDate;
	private String lastUpdatedBy;
	private String creationDate;
	private String createdBy;
	private String organId;
	private String remark;
	private String learningTarget;
	private Set<TrainImplementCourse> trainImplementCourses = new HashSet<TrainImplementCourse>(
			0);
	private Set<TrainOnline> trainOnlines = new HashSet<TrainOnline>(0);
	private Set<ThemeBank> themeBanks = new HashSet<ThemeBank>(0);
	

	// Constructors

	/** default constructor */
	public TrainImplement() {
	}

	/** full constructor */
	public TrainImplement(MonthPlan monthPlan, String subject,
			Integer duration, String category, String quarterType,
			String timeRange, String startDate, String endDate,
			String trainMode, String place, String teacher,
			String trainManager, String trainManagerName,
			String examineManager, String examineManagerName,
			String examineMode, String examineTime, String examineResult,
			Integer isAnnounced, Integer isAudited, String auditer,
			String auditerName, String auditTime, String distributor,
			String distributorName, String distributeTime,
			Integer finishStatus, Integer isDel, Integer syncStatus,
			String lastUpdateDate, String lastUpdatedBy, String creationDate,
			String createdBy, String organId, String remark,
			Set<TrainImplementCourse> trainImplementCourses,
			Set<TrainOnline> trainOnlines) {
		this.monthPlan = monthPlan;
		this.subject = subject;
		this.duration = duration;
		this.category = category;
		this.quarterType = quarterType;
		this.timeRange = timeRange;
		this.startDate = startDate;
		this.endDate = endDate;
		this.trainMode = trainMode;
		this.place = place;
		this.teacher = teacher;
		this.trainManager = trainManager;
		this.trainManagerName = trainManagerName;
		this.examineManager = examineManager;
		this.examineManagerName = examineManagerName;
		this.examineMode = examineMode;
		this.examineTime = examineTime;
		this.examineResult = examineResult;
		this.isAnnounced = isAnnounced;
		this.isAudited = isAudited;
		this.auditer = auditer;
		this.auditerName = auditerName;
		this.auditTime = auditTime;
		this.distributor = distributor;
		this.distributorName = distributorName;
		this.distributeTime = distributeTime;
		this.finishStatus = finishStatus;
		this.isDel = isDel;
		this.syncStatus = syncStatus;
		this.lastUpdateDate = lastUpdateDate;
		this.lastUpdatedBy = lastUpdatedBy;
		this.creationDate = creationDate;
		this.createdBy = createdBy;
		this.organId = organId;
		this.remark = remark;
		this.trainImplementCourses = trainImplementCourses;
		this.trainOnlines = trainOnlines;
	}

	// Property accessors

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public MonthPlan getMonthPlan() {
		return this.monthPlan;
	}

	public void setMonthPlan(MonthPlan monthPlan) {
		this.monthPlan = monthPlan;
	}

	public String getSubject() {
		return this.subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public Integer getDuration() {
		return this.duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public String getCategory() {
		return this.category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getQuarterType() {
		return this.quarterType;
	}

	public void setQuarterType(String quarterType) {
		this.quarterType = quarterType;
	}

	public String getTimeRange() {
		return this.timeRange;
	}

	public void setTimeRange(String timeRange) {
		this.timeRange = timeRange;
	}

	public String getStartDate() {
		return this.startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return this.endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getTrainMode() {
		return this.trainMode;
	}

	public void setTrainMode(String trainMode) {
		this.trainMode = trainMode;
	}

	public String getPlace() {
		return this.place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public String getTeacher() {
		return this.teacher;
	}

	public void setTeacher(String teacher) {
		this.teacher = teacher;
	}

	public String getTrainManager() {
		return this.trainManager;
	}

	public void setTrainManager(String trainManager) {
		this.trainManager = trainManager;
	}

	public String getTrainManagerName() {
		return this.trainManagerName;
	}

	public void setTrainManagerName(String trainManagerName) {
		this.trainManagerName = trainManagerName;
	}

	public String getExamineManager() {
		return this.examineManager;
	}

	public void setExamineManager(String examineManager) {
		this.examineManager = examineManager;
	}

	public String getExamineManagerName() {
		return this.examineManagerName;
	}

	public void setExamineManagerName(String examineManagerName) {
		this.examineManagerName = examineManagerName;
	}

	public String getExamineMode() {
		return this.examineMode;
	}

	public void setExamineMode(String examineMode) {
		this.examineMode = examineMode;
	}

	public String getExamineTime() {
		return this.examineTime;
	}

	public void setExamineTime(String examineTime) {
		this.examineTime = examineTime;
	}

	public String getExamineResult() {
		return this.examineResult;
	}

	public void setExamineResult(String examineResult) {
		this.examineResult = examineResult;
	}

	public Integer getIsAnnounced() {
		return this.isAnnounced;
	}

	public void setIsAnnounced(Integer isAnnounced) {
		this.isAnnounced = isAnnounced;
	}

	public Integer getIsAudited() {
		return this.isAudited;
	}

	public void setIsAudited(Integer isAudited) {
		this.isAudited = isAudited;
	}

	public String getAuditer() {
		return this.auditer;
	}

	public void setAuditer(String auditer) {
		this.auditer = auditer;
	}

	public String getAuditerName() {
		return this.auditerName;
	}

	public void setAuditerName(String auditerName) {
		this.auditerName = auditerName;
	}

	public String getAuditTime() {
		return this.auditTime;
	}

	public void setAuditTime(String auditTime) {
		this.auditTime = auditTime;
	}

	public String getDistributor() {
		return this.distributor;
	}

	public void setDistributor(String distributor) {
		this.distributor = distributor;
	}

	public String getDistributorName() {
		return this.distributorName;
	}

	public void setDistributorName(String distributorName) {
		this.distributorName = distributorName;
	}

	public String getDistributeTime() {
		return this.distributeTime;
	}

	public void setDistributeTime(String distributeTime) {
		this.distributeTime = distributeTime;
	}

	public Integer getFinishStatus() {
		return this.finishStatus;
	}

	public void setFinishStatus(Integer finishStatus) {
		this.finishStatus = finishStatus;
	}

	public Integer getIsDel() {
		return this.isDel;
	}

	public void setIsDel(Integer isDel) {
		this.isDel = isDel;
	}

	public Integer getSyncStatus() {
		return this.syncStatus;
	}

	public void setSyncStatus(Integer syncStatus) {
		this.syncStatus = syncStatus;
	}

	public String getLastUpdateDate() {
		return this.lastUpdateDate;
	}

	public void setLastUpdateDate(String lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	public String getLastUpdatedBy() {
		return this.lastUpdatedBy;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public String getCreationDate() {
		return this.creationDate;
	}

	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getOrganId() {
		return this.organId;
	}

	public void setOrganId(String organId) {
		this.organId = organId;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Set<TrainImplementCourse> getTrainImplementCourses() {
		return this.trainImplementCourses;
	}

	public void setTrainImplementCourses(
			Set<TrainImplementCourse> trainImplementCourses) {
		this.trainImplementCourses = trainImplementCourses;
	}

	public Set<TrainOnline> getTrainOnlines() {
		return trainOnlines;
	}

	public void setTrainOnlines(Set<TrainOnline> trainOnlines) {
		this.trainOnlines = trainOnlines;
	}

	public String getLearningTarget() {
		return learningTarget;
	}

	public void setLearningTarget(String learningTarget) {
		this.learningTarget = learningTarget;
	}

	public Integer getCredit() {
		return credit;
	}

	public void setCredit(Integer credit) {
		this.credit = credit;
	}

	public Set<ThemeBank> getThemeBanks() {
		return themeBanks;
	}

	public void setThemeBanks(Set<ThemeBank> themeBanks) {
		this.themeBanks = themeBanks;
	}

}