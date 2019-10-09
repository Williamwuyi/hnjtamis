package cn.com.ite.hnjtamis.exam.base.theme.form;

import java.util.ArrayList;
import java.util.List;

import cn.com.ite.eap2.domain.organization.Employee;
import cn.com.ite.hnjtamis.exam.hibernatemap.ThemeType;

/**
 *
 * <p>Title cn.com.ite.hnjtamis.exam.base.theme.form.ThemeForm</p>
 * <p>Description 试题Form</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2015</p>
 * @author 朱健
 * @create time: 2015年3月25日 下午4:51:39
 * @version 1.0
 * 
 * @modified records:
 */
public class ThemeForm {

	private String themeInBankNames;//所属题库名称
	private String themeInBankIds;//所属题库名称
	private String themeInBankCodes;//所属题库名称
	private String themeId;
	//private ThemeType themeType;//题型
	private String themeTypeId;//题型
	private String themeTypeName;//题型
	private String themeInType;//题型(题型中的类型 单选多选等)
	private String specialityIds;//专业
	private String specialityNames;//专业
	private String postIds;//岗位
	private String postNames;//岗位
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
	private String themeAns;//显示答案
	private String themeHisId;//最后一个试题版本ID
	private String imagesPath;//图片路径
	private String imagesPackageName;//图片包名
	private String imagesSucc;//图片是否导入成功说明标识
	private String allowFk;//该试题允许反馈 10-允许 20-不允许
	private List<ThemeAnswerkeyForm> themeAnswerkeyFormList = new ArrayList<ThemeAnswerkeyForm>();//答案
	private List<ThemeSpecialityForm> themeSpecialityFormList = new ArrayList<ThemeSpecialityForm>();//涉及专业
	private List<ThemePostForm> themePostFormList = new ArrayList<ThemePostForm>();//涉及岗位
	private List<ThemeInThemeBankForm> themeBankFormList = new ArrayList<ThemeInThemeBankForm>();//涉及题库
	private List<ThemeFkauditForm> themeFkauditFormList = new ArrayList<ThemeFkauditForm>();//反馈信息
	private String fkRemark;//反馈信息
	
	private Employee lastFkAuditEmp;
	private String lastFkAuditName;//最后反馈审核人
	private String lastFkState;
	private String lastFkAuditTime;//最后反馈审核时间
	
	public List<ThemeFkauditForm> getThemeFkauditFormList() {
		return themeFkauditFormList;
	}
	public void setThemeFkauditFormList(List<ThemeFkauditForm> themeFkauditFormList) {
		this.themeFkauditFormList = themeFkauditFormList;
	}
	public String getFkRemark() {
		return fkRemark;
	}
	public void setFkRemark(String fkRemark) {
		this.fkRemark = fkRemark;
	}
	public String getAllowFk() {
		return allowFk;
	}
	public void setAllowFk(String allowFk) {
		this.allowFk = allowFk;
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
	public String getCheckRemark() {
		return checkRemark;
	}
	public void setCheckRemark(String checkRemark) {
		this.checkRemark = checkRemark;
	}
	public String getThemeInBankNames() {
		return themeInBankNames;
	}
	public void setThemeInBankNames(String themeInBankNames) {
		this.themeInBankNames = themeInBankNames;
	}
	public String getThemeInType() {
		return themeInType;
	}
	public void setThemeInType(String themeInType) {
		this.themeInType = themeInType;
	}
	public String getSpecialityIds() {
		return specialityIds;
	}
	public void setSpecialityIds(String specialityIds) {
		this.specialityIds = specialityIds;
	}
	public String getPostIds() {
		return postIds;
	}
	public void setPostIds(String postIds) {
		this.postIds = postIds;
	}
	public String getPostNames() {
		return postNames;
	}
	public void setPostNames(String postNames) {
		this.postNames = postNames;
	}
	public String getSpecialityNames() {
		return specialityNames;
	}
	public void setSpecialityNames(String specialityNames) {
		this.specialityNames = specialityNames;
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
	public String getSyncFlag() {
		return syncFlag;
	}
	public void setSyncFlag(String syncFlag) {
		this.syncFlag = syncFlag;
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
	public List<ThemeInThemeBankForm> getThemeBankFormList() {
		return themeBankFormList;
	}
	public void setThemeBankFormList(List<ThemeInThemeBankForm> themeBankFormList) {
		this.themeBankFormList = themeBankFormList;
	}
	public List<ThemePostForm> getThemePostFormList() {
		return themePostFormList;
	}
	public void setThemePostFormList(List<ThemePostForm> themePostFormList) {
		this.themePostFormList = themePostFormList;
	}
	public String getThemeId() {
		return themeId;
	}
	public void setThemeId(String themeId) {
		this.themeId = themeId;
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
	public String getKnowledgePoint() {
		return knowledgePoint;
	}
	public void setKnowledgePoint(String knowledgePoint) {
		this.knowledgePoint = knowledgePoint;
	}
	public String getThemeKeyword() {
		return themeKeyword;
	}
	public void setThemeKeyword(String themeKeyword) {
		this.themeKeyword = themeKeyword;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getThemeName() {
		return themeName;
	}
	public void setThemeName(String themeName) {
		this.themeName = themeName;
	}
	public String getScoreType() {
		return scoreType;
	}
	public void setScoreType(String scoreType) {
		this.scoreType = scoreType;
	}
	public Integer getDegree() {
		return degree;
	}
	public void setDegree(Integer degree) {
		this.degree = degree;
	}
	public Integer getEachline() {
		return eachline;
	}
	public void setEachline(Integer eachline) {
		this.eachline = eachline;
	}
	public String getWriteUser() {
		return writeUser;
	}
	public void setWriteUser(String writeUser) {
		this.writeUser = writeUser;
	}
	public String getExplain() {
		return explain;
	}
	public void setExplain(String explain) {
		this.explain = explain;
	}
	public Double getDefaultScore() {
		return defaultScore;
	}
	public void setDefaultScore(Double defaultScore) {
		this.defaultScore = defaultScore;
	}
	public Integer getThemeSetNum() {
		return themeSetNum;
	}
	public void setThemeSetNum(Integer themeSetNum) {
		this.themeSetNum = themeSetNum;
	}
	public Integer getThemePeopleNum() {
		return themePeopleNum;
	}
	public void setThemePeopleNum(Integer themePeopleNum) {
		this.themePeopleNum = themePeopleNum;
	}
	public Integer getThemeRightNum() {
		return themeRightNum;
	}
	public void setThemeRightNum(Integer themeRightNum) {
		this.themeRightNum = themeRightNum;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public Long getSortNum() {
		return sortNum;
	}
	public void setSortNum(Long sortNum) {
		this.sortNum = sortNum;
	}
	public String getIsUse() {
		return isUse;
	}
	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}
	public List<ThemeAnswerkeyForm> getThemeAnswerkeyFormList() {
		return themeAnswerkeyFormList;
	}
	public void setThemeAnswerkeyFormList(
			List<ThemeAnswerkeyForm> themeAnswerkeyFormList) {
		this.themeAnswerkeyFormList = themeAnswerkeyFormList;
	}
	public List<ThemeSpecialityForm> getThemeSpecialityFormList() {
		return themeSpecialityFormList;
	}
	public void setThemeSpecialityFormList(
			List<ThemeSpecialityForm> themeSpecialityFormList) {
		this.themeSpecialityFormList = themeSpecialityFormList;
	}
	public String getThemeInBankIds() {
		return themeInBankIds;
	}
	public void setThemeInBankIds(String themeInBankIds) {
		this.themeInBankIds = themeInBankIds;
	}
	public String getThemeInBankCodes() {
		return themeInBankCodes;
	}
	public void setThemeInBankCodes(String themeInBankCodes) {
		this.themeInBankCodes = themeInBankCodes;
	}
	public Employee getLastFkAuditEmp() {
		return lastFkAuditEmp;
	}
	public void setLastFkAuditEmp(Employee lastFkAuditEmp) {
		this.lastFkAuditEmp = lastFkAuditEmp;
	}
	public String getLastFkState() {
		return lastFkState;
	}
	public void setLastFkState(String lastFkState) {
		this.lastFkState = lastFkState;
	}
	public String getLastFkAuditTime() {
		return lastFkAuditTime;
	}
	public void setLastFkAuditTime(String lastFkAuditTime) {
		this.lastFkAuditTime = lastFkAuditTime;
	}
	public String getLastFkAuditName() {
		return lastFkAuditName;
	}
	public void setLastFkAuditName(String lastFkAuditName) {
		this.lastFkAuditName = lastFkAuditName;
	}
	
}
