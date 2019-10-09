/**
 * 日志的模块类
 */
ClassDefine('modules.baseinfo.Log', {
			extend : 'base.model.List',
			yearArray:[],
			initComponent : function() {
				var date = new Date();
				for(var i=date.getFullYear()-5;i<=date.getFullYear();i++){
					var item = new Array();
					item.push(i);item.push(i);
					this.yearArray[this.yearArray.length] = item;
				}
				this.dayArray = [];
				var temp = new Date(date.getFullYear(),date.getMonth()+1,0);
				for(var i = 1;i<=temp.getDate();i++){
					this.dayArray.push([i,i]);
				}
				this.typeArray = [[1,'登录'],[2,'退出'],[3,'操作'],[4,'异常'],[5,'警告']];
				//日志列表对象
				this.columns = [{
							name : 'logId',
							width : 0
						}, {
							name : 'type',
							header : '日志类型',
							width : 1,
							renderer:function(value){
								return this.typeArray[value-1][1];
							}
						}, {
							name : 'content',
							header : '内容',
							width : 3
						}, {
							name : 'app',
							header : '系统',
							width : 2
						},{
							name : 'moduleName',
							header : '模块',
							width : 2
						}, {
							name : 'employeeUser',
							header : '操作人(账号)',
							width : 1.5
						}, {
							name : 'organ',
							header : '机构',
							width : 2
						}, {
							name : 'ip',
							header : 'IP',
							width : 1.5
						},{
							name : 'dateObject',
							header : '操作时间',
							type:'date',
							format:'Y-m-d H:i',
							width : 2
						}];
				//日志查询条件对象
				this.terms = [{
							xtype : 'textfield',
							name : 'userTerm',
							fieldLabel : '操作者'
						},{
							xtype : 'select',
							name : 'yearTerm',
							fieldLabel : '年',
							data : this.yearArray,
							defaultValue : new Date().getFullYear()
						},{
							xtype : 'select',
							name : 'monthTerm',
							fieldLabel : '月',
							data : [[1,1],[2,2],[3,3],[4,4],[5,5],[6,6],[7,7],[8,8],[9,9],[10,10],[11,11],[12,12]],
							defaultValue : (new Date().getMonth()+1)
						},{
							xtype : 'select',
							name : 'dayStartTerm',
							fieldLabel : '开始日',
							data : this.dayArray,
							defaultValue : 1
						},{
							xtype : 'select',
							name : 'dayEndTerm',
							fieldLabel : '结束日',
							data : this.dayArray,
							defaultValue : new Date().getDate()
						},{
							xtype : 'select',
							name : 'typeTerm',
							fieldLabel : '类型',
							enableSelectOne: false,
							data : this.typeArray
						},{
							xtype : 'textfield',
							name : 'contentTerm',
							fieldLabel : '日志内容'
						},{
							xtype : 'textfield',
							name : 'appTerm',
							fieldLabel : '系统'
						},{
							xtype : 'textfield',
							name : 'moduleTerm',
							fieldLabel : '模块'
						}];
				this.keyColumnName = "logId";//主健属性名
				this.viewOperater = true;
		        this.addOperater = false;
		        this.deleteOperater = false;
		        this.updateOperater = false;
		        this.selModel = {};//取消复选框选择
				this.listUrl = "baseinfo/log/listForLogListAction!list.action";//列表请求地址 
				//打开表单页面方法
				this.openFormWin = function(id, callback,readOnly) {
					var formConfig = {};
					var readOnly = true;//只有查看，不能修改或删除
			        formConfig.readOnly = readOnly;
			        formConfig.fieldDefaults = {labelWidth : 100,labelAlign:'right'};
					formConfig.findUrl = "baseinfo/log/findForLogFormAction!find.action";
					formConfig.callback = callback;
					//formConfig.height = 400;
					//formConfig.width = 550;
					formConfig.oper='view';//复制操作类型变量
					formConfig.items = [{
								fieldLabel : '日志类型',
								name : 'type',
								data : this.typeArray,
								xtype:'select',
								readOnly : readOnly
							}, {
								fieldLabel : '系统',
								name : 'app',
								xtype:'displayfield',
								readOnly : readOnly
							}, {
								fieldLabel : '模块',
								name : 'moduleName',
								xtype:'displayfield',
								readOnly : readOnly
							}, {
								fieldLabel : '操作',
								name : 'workName',
								xtype:'displayfield',
								readOnly : readOnly
							}, {
								fieldLabel : '操作人（账号）',
								readOnly : readOnly,
								xtype:'displayfield',
								name : 'employeeUser'
							}, {
								fieldLabel : '机构',
								readOnly : readOnly,
								xtype:'displayfield',
								name : 'organ'
							}, {
								fieldLabel : 'IP',
								readOnly : readOnly,
								xtype:'displayfield',
								name : 'ip'
							}, {
								fieldLabel : '日志内容',
								readOnly : readOnly,
								xtype : 'displayfield',
								name : 'content'
							}, {
								fieldLabel : '操作时间',
								readOnly : readOnly,
								xtype : 'displayfield',
								type:'date',
								format:'Y-m-d H:i',
								name : 'dateObject'
							}, {
								fieldLabel : '用时(毫秒)',
								readOnly : readOnly,
								xtype : 'displayfield',
								name : 'useTime'
							}];
					var form = ClassCreate('base.model.Form', formConfig);
					var tabs = ClassCreate('TabObject',{width:'100%'});
					tabs.add({title:'日志主信息',items:[form]});
					//表单窗口
					var formWin = new WindowObject({
								layout : 'form',
								title : '业务日志',
								width : 550,
								border : false,
								frame : false,
								modal : true,//模态
								closeAction : 'hide',
								draggable:true,//拖动
						        resizable:false, //变大小 
								items : [tabs]
							});
					form.close = function(){formWin.close();};
					form.setFormData(id,function callbackFun(result){
						//显示子数据
						var logDatas = result.logDatas;
						for(var i=0;i<logDatas.length;i++){
							var logData = logDatas[i];
							var operator = (logData.dataOperaterType==1?"增加":
							    (logData.dataOperaterType==2?"修改":"删除"))+logData.dataName;
							var logSubs = logData.logSubs;
							var formInnerConfig = {};
							formInnerConfig.readOnly = readOnly;
							formInnerConfig.items = [];
							formInnerConfig.items.push({
								                name : 'list',
												xtype : 'editlist',
												fieldLabel : '',
												autoHeight : true,
												readOnly : true//,
												//viewConfig:{autoScroll:true,height:300}
								             });
						    formInnerConfig.items[0].columns = [];
						    formInnerConfig.items[0].columns.push({
															name : 'fieldName',
															header : '字段名称',
															width : 3
														});
						    if(logData.dataOperaterType==2) 
						       formInnerConfig.items[0].columns.push({
															name : 'oldValue',
															header : '原数据',
															width : 2
														});
		                    formInnerConfig.items[0].columns.push({
															name : 'newValue',
															header : '数据',
															width : 2
														});
							formInnerConfig.autoScroll = true;
							//formInnerConfig.height = 500;
							//formInnerConfig.width = formConfig.width;
							formInnerConfig.fieldDefaults = formConfig.fieldDefaults;
							formInnerConfig.maxHeight=400;
							//formInnerConfig.autoScroll = true;
							var formInner = ClassCreate('base.model.Form', formInnerConfig);
							formInner.close = function(){formWin.close();};
							tabs.add({title:operator,items:[formInner]});
							formInner.un('afterrender',formInner.afterRenderEventFun);//取消原监听方法
							var list = formInner.getForm().findField('list');
							var index = 0;
							for(var j=0;j<logSubs.length;j++){
								var logSub = logSubs[j];
								if(logSub.attType!=0&&logSub.linkType!=1){//只显示非主键且非级联数据
								   var p = {};
								   p['fieldName'] = logSub.attName;
								   p['oldValue'] = logSub.attOldValueEx;
								   p['newValue'] = logSub.attValueEx;
								   list.store.insert(index++,p);
								}
							}
						}
					});
					form.un('afterrender',form.afterRenderEventFun);//取消监听方法
					formWin.show();
				};
				this.callParent();
			}
		});