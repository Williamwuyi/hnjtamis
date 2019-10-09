package cn.com.ite.hnjtamis.train.domain;

import java.util.HashSet;
import java.util.Set;

/**
 * TrainOnline entity. @author MyEclipse Persistence Tools
 */

public class TrainOnline implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -3197051668492476392L;
	private String id;
	private TrainImplement trainImplement;
	private String studentId;
	private String studentName;
	private Integer duration;
	private Integer status;
	private Integer isDel;
	private Integer syncStatus;
	private String lastUpdateDate;
	private String lastUpdatedBy;
	private String creationDate;
	private String createdBy;
	private String organId;
	private String remark;
	private Set<TrainOnlineRecord> trainOnlineRecords = new HashSet<TrainOnlineRecord>(
			0);
	private Set<TestBeforeStudy> testBeforeStudies = new HashSet<TestBeforeStudy>(
			0);
	private Set<TestAfterStudy> testAfterStudies = new HashSet<TestAfterStudy>(
			0);

	// Constructors

	/** default constructor */
	public TrainOnline() {
	}

	/** full constructor */
	public TrainOnline(TrainImplement trainImplement, String studentId,
			String studentName, Integer duration, Integer status,
			Integer isDel, Integer syncStatus, String lastUpdateDate,
			String lastUpdatedBy, String creationDate, String createdBy,
			String organId, String remark,
			Set<TrainOnlineRecord> trainOnlineRecords,
			Set<TestBeforeStudy> testBeforeStudies,
			Set<TestAfterStudy> testAfterStudies) {
		this.trainImplement = trainImplement;
		this.studentId = studentId;
		this.studentName = studentName;
		this.duration = duration;
		this.status = status;
		this.isDel = isDel;
		this.syncStatus = syncStatus;
		this.lastUpdateDate = lastUpdateDate;
		this.lastUpdatedBy = lastUpdatedBy;
		this.creationDate = creationDate;
		this.createdBy = createdBy;
		this.organId = organId;
		this.remark = remark;
		this.trainOnlineRecords = trainOnlineRecords;
		this.testBeforeStudies = testBeforeStudies;
		this.testAfterStudies = testAfterStudies;
	}

	// Property accessors

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public TrainImplement getTrainImplement() {
		return this.trainImplement;
	}

	public void setTrainImplement(TrainImplement trainImplement) {
		this.trainImplement = trainImplement;
	}

	public String getStudentId() {
		return this.studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public String getStudentName() {
		return this.studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public Integer getDuration() {
		return this.duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
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

	public Set<TrainOnlineRecord> getTrainOnlineRecords() {
		return this.trainOnlineRecords;
	}

	public void setTrainOnlineRecords(Set<TrainOnlineRecord> trainOnlineRecords) {
		this.trainOnlineRecords = trainOnlineRecords;
	}

	public Set<TestBeforeStudy> getTestBeforeStudies() {
		return this.testBeforeStudies;
	}

	public void setTestBeforeStudies(Set<TestBeforeStudy> testBeforeStudies) {
		this.testBeforeStudies = testBeforeStudies;
	}

	public Set<TestAfterStudy> getTestAfterStudies() {
		return this.testAfterStudies;
	}

	public void setTestAfterStudies(Set<TestAfterStudy> testAfterStudies) {
		this.testAfterStudies = testAfterStudies;
	}

}