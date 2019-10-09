package cn.com.ite.hnjtamis.train.plan;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.com.ite.eap2.common.utils.DateUtils;
import cn.com.ite.eap2.core.struts2.AbstractListAction;
import cn.com.ite.eap2.module.power.login.LoginAction;
import cn.com.ite.hnjtamis.train.domain.MonthPlan;

public class MonthPlanListAction extends AbstractListAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3788584381504108881L;

	/**
	 * 查询结果对象
	 */
	private List<MonthPlan> list = new ArrayList<MonthPlan>();
	/**
	 * 查询条件
	 */
	private String titleTerm;
	private String quarterTerm;
	private String specialityTerm;
	private Integer monthTerm;
	private Integer statusTerm;
	private String deptTerm;

	/**
	 * 查询方法
	 * 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String list() throws Exception {
		list = (List<MonthPlan>) service.queryData("queryHql", this,
				this.getSortMap(), this.getStart(), this.getLimit());
		this.setTotal(service.countData("queryHql", this));
		return "list";
	}

	@SuppressWarnings({ "unchecked" })
	public String all() throws Exception {
		list = (List<MonthPlan>) service.queryData("queryAllHql", this,
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
			MonthPlan plan = (MonthPlan) service.findDataByKey(ids[i],
					MonthPlan.class);
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
		MonthPlan plan = (MonthPlan) service.findDataByKey(this.getId(),
				MonthPlan.class);
		plan.setStatus(statusTerm);
		plan.setAuditTime(DateUtils.convertDateToStr(new Date(),
				"yyyy-MM-dd HH:mm:ss"));
		plan.setAuditerId(LoginAction.getUserSessionInfo().getEmployeeId());
		plan.setAuditerName(LoginAction.getUserSessionInfo().getEmployeeName());
		service.saveOld(plan);
		this.setMsg("审核成功！");
		return "save";
	}

	public List<MonthPlan> getList() {
		return list;
	}

	public void setList(List<MonthPlan> list) {
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

	public Integer getMonthTerm() {
		return monthTerm;
	}

	public void setMonthTerm(Integer monthTerm) {
		this.monthTerm = monthTerm;
	}

	public Integer getStatusTerm() {
		return statusTerm;
	}

	public void setStatusTerm(Integer statusTerm) {
		this.statusTerm = statusTerm;
	}

	public String getDeptTerm() {
		return deptTerm;
	}

	public void setDeptTerm(String deptTerm) {
		this.deptTerm = deptTerm;
	}

}
