<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/tld/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/tld/fn.tld" prefix="fn"%>
<%-- 阅卷复核时的 单人阅卷视图 --%>
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
	</style>
</head>
<body style="background-color:#d1dff1;" id="mybody">
	<h1>${exam.examName } 考生答卷</h1>
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
			<c:forEach items="${timu.examTestpaperAnswerkeies }" var="citem" varStatus="cSortNo"> 
				<c:if test="${citem.isRight eq 10 }">
					<c:set var="rightAnswer" value="&nbsp;${rightAnswer }${citem['answerkeyValue'] };&nbsp;&nbsp;&nbsp;" />
					<c:set var="rightAnswer" value="${fn:replace(rightAnswer, 'upload', '../upload')}"></c:set>
					<c:set var="rightAnswer" value="${fn:replace(rightAnswer, 'upload', '../upload')}"></c:set>
				</c:if>
				<c:if test="${showTimuItemFlag eq 1 }">
					<c:set var="showLetter" value="${choiceNum[cSortNo.index] }" />
					<c:set var="answerkeyValueStr" value="${fn:replace(citem['answerkeyValue'], 'upload', '../upload')}"></c:set>
					<c:set var="answerkeyValueStr" value="${fn:replace(answerkeyValueStr, 'upload', '../upload')}"></c:set>
					<c:choose>
						<c:when test="${timu.eachline eq '0'}">
							<!-- 不换行 -->
							<span>${showLetter}.${answerkeyValueStr }</span>
						</c:when>
						<c:when test="${timu.eachline eq '1'}">
							<!-- 每行一个 -->
							<span>${showLetter}.${answerkeyValueStr }</span><br>
						</c:when>
						<c:when test="${timu.eachline eq '2'}">
							<!-- 每行两个 -->
							<c:if test="${(cSortNo.index+1)%2 eq 0 }">
								<span>${showLetter}.${answerkeyValueStr }</span><br>
							</c:if>
							<c:if test="${(cSortNo.index+1)%2 ne 0 }">
								<span>${showLetter}.${answerkeyValueStr }</span>
							</c:if>
						</c:when>
						<c:when test="${timu.eachline eq '3'}">
							<!-- 每行三个 -->
							<c:if test="${(cSortNo.index+1)%3 eq 0 }">
								<span>${showLetter}.${answerkeyValueStr }</span><br>
							</c:if>
							<c:if test="${(cSortNo.index+1)%3 ne 0 }">
								<span>${showLetter}.${answerkeyValueStr }</span>
							</c:if>
						</c:when>
						<c:when test="${timu.eachline eq '4'}">
							<!-- 每行四个 -->
							<c:if test="${(cSortNo.index+1)%4 eq 0 }">
								<span>${showLetter}.${answerkeyValueStr }</span><br>
							</c:if>
							<c:if test="${(cSortNo.index+1)%4 ne 0 }">
								<span>${showLetter}.${answerkeyValueStr }</span>
							</c:if>
						</c:when>
					</c:choose>
				</c:if>
			</c:forEach>
			<h3 class="rightAnswerClass">
				<c:if test="${rightAnswer ne null && rightAnswer ne '' }">
				<c:set var="rightAnswer" value="${fn:replace(rightAnswer, 'upload', '../upload')}"></c:set>
					正确答案: ${rightAnswer }
				</c:if>
			</h3>
			<h3 class="examineeAnswerClass">
				考生答案:
				<c:forEach items="${timu.examUserAnswerkeies}" var="examineeAnswer">
					<c:if test="${examineeAnswer.answerkeyValue ne null && examineeAnswer.answerkeyValue ne ''
						&& examineeAnswer.answerkeyValue ne 'null' }">
					<c:set var="answerkeyValueStr" value="${fn:replace(examineeAnswer.answerkeyValue, 'upload', '../upload')}"></c:set>
					${answerkeyValueStr}
					</c:if>
				</c:forEach>
				
			</h3>
			<input type="hidden" name="${timu.examTestpaperThemeId }timuType" value="${timu.themeTypeName }"/>
			<input type="hidden" name="${timu.examTestpaperThemeId }defaultScore" value="${timu.defaultScore }"/>
			<input type="hidden" name="timuId" value="${timu.examTestpaperThemeId }" />
			<div class="commentDiv">
				得分:<input type="text" name="${timu.examTestpaperThemeId }score"  maxlength="6" style="width:70px;" value="${timu.score }" readonly="readonly"/>
				评语:<input type="text" name="${timu.examTestpaperThemeId }comment"  maxlength="100" style="width:300px;" value="${timu.reviewComments }"/>
			</div>
		</div>
		</c:forEach>	
	</c:forEach>
	<div class="bottomDiv">
		<button id="rollBackBtn">打回试卷</button>
		<button id="reviewCommitBtn">阅卷结束</button>
	</div>
	<script type="text/javascript">
		var saveMask; 
		Ext.onReady(function(){
			Ext.get('rollBackBtn').on('click',function(){
				//遍历各题 验证是否都有 给予 评分 
				var scoreArray = Ext.query('input[name$=score]');//评分Array
				var timuTypeArray = Ext.query('input[name$=timuType]');//题目类型
				//打开遮掩层
				saveMask = new Ext.LoadMask(Ext.get('mybody'), {                          
						msg : '正在保存数据，请稍等',                           
						removeMask : true 
				}); 	
				saveMask.show();
				
				var timuIds = Ext.query('input[name=timuId]');//题目id集合
				var comments = Ext.query('input[name$=comment]');//评语集合
				var dataArray = new Array();
				var remoteTimuIds="";
				for(var i=0;i<timuIds.length;i++){
					remoteTimuIds+=timuIds[i].value+",";
					var data = {
							'examTestpaperThemeId':timuIds[i].value,
							'score':scoreArray[i].value,
							'reviewComments':comments[i].value
					};
					dataArray.push(data);
				}
				//验证通过提交后台  
				commitData(dataArray,remoteTimuIds,'rollback'); 
			});
			Ext.get('reviewCommitBtn').on('click',function(){
				//遍历各题 验证是否都有 给予 评分 
				var scoreArray = Ext.query('input[name$=score]');//评分Array
				var timuTypeArray = Ext.query('input[name$=timuType]');//题目类型
				//打开遮掩层
				saveMask = new Ext.LoadMask(Ext.get('mybody'), {                          
						msg : '正在保存数据，请稍等',                           
						removeMask : true 
				}); 	
				saveMask.show();
				
				var timuIds = Ext.query('input[name=timuId]');//题目id集合
				var comments = Ext.query('input[name$=comment]');//评语集合
				var dataArray = new Array();
				var remoteTimuIds="";
				for(var i=0;i<timuIds.length;i++){
					remoteTimuIds+=timuIds[i].value+",";
					var data = {
							'examTestpaperThemeId':timuIds[i].value,
							'score':scoreArray[i].value,
							'reviewComments':comments[i].value
					};
					dataArray.push(data);
				}
				//验证通过提交后台  
				commitData(dataArray,remoteTimuIds,'save'); 
			});
		});
		//提交
		function commitData(dataArray,remoteTimuIds,value){
			var remoteData = "";
			if(dataArray!=null && dataArray.length>0){
				for(var i=0;i<dataArray.length;i++){
					remoteData += JSON.stringify(dataArray[i])+"@&@";
				}
			}
			Ext.Ajax.request({
				url:'base/examreview/saveReReviewedForExamReviewFormAction!saveReReviewed.action',
				method:'POST',
				params:{
					dataArray:remoteData,
					timuIds:remoteTimuIds,
					examId:'${exam.examId }',
					saveWay:value
				},
				success:function(response){
					var re = Ext.decode(response.responseText);
					Ext.Msg.alert('提示',re['msg'],function(){
						//window.parent.grablEapLogin.desktop.closeWindow('1');
						window.parent.Ext.getCmp('mytest').close();
					});
					saveMask.hide();
				},
				failure:function(){
					Ext.Msg.alert("信息","未能与服务器取得通讯");
					saveMask.hide();
				}
			});
		}
		
		//获取题号
		function getTiHao(timuTypeArray,indexNum){
			var tihao = indexNum+1;
			var flag = 0;
			for(var i=indexNum;(i>-1)&&((i-1)>-1);i--){
				if(timuTypeArray[i].value!=timuTypeArray[i-1].value){
					flag = i;
					break;
				}
			}
			return tihao-flag;
		}
		function isNotNull(value){
			if(value!=null && trim(value)!=''){
				return true;
			}else{
				return false;
			}
		}
		function trim(str){
		    str = str.replace(/^(\s|\u00A0)+/,'');
		    for(var i=str.length-1; i>=0; i--){
		        if(/\S/.test(str.charAt(i))){
		            str = str.substring(0, i+1);
		            break;
		        }
		    }
		    return str;
		}
		//验证数字 
		function checkNum(obj,str){
			if(isNotNull(str)){
				//var numRule = new RegExp("^(([0-9]+\\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\\.[0-9]+)|([0-9]*[1-9][0-9]*))$");
				var numRule = new RegExp("^[0-9]+(.[0-9]{1,2})?$");
				if(!numRule.test(str)){
					Ext.Msg.alert('提示','请输入正实数作为分值<br>最多两位小数 ',function(){
						obj.focus();
					});
				}
			}
		}
		// Firefox, Google Chrome, Opera, Safari, Internet Explorer from version 9 
        function OnInput (event) {
        	checkNum(event.target.value);
        }
  		// Internet Explorer
        function OnPropChanged (event) {
            if (event.propertyName.toLowerCase () == "value") {
            	checkNum(event.srcElement.value);
            }
        } 
	</script>
</body>
</html>