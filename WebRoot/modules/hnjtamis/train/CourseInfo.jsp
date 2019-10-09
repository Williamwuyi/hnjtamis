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
		<base href="${basePath}modules/hnjtamis/train/" />
		<title></title>
		<link rel="stylesheet" type="text/css"
			href="${basePath}resources/css/ext-all.css" />
		<link rel="stylesheet" type="text/css"
			href="${basePath}modules/hnjtamis/train/course.css" />
		<script type="text/javascript" src="${basePath}base/ext/ext-all.js"></script>
		<style type="text/css">
.myHeader .cont {
	margin: 0 auto;
	width: 1000px;
}

.mainList1 {
	padding-bottom: 16px;
}

.mainList1 td,.mainList1 th {
	text-align: center;
}

.mainList1 td.name,.mainList1 th.name {
	text-align: left;
}

strong {
	font-weight: bold !important;
}

.imgStudy {
	float: right;
	margin: 10px;
	width: 14px;
}
</style>
		<script>
	function activeTab(tabId) {
		window.parent.doActiveTab(tabId);
	}
</script>
	</head>
	<body>
		<div class="page">
			<div class="courseInfo">
				<div class="leftPage">
					<div class="wrap">
						<h2>
							<img src="images/h2Icon01.gif" />
							课程信息
						</h2>
						<div class="cont3">
							<ul>
								<li id="courseTitle">
									课程名称：${course.trainImplement.subject}
								</li>
								<li id="liTeacher">
									培训教师：${course.trainImplement.teacher}
								</li>
							</ul>
							<div style="clear: both;"></div>
						</div>
						<h2>
							<img src="${basePath}modules/hnjtamis/train/images/h2Icon02.gif" />
							课程概况
						</h2>
						<div class="cont1">
							<p id="courseRemark" style="height: 200px; overflow: hidden">
								${course.trainImplement.remark}
							</p>
						</div>
						<h2>
							<img src="${basePath}modules/hnjtamis/train/images/h2Icon03.gif" />
							课程目标
						</h2>
						<div class="cont2">
							<p id="courseLearningTarget"
								style="height: 200px; overflow: hidden;">
								${course.trainImplement.learningTarget}
							</p>
						</div>
					</div>
				</div>
				<div class="rightPage">
					<div class="wrap">
						<h2>
							<img src="${basePath}modules/hnjtamis/train/images/h2Icon01.gif" />
							课前评测情况
						</h2>
						<div class="cont3">
							<ul>
								<li id="testBeforeGrade">
									评测成绩：${before.examScore}
								</li>
								<li id="testBeforeStart">
									开始日期：${before.inTime}
								</li>
								<li id="testBeforeEnd">
									结束日期：${before.subTime}
								</li>
								<li>
									<input type="button" id="btnTestBefore" value="进入" class="btn1"
										onclick="activeTab('TestBefore');" />
								</li>
							</ul>
							<div class="clear">
							</div>
						</div>
						<h2>
							<img src="${basePath}modules/hnjtamis/train/images/h2Icon01.gif" />
							课程学习情况
						</h2>
						<div class="cont3">
							<ul>

								<li id="liStartDate">
									开始时间：${startTime}
								</li>
								<li id="liEndDate">
									结束时间：${endTime}
								</li>
								<li>
									学习完成情况：
									<span id="courseOver" style="color: Black">${studyCount}</span>
									/
									<span id="totalCourse">${allCount}</span>
								</li>
								<%--<li>
									必考完成情况：
									<span id="examOver" style="color: Black">0</span> /
									<span id="tltalExam">0</span>
								</li>--%>
								<li>
									<input type="button" id="btnStartStudy" value="进入" class="btn1"
										onclick="activeTab('Study');" />
								</li>
							</ul>
							<div class="clear">
							</div>
						</div>
						<h2>
							<img src="${basePath}modules/hnjtamis/train/images/h2Icon01.gif" />
							课后考试情况
						</h2>
						<div class="cont3">
							<ul>
								<li id="testAfterGrade">
									评测成绩：${after.examScore}
								</li>
								<li id="testAfterStartTime">
									开始日期：${after.inTime }
								</li>
								<li id="testAfterEndTime">
									结束日期：${after.subTime }
								</li>
								<li id="testAfterIsPass">
									是否通过：${after.examScore >= after.totalScore * afterPassScore /100 ? "是" : "否" }
								</li>
								<li>
									<input type="button" id="btnTestAfter" value="进入" class="btn1"
										onclick="activeTab('TestAfter');" />
								</li>
							</ul>
							<div class="clear">
							</div>
							<ul>
								<li id="finishStatus"
									style="background: #eee; font-size: 14px; padding: 5px 0px;">
									完成状态：
									<c:choose>
										<c:when test="${studyCount < allCount || after.examScore < after.totalScore * afterPassScore /100}">
											<span class="red">未完成</span>
										</c:when>
										<c:otherwise>
											<span class="green">已完成</span>
										</c:otherwise>
									</c:choose>
								</li>

							</ul>
						</div>
					</div>
				</div>
				<div class="clear">
				</div>
			</div>
		</div>
	</body>
</html>