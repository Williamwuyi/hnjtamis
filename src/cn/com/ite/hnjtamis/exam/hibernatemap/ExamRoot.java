package cn.com.ite.hnjtamis.exam.hibernatemap;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

/**
 * ExamRoot entity. @author MyEclipse Persistence Tools
 */

public class ExamRoot implements java.io.Serializable {

	// Fields

	private String examRootId;
	private String examRootCode;
	private String examRootName;
	private String examRootPlace;
	private Integer sortNum;
	private String remark;
	private BigDecimal maxUserNum;
	private String loginUrl;
	private String syncFlag;
	private String organName;
	private String organId;
	private String lastUpdateDate;
	private String lastUpdatedBy;
	private String lastUpdatedIdBy;
	private String creationDate;
	private String createdBy;
	private String createdIdBy;
	private Set examExamroots = new HashSet(0);

	// Constructors

	/** default constructor */
	public ExamRoot() {
	}

	/** full constructor */
	public ExamRoot(String examRootCode, String examRootName,
			String examRootPlace, Integer sortNum, String remark,
			BigDecimal maxUserNum, String loginUrl, String syncFlag,
			String organName, String organId, String lastUpdateDate,
			String lastUpdatedBy, String lastUpdatedIdBy, String creationDate,
			String createdBy, String createdIdBy, Set examExamroots) {
		this.examRootCode = examRootCode;
		this.examRootName = examRootName;
		this.examRootPlace = examRootPlace;
		this.sortNum = sortNum;
		this.remark = remark;
		this.maxUserNum = maxUserNum;
		this.loginUrl = loginUrl;
		this.syncFlag = syncFlag;
		this.organName = organName;
		this.organId = organId;
		this.lastUpdateDate = lastUpdateDate;
		this.lastUpdatedBy = lastUpdatedBy;
		this.lastUpdatedIdBy = lastUpdatedIdBy;
		this.creationDate = creationDate;
		this.createdBy = createdBy;
		this.createdIdBy = createdIdBy;
		this.examExamroots = examExamroots;
	}

	// Property accessors

	public String getExamRootId() {
		return this.examRootId;
	}

	public void setExamRootId(String examRootId) {
		this.examRootId = examRootId;
	}

	public String getExamRootCode() {
		return this.examRootCode;
	}

	public void setExamRootCode(String examRootCode) {
		this.examRootCode = examRootCode;
	}

	public String getExamRootName() {
		return this.examRootName;
	}

	public void setExamRootName(String examRootName) {
		this.examRootName = examRootName;
	}

	public String getExamRootPlace() {
		return this.examRootPlace;
	}

	public void setExamRootPlace(String examRootPlace) {
		this.examRootPlace = examRootPlace;
	}

	public Integer getSortNum() {
		return this.sortNum;
	}

	public void setSortNum(Integer sortNum) {
		this.sortNum = sortNum;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public BigDecimal getMaxUserNum() {
		return this.maxUserNum;
	}

	public void setMaxUserNum(BigDecimal maxUserNum) {
		this.maxUserNum = maxUserNum;
	}

	public String getLoginUrl() {
		return this.loginUrl;
	}

	public void setLoginUrl(String loginUrl) {
		this.loginUrl = loginUrl;
	}

	public String getSyncFlag() {
		return this.syncFlag;
	}

	public void setSyncFlag(String syncFlag) {
		this.syncFlag = syncFlag;
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

	public Set getExamExamroots() {
		return this.examExamroots;
	}

	public void setExamExamroots(Set examExamroots) {
		this.examExamroots = examExamroots;
	}

}