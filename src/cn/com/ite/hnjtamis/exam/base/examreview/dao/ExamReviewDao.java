package cn.com.ite.hnjtamis.exam.base.examreview.dao;

import java.util.LinkedList;
import java.util.List;

import cn.com.ite.eap2.core.hibernate.DefaultDAO;
import cn.com.ite.hnjtamis.exam.hibernatemap.ExamTestpaperTheme;


/**
 *
 * <p>Title cn.com.ite.hnjtamis.exam.base.examreview.dao.ExamReviewDao</p>
 * <p>Description </p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2017</p>
 * @create time: 2017年3月13日 下午3:41:50
 * @version 1.0
 * 
 * @modified records:
 */
public interface ExamReviewDao extends DefaultDAO{
	
	/**
	 * 查询试题
	 * @description
	 * @param examId
	 * @param timuids
	 * @param curtimuids
	 * @param state
	 * @param anotherstate
	 * @return
	 * @modified
	 */
	public List<ExamTestpaperTheme> queryExamTestpaperThemeList(String examId,
			LinkedList<String> timuids,LinkedList<String> curtimuids,
			String state,String anotherstate);
	
}
