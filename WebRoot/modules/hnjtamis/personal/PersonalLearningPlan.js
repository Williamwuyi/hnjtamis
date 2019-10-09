/**
 * desc :岗位培训达标系统-个人学习模块-个人学习计划
 * date :2015.4.13
 * author:wangyong
 */
ClassDefine('modules.hnjtamis.personal.PersonalLearningPlan', {
	extend : 'base.model.List',
	initComponent : function() {
		var me = this;
		// 模块列表对象
		this.columns = [{
					name : 'plpid',
					width : 0
				}, {
					name : 'personname',
					header : '姓名',
					width : 1
				}, {
					name : 'planstatus',
					header : '是否完成',
					width : 1,
					renderer : function(value){
						return value==1?"是":"否";
					}
				}, {
					name : 'planstarttime',
					header : '计划开始时间',
					width : 1
				}, {
					name : 'planendtime',
					header : '计划结束时间',
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
		this.keyColumnName = "plpid";// 主健属性名
		this.viewOperater = true;
		this.addOperater = true;
		this.deleteOperater = true;
		this.updateOperater = true;
		
		this.listUrl = "personal/learningplan/listForPersonalLearningPlanListAction!list.action";// 列表请求地址
		this.deleteUrl = "personal/learningplan/deleteForPersonalLearningPlanListAction!delete.action";// 删除请求地址
		// 打开表单页面方法
		this.openFormWin = function(id, callback, readOnly,data,term,oper) {
			var formConfig = {};
			var readOnly = readOnly || false;
			formConfig.readOnly = readOnly;
			formConfig.fileUpload = true;
			formConfig.formUrl = "personal/learningplan/saveForPersonalLearningPlanFormAction!save.action";
			formConfig.findUrl = "personal/learningplan/findForPersonalLearningPlanFormAction!find.action";
			formConfig.callback = callback;
			formConfig.columnSize = 1;
			formConfig.jsonParemeterName = 'form';
			formConfig.items = new Array();
			formConfig.oper = oper;// 复制操作类型变量
			formConfig.items.push({
						colspan : 1,
						xtype : 'hidden',
						name : 'plpid'
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
						fieldLabel : '计划名称',
						name : 'planname',
						xtype : 'textfield',
						allowBlank : false,
						readOnly : readOnly,
						maxLength : 50
					});
			formConfig.items.push({
						colspan : 1,
						fieldLabel : '岗位',
						name : 'thisquarter',
						xtype : 'selecttree',
						nameKey : 'quarterId',
						nameLable : 'quarterName',
						readerRoot : 'children',
						selectType :'quarter',//指定可选择的节点
						selectTypeName:'岗位',
						keyColumnName : 'id',
						addPickerWidth : 120,
						titleColumnName : 'title',
						childColumnName : 'children',
						///editorType:'str',//编辑类型为字符串，不是对象
						selectUrl : "organization/quarter/odqtreeForQuarterListAction!odqtree.action?organTerm="+base.Login.userSession.currentOrganId,
					});
			formConfig.items.push({
						colspan : 1,
						fieldLabel : '是否完成',
						name : 'planstatus',
						xtype : 'select',
						data:[[0,'否'],[1,'是']],
						readOnly : readOnly,
						defaultValue : 0
					});
			formConfig.items.push({
						colspan : 1,
						fieldLabel : '计划开始时间',
						name : 'planstarttime',
						xtype : 'datetimefield',
						allowBlank : false,
						readOnly : readOnly,
						maxLength : 20,
						format:'Y-m-d'
					});
			formConfig.items.push({
						colspan : 1,
						fieldLabel : '计划结束时间',
						name : 'planendtime',
						xtype : 'datetimefield',
						allowBlank : true,
						readOnly : readOnly,
						maxLength : 20,
						format:'Y-m-d'
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
						height : 130
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
					   		'planstarttime':cdate,
					   		'thisquarter':{'quarterId':base.Login.userSession.quarterId,'quarterName':base.Login.userSession.quarterName}});
					  // form.getForm().setValues({'thisquarter':{'quarterId':(data?data.deptId:''),'deptName':(data?data.deptName:'')},
					  //                           'organ':{'organId':term.organTerm,'organName':''}});
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