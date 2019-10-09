<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>粤电集团先进发电企业考核管理系统</title>
		<style type="text/css">
img {
	border: none
}

body,td,th {
	font-family: "微软雅黑";
	font-size: 14px;
}

.btn {
	height: 47px;
	width: 260px;
	background-image: url(resources/images/login_btn.jpg);
	border: none;
}

.ipt {
	font-family: "微软雅黑";
	color: #737373;
	font-size: 16px;
	height: 30px;
	width: 90%;
	//border: none;
	margin-top: 8px;
	margin-left: 3px;
}
</style>
		<script type="text/javascript" src="base/include-ext.js"></script>
		<script type="text/javascript"> 
    function submitLogin(){
    	var sysUrl = document.all.sysUrl.value;
        var account = document.all.account.value;
        var password = document.all.password.value;
        var appId = document.all.appId.value;
        var url = document.all.url.value;
        var tip = new Ext.LoadMask(document.body,{
     	   msg: '正在登录,请稍候...',
     	   removeMask: false //完成后移除
     	});
     	tip.show();
    	Ext.Ajax.request({
			method : 'post',
			url : 'power/login/queryPwdForLoginAction!queryPwd.action',
			success : function(response) {
    		   tip.hide();
    		   var obj = Ext.decode(response.responseText);
    		   document.all.pwd.value = obj.pwd;
    		   var redirect = sysUrl+"&uid="+account+"&pwd="+document.all.pwd.value;
    		   window.open(redirect)
    		   //document.all["ftable"].src = redirect;
			},
			failure : function(response) {
				tip.hide();
				Ext.Msg.alert('警告', '登录失败！'); 
			},
			params : {account:account,password:password,appId:appId}
		});
    }
    
    function cal(){
    	var sysUrl = document.all.sysUrl.value;
        var account = document.all.account.value;
        var password = document.all.password.value;
        var appId = document.all.appId.value;
        var url = document.all.url.value;
        var tip = new Ext.LoadMask(document.body,{
     	   msg: '正在登录,请稍候...',
     	   removeMask: false //完成后移除
     	});
     	tip.show();
    	Ext.Ajax.request({
			method : 'post',
			url : 'power/login/queryPwdForLoginAction!queryPwd.action',
			success : function(response) {
    		   tip.hide();
    		   var obj = Ext.decode(response.responseText);
    		   document.all.pwd.value = obj.pwd;
    		   //var redirect = sysUrl+url+"&uid="+account+"&pwd="+document.all.pwd.value;
    		   //window.location = redirect;
			},
			failure : function(response) {
				tip.hide();
				Ext.Msg.alert('警告', '登录失败！'); 
			},
			params : {account:account,password:password,appId:appId}
		});
    	
    }
    </script>
	</head>
	<body scroll=no  style="overflow: hidden">
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
  		<tr>
   		<td align="center" valign="middle">
			<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0" id="mainTable">
				<tr>
					<td align="left" valign="middle" style="padding:5px;">
					系统:<input type="text" class="ipt" id="sysUrl" name="sysUrl" value="http://127.0.0.1:8080/ydkh/index.jsp?1=1" /><br>
					UEL:<input type="text" class="ipt" id="url" name="url" value="" /><br>
					账号:<input type="text" class="ipt" id="account" name="account" style="width:350px;" value="710085" />&nbsp;&nbsp;
					KEY:<input type="text" class="ipt" id="appId" name="appId" value="8e2kxdtYf4LYTNJrV3jrkAufzgwfVkn6" style="width:350px;"/><br>
					密码:<input type="text" class="ipt" id="password" name="password"  style="width:350px;" value="21218cca77804d2ba1922c33e0151105" />&nbsp;&nbsp;
					PWD:<input type="text" class="ipt" id="pwd" name="pwd" value="" style="width:350px;" /><br><br>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="button" name="bt" value="获取PWD" onclick="cal()"/>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="button" name="bt" value="测试页面打开" onclick="submitLogin()"/>
				</td>
				</tr>
				<tr>
				<td align="left" valign="middle" style="padding:5px;height:500px;">
				<iframe name="ftable" id="ftable"  scrolling="auto" frameborder="0" height="100%" width="100%" src="" 
										        style="overflow: auto;height: 100%;border: 1px solid #A1A7AD;"></iframe>
				</td>
				</tr>
			</table>
		</td>
		</tr>
	</table>
	</body>
</html>