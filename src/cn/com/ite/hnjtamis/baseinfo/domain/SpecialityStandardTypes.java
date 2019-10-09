package cn.com.ite.hnjtamis.baseinfo.domain;

/**
 * BaseSpecialityStandardTypes entity. @author MyEclipse Persistence Tools
 */

public class SpecialityStandardTypes implements java.io.Serializable {

	// Fields

	private String specialityStandardId;
	private Speciality speciality;
	private String parentTypesName;
	private String parentTypesId;
	private String typesName;
	private String typesId;
	private Integer orderno;
	private String lastUpdateDate;
	private String lastUpdatedBy;
	private String creationDate;
	private String createdBy;

	// Constructors

	/** default constructor */
	public SpecialityStandardTypes() {
	}

	/** full constructor */
	public SpecialityStandardTypes(Speciality speciality,
			String parentTypesName, String parentTypesId, String typesName,
			String typesId, Integer orderno, String lastUpdateDate,
			String lastUpdatedBy, String creationDate, String createdBy) {
		this.speciality = speciality;
		this.parentTypesName = parentTypesName;
		this.parentTypesId = parentTypesId;
		this.typesName = typesName;
		this.typesId = typesId;
		this.orderno = orderno;
		this.lastUpdateDate = lastUpdateDate;
		this.lastUpdatedBy = lastUpdatedBy;
		this.creationDate = creationDate;
		this.createdBy = createdBy;
	}

	// Property accessors

	public String getSpecialityStandardId() {
		return this.specialityStandardId;
	}

	public void setSpecialityStandardId(String specialityStandardId) {
		this.specialityStandardId = specialityStandardId;
	}

	
	public Speciality getSpeciality() {
		return speciality;
	}

	public void setSpeciality(Speciality speciality) {
		this.speciality = speciality;
	}

	public String getParentTypesName() {
		return this.parentTypesName;
	}

	public void setParentTypesName(String parentTypesName) {
		this.parentTypesName = parentTypesName;
	}

	public String getParentTypesId() {
		return this.parentTypesId;
	}

	public void setParentTypesId(String parentTypesId) {
		this.parentTypesId = parentTypesId;
	}

	public String getTypesName() {
		return this.typesName;
	}

	public void setTypesName(String typesName) {
		this.typesName = typesName;
	}

	public String getTypesId() {
		return this.typesId;
	}

	public void setTypesId(String typesId) {
		this.typesId = typesId;
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