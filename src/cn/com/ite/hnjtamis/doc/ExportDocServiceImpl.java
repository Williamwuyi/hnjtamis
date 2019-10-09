package cn.com.ite.hnjtamis.doc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.com.ite.hnjtamis.common.RandomGUID;

import com.lowagie.text.Document;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.html.simpleparser.HTMLWorker;
import com.lowagie.text.html.simpleparser.StyleSheet;
import com.lowagie.text.rtf.RtfWriter2;



/**
 * <p>Title cn.com.ite.ydkh.doc.ExportDocServiceImpl</p>
 * <p>Description 导出Doc</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2012</p>
 * @author 朱健
 * @create time: 2012-11-16 上午10:01:20
 * @version 1.0
 * 
 * @modified records:
 */
public abstract class ExportDocServiceImpl implements ExportDocService {

	protected String replaceToHtml(String themeContext){
		 themeContext = themeContext.replaceAll("<", "&lt;");
		 themeContext = themeContext.replaceAll(">", "&gt;");
		 return themeContext;
	}
	
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
			HttpServletResponse response, String exportName,Object form)throws Exception{
		return this.download(request, response, "GBK", null, exportName,  exportDocument(form));
	}
	
	/**
	 * Doc处理
	 * @param form
	 */
	private String exportDocument(Object form)throws Exception{
		String fileName = RandomGUID.getGUID()+".doc" ;
		try {
			 Document document = new Document(PageSize.A4);// 设置纸张大小
			  //建立一个书写器(Writer)与document对象关联，通过书写器(Writer)可以将文档写入到磁盘中
			  RtfWriter2.getInstance(document, new FileOutputStream(fileName));       
			  document.open();
			  setDocument(document,form);
			  document.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return fileName ;
	}
	
	
	/**
	 * @author 朱健
	 * @description 设置Word内容
	 * @param document
	 * @modified
	 */
	protected abstract void setDocument(Document document,Object form)throws Exception;
	
	
	protected static Paragraph getDocHtml(String str)throws Exception{
		 Paragraph context = new Paragraph();
		 context.setSpacingBefore(0); //离上一段落（标题）空的行数             
		 context.setFirstLineIndent(0);//设置第一行空的列数       
		 StyleSheet ss = new StyleSheet();   
		 List htmlList = HTMLWorker.parseToList(new StringReader(str), ss);   
		 for (int i = 0; i < htmlList.size(); i++) {   
		    com.lowagie.text.Element e = (com.lowagie.text.Element) htmlList.get(i);   
		    context.add(e);   
		 }
		 return context;
	}
	
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
	private boolean download(HttpServletRequest request,
			HttpServletResponse response, String encoding, String contentType,
			String exportName,String fileName) throws Exception{
		boolean flag = false;

		request.setCharacterEncoding(encoding);
		File file = new File(fileName);// +filename
		if(!file.exists()){
			file.createNewFile() ;
		}
		InputStream ins = null;
		OutputStream outStream = null;
		try {
			ins = new FileInputStream(file);

			response.setContentType("application/vnd.ms-word");
			// filename=MimeUtility.decodeText(filename);
			String tmpname = exportName;
			// 保存文件名转码
			tmpname = new String(tmpname.getBytes("GB2312"), "ISO_8859_1");

			response.addHeader("Content-Disposition", "attachment; filename=" + tmpname + "");

			outStream = response.getOutputStream();
			byte[] bytes = new byte[1024];
			int len = 0;
			while ((len = ins.read(bytes)) != -1) {
				outStream.write(bytes, 0, len);
			}
			flag = true;

		} catch (Exception e) {
			System.out.print("e:" + e.getMessage());
		} finally {
			closeStream(ins, outStream);
			boolean delfile = file.delete();// 删除临时文件
		}
		return flag;
	}
	

	/**
	 * 关闭相关流
	 * 
	 * @param ins
	 * @param outStream
	 * @throws IOException
	 */
	private void closeStream(InputStream ins, OutputStream outStream)
			throws IOException {
		if (ins != null)
			ins.close();
		if (outStream != null) {
			outStream.close();
		}
	}
}
