package cn.com.ite.hnjtamis.kb.coursedistribute;

import java.util.Date;

import org.apache.commons.lang.StringUtils;

import cn.com.ite.eap2.common.utils.DateUtils;
import cn.com.ite.eap2.core.struts2.AbstractFormAction;
import cn.com.ite.eap2.domain.organization.Organ;
import cn.com.ite.eap2.module.power.login.LoginAction;
import cn.com.ite.eap2.module.power.login.UserSession;
import cn.com.ite.hnjtamis.kb.domain.CoursewareDistribute;

/**
 * <p>
 * Title cn.com.ite.hnjtamis.kb.coursedistribute.CoursewareDistributeFormAction
 * </p>
 * <p>
 * Description 课件库分配FormAction
 * </p>
 * <p>
 * Company ITE
 * </p>
 * <p>
 * Copyright Copyright(c)2014
 * </p>
 * 
 * @author 李奉学
 * @create time 2015-3-24
 * @version 1.0
 * 
 * @modified
 */
public class CoursewareDistributeFormAction extends AbstractFormAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8777074863662577059L;

	private CoursewareDistribute form;

	/**
	 * 查询数据
	 * 
	 * @return
	 * @modified
	 */
	public String find() {
		form = (CoursewareDistribute) service.findDataByKey(this.getId(),
				CoursewareDistribute.class);
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
			form = (CoursewareDistribute) this
					.jsonToObject(CoursewareDistribute.class);
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
			form.setCreatedBy(session.getEmployeeCode());
			form.setCreationDate(nowTime);
			form.setLastUpdateDate(nowTime);
			form.setLastUpdatedBy(session.getEmployeeCode());
			form.setOrgan((Organ) service.findDataByKey(session.getOrganId(),
					Organ.class));
		}
		try {
			service.save(form);
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.setMsg("保存成功！");
		return "save";
	}

	public CoursewareDistribute getForm() {
		return form;
	}

	public void setForm(CoursewareDistribute form) {
		this.form = form;
	}
}
