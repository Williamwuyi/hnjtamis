/**
 * 系统公告的模块类
 */
ClassDefine('modules.baseinfo.Dictionary', {
	extend : 'base.model.TreeList',
	typedata : [[1, '系统'], [2, '用户']],
	windowConfig : {
		maximized : false
	},// 模块打开窗口配置
	statics : {// 静态方法定义
		/**
		 * 根据字典编码获得字典数据列表
		 * 
		 * @param {}
		 *            typeCode
		 * @return {}
		 */
		getDictionaryList : function(typeCode) {
			var data;
			EapAjax.request({
				method : 'GET',
				url : 'baseinfo/dic/findDataForDicFormAction!findData.action?typeCode='
						+ typeCode,
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
			return data;
		},
		/**
		 * 根据类型编码和数据编码查询数据
		 * 
		 * @param {}
		 *            typeCode
		 * @param {}
		 *            dataKey
		 * @return {}
		 */
		getDictionaryData : function(typeCode, dataKey) {
			var data = modules.baseinfo.Dictionary.getDictionaryList(typeCode);
			Ext.Array.each(data, function(item) {
						if (item['dataKey'] == dataKey)
							return item['dataName'];
					});
			return null;
		},
		/**
		 * 字典下拉框
		 * 
		 * @param {}
		 *            title
		 * @param {}
		 *            typeCode
		 * @param {}
		 *            fieldName
		 * @param {}
		 *            allowBlank
		 * @param {}
		 *            defaultValue
		 * @return {}
		 */
		createDictionarySelectPanel : function(title, typeCode, fieldName,
				allowBlank, defaultValue, enableSelectOne, readOnly) {
			return PanelCreate('select', {
				selectUrl : 'baseinfo/dic/findDataForDicFormAction!findData.action?typeCode='
						+ typeCode,
				fieldLabel : title,
				valueField : 'dataKey',
				displayField : 'dataName',
				jsonParemeterName : 'datas',
				allowBlank : allowBlank,
				name : fieldName,
				defaultValue : defaultValue,
				readOnly : readOnly,
				enableSelectOne : (enableSelectOne != undefined
						? enableSelectOne
						: true)
			});
		}
	},
	initComponent : function() {
		// 模块列表对象
		this.columns = [{
					name : 'dtId',
					width : 0
				}, {
					name : 'dtName',
					header : '类型名称',
					xtype : 'treecolumn',
					width : 5
				}, {
					name : 'dtCode',
					header : '类型编码',
					width : 3
				}, {
					name : 'sysType',
					header : '类型方式',
					width : 2,
					renderer : this.rendererSysTypeFn
				}];
		// 模块查询条件对象
		this.terms = [{
					xtype : 'textfield',
					name : 'codeTerm',
					fieldLabel : '编码'
				}, {
					fieldLabel : '名称',
					name : 'nameTerm'
				}];
		this.keyColumnName = "dtId";// 主健属性名
		this.viewOperater = true;
		this.addOperater = true;
		this.deleteOperater = true;
		this.updateOperater = true;
		this.readerRoot = 'dictionaryTypes';
		this.listUrl = "baseinfo/dic/listForDicListAction!list.action";// 列表请求地址
		this.deleteUrl = "baseinfo/dic/deleteForDicListAction!delete.action";// 删除请求地址
		this.childColumnName = 'dictionaryTypes';// 子集合的属性名
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
			sortConfig.sortlistUrl = "baseinfo/dic/subListForDicListAction!subList.action?parentId="
					+ (record ? record.dtId : '');
			sortConfig.jsonParemeterName = 'dictionaryTypes';
			sortConfig.saveSortUrl = 'baseinfo/dic/saveSortForDicFormAction!saveSort.action';
			sortConfig.callback = callback;
			var sortPanel = ClassCreate('base.model.SortList', Ext
							.clone(sortConfig));
			// 表单窗口
			var sortWin = new WindowObject({
						layout : 'fit',
						title : '数据字典排序',
						height : 500,
						width : 700,
						border : false,
						frame : false,
						modal : true,// 模态
						closeAction : 'hide',
						items : [sortPanel]
					});
			sortPanel.showWin = function(){sortWin.show();}
		}
		// 打开表单页面方法
		this.openFormWin = function(id, callback, readOnly, data, term, oper) {
			var me = this;
			var formConfig = {};
			var readOnly = readOnly || false;
			formConfig.readOnly = readOnly;
			formConfig.fileUpload = true;
			formConfig.formUrl = "baseinfo/dic/saveForDicFormAction!save.action";
			formConfig.findUrl = "baseinfo/dic/findForDicFormAction!find.action";
			formConfig.callback = callback;
			formConfig.columnSize = 2;
			formConfig.jsonParemeterName = 'form';
			formConfig.items = new Array();
			formConfig.oper = oper;// 复制操作类型变量
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
					title : '字典类型',
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
								name : 'dtId'
							}, {
								colspan : 1,
								fieldLabel : '类型名称',
								name : 'dtName',
								xtype : 'textfield',
								allowBlank : false,
								readOnly : readOnly,
								maxLength : 32
							}, {
								colspan : 1,
								fieldLabel : '类型编码',
								name : 'dtCode',
								xtype : 'textfield',
								allowBlank : false,
								readOnly : readOnly,
								maxLength : 32
							}, {
								colspan : 1,
								fieldLabel : '父类型名称',
								name : 'dictionaryType',
								xtype : 'selecttree',
								readOnly : readOnly,
								addPickerWidth : 50,
								nameKey : 'dtId',
								nameLable : 'dtName',
								readerRoot : 'dictionaryTypes',
								keyColumnName : 'dtId',
								titleColumnName : 'dtName',
								childColumnName : 'dictionaryTypes',
								selectUrl : me.listUrl
										+ "?filterIds="
										+ (data && oper == 'update'
												? data.dtId
												: ''),
								selectEventFun : function(combo, record, index) {
									if (combo.value) {
										form.getForm().findField('sysDics')
												.setValue([]);
										form.getForm().findField('sysDics')
												.hide();
									} else {
										form.getForm().findField('sysDics')
												.show();
									}
								}
							}, {
								colspan : 1,
								fieldLabel : '系统类型',
								name : 'sysType',
								xtype : 'select',
								allowBlank : false,
								readOnly : readOnly,
								data : me.typedata
							}, {
								colspan : 2,
								width : 540,
								fieldLabel : '所属系统',
								checked : true,
								name : 'sysDics',
								xtype : 'selecttree',
								readOnly : readOnly,
								nameKey : 'appId',
								nameLable : 'appName',
								readerRoot : 'list',
								keyColumnName : 'appId',
								titleColumnName : 'appName',
								childColumnName : 'homePanels',
								dynamicLoading : false,
								stepLoad : false,
								selectUrl : "funres/app/listForAppListAction!list.action?limit=0"
							}, {
								colspan : 2,
								fieldLabel : '备注',
								name : 'remark',
								xtype : 'textarea',
								readOnly : readOnly,
								maxLength : 100,
								width : 538,
								height : 70
							}, {
								colspan : 1,
								name : 'orderNo',
								xtype : 'hidden',
								value : 0
							}, {
								colspan : 1,
								name : 'levelCode',
								xtype : 'hidden'
							}]
				}, {
					title : '字典数据',
					xtype : 'panel',
					//collapsed : false,// 缺省折叠
					 collapsible: true,
					items : [{
								colspan : 2,
								fieldLabel : '',
								name : 'dictionaries',
								xtype : 'editlist',
								autoHeight : true,
								maxHeight:200,
								addOperater : true,
								deleteOperater : true,
								// viewConfig:{height:210},//高度
								columns : [{
											width : 0,
											name : 'dicId'
										}, {
											name : 'dataKey',
											header : '数据编码',
											editor : {
												xtype : 'textfield',
												maxLength : 32,
												allowBlank : false
											},
											width : 2
										}, {
											name : 'dataName',
											header : '数据名称',
											editor : {
												xtype : 'textfield',
												allowBlank : false,
												maxLength : 32
											},
											width : 3
										}, {
											name : 'description',
											header : '数据描述',
											editor : {
												xtype : 'textarea',
												maxLength : 100
											},
											width : 5
										}],
								readOnly : readOnly
							}]
				}]
			}];
			var form = ClassCreate('base.model.Form', formConfig);
			// 表单窗口
			var formWin = new WindowObject({
						layout : 'fit',
						title : '数据字典',
						autoHeight : true,
						// height : 500,
						width : 600,
						border : false,
						frame : false,
						modal : true,// 模态
						closeAction : 'hide',
						items : [form]
					});
			formWin.show();
			form.setFormData(id, function(result) {
						if (oper == 'add') {
							if (term) {
								form.getForm().setValues({
											'dictionaryType' : {
												'dtId' : (data ? data.dtId : ''),
												'dtName' : (data
														? data.dtName
														: '')
											}
										});
							}
						}
						if (oper == 'update' && result['form'].dictionaryType) {
							form.getForm().findField('sysDics').setValue([]);
							form.getForm().findField('sysDics').hide();
						}
					});
		};
		this.callParent();
	},// 渲染字典类型
	rendererSysTypeFn : function(value) {
		var me = this;
		var ret = "";
		Ext.Array.each(me.typedata, function(item) {
					if (item[0] == value)
						ret = item[1];
				});
		return ret;
	}
});