/**
 * desc :岗位培训达标系统-个人学习模块-个人日志查询
 * date :2015.4.3
 * author:wangyong
 */
ClassDefine('modules.hnjtamis.personal.PersonalLog', {
	extend : 'base.model.List',
	initComponent : function() {
		var me = this;
		// 模块列表对象
		this.columns = [{
					name : 'plogid',
					width : 0
				}, {
					name : 'personname',
					header : '姓名',
					width : 1
				}, {
					name : 'learningtype',
					header : '学习类型',
					width : 1,
					renderer : function(value){
						return value==1?"个人学习":"岗位达标";
					}
				}, {
					name : 'learningstarttime',
					header : '开始时间',
					width : 1
				}, {
					name : 'learningendtime',
					header : '结束时间',
					width : 1
				}, {
					name : 'logScore',
					header : '分数',
					width : 1
				},{
					name : 'contents',
					header : '学习内容',
					width : 1
				}, {
					name : 'createdBy',
					header : '创建人员',
					width : 1
				}, {
					name : 'lastUpdatedBy',
					header : '最后修改人员',
					width : 1
				}];
		// 模块查询条件对象
		this.terms = [{
					xtype : 'textfield',
					name : 'contentsTerm',
					fieldLabel : '内容'
				}, {
					fieldLabel : '开始日期',
					name : 'startTimeTerm',
					xtype : 'datefield',
					format : 'Y-m-d'
				}, {
					fieldLabel : '结束日期',
					name : 'endTimeTerm',
					xtype : 'datefield',
					format : 'Y-m-d'
				}];
		this.keyColumnName = "plogid";// 主健属性名
		this.viewOperater = true;
		this.addOperater = true;
		this.deleteOperater = true;
		this.updateOperater = true;
		
		this.listUrl = "personal/log/listForPersonalLogListAction!list.action";// 列表请求地址
		this.deleteUrl = "personal/log/deleteForPersonalLogListAction!delete.action";// 删除请求地址
		// 打开表单页面方法
		this.openFormWin = function(id, callback, readOnly,data,term,oper) {
			var formConfig = {};
			var readOnly = readOnly || false;
			formConfig.readOnly = readOnly;
			formConfig.fileUpload = true;
			formConfig.formUrl = "personal/log/saveForPersonalLogFormAction!save.action";
			formConfig.findUrl = "personal/log/findForPersonalLogFormAction!find.action";
			formConfig.callback = callback;
			formConfig.columnSize = 1;
			formConfig.jsonParemeterName = 'form';
			formConfig.items = new Array();
			formConfig.oper = oper;// 复制操作类型变量
			formConfig.items.push({
						colspan : 1,
						xtype : 'hidden',
						name : 'plogid'
					});
			formConfig.items.push({
						colspan : 1,
						fieldLabel : '姓名',
						name : 'personname',
						xtype : 'textfield',
						allowBlank : false,
						readOnly : readOnly,
						maxLength : 20
					});
			formConfig.items.push({
						colspan : 1,
						fieldLabel : '学习类型',
						name : 'learningtype',
						xtype : 'select',
						data:[[1,'个人学习'],[2,'岗位达标']],
						readOnly : readOnly,
						defaultValue : 1
					});
			formConfig.items.push({
						colspan : 1,
						fieldLabel : '开始时间',
						name : 'learningstarttime',
						xtype : 'datetimefield',
						allowBlank : false,
						readOnly : readOnly,
						maxLength : 20,
						format:'Y-m-d'
					});
			formConfig.items.push({
						colspan : 1,
						fieldLabel : '结束时间',
						name : 'learningendtime',
						xtype : 'datetimefield',
						allowBlank : false,
						readOnly : readOnly,
						maxLength : 20,
						format:'Y-m-d'
					});
			formConfig.items.push({
						colspan : 1,
						fieldLabel : '分数',
						name : 'logScore',
						xtype : 'numberfield',
						allowBlank : false,
						readOnly : false,
						maxLength : 10
					});	
			formConfig.items.push({
						colspan : 1,
						fieldLabel : '学习内容',
						name : 'contents',
						xtype : 'textarea',
						allowBlank : false,
						readOnly : readOnly,
						maxLength : 250,
						width:540,
						height : 170
					});
			formConfig.items.push({
						colspan : 1,
						name : 'personcode',
						xtype : 'hidden'
					});
			formConfig.items.push({
						colspan : 1,
						name : 'lastUpdateDate',
						xtype : 'hidden'
					});
			formConfig.items.push({
						colspan : 1,
						name : 'lastUpdatedBy',
						xtype : 'hidden'
					});
			formConfig.items.push({
						colspan : 1,
						name : 'creationDate',
						xtype : 'hidden'
					});
			formConfig.items.push({
						colspan : 1,
						name : 'createdBy',
						xtype : 'hidden'
					});
			formConfig.items.push({
						colspan : 1,
						name : 'organid',
						xtype : 'hidden'
					});
			
			var form = ClassCreate('base.model.Form', formConfig);
			// 表单窗口
			var formWin = new WindowObject({
						layout : 'fit',
						personname : '学习日志',
						height : 390,
						width : 600,
						border : false,
						frame : false,
						modal : true,// 模态
						closeAction : 'hide',
						items : [form]
					});	
			formWin.show();
			form.setFormData(id,function(result){
				if(oper == 'add'){
					var currentDate=new Date();
					var cdate=Ext.Date.format(currentDate, 'YYYY-MM-dd'); 
					if(term){
					   //form.getForm().findField('dept').reflash(me.listUrl+"?organTerm="+term.organTerm);
					   form.getForm().setValues(
					   		{'personname':base.Login.userSession.employeeName,
					   		'personcode':base.Login.userSession.employeeId,
					   		'learningstarttime':cdate});
					   //form.getForm().setValues({'dept':{'deptId':(data?data.deptId:''),'deptName':(data?data.deptName:'')},
					   //                          'organ':{'organId':term.organTerm,'organName':''}});
					}
				}else
				if(oper == 'update'){
					///
					
				}
			});
		};
		this.callParent();
		//设置当前的人员条件
		//Ext.getCmp(me.defaultTermId).setValue({'personcode':base.Login.userSession.employeeId
		//            ,'personname':base.Login.userSession.employeeName});
	}
	
});