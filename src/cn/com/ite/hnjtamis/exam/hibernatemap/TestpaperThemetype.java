package cn.com.ite.hnjtamis.exam.hibernatemap;

/**
 * TestpaperThemetype entity. @author MyEclipse Persistence Tools
 */

public class TestpaperThemetype implements java.io.Serializable {

	// Fields

	private String testpaperThemetypeId;//试卷-题型ID
	private ThemeType themeType;//题型
	private Testpaper testpaper;///试卷
	private String bankId;//题库ID
	private String rankRate;/////难度比例 {容易,难,很难}{0，0，0}逗号分割，表示某试卷某题型的难度分布
	private String professionId;//专业ID
	private String professionName;//专业
	private String professionLevelCode;//专业级别编码
	private Short rate;//比例%
	private Double score;//按分数筛选时为分数,按题数筛选时为题数
	private String setThemetype;//配置方式 10-按分数 20-按题数
	private Integer total;///题数
	private Integer sortNum;//排序号
	private String syncFlag;////同步标志
	private String lastUpdateDate;///最后修改时间
	private String lastUpdatedBy;//最后修改人
	private String lastUpdatedIdBy;//最后修改人ID
	private String creationDate;//创建时间
	private String createdBy;//创建人
	private String createdIdBy;//创建人ID
	
	private Double actualScore;//实际分数
	private Integer actualTotal;//实际题数

	// Constructors

	/** default constructor */
	public TestpaperThemetype() {
	}

	/** minimal constructor */
	public TestpaperThemetype(Double score) {
		this.score = score;
	}

	/** full constructor */
	public TestpaperThemetype(ThemeType themeType, Testpaper testpaper,
			String bankId, String rankRate, String professionId, String professionName,
			String professionLevelCode, Short rate, Double score,
			String setThemetype, Integer total, Integer sortNum, String syncFlag,
			String lastUpdateDate, String lastUpdatedBy,
			String lastUpdatedIdBy, String creationDate, String createdBy,
			String createdIdBy,Double actualScore,Integer actualTotal) {
		this.themeType = themeType;
		this.bankId = bankId;
		this.testpaper = testpaper;
		this.rankRate = rankRate;
		this.professionId = professionId;
		this.professionName = professionName;
		this.professionLevelCode = professionLevelCode;
		this.rate = rate;
		this.score = score;
		this.setThemetype = setThemetype;
		this.total = total;
		this.sortNum = sortNum;
		this.syncFlag = syncFlag;
		this.lastUpdateDate = lastUpdateDate;
		this.lastUpdatedBy = lastUpdatedBy;
		this.lastUpdatedIdBy = lastUpdatedIdBy;
		this.creationDate = creationDate;
		this.createdBy = createdBy;
		this.createdIdBy = createdIdBy;
		this.actualScore = actualScore;
		this.actualTotal = actualTotal;
	}

	// Property accessors

	
	
	
	public String getTestpaperThemetypeId() {
		return this.testpaperThemetypeId;
	}

	public void setTestpaperThemetypeId(String testpaperThemetypeId) {
		this.testpaperThemetypeId = testpaperThemetypeId;
	}

	public String getBankId() {
		return bankId;
	}

	public void setBankId(String bankId) {
		this.bankId = bankId;
	}
	
	public ThemeType getThemeType() {
		return this.themeType;
	}

	public void setThemeType(ThemeType themeType) {
		this.themeType = themeType;
	}

	public Testpaper getTestpaper() {
		return this.testpaper;
	}

	public void setTestpaper(Testpaper testpaper) {
		this.testpaper = testpaper;
	}

	public String getRankRate() {
		return this.rankRate;
	}

	public void setRankRate(String rankRate) {
		this.rankRate = rankRate;
	}

	public String getProfessionId() {
		return this.professionId;
	}

	public void setProfessionId(String professionId) {
		this.professionId = professionId;
	}

	public String getProfessionName() {
		return this.professionName;
	}

	public void setProfessionName(String professionName) {
		this.professionName = professionName;
	}

	public String getProfessionLevelCode() {
		return this.professionLevelCode;
	}

	public void setProfessionLevelCode(String professionLevelCode) {
		this.professionLevelCode = professionLevelCode;
	}

	public Short getRate() {
		return this.rate;
	}

	public void setRate(Short rate) {
		this.rate = rate;
	}

	public Double getScore() {
		return this.score;
	}

	public void setScore(Double score) {
		this.score = score;
	}

	public String getSetThemetype() {
		return this.setThemetype;
	}

	public void setSetThemetype(String setThemetype) {
		this.setThemetype = setThemetype;
	}

	public Integer getTotal() {
		return this.total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public Integer getSortNum() {
		return this.sortNum;
	}

	public void setSortNum(Integer sortNum) {
		this.sortNum = sortNum;
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
	
	public Double getActualScore() {
		return actualScore;
	}

	public void setActualScore(Double actualScore) {
		this.actualScore = actualScore;
	}

	public Integer getActualTotal() {
		return actualTotal;
	}

	public void setActualTotal(Integer actualTotal) {
		this.actualTotal = actualTotal;
	}

}