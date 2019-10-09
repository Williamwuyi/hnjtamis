package cn.com.ite.hnjtamis.exam.invigilate.log;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import cn.com.ite.eap2.common.utils.DateUtils;
import cn.com.ite.eap2.core.struts2.AbstractFormAction;
import cn.com.ite.eap2.module.power.login.LoginAction;
import cn.com.ite.eap2.module.power.login.UserSession;
import cn.com.ite.hnjtamis.exam.hibernatemap.Exam;
import cn.com.ite.hnjtamis.exam.invigilate.domain.InvigilateLog;

public class InvigilateLogFormAction extends AbstractFormAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7618375196417484302L;

	private InvigilateLog form;

	private String examIdTerm;
	private String userIdTerm;

	/**
	 * 查询数据
	 * 
	 * @return
	 * @modified
	 */
	public String find() throws Exception {
		form = (InvigilateLog) service.findDataByKey(this.getId(),
				InvigilateLog.class);
		return "find";
	}

	/**
	 * 查询考试科目的监考日志
	 * 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String findByExam() throws Exception {
		try {
			List<InvigilateLog> list = service.queryData("queryByExamIdHql",
					this, null);
			if (list != null && list.size() > 0)
				form = list.get(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
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
			form = (InvigilateLog) this.jsonToObject(InvigilateLog.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if ("".equals(form.getId()))
			form.setId(null);
		form.setExam((Exam)service.findDataByKey(this.getExamIdTerm(), Exam.class));
		if (StringUtils.isEmpty(form.getId())) {
			String nowTime = DateUtils.convertDateToStr(new Date(),
					"yyyy-MM-dd HH:mm:ss");
			form.setUserId(session.getEmployeeId());
			form.setUserName(session.getEmployeeName());
			form.setStatus(0);
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

	public InvigilateLog getForm() {
		return form;
	}

	public void setForm(InvigilateLog form) {
		this.form = form;
	}

	public String getExamIdTerm() {
		return examIdTerm;
	}

	public void setExamIdTerm(String examIdTerm) {
		this.examIdTerm = examIdTerm;
	}

	public String getUserIdTerm() {
		return userIdTerm;
	}

	public void setUserIdTerm(String userIdTerm) {
		this.userIdTerm = userIdTerm;
	}

}
