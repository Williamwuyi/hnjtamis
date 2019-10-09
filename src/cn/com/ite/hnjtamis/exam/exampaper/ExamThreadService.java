package cn.com.ite.hnjtamis.exam.exampaper;

import javax.servlet.http.HttpServletRequest;

import cn.com.ite.eap2.module.power.login.UserSession;




/**
 * 试题初始化线程处理
 * <p>Title cn.com.ite.hnjtamis.exam.exampaper.ExamThreadService</p>
 * <p>Description </p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2016</p>
 * @create time: 2016年11月14日 上午9:31:50
 * @version 1.0
 * 
 * @modified records:
 */
public interface ExamThreadService{
	
	/**
	 * 添加考试处理信息
	 * @description
	 * @param exam
	 * @param request
	 * @param usersess
	 * @modified
	 */
	public void addExam(String examId,HttpServletRequest request,UserSession usersess);
	
}
