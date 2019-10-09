package cn.com.ite.hnjtamis.exam.base.exammarkpeopleinfo.service;

import java.text.SimpleDateFormat;
import java.util.*;

import cn.com.ite.eap2.common.utils.StringUtils;
import cn.com.ite.eap2.core.service.DefaultServiceImpl;
import cn.com.ite.eap2.module.power.login.LoginAction;
import cn.com.ite.eap2.module.power.login.UserSession;
import cn.com.ite.hnjtamis.baseinfo.domain.Speciality;
import cn.com.ite.hnjtamis.exam.base.exammarkpeopleinfo.form.ExamCReviewerForm;
import cn.com.ite.hnjtamis.exam.base.exammarkpeopleinfo.form.ExamReviewerForm;
import cn.com.ite.hnjtamis.exam.hibernatemap.Exam;
import cn.com.ite.hnjtamis.exam.hibernatemap.ExamMarkpeople;
import cn.com.ite.hnjtamis.exam.hibernatemap.ExamMarkpeopleInfo;
import cn.com.ite.hnjtamis.exam.hibernatemap.ThemeType;

public class ExamMarkPeopleServiceImpl extends DefaultServiceImpl implements ExamMarkPeopleService{
	public List<String> save(ExamReviewerForm reviewerForm,Exam exam,List<ExamMarkpeople> deletes) throws Exception{
		List<ExamCReviewerForm> reviewerChilds = reviewerForm.getReviewerChilds();
		UserSession usersess = LoginAction.getUserSessionInfo();
		String deleteids = "";
		List<ThemeType> themeTypeList  = this.queryAllDate(ThemeType.class);
		List <ThemeType> zgtthemeTypeList = new ArrayList();
		for(int i=0;i<themeTypeList.size();i++){
			ThemeType themeType = themeTypeList.get(i);
			if(themeType.getIsUse()!=null && themeType.getIsUse().intValue()==10 
					&& themeType.getJudge()!=null && themeType.getJudge().intValue() == 5){
				zgtthemeTypeList.add(themeType);
			}
			
		}
		if(reviewerChilds!=null && reviewerChilds.size()>0){
			List<ExamMarkpeople> saveList = new ArrayList<ExamMarkpeople>();
			for (ExamCReviewerForm examCReviewerForm : reviewerChilds) {
				ExamMarkpeople examPeo = new ExamMarkpeople();
				examPeo.setExamMarkpeopleId(examCReviewerForm.getExamMarkpeopleId());
				if(StringUtils.isEmpty(examPeo.getExamMarkpeopleId())){//新增
					examPeo.setCreatedBy(usersess.getEmployeeName());
					examPeo.setCreatedIdBy(usersess.getEmployeeId());
					examPeo.setCreationDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
				}else{//修改
					examPeo.setLastUpdateDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
					examPeo.setLastUpdatedBy(usersess.getEmployeeName());
					examPeo.setLastUpdatedIdBy(usersess.getEmployeeId());
				}
				examPeo.setExam(exam);
				//ExamMarkpeopleInfo tmpInfo = new ExamMarkpeopleInfo();
				//tmpInfo.setMarkPeopleId(examCReviewerForm.getExamMarkpeopleInfo());
				//examPeo.setMarkPeopleName(examCReviewerForm.getReviewerName());
				//examPeo.setExamMarkpeopleInfo(tmpInfo);
				if(exam.getExamEndTime()!=null){
					examPeo.setStartTime(exam.getExamEndTime());
					examPeo.setEndTime((Integer.parseInt(exam.getExamEndTime().substring(0,4))+1)
							+exam.getExamEndTime().substring(4,exam.getExamEndTime().length()));
				}
				
				//examPeo.setStartTime(reviewerForm.getStartTime());
				//examPeo.setEndTime(reviewerForm.getEndTime());
				examPeo.setIsMain(examCReviewerForm.getIsMain());
				//examPeo.setEmpId(examCReviewerForm.getEmpId());
				
				if(examCReviewerForm.getExamMarkEmp()!=null){
					examPeo.setMarkPeopleName(examCReviewerForm.getExamMarkEmp().getEmployeeName());
					examPeo.setEmpId(examCReviewerForm.getExamMarkEmp().getEmployeeId());
				}
				
				List<ThemeType> t = zgtthemeTypeList;//examCReviewerForm.getThemeTypes();
				if(t!=null && t.size()>0){
					String ids="",names="";
					for (ThemeType themeType : t) {
						ids+=themeType.getThemeTypeId()+",";
						names+=themeType.getThemeTypeName()+",";
					}
					ids = ids.substring(0, ids.length()-1);
					names = names.substring(0, names.length()-1);
					examPeo.setThemeTypeId(ids);
					examPeo.setThemeTypeName(names);
				}
				List<Speciality> s = examCReviewerForm.getSpecialitys();
				if(s!=null && s.size()>0){
					String ids="",names="";
					for (Speciality speciality : s) {
						ids+=speciality.getSpecialityid()+",";
						names+=speciality.getSpecialityname()+",";
					}
					ids = ids.substring(0, ids.length()-1);
					names = names.substring(0, names.length()-1);
					examPeo.setProfessionId(ids);
					examPeo.setProfessionName(names);
				}
				if(!StringUtils.isEmpty(examPeo.getExamMarkpeopleId())){
					deleteids+=examPeo.getExamMarkpeopleId();
				}
				saveList.add(examPeo);
			}
			this.saves(saveList);
		}
		if(deletes!=null && deletes.size()>0){
			List<String> deStr = new ArrayList<String>();
			for (ExamMarkpeople peo : deletes) {
				if(!(deleteids.indexOf(peo.getExamMarkpeopleId())!=-1)){
					deStr.add(peo.getExamMarkpeopleId());
				}
			}
			return deStr;
		}
		
		return null;
	}
}
