/**
 * 课件库
 */
ClassDefine(
		'modules.hnjtamis.talent.TalentRegistration',
		{
			extend : 'base.model.List',
			requires : [ 'modules.baseinfo.Dictionary' ],
			initComponent : function() {
				var Dictionary = modules.baseinfo.Dictionary;// 类别名
			this.types = Dictionary.getDictionaryList('TALENT_TYPE');
			var me = this;
			// 模块列表对象
			this.columns = [ {
				name : 'id',
				width : 0
			}, {
				name : 'name',
				header : '姓名',
				width : 1,
				sortable: true,
				align:"center"
			}, {
				name : 'quarter.quarterName',
				header : '岗位',
				width : 2,
				sortable: true,
				align:"center"
			}, {
				name : 'specialityNames',
				header : '专业',
				width : 2,
				sortable: true,
				align:"center"
			}, /*{
				name : 'sex',
				header : '性别',
				width : 1,
				renderer : function(value) {
					return value == 1 ? "男" : "女";
				}
			}, {
				name : 'birthday',
				header : '出生日期',
				width : 1
			}, *//*{
				name : 'type',
				header : '专家类型',
				width : 1,
				renderer : function(value) {
					var display = "";
					Ext.Array.each(me.types.datas, function(item) {
						if (item['dataKey'] == value) {
							display = item['dataName'];
						}
					});
					return display;
				},align:"center"
			},*/ /*{
				name : 'electedTime',
				header : '当选时间',
				width : 1,
				sortable: true
			}, {
				name : 'certificate',
				header : '资质证书',
				width : 1,
				sortable: true
			}, {
				name : 'skill',
				header : '技能特长',
				width : 1,
				sortable: true
			}, */
			{
				name : 'bankNames',
				header : '负责的题库',
				width : 3,
				sortable: true,
				renderer:function(value, metadata, record){ 
				    if(value!=undefined && value!=null && value!=""){
				    	metadata.tdAttr = " data-qtip = '"+value+"'";
				    }
				    //metadata.style="white-space: normal !important;";
				    return value; 
				},titleAlign:"center"
			},{
				name : 'fkThemeAuditNum',
				header : '反馈审核试题次数',
				align:"center",
				width : 1,
				renderer:function(value, metadata, record){ 
				    if(value!=undefined && value!=null && value!=""){
				    	metadata.tdAttr = " data-qtip = '反馈审核试题次数："+value+"'";
				    }
				    //metadata.style="white-space: normal !important;";
				    return value; 
				}
			}, {
				name : 'examMarkNum',
				header : '阅卷次数',
				align:"center",
				width : 1,
				renderer:function(value, metadata, record){ 
				    if(value!=undefined && value!=null && value!=""){
				    	metadata.tdAttr = " data-qtip = '阅卷次数："+value+"'";
				    }
				    //metadata.style="white-space: normal !important;";
				    return value; 
				}
			}, {
				name : 'isDel',
				header : '是否注销',
				width : 1,
				renderer : function(value) {
					return value == 0 ? "<font color=green>否</font>" : "<font color=red>是</font>";
				},
				align:"center"
			}, {
				name : 'isAudited',
				header : '是否审核',
				width : 1,
				renderer : function(value) {
					return value == 0 ? "<font color=red>否</font>" : "<font color=green>是</font>";
				},
				align:"center"
			} ];
			// 模块查询条件对象
			this.termWidth = 280
			this.terms = [
					{
						xtype : 'textfield',
						name : 'nameTerm',
						fieldLabel : '姓名',
						labelWidth : 85,
						width:257
					},
					{
						name : 'quarterTerm',
						fieldLabel : '岗位',
						addPickerWidth : 200,
						xtype : 'selecttree',
						nameKey : 'quarterId',
						nameLable : 'quarterName',
						// readerRoot : 'quarters',
						keyColumnName : 'id',
						titleColumnName : 'title',
						childColumnName : 'children',
						editorType : 'str',// 编辑类型为字符串，不是对象
						selectType : 'quarter',
						selectTypeName : '岗位',
						labelWidth : 100,
						width:257,
						selectUrl : "organization/quarter/odqtreeForQuarterListAction!odqtree.action?organTerm="
								//+ base.Login.userSession.currentOrganId
					},
					{
						name : 'specialityTerm',
						fieldLabel : '专业',
						addPickerWidth : 40,
						labelWidth : 100,
						width:257,
						xtype : 'selecttree',
						nameKey : 'specialityid',
						nameLable : 'specialityname',
						// readerRoot : 'quarters',
						keyColumnName : 'id',
						titleColumnName : 'title',
						childColumnName : 'children',
						editorType : 'str',// 编辑类型为字符串，不是对象
						selectUrl : "baseinfo/speciality/treeForSpecialityListAction!tree.action?jobidTerm="
					},
					Dictionary
							.createDictionarySelectPanel('专家类型', 'TALENT_TYPE',
									'typeTerm', true, null, false, false) ];
			this.keyColumnName = "id";// 主健属性名
			this.viewOperater = true;
			this.addOperater = true;
			this.deleteOperater = false;
			this.updateOperater = false;
			this.otherOperaters = [];// 其它扩展按钮操作
			this.otherOperaters
					.push( 							
							{
					            xtype : 'button',
								icon : 'resources/icons/fam/pencil.png',
								text : '修改',
								handler : function() {
									var id = me.getSelectIds();
									if(id==""||id.split(",").length>1){
										Ext.Msg.alert('提示', '请选择一条记录！');
										return;
									}
									var selected = me.getSelectionModel().selected;					
									var record = selected.get(0);
									var isAudited = record.get("isAudited");
									if(isAudited != 1){
										var queryTerm = {};
										if(me.termForm)
											queryTerm = me.termForm.getValues(false);
										me.openFormWin(id, function() {
													me.termQueryFun(false,'flash');//me.pagingToolbar.doRefresh();
												},false,record.data,queryTerm,'update');
									}else{
										Ext.Msg.confirm('提示', '该记录已审核，如您修改并保存数据需要重新审核?',function(bt){
								            if(bt=='yes'){
								            	var queryTerm = {};
												if(me.termForm)
														queryTerm = me.termForm.getValues(false);
												me.openFormWin(id, function() {
													me.termQueryFun(false,'flash');//me.pagingToolbar.doRefresh();
												},false,record.data,queryTerm,'update');
								            }
								        });
										//Ext.Msg.alert('提示', '该条记录已经审核，不能进行修改！');
									}
								}
							},{
					            xtype : 'button',
								icon : 'resources/icons/fam/delete.gif',
								text : '注销',
								handler : function() {
									var selected = me.getSelectionModel().selected;
									if (selected.getCount() == 0) {
										Ext.Msg.alert('提示', '请选择记录！');
										return;
									}
									var id = me.getSelectIds();
									//if(isDel == 0){
										Ext.MessageBox.confirm('询问', '确定注销该条记录吗？',function(btn){
											if(btn=='yes'){
												EapAjax.request({
													method : 'GET',
													url : 'talent/reg/deleteForTalentRegistrationListAction!delete.action?id='+id,
													async : true,
													success : function(response) {
														var result = Ext.decode(response.responseText);
														if (Ext.isArray(result)&&result[0].errors) {
															var msg = result[0].errors;
															Ext.Msg.alert('错误', msg);
														} else if(result.msg){
															Ext.Msg.alert('提示', result.msg);
															me.termQueryFun(false,'flash');
														}else {
															Ext.Msg.alert('提示', '注销成功！');
															me.termQueryFun(false,'flash');
														}
													},
													failure : function() {
														Ext.Msg.alert('提示', '后台未响应，网络异常！');
													}
												});
											}
										});
									//}else{
										//Ext.Msg.alert('提示', '该条记录已注销，不需要此操作！');
										//Ext.Msg.alert('提示', '该条记录已经审核，不能删除！');
									//}
								}
							},{
					            xtype : 'button',
								icon : 'resources/icons/fam/accept.png',
								text : '启用',
								handler : function() {
									var selected = me.getSelectionModel().selected;
									if (selected.getCount() == 0) {
										Ext.Msg.alert('提示', '请选择记录！');
										return;
									}
									var id = me.getSelectIds();
										Ext.MessageBox.confirm('询问', '确定启用该条记录吗？',function(btn){
											if(btn=='yes'){
												EapAjax.request({
													method : 'GET',
													url : 'talent/reg/reuseForTalentRegistrationListAction!reuse.action?id='+id,
													async : true,
													success : function(response) {
														var result = Ext.decode(response.responseText);
														if (Ext.isArray(result)&&result[0].errors) {
															var msg = result[0].errors;
															Ext.Msg.alert('错误', msg);
														} else if(result.msg){
															Ext.Msg.alert('提示', result.msg);
															me.termQueryFun(false,'flash');
														} else {
															Ext.Msg.alert('提示', '启用成功！');
															me.termQueryFun(false,'flash');
														}
													},
													failure : function() {
														Ext.Msg.alert('提示', '后台未响应，网络异常！');
													}
												});
											}
										});
									//}else{
										//Ext.Msg.alert('提示', '该条记录未注销，不需要此操作！');
									//}
								}
							},
							{
						xtype : 'button',
						icon : 'resources/icons/fam/accept.png',
						text : '审核',
						handler : function() {
							Ext.MessageBox.confirm('询问','确定审核通过吗？',
											function(btn) {
												if (btn != 'yes')
													return;
												var id = me.getSelectIds();
												if (id == ""|| id.split(",").length > 1) {
													Ext.Msg.alert('提示','请选择一条记录！');
													return;
												}
												EapAjax.request( {
															method : 'GET',
															url : 'talent/reg/auditForTalentRegistrationListAction!audit.action?id=' + id,
															async : true,
															success : function(
																	response) {
																var result = Ext.decode(response.responseText);
																if (Ext.isArray(result)
																		&& result[0].errors) {
																	var msg = result[0].errors;
																	Ext.Msg.alert('错误',msg);
																} else {
																	Ext.Msg.alert('信息','操作成功！');
																	me.termQueryFun(false,'flash');
																}
															},
															failure : function() {
																Ext.Msg.alert('信息','后台未响应，网络异常！');
															}
														});
											});
						}
					});
			me.syncBankBtn = Ext.create("Ext.Button", {
				icon : 'resources/icons/fam/book.png',//按钮图标
				text : '同步关联题库',
				handler : function() {
							Ext.MessageBox.confirm('询问', '系统将根据专家对应专业，专业对应的模块，模块对应题库，自动进行专家与题库的匹配，系统将会清除原有数据，您确定继续操作吗？',
									function(btn) {
										if (btn == 'yes'){
											me.alertDelMask = new Ext.LoadMask(me, {  
											    msg     : '数据正在处理,请稍候',  
											    removeMask  : true// 完成后移除  
											});
											me.alertDelMask.show();
											Ext.Ajax.request({
												method : 'POST',
												url : 'talent/reg/syncBankForTalentRegistrationListAction!syncBank.action',
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
		this.otherOperaters.push(me.syncBankBtn);
		me.extTrgBtGroup = Ext.create("Ext.Button", {
			    text: "导入", 
			    icon : 'resources/icons/fam/xls.gif',//按钮图标
			    menu: 
			    { 
			        items: [{
					icon : 'resources/icons/fam/import.gif',
					text : '导入专家',
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
					                        url:'talent/reg/importXlsForTalentRegistrationListAction!importXls.action',
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
									title : '导入专家信息  - [导入文件必须是.xls结尾的Excel文件]',
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
				},{
					icon : 'resources/icons/fam/export.gif',//按钮图标
					text : "导出模版",//按钮标题
					handler : function() {//按钮事件
						window.open("./modules/hnjtamis/talent/downMasterplate.jsp");
					}
				}
			]}
		 });
		 this.otherOperaters.push(me.extTrgBtGroup);
		 

			this.listUrl = "talent/reg/listForTalentRegistrationListAction!list.action";//?organTerm="+base.Login.userSession.currentOrganId;// 列表请求地址
			this.deleteUrl = "talent/reg/deleteForTalentRegistrationListAction!delete.action;"//organTerm="+base.Login.userSession.currentOrganId;// 删除请求地址
			// 打开表单页面方法
			this.openFormWin = function(id, callback, readOnly,data,term,oper){
				var formConfig = {};
				var readOnly = readOnly || false;
				formConfig.readOnly = readOnly;
				formConfig.fileUpload = true;
				formConfig.formUrl = "talent/reg/saveForTalentRegistrationFormAction!save.action";
				formConfig.findUrl = "talent/reg/findForTalentRegistrationFormAction!find.action";
				formConfig.callback = callback;
				formConfig.columnSize = 2;
				formConfig.jsonParemeterName = 'form';
				formConfig.oper = oper;// 复制操作类型变量
				formConfig.items = new Array();
				formConfig.items.push( {
					colspan : 2,
					xtype : 'hidden',
					name : 'id'
				});
				formConfig.items.push( {
					colspan : 2,
					xtype : 'hidden',
					name : 'organ.organId'
				});
				formConfig.items.push( {
					colspan : 1,
					name : 'name',
					xtype : 'hidden'
				});
				formConfig.items.push({
					colspan : 1,
					fieldLabel : '姓名',
					name : 'employee',
					xtype : 'selecttree',
					readOnly : readOnly,
					checked : false,
					addPickerWidth : 350,
					allowBlank : false,
					nameKey : 'employeeId',
					nameLable : 'employeeName',
					keyColumnName : 'id',
					titleColumnName : 'title',
					childColumnName : 'children',
					selectType:'employee',
					selectTypeName:'员工',
					labelWidth : 150,
					//editorType:'str',
					selectUrl : "organization/employee/opxdetreeForEmployeeListAction!opxdetree.action",//?organTerm="+base.Login.userSession.currentOrganId,
					selectEventFun:function(combo,record,index){
						var nameField = form.getForm().findField('name');
						nameField.setValue(combo.title);
						EapAjax.request( {
							method : 'GET',
							url : 'organization/employee/findForEmployeeFormAction!find.action?id=' + combo.value,
							async : true,
							success : function(
									response) {
								var result = Ext.decode(response.responseText);
								if(result.form) {
									form.getForm().setValues({'quarter':{'quarterId':result.form.quarter.quarterId,'quarterName':result.form.quarter.quarterName}});
								}
							},
							failure : function() {
								Ext.Msg.alert('信息','后台未响应，获取员工岗位信息失败！');
							}
						});
					}
				});
				var zjlx = modules.baseinfo.Dictionary
						.createDictionarySelectPanel('专家类型', 'TALENT_TYPE',
								'type', true, null, true, readOnly);
				zjlx.labelWidth = 150;
				formConfig.items.push(zjlx);
				formConfig.items.push( {
					colspan : 1,
					fieldLabel : '性别',
					name : 'sex',
					xtype : 'select',
					labelWidth : 150,
					data : [ [ 1, '男' ], [ 2, '女' ] ],
					readOnly : readOnly,
					defaultValue : 1
				});
				formConfig.items.push( {
					colspan : 1,
					fieldLabel : '出生日期',
					name : 'birthday',
					xtype : 'datefield',
					format : 'Y-m-d',
					allowBlank : true,
					readOnly : readOnly,
					// size:30,
					labelWidth : 150,
					maxLength : 10
				});
				formConfig.items.push({
					colspan : 2,
					fieldLabel : '注册机构',
					name : 'organ',
					xtype : 'selecttree',
					readOnly : readOnly,
					//addPickerWidth:300,
					nameKey : 'organId',
					nameLable : 'organName',
					readerRoot : 'organs',
					keyColumnName : 'organId',
					titleColumnName : 'organName',
					childColumnName : 'organs',
					selectUrl : "organization/organ/listForOrganListAction!list.action",
					labelWidth : 150,
					width : 675
				});
				formConfig.items
						.push( {
							colspan : 1,
							name : 'quarter',
							fieldLabel : '岗位',
							checked : false,
							allowBlank : false,
							addPickerWidth : 300,
							xtype : 'selecttree',
							readOnly : readOnly,
							nameKey : 'quarterId',
							nameLable : 'quarterName',
							// readerRoot : 'quarters',
							keyColumnName : 'id',
							titleColumnName : 'title',
							childColumnName : 'children',
							selectType : 'quarter',
							selectTypeName : '岗位',
							labelWidth : 150,
							selectUrl : "organization/quarter/odqtreeForQuarterListAction!odqtree.action?organTerm=",
									//+ base.Login.userSession.currentOrganId,
							selectEventFun:function(combo,record,index){
								var specialityField = form.getForm().findField('speciality');
								specialityField.reflash("baseinfo/speciality/treeForSpecialityListAction!tree.action?jobidTerm="+(combo.value));								
							}
						});
				formConfig.items.push( {
					colspan : 1,
					fieldLabel : '参加工作年月',
					name : 'wordDay',
					xtype : 'textfield',
					allowBlank : false,
					maxLength: 10,
					labelWidth : 150,
					readOnly : readOnly
				});
				formConfig.items.push( {
					colspan : 1,
					fieldLabel : '现岗位及职务',
					name : 'quarterDuty',
					xtype : 'textfield',
					allowBlank : false,
					maxLength: 40,
					labelWidth : 150,
					readOnly : readOnly
				});
				formConfig.items.push( {
					colspan : 1,
					fieldLabel : '专业技术资格情况',
					name : 'specialityGrade',
					xtype : 'textfield',
					allowBlank : false,
					maxLength: 40,
					labelWidth : 150,
					readOnly : readOnly
				});
				formConfig.items.push( {
					colspan : 1,
					fieldLabel : '资格年限（年）',
					name : 'specialityGradeYear',
					xtype : 'numberfield',
					maxLength : 2,
					allowNegative:false, //不允许输入负数
					allowDecimals:false, //不允许输入小数   
					allowBlank : true,
					nanText:"请输入正整数",
					minValue: 0,     //最小值
					labelWidth : 150,
					readOnly : readOnly
				});
				formConfig.items.push( {
					colspan : 1,
					fieldLabel : '现技能等级',
					name : 'skillGrade',
					xtype : 'textfield',
					allowBlank : false,
					maxLength: 40,
					labelWidth : 150,
					readOnly : readOnly
				});
				formConfig.items.push( {
					colspan : 1,
					fieldLabel : '资格年限（年）',
					name : 'skillGradeYear',
					xtype : 'numberfield',
					maxLength : 2,
					allowNegative:false, //不允许输入负数
					allowDecimals:false, //不允许输入小数   
					allowBlank : true,
					nanText:"请输入正整数",
					minValue: 0,     //最小值
					labelWidth : 150,
					readOnly : readOnly
				});
				formConfig.items.push( {
					colspan : 1,
					fieldLabel : '112人才情况',
					name : 'explain211',
					xtype : 'textfield',
					allowBlank : true,
					maxLength: 40,
					labelWidth : 150,
					readOnly : readOnly,
					hidden:true
				});
				formConfig.items.push( {
					colspan : 1,
					fieldLabel : '集团及以上级荣誉',
					name : 'explainGroup',
					xtype : 'textfield',
					allowBlank : true,
					maxLength: 40,
					labelWidth : 150,
					readOnly : readOnly
				});
				
				/*formConfig.items
						.push( {
							colspan : 2,
							name : 'speciality',
							fieldLabel : '专业',
							checked : true,
							allowBlank : false,
							// addPickerWidth:100,
							xtype : 'selecttree',
							readOnly : readOnly,
							nameKey : 'specialityid',
							nameLable : 'specialityname',
							// readerRoot : 'quarters',
							keyColumnName : 'id',
							titleColumnName : 'title',
							childColumnName : 'children',
							selectType : 'speciality',
							selectTypeName : '专业',
							selectUrl : "baseinfo/speciality/treeForSpecialityListAction!tree.action?jobidTerm="
						});*/
				formConfig.items.push( {
					colspan : 1,
					fieldLabel : '当选时间',
					name : 'electedTime',
					xtype : 'datefield',
					format : 'Y-m-d',
					allowBlank : false,
					readOnly : readOnly,
					// size:30,
					labelWidth : 150,
					maxLength : 10
				});
				formConfig.items.push( {
					colspan : 1,
					fieldLabel : '资质证书',
					name : 'certificate',
					xtype : 'textfield',
					allowBlank : false,
					labelWidth : 150,
					maxLength : 50,
					readOnly : readOnly
				});
				formConfig.items.push({
					colspan : 2,
					checked : true,
					allowBlank : true,
					//addPickerWidth:300,
					fieldLabel:"负责的题库",
					xtype : 'selecttree',
					name : 'talentRegistrationBanks',
					nameKey : 'bankId',
					nameLable : 'bankName',
					readerRoot : 'children',
					keyColumnName : 'id',
					titleColumnName : 'title',
					childColumnName : 'children',
					selectType : 'bank',
					selectTypeName : '题库',
					selectUrl : 'base/themebank/specialityThemeBankTreeForThemeBankListAction!specialityThemeBankTree.action',
					readOnly : readOnly,
					labelWidth : 150,
					width : 675
				});
				formConfig.items.push( {
					colspan : 2,
					fieldLabel : '技能特长',
					name : 'skill',
					xtype : 'textarea',
					allowBlank : false,
					readOnly : readOnly,
					maxLength : 2000,
					width : 675,
					labelWidth : 150,
					height : 50
				});
				formConfig.items.push( {
					colspan : 2,
					fieldLabel : '其它奖励',
					name : 'otherInfo',
					xtype : 'textfield',
					allowBlank : true,
					size:70,
					width : 675,
					maxLength: 100,
					labelWidth : 150,
					readOnly : readOnly
				});
				
				formConfig.items.push(me.setSpecialityForm(readOnly));
				
				var form = ClassCreate('base.model.Form', formConfig);
				form.validate = function(){
					return me.specialityForm.validate();
				}
				form.parentWindow = this;
				// 表单窗口
				var formWin = new WindowObject( {
					layout : 'fit',
					title : '专家登记',
					//height : 380,
					width : 750,
					border : false,
					frame : false,
					modal : true,// 模态
					closeAction : 'hide',
					items : [ form ]
				});
				form.setFormData(id,function(result){
					if(oper == 'update'){	
						/*setTimeout(function(){
							form.getForm().isValid();
						},300);*/
					}
					
				});
				formWin.show();
			};
			this.callParent();
		},
		setSpecialityForm : function(readOnly){
			var me = this;
			me.specialityForm = ClassCreate('base.core.EditList',{
				colspan : 2,
				fieldLabel : '专业',
				name : 'specialitys',
				xtype : 'editlist',
				addOperater : true,
				deleteOperater : true,
				readOnly : readOnly,
				viewConfig:{height:150,autoScroll:true},//高度
				columns : [
							{
								width : 0,
								name : 'reSpecialityId'
							},{
								name : 'speciality',
								header : '专业',
								width : 2,
								sortable:false,
								menuDisabled:true,
								titleAlign:"center",
								editor : {
									checked : false,
									allowBlank : false,
									xtype : 'selecttree',
									readOnly : readOnly,
									nameKey : 'specialityid',
									nameLable : 'specialityname',
									// readerRoot : 'quarters',
									keyColumnName : 'id',
									titleColumnName : 'title',
									childColumnName : 'children',
									selectType : 'speciality',
									selectTypeName : '专业',
									selectUrl : "baseinfo/speciality/treeForSpecialityListAction!tree.action?jobidTerm="
								},
								renderer : function(value){
									return value['specialityname'];
								}
							},{
								name : 'toZz',
								header : '是否组长',
								align:'center',
								width : 0.5 ,
								sortable:false,
								menuDisabled:true,
								titleAlign:"center",
								editor : {
									xtype : 'checkbox',
									readOnly : readOnly
								},
								renderer:function(value){
									if(value){
										return "是";
									}else{
										return "否";
									}
								}
							},{
								name : 'toFzz',
								header : '是否副组长',
								align:'center',
								width : 0.5 ,
								sortable:false,
								menuDisabled:true,
								titleAlign:"center",
								editor : {
									xtype : 'checkbox',
									readOnly : readOnly
								},
								renderer:function(value){
									if(value){
										return "是";
									}else{
										return "否";
									}
								}
							}
				           ]
			});
			return me.specialityForm;
		}
	});