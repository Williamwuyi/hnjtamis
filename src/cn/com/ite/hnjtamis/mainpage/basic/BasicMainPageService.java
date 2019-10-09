package cn.com.ite.hnjtamis.mainpage.basic;

import java.util.List;

import cn.com.ite.eap2.core.service.DefaultService;
import cn.com.ite.hnjtamis.mainpage.domain.ViewOrganProgress;
import cn.com.ite.hnjtamis.mainpage.domain.ViewTrainPerson;
import cn.com.ite.hnjtamis.mainpage.domain.ViewTranPlan;
/**
 * 
 * <p>Title 岗位达标培训信息系统-首页管理模块</p>
 * <p>Description 首页service接口定义
 * 包括分子公司、基层单位进入后的首页展示。主要显示内容：培训情况、达标情况
 * </p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2015</p>
 * @author wangyong
 * @create 2015.5.5  10:45:27 AM
 * @version 1.0
 * 
 * @modified records:
 */
public interface BasicMainPageService extends DefaultService {

//	
	/**
	 * 按部门统计学习情况
	 */
	public List<ViewTranPlan> findTranplanlist(String organid);
	
	/**
	 * 月度计划完成情况
	 * @param organid
	 * @return
	 */
	public List<ViewTranPlan> findMonthplanlist(String organid,String monthTerm);
	
	/**
	 * 统计单位达标完成情况
	 * @param organid
	 * @return
	 */
	public ViewOrganProgress findSubOrganprogresslist(String organid);
	
	/**
	 * 人员培训列表
	 * @param organid
	 * @return
	 */
	public ViewTrainPerson findTrainperson(String organid);
}
