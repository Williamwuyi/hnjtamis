<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/tld/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/tld/fn.tld" prefix="fn"%>
<%-- 查询考生试卷得分详细情况 --%>
<!DOCTYPE html>
<html>

<c:set var="request" scope="page" value="${pageContext.request}" />
<c:set var="serverPort" value="${request.serverPort}" />
<c:set var="base" scope="page" value="${request.scheme}://${request.serverName}:${serverPort}" />
<c:set var="contextPath" scope="page" value="${request.contextPath}" />
<c:set var="basePath" scope="page" value="${base}${contextPath}/" />

<head>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="Access-Control-Allow-Origin" content="*">
<title>亿泰企业管理平台</title>
    <script type="text/javascript" src="${basePath }base/ext/ext-all.js"></script>
	<link href="${basePath}resources/css/ext-all.css" rel="stylesheet" type="text/css">
	<style type="text/css">
		.tixing{
			margin-left:20px;
		}
		.timu{
			margin-left:40px;
			margin-right:40px;
			background-color: #c3c8e0;
			padding-left: 5px;
		}
		.timu div.commentDiv{
			font-size: 15px;
			font-weight: bold;
			margin-left: 5px;
			padding-bottom: 5px;
		}
		.timu span{
			font-size: 18px;
			margin-left:20px;
		}
		.timu h2{
		  padding-top: 3px;
		}
		.timu h2 span{
			font-size: 14px;
		}
		h1{
		  text-align: center;
		}
		.bottomDiv{
			text-align: center;
			margin-top:10px;
		}
		.bottomDiv button{
			width:100px;
			height:40px;
		}
		h3.rightAnswerClass{
			color:Green;
		}
		h3.examineeAnswerClass{
			color:SlateBlue;
		}
		.greenAnswer{
			color:Green;
		}
.R_btn03 {color: #FFF;border: 0px solid #478903;width: 115px;height: 36px;margin-right: 4px;cursor: pointer;background-color: #2694d4;background-image: url(${basePath}/modules/hnjtamis/mainEx/images/ks_btn05.jpg);}
.R_btn04 {color: #FFF;border: 0px solid #478903;width: 115px;height: 36px;margin-right: 4px;cursor: pointer;background-color: #2694d4;background-image: url(${basePath}/modules/hnjtamis/mainEx/images/ks_btn04.jpg);}	
	</style>
	<script type="text/javascript">
	var examThemeBodyMask = null;
	var isNotInitAll = true;
	function fkThemeShow(themeId){
		if(isNotInitAll){
			alert('页面还未初始化完毕，请等待！');
		}else{
			if(examThemeBodyMask == null){
				examThemeBodyMask = new Ext.LoadMask(document.getElementById("mybody"), {  
				    msg     : '数据正在处理,请稍候',  
				    removeMask  : true// 完成后移除  
				});
			}
			Ext.Ajax.request({
				url : '${basePath}onlineExam/fkThemeShowForOnlineListAction!fkThemeShow.action',
				params : {
					id : themeId
				},
				method : 'POST',
				timeout: 15000,
				success : function(response) {
						var result = Ext.decode(response.responseText);
			    		var ansfktheme = result.fktheme;
			    		var themeFkauditFormList = result.themeFkauditFormList;
			    		var htmlStr = "<div style='width:690px;overflow-x:auto;overflow-y:hidden;word-break: break-all; word-wrap:break-word;'>";
						if(ansfktheme.lastFkState=='20' || ansfktheme.lastFkState=='30' || ansfktheme.lastFkState=='40' || ansfktheme.lastFkState=='-40'){
							htmlStr+="<div style='font-weight: normal; font-size: 14px;width:100%;text-align:right;padding:10px;'>"
							htmlStr+="<input type=\"button\" class=\"R_btn04\" value=\" \" alt=\"关闭\" onclick=\"fkCjThemeWin.hide();\" />";      
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
							htmlStr+="<div style='font-weight: normal; font-size: 14px;width:690px;text-align:right;padding:10px;'>"
							htmlStr+="<input type=\"button\" class=\"R_btn03\" value=\" \" alt=\"提交反馈\" onclick=\"fkCjThemeWin.sumitCjFk();\"/>";
							htmlStr+="<input type=\"button\" class=\"R_btn04\" value=\" \" alt=\"关闭\" onclick=\"fkCjThemeWin.hide();\" />";      
							htmlStr+="</div>";
							
							htmlStr+="<div style='font-weight: normal; font-size: 20px;width:690px;padding:10px;padding-bottom:3px;padding-top:3px;'>反馈说明：</div>";
							htmlStr+="<div style='font-weight: normal; font-size: 13px;width:690px;word-break: break-all; word-wrap:break-word;padding:10px;padding-top:3px;'>"
							htmlStr+="<textarea name='fkRemark_"+themeId+"' id='fkRemark_"+themeId+"'  rows='10' cols='300' style='width:650px;height:220'></textarea></div>";	
						}
						htmlStr+="</div>";
						htmlStr+="<div style='width:100%;overflow-x:auto;overflow-y:hidden;word-break: break-all; word-wrap:break-word;padding-left:10px;padding-right:10px;'>";
						htmlStr+="<font color=red>注意：请详细填写反馈的问题，如：答案A.压强、速度和粘度；存在**问题，不要只反馈：答案A存在错误，因为您查看的答案顺序可能与系统中试题管理中的答案顺序不一致，系统在出题时会根据情况打乱答案顺序。</font>";
						htmlStr+="</div>";
						fkCjThemeWin = new Ext.Window({
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
						fkCjThemeWin.sumitCjFk = function(){
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
									url : '${basePath}exam/base/theme/saveFkForThemeFormAction!saveFk.action?op=fk',
									params : {
										json : jsonStr
									},
									method : 'POST',
									timeout: 15000,
									success : function(response) {
										 var result = Ext.decode(response.responseText);
										 Ext.Msg.alert('提示', result.msg, function(){
										 	fkCjThemeWin.hide();
										 	examThemeBodyMask.hide();
										 	//location.reload();
										 });
									},
									failure : function() { }
								 });
							}
						}
						fkCjThemeWin.show();
			    		
			    },
				failure : function() {
				}
			});
		}
	}

	function replaceString(str,reallyDo,replaceWith) { 
		var e=new RegExp(reallyDo,"g"); 
		words = str.replace(e, replaceWith); 
		return words; 
	}
	</script>
</head>
<body style="background-color:#d1dff1;" id="mybody">
	<c:if test="${showExamTitle eq 1 || showExamTitle eq '1'}">
		<h1>${exam.examName } 考生答卷</h1>
	</c:if>
	<c:forEach items="${resultMap }" var="tixing" varStatus="tixingNum">
		<h2 class="tixing">${themeTypeNum[tixingNum.index]}、${tixing.key } (共${fn:length(tixing.value) }题,共${scoreMap[tixing.key] }分)</h2>
		<c:set var="showTimuItemFlag" value="1" />
		<c:if test="${!(tixing.key eq '单选题' || tixing.key eq '多选题' || tixing.key eq '判断题') }">
			<c:set var="showTimuItemFlag" value="0" />
		</c:if>
		<c:forEach items="${tixing.value }" var="timu" varStatus="timuNum">
			<div class="timu">
				<h2>
				    <c:set var="themeNameStr" value="${fn:replace(timu.themeName, 'upload', '../upload')}"></c:set>
				    <c:set var="themeNameStr" value="${fn:replace(themeNameStr, 'upload', '../upload')}"></c:set>
					${timuNum.index+1 }、 ${themeNameStr}&nbsp;&nbsp;&nbsp;<span>分值:(${timu.defaultScore })</span>
				</h2>
			<c:set var="rightAnswer" value="" />
			<c:set var="rightAnswerLetter" value="" />
			<c:set var="rightAnswerNum" value="0" />
			<c:forEach items="${timu.examTestpaperAnswerkeies }" var="citem" varStatus="cSortNo"> 
				<c:set var="tmpAnswer" value="" />
				<c:if test="${citem.isRight eq 10 }">
					<c:set var="tmpAnswer" value="greenAnswer" />
					<c:set var="rightAnswer" value="&nbsp;${rightAnswer }${citem['answerkeyValue'] };&nbsp;&nbsp;&nbsp;" />
					<c:set var="rightAnswer" value="${fn:replace(rightAnswer, 'upload', '../upload')}"></c:set>
					<c:set var="rightAnswerNum" value="${rightAnswerNum+1}" />
				</c:if>
				<c:if test="${showTimuItemFlag eq 1 }">
					<c:set var="showLetter" value="${choiceNum[cSortNo.index] }" />
					<%-- c:if test="${tmpAnswer ne null && tmpAnswer ne ''}" --%>
						<c:if test="${citem.isRight eq 10 }">
						<c:set var="rightAnswerLetter" value="${rightAnswerLetter }${showLetter }" />
						</c:if>
						<c:set var="answerkeyValueStr" value="${fn:replace(citem['answerkeyValue'], 'upload', '../upload')}"></c:set>
						<c:set var="answerkeyValueStr" value="${fn:replace(answerkeyValueStr, 'upload', '../upload')}"></c:set>
					<%--/c:if --%>
					<c:choose>
						<c:when test="${timu.eachline eq '0'}">
							<!-- 不换行 -->
							<span class="${tmpAnswer }">${showLetter}.${answerkeyValueStr }</span>
						</c:when>
						<c:when test="${timu.eachline eq '1'}">
							<!-- 每行一个 -->
							<span class="${tmpAnswer }">${showLetter}.${answerkeyValueStr }</span><br>
						</c:when>
						<c:when test="${timu.eachline eq '2'}">
							<!-- 每行两个 -->
							<c:if test="${(cSortNo.index+1)%2 eq 0 }">
								<span class="${tmpAnswer }">${showLetter}.${answerkeyValueStr }</span><br>
							</c:if>
							<c:if test="${(cSortNo.index+1)%2 ne 0 }">
								<span class="${tmpAnswer }">${showLetter}.${answerkeyValueStr }</span>
							</c:if>
						</c:when>
						<c:when test="${timu.eachline eq '3'}">
							<!-- 每行三个 -->
							<c:if test="${(cSortNo.index+1)%3 eq 0 }">
								<span class="${tmpAnswer }">${showLetter}.${answerkeyValueStr }</span><br>
							</c:if>
							<c:if test="${(cSortNo.index+1)%3 ne 0 }">
								<span class="${tmpAnswer }">${showLetter}.${answerkeyValueStr }</span>
							</c:if>
						</c:when>
						<c:when test="${timu.eachline eq '4'}">
							<!-- 每行四个 -->
							<c:if test="${(cSortNo.index+1)%4 eq 0 }">
								<span class="${tmpAnswer }">${showLetter}.${answerkeyValueStr }</span><br>
							</c:if>
							<c:if test="${(cSortNo.index+1)%4 ne 0 }">
								<span class="${tmpAnswer }">${showLetter}.${answerkeyValueStr }</span>
							</c:if>
						</c:when>
					</c:choose>
				</c:if>
			</c:forEach>
			<h3 class="rightAnswerClass" style="display:${showTimuItemFlag eq 1?'none':'block'}">
				<c:if test="${rightAnswer ne null && rightAnswer ne '' }">
				<c:set var="rightAnswer" value="${fn:replace(rightAnswer, 'upload', '../upload')}"></c:set>
					正确答案: ${rightAnswer }
				</c:if>
			</h3>
			<h3 class="examineeAnswerClass">
				考生答案:
				<c:set var="answerImg" value="right.jpg" />
				<c:if test="${timu.examUserAnswerkeies eq null || fn:length(timu.examUserAnswerkeies) == 0}">
					<c:set var="answerImg" value="wrong.jpg" />
				</c:if>
				<c:if test="${timu.examUserAnswerkeies ne null && fn:length(timu.examUserAnswerkeies) > 0}">
				<c:forEach items="${timu.examUserAnswerkeies}" var="examineeAnswer">
					<c:if test="${examineeAnswer.answerkeyValue ne null && examineeAnswer.answerkeyValue ne 'null' }">
						<c:set var="answerkeyValueStr" value="${fn:replace(examineeAnswer.answerkeyValue, 'upload', '../upload')}"></c:set>
						<span>${answerkeyValueStr}</span>
						<c:choose>
						<c:when test="${fn:contains(rightAnswerLetter,examineeAnswer.answerkeyValue) }">
						</c:when>
						<c:otherwise>
							<c:set var="answerImg" value="wrong.jpg" />
						</c:otherwise>
					</c:choose>
					</c:if>
					<c:if test="${!(examineeAnswer.answerkeyValue ne null && examineeAnswer.answerkeyValue ne 'null' )}">
						<c:set var="answerImg" value="wrong.jpg" />
					</c:if>
				</c:forEach>
				</c:if>
				<c:if test="${tixing.key eq '多选题' && timu.examUserAnswerkeies ne null && rightAnswerNum!= fn:length(timu.examUserAnswerkeies) }">
					<c:set var="answerImg" value="wrong.jpg" />
				</c:if>
				<c:if test="${tixing.key eq '单选题' || tixing.key eq '多选题' || tixing.key eq '判断题'}">
				<img alt="" src="${basePath}resources/personImage/${answerImg}" style="height:40px;width:40px;" >
				</c:if>
			</h3>
			<input type="hidden" name="${timu.examTestpaperThemeId }timuType" value="${timu.themeTypeName }"/>
			<input type="hidden" name="${timu.examTestpaperThemeId }defaultScore" value="${timu.defaultScore }"/>
			<input type="hidden" name="timuId" value="${timu.examTestpaperThemeId }" />
			<div class="commentDiv">
				<a href="javascript:fkThemeShow('${timu.themeId}');">点击此处进行试题反馈</a>&nbsp;&nbsp;&nbsp;得分:${timu.score }
				<c:if test="${fn:length(timu.reviewComments)>0 }">
					&nbsp;&nbsp;&nbsp;评语: ${timu.reviewComments }
				</c:if>
			</div>
		</div>
		</c:forEach>	
	</c:forEach>
	<div class="bottomDiv">
		<button id="expBtn">导出</button>
	</div>
	<script type="text/javascript">
		var saveMask; 
		Ext.onReady(function(){
			Ext.get('expBtn').on('click',function(){
				var url = 'base/scorequery/exportToDocForExamScoreQueryListAction!exportToDoc.action?testPaperId='+'${testPaperId}';
				window.open(url);
			});
			isNotInitAll = false;
		});
</script>
</body>
</html>