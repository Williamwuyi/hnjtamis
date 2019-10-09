package cn.com.ite.hnjtamis.personal.rateprogress;

import java.util.ArrayList;
import java.util.List;

import cn.com.ite.eap2.common.utils.StringUtils;
import cn.com.ite.eap2.core.struts2.AbstractFormAction;
import cn.com.ite.eap2.domain.organization.Quarter;
import cn.com.ite.eap2.module.power.login.LoginAction;
import cn.com.ite.eap2.module.power.login.UserSession;
import cn.com.ite.hnjtamis.baseinfo.domain.AbstractDomain;
import cn.com.ite.hnjtamis.personal.domain.PersonalRateProgress;

/**
 * 
 * <p>
 * Title 岗位达标培训信息系统-个人学习管理模块
 * </p>
 * <p>
 * Description 个人岗位达标记录FormAction
 * </p>
 * <p>
 * Company ITE
 * </p>
 * <p>
 * Copyright Copyright(c)2015
 * </p>
 * 
 * @author wangyong
 * @create Apr 9, 2015 10:45:27 AM
 * @version 1.0
 * 
 * @modified records:
 */
public class PersonalRateProgressFormAction extends AbstractFormAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3477961125567014764L;
	private PersonalRateProgress form;
	private String jobId; // 岗位ID
	private String parentJobIds;

	// private JobUnionSpecialityService jobUnionSpecialityService

	public PersonalRateProgress getForm() {
		return form;
	}

	public void setForm(PersonalRateProgress form) {
		this.form = form;
	}

	/**
	 * 查询数据
	 * 
	 * @return
	 * @modified
	 */
	public String find() {
		// PersonalRateProgressService
		// PersonalRateProgressService=(PersonalRateProgressService)service;
		form = (PersonalRateProgress) service.findDataByKey(this.getId(),
				PersonalRateProgress.class);
		// / 添加关联属性
		if (StringUtils.isEmpty(form.getPersonname())) {
			UserSession us = LoginAction.getUserSessionInfo();
			// / 默认取当前登录人员
			form.setPersonname(StringUtils.isEmpty(us.getEmployeeName()) ? us
					.getAccount() : us.getEmployeeName());
			form.setPersoncode(StringUtils.isEmpty(us.getEmployeeId()) ? us
					.getAccount() : us.getEmployeeId());
		}
		// / 当前设置岗位VO

		return "find";
	}

	/**
	 * 保存主对象
	 * 
	 * @return
	 * @throws Exception
	 */
	public String save() throws Exception {
		UserSession us = LoginAction.getUserSessionInfo();
		form = (PersonalRateProgress) this
				.jsonToObject(PersonalRateProgress.class);
		try {
			// 其他属性赋值
			Quarter thisquarter = (Quarter) service.findDataByKey(form
					.getJobscode(), Quarter.class);

			form.setJobscode(us.getQuarterId());// / 岗位名称前添加部门名称标识
			form.setJobsname(thisquarter.getDept().getDeptName() + "->"
					+ thisquarter.getQuarterName());
		} catch (Exception e) {
			//e.printStackTrace();
		}
		if (StringUtils.isEmpty(form.getRateid())) {
			// /新增操作公共字段
			// form.setOrderno(1+service.getFieldMax(PersonalRateProgress.class,
			// "orderno"));
			AbstractDomain.addCommonFieldValue(form);

		} else {
			// 修改操作公共字段
			AbstractDomain.updateCommonFieldValue(form);
		}
		service.save(form);
		// // 设置晋升岗位集合
		return "save";
	}

	public String saveSort() throws Exception {
		List<PersonalRateProgress> saves = new ArrayList<PersonalRateProgress>();
		int index = 1;
		for (String id : this.getSortIds()) {
			PersonalRateProgress dt = (PersonalRateProgress) service
					.findDataByKey(id, PersonalRateProgress.class);
			// dt.setOrderno(index++);
			saves.add(dt);
		}
		service.saves(saves);
		return "save";
	}

	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	public String getParentJobIds() {
		return parentJobIds;
	}

	public void setParentJobIds(String parentJobIds) {
		this.parentJobIds = parentJobIds;
	}
}
