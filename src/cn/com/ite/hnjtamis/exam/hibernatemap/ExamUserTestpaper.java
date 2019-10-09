package cn.com.ite.hnjtamis.exam.hibernatemap;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * ExamUserTestpaper entity. @author MyEclipse Persistence Tools
 */

public class ExamUserTestpaper implements java.io.Serializable {

	// Fields

	private String userTestpaperId;
	private ExamExamroot examExamroot;
	private ExamPublicUser examPublicUser;
	private ExamTestpaper examTestpaper;
	private Exam exam;
	private String userOrganId;//所属机构ID
	private String userOrganName;//所属机构
	private String userDeptId;//所属部门ID
	private String userDeptName;//所属部门
	private String userGroupId;//所属班组ID
	private String userGroupName;//所属班组
	private String postId;//所属岗位ID
	private String postName;//所属岗位
	private String userName;//姓名
	private String userSex;//性别  1-男  2-女
	private String inticket;//准考证号
	private String idNumber;//身份证号
	private String userBirthday;//出生年月
	private String userNation;//民族
	private String userAddr;//住址
	private String userPhone;//联系电话
	private String userInfo;//考生信息
	private String examPassword;//考试密码
	private Integer examPaperType;//考试方式 10-正式（同试卷打乱） 11-正式（各考生随机抽题） 20-模拟 30-练习
	private String inTime;//入场时间
	private String outTime;//离场时间
	private Integer isLocked;//是否锁定 0-未锁定  5-锁定
	private Double fristScote;//调整前得分
	private Double scote;//调整得分
	private String adjustRemark;//调整说明
	private String adjustId;//调整人ID
	private String adjustUser;///调整人
	private String subTime;///交卷时间
	private String loginUrl;///登陆地址
	private String examRootName;//考点名称
	private String examRootPlace;//考点地点
	private String seatNum;//座位号
	private String isIdNumberLogin;//是否允许身份证登陆 0-允许  1-不允许
	private String examPaperInit;//试卷是否初始化 10-否 11-初始化失败 20-是  21-需要进行重置
	private String state;//状态 5:生成10:待考15:已考完 16-作弊 20:已阅卷 25 打回 30-发布
	private String relationId;//关联ID（如：练习安排等）
	private String relationType;//关联类型
	private String organName;//机构名
	private String organId;//机构编号
	private String syncFlag;////同步标志
	private String lastUpdateDate;///最后修改时间
	private String lastUpdatedBy;//最后修改人
	private String lastUpdatedIdBy;//最后修改人ID
	private String creationDate;//创建时间
	private String createdBy;//创建人
	private String createdIdBy;//创建人ID
	private String employeeId;//人员ID
	private String employeeName;//人员
	
	private String passState;//是否合格
	private String scoreStartTime;//成绩有效期（开始时间）
	private String scoreEndTime;//成绩有效期（结束时间）
	private String publicTime;//成绩发布时间
	
	
	private String loginId;///登录用户
	private String loginPassword;//登录考试密码
	private String employeeDeptId;//部门ID
	private String employeeDeptName;//部门名称
	private String employeeQuarterId;//岗位ID
	private String employeeQuarterName;//岗位名称
	
	
	private List<ExamProctorDeduct> examProctorDeducts = new ArrayList<ExamProctorDeduct>(0);

	// Constructors

	/** default constructor */
	public ExamUserTestpaper() {
	}

	/** full constructor */
	public ExamUserTestpaper(ExamExamroot examExamroot,
			ExamPublicUser examPublicUser, ExamTestpaper examTestpaper,
			Exam exam, String userOrganId, String userOrganName,
			String userDeptId, String userDeptName, String userGroupId,
			String userGroupName, String postId, String postName,
			String userName, String userSex, String inticket, String idNumber,
			String userBirthday, String userNation, String userAddr,
			String userPhone, String userInfo, String examPassword,
			Integer examPaperType, String inTime, String outTime, Integer isLocked,
			Double fristScote, Double scote, String adjustRemark,
			String adjustId, String adjustUser, String subTime,
			String loginUrl, String examRootName, String examRootPlace,
			String seatNum, String isIdNumberLogin, String examPaperInit,
			String state, String relationId, String relationType,
			String organName, String organId, String syncFlag,
			String creationDate, String createdBy, String createdIdBy,
			String lastUpdateDate, String lastUpdatedBy,
			String lastUpdatedIdBy, List<ExamProctorDeduct> examProctorDeducts,String employeeId,String employeeName,
			String passState,String scoreStartTime,String scoreEndTime,String publicTime,
			String loginId,String loginPassword,String employeeDeptId,String employeeDeptName,String employeeQuarterId,
			String employeeQuarterName) {
		this.examExamroot = examExamroot;
		this.examPublicUser = examPublicUser;
		this.examTestpaper = examTestpaper;
		this.exam = exam;
		this.userOrganId = userOrganId;
		this.userOrganName = userOrganName;
		this.userDeptId = userDeptId;
		this.userDeptName = userDeptName;
		this.userGroupId = userGroupId;
		this.userGroupName = userGroupName;
		this.postId = postId;
		this.postName = postName;
		this.userName = userName;
		this.userSex = userSex;
		this.inticket = inticket;
		this.idNumber = idNumber;
		this.userBirthday = userBirthday;
		this.userNation = userNation;
		this.userAddr = userAddr;
		this.userPhone = userPhone;
		this.userInfo = userInfo;
		this.examPassword = examPassword;
		this.examPaperType = examPaperType;
		this.inTime = inTime;
		this.outTime = outTime;
		this.isLocked = isLocked;
		this.fristScote = fristScote;
		this.scote = scote;
		this.adjustRemark = adjustRemark;
		this.adjustId = adjustId;
		this.adjustUser = adjustUser;
		this.subTime = subTime;
		this.loginUrl = loginUrl;
		this.examRootName = examRootName;
		this.examRootPlace = examRootPlace;
		this.seatNum = seatNum;
		this.isIdNumberLogin = isIdNumberLogin;
		this.examPaperInit = examPaperInit;
		this.state = state;
		this.relationId = relationId;
		this.relationType = relationType;
		this.organName = organName;
		this.organId = organId;
		this.syncFlag = syncFlag;
		this.creationDate = creationDate;
		this.createdBy = createdBy;
		this.createdIdBy = createdIdBy;
		this.lastUpdateDate = lastUpdateDate;
		this.lastUpdatedBy = lastUpdatedBy;
		this.lastUpdatedIdBy = lastUpdatedIdBy;
		this.examProctorDeducts = examProctorDeducts;
		this.employeeId=employeeId;
		this.employeeName=employeeName;
		this.passState = passState;
		this.scoreStartTime = scoreStartTime;
		this.scoreEndTime =scoreEndTime;
		this.publicTime = publicTime;
		this.loginId = loginId ; 
		this.loginPassword = loginPassword ; 
		this.employeeDeptId = employeeDeptId ; 
		this.employeeDeptName = employeeDeptName ; 
		this.employeeQuarterId = employeeQuarterId ; 
		this.employeeQuarterName = employeeQuarterName ; 
	}

	// Property accessors

	public String getUserTestpaperId() {
		return this.userTestpaperId;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public void setUserTestpaperId(String userTestpaperId) {
		this.userTestpaperId = userTestpaperId;
	}

	public ExamExamroot getExamExamroot() {
		return this.examExamroot;
	}

	public void setExamExamroot(ExamExamroot examExamroot) {
		this.examExamroot = examExamroot;
	}

	public ExamPublicUser getExamPublicUser() {
		return this.examPublicUser;
	}

	public void setExamPublicUser(ExamPublicUser examPublicUser) {
		this.examPublicUser = examPublicUser;
	}

	public ExamTestpaper getExamTestpaper() {
		return this.examTestpaper;
	}

	public void setExamTestpaper(ExamTestpaper examTestpaper) {
		this.examTestpaper = examTestpaper;
	}

	public Exam getExam() {
		return this.exam;
	}

	public void setExam(Exam exam) {
		this.exam = exam;
	}

	public String getUserOrganId() {
		return this.userOrganId;
	}

	public void setUserOrganId(String userOrganId) {
		this.userOrganId = userOrganId;
	}

	public String getUserOrganName() {
		return this.userOrganName;
	}

	public void setUserOrganName(String userOrganName) {
		this.userOrganName = userOrganName;
	}

	public String getUserDeptId() {
		return this.userDeptId;
	}

	public void setUserDeptId(String userDeptId) {
		this.userDeptId = userDeptId;
	}

	public String getUserDeptName() {
		return this.userDeptName;
	}

	public void setUserDeptName(String userDeptName) {
		this.userDeptName = userDeptName;
	}

	public String getUserGroupId() {
		return this.userGroupId;
	}

	public void setUserGroupId(String userGroupId) {
		this.userGroupId = userGroupId;
	}

	public String getUserGroupName() {
		return this.userGroupName;
	}

	public void setUserGroupName(String userGroupName) {
		this.userGroupName = userGroupName;
	}

	public String getPostId() {
		return this.postId;
	}

	public void setPostId(String postId) {
		this.postId = postId;
	}

	public String getPostName() {
		return this.postName;
	}

	public void setPostName(String postName) {
		this.postName = postName;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserSex() {
		return this.userSex;
	}

	public void setUserSex(String userSex) {
		this.userSex = userSex;
	}

	public String getInticket() {
		return this.inticket;
	}

	public void setInticket(String inticket) {
		this.inticket = inticket;
	}

	public String getIdNumber() {
		return this.idNumber;
	}

	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}

	public String getUserBirthday() {
		return this.userBirthday;
	}

	public void setUserBirthday(String userBirthday) {
		this.userBirthday = userBirthday;
	}

	public String getUserNation() {
		return this.userNation;
	}

	public void setUserNation(String userNation) {
		this.userNation = userNation;
	}

	public String getUserAddr() {
		return this.userAddr;
	}

	public void setUserAddr(String userAddr) {
		this.userAddr = userAddr;
	}

	public String getUserPhone() {
		return this.userPhone;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}

	public String getUserInfo() {
		return this.userInfo;
	}

	public void setUserInfo(String userInfo) {
		this.userInfo = userInfo;
	}

	public String getExamPassword() {
		return this.examPassword;
	}

	public void setExamPassword(String examPassword) {
		this.examPassword = examPassword;
	}

	public Integer getExamPaperType() {
		return this.examPaperType;
	}

	public void setExamPaperType(Integer examPaperType) {
		this.examPaperType = examPaperType;
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

	public Integer getIsLocked() {
		return this.isLocked;
	}

	public void setIsLocked(Integer isLocked) {
		this.isLocked = isLocked;
	}

	public Double getFristScote() {
		return this.fristScote;
	}

	public void setFristScote(Double fristScote) {
		this.fristScote = fristScote;
	}

	public Double getScote() {
		return this.scote;
	}

	public void setScote(Double scote) {
		this.scote = scote;
	}

	public String getAdjustRemark() {
		return this.adjustRemark;
	}

	public void setAdjustRemark(String adjustRemark) {
		this.adjustRemark = adjustRemark;
	}

	public String getAdjustId() {
		return this.adjustId;
	}

	public void setAdjustId(String adjustId) {
		this.adjustId = adjustId;
	}

	public String getAdjustUser() {
		return this.adjustUser;
	}

	public void setAdjustUser(String adjustUser) {
		this.adjustUser = adjustUser;
	}

	public String getSubTime() {
		return this.subTime;
	}

	public void setSubTime(String subTime) {
		this.subTime = subTime;
	}

	public String getLoginUrl() {
		return this.loginUrl;
	}

	public void setLoginUrl(String loginUrl) {
		this.loginUrl = loginUrl;
	}

	public String getExamRootName() {
		return this.examRootName;
	}

	public void setExamRootName(String examRootName) {
		this.examRootName = examRootName;
	}

	public String getExamRootPlace() {
		return this.examRootPlace;
	}

	public void setExamRootPlace(String examRootPlace) {
		this.examRootPlace = examRootPlace;
	}

	public String getSeatNum() {
		return this.seatNum;
	}

	public void setSeatNum(String seatNum) {
		this.seatNum = seatNum;
	}

	public String getIsIdNumberLogin() {
		return this.isIdNumberLogin;
	}

	public void setIsIdNumberLogin(String isIdNumberLogin) {
		this.isIdNumberLogin = isIdNumberLogin;
	}

	public String getExamPaperInit() {
		return this.examPaperInit;
	}

	public void setExamPaperInit(String examPaperInit) {
		this.examPaperInit = examPaperInit;
	}

	public String getState() {
		return this.state;
	}

	public void setState(String state) {
		this.state = state;
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

	public List<ExamProctorDeduct> getExamProctorDeducts() {
		return this.examProctorDeducts;
	}

	public void setExamProctorDeducts(List<ExamProctorDeduct> examProctorDeducts) {
		this.examProctorDeducts = examProctorDeducts;
	}

	public String getPassState() {
		return passState;
	}

	public void setPassState(String passState) {
		this.passState = passState;
	}

	public String getScoreStartTime() {
		return scoreStartTime;
	}

	public void setScoreStartTime(String scoreStartTime) {
		this.scoreStartTime = scoreStartTime;
	}

	public String getScoreEndTime() {
		return scoreEndTime;
	}

	public void setScoreEndTime(String scoreEndTime) {
		this.scoreEndTime = scoreEndTime;
	}

	public String getPublicTime() {
		return publicTime;
	}

	public void setPublicTime(String publicTime) {
		this.publicTime = publicTime;
	}

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public String getLoginPassword() {
		return loginPassword;
	}

	public void setLoginPassword(String loginPassword) {
		this.loginPassword = loginPassword;
	}

	public String getEmployeeDeptId() {
		return employeeDeptId;
	}

	public void setEmployeeDeptId(String employeeDeptId) {
		this.employeeDeptId = employeeDeptId;
	}

	public String getEmployeeDeptName() {
		return employeeDeptName;
	}

	public void setEmployeeDeptName(String employeeDeptName) {
		this.employeeDeptName = employeeDeptName;
	}

	public String getEmployeeQuarterId() {
		return employeeQuarterId;
	}

	public void setEmployeeQuarterId(String employeeQuarterId) {
		this.employeeQuarterId = employeeQuarterId;
	}

	public String getEmployeeQuarterName() {
		return employeeQuarterName;
	}

	public void setEmployeeQuarterName(String employeeQuarterName) {
		this.employeeQuarterName = employeeQuarterName;
	}
	
	

}