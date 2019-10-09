/**
 * 专业管理 
 * Date : 2015.3.25
 * Desc : 基础信息模块-专业信息维护
 * Author: Wangyong
 */
ClassDefine('modules.hnjtamis.baseinfo.speciality.Speciality', {
	extend : 'base.model.TreeList',
	setOtherAttribute:function(data){
		if(data.id.indexOf("_")>0){
			data.closeIcon = 'resources/icons/fam/theme.gif';
		   	data.icon='resources/icons/fam/theme.gif';
		}
		else{
			data.closeIcon = 'resources/icons/fam/plugin_add.gif';
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
					width : 3
				}, {
					name : 'typecode',
					header : '编码',
					width : 1
				}, {
					name : 'isavailable',
					header : '是否有效',
					width : 0.5,
					renderer : function(value){
						return value==0?"无效":"有效";
					}
				}, {
					name : 'remarks',
					header : '备注',
					width : 2
				}/*, {
					name : 'creationDate',
					header : '创建时间',
					width : 1
				}, {
					name : 'createdBy',
					header : '创建人员',
					width : 0.5
				}, {
					name : 'lastUpdatedBy',
					header : '最后修改人员',
					width : 1
				}*/];
		// 模块查询条件对象
		this.terms = [{
					xtype : 'textfield',
					name  : 'typenameTerm',
					width : 270,
					fieldLabel : '类型名称'
				}];
		this.keyColumnName = "bstid";// 主健属性名
		this.viewOperater = true;
		this.addOperater = true;
		this.deleteOperater = true;
		this.updateOperater = true;
		//this.readerRoot = 'subSpecialitytypes';/////
		this.listUrl = "baseinfo/speciality/listForSpecialityListAction!list.action";// 列表请求地址
		this.deleteUrl = "baseinfo/speciality/listForSpecialityListAction!delete.action";// 删除请求地址
		this.childColumnName = 'subSpecialitytypes';// 子集合的属性名
		this.grabl_tree_numberColumnWidth = 60;
		
		this.otherOperaters = [];//其它扩展按钮操作
		me.syncBtn = Ext.create("Ext.Button", {
				icon : 'resources/icons/fam/book.png',//按钮图标
				text : '同步岗位对应专业',
				handler : function() {
							Ext.MessageBox.confirm('询问', '系统将根据专业对应的模块，以及岗位对应的模块，自动进行岗位与专业的匹配，系统将会清除原有数据，您确定继续操作吗？',
									function(btn) {
										if (btn == 'yes'){
											me.alertDelMask = new Ext.LoadMask(me, {  
											    msg     : '数据正在处理,请稍候',  
											    removeMask  : true// 完成后移除  
											});
											me.alertDelMask.show();
											Ext.Ajax.request({
												method : 'POST',
												url : 'baseinfo/speciality/syncJobsUnionSpecialityForSpecialityListAction!syncJobsUnionSpeciality.action',
												timeout: 600000,
												success : function(response) {
													me.alertDelMask.hide();
													var result = Ext.decode(response.responseText);
													Ext.Msg.alert('信息', result.msg);
												},
												failure : function(response) {
													me.alertDelMask.hide();
													var result = Ext.decode(response.responseText);
													if (result && result.msg)
														Ext.Msg.alert('信息', result.msg);
													else
														Ext.Msg.alert('信息', '后台未响应，网络异常！');
												}
											});
										}
									}
							 );
						}
					});
		this.otherOperaters.push(me.syncBtn);			
		me.extSpBtGroup = Ext.create("Ext.Button", {
			    text: "导入", 
			    icon : 'resources/icons/fam/xls.gif',//按钮图标
			    menu: 
			    { 
			        items: [{
					icon : 'resources/icons/fam/import.gif',
					text : '导入专业',
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
					                fieldLabel:'导入文件', 
					                xtype:'fileuploadfield',
					                buttonText:'浏览',
					                name:'xls',
					                emptyText:'请选择EXCEL文件。',
					                colspan : 2,
					                width : 540,
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
					                        url:'baseinfo/speciality/importXlsForSpecialityListAction!importXls.action',
					                        success:function(form, action){
					                        	var msg = action.result.msg; 
					                        	if (!msg) msg = '操作成功！';
					                        	Ext.Msg.alert('信息', msg,function(){
					                                 importWin.hide();
					                                 delete importWin;
					                                 me.termQueryFun();
					                            }); 
					                        	var tmpPosition = importWin.getPosition();
					                        	importWin.hide();
				                                delete importWin;
				                                me.termQueryFun();
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
									title : '导入专业  - [导入文件必须是.xls结尾的Excel文件]',
									autoHeight : true,
									width : 600,
									border : false,
									frame : false,
									modal : true,//模态
									closeAction : 'hide',
									items : [importForm]
								});
						importWin.show();
					}
				},/*{
					icon : 'resources/icons/fam/accept.png',
					text : "导入确认",//按钮标题
					handler : function() {
					var selected = me.getSelectionModel().selected;
						if (selected.getCount() == 0) {
							Ext.Msg.alert('提示', '请选择记录,仅导入成功的试题才能发布！');
							return;
						}
						var confirmFn = function(btn) {
									if (btn == 'yes'){
										var ids = me.getSelectIds();
										var examMaskTip = EapMaskTip(document.body);
										examMaskTip.show();
										Ext.Ajax.request({
											method : 'POST',
											url : 'baseinfo/speciality/expPublicSpForThemeListAction!expPublicSp.action',
											timeout: 120000,
											success : function(response) {
												var result = Ext.decode(response.responseText);
												var selected = me.getSelectionModel().selected;
												examMaskTip.hide();
												if(result.success==true){
													Ext.Msg.alert('信息', result.msg, function(btn) {
														if(me.pagingToolbar){
															var pageData = me.pagingToolbar.getPageData();
															if (pageData.currentPage < pageData.pageCount)
																me.termQueryFun(false,'flash');//me.pagingToolbar.doRefresh();
															else {
																if (pageData.currentPage==1||
																(pageData.toRecord - pageData.fromRecord + 1) > selected.getCount())
																	me.termQueryFun(false,'flash');//me.pagingToolbar.doRefresh();
																else
																	me.termQueryFun(false,'previous');//me.pagingToolbar.movePrevious();
															}
														}else
														   me.store.load();
													});
												}else{
												   Ext.Msg.alert('错误提示', result[0].errors);
												}
											},
											failure : function(response) {
												var result = Ext.decode(response.responseText);
												examMaskTip.hide();
												if (result && result.length > 0)
													Ext.Msg.alert('错误提示', result[0].errors);
												else
													Ext.Msg.alert('信息', '后台未响应，网络异常！');
											},
											params : "id=" + ids
										});
									}
						};
						Ext.MessageBox.confirm('询问', '你真要发布这些试题吗？', confirmFn);
					}
				},*/{
					icon : 'resources/icons/fam/export.gif',//按钮图标
					text : "导出模版",//按钮标题
					handler : function() {//按钮事件
						window.open("./modules/hnjtamis/baseinfo/speciality/downMasterplate.jsp");
					}
				}
			]}
		 });
		
		this.otherOperaters.push(me.extSpBtGroup);
		
		// 打开表单页面方法
		this.openFormWin = function(id, callback, readOnly,data,term,oper) {
			var me = this;
			var formConfig = {};
			var readOnly = readOnly || false;
			
			if ((oper=='view' || oper=='update') && id.indexOf('_')==-1){
				Ext.Msg.alert('错误', '请选择子专业节点');
				return;
			}
			
			formConfig.readOnly = readOnly;
			formConfig.fileUpload = true;           
			formConfig.formUrl = "baseinfo/speciality/saveForSpecialityFormAction!save.action";
			formConfig.findUrl = "baseinfo/speciality/findForSpecialityFormAction!find.action";
			formConfig.callback = callback;
			formConfig.columnSize = 1;
			formConfig.jsonParemeterName = 'form';
			formConfig.items = new Array();
			formConfig.oper = oper;// 复制操作类型变量
			formConfig.items.push({
						colspan : 1,
						xtype : 'hidden',
						name : 'specialityid'
					});
			formConfig.items.push({
						colspan : 1,
						fieldLabel : '专业名称',
						name : 'specialityname',
						xtype : 'textfield',
						allowBlank : false,
						readOnly : readOnly,
						maxLength : 32
					});
			formConfig.items.push({
						colspan : 1,
						fieldLabel : '专业编码',
						name : 'specialitycode',
						xtype : 'textfield',
						allowBlank : false,
						readOnly : readOnly,
						maxLength : 32
					});
			formConfig.items.push({
						colspan : 1,
						fieldLabel : '专业类型',
						name : 'specialityType',
						addPickerWidth:100,
						xtype : 'selecttree',
						readOnly : readOnly,
						nameKey : 'bstid',
						nameLable : 'typename',
						readerRoot : 'children',
						keyColumnName : 'id',
						titleColumnName : 'title',
						childColumnName : 'children',
						selectType : 'specialitytype',
						selectTypeName : '专业类型',
						allowBlank : false,
						selectUrl : 'baseinfo/specialitytype/specialtreeForSpecialityTypeListAction!specialtree.action'
					});
			formConfig.items.push({
						colspan : 1,
						fieldLabel : '关联模块',
						name : 'specialityStandardTypeslist',
						addPickerWidth:100,
						checked : true,
						xtype : 'selecttree',
						readOnly : readOnly,
						nameKey : 'typesId',
						nameLable : 'typesName',
						readerRoot : 'children',
						keyColumnName : 'id',
						titleColumnName : 'title',
						childColumnName : 'children',
						selectType : 'stmodel',
						selectTypeName : '关联模块',
						allowBlank : false,
						width : 538,
						selectUrl : 'jobstandard/termsEx/getStandardModelTreeForStandardTermsExListAction!getStandardModelTree.action'
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
						title : '专业',
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
					   //var v = {'dept':{'deptId':term.deptTerm,'deptName':''}};
					   //form.getForm().findField('quarter').reflash(
					       //"organization/quarter/listForQuarterListAction!list.action?deptTerm="+term.deptTerm);
					   //form.getForm().setValues(v);
					}
				}else
				if(oper == 'update'){
					//form.getForm().findField('quarter').reflash(
					//       "organization/quarter/listForQuarterListAction!list.action?deptTerm="+result.form.dept.deptId,false);
				}
			});
		};
		me.callParent();
	}
});