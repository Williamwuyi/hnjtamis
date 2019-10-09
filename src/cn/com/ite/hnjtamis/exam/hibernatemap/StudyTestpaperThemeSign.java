package cn.com.ite.hnjtamis.exam.hibernatemap;

/**
 * StudyTestpaperThemeSign entity. @author MyEclipse Persistence Tools
 */

public class StudyTestpaperThemeSign implements java.io.Serializable {

	// Fields

	private String themeSignId;
	private StudyTestpaperTheme studyTestpaperTheme;//学习试卷试题ID
	private String themeId;//试题ID(试题库)
	private String signInTheme;//是否标记试题 10-是  空-否
	private String organId;//机构编号
	private String organName;//机构名称
	private String creationDate;//创建时间
	private String createdBy;//创建人
	private String createdIdBy;//创建人ID
	private String lastUpdateDate;//最后修改时间

	// Constructors

	/** default constructor */
	public StudyTestpaperThemeSign() {
	}

	/** full constructor */
	public StudyTestpaperThemeSign(StudyTestpaperTheme studyTestpaperTheme,
			String themeId, String signInTheme, String organId,
			String organName, String creationDate, String createdBy,
			String createdIdBy, String lastUpdateDate) {
		this.studyTestpaperTheme = studyTestpaperTheme;
		this.themeId = themeId;
		this.signInTheme = signInTheme;
		this.organId = organId;
		this.organName = organName;
		this.creationDate = creationDate;
		this.createdBy = createdBy;
		this.createdIdBy = createdIdBy;
		this.lastUpdateDate = lastUpdateDate;
	}

	// Property accessors

	public String getThemeSignId() {
		return this.themeSignId;
	}

	public void setThemeSignId(String themeSignId) {
		this.themeSignId = themeSignId;
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

	public String getSignInTheme() {
		return this.signInTheme;
	}

	public void setSignInTheme(String signInTheme) {
		this.signInTheme = signInTheme;
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

	public String getLastUpdateDate() {
		return this.lastUpdateDate;
	}

	public void setLastUpdateDate(String lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

}