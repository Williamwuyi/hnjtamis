package cn.com.ite.hnjtamis.exam.base.themebank.form;

import java.util.ArrayList;
import java.util.List;

import cn.com.ite.eap2.domain.organization.Employee;
import cn.com.ite.hnjtamis.baseinfo.domain.Speciality;
import cn.com.ite.hnjtamis.exam.base.theme.form.ThemePostForm;
import cn.com.ite.hnjtamis.exam.hibernatemap.ThemeBank;
/*
 * 题库表单 form
 */
public class ThemeBankForm {
	private String themeBankId;
	private ThemeBank themeBank;
	private String themeBankName;
	private String themeBankCode;
	private String publicName;
	private String publicTime;
	private String organId;
	private String organName;
	private String bankLevelCode;
	private Integer sortNum;
	private String remark;
	private String isL;
	private String syncFlag;
	private String lastUpdateDate;
	private String lastUpdatedBy;
	private String lastUpdatedIdBy;
	private String creationDate;
	private String createdBy;
	private String createdIdBy;
	private String bankPublic;//是否公有或私有 10-公有  20-电厂私有
	private String bankType;//题库类型 10-岗位题库  20-专业题库
	private String bankPublicTxt;
	private List<Employee> auditEmps = new ArrayList<Employee>();//试题审核人
	
	private List<Speciality> specialitys = new ArrayList<Speciality>();//专业
	private List<ThemePostForm> themePostFormList = new ArrayList<ThemePostForm>();
	
	private List<StandardTypeForm>  standardTypeslist = new ArrayList<StandardTypeForm>();//关联的标准模块
	
	
	public List<Employee> getAuditEmps() {
		return auditEmps;
	}
	public void setAuditEmps(List<Employee> auditEmps) {
		this.auditEmps = auditEmps;
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
	public ThemeBank getThemeBank() {
		return themeBank;
	}
	public void setThemeBank(ThemeBank themeBank) {
		this.themeBank = themeBank;
	}
	public String getThemeBankName() {
		return themeBankName;
	}
	public void setThemeBankName(String themeBankName) {
		this.themeBankName = themeBankName;
	}
	public String getPublicName() {
		return publicName;
	}
	public void setPublicName(String publicName) {
		this.publicName = publicName;
	}
	public String getPublicTime() {
		return publicTime;
	}
	public void setPublicTime(String publicTime) {
		this.publicTime = publicTime;
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
	public String getBankLevelCode() {
		return bankLevelCode;
	}
	public void setBankLevelCode(String bankLevelCode) {
		this.bankLevelCode = bankLevelCode;
	}
	public Integer getSortNum() {
		return sortNum;
	}
	public void setSortNum(Integer sortNum) {
		this.sortNum = sortNum;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getIsL() {
		return isL;
	}
	public void setIsL(String isL) {
		this.isL = isL;
	}
	public String getSyncFlag() {
		return syncFlag;
	}
	public void setSyncFlag(String syncFlag) {
		this.syncFlag = syncFlag;
	}
	public String getLastUpdateDate() {
		return lastUpdateDate;
	}
	public void setLastUpdateDate(String lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}
	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}
	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}
	public String getLastUpdatedIdBy() {
		return lastUpdatedIdBy;
	}
	public void setLastUpdatedIdBy(String lastUpdatedIdBy) {
		this.lastUpdatedIdBy = lastUpdatedIdBy;
	}
	public String getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getCreatedIdBy() {
		return createdIdBy;
	}
	public void setCreatedIdBy(String createdIdBy) {
		this.createdIdBy = createdIdBy;
	}
	public List<Speciality> getSpecialitys() {
		return specialitys;
	}
	public void setSpecialitys(List<Speciality> specialitys) {
		this.specialitys = specialitys;
	}
	public List<ThemePostForm> getThemePostFormList() {
		return themePostFormList;
	}
	public void setThemePostFormList(List<ThemePostForm> themePostFormList) {
		this.themePostFormList = themePostFormList;
	}
	public String getBankPublic() {
		return bankPublic;
	}
	public void setBankPublic(String bankPublic) {
		this.bankPublic = bankPublic;
	}
	public String getBankPublicTxt() {
		return "10".equals(this.bankPublic)?"公有":"电厂私有";
	}
	public void setBankPublicTxt(String bankPublicTxt) {
		this.bankPublicTxt = bankPublicTxt;
	}
	public List<StandardTypeForm> getStandardTypeslist() {
		return standardTypeslist;
	}
	public void setStandardTypeslist(List<StandardTypeForm> standardTypeslist) {
		this.standardTypeslist = standardTypeslist;
	}
	public String getBankType() {
		return bankType;
	}
	public void setBankType(String bankType) {
		this.bankType = bankType;
	};
	
	
}
