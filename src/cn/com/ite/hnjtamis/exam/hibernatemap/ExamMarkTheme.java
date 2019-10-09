package cn.com.ite.hnjtamis.exam.hibernatemap;

/**
 * ExamMarkTheme entity. @author MyEclipse Persistence Tools
 */

public class ExamMarkTheme implements java.io.Serializable {

	// Fields

	private String markThemeId;
	private ExamMarkpeople examMarkpeople;
	private ExamTestpaperTheme examTestpaperTheme;
	private Double score;
	private String memo;
	private Integer state;
	private String organName;
	private String organId;
	private String syncFlag;
	private String lastUpdateDate;
	private String lastUpdatedBy;
	private String lastUpdatedIdBy;
	private String creationDate;
	private String createdBy;
	private String createdIdBy;

	// Constructors

	/** default constructor */
	public ExamMarkTheme() {
	}

	/** full constructor */
	public ExamMarkTheme(ExamMarkpeople examMarkpeople,
			ExamTestpaperTheme examTestpaperTheme, Double score, String memo,
			Integer state, String organName, String organId, String syncFlag,
			String lastUpdateDate, String lastUpdatedBy,
			String lastUpdatedIdBy, String creationDate, String createdBy,
			String createdIdBy) {
		this.examMarkpeople = examMarkpeople;
		this.examTestpaperTheme = examTestpaperTheme;
		this.score = score;
		this.memo = memo;
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
	}

	// Property accessors

	public String getMarkThemeId() {
		return this.markThemeId;
	}

	public void setMarkThemeId(String markThemeId) {
		this.markThemeId = markThemeId;
	}

	public ExamMarkpeople getExamMarkpeople() {
		return this.examMarkpeople;
	}

	public void setExamMarkpeople(ExamMarkpeople examMarkpeople) {
		this.examMarkpeople = examMarkpeople;
	}

	public ExamTestpaperTheme getExamTestpaperTheme() {
		return this.examTestpaperTheme;
	}

	public void setExamTestpaperTheme(ExamTestpaperTheme examTestpaperTheme) {
		this.examTestpaperTheme = examTestpaperTheme;
	}

	public Double getScore() {
		return this.score;
	}

	public void setScore(Double score) {
		this.score = score;
	}

	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public Integer getState() {
		return this.state;
	}

	public void setState(Integer state) {
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

}