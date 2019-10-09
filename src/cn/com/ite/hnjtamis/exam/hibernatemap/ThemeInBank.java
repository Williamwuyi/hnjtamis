package cn.com.ite.hnjtamis.exam.hibernatemap;

/**
 * ThemeInBank entity. @author MyEclipse Persistence Tools
 */

public class ThemeInBank implements java.io.Serializable {

	// Fields

	private String themeInBankId;
	private Theme theme;
	private ThemeBank themeBank;
	private String syncFlag;
	private String lastUpdateDate;
	private String lastUpdatedBy;
	private String lastUpdatedIdBy;
	private String creationDate;
	private String createdBy;
	private String createdIdBy;
	private String organId;
	private String organName;
	
	private String bankOrganId;//题库创建机构ID
	private String bankOrganName;//题库创建机构
	private String bankPublic;//是否公有或私有 10-公有  20-电厂私有
	private String bankType;//题库类型 10-岗位题库  20-专业题库
	// Constructors

	/** default constructor */
	public ThemeInBank() {
	}

	/** full constructor */
	public ThemeInBank(Theme theme, ThemeBank themeBank, String syncFlag,
			String lastUpdateDate, String lastUpdatedBy,
			String lastUpdatedIdBy, String creationDate, String createdBy,
			String createdIdBy, String organId, String organName,
			String bankOrganId,String bankOrganName,String bankPublic,String bankType) {
		this.theme = theme;
		this.themeBank = themeBank;
		this.syncFlag = syncFlag;
		this.lastUpdateDate = lastUpdateDate;
		this.lastUpdatedBy = lastUpdatedBy;
		this.lastUpdatedIdBy = lastUpdatedIdBy;
		this.creationDate = creationDate;
		this.createdBy = createdBy;
		this.createdIdBy = createdIdBy;
		this.organId = organId;
		this.organName = organName;
		this.bankOrganId = bankOrganId;
		this.bankOrganName = bankOrganName;
		this.bankPublic=bankPublic;
		this.bankType=bankType;
	}

	// Property accessors

	public String getThemeInBankId() {
		return this.themeInBankId;
	}

	public void setThemeInBankId(String themeInBankId) {
		this.themeInBankId = themeInBankId;
	}

	public Theme getTheme() {
		return this.theme;
	}

	public void setTheme(Theme theme) {
		this.theme = theme;
	}

	public ThemeBank getThemeBank() {
		return this.themeBank;
	}

	public void setThemeBank(ThemeBank themeBank) {
		this.themeBank = themeBank;
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

	public String getBankOrganId() {
		return bankOrganId;
	}

	public void setBankOrganId(String bankOrganId) {
		this.bankOrganId = bankOrganId;
	}

	public String getBankOrganName() {
		return bankOrganName;
	}

	public void setBankOrganName(String bankOrganName) {
		this.bankOrganName = bankOrganName;
	}

	public String getBankPublic() {
		return bankPublic;
	}

	public void setBankPublic(String bankPublic) {
		this.bankPublic = bankPublic;
	}

	public String getBankType() {
		return bankType;
	}

	public void setBankType(String bankType) {
		this.bankType = bankType;
	}
	
	

}