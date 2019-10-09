/**
 * 系统公告的模块类
 */
ClassDefine('modules.organization.Employee', {
	extend : 'base.model.List',
	initComponent : function() {
		var me = this;
		me.op = me.op || 'organ';
		// 模块列表对象
		this.columns = [{
					name : 'employeeId',
					width : 0
				}, {
					name : 'employeeName',
					header : '姓名',
					width : 2
				}, {
					name : 'employeeCode',
					header : '员工编号',
					width : 2
				}, {
					name : 'validation',
					header : '是否有效',
					width : 1,
					renderer : function(value){
						return value==false?"无效":"有效";
					}
				}, /*{
					name : 'quarter.quarterName',
					header : '主岗位',
					width : 2
				}, */{
					name : 'quarterTrainName',
					header : '培训岗位',
					width : 2
				}, {
					name : 'dept.deptName',
					header : '所在部门',
					width : 3
				}, {
					name : 'dept.organ.organName',
					header : '所在机构',
					width : 4
				}];
		// 模块查询条件对象
		me.stTypeSelect = Ext.widget('select',{
			fieldLabel:"是否有标准岗位",
			name:'stTypeTerm',
			xtype : 'select',
			data:[[null,'全部'],['haveSt','有标准岗位'],['noneSt','没有标准岗位']],
			labelWidth : 125,
			width:260
		});
		var deptTermUrl = "organization/dept/odtreeForDeptListAction!odtree.action?organTerm="+base.Login.userSession.currentOrganId;
		if(me.op == 'dept'){
			deptTermUrl= "organization/dept/ownerDeptTreeForDeptListAction!ownerDeptTree.action";
		}
		this.termWidth = 350;
		me.deptTermObj = Ext.widget('selecttree',{
					name : 'deptTerm',
					fieldLabel : '机构部门',
					addPickerWidth:100,
					width:320,
					labelWidth : 90,
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
					selectUrl : deptTermUrl,
					nameTermWidth : 250,
					selectEventFun:function(combo){
						var quarterField = me.termForm.getForm().findField('quarterTerm');
						quarterField.reflash("organization/quarter/listForQuarterListAction!list.action?deptTerm="+(combo.value));
					}
				});
		this.terms = [
				me.stTypeSelect,
				me.deptTermObj,/*{
					name : 'quarterTerm',
					fieldLabel : '岗位',
					addPickerWidth:200,
					xtype : 'selecttree',
					nameKey : 'quarterId',
					nameLable : 'quarterName',
					readerRoot : 'quarters',
					keyColumnName : 'quarterId',
					titleColumnName : 'quarterName',
					childColumnName : 'quarters',
					editorType:'str',//编辑类型为字符串，不是对象
					selectUrl : "organization/quarter/listForQuarterListAction!list.action"
				},*/
				
				,{
					name : 'quarterTrainTerm',
					fieldLabel : '岗位',
					addPickerWidth:100,
					labelWidth : 90,
					width:320,
					xtype : 'selecttree',
					nameKey : 'id',
					nameLable : 'title',
					readerRoot : 'children',
					editorType:'str',//编辑类型为字符串，不是对象
					keyColumnName : 'id',
					titleColumnName : 'title',
					childColumnName : 'children',
					selectType : 'quarter',
					selectTypeName : '岗位',
					nameTermWidth : 250,
					selectUrl : "organization/quarter/getQuarterStandardExForQuarterListAction!getQuarterStandardEx.action"
				},{
					xtype : 'textfield',
					name : 'nameTerm',
					labelWidth : 90,
					fieldLabel : '姓名'
				}, {
					fieldLabel : '员工编号',
					labelWidth : 90,
					name : 'codeTerm'
				}, 
				{
					xtype :'select',
					fieldLabel : '是否有效',
					name : 'validStr',
					labelWidth : 90,
					data:[['','所有'],['0','否'],['1','是']]
				}];
		this.keyColumnName = "employeeId";// 主健属性名
		this.viewOperater = true;
		this.addOperater = true;
		this.deleteOperater = true;
		this.updateOperater = true;
		this.listUrl = "organization/employee/listForEmployeeListAction!list.action?op="+me.op+"&organTerm="+base.Login.userSession.currentOrganId;// 列表请求地址
		this.deleteUrl = "organization/employee/deleteForEmployeeListAction!delete.action?op="+me.op;// 删除请求地址
		
		this.otherOperaters = [];
		me.queryFatBt = Ext.create("Ext.Button", {
				xtype : 'button',//按钮类型
				iconCls : 'query',
				text : "查询无标准岗位员工",//按钮标题
				timeout: 60000,
				handler : function() {
					me.termFormWin.close();
					me.stTypeSelect.setValue('noneSt');
					me.termQueryFun(true,'first');	
				}
			});
			this.otherOperaters.push(me.queryFatBt);
			
			/*me.queryAllBt = Ext.create("Ext.Button", {
				xtype : 'button',//按钮类型
				icon : 'resources/icons/fam/table_refresh.png',//按钮图标
				text : "显示全部员工",//按钮标题
				timeout: 60000,
				handler : function() {
					me.termFormWin.close();
					me.stTypeSelect.setValue(null);
					me.termQueryFun(true,'first');	
				}
			});
			this.otherOperaters.push(me.queryAllBt);*/
		/*var sortConfig = {};	
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
			sortConfig.sortlistUrl = me.listUrl+"&"+me.termForm.getValues(true)+"&limit=0";
			sortConfig.saveSortUrl = 'organization/employee/saveSortForEmployeeFormAction!saveSort.action';
			sortConfig.callback = callback;
			var sortPanel = ClassCreate('base.model.SortList', Ext.clone(sortConfig));
			// 表单窗口
			var sortWin = new WindowObject({
						layout : 'fit',
						title : '员工排序',
						height : 500,
						width : 700,
						border : false,
						frame : false,
						modal : true,// 模态
						closeAction : 'hide',
						items : [sortPanel]
					});
			sortWin.show();
		}*/
		// 打开表单页面方法
		this.openFormWin = function(id, callback, readOnly,data,term,oper) {
			var me = this;
			var formConfig = {};
			var readOnly = readOnly || false;
			formConfig.readOnly = readOnly;
			formConfig.fileUpload = true;
			formConfig.formUrl = "organization/employee/saveForEmployeeFormAction!save.action";
			formConfig.findUrl = "organization/employee/findForEmployeeFormAction!find.action";
			formConfig.callback = callback;
			formConfig.columnSize = 2;
			formConfig.jsonParemeterName = 'form';
			formConfig.items = new Array();
			formConfig.oper=oper;//复制操作类型变量
			formConfig.items.push({
						colspan : 2,
						xtype : 'hidden',
						name : 'employeeId'
					});
			formConfig.items.push({
						colspan : 2,
						xtype : 'hidden',
						name : 'oldQuarterTrainId'
					});
			formConfig.items.push({
						colspan : 1,
						fieldLabel : '员工名称',
						name : 'employeeName',
						xtype : 'textfield',
						allowBlank : false,
						readOnly : readOnly,
						labelWidth:120,
						maxLength : 32
					});
			formConfig.items.push({
						colspan : 1,
						fieldLabel : '员工简拼',
						name : 'simpleName',
						xtype : 'textfield',
						readOnly : readOnly,
						labelWidth:120,
						maxLength : 32
					});
			formConfig.items.push({
						colspan : 1,
						fieldLabel : '曾用名',
						name : 'alias',
						xtype : 'textfield',
						readOnly : readOnly,
						labelWidth:120,
						maxLength : 32
					});
			formConfig.items.push({
						colspan : 1,
						fieldLabel : '员工编码',
						name : 'employeeCode',
						xtype : 'textfield',
						allowBlank : false,
						readOnly : readOnly,
						labelWidth:120,
						maxLength : 32
					});
			var deptSelectUrl = "organization/dept/odtreeForDeptListAction!odtree.action?organTerm="+base.Login.userSession.currentOrganId;
			if(me.op == 'dept'){
				deptSelectUrl= "organization/dept/ownerDeptTreeForDeptListAction!ownerDeptTree.action";
			}
			me.deptSelect = Ext.widget('selecttree',{
						colspan : 2,
						fieldLabel : '部门/班组',
						name : 'dept',
						labelWidth:120,
						//addPickerWidth:200,
						width : 575,
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
						selectUrl : deptSelectUrl,
						nameTermWidth : 250,
						selectEventFun:function(combo,record,index){
							
							//var quarterField = form.getForm().findField('quarter');
							//quarterField.reflash("organization/quarter/listForQuarterListAction!list.action?deptTerm="+(combo.value));
						}
			  });
			formConfig.items.push(me.deptSelect);
			formConfig.items.push({
						colspan : 1,
						fieldLabel : '性别',
						name : 'sex',
						xtype : 'select',
						allowBlank : false,
						readOnly : readOnly,
						labelWidth:120,
						maxLength : 32,
						data : [[0,'男'],[1,'女']]
					});
			formConfig.items.push({
						colspan : 1,
						labelWidth:120,
						fieldLabel : '培训标准岗位',
						checked : false,
						addPickerWidth:170,
						name : 'quarterStandard',
						xtype : 'selecttree',
						readOnly : readOnly,
						nameKey : 'quarterId',
						nameLable : 'quarterName',
						//readerRoot : 'children',
						keyColumnName : 'id',
						titleColumnName : 'title',
						childColumnName : 'children',
						nameTermWidth : 250,
						selectType : 'quarter',
						selectTypeName : '培训标准岗位',
						selectUrl : "organization/quarter/getQuarterStandardExForQuarterListAction!getQuarterStandardEx.action",
						allowBlank : false
					});
		    /*formConfig.items.push({
						colspan : 1,
						fieldLabel : '主岗位',
						name : 'quarter',
						addPickerWidth:200,
						xtype : 'selecttree',
						readOnly : readOnly,
						allowBlank : true,
						nameKey : 'quarterId',
						nameLable : 'quarterName',
						readerRoot : 'quarters',
						keyColumnName : 'quarterId',
						titleColumnName : 'quarterName',
						childColumnName : 'quarters',
						selectUrl : "organization/quarter/listForQuarterListAction!list.action?deptTerm="+(term?term.deptTerm:"")
					});*/
			/*formConfig.items.push({
						colspan : 1,
						checked : true,
						fieldLabel : '其它岗位',
						name : 'employeeQuarters',
						addPickerWidth:200,
						xtype : 'selecttree',
						readOnly : readOnly,
						nameKey : 'quarterId',
						nameLable : 'quarterName',
						//readerRoot : 'quarters',
						keyColumnName : 'id',
						titleColumnName : 'title',
						childColumnName : 'children',
						selectUrl : "organization/quarter/odqtreeForQuarterListAction!odqtree.action?organTerm="+base.Login.userSession.currentOrganId
					});*/
			formConfig.items.push({
						colspan : 1,
						fieldLabel : '民族',
						name : 'nationality',
						xtype : 'textfield',
						readOnly : readOnly,
						labelWidth:120,
						maxLength : 32
					});
			formConfig.items.push({
						colspan : 1,
						fieldLabel : '出生日期',
						name : 'birthday',
						xtype : 'datefield',
						format:'Y-m-d',
						readOnly : readOnly,
						labelWidth:120,
						maxLength : 32
					});
		    formConfig.items.push({
						colspan : 1,
						fieldLabel : '籍贯',
						name : 'nativeplace',
						xtype : 'textfield',
						readOnly : readOnly,
						labelWidth:120,
						maxLength : 32
					});
			 formConfig.items.push({
						colspan : 1,
						fieldLabel : '身份证号',
						name : 'identityCard',
						xtype : 'textfield',
						readOnly : readOnly,
						labelWidth:120,
						maxLength : 32
					});
		    formConfig.items.push({
						colspan : 1,
						fieldLabel : '办公电话',
						name : 'officePhone',
						xtype : 'textfield',
						readOnly : readOnly,
						labelWidth:120,
						maxLength : 32
					});
			formConfig.items.push({
						colspan : 1,
						fieldLabel : '住宅电话',
						name : 'addressPhone',
						xtype : 'textfield',
						readOnly : readOnly,
						labelWidth:120,
						maxLength : 32
					});
			formConfig.items.push({
						colspan : 1,
						fieldLabel : '移动电话',
						name : 'movePhone',
						xtype : 'textfield',
						readOnly : readOnly,
						labelWidth:120,
						maxLength : 32
					});
		    formConfig.items.push({
						colspan : 1,
						fieldLabel : '传真',
						name : 'fax',
						xtype : 'textfield',
						readOnly : readOnly,
						labelWidth:120,
						maxLength : 32
					});
			formConfig.items.push({
						colspan : 1,
						fieldLabel : '传真',
						name : 'email',
						xtype : 'textfield',
						readOnly : readOnly,
						labelWidth:120,
						maxLength : 32
					});
			formConfig.items.push({
						colspan : 1,
						fieldLabel : '住址',
						name : 'address',
						xtype : 'textfield',
						readOnly : readOnly,
						labelWidth:120,
						maxLength : 32
					});
		    formConfig.items.push({
						colspan : 1,
						fieldLabel : '邮政编码',
						name : 'postalCode',
						xtype : 'textfield',
						readOnly : readOnly,
						labelWidth:120,
						maxLength : 32
					});
			formConfig.items.push({
						colspan : 1,
						fieldLabel : '是否有效',
						name : 'validation',
						xtype : 'select',
						data:[[false,'否'],[true,'是']],
						labelWidth:120,
						readOnly : readOnly,
						defaultValue : true
					});
			formConfig.items.push({
						colspan : 2,
						fieldLabel : '备注',
						name : 'remark',
						xtype : 'textarea',
						readOnly : readOnly,
						labelWidth:120,
						maxLength : 100,
						width:575,
						height : 70
					});
            formConfig.items.push({
						colspan : 1,
						name : 'orderNo',
						xtype : 'hidden',
						value : 0
					});
		    formConfig.items.push({
						colspan : 1,
						name : 'levelCode',
						xtype : 'hidden'
					});
			me.form = ClassCreate('base.model.Form', formConfig);
			// 表单窗口
			var formWin = new WindowObject({
						layout : 'fit',
						title : '员工',
						//height : 500,
						autoHeight : true,
						width : 610,
						border : false,
						frame : false,
						modal : true,// 模态
						closeAction : 'hide',
						items : [me.form]
					});
			formWin.show();
			me.form.setFormData(id,function(result){
				if(oper == 'add'){
					if(term){
					   var v = {'dept':{'deptId':base.Login.userSession.currentDeptId,'deptName':''}};
					   //form.getForm().findField('quarter').reflash(
					       //"organization/quarter/listForQuarterListAction!list.action?deptTerm="+term.deptTerm);
					   me.form.getForm().setValues(v);
					}
				}else
				if(oper == 'update'){
					//form.getForm().findField('quarter').reflash(
					       //"organization/quarter/listForQuarterListAction!list.action?deptTerm="+result.form.dept.deptId,false);
				}
				me.setDeptShow();
			});
		};
		me.callParent();
		me.deptTermObj.setValue({'deptId':base.Login.userSession.currentOrganId,
		       'deptName':base.Login.userSession.currentOrganName});
	},
	setDeptShow : function(){
		var me = this;
		var dept = me.form.getForm().findField('dept').getValue();
		if(dept){
			var deptName = dept.deptName;
			var deptId = dept.deptId;
			EapAjax.request({
				method : 'GET',
				url : 'organization/dept/getAllShowDeptNameForDeptListAction!getAllShowDeptName.action?id='+deptId,
				async : true,
				success : function(response) {
					var result = Ext.decode(response.responseText);
					if(result && result.msg && result.msg!=null && result.msg!=""){
						me.deptSelect.setRawValue(result.msg);
					}else{
						me.deptSelect.setRawValue(deptName);
					}
				},
				failure : function() {
					me.deptSelect.setRawValue(deptName);
				}
			});
		}
	}
});