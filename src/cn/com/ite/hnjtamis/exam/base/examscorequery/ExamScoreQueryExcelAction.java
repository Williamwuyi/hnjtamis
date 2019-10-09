package cn.com.ite.hnjtamis.exam.base.examscorequery;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.springframework.beans.BeanUtils;

import cn.com.ite.eap2.common.utils.StringUtils;
import cn.com.ite.eap2.core.struts2.AbstractListAction;
import cn.com.ite.eap2.core.struts2.ServletContent;
import cn.com.ite.eap2.domain.baseinfo.Dictionary;
import cn.com.ite.eap2.module.power.login.LoginAction;
import cn.com.ite.eap2.module.power.login.UserSession;
import cn.com.ite.hnjtamis.common.StaticVariable;
import cn.com.ite.hnjtamis.exam.base.examscorequery.form.ScoreForm;
import cn.com.ite.hnjtamis.exam.exampaper.form.ExamUserForm;
import cn.com.ite.hnjtamis.exam.hibernatemap.Exam;
import cn.com.ite.hnjtamis.exam.hibernatemap.ExamUserTestpaper;
import cn.com.ite.hnjtamis.excel.ExportExcelService;

/**
 *
 * @author 朱健
 * @create time: 2016年2月16日 下午4:39:11
 * @version 1.0
 * 
 * @modified records:
 */
public class ExamScoreQueryExcelAction  extends AbstractListAction implements ServletRequestAware,ServletResponseAware{
	
	private static final long serialVersionUID = 448368666687947961L;

	private HttpServletRequest request;
	
	private HttpServletResponse response;
	

	/**
	 * 导出Excel 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String exportXls()
			throws Exception {
			UserSession usersess = (UserSession)ServletContent.getSession().get(StaticVariable.USERSESSION);
			if(usersess == null){
				this.setMsg("SESSION　信息已失效，请关闭浏览器并重新登陆！");
				return "list";
			}
		    try { 
		    	 ExportExcelService excel = new ExamScoreQueryExcelServiceImpl();
		    	 /*Exam exam = (Exam)this.getService().findDataByKey(this.getId(), Exam.class);
		    	 List examineeInfoList = new ArrayList();
		    	 for(int i=0;i<exam.getExamUserTestpapers().size();i++){
		 			ExamUserTestpaper examUserTestpaper = (ExamUserTestpaper)exam.getExamUserTestpapers().get(i);
		 			ExamUserForm examUserForm = new ExamUserForm();
		 			BeanUtils.copyProperties(examUserTestpaper,examUserForm);
		 			if(examUserTestpaper.getExamPublicUser()!=null){
		 				examUserForm.setUserId(examUserTestpaper.getExamPublicUser().getUserId());
		 			}
		 			examineeInfoList.add(examUserForm);
		 		}*/
		    	 String examId = request.getParameter("examId");
		    	 List list = new ArrayList();
		    	 Map typeNameMap = new HashMap();
		    	 Exam  exam = null;
		    	 if(!StringUtils.isEmpty(examId)){
		    		    exam = (Exam)this.getService().findDataByKey(examId,Exam.class);
		    		 
						Map term = new HashMap();
						term.put("examId", examId);
						List themeTypeList = service.queryData("queryExamThemeTypeByExamIdSql", term, null, null);
						ArrayList<String> typeList = new ArrayList<String>();
						
						if(themeTypeList!=null && themeTypeList.size()>0){
							for (Object object : themeTypeList) {
								HashMap t = (HashMap) object;
								String THEME_TYPE_NAME = t.get("THEME_TYPE_NAME").toString();
								typeList.add(THEME_TYPE_NAME);
								typeNameMap.put(THEME_TYPE_NAME, THEME_TYPE_NAME);
							}
						}
						term.put("organId", usersess.getCurrentOrganId());
						List resultList = service.queryData("queryScoreByExamIdSql", term, null, null);
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
		    	 Map valueMap = new HashMap();
		 		 valueMap.put("list", list);
		 		 valueMap.put("typeNameMap", typeNameMap);
		 		 String sheetName = (exam!=null?exam.getExamName() : "") +"考试得分";
		 		 valueMap.put("sheetName", sheetName);
				 excel.download(request, response, "GBK", null, sheetName+"导出.xls", excel.exportExcel(valueMap));
		 	} catch (Exception e) {
				e.printStackTrace();
			}
		    return null;
	}
	
	
	public String exportSingleXls()throws Exception {		
		    try { 
		    	UserSession usersess = LoginAction.getUserSessionInfo();
		    	 ExportExcelService excel = new ExamScoreQueryExcelServiceImpl();
		    	 /*Exam exam = (Exam)this.getService().findDataByKey(this.getId(), Exam.class);
		    	 List examineeInfoList = new ArrayList();
		    	 for(int i=0;i<exam.getExamUserTestpapers().size();i++){
		 			ExamUserTestpaper examUserTestpaper = (ExamUserTestpaper)exam.getExamUserTestpapers().get(i);
		 			ExamUserForm examUserForm = new ExamUserForm();
		 			BeanUtils.copyProperties(examUserTestpaper,examUserForm);
		 			if(examUserTestpaper.getExamPublicUser()!=null){
		 				examUserForm.setUserId(examUserTestpaper.getExamPublicUser().getUserId());
		 			}
		 			examineeInfoList.add(examUserForm);
		 		}*/
		    	 String examId = request.getParameter("examId");
		    	 List list = new ArrayList();
		    	 Exam  exam = null;
		    	 if(!StringUtils.isEmpty(examId)){
		    		    exam = (Exam)this.getService().findDataByKey(examId,Exam.class);
		    	 }	 
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
					
		    	 Map valueMap = new HashMap();
		 		 valueMap.put("list", list);
		 		 String sheetName = (exam!=null?exam.getExamName() : "") +"考试得分";
		 		 valueMap.put("sheetName", sheetName);
				 excel.download(request, response, "GBK", null, sheetName+"导出.xls", excel.exportExcel(valueMap));
		 	} catch (Exception e) {
				e.printStackTrace();
			}
		    return null;
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
	
}
