package cn.com.ite.hnjtamis.talent.domain;

/**
 * TalentRegistrationBank entity. @author MyEclipse Persistence Tools
 */

public class TalentRegistrationBank implements java.io.Serializable {

	// Fields

	private String talentBankId;
	private TalentRegistration talentRegistration;
	private String bankId;
	private String bankCode;
	private String bankName;
	private String jstypeid;
	private String typename;
	private String standardid;
	private String standardname;
	private Integer orderno;
	private String lastUpdateDate;
	private String lastUpdatedBy;
	private String creationDate;
	private String createdBy;

	// Constructors

	/** default constructor */
	public TalentRegistrationBank() {
	}

	/** full constructor */
	public TalentRegistrationBank(TalentRegistration talentRegistration,
			String bankId, String bankCode, String bankName, String jstypeid,
			String typename, String standardid, String standardname,
			Integer orderno, String lastUpdateDate, String lastUpdatedBy,
			String creationDate, String createdBy) {
		this.talentRegistration = talentRegistration;
		this.bankId = bankId;
		this.bankCode = bankCode;
		this.bankName = bankName;
		this.jstypeid = jstypeid;
		this.typename = typename;
		this.standardid = standardid;
		this.standardname = standardname;
		this.orderno = orderno;
		this.lastUpdateDate = lastUpdateDate;
		this.lastUpdatedBy = lastUpdatedBy;
		this.creationDate = creationDate;
		this.createdBy = createdBy;
	}

	// Property accessors

	public String getTalentBankId() {
		return this.talentBankId;
	}

	public void setTalentBankId(String talentBankId) {
		this.talentBankId = talentBankId;
	}

	public TalentRegistration getTalentRegistration() {
		return this.talentRegistration;
	}

	public void setTalentRegistration(TalentRegistration talentRegistration) {
		this.talentRegistration = talentRegistration;
	}

	public String getBankId() {
		return this.bankId;
	}

	public void setBankId(String bankId) {
		this.bankId = bankId;
	}

	public String getBankCode() {
		return this.bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getBankName() {
		return this.bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getJstypeid() {
		return this.jstypeid;
	}

	public void setJstypeid(String jstypeid) {
		this.jstypeid = jstypeid;
	}

	public String getTypename() {
		return this.typename;
	}

	public void setTypename(String typename) {
		this.typename = typename;
	}

	public String getStandardid() {
		return this.standardid;
	}

	public void setStandardid(String standardid) {
		this.standardid = standardid;
	}

	public String getStandardname() {
		return this.standardname;
	}

	public void setStandardname(String standardname) {
		this.standardname = standardname;
	}

	public Integer getOrderno() {
		return this.orderno;
	}

	public void setOrderno(Integer orderno) {
		this.orderno = orderno;
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

}