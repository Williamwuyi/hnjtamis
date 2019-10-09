package cn.com.ite.hnjtamis.exam.testpaper.form;

import java.util.ArrayList;
import java.util.List;

import cn.com.ite.hnjtamis.exam.hibernatemap.Testpaper;
import cn.com.ite.hnjtamis.exam.hibernatemap.Theme;
import cn.com.ite.hnjtamis.exam.hibernatemap.ThemeType;

/**
 *
 * <p>Title cn.com.ite.hnjtamis.exam.testpaper.form.TestpaperThemeForm</p>
 * <p>Description 考试模版配置 - 试题</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2015</p>
 * @author 朱健
 * @create time: 2015年4月1日 上午9:28:29
 * @version 1.0
 * 
 * @modified records:
 */
public class TestpaperThemeForm {

	private int sortIndex;//显示序号
	private String testpaperThemeId;//试卷模版-试题ID
	private ThemeType themeType;//试题类型
	private String themeBankName;//题库名
	private Theme theme;//试题
	private String themeId;
	private Testpaper testpaper;//试卷
	private String themeTypeName;//试题类型名称
	private String themeTypeId;//试题类型Id
	private String themeTypeNameSort;
	private String themeName;//试题
	private Integer degree;//难度 5：容易,10：一般15：难,20：很难
	private String knowledgePoint;//所属知识点
	private Integer eachline;//答案显示方式 0:不换行　1:一行一个 2:一行二个 3：一行三个 4:一行四个
	private String writeUser;//出题人
	private String explain;//注解
	private Double defaultScore;//分数
	private Integer themeSetNum;//出题次数
	private Integer themePeopleNum;//考试人次
	private Integer themeRightNum;///答题正确数
	private String sortNum;///试题排序（专业4位+题型4位+题目8位）
	private String scoreType;//得分类型 0自动阅卷；1人工阅卷；2系统外阅卷
	private String remark;//备注
	private Integer state;//状态 5:保存10:上报15:发布20:打回
	private String isUse;//有否有效 5:有效 10：无效
	private String syncFlag;////同步标志
	private String organId;//机构ID
	private String organName;//机构名
	private String lastUpdateDate;///最后修改时间
	private String lastUpdatedBy;//最后修改人
	private String lastUpdatedIdBy;//最后修改人ID
	private String creationDate;//创建时间
	private String createdBy;//创建人
	private String createdIdBy;//创建人ID
	private String rightAnswerkey;//正确答案
	private String rightAnswerkeyId;//正确答案ID(单选、多选用),多个逗号隔开
	private List<TestpaperThemeAnswerkeyForm> testpaperThemeAnswerkeyFormList = new ArrayList<TestpaperThemeAnswerkeyForm>();//考试答案
	
	private String answerkeyStr;
	private String answerkeyLiStr;
	
	
	public int getSortIndex() {
		return sortIndex;
	}
	public void setSortIndex(int sortIndex) {
		this.sortIndex = sortIndex;
	}
	public String getAnswerkeyStr() {
		return answerkeyStr;
	}
	public void setAnswerkeyStr(String answerkeyStr) {
		this.answerkeyStr = answerkeyStr;
	}
	public String getThemeId() {
		return themeId;
	}
	public void setThemeId(String themeId) {
		this.themeId = themeId;
	}
	public String getTestpaperThemeId() {
		return testpaperThemeId;
	}
	public void setTestpaperThemeId(String testpaperThemeId) {
		this.testpaperThemeId = testpaperThemeId;
	}
	public ThemeType getThemeType() {
		return themeType;
	}
	public void setThemeType(ThemeType themeType) {
		this.themeType = themeType;
	}
	public Theme getTheme() {
		return theme;
	}
	public void setTheme(Theme theme) {
		this.theme = theme;
	}
	public Testpaper getTestpaper() {
		return testpaper;
	}
	public void setTestpaper(Testpaper testpaper) {
		this.testpaper = testpaper;
	}
	public String getThemeTypeName() {
		return themeTypeName;
	}
	public void setThemeTypeName(String themeTypeName) {
		this.themeTypeName = themeTypeName;
	}
	public String getThemeName() {
		return themeName;
	}
	public void setThemeName(String themeName) {
		this.themeName = themeName;
	}
	public Integer getDegree() {
		return degree;
	}
	public void setDegree(Integer degree) {
		this.degree = degree;
	}
	public String getKnowledgePoint() {
		return knowledgePoint;
	}
	public void setKnowledgePoint(String knowledgePoint) {
		this.knowledgePoint = knowledgePoint;
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
	public String getSortNum() {
		return sortNum;
	}
	public void setSortNum(String sortNum) {
		this.sortNum = sortNum;
	}
	public String getScoreType() {
		return scoreType;
	}
	public void setScoreType(String scoreType) {
		this.scoreType = scoreType;
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
	public String getIsUse() {
		return isUse;
	}
	public void setIsUse(String isUse) {
		this.isUse = isUse;
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
	public String getRightAnswerkey() {
		return rightAnswerkey;
	}
	public void setRightAnswerkey(String rightAnswerkey) {
		this.rightAnswerkey = rightAnswerkey;
	}
	public String getRightAnswerkeyId() {
		return rightAnswerkeyId;
	}
	public void setRightAnswerkeyId(String rightAnswerkeyId) {
		this.rightAnswerkeyId = rightAnswerkeyId;
	}
	public List<TestpaperThemeAnswerkeyForm> getTestpaperThemeAnswerkeyFormList() {
		return testpaperThemeAnswerkeyFormList;
	}
	public void setTestpaperThemeAnswerkeyFormList(
			List<TestpaperThemeAnswerkeyForm> testpaperThemeAnswerkeyFormList) {
		this.testpaperThemeAnswerkeyFormList = testpaperThemeAnswerkeyFormList;
	}
	public String getAnswerkeyLiStr() {
		return answerkeyLiStr;
	}
	public void setAnswerkeyLiStr(String answerkeyLiStr) {
		this.answerkeyLiStr = answerkeyLiStr;
	}
	public String getThemeTypeNameSort() {
		return themeTypeNameSort;
	}
	public void setThemeTypeNameSort(String themeTypeNameSort) {
		this.themeTypeNameSort = themeTypeNameSort;
	}
	public String getThemeTypeId() {
		return themeTypeId;
	}
	public void setThemeTypeId(String themeTypeId) {
		this.themeTypeId = themeTypeId;
	}
	public String getThemeBankName() {
		return themeBankName;
	}
	public void setThemeBankName(String themeBankName) {
		this.themeBankName = themeBankName;
	}
	
	
	
	
}
