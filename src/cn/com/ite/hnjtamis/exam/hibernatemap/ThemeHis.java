package cn.com.ite.hnjtamis.exam.hibernatemap;

import java.util.ArrayList;
import java.util.List;

/**
 * ThemeHis entity. @author MyEclipse Persistence Tools
 */

/**
 * 试题历史信息
 * @author 朱健
 * @create time: 2016年2月25日 上午8:54:08
 * @version 1.0
 * 
 * @modified records:
 */
public class ThemeHis implements java.io.Serializable {

	// Fields

	private String themeHisId;
	private String themeTypeId;
	private String themeTypeName;
	private String themeName;
	private String organId;
	private String organName;
	private String themeSourceId;
	private Integer themeVersion;
	private String themeCode;
	private String themeAns;
	private String creationDate;
	private String createdBy;
	private String createdIdBy;
	private String lockState;//状态  10-锁定处理中   20-已完成
	private List<ThemeAnswerkeyHis> themeAnswerkeyHises = new ArrayList<ThemeAnswerkeyHis>(0);

	// Constructors

	/** default constructor */
	public ThemeHis() {
	}

	/** full constructor */
	public ThemeHis(String themeTypeId, String themeTypeName, String themeName,
			String organId, String organName, String themeSourceId,
			Integer themeVersion, String themeCode, String themeAns,
			String creationDate, String createdBy, String createdIdBy,String lockState,
			List<ThemeAnswerkeyHis> themeAnswerkeyHises) {
		this.themeTypeId = themeTypeId;
		this.themeTypeName = themeTypeName;
		this.themeName = themeName;
		this.organId = organId;
		this.organName = organName;
		this.themeSourceId = themeSourceId;
		this.themeVersion = themeVersion;
		this.themeCode = themeCode;
		this.themeAns = themeAns;
		this.creationDate = creationDate;
		this.createdBy = createdBy;
		this.createdIdBy = createdIdBy;
		this.lockState=lockState;
		this.themeAnswerkeyHises = themeAnswerkeyHises;
	}

	// Property accessors

	public String getThemeHisId() {
		return this.themeHisId;
	}

	public void setThemeHisId(String themeHisId) {
		this.themeHisId = themeHisId;
	}

	public String getThemeTypeId() {
		return this.themeTypeId;
	}

	public void setThemeTypeId(String themeTypeId) {
		this.themeTypeId = themeTypeId;
	}

	public String getThemeTypeName() {
		return this.themeTypeName;
	}

	public void setThemeTypeName(String themeTypeName) {
		this.themeTypeName = themeTypeName;
	}

	public String getThemeName() {
		return this.themeName;
	}

	public void setThemeName(String themeName) {
		this.themeName = themeName;
	}

	public String getOrganId() {
		return this.organId;
	}

	public void setOrganId(String organId) {
		this.organId = organId;
	}

	public String getOrganName() {
		return this.organName;
	}

	public void setOrganName(String organName) {
		this.organName = organName;
	}

	public String getThemeSourceId() {
		return this.themeSourceId;
	}

	public void setThemeSourceId(String themeSourceId) {
		this.themeSourceId = themeSourceId;
	}

	public Integer getThemeVersion() {
		return this.themeVersion;
	}

	public void setThemeVersion(Integer themeVersion) {
		this.themeVersion = themeVersion;
	}

	public String getThemeCode() {
		return this.themeCode;
	}

	public void setThemeCode(String themeCode) {
		this.themeCode = themeCode;
	}

	public String getThemeAns() {
		return this.themeAns;
	}

	public void setThemeAns(String themeAns) {
		this.themeAns = themeAns;
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

	public List<ThemeAnswerkeyHis> getThemeAnswerkeyHises() {
		return this.themeAnswerkeyHises;
	}

	public void setThemeAnswerkeyHises(List<ThemeAnswerkeyHis> themeAnswerkeyHises) {
		this.themeAnswerkeyHises = themeAnswerkeyHises;
	}

	public String getLockState() {
		return lockState;
	}

	public void setLockState(String lockState) {
		this.lockState = lockState;
	}
	
	

}