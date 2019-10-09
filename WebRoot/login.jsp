<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<%  request.setCharacterEncoding("UTF-8"); %>
<TITLE>岗位培训管理系统</TITLE>
<META http-equiv=Content-Type content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7" />
<meta name="keywords" content="长沙市亿泰科技项目EAP平台" />
<meta name="description" content="长沙市亿泰科技项目EAP平台"/> 
<style type="text/css">
body,td,th {font-size: 10px;color: #000;overflow:hidden;}
.ipt {height: 28px;width: 235px;margin-top: 3px;margin-left: 50px;border: none;font-size: 14px;color: #505050;font-family: "微软雅黑";padding-top: 4px;behavior:url(#default#userdata);}
.btn {height: 38px;width: 294px;background-image: url(resources/login-images/htdl_06.jpg);border:none;cursor:pointer;}
</style>
<link rel="Shortcut Icon" href="favicon.ico" />
<link rel="Bookmark" href="favicon.ico" />
<script type="text/javascript" src="base/include-ext.js"></script>
<script type="text/javascript">
var cp,isSaveHist=false;
Ext.onReady(function () {
    cp = new Ext.state.CookieProvider(),//cookie对象
    Ext.state.Manager.setProvider(cp);
    document.all.account.value=cp.get('account')||'';
    document.all.password.value=cp.get('password')||'';
    isSaveHist=document.all.account.value!=""||document.all.password.value!="";
    save.checked = isSaveHist;
    Ext.create('Ext.util.KeyNav', document.body, {  
        enter: function(){  
		    submitLogin();
        },  
        scope: this  
    }); 
});	 
function submitLogin(){
    var account = document.all.account.value;
    var password = document.all.password.value;
    if(!account||!password){
    	Ext.Msg.alert('提示', '请录入账号和密码！');
    	return;
    }
    if(document.all.save.checked){
    	cp.set('account',account);
    	cp.set('password',password);
    }else{
    	cp.set('account','');
    	cp.set('password','');
    }
    var tip = new Ext.LoadMask(document.body,{
 	   msg: '正在登录,请稍候...',
 	   removeMask: false //完成后移除
 	});
 	tip.show();
	Ext.Ajax.request({
		method : 'post',
		url : 'power/login/loginForLoginAction!login.action',
		success : function(response) {
		   tip.hide();
		   var obj = Ext.decode(response.responseText);
		   if(Ext.isArray(obj)) obj=obj[0];
		   if(obj.success==false){
			   Ext.Msg.alert('警告', obj.errors); 
		   }else{
    		   var redirect = 'index.jsp';//?theme='+obj.userSession.theme; 
               window.location = redirect;
		   }
		},
		failure : function(response) {
			tip.hide();
			if(response.failureType == 'server'){
                var obj = Ext.decode(response.responseText);
                if(Ext.isArray(obj)) obj = obj[0];
                   Ext.Msg.alert('登录失败', obj.errors,function(){
                	   document.all.account.focus();
                }); 
            }else{ 
                Ext.Msg.alert('警告', '网络出现问题！'); 
            }
		},
		params : {account:account,password:password}
	});
}
</SCRIPT>
<META content="MSHTML 6.00.2900.2668" name=GENERATOR></HEAD>
<body bgcolor="#00b1f0" scroll=no style="overflow: hidden">
<table width="1024" border="0" align="center" cellpadding="0" cellspacing="0">
  <tr>
    <td height="288" colspan="3" background="resources/login-images/htdl_01.jpg"></td>
  </tr>
  <tr>
    <td width="369" height="452" rowspan="2" background="resources/login-images/htdl_02.jpg"></td>
    <td width="294" height="154" background="resources/login-images/htdl_03.jpg"><table width="294" height="154" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td height="57" valign="top"><input name="account" id="account" type="text"  class="ipt" onfocus="this.select();" onKeyDown="if(event.keyCode==13)submitLogin(); " value="" />&nbsp;</td>
      </tr>
      <tr>
        <td height="58" valign="top"><input name="password" type="password"  class="ipt" onKeyDown="if(event.keyCode==13)submitLogin();"onfocus="this.value='';" value="" /></td>
      </tr>
      <tr>
        <td height="39"> <input name="loginBtn" type="button" class="btn" id="loginBtn" value="" onclick="submitLogin();" /></td>
      </tr>
    </table></td>
    <td width="361" height="452" rowspan="2" background="resources/login-images/htdl_05.jpg"></td>
  </tr>
  <tr>
    <td height="298" background="resources/login-images/htdl_04.jpg"><table width="294" height="298" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td width="147" height="72"><input name="save" id="save" type="checkbox"  /></td>
        <td width="147" align="right"><%--忘记密码？--%></td>
      </tr>
      <tr>
        <td height="113" colspan="2">&nbsp;</td>
      </tr>
      <tr>
        <td height="113" colspan="2" align="center">技术支持：长沙市亿泰科技有限公司</td>
      </tr>
    </table></td>
  </tr>
</table>
</body>
</html>
