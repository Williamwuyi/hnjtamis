ClassDefine('modules.hnjtamis.param.SystemParams', {
	extend : 'base.model.List',
	requires: [
        'modules.baseinfo.Dictionary'
    ],
	initComponent : function() {
		var Dictionary = modules.baseinfo.Dictionary;//类别名
		this.sorts = Dictionary.getDictionaryList('SYSTEM_PARAM_SORT');
		var me = this;
		// 模块列表对象
		this.columns = [{
			name : 'id',
			width : 0
		}, {
			name : 'code',
			header : '编码',
			width : 1
		}, {
			name : 'name',
			header : '名称',
			width : 1
		}, {
			name : 'value',
			header : '参数值',
			width : 3
		}, {
			name : 'sort',
			header : '参数分类',
			width : 1,
			renderer : function(value){
				var display = "";
				Ext.Array.each(me.sorts.datas, function(item) {
					if (item['dataKey']==value){
						display = item['dataName'];
					}
				});
				return display;
			}
		}];
		// 模块查询条件对象
		this.terms = [];
		this.keyColumnName = "id";// 主健属性名
		this.viewOperater = true;
		this.addOperater = true;
		this.deleteOperater = true;
		this.updateOperater = true;
		this.otherOperaters = [];//其它扩展按钮操作
		
		this.listUrl = "param/listForSystemParamsListAction!list.action";// 列表请求地址
		this.deleteUrl = "param/deleteForSystemParamsListAction!delete.action";// 删除请求地址
		// 打开表单页面方法
		this.openFormWin = function(id, callback, readOnly) {
			var formConfig = {};
			var readOnly = readOnly || false;
			formConfig.readOnly = readOnly;
			formConfig.fileUpload = true;
			formConfig.formUrl = "param/saveForSystemParamsFormAction!save.action";
			formConfig.findUrl = "param/findForSystemParamsFormAction!find.action";
			formConfig.callback = callback;
			formConfig.columnSize = 2;
			formConfig.jsonParemeterName = 'form';
			formConfig.items = new Array();
			formConfig.items.push({
						colspan : 2,
						xtype : 'hidden',
						name : 'id'
					});
			formConfig.items.push({
				colspan : 1,
				fieldLabel : '编码',
				name : 'code',
				xtype : 'textfield',
				allowBlank : false,
				readOnly : readOnly,
				//size:70,
				maxLength : 50
			});
			formConfig.items.push({
				colspan : 1,
				fieldLabel : '名称',
				name : 'name',
				xtype : 'textfield',
				allowBlank : false,
				readOnly : readOnly,
				//size:70,
				maxLength : 50
			});
			formConfig.items.push({
				colspan : 2,
				fieldLabel : '参数值',
				name : 'value',
				xtype : 'textarea',
				allowBlank : true,
				readOnly : readOnly,
				maxLength : 500,
				width:570,
				height : 50
			});
			formConfig.items.push(
				modules.baseinfo.Dictionary.createDictionarySelectPanel(
		              '参数分类','SYSTEM_PARAM_SORT','sort',true,null,true,readOnly)
		    );
			formConfig.items.push({
				colspan : 1,
				fieldLabel : '排序号',
				name : 'sortNo',
				xtype : 'textfield',
				allowBlank : true,
				readOnly : readOnly,
				//size:70,
				maxLength : 32
			});
			var form = ClassCreate('base.model.Form', formConfig);
			form.parentWindow = this;
			// 表单窗口
			var formWin = new WindowObject({
						layout : 'fit',
						title : '系统参数配置',
						height : 200,
						width : 660,
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