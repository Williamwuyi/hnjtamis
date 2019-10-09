package cn.com.ite.hnjtamis.exam.base.exampublic.service;

import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.commons.beanutils.PropertyUtilsBean;

import cn.com.ite.eap2.common.utils.DateUtils;
import cn.com.ite.eap2.common.utils.StringUtils;
import cn.com.ite.eap2.core.service.DefaultServiceImpl;
import cn.com.ite.eap2.domain.baseinfo.SysAffiche;
import cn.com.ite.eap2.domain.baseinfo.SysAfficheIncepter;
import cn.com.ite.eap2.domain.organization.Dept;
import cn.com.ite.eap2.domain.organization.Employee;
import cn.com.ite.eap2.domain.organization.Organ;
import cn.com.ite.eap2.domain.organization.Quarter;
import cn.com.ite.eap2.exception.EapException;
import cn.com.ite.eap2.module.power.login.LoginAction;
import cn.com.ite.eap2.module.power.login.UserSession;
import cn.com.ite.hnjtamis.baseinfo.domain.Speciality;
import cn.com.ite.hnjtamis.exam.base.exampublic.dao.ExamPublicDao;
import cn.com.ite.hnjtamis.exam.base.exampublic.form.ExamPublicForm;
import cn.com.ite.hnjtamis.exam.base.theme.form.ThemePostForm;
import cn.com.ite.hnjtamis.exam.base.theme.form.ThemeStandardQuarterForm;
import cn.com.ite.hnjtamis.exam.hibernatemap.ExamPublic;
import cn.com.ite.hnjtamis.exam.hibernatemap.ExamPublicSearchkey;
import cn.com.ite.hnjtamis.exam.hibernatemap.ExamPublicUser;
import cn.com.ite.hnjtamis.exam.hibernatemap.ExamUserTestpaper;
import cn.com.ite.hnjtamis.exam.hibernatemap.ThemeBank;
import cn.com.ite.hnjtamis.jobstandard.domain.JobsStandardQuarter;
import cn.com.ite.hnjtamis.jobstandard.domain.JobsUnionStandard;
import cn.com.ite.hnjtamis.jobstandard.domain.QuarterStandard;

public class ExamPublicServiceImpl extends DefaultServiceImpl implements ExamPublicService{
	/*
	 * 根据岗位id 查询考试信息
	 */
	public List<ExamPublic> queryExamNotic(String postId){
		List<ExamPublic> resultList = new ArrayList<ExamPublic>();
		if(StringUtils.isEmpty(postId)){
			return resultList;
		}
		try {
			Map term = new HashMap();
			term.put("", postId);
			resultList = this.queryData("", term, null, ExamPublic.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultList;
	}
	/*
	 * 根据岗位id集 查询考试信息
	 */
	public List<ExamPublic> queryExamNotic(List<String> postIds){
		List<ExamPublic> resultList = new ArrayList<ExamPublic>();
		if(postIds==null || postIds.size()==0){
			return resultList;
		}
		try {
			Map term = new HashMap();
			term.put("", postIds);
			resultList = this.queryData("", term, null, ExamPublic.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultList;
	}
	/*
	 * 删除考试信息对应的 检索关键字
	 */
	public void deleteSearchInfo(ExamPublic po){
		this.getDao().refreshObject(po);
		Map term = new HashMap();
		term.put("publicId", po.getPublicId());
		List<ExamPublicSearchkey> deletes = this.queryData("deleteHql", term, null, ExamPublicSearchkey.class);
		System.out.println("delete size is:"+deletes.size());
		try {
			this.deletes(deletes);
		} catch (EapException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/*
	 * 保存 考试信息对应的 检索关键字
	 */
	public void saveSearchInfoAndExamPublicUser(String examPublicId,ThemeBank themeBank,List<Speciality> specialityList,
			List<ThemeStandardQuarterForm> themeStandardQuarterFormList,List<Organ> organList,UserSession usersess,boolean isPublic){
		ExamPublic po = (ExamPublic)this.getDao().findEntityBykey(ExamPublic.class, examPublicId);
		List<QuarterStandard> quarterStandardList = this.queryAllDate(QuarterStandard.class);
		Map<String,List<QuarterStandard>> quarterStandardMap = new HashMap<String,List<QuarterStandard>>();
		for(int i=0;i<quarterStandardList.size();i++){
			QuarterStandard quarterStandard = quarterStandardList.get(i);
			List list = quarterStandardMap.get(quarterStandard.getQuarterCode());//发现编码有重复的，因此用list
			if(list == null){ list = new ArrayList();}
			list.add(quarterStandard);
			quarterStandardMap.put(quarterStandard.getQuarterCode(),list);
		}
		
		List<ExamPublicSearchkey> savaPos = new ArrayList<ExamPublicSearchkey>();
		List<Speciality> s = specialityList;
		if(s!=null && s.size()>0){
			for (Speciality speciality : s) {
				ExamPublicSearchkey tmp = new ExamPublicSearchkey();
				tmp.setExamPublic(po);
				tmp.setProfessionId(speciality.getSpecialityid());
				tmp.setProfessionName(speciality.getSpecialityname());
				if(themeBank!=null){
					tmp.setThemeBankId(themeBank.getThemeBankId());
					tmp.setThemeBankName(themeBank.getThemeBankName());
				}
				tmp.setCreatedBy(usersess.getEmployeeName());
				tmp.setCreatedIdBy(usersess.getEmployeeId());
				tmp.setCreationDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
				tmp.setOrganId(usersess.getOrganId());
				tmp.setOrganName(usersess.getOrganName());
				savaPos.add(tmp);
			}
		}
		
		Map<String,ThemeStandardQuarterForm> standQuarterCodesMap = new HashMap<String,ThemeStandardQuarterForm>();
		int standardQuarterIndex = 0;
		for(int i=0;i<themeStandardQuarterFormList.size();i++){
			ThemeStandardQuarterForm jobsStandardQuarter = (ThemeStandardQuarterForm)themeStandardQuarterFormList.get(i);
			String quarterTrainCode = jobsStandardQuarter.getQuarterTrainCode();
			if(quarterTrainCode.indexOf("@")!=-1){
				quarterTrainCode = quarterTrainCode.split("@")[1];
			}
			List<QuarterStandard> quarterStandardlist = quarterStandardMap.get(quarterTrainCode);
			if(quarterStandardlist!=null && quarterStandardlist.size()>0){
				for(int k=0;k<quarterStandardlist.size();k++){
					QuarterStandard quarterStandard = quarterStandardlist.get(k);
					ExamPublicSearchkey tmp = new ExamPublicSearchkey();
					tmp.setExamPublic(po);
					tmp.setSortNum(standardQuarterIndex++);
					tmp.setQuarterTrainCode(quarterTrainCode);
					tmp.setQuarterTrainName(quarterStandard.getQuarterName());
					tmp.setQuarterTrainId(quarterStandard.getQuarterId());
					tmp.setDeptName(quarterStandard.getDeptName());
					tmp.setDeptId(quarterStandard.getDeptId());
					tmp.setDcType(quarterStandard.getDcType());
					tmp.setCreatedBy(usersess.getEmployeeName());
					tmp.setCreatedIdBy(usersess.getEmployeeId());
					tmp.setCreationDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
					tmp.setOrganId(usersess.getOrganId());
					tmp.setOrganName(usersess.getOrganName());
					savaPos.add(tmp);
					standQuarterCodesMap.put(quarterTrainCode, jobsStandardQuarter);
				}
				
			}
		}
		
		List<Quarter>  quarterList= this.queryAllDate(Quarter.class);
		List<Organ> organs = organList;
		Map<String,String> organIds = new HashMap<String,String>();
		for(int i=0;i<organs.size();i++){
			String organId = organs.get(i).getOrganId();
			organIds.put(organId, organId);
		}
		Map gwMap =new HashMap();
		for(int i=0;i<quarterList.size();i++){
			Quarter quarter=(Quarter)quarterList.get(i);
			ThemeStandardQuarterForm jobsStandardQuarter = standQuarterCodesMap.get(quarter.getQuarterTrainCode());
			Dept dept = quarter.getDept();
			Organ organ = dept!=null ? dept.getOrgan() : null;
			if(organ!=null && organIds.get(organ.getOrganId())!=null 
					&& quarter.getQuarterTrainCode()!=null && jobsStandardQuarter!=null){
				ExamPublicSearchkey tmp = new ExamPublicSearchkey();
				tmp.setExamPublic(po);
				tmp.setSortNum(standardQuarterIndex++);
				tmp.setPostId(quarter.getQuarterId());
				tmp.setPostName(quarter.getQuarterName());
				tmp.setDeptName(dept!=null ? dept.getDeptName() : null);
				tmp.setDeptId(dept!=null ? dept.getDeptId() : null);
				tmp.setOrganId(organ!=null ? organ.getOrganId() : null);
				tmp.setOrganName(organ!=null ? organ.getOrganName() : null);
				tmp.setCreatedBy(usersess.getEmployeeName());
				tmp.setCreatedIdBy(usersess.getEmployeeId());
				tmp.setCreationDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
				gwMap.put(quarter.getQuarterId(), quarter.getQuarterId());
				savaPos.add(tmp);
			}
			
		}
		
		/*List<ThemePostForm> t = form.getThemePostFormList();
		if(t!=null && t.size()>0){
			for (ThemePostForm themePostForm : t) {
				ExamPublicSearchkey tmp = new ExamPublicSearchkey();
				tmp.setExamPublic(po);
				tmp.setPostId(themePostForm.getPostId());
				tmp.setPostName(themePostForm.getPostName());
				if(form.getThemeBank()!=null){
					tmp.setThemeBankId(form.getThemeBank().getThemeBankId());
					tmp.setThemeBankName(form.getThemeBank().getThemeBankName());
				}
				tmp.setCreatedBy(usersess.getEmployeeName());
				tmp.setCreatedIdBy(usersess.getEmployeeId());
				tmp.setCreationDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
				tmp.setOrganId(usersess.getOrganId());
				tmp.setOrganName(usersess.getOrganName());
				savaPos.add(tmp);
			}
		}*/
		
		if(savaPos.size()>0){
			this.getDao().saveBatchEntity(savaPos);
			if("20".equals(po.getState())||isPublic){
				List<ExamPublicUser> examUserList = this.saveExamPublicUser(po, gwMap,usersess);
				this.saveSysAffiche(po, organList, usersess, examUserList);
				
			}
		}
	}
	
	/**
	 * 保存考试通知
	 * @description
	 * @param po
	 * @param organList
	 * @param usersess
	 * @modified
	 */
	private void saveSysAffiche(ExamPublic po,List<Organ> organList,UserSession usersess ,List<ExamPublicUser> examUserList){
		if("0".equals(po.getIsReg())){
			//自动发出通知
			SysAffiche sysAffiche = new SysAffiche();
			sysAffiche.setSender(usersess.getEmployeeName());//发送者
			sysAffiche.setSendTime(new java.sql.Date(System.currentTimeMillis()));//发送时间
			sysAffiche.setTitle("关于“"+po.getExamTitle()+"”考试的通知");
			String[] a1 = po.getPlanExamTime().split(" ");
			String[] aa1 = a1[0].trim().split("-");
			String[] aa2 = a1[1].trim().split(":");
			String content = po.getExamTitle();
			content += "&nbsp;&nbsp;&nbsp;&nbsp;考试计划定于"+aa1[0]+"年"+aa1[1]+"月"+aa1[2]+"日"+aa2[0]+"时"+aa2[1]+"分举行，请组织考试的人员及时审核自主报名的考生信息，如未出现在已报名考生名单中，而必须参加或想参与本场考试的人员，可自主报名，如有问题请与组织考试的人员联系。";
			sysAffiche.setContent(content);
			
			sysAffiche.setDeadline(DateUtils.getDaysBetween(new Date(),DateUtils.convertStrToDate(po.getPlanExamTime().substring(0,10), "yyyy-MM-dd"))+1);
			sysAffiche.setRelationId(po.getPublicId());
			sysAffiche.setRelationType("exmpublic");
			this.getDao().addEntity(sysAffiche);
			
			//添加发送对象
			List<SysAfficheIncepter> addList = new ArrayList<SysAfficheIncepter>();
			for(int i=0;i<organList.size();i++){
				String organId = organList.get(i).getOrganId();
				SysAfficheIncepter sysAfficheIncepter = new SysAfficheIncepter();
				sysAfficheIncepter.setIncepterId(organId);
				sysAfficheIncepter.setIncepterName(organList.get(i).getOrganName());
				sysAfficheIncepter.setIncepterType(1);
				sysAfficheIncepter.setSortNum(new Integer(i));
				sysAfficheIncepter.setSysAffiche(sysAffiche);
				addList.add(sysAfficheIncepter);
			}
			if(addList!=null && addList.size()>0)
				this.getDao().saveBatchEntity(addList);
		}else{
			//自动发出通知
			SysAffiche sysAffiche = new SysAffiche();
			sysAffiche.setSender(usersess.getEmployeeName());//发送者
			sysAffiche.setSendTime(new java.sql.Date(System.currentTimeMillis()));//发送时间
			sysAffiche.setTitle("关于“"+po.getExamTitle()+"”考试的通知");
			String[] a1 = po.getPlanExamTime().split(" ");
			String[] aa1 = a1[0].trim().split("-");
			String[] aa2 = a1[1].trim().split(":");
			String content = po.getExamTitle();
			content += "&nbsp;&nbsp;&nbsp;&nbsp;考试计划定于"+aa1[0]+"年"+aa1[1]+"月"+aa1[2]+"日"+(Integer.parseInt(aa2[0])<12? "上午" : "下午")+"举行，请组织考试的人员及时审核自主报名的考生信息，如未出现在已报名考生名单中，而必须参加或想参与本场考试的人员，可自主报名，如有问题请与组织考试的人员联系。";
			sysAffiche.setContent(content);
			
			sysAffiche.setDeadline(DateUtils.getDaysBetween(new Date(),DateUtils.convertStrToDate(po.getPlanExamTime().substring(0,10), "yyyy-MM-dd"))+1);
			sysAffiche.setRelationId(po.getPublicId());
			sysAffiche.setRelationType("exmpublic");
			this.getDao().addEntity(sysAffiche);
			
			//添加发送对象
			List<SysAfficheIncepter> addList = new ArrayList<SysAfficheIncepter>();
			for(int i=0;i<examUserList.size();i++){
				ExamPublicUser examPublicUser = examUserList.get(i);
				SysAfficheIncepter sysAfficheIncepter = new SysAfficheIncepter();
				sysAfficheIncepter.setIncepterId(examPublicUser.getEmployeeId());
				sysAfficheIncepter.setIncepterName(examPublicUser.getEmployeeName());
				sysAfficheIncepter.setIncepterType(4);
				sysAfficheIncepter.setSortNum(new Integer(i));
				sysAfficheIncepter.setSysAffiche(sysAffiche);
				addList.add(sysAfficheIncepter);
			}
			if(addList!=null && addList.size()>0)
				this.getDao().saveBatchEntity(addList);
		}
	}
	
	/*
	 * 保存 考试信息对应的 检索关键字
	 */
	public void saveSearchInfo(ExamPublic po,ExamPublicForm form){
		List<QuarterStandard> quarterStandardList = this.queryAllDate(QuarterStandard.class);
		Map<String,List<QuarterStandard>> quarterStandardMap = new HashMap<String,List<QuarterStandard>>();
		for(int i=0;i<quarterStandardList.size();i++){
			QuarterStandard quarterStandard = quarterStandardList.get(i);
			List list = quarterStandardMap.get(quarterStandard.getQuarterCode());//发现编码有重复的，因此用list
			if(list == null){ list = new ArrayList();}
			list.add(quarterStandard);
			quarterStandardMap.put(quarterStandard.getQuarterCode(),list);
		}
		
		this.getDao().refreshObject(po);
		List<ExamPublicSearchkey> savaPos = new ArrayList<ExamPublicSearchkey>();
		List<Speciality> s = form.getSpecialitys();
		UserSession usersess = LoginAction.getUserSessionInfo();
		if(s!=null && s.size()>0){
			for (Speciality speciality : s) {
				ExamPublicSearchkey tmp = new ExamPublicSearchkey();
				tmp.setExamPublic(po);
				tmp.setProfessionId(speciality.getSpecialityid());
				tmp.setProfessionName(speciality.getSpecialityname());
				if(form.getThemeBank()!=null){
					tmp.setThemeBankId(form.getThemeBank().getThemeBankId());
					tmp.setThemeBankName(form.getThemeBank().getThemeBankName());
				}
				tmp.setCreatedBy(usersess.getEmployeeName());
				tmp.setCreatedIdBy(usersess.getEmployeeId());
				tmp.setCreationDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
				tmp.setOrganId(usersess.getOrganId());
				tmp.setOrganName(usersess.getOrganName());
				savaPos.add(tmp);
			}
		}
		
		Map<String,ThemeStandardQuarterForm> standQuarterCodesMap = new HashMap<String,ThemeStandardQuarterForm>();
		int standardQuarterIndex = 0;
		for(int i=0;i<form.getStandardQuarterList().size();i++){
			ThemeStandardQuarterForm jobsStandardQuarter = (ThemeStandardQuarterForm)form.getStandardQuarterList().get(i);
			String quarterTrainCode = jobsStandardQuarter.getQuarterTrainCode();
			if(quarterTrainCode.indexOf("@")!=-1){
				quarterTrainCode = quarterTrainCode.split("@")[1];
			}
			List<QuarterStandard> quarterStandardlist = quarterStandardMap.get(quarterTrainCode);
			if(quarterStandardlist!=null && quarterStandardlist.size()>0){
				for(int k=0;k<quarterStandardlist.size();k++){
					QuarterStandard quarterStandard = quarterStandardlist.get(k);
					ExamPublicSearchkey tmp = new ExamPublicSearchkey();
					tmp.setExamPublic(po);
					tmp.setSortNum(standardQuarterIndex++);
					tmp.setQuarterTrainCode(quarterTrainCode);
					tmp.setQuarterTrainName(quarterStandard.getQuarterName());
					tmp.setQuarterTrainId(quarterStandard.getQuarterId());
					tmp.setDeptName(quarterStandard.getDeptName());
					tmp.setDeptId(quarterStandard.getDeptId());
					tmp.setDcType(quarterStandard.getDcType());
					tmp.setCreatedBy(usersess.getEmployeeName());
					tmp.setCreatedIdBy(usersess.getEmployeeId());
					tmp.setCreationDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
					tmp.setOrganId(usersess.getOrganId());
					tmp.setOrganName(usersess.getOrganName());
					savaPos.add(tmp);
					standQuarterCodesMap.put(quarterTrainCode, jobsStandardQuarter);
				}
				
			}
		}
		
		List<Quarter>  quarterList= this.queryAllDate(Quarter.class);
		List<Organ> organs = form.getOrgans();
		Map<String,String> organIds = new HashMap<String,String>();
		for(int i=0;i<organs.size();i++){
			String organId = organs.get(i).getOrganId();
			organIds.put(organId, organId);
		}
		Map gwMap =new HashMap();
		for(int i=0;i<quarterList.size();i++){
			Quarter quarter=(Quarter)quarterList.get(i);
			ThemeStandardQuarterForm jobsStandardQuarter = standQuarterCodesMap.get(quarter.getQuarterTrainCode());
			Dept dept = quarter.getDept();
			Organ organ = dept!=null ? dept.getOrgan() : null;
			if(organ!=null && organIds.get(organ.getOrganId())!=null 
					&& quarter.getQuarterTrainCode()!=null && jobsStandardQuarter!=null){
				ExamPublicSearchkey tmp = new ExamPublicSearchkey();
				tmp.setExamPublic(po);
				tmp.setSortNum(standardQuarterIndex++);
				tmp.setPostId(quarter.getQuarterId());
				tmp.setPostName(quarter.getQuarterName());
				tmp.setDeptName(dept!=null ? dept.getDeptName() : null);
				tmp.setDeptId(dept!=null ? dept.getDeptId() : null);
				tmp.setOrganId(organ!=null ? organ.getOrganId() : null);
				tmp.setOrganName(organ!=null ? organ.getOrganName() : null);
				tmp.setCreatedBy(usersess.getEmployeeName());
				tmp.setCreatedIdBy(usersess.getEmployeeId());
				tmp.setCreationDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
				gwMap.put(quarter.getQuarterId(), quarter.getQuarterId());
				savaPos.add(tmp);
			}
			
		}
		
		/*List<ThemePostForm> t = form.getThemePostFormList();
		if(t!=null && t.size()>0){
			for (ThemePostForm themePostForm : t) {
				ExamPublicSearchkey tmp = new ExamPublicSearchkey();
				tmp.setExamPublic(po);
				tmp.setPostId(themePostForm.getPostId());
				tmp.setPostName(themePostForm.getPostName());
				if(form.getThemeBank()!=null){
					tmp.setThemeBankId(form.getThemeBank().getThemeBankId());
					tmp.setThemeBankName(form.getThemeBank().getThemeBankName());
				}
				tmp.setCreatedBy(usersess.getEmployeeName());
				tmp.setCreatedIdBy(usersess.getEmployeeId());
				tmp.setCreationDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
				tmp.setOrganId(usersess.getOrganId());
				tmp.setOrganName(usersess.getOrganName());
				savaPos.add(tmp);
			}
		}*/
		
		if(savaPos.size()>0){
			this.getDao().saveBatchEntity(savaPos);
			if("20".equals(po.getState())){
				this.saveExamPublicUser(po, gwMap,usersess);
			}
		}
	}
	
	
	/**
	 * 自动发布涉及岗位的考生
	 * @author 朱健
	 * @param po
	 * @modified
	 */
	private List<ExamPublicUser> saveExamPublicUser(ExamPublic ep,Map<String,String> gwMap,UserSession usersess){
		ExamPublicDao examPublicDao = (ExamPublicDao)this.getDao();
		Map term = new HashMap();
		term.put("publicId", ep.getPublicId());
		List<ExamPublicUser> examPublicUserList = this.queryData("queryExamPublicUserByPublicId", term, null);
		Map<String,ExamPublicUser> examPublicUserMap = new HashMap<String,ExamPublicUser>();
		for(int i=0;i<examPublicUserList.size();i++){
			ExamPublicUser examPublicUser = examPublicUserList.get(i);
			examPublicUserMap.put(examPublicUser.getEmployeeId(), examPublicUser);
		}
		
		//if(examPublicUserList==null || examPublicUserList.size() == 0){
		    //UserSession usersess = LoginAction.getUserSessionInfo();
		    List<Employee> emplist = new ArrayList();
		   /* if("20".equals(ep.getReachUserJoin()) || ep.getExamProperty().intValue() != 10){
		    	emplist = this.queryAllDate(Employee.class);
		    }else{
			    if(ep.getExamProperty().intValue() == 10 && !gwMap.isEmpty()){
			    	emplist = examPublicDao.getExamEmployeeInQuarter(ep.getScoreStartTime(), ep.getScoreEndTime(), gwMap);
			    }else if(ep.getExamProperty().intValue() == 10){
			    	emplist = examPublicDao.getExamEmployee(ep.getScoreStartTime(), ep.getScoreEndTime());
			    }else{
			    	emplist = this.queryAllDate(Employee.class);
			    }
		    }*/
		    emplist = examPublicDao.getExamEmployeeInQuarter(ep.getScoreStartTime(), ep.getScoreEndTime(), gwMap);
			
			List<ExamPublicUser> savaPos = new ArrayList<ExamPublicUser>();
			for(int i=0;i<emplist.size();i++){
				Employee employee = (Employee)emplist.get(i);
				Quarter quarter = employee.getQuarter();
				if(quarter==null){
					continue;
				}
				Dept dept = quarter.getDept();
				Organ organ = dept!=null ? dept.getOrgan() : null;
				if(quarter!=null && dept!=null && organ!=null 
						&& gwMap.get(quarter.getQuarterId())!=null 
						&& employee.isValidation()){
					ExamPublicUser po = examPublicUserMap.get(employee.getEmployeeId());
					boolean isUpdate = true;
					if(po==null){
						po = new ExamPublicUser();
						po.setCreatedBy(usersess.getEmployeeName());
						po.setCreatedIdBy(usersess.getEmployeeId());
						po.setCreationDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
						isUpdate = false;
						po.setSyncFlag("1");
					}else{
						po.setLastUpdatedBy(usersess.getEmployeeName());
						po.setLastUpdatedIdBy(usersess.getEmployeeId());
						po.setLastUpdateDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
						po.setSyncFlag("2");
					}
							
					po.setUserOrganId(organ.getOrganId());
					po.setUserOrganName(organ.getOrganName());
					po.setUserDeptId(dept!=null ? dept.getDeptId() : null);
					po.setUserDeptName(dept!=null ? dept.getDeptName(): null);
					po.setPostId(quarter.getQuarterId());
					po.setPostName(quarter.getQuarterName());
					po.setEmployeeId(employee.getEmployeeId());
					po.setEmployeeName(employee.getEmployeeName());
					po.setExamPublic(ep);
					
					po.setIsExp("0");
					po.setUserName(po.getEmployeeName());
					po.setOrganId(po.getOrganId());
					po.setOrganName(po.getOrganName());
					po.setState("20");
					po.setIsDel("0");
					po.setBmType("10");//报名方式 10-系统要求 20-自主报名
					if(employee.getSex() == 0){
						po.setUserSex("1");//['1','男'],['2','女']
		    		}else if(employee.getSex() == 1){
		    			po.setUserSex("2");//['1','男'],['2','女']
		    		}
		    		
		    		if(employee.getBirthday()!=null){
		    			po.setUserBirthday(DateUtils.convertDateToStr(employee.getBirthday(),"yyyy-MM-dd"));//生日
		    		}
		    		po.setIdNumber(employee.getIdentityCard()!=null?employee.getIdentityCard().trim():employee.getIdentityCard());//身份证号
		    		po.setUserNation(employee.getNationality());////民族
		    		if(employee.getMovePhone()!=null){
		    			po.setUserPhone(employee.getMovePhone());//手机号
		    		}else{
		    			po.setUserPhone(employee.getOfficePhone());//手机号
		    		}
		    		po.setUserAddr(employee.getAddress());//地址
		    		
					savaPos.add(po);
					if(isUpdate){
						examPublicUserMap.remove(employee.getEmployeeId());
					}
				}
			}
			
			
			if(!examPublicUserMap.isEmpty()){
				Iterator its = examPublicUserMap.keySet().iterator();
				while(its.hasNext()){
					String id = (String)its.next();
					ExamPublicUser po = examPublicUserMap.get(id);
					po.setIsDel("1");
					po.setSyncFlag("3");
				}
			}
			this.getDao().saveBatchEntity(savaPos);
			
			return savaPos;
			
		//}
	}
}
