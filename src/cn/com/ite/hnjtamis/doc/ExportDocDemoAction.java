package cn.com.ite.hnjtamis.doc;





import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import cn.com.ite.eap2.core.struts2.AbstractListAction;



/**
 * <p>Title cn.com.ite.ydkh.doc.ExportDocDemoAction</p>
 * <p>Description 导出Doc Action</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2012</p>
 * @author 朱健
 * @create time: 2012-11-16 上午10:31:22
 * @version 1.0
 * 
 * @modified records:
 */
public class ExportDocDemoAction extends AbstractListAction implements ServletRequestAware,ServletResponseAware{

	
	private HttpServletRequest request;
	
	private HttpServletResponse response;
	
	/**
	 * 导出Excel例子  测试路径：http://127.0.0.1:8080/应用名/docExp/exportToDocForExportDocDemoAction!exportToDoc.action
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String exportToDoc()
			throws Exception {		
		try {
			ExportDocService doc = new ExportDocDemoServiceImpl();
			Map valueMap=new HashMap();
			valueMap.put("MSG", "我的例子");
			doc.exportDoc(request, response,  "导出Doc例子.doc",valueMap);
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
