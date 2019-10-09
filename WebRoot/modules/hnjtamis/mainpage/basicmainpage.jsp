<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%--
@version: 1.0 基层企业首页展示
@author: wangyong
@time: 2015.05.07
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
%>
<head>
	<title>基层企业首页信息</title>
	<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=utf-8">
	<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
	<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache">
	<META HTTP-EQUIV="Expires" CONTENT="0">
	<link rel="stylesheet" type="text/css" href="${basePath}resources/css/ext-all.css" />
	<script type="text/javascript" src="${basePath}base/ext/ext-all.js"></script>
  <script type="text/javascript">
  <!--
	  
     	Ext.require([
		    'Ext.grid.*',
		    'Ext.data.*',
		    'Ext.panel.*',
		    'Ext.layout.container.Border'
		]);
		
		Ext.onReady(function(){
		    // create the Data Store
		    var store = Ext.create('Ext.data.Store', {
		    	fields:[
		    		{name: 'deptname' },
	                {name: 'datetime' },
	                {name: 'statusstr'}	
		    	],
		        proxy: {
		            // load using HTTP
		            type: 'ajax',
		            actionMethods : "POST",
		            url: 'mainpage/basic/monthplanlistForBasicMainPageListAction!monthplanlist.action',
		            // the return will be XML, so lets set up a reader
		            reader: {
		                type: 'json',
		                root: 'monthplanlist',
		                totalProperty  : 'total'
		            }
		        }
		    });
		
		    // create the grid
		    var grid = Ext.create('Ext.grid.Panel', {
		    	id : 'gridplanlist',
		        bufferedRenderer: false,
		        store: store,
		        columns: [
		            {text: "部门名称", width: 130, dataIndex: 'deptname', sortable: true},
		            {text: "月份", flex: 1, dataIndex: 'datetime', sortable: true},
		            {text: "状态", width: 90, dataIndex: 'statusstr', sortable: true}
		        ],
		        forceFit: true,
		        height:210,
		        split: true,
		        region: 'north'
		    });
		    
		    Ext.create('Ext.Panel', {
		    	margins : '22 22 22 22', // 为了不要与容器的边框重叠，设定2px的间距
		        renderTo: 'month-train-plan',
		        ///frame: true,
		        //title: '月度计划完成情况',
		        width: '100%',
		        items: [
		            grid]
		    });
		    
		    // update panel body on selection change
		    grid.getSelectionModel().on('selectionchange', function(sm, selectedRecord) {
		        if (selectedRecord.length) {
		            ///var detailPanel = Ext.getCmp('detailPanel');
		            
		        }
		    });
		
		    store.load(); 
		});
      	
      	
      	function chgmonth(obj){
      		var ds=Ext.getCmp('gridplanlist').store;
      		ds.load({
	  	  			params:{
	  	  				monthTerm : obj.value
	  	  			}
	  	  		});
	  	  	////alert(obj.value);
      	}
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
<body bgcolor="#f0f0f0" style="margin:14px;">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="17" height="431" valign="bottom" background="${basePath}resources/images/bg8_01.png"><img src="${basePath}resources/images/bg8_01_2.png" /></td>
    <td width="65%" valign="top" bgcolor="#FFFFFF" style="background:#ffffff url(${basePath}resources/images/bg_btm01.png) repeat-x bottom">
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
      <!--本单位学习计划-->
      <tr>
        <td height="29" colspan="3" align="left" valign="top" style="background:url(${basePath}resources/images/title_03.png) no-repeat"><div  class="title_zi">本单位学习计划</div></td>
      </tr>
      <tr>
        <td height="17" colspan="3"> </td>
      </tr>
      <!--本单位学习计划 内容-->
      <tr bgcolor="#e8e8e8">
        <td width="45" height="40"> </td>
        <td colspan="2" align="left">本单位学习计划总数：${plantotalnums}人，达标数：${completetotalnums}人</td>
      </tr>
      <tr>
        <td height="2" colspan="3"> </td>
      </tr>
      <tr bgcolor="#f7f7f7">
        <td height="40"> </td>
        <td width="307" align="left">
        	<fmt:formatNumber var="fmtcompleteratio" value="${plantotalnums==0?0:completetotalnums*100/plantotalnums}" pattern="##.##" minFractionDigits="2" ></fmt:formatNumber> 
        	<c:set var="completeratio" value="${fmtcompleteratio}"></c:set>
            <div style="width:200px; height:14px; padding:2px 2px 0px 2px; border:1px solid #bbbbbb; background-color:#FFF; text-align:left;">
                <div style="width:${completeratio}%; height:12px; background-color:#9dc8fd;"></div>
            </div>
        </td>
        <td width="398" align="left">已完成${completetotalnums}人，总共${plantotalnums}人，计划占比:${completeratio}%</td>
      </tr>
      <tr>
        <td height="20" colspan="3"> </td>
      </tr>
    </table>
        <!--部门学习情况-->
    <s:set var="radomcolor" value="{'e5eb69','8de7e5','efba8e','b1de69','2fa6de','eda613','c0baf8','ae91b9','beba85'}"></s:set>
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr>
        <td height="29" colspan="11" align="left" valign="top" style="background:url(${basePath}resources/images/title_02.jpg) no-repeat"><div  class="title_zi">部门学习情况</div></td>
          </tr>
          <tr>
            <td height="13" colspan="7" valign="top"> </td>
          </tr>
          
          <!--部门学习情况 列表-->
          <tr style="background:url(${basePath}resources/images/list_lan02.png) repeat-x">
            <td width="85" height="39" align="center">部门列表</td>
            <td width="2"><img src="${basePath}resources/images/list_icon02.png"/></td>
            <td width="235" align="center">已学习人数/总学习人数</td>
            <td width="2"><img src="${basePath}resources/images/list_icon02.png"/></td>
            <td width="60" align="center">达标率</td>
            <td width="2"><img src="${basePath}resources/images/list_icon02.png"/></td>
            <td width="59" align="center">计划率</td>
          </tr>
          <tr>
            <td height="1" colspan="7" valign="top"> </td>
            </tr>
      <c:if test="${empty tranplanlist}">
      	<c:forEach var="item" begin="1" end="6">
      		<c:set var="varFirstStr" value=""></c:set><%/**第1行内容**/ %>
      		<c:set var="varBgColor" value="e8e8e8"></c:set><%/**tr背景色，根据奇偶变化**/ %>
      		<c:if test="${item%2 eq 0}"><c:set var="varBgColor" value="f7f7f7"></c:set></c:if>	
      		<c:if test="${item eq 1}"><c:set var="varFirstStr" value="无记录"></c:set></c:if>	
      		<tr bgcolor="#${varBgColor}">
      			<td height="45" align="center">${varFirstStr}</td>
      			<td> </td><td> </td><td> </td><td> </td><td> </td><td> </td>
      		</tr>
      	</c:forEach>
      </c:if>
      <c:forEach var="itemtp" items="${tranplanlist}" varStatus="st">
      	<%
			      			java.util.Random r = new java.util.Random();
			      			request.setAttribute("randomVal",r.nextInt(8));  %>
      	<c:set var="varBgColor" value="e8e8e8"></c:set><%/**tr背景色，根据奇偶变化**/ %>
      	<c:if test="${st.count%2 eq 0}"><c:set var="varBgColor" value="f7f7f7"></c:set></c:if>
      	<c:set var="varProgress" value="${itemtp.planratio}"></c:set>
      	<tr bgcolor="#${varBgColor}">
      		<td height="45" align="center">${itemtp.deptname}</td>	<%/**部门名称**/ %>
      		<td> </td>
      		<td align="center">${itemtp.factnums}/${itemtp.plannums}</br><%/**已学习（实际参加学习）人数 、 总学习人数(计划参加学习人数)**/ %>
      		<div style="width:200px; height:14px; padding:2px 2px 0px 2px; border:1px solid #bbbbbb; background-color:#FFF; text-align:left;">
                <div style="width:${varProgress}%; height:12px; background-color:#${radomcolor[randomVal]};"></div>
            </div>
            </td>
            <td> </td>
            <td align="center">${itemtp.completeratio}%</td><%/**达标比例**/ %>
      		<td> </td>
            <td align="center">${varProgress}%</td><%/**学习人数比例**/ %>
      	</tr>
      </c:forEach>
      
  </table>
          <br/>
    </td>
    <td width="51" valign="bottom" background="${basePath}resources/images/bg8_02.png"><img src="${basePath}resources/images/bg8_02_2.png"></td>
    
    <td width="35%" valign="top" bgcolor="#FFFFFF" style="background:#ffffff url(${basePath}resources/images/bg_btm01.png) repeat-x bottom">
    <!--岗位达标率-->
    <table width="100%" border="0" cellspacing="0" cellpadding="0" style="background:url(${basePath}resources/images/bg8_04.png) repeat-x">
      <tr>
        <td height="12" colspan="2"> </td>
        </tr>
      <tr>
        <td width="30%" height="30" class="score_zi">岗位达标率</td>
        <td width="70%" class="score_zi2">${organprogress.reachratio}%</td>
      </tr>
      <tr>
        <td height="30" class="score_zi">最新达标人</td>
        <td class="score_zi2"><span class="zt_study">${trainperson.employeename} ${trainperson.deptname} ${trainperson.subject}</span></td>
      </tr>
      <tr>
        <td height="22" colspan="2">&nbsp;</td>
        </tr>
    </table>
    <!--月度计划/完成情况-->
    <table width="100%" height="44" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td width="60%" height="29"  align="left" valign="top" style="background:url(${basePath}resources/images/title_03.png) no-repeat"><div class="title_zi">月度计划/完成情况</div></td>
        <td width="40%" align="right" valign="bottom" style="background:url(${basePath}resources/images/bg_top01.png) repeat-x">
       		<!-- 选择月份 -->
       		<select onchange="chgmonth(this)">
       			<option value="">全年</option>	
       			<c:forEach var="mitem" begin="1" end="12">
       				<c:choose>
       					<c:when test="${mitem<10}">
       						<option value="0${mitem}">0${mitem}</option>	
       					</c:when>
       					<c:otherwise><option value="${mitem}">${mitem}</option>	
       					</c:otherwise>
       				</c:choose>
       			</c:forEach>
       			
       		</select>
       		
       </td>
      </tr>
      <tr>
        <td height="15" colspan="2" valign="top">&nbsp;</td>
        </tr>
    </table>
      <!--月度计划/完成情况 列表-->
      <div id="month-train-plan"></div>
    
    <br/>
    </td>
    <td width="17" valign="bottom" background="${basePath}resources/images/bg8_03.png"><img src="${basePath}resources/images/bg8_03_2.png" /></td>
  </tr>
</table>
</body>
</html>
