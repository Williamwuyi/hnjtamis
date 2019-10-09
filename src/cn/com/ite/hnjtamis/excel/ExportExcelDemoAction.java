package cn.com.ite.hnjtamis.excel;





import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;



import cn.com.ite.eap2.core.struts2.AbstractListAction;
import cn.com.ite.hnjtamis.excel.ExportExcelDemoServiceImpl;
import cn.com.ite.hnjtamis.excel.ExportExcelService;




/**
 * <p>Title cn.com.ite.ydtcms.excel.ExportExcelDemoAction</p>
 * <p>Description  导出Excel Action</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2011</p>
 * @author 朱健
 * @create time: 2011-10-10 下午07:02:41
 * @version 1.0
 * 
 * @modified records:
 */
public class ExportExcelDemoAction  extends AbstractListAction implements ServletRequestAware,ServletResponseAware{

	private HttpServletRequest request;
	
	private HttpServletResponse response;
	
	/**
	 * 导出Excel例子  测试路径：/exportExcelDemoAction.do?action=exportToExcel
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String exportToExcel()
			throws Exception {		
		try {
			ExportExcelService excel = new ExportExcelDemoServiceImpl();
			String contentType = request.getParameter("contentType");
			Map valueMap=new HashMap();
			excel.download(request, response, "GBK", contentType, "导出Excel例子.xls", excel.exportExcel(valueMap));
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
