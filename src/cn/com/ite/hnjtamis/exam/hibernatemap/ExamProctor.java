package cn.com.ite.hnjtamis.exam.hibernatemap;

import java.util.HashSet;
import java.util.Set;

/**
 * ExamProctor entity. @author MyEclipse Persistence Tools
 */

public class ExamProctor implements java.io.Serializable {

	// Fields

	private String examProctorId;
	private ExamExamroot examExamroot;
	private String proctorName;
	private String proctorId;
	private String inTime;
	private String outTime;
	private Integer isMain;
	private String organName;
	private String organId;
	private String syncFlag;
	private String lastUpdateDate;
	private String lastUpdatedBy;
	private String lastUpdatedIdBy;
	private String creationDate;
	private String createdBy;
	private String createdIdBy;
	private Set examProctorDeducts = new HashSet(0);

	// Constructors

	/** default constructor */
	public ExamProctor() {
	}

	/** full constructor */
	public ExamProctor(ExamExamroot examExamroot, String proctorName,
			String proctorId, String inTime, String outTime, Integer isMain,
			String organName, String organId, String syncFlag,
			String lastUpdateDate, String lastUpdatedBy,
			String lastUpdatedIdBy, String creationDate, String createdBy,
			String createdIdBy, Set examProctorDeducts) {
		this.examExamroot = examExamroot;
		this.proctorName = proctorName;
		this.proctorId = proctorId;
		this.inTime = inTime;
		this.outTime = outTime;
		this.isMain = isMain;
		this.organName = organName;
		this.organId = organId;
		this.syncFlag = syncFlag;
		this.lastUpdateDate = lastUpdateDate;
		this.lastUpdatedBy = lastUpdatedBy;
		this.lastUpdatedIdBy = lastUpdatedIdBy;
		this.creationDate = creationDate;
		this.createdBy = createdBy;
		this.createdIdBy = createdIdBy;
		this.examProctorDeducts = examProctorDeducts;
	}

	// Property accessors

	public String getExamProctorId() {
		return this.examProctorId;
	}

	public void setExamProctorId(String examProctorId) {
		this.examProctorId = examProctorId;
	}

	public ExamExamroot getExamExamroot() {
		return this.examExamroot;
	}

	public void setExamExamroot(ExamExamroot examExamroot) {
		this.examExamroot = examExamroot;
	}

	public String getProctorName() {
		return this.proctorName;
	}

	public void setProctorName(String proctorName) {
		this.proctorName = proctorName;
	}

	public String getProctorId() {
		return this.proctorId;
	}

	public void setProctorId(String proctorId) {
		this.proctorId = proctorId;
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

	public Integer getIsMain() {
		return this.isMain;
	}

	public void setIsMain(Integer isMain) {
		this.isMain = isMain;
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

	public Set getExamProctorDeducts() {
		return this.examProctorDeducts;
	}

	public void setExamProctorDeducts(Set examProctorDeducts) {
		this.examProctorDeducts = examProctorDeducts;
	}

}