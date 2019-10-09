<%@page language="java" contentType="application/x-msdownload"
	pageEncoding="utf-8"%>
<%
	response.reset();//可以加也可以不加   
   	response.setContentType("application/x-download");
	
	String filename = "试题导入模版.xls";
	System.out.println(filename);
	String filedownload = application.getRealPath("/modules/hnjtamis/base/theme/试题导入模版.xls");
	//System.out.println(filedownload);
   	String filedisplay = filename;
   	filedisplay = java.net.URLEncoder.encode(filedisplay, "UTF-8");
   	response.setHeader("Content-Disposition", "attachment;filename=" + filedisplay);

   	java.io.OutputStream outp = null;
   	java.io.FileInputStream in = null;
   	try {
   		outp = response.getOutputStream();
   		in = new java.io.FileInputStream(filedownload);

   		byte[] b = new byte[1024];
   		int i = 0;

   		while ((i = in.read(b)) > 0) {
   			outp.write(b, 0, i);
   		}   
   		outp.flush();
   		out.clear();
   		out = pageContext.pushBody();
   	} catch (Exception e) {
   		
   	} finally {
   		if (in != null) {
   			in.close();
   			in = null;
   		}  
   	}
   %>