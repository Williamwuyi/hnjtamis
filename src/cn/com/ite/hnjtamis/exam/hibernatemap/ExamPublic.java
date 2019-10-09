package cn.com.ite.hnjtamis.exam.hibernatemap;

import java.util.*;
import java.util.HashSet;
import java.util.Set;

/**
 * ExamPublic entity. @author MyEclipse Persistence Tools
 */

public class ExamPublic implements java.io.Serializable {

	// Fields

	private String publicId;
	private String examTitle;
	private String examScope;
	private String examTypeId;
	private String examTypeName;
	private Integer examProperty;
	private String examOrgan;
	private String examSponsor;
	private String examExplain;
	private String examStartTime;
	private String examEndTime;
	private String examType;
	private String isReg;
	private String publicUser;
	private String publicTime;
	private String checkUser;
	private String checkUserId;
	private String checkTime;
	private String state;
	private String isDel;
	private String syncFlag;
	private String organId;
	private String organName;
	private String remark;
	private String relationId;
	private String relationType;
	private String lastUpdateDate;
	private String lastUpdatedBy;
	private String lastUpdatedIdBy;
	private String creationDate;
	private String createdBy;
	private String createdIdBy;
	private String planExamTime;//计划考试时间
	private String scoreStartTime;//达标有效期开始日期
	private String scoreEndTime;//达标有效期结束日期
	private String reachUserJoin;//已达标人员是否参加 10-不参加 20-参加
	private Set examArranges = new HashSet(0);
	
	private String stCreateNow = "F";//是否正在执行
	
	private List<ExamPublicSearchkey> examPublicSearchkeies = new ArrayList<ExamPublicSearchkey>(0);
	//private Set examPublicSearchkeies = new HashSet(0);
	//private Set examPublicUsers = new HashSet(0);
	private List<ExamPublicUser> examPublicUsers = new ArrayList<ExamPublicUser>(0);

	// Constructors

	/** default constructor */
	public ExamPublic() {
	}

	/** full constructor */
	public ExamPublic(String examTitle, String examScope, String examTypeId,
			String examTypeName, Integer examProperty, String examOrgan,
			String examSponsor, String examExplain, String examStartTime,
			String examEndTime, String examType, String isReg,
			String publicUser, String publicTime, String checkUser,
			String checkUserId, String checkTime, String state, String isDel,
			String syncFlag, String organId, String organName, String remark,
			String relationId, String relationType, String lastUpdateDate,
			String lastUpdatedBy, String lastUpdatedIdBy, String creationDate,
			String createdBy, String createdIdBy, Set examArranges,
			List<ExamPublicSearchkey> examPublicSearchkeies, List<ExamPublicUser> examPublicUsers,
			String planExamTime,String scoreStartTime,String scoreEndTime,String reachUserJoin) {
		this.examTitle = examTitle;
		this.examScope = examScope;
		this.examTypeId = examTypeId;
		this.examTypeName = examTypeName;
		this.examProperty = examProperty;
		this.examOrgan = examOrgan;
		this.examSponsor = examSponsor;
		this.examExplain = examExplain;
		this.examStartTime = examStartTime;
		this.examEndTime = examEndTime;
		this.examType = examType;
		this.isReg = isReg;
		this.publicUser = publicUser;
		this.publicTime = publicTime;
		this.checkUser = checkUser;
		this.checkUserId = checkUserId;
		this.checkTime = checkTime;
		this.state = state;
		this.isDel = isDel;
		this.syncFlag = syncFlag;
		this.organId = organId;
		this.organName = organName;
		this.remark = remark;
		this.relationId = relationId;
		this.relationType = relationType;
		this.lastUpdateDate = lastUpdateDate;
		this.lastUpdatedBy = lastUpdatedBy;
		this.lastUpdatedIdBy = lastUpdatedIdBy;
		this.creationDate = creationDate;
		this.createdBy = createdBy;
		this.createdIdBy = createdIdBy;
		this.examArranges = examArranges;
		this.examPublicSearchkeies = examPublicSearchkeies;
		this.examPublicUsers = examPublicUsers;
		this.planExamTime=planExamTime;
		this.scoreStartTime=scoreStartTime;
		this.scoreEndTime=scoreEndTime;
		this.reachUserJoin=reachUserJoin;
	}

	// Property accessors

	
	
	public String getPublicId() {
		return this.publicId;
	}

	public String getPlanExamTime() {
		return planExamTime;
	}

	public void setPlanExamTime(String planExamTime) {
		this.planExamTime = planExamTime;
	}

	public void setPublicId(String publicId) {
		this.publicId = publicId;
	}

	public String getExamTitle() {
		return this.examTitle;
	}

	public void setExamTitle(String examTitle) {
		this.examTitle = examTitle;
	}

	public String getExamScope() {
		return this.examScope;
	}

	public void setExamScope(String examScope) {
		this.examScope = examScope;
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

	public String getExamOrgan() {
		return this.examOrgan;
	}

	public void setExamOrgan(String examOrgan) {
		this.examOrgan = examOrgan;
	}

	public String getExamSponsor() {
		return this.examSponsor;
	}

	public void setExamSponsor(String examSponsor) {
		this.examSponsor = examSponsor;
	}

	public String getExamExplain() {
		return this.examExplain;
	}

	public void setExamExplain(String examExplain) {
		this.examExplain = examExplain;
	}

	public String getExamStartTime() {
		return this.examStartTime;
	}

	public void setExamStartTime(String examStartTime) {
		this.examStartTime = examStartTime;
	}

	public String getExamEndTime() {
		return this.examEndTime;
	}

	public void setExamEndTime(String examEndTime) {
		this.examEndTime = examEndTime;
	}

	public String getExamType() {
		return this.examType;
	}

	public void setExamType(String examType) {
		this.examType = examType;
	}

	public String getIsReg() {
		return this.isReg;
	}

	public void setIsReg(String isReg) {
		this.isReg = isReg;
	}

	public String getPublicUser() {
		return this.publicUser;
	}

	public void setPublicUser(String publicUser) {
		this.publicUser = publicUser;
	}

	public String getPublicTime() {
		return this.publicTime;
	}

	public void setPublicTime(String publicTime) {
		this.publicTime = publicTime;
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

	public String getIsDel() {
		return this.isDel;
	}

	public void setIsDel(String isDel) {
		this.isDel = isDel;
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

	public Set getExamArranges() {
		return this.examArranges;
	}

	public void setExamArranges(Set examArranges) {
		this.examArranges = examArranges;
	}

	public List<ExamPublicSearchkey> getExamPublicSearchkeies() {
		return examPublicSearchkeies;
	}

	public void setExamPublicSearchkeies(
			List<ExamPublicSearchkey> examPublicSearchkeies) {
		this.examPublicSearchkeies = examPublicSearchkeies;
	}

	public List<ExamPublicUser> getExamPublicUsers() {
		return this.examPublicUsers;
	}

	public void setExamPublicUsers(List<ExamPublicUser> examPublicUsers) {
		this.examPublicUsers = examPublicUsers;
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

	public String getReachUserJoin() {
		return reachUserJoin;
	}

	public void setReachUserJoin(String reachUserJoin) {
		this.reachUserJoin = reachUserJoin;
	}

	public String getStCreateNow() {
		return stCreateNow;
	}

	public void setStCreateNow(String stCreateNow) {
		this.stCreateNow = stCreateNow;
	}

	
}