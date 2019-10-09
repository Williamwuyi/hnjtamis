package cn.com.ite.hnjtamis.exam.hibernatemap;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * ExamTestpaperTheme entity. @author MyEclipse Persistence Tools
 */

public class ExamTestpaperTheme implements java.io.Serializable {

	// Fields

	private String examTestpaperThemeId;
	private ExamTestpaper examTestpaper;
	private String themeId;
	private String testpaperThemeId;
	private String themeTypeId;
	private String themeTypeName;
	private String themeName;
	private String knowledgePoint;
	private Integer degree;
	private Integer eachline;
	private String explain;
	private Double defaultScore;
	private Double score;
	private String scoreType;
	private Integer sortNum;
	private Integer randomSortnum;
	private String remark;
	private Integer state;
	private String isUse;
	private String syncFlag;
	private String organId;
	private String organName;
	private String lastUpdateDate;
	private String lastUpdatedBy;
	private String lastUpdatedIdBy;
	private String creationDate;
	private String createdBy;
	private String createdIdBy;
	private String rightAnswerkey;
	private String rightAnswerkeyid;
	private String answerkeyContent;
	private String sjRightAnswerkey;
	private String sjRightAnswerkeyid;
	private String sjAnswerkeyContent;
	private String userId;
	private String examArrangeId;
	private String examId;
	private String testpaperId;
	private String reviewComments;
	private String signInTheme;
	private List<ExamTestpaperAnswerkey> examTestpaperAnswerkeies = new ArrayList<ExamTestpaperAnswerkey>(0);
	private List<ExamMarkTheme> examMarkThemes = new ArrayList<ExamMarkTheme>(0);
	private List<ExamUserAnswerkey> examUserAnswerkeies = new ArrayList<ExamUserAnswerkey>(0);
	private List<ExamThemeSkey> examThemeSkeies = new ArrayList<ExamThemeSkey>(0);

	// Constructors

	/** default constructor */
	public ExamTestpaperTheme() {
	}

	/** full constructor */
	public ExamTestpaperTheme(ExamTestpaper examTestpaper, String themeId,
			String testpaperThemeId, String themeTypeId, String themeTypeName,
			String themeName, String knowledgePoint, Integer degree,
			Integer eachline, String explain, Double defaultScore, Double score,
			String scoreType, Integer sortNum, Integer randomSortnum,
			String remark, Integer state, String isUse, String syncFlag,
			String organId, String organName, String lastUpdateDate,
			String lastUpdatedBy, String lastUpdatedIdBy, String creationDate,
			String createdBy, String createdIdBy, String rightAnswerkey,
			String rightAnswerkeyid, String answerkeyContent,
			String sjRightAnswerkey, String sjRightAnswerkeyid,
			String sjAnswerkeyContent, String userId, String examArrangeId,
			String examId, String testpaperId, String signInTheme,
			List<ExamTestpaperAnswerkey> examTestpaperAnswerkeies,
			List<ExamMarkTheme> examMarkThemes, List<ExamUserAnswerkey> examUserAnswerkeies, List<ExamThemeSkey> examThemeSkeies) {
		this.examTestpaper = examTestpaper;
		this.themeId = themeId;
		this.testpaperThemeId = testpaperThemeId;
		this.themeTypeId = themeTypeId;
		this.themeTypeName = themeTypeName;
		this.themeName = themeName;
		this.knowledgePoint = knowledgePoint;
		this.degree = degree;
		this.eachline = eachline;
		this.explain = explain;
		this.defaultScore = defaultScore;
		this.score = score;
		this.scoreType = scoreType;
		this.sortNum = sortNum;
		this.randomSortnum = randomSortnum;
		this.remark = remark;
		this.state = state;
		this.isUse = isUse;
		this.syncFlag = syncFlag;
		this.organId = organId;
		this.organName = organName;
		this.lastUpdateDate = lastUpdateDate;
		this.lastUpdatedBy = lastUpdatedBy;
		this.lastUpdatedIdBy = lastUpdatedIdBy;
		this.creationDate = creationDate;
		this.createdBy = createdBy;
		this.createdIdBy = createdIdBy;
		this.rightAnswerkey = rightAnswerkey;
		this.rightAnswerkeyid = rightAnswerkeyid;
		this.answerkeyContent = answerkeyContent;
		this.sjRightAnswerkey = sjRightAnswerkey;
		this.sjRightAnswerkeyid = sjRightAnswerkeyid;
		this.sjAnswerkeyContent = sjAnswerkeyContent;
		this.userId = userId;
		this.examArrangeId = examArrangeId;
		this.examId = examId;
		this.testpaperId = testpaperId;
		this.examTestpaperAnswerkeies = examTestpaperAnswerkeies;
		this.examMarkThemes = examMarkThemes;
		this.examUserAnswerkeies = examUserAnswerkeies;
		this.examThemeSkeies = examThemeSkeies;
		this.signInTheme=signInTheme;
	}

	// Property accessors

	public String getExamTestpaperThemeId() {
		return this.examTestpaperThemeId;
	}

	public void setExamTestpaperThemeId(String examTestpaperThemeId) {
		this.examTestpaperThemeId = examTestpaperThemeId;
	}

	public ExamTestpaper getExamTestpaper() {
		return this.examTestpaper;
	}

	public void setExamTestpaper(ExamTestpaper examTestpaper) {
		this.examTestpaper = examTestpaper;
	}

	public String getThemeId() {
		return this.themeId;
	}

	public void setThemeId(String themeId) {
		this.themeId = themeId;
	}

	public String getTestpaperThemeId() {
		return this.testpaperThemeId;
	}

	public void setTestpaperThemeId(String testpaperThemeId) {
		this.testpaperThemeId = testpaperThemeId;
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

	public Double getScore() {
		return this.score;
	}

	public void setScore(Double score) {
		this.score = score;
	}

	public String getScoreType() {
		return this.scoreType;
	}

	public void setScoreType(String scoreType) {
		this.scoreType = scoreType;
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

	public String getRightAnswerkey() {
		return this.rightAnswerkey;
	}

	public void setRightAnswerkey(String rightAnswerkey) {
		this.rightAnswerkey = rightAnswerkey;
	}

	public String getRightAnswerkeyid() {
		return this.rightAnswerkeyid;
	}

	public void setRightAnswerkeyid(String rightAnswerkeyid) {
		this.rightAnswerkeyid = rightAnswerkeyid;
	}

	public String getAnswerkeyContent() {
		return this.answerkeyContent;
	}

	public void setAnswerkeyContent(String answerkeyContent) {
		this.answerkeyContent = answerkeyContent;
	}

	public String getSjRightAnswerkey() {
		return this.sjRightAnswerkey;
	}

	public void setSjRightAnswerkey(String sjRightAnswerkey) {
		this.sjRightAnswerkey = sjRightAnswerkey;
	}

	public String getSjRightAnswerkeyid() {
		return this.sjRightAnswerkeyid;
	}

	public void setSjRightAnswerkeyid(String sjRightAnswerkeyid) {
		this.sjRightAnswerkeyid = sjRightAnswerkeyid;
	}

	public String getSjAnswerkeyContent() {
		return this.sjAnswerkeyContent;
	}

	public void setSjAnswerkeyContent(String sjAnswerkeyContent) {
		this.sjAnswerkeyContent = sjAnswerkeyContent;
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

	public List<ExamTestpaperAnswerkey> getExamTestpaperAnswerkeies() {
		return this.examTestpaperAnswerkeies;
	}

	public void setExamTestpaperAnswerkeies(List<ExamTestpaperAnswerkey> examTestpaperAnswerkeies) {
		this.examTestpaperAnswerkeies = examTestpaperAnswerkeies;
	}

	public List<ExamMarkTheme> getExamMarkThemes() {
		return this.examMarkThemes;
	}

	public void setExamMarkThemes(List<ExamMarkTheme> examMarkThemes) {
		this.examMarkThemes = examMarkThemes;
	}

	public List<ExamUserAnswerkey> getExamUserAnswerkeies() {
		return this.examUserAnswerkeies;
	}

	public void setExamUserAnswerkeies(List<ExamUserAnswerkey> examUserAnswerkeies) {
		this.examUserAnswerkeies = examUserAnswerkeies;
	}

	public List<ExamThemeSkey> getExamThemeSkeies() {
		return this.examThemeSkeies;
	}

	public void setExamThemeSkeies(List<ExamThemeSkey> examThemeSkeies) {
		this.examThemeSkeies = examThemeSkeies;
	}

	public String getReviewComments() {
		return reviewComments;
	}

	public void setReviewComments(String reviewComments) {
		this.reviewComments = reviewComments;
	}

	public String getSignInTheme() {
		return signInTheme;
	}

	public void setSignInTheme(String signInTheme) {
		this.signInTheme = signInTheme;
	}

}