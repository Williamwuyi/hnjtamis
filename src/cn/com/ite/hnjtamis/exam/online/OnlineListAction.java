package cn.com.ite.hnjtamis.exam.online;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.springframework.beans.BeanUtils;

import cn.com.ite.eap2.common.utils.CharsetSwitchUtil;
import cn.com.ite.eap2.common.utils.DateUtils;
import cn.com.ite.eap2.common.utils.NumericUtils;
import cn.com.ite.eap2.core.spring.SpringContextUtil;
import cn.com.ite.eap2.core.struts2.AbstractListAction;
import cn.com.ite.eap2.core.struts2.ServletContent;
import cn.com.ite.eap2.exception.EapException;
import cn.com.ite.eap2.module.power.login.UserSession;
import cn.com.ite.hnjtamis.baseinfo.baseSignIn.BaseSignInService;
import cn.com.ite.hnjtamis.common.ExamVariable;
import cn.com.ite.hnjtamis.common.StaticVariable;
import cn.com.ite.hnjtamis.exam.base.theme.ThemeService;
import cn.com.ite.hnjtamis.exam.base.theme.form.ThemeFkauditForm;
import cn.com.ite.hnjtamis.exam.examControl.MoniExamControlMain;
import cn.com.ite.hnjtamis.exam.exampaper.EmployeeMoniService;
import cn.com.ite.hnjtamis.exam.exampaper.ExampaperService;
import cn.com.ite.hnjtamis.exam.exampaper.OnlineMoniService;
import cn.com.ite.hnjtamis.exam.exampaper.form.ExamNameForm;
import cn.com.ite.hnjtamis.exam.exampaper.form.ExamUserForm;
import cn.com.ite.hnjtamis.exam.exampaper.jsonFormat.ExamTestpaperAnswerJsonForm;
import cn.com.ite.hnjtamis.exam.exampaper.jsonFormat.ExamTestpaperJsonForm;
import cn.com.ite.hnjtamis.exam.exampaper.jsonFormat.ExamUserInticketMap;
import cn.com.ite.hnjtamis.exam.hibernatemap.Exam;
import cn.com.ite.hnjtamis.exam.hibernatemap.ExamTestpaper;
import cn.com.ite.hnjtamis.exam.hibernatemap.ExamTestpaperAnswerkey;
import cn.com.ite.hnjtamis.exam.hibernatemap.ExamTestpaperTheme;
import cn.com.ite.hnjtamis.exam.hibernatemap.ExamUserAnswerkey;
import cn.com.ite.hnjtamis.exam.hibernatemap.ExamUserTestpaper;
import cn.com.ite.hnjtamis.exam.hibernatemap.Theme;
import cn.com.ite.hnjtamis.exam.hibernatemap.ThemeFkaudit;
import cn.com.ite.hnjtamis.exam.hibernatemap.ThemeType;
import cn.com.ite.hnjtamis.exam.testpaper.TestpaperService;
import cn.com.ite.hnjtamis.exam.testpaper.form.TestpaperThemeForm;
import cn.com.ite.hnjtamis.personal.mainEx.UserDefineOption;

/**
 *
 * <p>Title cn.com.ite.hnjtamis.exam.online.OnlineListAction</p>
 * <p>Description 在线考试模拟考试取数</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2015</p>
 * @author 朱健
 * @create time: 2015年5月7日 下午2:56:36
 * @version 1.0
 * 
 * @modified records:
 */
public class OnlineListAction extends AbstractListAction implements ServletRequestAware{
 
	private static final long serialVersionUID = -8156465883315201858L;

	private HttpServletRequest request;
	
	private String id;

	private String rsContent;
	
	private String submitType;
	
	private String no;
	
	private String ans;
	
	private String rightAns;
	
	private String isHidden;
	
	private int currIndex;
	
	private String inticket;//准考证
	
	private String idNumber;//准考证
	
	private String pwd;
	
	private String relationType;//关联类型
	
	private String relationId;//关联ID
	
	private String employeeId;//员工ID
	
	private List<ExamUserForm> list = new ArrayList<ExamUserForm>();
	
	private String requestHttpUrl;
	
	private OnlineMoniService onlineMoniService;
	
	private EmployeeMoniService employeeMoniService;
	
	private String examName;
	
	private String opty;
	
	private int signIncount;
	
	List<TestpaperThemeForm> themelist = null;
	
	private String qtype;
	
	private String ny;
	
	private String showSign;
	
	private Theme fktheme;
	
	private List<ThemeFkauditForm> themeFkauditFormList;

	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
		
	}
	
	public String showMoniThemelist() throws Exception{
		UserSession usersess = (UserSession)ServletContent.getSession().get(StaticVariable.USERSESSION);
		if(usersess == null){
			this.setMsg("SESSION　信息已失效，请关闭浏览器并重新登陆！");
			return "showMainEx";
	 	}
		String themeBankId = request.getParameter("themeBankId");
		String examName = request.getParameter("examName");
		ny = request.getParameter("ny");
		examName = StaticVariable.banksNameMap.get(themeBankId);
		TestpaperService testpaperService = (TestpaperService)SpringContextUtil.getBean("testpaperService");
		Map paramMap = new HashMap();
		paramMap.put("themeBankIds", themeBankId);
		paramMap.put("examType", "20,40");
		if(showSign!=null && !"".equals(showSign)){
			paramMap.put("showSign", "1");
			paramMap.put("employeeId", usersess.getEmployeeId());
			
		}
		themelist = testpaperService.getHandAddThemeList(null,paramMap);
		request.setAttribute("themeBankId", themeBankId);
		request.setAttribute("examName", examName);
		request.setAttribute("themelist", themelist);
		request.setAttribute("qtype", qtype);
		request.setAttribute("ny", ny);
		StaticVariable.nowThemeBankInIndexMap.put(usersess.getAccount(),themeBankId);
		UserDefineOption.saveNowThemeBank(request,usersess.getAccount(),themeBankId);
		
		BaseSignInService baseSignInService = (BaseSignInService)SpringContextUtil.getBean("baseSignInService");
		request.setAttribute("signIncount", baseSignInService.getSignInCount(
				usersess.getEmployeeId() == null ? usersess.getAccount() : usersess.getEmployeeId(), usersess.getAccount()));
		return "showMoniThemelist";
	}
	
	
	/**
	 * 查询考生信息
	 * @author zhujian
	 * @description
	 * @return
	 * @throws Exception
	 * @modified
	 */
	public String list()throws Exception{
		list = new ArrayList();
		if(inticket!=null || idNumber!=null){
			Map term = new HashMap();
			term.put("inticket", inticket);
			term.put("idNumber", idNumber);
			List<ExamUserTestpaper> userExamlist = this.getService().queryData("queryUserExamHql", term, new HashMap());
			for(int i=0;i<userExamlist.size();i++){
				ExamUserTestpaper examUserTestpaper = (ExamUserTestpaper)userExamlist.get(i);
				ExamUserForm examUserForm = new ExamUserForm();
				BeanUtils.copyProperties(examUserTestpaper, examUserForm);
				examUserForm.setTestpaperScote(examUserTestpaper.getExamTestpaper()!=null ? examUserTestpaper.getExamTestpaper().getTotalScore() : null);
				examUserForm.setExamName(examUserTestpaper.getExam().getExamName());
				examUserForm.setScote(examUserTestpaper.getScote()==null ? examUserTestpaper.getFristScote() : examUserTestpaper.getScote());
				list.add(examUserForm);
			}
		}
		return "list";
	}
	
	public String exam(){
		UserSession usersess = (UserSession)ServletContent.getSession().get(StaticVariable.USERSESSION);
		List examList = ExamUserInticketMap.getNowDayExamNamesList();
		request.setAttribute("examList", examList);
		if(usersess == null){
			requestHttpUrl = ExamVariable.getRequestUrl(request);
			request.setAttribute("requestHttpUrl", requestHttpUrl);
	 	}else{
	 		requestHttpUrl = ExamVariable.getRequestUrl(request);
			request.setAttribute("requestHttpUrl", requestHttpUrl);
	 	}
		return "onlineExam";
	}
	
	public String findExamInticke(){
		String[] value = ExamUserInticketMap.getUserAndInticket(inticket, pwd, id);
		if(value==null){
			rsContent="{code:'00002',message:'未根据您录入的用户名或身份证检索到准考证信息！'}";
		}else{
			rsContent="{code:'00001',message:'检查到准考证！',result:{'loginInticket':'"+value[0]+"','loginPassword':'"+value[1]+"'}}";
		}
		return "rsContent";
	}
	
	
	public String findExam(){
		List<ExamNameForm> list = ExamUserInticketMap.getNowDayExamNamesListByAccountOrId(inticket);
		if(list==null || list.size()==0){
			rsContent="{code:'00002',message:'没有找到对应的考试！'}";
		}else{
			rsContent="{code:'00001',message:'检查到对应的考试！',result:{'examlist':[";
			for(int i=0;i<list.size();i++){
				ExamNameForm examNameForm = list.get(i);
				rsContent+="{'value':'"+examNameForm.getExamId()+"',";
				rsContent+="'text':'"+examNameForm.getExamName()+"'},";
			}
			rsContent = rsContent.substring(0,rsContent.length()-1);
			rsContent+="]}}";
		}
		return "rsContent";
	}
	
	/**
	 *
	 * @author zhujian
	 * @description
	 * @return
	 * @modified
	 */
	public String examTestpaper(){
		if(employeeId!=null && "sessionUser".equals(employeeId)){
			UserSession usersess = (UserSession)ServletContent.getSession().get(StaticVariable.USERSESSION);
			if(usersess == null){
				this.setMsg("SESSION　信息已失效，请关闭浏览器并重新登陆！");
				return "onlineExamMoni";
		 	}
			employeeId = usersess.getEmployeeId();
		}
		Map term = new HashMap();
		term.put("relationId", relationId);
		term.put("relationType", relationType);
		term.put("employeeId", employeeId);
		List<ExamUserTestpaper> userExamlist = this.getService().queryData("queryExamUserTestpaperInRelationHql", term, new HashMap());
		for(int i=0;i<userExamlist.size();i++){
			ExamUserTestpaper examUserTestpaper = (ExamUserTestpaper)userExamlist.get(i);
			id = examUserTestpaper.getUserTestpaperId();
		}
		request.setAttribute("id", id);
		return "onlineExamMoni";
	}
	
	
	/**
	 *
	 * @author zhujian
	 * @description 首页模拟试题打开页面
	 * @return
	 * @throws Exception
	 * @modified
	 */
	public String examIndexUserMoni()throws Exception{
		String employeeName = request.getParameter("employeeName");
		String themeBankId = request.getParameter("themeBankId");
		String cleanUserAns = request.getParameter("clean");
		//String isCreate = request.getParameter("isCreate");
		String ajaxType = request.getParameter("ajaxType");
		String examTitle = request.getParameter("examTitle");
		if(employeeId==null||"".equals(employeeId)||"null".equals(employeeId)){
			UserSession usersess = (UserSession)ServletContent.getSession().get(StaticVariable.USERSESSION);
			if(usersess == null){
				this.setMsg("SESSION　信息已失效，请关闭浏览器并重新登陆！");
				return "showMainEx";
		 	}
			employeeId = usersess.getEmployeeId();
			employeeName = usersess.getEmployeeName();
		}
		relationId = themeBankId;//+"@"+DateUtils.convertDateToStr(new Date(),"yyyyMMddHHmmss");
		if("mocs".equals(relationType)){
			UserSession usersess = (UserSession)ServletContent.getSession().get(StaticVariable.USERSESSION);
			if(usersess == null){
				this.setMsg("SESSION　信息已失效，请关闭浏览器并重新登陆！");
				return "showMainEx";
		 	}
			String typeId = request.getParameter("typeId");
			themeBankId = StaticVariable.moniCsBanksMap.get(usersess.getAccount()+"@"+typeId);
			examName = StaticVariable.banksNameMap.get(typeId+"@mk");
			relationId = typeId+"@"+usersess.getAccount()+"@"+DateUtils.convertDateToStr(new Date(),"yyyyMMdd");
		}else{
			examTitle = StaticVariable.banksNameMap.get(themeBankId);
		}
		
		String tabid = request.getParameter("tabid");
		request.setAttribute("tabid", tabid);	
		
		
		String choutiParam = request.getParameter("choutiParam");
		String addExamType = request.getParameter("addExamType");//试卷生成方式  10-同试卷打乱  20-同试卷同顺序 30-各考生按模版随机抽题
		id = employeeMoniService.addOrGetMoniIndexExam( relationId, relationType, employeeId, employeeName,
			 cleanUserAns, examTitle, choutiParam, addExamType, themeBankId);
		request.setAttribute("id", id);
		
		if("init".equals(ajaxType)){
			return "examIndexUserMoniInit";
		}else{
			return "onlineExamMoni";
		}
		
	}
	
	/**
	 *
	 * @author zhujian
	 * @description 首页模拟试题打开页面(快速做题)
	 * @return
	 * @throws Exception
	 * @modified
	 */
	public String examFastIndexUserMoni()throws Exception{
		String employeeName = request.getParameter("employeeName");
		String themeBankId = request.getParameter("themeBankId");
		String cleanUserAns = request.getParameter("clean");
		//String isCreate = request.getParameter("isCreate");
		String ajaxType = request.getParameter("ajaxType");
		ny = request.getParameter("ny");
		if(employeeId==null||"".equals(employeeId)||"null".equals(employeeId)){
			UserSession usersess = (UserSession)ServletContent.getSession().get(StaticVariable.USERSESSION);
			if(usersess == null){
				this.setMsg("SESSION　信息已失效，请关闭浏览器并重新登陆！");
				return "onlineExamMoni";
		 	}
			employeeId = usersess.getEmployeeId();
			employeeName = usersess.getEmployeeName();
			
			BaseSignInService baseSignInService = (BaseSignInService)SpringContextUtil.getBean("baseSignInService");
			request.setAttribute("signIncount", baseSignInService.getSignInCount(
					usersess.getEmployeeId() == null ? usersess.getAccount() : usersess.getEmployeeId(), usersess.getAccount()));
			
			if(!"mocs".equals(relationType)){
				StaticVariable.nowThemeBankInIndexMap.put(usersess.getAccount(),themeBankId);
				UserDefineOption.saveNowThemeBank(request,usersess.getAccount(),themeBankId);
			}
		}
		examName = request.getParameter("examName");
		relationId = themeBankId;//+"@"+DateUtils.convertDateToStr(new Date(),"yyyyMMddHHmmss");
		if("mocs".equals(relationType)){
			UserSession usersess = (UserSession)ServletContent.getSession().get(StaticVariable.USERSESSION);
			if(usersess == null){
				this.setMsg("SESSION　信息已失效，请关闭浏览器并重新登陆！");
				return "onlineExamMoni";
		 	}
			String typeId = request.getParameter("typeId");
			themeBankId = StaticVariable.moniCsBanksMap.get(usersess.getAccount()+"@"+typeId);
			examName = StaticVariable.banksNameMap.get(typeId+"@mk");
			relationId = typeId+"@"+usersess.getAccount()+"@"+DateUtils.convertDateToStr(new Date(),"yyyyMMdd");
		}else{
			examName = StaticVariable.banksNameMap.get(themeBankId);
		}
		
		//String tabid = request.getParameter("tabid");
		//request.setAttribute("tabid", tabid);	
		
		
		String choutiParam = request.getParameter("choutiParam");
		String addExamType = request.getParameter("addExamType");//试卷生成方式  10-同试卷打乱  20-同试卷同顺序 30-各考生按模版随机抽题
		id = employeeMoniService.addOrGetMoniIndexExam( relationId, relationType, employeeId, employeeName,
			 cleanUserAns, examName, choutiParam, addExamType, themeBankId);
		request.setAttribute("id", id);
		request.setAttribute("examName", examName);
		request.setAttribute("relationType", relationType);
		request.setAttribute("ny", ny);
		return "onlineFastExamMoni";
	}
	
	
	public String fkThemeInExamThemeShow()throws Exception{
		UserSession usersess = (UserSession)ServletContent.getSession().get(StaticVariable.USERSESSION);
		if(usersess == null){
			this.setMsg("SESSION　信息已失效，请关闭浏览器并重新登陆！");
			return "fkThemeShow";
	 	}
		request.setAttribute("id", id);
		ExamTestpaperTheme examTestpaperTheme = (ExamTestpaperTheme)employeeMoniService.findDataByKey(id, ExamTestpaperTheme.class);
		if(examTestpaperTheme!=null){
			fktheme = (Theme)employeeMoniService.findDataByKey(examTestpaperTheme.getThemeId(), Theme.class);
			themeFkauditFormList = new ArrayList<ThemeFkauditForm>();
			if(fktheme==null){
				this.setMsg("没有找到对应的试题！");
			}else if(fktheme.getLastFkState()!=null && !"10".equals(fktheme.getLastFkState())){
				this.setMsg("该试题已经进行了反馈！");
				ThemeService themeService = (ThemeService)SpringContextUtil.getBean("themeService");
				Map term = new HashMap();
				term.put("themeId", fktheme.getThemeId());
				term.put("state", "10,20");
				List<ThemeFkaudit> fkAuditlist = themeService.queryData("queryThemeFkauditHql", term, null);
				for(int i=0;i<fkAuditlist.size();i++){
					ThemeFkaudit themeFkaudit = fkAuditlist.get(i);
					ThemeFkauditForm themeFkauditForm = new ThemeFkauditForm();
					BeanUtils.copyProperties(themeFkaudit,themeFkauditForm);
					themeFkauditFormList.add(themeFkauditForm);
				}
			}else{
				fktheme.setLastFkState("10");
			}
			request.setAttribute("fktheme", fktheme);
		}else{
			this.setMsg("没有找到对应的试题！");
		}
		return "fkThemeShow";
	}
	
	
	public String fkThemeShow()throws Exception{
		UserSession usersess = (UserSession)ServletContent.getSession().get(StaticVariable.USERSESSION);
		if(usersess == null){
			this.setMsg("SESSION　信息已失效，请关闭浏览器并重新登陆！");
			return "fkThemeShow";
	 	}
		request.setAttribute("id", id);
		fktheme = (Theme)employeeMoniService.findDataByKey(id, Theme.class);
		themeFkauditFormList = new ArrayList<ThemeFkauditForm>();
		if(fktheme==null){
			this.setMsg("没有找到对应的试题！");
		}else if(fktheme.getLastFkState()!=null && !"10".equals(fktheme.getLastFkState())){
			this.setMsg("该试题已经进行了反馈！");
			ThemeService themeService = (ThemeService)SpringContextUtil.getBean("themeService");
			Map term = new HashMap();
			term.put("themeId", fktheme.getThemeId());
			term.put("state", "10,20");
			List<ThemeFkaudit> fkAuditlist = themeService.queryData("queryThemeFkauditHql", term, null);
			for(int i=0;i<fkAuditlist.size();i++){
				ThemeFkaudit themeFkaudit = fkAuditlist.get(i);
				ThemeFkauditForm themeFkauditForm = new ThemeFkauditForm();
				BeanUtils.copyProperties(themeFkaudit,themeFkauditForm);
				themeFkauditFormList.add(themeFkauditForm);
			}
		}else{
			fktheme.setLastFkState("10");
		}
		request.setAttribute("fktheme", fktheme);
		return "fkThemeShow";
	}


	
	
	/**
	 *
	 * @author zhujian
	 * @description 模拟试题打开页面
	 * @return
	 * @throws Exception
	 * @modified
	 */
	public String examMoni()throws Exception{
		request.setAttribute("id", id);
		String cleanUserAns = request.getParameter("clean");
		String nowTime = DateUtils.convertDateToStr(new Date(),"yyyy-MM-dd HH:mm:ss");
		if("1".equals(cleanUserAns)){ //清理答案
			employeeMoniService.saveAndCleanExamPaperByUserTestpaperId(id);
			
			ExamUserTestpaper examUserTestpaper = (ExamUserTestpaper)this.getService().findDataByKey(id, ExamUserTestpaper.class);
			examUserTestpaper.setSubTime(null);
			examUserTestpaper.setFristScote(null);
			examUserTestpaper.setScote(null);
			examUserTestpaper.setInTime(nowTime);
			examUserTestpaper.setOutTime(null);
			
			examUserTestpaper.getExamTestpaper().setInTime(nowTime);
			examUserTestpaper.getExamTestpaper().setOutTime(null);
			examUserTestpaper.getExamTestpaper().setSubTime(null);
			
			this.getService().saveOld(examUserTestpaper);
		}else{
			String addExampaper = request.getParameter("addExam");
			if("1".equals(addExampaper)){
				String addExamType = request.getParameter("addExamType");//试卷生成方式  10-同试卷打乱  20-同试卷同顺序 30-各考生按模版随机抽题
				int examPaperType = -1;
				if(addExamType!=null && !"".equals(addExamType) && !"null".equals(addExamType)){
					try{
						examPaperType = Integer.parseInt(addExamType);
					}catch(Exception e){
						e.printStackTrace();
						examPaperType = -1;
					}
				}
				
				onlineMoniService.addExamPaperByUserTestpaperId(id,examPaperType,null);
				ExamUserTestpaper examUserTestpaper = (ExamUserTestpaper)this.getService().findDataByKey(id, ExamUserTestpaper.class);
				if(examUserTestpaper!=null){
					examUserTestpaper.setSubTime(null);
					examUserTestpaper.setFristScote(null);
					examUserTestpaper.setScote(null);
					examUserTestpaper.setOutTime(null);
					examUserTestpaper.getExamTestpaper().setInTime(nowTime);
					examUserTestpaper.getExamTestpaper().setOutTime(null);
					examUserTestpaper.getExamTestpaper().setSubTime(null);
					examUserTestpaper.setInTime(nowTime);
					this.getService().saveOld(examUserTestpaper);
				}
			}
		}
		return "onlineExamMoni";
	}
	
	
	/**
	 * 获取用户信息
	 * @author zhujian
	 * @description
	 * @return
	 * @modified
	 */
	public String getUser(){
		rsContent = null;
		ExamUserTestpaper examUserTestpaper = (ExamUserTestpaper)this.getService().findDataByKey(id, ExamUserTestpaper.class);
		if(examUserTestpaper!=null && examUserTestpaper.getSubTime()!=null && !"".equals(examUserTestpaper.getSubTime())){
			rsContent="{code:'00011',message:'本次考试已交卷！'}";
		}else 
			if(examUserTestpaper!=null){
			Exam exam = examUserTestpaper.getExam();
			String examTestpaperStr = "{code:'00001',message:'返回成功！',result:";
			examTestpaperStr+="{examId:'"+exam.getExamId()+"',";
			examTestpaperStr+="examName:'"+exam.getExamName()+"',";
			examTestpaperStr += "userTestPaperId:'"+examUserTestpaper.getUserTestpaperId()+"',";
			examTestpaperStr += "testPaperId:'"+examUserTestpaper.getExamTestpaper().getExamTestpaperId()+"',";
			//examTestpaperStr+="userOrganId:'"+examUserTestpaper.getUserOrganId()+"',";//所属机构ID
			examTestpaperStr+="userOrganName:'"+examUserTestpaper.getUserOrganName()+"',";//所属机构
			//examTestpaperStr+="userDeptId:'"+examUserTestpaper.getUserDeptId()+"',";//所属部门ID
			examTestpaperStr+="userDeptName:'"+examUserTestpaper.getUserDeptName()+"',";//所属部门
			//examTestpaperStr+="userGroupId:'"+examUserTestpaper.getUserGroupId()+"',";//所属班组ID
			examTestpaperStr+="userGroupName:'"+examUserTestpaper.getUserGroupName()+"',";//所属班组
			//examTestpaperStr+="postId:'"+examUserTestpaper.getPostId()+"',";//所属岗位ID
			examTestpaperStr+="postName:'"+examUserTestpaper.getPostName()+"',";//所属岗位
			examTestpaperStr+="userName:'"+examUserTestpaper.getUserName()+"',";//姓名
			examTestpaperStr+="userSex:'"+examUserTestpaper.getUserSex()+"',";//性别  1-男  2-女
			examTestpaperStr+="inticket:'"+examUserTestpaper.getInticket()+"',";//准考证号
			examTestpaperStr+="idNumber:'"+examUserTestpaper.getIdNumber()+"',";//身份证号
			examTestpaperStr+="userBirthday:'"+examUserTestpaper.getUserBirthday()+"',";//出生年月
			examTestpaperStr+="userNation:'"+examUserTestpaper.getUserNation()+"',";//民族
			//examTestpaperStr+="userAddr:'"+examUserTestpaper.getUserAddr()+"',";//住址
			//examTestpaperStr+="userPhone:'"+examUserTestpaper.getUserPhone()+"',";//联系电话
			//examTestpaperStr+="userInfo:'"+examUserTestpaper.getUserInfo()+"',";//考生信息
			examTestpaperStr+="examPassword:'"+examUserTestpaper.getExamPassword()+"',";//考试密码
			//examTestpaperStr+="examPaperType:'"+examUserTestpaper.getExamPaperType()+"',";//考试方式 10-正式（同试卷打乱） 11-正式（各考生随机抽题） 20-模拟 30-练习
			examTestpaperStr+="inTime:'"+examUserTestpaper.getInTime()+"',";//入场时间
			examTestpaperStr+="outTime:'"+examUserTestpaper.getOutTime()+"',";//离场时间
			//examTestpaperStr+="isLocked:'"+examUserTestpaper.getIsLocked()+"',";//是否锁定 0-未锁定  5-锁定
			examTestpaperStr+="subTime:'"+examUserTestpaper.getSubTime()+"',";///交卷时间
			examTestpaperStr+="loginUrl:'"+examUserTestpaper.getLoginUrl()+"',";///登陆地址
			//examTestpaperStr+="examRootName:'"+examUserTestpaper.getExamRootName()+"',";//考点名称
			//examTestpaperStr+="examRootPlace:'"+examUserTestpaper.getExamRootPlace()+"',";//考点地点
			//examTestpaperStr+="seatNum:'"+examUserTestpaper.getSeatNum()+"',";//座位号
			examTestpaperStr+="isIdNumberLogin:'"+examUserTestpaper.getIsIdNumberLogin()+"',";//是否允许身份证登陆 0-允许  1-不允许
			//examTestpaperStr+="examPaperInit:'"+examUserTestpaper.getExamPaperInit()+"',";//试卷是否初始化 10-否 11-初始化失败 20-是  21-需要进行重置
			examTestpaperStr+="state:'"+examUserTestpaper.getState()+"',";//状态 5:生成10:待考15:已考完 16-作弊 20:已阅卷 25 打回 30-发布
			examTestpaperStr+="organName:'"+examUserTestpaper.getOrganName()+"',";//机构名
			//examTestpaperStr+="organId:'"+examUserTestpaper.getOrganId()+"',";//机构编号
			//examTestpaperStr+="employeeId:'"+examUserTestpaper.getEmployeeId()+"',";//人员ID
			//examTestpaperStr+="employeeName:'"+examUserTestpaper.getEmployeeName()+"',";//人员
			if(exam.getExamStartTime()==null || "".equals(exam.getExamStartTime())){
				Date nowDate = new Date();
				Date endDate = new Date();
				endDate = new Date(endDate.getTime()+1000*60*60*3);
				examTestpaperStr+="examStartTime:'"+DateUtils.convertDateToStr(nowDate,"yyyy-MM-dd HH:mm:ss")+"',";//开始时间
				examTestpaperStr+="examEndTime:'"+DateUtils.convertDateToStr(endDate,"yyyy-MM-dd HH:mm:ss")+"',";//结束时间
			}else{
				examTestpaperStr+="examStartTime:'"+exam.getExamStartTime()+"',";//开始时间
				examTestpaperStr+="examEndTime:'"+exam.getExamEndTime()+"',";//结束时间
			}
			
			examTestpaperStr+="examBanTime:'"+exam.getBanTime()+"',";//开考后禁止进入时间
			examTestpaperStr+="examBeforeTime:'"+exam.getBeforeTime()+"'";//提前进入时间
			examTestpaperStr+="}";
			examTestpaperStr+="}";
			
			rsContent = examTestpaperStr;
		}else{
			rsContent="{code:'00002',message:'未检索到该用户！'}";
		}
		return "rsContent";
	}
	
	
	/**
	 *
	 * @author zhujian
	 * @description 获取服务器时间
	 * @return
	 * @modified
	 */
	public String gettime(){
		rsContent="{code:'00001',message:'返回成功！',result:{date:'"+DateUtils.convertDateToStr(new Date(),"yyyy-MM-dd HH:mm:ss")+"'}}";
		return "rsContent";
	}
	
	
	/**
	 *
	 * @author zhujian
	 * @description 获取考试题目
	 * @return
	 * @modified
	 */
	public String gettitle(){
		rsContent = MoniExamControlMain.getMoniExamTitleInFile(request, id);//先从预文件里面读取
		if(rsContent == null){
			ExamTestpaper examTestpaper = (ExamTestpaper)this.getService().findDataByKey(id, ExamTestpaper.class);
			if(examTestpaper!=null){
				ExampaperService exampaperService = (ExampaperService)this.getService();
				Map term = new HashMap();
				term.put("examTestpaperId", examTestpaper.getExamTestpaperId());
				List themeTypeList = this.getService().queryData("queryTypeInExampaperHql", term, new HashMap());
				
				List<ExamTestpaperTheme> examTestpaperThemelist = this.getService().queryData("queryExamTestpaperThemeByExamTestpaperIdHql", term, new HashMap());
				Map themeAnsMap = exampaperService.getExamTestpaperAnswerkeyByTestpaperId(examTestpaper.getExamTestpaperId());
				
				
				String examTestpaperStr = "{code:'00001',message:'返回成功！',result:";
				examTestpaperStr+=ExamTestpaperJsonForm.getJsonStrInExamTestpaper(examTestpaper, 
						themeTypeList,examTestpaperThemelist,themeAnsMap);
				examTestpaperStr+="}";
				try {
					this.getService().saveOld(examTestpaper);
				} catch (EapException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				rsContent = examTestpaperStr;
				MoniExamControlMain.saveMoniExamTitleToFile(request, id, examTestpaperStr);
			}else{
				rsContent="{code:'00003',message:'未检索到试卷！'}";
			}
		}
		return "rsContent";
	}
	
	
	/**
	 *
	 * @author zhujian
	 * @description 保存考试答案
	 * @return
	 * @modified
	 */
	public String saveans(){
		rsContent = null;
		try{
			String nowTime = DateUtils.convertDateToStr(new Date(),"yyyy-MM-dd HH:mm:ss");
			
			ExampaperService exampaperService = (ExampaperService)this.getService();
			Map<String,String> ansMap = new HashMap<String,String>();
			String[] noArr = no.split("@\\$@");
			String[] ansArr = ans.split("@\\$@");
			for(int i=0;i<noArr.length;i++){
				String index = noArr[i];
				if(index!=null && !"".equals(noArr[i])){
					//System.out.println((Integer.parseInt(index)-1)+""+"     " +ansArr[i]);
					ansMap.put((Integer.parseInt(index))+"", ansArr[i]);
				}
			}
			ExamUserTestpaper examUserTestpaper = (ExamUserTestpaper)this.getService().findDataByKey(id, ExamUserTestpaper.class);
			ExamTestpaper examTestpaper = examUserTestpaper.getExamTestpaper();
			double sumscore = 0.0d;
			double score = 0.0d;
			if(examTestpaper!=null && examTestpaper.getExamTestpaperThemes()!=null && examTestpaper.getExamTestpaperThemes().size()>0){
				 Map term = new HashMap();
				 term.put("examTestpaperId", examTestpaper.getExamTestpaperId());
				 List themeTypeList = this.getService().queryData("queryTypeInExampaperHql", term, new HashMap());
				 List<ExamTestpaperTheme> themeList  = this.getService().queryData("queryExamTestpaperThemeByExamTestpaperIdHql", term, new HashMap());
				 ExamTestpaperJsonForm.sortThemelist(themeList);//按顺序排序
				 for(int kk = 0;kk<themeTypeList.size();kk++){
					    ThemeType themeType = (ThemeType)themeTypeList.get(kk);
						for(int i=0;i<themeList.size();i++){
							ExamTestpaperTheme theme = themeList.get(i);
							if(themeType.getThemeTypeId().equals(theme.getThemeTypeId())){
								sumscore += theme.getDefaultScore()!=null ?theme.getDefaultScore().doubleValue() : 0.0d;
								//System.out.println("=================================="+theme.getRandomSortnum().intValue());
								//System.out.println(theme.getThemeName());
								String sortIndex = theme.getRandomSortnum().intValue()+"";
								String ansStr = ansMap.get(sortIndex);
								
								//状态 5:生成10:待考15:已考完 16-作弊 20:已阅卷 25 打回 30-发布
								if("submit".equals(submitType)){
									theme.setState(15);
								}else{
									theme.setState(10);
								}
								if(ansStr!=null){
										if(theme.getExamUserAnswerkeies()!=null && theme.getExamUserAnswerkeies().size()>0){
											theme.getExamUserAnswerkeies().clear();
										}
										if(!"".equals(ansStr)){
											String[] themeAnsArr = ansStr.split("#\\*#");
											String type = themeType!=null ? themeType.getThemeType() : null;
											for(int k=0;k<themeAnsArr.length;k++){
												ExamUserAnswerkey examUserAnswerkey = new ExamUserAnswerkey();
												if("5".equals(type) || "10".equals(type) || "25".equals(type)){
													examUserAnswerkey.setAnswerkeyValue(themeAnsArr[k]);
												}else{
													examUserAnswerkey.setAnswerkeyValue(CharsetSwitchUtil.decode(themeAnsArr[k]));
												}
								    			//System.out.println(examUserAnswerkey.getAnswerkeyValue());
								    			examUserAnswerkey.setSortNum(k);
								    			examUserAnswerkey.setState(5);
								    			examUserAnswerkey.setExamTestpaperTheme(theme);
								    			examUserAnswerkey.setCreationDate(DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));//创建时间
								    			examUserAnswerkey.setOrganId(examTestpaper.getOrganId());
								    			examUserAnswerkey.setOrganName(examTestpaper.getOrganName());
								    			theme.getExamUserAnswerkeies().add(examUserAnswerkey);
											}
										}
								}
							}
						}
				 }
				 score = exampaperService.saveAutoMarkPeopleInThemeType(themeTypeList,themeList,true);
			}
			
			if("submit".equals(submitType)){
				examUserTestpaper.setSubTime(nowTime);
				examUserTestpaper.setFristScote(score);
				examUserTestpaper.getExamTestpaper().setExamScore(score);
				examUserTestpaper.getExamTestpaper().setSubTime(nowTime);
				examUserTestpaper.getExamTestpaper().setExamFileUserAnswerkey(no+"#$#"+ans);
				examUserTestpaper.setState("30");//状态 5:生成10:待考15:已考完 16-作弊 20:已阅卷 25 打回 30-发布
				
				if("TEST_AFTER".equals(examUserTestpaper.getRelationType()) 
						&& examUserTestpaper.getRelationId()!=null && !"".equals(examUserTestpaper.getRelationId())){
					exampaperService.updatePersonalRateProgress(examUserTestpaper, examTestpaper, score);
				}
				
			}
			examUserTestpaper.setOutTime(nowTime);
			examUserTestpaper.getExamTestpaper().setOutTime(nowTime);
			exampaperService.saveOld(examUserTestpaper);
			
			String examTestpaperId = examTestpaper.getExamTestpaperId();
			
			
			//MoniExamControlMain.delMoniExamUserAnsFile(request, examTestpaperId);
			rsContent="{code:'00001',message:'返回成功！',score:'"+score+"',sumscore:'"+sumscore+"',dfl:'"+(sumscore>0? NumericUtils.round(score/sumscore*100,2) : "0.00")+"',examTestpaperId:'"+examTestpaperId+"'}";
			//System.out.println(rsContent);
		}catch(Exception e){
			rsContent="{code:'00005',message:'保存答案失败！'}";
			e.printStackTrace();
		}
		return "rsContent";
	}
	
	
	/**
	 *
	 * @author zhujian
	 * @description 获取服务器的考试答案
	 * @return
	 * @modified
	 */
	public String getansbak(){
		//rsContent = MoniExamControlMain.getMoniExamUserAnsInFile(request, id);//先从预文件里面读取
		//if(rsContent == null){
			try{
				String ans = "";
				String no = "";
				Map ThemeTypeMap = new HashMap();
				//ExamTestpaper examTestpaper = (ExamTestpaper)this.getService().findDataByKey(id, ExamTestpaper.class);
				Map term2 = new HashMap();
				term2.put("examTestpaperId", id);
				List<ExamTestpaperTheme> themeList  = this.getService().queryData("queryExamTestpaperThemeByExamTestpaperIdHql", term2, new HashMap());
				if(themeList!=null && themeList.size()>0){
					//if(examTestpaper!=null && examTestpaper.getExamTestpaperThemes()!=null && examTestpaper.getExamTestpaperThemes().size()>0){
						Map term = new HashMap();
						term.put("examTestpaperId", id);
						List themeTypeList = this.getService().queryData("queryTypeInExampaperHql", term, new HashMap());
						
						//List<ExamTestpaperTheme> themeList = examTestpaper.getExamTestpaperThemes();
						
						 
						Map<String,List<ExamUserAnswerkey>> ansMap = new HashMap<String,List<ExamUserAnswerkey>>();
						List<ExamUserAnswerkey> ansList2 = this.getService().queryData("queryExamUserAnswerkeyInETIdHql", term , null);
						if(ansList2!=null){
							for(int i=0;i<ansList2.size();i++){
								ExamUserAnswerkey examUserAnswerkey = (ExamUserAnswerkey)ansList2.get(i);
								String ansKey = examUserAnswerkey.getExamTestpaperTheme().getExamTestpaperThemeId();
								List<ExamUserAnswerkey> tmplist = ansMap.get(ansKey);
								if(tmplist==null)tmplist = new ArrayList<ExamUserAnswerkey>();
								tmplist.add(examUserAnswerkey);
								ansMap.put(ansKey,tmplist);
							}
						}
						
						
						 for(int kk = 0;kk<themeTypeList.size();kk++){
							    ThemeType themeType = (ThemeType)themeTypeList.get(kk);
								for(int i=0;i<themeList.size();i++){
									ExamTestpaperTheme theme = themeList.get(i);
									if(themeType.getThemeTypeId().equals(theme.getThemeTypeId())){
										List<ExamUserAnswerkey> ansList = ansMap.get(theme.getExamTestpaperThemeId());
										/*if(ansList == null){
											ansList = theme.getExamUserAnswerkeies();
										}*/
										if(ansList!=null && ansList.size()>0){
											String ansStr = "";
											String type = themeType!=null ? themeType.getThemeType() : null;
											for(int j=0;j<ansList.size();j++){
												if("5".equals(type) || "10".equals(type) || "25".equals(type)){
													ansStr+=ansList.get(j).getAnswerkeyValue()+"#*#";
												}else{
													ansStr+=CharsetSwitchUtil.encode(ansList.get(j).getAnswerkeyValue())+"#*#";
												}
											}
											if(ansStr.length()>0){
												ansStr = ansStr.substring(0,ansStr.length()-3);
											}else{
												ansStr = "null";
											}
											ans+=ansStr+"@$@";
											no+=(theme.getRandomSortnum())+"@$@";
										}
									}
								}
						 }
					//}
					if(ans.length()>0)ans = ans.substring(0,ans.length()-3);
					if(no.length()>0)no = no.substring(0,no.length()-3);
					
					rsContent="{code:'00001',message:'返回成功！',result:{ans:'"+ans+"',no:'"+no+"'}}";
					//MoniExamControlMain.saveMoniExamUserAnsToFile(request, id, rsContent);
				}else{
					rsContent="{code:'00003',message:'未检索到试卷！'}";
				}
				//System.out.println(rsContent);
			}catch(Exception e){
				rsContent="{code:'00005',message:'保存答案失败！'}";
				e.printStackTrace();
			}
		//}else{
			//System.out.println(rsContent);
		//}
		return "rsContent";
		
	}
	
	
	
	public String signInMoniTheme(){
		rsContent = null;
		try{
			Map term2 = new HashMap();
			term2.put("examTestpaperId", id);
			List<ExamTestpaperTheme> themeList  = this.getService().queryData("queryExamTestpaperThemeByExamTestpaperIdHql", term2, new HashMap());
			if(themeList!=null && themeList.size()>currIndex){
			    ExamTestpaperTheme examTestpaperTheme = (ExamTestpaperTheme)themeList.get(currIndex);
				if("add".equals(opty)){
					 if(examTestpaperTheme.getSignInTheme()==null || "".equals(examTestpaperTheme.getSignInTheme())
							 || "null".equals(examTestpaperTheme.getSignInTheme())){
						 examTestpaperTheme.setSignInTheme("10");
						 this.getService().saveOld(examTestpaperTheme);
						 rsContent="{code:'00001',message:'已收藏！'}";
					 }else{
						 rsContent="{code:'00005',message:'已收藏，收藏失败！'}";
					 }
				} else if("del".equals(opty)){
					if(!(examTestpaperTheme.getSignInTheme()==null || "".equals(examTestpaperTheme.getSignInTheme())
							 || "null".equals(examTestpaperTheme.getSignInTheme()))){
						 examTestpaperTheme.setSignInTheme(null);
						 this.getService().saveOld(examTestpaperTheme);
						 rsContent="{code:'00001',message:'取消收藏成功！'}";
					 }else{
						 rsContent="{code:'00005',message:'未收藏，取消失败！'}";
					 } 
				}else{
					rsContent="{code:'00005',message:'处理失败！'}";
				}
			   
				 
				 
			 }
		}catch(Exception e){
			rsContent="{code:'00005',message:'处理失败！'}";
			e.printStackTrace();
		}
		return "rsContent";
	}
	
	/**
	 *
	 * @author zhujian
	 * @description 获取正确答案
	 * @return
	 * @modified
	 */
	public String getAllAns(){
		rsContent = null;
		try{
			//ExamTestpaper examTestpaper = (ExamTestpaper)this.getService().findDataByKey(id, ExamTestpaper.class);
			Map term2 = new HashMap();
			term2.put("examTestpaperId", id);
			Map orderMap = new HashMap();
			orderMap.put("randomSortnum", true);
			List<ExamTestpaperTheme> themeList  = this.getService().queryData("queryExamTestpaperThemeByExamTestpaperIdHql", term2, orderMap);
			if(themeList!=null){
				List<ExamTestpaperAnswerkey> allRightAnsList = this.getService().queryData("queryRightAnsByExamTestpaperId", term2, orderMap);//examTestpaperTheme.getExamTestpaperAnswerkeies();
				Map<String,List<ExamTestpaperAnswerkey>> rightAnsMap = new HashMap<String,List<ExamTestpaperAnswerkey>>();
				for(int i=0;i<allRightAnsList.size();i++){
					ExamTestpaperAnswerkey examTestpaperAnswerkey = allRightAnsList.get(i);
					String key = examTestpaperAnswerkey.getExamTestpaperTheme().getExamTestpaperThemeId();
					List<ExamTestpaperAnswerkey> tmpllist= rightAnsMap.get(key);
					if(tmpllist == null){
						tmpllist = new ArrayList<ExamTestpaperAnswerkey>();
					}
					tmpllist.add(examTestpaperAnswerkey);
					rightAnsMap.put(key,tmpllist);
				}
				String examTestpaperStr = "{code:'00001',message:'返回成功！',result:[";
				for(int i=0;i<themeList.size();i++){
				    ExamTestpaperTheme examTestpaperTheme = (ExamTestpaperTheme)themeList.get(i);
				    examTestpaperStr+="{sort:"+i;
				    if(!(examTestpaperTheme.getSignInTheme()==null || "".equals(examTestpaperTheme.getSignInTheme())
							 || "null".equals(examTestpaperTheme.getSignInTheme()))){
				    	examTestpaperStr+=",sg:'t'";
				    }
					if(examTestpaperTheme!=null){
						List<ExamTestpaperAnswerkey> rightAnsList = rightAnsMap.get(examTestpaperTheme.getExamTestpaperThemeId());
						if(rightAnsList!=null && rightAnsList.size()>0){
							ExamTestpaperAnswerJsonForm examTestpaperAnswerJsonForm =new ExamTestpaperAnswerJsonForm();
							examTestpaperStr+=",rightAns:";
							examTestpaperStr+=examTestpaperAnswerJsonForm.getJsonStrInThemeRightAnswer(examTestpaperTheme,rightAnsList);
						}
					}
					examTestpaperStr+="},";
				}
				examTestpaperStr =examTestpaperStr.substring(0,examTestpaperStr.length()-1);
				examTestpaperStr+="]}";
				rsContent = examTestpaperStr;
			}else{
				rsContent="{code:'00003',message:'未检索到试卷！'}";
			}
		}catch(Exception e){
			rsContent="{code:'00005',message:'该题没有正确答案！'";
			e.printStackTrace();
		}
		//System.out.println(rsContent);
		//System.out.println(rsContent);
		return "rsContent";
		
	}
	
	
	/**
	 *
	 * @author zhujian
	 * @description 获取正确答案
	 * @return
	 * @modified
	 */
	public String getans(){
		rsContent = null;
		try{
			//ExamTestpaper examTestpaper = (ExamTestpaper)this.getService().findDataByKey(id, ExamTestpaper.class);
			Map term2 = new HashMap();
			term2.put("examTestpaperId", id);
			List<ExamTestpaperTheme> themeList  = this.getService().queryData("queryExamTestpaperThemeByExamTestpaperIdHql", term2, new HashMap());
			if(themeList!=null && themeList.size()>currIndex){
			    ExamTestpaperTheme examTestpaperTheme = (ExamTestpaperTheme)themeList.get(currIndex);
				if(examTestpaperTheme!=null){
					Map term = new HashMap();
					term.put("examTestpaperThemeId", examTestpaperTheme.getExamTestpaperThemeId());
					List<ExamTestpaperAnswerkey> rightAnsList = this.getService().queryData("queryRightExamTestpaperAnswerkeyByThemeId", term, new HashMap());//examTestpaperTheme.getExamTestpaperAnswerkeies();
					if(rightAnsList!=null && rightAnsList.size()>0){
						String examTestpaperStr = "{code:'00001',message:'返回成功！',result:";
						ExamTestpaperAnswerJsonForm examTestpaperAnswerJsonForm =new ExamTestpaperAnswerJsonForm();
						examTestpaperStr+=examTestpaperAnswerJsonForm.getJsonStrInThemeRightAnswer(examTestpaperTheme,rightAnsList);
						examTestpaperStr+="}";
						rsContent = examTestpaperStr;
					}else{
						rsContent="{code:'00004',message:'该题没有正确答案！'}";
					}
				}else{
					rsContent="{code:'00003',message:'未检索到试题！'}";
				}
			}else{
				rsContent="{code:'00003',message:'未检索到试卷！'}";
			}
		}catch(Exception e){
			rsContent="{code:'00005',message:'保存答案失败！'}";
			e.printStackTrace();
		}
		//System.out.println(rsContent);
		return "rsContent";
		
	}

	
	

	public String getNo() {
		return no;
	}


	public void setNo(String no) {
		this.no = no;
	}


	public String getAns() {
		return ans;
	}


	public void setAns(String ans) {
		this.ans = ans;
	}


	public String getSubmitType() {
		return submitType;
	}


	public void setSubmitType(String submitType) {
		this.submitType = submitType;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getRsContent() {
		return rsContent;
	}


	public void setRsContent(String rsContent) {
		this.rsContent = rsContent;
	}


	public String getIsHidden() {
		return isHidden;
	}


	public void setIsHidden(String isHidden) {
		this.isHidden = isHidden;
	}


	public String getInticket() {
		return inticket;
	}


	public void setInticket(String inticket) {
		this.inticket = inticket;
	}


	public String getIdNumber() {
		return idNumber;
	}


	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}


	public List<ExamUserForm> getList() {
		return list;
	}


	public void setList(List<ExamUserForm> list) {
		this.list = list;
	}


	public String getRelationType() {
		return relationType;
	}


	public void setRelationType(String relationType) {
		this.relationType = relationType;
	}


	public String getRelationId() {
		return relationId;
	}


	public void setRelationId(String relationId) {
		this.relationId = relationId;
	}


	public String getEmployeeId() {
		return employeeId;
	}


	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}


	public String getRightAns() {
		return rightAns;
	}


	public void setRightAns(String rightAns) {
		this.rightAns = rightAns;
	}


	public int getCurrIndex() {
		return currIndex;
	}


	public void setCurrIndex(int currIndex) {
		this.currIndex = currIndex;
	}


	public String getRequestHttpUrl() {
		return requestHttpUrl;
	}


	public void setRequestHttpUrl(String requestHttpUrl) {
		this.requestHttpUrl = requestHttpUrl;
	}


	public OnlineMoniService getOnlineMoniService() {
		return onlineMoniService;
	}


	public void setOnlineMoniService(OnlineMoniService onlineMoniService) {
		this.onlineMoniService = onlineMoniService;
	}


	public EmployeeMoniService getEmployeeMoniService() {
		return employeeMoniService;
	}


	public void setEmployeeMoniService(EmployeeMoniService employeeMoniService) {
		this.employeeMoniService = employeeMoniService;
	}


	public String getPwd() {
		return pwd;
	}


	public void setPwd(String pwd) {
		this.pwd = pwd;
	}


	public String getExamName() {
		return examName;
	}


	public void setExamName(String examName) {
		this.examName = examName;
	}


	public String getOpty() {
		return opty;
	}


	public void setOpty(String opty) {
		this.opty = opty;
	}


	public int getSignIncount() {
		return signIncount;
	}


	public void setSignIncount(int signIncount) {
		this.signIncount = signIncount;
	}

	public List<TestpaperThemeForm> getThemelist() {
		return themelist;
	}

	public void setThemelist(List<TestpaperThemeForm> themelist) {
		this.themelist = themelist;
	}

	public String getQtype() {
		return qtype;
	}

	public void setQtype(String qtype) {
		this.qtype = qtype;
	}

	public String getShowSign() {
		return showSign;
	}

	public void setShowSign(String showSign) {
		this.showSign = showSign;
	}

	public String getNy() {
		return ny;
	}

	public void setNy(String ny) {
		this.ny = ny;
	}

	public Theme getFktheme() {
		return fktheme;
	}

	public void setFktheme(Theme fktheme) {
		this.fktheme = fktheme;
	}

	public List<ThemeFkauditForm> getThemeFkauditFormList() {
		return themeFkauditFormList;
	}

	public void setThemeFkauditFormList(List<ThemeFkauditForm> themeFkauditFormList) {
		this.themeFkauditFormList = themeFkauditFormList;
	}

	
}
