package cn.com.ite.eap2.domain.baseinfo;

/**
 * SysAfficheUser entity. @author MyEclipse Persistence Tools
 */

public class SysAfficheUser implements java.io.Serializable {

	// Fields

	private String sysAiUserId;
	private String saId;
	private String account;
	private String employeeId;
	private String employeeName;
	private String readTime;

	// Constructors

	/** default constructor */
	public SysAfficheUser() {
	}

	/** full constructor */
	public SysAfficheUser(String saId, String account, String employeeId,
			String employeeName, String readTime) {
		this.saId = saId;
		this.account = account;
		this.employeeId = employeeId;
		this.employeeName = employeeName;
		this.readTime = readTime;
	}

	// Property accessors

	public String getSysAiUserId() {
		return this.sysAiUserId;
	}

	public void setSysAiUserId(String sysAiUserId) {
		this.sysAiUserId = sysAiUserId;
	}

	public String getSaId() {
		return this.saId;
	}

	public void setSaId(String saId) {
		this.saId = saId;
	}

	public String getAccount() {
		return this.account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getEmployeeId() {
		return this.employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getEmployeeName() {
		return this.employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getReadTime() {
		return this.readTime;
	}

	public void setReadTime(String readTime) {
		this.readTime = readTime;
	}

}