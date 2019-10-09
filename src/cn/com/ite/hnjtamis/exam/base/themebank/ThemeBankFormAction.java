package cn.com.ite.hnjtamis.exam.base.themebank;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.struts2.interceptor.ServletRequestAware;

import cn.com.ite.eap2.common.utils.DateUtils;
import cn.com.ite.eap2.common.utils.StringUtils;
import cn.com.ite.eap2.core.spring.SpringContextUtil;
import cn.com.ite.eap2.core.struts2.AbstractFormAction;
import cn.com.ite.eap2.core.struts2.ServletContent;
import cn.com.ite.eap2.domain.organization.Employee;
import cn.com.ite.eap2.module.power.login.LoginAction;
import cn.com.ite.eap2.module.power.login.UserSession;
import cn.com.ite.hnjtamis.common.StaticVariable;
import cn.com.ite.hnjtamis.exam.base.themebank.form.StandardTypeForm;
import cn.com.ite.hnjtamis.exam.base.themebank.form.ThemeBankForm;
import cn.com.ite.hnjtamis.exam.base.themebank.service.ThemeBankService;
import cn.com.ite.hnjtamis.exam.hibernatemap.ThemeBank;
import cn.com.ite.hnjtamis.jobstandard.domain.JobsStandardThemebank;
import cn.com.ite.hnjtamis.jobstandard.domain.StandardTerms;
import cn.com.ite.hnjtamis.jobstandard.termsEx.StandardTermsExService;
/*
 * 题库管理维护  - form
 */
public class ThemeBankFormAction extends AbstractFormAction implements ServletRequestAware{
	private static final long serialVersionUID = 8458108058549746735L;
	private HttpServletRequest request;
	
	private ThemeBankForm form;
	
	private String op;////admin-管理员 可以配置    dc 电厂私有
	
	private String bankType;//题库类型 10-岗位题库  20-专业题库
	/*
	 * 修改
	 */
	public String find(){
		UserSession usersess = (UserSession)ServletContent.getSession().get(StaticVariable.USERSESSION);
		if(usersess == null){
			this.setMsg("SESSION　信息已失效，请关闭浏览器并重新登陆！");
			return "find";
	 	}
		ThemeBank po = (ThemeBank) service.findDataByKey(this.getId(), ThemeBank.class);
		PropertyUtilsBean pU = new PropertyUtilsBean();
		try {
			form = new ThemeBankForm();
			pU.copyProperties(form, po);
			
			Map term = new HashMap();
			term.put("id", form.getThemeBankId());
			List<StandardTerms> standardTermslist= service.queryData("queryStandardTermsInBankIdHql", term, null);
			for(int i=0;i<standardTermslist.size();i++){
				StandardTerms standardTerms = standardTermslist.get(i);
				
				StandardTypeForm standardTypeForm = new StandardTypeForm();
				standardTypeForm.setTypesId(standardTerms.getStandardid());
				standardTypeForm.setTypesName(standardTerms.getStandardname());
				form.getStandardTypeslist().add(standardTypeForm);
			}
			
			/*List<Speciality> specialitys = new ArrayList<Speciality>();//专业
			List<ThemePostForm> themePostFormList = new ArrayList<ThemePostForm>();//岗位
			if(po.getThemeBankPosts()!=null && po.getThemeBankPosts().size()>0){
				for (ThemeBankPost post : po.getThemeBankPosts()) {
					ThemePostForm t = new ThemePostForm();
					t.setPostId(post.getPostId());
					if(post.getOrganName()!=null && !"".equals(post.getOrganName()) && !"null".equals(post.getOrganName())
							&&post.getDeptName()!=null && !"".equals(post.getDeptName()) && !"null".equals(post.getDeptName())){
						t.setPostName(post.getOrganName()+"-"+post.getDeptName()+"("+post.getPostName()+")");
					}else if(post.getDeptName()!=null && !"".equals(post.getDeptName()) && !"null".equals(post.getDeptName())){
						t.setPostName(post.getDeptName()+"("+post.getPostName()+")");
					}else{
						t.setPostName(post.getPostName());
					}
					
					themePostFormList.add(t);
				}
			}
			if(po.getThemeBankProfessions()!=null && po.getThemeBankProfessions().size()>0){
				for (ThemeBankProfession pro : po.getThemeBankProfessions()) {
					Speciality s = new Speciality();
					s.setSpecialityid(pro.getSpeciality().getSpecialityid());
					s.setSpecialityname(pro.getSpeciality().getSpecialityname());
					specialitys.add(s);
				}
			}
			form.setSpecialitys(specialitys);
			form.setThemePostFormList(themePostFormList);*/
			if(po.getThemeAuditId()!=null && !"null".equals(po.getThemeAuditId()) && po.getThemeAuditId().length()>0){
				String[] themeAuditIds = po.getThemeAuditId().split(",");
				String[] themeAuditNames = po.getThemeAuditName().split(",");
				for (int i=0;i<themeAuditIds.length;i++) {
					Employee s = new Employee();
					s.setEmployeeName(themeAuditNames[i]);
					s.setEmployeeId(themeAuditIds[i]);
					form.getAuditEmps().add(s);
				}
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "find";
	}
	/*
	 * 保存
	 */
	public String save() throws Exception{
		try {
			
			ThemeBankService themeBankService = (ThemeBankService)this.getService();
			
			
			UserSession usersess = LoginAction.getUserSessionInfo();
			form = (ThemeBankForm) this.jsonToObject(ThemeBankForm.class);
			PropertyUtilsBean pU = new PropertyUtilsBean();
			if(StringUtils.isEmpty(form.getThemeBankId())){
				form.setCreatedBy(usersess.getEmployeeName());
				form.setCreatedIdBy(usersess.getEmployeeId());
				form.setCreationDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			}else{
				form.setLastUpdateDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
				form.setLastUpdatedBy(usersess.getEmployeeName());
				form.setLastUpdatedIdBy(usersess.getEmployeeId());
			}
			form.setOrganId(usersess.getCurrentOrganId());
			form.setOrganName(usersess.getCurrentOrganName());
			form.setBankType(bankType);
			
			
			String themeAuditIds = "";
			String themeAuditNames = "";
			List<Employee> auditEmps = form.getAuditEmps();
			if(auditEmps!=null && auditEmps.size()>0){
				for(int i=0;i<auditEmps.size();i++){
					Employee emp = auditEmps.get(i);
					themeAuditIds+=emp.getEmployeeId()+",";
					themeAuditNames+=emp.getEmployeeName()+",";
				}
				if(themeAuditIds.length()>0)themeAuditIds = themeAuditIds.substring(0,themeAuditIds.length()-1);
				if(themeAuditNames.length()>0)themeAuditNames = themeAuditNames.substring(0,themeAuditNames.length()-1);
			}
			
			ThemeBank po = new ThemeBank();
			pU.copyProperties(po, form);
			po.setThemeAuditId(themeAuditIds);
			po.setThemeAuditName(themeAuditNames);
			
			themeBankService.saveThemeBank(po);
			//更新关联表的一些信息
			themeBankService.updateBankInRelation(po.getThemeBankId(), 
					po.getOrganId(), po.getOrganName(), po.getBankPublic(), po.getBankType());
			//((ThemeBankService)service).deletePosts(po);
			//((ThemeBankService)service).deletePros(po);
			//((ThemeBankService)service).savePosts(po, form.getThemePostFormList(),usersess);
			//((ThemeBankService)service).savePros(po, form.getSpecialitys(),usersess);
			
			//以下处理题库与标准模块的关联
			Map term = new HashMap();
			term.put("id", po.getThemeBankId());
			List<StandardTerms> old_standardTermslist= service.queryData("queryStandardTermsInBankIdHql", term, null);
			List<StandardTypeForm> new_standardTermslist= form.getStandardTypeslist();
			
			StandardTermsExService standardTermsExService = (StandardTermsExService)SpringContextUtil.getBean("standardtermsExServer");
			
			//以下是原有不存在，需要新增的情况
			for(int i=0;i<new_standardTermslist.size();i++){
				StandardTypeForm standardTypeForm = new_standardTermslist.get(i);
				boolean isAdd = true;
				for(int j=0;j<old_standardTermslist.size();j++){
					StandardTerms standardTerms = old_standardTermslist.get(j);
					if(standardTerms.getStandardid().equals(standardTypeForm.getTypesId())){
						isAdd = false;
						break;
					}
				}
				if(isAdd){
					StandardTerms standardTerms = (StandardTerms)this.getService().findDataByKey(standardTypeForm.getTypesId(), StandardTerms.class);
					JobsStandardThemebank jobsStandardThemebank = new JobsStandardThemebank();
					jobsStandardThemebank.setThemeBankId(po.getThemeBankId());
					jobsStandardThemebank.setThemeBankName(po.getThemeBankName());
					if("20".equals(po.getBankPublic())){
						jobsStandardThemebank.setBankPublic("20");
						jobsStandardThemebank.setOrganId(po.getOrganId());
						jobsStandardThemebank.setOrganName(po.getOrganName());
					}else if("10".equals(po.getBankPublic())){
						jobsStandardThemebank.setBankPublic("10");
						jobsStandardThemebank.setOrganId("");
						jobsStandardThemebank.setOrganName("");
					}
					jobsStandardThemebank.setStandardTerms(standardTerms);
					jobsStandardThemebank.setCreatedBy(StringUtils.isEmpty(usersess.getEmployeeName())?usersess.getAccount():usersess.getEmployeeName());
					jobsStandardThemebank.setCreationDate(DateUtils.convertDateToStr(new Date(),"yyyy-MM-dd HH:mm:ss"));
					this.getService().add(jobsStandardThemebank);
					try {
						standardTermsExService.updateMoreStandardQuarterBank(standardTerms.getStandardid());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			
			//以下是存在，但最新的已经删除的情况
			for(int j=0;j<old_standardTermslist.size();j++){
				StandardTerms standardTerms = old_standardTermslist.get(j);
				boolean isDel = true;
				for(int i=0;i<new_standardTermslist.size();i++){
					StandardTypeForm standardTypeForm = new_standardTermslist.get(i);
					if(standardTerms.getStandardid().equals(standardTypeForm.getTypesId())){
						isDel = false;
						break;
					}
				}
				if(isDel){
					Iterator its = standardTerms.getJobsStandardThemebanks().iterator();
					while(its.hasNext()){
						JobsStandardThemebank jobsStandardThemebank = (JobsStandardThemebank)its.next();
						if(po.getThemeBankId().equals(jobsStandardThemebank.getThemeBankId())){
							its.remove();
							break;
						}
					}
					this.getService().saveOld(standardTerms);
					try {
						standardTermsExService.updateMoreStandardQuarterBank(standardTerms.getStandardid());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "save";
	}
	/*
	 * 保存排序
	 */
	public String saveSort() throws Exception{
		((ThemeBankService)service).saveSort(this.getSortIds());
		return "save";
	}
	
	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
		
	}
	public ThemeBankForm getForm() {
		return form;
	}
	public void setForm(ThemeBankForm form) {
		this.form = form;
	}

	public String getOp() {
		return op;
	}

	public void setOp(String op) {
		this.op = op;
	}
	public String getBankType() {
		return bankType;
	}
	public void setBankType(String bankType) {
		this.bankType = bankType;
	}
	
	
}
