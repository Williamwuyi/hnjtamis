/**
 * 模块功能的模块类
 */
ClassDefine('modules.funres.Menu', {
	extend : 'base.model.TreeList',
	typedata : [[1, '菜单组'], [2, '菜单'],[3,'分隔符']],
	targetdata : [[1,'内部地址'],[2,'外部地址']],
	initComponent : function() {
		var me = this;
		// 模块列表对象
		this.columns = [{
					name : 'menuId',
					width : 0
				}, {
					name : 'menuName',
					header : '菜单名称',
					xtype : 'treecolumn',
					width : 4
				}, {
					name : 'tabName',
					header : '标签名称',
					width : 2
				}, {
					name : 'moduleResource.resourceName',
					header : '对应资源',
					width : 2
				}, {
					name : 'menuType',
					header : '菜单类型',
					width : 2,
					renderer:function(value){
					    return me.typedata[value-1][1];
				    }
				}, {
					name : 'description',
					header : '描述',
					width : 3
				}];
		// 模块查询条件对象
		this.terms = [{
					xtype : 'select',
					name : 'appTerm',
					fieldLabel : '应用系统',
					selectUrl:'funres/app/allForAppListAction!all.action',
					valueField:'appId',
					displayField:'appName',
					jsonParemeterName:'list',
					width:250,
					defaultValue:base.Login.userSession.appId
				}, {
					fieldLabel : '菜单名称',
					name : 'nameTerm'
				}, {
					fieldLabel : '资源名称',
					name : 'resourceNameTerm'
				}, {
					fieldLabel : '资源编码',
					name : 'resourceCodeTerm'
				}];
		this.keyColumnName = "menuId";// 主健属性名
		this.viewOperater = true;
		this.addOperater = true;
		this.deleteOperater = true;
		this.updateOperater = true;
		//this.readerRoot = 'appMenus';
		this.listUrl = "funres/menu/listForMenuListAction!list.action";// 列表请求地址
		this.deleteUrl = "funres/menu/deleteForMenuListAction!delete.action";// 删除请求地址
		this.childColumnName = 'appMenus';// 子集合的属性名
		var sortConfig = {};
		//列属性配置复制
		sortConfig.columns = new Array(this.columns.length);
		for(var i=0;i<this.columns.length;i++){
		   sortConfig.columns[i] = Ext.clone(this.columns[i]);
		   delete sortConfig.columns[i].xtype;
		}
		//打开排序页面,定义了些方法说明有排序功能
		this.openSortWin = function(record,term,store,callback){
			var me = this;
			sortConfig.keyColumnName = me.keyColumnName;
			sortConfig.sortlistUrl = "funres/menu/subListForMenuListAction!subList.action?parentId="
			               +(record?record.menuId:'')+"&appTerm="+(term?term.appTerm:'');
			sortConfig.jsonParemeterName = 'appMenus';
			sortConfig.saveSortUrl = 'funres/menu/saveSortForMenuFormAction!saveSort.action';
			sortConfig.callback = callback;
			var sortPanel = ClassCreate('base.model.SortList', Ext.clone(sortConfig));
			// 表单窗口
			var sortWin = new WindowObject({
						layout : 'fit',
						title : '菜单排序',
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
			formConfig.formUrl = "funres/menu/saveForMenuFormAction!save.action";
			formConfig.findUrl = "funres/menu/findForMenuFormAction!find.action";
			formConfig.callback = callback;
			formConfig.columnSize = 2;
			formConfig.fieldDefaults = {
				labelAlign : 'right',
				labelWidth : 90
			}
			formConfig.items = new Array();
			formConfig.jsonParemeterName = 'form';
			formConfig.oper=oper;//复制操作类型变量
			formConfig.items.push({
						colspan : 2,
						xtype : 'hidden',
						name : 'menuId'
					});
			formConfig.items.push({
						colspan : 1,
						fieldLabel : '菜单名称',
						name : 'menuName',
						xtype : 'textfield',
						allowBlank : false,
						readOnly : readOnly,
						maxLength : 32
					});
		    formConfig.items.push({
						colspan : 1,
						fieldLabel : '菜单类型',
						name : 'menuType',
						xtype : 'select',
						allowBlank : false,
						readOnly : readOnly,
						data : me.typedata,
						selectEventFun:function(combo,record,index){
							if(combo.value==2){
								form.getForm().findField('moduleResource').readOnly = false;
								form.getForm().findField('moduleResource').allowBlank = false;
							}else{
								form.getForm().findField('moduleResource').setValue({});
						        form.getForm().findField('moduleResource').readOnly = true;
						        form.getForm().findField('moduleResource').allowBlank = true;
							}
						}
					});
			formConfig.items.push({
						colspan : 1,
						fieldLabel : '标签名称',
						name : 'tabName',
						xtype : 'textfield',
						allowBlank : false,
						readOnly : readOnly,
						maxLength : 32
					});
		    formConfig.items.push({
						colspan : 1,
						fieldLabel : '应用系统',
						name : 'appSystem',
						xtype : 'selectobject',
						readOnly : readOnly,
						valueField : 'appId',
						displayField : 'appName',
						allowBlank : false,
						readerRoot : 'list',
						selectUrl : 'funres/app/allForAppListAction!all.action?limit=0',
						selectEventFun:function(combo,record,index){
							var appMenuField = form.getForm().findField('appMenu');
							appMenuField.reflash(me.listUrl+"?meneTypeTerm=1&appTerm="+(combo.value.appId)+"&filterIds="+(oper=='update'&&data?data.menuId:''));
							var moduleResourceField = form.getForm().findField('moduleResource');
							moduleResourceField.reflash('funres/module/findMRTreeForModuleListAction!findMRTree.action'
						            +"?appTerm="+(combo.value.appId));
						}
					});
			formConfig.items.push({
						colspan : 1,
						fieldLabel : '父菜单名称',
						name : 'appMenu',
						xtype : 'selecttree',
						readOnly : readOnly,
						addPickerWidth:40,
						nameKey : 'menuId',
						nameLable : 'menuName',
						readerRoot : 'appMenus',
						keyColumnName : 'menuId',
						titleColumnName : 'menuName',
						childColumnName : 'appMenus',
						selectUrl : me.listUrl+"?meneTypeTerm=1&appTerm="+(term?term.appTerm:'')+"&filterIds="+(oper=='update'&&data?data.menuId:'')
					});
			formConfig.items.push({
						colspan : 1,
						fieldLabel : '对应资源',
						name : 'moduleResource',
						addPickerWidth:40,
						xtype : 'selecttree',
						readOnly : readOnly,
						nameKey : 'resourceId',
						nameLable : 'resourceName',
						keyColumnName : 'id',
						titleColumnName : 'title',
						childColumnName : 'children',
						selectType : 'resource',
						selectTypeName : '功能资源',
						selectUrl : 'funres/module/findMRTreeForModuleListAction!findMRTree.action'
						            +"?appTerm="+(term?term.appTerm:'')
					});
			formConfig.items.push({
						colspan : 1,
						fieldLabel : '地址类型',
						xtype : 'select',
						allowBlank : false,
						data:me.targetdata,
						readOnly : readOnly,
						name : 'target',
						selectEventFun:function(combo,record,index){
							form.getForm().findField('otherUrl').allowBlank = (combo.value==1);
						}
					});
			formConfig.items.push({
						colspan : 1,
						fieldLabel : '是否隐藏',
						name : 'hidden',
						xtype : 'select',
						allowBlank : false,
						readOnly : readOnly,
						data:[[false,'否'],[true,'是']]
					});
			formConfig.items.push({
						colspan : 1,
						fieldLabel : '外部地址',
						name : 'otherUrl',
						xtype : 'textfield',
						readOnly : readOnly,
						maxLength : 100,
						width : 300
					});
			formConfig.items.push({
						colspan : 1,
						fieldLabel : '是否展开',
						name : 'expand',
						xtype : 'select',
						allowBlank : false,
						readOnly : readOnly,
						data:[[false,'否'],[true,'是']]
					});
		    formConfig.items.push({
						colspan : 2,
						fieldLabel : '描述',
						name : 'description',
						xtype : 'textarea',
						readOnly : readOnly,
						maxLength : 100,
						width: 590,
						height : 70
					});
		    formConfig.items.push({
						colspan : 2,
						fieldLabel : '帮助页面地址',
						name : 'helpUrl',
						xtype : 'textfield',
						readOnly : readOnly,
						maxLength : 100,
						width: 590
					});
			formConfig.items.push({
						colspan : 2,
						fieldLabel : '帮助内容',
						name : 'helpContent',
						xtype : 'htmleditor',
						readOnly : readOnly,
						height:250,
						width: 590
					});
            formConfig.items.push({
						colspan : 1,
						name : 'orderNo',
						xtype : 'hidden',
						value : 1
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
						title : '菜单功能',
						height : 600,
						width : 646,
						border : false,
						frame : false,
						modal : true,// 模态
						closeAction : 'hide',
						draggable:true,//拖动
						resizable:false, //变大小 
						items : [form]
					});
			formWin.show();
			form.setFormData(id,function(result){
				//添加操作时，应用系统缺省使用查询条件中
				if(oper == 'add'){
					if(term){
					   var addData = {
							'appSystem' : {
								'appId' : term.appTerm
							}};
					   addData.appMenu = {};
				       if (data && data.menuType != 3)
				          if(data.menuType==1)
						     addData.appMenu={'menuId':data.menuId};
						  else
						     addData.appMenu={'menuId':data.parentId};
					   form.getForm().setValues(addData);
					}
					form.getForm().findField('moduleResource').readOnly = true;
					form.getForm().findField('moduleResource').allowBlank = true;
				}else if(oper == 'update'){
					form.getForm().findField('otherUrl').allowBlank = (form.getForm().findField('target').value==1);
					if(form.getForm().findField('menuType').value==2){
						form.getForm().findField('moduleResource').readOnly = false;
						form.getForm().findField('moduleResource').allowBlank = false;
					}else{
					    form.getForm().findField('moduleResource').readOnly = true;
					    form.getForm().findField('moduleResource').allowBlank = true;
					}
				}
			});
		};
		this.callParent();
		base.Login.handlePopdomButtom(this);
	}
});