package cn.com.ite.hnjtamis.exam.testpaper.form;

import java.util.ArrayList;
import java.util.List;

import cn.com.ite.hnjtamis.exam.base.theme.form.ThemeInThemeBankForm;

/**
 *
 * <p>Title cn.com.ite.hnjtamis.exam.testpaper.form.TestpaperForm</p>
 * <p>Description 考试模版配置</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2015</p>
 * @author 朱健
 * @create time: 2015年4月1日 上午9:25:07
 * @version 1.0
 * 
 * @modified records:
 */
public class TestpaperForm {

	
	private String testpaperId;//试卷模版ID
	private String testpaperName;//试卷名称
	private String testpaperType;//试卷生成方式  10-同试卷打乱  20-同试卷同顺序 30-各考生按模版随机抽题
	private String examTypeId;//考试类型(性质)ID，数据字典定义（安规、岗位、竞赛等）
	private String examTypeName;//考试类型(性质)，与考试类型ID一组
	private Integer examProperty;//考试类型  10- 达标考试 20-竞赛考试 30-培训测试 40-模拟考试
	private Short totalTheme;//总题数
	private Double totalScore;//总分数
	private Double testpaperRank;//难度系数
	private Integer screeningMethods;//筛选方式(5：分数，10：题数)
	private Short testpaperTime;//参考考时（分钟）
	private Integer isUse;//是否使用(5：否,10：是)
	private String isPrivate;//是否私有(5：否,10：是)
	private String remark;//备注
	private String checkUser;//审核人
	private String checkUserId;//审核人ID
	private String checkTime;//审核时间
	private String checkIdear;///审核意见
	private String state;//状态 5：未上报，10：等待审核，15：审核通过，20：审核打回
	private Integer useNum;//使用次数
	private String organ;//机构名
	private String organId;//机构编号e
	private String syncFlag;////同步标志
	private String lastUpdateDate;///最后修改时间
	private String lastUpdatedBy;//最后修改人
	private String lastUpdatedIdBy;//最后修改人ID
	private String creationDate;//创建时间
	private String createdBy;//创建人
	private String createdIdBy;//创建人ID
	
	private List<ThemeInThemeBankForm> themeBankFormList =new ArrayList<ThemeInThemeBankForm>();
	private List<TestpaperThemeForm> testpaperThemeFormList = new ArrayList<TestpaperThemeForm>();
	private List<TestpaperThemetypeForm> testpaperThemetypeFormList = new ArrayList<TestpaperThemetypeForm>();
	
	
	private String themeIds;//试题ID
	private String themeDefScore;//默认得分
	private String addThemeTypes;//修改方式
	private String testpaperThemeIds;//模版试题ID
	
	
	
	public List<ThemeInThemeBankForm> getThemeBankFormList() {
		return themeBankFormList;
	}
	public void setThemeBankFormList(List<ThemeInThemeBankForm> themeBankFormList) {
		this.themeBankFormList = themeBankFormList;
	}
	public String getTestpaperThemeIds() {
		return testpaperThemeIds;
	}
	public void setTestpaperThemeIds(String testpaperThemeIds) {
		this.testpaperThemeIds = testpaperThemeIds;
	}
	public String getThemeIds() {
		return themeIds;
	}
	public void setThemeIds(String themeIds) {
		this.themeIds = themeIds;
	}
	public String getAddThemeTypes() {
		return addThemeTypes;
	}
	public void setAddThemeTypes(String addThemeTypes) {
		this.addThemeTypes = addThemeTypes;
	}
	public List<TestpaperThemetypeForm> getTestpaperThemetypeFormList() {
		return testpaperThemetypeFormList;
	}
	public void setTestpaperThemetypeFormList(
			List<TestpaperThemetypeForm> testpaperThemetypeFormList) {
		this.testpaperThemetypeFormList = testpaperThemetypeFormList;
	}
	public List<TestpaperThemeForm> getTestpaperThemeFormList() {
		return testpaperThemeFormList;
	}
	public void setTestpaperThemeFormList(
			List<TestpaperThemeForm> testpaperThemeFormList) {
		this.testpaperThemeFormList = testpaperThemeFormList;
	}
	public String getTestpaperId() {
		return testpaperId;
	}
	public void setTestpaperId(String testpaperId) {
		this.testpaperId = testpaperId;
	}
	public String getTestpaperName() {
		return testpaperName;
	}
	public void setTestpaperName(String testpaperName) {
		this.testpaperName = testpaperName;
	}
	public String getTestpaperType() {
		return testpaperType;
	}
	public void setTestpaperType(String testpaperType) {
		this.testpaperType = testpaperType;
	}
	public String getExamTypeId() {
		return examTypeId;
	}
	public void setExamTypeId(String examTypeId) {
		this.examTypeId = examTypeId;
	}
	public String getExamTypeName() {
		return examTypeName;
	}
	public void setExamTypeName(String examTypeName) {
		this.examTypeName = examTypeName;
	}
	public Integer getExamProperty() {
		return examProperty;
	}
	public void setExamProperty(Integer examProperty) {
		this.examProperty = examProperty;
	}
	public Short getTotalTheme() {
		return totalTheme;
	}
	public void setTotalTheme(Short totalTheme) {
		this.totalTheme = totalTheme;
	}
	public Double getTotalScore() {
		return totalScore;
	}
	public void setTotalScore(Double totalScore) {
		this.totalScore = totalScore;
	}
	public Double getTestpaperRank() {
		return testpaperRank;
	}
	public void setTestpaperRank(Double testpaperRank) {
		this.testpaperRank = testpaperRank;
	}
	public Integer getScreeningMethods() {
		return screeningMethods;
	}
	public void setScreeningMethods(Integer screeningMethods) {
		this.screeningMethods = screeningMethods;
	}
	public Short getTestpaperTime() {
		return testpaperTime;
	}
	public void setTestpaperTime(Short testpaperTime) {
		this.testpaperTime = testpaperTime;
	}
	public Integer getIsUse() {
		return isUse;
	}
	public void setIsUse(Integer isUse) {
		this.isUse = isUse;
	}
	public String getIsPrivate() {
		return isPrivate;
	}
	public void setIsPrivate(String isPrivate) {
		this.isPrivate = isPrivate;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getCheckUser() {
		return checkUser;
	}
	public void setCheckUser(String checkUser) {
		this.checkUser = checkUser;
	}
	public String getCheckUserId() {
		return checkUserId;
	}
	public void setCheckUserId(String checkUserId) {
		this.checkUserId = checkUserId;
	}
	public String getCheckTime() {
		return checkTime;
	}
	public void setCheckTime(String checkTime) {
		this.checkTime = checkTime;
	}
	public String getCheckIdear() {
		return checkIdear;
	}
	public void setCheckIdear(String checkIdear) {
		this.checkIdear = checkIdear;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public Integer getUseNum() {
		return useNum;
	}
	public void setUseNum(Integer useNum) {
		this.useNum = useNum;
	}
	public String getOrgan() {
		return organ;
	}
	public void setOrgan(String organ) {
		this.organ = organ;
	}
	public String getOrganId() {
		return organId;
	}
	public void setOrganId(String organId) {
		this.organId = organId;
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
	public String getThemeDefScore() {
		return themeDefScore;
	}
	public void setThemeDefScore(String themeDefScore) {
		this.themeDefScore = themeDefScore;
	}
	
	
	
	
}
