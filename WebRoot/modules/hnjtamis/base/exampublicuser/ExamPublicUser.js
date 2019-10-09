/**
 * 考生报名管理的模块类
 */
ClassDefine('modules.hnjtamis.base.exampublicuser.ExamPublicUser', {
	extend : 'modules.hnjtamis.base.exampublicuser.ExamPublicUserList',
	//requires: ['modules.baseinfo.Dictionary'],
	initComponent : function() {
		//var Dictionary = modules.baseinfo.Dictionary;//类别名
		//this.dic_kslx = Dictionary.getDictionaryList('KSLX');
		var me = this;
		var parentMe = me;
		me.op = me.op || 'input';
		me.form = null;
		me.tip = Ext.create('Ext.tip.ToolTip', {
		    title : '导入信息',
		    closable :true,
		    autoHide : false,
		    autoScroll : true,
		    maxHeight : 300,
		    width : 400,
		    html: ''
		});
		me.pageRecordSize = 500;
		me.Wi = [ 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2, 1 ];    // 加权因子   
		me.ValideCode = [ 1, 0, 10, 9, 8, 7, 6, 5, 4, 3, 2 ];            // 身份证验证位值.10代表X  
		me.addText = '';//控制增加按钮的名称
		me.adminflag = true;//管理员标识
		if(me.enroll == 'self'){
			me.addText = '我要报名';
			me.adminflag = false;
		}else if(me.enroll == 'admin'){
			me.addText = '添加考生';
		}
		// 模块列表对象
		this.columns = [{
					name : 'userId',
					width : 0
				},{
					name : 'organId',
					width : 0
				},{
					name : 'postId',
					width : 0
				},{
					name : 'userDeptId',
					width : 0
				},{
					name : 'isDel',
					width : 0
				},{
					name : 'examPublic.examTitle',
					header : '考试信息名称',
					align:"center",
					width : 1,
					renderer:function(value, metadata, record){ 
					    if(value!=undefined && value!=null && value!=""){
					    	metadata.tdAttr = " data-qtip = '"+value+"'";
					    }
					    metadata.style="white-space: normal !important;";
					    return value; 
					}
				},{
					name : 'examPublic.examStartTime',
					header : '报名开始时间',
					align:"center",
					width : 1
				},{
					name : 'examPublic.examEndTime',
					header : '报名结束时间',
					align:"center",
					width : 1
				},{
					name : 'userName',
					header : '参考人员',
					align:"center",
					width : 1
				},{
					name : 'userOrganName',
					header : '所属机构',
					align:"center",
					width : me.enroll == 'self'?0:2,
					renderer:function(value, metadata, record){ 
					    if(value!=undefined && value!=null && value!=""){
					    	metadata.tdAttr = " data-qtip = '"+value+"'";
					    }
					    metadata.style="white-space: normal !important;";
					    return value; 
					}
				},{
					name : 'state',
					header : '状态',
					align:"center",
					renderer : function(value){
						if(value == 20){
							return '审核通过';
						}else if(value == 10){
							return '<font color=red>未审核</font>';
						}
					},
					width : 0.8
				},{
					name : 'bmType',
					header : '是否自主报名',
					align:"center",
					width : me.enroll == 'self'?0:0.8,
					renderer : function(value){
						if(value == 10){
							return '否';
						}else if(value == 20){
							return '<font color=red>是</font>';
						}
					}
				}/*,{
					name : 'isDel',
					header : '参加报名',
					renderer : function(value){
						if(value == 0){
							return '是';
						}else if(value == 1){
							return '否';
						}
					},
					width : 1
				}*/];
		// 模块查询条件对象
		if(me.enroll == 'self'){
			this.terms = [{
					xtype : 'datefield',
					name : 'startTerm',
					format : 'Y-m-d',
					fieldLabel : '报名起始时间',
					labelWidth : 110,
					width:285
				},{
					xtype : 'datefield',
					name : 'endTerm',
					format : 'Y-m-d',
					fieldLabel : '报名截止时间',
					labelWidth : 110,
					width:285
				},{
					xtype : 'radiogroup',
					fieldLabel : '审核状态',
					labelWidth : 110,
					items : [
					         {
					        	 boxLabel :'未审核',
					        	 name : 'stateTerm',
					        	 inputValue : '10'
					         },
					         {
					        	 boxLabel :'审核通过',
					        	 name : 'stateTerm',
					        	 inputValue : '20'
					         }
					         ],
					 width:285
				}];
		}else{
			this.terms = [{
					xtype : 'select',
					name : 'publicIdTerm',
					fieldLabel : '考试信息名称',
					selectUrl:me.op != 'audit'?
						'exam/exampaper/queryExamPublicListForExampaperListAction!queryExamPublicList.action?op=ctandjoin'
						: 'base/exampublic/comboExamPublishForExamPublicListAction!comboExamPublish.action',
					valueField: 'publicId',
					displayField:'examTitle',
					jsonParemeterName:me.op != 'audit'?'examPublicList' :'comboExamPublish',
					width : 380,
					labelWidth : 120,
					allowBlank : me.enroll == 'self' ,//  false,
					defaultValue:me.enroll == 'self'?'':undefined,
					selectEventFun : function(combo,record,index){
						me.publicIdTermValue = (combo.value)
						me.termQueryFun(true,'first');
					}
				},{
					xtype : 'datefield',
					name : 'startTerm',
					format : 'Y-m-d',
					fieldLabel : '报名起始时间',
					labelWidth : 110,
					width:285
				},{
					xtype : 'datefield',
					name : 'endTerm',
					format : 'Y-m-d',
					fieldLabel : '报名截止时间',
					labelWidth : 110,
					width:285
				},{
					xtype : 'radiogroup',
					fieldLabel : '是否到期',
					labelWidth : 110,
					items : [
							 {
								boxLabel : '所有',
								name : 'passDeadLineTerm',
								inputValue:'2',
								checked: true
							 },
					         {
					        	 boxLabel : '否',
					        	 name : 'passDeadLineTerm',
					        	 inputValue:'0'
					         },{
					        	 boxLabel : '是',
					        	 name : 'passDeadLineTerm',
					        	 inputValue:'1'
					         }
					         ],
							 width:285
				},{
					xtype : 'radiogroup',
					fieldLabel : '审核状态',
					labelWidth : 110,
					items : [
					         {
					        	 boxLabel :'未审核',
					        	 name : 'stateTerm',
					        	 inputValue : '10'
					         },
					         {
					        	 boxLabel :'审核通过',
					        	 name : 'stateTerm',
					        	 inputValue : '20'
					         }
					         ],
					 width:285
				}];
		}
		
		this.keyColumnName = "userId";// 主健属性名
		this.viewOperater = true;
		this.otherOperaters = [];//其它扩展按钮操作
		
		if(me.enroll == 'admin'){
			this.addOperater = false;
			this.deleteOperater = false;
			this.updateOperater = false;
			this.otherOperaters.push(Ext.create("Ext.Button", {
				icon : 'resources/icons/fam/plugin_add.gif',
				text : '批量录入',
				hidden: me.op == 'audit',
				handler : function() {
					var _publicId = parentMe.termForm.getForm().findField('publicIdTerm').getValue();
					if(_publicId == undefined || _publicId == null || _publicId==''){
						Ext.Msg.alert('提示', '请选择一场考试信息！');
						return;
					}
					var queryTerm = {};
					if(me.termForm)
						queryTerm = me.termForm.getValues(false);
					var record = {'data':{}};
					var selected = me.getSelectionModel().selected;
					for (var i = 0; i < selected.getCount(); i++) {
						record = selected.get(i);
					}
					me.openMoreFormWin('', function() {
						me.termQueryFun(false,'flash');
					},false,record.data,queryTerm,'add');
				}
			}));
			
			this.otherOperaters.push(Ext.create("Ext.Button", {
				iconCls : 'add',
				text : me.addText,
				resourceCode:me.addResourceCode,
				hidden: me.op == 'audit',
				handler : function() {
					if(me.enroll != 'self'){
						var _publicId = parentMe.termForm.getForm().findField('publicIdTerm').getValue();
						if(_publicId == undefined || _publicId == null || _publicId==''){
							Ext.Msg.alert('提示', '请选择一场考试信息！');
							return;
						}
					}
					var queryTerm = {};
					if(me.termForm)
						queryTerm = me.termForm.getValues(false);
					var record = {'data':{}};
					var selected = me.getSelectionModel().selected;
					for (var i = 0; i < selected.getCount(); i++) {
						record = selected.get(i);
					}
					me.openFormWin('', function() {
						me.termQueryFun(false,'flash');
					},false,record.data,queryTerm,'add');
				}
			}));
			
			this.otherOperaters.push(Ext.create("Ext.Button", {
							iconCls : 'remove',
							text : eap_operate_delete,
							resourceCode:me.deleteResourceCode,
							hidden: me.op == 'audit',
							handler : function() {
								var selected = me.getSelectionModel().selected;
								if (selected.getCount() == 0) {
									Ext.Msg.alert('提示', '请选择记录！');
									return;
								}
								var confirmFn = function(btn) {
									if (btn == 'yes')
										me.deleteData(me.getSelectIds());
								};
								Ext.MessageBox.confirm('询问', '你真要删除这些数据吗？',
										confirmFn);
							}
						}));
						
			/*this.otherOperaters.push(Ext.create("Ext.Button", {
							iconCls : 'update',
							text : eap_operate_update,
							resourceCode:me.updateResourceCode,
							hidden: me.op == 'audit',
							handler : function() {
								var selected = me.getSelectionModel().selected;
								if (selected.getCount() == 0) {
									Ext.Msg.alert('提示', '请选择记录！');
									return;
								}
								var id = "";
								var record = null;
								for (var i = 0; i < selected.getCount(); i++) {
									record = selected.get(i);
									id = record.get(me.keyColumnName);
								}
								var queryTerm = {};
								if(me.termForm)
									queryTerm = me.termForm.getValues(false);
								me.openFormWin(id, function() {
											me.termQueryFun(false,'flash');//me.pagingToolbar.doRefresh();
										},false,record.data,queryTerm,'update');
							}
						}));*/
		}else{
			this.addOperater = true;
			this.deleteOperater = true;
			this.updateOperater = false;
		}
		
		if(me.enroll == 'admin'){
			var auditBtn = Ext.button.Button({
				text : '审核',
				iconCls : 'update',
				hidden: me.op != 'audit',
				handler : function(){
					var selected = me.getSelectionModel().selected;
					if (selected.getCount() == 0) {
						Ext.Msg.alert('提示', '请选择需要审核的考生记录！');
						return;
					}
					var id = "";
					var record = null;
					for (var i = 0; i < selected.getCount(); i++) {
						record = selected.get(i);
						if(record.get('isDel')==0 && record.get('state')==10){
							id += record.get(me.keyColumnName)+",";
						}
					}
					if(id==''){
						Ext.Msg.alert('提示', '请选择状态为未审核 与 参加考试的考生记录！');
						return;
					}else{
						Ext.Ajax.request({
							url:'base/exampublicuser/auditListForExamPublicUserListAction!auditList.action',
							params:{
								ids : id
							},
							success:function(response){
								var re = Ext.decode(response.responseText);
								if(re['updateResult']=='success'){
									Ext.Msg.alert("信息","审核操作成功");
								}else{
									Ext.Msg.alert("信息","审核操作失败");
								}
								me.termQueryFun(false,'flash');
							},
							failure:function(){
								Ext.Msg.alert("信息","未能与服务器取得通讯");
							}
						});
					}
				}
			});
			this.otherOperaters.push(auditBtn);
			
			/*me.optBtGroup = Ext.create("Ext.Button", {
		    text: "报名信息导入", 
		    icon : 'resources/icons/fam/cog_edit.png',//按钮图标
		    hidden: me.op == 'audit',
		    menu: 
		    { 
		        items: [ 
				{
		            //xtype : 'button',
					icon : 'resources/icons/fam/export.gif',
					text : '报名Excel格式导出',
					handler : function() {
						if(me.selectNode==null){
							Ext.Msg.alert('提示', '请选择一机构！');
							return;
						}
						var id = me.selectNode.organId;
						window.location = 'base/exampublicuser/exportXlsForExamPublicUserListAction!exportXls.action?id='+id;
						//window.location = 'base/exampublicuser/exportXlsForExamPublicUserListAction!exportXls.action';
						window.open("./modules/hnjtamis/base/exampublicuser/downMasterplate.jsp");
					}
				},
				{
		           // xtype : 'button',
					icon : 'resources/icons/fam/import.gif',
					text : '考生报名信息导入',
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
					                width : 600,
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
					                        url:'base/exampublicuser/importXlsForExamPublicUserListAction!importXls.action',
					                        success:function(form, action){
					                        	var resultInfo = action.result['resultInfo'];
					                        	var myContentHtml = "";
					                        	for(var i=0;i<resultInfo.length;i++){
					                        		myContentHtml += "<div class='conTentDiv' >"+resultInfo[i]+"</div>"
					                        	}
					                        	Ext.Msg.alert('提示', '导入成功!',function(){
					                                   importWin.hide();
					                                   delete importWin;
					                                   me.termQueryFun();
					                            }); 
					                        	var tmpPosition = importWin.getPosition();
					                        	importWin.hide();
				                                delete importWin;
				                                me.termQueryFun();
				                                   
					                        	me.tip.update(myContentHtml);
					                        	me.tip.showAt(tmpPosition,true);
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
									title : '导入考生信息',
									autoHeight : true,
									width : 650,
									border : false,
									frame : false,
									modal : true,//模态
									closeAction : 'hide',
									items : [importForm]
								});
						importWin.show();
					}
				}
			] 
		    }
			});
			this.otherOperaters.push(me.optBtGroup );*/
		}
		
		this.listUrl = "base/exampublicuser/listForExamPublicUserListAction!list.action?enroll="+me.enroll+"&op="+me.op;// 列表请求地址
		this.deleteUrl = "base/exampublicuser/deleteForExamPublicUserListAction!delete.action?op="+me.op;// 删除请求地址
		/*
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
			sortConfig.sortlistUrl = me.listUrl+"?"+me.termForm.getValues(true)+"&limit=0";
			sortConfig.saveSortUrl = 'base/exampublic/saveSortForExamPublicUserFormAction!saveSort.action';
			sortConfig.callback = callback;
			var sortPanel = ClassCreate('base.model.SortList', Ext.clone(sortConfig));
			// 表单窗口
			var sortWin = new WindowObject({
						layout : 'fit',
						title : '题型排序',
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
		*/
		var IsExsit=false;
		var IsExsitFlag=true;
		// 打开表单页面方法
		this.openFormWin = function(id, callback, readOnly,data,term,oper) {
			var formConfig = {};
			var readOnly = readOnly || false;
			formConfig.readOnly = readOnly;
			//formConfig.fileUpload = true;
			formConfig.formUrl = "base/exampublicuser/saveForExamPublicUserFormAction!save.action?enroll="+me.enroll+"&oper="+oper+"&op="+me.op;
			formConfig.findUrl = "base/exampublicuser/findForExamPublicUserFormAction!find.action?enroll="+me.enroll+"&oper="+oper+"&op="+me.op;
			formConfig.callback = callback;
			formConfig.clearButtonEnabled = false;
			formConfig.columnSize = 2;
			formConfig.jsonParemeterName = 'form';
			formConfig.items = new Array();
			formConfig.oper = oper;// 复制操作类型变量
			formConfig.items.push({
						xtype : 'hidden',
						name : 'userId'
					});
			//=====================================================================
			formConfig.items.push({
				xtype : 'hidden',
				name : 'bmType'
			});
			
			if(me.enroll == 'self' && oper=='add'){
				formConfig.items.push({
					colspan : 2,
					xtype : 'select',
					name : 'examPublicId',
					fieldLabel : '考试信息名称',
					//selectUrl:'base/exampublic/queryComboBoxSelfForExamPublicListAction!queryComboBoxSelf.action?employeeId='+base.Login.userSession.employeeId+"&id="+id,
					//valueField:'publicId',
					//displayField:'examTitle',
					//jsonParemeterName:'comboExamPublish',
					selectUrl:'exam/exampaper/queryExamPublicListForExampaperListAction!queryExamPublicList.action?op=ctandjoinAndEndTime',
					valueField:'publicId',
					displayField:'examTitle',
					jsonParemeterName:'examPublicList',
					allowBlank : false,
					readOnly : false,//?true:!me.adminflag,
					width : 615,
					labelWidth:130
				});
			}else{
				if(me.enroll == 'admin'){
					parentMe.setPublicName(parentMe.termForm.getForm().findField('publicIdTerm').getValue());
				}
				formConfig.items.push({
					xtype : 'hidden',
					name : 'examPublicId'
				});
				formConfig.items.push({
					colspan : 2,
					xtype : 'textfield',
					name : 'examPublicName',
					fieldLabel : '考试信息名称',
					readOnly : true,
					width : 615,
					labelWidth:130
				});
				/*formConfig.items.push({
					colspan : 2,
					xtype : 'select',
					name : 'examPublicId',
					fieldLabel : '考试信息名称',
					selectUrl:'base/exampublic/comboExamPublishForExamPublicListAction!comboExamPublish.action',
					valueField:'publicId',
					displayField:'examTitle',
					jsonParemeterName:'comboExamPublish',
					allowBlank : false,
					readOnly : true,//readOnly?true:!me.adminflag,
					width : 615
				});*/
			}
			/*formConfig.items.push({
				colspan : 1,
				fieldLabel : '机构',
				name : 'organ',
				xtype : 'selecttree',
				allowBlank : false,
				labelWidth:130,
				addPickerWidth:200,
				readOnly : readOnly?true:!me.adminflag,
				nameKey : 'organId',
				nameLable : 'organName',
				readerRoot : 'organs',
				keyColumnName : 'organId',
				titleColumnName : 'organName',
				childColumnName : 'organs',
				selectUrl : "organization/organ/listForOrganListAction!list.action?topOrganTerm="+base.Login.userSession.currentOrganId,
				selectEventFun:function(combo,record,index){
					var deptField = me.form.getForm().findField('dept');
					deptField.reflash("organization/dept/listForDeptListAction!list.action?organTerm="+(combo.value));
					var employeeField = me.form.getForm().findField('employee');
					employeeField.reflash('organization/employee/listForEmployeeListAction!list.action?organTerm='+combo.value+"&limit=999");
				}
			});
			formConfig.items.push({
				colspan : 1,
				fieldLabel : '部门',
				name : 'dept',
				xtype : 'selecttree',
				readOnly : readOnly?true:!me.adminflag,
				addPickerWidth:200,
				labelWidth:130,
				nameKey : 'deptId',
				nameLable : 'deptName',
				readerRoot : 'depts',
				allowBlank : false,
				keyColumnName : 'deptId',
				titleColumnName : 'deptName',
				childColumnName : 'depts',
				selectUrl : "organization/dept/listForDeptListAction!list.action?organTerm="+(data['organId']?data['organId']:base.Login.userSession.currentOrganId),
				selectEventFun:function(combo,record,index){
					var employeeField = me.form.getForm().findField('employee');
					employeeField.reflash("organization/employee/listForEmployeeListAction!list.action?deptTerm="+(combo.value||'')
					                     +'&organTerm='+me.form.getForm().findField('organ').getValue().organId+"&limit=999");
					var quarterField = me.form.getForm().findField('quarter');
					quarterField.reflash("organization/quarter/listForQuarterListAction!list.action?deptTerm="+(combo.value));
				}
			});
			formConfig.items.push({
				colspan : 1,
				fieldLabel : '岗位',
				name : 'quarter',
				addPickerWidth:200,
				labelWidth:130,
				xtype : 'selecttree',
				readOnly : readOnly?true:!me.adminflag,
				allowBlank : false,
				nameKey : 'quarterId',
				nameLable : 'quarterName',
				readerRoot : 'quarters',
				keyColumnName : 'quarterId',
				titleColumnName : 'quarterName',
				childColumnName : 'quarters',
				selectUrl : "organization/quarter/listForQuarterListAction!list.action?deptTerm="+(data['userDeptId']?data['userDeptId']:base.Login.userSession.currentDeptId)
							+"&organTerm="+(data['organId']?data['organId']:base.Login.userSession.currentOrganId),
				selectEventFun:function(combo,record,index){
					var employeeField = me.form.getForm().findField('employee');
					employeeField.reflash("organization/employee/listForEmployeeListAction!list.action?deptTerm="+me.form.getForm().findField('dept').getValue().deptId
					                     +'&organTerm='+me.form.getForm().findField('organ').getValue().organId
					                     +'&quarterTerm='+(combo.value)+"&limit=999");
					
				}
			});*/
			/*
			formConfig.items.push({
				colspan : 1,
				fieldLabel : '姓名',
				name : 'userName',
				allowBlank : false,
				readOnly : readOnly
			});
			*/
			
			/*formConfig.items.push({
				colspan : 1,
				fieldLabel : '姓名',
				name : 'employee',
				xtype : 'selectobject',
				labelWidth:130,
				readOnly :readOnly?true:!me.adminflag,
				valueField : 'employeeId',
				displayField : 'employeeName',
				readerRoot : 'list',
				allowBlank : false,
				enableSelectOne : false,//缺省不选择员工
				selectUrl : 'organization/employee/listForEmployeeListAction!list.action?organTerm='
				           +(data['organId']?data['organId']:base.Login.userSession.currentOrganId)
				           +'&deptTerm='+(data['userDeptId']?data['userDeptId']:base.Login.userSession.currentDeptId)+"&limit=999",
				selectEventFun:function(combo,record,index){
					me.setSysEmployeeValue();
				}
			});*/
			formConfig.items.push({
					colspan : 2,
					fieldLabel : '姓名',
					name : 'employee',
					xtype : 'selecttree',
					readOnly : readOnly,
					checked : false,
					//addPickerWidth : 350,
					allowBlank : false,
					nameKey : 'employeeId',
					nameLable : 'employeeName',
					keyColumnName : 'id',
					titleColumnName : 'title',
					childColumnName : 'children',
					selectType:'employee',
					selectTypeName:'员工',
					labelWidth:130,
					width : 615,
					//editorType:'str',
					selectUrl : "organization/employee/opxdetreeForEmployeeListAction!opxdetree.action?organTerm="
				           +(base.Login.userSession.currentOrganId),//?organTerm="+base.Login.userSession.currentOrganId,
					selectEventFun:function(combo,record,index){
						me.setSysEmployeeValue();
						/*var nameField = form.getForm().findField('name');
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
						});*/
					}
				});
			if(oper=='view'){
				formConfig.items.push({
					colspan : 1,
					labelWidth:130,
					name : 'userSex',
					fieldLabel : '性别',
					xtype : 'textfield',
					readOnly : true,
					renderer : function(value){
						//console.log(value);
						if(value == 1){
							return '男';
						}else if(value == 2){
							return '女';
						}else{
							return '';
						}
					}
				});
			}else{
				formConfig.items.push({
					colspan : 1,
					labelWidth:130,
					fieldLabel : '性别',
					xtype : 'radiogroup',
					items:[
					       {
					    	   boxLabel : '男',
					    	   name : 'userSex',
					    	   readOnly : true,
					    	   inputValue :'1'
					       },
					       {
					    	   boxLabel : '女',
					    	   name : 'userSex',
					    	   readOnly : true,
					    	   inputValue :'2'
					       }
					       ],
					/*renderer : function(value){
						if(value == 1){
							return '男';
						}else if(value == 2){
							return '女';
						}else{
							return '';
						}
					},*/
					width:240,
					allowBlank : true,
					readOnly : true
				});
			}
			formConfig.items.push({
				colspan : 1,
				labelWidth:130,
				name : 'userBirthday',
				fieldLabel : '出生年月',
				xtype : 'datefield',
				format : 'Y-m-d',
				readOnly : true
			});
			formConfig.items.push({
				colspan : 1,
				labelWidth:130,
				name : 'userNation',
				fieldLabel : '名族',
				xtype : 'textfield',
				readOnly : true
			});
			formConfig.items.push({
				colspan : 1,
				labelWidth:130,
				name : 'userPhone',
				fieldLabel : '联系电话',
				xtype : 'textfield',
				readOnly : true
			});
			formConfig.items.push({
				colspan : 2,
				labelWidth:130,
				name : 'userAddr',
				fieldLabel : '住址',
				xtype : 'textfield',
				readOnly : true,
				width : 615
			});
			
			formConfig.items.push({
				colspan : 1,
				labelWidth:130,
				name : 'inticket',
				fieldLabel : '准考证号',
				xtype : 'hidden',
				readOnly : true
			});
			formConfig.items.push({
				colspan : 1,
				labelWidth:130,
				name : 'examPassword',
				fieldLabel : '考生密码',
				xtype : 'hidden',
				readOnly : true
			});
			formConfig.items.push({
				colspan : 1,
				labelWidth:130,
				name : 'idNumber',
				fieldLabel : '身份证号',
				allowBlank : true,
				xtype : 'textfield',
				/*validator : function(thisText){
					if(!!thisText){ //不为空
						if(!me.IdCardValidate(thisText)){
							return '身份证格式错误';
						}else{
							//return true;
							var examPublicIdTerm = me.form.getForm().findField('examPublicId').getValue();
							var userIdTerm = me.form.getForm().findField('userId').getValue();
							Ext.Ajax.request({
								url : 'base/exampublicuser/isRegisterExamForExamPublicUserListAction!isRegisterExam.action',
								method : 'post',
								params : {
									examPublicIdTerm : examPublicIdTerm,
									idNumberTerm : thisText,
									userIdTerm : userIdTerm
								},
								success:function(response){
									var re = Ext.decode(response.responseText);
									if(re['countResult']=='0'){
										ReturnValue(true);
									}else{
										ReturnValue('已使用此身份证报名该此考试');
									}
								},
								failure:function(){
									Ext.Msg.alert("信息","未能与服务器取得通讯");
								}
							});
						}
					}
					if(IsExsitFlag){
						IsExsitFlag = false;
						IsExsit = true;
					}
					function ReturnValue(ok){
						   IsExsit = ok;
					}
					return IsExsit;
				},*/
				readOnly : true
			});
			formConfig.items.push({
				colspan : 1,
				labelWidth:130,
				name : 'score',
				fieldLabel : '总得分',
				xtype : 'numberfield',
				readOnly : true
			});
			
			formConfig.items.push({
				colspan : 1,
				labelWidth:130,
				name : 'state',
				fieldLabel : '状态',
				xtype : 'hidden',
				value: me.enroll == 'self'?'10':'20'
			});
			formConfig.items.push({
				colspan : 1,
				labelWidth:130,
				name : 'isDel',
				fieldLabel : '参加报名',
				xtype : 'hidden',
				value: '0'
			});
			formConfig.items.push({
				colspan : 2,
				labelWidth:130,
				name : 'userInfo',
				fieldLabel : '考生信息',
				xtype : 'textarea',
				readOnly : readOnly,
				width : 615
			});
			/*formConfig.items.push({
				colspan : 1,
				fieldLabel : '状态',
				xtype : 'radiogroup',
				items:[
				       {
				    	   boxLabel:'未审核',
				    	   name : 'state',
				    	   inputValue : '10',
				    	   readOnly : readOnly?true:!me.adminflag,
				    	   checked :true
				       },
				       {
				    	   boxLabel:'审核通过',
				    	   name : 'state',
				    	   readOnly : readOnly?true:!me.adminflag,
				    	   inputValue : '20'
				       }],
				 width:240,
				 hidden : !me.adminflag,
				 readOnly : readOnly
			});
			formConfig.items.push({
				colspan : !me.adminflag?2:1,
				fieldLabel : '参加报名',
				xtype : 'radiogroup',
				items:[
				       {
				    	   boxLabel:'是',
				    	   name : 'isDel',
				    	   inputValue : '0',
				    	   readOnly : readOnly?true:!me.adminflag,
				    	   checked :true
				       },
				       {
				    	   boxLabel:'否',
				    	   name : 'isDel',
				    	   readOnly : readOnly?true:!me.adminflag,
				    	   inputValue : '1'
				       }],
				 width:240,
				 readOnly : readOnly
			});*/
			formConfig.items.push({
				colspan : 2,
				labelWidth:130,
				name : 'remark',
				fieldLabel : '备注',
				xtype : 'textarea',
				readOnly : readOnly,
				width : 615
			});
			
			if(me.oper=='view'){
				var editTable = Ext.widget('editlist',{
					colspan : 2,
					fieldLabel : '登录信息',
					name : 'pwdList',
					xtype : 'editlist',
					readOnly : true,
					enableMoveButton:false,
					viewConfig:{
						//height:150,
						autoScroll:true,
						enableTextSelection: 'true'  //本文是否允许选中
					},
					columns : [{
								width : 1,
								header : '考试科目',
								name : 'exam',
								renderer:function(value){
									return value.examName;
								}
							   },
					           {
								width : 1,
								header : '准考证号',
								name : 'inticket'
							   },{
									width : 1,
									header : '登录密码',
									name : 'examPassword'
							  }]
				});
			    
			    formConfig.items.push(editTable);
			}
			
			me.form = ClassCreate('modules.hnjtamis.base.exampublicuser.ExamPublicUserForm', formConfig);
			me.form.parentWindow = this;
			// 表单窗口
			var formWin = new WindowObject({
						layout : 'fit',
						title : '报名',
						width : 670,
						border : false,
						frame : false,
						modal : true,// 模态
						closeAction : 'hide',
						items : [me.form]
					});	
			
			me.form.setFormData(id,function(result){
				var bmType = me.form.getForm().findField("bmType");
				if(me.enroll == 'self'){
					if(oper == 'add' || (bmType.getValue()==undefined || bmType.getValue()=='' || bmType.getValue() ==null || bmType.getValue()=='null')){
						me.form.getForm().findField("bmType").setValue("20");
					}
					
					me.form.getForm().findField("employee").store.on('load',function(pa1,pa2){
						if(oper == 'add'){
							me.form.getForm().findField("employee").value = {
								employeeId : base.Login.userSession.employeeId,
								employeeName : base.Login.userSession.employeeName
						    };
							me.setSysEmployeeValue();
						}else if(oper == 'update'){
						
						}
					});
				}else{
					if(oper == 'add' || (bmType.getValue()==undefined || bmType.getValue()=='' || bmType.getValue() ==null || bmType.getValue()=='null')){
						me.form.getForm().findField("bmType").setValue("10");
					}
					me.form.getForm().findField("employee").store.on('load',function(pa1,pa2){
						
					});
				}
				if(me.enroll == 'admin'){
					me.form.getForm().findField("examPublicName").setValue(parentMe.publicName);
					me.form.getForm().findField("examPublicId").setValue(parentMe.publicId);
				}
				if(!(me.enroll == 'self' && oper=='add')){
					var _examPublicId = me.form.getForm().findField("examPublicId").getValue();
					if(_examPublicId==undefined || _examPublicId==null ||  _examPublicId==''){
						me.form.getForm().findField("examPublicId").setValue(parentMe.termForm.getForm().findField('publicIdTerm').getValue());
					}
				}
			});
			formWin.show();
		};
		
		
		//批量添加
		this.openMoreFormWin = function(id, callback, readOnly,data,term,oper) {
			var me = this;
			parentMe.setPublicName(parentMe.termForm.getForm().findField('publicIdTerm').getValue());
			var formConfig = {};
			var readOnly = readOnly || false;
			formConfig.readOnly = readOnly;
			//formConfig.fileUpload = true;
			formConfig.formUrl = "base/exampublicuser/saveForExamPublicUserFormAction!saveMore.action?enroll="+me.enroll+"&oper="+oper;
			formConfig.findUrl =  "base/exampublicuser/findForExamPublicUserFormAction!find.action?enroll="+me.enroll+"&oper="+oper;
			formConfig.callback = callback;
			formConfig.clearButtonEnabled = false;
			formConfig.columnSize = 2;
			formConfig.jsonParemeterName = 'form';
			formConfig.items = new Array();
			formConfig.oper = oper;// 复制操作类型变量
			formConfig.items.push({
						xtype : 'hidden',
						name : 'userId'
					});
			//=====================================================================
			formConfig.items.push({
				xtype : 'hidden',
				name : 'examPublicId'
			});
			formConfig.items.push({
				colspan : 2,
				xtype : 'displayfield',
				name : 'examPublicName',
				fieldLabel : '考试信息名称',
				readOnly : true,
				labelWidth:130,
				width : 615
			});
			
			me.employeeTree = Ext.create('modules.hnjtamis.common.employeeTree.employeeFormPanel',{height:520});
			formConfig.items.push(me.employeeTree);

			me.form = ClassCreate('modules.hnjtamis.base.exampublicuser.ExamPublicUserForm', formConfig);
			me.form.parentWindow = this;
			// 表单窗口
			var formWin = new WindowObject({
						layout : 'fit',
						title : '报名考生信息批量录入',
						width : 1000,
						border : false,
						frame : false,
						modal : true,// 模态
						closeAction : 'hide',
						items : [me.form]
					});	
			formWin.show();
			me.form.setFormData(id,function(result){
				me.form.getForm().findField("examPublicId").setValue(parentMe.termForm.getForm().findField('publicIdTerm').getValue());
				me.form.getForm().findField("examPublicName").setValue(parentMe.publicName);
				me.employeeTree.queryEmpList();
			});
		};
		this.callParent();
	},
	setSysEmployeeValue : function(){
		var me = this;
		var employeeField = me.form.getForm().findField('employee');
					var employeeId = employeeField.getValue().employeeId;
					EapAjax.request( {
						method : 'GET',
						url : 'organization/employee/findForEmployeeFormAction!find.action?id=' + employeeId,
						async : true,
						success : function(response) {
							var result = Ext.decode(response.responseText);
							if(result.form) {
								me.form.getForm().setValues({
									'userSex' : (result.form.sex == 0? 1 : 2),
									'userBirthday' : result.form.birthday,
									'userNation' : result.form.nationality,
									'userPhone' : result.form.officePhone,
									'userAddr' : result.form.address,
									'idNumber' : result.form.identityCard
								
								
								});
							}
						},
						failure : function() {
							Ext.Msg.alert('信息','后台未响应，获取员工岗位信息失败！');
						}
					});
	},
	setPublicName : function(id){
		var me = this;
		EapAjax.request( {
		method : 'GET',
		url : 'base/exampublic/findForExamPublicFormAction!find.action?id=' + id,
		async : true,
		success : function(response) {
			var result = Ext.decode(response.responseText);
			if(result.form) {me.publicName = result.form.examTitle;}
		},
		failure : function() {}
		});
	},
	IdCardValidate : function(idCard){
		var me = this;
		idCard = me.trim(idCard.replace(/ /g, ""));               //去掉字符串头尾空格                     
	    if (idCard.length == 15) {   
	        return me.isValidityBrithBy15IdCard(idCard);       //进行15位身份证的验证    
	    } else if (idCard.length == 18) {   
	        var a_idCard = idCard.split("");                // 得到身份证数组   
	        if(me.isValidityBrithBy18IdCard(idCard)&&me.isTrueValidateCodeBy18IdCard(a_idCard)){   //进行18位身份证的基本验证和第18位的验证
	            return true;   
	        }else {   
	            return false;   
	        }   
	    } else {   
	        return false;   
	    }   
	},
	/**  
	 * 判断身份证号码为18位时最后的验证位是否正确  
	 * @param a_idCard 身份证号码数组  
	 * @return  
	 */  
	isTrueValidateCodeBy18IdCard : function(a_idCard){
		var me = this;
		var sum = 0;                             // 声明加权求和变量   
	    if (a_idCard[17].toLowerCase() == 'x') {   
	        a_idCard[17] = 10;                    // 将最后位为x的验证码替换为10方便后续操作   
	    }   
	    for ( var i = 0; i < 17; i++) {   
	        sum += me.Wi[i] * a_idCard[i];            // 加权求和   
	    }   
	    var valCodePosition = sum % 11;                // 得到验证码所位置   
	    if (a_idCard[17] == me.ValideCode[valCodePosition]) {   
	        return true;   
	    } else {   
	        return false;   
	    }   
	},
	/**  
	  * 验证18位数身份证号码中的生日是否是有效生日  
	  * @param idCard 18位书身份证字符串  
	  * @return  
	  */ 
	isValidityBrithBy18IdCard : function(idCard18){
		var me = this;
		var year =  idCard18.substring(6,10);   
	    var month = idCard18.substring(10,12);   
	    var day = idCard18.substring(12,14);   
	    var temp_date = new Date(year,parseFloat(month)-1,parseFloat(day));   
	    // 这里用getFullYear()获取年份，避免千年虫问题   
	    if(temp_date.getFullYear()!=parseFloat(year)   
	          ||temp_date.getMonth()!=parseFloat(month)-1   
	          ||temp_date.getDate()!=parseFloat(day)){   
	            return false;   
	    }else{   
	        return true;   
	    }   
	},
	/**  
	   * 验证15位数身份证号码中的生日是否是有效生日  
	   * @param idCard15 15位书身份证字符串  
	   * @return  
	   */  
	isValidityBrithBy15IdCard : function(idCard15){
		var me = this;
		var year =  idCard15.substring(6,8);   
	      var month = idCard15.substring(8,10);   
	      var day = idCard15.substring(10,12);   
	      var temp_date = new Date(year,parseFloat(month)-1,parseFloat(day));   
	      // 对于老身份证中的你年龄则不需考虑千年虫问题而使用getYear()方法   
	      if(temp_date.getYear()!=parseFloat(year)   
	              ||temp_date.getMonth()!=parseFloat(month)-1   
	              ||temp_date.getDate()!=parseFloat(day)){   
	                return false;   
	        }else{   
	            return true;   
	      }   
	},
	trim:function(str){
		return str.replace(/(^\s*)|(\s*$)/g, "");
	}
});