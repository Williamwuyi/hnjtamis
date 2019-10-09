package cn.com.ite.hnjtamis.exam.base.exammarkpeopleinfo;

import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.struts2.interceptor.ServletRequestAware;

import cn.com.ite.eap2.common.utils.CharsetSwitchUtil;
import cn.com.ite.eap2.common.utils.StringUtils;
import cn.com.ite.eap2.core.struts2.AbstractFormAction;
import cn.com.ite.eap2.domain.organization.Dept;
import cn.com.ite.eap2.domain.organization.Employee;
import cn.com.ite.eap2.domain.organization.Organ;
import cn.com.ite.eap2.domain.organization.Quarter;
import cn.com.ite.eap2.module.power.login.LoginAction;
import cn.com.ite.eap2.module.power.login.UserSession;
import cn.com.ite.hnjtamis.baseinfo.domain.Speciality;
import cn.com.ite.hnjtamis.exam.base.exammarkpeopleinfo.form.ExamCReviewerForm;
import cn.com.ite.hnjtamis.exam.base.exammarkpeopleinfo.form.ExamMarkpeopleInfoForm;
import cn.com.ite.hnjtamis.exam.base.exammarkpeopleinfo.form.ExamReviewerForm;
import cn.com.ite.hnjtamis.exam.base.exammarkpeopleinfo.service.ExamMarkPeopleService;
import cn.com.ite.hnjtamis.exam.exampaper.ExampaperService;
import cn.com.ite.hnjtamis.exam.hibernatemap.Exam;
import cn.com.ite.hnjtamis.exam.hibernatemap.ExamMarkpeople;
import cn.com.ite.hnjtamis.exam.hibernatemap.ExamMarkpeopleInfo;
import cn.com.ite.hnjtamis.exam.hibernatemap.ThemeType;
/*
 * 阅卷老师信息维护 - form
 */
public class ExamMarkPeopleInfoFormAction extends AbstractFormAction implements ServletRequestAware{
	private static final long serialVersionUID = -5387505467311485230L;
	private HttpServletRequest request;
	private ExamMarkPeopleService examMarkPeopleServer;
	private ExampaperService exampaperService;
	
	private ExamMarkpeopleInfoForm form;//阅卷人基本信息form
	
	private ExamReviewerForm reviewerForm; //考试安排阅卷人 维护
	/*
	 * 考试安排中 查询阅卷人信息
	 */
	public String findReviewer(){
		try {
			String examId = request.getParameter("id");
			String examName = request.getParameter("examName");
			if(!StringUtils.isEmpty(examId)){
				Map term = new HashMap();
				term.put("examId", examId);
				//查询阅卷人 信息
				List<ExamMarkpeople> reviewerList = service.queryData("queryReviewer", term, null, ExamMarkpeople.class);  
				//组装成form 
				reviewerForm = new ExamReviewerForm();
				if(reviewerList!=null && reviewerList.size()>0){
					
					List<ExamCReviewerForm> reviewerChilds = new ArrayList<ExamCReviewerForm>();
					for (ExamMarkpeople examMarkpeople : reviewerList) {
						ExamCReviewerForm t = new ExamCReviewerForm();
						t.setExamMarkpeopleId(examMarkpeople.getExamMarkpeopleId());
						t.setExamMarkpeopleInfo(examMarkpeople.getEmpId());
						Employee examMarkEmp = new Employee();
						examMarkEmp.setEmployeeId(examMarkpeople.getEmpId());
						examMarkEmp.setEmployeeName(examMarkpeople.getMarkPeopleName());
						t.setExamMarkEmp(examMarkEmp);
						t.setIsMain(examMarkpeople.getIsMain());
						//设置题型
						if(!StringUtils.isEmpty(examMarkpeople.getThemeTypeId())){
							List<ThemeType> themeTypes = new ArrayList<ThemeType>();
							String[] ids = examMarkpeople.getThemeTypeId().split(",");
							String[] names = examMarkpeople.getThemeTypeName().split(",");
							for(int i = 0;i<ids.length;i++){
								ThemeType tmpType = new ThemeType();
								tmpType.setThemeTypeId(ids[i]);
								tmpType.setThemeTypeName(names[i]);
								themeTypes.add(tmpType);
							}
							t.setThemeTypes(themeTypes);
						}
						//设置专业
						if(!StringUtils.isEmpty(examMarkpeople.getProfessionId())){
							List<Speciality> specialitys = new ArrayList<Speciality>();//专业
							String[] ids = examMarkpeople.getProfessionId().split(",");
							String[] names = examMarkpeople.getProfessionName().split(",");
							for(int i = 0;i<ids.length;i++){
								Speciality tmpS = new Speciality();
								tmpS.setSpecialityid(ids[i]);
								tmpS.setSpecialityname(names[i]);
								specialitys.add(tmpS);
							}
							t.setSpecialitys(specialitys);
						}
						reviewerChilds.add(t);
					}
					//reviewerForm.setExam(reviewerList.get(0).getExam());
					reviewerForm.setExamId(reviewerList.get(0).getExam().getExamId());
					reviewerForm.setExamName(reviewerList.get(0).getExam().getExamName());
					reviewerForm.setStartTime(reviewerList.get(0).getStartTime());
					reviewerForm.setEndTime(reviewerList.get(0).getEndTime());
					reviewerForm.setReviewerChilds(reviewerChilds);
				}else{
					reviewerForm.setExamId(examId);
					examName = StringUtils.decode(examName);
					reviewerForm.setExamName(examName);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "findReviewer";
	}
	/*
	 * 修改 阅卷人基本信息
	 */
	public String find(){
		try {
			Organ o = new Organ();
			Dept d = new Dept();
			Quarter q = new Quarter();
			Employee e = new Employee();
			
			form = new ExamMarkpeopleInfoForm();
			PropertyUtilsBean pU = new PropertyUtilsBean();
			ExamMarkpeopleInfo po= (ExamMarkpeopleInfo) service.findDataByKey(this.getId(), ExamMarkpeopleInfo.class);
			pU.copyProperties(form, po);
			o.setOrganId(po.getOrganId());
			o.setOrganName(po.getOrganName());
			d.setDeptId(po.getUserDeptId());
			d.setDeptName(po.getUserDeptName());
			q.setQuarterId(po.getPostId());
			q.setQuarterName(po.getPostName());
			e.setEmployeeId(po.getEmpId());
			e.setEmployeeName(po.getMarkPeopleName());
			form.setOrgan(o);
			form.setDept(d);
			form.setQuarter(q);
			form.setEmployee(e);
			
			if(!StringUtils.isEmpty(po.getProfessionId()) && !StringUtils.isEmpty(po.getProfessionName())){
				String[] ids = po.getProfessionId().split(",");
				String[] names = po.getProfessionName().split(",");
				
				List<Speciality> l = new ArrayList<Speciality>();
				for(int i=0;i<ids.length;i++){
					Speciality t = new Speciality();
					t.setSpecialityid(ids[i]);
					t.setSpecialityname(names[i]);
					l.add(t);
				}
				form.setSpecialitys(l);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "find";
	}
	/*
	 * 考试安排中 保存阅卷人
	 */
	public String saveReviewer() throws Exception{
		try {
			reviewerForm = (ExamReviewerForm) this.jsonToObject(ExamReviewerForm.class);
			//分拆form 进行保存
			Exam exam =  (Exam) exampaperService.findDataByKey(reviewerForm.getExamId(), Exam.class);
			List<ExamMarkpeople> deletes = exam.getExamMarkpeoples();//已有的阅卷人
			List<String> deList =  examMarkPeopleServer.save(reviewerForm,exam,deletes);
			if(deList!=null && deList.size()>0){
				//examMarkPeopleServer.deletes(deList);
				List params = new ArrayList();
				Map term = new HashMap();
				term.put("deList", deList);
				params.add(term);
				examMarkPeopleServer.excuteQl("deleteFromExamMarkpeople", params);;
			}
			this.setMsg("保存阅卷人成功");
		} catch (Exception e) {
			this.setMsg("保存阅卷人失败");
			e.printStackTrace();
		}
		return "save";
	}
	/*
	 * 保存 阅卷人基本信息
	 */
	public String save() throws Exception{
		UserSession usersess = LoginAction.getUserSessionInfo();
		try {
			form = (ExamMarkpeopleInfoForm) this.jsonToObject(ExamMarkpeopleInfoForm.class);
			if(StringUtils.isEmpty(form.getMarkPeopleId())){
				form.setCreatedBy(usersess.getEmployeeName());
				form.setCreatedIdBy(usersess.getEmployeeId());
				form.setCreationDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			}else{
				form.setLastUpdateDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
				form.setLastUpdatedBy(usersess.getEmployeeName());
				form.setLastUpdatedIdBy(usersess.getEmployeeId());
			}
			form.setOrganId(usersess.getOrganId());
			form.setOrganName(usersess.getOrganName());
			
			ExamMarkpeopleInfo po = new ExamMarkpeopleInfo();
			PropertyUtilsBean pU = new PropertyUtilsBean();
			pU.copyProperties(po, form);
			po.setOrganId(form.getOrgan().getOrganId());
			po.setOrganName(form.getOrgan().getOrganName());
			po.setUserDeptId(form.getDept().getDeptId());
			po.setUserDeptName(form.getDept().getDeptName());
			po.setPostId(form.getQuarter().getQuarterId());
			po.setPostName(form.getQuarter().getQuarterName());
			po.setEmpId(form.getEmployee().getEmployeeId());
			po.setMarkPeopleName(form.getEmployee().getEmployeeName());
			
			List<Speciality> l = form.getSpecialitys();
			String ids="";
			String names="";
			for (Speciality speciality : l) {
				ids+=speciality.getSpecialityid()+",";
				names+=speciality.getSpecialityname()+",";
			}
			if(!StringUtils.isEmpty(ids)){
				ids = ids.substring(0, ids.length()-1);
			}
			if(!StringUtils.isEmpty(names)){
				names = names.substring(0, names.length()-1);
			}
			po.setProfessionId(ids);
			po.setProfessionName(names);
			
			service.save(po);
			this.setMsg("阅卷老师信息保存成功");
		} catch (Exception e) {
			this.setMsg("阅卷老师信息保存失败");
			e.printStackTrace();
		}
		return "save";
	}
	
	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public ExamMarkpeopleInfoForm getForm() {
		return form;
	}

	public void setForm(ExamMarkpeopleInfoForm form) {
		this.form = form;
	}
	public ExamReviewerForm getReviewerForm() {
		return reviewerForm;
	}
	public void setReviewerForm(ExamReviewerForm reviewerForm) {
		this.reviewerForm = reviewerForm;
	}
	public ExamMarkPeopleService getExamMarkPeopleServer() {
		return examMarkPeopleServer;
	}
	public void setExamMarkPeopleServer(ExamMarkPeopleService examMarkPeopleServer) {
		this.examMarkPeopleServer = examMarkPeopleServer;
	}
	public ExampaperService getExampaperService() {
		return exampaperService;
	}
	public void setExampaperService(ExampaperService exampaperService) {
		this.exampaperService = exampaperService;
	}
	
}
