package cn.com.ite.hnjtamis.jobstandard.domain;

/**
 * JobsStandardThemebank entity. @author MyEclipse Persistence Tools
 */

public class JobsStandardThemebank implements java.io.Serializable {

	// Fields

	private static final long serialVersionUID = 655460112034669476L;
	
	private String standardThemebankId;
	private StandardTerms standardTerms;
	private String themeBankId;
	private String themeBankName;
	private String themeBankCode;
	private String organName;
	private String organId;
	private String bankPublic;
	private String lastUpdateDate;
	private String lastUpdatedBy;
	private String creationDate;
	private String createdBy;

	// Constructors

	/** default constructor */
	public JobsStandardThemebank() {
	}

	/** full constructor */
	public JobsStandardThemebank(StandardTerms standardTerms, String themeBankId,
			String themeBankName, String themeBankCode, String organName,
			String organId, String bankPublic, String lastUpdateDate,
			String lastUpdatedBy, String creationDate, String createdBy) {
		this.standardTerms = standardTerms;
		this.themeBankId = themeBankId;
		this.themeBankName = themeBankName;
		this.themeBankCode = themeBankCode;
		this.organName = organName;
		this.organId = organId;
		this.bankPublic = bankPublic;
		this.lastUpdateDate = lastUpdateDate;
		this.lastUpdatedBy = lastUpdatedBy;
		this.creationDate = creationDate;
		this.createdBy = createdBy;
	}

	// Property accessors

	public String getStandardThemebankId() {
		return this.standardThemebankId;
	}

	public void setStandardThemebankId(String standardThemebankId) {
		this.standardThemebankId = standardThemebankId;
	}

	public StandardTerms getStandardTerms() {
		return standardTerms;
	}

	public void setStandardTerms(StandardTerms standardTerms) {
		this.standardTerms = standardTerms;
	}

	public String getThemeBankId() {
		return this.themeBankId;
	}

	public void setThemeBankId(String themeBankId) {
		this.themeBankId = themeBankId;
	}

	public String getThemeBankName() {
		return this.themeBankName;
	}

	public void setThemeBankName(String themeBankName) {
		this.themeBankName = themeBankName;
	}

	public String getThemeBankCode() {
		return this.themeBankCode;
	}

	public void setThemeBankCode(String themeBankCode) {
		this.themeBankCode = themeBankCode;
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

	public String getBankPublic() {
		return this.bankPublic;
	}

	public void setBankPublic(String bankPublic) {
		this.bankPublic = bankPublic;
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