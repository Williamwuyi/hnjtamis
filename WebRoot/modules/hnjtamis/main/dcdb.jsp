<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%--
@version: 1.0 电厂首页展示
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
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>培训系统-电厂达标情况</title>
<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=utf-8">
<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache">
<META HTTP-EQUIV="Expires" CONTENT="0">
<link href="${basePath}resources/css/ext-all.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${basePath}base/ext/ext-all.js"></script>
<link href="${basePath}modules/hnjtamis/main/css/main.css" rel="stylesheet" type="text/css" />
<c:set var="initDeptId" scope="page" value=""></c:set>
<c:set var="initDeptName" scope="page" value="无"></c:set>
</head>
<body id="puser" onload="initFun()">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td colspan="2" class="top_lan">
<!--顶部查询栏--><table width="730" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td width="20" align="center">&nbsp;</td>
        <td width="190" align="left" id="startDcDiv"></td>
        <td width="1" align="center">&nbsp;</td>
        <td width="190" align="left" id="endDcDiv"></td>
        <td width="85"  align="left" ><input name="queryBt" type="button" class="btn_cx" id="queryBt" value="查 询" onclick="queryInTime()" /></td>
        <td width="165"  align="left" ><input name="excelBt" type="button" class="btn_dc" id="excelBt" value="导出excel" onclick="toUserExamExcel()" /></td>
      </tr>
    </table></td>
  </tr>
  <tr>
<!--机构部门岗位--><td width="25%" rowspan="2" valign="top" bgcolor="#e6e6e6" ><table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td align="center" class="tree_topzi" ><img src="${basePath}modules/hnjtamis/main/images/icon_04.png" align="absmiddle" />&nbsp;机构部门岗位</td>
      </tr>
      <c:if test="${organDeptlist== null || fn:length(organDeptlist) == 0}">
     	 <tr>
		     <td class="tree_zi2">暂无部门信息</td>
		 </tr>
      </c:if>
      <c:if test="${organDeptlist ne null && fn:length(organDeptlist) > 0}">
        <c:forEach items="${organDeptlist}" var="item" varStatus="state">
	      <c:choose>
		      <c:when test="${item.type eq 'organ' }">
		      	<%-- tr>
			        <td class="tree_zi2" id="od_${item.id}" onclick="setDeptTable('${item.id}','${item.title}')">${item.title}</td>
			     </tr> --%>
		      </c:when>
		      <c:otherwise>
				  <tr>
					<c:if test="${initDeptId eq null || initDeptId eq ''}">
							<c:set var="initDeptId" scope="page" value="${item.id}"></c:set>
							<c:set var="initDeptName" scope="page" value="${item.title}"></c:set>
					</c:if>
					<%-- onmouseover="this.className='tree_zi2_1'" 
			        	onmouseout="this.className='tree_zi2'" --%>
			        <td class="tree_zi2"  id="od_${item.id}" onclick="setDeptTable('${item.id}','${item.title}')">${item.title}</td>
			      </tr>
		      </c:otherwise>
	      </c:choose>
	    </c:forEach>
	   </c:if>
    </table></td>
    <td width="75%" valign="top" style="height:10px;">
    <!--部门达标情况--><table width="100%" border="0" cellspacing="0" cellpadding="0" style="padding:20px;">
      <thead>
      <tr>
        <td colspan="4" class="tbl_lan">部门达标情况 - <label id="deptTitle">${initDeptName}</label></td>
        <td colspan="3" align="right"><table width="245" border="0" cellspacing="5" cellpadding="0">
          <tr>
            <td>&nbsp;</td>
          </tr>
        </table></td>
      </tr>
      <tr>
<!--部门达标情况-->
        <td width="10%" height="36" align="center" class="list_lan">序号</td>
        <td width="30%" align="center" class="list_lan">部门</td>
        <td width="15%" align="center" class="list_lan">总人数</td>
        <td width="15%" align="center" class="list_lan">已完成学习人数</td>
        <td width="15%" align="center" class="list_lan">在学习人数</td>
        <td width="15%" align="center" class="list_lan">达标人数</td>
        <td width="15%" align="center" class="list_lan">未达标人数</td>
      </tr>
      </thead>
      <tbody id="deptTable">
      <tr>
        <td align="center" class="list_blue">&nbsp;</td>
        <td align="center" class="list_blue">&nbsp;</td>
        <td align="center" class="list_blue">&nbsp;</td>
        <td align="center" class="list_blue">&nbsp;</td>
        <td align="center" class="list_blue">&nbsp;</td>
        <td align="center" class="list_blue">&nbsp;</td>
        <td align="center" class="list_blue">&nbsp;</td>
        </tr>
        </tbody>
    </table></td>
  </tr>
  <tr>
    <td valign="top">
<!--达标考试成绩--><table width="100%" border="0" cellspacing="0" cellpadding="0" style="padding:20px;">
     <thead>
      <tr>
        <td colspan="3" class="tbl_lan">达标考试成绩</td>
        <td colspan="4" align="right">
   <!--查询--><table width="515" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td width="90" align="right">姓名：</td>
            <td width="92"><input type="text" class="ipt2" name="userName" id="userName"/></td>
            <td width="78" align="right"><input name="queryBt2" type="button" class="btn_cx" id="queryBt2" value="查 询" onclick="setQueryType('',null)"/></td>
            <td width="10" align="right">&nbsp;</td>
            <td class="choice01" onclick="setQueryType('all',this)" id="qt1">全 部</td>
            <td class="choice02" onclick="setQueryType('hg',this)" id="qt2">合 格</td>
            <td class="choice02" onclick="setQueryType('unhg',this)" id="qt3">不合格</td>
          </tr>
<!--查询 结束--></table></td>
      </tr>
      
<!--达标考试成绩 查询--><tr>
        <td class="list_lan" width="12%">序号</td>
        <td class="list_lan" width="18%">人员</td>
        <td class="list_lan" width="15%">部门</td>
        <td class="list_lan" width="15%">岗位</td>
        <td class="list_lan" width="14%">是否合格</td>
        <td class="list_lan" width="13%">有效期(开始)</td>
        <td class="list_lan" width="13%">有效期(结束)</td>
      </tr>
      </thead>
      <tbody id="userTable">
      <tr>
        <td class="list_gray">&nbsp;</td>
        <td class="list_gray">&nbsp;</td>
        <td class="list_gray">&nbsp;</td>
        <td class="list_gray">&nbsp;</td>
        <td class="list_gray">&nbsp;</td>
        <td class="list_gray">&nbsp;</td>
        <td class="list_gray">&nbsp;</td>
      </tr>
        </tbody>
    </table></td>
  </tr>
</table>
<script type="text/javascript">
	var startDcDate = null;
	var endDcDate = null;

	function initFun(){
		startDcDate = new Ext.form.DateField({ 
			fieldLabel:'有效期(≥)', 
			emptyText:'请选择日期', 
			format:'Y-m-d', 
			labelWidth : 70,
			width:200,
			height:28,
			renderTo: 'startDcDiv'
		}); 
		endDcDate = new Ext.form.DateField({ 
			fieldLabel:'有效期(＜)', 
			emptyText:'请选择日期', 
			format:'Y-m-d', 
			labelWidth : 70,
			width:200,
			height:28,
			renderTo: 'endDcDiv'
		}); 
		setDeptTable('${initDeptId}','${initDeptName}');
	}

	var pageMask = new Ext.LoadMask(document.getElementById("puser"), {  
	    msg     : '数据正在处理,请稍候',  
	    removeMask  : true// 完成后移除  
	});
	String.prototype.trim = function()
	{
		return this.replace( /(^\s*)|(\s*$)/g, '' ) ;
	};
	function formatDate(value){
		if(value==undefined || value==null || value=='')return '';
		return Ext.Date.format(value,'Y-m-d')
	}
    var oldodId = null;//最后选择的部门的ID
    var queryType = 'all';//人员筛选，默认全部
    var oldQt = document.getElementById("qt1");//人员筛选，点击全部、不合格与合格，这个为最后一次点击的控件对象
	var _qid = '${initDeptId}';//查询的部门ID
    var _qname = '${initDeptName}';//查询的部门名
	var _examUserList = [];//查询出来的人员信息
	var _startDate = null;
	var _endDate = null;
	//按时间进行查询
	function queryInTime(){
		setDeptTable(_qid,_qname);
	}
	//Excel 导出
	function toUserExamExcel(){
		var startDate = formatDate(startDcDate.getValue());
		var endDate = formatDate(endDcDate.getValue());
		window.open("${basePath}mainpage/basic/expExcelForBasicMainPageExpExcelAction!expExcel.action?qid=" 
				+ _qid+ "&qtype=dept&qexamId=&qstartDay="+startDate+"&qendDay="+endDate);
	}
	//人员信息按条件进行查询
	function setQueryType(_queryType,thisobj){
		if(thisobj!=undefined && thisobj!=null){
			thisobj.className = "choice01";
			oldQt.className = "choice02";
			queryType = _queryType;
			oldQt = thisobj;
		}
		var userName = document.getElementById("userName").value.trim();
		var startDate = formatDate(startDcDate.getValue());
		var endDate = formatDate(endDcDate.getValue());
		if(startDate!=_startDate || endDate!=_endDate){//最后一次查询的时间不同了
			setUserExamTable(_qid,_qname,true)
		}else{
			pageMask.show();
			var userTable = document.getElementById("userTable");
			var trs = userTable.rows;
			if(trs && trs.length && trs.length>0){
				for(var i=trs.length-1;i>=0;i--){
					userTable.deleteRow(i);  
				}
			}
			//var htmlStr = "";
			for(var i=0,ii=0;i<_examUserList.length;i++){
				var rsvalue=_examUserList[i];
				if(userName!=undefined && userName!=null && userName!="" && rsvalue.userName.indexOf(userName)==-1){
					continue;
				}
				if(queryType == 'all' 
						|| (queryType == 'hg' && rsvalue.passState == 'T')
						|| (queryType == 'unhg' && rsvalue.passState != 'T')){
					var tdClassName = "list_gray";
					if(ii%2==1)tdClassName = "list_white";
					/* htmlStr+="<tr>";
					htmlStr+="<td class=\""+tdClassName+"\">"+(ii+1)+"</td>";
					htmlStr+="<td class=\""+tdClassName+"\">"+rsvalue.userName+"</td>";
					//htmlStr+="<td class=\""+tdClassName+"\">"+rsvalue.userOrganName+"</td>";
					htmlStr+="<td class=\""+tdClassName+"\">"+rsvalue.userDeptName+"</td>";
					htmlStr+="<td class=\""+tdClassName+"\">"+rsvalue.quarterName+"</td>";
					//htmlStr+="<td class=\""+tdClassName+"\">"+rsvalue.examName+"</td>";
					if(rsvalue.passState == 'T'){
						htmlStr+="<td class=\""+tdClassName+"\" title=\"考试："+rsvalue.examName+" 发布时间：["+rsvalue.publicTime+"]\"><font color=green>合格</font></td>";
					}else{
						htmlStr+="<td class=\""+tdClassName+"\" title=\"考试："+rsvalue.examName+" 发布时间：["+rsvalue.publicTime+"]\"><font color=red>不合格</font></td>";
					}
					
					htmlStr+="<td class=\""+tdClassName+"\">"+rsvalue.scoreStartTime+"</td>";
					htmlStr+="<td class=\""+tdClassName+"\">"+rsvalue.scoreEndTime+"</td>";
					//htmlStr+="<td class=\""+tdClassName+"\">"+rsvalue.publicTime+"</td>";
					htmlStr+="</tr>"; */
					
					
					var newRow = userTable.insertRow();
					newCell = newRow.insertCell();
					newCell.innerHTML=(ii+1);
					newCell.className=tdClassName;
					
					newCell = newRow.insertCell();
					newCell.innerHTML=rsvalue.userName;
					newCell.className=tdClassName;
					
					newCell = newRow.insertCell();
					newCell.innerHTML=rsvalue.userDeptName;
					newCell.className=tdClassName;
					
					newCell = newRow.insertCell();
					newCell.innerHTML=rsvalue.quarterName;
					newCell.className=tdClassName;
					
					newCell = newRow.insertCell();
					newCell.innerHTML=rsvalue.quarterName;
					newCell.className=tdClassName;
					
					
					if(rsvalue.passState == 'T'){
						newCell = newRow.insertCell();
						newCell.innerHTML="<font color=green>合格</font>";
						newCell.className=tdClassName;
						newCell.title="\"考试："+rsvalue.examName+" 发布时间：["+(rsvalue.publicTime!=undefined?rsvalue.publicTime:'无')+"]\"";
					}else{
						newCell = newRow.insertCell();
						newCell.innerHTML="<font color=red>不合格</font>";
						newCell.className=tdClassName;
						newCell.title="\"考试："+rsvalue.examName+" 发布时间：["+(rsvalue.publicTime!=undefined?rsvalue.publicTime:'无')+"]\"";
					}
					
					newCell = newRow.insertCell();
					newCell.innerHTML=rsvalue.scoreStartTime;
					newCell.className=tdClassName;
					
					newCell = newRow.insertCell();
					newCell.innerHTML=rsvalue.scoreEndTime;
					newCell.className=tdClassName;
					
					ii++;
				}
			}
			//.innerHTML = htmlStr;
			pageMask.hide();
		}
	}
	//根据部门查询
	//var oldqid = '';
	//var oldstartDate = '';
	//var oldendDate = '';
	function setDeptTable(qid,qname){
		//if(oldqid == qid && oldstartDate == startDate && oldendDate == endDate){
			//return;
		//}
		//oldqid = qid;
		//oldstartDate = startDate;
		//oldendDate = endDate;
		if(qid!=undefined && qid!=null && qid!=''){
		pageMask.show();
		var startDate = formatDate(startDcDate.getValue());
		var endDate = formatDate(endDcDate.getValue());
		_startDate = startDate;
		_endDate = endDate;	
		if(oldodId!=null){
			document.getElementById(oldodId).className = "tree_zi2";
		}
		document.getElementById("od_"+qid).className = "tree_zi1";
		oldodId = "od_"+qid;
		document.getElementById("deptTitle").innerHTML = qname;
		_qid = qid;
		_qname = qname;
		Ext.Ajax.request({
			timeout: 6000000,
			url : "${basePath}exam/exampaper/queryExamInTopDeptTjxxForExampaperListAction!queryExamInTopDeptTjxx.action?qid=" 
				+ qid+ "&qexamId=&qstartDay="+startDate+"&qendDay="+endDate,
			//async : false,//同步
			success: function(response) {
				setUserExamTable(qid,qname,false);
				var result = Ext.decode(response.responseText);
				//var htmlStr = "";
				var deptTable = document.getElementById("deptTable");
				var trs = deptTable.rows;
				if(trs && trs.length && trs.length>0){
					for(var i=trs.length-1;i>=0;i--){
						deptTable.deleteRow(i);  
					}
				}
	    		if(result && result.examUserList){
	    			var list = result.examUserList;
	    			for(var i=0;i<list.length;i++){
	    				var rsvalue=list[i];
	    				var tdClassName = "list_green";
	    				if(i%2==1)tdClassName = "list_blue";
	    				/* htmlStr+="<tr>";
	    				htmlStr+="<td align=\"center\" class=\""+tdClassName+"\">"+(i+1)+"</td>";
	    				htmlStr+="<td align=\"left\" class=\""+tdClassName+"\">"+rsvalue.showDeptName+"</td>";
	    				htmlStr+="<td align=\"center\" class=\""+tdClassName+"\">"+rsvalue.passStateCount+"</td>";
	    				htmlStr+="<td align=\"center\" class=\""+tdClassName+"\">"+rsvalue.passCount+"</td>";
	    				htmlStr+="<td align=\"center\" class=\""+tdClassName+"\">"+rsvalue.unPassCount+"</td>";
	    				htmlStr+="</tr>";	 */
	    				
	    				
	    				var newRow = deptTable.insertRow();
						newCell = newRow.insertCell();
						newCell.innerHTML=rsvalue.showDeptName!='合计'?(i+1):'&nbsp';
						newCell.className=tdClassName;
						newCell.style.textAlign = "center";
						
						
						newCell = newRow.insertCell();
						newCell.innerHTML=rsvalue.showDeptName;
						newCell.className=tdClassName;
						newCell.style.textAlign = "left";
						
						newCell = newRow.insertCell();
						newCell.innerHTML=rsvalue.passStateCount;
						newCell.className=tdClassName;
						newCell.style.textAlign = "center";
						
						newCell = newRow.insertCell();
						newCell.innerHTML=rsvalue.xxrsCount;
						newCell.className=tdClassName;
						newCell.style.textAlign = "center";
						
						newCell = newRow.insertCell();
						newCell.innerHTML=rsvalue.zzxxrsCount;
						newCell.className=tdClassName;
						newCell.style.textAlign = "center";
						
						newCell = newRow.insertCell();
						newCell.innerHTML=rsvalue.passCount;
						newCell.className=tdClassName;
						newCell.style.textAlign = "center";
						
						newCell = newRow.insertCell();
						newCell.innerHTML=rsvalue.unPassCount;
						newCell.className=tdClassName;
						newCell.style.textAlign = "center";
	    			}
	    			//document.getElementById("deptTable").innerHTML = htmlStr;
	    		}
	    		pageMask.hide();
			},
		    failure:function(form, action){
		    	pageMask.hide();
		    }
		});
		}
	}
	//查询考生信息
	function setUserExamTable(qid,qname,showMask){
		if(qid!=undefined && qid!=null && qid!=''){
		if(showMask){
			pageMask.show();
		}
		var startDate = formatDate(startDcDate.getValue());
		var endDate = formatDate(endDcDate.getValue());
		_startDate = startDate;
		_endDate = endDate;
		Ext.Ajax.request({
			timeout: 6000000,
			url : "${basePath}exam/exampaper/queryExamUserForExampaperListAction!queryExamUser.action?qid=" 
				+ (qid) + "&qtype=dept&qexamId=&qstartDay="+startDate+"&qendDay="+endDate,
			//async : false,//同步
			success: function(response) {
				var result = Ext.decode(response.responseText);
				var htmlStr = "";
	    		if(result && result.examUserList){
	    			_examUserList = result.examUserList;
	    			for(var i=0,ii=0;i<_examUserList.length;i++){
	    				var rsvalue=_examUserList[i];
	    				if(queryType == 'all' 
	    						|| (queryType == 'hg' && rsvalue.passState == 'T')
	    						|| (queryType == 'unhg' && rsvalue.passState != 'T')){
		    				var tdClassName = "list_gray";
		    				if(ii%2==1)tdClassName = "list_white";
		    				htmlStr+="<tr>";
		    				htmlStr+="<td class=\""+tdClassName+"\">"+(ii+1)+"</td>";
		    				htmlStr+="<td class=\""+tdClassName+"\">"+rsvalue.userName+"</td>";
		    				//htmlStr+="<td class=\""+tdClassName+"\">"+rsvalue.userOrganName+"</td>";
		    				htmlStr+="<td class=\""+tdClassName+"\">"+rsvalue.userDeptName+"</td>";
		    				htmlStr+="<td class=\""+tdClassName+"\">"+rsvalue.quarterName+"</td>";
		    				//htmlStr+="<td class=\""+tdClassName+"\">"+rsvalue.examName+"</td>";
		    				if(rsvalue.passState == 'T'){
		    					htmlStr+="<td class=\""+tdClassName+"\" title=\"考试："+rsvalue.examName+" 发布时间：["+(rsvalue.publicTime!=undefined?rsvalue.publicTime:'无')+"]\"><font color=green>合格</font></td>";
		    				}else{
		    					htmlStr+="<td class=\""+tdClassName+"\" title=\"考试："+rsvalue.examName+" 发布时间：["+(rsvalue.publicTime!=undefined?rsvalue.publicTime:'无')+"]\"><font color=red>不合格</font></td>";
		    				}
		    				
		    				htmlStr+="<td class=\""+tdClassName+"\">"+rsvalue.scoreStartTime+"</td>";
		    				htmlStr+="<td class=\""+tdClassName+"\">"+rsvalue.scoreEndTime+"</td>";
		    				//htmlStr+="<td class=\""+tdClassName+"\">"+rsvalue.publicTime+"</td>";
		    				htmlStr+="</tr>";
		    				ii++;
	    				}
	    			}
	    			document.getElementById("userTable").innerHTML = htmlStr;
	    		}
	    		if(showMask){
	    			pageMask.hide();
	    		}
			},
		    failure:function(form, action){
		    	if(showMask){
	    			pageMask.hide();
	    		}
		    }
		});
		}
	}
</script>
</body>
</html>
