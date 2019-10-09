/**
 * 课件库
 */
ClassDefine('modules.hnjtamis.courseware.SharedLib', {
	extend : 'base.model.List',
	initComponent : function() {
		var me = this;
		// 模块列表对象
		this.columns = [{
			name : 'id',
			width : 0
		}, {
			name : 'customFolder.name',
			header : '所在文件夹',
			width : 3
		}, {
			name : 'title',
			header : '标题',
			width : 5
		}, {
			name : 'isAnnounced',
			header : '是否公开',
			width : 1,
			renderer : function(value){
				return value==0?"否":"是";
			}
		}];
		// 模块查询条件对象
		this.terms = [{
			name : 'folderTerm',
			fieldLabel : '文件夹',
			xtype : 'selecttree',
			addPickerWidth:100,
			nameKey : 'id',
			nameLable : 'name',
			readerRoot : 'children',
			keyColumnName : 'id',
			titleColumnName : 'title',
			childColumnName : 'children',
			editorType:'str',
			selectUrl : "kb/customfolder/listForCustomFolderListAction!findFolderTree.action?createUserId="+base.Login.userSession.employeeCode
		}];
		this.keyColumnName = "id";// 主健属性名
		this.viewOperater = true;
		this.addOperater = true;
		this.deleteOperater = true;
		this.updateOperater = true;
		this.readerRoot = 'childFolders';
		//this.otherOperaters = [];//其它扩展按钮操作
		
		this.listUrl = "kb/sharedlib/listForSharedLibListAction!list.action?createUserId=" + base.Login.userSession.employeeCode;// 列表请求地址
		this.deleteUrl = "kb/sharedlib/deleteForSharedLibListAction!delete.action";// 删除请求地址
		// 打开表单页面方法
		this.openFormWin = function(id, callback, readOnly) {
			var formConfig = {};
			var readOnly = readOnly || false;
			formConfig.readOnly = readOnly;
			formConfig.fileUpload = true;
			formConfig.formUrl = "kb/sharedlib/saveForSharedLibFormAction!save.action?createUserId=" + base.Login.userSession.employeeCode;
			formConfig.findUrl = "kb/sharedlib/findForSharedLibFormAction!find.action";
			formConfig.callback = callback;
			formConfig.columnSize = 2;
			formConfig.jsonParemeterName = 'form';
			formConfig.items = new Array();
			formConfig.items.push({
						colspan : 1,
						xtype : 'hidden',
						name : 'id'
					});
			formConfig.items.push({
				colspan : 1,
				fieldLabel : '文件夹',
				name : 'customFolder',
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
				fieldLabel : '是否公布',
				name : 'isAnnounced',
				xtype : 'select',
				data:[[0,'否'],[1,'是']],
				readOnly : readOnly,
				defaultValue : 1
			});
			formConfig.items.push({
						colspan : 2,
						fieldLabel : '标题',
						name : 'title',
						xtype : 'textfield',
						allowBlank : false,
						readOnly : readOnly,
						size:65,
						maxLength : 32
					});
			formConfig.items.push({
				colspan : 2,
				fieldLabel : '附件',
				name : 'accessories',
				xtype : 'accessory',
				readOnly : readOnly,
				accessoryType : 'single',
				allowMaxSize : 5/*,
				viewConfig:{height:100}//高度*/
			});
			var form = ClassCreate('base.model.Form', formConfig);
			form.parentWindow = this;
			// 表单窗口
			var formWin = new WindowObject({
						layout : 'fit',
						title : '共享库',
						height : 350,
						width : 600,
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