package cn.com.ite.hnjtamis.exam.hibernatemap;

/**
 * ExamProctorDeduct entity. @author MyEclipse Persistence Tools
 */

public class ExamProctorDeduct implements java.io.Serializable {

	// Fields

	private String proctorDeductId;
	private ExamUserTestpaper examUserTestpaper;
	private ExamProctor examProctor;
	private Integer reason;
	private String remark;
	private String deductTime;
	private Double deductScore;
	private String column8;
	private String column9;
	private String organName;
	private String organId;
	private String syncFlag;
	private String lastUpdateDate;
	private String lastUpdatedBy;
	private String lastUpdatedIdBy;
	private String creationDate;
	private String createdBy;
	private String createdIdBy;

	// Constructors

	/** default constructor */
	public ExamProctorDeduct() {
	}

	/** full constructor */
	public ExamProctorDeduct(ExamUserTestpaper examUserTestpaper,
			ExamProctor examProctor, Integer reason, String remark,
			String deductTime, Double deductScore, String column8,
			String column9, String organName, String organId, String syncFlag,
			String lastUpdateDate, String lastUpdatedBy,
			String lastUpdatedIdBy, String creationDate, String createdBy,
			String createdIdBy) {
		this.examUserTestpaper = examUserTestpaper;
		this.examProctor = examProctor;
		this.reason = reason;
		this.remark = remark;
		this.deductTime = deductTime;
		this.deductScore = deductScore;
		this.column8 = column8;
		this.column9 = column9;
		this.organName = organName;
		this.organId = organId;
		this.syncFlag = syncFlag;
		this.lastUpdateDate = lastUpdateDate;
		this.lastUpdatedBy = lastUpdatedBy;
		this.lastUpdatedIdBy = lastUpdatedIdBy;
		this.creationDate = creationDate;
		this.createdBy = createdBy;
		this.createdIdBy = createdIdBy;
	}

	// Property accessors

	public String getProctorDeductId() {
		return this.proctorDeductId;
	}

	public void setProctorDeductId(String proctorDeductId) {
		this.proctorDeductId = proctorDeductId;
	}

	public ExamUserTestpaper getExamUserTestpaper() {
		return this.examUserTestpaper;
	}

	public void setExamUserTestpaper(ExamUserTestpaper examUserTestpaper) {
		this.examUserTestpaper = examUserTestpaper;
	}

	public ExamProctor getExamProctor() {
		return this.examProctor;
	}

	public void setExamProctor(ExamProctor examProctor) {
		this.examProctor = examProctor;
	}

	public Integer getReason() {
		return this.reason;
	}

	public void setReason(Integer reason) {
		this.reason = reason;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getDeductTime() {
		return this.deductTime;
	}

	public void setDeductTime(String deductTime) {
		this.deductTime = deductTime;
	}

	public Double getDeductScore() {
		return this.deductScore;
	}

	public void setDeductScore(Double deductScore) {
		this.deductScore = deductScore;
	}

	public String getColumn8() {
		return this.column8;
	}

	public void setColumn8(String column8) {
		this.column8 = column8;
	}

	public String getColumn9() {
		return this.column9;
	}

	public void setColumn9(String column9) {
		this.column9 = column9;
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

}