package cn.com.ite.eap2.domain.organization;

import java.util.*;

import org.apache.struts2.json.annotations.JSON;

import cn.com.ite.eap2.domain.power.SysUser;
import cn.com.ite.hnjtamis.jobstandard.domain.QuarterStandard;

/**
 * <p>Title cn.com.ite.eap2.domain.organization.Employee</p>
 * <p>Description 员工</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2014</p>
 * @author 宋文科
 * @create time: 2014-7-2 下午03:33:06
 * @version 2.0
 * 
 * @modified records:
 */
public class Employee implements java.io.Serializable {
	private static final long serialVersionUID = 1930788062509525373L;
	private String employeeId;
	//主岗位
	private Quarter quarter;
	private Dept dept;
	private String employeeCode;
	private String employeeName;
	private String simpleName;
	private String alias;
	private int sex;
	private String nationality;
	private Date birthday;
	private String nativeplace;
	private String identityCard;
	private String officePhone;
	private String addressPhone;
	private String movePhone;
	private String fax;
	private String email;
	private String address;
	private String postalCode;
	private boolean validation;
	private String remark;
	private Integer orderNo;
	private String groupId;
	private String oldQuarterTrainId;//未修改前培训岗位，用于保存时判断如果与现在的培训岗位相同，不新增岗位用
	private String quarterTrainCode;//培训岗位编码
	private String quarterTrainId;//培训岗位ID
	private String quarterTrainName;//培训岗位Name
	private QuarterStandard quarterStandard;
	
	//从岗位
	private List<Quarter> employeeQuarters = new ArrayList<Quarter>(0);
	private Set<SysUser> sysUsers = new HashSet<SysUser>(0);
	public String getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	public Quarter getQuarter() {
		return quarter;
	}
	public void setQuarter(Quarter quarter) {
		this.quarter = quarter;
	}
	public Dept getDept() {
		return dept;
	}
	public void setDept(Dept dept) {
		this.dept = dept;
	}
	public String getEmployeeCode() {
		return employeeCode;
	}
	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}
	public String getEmployeeName() {
		return employeeName;
	}
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
	public String getSimpleName() {
		return simpleName;
	}
	public void setSimpleName(String simpleName) {
		this.simpleName = simpleName;
	}
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public int getSex() {
		return sex;
	}
	public void setSex(int sex) {
		this.sex = sex;
	}
	public String getNationality() {
		return nationality;
	}
	public void setNationality(String nationality) {
		this.nationality = nationality;
	}
	@JSON(format="yyyy-MM-dd")
	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	public String getNativeplace() {
		return nativeplace;
	}
	public void setNativeplace(String nativeplace) {
		this.nativeplace = nativeplace;
	}
	public String getIdentityCard() {
		return identityCard;
	}
	public void setIdentityCard(String identityCard) {
		this.identityCard = identityCard;
	}
	public String getOfficePhone() {
		return officePhone;
	}
	public void setOfficePhone(String officePhone) {
		this.officePhone = officePhone;
	}
	public String getAddressPhone() {
		return addressPhone;
	}
	public void setAddressPhone(String addressPhone) {
		this.addressPhone = addressPhone;
	}
	public String getMovePhone() {
		return movePhone;
	}
	public void setMovePhone(String movePhone) {
		this.movePhone = movePhone;
	}
	public String getFax() {
		return fax;
	}
	public void setFax(String fax) {
		this.fax = fax;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPostalCode() {
		return postalCode;
	}
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
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
	public Integer getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public List<Quarter> getEmployeeQuarters() {
		return employeeQuarters;
	}
	public void setEmployeeQuarters(List<Quarter> employeeQuarters) {
		this.employeeQuarters = employeeQuarters;
	}
	public Set<SysUser> getSysUsers() {
		return sysUsers;
	}
	public void setSysUsers(Set<SysUser> sysUsers) {
		this.sysUsers = sysUsers;
	}
	public String getQuarterTrainCode() {
		return quarterTrainCode;
	}
	public void setQuarterTrainCode(String quarterTrainCode) {
		this.quarterTrainCode = quarterTrainCode;
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
	public QuarterStandard getQuarterStandard() {
		return quarterStandard;
	}
	public void setQuarterStandard(QuarterStandard quarterStandard) {
		this.quarterStandard = quarterStandard;
	}
	public String getOldQuarterTrainId() {
		return oldQuarterTrainId;
	}
	public void setOldQuarterTrainId(String oldQuarterTrainId) {
		this.oldQuarterTrainId = oldQuarterTrainId;
	}
	
	
}