package cn.com.ite.hnjtamis.mainpage.branch;

import java.util.List;

import cn.com.ite.eap2.core.service.DefaultService;
import cn.com.ite.hnjtamis.mainpage.domain.ViewOrganProgress;
import cn.com.ite.hnjtamis.mainpage.domain.ViewTrainPlanRatio;
import cn.com.ite.hnjtamis.mainpage.domain.ViewTranPlan;
/**
 * 
 * <p>Title 岗位达标培训信息系统-首页管理模块</p>
 * <p>Description 分子公司首页展示service接口定义</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2015</p>
 * @author wangyong
 * @create May 5, 2015 
 * @version 1.0
 * 
 * @modified records:
 */
public interface BranchMainPageService extends DefaultService {

//	
	/**
	 * 查询培训计划
	 */
	public List<ViewTranPlan> findTranplanlist(String organid);
	/**
	 * 统计培训计划完成比率
	 * @param organid
	 * @return
	 */
	public List<ViewTrainPlanRatio> findTranplanratiolist(String organid);
	/**
	 * 统计下级单位达标完成情况
	 * @param organid
	 * @return
	 */
	public List<ViewOrganProgress> findSubOrganprogresslist(String organid);
}
