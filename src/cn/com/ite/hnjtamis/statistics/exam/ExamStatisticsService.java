package cn.com.ite.hnjtamis.statistics.exam;

import java.util.List;

import cn.com.ite.eap2.core.service.DefaultService;
import cn.com.ite.hnjtamis.statistics.domain.ViewExamStatistics1;
import cn.com.ite.hnjtamis.statistics.domain.ViewExamStatisticsFailList;
import cn.com.ite.hnjtamis.statistics.domain.ViewExamStatisticsSection;
import cn.com.ite.hnjtamis.statistics.domain.ViewExamStatisticsTopThree;
/**
 * <p>Title 岗位达标培训信息系统-考试模块</p>
 * <p>Description 考试结果统计service接口定义</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2015</p>
 * @author wangyong
 * @create Apr 23, 2015  10:56:04 AM
 * @version 1.0
 * 
 * @modified records:
 */
public interface ExamStatisticsService extends DefaultService {
	/**
	 * 考试统计
	 * @param year
	 * @return
	 */
	public List<ViewExamStatistics1> findStatisticsData1(String year);
	
	/**
	 * 前三名统计
	 * @param year
	 * @return
	 */
	public List<ViewExamStatisticsTopThree> findStatisticsTopThree(String year);
	
	/**
	 * 每场考试的不及格人员成绩
	 * @param examId
	 * @return
	 */
	public List<ViewExamStatisticsFailList> findStatisticsFailList(String examId);
	
	/**
	 * 每场考试分数的分段统计
	 * @param examId
	 * @return
	 */
	public List<ViewExamStatisticsSection> findStatisticsSections(String examId);
}
