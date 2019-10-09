/**
 * 模块功能的模块类
 */
ClassDefine('modules.funres.Module', {
	extend : 'base.model.TreeList',
	// typedata : [['0','固定'],['1', '系统'], ['2', '用户']],
	initComponent : function() {
		// 模块列表对象
		this.columns = [{
					name : 'moduleId',
					width : 0
				}, {
					name : 'moduleName',
					header : '模块名称',
					xtype : 'treecolumn',
					width : 4
				}, {
					name : 'moduleCode',
					header : '模块编码',
					width : 2
				}, {
					name : 'appSystem.appId',
					width : 0
				}, {
					name : 'appSystem.appName',
					header : '应用系统',
					width : 2
				}, {
					name : 'description',
					header : '描述',
					width : 3
				}];
		// 模块查询条件对象
		this.terms = [{
					xtype : 'select',
					name : 'appTerm',
					fieldLabel : '应用系统',
					selectUrl : 'funres/app/allForAppListAction!all.action',
					valueField : 'appId',
					displayField : 'appName',
					jsonParemeterName : 'list',
					defaultValue : base.Login.userSession.appId
				}, {
					fieldLabel : '模块名称',
					name : 'nameTerm'
				}, {
					fieldLabel : '模块编码',
					name : 'codeTerm'
				}, {
					fieldLabel : '资源名称',
					name : 'resourceNameTerm'
				}, {
					fieldLabel : '资源编码',
					name : 'resourceCodeTerm'
				}];
		this.keyColumnName = "moduleId";// 主健属性名
		this.viewOperater = true;
		this.addOperater = true;
		this.deleteOperater = true;
		this.updateOperater = true;
		// this.readerRoot = 'appModules';
		this.listUrl = "funres/module/listForModuleListAction!list.action";// 列表请求地址
		this.deleteUrl = "funres/module/deleteForModuleListAction!delete.action";// 删除请求地址
		this.childColumnName = 'appModules';// 子集合的属性名
		var sortConfig = {};
		// 列属性配置复制
		sortConfig.columns = new Array(this.columns.length);
		for (var i = 0; i < this.columns.length; i++) {
			sortConfig.columns[i] = Ext.clone(this.columns[i]);
			delete sortConfig.columns[i].xtype;
		}
		// 打开排序页面
		this.openSortWin = function(record, term, store, callback) {
			var me = this;
			sortConfig.keyColumnName = me.keyColumnName;
			sortConfig.sortlistUrl = "funres/module/subListForModuleListAction!subList.action?parentId="
					+ (record ? record.moduleId : '')
					+ "&appTerm="
					+ (term ? term.appTerm : '');
			sortConfig.jsonParemeterName = 'appModules';
			sortConfig.saveSortUrl = 'funres/module/saveSortForModuleFormAction!saveSort.action';
			sortConfig.callback = callback;
			var sortPanel = ClassCreate('base.model.SortList', Ext
							.clone(sortConfig));
			// 表单窗口
			var sortWin = new WindowObject({
						layout : 'fit',
						title : '模块排序',
						height : 500,
						width : 700,
						border : false,
						frame : false,
						modal : true,// 模态
						closeAction : 'hide',
						items : [sortPanel]
					});
			sortWin.show();
		}
		// 打开表单页面方法
		this.openFormWin = function(id, callback, readOnly, data, term, oper) {
			var me = this;
			var formConfig = {};
			var readOnly = readOnly || false;
			formConfig.readOnly = readOnly;
			formConfig.fileUpload = true;
			formConfig.formUrl = "funres/module/saveForModuleFormAction!save.action";
			formConfig.findUrl = "funres/module/findForModuleFormAction!find.action";
			formConfig.callback = callback;
			formConfig.columnSize = 2;
			formConfig.fieldDefaults = {
				labelAlign : 'right',
				labelWidth : 90
			}
			formConfig.items = new Array();
			formConfig.oper = oper;// 复制操作类型变量
			formConfig.jsonParemeterName = 'form';
			formConfig.items = [{
				xtype : 'panel',
				autoScroll : true,
				layout : {
					type : 'accordion',
					fill : false,
					multi : true
				},// 是否同时只有一个页签打开
				items : [{
					xtype : 'panel',
					title : '基本信息',
					hideCollapseTool : true,// 隐藏打开关闭工具图标
					titleCollapse : false,// 使能点击标题就打开关闭
					// autoScroll: true,
					// height:190,
					layout : {
						type : 'table',
						columns : 2
					},
					items : [{
								colspan : 2,
								xtype : 'hidden',
								name : 'moduleId'
							}, {
								colspan : 1,
								fieldLabel : '模块名称',
								name : 'moduleName',
								xtype : 'textfield',
								allowBlank : false,
								readOnly : readOnly,
								maxLength : 32
							}, {
								colspan : 1,
								fieldLabel : '模块编码',
								name : 'moduleCode',
								xtype : 'textfield',
								allowBlank : false,
								readOnly : readOnly,
								maxLength : 32
							}, {
								colspan : 1,
								fieldLabel : '父模块名称',
								name : 'appModule',
								xtype : 'selecttree',
								addPickerWidth : 40,
								readOnly : readOnly,
								nameKey : 'moduleId',
								nameLable : 'moduleName',
								readerRoot : 'appModules',
								keyColumnName : 'moduleId',
								titleColumnName : 'moduleName',
								childColumnName : 'appModules',
								selectUrl : me.listUrl
										+ "?appTerm="
										+ (term ? term.appTerm : '')
										+ "&filterIds="
										+ (oper == 'update' && data
												? data.moduleId
												: '')
							}, {
								colspan : 1,
								fieldLabel : '应用系统',
								name : 'appSystem',
								xtype : 'selectobject',
								addPickerWidth : 40,
								allowBlank : false,
								readOnly : readOnly,
								valueField : 'appId',
								displayField : 'appName',
								readerRoot : 'list',
								selectUrl : 'funres/app/allForAppListAction!all.action?limit=0',
								selectEventFun : function(combo, record, index) {
									var appModuleField = form.getForm()
											.findField('appModule');
									appModuleField.reflash(me.listUrl
											+ "?appTerm="
											+ (combo.value)
											+ "&filterIds="
											+ (oper == 'update' && data
													? data.moduleId
													: ''));
								}
							}, {
								colspan : 2,
								fieldLabel : '描述',
								name : 'description',
								xtype : 'textarea',
								readOnly : readOnly,
								maxLength : 100,
								height : 70,
								width : 588
							}, {
								colspan : 1,
								name : 'orderNo',
								xtype : 'hidden',
								value : 1
							}, {
								colspan : 1,
								name : 'levelCode',
								xtype : 'hidden'
							}]
				}, {
					title : '功能资源',
					xtype : 'panel',
					//collapsed : false,// 缺省折叠
					items : [{
						colspan : 2,
						fieldLabel : '',
						name : 'moduleResources',
						xtype : 'editlist',
						addOperater : true,
						deleteOperater : true,
						maxHeight : 200,
						// viewConfig:{height:315},//高度
						columns : [{
									width : 0,
									name : 'resourceId'
								}, {
									name : 'resourceCode',
									header : '资源编码',
									editor : {
										xtype : 'textfield',
										allowBlank : false,
										maxLength : 32
									},
									width : 2
								}, {
									name : 'resourceName',
									header : '资源名称',
									editor : {
										xtype : 'textfield',
										allowBlank : false,
										maxLength : 32
									},
									width : 3
								}, {
									name : 'popedomType',
									header : '权限类型',
									editor : {
										xtype : 'select',
										allowBlank : false,
										data : [[2, '可分配'], [1, '管理员独享'],
												[0, '超级管理员独享']]
									},
									width : (base.Login.userSession.userId == 'admin'
											? 2
											: 0),
									defaultValue : 2,
									renderer : function(value) {
										if (value == "")
											return "";
										return value == 0
												? "超级管理员独享"
												: (value == 1 ? "管理员独享" : "可分配");
									}
								}, {
									name : 'resourceType',
									header : '资源类型',
									editor : {
										xtype : 'select',
										allowBlank : false,
										data : [[1, '菜单'], [2, '窗口']]
									},
									defaultValue : 1,
									width : 2,
									renderer : function(value) {
										return value == 1 ? "菜单" : (value == 2
												? "窗口"
												: "");
									}
								}, {
									name : 'resourceUrl',
									header : '资源地址',
									editor : {
										xtype : 'textfield',
										allowBlank : false,
										maxLength : 200
									},
									width : 5
								}, {
									name : 'bigIcon',
									header : '大图标',
									editor : {
										xtype : 'selectimage',
										imageColumnSize : 2
									},
									width : 2
								}, {
									name : 'icon',
									header : '小图标',
									editor : {
										xtype : 'selectimage',
										imageColumnSize : 2
									},
									width : 2
								}],
						readOnly : readOnly
					}]
				}]
			}];
			var form = ClassCreate('base.model.Form', formConfig);
			// 表单窗口
			var formWin = new WindowObject({
						layout : 'fit',
						title : '模块功能',
						autoHeight : true,
						width : 1000,
						border : false,
						frame : false,
						modal : true,// 模态
						closeAction : 'hide',
						draggable : true,// 拖动
						resizable : false, // 变大小
						items : [form]
					});
			formWin.show();
			form.setFormData(id, function(result) {
				// 添加操作时，应用系统缺省使用查询条件中
				if (oper == 'add') {
					form.getForm().setValues({
								'appSystem' : {
									appId : term.appTerm
								},
								'appModule' : {
									'moduleId' : (data ? data.moduleId : ''),
									'moduleName' : (data ? data.moduleName : '')
								}
							});
				}
			});
		};
		this.callParent();
	}
});