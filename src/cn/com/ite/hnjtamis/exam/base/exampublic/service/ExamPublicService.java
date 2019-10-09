package cn.com.ite.hnjtamis.exam.base.exampublic.service;

import java.util.List;

import cn.com.ite.eap2.core.service.DefaultService;
import cn.com.ite.eap2.domain.organization.Organ;
import cn.com.ite.eap2.module.power.login.UserSession;
import cn.com.ite.hnjtamis.baseinfo.domain.Speciality;
import cn.com.ite.hnjtamis.exam.base.exampublic.form.ExamPublicForm;
import cn.com.ite.hnjtamis.exam.base.theme.form.ThemeStandardQuarterForm;
import cn.com.ite.hnjtamis.exam.hibernatemap.ExamPublic;
import cn.com.ite.hnjtamis.exam.hibernatemap.ThemeBank;

public interface ExamPublicService extends DefaultService{
	public void saveSearchInfo(ExamPublic po,ExamPublicForm form);
	public void deleteSearchInfo(ExamPublic po);
	
	/*
	 * 保存 考试信息对应的 检索关键字
	 */
	public void saveSearchInfoAndExamPublicUser(String examPublicId,ThemeBank themeBank,List<Speciality> specialityList,
			List<ThemeStandardQuarterForm> themeStandardQuarterFormList,List<Organ> organList,UserSession usersess,boolean isPublic);
}
