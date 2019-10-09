/**
 * 系统公告的模块类
 */
ClassDefine('modules.organization.Organ', {
	extend : 'base.model.TreeList',
	requires: ['modules.baseinfo.Dictionary'],
	initComponent : function() {
		var Dictionary = modules.baseinfo.Dictionary;//类别名
		this.organTypes = Dictionary.getDictionaryList('ORGAN_TYPE');
		var me = this;
		// 模块列表对象
		this.columns = [{
					name : 'organId',
					width : 0
				}, {
					name : 'organName',
					header : '机构名称',
					xtype : 'treecolumn',
					width : 5
				}, {
					name : 'organCode',
					header : '机构编码',
					width : 3
				}, {
					name : 'organAlias',
					header : '机构别名',
					width : 2
				}, {
					name : 'organType',
					header : '机构类型',
					width : 2,
					renderer : function(value){
						var display = "";
						Ext.Array.each(me.organTypes.datas, function(item) {
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
				}];
		// 模块查询条件对象
		this.terms = [{
					xtype : 'textfield',
					name : 'nameTerm',
					width:270,
					fieldLabel : '名称'
				}, {
					fieldLabel : '编码',
					name : 'codeTerm'
				}, Dictionary.createDictionarySelectPanel('类型','ORGAN_TYPE','typeTerm',true,null,false,false), 
				{
					fieldLabel : '地区',
					name : 'areaTerm'
				}, {
					xtype :'select',
					fieldLabel : '是否有效',
					name : 'validStr',
					data:[['','所有'],['0','否'],['1','是']]
				}];
		this.keyColumnName = "organId";// 主健属性名
		this.viewOperater = true;
		this.addOperater = true;
		this.deleteOperater = true;
		this.updateOperater = true;
		this.readerRoot = 'organs';
		this.otherOperaters = [];//其它扩展按钮操作
        this.otherOperaters.push({
	                xtype : 'button',
					icon : 'resources/icons/fam/export.gif',
					text : '导出',
					handler : function() {
						if(me.selectNode==null){
							Ext.Msg.alert('提示', '请选择一机构！');
							return;
						}
						var id = me.selectNode.organId;
						window.location = 'organization/organ/exportXlsForOrganListAction!exportXls.action?id='+id;
					}
				});
	    this.otherOperaters.push({
	                xtype : 'button',
					icon : 'resources/icons/fam/import.gif',
					text : '导入',
					handler : function() {
						var importForm = new Ext.FormPanel({
					        frame:false,
					        fileUpload : true,//指定带文件上传
					        bodyStyle:'padding:10px 5px 0', 
					        defaultType:'textfield',
					        defaults: {
					           labelWidth:90,
					           labelAlign :'right'
					        },
					        monitorValid:true,
					        items:[{ 
					                fieldLabel:'导入XLS文件', 
					                xtype:'fileuploadfield',
					                buttonText:'浏览',
					                name:'xls',
					                emptyText:'请选择EXCEL文件',
					                colspan : 2,
					                width : 300,
					                allowBlank:false
					            }],
					        buttons:['->',{ 
					                text:'确定',
					                formBind: true, 
					                handler:function(){ 
					                    importForm.getForm().submit({ 
					                        method:'POST', 
					                        waitTitle:'导入提示', 
					                        waitMsg:'正在导入,请稍候...',
					                        url:'organization/organ/importXlsForOrganListAction!importXls.action',
					                        success:function(form, action){
					                        	Ext.Msg.alert('提示', '导入成功!',function(){
					                                   importWin.hide();
					                                   delete importWin;
					                                   me.termQueryFun();
					                            }); 
					                        },
					                        failure:function(form, action){
					                            if(action.failureType == 'server'){
					                                var obj = Ext.decode(action.response.responseText);
					                                if(Ext.isArray(obj)) obj = obj[0];
					                                Ext.Msg.alert('导入失败', obj.errors,function(){
					                                        form.findField('xls').focus();
					                                }); 
					                            }else{ 
					                                Ext.Msg.alert('警告', '网络出现问题！'); 
					                            }                            
					                        } 
					                    }); 
					                } 
					            }]
					    });
						var importWin = new WindowObject({
									layout : 'form',
									title : '导入机构部门岗位员工用户',
									autoHeight : true,
									width : 400,
									border : false,
									frame : false,
									modal : true,//模态
									closeAction : 'hide',
									items : [importForm]
								});
						importWin.show();
					}
				});
		this.listUrl = "organization/organ/listForOrganListAction!list.action";// 列表请求地址
		this.deleteUrl = "organization/organ/deleteForOrganListAction!delete.action";// 删除请求地址
		this.childColumnName = 'organs';// 子集合的属性名
		var sortConfig = {};
		//列属性配置复制
		sortConfig.columns = new Array(this.columns.length);
		for(var i=0;i<this.columns.length;i++){
		   sortConfig.columns[i] = Ext.clone(this.columns[i]);
		   delete sortConfig.columns[i].xtype;
		   delete sortConfig.columns[i].defaultValue;
		}
		//打开排序页面
		this.openSortWin = function(record,term,store,callback){
			var me = this;
			sortConfig.keyColumnName = me.keyColumnName;
			sortConfig.sortlistUrl = "organization/organ/subListForOrganListAction!subList.action?parentId="+(record?record.organId:'');
			sortConfig.jsonParemeterName = 'organs';
			sortConfig.saveSortUrl = 'organization/organ/saveSortForOrganFormAction!saveSort.action';
			sortConfig.callback = callback;
			var sortPanel = ClassCreate('base.model.SortList', Ext.clone(sortConfig));
			// 表单窗口
			var sortWin = new WindowObject({
						layout : 'fit',
						title : '机构排序',
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
			formConfig.formUrl = "organization/organ/saveForOrganFormAction!save.action";
			formConfig.findUrl = "organization/organ/findForOrganFormAction!find.action";
			formConfig.callback = callback;
			formConfig.columnSize = 2;
			formConfig.jsonParemeterName = 'form';
			formConfig.oper=oper;//复制操作类型变量
			formConfig.items = new Array();
			formConfig.items.push({
						colspan : 2,
						xtype : 'hidden',
						name : 'organId'
					});
			formConfig.items.push({
						colspan : 1,
						fieldLabel : '机构名称',
						name : 'organName',
						xtype : 'textfield',
						allowBlank : false,
						readOnly : readOnly,
						labelWidth:120,
						maxLength : 32
					});
			formConfig.items.push({
						colspan : 1,
						fieldLabel : '机构别名',
						name : 'organAlias',
						xtype : 'textfield',
						allowBlank : false,
						readOnly : readOnly,
						labelWidth:120,
						maxLength : 32
					});
			formConfig.items.push({
						colspan : 1,
						fieldLabel : '机构编码',
						name : 'organCode',
						xtype : 'textfield',
						allowBlank : false,
						readOnly : readOnly,
						labelWidth:120,
						maxLength : 32
					});
			formConfig.items.push({
						colspan : 1,
						fieldLabel : '父机构',
						name : 'organ',
						addPickerWidth:200,
						labelWidth:120,
						xtype : 'selecttree',
						readOnly : readOnly,
						nameKey : 'organId',
						nameLable : 'organName',
						readerRoot : 'organs',
						keyColumnName : 'organId',
						titleColumnName : 'organName',
						childColumnName : 'organs',
						selectUrl : me.listUrl+"?filterIds="+(oper=='update'&&data?data.organId:'')
					});
			formConfig.items.push({
						colspan : 1,
						fieldLabel : '关联机构',
						name : 'linkOrgan',
						xtype : 'selecttree',
						addPickerWidth:50,
						labelWidth:120,
						readOnly : readOnly,
						nameKey : 'organId',
						nameLable : 'organName',
						readerRoot : 'organs',
						keyColumnName : 'organId',
						titleColumnName : 'organName',
						childColumnName : 'organs',
						selectUrl : me.listUrl+"&filterIds="+(oper=='update'&&data?data.organId:'')
					});
			var organType = modules.baseinfo.Dictionary.createDictionarySelectPanel(
			              '机构类型','ORGAN_TYPE','organType',true,null,true,readOnly);
			organType.labelWidth = 120;
			formConfig.items.push(organType);
			formConfig.items.push({
						colspan : 1,
						fieldLabel : '地区',
						name : 'area',
						xtype : 'textfield',
						allowBlank : false,
						readOnly : readOnly,
						labelWidth:120,
						maxLength : 32
					});
			formConfig.items.push({
						colspan : 1,
						fieldLabel : '邮编',
						name : 'postcode',
						xtype : 'numberfield',
						allowBlank : false,
						readOnly : readOnly,
						labelWidth:120,
						maxLength : 32
					});
			formConfig.items.push({
						colspan : 1,
						fieldLabel : '地址',
						name : 'address',
						xtype : 'textfield',
						allowBlank : false,
						readOnly : readOnly,
						labelWidth:120,
						maxLength : 32
					});
		    formConfig.items.push({
						colspan : 1,
						fieldLabel : '电话',
						name : 'telephone',
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
						fieldLabel : '是否有效',
						name : 'validation',
						xtype : 'select',
						data:[[false,'否'],[true,'是']],
						readOnly : readOnly,
						labelWidth:120,
						defaultValue : true
					});
			formConfig.items.push({
					colspan : 1,
	                name:'sysParemeter', 
	                fieldLabel:'SSOWsKey',
	                inputType:'password',
	                labelWidth:120,
	                validator : function(){
						var IsExsit = true;
						var sysParemeter = me.form.getForm().findField('sysParemeter').getValue(); 
						var sysParemeterQr = me.form.getForm().findField('sysParemeterQr').getValue(); 
						if(!!sysParemeter && !!sysParemeterQr && sysParemeter!='' && sysParemeter!=sysParemeterQr){
							IsExsit ='ssoWsKey与key再次确认不一致!';
						}
						return IsExsit;
					}
	            });
	        formConfig.items.push({ 
	        		colspan : 1,
	        		name:'sysParemeterQr',
	                fieldLabel:'Key再次确认',
	                inputType:'password',
	                labelWidth:120,
	                validator : function(){
						var IsExsit = true;
						var sysParemeter = me.form.getForm().findField('sysParemeter').getValue(); 
						var sysParemeterQr = me.form.getForm().findField('sysParemeterQr').getValue(); 
						if(!!sysParemeter && !!sysParemeterQr && sysParemeter!='' && sysParemeter!=sysParemeterQr){
							IsExsit ='ssoWsKey与key再次确认不一致!';
						}
						return IsExsit;
					}
	            }); 
	        formConfig.items.push({
						colspan : 2,
						fieldLabel : '题库关联编码',
						name : 'bankMapCode',
						xtype : 'textfield',
						readOnly : readOnly,
						labelWidth:120,
						maxLength : 10
					});
		    formConfig.items.push({
						colspan : 2,
						fieldLabel : '备注',
						name : 'remark',
						xtype : 'textarea',
						readOnly : readOnly,
						maxLength : 100,
						labelWidth:120,
						width : 596,
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
						title : '机构',
						autoHeight : true,
						width : 650,
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
					   me.form.getForm().setValues({'organ':{'organId':(data?data.organId:''),'organName':(data?data.organName:'')}});
					}
				}else{
					me.form.getForm().findField('sysParemeterQr').setValue(me.form.getForm().findField('sysParemeter').getValue()); 
				}
			});
		};
		this.callParent();
	}
});