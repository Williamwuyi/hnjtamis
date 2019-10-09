<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html xmlns="http://www.w3.org/1999/xhtml">
<%
/*
 *@version: 1.0 首页
 *@author: wangyong
 *@time: 2015.05.12
 *@modify: 页面访问直接采用.jsp访问。页面中所有用foreach循环session中的列表已作废，全部采用ajax方式动态构建内容.如果需要回到action
 *         访问方式，注释掉除登录操作外其他AJAX的访问方法即可
 *@time: 
*/%>
<%@ taglib uri="/WEB-INF/tld/struts-tags.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/tld/fn.tld" prefix="fn"%>
<%
	/////request.setCharacterEncoding("GBK");
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	request.setAttribute("basePath",basePath);
%>
<head>
<TITLE>岗位培训管理系统</TITLE>
	<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=utf-8">
	<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
	<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache">
	<META HTTP-EQUIV="Expires" CONTENT="0">
	<link rel="stylesheet" type="text/css" href="${basePath}resources/css/ext-all.css" />
	<link rel="stylesheet" type="text/css" href="${basePath}resources/css/desktop.css" />
<style type="text/css">
	body, td, p, div, a, a:link, a:visited { font-family: "微软雅黑"; font-size:12px; color:#336699; text-decoration:none;}
	a:hover, a:active { font-family: "微软雅黑"; font-size:12px; color:#000000; text-decoration:none;}
	img {border:none;}
	
	
	
	.top { font-size:12px; color:#858fa2; margin:0px 12px 30px 0px;}
	.ipt01 { width:135px; height:19px; background:#4699e2 url(${basePath}resources/images/mainpage/ipt01.gif); border:none; text-indent:4px; color:#336699;}
	.ipt02 { width:125px; height:19px; background:#4699e2 url(${basePath}resources/images/mainpage/ipt02.gif); border:none; text-indent:4px; color:#336699;}
	.btn01 { width:40px; height:20px; background-image:url(${basePath}resources/images/mainpage/btn1.jpg); border:none; text-indent:1px; color:#fefeff; font-family: "微软雅黑"; font-size:12px; cursor:pointer;}
	.btn02 { width:65px; height:20px; background-image:url(${basePath}resources/images/mainpage/btn1-2.jpg); border:none; text-indent:1px; color:#fefeff; font-family: "微软雅黑"; font-size:12px; cursor:pointer;}
	.top td a, .top td a:link, .top td a:visited { color:#eff5fc; }
	.top td a:hover, .top td a:active { color:#cde8f6; text-decoration:underline;}
	
	.lan01 { margin:8px 0px 0px 1px;}
	.lan01 td { width:101px; text-align:left;}
	.lan01 img { width:77px; height:97px;}
	
	.lan03 { margin:4px 0px 0px 1px;}
	.lan03 td { height:24px; vertical-align:top; text-align:left;}
	.lan03 td a, a:link, a:visited { font-family: "微软雅黑"; font-size:12px; color:#336699; text-decoration:none;}
	.lan05 { margin:0px 0px 0px 36px;}
	.lan05 td { line-height:20px; vertical-align:top; text-align:left; text-indent:12px; width:170px; padding-bottom:8px;}
	
	.btm { width:600px; font-size:12px; color:#778296; line-height:20px; margin:12px 52px 18px 0px;}
	.handcss{
		cursor:pointer
	}
</style>
	
	<script type="text/javascript" src="${basePath}base/ext/ext-all.js"></script>
	<script type="text/javascript">
  	<!--
  	    var cp = null;
	  	function loadFun(){
	  		cp = new Ext.state.CookieProvider();
	  		Ext.state.Manager.setProvider(cp);
	  		if(cp){
	  			var aa = cp.get("account");
	  			if(aa && aa!=null){
	  				document.getElementById('txtaccount').value = cp.get("account");
	  			}
	  			document.getElementById('txtpwd').value = "";
	  			cp.set('password',null);
	  		}
	  	}
  		function login(lgt){
  			var account=document.getElementById('txtaccount').value;
				var password=document.getElementById('txtpwd').value;
				if(account==''){
					Ext.Msg.alert('信息', '请输入登录用户名');
					return;
				};
				//alert(password);
				if(password==''){
					Ext.Msg.alert('信息', '请输入登录密码');
					return;
				};
				///this.disabled='true';
				Ext.Ajax.request({
					method : 'POST',
					url : '${basePath}power/login/loginForLoginAction!login.action',
					success : function(response) {
						////alert(response.responseText);
						cp.set("account",account);
						cp.set('password',null);
	                   	var obj = Ext.decode(response.responseText);
	                   	if(obj.constructor==Array){
	                   		Ext.Msg.alert('信息', obj[0].errors);
	                   		return;
	                   	}
	                   	////var pe = obj.proxyEmployee;//多个被代理用户
	                   	/* var redirect = 'index.jsp?theme='+obj.userSession.theme; 
	                   	if(logintype == 'normal'){
	                   		 
	                   	}else if(logintype == 'fast'){
	                   		
	                   	}else{
	                   		
	                   	} */
	                   	var redirect = 'index.jsp?theme='+obj.userSession.theme+'&lgt='+lgt; 
	                    window.location = redirect;
	                    
					},
					failure : function(response) {
						var result = Ext.decode(response.responseText);
						if (result && result.length > 0)
							Ext.Msg.alert('错误提示', result[0].errors);
						else
							Ext.Msg.alert('信息', '后台未响应，网络异常！');
					},
					params : "account="+account+"&password="+password
				});
  		}
  		 
  		Ext.onReady(function(){
  			var button = Ext.get('btnqueryscore');//查询成绩
  			var btnlogin = Ext.get('btnlogin'); // 登录
  			var txtaccount=Ext.get('txtaccount');// 用户名
  			var txtpwd = Ext.get('txtpwd');//密码
  			loadFun();
  			
  			var win; // 成绩弹出窗口
  			
			
			////var data = [['1', 'male', 'name1', 'descn1'], ['2', 'female', 'name2', 'descn2'], ['3', 'male', 'name3', 'descn3'], ['4', 'female', 'name4', 'descn4'], ['5', 'male', 'name5', 'descn5']];
			
			var ds = Ext.create('Ext.data.JsonStore', {
	            fields: ['userName', {name:'fristScote',type:'float'},'inticket','idNumber','examName','creationDate'],
	            proxy : {
					type : 'ajax',
					actionMethods : "POST",
					url : '${basePath}onlineExam/listForOnlineListAction!list.action',// 后台请求地址
					reader : {
						type : 'json',
						root : 'list',
						totalProperty : "total"
					}
				}
			            
	        });
	       	
	        var gridPanel = Ext.create('Ext.grid.Panel', {
		        id: 'examscorelist',
		        flex: 4,
		        ///title:'最近考试成绩',
		        store: ds,
		        forceFit: true,
		        frame:true,
		        padding: '0 2 0 0',
		        width:'100%',
		        defaults: {
		            sortable: true
		        },//'userName', 'fristScote','inticket','idNumber','exam.examName','examStartTime'
		        columns: [
		            {
		                text: '姓名',
		                width: 30,
		                dataIndex: 'userName'
		            },
		            {
		                text: '考试科目',
		                dataIndex: 'examName'
		            },
		            {
		                text: '开考时间',
		                dataIndex: 'creationDate'
		            },
		            {
		                text: '得分',
		                dataIndex: 'fristScote'
		            }
		        ],
		
		        listeners: {
		            selectionchange: function(model, records) {
		                var fields;
		                if (records[0]) {
		                	//
		                }
		            }
		        }
		    });
			////scoreStore.load();
			button.on('click', function(){
				var inticket=document.getElementById('inticket').value;
				var idNumber=document.getElementById('idnumber').value;
				if(inticket=='' || inticket=='身份证号码'){
					Ext.Msg.alert('信息', '请输入身份证号码！');
					return false;
				};
				if(idNumber=='准考证号'){
					idNumber='';	
				}
				ds.load({
	  	  			params:{
	  	  				inticket : inticket,
	  	  				idNumber : idNumber
	  	  			}
	  	  		});
				if(!win){
					win = new Ext.Window({ 
		             //   el:'hello-win', 
		             	id: "scorequerywin",
		             	title: "成绩查询",
		             	layout:'fit', 
		                width:600, 
		                height:300, 
		                closeAction:'hide',//关闭窗口时渐渐缩小 
		                plain: true, 
		                modal: true, 
		                items:[gridPanel], 
		                buttons: [{ 
		                    text: '关闭', 
		                    iconCls : 'close',
		                    handler: function(){ 
		                        win.hide(); 
		                	}
		                }] 
			        }); 
			    }; 
			    win.show(this); 		
			});
			/// 登录事件
			//btnlogin.on('click',login);	
			// 
			
			
			///// 动态加载最新文库资料
			Ext.Ajax.request({
				method : 'POST',
				url : 'mainpage/queryShareDocumentLibForMainPageListAction!queryShareDocumentLib.action',
				success : function(response) {
					var result = Ext.decode(response.responseText);
					if(typeof result.documentlist !='undefined'){
						///alert(response.responseText); ///// 动态添加图标数据集
						if(typeof result.documentlist!='undefined' && result.documentlist!=''){
							delAlltabletr('tbcourseware');// 删除表格列
							var len=result.documentlist.length;
							if(len>5) len=5; // 最多只能显示5个
							for(var i=0;i<len;i++){
								//var imgurl='${basePath}mainpage/writepicForMainPageListAction!writepic.action?coursewareId='+result.coursewarelist[i].id;
								//var url='#';
								//var htm='<a href="'+url+'"><img src="'+imgurl+'"></a>'
								//var htm='<img src="'+imgurl+'">'
								//addtabletd('tbcourseware',htm,result.coursewarelist[i].title,'',0);
								var item = result.documentlist[i];
								var documentName = item.documentName;
								var url = "baseinfo/affiche/outputFileForAfficheFormAction!outputFile.action?id="+item.afficheId;
        	    				if((item.writerUser !=undefined && item.writerUser !=null && item.writerUser!='' && item.writerUser!='null') ||
        							(item.publishers !=undefined && item.publishers !=null && item.publishers!='' && item.publishers!='null') ||
        							(item.fxDay !=undefined && item.fxDay !=null && item.fxDay!='' && item.fxDay!='null')){
        	    				 documentName+="（";
				        		 if(item.writerUser !=undefined && item.writerUser !=null && item.writerUser!='' && item.writerUser!='null'){
				        			 documentName+=item.writerUser+" 主编，";
				        		 }
				        		 if(item.publishers !=undefined && item.publishers !=null && item.publishers!='' && item.publishers!='null'){
				        			 documentName+=item.publishers+"，";
				        		 }
				        		 if(item.fxDay !=undefined && item.fxDay !=null && item.fxDay!='' && item.fxDay!='null'){
				        		 	documentName+=item.fxDay;
								 }
								documentName+="）";
								}
        	    				documentName ="<a target='_new' href=\""+url+"\">"+documentName+"</a>";
								addtabletr('tbcourseware',documentName,url);
							}
						}
						
					}else{
					   Ext.Msg.alert('错误提示', result[0].errors);
					}
				},
				failure : function(response) {
					var result = Ext.decode(response.responseText);
					if (result && result.length > 0)
						Ext.Msg.alert('错误提示', result[0].errors);
					else
						Ext.Msg.alert('信息', '后台未响应，网络异常！');
				},
				params : "id="
			});
			
			///// 动态加载专家库信息
			Ext.Ajax.request({
				method : 'POST',
				url : 'mainpage/talentregForMainPageListAction!talentreg.action',
				success : function(response) {
					var result = Ext.decode(response.responseText);
					///alert(result);
					if(typeof result.talentreglist !='undefined'){
						///alert(response.responseText); ///// 动态添加图标数据集
						if(result.talentreglist!='' && result.talentreglist.length>0){
							delAlltabletd('tbtalentreg');// 删除表格列
							var len=result.talentreglist.length;
							if(len>3) len=3; // 最多只能显示3个
							for(var i=0;i<len;i++){
									////${item.name}，<br>${item.skill}
								var imgurl='${basePath}resources/images/mainpage/pic06.jpg';
								var url='#';
								
								//var htm='<a href="'+url+'"><img src="'+imgurl+'"></a>';
								var htm='<img src="'+imgurl+'">'
								addtabletd('tbtalentreg',htm,result.talentreglist[i].title,'',60);
								htm=result.talentreglist[i].name+'，<br>'+result.talentreglist[i].skill;
								addtabletd('tbtalentreg',htm,result.talentreglist[i].title,'',132);
								addtabletd('tbtalentreg','&nbsp;',result.talentreglist[i].title,'',16);
							}
						}
						
					}else{
					   Ext.Msg.alert('错误提示', result[0].errors);
					}
				},
				failure : function(response) {
					var result = Ext.decode(response.responseText);
					if (result && result.length > 0)
						Ext.Msg.alert('错误提示', result[0].errors);
					else
						Ext.Msg.alert('信息', '后台未响应，网络异常！');
				},
				params : "id="
			});
			
			/// 动态加载通知公告信息
			var sysafficheContent = [];
			var winafficheHtml = "";
			showSysaffiche = function (index){
				var winaffiche = new Ext.Window({  
	             	id: "sysaffichewin",
	             	title: "通知与公告详细内容",
	                width:800, 
	                height:400, 
	                closeAction : 'destroy',
	                layout: 'fit',
				    modal:true,
	                items:[{html:sysafficheContent[index],region:'center'}]
		        }); 
			    winaffiche.show(); 
			};
			Ext.Ajax.request({
				method : 'POST',
				url : 'mainpage/sysafficheForMainPageListAction!sysaffiche.action',
				success : function(response) {
					var result = Ext.decode(response.responseText);
					///alert(result);
					if(typeof result.sysaffichelist !='undefined'){
						var list=result.sysaffichelist;
						if(list!='' && list.length>0){
							delAlltabletr('tbsysaffiche');// 删除表格列
							var len=list.length;
							if(len>3) len=3; // 最多只能显示3个
							sysafficheContent = [];
							for(var i=0;i<len;i++){
									////${item.name}，<br>${item.skill}
								var url='';
								var affichetitle = "["+list[i].sendTime.substring(5,10)+"]"+list[i].title;
								if(affichetitle.length>27)affichetitle = affichetitle.substring(0,25)+"...";
								var tdobj = addtabletr('tbsysaffiche',"<a href='javascript:showSysaffiche("+i+");'>"+affichetitle+"</a>",url);
								var content ="<div style='width:100%;overflow-x:auto;overflow-y:hidden;padding:2px;word-break: break-all; word-wrap:break-word;'>";
								content+='<div style="font-size:18px;text-align:center;">';
								content+=list[i].title+'</div>';
								content+='<div style="font-size:15px;text-align:center;">发布人:'+list[i].sender+'&nbsp;&nbsp;&nbsp;&nbsp;发布时间：'+list[i].sendTime.substring(0,10);
								content+='</div><div style="font-size:12px;text-align:left;padding:5px;">'+list[i].content+'</div></div>';
								///alert(content);
								tdobj.className="handcss";
								sysafficheContent[i] = content;
								//tdobj.onclick=
							}
						}
						
					}else{
					   Ext.Msg.alert('错误提示', result[0].errors);
					}
				},
				failure : function(response) {
					var result = Ext.decode(response.responseText);
					if (result && result.length > 0)
						Ext.Msg.alert('错误提示', result[0].errors);
					else
						Ext.Msg.alert('信息', '后台未响应，网络异常！');
				},
				params : "id="
			});
			
			/// 动态加载达标信息
			Ext.Ajax.request({
				method : 'POST',
				url : 'mainpage/personprogressForMainPageListAction!personprogress.action',
				success : function(response) {
					var result = Ext.decode(response.responseText);
					///alert(result);
					if(typeof result.personprogresslist !='undefined'){
						var list=result.personprogresslist;
						if(list!='' && list.length>0){
							delAlltabletr('tbpersonprogress');// 删除表格列
							var len=list.length;
							if(len>3) len=3; // 最多只能显示3个
							for(var i=0;i<len;i++){
									////${item.name}，<br>${item.skill}
								var url='';
								var content='['+list[i].datetime+']'+list[i].organname+' '+
									list[i].personname+' '+list[i].contents+'已达标';
								addtabletr('tbpersonprogress',content,url);
							}
						}
						
					}else{
					   Ext.Msg.alert('错误提示', result[0].errors);
					}
				},
				failure : function(response) {
					var result = Ext.decode(response.responseText);
					if (result && result.length > 0)
						Ext.Msg.alert('错误提示', result[0].errors);
					else
						Ext.Msg.alert('信息', '后台未响应，网络异常！');
				},
				params : "id="
			});
		});	
		
		
  		var delAlltabletd=function(trid){
			var tb=Ext.get(trid);
			if(tb){
				var tableNode=tb.dom;//document.getElementById(trid);
				//if(tableNode.cells.length>0){
					for(var i=0;i<tableNode.rows.length;i++){
						 // 删除每一行的第一个单元格从而删除第一列
						var celllen=tableNode.rows[i].cells.length;
						for(var j=0;j<celllen;j++){
	            			tableNode.rows[i].deleteCell(0);
	            		}
					}
				//}
			}
		};
		var addtabletd=function(trid,html,title,url,width){
			var tableNode=document.getElementById(trid);
			if(tableNode){
				var td=tableNode.rows[0].insertCell();
				td.innerHTML=html;//'<a href="'+url+'"><img src="'+imgurl+'"></a>';
				if(width>0) td.width=width;
			}
		};
		
		var delAlltabletr=function(tbid){// 删除所有行
			var tableNode=Ext.get(tbid).dom;
			for(var i=0;i<tableNode.rows.length;i++){
				tableNode.deleteRow(i);	
			}
		}
		var addtabletr=function(tbid,content,url){
			var tableNode=Ext.get(tbid).dom;
			var tr=tableNode.insertRow();
			var td=tr.insertCell();
			///td.innerHTML='<a href="#">'+content+'</a>';
			td.innerHTML=content;
			return td;
		}
		
		var showaffiche=function(){
			
		}
		function openOnlineExam(){
			var oNewWindow  = window.open("online.html","onlineExamZs",'width='+(window.screen.availWidth-10)+',height='+(window.screen.availHeight-30)+ ',z-look=yes,top=0,left=0,toolbar=yes,menubar=yes,scrollbars=yes, resizable=yes,location=yes, status=yes');
			if(oNewWindow){
				oNewWindow.focus();
			}
		}
  	//-->
  	</script>
</head>
<body bgcolor="#f0f8ff" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" scroll="no">
<center>
<div id="p3" style="display: none;"></div>
<span class="status" id="p3text" style="display: none;"></span>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td align="center" valign="middle">
			<table width="1000" height="600" border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td colspan="3" align="right" valign="bottom" background="${basePath}resources/images/mainpage/dbindex_01.jpg" width="1000" height="140">
			        <!--登录信息-->
			        <table class="top" border="0" cellspacing="0" cellpadding="0">
			        	<tr>
				            <td width="185" valign="top"><input id="txtaccount" name="用户" class="ipt01" type="text" onKeyDown="if(event.keyCode==13){login('normal');};"</td>
				            <td width="135" valign="top"><input id="txtpwd" name="密码" class="ipt02" type="password" onKeyDown="if(event.keyCode==13){login('normal');};"></td>
				            <td width="52" valign="bottom"><input name="btnlogin" id="btnlogin" type="button" value="登录" class="btn01" onclick="login('normal')" /><input type="button" id="btnqueryscore" style="display: none;" /></td>
				            <td valign="bottom"><input name="btnKjlogin" id="btnKjlogin" type="button" value="在线学习" class="btn02"  onclick="login('fast')" /></td>
			          	</tr>
			        </table>
			        </td>
				</tr>
				<tr>
					<td><img src="${basePath}resources/images/mainpage/dbindex_02.jpg" width="331" height="135"  style="height:100%;"></td>
					<td valign="top" background="${basePath}resources/images/mainpage/dbindex_03.jpg" width="409" height="135">
						<!--达标信息-->
						<div style="margin-top:16px; width:200px;">
							<img src="${basePath}resources/images/mainpage/black.gif" alt="达标信息" width="105" height="30">
						</div>
						<table border="0" id="tbpersonprogress" cellspacing="0" cellpadding="0" class="lan03">
							<c:forEach var="item" items="${personprogresslist}" >
				        		<tr>
					            	<td>[${item.datetime}]${item.organname}${item.personname} ${item.contents}已达标</td>
					          	</tr>	
				        	</c:forEach>
				        	<c:if test="${empty personprogresslist}">
				        		<tr>
				            		<td>无达标记录</td>
				          		</tr>
				        	</c:if>
						</table>
					</td>
					<td valign="top" background="${basePath}resources/images/mainpage/dbindex_04.jpg" width="260" height="135">
						<!--在线考试-->
						<div style="margin:20px 0px 0px 32px; width:200px;">
							<a href="javascript:openOnlineExam();"><img src="${basePath}resources/images/mainpage/black.gif" alt="在线考试" width="170" height="95"></a>
						</div>
					</td>	
				</tr>
				<tr>
					<td><img src="${basePath}resources/images/mainpage/dbindex_05.jpg" width="331" height="176"  style="height:100%;"></td>
					<td valign="top" background="${basePath}resources/images/mainpage/dbindex_06.jpg" width="409" height="176">
						<!--最新课件-->
						<div style="margin-top:12px; width:200px;">
							<img src="${basePath}resources/images/mainpage/black.gif" alt="最新资料" width="105" height="30">
						</div>
						<table id="tbcourseware" border="0" cellspacing="0" cellpadding="0" class="lan03">
							<%-- <tr>
								<c:forEach var="item" items="${coursewarelist}" varStatus="st">
					          		<c:if test="${st.count<5}">
					          		<td><a href=""><img src="${basePath}mainpage/writepicForMainPageListAction!writepic.action?coursewareId=${item.id}"></a></td>
					          		</c:if>
					          	</c:forEach>
					          	<c:if test="${empty coursewarelist}">
					          		<td><a href=""><img src="${basePath}resources/images/mainpage/pic01.jpg"></a></td>
					            	<td><a href=""><img src="${basePath}resources/images/mainpage/pic01.jpg"></a></td>
					            	<td><a href=""><img src="${basePath}resources/images/mainpage/pic01.jpg"></a></td>
					            	<td><a href=""><img src="${basePath}resources/images/mainpage/pic01.jpg"></a></td>
					          	</c:if>
							</tr> --%>	
							<c:forEach var="item" items="${documentlist}" >
				        		<tr>
					            	<td>资料信息</td>
					          	</tr>	
				        	</c:forEach>
				        	<c:if test="${empty documentlist}">
				        		<tr>
				            		<td>无资料记录</td>
				          		</tr>
				        	</c:if>
						</table>
						
					</td>
					<td valign="top" background="${basePath}resources/images/mainpage/dbindex_07.jpg" width="260" height="176">
						<!--通知公告-->
					    <div style="margin:0px 0px 0px 32px; width:200px;">
						 	<img src="${basePath}resources/images/mainpage/black.gif" alt="公示公告" width="105" height="28">
						</div>
						<table border="0" id="tbsysaffiche" cellspacing="0" cellpadding="0" class="lan05">
						    <c:forEach var="item" items="${sysaffichelist}">
				        		<tr>
				        		     <td><a href="">${item.title}</a></td>
				        		</tr>	
				        	</c:forEach>
				        	<c:if test="${empty sysaffichelist}">
				        		<tr>
				        			<td>无公告内容!</td>
				        		</tr>
				        	</c:if>	
						 </table>
						  
					</td>
				</tr>
				<tr>
					<td colspan="3" align="right" valign="middle" background="${basePath}resources/images/mainpage/dbindex_08.jpg" height="149">
			        <div class="btm">浏览器支持：IE8或以上版本IE浏览器 | Google Chrome <br>公司首页 | 联系我们 | 版权所有 2005-2014 大唐华银电力股份有限公司<br>技术支持 长沙市亿泰科技有限公司</div>
			        </td>
				</tr>
			</table>
		</td>
	</tr>
	
		
	<!-- old -->	
	
</table>
</center>
<script type="text/javascript">
<!-- 
		
			
			
	
//-->
</script>
</body>
</html>
