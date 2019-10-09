package cn.com.ite.hnjtamis.train.impl;

import java.util.Date;
import java.util.Iterator;

import org.apache.commons.lang.StringUtils;

import cn.com.ite.eap2.common.utils.DateUtils;
import cn.com.ite.eap2.core.struts2.AbstractFormAction;
import cn.com.ite.eap2.module.power.login.LoginAction;
import cn.com.ite.eap2.module.power.login.UserSession;
import cn.com.ite.hnjtamis.exam.hibernatemap.ThemeBank;
import cn.com.ite.hnjtamis.train.domain.TrainImplement;
import cn.com.ite.hnjtamis.train.domain.TrainImplementCourse;

public class TrainImplementFormAction extends AbstractFormAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7699474616996243609L;
	private TrainImplement form;

	private String themeBankIds;

	/**
	 * 查询数据
	 * 
	 * @return
	 * @modified
	 */
	public String find() {
		form = (TrainImplement) service.findDataByKey(this.getId(),
				TrainImplement.class);
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
			form = (TrainImplement) this.jsonToObject(TrainImplement.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if ("".equals(form.getId()))
			form.setId(null);
		
		if (form.getTrainImplementCourses().size() == 0) {
			throw new Exception("至少需要指定一个课件！");
		}
		if (StringUtils.isEmpty(form.getId())) {
			String nowTime = DateUtils.convertDateToStr(new Date(),
					"yyyy-MM-dd HH:mm:ss");
			form.setIsAudited(0);
			form.setIsDel(0);
			form.setSyncStatus(1);
			form.setCreatedBy(session.getEmployeeCode());
			form.setCreationDate(nowTime);
			form.setLastUpdateDate(nowTime);
			form.setLastUpdatedBy(session.getEmployeeCode());
			form.setOrganId(session.getOrganId());
		}

		Iterator<TrainImplementCourse> iter = form.getTrainImplementCourses()
				.iterator();
		int sortNo = 0;
		while (iter.hasNext()) {
			TrainImplementCourse course = iter.next();
			course.setSortNo(++sortNo);
			course.setTrainImplement(form);
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
	 * 保存题库
	 * 
	 * @return
	 * @throws Exception
	 */
	public String saveThemeBank() throws Exception {
		String[] themeBankId = this.getThemeBankIds().split(",");
		try {
			TrainImplement impl = (TrainImplement) service.findDataByKey(this
					.getId(), TrainImplement.class);
			impl.getThemeBanks().clear();
			for (String bankId : themeBankId) {
				ThemeBank theme = (ThemeBank) service.findDataByKey(bankId,
						ThemeBank.class);
				impl.getThemeBanks().add(theme);
			}
			service.update(impl);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "save";
	}

	public TrainImplement getForm() {
		return form;
	}

	public void setForm(TrainImplement form) {
		this.form = form;
	}

	public String getThemeBankIds() {
		return themeBankIds;
	}

	public void setThemeBankIds(String themeBankIds) {
		this.themeBankIds = themeBankIds;
	}

}
