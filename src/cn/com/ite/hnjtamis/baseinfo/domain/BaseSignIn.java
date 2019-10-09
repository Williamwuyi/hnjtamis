package cn.com.ite.hnjtamis.baseinfo.domain;

/**
 * BaseSignIn entity. @author MyEclipse Persistence Tools
 */

public class BaseSignIn implements java.io.Serializable {

	// Fields

	private String signInId;
	private String organId;
	private String organName;
	private String deptId;
	private String deptName;
	private String quarterId;
	private String quarterName;
	private String employeeName;
	private String employeeId;
	private String userName;
	private String userId;
	private String signInDate;

	// Constructors

	/** default constructor */
	public BaseSignIn() {
	}

	/** full constructor */
	public BaseSignIn(String organId, String organName, String deptId,
			String deptName, String quarterId, String quarterName,
			String employeeName, String employeeId, String userName,
			String userId, String signInDate) {
		this.organId = organId;
		this.organName = organName;
		this.deptId = deptId;
		this.deptName = deptName;
		this.quarterId = quarterId;
		this.quarterName = quarterName;
		this.employeeName = employeeName;
		this.employeeId = employeeId;
		this.userName = userName;
		this.userId = userId;
		this.signInDate = signInDate;
	}

	// Property accessors

	public String getSignInId() {
		return this.signInId;
	}

	public void setSignInId(String signInId) {
		this.signInId = signInId;
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

	public String getDeptId() {
		return this.deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public String getDeptName() {
		return this.deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getQuarterId() {
		return this.quarterId;
	}

	public void setQuarterId(String quarterId) {
		this.quarterId = quarterId;
	}

	public String getQuarterName() {
		return this.quarterName;
	}

	public void setQuarterName(String quarterName) {
		this.quarterName = quarterName;
	}

	public String getEmployeeName() {
		return this.employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getEmployeeId() {
		return this.employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getSignInDate() {
		return this.signInDate;
	}

	public void setSignInDate(String signInDate) {
		this.signInDate = signInDate;
	}

}