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
<%@ taglib uri="/WEB-INF/tld/fmt.tld" prefix="fmt"%>
<%
	request.setCharacterEncoding("UTF-8");
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	request.setAttribute("basePath",basePath);
	pageContext.setAttribute("venter", "\n");
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>培训系统-个人达标情况</title>
<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=utf-8">
<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache">
<META HTTP-EQUIV="Expires" CONTENT="0">
<link href="${basePath}resources/css/ext-all.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${basePath}base/ext/ext-all.js"></script>
<style type="text/css">
body {
	font: 94%/1.2 "microsoft yahei";
	background-color: #eff5ff;
	margin: 0;
	padding: 0;
	color: #454545;
	/*border:1px solid #f00;*/
}
.proDiv { width:536px; height:26px; background:url(../modules/hnjtamis/mainEx/images/process_bg.gif) no-repeat; font-size:13px;}
img.popic { cursor:pointer;}
.btnDiv { display:inline; float:left; text-align:center; width:131px; height:60px; line-height:15px; color:#f2fbff; cursor:pointer;}
.btnMar { margin-left:17px;}
.listbl { width:86%; margin:auto 0px; padding:0px;}
.listbl td { border-bottom:1px dashed #afafaf; height:36px; line-height:36px; color:#454545; cursor:pointer;}
.listbl td.td1 { width:3%; font-size:13px;}
.listbl td.td2 { width:45%; width:auto;}
.listbl td.td3 { width:35%; font-size:13px; background:url(../modules/hnjtamis/mainEx/images/pro2_bg.png) 130px center no-repeat;}
.td3-1 { display:inline; float:left; width:130px;}
.td3-2 { display:inline; float:left; margin-left:1px; margin-top:14px; width:3px; height:10px;}
.td3-3 { display:inline; float:left; margin-top:14px; width:130px; height:10px;}
.listbl td.td4 { width:17%; font-size:13px;}
.listbl td.td5 { width:58%; width:auto;}
.listbl td.td6 { width:15%; font-size:13px;}
.listbl td.td7 { width:16%; font-size:13px;}
.listbl td.td8 { width:8%; font-size:13px;}
.listbl td.td9 { width:49%;}
.listbl td.td9 span { float:left; display:inline;}
.listbl td.td9 a { float:right; display:inline; padding:0px 12px 0px 20px; margin:0px; font-size:13px; color:#454545; text-decoration:none;}
.listbl td.td9 a:hover { color:#0066cc; text-decoration:none;}
.listbl th { width:auto; font-size:13px;background:url(../modules/hnjtamis/mainEx/images/all_open.png) right bottom no-repeat;}
.listbl th div { float:right; width:180px; height:30px; margin-right:120px; text-align:center; line-height:30px; font-size:12px; color:#666666; font-weight:normal; cursor:pointer;}
.listbl th a:hover { margin-right:180px; line-height:30px; font-size:12px; color:#0066cc; text-decoration:none; font-weight:normal;}
</style>
</head>
<script type="text/javascript">
var examArray = [];
function openTk(themebankid,name,examType){
	if(examType == 'exeExam'){
		window.location = "${basePath}onlineExam/examFastIndexUserMoniForOnlineListAction!examFastIndexUserMoni.action?relationType=moni&themeBankId="+themebankid+"&isHidden=1&isRight=true&examName="+name; 
	}else if(examType == 'fin'){
		 Ext.Msg.confirm('提示', '你确定要重新进行模拟考试吗，您将重新开始答题?',function(bt){
	         if(bt=='yes'){
	            window.location = "${basePath}onlineExam/examFastIndexUserMoniForOnlineListAction!examFastIndexUserMoni.action?relationType=moni&themeBankId="+themebankid+"&isHidden=1&isRight=true&examName="+name;
	         }
	     });
	}else{
		 var isFlag = false;
	   	 for(var i=0;i<examArray.length;i++){
	   		 if(examArray[i][0] == themebankid){
	   			 if(examArray[i][2]!=null){
	   				 isFlag = true;
	   			 }
	   			 break;
	   		 }
	   	 }
	   	 if(isFlag){
	   	 	window.location = "${basePath}onlineExam/examFastIndexUserMoniForOnlineListAction!examFastIndexUserMoni.action?relationType=moni&themeBankId="+themebankid+"&isHidden=1&isRight=true&examName="+name;
	   	 }else{
	   		 Ext.Msg.alert("提示","试题正在加载中，请稍后！");
	   	 }
	}
 }
function initExam(){
	querySignIn();
	for(var i = 0;i<examArray.length;i++){
		setExamArray(examArray[i][1]);
	}
}
function setExamArray(examUrl){
	Ext.Ajax.request({
			timeout: 6000000,
		url : examUrl+"&ajaxType=init",
		//async : false,//同步
		success: function(response) {
    		var result = Ext.decode(response.responseText);
    		if(result){
    			for(var k = 0;k<examArray.length;k++){
    				if(examArray[k][0] == result.relationId){
    					examArray[k][2] = result.id;
    					//document.getElementById("jz_"+result.relationId).style.display = "";
    					break;
    				}
    			}
    		}
    		
		}
	});
}

function showAllTd(divObj,tdName,maxShowNum){
	var displayTxt = "none";
	if(divObj.innerHTML == '展开全部 +'){
		divObj.innerHTML = '收拢全部 -';
		displayTxt = "";
	}else{
		divObj.innerHTML = '展开全部 +';
	}
	var tdObj = document.getElementById(tdName+maxShowNum);
	if(tdObj){
		//if(tdObj.style.display == "none"){
			//displayTxt = "";
		//}
		for(var i=maxShowNum;i<100000;i++){
			var tdObj = document.getElementById(tdName+i);
			if(tdObj){
				tdObj.style.display = displayTxt;
			}else{
				break;
			}
		}
	}
	
}
function redirectIndex(){
	var redirect = '${basePath}index.jsp?theme=${usersessTheme}&lgt=nomal'; 
    window.location = redirect;
}
function showStandard(){
	var redirect = '${basePath}mainPageEx/stlistForMainExListAction!stlist.action?qtc=${qtc}'; 
    window.location = redirect;
}



var isSignIn = true;
function querySignIn(){
	Ext.Ajax.request({
		timeout: 6000000,
		url : '${basePath}/baseinfo/baseSignIn/querySignInForBaseSignInListAction!querySignIn.action',
		//async : false,//同步
		success: function(response) {
	    	var result = Ext.decode(response.responseText);
	    	if(result && result.signInFlag && result.signInFlag>0){
	    		document.getElementById("signInDiv").style.background = "url(../modules/hnjtamis/mainEx/images/qiandao_ok1.png) right bottom no-repeat";
	    		isSignIn = false;
	    	}
	    		
		}
	});
}

function setSignIn(){
	if(isSignIn){
		Ext.Ajax.request({
			timeout: 5000,
			url : "${basePath}/baseinfo/baseSignIn/saveForBaseSignInFormAction!save.action",
			success: function(response) {
		    	var result = Ext.decode(response.responseText);
		    	if(result && result.msg){
		    		document.getElementById("signInDiv").style.background = "url(../modules/hnjtamis/mainEx/images/qiandao_ok1.png) right bottom no-repeat";
		    		//Ext.Msg.alert('信息', result.msg);
		    	}else{
					Ext.Msg.alert('错误提示', "操作失败");
				}
		    		
			},
			failure : function(response) {
				var result = Ext.decode(response.responseText);
				if (result && result.errors)
					Ext.Msg.alert('错误提示', result.errors);
				else
					Ext.Msg.alert('信息', '后台未响应，网络异常！');
			}
		});
	}
}
</script>
<body>
<table width="1000" border="0" cellspacing="0" cellpadding="0" align="center">
  <tr>
    <td height="37" align="right">
    <!--签到-->
   		<div id="signInDiv" style="width:115;height:41px; margin-right:56px; background:url(../modules/hnjtamis/mainEx/images/qiandao_no.png) right bottom no-repeat; cursor:pointer;" onclick="setSignIn()">
        	<h4 style="float:right; text-align:center; width:62px; font-size:13px; color:#666666; margin:19px 0px 0px 0px;"> </h4>
        </div>
    </td>
  </tr>
  <tr>
    <td height="35" align="center"><!--页面标题--><img src="../modules/hnjtamis/mainEx/images/title.png"></td>
  </tr>
  <tr>
    <td height="134" valign="middle" style="background:url(../modules/hnjtamis/mainEx/images/labelM.png) left top no-repeat;">
    	<!--标题页签部分-->
        <table width="93%" border="0" cellspacing="0" cellpadding="0" align="center">
          <tr>
            <td width="67%" height="72" align="center" valign="bottom">
            <!--进度条-->
            <div class="proDiv">
                <c:set var="empFinDfl" value="0"></c:set>
                <c:if test="${employeeLearningForm.themenum > 0}">
                	<c:set var="empFinDfl" value="${employeeLearningForm.finthemenum/employeeLearningForm.themenum*100}"></c:set>
                </c:if>
                <div style="display:inline;float:left;margin-left:2px;"> <c:if test="${empFinDfl > 0}"><img src="../modules/hnjtamis/mainEx/images/process_left.gif" width="12" height="26"></c:if></div>
                <div style="display:inline;float:left;width:410px; height:26px; text-align:left;">
                	<div style="width:${empFinDfl}%; height:100%; background:url(../modules/hnjtamis/mainEx/images/process.png) right top no-repeat;"></div>
                </div>
                <div style="display:inline;float:left;width:110px; height:26px; text-align:center; line-height:26px;"><c:if test="${employeeLearningForm.themenum >= 0}">${employeeLearningForm.finthemenum}/${employeeLearningForm.themenum}(<fmt:formatNumber value ="${empFinDfl}" pattern="0" />%)</c:if></div>
            </div>
            </td>
            <td width="33%">
            <!--其它按钮-->
                <div class="btnDiv btnMar" onMouseOver="this.style.color='#ddf39a'" onMouseOut="this.style.color='#ffffff'" onClick="showStandard()"><br><br>查看岗位标准</div>
                <div class="btnDiv" onMouseOver="this.style.color='#ddf39a'" onMouseOut="this.style.color='#ffffff'" onClick="redirectIndex()"><br><br>切换回原首页</div>
            </td>
          </tr>
        </table>
    </td>
  </tr>
  <tr>
    <td height="290" valign="top" align="center" style="background:url(../modules/hnjtamis/mainEx/images/bgC.png) left top repeat-y;">
        <!--进行中-->
        <img src="../modules/hnjtamis/mainEx/images/label1.png" style="float:left;"><br>
        <table border="0" cellspacing="0" cellpadding="0" align="center" class="listbl">
          <c:if test="${exeExamList== null || fn:length(exeExamList) == 0}">
          <tr>
            <td class="td1" colspan="4">没有找到对应的题库！</td>
          </tr>
          </c:if>
          <c:if test="${exeExamList ne null || fn:length(exeExamList) > 0}">
          <c:set var="exeExamShowNum" scope="page" value="3"></c:set>
          <c:set var="showMoreTitle" scope="page" value="F"></c:set>
          <c:forEach items="${exeExamList}" var="item" varStatus="state">
           <c:set var="varProgress" scope="page" value="${item.succThemeNum}"></c:set>
           <c:if test="${varProgress eq null || varProgress==''}">
           	  <c:set var="varProgress" scope="page" value="0.00"></c:set>
           </c:if>
           <c:set var="itemmoniScore" scope="page" value="${moniExamScoreMap[item.themeBankId]}"></c:set>
           <c:if test="${itemmoniScore eq null || itemmoniScore==''}">
           	  <c:set var="itemmoniScore" scope="page" value="0.0"></c:set>
           </c:if>
           <c:set var="displayStyle" scope="page" value=""></c:set>
           <c:if test="${state.index >= exeExamShowNum}">
           	  <c:set var="showMoreTitle" scope="page" value="T"></c:set>
           	  <c:set var="displayStyle" scope="page" value="display:none;"></c:set>
           </c:if>
          <tr id="exeExamTd${state.index}" onMouseOver="this.bgColor='#f2f7e7'" onMouseOut="this.bgColor=''" onclick="openTk('${item.themeBankId }','${item.themeBankName }','exeExam');" style="${displayStyle}">
            <td class="td1">&nbsp;${state.index+1}</td>
            <td class="td2">${item.themeBankName}</td>
            <td class="td3" valign="middle">
            	<div class="td3-1">${item.finThemeNum}/${item.themeNum} (${varProgress}%)</div>
                <div class="td3-2"><img src="../modules/hnjtamis/mainEx/images/pro2_left.png" width="3" height="9" align="top"></div>
                <div class="td3-3"><div style="width:${varProgress}%; height:10px; background:url(../modules/hnjtamis/mainEx/images/pro2.png) right top no-repeat;"></div></div>
            </td>
            <td class="td4">模拟考试得分&nbsp;&nbsp;${itemmoniScore}%</td>
          </tr>
          </c:forEach>
          <c:if test="${showMoreTitle == 'T'}">
          <tr>
            <th colspan="4" align="right" valign="bottom" height="56">
            	<div onMouseOver="this.style.color='#0066cc'" onMouseOut="this.style.color='#666666'" onClick="showAllTd(this,'exeExamTd',${exeExamShowNum})">展开全部 +</div>
            </th>
          </tr>
          </c:if>
          <c:if test="${showMoreTitle ne 'T'}">
            <tr>
	            <th colspan="4" align="right" valign="bottom" height="10" style="background:none;">
	            </th>
            </tr>
          </c:if>
          </c:if>
        </table>
        <!--已完成-->
        <img src="../modules/hnjtamis/mainEx/images/label2.png" width="240" height="50" style="float:left;"><br>
        <table border="0" cellspacing="0" cellpadding="0" align="center" class="listbl">
          <c:if test="${finExamList== null || fn:length(finExamList) == 0}">
          <tr>
            <td class="td1" colspan="5">没有找到对应的题库！</td>
          </tr>
          </c:if>
          <c:if test="${finExamList ne null || fn:length(finExamList) > 0}">
          <c:set var="finExamShowNum" scope="page" value="3"></c:set>
          <c:set var="showMoreTitle" scope="page" value="F"></c:set>
          <c:forEach items="${finExamList}" var="item" varStatus="state">
           <c:set var="varProgress" scope="page" value="${item.succThemeNum}"></c:set>
           <c:if test="${varProgress eq null || varProgress==''}">
           	  <c:set var="varProgress" scope="page" value="0.00"></c:set>
           </c:if>
           <c:set var="itemmoniScore" scope="page" value="${moniExamScoreMap[item.themeBankId]}"></c:set>
           <c:if test="${itemmoniScore eq null || itemmoniScore==''}">
           	  <c:set var="itemmoniScore" scope="page" value="0.0"></c:set>
           </c:if>
           <c:set var="itemmoniScoreText" scope="page" value="未达标"></c:set>
           <c:set var="itemmoniScoreColor" scope="page" value="red"></c:set>
           <c:if test="${itemmoniScore!='-' && itemmoniScore*1.0 >=60}">
              <c:set var="itemmoniScoreText" scope="page" value="已达标"></c:set>
              <c:set var="itemmoniScoreColor" scope="page" value="blue"></c:set>
           </c:if>
           <c:set var="displayStyle" scope="page" value=""></c:set>
           <c:if test="${state.index >= finExamShowNum}">
           	  <c:set var="showMoreTitle" scope="page" value="T"></c:set>
           	  <c:set var="displayStyle" scope="page" value="display:none;"></c:set>
           </c:if>
          <tr id="finExamTd${state.index}" onMouseOver="this.bgColor='#f6efdf'" onMouseOut="this.bgColor=''" onclick="openTk('${item.themeBankId }','${item.themeBankName }','fin');" style="${displayStyle}">
            <td class="td1">&nbsp;${state.index+1}</td>
            <td class="td5">${item.themeBankName}</td>
            <td class="td6" style="color:${itemmoniScoreColor};">${itemmoniScoreText}</td>
            <td class="td7">模拟考试得分 ${itemmoniScore}%</td>
            <td class="td8">标准60分</td>
          </tr>
          </c:forEach>
          <c:if test="${showMoreTitle == 'T'}">
          <tr>
            <th colspan="5" align="right" valign="bottom" height="56">
            	<div onMouseOver="this.style.color='#0066cc'" onMouseOut="this.style.color='#666666'" onClick="showAllTd(this,'finExamTd',${finExamShowNum})">展开全部 +</div>
            </th>
          </tr>
          </c:if>
          <c:if test="${showMoreTitle ne 'T'}">
            <tr>
	            <th colspan="5" align="right" valign="bottom" height="10" style="background:none;">
	            </th>
            </tr>
          </c:if>
          </c:if>
        </table>
        <!--未完成-->
        <img src="../modules/hnjtamis/mainEx/images/label3.png" width="240" height="50" style="float:left;"><br>
        <table border="0" cellspacing="0" cellpadding="0" align="center" class="listbl">
          <c:if test="${unFinExamList== null || fn:length(unFinExamList) == 0}">
          <tr>
            <td class="td1" colspan="6">没有找到对应的题库！</td>
          </tr>
          </c:if>
          <c:if test="${unFinExamList ne null || fn:length(unFinExamList) > 0}">
          <c:set var="unfinExamShowNum" scope="page" value="3"></c:set>
          <c:set var="showMoreTitle" scope="page" value="F"></c:set>
          <c:set var="unfinExamIndex" scope="page" value="0"></c:set>
          <c:forEach items="${unFinExamList}" var="item" varStatus="state">
            <c:set var="displayStyle" scope="page" value=""></c:set>
            <c:if test="${unfinExamIndex >= unfinExamShowNum}">
              <c:set var="showMoreTitle" scope="page" value="T"></c:set>
           	  <c:set var="displayStyle" scope="page" value="display:none;"></c:set>
            </c:if>
          	<c:if test="${state.index%2==0}">
          	<tr id="unfinExamTd${unfinExamIndex}" style="${displayStyle}">
            <td class="td9" onMouseOver="this.bgColor='#ecf9ff'" onMouseOut="this.bgColor=''" onclick="openTk('${item.themeBankId }','${item.themeBankName }','unFin');"><span>&nbsp;${item.themeBankName}</span><a href="javascript:openTk('${item.themeBankId }','${item.themeBankName }','unFin');">开始学习</a></td>
            </c:if> 
            <c:if test="${state.index%2==1}">
            <th>&nbsp;</th>
            <td class="td9" onMouseOver="this.bgColor='#ecf9ff'" onMouseOut="this.bgColor=''" onclick="openTk('${item.themeBankId }','${item.themeBankName }','unFin');"><span>&nbsp;${item.themeBankName}</span><a href="javascript:openTk('${item.themeBankId }','${item.themeBankName }','unFin');">开始学习</a></td>
          	 </tr>
          	 <c:set var="unfinExamIndex" scope="page" value="${unfinExamIndex+1}"></c:set>
          	 </c:if>
          	 <script type="text/javascript">
				examArray[examArray.length] = ["${item.themeBankId }","${basePath}onlineExam/examIndexUserMoniForOnlineListAction!examIndexUserMoni.action?relationType=moni&themeBankId=${item.themeBankId }",null];
			</script>
          </c:forEach>
          <c:if test="${showMoreTitle == 'T'}">
          <tr>
            <th colspan="5" align="right" valign="bottom" height="56">
            	<div onMouseOver="this.style.color='#0066cc'" onMouseOut="this.style.color='#666666'" onClick="showAllTd(this,'unfinExamTd',${unfinExamShowNum})">展开全部 +</div>
            </th>
          </tr>
          </c:if>
          <c:if test="${showMoreTitle ne 'T'}">
            <tr>
	            <th colspan="5" align="right" valign="bottom" height="10" style="background:none;">
	            </th>
            </tr>
          </c:if>
          </c:if>
        </table>
    </td>
  </tr>
  <tr>
    <td height="112" style="background:url(../modules/hnjtamis/mainEx/images/bgB.png) left top no-repeat;">&nbsp;</td>
  </tr>
</table>
<script type="text/javascript">
	initExam();
</script>
</body>
</html>
