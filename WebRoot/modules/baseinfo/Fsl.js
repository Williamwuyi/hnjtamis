/**
 * 友情链接的模块类
 */
ClassDefine('modules.baseinfo.Fsl', {
			extend : 'base.model.List',
			initComponent : function() {
				//模块列表对象
				this.columns = [{
							name : 'flId',
							width : 0
						}, {
							name : 'flName',
							header : '链接名称',
							width : 3
						}, {
							name : 'flUrl',
							header : '链接地址',
							width : 4
						}, {
							name : 'flIcon',
							header : '链接图标',
							width : 2
						}, {
							name : 'flIconSize',
							header : '图标大小',
							width : 2
						}];
				//模块查询条件对象
				this.terms = [{
							xtype : 'textfield',
							name : 'nameTerm',
							fieldLabel : '链接名称'
						}];
				this.keyColumnName = "flId";//主健属性名
				this.viewOperater = true;
		        this.addOperater = true;
		        this.deleteOperater = true;
		        this.updateOperater = true;
				this.listUrl = "baseinfo/fsl/listForFslListAction!list.action";//列表请求地址 
				this.deleteUrl = "baseinfo/fsl/deleteForFslListAction!delete.action";//删除请求地址
				//打开表单页面方法
				this.openFormWin = function(id, callback,readOnly,data,term,oper) {
					var formConfig = {};
					var readOnly = readOnly||false;
			        formConfig.readOnly = readOnly;
					formConfig.fileUpload = true;
					formConfig.formUrl = "baseinfo/fsl/saveForFslFormAction!save.action";
					formConfig.dataId = id;
					formConfig.labelAlign = 'right';
					formConfig.labelWidth = 70;
					formConfig.findUrl = "baseinfo/fsl/findForFslFormAction!find.action";
					formConfig.callback = callback;
					formConfig.oper=oper;//复制操作类型变量
					formConfig.items = [{
								xtype : 'hidden',
								name : 'flId'
							}, {
								fieldLabel : '链接名称',
								xtype : 'textfield',
								name : 'flName',
								readOnly : readOnly,
								allowBlank : false,
								maxLength : 32
							}, {
								fieldLabel : '链接地址',
								name : 'flUrl',
								allowBlank : false,
								readOnly : readOnly,
								xtype : 'textarea',
								maxLength : 100
							}, {
								fieldLabel : '链接图标',
								xtype : 'selectimage',
								allowBlank : false,
								readOnly : readOnly,
								name : 'flIcon',
								maxLength : 200
							}, {
								fieldLabel : '图标大小',
								allowBlank : false,
								readOnly : readOnly,
								name : 'flIconSize',
								xtype : 'textfield',
								maxLength : 10
							}];

					//}
					var form = ClassCreate('base.model.Form', formConfig);
					//表单窗口
					var formWin = new WindowObject({
								layout : 'form',
								title : '友情链接',
								autoHeight : true,
								width : 300,
								border : false,
								frame : false,
								modal : true,//模态
								closeAction : 'hide',
								items : [form]
							});
					formWin.show();
					form.setFormData(id);
				};
				this.callParent();
			}
		});