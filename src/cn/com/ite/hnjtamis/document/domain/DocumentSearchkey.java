package cn.com.ite.hnjtamis.document.domain;

/**
 * DocumentSearchkey entity. @author MyEclipse Persistence Tools
 */

public class DocumentSearchkey implements java.io.Serializable {

	// Fields

	private String searchkeyId;
	private DocumentLib documentLib;
	private String professionId;
	private String professionName;
	private String professionLevelCode;
	private String postId;
	private String postName;
	private String postLevelCode;
	private String deptName;
	private String deptId;
	private String specialityName;
	private String specialityCode;
	private String quarterTrainName;
	private String quarterTrainId;
	private String quarterTrainCode;
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
	private Integer sortNum;
	private String favoriteEmployee;
	private String favoriteEmployeeId;
	private String favoriteUserId;

	// Constructors

	/** default constructor */
	public DocumentSearchkey() {
	}

	/** full constructor */
	public DocumentSearchkey(DocumentLib documentLib, String professionId,
			String professionName, String professionLevelCode, String postId,
			String postName, String postLevelCode, String deptName,
			String deptId, String specialityName, String specialityCode,
			String quarterTrainName, String quarterTrainId,
			String quarterTrainCode, String organId, String organName,
			String organLevelCode, String syncFlag, String lastUpdateDate,
			String lastUpdatedBy, String lastUpdatedIdBy, String creationDate,
			String createdBy, String createdIdBy, Integer sortNum,
			String favoriteEmployee,String favoriteEmployeeId,String favoriteUserId) {
		this.documentLib = documentLib;
		this.professionId = professionId;
		this.professionName = professionName;
		this.professionLevelCode = professionLevelCode;
		this.postId = postId;
		this.postName = postName;
		this.postLevelCode = postLevelCode;
		this.deptName = deptName;
		this.deptId = deptId;
		this.specialityName = specialityName;
		this.specialityCode = specialityCode;
		this.quarterTrainName = quarterTrainName;
		this.quarterTrainId = quarterTrainId;
		this.quarterTrainCode = quarterTrainCode;
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
		this.sortNum = sortNum;
		this.favoriteEmployee = favoriteEmployee;
		this.favoriteEmployeeId = favoriteEmployeeId;
		this.favoriteUserId = favoriteUserId;
	}

	// Property accessors

	public String getSearchkeyId() {
		return this.searchkeyId;
	}

	public void setSearchkeyId(String searchkeyId) {
		this.searchkeyId = searchkeyId;
	}

	public DocumentLib getDocumentLib() {
		return this.documentLib;
	}

	public void setDocumentLib(DocumentLib documentLib) {
		this.documentLib = documentLib;
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

	public String getDeptName() {
		return this.deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getDeptId() {
		return this.deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public String getSpecialityName() {
		return this.specialityName;
	}

	public void setSpecialityName(String specialityName) {
		this.specialityName = specialityName;
	}

	public String getSpecialityCode() {
		return this.specialityCode;
	}

	public void setSpecialityCode(String specialityCode) {
		this.specialityCode = specialityCode;
	}

	public String getQuarterTrainName() {
		return this.quarterTrainName;
	}

	public void setQuarterTrainName(String quarterTrainName) {
		this.quarterTrainName = quarterTrainName;
	}

	public String getQuarterTrainId() {
		return this.quarterTrainId;
	}

	public void setQuarterTrainId(String quarterTrainId) {
		this.quarterTrainId = quarterTrainId;
	}

	public String getQuarterTrainCode() {
		return this.quarterTrainCode;
	}

	public void setQuarterTrainCode(String quarterTrainCode) {
		this.quarterTrainCode = quarterTrainCode;
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

	public Integer getSortNum() {
		return this.sortNum;
	}

	public void setSortNum(Integer sortNum) {
		this.sortNum = sortNum;
	}

	public String getFavoriteEmployee() {
		return favoriteEmployee;
	}

	public void setFavoriteEmployee(String favoriteEmployee) {
		this.favoriteEmployee = favoriteEmployee;
	}

	public String getFavoriteEmployeeId() {
		return favoriteEmployeeId;
	}

	public void setFavoriteEmployeeId(String favoriteEmployeeId) {
		this.favoriteEmployeeId = favoriteEmployeeId;
	}

	public String getFavoriteUserId() {
		return favoriteUserId;
	}

	public void setFavoriteUserId(String favoriteUserId) {
		this.favoriteUserId = favoriteUserId;
	}

}