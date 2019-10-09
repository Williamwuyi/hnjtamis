package cn.com.ite.hnjtamis.exam.hibernatemap;

import java.util.HashSet;
import java.util.Set;

/**
 * ExamMarkpeople entity. @author MyEclipse Persistence Tools
 */

public class ExamMarkpeople implements java.io.Serializable {

	// Fields

	private String examMarkpeopleId;
	private ExamMarkpeopleInfo examMarkpeopleInfo;
	private Exam exam;
	private String empId;
	private String markPeopleName;
	private String startTime;
	private String endTime;
	private Integer markWay;
	private Integer isMain;
	private String organName;
	private String organId;
	private String syncFlag;
	private String lastUpdateDate;
	private String lastUpdatedBy;
	private String lastUpdatedIdBy;
	private String creationDate;
	private String createdBy;
	private String createdIdBy;
	private String themeTypeId;//涉及题型ID
	private String themeTypeName;//涉及题型Name
	private String professionId;//涉及专业ID
	private String professionName;//涉及专业Name
	private Set examMarkThemes = new HashSet(0);

	// Constructors

	/** default constructor */
	public ExamMarkpeople() {
	}

	/** full constructor */
	public ExamMarkpeople(ExamMarkpeopleInfo examMarkpeopleInfo, Exam exam,
			String markPeopleName, String startTime, String endTime,
			Integer markWay, Integer isMain, String organName, String organId,
			String syncFlag, String lastUpdateDate, String lastUpdatedBy,
			String lastUpdatedIdBy, String creationDate, String createdBy,
			String createdIdBy, Set examMarkThemes,
			String themeTypeId,String themeTypeName,
			String professionId,String professionName) {
		this.examMarkpeopleInfo = examMarkpeopleInfo;
		this.exam = exam;
		this.markPeopleName = markPeopleName;
		this.startTime = startTime;
		this.endTime = endTime;
		this.markWay = markWay;
		this.isMain = isMain;
		this.organName = organName;
		this.organId = organId;
		this.syncFlag = syncFlag;
		this.lastUpdateDate = lastUpdateDate;
		this.lastUpdatedBy = lastUpdatedBy;
		this.lastUpdatedIdBy = lastUpdatedIdBy;
		this.creationDate = creationDate;
		this.createdBy = createdBy;
		this.createdIdBy = createdIdBy;
		this.examMarkThemes = examMarkThemes;
		this.themeTypeId=themeTypeId;
		this.themeTypeName=themeTypeName;
		this.professionId=professionId;
		this.professionName=professionName;
	}

	// Property accessors

	public String getExamMarkpeopleId() {
		return this.examMarkpeopleId;
	}

	public String getThemeTypeId() {
		return themeTypeId;
	}

	public void setThemeTypeId(String themeTypeId) {
		this.themeTypeId = themeTypeId;
	}

	public String getThemeTypeName() {
		return themeTypeName;
	}

	public void setThemeTypeName(String themeTypeName) {
		this.themeTypeName = themeTypeName;
	}

	public String getProfessionId() {
		return professionId;
	}

	public void setProfessionId(String professionId) {
		this.professionId = professionId;
	}

	public String getProfessionName() {
		return professionName;
	}

	public void setProfessionName(String professionName) {
		this.professionName = professionName;
	}

	public void setExamMarkpeopleId(String examMarkpeopleId) {
		this.examMarkpeopleId = examMarkpeopleId;
	}

	public ExamMarkpeopleInfo getExamMarkpeopleInfo() {
		return this.examMarkpeopleInfo;
	}

	public void setExamMarkpeopleInfo(ExamMarkpeopleInfo examMarkpeopleInfo) {
		this.examMarkpeopleInfo = examMarkpeopleInfo;
	}

	public Exam getExam() {
		return this.exam;
	}

	public void setExam(Exam exam) {
		this.exam = exam;
	}

	public String getMarkPeopleName() {
		return this.markPeopleName;
	}

	public void setMarkPeopleName(String markPeopleName) {
		this.markPeopleName = markPeopleName;
	}

	public String getStartTime() {
		return this.startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return this.endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public Integer getMarkWay() {
		return this.markWay;
	}

	public void setMarkWay(Integer markWay) {
		this.markWay = markWay;
	}

	public Integer getIsMain() {
		return this.isMain;
	}

	public void setIsMain(Integer isMain) {
		this.isMain = isMain;
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

	public Set getExamMarkThemes() {
		return this.examMarkThemes;
	}

	public void setExamMarkThemes(Set examMarkThemes) {
		this.examMarkThemes = examMarkThemes;
	}

	public String getEmpId() {
		return empId;
	}

	public void setEmpId(String empId) {
		this.empId = empId;
	}
	
}