package cn.com.ite.hnjtamis.train.plan;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import cn.com.ite.eap2.common.utils.DateUtils;
import cn.com.ite.eap2.core.struts2.AbstractFormAction;
import cn.com.ite.eap2.module.power.login.LoginAction;
import cn.com.ite.eap2.module.power.login.UserSession;
import cn.com.ite.hnjtamis.train.domain.MonthPlan;
import cn.com.ite.hnjtamis.train.domain.MonthPlanQuarter;

public class MonthPlanFormAction extends AbstractFormAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7466847928312638127L;

	private MonthPlan form;

	/**
	 * 查询数据
	 * 
	 * @return
	 * @modified
	 */
	public String find() {
		form = (MonthPlan) service.findDataByKey(this.getId(), MonthPlan.class);
		return "find";
	}

	/**
	 * 保存主对象
	 * 
	 * @return
	 * @throws Exception
	 */
	public String save() throws Exception {
		UserSession session = LoginAction.getUserSessionInfo();
		try {
			form = (MonthPlan) this.jsonToObject(MonthPlan.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if ("".equals(form.getId()))
			form.setId(null);
		form.setStatus(0);
		if (StringUtils.isEmpty(form.getId())) {
			String nowTime = DateUtils.convertDateToStr(new Date(),
					"yyyy-MM-dd HH:mm:ss");
			form.setIsDel(0);
			form.setSyncStatus(1);
			form.setDistributorId(session.getEmployeeId());
			form.setDistributorName(session.getEmployeeName());
			form.setDistributeTime(nowTime);
			form.setCreatedBy(session.getEmployeeCode());
			form.setCreationDate(nowTime);
			form.setLastUpdateDate(nowTime);
			form.setLastUpdatedBy(session.getEmployeeCode());
			form.setOrganId(session.getOrganId());
		}
		List<MonthPlanQuarter> list = form.getQuarterPlans();
		for (MonthPlanQuarter quarter : list) {
			quarter.setMonthPlan(form);
		}
		try {
			service.save(form);
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.setMsg("保存成功！");
		return "save";
	}

	/**
	 * 计划执行
	 * 
	 * @return
	 * @throws Exception
	 */
	public String execute() throws Exception {
		UserSession session = LoginAction.getUserSessionInfo();
		try {
			form = (MonthPlan) this.jsonToObject(MonthPlan.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			MonthPlan plan = (MonthPlan) service.findDataByKey(this.getId(),
					MonthPlan.class);

			String nowTime = DateUtils.convertDateToStr(new Date(),
					"yyyy-MM-dd HH:mm:ss");
			plan.setLastUpdateDate(nowTime);
			plan.setLastUpdatedBy(session.getEmployeeCode());
			plan.setExeDate(nowTime);
			plan.setIsFinished(form.getIsFinished());
			plan.setExeContent(form.getExeContent());
			service.update(plan);
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.setMsg("保存成功！");
		return "save";
	}

	public MonthPlan getForm() {
		return form;
	}

	public void setForm(MonthPlan form) {
		this.form = form;
	}
}
