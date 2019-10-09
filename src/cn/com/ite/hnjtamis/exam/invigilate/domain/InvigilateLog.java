package cn.com.ite.hnjtamis.exam.invigilate.domain;

import cn.com.ite.hnjtamis.exam.hibernatemap.Exam;

/**
 * InvigilateLog entity. @author MyEclipse Persistence Tools
 */

public class InvigilateLog implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 2016171470521162640L;
	private String id;
	private Exam exam;
	private Integer planNum;
	private Integer factNum;
	private Integer cheatNum;
	private String examInfo;
	private String userId;
	private String userName;
	private Integer status;
	private Integer syncStatus;
	private String lastUpdateDate;
	private String lastUpdatedBy;
	private String creationDate;
	private String createdBy;
	private String organId;

	// Constructors

	/** default constructor */
	public InvigilateLog() {
	}

	/** full constructor */
	public InvigilateLog(Exam exam, Integer planNum, Integer factNum,
			Integer cheatNum, String examInfo, String userId, String userName,
			Integer status, Integer syncStatus, String lastUpdateDate,
			String lastUpdatedBy, String creationDate, String createdBy,
			String organId) {
		this.exam = exam;
		this.planNum = planNum;
		this.factNum = factNum;
		this.cheatNum = cheatNum;
		this.examInfo = examInfo;
		this.userId = userId;
		this.userName = userName;
		this.status = status;
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

	public Exam getExam() {
		return this.exam;
	}

	public void setExam(Exam exam) {
		this.exam = exam;
	}

	public Integer getPlanNum() {
		return this.planNum;
	}

	public void setPlanNum(Integer planNum) {
		this.planNum = planNum;
	}

	public Integer getFactNum() {
		return this.factNum;
	}

	public void setFactNum(Integer factNum) {
		this.factNum = factNum;
	}

	public Integer getCheatNum() {
		return this.cheatNum;
	}

	public void setCheatNum(Integer cheatNum) {
		this.cheatNum = cheatNum;
	}

	public String getExamInfo() {
		return this.examInfo;
	}

	public void setExamInfo(String examInfo) {
		this.examInfo = examInfo;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
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

}