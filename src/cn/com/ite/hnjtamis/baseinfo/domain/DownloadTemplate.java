package cn.com.ite.hnjtamis.baseinfo.domain;

/**
 * DownloadTemplate entity. @author MyEclipse Persistence Tools
 */

public class DownloadTemplate implements java.io.Serializable {

	// Fields

	private String templateId;//模版ID
	private String templateName;//模版名
	private String relationId;//关联ID
	private String relationType;//关联类型
	private Integer acceNum;//附件数量
	private String acceId;//附件ID
	private String acceSize;//附件大小
	private String remark;//备注
	private String organId;//机构ID
	private String organName;//机构名
	private String syncFlag;////同步标志
	private String lastUpdateDate;///最后修改时间
	private String lastUpdatedBy;//最后修改人
	private String lastUpdatedIdBy;//最后修改人ID
	private String creationDate;//创建时间
	private String createdBy;//创建人
	private String createdIdBy;//创建人ID

	// Constructors

	/** default constructor */
	public DownloadTemplate() {
	}

	/** full constructor */
	public DownloadTemplate(String templateName, String relationId,
			String relationType, Integer acceNum, String acceId,
			String acceSize, String remark, String organName, String organId,
			String syncFlag, String lastUpdateDate, String lastUpdatedBy,
			String lastUpdatedIdBy, String creationDate, String createdBy,
			String createdIdBy) {
		this.templateName = templateName;
		this.relationId = relationId;
		this.relationType = relationType;
		this.acceNum = acceNum;
		this.acceId = acceId;
		this.acceSize = acceSize;
		this.remark = remark;
		this.organName = organName;
		this.organId = organId;
		this.syncFlag = syncFlag;
		this.lastUpdateDate = lastUpdateDate;
		this.lastUpdatedBy = lastUpdatedBy;
		this.lastUpdatedIdBy = lastUpdatedIdBy;
		this.creationDate = creationDate;
		this.createdBy = createdBy;
		this.createdIdBy = createdIdBy;
	}

	// Property accessors

	public String getTemplateId() {
		return this.templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public String getTemplateName() {
		return this.templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public String getRelationId() {
		return this.relationId;
	}

	public void setRelationId(String relationId) {
		this.relationId = relationId;
	}

	public String getRelationType() {
		return this.relationType;
	}

	public void setRelationType(String relationType) {
		this.relationType = relationType;
	}

	public Integer getAcceNum() {
		return this.acceNum;
	}

	public void setAcceNum(Integer acceNum) {
		this.acceNum = acceNum;
	}

	public String getAcceId() {
		return this.acceId;
	}

	public void setAcceId(String acceId) {
		this.acceId = acceId;
	}

	public String getAcceSize() {
		return this.acceSize;
	}

	public void setAcceSize(String acceSize) {
		this.acceSize = acceSize;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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

}