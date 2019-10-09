package cn.com.ite.hnjtamis.mainpage.all;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import cn.com.ite.eap2.core.spring.SpringContextUtil;
import cn.com.ite.eap2.core.struts2.AbstractListAction;
import cn.com.ite.eap2.domain.baseinfo.SysAffiche;
import cn.com.ite.eap2.domain.organization.Organ;
import cn.com.ite.eap2.module.power.login.LoginAction;
import cn.com.ite.eap2.module.power.login.UserSession;
import cn.com.ite.hnjtamis.document.DocumentLibService;
import cn.com.ite.hnjtamis.document.domain.DocumentLib;
import cn.com.ite.hnjtamis.kb.course.CoursewareService;
import cn.com.ite.hnjtamis.kb.domain.Courseware;
import cn.com.ite.hnjtamis.mainpage.domain.ViewPersonProgress;
import cn.com.ite.hnjtamis.personal.domain.PersonalRateProgress;
import cn.com.ite.hnjtamis.talent.domain.TalentRegistration;
/**
 * 
 * <p>Title 岗位达标培训信息系统-首页管理模块</p>
 * <p>Description 首页Action
 * 包括分子公司、基层单位进入后的首页展示。主要显示内容：培训情况、达标情况
 * </p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2015</p>
 * @author wangyong
 * @create 2015.5.5  10:45:27 AM
 * @version 1.0
 * 
 * @modified records:
 */
public class MainPageListAction extends AbstractListAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8061559691203248709L;
	private CoursewareService coursewareService;
	//private MainPageService 
	/**
	 * 查询结果
	 */
	private List<Courseware> coursewarelist = new ArrayList<Courseware>();//课件教材库
	private List<TalentRegistration> talentreglist = new ArrayList<TalentRegistration>();// 专家库
	private List<SysAffiche> sysaffichelist = new ArrayList<SysAffiche>(); // 公告
	private List<ViewPersonProgress> personprogresslist = new ArrayList<ViewPersonProgress>();
	private Organ organ = new Organ();
	private List<DocumentLib> documentlist = new ArrayList<DocumentLib>();//文档库资料
	
	// 查询条件
	private String coursewareId="";
	private String inticketTerm="";
	private String idnumberTerm="";
	
	
	
	public String getInticketTerm() {
		return inticketTerm;
	}

	public void setInticketTerm(String inticketTerm) {
		this.inticketTerm = inticketTerm;
	}

	public String getIdnumberTerm() {
		return idnumberTerm;
	}

	public void setIdnumberTerm(String idnumberTerm) {
		this.idnumberTerm = idnumberTerm;
	}

	public String getCoursewareId() {
		return coursewareId;
	}

	public void setCoursewareId(String coursewareId) {
		this.coursewareId = coursewareId;
	}
	
	public String queryShareDocumentLib(){
		DocumentLibService documentLibService = (DocumentLibService)SpringContextUtil.getBean("documentLibService");
		Map term = new HashMap();
		documentlist = documentLibService.queryData("getShareDocumentLibList", term, new HashMap());
		Map accessoryMap = documentLibService.getAccessoryIdInDocument();
		for(int i=0;i<documentlist.size();i++){
			DocumentLib documentLib = documentlist.get(i);
			String accId = (String)accessoryMap.get(documentLib.getDocumentId());
			documentLib.setAfficheId(accId);
		}
		return "queryShareDocumentLib";
	}
	
	/**
	 * ajax显示课件
	 * @return
	 * @throws Exception
	 */
	public String courseware() throws Exception{
		coursewarelist = ((MainPageService)service).findCoursewarelist();
		return "coursewarelist";
	}
	/**
	 * ajax显示专家信息
	 * @return
	 * @throws Exception
	 */
	public String talentreg() throws Exception{
		talentreglist = ((MainPageService)service).findTalentreglist();
		return "talentreglist";
	}
	
	/**
	 * ajax显示公告信息
	 * @return
	 * @throws Exception
	 */
	public String sysaffiche() throws Exception{
		sysaffichelist = ((MainPageService)service).findSysaffichelist();
		return "sysaffichelist";
	}
	
	/**
	 * ajax显示个人达标信息
	 * @return
	 * @throws Exception
	 */
	public String personprogress() throws Exception{
		personprogresslist = ((MainPageService)service).findPersonprogresslist();
		return "personprogresslist";
	}
	
	public String list()throws Exception{
		MainPageService mservice=(MainPageService)service;
		coursewarelist = mservice.findCoursewarelist();
		talentreglist = mservice.findTalentreglist();
		sysaffichelist = mservice.findSysaffichelist();
		personprogresslist = mservice.findPersonprogresslist();
		return "list";
	}
	
	/**
	 * 显示分数
	 * @return
	 * @throws Exception
	 */
	public String score() throws Exception {
		return "score";
	}
	
	/**
	 * 查询机构
	 * @return
	 * @throws Exception
	 */
	public String organization() throws Exception{
		UserSession us = LoginAction.getUserSessionInfo();
		organ = (Organ)service.findDataByKey(us.getOrganId(), Organ.class);
		return "organization";
	}
	
	public String writepic() throws Exception {
//		HttpServletResponse response = (HttpServletResponse)
//				ActionContext.getContext().get(org.apache.struts2.StrutsStatics.HTTP_RESPONSE);
		///String coursewareId = 
		HttpServletResponse response = ServletActionContext.getResponse();
		Courseware vo =(Courseware)getCoursewareService().findDataByKey(coursewareId, Courseware.class);
		
		if(vo!=null && vo.getPoster().length>0){
			try{
				response.getOutputStream().write(vo.getPoster());
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				response.getOutputStream().flush();
				response.getOutputStream().close();
			}
				
			///ExamUserTestpaper d;
		}
		
		return null;
	}
	
	/**
	 * 删除
	 * @return
	 * @modified
	 */
	public String delete() throws Exception{
		// 
		///service.findDataByKey(key, type)
		service.deleteByKeys(this.getId().split(","), PersonalRateProgress.class);
		this.setMsg("信息删除成功！");
		return "delete";
	}
	public List<Courseware> getCoursewarelist() {
		return coursewarelist;
	}
	public void setCoursewarelist(List<Courseware> coursewarelist) {
		this.coursewarelist = coursewarelist;
	}
	public List<TalentRegistration> getTalentreglist() {
		return talentreglist;
	}
	public void setTalentreglist(List<TalentRegistration> talentreglist) {
		this.talentreglist = talentreglist;
	}
	public List<SysAffiche> getSysaffichelist() {
		return sysaffichelist;
	}
	public void setSysaffichelist(List<SysAffiche> sysaffichelist) {
		this.sysaffichelist = sysaffichelist;
	}
	public List<ViewPersonProgress> getPersonprogresslist() {
		return personprogresslist;
	}
	public void setPersonprogresslist(List<ViewPersonProgress> personprogresslist) {
		this.personprogresslist = personprogresslist;
	}

	public CoursewareService getCoursewareService() {
		return coursewareService;
	}

	public void setCoursewareService(CoursewareService coursewareService) {
		this.coursewareService = coursewareService;
	}

	public Organ getOrgan() {
		return organ;
	}

	public void setOrgan(Organ organ) {
		this.organ = organ;
	}

	public List<DocumentLib> getDocumentlist() {
		return documentlist;
	}

	public void setDocumentlist(List<DocumentLib> documentlist) {
		this.documentlist = documentlist;
	}
	
 
	 
	 
}
