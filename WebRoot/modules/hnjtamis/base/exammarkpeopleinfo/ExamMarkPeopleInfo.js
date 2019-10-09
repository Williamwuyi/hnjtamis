/**
 * 阅卷老师信息维护的模块类
 */
ClassDefine('modules.hnjtamis.base.exammarkpeopleinfo.ExamMarkPeopleInfo', {
	extend : 'base.model.List',
	initComponent : function() {
		var me = this;
		me.form = null;
		// 模块列表对象
		this.columns = [{
					name : 'markPeopleId',
					width : 0
				},{
					name : 'organId',
					width : 0
				},{
					name : 'postId',
					width : 0
				},{
					name : 'userDeptId',
					width : 0
				}, {
					name : 'markPeopleName',
					header : '姓名',
					width : 1
				}, {
					name : 'organName',
					header : '机构名称',
					width : 1
				},{
					name : 'userDeptName',
					header : '部门',
					width : 1
				},{
					name : 'postName',
					header : '岗位',
					width : 1
				}, {
					name : 'professionName',
					header : '专业',
					width : 2
				}];
		// 模块查询条件对象
		this.terms = [{
					xtype : 'textfield',
					name : 'titleTerm',
					fieldLabel : '阅卷老师名称',
					labelWidth : 100,
					width:300
				}];
		this.keyColumnName = "markPeopleId";// 主健属性名
		this.viewOperater = true;
		this.addOperater = true;
		this.deleteOperater = true;
		this.updateOperater = true;
		this.otherOperaters = [];//其它扩展按钮操作
		this.otherOperaters.push("");
		this.listUrl = "base/exammarkpeopleinfo/listForExamMarkPeopleInfoListAction!list.action";// 列表请求地址
		this.deleteUrl = "base/exammarkpeopleinfo/deleteForExamMarkPeopleInfoListAction!delete.action";// 删除请求地址
		/*
		var sortConfig = {};
		//列属性配置复制
		sortConfig.columns = new Array(this.columns.length);
		for(var i=0;i<this.columns.length;i++){
		   sortConfig.columns[i] = Ext.clone(this.columns[i]);
		   delete sortConfig.columns[i].xtype;
		}
		//打开排序页面
		this.openSortWin = function(record,term,store,callback){
			var me = this;
			sortConfig.keyColumnName = me.keyColumnName;
			sortConfig.sortlistUrl = me.listUrl+"?"+me.termForm.getValues(true)+"&limit=0";
			sortConfig.saveSortUrl = 'base/exammarkpeopleinfo/saveSortForExamMarkPeopleInfoFormAction!saveSort.action';
			sortConfig.callback = callback;
			var sortPanel = ClassCreate('base.model.SortList', Ext.clone(sortConfig));
			// 表单窗口
			var sortWin = new WindowObject({
						layout : 'fit',
						title : '题型排序',
						height : 500,
						width : 700,
						border : false,
						frame : false,
						modal : true,// 模态
						closeAction : 'hide',
						items : [sortPanel]
					});
			sortWin.show();
		}
		*/
		// 打开表单页面方法
		this.openFormWin = function(id, callback, readOnly,data,term,oper) {
			var formConfig = {};
			var readOnly = readOnly || false;
			formConfig.readOnly = readOnly;
			//formConfig.fileUpload = true;
			formConfig.formUrl = "base/exammarkpeopleinfo/saveForExamMarkPeopleInfoFormAction!save.action";
			formConfig.findUrl = "base/exammarkpeopleinfo/findForExamMarkPeopleInfoFormAction!find.action";
			formConfig.clearButtonEnabled = false;
			formConfig.callback = callback;
			formConfig.columnSize = 2;
			formConfig.jsonParemeterName = 'form';
			formConfig.oper = oper;// 复制操作类型变量
			formConfig.items = new Array();
			formConfig.items.push({
						xtype : 'hidden',
						name : 'markPeopleId'
					});
			//=====================================================================
			formConfig.items.push({
				colspan : 1,
				fieldLabel : '机构',
				name : 'organ',
				xtype : 'selecttree',
				allowBlank : false,
				addPickerWidth:200,
				readOnly : readOnly,
				nameKey : 'organId',
				nameLable : 'organName',
				readerRoot : 'organs',
				keyColumnName : 'organId',
				titleColumnName : 'organName',
				childColumnName : 'organs',
				selectUrl : "organization/organ/listForOrganListAction!list.action?topOrganTerm="+base.Login.userSession.currentOrganId,
				selectEventFun:function(combo,record,index){
					var deptField = me.form.getForm().findField('dept');
					deptField.reflash("organization/dept/listForDeptListAction!list.action?organTerm="+(combo.value));
					var employeeField = me.form.getForm().findField('employee');
					employeeField.reflash('organization/employee/listForEmployeeListAction!list.action?organTerm='+combo.value+"&limit=999");
				}
			});
			formConfig.items.push({
				colspan : 1,
				fieldLabel : '部门',
				name : 'dept',
				xtype : 'selecttree',
				readOnly : readOnly,
				addPickerWidth:200,
				nameKey : 'deptId',
				nameLable : 'deptName',
				readerRoot : 'depts',
				allowBlank : false,
				keyColumnName : 'deptId',
				titleColumnName : 'deptName',
				childColumnName : 'depts',
				selectUrl : "organization/dept/listForDeptListAction!list.action?organTerm="+(data['organId']?data['organId']:base.Login.userSession.currentOrganId),
				selectEventFun:function(combo,record,index){
					var employeeField = me.form.getForm().findField('employee');
					employeeField.reflash("organization/employee/listForEmployeeListAction!list.action?deptTerm="+(combo.value||'')
					                     +'&organTerm='+me.form.getForm().findField('organ').getValue().organId+"&limit=999");
					var quarterField = me.form.getForm().findField('quarter');
					quarterField.reflash("organization/quarter/listForQuarterListAction!list.action?deptTerm="+(combo.value));
				}
			});
			formConfig.items.push({
				colspan : 1,
				fieldLabel : '岗位',
				name : 'quarter',
				addPickerWidth:200,
				xtype : 'selecttree',
				readOnly : readOnly,
				allowBlank : false,
				nameKey : 'quarterId',
				nameLable : 'quarterName',
				readerRoot : 'quarters',
				keyColumnName : 'quarterId',
				titleColumnName : 'quarterName',
				childColumnName : 'quarters',
				selectUrl : "organization/quarter/listForQuarterListAction!list.action?deptTerm="+(data['userDeptId']?data['userDeptId']:null),
				selectEventFun:function(combo,record,index){
					var employeeField = me.form.getForm().findField('employee');
					employeeField.reflash("organization/employee/listForEmployeeListAction!list.action?deptTerm="+me.form.getForm().findField('dept').getValue().deptId
					                     +'&organTerm='+me.form.getForm().findField('organ').getValue().organId
					                     +'&quarterTerm='+(combo.value)+"&limit=999");
					
				}
			});
			formConfig.items.push({
				colspan : 1,
				fieldLabel : '姓名',
				name : 'employee',
				xtype : 'selectobject',
				readOnly : readOnly,
				valueField : 'employeeId',
				displayField : 'employeeName',
				readerRoot : 'list',
				allowBlank : false,
				enableSelectOne : false,//缺省不选择员工
				selectUrl : 'organization/employee/listForEmployeeListAction!list.action?organTerm='
				           +(data['organId']?data['organId']:base.Login.userSession.currentOrganId)
				           +'&deptTerm='+(data['userDeptId']?data['userDeptId']:base.Login.userSession.currentDeptId+"&limit=999")
			});
			/*formConfig.items.push({
				colspan : 1,
				fieldLabel : '姓名',
				name : 'markPeopleName',
				allowBlank : false,
				readOnly : readOnly
			});*/
			
			formConfig.items.push({
				colspan : 1,
				fieldLabel : '性别',
				xtype : 'radiogroup',
				items:[
				       {
				    	   boxLabel : '男',
				    	   name : 'userSex',
				    	   inputValue :'1'
				       },
				       {
				    	   boxLabel : '女',
				    	   name : 'userSex',
				    	   inputValue :'2'
				       }
				       ],
				width:240,
				allowBlank : false,
				readOnly : readOnly
			});
			formConfig.items.push({
				colspan : 1,
				name : 'userBirthday',
				fieldLabel : '出生年月',
				xtype : 'datefield',
				format : 'Y-m-d',
				readOnly : readOnly
			});
			formConfig.items.push({
				colspan : 1,
				name : 'userNation',
				fieldLabel : '名族',
				xtype : 'textfield',
				readOnly : readOnly
			});
			formConfig.items.push({
				colspan : 1,
				name : 'userPhone',
				fieldLabel : '联系电话',
				xtype : 'textfield',
				readOnly : readOnly
			});
			formConfig.items.push({
				colspan : 2,
				name : 'userAddr',
				fieldLabel : '住址',
				xtype : 'textfield',
				readOnly : readOnly,
				width : 537
			});
			formConfig.items.push({
				colspan : 2,
				allowBlank:false,
				fieldLabel : '专业',
				name : 'specialitys',
				addPickerWidth:100,
				xtype : 'selecttree',
				checked : true,
				readOnly : readOnly,
				nameKey : 'specialityid',
				nameLable : 'specialityname',
				readerRoot : 'children',
				keyColumnName : 'id',
				titleColumnName : 'title',
				childColumnName : 'children',
				selectType : 'speciality',
				selectTypeName : '专业',
				selectUrl : 'baseinfo/speciality/treeForSpecialityListAction!tree.action',
				width : 537
			});
			
			me.form = ClassCreate('base.model.Form', formConfig);
			me.form.parentWindow = this;
			// 表单窗口
			var formWin = new WindowObject({
						layout : 'fit',
						title : '阅卷老师信息维护',
						width : 600,
						border : false,
						frame : false,
						modal : true,// 模态
						closeAction : 'hide',
						items : [me.form]
					});	
			formWin.show();
			me.form.setFormData(id,function(result){
			});
		};
		this.callParent();
	}
});