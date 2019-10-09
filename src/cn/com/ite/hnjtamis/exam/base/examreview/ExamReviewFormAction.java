package cn.com.ite.hnjtamis.exam.base.examreview;

import java.math.BigDecimal;
import java.util.*;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;

import cn.com.ite.eap2.common.utils.DateUtils;
import cn.com.ite.eap2.common.utils.JsonUtils;
import cn.com.ite.eap2.common.utils.StringUtils;
import cn.com.ite.eap2.core.struts2.AbstractFormAction;
import cn.com.ite.eap2.module.power.login.LoginAction;
import cn.com.ite.eap2.module.power.login.UserSession;
import cn.com.ite.hnjtamis.exam.base.examreview.form.ReviewTestpaperTheme;
import cn.com.ite.hnjtamis.exam.base.examreview.service.ExamReviewService;
import cn.com.ite.hnjtamis.exam.hibernatemap.ExamMarkTheme;
import cn.com.ite.hnjtamis.exam.hibernatemap.ExamMarkpeople;
import cn.com.ite.hnjtamis.exam.hibernatemap.ExamTestpaperTheme;

public class ExamReviewFormAction extends AbstractFormAction implements ServletRequestAware{
	private static final long serialVersionUID = 7489887846926600266L;
	private HttpServletRequest request;
	/*
	 * 保存审核阅卷
	 */
	public String saveReReviewed(){
		String saveWay = request.getParameter("saveWay");//save:保存  rollback:打回
		String examId = request.getParameter("examId");
		UserSession usersess = LoginAction.getUserSessionInfo();
		boolean myFlag = false;
		if(!StringUtils.isEmpty(saveWay) && saveWay.equals("rollback")){
			myFlag = true;
		}
		String myDataArray = request.getParameter("dataArray");
		String timuIds = request.getParameter("timuIds");
		try {
			//查询题目
			Map condition = new HashMap();
			String[] ct = timuIds.split(",");
			condition.put("timuIds", ct);
			List<ExamTestpaperTheme> pos = service.queryData("queryTimusForSave", condition, null, ExamTestpaperTheme.class);
			Map<String,ExamTestpaperTheme> tmpmap = new HashMap<String,ExamTestpaperTheme>();
			for (ExamTestpaperTheme examTestpaperTheme : pos) {
				tmpmap.put(examTestpaperTheme.getExamTestpaperThemeId(), examTestpaperTheme);
			}
			
			List<ExamTestpaperTheme> saveList = new ArrayList<ExamTestpaperTheme>();
			List<String> reviewedTimusList = new ArrayList<String>();
			if(!StringUtils.isEmpty(myDataArray)){	
				String[] jsonStr = myDataArray.split("@&@");
				if(jsonStr!=null && jsonStr.length>0){
					for(int i=0;i<jsonStr.length;i++){
						ExamTestpaperTheme po = JsonUtils.fromJson(jsonStr[i], ExamTestpaperTheme.class);
						if(tmpmap.containsKey(po.getExamTestpaperThemeId())){
							ExamTestpaperTheme t = tmpmap.get(po.getExamTestpaperThemeId());
							t.setReviewComments(po.getReviewComments());
							if(myFlag){
								t.setState(25);
								t.getExamTestpaper().setState("15");
							}
							saveList.add(t);
							reviewedTimusList.add(t.getExamTestpaperThemeId());
						}
					}
				}
			}
			service.saves(saveList);
			//更新 试卷表 状态
			
			Map paramMap = new HashMap();
			paramMap.put("examId", examId);
			List themeStateList = service.queryData("queryExamThemeState", paramMap, null, null);
			if(themeStateList!=null && themeStateList.size()>0){
				String msg = "";
				for(int i=0;i<themeStateList.size();i++){
					Map t = (Map) themeStateList.get(i);
					BigDecimal succ = (BigDecimal)t.get("SUCC");
					BigDecimal unsucc = (BigDecimal)t.get("UNSUCC");
					BigDecimal notsub = (BigDecimal)t.get("NOTSUB");
					String scoreType = (String)t.get("SCORETYPE");
					int num = succ.intValue()+unsucc.intValue()+notsub.intValue();
					if(Integer.parseInt(scoreType)==0){
						msg+="系统自动阅卷[共"+num+"题,已阅"+succ+"题,未阅"+unsucc+"题，还有未提交阅卷"+notsub+"题],";
					}else if(Integer.parseInt(scoreType)==1){
						msg+="人工阅卷[共"+num+"题,已阅"+succ+"题,未阅"+unsucc+"题，还有未提交阅卷"+notsub+"题],";
					}else if(Integer.parseInt(scoreType)==2){
						msg+="系统外导入[共"+num+"题,已阅"+succ+"题,未阅"+(unsucc.intValue()+notsub.intValue())+"题],";
					}
				}
				if(!"".equals(msg)){
					msg = msg.substring(0,msg.length()-1);
					this.setMsg(msg);
				}else{
					this.setMsg("保存成功");
				}
			}else{
				this.setMsg("保存成功");
			}
			
			
		} catch (Exception e) {
			this.setMsg("保存失败");
			e.printStackTrace();
		}
		return "savaSingleReview";
	}
	/*
	 * 阅卷保存 (单人，多人)
	 */
	public String savaSingleReview(){
		try {
			UserSession usersess = LoginAction.getUserSessionInfo();
			String myDataArray = request.getParameter("dataArray");
			String timuIds = request.getParameter("timuIds");
			String examId = request.getParameter("examId");
			//查询题目
			Map condition = new HashMap();
			String[] ct = timuIds.split(",");
			condition.put("timuIds", ct);
			List<ExamTestpaperTheme> pos = service.queryData("queryTimusForSave", condition, null, ExamTestpaperTheme.class);
			Map<String,ExamTestpaperTheme> tmpmap = new HashMap<String,ExamTestpaperTheme>();
			for (ExamTestpaperTheme examTestpaperTheme : pos) {
				tmpmap.put(examTestpaperTheme.getExamTestpaperThemeId(), examTestpaperTheme);
			}
			
			List<ExamTestpaperTheme> saveList = new ArrayList<ExamTestpaperTheme>();
			List<ExamMarkTheme> saveList2 = new ArrayList<ExamMarkTheme>();
			List<String> reviewedTimusList = new ArrayList<String>();
			if(!StringUtils.isEmpty(myDataArray)){
				Map term2 = new HashMap();
				term2.put("examId", examId);
				term2.put("employeeId", usersess.getEmployeeId());
				List<ExamMarkpeople> examMarkpeoplelist = 
						(List<ExamMarkpeople>)service.queryData("queryExamMarkpeopleInEmployeeIdAndExamId", term2, null);
				
				ExamMarkpeople examMarkpeople = null;
				if(examMarkpeoplelist!=null&& examMarkpeoplelist.size()>0){
					examMarkpeople = (ExamMarkpeople)examMarkpeoplelist.get(0);
				}
				
				
				String[] jsonStr = myDataArray.split("@&@");
				if(jsonStr!=null && jsonStr.length>0){
					for(int i=0;i<jsonStr.length;i++){
						ReviewTestpaperTheme po = JsonUtils.fromJson(jsonStr[i], ReviewTestpaperTheme.class);
						if(tmpmap.containsKey(po.getExamTestpaperThemeId())){
							ExamTestpaperTheme t = tmpmap.get(po.getExamTestpaperThemeId());
							t.setScore(po.getScore());
							t.setReviewComments(po.getReviewComments());
							t.setState(20);
							saveList.add(t);
							
							reviewedTimusList.add(t.getExamTestpaperThemeId());
							
							if(examMarkpeople != null){
								ExamMarkTheme examMarkTheme  = new ExamMarkTheme();
								examMarkTheme.setExamMarkpeople(examMarkpeople);
								examMarkTheme.setExamTestpaperTheme(t);
								examMarkTheme.setScore(po.getScore());
								examMarkTheme.setMemo(po.getReviewComments());
								examMarkTheme.setState(10);
								examMarkTheme.setOrganId(usersess.getOrganId());
								examMarkTheme.setSyncFlag("1");
								examMarkTheme.setCreatedBy(usersess.getEmployeeCode());
								examMarkTheme.setCreationDate(DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));
								saveList2.add(examMarkTheme);
							}
							
						}
					}
				}
			}
			service.saves(saveList);
			if(saveList2!=null&&saveList2.size()>0)service.saves(saveList2);
			
			
			((ExamReviewService)service).updateExamTestpaperState(examId);//更新试卷表状态
			
			// 清理 正在阅卷的试题 ---   start
			LinkedList<String> reviewingExamPaper = ExamReviewListAction.reviewingExamPaper.get(examId);
			LinkedList<String> loginPeoreviewingExamPaper = ExamReviewListAction.loginPeoreviewingExamPaper.get(usersess.getEmployeeId());
			for (String timuIdStr : reviewedTimusList) {
				if(reviewingExamPaper!=null && reviewingExamPaper.contains(timuIdStr)){
					reviewingExamPaper.remove(timuIdStr);
				}
				if(loginPeoreviewingExamPaper!=null && loginPeoreviewingExamPaper.contains(timuIdStr)){
					loginPeoreviewingExamPaper.remove(timuIdStr);
				}
			}
			if(reviewingExamPaper!=null){
				ExamReviewListAction.reviewingExamPaper.put(examId, reviewingExamPaper);
			}
			if(loginPeoreviewingExamPaper!=null){
				ExamReviewListAction.loginPeoreviewingExamPaper.put(usersess.getEmployeeId(), loginPeoreviewingExamPaper);
			}
			String tempKey = usersess.getEmployeeId()+"@"+examId;
			if(ExamReviewListAction.peopleToExam.contains(tempKey)){
				ExamReviewListAction.peopleToExam.remove(tempKey);
			}
			// 清理 正在阅卷的试题 ---   end
			
			Map paramMap = new HashMap();
			paramMap.put("examId", examId);
			List themeStateList = service.queryData("queryExamThemeState", paramMap, null, null);
			if(themeStateList!=null && themeStateList.size()>0){
				String msg = "";
				for(int i=0;i<themeStateList.size();i++){
					Map t = (Map) themeStateList.get(i);
					BigDecimal succ = (BigDecimal)t.get("SUCC");
					BigDecimal unsucc = (BigDecimal)t.get("UNSUCC");
					BigDecimal notsub = (BigDecimal)t.get("NOTSUB");
					String scoreType = (String)t.get("SCORETYPE");
					int num = succ.intValue()+unsucc.intValue()+notsub.intValue();
					if(Integer.parseInt(scoreType)==0){
						msg+="系统自动阅卷[共"+num+"题,已阅"+succ+"题,未阅"+unsucc+"题，还有未提交阅卷"+notsub+"题],";
					}else if(Integer.parseInt(scoreType)==1){
						msg+="人工阅卷[共"+num+"题,已阅"+succ+"题,未阅"+unsucc+"题，还有未提交阅卷"+notsub+"题],";
					}else if(Integer.parseInt(scoreType)==2){
						msg+="系统外导入[共"+num+"题,已阅"+succ+"题,未阅"+(unsucc.intValue()+notsub.intValue())+"题],";
					}
				}
				if(!"".equals(msg)){
					msg = msg.substring(0,msg.length()-1);
					this.setMsg(msg);
				}else{
					this.setMsg("保存成功");
				}
			}else{
				this.setMsg("保存成功");
			}
		} catch (Exception e) {
			this.setMsg("保存失败");
			e.printStackTrace();
		}
		return "savaSingleReview";
	}
	/*
	 * 复核分数 保存
	 */
	public String checkSysOutScore(){
		try {
			UserSession usersess = LoginAction.getUserSessionInfo();
			String myDataArray = request.getParameter("dataArray");
			String timuIds = request.getParameter("timuIds");
			String saveType = request.getParameter("saveType");
			String examId = request.getParameter("examId");
			boolean myFlag = false;
			if(!StringUtils.isEmpty(saveType) && saveType.equals("commit")){
				myFlag = true;
			}
			//查询题目
			Map condition = new HashMap();
			String[] ct = timuIds.split(",");
			condition.put("timuIds", ct);
			List<ExamTestpaperTheme> pos = service.queryData("queryTimusForSave", condition, null, ExamTestpaperTheme.class);
			Map<String,ExamTestpaperTheme> tmpmap = new HashMap<String,ExamTestpaperTheme>();
			for (ExamTestpaperTheme examTestpaperTheme : pos) {
				tmpmap.put(examTestpaperTheme.getExamTestpaperThemeId(), examTestpaperTheme);
			}
			
			List<ExamTestpaperTheme> saveList = new ArrayList<ExamTestpaperTheme>();
			List<ExamMarkTheme> saveList2 = new ArrayList<ExamMarkTheme>();
			List<String> reviewedTimusList = new ArrayList<String>();
			if(!StringUtils.isEmpty(myDataArray)){
				Map term2 = new HashMap();
				term2.put("examId", examId);
				term2.put("employeeId", usersess.getEmployeeId());
				List<ExamMarkpeople> examMarkpeoplelist = 
						(List<ExamMarkpeople>)service.queryData("queryExamMarkpeopleInEmployeeIdAndExamId", term2, null);
				
				ExamMarkpeople examMarkpeople = null;
				if(examMarkpeoplelist!=null&& examMarkpeoplelist.size()>0){
					examMarkpeople = (ExamMarkpeople)examMarkpeoplelist.get(0);
				}
				
				String[] jsonStr = myDataArray.split("@&@");
				if(jsonStr!=null && jsonStr.length>0){
					for(int i=0;i<jsonStr.length;i++){
						ExamTestpaperTheme po = JsonUtils.fromJson(jsonStr[i], ExamTestpaperTheme.class);
						if(tmpmap.containsKey(po.getExamTestpaperThemeId())){
							ExamTestpaperTheme t = tmpmap.get(po.getExamTestpaperThemeId());
							t.setScore(po.getScore());
							t.setReviewComments(po.getReviewComments());
							if(myFlag){
								t.setState(20);
							}
							saveList.add(t);
							reviewedTimusList.add(t.getExamTestpaperThemeId());
							
							if(examMarkpeople != null){
								ExamMarkTheme examMarkTheme  = new ExamMarkTheme();
								examMarkTheme.setExamMarkpeople(examMarkpeople);
								examMarkTheme.setExamTestpaperTheme(t);
								examMarkTheme.setScore(po.getScore());
								examMarkTheme.setMemo(po.getReviewComments());
								examMarkTheme.setState(10);
								examMarkTheme.setOrganId(usersess.getOrganId());
								examMarkTheme.setSyncFlag("1");
								examMarkTheme.setCreatedBy(usersess.getEmployeeCode());
								examMarkTheme.setCreationDate(DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));
								saveList2.add(examMarkTheme);
							}
						}
					}
				}
			}
			service.saves(saveList);
			if(saveList2!=null&&saveList2.size()>0)service.saves(saveList2);
			
			if(myFlag){
				((ExamReviewService)service).updateExamTestpaperState(examId);//更新试卷表状态
			}
			// 清理 正在阅卷的试题 ---   start
			/*LinkedList<String> reviewingExamPaper = ExamReviewListAction.reviewingExamPaper.get(examId);
			LinkedList<String> loginPeoreviewingExamPaper = ExamReviewListAction.loginPeoreviewingExamPaper.get(usersess.getEmployeeId());
			for (String timuIdStr : reviewedTimusList) {
				if(reviewingExamPaper!=null && reviewingExamPaper.contains(timuIdStr)){
					reviewingExamPaper.remove(timuIdStr);
				}
				if(loginPeoreviewingExamPaper!=null && loginPeoreviewingExamPaper.contains(timuIdStr)){
					loginPeoreviewingExamPaper.remove(timuIdStr);
				}
			}
			if(reviewingExamPaper!=null){
				ExamReviewListAction.reviewingExamPaper.put(examId, reviewingExamPaper);
			}
			if(loginPeoreviewingExamPaper!=null){
				ExamReviewListAction.loginPeoreviewingExamPaper.put(usersess.getEmployeeId(), loginPeoreviewingExamPaper);
			}*/
			// 清理 正在阅卷的试题 ---   end
			this.setMsg("保存成功");
		} catch (Exception e) {
			this.setMsg("保存失败");
			e.printStackTrace();
		}
		return "savaSingleReview";
	}
	
	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	
}
