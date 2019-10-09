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
				href="${basePath}modules/hnjtamis/train/resource/flexpaper.css" />
		<link rel="stylesheet" type="text/css"
				href="${basePath}modules/hnjtamis/train/resource/minimalist.css" />
		<link rel="stylesheet" type="text/css"
				href="${basePath}modules/hnjtamis/train/resource/js.css" />
		<%--<link rel="stylesheet" type="text/css"
				href="${basePath}modules/hnjtamis/train/resource/vendorstyle.css" />--%>
		<script type="text/javascript" src="${basePath}base/ext/ext-all.js"></script>
		<script type="text/javascript" src="${basePath}modules/hnjtamis/train/resource/jquery-1.11.2.min.js"></script>
		<script type="text/javascript" src="${basePath}modules/hnjtamis/train/resource/flexpaper.js"></script>
		<%--<script type="text/javascript" src="${basePath}modules/hnjtamis/train/resource/flexpaper_handlers.js"></script>--%>
		<script type="text/javascript"
			src="${basePath}modules/hnjtamis/train/resource/flowplayer-3.2.13.min.js"></script>
		<%--<script type="text/javascript"
			src="${basePath}modules/hnjtamis/train/resource/flowplayer-5.5.2.min.js"></script>--%>
		<script type="text/javascript" src="${basePath}modules/hnjtamis/train/resource/swfobject.js"></script>
		<script type="text/javascript"
			src="${basePath}modules/hnjtamis/train/resource/datetime.js"></script>
		<script type="text/javascript">
			var vStartTime,vEndTime;//全局变量，记录当前学习的开始、结束时间
			var vStartTimePer, vEndTimePer;//记录每次操作视频的开始、结束时间，如暂停时间
			var videoLength;//视频总长度
			var vOldLeafId = "";//树节点点击之前记录的视频节点ID
			var vStudyDurations = 0;//单个视频学习时长
			var needSave = false;//标记是否需要保存

			//保存学习记录
			function saveRecord(leafId) {
				//计算学习时长
				var studyMinutes = Math.round(vStudyDurations / 60);//secondsBetween(vStartTime, vEndTime);
				var startTime = vStartTime.format('yyyy-MM-dd hh:mm:ss');
				var endTime = vEndTime.format('yyyy-MM-dd hh:mm:ss');
				Ext.Ajax.request({
					method : 'GET',
					url : 'train/record/saveRecordForTrainRecordFormAction!saveRecord.action?trainId=${param.courseId}&leafId='+leafId+'&courseLength='+videoLength+'&studyMinutes='+studyMinutes+'&startTime='+startTime+'&endTime='+endTime,
					async : true,
					success : function(response) {
						needSave = false;
						vStudyDurations = 0;//学习时长清零
						vStartTime = new Date();
						vEndTime = new Date();
						vStartTimePer = vStartTime;
						vEndTimePer = vEndTime;
						
						var result = Ext.decode(response.responseText);
						if (Ext.isArray(result)&&result[0].errors) {
							var msg = result[0].errors;
							Ext.Msg.alert('错误', msg);
						} else {
							//Ext.Msg.alert('信息', '学习记录保存成功！');							
						}
					},
					failure : function() {
						//Ext.Msg.alert('信息', '后台未响应，网络异常！');
					}
				});
			}

			//验证前提课程是否学习完成
			function validatePreLeafAndPlay(record) {
				var leafId = record.raw.id;
				Ext.Ajax.request({
					method : 'GET',
					url : 'train/online/findPreCourseForTrainOnlineListAction!findPreCourse.action?courseId=${param.courseId}&currentLeafId='+leafId,
					async : false,
					success : function(response) {
						var result = Ext.decode(response.responseText);
						if (result.length > 0) {
							document.getElementById('video_player').innerHTML = '';
							var myStore = new Ext.data.JsonStore({  
								data: result,  
								autoLoad: true,  
								fields: [
										{name: 'fileName', type: 'string'},  
										{name: 'duration',  type: 'int'},  
										{name: 'needDuration',  type: 'int'},
										{name: 'orderNo',  type: 'int'},
										{name: 'sortNo',  type: 'int'},
										{name: 'type',  type: 'int'},
										]  
								});
							 var grid = new Ext.grid.GridPanel({
								title: '学习当前节点之前应先完成以下节点学习',
						        renderTo: 'video_player',
						        store: myStore,
						        autoHeight: true,
						        columns: [
						        { header: '节点名称', dataIndex: 'fileName', width: 600}
						        ]
						    });		
						}
						else 
							play(record);
					},
					failure : function() {
						Ext.Msg.alert('信息', '后台未响应，网络异常！');
					}
				});
			}

			//pt:true计算时间,false不计算
			function preSave(pt) {
				if(!needSave)
					return;
				if (vOldLeafId == '') 
					return;
				
				vEndTime = new Date();
				vEndTimePer = vEndTime;

				if (pt) {
					vStudyDurations += secondsBetween(vStartTimePer, vEndTimePer);
				}
				saveRecord(vOldLeafId);
			}


			//播放视频
			function playVideo(id, filePath, fCount, fDuration) {
				document.getElementById('video_player').innerHTML = '<div id="flvViewer" style="width:640px;height:480px"></div>';
				/*document.getElementById('video_player').innerHTML = '<div id="flvViewer">'+
				            '<div id="jsplaylist" class="is-splash playful color-light">'+
				                '<a class="fp-prev">前一个</a>'+
				                '<a class="fp-next">后一个</a>'+
				            '</div></div>';*/
				var plist = new Array();
				for (var i = 1; i <= fCount; i++) {
					plist.push(filePath + '/' +id + '/' + i + '.flv');
				}
				
				/*$("#jsplaylist").flowplayer({
		            playlist: plist
		        });
				flowplayer($("#jsplaylist")).play(0);*/
				var player = flowplayer("flvViewer", "${basePath}modules/hnjtamis/train/resource/flowplayer-3.2.18.swf", {
					clip: {
						//url: "${basePath}" + url, 
						autoPlay: true, 
						autoBuffering: true,
						autoPlayNext: false,
						baseUrl: '${basePath}'
					},
				    playlist: plist,
					plugins: {
						controls: {
							scrubber: true,
							playlist: true
						}
					},
					onStart : function(clip) {
						videoLength = fDuration;//clip.fullDuration.toFixed(1);//记录视频总长度								
						vStartTime = new Date();//记录开始时间
						vStartTimePer = vStartTime;
						needSave = true;
					},
					onBeforeFinish : function() {
						vEndTime = new Date();//记录结束时间
						vEndTimePer = vEndTime;
						vStudyDurations += secondsBetween(vStartTimePer, vEndTimePer);
						saveRecord(id);
					},
					onResume: function() {
						vStartTimePer = new Date();//重新播放记录开始时间
					}, 
					onPause: function() {//暂停计算时长
						vEndTimePer = new Date();
						vStudyDurations += secondsBetween(vStartTimePer, vEndTimePer);
					},
					onError: function(errorCode, errorMessage) {//出错
						if (errorCode == 200) {
							//Ext.Msg.alert('错误', '视频加载失败！');
							document.getElementById('flvViewer').innerHTML = '视频加载失败';
						} else {
							Ext.Msg.alert('错误', errorMessage);
						}
					}							
				});
			}

			//播放文档
			function playDoc(id, url, filePath) {
				//替换为转换后的文件名
			    var fpath = url.replace(filePath, filePath + "/" + id);
			    var pos = fpath.lastIndexOf(".");
			    var fname = fpath.substring(0, pos + 1) + "swf";
				document.getElementById('video_player').innerHTML = '<div id="documentViewer" class="flexpaper_viewer" style="width:100%;height:700px"></div>';
				$('#documentViewer').FlexPaperViewer(
			            {id: 'docview', config : {					            
			                SWFFile : '${basePath}' + fname,
			                Scale : 1,
			                ZoomTransition : 'easeOut',
			                ZoomTime : 0.5,
			                ZoomInterval : 0.2,
			                FitPageOnLoad : true,
			                FitWidthOnLoad : true,
			                PrintEnabled: false,
			                FullScreenAsMaxWindow :false,
			                ProgressiveLoading : true,
			                MinZoomSize : 0.2,
			                MaxZoomSize : 5,
			                SearchMatchAll : false,
			                InitViewMode : 'Portrait',
			                RenderingOrder : 'flash',
			                StartAtPage : '',
			                ViewModeToolsVisible : true,
			                ZoomToolsVisible : true,
			                NavToolsVisible : true,
			                CursorToolsVisible : true,
			                SearchToolsVisible : false,
			                WMode : 'window',
			                localeChain: 'zh_CN'
			            }
			         }
			    );
			    jQuery('#documentViewer').bind('onDocumentLoaded', function(e, totalPages){
			    	videoLength = totalPages * 60;//记录文档总页数，因为视频时长在后台转换为分钟除以60，为了保持统一这里乘以60			
					vStartTime = new Date();//记录开始时间
					vStartTimePer = vStartTime;
					vStudyDurations = 1 * 60;
					needSave = true;
					//alert('Current page:' + $FlexPaper('documentViewer').getCurrPage());
			    });
				jQuery('#documentViewer').bind('onDocumentLoadedError',function(e, errorMessage){
					needSave = false;
					document.getElementById('video_player').innerHTML = '教材加载失败';
				});
				jQuery('#documentViewer').bind('onCurrentPageChanged',function(e, pagenum) {
					vEndTime = new Date();//记录结束时间
					vEndTimePer = vEndTime;
					if (vStudyDurations < pagenum * 60) {
						vStudyDurations = pagenum * 60;
					}						
					needSave = true;
					//翻到最后一页时保存记录
					if (pagenum * 60 == videoLength) {
						saveRecord(id);
					}
				});				
			}
		    
			//播放视频
			function play(record) {
				var url = record.raw.url;//文件存放路径
				var id = record.raw.id;
				var extension = record.raw.tagName;//文件扩展名
				var filePath = record.raw.temp;//转换前文件路径
				//查询附件转换信息
				Ext.Ajax.request({
					method : 'GET',
					url : 'train/online/findAccessoryForTrainOnlineListAction!findAccessory.action?acceId='+record.raw.id,
					async : false,
					success : function(response) {
						var result = Ext.decode(response.responseText);
						if (result.accessory) {
							var fileType = result.accessory.fileType;
							var fileCount = result.accessory.fileCount;
							var fduration = result.accessory.fullDuaration;;
							if (fileType == 1) {
								playVideo(id, filePath, fileCount, fduration);
							} else if (fileType == 2) {
								playDoc(id, url, filePath);
							} else {
	
							}
						} else {
							document.getElementById('video_player').innerHTML = '视频正在准备中，请稍后再试';
						}
					},
					failure : function() {
						Ext.Msg.alert('信息', '后台未响应，网络异常！');
					}
				});
			}
			
	Ext
			.onReady(function() {
				Ext.define('CourseModel', {
					extend : 'Ext.data.Model',
					fields : [ {
						name : 'id',
						type : 'string'
					}, {
						name : 'text',
						mapping: 'title',
						type : 'string'
					}, {
						name : 'closeIcon',
						type : 'string'
					}, {
						name : 'icon',
						type : 'string'
					}, {
						name : 'leaf',
						type : 'bool'
					}, {
						name : 'type',
						type : 'string'
					}, {
						name : 'tagName',
						type : 'string'
					}, {
						name : 'temp',
						type : 'string'
					} ]
				});
				var mystore = Ext
						.create(
								'Ext.data.TreeStore',
								{
									model : 'CourseModel',
									proxy : {
										type : 'ajax',
										//actionMethods : "GET",
										url : 'train/online/findEmployeeCourseTreeForTrainOnlineListAction!findEmployeeCourseTree.action?courseId=${param.courseId}',
										reader : {
											type : 'json'
										}
									},
									nodeParam : 'parentId',
									defaultRootId : ''/*,
												    root: { expanded: false },
												    autoLoad: false*/
								});

				
				var treePanel = new Ext.tree.TreePanel( {
					title : '我的课程',
					xtype : 'treepanel',
					rootVisible : false,
					store : mystore,
					useArrows : true,
					autoScroll : true,
					//width:260,
					el : 'divTree',
					region : 'north',
					/*columns : [ {
						xtype : 'treecolumn',
						header : '',
						dataIndex : 'title',
						flex : 1
					} ],*/
					//collapsible: true,
					listeners : {
						load : function() {
							//treePanel.expandAll();
					},
					itemclick : function(view, record, item, index, e) {//树节点点击事件
						//只有附件可点击
						if (record.raw.type != "acc")
							return;

						//切换节点之前先保存上一个视频学习记录
						preSave();

						//验证是否可以播放
						validatePreLeafAndPlay(record);
						
						needSave = false;
						vStudyDurations = 0;
						vStartTime = new Date();
						vEndTime = new Date();
						vStartTimePer = vStartTime;
						vEndTimePer = vEndTime;
						
						vOldLeafId = record.raw.id;//视频节点ID
					  }
					}
				});

				treePanel.expandAll();
				treePanel.render();

				var centerItem = new Ext.Panel( {
					region : 'center',
					contentEl : 'video_player',
					title : '在线学习',
					split : true,
					//collapsible: true,
					layout: 'fit',
					titlebar : true
				});
				var westSouthItem = new Ext.Panel( {
					region : 'south',
					height : 200
				});

				// 布局开始
				var viewport = new Ext.Viewport( {
					layout : 'border',
					items : [ {
						region : 'west',
						split : true,
						border : true,
						layout : 'fit',
						width : 260,
						items : [ treePanel, westSouthItem ]
					}, centerItem ]
				});
				// 布局结束	  								    
			});

	window.onbeforeunload = function(e) {
		preSave();
	}; 

	window.onload = function() {
		/*if("${canStudy}" == 0) {
			Ext.Msg.alert("提示", "在开始学习之前应该先进行课前评测！", function(){
				window.parent.doActiveTab("TestBefore");
			});			
		}*/
	};
</script>
	</head>
	<body>
		<div id='divTree'></div>
		<div id="video_player">
			<%--<video id="studyVideo" controls="controls" width="800" height="600" autoplay> 
				<source	src="" type="video/mp4" /> 
				<source src="" type="video/webm" /> 
				<source src="" type="video/ogg" /> 
			</video>--%>
		</div>		
	</body>
</html>