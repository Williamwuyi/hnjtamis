package cn.com.ite.hnjtamis.jobstandard.domain;

/**
 * JobsStandardQuarter entity. @author MyEclipse Persistence Tools
 */

public class JobsStandardQuarter implements java.io.Serializable {

	// Fields

	private String relationId;
	private StandardTerms standardTerms;
	private String quarterTrainCode;
	private String quarterTrainName;
	private Integer orderno;
	private String lastUpdateDate;
	private String lastUpdatedBy;
	private String creationDate;
	private String createdBy;
	private String deptName;
	private String deptId;
	private String specialityName;
	private String specialityCode;
	private String dcType;
	private String quarterId;
	
	private String themeBankName;//题库名称
	private String themeBankCode;//题库名称
	private String themeBankId;//题库名称
	
	private String parentTrainQuarterId;
	private String parentTrainQuarterName;
	private String parentTrainQuarterCode;
	// Constructors

	/** default constructor */
	public JobsStandardQuarter() {
	}

	/** full constructor */
	public JobsStandardQuarter(StandardTerms standardTerms,
			String quarterTrainCode, String quarterTrainName, Integer orderno,
			String lastUpdateDate, String lastUpdatedBy, String creationDate,
			String createdBy, String deptName, String deptId,
			String specialityName, String specialityCode, String dcType,
			String quarterId,String themeBankName,String themeBankCode,String themeBankId,
			String parentTrainQuarterId,String parentTrainQuarterName,String parentTrainQuarterCode) {
		this.standardTerms = standardTerms;
		this.quarterTrainCode = quarterTrainCode;
		this.quarterTrainName = quarterTrainName;
		this.orderno = orderno;
		this.lastUpdateDate = lastUpdateDate;
		this.lastUpdatedBy = lastUpdatedBy;
		this.creationDate = creationDate;
		this.createdBy = createdBy;
		this.deptName = deptName;
		this.deptId = deptId;
		this.specialityName = specialityName;
		this.specialityCode = specialityCode;
		this.dcType = dcType;
		this.quarterId = quarterId;
		
		this.themeBankName=themeBankName;
        this.themeBankId=themeBankId;
        this.themeBankCode=themeBankCode;
        
        this.parentTrainQuarterId=parentTrainQuarterId;
        this.parentTrainQuarterName=parentTrainQuarterName;
        this.parentTrainQuarterCode=parentTrainQuarterCode;
	}

	// Property accessors

	public String getRelationId() {
		return this.relationId;
	}

	public void setRelationId(String relationId) {
		this.relationId = relationId;
	}

	public StandardTerms getStandardTerms() {
		return standardTerms;
	}

	public void setStandardTerms(StandardTerms standardTerms) {
		this.standardTerms = standardTerms;
	}

	public String getQuarterTrainCode() {
		return this.quarterTrainCode;
	}

	public void setQuarterTrainCode(String quarterTrainCode) {
		this.quarterTrainCode = quarterTrainCode;
	}

	public String getQuarterTrainName() {
		return this.quarterTrainName;
	}

	public void setQuarterTrainName(String quarterTrainName) {
		this.quarterTrainName = quarterTrainName;
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

	public String getDcType() {
		return this.dcType;
	}

	public void setDcType(String dcType) {
		this.dcType = dcType;
	}

	public String getQuarterId() {
		return this.quarterId;
	}

	public void setQuarterId(String quarterId) {
		this.quarterId = quarterId;
	}

	public String getThemeBankName() {
		return themeBankName;
	}

	public void setThemeBankName(String themeBankName) {
		this.themeBankName = themeBankName;
	}

	public String getThemeBankCode() {
		return themeBankCode;
	}

	public void setThemeBankCode(String themeBankCode) {
		this.themeBankCode = themeBankCode;
	}

	public String getThemeBankId() {
		return themeBankId;
	}

	public void setThemeBankId(String themeBankId) {
		this.themeBankId = themeBankId;
	}

	public String getParentTrainQuarterId() {
		return parentTrainQuarterId;
	}

	public void setParentTrainQuarterId(String parentTrainQuarterId) {
		this.parentTrainQuarterId = parentTrainQuarterId;
	}

	public String getParentTrainQuarterName() {
		return parentTrainQuarterName;
	}

	public void setParentTrainQuarterName(String parentTrainQuarterName) {
		this.parentTrainQuarterName = parentTrainQuarterName;
	}

	public String getParentTrainQuarterCode() {
		return parentTrainQuarterCode;
	}

	public void setParentTrainQuarterCode(String parentTrainQuarterCode) {
		this.parentTrainQuarterCode = parentTrainQuarterCode;
	}
	
	

}