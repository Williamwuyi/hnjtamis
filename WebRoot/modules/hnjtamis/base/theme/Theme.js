/**
 * 试题
 */
ClassDefine('modules.hnjtamis.base.theme.Theme', {
	extend : 'base.model.List',
	initComponent : function() {
		//Ext.QuickTips.init();
		String.prototype.trim = function()
		{
			return this.replace( /(^\s*)|(\s*$)/g, '' ) ;
		};
		
		var me = this;
		me.op = me.op || 'admin';//'input';
		me.sf = me.sf || 'all';//'pro';
		me.bankType = me.bankType || '10';
		me.themeBankId = me.themeBankId || '',
		this.columns = [
		    {name:'themeId',width:0},
		    {name:'state',width:0},
		    {name:'checkRemark',width:0},
		    {name:'themeInBankIds',width:0},
		    {name:'imagesPath',width:0},
		    {name:'lastFkAuditName',width:0},
		    {header:'题库',name:'themeInBankNames',width:4,sortable:false,menuDisabled:true,
			    renderer:function(value, metadata, record){ 
				    if(value!=undefined && value!=null && value!=""){
				    	metadata.tdAttr = " data-qtip = '"+value+"'";
				    }
				    metadata.style="white-space: normal !important;";
				    return value; 
				},titleAlign:"center"
		    },
		    {header:'题库编码',name:'themeInBankCodes',width:3,sortable:false,menuDisabled:true,
			    renderer:function(value, metadata, record){ 
				    if(value!=undefined && value!=null && value!=""){
				    	metadata.tdAttr = " data-qtip = '"+value+"'";
				    }
				    metadata.style="white-space: normal !important;";
				    return value; 
				},titleAlign:"center"
		    },
			{header:'试题内容',name:'themeName',width:10,sortable:false,menuDisabled:true,
				renderer:function(value, metadata, record){
					var imagesPath = record.get("imagesPath");
					if(imagesPath!=undefined && imagesPath!=null && imagesPath!=""){
						if(value!=undefined && value!=null && value!=""){
					    	metadata.tdAttr = " data-qtip = '"+value+"'";
					    }
						return "<a href='javascript:this.showTheme(\""+record.get("themeId")+"\")'>本题包含图片,点击此处查看</a>";
					}else{
						if(value!=undefined && value!=null && value!=""){
					    	metadata.tdAttr = " data-qtip = '"+value+"'";
					    }
					    metadata.style="white-space: normal !important;";
						return "<a href='javascript:this.showTheme(\""+record.get("themeId")+"\")'>"+value+"</a>";
					}
				},titleAlign:"center"
			},
			{header:'题型',name:'themeTypeName',width:2,sortable:false,menuDisabled:true,align:"center"},
			{header:'难度',name:'degree',width:2,sortable:false,menuDisabled:true,
				renderer:function(value){//难度 5：容易,10：一般15：难,20：很难
					switch(value){
						case 5 :  return "容易";
						case 10 : return "一般";
						case 15 : return "难";
						case 20 : return "很难";
						default : return "";
					}
				},align:"center"
			},
			{header:'出题人',name:'writeUser',width:2,sortable:false,menuDisabled:true,align:"center"},
			{header:'状态',name:'state',width:3,sortable:false,menuDisabled:true,
				renderer:function(value , metadata, record ){//状态 5:保存10:上报15:发布20:打回
					var checkRemark = record.get("checkRemark");
					if(value == -1 &&  (checkRemark!=undefined && checkRemark!=null && checkRemark!="")){
						metadata.tdAttr = " data-qtip = '导入校验存在问题："+checkRemark+"'";
					}
					switch(value){
						case 5 :  return "<font color='red'>保存</font>";
						case 10 : return "<font color='red'>上报</font>";
						case 15 : return "<font color='blue'>发布</font>";
						case 20 : return "<font color='red'>打回</font>";
						case 1 : return "<font color='green'>导入成功</font>";
						case -1 : return "<font color='#FFA500'>导入校验失败</font>";
						default : return "";
					}
				},align:"center"
			},
			{header:'反馈情况',name:'lastFkState',width:3,sortable:false,menuDisabled:true,
				renderer:function(value , metadata, record ){//状态 5:保存10:上报15:发布20:打回
					var lastFkAuditName = record.get("lastFkAuditName");
					if(value!=undefined && value!=null && value!=""){
						metadata.tdAttr = " data-qtip = '"+value+"'";
						switch(value){
							case '-50' : metadata.tdAttr = " data-qtip = '管理员否决反馈修改'";break;
							case '-40' : metadata.tdAttr = " data-qtip = '重新分配了审核人，待审核人进行审核'";break;
							case '40' : metadata.tdAttr = " data-qtip = '审核人已审核，待分公司确认'";break;
							case '30' : metadata.tdAttr = " data-qtip = '已分配了审核人("+lastFkAuditName+")，待审核人审核'";break;
							case '20' : metadata.tdAttr = " data-qtip = '存在反馈，等待分配审核人。'";break;
							default :metadata.tdAttr = " data-qtip = '暂无反馈'";
						}
					}
					switch(value){
						case '-50' : return "<font color='red'>否决反馈</font>";
						case '-40' : return "<font color='red'>待审核</font>";
						case '40' : return "<font color='red'>待确认</font>";
						case '30' : return "<font color='red'>待审核</font>";
						case '20' : return "<font color='red'>待分配</font>";
						default : return "暂无反馈";
					}
				},align:"center"
			},
			{header:'创建人',name:'createdBy',width:2,sortable:false,menuDisabled:true,align:"center"},
 			{header:'创建时间',name:'creationDate',width:3,sortable:false,menuDisabled:true,type : 'date', format:'Y-m-d',align:"center"}
		]; 
		showTheme =  function(id){
			 me.openFormWin(id, function() {},true,null,{},'view');
		};
		this.showTermSize = 1;//设定查询条件出现的个数
		me.themeBankIdTerm = Ext.widget('selecttree',{
						//checked : true,
						fieldLabel:"所属题库",
						xtype : 'selecttree',
						addPickerWidth : 210,
						nameKey : 'themeBankId',
						nameLable : 'themeBankName',
						name:'themeBankIdTerm',
						readerRoot : 'children',
						keyColumnName : 'id',
						titleColumnName : 'title',
						childColumnName : 'children',
						selectType : 'bank',
						selectTypeName : '题库',
						editorType:'str',
						selectUrl : 
						me.bankType == '20' ? "base/themebank/treeForThemeBankListAction!tree.action?op="+me.op+"&bankType="+me.bankType+"&notHaveIds="
						:'base/themebank/specialityThemeBankTreeForThemeBankListAction!specialityThemeBankTree.action?bankType='+me.bankType,
						labelWidth : 95,
						width:370
					});
		me.themeBankIdTerm.setValue(me.themeBankId);
		this.terms = [{
						xtype : 'textfield',
						name : 'themeNameTerm',
						fieldLabel : '题目名称',
						labelWidth : 95,
						width:240
					},{
						xtype : 'select',
						name : 'themeTypeIdTerm',
						fieldLabel:"题型",
						selectUrl:'exam/base/theme/queryThemeTypeAndNullListForThemeListAction!queryThemeTypeAndNullList.action',
						valueField:'themeTypeId',
						displayField:'themeTypeName',
						jsonParemeterName:'themeTypeList',
						labelWidth : 95,
						width:370
					},{
						
						fieldLabel:"专业",
						xtype : 'selecttree',
						name : 'specialityIdTerm',
						nameKey : 'specialityId',
						nameLable : 'specialityName',
						readerRoot : 'children',
						keyColumnName : 'id',
						titleColumnName : 'title',
						childColumnName : 'children',
						selectType:'speciality',
						selectTypeName:'所属专业',
						selectUrl : "baseinfo/speciality/treeForSpecialityListAction!tree.action?jobidTerm=",
						editorType:'str',
						labelWidth : 95,
						width:370
					},{
						fieldLabel:"出题人",
						name:'writeUserTerm',
						xtype:'textfield',
						maxLength:32,
						labelWidth : 95,
						width:370
					},{
						fieldLabel:"难度",
						name:'degreeTerm',
						xtype : 'select',
						data:[[null,'全部'],[5,'容易'],[10,'一般'],[15,'难'],[20,'很难']],
						labelWidth : 95,
						width:370
					},{
						fieldLabel:"所属知识点",
						name:'knowledgePointTerm',
						xtype:'textfield',
						labelWidth : 95,
						width:370
					},
					me.themeBankIdTerm,
					{
						fieldLabel:"状态",
						name:'stateTerm',
						xtype : 'select',
						data:[[null,'全部'],[5,'保存'],[10,'上报'],[15,'发布'],[20,'打回'],[1,'导入成功'],[-1,'导入校验失败']],
						labelWidth : 95,
						width:370
					},{
						fieldLabel:"查询方式",
						name:'queryTypeTerm',
						xtype : 'select',
						data:[[null,'全部'],['SAMETHEME','存在相同试题'],['PROBTHEME','仅问题试题']],
						labelWidth : 95,
						width:370
					}
			];
		this.keyColumnName = "themeId";// 主健属性名
		this.jsonParemeterName = "themelist";
		//this.pageRecordSize = 10;
		this.viewOperater = me.op == 'fk'?false:true;
		this.addOperater = me.op == 'admin' || me.op == 'input' ? true : false;
		this.updateOperater = false;
		this.deleteOperater = me.op == 'admin' || me.op == 'input' ? true : false;
		this.enableExportXls = false;
		this.otherOperaters = [];//其它扩展按钮操作
		this.listUrl = "exam/base/theme/listForThemeListAction!list.action?op="+me.op+"&sf="+me.sf+"&bankType="+me.bankType;// 列表请求地址
		this.deleteUrl = "exam/base/theme/deleteForThemeListAction!delete.action?op="+me.op+"&sf="+me.sf+"&bankType="+me.bankType;// 删除请求地址

		if(me.op == 'admin' || me.op == 'input'){
			me.updateBt = Ext.create("Ext.Button", {
								iconCls : 'update',
								text : eap_operate_update,
								resourceCode:me.updateResourceCode,
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
									var state = record.get("state");
									//状态 5:保存10:上报15:发布20:打回
									if(state === 1 ||state === -1 ||state === 5 || state === 20 || state === 15){
										var queryTerm = {};
										if(me.termForm)
											queryTerm = me.termForm.getValues(false);
										me.openFormWin(id, function() {
													me.termQueryFun(false,'flash');//me.pagingToolbar.doRefresh();
												},false,record.data,queryTerm,'update');
									}else{
										Ext.Msg.alert('提示', '非保存或打回状态，不能进行修改！');
									}
									
								}
							});
			this.otherOperaters.push(me.updateBt);
			
			me.batchUpdateBank = Ext.create("Ext.Button", {
								icon : 'resources/icons/fam/connect.png',//按钮图标
							    text : '批量修改题库',
								handler : function() {
									var selected = me.getSelectionModel().selected;
									if (selected.getCount() == 0) {
										Ext.Msg.alert('提示', '请选择记录！');
										return;
									}
									var id = "";
									var num = 0;
									var record = null;
									//var themeInBankIdsStr = "";
									//var themeInBankNamesStr = "";
									for (var i = 0; i < selected.getCount(); i++) {
										record = selected.get(i);
										id += record.get(me.keyColumnName) +",";
										num++;
										//if(selected.getCount() == 1){
											//themeInBankIdsStr = record.get("themeInBankIds");
											//themeInBankNamesStr = record.get("themeInBankNames");
										//}
									}
									me.batchUpdateBankForm = new Ext.FormPanel({
								        frame:false,
								        fileUpload : true,//指定带文件上传
								        bodyStyle:'padding:10px 5px 10px 5px', 
								        defaultType:'textfield',
								        defaults: {
								           labelWidth:90,
								           labelAlign :'right'
								        },
								        monitorValid:true,
								        items:[/*{
											checked : true,
											fieldLabel:"题库",
											xtype : 'selecttree',
											nameKey : 'themeBankId',
											nameLable : 'themeBankName',
											name:'batchUpdateThemeBank',
											readerRoot : 'themeBanks',
											keyColumnName : 'themeBankId',
											titleColumnName : 'themeBankName',
											childColumnName : 'themeBanks',
											selectUrl : 'base/themebank/listForThemeBankListAction!list.action',
											allowBlank:false,
											editorType:'str',
											labelWidth : 53,
											width : 560
										}*/{
											checked : true,
											fieldLabel:"题库",
											xtype : 'selecttree',
											//addPickerWidth : 210,
											nameKey : 'themeBankId',
											nameLable : 'themeBankName',
											name:'batchUpdateThemeBank',
											readerRoot : 'children',
											keyColumnName : 'id',
											titleColumnName : 'title',
											childColumnName : 'children',
											selectType : 'bank',
											selectTypeName : '题库',
											selectUrl : 'base/themebank/specialityThemeBankTreeForThemeBankListAction!specialityThemeBankTree.action?bankType='+me.bankType,
											editorType:'str',
											labelWidth : 53,
											width : 560
										 },{
											colspan: 1,
											xtype:'label',
											style : 'padding-left:23px',
											text: '注：您已选中了“'+num+'”个试题进行修改,系统会根据您选择的最新题库覆盖原所属题库信息。'
										 },{
											colspan: 1,
											xtype:'hidden',
											name:'batchUpdateThemeIds',
											value: id
										 }
										],
								        buttons:['->',{ 
								                text:'确定',
								                formBind: true, 
								                handler:function(){ 
								                	var confirmtitle = '你确定要修改已选中的<font color=red>'+num+'</font>个试题的题库信息吗？系统会根据您选择的题库覆盖原试题所属题库。';
								                	Ext.MessageBox.confirm('询问', confirmtitle , function(btn){
								                		if (btn == 'yes'){
									                    me.batchUpdateBankForm.getForm().submit({ 
									                        method:'POST', 
									                        waitTitle:'提示', 
									                        waitMsg:'正在处理,请稍候...',
									                        timeout: 10000,
									                        url:'exam/base/theme/batchUpdateBankForThemeListAction!batchUpdateBank.action',
									                        success:function(form, action){
									                        	var msg = action.result.msg; 
									                        	if (!msg) msg = '操作成功！';
									                        	Ext.Msg.alert('信息', msg,function(){
									                                 me.batchUpdateBankWin.hide();
									                                 delete me.batchUpdateBankWin;
									                                 me.termQueryFun();
									                            }); 
									                        	me.batchUpdateBankWin.hide();
									                        	delete me.batchUpdateBankWin;
								                                me.termQueryFun();
									                        },
									                        failure:function(form, action){
									                           Ext.Msg.alert('警告', '网络出现问题！');                        
									                        } 
									                    });
								                		}
								                	});
								                } 
								            }]
								    });
									me.batchUpdateBankWin = new WindowObject({
												layout : 'form',
												title : '批量修改题库',
												autoHeight : true,
												width : 600,
												border : false,
												frame : false,
												modal : true,//模态
												closeAction : 'hide',
												items : [me.batchUpdateBankForm]
											});
									me.batchUpdateBankWin.show();
									/*var state = record.get("state");
									//状态 5:保存10:上报15:发布20:打回
									if(state === 1 ||state === -1 ||state === 5 || state === 20 || state === 15){
										var queryTerm = {};
										if(me.termForm)
											queryTerm = me.termForm.getValues(false);
										me.openFormWin(id, function() {
													me.termQueryFun(false,'flash');//me.pagingToolbar.doRefresh();
												},false,record.data,queryTerm,'update');
									}else{
										Ext.Msg.alert('提示', '非保存或打回状态，不能进行修改！');
									}*/
									
								}
							});
			this.otherOperaters.push(me.batchUpdateBank);
			
			me.chkThemeBt = Ext.create("Ext.Button", {
					icon : 'resources/icons/fam/application_go.png',//按钮图标
					text : "试题检测",//按钮标题
					handler : function() {//按钮事件
						if(me.chkThemeBt.getText() == "试题检测"){
							Ext.MessageBox.confirm('询问', "该操作会持续一个小时（由当前试题库中的试题量决定），并有可能影响服务器性能，请确定该时段使用人较少，系统将自动启动试题检测程序？" , function(btn){
							 	if (btn == 'yes'){
									 Ext.Ajax.request({
											method : 'POST',
											url : 'exam/base/theme/checkAllThemeForThemeListAction!checkAllTheme.action'
									});
									me.chkThemeBt.setText('试题检测(0%)');
									me.themeCheckStateRunner.start(me.themeCheckStateTask);
									Ext.Msg.alert('提示', '试题检测程序启动成功，请在一个小时后再进行查看！');
							 	}
							});
						}else{
							Ext.Msg.alert('提示', '试题检测正在处理！');
						}
						
					}
				});
			this.otherOperaters.push(me.chkThemeBt);
		}
		
		if(me.op == 'fk'){
			me.empXxbLabel = Ext.create("Ext.form.Label",{
				text: '学习币：_个',
				style : 'padding-right:15px'
			}),
			this.otherOperaters.push(me.empXxbLabel);
			
			
			this.otherOperaters.push(Ext.create("Ext.Button", {
							iconCls : 'view',
							text : eap_operate_view,
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
								me.openFormWin(id, function() {},true,record.data,queryTerm,'view');
							}
						}));
			
			me.fkBt = Ext.create("Ext.Button", {
								colspan : 1,
								icon : 'resources/icons/fam/user_comment.png',//按钮图标
								text : '试题问题反馈',
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
									var state = record.get("state");
									var lastFkState= record.get("lastFkState");
									//状态 5:保存10:上报15:发布20:打回
									if(lastFkState=='20' || lastFkState=='30' || lastFkState=='40' || lastFkState=='-40'){
										Ext.Msg.alert('提示', '该试题已提交了反馈！');
									}else{
										
										var queryTerm = {};
										if(me.termForm)
											queryTerm = me.termForm.getValues(false);
										me.openFormWin(id, function() {
													me.termQueryFun(false,'flash');//me.pagingToolbar.doRefresh();
												},true,record.data,queryTerm,'fk');
									}
								}
							});
			this.otherOperaters.push(me.fkBt);
		}else if(me.op == 'fp'){
			me.fkauditBt = Ext.create("Ext.Button", {
								colspan : 1,
								icon : 'resources/icons/fam/user_comment.png',//按钮图标
								text : '直接修改',
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
									var state = record.get("state");
									//状态 5:保存10:上报15:发布20:打回
									//if(state === 10 || state === 1){
										var queryTerm = {};
										if(me.termForm)
											queryTerm = me.termForm.getValues(false);
										me.openFormWin(id, function() {me.termQueryFun(false,'flash');},false,record.data,queryTerm,'fpaudit');
								}
							});
			this.otherOperaters.push(me.fkauditBt);
			
			me.fkBt = Ext.create("Ext.Button", {
								colspan : 1,
								icon : 'resources/icons/fam/user_comment.png',//按钮图标
								text : '分配审核人',
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
									var state = record.get("state");
									//状态 5:保存10:上报15:发布20:打回
									//if(state === 10 || state === 1){
										var queryTerm = {};
										if(me.termForm)
											queryTerm = me.termForm.getValues(false);
										me.openFormWin(id, function() {
													me.termQueryFun(false,'flash');//me.pagingToolbar.doRefresh();
												},true,record.data,queryTerm,'fp');
									//}else{
										//Ext.Msg.alert('提示', '非上报或导入成功状态，不能进行发布！');
									//}
								}
							});
			this.otherOperaters.push(me.fkBt);
		}else if(me.op == 'qr'){
			me.fkBt = Ext.create("Ext.Button", {
								colspan : 1,
								icon : 'resources/icons/fam/user_comment.png',//按钮图标
								text : '试题审核确认',
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
									var state = record.get("state");
									//状态 5:保存10:上报15:发布20:打回
									//if(state === 10 || state === 1){
										var queryTerm = {};
										if(me.termForm)
											queryTerm = me.termForm.getValues(false);
										me.openFormWin(id, function() {
													me.termQueryFun(false,'flash');//me.pagingToolbar.doRefresh();
												},true,record.data,queryTerm,'qr');
									//}else{
										//Ext.Msg.alert('提示', '非上报或导入成功状态，不能进行发布！');
									//}
								}
							});
			this.otherOperaters.push(me.fkBt);
		}else if(me.op == 'audit'){
			me.auditBt = Ext.create("Ext.Button", {
								colspan : 1,
								icon : 'resources/icons/fam/user_comment.png',//按钮图标
								text : '审核',
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
									var state = record.get("state");
									//状态 5:保存10:上报15:发布20:打回
									//if(state === 10 || state === 1){
										var queryTerm = {};
										if(me.termForm)
											queryTerm = me.termForm.getValues(false);
										me.openFormWin(id, function() {me.termQueryFun(false,'flash');},false,record.data,queryTerm,'audit');
								}
							});
			this.otherOperaters.push(me.auditBt);
		}else if(me.op == 'fpaudit'){
			me.auditBt = Ext.create("Ext.Button", {
								colspan : 1,
								icon : 'resources/icons/fam/user_comment.png',//按钮图标
								text : '确认修改',
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
									var state = record.get("state");
									//状态 5:保存10:上报15:发布20:打回
									//if(state === 10 || state === 1){
										var queryTerm = {};
										if(me.termForm)
											queryTerm = me.termForm.getValues(false);
										me.openFormWin(id, function() {me.termQueryFun(false,'flash');},false,record.data,queryTerm,'audit');
								}
							});
			this.otherOperaters.push(me.auditBt);
		}
		
		if(me.op == 'input' || me.op == 'admin'){
			me.extBtGroup = Ext.create("Ext.Button", {
			    text: "试题导入", 
			    icon : 'resources/icons/fam/xls.gif',//按钮图标
			    menu: 
			    { 
			        items: [{
					icon : 'resources/icons/fam/import.gif',
					text : '导入试题',
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
					                emptyText:'请选择EXCEL或ZIP文件，其中ZIP可以包括多个Excel文件与题目有关图片。',
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
					                        url:'exam/base/theme/importXlsForThemeListAction!importXls.action?bankType='+me.bankType,
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
									title : '导入试题  - [导入文件必须是.xls结尾的Excel文件或.zip结尾的Zip文件]',
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
											url : 'exam/base/theme/expPublicForThemeListAction!expPublic.action',
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
				},{
					icon : 'resources/icons/fam/export.gif',//按钮图标
					text : "导出模版",//按钮标题
					handler : function() {//按钮事件
						window.open("./modules/hnjtamis/base/theme/downMasterplate.jsp");
					}
				}
			]}
		 });
		this.otherOperaters.push(me.extBtGroup);
		}
		
		this.otherOperaters.push({
			xtype : 'button',//按钮类型
			icon : 'resources/icons/fam/application_view_list.png',//按钮图标
			text : "试题预览",//按钮标题
			handler : function() {//按钮事件
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
				me.themeOpenView(id);
			}
		
		});

		//打开表单
		this.openFormWin = function(id,callback,readOnly,data,term,oper){
			var formConfig = {};
			var readOnly = readOnly || false;
			formConfig.readOnly = readOnly;
			formConfig.fileUpload = true;
			formConfig.clearButtonEnabled = false;
			formConfig.saveButtonEnabled = false;
			if(oper=='fk' || oper=='fp'){
				formConfig.formUrl = "exam/base/theme/saveFkForThemeFormAction!saveFk.action?op="+me.op+"&sf="+me.sf+"&bankType="+me.bankType;//保存
			}else if(oper=='audit'){
				formConfig.formUrl = "exam/base/theme/saveForThemeFormAction!save.action?isFkAuditXd=true&op="+me.op+"&sf="+me.sf+"&bankType="+me.bankType;//保存
			}else if(oper=='fpaudit'){
				formConfig.formUrl = "exam/base/theme/saveForThemeFormAction!save.action?isFkAuditXd=true&op=fpaudit&sf="+me.sf+"&bankType="+me.bankType;//保存
			}else if(oper=='qr'){
				formConfig.formUrl = "exam/base/theme/saveQrForThemeFormAction!saveQr.action?op="+me.op+"&sf="+me.sf+"&bankType="+me.bankType;//保存
			}else{
				formConfig.formUrl = "exam/base/theme/saveForThemeFormAction!save.action?op="+me.op+"&sf="+me.sf+"&bankType="+me.bankType;//保存
			}
			formConfig.findUrl = "exam/base/theme/findForThemeFormAction!find.action?op="+me.op+"&sf="+me.sf+"&bankType="+me.bankType;
			formConfig.callback = callback;
			formConfig.columnSize = 2;
			formConfig.jsonParemeterName = 'form';
			formConfig.items = new Array();
			formConfig.oper = oper;// 复制操作类型变量
			//formConfig.operViewReadOnly = true;
			formConfig.items.push({
				colspan: 1,
				xtype:'hidden',
				name:'themeId'
			});
			formConfig.items.push({
				colspan: 1,
				xtype:'hidden',
				name:'state'
			});
			formConfig.items.push({
				colspan: 1,
				xtype:'hidden',
				name:'haveImages'
			});
			formConfig.items.push({
				colspan: 1,
				xtype:'hidden',
				name:'imagesNames'
			});
			formConfig.items.push({
				colspan: 1,
				xtype:'hidden',
				name:'themeVersion'
			});
			formConfig.items.push({
				colspan: 1,
				xtype:'hidden',
				name:'themeCode'
			});
			formConfig.items.push({
				colspan: 1,
				xtype:'hidden',
				name:'themeCrc'
			});
			formConfig.items.push({
				colspan: 1,
				xtype:'hidden',
				name:'themeAns'
			});
			formConfig.items.push({
				colspan: 1,
				xtype:'hidden',
				name:'themeHisId'
			});
			formConfig.items.push({
				colspan: 1,
				xtype:'hidden',
				name:'lastFkState'
			});
			formConfig.items.push({
				colspan: 1,
				xtype:'hidden',
				name:'imagesPath'
			});
			formConfig.items.push({
				colspan: 1,
				xtype:'hidden',
				name:'imagesPackageName'
			});
			formConfig.items.push({
				colspan: 1,
				xtype:'hidden',
				name:'imagesSucc'
			});
			me.themeBank = Ext.widget('selecttree',{
				colspan:2,
				checked : true,
				fieldLabel:"所属题库",
				xtype : 'selecttree',
				//addPickerWidth : 210,
				nameKey : 'themeBankId',
				nameLable : 'themeBankName',
				name:'themeBankFormList',
				readerRoot : 'children',
				keyColumnName : 'id',
				titleColumnName : 'title',
				childColumnName : 'children',
				selectType : 'bank',
				selectTypeName : '题库',
				selectUrl : me.bankType == '20' ? "base/themebank/treeForThemeBankListAction!tree.action?op="+me.op+"&bankType="+me.bankType+"&notHaveIds="
						:'base/themebank/specialityThemeBankTreeForThemeBankListAction!specialityThemeBankTree.action?bankType='+me.bankType,
				readOnly : readOnly,
				allowBlank:false,
				labelWidth : 130,
				width : 840
			});
			formConfig.items.push(/*{
				colspan:2,
				checked : true,
				fieldLabel:"所属题库",
				xtype : 'selecttree',
				nameKey : 'themeBankId',
				nameLable : 'themeBankName',
				name:'themeBankFormList',
				readerRoot : 'themeBanks',
				keyColumnName : 'themeBankId',
				titleColumnName : 'themeBankName',
				childColumnName : 'themeBanks',
				selectUrl : 'base/themebank/listForThemeBankListAction!list.action',
				readOnly : readOnly,
				allowBlank:false,
				labelWidth : 130,
				width : 800
			}*/
			me.themeBank);
			formConfig.items.push({
				colspan: oper=='fk' || oper=='fp' || oper=='qr'?2:1,
				xtype : 'select',
				name : 'themeTypeId',
				fieldLabel:"题型",
				selectUrl:'exam/base/theme/queryThemeTypeListForThemeListAction!queryThemeTypeList.action',
				valueField:'themeTypeId',
				displayField:'themeTypeName',
				jsonParemeterName:'themeTypeList',
				labelWidth : 130,
				allowBlank : false,
				readOnly : readOnly,
				width:230,
				listeners:{  
					'change':function(value1,value2){
						Ext.Ajax.request({
							timeout: 10000,
							url : 'base/themetype/findForThemeTypeFormAction!find.action',
							params : {
								id : value2
							},
							success: function(response) {
								    var result = Ext.decode(response.responseText);
								    me.typeInTheme = parseInt(result.form.themeType);
								    me.judgeInTheme = result.form.judge;
								    //根据题型设置答案是否正确的默认值
								    var t = me.ansForm.columns;
									for(var i=0;i<t.length;i++){
										if(t[i].name=='isRight'){
											if(me.typeInTheme == 5 || me.typeInTheme == 10 || me.typeInTheme == 25){
									    		t[i].defaultValue = false;
									   		}else{
									   			t[i].defaultValue = true;
									   		}
										}
									}
									//用于处理默认得分类型（阅卷方式），根据题型中配置来判断
									if(me.oldThemeTypeId!=me.form.getForm().findField("themeTypeId").getValue()){				
										if(me.judgeInTheme=='10'){
											me.form.getForm().findField("scoreType").setValue("0");
										}else{
											me.form.getForm().findField("scoreType").setValue("1");
										}
									}
									
									
									me.oldThemeTypeId = me.form.getForm().findField("themeTypeId").getValue();
								    
							},
							failure : function() {
								Ext.Msg.alert('提示','连接网络失败！');
							}
						});		
					}
				}
			});
			formConfig.items.push({
				colspan:1,
				fieldLabel:"所属题库类型",
				xtype:'radiogroup',
				allowBlank: oper=='fk'|| oper=='fp' || oper=='qr'? true: false,
				width:420,
				labelWidth : 130,
				columns:4,
				readOnly : readOnly,
				items:[{boxLabel:'正式',name:'type',inputValue:10,readOnly : readOnly},
					{boxLabel:'模拟',name:'type',inputValue:20,readOnly : readOnly},
					{boxLabel:'非正式',name:'type',inputValue:30,readOnly : readOnly,width:80},
					{boxLabel:'都可以',name:'type',inputValue:40,readOnly : readOnly}],
				hidden : oper=='fk'|| oper=='fp' || oper=='qr'? true: false
			});
			formConfig.items.push({
				colspan:2,
				fieldLabel:"试题内容",
				name:'themeName',
				xtype:'htmleditor',
				allowBlank:false,
				readOnly : readOnly,
				maxLength : 1000,
				labelWidth : 130,
				width:840,
				height : 100,
				fontFamilies: ["宋体", "隶书", "黑体"],
				enableSourceEdit:oper=='add'||oper=='update'?true:false,
				plugins: [
                   Ext.create('base.core.HtmlEditorImageFile',{imageWidth:'400.0'})
                ]
			});
			/*me.organSelectEditor = Ext.widget('selecttree',{
		    							colspan : 2,
		    							checked : true,
										nameKey : 'sendOrganCode',
										nameLable : 'sendOrganName',
										name:"sendOrgans",
										readerRoot : 'children',
										keyColumnName : 'id',
										allowBlank : false,
										titleColumnName : 'title',
										childColumnName : 'children',
										selectType:'organ',
										selectTypeName:'[机构]',
										fieldLabel:'下发范围',
										labelWidth : 118,
										width:724,
										selectUrl : "base/organEx/organtreeForOrganListExAction!organtree.action"});
			formConfig.items.push(me.organSelectEditor);*/
			/*formConfig.items.push({
			    colspan:2,
				fieldLabel:"岗位",
				name:'themeName',
				xtype:'textfield',
				allowBlank:false,
				readOnly : readOnly,
				maxLength:32,
				width:550
			});*/
			formConfig.items.push({
				colspan:2,
				fieldLabel:"所属知识点",
				name:'knowledgePoint',
				xtype:oper=='fk'?'displayfield':'textfield',
				allowBlank:true,
				readOnly : readOnly,
				maxLength : 100,
				labelWidth : 130,
				width:840,
				hidden : oper=='fk' || oper=='fp' || oper=='qr'? true: false
			});
			
			formConfig.items.push({//0自动阅卷；1人工阅卷；2系统外阅卷
				colspan:1,
				fieldLabel:"得分类型",
				xtype:'radiogroup',
				allowBlank: oper=='fk'|| oper=='fp' || oper=='qr'? true: false,
				width:440,
				labelWidth : 130,
				columns:3,
				readOnly : readOnly,
				items:[{boxLabel:'自动阅卷',name:'scoreType',inputValue:'0',readOnly : readOnly},
					{boxLabel:'人工阅卷',name:'scoreType',inputValue:'1',readOnly : readOnly},
					{boxLabel:'系统外阅卷',name:'scoreType',inputValue:'2',readOnly : readOnly}],
				hidden : oper=='fk' || oper=='fp' ? true: false
			});
			formConfig.items.push({//难度 5：容易,10：一般15：难,20：很难
				colspan:1,
				fieldLabel:"难度",
				xtype:'radiogroup',
				allowBlank: oper=='fk'|| oper=='fp' || oper=='qr'? true: false,
				width:370,
				labelWidth : 130,
				columns:4,
				readOnly : readOnly,
				items:[{boxLabel:'容易',name:'degree',inputValue:'5',readOnly : readOnly},
					{boxLabel:'一般',name:'degree',inputValue:'10',readOnly : readOnly},
					{boxLabel:'难',name:'degree',inputValue:'15',readOnly : readOnly},
					{boxLabel:'很难',name:'degree',inputValue:'20',readOnly : readOnly}],
				hidden : oper=='fk' || oper=='fp' || oper=='qr'? true: false
			});
			formConfig.items.push({//答案显示方式 0:不换行　1:一行一个 2:一行二个 3：一行三个 4:一行四个
				colspan:1,
				fieldLabel:"答案显示方式",
				xtype:'select',
				name : 'eachline',
				allowBlank: oper=='fk'|| oper=='fp' || oper=='qr'? true: false,
				labelWidth : 130,
				readOnly : readOnly,
				data:[[0,'不换行'],[1,'一行一个'],[2,'一行二个'],[3,'一行三个'],[4,'一行四个']],
				defaultValue : 0,
				hidden : oper=='fk' || oper=='fp' ? true: false
			});
			formConfig.items.push({
				colspan:1,
				fieldLabel:"出题人",
				name:'writeUser',
				xtype:oper=='fk' || oper=='fp' ?'displayfield':'textfield',
				allowBlank:true,
				readOnly : readOnly,
				maxLength:20,
				labelWidth : 130,
				hidden : oper=='fk' || oper=='fp' || oper=='qr'? true: false
			});
			
			formConfig.items.push({
				colspan:1,
				fieldLabel:"默认分数",
				name:'defaultScore',
				xtype:oper=='fk' || oper=='fp' ?'displayfield':'numberfield',
				allowBlank: oper=='fk'|| oper=='fp' || oper=='qr'? true: false,
				readOnly : readOnly,
				nanText:'请输入有效正数',//无效数字提示   
				allowDecimals:true, //不允许输入小数
                allowNegative:false, //不允许输入负数
                minValue: 0 ,   //最小值
				maxLength : 8,
				labelWidth : 130,
				hidden : oper=='fk' || oper=='fp' || oper=='qr'? true: false
			});
			formConfig.items.push({
				colspan:1,
				fieldLabel:"是否有效",
				xtype:'radiogroup',
				allowBlank:false,
				width:480,
				labelWidth : 130,
				columns:5,
				readOnly : readOnly,
				items:[{boxLabel:'有效',name:'isUse',inputValue:'5',readOnly : readOnly},
					{boxLabel:'无效',name:'isUse',inputValue:'10',readOnly : readOnly}],
				hidden : oper=='fk' || oper=='fp' || oper=='qr'? true: false
			});
			formConfig.items.push({
				colspan:2,
				fieldLabel:"允许反馈",
				xtype:'radiogroup',
				allowBlank: oper=='fk'|| oper=='fp' || oper=='qr'? true: false,
				width:480,
				labelWidth : 130,
				columns:5,
				readOnly : readOnly,
				items:[{boxLabel:'允许',name:'allowFk',inputValue:'10',readOnly : readOnly},
					{boxLabel:'不允许',name:'allowFk',inputValue:'20',readOnly : readOnly}],
				hidden : oper=='fk' || oper=='fp' || oper=='qr'? true: false
			});
			formConfig.items.push({
				colspan:2,
				fieldLabel:"注解",
				name:'explain',
				xtype:oper=='fk'?'displayfield':'textfield',
				allowBlank:true,
				readOnly : readOnly,
				maxLength : 100,
				labelWidth : 130,
				width: 840,
				hidden : oper=='fk' || oper=='fp' || oper=='qr'? true: false
			});
			/*formConfig.items.push({////状态 5:保存10:上报15:发布20:打回
				colspan:1,
				fieldLabel:"状态",
				xtype:'radiogroup',
				allowBlank:false,
				width:350,
				labelWidth : 130,
				columns:5,
				items:[{boxLabel:'保存',name:'state',inputValue:'5'},
					{boxLabel:'上报',name:'state',inputValue:'10'},
					{boxLabel:'发布',name:'state',inputValue:'15'},
					{boxLabel:'打回',name:'state',inputValue:'20'}]
			});*/
			
			/*formConfig.items.push({
				colspan:2,
				checked : true,
				allowBlank : true,
				fieldLabel:"专业",
				xtype : 'selecttree',
				nameKey : 'specialityId',
				nameLable : 'specialityName',
				name:'themeSpecialityFormList',
				readerRoot : 'children',
				keyColumnName : 'id',
				titleColumnName : 'title',
				childColumnName : 'children',
				selectType:'speciality',
				selectTypeName:'所属专业',
				readOnly : readOnly,
				selectUrl : "baseinfo/speciality/treeForSpecialityListAction!tree.action?jobidTerm=",
				labelWidth : 130,
				width : 800
			});
			
			formConfig.items.push({
				colspan:2,
				checked : true,
				allowBlank : true,
				fieldLabel:"所属岗位",
				xtype : 'selecttree',
				nameKey : 'postId',
				nameLable : 'postName',
				name:'themePostFormList',
				//readerRoot : 'children',
				//keyColumnName : 'id',
				//titleColumnName : 'title',
				keyColumnName : 'id',
				titleColumnName : 'title',
				childColumnName : 'children',
				readOnly : readOnly,
				selectUrl : "organization/quarter/odqtreeForQuarterListAction!odqtree.action",
				labelWidth : 130,
				width : 800
			});*/
			
			formConfig.items.push({
				colspan:2,
				fieldLabel:"备注",
				name:'remark',
				xtype:oper=='fk'?'displayfield':'textfield',
				maxLength : 250,
				labelWidth : 130,
				readOnly : readOnly,
				width : 840,
				hidden : oper=='fk' || oper=='fp' || oper=='qr'? true: false
			});
			
			if(oper=='fp'){
				formConfig.items.push({
			    	colspan : 2,
			    	checked : false,
			    	addPickerWidth:300,
					name : 'lastFkAuditEmp',
					fieldLabel : '指定审核人',
					xtype : 'selecttree',
					nameKey : 'employeeId',
					nameLable : 'employeeName',
					readerRoot : 'children',
					selectType :'employee',//指定可选择的节点
					selectTypeName:'审核人员',
					keyColumnName : 'id',
					titleColumnName : 'title',
					childColumnName : 'children',
					allowBlank:false,
					readOnly : false,
					selectUrl : "base/exammarkpeopleinfo/queryReviewerTreeForExamMarkPeopleInfoListAction!queryReviewerTree.action?organId="+base.Login.userSession.currentOrganId,
					//selectUrl : "organization/employee/opxdetreeForEmployeeListAction!opxdetree.action?organTerm="
				          // +(base.Login.userSession.currentOrganId),
					selectEventFun:function(combo){
					},
					/*validator : function(thisText){
						if(!!thisText){
							var  auditEmps = me.form.getForm().findField("auditEmps").getValue();
							var maxLen = 5;
							if(auditEmps.length>maxLen){
								return "审核(试题)人最多能设置"+maxLen+"人";
							}else{
								return true;
							}
						}else{
							return true;
						}
					},*/
					labelWidth : 130,
					width:400
			    });
				
			}else if(oper=='qr'){
				formConfig.items.push({
			    	checked : false,
			    	addPickerWidth:300,
					name : 'lastFkAuditEmp',
					fieldLabel : '重新分配审核人',
					xtype : 'selecttree',
					nameKey : 'employeeId',
					nameLable : 'employeeName',
					readerRoot : 'children',
					selectType :'employee',//指定可选择的节点
					selectTypeName:'审核人员',
					keyColumnName : 'id',
					titleColumnName : 'title',
					childColumnName : 'children',
					allowBlank:false,
					readOnly : false,
					selectUrl : "base/exammarkpeopleinfo/queryReviewerTreeForExamMarkPeopleInfoListAction!queryReviewerTree.action?organId="+base.Login.userSession.currentOrganId,
					//selectUrl : "organization/employee/opxdetreeForEmployeeListAction!opxdetree.action",
					selectEventFun:function(combo){
					},
					/*validator : function(thisText){
						if(!!thisText){
							var  auditEmps = me.form.getForm().findField("auditEmps").getValue();
							var maxLen = 5;
							if(auditEmps.length>maxLen){
								return "审核(试题)人最多能设置"+maxLen+"人";
							}else{
								return true;
							}
						}else{
							return true;
						}
					},*/
					labelWidth : 130,
					width:400
			    });
			    
			    formConfig.items.push({
					fieldLabel:"反馈审核时间",
					name:'lastFkAuditTime',
					xtype:'displayfield',
					maxLength : 250,
					labelWidth : 130,
					readOnly : readOnly
				});
			}
			formConfig.items.push(me.setAnsForm(readOnly));

			if(oper=='fk'){
				formConfig.items.push({
					 region: 'center',
			         xtype: "panel",
			         bodyStyle : "border:0;padding:5px 5px 5px 5px;text-align:center;",
			         height : 80,
			         colspan:2,
					 items : [{
							colspan:2,
							fieldLabel:"反馈说明",
							name:'fkRemark',
							xtype:'textarea',
							maxLength : 1500,
							labelWidth : 130,
							allowBlank:false,
							width : 600,
							readOnly : false,
							height : 60
					 }]
				});
				
				formConfig.otherViewOperaters = [];
				formConfig.otherViewOperaters.push({
				    xtype : 'button',//按钮类型
					icon : 'resources/icons/fam/user_comment.png',//按钮图标
					text : "提交反馈",//按钮标题
					handler : function() {//按钮事件
						//Ext.Msg.alert('提示', '建设中！');
						//状态 5:保存10:上报15:发布20:打回
						me.form.submit();
					}
				});
			}else if(oper=='fp'){
				formConfig.otherViewOperaters = [];
				formConfig.otherViewOperaters.push({
				    xtype : 'button',//按钮类型
					icon : 'resources/icons/fam/user_comment.png',//按钮图标
					text : "提交指定审核人",//按钮标题
					handler : function() {//按钮事件
						//Ext.Msg.alert('提示', '建设中！');
						//状态 5:保存10:上报15:发布20:打回
						me.form.submit();
					}
				});
				formConfig.otherViewOperaters.push({
				    xtype : 'button',//按钮类型
					icon : 'resources/icons/fam/delete.gif',//按钮图标
					text : "否决修改",//按钮标题
					handler : function() {//按钮事件
						//Ext.Msg.alert('提示', '建设中！');
						//状态 5:保存10:上报15:发布20:打回
						Ext.MessageBox.confirm('询问', "否决修改该试题吗，确认后该试题恢复原有状态，不再进行反馈修改？" , function(btn){
							 if (btn == 'yes'){
							 	me.form.getForm().findField("lastFkAuditEmp").allowBlank = true;
								me.form.getForm().findField("lastFkState").setValue("-50");
								me.form.submit();
							 }
						});
						
					}
				});
			}else if(oper == 'audit' || oper == 'fpaudit'){
				formConfig.items.push({
					 region: 'center',
			         xtype: "panel",
			         bodyStyle : "border:0;padding:5px 5px 5px 5px;text-align:center;",
			         height : 80,
			         colspan:2,
					 items : [{
							colspan:2,
							fieldLabel:"修改说明",
							name:'fkRemark',
							xtype:'textarea',
							maxLength : 1500,
							labelWidth : 130,
							allowBlank:false,
							width : 600,
							readOnly : false,
							height : 60
					 }]
				});
			}else if(oper=='qr'){
				formConfig.items.push({
					 region: 'center',
			         xtype: "panel",
			         bodyStyle : "border:0;padding:5px 5px 5px 5px;text-align:center;",
			         height : 80,
			         colspan:2,
					 items : [{
							colspan:2,
							fieldLabel:"说明",
							name:'fkRemark',
							xtype:'textarea',
							maxLength : 1500,
							labelWidth : 130,
							allowBlank:true,
							width : 600,
							readOnly : false,
							height : 60
					 }]
				});
				
				formConfig.otherViewOperaters = [];
				formConfig.otherViewOperaters.push({
				    xtype : 'button',//按钮类型
					icon : 'resources/icons/fam/user_comment.png',//按钮图标
					text : "确认重新指定审核人",//按钮标题
					handler : function() {//按钮事件
						//Ext.Msg.alert('提示', '建设中！');
						//状态 5:保存10:上报15:发布20:打回
						me.form.getForm().findField("lastFkState").setValue("-40");
						//me.form.getForm().findField('json').setValue(Ext.encode(me.form.getForm().getValues(false)));
						me.form.submit();
					}
				});
				formConfig.otherViewOperaters.push({
				    xtype : 'button',//按钮类型
					icon : 'resources/icons/fam/organ_select.gif',//按钮图标
					text : "否决修改&完成反馈",//按钮标题
					handler : function() {//按钮事件
						//Ext.Msg.alert('提示', '建设中！');
						//状态 5:保存10:上报15:发布20:打回
						Ext.MessageBox.confirm('询问', "否决修改该试题吗，确认后该试题恢复原有状态，不再进行反馈修改？" , function(btn){
							 if (btn == 'yes'){
								me.form.getForm().findField("lastFkAuditEmp").allowBlank = true;
								me.form.getForm().findField("lastFkState").setValue("-50");
								me.form.getForm().url = "exam/base/theme/saveUnQrForThemeFormAction!saveUnQr.action?op="+me.op+"&sf="+me.sf;//保存
								//me.form.getForm().findField('json').setValue(Ext.encode(me.form.getForm().getValues(false)));
								me.form.submit();
						 	}
						});
					}
				});
				formConfig.otherViewOperaters.push({
					xtype : 'button',//按钮类型
					icon : 'resources/icons/fam/user_comment.png',//按钮图标
					text : "完成反馈&发布",//按钮标题
					handler : function() {//按钮事件
						//Ext.Msg.alert('提示', '建设中！');
						//状态 5:保存10:上报15:发布20:打回
						me.form.getForm().findField("lastFkAuditEmp").allowBlank = true;
						me.form.getForm().findField("lastFkState").setValue("50");
						//me.form.getForm().findField('json').setValue(Ext.encode(me.form.getForm().getValues(false)));
						me.form.submit();
					}
				});
			}
			
			 me.themeFkauditFormEList = ClassCreate('base.core.EditList',{
					colspan : 2,
					fieldLabel : null,
					name : 'themeFkauditFormList',
					xtype : 'editlist',
					addOperater : false,
					deleteOperater : false,
					readOnly : true,
					width:oper=='view'?930:890,
					viewConfig:{
						autoScroll:true,
						height:oper=='view'? 470: oper=='fk'?423:551
					},//高度
					columns : [
								{
									name : 'fkauditRemark',
									header : '反馈说明',
									width : 6,
									sortable:false,
									menuDisabled:true,
									titleAlign:"center",
									renderer:function(value, metadata, record){ 
										value = me.replaceString(value,"\n","<br>");
									 	if(value!=undefined && value!=null && value!=""){
								    		metadata.tdAttr = " data-qtip = '"+value+"'";
									    }
									    metadata.style="white-space: normal !important;";
									    return value; 
									}
								},{
									name : 'awardCenter',
									header : '学习币',
									width : 1,
									sortable:false,
									menuDisabled:true,
									align:"center"
								},{
									name : 'createdBy',
									header : '操作人',
									width : 1,
									sortable:false,
									menuDisabled:true,
									align:"center"
								},{
									name : 'creationDate',
									header : '操作时间',
									width : 1.5,
									sortable:false,
									menuDisabled:true,
									align:"center",
									renderer:function(value, metadata, record){ 
										if(value!=undefined && value!=null && value!=""){
								    		metadata.tdAttr = " data-qtip = '"+value+"'";
									    }
									    return value.length>10?value.substring(0,10):value; 
									}
								}/*,{
									name : 'state',
									header : '是否处理',
									width : 1,
									sortable:false,
									menuDisabled:true,
									align:"center",
									renderer:function(value){
										if(value == '10'){
											return "<font color='red'>待处理</font>";
										}else if(value == '20'){
											return "<font color='blue'>已处理</font>";
										}else{
											return "-";
										}
									}
								}*/
					           ]
				});
			
			
			
			
			formConfig.otherOperaters = [];//其它扩展按钮操作
			/*formConfig.otherOperaters.push({
					boxLabel:'保存&自动发布 ',
	                fieldLabel:' ',
	                labelSeparator :'',
	                name:'publicChk',
	                inputValue : true,
	                checked : true,
	                xtype : 'checkbox'
			});*/
			
			formConfig.otherOperaters.push({
			    xtype : 'button',//按钮类型
				icon : 'resources/icons/fam/information.png',//按钮图标
				text : "预览",//按钮标题
				handler : function() {//按钮事件
					me.themeOpenViewInForm(me.form.getForm());	 
				}
			});
			if(!(oper == 'audit' || oper == 'fpaudit')){
				formConfig.otherOperaters.push({
					xtype : 'button',//按钮类型
					icon : 'resources/icons/fam/image_add.png',//按钮图标
					text : "保存&新增",//按钮标题
					handler : function() {//按钮事件
						formConfig.callback = function(){};
						if (me.yz() && me.form.getForm().isValid()&&
						   (!me.form.validate||me.form.validate(me.form.getForm().getValues(false)))){
							me.form.MaskTip.show();
							var publicChkObj = me.form.getForm().findField("publicChk");
							if(publicChkObj && publicChkObj.getValue() == true){
								me.form.getForm().findField("state").setValue("15");
							}else{
								me.form.getForm().findField("state").setValue("5");
							}
							me.form.getForm().findField('json').setValue("");
							me.form.getForm().findField('json').setValue(Ext.encode(me.form.getForm().getValues(false)));
							me.form.getForm().submit({
								success : function(form, action) {
									var msg = action.result.msg;
									if (!msg)
										msg = '操作成功！';
								    me.form.MaskTip.hide();
								    me.termQueryFun(false,'flash');
									Ext.Msg.alert('提示', msg, function(){
										oper = 'add';
										me.form.getForm().findField("themeId").setValue(null);
										me.form.getForm().findField("themeName").setValue(null);
										me.form.getForm().findField("knowledgePoint").setValue(null);
										me.form.getForm().findField("explain").setValue(null);
										me.form.getForm().findField("writeUser").setValue(null);
										me.form.getForm().findField("remark").setValue(null);
										
										
										me.form.getForm().findField("state").setValue("5");
										me.form.getForm().findField("isUse").setValue("5");
										//me.form.getForm().findField("eachline").setValue(0);
										me.form.getForm().findField("type").setValue("40");
										me.form.getForm().findField("scoreType").setValue("0");
										me.form.getForm().findField("degree").setValue("10");		
										me.form.getForm().findField("defaultScore").setValue("1");
					
										
										if(me.typeInTheme == 25){
											for (var i = 0; i < me.ansForm.store.getCount();i++) {
											   var record = me.ansForm.store.getAt(i);
											   //console.log(record);
											   record.data["answerkeyId"]=null;
											   //record.data["answerRemark"]=null;
											}
										}else{
											for (var i = 0; i < me.ansForm.store.getCount();) {
											   var record = me.ansForm.store.getAt(i);
											   me.ansForm.store.remove(record);
											}
										}
									});
								},
								failure : function(form, action) {
									if (action.result && action.result.length > 0)
										Ext.Msg.alert('错误提示',
												action.result[0].errors);
									else
										Ext.Msg.alert('信息', '后台未响应，网络异常！');
								    me.form.MaskTip.hide();
								}
							});
						}
					}	
				});
				formConfig.otherOperaters.push({
					xtype : 'button',//按钮类型
					icon : 'resources/icons/fam/folder_go.png',//按钮图标
					text : "发布&新增",//按钮标题
					handler : function() {//按钮事件
						formConfig.callback = function(){};
						if (me.yz() && me.form.getForm().isValid()&&
						   (!me.form.validate||me.form.validate(me.form.getForm().getValues(false)))){
							me.form.MaskTip.show();
							var publicChkObj = me.form.getForm().findField("publicChk");
							//if(publicChkObj && publicChkObj.getValue() == true){
								me.form.getForm().findField("state").setValue("15");
							//}else{
								//me.form.getForm().findField("state").setValue("5");
							//}
							me.form.getForm().findField('json').setValue("");
							me.form.getForm().findField('json').setValue(Ext.encode(me.form.getForm().getValues(false)));
							me.form.getForm().submit({
								success : function(form, action) {
									var msg = action.result.msg;
									if (!msg)
										msg = '操作成功！';
								    me.form.MaskTip.hide();
								    me.termQueryFun(false,'flash');
									Ext.Msg.alert('提示', msg, function(){
										oper = 'add';
										me.form.getForm().findField("themeId").setValue(null);
										me.form.getForm().findField("themeName").setValue(null);
										me.form.getForm().findField("knowledgePoint").setValue(null);
										me.form.getForm().findField("explain").setValue(null);
										me.form.getForm().findField("writeUser").setValue(null);
										me.form.getForm().findField("remark").setValue(null);
										
										
										me.form.getForm().findField("state").setValue("5");
										me.form.getForm().findField("isUse").setValue("5");
										//me.form.getForm().findField("eachline").setValue(0);
										me.form.getForm().findField("type").setValue("40");
										me.form.getForm().findField("scoreType").setValue("0");
										me.form.getForm().findField("degree").setValue("10");		
										me.form.getForm().findField("defaultScore").setValue("1");
					
										
										if(me.typeInTheme == 25){
											for (var i = 0; i < me.ansForm.store.getCount();i++) {
											   var record = me.ansForm.store.getAt(i);
											   //console.log(record);
											   record.data["answerkeyId"]=null;
											   //record.data["answerRemark"]=null;
											}
										}else{
											for (var i = 0; i < me.ansForm.store.getCount();) {
											   var record = me.ansForm.store.getAt(i);
											   me.ansForm.store.remove(record);
											}
										}
									});
								},
								failure : function(form, action) {
									if (action.result && action.result.length > 0)
										Ext.Msg.alert('错误提示',
												action.result[0].errors);
									else
										Ext.Msg.alert('信息', '后台未响应，网络异常！');
								    me.form.MaskTip.hide();
								}
							});
						}
					}	
				});
				formConfig.otherOperaters.push({
					text : eap_operate_save,
					iconCls : 'save',
					tabIndex:900,
					handler : function() {
						if (me.form.getForm().isValid()&&
						   (!me.form.validate||me.form.validate(me.form.getForm().getValues(false)))){
						   	var publicChkObj = me.form.getForm().findField("publicChk");
							if(publicChkObj && publicChkObj.getValue() == true){
								me.form.getForm().findField("state").setValue("15");
							}else{
								me.form.getForm().findField("state").setValue("5");
							}
							me.form.submit();
						}
					}
				});
				
				formConfig.otherOperaters.push({
					xtype : 'button',//按钮类型
					icon : 'resources/icons/fam/user_comment.png',//按钮图标
					text : "发布",//按钮标题
					handler : function() {//按钮事件
						//Ext.Msg.alert('提示', '建设中！');
						//状态 5:保存10:上报15:发布20:打回
						me.form.getForm().findField("state").setValue("15");
						me.form.submit();
					}
				});
			}else if(oper== 'audit'){
				formConfig.otherOperaters.push({
					xtype : 'button',//按钮类型
					icon : 'resources/icons/fam/user_comment.png',//按钮图标
					text : "确认修改&提交审核",//按钮标题
					handler : function() {//按钮事件
						//Ext.Msg.alert('提示', '建设中！');
						//状态 5:保存10:上报15:发布20:打回
						me.form.submit();
					}
				});
			
			}else if(oper== 'fpaudit'){
				formConfig.otherOperaters.push({
					xtype : 'button',//按钮类型
					icon : 'resources/icons/fam/user_comment.png',//按钮图标
					text : "确认修改",//按钮标题
					handler : function() {//按钮事件
						//Ext.Msg.alert('提示', '建设中！');
						//状态 5:保存10:上报15:发布20:打回
						me.form.submit();
					}
				});
			
			}
			
			/*if(oper == 'audit'){
				
				formConfig.otherOperaters.push({
					xtype : 'button',//按钮类型
					icon : 'resources/icons/fam/folder_go.png',//按钮图标
					text : "打回",//按钮标题
					handler : function() {//按钮事件
						//Ext.Msg.alert('提示', '建设中！');
						//状态 5:保存10:上报15:发布20:打回
						me.form.getForm().findField("state").setValue("20");
						me.form.submit();
					}
				});
			}else{
				formConfig.otherOperaters.push({
					xtype : 'button',//按钮类型
					icon : 'resources/icons/fam/user_comment.png',//按钮图标
					text : "上报",//按钮标题
					handler : function() {//按钮事件
						//Ext.Msg.alert('提示', '建设中！');
						//状态 5:保存10:上报15:发布20:打回
						me.form.getForm().findField("state").setValue("10");
						me.form.submit();
					}
				});
			}*/
			me.form = ClassCreate('base.model.Form',formConfig);
			me.form.parentWindow = this;
			
			me.form.submit = function(){
				var meForm = me.form;
				if (me.yz() && meForm.getForm().isValid()&&
				   (!meForm.validate||meForm.validate(meForm.getForm().getValues(false)))){
					meForm.MaskTip.show();
					meForm.getForm().findField('json').setValue("");
					meForm.getForm().findField('json').setValue(Ext.encode(meForm.getForm().getValues(false)));
					meForm.getForm().submit({
						success : function(form, action) {
							var msg = action.result.msg;
							if (!msg)
								msg = '操作成功！';
						    meForm.MaskTip.hide();
							Ext.Msg.alert('提示', msg,function(){
								meForm.close();
								if(meForm.callback) 
								   meForm.callback(Ext.encode(meForm.getForm().getValues(false)),action.result);
								if(callback) callback(Ext.encode(meForm.getForm().getValues(false)),action.result);
							});
						},
						failure : function(form, action) {
							if (action.result && action.result.length > 0)
								Ext.Msg.alert('错误提示',
										action.result[0].errors);
							else
								Ext.Msg.alert('信息', '后台未响应，网络异常！');
						    meForm.MaskTip.hide();
						}
					});
				}
			}
			
			me.form.close = function() {
				me.themeTabPanel.close();
			}
			
			me.themeTabPanel = Ext.create("Ext.tab.Panel", {
                //title: "Ext.tab.Panel示例",
                frame: true,
                //height: 150,
                //width: 300,
                activeTab:oper == 'audit' || oper == 'fpaudit'?1:0,
                items : [
                {
                	title : '试题基本信息',
                	items : [me.form]
                },{
                	title : '试题反馈信息',
                	items : [me.themeFkauditFormEList]
                }],
                close : function(){
                	var me = this;
					me.ownerCt.close();
					if(me.MaskTip) me.MaskTip.hide();
					delete me.ownerCt;
					delete me;
                }
			});
			//表单窗口
			var formWin = new WindowObject({
				layout:'fit',
				title:'试题管理',
				//height:590,
				width:oper == 'view'?950:913,
				border:false,
				frame:false,
				modal:true,
				//autoScroll:true,
				bodyStyle:'overflow-x:auto;overflow-y:hidden;',
				closeAction:'hide',
				items:[me.themeTabPanel]
			});
			
			
			me.form.setFormData(id,function(result){
				me.oldThemeTypeId = me.form.getForm().findField("themeTypeId").getValue();
				if(oper == 'add'){	
					//增加操作
					//me.initRadiogroup(form,'isUse',5);
					me.form.getForm().findField("state").setValue("5");
					me.form.getForm().findField("isUse").setValue("5");
					me.form.getForm().findField("eachline").setValue(0);
					me.form.getForm().findField("type").setValue("40");
					me.form.getForm().findField("scoreType").setValue("0");
					me.form.getForm().findField("degree").setValue("10");		
					me.form.getForm().findField("defaultScore").setValue("1");
					me.form.getForm().findField("allowFk").setValue("10");
					
					if(me.parentMe){
						me.themeBank.setValue({"themeBankId":me.parentMe.nodeId,"themeBankName":me.parentMe.nodeText})
					}
				}else if(oper == 'update'){
					//更新操作
					me.form.getForm().findField("state").setValue("5");
				}else if(oper == 'audit' || oper == 'fpaudit'){
					//me.form.getForm().findField("state").setValue("5");
				}else if(oper == 'fk'){
					//var themeFkauditFormList = me.form.getForm().findField("themeFkauditFormList");
					//if(themeFkauditFormList.store.getCount()==0){
						//themeFkauditFormList.hide();
					//}
				}else if(oper=='qr'){
					//me.form.getForm().findField("lastFkAuditEmp").setValue(null);
				}
				me.setFkRemarkList(id);
				if(oper == 'update' || oper == 'audit' || oper == 'fpaudit'){
					try{
						var allowFkObj = me.form.getForm().findField("allowFk");
						if(allowFkObj && allowFkObj.getGroupValue()==null){
							allowFkObj.setValue("10");
						}
					}catch(e){}
					setTimeout(function(){
						me.form.getForm().isValid();
					},300);
				}
				
			});
			formWin.show();
		};
		this.callParent();
		if(me.op == 'fk'){
			me.store.on("load",function(){
				me.refreshXxb();
			})
		}
		me.termFormWin.width = 390;
		
		if(me.chkThemeBt){
			me.themeCheckStateTask = {
			    run: function(){
			       me.flushCheckState();
			    },
			    interval: 6000 //30*60*1000
			}
			me.checkDisConnect = 0;
			me.themeCheckStateRunner = new Ext.util.TaskRunner();  
			me.themeCheckStateRunner.start(me.themeCheckStateTask);
		}
		
		
	},
	flushCheckState : function(){//更新消息图片
	   var me = this;
	   Ext.Ajax.request({
				method : 'GET',
				url : 'exam/base/theme/getCheckThemeFinNumForThemeListAction!getCheckThemeFinNum.action',
				params:{},
				success:function(response){
					var re = Ext.decode(response.responseText);
					var checkSchedule = 0;
					if(re && re.checkSumNum == re.checkFinNum){
						checkSchedule = Number(re.checkFinNum/re.checkSumNum).toFixed(0);
						me.chkThemeBt.setText('试题检测');
						me.themeCheckStateRunner.stop(me.themeCheckStateTask);
					}else if(re && re.checkSumNum>0){
						checkSchedule = Number(re.checkFinNum/re.checkSumNum*100).toFixed(0);
						me.chkThemeBt.setText('试题检测('+checkSchedule+"%)");
					}else{
						me.chkThemeBt.setText('试题检测');
						me.themeCheckStateRunner.stop(me.themeCheckStateTask);
					}
					
				},
				failure:function(){
					me.checkDisConnect = me.checkDisConnect+1;
					if(me.checkDisConnect>10){
						me.themeCheckStateRunner.stop(me.themeCheckStateTask);
					}
				}
			});
	},
	setFkRemarkList : function(id){
		var me = this;
		//设置反馈数据	
		EapAjax.request( {
			method : 'GET',
			url : "exam/base/theme/getAuditRemarkListForThemeFormAction!getAuditRemarkList.action?op="+me.op+"&sf="+me.sf+"&id="+id,
			async : true,
			success : function(response) {
				var result = Ext.decode(response.responseText);
				me.themeFkauditFormEList.setValue(result.themeFkauditFormList);
			},
			failure : function() {}
		});
	},
	//数据验证
	yz : function(){
		var me = this;
		var themeTypeId = me.form.getForm().findField("themeTypeId").getValue();
		Ext.Ajax.request({
			timeout: 10000,
			async : false,
			url : 'base/themetype/findForThemeTypeFormAction!find.action',
			params : {
				id : themeTypeId
			},
			success: function(response) {
				var result = Ext.decode(response.responseText);
				me.typeInTheme = parseInt(result.form.themeType);
			},
			failure : function() {
				Ext.Msg.alert('提示','连接网络失败！');
				return false;
			}
	    });		
						
		var themeName = me.form.getForm().findField("themeName").getValue();
		var ansStore = me.ansForm.getStore();
		//类型 5：单选；10：多选 15：填空；20：问答；25：判断；30：视听 35：其他
		if(me.typeInTheme == 5){
			var isRightNum = 0;
			for(var i=0;i<ansStore.getCount();i++){
				 var record = ansStore.getAt(i);
				 var answerkeyValue = record.get("answerkeyValue");
				 if(answerkeyValue==null || String(answerkeyValue).trim()==""){
					Ext.Msg.alert('信息', '答案值不能为空！');
					return false;
				 }
				 var isRight = record.get("isRight");
				 if(isRight  || isRight=='true'){
					 isRightNum++;
				 } 
			}
			if(ansStore.getCount() < 2){
				Ext.Msg.alert('信息', '单选最少需要有两个选择！');
				return false;
			}
			if(isRightNum!=1){
				Ext.Msg.alert('信息', '单选只能有一个正确答案！');
				return false;
			}
		}else if(me.typeInTheme == 10){
			var isRightNum = 0;
			for(var i=0;i<ansStore.getCount();i++){
				 var record = ansStore.getAt(i);
				 var answerkeyValue = record.get("answerkeyValue");
				 if(answerkeyValue==null || String(answerkeyValue).trim()==""){
					Ext.Msg.alert('信息', '答案值不能为空！');
					return false;
				 }
				 var isRight = record.get("isRight");
				 if(isRight || isRight=='true'){
					 isRightNum++;
				 } 
			}
			if(ansStore.getCount() < 2){
				Ext.Msg.alert('信息', '多选最少需要有两个选择！');
				return false;
			}
			if(isRightNum==0){
				Ext.Msg.alert('信息', '多选最少有一个正确答案！');
				return false;
			}
		}else if(me.typeInTheme == 25){
			var isRightNum = 0;
			for(var i=0;i<ansStore.getCount();i++){
				 var record = ansStore.getAt(i);
				 var answerkeyValue = record.get("answerkeyValue");
				 if(answerkeyValue==null || String(answerkeyValue).trim()==""){
					Ext.Msg.alert('信息', '答案值不能为空！');
					return false;
				 }
				 var isRight = record.get("isRight");
				 if(isRight  || isRight=='true'){
					 isRightNum++;
				 } 
			}
			if(ansStore.getCount() != 2){
				Ext.Msg.alert('信息', '判断只能有两个选择！');
				return false;
			}
			if(isRightNum!=1){
				Ext.Msg.alert('信息', '判断必须且只能有一个正确答案！');
				return false;
			}
		}else if(me.typeInTheme == 15){
			var kgNum = ((","+themeName+",").split("()")).length-1;
			kgNum+=((","+themeName+",").split("（）")).length-1;
			var isRightNum = 0;
			for(var i=0;i<ansStore.getCount();i++){
				 var record = ansStore.getAt(i);
				 var answerkeyValue = record.get("answerkeyValue");
				 if(answerkeyValue==null || String(answerkeyValue).trim()==""){
					Ext.Msg.alert('信息', '答案值不能为空！');
					return false;
				 }
				 var isRight = record.get("isRight");
				 if(isRight){
					 isRightNum++;
				 } 
			}
			if(ansStore.getCount() != kgNum){
				Ext.Msg.alert('信息', '设置的答案数量应该与题目中的“()”或“（）”数量匹配！');
				return false;
			}
			if(isRightNum!=ansStore.getCount()){
				Ext.Msg.alert('信息', '设置的答案应全部选择正确！');
				return false;
			}
		}else{
			var isRightNum = 0;
			for(var i=0;i<ansStore.getCount();i++){
				 var record = ansStore.getAt(i);
				 var answerkeyValue = record.get("answerkeyValue");
				 if(answerkeyValue==null || String(answerkeyValue).trim()==""){
					Ext.Msg.alert('信息', '答案值不能为空！');
					return false;
				 }
				 var isRight = record.get("isRight");
				 if(isRight){
					 isRightNum++;
				 } 
			}
			if(ansStore.getCount()>1){
				Ext.Msg.alert('信息', '设置的答案最多为1个！');
				return false;
			}
			if(isRightNum!=ansStore.getCount()){
				Ext.Msg.alert('信息', '设置的答案应全部选择正确！');
				return false;
			}
		}
		return true;
	},
	initRadiogroup : function(form,id,value){
		var field = form.getForm().findField(id).items;
		if(field){
			for(var i = 0 ;i<field.length;i++){
				if(field.get(i).inputValue == value){
					field.get(i).setValue(true);
				}else{
					field.get(i).setValue(false);
				}
			}
		}
	},
	setAnsForm : function(readOnly){
		var me = this;
		me.ansForm = ClassCreate('base.core.EditList',{
				colspan : 2,
				fieldLabel : '考试答案',
				name : 'themeAnswerkeyFormList',
				xtype : 'editlist',
				addOperater : true,
				deleteOperater : true,
				width:readOnly==true?null:890,
				readOnly : readOnly,
				viewConfig:{height:150,autoScroll:true},//高度
				columns : [
							{
								width : 0,
								name : 'answerkeyId'
							},{
								name : 'answerkeyValue',
								header : '答案值',
								width : 1.8,
								sortable:false,
								menuDisabled:true,
								titleAlign:"center",
								renderer:function(value,cellmeta,record,rowIndex,columnIndex,store){
									//自动换行
									cellmeta.style="white-space: normal !important;";
									return value;
								},
								listeners : {
									'click' : function(v1,v2,v3,v4,v5){
										if(!readOnly){
											var formConfigAns = {};
											formConfigAns.items = new Array();
											formConfigAns.clearButtonEnabled = false;
											formConfigAns.saveButtonEnabled = false;
											formConfigAns.items.push({
													//xtype : 'textfield',
													//readOnly : readOnly,
													//allowBlank : true
													//fieldLabel:"试题内容",
													name:'answerkeyValueStr',
													xtype:'htmleditor',
													allowBlank:false,
													readOnly : readOnly,
													//maxLength : 1000,
													//labelWidth : 90,
													width:700,
													height : 300,
													enableColors: true,
				              						enableAlignments: false,
													enableLists : false,
													enableLinks : false,
													enableFontSize:false,
													enableSourceEdit:true,
													enableFormat:false,
													enableColors :false,
													enableFont : false,
													value : v1.getStore().getAt(v3).get('answerkeyValue'),
													plugins: [
									                   Ext.create('base.core.HtmlEditorImageFile',{imageWidth:'400.0'})
									                ]
											});
											
											formConfigAns.otherOperaters = [];//其它扩展按钮操作
											formConfigAns.otherOperaters.push({
								            	icon : 'resources/icons/fam/accept.gif',//按钮图标
												text : "确定录入答案值",//按钮标题
												handler : function() {//按钮事件
													v1.getStore().getAt(v3).set('answerkeyValue',me.formAns.getForm().findField("answerkeyValueStr").getValue());
													me.formAnsWin.hide();
												}
											});
											
											
											me.formAns = ClassCreate('base.model.Form', formConfigAns);
											me.formAns.parentWindow = this;
											// 表单窗口
											me.formAnsWin = new WindowObject({
														layout : 'fit',
														title : '答案值录入',
														width : 715,
														height:362,
														border : false,
														frame : false,
														modal : true,// 模态
														closeAction : 'hide',
														items : [me.formAns]
													});	
											me.formAnsWin.show();
										}
									}
								}
							},/*{
								name : 'answerkeyValue',
								header : '答案值',
								width : 1.8,
								sortable:false,
								menuDisabled:true,
								titleAlign:"center",
								editor : {
									//xtype : 'textfield',
									//readOnly : readOnly,
									//allowBlank : true
									//fieldLabel:"试题内容",
									//name:'answerkeyValue',
									xtype:'htmleditor',
									allowBlank:false,
									readOnly : readOnly,
									//maxLength : 1000,
									//labelWidth : 90,
									width:800,
									height : 130,
									enableColors: true,
              						enableAlignments: false,
									enableLists : false,
									enableLinks : false,
									enableFontSize:false,
									enableSourceEdit:true,
									enableFormat:false,
									enableColors :false,
									enableFont : false,
									plugins: [
					                   Ext.create('base.core.HtmlEditorImageFile',{imageWidth:'400.0'})
					                ]
					                
								},
								renderer:function(value,cellmeta,record,rowIndex,columnIndex,store){
									//自动换行
									cellmeta.style="white-space: normal !important;";
									return value;
								}
							},*/{
								name : 'isRight',
								header : '是否正确',
								align:'center',
								width : 0.3 ,
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
								name : 'answerRemark',
								header : '备注',
								width : 1,
								sortable:false,
								menuDisabled:true,
								titleAlign:"center",
								editor : {
									xtype : 'textfield',
									allowBlank : true,
									maxLength : 100,
									readOnly : readOnly
								}
							}
				           ]
			});
		return me.ansForm;
	},
	themeOpenView : function(id){
			var me = this;
			EapAjax.request({
			 	timeout: 30000,
				url : 'exam/base/theme/findForThemeFormAction!find.action',
				params : {
					id : id
				},
				success : function(response) {
					var result = Ext.decode(response.responseText);
					var form = result.form;
					var themeType = result.form.themeType;
					var themeInType = result.form.themeInType;
					var eachline =result.form.eachline;
					var explain = result.form.explain;
					var imagesNames = result.form.imagesNames;
				    var isImg = false;
				    if(imagesNames!=undefined && imagesNames!=null && imagesNames!=""){
				    	isImg = true;
				    }
					var answerShowSort = ["A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"];
					var htmlStr = "<div style='font-weight: normal; font-size: 15px;'>"+form.themeName+"（"+form.defaultScore+"分）</div>";
					if(explain!=undefined && explain!="" && explain!=null){
					htmlStr+="<div style='font-weight: normal; font-size: 13px;'>注解："+explain+"</div>";	
					}
					 //类型 5：单选；10：多选 15：填空；20：问答；25：判断；30：视听 35：其他
					if(parseInt(themeInType) == 5  || parseInt(themeInType) == 10  || parseInt(themeInType) == 25){
						var anslist = form.themeAnswerkeyFormList;
						htmlStr+="<div style='font-weight: normal; font-size: 15px;'>";
						var ansStr = "";
						for(var j = 0;j<anslist.length;j++){
							if(parseInt(themeInType) == 5  || parseInt(themeInType) == 10  || parseInt(themeInType) == 25){
								htmlStr+=(answerShowSort[j])+"、"+anslist[j].answerkeyValue;
								if(anslist[j].isRight == true){
									ansStr+=answerShowSort[j]+"、";
								}
								if(j == anslist.length-1 || parseInt(eachline) ==1 
									|| (parseInt(eachline)>0  && j>0 && parseInt((j+1) % (parseInt(eachline))) == 0)){
									htmlStr+="<br>"
								}else{
									htmlStr+="&nbsp;&nbsp;&nbsp;&nbsp;"
								}
							}else{
								htmlStr+="<br>"
							}
						}
						htmlStr+="</div><div style='font-weight: normal; font-size: 15px;color: blue;padding-top:10px;'>";
						htmlStr+="正确答案："
						htmlStr+=(ansStr!='' && ansStr.length>0 ? ansStr.substring(0,ansStr.length-1) : '无');
						htmlStr+="</div><br>";
					}else if(parseInt(themeInType) == 15){
						htmlStr = me.replaceString(htmlStr,"\\(\\)","<input type=text width=100  value='' >");
						htmlStr = me.replaceString(htmlStr,"\\（\\）","<input type=text width=100  value='' >");
						
						var ansStr = "";
						var anslist = form.themeAnswerkeyFormList;
						for(var j = 0;j<anslist.length;j++){
							ansStr+=anslist[j].answerkeyValue+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
						}
						htmlStr+="<div style='font-weight: normal; font-size: 15px;color: blue;'>";
						htmlStr+="正确答案："
						htmlStr+=(ansStr!='' && ansStr.length>0 ? ansStr : '无');
						htmlStr+="<br>";
					}else{
						var ansStr = "";
						var anslist = form.themeAnswerkeyFormList;
						for(var j = 0;j<anslist.length;j++){
							ansStr+=anslist[j].answerkeyValue+"\n";
						}
						
						htmlStr+="<div style='font-weight: normal; font-size: 13px;'>"
						//htmlStr+="<textarea  rows='15' cols='65' style='width:100;height:50'>";
						htmlStr+=(ansStr!='' && ansStr.length>0 ? ansStr : '无');
						//htmlStr+="</textarea>";
						htmlStr+="</div><br>";
					}
							
					me.viewTheme = new WindowObject({
						layout:'fit',
						title:'试题预览',
						height:isImg?screen.availHeight*0.8:400, 
						width:isImg?screen.availWidth*0.8:600,
						border:false,
						frame:false,
						modal:true,
						autoScroll:true,
						bodyStyle:'overflow-x:auto;overflow-y:auto;background:#FFFFFF;padding:15px;font-size:13px',
						closeAction:'hide',
						html:htmlStr
					});
					me.viewTheme.show();
				},
				failure : function() {
					Ext.Msg.alert('提示', '网络异常！');
				}
			});
	},
	themeOpenViewInForm : function(form){
		var me = this;
		var eachline = form.findField("eachline").getValue();
		var themeTypeId = form.findField("themeTypeId").getValue();
		var answerShowSort = ["A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"];
		var htmlStr = "";
		Ext.Ajax.request({
			timeout: 10000,
			url : 'base/themetype/findForThemeTypeFormAction!find.action',
			params : {
				id : themeTypeId
			},
			success: function(response) {
				    var result = Ext.decode(response.responseText);
				    var themeInType = parseInt(result.form.themeType);
				    var explain = form.findField("explain").getValue();
				    var themeName = form.findField("themeName").getValue();
				    var imagesNames = form.findField("imagesNames").getValue();
				    var isImg = false;
				    if(imagesNames!=undefined && imagesNames!=null && imagesNames!=""){
				    	isImg = true;
				    }
				    if(themeName!=null && themeName!=""){
						htmlStr+="<div style='font-weight: normal; font-size: 15px;'>"+themeName+"（"+form.findField("defaultScore").getValue()+"分）</div>";	
						if(explain!=undefined && explain!="" && explain!=null){
						htmlStr+="<div style='font-weight: normal; font-size: 13px;'>注解："+explain+"</div>";	
						} 
						//类型 5：单选；10：多选 15：填空；20：问答；25：判断；30：视听 35：其他
						if(parseInt(themeInType) == 5  || parseInt(themeInType) == 10  || parseInt(themeInType) == 25){
							htmlStr+="<div style='font-weight: normal; font-size: 15px;'>"
							var ansStr = "";
							for(var i=0;i<me.ansForm.getStore().getCount();i++){
								var record = me.ansForm.getStore().getAt(i);
								if(themeInType == 5  || themeInType == 10  || themeInType == 25){
									htmlStr+=(answerShowSort[i])+"、"+record.get("answerkeyValue");
									
									if(record.get("isRight")== true){
										ansStr+=answerShowSort[i]+"、";
									}
								
									if(i == me.ansForm.getStore().getCount()-1  || parseInt(eachline) ==1 
										|| (parseInt(eachline)>0  && i>0 && parseInt((i+1) % (parseInt(eachline))) == 0)){
										htmlStr+="<br>"
									}else{
										htmlStr+="&nbsp;&nbsp;&nbsp;&nbsp;"
									}
								}else{
									htmlStr+="<br>"
								}
							}
							htmlStr+="</div><div style='font-weight: normal; font-size: 15px;color: blue;padding-top:10px;'>";
							htmlStr+="正确答案："
							htmlStr+=(ansStr!='' && ansStr.length>0 ? ansStr.substring(0,ansStr.length-1) : '无');
							htmlStr+="</div><br>";
						}else if(parseInt(themeInType) == 15){
							htmlStr = me.replaceString(htmlStr,"\\(\\)","<input type=text width=100>");
							htmlStr = me.replaceString(htmlStr,"\\（\\）","<input type=text width=100>");
							
							var ansStr = "";
							for(var i=0;i<me.ansForm.getStore().getCount();i++){
								var record = me.ansForm.getStore().getAt(i);
								ansStr+=record.get("answerkeyValue")+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
							}
							htmlStr+="<div style='font-weight: normal; font-size: 15px;color: blue;'>";
							htmlStr+="正确答案："
							htmlStr+=(ansStr!='' && ansStr.length>0 ? ansStr : '无');
							htmlStr+="<br>";
						}else{
							var ansStr = "";
							for(var i=0;i<me.ansForm.getStore().getCount();i++){
								var record = me.ansForm.getStore().getAt(i);
								ansStr+=record.get("answerkeyValue")+"\n";
							}
						
							htmlStr+="<div style='font-weight: normal; font-size: 13px;'>"
							//htmlStr+="<textarea  rows='15' cols='65' style='width:100;height:50'>";
							htmlStr+=(ansStr!='' && ansStr.length>0 ? ansStr : '无');
							//htmlStr+="</textarea>";
							htmlStr+="</div><br>";
						}
				    }else{
				    	htmlStr+="<div style='font-weight: normal; font-size: 15px;'>请录入试题</div>";	
				    }
					
					me.viewTheme = new WindowObject({
						layout:'fit',
						title:'试题预览',
						height:isImg?screen.availHeight*0.8:400, 
						width:isImg?screen.availWidth*0.8:600,
						border:false,
						frame:false,
						modal:true,
						autoScroll:true,
						bodyStyle:'overflow-x:auto;overflow-y:auto;background:#FFFFFF;padding:15px;font-size:13px',
						closeAction:'hide',
						html:htmlStr
					});
					me.viewTheme.show();
			},
			failure : function() {
				Ext.Msg.alert('提示','获取试题失败，请与管理员联系！');
				me.initExamThemeFlag = false;
			}
		});			
	},
	replaceString : function(str,reallyDo,replaceWith) { 
		var e=new RegExp(reallyDo,"g"); 
		words = str.replace(e, replaceWith); 
		return words; 
	},
	refreshXxb : function(){
		var me = this;
		me.empXxbLabel.setText("学习币：_个");
		Ext.Ajax.request({
			timeout: 10000,
			url : 'exam/base/theme/queryEmployeeXxbForThemeListAction!queryEmployeeXxb.action',
			params : {
				
			},
			success: function(response) {
				var result = Ext.decode(response.responseText);
				if(result && result.empXxb){
					myshowEmpXxb = function(){
						me.showEmpXxb();
					};
					me.empXxbLabel.el.dom.innerHTML =("<a href='javascript:myshowEmpXxb()'>学习币："+result.empXxb+"个</a>");
					me.empXxbLabel.show(); 
				}
			},
			failure : function() {
				Ext.Msg.alert('提示','刷新学习币，请与管理员联系！');
				me.initExamThemeFlag = false;
			}
		});
	},
	showEmpXxb:function(){
		var me = this;
		var xxbTableStore = new Ext.data.Store({
			fields:[{name:"xxb"},{name:"awardTime"},{name:"fkType"},{name:"themeId"},
				{name:"themeName"},{name:"creationDate"}],
			autoLoad :false,
			proxy:{
				type : 'ajax',
				actionMethods : "POST",
				timeout: 120000,
				url:'exam/base/theme/queryEmpXxbListForThemeListAction!queryEmpXxbList.action',
	            reader : {
					type : 'json',
					root : 'empXxbItemList',
					totalProperty : "empXxbItemListTotal"
				}
			},
			remoteSort : false
		}); 
	    var xxbTableGrid = new Ext.grid.GridPanel({
    	 	store:xxbTableStore,
    	 	autoScroll :true,
			//autoHeight:true,
    	 	//height:490,
    	 	colspan:2,
    	 	autoWidth:true, 
    	 	region: 'center',
			stripeRows:true, //斑马线效果          
    	 	columnLines : true,
    	 	forceFit: true,
    	 	collapsible : false,
    	 	//title: '考生信息',
	    	columns:[
	    		new Ext.grid.RowNumberer({text:"序号",width:1,align:"center"}),
	    		{header:"试题",dataIndex:"themeName",sortable:false, menuDisabled:true,width:6,align:"left",titleAlign:"center",
	    		renderer:function(value, metadata, record){
	    			metadata.style="white-space: normal !important;"; return value;
	    		}},
	    		{header:"操作时间",dataIndex:"creationDate",sortable:false, menuDisabled:true,width:1.5,align:"center",
	    		renderer:function(value, metadata, record){
	    			metadata.style="white-space: normal !important;"; return value;
	    		}},
	    		{header:"操作",dataIndex:"fkType",sortable:false, menuDisabled:true,width:1.5,align:"center",
	    		renderer:function(value, metadata, record){
	    			 if(value == '10'){
	    			 	return '反馈';
	    			 }else if(value == '20'){
	    			 	return '指定审核人';
	    			 }else if(value == '30'){
	    			 	return '反馈审核';
	    			 }else if(value == '40'){
	    			 	return '反馈确认';
	    			 }
	    			return '不确定';
	    		}},
	    		{header:"奖励数目(个)",dataIndex:"xxb",sortable:false, menuDisabled:true,width:1.5,align:"center",
	    		renderer:function(value, metadata, record){
	    			return value;
	    		}}
	    		]
    	});
    	xxbTableStore.load();
    	var xxbShowForm = new WindowObject({
				layout:'fit',
				title:'查看奖励学习币详细',
				height:500,
				width:800,
				border:false,
				frame:false,
				modal:true,
				//autoScroll:true,
				bodyStyle:'overflow-x:auto;overflow-y:hidden;',
				closeAction:'hide',
				items:[xxbTableGrid]
			});
		xxbShowForm.show();
	}
});