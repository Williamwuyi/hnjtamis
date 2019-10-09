package cn.com.ite.hnjtamis.statistics.exam;

import java.util.ArrayList;
import java.util.List;

import cn.com.ite.eap2.common.utils.DateUtils;
import cn.com.ite.eap2.common.utils.StringUtils;
import cn.com.ite.eap2.core.struts2.AbstractListAction;
import cn.com.ite.hnjtamis.statistics.domain.ViewExamStatistics1;
import cn.com.ite.hnjtamis.statistics.domain.ViewExamStatisticsFailList;
import cn.com.ite.hnjtamis.statistics.domain.ViewExamStatisticsSection;
import cn.com.ite.hnjtamis.statistics.domain.ViewExamStatisticsTopThree;
/**
 * <p>Title 岗位达标培训信息系统-考试模块</p>
 * <p>Description 考试结果统计ListAction</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2015</p>
 * @author wangyong
 * @create Apr 23, 2015  10:56:04 AM
 * @version 1.0
 * 
 * @modified records:
 */
public class ExamStatisticsListAction extends AbstractListAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8061559691203248709L;
	/**
	 * 查询结果
	 */
	private List<ViewExamStatistics1> list = new ArrayList<ViewExamStatistics1>();
	private List<ViewExamStatisticsTopThree> topthreelist=new ArrayList<ViewExamStatisticsTopThree>();
	private List<ViewExamStatisticsFailList> faillist = new ArrayList<ViewExamStatisticsFailList>();
	private List<ViewExamStatisticsSection> sectionslist = new ArrayList<ViewExamStatisticsSection>();
	// 查询条件
	
	
	private String yearTerm = "";
	private String examIdTerm="";
	
	public List<ViewExamStatisticsTopThree> getTopthreelist() {
		return topthreelist;
	}

	public void setTopthreelist(List<ViewExamStatisticsTopThree> topthreelist) {
		this.topthreelist = topthreelist;
	}

	public List<ViewExamStatisticsFailList> getFaillist() {
		return faillist;
	}

	public void setFaillist(List<ViewExamStatisticsFailList> faillist) {
		this.faillist = faillist;
	}

	public String getExamIdTerm() {
		return examIdTerm;
	}

	public void setExamIdTerm(String examIdTerm) {
		this.examIdTerm = examIdTerm;
	}

	public String list()throws Exception{
		ExamStatisticsService mservice=(ExamStatisticsService)service;
//		UserSession us = LoginAction.getUserSessionInfo();
		
		list =mservice.findStatisticsData1(yearTerm);
		
//		if(!StringUtils.isEmpty(this.getValidStr()))
//			this.setValid(this.getValidStr().equals("1"));
		//list = (List<ViewExamStatistics1>)service.queryData("queryHql", this,null,this.getStart(),this.getLimit());
		//把线性结构转成树形结构
//		baseSpecialities = service.childObjectHandler(baseSpecialities, "bstid", "parentspeciltype", 
//				"baseSpecialities",new String[]{},null,this.getFilterIds(),"orderno",null);
		//// 个人岗位达标记录
		
		///this.setTotal(service.countData("queryHql", this));
		return "list";
	}
	
	public String topthreelist() throws Exception{
		ExamStatisticsService mservice=(ExamStatisticsService)service;
		if(StringUtils.isEmpty(yearTerm)){
			yearTerm = String.valueOf(DateUtils.getCurrYear());
		}
		//// 按考试科目 分组，统计前三名
		topthreelist =mservice.findStatisticsTopThree(yearTerm);
		if(topthreelist!=null && topthreelist.size()>0){
//			String examId = topthreelist.iterator().next().getExamid();
			// 根据考试科目，统计该场考试的不及格人数
//			failist = mservice.findStatisticsFailList(examId);
//			sectionlist =mservice.findStatisticsSections(examId);
		}
		return "topthreelist";
	}
	
	public String faillist() throws Exception{
		ExamStatisticsService mservice=(ExamStatisticsService)service;
		String examId = this.getExamIdTerm();
		// 根据考试科目，统计该场考试的不及格人数
		faillist = mservice.findStatisticsFailList(examId);
		return "faillist";
	}
	
	public String sectionslist() throws Exception{
		ExamStatisticsService mservice=(ExamStatisticsService)service;
		String examId = this.getExamIdTerm();
		// 按分数段统计
		sectionslist =mservice.findStatisticsSections(examId);
		return "sectionslist";
	}
	
	/**
	 * 删除
	 * @return
	 * @modified
	 */
	public String delete() throws Exception{
		// 
		///service.findDataByKey(key, type)
		service.deleteByKeys(this.getId().split(","), ViewExamStatistics1.class);
		this.setMsg("信息删除成功！");
		return "delete";
	}
	
	 
	public List<ViewExamStatistics1> getList() {
		return list;
	}
	public void setList(List<ViewExamStatistics1> list) {
		this.list = list;
	}
	public String getYearTerm() {
		return yearTerm;
	}
	public void setYearTerm(String yearTerm) {
		this.yearTerm = yearTerm;
	}

	public List<ViewExamStatisticsSection> getSectionslist() {
		return sectionslist;
	}

	public void setSectionslist(List<ViewExamStatisticsSection> sectionslist) {
		this.sectionslist = sectionslist;
	}
	
	 
}
