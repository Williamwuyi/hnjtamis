package cn.com.ite.hnjtamis.kb.domain;

import java.util.HashSet;
import java.util.Set;

import cn.com.ite.eap2.domain.baseinfo.Accessory;

/**
 * SharedLib entity. @author MyEclipse Persistence Tools
 */

public class SharedLib implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -8345852680989936929L;
	private String id;
	private CustomFolder customFolder;
	private String title;
	private String description;
	private String specialtyId;
	private String quarterId;
	private Integer isAnnounced;
	private Integer isAudited;
	private String auditer;
	private String auditTime;
	private Integer isDistributeVersion;
	private String originalOrganId;
	private String distributor;
	private String distributeTime;
	private String uploader;
	private String uploaderName;
	private String uploadTime;
	private String content;
	private String fileName;
	private Integer isDisable;
	private Integer isDel;
	private Integer syncStatus;
	private String lastUpdateDate;
	private String lastUpdatedBy;
	private String creationDate;
	private String createdBy;
	private String organId;
	private String remark;
	/**
	 * 附件
	 */
	private Set<Accessory> accessories = new HashSet<Accessory>();

	// Constructors

	/** default constructor */
	public SharedLib() {
	}

	/** full constructor */
	public SharedLib(CustomFolder customFolder, String title,
			String description, String specialtyId, String quarterId,
			Integer isAnnounced, Integer isAudited, String auditer,
			String auditTime, Integer isDistributeVersion,
			String originalOrganId, String distributor, String distributeTime,
			String uploader, String uploaderName, String uploadTime,
			String content, String fileName, Integer isDisable, Integer isDel,
			Integer syncStatus, String lastUpdateDate, String lastUpdatedBy,
			String creationDate, String createdBy, String organId, String remark) {
		this.customFolder = customFolder;
		this.title = title;
		this.description = description;
		this.specialtyId = specialtyId;
		this.quarterId = quarterId;
		this.isAnnounced = isAnnounced;
		this.isAudited = isAudited;
		this.auditer = auditer;
		this.auditTime = auditTime;
		this.isDistributeVersion = isDistributeVersion;
		this.originalOrganId = originalOrganId;
		this.distributor = distributor;
		this.distributeTime = distributeTime;
		this.uploader = uploader;
		this.uploaderName = uploaderName;
		this.uploadTime = uploadTime;
		this.content = content;
		this.fileName = fileName;
		this.isDisable = isDisable;
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

	public CustomFolder getCustomFolder() {
		return this.customFolder;
	}

	public void setCustomFolder(CustomFolder customFolder) {
		this.customFolder = customFolder;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSpecialtyId() {
		return this.specialtyId;
	}

	public void setSpecialtyId(String specialtyId) {
		this.specialtyId = specialtyId;
	}

	public String getQuarterId() {
		return this.quarterId;
	}

	public void setQuarterId(String quarterId) {
		this.quarterId = quarterId;
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

	public String getAuditer() {
		return this.auditer;
	}

	public void setAuditer(String auditer) {
		this.auditer = auditer;
	}

	public String getAuditTime() {
		return this.auditTime;
	}

	public void setAuditTime(String auditTime) {
		this.auditTime = auditTime;
	}

	public Integer getIsDistributeVersion() {
		return this.isDistributeVersion;
	}

	public void setIsDistributeVersion(Integer isDistributeVersion) {
		this.isDistributeVersion = isDistributeVersion;
	}

	public String getOriginalOrganId() {
		return this.originalOrganId;
	}

	public void setOriginalOrganId(String originalOrganId) {
		this.originalOrganId = originalOrganId;
	}

	public String getDistributor() {
		return this.distributor;
	}

	public void setDistributor(String distributor) {
		this.distributor = distributor;
	}

	public String getDistributeTime() {
		return this.distributeTime;
	}

	public void setDistributeTime(String distributeTime) {
		this.distributeTime = distributeTime;
	}

	public String getUploader() {
		return this.uploader;
	}

	public void setUploader(String uploader) {
		this.uploader = uploader;
	}

	public String getUploaderName() {
		return this.uploaderName;
	}

	public void setUploaderName(String uploaderName) {
		this.uploaderName = uploaderName;
	}

	public String getUploadTime() {
		return this.uploadTime;
	}

	public void setUploadTime(String uploadTime) {
		this.uploadTime = uploadTime;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getFileName() {
		return this.fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Integer getIsDisable() {
		return this.isDisable;
	}

	public void setIsDisable(Integer isDisable) {
		this.isDisable = isDisable;
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
		return accessories;
	}

	public void setAccessories(Set<Accessory> accessories) {
		this.accessories = accessories;
	}

}