package cn.com.ite.hnjtamis.mainpage.all;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.ite.eap2.core.service.DefaultServiceImpl;
import cn.com.ite.eap2.domain.baseinfo.SysAffiche;
import cn.com.ite.hnjtamis.kb.domain.Courseware;
import cn.com.ite.hnjtamis.mainpage.domain.ViewPersonProgress;
import cn.com.ite.hnjtamis.talent.domain.TalentRegistration;
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
public class MainPageServiceImpl extends DefaultServiceImpl implements
		MainPageService {
//	private CoursewareService coursewareService;
//	private TalentRegistrationService talentRegistrationService;
	
	
	public List<Courseware> findCoursewarelist(){
		//课件教材库
		///this.queryData(queryName, term, sortMap, pageNo, rowSize)
		List<Courseware> list = this.queryData("queryCourseware", null, null,0,5);
		return list;
	}
	public List<TalentRegistration> findTalentreglist(){
		// 专家库
		List<TalentRegistration> list = this.queryData("queryTalentreg", null, null,0,10);
		return list;
	}
	public List<SysAffiche> findSysaffichelist(){
		// 公告
		List<SysAffiche> list = this.queryData("querySysaffiche", null, null,0,5);
		return list;
	}
	public List<ViewPersonProgress> findPersonprogresslist(){
		Map<String,Object> term = new HashMap<String, Object>();
		//term.put("ORGANID", organid);
		List<ViewPersonProgress> list = this.queryData("querySqlPersonprogress", term, null, ViewPersonProgress.class,0,5);
		
		return list;
	}
	
}
