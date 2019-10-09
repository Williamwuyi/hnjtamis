package cn.com.ite.eap2.module.funres.app;

import cn.com.ite.eap2.common.utils.StringUtils;
import cn.com.ite.eap2.core.struts2.AbstractFormAction;
import cn.com.ite.eap2.domain.funres.AppSystem;

/**
 * <p>Title cn.com.ite.eap2.module.funres.app.AppFormAction</p>
 * <p>Description 系统FormAction</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2014</p>
 * @author 宋文科
 * @create time: 2014-6-24 下午01:14:34
 * @version 2.0
 * 
 * @modified records:
 */
public class AppFormAction extends AbstractFormAction{
	private static final long serialVersionUID = 5927931667526549415L;
	/**
	 * 表单数据
	 */
	private AppSystem form;
	
	/**
	 * 查询结果
	 * @return
	 * @modified
	 */
	public String find(){
		form = (AppSystem)service.findDataByKey(this.getId(), AppSystem.class);
		return "find";
	}
	/**
	 * 保存
	 * @return
	 * @throws Exception
	 * @modified
	 */
	public String save() throws Exception{
		form = (AppSystem)this.jsonToObject(AppSystem.class);
		if(StringUtils.isEmpty(form.getAppId()))
		form.setOrderNo(1+service.getFieldMax(AppSystem.class, "orderNo"));
		service.save(form);
		return "save";
	}
	
	public AppSystem getForm() {
		return form;
	}
	public void setForm(AppSystem form) {
		this.form = form;
	}
}