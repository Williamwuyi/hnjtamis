package cn.com.ite.hnjtamis.exam.base.exammarkpeopleinfo;

import java.util.*;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;

import cn.com.ite.eap2.common.utils.StringUtils;
import cn.com.ite.eap2.core.service.TreeNode;
import cn.com.ite.eap2.core.struts2.AbstractListAction;
import cn.com.ite.eap2.core.struts2.ServletContent;
import cn.com.ite.eap2.module.power.login.UserSession;
import cn.com.ite.hnjtamis.common.StaticVariable;
import cn.com.ite.hnjtamis.exam.base.exammarkpeopleinfo.service.ExamMarkPeopleInfoService;
import cn.com.ite.hnjtamis.exam.hibernatemap.ExamMarkpeopleInfo;
import cn.com.ite.hnjtamis.talent.domain.TalentRegistration;
/*
 *  阅卷老师信息维护 - list
 */
public class ExamMarkPeopleInfoListAction extends AbstractListAction implements ServletRequestAware{
	private static final long serialVersionUID = -5864462903447351081L;

	private HttpServletRequest request;
	private String titleTerm;//姓名查询条件
	private String nameTerm;//树的姓名查询
	private String examId;
	private List<ExamMarkpeopleInfo> list = new ArrayList<ExamMarkpeopleInfo>();
	private List<ExamMarkpeopleInfo> queryReviewerCom = new ArrayList<ExamMarkpeopleInfo>();
	
	//专家阅卷人 下拉框
	private List<TalentRegistration> queryZJReviewerCom = new ArrayList<TalentRegistration>();
	
	private String organId;
	private List children;
	
	public String queryReviewerTree(){
		ExamMarkPeopleInfoService examMarkPeopleInfoService = (ExamMarkPeopleInfoService)this.getService();
		children = examMarkPeopleInfoService.getExaminationUserList(organId);
		if(!StringUtils.isEmpty(nameTerm)){//条件过滤
			TreeNode.select(children, nameTerm);
		}
		return "examMarkPeopleInfoList";
	}
	
	public String queryExamReviewerTree(){
		ExamMarkPeopleInfoService examMarkPeopleInfoService = (ExamMarkPeopleInfoService)this.getService();
		children = examMarkPeopleInfoService.getExaminationUserInExamList(examId);
		if(!StringUtils.isEmpty(nameTerm)){//条件过滤
			TreeNode.select(children, nameTerm);
		}
		return "examMarkPeopleInfoList";
	}
	
	public String queryExamReviewerExTree(){
		UserSession usersess = (UserSession)ServletContent.getSession().get(StaticVariable.USERSESSION);
		if(usersess == null){
			this.setMsg("SESSION　信息已失效，请关闭浏览器并重新登陆！");
			children = new ArrayList();
			return "examMarkPeopleInfoList";
	 	}
		if(organId == null){
			organId = usersess.getCurrentOrganId();
		}
		ExamMarkPeopleInfoService examMarkPeopleInfoService = (ExamMarkPeopleInfoService)this.getService();
		//查询阅卷老师(还包括本机构的其它人员)
		children = examMarkPeopleInfoService.getExaminationUserInExamListEx(examId,organId,nameTerm);
		if(!StringUtils.isEmpty(nameTerm)){//条件过滤
			TreeNode.select(children, nameTerm);
		}
		return "examMarkPeopleInfoList";
	}
	
	public String getNameTerm() {
		return nameTerm;
	}

	public void setNameTerm(String nameTerm) {
		this.nameTerm = nameTerm;
	}

	//查询专家阅卷人下拉框
	public String queryZJReviewerCom(){
		queryZJReviewerCom = service.queryData("queryZJReviewerCom", null, null, TalentRegistration.class);
		return "queryZJReviewerCom";
	}
	
	/*
	 * 列表查询
	 */
	public String list() throws Exception{
		try {
			list = service.queryData("queryHql", this, null, ExamMarkpeopleInfo.class, this.getStart(), this.getLimit());
			this.setTotal(service.countData("queryHql", this));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "list";
	}
	public String queryReviewerCom(){
		queryReviewerCom = service.queryData("queryReviewerCom", null, null, ExamMarkpeopleInfo.class);
		return "queryReviewerCom";
	}
	/*
	 * 删除
	 */
	public String delete(){
		try {
			service.deleteByKeys(this.getId().split(","), ExamMarkpeopleInfo.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "delete";
	}
	
	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public List<ExamMarkpeopleInfo> getList() {
		return list;
	}

	public void setList(List<ExamMarkpeopleInfo> list) {
		this.list = list;
	}

	public String getTitleTerm() {
		return titleTerm;
	}

	public void setTitleTerm(String titleTerm) {
		this.titleTerm = titleTerm;
	}
	public List<ExamMarkpeopleInfo> getQueryReviewerCom() {
		return queryReviewerCom;
	}
	public void setQueryReviewerCom(List<ExamMarkpeopleInfo> queryReviewerCom) {
		this.queryReviewerCom = queryReviewerCom;
	}
	public List<TalentRegistration> getQueryZJReviewerCom() {
		return queryZJReviewerCom;
	}
	public void setQueryZJReviewerCom(List<TalentRegistration> queryZJReviewerCom) {
		this.queryZJReviewerCom = queryZJReviewerCom;
	}

	public String getOrganId() {
		return organId;
	}

	public void setOrganId(String organId) {
		this.organId = organId;
	}

	public List getChildren() {
		return children;
	}

	public void setChildren(List children) {
		this.children = children;
	}

	public String getExamId() {
		return examId;
	}

	public void setExamId(String examId) {
		this.examId = examId;
	}
	
	
	
}
