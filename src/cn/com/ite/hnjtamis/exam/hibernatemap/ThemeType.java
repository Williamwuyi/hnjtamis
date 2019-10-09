package cn.com.ite.hnjtamis.exam.hibernatemap;

import java.util.*;

/**
 * ThemeType entity. @author MyEclipse Persistence Tools
 */

public class ThemeType implements java.io.Serializable {

	// Fields

	private String themeTypeId;
	private String themeTypeName;
	private String parentTypeName;
	private String themeType;
	private Double score;
	private Integer judge;
	private Integer isUse;
	private Integer sortNum;
	private String remark;
	private String syncFlag;
	private String organId;
	private String organName;
	private String lastUpdateDate;
	private String lastUpdatedBy;
	private String lastUpdatedIdBy;
	private String creationDate;
	private String createdBy;
	private String createdIdBy;
	private List<TestpaperTheme> testpaperThemes = new ArrayList<TestpaperTheme>(0);
	private List<TestpaperThemetype> testpaperThemetypes = new ArrayList<TestpaperThemetype>(0);
	private List<Theme> themes = new ArrayList<Theme>(0);

	private List<ThemeType> themeTypeCom = new ArrayList<ThemeType>();//用于生成下拉
	// Constructors

	/** default constructor */
	public ThemeType() {
	}

	/** full constructor */
	public ThemeType(String themeTypeId, String themeTypeName,
			String parentTypeName, String themeType, Double score,
			Integer judge, Integer isUse, Integer sortNum, String remark,
			String syncFlag, String organId, String organName,
			String lastUpdateDate, String lastUpdatedBy,
			String lastUpdatedIdBy, String creationDate, String createdBy,
			String createdIdBy, List<TestpaperTheme> testpaperThemes,
			List<TestpaperThemetype> testpaperThemetypes, List<Theme> themes) {
		super();
		this.themeTypeId = themeTypeId;
		this.themeTypeName = themeTypeName;
		this.parentTypeName = parentTypeName;
		this.themeType = themeType;
		this.score = score;
		this.judge = judge;
		this.isUse = isUse;
		this.sortNum = sortNum;
		this.remark = remark;
		this.syncFlag = syncFlag;
		this.organId = organId;
		this.organName = organName;
		this.lastUpdateDate = lastUpdateDate;
		this.lastUpdatedBy = lastUpdatedBy;
		this.lastUpdatedIdBy = lastUpdatedIdBy;
		this.creationDate = creationDate;
		this.createdBy = createdBy;
		this.createdIdBy = createdIdBy;
		this.testpaperThemes = testpaperThemes;
		this.testpaperThemetypes = testpaperThemetypes;
		this.themes = themes;
	}

	// Property accessors

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

	public String getParentTypeName() {
		return this.parentTypeName;
	}

	public void setParentTypeName(String parentTypeName) {
		this.parentTypeName = parentTypeName;
	}

	public String getThemeType() {
		return this.themeType;
	}

	public void setThemeType(String themeType) {
		this.themeType = themeType;
	}

	public Double getScore() {
		return this.score;
	}

	public void setScore(Double score) {
		this.score = score;
	}

	public Integer getJudge() {
		return this.judge;
	}

	public void setJudge(Integer judge) {
		this.judge = judge;
	}

	public Integer getIsUse() {
		return this.isUse;
	}

	public void setIsUse(Integer isUse) {
		this.isUse = isUse;
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

	public String getSyncFlag() {
		return this.syncFlag;
	}

	public void setSyncFlag(String syncFlag) {
		this.syncFlag = syncFlag;
	}

	public String getOrganId() {
		return this.organId;
	}

	public void setOrganId(String organId) {
		this.organId = organId;
	}

	public String getOrganName() {
		return this.organName;
	}

	public void setOrganName(String organName) {
		this.organName = organName;
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

	public List<TestpaperTheme> getTestpaperThemes() {
		return testpaperThemes;
	}

	public void setTestpaperThemes(List<TestpaperTheme> testpaperThemes) {
		this.testpaperThemes = testpaperThemes;
	}

	public List<TestpaperThemetype> getTestpaperThemetypes() {
		return testpaperThemetypes;
	}

	public void setTestpaperThemetypes(List<TestpaperThemetype> testpaperThemetypes) {
		this.testpaperThemetypes = testpaperThemetypes;
	}

	public List<Theme> getThemes() {
		return themes;
	}

	public void setThemes(List<Theme> themes) {
		this.themes = themes;
	}

	public List<ThemeType> getThemeTypeCom() {
		return themeTypeCom;
	}

	public void setThemeTypeCom(List<ThemeType> themeTypeCom) {
		this.themeTypeCom = themeTypeCom;
	}

	

}