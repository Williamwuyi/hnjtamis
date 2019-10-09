<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/tld/c.tld" prefix="c"%>
<!DOCTYPE html>
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
<meta http-equiv="Access-Control-Allow-Origin" content="*">
<title>考试系统</title>
<script type="text/javascript" src="${basePath }base/ext/ext-all.js"></script>
<link href="${basePath}resources/css/ext-all.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="${basePath }modules/hnjtamis/online/examineeLogin/ThemeBase.js"></script>
<script type="text/javascript" src="${basePath }modules/hnjtamis/online/examineeLogin/ThemeMoni.js"></script>
<link href="${basePath}modules/hnjtamis/online/examineeLogin/online.css" rel="stylesheet" type="text/css">
</head>
<body bgcolor="#e0f0fe" text="#000000" style="padding-left: 5px;padding-right: 0px;overflow: hidden;" id="onlineMoniBody">
<table  width="100%" height="100%" border="0" cellspacing="4" cellpadding="0" align="center">
  <tr style="${hiddenStyle};"><!--系统顶部 区域-->
    <td colspan="2" style="height:50px; background:#558f17 url(${basePath }modules/hnjtamis/online/examineeLogin/images/ks_top01.jpg); margin:0px;">
        <table border="0" cellspacing="0" cellpadding="0" align="left">
          <tr>
            <td><img src="${basePath }modules/hnjtamis/online/examineeLogin/images/ks_toplogo.jpg" width="393" height="49" /></td>
            <td class="logodiv" valign="bottom" id="examName">&nbsp;</td>
          </tr>
        </table>
    </td>
  </tr>
  <tr>
    <td height="42" style="background:#fff url(${basePath }modules/hnjtamis/online/examineeLogin/images/ks_left01.jpg) left bottom repeat-x; width: 202px; border: 1px solid #a3a3a3;${hiddenStyle};">
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td align="right" valign="bottom" style=" line-height:24px;font-size: 12px;" id="minuteLabel">&nbsp;</td>
        <td style="font-size: 28px;width:130px;" id="minute">&nbsp;</td>
      </tr>
    </table>
    </td>
    <td id="table1" height="100%" rowspan="${tableSpan}" align="center" valign="top" background="${basePath }modules/hnjtamis/online/examineeLogin/images/ks_bg.jpg" 
    	style="border: 1px solid #a3a3a3;width:100%;font-size: 12px;">
    <table width="100%" height="100%" id="table2" border="0" cellspacing="8" cellpadding="0">
    <form id="form1" name="form1" method="post" action="">
      <tr>
        <td valign="top">
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr align="left"><!--系统题型、交卷按钮 区域-->
            <td width="34%" height="40" style="font-weight: bold; font-size: 14px; border-bottom: 1px dashed #555555; color: #000;" id="themeTypeLabel">&nbsp;</td>
            <td width="66%" align="right" style=" border-bottom: 1px dashed #555555;" >
            	<input type="button" name="savebutton" id="savebutton" class="R_btn01" value=" " alt="暂存" onclick="theme.saveAnswer('save')" disabled="disabled"/>
            	<input type="button" name="submitbutton" id="submitbutton" class="R_btn02" value=" " alt="交卷" onclick="submitThemeToDb()" disabled="disabled" />
            </td>
          </tr>
        </table>
        <!--问题区 开始-->
        <div id="themeTable">
        <table width="97%" border="0" cellpadding="0" cellspacing="0" class="R_answer" align="center" >
          <tr>
            <th colspan="2" class="R_timu">&nbsp;</th>
          </tr>
          <tr>
            <td height="32">&nbsp;</td>
            <td>&nbsp;</td>
          </tr>
          <tr>
            <td height="32">&nbsp;</td>
            <td>&nbsp;</td>
          </tr>
          <tr>
            <td height="32">&nbsp;</td>
            <td>&nbsp;</td>
          </tr>
             <tr>
            <td height="32">&nbsp;</td>
            <td>&nbsp;</td>
          </tr>
        </table>
        </div>
        <!--问题区 结束-->
        </td>
      </tr>
      <tr>
        <td valign="bottom" align="center">
        <table width="97%" border="0" cellspacing="0" cellpadding="0">
          <!--答题区 开始-->
          <tr>
            <td style="text-align: left; font-size: 14px; color: #000;" id="userAns"><span  class="R_answer_yx" >&nbsp;</span></td>
            <td width="60%" height="40" align="right" id="userAnsSelectedBt" style="text-align: right;">&nbsp;</td>
          </tr>
          <tr>
            <td height="39" colspan="2" style="text-align: right; text-indent: 7px; font-size: 14px; color: #000;">
               <c:if test="${isRight eq 'true'}">
              	<input type="button" name="rightAnsBt" id="rightAnsBt" class="R_btn04" value="查看答案" onclick="theme.rightAnsShow()"/>
              </c:if>
              <input type="button" name="jdHideBt" id="jdHideBt" class="R_btn04" value="隐藏进度" onclick="theme.jdTrHide()"/>
              <input type="button" name="signFlagBt" id="signFlagBt" class="R_btn04" value="标记" onclick="theme.setSignFlag()" disabled="disabled"/>
              <input type="button" name="prevThemeBt" id="prevThemeBt" class="R_btn04" value="上一题" onclick="theme.prevTheme()" disabled="disabled"/>
              <input type="button" name="nextThemeBt" id="nextThemeBt" class="R_btn04" value="下一题" onclick="theme.nextTheme()" disabled="disabled"/>
            </td>
          </tr>
          <!--答题区 结束-->
          <!--进度条 开始-->
          <tr>
            <td height="9" colspan="2" align="left" id="jdBfbTd"><div class="R_pro"><img id="jdBfb" src="${basePath }modules/hnjtamis/online/examineeLogin/images/ks_pro01.jpg" height="9" width="0%" style="padding-bottom: 4px;" /></div></td>
          </tr>
          <tr>
            <td height="20" colspan="2" align="left"><img src="${basePath }modules/hnjtamis/online/examineeLogin/images/ks_pro02.jpg" align="absbottom" style="display:inline;"/><span style="font-size: 12px; color: #000;" id="themeJdContent">进度：0% (0/0)</span></td>
          </tr>
          <!--进度条 结束-->
          <tr id="jdTr">
            <td align="left" colspan="2">
            <!--进度页签列表 开始-->
            <table border="0" cellspacing="0" cellpadding="0" style="display:inline;">
              <tr>
                <td id="themeSortTable" style="height: 55px;">&nbsp;</td>
                <td align="left" valign="top" id="themeSortTab">&nbsp;</td>
              </tr>
            </table>
            <!--进度页签列表 开始-->
          </td>
          </tr>
          </table>
        </td>
      </tr>
     </form>
    </table>
    </td>
  </tr>
  <tr>
    <td height="auto" valign="top" background="${basePath }modules/hnjtamis/online/examineeLogin/images/ks_bg.jpg" style="border:1px solid #a3a3a3;${hiddenStyle};">
    <table width="202" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td align="center"><img src="${basePath }modules/hnjtamis/online/examineeLogin/images/ks_left02.jpg" /></td>
      </tr>
      <tr>
        <td height="336" valign="top" background="${basePath }modules/hnjtamis/online/examineeLogin/images/ks_left03.jpg">
   	    <!--考生信息 开始-->
        <table width="202" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td height="22" class="left_zi1">姓名</td>
          </tr>
          <tr>
            <td height="20" class="left_zi2" id="userName">&nbsp;</td>
          </tr>
          <tr>
            <td height="22" class="left_zi1">身份证</td>
          </tr>
          <tr>
            <td height="20" class="left_zi2" id="idNumber">&nbsp;</td>
          </tr>
          <tr>
            <td height="22" class="left_zi1">准考证号</td>
          </tr>
          <tr>
            <td height="20" class="left_zi2" id="inticket">&nbsp;</td>
          </tr>
          <tr>
            <td height="22" class="left_zi1">机构</td>
          </tr>
          <tr>
            <td height="20" class="left_zi2" id="organName">&nbsp;</td>
          </tr>
          <tr>
            <td height="22" class="left_zi1">开始时间</td>
          </tr>
          <tr>
            <td height="20" class="left_zi2" id="examStartTime">&nbsp;</td>
          </tr>
          <tr>
            <td height="22" class="left_zi1">结束时间</td>
          </tr>
          <tr>
            <td height="20" class="left_zi2" id="examEndTime">&nbsp;</td>
          </tr>
          <tr>
            <td height="22" class="left_zi1">&nbsp;</td>
          </tr>
          <tr>
            <td height="20" class="left_zi2" id="synchroTime">&nbsp;</td>
          </tr>
          <tr>
            <td height="22" class="left_zi1">&nbsp;</td>
          </tr>
          <tr>
            <td height="20" class="left_zi2" id="nextsynchroTime">&nbsp;</td>
          </tr>
        </table>
   	    <!--考生信息 结束-->
        </td>
      </tr>
    </table>
      <img src="${basePath }modules/hnjtamis/online/examineeLogin/images/ks_left04.jpg" width="184" height="36" style="margin:40px 0px 0px 9px;cursor:pointer;"
	   onclick="openWin('examHelp','${basePath}modules/hnjtamis/online/examineeLogin/examhelp.html')" /></td>
  </tr>
  <tr><!--系统底部 区域-->
    <td colspan="2" style="height:24px; background-color:#b5d2f0;">&nbsp;</td>
  </tr>
</table>
<script type="text/javascript">
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
	    		theme = Ext.create('modules.hnjtamis.online.examineeLogin.ThemeMoni',{requestHttp:'${requestHttp}',examUser : result.result,isHidden:'${isHidden}',isRight:'${isRight}',tabid : '${tabid}',onlineMoniBodyMask : onlineMoniBodyMask});
	    		document.onkeydown = function(){theme.themeOnKey();}
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