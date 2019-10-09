<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%--
@version: 1.0 个人首页展示
@modify: 
@time: 
--%>
<%@ taglib uri="/WEB-INF/tld/struts-tags.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/tld/fn.tld" prefix="fn"%>
<%
	request.setCharacterEncoding("UTF-8");
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	request.setAttribute("basePath",basePath);
	pageContext.setAttribute("venter", "\n");
%>
<html>
<c:set var="request" scope="page" value="${pageContext.request}" />
<c:set var="serverPort" value="${request.serverPort}" />
<c:set var="base" scope="page" value="${request.scheme}://${request.serverName}:${serverPort}" />
<c:set var="contextPath" scope="page" value="${request.contextPath}" />
<c:set var="basePath" scope="page" value="${base}${contextPath}/" />
<c:set var="requestHttp" scope="page" value="${basePath}" />
<c:set var="isHidden" scope="page" value="${param.isHidden}" />
<c:set var="hiddenStyle" scope="page" value="" />
<c:set var="tableSpan" scope="page" value="2" />
<c:if test="${isHidden eq 1}">
<c:set var="hiddenStyle" scope="page" value="display:none" />
<c:set var="tableSpan" scope="page" value="1" />
</c:if>
<c:set var="isRight" scope="page" value="${param.isRight}" />
<c:set var="testpaperId" scope="page" value="${param.id}" />
<c:if test="${testpaperId ==null || testpaperId eq ''}">
<c:set var="testpaperId" scope="page" value="${id}" />
</c:if>
<head>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>培训系统-在线考试</title>
<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache">
<META HTTP-EQUIV="Expires" CONTENT="0">
<link href="${basePath}resources/css/ext-all.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${basePath }base/ext/ext-all.js"></script>
<script type="text/javascript" src="${basePath }modules/hnjtamis/mainEx/exam/ThemeFastMoni.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<style type="text/css">
body {
	font: 94%/1.2 "microsoft yahei";
	background-color: #eff5ff;
	margin: 0;
	padding: 0;
	color: #454545;
	/*border:1px solid #f00;*/
}
.btnIn { text-align:center; width:100%; height:60px; line-height:15px; font-size:14px; color:#f2fbff; cursor:pointer;}
.tblIn { width:86%; margin:auto 0px; padding:0px;}
.tblIn td.tdhead { font-size:15px;font-weight:bold;background-color:#edfaff; height:36px; border-bottom:1px solid #67a9dd; text-align:left; text-indent:20px; line-height:36px;}
.tblIn td { border-bottom:1px dashed #afafaf; line-height:36px; color:#454545; cursor:pointer;}

.tblIn td.td1 { width:88%; border-bottom:1px solid #8d8e8e; border-right:1px solid #e3e3e4; line-height:16px;}
.tblIn td.td1 p { line-height:20px; margin:12px 8px 8px 8px;}
.tblIn td.td1 p span { color:#0072ca; font-size:16px;margin:0px 25px; width:160px;}
.tblIn td.td1 ul { margin:0px; padding:0px;}
.tblIn td.td1 ul li {display:block; float:left; width:45%; margin-left:3%; margin-bottom:4px; line-height:22px;}
.tblIn td.td1 .divAnswer {clear:both; width:100%; height:auto; background-color:#f3f4f6;padding:8px 0px;}
.tblIn td.td1 .divAnswer div {margin:0px 22px; line-height:20px; font-size:13px;}

.tblIn td.td2 { width:12%; border-bottom:1px solid #8d8e8e; border-right:1px solid #ffffff; background-color:#f4f5f7}
.tblIn td.td2 div { line-height:30px; font-size:13px; margin:0px 0px 4px 8px; line-height:24px; color:#999999; width:75px;}
.tblIn td.td2 div a { color:#999999;  text-decoration:none;}
.tblIn td.td2 div a:hover { color:#0066cc;  text-decoration:none;}

.tblIn td.td2 div.div1 {/* background:url(../modules/hnjtamis/mainEx/images/icon-starG.gif) right top no-repeat;*/}
.tblIn td.td2 div.divOK {color:#000000; background:url(../modules/hnjtamis/mainEx/images/icon-starOK.gif) right top no-repeat;}
.tblIn td.td2 div.div2 {}
.tblIn td.td2 div.divAN {color:#000000; background:url(../modules/hnjtamis/mainEx/images/icon-answer.gif) right top no-repeat;}
.inputThemeCss {font-size: 16px;text-align:left;border: 1px solid #000000;BORDER-LEFT-STYLE: none;BORDER-RIGHT-STYLE: none;BORDER-TOP-STYLE: none; color: #244686; border-right-color:#000000;vertical-align: bottom;}
.R_btn01 {color: #FFF;border: 0px solid #478903;width: 115px;height: 36px;margin-right: 4px;cursor: pointer;background-color: #2694d4;background-image: url(../modules/hnjtamis/mainEx/images/ks_btn01.jpg);}
.R_btn02 {color: #FFF;border: 0px solid #478903;width: 115px;height: 36px;margin-right: 4px;cursor: pointer;background-color: #2694d4;background-image: url(../modules/hnjtamis/mainEx/images/ks_btn02.jpg);}
.R_btn03 {color: #FFF;border: 0px solid #478903;width: 115px;height: 36px;margin-right: 4px;cursor: pointer;background-color: #2694d4;background-image: url(../modules/hnjtamis/mainEx/images/ks_btn05.jpg);}
.R_btn04 {color: #FFF;border: 0px solid #478903;width: 115px;height: 36px;margin-right: 4px;cursor: pointer;background-color: #2694d4;background-image: url(../modules/hnjtamis/mainEx/images/ks_btn04.jpg);}
</style>
</head>
<body id="onlineMoniBody">
<table width="1000" border="0" cellspacing="0" cellpadding="0" align="center">
  <tr>
    <td height="37" align="right">
    <!--第一行 签到-->
   		<div style="width:115;height:41px; margin-right:56px; background:url(../modules/hnjtamis/mainEx/images/qiandao_ok.png) right bottom no-repeat;">
        	<h4 style="float:right; text-align:center; width:62px; font-size:13px; color:#666666; margin:19px 0px 0px 0px;">${signIncount}</h4>
        </div>
    </td>
  </tr>
  <c:if test="${relationType eq 'mocs'}">
  <tr>
    <td height="35" align="center"><!--第二行 页面标题--><img src="../modules/hnjtamis/mainEx/images/title4.png"></td>
  </tr>
  </c:if>
   <c:if test="${relationType ne 'mocs'}">
  <tr>
    <td height="35" align="center"><!--第二行 页面标题--><img src="../modules/hnjtamis/mainEx/images/title5.png"></td>
  </tr>
  </c:if>
  <tr>
    <td height="97" valign="top" style="background:url(../modules/hnjtamis/mainEx/images/labelIN.png) left top no-repeat;">
   	<!--第三行 标题页签部分-->
    <table width="93%" border="0" cellspacing="0" cellpadding="0" align="center">
      <tr>
        <td width="67%" height="72" align="left" valign="bottom">    
        <h4 style="padding-left:120px;margin-top:50px; color:#666666;">${examName}</h4>
        </td>
        <td width="33%" valign="top"><div class="btnIn" onMouseOver="this.style.color='#ddf39a'" onMouseOut="this.style.color='#ffffff'" onClick="back()"><br><br><br>&nbsp;&nbsp;&nbsp;返回首页</div>
        </td>
      </tr>
    </table>
    </td>
  </tr>
  <tr>
    <td height="290" valign="top" align="center" style="background:url(../modules/hnjtamis/mainEx/images/bgCin.png) left top repeat-y;">
    <!--第四行 单选题-->
        <table border="0" cellspacing="0" cellpadding="0" align="center" class="tblIn" id="examTable"></table>
    </td>
  </tr>
  <tr>
    <td height="112" style="background:url(../modules/hnjtamis/mainEx/images/bgBin.png) left top no-repeat;">&nbsp;</td>
  </tr>
  <tr>
    <td style="text-align: center;padding: 5px;padding-bottom: 30px;">
    	<input type="button" name="savebutton" id="savebutton" class="R_btn01" value=" " alt="暂存" onclick="theme.saveAnswer('save')" disabled="disabled"/>
        <input type="button" name="submitbutton" id="submitbutton" class="R_btn02" value=" " alt="交卷" onclick="submitThemeToDb()" disabled="disabled" />        
    </td>
  </tr>
</table>
<script type="text/javascript">
function back(){
	<c:if test="${ny eq 't'}">
	window.location = '${requestHttp}personal/mainpage/listForPersonalMainPageListAction!list.action';
	</c:if>
	<c:if test="${ny ne 't'}">
	window.location = '${requestHttp}mainPageEx/listForMainExListAction!list.action?qtype=${qtype}';
	</c:if>
}
var theme = null;
var onlineMoniBodyMask = new Ext.LoadMask(document.getElementById("onlineMoniBody"), {  
    msg     : '数据正在处理,请稍候',  
    removeMask  : true// 完成后移除  
});
Ext.onReady(function(){
	Ext.Ajax.request({
	    url: '${requestHttp}/onlineExam/getUserForOnlineListAction!getUser.action',
	    timeout: 10000,
	    params: { 
	    	id: '${testpaperId}'//exam_user_testpaper表id=4028e4784d2c3a98014d2c3aefea001b
	    },
	    success: function(response) {
	    	var result = Ext.decode(response.responseText).rsContent;
	    	result = (Ext.decode(result));
	    	if(result.result == undefined || result.result == 'undefined' || result.result =='' || result.result ==null){
	    		Ext.Msg.alert('提示',result.message);
	    	}else{
	    		theme = Ext.create('modules.hnjtamis.mainEx.exam.ThemeFastMoni',{requestHttp:'${requestHttp}',examUser : result.result,isHidden:'${isHidden}',isRight:'${isRight}',tabid : '${tabid}',onlineMoniBodyMask : onlineMoniBodyMask,relationType:'${relationType}'});
	    		//document.onkeydown = function(){theme.themeOnKey();}
	    		theme.backFun = function(){back();}
	    	} 
	    },
	    failure: function(result) {
	    	Ext.Msg.alert('提示','获取人员信息出现错误，请与管理员联系！');
	    }
	});
});
function submitThemeToDb(){
	theme.saveAnswer('submit')
}

</script>
</body>
</html>
