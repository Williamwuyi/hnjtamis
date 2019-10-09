<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
<%
String title = request.getParameter("moduletitle");
if(title==null){
	title="未知模块";
}else
title = cn.com.ite.eap2.common.utils.StringUtils.tranEncode(title,"UTF-8");
%>
<META http-equiv=Content-Type content="text/html; charset=UTF-8">
<title><%=title%></title>
    <script type="text/javascript" src="base/include-ext.js"></script>
    <script type="text/javascript" src="base/options-toolbar.js"></script>
    <script type="text/javascript">
        /**类路径与文件路径的映射配置**/
        <%
          String jsFilePath = (String)request.getAttribute("jsFilePath");
          String jsClass = (String)request.getAttribute("jsClass");
          String param = request.getQueryString();
        %>
        var grabl_pageRecordSize=<%=cn.com.ite.eap2.Config.getPropertyValue("grabl_pageRecordSize")%>;
        var grabl_tabPageHeight=<%=cn.com.ite.eap2.Config.getPropertyValue("grabl_tabPageHeight")%>;//TAB页签的高度
        var grabl_tabPageMaxSize=<%=cn.com.ite.eap2.Config.getPropertyValue("grabl_tabPageMaxSize")%>;//打开最大窗口数量
        var jsFilePath = "<%=jsFilePath%>";
        if(jsFilePath==""||jsFilePath==null||jsFilePath=="null"){
        	jsFilePath = jsClass.replace(/\./g,'/')+".js";
        }
        Ext.Loader.loadScript({ url: jsFilePath});
        Ext.onReady(function(){
           var login = new ClassCreate("base.Login");
           login.findUserSession();
           var params = "<%=param%>".split("&");
           var config = {};
           for(var i=1;i<params.length;i++){
        	   var ps = params[i].split("=");
        	   config[ps[0]]=ps[1];
           }
           var window = eval("new <%=jsClass%>(config)");//new ClassCreate("<%=jsClass%>",config);           
           new Ext.Viewport({
              layout: "fit",
              items: [window]
           });
        });        
    </script>
</head>
<body></body>
</html>