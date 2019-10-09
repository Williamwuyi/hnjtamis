package cn.com.ite.hnjtamis.train.domain;

import cn.com.ite.eap2.domain.organization.Quarter;
import cn.com.ite.hnjtamis.baseinfo.domain.Speciality;
import cn.com.ite.hnjtamis.kb.domain.CoursewareDistribute;

/**
 * TrainImplementCourse entity. @author MyEclipse Persistence Tools
 */

public class TrainImplementCourse implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -2613678237117883752L;
	private String id;
	private Quarter quarter;
	private Speciality speciality;
	private TrainImplement trainImplement;
	private CoursewareDistribute coursewareDistribute;
	private String lastUpdateDate;
	private String lastUpdatedBy;
	private String creationDate;
	private String createdBy;
	private String organId;
	private Integer sortNo;

	// Constructors

	/** default constructor */
	public TrainImplementCourse() {
	}

	/** full constructor */
	public TrainImplementCourse(Quarter quarter, Speciality speciality,
			TrainImplement trainImplement,
			CoursewareDistribute coursewareDistribute, String lastUpdateDate,
			String lastUpdatedBy, String creationDate, String createdBy,
			String organId) {
		this.quarter = quarter;
		this.speciality = speciality;
		this.trainImplement = trainImplement;
		this.coursewareDistribute = coursewareDistribute;
		this.lastUpdateDate = lastUpdateDate;
		this.lastUpdatedBy = lastUpdatedBy;
		this.creationDate = creationDate;
		this.createdBy = createdBy;
		this.organId = organId;
	}

	// Property accessors

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Quarter getQuarter() {
		return this.quarter;
	}

	public void setQuarter(Quarter quarter) {
		this.quarter = quarter;
	}

	public Speciality getSpeciality() {
		return this.speciality;
	}

	public void setSpeciality(Speciality speciality) {
		this.speciality = speciality;
	}

	public TrainImplement getTrainImplement() {
		return this.trainImplement;
	}

	public void setTrainImplement(TrainImplement trainImplement) {
		this.trainImplement = trainImplement;
	}

	public CoursewareDistribute getCoursewareDistribute() {
		return this.coursewareDistribute;
	}

	public void setCoursewareDistribute(
			CoursewareDistribute coursewareDistribute) {
		this.coursewareDistribute = coursewareDistribute;
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

	public String getOrganId() {
		return this.organId;
	}

	public void setOrganId(String organId) {
		this.organId = organId;
	}

	public Integer getSortNo() {
		return sortNo;
	}

	public void setSortNo(Integer sortNo) {
		this.sortNo = sortNo;
	}

}