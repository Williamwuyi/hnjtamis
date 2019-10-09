ClassDefine('modules.hnjtamis.train.MonthPlan', {
	extend : 'base.model.List',
	requires: [
        'modules.baseinfo.Accessory',
        'modules.baseinfo.Dictionary'
    ],
	initComponent : function() {
		var Dictionary = modules.baseinfo.Dictionary;//类别名
		this.categorys = Dictionary.getDictionaryList('TRAIN_CATEGORY');
		var me = this;
		// 模块列表对象
		this.columns = [{
			name : 'id',
			width : 0
		}, {
			name : 'title',
			header : '标题',
			width : 5
		},{
			name : 'deptName',
			header : '部门',
			width : 2
		}, /*{
			name : 'quarter.quarterName',
			header : '岗位',
			width : 2
		}, {
			name : 'speciality.specialityname',
			header : '专业',
			width : 2
		},*/ {
			name : 'month',
			header : '月份',
			width : 1
		}, {
			name : 'category',
			header : '培训类别',
			width : 2,
			renderer : function(value){
				var display = "";
				Ext.Array.each(me.categorys.datas, function(item) {
					if (item['dataKey']==value){
						display = item['dataName'];
					}
				});
				return display;
			}
		}, {
			name : 'trainMode',
			header : '培训方式',
			width : 2,
			renderer : function(value){
				return value=="1"?"在线培训":(value=="2"?"集中培训":"委培");
			}
		}, {
			name : 'status',
			header : '状态',
			width : 1,
			renderer : function(value){
				var status = ['生成', '已上报', '打回', '已审核'];
				return status[value];
			}
		},{
			name : 'distributorName',
			header : '制定人',
			width : 1
		},{
			name : 'auditerName',
			header : '审核人',
			width : 1
		}];
		// 模块查询条件对象
		this.terms = [{
					xtype : 'textfield',
					name : 'titleTerm',
					fieldLabel : '标题'
				},{
					name : 'deptTerm',
					fieldLabel : '机构部门',
					addPickerWidth:200,
					width:300,
					xtype : 'selecttree',
					nameKey : 'deptId',
					nameLable : 'deptName',
					readerRoot : 'children',
					keyColumnName : 'id',
					titleColumnName : 'title',
					childColumnName : 'children',
					editorType:'str',//编辑类型为字符串，不是对象
					//selectType : 'dept',
					//selectTypeName : '部门',
					selectUrl : "organization/dept/odtreeForDeptListAction!odtree.action?organTerm="+base.Login.userSession.currentOrganId,
					selectEventFun:function(combo){
						
					}
				},/*{
					name : 'quarterTerm',
					fieldLabel : '岗位',
					addPickerWidth:40,
					xtype : 'selecttree',
					nameKey : 'quarterId',
					nameLable : 'quarterName',
					//readerRoot : 'quarters',
					keyColumnName : 'id',
					titleColumnName : 'title',
					childColumnName : 'children',
					editorType : 'str',// 编辑类型为字符串，不是对象
					selectType : 'quarter',
					selectTypeName : '岗位',
					selectUrl : "organization/quarter/odqtreeForQuarterListAction!odqtree.action?organTerm="
							+ base.Login.userSession.currentOrganId
				},{
					name : 'specialityTerm',
					fieldLabel : '专业',
					addPickerWidth:40,
					xtype : 'selecttree',
					nameKey : 'specialityid',
					nameLable : 'specialityname',
					//readerRoot : 'quarters',
					keyColumnName : 'id',
					titleColumnName : 'title',
					childColumnName : 'children',
					editorType:'str',//编辑类型为字符串，不是对象
					selectUrl : "baseinfo/speciality/treeForSpecialityListAction!tree.action?jobidTerm="
				},*/{
					xtype : 'textfield',
					name : 'monthTerm',
					fieldLabel : '月份'
				}];
		this.keyColumnName = "id";// 主健属性名
		this.viewOperater = true;
		this.addOperater = true;
		this.deleteOperater = true;
		this.updateOperater = false;
		this.otherOperaters = [];//其它扩展按钮操作
		this.otherOperaters.push(
			{
	            xtype : 'button',
				icon : 'resources/icons/fam/pencil.png',
				text : '修改',
				handler : function() {
					var id = me.getSelectIds();
					if(id==""||id.split(",").length>1){
						Ext.Msg.alert('提示', '请选择一条记录！');
						return;
					}
					var selected = me.getSelectionModel().selected;					
					var record = selected.get(0);
					var status = record.get("status");
					if(status == 0 || status == 2){
						var queryTerm = {};
						if(me.termForm)
							queryTerm = me.termForm.getValues(false);
						me.openFormWin(id, function() {
									me.termQueryFun(false,'flash');//me.pagingToolbar.doRefresh();
								},false,record.data,queryTerm,'update');
					}else{
						Ext.Msg.alert('提示', '该条记录已提交审核或已审核通过，不能进行修改！');
					}
				}
			},
			{
	            xtype : 'button',
				icon : 'resources/icons/fam/moveTop.png',
				text : '提交审核',
				handler : function() {
					var id = me.getSelectIds();
					if(id==""||id.split(",").length>1){
						Ext.Msg.alert('提示', '请选择一条记录！');
						return;
					}
					var selected = me.getSelectionModel().selected;					
					var record = selected.get(0);
					var status = record.get("status");
					if (status == 1 || status == 3) {
						Ext.Msg.alert('提示', '该记录已上报或已审核！');
						return;
					}
					Ext.MessageBox.confirm('询问', '确定提交审核吗？',function(btn){
						if(btn!='yes') return;
						EapAjax.request({
							method : 'GET',
							url : 'train/monthplan/auditForMonthPlanListAction!audit.action?id='+id+'&statusTerm=1',
							async : true,
							success : function(response) {
								var result = Ext.decode(response.responseText);
								if (Ext.isArray(result)&&result[0].errors) {
									var msg = result[0].errors;
									Ext.Msg.alert('错误', msg);
								} else {
									Ext.Msg.alert('信息', '操作成功！');
									me.termQueryFun(false,'flash');
									me.getSelectionModel().clearSelections();
								}
							},
							failure : function() {
								Ext.Msg.alert('信息', '后台未响应，网络异常！');
							}
						});
					});
				}
			},
			{
            xtype : 'button',
			icon : 'resources/icons/fam/moveButtom.png',
			text : '打回',
			handler : function() {
				var id = me.getSelectIds();
				if(id==""||id.split(",").length>1){
					Ext.Msg.alert('提示', '请选择一条记录！');
					return;
				}
				var selected = me.getSelectionModel().selected;					
				var record = selected.get(0);
				var status = record.get("status");
				if (status != 1) {
					Ext.Msg.alert('提示', '该记录未上报或已审核！');
					return;
				}
				Ext.MessageBox.confirm('询问', '确定要打回吗？',function(btn){
					if(btn!='yes') return;
					EapAjax.request({
						method : 'GET',
						url : 'train/monthplan/auditForMonthPlanListAction!audit.action?id='+id+'&statusTerm=2',
						async : true,
						success : function(response) {
							var result = Ext.decode(response.responseText);
							if (Ext.isArray(result)&&result[0].errors) {
								var msg = result[0].errors;
								Ext.Msg.alert('错误', msg);
							} else {
								Ext.Msg.alert('信息', '操作成功！');
								me.termQueryFun(false,'flash');
								me.getSelectionModel().clearSelections();
							}
						},
						failure : function() {
							Ext.Msg.alert('信息', '后台未响应，网络异常！');
						}
					});
				});
			}
		},
		{
            xtype : 'button',
			icon : 'resources/icons/fam/accept.png',
			text : '审核',
			handler : function() {
				var id = me.getSelectIds();
				if(id==""||id.split(",").length>1){
					Ext.Msg.alert('提示', '请选择一条记录！');
					return;
				}
				var selected = me.getSelectionModel().selected;					
				var record = selected.get(0);
				var status = record.get("status");
				if (status != 1) {
					Ext.Msg.alert('提示', '该记录未上报或已审核！');
					return;
				}
				Ext.MessageBox.confirm('询问', '确定审核通过吗？',function(btn){
					if(btn!='yes') return;
					EapAjax.request({
						method : 'GET',
						url : 'train/monthplan/auditForMonthPlanListAction!audit.action?id='+id+'&statusTerm=3',
						async : true,
						success : function(response) {
							var result = Ext.decode(response.responseText);
							if (Ext.isArray(result)&&result[0].errors) {
								var msg = result[0].errors;
								Ext.Msg.alert('错误', msg);
							} else {
								Ext.Msg.alert('信息', '操作成功！');
								me.termQueryFun(false,'flash');
								me.getSelectionModel().clearSelections();
							}
						},
						failure : function() {
							Ext.Msg.alert('信息', '后台未响应，网络异常！');
						}
					});
				});
			}
		},
		{
            xtype : 'button',
			icon : 'resources/icons/fam/accept.png',
			text : '计划执行',
			handler : function() {
				var id = me.getSelectIds();
				if(id==""||id.split(",").length>1){
					Ext.Msg.alert('提示', '请选择一条记录！');
					return;
				}
				var selected = me.getSelectionModel().selected;					
				var record = selected.get(0);
				var status = record.get("status");
				if (status != 3) {
					Ext.Msg.alert('提示', '该记录未审核，不能执行！');
					return;
				}
				
				//打开窗口
				me.executeForm(id, false);
			}
		});
		
		this.listUrl = "train/monthplan/listForMonthPlanListAction!list.action?organId="+base.Login.userSession.currentOrganId;// 列表请求地址
		this.deleteUrl = "train/monthplan/deleteForMonthPlanListAction!delete.action?organId="+base.Login.userSession.currentOrganId;// 删除请求地址
		// 打开表单页面方法
		this.openFormWin = function(id, callback, readOnly,data,term,oper) {
			var formConfig = {};
			var readOnly = readOnly || false;
			formConfig.readOnly = readOnly;
			formConfig.fileUpload = true;
			formConfig.formUrl = "train/monthplan/saveForMonthPlanFormAction!save.action";
			formConfig.findUrl = "train/monthplan/findForMonthPlanFormAction!find.action";
			formConfig.callback = callback;
			formConfig.columnSize = 2;
			formConfig.jsonParemeterName = 'form';
			formConfig.items = new Array();
			formConfig.oper = oper;// 复制操作类型变量
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
			formConfig.items.push({
				colspan : 2,
				xtype : 'hidden',
				name : 'deptName'
			});
			formConfig.items.push({
				colspan : 1,
				fieldLabel : '部门',
				name : 'dept',
				addPickerWidth:200,
				xtype : 'selecttree',
				allowBlank : false,
				readOnly : readOnly,
				nameKey : 'deptId',
				nameLable : 'deptName',
				readerRoot : 'children',
				keyColumnName : 'id',
				titleColumnName : 'title',
				childColumnName : 'children',
				selectType : 'dept',
				selectTypeName : '部门',
				selectUrl : "organization/dept/odtreeForDeptListAction!odtree.action?organTerm="+base.Login.userSession.currentOrganId,
				selectEventFun:function(combo,record,index){
					quarterTree.reflash("organization/quarter/listForQuarterListAction!list.action?deptTerm="+(combo.value));
					//重新选择部门后清空已选岗位信息
					for (var i = 0; i < editTable.store.getCount(); i++) {
						var record = editTable.store.getAt(i);
						record.data['quarter']= {'quarterId':'','quarterName':''};
					}
					editTable.getView().refresh();
				}
			});
			/*formConfig.items.push({
				colspan : 1,
				name: 'quarter',
				fieldLabel : '岗位',
				checked : false,
				allowBlank : false,
				//addPickerWidth:100,
				xtype : 'selecttree',
				readOnly : readOnly,
				nameKey : 'quarterId',
				nameLable : 'quarterName',
				//readerRoot : 'quarters',
				keyColumnName : 'id',
				titleColumnName : 'title',
				childColumnName : 'children',
				selectType:'quarter',
				selectTypeName:'岗位',
				selectUrl : "organization/quarter/odqtreeForQuarterListAction!odqtree.action?organTerm="+base.Login.userSession.currentOrganId
			});
			formConfig.items.push({
				colspan : 1,
				name: 'speciality',
				fieldLabel : '专业',
				checked : false,
				allowBlank : false,
				//addPickerWidth:100,
				xtype : 'selecttree',
				readOnly : readOnly,
				nameKey : 'specialityid',
				nameLable : 'specialityname',
				//readerRoot : 'quarters',
				keyColumnName : 'id',
				titleColumnName : 'title',
				childColumnName : 'children',
				selectType:'speciality',
				selectTypeName:'专业',
				selectUrl : "baseinfo/speciality/treeForSpecialityListAction!tree.action?jobidTerm="
			});*/
			formConfig.items.push({
				colspan : 1,
				fieldLabel : '月份',
				name : 'month',
				//xtype : 'textfield',
				xtype : 'datefield',
				format:'Y-m',
				allowBlank : false,
				readOnly : readOnly,
				//size:30,
				maxLength : 10
			});
			formConfig.items.push(
				modules.baseinfo.Dictionary.createDictionarySelectPanel(
		              '培训类别','TRAIN_CATEGORY','category',true,null,true,readOnly)
		    );
			formConfig.items.push({
				colspan : 1,
				fieldLabel : '培训方式',
				name : 'trainMode',
				xtype : 'select',
				data:[['1','在线培训'],['2','集中培训'],['3','委培']],
				readOnly : readOnly,
				defaultValue : '1'
			});
			/*formConfig.items.push({
				colspan : 2,
				fieldLabel : '培训内容',
				name : 'planContent',
				xtype : 'textarea',
				allowBlank : true,
				readOnly : readOnly,
				maxLength : 4000,
				width:570,
				height : 200
			});*/
			var quarterTree = Ext.widget('selecttree',{
				checked : false,
				allowBlank : false,
				addPickerWidth:200,
				xtype : 'selecttree',
				readOnly : readOnly,
				nameKey : 'quarterId',
				nameLable : 'quarterName',
				readerRoot : 'quarters',
				keyColumnName : 'quarterId',
				titleColumnName : 'quarterName',
				childColumnName : 'quarters',
				selectUrl : "organization/quarter/listForQuarterListAction!list.action?deptTerm=",
				selectEventFun:function(combo,record,index){
					
				}
			});
			var editTable = Ext.widget('editlist',{
				colspan : 2,
				fieldLabel : '培训内容',
				name : 'quarterPlans',
				xtype : 'editlist',
				addOperater : true,
				deleteOperater : true,
				enableMoveButton: false,
				viewConfig:{height:200},//高度
				columns : [{
							width : 0,
							name : 'id'
						}, {
							name : 'quarter',
							header : '岗位',
							editor : quarterTree,
							allowBlank: false,
							renderer:function(value){
							    return value.quarterName;
						    },
							width : 1
						}, {
							header : '培训内容',
							name : 'planContent',
							editor:{
								xtype : 'textarea',
								allowBlank : false,
								maxLength : 4000,
								//width:470,
								height : 100
							},
							width: 3
						}],
				readOnly : readOnly
			});
			formConfig.items.push(editTable);
			formConfig.items.push({
						colspan : 2,
						fieldLabel : '附件',
						name : 'accessories',
						xtype : 'accessory',
						readOnly : readOnly,
						accessoryType : 'single',
						allowMaxSize : 5/*,
						viewConfig:{height:100}//高度*/
					});
			var form = ClassCreate('base.model.Form', formConfig);
			form.parentWindow = this;
			// 表单窗口
			var formWin = new WindowObject({
						layout : 'fit',
						title : '月度计划安排',
						height : 600,
						width : 660,
						border : false,
						frame : false,
						modal : true,// 模态
						closeAction : 'hide',
						items : [form]
					});	
			formWin.show();
			form.setFormData(id,function(result){
				if(oper == 'update'){
					quarterTree.reflash(
					       "organization/quarter/listForQuarterListAction!list.action?deptTerm="+result.form.dept.deptId);
				}
			});
		};
		//计划执行窗口
		me.executeForm = function(id, readOnly) {
			var formConfig = {};
			var readOnly = readOnly || false;
			formConfig.readOnly = readOnly;
			formConfig.fileUpload = true;
			formConfig.formUrl = "train/monthplan/executeForMonthPlanFormAction!execute.action";
			formConfig.findUrl = "train/monthplan/findForMonthPlanFormAction!find.action";
			//formConfig.callback = callback;
			formConfig.columnSize = 1;
			formConfig.jsonParemeterName = 'form';
			formConfig.items = new Array();
			formConfig.items.push({
						colspan : 2,
						xtype : 'hidden',
						name : 'id'
					});
			formConfig.items.push({
						colspan : 1,
						fieldLabel : '标题',
						name : 'title',
						xtype : 'textfield',
						allowBlank : false,
						readOnly : true,
						size:70,
						maxLength : 32
					});
			formConfig.items.push({
				colspan : 1,
				fieldLabel : '月份',
				name : 'month',
				//xtype : 'textfield',
				xtype : 'datefield',
				format:'Y-m',
				allowBlank : false,
				readOnly : true,
				//size:30,
				maxLength : 10
			});
			formConfig.items.push({
				colspan : 1,
				fieldLabel : '是否完成',
				name : 'isFinished',
				xtype : 'select',
				data:[[0,'进行中'], [1,'已完成']],
				readOnly : readOnly,
				defaultValue : 0
			});
			formConfig.items.push({
				colspan : 1,
				fieldLabel : '执行情况',
				name : 'exeContent',
				xtype : 'textarea',
				allowBlank : true,
				readOnly : readOnly,
				maxLength : 250,
				width:570,
				height : 120
			});
			var form = ClassCreate('base.model.Form', formConfig);
			form.parentWindow = this;
			// 表单窗口
			var formWin = new WindowObject({
						layout : 'fit',
						title : '月度计划执行',
						height : 280,
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