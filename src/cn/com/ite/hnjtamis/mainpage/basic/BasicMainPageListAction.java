package cn.com.ite.hnjtamis.mainpage.basic;

import java.util.ArrayList;
import java.util.List;

import cn.com.ite.eap2.common.utils.DateUtils;
import cn.com.ite.eap2.common.utils.StringUtils;
import cn.com.ite.eap2.core.spring.SpringContextUtil;
import cn.com.ite.eap2.core.struts2.AbstractListAction;
import cn.com.ite.eap2.module.power.login.LoginAction;
import cn.com.ite.eap2.module.power.login.UserSession;
import cn.com.ite.hnjtamis.common.action.employee.EmployeeTreeService;
import cn.com.ite.hnjtamis.mainpage.domain.ViewOrganProgress;
import cn.com.ite.hnjtamis.mainpage.domain.ViewTrainPerson;
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
public class BasicMainPageListAction extends AbstractListAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8061559691203248709L;
	/**
	 * 查询结果
	 */
	private List<ViewTranPlan> tranplanlist = new ArrayList<ViewTranPlan>();
	private List<ViewTranPlan> monthplanlist = new ArrayList<ViewTranPlan>();// 月度计划完成情况
	private ViewTrainPerson trainperson = new ViewTrainPerson();
	private ViewOrganProgress organprogress = new ViewOrganProgress();
	private Integer plantotalnums = 0; // 计划总学习人数
	private Integer completetotalnums =0; // 完成学习总人数
	
	// 查询条件
	private String toptypeidTerm;
	private String monthTerm;
	private String organIdTerm;
	
	private List organDeptlist;//机构部门信息
	
	 
	public String getMonthTerm() {
		return monthTerm;
	}
	public void setMonthTerm(String monthTerm) {
		this.monthTerm = monthTerm;
	}
	public List<ViewTranPlan> getTranplanlist() {
		return tranplanlist;
	}
	public void setTranplanlist(List<ViewTranPlan> tranplanlist) {
		this.tranplanlist = tranplanlist;
	}
	/**
	 * 显示月度培训计划
	 * @return
	 * @throws Exception
	 */
	public String monthplanlist() throws Exception {
		BasicMainPageService mservice=(BasicMainPageService)service;
		UserSession us = LoginAction.getUserSessionInfo();
		String organid =us.getOrganId(); // 机构ID
		
		/// 月度计划完成情况
		String month ="";
		if(!StringUtils.isEmpty(monthTerm)) month=DateUtils.getCurrYear()+"-"+monthTerm;
		this.monthplanlist =mservice.findMonthplanlist(organid,month);
		return "monthplanlist";
	}
	/**
	 * 基层企业首页显示
	 * @return
	 * @throws Exception
	 */
	public String basiclist() throws Exception {
		return "showbasiclist";
	}
	
	
	public String list()throws Exception{
		BasicMainPageService mservice=(BasicMainPageService)service;
		UserSession us = LoginAction.getUserSessionInfo();
		
		if(organIdTerm==null || "".equals(organIdTerm) || "null".equals(organIdTerm)){
			organIdTerm =us.getOrganId(); // 机构ID
		}
		//String organid =us.getOrganId(); // 机构ID
		
		
		EmployeeTreeService employeeTreeService = (EmployeeTreeService)SpringContextUtil.getBean("employeeTreeService");
		organDeptlist = employeeTreeService.queryDeptInOwnerOrgan(organIdTerm,1);
		return "showbasiclist";
	}
	
	/*public String list()throws Exception{
		BasicMainPageService mservice=(BasicMainPageService)service;
		UserSession us = LoginAction.getUserSessionInfo();
		
		String organid =us.getOrganId(); // 机构ID
		
		this.tranplanlist = mservice.findTranplanlist(organid);
		/// 求合计数
		for(int i=0;i<tranplanlist.size();i++){
			ViewTranPlan vo=tranplanlist.get(i);
			plantotalnums+=vo.getPlannums();
			completetotalnums+=vo.getCompletenums();
		}
		/// 本单位达标情况
		this.organprogress = mservice.findSubOrganprogresslist(organid);
		/// 本单位最新达标人员
		this.trainperson = mservice.findTrainperson(organid);
		
		/// 月度计划完成情况
		///this.monthplanlist =mservice.findMonthplanlist(organid);
		
		
		///this.setTotal(service.countData("queryHql", this));
		return "showbasiclist";
	}*/
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
	public Integer getPlantotalnums() {
		return plantotalnums;
	}
	public void setPlantotalnums(Integer plantotalnums) {
		this.plantotalnums = plantotalnums;
	}
	public Integer getCompletetotalnums() {
		return completetotalnums;
	}
	public void setCompletetotalnums(Integer completetotalnums) {
		this.completetotalnums = completetotalnums;
	}
	public ViewTrainPerson getTrainperson() {
		return trainperson;
	}
	public void setTrainperson(ViewTrainPerson trainperson) {
		this.trainperson = trainperson;
	}
	public ViewOrganProgress getOrganprogress() {
		return organprogress;
	}
	public void setOrganprogress(ViewOrganProgress organprogress) {
		this.organprogress = organprogress;
	}
	public List<ViewTranPlan> getMonthplanlist() {
		return monthplanlist;
	}
	public void setMonthplanlist(List<ViewTranPlan> monthplanlist) {
		this.monthplanlist = monthplanlist;
	}
	public List getOrganDeptlist() {
		return organDeptlist;
	}
	public void setOrganDeptlist(List organDeptlist) {
		this.organDeptlist = organDeptlist;
	}
	public String getOrganIdTerm() {
		return organIdTerm;
	}
	public void setOrganIdTerm(String organIdTerm) {
		this.organIdTerm = organIdTerm;
	}
	 
	 
	 
}
