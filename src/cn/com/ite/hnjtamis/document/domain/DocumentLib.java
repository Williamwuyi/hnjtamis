package cn.com.ite.hnjtamis.document.domain;

import java.util.ArrayList;
import java.util.List;

import cn.com.ite.hnjtamis.baseinfo.domain.Speciality;

/**
 * DocumentLib entity. @author MyEclipse Persistence Tools
 */

public class DocumentLib implements java.io.Serializable {

	// Fields

	private String documentId;
	private String documentName;//文档名
	private String documentType;//文档类型
	private String documentTypeId;//文档类型ID
	private String description;//描述
	private String writerUser;//编著人
	private String publishers;//出版社
	private String fxDay;//发行日期
	private Integer isAnnounced;//是否公布：10、草稿不公布  20、公布
	private Integer isAudited;///是否通过审核：10、未审核；20、已审核
	private String auditer;//审核人
	private String auditTime;//审核时间
	private Integer isDistributeVersion;//是否分发版本：0、否；1、是
	private String originalOrganId;//原机构ID
	private String originalOrganName;//原机构NAME
	private String distributor;//分发人
	private String distributeTime;//分发时间
	private String uploader;//上传人
	private String uploaderName;//上传人名称
	private String uploadTime;//上传时间
	private String content;//内容：文本或编码后内容
	private String fileName;//文件名
	private Integer isDisable;//是否停用：0、正常；1、停用
	private Integer isDel;//是否删除：0、正常，1、已删除
	private Integer syncStatus;//同步标识：0、正常，1、添加，2、更新，3、删除
	private String lastUpdateDate;//上次修改时间
	private String lastUpdatedBy;//上次修改人员
	private String lastUpdatedById;//上次修改人员
	private String creationDate;//创建时间
	private String createdBy;//创建人员
	private String createdById;//创建人员
	private String organId;//机构ID
	private String organName;//机构Name
	private String remark;//备注
	private String afficheId;//附件用的ID
	private String specialityNames;//专业（显示用）
	private String tofavorite = "false";
	private List<DocumentSearchkey> documentSearchkeies = new ArrayList<DocumentSearchkey>(0);//

	private List<Speciality> specialityList = new ArrayList<Speciality>();//专业
	// Constructors

	/** default constructor */
	public DocumentLib() {
	}

	/** full constructor */
	public DocumentLib(String documentName, String documentType,
			String documentTypeId, String description, String writerUser,
			String publishers, String fxDay, Integer isAnnounced,
			Integer isAudited, String auditer, String auditTime,
			Integer isDistributeVersion, String originalOrganId,
			String distributor, String distributeTime, String uploader,
			String uploaderName, String uploadTime, String content,
			String fileName, Integer isDisable, Integer isDel,
			Integer syncStatus, String lastUpdateDate, String lastUpdatedBy,
			String creationDate, String createdBy, String organId,
			String originalOrganName,String lastUpdatedById,String createdById,String organName,
			String remark, List<DocumentSearchkey> documentSearchkeies,String specialityNames) {
		this.documentName = documentName;
		this.documentType = documentType;
		this.documentTypeId = documentTypeId;
		this.description = description;
		this.writerUser = writerUser;
		this.publishers = publishers;
		this.fxDay = fxDay;
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
		this.originalOrganName  = originalOrganName;
		this.lastUpdatedById  = lastUpdatedById;
		this.createdById  = createdById;
		this.organName  = organName;
		this.documentSearchkeies = documentSearchkeies;
		this.specialityNames = specialityNames;
	}

	// Property accessors

	public String getDocumentId() {
		return this.documentId;
	}

	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}

	public String getDocumentName() {
		return this.documentName;
	}

	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}

	public String getDocumentType() {
		return this.documentType;
	}

	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}

	public String getDocumentTypeId() {
		return this.documentTypeId;
	}

	public void setDocumentTypeId(String documentTypeId) {
		this.documentTypeId = documentTypeId;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getWriterUser() {
		return this.writerUser;
	}

	public void setWriterUser(String writerUser) {
		this.writerUser = writerUser;
	}

	public String getPublishers() {
		return publishers;
	}

	public void setPublishers(String publishers) {
		this.publishers = publishers;
	}

	public String getFxDay() {
		return this.fxDay;
	}

	public void setFxDay(String fxDay) {
		this.fxDay = fxDay;
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

	public List<DocumentSearchkey> getDocumentSearchkeies() {
		return this.documentSearchkeies;
	}

	public void setDocumentSearchkeies(List<DocumentSearchkey> documentSearchkeies) {
		this.documentSearchkeies = documentSearchkeies;
	}

	public String getOriginalOrganName() {
		return originalOrganName;
	}

	public void setOriginalOrganName(String originalOrganName) {
		this.originalOrganName = originalOrganName;
	}

	public String getLastUpdatedById() {
		return lastUpdatedById;
	}

	public void setLastUpdatedById(String lastUpdatedById) {
		this.lastUpdatedById = lastUpdatedById;
	}

	public String getCreatedById() {
		return createdById;
	}

	public void setCreatedById(String createdById) {
		this.createdById = createdById;
	}

	public String getOrganName() {
		return organName;
	}

	public void setOrganName(String organName) {
		this.organName = organName;
	}

	public String getAfficheId() {
		return afficheId;
	}

	public void setAfficheId(String afficheId) {
		this.afficheId = afficheId;
	}

	public List<Speciality> getSpecialityList() {
		return specialityList;
	}

	public void setSpecialityList(List<Speciality> specialityList) {
		this.specialityList = specialityList;
	}

	public String getSpecialityNames() {
		return specialityNames;
	}

	public void setSpecialityNames(String specialityNames) {
		this.specialityNames = specialityNames;
	}

	public String getTofavorite() {
		return tofavorite;
	}

	public void setTofavorite(String tofavorite) {
		this.tofavorite = tofavorite;
	}
	
	

}