/**
 * 执行流程的表单类
 */
ClassDefine('modules.workflow.ExcuteFlow', {
	extend : 'base.model.Form',	
	initComponent : function() {
			var formConfig = this;
			formConfig.formUrl = "workflow/excuteForFlowTestAction!excute.action";
			formConfig.findUrl = "workflow/findTaskForFlowTestAction!findTask.action";
			formConfig.columnSize = 2;
			formConfig.saveButtonEnabled = false;
			formConfig.clearButtonEnabled = false;
			formConfig.fieldDefaults = {
				labelAlign : 'right',
				labelWidth : 90
			}
			formConfig.items=[];
			formConfig.items.push({
						colspan : 1,
						fieldLabel : '流程名称',
						name : 'flowName',
						xtype : 'textfield',
						readOnly : true,
						maxLength : 32
					});
			formConfig.items.push({
						colspan : 1,
						fieldLabel : '任务名称',
						name : 'taskName',
						xtype : 'textfield',
						readOnly : true,
						maxLength : 32
					});
			formConfig.items.push({
						colspan : 1,
						fieldLabel : '操作者',
						xtype:'selecttree',
						name:'employeeId',
						checked : false,
						nameKey : 'employeeId',
						nameLable : 'employeeName',
						keyColumnName : 'id',
						addPickerWidth : 200,
						allowBlank : true,
						titleColumnName : 'title',
						childColumnName : 'children',
						selectType:'employee',
						selectTypeName:'员工',
						editorType:'str',
						selectUrl : "organization/employee/odetreeForEmployeeListAction!odetree.action?organTerm="+base.Login.userSession.organId,
						maxLength : 32
					});
		    formConfig.items.push({
						colspan : 1,
						fieldLabel : '创建时间',
						name : 'createTime',
						xtype : 'textfield',
						readOnly : true,
						maxLength : 20
					});
			formConfig.items.push({
						colspan : 2,
						fieldLabel : '业务地址',
						name : 'url',
						xtype : 'textarea',
						maxLength : 300,
						height:40,
						width:509
					});
			formConfig.items.push({
						colspan : 2,
						fieldLabel : '审核意见',
						name : 'advice',
						xtype : 'textarea',
						maxLength : 300,
						width:509
					});
			formConfig.items.push({
						colspan : 2,
						name : 'toCode',
						xtype : 'hidden'
					});
			formConfig.items.push({
						colspan : 2,
						name : 'excuteId',
						xtype : 'hidden'
					});
			var audit = Ext.widget('editlist',{
						colspan : 2,
						fieldLabel : '审核历史信息',
						name : 'audits',
						readOnly : true,
						viewConfig:{height:100},//高度
						columns : [ {
									name : 'createTime',
									header : '创建时间',
									width : 2
								}, {
									name : 'taskName',
									header : '任务名称',
									width : 3
								}, {
									name : 'employeeName',
									header : '执行者',
									width : 3
								}, {
									name : 'time',
									header : '执行时间',
									width : 2
								}, {
									name : 'to',
									header : '执行结果',
									width : 1
								}, {
									name : 'advice',
									header : '审核意见',
									width : 4
								}]
					});
		    formConfig.items.push(audit);
		    var flowNode = Ext.widget('editlist',{
						colspan : 2,
						fieldLabel : '业务参数',
						name : 'paramInfo',
						addOperater : true,
						deleteOperater : true,
						viewConfig:{height:100},//高度
						columns : [ {
									name : 'value',
									header : '参数名称',
									editor : {
										xtype : 'textfield',
										allowBlank : false,
										maxLength : 32
									},
									width : 2
								}, {
									name : 'text',
									header : '参数数据',
									editor : {
										xtype : 'textfield',
										allowBlank : false,
										maxLength : 32
									},
									width : 3
								}],
						readOnly : false
					});
		    formConfig.items.push(flowNode);
		    formConfig.otherOperaters = [];//其它扩展按钮操作
		    var b = new Array(4);
		    for(var x=0;x<4;x++){
		      b[x]=Ext.widget('button',{text:'b1',
		      handler : function() {
								formConfig.MaskTip.show();
								formConfig.getForm().findField('toCode').setValue(this.toCode);
								var json = Ext.encode(formConfig.getForm().getValues(false).paramInfo);
								formConfig.getForm().findField('json').setValue(json);
								formConfig.getForm().submit({
								success : function(form, action) {
									var msg = action.result.msg;
									if (!msg) msg = '操作成功！';
								    formConfig.MaskTip.hide();
									Ext.Msg.alert('提示', msg,function(){
										formConfig.ownerCt.parentPanel.termQueryFun(false,'flash');
										formConfig.ownerCt.close();										
									});
								},
								failure : function(form, action) {
									if (action.result && action.result.length > 0)
										Ext.Msg.alert('错误提示', action.result[0].errors);
									else
										Ext.Msg.alert('信息', '后台未响应，网络异常！');
								    formConfig.MaskTip.hide();
								}
								});
							}});
		      b[x].hide();
		      formConfig.otherOperaters.push(b[x]);
		    }
			formConfig.jsonParemeterName = 'excute';	
			formConfig.callParent();
			formConfig.setFormData(this.excuteId,function(result){
				var i =1;		
				if(result.excute)
				Ext.Array.each(result.excute.operater, function(r) {
					b[i].setText(r.text);//注意在按钮初始化之后，要修改标题要用setText，不能text=多少
					b[i].toCode = r.value;
					b[i].show();i++;
			    });
			});
		}
});