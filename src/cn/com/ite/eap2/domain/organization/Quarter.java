package cn.com.ite.eap2.domain.organization;

import java.util.*;

import cn.com.ite.eap2.domain.power.SysRole;
import cn.com.ite.hnjtamis.jobstandard.domain.QuarterStandard;

/**
 * <p>Title cn.com.ite.eap2.domain.organization.Quarter</p>
 * <p>Description 岗位</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2014</p>
 * @author 宋文科
 * @create time: 2014-7-2 下午05:27:10
 * @version 2.0
 * 
 * @modified records:
 */
public class Quarter implements java.io.Serializable {
	private static final long serialVersionUID = 6163813084430269483L;
	private String quarterId;
	private Dept dept;
	private Quarter quarter;
	private String quarterCode;
	private String quarterName;
	private String quarterType;
	private boolean validation;
	private String remark;
	private String responsibility;
	private String levelCode;
	private Integer orderNo;
	private String quarterTrainCode;//培训岗位编码
	private String quarterTrainId;//培训岗位ID
	private String quarterTrainName;
	private QuarterStandard quarterStandard;
	private List<SysRole> quarterRoles = new ArrayList<SysRole>(0);
	private List<Quarter> quarters = new ArrayList<Quarter>(0);
	private List<Employee> employees = new ArrayList<Employee>(0);

	
	
	
	
	public QuarterStandard getQuarterStandard() {
		return quarterStandard;
	}

	public void setQuarterStandard(QuarterStandard quarterStandard) {
		this.quarterStandard = quarterStandard;
	}

	public String getQuarterTrainCode() {
		return quarterTrainCode;
	}

	public void setQuarterTrainCode(String quarterTrainCode) {
		this.quarterTrainCode = quarterTrainCode;
	}

	public List<Employee> getEmployees() {
		return employees;
	}

	public void setEmployees(List<Employee> employees) {
		this.employees = employees;
	}

	public String getQuarterId() {
		return quarterId;
	}

	public void setQuarterId(String quarterId) {
		this.quarterId = quarterId;
	}

	public Dept getDept() {
		return dept;
	}

	public void setDept(Dept dept) {
		this.dept = dept;
	}

	public Quarter getQuarter() {
		return quarter;
	}

	public void setQuarter(Quarter quarter) {
		this.quarter = quarter;
	}

	public String getQuarterCode() {
		return quarterCode;
	}

	public void setQuarterCode(String quarterCode) {
		this.quarterCode = quarterCode;
	}

	public String getQuarterName() {
		return quarterName;
	}

	public void setQuarterName(String quarterName) {
		this.quarterName = quarterName;
	}

	public String getQuarterType() {
		return quarterType;
	}

	public void setQuarterType(String quarterType) {
		this.quarterType = quarterType;
	}

	public boolean isValidation() {
		return validation;
	}
	
	public boolean getValidation() {
		return validation;
	}

	public void setValidation(boolean validation) {
		this.validation = validation;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getResponsibility() {
		return responsibility;
	}

	public void setResponsibility(String responsibility) {
		this.responsibility = responsibility;
	}

	public String getLevelCode() {
		return levelCode;
	}

	public void setLevelCode(String levelCode) {
		this.levelCode = levelCode;
	}

	public Integer getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}

	public List<SysRole> getQuarterRoles() {
		return quarterRoles;
	}

	public void setQuarterRoles(List<SysRole> quarterRoles) {
		this.quarterRoles = quarterRoles;
	}

	public List<Quarter> getQuarters() {
		return quarters;
	}

	public void setQuarters(List<Quarter> quarters) {
		this.quarters = quarters;
	}

	public String getQuarterNameEx() {
		if(this.getDept()!=null)
		    return this.quarterName+"("+this.getDept().getDeptName()+")";
		else
			return this.quarterName;
	}

	public String getQuarterTrainId() {
		return quarterTrainId;
	}

	public void setQuarterTrainId(String quarterTrainId) {
		this.quarterTrainId = quarterTrainId;
	}

	public String getQuarterTrainName() {
		return quarterTrainName;
	}

	public void setQuarterTrainName(String quarterTrainName) {
		this.quarterTrainName = quarterTrainName;
	}
	
	
}