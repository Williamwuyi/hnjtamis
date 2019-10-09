/**
 * 专业类型
 */
ClassDefine('modules.hnjtamis.baseinfo.specialitytype.Specialitytype', {
	extend : 'base.model.TreeList',
	setOtherAttribute:function(data){
		if(data.id.indexOf("_")>0){
			data.closeIcon='resources/icons/fam/plugin_add.gif';
		   	data.icon='resources/icons/fam/plugin_add.gif';
		}
		else{
			data.closeIcon='resources/icons/fam/plugin_add.gif';
			data.icon='resources/icons/fam/plugin_add.gif';
		}
		   
	},
	
	initComponent : function() {
		var me = this;
		// 模块列表对象
		this.columns = [{
					name : 'bstid',
					width : 0
				}, {
					name : 'typename',
					header : '名称',
					xtype : 'treecolumn',
					width : 1.5
				}, {
					name : 'isavailable',
					header : '是否有效',
					width : 1,
					renderer : function(value){
						return value==0?"无效":"有效";
					}
				}, {
					name : 'remarks',
					header : '备注',
					width : 3
				}, {
					name : 'creationDate',
					header : '创建时间',
					width : 1
				}, {
					name : 'createdBy',
					header : '创建人员',
					width : 0.8
				}, {
					name : 'lastUpdatedBy',
					header : '最后修改人员',
					width : 0.8
				}];
		// 模块查询条件对象
		this.terms = [
				{
					xtype : 'textfield',
					name  : 'nameTerm',
					width : 270,
					labelWidth : 80,
					fieldLabel : '类型名称'
				}];
		this.keyColumnName = "bstid";// 主健属性名
		this.viewOperater = true;
		this.addOperater = true;
		this.deleteOperater = true;
		this.updateOperater = true;
		this.readerRoot = 'subSpecialitytypes';/////
		
		this.listUrl = "baseinfo/specialitytype/listForSpecialityTypeListAction!list.action";// 列表请求地址
		this.deleteUrl = "baseinfo/specialitytype/listForSpecialityTypeListAction!delete.action";// 删除请求地址
		
		this.childColumnName = 'subSpecialitytypes';// 子集合的属性名
		this.grabl_tree_numberColumnWidth = 60;
		/// 添加自定义按钮
		me.otherOperaters=[];
		me.otherOperaters.push(Ext.create("Ext.Button", {
							iconCls : 'option',
							text : '还原',
							handler : function() {
								var selected = me.getSelectionModel().selected;
								if (selected.getCount() == 0) {
									Ext.Msg.alert('提示', '请选择记录！');
									return;
								}
								var ids = "";
								for (var i = 0; i < selected.getCount(); i++) {
									var record = selected.get(i);
									if (i != 0)
										ids += "," + record.get(me.keyColumnName);
									else
										ids += record.get(me.keyColumnName);
								}
								var confirmFn = function(btn) {
									if (btn == 'yes'){
										//
										var oldurl=me.deleteUrl;
										me.deleteUrl=oldurl+'?restoreSpecialtypeTerm=1'
										//me.store.proxy.extraParams.restoreSpecialtypeTerm='1';
										//alert(me.deleteData);
										me.deleteData(ids);
										me.deleteUrl=oldurl;
										//alert(getSelectIds);
										//me.deleteData(me.getSelectIds());
									}
								};
								Ext.MessageBox.confirm('询问', '你真要还原这些数据吗？',
										confirmFn);
							}
						}));
		me.otherOperaters.push(Ext.create("Ext.form.Checkbox", {
							iconCls : 'option',
							boxLabel : '显示已删除数据',
							handler : function(checkbox,checked) {
								var selected = me.getSelectionModel().selected;
								if (checked) {
									///Ext.Msg.alert('提示', '已勾选！');
									///alert(me.termQueryFun);
									
									me.store.load({
						  	  			params:{
						  	  				showAllSpecialtypeTerm:'1'/// 显示所有数据，包括逻辑删除数据
						  	  			}
						  	  		});
						  	  		me.store.proxy.extraParams.showAllSpecialtypeTerm='1';
									///me.termQueryFun(true,'flash');
									return;
								}
								else{
									//Ext.Msg.alert('提示', '取消勾选！');
									me.store.load({
						  	  			params:{
						  	  				showAllSpecialtypeTerm:'0'
						  	  			}
						  	  		});
						  	  		me.store.proxy.extraParams.showAllSpecialtypeTerm='0';
								}
							}
						}));
		
		// 打开表单页面方法
		this.openFormWin = function(id, callback, readOnly,data,term,oper) {
			var me = this;
			var formConfig = {};
			var readOnly = readOnly || false;
			formConfig.readOnly = readOnly;
			formConfig.fileUpload = true;
			formConfig.formUrl = "baseinfo/specialitytype/saveForSpecialityTypeFormAction!save.action";
			formConfig.findUrl = "baseinfo/specialitytype/findForSpecialityTypeFormAction!find.action";
			formConfig.callback = callback;
			formConfig.columnSize = 1;
			formConfig.jsonParemeterName = 'form';
			formConfig.items = new Array();
			formConfig.oper = oper;// 复制操作类型变量
			formConfig.items.push({
						colspan : 1,
						xtype : 'hidden',
						name : 'bstid'
					});
			formConfig.items.push({
						colspan : 1,
						fieldLabel : '类型名称',
						name : 'typename',
						xtype : 'textfield',
						allowBlank : false,
						readOnly : readOnly,
						maxLength : 32
					});
			formConfig.items.push({
						colspan : 1,
						fieldLabel : '上级类型',
						name : 'parentspeciltype',
						addPickerWidth:100,
						xtype : 'selecttree',
						readOnly : readOnly,
						nameKey : 'bstid',
						nameLable : 'typename',
						readerRoot : 'subSpecialitytypes',
						keyColumnName : 'bstid',
						titleColumnName : 'typename',
						childColumnName : 'subSpecialitytypes',
						selectUrl : me.listUrl+"?filterIds="+(oper=='update'&&data?data.bstid:'')
					});
			
		    formConfig.items.push({
						colspan : 1,
						fieldLabel : '是否有效',
						name : 'isavailable',
						xtype : 'select',
						data:[[0,'否'],[1,'是']],
						readOnly : readOnly,
						defaultValue : 1
					});
					
		    formConfig.items.push({
						colspan : 1,
						fieldLabel : '备注',
						name : 'remarks',
						xtype : 'textarea',
						readOnly : readOnly,
						maxLength : 100,
						width : 538,
						height : 70
					});
            formConfig.items.push({
						colspan : 1,
						name : 'orderno',
						xtype : 'hidden',
						value : 0
					});
		    formConfig.items.push({
						colspan : 1,
						name : 'levelno',
						xtype : 'hidden'
					});
			formConfig.items.push({
						colspan : 1,
						name : 'status',
						xtype : 'hidden'
					});
			formConfig.items.push({
						colspan : 1,
						name : 'lastUpdateDate',
						xtype : 'hidden'
					});
			formConfig.items.push({
						colspan : 1,
						name : 'lastUpdatedBy',
						xtype : 'hidden'
					});
			formConfig.items.push({
						colspan : 1,
						name : 'creationDate',
						xtype : 'hidden'
					});
			formConfig.items.push({
						colspan : 1,
						name : 'createdBy',
						xtype : 'hidden'
					});
			formConfig.items.push({
						colspan : 1,
						name : 'organid',
						xtype : 'hidden'
					});
			var form = ClassCreate('base.model.Form', formConfig);
			// 表单窗口
			var formWin = new WindowObject({
						layout : 'fit',
						title : '专业类型',
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
					   form.getForm().setValues({'organ':{'organId':(data?data.organId:''),'organName':(data?data.organName:'')}});
					}
				}
			});
		};
		me.callParent();
		
	}
});
