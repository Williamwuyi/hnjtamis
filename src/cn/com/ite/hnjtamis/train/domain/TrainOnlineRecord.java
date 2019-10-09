package cn.com.ite.hnjtamis.train.domain;

/**
 * TrainOnlineRecord entity. @author MyEclipse Persistence Tools
 */

public class TrainOnlineRecord implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 5134176316067385102L;
	private String id;
	private TrainOnline trainOnline;
	private String leafId;
	private String startTime;
	private String endTime;
	private Integer duration;
	private Integer needDuration;
	private Integer isDel;
	private Integer syncStatus;
	private String lastUpdateDate;
	private String lastUpdatedBy;
	private String creationDate;
	private String createdBy;
	private String organId;
	private String remark;

	// Constructors

	/** default constructor */
	public TrainOnlineRecord() {
	}

	/** full constructor */
	public TrainOnlineRecord(TrainOnline trainOnline, String startTime,
			String endTime, Integer duration, Integer isDel,
			Integer syncStatus, String lastUpdateDate, String lastUpdatedBy,
			String creationDate, String createdBy, String organId, String remark) {
		this.trainOnline = trainOnline;
		this.startTime = startTime;
		this.endTime = endTime;
		this.duration = duration;
		this.isDel = isDel;
		this.syncStatus = syncStatus;
		this.lastUpdateDate = lastUpdateDate;
		this.lastUpdatedBy = lastUpdatedBy;
		this.creationDate = creationDate;
		this.createdBy = createdBy;
		this.organId = organId;
		this.remark = remark;
	}

	// Property accessors

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public TrainOnline getTrainOnline() {
		return this.trainOnline;
	}

	public void setTrainOnline(TrainOnline trainOnline) {
		this.trainOnline = trainOnline;
	}

	public String getStartTime() {
		return this.startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return this.endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public Integer getDuration() {
		return this.duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
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

	public String getLeafId() {
		return leafId;
	}

	public void setLeafId(String leafId) {
		this.leafId = leafId;
	}

	public Integer getNeedDuration() {
		return needDuration;
	}

	public void setNeedDuration(Integer needDuration) {
		this.needDuration = needDuration;
	}

}