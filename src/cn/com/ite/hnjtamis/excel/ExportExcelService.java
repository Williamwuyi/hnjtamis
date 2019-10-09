package cn.com.ite.hnjtamis.excel;

import java.io.UnsupportedEncodingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>Title cn.com.ite.excel.ExportExcelService</p>
 * <p>Description </p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2009</p>
 * @author 朱健
 * @create time: 2009-3-11 上午09:37:19
 * @version 1.0
 * 
 * @modified records:
 */
public interface ExportExcelService {

	/**
	 * Excel处理
	 * @param list
	 */
	public String exportExcel(Object form)throws Exception;	
	
	/**
	 * 下载文件
	 * 
	 * @param request
	 * @param response
	 * @param encoding
	 *            编码方式
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public boolean download(HttpServletRequest request,
			HttpServletResponse response, String encoding, String contentType,
			String name,String fileName) throws Exception;
}
