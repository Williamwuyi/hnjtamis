/**
 * 课件库
 */
ClassDefine('modules.hnjtamis.invigilate.InvigilateLog', {
	extend : 'base.model.List',
	initComponent : function() {
		var me = this;
		// 模块列表对象
		this.columns = [{
			name : 'examId',
			width : 0
		}, {
			name : 'examName',
			header : '考试科目',
			width : 3
		}, {
			name : 'examStartTime',
			header : '考试开始时间',
			width : 1
		}, {
			name : 'examEndTime',
			header : '考试结束时间',
			width : 1
		}, {
			name : 'banTime',
			header : '禁止进入时间',
			width : 1
		}, {
			name : 'beforeTime',
			header : '提前进入时间',
			width : 1
		}, {
			name : 'examState',
			header : '考试状态',
			width : 1,
			renderer : function(value,cellmeta,record,rowIndex,columnIndex,store){
				var v = "";
				EapAjax.request({
					method : 'GET',
					url : 'exam/invigilate/log/currentTimeForInvigilateLogListAction!currentTime.action',
					async : false,
					success : function(response) {
						var result = Ext.decode(response.responseText);
						var currentTime = new Date(result.msg.replace(/-/g, "/")).getTime();
						var startTime = new Date(record.get("examStartTime").replace(/-/g, "/")).getTime();
						var endTime = new Date(record.get("examEndTime").replace(/-/g, "/")).getTime();
						
						//考试未开始
						if (currentTime < startTime) {
							record.data.examState = 0;
							v = "<font color='red'>未开始</font>";
						} 
						//已结束
						else if (currentTime > endTime) {
							record.data.examState = 2;
							v = "<font color='blue'>已结束</font>";
						} else {
							record.data.examState = 1;
							v = "<font color='green'>正在考试</font>";
						}
					},
					failure : function() {
						//Ext.Msg.alert('信息', '后台未响应，网络异常！');
					}
				});
				return v;
			}
		}];
		// 模块查询条件对象
		this.terms = [];
		this.keyColumnName = "examId";// 主健属性名
		this.viewOperater = false;
		this.addOperater = false;
		this.deleteOperater = false;
		this.updateOperater = false;
		this.jsonParemeterName = "examList";
		//this.readerRoot = 'examList';
		this.otherOperaters = [];//其它扩展按钮操作
		this.otherOperaters.push({
				xtype : 'button',//按钮类型
				icon : 'resources/icons/fam/user_add.gif',//按钮图标
				text : "日志填写",//按钮标题
				handler : function() {//按钮事件
					var examId = me.getSelectIds();
					if(examId == "" || examId.split(",").length>1){
						Ext.Msg.alert('提示', '请选择一条记录！');
						return;
					}
					var selected = me.getSelectionModel().selected;					
					var record = selected.get(0);
					var examState = record.data.examState;
					//未开始考试不允许填写日志					
					if(examState == 0) {
						Ext.Msg.alert('警告','请在开考后填写！');
						return;
					}
					EapAjax.request({
						method : 'GET',
						url : 'exam/invigilate/log/findByExamForInvigilateLogFormAction!findByExam.action?examIdTerm='+examId+'&userIdTerm='+base.Login.userSession.employeeId,
						async : true,
						success : function(response) {
							var result = Ext.decode(response.responseText);
							if(result.form) {
								var readOnly = false;
								if(result.form.status == 1) {
									readOnly = true;
								}
								me.openLogFormWin(result.form.id, examId, readOnly);
							} else {
								me.openLogFormWin("", examId, false);
							}
						},
						failure : function() {
							Ext.Msg.alert('信息', '后台未响应，网络异常！');
						}
					});
					//me.getExamineeForm(id);
				}
			
		});
		
		this.listUrl = "exam/invigilate/log/examListForInvigilateLogListAction!examList.action?invigilaterId="+base.Login.userSession.employeeId;// 列表请求地址
		//this.deleteUrl = "exam/invigilate/log/deleteForTrainPlanListAction!delete.action?organId="+base.Login.userSession.currentOrganId;// 删除请求地址
		// 打开表单页面方法
		me.openLogFormWin = function(id, examId, readOnly) {
			var formConfig = {};
			var readOnly = readOnly || false;
			formConfig.readOnly = readOnly;
			formConfig.fileUpload = true;
			formConfig.formUrl = "exam/invigilate/log/saveForInvigilateLogFormAction!save.action?examIdTerm="+examId;
			formConfig.findUrl = "exam/invigilate/log/findForInvigilateLogFormAction!find.action?examIdTerm="+examId;
			//formConfig.callback = callback;
			formConfig.columnSize = 3;
			formConfig.jsonParemeterName = 'form';
			formConfig.items = new Array();
			formConfig.items.push({
						colspan : 3,
						xtype : 'hidden',
						name : 'id'
					});
			formConfig.items.push({
				colspan : 3,
				xtype : 'hidden',
				name : 'status'
			});
			formConfig.items.push({
				colspan : 3,
				xtype : 'hidden',
				name : 'userId'
			});
			formConfig.items.push({
				colspan : 3,
				xtype : 'hidden',
				name : 'userName'
			});
			formConfig.items.push({
					colspan : 1,
					fieldLabel : '应到人数',
					name : 'planNum',
					xtype : 'numberfield',
					allowBlank : false,
					readOnly : readOnly,
					allowDecimals:false,
					allowNegative:false,
					minValue:0,
					width:170,
					maxLength : 4
				});
			formConfig.items.push({
				colspan : 1,
				fieldLabel : '实到人数',
				name : 'factNum',
				xtype : 'numberfield',
				allowBlank : false,
				readOnly : readOnly,
				allowDecimals:false,
				allowNegative:false,
				minValue:0,
				width:170,
				maxLength : 4
			});
			formConfig.items.push({
				colspan : 1,
				fieldLabel : '作弊人数',
				name : 'cheatNum',
				xtype : 'numberfield',
				allowBlank : false,
				readOnly : readOnly,
				allowDecimals:false,
				allowNegative:false,
				minValue:0,
				width:170,
				maxLength : 4
			});
			formConfig.items.push({
				colspan : 3,
				fieldLabel : '考试情况',
				name : 'examInfo',
				xtype : 'htmleditor',
				allowBlank : false,
				readOnly : readOnly,
				maxLength : 4000,
				width:600,
				height : 400
			});
			formConfig.otherOperaters = [];
			formConfig.otherOperaters.push({
				xtype : 'button',//按钮类型
				icon : 'resources/icons/fam/accept.png',//按钮图标
				text : "提交",//按钮标题
				handler : function() {
					Ext.MessageBox.confirm('询问', '提交后不可更改，确定提交监考日志吗？',function(btn){
						if(btn!='yes') return;
						form.getForm().findField("status").setValue(1);
						form.submit();
					});
				}
			});
			var form = ClassCreate('base.model.Form', formConfig);
			form.parentWindow = this;
			// 表单窗口
			var formWin = new WindowObject({
						layout : 'fit',
						title : '监考日志填写',
						height : 500,
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