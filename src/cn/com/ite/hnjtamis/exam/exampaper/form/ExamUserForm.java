package cn.com.ite.hnjtamis.exam.exampaper.form;

/**
 *
 * <p>Title cn.com.ite.hnjtamis.exam.exampaper.form.ExamUserForm</p>
 * <p>Description 考生信息</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2015</p>
 * @author 朱健
 * @create time: 2015年4月14日 下午1:46:03
 * @version 1.0
 * 
 * @modified records:
 */
public class ExamUserForm {

	private String userTestpaperId;
	private String examName;//科目名称
	private String examId;//科目Id
	private String userOrganId;//所属机构ID
	private String userOrganName;//所属机构
	private String userDeptId;//所属部门ID
	private String userDeptName;//所属部门
	private String userGroupId;//所属班组ID
	private String userGroupName;//所属班组
	private String postId;//所属岗位ID
	private String postName;//所属岗位
	private String userName;//姓名
	private String userId;//考生ID
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
	private Double testpaperScote;//试卷得分
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
	private String state;//状态 5:生成10:待考15:已考完 16-作弊 20:已阅卷 25 打回
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
	private String initType = "保存";//初始化状态
	private String loginId;///登录用户
	
	public String getLoginId() {
		return loginId;
	}
	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}
	private int titleFile = 0;//0-不存在 1-存在
	private String titleFileString;
	private int ansFile = -1;//-1不提供  0-提供未产生 1-提供并产生
	private String ansFileString;
	
	private String yjstate;//阅卷状态
	private String yjstateRemark;//阅卷状态

	
	private int userAnsFile = 0;//0-不存在 1-存在
	
	
	private String iocpUrl;
	
	
	
	public String getExamId() {
		return examId;
	}
	public void setExamId(String examId) {
		this.examId = examId;
	}
	public String getIocpUrl() {
		return iocpUrl;
	}
	public void setIocpUrl(String iocpUrl) {
		this.iocpUrl = iocpUrl;
	}
	public String getYjstateRemark() {
		return yjstateRemark;
	}
	public void setYjstateRemark(String yjstateRemark) {
		this.yjstateRemark = yjstateRemark;
	}
	public String getYjstate() {
		return yjstate;
	}
	public void setYjstate(String yjstate) {
		this.yjstate = yjstate;
	}
	public String getTitleFileString() {
		return titleFileString;
	}
	public void setTitleFileString(String titleFileString) {
		this.titleFileString = titleFileString;
	}
	public String getAnsFileString() {
		return ansFileString;
	}
	public void setAnsFileString(String ansFileString) {
		this.ansFileString = ansFileString;
	}
	public int getAnsFile() {
		return ansFile;
	}
	public void setAnsFile(int ansFile) {
		this.ansFile = ansFile;
	}
	public int getUserAnsFile() {
		return userAnsFile;
	}
	public void setUserAnsFile(int userAnsFile) {
		this.userAnsFile = userAnsFile;
	}
	public int getTitleFile() {
		return titleFile;
	}
	public void setTitleFile(int titleFile) {
		this.titleFile = titleFile;
	}
	public String getExamName() {
		return examName;
	}
	public void setExamName(String examName) {
		this.examName = examName;
	}
	public Double getTestpaperScote() {
		return testpaperScote;
	}
	public void setTestpaperScote(Double testpaperScote) {
		this.testpaperScote = testpaperScote;
	}
	public String getInitType() {
		return initType;
	}
	public void setInitType(String initType) {
		this.initType = initType;
	}
	public String getUserTestpaperId() {
		return userTestpaperId;
	}
	public void setUserTestpaperId(String userTestpaperId) {
		this.userTestpaperId = userTestpaperId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserOrganId() {
		return userOrganId;
	}
	public void setUserOrganId(String userOrganId) {
		this.userOrganId = userOrganId;
	}
	public String getUserOrganName() {
		return userOrganName;
	}
	public void setUserOrganName(String userOrganName) {
		this.userOrganName = userOrganName;
	}
	public String getUserDeptId() {
		return userDeptId;
	}
	public void setUserDeptId(String userDeptId) {
		this.userDeptId = userDeptId;
	}
	public String getUserDeptName() {
		return userDeptName;
	}
	public void setUserDeptName(String userDeptName) {
		this.userDeptName = userDeptName;
	}
	public String getUserGroupId() {
		return userGroupId;
	}
	public void setUserGroupId(String userGroupId) {
		this.userGroupId = userGroupId;
	}
	public String getUserGroupName() {
		return userGroupName;
	}
	public void setUserGroupName(String userGroupName) {
		this.userGroupName = userGroupName;
	}
	public String getPostId() {
		return postId;
	}
	public void setPostId(String postId) {
		this.postId = postId;
	}
	public String getPostName() {
		return postName;
	}
	public void setPostName(String postName) {
		this.postName = postName;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserSex() {
		return userSex;
	}
	public void setUserSex(String userSex) {
		this.userSex = userSex;
	}
	public String getInticket() {
		return inticket;
	}
	public void setInticket(String inticket) {
		this.inticket = inticket;
	}
	public String getIdNumber() {
		return idNumber;
	}
	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}
	public String getUserBirthday() {
		return userBirthday;
	}
	public void setUserBirthday(String userBirthday) {
		this.userBirthday = userBirthday;
	}
	public String getUserNation() {
		return userNation;
	}
	public void setUserNation(String userNation) {
		this.userNation = userNation;
	}
	public String getUserAddr() {
		return userAddr;
	}
	public void setUserAddr(String userAddr) {
		this.userAddr = userAddr;
	}
	public String getUserPhone() {
		return userPhone;
	}
	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}
	public String getUserInfo() {
		return userInfo;
	}
	public void setUserInfo(String userInfo) {
		this.userInfo = userInfo;
	}
	public String getExamPassword() {
		return examPassword;
	}
	public void setExamPassword(String examPassword) {
		this.examPassword = examPassword;
	}
	public Integer getExamPaperType() {
		return examPaperType;
	}
	public void setExamPaperType(Integer examPaperType) {
		this.examPaperType = examPaperType;
	}
	public String getInTime() {
		return inTime;
	}
	public void setInTime(String inTime) {
		this.inTime = inTime;
	}
	public String getOutTime() {
		return outTime;
	}
	public void setOutTime(String outTime) {
		this.outTime = outTime;
	}
	public Integer getIsLocked() {
		return isLocked;
	}
	public void setIsLocked(Integer isLocked) {
		this.isLocked = isLocked;
	}
	public Double getFristScote() {
		return fristScote;
	}
	public void setFristScote(Double fristScote) {
		this.fristScote = fristScote;
	}
	public Double getScote() {
		return scote;
	}
	public void setScote(Double scote) {
		this.scote = scote;
	}
	public String getAdjustRemark() {
		return adjustRemark;
	}
	public void setAdjustRemark(String adjustRemark) {
		this.adjustRemark = adjustRemark;
	}
	public String getAdjustId() {
		return adjustId;
	}
	public void setAdjustId(String adjustId) {
		this.adjustId = adjustId;
	}
	public String getAdjustUser() {
		return adjustUser;
	}
	public void setAdjustUser(String adjustUser) {
		this.adjustUser = adjustUser;
	}
	public String getSubTime() {
		return subTime;
	}
	public void setSubTime(String subTime) {
		this.subTime = subTime;
	}
	public String getLoginUrl() {
		return loginUrl;
	}
	public void setLoginUrl(String loginUrl) {
		this.loginUrl = loginUrl;
	}
	public String getExamRootName() {
		return examRootName;
	}
	public void setExamRootName(String examRootName) {
		this.examRootName = examRootName;
	}
	public String getExamRootPlace() {
		return examRootPlace;
	}
	public void setExamRootPlace(String examRootPlace) {
		this.examRootPlace = examRootPlace;
	}
	public String getSeatNum() {
		return seatNum;
	}
	public void setSeatNum(String seatNum) {
		this.seatNum = seatNum;
	}
	public String getIsIdNumberLogin() {
		return isIdNumberLogin;
	}
	public void setIsIdNumberLogin(String isIdNumberLogin) {
		this.isIdNumberLogin = isIdNumberLogin;
	}
	public String getExamPaperInit() {
		return examPaperInit;
	}
	public void setExamPaperInit(String examPaperInit) {
		this.examPaperInit = examPaperInit;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
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
	
	
	
	
}
