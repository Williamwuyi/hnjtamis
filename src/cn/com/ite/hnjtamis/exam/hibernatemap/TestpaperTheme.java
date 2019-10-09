package cn.com.ite.hnjtamis.exam.hibernatemap;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * TestpaperTheme entity. @author MyEclipse Persistence Tools
 */

public class TestpaperTheme implements java.io.Serializable {

	// Fields

	private String testpaperThemeId;//试卷模版-试题ID
	private ThemeType themeType;//试题类型
	private Theme theme;//试题
	private Testpaper testpaper;//试卷
	private String themeTypeName;//试题类型名称
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
	private Integer sortNum;///试题排序
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
	private List<TestpaperThemeAnswerkey> testpaperThemeAnswerkeies = new ArrayList<TestpaperThemeAnswerkey>(0);//答案
	private List<TestpaperThemeSkey> testpaperThemeSkeies = new ArrayList<TestpaperThemeSkey>(0);//搜索关键字

	// Constructors

	/** default constructor */
	public TestpaperTheme() {
	}

	/** minimal constructor */
	public TestpaperTheme(Theme theme) {
		this.theme = theme;
	}

	/** full constructor */
	public TestpaperTheme(ThemeType themeType, Theme theme,
			Testpaper testpaper, String themeTypeName, String themeName,
			Integer degree, String knowledgePoint, Integer eachline,
			String writeUser, String explain, Double defaultScore,
			Integer themeSetNum, Integer themePeopleNum, Integer themeRightNum,
			Integer sortNum, String scoreType, String remark, Integer state,
			String isUse, String syncFlag, String organId, String organName,
			String lastUpdateDate, String lastUpdatedBy,
			String lastUpdatedIdBy, String creationDate, String createdBy,
			String createdIdBy, String rightAnswerkey, String rightAnswerkeyId,
			List<TestpaperThemeAnswerkey> testpaperThemeAnswerkeies, List<TestpaperThemeSkey> testpaperThemeSkeies) {
		this.themeType = themeType;
		this.theme = theme;
		this.testpaper = testpaper;
		this.themeTypeName = themeTypeName;
		this.themeName = themeName;
		this.degree = degree;
		this.knowledgePoint = knowledgePoint;
		this.eachline = eachline;
		this.writeUser = writeUser;
		this.explain = explain;
		this.defaultScore = defaultScore;
		this.themeSetNum = themeSetNum;
		this.themePeopleNum = themePeopleNum;
		this.themeRightNum = themeRightNum;
		this.sortNum = sortNum;
		this.scoreType = scoreType;
		this.remark = remark;
		this.state = state;
		this.isUse = isUse;
		this.syncFlag = syncFlag;
		this.organId = organId;
		this.organName = organName;
		this.lastUpdateDate = lastUpdateDate;
		this.lastUpdatedBy = lastUpdatedBy;
		this.lastUpdatedIdBy = lastUpdatedIdBy;
		this.creationDate = creationDate;
		this.createdBy = createdBy;
		this.createdIdBy = createdIdBy;
		this.rightAnswerkey = rightAnswerkey;
		this.rightAnswerkeyId = rightAnswerkeyId;
		this.testpaperThemeAnswerkeies = testpaperThemeAnswerkeies;
		this.testpaperThemeSkeies = testpaperThemeSkeies;
	}

	// Property accessors

	public String getTestpaperThemeId() {
		return this.testpaperThemeId;
	}

	public void setTestpaperThemeId(String testpaperThemeId) {
		this.testpaperThemeId = testpaperThemeId;
	}

	public ThemeType getThemeType() {
		return this.themeType;
	}

	public void setThemeType(ThemeType themeType) {
		this.themeType = themeType;
	}

	public Theme getTheme() {
		return this.theme;
	}

	public void setTheme(Theme theme) {
		this.theme = theme;
	}

	public Testpaper getTestpaper() {
		return this.testpaper;
	}

	public void setTestpaper(Testpaper testpaper) {
		this.testpaper = testpaper;
	}

	public String getThemeTypeName() {
		return this.themeTypeName;
	}

	public void setThemeTypeName(String themeTypeName) {
		this.themeTypeName = themeTypeName;
	}

	public String getThemeName() {
		return this.themeName;
	}

	public void setThemeName(String themeName) {
		this.themeName = themeName;
	}

	public Integer getDegree() {
		return this.degree;
	}

	public void setDegree(Integer degree) {
		this.degree = degree;
	}

	public String getKnowledgePoint() {
		return this.knowledgePoint;
	}

	public void setKnowledgePoint(String knowledgePoint) {
		this.knowledgePoint = knowledgePoint;
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

	public Integer getSortNum() {
		return this.sortNum;
	}

	public void setSortNum(Integer sortNum) {
		this.sortNum = sortNum;
	}

	public String getScoreType() {
		return this.scoreType;
	}

	public void setScoreType(String scoreType) {
		this.scoreType = scoreType;
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

	public String getIsUse() {
		return this.isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
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

	public String getRightAnswerkey() {
		return this.rightAnswerkey;
	}

	public void setRightAnswerkey(String rightAnswerkey) {
		this.rightAnswerkey = rightAnswerkey;
	}

	public String getRightAnswerkeyId() {
		return this.rightAnswerkeyId;
	}

	public void setRightAnswerkeyId(String rightAnswerkeyId) {
		this.rightAnswerkeyId = rightAnswerkeyId;
	}

	public List<TestpaperThemeAnswerkey> getTestpaperThemeAnswerkeies() {
		return this.testpaperThemeAnswerkeies;
	}

	public void setTestpaperThemeAnswerkeies(List<TestpaperThemeAnswerkey> testpaperThemeAnswerkeies) {
		this.testpaperThemeAnswerkeies = testpaperThemeAnswerkeies;
	}

	public List<TestpaperThemeSkey> getTestpaperThemeSkeies() {
		return this.testpaperThemeSkeies;
	}

	public void setTestpaperThemeSkeies(List<TestpaperThemeSkey> testpaperThemeSkeies) {
		this.testpaperThemeSkeies = testpaperThemeSkeies;
	}

}