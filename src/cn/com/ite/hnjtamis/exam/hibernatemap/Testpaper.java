package cn.com.ite.hnjtamis.exam.hibernatemap;

import java.util.ArrayList;
import java.util.List;

/**
 * Testpaper entity. @author MyEclipse Persistence Tools
 */

public class Testpaper implements java.io.Serializable {

	// Fields

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
	private String organId;//机构编号
	private String syncFlag;////同步标志
	private String lastUpdateDate;///最后修改时间
	private String lastUpdatedBy;//最后修改人
	private String lastUpdatedIdBy;//最后修改人ID
	private String creationDate;//创建时间
	private String createdBy;//创建人
	private String createdIdBy;//创建人ID
	private String relationId;//关联ID（如：练习安排等）
	private String relationType;//关联类型
	private List<TestpaperTheme> testpaperThemes = new ArrayList<TestpaperTheme>(0);//试卷模版-试题
	private List<TestpaperShare> testpaperShares = new ArrayList<TestpaperShare>(0);//试题共享
	private List<TestpaperThemetype> testpaperThemetypes = new ArrayList<TestpaperThemetype>(0);//试卷模版-题型配置比例
	private List<Exam> exams = new ArrayList<Exam>(0);//考试安排
	private List<TestpaperSkey> testpaperSkeies = new ArrayList<TestpaperSkey>(0);//试卷模版检索关键字

	// Constructors

	/** default constructor */
	public Testpaper() {
	}

	/** full constructor */
	public Testpaper(String testpaperName, String testpaperType,
			String examTypeId, String examTypeName, Integer examProperty,
			Short totalTheme, Double totalScore, Double testpaperRank,
			Integer screeningMethods, Short testpaperTime, Integer isUse,
			String isPrivate, String remark, String checkUser,
			String checkUserId, String checkTime, String checkIdear,
			String state, Integer useNum, String organ, String organId,String relationId, String relationType,
			String syncFlag, String lastUpdateDate, String lastUpdatedBy,
			String lastUpdatedIdBy, String creationDate, String createdBy,
			String createdIdBy, List<TestpaperTheme> testpaperThemes, List<TestpaperShare> testpaperShares,
			List<TestpaperThemetype> testpaperThemetypes, List<Exam> exams, List<TestpaperSkey> testpaperSkeies) {
		this.testpaperName = testpaperName;
		this.testpaperType = testpaperType;
		this.examTypeId = examTypeId;
		this.examTypeName = examTypeName;
		this.examProperty = examProperty;
		this.totalTheme = totalTheme;
		this.totalScore = totalScore;
		this.testpaperRank = testpaperRank;
		this.screeningMethods = screeningMethods;
		this.testpaperTime = testpaperTime;
		this.isUse = isUse;
		this.isPrivate = isPrivate;
		this.remark = remark;
		this.checkUser = checkUser;
		this.checkUserId = checkUserId;
		this.checkTime = checkTime;
		this.checkIdear = checkIdear;
		this.state = state;
		this.useNum = useNum;
		this.organ = organ;
		this.organId = organId;
		this.syncFlag = syncFlag;
		this.lastUpdateDate = lastUpdateDate;
		this.lastUpdatedBy = lastUpdatedBy;
		this.lastUpdatedIdBy = lastUpdatedIdBy;
		this.creationDate = creationDate;
		this.createdBy = createdBy;
		this.createdIdBy = createdIdBy;
		this.testpaperThemes = testpaperThemes;
		this.testpaperShares = testpaperShares;
		this.testpaperThemetypes = testpaperThemetypes;
		this.exams = exams;
		this.testpaperSkeies = testpaperSkeies;
		this.relationId = relationId;
		this.relationType = relationType;
	}

	// Property accessors

	public String getTestpaperId() {
		return this.testpaperId;
	}

	public String getRelationId() {
		return relationId;
	}

	public void setRelationId(String relationId) {
		this.relationId = relationId;
	}

	public String getRelationType() {
		return relationType;
	}

	public void setRelationType(String relationType) {
		this.relationType = relationType;
	}

	public void setTestpaperId(String testpaperId) {
		this.testpaperId = testpaperId;
	}

	public String getTestpaperName() {
		return this.testpaperName;
	}

	public void setTestpaperName(String testpaperName) {
		this.testpaperName = testpaperName;
	}

	public String getTestpaperType() {
		return this.testpaperType;
	}

	public void setTestpaperType(String testpaperType) {
		this.testpaperType = testpaperType;
	}

	public String getExamTypeId() {
		return this.examTypeId;
	}

	public void setExamTypeId(String examTypeId) {
		this.examTypeId = examTypeId;
	}

	public String getExamTypeName() {
		return this.examTypeName;
	}

	public void setExamTypeName(String examTypeName) {
		this.examTypeName = examTypeName;
	}

	public Integer getExamProperty() {
		return this.examProperty;
	}

	public void setExamProperty(Integer examProperty) {
		this.examProperty = examProperty;
	}

	public Short getTotalTheme() {
		return this.totalTheme;
	}

	public void setTotalTheme(Short totalTheme) {
		this.totalTheme = totalTheme;
	}

	public Double getTotalScore() {
		return this.totalScore;
	}

	public void setTotalScore(Double totalScore) {
		this.totalScore = totalScore;
	}

	public Double getTestpaperRank() {
		return this.testpaperRank;
	}

	public void setTestpaperRank(Double testpaperRank) {
		this.testpaperRank = testpaperRank;
	}

	public Integer getScreeningMethods() {
		return this.screeningMethods;
	}

	public void setScreeningMethods(Integer screeningMethods) {
		this.screeningMethods = screeningMethods;
	}

	public Short getTestpaperTime() {
		return this.testpaperTime;
	}

	public void setTestpaperTime(Short testpaperTime) {
		this.testpaperTime = testpaperTime;
	}

	public Integer getIsUse() {
		return this.isUse;
	}

	public void setIsUse(Integer isUse) {
		this.isUse = isUse;
	}

	public String getIsPrivate() {
		return this.isPrivate;
	}

	public void setIsPrivate(String isPrivate) {
		this.isPrivate = isPrivate;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getCheckUser() {
		return this.checkUser;
	}

	public void setCheckUser(String checkUser) {
		this.checkUser = checkUser;
	}

	public String getCheckUserId() {
		return this.checkUserId;
	}

	public void setCheckUserId(String checkUserId) {
		this.checkUserId = checkUserId;
	}

	public String getCheckTime() {
		return this.checkTime;
	}

	public void setCheckTime(String checkTime) {
		this.checkTime = checkTime;
	}

	public String getCheckIdear() {
		return this.checkIdear;
	}

	public void setCheckIdear(String checkIdear) {
		this.checkIdear = checkIdear;
	}

	public String getState() {
		return this.state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Integer getUseNum() {
		return this.useNum;
	}

	public void setUseNum(Integer useNum) {
		this.useNum = useNum;
	}

	public String getOrgan() {
		return this.organ;
	}

	public void setOrgan(String organ) {
		this.organ = organ;
	}

	public String getOrganId() {
		return this.organId;
	}

	public void setOrganId(String organId) {
		this.organId = organId;
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

	public List<TestpaperTheme> getTestpaperThemes() {
		return this.testpaperThemes;
	}

	public void setTestpaperThemes(List<TestpaperTheme> testpaperThemes) {
		this.testpaperThemes = testpaperThemes;
	}

	public List<TestpaperShare> getTestpaperShares() {
		return this.testpaperShares;
	}

	public void setTestpaperShares(List<TestpaperShare> testpaperShares) {
		this.testpaperShares = testpaperShares;
	}

	public List<TestpaperThemetype> getTestpaperThemetypes() {
		return this.testpaperThemetypes;
	}

	public void setTestpaperThemetypes(List<TestpaperThemetype> testpaperThemetypes) {
		this.testpaperThemetypes = testpaperThemetypes;
	}

	public List<Exam> getExams() {
		return this.exams;
	}

	public void setExams(List<Exam> exams) {
		this.exams = exams;
	}

	public List<TestpaperSkey> getTestpaperSkeies() {
		return this.testpaperSkeies;
	}

	public void setTestpaperSkeies(List<TestpaperSkey> testpaperSkeies) {
		this.testpaperSkeies = testpaperSkeies;
	}

}