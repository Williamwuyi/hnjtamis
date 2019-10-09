package cn.com.ite.hnjtamis.kb.domain;

import cn.com.ite.eap2.domain.organization.Organ;
import cn.com.ite.eap2.domain.organization.Quarter;
import cn.com.ite.hnjtamis.baseinfo.domain.Speciality;

/**
 * CoursewareDistribute entity. @author MyEclipse Persistence Tools
 */

public class CoursewareDistribute implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -4586717210513737343L;
	private String id;
	private Quarter quarter;
	private Courseware courseware;
	private Speciality speciality;
	private Integer isRequired;
	private String distributorId;
	private String distributorName;
	private String distributeTime;
	private Integer isAudited;
	private String auditer;
	private String auditTime;
	private Integer isDel;
	private Integer syncStatus;
	private String lastUpdateDate;
	private String lastUpdatedBy;
	private String creationDate;
	private String createdBy;
	private Organ organ;
	@SuppressWarnings("unused")
	private String courseTitle;

	// Constructors

	/** default constructor */
	public CoursewareDistribute() {
	}

	/** full constructor */
	public CoursewareDistribute(Quarter quarter, Courseware courseware,
			Speciality speciality, Integer isRequired, String distributorId,
			String distributeTime, Integer isDel, Integer syncStatus,
			String lastUpdateDate, String lastUpdatedBy, String creationDate,
			String createdBy, Organ organ) {
		this.quarter = quarter;
		this.courseware = courseware;
		this.speciality = speciality;
		this.isRequired = isRequired;
		this.distributorId = distributorId;
		this.distributeTime = distributeTime;
		this.isDel = isDel;
		this.syncStatus = syncStatus;
		this.lastUpdateDate = lastUpdateDate;
		this.lastUpdatedBy = lastUpdatedBy;
		this.creationDate = creationDate;
		this.createdBy = createdBy;
		this.organ = organ;
	}

	// Property accessors

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Quarter getQuarter() {
		return this.quarter;
	}

	public void setQuarter(Quarter quarter) {
		this.quarter = quarter;
	}

	public Courseware getCourseware() {
		return this.courseware;
	}

	public void setCourseware(Courseware courseware) {
		this.courseware = courseware;
	}

	public Speciality getSpeciality() {
		return this.speciality;
	}

	public void setSpeciality(Speciality speciality) {
		this.speciality = speciality;
	}

	public Integer getIsRequired() {
		return this.isRequired;
	}

	public void setIsRequired(Integer isRequired) {
		this.isRequired = isRequired;
	}

	public String getDistributorId() {
		return this.distributorId;
	}

	public void setDistributorId(String distributorId) {
		this.distributorId = distributorId;
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

	public Organ getOrgan() {
		return this.organ;
	}

	public void setOrgan(Organ organ) {
		this.organ = organ;
	}

	public Integer getIsAudited() {
		return isAudited;
	}

	public void setIsAudited(Integer isAudited) {
		this.isAudited = isAudited;
	}

	public String getAuditer() {
		return auditer;
	}

	public void setAuditer(String auditer) {
		this.auditer = auditer;
	}

	public String getAuditTime() {
		return auditTime;
	}

	public void setAuditTime(String auditTime) {
		this.auditTime = auditTime;
	}

	public String getCourseTitle() {
		return this.courseware.getTitle();
	}

}