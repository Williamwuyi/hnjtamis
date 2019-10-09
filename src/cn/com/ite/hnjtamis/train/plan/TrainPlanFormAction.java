package cn.com.ite.hnjtamis.train.plan;

import java.util.Date;

import org.apache.commons.lang.StringUtils;

import cn.com.ite.eap2.common.utils.DateUtils;
import cn.com.ite.eap2.core.struts2.AbstractFormAction;
import cn.com.ite.eap2.module.power.login.LoginAction;
import cn.com.ite.eap2.module.power.login.UserSession;
import cn.com.ite.hnjtamis.train.domain.TrainPlan;

public class TrainPlanFormAction extends AbstractFormAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7466847928312638127L;

	private TrainPlan form;

	/**
	 * 查询数据
	 * 
	 * @return
	 * @modified
	 */
	public String find() {
		form = (TrainPlan) service.findDataByKey(this.getId(), TrainPlan.class);
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
			form = (TrainPlan) this.jsonToObject(TrainPlan.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if ("".equals(form.getId()))
			form.setId(null);
		if (StringUtils.isEmpty(form.getId())) {
			String nowTime = DateUtils.convertDateToStr(new Date(),
					"yyyy-MM-dd HH:mm:ss");
			form.setIsAudited(0);
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
		try {
			service.save(form);
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.setMsg("保存成功！");
		return "save";
	}

	public TrainPlan getForm() {
		return form;
	}

	public void setForm(TrainPlan form) {
		this.form = form;
	}
}
