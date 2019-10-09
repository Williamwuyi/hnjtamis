/**
 * 课件库
 */
ClassDefine('modules.hnjtamis.train.TrainOnline', {
	extend : 'base.model.List',
	requires: [
        'modules.baseinfo.Dictionary'
    ],
	initComponent : function() {
		var me = this;
		// 模块列表对象
		this.columns = [{
			name : 'id',
			width : 0
		}, {
			name : 'trainImplement.subject',
			header : '课程名称',
			width : 5
		}, {
			name : 'trainImplement.duration',
			header : '课时',
			width : 1
		}, {
			name : 'status',
			header : '完成状态',
			width : 1,
			renderer : function(value){
				var ret = ['<font color="red">未开始</font>','<font color="blue">学习中</font>','<font color="green">已完成</font>'];
				return ret[value];
			}
		}, {
			name : 'trainImplement.teacher',
			header : '授课老师',
			width : 1
		}, {
			name : 'trainImplement.remark',
			header : '课程描述',
			width : 2
		}, {
			name : 'trainImplement.trainMode',
			header : '培训方式',
			width : 2,
			renderer : function(value){
				return value=="1"?"在线培训":(value=="2"?"集中培训":"委培");
			}
		}];
		// 模块查询条件对象
		this.terms = [{
					xtype : 'datefield',
					format:'Y-m-d',
					name : 'startDateTerm',
					fieldLabel : '开始日期'
				},{
					xtype : 'datefield',
					format:'Y-m-d',
					name : 'endDateTerm',
					fieldLabel : '结束日期'
				}];
		this.keyColumnName = "id";// 主健属性名
		this.viewOperater = false;
		this.addOperater = false;
		this.deleteOperater = false;
		this.updateOperater = false;
		this.otherOperaters = [];//其它扩展按钮操作
		this.otherOperaters.push({
            xtype : 'button',
			icon : 'resources/icons/fam/rss_go.png',
			text : '进入学习',
			handler : function() {
				var id = me.getSelectIds();
				if(id==""||id.split(",").length>1){
					Ext.Msg.alert('提示', '请选择一条记录！');
					return;
				}
				var ps = window.location.pathname.split("/");
				var url = base.Login.userSession.basePath;
				url = url.replace('localhost:8080/eap',window.location.host+"/"+ps[1]);
				//先关掉窗口
				grablEapLogin.closeWindow("study_click");
				grablEapLogin.createWindow("study_click","课程学习","","" + url + "/modules/hnjtamis/train/studyFrame.jsp?courseId="+id);
				//grablEapLogin.createWindow("studyss","课程学习","","" + url + "train/online/findEmployeeCourseTreeForTrainOnlineListAction!findEmployeeCourseTree.action?courseId="+id);
			}
		});
		
		this.listUrl = "train/online/listForTrainOnlineListAction!list.action?studentTerm=" + base.Login.userSession.employeeId;// 列表请求地址
		this.deleteUrl = "train/plan/deleteForTrainOnlineListAction!delete.action";// 删除请求地址
		// 打开表单页面方法
		this.openFormWin = function(id, callback, readOnly) {
			var formConfig = {};
			var readOnly = readOnly || false;
			formConfig.readOnly = readOnly;
			formConfig.fileUpload = true;
			formConfig.formUrl = "train/online/saveForTrainOnlineFormAction!save.action";
			formConfig.findUrl = "train/online/findForTrainOnlineFormAction!find.action";
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
						title : '在线学习',
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