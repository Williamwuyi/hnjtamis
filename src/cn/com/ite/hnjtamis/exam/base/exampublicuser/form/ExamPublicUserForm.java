package cn.com.ite.hnjtamis.exam.base.exampublicuser.form;

import java.util.*;

import cn.com.ite.eap2.domain.organization.Dept;
import cn.com.ite.eap2.domain.organization.Employee;
import cn.com.ite.eap2.domain.organization.Organ;
import cn.com.ite.eap2.domain.organization.Quarter;
import cn.com.ite.hnjtamis.exam.hibernatemap.ExamPublic;
import cn.com.ite.hnjtamis.exam.hibernatemap.ExamUserTestpaper;

public class ExamPublicUserForm {
	private String userId;
	private String  examPublicId;
	private String examPublicName;
	private Organ organ;
	private Dept dept;
	private Quarter quarter;
	private Employee employee;
	
	//------
	private String userOrganName;
	private String userDeptName;
	private String postName;
	//------
	
	private String userName;
	private String userSex;
	private String userBirthday;
	private String userNation;
	private String userAddr;
	private String userPhone;
	private String userInfo;
	private Double score;
	private String inticket;
	private String idNumber;
	private String examPassword;
	private String checkUser;
	private String checkUserId;
	private String checkTime;
	private String state;
	private String isDel;
	private String syncFlag;
	private String isExp;
	private String organId;
	private String organName;
	private String remark;
	private String lastUpdateDate;
	private String lastUpdatedBy;
	private String lastUpdatedIdBy;
	private String creationDate;
	private String createdBy;
	private String createdIdBy;
	private String bmType;//报名方式 10-系统要求 20-自主报名
	private List<ExamUserTestpaper> pwdList = new ArrayList<ExamUserTestpaper>();
	
	
	public String getExamPublicName() {
		return examPublicName;
	}
	public void setExamPublicName(String examPublicName) {
		this.examPublicName = examPublicName;
	}
	public String getBmType() {
		return bmType;
	}
	public void setBmType(String bmType) {
		this.bmType = bmType;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public String getExamPublicId() {
		return examPublicId;
	}
	public void setExamPublicId(String examPublicId) {
		this.examPublicId = examPublicId;
	}
	public Organ getOrgan() {
		return organ;
	}
	public void setOrgan(Organ organ) {
		this.organ = organ;
	}
	public Dept getDept() {
		return dept;
	}
	public void setDept(Dept dept) {
		this.dept = dept;
	}
	public Quarter getQuarter() {
		return quarter;
	}
	public void setQuarter(Quarter quarter) {
		this.quarter = quarter;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserSex() {
		return userSex;
	}
	public void setUserSex(String userSex) {
		this.userSex = userSex;
	}
	public String getUserBirthday() {
		return userBirthday;
	}
	public void setUserBirthday(String userBirthday) {
		this.userBirthday = userBirthday;
	}
	public String getUserNation() {
		return userNation;
	}
	public void setUserNation(String userNation) {
		this.userNation = userNation;
	}
	public String getUserAddr() {
		return userAddr;
	}
	public void setUserAddr(String userAddr) {
		this.userAddr = userAddr;
	}
	public String getUserPhone() {
		return userPhone;
	}
	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}
	public String getUserInfo() {
		return userInfo;
	}
	public void setUserInfo(String userInfo) {
		this.userInfo = userInfo;
	}
	public Double getScore() {
		return score;
	}
	public void setScore(Double score) {
		this.score = score;
	}
	public String getInticket() {
		return inticket;
	}
	public void setInticket(String inticket) {
		this.inticket = inticket;
	}
	public String getIdNumber() {
		return idNumber;
	}
	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}
	public String getExamPassword() {
		return examPassword;
	}
	public void setExamPassword(String examPassword) {
		this.examPassword = examPassword;
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
	public String getIsExp() {
		return isExp;
	}
	public void setIsExp(String isExp) {
		this.isExp = isExp;
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
	public String getUserOrganName() {
		return userOrganName;
	}
	public void setUserOrganName(String userOrganName) {
		this.userOrganName = userOrganName;
	}
	public String getUserDeptName() {
		return userDeptName;
	}
	public void setUserDeptName(String userDeptName) {
		this.userDeptName = userDeptName;
	}
	public String getPostName() {
		return postName;
	}
	public void setPostName(String postName) {
		this.postName = postName;
	}
	public Employee getEmployee() {
		return employee;
	}
	public void setEmployee(Employee employee) {
		this.employee = employee;
	}
	public List<ExamUserTestpaper> getPwdList() {
		return pwdList;
	}
	public void setPwdList(List<ExamUserTestpaper> pwdList) {
		this.pwdList = pwdList;
	}
	
	
}
