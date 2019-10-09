package cn.com.ite.hnjtamis.talent.reg;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import cn.com.ite.eap2.common.utils.DateUtils;
import cn.com.ite.eap2.core.service.TreeNode;
import cn.com.ite.eap2.core.struts2.AbstractListAction;
import cn.com.ite.eap2.core.struts2.ServletContent;
import cn.com.ite.eap2.module.power.login.LoginAction;
import cn.com.ite.eap2.module.power.login.UserSession;
import cn.com.ite.hnjtamis.baseinfo.speciality.SpecialityService;
import cn.com.ite.hnjtamis.common.StaticVariable;
import cn.com.ite.hnjtamis.talent.domain.TalentRegistration;

/**
 * <p>
 * Title cn.com.ite.hnjtamis.talent.reg.TalentRegistrationListAction
 * </p>
 * <p>
 * Description 专家库ListAction
 * </p>
 * <p>
 * Company ITE
 * </p>
 * <p>
 * Copyright Copyright(c)2014
 * </p>
 * 
 * @author 李奉学
 * @create time 2015-4-8
 * @version 1.0
 * 
 * @modified
 */
public class TalentRegistrationListAction extends AbstractListAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6453892396403395220L;

	/**
	 * 查询对象
	 */
	private List<TalentRegistration> list = new ArrayList<TalentRegistration>();

	List<TalentChart> chartList = new ArrayList<TalentChart>();
	/**
	 * 树对象
	 */
	private List<TreeNode> children;
	private String nameTerm;
	private String quarterTerm;
	private String specialityTerm;
	private String typeTerm;
	private String organTerm;
	private String deptTerm;
	
	/**
	 * 导入文件
	 */
	private File xls;
	
	/*
	 * 导入
	 */
	public String importXls() throws Exception{
		UserSession usersess = (UserSession)ServletContent.getSession().get(StaticVariable.USERSESSION);
		if(usersess == null){
			this.setMsg("SESSION　信息已失效，请关闭浏览器并重新登陆！");
			return "save";
	 	}
		TalentRegistrationService talentRegistrationService=(TalentRegistrationService)getService();
		String msg = talentRegistrationService.importTalentRegistration(xls,usersess);
		this.setMsg(msg);
		return "save";
	}
	
	public String syncBank()throws Exception{
		UserSession usersess = (UserSession)ServletContent.getSession().get(StaticVariable.USERSESSION);
		if(usersess == null){
			this.setMsg("SESSION　信息已失效，请关闭浏览器并重新登陆！");
			return "save";
	 	}
		try {
			TalentRegistrationService talentRegistrationService=(TalentRegistrationService)getService();
			talentRegistrationService.saveSyncBank();
			this.setMsg("专家与题库关联关系同步成功！");
		} catch (Exception e) {
			this.setMsg("专家与题库关联关系同步失败！");
			e.printStackTrace();
		}
		return "save";
	}

	/**
	 * 查询方法
	 * 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String list() throws Exception {
		try {
			list = (List<TalentRegistration>) service.queryData("queryHql",
					this, this.getSortMap(), this.getStart(), this.getLimit());
			
			TalentRegistrationService talentRegistrationService = (TalentRegistrationService)service;
			Map<String,Integer> fkThemeAuditNumMap = talentRegistrationService.getFkThemeAuditNumMap();//获取用户反馈审核的次数
			Map<String,Integer> examMarkNumMap = talentRegistrationService.getExamMarkNumMap();//获取用户阅卷的次数
			for(int i=0;i<list.size();i++){
				TalentRegistration talentRegistration = list.get(i);
				try{
					Integer fkThemeAuditNum = fkThemeAuditNumMap.get(talentRegistration.getEmployee().getEmployeeId());
					if(fkThemeAuditNum!=null)
						talentRegistration.setFkThemeAuditNum(fkThemeAuditNum);
					Integer examMarkNum = examMarkNumMap.get(talentRegistration.getEmployee().getEmployeeId());
					if(examMarkNum!=null)
						talentRegistration.setExamMarkNum(examMarkNum);
				}catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.setTotal(service.countData("queryHql", this));
		return "list";
	}

	/**
	 * 查询统计图表数据
	 * 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String chart() throws Exception {
		try {
			chartList = service.queryData("queryChartHql", this, null,
					TalentChart.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "chart";
	}

	/**
	 * 统计数据形成树形结构
	 * 
	 * @return
	 * @throws Exception
	 */
	public String treeList() throws Exception {
		try {
			children = ((TalentRegistrationService) service)
					.findTreeList(organTerm);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "treeList";
	}

	public String showReport() throws Exception {
		return "showReport";
	}

	/**
	 * 删除
	 * 
	 * @return
	 * @modified
	 */
	public String delete() throws Exception {
		String[] ids = this.getId().split(",");
		int num = 0;
		for (int i = 0; i < ids.length; i++) {
			TalentRegistration online = (TalentRegistration) service
					.findDataByKey(ids[i], TalentRegistration.class);
			if(online.getIsDel() == 0){
				online.setIsDel(1);
				online.setSyncStatus(3);
				service.saveOld(online);
				num++;
			}
		}
		//this.setMsg("注销成功！");
		this.setMsg("您成功注销了"+num+"条记录！");
		return "delete";
	}
	
	/**
	 * 重新启用
	 * 
	 * @return
	 * @modified
	 */
	public String reuse() throws Exception {
		String[] ids = this.getId().split(",");
		int num = 0;
		for (int i = 0; i < ids.length; i++) {
			TalentRegistration online = (TalentRegistration) service
					.findDataByKey(ids[i], TalentRegistration.class);
			if(online.getIsDel() == 1){
				online.setIsDel(0);
				online.setSyncStatus(2);
				service.saveOld(online);
				num++;
			}
		}
		this.setMsg("您成功启用了"+num+"条记录！");
		return "delete";
	}

	/**
	 * 审核
	 * 
	 * @return
	 * @throws Exception
	 */
	public String audit() throws Exception {
		TalentRegistration talent = (TalentRegistration) service.findDataByKey(
				this.getId(), TalentRegistration.class);
		talent.setIsAudited(1);
		talent.setAuditTime(DateUtils.convertDateToStr(new Date(),
				"yyyy-MM-dd HH:mm:ss"));
		talent.setAuditerId(LoginAction.getUserSessionInfo().getEmployeeId());
		talent.setAuditerName(LoginAction.getUserSessionInfo()
				.getEmployeeName());
		service.saveOld(talent);
		this.setMsg("审核成功！");
		return "save";
	}

	public List<TalentRegistration> getList() {
		return list;
	}

	public void setList(List<TalentRegistration> list) {
		this.list = list;
	}

	public String getNameTerm() {
		return nameTerm;
	}

	public void setNameTerm(String nameTerm) {
		this.nameTerm = nameTerm;
	}

	public String getQuarterTerm() {
		return quarterTerm;
	}

	public void setQuarterTerm(String quarterTerm) {
		this.quarterTerm = quarterTerm;
	}

	public String getSpecialityTerm() {
		return specialityTerm;
	}

	public void setSpecialityTerm(String specialityTerm) {
		this.specialityTerm = specialityTerm;
	}

	public String getTypeTerm() {
		return typeTerm;
	}

	public void setTypeTerm(String typeTerm) {
		this.typeTerm = typeTerm;
	}

	public String getOrganTerm() {
		return organTerm;
	}

	public void setOrganTerm(String organTerm) {
		this.organTerm = organTerm;
	}

	public List<TreeNode> getChildren() {
		return children;
	}

	public void setChildren(List<TreeNode> children) {
		this.children = children;
	}

	public String getDeptTerm() {
		return deptTerm;
	}

	public void setDeptTerm(String deptTerm) {
		this.deptTerm = deptTerm;
	}

	public List<TalentChart> getChartList() {
		return chartList;
	}

	public void setChartList(List<TalentChart> chartList) {
		this.chartList = chartList;
	}


	public File getXls() {
		return xls;
	}


	public void setXls(File xls) {
		this.xls = xls;
	}
	
	
}
