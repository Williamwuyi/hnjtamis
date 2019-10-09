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
		<base href="${basePath}" />
		<title></title>
		<link rel="stylesheet" type="text/css"
			href="${basePath}resources/css/ext-all.css" />
		<link rel="stylesheet" type="text/css"
			href="${basePath}modules/hnjtamis/train/course.css" />
		<link rel="stylesheet" type="text/css"
			href="${basePath}modules/hnjtamis/train/resource/minimalist.css" />
		<script type="text/javascript" src="${basePath}base/ext/ext-all.js"></script>
		<script type="text/javascript"
			src="${basePath}modules/hnjtamis/train/resource/jquery-1.11.2.min.js"></script>
		<script type="text/javascript"
			src="${basePath}modules/hnjtamis/train/resource/datetime.js"></script>
		<script type="text/javascript">
		function test() {
			Ext.Ajax.request({
				method : 'GET',
				url : 'param/findByCodeForSystemParamsFormAction!findByCode.action?code=IOCPURL',
				async : true,
				success : function(response) {
					var result = Ext.decode(response.responseText);
					if (result.form) {
						Ext.data.JsonP.request({
					     	timeout: 10000,
					  	 	url : result.form.value + '/gettime',
					   		callbackKey: "jsonpcallback",
					   		success : function(result) {
					   			document.getElementById("state").innerHTML = "<font color='green'>正在运行</font>";
					   			document.getElementById("statetime").innerText = result.result.date;
					   		},
			        	    failure: function(result) {
					   			document.getElementById("state").innerHTML= "<font color='red'>已停止</font>";
					   			document.getElementById("statetime").innerText = "";
			        	    }
					 });
					}
				},
				failure : function() {
					Ext.Msg.alert('信息', '后台未响应，网络异常！');
				}
			});
		}
	Ext.onReady(function() {
		test();
	});
</script>
	</head>
	<body>
		<div id="divContent" class="page" style="width:500px">
			<div class="">
				<div style="border-bottom: dashed 1px #ccc; padding-bottom: 3px;"
					class="name">
					<img src="${basePath}modules/hnjtamis/train/images/lightbulb.png" class="icon" />
					<strong>服务端运行情况</strong>&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:test();">刷新</a>
				</div>
				<div class="mainList1">
					<div>
						<table cellspacing="0" rules="all" border="1" id="grvList"
							style="width: 100%; border-collapse: collapse;">
							<tr class="theadbg">
								<th align="left" scope="col">
									<strong>运行状态</strong>
								</th>
								<th scope="col">
									<strong>刷新时间</strong>
								</th>
							</tr>
							<tr>
								<td id="state">
									&nbsp;
								</td>
								<td id="statetime">
									&nbsp;
								</td>
							</tr>
						</table>
					</div>
				</div>
		</div>
	</body>
</html>