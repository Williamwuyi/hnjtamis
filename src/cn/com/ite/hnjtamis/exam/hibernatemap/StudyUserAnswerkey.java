package cn.com.ite.hnjtamis.exam.hibernatemap;

/**
 * StudyUserAnswerkey entity. @author MyEclipse Persistence Tools
 * 学习考生答题结果表
 */

public class StudyUserAnswerkey implements java.io.Serializable {

	// Fields

	private String userAnswerkeyId;//考生答题结果ID
	private String studyTestpaperThemeId;//学习试卷试题ID
	private String studyTestpaperId;//学习试卷ID
	private String themeId;//试题ID(试题库)
	private String examAnswerkey;//考试答案
	private String answerkeyValue;//答案
	private Integer sortNum;//默认为有序（试卷）,如果有其他排序则无效。
	private Double score;//考生得分
	private String answerkeyId;//正确答案ID
	private String organName;//机构名
	private String organId;//机构编号
	private String syncFlag;//同步标志
	private String state;//是否提交 10-未提交 20-提交
	private String inTime;//入场时间
	private String outTime;//离场时间
	private String subTime;//交卷时间
	private String lastUpdateDate;//最后修改时间
	private String lastUpdatedBy;//最后修改人
	private String lastUpdatedIdBy;//最后修改人ID
	private String creationDate;//创建时间
	private String createdBy;//创建人
	private String createdIdBy;//创建人（员工）ID
	private String batchNo;//批次号(年月日时分秒)
	private String relationId;//关联ID（如：练习安排等）
	private String relationType;//关联类型
	private String themeTypeId;
	private String themeTypeName;

	// Constructors

	/** default constructor */
	public StudyUserAnswerkey() {
	}

	/** full constructor */
	public StudyUserAnswerkey(String studyTestpaperThemeId,
			String studyTestpaperId, String themeId, String examAnswerkey,
			String answerkeyValue, Integer sortNum, Double score,
			String answerkeyId, String organName, String organId,
			String syncFlag, String state, String inTime, String outTime,
			String subTime, String lastUpdateDate, String lastUpdatedBy,
			String lastUpdatedIdBy, String creationDate, String createdBy,
			String createdIdBy, String batchNo, String relationId,
			String relationType,String themeTypeId, String themeTypeName) {
		this.studyTestpaperThemeId = studyTestpaperThemeId;
		this.studyTestpaperId = studyTestpaperId;
		this.themeId = themeId;
		this.examAnswerkey = examAnswerkey;
		this.answerkeyValue = answerkeyValue;
		this.sortNum = sortNum;
		this.score = score;
		this.answerkeyId = answerkeyId;
		this.organName = organName;
		this.organId = organId;
		this.syncFlag = syncFlag;
		this.state = state;
		this.inTime = inTime;
		this.outTime = outTime;
		this.subTime = subTime;
		this.lastUpdateDate = lastUpdateDate;
		this.lastUpdatedBy = lastUpdatedBy;
		this.lastUpdatedIdBy = lastUpdatedIdBy;
		this.creationDate = creationDate;
		this.createdBy = createdBy;
		this.createdIdBy = createdIdBy;
		this.batchNo = batchNo;
		this.relationId = relationId;
		this.relationType = relationType;
		this.themeTypeId = themeTypeId;
		this.themeTypeName = themeTypeName;
	}

	// Property accessors

	public String getUserAnswerkeyId() {
		return this.userAnswerkeyId;
	}

	public void setUserAnswerkeyId(String userAnswerkeyId) {
		this.userAnswerkeyId = userAnswerkeyId;
	}

	public String getStudyTestpaperThemeId() {
		return this.studyTestpaperThemeId;
	}

	public void setStudyTestpaperThemeId(String studyTestpaperThemeId) {
		this.studyTestpaperThemeId = studyTestpaperThemeId;
	}

	public String getStudyTestpaperId() {
		return this.studyTestpaperId;
	}

	public void setStudyTestpaperId(String studyTestpaperId) {
		this.studyTestpaperId = studyTestpaperId;
	}

	public String getThemeId() {
		return this.themeId;
	}

	public void setThemeId(String themeId) {
		this.themeId = themeId;
	}

	public String getExamAnswerkey() {
		return this.examAnswerkey;
	}

	public void setExamAnswerkey(String examAnswerkey) {
		this.examAnswerkey = examAnswerkey;
	}

	public String getAnswerkeyValue() {
		return this.answerkeyValue;
	}

	public void setAnswerkeyValue(String answerkeyValue) {
		this.answerkeyValue = answerkeyValue;
	}

	public Integer getSortNum() {
		return this.sortNum;
	}

	public void setSortNum(Integer sortNum) {
		this.sortNum = sortNum;
	}

	public Double getScore() {
		return this.score;
	}

	public void setScore(Double score) {
		this.score = score;
	}

	public String getAnswerkeyId() {
		return this.answerkeyId;
	}

	public void setAnswerkeyId(String answerkeyId) {
		this.answerkeyId = answerkeyId;
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

	public String getSyncFlag() {
		return this.syncFlag;
	}

	public void setSyncFlag(String syncFlag) {
		this.syncFlag = syncFlag;
	}

	public String getState() {
		return this.state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getInTime() {
		return this.inTime;
	}

	public void setInTime(String inTime) {
		this.inTime = inTime;
	}

	public String getOutTime() {
		return this.outTime;
	}

	public void setOutTime(String outTime) {
		this.outTime = outTime;
	}

	public String getSubTime() {
		return this.subTime;
	}

	public void setSubTime(String subTime) {
		this.subTime = subTime;
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

	public String getBatchNo() {
		return this.batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public String getRelationId() {
		return this.relationId;
	}

	public void setRelationId(String relationId) {
		this.relationId = relationId;
	}

	public String getRelationType() {
		return this.relationType;
	}

	public void setRelationType(String relationType) {
		this.relationType = relationType;
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

}