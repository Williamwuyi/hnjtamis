package cn.com.ite.hnjtamis.exam.hibernatemap;

/**
 * StudyTestpaperAnswerkey entity. @author MyEclipse Persistence Tools
 */

public class StudyTestpaperAnswerkey implements java.io.Serializable {

	// Fields

	private String answerkeyId;
	private StudyTestpaperTheme studyTestpaperTheme;
	private String themeId;
	private String studyTestpaperId;
	private String answerkeyValue;
	private Integer isRight;
	private Integer sortNum;
	private String remark;
	private String themeTypeId;
	private String themeTypeName;
	private String organName;
	private String organId;
	private String syncFlag;
	private String lastUpdateDate;
	private String lastUpdatedBy;
	private String lastUpdatedIdBy;
	private String creationDate;
	private String createdBy;
	private String createdIdBy;
	private String relationId;
	private String relationType;

	// Constructors

	/** default constructor */
	public StudyTestpaperAnswerkey() {
	}

	/** full constructor */
	public StudyTestpaperAnswerkey(StudyTestpaperTheme studyTestpaperTheme,
			String themeId, String studyTestpaperId, String answerkeyValue,
			Integer isRight,Integer sortNum, String remark, String themeTypeId,
			String themeTypeName, String organName, String organId,
			String syncFlag, String lastUpdateDate, String lastUpdatedBy,
			String lastUpdatedIdBy, String creationDate, String createdBy,
			String createdIdBy, String relationId, String relationType) {
		this.studyTestpaperTheme = studyTestpaperTheme;
		this.themeId = themeId;
		this.studyTestpaperId = studyTestpaperId;
		this.answerkeyValue = answerkeyValue;
		this.isRight = isRight;
		this.sortNum = sortNum;
		this.remark = remark;
		this.themeTypeId = themeTypeId;
		this.themeTypeName = themeTypeName;
		this.organName = organName;
		this.organId = organId;
		this.syncFlag = syncFlag;
		this.lastUpdateDate = lastUpdateDate;
		this.lastUpdatedBy = lastUpdatedBy;
		this.lastUpdatedIdBy = lastUpdatedIdBy;
		this.creationDate = creationDate;
		this.createdBy = createdBy;
		this.createdIdBy = createdIdBy;
		this.relationId = relationId;
		this.relationType = relationType;
	}

	// Property accessors

	public String getAnswerkeyId() {
		return this.answerkeyId;
	}

	public void setAnswerkeyId(String answerkeyId) {
		this.answerkeyId = answerkeyId;
	}

	public StudyTestpaperTheme getStudyTestpaperTheme() {
		return this.studyTestpaperTheme;
	}

	public void setStudyTestpaperTheme(StudyTestpaperTheme studyTestpaperTheme) {
		this.studyTestpaperTheme = studyTestpaperTheme;
	}

	public String getThemeId() {
		return this.themeId;
	}

	public void setThemeId(String themeId) {
		this.themeId = themeId;
	}

	public String getStudyTestpaperId() {
		return this.studyTestpaperId;
	}

	public void setStudyTestpaperId(String studyTestpaperId) {
		this.studyTestpaperId = studyTestpaperId;
	}

	public String getAnswerkeyValue() {
		return this.answerkeyValue;
	}

	public void setAnswerkeyValue(String answerkeyValue) {
		this.answerkeyValue = answerkeyValue;
	}

	public Integer getIsRight() {
		return this.isRight;
	}

	public void setIsRight(Integer isRight) {
		this.isRight = isRight;
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

	public String getThemeTypeId() {
		return this.themeTypeId;
	}

	public void setThemeTypeId(String themeTypeId) {
		this.themeTypeId = themeTypeId;
	}

	public String getThemeTypeName() {
		return this.themeTypeName;
	}

	public void setThemeTypeName(String themeTypeName) {
		this.themeTypeName = themeTypeName;
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

}