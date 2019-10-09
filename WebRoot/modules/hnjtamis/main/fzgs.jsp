<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%--
@version: 1.0 分子公司首页展示
@author: wangyong
@time: 2015.05.07
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
<head>
<title>分子公司首页信息</title>
<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=utf-8">
<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache">
<META HTTP-EQUIV="Expires" CONTENT="0">
<script type="text/javascript" src="${basePath}base/ext/ext-all.js"></script>
<link href="${basePath}modules/hnjtamis/main/css/main.css" rel="stylesheet" type="text/css" />
<script type="text/javascript">
function openDcDbWin(organName,organId){
	var url = "${basePath}/mainpage/basic/listForBasicMainPageListAction!list.action?organIdTerm="+organId;
	window.parent.grablEapLogin.createWindow("dbqk"+organId,organName+"达标情况","",url);
}
</script>
</head>
<body>
<table width="100%" border="0" cellspacing="0" cellpadding="0" >
  <tr>
    <td width="80%" valign="top"><table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td width="49%" align="center" valign="top">
<!--本部达标情况--><table width="94%" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td colspan="4" class="tbl_lan">本部达标情况</td>
            </tr>
          <tr>
            <td class="list_lan">部门列表</td>
            <td class="list_lan">总人数</td>
            <td class="list_lan">学习人数</td>
            <td class="list_lan">达标人数</td>
            <td class="list_lan">达标率</td>
          </tr>
          <c:if test="${tranplanlist== null || fn:length(tranplanlist) == 0}">
	     	 <tr>
             	<td colspan="4" class="list_gray">没有找到对应的数据！</td>
             </tr>
          </c:if>
          <c:if test="${tranplanlist ne null && fn:length(tranplanlist) > 0}">
	          <c:forEach var="itemtp" items="${tranplanlist}" varStatus="state">
		         <c:choose>
		          <c:when test="${state.index%2 eq 0 }">
			          <tr>
			            <td class="list_gray">${itemtp.deptname}</td>
			            <td class="list_gray">${itemtp.plannums}</td>
			            <td class="list_gray">${itemtp.factnums}</td>
			            <td class="list_gray">${itemtp.completenums}</td>
			            <td class="list_gray red">${itemtp.completeratio}%</td>
			          </tr>
		          </c:when>
		          <c:otherwise>
			          <tr>
			            <td class="list_white">${itemtp.deptname}</td>
			            <td class="list_white">${itemtp.plannums}</td>
			            <td class="list_white">${itemtp.factnums}</td>
			            <td class="list_white">${itemtp.completenums}</td>
			            <td class="list_white red">${itemtp.completeratio}%</td>
			          </tr>
		          </c:otherwise>
		         </c:choose>
	          </c:forEach>
          </c:if>
        </table></td>
        <td width="1%" bgcolor="#e8e8e8">&nbsp;</td>
        <td width="50%" align="center" valign="top">
<!--基层单位达标情况--><table width="94%" border="0" cellspacing="0" cellpadding="0">
          <tr><td colspan="6" class="tbl_lan">基层单位达标情况</td></tr>
          <c:forEach var="item" items="${organprogresslist}" varStatus="state">
              <c:set var="varProgress" value="${item.reachratio*100}"></c:set>
              <c:set var="v_indent" value="13px"></c:set>
              <c:if test="${state.index > 8}"><c:set var="v_indent" value="9px"></c:set></c:if>
	        <tr><td width="9%" align="left" class="list_ranking" style="text-indent:${v_indent};">${state.index+1}</td>
	            <td width="35%" align="left" class="list_ranking_bg">${item.organname}</td>
	            <td width="30%" class="list_ranking_bg"><!--进度条--><div class="white"><div class="blue" style="width: ${varProgress}%;"></div></div></td>
	            <td width="10%" class="list_ranking_bg">${varProgress}%</td>
				<td width="14%" align="center" class="list_ranking_bg"><!--详情 按钮--><img src="${basePath}modules/hnjtamis/main/images/details.jpg" onclick="openDcDbWin('${item.organname}','${item.organid}')" /></td>
	            <td width="2%" align="right" class="list_ranking_bg"><img src="${basePath}modules/hnjtamis/main/images/ranking_bg02.jpg" /></td></tr>
          </c:forEach>
<!--基层单位达标情况 结束--></table></td>
      </tr>
    </table> </td>
  </tr>
</table>
</body>
</html>