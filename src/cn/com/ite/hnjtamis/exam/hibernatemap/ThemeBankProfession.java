package cn.com.ite.hnjtamis.exam.hibernatemap;

import cn.com.ite.hnjtamis.baseinfo.domain.Speciality;

/**
 * ThemeBankProfession entity. @author MyEclipse Persistence Tools
 */

public class ThemeBankProfession implements java.io.Serializable {

	// Fields

	private String bankProfessionId;
	private ThemeBank themeBank;
	private Speciality speciality;
	private String professionName;
	private String professionLevelCode;
	private Integer sortNum;
	private String syncFlag;
	private String lastUpdateDate;
	private String lastUpdatedBy;
	private String lastUpdatedIdBy;
	private String creationDate;
	private String createdBy;
	private String createdIdBy;
	private String organId;
	private String organName;

	// Constructors

	/** default constructor */
	public ThemeBankProfession() {
	}

	/** full constructor */
	public ThemeBankProfession(ThemeBank themeBank,
			Speciality speciality, String professionName,
			String professionLevelCode, Integer sortNum, String syncFlag,
			String lastUpdateDate, String lastUpdatedBy,
			String lastUpdatedIdBy, String creationDate, String createdBy,
			String createdIdBy, String organId, String organName) {
		this.themeBank = themeBank;
		this.speciality = speciality;
		this.professionName = professionName;
		this.professionLevelCode = professionLevelCode;
		this.sortNum = sortNum;
		this.syncFlag = syncFlag;
		this.lastUpdateDate = lastUpdateDate;
		this.lastUpdatedBy = lastUpdatedBy;
		this.lastUpdatedIdBy = lastUpdatedIdBy;
		this.creationDate = creationDate;
		this.createdBy = createdBy;
		this.createdIdBy = createdIdBy;
		this.organId = organId;
		this.organName = organName;
	}

	// Property accessors

	public String getBankProfessionId() {
		return this.bankProfessionId;
	}

	public void setBankProfessionId(String bankProfessionId) {
		this.bankProfessionId = bankProfessionId;
	}

	public ThemeBank getThemeBank() {
		return this.themeBank;
	}

	public void setThemeBank(ThemeBank themeBank) {
		this.themeBank = themeBank;
	}
	public Speciality getSpeciality() {
		return speciality;
	}

	public void setSpeciality(Speciality speciality) {
		this.speciality = speciality;
	}

	public String getProfessionName() {
		return this.professionName;
	}

	public void setProfessionName(String professionName) {
		this.professionName = professionName;
	}

	public String getProfessionLevelCode() {
		return this.professionLevelCode;
	}

	public void setProfessionLevelCode(String professionLevelCode) {
		this.professionLevelCode = professionLevelCode;
	}

	public Integer getSortNum() {
		return this.sortNum;
	}

	public void setSortNum(Integer sortNum) {
		this.sortNum = sortNum;
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

}