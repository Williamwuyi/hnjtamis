package cn.com.ite.hnjtamis.exam.hibernatemap;

import java.util.ArrayList;
import java.util.List;

/**
 * StudyTestpaper entity. @author MyEclipse Persistence Tools
 */

public class StudyTestpaper implements java.io.Serializable {

	// Fields

	private String studyTestpaperId;
	private String studyTestpaperName;//
	private Integer totalTheme;//总题数
	private Double totalScore;//总分数
	private Integer isUse;//是否使用(5：否,10：是)
	private String remark;//备注
	private String state;//状态 5:生成
	private String organName;//机构名
	private String organId;//机构ID
	private String syncFlag;//同步标志
	private String lastUpdateDate;//最后修改时间
	private String lastUpdatedBy;//最后修改人
	private String lastUpdatedIdBy;//最后修改人ID
	private String creationDate;//创建时间
	private String createdBy;//创建人
	private String createdIdBy;//创建人ID
	private String relationId;//关联ID（如：练习安排等）
	private String relationType;//关联类型
	private List<StudyTestpaperTheme> studyTestpaperThemes = new ArrayList<StudyTestpaperTheme>(0);

	// Constructors

	/** default constructor */
	public StudyTestpaper() {
	}

	/** full constructor */
	public StudyTestpaper(String studyTestpaperName, Integer totalTheme,
			Double totalScore, Integer isUse, String remark, String state,
			String organName, String organId, String syncFlag,
			String lastUpdateDate, String lastUpdatedBy,
			String lastUpdatedIdBy, String creationDate, String createdBy,
			String createdIdBy, String relationId, String relationType,
			List<StudyTestpaperTheme> studyTestpaperThemes) {
		this.studyTestpaperName = studyTestpaperName;
		this.totalTheme = totalTheme;
		this.totalScore = totalScore;
		this.isUse = isUse;
		this.remark = remark;
		this.state = state;
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
		this.studyTestpaperThemes = studyTestpaperThemes;
	}

	// Property accessors

	public String getStudyTestpaperId() {
		return this.studyTestpaperId;
	}

	public void setStudyTestpaperId(String studyTestpaperId) {
		this.studyTestpaperId = studyTestpaperId;
	}

	public String getStudyTestpaperName() {
		return this.studyTestpaperName;
	}

	public void setStudyTestpaperName(String studyTestpaperName) {
		this.studyTestpaperName = studyTestpaperName;
	}

	public Integer getTotalTheme() {
		return this.totalTheme;
	}

	public void setTotalTheme(Integer totalTheme) {
		this.totalTheme = totalTheme;
	}

	public Double getTotalScore() {
		return this.totalScore;
	}

	public void setTotalScore(Double totalScore) {
		this.totalScore = totalScore;
	}

	public Integer getIsUse() {
		return this.isUse;
	}

	public void setIsUse(Integer isUse) {
		this.isUse = isUse;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getState() {
		return this.state;
	}

	public void setState(String state) {
		this.state = state;
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

	public List<StudyTestpaperTheme> getStudyTestpaperThemes() {
		return this.studyTestpaperThemes;
	}

	public void setStudyTestpaperThemes(List<StudyTestpaperTheme> studyTestpaperThemes) {
		this.studyTestpaperThemes = studyTestpaperThemes;
	}

}