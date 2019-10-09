package cn.com.ite.hnjtamis.exam.hibernatemap;

/**
 * TestpaperThemeAnswerkey entity. @author MyEclipse Persistence Tools
 */

public class TestpaperThemeAnswerkey implements java.io.Serializable {

	// Fields

	private String answerkeyId;//答案ID
	private TestpaperTheme testpaperTheme;//考试模版-试题
	private String themeId;//关联的试题ID
	private String answerkeyValue;///答案值
	private Integer isRight;//是否正确 5：否,10：是
	private Integer sortNum;//序号
	private Integer randomSortnum;//随机数排序号
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

	// Constructors

	/** default constructor */
	public TestpaperThemeAnswerkey() {
	}

	/** full constructor */
	public TestpaperThemeAnswerkey(TestpaperTheme testpaperTheme,
			String themeId, String answerkeyValue, Integer isRight, Integer sortNum,
			Integer randomSortnum, String remark, String themeTypeId,
			String themeTypeName, String syncFlag, String organId,
			String organName, String lastUpdateDate, String lastUpdatedBy,
			String lastUpdatedIdBy, String creationDate, String createdBy,
			String createdIdBy) {
		this.testpaperTheme = testpaperTheme;
		this.themeId = themeId;
		this.answerkeyValue = answerkeyValue;
		this.isRight = isRight;
		this.sortNum = sortNum;
		this.randomSortnum = randomSortnum;
		this.remark = remark;
		this.themeTypeId = themeTypeId;
		this.themeTypeName = themeTypeName;
		this.syncFlag = syncFlag;
		this.organId = organId;
		this.organName = organName;
		this.lastUpdateDate = lastUpdateDate;
		this.lastUpdatedBy = lastUpdatedBy;
		this.lastUpdatedIdBy = lastUpdatedIdBy;
		this.creationDate = creationDate;
		this.createdBy = createdBy;
		this.createdIdBy = createdIdBy;
	}

	// Property accessors

	public String getAnswerkeyId() {
		return this.answerkeyId;
	}

	public void setAnswerkeyId(String answerkeyId) {
		this.answerkeyId = answerkeyId;
	}

	public TestpaperTheme getTestpaperTheme() {
		return this.testpaperTheme;
	}

	public void setTestpaperTheme(TestpaperTheme testpaperTheme) {
		this.testpaperTheme = testpaperTheme;
	}

	public String getThemeId() {
		return this.themeId;
	}

	public void setThemeId(String themeId) {
		this.themeId = themeId;
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

	public Integer getSortNum() {
		return this.sortNum;
	}

	public void setSortNum(Integer sortNum) {
		this.sortNum = sortNum;
	}

	public Integer getRandomSortnum() {
		return this.randomSortnum;
	}

	public void setRandomSortnum(Integer randomSortnum) {
		this.randomSortnum = randomSortnum;
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

	public String getSyncFlag() {
		return this.syncFlag;
	}

	public void setSyncFlag(String syncFlag) {
		this.syncFlag = syncFlag;
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

}