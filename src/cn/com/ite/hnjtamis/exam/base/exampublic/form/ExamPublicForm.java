package cn.com.ite.hnjtamis.exam.base.exampublic.form;

import java.util.ArrayList;
import java.util.List;

import cn.com.ite.eap2.domain.organization.Organ;
import cn.com.ite.hnjtamis.baseinfo.domain.Speciality;
import cn.com.ite.hnjtamis.exam.base.theme.form.ThemePostForm;
import cn.com.ite.hnjtamis.exam.base.theme.form.ThemeStandardQuarterForm;
import cn.com.ite.hnjtamis.exam.hibernatemap.ThemeBank;

public class ExamPublicForm {
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
	
	private List<Organ> organs = new ArrayList<Organ>();//参加的机构
	private List<Speciality> specialitys = new ArrayList<Speciality>();//专业
	private List<ThemePostForm> themePostFormList = new ArrayList<ThemePostForm>();;//岗位
	private List<ThemeStandardQuarterForm> standardQuarterList = new ArrayList<ThemeStandardQuarterForm>();//标准岗位信息
	private ThemeBank themeBank;//题库
	
	
	
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
	public List<Organ> getOrgans() {
		return organs;
	}
	public void setOrgans(List<Organ> organs) {
		this.organs = organs;
	}
	public String getPlanExamTime() {
		return planExamTime;
	}
	public void setPlanExamTime(String planExamTime) {
		this.planExamTime = planExamTime;
	}
	public String getPublicId() {
		return publicId;
	}
	public void setPublicId(String publicId) {
		this.publicId = publicId;
	}
	public String getExamTitle() {
		return examTitle;
	}
	public void setExamTitle(String examTitle) {
		this.examTitle = examTitle;
	}
	public String getExamScope() {
		return examScope;
	}
	public void setExamScope(String examScope) {
		this.examScope = examScope;
	}
	public String getExamTypeId() {
		return examTypeId;
	}
	public void setExamTypeId(String examTypeId) {
		this.examTypeId = examTypeId;
	}
	public String getExamTypeName() {
		return examTypeName;
	}
	public void setExamTypeName(String examTypeName) {
		this.examTypeName = examTypeName;
	}
	public Integer getExamProperty() {
		return examProperty;
	}
	public void setExamProperty(Integer examProperty) {
		this.examProperty = examProperty;
	}
	public String getExamOrgan() {
		return examOrgan;
	}
	public void setExamOrgan(String examOrgan) {
		this.examOrgan = examOrgan;
	}
	public String getExamSponsor() {
		return examSponsor;
	}
	public void setExamSponsor(String examSponsor) {
		this.examSponsor = examSponsor;
	}
	public String getExamExplain() {
		return examExplain;
	}
	public void setExamExplain(String examExplain) {
		this.examExplain = examExplain;
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
	public String getExamType() {
		return examType;
	}
	public void setExamType(String examType) {
		this.examType = examType;
	}
	public String getIsReg() {
		return isReg;
	}
	public void setIsReg(String isReg) {
		this.isReg = isReg;
	}
	public String getPublicUser() {
		return publicUser;
	}
	public void setPublicUser(String publicUser) {
		this.publicUser = publicUser;
	}
	public String getPublicTime() {
		return publicTime;
	}
	public void setPublicTime(String publicTime) {
		this.publicTime = publicTime;
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
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getIsDel() {
		return isDel;
	}
	public void setIsDel(String isDel) {
		this.isDel = isDel;
	}
	public String getSyncFlag() {
		return syncFlag;
	}
	public void setSyncFlag(String syncFlag) {
		this.syncFlag = syncFlag;
	}
	public String getOrganId() {
		return organId;
	}
	public void setOrganId(String organId) {
		this.organId = organId;
	}
	public String getOrganName() {
		return organName;
	}
	public void setOrganName(String organName) {
		this.organName = organName;
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
	public List<Speciality> getSpecialitys() {
		return specialitys;
	}
	public void setSpecialitys(List<Speciality> specialitys) {
		this.specialitys = specialitys;
	}
	public List<ThemePostForm> getThemePostFormList() {
		return themePostFormList;
	}
	public void setThemePostFormList(List<ThemePostForm> themePostFormList) {
		this.themePostFormList = themePostFormList;
	}
	public ThemeBank getThemeBank() {
		return themeBank;
	}
	public void setThemeBank(ThemeBank themeBank) {
		this.themeBank = themeBank;
	}
	public List<ThemeStandardQuarterForm> getStandardQuarterList() {
		return standardQuarterList;
	}
	public void setStandardQuarterList(
			List<ThemeStandardQuarterForm> standardQuarterList) {
		this.standardQuarterList = standardQuarterList;
	}
	
	
}
