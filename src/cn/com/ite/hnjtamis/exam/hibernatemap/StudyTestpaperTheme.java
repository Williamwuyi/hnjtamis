package cn.com.ite.hnjtamis.exam.hibernatemap;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * StudyTestpaperTheme entity. @author MyEclipse Persistence Tools
 */

public class StudyTestpaperTheme implements java.io.Serializable {

	// Fields

	private String studyTestpaperThemeId;
	private StudyTestpaper studyTestpaper;
	private String themeId;
	private String themeTypeId;
	private String themeTypeName;
	private String themeName;
	private String knowledgePoint;
	private Integer degree;
	private Integer eachline;
	private String explain;
	private Double defaultScore;
	private String scoreType;
	private Long sortNum;
	private String remark;
	private Integer state;
	private String isUse;
	private String syncFlag;
	private String lastUpdateDate;
	private String lastUpdatedBy;
	private String lastUpdatedIdBy;
	private String creationDate;
	private String createdBy;
	private String createdIdBy;
	private String relationId;
	private String relationType;
	private List<StudyTestpaperAnswerkey> studyTestpaperAnswerkeies = new ArrayList<StudyTestpaperAnswerkey>(0);
	private List<StudyTestpaperThemeSign> studyTestpaperThemeSigns = new ArrayList<StudyTestpaperThemeSign>(0);

	
	private int themeIndex;
	// Constructors

	/** default constructor */
	public StudyTestpaperTheme() {
	}

	/** full constructor */
	public StudyTestpaperTheme(StudyTestpaper studyTestpaper, String themeId,
			String themeTypeId, String themeTypeName, String themeName,
			String knowledgePoint, Integer degree, Integer eachline, String explain,
			Double defaultScore, String scoreType, Long sortNum,
			String remark, Integer state, String isUse, String syncFlag,
			String lastUpdateDate, String lastUpdatedBy,
			String lastUpdatedIdBy, String creationDate, String createdBy,
			String createdIdBy, String relationId, String relationType,
			List<StudyTestpaperAnswerkey> studyTestpaperAnswerkeies, List<StudyTestpaperThemeSign> studyTestpaperThemeSigns) {
		this.studyTestpaper = studyTestpaper;
		this.themeId = themeId;
		this.themeTypeId = themeTypeId;
		this.themeTypeName = themeTypeName;
		this.themeName = themeName;
		this.knowledgePoint = knowledgePoint;
		this.degree = degree;
		this.eachline = eachline;
		this.explain = explain;
		this.defaultScore = defaultScore;
		this.scoreType = scoreType;
		this.sortNum = sortNum;
		this.remark = remark;
		this.state = state;
		this.isUse = isUse;
		this.syncFlag = syncFlag;
		this.lastUpdateDate = lastUpdateDate;
		this.lastUpdatedBy = lastUpdatedBy;
		this.lastUpdatedIdBy = lastUpdatedIdBy;
		this.creationDate = creationDate;
		this.createdBy = createdBy;
		this.createdIdBy = createdIdBy;
		this.relationId = relationId;
		this.relationType = relationType;
		this.studyTestpaperAnswerkeies = studyTestpaperAnswerkeies;
		this.studyTestpaperThemeSigns = studyTestpaperThemeSigns;
	}

	// Property accessors

	public String getStudyTestpaperThemeId() {
		return this.studyTestpaperThemeId;
	}

	public void setStudyTestpaperThemeId(String studyTestpaperThemeId) {
		this.studyTestpaperThemeId = studyTestpaperThemeId;
	}

	public StudyTestpaper getStudyTestpaper() {
		return this.studyTestpaper;
	}

	public void setStudyTestpaper(StudyTestpaper studyTestpaper) {
		this.studyTestpaper = studyTestpaper;
	}

	public String getThemeId() {
		return this.themeId;
	}

	public void setThemeId(String themeId) {
		this.themeId = themeId;
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

	public String getThemeName() {
		return this.themeName;
	}

	public void setThemeName(String themeName) {
		this.themeName = themeName;
	}

	public String getKnowledgePoint() {
		return this.knowledgePoint;
	}

	public void setKnowledgePoint(String knowledgePoint) {
		this.knowledgePoint = knowledgePoint;
	}

	public Integer getDegree() {
		return this.degree;
	}

	public void setDegree(Integer degree) {
		this.degree = degree;
	}

	public Integer getEachline() {
		return this.eachline;
	}

	public void setEachline(Integer eachline) {
		this.eachline = eachline;
	}

	public String getExplain() {
		return this.explain;
	}

	public void setExplain(String explain) {
		this.explain = explain;
	}

	public Double getDefaultScore() {
		return this.defaultScore;
	}

	public void setDefaultScore(Double defaultScore) {
		this.defaultScore = defaultScore;
	}

	public String getScoreType() {
		return this.scoreType;
	}

	public void setScoreType(String scoreType) {
		this.scoreType = scoreType;
	}

	public Long getSortNum() {
		return this.sortNum;
	}

	public void setSortNum(Long sortNum) {
		this.sortNum = sortNum;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getState() {
		return this.state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getIsUse() {
		return this.isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
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

	public List<StudyTestpaperAnswerkey> getStudyTestpaperAnswerkeies() {
		return this.studyTestpaperAnswerkeies;
	}

	public void setStudyTestpaperAnswerkeies(List<StudyTestpaperAnswerkey> studyTestpaperAnswerkeies) {
		this.studyTestpaperAnswerkeies = studyTestpaperAnswerkeies;
	}

	public List<StudyTestpaperThemeSign> getStudyTestpaperThemeSigns() {
		return this.studyTestpaperThemeSigns;
	}

	public void setStudyTestpaperThemeSigns(List<StudyTestpaperThemeSign> studyTestpaperThemeSigns) {
		this.studyTestpaperThemeSigns = studyTestpaperThemeSigns;
	}

	public int getThemeIndex() {
		return themeIndex;
	}

	public void setThemeIndex(int themeIndex) {
		this.themeIndex = themeIndex;
	}

}