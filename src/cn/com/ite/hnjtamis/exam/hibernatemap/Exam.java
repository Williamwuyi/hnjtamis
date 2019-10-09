package cn.com.ite.hnjtamis.exam.hibernatemap;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Exam entity. @author MyEclipse Persistence Tools
 */

public class Exam implements java.io.Serializable {

	// Fields

	private String examId;//考试科目ID
	private String examName;//考试科目
	private ExamArrange examArrange;//考试安排ID
	private Testpaper testpaper;//试卷模版ID
	private Integer examPaperType;//试卷生成方式  10-同试卷打乱  20-同试卷同顺序 30-各考生按模版随机抽题
	private String examCode;//科目编码  考试安排编码+二位顺序号
	private Double score;//科目总分
	private Integer isPublic;//是否发布成绩 5：否，10：是
	private Short banTime;//禁止进入时间_分钟
	private Short beforeTime;//提前进入时间_分钟
	private Double passScore;//合格分数线 默认按总分的60％，可进行设置。
	private String publicUser;//成绩发布人
	private String publicUserId;//成绩发布人ID
	private String publicTime;//成绩发布时间
	private String isCutScreen;//是否截取图片 0：否；5是
	private Integer isUse;///是否使用 5：否,10：是
	private String checkUser;//审核人
	private String checkUserId;///审核人ID
	private String checkTime;//审核时间
	private String markScoreType;//评分方式 10-考完系统立即算分  20-待审核后算法
	private String state;//状态 5：未上报，10：等待审核，15：审核通过，20：审核打回
	private String remark;//备注
	private String relationId;//关联ID（如：练习安排等）
	private String relationType;//关联类型
	private String organName;//机构名
	private String organId;//机构编号
	private String syncFlag;////同步标志
	private String lastUpdateDate;///最后修改时间
	private String lastUpdatedBy;//最后修改人
	private String lastUpdatedIdBy;//最后修改人ID
	private String creationDate;//创建时间
	private String createdBy;//创建人
	private String createdIdBy;//创建人ID
	private String examStartTime;//考试开始时间
	private String examEndTime;//考试结束时间
	private String scoreStartTime;//达标有效期开始日期
	private String scoreEndTime;//达标有效期结束日期
	private List<ExamSkey> examSkeies = new ArrayList<ExamSkey>(0);
	private List<ExamUserTestpaper> examUserTestpapers = new ArrayList<ExamUserTestpaper>(0);
	private List<ExamMarkpeople> examMarkpeoples = new ArrayList<ExamMarkpeople>(0);
	private List<ExamExamroot> examExamroots = new ArrayList<ExamExamroot>(0);

	/** default constructor */
	public Exam() {
	}

	/** full constructor */
	public Exam(String examName,ExamArrange examArrange, Testpaper testpaper,
			Integer examPaperType, String examCode, Double score, Integer isPublic,
			Short banTime, Short beforeTime, Double passScore,
			String publicUser, String publicUserId, String publicTime,
			String isCutScreen, Integer isUse, String checkUser,
			String checkUserId, String checkTime, String markScoreType,
			String state, String remark, String relationId,
			String relationType, String organName, String organId,
			String syncFlag, String lastUpdateDate, String lastUpdatedBy,
			String lastUpdatedIdBy, String creationDate, String createdBy,
			String createdIdBy, List<ExamSkey> examSkeies, List<ExamUserTestpaper> examUserTestpapers,
			List<ExamMarkpeople> examMarkpeoples, List<ExamExamroot> examExamroots,
			String examStartTime,String examEndTime,
			String scoreStartTime,String scoreEndTime) {
		this.examName=examName;
		this.examArrange = examArrange;
		this.testpaper = testpaper;
		this.examPaperType = examPaperType;
		this.examCode = examCode;
		this.score = score;
		this.isPublic = isPublic;
		this.banTime = banTime;
		this.beforeTime = beforeTime;
		this.passScore = passScore;
		this.publicUser = publicUser;
		this.publicUserId = publicUserId;
		this.publicTime = publicTime;
		this.isCutScreen = isCutScreen;
		this.isUse = isUse;
		this.checkUser = checkUser;
		this.checkUserId = checkUserId;
		this.checkTime = checkTime;
		this.markScoreType = markScoreType;
		this.state = state;
		this.remark = remark;
		this.relationId = relationId;
		this.relationType = relationType;
		this.organName = organName;
		this.organId = organId;
		this.syncFlag = syncFlag;
		this.lastUpdateDate = lastUpdateDate;
		this.lastUpdatedBy = lastUpdatedBy;
		this.lastUpdatedIdBy = lastUpdatedIdBy;
		this.creationDate = creationDate;
		this.createdBy = createdBy;
		this.createdIdBy = createdIdBy;
		this.examSkeies = examSkeies;
		this.examUserTestpapers = examUserTestpapers;
		this.examMarkpeoples = examMarkpeoples;
		this.examExamroots = examExamroots;
		this.examStartTime=examStartTime;
		this.examEndTime=examEndTime;
		this.scoreStartTime=scoreStartTime;
		this.scoreEndTime=scoreEndTime;
	}

	// Property accessors
	
	
	public String getExamName() {
		return examName;
	}

	public String getExamStartTime() {
		return examStartTime;
	}

	public void setExamStartTime(String examStartTime) {
		this.examStartTime = examStartTime;
	}

	public String getExamEndTime() {
		return examEndTime;
	}

	public void setExamEndTime(String examEndTime) {
		this.examEndTime = examEndTime;
	}

	public void setExamName(String examName) {
		this.examName = examName;
	}
	
	public String getExamId() {
		return this.examId;
	}
	
	public void setExamId(String examId) {
		this.examId = examId;
	}

	public ExamArrange getExamArrange() {
		return this.examArrange;
	}

	public void setExamArrange(ExamArrange examArrange) {
		this.examArrange = examArrange;
	}

	public Testpaper getTestpaper() {
		return this.testpaper;
	}

	public void setTestpaper(Testpaper testpaper) {
		this.testpaper = testpaper;
	}

	public Integer getExamPaperType() {
		return this.examPaperType;
	}

	public void setExamPaperType(Integer examPaperType) {
		this.examPaperType = examPaperType;
	}

	public String getExamCode() {
		return this.examCode;
	}

	public void setExamCode(String examCode) {
		this.examCode = examCode;
	}

	public Double getScore() {
		return this.score;
	}

	public void setScore(Double score) {
		this.score = score;
	}

	public Integer getIsPublic() {
		return this.isPublic;
	}

	public void setIsPublic(Integer isPublic) {
		this.isPublic = isPublic;
	}

	public Short getBanTime() {
		return this.banTime;
	}

	public void setBanTime(Short banTime) {
		this.banTime = banTime;
	}

	public Short getBeforeTime() {
		return this.beforeTime;
	}

	public void setBeforeTime(Short beforeTime) {
		this.beforeTime = beforeTime;
	}

	public Double getPassScore() {
		return this.passScore;
	}

	public void setPassScore(Double passScore) {
		this.passScore = passScore;
	}

	public String getPublicUser() {
		return this.publicUser;
	}

	public void setPublicUser(String publicUser) {
		this.publicUser = publicUser;
	}

	public String getPublicUserId() {
		return this.publicUserId;
	}

	public void setPublicUserId(String publicUserId) {
		this.publicUserId = publicUserId;
	}

	public String getPublicTime() {
		return this.publicTime;
	}

	public void setPublicTime(String publicTime) {
		this.publicTime = publicTime;
	}

	public String getIsCutScreen() {
		return this.isCutScreen;
	}

	public void setIsCutScreen(String isCutScreen) {
		this.isCutScreen = isCutScreen;
	}

	public Integer getIsUse() {
		return this.isUse;
	}

	public void setIsUse(Integer isUse) {
		this.isUse = isUse;
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

	public String getMarkScoreType() {
		return this.markScoreType;
	}

	public void setMarkScoreType(String markScoreType) {
		this.markScoreType = markScoreType;
	}

	public String getState() {
		return this.state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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

	public List<ExamSkey> getExamSkeies() {
		return this.examSkeies;
	}

	public void setExamSkeies(List<ExamSkey> examSkeies) {
		this.examSkeies = examSkeies;
	}

	public List<ExamUserTestpaper> getExamUserTestpapers() {
		return this.examUserTestpapers;
	}

	public void setExamUserTestpapers(List<ExamUserTestpaper> examUserTestpapers) {
		this.examUserTestpapers = examUserTestpapers;
	}

	public List<ExamMarkpeople> getExamMarkpeoples() {
		return this.examMarkpeoples;
	}

	public void setExamMarkpeoples(List<ExamMarkpeople> examMarkpeoples) {
		this.examMarkpeoples = examMarkpeoples;
	}

	public List<ExamExamroot> getExamExamroots() {
		return this.examExamroots;
	}

	public void setExamExamroots(List<ExamExamroot> examExamroots) {
		this.examExamroots = examExamroots;
	}

	public String getScoreStartTime() {
		return scoreStartTime;
	}

	public void setScoreStartTime(String scoreStartTime) {
		this.scoreStartTime = scoreStartTime;
	}

	public String getScoreEndTime() {
		return scoreEndTime;
	}

	public void setScoreEndTime(String scoreEndTime) {
		this.scoreEndTime = scoreEndTime;
	}
	

}