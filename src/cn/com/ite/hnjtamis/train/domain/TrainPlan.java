package cn.com.ite.hnjtamis.train.domain;

import java.util.HashSet;
import java.util.Set;

import cn.com.ite.eap2.domain.baseinfo.Accessory;
import cn.com.ite.eap2.domain.organization.Dept;
import cn.com.ite.eap2.domain.organization.Quarter;
import cn.com.ite.hnjtamis.baseinfo.domain.Speciality;

/**
 * TrainPlan entity. @author MyEclipse Persistence Tools
 */

public class TrainPlan implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -7546968203266200459L;
	private String id;
	private Quarter quarter;
	private Speciality speciality;
	private Dept dept;
	private String deptName;
	private String title;
	private Integer year;
	private String category;
	private String startDate;
	private String endDate;
	private String planContent;
	private String trainMode;
	private String trainContent;
	private Integer isAnnounced;
	private Integer isAudited;
	private String auditerId;
	private String auditerName;
	private String auditTime;
	private String distributorId;
	private String distributorName;
	private String distributeTime;
	private Integer isDel;
	private Integer syncStatus;
	private String lastUpdateDate;
	private String lastUpdatedBy;
	private String creationDate;
	private String createdBy;
	private String organId;
	private String remark;
	private Set<Accessory> accessories = new HashSet<Accessory>(0);

	// Constructors

	/** default constructor */
	public TrainPlan() {
	}

	/** full constructor */
	public TrainPlan(Quarter quarter, Speciality speciality, String title,
			Integer year, String category, String startDate, String endDate,
			String planContent, String trainMode, String trainContent,
			Integer isAnnounced, Integer isAudited, String auditerId,
			String auditerName, String auditTime, String distributorId,
			String distributorName, String distributeTime, Integer isDel,
			Integer syncStatus, String lastUpdateDate, String lastUpdatedBy,
			String creationDate, String createdBy, String organId,
			String remark,
			Set<Accessory> accessories) {
		this.quarter = quarter;
		this.speciality = speciality;
		this.title = title;
		this.year = year;
		this.category = category;
		this.startDate = startDate;
		this.endDate = endDate;
		this.planContent = planContent;
		this.trainMode = trainMode;
		this.trainContent = trainContent;
		this.isAnnounced = isAnnounced;
		this.isAudited = isAudited;
		this.auditerId = auditerId;
		this.auditerName = auditerName;
		this.auditTime = auditTime;
		this.distributorId = distributorId;
		this.distributorName = distributorName;
		this.distributeTime = distributeTime;
		this.isDel = isDel;
		this.syncStatus = syncStatus;
		this.lastUpdateDate = lastUpdateDate;
		this.lastUpdatedBy = lastUpdatedBy;
		this.creationDate = creationDate;
		this.createdBy = createdBy;
		this.organId = organId;
		this.remark = remark;
		this.accessories = accessories;
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

	public Speciality getSpeciality() {
		return this.speciality;
	}

	public void setSpeciality(Speciality speciality) {
		this.speciality = speciality;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getYear() {
		return this.year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public String getCategory() {
		return this.category;
	}

	public void setCategory(String category) {
		this.category = category;
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

	public String getPlanContent() {
		return this.planContent;
	}

	public void setPlanContent(String planContent) {
		this.planContent = planContent;
	}

	public String getTrainMode() {
		return this.trainMode;
	}

	public void setTrainMode(String trainMode) {
		this.trainMode = trainMode;
	}

	public String getTrainContent() {
		return this.trainContent;
	}

	public void setTrainContent(String trainContent) {
		this.trainContent = trainContent;
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

	public String getAuditerId() {
		return this.auditerId;
	}

	public void setAuditerId(String auditerId) {
		this.auditerId = auditerId;
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

	public Set<Accessory> getAccessories() {
		return this.accessories;
	}

	public void setAccessories(Set<Accessory> accessories) {
		this.accessories = accessories;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public Dept getDept() {
		return dept;
	}

	public void setDept(Dept dept) {
		this.dept = dept;
	}

}