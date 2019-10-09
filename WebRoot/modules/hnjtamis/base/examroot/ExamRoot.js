/**
 * 考点管理的模块类
 */
ClassDefine('modules.hnjtamis.base.examroot.ExamRoot', {
	extend : 'base.model.List',
	initComponent : function() {
		var me = this;
		// 模块列表对象
		this.columns = [{
					name : 'examRootId',
					width : 0
				}, {
					name : 'examRootName',
					header : '考点名称',
					width : 2
				}, {
					name : 'examRootPlace',
					header : '考点地点',
					width : 2
				}, {
					name : 'maxUserNum',	
					header : '最大人数',
					width : 1
				}, {
					name : 'loginUrl',	
					header : '登录地址',
					width : 2
				}];
		// 模块查询条件对象
		this.terms = [{
					xtype : 'textfield',
					name : 'titleTerm',
					fieldLabel : '考点名称'
				}];
		this.keyColumnName = "examRootId";// 主健属性名
		this.viewOperater = true;
		this.addOperater = true;
		this.deleteOperater = true;
		this.updateOperater = true;
		this.otherOperaters = [];//其它扩展按钮操作
		this.otherOperaters.push("");
		this.listUrl = "base/examroot/listForExamRootListAction!list.action";// 列表请求地址
		this.deleteUrl = "base/examroot/deleteForExamRootListAction!delete.action";// 删除请求地址
		
		var sortConfig = {};
		//列属性配置复制
		sortConfig.columns = new Array(this.columns.length);
		for(var i=0;i<this.columns.length;i++){
		   sortConfig.columns[i] = Ext.clone(this.columns[i]);
		   delete sortConfig.columns[i].xtype;
		}
		//打开排序页面
		this.openSortWin = function(record,term,store,callback){
			var me = this;
			sortConfig.keyColumnName = me.keyColumnName;
			sortConfig.sortlistUrl = me.listUrl+"?"+me.termForm.getValues(true)+"&limit=0";
			sortConfig.saveSortUrl = 'base/examroot/saveSortForExamRootFormAction!saveSort.action';
			sortConfig.callback = callback;
			var sortPanel = ClassCreate('base.model.SortList', Ext.clone(sortConfig));
			// 表单窗口
			var sortWin = new WindowObject({
						layout : 'fit',
						title : '题型排序',
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
		this.openFormWin = function(id, callback, readOnly,data,term,oper) {
			var formConfig = {};
			var readOnly = readOnly || false;
			formConfig.readOnly = readOnly;
			//formConfig.fileUpload = true;
			formConfig.formUrl = "base/examroot/saveForExamRootFormAction!save.action";
			formConfig.findUrl = "base/examroot/findForExamRootFormAction!find.action";
			formConfig.callback = callback;
			formConfig.columnSize = 2;
			formConfig.jsonParemeterName = 'form';
			formConfig.clearButtonEnabled = false;
			formConfig.items = new Array();
			formConfig.oper = oper;// 复制操作类型变量
			formConfig.items.push({
						xtype : 'hidden',
						name : 'examRootId'
					});
			//=====================================================================
			formConfig.items.push({
						colspan : 1,
						fieldLabel : '考点名称',
						name : 'examRootName',
						xtype : 'textfield',
						allowBlank : false,
						readOnly : readOnly
					});
			formConfig.items.push({
				colspan : 1,
				fieldLabel : '考点地点',
				name : 'examRootPlace',
				xtype : 'textfield',
				allowBlank : false,
				readOnly : readOnly
			});
			formConfig.items.push({
				colspan : 1,
				fieldLabel : '最大人数',
				name : 'maxUserNum',
				xtype : 'numberfield',
				allowBlank : false,
				readOnly : readOnly
			});
			formConfig.items.push({
				colspan : 1,
				fieldLabel : '登录地址',
				name : 'loginUrl',
				xtype : 'textfield',
				readOnly : readOnly
			});
			formConfig.items.push({
						colspan : 2,
						fieldLabel : '备注',
						name : 'remark',
						xtype : 'textarea',
						readOnly : readOnly,
						maxLength : 500,
						width : 538,
						height : 70
					});
			var form = ClassCreate('base.model.Form', formConfig);
			form.parentWindow = this;
			// 表单窗口
			var formWin = new WindowObject({
						layout : 'fit',
						title : '考点维护',
						width : 600,
						border : false,
						frame : false,
						modal : true,// 模态
						closeAction : 'hide',
						items : [form]
					});	
			formWin.show();
			form.setFormData(id,function(result){
				
			});
		};
		this.callParent();
	}
});