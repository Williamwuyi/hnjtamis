package cn.com.ite.hnjtamis.exam.base.examreview;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import oracle.sql.CLOB;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;

import cn.com.ite.eap2.common.utils.StringUtils;
import cn.com.ite.eap2.core.struts2.AbstractListAction;
import cn.com.ite.eap2.module.power.login.LoginAction;
import cn.com.ite.eap2.module.power.login.UserSession;
import cn.com.ite.hnjtamis.exam.base.exampublicuser.service.ExamPublicUserService;
import cn.com.ite.hnjtamis.exam.base.examreview.form.SysOutScoreExcelForm;
import cn.com.ite.hnjtamis.exam.base.examreview.service.ExamReviewService;
import cn.com.ite.hnjtamis.exam.hibernatemap.Exam;
import cn.com.ite.hnjtamis.exam.hibernatemap.ExamTestpaperAnswerkey;
import cn.com.ite.hnjtamis.exam.hibernatemap.ExamTestpaperTheme;
import cn.com.ite.hnjtamis.exam.hibernatemap.ExamUserAnswerkey;
/*
 * 考试 阅卷
 */
public class ExamReviewListAction extends AbstractListAction implements ServletRequestAware{
	private static final long serialVersionUID = -7078325139765234147L;
	private HttpServletRequest request;
	private static String[] choiceNum = new String[]{"A","B","C","D","E","F","G","H","I","J","K","L","M","N"};
	private static String[] themeTypeNum = new String[]{"一","二","三","四","五","六","七","八","九","十"};
	
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
	private List<String> resultInfo = new ArrayList<String>();
	
	//科目下所有参考考生map   key:考试科目id value:<考生id集合,试卷id>
	//public static ConcurrentHashMap<String,ConcurrentHashMap<String,String>> examPaperToExaminee = new ConcurrentHashMap<String,ConcurrentHashMap<String,String>>(); 
	
	//科目下所有参考考生map   key:考试科目id value:<考生id集合,试卷id> 阅卷审核
	//public static ConcurrentHashMap<String,ConcurrentHashMap<String,String>> reExamPaperToExaminee = new ConcurrentHashMap<String,ConcurrentHashMap<String,String>>();
		
	//正在阅卷的 考生试卷              key:科目id,value:题目id
	public static ConcurrentHashMap<String,LinkedList<String>> reviewingExamPaper = new ConcurrentHashMap<String,LinkedList<String>>();
	//阅卷人 正在阅卷的题目	 key:登录人帐号id,value:题目id
	public static ConcurrentHashMap<String,LinkedList<String>> loginPeoreviewingExamPaper = new ConcurrentHashMap<String,LinkedList<String>>();
	//阅卷人正在阅卷科目  key:登录人帐号id@examid,value:examid@examName
	public static ConcurrentHashMap<String,LinkedList<String>> peopleToExam = new ConcurrentHashMap<String,LinkedList<String>>();
	
	/*
	 * 情况所有阅卷人正在阅卷试题
	 */
	public String clearReviewing(){
		try {
			reviewingExamPaper.clear();
			loginPeoreviewingExamPaper.clear();
			this.setMsg("解锁成功");
		} catch (Exception e) {
			this.setMsg("解锁失败");
			e.printStackTrace();
		}
		return "clearReviewing";
	}
	/*
	 * 解锁 登陆人产生 未提交 题目
	 */
	public String releaseEmpTimu(){
		try {
			String empIdExamId = request.getParameter("empIdExamId");
			System.out.println(empIdExamId);
			String empId = empIdExamId.split("@")[0];
			String examId = empIdExamId.split("@")[1];
			
			LinkedList<String> tmp = peopleToExam.get(empIdExamId);
			boolean flag = peopleToExam.containsKey(empIdExamId);
			System.out.println(flag);
			if(reviewingExamPaper.containsKey(examId) && flag){
				LinkedList<String> reList = reviewingExamPaper.get(examId);
				for(String str : tmp){
					if(reList.contains(str)){
						reList.remove(str);
					}
				}
				reviewingExamPaper.put(examId, reList);
			}
			if(loginPeoreviewingExamPaper.containsKey(empId) && flag){
				LinkedList<String> loList = loginPeoreviewingExamPaper.get(empId);
				for(String str : tmp){
					if(loList.contains(str)){
						loList.remove(str);
					}
				}
				loginPeoreviewingExamPaper.put(empId, loList);
			}
			if(flag){
				peopleToExam.remove(empIdExamId);
			}
			this.setMsg("解锁成功");
		} catch (Exception e) {
			this.setMsg("解锁失败");
			e.printStackTrace();
		}
		return "clearReviewing";
	}
	/*
	 * 显示 系统外得分复核
	 */
	private static Map<String,String> SysOutScore_EmployeeReviewExamPaperMap = new HashMap<String,String>();
	public String showSysOutScoreView(){
		UserSession usersess = LoginAction.getUserSessionInfo();
		try {
			String examId = request.getParameter("examId");//考试科目id
			Exam exam = (Exam) service.findDataByKey(examId, Exam.class);
			String userId = getExamineeId(usersess.getEmployeeId(),SysOutScore_EmployeeReviewExamPaperMap,examId,"15","15","15");//参考人id
			if(userId!=null){
				Map term = new HashMap();
				term.put("examId", examId);
				term.put("userId", userId);
				
				
				List<ExamTestpaperTheme> timulist = null;
				
				List userNameList = service.queryData("queryExamUserNameSql", term, null, null);
				Map<String,String> userNameMap = new HashMap<String,String>();
				if(userNameList!=null && userNameList.size()>0){
					for (Object object : userNameList) {
						HashMap t = (HashMap) object;
						String userid = t.get("USER_ID").toString();
						String username = t.get("USER_NAME").toString();
						String inticket = t.get("INTICKET").toString();
						
						userNameMap.put(userid, username+"("+inticket+")");
					}
				}
				
				timulist = service.queryData("queryExamineePageSysOut", term, null, ExamTestpaperTheme.class);
				
				
				//将题目按类型划分 设置到 map中 (key:题型名称,value:题目list)
				LinkedHashMap<String,ArrayList<ExamTestpaperTheme>> resultMap = new LinkedHashMap<String,ArrayList<ExamTestpaperTheme>>();
				LinkedHashMap<String,Double> scoreMap = new LinkedHashMap<String,Double>();
				if(timulist!=null && timulist.size()>0){
					for (ExamTestpaperTheme timu : timulist) {
						if(resultMap.containsKey(timu.getUserId())){
							ArrayList<ExamTestpaperTheme> tmp = resultMap.get(timu.getUserId());
							tmp.add(timu);
							resultMap.put(timu.getUserId(), tmp);
							scoreMap.put(timu.getUserId(), timu.getDefaultScore()+scoreMap.get(timu.getUserId()));
						}else{
							ArrayList<ExamTestpaperTheme> tmp = new ArrayList<ExamTestpaperTheme>();
							tmp.add(timu);
							resultMap.put(timu.getUserId(), tmp);
							scoreMap.put(timu.getUserId(), timu.getDefaultScore());
						}
					}
				}
				request.setAttribute("userNameMap", userNameMap);
				request.setAttribute("resultMap", resultMap);
				request.setAttribute("scoreMap", scoreMap);
			}else{
				request.setAttribute("userNameMap", new HashMap());
				request.setAttribute("resultMap",  new HashMap());
				request.setAttribute("scoreMap",  new HashMap());
			}
			request.setAttribute("choiceNum", choiceNum);
			request.setAttribute("themeTypeNum", themeTypeNum);
			request.setAttribute("exam", exam);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "showSysOutScoreView";
	}
	
	/*
	 * 单人阅卷视图
	 */
	private static Map<String,String> SingleView_EmployeeReviewExamPaperMap = new HashMap<String,String>();
	public String showSingleView(){
		UserSession usersess = LoginAction.getUserSessionInfo();
		String reReviewed = request.getParameter("reReviewed");
		try {
			String examId = request.getParameter("examId");//考试科目id
			Exam exam = (Exam) service.findDataByKey(examId, Exam.class);
			String userId = null;//参考人id
			if(!StringUtils.isEmpty(reReviewed) && reReviewed.equals("reReviewed")){
				userId = getExamineeId(usersess.getEmployeeId(),SingleView_EmployeeReviewExamPaperMap,examId,"20","20","20");//参考人id
			}else{
				userId = getExamineeId(usersess.getEmployeeId(),SingleView_EmployeeReviewExamPaperMap,examId,"15","25","25");//参考人id
			}
			if(userId!=null){
				Map term = new HashMap();
				term.put("examId", examId);
				term.put("userId", userId);
				term.put("state", 15);
				term.put("anotherstate", 25);
				if(!StringUtils.isEmpty(reReviewed) && reReviewed.equals("reReviewed")){
					term.put("state", 20);
					term.put("anotherstate", 20);
					userId = getReExamineeId(examId);
				}
				
				
				List<ExamTestpaperTheme> timulist = null;
				if(reviewingExamPaper.get(examId)!=null && reviewingExamPaper.get(examId).size()>0){//正在阅卷的试题
					LinkedList<String> t = reviewingExamPaper.get(examId);
					StringBuffer param = new StringBuffer("',");
					for (String string : t) {
						param.append(string).append(",");
					}
					term.put("timuids", param.append("'").toString());
					if(loginPeoreviewingExamPaper.get(usersess.getEmployeeId())!=null && loginPeoreviewingExamPaper.get(usersess.getEmployeeId()).size()>0){//登录人有未保存阅卷的试题
						LinkedList<String> et = loginPeoreviewingExamPaper.get(usersess.getEmployeeId());
						StringBuffer param2 = new StringBuffer("',");
						for (String string : et) {
							param2.append(string).append(",");
						}
						term.put("curtimuids", param2.append("'").toString());
						timulist = service.queryData("queryExamineePageExistsSql", term, null, ExamTestpaperTheme.class);
					}else{
						timulist = service.queryData("queryExamineePageSql", term, null, ExamTestpaperTheme.class);
					}
				}else{
					timulist = service.queryData("queryExamineePage", term, null, ExamTestpaperTheme.class);
				}
				
				//将题目按类型划分 设置到 map中 (key:题型名称,value:题目list)
				LinkedHashMap<String,ArrayList<ExamTestpaperTheme>> resultMap = new LinkedHashMap<String,ArrayList<ExamTestpaperTheme>>();
				LinkedHashMap<String,Double> scoreMap = new LinkedHashMap<String,Double>();
				if(timulist!=null && timulist.size()>0){
					LinkedList<String> timuIds = new LinkedList<String>();
					Map term1 = new HashMap();
					term1.put("examId", exam.getExamId());
					List<ExamTestpaperAnswerkey> examTestpaperAnswerkeyList = 
							this.getService().queryData("queryExamTestpaperAnswerkeyByExamId", term1, null);
					Map<String ,List<ExamTestpaperAnswerkey>> examTestpaperAnswerkeyMap = new HashMap<String ,List<ExamTestpaperAnswerkey>>();
					for(int i=0;i<examTestpaperAnswerkeyList.size();i++){
						ExamTestpaperAnswerkey examTestpaperAnswerkey = examTestpaperAnswerkeyList.get(i);
						String themeId = examTestpaperAnswerkey.getExamTestpaperTheme().getExamTestpaperThemeId();
						List tmpList = (List)examTestpaperAnswerkeyMap.get(themeId);
						if(tmpList == null){
							tmpList = new ArrayList();
						}
						tmpList.add(examTestpaperAnswerkey);
						examTestpaperAnswerkeyMap.put(themeId, tmpList);
					}
					
					List<ExamUserAnswerkey> examUserAnswerkeyList = 
								this.getService().queryData("queryExamUserAnswerkeyByExamId", term1, null);
					Map<String ,List<ExamUserAnswerkey>> examUserAnswerkeyMap = new HashMap<String ,List<ExamUserAnswerkey>>();
					for(int i=0;i<examUserAnswerkeyList.size();i++){
						ExamUserAnswerkey examUserAnswerkey = examUserAnswerkeyList.get(i);
						String themeId = examUserAnswerkey.getExamTestpaperTheme().getExamTestpaperThemeId();
						List tmpList = (List)examUserAnswerkeyMap.get(themeId);
						if(tmpList == null){
							tmpList = new ArrayList();
						}
						tmpList.add(examUserAnswerkey);
						examUserAnswerkeyMap.put(themeId, tmpList);
					}
					
					
					for (ExamTestpaperTheme timu : timulist) {
						List examTestpaperAnswerkeies = (List)examTestpaperAnswerkeyMap.get(timu.getExamTestpaperThemeId());
						//两个答案的一次性替换，避免多次查询
						if(examTestpaperAnswerkeies!=null){
							timu.setExamTestpaperAnswerkeies(examTestpaperAnswerkeies);
						}
						
						List examUserAnswerkeies = (List)examUserAnswerkeyMap.get(timu.getExamTestpaperThemeId());
						if(examUserAnswerkeies!=null){
							timu.setExamUserAnswerkeies(examUserAnswerkeies);
						}
						
						
						if(resultMap.containsKey(timu.getThemeTypeName())){
							ArrayList<ExamTestpaperTheme> tmp = resultMap.get(timu.getThemeTypeName());
							tmp.add(timu);
							resultMap.put(timu.getThemeTypeName(), tmp);
							scoreMap.put(timu.getThemeTypeName(), timu.getDefaultScore()+scoreMap.get(timu.getThemeTypeName()));
							timuIds.add(timu.getExamTestpaperThemeId());
						}else{
							ArrayList<ExamTestpaperTheme> tmp = new ArrayList<ExamTestpaperTheme>();
							tmp.add(timu);
							resultMap.put(timu.getThemeTypeName(), tmp);
							scoreMap.put(timu.getThemeTypeName(), timu.getDefaultScore());
							timuIds.add(timu.getExamTestpaperThemeId());
						}
					}
					//reviewingExamPaper.put(timulist.get(0).getExamTestpaper().getExamTestpaperId(), timuIds);
					if(reviewingExamPaper.containsKey(exam.getExamId())){
						LinkedList<String> tmp = reviewingExamPaper.get(exam.getExamId());
						tmp.addAll(timuIds);
						reviewingExamPaper.put(exam.getExamId(), tmp);
					}else{
						reviewingExamPaper.put(exam.getExamId(), timuIds);
					}
					if(loginPeoreviewingExamPaper.containsKey(usersess.getEmployeeId())){
						LinkedList<String> tmp = loginPeoreviewingExamPaper.get(usersess.getEmployeeId());
						tmp.addAll(timuIds);
						loginPeoreviewingExamPaper.put(usersess.getEmployeeId(), tmp);
					}else{
						loginPeoreviewingExamPaper.put(usersess.getEmployeeId(), timuIds);
					}
					String tempKey = usersess.getEmployeeId()+"@"+exam.getExamId();
					System.out.println("tempKey is:"+tempKey);
					peopleToExam.put(tempKey, timuIds);
					
				}
				//HttpServletResponse response = ServletActionContext.getResponse();
				request.setAttribute("resultMap", resultMap);
				request.setAttribute("scoreMap", scoreMap);
			}else{
				request.setAttribute("resultMap", new HashMap());
				request.setAttribute("scoreMap", new HashMap());
			}
			
			request.setAttribute("choiceNum", choiceNum);
			request.setAttribute("themeTypeNum", themeTypeNum);
			request.setAttribute("exam", exam);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(!StringUtils.isEmpty(reReviewed) && reReviewed.equals("reReviewed")){
			return "reReviewedView";
		}
		return "showSingleView";
	}
	/*
	 * 随机获取参考人id
	 */
	private String getExamineeId(String employeeId,Map<String,String> employeeReviewInExamPaperMap,String examId,String state,String state1,String state2){
		String userId = null;
		//if(examPaperToExaminee.containsKey(examId)){
			//ConcurrentHashMap<String,String> examineeIdMap = examPaperToExaminee.get(examId);
			//String[] keys = examineeIdMap.keySet().toArray(new String[0]);
			//Random r = new Random();
			//userId = keys[r.nextInt(keys.length)];
		//}else{//没有考试信息则 查询
			//查询该考试科目下所有考生
			Map condition = new HashMap();
			condition.put("examId", examId);
			condition.put("state", state);
			condition.put("state1", state1);
			condition.put("state2", state2);
			List queryList = service.queryData("queryExamPeople", condition, null,null);//userid  examtestpaperid
			ConcurrentHashMap<String,ConcurrentHashMap<String,String>> examPaperToExaminee = new ConcurrentHashMap<String,ConcurrentHashMap<String,String>>(); 
			if(queryList!=null && queryList.size()>0){
				ConcurrentHashMap<String,String> t = new ConcurrentHashMap<String,String>();
				for (Object object : queryList) {
					Object[] array = (Object[]) object;
					t.put(array[0].toString(), array[1].toString());
				}
				examPaperToExaminee.put(examId, t);
			}
			if(examPaperToExaminee.containsKey(examId)){
				ConcurrentHashMap<String,String> examineeIdMap = examPaperToExaminee.get(examId);
				
				String _userId = employeeReviewInExamPaperMap.get(employeeId);//测试这个用户是不是已经分配了，并且存在于未审核的信息列表
				if(_userId!=null && examineeIdMap.get(_userId)!=null){
					userId = _userId;
				}else{
					String[] keys = examineeIdMap.keySet().toArray(new String[0]);
					Random r = new Random();
					userId = keys[r.nextInt(keys.length)];
					employeeReviewInExamPaperMap.put(employeeId,userId);
				}
				
				
			}
		//}
		return userId;
	}
	/*
	 * 随机获取参考人id 阅卷审核
	 */
	private String getReExamineeId(String examId){
		String userId = null;
		//if(reExamPaperToExaminee.containsKey(examId)){
			//ConcurrentHashMap<String,String> examineeIdMap = reExamPaperToExaminee.get(examId);
			//String[] keys = examineeIdMap.keySet().toArray(new String[0]);
			//Random r = new Random();
			//userId = keys[r.nextInt(keys.length)];
		//}else{//没有考试信息则 查询
			//查询该考试科目下所有考生
			Map condition = new HashMap();
			condition.put("examId", examId);
			List queryList = service.queryData("queryExamPeopleForReView", condition, null,null);//userid  examtestpaperid
			ConcurrentHashMap<String,ConcurrentHashMap<String,String>> reExamPaperToExaminee = new ConcurrentHashMap<String,ConcurrentHashMap<String,String>>();
			if(queryList!=null && queryList.size()>0){
				ConcurrentHashMap<String,String> t = new ConcurrentHashMap<String,String>();
				for (Object object : queryList) {
					Object[] array = (Object[]) object;
					t.put(array[0].toString(), array[1].toString());
				}
				reExamPaperToExaminee.put(examId, t);
			}
			if(reExamPaperToExaminee.containsKey(examId)){
				ConcurrentHashMap<String,String> examineeIdMap = reExamPaperToExaminee.get(examId);
				String[] keys = examineeIdMap.keySet().toArray(new String[0]);
				Random r = new Random();
				userId = keys[r.nextInt(keys.length)];
			}
		//}
		return userId;
	}
	/*
	 * 多人阅卷视图
	 */
	public String showMultiView(){
		UserSession usersess = LoginAction.getUserSessionInfo();
		if(usersess == null){
			this.setMsg("SESSION　信息已失效，请关闭浏览器并重新登陆！");
			return "showMultiView";
	 	}
		if(usersess.getEmployeeId() == null){
			this.setMsg("没有找到用户信息！");
			return "showMultiView";
		}else{
			try {
				String examId = request.getParameter("examId");//考试科目id
				ExamReviewService examReviewService = (ExamReviewService)service;
				Exam exam = (Exam) service.findDataByKey(examId, Exam.class);
				//抽取试题  题型  题目名称排序  条件:考试科目id 不查询正在阅卷的题目
				/*Map term = new HashMap();
				term.put("examId", examId);
				term.put("state", 15);
				term.put("anotherstate", 25);*/
				List<ExamTestpaperTheme> timulist = null;
				if(reviewingExamPaper.get(examId)!=null && reviewingExamPaper.get(examId).size()>0){
					LinkedList<String> t = reviewingExamPaper.get(examId);
					/*StringBuffer param = new StringBuffer("',");
					for (String string : t) {
						param.append(string).append(",");
					}
					term.put("timuids", param.append("'").toString());*/
					if(loginPeoreviewingExamPaper.get(usersess.getEmployeeId())!=null && loginPeoreviewingExamPaper.get(usersess.getEmployeeId()).size()>0){//登录人有未保存阅卷的试题
						LinkedList<String> et = loginPeoreviewingExamPaper.get(usersess.getEmployeeId());
						/*StringBuffer param2 = new StringBuffer("',");
						for (String string : et) {
							param2.append(string).append(",");
						}
						term.put("curtimuids", param2.append("'").toString());
						timulist = service.queryData("queryTimusExitsSql", term, null, ExamTestpaperTheme.class);*/
						
						timulist = examReviewService.queryExamTestpaperThemeList(examId, t, et, "15", "25");
					}else{
						//timulist = service.queryData("queryTimusSql", term, null, ExamTestpaperTheme.class);
						timulist = examReviewService.queryExamTestpaperThemeList(examId, t, null, "15", "25");
					}
					/*LinkedList<String> t = reviewingExamPaper.get(examId);
					StringBuffer param = new StringBuffer("',");
					for (String string : t) {
						param.append(string).append(",");
					}
					term.put("timuids", param.append("'").toString());
					timulist = service.queryData("queryTimusSql", term, null, ExamTestpaperTheme.class);*/
				}else{
					//timulist = service.queryData("queryTimusAllSql", term, null, ExamTestpaperTheme.class);
					timulist = examReviewService.queryExamTestpaperThemeList(examId, null, null, "15", "25");
				}
				
				//将题目按类型划分 设置到 map中 (key:题型名称,value:题目list)
				LinkedHashMap<String,ArrayList<ExamTestpaperTheme>> resultMap = new LinkedHashMap<String,ArrayList<ExamTestpaperTheme>>();
				LinkedHashMap<String,Double> scoreMap = new LinkedHashMap<String,Double>();
				if(timulist!=null && timulist.size()>0){
					LinkedList<String> timuIds = new LinkedList<String>();
					for (ExamTestpaperTheme timu : timulist) {
						if(resultMap.containsKey(timu.getThemeTypeName())){
							ArrayList<ExamTestpaperTheme> tmp = resultMap.get(timu.getThemeTypeName());
							tmp.add(timu);
							resultMap.put(timu.getThemeTypeName(), tmp);
							scoreMap.put(timu.getThemeTypeName(), timu.getDefaultScore()+scoreMap.get(timu.getThemeTypeName()));
							timuIds.add(timu.getExamTestpaperThemeId());
						}else{
							ArrayList<ExamTestpaperTheme> tmp = new ArrayList<ExamTestpaperTheme>();
							tmp.add(timu);
							resultMap.put(timu.getThemeTypeName(), tmp);
							scoreMap.put(timu.getThemeTypeName(), timu.getDefaultScore());
							timuIds.add(timu.getExamTestpaperThemeId());
						}
						
					}
					LinkedList<String> alltimuIds = reviewingExamPaper.get(exam.getExamId());
					if(alltimuIds == null){
						alltimuIds = new LinkedList<String>();
						alltimuIds.addAll(timuIds);
						reviewingExamPaper.put(exam.getExamId(),alltimuIds);
					}else{
						if(timuIds!=null && timuIds.size()>0){
							LinkedList<String> addtimuIds = new LinkedList<String>();
							for (String timu0 : timuIds) {
								boolean isAddThemeId = true;
								for (String timu2 : alltimuIds) {
									if(timu2!=null && timu2.equals(timu0)){
										isAddThemeId = false;
										break;
									}
								}
								if(isAddThemeId){
									addtimuIds.add(timu0);
								}
							}
							alltimuIds.addAll(addtimuIds);
							reviewingExamPaper.put(exam.getExamId(),alltimuIds);
						}
					}
					
					
					//reviewingExamPaper.put(timulist.get(0).getExamTestpaper().getExamTestpaperId(), timuIds);
					/*if(reviewingExamPaper.containsKey(exam.getExamId())){
						LinkedList<String> tmp = reviewingExamPaper.get(exam.getExamId());
						tmp.addAll(timuIds);
						reviewingExamPaper.put(exam.getExamId(), tmp);
					}else{
						reviewingExamPaper.put(exam.getExamId(), timuIds);
					}*/
					if(loginPeoreviewingExamPaper.containsKey(usersess.getEmployeeId())){
						LinkedList<String> tmp = loginPeoreviewingExamPaper.get(usersess.getEmployeeId());
						tmp.addAll(timuIds);
						loginPeoreviewingExamPaper.put(usersess.getEmployeeId(), tmp);
					}else{
						LinkedList<String> tmp = new LinkedList<String>();
						tmp.addAll(timuIds);
						loginPeoreviewingExamPaper.put(usersess.getEmployeeId(), tmp);
					}
					peopleToExam.put(usersess.getEmployeeId()+"@"+exam.getExamId(), timuIds);
				}
				
				//HttpServletResponse response = ServletActionContext.getResponse();
				request.setAttribute("resultMap", resultMap);
				request.setAttribute("scoreMap", scoreMap);
				request.setAttribute("choiceNum", choiceNum);
				request.setAttribute("themeTypeNum", themeTypeNum);
				request.setAttribute("exam", exam);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return "showMultiView";
	}
	/*
	 * 导入
	 */
	public String importXls() throws Exception{
		try {
			String examId = request.getParameter("examId");
			HashMap<String,Object[]> updataMap = new HashMap<String,Object[]>();//<key:考试科目名称@题号@准考证号,value: object[考试试题id,题目最高分得分]>
			if(!StringUtils.isEmpty(examId)){
				Map term = new HashMap();
				term.put("examId", examId);
				List datas = service.queryData("queryExcelByExamIdSql", term, null, null);
				if(datas!=null && datas.size()>0){
					for (Object object : datas) {
						HashMap t = (HashMap) object;
						SysOutScoreExcelForm vform = new SysOutScoreExcelForm();
						vform.setExamPaperThemeId(t.get("EXAM_TESTPAPER_THEME_ID").toString());
						vform.setExamName(t.get("EXAM_NAME").toString());
						vform.setTihao(Integer.parseInt(t.get("SORT_NUM").toString()));
						CLOB timu = (CLOB) t.get("THEME_NAME");
						vform.setTimu(this.ClobToString(timu));
						vform.setUserName(t.get("USER_NAME").toString());
						vform.setZkzNum(t.get("INTICKET").toString());
						vform.setDefaultScore(Double.parseDouble(t.get("DEFAULT_SCORE").toString()));
						
						Object[] tmpObj = new Object[]{vform.getExamPaperThemeId(),vform.getDefaultScore()};
						updataMap.put(vform.getExamName()+"@"+vform.getTihao()+"@"+vform.getZkzNum(), tmpObj);
					}
				}
			}
			if(updataMap.size()>0){
				resultInfo = ((ExamReviewService)service).importDate(xls,updataMap,examId);
			}else{
				resultInfo.add("未查询到所选考试科目的系统外题集");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "save";
	}
	
	/*
	 * 导出
	 */
	public String exportXls() throws Exception{
		try {
			String examId = request.getParameter("examId");
			List<SysOutScoreExcelForm> exportDatas = new ArrayList<SysOutScoreExcelForm>();
			if(!StringUtils.isEmpty(examId)){
				//查询此科目下所有考生 和 试题信息
				Map term = new HashMap();
				term.put("examId", examId);
				List datas = service.queryData("queryExcelByExamIdSql", term, null, null);
				if(datas!=null && datas.size()>0){
					for (Object object : datas) {
						HashMap t = (HashMap) object;
						SysOutScoreExcelForm vform = new SysOutScoreExcelForm();
						vform.setExamName(t.get("EXAM_NAME").toString());
						vform.setTihao(Integer.parseInt(t.get("SORT_NUM").toString())+1);
						CLOB timu = (CLOB) t.get("THEME_NAME");
						vform.setTimu(this.ClobToString(timu));
						vform.setUserName(t.get("USER_NAME").toString());
						vform.setZkzNum(t.get("INTICKET").toString());
						
						exportDatas.add(vform);
					}
				}
			}
			File exportXlsFile = ((ExamReviewService)service).exportDate(exportDatas);
			inputStream = new FileInputStream(exportXlsFile);
			this.fileName = new String("系统外得分考生信息.xls".getBytes(),"ISO-8859-1");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "export";
	}
	
	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}
	public File getXls() {
		return xls;
	}
	public void setXls(File xls) {
		this.xls = xls;
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
	
	public List<String> getResultInfo() {
		return resultInfo;
	}
	public void setResultInfo(List<String> resultInfo) {
		this.resultInfo = resultInfo;
	}
	public String ClobToString(CLOB clob) {
        String reString = "";
        Reader is = null;
        try {
            is = clob.getCharacterStream();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // 得到流
        BufferedReader br = new BufferedReader(is);
        String s = null;
        try {
            s = br.readLine();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        StringBuffer sb = new StringBuffer();
        while (s != null) {
            //执行循环将字符串全部取出付值给StringBuffer由StringBuffer转成STRING
            sb.append(s);
            try {
                s = br.readLine();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        reString = sb.toString();
        return reString;
    }
}
