package cn.com.ite.hnjtamis.personal.log;

import cn.com.ite.eap2.common.utils.StringUtils;
import cn.com.ite.eap2.core.struts2.AbstractFormAction;
import cn.com.ite.eap2.module.power.login.LoginAction;
import cn.com.ite.eap2.module.power.login.UserSession;
import cn.com.ite.hnjtamis.baseinfo.domain.AbstractDomain;
import cn.com.ite.hnjtamis.personal.domain.PersonalLog;
/**
 * 
 * <p>Title 岗位达标培训信息系统-个人学习模块</p>
 * <p>Description 个人学习日志FormAction</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2015</p>
 * @author wangyong
 * @create Apr 3, 2015  10:45:27 AM
 * @version 1.0
 * 
 * @modified records:
 */
public class PersonalLogFormAction extends AbstractFormAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3477961125567014764L;
	private PersonalLog form;
	private String jobId;	// 岗位ID
	private String parentJobIds;
	
	//private JobUnionSpecialityService jobUnionSpecialityService

	
	
	/**
	 * 查询数据
	 * @return
	 * @modified
	 */
	public String find(){
//		PersonalLogService PersonalLogService=(PersonalLogService)service;
		form = (PersonalLog)service.findDataByKey(this.getId(), PersonalLog.class);
		/// 添加关联属性
		if(StringUtils.isEmpty(form.getPersonname())){
			UserSession us = LoginAction.getUserSessionInfo();
			/// 默认取当前登录人员
			form.setPersonname(StringUtils.isEmpty(us.getEmployeeName())?us.getAccount():us.getEmployeeName());
			form.setPersoncode(StringUtils.isEmpty(us.getEmployeeId())?us.getAccount():us.getEmployeeId());
		}
		/// 当前设置岗位VO
		
		
		return "find";
	}
	/**
	 * 保存主对象
	 * @return
	 * @throws Exception
	 */
	public String save() throws Exception{
		form = (PersonalLog)this.jsonToObject(PersonalLog.class);
		// 其他属性赋值
		if(StringUtils.isEmpty(form.getPlogid())){
			///新增操作公共字段
//			form.setOrderno(1+service.getFieldMax(PersonalLog.class, 
//					"orderno"));
			AbstractDomain.addCommonFieldValue(form);
			
		}else{
			// 修改操作公共字段
			AbstractDomain.updateCommonFieldValue(form);
		}
		service.save(form);
		//// 设置晋升岗位集合
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
	public PersonalLog getForm() {
		return form;
	}
	public void setForm(PersonalLog form) {
		this.form = form;
	}
}
