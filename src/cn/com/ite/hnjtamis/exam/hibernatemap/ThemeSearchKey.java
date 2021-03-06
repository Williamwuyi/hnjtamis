package cn.com.ite.hnjtamis.exam.hibernatemap;

/**
 * ThemeSearchKey entity. @author MyEclipse Persistence Tools
 */

public class ThemeSearchKey implements java.io.Serializable {

	// Fields

	private String searchKeyId;
	private Theme theme;
	private String professionId;
	private String professionName;
	private String professionLevelCode;
	private String postId;
	private String postName;
	private String postLevelCode;
	private String themeBankId;
	private String themeBankName;
	private Integer sortNum;
	private String bankLevelCode;
	private String organId;
	private String organName;
	private String organLevelCode;
	private String syncFlag;
	private String lastUpdateDate;
	private String lastUpdatedBy;
	private String lastUpdatedIdBy;
	private String creationDate;
	private String createdBy;
	private String createdIdBy;

	// Constructors

	/** default constructor */
	public ThemeSearchKey() {
	}

	/** full constructor */
	public ThemeSearchKey(Theme theme, String professionId,
			String professionName, String professionLevelCode, String postId,
			String postName, String postLevelCode, String themeBankId,String themeBankName,Integer sortNum,
			String bankLevelCode, String organId, String organName,
			String organLevelCode, String syncFlag, String lastUpdateDate,
			String lastUpdatedBy, String lastUpdatedIdBy, String creationDate,
			String createdBy, String createdIdBy) {
		this.theme = theme;
		this.professionId = professionId;
		this.professionName = professionName;
		this.professionLevelCode = professionLevelCode;
		this.postId = postId;
		this.postName = postName;
		this.postLevelCode = postLevelCode;
		this.themeBankId = themeBankId;
		this.themeBankName = themeBankName;
		this.sortNum = sortNum;
		this.bankLevelCode = bankLevelCode;
		this.organId = organId;
		this.organName = organName;
		this.organLevelCode = organLevelCode;
		this.syncFlag = syncFlag;
		this.lastUpdateDate = lastUpdateDate;
		this.lastUpdatedBy = lastUpdatedBy;
		this.lastUpdatedIdBy = lastUpdatedIdBy;
		this.creationDate = creationDate;
		this.createdBy = createdBy;
		this.createdIdBy = createdIdBy;
	}

	// Property accessors

	public String getSearchKeyId() {
		return this.searchKeyId;
	}

	public String getThemeBankName() {
		return themeBankName;
	}

	public void setThemeBankName(String themeBankName) {
		this.themeBankName = themeBankName;
	}

	public Integer getSortNum() {
		return sortNum;
	}

	public void setSortNum(Integer sortNum) {
		this.sortNum = sortNum;
	}

	public void setSearchKeyId(String searchKeyId) {
		this.searchKeyId = searchKeyId;
	}

	public Theme getTheme() {
		return this.theme;
	}

	public void setTheme(Theme theme) {
		this.theme = theme;
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

	public String getProfessionLevelCode() {
		return this.professionLevelCode;
	}

	public void setProfessionLevelCode(String professionLevelCode) {
		this.professionLevelCode = professionLevelCode;
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

	public String getThemeBankId() {
		return this.themeBankId;
	}

	public void setThemeBankId(String themeBankId) {
		this.themeBankId = themeBankId;
	}

	public String getBankLevelCode() {
		return this.bankLevelCode;
	}

	public void setBankLevelCode(String bankLevelCode) {
		this.bankLevelCode = bankLevelCode;
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

	public String getOrganLevelCode() {
		return this.organLevelCode;
	}

	public void setOrganLevelCode(String organLevelCode) {
		this.organLevelCode = organLevelCode;
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