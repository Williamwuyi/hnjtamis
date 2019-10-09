/**
 * 系统公告的模块类
 */
ClassDefine('modules.organization.Dept', {
	extend : 'base.model.TreeList',
	requires: ['modules.baseinfo.Dictionary'],
	initComponent : function() {
		var Dictionary = modules.baseinfo.Dictionary;//类别名
		this.deptTypes = Dictionary.getDictionaryList('DEPT_TYPE');
		var me = this;
		// 模块列表对象
		this.columns = [{
					name : 'deptId',
					width : 0
				}, {
					name : 'deptName',
					header : '部门名称',
					xtype : 'treecolumn',
					width : 5
				}, {
					name : 'deptCode',
					header : '部门编码',
					width : 3
				}, {
					name : 'deptAlias',
					header : '部门别名',
					width : 2
				}, {
					name : 'deptType',
					header : '部门类型',
					width : 2,
					renderer : function(value){
						var display = "";
						Ext.Array.each(me.deptTypes.datas, function(item) {
							if (item['dataKey']==value){
								display = item['dataName'];
							}
						});
						return display;
					}
				}, {
					name : 'validation',
					header : '是否有效',
					width : 1,
					renderer : function(value){
						return value==false?"无效":"有效";
					}
				}, {
					name : 'organ.organName',
					header : '所在机构',
					width : 2
				}];
		// 模块查询条件对象
		this.terms = [{
					name : 'organTerm',
					fieldLabel : '机构',
					xtype : 'selecttree',
					addPickerWidth:200,
					nameKey : 'organId',
					nameLable : 'organName',
					readerRoot : 'organs',
					allowBlank : false,
					width:300,
					keyColumnName : 'organId',
					titleColumnName : 'organName',
					childColumnName : 'organs',
					editorType:'str',//编辑类型为字符串，不是对象
					selectUrl : "organization/organ/listForOrganListAction!list.action?topOrganTerm="+base.Login.userSession.organId
				},{
					xtype : 'textfield',
					name : 'nameTerm',
					fieldLabel : '名称'
				}, {
					fieldLabel : '编码',
					name : 'codeTerm'
				}, Dictionary.createDictionarySelectPanel('类型','DEPT_TYPE','typeTerm',true,null,false,false), 
				{
					xtype :'select',
					fieldLabel : '是否有效',
					name : 'validStr',
					data:[['','所有'],['0','否'],['1','是']]
				}];
		this.keyColumnName = "deptId";// 主健属性名
		this.viewOperater = true;
		this.addOperater = true;
		this.deleteOperater = true;
		this.updateOperater = true;
		this.readerRoot = 'depts';
		this.listUrl = "organization/dept/listForDeptListAction!list.action";// 列表请求地址
		this.deleteUrl = "organization/dept/deleteForDeptListAction!delete.action";// 删除请求地址
		this.childColumnName = 'depts';// 子集合的属性名
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
			sortConfig.sortlistUrl = "organization/dept/subListForDeptListAction!subList.action?parentId="+(record?record.deptId:'')+"&organTerm="+term.organTerm;
			sortConfig.jsonParemeterName = 'depts';
			sortConfig.saveSortUrl = 'organization/dept/saveSortForDeptFormAction!saveSort.action';
			sortConfig.callback = callback;
			var sortPanel = ClassCreate('base.model.SortList', Ext.clone(sortConfig));
			// 表单窗口
			var sortWin = new WindowObject({
						layout : 'fit',
						title : '部门排序',
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
			formConfig.formUrl = "organization/dept/saveForDeptFormAction!save.action";
			formConfig.findUrl = "organization/dept/findForDeptFormAction!find.action";
			formConfig.callback = callback;
			formConfig.columnSize = 2;
			formConfig.jsonParemeterName = 'form';
			formConfig.items = new Array();
			formConfig.oper=oper;//复制操作类型变量
			formConfig.items.push({
						colspan : 2,
						xtype : 'hidden',
						name : 'deptId'
					});
			formConfig.items.push({
						colspan : 1,
						fieldLabel : '部门名称',
						name : 'deptName',
						xtype : 'textfield',
						allowBlank : false,
						readOnly : readOnly,
						maxLength : 32
					});
			formConfig.items.push({
						colspan : 1,
						fieldLabel : '部门别名',
						name : 'deptAlias',
						xtype : 'textfield',
						allowBlank : true,
						readOnly : readOnly,
						maxLength : 32
					});
			formConfig.items.push({
						colspan : 1,
						fieldLabel : '部门编码',
						name : 'deptCode',
						xtype : 'textfield',
						allowBlank : false,
						readOnly : readOnly,
						maxLength : 32
					});
			formConfig.items.push({
						colspan : 1,
						fieldLabel : '所在机构',
						name : 'organ',
						xtype : 'selecttree',
						readOnly : readOnly,
						addPickerWidth:200,
						nameKey : 'organId',
						nameLable : 'organName',
						readerRoot : 'organs',
						keyColumnName : 'organId',
						titleColumnName : 'organName',
						childColumnName : 'organs',
						selectUrl : "organization/organ/listForOrganListAction!list.action?topOrganTerm="+base.Login.userSession.organId,
						selectEventFun:function(combo,record,index){
							var deptField = form.getForm().findField('dept');
							deptField.reflash(me.listUrl+"?filterIds="+(oper=='update'&&data?data.deptId:'')+"&organTerm="+(combo.value));
						}
					});
			formConfig.items.push({
						colspan : 1,
						fieldLabel : '父部门',
						name : 'dept',
						addPickerWidth:200,
						xtype : 'selecttree',
						readOnly : readOnly,
						nameKey : 'deptId',
						nameLable : 'deptName',
						readerRoot : 'depts',
						keyColumnName : 'deptId',
						titleColumnName : 'deptName',
						childColumnName : 'depts'
					});
			formConfig.items.push({
						colspan : 1,
						fieldLabel : '部门性质',
						name : 'deptCharacter',
						xtype : 'textfield',
						readOnly : readOnly,
						maxLength : 32
					});
			formConfig.items.push(modules.baseinfo.Dictionary.createDictionarySelectPanel(
			              '部门类型','DEPT_TYPE','deptType',true,null,true,readOnly));
		    formConfig.items.push({
						colspan : 1,
						fieldLabel : '是否有效',
						name : 'validation',
						xtype : 'select',
						data:[[false,'否'],[true,'是']],
						readOnly : readOnly,
						defaultValue : true
					});
					
		    formConfig.items.push({
						colspan : 2,
						fieldLabel : '备注',
						name : 'remark',
						xtype : 'textarea',
						readOnly : readOnly,
						maxLength : 100,
						width : 538,
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
						title : '部门',
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
					   form.getForm().findField('dept').reflash(me.listUrl+"?organTerm="+term.organTerm);
					   form.getForm().setValues({'dept':{'deptId':(data?data.deptId:''),'deptName':(data?data.deptName:'')},
					                             'organ':{'organId':term.organTerm,'organName':''}});
					}
				}else
				if(oper == 'update'){
					form.getForm().findField('dept').reflash(me.listUrl+
					     "?filterIds="+result.form.deptId+"&organTerm="+result.form.organ.organId,false);
				}
			});
		};
		me.callParent();
		//设置当前的机构条件
		Ext.getCmp(me.defaultTermId).setValue({'organId':base.Login.userSession.currentOrganId
		            ,'organName':base.Login.userSession.currentOrganName});
	}
});