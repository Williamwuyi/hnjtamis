package cn.com.ite.hnjtamis.mainpage.basic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.ite.eap2.core.hibernate.DefaultDAO;
import cn.com.ite.eap2.core.service.DefaultServiceImpl;
import cn.com.ite.hnjtamis.mainpage.domain.ViewOrganProgress;
import cn.com.ite.hnjtamis.mainpage.domain.ViewTrainPerson;
import cn.com.ite.hnjtamis.mainpage.domain.ViewTranPlan;
/**
 * 
 * <p>Title 岗位达标培训信息系统-个人学习管理模块</p>
 * <p>Description 个人首页展示service实现</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2015</p>
 * @author wangyong
 * @create Apr 15, 2015  10:45:27 AM
 * @version 1.0
 * 
 * @modified records:
 */
public class BasicMainPageServiceImpl extends DefaultServiceImpl implements
		BasicMainPageService {
	private DefaultDAO branchmainpageDao;
	public DefaultDAO getBranchmainpageDao() {
		return branchmainpageDao;
	}

	public void setBranchmainpageDao(DefaultDAO branchmainpageDao) {
		this.branchmainpageDao = branchmainpageDao;
	}

	/**
	 * 按部门统计学习情况
	 */
	public List<ViewTranPlan> findTranplanlist(String organid){
		Map<String,Object> term = new HashMap<String, Object>();
		term.put("ORGANID", organid);
		List list = this.queryData("querySqlTrainplan", term, null, ViewTranPlan.class);
		
		return list;
	}
	
	/**
	 * 月度计划完成情况
	 * @param organid
	 * @return
	 */
	public List<ViewTranPlan> findMonthplanlist(String organid,String monthTerm){
		Map<String,Object> term = new HashMap<String, Object>();
		term.put("ORGANID", organid);
		term.put("monthTerm", monthTerm);
		List list = this.queryData("querySqlDeptMonthplan", term, null, ViewTranPlan.class);
		
		return list;
	}
	
	/**
	 * 统计单位达标完成情况
	 * @param organid
	 * @return
	 */
	public ViewOrganProgress findSubOrganprogresslist(String organid){
		ViewOrganProgress vo = null;
		Map<String,Object> term = new HashMap<String, Object>();
		term.put("ORGANID", organid);
		List list = branchmainpageDao.queryConfigQl("querySqlSuborganprogress", term, null, ViewOrganProgress.class);
		//List list = this.queryData("querySqlSuborganprogress", term, null, ViewOrganProgress.class);
		if(list!=null && list.size()>0){
			vo=(ViewOrganProgress)list.iterator().next();
		}else{
			return new ViewOrganProgress();
		}
		return vo;
	}
	
	/**
	 * 人员培训列表
	 * @param organid
	 * @return
	 */
	public ViewTrainPerson findTrainperson(String organid){
		ViewTrainPerson vo = null;
		Map<String,Object> term = new HashMap<String, Object>();
		term.put("ORGANID", organid);
		List list = this.queryData("querySqlPersonTrainplan", term, null, ViewTrainPerson.class);
		if(list!=null && list.size()>0){
			vo=(ViewTrainPerson)list.iterator().next();
		}else{
			return new ViewTrainPerson();
		}
		return vo;
	}
	
}
