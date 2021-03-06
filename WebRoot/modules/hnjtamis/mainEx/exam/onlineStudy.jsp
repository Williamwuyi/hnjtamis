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
<title>培训系统-随堂联系</title>
<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache">
<META HTTP-EQUIV="Expires" CONTENT="0">
<link href="${basePath}resources/css/ext-all.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${basePath}base/ext/ext-all.js"></script>
<script type="text/javascript" src="${basePath}modules/hnjtamis/mainEx/exam/ThemeFastStudy.js"></script>
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

.tblIn2 a { color:#999999;  text-decoration:none;font-size:13px;}
.tblIn2 a:hover { color:#0066cc;  text-decoration:none;font-size:13px;}

.tblIn td.td2 div.div1 {/* background:url(../../modules/hnjtamis/mainEx/images/icon-starG.gif) right top no-repeat;*/}
.tblIn td.td2 div.divOK {color:#000000; background:url(../../modules/hnjtamis/mainEx/images/icon-starOK.gif) right top no-repeat;}
.tblIn td.td2 div.div2 {}
.tblIn td.td2 div.divAN {color:#000000; background:url(../../modules/hnjtamis/mainEx/images/icon-answer.gif) right top no-repeat;}
.inputThemeCss {font-size: 16px;text-align:left;border: 1px solid #000000;BORDER-LEFT-STYLE: none;BORDER-RIGHT-STYLE: none;BORDER-TOP-STYLE: none; color: #244686; border-right-color:#000000;vertical-align: bottom;}
.R_btn01 {color: #FFF;border: 0px solid #478903;width: 115px;height: 36px;margin-right: 4px;cursor: pointer;background-color: #2694d4;background-image: url(../../modules/hnjtamis/mainEx/images/ks_btn01.jpg);}
.R_btn02 {color: #FFF;border: 0px solid #478903;width: 115px;height: 36px;margin-right: 4px;cursor: pointer;background-color: #2694d4;background-image: url(../../modules/hnjtamis/mainEx/images/ks_btn02.jpg);}
.R_btn03 {color: #FFF;border: 0px solid #478903;width: 115px;height: 36px;margin-right: 4px;cursor: pointer;background-color: #2694d4;background-image: url(../../modules/hnjtamis/mainEx/images/ks_btn05.jpg);}
.R_btn04 {color: #FFF;border: 0px solid #478903;width: 115px;height: 36px;margin-right: 4px;cursor: pointer;background-color: #2694d4;background-image: url(../../modules/hnjtamis/mainEx/images/ks_btn04.jpg);}
.R_btn06 {color: #FFF;border: 0px solid #478903;width: 115px;height: 36px;margin-right: 4px;cursor: pointer;background-color: #2694d4;background-image: url(../../modules/hnjtamis/mainEx/images/ks_btn06.jpg);}
.R_btn07 {color: #FFF;border: 0px solid #478903;width: 115px;height: 36px;margin-right: 4px;cursor: pointer;background-color: #2694d4;background-image: url(../../modules/hnjtamis/mainEx/images/ks_btn07.jpg);}
</style>
</head>
<body id="onlineMoniBody">
<table width="1000" border="0" cellspacing="0" cellpadding="0" align="center">
  <tr>
    <td height="37" align="right">
    <!--第一行 签到-->
   		<div style="width:115;height:41px; margin-right:56px; background:url(../../modules/hnjtamis/mainEx/images/qiandao_ok.png) right bottom no-repeat;">
        	<h4 style="float:right; text-align:center; width:62px; font-size:13px; color:#666666; margin:19px 0px 0px 0px;">${signIncount}</h4>
        </div>
    </td>
  </tr>
  <tr>
    <td height="35" align="center"><!--第二行 页面标题--><img src="../../modules/hnjtamis/mainEx/images/title.png"></td>
  </tr>
  <tr>
    <td height="97" valign="top" style="background:url(../../modules/hnjtamis/mainEx/images/labelIN.png) left top no-repeat;">
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
    <td id="onlineTd" name="onlineTd" height="400" valign="top" align="center" style="background:url(../../modules/hnjtamis/mainEx/images/bgCin.png) left top repeat-y;">
    	<div id="onlineTdDiv"  style="width:86%;">
    		<table border="0" cellspacing="0" cellpadding="0" align="center" class="tblIn2" style="width: 100%;border: 0px;">
    			<tr>
    				<td id="examTableTitle" style="width:65%; font-size: 15px;font-weight:bold;text-align: left;white-space:nowrap;">当前考试情况 - 统计中</td>
    				<td style="width:35%;text-align: right;white-space:nowrap;"><select id="pageIndex" name="pageIndex" onchange="theme.queryTheme()">
    				<%  int themeCount = ((Integer)request.getAttribute("themeCount")).intValue();
    					int pageCount = 0;
    					int themeInPage = 200;
    					if(themeCount == 0){
    						pageCount = 0;
    					}else if(themeCount%themeInPage==0){
    						pageCount = themeCount/themeInPage;
    					}else{
    						pageCount = themeCount/themeInPage+1;
    					}
    					for(int i = 0; i < pageCount; i++){
    						int theme_start = (i*themeInPage+1);
							int theme_end = ((i+1)*themeInPage);
    						if(i==pageCount-1){
    							theme_end = themeCount;
    						}
    						out.print("<option value=\""+i+","+theme_start+","+theme_end+"\">"+theme_start+"~"+theme_end+"题</option>");
    						
    					}
    				%>
    				</select>&nbsp;<a id='sid1' href="javascript:theme.showalltheme();selectType('1');">全部试题</a>&nbsp;<a id='sid2' href="javascript:theme.unLearn();selectType('2');">未学完</a>&nbsp;<a id='sid3' href="javascript:theme.onlyProb();selectType('3');">错误试题</a></td>
    			</tr>
    		</table>
    	</div>
    	<div style="width:86%; height: 92%; overflow-y : scroll;border: 1px solid #8BD6EE;vertical-align: top;" id="examTableDiv" name="examTableDiv">
        <table border="0" cellspacing="0" cellpadding="0" align="center" class="tblIn" id="examTable" name="examTable" style="width:100%;height: 100%;"></table>
        </div>
    </td>
  </tr>
  <tr>
    <td height="22" style="background:url(../../modules/hnjtamis/mainEx/images/bgBin2.png) left top no-repeat;">&nbsp;</td>
  </tr>
  <tr>
    <td style="text-align: center;padding: 10px;padding-bottom: 10px;">
    	<input type="button" name="savebutton" id="savebutton" class="R_btn06" value=" " alt="上传考生答案到服务器" onclick="theme.saveAnswer('save')" disabled="disabled"/>
        <input type="button" name="submitbutton" id="submitbutton" class="R_btn07" value=" " alt="清理用户练习填写的答案，重新做本套试卷" onclick="theme.saveAnswer('saveAndNew')" disabled="disabled" />        
    </td>
   </tr>
</table>
<script type="text/javascript">
var examThemeBodyMask = new Ext.LoadMask(document.getElementById("onlineMoniBody"), {  
    msg     : '数据正在处理,请稍候',  
    removeMask  : true// 完成后移除  
});
function refback(){
	window.location = '${requestHttp}study/studyTheme/studyOnlineExamForStudyThemeListAction!studyOnlineExam.action?relationType=moni&themeBankId=${themeBankId}&isHidden=1&isRight=true&qtype=${qtype}&ny=${ny}';
}
var oldSelectTypIndex = 'none';
function selectType(index){
	if(oldSelectTypIndex != 'none'){
		document.getElementById("sid"+oldSelectTypIndex).style['font-weight'] ='normal';
		document.getElementById("sid"+oldSelectTypIndex).style['color'] ='#B199B2';
	}
	document.getElementById("sid"+index).style['font-weight'] = 'bold';
	document.getElementById("sid"+index).style['color'] = '#454545';
	oldSelectTypIndex = index;
}
selectType('1');
Ext.onReady(function(){
	if(Ext.isIE8){
		
	}else if(Ext.isIE6||Ext.isIE7){
		document.getElementById("onlineTd").style.height = document.body.clientHeight - 270;
	}else{
		document.getElementById("onlineTd").style.height = document.body.clientHeight - 270;
	    document.getElementById("examTableDiv").style.height = document.body.clientHeight - 300;
	}
	examThemeBodyMask.show();
	theme = Ext.create('modules.hnjtamis.mainEx.exam.ThemeFastStudy',{
		requestHttp:'${requestHttp}',
		//examUser : result.result,
		//isHidden:'${isHidden}',
		//isRight:'${isRight}',
		//tabid : '${tabid}',
		testPaperId : '${id}',
		onlineMoniBodyMask : examThemeBodyMask,
		relationType:'${relationType}',
		backFun :  function(){back();},
		refbackFun :  function(){refback();},
		themeCount : ${themeCount},
		themeFraction : ${themeFraction},//总分数
		userRightThemeCount : ${userRightThemeCount},//考生正确题数
		userRightFraction : ${userRightFraction}//考生正确总分数
	});
});
function back(){
	<c:if test="${ny eq 't'}">
		window.location = '${requestHttp}personal/mainpage/listForPersonalMainPageListAction!list.action';
	</c:if>
	<c:if test="${ny ne 't'}">
		window.location = '${requestHttp}mainPageEx/listForMainExListAction!list.action?qtype=${qtype}';
	</c:if>
}
function fkThemeShow(themeId){
	Ext.Ajax.request({
		url : '${requestHttp}/onlineExam/fkThemeShowForOnlineListAction!fkThemeShow.action',
		params : {
			id : themeId
		},
		method : 'POST',
		timeout: 15000,
		success : function(response) {
				var result = Ext.decode(response.responseText);
	    		var ansfktheme = result.fktheme;
	    		var themeFkauditFormList = result.themeFkauditFormList;
	    		var htmlStr = "<div style='width:100%;overflow-x:auto;overflow-y:hidden;word-break: break-all; word-wrap:break-word;'>";
				if(ansfktheme.lastFkState=='20' || ansfktheme.lastFkState=='30' || ansfktheme.lastFkState=='40' || ansfktheme.lastFkState=='-40'){
					htmlStr+="<div style='font-weight: normal; font-size: 14px;width:100%;text-align:right;padding:10px;'>"
					htmlStr+="<input type=\"button\" class=\"R_btn04\" value=\" \" alt=\"关闭\" onclick=\"fkThemeWin.hide();\" />";      
					htmlStr+="</div>";
					if(themeFkauditFormList && themeFkauditFormList.length && themeFkauditFormList.length>0){
						htmlStr+="<div style='overflow-x:auto;overflow-y:auto;height:260px;width:690px;font-weight: normal; font-size: 14px;word-break: break-all; word-wrap:break-word;padding:6px;'>";
						for(var i=0;i<themeFkauditFormList.length;i++){
							var fkauditRemark = themeFkauditFormList[i].fkauditRemark;
							var createdBy = themeFkauditFormList[i].createdBy;
							var creationDate = themeFkauditFormList[i].creationDate;
							var sssss = "<font color='#0000FF'>"+createdBy+"["+creationDate+"]：</font><br>"+fkauditRemark+"<br><br>";
							htmlStr+= replaceString(sssss,"\n","<br>");
						}
						htmlStr+="</div>";
					}
				}else{
					htmlStr+="<div style='font-weight: normal; font-size: 14px;width:100%;text-align:right;padding:10px;'>"
					htmlStr+="<input type=\"button\" class=\"R_btn03\" value=\" \" alt=\"提交反馈\" onclick=\"fkThemeWin.sumitFk();\"/>";
					htmlStr+="<input type=\"button\" class=\"R_btn04\" value=\" \" alt=\"关闭\" onclick=\"fkThemeWin.hide();\" />";      
					htmlStr+="</div>";
					
					htmlStr+="<div style='font-weight: normal; font-size: 20px;width:100%;padding:10px;padding-bottom:3px;padding-top:3px;'>反馈说明：</div>";
					htmlStr+="<div style='font-weight: normal; font-size: 13px;word-break: break-all; word-wrap:break-word;padding:10px;padding-top:3px;'>"
					htmlStr+="<textarea name='fkRemark_"+themeId+"' id='fkRemark_"+themeId+"'  rows='10' cols='300' style='width:650;height:220'></textarea></div>";	
				}
				htmlStr+="</div>";
				htmlStr+="<div style='width:100%;overflow-x:auto;overflow-y:hidden;word-break: break-all; word-wrap:break-word;padding-left:10px;padding-right:10px;padding-top:5px;'>";
				htmlStr+="<font color=red>注意：请详细填写反馈的问题，如：答案A.压强、速度和粘度；存在**问题，不要只反馈：答案A存在错误，因为您查看的答案顺序可能与系统中试题管理中的答案顺序不一致，系统在出题时会根据情况打乱答案顺序。</font>";
				htmlStr+="</div>";
				fkThemeWin = new Ext.Window({
					layout:'fit',
					title:'试题反馈',
					height:400,
					width:700,
					border:false,
					frame:false,
					modal:true,
					autoScroll:true,
					bodyStyle:'overflow-x:auto;background:#FFFFFF;font-size:13px;padding:0px;height:500px;',
					closeAction:'hide',
					html:htmlStr
				});
				fkThemeWin.sumitFk = function(){
					var fkRemark_Code = "fkRemark_"+themeId;
					var fkRemark_Obj =Ext.get(fkRemark_Code);
					var fkRemark = fkRemark_Obj.getValue();
					if(fkRemark==null || fkRemark==''){
						 Ext.Msg.alert('提示', '反馈信息不能为空！');
					}else if(fkRemark.length>1200){
						Ext.Msg.alert('提示', '反馈信息不能大于1200个汉字！');
					}else{
						var jsonStr = "{\"themeId\":\""+ansfktheme.themeId+"\",\"fkRemark\":\""+fkRemark+"\"}";
					   	Ext.Ajax.request({
							url : '${requestHttp}/exam/base/theme/saveFkForThemeFormAction!saveFk.action?op=fk',
							params : {
								json : jsonStr
							},
							method : 'POST',
							timeout: 15000,
							success : function(response) {
								 var result = Ext.decode(response.responseText);
								 Ext.Msg.alert('提示', result.msg, function(){
								 	fkThemeWin.hide();
								 	examThemeBodyMask.show();
								 	location.reload();
								 });
							},
							failure : function() { }
						 });
					}
				}
				fkThemeWin.show();
	    		
	    },
		failure : function() {
		}
	});
}

function replaceString(str,reallyDo,replaceWith) { 
	var e=new RegExp(reallyDo,"g"); 
	words = str.replace(e, replaceWith); 
	return words; 
}
</script>
</body>
</html>
