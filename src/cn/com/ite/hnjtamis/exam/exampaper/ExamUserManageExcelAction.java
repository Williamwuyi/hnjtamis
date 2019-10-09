package cn.com.ite.hnjtamis.exam.exampaper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import cn.com.ite.eap2.core.struts2.AbstractListAction;
import cn.com.ite.eap2.core.struts2.ServletContent;
import cn.com.ite.eap2.module.power.login.UserSession;
import cn.com.ite.hnjtamis.common.StaticVariable;
import cn.com.ite.hnjtamis.excel.ExportExcelService;


/**
 *
 * <p>Title cn.com.ite.hnjtamis.exam.exampaper.ExamUserManageExcelAction</p>
 * <p>Description 达标成绩导出</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2018</p>
 * @create time: 2018年8月9日 下午4:09:31
 * @version 1.0
 * 
 * @modified records:
 */
public class ExamUserManageExcelAction  extends AbstractListAction implements ServletRequestAware,ServletResponseAware{

	private static final long serialVersionUID = 363496752746775431L;

	private HttpServletRequest request;
	
	private HttpServletResponse response;
	
	private String qid;
	private String qtype;
	private String qexamId;
	private String qstartDay;
	private String qendDay;

	/**
	 * 导出Excel 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String toExcel()
			throws Exception {		
		    try { 
		    	 ExportExcelService excel = new ExamUserManageExcelServiceImpl();
		    	 
		    	 UserSession usersess = (UserSession)ServletContent.getSession().get(StaticVariable.USERSESSION);
		 		if(usersess == null){
		 			this.setMsg("SESSION　信息已失效，请关闭浏览器并重新登陆！");
		 			return "list";
		 	 	}
		 		ExampaperService exampaperService = (ExampaperService)this.getService();
				String examTypeId = StaticVariable.EXAM_EXAM_PROPERTY;
				List deptCountList = exampaperService.getExamTjxxByUser( qid, qexamId,qstartDay,qendDay,examTypeId);
				
				List examUserList = exampaperService.getExamUserInfos( qid, qtype, qexamId,qstartDay,qendDay,examTypeId);
		 		String[] sheetName= new String[]{"电厂达标情况("+qstartDay+"~"+qendDay+")","达标考试成绩("+qstartDay+"~"+qendDay+")"};
		 		String[] titleName= new String[]{"电厂达标情况汇总","达标考试成绩汇总"};
		 		String fileName= "电厂达标情况("+qstartDay+"~"+qendDay+")";
		    	Map valueMap = new HashMap();
		 		valueMap.put("deptCountList", deptCountList);
		 		valueMap.put("examUserList", examUserList);
		 		valueMap.put("sheetName", sheetName);
		 		valueMap.put("titleName", titleName);
				excel.download(request, response, "GBK", null, fileName+"导出.xls", excel.exportExcel(valueMap));
		 	} catch (Exception e) {
				e.printStackTrace();
			}
		    return null;
	}
	
	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
		
	}
	
	@Override
	public void setServletResponse(HttpServletResponse httpservletresponse) {
		this.response = httpservletresponse;
	}


	public String getQid() {
		return qid;
	}

	public void setQid(String qid) {
		this.qid = qid;
	}

	public String getQtype() {
		return qtype;
	}

	public void setQtype(String qtype) {
		this.qtype = qtype;
	}

	public String getQexamId() {
		return qexamId;
	}

	public void setQexamId(String qexamId) {
		this.qexamId = qexamId;
	}

	public String getQstartDay() {
		return qstartDay;
	}

	public void setQstartDay(String qstartDay) {
		this.qstartDay = qstartDay;
	}

	public String getQendDay() {
		return qendDay;
	}

	public void setQendDay(String qendDay) {
		this.qendDay = qendDay;
	}

	
	
	
}
