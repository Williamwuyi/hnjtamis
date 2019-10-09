package cn.com.ite.hnjtamis.talent.domain;

import java.util.ArrayList;
import java.util.List;

import cn.com.ite.eap2.domain.organization.Employee;
import cn.com.ite.eap2.domain.organization.Organ;
import cn.com.ite.eap2.domain.organization.Quarter;
import cn.com.ite.hnjtamis.baseinfo.domain.Speciality;

/**
 * TalentRegistration entity. @author MyEclipse Persistence Tools
 */

public class TalentRegistration implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1488734071797388966L;
	private String id;
	private Employee employee;
	private Quarter quarter;
	private String specialityNames;
	//private Speciality speciality;
	private String name;
	private Integer sex;
	private String birthday;
	private Integer isAudited;
	private String auditerId;
	private String auditerName;
	private String auditTime;
	private String electedTime;
	private String type;
	private String certificate;
	private String skill;
	private String otherInfo;
	private Integer isDel;
	private String remark;
	private Integer syncStatus;
	private String lastUpdateDate;
	private String lastUpdatedBy;
	private String creationDate;
	private String createdBy;
	private Organ organ;
	private String bankNames;//负责的题库名（显示用）
	private String wordDay;//参加工作年月
	private String quarterDuty;//现岗位及职务
	private String specialityGrade;//专业技术资格情况
	private String specialityGradeYear;//专业技术资格情况资格年限（年）
	private String skillGrade;//现技能等级
	private String skillGradeYear;//技能资格年限（年）
	private String explain211;//112人才情况
	private String explainGroup;//集团及以上级荣誉
	
	private int fkThemeAuditNum = 0;//反馈审核次数
	private int examMarkNum = 0;//阅卷次数
	
	private List<TalentRegistrationBank> talentRegistrationBanks = new ArrayList<TalentRegistrationBank>();
	private List<TalentRegistrationSpeciality> specialitys = new ArrayList<TalentRegistrationSpeciality>();

	// Constructors

	/** default constructor */
	public TalentRegistration() {
	}

	/** full constructor */
	public TalentRegistration(Quarter quarter, 
			String name, Integer isAudited, String auditerId,
			String auditerName, String auditTime, String electedTime,
			String type, String certificate, String skill, String otherInfo,
			Integer isDel, String remark, Integer syncStatus,
			String lastUpdateDate, String lastUpdatedBy, String creationDate,
			String createdBy, Organ organ,String bankNames,List<TalentRegistrationBank> talentRegistrationBanks,
			String wordDay,String quarterDuty,String specialityGrade,String specialityGradeYear,
			String skillGrade,String skillGradeYear,String explain211,String explainGroup,
			List<TalentRegistrationSpeciality> specialitys,String specialityNames) {
		this.quarter = quarter;
		//this.speciality = speciality;
		this.name = name;
		this.isAudited = isAudited;
		this.auditerId = auditerId;
		this.auditerName = auditerName;
		this.auditTime = auditTime;
		this.electedTime = electedTime;
		this.type = type;
		this.certificate = certificate;
		this.skill = skill;
		this.otherInfo = otherInfo;
		this.isDel = isDel;
		this.remark = remark;
		this.syncStatus = syncStatus;
		this.lastUpdateDate = lastUpdateDate;
		this.lastUpdatedBy = lastUpdatedBy;
		this.creationDate = creationDate;
		this.createdBy = createdBy;
		this.organ = organ;
		this.bankNames=bankNames;
		this.talentRegistrationBanks = talentRegistrationBanks;
		this.wordDay = wordDay;
		this.quarterDuty = quarterDuty;
		this.specialityGrade = specialityGrade;
		this.specialityGradeYear = specialityGradeYear;
		this.skillGrade = skillGrade;
		this.skillGradeYear = skillGradeYear;
		this.explain211 = explain211;
		this.explainGroup = explainGroup;
		this.specialitys = specialitys;
		this.specialityNames=specialityNames;
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

	/*public Speciality getSpeciality() {
		return this.speciality;
	}

	public void setSpeciality(Speciality speciality) {
		this.speciality = speciality;
	}*/

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getIsAudited() {
		return this.isAudited;
	}

	public void setIsAudited(Integer isAudited) {
		this.isAudited = isAudited;
	}

	public String getAuditerId() {
		return this.auditerId;
	}

	public void setAuditerId(String auditerId) {
		this.auditerId = auditerId;
	}

	public String getAuditerName() {
		return this.auditerName;
	}

	public void setAuditerName(String auditerName) {
		this.auditerName = auditerName;
	}

	public String getAuditTime() {
		return this.auditTime;
	}

	public void setAuditTime(String auditTime) {
		this.auditTime = auditTime;
	}

	public String getElectedTime() {
		return this.electedTime;
	}

	public void setElectedTime(String electedTime) {
		this.electedTime = electedTime;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCertificate() {
		return this.certificate;
	}

	public void setCertificate(String certificate) {
		this.certificate = certificate;
	}

	public String getSkill() {
		return this.skill;
	}

	public void setSkill(String skill) {
		this.skill = skill;
	}

	public String getOtherInfo() {
		return this.otherInfo;
	}

	public void setOtherInfo(String otherInfo) {
		this.otherInfo = otherInfo;
	}

	public Integer getIsDel() {
		return this.isDel;
	}

	public void setIsDel(Integer isDel) {
		this.isDel = isDel;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getSyncStatus() {
		return this.syncStatus;
	}

	public void setSyncStatus(Integer syncStatus) {
		this.syncStatus = syncStatus;
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

	public Organ getOrgan() {
		return this.organ;
	}

	public void setOrgan(Organ organ) {
		this.organ = organ;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public String getBankNames() {
		return bankNames;
	}

	public void setBankNames(String bankNames) {
		this.bankNames = bankNames;
	}

	public List<TalentRegistrationBank> getTalentRegistrationBanks() {
		return talentRegistrationBanks;
	}

	public void setTalentRegistrationBanks(
			List<TalentRegistrationBank> talentRegistrationBanks) {
		this.talentRegistrationBanks = talentRegistrationBanks;
	}

	public String getWordDay() {
		return wordDay;
	}

	public void setWordDay(String wordDay) {
		this.wordDay = wordDay;
	}

	public String getQuarterDuty() {
		return quarterDuty;
	}

	public void setQuarterDuty(String quarterDuty) {
		this.quarterDuty = quarterDuty;
	}

	public String getSpecialityGrade() {
		return specialityGrade;
	}

	public void setSpecialityGrade(String specialityGrade) {
		this.specialityGrade = specialityGrade;
	}

	public String getSpecialityGradeYear() {
		return specialityGradeYear;
	}

	public void setSpecialityGradeYear(String specialityGradeYear) {
		this.specialityGradeYear = specialityGradeYear;
	}

	public String getSkillGrade() {
		return skillGrade;
	}

	public void setSkillGrade(String skillGrade) {
		this.skillGrade = skillGrade;
	}

	public String getSkillGradeYear() {
		return skillGradeYear;
	}

	public void setSkillGradeYear(String skillGradeYear) {
		this.skillGradeYear = skillGradeYear;
	}

	public String getExplain211() {
		return explain211;
	}

	public void setExplain211(String explain211) {
		this.explain211 = explain211;
	}

	public String getExplainGroup() {
		return explainGroup;
	}

	public void setExplainGroup(String explainGroup) {
		this.explainGroup = explainGroup;
	}

	public List<TalentRegistrationSpeciality> getSpecialitys() {
		return specialitys;
	}

	public void setSpecialitys(List<TalentRegistrationSpeciality> specialitys) {
		this.specialitys = specialitys;
	}

	public String getSpecialityNames() {
		return specialityNames;
	}

	public void setSpecialityNames(String specialityNames) {
		this.specialityNames = specialityNames;
	}

	public int getFkThemeAuditNum() {
		return fkThemeAuditNum;
	}

	public void setFkThemeAuditNum(int fkThemeAuditNum) {
		this.fkThemeAuditNum = fkThemeAuditNum;
	}

	public int getExamMarkNum() {
		return examMarkNum;
	}

	public void setExamMarkNum(int examMarkNum) {
		this.examMarkNum = examMarkNum;
	}

	
	
}