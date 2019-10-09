package cn.com.ite.hnjtamis.exam.base.theme.form;


public class ThemeFkauditForm {
	
	private String fkauditId;
	private String fkauditRemark;//反馈说明或意见
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
	
	public String getFkauditId() {
		return fkauditId;
	}
	public void setFkauditId(String fkauditId) {
		this.fkauditId = fkauditId;
	}
	public String getFkauditRemark() {
		return fkauditRemark;
	}
	public void setFkauditRemark(String fkauditRemark) {
		this.fkauditRemark = fkauditRemark;
	}
	public Double getAwardCenter() {
		return awardCenter;
	}
	public void setAwardCenter(Double awardCenter) {
		this.awardCenter = awardCenter;
	}
	public String getAwardTime() {
		return awardTime;
	}
	public void setAwardTime(String awardTime) {
		this.awardTime = awardTime;
	}
	public Integer getThemeVersion() {
		return themeVersion;
	}
	public void setThemeVersion(Integer themeVersion) {
		this.themeVersion = themeVersion;
	}
	public String getThemeCode() {
		return themeCode;
	}
	public void setThemeCode(String themeCode) {
		this.themeCode = themeCode;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getThemeHisId() {
		return themeHisId;
	}
	public void setThemeHisId(String themeHisId) {
		this.themeHisId = themeHisId;
	}
	public String getOrganName() {
		return organName;
	}
	public void setOrganName(String organName) {
		this.organName = organName;
	}
	public String getOrganId() {
		return organId;
	}
	public void setOrganId(String organId) {
		this.organId = organId;
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
