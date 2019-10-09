<%
    String path = request.getParameter("path");
    String fileName = request.getParameter("fileName");
    java.io.File file = new java.io.File(request.getRealPath("/")+path);
    if(!file.exists()){
    	response.setContentType("text/html;charset=GBK");
    	out.write(new String(("未发现此文件,可能已经删除或移除!").getBytes("iso-8859-1"),"UTF-8"));
    	return;
    }
    java.io.FileInputStream in = new java.io.FileInputStream(request.getRealPath("/")+path);
	response.setContentType("application/unkown");
	response.addHeader("Content-Disposition", "attachment; filename=\""+fileName + "\"");
	int read = 0;
	while ((read = in.read()) != -1 & in != null) {
		out.write(read);
		out.flush();
	}
	in.close();
%>
