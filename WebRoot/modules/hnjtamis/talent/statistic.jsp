<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="/WEB-INF/tld/c.tld" prefix="c"%>
<%@ page import="cn.com.ite.eap2.module.power.login.LoginAction" %>

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
		<script type="text/javascript" src="${basePath}base/ext/ext-all.js"></script>
		<%--<script type="text/javascript" src="${basePath}base/Login.js"></script>--%>
		<script type="text/javascript">
	Ext.onReady(function() {
		// 表格配置开始
			var columns = [ 
			   	{
				   	dataIndex : 'name',
					header : '姓名'
				}, {
					dataIndex : 'quarter.quarterName',
					header : '岗位'
				}, {
					dataIndex : 'specialityNames',
					header : '专业'
				}, {
					dataIndex : 'sex',
					header : '性别',
					renderer : function(value) {
						return value == 1 ? "男" : "女";
					}
				}, {
					dataIndex : 'birthday',
					header : '出生日期'
				}, /*{
					dataIndex : 'type',
					header : '专家类型',
					width : 1,
					renderer : function(value) {
						var display = "";
						Ext.Array.each(me.types.datas, function(item) {
							if (item['dataKey'] == value) {
								display = item['dataName'];
							}
						});
						return display;
					}
				},*/ {
					dataIndex : 'electedTime',
					header : '当选时间'
				}, {
					dataIndex : 'certificate',
					header : '资质证书'
				}, {
					dataIndex : 'skill',
					header : '技能特长'
				}, {
					dataIndex : 'fkThemeAuditNum',
					header : '反馈审核试题次数'
				}, {
					dataIndex : 'examMarkNum',
					header : '阅卷次数'
				}, {
					dataIndex : 'isDel',
					header : '是否注销',
					renderer : function(value) {
						return value == 0 ? "否" : "是";
					}
				}, {
					dataIndex : 'isAudited',
					header : '是否审核',
					renderer : function(value) {
						return value == 0 ? "否" : "是";
					}
				},{
					dataIndex: 'organ.organName',
					header: '所属机构'
				}
			   	];
			
			var gridstore = new Ext.data.Store({
					proxy:{
						type: 'ajax',
						url: 'talent/reg/listForTalentRegistrationListAction!list.action?organTerm=<%=LoginAction.getUserSessionInfo().getCurrentOrganId() %>',
						reader: {
							type: 'json',
							idProperty: 'id',
							totalProperty: 'totalProperty',
							root: 'list'
						}
					},
					autoLoad: true,  
					fields:[{
						name : 'name'
					}, {
						name : 'quarter.quarterName'
					}, {
						name : 'specialityNames'
					}, {
						name : 'sex'
					}, {
						name : 'birthday'
					}, /*{
						name : 'type'
					}, */{
						name : 'electedTime'
					}, {
						name : 'certificate'
					}, {
						name : 'skill'
					}, {
						name : 'isDel'
					}, {
						name : 'isAudited'
					}, {
						name: 'organ.organName'
					}, {
						name : 'fkThemeAuditNum'
					}, {
						name : 'examMarkNum'
					}]
				});

			var grid = new Ext.grid.GridPanel( {
				id: 'dataGrid',
				store : gridstore,
				height: 400,
				columns : columns,
				title : '专家信息',
				region : 'north'
			});
			// 表格配置结束
			
			
			
			//统计图标开始
			Ext.define('ChartModel', {
				extend: 'Ext.data.Model',
				fields: ['specialityName', 'talentCount']
			});			
			var chartStore = Ext.create('Ext.data.Store',{
				model: ChartModel,
				proxy : {
					type : 'ajax',
					//actionMethods : "GET",
					url : 'talent/reg/chartForTalentRegistrationListAction!chart.action?organTerm=<%=LoginAction.getUserSessionInfo().getCurrentOrganId() %>',
					reader : {
						type : 'json'
					}
				}
			}).load();
			
			//柱状图
			var columnChart = Ext.create('Ext.chart.Chart', {
				id: 'columnChart',
	            style: 'background:#fff',
	            animate: true,//是否显示动画效果
	            shadow: true,//是否显示阴影部分
	            store: chartStore,//序列
		        //region: 'west',
	            legend: {
	                position: 'bottom' //图例显示位置
	            },
	            //坐标轴定义
	            axes: [{
	                type: 'Numeric',
	                position: 'left',
	                fields: ['talentCount'],
	                label: {
	                    renderer: Ext.util.Format.numberRenderer('0,0')
	                },
	                grid: false,
	                title: '人数',
	                minimum: 0
	            }, {
	                type: 'Category',
	                position: 'bottom',
	                fields: ['specialityName'],
	                title: '专业',
	                grid: false //是否显示纵向网格线
	            }],
	            series: [{
	                type: 'column',
	                axis: 'left',	                
	                //style: {width: 10},
	                highlight: true, //高亮显示
	                //鼠标移动到柱子上所显示的数据提示框
	                tips: {
	                  trackMouse: true,//数据提示框是否跟着鼠标移动
	                  width: 180,//提示框宽度
	                  height: 28,
	                  renderer: function (storeItem, item) {
	                      this.setTitle(storeItem.get('specialityName') + ': ' + storeItem.get(item.yField) + '人');
	                  }
	                },
	                /*label: {
	                	display: 'insideEnd',
	                	'text-anchor': 'middle',
	                	field: 'talentCount',
	                	renderer: Ext.util.Format.numberRenderer('0'),
	                	orientation: 'vertical',
	                	color: '#333'
	                }, */               		                
	                showInLegend: false,
	                xField: 'specialityName',
	                yField: 'talentCount'
	            }]
	        });

	        //饼图
			var pieChart = Ext.create('Ext.chart.Chart', {
				id: 'pieChart',
				style: 'background:#fff',
				store: chartStore,
	            animate: true,//是否显示动画效果
	            shadow: true,//是否显示阴影部分
		        region: 'center',
	            legend: {
	                position: 'right'
	            },
				series: [
					{
						type: 'pie',
						field: 'talentCount',
						showInLegend: true,	
						highlight: true,                
		                tips: {
			                  trackMouse: true,//数据提示框是否跟着鼠标移动
			                  width: 180,//提示框宽度
			                  height: 28,
			                  renderer: function (storeItem, item) {
			                      this.setTitle(storeItem.get('specialityName') + ': ' + storeItem.get('talentCount') + '人');
			                  }
			            },
						label: {
							field: 'specialityName',
							display: 'rotate',
							contrast: true,
							font: '12px Arial'
						}
					}
				]
	        });
			//统计图标结束
			

			// 树形配置开始
			Ext.define('OrganDeptModel', {
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
					} ]
				});
			
			var treestore = Ext
				.create(
						'Ext.data.TreeStore',{
							model: OrganDeptModel,
							root: { expanded: false },
							autoLoad: false,
							proxy : {
								type : 'ajax',
								//actionMethods : "GET",
								url : 'organization/quarter/odqtreeForQuarterListAction!odqtree.action?organTerm=<%=LoginAction.getUserSessionInfo().getCurrentOrganId() %>',
								reader : {
									type : 'json'
								}
							},
							nodeParam : 'parentId',
							defaultRootId : ''
						});
			var tree = new Ext.tree.TreePanel(
					{
						store : treestore,
						id: 'leftTree',
						title : '机构部门岗位',
						region : 'west',
						split : true,
						collapsible : true,
						rootVisible : false,
						useArrows : true,
						autoScroll : true,
						width : 260,
						minSize : 80,
						maxSize : 200,
						listeners : {
							itemclick : function(view, record, item, index, e) {
								var type = record.raw.type;
								var id = record.raw.id;
								var gridBaseURL = "talent/reg/listForTalentRegistrationListAction!list.action?";
								grid.store.proxy.url = gridBaseURL + type + "Term=" + id;									
								grid.store.reload();

								var chartBaseURL = "talent/reg/chartForTalentRegistrationListAction!chart.action?";
								chartStore.proxy.url = chartBaseURL + type + "Term=" + id;
								chartStore.reload();
							}
						}
					});
			treestore.on("load", function(mystore,node,success) {
		        var record = mystore.getRootNode().childNodes[0];
		        record.expand();
		        tree.getView().refresh();
			});
			//treestore.load();
			treestore.getRootNode().expand(false,false)
			// 树形配置结束

		    // 表单配置开始
			var columnChartPanel = Ext.create('Ext.panel.Panel',{
				id:'columnChartPanel',
		        labelWidth: 50,
		        width: 500,
				layout: 'fit',
		        title: '专家人数统计',
		        region: 'west',
		        items: [columnChart]
		    });

			var pieChartPanel = Ext.create('Ext.panel.Panel',{
				id:'pieChartPanel',
		        labelWidth: 50,
		        width: 500,
				layout: 'fit',
		        title: '专家人数统计',
		        region: 'west',
		        items: [pieChart]
		    });
		    // 表单配置结束
			
			// 布局开始
			var viewport = new Ext.Viewport( {
				layout : 'border',
				items : [ tree, {
					region : 'center',
					split : true,
					border : false,
					layout : 'border',
					items : [ grid, {
						layout: 'border',
						region: 'center',
						border: false,
						split: true,
						items: [columnChartPanel, pieChartPanel]
						}]
					}],
				listeners: { 
					'resize': function(  viewport,  adjWidth,  adjHeight,  rawWidth,  rawHeight )  {
					var alignWidth = Ext.getCmp('dataGrid').getWidth();
					Ext.getCmp("columnChartPanel").setWidth(alignWidth * 0.5);
					Ext.getCmp("pieChartPanel").setWidth(alignWidth * 0.5);
				}
			}
        });
			// 布局结束
		});
</script>
	</head>
	<body>
		<div id="south-div"></div>
	</body>
</html>