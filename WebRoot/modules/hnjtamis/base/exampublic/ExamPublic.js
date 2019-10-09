/**
 * 考试信息 的模块类
 */
ClassDefine('modules.hnjtamis.base.exampublic.ExamPublic', {
	extend : 'base.model.List',
	requires: ['modules.baseinfo.Dictionary'],
	initComponent : function() {
		var Dictionary = modules.baseinfo.Dictionary;//类别名
		this.dic_kslx = Dictionary.getDictionaryList('KSLX');
		var me = this;
		me.titleExactValidate;
		me.alertMask = new Ext.LoadMask(me, {  
		    msg     : '发布中,请稍候',  
		    removeMask  : true// 完成后移除  
		}); 
		// 模块列表对象
		this.columns = [{
					name : 'publicId',
					width : 0
				},{
					name : 'isDel',
					width : 0
				},{
					name : 'stCreateNow',
					width : 0
				},{
					name : 'examTitle',
					header : '考试或竞赛标题',
					titleAlign:"center",
					sortable:false,
					menuDisabled:true,
					width : 2
				},{
					name : 'examTypeId',
					header : '考试类型(性质)',
					align:"center",
					sortable:false,
					menuDisabled:true,
					renderer : function(value){
						var display = "";
						Ext.Array.each(me.dic_kslx.datas, function(item) {
							if (item['dataKey']==value){
								display = item['dataName'];
							}
						});
						return display;
					},
					width : 0.8
				},{
					name : 'examProperty',
					header : '是否达标考试',
					align:"center",
					sortable:false,
					menuDisabled:true,
					renderer : function(value){
						switch(value){
							case 10:
								return '达标考试';
							case 20:
								return '竞赛考试';
							default: return '';
						}
					},
					width : 0.8
				},{
					name : 'examStartTime',
					header : '报名开始时间',
					align:"center",
					sortable:false,
					menuDisabled:true,
					width : 1
				},{
					name : 'examEndTime',
					header : '报名结束时间',
					align:"center",
					sortable:false,
					menuDisabled:true,
					width : 1
				},{
					name : 'planExamTime',
					header : '计划考试时间',
					align:"center",
					sortable:false,
					menuDisabled:true,
					width : 1
				},{
					name : 'examType',
					header : '考试方式',
					align:"center",
					sortable:false,
					menuDisabled:true,
					width : 0.5,
					renderer : function(value){
						switch(value){
						case '10':
							return '在线';
						case '20':
							return '离线';
						case '30':
							return '其它';
						default : return '';
						}
					}
				},{
					name : 'isReg',
					header : '是否允许报名',
					align:"center",
					sortable:false,
					menuDisabled:true,
					width : 0.5,
					renderer:function(value){
						switch(value){
						case '0' :
							return '允许';
						case '1' :
							return '不允许';
						default:return '';
						}
					}
				},{
					name : 'state',
					header : '状态',
					align:"center",
					sortable:false,
					menuDisabled:true,
					width : 0.5,
					renderer:function(value, metadata, record){
						var stCreateNow = record.get("stCreateNow");
						if(stCreateNow && stCreateNow == 'T'){
							return '<font color="red">处理中</font>';
						}else{
							switch(value){
								case '10' :
									return '<font color="red">保存</font>';
								case '20' :
									return '<font color="green">发布</font>';
								default:return '';
							}
						}	
					}
				}];
		// 模块查询条件对象
		this.termWidth = 300;
		this.terms = [{
					xtype : 'textfield',
					name : 'titleTerm',
					fieldLabel : '考试或竞赛标题',
					labelWidth : 120,
					width : 340
				},{
					name : 'startTerm',
					xtype: 'datefield',
					format : 'Y-m-d',
					fieldLabel : '报名起始时间',
					labelWidth : 120,
					width:280
				},{
					name : 'endTerm',
					xtype: 'datefield',
					format : 'Y-m-d',
					fieldLabel : '报名截止时间',
					labelWidth : 120,
					width:280
				},{
					xtype : 'select',
					name : 'examTypeIdTerm',
					fieldLabel : '考试类型(性质)',
					selectUrl:'baseinfo/dic/findDataForDicFormAction!findData.action?typeCode=KSLX',
					valueField:'dataKey',
					displayField:'dataName',
					jsonParemeterName:'datas',
					defaultValue:'',
					labelWidth : 120,
					width:280
				},{
					xtype : 'radiogroup',
					fieldLabel : '状态',
					items : [
					     {
				        	 boxLabel:'全部',
				        	 inputValue:'30',
				        	 name : 'stateTerm',
				        	 checked : true,
				        	 width:60
				         },
				         {
				        	 boxLabel:'保存',
				        	 inputValue:'10',
				        	 name : 'stateTerm',
				        	 width:60
				         },
				         {
				        	 boxLabel:'发布',
				        	 inputValue:'20',
				        	 name : 'stateTerm',
				        	 width:90
				         }
					         ],
					labelWidth : 100,
					width:280
				}];
		this.keyColumnName = "publicId";// 主健属性名
		this.viewOperater = true;
		this.addOperater = true;
		this.deleteOperater = false;
		this.updateOperater = false;
		this.otherOperaters = [];//其它扩展按钮操作
		
		me.updateBt = Ext.create("Ext.Button", {
							iconCls : 'update',
							text : eap_operate_update,
							resourceCode:me.updateResourceCode,
							handler : function() {
								var selected = me.getSelectionModel().selected;
								if (selected.getCount() == 0) {
									Ext.Msg.alert('提示', '请单击记录！');
									return;
								}
								
								var id = "";
								var record = null;
								for (var i = 0; i < selected.getCount(); i++) {
									record = selected.get(i);
									id = record.get(me.keyColumnName);
								}
								var state = record.get("state");
								var stCreateNow = record.get("stCreateNow");
								if(stCreateNow && stCreateNow == 'T'){
									Ext.Msg.alert('提示', '信息已发布，不能进行修改！');
								
								//状态 5:保存10:上报15:发布20:打回
								}else if(state==null || state=='' || state === '10' ){
									if(me.termForm)
								  		queryTerm = me.termForm.getValues(false);
									me.openFormWin(id, function() {
											me.termQueryFun();
										},false,record.data,queryTerm,'update');
								}else{
									Ext.Msg.alert('提示', '信息已发布，不能进行修改！');
								}
								
							}
						});
		this.otherOperaters.push(me.updateBt);
		
		
		this.otherOperaters.push(Ext.create("Ext.Button", {
							iconCls : 'remove',
							text : eap_operate_delete,
							resourceCode:me.deleteResourceCode,
							handler : function() {
								var selected = me.getSelectionModel().selected;
								if (selected.getCount() == 0) {
									Ext.Msg.alert('提示', '请单击记录！');
									return;
								}
								var id = "";
								var record = null;
								for (var i = 0; i < selected.getCount(); i++) {
									record = selected.get(i);
									id = record.get(me.keyColumnName);
								}
								var state = record.get("state");
								var stCreateNow = record.get("stCreateNow");
								if(stCreateNow && stCreateNow == 'T'){
									Ext.Msg.alert('提示', '信息已发布，不能进行修改！');
								//状态 5:保存10:上报15:发布20:打回
								}else if(state==null || state=='' || state === '10' ){
									var confirmFn = function(btn) {
									if (btn == 'yes')
										me.deleteData(id);
									};
									Ext.MessageBox.confirm('询问', '你真要删除这些数据吗？',
											confirmFn);
								}else{
									Ext.Msg.alert('提示', '信息已发布，不能进行删除！');
								}
								
							}
						}));
						
		/*var ExamArrangeBtn = Ext.button.Button({
			text : '发布',
			iconCls : 'view',
			handler : function(){
				var selected = me.getSelectionModel().selected;
				if (selected.getCount() == 0) {
					Ext.Msg.alert('提示', '请选择需要发布的记录！');
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
					Ext.Msg.alert('提示', '请选择状态为未发布 且 状态为未删除的记录！');
					return;
				}else{
					me.alertMask.show();
					Ext.Ajax.request({
						url:'base/exampublic/auditListForExamPublicListAction!auditList.action',
						params:{
							ids : id
						},
						success:function(response){
							var re = Ext.decode(response.responseText);
							if(re['updateResult']=='success'){
								Ext.Msg.alert("信息","发布操作成功");
							}else{
								Ext.Msg.alert("信息","发布操作失败");
							}
							me.alertMask.hide();
							me.termQueryFun(false,'flash');
						},
						failure:function(){
							Ext.Msg.alert("信息","未能与服务器取得通讯");
							me.alertMask.hide();
						}
					});
				}
				
			}
		});
		
		this.otherOperaters.push(ExamArrangeBtn);*/
		
		
		me.hzviewBt = Ext.create("Ext.Button", {
							text : '查看消息回执',
							iconCls : 'view',
							handler : function() {
								var selected = me.getSelectionModel().selected;
								if (selected.getCount() == 0) {
									Ext.Msg.alert('提示', '请单击记录！');
									return;
								}
								
								var id = "";
								var record = null;
								for (var i = 0; i < selected.getCount(); i++) {
									record = selected.get(i);
									id = record.get(me.keyColumnName);
								}
								var state = record.get("state");
								var stCreateNow = record.get("stCreateNow");
								if(stCreateNow && stCreateNow == 'T'){
									Ext.Msg.alert('提示', '信息已发布，不能进行修改！');
								
								//状态 5:保存10:上报15:发布20:打回
								}else if(state==null || state=='' || state === '10' ){
									Ext.Msg.alert('提示', '信息必须发布了才可以看消息回执！'); 
								}else{
									me.openMsgHz(id);
								}
								
							}
						});
		this.otherOperaters.push(me.hzviewBt);
		this.listUrl = "base/exampublic/listForExamPublicListAction!list.action";// 列表请求地址
		this.deleteUrl = "base/exampublic/deleteForExamPublicListAction!delete.action";// 删除请求地址
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
			sortConfig.saveSortUrl = 'base/exampublic/saveSortForExamPublicFormAction!saveSort.action';
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
		me.form = null;
		var IsExsit=false;
		var IsExsitFlag=true;
		// 打开表单页面方法
		this.openFormWin = function(id, callback, readOnly,data,term,oper) {
			var formConfig = {};
			var readOnly = readOnly || false;
			formConfig.readOnly = readOnly;
			//formConfig.fileUpload = true;
			formConfig.formUrl = "base/exampublic/saveForExamPublicFormAction!save.action";
			formConfig.findUrl = "base/exampublic/findForExamPublicFormAction!find.action";
			formConfig.callback = callback;
			formConfig.clearButtonEnabled = false;
			formConfig.columnSize = 2;
			formConfig.jsonParemeterName = 'form';
			formConfig.items = new Array();
			formConfig.oper = oper;// 复制操作类型变量
			formConfig.items.push({
						xtype : 'hidden',
						name : 'publicId'
					});
			//=====================================================================
			formConfig.items.push({
						colspan : 2,
						fieldLabel : '考试和竞赛标题',
						name : 'examTitle',
						xtype : 'textfield',
						allowBlank : false,
						readOnly : readOnly,
						//validateOnChange : false,
						validator : function(thisText){
							if(!!thisText){ //不为空
								var publicIdValue = me.form.getForm().findField('publicId').getValue();
									Ext.Ajax.request({
										url : 'base/exampublic/queryExactExamPublishForExamPublicListAction!queryExactExamPublish.action',
										method : 'post',
										params : {
											titleTerm : thisText,
												id	  : publicIdValue
										},
										success:function(response){
											var re = Ext.decode(response.responseText);
											if(re['isExist']=='0'){
												ReturnValue(true);
											}else{
												ReturnValue('此名称已存在');
											}
										},
										failure:function(){
											Ext.Msg.alert("信息","未能与服务器取得通讯");
										}
									});
							}
							if(IsExsitFlag){
								IsExsitFlag = false;
								IsExsit = true;
							}
							function ReturnValue(ok){
							   IsExsit = ok;
							}
							return IsExsit;
						},
						labelWidth:130,
						width :630
					});
			formConfig.items.push({
				colspan : 2,
				fieldLabel : '适应范围',
				labelWidth:130,
				width :630,
				name : 'examScope',
				xtype : 'hidden',
				//allowBlank : false,
				//readOnly : readOnly,
				maxLength : 32
			});
			me.kslxHidden = new Ext.form.field.Hidden({
				name : 'examTypeName'
			});
			formConfig.items.push(me.kslxHidden);
			
			/*me.kslxCombo = new base.core.Select({
				name : 'examTypeId',
				fieldLabel : '考试类型(性质)',
				labelWidth:130,
				selectUrl:'baseinfo/dic/findDataForDicFormAction!findData.action?typeCode=KSLX',
				valueField:'dataKey',
				displayField:'dataName',
				jsonParemeterName:'datas',
				allowBlank : false,
				defaultValue:null,
				readOnly:readOnly,
				selectEventFun : function(combo,record,index){
					kslxHidden.setValue(record[0]['data']['dataName']);
				}
			});*/
			formConfig.items.push({
				colspan : 1,
				labelWidth:130,
				fieldLabel : '是否达标考试',
				xtype : 'radiogroup',
				items : [
				         {
				        	 boxLabel : '达标考试',
				        	 name : 'examProperty',
				        	 inputValue : '10',
				        	 checked : true,
				        	 readOnly : readOnly,
				        	 width : 90
				         },
				         {
				        	 boxLabel : '竞赛考试',
				        	 name : 'examProperty',
				        	 inputValue : '20',
				        	 readOnly : readOnly,
				        	 width : 90
				         }
				        ],
				width : 260,
				allowBlank : false,
				readOnly : readOnly,
				listeners : {
					'change' : function(radiogroup,radio){
						me.changeExamProperty(oper);
					}
				}
			});
			
			me.kslxCombo = new base.core.Select({
				colspan:1,
				xtype : 'select',
				name:'examTypeId',
				fieldLabel : '考试类型(性质)',
				selectUrl:'exam/exampaper/getDicKslxForExampaperListAction!getDicKslx.action',
				valueField:'dataKey',
				displayField:'dataName',
				jsonParemeterName:'examKslxList',
				labelWidth:130,
				readOnly : readOnly,
				allowBlank : false
			});
			formConfig.items.push(me.kslxCombo);
			
			
			
			formConfig.items.push({
				colspan : 2,
				checked : true,
				labelWidth:130,
				width :630,
				fieldLabel : '参与考试机构',
				name : 'organs',
				xtype : 'selecttree',
				allowBlank : false,
				//addPickerWidth:150,
				readOnly : readOnly,
				nameKey : 'organId',
				nameLable : 'organName',
				readerRoot : 'organs',
				keyColumnName : 'organId',
				titleColumnName : 'organName',
				childColumnName : 'organs',
				levelAffect : '00',
				selectUrl : "organization/organ/listForOrganListAction!list.action?topOrganTerm="+base.Login.userSession.currentOrganId,
				selectEventFun:function(combo,record,index){
				}
			});
			
			if(oper != 'view'){
				formConfig.items.push({
						colspan : 2,
						labelWidth:130,
						width :630,
						fieldLabel : '参与考试岗位',
						checked : true,
						name : 'standardQuarterList',
						xtype : 'selecttree',
						readOnly : readOnly,
						nameKey : 'quarterTrainCode',
						nameLable : 'quarterTrainName',
						//readerRoot : 'children',
						keyColumnName : 'id',
						titleColumnName : 'title',
						childColumnName : 'children',
						selectUrl : "jobstandard/termsEx/getQuarterStandardForStandardTermsExListAction!getQuarterStandard.action?publicId="+id,
						allowBlank : false
				});			
			}
			
			formConfig.items.push({
				colspan : 1,
				fieldLabel : '机构名称',
				labelWidth:130,
				name : 'examOrgan',
				xtype : 'textfield',
				allowBlank : false,
				readOnly : readOnly,
				maxLength : 32
			});
			formConfig.items.push({
				colspan : 1,
				labelWidth:130,
				fieldLabel : '主办人',
				name : 'examSponsor',
				xtype : 'textfield',
				allowBlank : false,
				readOnly : readOnly,
				maxLength : 32
			});
			/*formConfig.items.push({
				colspan : 2,
				//allowBlank:false,
				fieldLabel : '专业',
				labelWidth:130,
				width :630,
				name : 'specialitys',
				xtype : 'selecttree',
				checked : true,
				readOnly : readOnly,
				nameKey : 'specialityid',
				nameLable : 'specialityname',
				readerRoot : 'children',
				keyColumnName : 'id',
				titleColumnName : 'title',
				childColumnName : 'children',
				selectType : 'speciality',
				selectTypeName : '专业',
				selectUrl : 'baseinfo/speciality/treeForSpecialityListAction!tree.action'
			});*/
			 /*formConfig.items.push({
				    colspan:2,
				    checked : true,
				    allowBlank : true,
				    width :630,
				    fieldLabel:"岗位",
				    xtype : 'selecttree',
				    nameKey : 'postId',
				    nameLable : 'postName',
				    name:'themePostFormList',
				    readOnly : readOnly,
				    //readerRoot : 'children',
				    //keyColumnName : 'id',
				    //titleColumnName : 'title',
				    keyColumnName : 'id',
				    titleColumnName : 'title',
				    childColumnName : 'children',
				    selectUrl : "organization/quarter/odqtreeForQuarterListAction!odqtree.action",
				    labelWidth:130
			});*/
			
			/*formConfig.items.push({
					colspan : 2,
					fieldLabel : '题库',
					name : 'themeBank',
					labelWidth:130,
					addPickerWidth:100,
					width :630,
					xtype : 'selecttree',
					readOnly : readOnly,
					nameKey : 'themeBankId',
					nameLable : 'themeBankName',
					readerRoot : 'themeBanks',
					keyColumnName : 'themeBankId',
					titleColumnName : 'themeBankName',
					childColumnName : 'themeBanks',
					selectUrl :'base/themebank/listForThemeBankListAction!list.action'
			});*/
			formConfig.items.push({
				colspan : 2,
				labelWidth:130,
				fieldLabel : '详细说明',
				name : 'examExplain',
				xtype : 'textfield',
				readOnly : readOnly,
				width :630
			});
			/*formConfig.items.push({
				colspan : 1,
				labelWidth:130,
				fieldLabel : '报名开始时间',
				name : 'examStartTime',
				xtype: 'datetimefield',
				format : 'Y-m-d H:i:s',
				allowBlank : false,
				readOnly : readOnly
			});*/
			formConfig.items.push({
				colspan : 1,
				fieldLabel : '报名截至时间',
				labelWidth:130,
				name : 'examEndTime',
				xtype: 'datefield',
				format : 'Y-m-d',
				allowBlank : false,
				readOnly : readOnly
			});
			formConfig.items.push({
				colspan : 1,
				fieldLabel : '计划考试时间',
				labelWidth:130,
				name : 'planExamTime',
				xtype: 'datetimefield',
				format : 'Y-m-d H:i:s',
				allowBlank : false,
				readOnly : readOnly,
				listeners : {
					'select' : function(){
						var scoreStartTime = me.form.getForm().findField("scoreStartTime");
						if(oper == 'add' || (scoreStartTime!=undefined && scoreStartTime.getValue()!=undefined
						 	&& scoreStartTime.getValue()!="")){
						    var planExamTime = me.form.getForm().findField("planExamTime").getValue();
						 	scoreStartTime.setValue(Ext.Date.add(planExamTime,Ext.Date.DAY,1));
						 	me.form.getForm().findField("scoreEndTime").setValue(Ext.Date.add(planExamTime,Ext.Date.YEAR,1));
						}
					}
				}
			});
			formConfig.items.push({
				colspan : 1,
				labelWidth:130,
				fieldLabel : '考试方式',
				xtype : 'radiogroup',
				items : [
				         {
				        	 boxLabel:'在线',
				        	 inputValue:'10',
				        	 name : 'examType',
				        	 readOnly : readOnly,
				        	 checked : true,
				        	 width:50
				         },
				         {
				        	 boxLabel:'离线',
				        	 inputValue:'20',
				        	 name : 'examType',
				        	 readOnly : readOnly,
				        	 width:50
				         },
				         {
				        	 boxLabel:'其它',
				        	 inputValue:'30',
				        	 name : 'examType',
				        	 readOnly : readOnly,
				        	 width:50
				         }
				        ],
				width:240,
				allowBlank : false,
				readOnly : readOnly
			});
			formConfig.items.push({
				colspan : 1,
				fieldLabel : '是否允许报名',
				labelWidth:130,
				xtype : 'radiogroup',
				items : [
				         {
				        	 boxLabel : '允许',
				        	 name : 'isReg',
				        	 inputValue :'0',
				        	 checked : true,
				        	 readOnly : readOnly,
				        	 width : 80
				         },
				         {
				        	 boxLabel : '不允许',
				        	 name : 'isReg',
				        	 inputValue :'1',
				        	 readOnly : readOnly,
				        	 width : 80
				         }
				        ],
				width : 240,
				allowBlank : false,
				readOnly : readOnly
			});
			formConfig.items.push({
				colspan : 1,
				fieldLabel : '达标有效期(≥)',
				labelWidth:130,
				name : 'scoreStartTime',
				xtype: 'datefield',
				format : 'Y-m-d',
				allowBlank : false,
				readOnly : readOnly
			});
			formConfig.items.push({
				colspan : 1,
				fieldLabel : '达标有效期(＜)',
				labelWidth:130,
				name : 'scoreEndTime',
				xtype: 'datefield',
				format : 'Y-m-d',
				allowBlank : false,
				readOnly : readOnly
			});
			
			formConfig.items.push({
				colspan : 1,
				labelWidth:130,
				fieldLabel : '发布人',
				name : 'publicUser',
				xtype : 'textfield',
				allowBlank : false,
				readOnly : readOnly
			});
			/*formConfig.items.push({
					colspan : 1,
					labelWidth:130,
					fieldLabel : '达标人员是否参加',
					xtype : 'radiogroup',
					name : "reachUserJoinGroup",
					items : [
					         {
					        	 boxLabel : '不参加',
					        	 name : 'reachUserJoin',
					        	 inputValue : '10',
					        	 checked : true,
					        	 width : 90
					         },
					         {
					        	 boxLabel : '参加',
					        	 name : 'reachUserJoin',
					        	 inputValue : '20',
					        	 width : 90
					         }
					         ],
					width : 240,
					allowBlank : false,
					readOnly : readOnly
				});*/
			formConfig.items.push({
					colspan : 1,
					labelWidth:130,
					fieldLabel : '达标人员是否参加',
					xtype : 'hidden',
					value: '20'
			});
			formConfig.items.push({
				colspan : 1,
				fieldLabel : '发布时间',
				labelWidth:130,
				name : 'publicTime',
				xtype: 'datetimefield',
				format : 'Y-m-d H:i:s',
				allowBlank : false,
				readOnly : readOnly
			});
			formConfig.items.push({
				colspan : 1,
				//labelWidth:130,
				fieldLabel : '审核人',
				name : 'checkUser',
				xtype : 'hidden'//,
				//readOnly : true,
				//hidden:true
			});
			formConfig.items.push({
				colspan : 1,
				fieldLabel : '审核时间',
				//labelWidth:130,
				name : 'checkTime',
				xtype : 'hidden'//,
				//format : 'Y-m-d',
				//readOnly : true,
				//hidden:true
			});
			if(!readOnly){
				/*formConfig.items.push({
					colspan : 1,
					labelWidth:130,
					fieldLabel : '是否发布',
					xtype : 'radiogroup',
					items : [
					         {
					        	 boxLabel : '否',
					        	 name : 'state',
					        	 inputValue : '10',
					        	 checked : true,
					        	 width : 90
					         },
					         {
					        	 boxLabel : '是',
					        	 name : 'state',
					        	 inputValue : '20',
					        	 width : 90
					         }
					         ],
					width : 240,
					allowBlank : false,
					readOnly : readOnly
				});*/
				formConfig.items.push({
					name : 'state',
					xtype : 'hidden'
				});
				formConfig.items.push({
						xtype : 'hidden',
						name : 'isDel',
						value : '0'
				});
				/*formConfig.items.push({
					colspan : 1,
					labelWidth:130,
					fieldLabel : '是否删除',
					xtype : 'radiogroup',
					items:[
					      {
					    	  boxLabel:'是',
					    	  inputValue : '1',
					    	  name : 'isDel',
					    	  width : 100
					      },
					      {
					    	  boxLabel:'否',
					    	  inputValue : '0',
					    	  name : 'isDel',
					    	  checked : true,
					    	  width : 100
					      }
					      
					      ],
					allowBlank : false,
					width : 240,
					readOnly : readOnly
				});*/
			}
			formConfig.items.push({
				colspan : 2,
				labelWidth:130,
				fieldLabel : '备注',
				name : 'remark',
				xtype : 'textfield',
				readOnly : readOnly,
				 width :630
			});
			formConfig.items.push({
				name : 'oldReachUserJoin',
				xtype : 'hidden'
			});
			if(oper == 'view'){
				me.jobsStandardQuartersForm = ClassCreate('base.core.EditList',{
					colspan : 2,
					fieldLabel : '参与考试岗位',
					name : 'standardQuarterList',
					xtype : 'editlist',
					addOperater : false,
					deleteOperater : false,
					readOnly : readOnly,
					viewConfig:{height:100},//高度
					columns : [
								{
									name : 'deptName',
									header : '部门',
									width : 1,
									align:'center',
									sortable:false,
									menuDisabled:true,
									titleAlign:"center"
								},{
									name : 'quarterTrainName',
									header : '岗位',
									width : 1,
									align:'center',
									sortable:false,
									menuDisabled:true,
									titleAlign:"center"
								},
								{
									name : 'quarterTrainCode',
									header : '岗位编码',
									width : 1,
									align:'center',
									sortable:false,
									menuDisabled:true,
									titleAlign:"center"
								}
					 ]
				});
				formConfig.items.push(me.jobsStandardQuartersForm);
			}
			
			formConfig.otherOperaters = [];//其它扩展按钮操作
			formConfig.otherOperaters.push({
					text : "发布",//按钮标题
					xtype : 'button',//按钮类型
					icon : 'resources/icons/fam/folder_go.png',//按钮图标
					tabIndex:900,
					timeout: 600000,
					handler : function() {
						if (me.form.getForm().isValid()&&
						   (!me.form.validate||me.form.validate(me.form.getForm().getValues(false)))){
						   	me.form.getForm().findField("state").setValue("20");
							me.form.submit();
						}
					}
			});
			
			me.form = ClassCreate('base.model.Form', formConfig);
			me.form.parentWindow = this;
			// 表单窗口
			var formWin = new WindowObject({
						layout : 'fit',
						title : '信息发布维护',
						width : 700,
						border : false,
						frame : false,
						modal : true,// 模态
						closeAction : 'hide',
						items : [me.form]
					});	
			formWin.show();
			me.form.setFormData(id,function(result){
				if(oper != 'view'){
					me.form.getForm().findField("examSponsor").setValue(base.Login.userSession.employeeName);
					me.form.getForm().findField("examOrgan").setValue(base.Login.userSession.currentOrganName);
					me.form.getForm().findField("publicUser").setValue(base.Login.userSession.employeeName);
					me.form.getForm().findField("publicTime").setValue(new Date());
					
					var _state = me.form.getForm().findField("state").getValue();
					if(_state==undefined || _state =='' || _state==null){
						me.form.getForm().findField("state").setValue("10");
					}
				}
				
				//if(oper == 'add'){
				   //me.form.getForm().findField("oldReachUserJoin").setValue('10');
				//}else{
				  // me.form.getForm().findField("oldReachUserJoin").setValue(me.form.getForm().findField("reachUserJoin").getValue());
				//}
				/*me.form.getForm().findField("organs").store.on("load",function(){
					var organsStore = me.form.getForm().findField("organs").store.proxy.reader.jsonData;
					if(organsStore.organs.length > 0 ){
						me.form.getForm().findField("organs").setValue((organsStore.organs)[0]);
					}
				});*/
				me.changeExamProperty(oper);
			});
		};
		this.callParent();
		
		
		me.stCreateNowIds = "";
		me.examPbTaskRunner = new Ext.util.TaskRunner();  
		me.examPbTask = {
		    run: function(){
		    	if(me.stCreateNowIds!=""){
			        EapAjax.request({
						timeout: 10000,
						url : 'base/exampublic/getExamStCreateNowForExamPublicListAction!getExamStCreateNow.action',
						async : false,
						params : {id : me.stCreateNowIds},
						success : function(response) {
							var result = Ext.decode(response.responseText);
							if(result.scexers ==undefined || result.scexers==null || result.scexers==''){
								me.examPbTaskRunner.stop(me.examPbTask);
								result.scexers="";
							}else{
								me.stCreateNowIds = result.scexers;
							}
							for(var i=0;i<me.store.getCount();i++){
								var rs = me.store.getAt(i);
								var stCreateNow = rs.get("stCreateNow");
								if(stCreateNow=='T' && result.scexers.indexOf(rs.get("publicId")) ==-1){
									rs.set("stCreateNow","F");
									me.getView().refreshNode(i);  
								}
							}
						},failure : function() {
							me.examPbTaskRunner.stop(me.examPbTask);
						}
					});
		    	}else{
		    		me.examPbTaskRunner.stop(me.examPbTask);
		    	}
		    },
		    interval: 30000
		}
		
		me.store.on("load",function(){
			me.stCreateNowIds = "";
			for(var i=0;i<me.store.getCount();i++){
				var rs = me.store.getAt(i);
				var stCreateNow = rs.get("stCreateNow");
				if(stCreateNow == 'T'){
					me.stCreateNowIds+=rs.get("publicId")+",";
				}
			}
			if(me.stCreateNowIds!=""){
				me.examPbTaskRunner.start(me.examPbTask);
			}else{
				me.examPbTaskRunner.stop(me.examPbTask);
			}
		});
	},
	changeExamProperty : function(oper){
		var me = this;
		var examProperty = "10";
		if(me.form.getForm().findField("examProperty").getValue()){
			me.form.getForm().findField("scoreStartTime").show();
			me.form.getForm().findField("scoreEndTime").show();
			me.form.getForm().findField("scoreStartTime").allowBlank = false;
			me.form.getForm().findField("scoreEndTime").allowBlank = false;
			me.form.getForm().findField("publicUser").colspan = 1;
			//if(oper!='view')me.form.getForm().findField("reachUserJoin").setValue('20');
			//if(oper!='view')me.form.getForm().findField("reachUserJoinGroup").show();
		}else{
			me.form.getForm().findField("scoreStartTime").hide();
			me.form.getForm().findField("scoreEndTime").hide();
			me.form.getForm().findField("scoreStartTime").allowBlank = true;
			me.form.getForm().findField("scoreEndTime").allowBlank = true;
			me.form.getForm().findField("publicUser").colspan = 1;
			//if(oper!='view')me.form.getForm().findField("reachUserJoin").setValue( me.form.getForm().findField("oldReachUserJoin").getValue());
			//if(oper!='view')me.form.getForm().findField("reachUserJoinGroup").hide();
			examProperty = "20";
		}
		me.setExamTypeIdList();
	},
	setExamTypeIdList : function(){
		var me = this;
		var examProperty = me.form.getForm().findField("examProperty").getValue();
		if(examProperty == true || examProperty == '达标考试'){
			examProperty = "10";
		}else{
			examProperty = "20";
		}
		if(examProperty == '10' || examProperty == '20'){
			/*if(me.form.getForm().findField("examProperty").getValue()){
				
			}else{
				examProperty = "20";
			}*/
			var examTypeIdObj= me.kslxCombo;
			var examTypeId = examTypeIdObj.getValue();
			examTypeIdObj.reflash("exam/exampaper/getDicKslxForExampaperListAction!getDicKslx.action?examPropertyTerm="+examProperty);				
			examTypeIdObj.store.on("load",function(){
			var __store = examTypeIdObj.store;
			if(__store.getCount()>0){
				var v1 = "";
				for(var i=0;i<__store.getCount();i++){
						var recode = __store.getAt(i);
						if(i == 0){
						  v1 = recode.get(examTypeIdObj.valueField);
						  examTypeIdObj.setValue(v1);
						  examTypeIdObj.select(v1);
						}
						if(recode.get(examTypeIdObj.valueField) == examTypeId){
							examTypeIdObj.setValue(examTypeId);
							examTypeIdObj.select(examTypeId);
							break;
						}
					}
				}else{
					examTypeIdObj.setValue("");
				}
			});
		}
	},
	openMsgHz : function(id){
			var me = this;
			me.msgHzMask = new Ext.LoadMask(me, {  
				 msg     : '数据正在处理,请稍候',  
				 removeMask  : true// 完成后移除  
			});
			me.msgHzMask.show();
			me.msgHzListGridStore = new Ext.data.Store({
				fields:[{name:"userOrganName"},{name:"employeeName"},{name:"msgHzFlag"}],
				autoLoad :false,
				proxy:{
					type : 'ajax',
					actionMethods : "POST",
					timeout: 120000,
					url:"base/exampublicuser/msgHzlistForExamPublicUserListAction!msgHzlist.action?publicIdTerm="+id,
		            reader : {
						type : 'json',
						root : 'list',
						totalProperty : "total"
					}
				},
				remoteSort : false
			}); 
		    me.msgHzListGrid = new Ext.grid.GridPanel({
	    	 	store:me.msgHzListGridStore,
	    	 	bodyStyle:'overflow-x:hidden;overflow-y:hidden;',
	    	 	autoScroll :true,
				autoHeight:true,
	    	 	//height:626,
	    	 	autoWidth:true, 
				stripeRows:true, //斑马线效果          
	    	 	columnLines : true,
	    	 	forceFit: true,
	    	 	collapsible : false,
		    	columns:[
		    		new Ext.grid.RowNumberer({text:"序号",width:70,align:"center"}),
		    		{header : '所属机构', dataIndex : 'userOrganName',sortable:false, menuDisabled:true,width:180,titleAlign:"center",
						renderer:function(value, metadata, record){ 
						    metadata.style="white-space: normal !important;";
						    return value; 
						}
					},{header:"姓名",dataIndex:"employeeName",sortable:false, menuDisabled:true,width:80,titleAlign:"center",
		    		 	renderer:function(value, metadata, record){
			    			metadata.style="white-space: normal !important;";
			    			return value;
		    			}
		    		},{header:"是否回执",dataIndex:"msgHzFlag",sortable:false, menuDisabled:true,width:80,align:"center",
		    		 	renderer:function(value, metadata, record){
			    			metadata.style="white-space: normal !important;";
			    			return value=='T'?'<font color=green>已回执</font>':'<font color=red>未回执</font>';
		    			}
		    		}
		    		]
	    	});
			me.msgHzListGridStore.on("load",function(){
				var hzNum = 0;
				var unhzNum = 0;
				for(var i=0;i<me.msgHzListGridStore.getCount();i++){
					var recode = me.msgHzListGridStore.getAt(i);
					var  msgHzFlag = recode.get('msgHzFlag');
					if(msgHzFlag == 'T'){
						hzNum++;
					}else{
						unhzNum++;
					}
				}
				me.msgHzformWin.setTitle('人员回执情况 - 已回执：'+hzNum+' 未回执：'+unhzNum);
				me.msgHzMask.hide();
				me.msgHzformWin.show();
				
			})
			me.msgHzListGridStore.load();
			//表单窗口
			me.msgHzformWin = new WindowObject({
				layout:'fit',
				title:'查看消息回执',
				height:600,
				width:800,
				border:false,
				frame:false,
				modal:true,
				//autoScroll:true,
				bodyStyle:'overflow-x:auto;overflow-y:hidden;',
				closeAction:'hide',
				items:[me.msgHzListGrid]
			});
	}
});