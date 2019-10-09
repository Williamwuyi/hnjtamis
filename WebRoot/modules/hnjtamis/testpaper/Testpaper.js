/**
 * 试题
 */
ClassDefine('modules.hnjtamis.testpaper.Testpaper', {
	extend : 'base.model.List',
	initComponent : function() {
		var me = this;
		me.exbt = me.exbt || 'none';
		me.sf = me.sf || 'dc';//dc dept pro
		this.columns = [
		    {name:'testpaperId',width:0},
			{header:'试卷名称',name:'testpaperName',width:10,sortable:false,menuDisabled:true,titleAlign:"center",
				renderer:function(value){
					return "<a href='javascript:this.showTestpaper()'>"+value+"</a>";
				},titleAlign:"center"
			},
			{header:'总题数',name:'totalTheme',width:4,sortable:false,menuDisabled:true,titleAlign:"center",align:"right"},
			{header:'总分数',name:'totalScore',width:4,sortable:false,menuDisabled:true,titleAlign:"center",align:"right"},
			{header:'使用次数',name:'useNum',width:2,sortable:false,menuDisabled:true,titleAlign:"center",align:"right",
				renderer:function(value){
					return value;
				}
			},	
			{header:'审核状态',name:'state',width:2.5,sortable:false,menuDisabled:true,align:"center",
				renderer:function(value){// 5:未上报，10：等待审核，15：审核通过，20：审核打回
					switch(value){
						case '5' :  return "<font color='red'>未上报</font>";
						case '10' : return "<font color='red'>等待审核</font>";
						case '15' : return "<font color='blue'>审核通过</font>";
						case '20' : return "<font color='red'>审核打回</font>";
						default : return "";
					}
				}
			},
			{header:'创建人',name:'createdBy',width:2,sortable:false,menuDisabled:true,align:"center"},
 			{header:'创建时间',name:'creationDate',width:3,sortable:false,menuDisabled:true,type : 'date', format:'Y-m-d',align:"center"}
		]; 
		showTestpaper =  function(){
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
		};
		this.showTermSize = 1;//设定查询条件出现的个数
		this.terms = [{
						xtype : 'textfield',
						name : 'testpaperNameTerm',
						fieldLabel : '试卷名称',
						width:250
					},{
						fieldLabel:"总题数(≥)",
						name:'totalThemeMinTerm',
						xtype:'textfield',
						maxLength:32,
						labelWidth : 105,
						width:260
					},{
						fieldLabel:"总题数(＜)",
						name:'totalThemeMaxTerm',
						xtype:'textfield',
						maxLength:32,
						labelWidth : 105,
						width:260
					},{
						fieldLabel:"总分数(≥)",
						name:'totalScoreMinTerm',
						xtype:'textfield',
						maxLength:32,
						labelWidth : 105,
						width:260
					},{
						fieldLabel:"总分数(＜)",
						name:'totalScoreMaxTerm',
						xtype:'textfield',
						maxLength:32,
						labelWidth : 105,
						width:260
					},{
						fieldLabel:"创建人",
						name:'createdByTerm',
						xtype:'textfield',
						maxLength:32,
						labelWidth : 105,
						width:260
					},{
						fieldLabel:"创建时间(≥)",
						name:'creationDateMaxTerm',
						xtype: 'datefield',
						format : 'Y-m-d',
						maxLength:32,
						labelWidth : 105,
						width:260
					},{
						fieldLabel:"创建时间(＜)",
						name:'creationDateMinTerm',
						xtype: 'datefield',
						format : 'Y-m-d',
						maxLength:32,
						labelWidth : 105,
						width:260
					},{	
						fieldLabel:"审核状态",
						name:'stateTerm',
						xtype : 'select',
						data:[[null,'全部'],[5,'未上报'],[10,'等待审核'],[15,'审核通过'],[20,'审核打回']],
						labelWidth : 105,
						width:260
					}
			];
		this.keyColumnName = "testpaperId";// 主健属性名
		this.jsonParemeterName = "testpaperFormList";
		//this.pageRecordSize = 10;
		this.viewOperater = true;
		this.addOperater = true;
		this.updateOperater = false;
		this.deleteOperater = true;
		this.otherOperaters = [];//其它扩展按钮操作
		this.listUrl = "exam/testpaper/listForTestpaperListAction!list.action?examType="+me.examType+"&sf="+me.sf;// 列表请求地址
		this.deleteUrl = "exam/testpaper/deleteForTestpaperListAction!delete.action?sf="+me.sf;// 删除请求地址
		
		if(me.examType!=undefined && me.examType!=null 
				&& (me.examType==30 || me.examType==40)){
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
								var queryTerm = {};
									if(me.termForm)
									queryTerm = me.termForm.getValues(false);
									me.openFormWin(id, function() {
										me.termQueryFun(false,'flash');//me.pagingToolbar.doRefresh();
									},false,record.data,queryTerm,'update');
								
							}
				});	
				this.otherOperaters.push(me.updateBt);
				
				if(me.examType==40){
					me.creatExamBt = Ext.create("Ext.Button", {
								icon : 'resources/icons/fam/book.png',//按钮图标
								text : '初始化试卷',
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
									Ext.Msg.confirm('提示', '您正在初始化试卷，如果已存在该模版的初始化的试卷以及考生作答，则会被自动清理？',function(bt){
			                        	if(bt=='yes'){
											EapAjax.request({
											 	timeout: 60000,
												url : 'exam/exampaper/createUserExamForExampaperFormAction!createUserExam.action',
												params : {
													testpaperId : id,
													examType : me.examType
												},
												success : function(response) {
													var result = Ext.decode(response.responseText);
													var msg = result.msg;
													if(msg==undefined || msg == null  || msg==''){
														Ext.Msg.alert('提示', '处理成功！');
													}else{
														Ext.Msg.alert('提示', msg);
													}
												},
												failure : function() {
													Ext.Msg.alert('提示', '网络异常！');
												}
											});	
									   }
									});
									
								}
					});	
					this.otherOperaters.push(me.creatExamBt);
					
					
					me.moniExamBt = Ext.create("Ext.Button", {
								icon : 'resources/icons/fam/user_comment.png',//按钮图标
								text : '考试模拟',
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
									var url = './onlineExam/examTestpaperForOnlineListAction!examTestpaper.action?employeeId=sessionUser&relationId='+id+'&relationType=userExam&isHidden=1&isRight=true';
									me.openWin(url)
									/*var htmlStr="<div><iframe src='' scrolling=\"auto\" frameborder=\"0\" height=\"800\" width=\"100%\" style='overflow: hidden;height: 800;'></iframe></div>";
									me.viewTheme = new WindowObject({
										layout:'fit',
										title:'考试模拟',
										height:800,
										width:1000,
										border:false,
										frame:false,
										modal:true,
										autoScroll:true,
										bodyStyle:'overflow-x:hidden;overflow-y:hidden;background:#FFFFFF;padding:0px;',
										closeAction:'hide',
										html:htmlStr
									});
									me.viewTheme.show();*/
									
								}
					});	
					this.otherOperaters.push(me.moniExamBt);
				}
				
		}else{
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
								if(state==null || state=='' || state === '5' || state === '20' || state === '15'){
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
								if(state === '10'){
									var queryTerm = {};
									if(me.termForm)
										queryTerm = me.termForm.getValues(false);
									me.openFormWin(id, function() {
												me.termQueryFun(false,'flash');//me.pagingToolbar.doRefresh();
											},false,record.data,queryTerm,'audit');
								}else{
									Ext.Msg.alert('提示', '非上报状态，不能进行审核！');
								}
							}
						});
		this.otherOperaters.push(me.auditBt);
		
		this.otherOperaters.push({
			xtype : 'button',//按钮类型
			icon : 'resources/icons/fam/export.gif',//按钮图标
			text : "导出试卷",//按钮标题
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
				window.open("./docExp/exportToDocForTestpaperExpDocAction!exportToDoc.action?id="+id+"&random="+Math.random());
			}
		
		});
		
		if(me.exbt == 'true'){
			this.otherOperaters.push({
				xtype : 'button',//按钮类型
				icon : 'resources/icons/fam/export.gif',//按钮图标
				text : "导出试卷与答案",//按钮标题
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
					window.open("./docExp/exportToDocForTestpaperExpDocAction!exportToDoc.action?id="+id+"&showRight=true&random="+Math.random());
				}
			
			});
		}
		
		
		this.otherOperaters.push({
			xtype : 'button',//按钮类型
			icon : 'resources/icons/fam/pencil.png',//按钮图标
			text : "试卷预览",//按钮标题
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
		}
		
		
		this.callParent();
		
	},
	openFormWin : function(id,callback,readOnly,data,term,oper){
		var me = this;
		me.baseForm = me.getBaseForm(id, callback, readOnly,data,term,oper);
		me.themeTableForm = me.getThemeTableForm();
		
		var formOperaters = [];
		formOperaters.push('->');
		if(!(me.examType!=undefined && me.examType!=null 
				&& (me.examType==30 || me.examType==40))){
				formOperaters.push({
						boxLabel:'保存&自动审核 ',
		                fieldLabel:' ',
		                labelSeparator :'',
		                name:'publicChk',
		                inputValue : true,
		                checked : true,
		                xtype : 'checkbox'
				});
			}
			if(oper == 'audit'){
				formOperaters.push({
					xtype : 'button',//按钮类型
					icon : 'resources/icons/fam/user_comment.png',//按钮图标
					text : "审核通过",//按钮标题
					handler : function() {//按钮事件
						//Ext.Msg.alert('提示', '建设中！');
						//状态 5:保存10:上报15:发布20:打回
						me.setAllDefScore();
						me.form.getForm().findField("state").setValue("15");
						me.form.submit();
					}
				});
				formOperaters.push({
					xtype : 'button',//按钮类型
					icon : 'resources/icons/fam/folder_go.png',//按钮图标
					text : "审核打回",//按钮标题
					handler : function() {//按钮事件
						//Ext.Msg.alert('提示', '建设中！');
						//状态 5:保存10:上报15:发布20:打回
						me.setAllDefScore();
						me.form.getForm().findField("state").setValue("20");
						me.form.submit();
					}
				});
			}else{
				formOperaters.push({
					xtype : 'button',//按钮类型
					icon : 'resources/icons/fam/pencil.png',//按钮图标
					text : '手工添加新试题',
					handler : function(){
						var themeBankFormList = me.form.getForm().findField("themeBankFormList").getValue();
						var handThemeBanknums = 0;
						if(themeBankFormList!=undefined && themeBankFormList.length!=undefined && themeBankFormList.length>0){
							handThemeBanknums = themeBankFormList.length;
						}
						if(handThemeBanknums == 0){
							Ext.Msg.alert('提示', '必须选择一个题库！');
						}else if(handThemeBanknums>10){
							Ext.Msg.alert('提示', '手工添加试题的题库不能大于10个！');
						}else{
							me.addThemeInTestpaper();
						}
						
					}
				});
				
				formOperaters.push({
					text : eap_operate_save,
					xtype : 'button',//按钮类型
					iconCls : 'save',
					tabIndex:900,
					handler : function() {
						if (me.form.getForm().isValid()&&
						   (!me.form.validate||me.form.validate(me.form.getForm().getValues(false)))){
						   	me.setAllDefScore();
						   	var examProperty = me.form.getForm().findField("examProperty").getValue();
						   	if(examProperty == '30' || examProperty == '40'){
						   		me.form.getForm().findField("state").setValue("15");
						   	}else{
							   	if(me.form.getForm().findField("publicChk")!=undefined && 
							   		me.form.getForm().findField("publicChk").getValue() == true){
									me.form.getForm().findField("state").setValue("15");
								}else{
									me.form.getForm().findField("state").setValue("5");
								}
						   	}
							me.form.submit();
						}
					}
				});
				if(!(me.examType==30 || me.examType==40)){
					formOperaters.push({
						xtype : 'button',//按钮类型
						icon : 'resources/icons/fam/user_comment.png',//按钮图标
						text : "上报",//按钮标题
						handler : function() {//按钮事件
							//Ext.Msg.alert('提示', '建设中！');
							//状态 5:保存10:上报15:发布20:打回
							me.setAllDefScore();
							me.form.getForm().findField("state").setValue("10");
							me.form.submit();
						}
					});
				}
			}
			
			formOperaters.push({
				text : eap_operate_close,
				xtype : 'button',//按钮类型
				iconCls : 'close',
				tabIndex:902,
				handler : function() {
					me.tastpaperTabPanel.close();
				}
			});
			
		me.toolTermform = new Ext.Toolbar({
				fieldDefaults:{
					labelAlign:'right',
					labelWidth:70
				},
				border : false,
				//frame : false,
				defaultType : 'textfield',
				//region: 'north',
				margins : '2 2 2 2',
				items : formOperaters
		});
			
		me.tabform = Ext.create("Ext.tab.Panel", {
                frame: true,
                height: 530,
                //width: 300,
                //activeTab:oper == 'audit' || oper == 'fpaudit'?1:0,
                items : [
                {
                	title : '基本信息',
                	dataIndex: 0,
                	nextIndex: 1,
                	items : [me.baseForm]
                },
                {
                	title : '试题列表',
                	dataIndex: 1,
                	nextIndex: 2,
                	items : [me.themeTableForm]
                }]/*,
                listeners:{
					  'tabchange':function(){
					  	  
					  }
				 }*/
			});
		me.tastpaperTabPanel = new Ext.Panel({ 
				 region : 'center',
				 frame : true,
				 bodyStyle : "border:0;padding:0px 0px 0px 0px;overflow-x:hidden; overflow-y:auto",
				 defaultType : 'textfield',
				 margins : '0 0 0 0',
				 colspan : 1,
				 forceFit : true,// 自动填充列宽,根据宽度比例
				 allowMaxSize : -1,// 允许最大行数,-1为不受限制
				 layout: 'fit',
				 tbar : me.toolTermform,
				 items :   [me.tabform],
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
				title:'试卷管理',
				//height:670,
				width:950,
				border:false,
				frame:false,
				modal:true,
				//autoScroll:true,
				bodyStyle:'overflow-x:auto;overflow-y:hidden;',
				closeAction:'hide',
				items:[me.tastpaperTabPanel]
		});
		formWin.show();
	},
	//打开表单
	getBaseForm : function(id,callback,readOnly,data,term,oper){
			var me = this;
			me.listType = '';
			var formConfig = {};
			me.isInitForm = false;
			var readOnly = readOnly || false;
			formConfig.readOnly = readOnly;
			formConfig.fileUpload = true;
			formConfig.clearButtonEnabled = false;
			formConfig.saveButtonEnabled = false;
			formConfig.formUrl = "exam/testpaper/saveForTestpaperFormAction!save.action?sf="+me.sf//保存
			formConfig.findUrl = "exam/testpaper/findForTestpaperFormAction!find.action?sf="+me.sf;
			formConfig.callback = callback;
			formConfig.columnSize = 2;
			formConfig.jsonParemeterName = 'form';
			formConfig.items = new Array();
			formConfig.closeButtonEnabled = false;
			formConfig.oper = oper;// 复制操作类型变量
			formConfig.items.push({
				xtype:'hidden',
				name:'testpaperId'
			});
			formConfig.items.push({
				xtype:'hidden',
				name:'themeIds'
			});
			formConfig.items.push({
				xtype:'hidden',
				name:'themeDefScore'
			});
			formConfig.items.push({
				xtype:'hidden',
				name:'testpaperThemeIds'
			});
			formConfig.items.push({
				xtype:'hidden',
				name:'addThemeTypes'
			});
			formConfig.items.push({
				xtype:'hidden',
				name:'state'
			});
			formConfig.items.push({
				colspan:2,
				fieldLabel:"试题内容",
				name:'testpaperName',
				xtype:oper=='view'?'displayfield':'textfield',
				allowBlank:false,
				readOnly : readOnly,
				maxLength : 800,
				labelWidth : 150,
				width:900
			});
			if(me.examType!=undefined && me.examType!=null 
				&& (me.examType==10 || me.examType==20 || me.examType==30 || me.examType==40)){
				if(me.examType == 10){
					me.examTypeName = "达标考试";
				}else if(me.examType == 20){
					me.examTypeName = "竞赛考试";
				}else if(me.examType == 30){
					me.examTypeName = "培训测试";
				}else if(me.examType == 40){
					me.examTypeName = "模拟考试";
				}
				var examTypeNameLabel = new Ext.form.Label({
					colspan : 1,
					labelWidth : 150,
					text: "考试类型: "+me.examTypeName,
					style : {	
						paddingLeft : "68px"
					}
				});
				formConfig.items.push(examTypeNameLabel);
				formConfig.items.push({
					xtype:'hidden',
					name:'examProperty',
					value:me.examType
				});
			}else{
				//,[30,'培训测试'],[40,'模拟考试']
				formConfig.items.push({
					colspan : 1,
					fieldLabel:"考试类型",
					xtype : 'select',
					allowBlank : false,
					readOnly : readOnly,
					labelWidth : 150,
					name:'examProperty',
					data:[[10,'达标考试'],[20,'竞赛考试']],
					defaultValue : 10
				});
			}
			formConfig.items.push({
				colspan : 1,
				fieldLabel:"出题方式",
				xtype : 'select',
				allowBlank : false,
				readOnly : readOnly,
				labelWidth : 150,
				name:'testpaperType',
				data:[['10','同试卷打乱'],['20','同试卷同顺序'],['30','各考生按模版随机抽题']],
				defaultValue : '10'
			});
			formConfig.items.push({
				colspan:1,
				xtype : 'select',
				name:'examTypeId',
				fieldLabel:"考试性质",
				selectUrl:'exam/exampaper/getDicKslxForExampaperListAction!getDicKslx.action',
				valueField:'dataKey',
				displayField:'dataName',
				jsonParemeterName:'examKslxList',
				labelWidth : 150,
				readOnly : readOnly,
				allowBlank : false
			});
			formConfig.items.push({
				colspan:1,
				fieldLabel:"是否私有",
				xtype:'radiogroup',
				allowBlank:false,
				width:250,
				labelWidth : 150,
				columns:2,
				readOnly : readOnly,
				items:[{boxLabel:'否',name:'isPrivate',inputValue:5,readOnly : readOnly},
					{boxLabel:'是',name:'isPrivate',inputValue:10,readOnly : readOnly}]
			});
			formConfig.items.push({
				colspan:1,
				fieldLabel:"总题数",
				name:'totalTheme',
				xtype:oper=='view'?'displayfield':'textfield',
				readOnly : true,
				maxLength : 4,
				labelWidth : 150
			});
			formConfig.items.push({
				colspan:1,
				fieldLabel:"总分数",
				name:'totalScore',
				xtype:oper=='view'?'displayfield':'textfield',
				readOnly : true,
				maxLength : 18,
				labelWidth : 150
			});
			
			formConfig.items.push({
				colspan:1,
				fieldLabel:"难度系数",
				name:'testpaperRank',
				xtype:'hidden',//oper=='view'?'displayfield':'numberfield',
				allowNegative:false, //不允许输入负数
				allowDecimals:true, //不允许输入小数   
				allowBlank:false,
				minValue: 0 ,   //最小值
				nanText:"请输入正数",
				readOnly : readOnly,
				maxLength : 18,
				labelWidth : 150
			});
			
			formConfig.items.push({
				colspan:1,
				fieldLabel:"筛选方式",
				xtype:'radiogroup',
				allowBlank:false,
				width:270,
				labelWidth : 150,
				columns:2,
				items:[{boxLabel:'题数',name:'screeningMethods',inputValue:10,readOnly : readOnly},{boxLabel:'分数',name:'screeningMethods',inputValue:5,readOnly : readOnly}],
				listeners:{  
					'change':function(){
						if(me.isInitForm){
							var setThemetype = me.form.getForm().findField("screeningMethods").getGroupValue();//配置方式 5-按分数 10-按题数 
							var t = me.paperConfigForm.columns;
							for(var i=0;i<t.length;i++){
								if((setThemetype==10 && t[i].name=='score') || (setThemetype==5 && t[i].name=='total')){
									t[i].hide();
								}else if(t[i].name=='score' || t[i].name=='total'){
									t[i].show();
								}
							}
						}
					 }  
				}	
			});
			formConfig.items.push({//0自动阅卷；1人工阅卷；2系统外阅卷
				colspan:1,
				fieldLabel:"是否使用",
				xtype:'radiogroup',
				allowBlank:false,
				width:300,
				labelWidth : 150,
				columns:2,
				items:[{boxLabel:'否',name:'isUse',inputValue:'5',readOnly : readOnly},
					{boxLabel:'是',name:'isUse',inputValue:'10',readOnly : readOnly}]
			});
			formConfig.items.push({//0自动阅卷；1人工阅卷；2系统外阅卷
				colspan:1,
				fieldLabel:"参考考时（分钟）",
				name:'testpaperTime',
				xtype:'hidden',//oper=='view'?'displayfield': 'numberfield',
				allowNegative:false, //不允许输入负数
				allowDecimals:false, //不允许输入小数   
				allowBlank:false,
				minValue: 0 ,   //最小值
				nanText:"请输入正整数",
				defaultValue : 120,
				readOnly : readOnly,
				maxLength : 4,
				labelWidth : 150
			});
			
			formConfig.items.push({
				colspan:2,
				checked : true,
				allowBlank : true,
				fieldLabel:"来自题库",
				readOnly : readOnly,
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
				selectUrl : 'base/themebank/specialityThemeBankTreeForThemeBankListAction!specialityThemeBankTree.action?notZero=true&op=auto&showProTerm=true',
				labelWidth : 150,
				width : 900,
				selectEventFun:function(combo,record,index){
					me.setSelectBanks();
				}
			});
			formConfig.items.push({
				colspan:2,
				fieldLabel:"备注",
				name:'remark',
				xtype:oper=='view'?'displayfield':'textfield',
				readOnly : readOnly,
				maxLength : 250,
				labelWidth : 150,
				width:900
			});
			formConfig.items.push(me.setPaperConfigForm(id,readOnly));
			
			/*me.paperConfigStr = new Ext.form.Hidden
					({name : 'paperConfigStr',
					  xtype : 'hidden'
					});
			me.ctForm = new Ext.Toolbar({
				columnSize : 2,
				colspan:2,
				fieldDefaults:{
					labelAlign:'right',
					labelWidth:70
				},
				border : false,
				frame : false,
				defaultType : 'textfield',
				margins : '2 2 2 2',
				items : [
					me.paperConfigStr,
					'->',
					Ext.create("Ext.Button", {
						colspan : 2,
						iconCls : 'add',
						text : '抽题',
						handler : function() {
							me.listType = 'create';
							me.themeInTemplateQuery(id);
						}
					})
				]
			});*/
			/*if(oper == 'add' || oper == 'update'){
				formConfig.items.push(me.ctForm);
			}*/
	

			formConfig.otherOperaters = [];//其它扩展按钮操作

			me.form = ClassCreate('base.model.Form',formConfig);
			me.form.isSubmit = true;
			me.form.parentWindow = this;
			me.form.close = function(){
				me.tastpaperTabPanel.close()
			}
			
			
			me.form.submit = function(callback){
				var vme = me.form;
				if(vme.isSubmit){//是否正在提交
					vme.isSubmit = false;
					if (vme.getForm().isValid()&&
					   (!vme.validate||vme.validate(vme.getForm().getValues(false)))){
						vme.MaskTip.show();
						vme.getForm().findField('json').setValue("");
						vme.getForm().findField('json').setValue(Ext.encode(Ext.Object.merge(vme.getForm().getValues(false),me.themelistform.getForm().getValues(false))));
						vme.getForm().submit({
							success : function(form, action) {
								vme.isSubmit = true;
								var msg = action.result.msg;
								if (!msg)
									msg = '操作成功！';
								vme.MaskTip.hide();
								Ext.Msg.alert('提示', msg,function(){
									vme.close();
									if(vme.callback) 
									   vme.callback(Ext.encode(vme.getForm().getValues(false)),action.result);
									if(callback) callback(Ext.encode(vme.getForm().getValues(false)),action.result);
								});
							},
							failure : function(form, action) {
								vme.isSubmit = true;
								if (action.result && action.result.length > 0)
									Ext.Msg.alert('错误提示',
											action.result[0].errors);
								else
									Ext.Msg.alert('信息', '后台未响应，网络异常！');
								vme.MaskTip.hide();
							}
						});
					}else{
						vme.isSubmit = true;
					}
				}
			};
			
			me.form.setFormData(id,function(result){
				me.examType = me.form.getForm().findField("examProperty").getValue();
				if(oper == 'add'){	
					//增加操作
					me.form.getForm().findField("isUse").setValue("10");
					me.form.getForm().findField("state").setValue("5");
					//me.form.getForm().findField("testpaperType").setValue("10");
					me.form.getForm().findField("isPrivate").setValue("5");
					//me.form.getForm().findField("examProperty").setValue("10");
					me.form.getForm().findField("screeningMethods").setValue("10");
					me.form.getForm().findField("testpaperRank").setValue(0);
					me.form.getForm().findField("testpaperTime").setValue(120);
				}else if(oper == 'update'){
					//更新操作
					me.listType = 'query';
					me.themeInTemplateQuery(id);
				}else if(oper == 'audit'){
					//更新操作
					me.listType = 'query';
					me.themeInTemplateQuery(id);
				}else if( oper == 'view'){
					me.listType = 'query';
					me.themeInTemplateQuery(id);
				}
				me.setSelectBanks();
				me.isInitForm = true;
				if(oper == 'add'){
					setTimeout(function(){
						var t = me.paperConfigForm.columns;
						for(var i=0;i<t.length;i++){
							if(t[i].name=='score'){
								t[i].hide();
							}else if(t[i].name=='total'){
								t[i].show();
							}
						}
					},300);
				}else if(oper == 'view'){
					var setThemetype = me.form.getForm().findField("screeningMethods").getValue();//配置方式 5-按分数 10-按题数 
					var t = me.paperConfigForm.columns;
					for(var i=0;i<t.length;i++){
						if((setThemetype=='题数' && t[i].name=='score') || (setThemetype=='分数' && t[i].name=='total')){
							t[i].hide();
						}else if(t[i].name=='score' || t[i].name=='total'){
							t[i].show();
						}
					}
				}else if(oper != 'view'){
					var setThemetype = me.form.getForm().findField("screeningMethods").getGroupValue();//配置方式 5-按分数 10-按题数 
					var t = me.paperConfigForm.columns;
					for(var i=0;i<t.length;i++){
						if((setThemetype==10 && t[i].name=='score') || (setThemetype==5 && t[i].name=='total')){
							t[i].hide();
						}else if(t[i].name=='score' || t[i].name=='total'){
							t[i].show();
						}
					}
				}
				if(oper == 'update' || oper == 'audit'){	
					setTimeout(function(){
						me.form.getForm().isValid();
					},300);
				}
			});
			return me.form;
	},
	setSelectBanks:function(){
		var me = this;
		var themeBankFormList = me.form.getForm().findField("themeBankFormList").getValue();
		var ids = ",";
		for(var i = 0; i<themeBankFormList.length;i++){
			var record = themeBankFormList[i];
			ids+=record.themeBankId+",";
		}
		me.selectThemeBankIds = ids;
		
		//if(me.selectThemeBankIds!='' && me.selectThemeBankIds!=','){
			var cstore = me.paperConfigForm.store;
			var isAlter = false;
			if(cstore.getCount()>0){
			    for(var i=0;i<cstore.getCount();i++){
			    	var record = cstore.getAt(i);
			    	var selectbanks = record.get("selectbanks");
			    	if(selectbanks && selectbanks.length>0){
			    		var new_b = [];
			    		var isFalg = false;
			    		for(var k=0;k<selectbanks.length;k++){
			    			 var b = selectbanks[k];
			    			 if(me.selectThemeBankIds.indexOf((","+b["themeBankId"]+","))==-1){
			    			 	isFalg = true;
			    			 }else{
			    			 	new_b[new_b.length] = b;
			    			 }
			    		}
			    		if(isFalg){
			    			record.set("selectbanks",new_b);
			    			isAlter = true;
			    		}
			    	}
			    }
			}
			if(isAlter){
				Ext.Msg.alert("提示","您删减的题库在抽题规则中已使用，系统根据您选择的题库范围自动删除抽题规则中选择题库！");
			}
		//}
	},
	getThemeTableForm : function(id, callback, readOnly,data,term,oper) {
			var me = this;
			var formConfig = {};
			var readOnly =  false;
			formConfig.readOnly = readOnly;
			formConfig.fileUpload = true;
			formConfig.callback = callback;
			formConfig.columnSize = 2;
			formConfig.jsonParemeterName = 'form';
			formConfig.bodyStyle = "overflow-y:auto;overflow-x:hidden;padding-top:1px;";
			formConfig.saveButtonEnabled = false;
			formConfig.clearButtonEnabled = false;
			formConfig.closeButtonEnabled = false;
			formConfig.operViewReadOnly = (oper == 'audit' ? true : false);
			formConfig.changeEditorForm = (oper == 'audit' ? false : undefined);
			formConfig.items = new Array();
			formConfig.oper=oper;//复制操作类型变量
			if(!readOnly){
				me.testPaperThemeTermformItems = [];
				me.testPaperThemeTermformItems.push('->');
				me.testPaperThemeTermformItems.push(new Ext.form.Label({
					text: '提示：系统可根据上面题型配置进行自动抽题，也可手工删除或添加试题。',
					margins : '0 0 0 20',
					style : {
						color:'red'
					}
				}));
				me.testPaperThemeTermformItems.push({
					xtype : 'button',//按钮类型
					icon : 'resources/icons/fam/folder_wrench.png',//按钮图标
					text : '批量修改默认分',
					handler : function(){
						me.alterThemeScore();
					}
				});
				/*me.testPaperThemeTermformItems.push({
					xtype : 'button',//按钮类型
					icon : 'resources/icons/fam/folder_go.gif',//按钮图标
					text : '系统抽题',
					handler : function() {
						if(me.themeGrid.store.getCount()>0){
							Ext.MessageBox.confirm('提示', '您需要按规则进行系统抽题吗，这将删除下列试题？',
								function(btn) {
									if (btn == 'yes'){
										me.listType = 'create';
										me.themeInTemplateQuery(id);
									}
								}
							);
						}else{
							me.listType = 'create';
							me.themeInTemplateQuery(id);
						}
					}
				});*/
				me.addThemeInTestpaperShowFlag = true;
				/*me.testPaperThemeTermformItems.push({
					xtype : 'button',//按钮类型
					icon : 'resources/icons/fam/pencil.png',//按钮图标
					text : '手工添加新试题',
					handler : function(){
						var themeBankFormList = me.form.getForm().findField("themeBankFormList").getValue();
						var handThemeBanknums = 0;
						if(themeBankFormList!=undefined && themeBankFormList.length!=undefined && themeBankFormList.length>0){
							handThemeBanknums = themeBankFormList.length;
						}
						if(handThemeBanknums == 0){
							Ext.Msg.alert('提示', '必须选择一个题库！');
						}else if(handThemeBanknums>10){
							Ext.Msg.alert('提示', '手工添加试题的题库不能大于10个！');
						}else{
							me.addThemeInTestpaper();
						}
						
					}
				});*/
				
				me.testPaperThemeTermform = new Ext.Toolbar({
					colspan : 2,
					fieldDefaults:{
						labelAlign:'right',
						labelWidth:70
					},
					border : false,
					frame : false,
					defaultType : 'textfield',
					margins : '2 2 2 2',
					items : me.testPaperThemeTermformItems
				});
				formConfig.items.push(me.testPaperThemeTermform);
		    }
			formConfig.items.push(me.setThemeTableInTemplate(readOnly));
			var otherOperaters = [];
			
			me.themelistform= ClassCreate('base.model.Form', formConfig);
			return me.themelistform;
	},
	/*setRateInTotal : function(){
		var me = this;
		try{
			if(me.form && me.form.getForm()){
				me.setThemetypeValue = me.form.getForm().findField("screeningMethods").getGroupValue();//配置方式 5-按分数 10-按题数
				if(me.setThemetypeValue == 10){
					me.totalSumValue = 0;
					for(var i=0;i<me.paperConfigForm.store.getCount();i++){
						var record = me.paperConfigForm.store.getAt(i);
						me.totalSumValue+=Number(record.get("total"));
					}
					for(var i=0;i<me.paperConfigForm.store.getCount();i++){
						var record = me.paperConfigForm.store.getAt(i);
						record.set("rate",me.totalSumValue>0?(Number(record.get("total"))/me.totalSumValue*100).toFixed(2) : 0 );
					}
				}else if(me.setThemetypeValue == 5){
					me.scoreSumValue = 0;
				    for(var i=0;i<me.paperConfigForm.store.getCount();i++){
						var record = me.paperConfigForm.store.getAt(i);
						me.scoreSumValue+=Number(record.get("score"));
				    }

					for(var i=0;i<me.paperConfigForm.store.getCount();i++){
						var record = me.paperConfigForm.store.getAt(i);
						record.set("rate",me.scoreSumValue>0?(Number(record.get("score"))/me.scoreSumValue*100).toFixed(2) : 0 );
					}
			    }
			}
		}catch(e){}
	},*/
	setAllDefScore : function(){
		var me = this;
		var dfvalueStr = "";
		for(var i=0;i<me.themeGrid.store.getCount();i++){
			var record = me.themeGrid.store.getAt(i);
			var defaultScoreStr = record.get("defaultScore");
			dfvalueStr+=record.get("themeId")+"@"+defaultScoreStr+",";
		}
		me.form.getForm().findField("themeDefScore").setValue(dfvalueStr);
	},
	setPaperConfigForm : function(id,readOnly){
		var me = this;
		/*me.themeTypeSelect = new base.core.Select({
				checked : false,
				allowBlank : true,
				readOnly : readOnly,
				xtype : 'select',
				name : 'themeTypeId',
				selectTypeName:"题型",
				selectUrl:'exam/base/theme/queryThemeTypeListForThemeListAction!queryThemeTypeList.action',
				valueField:'themeTypeId',
				displayField:'themeTypeName',
				jsonParemeterName:'themeTypeList'
		});*/
		/*me.othoptBt = [];
		if(!readOnly){
			me.othoptBt.push({
				xtype : 'button',//按钮类型
				colspan : 2,
				icon : 'resources/icons/fam/folder_go.gif',//按钮图标
				text : '系统抽题',
				handler : function() {
					if(me.themeGrid.store.getCount()>0){
						Ext.MessageBox.confirm('提示', '您需要按规则进行系统抽题吗，这将删除下列试题？',
							function(btn) {
								if (btn == 'yes'){
									me.listType = 'create';
									me.themeInTemplateQuery(id);
								}
							}
						);
					}else{
						me.listType = 'create';
						me.themeInTemplateQuery(id);
					}
				}
			});
			me.othoptBt.push({
				xtype : 'button',//按钮类型
				colspan : 2,
				icon : 'resources/icons/fam/pencil.png',//按钮图标
				text : '手工添加新试题',
				handler : function(){
					me.addThemeInTestpaper();
				}
			});
		}*/
		me.themetypeArr = [];
		me.othoptBt = [{
					xtype : 'button',//按钮类型
					icon : 'resources/icons/fam/folder_go.gif',//按钮图标
					text : '系统抽题',
					sortIndex : 200,
					handler : function() {
						if(me.themeGrid.store.getCount()>0){
							Ext.MessageBox.confirm('提示', '您需要按规则进行系统抽题吗，这将删除下列试题？',
								function(btn) {
									if (btn == 'yes'){
										me.listType = 'create';
										me.themeInTemplateQuery(id);
									}
								}
							);
						}else{
							me.listType = 'create';
							me.themeInTemplateQuery(id);
						}
					}
				}];
		//me.seletPaperBanks  = Ext.widget('selecttree',);
		me.oldSelectThemeBankIds = '';
		me.paperConfigForm = ClassCreate('base.core.EditList',{
				colspan : 2,
				fieldLabel : '系统抽题-题型配置',
				name : 'testpaperThemetypeFormList',
				xtype : 'editlist',
				addOperater : true,
				deleteOperater : true,
				readOnly : readOnly,
				viewConfig:{height:223},//高度
				otherOperaters:me.othoptBt,
				listeners:{
		          beforeedit:{
		              fn:function(editor,e,eOpts){
		              	  if(e.field == 'selectbanks'){
							if(me.selectThemeBankIds!=me.oldSelectThemeBankIds){
								e.column.getEditor().reflash("exam/testpaper/queryBankTreeForTestpaperListAction!queryBankTree.action?id="+me.selectThemeBankIds);
								me.oldSelectThemeBankIds = me.selectThemeBankIds;
							}
		              	  }
		              }
		          }
				},
				columns : [{
								width : 0,
								name : 'testpaperThemetypeId'
							},{
								name : 'selectbanks',
								header : '题库',
								width : 6,
								readOnly : readOnly,
								titleAlign:"center",
								editor : {
									checked : true,
									//allowBlank : false,
									//readOnly : false,
									xtype : 'selecttree',
									addPickerWidth : 210,
									nameKey : 'themeBankId',
									nameLable : 'themeBankName',
									//name:'banks',
									readerRoot : 'children',
									keyColumnName : 'id',
									titleColumnName : 'title',
									childColumnName : 'children',
									selectType : 'bank',
									selectTypeName : '题库',
									selectUrl : "exam/testpaper/queryBankTreeForTestpaperListAction!queryBankTree.action?id=",
									listeners:{
										collapse:function(){
											me.paperConfigForm.getView().refresh();
										}
									}
								},
								renderer:function(value,cellmeta,record,rowIndex,columnIndex,store){
										var v = "";
										//自动换行
										cellmeta.style="white-space: normal !important;";
										Ext.Array.each(value,function(item){
											if(v=="") v = item['themeBankName'];
											else v += ","+item['themeBankName'];
										});
										return v;
								}
								/*renderer : function(value, metadata, record, rowIndex, colIndex, store){
									console.log(value)
									var selectbanks = record.get("selectbanks");
									var bankName = "";
									console.log(selectbanks.length)
									for(var i=0;i<selectbanks.length;i++){
										var sBank = selectbanks[i];
										if(i==0){
											bankName = sBank["themeBankName"];
											console.log("i="+bankName)
										}else{
											bankName += ","+sBank["themeBankName"];
											console.log("i="+bankName)
										}
									}
									console.log(bankName)
									return bankName;
								}*/
							},{
								name : 'themeTypeId',
								header : '题型',
								width : 1.5,
								editor : {
										checked : false,
										allowBlank : true,
										xtype : 'select',
										name : 'themeTypeId',
										selectTypeName:"题型",
										selectUrl:'exam/base/theme/queryThemeTypeListForThemeListAction!queryThemeTypeList.action',
										valueField:'themeTypeId',
										displayField:'themeTypeName',
										jsonParemeterName:'themeTypeList'
								},
								readOnly : readOnly,
								titleAlign:"center",
								renderer : function(value, metadata, record, rowIndex, colIndex, store){
									if(value!=undefined && value!=null && value!=""){
										var themeTypeName = null;
										for(var i=0;i<me.themetypeArr.length;i++){
											if(me.themetypeArr[i].themeTypeId == value){
												themeTypeName = me.themetypeArr[i].themeTypeName;
												break;
											}
										}
										if(themeTypeName == null){
											Ext.Ajax.request({
												timeout: 60000,
												url : 'base/themetype/findForThemeTypeFormAction!find.action',
												async : false,//同步
												params : {
													id : value
												},
												success: function(response) {
													 var result = Ext.decode(response.responseText);
													 if(result!=undefined){
														 var themetypeForm = result.form;
														 me.themetypeArr[me.themetypeArr.length] = themetypeForm;
														 themeTypeName =  themetypeForm.themeTypeName;
													 }
												}
											});
										}
										return themeTypeName;
									}
									/*var rtValue = "";
									for(var i = 0 ; i< me.themeTypeSelect.store.getCount();i++){
										var rs = me.themeTypeSelect.store.getAt(i);
										if(rs.get("themeTypeId") == value){
											rtValue = rs.get("themeTypeName");
											break;
										}
									}
									return rtValue;*/
								}
							},{
								name : 'speciality',
								header : '专业',
								width : 0,
								titleAlign:"center",
								editor : {
									checked : false,
									allowBlank : true,
									//addPickerWidth:100,
									xtype : 'selecttree',
									addPickerWidth : 150,
									nameKey : 'specialityid',
									nameLable : 'specialityname',
									readerRoot : 'children',
									keyColumnName : 'id',
									titleColumnName : 'title',
									childColumnName : 'children',
									selectType:'speciality',
									selectTypeName:'专业',
									readOnly : readOnly,
									selectUrl : "baseinfo/speciality/treeForSpecialityListAction!tree.action?jobidTerm="
								},
								renderer : function(value){
									//console.log(value);
									return value['specialityname'];
								}
							},
							/*{
								name : 'rate',
								header : '比例',
								align:'center',
								width : 0.4 ,
								sortable:false,
								menuDisabled:true,
								editor : {
									xtype : 'textfield',
									allowBlank : true,
									readOnly : true
								}
							},*/
							{
								name : 'total',
								header : '题数',
								width : 1.5,
								sortable:false,
								menuDisabled:true,
								readOnly : readOnly,
								titleAlign:"center",
								editor : {
									xtype : 'numberfield',
									maxLength : 3,
									allowNegative:false, //不允许输入负数
									allowDecimals:false, //不允许输入小数   
									allowBlank : true,
									nanText:"请输入正整数",
									minValue: 0     //最小值
									
								},
								renderer : function(value2){
									//setTimeout(me.setRateInTotal(),500);
									return value2;
								}
							},{
								name : 'score',
								header : '分值',
								width : 1.5,
								sortable:false,
								menuDisabled:true,
								readOnly : readOnly,
								titleAlign:"center",
								editor : {
									xtype : 'numberfield',
									maxLength : 5,
									allowNegative:false, //不允许输入负数
									allowDecimals:true,               //不允许输入小数   
									allowBlank : true,
									nanText:"请输入正数",
									minValue: 0   //最小值
								},
								renderer : function(value){
									//setTimeout(me.setRateInTotal(),500);
									return value;
								}
							},{
								name : 'actualScore',
								header : '实际总分',
								width : 1.5,
								sortable:false,
								menuDisabled:true,
								readOnly : readOnly,
								titleAlign:"center",
								renderer : function(value){
									return value;
								}
							},{
								name : 'actualTotal',
								header : '实际题数',
								width : 1.5,
								sortable:false,
								menuDisabled:true,
								readOnly : readOnly,
								titleAlign:"center",
								renderer : function(value){
									return value;
								}
							}
				           ]
			});
		return me.paperConfigForm;
	},
	addThemeInTestpaper : function(){
			var me = this;
			if(me.addThemeInTestpaperShowFlag){
				me.addThemeInTestpaperShowFlag = false;
				me.addThemeInTpMask = new Ext.LoadMask(me.form, {  
				    msg     : '数据正在处理,请稍候',  
				    removeMask  : true// 完成后移除  
				});
				me.addThemeInTpMask.show();
				var formConfig = {};
				var readOnly = readOnly || false;
				formConfig.readOnly = readOnly;
				formConfig.fileUpload = true;
				formConfig.clearButtonEnabled = false;
				formConfig.saveButtonEnabled = false;
				//formConfig.formUrl = "exam/exampaper/saveExamUserForExampaperFormAction!saveExamUser.action";//保存
				//formConfig.findUrl = "exam/exampaper/findExamUserForExampaperFormAction!findExamUser.action";
				formConfig.callback = function() {};
				formConfig.columnSize = 1;
				formConfig.jsonParemeterName = 'examForm';
				formConfig.items = new Array();
				
				formConfig.items.push({
					xtype:'hidden',
					name:'checkHandThemeIds'
				});
				
				var selectThemeIds = me.form.getForm().findField("themeIds").getValue();
				var themeBankFormList = me.form.getForm().findField("themeBankFormList").getValue();
				var themeBankIds = "";
				if(themeBankFormList!=undefined && themeBankFormList.length!=undefined && themeBankFormList.length>0){
					for(var i=0;i<themeBankFormList.length;i++){
						themeBankIds+=themeBankFormList[i].themeBankId+",";
					}
					themeBankIds=themeBankIds.substring(0,themeBankIds.length-1);
				}
				
				me.addThemeListStore = new Ext.data.Store({
					fields:[{name:"themeId"},{name:"themeTypeId"},{name:"themeBankName"},{name:"testpaperThemeId"},{name:"checkOpt"},{name:"sortIndex"},{name:"themeTypeName"},{name:"themeName"},{name:"defaultScore"},{name:"answerkeyStr"}],
					autoLoad :false,
					proxy:{
						type : 'ajax',
						actionMethods : "POST",
						timeout: 120000,
						url:"exam/testpaper/getHandThemeListForTestpaperListAction!getHandThemeList.action?xz=page",
			            reader : {
							type : 'json',
							root : 'themeInTemplateList',
							totalProperty : "themeInTemplateListTotal"
						}
					},
					remoteSort : false
				}); 
				//console.log(selectThemeIds);
				//console.log(themeBankIds);
				me.addThemeListStore.proxy.extraParams = {
							selectThemeIds:selectThemeIds,
							themeBankIds:themeBankIds
						};
			    me.addThemeListGrid = new Ext.grid.GridPanel({
		    	 	store:me.addThemeListStore,
		    	 	autoScroll :true,
					//autoHeight:true,
		    	 	height:626,
		    	 	colspan:2,
		    	 	autoWidth:true, 
					stripeRows:true, //斑马线效果          
		    	 	columnLines : true,
		    	 	forceFit: true,
		    	 	collapsible : false,
		    	 //	title: '试题',
		    	 	bbar : new Ext.PagingToolbar({// 分页
						plugins : Ext.create('base.core.PageSizePlugin'),
						pageSize : me.pageRecordSize||grabl_pageRecordSize,
						store : me.addThemeListStore,
						displayInfo : true,
						displayMsg : eap_list_displayMsg,
						emptyMsg : eap_list_emptyMsg
					}),
			    	columns:[
			    		{header:"themeId",dataIndex:"themeId",hidden:true},
			    		{header:"themeTypeId",dataIndex:"themeTypeId",hidden:true},
			    		{header:"themeBankName",dataIndex:"themeBankName",hidden:true},
			    		{header:"testpaperThemeId",dataIndex:"testpaperThemeId",hidden:true},
			    		{header:"选择",dataIndex:"checkOpt",sortable:false, menuDisabled:true,width:20,titleAlign:"center",
			    		renderer:function(value,v1,v2){
			    			setCheckHandThemeIds = function(obj){
			    				var tmpvalue = me.form2.getForm().findField("checkHandThemeIds").getValue();
			    				if(tmpvalue==undefined || tmpvalue ==null || tmpvalue=="null" || tmpvalue==""){
			    					tmpvalue=",";
			    				}
			    				if(obj.checked){
			    					tmpvalue+=obj.value+",";
			    				}else{
			    					tmpvalue=tmpvalue.replace((obj.value+","),"");
			    				}
			    				me.form2.getForm().findField("checkHandThemeIds").setValue(tmpvalue);
			    			}
			    			
			    			return '<input type="checkbox" value="'+(v2.get("themeId"))+'" onclick="setCheckHandThemeIds(this)"/>';
			    		}},
			    		{header:"序号",dataIndex:"sortIndex",sortable:false, menuDisabled:true,width:20,titleAlign:"center"},
			    		{header:"题型",dataIndex:"themeTypeName",sortable:false, menuDisabled:true,width:35,titleAlign:"center"},
			    		{header:"试题",dataIndex:"themeName",sortable:false, menuDisabled:true,width:150,titleAlign:"center",	
			    		 renderer:function(value, metadata, record){
			    		 	var themeBankName = record.get("themeBankName");
			    			metadata.style="white-space: normal !important;";
			    			if(themeBankName!=undefined && themeBankName!=null && themeBankName!=''  && themeBankName!='null'){
			    				return value+"<br><font color=blue>[题库："+themeBankName+"]</font>";
			    			}else{
			    				return value;
			    			}
			    			
			    		}},
			    		{header:"分值",dataIndex:"defaultScore",sortable:false, menuDisabled:true,width:25,titleAlign:"center"},
			    		{header:"答案",dataIndex:"answerkeyStr",sortable:false, menuDisabled:true,width:150,titleAlign:"center",
			    		 renderer:function(value, metadata, record){
			    			metadata.style="white-space: normal !important;";
			    			return value;
			    		}}
			    		]
		    	});
		    	
				formConfig.items.push(me.addThemeListGrid);
				me.addThemeListStore.load();
				
				
				formConfig.otherOperaters = [];//其它扩展按钮操作
				var themeTypeSelectTm = Ext.widget('select',{
					xtype : 'select',
					name : 'themeTypeSelectTm',
					fieldLabel : '题型',
					selectTypeName:"题型",
					selectUrl:'exam/base/theme/queryThemeTypeAndNullListForThemeListAction!queryThemeTypeAndNullList.action',
					valueField:'themeTypeId',
					displayField:'themeTypeName',
					jsonParemeterName:'themeTypeList',
					labelWidth : 60,
					width : 160
				});
				formConfig.otherOperaters.push(themeTypeSelectTm);
				var themeBankSelectTm = Ext.widget('selecttree',{
					checked : true,
					fieldLabel:"来自题库",
					xtype : 'selecttree',
					addPickerWidth : 210,
					nameKey : 'themeBankId',
					nameLable : 'themeBankName',
					name:'themeBankSelectTm',
					readerRoot : 'children',
					keyColumnName : 'id',
					titleColumnName : 'title',
					childColumnName : 'children',
					selectType : 'bank',
					selectTypeName : '题库',
					selectUrl : 'base/themebank/specialityThemeBankTreeForThemeBankListAction!specialityThemeBankTree.action?notZero=true&op=auto&showProTerm=true',
					labelWidth :80,
					width : 350
				});
				formConfig.otherOperaters.push(themeBankSelectTm);
				themeBankSelectTm.setValue(themeBankFormList);
				var gjzNameFieldTm = new Ext.form.TextField({
					xtype : 'textfield',
					name : 'gjzNameFieldTm',
					fieldLabel : '关键字',
					labelWidth : 60,
					width:240
				});
				formConfig.otherOperaters.push(gjzNameFieldTm);
				formConfig.otherOperaters.push({
			        icon : 'resources/icons/fam/zoom.png',//按钮图标
					text : "查询",//按钮标题
					handler : function() {//按钮事件
						var themeTypeSelectValue = themeTypeSelectTm.getValue();
						var themeBankSelectValue = themeBankSelectTm.getValue();
						var gjzNameValue = gjzNameFieldTm.getValue();
						if(themeBankSelectValue && themeBankSelectValue.length &&themeBankSelectValue.length>10){
							Ext.Msg.alert('提示', '筛选的题库不能大于10个！');
						}else if(themeBankSelectValue && themeBankSelectValue.length &&themeBankSelectValue.length==0){
							Ext.Msg.alert('提示', '筛选的题库不能为0个！');
						}else{
							var themeBankIdsTm = "";
							for(var i=0;i<themeBankSelectValue.length;i++){
								themeBankIdsTm+=themeBankSelectValue[i].themeBankId+",";
							}
							var tlurl = "exam/testpaper/getHandThemeListForTestpaperListAction!getHandThemeList.action?xz=page";
							me.addThemeListStore.proxy.url = tlurl;
							me.addThemeListStore.proxy.extraParams = {
								selectThemeIds:selectThemeIds,
								themeBankIds:themeBankIdsTm,
								qthemeType:themeTypeSelectValue,
								qgjzName:gjzNameValue
							};
		    				me.addThemeListStore.load();
							
						}
					}
			    });
				formConfig.otherOperaters.push({
			        icon : 'resources/icons/fam/folder_wrench.png',//按钮图标
					text : "确定添加",//按钮标题
					handler : function() {//按钮事件
						var tmpvalue = me.form2.getForm().findField("checkHandThemeIds").getValue();
						//console.log(tmpvalue)
						var tmpList = me.addThemeListGrid.store;
		    			var index= 1;
		    			var isFalg = false;
		    			var addList = [];
						for(var i=0;i<tmpList.getCount();i++){
							var record = tmpList.getAt(i);
							var _themeId = ","+record.get("themeId")+",";
							//console.log(_themeId)
							if(tmpvalue.indexOf(_themeId)!=-1){
								var newrecord = record.copy();
								addList.push(newrecord);
								isFalg= true;
							}
						}
						if(isFalg){
							var sortIndex = 0;
							if(me.themeGrid.store.getCount()>0){
								var record = me.themeGrid.store.getAt(me.themeGrid.store.getCount()-1);
								sortIndex = Number(record.get("sortIndex"))+1;
							}
							for(var i=0;i<addList.length;i++){
								var tmpRd = addList[i];
								tmpRd.set("sortIndex",sortIndex)
								me.themeGrid.store.insert(me.themeGrid.store.getCount(), tmpRd);
								sortIndex++;
							}
							me.sortTheme();
							me.setThemeDateInTestpaper();
							Ext.Msg.alert('提示', '添加成功！',function(){
								me.handThemeformWin.hide();
							});
						}else{
							Ext.Msg.alert('提示', '没有选择添加的试题！',function(){
								me.handThemeformWin.hide();
							});
						}
					}
			    });
				
			    me.form2 = ClassCreate('base.model.Form',formConfig);
				me.form2.parentWindow = this;
				
				
				//表单窗口
				me.handThemeformWin = new WindowObject({
					layout:'fit',
					title:'手工添加试题',
					height:700,
					width:1000,
					border:false,
					frame:false,
					modal:true,
					//autoScroll:true,
					bodyStyle:'overflow-x:auto;overflow-y:hidden;',
					closeAction:'hide',
					items:[me.form2]
				});
				me.addThemeListGrid.store.on("load",function(){
					me.addThemeInTpMask.hide();
					me.handThemeformWin.show();
					me.addThemeInTestpaperShowFlag = true;
				});
			}else{
				Ext.Msg.alert('提示', '操作正在处理中！');
			}
	},
	alterThemeScore : function(){
			var me = this;
			me.alterThemeScoreMask = new Ext.LoadMask(me.form, {  
			    msg     : '数据正在处理,请稍候',  
			    removeMask  : true// 完成后移除  
			});
			me.alterThemeScoreMask.show();
			var formConfig = {};
			var readOnly = readOnly || false;
			formConfig.readOnly = readOnly;
			formConfig.fileUpload = true;
			formConfig.clearButtonEnabled = false;
			formConfig.saveButtonEnabled = false;
			//formConfig.formUrl = "exam/exampaper/saveExamUserForExampaperFormAction!saveExamUser.action";//保存
			//formConfig.findUrl = "exam/exampaper/findExamUserForExampaperFormAction!findExamUser.action";
			formConfig.callback = function() {};
			formConfig.columnSize = 1;
			formConfig.jsonParemeterName = 'form';
			formConfig.items = new Array();
			
			var themeTypeIds = "";
			var ss = me.paperConfigForm.store;
			for(var i=0;i<ss.getCount();i++){
				var _record = ss.getAt(i);
				var _themeTypeId = _record.get("themeTypeId");
				themeTypeIds+=_themeTypeId+",";			
			}
			
			me.alterThemeTypesListStore = new Ext.data.Store({
				fields:[{name:"themeTypeId"},{name:"themeTypeName"}],
				autoLoad :false,
				proxy:{
					type : 'ajax',
					actionMethods : "POST",
					timeout: 120000,
					url:"base/themetype/queryTypeslistForThemeTypeListAction!queryTypeslist.action?themeTypeIds="+themeTypeIds,
		            reader : {
						type : 'json',
						root : 'list',
						totalProperty : "total"
					}
				},
				remoteSort : false
			}); 
		    me.alterThemeTypesListGrid = new Ext.grid.GridPanel({
	    	 	store:me.alterThemeTypesListStore,
	    	 	autoScroll :true,
				autoHeight:true,
	    	 	//height:626,
	    	 	colspan:2,
	    	 	autoWidth:true, 
				stripeRows:true, //斑马线效果          
	    	 	columnLines : true,
	    	 	forceFit: true,
	    	 	collapsible : false,
				selType: 'cellmodel',  
				plugins:[  
				  Ext.create('Ext.grid.plugin.CellEditing',{  
				      clicksToEdit:1 //设置单击单元格编辑  
				   })  
				],       
		    	columns:[
		    		{header:"themeTypeId",dataIndex:"themeTypeId",hidden:true},
		    		{header:"题型",dataIndex:"themeTypeName",sortable:false, menuDisabled:true,width:4,titleAlign:"center"},
		    		{   header : '分值',
						dataIndex:"score",
						width : 1,
						sortable:false,
						menuDisabled:true,
						titleAlign:"center",
						editor : {
							xtype : 'numberfield',
							maxLength : 5,
							allowNegative:false, //不允许输入负数
							allowDecimals:true,               //不允许输入小数   
							allowBlank : true,
							nanText:"请输入正数",
							minValue: 0   //最小值
						},
						renderer : function(value){
							return value;
						}
					}]
	    	});
			formConfig.items.push(me.alterThemeTypesListGrid);
			me.alterThemeTypesListGrid.store.load();
			
			formConfig.otherOperaters = [];//其它扩展按钮操作
			formConfig.otherOperaters.push(new Ext.form.Label({
					text: '提示：不需要修改的题型不要填写分值。',
					margins : '0 0 0 20',
					style : {
						color:'red'
					}
				}));
			formConfig.otherOperaters.push({
		        icon : 'resources/icons/fam/folder_wrench.png',//按钮图标
				text : "确定修改",//按钮标题
				handler : function() {//按钮事件
					var altypelist = me.alterThemeTypesListGrid.store;
					for(var i=0;i<altypelist.getCount();i++){
						var record = altypelist.getAt(i);
						var _score = record.get("score");
						if(_score && _score!=null && _score!='' && _score>0){
							var _themeTypeId = record.get("themeTypeId");
							var tmpList = me.themeGrid.store;
							for(var k=0;k<tmpList.getCount();k++){
								var record = tmpList.getAt(k);
								if(record.get("themeTypeId") == _themeTypeId){
									record.set("defaultScore",_score);
								}
							}
						}
					}
					me.setThemeDateInTestpaper();
				}
		    });
			
		    me.form3 = ClassCreate('base.model.Form',formConfig);
			me.form3.parentWindow = this;
			
			
			//表单窗口
			me.alterThemeScoreformWin = new WindowObject({
				layout:'fit',
				title:'批量修改试题默认分',
				width:500,
				border:false,
				frame:false,
				modal:true,
				//autoScroll:true,
				bodyStyle:'overflow-x:auto;overflow-y:hidden;',
				closeAction:'hide',
				items:[me.form3]
			});
			me.alterThemeTypesListGrid.store.on("load",function(){
				me.alterThemeScoreMask.hide();
				me.alterThemeScoreformWin.show();
			});
			
	},
	setThemeTableInTemplate : function (readOnly){
		var me = this;
		me.themeStore = new Ext.data.Store({
			fields:[{name:"themeId"},{name:"optBt"},{name:"themeBankName"},{name:"testpaperThemeId"},{name:"sortIndex"},{name:"themeTypeId"},{name:"themeTypeName"},{name:"themeName"},{name:"defaultScore"},{name:"answerkeyStr"}],
			autoLoad :false,
			proxy:{
				type : 'ajax',
				actionMethods : "POST",
				timeout: 120000,
				url:'',
	            reader : {
					type : 'json',
					root : 'themeInTemplateList',
					totalProperty : "themeInTemplateListTotal"
				}
			},
			remoteSort : false
		}); 
	    me.themeGrid = new Ext.grid.GridPanel({
    	 	store:me.themeStore,
    	 	autoScroll :true,
			//autoHeight:true, 
    	 	height: readOnly ? 480 : 450,
    	 	colspan:2,
    	 	autoWidth:true, 
			stripeRows:true, //斑马线效果          
    	 	columnLines : true,
    	 	forceFit: true,
    	 	collapsible : false,
    	 	selType: 'cellmodel',  
			plugins:[  
				Ext.create('Ext.grid.plugin.CellEditing',{  
				  clicksToEdit:1 //设置单击单元格编辑  
				})  
			],     
    	 	//title: '<font style="color:#4C4C4C;font-weight:normal;">试题</font>',
	    	columns:[
	    		{header:"themeId",dataIndex:"themeId",hidden:true},
	    		{header:"themeTypeId",dataIndex:"themeTypeId",hidden:true},
	    		{header:"themeBankName",dataIndex:"themeBankName",hidden:true},
	    		{header:"testpaperThemeId",dataIndex:"testpaperThemeId",hidden:true},
	    		{header:"序号",dataIndex:"sortIndex",sortable:false, menuDisabled:true,width:20,titleAlign:"center"},
	    		{header:"题型",dataIndex:"themeTypeName",sortable:false, menuDisabled:true,width:35,titleAlign:"center"},
	    		{header:"试题",dataIndex:"themeName",sortable:false, menuDisabled:true,width:150,titleAlign:"center",
	    		 renderer:function(value, metadata, record){
		    			var themeBankName = record.get("themeBankName");
			    		metadata.style="white-space: normal !important;";
			    		if(themeBankName!=undefined && themeBankName!=null && themeBankName!=''  && themeBankName!='null'){
			    			return value+"<br><font color=blue>[题库："+themeBankName+"]</font>";
			    		}else{
			    			return value;
			    		}
		    	}},
	    		{header:"分值",dataIndex:"defaultScore",sortable:false, menuDisabled:true,width:25,titleAlign:"center",
	    			editor : {
							xtype : 'numberfield',
							maxLength : 5,
							allowNegative:false, //不允许输入负数
							allowDecimals:true,               //不允许输入小数   
							allowBlank : true,
							nanText:"请输入正数",
							allowBlank:false,
							minValue: 0 ,  //最小值
							listeners:{
								change:{
									fn:function(text,newValue,oldValue,eOpts){
										if(newValue == null || newValue == ''){
											var o=me.themeGrid.getSelectionModel().selected.get(0);
											var dataIndex = me.themeGrid.store.indexOf(o);
											var rec = me.themeGrid.store.getAt(dataIndex);
											rec.set("defaultScore",oldValue);
										}else{
											var totalScore = Number(me.form.getForm().findField("totalScore").getValue());
											me.form.getForm().findField("totalScore").setValue(totalScore + Number(newValue) - Number(oldValue));
											var o=me.themeGrid.getSelectionModel().selected.get(0);
											var dataIndex = me.themeGrid.store.indexOf(o);
											var rec = me.themeGrid.store.getAt(dataIndex);
											var themeTypeId = rec.get("themeTypeId");
											var ss = me.paperConfigForm.store;
											for(var i=0;i<ss.getCount();i++){
												var _record = ss.getAt(i);
												var _themeTypeId = _record.get("themeTypeId");
												if(themeTypeId == _themeTypeId){
													_record.set("actualScore",Number(_record.get("actualScore")) + Number(newValue) - Number(oldValue));
													break;
												}
											}
										}
									}
								}
							}
					},
					renderer : function(value){
						return value;
					}},
	    		{header:"答案",dataIndex:"answerkeyStr",sortable:false, menuDisabled:true,width:150,titleAlign:"center",
	    		 renderer:function(value, metadata, record){
		    			metadata.style="white-space: normal !important;";
		    			return value;
		    	}},
	    		{header:"操作",dataIndex:"optBt",sortable:false, menuDisabled:true,width:25,align:"center",
	    			hidden : readOnly ,renderer :
	    			function(v1,v2,v3){
	    				delTestPaperTheme = function(themeId){
	    					var tmpList = me.themeGrid.store;
	    					var index= 1;
							for(var i=0;i<tmpList.getCount();i++){
								var record = tmpList.getAt(i);
								if(record.get("themeId") == themeId){
									tmpList.remove(record);
									break;
								}
							}
							/*for(var i=0;i<tmpList.getCount();i++){
								var record = tmpList.getAt(i);
								record.set("sortIndex",index);
								index++;
							}*/
							me.setThemeDateInTestpaper();
						};
	    				var themeId = v3.get("themeId");
	    				return '<input type="button" value="删除" onclick="delTestPaperTheme(\''+themeId+'\')">'
	    			}
	    		}]
    	});
    	me.themeGrid.store.on("load", function() {
    		//设置选中的题目情况
			var themeIdsStr = "";
			var addThemeTypesStr = "";
			var testpaperThemeIdsStr = "";
			var totalSum = 0;
			var scoreSum = 0;
			var themeTypeNumArr = [];
			me.examType = me.form.getForm().findField("examProperty").getValue();
			for(var i=0;i<me.themeGrid.store.getCount();i++){
				var record = me.themeGrid.store.getAt(i);
				var themeTypeNameStr = record.get("themeTypeName");
				var defaultScoreStr = record.get("defaultScore");
				themeIdsStr+=record.get("themeId")+",";
				testpaperThemeIdsStr+=record.get("testpaperThemeId")=="" || record.get("testpaperThemeId")=="" ? "null," : record.get("testpaperThemeId")+"," ;
				addThemeTypesStr+=me.listType;
				
				totalSum+=1;
				try{scoreSum+=Number(record.get("defaultScore"));}catch(e){}
				
				//if(me.listType == 'create'){
					var themeTypeNumIndex = -1;
					for(var j=0;j<themeTypeNumArr.length;j++){
						if(themeTypeNumArr[j][0]==themeTypeNameStr){
							themeTypeNumIndex = j;
							if(themeTypeNumArr[j][3] == undefined || themeTypeNumArr[j][3] ==null){
								for(var t=0;t<me.themetypeArr.length;t++){
									if(me.themetypeArr[t].themeTypeName == themeTypeNameStr){
										themeTypeNumArr[j][3] = me.themetypeArr[t].themeTypeId;
										break;
									}
								}
							}
							break;
						}
					}
					if(themeTypeNumIndex!=-1){
						themeTypeNumArr[themeTypeNumIndex][1] = Number(themeTypeNumArr[themeTypeNumIndex][1])+1;
						themeTypeNumArr[themeTypeNumIndex][2] = Number(themeTypeNumArr[themeTypeNumIndex][2])+Number(defaultScoreStr);
					}else{
						themeTypeNumArr[themeTypeNumArr.length] = [themeTypeNameStr,1,defaultScoreStr];
					}
				//}
				
			}
			if(themeIdsStr!=""){
				themeIdsStr = themeIdsStr.substring(0,themeIdsStr.length-1);
			}
			if(addThemeTypesStr!=""){
				addThemeTypesStr = addThemeTypesStr.substring(0,addThemeTypesStr.length-1);
			}
			me.form.getForm().findField("themeIds").setValue(themeIdsStr);
			me.form.getForm().findField("addThemeTypes").setValue(addThemeTypesStr);
			me.form.getForm().findField("testpaperThemeIds").setValue(testpaperThemeIdsStr);
			me.form.getForm().findField("totalTheme").setValue(totalSum);
			me.form.getForm().findField("totalScore").setValue(scoreSum);
			
			if(me.listType == 'create'){
				var msg = "本次实际抽题结果：";
				if(themeTypeNumArr.length>0){
					var ss = me.paperConfigForm.store;
					for(var j=0;j<themeTypeNumArr.length;j++){
						msg+=themeTypeNumArr[j][0]+"有 <font style='font-weight:bold;color:blue;font-size:13px;'>"+themeTypeNumArr[j][1]+"</font> 题[共 <font style='font-weight:bold;color:blue;font-size:13px;'>"+themeTypeNumArr[j][2]+"</font> 分]，";
						for(var i=0;i<ss.getCount();i++){
							var _record = ss.getAt(i);
							//var specialityid = (record.get("speciality"))["specialityid"];
							var _themeTypeId = _record.get("themeTypeId");
							if(themeTypeNumArr[j][3] == _themeTypeId){
								_record.set("actualScore",themeTypeNumArr[j][2]);
								_record.set("actualTotal",themeTypeNumArr[j][1]);
								break;
							}
						}
					}
					for(var i=0;i<ss.getCount();i++){
							var _record = ss.getAt(i);
							if(_record.get("actualScore") == undefined || _record.get("actualScore") == null
									|| _record.get("actualScore") == ''){
								_record.set("actualScore",0);
								_record.set("actualTotal",0);
							}
					}
					msg +="合计 <font style='font-weight:bold;color:blue;font-size:13px;'>"+totalSum+"</font> 题[共 <font style='font-weight:bold;color:blue;font-size:13px;'>"+scoreSum+"</font> 分]。";
				}else{
					msg+="无。";
				}
				Ext.Msg.alert('提示', msg);
    		}
		});
    	return me.themeGrid;
	},
	setThemeDateInTestpaper : function(){
		var me = this;
		var themeIdsStr = "";
		var addThemeTypesStr = "";
		var testpaperThemeIdsStr = "";
		var totalSum = 0;
		var scoreSum = 0;
		var themeTypeNumArr = [];
		me.examType = me.form.getForm().findField("examProperty").getValue();
		for(var i=0;i<me.themeGrid.store.getCount();i++){
				var record = me.themeGrid.store.getAt(i);
				var themeTypeNameStr = record.get("themeTypeName");
				var defaultScoreStr = record.get("defaultScore");
				themeIdsStr+=record.get("themeId")+",";
				testpaperThemeIdsStr+=record.get("testpaperThemeId")=="" || record.get("testpaperThemeId")=="" ? "null," : record.get("testpaperThemeId")+"," ;
				addThemeTypesStr+=me.listType;
								
				totalSum+=1;
				try{scoreSum+=Number(record.get("defaultScore"));}catch(e){}
								
				//if(me.listType == 'create'){
					var themeTypeNumIndex = -1;
					for(var j=0;j<themeTypeNumArr.length;j++){
						if(themeTypeNumArr[j][0]==themeTypeNameStr){
							themeTypeNumIndex = j;
							if(themeTypeNumArr[j][3] == undefined || themeTypeNumArr[j][3] ==null){
								for(var t=0;t<me.themetypeArr.length;t++){
									if(me.themetypeArr[t].themeTypeName == themeTypeNameStr){
										themeTypeNumArr[j][3] = me.themetypeArr[t].themeTypeId;
										break;
									}
								}
							}
							break;
						}
					}
					if(themeTypeNumIndex!=-1){
						themeTypeNumArr[themeTypeNumIndex][1] = Number(themeTypeNumArr[themeTypeNumIndex][1])+1;
						themeTypeNumArr[themeTypeNumIndex][2] = Number(themeTypeNumArr[themeTypeNumIndex][2])+Number(defaultScoreStr);
					}else{
						themeTypeNumArr[themeTypeNumArr.length] = [themeTypeNameStr,1,defaultScoreStr];
					}
				//}
								
			}
			if(themeIdsStr!=""){
				themeIdsStr = themeIdsStr.substring(0,themeIdsStr.length-1);
			}
			if(addThemeTypesStr!=""){
				addThemeTypesStr = addThemeTypesStr.substring(0,addThemeTypesStr.length-1);
			}
			me.form.getForm().findField("themeIds").setValue(themeIdsStr);
			me.form.getForm().findField("addThemeTypes").setValue(addThemeTypesStr);
			me.form.getForm().findField("testpaperThemeIds").setValue(testpaperThemeIdsStr);
			me.form.getForm().findField("totalTheme").setValue(totalSum);
			me.form.getForm().findField("totalScore").setValue(scoreSum);
			
			
			if(themeTypeNumArr.length>0){
				var ss = me.paperConfigForm.store;
				for(var j=0;j<themeTypeNumArr.length;j++){
					for(var i=0;i<ss.getCount();i++){
						var _record = ss.getAt(i);
						//var specialityid = (record.get("speciality"))["specialityid"];
						var _themeTypeId = _record.get("themeTypeId");
						if(themeTypeNumArr[j][3] == _themeTypeId){
							_record.set("actualScore",themeTypeNumArr[j][2]);
							_record.set("actualTotal",themeTypeNumArr[j][1]);
							break;
						}
					}
				}
				for(var i=0;i<ss.getCount();i++){
						var _record = ss.getAt(i);
						if(_record.get("actualScore") == undefined || _record.get("actualScore") == null
									|| _record.get("actualScore") == ''){
							_record.set("actualScore",0);
							_record.set("actualTotal",0);
						}
				}
			}
	},
	themeInTemplateQuery : function(testpaperId){
		var me = this;
		me.examType = me.form.getForm().findField("examProperty").getValue();
		if(me.listType == 'query'){
			me.themeGrid.store.proxy.url = "exam/testpaper/getThemeListForTestpaperListAction!getThemeList.action?testpaperId="+testpaperId+"&listType="+me.listType+"&examType="+me.examType;
		    me.themeGrid.store.load();
		}else if(me.listType == 'create'){
			var themeBankFormList = me.form.getForm().findField("themeBankFormList").getValue();
			var themeBankIds = "";
			if(themeBankFormList!=undefined && themeBankFormList.length!=undefined && themeBankFormList.length>0){
				for(var i=0;i<themeBankFormList.length;i++){
					themeBankIds+=themeBankFormList[i].themeBankId+",";
				}
				themeBankIds=themeBankIds.substring(0,themeBankIds.length-1);
			}
			
			var setThemetype = me.form.getForm().findField("screeningMethods").getGroupValue();//配置方式 5-按分数 10-按题数
			var ss = me.paperConfigForm.store;
			if(ss.getCount() == 0){
				Ext.Msg.alert('提示', '系统抽题必须有一个抽题规则！');
			}else{
				var templateConfigStr = "";
				var msg = "";
				for(var i=0;i<ss.getCount();i++){
					var record = ss.getAt(i);
					var specialityid = (record.get("speciality"))["specialityid"];
					var themeTypeId = record.get("themeTypeId");
					var rate = '';//record.get("rate");
					var total = record.get("total");
					var score = record.get("score");
					
					var selectbanks = record.get("selectbanks");
					var bk = '';
			    	if(selectbanks && selectbanks.length>0){
			    		for(var kkk=0;kkk<selectbanks.length;kkk++){
			    			 var b = selectbanks[kkk];
			    			 bk+=b["themeBankId"]+',';
			    		}
			    	}
			    	
					//console.log(specialityid+" "+rate+" "+total+" "+score);
					templateConfigStr+="{"+themeTypeId+","+specialityid+","+setThemetype+","+total+","+score+","+rate+","+bk+"},";	
					if(themeTypeId == undefined || themeTypeId==null || themeTypeId==''|| themeTypeId=='null'){
						msg+="第"+(i+1)+"行必须选择题型。<br>";
					}else if(setThemetype == 5 && Number(score) == 0){
						msg+="按分数筛题，第"+(i+1)+"行分数不能为0。<br>";
					}else if(setThemetype == 10 && Number(total) == 0){
						msg+="按题数筛题，第"+(i+1)+"行数量不能为0。<br>";
					}
				}
				if(msg == ""){
					me.themeGrid.store.proxy.url = "exam/testpaper/getThemeListForTestpaperListAction!getThemeList.action";
			    	me.themeGrid.store.proxy.extraParams = {
						testpaperId:testpaperId,
						templateConfigStr:templateConfigStr,
						listType:me.listType,
						themeBankIds:themeBankIds,
						examType:me.examType
					};
			    	me.themeGrid.store.load();
				}else {
					Ext.Msg.alert('提示', msg);
				}
			}
		}else{
			me.themeGrid.store.proxy.url = "exam/testpaper/getThemeListForTestpaperListAction!getThemeList.action?testpaperId=";
	   	 	me.themeGrid.store.load();
		}
	},
	themeOpenView : function(id){
			var me = this;
			EapAjax.request({
			 	timeout: 60000,
				url : 'exam/testpaper/getThemeListForTestpaperListAction!getThemeList.action',
				params : {
					listType : 'preview',
					testpaperId : id
				},
				success : function(response) {
					var result = Ext.decode(response.responseText);
					var themeInTemplateList = result.themeInTemplateList;
					var themeShowSort = ["一","二","三","四","五","六","七","八","九","十"];
					var answerShowSort = ["A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"];
					var htmlStr = "<div style='width:100%;overflow-x:auto;overflow-y:hidden;padding:2px;word-break: break-all; word-wrap:break-word;'>";
					var oldthemeType = null;
					var titleIndex = 0;
					for(var i=0;i<themeInTemplateList.length;i++){
						if(oldthemeType == null || oldthemeType.themeTypeId != themeInTemplateList[i].themeType.themeTypeId){
							htmlStr+="<div style='background:#FFFFFF;font-weight: bold; font-size: 16px;width:100%;word-break: break-all; word-wrap:break-word;'>"+themeShowSort[titleIndex]+"、"+themeInTemplateList[i].themeType.themeTypeName+"</div>";
							oldthemeType = themeInTemplateList[i].themeType;
							titleIndex++;
						}
						var themeInType = parseInt(themeInTemplateList[i].themeType.themeType);
						var explain = themeInTemplateList[i].explain;
						htmlStr+="<div style='font-weight: normal; font-size: 14px;width:100%;'>"+(i+1)+"、"+themeInTemplateList[i].themeName+"（"+themeInTemplateList[i].defaultScore+"分）</div>";
						if(explain!=undefined && explain!="" && explain!=null){
							htmlStr+="<div style='font-weight: normal; font-size: 12px;width:100%;word-break: break-all; word-wrap:break-word;'>注解："+explain+"</div>";	
						} 
						//类型 5：单选；10：多选 15：填空；20：问答；25：判断；30：视听 35：其他
						if(themeInType== 5  || themeInType== 10  || themeInType== 25){
							var anslist = themeInTemplateList[i].theme.themeAnswerkeies;
							var eachline = themeInTemplateList[i].eachline;
							htmlStr+="<div style='font-weight: normal; font-size: 13px;word-break: break-all; word-wrap:break-word;'>"
							for(var j = 0;j<anslist.length;j++){		
								if(themeInType== 5  || themeInType== 10  || themeInType== 25){
									htmlStr+=(answerShowSort[j])+"、"+anslist[j].answerkeyValue;
									if(j == anslist.length-1 || parseInt(eachline) ==1 
										||(parseInt(eachline)>0  && j>0 && parseInt((j+1) % (parseInt(eachline))) == 0)){
										htmlStr+="<br>"
									}else{
										htmlStr+="&nbsp;&nbsp;&nbsp;&nbsp;"
									}
								}else{
									htmlStr+="<br>"
								}
							}
							htmlStr+="<br></div>";
						}else if(parseInt(themeInType) == 15){
							htmlStr = me.replaceString(htmlStr,"\\(\\)","<input type=text width=100>");
							htmlStr = me.replaceString(htmlStr,"\\（\\）","<input type=text width=100>");
							//htmlStr+="<br>";
						}else{
							htmlStr+="<div style='font-weight: normal; font-size: 13px;word-break: break-all; word-wrap:break-word;'>"
							htmlStr+="<textarea  rows='5' cols='100' style='width:300;height:50'></textarea>";
							htmlStr+="<br></div>";
						}
					}
					htmlStr+="</div>";
					me.viewTheme = new WindowObject({
						layout:'fit',
						title:'试卷预览',
						height:500,
						width:850,
						border:false,
						frame:false,
						modal:true,
						autoScroll:true,
						bodyStyle:'overflow-x:auto;background:#FFFFFF;font-size:13px;padding:0px;height:500px;',
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
	replaceString : function(str,reallyDo,replaceWith) { 
		var e=new RegExp(reallyDo,"g"); 
		words = str.replace(e, replaceWith); 
		return words; 
	},
	openWin : function (url){
	    //var reg=new RegExp("-","g"); 
		//var stringObj = "testpaerOnline";
		//var newstr=stringObj.replace(reg,"");
		var oNewWindow  = window.open(url,"testpaerOnline",'width='+(window.screen.availWidth-10)+',height='+(window.screen.availHeight-30)+ ',z-look=yes,top=0,left=0,toolbar=yes,menubar=yes,scrollbars=yes, resizable=yes,location=yes, status=yes');
		if(oNewWindow){
			oNewWindow.focus();
		}
    	return oNewWindow;
	},
	sortTheme : function(){
		var me = this;
		var themeGridStore = me.themeGrid.store;
		if(themeGridStore.getCount()>0){
			if(me.allThemeTypeList == undefined || me.allThemeTypeList == null){
				me.allThemeTypeList = [];
				Ext.Ajax.request({
		 			timeout: 10000,
		 			async : false,//同步
					url : 'exam/base/theme/queryThemeTypeListForThemeListAction!queryThemeTypeList.action',
					success: function(response) {
			    		var result = Ext.decode(response.responseText);
			    		me.allThemeTypeList = result.themeTypeList;
					}
				});
			}
			if(me.allThemeTypeList && me.allThemeTypeList.length>0){
				var newStore = [];
				for(var i=0;i<me.allThemeTypeList.length;i++){
					for(var k=0;k<themeGridStore.getCount();k++){
						var record = themeGridStore.getAt(k);
						if(me.allThemeTypeList[i].themeTypeId == record.get("themeTypeId")){
							 newStore[newStore.length] = record;
						}
					}
				}
				themeGridStore.removeAll();
				var _themeIds = "";
				for(var i=0;i<newStore.length;i++){
					var tmpRd = newStore[i];
					tmpRd.set("sortIndex",i+1)
					themeGridStore.insert(i, tmpRd);
					_themeIds+=tmpRd.get("themeId")+",";
				}
				//console.log(_themeIds)
				me.form.getForm().findField("themeIds").setValue(_themeIds)
			}
		}
	}
});