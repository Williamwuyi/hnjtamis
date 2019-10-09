package cn.com.ite.hnjtamis.exam.exampaper.form;

public class ExamDeptPassForm {

	private String sortIndex;//序号
	private String organId;//所属机构ID
	private String organName;//所属机构
	private String deptId;//所属部门ID
	private String deptName;//所属部门
	private String showDeptName;//所属部门
	private String passCount;//合格的数量
	private String unPassCount;//不合格的数量
	private String passStateCount;//总数量
	private String zzxxrsCount;//在学习人数
	private String xxrsCount;//学习人数
	private String parentDeptId;
	
	
	
	public String getShowDeptName() {
		return showDeptName;
	}
	public void setShowDeptName(String showDeptName) {
		this.showDeptName = showDeptName;
	}
	public String getSortIndex() {
		return sortIndex;
	}
	public void setSortIndex(String sortIndex) {
		this.sortIndex = sortIndex;
	}
	public String getOrganId() {
		return organId;
	}
	public void setOrganId(String organId) {
		this.organId = organId;
	}
	public String getOrganName() {
		return organName;
	}
	public void setOrganName(String organName) {
		this.organName = organName;
	}
	public String getDeptId() {
		return deptId;
	}
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getPassCount() {
		return passCount;
	}
	public void setPassCount(String passCount) {
		this.passCount = passCount;
	}
	public String getUnPassCount() {
		return unPassCount;
	}
	public void setUnPassCount(String unPassCount) {
		this.unPassCount = unPassCount;
	}
	public String getPassStateCount() {
		return passStateCount;
	}
	public void setPassStateCount(String passStateCount) {
		this.passStateCount = passStateCount;
	}
	public String getXxrsCount() {
		return xxrsCount;
	}
	public void setXxrsCount(String xxrsCount) {
		this.xxrsCount = xxrsCount;
	}
	public String getParentDeptId() {
		return parentDeptId;
	}
	public void setParentDeptId(String parentDeptId) {
		this.parentDeptId = parentDeptId;
	}
	public String getZzxxrsCount() {
		return zzxxrsCount;
	}
	public void setZzxxrsCount(String zzxxrsCount) {
		this.zzxxrsCount = zzxxrsCount;
	}
	
	
}
