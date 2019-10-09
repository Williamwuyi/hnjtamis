package cn.com.ite.hnjtamis.exam.hibernatemap;

/**
 * StudyTestpaperHis entity. @author MyEclipse Persistence Tools
 */

public class StudyTestpaperHis implements java.io.Serializable {

	// Fields

	private String testpaperHisId;
	private String studyTestpaperName;
	private Integer totalTheme;
	private Double totalScore;
	private Integer rightTheme;
	private Double rightScore;
	private String employeeId;
	private String employeename;
	private String isPass;
	private Integer isUse;
	private String remark;
	private String state;
	private String organName;
	private String organId;
	private String syncFlag;
	private String lastUpdateDate;
	private String lastUpdatedBy;
	private String lastUpdatedIdBy;
	private String creationDate;
	private String createdBy;
	private String createdIdBy;
	private String relationId;
	private String relationType;
	private String batchNo;
	private String inTime;
	private String outTime;
	private String subTime;

	// Constructors

	/** default constructor */
	public StudyTestpaperHis() {
	}

	/** full constructor */
	public StudyTestpaperHis(String studyTestpaperName, Integer totalTheme,
			Double totalScore, Integer rightTheme, Double rightScore,
			String employeeId, String employeename, String isPass, Integer isUse,
			String remark, String state, String organName, String organId,
			String syncFlag, String lastUpdateDate, String lastUpdatedBy,
			String lastUpdatedIdBy, String creationDate, String createdBy,
			String createdIdBy, String relationId, String relationType,
			String batchNo, String inTime, String outTime, String subTime) {
		this.studyTestpaperName = studyTestpaperName;
		this.totalTheme = totalTheme;
		this.totalScore = totalScore;
		this.rightTheme = rightTheme;
		this.rightScore = rightScore;
		this.employeeId = employeeId;
		this.employeename = employeename;
		this.isPass = isPass;
		this.isUse = isUse;
		this.remark = remark;
		this.state = state;
		this.organName = organName;
		this.organId = organId;
		this.syncFlag = syncFlag;
		this.lastUpdateDate = lastUpdateDate;
		this.lastUpdatedBy = lastUpdatedBy;
		this.lastUpdatedIdBy = lastUpdatedIdBy;
		this.creationDate = creationDate;
		this.createdBy = createdBy;
		this.createdIdBy = createdIdBy;
		this.relationId = relationId;
		this.relationType = relationType;
		this.batchNo = batchNo;
		this.inTime = inTime;
		this.outTime = outTime;
		this.subTime = subTime;
	}

	// Property accessors

	public String getTestpaperHisId() {
		return this.testpaperHisId;
	}

	public void setTestpaperHisId(String testpaperHisId) {
		this.testpaperHisId = testpaperHisId;
	}

	public String getStudyTestpaperName() {
		return this.studyTestpaperName;
	}

	public void setStudyTestpaperName(String studyTestpaperName) {
		this.studyTestpaperName = studyTestpaperName;
	}

	public Integer getTotalTheme() {
		return this.totalTheme;
	}

	public void setTotalTheme(Integer totalTheme) {
		this.totalTheme = totalTheme;
	}

	public Double getTotalScore() {
		return this.totalScore;
	}

	public void setTotalScore(Double totalScore) {
		this.totalScore = totalScore;
	}

	public Integer getRightTheme() {
		return this.rightTheme;
	}

	public void setRightTheme(Integer rightTheme) {
		this.rightTheme = rightTheme;
	}

	public Double getRightScore() {
		return this.rightScore;
	}

	public void setRightScore(Double rightScore) {
		this.rightScore = rightScore;
	}

	public String getEmployeeId() {
		return this.employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getEmployeename() {
		return this.employeename;
	}

	public void setEmployeename(String employeename) {
		this.employeename = employeename;
	}

	public String getIsPass() {
		return this.isPass;
	}

	public void setIsPass(String isPass) {
		this.isPass = isPass;
	}

	public Integer getIsUse() {
		return this.isUse;
	}

	public void setIsUse(Integer isUse) {
		this.isUse = isUse;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getState() {
		return this.state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getOrganName() {
		return this.organName;
	}

	public void setOrganName(String organName) {
		this.organName = organName;
	}

	public String getOrganId() {
		return this.organId;
	}

	public void setOrganId(String organId) {
		this.organId = organId;
	}

	public String getSyncFlag() {
		return this.syncFlag;
	}

	public void setSyncFlag(String syncFlag) {
		this.syncFlag = syncFlag;
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

	public String getLastUpdatedIdBy() {
		return this.lastUpdatedIdBy;
	}

	public void setLastUpdatedIdBy(String lastUpdatedIdBy) {
		this.lastUpdatedIdBy = lastUpdatedIdBy;
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

	public String getCreatedIdBy() {
		return this.createdIdBy;
	}

	public void setCreatedIdBy(String createdIdBy) {
		this.createdIdBy = createdIdBy;
	}

	public String getRelationId() {
		return this.relationId;
	}

	public void setRelationId(String relationId) {
		this.relationId = relationId;
	}

	public String getRelationType() {
		return this.relationType;
	}

	public void setRelationType(String relationType) {
		this.relationType = relationType;
	}

	public String getBatchNo() {
		return this.batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public String getInTime() {
		return this.inTime;
	}

	public void setInTime(String inTime) {
		this.inTime = inTime;
	}

	public String getOutTime() {
		return this.outTime;
	}

	public void setOutTime(String outTime) {
		this.outTime = outTime;
	}

	public String getSubTime() {
		return this.subTime;
	}

	public void setSubTime(String subTime) {
		this.subTime = subTime;
	}

}