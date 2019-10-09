package cn.com.ite.hnjtamis.kb.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cn.com.ite.eap2.domain.baseinfo.Accessory;
import cn.com.ite.eap2.domain.organization.Organ;

/**
 * Courseware entity. @author MyEclipse Persistence Tools
 */

public class Courseware implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -3284439766895082327L;
	private String id;
	private Integer type;
	private String title;
	private String description;
	private String series;
	private String editor;
	private String press;
	private String edition;
	private String publishTime;
	private Integer isAnnounced;
	private Integer isAudited;
	private String auditer;
	private String auditTime;
	private Integer isDistributeVersion;
	private String originalCourseId;
	private String originalOrganId;
	private String distributor;
	private String distributeTime;
	private String uploaderId;
	private String uploaderName;
	private String uploadTime;
	private String content;
	private byte[] poster;
	private String fileName;
	private Integer isExpertLectures;
	private Integer isDisable;
	private Integer isDel;
	private Integer syncStatus;
	private String lastUpdateDate;
	private String lastUpdatedBy;
	private String creationDate;
	private String createdBy;
	private Organ organ;
	private String remark;
	private List<CoursewareDistribute> coursewareDistributes = new ArrayList<CoursewareDistribute>(0);
	/**
	 * 附件
	 */
	private Set<Accessory> accessories = new HashSet<Accessory>();

	// Constructors

	/** default constructor */
	public Courseware() {
	}

	/** full constructor */
	public Courseware(Integer type, String title, String description,
			String series, String editor, String press, String edition,
			String publishTime, Integer isAnnounced, Integer isAudited,
			String auditer, String auditTime, Integer isDistributeVersion,
			String originalCourseId, String originalOrganId,
			String distributor, String distributeTime, String uploaderId,String uploaderName,
			String uploadTime, String content, String fileName,
			Integer isDisable, Integer isDel, Integer syncStatus,
			String lastUpdateDate, String lastUpdatedBy, String creationDate,
			String createdBy, Organ organ, String remark,
			List<CoursewareDistribute> coursewareDistributes) {
		this.type = type;
		this.title = title;
		this.description = description;
		this.series = series;
		this.editor = editor;
		this.press = press;
		this.edition = edition;
		this.publishTime = publishTime;
		this.isAnnounced = isAnnounced;
		this.isAudited = isAudited;
		this.auditer = auditer;
		this.auditTime = auditTime;
		this.isDistributeVersion = isDistributeVersion;
		this.originalCourseId = originalCourseId;
		this.originalOrganId = originalOrganId;
		this.distributor = distributor;
		this.distributeTime = distributeTime;
		this.uploaderId = uploaderId;
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
		this.organ = organ;
		this.remark = remark;
		this.coursewareDistributes = coursewareDistributes;
	}

	// Property accessors

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getType() {
		return this.type;
	}

	public void setType(Integer type) {
		this.type = type;
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

	public String getSeries() {
		return this.series;
	}

	public void setSeries(String series) {
		this.series = series;
	}

	public String getEditor() {
		return this.editor;
	}

	public void setEditor(String editor) {
		this.editor = editor;
	}

	public String getPress() {
		return this.press;
	}

	public void setPress(String press) {
		this.press = press;
	}

	public String getEdition() {
		return this.edition;
	}

	public void setEdition(String edition) {
		this.edition = edition;
	}

	public String getPublishTime() {
		return this.publishTime;
	}

	public void setPublishTime(String publishTime) {
		this.publishTime = publishTime;
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

	public String getOriginalCourseId() {
		return this.originalCourseId;
	}

	public void setOriginalCourseId(String originalCourseId) {
		this.originalCourseId = originalCourseId;
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

	public String getUploaderId() {
		return this.uploaderId;
	}

	public void setUploaderId(String uploaderId) {
		this.uploaderId = uploaderId;
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

	public Organ getOrgan() {
		return this.organ;
	}

	public void setOrgan(Organ organ) {
		this.organ = organ;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public List<CoursewareDistribute> getCoursewareDistributes() {
		return this.coursewareDistributes;
	}

	public void setCoursewareDistributes(
			List<CoursewareDistribute> coursewareDistributes) {
		this.coursewareDistributes = coursewareDistributes;
	}

	public Set<Accessory> getAccessories() {
		return accessories;
	}

	public void setAccessories(Set<Accessory> accessories) {
		this.accessories = accessories;
	}

	public byte[] getPoster() {
		return poster;
	}

	public void setPoster(byte[] poster) {
		this.poster = poster;
	}

	public Integer getIsExpertLectures() {
		return isExpertLectures;
	}

	public void setIsExpertLectures(Integer isExpertLectures) {
		this.isExpertLectures = isExpertLectures;
	}

}