package cn.com.ite.hnjtamis.kb.sharedlib;

import java.util.Date;

import org.apache.commons.lang.StringUtils;

import cn.com.ite.eap2.common.utils.DateUtils;
import cn.com.ite.eap2.core.struts2.AbstractFormAction;
import cn.com.ite.eap2.module.power.login.LoginAction;
import cn.com.ite.eap2.module.power.login.UserSession;
import cn.com.ite.hnjtamis.kb.domain.SharedLib;
/**
 * <p>
 * Title cn.com.ite.hnjtamis.kb.sharedlib.SharedLibFormAction
 * </p>
 * <p>
 * Description 共享库FormAction
 * </p>
 * <p>
 * Company ITE
 * </p>
 * <p>
 * Copyright Copyright(c)2014
 * </p>
 * 
 * @author 李奉学
 * @create time 2015-3-30
 * @version 1.0
 * 
 * @modified
 */
public class SharedLibFormAction extends AbstractFormAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8387683644088807836L;

	private SharedLib form;
	
	/**
	 * 查询数据
	 * 
	 * @return
	 * @modified
	 */
	public String find() {
		form = (SharedLib) service.findDataByKey(this.getId(),
				SharedLib.class);
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
			form = (SharedLib) this.jsonToObject(SharedLib.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if ("".equals(form.getId()))
			form.setId(null);
		if (StringUtils.isEmpty(form.getId())) {
			String nowTime = DateUtils.convertDateToStr(new Date(),
					"yyyy-MM-dd HH:mm:ss");
			form.setIsDistributeVersion(0);
			form.setIsDisable(0);
			form.setIsAudited(0);
			form.setIsDel(0);
			form.setSyncStatus(1);
			form.setUploader(session.getEmployeeId());
			form.setUploaderName(session.getEmployeeName());
			form.setUploadTime(nowTime);
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

	public SharedLib getForm() {
		return form;
	}

	public void setForm(SharedLib form) {
		this.form = form;
	}
}
