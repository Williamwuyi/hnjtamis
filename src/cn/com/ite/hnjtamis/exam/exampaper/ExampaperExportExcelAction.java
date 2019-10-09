package cn.com.ite.hnjtamis.exam.exampaper;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.springframework.beans.BeanUtils;

import cn.com.ite.eap2.core.struts2.AbstractListAction;
import cn.com.ite.eap2.domain.baseinfo.Dictionary;
import cn.com.ite.hnjtamis.exam.exampaper.form.ExamUserForm;
import cn.com.ite.hnjtamis.exam.hibernatemap.Exam;
import cn.com.ite.hnjtamis.exam.hibernatemap.ExamUserTestpaper;
import cn.com.ite.hnjtamis.excel.ExportExcelService;





/**
 *
 * <p>Title cn.com.ite.hnjtamis.exam.exampaper.ExampaperExportExcelAction</p>
 * <p>Description Excel导出</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2015</p>
 * @author 朱健
 * @create time: 2015年6月10日 下午3:33:58
 * @version 1.0
 * 
 * @modified records:
 */
public class ExampaperExportExcelAction  extends AbstractListAction implements ServletRequestAware,ServletResponseAware{
	
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
	public String excelExamineeInfoList()
			throws Exception {		
		    try { 
		    	 ExportExcelService excel = new ExampaperUserExcelServiceImpl();
		    	 Exam exam = (Exam)this.getService().findDataByKey(this.getId(), Exam.class);
		    	 List examineeInfoList = new ArrayList();
		    	 for(int i=0;i<exam.getExamUserTestpapers().size();i++){
		 			ExamUserTestpaper examUserTestpaper = (ExamUserTestpaper)exam.getExamUserTestpapers().get(i);
		 			ExamUserForm examUserForm = new ExamUserForm();
		 			BeanUtils.copyProperties(examUserTestpaper,examUserForm);
		 			if(examUserTestpaper.getExamPublicUser()!=null){
		 				examUserForm.setUserId(examUserTestpaper.getExamPublicUser().getUserId());
		 			}
		 			examineeInfoList.add(examUserForm);
		 		}
		    	 Map valueMap = new HashMap();
		 		 valueMap.put("examineeInfoList", examineeInfoList);
		 		 String sheetName = exam.getExamName()+"考生信息";
		 		 valueMap.put("sheetName", sheetName);
				 excel.download(request, response, "GBK", null, sheetName+"导出.xls", excel.exportExcel(valueMap));
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
