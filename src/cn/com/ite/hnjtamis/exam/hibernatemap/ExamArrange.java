package cn.com.ite.hnjtamis.exam.hibernatemap;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * ExamArrange entity. @author MyEclipse Persistence Tools
 */

public class ExamArrange implements java.io.Serializable {

	// Fields

	private String examArrangeId;//考试安排ID
	private ExamPublic examPublic;//发布ID
	private String examName;//考试名称
	private Double score;//总分
	private String examCode;//考试编码 考试类型编码 + 日期 + 序号（2位）
	private String examTypeId;//考试类型(性质)ID，数据字典定义（安规、岗位、竞赛等）
	private String examTypeName;//考试类型(性质)，与考试类型ID一组
	private Integer examProperty;//考试类型  10- 达标考试 20-竞赛考试 30-培训测试 40-模拟考试
	private Integer isPublic;//是否发布成绩 5：否，10：是
	private String publicUser;//成绩发布人
	private String publicUserId;//成绩发布人编号
	private String publicTime;//成绩发布时间
	private Integer isUse;///是否使用 5：否,10：是
	private String checkUser;//审核人
	private String checkUserId;///审核人ID
	private String checkTime;//审核时间
	private String state;//状态 5：未上报，10：等待审核，15：审核通过，20：审核打回 30-发布
	private Integer examPaperType;//考试方式 10-正式 20-模拟
	private String isIdNumberLogin;//是否允许身份证登陆 0-允许  1-不允许
	private String relationId;//关联ID（如：练习安排等）
	private String relationType;//关联类型
	private String remark;//备注
	private String organName;//机构名
	private String organId;//机构编号
	private String syncFlag;////同步标志
	private String lastUpdateDate;///最后修改时间
	private String lastUpdatedBy;//最后修改人
	private String lastUpdatedIdBy;//最后修改人ID
	private String creationDate;//创建时间
	private String createdBy;//创建人
	private String createdIdBy;//创建人ID
	private String scoreStartTime;//达标有效期开始日期
	private String scoreEndTime;//达标有效期结束日期
	private List<Exam> exams = new ArrayList<Exam>(0);//考试科目

	// Constructors

	/** default constructor */
	public ExamArrange() {
	}

	/** full constructor */
	public ExamArrange(ExamPublic examPublic, String examName, Double score,
			String examCode, String examTypeId, String examTypeName,
			Integer examProperty, Integer isPublic, String publicUser,
			String publicUserId, String publicTime, Integer isUse,
			String checkUser, String checkUserId, String checkTime,
			String state, Integer examPaperType, String isIdNumberLogin,
			String relationId, String relationType, String remark,
			String organName, String organId, String syncFlag,
			String lastUpdateDate, String lastUpdatedBy,
			String lastUpdatedIdBy, String creationDate, String createdBy,
			String createdIdBy, List<Exam> exams,String scoreStartTime,String scoreEndTime) {
		this.examPublic = examPublic;
		this.examName = examName;
		this.score = score;
		this.examCode = examCode;
		this.examTypeId = examTypeId;
		this.examTypeName = examTypeName;
		this.examProperty = examProperty;
		this.isPublic = isPublic;
		this.publicUser = publicUser;
		this.publicUserId = publicUserId;
		this.publicTime = publicTime;
		this.isUse = isUse;
		this.checkUser = checkUser;
		this.checkUserId = checkUserId;
		this.checkTime = checkTime;
		this.state = state;
		this.examPaperType = examPaperType;
		this.isIdNumberLogin = isIdNumberLogin;
		this.relationId = relationId;
		this.relationType = relationType;
		this.remark = remark;
		this.organName = organName;
		this.organId = organId;
		this.syncFlag = syncFlag;
		this.lastUpdateDate = lastUpdateDate;
		this.lastUpdatedBy = lastUpdatedBy;
		this.lastUpdatedIdBy = lastUpdatedIdBy;
		this.creationDate = creationDate;
		this.createdBy = createdBy;
		this.createdIdBy = createdIdBy;
		this.exams = exams;
		this.scoreStartTime=scoreStartTime;
		this.scoreEndTime=scoreEndTime;
	}

	// Property accessors

	public String getExamArrangeId() {
		return this.examArrangeId;
	}

	public void setExamArrangeId(String examArrangeId) {
		this.examArrangeId = examArrangeId;
	}

	public ExamPublic getExamPublic() {
		return this.examPublic;
	}

	public void setExamPublic(ExamPublic examPublic) {
		this.examPublic = examPublic;
	}

	public String getExamName() {
		return this.examName;
	}

	public void setExamName(String examName) {
		this.examName = examName;
	}

	public Double getScore() {
		return this.score;
	}

	public void setScore(Double score) {
		this.score = score;
	}

	public String getExamCode() {
		return this.examCode;
	}

	public void setExamCode(String examCode) {
		this.examCode = examCode;
	}

	public String getExamTypeId() {
		return this.examTypeId;
	}

	public void setExamTypeId(String examTypeId) {
		this.examTypeId = examTypeId;
	}

	public String getExamTypeName() {
		return this.examTypeName;
	}

	public void setExamTypeName(String examTypeName) {
		this.examTypeName = examTypeName;
	}

	public Integer getExamProperty() {
		return this.examProperty;
	}

	public void setExamProperty(Integer examProperty) {
		this.examProperty = examProperty;
	}

	public Integer getIsPublic() {
		return this.isPublic;
	}

	public void setIsPublic(Integer isPublic) {
		this.isPublic = isPublic;
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

	public String getState() {
		return this.state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Integer getExamPaperType() {
		return this.examPaperType;
	}

	public void setExamPaperType(Integer examPaperType) {
		this.examPaperType = examPaperType;
	}

	public String getIsIdNumberLogin() {
		return this.isIdNumberLogin;
	}

	public void setIsIdNumberLogin(String isIdNumberLogin) {
		this.isIdNumberLogin = isIdNumberLogin;
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

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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

	public List<Exam> getExams() {
		return this.exams;
	}

	public void setExams(List<Exam> exams) {
		this.exams = exams;
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