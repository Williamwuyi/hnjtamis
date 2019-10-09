package cn.com.ite.hnjtamis.exam.hibernatemap;

import java.util.HashSet;
import java.util.Set;

/**
 * ExamPublicUser entity. @author MyEclipse Persistence Tools
 */

public class ExamPublicUser implements java.io.Serializable {

	// Fields

	private String userId;
	private ExamPublic examPublic;
	private String userOrganId;
	private String userOrganName;
	private String userDeptId;
	private String userDeptName;
	private String userGroupId;
	private String userGroupName;
	private String postId;
	private String postName;
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
	private String employeeId;//人员ID
	private String employeeName;//人员
	private String examFinTime;//考试完成时间
	private String examFinFlag;//考试标识 10-未参考  20-已参考  30-缺考 40-被处罚
	private String bmType;//报名方式 10-系统要求 20-自主报名
	private String msgHzFlag = "F";
	private Set examUserTestpapers = new HashSet(0);

	// Constructors

	/** default constructor */
	public ExamPublicUser() {
	}

	/** full constructor */
	public ExamPublicUser(ExamPublic examPublic, String userOrganId,
			String userOrganName, String userDeptId, String userDeptName,
			String userGroupId, String userGroupName, String postId,
			String postName, String userName, String userSex,
			String userBirthday, String userNation, String userAddr,
			String userPhone, String userInfo, Double score, String inticket,
			String idNumber, String examPassword, String checkUser,
			String checkUserId, String checkTime, String state, String isDel,
			String syncFlag, String isExp, String organId, String organName,
			String remark, String lastUpdateDate, String lastUpdatedBy,
			String lastUpdatedIdBy, String creationDate, String createdBy,
			String createdIdBy, Set examUserTestpapers,String employeeId,String employeeName,
			String examFinTime,String examFinFlag,String bmType) {
		this.examPublic = examPublic;
		this.userOrganId = userOrganId;
		this.userOrganName = userOrganName;
		this.userDeptId = userDeptId;
		this.userDeptName = userDeptName;
		this.userGroupId = userGroupId;
		this.userGroupName = userGroupName;
		this.postId = postId;
		this.postName = postName;
		this.userName = userName;
		this.userSex = userSex;
		this.userBirthday = userBirthday;
		this.userNation = userNation;
		this.userAddr = userAddr;
		this.userPhone = userPhone;
		this.userInfo = userInfo;
		this.score = score;
		this.inticket = inticket;
		this.idNumber = idNumber;
		this.examPassword = examPassword;
		this.checkUser = checkUser;
		this.checkUserId = checkUserId;
		this.checkTime = checkTime;
		this.state = state;
		this.isDel = isDel;
		this.syncFlag = syncFlag;
		this.isExp = isExp;
		this.organId = organId;
		this.organName = organName;
		this.remark = remark;
		this.lastUpdateDate = lastUpdateDate;
		this.lastUpdatedBy = lastUpdatedBy;
		this.lastUpdatedIdBy = lastUpdatedIdBy;
		this.creationDate = creationDate;
		this.createdBy = createdBy;
		this.createdIdBy = createdIdBy;
		this.examUserTestpapers = examUserTestpapers;
		this.employeeId=employeeId;
		this.employeeName=employeeName;
		this.examFinTime = examFinTime;
		this.examFinFlag = examFinFlag;
		this.bmType = bmType;
	}

	// Property accessors

	
	
	public String getUserId() {
		return this.userId;
	}

	public String getBmType() {
		return bmType;
	}

	public void setBmType(String bmType) {
		this.bmType = bmType;
	}

	public String getExamFinTime() {
		return examFinTime;
	}

	public void setExamFinTime(String examFinTime) {
		this.examFinTime = examFinTime;
	}

	public String getExamFinFlag() {
		return examFinFlag;
	}

	public void setExamFinFlag(String examFinFlag) {
		this.examFinFlag = examFinFlag;
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

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public ExamPublic getExamPublic() {
		return this.examPublic;
	}

	public void setExamPublic(ExamPublic examPublic) {
		this.examPublic = examPublic;
	}

	public String getUserOrganId() {
		return this.userOrganId;
	}

	public void setUserOrganId(String userOrganId) {
		this.userOrganId = userOrganId;
	}

	public String getUserOrganName() {
		return this.userOrganName;
	}

	public void setUserOrganName(String userOrganName) {
		this.userOrganName = userOrganName;
	}

	public String getUserDeptId() {
		return this.userDeptId;
	}

	public void setUserDeptId(String userDeptId) {
		this.userDeptId = userDeptId;
	}

	public String getUserDeptName() {
		return this.userDeptName;
	}

	public void setUserDeptName(String userDeptName) {
		this.userDeptName = userDeptName;
	}

	public String getUserGroupId() {
		return this.userGroupId;
	}

	public void setUserGroupId(String userGroupId) {
		this.userGroupId = userGroupId;
	}

	public String getUserGroupName() {
		return this.userGroupName;
	}

	public void setUserGroupName(String userGroupName) {
		this.userGroupName = userGroupName;
	}

	public String getPostId() {
		return this.postId;
	}

	public void setPostId(String postId) {
		this.postId = postId;
	}

	public String getPostName() {
		return this.postName;
	}

	public void setPostName(String postName) {
		this.postName = postName;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserSex() {
		return this.userSex;
	}

	public void setUserSex(String userSex) {
		this.userSex = userSex;
	}

	public String getUserBirthday() {
		return this.userBirthday;
	}

	public void setUserBirthday(String userBirthday) {
		this.userBirthday = userBirthday;
	}

	public String getUserNation() {
		return this.userNation;
	}

	public void setUserNation(String userNation) {
		this.userNation = userNation;
	}

	public String getUserAddr() {
		return this.userAddr;
	}

	public void setUserAddr(String userAddr) {
		this.userAddr = userAddr;
	}

	public String getUserPhone() {
		return this.userPhone;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}

	public String getUserInfo() {
		return this.userInfo;
	}

	public void setUserInfo(String userInfo) {
		this.userInfo = userInfo;
	}

	public Double getScore() {
		return this.score;
	}

	public void setScore(Double score) {
		this.score = score;
	}

	public String getInticket() {
		return this.inticket;
	}

	public void setInticket(String inticket) {
		this.inticket = inticket;
	}

	public String getIdNumber() {
		return this.idNumber;
	}

	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}

	public String getExamPassword() {
		return this.examPassword;
	}

	public void setExamPassword(String examPassword) {
		this.examPassword = examPassword;
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

	public String getIsExp() {
		return this.isExp;
	}

	public void setIsExp(String isExp) {
		this.isExp = isExp;
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

	public Set getExamUserTestpapers() {
		return this.examUserTestpapers;
	}

	public void setExamUserTestpapers(Set examUserTestpapers) {
		this.examUserTestpapers = examUserTestpapers;
	}

	public String getMsgHzFlag() {
		return msgHzFlag;
	}

	public void setMsgHzFlag(String msgHzFlag) {
		this.msgHzFlag = msgHzFlag;
	}
	
	

}