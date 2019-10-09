/**
 * 启动流程的表单类
 */
ClassDefine('modules.workflow.StartFlow', {
	extend : 'base.model.Form',	
	initComponent : function() {
			var formConfig = this;
			formConfig.formUrl = "workflow/startForFlowTestAction!start.action";
			formConfig.columnSize = 1;
			formConfig.saveButtonEnabled = false;
			formConfig.clearButtonEnabled = false;
			formConfig.fieldDefaults = {
				labelAlign : 'right',
				labelWidth : 90
			}
			formConfig.items=[];
			formConfig.items.push({
						colspan : 1,
						fieldLabel : '工作流',
						name : 'flowCode',
						selectUrl:'workflow/flowMapForFlowTestAction!flowMap.action',
						xtype : 'select',
						allowBlank : false,
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
			var flowNode = Ext.widget('editlist',{
						colspan : 1,
						fieldLabel : '业务参数',
						name : 'param',
						addOperater : true,
						deleteOperater : true,
						viewConfig:{height:160},//高度
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
						readOnly : this.readOnly
					});
		    formConfig.items.push(flowNode);
		    var me = this;
		    this.otherOperaters = [];//其它扩展按钮操作
		        this.otherOperaters.push({
	                xtype : 'button',
					icon : 'resources/icons/fam/tick.png',
					text : '确定',
					handler : function() {
						 if (me.getForm().isValid()&&
						   (!me.validate||me.validate(me.getForm().getValues(false)))){
							me.MaskTip.show();
							me.getForm().submit({
								success : function(form, action) {
									var msg = action.result.msg;
									if (!msg)
										msg = '操作成功！';
								    me.MaskTip.hide();
									Ext.Msg.alert('提示', msg,function(){
										me.ownerCt.parentPanel.termQueryFun(false,'flash');
										me.ownerCt.close();										
									});
								},
								failure : function(form, action) {
									if (action.result && action.result.length > 0)
										Ext.Msg.alert('错误提示',
												action.result[0].errors);
									else
										Ext.Msg.alert('信息', '后台未响应，网络异常！');
								    me.MaskTip.hide();
								}
							});
						}
					}
				});
			this.callParent();
		}
});