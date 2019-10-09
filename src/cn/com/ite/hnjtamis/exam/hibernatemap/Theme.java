package cn.com.ite.hnjtamis.exam.hibernatemap;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cn.com.ite.eap2.domain.organization.Employee;

/**
 * Theme entity. @author MyEclipse Persistence Tools
 */

public class Theme implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 4534826412646393472L;
	
	private String themeId;
	private ThemeType themeType;//题型
	private String themeTypeName;//题型
	private String knowledgePoint;//所属知识点
	private String themeKeyword;//关键字
	private String type;//所属题库类型 10-正式 20-模拟 30-非正式 40-都可以
	private String themeName;//试题
	private String scoreType;//得分类型 0自动阅卷；1人工阅卷；2系统外阅卷
	private Integer degree;//难度 5：容易,10：一般15：难,20：很难
	private Integer eachline;//答案显示方式 0:不换行　1:一行一个 2:一行二个 3：一行三个 4:一行四个
	private String writeUser;//出题人
	private String explain;//注解
	private Double defaultScore;//默认分数
	private Integer themeSetNum;//出题次数
	private Integer themePeopleNum;//考试人次
	private Integer themeRightNum;//答题正确数
	private String remark;//备注
	private Integer state;//状态 5:保存10:上报15:发布20:打回
	private Long sortNum;//排序
	private String isUse;//有否有效 5:有效 10：无效
	private String organId;//机构ID
	private String organName;//机构名称
	private String syncFlag;//同步标志
	private String lastUpdateDate;///最后修改时间
	private String lastUpdatedBy;//最后修改人
	private String lastUpdatedIdBy;//最后修改人ID
	private String creationDate;//创建时间
	private String createdBy;//创建人
	private String createdIdBy;//创建人ID
	private String checkRemark;//导入校验问题说明
	private String haveImages;//是否存在图片文件
	private String imagesNames;//图片文件名字
	private Integer themeVersion;//版本号
	private String themeCode;//试题编码
	private String themeCrc;//试题校验码
	private Integer crcnum;
	private String themeAns;//显示答案
	private String themeHisId;//最后一个试题版本ID
	private String imagesPath;//图片路径
	private String imagesPackageName;//图片包名
	private String imagesSucc;//图片是否导入成功说明标识
	private String allowFk;//该试题允许反馈 10-允许 20-不允许
	
	private Employee lastFkAuditEmp;//反馈审核人
	private String lastFkAuditName;//最后反馈审核人
	private String lastFkAuditId;//最后反馈审核人ID
	private String lastFkAuditTime;//最后反馈审核时间
	private String lastFkState;//最后反馈状态  10-无反馈  20-存在反馈  30-指定了反馈审核人  40-反馈审核人审核完成  -40 - 打回需要重新指定反馈审核人  50-修改完成 
	
	private String lastFkEmployeeName;//最后反馈人
	private String lastFkEmployeeId;//最后反馈人ID
	private String lastFkOrganId;//最后反馈机构ID
	
	private List<ThemeAnswerkey> themeAnswerkeies = new ArrayList<ThemeAnswerkey>(0);//答案
	private List<TestpaperTheme> testpaperThemes = new ArrayList<TestpaperTheme>(0);//关联试题模版
	private List<ThemeSearchKey> themeSearchKeies = new ArrayList<ThemeSearchKey>(0);//查询关键字
	private List<ThemeInBank> themeInBanks = new ArrayList<ThemeInBank>(0);//关联题库
	private List<ThemeFkaudit> themeFkaudits = new ArrayList<ThemeFkaudit>(0);//反馈意见与审核内容
	// Constructors

	/** default constructor */
	public Theme() {
	}

	/** full constructor */
	public Theme(ThemeType themeType, String themeTypeName,
			String knowledgePoint, String themeKeyword, String type,
			String themeName, String scoreType, Integer degree, Integer eachline,
			String writeUser, String explain, Double defaultScore,
			Integer themeSetNum, Integer themePeopleNum, Integer themeRightNum,
			String remark, Integer state, Long sortNum, String isUse,
			String organId, String organName, String syncFlag,
			String lastUpdateDate, String lastUpdatedBy,
			String lastUpdatedIdBy, String creationDate, String createdBy,String checkRemark,
			String createdIdBy, List<ThemeInBank> themeInBanks, List<ThemeAnswerkey> themeAnswerkeies,
			List<TestpaperTheme> testpaperThemes, List<ThemeSearchKey> themeSearchKeies,
			String haveImages,String imagesNames,
			Integer themeVersion,String themeCode,String themeCrc,String themeAns,String themeHisId,String allowFk,
			String imagesPath,String imagesPackageName,String imagesSucc,List<ThemeFkaudit> themeFkaudits,
			String lastFkAuditName,String lastFkAuditId,String lastFkAuditTime,String lastFkState,
			String lastFkEmployeeName,String lastFkEmployeeId,String lastFkOrganId) {
		this.themeType = themeType;
		this.themeTypeName = themeTypeName;
		this.knowledgePoint = knowledgePoint;
		this.themeKeyword = themeKeyword;
		this.type = type;
		this.themeName = themeName;
		this.scoreType = scoreType;
		this.degree = degree;
		this.eachline = eachline;
		this.writeUser = writeUser;
		this.explain = explain;
		this.defaultScore = defaultScore;
		this.themeSetNum = themeSetNum;
		this.themePeopleNum = themePeopleNum;
		this.themeRightNum = themeRightNum;
		this.remark = remark;
		this.state = state;
		this.sortNum = sortNum;
		this.isUse = isUse;
		this.organId = organId;
		this.organName = organName;
		this.syncFlag = syncFlag;
		this.lastUpdateDate = lastUpdateDate;
		this.lastUpdatedBy = lastUpdatedBy;
		this.lastUpdatedIdBy = lastUpdatedIdBy;
		this.creationDate = creationDate;
		this.createdBy = createdBy;
		this.createdIdBy = createdIdBy;
		this.themeInBanks = themeInBanks;
		this.themeAnswerkeies = themeAnswerkeies;
		this.testpaperThemes = testpaperThemes;
		this.themeSearchKeies = themeSearchKeies;
		this.checkRemark=checkRemark;
		this.haveImages = haveImages;
		this.imagesNames = imagesNames;
		this.themeVersion = themeVersion;
		this.themeCode  = themeCode;
		this.themeCrc = themeCrc;
		this.themeAns = themeAns;
		this.themeHisId = themeHisId;
		this.imagesPath = imagesPath;
		this.imagesPackageName = imagesPackageName;
		this.imagesSucc = imagesSucc;
		this.themeFkaudits= themeFkaudits;
		this.allowFk=allowFk;
		this.lastFkAuditName=lastFkAuditName;
		this.lastFkAuditId=lastFkAuditId;
		this.lastFkAuditTime=lastFkAuditTime;
		this.lastFkState=lastFkState;
		this.lastFkEmployeeName=lastFkEmployeeName;
		this.lastFkEmployeeId=lastFkEmployeeId;
		this.lastFkOrganId=lastFkOrganId;
	}

	// Property accessors

	
	
	public String getThemeId() {
		return this.themeId;
	}

	public String getAllowFk() {
		return allowFk;
	}

	public void setAllowFk(String allowFk) {
		this.allowFk = allowFk;
	}

	public List<ThemeFkaudit> getThemeFkaudits() {
		return themeFkaudits;
	}

	public void setThemeFkaudits(List<ThemeFkaudit> themeFkaudits) {
		this.themeFkaudits = themeFkaudits;
	}

	public String getImagesPath() {
		return imagesPath;
	}

	public void setImagesPath(String imagesPath) {
		this.imagesPath = imagesPath;
	}

	public String getImagesPackageName() {
		return imagesPackageName;
	}

	public void setImagesPackageName(String imagesPackageName) {
		this.imagesPackageName = imagesPackageName;
	}

	public String getImagesSucc() {
		return imagesSucc;
	}

	public void setImagesSucc(String imagesSucc) {
		this.imagesSucc = imagesSucc;
	}

	public String getHaveImages() {
		return haveImages;
	}

	public void setHaveImages(String haveImages) {
		this.haveImages = haveImages;
	}

	public String getImagesNames() {
		return imagesNames;
	}

	public void setImagesNames(String imagesNames) {
		this.imagesNames = imagesNames;
	}

	public String getCheckRemark() {
		return checkRemark;
	}

	public void setCheckRemark(String checkRemark) {
		this.checkRemark = checkRemark;
	}

	public void setThemeId(String themeId) {
		this.themeId = themeId;
	}

	public ThemeType getThemeType() {
		return this.themeType;
	}

	public void setThemeType(ThemeType themeType) {
		this.themeType = themeType;
	}

	public String getThemeTypeName() {
		return this.themeTypeName;
	}

	public void setThemeTypeName(String themeTypeName) {
		this.themeTypeName = themeTypeName;
	}

	public String getKnowledgePoint() {
		return this.knowledgePoint;
	}

	public void setKnowledgePoint(String knowledgePoint) {
		this.knowledgePoint = knowledgePoint;
	}

	public String getThemeKeyword() {
		return this.themeKeyword;
	}

	public void setThemeKeyword(String themeKeyword) {
		this.themeKeyword = themeKeyword;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getThemeName() {
		return this.themeName;
	}

	public void setThemeName(String themeName) {
		this.themeName = themeName;
	}

	public String getScoreType() {
		return this.scoreType;
	}

	public void setScoreType(String scoreType) {
		this.scoreType = scoreType;
	}

	public Integer getDegree() {
		return this.degree;
	}

	public void setDegree(Integer degree) {
		this.degree = degree;
	}

	public Integer getEachline() {
		return this.eachline;
	}

	public void setEachline(Integer eachline) {
		this.eachline = eachline;
	}

	public String getWriteUser() {
		return this.writeUser;
	}

	public void setWriteUser(String writeUser) {
		this.writeUser = writeUser;
	}

	public String getExplain() {
		return this.explain;
	}

	public void setExplain(String explain) {
		this.explain = explain;
	}

	public Double getDefaultScore() {
		return this.defaultScore;
	}

	public void setDefaultScore(Double defaultScore) {
		this.defaultScore = defaultScore;
	}

	public Integer getThemeSetNum() {
		return this.themeSetNum;
	}

	public void setThemeSetNum(Integer themeSetNum) {
		this.themeSetNum = themeSetNum;
	}

	public Integer getThemePeopleNum() {
		return this.themePeopleNum;
	}

	public void setThemePeopleNum(Integer themePeopleNum) {
		this.themePeopleNum = themePeopleNum;
	}

	public Integer getThemeRightNum() {
		return this.themeRightNum;
	}

	public void setThemeRightNum(Integer themeRightNum) {
		this.themeRightNum = themeRightNum;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getState() {
		return this.state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Long getSortNum() {
		return this.sortNum;
	}

	public void setSortNum(Long sortNum) {
		this.sortNum = sortNum;
	}

	public String getIsUse() {
		return this.isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
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

	public List<ThemeInBank> getThemeInBanks() {
		return themeInBanks;
	}

	public void setThemeInBanks(List<ThemeInBank> themeInBanks) {
		this.themeInBanks = themeInBanks;
	}

	public List<ThemeAnswerkey> getThemeAnswerkeies() {
		return themeAnswerkeies;
	}

	public void setThemeAnswerkeies(List<ThemeAnswerkey> themeAnswerkeies) {
		this.themeAnswerkeies = themeAnswerkeies;
	}

	public List<TestpaperTheme> getTestpaperThemes() {
		return testpaperThemes;
	}

	public void setTestpaperThemes(List<TestpaperTheme> testpaperThemes) {
		this.testpaperThemes = testpaperThemes;
	}

	public List<ThemeSearchKey> getThemeSearchKeies() {
		return themeSearchKeies;
	}

	public void setThemeSearchKeies(List<ThemeSearchKey> themeSearchKeies) {
		this.themeSearchKeies = themeSearchKeies;
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

	public String getThemeCrc() {
		return themeCrc;
	}

	public void setThemeCrc(String themeCrc) {
		this.themeCrc = themeCrc;
	}

	public String getThemeAns() {
		return themeAns;
	}

	public void setThemeAns(String themeAns) {
		this.themeAns = themeAns;
	}

	public String getThemeHisId() {
		return themeHisId;
	}

	public void setThemeHisId(String themeHisId) {
		this.themeHisId = themeHisId;
	}

	public Integer getCrcnum() {
		return crcnum;
	}

	public void setCrcnum(Integer crcnum) {
		this.crcnum = crcnum;
	}

	public String getLastFkAuditName() {
		return lastFkAuditName;
	}

	public void setLastFkAuditName(String lastFkAuditName) {
		this.lastFkAuditName = lastFkAuditName;
	}

	public String getLastFkAuditId() {
		return lastFkAuditId;
	}

	public void setLastFkAuditId(String lastFkAuditId) {
		this.lastFkAuditId = lastFkAuditId;
	}

	public String getLastFkAuditTime() {
		return lastFkAuditTime;
	}

	public void setLastFkAuditTime(String lastFkAuditTime) {
		this.lastFkAuditTime = lastFkAuditTime;
	}

	public String getLastFkState() {
		return lastFkState;
	}

	public void setLastFkState(String lastFkState) {
		this.lastFkState = lastFkState;
	}

	public Employee getLastFkAuditEmp() {
		return lastFkAuditEmp;
	}

	public void setLastFkAuditEmp(Employee lastFkAuditEmp) {
		this.lastFkAuditEmp = lastFkAuditEmp;
	}

	public String getLastFkEmployeeName() {
		return lastFkEmployeeName;
	}

	public void setLastFkEmployeeName(String lastFkEmployeeName) {
		this.lastFkEmployeeName = lastFkEmployeeName;
	}

	public String getLastFkEmployeeId() {
		return lastFkEmployeeId;
	}

	public void setLastFkEmployeeId(String lastFkEmployeeId) {
		this.lastFkEmployeeId = lastFkEmployeeId;
	}

	public String getLastFkOrganId() {
		return lastFkOrganId;
	}

	public void setLastFkOrganId(String lastFkOrganId) {
		this.lastFkOrganId = lastFkOrganId;
	}

	

}