package cn.com.ite.eap2.domain.organization;

import java.util.HashSet;
import java.util.Set;

/**
 * EmployeeModifyBatch entity. @author MyEclipse Persistence Tools
 */

public class EmployeeModifyBatch implements java.io.Serializable {

	// Fields

	private String batchId;
	private String batchCode;
	private String batchName;
	private String organName;
	private String organId;
	private String sortNum;
	private String state;
	private String lastUpdateDate;
	private String lastUpdatedBy;
	private String lastUpdatedIdBy;
	private String creationDate;
	private String createdBy;
	private String createdIdBy;
	private Set employeeModifyItems = new HashSet(0);

	// Constructors

	/** default constructor */
	public EmployeeModifyBatch() {
	}

	/** full constructor */
	public EmployeeModifyBatch(String batchCode, String batchName,
			String organName, String organId, String sortNum, String state,
			String lastUpdateDate, String lastUpdatedBy,
			String lastUpdatedIdBy, String creationDate, String createdBy,
			String createdIdBy, Set employeeModifyItems) {
		this.batchCode = batchCode;
		this.batchName = batchName;
		this.organName = organName;
		this.organId = organId;
		this.sortNum = sortNum;
		this.state = state;
		this.lastUpdateDate = lastUpdateDate;
		this.lastUpdatedBy = lastUpdatedBy;
		this.lastUpdatedIdBy = lastUpdatedIdBy;
		this.creationDate = creationDate;
		this.createdBy = createdBy;
		this.createdIdBy = createdIdBy;
		this.employeeModifyItems = employeeModifyItems;
	}

	// Property accessors

	public String getBatchId() {
		return this.batchId;
	}

	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}

	public String getBatchCode() {
		return this.batchCode;
	}

	public void setBatchCode(String batchCode) {
		this.batchCode = batchCode;
	}

	public String getBatchName() {
		return this.batchName;
	}

	public void setBatchName(String batchName) {
		this.batchName = batchName;
	}

	public String getOrganName() {
		return this.organName;
	}

	public void setOrganName(String organName) {
		this.organName = organName;
	}

	public String getOrganId() {
		return this.organId;
	}

	public void setOrganId(String organId) {
		this.organId = organId;
	}

	public String getSortNum() {
		return this.sortNum;
	}

	public void setSortNum(String sortNum) {
		this.sortNum = sortNum;
	}

	public String getState() {
		return this.state;
	}

	public void setState(String state) {
		this.state = state;
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

	public Set getEmployeeModifyItems() {
		return this.employeeModifyItems;
	}

	public void setEmployeeModifyItems(Set employeeModifyItems) {
		this.employeeModifyItems = employeeModifyItems;
	}

}