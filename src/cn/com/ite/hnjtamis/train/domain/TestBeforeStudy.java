package cn.com.ite.hnjtamis.train.domain;

import cn.com.ite.hnjtamis.exam.hibernatemap.ExamTestpaper;

/**
 * TestBeforeStudy entity. @author MyEclipse Persistence Tools
 */

public class TestBeforeStudy implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 859028724831285810L;
	private String id;
	private ExamTestpaper examTestpaper;
	private TrainOnline trainOnline;
	private String studentId;
	private Double score;
	private String startTime;
	private String endTime;
	private Integer isDel;
	private Integer syncStatus;
	private String lastUpdateDate;
	private String lastUpdatedBy;
	private String creationDate;
	private String createdBy;
	private String organId;

	// Constructors

	/** default constructor */
	public TestBeforeStudy() {
	}

	/** full constructor */
	public TestBeforeStudy(ExamTestpaper examTestpaper,
			TrainOnline trainOnline, String studentId, Double score,
			Integer isDel, Integer syncStatus, String lastUpdateDate,
			String lastUpdatedBy, String creationDate, String createdBy,
			String organId) {
		this.examTestpaper = examTestpaper;
		this.trainOnline = trainOnline;
		this.studentId = studentId;
		this.score = score;
		this.isDel = isDel;
		this.syncStatus = syncStatus;
		this.lastUpdateDate = lastUpdateDate;
		this.lastUpdatedBy = lastUpdatedBy;
		this.creationDate = creationDate;
		this.createdBy = createdBy;
		this.organId = organId;
	}

	// Property accessors

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ExamTestpaper getExamTestpaper() {
		return this.examTestpaper;
	}

	public void setExamTestpaper(ExamTestpaper examTestpaper) {
		this.examTestpaper = examTestpaper;
	}

	public TrainOnline getTrainOnline() {
		return this.trainOnline;
	}

	public void setTrainOnline(TrainOnline trainOnline) {
		this.trainOnline = trainOnline;
	}

	public String getStudentId() {
		return this.studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public Double getScore() {
		return this.score;
	}

	public void setScore(Double score) {
		this.score = score;
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

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

}