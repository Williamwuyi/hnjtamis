package cn.com.ite.hnjtamis.exam.base.exampublic.service;

import java.util.List;

import cn.com.ite.eap2.domain.organization.Organ;
import cn.com.ite.eap2.module.power.login.UserSession;
import cn.com.ite.hnjtamis.baseinfo.domain.Speciality;
import cn.com.ite.hnjtamis.exam.base.theme.form.ThemeStandardQuarterForm;
import cn.com.ite.hnjtamis.exam.hibernatemap.ThemeBank;

/**
 * 异步初始化考试发布信息中的考生
 * <p>Title cn.com.ite.hnjtamis.exam.base.exampublic.service.ExamPublicThreadService</p>
 * <p>Description </p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2016</p>
 * @create time: 2016年12月12日 下午2:06:28
 * @version 1.0
 * 
 * @modified records:
 */
public interface ExamPublicThreadService{
	
	/**
	 * 添加处理信息
	 * @description
	 * @param exam
	 * @param request
	 * @param usersess
	 * @modified
	 */
	public void addExamPublic(String examPublicId,ThemeBank themeBank,List<Speciality> specialityList,
			List<ThemeStandardQuarterForm> themeStandardQuarterFormList,List<Organ> organList,UserSession usersess,boolean isPublic);
}
