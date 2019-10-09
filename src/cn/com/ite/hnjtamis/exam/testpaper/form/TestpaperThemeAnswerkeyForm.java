package cn.com.ite.hnjtamis.exam.testpaper.form;


/**
 *
 * <p>Title cn.com.ite.hnjtamis.exam.testpaper.form.TestpaperThemeAnswerkeyForm</p>
 * <p>Description 试题模版 - 试题 - 答案</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2015</p>
 * @author 朱健
 * @create time: 2015年4月1日 上午9:51:14
 * @version 1.0
 * 
 * @modified records:
 */
public class TestpaperThemeAnswerkeyForm {
	
	
	private String answerkeyId;//答案ID
	private String themeId;//关联的试题ID
	private String answerkeyValue;///答案值
	private Integer isRight;//是否正确 5：否,10：是
	private Short sortNum;//序号
	private String randomSortnum;//随机数排序号
	private String remark;//备注
	private String themeTypeId;//试题类型ID
	private String themeTypeName;//试题类型
	private String syncFlag;////同步标志
	private String organId;//机构ID
	private String organName;//机构名
	private String lastUpdateDate;///最后修改时间
	private String lastUpdatedBy;//最后修改人
	private String lastUpdatedIdBy;//最后修改人ID
	private String creationDate;//创建时间
	private String createdBy;//创建人
	private String createdIdBy;//创建人ID
	
	
	public String getAnswerkeyId() {
		return answerkeyId;
	}
	public void setAnswerkeyId(String answerkeyId) {
		this.answerkeyId = answerkeyId;
	}
	public String getThemeId() {
		return themeId;
	}
	public void setThemeId(String themeId) {
		this.themeId = themeId;
	}
	public String getAnswerkeyValue() {
		return answerkeyValue;
	}
	public void setAnswerkeyValue(String answerkeyValue) {
		this.answerkeyValue = answerkeyValue;
	}
	public Integer getIsRight() {
		return isRight;
	}
	public void setIsRight(Integer isRight) {
		this.isRight = isRight;
	}
	public Short getSortNum() {
		return sortNum;
	}
	public void setSortNum(Short sortNum) {
		this.sortNum = sortNum;
	}
	public String getRandomSortnum() {
		return randomSortnum;
	}
	public void setRandomSortnum(String randomSortnum) {
		this.randomSortnum = randomSortnum;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
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
	public String getSyncFlag() {
		return syncFlag;
	}
	public void setSyncFlag(String syncFlag) {
		this.syncFlag = syncFlag;
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
	
	

}
