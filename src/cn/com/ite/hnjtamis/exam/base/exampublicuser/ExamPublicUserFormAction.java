package cn.com.ite.hnjtamis.exam.base.exampublicuser;

import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.struts2.interceptor.ServletRequestAware;

import cn.com.ite.eap2.common.utils.StringUtils;
import cn.com.ite.eap2.core.struts2.AbstractFormAction;
import cn.com.ite.eap2.domain.organization.Dept;
import cn.com.ite.eap2.domain.organization.Employee;
import cn.com.ite.eap2.domain.organization.Organ;
import cn.com.ite.eap2.domain.organization.Quarter;
import cn.com.ite.eap2.module.power.login.LoginAction;
import cn.com.ite.eap2.module.power.login.UserSession;
import cn.com.ite.hnjtamis.common.action.employee.form.EmployeeForm;
import cn.com.ite.hnjtamis.exam.base.exampublic.service.ExamPublicService;
import cn.com.ite.hnjtamis.exam.base.exampublicuser.form.ExamPublicUserForm;
import cn.com.ite.hnjtamis.exam.base.exampublicuser.form.ExamPublicUserListForm;
import cn.com.ite.hnjtamis.exam.hibernatemap.ExamPublic;
import cn.com.ite.hnjtamis.exam.hibernatemap.ExamPublicUser;
import cn.com.ite.hnjtamis.exam.hibernatemap.ExamUserTestpaper;
/*
 * 考生信息维护 - form
 */
public class ExamPublicUserFormAction extends AbstractFormAction implements ServletRequestAware{
	private static final long serialVersionUID = 8734009763292574124L;
	private ExamPublicService examPublicService;
	private HttpServletRequest request;
	
	private ExamPublicUserForm form;
	
	private List employeeList;
	
	private String op;
	
	/*
	 * 新增 or 删除
	 */
	public String find(){
		String enroll = request.getParameter("enroll");
		String oper = request.getParameter("oper");
		String publicId = request.getParameter("publicId");
		UserSession us = LoginAction.getUserSessionInfo();
		try {
			Organ o = new Organ();
			Dept d = new Dept();
			Quarter q = new Quarter();
			Employee e = new Employee();
			
			form = new ExamPublicUserForm();
			PropertyUtilsBean pU = new PropertyUtilsBean();
			if(!StringUtils.isEmpty(enroll) && enroll.equals("self") && "add".equals(oper) && 
					(this.getId()==null || "".equals(this.getId()) || "null".equals(this.getId()))){
				o.setOrganId(us.getOrganId());
				o.setOrganName(us.getOrganName());
				d.setDeptId(us.getDeptId());
				d.setDeptName(us.getDeptName());
				q.setQuarterId(us.getQuarterId());
				q.setQuarterName(us.getQuarterName());
				e.setEmployeeId(us.getEmployeeId());
				e.setEmployeeName(us.getEmployeeName());
				//form.setUserName(us.getEmployeeName());
				form.setExamPublicId(publicId);			
			}else if("update".equals(oper) || "view".equals(oper)
					|| !(this.getId()==null || "".equals(this.getId()) || "null".equals(this.getId()))){
				ExamPublicUser po = (ExamPublicUser) service.findDataByKey(this.getId(), ExamPublicUser.class);
				//查询 准考证 登录密码
				Map term = new HashMap();
				term.put("userId", po.getUserId());
				List<ExamUserTestpaper> pwdList = service.queryData("queryUserPwdAndLoginNumHql", term, null, ExamUserTestpaper.class);
				/*if(pwdList!=null && pwdList.size()>0){
					for(ExamUserTestpaper tmp1 : pwdList){
						System.out.println(tmp1.getExam().getExamName());
					}
				}*/
				form.setPwdList(pwdList);
				
				pU.copyProperties(form, po);
				form.setExamPublicId(po.getExamPublic().getPublicId());
				form.setExamPublicName(po.getExamPublic().getExamTitle());
				o.setOrganId(po.getUserOrganId());
				o.setOrganName(po.getUserOrganName());
				d.setDeptId(po.getUserDeptId());
				d.setDeptName(po.getUserDeptName());
				q.setQuarterId(po.getPostId());
				q.setQuarterName(po.getPostName());
				e.setEmployeeId(po.getEmployeeId());
				e.setEmployeeName(po.getEmployeeName());
			}
			form.setEmployee(e);
			form.setOrgan(o);
			form.setDept(d);
			form.setQuarter(q);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "find";
	}
	/*
	 * 保存
	 */
	public String save() throws Exception{
		String enroll = request.getParameter("enroll");
		UserSession us = LoginAction.getUserSessionInfo();
		try {
			form = (ExamPublicUserForm) this.jsonToObject(ExamPublicUserForm.class);
			Map term = new HashMap();
			term.put("examPublicId", form.getExamPublicId());
			term.put("employeeId", form.getEmployee().getEmployeeId());
			List<ExamPublicUser> uselist = this.getService().queryData("queryUserByPublicIdAndEmpId", term, null);
			ExamPublicUser po = uselist!=null && uselist.size()>0 ? uselist.get(0) : null;
			boolean isUpdate = true;
			if(po==null){
				po = new ExamPublicUser(); 
				po.setCreatedBy(us.getEmployeeName());
				po.setCreatedIdBy(us.getEmployeeId());
				po.setCreationDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
				isUpdate = false;
				po.setSyncFlag("1");
				po.setIsExp("0");
			}else{
				po.setLastUpdatedBy(us.getEmployeeName());
				po.setLastUpdatedIdBy(us.getEmployeeId());
				po.setLastUpdateDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
				po.setSyncFlag("2");
			}
			PropertyUtilsBean pU = new PropertyUtilsBean();
			pU.copyProperties(po, form);
			ExamPublic ep = (ExamPublic) examPublicService.findDataByKey(form.getExamPublicId(), ExamPublic.class);
			po.setExamPublic(ep);
			
			po.setEmployeeId(form.getEmployee().getEmployeeId());
			po.setEmployeeName(form.getEmployee().getEmployeeName());
			if(form.getEmployee().getEmployeeId()!=null){
				Employee employee = (Employee)examPublicService.findDataByKey(form.getEmployee().getEmployeeId(), Employee.class);
				if(employee!=null && employee.getQuarter()!=null){
					po.setPostId(employee.getQuarter().getQuarterId());
					po.setPostName(employee.getQuarter().getQuarterName());
				}
				if(employee!=null && employee.getDept()!=null){
					po.setUserDeptId(employee.getDept().getDeptId());
					po.setUserDeptName(employee.getDept().getDeptName());
					if(employee.getDept().getOrgan()!=null){
						po.setUserOrganId(employee.getDept().getOrgan().getOrganId());
						po.setUserOrganName(employee.getDept().getOrgan().getOrganName());
					}
				}
			}
			po.setIsDel("0");
			po.setUserName(po.getEmployeeName());
			po.setOrganId(us.getOrganId());
			po.setOrganName(us.getOrganName());
			if(po.getBmType()==null || "".equals(po.getBmType()) || "null".equals(po.getBmType())){
				po.setBmType("10");
			}
			service.save(po);
			this.setMsg("保存成功！");
		} catch (Exception e) {
			this.setMsg("保存失败！");
			e.printStackTrace();
		}
		return "save";
	}
	
	
	public String queryMoreEmpList(){
		UserSession us = LoginAction.getUserSessionInfo();
		Map term = new HashMap();
		ExamPublic ep = (ExamPublic) examPublicService.findDataByKey(this.getId(), ExamPublic.class);
		term.put("examPublicIdTerm", this.getId());
		//System.out.println(this.getId());
		List<ExamPublicUser> list =   this.getService().queryData("queryUserByPublicId", term, null,ExamPublicUser.class);
		employeeList = new ArrayList();
		//System.out.println(list.size());
		
		boolean isCreateUser = true;
		if(!ep.getOrganId().equals(us.getOrganId())){
			isCreateUser = false;
		}
		for(int i=0;i<list.size();i++){
			ExamPublicUser po =  list.get(i);
			if(isCreateUser || us.getOrganId().equals(po.getUserOrganId())){
				EmployeeForm employeeForm = new EmployeeForm();
				employeeForm.setOrganId(po.getUserOrganId());
				employeeForm.setOrganName(po.getUserOrganName());
				employeeForm.setDeptId(po.getUserDeptId());
				employeeForm.setDeptName(po.getUserDeptName());
				employeeForm.setQuarterId(po.getPostId());
				employeeForm.setQuarterName(po.getPostName());
				employeeForm.setEmployeeId(po.getEmployeeId());
				employeeForm.setEmployeeName(po.getEmployeeName());
				employeeForm.setUserSex(po.getUserSex());//['1','男'],['2','女']
				employeeForm.setUserBirthday(po.getUserBirthday());//生日
				employeeForm.setIdNumber(po.getIdNumber()!=null?po.getIdNumber().trim():po.getIdNumber());//身份证号
				employeeForm.setUserNation(po.getUserNation());//民族
				employeeForm.setUserPhone(po.getUserPhone());//手机号
				employeeForm.setUserAddr(po.getUserAddr());//地址
				employeeList.add(employeeForm);
			}
			
		}
		
		return "queryMoreEmpList";
	}
	
	public String saveMore() throws Exception{
		String enroll = request.getParameter("enroll");
		UserSession us = LoginAction.getUserSessionInfo();
		try {
			ExamPublicUserListForm usersForm = (ExamPublicUserListForm) this.jsonToObject(ExamPublicUserListForm.class);
			ExamPublic ep = (ExamPublic) examPublicService.findDataByKey(usersForm.getExamPublicId(), ExamPublic.class);
			
			boolean isCreateUser = true;//是否创建考试发布信息的机构
			if(!ep.getOrganId().equals(us.getOrganId())){
				isCreateUser = false;
			}
			
			Map term = new HashMap();
			term.put("examPublicIdTerm", usersForm.getExamPublicId());
			List<ExamPublicUser> list =   this.getService().queryData("queryUserByPublicId", term, null,ExamPublicUser.class);
			Map<String,ExamPublicUser> examPublicUserMap = new HashMap<String,ExamPublicUser>();
			for(int i=0;i<list.size();i++){
				ExamPublicUser po = list.get(i);
				if(isCreateUser || us.getOrganId().equals(po.getUserOrganId())){
					examPublicUserMap.put(po.getEmployeeId(), po);
				}
			}
			if(usersForm.getEmployeeList()!=null && usersForm.getEmployeeList().size()>0){
				for(int i=0;i<usersForm.getEmployeeList().size();i++){
					EmployeeForm employeeForm = usersForm.getEmployeeList().get(i);
					ExamPublicUser po = examPublicUserMap.get(employeeForm.getEmployeeId());	
					if(po == null){
						po = new ExamPublicUser();
						po.setCreatedBy(us.getEmployeeName());
						po.setCreatedIdBy(us.getEmployeeId());
						po.setCreationDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
						po.setIsExp("0");
						po.setState("20");
						po.setIsDel("0");
						po.setSyncFlag("1");
						po.setBmType("10");
					}else{
						po.setLastUpdateDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
						po.setLastUpdatedBy(us.getEmployeeName());
						po.setLastUpdatedIdBy(us.getEmployeeId());
						po.setSyncFlag("2");
						examPublicUserMap.remove(employeeForm.getEmployeeId());
						if(po.getBmType()==null|| "".equals(po.getBmType()) || "null".equals(po.getBmType())){
							po.setBmType("10");
						}
					}
					
					po.setUserOrganId(employeeForm.getOrganId());
					po.setUserOrganName(employeeForm.getOrganName());
					po.setUserDeptId(employeeForm.getDeptId());
					po.setUserDeptName(employeeForm.getDeptName());
					po.setPostId(employeeForm.getQuarterId());
					po.setPostName(employeeForm.getQuarterName());
					po.setEmployeeId(employeeForm.getEmployeeId());
					po.setEmployeeName(employeeForm.getEmployeeName());
					po.setExamPublic(ep);
					po.setUserName(po.getEmployeeName());
					po.setOrganId(us.getOrganId());
					po.setOrganName(us.getOrganName());
					
					po.setUserSex(employeeForm.getUserSex());//['1','男'],['2','女']
					po.setUserBirthday(employeeForm.getUserBirthday());//生日
					po.setIdNumber(employeeForm.getIdNumber()!=null?employeeForm.getIdNumber().trim():employeeForm.getIdNumber());//身份证号
					po.setUserNation(employeeForm.getUserNation());//民族
					po.setUserPhone(employeeForm.getUserPhone());//手机号
					po.setUserAddr(employeeForm.getUserAddr());//地址
					po.setIsDel("0");
					service.saveOld(po);
				}
			}
			
			if(!examPublicUserMap.keySet().isEmpty()){
				Iterator its = examPublicUserMap.keySet().iterator();
				while(its.hasNext()){
					String key = (String)its.next();
					ExamPublicUser po = examPublicUserMap.get(key);
					po.setIsDel("1");
					po.setSyncFlag("3");
					if(po.getBmType()==null|| "".equals(po.getBmType()) || "null".equals(po.getBmType())){
						po.setBmType("10");
					}
					service.saveOld(po);
				}
			}
			this.setMsg("保存成功！");
		} catch (Exception e) {
			this.setMsg("保存失败！");
			e.printStackTrace();
		}
		return "save";
	}
	
	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public ExamPublicUserForm getForm() {
		return form;
	}

	public void setForm(ExamPublicUserForm form) {
		this.form = form;
	}

	public ExamPublicService getExamPublicService() {
		return examPublicService;
	}

	public void setExamPublicService(ExamPublicService examPublicService) {
		this.examPublicService = examPublicService;
	}
	public List getEmployeeList() {
		return employeeList;
	}
	public void setEmployeeList(List employeeList) {
		this.employeeList = employeeList;
	}
	public String getOp() {
		return op;
	}
	public void setOp(String op) {
		this.op = op;
	}
	
	
	
}
