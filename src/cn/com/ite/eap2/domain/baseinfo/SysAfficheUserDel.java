package cn.com.ite.eap2.domain.baseinfo;

/**
 * SysAfficheUserDel entity. @author MyEclipse Persistence Tools
 */

public class SysAfficheUserDel implements java.io.Serializable {

	// Fields

	private String sysAiUserDelId;
	private String saId;
	private String account;
	private String employeeId;
	private String employeeName;
	private String delTime;

	// Constructors

	/** default constructor */
	public SysAfficheUserDel() {
	}

	/** full constructor */
	public SysAfficheUserDel(String saId, String account, String employeeId,
			String employeeName, String delTime) {
		this.saId = saId;
		this.account = account;
		this.employeeId = employeeId;
		this.employeeName = employeeName;
		this.delTime = delTime;
	}

	// Property accessors

	public String getSysAiUserDelId() {
		return this.sysAiUserDelId;
	}

	public void setSysAiUserDelId(String sysAiUserDelId) {
		this.sysAiUserDelId = sysAiUserDelId;
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

	public String getDelTime() {
		return this.delTime;
	}

	public void setDelTime(String delTime) {
		this.delTime = delTime;
	}

}