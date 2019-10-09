package cn.com.ite.hnjtamis.doc;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>Title cn.com.ite.ydkh.doc.ExportDocService</p>
 * <p>Description 导出Doc接口</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2012</p>
 * @author 朱健
 * @create time: 2012-11-16 上午10:00:45
 * @version 1.0
 * 
 * @modified records:
 */
public interface ExportDocService {

	/**
	 * @author 朱健
	 * @description 导出Word
	 * @param request
	 * @param response
	 * @param exportName 要导出的文件名称
	 * @param form 传入的数据
	 * @return
	 * @throws Exception
	 * @modified
	 */
	public boolean exportDoc(HttpServletRequest request,
			HttpServletResponse response, String exportName,Object form)throws Exception;	
	

}
