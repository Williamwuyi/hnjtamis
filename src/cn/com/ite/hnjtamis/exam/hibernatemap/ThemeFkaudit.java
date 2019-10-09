package cn.com.ite.hnjtamis.exam.hibernatemap;


/**
 * 试题反馈与审核意见
 * @author 朱健
 * @create time: 2016年2月25日 上午8:54:36
 * @version 1.0
 * 
 * @modified records:
 */
public class ThemeFkaudit implements java.io.Serializable {

	// Fields

	private String fkauditId;
	private Theme theme;
	private String fkauditRemark;//反馈说明或意见
	private String themeName;//当前试题
	private String themeAns;//当前答案
	private Double awardCenter;//奖励分数
	private String awardTime;//奖励时间
	private Integer themeVersion;//版本号
	private String themeCode;//试题编码
	private String state;//状态 10-提交反馈  20反馈通过
	private String themeHisId;//试题历史版本ID（来源历史表）
	private String organName;//机构
	private String organId;//机构ID
	private String creationDate;//操作时间
	private String createdBy;//操作人
	private String createdIdBy;///操作人ID
	private String fkType;//类型 10-提出反馈  20-专家指定  30专家审核 40-反馈确认
	private String fkAuditUserName;//指定反馈审核人
	private String fkAuditUserId;//指定反馈审核人ID
	private String fkFinState;//本次反馈是否完成  10-未完成  20-已完成
	private String fkNewState;//是否最后一条反馈  10-否 20-是

	// Constructors

	/** default constructor */
	public ThemeFkaudit() {
	}

	/** full constructor */
	public ThemeFkaudit(Theme theme, String fkauditRemark, String themeName,
			String themeAns, Double awardCenter, String awardTime,
			Integer themeVersion, String themeCode, String state,
			String themeHisId, String organName, String organId,
			String creationDate, String createdBy, String createdIdBy,
			String fkType,String fkAuditUserName,String fkAuditUserId,String fkFinState,String fkNewState) {
		this.theme = theme;
		this.fkauditRemark = fkauditRemark;
		this.themeName = themeName;
		this.themeAns = themeAns;
		this.awardCenter = awardCenter;
		this.awardTime = awardTime;
		this.themeVersion = themeVersion;
		this.themeCode = themeCode;
		this.state = state;
		this.themeHisId = themeHisId;
		this.organName = organName;
		this.organId = organId;
		this.creationDate = creationDate;
		this.createdBy = createdBy;
		this.createdIdBy = createdIdBy;
		this.fkType = fkType;
		this.fkAuditUserName = fkAuditUserName;
		this.fkAuditUserId = fkAuditUserId;
		this.fkFinState = fkFinState;
		this.fkNewState = fkNewState;
	}

	// Property accessors

	public String getFkauditId() {
		return this.fkauditId;
	}

	public void setFkauditId(String fkauditId) {
		this.fkauditId = fkauditId;
	}

	public Theme getTheme() {
		return this.theme;
	}

	public void setTheme(Theme theme) {
		this.theme = theme;
	}

	public String getFkauditRemark() {
		return this.fkauditRemark;
	}

	public void setFkauditRemark(String fkauditRemark) {
		this.fkauditRemark = fkauditRemark;
	}

	public String getThemeName() {
		return this.themeName;
	}

	public void setThemeName(String themeName) {
		this.themeName = themeName;
	}

	public String getThemeAns() {
		return this.themeAns;
	}

	public void setThemeAns(String themeAns) {
		this.themeAns = themeAns;
	}

	public Double getAwardCenter() {
		return this.awardCenter;
	}

	public void setAwardCenter(Double awardCenter) {
		this.awardCenter = awardCenter;
	}

	public String getAwardTime() {
		return this.awardTime;
	}

	public void setAwardTime(String awardTime) {
		this.awardTime = awardTime;
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

	public String getState() {
		return this.state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getThemeHisId() {
		return this.themeHisId;
	}

	public void setThemeHisId(String themeHisId) {
		this.themeHisId = themeHisId;
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

	public String getFkType() {
		return fkType;
	}

	public void setFkType(String fkType) {
		this.fkType = fkType;
	}

	public String getFkAuditUserName() {
		return fkAuditUserName;
	}

	public void setFkAuditUserName(String fkAuditUserName) {
		this.fkAuditUserName = fkAuditUserName;
	}

	public String getFkAuditUserId() {
		return fkAuditUserId;
	}

	public void setFkAuditUserId(String fkAuditUserId) {
		this.fkAuditUserId = fkAuditUserId;
	}

	public String getFkFinState() {
		return fkFinState;
	}

	public void setFkFinState(String fkFinState) {
		this.fkFinState = fkFinState;
	}

	public String getFkNewState() {
		return fkNewState;
	}

	public void setFkNewState(String fkNewState) {
		this.fkNewState = fkNewState;
	}
	
	

}