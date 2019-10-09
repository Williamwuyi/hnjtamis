<%@ page contentType="text/html; charset=UTF-8"%>
<%@page import="java.io.*"%>
<%
   String imagePath = request.getParameter("imagePath");
   File file = new File(request.getRealPath(imagePath));
   out.println("[");
   if(file.exists()){
	   File[] files = file.listFiles();
	   for(int i=0;i<files.length;i++){
		   if(i>0)
			   out.println(",");
		   out.println("{'name':'','url':\'"+imagePath+file.separator+files[i].getName()+"\'}");
	   }
   }
   out.println("]");
%>