/**
 * 课件库
 */
ClassDefine('modules.hnjtamis.train.TrainRecord', {
	extend : 'base.model.List',
	initComponent : function() {
		var me = this;
		// 模块列表对象
		this.columns = [{
			name : 'id',
			width : 0
		}, {
			name : 'trainOnline.trainImplement.subject',
			header : '课程名称',
			width : 3
		}, {
			name : 'startTime',
			header : '开始时间',
			width : 1
		}, {
			name : 'endTime',
			header : '结束时间',
			width : 1
		}, {
			name : 'duration',
			header : '学习时长',
			width : 1
		}];
		// 模块查询条件对象
		this.terms = [];
		this.keyColumnName = "id";// 主健属性名
		this.viewOperater = false;
		this.addOperater = false;
		this.deleteOperater = false;
		this.updateOperater = false;
		this.otherOperaters = [];//其它扩展按钮操作
		
		this.listUrl = "train/record/listForTrainRecordListAction!list.action?studentId=" + base.Login.userSession.employeeId;// 列表请求地址
		this.deleteUrl = "train/record/deleteForTrainRecordListAction!delete.action";// 删除请求地址
		
		// 打开表单页面方法
		this.openFormWin = function(id, callback, readOnly) {
			var formConfig = {};
			var readOnly = readOnly || false;
			formConfig.readOnly = readOnly;
			formConfig.fileUpload = true;
			formConfig.formUrl = "train/record/saveForTrainRecordFormAction!save.action";
			formConfig.findUrl = "train/record/findForTrainRecordFormAction!find.action";
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
						colspan : 2,
						fieldLabel : '标题',
						name : 'title',
						xtype : 'textfield',
						allowBlank : false,
						readOnly : readOnly,
						size:70,
						maxLength : 32
					});
			var form = ClassCreate('base.model.Form', formConfig);
			form.parentWindow = this;
			// 表单窗口
			var formWin = new WindowObject({
						layout : 'fit',
						title : '学习记录',
						height : 600,
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