package cn.com.ite.hnjtamis.exam.base.examroot.form;

import java.util.ArrayList;
import java.util.List;

import cn.com.ite.hnjtamis.exam.hibernatemap.ExamExamroot;
import cn.com.ite.hnjtamis.exam.hibernatemap.ExamPublic;
import cn.com.ite.hnjtamis.exam.hibernatemap.ExamRoot;
/*
 * 考试安排中 考点form
 */
public class ArrangeRootForm {
	private String examArrangeId;//考试安排ID
	private String examArrangeName;//考试名称
	private Double score;//总分
	private String examCode;//考试编码 考试类型编码 + 日期 + 序号（2位）
	private String examTypeId;//考试类型(性质)ID，数据字典定义（安规、岗位、竞赛等）
	private String examTypeName;//考试类型(性质)，与考试类型ID一组
	private Integer examProperty;//考试类型  10- 达标考试 20-竞赛考试 30-培训测试 40-模拟考试
	private Integer isPublic;//是否发布成绩 5：否，10：是
	private String publicUser;//成绩发布人
	private String publicUserId;//成绩发布人编号
	private String publicTime;//成绩发布时间
	private Integer isUse;///是否使用 5：否,10：是
	private String checkUser;//审核人
	private String checkUserId;///审核人ID
	private String checkTime;//审核时间
	private String state;//
	private Integer examPaperType;//
	private String isIdNumberLogin;//
	private String relationId;//关联ID（如：练习安排等）
	private String relationType;//关联类型
	private String remark;//备注
	private String organName;//机构名
	private String organId;//机构编号
	private String syncFlag;////同步标志
	private String lastUpdateDate;///最后修改时间
	private String lastUpdatedBy;//最后修改人
	private String lastUpdatedIdBy;//最后修改人ID
	private String creationDate;//创建时间
	private String createdBy;//创建人
	private String createdIdBy;//创建人ID
	
	private String publicId;
	private ExamPublic examPublic;
	
	private List<ExamExamroot>  examRootList = new ArrayList<ExamExamroot>() ;

	public String getExamArrangeId() {
		return examArrangeId;
	}

	public void setExamArrangeId(String examArrangeId) {
		this.examArrangeId = examArrangeId;
	}

	public String getExamArrangeName() {
		return examArrangeName;
	}

	public void setExamArrangeName(String examArrangeName) {
		this.examArrangeName = examArrangeName;
	}

	public Double getScore() {
		return score;
	}

	public void setScore(Double score) {
		this.score = score;
	}

	public String getExamCode() {
		return examCode;
	}

	public void setExamCode(String examCode) {
		this.examCode = examCode;
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

	public Integer getIsPublic() {
		return isPublic;
	}

	public void setIsPublic(Integer isPublic) {
		this.isPublic = isPublic;
	}

	public String getPublicUser() {
		return publicUser;
	}

	public void setPublicUser(String publicUser) {
		this.publicUser = publicUser;
	}

	public String getPublicUserId() {
		return publicUserId;
	}

	public void setPublicUserId(String publicUserId) {
		this.publicUserId = publicUserId;
	}

	public String getPublicTime() {
		return publicTime;
	}

	public void setPublicTime(String publicTime) {
		this.publicTime = publicTime;
	}

	public Integer getIsUse() {
		return isUse;
	}

	public void setIsUse(Integer isUse) {
		this.isUse = isUse;
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

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Integer getExamPaperType() {
		return examPaperType;
	}

	public void setExamPaperType(Integer examPaperType) {
		this.examPaperType = examPaperType;
	}

	public String getIsIdNumberLogin() {
		return isIdNumberLogin;
	}

	public void setIsIdNumberLogin(String isIdNumberLogin) {
		this.isIdNumberLogin = isIdNumberLogin;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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

	public String getPublicId() {
		return publicId;
	}

	public void setPublicId(String publicId) {
		this.publicId = publicId;
	}

	public ExamPublic getExamPublic() {
		return examPublic;
	}

	public void setExamPublic(ExamPublic examPublic) {
		this.examPublic = examPublic;
	}

	public List<ExamExamroot> getExamRootList() {
		return examRootList;
	}

	public void setExamRootList(List<ExamExamroot> examRootList) {
		this.examRootList = examRootList;
	}

	
	
	
	
}
