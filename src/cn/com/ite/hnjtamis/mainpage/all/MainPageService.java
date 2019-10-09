package cn.com.ite.hnjtamis.mainpage.all;

import java.util.List;

import cn.com.ite.eap2.core.service.DefaultService;
import cn.com.ite.eap2.domain.baseinfo.SysAffiche;
import cn.com.ite.hnjtamis.kb.domain.Courseware;
import cn.com.ite.hnjtamis.mainpage.domain.ViewPersonProgress;
import cn.com.ite.hnjtamis.talent.domain.TalentRegistration;
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
public interface MainPageService extends DefaultService {

//	
	public List<Courseware> findCoursewarelist();//课件教材库
	public List<TalentRegistration> findTalentreglist();// 专家库
	public List<SysAffiche> findSysaffichelist(); // 公告
	public List<ViewPersonProgress> findPersonprogresslist();/// 个人达标记录
}
