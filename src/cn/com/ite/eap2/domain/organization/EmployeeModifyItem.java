package cn.com.ite.eap2.domain.organization;

/**
 * EmployeeModifyItem entity. @author MyEclipse Persistence Tools
 */

public class EmployeeModifyItem implements java.io.Serializable {

	// Fields

	private String itemId;
	private EmployeeModifyBatch employeeModifyBatch;
	private String employeeName;
	private String employeeId;
	private String deptNameOld;
	private String deptIdOld;
	private String quarterNameOld;
	private String quarterIdOld;
	private String organNameOld;
	private String organIdOld;
	private String deptNameNew;
	private String deptIdNew;
	private String quarterNameNew;
	private String quarterIdNew;
	private String organNameNew;
	private String organIdNew;
	private String itemState;
	private String itemSortNum;
	private String lastUpdateDate;
	private String lastUpdatedBy;
	private String lastUpdatedIdBy;
	private String creationDate;
	private String createdBy;
	private String createdIdBy;

	// Constructors

	/** default constructor */
	public EmployeeModifyItem() {
	}

	/** full constructor */
	public EmployeeModifyItem(EmployeeModifyBatch employeeModifyBatch,
			String employeeName, String employeeId, String deptNameOld,
			String deptIdOld, String quarterNameOld, String quarterIdOld,
			String organNameOld, String organIdOld, String deptNameNew,
			String deptIdNew, String quarterNameNew, String quarterIdNew,
			String organNameNew, String organIdNew, String itemState,
			String itemSortNum, String lastUpdateDate, String lastUpdatedBy,
			String lastUpdatedIdBy, String creationDate, String createdBy,
			String createdIdBy) {
		this.employeeModifyBatch = employeeModifyBatch;
		this.employeeName = employeeName;
		this.employeeId = employeeId;
		this.deptNameOld = deptNameOld;
		this.deptIdOld = deptIdOld;
		this.quarterNameOld = quarterNameOld;
		this.quarterIdOld = quarterIdOld;
		this.organNameOld = organNameOld;
		this.organIdOld = organIdOld;
		this.deptNameNew = deptNameNew;
		this.deptIdNew = deptIdNew;
		this.quarterNameNew = quarterNameNew;
		this.quarterIdNew = quarterIdNew;
		this.organNameNew = organNameNew;
		this.organIdNew = organIdNew;
		this.itemState = itemState;
		this.itemSortNum = itemSortNum;
		this.lastUpdateDate = lastUpdateDate;
		this.lastUpdatedBy = lastUpdatedBy;
		this.lastUpdatedIdBy = lastUpdatedIdBy;
		this.creationDate = creationDate;
		this.createdBy = createdBy;
		this.createdIdBy = createdIdBy;
	}

	// Property accessors

	public String getItemId() {
		return this.itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public EmployeeModifyBatch getEmployeeModifyBatch() {
		return this.employeeModifyBatch;
	}

	public void setEmployeeModifyBatch(EmployeeModifyBatch employeeModifyBatch) {
		this.employeeModifyBatch = employeeModifyBatch;
	}

	public String getEmployeeName() {
		return this.employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getEmployeeId() {
		return this.employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getDeptNameOld() {
		return this.deptNameOld;
	}

	public void setDeptNameOld(String deptNameOld) {
		this.deptNameOld = deptNameOld;
	}

	public String getDeptIdOld() {
		return this.deptIdOld;
	}

	public void setDeptIdOld(String deptIdOld) {
		this.deptIdOld = deptIdOld;
	}

	public String getQuarterNameOld() {
		return this.quarterNameOld;
	}

	public void setQuarterNameOld(String quarterNameOld) {
		this.quarterNameOld = quarterNameOld;
	}

	public String getQuarterIdOld() {
		return this.quarterIdOld;
	}

	public void setQuarterIdOld(String quarterIdOld) {
		this.quarterIdOld = quarterIdOld;
	}

	public String getOrganNameOld() {
		return this.organNameOld;
	}

	public void setOrganNameOld(String organNameOld) {
		this.organNameOld = organNameOld;
	}

	public String getOrganIdOld() {
		return this.organIdOld;
	}

	public void setOrganIdOld(String organIdOld) {
		this.organIdOld = organIdOld;
	}

	public String getDeptNameNew() {
		return this.deptNameNew;
	}

	public void setDeptNameNew(String deptNameNew) {
		this.deptNameNew = deptNameNew;
	}

	public String getDeptIdNew() {
		return this.deptIdNew;
	}

	public void setDeptIdNew(String deptIdNew) {
		this.deptIdNew = deptIdNew;
	}

	public String getQuarterNameNew() {
		return this.quarterNameNew;
	}

	public void setQuarterNameNew(String quarterNameNew) {
		this.quarterNameNew = quarterNameNew;
	}

	public String getQuarterIdNew() {
		return this.quarterIdNew;
	}

	public void setQuarterIdNew(String quarterIdNew) {
		this.quarterIdNew = quarterIdNew;
	}

	public String getOrganNameNew() {
		return this.organNameNew;
	}

	public void setOrganNameNew(String organNameNew) {
		this.organNameNew = organNameNew;
	}

	public String getOrganIdNew() {
		return this.organIdNew;
	}

	public void setOrganIdNew(String organIdNew) {
		this.organIdNew = organIdNew;
	}

	public String getItemState() {
		return this.itemState;
	}

	public void setItemState(String itemState) {
		this.itemState = itemState;
	}

	public String getItemSortNum() {
		return this.itemSortNum;
	}

	public void setItemSortNum(String itemSortNum) {
		this.itemSortNum = itemSortNum;
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