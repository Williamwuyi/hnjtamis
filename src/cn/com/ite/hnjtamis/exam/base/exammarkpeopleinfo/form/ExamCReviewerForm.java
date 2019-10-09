package cn.com.ite.hnjtamis.exam.base.exammarkpeopleinfo.form;

import java.util.ArrayList;
import java.util.List;

import cn.com.ite.eap2.domain.organization.Employee;
import cn.com.ite.hnjtamis.baseinfo.domain.Speciality;
import cn.com.ite.hnjtamis.exam.hibernatemap.ExamMarkpeopleInfo;
import cn.com.ite.hnjtamis.exam.hibernatemap.ThemeType;

public class ExamCReviewerForm {
	private String examMarkpeopleId;//阅卷人表id
	//private ExamMarkpeopleInfo examMarkpeopleInfo;//阅卷人
	private String reviewerName;
	private String examMarkpeopleInfo;//阅卷人
	private String empId;//员工id
	private Employee examMarkEmp;
	private int isMain;//是否主阅卷
	private List<ThemeType> themeTypes = new ArrayList<ThemeType>();//题型
	private List<Speciality> specialitys = new ArrayList<Speciality>();//专业
	
	
	
	
	public Employee getExamMarkEmp() {
		return examMarkEmp;
	}
	public void setExamMarkEmp(Employee examMarkEmp) {
		this.examMarkEmp = examMarkEmp;
	}
	public String getExamMarkpeopleId() {
		return examMarkpeopleId;
	}
	public void setExamMarkpeopleId(String examMarkpeopleId) {
		this.examMarkpeopleId = examMarkpeopleId;
	}
	
	public String getExamMarkpeopleInfo() {
		return examMarkpeopleInfo;
	}
	public void setExamMarkpeopleInfo(String examMarkpeopleInfo) {
		this.examMarkpeopleInfo = examMarkpeopleInfo;
	}
	public int getIsMain() {
		return isMain;
	}
	public void setIsMain(int isMain) {
		this.isMain = isMain;
	}
	
	public List<ThemeType> getThemeTypes() {
		return themeTypes;
	}
	public void setThemeTypes(List<ThemeType> themeTypes) {
		this.themeTypes = themeTypes;
	}
	public List<Speciality> getSpecialitys() {
		return specialitys;
	}
	public void setSpecialitys(List<Speciality> specialitys) {
		this.specialitys = specialitys;
	}
	public String getReviewerName() {
		return reviewerName;
	}
	public void setReviewerName(String reviewerName) {
		this.reviewerName = reviewerName;
	}
	public String getEmpId() {
		return empId;
	}
	public void setEmpId(String empId) {
		this.empId = empId;
	}
	
	
}
