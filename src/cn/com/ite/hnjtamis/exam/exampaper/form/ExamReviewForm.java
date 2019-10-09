package cn.com.ite.hnjtamis.exam.exampaper.form;

import java.util.ArrayList;
import java.util.List;

import cn.com.ite.hnjtamis.exam.hibernatemap.Testpaper;


/**
 *
 * <p>Title cn.com.ite.hnjtamis.exam.exampaper.form.ExamForm</p>
 * <p>Description 考试科目Form</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2015</p>
 * @author 朱健
 * @create time: 2015年4月7日 下午4:04:58
 * @version 1.0
 * 
 * @modified records:
 */
public class ExamReviewForm {

	private String pid;//父ID
	private String examId;//考试科目ID
	private String examArrangeName;//考试名称
	private String examName;//考试科目名称
	private Integer examPaperType;//考试方式 10-正式（同试卷打乱） 11-正式（各考生随机抽题） 20-模拟
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
	
	private String reviewStartTime;//阅卷开始时间
	private String reviewEndTime;//阅卷结束时间
	private String needReviewCount;//待阅卷数量
	private String unReviewCount;//还未提交待阅卷数量
	private String succReviewCount;//已阅卷数量
	private String currentTime;//当前时间
	private boolean allowReview = false;
	private boolean isMain = false;
	private String showRelease;
	
	private List<ExamReviewForm> examReviewList = new ArrayList<ExamReviewForm>();
	private ExamTestpaperForm testpaper;
	
	
	private List<ExamUserForm> examUserFormList = new ArrayList<ExamUserForm>();
	
	
	
	public String getExamArrangeName() {
		return examArrangeName;
	}
	public void setExamArrangeName(String examArrangeName) {
		this.examArrangeName = examArrangeName;
	}
	public String getUnReviewCount() {
		return unReviewCount;
	}
	public void setUnReviewCount(String unReviewCount) {
		this.unReviewCount = unReviewCount;
	}
	public String getSuccReviewCount() {
		return succReviewCount;
	}
	public void setSuccReviewCount(String succReviewCount) {
		this.succReviewCount = succReviewCount;
	}
	public List<ExamUserForm> getExamUserFormList() {
		return examUserFormList;
	}
	public void setExamUserFormList(List<ExamUserForm> examUserFormList) {
		this.examUserFormList = examUserFormList;
	}
	public ExamTestpaperForm getTestpaper() {
		return testpaper;
	}
	public void setTestpaper(ExamTestpaperForm testpaper) {
		this.testpaper = testpaper;
	}
	
	public List<ExamReviewForm> getExamReviewList() {
		return examReviewList;
	}
	public void setExamReviewList(List<ExamReviewForm> examReviewList) {
		this.examReviewList = examReviewList;
	}
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
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
	public String getExamId() {
		return examId;
	}
	public void setExamId(String examId) {
		this.examId = examId;
	}
	public String getExamName() {
		return examName;
	}
	public void setExamName(String examName) {
		this.examName = examName;
	}
	public Integer getExamPaperType() {
		return examPaperType;
	}
	public void setExamPaperType(Integer examPaperType) {
		this.examPaperType = examPaperType;
	}
	public String getExamCode() {
		return examCode;
	}
	public void setExamCode(String examCode) {
		this.examCode = examCode;
	}
	public Double getScore() {
		return score;
	}
	public void setScore(Double score) {
		this.score = score;
	}
	public Integer getIsPublic() {
		return isPublic;
	}
	public void setIsPublic(Integer isPublic) {
		this.isPublic = isPublic;
	}
	public Short getBanTime() {
		return banTime;
	}
	public void setBanTime(Short banTime) {
		this.banTime = banTime;
	}
	public Short getBeforeTime() {
		return beforeTime;
	}
	public void setBeforeTime(Short beforeTime) {
		this.beforeTime = beforeTime;
	}
	public Double getPassScore() {
		return passScore;
	}
	public void setPassScore(Double passScore) {
		this.passScore = passScore;
	}
	public String getPublicUser() {
		return publicUser;
	}
	public void setPublicUser(String publicUser) {
		this.publicUser = publicUser;
	}
	public String getPublicUserId() {
		return publicUserId;
	}
	public void setPublicUserId(String publicUserId) {
		this.publicUserId = publicUserId;
	}
	public String getPublicTime() {
		return publicTime;
	}
	public void setPublicTime(String publicTime) {
		this.publicTime = publicTime;
	}
	public String getIsCutScreen() {
		return isCutScreen;
	}
	public void setIsCutScreen(String isCutScreen) {
		this.isCutScreen = isCutScreen;
	}
	public Integer getIsUse() {
		return isUse;
	}
	public void setIsUse(Integer isUse) {
		this.isUse = isUse;
	}
	public String getCheckUser() {
		return checkUser;
	}
	public void setCheckUser(String checkUser) {
		this.checkUser = checkUser;
	}
	public String getCheckUserId() {
		return checkUserId;
	}
	public void setCheckUserId(String checkUserId) {
		this.checkUserId = checkUserId;
	}
	public String getCheckTime() {
		return checkTime;
	}
	public void setCheckTime(String checkTime) {
		this.checkTime = checkTime;
	}
	public String getMarkScoreType() {
		return markScoreType;
	}
	public void setMarkScoreType(String markScoreType) {
		this.markScoreType = markScoreType;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
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
	public String getOrganName() {
		return organName;
	}
	public void setOrganName(String organName) {
		this.organName = organName;
	}
	public String getOrganId() {
		return organId;
	}
	public void setOrganId(String organId) {
		this.organId = organId;
	}
	public String getSyncFlag() {
		return syncFlag;
	}
	public void setSyncFlag(String syncFlag) {
		this.syncFlag = syncFlag;
	}
	public String getLastUpdateDate() {
		return lastUpdateDate;
	}
	public void setLastUpdateDate(String lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}
	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}
	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}
	public String getLastUpdatedIdBy() {
		return lastUpdatedIdBy;
	}
	public void setLastUpdatedIdBy(String lastUpdatedIdBy) {
		this.lastUpdatedIdBy = lastUpdatedIdBy;
	}
	public String getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getCreatedIdBy() {
		return createdIdBy;
	}
	public void setCreatedIdBy(String createdIdBy) {
		this.createdIdBy = createdIdBy;
	}
	public String getReviewStartTime() {
		return reviewStartTime;
	}
	public void setReviewStartTime(String reviewStartTime) {
		this.reviewStartTime = reviewStartTime;
	}
	public String getReviewEndTime() {
		return reviewEndTime;
	}
	public void setReviewEndTime(String reviewEndTime) {
		this.reviewEndTime = reviewEndTime;
	}
	public String getNeedReviewCount() {
		return needReviewCount;
	}
	public void setNeedReviewCount(String needReviewCount) {
		this.needReviewCount = needReviewCount;
	}
	public String getCurrentTime() {
		return currentTime;
	}
	public void setCurrentTime(String currentTime) {
		this.currentTime = currentTime;
	}
	public boolean isAllowReview() {
		return allowReview;
	}
	public void setAllowReview(boolean allowReview) {
		this.allowReview = allowReview;
	}
	public String getShowRelease() {
		return showRelease;
	}
	public void setShowRelease(String showRelease) {
		this.showRelease = showRelease;
	}
	public boolean getIsMain() {
		return isMain;
	}
	public void setIsMain(boolean isMain) {
		this.isMain = isMain;
	}
	
	
}
