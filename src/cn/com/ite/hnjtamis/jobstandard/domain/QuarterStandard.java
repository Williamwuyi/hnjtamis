package cn.com.ite.hnjtamis.jobstandard.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * QuarterStandard entity. @author MyEclipse Persistence Tools
 */

public class QuarterStandard implements java.io.Serializable {

	// Fields

	private String quarterId;
	private String deptName;
	private String deptId;
	private String specialityName;
	private String specialityCode;
	private String quarterName;
	private String quarterCode;
	private String remark;
	private String dcType;
	private BigDecimal sortNum;
	private String lastUpdateDate;
	private String lastUpdatedBy;
	private String creationDate;
	private String createdBy;
	private String organId;
	private String organName;
	private String belongType;
	
	private String parentQuarterId;
	private String parentQuarterName;
	private String parentQuarterCode;
	
	private int standardnums = 0;//此标准有的标准
	
	private List<QuarterStandard> parentQuarterStandard = new ArrayList<QuarterStandard>(0);;

	// Constructors

	/** default constructor */
	public QuarterStandard() {
	}

	/** full constructor */
	public QuarterStandard(String deptName, String deptId,
			String specialityName, String specialityCode, String quarterName,
			String quarterCode, String remark, String dcType,
			BigDecimal sortNum, String lastUpdateDate, String lastUpdatedBy,
			String creationDate, String createdBy, String organId,
			String organName,String belongType,
			String parentQuarterId,String parentQuarterName,String parentQuarterCode) {
		this.deptName = deptName;
		this.deptId = deptId;
		this.specialityName = specialityName;
		this.specialityCode = specialityCode;
		this.quarterName = quarterName;
		this.quarterCode = quarterCode;
		this.remark = remark;
		this.dcType = dcType;
		this.sortNum = sortNum;
		this.lastUpdateDate = lastUpdateDate;
		this.lastUpdatedBy = lastUpdatedBy;
		this.creationDate = creationDate;
		this.createdBy = createdBy;
		this.organId = organId;
		this.organName = organName;
		this.belongType = belongType;
		this.parentQuarterId = parentQuarterId;
		this.parentQuarterName = parentQuarterName;
		this.parentQuarterCode = parentQuarterCode;
	}

	// Property accessors

	public String getQuarterId() {
		return this.quarterId;
	}

	public void setQuarterId(String quarterId) {
		this.quarterId = quarterId;
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

	public String getQuarterName() {
		return this.quarterName;
	}

	public void setQuarterName(String quarterName) {
		this.quarterName = quarterName;
	}

	public String getQuarterCode() {
		return this.quarterCode;
	}

	public void setQuarterCode(String quarterCode) {
		this.quarterCode = quarterCode;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getDcType() {
		return this.dcType;
	}

	public void setDcType(String dcType) {
		this.dcType = dcType;
	}

	public BigDecimal getSortNum() {
		return this.sortNum;
	}

	public void setSortNum(BigDecimal sortNum) {
		this.sortNum = sortNum;
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

	public String getOrganName() {
		return this.organName;
	}

	public void setOrganName(String organName) {
		this.organName = organName;
	}

	public String getBelongType() {
		return belongType;
	}

	public void setBelongType(String belongType) {
		this.belongType = belongType;
	}

	public String getParentQuarterId() {
		return parentQuarterId;
	}

	public void setParentQuarterId(String parentQuarterId) {
		this.parentQuarterId = parentQuarterId;
	}

	public String getParentQuarterName() {
		return parentQuarterName;
	}

	public void setParentQuarterName(String parentQuarterName) {
		this.parentQuarterName = parentQuarterName;
	}

	public String getParentQuarterCode() {
		return parentQuarterCode;
	}

	public void setParentQuarterCode(String parentQuarterCode) {
		this.parentQuarterCode = parentQuarterCode;
	}

	public List<QuarterStandard> getParentQuarterStandard() {
		return parentQuarterStandard;
	}

	public void setParentQuarterStandard(List<QuarterStandard> parentQuarterStandard) {
		this.parentQuarterStandard = parentQuarterStandard;
	}

	public int getStandardnums() {
		return standardnums;
	}

	public void setStandardnums(int standardnums) {
		this.standardnums = standardnums;
	}
	
	

}