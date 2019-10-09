<%@ page language="java" contentType="text/html; charset=gbk"
	pageEncoding="gbk"%>
<jsp:directive.page import="com.ite.sso.login.SessionHouse"/>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/c.tld" prefix="c"%>
<c:set var="request" scope="page" value="${pageContext.request}" />
<c:set var="base" scope="page"
	value="${request.scheme}://${request.serverName}:${request.serverPort}" />
<c:set var="contextPath" scope="page" value="${request.contextPath}" />
<c:set var="basePath" scope="page" value="${base}${contextPath}/" />
<html>
	<c:set var="contextPath" scope="page" value="${request.contextPath}" />
	<HEAD>
	   <link rel="stylesheet" id='skin' type="text/css" href="${basePath}/resource/${faceStyle}/css/main.css" />
	   <link rel="stylesheet" id='skin' type="text/css" href="${basePath}/resource/${faceStyle}/css/ymPrompt.css" />
       <script type="text/javascript" src="${basePath}/resource/js/ymPrompt.js"></script>
       <script type="text/javascript" src="${basePath}/resource/js/cookie.js?00009" charset="UTF-8"></script>
		<TITLE>�����û���Ϣ</TITLE>
	</HEAD>

	<body>
	<table cellspacing="0" cellpadding="0" width="100%" border="0" class="tablecss">
	        <tr>
				<td align="left" class="tabletdcssA">
					<div align="right"><font color=red>*</font>session ID:</div>
			    </td>
				<td class="tabletdcssB">
				        <%=session.getId()%>
				</td>
			</tr>
			<tr>
				<td align="left" class="tabletdcssA">
					<div align="right"><font color=red>*</font>�û�:</div>
			    </td>
				<td class="tabletdcssB">
				        ${sessionScope.USERSESSION.username}(${sessionScope.USERSESSION.userid})
				</td>
			</tr>
			<tr>
				<td align="left" class="tabletdcssA">
					<div align="right"><font color=red>*</font>IP:</div>
			    </td>
				<td class="tabletdcssB">
				        <%=request.getServerName() %>
				</td>
			</tr>
			<tr>
				<td align="left" class="tabletdcssA">
					<div align="right"><font color=red>*</font>��λ:</div>
			    </td>
				<td class="tabletdcssB">
				        ${sessionScope.USERSESSION.quartername}(${sessionScope.USERSESSION.quarterid})
				</td>
			</tr>
			<tr>
				<td align="left" class="tabletdcssA">
					<div align="right"><font color=red>*</font>��ǰ����:</div>
			    </td>
				<td class="tabletdcssB">
				        ${sessionScope.USERSESSION.deptname}(${sessionScope.USERSESSION.deptid})
				</td>
			</tr>
			<tr>
				<td align="left" class="tabletdcssA">
					<div align="right"><font color=red>*</font>�û����ڲ���:</div>
			    </td>
				<td class="tabletdcssB">
				        ${sessionScope.USERSESSION.userdeptname}(${sessionScope.USERSESSION.userdeptid})
				</td>
			</tr>
			<tr>
				<td align="left" class="tabletdcssA">
					<div align="right"><font color=red>*</font>��ǰ����:</div>
			    </td>
				<td class="tabletdcssB">
				        ${sessionScope.USERSESSION.organname}(${sessionScope.USERSESSION.organid})
				</td>
			</tr>
			<tr>
				<td align="left" class="tabletdcssA">
					<div align="right"><font color=red>*</font>�û����ڻ���:</div>
			    </td>
				<td class="tabletdcssB">
				        ${sessionScope.USERSESSION.userorganname}(${sessionScope.USERSESSION.userorganid})
				</td>
			</tr>
			<tr>
				<td align="left" class="tabletdcssA">
					<div align="right"><font color=red>*</font>Ա��:</div>
			    </td>
				<td class="tabletdcssB">
				        ${sessionScope.USERSESSION.employeename}(${sessionScope.USERSESSION.employeeid})
				</td>
			</tr>
			<tr>
				<td align="left" class="tabletdcssA">
					<div align="right"><font color=red>*</font>��ʽ:</div>
			    </td>
				<td class="tabletdcssB">
				        ${sessionScope.USERSESSION.faceStyle}
				</td>
			</tr>
			<tr>
				<td align="left" class="tabletdcssA">
					<div align="right"><font color=red>*</font>Ȩ��:</div>
			    </td>
				<td class="tabletdcssB">
				        <table cellspacing="0" cellpadding="0" width="100%" border="0" class="tablecss">
					        <tr>
								<td align="left" class="tabletdcssA">��Դ����</td>
								<td align="left" class="tabletdcssA">��Դ����</td>
								<td class="tabletdcssA">·��</td>
								<td class="tabletdcssA">�Ƿ���Ȩ��</td>
							</tr>
							<c:forEach var="item" items="${sessionScope.USERSESSION.popedoms}">
							<tr>
							    <td align="left" class="tabletdcssB">${item.id}</td>
							    <td align="left" class="tabletdcssB">${item.name}</td>
								<td class="tabletdcssB">${item.url}</td>
								<td class="tabletdcssB">${item.have=="0"?"��":"��"}</td>
							</tr>
							</c:forEach>
						</table>
				</td>
			</tr>
			<tr>
				<td align="left" class="tabletdcssA">
					<div align="right"><font color=red>*</font>���ʹ���Ӧ�õ�ַ��</div>
			    </td>
				<td class="tabletdcssB">
							<c:forEach var="item" items="${sessionScope.application_base_urls}">
							${item}<br>
							</c:forEach>
				</td>
			</tr>
			<tr>
				<td align="left" class="tabletdcssA">
					<div align="right"><font color=red>*</font>���ʹ���Ӧ��SESSIONID��</div>
			    </td>
				<td class="tabletdcssB">
							<%
							String[] keys=SessionHouse.getKey();
							for(int i=0;i<keys.length;i++){
							   out.println(keys[i]+",");
							}
							 %>
				</td>
			</tr>
			<tr>
				<td align="left" class="tabletdcssA">
					<div align="right"><font color=red>*</font>cookeie</div>
			    </td>
				<td class="tabletdcssB" id="cookieId">
							
				</td>
			</tr>
	</table>
	<script type="text/javascript">
	  cookieId.innerHTML=(document.cookie);
	</script>
	</body>
</html>
