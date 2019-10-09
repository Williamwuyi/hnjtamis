<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="/WEB-INF/tld/c.tld" prefix="c"%>

<html xmlns="http://www.w3.org/1999/xhtml">
	<c:set var="request" scope="page" value="${pageContext.request}" />
	<c:set var="base" scope="page"
		value="${request.scheme}://${request.serverName}:${request.serverPort}" />
	<c:set var="contextPath" scope="page" value="${request.contextPath}" />
	<c:set var="basePath" scope="page" value="${base}${contextPath}/" />
	<head>
		<base href="${basePath}"/>
		<title>课程学习记录</title>
			<link rel="stylesheet" type="text/css"
				href="${basePath}resources/css/ext-all.css" />
			<link rel="stylesheet" type="text/css"
				href="${basePath}modules/hnjtamis/train/course.css"  media="screen"/>
		<style>			
.mainCont,.mainCont_title{
	    margin-left:208px!important;
	    margin-left:205px;
	    border:solid 1px #ccc;
    }
.myHeader .cont{margin:0 auto; width:100%;}
.mainList1{padding-bottom:16px;}
.mainList1 td,.mainList1 th{text-align:center;}
.mainList1 td.name,.mainList1 th.name{text-align:left;}
strong{font-weight:bold!important;}
		</style>
	</head>
	<body>
		<div id="divContent" class="page">
			<div class="">
				<div style="border-bottom: dashed 1px #ccc; padding-bottom: 3px;"
					class="name">
					<img src="${basePath}modules/hnjtamis/train/images/lightbulb.png" class="icon" />
					<strong>学习情况</strong>
				</div>
				<div class="mainList1">
					<div>
						<table cellspacing="0" rules="all" border="1" id="grvList"
							style="width: 100%; border-collapse: collapse;">
							<tr class="theadbg">
								<th align="left" scope="col">
									<strong>课程内容</strong>
								</th>
								<th scope="col">
									<strong>模式</strong>
								</th>
								<th scope="col" style="width:140px">
									<strong>开始时间</strong>
								</th>
								<th scope="col" style="width:140px">
									<strong>结束时间</strong>
								</th>
								<th scope="col">
									<strong>应学</strong>
								</th>
								<th scope="col">
									<strong>实学</strong>
								</th>
								<th scope="col">
									<strong>学习次数</strong>
								</th>
								<th class="last_td" scope="col">
									<strong>学习状态</strong>
								</th>
							</tr>
							<c:forEach var="item" items="${recordList}" varStatus="status">
							<tr>
								<td class="name" style="white-space: nowrap;">
									<c:forEach var="i" begin="1" end="${item.level}" step="1">
										&nbsp;&nbsp;&nbsp;&nbsp;
									</c:forEach>
									<c:if test="${item.level == 2}">
										<img src="${basePath}resources/icons/fam/book.png"/>
									</c:if>
									${item.fileName}
								</td>
								<td>
									${empty(item.isRequired) ? "" :(item.isRequired == 1 ? "必修" : "选修")}
								</td>
								<td>
									${item.startTime}
								</td>
								<td>
									${item.endTime}
								</td>
								<td>
									<c:if test="${item.needDuration != 0}">
										${item.needDuration}${item.unit}
									</c:if>		
								</td>
								<td>
									<c:if test="${item.duration != 0}">
										${item.duration}${item.unit}
									</c:if>
								</td>
								<td>
									${item.studyCount == 0 ? "" : item.studyCount}
								</td>
								<td>
									<c:if test="${item.level == 2}">
									<c:choose>
										<c:when test="${item.finishStatus == null}">
											<font color="red">未开始</font>
										</c:when>
										<c:when test="${item.finishStatus == 1}">
											<font color="green">已完成</font>
										</c:when>
										<c:otherwise>
											<font color="blue">学习中</font>
										</c:otherwise>
									</c:choose>
									</c:if>
								</td>
							</tr>
							</c:forEach>
						</table>
					</div>
				</div>

				<%--<div class="mainList1">
					<table border="0" cellpadding="0" cellspacing="0">
						<tbody>
							<tr>
								<td width="120">
									学习次数
								</td>
								<td id="studyTimes">
									22次
								</td>

								<td width="120">
									总学习时间
								</td>
								<td id="studyTimeSum">
									151分钟
								</td>

							</tr>
							<tr>
								<td align="right">
									完成学习时间
								</td>
								<td id="finishStudyTime">
									<span></span>
								</td>

								<td>
									学习进度
								</td>
								<td id="studyProcess">
									<span style="color: Red">21%</span>(规定完成率：50%)
								</td>

							</tr>
							<tr>
								<td align="right">
									结业状态
								</td>
								<td id="graduationStateTd" colspan="3">
									未结业
								</td>

							</tr>
						</tbody>
					</table>
				</div>--%>

				<div style="border-bottom: dashed 1px #ccc; padding-bottom: 3px;"
					class="name">
					<img src="${basePath}modules/hnjtamis/train/images/lightbulb.png" class="icon" />
					<strong>课后考试情况</strong>
				</div>
				<div class="mainList1">
					<div>
						<table class="listTable" cellspacing="0" rules="all" border="1"
							id="courseExamGvList" style="border-collapse: collapse;">
							<tr>
								<th class="name" scope="col">
									考试名称
								</th>
								<th scope="col" style="white-space: nowrap;">
									开始时间
								</th>
								<th scope="col" style="white-space: nowrap;">
									结束时间
								</th>
								<th scope="col">
									总分
								</th>
								<th scope="col">
									通过分数
								</th>
								<th scope="col">
									个人得分
								</th>
								<th scope="col" style="white-space: nowrap;">
									是否通过
								</th>
								<th scope="col" style="white-space: nowrap;">
									评分状态
								</th>
								<%--<th scope="col">
									操作
								</th>--%>
							</tr>
							<c:forEach var="after" items="${afterList}" varStatus="status">
							<tr>
								<td class="name">
									课后考试
								</td>
								<td>
									${after.inTime}
								</td>
								<td>
									${after.subTime}
								</td>
								<td>
									${after.totalScore}
								</td>
								<td>
									${after.totalScore * passScore /100 }
								</td>
								<td>
									${after.examScore}
								</td>
								<td>
									${after.examScore >= after.totalScore * passScore /100 ? "是" : "否" }
								</td>
								<td>
									${empty(after.subTime)?"未评":"已评"}
								</td>
								<%--<td>
									查看答卷
								</td>--%>
							</tr>
							</c:forEach>
						</table>
					</div>
				</div>

				<div style="border-bottom: dashed 1px #ccc; padding-bottom: 3px;"
					class="name">
					<img src="${basePath}modules/hnjtamis/train/images/lightbulb.png" class="icon" />
					<strong>课前评测情况</strong>
				</div>
				<div class="mainList1">
					<div>
						<table class="listTable" cellspacing="0" rules="all" border="1"
							id="courseTestGvList" style="border-collapse: collapse;">
							<tr>
								<th class="name" scope="col">
									练习名称
								</th>
								<th scope="col" style="white-space: nowrap;">
									开始时间
								</th>
								<th scope="col" style="white-space: nowrap;">
									结束时间
								</th>
								<th scope="col">
									总分
								</th>
								<th scope="col">
									通过分数
								</th>
								<th scope="col">
									个人得分
								</th>
							</tr>
							<c:forEach var="before" items="${beforeList}" varStatus="status">
							<tr>
								<td class="name">
									课前测评
								</td>
								<td>
									${before.inTime}
								</td>
								<td>
									${before.subTime}
								</td>
								<td>
									${before.totalScore}
								</td>
								<td>
									${before.totalScore * passScore /100 }
								</td>
								<td>
									${before.examScore}
								</td>
							</tr>
							</c:forEach>
						</table>
					</div>
				</div>
			</div>
			<div style="padding: 16px; text-align: center;">
				<input type="button" value="关闭本页" class="btn1" style="display: none" />
			</div>
		</div>
		<p>
			&nbsp;&nbsp;&nbsp;
		</p>
	</body>
</html>
