/**
 * desc :岗位培训达标系统-个人学习模块-个人岗位达标记录查询
 * date :2015.4.9
 * author:wangyong
 */
ClassDefine('modules.hnjtamis.personal.PersonalRateProgress', {
	extend : 'base.model.List',
	initComponent : function() {
		var me = this;
		// 模块列表对象
		this.columns = [{
					name : 'rateid',
					width : 0
				}, {
					name : 'personname',
					header : '姓名',
					width : 1
				}, {
					name : 'isreachthestd',
					header : '是否达标',
					width : 1,
					renderer : function(value){
						return value==1?"是":"否";
					}
				}, {
					name : 'totalscore',
					header : '学时（得分）',
					width : 1
				}, {
					name : 'reachetype',
					header : '达标类型',
					width : 1,
					renderer : function(value){
						return value==1?"学时达标":"考试达标";
					}
				}, {
					name : 'reachtime',
					header : '达标日期',
					width : 1
				},{
					name : 'timeoverdue',
					header : '过期时间',
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
					fieldLabel : '创建日期开始',
					name : 'startTimeTerm',
					xtype : 'datefield',
					format : 'Y-m-d'
				}, {
					fieldLabel : '创建日期结束',
					name : 'endTimeTerm',
					xtype : 'datefield',
					format : 'Y-m-d'
				}];
		this.keyColumnName = "rateid";// 主健属性名
		this.viewOperater = true;
		this.addOperater = true;
		this.deleteOperater = true;
		this.updateOperater = true;
		
		this.listUrl = "personal/rateprogress/listForPersonalRateProgressListAction!list.action";// 列表请求地址
		this.deleteUrl = "personal/rateprogress/deleteForPersonalRateProgressListAction!delete.action";// 删除请求地址
		// 打开表单页面方法
		this.openFormWin = function(id, callback, readOnly,data,term,oper) {
			var formConfig = {};
			var readOnly = readOnly || false;
			formConfig.readOnly = readOnly;
			formConfig.fileUpload = true;
			formConfig.formUrl = "personal/rateprogress/saveForPersonalRateProgressFormAction!save.action";
			formConfig.findUrl = "personal/rateprogress/findForPersonalRateProgressFormAction!find.action";
			formConfig.callback = callback;
			formConfig.columnSize = 1;
			formConfig.jsonParemeterName = 'form';
			formConfig.items = new Array();
			formConfig.oper = oper;// 复制操作类型变量
			formConfig.items.push({
						colspan : 1,
						xtype : 'hidden',
						name : 'rateid'
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
						fieldLabel : '达标类型',
						name : 'reachetype',
						xtype : 'select',
						data:[[1,'学时达标'],[2,'考试达标']],
						readOnly : readOnly,
						defaultValue : 1
					});
			formConfig.items.push({
						colspan : 1,
						fieldLabel : '是否达标',
						name : 'isreachthestd',
						xtype : 'select',
						data:[[0,'否'],[1,'是']],
						readOnly : readOnly,
						defaultValue : 1
					});
			formConfig.items.push({
						colspan : 1,
						fieldLabel : '过期时间',
						name : 'timeoverdue',
						xtype : 'datetimefield',
						allowBlank : false,
						readOnly : readOnly,
						maxLength : 20,
						format:'Y-m-d'
					});
			formConfig.items.push({
						colspan : 1,
						fieldLabel : '达标日期',
						name : 'reachtime',
						xtype : 'datetimefield',
						allowBlank : true,
						readOnly : readOnly,
						maxLength : 10,
						format:'Y-m-d'
					});
			formConfig.items.push({
						colspan : 1,
						fieldLabel : '目标学时(得分)',
						name : 'targetscore',
						xtype : 'numberfield',
						allowBlank : false,
						readOnly : false,
						maxLength : 10
					});	
			formConfig.items.push({
						colspan : 1,
						fieldLabel : '当前学时(得分)',
						name : 'totalscore',
						xtype : 'numberfield',
						allowBlank : false,
						readOnly : false,
						maxLength : 10
					});	
			formConfig.items.push({
						colspan : 1,
						fieldLabel : '达标目标',
						name : 'contents',
						xtype : 'textarea',
						allowBlank : false,
						readOnly : readOnly,
						maxLength : 250,
						width:540,
						height : 100
					});
			formConfig.items.push({
						colspan : 1,
						name : 'personcode',
						xtype : 'hidden'
					});
			formConfig.items.push({
				colspan : 1,
				name : 'jobscode',
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