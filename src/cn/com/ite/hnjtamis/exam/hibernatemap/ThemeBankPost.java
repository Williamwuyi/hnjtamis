package cn.com.ite.hnjtamis.exam.hibernatemap;

/**
 * ThemeBankPost entity. @author MyEclipse Persistence Tools
 */

public class ThemeBankPost implements java.io.Serializable {

	// Fields

	private String bankPostId;
	private ThemeBank themeBank;
	private String organId;
	private String organName;
	private String deptId;
	private String deptName;
	private String postId;
	private String postName;
	private String postLevelCode;
	private Integer sortNum;
	private String syncFlag;
	private String lastUpdateDate;
	private String lastUpdatedBy;
	private String lastUpdatedIdBy;
	private String creationDate;
	private String createdBy;
	private String createdIdBy;

	// Constructors

	/** default constructor */
	public ThemeBankPost() {
	}

	/** full constructor */
	public ThemeBankPost(ThemeBank themeBank, String organId, String organName,
			String deptId, String deptName, String postId, String postName,
			String postLevelCode, Integer sortNum, String syncFlag,
			String lastUpdateDate, String lastUpdatedBy,
			String lastUpdatedIdBy, String creationDate, String createdBy,
			String createdIdBy) {
		this.themeBank = themeBank;
		this.organId = organId;
		this.organName = organName;
		this.deptId = deptId;
		this.deptName = deptName;
		this.postId = postId;
		this.postName = postName;
		this.postLevelCode = postLevelCode;
		this.sortNum = sortNum;
		this.syncFlag = syncFlag;
		this.lastUpdateDate = lastUpdateDate;
		this.lastUpdatedBy = lastUpdatedBy;
		this.lastUpdatedIdBy = lastUpdatedIdBy;
		this.creationDate = creationDate;
		this.createdBy = createdBy;
		this.createdIdBy = createdIdBy;
	}

	// Property accessors

	public String getBankPostId() {
		return this.bankPostId;
	}

	public void setBankPostId(String bankPostId) {
		this.bankPostId = bankPostId;
	}

	public ThemeBank getThemeBank() {
		return this.themeBank;
	}

	public void setThemeBank(ThemeBank themeBank) {
		this.themeBank = themeBank;
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

	public String getPostLevelCode() {
		return this.postLevelCode;
	}

	public void setPostLevelCode(String postLevelCode) {
		this.postLevelCode = postLevelCode;
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

}