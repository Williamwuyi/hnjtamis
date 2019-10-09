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
	request.setCharacterEncoding("GBK");
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
<link href="${basePath}modules/hnjtamis/main/css/main.css" rel="stylesheet" type="text/css" />
<c:set var="venter" value="\n" scope="request"/>
<script type="text/javascript">
var examArray = [];
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
</head>
<body id="puser">
<table width="100%" border="0" cellspacing="23" cellpadding="0">
  <tr>
    <td width="80%" valign="top">
<!--达标模块--><table width="100%" border="0" cellspacing="0" cellpadding="0" id="standartTable">
      <tr>
        <td colspan="6"><table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td width="3%"><img src="${basePath}modules/hnjtamis/main/images/icon_01.png" /></td>
            <td width="21%" align="left" class="tbl_lan_sy">达标标准</td>
            <td width="76%" align="right" class="tbl_lan_right">岗位：${employeeLearningForm.quartername}&nbsp;&nbsp;<font color="<c:if test="${employeeLearningForm.lengrningPass eq '未达标'}">red</c:if><c:if test="${employeeLearningForm.lengrningPass ne '未达标'}">#2390f4</c:if>">${employeeLearningForm.lengrningPass}</font>&nbsp;&nbsp;有效期至：${employeeLearningForm.passEndDay}&nbsp;</td>
          </tr>
        </table></td>
        </tr>
<!--达标模块 列表--><tr>
        <td width="10%" height="36" align="center" class="list_lan">类别</td>
        <td width="11%" align="center" class="list_lan">模块</td>
        <td width="17%" align="center" class="list_lan">子模块</td>
        <td width="46%" align="center" class="list_lan">详细内容</td>
        <td width="8%" align="center" class="list_lan">参考学分</td>
        <td width="8%" align="center" class="list_lan">有效期</td>
      </tr>
      <c:if test="${standardTermslist== null || fn:length(standardTermslist) == 0}">
     	 <tr>
		     <td align="center" class="list_blue" colspan="6">没有找到相应的达标标准！</td>
		 </tr>
      </c:if>
      <c:if test="${standardTermslist ne null && fn:length(standardTermslist) > 0}">
      <c:forEach items="${standardTermslist}" var="item" varStatus="state">
	      <c:choose>
		      <c:when test="${state.index%2 eq 0 }">
		      <tr>
		        <td align="center" class="list_blue">${item.parentTypeName}</td>
		        <td align="center" class="list_blue">${item.typename}</td>
		        <td align="center" class="list_blue">${item.standardname}</td>
		        <td class="list_blue" style="padding-top:5px;padding-bottom:5px;">${fn:replace(item.contents,venter,'<br>')}</td>
		        <td align="center" class="list_blue">${item.refScore}</td>
		        <td align="center" class="list_blue">${item.efficient}</td>
		      </tr>
		      </c:when>
		      <c:otherwise>
		       <tr>
		        <td align="center" class="list_green">${item.parentTypeName}</td>
		        <td align="center" class="list_green">${item.typename}</td>
		        <td align="center" class="list_green">${item.standardname}</td>
		        <td class="list_green"  style="padding-top:5px;padding-bottom:5px;">${fn:replace(item.contents,venter,'<br>')}</td>
		        <td align="center" class="list_green">${item.refScore}</td>
		        <td align="center" class="list_green">${item.efficient}</td>
		      </tr>
		      </c:otherwise>
	      </c:choose>
       </c:forEach>
       	<tr>
<!--学习进度 翻页--><td colspan="6" align="right"><table width="500" border="0" cellspacing="6" cellpadding="0">
          <tr>
            <td style="text-align: right;">当前使用中：共${standardTotal}条&nbsp;每页5条&nbsp;共${standardPagTotal}页</td>
            <td class="flip"><img src="${basePath}modules/hnjtamis/main/images/flip_01.png" onclick="indStandardTableBar.topPage();" /></td>
            <td class="flip"><img src="${basePath}modules/hnjtamis/main/images/flip_02.png" onclick="indStandardTableBar.prevPage();" /></td>
            <c:forEach var="iii" begin="1" end="${standardPagTotal}"  step="1">
            	<c:if test="${iii == 1}"><td class="flip_C" id="ssp${iii}" onclick="indStandardTableBar.changePage(${iii})">${iii}</td></c:if>
			    <c:if test="${iii > 1}"><td class="flip" id="ssp${iii}" <c:if test="${iii>5}">style="display:none"</c:if> onclick="indStandardTableBar.changePage(${iii})">${iii}</td></c:if>
			</c:forEach>
            <td class="flip"><img src="${basePath}modules/hnjtamis/main/images/flip_03.png" onclick="indStandardTableBar.nextPage();" /></td>
            <td class="flip"><img src="${basePath}modules/hnjtamis/main/images/flip_04.png" onclick="indStandardTableBar.endPage();" /></td>
          </tr>
        </table></td>
        </tr></c:if>
<!--达标模块 结束--></table></td>
  </tr>
  <tr>
    <td valign="top"><table width="100%" border="0" cellspacing="0" cellpadding="0" id="tkTable">
      <tr>
        <td colspan="6">
<!--我的学习进度--><table width="100%" border="0" cellpadding="0" cellspacing="0">
          <tr>
<td width="3%"><img src="${basePath}modules/hnjtamis/main/images/icon_02.png" /></td>
            <td width="21%" align="left" class="tbl_lan_sy">&nbsp;我的学习进度</td>            
            <td width="76%" align="right" class="tbl_lan_right">共完成${employeeLearningForm.finthemenum}题/共有${employeeLearningForm.themenum }题 &nbsp; 达标率${employeeLearningForm.finthemebfl}%&nbsp;</td>
          </tr>
        </table></td>
        </tr>
        <c:if test="${indexTkList== null || fn:length(indexTkList) == 0}">
     	 <tr bgcolor="#f7f7f7">
		     <td align="center" class="list_study" colspan="6" height="38" style="font-size: 14px;">没有找到相应的学习内容！</td>
		 </tr>
         </c:if>
         <c:if test="${indexTkList ne null && fn:length(indexTkList) > 0}">
        <c:forEach items="${indexTkList}" var="item" varStatus="state">
        	<script type="text/javascript">
				examArray[examArray.length] = ["${item.themeBankId }","${basePath}onlineExam/examIndexUserMoniForOnlineListAction!examIndexUserMoni.action?relationType=moni&themeBankId=${item.themeBankId }",null];
			</script>
           <c:set var="varProgress" scope="page" value="${(indexTkMap[item.themeBankId]).succThemeNum}"></c:set>
           <c:if test="${varProgress eq null || varProgress==''}">
           	  <c:set var="varProgress" scope="page" value="0.00"></c:set>
           </c:if>
           <c:set var="themeNum" scope="page" value="${(indexTkMap[item.themeBankId]).themeNum}"></c:set>
           <c:if test="${themeNum eq null || themeNum==''}">
           	  <c:set var="themeNum" scope="page" value="0"></c:set>
           </c:if>
           <c:set var="finThemeNum" scope="page" value="${(indexTkMap[item.themeBankId]).finThemeNum}"></c:set>
           <c:if test="${finThemeNum eq null || finThemeNum==''}">
           	  <c:set var="finThemeNum" scope="page" value="0"></c:set>
           </c:if>
           <c:set var="itemmoniScore" scope="page" value="${moniExamScoreMap[item.themeBankId]}"></c:set>
           <c:if test="${itemmoniScore eq null || itemmoniScore==''}">
           	  <c:set var="itemmoniScore" scope="page" value="0.0"></c:set>
           </c:if>
           <c:set var="varProgressClass" scope="page" value="blue"></c:set>
           <c:if test="${varProgress*1.0 < 30}">
          		<c:set var="varProgressClass" scope="page" value="red"></c:set>
           </c:if>
           <c:if test="${varProgress*1.0 >90}">
           		<c:set var="varProgressClass" scope="page" value="green"></c:set>
           </c:if>
           <c:set var="itemmoniScoreImage" scope="page" value="icon_05.png"></c:set>
           <c:if test="${itemmoniScore!='-' && itemmoniScore*1.0 >=60}">
              <c:set var="itemmoniScoreImage" scope="page" value="icon_06.png"></c:set>
           </c:if>
	      <c:choose>
		      <c:when test="${state.index%2 eq 0 }">
		      	<!--学习进度 列表--><tr bgcolor="#e8e8e8">
			        <td width="25%" height="38" class="list_study">${state.index+1}、<a href="javascript:void(0)" onclick="openTk('${item.themeBankId }','${item.themeBankName }');">${item.themeBankName}</a></td>
			        <td width="20%" >
			  <!--进度条--><div class="white"><div class="${varProgressClass}" style="width:${varProgress}%;"></div></div></td>
			        <td width="23%" class="list_study2">完成${finThemeNum}题/共有${themeNum}题（${varProgress}%）</td>
			        <td width="22%" class="list_study2">最后模拟得分率：${itemmoniScore}%</td>
			  <!--达标 图标--><td width="10%"><img src="${basePath}modules/hnjtamis/main/images/${itemmoniScoreImage}" /></td>
			      </tr>
		      </c:when>
		      <c:otherwise>
		       	<tr bgcolor="#f7f7f7">
			        <td height="38" class="list_study">${state.index+1}、<a href="javascript:void(0)" onclick="openTk('${item.themeBankId }','${item.themeBankName }');">${item.themeBankName}</a></td>
			        <td>
			  <!--进度条--><div class="white"><div class="${varProgressClass}" style="width:${varProgress}%;"></div></div></td>
			        <td class="list_study2">完成${finThemeNum}题/共有${themeNum}题（${varProgress}%）</td>
			        <td class="list_study2">最后模拟得分率：${itemmoniScore}%</td>
			  <!--未达标 图标--><td><img src="${basePath}modules/hnjtamis/main/images/${itemmoniScoreImage}" /></td>
			      </tr>
		      </c:otherwise>
		  </c:choose>
		</c:forEach><tr>
<!--学习进度 翻页--><td colspan="5" align="right"><table width="500" border="0" cellspacing="6" cellpadding="0">
          <tr>
            <td style="text-align: right;">当前使用中：共${tkTotal}条&nbsp;每页5条&nbsp;共${tkPagTotal}页</td>
            <td class="flip"><img src="${basePath}modules/hnjtamis/main/images/flip_01.png" onclick="indTkTableBar.topPage();" /></td>
            <td class="flip"><img src="${basePath}modules/hnjtamis/main/images/flip_02.png" onclick="indTkTableBar.prevPage();" /></td>
            <c:forEach var="iii" begin="1" end="${standardPagTotal}"  step="1">
            	<c:if test="${iii == 1}"><td class="flip_C" id="tksp${iii}" onclick="indTkTableBar.changePage(${iii})">${iii}</td></c:if>
			    <c:if test="${iii > 1}"><td class="flip" id="tksp${iii}" <c:if test="${iii>5}">style="display:none"</c:if> onclick="indTkTableBar.changePage(${iii})">${iii}</td></c:if>
			</c:forEach>
            <td class="flip"><img src="${basePath}modules/hnjtamis/main/images/flip_03.png" onclick="indTkTableBar.nextPage();" /></td>
            <td class="flip"><img src="${basePath}modules/hnjtamis/main/images/flip_04.png" onclick="indTkTableBar.endPage();" /></td>
          </tr>
        </table></td>
        </tr></c:if>
<!--我的学习进度 结束--></table></td>
  </tr>
  <tr>
    <td valign="top"><table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td colspan="3">
<!--我的资料--><table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td width="3%" align="left"><img src="${basePath}modules/hnjtamis/main/images/icon_03.png" /></td>
            <td align="left" class="tbl_lan_sy">我的资料</td>
            </tr>
        </table></td>
        </tr>
         <c:if test="${documentlist== null || fn:length(documentlist) == 0}">
         <tr>
         <td width="4%" align="left" class="list_book"><img src="${basePath}modules/hnjtamis/main/images/list_pic01.jpg" /></td>
         <td width="95%" valign="top" class="list_book"><div>暂无分享的资料！</div></td>
         <td width="1%" align="right" class="list_book"><img src="${basePath}modules/hnjtamis/main/images/list_bookbg02.jpg" /></td>
         </tr>
         </c:if>
         <c:if test="${documentlist ne null && fn:length(documentlist) > 0}">
         <c:forEach items="${documentlist}" var="item" varStatus="state">
        	    <c:set var="documentName" value="${item.documentName}"></c:set>
        	    <c:if test="${(item.writerUser ne null && item.writerUser!='' && item.writerUser!='null') ||
        					(item.publishers ne null && item.publishers!='' && item.publishers!='null') ||
        					(item.fxDay ne null && item.fxDay!='' && item.fxDay!='null')}">
        		<c:set var="documentName" value="${documentName}（"></c:set>
        		<c:if test="${item.writerUser ne null && item.writerUser!='' && item.writerUser!='null'}">
        			<c:set var="documentName" value="${documentName}${item.writerUser} 主编，"></c:set>
        		</c:if>
        		<c:if test="${item.publishers ne null && item.publishers!='' && item.publishers!='null'}">
        			<c:set var="documentName" value="${documentName}${item.publishers}，"></c:set>
        		</c:if>
        		<c:if test="${item.fxDay ne null && item.fxDay!='' && item.fxDay!='null'}">
        			<c:set var="documentName" value="${documentName}${item.fxDay}"></c:set>
        		</c:if>
        		<c:set var="documentName" value="${documentName}）"></c:set>
        	</c:if>
<!--我的资料 列表--><tr>
        <td width="4%" align="left" class="list_book"><img src="${basePath}modules/hnjtamis/main/images/list_pic0${(state.index)%10}.jpg" /></td>
        <td width="95%" valign="top" class="list_book"><div><a href='${basePath}baseinfo/affiche/outputFileForAfficheFormAction!outputFile.action?id=${item.afficheId}' target='_new'>${documentName}</a></div></td>
        <td width="1%" align="right" class="list_book"><img src="${basePath}modules/hnjtamis/main/images/list_bookbg02.jpg" /></td>
         </tr>
      </c:forEach></c:if>
    </table></td>
  </tr>
</table>
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
    					//document.getElementById("jz_"+result.relationId).style.display = "";
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
function replaceString(str,reallyDo,replaceWith) { 
	var e=new RegExp(reallyDo,"g"); 
	var words = str.replace(e, replaceWith); 
	return words; 
}
indStandardTableBar = {
	oldSelectTd :null,
	pageIndex : 1,//当前页码
	pageIndexMix : 1,//当前页码最小页码
	pageIndexMax : 5, //当前页码最大页码
	rowInPage : 5,//每页显示多少
	startRow :2,//需要表格的行数
	tableId : "standartTable",
	p_defined_ClassName : "flip",//页码的默认样式
	p_foc_ClassName : "flip_C",//页码的被选中的样式
	p_bar_qz : "ssp",//页码用于的前缀
	maxPage : ${standardPagTotal},
	tableDataUrl : "${basePath}personal/mainpage/getStandardInPageNumForPersonalMainPageListAction!getStandardInPageNum.action?qtc=${qtc}",
	topPage : function (){
		this.changePage(1);
	},
	prevPage :function (){
		if(this.pageIndex>1)this.changePage(this.pageIndex-1);
	},
	nextPage : function (){
		if(this.pageIndex<this.maxPage)this.changePage(this.pageIndex+1);
	},
	endPage : function (){
		this.changePage(this.maxPage);
	},
	pageMask2 : new Ext.LoadMask(document.getElementById("puser"), {  
	    msg     : '数据正在处理,请稍候',  
	    removeMask  : true// 完成后移除  
	}),
	changePage : function (pIndex){
		var me = this;
		var obj = document.getElementById(me.p_bar_qz+pIndex);
		if(me.oldSelectTd == null){
			me.oldSelectTd = document.getElementById(me.p_bar_qz+"1");
		}
		if(pIndex!=me.pageIndex){
			obj.className = me.p_foc_ClassName;
			me.oldSelectTd.className = me.p_defined_ClassName;
			me.oldSelectTd = obj;
			me.pageMask2.show();
			if(me.pageIndexMix == pIndex || me.pageIndexMax == pIndex
					|| me.pageIndexMix > pIndex || me.pageIndexMax < pIndex){
				me.pageIndexMix = pIndex-2;
				me.pageIndexMax = pIndex+2;
				for(var i=1;i<=me.maxPage;i++){
					if(pIndex<3 && i<(me.rowInPage+1)){
						document.getElementById(me.p_bar_qz+i).style.display = "";
					}else if(i>(pIndex+2) || i<(pIndex-2)){
						document.getElementById(me.p_bar_qz+i).style.display = "none";
					}else{
						document.getElementById(me.p_bar_qz+i).style.display = "";
					}
				}
			}
			me.setTableRows(pIndex);
		}
	},
	setTableRows : function(pIndex){
		var me = this;
		Ext.Ajax.request({
			timeout: 6000000,
			url : me.tableDataUrl+"&p="+pIndex,
			success: function(response) {
				var result = Ext.decode(response.responseText);
				if(result){
					var standardTermslist = result.standardTermslist;
					var trs = document.getElementById(me.tableId).rows;
					for(var i=0;i<me.rowInPage;i++){
						var htmlstr = "";
						var tdclss = "list_green";
						if(i%2==0){
							tdclss = "list_blue";
						}
						var td = (trs[i+me.startRow]).getElementsByTagName('td');
						if(i<standardTermslist.length){
							var item = standardTermslist[i];
							td[0].innerHTML = item.parentTypeName;
							td[0].align="center"
							td[0].className=tdclss;
							
							td[1].innerHTML = item.typename;
							td[1].align="center"
							td[1].className=tdclss;
							
							td[2].innerHTML = item.standardname;
							td[2].className=tdclss;
							
							td[3].innerHTML = replaceString(item.contents,"\n","<br>");
							td[3].className=tdclss;
							td[3].style.paddingTop = '5px';
							td[3].style.paddingBottom = '5px';
							
							td[4].innerHTML = item.refScore==undefined?"-":item.refScore;
							td[4].align="center"
							td[4].className=tdclss;
							
							td[5].innerHTML = item.efficient==undefined?"-":item.efficient;
							td[5].align="center"
							td[5].className=tdclss;
						}else{
							td[0].innerHTML = '&nbsp;';
							td[0].align="center"
							td[0].className=tdclss;
							
							td[1].innerHTML = '&nbsp;';
							td[1].align="center"
							td[1].className=tdclss;
							
							td[2].innerHTML = '&nbsp;';
							td[2].className=tdclss;
							
							td[3].innerHTML = '&nbsp;';
							td[3].className=tdclss;
							
							td[4].innerHTML = '&nbsp;';
							td[4].align="center"
							td[4].className=tdclss;
							
							td[5].innerHTML = '&nbsp;';
							td[5].align="center"
							td[5].className=tdclss;
						}
					}
				}
				me.pageIndex = pIndex;
				me.pageMask2.hide();
			},
		    failure:function(form, action){
		    	me.pageMask2.hide();
		    }
		});
	}
}

indTkTableBar = {
		oldSelectTd :null,
		pageIndex : 1,//当前页码
		pageIndexMix : 1,//当前页码最小页码
		pageIndexMax : 5, //当前页码最大页码
		rowInPage : 5,//每页显示多少
		startRow :1,//需要表格的行数
		tableId : "tkTable",
		p_defined_ClassName : "flip",//页码的默认样式
		p_foc_ClassName : "flip_C",//页码的被选中的样式
		p_bar_qz : "tksp",//页码用于的前缀
		maxPage : ${tkPagTotal},
		tableDataUrl : "${basePath}personal/mainpage/getTkInPageNumForPersonalMainPageListAction!getTkInPageNum.action?qtc=${qtc}",
		topPage : function (){
			this.changePage(1);
		},
		prevPage :function (){
			if(this.pageIndex>1)this.changePage(this.pageIndex-1);
		},
		nextPage : function (){
			if(this.pageIndex<this.maxPage)this.changePage(this.pageIndex+1);
		},
		endPage : function (){
			this.changePage(this.maxPage);
		},
		pageMask2 : new Ext.LoadMask(document.getElementById("puser"), {  
		    msg     : '数据正在处理,请稍候',  
		    removeMask  : true// 完成后移除  
		}),
		changePage : function (pIndex){
			var me = this;
			var obj = document.getElementById(me.p_bar_qz+pIndex);
			if(me.oldSelectTd == null){
				me.oldSelectTd = document.getElementById(me.p_bar_qz+"1");
			}
			if(pIndex!=me.pageIndex){
				obj.className = me.p_foc_ClassName;
				me.oldSelectTd.className = me.p_defined_ClassName;
				me.oldSelectTd = obj;
				me.pageMask2.show();
				if(me.pageIndexMix == pIndex || me.pageIndexMax == pIndex
						|| me.pageIndexMix > pIndex || me.pageIndexMax < pIndex){
					me.pageIndexMix = pIndex-2;
					me.pageIndexMax = pIndex+2;
					for(var i=1;i<=me.maxPage;i++){
						if(pIndex<3 && i<(me.rowInPage+1)){
							document.getElementById(me.p_bar_qz+i).style.display = "";
						}else if(i>(pIndex+2) || i<(pIndex-2)){
							document.getElementById(me.p_bar_qz+i).style.display = "none";
						}else{
							document.getElementById(me.p_bar_qz+i).style.display = "";
						}
					}
				}
				me.setTableRows(pIndex);
			}
		},
		setTableRows : function(pIndex){
			var me = this;
			Ext.Ajax.request({
				timeout: 6000000,
				url : me.tableDataUrl+"&p="+pIndex,
				success: function(response) {
					var result = Ext.decode(response.responseText);
					if(result){
						var indexTkList = result.indexTkList;
						var trs = document.getElementById(me.tableId).rows;
						examArray=new Array();
						var rowIndex = (pIndex-1)*me.rowInPage+1;
						for(var i=0;i<me.rowInPage;i++){
							var htmlstr = "";
							var td = (trs[i+me.startRow]).getElementsByTagName('td');
							if(i<indexTkList.length){
								var item = indexTkList[i];
								examArray[examArray.length] = [item.themeBankId,"${basePath}onlineExam/examIndexUserMoniForOnlineListAction!examIndexUserMoni.action?relationType=moni&themeBankId="+item.themeBankId,null];
					            var varProgress = item.succThemeNum;
					            if(varProgress==undefined || varProgress==null || varProgress=='null' || varProgress==''){
					            	varProgress = 0.00;
					            }
					           	var themeNum = item.themeNum;
					           	if(themeNum==undefined || themeNum==null || themeNum=='null' || themeNum==''){
					           		themeNum = 0;
					           	}
					           var finThemeNum = item.finThemeNum;
					           if(finThemeNum==undefined || finThemeNum==null || finThemeNum=='null' || finThemeNum==''){
					        	   finThemeNum = 0;
					           }
					           var itemmoniScore= item.itemmoniScore;
					           if(itemmoniScore==undefined || itemmoniScore==null || itemmoniScore=='null' || itemmoniScore==''){
					        	   itemmoniScore = "0.0";
					           }
					           var varProgressClass = 'blue'
					           if(Number(varProgress) < 30){
					        	   varProgressClass = 'red'
					           }
					           if(Number(varProgress) > 90){
					        	   varProgressClass = 'green'
					           }
					           var itemmoniScoreImage = "icon_05.png";
					           if(itemmoniScore!=undefined && itemmoniScore!=null && itemmoniScore!='' 
					        		   && itemmoniScore!='-' && Number(itemmoniScore)*1.0 >=60){
					        	   itemmoniScoreImage = "icon_06.png";
					           }
							    if(i%2==0){
									item.bgcolor="#e8e8e8"
								}else{
									item.bgcolor="#f7f7f7"
								}
							    
							    td[0].innerHTML = (rowIndex)+'、<a href="javascript:void(0)" onclick="openTk(\''+item.themeBankId+'\',\''+item.themeBankName+'\');">'+item.themeBankName+'</a>';
							    td[0].width="25%";
							    td[0].height="38";
							    td[0].className="list_study";
							    
							    td[1].innerHTML = '<div class="white"><div class="'+varProgressClass+'" style="width:'+varProgress+'%;"></div></div>';
							    td[1].width="20%";
							    
							    td[2].innerHTML = '完成'+finThemeNum+'题/共有'+themeNum+'题（'+varProgress+'%）';
							    td[2].width="23%";
							    
							    td[3].innerHTML = '最后模拟得分率：'+itemmoniScore+'%';
								td[3].width="22%";
							    
							    td[4].innerHTML = '<img src="${basePath}modules/hnjtamis/main/images/'+itemmoniScoreImage+'" />';
								td[4].width="10%";
							    		 
								//htmlstr+='<td width="25%" height="38" class="list_study"></td>';
								//htmlstr+='<td width="20%" >';
								//htmlstr+='<div class="white"><div class="'+varProgressClass+'" style="width:'+varProgress+'%;"></div></div></td>';
								//htmlstr+='<td width="23%" class="list_study2">完成'+finThemeNum+'题/共有'+themeNum+'题（'+varProgress+'%）</td>';
								//htmlstr+='<td width="22%" class="list_study2">最后模拟得分率：'+itemmoniScore+'%</td>';
								//htmlstr+='<td width="10%"><img src="${basePath}modules/hnjtamis/main/images/'+itemmoniScoreImage+'" /></td>';
								
								
								
								rowIndex++;
							}else{
								if(i%2==0){
									item.bgcolor="#e8e8e8"
								}else{
									item.bgcolor="#f7f7f7"
								}
							    
							    td[0].innerHTML = '&nbsp;';
							    td[0].width="25%";
							    td[0].height="38";
							    td[0].className="list_study";
							    
							    td[1].innerHTML ='&nbsp;';
							    td[1].width="20%";
							    
							    td[2].innerHTML = '&nbsp;';
							    td[2].width="23%";
							    
							    td[3].innerHTML = '&nbsp;';
								td[3].width="22%";
							    
							    td[4].innerHTML = '&nbsp;';
								td[4].width="10%";
							}
							//trs[i+me.startRow].innerHTML = htmlstr
						}
					}
					me.pageIndex = pIndex;
					me.pageMask2.hide();
					for(var i = 0;i<examArray.length;i++){
						setExamArray(examArray[i][1]);
					}
				},
			    failure:function(form, action){
			    	me.pageMask2.hide();
			    }
			});
		}
}
initExam();
</script>
</body>
</html>
