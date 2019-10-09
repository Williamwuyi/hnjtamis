<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%--
@version: 1.0 企业首页展示
@author: wangyong 根据登录用户跳转
@time: 2015.05.07
@modify: 
@time: 
--%>
<%@ taglib uri="/WEB-INF/tld/struts-tags.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/tld/fn.tld" prefix="fn"%>
<%
	request.setCharacterEncoding("GBK");
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	request.setAttribute("basePath",basePath);
%>
<head>
	<title>基层企业首页信息</title>
	<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=utf-8">
	<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
	<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache">
	<META HTTP-EQUIV="Expires" CONTENT="0">
	<link rel="stylesheet" type="text/css" href="${basePath}resources/css/ext-all.css" />
	<script type="text/javascript" src="${basePath}base/ext/ext-all.js"></script>
	
  <script type="text/javascript">
  <!--
		//Ext.onReady(function(){
		    // create the Data Store
		    ////var usersession;
		    var organ;
		    var findUserOrgan=function(){
				var me = this;
				Ext.Ajax.request({
					method : 'GET',
					url : '${basePath}mainpage/organizationForMainPageListAction!organization.action',
					async : false,
					success : function(response) {
						var result = Ext.decode(response.responseText);
						organ = result.organ;
					    //console.log(us);
						
					},
					failure:function(form, action){
		                if(action.failureType == 'server'){
		                    var obj = Ext.decode(action.response.responseText);
		                    if(Ext.isArray(obj)) obj = obj[0];
		                    Ext.Msg.alert('错误', obj.errors); 
		                }else{ 
		                    Ext.Msg.alert('警告', '网络出现问题！'); 
		                }
		            }
				});
			};
			findUserOrgan();
			var redirect="";
		    //var usersession=base.Login.userSession;
		    if(organ.organType=='10' || organ.organType=='20'){
		    	redirect = '${basePath}mainpage/branch/listForBranchMainPageListAction!list.action'; 
	                   	///alert(redirect);
		    }else{
		    	redirect = '${basePath}mainpage/basic/listForBasicMainPageListAction!list.action';
		    }
		    window.location = redirect;
		///});
      	
      	 
  //-->
  </script>
  
</head>
<body bgcolor="#f0f0f0" style="margin:14px;">
	正在加载...	
</body>
</html>
