package cn.com.ite.hnjtamis.exam.base.themetype;

import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;

import cn.com.ite.eap2.common.utils.StringUtils;
import cn.com.ite.eap2.core.struts2.AbstractFormAction;
import cn.com.ite.eap2.core.struts2.ServletContent;
import cn.com.ite.eap2.module.power.login.LoginAction;
import cn.com.ite.eap2.module.power.login.UserSession;
import cn.com.ite.hnjtamis.exam.hibernatemap.ThemeType;
/*
 * 提醒管理维护 - form
 */
public class ThemeTypeFormAction extends AbstractFormAction implements ServletRequestAware{
	private static final long serialVersionUID = -4958597684520307786L;
	private HttpServletRequest request;
	
	private ThemeType form;
	/*
	 * 修改
	 */
	public String find(){
		form = (ThemeType) service.findDataByKey(this.getId(), ThemeType.class);
		return "find";
	}
	/*
	 * 保存
	 */
	public String save() throws Exception{
		try {
			UserSession usersess = LoginAction.getUserSessionInfo();
			form = (ThemeType) this.jsonToObject(ThemeType.class);
			if(StringUtils.isEmpty(form.getThemeTypeId())){
				form.setCreatedBy(usersess.getEmployeeName());
				form.setCreatedIdBy(usersess.getEmployeeId());
				form.setCreationDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
				//设置排序号
				form.setSortNum(1+service.getFieldMax(ThemeType.class, "sortNum"));
			}else{
				form.setLastUpdateDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
				form.setLastUpdatedBy(usersess.getEmployeeName());
				form.setLastUpdatedIdBy(usersess.getEmployeeId());
			}
			form.setOrganId(usersess.getOrganId());
			form.setOrganName(usersess.getOrganName());
			service.save(form);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		this.setMsg("题型保存成功！");
		
		return "save";
	}
	/*
	 * 保存排序
	 */
	public String saveSort() throws Exception{
		List saves = new ArrayList();
		int index = 1;
		try {
			for(String id:this.getSortIds()){
				ThemeType tt = (ThemeType) service.findDataByKey(id, ThemeType.class);
				tt.setSortNum(index++);
				saves.add(tt);
			}
			service.saves(saves);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "save";
	}
	
	
	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
		
	}

	public ThemeType getForm() {
		return form;
	}
	public void setForm(ThemeType form) {
		this.form = form;
	}
	
}
