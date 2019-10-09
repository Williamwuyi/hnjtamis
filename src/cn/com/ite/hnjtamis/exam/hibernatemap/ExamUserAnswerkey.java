package cn.com.ite.hnjtamis.exam.hibernatemap;

/**
 * ExamUserAnswerkey entity. @author MyEclipse Persistence Tools
 */

public class ExamUserAnswerkey implements java.io.Serializable {

	// Fields

	private String userAnswerkeyId;
	private ExamTestpaperTheme examTestpaperTheme;
	private String examAnswerkey;
	private String answerkeyValue;
	private Integer sortNum;
	private Double score;
	private String memo;
	private Integer state;
	private String answerkeyId;
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
	public ExamUserAnswerkey() {
	}

	/** minimal constructor */
	public ExamUserAnswerkey(String answerkeyId) {
		this.answerkeyId = answerkeyId;
	}

	/** full constructor */
	public ExamUserAnswerkey(ExamTestpaperTheme examTestpaperTheme,
			String examAnswerkey, String answerkeyValue, Integer sortNum,
			Double score, String memo, Integer state, String answerkeyId,
			String organName, String organId, String syncFlag,
			String lastUpdateDate, String lastUpdatedBy,
			String lastUpdatedIdBy, String creationDate, String createdBy,
			String createdIdBy) {
		this.examTestpaperTheme = examTestpaperTheme;
		this.examAnswerkey = examAnswerkey;
		this.answerkeyValue = answerkeyValue;
		this.sortNum = sortNum;
		this.score = score;
		this.memo = memo;
		this.state = state;
		this.answerkeyId = answerkeyId;
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

	public String getUserAnswerkeyId() {
		return this.userAnswerkeyId;
	}

	public void setUserAnswerkeyId(String userAnswerkeyId) {
		this.userAnswerkeyId = userAnswerkeyId;
	}

	public ExamTestpaperTheme getExamTestpaperTheme() {
		return this.examTestpaperTheme;
	}

	public void setExamTestpaperTheme(ExamTestpaperTheme examTestpaperTheme) {
		this.examTestpaperTheme = examTestpaperTheme;
	}

	public String getExamAnswerkey() {
		return this.examAnswerkey;
	}

	public void setExamAnswerkey(String examAnswerkey) {
		this.examAnswerkey = examAnswerkey;
	}

	public String getAnswerkeyValue() {
		return this.answerkeyValue;
	}

	public void setAnswerkeyValue(String answerkeyValue) {
		this.answerkeyValue = answerkeyValue;
	}

	public Integer getSortNum() {
		return this.sortNum;
	}

	public void setSortNum(Integer sortNum) {
		this.sortNum = sortNum;
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

	public String getAnswerkeyId() {
		return this.answerkeyId;
	}

	public void setAnswerkeyId(String answerkeyId) {
		this.answerkeyId = answerkeyId;
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