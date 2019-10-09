package cn.com.ite.hnjtamis.train.plan;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.com.ite.eap2.common.utils.DateUtils;
import cn.com.ite.eap2.core.struts2.AbstractListAction;
import cn.com.ite.eap2.module.power.login.LoginAction;
import cn.com.ite.hnjtamis.train.domain.TrainPlan;

public class TrainPlanListAction extends AbstractListAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3788584381504108881L;

	/**
	 * 查询结果对象
	 */
	private List<TrainPlan> list = new ArrayList<TrainPlan>();
	/**
	 * 查询条件
	 */
	private String titleTerm;
	private String quarterTerm;
	private String specialityTerm;
	private Integer yearTerm;
	private Integer auditTerm;
	private String deptTerm;

	/**
	 * 查询方法
	 * 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String list() throws Exception {
		list = (List<TrainPlan>) service.queryData("queryHql", this,
				this.getSortMap(), this.getStart(), this.getLimit());
		this.setTotal(service.countData("queryHql", this));
		return "list";
	}

	@SuppressWarnings({ "unchecked" })
	public String all() throws Exception {
		list = (List<TrainPlan>) service.queryData("queryAllHql", this,
				this.getSortMap());
		return "list";
	}

	/**
	 * 删除
	 * 
	 * @return
	 * @modified
	 */
	public String delete() throws Exception {
		String[] ids = this.getId().split(",");
		for (int i = 0; i < ids.length; i++) {
			TrainPlan plan = (TrainPlan) service.findDataByKey(ids[i],
					TrainPlan.class);
			plan.setIsDel(1);
			plan.setSyncStatus(3);
			service.saveOld(plan);
		}
		this.setMsg("删除成功！");
		return "delete";
	}

	/**
	 * 审核
	 * 
	 * @return
	 * @throws Exception
	 */
	public String audit() throws Exception {
		TrainPlan plan = (TrainPlan) service.findDataByKey(this.getId(),
				TrainPlan.class);
		plan.setIsAudited(1);
		plan.setAuditTime(DateUtils.convertDateToStr(new Date(),
				"yyyy-MM-dd HH:mm:ss"));
		plan.setAuditerId(LoginAction.getUserSessionInfo().getEmployeeId());
		plan.setAuditerName(LoginAction.getUserSessionInfo().getEmployeeName());
		service.saveOld(plan);
		this.setMsg("审核成功！");
		return "save";
	}

	public List<TrainPlan> getList() {
		return list;
	}

	public void setList(List<TrainPlan> list) {
		this.list = list;
	}

	public String getTitleTerm() {
		return titleTerm;
	}

	public void setTitleTerm(String titleTerm) {
		this.titleTerm = titleTerm;
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

	public Integer getYearTerm() {
		return yearTerm;
	}

	public void setYearTerm(Integer yearTerm) {
		this.yearTerm = yearTerm;
	}

	public Integer getAuditTerm() {
		return auditTerm;
	}

	public void setAuditTerm(Integer auditTerm) {
		this.auditTerm = auditTerm;
	}

	public String getDeptTerm() {
		return deptTerm;
	}

	public void setDeptTerm(String deptTerm) {
		this.deptTerm = deptTerm;
	}

}
