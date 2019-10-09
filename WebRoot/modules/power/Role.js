/**
 * 系统公告的模块类
 */
ClassDefine('modules.power.Role', {
	extend : 'base.model.TreeList',
	setOtherAttribute:function(data){
		if(data.id.indexOf("_")>0)
		   data.icon='resources/icons/fam/dept.gif';
		else
		   data.icon='resources/icons/fam/organ.gif';
	},
	initComponent : function() {
		// 模块列表对象
		this.columns = [{
					name : 'rtId',
					width : 0
				}, {
					name : 'roleTypename',
					header : '名称',
					xtype : 'treecolumn',
					sortable : false,
					width : 3
				}, {
					name : 'roleTypeCode',
					header : '编码',
					sortable : false,
					width : 1
				}, {
					name : 'resourceName',
					header : '功能资源',
					sortable : false,
					width : 5,
					renderer:function(value,cellmeta,record,rowIndex,columnIndex,store){
						//自动换行
						cellmeta.style="white-space: normal !important;";
						return value;
					}
				}, {
					name : 'desc',
					header : '描述',
					sortable : false,
					width : 2,
					renderer : this.rendererSysTypeFn
				}];
		// 模块查询条件对象
		this.terms = [{
						fieldLabel : '名称',
						xtype : 'textfield',
						name : 'nameTerm'
					},{
						xtype : 'textfield',
						name : 'codeTerm',
						fieldLabel : '编码'
					}];
		this.keyColumnName = "rtId";// 主健属性名
		this.viewOperater = true;
		this.addOperater = true;
		this.deleteOperater = true;
		this.updateOperater = true;
		this.readerRoot = 'roleTypes';
		this.listUrl = "power/role/listForRoleListAction!list.action";// 列表请求地址
		this.deleteUrl = "power/role/deleteForRoleListAction!delete.action";// 删除请求地址
		this.childColumnName = 'roleTypes';// 子集合的属性名
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
			sortConfig.sortlistUrl = "power/role/subListForRoleListAction!subList.action?parentId="+(record?record.rtId:'');
			sortConfig.jsonParemeterName = 'roleTypes';
			sortConfig.saveSortUrl = 'power/role/saveSortForRoleFormAction!saveSort.action';
			sortConfig.callback = callback;
			var sortPanel = ClassCreate('base.model.SortList', Ext.clone(sortConfig));
			// 表单窗口
			var sortWin = new WindowObject({
						layout : 'fit',
						title : '角色类型排序',
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
			id = id.split("_")[0];
			var me = this;
			var formConfig = {};
			var readOnly = readOnly || false;
			formConfig.readOnly = readOnly;
			formConfig.fileUpload = true;
			formConfig.formUrl = "power/role/saveForRoleFormAction!save.action";
			formConfig.findUrl = "power/role/findForRoleFormAction!find.action";
			formConfig.callback = callback;
			formConfig.columnSize = 2;
			formConfig.jsonParemeterName = 'form';
			formConfig.items = new Array();
			formConfig.oper = oper;// 复制操作类型变量
		    //功能资源选择框
		    var editorResourceName = Ext.widget('selecttree',{checked : true,nameKey : 'resourceId',
								nameLable : 'resourceName',
								readerRoot : 'children',
								keyColumnName : 'id',
								addPickerWidth : 120,
								allowBlank : false,
								titleColumnName : 'title',
								childColumnName : 'children',
								selectType:'resource',
								selectUrl : "funres/module/findAMRTreeForModuleListAction!findAMRTree.action?userId="
								  +base.Login.userSession.userId,
								listeners:{collapse:function(){
									editTable.getView().refresh();
								}}});
		    var editTable = Ext.widget('editlist',{
						colspan : 2,
						fieldLabel : '',
						height : 260,
						name : 'sysRoles',
						xtype : 'editlist',
						addOperater : true,
						deleteOperater : true,
						//viewConfig:{height:280,autoScroll:true},
						columns : [{
									width : 0,
									name : 'roleId'
								}, {
									name : 'roleCode',
									header : '角色编码',
									editor : {
										xtype : 'textfield',
										maxLength : 32,
										allowBlank : false
									},
									width : 2
								}, {
									name : 'roleName',
									header : '角色名称',
									editor : {
										xtype : 'textfield',
										allowBlank : false,
										maxLength : 32
									},
									width : 3
								}, {
									header : '功能资源',
									name : 'roleResources',
									editor:editorResourceName,
									width:7,
									renderer:function(value,cellmeta,record,rowIndex,columnIndex,store){
										var v = "";
										//自动换行
										cellmeta.style="white-space: normal !important;";
										Ext.Array.each(value,function(item){
											if(v=="") v = item['resourceName'];
											else v += ","+item['resourceName'];
										});
										return v;
									}
								}],
						readOnly : readOnly
					});
			formConfig.items=[{
				xtype : 'panel',
				autoScroll : true,
				layout : {
					type : 'accordion',
					fill : false,
					multi : true
				},// 是否同时只有一个页签打开
				items : [{
					xtype : 'panel',
					title : '角色类型',
					hideCollapseTool : true,// 隐藏打开关闭工具图标
					titleCollapse : false,// 使能点击标题就打开关闭
					// autoScroll: true,
					// height:190,
					layout : {
						type : 'table',
						columns : 2
					},
					items : [{
						colspan : 2,
						xtype : 'hidden',
						name : 'rtId'
					},{
						colspan : 1,
						fieldLabel : '角色类型名称',
						name : 'roleTypename',
						xtype : 'textfield',
						allowBlank : false,
						readOnly : readOnly,
						labelWidth:130,
						maxLength : 32
					},{
						colspan : 1,
						fieldLabel : '角色类型编码',
						name : 'roleTypeCode',
						xtype : 'textfield',
						allowBlank : false,
						readOnly : readOnly,
						labelWidth:130,
						maxLength : 32
					},{
						colspan : 1,
						fieldLabel : '父角色类型',
						name : 'roleType',
						xtype : 'selecttree',
						readOnly : readOnly,
						labelWidth:130,
						nameKey : 'rtId',
						nameLable : 'roleTypename',
						readerRoot : 'roleTypes',
						keyColumnName : 'rtId',
						titleColumnName : 'roleTypename',
						childColumnName : 'roleTypes',
						selectUrl : "power/role/queryForRoleListAction!query.action?filterIds="+(oper=='update'&&data?data.rtId.split("_")[0]:'')
					},{
						colspan : 1,
						fieldLabel : '描述',
						name : 'desc',
						xtype : 'textarea',
						readOnly : readOnly,
						maxLength : 100,
						labelWidth:130,
						height : 50
					},{
						colspan : 1,
						name : 'sortNo',
						xtype : 'hidden',
						value : 0
					}]},
					 {
					title : '角色',
					xtype : 'panel',
					//collapsed : false,// 缺省折叠
					 collapsible: true,
					items : [editTable]}]}];
			var form = ClassCreate('base.model.Form', formConfig);
			// 表单窗口
			var formWin = new WindowObject({
						layout : 'fit',
						title : '角色及类型',
						autoHeight : true,
						width : 800,
						height:500,
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
					   form.getForm().setValues({'roleType':{'rtId':(data?data.rtId:''),'roleTypename':(data?data.roleTypename:'')}});
					}
				}
			});
		};
		this.callParent();
	}
});