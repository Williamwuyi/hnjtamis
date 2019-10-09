/**
 * 课件库
 */
ClassDefine('modules.hnjtamis.courseware.CustomFolder', {
	extend : 'base.model.TreeList',
	initComponent : function() {
		var me = this;
		// 模块列表对象
		this.columns = [{
			name : 'id',
			width : 0
		}, {
			name : 'name',
			header : '文件夹名称',
			xtype : 'treecolumn',
			width : 5
		}];
		// 模块查询条件对象
		//this.terms = [];
		this.keyColumnName = "id";// 主健属性名
		this.viewOperater = true;
		this.addOperater = true;
		this.deleteOperater = true;
		this.updateOperater = true;
		this.readerRoot = 'childFolders';
		//this.otherOperaters = [];//其它扩展按钮操作
		
		this.listUrl = "kb/customfolder/listForCustomFolderListAction!list.action?createUserId=" + base.Login.userSession.employeeCode;// 列表请求地址
		this.deleteUrl = "kb/customfolder/deleteForCustomFolderListAction!delete.action";// 删除请求地址
		// 打开表单页面方法
		this.openFormWin = function(id, callback, readOnly) {
			var formConfig = {};
			var readOnly = readOnly || false;
			formConfig.readOnly = readOnly;
			formConfig.fileUpload = true;
			formConfig.formUrl = "kb/customfolder/saveForCustomFolderFormAction!save.action?createUserId=" + base.Login.userSession.employeeCode;
			formConfig.findUrl = "kb/customfolder/findForCustomFolderFormAction!find.action";
			formConfig.callback = callback;
			formConfig.columnSize = 1;
			formConfig.jsonParemeterName = 'form';
			formConfig.items = new Array();
			formConfig.items.push({
						colspan : 1,
						xtype : 'hidden',
						name : 'id'
					});
			formConfig.items.push({
				colspan : 1,
				fieldLabel : '上级文件夹',
				name : 'parentFolder',
				xtype : 'selecttree',
				readOnly : readOnly,
				addPickerWidth:100,
				nameKey : 'id',
				nameLable : 'name',
				readerRoot : 'children',
				keyColumnName : 'id',
				titleColumnName : 'title',
				childColumnName : 'children',
				selectUrl : "kb/customfolder/listForCustomFolderListAction!findFolderTree.action?createUserId="+base.Login.userSession.employeeCode,
				selectEventFun:function(combo,record,index){
					//var folderField = form.getForm().findField('dept');
					//deptField.reflash(me.listUrl+"?filterIds="+(oper=='update'&&data?data.deptId:'')+"&organTerm="+(combo.value));
				}
			});
			formConfig.items.push({
						colspan : 1,
						fieldLabel : '文件夹名称',
						name : 'name',
						xtype : 'textfield',
						allowBlank : false,
						readOnly : readOnly,
						//size:65,
						maxLength : 32
					});
			var form = ClassCreate('base.model.Form', formConfig);
			form.parentWindow = this;
			// 表单窗口
			var formWin = new WindowObject({
						layout : 'fit',
						title : '自定义文件夹',
						height : 150,
						width : 300,
						border : false,
						frame : false,
						modal : true,// 模态
						closeAction : 'hide',
						items : [form]
					});	
			formWin.show();
			form.setFormData(id);
		};
		this.callParent();
	}
});