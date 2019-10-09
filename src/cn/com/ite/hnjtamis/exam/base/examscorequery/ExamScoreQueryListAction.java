package cn.com.ite.hnjtamis.exam.base.examscorequery;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import cn.com.ite.eap2.common.utils.StringUtils;
import cn.com.ite.eap2.core.struts2.AbstractListAction;
import cn.com.ite.eap2.core.struts2.ServletContent;
import cn.com.ite.eap2.module.power.login.LoginAction;
import cn.com.ite.eap2.module.power.login.UserSession;
import cn.com.ite.hnjtamis.common.FileOption;
import cn.com.ite.hnjtamis.common.StaticVariable;
import cn.com.ite.hnjtamis.doc.ExportDocService;
import cn.com.ite.hnjtamis.exam.base.examscorequery.form.ScoreForm;
import cn.com.ite.hnjtamis.exam.base.examscorequery.service.ExamScorePaperExpServiceImpl;
import cn.com.ite.hnjtamis.exam.hibernatemap.Exam;
import cn.com.ite.hnjtamis.exam.hibernatemap.ExamTestpaperAnswerkey;
import cn.com.ite.hnjtamis.exam.hibernatemap.ExamTestpaperTheme;
/*
 * 考试成绩查询
 */
public class ExamScoreQueryListAction extends AbstractListAction implements ServletRequestAware,ServletResponseAware{
	private static final long serialVersionUID = 5412522098829068116L;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private static String[] choiceNum = new String[]{"A","B","C","D","E","F","G","H","I","J","K","L","M","N"};
	private static String[] themeTypeNum = new String[]{"一","二","三","四","五","六","七","八","九","十"};
	private List<ScoreForm> list = new ArrayList<ScoreForm>();
	private String examId;
	
	
	public String exportToDoc() throws Exception{
		UserSession usersess = (UserSession)ServletContent.getSession().get(StaticVariable.USERSESSION);
		if(usersess == null){
			this.setMsg("SESSION　信息已失效，请关闭浏览器并重新登陆！");
			return "save";
	 	}
		try {
			String testPaperId = request.getParameter("testPaperId");
			if(!StringUtils.isEmpty(testPaperId)){
				Map term = new HashMap();
				term.put("testPaperId", testPaperId);
				List<ExamTestpaperTheme> timuList = service.queryData("queryTimuByExamPaperIdInExam", term, null, ExamTestpaperTheme.class);
				List<ExamTestpaperAnswerkey> timuAnsList = service.queryData("queryTimuAnsByExamPaperIdInExam", term, null, ExamTestpaperAnswerkey.class);
				Map<String,List<ExamTestpaperAnswerkey>> ansMap = new HashMap<String,List<ExamTestpaperAnswerkey>>();
				for(int i=0;i<timuAnsList.size();i++){
					ExamTestpaperAnswerkey examTestpaperAnswerkey = (ExamTestpaperAnswerkey)timuAnsList.get(i);
					String key = examTestpaperAnswerkey.getExamTestpaperTheme().getExamTestpaperThemeId();
					List tmplist = ansMap.get(key);
					if(tmplist == null){
						tmplist = new ArrayList();
					}
					tmplist.add(examTestpaperAnswerkey);
					ansMap.put(key,tmplist);
					
				}
				LinkedHashMap<String,ArrayList<ExamTestpaperTheme>> resultMap = new LinkedHashMap<String,ArrayList<ExamTestpaperTheme>>();
				LinkedHashMap<String,Double> scoreMap = new LinkedHashMap<String,Double>();
				Exam exam = null;
				if(timuList!=null && timuList.size()>0){
					boolean examFlag = true;
					LinkedList<String> timuIds = new LinkedList<String>();
					for (ExamTestpaperTheme timu : timuList) {
						timu.setExamTestpaperAnswerkeies(ansMap.get(timu.getExamTestpaperThemeId()));
						if(examFlag){
							exam = (Exam) service.findDataByKey(timu.getExamId(), Exam.class);
							examFlag = false;
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
				}
				String basepath = FileOption.getFileBasePath(request);
				Map valueMap=new HashMap();
				valueMap.put("exam", exam);
				valueMap.put("resultMap", resultMap);
				valueMap.put("scoreMap", scoreMap);
				valueMap.put("choiceNum", choiceNum);
				valueMap.put("themeTypeNum", themeTypeNum);
				valueMap.put("basepath", basepath);
				ExportDocService doc = new ExamScorePaperExpServiceImpl();
				
				doc.exportDoc(request, response, exam.getExamName()+".doc", valueMap);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	/*
	 * 显示单张试卷的得分详情
	 */
	public String showExamPaperDetail(){
		String testPaperId = request.getParameter("testPaperId");
		String showExamTitle = request.getParameter("showExamTitle");
		try {
			if(!StringUtils.isEmpty(testPaperId)){
				Map term = new HashMap();
				term.put("testPaperId", testPaperId);
				List<ExamTestpaperTheme> timuList = service.queryData("queryTimuByExamPaperIdInExam", term, null, ExamTestpaperTheme.class);
				List<ExamTestpaperAnswerkey> timuAnsList = service.queryData("queryTimuAnsByExamPaperIdInExam", term, null, ExamTestpaperAnswerkey.class);
				Map<String,List<ExamTestpaperAnswerkey>> ansMap = new HashMap<String,List<ExamTestpaperAnswerkey>>();
				for(int i=0;i<timuAnsList.size();i++){
					ExamTestpaperAnswerkey examTestpaperAnswerkey = (ExamTestpaperAnswerkey)timuAnsList.get(i);
					String key = examTestpaperAnswerkey.getExamTestpaperTheme().getExamTestpaperThemeId();
					List tmplist = ansMap.get(key);
					if(tmplist == null){
						tmplist = new ArrayList();
					}
					tmplist.add(examTestpaperAnswerkey);
					ansMap.put(key,tmplist);
					
				}
				
				LinkedHashMap<String,ArrayList<ExamTestpaperTheme>> resultMap = new LinkedHashMap<String,ArrayList<ExamTestpaperTheme>>();
				LinkedHashMap<String,Double> scoreMap = new LinkedHashMap<String,Double>();
				Exam exam = null;
				if(timuList!=null && timuList.size()>0){
					boolean examFlag = true;
					LinkedList<String> timuIds = new LinkedList<String>();
					for (ExamTestpaperTheme timu : timuList) {
						//因为考试试题按randomSortnum排序，所以有些不一致
						timu.setExamTestpaperAnswerkeies(ansMap.get(timu.getExamTestpaperThemeId()));
						if(examFlag){
							exam = (Exam) service.findDataByKey(timu.getExamId(), Exam.class);
							examFlag = false;
						}
						if(resultMap.containsKey(timu.getThemeTypeName())){
							ArrayList<ExamTestpaperTheme> tmp = resultMap.get(timu.getThemeTypeName());
							tmp.add(timu);
							resultMap.put(timu.getThemeTypeName(), tmp);
							double defaultScore = timu.getDefaultScore()==null?0.0d:timu.getDefaultScore();
							Double  score = scoreMap.get(timu.getThemeTypeName());
							defaultScore += (score!=null ? score:0.0d);
							scoreMap.put(timu.getThemeTypeName(), defaultScore);
							timuIds.add(timu.getExamTestpaperThemeId());
						}else{
							ArrayList<ExamTestpaperTheme> tmp = new ArrayList<ExamTestpaperTheme>();
							tmp.add(timu);
							resultMap.put(timu.getThemeTypeName(), tmp);
							scoreMap.put(timu.getThemeTypeName(), timu.getDefaultScore());
							timuIds.add(timu.getExamTestpaperThemeId());
						}
					}
					request.setAttribute("exam", exam);
					request.setAttribute("resultMap", resultMap);
					request.setAttribute("scoreMap", scoreMap);
					request.setAttribute("choiceNum", choiceNum);
					request.setAttribute("themeTypeNum", themeTypeNum);
					request.setAttribute("testPaperId", testPaperId);
					request.setAttribute("showExamTitle", showExamTitle);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "showExamPaperDetail";
	}
	/*
	 * 查询 登录人 所有 科目考试成绩
	 */
	public String singleList(){
		UserSession usersess = LoginAction.getUserSessionInfo();
		try {
				Map term = new HashMap();
				term.put("empid", usersess.getEmployeeId());
				term.put("examId", examId);
				List resultList = service.queryData("queryScoreByEmpIdSql", term, null, null);
				if(resultList!=null && resultList.size()>0){
					for (Object object : resultList) {
						HashMap t = (HashMap) object;
						ScoreForm s = new ScoreForm();
						s.setTestPaperId(t.get("TESTPAPER_ID").toString());
						s.setExamName(t.get("EXAM_NAME").toString());
						s.setUserName(t.get("USER_NAME").toString());
						s.setFirstScore(getDoubleValue2(t.get("FRIST_SCOTE")));
						s.setDanxuan(getDoubleValue2(t.get("DANXUAN")));
						s.setDuoxuan(getDoubleValue2(t.get("DUOXUAN")));
						s.setTiankong(getDoubleValue2(t.get("TIANKONG")));
						s.setPanduan(getDoubleValue2(t.get("PANDUAN")));
						s.setWenda(getDoubleValue2(t.get("WENDA")));
						s.setShiting(getDoubleValue2(t.get("SHITING")));
						s.setQita(getDoubleValue2(t.get("QITA")));
						s.setKeguan(getDoubleValue2(t.get("KEGUAN")));
						s.setZhuguan(getDoubleValue2(t.get("ZHUGUAN")));
						s.setXtw(getDoubleValue2(t.get("XTW")));
						s.setLst(getDoubleValue2(t.get("LST")));
						s.setJst(getDoubleValue2(t.get("JST")));
						s.setHtt(getDoubleValue2(t.get("HTT")));
						list.add(s);
					}
				}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "singleList";
	}
	/*
	 * 查询指定考试科目下 所有考生成绩
	 */
	public String list(){
		UserSession usersess = (UserSession)ServletContent.getSession().get(StaticVariable.USERSESSION);
		if(usersess == null){
			this.setMsg("SESSION　信息已失效，请关闭浏览器并重新登陆！");
			return "list";
	 	}
		try {
			//String examId = request.getParameter("examId");
			String vt = request.getParameter("vt");
			if(!StringUtils.isEmpty(examId)){
				Map term = new HashMap();
				term.put("examId", examId);
				List themeTypeList = service.queryData("queryExamThemeTypeByExamIdSql", term, null, null);
				ArrayList<String> typeList = new ArrayList<String>();
				if(themeTypeList!=null && themeTypeList.size()>0){
					for (Object object : themeTypeList) {
						HashMap t = (HashMap) object;
						typeList.add(t.get("THEME_TYPE_NAME").toString());
					}
				}
				term.put("organId", usersess.getCurrentOrganId());
				List resultList = null;
				if("all".equals(vt)){
					resultList = service.queryData("queryAllScoreByExamIdSql", term, null, null);
				}else{
					resultList = service.queryData("queryScoreByExamIdSql", term, null, null);
				}
				if(resultList!=null && resultList.size()>0){
					for (Object object : resultList) {
						HashMap t = (HashMap) object;
						ScoreForm s = new ScoreForm();
						s.setTestPaperId(t.get("TESTPAPER_ID").toString());
						s.setExamName(t.get("EXAM_NAME").toString());
						s.setUserName(t.get("USER_NAME").toString());
						s.setFirstScore(getDoubleValue(t.get("FRIST_SCOTE")));
						s.setDanxuan(getDoubleValue(t.get("DANXUAN")));
						s.setDuoxuan(getDoubleValue(t.get("DUOXUAN")));
						s.setTiankong(getDoubleValue(t.get("TIANKONG")));
						s.setPanduan(getDoubleValue(t.get("PANDUAN")));
						s.setWenda(getDoubleValue(t.get("WENDA")));
						s.setShiting(getDoubleValue(t.get("SHITING")));
						s.setQita(getDoubleValue(t.get("QITA")));
						s.setKeguan(getDoubleValue(t.get("KEGUAN")));
						s.setZhuguan(getDoubleValue(t.get("ZHUGUAN")));
						s.setXtw(getDoubleValue(t.get("XTW")));
						s.setLst(getDoubleValue(t.get("LST")));
						s.setJst(getDoubleValue(t.get("JST")));
						s.setHtt(getDoubleValue(t.get("HTT")));
						s.setTypeList(typeList);
						list.add(s);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "list";
	}
	private double getDoubleValue(Object value){
		double returnValue = 0;
		if(value!=null && !StringUtils.isEmpty(value.toString())){
			returnValue = Double.parseDouble(value.toString());
		}
		return returnValue;
	}
	
	private double getDoubleValue2(Object value){
		double returnValue = -1;
		if(value!=null && !StringUtils.isEmpty(value.toString())){
			returnValue = Double.parseDouble(value.toString());
		}
		return returnValue;
	}
	
	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
		
	}
	@Override
	public void setServletResponse(HttpServletResponse httpservletresponse) {
		this.response = httpservletresponse;
	}
	
	public List<ScoreForm> getList() {
		return list;
	}

	public void setList(List<ScoreForm> list) {
		this.list = list;
	}
	public String getExamId() {
		return examId;
	}
	public void setExamId(String examId) {
		this.examId = examId;
	}
	
}
