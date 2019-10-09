package cn.com.ite.eap2.domain.organization;

import java.util.*;

import cn.com.ite.eap2.domain.power.SysUser;

/**
 * <p>Title cn.com.ite.eap2.domain.organization.Organ</p>
 * <p>Description 机构</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2014</p>
 * @author 宋文科
 * @create time: 2014-7-2 下午03:31:27
 * @version 2.0
 * 
 * @modified records:
 */
public class Organ implements java.io.Serializable {
	private static final long serialVersionUID = 771110254158825141L;
	private String organId;
	private Organ organ;
	private Organ linkOrgan;
	private String area;
	private String organCode;
	private String organName;
	private String organAlias;
	private String organType;
	private Integer orderNo;
	private Long postcode;
	private String address;
	private String telephone;
	private String fax;
	private Boolean validation;
	private String remark;
	private String levelCode;
	private String sysParemeter;
	private String sysParemeterQr;//sysParemeter再次确认
	private String bankMapCode;//题库关联映射编码
	private List<SysUser> organMangers = new ArrayList<SysUser>(0);
	private List<Organ> organs = new ArrayList<Organ>(0);
	private List<Dept> depts = new ArrayList<Dept>(0);
	private List<SysUser> users = new ArrayList<SysUser>(0);

	public List<SysUser> getUsers() {
		return users;
	}

	public void setUsers(List<SysUser> users) {
		this.users = users;
	}

	public String getOrganId() {
		return organId;
	}

	public void setOrganId(String organId) {
		this.organId = organId;
	}


	public Organ getOrgan() {
		return organ;
	}

	public void setOrgan(Organ organ) {
		this.organ = organ;
	}

	public Organ getLinkOrgan() {
		return linkOrgan;
	}

	public void setLinkOrgan(Organ linkOrgan) {
		this.linkOrgan = linkOrgan;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getOrganCode() {
		return organCode;
	}

	public void setOrganCode(String organCode) {
		this.organCode = organCode;
	}

	public String getOrganName() {
		return organName;
	}

	public void setOrganName(String organName) {
		this.organName = organName;
	}

	public String getOrganAlias() {
		return organAlias;
	}

	public void setOrganAlias(String organAlias) {
		this.organAlias = organAlias;
	}

	public String getOrganType() {
		return organType;
	}

	public void setOrganType(String organType) {
		this.organType = organType;
	}

	public Integer getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}

	public Long getPostcode() {
		return postcode;
	}

	public void setPostcode(Long postcode) {
		this.postcode = postcode;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public Boolean isValidation() {
		return validation;
	}
	
	public Boolean getValidation() {
		return validation;
	}

	public void setValidation(Boolean validation) {
		this.validation = validation;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getLevelCode() {
		return levelCode;
	}

	public void setLevelCode(String levelCode) {
		this.levelCode = levelCode;
	}

	public List<SysUser> getOrganMangers() {
		return organMangers;
	}

	public void setOrganMangers(List<SysUser> organMangers) {
		this.organMangers = organMangers;
	}

	public List<Organ> getOrgans() {
		return organs;
	}

	public void setOrgans(List<Organ> organs) {
		this.organs = organs;
	}

	public List<Dept> getDepts() {
		return depts;
	}

	public void setDepts(List<Dept> depts) {
		this.depts = depts;
	}

	
	public String getSysParemeter() {
		return sysParemeter;
	}

	public void setSysParemeter(String sysParemeter) {
		this.sysParemeter = sysParemeter;
	}

	
	
	public String getSysParemeterQr() {
		return sysParemeterQr;
	}

	public void setSysParemeterQr(String sysParemeterQr) {
		this.sysParemeterQr = sysParemeterQr;
	}

	public Organ() {
	}

	public String getBankMapCode() {
		return bankMapCode;
	}

	public void setBankMapCode(String bankMapCode) {
		this.bankMapCode = bankMapCode;
	}
	
	
}