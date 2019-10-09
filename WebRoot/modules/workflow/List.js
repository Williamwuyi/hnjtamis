/**
 * 工作流维护功能的列表类
 */
ClassDefine('modules.workflow.List', {
	extend : 'base.model.List',
	initComponent : function() {
		var me = this;
		// 模块列表对象
		this.columns = [{
					name : 'flowId',
					width : 0
				}, {
					name : 'name',
					header : '流程名称',
					width : 3
				}, {
					name : 'code',
					header : '流程编码',
					width : 2
				}, {
					name : 'version',
					header : '版本',
					width : 1
				}, {
					name : 'serviceName',
					header : '服务名',
					width : 2
				}, {
					name : 'that',
					header : '描述',
					width : 4
				}];
		//模块查询条件对象
		this.terms = [{
					fieldLabel : '名称',
					xtype : 'textfield',
					name : 'nameTerm'
				}, {
					fieldLabel : '编码',
					xtype : 'textfield',
					name : 'codeTerm'
				}, {
					fieldLabel : '是否最新',
					xtype : 'radiogroup',
					defaultType : 'radio',
					items : [
						{boxLabel : '是', name : 'isNew', inputValue : true, checked : true},
						{boxLabel : '否', name : 'isNew', inputValue : false}
					]
				}];
		this.keyColumnName = "flowId";// 主健属性名
		this.viewOperater = true;
		this.addOperater = true;
		this.deleteOperater = true;
		this.updateOperater = true;
		this.readerRoot = 'list';
		this.listUrl = "workflow/listForWorkFlowListAction!list.action";// 列表请求地址
		this.deleteUrl = "workflow/deleteForWorkFlowListAction!delete.action";// 删除请求地址	
		this.otherOperaters = [];//其它扩展按钮操作
		this.otherOperaters.push({
			                xtype : 'button',
							icon : 'resources/icons/fam/book.png',
							text : "复制",
							handler : function() {
								var id = me.getSelectIds();
								if(id==""||id.split(",").length>1){
									Ext.Msg.alert('提示', '请选择一条记录！');
									return;
								}
								Ext.Ajax.request({
									method : 'get',
									url : 'workflow/copyForWorkFlowFormAction!copy.action',
									success : function(response) {
										Ext.Msg.alert('信息', '复制成功！');
										me.termQueryFun(false,'flash');
									},
									failure : function() {
										Ext.Msg.alert('信息', '后台未响应，网络异常！');
									},
									params : "id=" + id
								});
							}
						});	
		// 打开表单页面方法
		this.openFormWin = function(id, callback, readOnly,data,term,oper) {
			var me = this;
			var form = ClassCreate('modules.workflow.Form',{oper:oper,readOnly:readOnly,callback:callback});//表单页面类
			// 表单窗口
			var formWin = new WindowObject({
						layout : 'fit',
						title : '工作流管理',
						//height : 650,
						width : 1000,
						border : false,
						frame : false,
						modal : true,// 模态
						closeAction : 'hide',
						draggable:true,//拖动
						resizable:false, //变大小 
						items : [form]
					});
			formWin.show();
			form.setFormData(id,function(result){
				//添加操作时，应用系统缺省使用查询条件中
				/*if(oper == 'add'){
					form.getForm().setValues({'appSystem':term.appTerm,
						      'appModule':{'moduleId':(data?data.moduleId:''),
						      'moduleName':(data?data.moduleName:'')}});
				}*/
			});
		};
		this.callParent();
	}
});