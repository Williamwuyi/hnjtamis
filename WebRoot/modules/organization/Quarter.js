/**
 * 系统公告的模块类
 */
ClassDefine('modules.organization.Quarter', {
	extend : 'base.model.TreeList',
	requires: ['modules.baseinfo.Dictionary'],
	numberColumnWidth:70,
	initComponent : function() {
		var Dictionary = modules.baseinfo.Dictionary;//类别名
		this.quarterTypes = Dictionary.getDictionaryList('QUARTER_TYPE');
		var me = this;
		// 模块列表对象
		this.columns = [{
					name : 'quarterId',
					width : 0
				}, {
					name : 'quarterName',
					header : '岗位名称',
					xtype : 'treecolumn',
					width : 4
				}, {
					name : 'dept.deptName',
					header : '所在部门',
					width : 2
				}, {
					name : 'quarterCode',
					header : '岗位编码',
					width : 3
				}, {
					name : 'quarterTrainName',
					header : '培训岗位名称',
					width : 4
				}, {
					name : 'quarterTrainCode',
					header : '培训岗位编码',
					width : 3
				}, /*{
					name : 'quarterType',
					header : '岗位类型',
					width : 2,
					renderer : function(value){
						var display = "";
						Ext.Array.each(me.quarterTypes.datas, function(item) {
							if (item['dataKey']==value){
								display = item['dataName'];
							}
						});
						return display;
					}
				}, */{
					name : 'validation',
					header : '是否有效',
					width : 1,
					renderer : function(value){
						return value==false?"无效":"有效";
					}
				}];
		// 模块查询条件对象
		this.terms = [{
					name : 'deptTerm',
					fieldLabel : '机构部门',
					xtype : 'selecttree',
					nameKey : 'deptId',
					nameLable : 'deptName',
					readerRoot : 'children',
					addPickerWidth:200,
					width:300,
					labelWidth:90,
					allowBlank : false,
					keyColumnName : 'id',
					titleColumnName : 'title',
					childColumnName : 'children',
					editorType:'str',//编辑类型为字符串，不是对象
					//selectType : 'dept',
					//selectTypeName : '部门',
					selectUrl : "organization/dept/odtreeForDeptListAction!odtree.action?organTerm="+base.Login.userSession.currentOrganId
				},{
					xtype : 'textfield',
					name : 'nameTerm',
					fieldLabel : '名称'
				}, {
					fieldLabel : '编码',
					name : 'codeTerm'
				}, Dictionary.createDictionarySelectPanel('类型','QUARTER_TYPE','typeTerm',true,null,false,false), 
				{
					xtype :'select',
					fieldLabel : '是否有效',
					name : 'valid',
					data:[[false,'否'],[true,'是']],
					defaultValue:true
				}];
		this.keyColumnName = "quarterId";// 主健属性名
		this.viewOperater = true;
		this.addOperater = true;
		this.deleteOperater = true;
		this.updateOperater = true;
		this.readerRoot = 'quarters';
		this.listUrl = "organization/quarter/listForQuarterListAction!list.action";// 列表请求地址
		this.deleteUrl = "organization/quarter/deleteForQuarterListAction!delete.action";// 删除请求地址
		this.childColumnName = 'quarters';// 子集合的属性名
		var sortConfig = {};
		sortConfig.columns = [{
					name : 'quarterId',
					width : 0
				}, {
					name : 'quarterName',
					header : '岗位名称',
					width : 3
				}, {
					name : 'quarterCode',
					header : '岗位编码',
					width : 2
				}, {
					name : 'quarterAlias',
					header : '岗位别名',
					width : 2
				}, {
					name : 'quarterType',
					header : '岗位类型',
					width : 2,
					renderer : function(value){
						var display = "";
						Ext.Array.each(me.quarterTypes.datas, function(item) {
							if (item['dataKey']==value){
								display = item['dataName'];
							}
						});
						return display;
					}
				}, {
					name : 'validation',
					header : '是否有效',
					width : 2,
					renderer : function(value){
						return value==false?"无效":"有效";
					}
				}, {
					name : 'dept.deptName',
					header : '所在部门',
					width : 3
				}];
		//打开排序页面
		this.openSortWin = function(record,term,store,callback){
			var me = this;
			sortConfig.keyColumnName = me.keyColumnName;
			sortConfig.sortlistUrl = "organization/quarter/subListForQuarterListAction!subList.action?deptTerm="+(term?term.deptTerm:'')+"&parentId="+(record?record.quarterId:'');
			sortConfig.jsonParemeterName = 'quarters';
			sortConfig.saveSortUrl = 'organization/quarter/saveSortForQuarterFormAction!saveSort.action';
			sortConfig.callback = callback;
			var sortPanel = ClassCreate('base.model.SortList', Ext.clone(sortConfig));
			// 表单窗口
			var sortWin = new WindowObject({
						layout : 'fit',
						title : '岗位排序',
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
		// 打开表单页面方法
		this.openFormWin = function(id, callback, readOnly,data,term,oper) {
			var me = this;
			var formConfig = {};
			var readOnly = readOnly || false;
			formConfig.readOnly = readOnly;
			formConfig.fileUpload = true;
			formConfig.formUrl = "organization/quarter/saveForQuarterFormAction!save.action";
			formConfig.findUrl = "organization/quarter/findForQuarterFormAction!find.action";
			formConfig.callback = callback;
			formConfig.columnSize = 2;
			formConfig.jsonParemeterName = 'form';
			formConfig.oper=oper;//复制操作类型变量
			formConfig.items = new Array();
			formConfig.items.push({
						colspan : 2,
						xtype : 'hidden',
						name : 'quarterId'
					});
			formConfig.items.push({
						colspan : 1,
						fieldLabel : '岗位名称',
						name : 'quarterName',
						xtype : 'textfield',
						allowBlank : false,
						readOnly : readOnly,
						labelWidth:120,
						maxLength : 32
					});
			formConfig.items.push({
						colspan : 1,
						fieldLabel : '岗位编码',
						name : 'quarterCode',
						xtype : 'textfield',
						allowBlank : false,
						readOnly : readOnly,
						labelWidth:120,
						maxLength : 32
					});
			formConfig.items.push({
						colspan : 2,
						width : 570,
						labelWidth:120,
						fieldLabel : '培训标准岗位',
						checked : false,
						name : 'quarterStandard',
						xtype : 'selecttree',
						readOnly : readOnly,
						nameKey : 'quarterId',
						nameLable : 'quarterName',
						//readerRoot : 'children',
						keyColumnName : 'id',
						titleColumnName : 'title',
						childColumnName : 'children',
						selectUrl : "organization/quarter/getQuarterStandardExForQuarterListAction!getQuarterStandardEx.action",
						allowBlank : false
					});
					
			formConfig.items.push({
						colspan : 2,
						width : 570,
						fieldLabel : '部门',
						name : 'dept',
						xtype : 'selecttree',
						readOnly : readOnly,
						allowBlank : false,
						labelWidth:120,
						addPickerWidth:200,
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
							//var quarterField = form.getForm().findField('quarter');
							//quarterField.reflash(me.listUrl+"?deptTerm="+(combo.value)+"&filterIds="+(oper=='update'&&data?data.qurterId:''));
						}
					});
			formConfig.items.push({
						colspan : 2,
						width : 570,
						labelWidth:120,
						fieldLabel : '父岗位',
						addPickerWidth:200,
						name : 'quarter',
						xtype : 'selecttree',
						readOnly : readOnly,
						nameKey : 'quarterId',
						nameLable : 'quarterName',
						readerRoot : 'quarters',
						keyColumnName : 'quarterId',
						titleColumnName : 'quarterNameEx',
						childColumnName : 'quarters',
						selectUrl : me.listUrl+"?organTerm="+base.Login.userSession.currentOrganId+"&filterIds="+(oper=='update'&&data?data.quarterId:'')
					});
					
			
					
			formConfig.items.push({
						colspan : 2,
						width : 570,
						labelWidth:120,
						fieldLabel : '岗位职责',
						name : 'responsibility',
						xtype : 'textfield',
						allowBlank : true,
						readOnly : readOnly,
						maxLength : 32
					});
			var quarterType = modules.baseinfo.Dictionary.createDictionarySelectPanel(
			              '岗位类型','QUARTER_TYPE','quarterType',true,null,true,readOnly);
			quarterType.labelWidth = 120;
			formConfig.items.push(quarterType);
		    formConfig.items.push({
						colspan : 1,
						fieldLabel : '是否有效',
						name : 'validation',
						xtype : 'select',
						labelWidth:120,
						data:[[false,'否'],[true,'是']],
						readOnly : readOnly,
						defaultValue : true
					});
		    
			formConfig.items.push({
						colspan : 2,
						width : 570,
						labelWidth:120,
						fieldLabel : '岗位角色',
						checked : true,
						name : 'quarterRoles',
						xtype : 'selecttree',
						readOnly : readOnly,
						nameKey : 'roleId',
						nameLable : 'roleName',
						//readerRoot : 'children',
						keyColumnName : 'id',
						titleColumnName : 'title',
						childColumnName : 'children',
						selectUrl : "power/role/treeForRoleListAction!tree.action"
					});
					
			formConfig.items.push({
						colspan : 2,
						width : 570,
						labelWidth:120,
						fieldLabel : '备注',
						name : 'remark',
						xtype : 'textarea',
						readOnly : readOnly,
						maxLength : 100,
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
			var form = ClassCreate('base.model.Form', formConfig);
			// 表单窗口
			var formWin = new WindowObject({
						layout : 'fit',
						title : '岗位',
						//height : 500,
						autoHeight : true,
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
					if(term){
					   var v = {'quarter':{'quarterId':(data?data.quarterId:''),'quarterName':(data?data.quarterName:'')}};
					   form.getForm().setValues(v);
					}
				}else
				if(oper == 'update'){
					
				}
			});
		};
		me.callParent();
		Ext.getCmp(me.defaultTermId).setValue({'deptId':base.Login.userSession.currentDeptId,
		       'deptName':base.Login.userSession.currentDeptName});
	}
});