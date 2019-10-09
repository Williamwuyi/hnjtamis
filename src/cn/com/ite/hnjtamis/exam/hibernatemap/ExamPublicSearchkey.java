package cn.com.ite.hnjtamis.exam.hibernatemap;

/**
 * ExamPublicSearchkey entity. @author MyEclipse Persistence Tools
 */

public class ExamPublicSearchkey implements java.io.Serializable {

	// Fields

	private String searchKeyId;
	private ExamPublic examPublic;
	private String professionId;
	private String professionName;
	private String professionLevelCode;
	private String postId;
	private String postName;
	private String postLevelCode;
	private String themeBankName;
	private String themeBankId;
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
	private String quarterTrainCode;
	private String quarterTrainName;
	private String quarterTrainId;
	private String deptName;
	private String deptId;
	private String dcType;

	// Constructors

	/** default constructor */
	public ExamPublicSearchkey() {
	}

	/** full constructor */
	public ExamPublicSearchkey(ExamPublic examPublic, String professionId,
			String professionName, String professionLevelCode, String postId,
			String postName, String postLevelCode, String themeBankId,String themeBankName,Integer sortNum,
			String bankLevelCode, String organId, String organName,
			String organLevelCode, String syncFlag, String lastUpdateDate,
			String lastUpdatedBy, String lastUpdatedIdBy, String creationDate,
			String createdBy, String createdIdBy,String quarterTrainCode,String quarterTrainName,String quarterTrainId,
			String deptName, String deptId,String dcType) {
		this.examPublic = examPublic;
		this.professionId = professionId;
		this.professionName = professionName;
		this.professionLevelCode = professionLevelCode;
		this.postId = postId;
		this.postName = postName;
		this.postLevelCode = postLevelCode;
		this.themeBankName=themeBankName;
		this.themeBankId = themeBankId;
		this.sortNum=sortNum;
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
		this.quarterTrainCode = quarterTrainCode;
		this.quarterTrainName = quarterTrainName;
		this.quarterTrainId = quarterTrainId;
		this.deptName = deptName;
		this.deptId = deptId;
		this.dcType = dcType;
	}

	// Property accessors

	
	
	public String getSearchKeyId() {
		return this.searchKeyId;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public String getDcType() {
		return dcType;
	}

	public void setDcType(String dcType) {
		this.dcType = dcType;
	}

	public String getQuarterTrainCode() {
		return quarterTrainCode;
	}

	public void setQuarterTrainCode(String quarterTrainCode) {
		this.quarterTrainCode = quarterTrainCode;
	}

	public String getQuarterTrainName() {
		return quarterTrainName;
	}

	public void setQuarterTrainName(String quarterTrainName) {
		this.quarterTrainName = quarterTrainName;
	}

	public String getQuarterTrainId() {
		return quarterTrainId;
	}

	public void setQuarterTrainId(String quarterTrainId) {
		this.quarterTrainId = quarterTrainId;
	}

	public Integer getSortNum() {
		return sortNum;
	}

	public void setSortNum(Integer sortNum) {
		this.sortNum = sortNum;
	}

	public String getThemeBankName() {
		return themeBankName;
	}

	public void setThemeBankName(String themeBankName) {
		this.themeBankName = themeBankName;
	}

	public void setSearchKeyId(String searchKeyId) {
		this.searchKeyId = searchKeyId;
	}

	public ExamPublic getExamPublic() {
		return this.examPublic;
	}

	public void setExamPublic(ExamPublic examPublic) {
		this.examPublic = examPublic;
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