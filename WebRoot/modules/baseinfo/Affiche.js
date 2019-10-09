/**
 * 系统公告的模块类
 */
ClassDefine('modules.baseinfo.Affiche', {
	extend : 'base.model.List',
	requires : ['modules.baseinfo.Accessory'],
	// flowCode:'1',
	// flowStateField:'accessoriesItemId',
	// flowStateName:'审核状态',
	initComponent : function() {
		var me = this;
		// 模块列表对象
		this.columns = [{
					name : 'saId',
					width : 0
				}, {
					name : 'title',
					header : '标题',
					width : 3
				}/*
					 * , { name : 'content', header : '内容', width : 4 }
					 */, {
					name : 'sendTime',
					header : '发布时间',
					type : 'date',
					format : 'Y-m-d',
					width : 2
				}, {
					name : 'sender',
					header : '发布人',
					width : 2
				}, {
					name : 'deadline',
					header : '有效天数',
					width : 2
				}];
		// 模块查询条件对象
		this.terms = [{
					xtype : 'textfield',
					name : 'titleTerm',
					fieldLabel : '标题'
				}, {
					fieldLabel : '发布人',
					name : 'senderTerm'
				}, {
					fieldLabel : '开始日期',
					name : 'startTimeTerm',
					xtype : 'datefield',
					format : 'Y-m-d'
				}, {
					fieldLabel : '结束日期',
					name : 'endTimeTerm',
					xtype : 'datefield',
					format : 'Y-m-d'
				}];
		this.keyColumnName = "saId";// 主健属性名
		this.viewOperater = true;
		this.addOperater = true;
		this.deleteOperater = true;
		this.updateOperater = true;
		this.otherOperaters = [];// 其它扩展按钮操作
		this.otherOperaters.push({
					xtype : 'button',
					icon : 'resources/icons/fam/user_edit.png',
					text : "管理附件",
					handler : function() {
						var id = me.getSelectIds();
						if (id == "" || id.split(",").length > 1) {
							Ext.Msg.alert('提示', '请选择一条记录！');
							return;
						}
						// 业务ID、是否只读、最大附件数量、附件类型、回调函数
						modules.baseinfo.Accessory.openWin(id, false, 5,
								'single', function() {
									// 输出附件个数
									// alert(this.accessory.getSize());
								});
						// 打开主页签接口方法
						// grablEapLogin.createWindow("custemMenu001","自定义菜单","","http://www.baidu.com/");
					}
				});
		this.listUrl = "baseinfo/affiche/listForAfficheListAction!list.action";// 列表请求地址
		this.deleteUrl = "baseinfo/affiche/deleteForAfficheListAction!delete.action";// 删除请求地址
		// 打开表单页面方法
		this.openFormWin = function(id, callback, readOnly, data, term, oper,
				flowConfig) {
			var formConfig = {};
			var readOnly = readOnly || false;
			formConfig.readOnly = readOnly;
			formConfig.fileUpload = true;
			formConfig.formUrl = "baseinfo/affiche/saveForAfficheFormAction!save.action";
			formConfig.findUrl = "baseinfo/affiche/findForAfficheFormAction!find.action";
			formConfig.callback = callback;
			formConfig.columnSize = 2;
			formConfig.oper = oper;// 复制操作类型变量
			formConfig.items = new Array();
			var updateForm = [{
						colspan : 2,
						xtype : 'hidden',
						name : 'saId'
					}, {
						colspan : 2,
						fieldLabel : '标题',
						name : 'title',
						xtype : 'textfield',// 查看时不显示录入框
						allowBlank : false,
						readOnly : readOnly,
						width : 740,
						maxLength : 32
					}, {
						colspan : 1,
						fieldLabel : '发布人',
						name : 'sender',
						xtype : 'textfield',
						allowBlank : false,
						readOnly : readOnly,
						width : 370,
						maxLength : 10
					}, {
						colspan : 1,
						fieldLabel : '有效天数',
						name : 'deadline',
						xtype : 'numberfield',
						allowBlank : false,
						readOnly : readOnly,
						width : 370,
						maxLength : 3
					}, {
						colspan : 2,
						fieldLabel : '内容',
						name : 'content',
						xtype : 'htmleditor',
						allowBlank : false,
						readOnly : readOnly,
						width : 740,
						height : 150,
						plugins : [Ext.create('base.core.HtmlEditorImage', {
									imageWidth : '100',
									imageHight : '100'
								})]
					}];
			if (oper=='view') {
				updateForm = [{xtype:'panel',colspan : 2,maxHeight:500,maxWidth:786,autoScroll:true,items:[{
							colspan : 2,
							xtype : 'displayfield',
							style : {
								width : '100%'
							},
							name : 'titleView'/*
												 * , padding:'10 0 20 20'
												 */
						}, {
							colspan : 2,
							fieldLabel : '',
							name : 'content',
							xtype : 'displayfield',
							allowBlank : false,
							readOnly : readOnly,
							//bodyStyle:{width:'100%'},
							padding : '10 0 20 20'
						}]}];
				formConfig.ifAddTableLine = 0;// 不加表格线
				formConfig.items=updateForm;
				var fj=Ext.widget('panel',{
							title : '附件',
							xtype : 'panel',
							collapsed : false,// 缺省折叠
							collapsible: true,
							hideCollapseTool : true,// 隐藏打开关闭工具图标
							titleCollapse : false,// 使能点击标题就打开关闭
							items : [{
										fieldLabel : '附件',
										name : 'accessories',
										xtype : 'accessory',
										readOnly : readOnly,
										accessoryType : 'single',
										// height : 150,
										// autoHeight:false,
										autoScroll : true,
										allowMaxSize : 5,
										setValue:function(value){
											base.core.Accessory. prototype.setValue.call(this,value);
											if(!value||value.length==0)
											  fj.hide();
										}
									}]
						});
				formConfig.items.push({
						xtype : 'panel',
						autoScroll : true,
						colspan : 2,
						layout : {
							type : 'accordion'
							,fill : false
							,multi : true
						},// 是否同时只有一个页签打开
						items : [fj]
					});
			}else
			formConfig.items.push({
						xtype : 'panel',
						autoScroll : true,
						layout : {
							type : 'accordion',
							fill : false,
							multi : true
						},// 是否同时只有一个页签打开
						items : [{
									xtype : 'panel',
									//title : '基本信息',
									hideCollapseTool : true,// 隐藏打开关闭工具图标
									titleCollapse : false,// 使能点击标题就打开关闭
									// autoScroll: true,
									// height:250,
									layout : {
										type : 'table',
										columns : 2,
										tableAttrs : {
											style : {
												width : (oper=='view'?'100%':null)
											}
										}
									},
									items : updateForm
								}, {
									title : '附件',
									xtype : 'panel',
									collapsed : false,// 缺省折叠
									collapsible: true,
									items : [{
												fieldLabel : '附件',
												name : 'accessories',
												xtype : 'accessory',
												readOnly : readOnly,
												accessoryType : 'single',
												// height : 150,
												// autoHeight:false,
												autoScroll : true,
												allowMaxSize : 5
											}]
								}]
					});
			formConfig.otherOperaters = [];
			// 增加工作流操作按钮
			// if(oper=='flow')
			// modules.workflow.Test.addWorkFlowOperaterButton(formConfig.otherOperaters,flowConfig);
			// 增加工作流的历史审核信息
			// if(oper!='add')
			// formConfig.items[0].items.push({title:'流程审核信息',items:[modules.workflow.Test.addWorkFlowAuditInfo(id,me.flowCode,2,'100%')]});
			var form = ClassCreate('base.model.Form', formConfig);
			form.parentWindow = this;
			// 表单窗口
			var formWin = new WindowObject({
						layout : 'fit',
						title : (flowConfig && flowConfig.title) || '系统公告',
						autoHeight : true,
						// height:200,
						width : 800,
						border : false,
						frame : false,
						modal : true,// 模态
						closeAction : 'hide',
						constrain: true,
						items : [form],
						x:300,y:120
					});
			formWin.show();
			form.setFormData(id);
		};
		this.callParent();
	},
	/**
	 * 撤消方法
	 * 
	 * @param {}
	 *            id
	 */
	undo : function(id) {
		Ext.Ajax.request({
					method : 'get',
					url : 'baseinfo/affiche/undoForAfficheFormAction!undo.action?id='
							+ id + "&random=" + Math.random(),
					async : false,// 是否异步
					success : function(response) {
						var result = Ext.decode(response.responseText);
						if (Ext.isArray(result) && result[0].errors) {
							Ext.Msg.alert('错误', result[0].errors);
						} else {
							Ext.Msg.alert('信息', '撤消成功！');
						}
					},
					failure : function() {
						Ext.Msg.alert('错误', '后台未响应，网络异常！');
					}
				});
	},
	statics : {
		/**
		 * 是否有最新公告
		 * 
		 * @return {}
		 */
		haveAffice : function() {
			var data = {}
			EapAjax.request({
						method : 'GET',
						url : 'baseinfo/affiche/sizeForAfficheListAction!size.action',
						async : false,
						success : function(response) {
							var result = Ext.decode(response.responseText);
							if (Ext.isArray(result)) {
								var msg = result[0].errors;
								Ext.Msg.alert('错误', msg);
							} else {
								data = result;
							}
						},
						failure : function() {
							Ext.Msg.alert('信息', '后台未响应，网络异常！');
						}
					});
			return data.total > 0;
		},
		viewAffice : function(id,callbackFun){
			var formConfig = {};
			var readOnly = true;
			formConfig.readOnly = readOnly;
			formConfig.fileUpload = true;
			formConfig.formUrl = "baseinfo/affiche/saveForAfficheFormAction!save.action";
			formConfig.findUrl = "baseinfo/affiche/readAfficheForAfficheFormAction!readAffiche.action";
			formConfig.callback = function(){
			
			};
			formConfig.columnSize = 2;
			formConfig.oper = 'view';// 复制操作类型变量
			formConfig.items = new Array();
			var updateForm = [{xtype:'panel',colspan : 2,maxHeight:500,maxWidth:786,autoScroll:true,items:[{
							colspan : 2,
							xtype : 'displayfield',
							style : {
								width : '100%'
							},
							name : 'titleView'/*
												 * , padding:'10 0 20 20'
												 */
						}, {
							colspan : 2,
							fieldLabel : '',
							name : 'content',
							xtype : 'displayfield',
							allowBlank : false,
							readOnly : readOnly,
							//bodyStyle:{width:'100%'},
							padding : '10 0 20 20'
						}]}];
				formConfig.ifAddTableLine = 0;// 不加表格线
				formConfig.items=updateForm;
				var fj=Ext.widget('panel',{
							title : '附件',
							xtype : 'panel',
							collapsed : false,// 缺省折叠
							collapsible: true,
							hideCollapseTool : true,// 隐藏打开关闭工具图标
							titleCollapse : false,// 使能点击标题就打开关闭
							items : [{
										fieldLabel : '附件',
										name : 'accessories',
										xtype : 'accessory',
										readOnly : readOnly,
										accessoryType : 'single',
										// height : 150,
										// autoHeight:false,
										autoScroll : true,
										allowMaxSize : 5,
										setValue:function(value){
											base.core.Accessory. prototype.setValue.call(this,value);
											if(!value||value.length==0)
											  fj.hide();
										}
									}]
						});
				formConfig.items.push({
						xtype : 'panel',
						autoScroll : true,
						colspan : 2,
						layout : {
							type : 'accordion'
							,fill : false
							,multi : true
						},// 是否同时只有一个页签打开
						items : [fj]
					});
			
			formConfig.otherOperaters = [];
			var form = ClassCreate('base.model.Form', formConfig);
			form.parentWindow = this;
			// 表单窗口
			var formWin = new WindowObject({
						layout : 'fit',
						title : '系统公告 - 详细',
						autoHeight : true,
						// height:200,
						width : 800,
						border : false,
						frame : false,
						modal : true,// 模态
						closeAction : 'hide',
						constrain: true,
						items : [form],
						x:300,y:120
					});
			formWin.show();
			formWin.on('beforeclose',function(){
			      try{
			         callbackFun();
			      }catch(e){}
			 });
			form.setFormData(id);
		},
		displayAffice : function(callbacklistFun) {
			var listConfig = {
				columns : [{
							name : 'saId',
							width : 0
						},{
							name : 'userReadTime',
							width : 0
						}, {
							name : 'title',
							header : '标题',
							width : 6,
							renderer : function(value, cellmeta, record,
									rowIndex, columnIndex, store) {
								cellmeta.style = "white-space: normal !important;";
								var userReadTime = record.get('userReadTime');
								if(userReadTime!=undefined && userReadTime!=null && userReadTime!=''){ 
									return "<a href=\"javascript:viewUserAffice('"+record.get('saId')+"')\">"+value+"</a>";
								}else{
									return "<a href=\"javascript:viewUserAffice('"+record.get('saId')+"')\"><b>"+value+"</b></a>";
								}
								
							}
						}, /*{
							name : 'content',
							header : '内容',
							width : 3,
							renderer : function(value, cellmeta, record,
									rowIndex, columnIndex, store) {
								cellmeta.style = "white-space: normal !important;";
								return value;
							}
						},{
							name : 'accessories',
							header : '附件',
							width : 2,
							renderer : function(value, cellmeta, record,
									rowIndex, columnIndex, store) {
								var me = this;
								var url = location.href;
								cellmeta.style = "white-space: normal !important;";
								var match = url.split(RegExp("[?&]"));
								var basePath = match[0].substr(0, match[0]
												.indexOf("/", match[0].indexOf(
																"/", 7)
																+ 1));
								var clientBasePath = basePath;
								if (me.ownerCt.parentWindow
										&& me.ownerCt.parentWindow.clientBasePath)
									clientBasePath = me.ownerCt.parentWindow.clientBasePath;
								var ret = "";
								for (var i = 0; i < value.length; i++) {
									ret += (i == 0
											? ""
											: "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;")
											+ "<a href='"
											+ clientBasePath
											+ "/openFile.jsp?path="
											+ value[i]['filePath']
											+ "&fileName="
											+ value[i]['fileName']
											+ "' target='_new'>"
											+ value[i]['fileName'] + "</a>";
								}
								return ret;
							}
						}*/ {
							name : 'sendTime',
							header : '发布时间',
							type : 'date',
							format : 'Y-m-d',
							width : 1.5,
							align:"center"
						},{
							name : 'delOpt',
							header : '操作',
							align:"center",
							width : 1,
							renderer : function(value, cellmeta, record,
									rowIndex, columnIndex, store) {
								var saId = record.get('saId');	
								return "<a href=\"javascript:userDelAffiche('"+saId+"')\">删除</a>";
							}
						}],
				keyColumnName : "saId",// 主健属性名
				viewOperater : false,
				addOperater : false,
				deleteOperater : false,
				updateOperater : false,
				listUrl : "baseinfo/affiche/displaysForAfficheListAction!displays.action?limit=15"
			};
			var list = ClassCreate('base.model.List', listConfig);
			viewUserAffice = function(saId){
				modules.baseinfo.Affiche.viewAffice(saId,function(){
					list.store.load();
				})
			}
			userDelAffiche = function(saId){
				Ext.Ajax.request({
					method : 'GET',
					url : 'baseinfo/affiche/userDelAfficheForAfficheFormAction!userDelAffiche.action',
					params:{id:saId},
					success:function(response){
						var re = Ext.decode(response.responseText);
						Ext.Msg.alert("提示",re.msg,function(){
							if(re.success){
								list.store.load();
							}
						});
					},
					failure:function(){Ext.Msg.alert("信息","未能与服务器取得通讯");}
				});
			}
			var formWin = new WindowObject({
						layout : 'fit',
						title : '系统公告',
						height : 420,
						width : 800,
						border : false,
						frame : false,
						modal : true,// 模态
						closeAction : 'hide',
						constrain: true,
						// constrainTo:eap.desktop,
						items : [list],
						x:300,y:120
					});
			formWin.show();
			formWin.on('beforeclose',function(){
			      try{
			         callbacklistFun();
			      }catch(e){}
			 });
		}
	}
});