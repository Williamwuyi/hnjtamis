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
<%@ taglib uri="/WEB-INF/tld/fmt.tld" prefix="fmt"%>
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
<title>培训系统-岗位标准</title>
<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache">
<META HTTP-EQUIV="Expires" CONTENT="0">
<link href="${basePath}resources/css/ext-all.css" rel="stylesheet" type="text/css" />
<c:set var="venter" value="\n" scope="request"/>
<style type="text/css">
body {
	font: 94%/1.2 "microsoft yahei";
	background-color: #eff5ff;
	margin: 0;
	padding: 0;
	color: #222222;
	/*border:1px solid #f00;*/
}
.ipt1 { border:1px solid #c2c3c6; height:18px; width:200px; background:url(../modules/hnjtamis/mainEx/images/ipt.gif) repeat-x;}
.btn1 { border:none; height:22px; width:63px; background:#ebebec url(../modules/hnjtamis/mainEx/images/ipt-btn.gif) 8px center no-repeat; text-indent:20px;}
.btnIn { text-align:center; width:100%; height:60px; line-height:15px; font-size:14px; color:#f2fbff; cursor:pointer;}
.tblIn { width:86%; margin:auto 0px; padding:0px;}
.tblIn caption { height:60px; line-height:60px; text-align:center; text-indent:24px; color:#555555; background-color:#f8f9fa;}
.tblIn th { font-size:15px; background-color:#edfaff; height:36px; border-bottom:1px solid #67a9dd; text-align:center; font-weight:normal; line-height:36px;}
.tblIn th span{ font-size:13px; color:#6F6F6F;padding-left:10px;}

.tblIn2 { width:86%; margin:auto 0px; padding:0px; border-top:1px solid #E0E0E0; border-right:1px solid #E0E0E0;}
.tblIn2 td { border-bottom:1px solid #E0E0E0; border-left:1px solid #E0E0E0; font-size:14px; font-weight:normal; line-height:20px;}
.tblIn2 td.td2 { background-color:#f7f8f9}
.tblIn2 td.td2 span{ display:block; font-size:13px; color:#888888;padding-left:14px;margin-top:4px;}
</style>
</head>
<script type="text/javascript">
	function query(){
		document.form1.submit();
	}
	function back(){
		window.location = '${basePath}mainPageEx/listForMainExListAction!list.action?qtype=${qtype}';
	}
</script>
<body>
<table width="1000" border="0" cellspacing="0" cellpadding="0" align="center">
  <tr>
    <td height="37" align="right">
    <!--第一行 签到-->
   		<div style="width:115;height:41px; margin-right:56px; background:url(../modules/hnjtamis/mainEx/images/qiandao_ok.png) right bottom no-repeat;">
        	<h4 style="float:right; text-align:center; width:62px; font-size:13px; color:#666666; margin:19px 0px 0px 0px;">${signIncount}</h4>
        </div>
    </td>
  </tr>
  <tr>
    <td height="35" align="center"><!--第二行 页面标题--><img src="../modules/hnjtamis/mainEx/images/title2.png"></td>
  </tr>
  <tr>
    <td height="97" valign="top" style="background:url(../modules/hnjtamis/mainEx/images/labelIN.png) left top no-repeat;">
   	<!--第三行 标题页签部分=-->
    <table width="93%" border="0" cellspacing="0" cellpadding="0" align="center">
      <tr>
        <td width="67%" height="72" align="left" valign="bottom">
        <form name="form1" method="get" action="${basePath}mainPageEx/stlistForMainExListAction!stlist.action">
        <h4 style="padding-left:120px;margin-top:49px; color:#666666;">子模块：
        <input type="text" name="standardnameTerm" id="standardnameTerm" class="ipt1" value="${standardnameTerm}"  />
        <input type="hidden" name="qtc" id="qtc"  value="${qtc}"  />
        <input type="hidden" name="qtype" id="qtype"  value="${qtype}"  />
        <input name="" type="button" class="btn1" value="查询" onclick="query()" />
        </h4>
        </form>
        </td>
        <td width="33%" valign="top"><div class="btnIn" onMouseOver="this.style.color='#ddf39a'" onMouseOut="this.style.color='#ffffff'" onClick="back()"><br><br><br>&nbsp;&nbsp;&nbsp;返回首页</div>
        </td>
      </tr>
    </table>
    </td>
  </tr>
  <tr>
    <td height="290" valign="top" align="center" style="background:url(../modules/hnjtamis/mainEx/images/bgCin.png) left top repeat-y;">
    <!--第四行 内容-->
        <table border="0" cellspacing="0" cellpadding="0" align="center" class="tblIn">
		  <caption>岗位标准条款--${qtcName}(${standardCount}项)</caption>
      	  <tr>
            <th width="5%">序号</th>
            <th width="13%">类别</th>
            <th width="13%">模块</th>
            <th width="13%">子模块</th>
            <th width="36%">详细内容</th>
            <th width="10%">参考学分</th>
            <th width="10%">有效期</th>
          </tr>
        </table><br>
        <table border="0" cellspacing="0" cellpadding="4" align="center" class="tblIn2">
          <c:if test="${standardTermslist ne null && fn:length(standardTermslist) > 0}">
          <c:forEach items="${standardTermslist}" var="item" varStatus="state">
	      <c:choose>
		      <c:when test="${state.index%2 eq 0 }">
		      <tr>
		        <td align="center" width="5%">${state.index+1}</td>
		        <td align="center" width="13%">${item.parentTypeName}</td>
		        <td align="center" width="13%">${item.typename}</td>
		        <td align="center" width="13%">${item.standardname}</td>
		        <td width="36%" style="padding-top:5px;padding-bottom:5px;">${fn:replace(item.contents,venter,'<br>')}</td>
		        <td align="center" width="10%">${item.refScore}</td>
		        <td align="center" width="10%">${item.efficient}</td>
		      </tr>
		      </c:when>
		      <c:otherwise>
		       <tr>
		        <td align="center" width="5%">${state.index+1}</td>
		        <td align="center" width="13%">${item.parentTypeName}</td>
		        <td align="center" width="13%">${item.typename}</td>
		        <td align="center" width="13%">${item.standardname}</td>
		        <td width="36%" style="padding-top:5px;padding-bottom:5px;">${fn:replace(item.contents,venter,'<br>')}</td>
		        <td align="center" width="10%">${item.refScore}</td>
		        <td align="center" width="10%">${item.efficient}</td>
		      </tr>
		      </c:otherwise>
	      </c:choose>
          </c:forEach>
          </c:if>
        </table>
    </td>
  </tr>
  <tr>
    <td height="112" style="background:url(../modules/hnjtamis/mainEx/images/bgBin.png) left top no-repeat;">&nbsp;</td>
  </tr>
</table>

</body>
</html>
