package cn.com.ite.hnjtamis.exam.hibernatemap;

import java.util.HashSet;
import java.util.Set;

/**
 * ExamMarkpeopleInfo entity. @author MyEclipse Persistence Tools
 */

public class ExamMarkpeopleInfo implements java.io.Serializable {

	// Fields

	private String markPeopleId;
	private String empId;
	private String markPeopleName;
	private String userSex;
	private String userBirthday;
	private String userNation;
	private String userAddr;
	private String userPhone;
	private String professionId;
	private String professionName;
	private String userDeptId;
	private String userDeptName;
	private String userGroupId;
	private String userGroupName;
	private String postId;
	private String postName;
	private String organName;
	private String organId;
	private String syncFlag;
	private String lastUpdateDate;
	private String lastUpdatedBy;
	private String lastUpdatedIdBy;
	private String creationDate;
	private String createdBy;
	private String createdIdBy;
	private Set examMarkpeoples = new HashSet(0);

	// Constructors

	/** default constructor */
	public ExamMarkpeopleInfo() {
	}

	/** full constructor */
	public ExamMarkpeopleInfo(String markPeopleName, String userSex,
			String userBirthday, String userNation, String userAddr,
			String userPhone, String professionId, String professionName,
			String userDeptId, String userDeptName, String userGroupId,
			String userGroupName, String postId, String postName,
			String organName, String organId, String syncFlag,
			String lastUpdateDate, String lastUpdatedBy,
			String lastUpdatedIdBy, String creationDate, String createdBy,
			String createdIdBy, Set examMarkpeoples) {
		this.markPeopleName = markPeopleName;
		this.userSex = userSex;
		this.userBirthday = userBirthday;
		this.userNation = userNation;
		this.userAddr = userAddr;
		this.userPhone = userPhone;
		this.professionId = professionId;
		this.professionName = professionName;
		this.userDeptId = userDeptId;
		this.userDeptName = userDeptName;
		this.userGroupId = userGroupId;
		this.userGroupName = userGroupName;
		this.postId = postId;
		this.postName = postName;
		this.organName = organName;
		this.organId = organId;
		this.syncFlag = syncFlag;
		this.lastUpdateDate = lastUpdateDate;
		this.lastUpdatedBy = lastUpdatedBy;
		this.lastUpdatedIdBy = lastUpdatedIdBy;
		this.creationDate = creationDate;
		this.createdBy = createdBy;
		this.createdIdBy = createdIdBy;
		this.examMarkpeoples = examMarkpeoples;
	}

	// Property accessors

	public String getMarkPeopleId() {
		return this.markPeopleId;
	}

	public void setMarkPeopleId(String markPeopleId) {
		this.markPeopleId = markPeopleId;
	}

	public String getMarkPeopleName() {
		return this.markPeopleName;
	}

	public void setMarkPeopleName(String markPeopleName) {
		this.markPeopleName = markPeopleName;
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

	public String getProfessionId() {
		return this.professionId;
	}

	public void setProfessionId(String professionId) {
		this.professionId = professionId;
	}

	public String getProfessionName() {
		return this.professionName;
	}

	public void setProfessionName(String professionName) {
		this.professionName = professionName;
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

	public Set getExamMarkpeoples() {
		return this.examMarkpeoples;
	}

	public void setExamMarkpeoples(Set examMarkpeoples) {
		this.examMarkpeoples = examMarkpeoples;
	}

	public String getEmpId() {
		return empId;
	}

	public void setEmpId(String empId) {
		this.empId = empId;
	}
	
}