/**
 * 应用系统的模块类
 */
ClassDefine('modules.funres.App', {
			extend : 'base.model.List',
			initComponent : function() {
				var me = this;
				//模块列表对象
				this.columns = [{
							name : 'appId',
							width : 0
						}, {
							name : 'appCode',
							header : '应用编码',
							width : 2
						}, {
							name : 'appName',
							header : '应用名称',
							width : 3
						}, {
							name : 'appUrl',
							header : '系统地址',
							width : 4
						}, {
							name : 'description',
							header : '系统描述',
							width : 5
						}, {
							name : 'stat',
							header : '状态',
							width : 2,
							renderer:function(value){
							    return value==1?"运行":"调试";
						    }
						}];
				//模块查询条件对象
				this.terms = [{
							xtype : 'textfield',
							name : 'nameTerm',
							fieldLabel : '名称'
						}];
				this.keyColumnName = "appId";//主健属性名
				this.viewOperater = true;
		        this.addOperater = true;
		        this.deleteOperater = true;
		        this.updateOperater = true;
		        this.otherOperaters = [];//其它扩展按钮操作
		        this.otherOperaters.push({
			                xtype : 'button',
							icon : 'resources/icons/fam/export.gif',
							text : '导出',
							handler : function() {
								var id = me.getSelectIds();
								if(id==""||id.split(",").length>1){
									Ext.Msg.alert('提示', '请选择一系统！');
									return;
								}
								window.location = 'funres/app/exportXlsForAppListAction!exportXls.action?id='+id;
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
							                        url:'funres/app/importXlsForAppListAction!importXls.action',
							                        success:function(form, action){
							                        	Ext.Msg.alert('提示', '导入成功!',function(){
							                                   importWin.hide();
							                                   delete importWin;
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
											title : '导入系统模块资源菜单及字典',
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
				this.listUrl = "funres/app/listForAppListAction!list.action";//列表请求地址 
				this.deleteUrl = "funres/app/deleteForAppListAction!delete.action";//删除请求地址
				//打开表单页面方法
				this.openFormWin = function(id, callback,readOnly,data,term,oper) {
					var formConfig = {};
					var readOnly = readOnly||false;
			        formConfig.readOnly = readOnly;
					formConfig.formUrl = "funres/app/saveForAppFormAction!save.action";
					formConfig.findUrl = "funres/app/findForAppFormAction!find.action";
					formConfig.callback = callback;
					formConfig.columnSize = 2;
					formConfig.jsonParemeterName = 'form';
					formConfig.items = new Array();
					formConfig.oper=oper;//复制操作类型变量
			        formConfig.items.push({
						colspan : 1,
						xtype : 'hidden',
						name : 'appId'
					});
					formConfig.items.push({
						colspan : 1,
						xtype : 'hidden',
						readOnly : readOnly,
						name : 'orderNo',
						value:1
					});
					formConfig.items.push({
						colspan : 1,
						fieldLabel : '应用编码',
						allowBlank : false,
						readOnly : readOnly,
						xtype : 'textfield',
						maxLength : 32,
						name : 'appCode'
					});
					formConfig.items.push({
						colspan : 1,
						fieldLabel : '应用名称',
						allowBlank : false,
						xtype : 'textfield',
						readOnly : readOnly,
						maxLength : 32,
						name : 'appName'
					});
					formConfig.items.push({
						colspan : 1,
						fieldLabel : '状态',
						xtype : 'select',
						allowBlank : false,
						readOnly : readOnly,
						data:[[1,'运行'],[2,'调试']],
						defaultValue : 1,
						name : 'stat'
					});
					formConfig.items.push({
						colspan : 1,
						fieldLabel : '独立机构',
						xtype : 'select',
						allowBlank : false,
						readOnly : readOnly,
						data:[['0','否'],['1','是']],
						defaultValue : 0,
						name : 'hasorgan'
					});
					formConfig.items.push({
						colspan : 1,
						fieldLabel : '独立权限',
						xtype : 'select',
						allowBlank : false,
						readOnly : readOnly,
						data:[['0','否'],['1','是']],
						defaultValue : 0,
						name : 'haspopedom'
					});
					formConfig.items.push({
						colspan : 1,
						fieldLabel : '独立系统',
						xtype : 'select',
						readOnly : readOnly,
						allowBlank : false,
						data:[['0','否'],['1','是']],
						defaultValue : 0,
						name : 'hasswaraj'
					});
					formConfig.items.push({
						colspan : 2,
						fieldLabel : '应用地址',
						allowBlank : false,
						readOnly : readOnly,
						xtype : 'textfield',
					    size:60,
						maxLength : 100,
						name : 'appUrl'
					});
					formConfig.items.push({
						colspan : 2,
						fieldLabel : '首页地址',
						allowBlank : false,
						readOnly : readOnly,
						xtype : 'textfield',
					    size:60,
						maxLength : 200,
						name : 'indexUrl'
					});
					formConfig.items.push({
						colspan : 1,
						fieldLabel : '图标',
						allowBlank : false,
						readOnly : readOnly,
						xtype : 'selectimage',
						name : 'smallPic'
					});
					formConfig.items.push({
						colspan : 1,
						fieldLabel : '主题',
						allowBlank : false,
						xtype : 'select',
						readOnly : readOnly,
						selectUrl:'baseinfo/dic/findDataForDicFormAction!findData.action?typeCode=THEME_TYPE',
						valueField:'dataKey',
						displayField:'dataName',
						jsonParemeterName:'datas',
						name : 'theme'
					});
					formConfig.items.push({
							colspan : 2,
							width : 540,
							fieldLabel : '系统管理员',
							checked : true,
							name : 'systemMangers',
							xtype : 'selecttree',
							readOnly : readOnly,
							nameKey : 'userId',
							nameLable : 'account',
							//readerRoot : 'children',
							selectType:'user',
							keyColumnName : 'id',
							titleColumnName : 'title',
							childColumnName : 'children',
							selectUrl : "power/user/userTreeForUserListAction!userTree.action?organTerm="+base.Login.userSession.currentOrganId
						});
					formConfig.items.push({
						colspan : 2,
						fieldLabel : '应用描述',
						xtype : 'textarea',
						maxLength : 250,
						readOnly : readOnly,
						name : 'description',
						height : 60,
						width : 542
					});
					var form = ClassCreate('base.model.Form', formConfig);
					//表单窗口
					var formWin = new WindowObject({
								layout : 'form',
								title : '应用系统',
								autoHeight : true,
								width : 600,
								border : false,
								frame : false,
								modal : true,//模态
								closeAction : 'hide',
								items : [form]
							});
					formWin.show();
					form.setFormData(id);
				};
				this.callParent();
			}
		});