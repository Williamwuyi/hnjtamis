package cn.com.ite.hnjtamis.mainpage.basic;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import cn.com.ite.eap2.core.spring.SpringContextUtil;
import cn.com.ite.eap2.core.struts2.AbstractListAction;
import cn.com.ite.hnjtamis.common.StaticVariable;
import cn.com.ite.hnjtamis.exam.exampaper.ExampaperService;
import cn.com.ite.hnjtamis.excel.ExportExcelService;
import cn.com.ite.hnjtamis.jobstandard.domain.JobsStandardQuarter;
import cn.com.ite.hnjtamis.jobstandard.domain.StandardTerms;
import cn.com.ite.hnjtamis.jobstandard.domain.StandardTypes;

/**
 * 电厂达标情况导出
 * @author 朱健
 * @create time: 2016年3月28日 上午11:16:51
 * @version 1.0
 * 
 * @modified records:
 */
public class BasicMainPageExpExcelAction  extends AbstractListAction implements ServletRequestAware,ServletResponseAware{

	private static final long serialVersionUID = -5270573487067596623L;

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
	public String expExcel()
			throws Exception {		
		    try {
		    	String qid = request.getParameter("qid");
		    	String qtype = request.getParameter("qtype");
		    	String qexamId = request.getParameter("qexamId");
		    	String qstartDay = request.getParameter("qstartDay");
		    	String qendDay = request.getParameter("qendDay");
		    	
		    	ExportExcelService excel = new BasicMainPageExcelServiceImpl();
		    	ExampaperService exampaperService = (ExampaperService)SpringContextUtil.getBean("exampaperService");
		    	List examDeptList = exampaperService.getExamDeptTjxxByUser( qid, qexamId,qstartDay,qendDay,StaticVariable.EXAM_EXAM_PROPERTY);
		    	List examUserList = exampaperService.getExamUserInfos( qid, qtype, qexamId,qstartDay,qendDay,StaticVariable.EXAM_EXAM_PROPERTY);
		    	Map valueMap = new HashMap();
		 		valueMap.put("examDeptList", examDeptList);
		 		valueMap.put("examUserList", examUserList);
		 		String[] sheetName = new String[]{"部门达标情况汇总","人员达标情况"};
		 		valueMap.put("sheetName", sheetName);
				excel.download(request, response, "GBK", null, "电厂达标情况导出.xls", excel.exportExcel(valueMap));
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
	
}
