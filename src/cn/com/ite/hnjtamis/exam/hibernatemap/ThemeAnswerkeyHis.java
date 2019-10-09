package cn.com.ite.hnjtamis.exam.hibernatemap;

/**
 * 试题历史信息 - 答案
 * @author 朱健
 * @create time: 2016年2月25日 上午8:54:22
 * @version 1.0
 * 
 * @modified records:
 */
public class ThemeAnswerkeyHis implements java.io.Serializable {

	// Fields

	private String answerkeyId;
	private ThemeHis themeHis;
	private String answerkeyValue;
	private Integer isRight;
	private Short sortNum;
	private String remark;
	private String themeTypeId;
	private String themeTypeName;

	// Constructors

	/** default constructor */
	public ThemeAnswerkeyHis() {
	}

	/** full constructor */
	public ThemeAnswerkeyHis(ThemeHis themeHis, String answerkeyValue,
			Integer isRight, Short sortNum, String remark, String themeTypeId,
			String themeTypeName) {
		this.themeHis = themeHis;
		this.answerkeyValue = answerkeyValue;
		this.isRight = isRight;
		this.sortNum = sortNum;
		this.remark = remark;
		this.themeTypeId = themeTypeId;
		this.themeTypeName = themeTypeName;
	}

	// Property accessors

	public String getAnswerkeyId() {
		return this.answerkeyId;
	}

	public void setAnswerkeyId(String answerkeyId) {
		this.answerkeyId = answerkeyId;
	}

	public ThemeHis getThemeHis() {
		return this.themeHis;
	}

	public void setThemeHis(ThemeHis themeHis) {
		this.themeHis = themeHis;
	}

	public String getAnswerkeyValue() {
		return this.answerkeyValue;
	}

	public void setAnswerkeyValue(String answerkeyValue) {
		this.answerkeyValue = answerkeyValue;
	}

	public Integer getIsRight() {
		return this.isRight;
	}

	public void setIsRight(Integer isRight) {
		this.isRight = isRight;
	}

	public Short getSortNum() {
		return this.sortNum;
	}

	public void setSortNum(Short sortNum) {
		this.sortNum = sortNum;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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

}