ClassDefine('modules.hnjtamis.client.ClientFile', {
	extend : 'base.model.List',
	initComponent : function() {
		var me = this;
		// 模块列表对象
		this.columns = [{
			name : 'id',
			width : 0
		}, {
			name : 'fileName',
			header : '文件名',
			width : 1
		}, {
			name : 'filePath',
			header : '相对路径',
			width : 1
		}, {
			name : 'fileSize',
			header : '文件大小',
			width : 1
		}, {
			name : 'lastUpdateTime',
			header : '最后修改时间',
			width : 1
		}, {
			name : 'fileVersion',
			header : '版本号',
			width : 1
		}, {
			name : 'uploadTime',
			header : '上传时间',
			width : 1
		}, {
			name : 'isRecoverVer',
			header : '是否指定恢复版本',
			width : 1,
			renderer : function(value){
				return value==0?"否":"是";
			}
		}];
		// 模块查询条件对象
		this.terms = [];
		this.keyColumnName = "id";// 主健属性名
		this.viewOperater = false;
		this.addOperater = true;
		this.deleteOperater = true;
		this.updateOperater = false;
		this.otherOperaters = [];//其它扩展按钮操作
		this.otherOperaters.push({
				xtype : 'button',//按钮类型
				icon : 'resources/icons/fam/cog_edit.png',//按钮图标
				text : "指定恢复版本",//按钮标题
				handler : function() {//按钮事件
					var id = me.getSelectIds();
					if(id == "" || id.split(",").length>1){
						Ext.Msg.alert('提示', '请选择一条记录！');
						return;
					}
					var selected = me.getSelectionModel().selected;					
					var record = selected.get(0);
					var fileName = record.data.fileName;
					Ext.MessageBox.confirm('询问', '确定恢复到此版本文件？',function(btn){
						if(btn!='yes') return;
					var eapMaskTip = EapMaskTip(document.body);
					EapAjax.request({
						method : 'GET',
						url : 'client/recoverForClientFileFormAction!recover.action?clientFileFileName='+fileName+'&id='+id,
						async : true,
						success : function(response) {
							var result = Ext.decode(response.responseText);
							if (Ext.isArray(result)&&result[0].errors) {
								var msg = result[0].errors;
								Ext.Msg.alert('错误', msg);
							} else {
								Ext.Msg.alert('信息', result.msg);
								me.termQueryFun(false,'flash');
							}
							eapMaskTip.hide();
						},
						failure : function() {
							Ext.Msg.alert('信息', '后台未响应，网络异常！');
							eapMaskTip.hide();
						}
					});
				});
					//me.getExamineeForm(id);
			}			
		});
		
		this.listUrl = "client/listForClientFileListAction!list.action";// 列表请求地址
		this.deleteUrl = "client/deleteForClientFileListAction!delete.action";// 删除请求地址
		// 打开表单页面方法
		this.openFormWin = function(id, callback, readOnly) {
			var formConfig = {};
			var readOnly = readOnly || false;
			formConfig.readOnly = readOnly;
			formConfig.fileUpload = true;
			formConfig.formUrl = "client/saveForClientFileFormAction!save.action";
			formConfig.findUrl = "client/findForClientFileFormAction!find.action";
			formConfig.callback = callback;
			formConfig.columnSize = 1;
			formConfig.jsonParemeterName = 'form';
			formConfig.items = new Array();
			formConfig.items.push({
						colspan : 2,
						xtype : 'hidden',
						name : 'id'
					});
			formConfig.items.push({
				colspan : 1,
				fieldLabel : '相对路径',
				name : 'filePath',
				xtype : 'textfield',
				allowBlank : true,
				readOnly : readOnly,
				size:60,
				maxLength : 32
			});
			formConfig.items.push({
				colspan : 1,
				fieldLabel : '版本号',
				name : 'fileVersion',
				xtype : 'textfield',
				allowBlank : false,
				readOnly : readOnly,
				//size:70,
				maxLength : 32
			});
			var file = Ext.create('Ext.form.field.File',{
				 name:'clientFile',
				 clearOnSubmit:false,
				 buttonText:'浏览',
				 allowBlank : false,
				 emptyText: '请选择要上传的文件',
				 width: 550
			});
			formConfig.items.push(file);
			var form = ClassCreate('base.model.Form', formConfig);
			form.parentWindow = this;
			// 表单窗口
			var formWin = new WindowObject({
						layout : 'fit',
						title : '系统参数配置',
						height : 150,
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