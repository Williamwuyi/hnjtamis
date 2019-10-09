package cn.com.ite.hnjtamis.exam.hibernatemap;

/**
 * ExamTestpaperAnswerkey entity. @author MyEclipse Persistence Tools
 */

public class ExamTestpaperAnswerkey implements java.io.Serializable {

	// Fields

	private String answerkeyId;
	private ExamTestpaperTheme examTestpaperTheme;
	private String themeId;
	private String answerkeyValue;
	private Integer isRight;
	private Integer sortNum;
	private Integer randomSortnum;
	private String remark;
	private String themeTypeId;
	private String themeTypeName;
	private String createdIdBy;
	private String userId;
	private String examArrangeId;
	private String organName;
	private String organId;
	private String syncFlag;
	private String lastUpdateDate;
	private String lastUpdatedBy;
	private String lastUpdatedIdBy;
	private String creationDate;
	private String createdBy;
	private String examId;
	private String testpaperId;

	// Constructors

	/** default constructor */
	public ExamTestpaperAnswerkey() {
	}

	/** full constructor */
	public ExamTestpaperAnswerkey(ExamTestpaperTheme examTestpaperTheme,
			String themeId, String answerkeyValue, Integer isRight, Integer sortNum,
			Integer randomSortnum, String remark, String themeTypeId,
			String themeTypeName, String createdIdBy, String userId,
			String examArrangeId, String organName, String organId,
			String syncFlag, String lastUpdateDate, String lastUpdatedBy,
			String lastUpdatedIdBy, String creationDate, String createdBy,
			String examId, String testpaperId) {
		this.examTestpaperTheme = examTestpaperTheme;
		this.themeId = themeId;
		this.answerkeyValue = answerkeyValue;
		this.isRight = isRight;
		this.sortNum = sortNum;
		this.randomSortnum = randomSortnum;
		this.remark = remark;
		this.themeTypeId = themeTypeId;
		this.themeTypeName = themeTypeName;
		this.createdIdBy = createdIdBy;
		this.userId = userId;
		this.examArrangeId = examArrangeId;
		this.organName = organName;
		this.organId = organId;
		this.syncFlag = syncFlag;
		this.lastUpdateDate = lastUpdateDate;
		this.lastUpdatedBy = lastUpdatedBy;
		this.lastUpdatedIdBy = lastUpdatedIdBy;
		this.creationDate = creationDate;
		this.createdBy = createdBy;
		this.examId = examId;
		this.testpaperId = testpaperId;
	}

	// Property accessors

	public String getAnswerkeyId() {
		return this.answerkeyId;
	}

	public void setAnswerkeyId(String answerkeyId) {
		this.answerkeyId = answerkeyId;
	}

	public ExamTestpaperTheme getExamTestpaperTheme() {
		return this.examTestpaperTheme;
	}

	public void setExamTestpaperTheme(ExamTestpaperTheme examTestpaperTheme) {
		this.examTestpaperTheme = examTestpaperTheme;
	}

	public String getThemeId() {
		return this.themeId;
	}

	public void setThemeId(String themeId) {
		this.themeId = themeId;
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

	public Integer getRandomSortnum() {
		return this.randomSortnum;
	}

	public void setRandomSortnum(Integer randomSortnum) {
		this.randomSortnum = randomSortnum;
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

	public String getCreatedIdBy() {
		return this.createdIdBy;
	}

	public void setCreatedIdBy(String createdIdBy) {
		this.createdIdBy = createdIdBy;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getExamArrangeId() {
		return this.examArrangeId;
	}

	public void setExamArrangeId(String examArrangeId) {
		this.examArrangeId = examArrangeId;
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

	public String getExamId() {
		return this.examId;
	}

	public void setExamId(String examId) {
		this.examId = examId;
	}

	public String getTestpaperId() {
		return this.testpaperId;
	}

	public void setTestpaperId(String testpaperId) {
		this.testpaperId = testpaperId;
	}

}