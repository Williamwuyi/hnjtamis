package cn.com.ite.hnjtamis.exam.base.exampublic;

import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.struts2.interceptor.ServletRequestAware;

import cn.com.ite.eap2.common.utils.StringUtils;
import cn.com.ite.eap2.core.struts2.AbstractFormAction;
import cn.com.ite.eap2.core.struts2.ServletContent;
import cn.com.ite.eap2.domain.organization.Organ;
import cn.com.ite.eap2.module.power.login.UserSession;
import cn.com.ite.hnjtamis.baseinfo.domain.Speciality;
import cn.com.ite.hnjtamis.common.StaticVariable;
import cn.com.ite.hnjtamis.exam.base.exampublic.form.ExamPublicForm;
import cn.com.ite.hnjtamis.exam.base.exampublic.service.ExamPublicService;
import cn.com.ite.hnjtamis.exam.base.exampublic.service.ExamPublicThreadService;
import cn.com.ite.hnjtamis.exam.base.theme.form.ThemePostForm;
import cn.com.ite.hnjtamis.exam.base.theme.form.ThemeStandardQuarterForm;
import cn.com.ite.hnjtamis.exam.hibernatemap.ExamPublic;
import cn.com.ite.hnjtamis.exam.hibernatemap.ExamPublicSearchkey;
import cn.com.ite.hnjtamis.exam.hibernatemap.ThemeBank;
/*
 * 考试信息发布 -form
 */
public class ExamPublicFormAction extends AbstractFormAction implements ServletRequestAware{
	private static final long serialVersionUID = 41908400643247072L;
	private HttpServletRequest request;
	
	private ExamPublicThreadService examPublicThreadService;
	
	//private ExamPublic form;
	private ExamPublicForm form;
	/*
	 * 修改
	 */
	public String find(){
		//form = (ExamPublic) service.findDataByKey(this.getId(), ExamPublic.class);
		PropertyUtilsBean pU = new PropertyUtilsBean();
		try {
			ExamPublic po = (ExamPublic) service.findDataByKey(this.getId(), ExamPublic.class);
			form = new ExamPublicForm();
			pU.copyProperties(form, po);//复制属性
			//获取检索信息 并 置入 form
			List<ExamPublicSearchkey> examInfoSers = po.getExamPublicSearchkeies();
			if(examInfoSers!=null && examInfoSers.size()>0){
				List<Speciality> specialitys = new ArrayList<Speciality>();
				List<ThemePostForm> themePostFormList = new ArrayList<ThemePostForm>();
				List<ThemeStandardQuarterForm> standardQuarterList = new ArrayList<ThemeStandardQuarterForm>();
				Map organIdsMap = new HashMap();
				ThemeBank themeBank = new ThemeBank();
				for (ExamPublicSearchkey examPublicSearchkey : examInfoSers) {
					if(!StringUtils.isEmpty(examPublicSearchkey.getPostId())){
						/*ThemePostForm t = new ThemePostForm();
						t.setPostId(examPublicSearchkey.getPostId());//岗位
						t.setPostName(examPublicSearchkey.getPostName());//岗位
						themePostFormList.add(t);*/
						if(organIdsMap.get(examPublicSearchkey.getOrganId())==null){
							Organ organ = new Organ();
							organ.setOrganId(examPublicSearchkey.getOrganId());
							organ.setOrganName(examPublicSearchkey.getOrganName());
							form.getOrgans().add(organ);
							organIdsMap.put(examPublicSearchkey.getOrganId(), examPublicSearchkey.getOrganId());
						}
					}
					if(!StringUtils.isEmpty(examPublicSearchkey.getProfessionId())){
						Speciality s = new Speciality();
						s.setSpecialityid(examPublicSearchkey.getProfessionId());//专业
						s.setSpecialityname(examPublicSearchkey.getProfessionName());//专业
						specialitys.add(s);
					}
					if(!StringUtils.isEmpty(examPublicSearchkey.getQuarterTrainCode())){
						ThemeStandardQuarterForm themeStandardQuarterForm = new ThemeStandardQuarterForm();
						themeStandardQuarterForm.setQuarterTrainCode(examPublicSearchkey.getDeptName()+"@"+examPublicSearchkey.getQuarterTrainCode());
						themeStandardQuarterForm.setQuarterTrainName(examPublicSearchkey.getQuarterTrainName());
						themeStandardQuarterForm.setDeptName(examPublicSearchkey.getDeptName());
						themeStandardQuarterForm.setDeptId(examPublicSearchkey.getDeptId());
						themeStandardQuarterForm.setDcType(examPublicSearchkey.getDcType());
						standardQuarterList.add(themeStandardQuarterForm);
					}
				}
				//System.out.println(examInfoSers.get(0).getThemeBankId()+"   "+examInfoSers.get(0).getThemeBankName());
				themeBank.setThemeBankId(examInfoSers.get(0).getThemeBankId());
				themeBank.setThemeBankName(examInfoSers.get(0).getThemeBankName());
				form.setSpecialitys(specialitys);
				form.setThemePostFormList(themePostFormList);
				form.setStandardQuarterList(standardQuarterList);
				form.setThemeBank(themeBank);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "find";
	}
	
	
	/*
	 * 查询信息发布中的基础信息
	 */
	public String findExamPublicBase(){
		//form = (ExamPublic) service.findDataByKey(this.getId(), ExamPublic.class);
		PropertyUtilsBean pU = new PropertyUtilsBean();
		try {
			ExamPublic po = (ExamPublic) service.findDataByKey(this.getId(), ExamPublic.class);
			form = new ExamPublicForm();
			pU.copyProperties(form, po);//复制属性
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "findExamPublicBase";
	}
	/*
	 * 保存
	 */
	public String save() throws Exception{
		//UserSession usersess = LoginAction.getUserSessionInfo();
		UserSession usersess = (UserSession)ServletContent.getSession().get(StaticVariable.USERSESSION);
		if(usersess == null){
			this.setMsg("SESSION　信息已失效，请关闭浏览器并重新登陆！");
			return "save";
	 	}else if(usersess.getOrganId() == null || "".equals(usersess.getOrganId())){
	 		this.setMsg("登录人员员工信息为空，请用有效用户登录后录入数据！");
			return "save";
	 	}
		try {
			PropertyUtilsBean pU = new PropertyUtilsBean();
			//form = (ExamPublic) this.jsonToObject(ExamPublic.class);
			form = (ExamPublicForm) this.jsonToObject(ExamPublicForm.class);
			if(StringUtils.isEmpty(form.getPublicId())){
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
			
			ExamPublic po = new ExamPublic();
			pU.copyProperties(po, form);
			if(po.getPublicTime()!=null)
			po.setExamStartTime(po.getPublicTime().substring(0,10));
			po.getExamPublicSearchkeies().clear();
			if(form.getExamProperty()!=null && form.getExamProperty().intValue() ==20){
				form.setScoreStartTime(null);
				form.setScoreEndTime(null);
			}
			service.save(po);
			((ExamPublicService)service).deleteSearchInfo(po);
			//((ExamPublicService)service).saveSearchInfo(po, form);
			if("20".equals(po.getState())){
				examPublicThreadService.addExamPublic(po.getPublicId(), form.getThemeBank(),
						form.getSpecialitys(), form.getStandardQuarterList(), form.getOrgans(),usersess,true);
				this.setMsg("信息发布成功,系统后台根据录入的岗位信息，启动初始化考生信息程序，请稍后点击列表的“查询”按钮，刷新列表查看初始化情况！");
			}else{
				examPublicThreadService.addExamPublic(po.getPublicId(), form.getThemeBank(),
						form.getSpecialitys(), form.getStandardQuarterList(), form.getOrgans(),usersess,false);
				this.setMsg("信息发布保存成功！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			this.setMsg("信息发布处理失败！");
		}
		return "save";
	}
	
	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}
	public ExamPublicForm getForm() {
		return form;
	}
	public void setForm(ExamPublicForm form) {
		this.form = form;
	}
	public ExamPublicThreadService getExamPublicThreadService() {
		return examPublicThreadService;
	}
	public void setExamPublicThreadService(
			ExamPublicThreadService examPublicThreadService) {
		this.examPublicThreadService = examPublicThreadService;
	}
	
	
}
