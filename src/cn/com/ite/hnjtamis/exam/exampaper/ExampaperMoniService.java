package cn.com.ite.hnjtamis.exam.exampaper;


import java.util.List;

import cn.com.ite.eap2.core.service.DefaultService;
import cn.com.ite.hnjtamis.exam.hibernatemap.ExamTestpaper;
import cn.com.ite.hnjtamis.exam.hibernatemap.ThemeType;

/**
 *
 * <p>Title cn.com.ite.hnjtamis.exam.exampaper.ExampaperMoniService</p>
 * <p>Description 模拟试卷处理</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2015</p>
 * @author 朱健
 * @create time: 2015年5月6日 上午9:35:42
 * @version 1.0
 * 
 * @modified records:
 */
public interface ExampaperMoniService extends DefaultService{
	
	
	
	/**
	 *
	 * @author zhujian
	 * @description 根据题库获取题型的数目
	 * @param themeBankId
	 * @return
	 * @modified
	 */
	public List<ThemeType> getThemeTypeInBank(String themeBankId);

	/**
	 *
	 * @author zhujian
	 * @description 根据考生的试卷ID清除考生的考试答案
	 * @param examTestpaperId
	 * @modified
	 */
	public void saveAndCleanExamPaperById(String examTestpaperId);
	
	
	/**
	 * 
	 * @author zhujian
	 * @description 清除考生的考试答案
	 * @param examTestpaper
	 * @modified
	 */
	public void saveAndCleanExamPaper(ExamTestpaper examTestpaper);
	
	
	/**
	 *
	 * @author zhujian
	 * @description 根据提供给其它接口的userTestpaperId，清除最后一份考试的考生答案
	 * @param userTestpaperId  提供给其它接口的userTestpaperId
	 * @modified
	 */
	public void saveAndCleanExamPaperByUserTestpaperId(String userTestpaperId);
			

	
	
}
