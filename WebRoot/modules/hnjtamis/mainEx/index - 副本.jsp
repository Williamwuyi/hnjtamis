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
	font-family:"microsoft yahei";
	background-color: #eff5ff;
	margin: 0;
	padding: 0;
	color: #454545;
}
.proDiv { font-family:"microsoft yahei";width:536px; height:26px; background:url(../modules/hnjtamis/mainEx/images/process_bg.gif) no-repeat; font-size:13px;}
.btnDiv { font-family:"microsoft yahei";display:inline; float:left; text-align:center; width:131px; height:60px; line-height:15px; color:#f2fbff; cursor:pointer;}
.btnMar { font-family:"microsoft yahei";margin-left:17px;}

.navleft {font-family:"microsoft yahei";display:inline; float:left; margin-left:84px; font-size:15px;}
.navleft span { font-family:"microsoft yahei";color:#1b89de;}
.navleft span a { font-family:"microsoft yahei";text-decoration:none; color:#1b89de;}
.navleft span a:hover { font-family:"microsoft yahei";text-decoration:none; color:#f30}
.navright {font-family:"microsoft yahei";display:inline; float:right; margin-right:50px; font-size:12px;}
.navright a { font-family:"microsoft yahei";text-decoration:none; color:#666; margin-right:30px;}
.navright a:hover { font-family:"microsoft yahei";text-decoration:none; color:#f30}

.listbl0 { font-family:"microsoft yahei";width:86%; margin:auto 0px; padding:0px;}
.listbl0_L {font-family:"microsoft yahei";background:url(../modules/hnjtamis/mainEx/images/dot_line.gif) center top repeat-y;}

.tdlan_open {font-family:"microsoft yahei";background: url(../modules/hnjtamis/mainEx/images/dot_number1.gif) left top no-repeat; height:42px; cursor:pointer;}
.tdlan_open .tdlan01 {font-family:"microsoft yahei";margin-left:11px;float:left; line-height:34px; font-family:'Lucida Sans Unicode', 'Lucida Grande', sans-serif; font-size:26px; color:#fff; }
.tdlan_open .tdlan02 {font-family:"microsoft yahei";margin-left:24px;float:left; font-size:15px; line-height:37px; color:#424242; font-weight:bold;}
.tdlan_open .tdlan03 {font-family:"microsoft yahei";margin-right:10px;float:right; font-size:12px; line-height:37px; color:#666;}
.tdlan_open .tdlan04 {font-family:"microsoft yahei";margin-right:10px;float:right; font-size:12px; line-height:37px; color:#666;}
.tdlan_close {font-family:"microsoft yahei";background: url(../modules/hnjtamis/mainEx/images/dot_number0.gif) left top no-repeat; height:42px;cursor:pointer;}
.tdlan_close .tdlan01 {font-family:"microsoft yahei";margin-left:11px;float:left; line-height:34px; font-family:'Lucida Sans Unicode', 'Lucida Grande', sans-serif; font-size:26px; color:#1080d5; }
.tdlan_close .tdlan02 {font-family:"microsoft yahei";margin-left:24px;float:left; font-size:15px; line-height:37px; color:#424242;}
.tdlan_close .tdlan03 {font-family:"microsoft yahei";margin-right:10px;float:right; font-size:12px; line-height:37px; color:#888;}
.tdlan_close .tdlan04 {font-family:"microsoft yahei";margin-right:10px;float:right; font-size:12px; line-height:37px; color:#666;}

.tdlan_open .tdlan04 a { font-family:"microsoft yahei";text-decoration:none; color:#666; margin-right:12px;}
.tdlan_open .tdlan04 a:hover { font-family:"microsoft yahei";text-decoration:none; color:#f30; margin-right:12px;}
.tdlan_close .tdlan04 a { font-family:"microsoft yahei";text-decoration:none; color:#666; margin-right:12px;}
.tdlan_close .tdlan04 a:hover { font-family:"microsoft yahei";text-decoration:none; color:#f30; margin-right:12px;}


.tdunit_open {font-family:"microsoft yahei";height:35px; background:#ebebeb url(../modules/hnjtamis/mainEx/images/dot_1.png) 18px 8px no-repeat; font-weight:bold; cursor:pointer;}
.tdunit_closeS {font-family:"microsoft yahei";height:35px; background: url(../modules/hnjtamis/mainEx/images/dot_1.png) 18px 8px no-repeat; cursor:pointer;}
.tdunit_closeD {font-family:"microsoft yahei";height:35px; background:#f5f5f5 url(../modules/hnjtamis/mainEx/images/dot_1.png) 18px 8px no-repeat; cursor:pointer;}
.tdunit_over {font-family:"microsoft yahei";height:35px; background:#ebebeb url(../modules/hnjtamis/mainEx/images/dot_1.png) 18px 8px no-repeat; cursor:pointer;}
.tdunit_tle { font-family:"microsoft yahei";display:inline; float:left; width:200px; margin-left:45px; font-size:15px; line-height:35px;}
.tdunit_proc {font-family:"microsoft yahei"; display:inline; float:left; width:280px; margin-left:30px; height:33px; padding-top:2px; background:url(../modules/hnjtamis/mainEx/images/pro2_bg.png) left center no-repeat;}
.tdlan_btm {font-family:"microsoft yahei";height:25px; background:url(../modules/hnjtamis/mainEx/images/dot_btm.gif) left top no-repeat;}

.tdunitIII {width:100%; height:auto; background-color:#FFF; padding:8px 0px; float:right;}
.listbl { font-family:"microsoft yahei";width:98%; margin:0px 8px 0px 8px; padding:0px;border-top:1px solid #e1e1e1;}
.listbl td {font-family:"microsoft yahei"; border-bottom:1px solid #e1e1e1; height:32px; color:#454545; cursor:pointer;}
.listbl td.td1 { font-family:"microsoft yahei";width:4%;}
.listbl td.td2 { font-family:"microsoft yahei";width:36%; font-size:13px; text-indent:2px;}
.listbl td.td3 { font-family:"microsoft yahei";width:35%; font-size:13px; background:url(../modules/hnjtamis/mainEx/images/pro2_bg.png) left center no-repeat;}
.td3-1 { font-family:"microsoft yahei";display:inline; float:left; width:120px; font-size:13px; line-height:32px; margin-left:10px; font-weight:normal; /**/}
.td3-2 { font-family:"microsoft yahei";display:inline; float:left; margin-left:1px; margin-top:12px; width:3px; height:10px;}
.td3-3 { font-family:"microsoft yahei";display:inline; float:left; margin-top:12px; width:130px; height:10px;}
.listbl td.td4 { font-family:"microsoft yahei";width:24%; font-size:12px;}
.listbl td.td4 a { font-family:"microsoft yahei";text-decoration:none; color:#666; margin-right:12px;}
.listbl td.td4 a:hover { font-family:"microsoft yahei";text-decoration:none; color:#f30; margin-right:12px;}
</style>

<script type="text/javascript">
var examArray = [];
var moniExamArray = [];
var qtype = "${qtype}";
var exeBodyMask = null;
function openXxTk(themebankid,name){
	if(exeBodyMask == null){
		exeBodyMask = new Ext.LoadMask(document.getElementById("examBodyId"), {  
		    msg     : '数据正在处理,请稍候',  
		    removeMask  : true// 完成后移除  
		});
	}
	exeBodyMask.show();
	window.location = "${basePath}study/studyTheme/showStudyThemelistForStudyThemeListAction!showStudyThemelist.action?themeBankId="+themebankid+"&qtype="+qtype;
}
function openBjTk(themebankid,name){
	if(exeBodyMask == null){
		exeBodyMask = new Ext.LoadMask(document.getElementById("examBodyId"), {  
		    msg     : '数据正在处理,请稍候',  
		    removeMask  : true// 完成后移除  
		});
	}
	exeBodyMask.show();
	window.location = "${basePath}study/studyTheme/showStudyThemelistForStudyThemeListAction!showStudyThemelist.action?showSign=1&themeBankId="+themebankid+"&qtype="+qtype;
}

function openMoni(typeId,typeName,themebankid){
	if(exeBodyMask == null){
		exeBodyMask = new Ext.LoadMask(document.getElementById("examBodyId"), {  
		    msg     : '数据正在处理,请稍候',  
		    removeMask  : true// 完成后移除  
		});
	}
	var isAddFlag = true;
	var isJzFlag = true;
	for(var k = 0;k<moniExamArray.length;k++){
		if(moniExamArray[k][0] == typeId){
			if(moniExamArray[k][1]!=null){
				isJzFlag = false;
			}
			isAddFlag = false;
			break;
		}
	}
	if(isAddFlag){
		moniExamArray[moniExamArray.length] = [typeId,null];
	}
	if(isJzFlag){
		var examUrl = "${basePath}onlineExam/examIndexUserMoniForOnlineListAction!examIndexUserMoni.action?relationType=mocs&typeId="+typeId+"&isHidden=1&isRight=true&qtype="+qtype;
		Ext.Ajax.request({
			timeout: 6000000,
			url : examUrl+"&ajaxType=init",
			//async : false,//同步
			success: function(response) {
				var result = Ext.decode(response.responseText);
				if(result && result.relationId && result.relationId.indexOf("@")!=-1){
					for(var k = 0;k<moniExamArray.length;k++){
						if(moniExamArray[k][0] == result.relationId.split("@")[0]){
							moniExamArray[k][1] = result.id;
							break;
						}
					}
				}
			}
		});
	}
	Ext.Msg.confirm('提示', '您将进行本模块的模拟测试，抽取试题可能需要几秒钟，本次生成试题如未交卷，只在今天内有效，交卷后可以查看得分，并且交卷后再次点击模拟测试，系统会重新生成一套模拟试卷?',function(bt){
        if(bt=='yes'){
        	exeBodyMask.show();
        	var moniExamIntr = setInterval(function(){
        		var isMoniExam = false;
        		for(var k = 0;k<moniExamArray.length;k++){
        			if(moniExamArray[k][0] == typeId && moniExamArray[k][1] != null){
        				isMoniExam = true;
        				break;
        			}
        		}
        		if(isMoniExam){
        			clearInterval(moniExamIntr);
        			window.location = "${basePath}onlineExam/examFastIndexUserMoniForOnlineListAction!examFastIndexUserMoni.action?relationType=mocs&typeId="+typeId+"&isHidden=1&isRight=true&qtype="+qtype;
        		}
        	},300);
        }
    });
	
}
function openTk(themebankid,name,examType){
	if(examType > 0.0 && examType < 100.0){
		window.location = "${basePath}onlineExam/examFastIndexUserMoniForOnlineListAction!examFastIndexUserMoni.action?relationType=moni&themeBankId="+themebankid+"&isHidden=1&isRight=true&qtype="+qtype;
	}else if(examType == 100.0){
		 Ext.Msg.confirm('提示', '你确定要重新进行模拟考试吗，您将重新开始答题?',function(bt){
	         if(bt=='yes'){
	            window.location = "${basePath}onlineExam/examFastIndexUserMoniForOnlineListAction!examFastIndexUserMoni.action?relationType=moni&themeBankId="+themebankid+"&isHidden=1&isRight=true&qtype="+qtype;
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
	   	 	window.location = "${basePath}onlineExam/examFastIndexUserMoniForOnlineListAction!examFastIndexUserMoni.action?relationType=moni&themeBankId="+themebankid+"&isHidden=1&isRight=true&qtype="+qtype;
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
	var redirect = '${basePath}mainPageEx/stlistForMainExListAction!stlist.action?qtc=${qtc}&qtype='+qtype;
    window.location = redirect;
}

function query(_qtype){
	if(_qtype == qtype){
		_qtype = "";
	}
	qtype = _qtype;
	window.location = '${basePath}mainPageEx/listForMainExListAction!list.action?qtype='+qtype;
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
var typeitemsArray = [];
var openTreeNode = "${treeOpenNode}";
function openTb(id){
	var spobj = document.getElementById("span_"+id);
	if(spobj.innerHTML.indexOf('展开')!=-1){
		optTds(id,"");
		spobj.innerHTML = "收起 -";
		if(document.getElementById("lan_"+id) && document.getElementById("lan_"+id).className){
			document.getElementById("lan_"+id).className = "tdlan_open";
		}
		openTreeNode+=","+id+",";
	}else{
		optTds(id,"none");
		spobj.innerHTML = "展开 +";
		var reg=new RegExp(","+id+",","g");  
		openTreeNode = openTreeNode.replace(reg,"");
		if(document.getElementById("lan_"+id) && document.getElementById("lan_"+id).className){
			document.getElementById("lan_"+id).className = "tdlan_close";
		}
		for(var i=0;i<typeitemsArray.length;i++){
			if(typeitemsArray[i][0] == id){
				document.getElementById("span_"+typeitemsArray[i][1]).innerHTML = "展开 +";
				reg=new RegExp(","+typeitemsArray[i][1]+",","g");  
				openTreeNode = openTreeNode.replace(reg,"");
				optTds(typeitemsArray[i][1],"none");
			}
		}
		
	}
	
	Ext.Ajax.request({
		timeout: 5000,
		url : "${basePath}/mainPageEx/saveTreeNodeForMainExListAction!saveTreeNode.action",
		params:{id:openTreeNode},
		success: function(response) {},
		failure : function(response) {}
	});
}
function optTds(id,displayType){
	var trobj = getByName("tr_"+id);
	if(trobj && trobj.length){
		for(var i=0;i<trobj.length;i++){
			trobj[i].style.display = displayType;
		}
	}else if(trobj){
		trobj.style.display = displayType;
	}
}

function getByName(Name){ 
	var aEle=document.getElementsByTagName('*'); 
    var arr=[];  
    for(var i=0;i<aEle.length;i++){
	    if(aEle[i].getAttribute("name")==Name){
	        arr.push(aEle[i])
	    }
    }
    return arr; 
}
</script>
</head>
<body id="examBodyId">
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
        <!--导航条-->
        <div class="navarea">
        	<div class="navleft">您的最近学习记录：<span>
        	<c:if test="${nowStandardTerms ne null && nowStandardTerms.themeBank ne null}">
        		${nowStandardTerms.parentTypeName} -- ${nowStandardTerms.typename} -- <a href="javascript:openXxTk('${nowStandardTerms.themeBank.themeBankId }','${nowStandardTerms.themeBank.themeBankName }')">${nowStandardTerms.themeBank.themeBankName}</a>
        	</c:if>
        	<c:if test="${nowStandardTerms eq null || nowStandardTerms.themeBank eq null}">
        		无。
        	</c:if></span></div>
            <div class="navright">&nbsp;<a href="javascript:query('finExam')"><font style="<c:if test="${qtype eq 'finExam'}">font-weight: bold;font-size:13px;</c:if>">查看已完成学习</font></a><a href="javascript:query('exeExam')"><font style="<c:if test="${qtype eq 'exeExam'}">font-weight: bold;font-size:13px;</c:if>">查看进行中学习</font></a></div>
        </div><br><br>
        <!--一级大类一通用基础知识-->
        <c:set var="varProgressMin" scope="page" value="90.0"></c:set>
        <c:if test="${typelist eq null || fn:length(typelist) == 0}">
        	<br><br><br><img src="../modules/hnjtamis/mainEx/images/record-no.png" />
        </c:if>
        <c:if test="${typelist ne null && fn:length(typelist) > 0}">
        <c:forEach items="${typelist}" var="item" varStatus="state">
        <c:set var="treeNodeId" value=",${item.typeId},"></c:set>
        <table border="0" cellspacing="6" cellpadding="0" align="center" class="listbl0">
          <tr ondblclick="openTb('${item.typeId}')">
            <!--一级模块-->
            <c:set var="varProgress" scope="page" value="${item.themeBank.succThemeNum}"></c:set>
	        <c:if test="${varProgress eq null || varProgress==''}">
	           <c:set var="varProgress" scope="page" value="0.00"></c:set>
	        </c:if>
	        <c:set var="jdleftImg" scope="page" value="pro2_left.png"></c:set>
	        <c:set var="jdImg" scope="page" value="pro2.png"></c:set>
	        <c:if test="${varProgress<varProgressMin}">
	           <c:set var="jdleftImg" scope="page" value="pro3_left.png"></c:set>
	           <c:set var="jdImg" scope="page" value="pro3.png"></c:set>
	        </c:if>
	        <c:set var="tdlanClassName" scope="page" value="tdlan_open"></c:set>
	        <c:if test="${treeOpenNode.indexOf(treeNodeId)==-1}"><c:set var="tdlanClassName" scope="page" value="tdlan_close"></c:set></c:if>
            <td class="${tdlanClassName}" colspan="2" valign="top" id="lan_${item.typeId}">
              <span class="tdlan01">${state.index+1}</span>
              <span class="tdlan02">
	              <div class="tdlan02" style="width: 120px;">${item.typename}</div>
	              <div class="tdunit_proc">
	                <div class="td3-2"><c:if test="${varProgress > 0.0}"><img src="../modules/hnjtamis/mainEx/images/${jdleftImg}" width="3" height="9" align="top"></c:if></div>
	                <div class="td3-3"><div style="width:${varProgress}%; height:10px; background:url(../modules/hnjtamis/mainEx/images/${jdImg}) right top no-repeat;"></div></div>
	                <div class="td3-1">${item.themeBank.finThemeNum}/${item.themeBank.themeNum}</div>
	              </div>
              </span>
              
              <span class="tdlan03" id="span_${item.typeId}" onclick="openTb('${item.typeId}')">
			      <c:if test="${treeOpenNode.indexOf(treeNodeId)==-1}">展开 +</c:if>
			      <c:if test="${treeOpenNode.indexOf(treeNodeId)!=-1}">收起 -</c:if>        
              </span>
              <span class="tdlan04">
              	<c:if test="${item.themeBank ne null && item.themeBank.themeBankId ne null && item.themeBank.themeBankId ne ''}">
              	<a href="javascript:openMoni('${item.typeId }','${item.typename}','${item.themeBank.themeBankId}')">模拟考试</a>
              	</c:if>
              </span>
            </td>
          </tr>
          <c:set var="khRowFlag" scope="page" value="T"></c:set>
          <c:set var="typeChilde" scope="page" value="${typechildmap[item.typeId]}"></c:set>
          
          <c:if test="${typeChilde eq null || fn:length(typeChilde) == 0}">
        	 <tr name="tr_${item.typeId}" style="<c:if test="${treeOpenNode.indexOf(treeNodeId)==-1}">display:none;</c:if>">
        	 	<td width="36" nowrap  class="listbl0_l"><!--左侧虚线--></td>
            	<td width="auto" class="tdunit_open"  onMouseOver="this.className='tdunit_over'" onMouseOut="this.className='tdunit_open'" style="background:#ebebeb;text-align: center;font-weight: normal;font-size: 13px; width: 99%;">
            		该模块下暂时没有您需要学习的题库！
            	</td>
        	 </tr>
          </c:if>
          <c:if test="${typeChilde ne null && fn:length(typeChilde) > 0}">
          <c:forEach items="${typeChilde}" var="item2" varStatus="state2">
          <c:set var="stChilde" scope="page" value="${typechildmap[item2.typeId]}"></c:set>
          <script type="text/javascript">
			 typeitemsArray[typeitemsArray.length] = ["${item.typeId}","${item2.typeId}"];
		  </script>
		  <c:set var="treeNodeId" value=",${item.typeId},"></c:set>
          <tr ondblclick="openTb('${item2.typeId}')" name="tr_${item.typeId}" style="<c:if test="${treeOpenNode.indexOf(treeNodeId)==-1}">display:none;</c:if>">
          	<c:if test="${khRowFlag == 'T'}">
            <td width="36" nowrap rowspan="${item.childeNums}" class="listbl0_l" id="xian_${item2.typeId}"><!--左侧虚线--></td>
            <c:set var="khRowFlag" scope="page" value="F"></c:set>
            </c:if>
            <!--二级模块-->
            <c:set var="varProgress" scope="page" value="${item2.themeBank.succThemeNum}"></c:set>
	        <c:if test="${varProgress eq null || varProgress==''}">
	           <c:set var="varProgress" scope="page" value="0.00"></c:set>
	        </c:if>
	        <c:set var="tdTwoClassName" scope="page" value="tdunit_open"></c:set>
	        <td width="auto" class="${tdTwoClassName}" onMouseOver="this.className='tdunit_over'" onMouseOut="this.className='${tdTwoClassName}'">
              <div class="tdunit_tle">${item2.typename}</div>
              <div class="tdunit_proc">
              	<c:set var="jdleftImg" scope="page" value="pro2_left.png"></c:set>
		        <c:set var="jdImg" scope="page" value="pro2.png"></c:set>
		        <c:if test="${varProgress<varProgressMin}">
		           <c:set var="jdleftImg" scope="page" value="pro3_left.png"></c:set>
		           <c:set var="jdImg" scope="page" value="pro3.png"></c:set>
		        </c:if>
                <div class="td3-2"><c:if test="${varProgress > 0.0}"><img src="../modules/hnjtamis/mainEx/images/${jdleftImg}" width="3" height="9" align="top"></c:if></div>
                <div class="td3-3"><div style="width:${varProgress}%; height:10px; background:url(../modules/hnjtamis/mainEx/images/${jdImg}) right top no-repeat;"></div></div>
                <div class="td3-1">${item2.themeBank.finThemeNum}/${item2.themeBank.themeNum}</div>
              </div>
              <div class="tdlan03" id="span_${item2.typeId}" onclick="openTb('${item2.typeId}')" style="font-weight: normal;text-align: right;font-family:'microsoft yahei';margin-right:10px;float:right; font-size:12px; line-height:37px; color:#666;">
              	  <c:set var="treeNodeId" value=",${item2.typeId},"></c:set>
              	  <c:if test="${treeOpenNode.indexOf(treeNodeId)==-1}">展开 +</c:if>
			      <c:if test="${treeOpenNode.indexOf(treeNodeId)!=-1}">收起 -</c:if>    
              </div>
            <div style="clear:both;"></div>
            <div class="tdunitIII" name="tr_${item2.typeId}" style="<c:if test="${treeOpenNode.indexOf(treeNodeId)==-1}">display:none;</c:if>">
            <table border="0" cellspacing="0" cellpadding="0" align="center" class="listbl">
          	  <c:forEach items="${stChilde}" var="item3" varStatus="state3">
          	  <c:if test="${item3.themeBank ne null}">
          	  	<c:if test="${item3.themeBank.succThemeNum==0}">
          	    <script type="text/javascript">
					examArray[examArray.length] = ["${item3.themeBank.themeBankId }","${basePath}study/studyTheme/studyOnlineExamForStudyThemeListAction!studyOnlineExam.action?relationType=moni&themeBankId=${item3.themeBank.themeBankId }",null];
			    </script>
			    </c:if>
          	   <c:set var="varProgress" scope="page" value="${item3.themeBank.succThemeNum}"></c:set>
	           <c:if test="${varProgress eq null || varProgress==''}">
	           	  <c:set var="varProgress" scope="page" value="0.00"></c:set>
	           </c:if>
	           <c:set var="itemmoniScore" scope="page" value="${moniExamScoreMap[item3.themeBank.themeBankId]}"></c:set>
	           <c:if test="${itemmoniScore eq null || itemmoniScore==''}">
	           	  <c:set var="itemmoniScore" scope="page" value="0.0"></c:set>
	           </c:if>
	           <c:set var="jdleftImg" scope="page" value="pro2_left.png"></c:set>
		        <c:set var="jdImg" scope="page" value="pro2.png"></c:set>
		        <c:if test="${varProgress<varProgressMin}">
		           <c:set var="jdleftImg" scope="page" value="pro3_left.png"></c:set>
		           <c:set var="jdImg" scope="page" value="pro3.png"></c:set>
		        </c:if>
              <tr onMouseOver="this.bgColor='#eff9fe'" onMouseOut="this.bgColor=''" onClick="">
                <td class="td1" align="right"><img src="../modules/hnjtamis/mainEx/images/dot_3.png"></td>
                <td class="td2">${item3.themeBank.themeBankName}</td>
                <td class="td3" valign="middle"  title="${item3.themeBank.finThemeNum}/${item3.themeBank.themeNum} (${varProgress}%) 模拟考试得分&nbsp;&nbsp;${itemmoniScore}%">
                    <div class="td3-2"><c:if test="${varProgress > 0.0}"><img src="../modules/hnjtamis/mainEx/images/${jdleftImg}" width="3" height="9" align="top"></c:if></div>
                    <div class="td3-3"><div style="width:${varProgress}%; height:10px; background:url(../modules/hnjtamis/mainEx/images/${jdImg}) right top no-repeat;"></div></div>
                    <div class="td3-1">${item3.themeBank.finThemeNum}/${item3.themeBank.themeNum}</div>
                </td>
                <td class="td4" align="right"><a href="javascript:openXxTk('${item3.themeBank.themeBankId }','${item3.themeBank.themeBankName }')">在线学习</a><a href="javascript:openTk('${item3.themeBank.themeBankId }','${item3.themeBank.themeBankName }',${item3.themeBank.succThemeNum});">随堂练习</a><a href="javascript:openBjTk('${item3.themeBank.themeBankId }','${item3.themeBank.themeBankName }')">查看标记</a></td>
              </tr>
              </c:if>
              <c:if test="${item3.themeBank eq null}">
              <tr onMouseOver="this.bgColor='#eff9fe'" onMouseOut="this.bgColor=''" onClick="">
                <td class="td1" align="right"><img src="../modules/hnjtamis/mainEx/images/dot_3.png"></td>
                <td class="td2">${item3.standardname}<font color=red>(暂无题库)</font></td>
                <td valign="middle">&nbsp;</td>
                <td class="td4" align="right">&nbsp;</td>
              </tr>
              </c:if>
              </c:forEach>
            </table>
            </div>
            </td>
          </tr>
          </c:forEach>
          </c:if>
          <c:set var="treeNodeId" value=",${item.typeId},"></c:set>
          <tr name="tr_${item.typeId}" style="<c:if test="${treeOpenNode.indexOf(treeNodeId)==-1}">display:none;</c:if>">
            <td class="tdlan_btm" colspan="2" valign="top"></td>
          </tr>
        </table>
        </c:forEach>
		</c:if>
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
