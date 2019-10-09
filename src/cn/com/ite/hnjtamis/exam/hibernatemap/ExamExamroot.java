package cn.com.ite.hnjtamis.exam.hibernatemap;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

/**
 * ExamExamroot entity. @author MyEclipse Persistence Tools
 */

public class ExamExamroot implements java.io.Serializable {

	// Fields

	private String examExamrootId;
	private ExamRoot examRoot;
	private Exam exam;
	private BigDecimal actualNum;
	private String examRootName;
	private Integer seatArrangement;
	private String organName;
	private String organId;
	private String syncFlag;
	private String lastUpdateDate;
	private String lastUpdatedBy;
	private String lastUpdatedIdBy;
	private String creationDate;
	private String createdBy;
	private String createdIdBy;
	private Set examUserTestpapers = new HashSet(0);
	private Set examProctors = new HashSet(0);

	// Constructors

	/** default constructor */
	public ExamExamroot() {
	}

	/** full constructor */
	public ExamExamroot(ExamRoot examRoot, Exam exam, BigDecimal actualNum,
			String examRootName, Integer seatArrangement, String organName,
			String organId, String syncFlag, String lastUpdateDate,
			String lastUpdatedBy, String lastUpdatedIdBy, String creationDate,
			String createdBy, String createdIdBy, Set examUserTestpapers,
			Set examProctors) {
		this.examRoot = examRoot;
		this.exam = exam;
		this.actualNum = actualNum;
		this.examRootName = examRootName;
		this.seatArrangement = seatArrangement;
		this.organName = organName;
		this.organId = organId;
		this.syncFlag = syncFlag;
		this.lastUpdateDate = lastUpdateDate;
		this.lastUpdatedBy = lastUpdatedBy;
		this.lastUpdatedIdBy = lastUpdatedIdBy;
		this.creationDate = creationDate;
		this.createdBy = createdBy;
		this.createdIdBy = createdIdBy;
		this.examUserTestpapers = examUserTestpapers;
		this.examProctors = examProctors;
	}

	// Property accessors

	public String getExamExamrootId() {
		return this.examExamrootId;
	}

	public void setExamExamrootId(String examExamrootId) {
		this.examExamrootId = examExamrootId;
	}

	public ExamRoot getExamRoot() {
		return this.examRoot;
	}

	public void setExamRoot(ExamRoot examRoot) {
		this.examRoot = examRoot;
	}

	public Exam getExam() {
		return this.exam;
	}

	public void setExam(Exam exam) {
		this.exam = exam;
	}

	public BigDecimal getActualNum() {
		return this.actualNum;
	}

	public void setActualNum(BigDecimal actualNum) {
		this.actualNum = actualNum;
	}

	public String getExamRootName() {
		return this.examRootName;
	}

	public void setExamRootName(String examRootName) {
		this.examRootName = examRootName;
	}

	public Integer getSeatArrangement() {
		return this.seatArrangement;
	}

	public void setSeatArrangement(Integer seatArrangement) {
		this.seatArrangement = seatArrangement;
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

	public Set getExamUserTestpapers() {
		return this.examUserTestpapers;
	}

	public void setExamUserTestpapers(Set examUserTestpapers) {
		this.examUserTestpapers = examUserTestpapers;
	}

	public Set getExamProctors() {
		return this.examProctors;
	}

	public void setExamProctors(Set examProctors) {
		this.examProctors = examProctors;
	}

}