<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
<META http-equiv=Content-Type content="text/html; charset=UTF-8">
<title>岗位培训管理系统</title>
<%
	String lgt = request.getParameter("lgt");
    if(lgt == null || "".equals(lgt) || "null".equals(lgt) || "undefined".equals(lgt)){
    	lgt = "normal";
    }
    if("fast".equals(lgt)){
    	String path = request.getContextPath();
    	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    	String url = "mainPageEx/listForMainExListAction!list.action";
    	//request.getRequestDispatcher(url).forward(request,response);
    	response.sendRedirect(basePath+url);
    }else{
%>
    <script type="text/javascript" src="base/include-ext.js"></script>
    <script type="text/javascript" src="base/options-toolbar.js"></script>
    <script type="text/javascript">
        var eapClientWidth = document.documentElement.clientWidth;
        var eapClientHeight = document.documentElement.clientHeight; 
        var u = navigator.userAgent;
        //是否为移动浏览器
        var isPhoneNavigator = !!u.match(/AppleWebKit.*Mobile.*/);
        /**类路径与文件路径的映射配置**/
        Ext.Loader.setConfig({
            enabled:true,
            //disableCaching:false,
            paths:{
              'Ext.ux.desktop': 'base/core',
              'eap':'base'
            }
        });       
        var grabl_pageRecordSize=<%=cn.com.ite.eap2.Config.getPropertyValue("grabl_pageRecordSize")%>;
        var grabl_tabPageHeight=<%=cn.com.ite.eap2.Config.getPropertyValue("grabl_tabPageHeight")%>;//TAB页签的高度
        var grabl_tabPageMaxSize=<%=cn.com.ite.eap2.Config.getPropertyValue("grabl_tabPageMaxSize")%>;//打开最大窗口数量
        var grabl_numberColumnWidth = 50;
    	var grabl_tree_numberColumnWidth = 50;
        if(isPhoneNavigator){
        	grabl_numberColumnWidth="50";
        	grabl_tree_numberColumnWidth="70";
        	document.write('<link rel="stylesheet" type="text/css" href="resources/css/ext-all-neptune.css"/>');
        	var phoneLoginPanel = new Ext.Panel({
                //layout : {type : 'table',columns : 1},
                width:'100%',
                align:'center',
                items:[
                      {html:'<table width="640" align="center" border="0" cellspacing="0" cellpadding="0">'+
                            '<tr><td><img width="640" height="354" src="resources/images/APP_01.jpg"/></td></tr>'+
                            '<tr><td height="224" valign="top" style="background:url(resources/images/APP_02.jpg) no-repeat">'+
                            '<table width="100%" border="0" cellspacing="0" cellpadding="0">'+
                            '<tr><td width="100px" rowspan="2" align="right" valign="bottom">&nbsp;</td>'+
                            '<td height="77" align="center" valign="bottom"><input type="text" class="phone_login_text" id="user_name"/></td>'+
                            '</tr><tr><td height="81" align="center" valign="bottom"><input type="password" class="phone_login_text" id="user_pass"/></td>'+
                            '</tr><tr><td height="66" valign="bottom" align="left" cellspan="2">&nbsp;&nbsp;&nbsp;&nbsp;'+
                            '<input type="checkbox" name="checkbox" class="phone_login_check" id="user_check"/></td></tr></table></td></tr>'+
                            '<tr><td height="344" align="center" valign="top" style="background:url(resources/images/APP_03.jpg) no-repeat">'+
                            '<input name="" type="submit" class="phone_login_button" value="" id="user_submit"/></td></tr></table>'}
                      ]
            });
            //手机登录页面
            Ext.onReady(function () {
            	var cp = new Ext.state.CookieProvider();
         	    Ext.state.Manager.setProvider(cp);
            	var viewport = new Ext.Viewport({
                    layout: 'fit',
                    border:0,
                    items: [phoneLoginPanel]
                });
            	var user = Ext.get("user_name");
                var pass = Ext.get("user_pass");
                var check = Ext.get("user_check");
                user.dom.value = cp.get('account');
                pass.dom.value = cp.get('password');
                if(user.dom.value!='')
                    check.dom.checked=true;
            	Ext.get("user_submit").on('click',function(){                       
                       if(check.dom.checked){
                    	   cp.set('account',user.dom.value);
                       	   cp.set('password',pass.dom.value);
                       }else{
                    	   cp.set('account','');
                       	   cp.set('password','');
                       }
                       if(user.dom.value==""||pass.dom.value==""){
                    	   Ext.Msg.alert('警告', '请录入账号和密码！'); 
                    	   return;
                       }
                       viewport.remove(phoneLoginPanel);
                  });
            });
        }else{
	        var eap = Ext.create('eap.EapApp');
	        function getQueryParam(name) {
	            var regex = RegExp('[?&]' + name + '=([^&]*)');

	            var match = regex.exec(location.search) || regex.exec(path);
	            return match && decodeURIComponent(match[1]);
	        }
	        if(location.search&&getQueryParam('theme')=='neptune'){
	        	grabl_numberColumnWidth = 50;
	        	grabl_tree_numberColumnWidth = 70;
	        }
	        document.onkeydown = function (e) {
	            var code;   
	            if (!e){ var e = window.event;}   
	            if (e.keyCode){ code = e.keyCode;}
	            else if (e.which){ code = e.which;}
	            //BackSpace 8;
	            if ((event.keyCode == 8)&& ((event.srcElement.type != "text" 
	                && event.srcElement.type != "textarea" &&  event.srcElement.type != "password")
	                ||  event.srcElement.readOnly == true)) {                
		            event.keyCode = 0;        
		            event.returnValue = false;    
	            }
	            return true;
	        };
        }
    </script>
    <% } %>
</head>
<body></body>
</html>