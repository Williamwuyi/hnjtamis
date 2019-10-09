package cn.com.ite.hnjtamis.exam.hibernatemap;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * ExamTestpaper entity. @author MyEclipse Persistence Tools
 */

public class ExamTestpaper implements java.io.Serializable {

	// Fields

	private String examTestpaperId;
	private String examTestpaperName;//试卷名称
	private Short totalTheme;//总题数
	private Double totalScore;//总分数
	private Double testpaperRank;//难度系数
	private Integer screeningMethods;//筛选方式(5：分数，10：题数)
	private Short testpaperTime;//参考考时（分钟）
	private Integer isUse;//是否使用(5：否,10：是)
	private String isPrivate;//是否私有
	private String remark;//
	private String checkUser;//
	private String checkUserId;
	private String checkTime;
	private String checkIdear;
	private String state;
	private Integer useNum;
	private String markScoreType;
	private String organName;
	private String organId;
	private String syncFlag;
	private String lastUpdateDate;
	private String lastUpdatedBy;
	private String lastUpdatedIdBy;
	private String creationDate;
	private String createdBy;
	private String createdIdBy;
	private String examFileTitle;
	private String examFileAnswerkey;
	private String examFileUserAnswerkey;
	private String userId;
	private String examArrangeId;
	private String examId;
	private String testpaperId;
	private String relationId;//关联ID（如：练习安排等）
	private String relationType;//关联类型
	private Double examScore;//考生得分
	private String inTime;//入场时间
	private String outTime;//离场时间
	private String subTime;//交卷时间
	private String employeeId;//人员ID
	private String employeeName;//人员
	private List<ExamUserTestpaper> examUserTestpapers = new ArrayList<ExamUserTestpaper>(0);
	private List<ExamTestpaperSearchkey> examTestpaperSearchkeies = new ArrayList<ExamTestpaperSearchkey>(0);
	private List<ExamTestpaperTheme> examTestpaperThemes = new ArrayList<ExamTestpaperTheme>(0);

	// Constructors

	/** default constructor */
	public ExamTestpaper() {
	}

	/** full constructor */
	public ExamTestpaper(String examTestpaperName, Short totalTheme,
			Double totalScore, Double testpaperRank, Integer screeningMethods,
			Short testpaperTime, Integer isUse, String isPrivate, String remark,
			String checkUser, String checkUserId, String checkTime,
			String checkIdear, String state, Integer useNum,
			String markScoreType, String organName, String organId,
			String syncFlag, String lastUpdateDate, String lastUpdatedBy,
			String lastUpdatedIdBy, String creationDate, String createdBy,
			String createdIdBy, String examFileTitle, String examFileAnswerkey,
			String examFileUserAnswerkey, String userId, String examArrangeId,String relationId, String relationType,
			String examId, String testpaperId, List<ExamUserTestpaper> examUserTestpapers,
			List<ExamTestpaperSearchkey> examTestpaperSearchkeies, List<ExamTestpaperTheme> examTestpaperThemes,
			Double examScore,String inTime,String outTime,String subTime,String employeeId,String employeeName) {
		this.examTestpaperName = examTestpaperName;
		this.totalTheme = totalTheme;
		this.totalScore = totalScore;
		this.testpaperRank = testpaperRank;
		this.screeningMethods = screeningMethods;
		this.testpaperTime = testpaperTime;
		this.isUse = isUse;
		this.isPrivate = isPrivate;
		this.remark = remark;
		this.checkUser = checkUser;
		this.checkUserId = checkUserId;
		this.checkTime = checkTime;
		this.checkIdear = checkIdear;
		this.state = state;
		this.useNum = useNum;
		this.markScoreType = markScoreType;
		this.organName = organName;
		this.organId = organId;
		this.syncFlag = syncFlag;
		this.lastUpdateDate = lastUpdateDate;
		this.lastUpdatedBy = lastUpdatedBy;
		this.lastUpdatedIdBy = lastUpdatedIdBy;
		this.creationDate = creationDate;
		this.createdBy = createdBy;
		this.createdIdBy = createdIdBy;
		this.examFileTitle = examFileTitle;
		this.examFileAnswerkey = examFileAnswerkey;
		this.examFileUserAnswerkey = examFileUserAnswerkey;
		this.userId = userId;
		this.examArrangeId = examArrangeId;
		this.examId = examId;
		this.testpaperId = testpaperId;
		this.examUserTestpapers = examUserTestpapers;
		this.examTestpaperSearchkeies = examTestpaperSearchkeies;
		this.examTestpaperThemes = examTestpaperThemes;
		this.relationId = relationId;
		this.relationType = relationType;
		this.examScore = examScore;
		this.inTime = inTime;
		this.outTime = outTime;
		this.subTime=subTime;
		this.employeeId=employeeId;
		this.employeeName=employeeName;
		
	}

	// Property accessors

	
	
	public String getExamTestpaperId() {
		return this.examTestpaperId;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getSubTime() {
		return subTime;
	}

	public void setSubTime(String subTime) {
		this.subTime = subTime;
	}

	public Double getExamScore() {
		return examScore;
	}

	public void setExamScore(Double examScore) {
		this.examScore = examScore;
	}

	public String getInTime() {
		return inTime;
	}

	public void setInTime(String inTime) {
		this.inTime = inTime;
	}

	public String getOutTime() {
		return outTime;
	}

	public void setOutTime(String outTime) {
		this.outTime = outTime;
	}

	public String getRelationId() {
		return relationId;
	}

	public void setRelationId(String relationId) {
		this.relationId = relationId;
	}

	public String getRelationType() {
		return relationType;
	}

	public void setRelationType(String relationType) {
		this.relationType = relationType;
	}

	public void setExamTestpaperId(String examTestpaperId) {
		this.examTestpaperId = examTestpaperId;
	}

	public String getExamTestpaperName() {
		return this.examTestpaperName;
	}

	public void setExamTestpaperName(String examTestpaperName) {
		this.examTestpaperName = examTestpaperName;
	}

	public Short getTotalTheme() {
		return this.totalTheme;
	}

	public void setTotalTheme(Short totalTheme) {
		this.totalTheme = totalTheme;
	}

	public Double getTotalScore() {
		return this.totalScore;
	}

	public void setTotalScore(Double totalScore) {
		this.totalScore = totalScore;
	}

	public Double getTestpaperRank() {
		return this.testpaperRank;
	}

	public void setTestpaperRank(Double testpaperRank) {
		this.testpaperRank = testpaperRank;
	}

	public Integer getScreeningMethods() {
		return this.screeningMethods;
	}

	public void setScreeningMethods(Integer screeningMethods) {
		this.screeningMethods = screeningMethods;
	}

	public Short getTestpaperTime() {
		return this.testpaperTime;
	}

	public void setTestpaperTime(Short testpaperTime) {
		this.testpaperTime = testpaperTime;
	}

	public Integer getIsUse() {
		return this.isUse;
	}

	public void setIsUse(Integer isUse) {
		this.isUse = isUse;
	}

	public String getIsPrivate() {
		return this.isPrivate;
	}

	public void setIsPrivate(String isPrivate) {
		this.isPrivate = isPrivate;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getCheckUser() {
		return this.checkUser;
	}

	public void setCheckUser(String checkUser) {
		this.checkUser = checkUser;
	}

	public String getCheckUserId() {
		return this.checkUserId;
	}

	public void setCheckUserId(String checkUserId) {
		this.checkUserId = checkUserId;
	}

	public String getCheckTime() {
		return this.checkTime;
	}

	public void setCheckTime(String checkTime) {
		this.checkTime = checkTime;
	}

	public String getCheckIdear() {
		return this.checkIdear;
	}

	public void setCheckIdear(String checkIdear) {
		this.checkIdear = checkIdear;
	}

	public String getState() {
		return this.state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Integer getUseNum() {
		return this.useNum;
	}

	public void setUseNum(Integer useNum) {
		this.useNum = useNum;
	}

	public String getMarkScoreType() {
		return this.markScoreType;
	}

	public void setMarkScoreType(String markScoreType) {
		this.markScoreType = markScoreType;
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

	public String getExamFileTitle() {
		return this.examFileTitle;
	}

	public void setExamFileTitle(String examFileTitle) {
		this.examFileTitle = examFileTitle;
	}

	public String getExamFileAnswerkey() {
		return this.examFileAnswerkey;
	}

	public void setExamFileAnswerkey(String examFileAnswerkey) {
		this.examFileAnswerkey = examFileAnswerkey;
	}

	public String getExamFileUserAnswerkey() {
		return this.examFileUserAnswerkey;
	}

	public void setExamFileUserAnswerkey(String examFileUserAnswerkey) {
		this.examFileUserAnswerkey = examFileUserAnswerkey;
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

	public List<ExamUserTestpaper> getExamUserTestpapers() {
		return this.examUserTestpapers;
	}

	public void setExamUserTestpapers(List<ExamUserTestpaper> examUserTestpapers) {
		this.examUserTestpapers = examUserTestpapers;
	}

	public List<ExamTestpaperSearchkey> getExamTestpaperSearchkeies() {
		return this.examTestpaperSearchkeies;
	}

	public void setExamTestpaperSearchkeies(List<ExamTestpaperSearchkey> examTestpaperSearchkeies) {
		this.examTestpaperSearchkeies = examTestpaperSearchkeies;
	}

	public List<ExamTestpaperTheme> getExamTestpaperThemes() {
		return this.examTestpaperThemes;
	}

	public void setExamTestpaperThemes(List<ExamTestpaperTheme> examTestpaperThemes) {
		this.examTestpaperThemes = examTestpaperThemes;
	}

}