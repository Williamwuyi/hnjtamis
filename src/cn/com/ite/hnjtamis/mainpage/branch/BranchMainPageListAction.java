package cn.com.ite.hnjtamis.mainpage.branch;

import java.util.ArrayList;
import java.util.List;

import cn.com.ite.eap2.core.struts2.AbstractListAction;
import cn.com.ite.eap2.module.power.login.LoginAction;
import cn.com.ite.eap2.module.power.login.UserSession;
import cn.com.ite.hnjtamis.mainpage.domain.ViewOrganProgress;
import cn.com.ite.hnjtamis.mainpage.domain.ViewTrainPlanRatio;
import cn.com.ite.hnjtamis.mainpage.domain.ViewTranPlan;
import cn.com.ite.hnjtamis.personal.domain.PersonalRateProgress;
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
public class BranchMainPageListAction extends AbstractListAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8061559691203248709L;
	/**
	 * 查询结果
	 */
	private List<ViewTranPlan> tranplanlist = new ArrayList<ViewTranPlan>();
	private List<ViewTrainPlanRatio> tranplanratiolist=new ArrayList<ViewTrainPlanRatio>();
	private List<ViewOrganProgress> organprogresslist= new ArrayList<ViewOrganProgress>();
//	private List<ViewTranPlan> depttrainplanlist = new ArrayList<ViewTranPlan>();
	
	// 查询条件
	private String toptypeidTerm;
	private String startTimeTerm="";  //
	private String endTimeTerm="";
	private String contentsTerm;
	private String nameTerm="";  //
	private String personcodeTerm="";
	
	
	public String getPersoncodeTerm() {
		return personcodeTerm;
	}
	public void setPersoncodeTerm(String personcodeTerm) {
		this.personcodeTerm = personcodeTerm;
	}
	public String getNameTerm() {
		return nameTerm;
	}
	public void setNameTerm(String nameTerm) {
		this.nameTerm = nameTerm;
	}
	 
	
	public String getStartTimeTerm() {
		return startTimeTerm;
	}
	public void setStartTimeTerm(String startTimeTerm) {
		this.startTimeTerm = startTimeTerm;
	}
	public String getEndTimeTerm() {
		return endTimeTerm;
	}
	public void setEndTimeTerm(String endTimeTerm) {
		this.endTimeTerm = endTimeTerm;
	}
	public String getContentsTerm() {
		return contentsTerm;
	}
	public void setContentsTerm(String contentsTerm) {
		this.contentsTerm = contentsTerm;
	}
	
	/**
	 * 分公司首页显示
	 * @return
	 * @throws Exception
	 */
	public String branchlist() throws Exception {
		return "showbranchlist";
	}
	/**
	 * 显示覆盖率
	 * @return
	 * @throws Exception
	 */
	public String coveragelist() throws Exception {
		/// 培训比率
		UserSession us = LoginAction.getUserSessionInfo();
		String organid =us.getOrganId();  /// 机构ID
		BranchMainPageService mservice=(BranchMainPageService)service;
		this.tranplanratiolist = mservice.findTranplanratiolist(organid);
		
		return "coveragelist";
	}
	
	public String list()throws Exception{
		BranchMainPageService mservice=(BranchMainPageService)service;
		UserSession us = LoginAction.getUserSessionInfo();
		setPersoncodeTerm(us.getEmployeeId());
		String organid =us.getOrganId();  /// 机构ID
 
		/// 本部机构培训达标情况
		tranplanlist = mservice.findTranplanlist(organid);
		/// 培训比率
//		this.tranplanratiolist = mservice.findTranplanratiolist(organid);
		/// 机构达标进度
		organprogresslist = mservice.findSubOrganprogresslist(organid);
		///this.setTotal(service.countData("queryHql", this));
		return "showbranchlist";
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
	
	
	public String getToptypeidTerm() {
		return toptypeidTerm;
	}
	public void setToptypeidTerm(String toptypeidTerm) {
		this.toptypeidTerm = toptypeidTerm;
	}
	public List<ViewTranPlan> getTranplanlist() {
		return tranplanlist;
	}
	public void setTranplanlist(List<ViewTranPlan> tranplanlist) {
		this.tranplanlist = tranplanlist;
	}
	public List<ViewTrainPlanRatio> getTranplanratiolist() {
		return tranplanratiolist;
	}
	public void setTranplanratiolist(List<ViewTrainPlanRatio> tranplanratiolist) {
		this.tranplanratiolist = tranplanratiolist;
	}
	public List<ViewOrganProgress> getOrganprogresslist() {
		return organprogresslist;
	}
	public void setOrganprogresslist(List<ViewOrganProgress> organprogresslist) {
		this.organprogresslist = organprogresslist;
	}
	
	
	 
}
