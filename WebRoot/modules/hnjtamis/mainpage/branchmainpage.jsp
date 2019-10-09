<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
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
	<script type="text/javascript" src="${basePath}fusioncharts/js/fusioncharts.js"></script>
	<script type="text/javascript" src="${basePath}fusioncharts/js/themes/fusioncharts.theme.zune.js"></script>
  <script type="text/javascript">
  <!--
	  
     /// Ext.onReady(Read4);
     FusionCharts.ready(function(){
		var revenueChart = new FusionCharts({
			type: "pie3d",
			renderAt: "chartContainer",
			width: "100%",
			height: "150",
			dataFormat: "json",
			containerBackgroundColor:"#ffffff",
			dataSource: {
					"data": [
					  	{
				            "label": "无数据",
				            "value": "100"
				        }
					]
				}
				
			});
			
			/////动态添加数据
			///revenueChart.dataSource.data.push({"label":"测试","value":"26570"});
			var chart_={"caption": "",
				        "startingangle": "120",
				        "showlabels": "0",
				        "showlegend": "1",
				        "enablemultislicing": "0",
				        "slicingdistance": "15",
				        "showpercentvalues": "1",
				        "showpercentintooltip": "0",
				        "pieRadius":"70",
				        "bgColor": "#ffffff",
				        "canvasBgAlpha":"0",
				        "bgAlpha":"0",
				        "plottooltext": " $label :  $value",
				        "theme": "fint"};
			var data_ =[];	//// 动态添加数据
			
			Ext.Ajax.request({
				method : 'POST',
				url : 'mainpage/branch/coveragelistForBranchMainPageListAction!coveragelist.action',
				success : function(response) {
					var result = Ext.decode(response.responseText);
					///alert(result);
					if(result.success==true){
						////alert(response.responseText); ///// 动态添加图标数据集
						if(typeof result.tranplanratiolist!='undefined' && result.tranplanratiolist!=''){
							var totalnums=result.tranplanratiolist[0].totalnums;// 机构总人数
							var factnums=result.tranplanratiolist[0].factnums; /// 参与学习人数
							///alert(totalnums+"___"+factnums);
							data_.push({
								"label":"参与学习人数",
								"value":factnums
							});
							data_.push({
								"label":"未参与学习人数",
								"value":(totalnums-factnums)
							});
							//alert(data_.length);
							
							revenueChart.setChartData({
								"chart":chart_,
								"data":data_
							},"json");
							
							
							
						}
						
					}else{
					   Ext.Msg.alert('错误提示', result[0].errors);
					}
					revenueChart.render("chartContainer");
				},
				failure : function(response) {
					revenueChart.render("chartContainer");
					var result = Ext.decode(response.responseText);
					if (result && result.length > 0)
						Ext.Msg.alert('错误提示', result[0].errors);
					else
						Ext.Msg.alert('信息', '后台未响应，网络异常！');
				},
				params : "id="
			});
			///revenueChart.SetTransparentChart(false);
			///revenueChart.render("chartContainer");
		});  
  //-->
  </script>
  <style type="text/css">
	body,td,th { font-size: 12px; color: #3f3f3f; font-family: "宋体"; margin:14px;}
	a:link, a:visited { text-decoration: none; color: #3f3f3f;}
	a:hover, a:active { text-decoration: none; color: #007af4;}
	img{ border:none }

	.Left_zi { line-height: 23px; padding-left: 12px; padding-top: 6px;}
	.Right_zi { line-height: 23px; padding-left: 12px; padding-right: 12px;}
	.button_01 { color: #FFF; background: url(${basePath}resources/images/btn_01.jpg) no-repeat; height: 21px; width: 70px; border: none; margin-right: 7px; cursor: pointer;}

	.title_zi{	font-size: 13px; font-weight: bold; color: #FFF; text-align:center; width:150px; height:29px; line-height:29px;}
	.list_lan { font-size: 12px; font-weight: bold; color: #005aa7;}
	.zt_study { color: #e63701;}
	.zt_finish { color: #09F;}
	.score_zi { background: url(${basePath}resources/images/dot.png) no-repeat; text-indent: 14px; background-position: left center; border-bottom: 1px dashed #9e9e9e;}
	.score_zi2 { border-bottom: 1px dashed #9e9e9e;}
	
	.zt_check { color: #046cd3; cursor: pointer;}
	.zt_check a:link, .zt_check a:visited{ color: #046cd3; text-decoration: none;}
	.zt_check a:hover, .zt_check a:active{ color: #046cd3; text-decoration: none;}
  </style>
  	
  
</head>
<body bgcolor="#f0f0f0">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="17" height="431" rowspan="3" valign="bottom" background="${basePath}resources/images/bg9_01.png"><img src="${basePath}resources/images/bg9_01_2.png" /></td>
     <!--培训覆盖率-->
    <td width="50%" height="186" valign="top" style="background:url(${basePath}resources/images/bg9_04.png) no-repeat"><table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td height="29" align="left" valign="top"><div  class="title_zi">培训覆盖率</div></td>
        </tr>
     <!--培训覆盖率 饼图-->  
      <tr>
        <td height="150" align="center">
        	<div id="chartContainer">FusionCharts XT will load here!</div>
        </td>
      </tr>
    </table>
    </td>
    
    <td width="51" rowspan="3" valign="bottom" background="${basePath}resources/images/bg9_02.png"><img src="${basePath}resources/images/bg9_02_2.png" /></td>
    <td rowspan="3" width="50%" valign="top" bgcolor="#FFFFFF" style="background-position: bottom; background-repeat: repeat-x; background-image: url(${basePath}resources/images/bg_btm01.png);">
    <!--本机构部门培训达标情况-->
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
        <td height="29" colspan="4" align="left" valign="top" style="background:url(${basePath}resources/images/title_03.png) no-repeat"><div  class="title_zi">基层单位达标情况</div></td>
        </tr>
        <tr>
        <td height="13" colspan="4"> </td>
        </tr>
       
      
      <!--基层单位达标情况 列表-->
      <s:set var="radomcolor" value="{'e5eb69','8de7e5','efba8e','b1de69','2fa6de','eda613','c0baf8','ae91b9','beba85'}"></s:set>
      <c:if test="${empty organprogresslist}">
      	<c:forEach var="item" begin="1" end="6">
      		<c:set var="varBgColor" value="e8e8e8"></c:set><%/**tr背景色，根据奇偶变化**/ %>
      		<c:if test="${item%2 eq 0}"><c:set var="varBgColor" value="f7f7f7"></c:set></c:if>	
      		<tr bgcolor="#${varBgColor}">
      			<td width="5%" height="39" align="center"></td>	
	      		<td width="40%" align="center">无数据!</td>
	      		<td align="center"></td>
	        	<td width="8%" align="center"></td>
      		</tr>
      		<tr>
	          	<td height="2" colspan="4" align="center"> </td>
	      	</tr>
      	</c:forEach>
      </c:if>
      <c:forEach var="item" items="${organprogresslist}" varStatus="st">
      	<%  java.util.Random r = new java.util.Random();
			request.setAttribute("randomVal",r.nextInt(8));  %>
		<c:set var="varBgColor" value="e8e8e8"></c:set><%/**tr背景色，根据奇偶变化**/ %>
      	<c:if test="${st.count%2 eq 0}"><c:set var="varBgColor" value="f7f7f7"></c:set></c:if>
      	<tr bgcolor="#${varBgColor}">
      		<td width="5%" height="39" align="center" style="padding: 2px;">${st.count}</td>	<%/**tr序号**/ %>
      		<td width="40%" align="center" style="padding: 2px;">${item.organname}</td><%/**机构名称**/ %>
      		<td align="center"><%/**计算百分比颜色**/ %>
      			<c:set var="varProgress" value="${item.reachratio*100}"></c:set>
      			<div style="width:90%; height:14px; padding:2px 2px 0px 2px; border:1px solid #bbbbbb; background-color:#FFF; text-align:left;">
	                <div style="width:${varProgress}%; height:12px; background-color:#${radomcolor[randomVal]};"></div>
	            </div>
      		</td><%/**比例**/ %>
      		<td width="8%" align="center" style="padding: 2px;padding-left: 10px;padding-right: 10px;">${item.reachratio*100}%</td><%/**达标比例**/ %>
      	</tr>
      	
      	<tr>
          	<td height="2" colspan="4" align="center"> </td>
      	</tr>
      </c:forEach>
      <c:if test="${fn:length(organprogresslist)<6}">
	      	<c:forEach var="item" begin="1" end="3">
	      		<c:set var="varBgColor" value="e8e8e8"></c:set><%/**tr背景色，根据奇偶变化**/ %>
	      		<c:if test="${item%2 eq 0}"><c:set var="varBgColor" value="f7f7f7"></c:set></c:if>	
	      		<tr bgcolor="#${varBgColor}">
	      			<td height="39" align="center"></td>	
		      		<td align="center"></td>
		      		<td align="center"></td>
		        	<td align="center"></td>
	      		</tr>
	      		<tr>
		          	<td height="2" colspan="4" align="center"> </td>
		      	</tr>
	      	</c:forEach>
	    </c:if>
    </table>
    <br/>
    </td>
    <td width="17" rowspan="3" valign="bottom" background="${basePath}resources/images/bg9_03.png"><img src="${basePath}resources/images/bg9_03_2.png"  /></td>
  </tr>
  <tr>
    <td height="12" valign="top"></td>
  </tr>
  <tr>
    <td valign="top" bgcolor="#FFFFFF" style="background-position: bottom; background-repeat: repeat-x; background-image: url(${basePath}resources/images/bg_btm01.png);">
    <!--基层单位达标情况-->
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td height="29" colspan="7" align="left" valign="top" style="background:url(${basePath}resources/images/title_03.png) no-repeat"><div  class="title_zi">本部培训达标情况</div></td>
      </tr>
      <tr>
        <td height="12" colspan="7"></td>
      </tr>
       <!--本机构部门培训达标情况-->
        <tr style="background:url(${basePath}resources/images/list_lan.png) repeat-x">
            <td width="100" align="center">部门列表</td>
        <td width="2"><img src="${basePath}resources/images/list_icon.png" /></td>
            <td width="80" align="center">学习人数</td>
        <td width="2"><img src="${basePath}resources/images/list_icon.png" /></td>
            <td width="80" align="center">达标人数</td>
        <td width="2"><img src="${basePath}resources/images/list_icon.png" /></td>
            <td width="113" align="center">达标率</td>
      </tr>
      <c:if test="${empty tranplanlist}">
      	<tr bgcolor="#fcfcfc">
      		<td height="30" align="center">无记录</td>
      		<td align="center"> </td>
      		<td align="center"> </td>
      		<td align="center"> </td>
      		<td align="center"> </td>
      		<td align="center"> </td>
      		<td align="center"> </td>
      	</tr>	
      </c:if>
      <c:forEach var="itemtp" items="${tranplanlist}" varStatus="st">
      	<c:set var="varBgColor" value="fcfcfc"></c:set><%/**tr背景色，根据奇偶变化**/ %>
      	<c:if test="${st.count%2 eq 0}"><c:set var="varBgColor" value="eef2f5"></c:set></c:if>
      	<tr bgcolor="#${varBgColor}">
      		<td height="30" align="center">${itemtp.deptname}</td>	<%/**部门名称**/ %>
      		<td align="center"> </td>
      		<td align="center">${itemtp.factnums}</td><%/**实际学习人数**/ %>
      		<td align="center"> </td>
      		<td align="center">${itemtp.completenums}</td><%/**达标人数**/ %>
      		<td align="center"> </td>
      		<td align="center">${itemtp.completeratio}%</td><%/**达标率**/ %>
      	</tr>		
      </c:forEach>
    </table>
    <br/>
    </td>
  </tr>
</table>
</body>
</html>
