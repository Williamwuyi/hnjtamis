package cn.com.ite.hnjtamis.exam.study;


import java.util.List;
import java.util.Map;

import cn.com.ite.eap2.core.service.DefaultService;
import cn.com.ite.hnjtamis.exam.hibernatemap.StudyTestpaperAnswerkey;
import cn.com.ite.hnjtamis.exam.hibernatemap.StudyTestpaperTheme;
import cn.com.ite.hnjtamis.exam.hibernatemap.StudyUserAnswerkey;

/**
 *
 * <p>Title cn.com.ite.hnjtamis.exam.study.StudyThemeService</p>
 * <p>Description 在线学习试题</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2017</p>
 * @create time: 2017年9月1日 下午2:21:25
 * @version 1.0
 * 
 * @modified records:
 */
public interface StudyThemeService extends DefaultService{
	
	/**
	 * 查询试卷的试题(统计值)
	 * @description
	 * @param studyTestpaperId
	 * @return
	 * @modified
	 */
	public Object[] getStudyTestpaperThemeCount(String studyTestpaperId,String employeeId);


	/**
	 * 查询试卷的试题
	 * @description
	 * @param studyTestpaperId
	 * @return
	 * @modified
	 */
	public List<StudyTestpaperTheme> getStudyTestpaperThemeList(String studyTestpaperId,Integer startIndex,Integer endIndex);
	
	/**
	 * 查询考生答案
	 * @description
	 * @param studyTestpaperId
	 * @param employeeId
	 * @return
	 * @modified
	 */
	public List<StudyUserAnswerkey> getUserAnswerkeyList(String studyTestpaperId,String employeeId);
	
	/**
	 * 添加数据库中没有题库的试卷
	 * @description
	 * @param relationType
	 * @param employeeId
	 * @param employeeName
	 * @modified
	 */
	public void addStudyTestpaper(String relationType,
			String employeeId,String employeeName);
	
	/**
	 * 获取或添加学习试卷
	 * @description
	 * @param relationId
	 * @param relationType
	 * @param employeeId
	 * @param employeeName
	 * @param examTitle
	 * @return
	 * @throws Exception
	 * @modified
	 */
	public String addOrGetExamStudyTestpaper(String relationId,String relationType,String employeeId,
			String employeeName,String examTitle)throws Exception;
	
	
	/**
	 * 试卷添加试题
	 * @description
	 * @param relationId
	 * @param relationType
	 * @param employeeId
	 * @param employeeName
	 * @throws Exception
	 * @modified
	 */
	public void addThemeInStudyTestpaper(String study_testpaper_id,String relationId,String relationType,
			String employeeId,String employeeName)throws Exception;
	
	
	/**
	 * 根据试题Id获取答案
	 * @description
	 * @param themeIds
	 * @return
	 * @modified
	 */
	public Map<String,List<StudyTestpaperAnswerkey>> getAnswerkeyByStThemeIds(String studyTestpaperThemeIds);
	
	/**
	 * 获取考生的试题显示答案
	 * @author 朱健
	 * @param examTestpaperId
	 * @return
	 * @modified
	 */
	public Map<String,List<StudyTestpaperAnswerkey>> getStudyTestpaperAnswerkeyByTestpaperId(String studyTestpaperId);
	
	
	/**
	 * 获取考生的试题显示答案
	 * @description
	 * @param studyTestpaperThemelist
	 * @return
	 * @modified
	 */
	public Map<String,List<StudyTestpaperAnswerkey>> getStudyTestpaperAnswerkeyByThemelist(List<StudyTestpaperTheme> studyTestpaperThemelist);
	
	/**
	 * 更新到答案历史表，清理答案表的内容
	 * @description
	 * @param studyTestpaperId
	 * @param employeeId
	 * @modified
	 */
	public void updateAnsHis(String studyTestpaperId,String employeeId);
	
	/**
	 * 获取原试题的人员信息
	 * @description
	 * @return
	 * @modified
	 */
	public List<String> getEmployeeIdInExamUserTestpaper(String relation_type);
	
	
	/**
	 * 根据旧的数据
	 * @description
	 * @modified
	 */
	public void updateUserAnsInOld(String employeeId,String relation_type);
}
