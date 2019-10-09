package cn.com.ite.hnjtamis.train.online;

import java.util.Date;

import org.apache.commons.lang.StringUtils;

import cn.com.ite.eap2.common.utils.DateUtils;
import cn.com.ite.eap2.core.struts2.AbstractFormAction;
import cn.com.ite.eap2.module.power.login.LoginAction;
import cn.com.ite.eap2.module.power.login.UserSession;
import cn.com.ite.hnjtamis.train.domain.TrainOnline;
import cn.com.ite.hnjtamis.train.impl.TrainImplementService;

/**
 * <p>
 * Title cn.com.ite.hnjtamis.train.online.TrainOnlineFormAction
 * </p>
 * <p>
 * Description 在线培训FormAction
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
public class TrainOnlineFormAction extends AbstractFormAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1838372706390701327L;
	@SuppressWarnings("unused")
	private TrainImplementService trainImplementService;

	private TrainOnline form;

	/**
	 * 查询数据
	 * 
	 * @return
	 * @modified
	 */
	public String find() throws Exception {
		form = (TrainOnline) service.findDataByKey(this.getId(), TrainOnline.class);
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
			form = (TrainOnline) this.jsonToObject(TrainOnline.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if ("".equals(form.getId()))
			form.setId(null);
		if (StringUtils.isEmpty(form.getId())) {
			String nowTime = DateUtils.convertDateToStr(new Date(),
					"yyyy-MM-dd HH:mm:ss");
			form.setIsDel(0);
			form.setSyncStatus(1);
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

	public TrainOnline getForm() {
		return form;
	}

	public void setForm(TrainOnline form) {
		this.form = form;
	}

	public void setTrainImplementService(TrainImplementService trainImplementService) {
		this.trainImplementService = trainImplementService;
	}
}
