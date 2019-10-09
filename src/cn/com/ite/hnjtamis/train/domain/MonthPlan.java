package cn.com.ite.hnjtamis.train.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cn.com.ite.eap2.domain.baseinfo.Accessory;
import cn.com.ite.eap2.domain.organization.Dept;
import cn.com.ite.hnjtamis.baseinfo.domain.Speciality;

/**
 * MonthPlan entity. @author MyEclipse Persistence Tools
 */

public class MonthPlan implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1130885791937977361L;
	private String id;
	//private Quarter quarter;
	private Speciality speciality;
	private Dept dept;
	private String deptName;
	private String title;
	private String month;
	private String category;
	private String startDate;
	private String endDate;
	private String planContent;
	private String trainMode;
	private String auditerId;
	private String auditerName;
	private String auditTime;
	private String distributorId;
	private String distributorName;
	private String distributeTime;
	private Integer isDel;
	private Integer status;
	private Integer isFinished;
	private Float percent;
	private String exeContent;
	private String exeDate;
	private Integer syncStatus;
	private String lastUpdateDate;
	private String lastUpdatedBy;
	private String creationDate;
	private String createdBy;
	private String organId;
	private String remark;
	private Set<TrainImplement> trainImplements = new HashSet<TrainImplement>(0);
	private Set<Accessory> accessories = new HashSet<Accessory>(0);
	private List<MonthPlanQuarter> quarterPlans = new ArrayList<MonthPlanQuarter>();

	// Constructors

	/** default constructor */
	public MonthPlan() {
	}

	/** full constructor */
	public MonthPlan(Speciality speciality, String title,
			String month, String category, String startDate, String endDate,
			String planContent, String trainMode, String auditerId,
			String auditerName, String auditTime, String distributorId,
			String distributorName, String distributeTime, Integer isDel,
			Integer status, Integer syncStatus, String lastUpdateDate,
			String lastUpdatedBy, String creationDate, String createdBy,
			String organId, String remark, Set<TrainImplement> trainImplements) {
		//this.quarter = quarter;
		this.speciality = speciality;
		this.title = title;
		this.month = month;
		this.category = category;
		this.startDate = startDate;
		this.endDate = endDate;
		this.planContent = planContent;
		this.trainMode = trainMode;
		this.auditerId = auditerId;
		this.auditerName = auditerName;
		this.auditTime = auditTime;
		this.distributorId = distributorId;
		this.distributorName = distributorName;
		this.distributeTime = distributeTime;
		this.isDel = isDel;
		this.status = status;
		this.syncStatus = syncStatus;
		this.lastUpdateDate = lastUpdateDate;
		this.lastUpdatedBy = lastUpdatedBy;
		this.creationDate = creationDate;
		this.createdBy = createdBy;
		this.organId = organId;
		this.remark = remark;
		this.trainImplements = trainImplements;
	}

	// Property accessors

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

//	public Quarter getQuarter() {
//		return this.quarter;
//	}
//
//	public void setQuarter(Quarter quarter) {
//		this.quarter = quarter;
//	}

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

	public String getMonth() {
		return this.month;
	}

	public void setMonth(String month) {
		this.month = month;
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

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Set<TrainImplement> getTrainImplements() {
		return this.trainImplements;
	}

	public void setTrainImplements(Set<TrainImplement> trainImplements) {
		this.trainImplements = trainImplements;
	}

	public Set<Accessory> getAccessories() {
		return accessories;
	}

	public void setAccessories(Set<Accessory> accessories) {
		this.accessories = accessories;
	}

	public Integer getIsFinished() {
		return isFinished;
	}

	public void setIsFinished(Integer isFinished) {
		this.isFinished = isFinished;
	}

	public Float getPercent() {
		return percent;
	}

	public void setPercent(Float percent) {
		this.percent = percent;
	}

	public String getExeContent() {
		return exeContent;
	}

	public void setExeContent(String exeContent) {
		this.exeContent = exeContent;
	}

	public String getExeDate() {
		return exeDate;
	}

	public void setExeDate(String exeDate) {
		this.exeDate = exeDate;
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

	public List<MonthPlanQuarter> getQuarterPlans() {
		return quarterPlans;
	}

	public void setQuarterPlans(List<MonthPlanQuarter> quarterPlans) {
		this.quarterPlans = quarterPlans;
	}

}