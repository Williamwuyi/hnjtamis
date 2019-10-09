package cn.com.ite.hnjtamis.exam.hibernatemap;

import java.util.*;

/**
 * ThemeBank entity. @author MyEclipse Persistence Tools
 */

public class ThemeBank implements java.io.Serializable {

	// Fields

	private String themeBankId;
	private ThemeBank themeBank;
	private String themeBankName;
	private String themeBankCode;
	private String publicName;
	private String publicTime;
	private String organId;
	private String organName;
	private String bankLevelCode;
	private Integer sortNum;
	private String remark;
	private String isL;
	private String syncFlag;
	private String lastUpdateDate;
	private String lastUpdatedBy;
	private String lastUpdatedIdBy;
	private String creationDate;
	private String createdBy;
	private String createdIdBy;
	private String themeAuditName;
	private String themeAuditId;
	private int themeNum = 0;
	private String bankPublic;//是否公有或私有 10-公有  20-电厂私有
	private String bankType;//题库类型 10-岗位题库  20-专业题库
	private List<ThemeBankPost> themeBankPosts = new ArrayList<ThemeBankPost>(0);
	private List<ThemeInBank> themeInBanks = new ArrayList<ThemeInBank>(0);
	private List<ThemeBankProfession> themeBankProfessions = new ArrayList<ThemeBankProfession>(0);
	private List<ThemeBank> themeBanks = new ArrayList<ThemeBank>(0);
	private int finThemeNum;
	private double succThemeNum;
	private String itemmoniScore;

	// Constructors

	/** default constructor */
	public ThemeBank() {
	}

	/** full constructor */
	public ThemeBank(String themeBankId, ThemeBank themeBank,
			String themeBankName, String themeBankCode,String publicName, String publicTime,
			String organId, String organName, String bankLevelCode,
			Integer sortNum, String remark, String isL, String syncFlag,
			String lastUpdateDate, String lastUpdatedBy,
			String lastUpdatedIdBy, String creationDate, String createdBy,
			String createdIdBy, List<ThemeBankPost> themeBankPosts,
			List<ThemeInBank> themeInBanks,
			List<ThemeBankProfession> themeBankProfessions,
			List<ThemeBank> themeBanks,String themeAuditName,
			String themeAuditId, String bankPublic,String bankType) {
		super();
		this.themeBankId = themeBankId;
		this.themeBank = themeBank;
		this.themeBankName = themeBankName;
		this.themeBankCode=themeBankCode;
		this.publicName = publicName;
		this.publicTime = publicTime;
		this.organId = organId;
		this.organName = organName;
		this.bankLevelCode = bankLevelCode;
		this.sortNum = sortNum;
		this.remark = remark;
		this.isL = isL;
		this.syncFlag = syncFlag;
		this.lastUpdateDate = lastUpdateDate;
		this.lastUpdatedBy = lastUpdatedBy;
		this.lastUpdatedIdBy = lastUpdatedIdBy;
		this.creationDate = creationDate;
		this.createdBy = createdBy;
		this.createdIdBy = createdIdBy;
		this.themeBankPosts = themeBankPosts;
		this.themeInBanks = themeInBanks;
		this.themeBankProfessions = themeBankProfessions;
		this.themeBanks = themeBanks;
		this.themeAuditName=themeAuditName;
		this.themeAuditId=themeAuditId;
		this.bankPublic=bankPublic;
		this.bankType=bankType;
	}

	// Property accessors

	
	
	public String getThemeBankId() {
		return this.themeBankId;
	}

	

	public String getThemeAuditName() {
		return themeAuditName;
	}

	public void setThemeAuditName(String themeAuditName) {
		this.themeAuditName = themeAuditName;
	}

	public String getThemeAuditId() {
		return themeAuditId;
	}

	public void setThemeAuditId(String themeAuditId) {
		this.themeAuditId = themeAuditId;
	}

	public int getThemeNum() {
		return themeNum;
	}

	public void setThemeNum(int themeNum) {
		this.themeNum = themeNum;
	}

	public String getThemeBankCode() {
		return themeBankCode;
	}

	public void setThemeBankCode(String themeBankCode) {
		this.themeBankCode = themeBankCode;
	}

	public void setThemeBankId(String themeBankId) {
		this.themeBankId = themeBankId;
	}

	public ThemeBank getThemeBank() {
		return this.themeBank;
	}

	public void setThemeBank(ThemeBank themeBank) {
		this.themeBank = themeBank;
	}

	public String getThemeBankName() {
		return this.themeBankName;
	}

	public void setThemeBankName(String themeBankName) {
		this.themeBankName = themeBankName;
	}

	public String getPublicName() {
		return this.publicName;
	}

	public void setPublicName(String publicName) {
		this.publicName = publicName;
	}

	public String getPublicTime() {
		return this.publicTime;
	}

	public void setPublicTime(String publicTime) {
		this.publicTime = publicTime;
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

	public String getBankLevelCode() {
		return this.bankLevelCode;
	}

	public void setBankLevelCode(String bankLevelCode) {
		this.bankLevelCode = bankLevelCode;
	}

	public Integer getSortNum() {
		return this.sortNum;
	}

	public void setSortNum(Integer sortNum) {
		this.sortNum = sortNum;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getIsL() {
		return this.isL;
	}

	public void setIsL(String isL) {
		this.isL = isL;
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

	public List<ThemeBankPost> getThemeBankPosts() {
		return themeBankPosts;
	}

	public void setThemeBankPosts(List<ThemeBankPost> themeBankPosts) {
		this.themeBankPosts = themeBankPosts;
	}

	public List<ThemeInBank> getThemeInBanks() {
		return themeInBanks;
	}

	public void setThemeInBanks(List<ThemeInBank> themeInBanks) {
		this.themeInBanks = themeInBanks;
	}

	public List<ThemeBankProfession> getThemeBankProfessions() {
		return themeBankProfessions;
	}

	public void setThemeBankProfessions(
			List<ThemeBankProfession> themeBankProfessions) {
		this.themeBankProfessions = themeBankProfessions;
	}

	public List<ThemeBank> getThemeBanks() {
		return themeBanks;
	}

	public void setThemeBanks(List<ThemeBank> themeBanks) {
		this.themeBanks = themeBanks;
	}

	public double getSuccThemeNum() {
		return succThemeNum;
	}

	public void setSuccThemeNum(double succThemeNum) {
		this.succThemeNum = succThemeNum;
	}

	public int getFinThemeNum() {
		return finThemeNum;
	}

	public void setFinThemeNum(int finThemeNum) {
		this.finThemeNum = finThemeNum;
	}

	public String getItemmoniScore() {
		return itemmoniScore;
	}

	public void setItemmoniScore(String itemmoniScore) {
		this.itemmoniScore = itemmoniScore;
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