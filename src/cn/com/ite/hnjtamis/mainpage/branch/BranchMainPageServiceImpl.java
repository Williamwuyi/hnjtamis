package cn.com.ite.hnjtamis.mainpage.branch;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.ite.eap2.common.utils.DateUtils;
import cn.com.ite.eap2.common.utils.NumericUtils;
import cn.com.ite.eap2.core.service.DefaultServiceImpl;
import cn.com.ite.eap2.core.spring.SpringContextUtil;
import cn.com.ite.hnjtamis.exam.exampaper.ExampaperDao;
import cn.com.ite.hnjtamis.exam.exampaper.form.ExamDeptPassForm;
import cn.com.ite.hnjtamis.mainpage.domain.ViewOrganProgress;
import cn.com.ite.hnjtamis.mainpage.domain.ViewTrainPlanRatio;
import cn.com.ite.hnjtamis.mainpage.domain.ViewTranPlan;
/**
 * 
 * <p>Title 岗位达标培训信息系统-首页管理模块</p>
 * <p>Description 分子公司首页展示service实现</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2015</p>
 * @author wangyong
 * @create May 5, 2015 
 * @version 1.0
 * 
 * @modified records:
 */
public class BranchMainPageServiceImpl extends DefaultServiceImpl implements
		BranchMainPageService {
//	private PersonalRateProgressService personalrateprogressServer; // 个人达标情况
//	private TrainImplementService trainImplementService; /// 岗位安排课程
//	private CoursewareService coursewareService; /// 课件
//	private ExamPublicService examPublicServer; // 信息发布（发布的考试信息）
//	private ExamPublicUserService examPublicUserServer; // 考生 信息（考生报名后信息与考试结果）
//	
//	  
//	
	/**
	 * 查询培训计划
	 */
	public List<ViewTranPlan> findTranplanlist(String organid){
		//Map<String,Object> term = new HashMap<String, Object>();
		//term.put("ORGANID", organid);
		//List list = this.queryData("querySqlTrainplan", term, null, ViewTranPlan.class);
		
		ExampaperDao exampaperDao = (ExampaperDao)SpringContextUtil.getBean("exampaperDao");
		List<ViewTranPlan> list = new ArrayList<ViewTranPlan>();
		String nowday = DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd");
		List<ExamDeptPassForm> examUserList = exampaperDao.getExamTjxxByUser( organid, null,nowday,nowday,"10");
		for(int i=0;i<examUserList.size();i++){
			ExamDeptPassForm examDeptPassForm = (ExamDeptPassForm)examUserList.get(i);
			if(examDeptPassForm.getParentDeptId()==null){
				ViewTranPlan viewTranPlan = new ViewTranPlan();
				viewTranPlan.setDeptname(examDeptPassForm.getDeptName());
				viewTranPlan.setPlannums(new Integer(examDeptPassForm.getPassStateCount()));
				viewTranPlan.setFactnums(new Integer(examDeptPassForm.getXxrsCount()));
				viewTranPlan.setCompletenums(new Integer(examDeptPassForm.getPassCount()));
				viewTranPlan.setCompleteratio(viewTranPlan.getPlannums()!=0?
						NumericUtils.round((viewTranPlan.getCompletenums()*1.0)/(viewTranPlan.getPlannums()*1.0)*100.0,2):0.0d);
				list.add(viewTranPlan);
			}
		}
		return list;
	}
	/**
	 * 统计培训计划完成比率
	 * @param organid
	 * @return
	 */
	public List<ViewTrainPlanRatio> findTranplanratiolist(String organid){
		Map<String,Object> term = new HashMap<String, Object>();
		term.put("ORGANID", organid);
		//List list = this.queryData("querySqlTrainplanratio", term, null, ViewTrainPlanRatio.class);
		List list = this.queryData("querySqlTrainplan", term, null, ViewTrainPlanRatio.class);
		return list;
	}
	/**
	 * 统计下级单位达标完成情况
	 * @param organid
	 * @return
	 */
	public List<ViewOrganProgress> findSubOrganprogresslist(String organid){
		//Map<String,Object> term = new HashMap<String, Object>();
		//term.put("ORGANID", organid);
		//List list = this.queryData("querySqlSuborganprogress", term, null, ViewOrganProgress.class);
		
		
		ExampaperDao exampaperDao = (ExampaperDao)SpringContextUtil.getBean("exampaperDao");
		List<ViewOrganProgress> list = new ArrayList<ViewOrganProgress>();
		String nowday = DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd");
		List<ExamDeptPassForm> examUserList = exampaperDao.getExamTjxxByOrgan(organid, null,nowday,nowday,"10");
		for(int i=0;i<examUserList.size();i++){
			ExamDeptPassForm examDeptPassForm = (ExamDeptPassForm)examUserList.get(i);
			if(examDeptPassForm.getParentDeptId()==null){
				ViewOrganProgress viewOrganProgress = new ViewOrganProgress();
				viewOrganProgress.setOrganid(examDeptPassForm.getOrganId());
				viewOrganProgress.setOrganname(examDeptPassForm.getOrganName());
				viewOrganProgress.setTotalnums(new Integer(examDeptPassForm.getPassStateCount()));
				//viewOrganProgress.setFactnums(new Integer(examDeptPassForm.getXxrsCount()));
				viewOrganProgress.setReachnums(new Integer(examDeptPassForm.getPassCount()));
				viewOrganProgress.setReachratio(viewOrganProgress.getTotalnums()!=0?
						NumericUtils.round((viewOrganProgress.getReachnums()*1.0)/(viewOrganProgress.getTotalnums()*1.0)*100.0,2):0.0d);
				list.add(viewOrganProgress);
			}
		}
		
		return list;
	}
}
