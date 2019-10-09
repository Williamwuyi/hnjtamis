<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%--
@version: 1.0 个人首页展示
@author: wangyong
@time: 2015.04.16
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
<html>
<head>
	<title>个人首页信息</title>
	<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=utf-8">
	<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
	<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache">
	<META HTTP-EQUIV="Expires" CONTENT="0">
	<link href="${basePath}resources/css/ext-all.css" rel="stylesheet" type="text/css" />
	<link href="${basePath}resources/css/personalmainpage.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="${basePath}base/ext/ext-all.js"></script>
	<!--<script type="text/javascript" src="${basePath}base/include-ext.js"></script>-->
	<script type="text/javascript">
	 var examArray = [];
     function openCourse(id){
     	 window.parent.Ext.require('modules.hnjtamis.courseware.Courseware',function(){
		     var courseware=new window.parent.modules.hnjtamis.courseware.Courseware();
		     courseware.openFormWin(id,null,true);
	     });
     }
     function setShow(){
    	 document.getElementById("ktdiv").style.visibility='visible';
     }
     function setHidden(){
    	 document.getElementById("ktdiv").style.visibility='hidden';
     }
     var tabid = null;
     var refInterval = null;
     window.parent.grablEapLogin.moniKsTableId = null;
     function openTk(themebankid,name){
    	 if(tabid!=null){
    		 var win = window.parent.grablEapLogin.desktop.getWindow("menu_"+tabid);
        	 if(win == undefined){//判断直接点页签关闭时，不刷新页面，停止定时器
        		 clearInterval(refInterval);
     			 refInterval = null;
        		 tabid = null;
        	 }
    	 }
    	 
    	 if(tabid==null){
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
	    	 	tabid = "lxtk"+themebankid;
	    	 	window.parent.grablEapLogin.moniKsTableId = tabid;
	    	 	window.parent.grablEapLogin.createWindow(tabid,"练习题库-"+name,"","${basePath}onlineExam/examIndexUserMoniForOnlineListAction!examIndexUserMoni.action?relationType=moni&themeBankId="+themebankid+"&isHidden=1&tabid="+tabid+"&isRight=true");
	    	 	refInterval = setInterval("refUrl()",1000);
	    	 }else{
	    		 Ext.Msg.alert("提示","试题正在加载中，请稍后！");
	    	 }
    	 }else{
    		 Ext.Msg.alert("提示","您已经加载了一场考试！");
    	 }
      }
  </script>
  <style type="text/css">
img{ border:none }
body,td,th {
	font-size: 12px;
	color: #3f3f3f;
	font-family: "宋体";
}
.Left_zi {
	line-height: 23px;
	padding-left: 12px;
	padding-top: 6px;
}
.Right_zi {
	line-height: 23px;
	padding-left: 12px;
	padding-right: 12px;
}
.button_01 {
	color: #FFF;
	background: url(${basePath}resources/personImage/btn_01.jpg) no-repeat;
	height: 21px;
	width: 70px;
	border: none;
	margin-right: 7px;
	cursor: pointer;
}

.title_zi{	font-size: 13px;
	font-weight: bold;
	color: #FFF;
	text-align:center;
	width:150px;
	height:35px;
	line-height:32px;
}
.list_lan {
	font-size: 13px;
	font-weight: bold;
	color: #005aa7;
}
.zt_study {
	color: #e63701;
}
.zt_finish {
	color: #09F;
}

.zt_check {
	color: #046cd3;
	cursor: pointer;
}

.zt_check a:link, .zt_check a:visited{
	color: #046cd3;
	text-decoration: none;
}
.score_zi {
	text-indent: 18px;
	background-position: 6px center;
	border-bottom: 1px dashed #9e9e9e;
	background-image: url(${basePath}resources/personImage/dot.png);
	background-repeat: no-repeat;
}
.score_zi2 {
	border-bottom: 1px dashed #9e9e9e;
}
.score_zi03 {
	text-indent: 14px;
	border-bottom: 1px dashed #9e9e9e;
}
.zt_check a:hover, .zt_check a:active{
	color: #046cd3;
	text-decoration: none;	
}
a:link {
	text-decoration: none;
	color: #3f3f3f;
}
a:visited {
	text-decoration: none;
	color: #3f3f3f;
}
a:hover {
	text-decoration: none;
	color: #007af4;
}
a:active {
	text-decoration: none;
	color: #007af4;
}

.refodd{
	display: inline-block;
    min-width: 200px;
    width: 95%;
    text-indent: 10px;
    background-position: 6px center;
    border-bottom: 1px dashed #9e9e9e;
    background-image: url(${basePath}resources/personImage/dot.png);
    background-repeat: no-repeat;
    /* min-height: 36px; */
    /* line-height: 36px; */
    padding-left: 15px;
    padding-top: 10px;
    padding-bottom: 10px;
    /*padding-right: 10px;  */
}
.refeven{
	display: inline-block;
    min-width: 200px;
    width: 95%;
    text-indent: 10px;
    background-position: 6px center;
    border-bottom: 1px dashed #9e9e9e;
    background-image: url(${basePath}resources/personImage/dot.png);
    background-repeat: no-repeat;
    /* min-height: 36px; */
    /* line-height: 36px; */
    padding-left: 15px;
    padding-top: 10px;
    padding-bottom: 10px;
    /*padding-right: 10px;*/
}

.jddiv{
	background-color: white;
    position: relative;
    
}
.jdmkdiv{
	float: left;
    background-color: Skyblue;
    height: 100%;
    position: absolute;
    min-width: 75px;
}
.jdnrdivodd{
    min-height: 52px;
    margin-left: 80px;
    background-color:#e8e8e8;
        margin-bottom: 4px;
}
.jdnrdiveven{
    min-height: 52px;
    margin-left: 80px;
    background-color:#f7f7f7;
        margin-bottom: 4px;
}
</style>
</head>
<body bgcolor="#f0f0f0" id="puser">
<center>
<table width="100%" border="0" cellspacing="0" cellpadding="6">
  <tr>
    <td colspan="3" valign="top"><table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
      <!--培训内容及标准-->
        <td width="50%"><table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0" bgcolor="#FFFFFF">
          <tr>
            <td width="17" valign="top" style="background:url(${basePath}resources/personImage/bg6_01.png) no-repeat">&nbsp;</td>
            <td valign="top" style="background:url(${basePath}resources/personImage/bg6_03.png) repeat-x" align="left">
            <div class="title_zi">培训内容及标准</div>
            <!--培训内容及标准 列表--> 
            <div style=" height:304px; overflow:auto;">             
              <table width="100%" border="0" cellspacing="0" cellpadding="0">
              	<c:forEach items="${contentList }" var="item">
              	<c:choose>
              		<c:when test="${fn:contains(item,'《') }">
              			<tr>
		                  	<td height="36" align="left" class="score_zi03" style="font-weight:bold;">${item }</td>
		                </tr>
              		</c:when>
              		<c:otherwise>
              			<tr>
		                  	<td height="36" align="left" class="score_zi03" style="text-indent: 50px;">${item }</td>
		                </tr>
              		</c:otherwise>
              	</c:choose>
              	  
              	</c:forEach>
              	<%--
                <tr>
                  <td height="36" align="left" class="score_zi03"><strong>1</strong>、培训是一种有组织的知识传递、技能传递、标准传递。</td>
                  </tr>
                <tr>
                  <td height="36" align="left" class="score_zi03"><strong>2</strong>、信息传递、信念传递、管理训诫行为。</td>
                  </tr>
                <tr>
                  <td height="36" align="left" class="score_zi03"><strong>3</strong>、培训是一种有组织的知识传递、技能传递、标准传递。</td>
                  </tr>
                <tr>
                  <td height="36" align="left" class="score_zi03"><strong>4</strong>、信息传递、信念传递、管理训诫行为。</td>
                  </tr>
                <tr>
                  <td height="36" align="left" class="score_zi03"><strong>5</strong>、信息传递、信念传递、管理训诫行为。</td>
                </tr>
                 --%>
              </table>
            </div>
            </td>
            <td width="17" valign="top" style="background:url(${basePath}resources/personImage/bg6_02.png) no-repeat">&nbsp;</td>
          </tr>
          <tr>
            <td width="17" height="18"><img src="${basePath}resources/personImage/bg_btm02.png" width="17" height="18" /></td>
            <td valign="top" style="background:url(${basePath}resources/personImage/bg_btm01.png) repeat-x top"></td>
            <td><img src="${basePath}resources/personImage/bg_btm03.png" width="17" height="18" /></td>
          </tr>
        </table></td>
        <td width="12">&nbsp;</td>
        <!--参考教材-->
        <td width="49%"><table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0" bgcolor="#FFFFFF">
          <tr>
            <td width="17" valign="top" style="background:url(${basePath}resources/personImage/bg6_01.png) no-repeat">&nbsp;</td>
            <td valign="top" style="background:url(${basePath}resources/personImage/bg6_03.png) repeat-x" align="left">
            <div  class="title_zi">参考教材</div>
            <!--参考教材 列表-->      
            <div style=" height:304px;min-width:400px; overflow:auto;"> 
            <c:forEach items="${referenceBookList }" var="item" varStatus="state">
            	<c:choose>
            		<c:when test="${state.index%2 eq 0 }">
            			<span class="refodd">${item }</span>
            		</c:when>
            		<c:otherwise>
            			<span class="refeven">${item }</span>
            		</c:otherwise>
            	</c:choose>
            </c:forEach>   
            <%--
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                  <td width="45%" height="36" align="left" class="score_zi">《集控运行》</td>
                  <td width="10%" rowspan="4" align="center"> </td>
                  <td width="45%" align="left" class="score_zi">《集控运行》2</td>
                </tr>
                <tr>
                  <td height="36" align="left" class="score_zi">《电力勘测设计》</td>
                  <td align="left" class="score_zi">《电力勘测设计》2</td>
                </tr>
                <tr>
                  <td height="36" align="left" class="score_zi">《锅炉运行值班员》</td>
                  <td align="left" class="score_zi">《锅炉运行值班员》2</td>
                </tr>
                <tr>
                  <td height="36" align="left" class="score_zi">《汽轮机运行》</td>
                  <td align="left" class="score_zi">《汽轮机运行》2</td>
                </tr>
              </table>
               --%>
              </div>
              </td>
            <td width="17" valign="top" style="background:url(${basePath}resources/personImage/bg6_02.png) no-repeat">&nbsp;</td>
          </tr>
          <tr>
            <td width="17" height="18"><img src="${basePath}resources/personImage/bg_btm02.png" width="17" height="18" /></td>
            <td valign="top" style="background:url(${basePath}resources/personImage/bg_btm01.png) repeat-x top"></td>
            <td><img src="${basePath}resources/personImage/bg_btm03.png" width="17" height="18" /></td>
          </tr>
        </table></td>
      </tr>
    </table></td>
  </tr>
  <tr>
    <td width="70%" height="211" valign="top">
    <!--我的学习进度-->
    <table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0" bgcolor="#FFFFFF">
      <tr>
        <td width="17" rowspan="2" valign="top" style="background:url(${basePath}resources/personImage/bg5_01.png) no-repeat">&nbsp;</td>
        <td width="100%" height="29" valign="top" align="left" style="background:url(${basePath}resources/personImage/title_03.png) no-repeat"><div  class="title_zi">我的学习进度</div></td>
        <td width="17" rowspan="2" valign="top" style="background:url(${basePath}resources/personImage/bg5_02.png) no-repeat">&nbsp;</td>
        </tr>
      <tr>
        <td valign="top">
        <!--我的学习进度 列表-->
        <%--
        <div class="jddiv">
        	<div class="jdmkdiv"><pre>达标模块</pre></div>
        	<c:forEach items="${standardnameList }" var="item" varStatus="state">
        		<c:choose>
        			<c:when test="${state.index%2 eq 0 }">
        				<div class="jdnrdivodd">${item }</div>
        			</c:when>
        			<c:otherwise>
        				<div class="jdnrdiveven">${item }</div>
        			</c:otherwise>
        		</c:choose>
        		
        	</c:forEach>
        </div>
         --%>
         
        <table width="100%" border="0" cellpadding="0" cellspacing="0">
          <tr>
            <td height="6" colspan="5"></td>
          </tr>
          
          <c:set var="dbDataLength" value="${fn:length(standardnameList) }" ></c:set>
          <s:set var="radomcolor" value="{'e5eb69','8de7e5','efba8e','b1de69','2fa6de','eda613','c0baf8','ae91b9','beba85'}"></s:set>
          <c:choose>
          	<c:when test="${dbDataLength eq null || dbDataLength eq 0 }">
          		<tr bgcolor="#e8e8e8">
		            <td width="75" rowspan="2" align="center" bgcolor="#e8e8e8" class="list_lan"><p>达 标</p>
		              <p>模 块</p></td>
		            <td width="2" bgcolor="#FFFFFF"></td>
		            <td width="88" height="52" bgcolor="#e8e8e8" class="Right_zi">无记录</td>
		            <td width="130" align="center" bgcolor="#e8e8e8">&nbsp;</td>
		            <td width="254" align="left" bgcolor="#e8e8e8">&nbsp;</td>
		            <td width="61" align="center" bgcolor="#e8e8e8">&nbsp;</td>
		            <td width="100" align="center" bgcolor="#e8e8e8">&nbsp;</td>
		        </tr>
          	</c:when>
          	<c:when test="${dbDataLength eq 1 }">
          		<tr bgcolor="#e8e8e8">
		            <td width="75" rowspan="2" align="center" bgcolor="#e8e8e8" class="list_lan"><p>达 标</p>
		              <p>模 块</p></td>
		            <td width="2" bgcolor="#FFFFFF"></td>
		            <td width="170" height="52" bgcolor="#e8e8e8" class="Right_zi">${standardnameList[0].standardname}</td>
		            <td width="130" align="center" bgcolor="#e8e8e8">
		            	<c:set var="itemgwExamQk" scope="page" value="${gwpxExamScore[item.themeBank.themeBankId]}"></c:set>
		            	<c:if test="${itemgwExamQk ne null}">[达标考试成绩${itemgwExamQk}]</c:if>
		            	<c:if test="${itemgwExamQk eq null}">[无达标考试成绩]</c:if>
		            </td>
		            <td width="154" align="left" bgcolor="#e8e8e8">
		            	<div style="width:150px; height:14px; padding:2px 2px 0px 2px; border:1px solid #bbbbbb; background-color:#FFF; text-align:left;">
			                <div style="width:${varProgress}%; height:12px; background-color:#${radomcolor[randomVal]};"></div>
			            </div>
					</td>
		            <td width="61" align="center" bgcolor="#e8e8e8" style="padding-left:5px;">完成${finThemeNum}题/共${themeNum}题(${varProgress}%)</td>
		            <td align="left" style="padding-left:5px;">
		            	<c:choose>
		            		<c:when test="${indexTkMap[standardnameList[0].themeBank.themeBankId] ne null}">
		            		<a href="javascript:void(0)" onclick="openTk('${standardnameList[0].themeBank.themeBankId }','${standardnameList[0].themeBank.themeBankName }');">${standardnameList[0].themeBank.themeBankName }</a>
		            		</c:when>
		            		<c:otherwise>
		            			<font color="red">${item.themeBank.themeBankName }</font>
		            		</c:otherwise>
		            	</c:choose>
		            </td>
		            <td width="100" align="left" bgcolor="#e8e8e8" style="padding-left:5px;white-space: nowrap;">
		            	<c:set var="itemmoniScore" scope="page" value="${moniExamScoreMap[item.themeBank.themeBankId]}"></c:set>
		            	<c:if test="${itemmoniScore ne null}">最后模拟得分率：${itemmoniScore}%</c:if>&nbsp;
		            </td>
		        </tr>
          	</c:when>
          	<c:otherwise>
          	<s:set var="varheight" value="52" ></s:set>
          	<c:if test="${dbDataLength>3}">
          		<s:set var="varheight" value="30" ></s:set>
          	</c:if>
          		<c:forEach items="${standardnameList }" var="item" varStatus="state">
          			<c:choose>
				       <c:when test="${indexTkMap[item.themeBank.themeBankId] ne null}">
          		       <c:set var="varProgress" scope="page" value="${(indexTkMap[item.themeBank.themeBankId]).succThemeNum}"></c:set>
          		       <c:set var="themeNum" scope="page" value="${(indexTkMap[item.themeBank.themeBankId]).themeNum}"></c:set>
          		       <c:set var="finThemeNum" scope="page" value="${(indexTkMap[item.themeBank.themeBankId]).finThemeNum}"></c:set>
          			   </c:when>
				       <c:otherwise>
				        <c:set var="varProgress" scope="page" value="0.0"></c:set>
				        <c:set var="themeNum" scope="page" value="-"></c:set>
          		       <c:set var="finThemeNum" scope="page" value="-"></c:set>
				       </c:otherwise>
				    </c:choose>
          			<c:if test="${state.index eq 0}">
          				<tr bgcolor="#e8e8e8">
				            <td width="75" rowspan="${dbDataLength*2-1 }" align="center" bgcolor="#e8e8e8" class="list_lan"><p>达 标</p>
				              <p>模 块</p></td>
				            <td width="2" bgcolor="#FFFFFF"></td>
				            <td style="min-width:170px;width:20%;" height="${varheight}" bgcolor="#e8e8e8" class="Right_zi">${state.index+1}、${item.standardname}</td>
				            <td width="130" align="center" bgcolor="#e8e8e8">
				            	<c:set var="itemgwExamQk" scope="page" value="${gwpxExamScore[item.themeBank.themeBankId]}"></c:set>
				            	<c:if test="${itemgwExamQk ne null}">[达标考试成绩${itemgwExamQk}]</c:if>
				            	<c:if test="${itemgwExamQk eq null}">[无达标考试成绩]</c:if>
			            	</td>
		            		<td width="154" align="left" bgcolor="#e8e8e8" title="完成比例：${varProgress}% [共${themeNum}题,完成${finThemeNum}题]">
				            	<div style="width:150px; height:14px; padding:2px 2px 0px 2px; border:1px solid #bbbbbb; background-color:#FFF; text-align:left;">
						                <div style="width:${varProgress}%; height:12px; background-color:#20BAFB;"></div>
						        </div>
							</td>
				            <td width="61" align="left" bgcolor="#e8e8e8" style="padding-left:5px;">完成${finThemeNum}题/共${themeNum}题(${varProgress}%)</td>
				             <td align="left" style="padding-left:5px;">
				            	<c:choose>
				            		<c:when test="${indexTkMap[item.themeBank.themeBankId] ne null && varProgress<=100}">
				            		<a href="javascript:void(0)" onclick="openTk('${item.themeBank.themeBankId }','${item.themeBank.themeBankName }');">${item.themeBank.themeBankName }</a><img id="jz_${item.themeBank.themeBankId }" title="试题加载状态：已加载！" src="${basePath}resources/icons/fam/accept.png" style="display:none"/>
				            		<script type="text/javascript">
				            		examArray[examArray.length] = ["${item.themeBank.themeBankId }","${basePath}onlineExam/examIndexUserMoniForOnlineListAction!examIndexUserMoni.action?relationType=moni&themeBankId=${item.themeBank.themeBankId }",null];
				            		</script>
				            		</c:when>
				            		<c:otherwise>
				            			<font color="red">${item.themeBank.themeBankName }</font>
				            		</c:otherwise>
				            	</c:choose>
				            </td>
				            <td width="100" align="left" bgcolor="#e8e8e8" style="padding-left:5px;white-space: nowrap;">
				            	<c:set var="itemmoniScore" scope="page" value="${moniExamScoreMap[item.themeBank.themeBankId]}"></c:set>
				            	<c:if test="${itemmoniScore ne null}">最后模拟得分率：${itemmoniScore}%</c:if>&nbsp;
				            </td>
				        </tr>
          			</c:if>
					<c:if test="${state.index ne 0 }">
						  <tr>
				            <td height="2" colspan="4"></td>
				          </tr>
				          <tr bgcolor="${(state.index%2 eq 0)?'#e8e8e8':'#f7f7f7'}">
				            <td bgcolor="#FFFFFF"></td>
				            <td height="${varheight}" class="Right_zi">${state.index+1}、 ${item.standardname}</td>
				            <td align="center">
				            	<c:set var="itemgwExamQk" scope="page" value="${gwpxExamScore[item.themeBank.themeBankId]}"></c:set>
				            	<c:if test="${itemgwExamQk ne null}">[达标考试成绩${itemgwExamQk}]</c:if>
				            	<c:if test="${itemgwExamQk eq null}">[无达标考试成绩]</c:if>
			            	</td>
				            <td align="left" title="完成比例：${varProgress}% [共${themeNum}题,完成${finThemeNum}题]">
								<div style="width:150px; height:14px; padding:2px 2px 0px 2px; border:1px solid #bbbbbb; background-color:#FFF; text-align:left;">
						                <div style="width:${varProgress}%; height:12px; background-color:#20BAFB"></div>
						        </div>
							</td>
				            <td align="left" style="padding-left:5px;">完成${finThemeNum}题/共${themeNum}题(${varProgress}%)</td>
				            <td align="left" style="padding-left:5px;">
				            	<c:choose>
				            		<c:when test="${indexTkMap[item.themeBank.themeBankId] ne null && varProgress<=100}">
				            		<a href="javascript:void(0)" onclick="openTk('${item.themeBank.themeBankId }','${item.themeBank.themeBankName }');">${item.themeBank.themeBankName }</a><img id="jz_${item.themeBank.themeBankId }" title="试题加载状态：已加载！" src="${basePath}resources/icons/fam/accept.png" style="display:none"/>
				            		<script type="text/javascript">
				            		examArray[examArray.length] = ["${item.themeBank.themeBankId }","${basePath}onlineExam/examIndexUserMoniForOnlineListAction!examIndexUserMoni.action?relationType=moni&themeBankId=${item.themeBank.themeBankId }",null];
				            		</script>
				            		</c:when>
				            		<c:otherwise>
				            			<font color="red">${item.themeBank.themeBankName}</font>
				            		</c:otherwise>
				            	</c:choose>
				            </td>
				            <td align="left" style="padding-left:5px;white-space: nowrap;">
				            	<c:set var="itemmoniScore" scope="page" value="${moniExamScoreMap[item.themeBank.themeBankId]}"></c:set>
				            	<c:if test="${itemmoniScore ne null}">最后模拟得分率：${itemmoniScore}%</c:if>&nbsp;
				            </td>
				          </tr>
					</c:if>          			
          		</c:forEach>
          	</c:otherwise>
          </c:choose>
          
          <%-- 
          <tr bgcolor="#e8e8e8">
            <td width="75" rowspan="5" align="center" bgcolor="#e8e8e8" class="list_lan"><p>达 标${dbDataLength }</p>
              <p>模 块</p></td>
            <td width="2" bgcolor="#FFFFFF"></td>
            <td width="88" height="52" bgcolor="#e8e8e8" class="Right_zi">运行巡检</td>
            <td width="254" align="left" bgcolor="#e8e8e8"><img src="${basePath}resources/personImage/bar_01.png" alt="" /></td>
            <td width="61" align="center" bgcolor="#e8e8e8">80%</td>
          </tr>
          <tr>
            <td height="2" colspan="4"></td>
          </tr>
          <tr bgcolor="#f7f7f7">
            <td bgcolor="#FFFFFF"></td>
            <td height="52" class="Right_zi">运行副值</td>
            <td align="left"><img src="${basePath}resources/personImage/bar_02.png" alt="" /></td>
            <td align="center">65%</td>
          </tr>
          <tr>
            <td height="2" colspan="4"></td>
          </tr>
          <tr bgcolor="#e8e8e8">
            <td bgcolor="#FFFFFF"></td>
            <td height="52" class="Right_zi">运行主值</td>
            <td align="left"><img src="${basePath}resources/personImage/bar_03.png" alt="" /></td>
            <td align="center">20%</td>
          </tr>
          --%>
          
        </table>
        </td>
      </tr>
      <tr>
        <td height="18" valign="top"><img src="${basePath}resources/personImage/bg_btm02.png" /></td>
        <td valign="top" style="background:url(${basePath}resources/personImage/bg_btm01.png) repeat-x"> </td>
        <td valign="top" ><img src="${basePath}resources/personImage/bg_btm03.png" /></td>
      </tr>
      
      
    </table></td>
    <!--我的考试-->
    <td width="30%" height="211" valign="top" align="left" style="display:none;">
    <table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0" bgcolor="#FFFFFF">
      <tr>
        <td width="17" valign="top" style="background:url(${basePath}resources/personImage/bg6_01.png) no-repeat">&nbsp;</td>
        <td valign="top" style="background:url(${basePath}resources/personImage/bg6_03.png) repeat-x">
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td height="35" colspan="2" align="left" valign="top"><div  class="title_zi">我的考试</div></td>
          </tr>
          <!--我的考试 列表-->
          <c:if test="${empty examusertestpaperlist}">
		    	<tr>
			        <td width="131" height="36" align="left" class="score_zi">没有参加考试</td>
			        <td width="80" align="center" class="score_zi2">&nbsp;</td>
			    </tr>	
		    	</c:if> 
		    	<c:forEach var="examusertestpaper" items="${examusertestpaperlist}">
		    	<tr>
			        <td width="131" height="36" align="left" class="score_zi">${examusertestpaper.exam.examTestpaperName}</td><%/***考试名称**/ %>
			        <td align="center" class="score_zi2">${examusertestpaper.score}分</td><%/***得分(<span class="zt_study">不及格</span>)**/ %>
		      	</tr>			
		    	</c:forEach>
        </table>
        </td>
        <td width="17" valign="top" style="background:url(${basePath}resources/personImage/bg6_02.png) no-repeat">&nbsp;</td>
      </tr>
      <tr>
        <td width="17" height="18"><img src="${basePath}resources/personImage/bg_btm02.png" width="17" height="18" /></td>
        <td valign="top" style="background:url(${basePath}resources/personImage/bg_btm01.png) repeat-x top"> </td>
        <td><img src="${basePath}resources/personImage/bg_btm03.png" width="17" height="18" /></td>
      </tr>
    </table></td>
  </tr>
  <tr>
    <td colspan="3" valign="top"><table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
      <!--进入题库-->
        <td width="187"><table width="187" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td width="17" height="202" rowspan="2" valign="top"><img src="${basePath}resources/personImage/bg7_01.jpg"/></td>
            <td valign="top" style="height: 184px;background-image:url(${basePath}resources/personImage/wdks.jpg);" onmouseover="setShow();" onmouseout="setHidden();">
            	<div style="height: 170px;width: 153px;background-color: white;margin-top: 5px;visibility:hidden;overflow:auto;" id="ktdiv">
            	
            	<c:if test="${empty examusertestpaperlist}">
		    		<span class="refodd" style="width: 115px;min-width:115px;padding-right:0px;padding-left:15px;" >
            				没有参加考试
            		</span>
		    	</c:if> 
		    	<c:forEach var="examusertestpaper" items="${examusertestpaperlist}">
		    		<span class="refodd" style="width: 115px;min-width:115px;padding-right:0px;padding-left:15px;" >
            				${examusertestpaper.exam.examTestpaperName}&nbsp;---&nbsp;${examusertestpaper.score}分
            		</span>
		    	</c:forEach>
            	
            	<%--
            		<c:forEach items="${indexTkList }" var="item">
            			<span class="refodd" style="width: 115px;min-width:115px;padding-right:0px;padding-left:15px;" >
            				<a href="javascript:void(0)" onclick="openTk('${item.themeBankId }','${item.themeBankName }');">${item.themeBankName }</a>
            			</span>
            		</c:forEach>
            		<c:if test="${fn:length(indexTkList) eq 0 }">
            			<span class="refodd" style="width: 115px;min-width:115px;padding-right:0px;padding-left:15px;" >
            				未查询到对应题库
            			</span>
            		</c:if>
            	 --%>	
            		
            		
            	</div>
            </td>
            <td width="17" rowspan="2" valign="top"><img src="${basePath}resources/personImage/bg7_02.jpg" /></td>
          </tr>
          <tr>
          <td height="18" colspan="11" style="background:url(${basePath}resources/personImage/bg_btm01.png) repeat-x"></td>
          </tr>
        </table></td>
          <!--进入题库 结束-->
        <td width="12">&nbsp;</td>
      <!--学习教材-->
        <td ><table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td width="17" height="202" valign="top"><img src="${basePath}resources/personImage/bg7_01.jpg"/></td>
            <td align="center"><table width="100%" border="0" cellspacing="0" cellpadding="0" bgcolor="#FFFFFF">
            <c:set var="courseware_len" value="${fn:length(coursewarelist)==0?1:fn:length(coursewarelist)}"></c:set>
	    	<c:if test="${courseware_len>5}"><c:set var="courseware_len" value="5"></c:set></c:if>
              <tr>
                <td height="32" colspan="${courseware_len+(courseware_len-1)}" align="left" style="background:url(${basePath}resources/personImage/title_03.png) repeat-x"><div class="title_zi">学习教材</div></td>
              </tr>
              <!--学习教材 列表-->
              
              <tr bgcolor="#f7f7f7">
			      	<c:forEach var="courseware" items="${coursewarelist}" varStatus="stcourse">
			      		<c:if test="${stcourse.count<=courseware_len}">
			      		<td height="129" align="center"><a href="#" onclick="openCourse('${courseware.id}');"><img width="124" height="125" src="${basePath}mainpage/writepicForMainPageListAction!writepic.action?coursewareId=${courseware.id}" /></a></td>
			      		<c:if test="${stcourse.count<courseware_len}">
			      			<td width="5" rowspan="2" bgcolor="#f7f7f7" style="background:url(${basePath}resources/images/dashed-s.jpg) repeat-y"></td>	
			      		</c:if>
			      		</c:if>
			      	</c:forEach>
			      	</tr>
			      	<tr bgcolor="#f7f7f7">
			      	<c:forEach var="courseware" items="${coursewarelist}" varStatus="st">
			      		<c:if test="${st.count<=courseware_len}">
			      		<td height="20" align="center" valign="top" onclick="openCourse('${courseware.id}');">${courseware.title}</td>	
			      		</c:if>
			      	</c:forEach>
			  </tr>
               <%--
              <tr bgcolor="#f7f7f7">
                <td width="124" height="129" align="center" bgcolor="#f7f7f7"><a href="index_ygjm.html"><img src="${basePath}resources/personImage/book_01.jpg" /></a></td>
                <td width="5" rowspan="2" bgcolor="#f7f7f7" style="background:url(${basePath}resources/personImage/dashed-s.jpg) repeat-y"></td>
                <td width="124" align="center" bgcolor="#e8e8e8"><a href="index_ygjm.html"><img src="${basePath}resources/personImage/book_01.jpg" /></a></td>
                <td width="5" rowspan="2" bgcolor="#f7f7f7" style="background:url(${basePath}resources/personImage/dashed-s.jpg) repeat-y"></td>
                <td width="124" align="center" bgcolor="#f7f7f7"><a href="index_ygjm.html"><img src="${basePath}resources/personImage/book_01.jpg" /></a></td>
                <td width="5" rowspan="2" bgcolor="#f7f7f7" style="background:url(${basePath}resources/personImage/dashed-s.jpg) repeat-y"></td>
                <td width="124" align="center" bgcolor="#e8e8e8"><a href="index_ygjm.html"><img src="${basePath}resources/personImage/book_01.jpg" /></a></td>
                </tr>
              <tr bgcolor="#f7f7f7">
                <td height="20" align="center" valign="top">集控运行${coursewarelist}</td>
                <td align="center" valign="top" bgcolor="#e8e8e8">集控运行</td>
                <td align="center" valign="top">集控运行</td>
                <td align="center" valign="top" bgcolor="#e8e8e8">集控运行</td>
              </tr>
                --%>
              <tr>
                <td height="18" colspan="${courseware_len+(courseware_len-1)}" style="background:url(${basePath}resources/personImage/bg_btm01.png) repeat-x"></td>
              </tr>
            </table></td>
            <td width="17" valign="top"><img src="${basePath}resources/personImage/bg7_02.jpg" /></td>
          </tr>
        </table></td>
      </tr>
    </table>
    </td>
  </tr>
</table>
</center>
<script type="text/javascript">
	function initExam(){
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
	    					document.getElementById("jz_"+result.relationId).style.display = "";
	    					break;
	    				}
	    			}
	    		}
	    		
			}
		});
	}
	function refUrl(){
		//var win = window.parent.grablEapLogin.desktop.getWindow("menu_"+tabid);
   	    if(window.parent.grablEapLogin.moniKsTableId == null){
   	    	var refMask = new Ext.LoadMask(document.getElementById("puser"), {  
   			    msg     : '数据正在处理,请稍候',  
   			    removeMask  : true// 完成后移除  
   			});
   	    	refMask.show();
   	    	clearInterval(refInterval);
			refInterval = null;
			document.execCommand('stop');
			window.location.reload();
   	    }
	}
	
	initExam();
</script>
</body>
</html>
