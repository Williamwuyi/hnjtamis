package cn.com.ite.hnjtamis.exam.base.exampublicuser;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.tools.ant.util.DateUtils;

import cn.com.ite.eap2.common.utils.StringUtils;
import cn.com.ite.eap2.core.spring.SpringContextUtil;
import cn.com.ite.eap2.core.struts2.AbstractListAction;
import cn.com.ite.eap2.domain.baseinfo.SysAfficheUser;
import cn.com.ite.eap2.domain.organization.Dept;
import cn.com.ite.eap2.domain.organization.Employee;
import cn.com.ite.eap2.domain.organization.Organ;
import cn.com.ite.eap2.domain.organization.Quarter;
import cn.com.ite.eap2.module.organization.dept.DeptService;
import cn.com.ite.eap2.module.organization.organ.OrganService;
import cn.com.ite.eap2.module.organization.quarter.QuarterService;
import cn.com.ite.eap2.module.power.login.LoginAction;
import cn.com.ite.eap2.module.power.login.UserSession;
import cn.com.ite.hnjtamis.exam.base.exampublic.service.ExamPublicService;
import cn.com.ite.hnjtamis.exam.base.exampublicuser.form.ExamPublicSelfUserForm;
import cn.com.ite.hnjtamis.exam.base.exampublicuser.service.ExamPublicUserService;
import cn.com.ite.hnjtamis.exam.base.exampublicuser.service.ExamPublicUserServiceImpl;
import cn.com.ite.hnjtamis.exam.hibernatemap.ExamPublic;
import cn.com.ite.hnjtamis.exam.hibernatemap.ExamPublicUser;
/*
 * 考生信息维护 - list
 */
public class ExamPublicUserListAction extends AbstractListAction implements ServletRequestAware{
	private static final long serialVersionUID = -6788417758628399322L;
	private HttpServletRequest request;
	
	private OrganService organService;//机构
	private DeptService deptService;//部门
	private QuarterService quarterService;//岗位
	private ExamPublicService examPublicService;//考试信息
	
	public static Map<String,String> organDeptQuarterMap = new HashMap<String,String>();//<机构名@部门名@岗位名,机构id@部门id@岗位id>
	public static Map<String,String> employeeMap = new HashMap<String,String>();//<机构名@部门名@岗位名,机构id@部门id@岗位id>
	public static Map<String,ExamPublic> examPublicMap = new HashMap<String,ExamPublic>();
	
	
	private String titleTerm;
	private String publicIdTerm;
	private String startTerm;
	private String endTerm;
	private String stateTerm;
	private String userNameTerm;
	private String passDeadLineTerm;
	private String takeInTerm;
	private String updateResult;//审核是否成功
	private String countResult;//本场考试信息是否已经报名
	private String bmTypeTerm;//报名类型
	private String employeeIdTerm;
	
	private String userOrganIdTerm;
	
	
	private String op;
	
	/**
	 * 导入文件
	 */
	private File xls;
	/**
	 * 导出文件流
	 */
	private InputStream inputStream;
	/**
	 * 文件名
	 */
	private String fileName;
	
	private List<ExamPublicUser> list = new ArrayList<ExamPublicUser>();
	
	private List<String> resultInfo = new ArrayList<String>();
	
	private List<ExamPublicSelfUserForm> selfList = new ArrayList<ExamPublicSelfUserForm>();
	
	/*
	 * 导出
	 */
	public String exportXls() throws Exception{
		try {
			File exportXlsFile = ((ExamPublicUserService)service).exportDate(null);
			inputStream = new FileInputStream(exportXlsFile);
			this.fileName = new String("参考考生信息.xls".getBytes(),"ISO-8859-1");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "export";
	}
	/*
	 * 导入
	 */
	public String importXls() throws Exception{
		try {
			//if(organDeptQuarterMap.size()<1){//无记录时 查询
				//List<Organ> organList = organService.queryData("queryOrgan", null, null);
				//List<Dept> deptList = deptService.queryData("queryDept", null, null);
				List<Quarter> quarterList = quarterService.queryData("queryQuarter", null, null);
				
				if(quarterList!=null && quarterList.size()>0){
					for (Quarter quarter : quarterList) {
						Dept dept = quarter.getDept();
						Organ organ = dept.getOrgan();
						String names = organ.getOrganName()+"@"+dept.getDeptName()+"@"+quarter.getQuarterName();
						String ids = organ.getOrganId()+"@"+dept.getDeptId()+"@"+quarter.getQuarterId();
						organDeptQuarterMap.put(names, ids);
						//查询员工
						List<Employee> empList = quarter.getEmployees();
						if(empList!=null && empList.size()>0){
							for (Employee employee : empList) {
								employeeMap.put(dept.getDeptName()+"@"+quarter.getQuarterName()+"@"+employee.getEmployeeName(), employee.getEmployeeId());
							}
						}
					}
				}
				
				List<ExamPublic> ExamPublicList = examPublicService.queryData("queryExamPublic", null, null);
				if(ExamPublicList!=null && ExamPublicList.size()>0){
					for (ExamPublic examPublic : ExamPublicList) {
						examPublicMap.put(examPublic.getExamTitle(), examPublic);
					}
				}
				
			//}
			//((ExamPublicUserService)service).importDate(xls);
			ExamPublicUserService es = (ExamPublicUserService) SpringContextUtil.getBean("examPublicUserServer");
			resultInfo = es.importDate(xls);
			//更新考生信息
			es.updateExamUser();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "save";
	}
	
	/*
	 * 审核
	 */
	public String auditList() throws Exception{
		try {
			String ids = request.getParameter("ids");
			if(!StringUtils.isEmpty(ids)){
				String[] saveids = ids.split(",");
				String updateIds = "";
				for(int i=0;i<saveids.length;i++){
					if(!StringUtils.isEmpty(saveids[i])){
						updateIds += "'"+saveids[i]+"',";
					}
				}
				if(!StringUtils.isEmpty(updateIds)){
					updateIds = updateIds.substring(0, updateIds.length()-1);
				}
				ExamPublicUserService examPublicUserService = (ExamPublicUserService)service;
				int resultInt = examPublicUserService.savePublicUserState(updateIds);
				updateResult = "success";
				
			}
		} catch (Exception e) {
			updateResult = "failure";
			e.printStackTrace();
		}
		
		return "auditList";
	}
	
	/*
	 * 列表数据显示
	 */
	public String list() throws Exception{
		UserSession us = LoginAction.getUserSessionInfo();
		try {
			String enroll = request.getParameter("enroll");
			if("无".equals(this.getTitleTerm())){
				this.setTitleTerm(null);
			}
			if(!StringUtils.isEmpty(enroll) && "self".equals(enroll)){
				//userNameTerm = us.getEmployeeName();
				this.setEmployeeIdTerm(us.getEmployeeId());
				this.setBmTypeTerm("20");
				this.setTitleTerm("%");
				this.setPassDeadLineTerm("2");
			}
			//Sat Apr 01 2017 00:00:00 GMT+0800 (中国标准时间) 
			if(this.getStartTerm()!=null && !"".equals(this.getStartTerm()) && this.getStartTerm().indexOf("GMT")!=-1){
				this.setStartTerm(DateUtils.format(new Date(this.getStartTerm()), "yyyy-MM-dd"));
			}
			if("audit".equals(op)){
				this.setBmTypeTerm("20");
			}
			ExamPublic examPublic = null;
			if(this.getPublicIdTerm()!=null && !"".equals(this.getPublicIdTerm())){
				examPublic = (ExamPublic)this.getService().findDataByKey(this.getPublicIdTerm(),ExamPublic.class);
			}
			if(examPublic!=null && !examPublic.getOrganId().equals(us.getOrganId())){
				this.setUserOrganIdTerm(us.getOrganId());
			}
			if(!"self".equals(enroll) && (publicIdTerm==null || "".equals(publicIdTerm) || "null".equals(publicIdTerm))){
				this.setPublicIdTerm("publicIdTerms");
			}
			
			list = (List<ExamPublicUser>)service.queryData("queryHql", this, null,ExamPublicUser.class,this.getStart(),this.getLimit());
			this.setTotal(service.countData("queryHql", this));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "list";
	}
	
	
	/*
	 * 列表数据显示
	 */
	public String msgHzlist() throws Exception{
		UserSession us = LoginAction.getUserSessionInfo();
		try {
			list = (List<ExamPublicUser>)service.queryData("queryMsgHzUserHql", this, null,ExamPublicUser.class);	
			List<SysAfficheUser> afuserlist = service.queryData("querySysAfficheUserInPublicIdHql", this, null,SysAfficheUser.class);
			if(afuserlist!=null && afuserlist.size()>0){
				for(int i=0;i<list.size();i++){
					ExamPublicUser examPublicUser = (ExamPublicUser)list.get(i);
					for(int j=0;j<afuserlist.size();j++){
						SysAfficheUser sysAfficheUser = afuserlist.get(j);
						if(sysAfficheUser.getEmployeeId().equals(examPublicUser.getEmployeeId())){
							examPublicUser.setMsgHzFlag("T");
							break;
						}
					}
				}
			}
			this.setTotal(service.countData("queryMsgHzUserHql", this));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "list";
	}
	/*
	 * 员工报名列表
	 */
	public String selfList() throws Exception{
		UserSession us = LoginAction.getUserSessionInfo();
		try {
			
			String quarteridId = us.getQuarterId();//获得岗位信息
			//查询晋升的岗位信息
			Map term = new HashMap();
			term.put("quarteridId", quarteridId);
			List promotionQIdsList = service.queryData("queryProQuaterIdsSql", term, null, null);
			List<String> termquid = new ArrayList<String>();
			if(promotionQIdsList!=null && promotionQIdsList.size()>0){
				for(Object obj : promotionQIdsList){
					Map querMap = (Map) obj;
					String querid = querMap.get("PC").toString();
					termquid.add(querid);
				}
			}
			termquid.add(quarteridId);
			System.out.println(termquid);
			
			term.clear();
			String stateTerm = "20";
			term.put("stateTerm", stateTerm);
			term.put("startTerm", startTerm);
			term.put("endTerm", endTerm);
			term.put("passDeadLineTerm", passDeadLineTerm);
			term.put("termquid", termquid);
			
			//查询 考试发布信息
			List<ExamPublic> examPublicList = service.queryData("queryExamPublicHql", term, null, ExamPublic.class);
			//查询当前登录人 参加的 考试信息
			term.clear();
			term.put("employeeIdTerm", us.getEmployeeId());
			List<ExamPublicUser> examPublicUserList = service.queryData("queryExamPublicUserHql", term, null, ExamPublicUser.class);
			
			Map<String,ExamPublicUser> examPublicUserMap = new HashMap<String,ExamPublicUser>();
			if(examPublicUserList!=null && examPublicUserList.size()>0){
				for(ExamPublicUser t : examPublicUserList){
					examPublicUserMap.put(t.getExamPublic().getPublicId(), t);
				}
			}
			
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			Date nowDate = df.parse(df.format(new Date()));
			if(examPublicList!=null && examPublicList.size()>0){
				
				for(ExamPublic t : examPublicList){
					ExamPublicSelfUserForm form = new ExamPublicSelfUserForm();
					boolean flag = false;
					form.setPublicId(t.getPublicId());
					form.setExamTitle(t.getExamTitle());
					form.setExamStartTime(t.getExamStartTime());
					form.setExamEndTime(t.getExamEndTime());
					Date endDate = df.parse(form.getExamEndTime());
					if(endDate.getTime()-nowDate.getTime()>=0){
						form.setIsDeadLine(false);
					}else{
						form.setIsDeadLine(true);
					}
					if(examPublicUserMap.containsKey(form.getPublicId())){
						ExamPublicUser tmp = examPublicUserMap.get(form.getPublicId());
						form.setIsDel(tmp.getIsDel());
						form.setUserId(tmp.getUserId());
						if(form.getIsDel().equals("0")){
							flag = true;
						}
					}
					//System.out.println(form.getExamTitle()+"    "+form.getIsDel());
					if(StringUtils.isEmpty(form.getIsDel())){
						form.setIsDel("1");
					}
					if(takeInTerm.equals("1") && flag){
						selfList.add(form);
					}else if(takeInTerm.equals("0") && !flag){
						selfList.add(form);
					}else if(takeInTerm.equals("2")){
						selfList.add(form);
					}
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "selfList";
	}
	
	/*
	 * 删除
	 */
	public String delete(){
		try {
			Map term = new HashMap();
			term.put("ids", this.getId());
			List list = service.queryData("queryIdNumbersByIdsSql", term, null, null);
			List<String> idList = new ArrayList<String>();
			String publicId = "";
			if(list!=null && list.size()>0){
				for(Object obj : list){
					HashMap po = (HashMap) obj;
					String idnumber = po.get("ID_NUMBER")!=null?po.get("ID_NUMBER").toString().trim():null;
					if(!StringUtils.isEmpty(idnumber)){
						idList.add(idnumber);
					}
					publicId = po.get("PUBLIC_ID").toString();
				}
			}
			ExamPublic examPublicPo = (ExamPublic) service.findDataByKey(publicId, ExamPublic.class);
			String existsIdNumber = ExamPublicUserServiceImpl.userExamMap.get(examPublicPo.getPublicId());
			//System.out.println(idList);
			//System.out.println(publicId);
			if(!StringUtils.isEmpty(existsIdNumber) && idList.size()>0){
				for(String str:idList){
					if(existsIdNumber.indexOf(str+",")!=-1){
						existsIdNumber = existsIdNumber.replace(str+",", "");
					}else if(existsIdNumber.indexOf(str)!=-1){
						existsIdNumber = existsIdNumber.replace(str, "");
					}
				}
				ExamPublicUserServiceImpl.userExamMap.put(examPublicPo.getPublicId(),existsIdNumber);
			}
			
			service.deleteByKeys(this.getId().split(","),ExamPublicUser.class);
			this.setMsg("记录删除成功！");
		} catch (Exception e) {
			e.printStackTrace();
			this.setMsg("记录已被使用，不能删除，删除失败！");
		}
		return "delete";
	}
	public String isRegisterExam(){
		String examPublicIdTerm = request.getParameter("examPublicIdTerm");
		String idNumberTerm = request.getParameter("idNumberTerm");
		String userIdTerm = request.getParameter("userIdTerm");
		Map reTerm = new HashMap();
		reTerm.put("examPublicIdTerm", examPublicIdTerm);
		reTerm.put("idNumberTerm", idNumberTerm);
		List<ExamPublicUser> countList = service.queryData("isRegisterExam",reTerm, null);
		
		if(StringUtils.isEmpty(userIdTerm)){
			if(countList!=null && countList.size()>0){
				countResult = countList.size()+"";
			}else{
				countResult = "0";
			}
		}else{
			if(countList!=null && countList.size()>0){
				ExamPublicUser po =  countList.get(0);
				if(po.getUserId().equals(userIdTerm)){
					countResult = "0";
				}else{
					countResult = countList.size()+"";
				}
			}else{
				countResult = "0";
			}
			
		}
		return "isRegisterExam";
	}
	
	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public List<ExamPublicUser> getList() {
		return list;
	}
	public void setList(List<ExamPublicUser> list) {
		this.list = list;
	}

	public String getTitleTerm() {
		return titleTerm;
	}

	public void setTitleTerm(String titleTerm) {
		this.titleTerm = titleTerm;
	}

	public String getStartTerm() {
		return startTerm;
	}

	public void setStartTerm(String startTerm) {
		this.startTerm = startTerm;
	}

	public String getEndTerm() {
		return endTerm;
	}

	public void setEndTerm(String endTerm) {
		this.endTerm = endTerm;
	}

	public String getStateTerm() {
		return stateTerm;
	}

	public void setStateTerm(String stateTerm) {
		this.stateTerm = stateTerm;
	}

	public String getPassDeadLineTerm() {
		return passDeadLineTerm;
	}

	public void setPassDeadLineTerm(String passDeadLineTerm) {
		this.passDeadLineTerm = passDeadLineTerm;
	}

	public String getUserNameTerm() {
		return userNameTerm;
	}

	public void setUserNameTerm(String userNameTerm) {
		this.userNameTerm = userNameTerm;
	}

	public String getUpdateResult() {
		return updateResult;
	}

	public void setUpdateResult(String updateResult) {
		this.updateResult = updateResult;
	}

	public File getXls() {
		return xls;
	}

	public void setXls(File xls) {
		this.xls = xls;
	}

	public OrganService getOrganService() {
		return organService;
	}

	public void setOrganService(OrganService organService) {
		this.organService = organService;
	}

	public DeptService getDeptService() {
		return deptService;
	}

	public void setDeptService(DeptService deptService) {
		this.deptService = deptService;
	}

	public QuarterService getQuarterService() {
		return quarterService;
	}

	public void setQuarterService(QuarterService quarterService) {
		this.quarterService = quarterService;
	}

	public static Map<String, String> getOrganDeptQuarterMap() {
		return organDeptQuarterMap;
	}

	public static void setOrganDeptQuarterMap(
			Map<String, String> organDeptQuarterMap) {
		ExamPublicUserListAction.organDeptQuarterMap = organDeptQuarterMap;
	}
	
	
	public static Map<String, String> getEmployeeMap() {
		return employeeMap;
	}
	public static void setEmployeeMap(Map<String, String> employeeMap) {
		ExamPublicUserListAction.employeeMap = employeeMap;
	}
	public static Map<String, ExamPublic> getExamPublicMap() {
		return examPublicMap;
	}

	public static void setExamPublicMap(Map<String, ExamPublic> examPublicMap) {
		ExamPublicUserListAction.examPublicMap = examPublicMap;
	}

	public ExamPublicService getExamPublicService() {
		return examPublicService;
	}

	public void setExamPublicService(ExamPublicService examPublicService) {
		this.examPublicService = examPublicService;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getCountResult() {
		return countResult;
	}
	public void setCountResult(String countResult) {
		this.countResult = countResult;
	}
	public List<String> getResultInfo() {
		return resultInfo;
	}
	public void setResultInfo(List<String> resultInfo) {
		this.resultInfo = resultInfo;
	}
	public List<ExamPublicSelfUserForm> getSelfList() {
		return selfList;
	}
	public void setSelfList(List<ExamPublicSelfUserForm> selfList) {
		this.selfList = selfList;
	}
	public String getTakeInTerm() {
		return takeInTerm;
	}
	public void setTakeInTerm(String takeInTerm) {
		this.takeInTerm = takeInTerm;
	}
	public String getPublicIdTerm() {
		return publicIdTerm;
	}
	public void setPublicIdTerm(String publicIdTerm) {
		this.publicIdTerm = publicIdTerm;
	}
	public String getOp() {
		return op;
	}
	public void setOp(String op) {
		this.op = op;
	}
	public String getBmTypeTerm() {
		return bmTypeTerm;
	}
	public void setBmTypeTerm(String bmTypeTerm) {
		this.bmTypeTerm = bmTypeTerm;
	}
	public String getEmployeeIdTerm() {
		return employeeIdTerm;
	}
	public void setEmployeeIdTerm(String employeeIdTerm) {
		this.employeeIdTerm = employeeIdTerm;
	}
	public String getUserOrganIdTerm() {
		return userOrganIdTerm;
	}
	public void setUserOrganIdTerm(String userOrganIdTerm) {
		this.userOrganIdTerm = userOrganIdTerm;
	}
	
}
