/**
 * 试题
 */
ClassDefine('modules.hnjtamis.exampaper.Exampaper', {
	extend : 'base.model.List',
	requires:['modules.hnjtamis.base.exammarkpeopleinfo.MySelect'],
	initComponent : function() {
		String.prototype.trim = function()
		{
			return this.replace( /(^\s*)|(\s*$)/g, '' ) ;
		};

		var me = this;
		me.cfg = me.cfg || 'base';
		this.columns = [
		    {name:'examId',width:0},
		    {name:'pid',width:0},
		    {name : 'stCreateNow',width:0},
		    {name:'isExam',width:0},
			{header:'考试名称',name:'examName',width:10,sortable:false,menuDisabled:true,
				renderer:function(value){
					return "<a href='javascript:this.showExamTestpaper()'>"+value+"</a>";
				},titleAlign:"center"
			},
			{header:'开始时间',name:'examStartTime',width:3,sortable:false,menuDisabled:true,type : 'date', format:'Y-m-d H:i:s',align:"center"},
			{header:'结束时间',name:'examEndTime',width:3,sortable:false,menuDisabled:true,type : 'date', format:'Y-m-d H:i:s',align:"center"},		
			{header:'存在手工阅卷',name:'haveUserReviewExam',sortable:false,menuDisabled:true,width:2,align:"center",hidden:me.cfg == 'base'},
			{header:'已配置阅卷人',name:'haveYjr',sortable:false,menuDisabled:true,width:2,align:"center",hidden:me.cfg == 'base',
				renderer:function(value){
					switch(value){
						case '已配置' : return "<font color='blue'>已配置</font>";
						case '未配置' : return "<font color='red'>未配置</font>";
						default : return value;
					}
				}
			},
			{header:'审核状态',name:'state',sortable:false,menuDisabled:true,width:2,
				renderer:function(value, metadata, record){// 5:草稿，10：等待审核，15：审核通过，20：审核打回
					var stCreateNow = record.get("stCreateNow");
						if(stCreateNow && stCreateNow == 'T'){
							return '<font color="red">处理中</font>';
						}else{
							switch(value){
								case '5' :  return "<font color='red'>草稿</font>";
								case '10' : return "<font color='red'>等待审核</font>";
								case '15' : return "<font color='blue'>已安排考试</font>";
								case '20' : return "<font color='red'>审核打回</font>";
								case '30' : return "<font color='green'>成绩发布</font>";
								case '-1' : return "<font color='red'>考试作废</font>";
								default : return "";
							}
						}
				},align:"center"
			},
 			{header:'创建时间',name:'creationDate',width:2,sortable:false,menuDisabled:true,type : 'date', format:'Y-m-d',align:"center"}
		]; 
		showExamTestpaper =  function(){
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
			var queryTerm = {};
			if(me.termForm)
				queryTerm = me.termForm.getValues(false);
			me.openFormWin(id, function() {},true,record.data,queryTerm,'view');
		};
		this.showTermSize = 1;//设定查询条件出现的个数
		this.terms = [{
				xtype : 'textfield',
				name : 'examNameTerm',
				fieldLabel : '考试名称',
				labelWidth : 115,
				width:280
			},
			{
				xtype : 'textfield',
				name : 'testpaperNameTerm',
				fieldLabel : '试卷名称',
				labelWidth : 115,
				width:280
			},{
				fieldLabel:"开始时间(≥)",
				name:'examStartTimeMaxTerm',
				xtype: 'datefield',
				format : 'Y-m-d',
				maxLength:32,
				labelWidth : 115,
				width:280
			},{
				fieldLabel:"开始时间(＜)",
				name:'examStartTimeMinTerm',
				xtype: 'datefield',
				format : 'Y-m-d',
				maxLength:32,
				labelWidth : 115,
				width:280
			},{
				fieldLabel:"创建时间(≥)",
				name:'creationDateMaxTerm',
				xtype: 'datefield',
				format : 'Y-m-d',
				maxLength:32,
				labelWidth : 115,
				width:280
			},{
				fieldLabel:"创建时间(＜)",
				name:'creationDateMinTerm',
				xtype: 'datefield',
				format : 'Y-m-d',
				maxLength:32,
				labelWidth : 115,
				width:280
			},{	
				fieldLabel:"考试性质",
				name:'examTypeIdTerm',
				xtype : 'select',
				selectUrl:'exam/exampaper/getDicKslxAndNullForExampaperListAction!getDicKslxAndNull.action',
				valueField:'dataKey',
				displayField:'dataName',
				jsonParemeterName:'examKslxList',
				labelWidth : 115,
				width:280,
				allowBlank : false
			}];
		this.keyColumnName = "examId";// 主健属性名
		this.viewOperater = true;
		this.addOperater = true;
		this.updateOperater = false;
		this.deleteOperater = false;
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
								var examStartTime = record.get("examStartTime");
								//状态 5:保存10:上报15:发布20:打回
								if(state==null || state=='' || state === '5' || state === '20' ){//|| state === '15'
									if(me.termForm)
								  		queryTerm = me.termForm.getValues(false);
									me.openFormWin(id, function() {
											me.termQueryFun();
										},false,record.data,queryTerm,'update');
								}else if(state=='-1'){
									Ext.Msg.alert('提示', '该考试安排已作废，不能修改！');
								}else if(examStartTime.getTime()>(new Date()).getTime()){
									if(me.termForm)
								  		queryTerm = me.termForm.getValues(false);
									me.openFormWin(id, function() {
											me.termQueryFun();
										},false,record.data,queryTerm,'updateLess');
								}else{
									Ext.Msg.alert('提示', '考试安排已发布，不能修改！');
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
								var state = record.get("state")
								//状态 5:保存10:上报15:发布20:打回
								if(state==null || state=='' || state === '5' || state === '20'){// || state === '15'
									var confirmFn = function(btn) {
									if (btn == 'yes')
										me.deleteData(id);
									};
									Ext.MessageBox.confirm('询问', '你真要删除这些数据吗？',
											confirmFn);
								}else if(state=='-1'){
									Ext.Msg.alert('提示', '该考试安排已作废，不能删除！');
								}else{
									Ext.Msg.alert('提示', '上报或发布状态，不能删除！');
								}
								
							}
						}));
		this.otherOperaters.push(Ext.create("Ext.Button", {
							icon : 'resources/icons/fam/user_delete.gif',//按钮图标
							text : '作废',
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
								var state = record.get("state")
								//状态 5:保存10:上报15:发布20:打回
								if(state!=-1){
									var confirmFn = function(btn) {
									if (btn == 'yes')
										me.cancelledData(id);
									};
									Ext.MessageBox.confirm('询问', '你真要作废这场考试吗？',
											confirmFn);
								}else{
									Ext.Msg.alert('提示', '考试已经作废！');
								}
								
							}
						}));
		/*this.otherOperaters.push({
				xtype : 'button',//按钮类型
				icon : 'resources/icons/fam/user_comment.png',//按钮图标
				text : "审核",//按钮标题
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
								var state = record.get("state");
								//状态 5:保存10:上报15:发布20:打回
								if(state === '10'){
									var queryTerm = {};
									if(me.termForm)
								  	queryTerm = me.termForm.getValues(false);
									me.openFormWin(id, function() {
											me.termQueryFun();
										},false,record.data,queryTerm,'audit');
								}else{
									Ext.Msg.alert('提示', '非上报状态，不能进行审核！');
								}
				}
			
		});*/
		if(me.cfg == 'base'){
			this.otherOperaters.push({
			    icon : 'resources/icons/fam/user_edit.png',//按钮图标
				text : "阅卷人",//按钮标题
				xtype : 'button',//按钮类型
				handler : function() {//按钮事件
					var selected = me.getSelectionModel().selected;
					if (selected.getCount() == 0) {
						Ext.Msg.alert('提示', '请选择要维护的考试名称记录！');
						return;
					}
					var id = "";
					var record = null;
					for (var i = 0; i < selected.getCount(); i++) {
						record = selected.get(i);
						id = record.get(me.keyColumnName);
					}
					var state = record.get("state")
					//状态 5:保存10:上报15:发布20:打回
					if(state==null || state=='' || state === '5' || state === '10' || state === '15' || state === '20'){
						var name = record.get("examName")
						me.showExamReviewer(id,name);
					}else if(state=='-1'){
						Ext.Msg.alert('提示', '该考试安排已作废，不能进行设置阅卷人！');
					}else{
						Ext.Msg.alert('提示', '该考试安排已发布，不能进行设置阅卷人！');
					}
				}
		    });
			
			this.otherOperaters.push({  
			    icon : 'resources/icons/fam/user_add.gif',//按钮图标
				text : "考生信息",//按钮标题
				xtype : 'button',//按钮类型
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
						var state = record.get("state")
						var isExam = record.get("isExam");
						var stCreateNow = record.get("stCreateNow");
						if(isExam!=10){
							Ext.Msg.alert('提示', '请选择科目！');
							return;
						}
						//状态 5:保存10:上报15:发布20:打回
						if(stCreateNow == 'T'){
							Ext.Msg.alert('提示', '该考试安排正在初始化，不能设置考生信息！');
						}else if(state==null || state=='' || state === '5' || state === '10' || state === '15' || state === '20'){
							me.getExamineeForm(id,state,me);
						}else if(state=='-1'){
							Ext.Msg.alert('提示', '该考试安排已作废，不能设置考生信息！');
						}else{
							Ext.Msg.alert('提示', '该考试安排已发布，不能设置考生信息！');
						}
					}
			});
		}else{
			me.optBtGroup = Ext.create("Ext.Button", {
			    text: "其它操作", 
			    icon : 'resources/icons/fam/cog_edit.png',//按钮图标
			    menu: 
			    { 
			        items: [ 
			            { 
			                icon : 'resources/icons/fam/organ.gif',//按钮图标
							text : "设置考点",//按钮标题
							handler : function() {//按钮事件
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
								var state = record.get("state")
								//状态 5:保存10:上报15:发布20:打回
								if(state==null || state=='' || state === '5' || state === '10' || state === '15' || state === '20'){
									me.showExamRoot(id,state);
								}else if(state=='-1'){
									Ext.Msg.alert('提示', '该考试安排已作废，不能进行修改！');
								}else{
									Ext.Msg.alert('提示', '该考试安排已发布，不能进行修改！');
								}
							}
			            } ,{ 
			                icon : 'resources/icons/fam/user_edit.png',//按钮图标
							text : "阅卷人",//按钮标题
							handler : function() {//按钮事件
								var selected = me.getSelectionModel().selected;
								if (selected.getCount() == 0) {
									Ext.Msg.alert('提示', '请选择要维护的考试名称记录！');
									return;
								}
								var id = "";
								var record = null;
								for (var i = 0; i < selected.getCount(); i++) {
									record = selected.get(i);
									id = record.get(me.keyColumnName);
								}
								var state = record.get("state")
								//状态 5:保存10:上报15:发布20:打回
								if(state==null || state=='' || state === '5' || state === '10' || state === '15' || state === '20'){
									var name = record.get("examName")
									me.showExamReviewer(id,name);
								}else if(state=='-1'){
									Ext.Msg.alert('提示', '该考试安排已作废，不能进行设置阅卷人！');
								}else{
									Ext.Msg.alert('提示', '该考试安排已发布，不能进行设置阅卷人！');
								}
							}
			            }, {  
			                icon : 'resources/icons/fam/user_add.gif',//按钮图标
							text : "考生信息",//按钮标题
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
								var state = record.get("state")
								var isExam = record.get("isExam");
								if(isExam!=10){
									Ext.Msg.alert('提示', '请选择科目！');
									return;
								}
								//状态 5:保存10:上报15:发布20:打回
								if(state==null || state=='' || state === '5' || state === '10' || state === '15' || state === '20'){
									me.getExamineeForm(id,state,me);
								}else if(state=='-1'){
									Ext.Msg.alert('提示', '该考试安排已作废，不能进行修改考生信息！');
								}else{
									Ext.Msg.alert('提示', '该考试安排已发布，不能进行修改考生信息！');
								}
							}
			            }, {
			            	icon : 'resources/icons/fam/connect.gif',//按钮图标
							text : "最近几日考试加载",//按钮标题
							handler : function() {//按钮事件
									var examMaskTip = EapMaskTip(document.body);
									examMaskTip.show();
									if(me.requestHttpUrl == undefined || me.requestHttpUrl == null){
										EapAjax.request({
											timeout: 60000,
											url : 'exam/exampaper/getRequestHttpForExampaperListAction!getRequestHttp.action',
											async : false,
											success : function(response) {
												me.requestHttpUrl = Ext.decode(response.responseText).requestHttpUrl;
											}
										});
									}	
												
									Ext.Ajax.request({
											method : 'POST',
											url : "exam/exampaper/queryExeExamUserListForExampaperListAction!queryExeExamUserList.action",
											timeout: 120000,
											async : false,
											success : function(response) {
												var result = Ext.decode(response.responseText);
												var examUserList = result.examUserList;
												
												if(examUserList!=undefined && examUserList!=null && examUserList.length>0){
													var succ=[examUserList.length,0,0];
													for(var i=0;i<examUserList.length;i++){
														  var examId = examUserList[i].examId;
														  var inticket = examUserList[i].inticket;
														  Ext.data.JsonP.request({
															 timeout: 60000,
															 url : me.requestHttpUrl + "/getuser",
															 params : {
															 	examid : examId,
															 	inticket : inticket
															 },
															 async : false,
															 callbackKey: "jsonpcallback",
															 success: function(result) {
																if(result!=undefined && result!=null && result.code == '00001'){
																	succ[1]=succ[1]+1;
																}else{
																	succ[2]=succ[2]+1;
																}
																if(succ[0]==(succ[1]+succ[2])){
																	Ext.Msg.alert('提示', '成功加载'+succ[1]+'个考生，失败加载'+succ[2]+'个考生！');
																	examMaskTip.hide();
																}
															 },
															 failure : function() {
																 succ[2]=succ[2]+1;
																 if(succ[0]==(succ[1]+succ[2])){
																	Ext.Msg.alert('提示', '成功加载'+succ[1]+'个考生，失败加载'+succ[2]+'个考生！');
																	examMaskTip.hide();
																}
															 }
														   });
													}
												}else{
													Ext.Msg.alert('提示', '没有找到需要加载的考生信息，请确定今明两天是否存在考试，且已设定了考生！');
													examMaskTip.hide();
												}
											},
											failure : function(response) {
												examMaskTip.hide();
												Ext.Msg.alert('信息', '后台未响应，网络异常！');
											}
									});
							}
			            } , {
			            	icon : 'resources/icons/fam/save.gif',//按钮图标
							text : "考后答案预处理",//按钮标题
							handler : function() {//按钮事件
								//var state = me.selectNode.state;
								//状态 5:保存10:上报15:发布20:打回
								//if(state==null || state=='' || state === '5' || state === '10' || state === '15' || state === '20'){
									var examMaskTip = EapMaskTip(document.body);
									examMaskTip.show();
									Ext.Ajax.request({
											method : 'POST',
											url : "exam/exampaper/saveAnsFileToDbForExampaperFormAction!saveAnsFileToDb.action",
											timeout: 120000,
											success : function(response) {
												examMaskTip.hide();
												var result = Ext.decode(response.responseText);
												if(result.success==true)
													Ext.Msg.alert('信息', result.msg, function(btn) {
														
													});
												else
												   Ext.Msg.alert('错误提示', result[0].errors);
											},
											failure : function(response) {
												examMaskTip.hide();
												var result = Ext.decode(response.responseText);
												if (result && result.length > 0)
													Ext.Msg.alert('错误提示', result[0].errors);
												else
													Ext.Msg.alert('信息', '后台未响应，网络异常！');
											}
									});
								//}else{
									//Ext.Msg.alert('提示', '该考试安排已发布，不能进行修改！');
								//}
							}
			            }
			        ] 
			    }
			});
			
			this.otherOperaters.push(me.optBtGroup);
		}
		/*this.otherOperaters.push({
				xtype : 'button',//按钮类型
				icon : 'resources/icons/fam/user_add.gif',//按钮图标
				text : "管理考生",//按钮标题
				handler : function() {//按钮事件
					var selected = me.getSelectionModel().selected;
					if (selected.getCount() == 0) {
						Ext.Msg.alert('提示', '请选择记录！');
						return;
					}
					var id = me.selectNode.id;
					me.getExamineeForm(id);
				}
			
		});
		this.otherOperaters.push({
			xtype : 'button',//按钮类型
			icon : 'resources/icons/fam/user_add.gif',//按钮图标
			text : "阅卷人",//按钮标题
			handler : function() {//按钮事件
				var selected = me.getSelectionModel().selected;
				if (selected.getCount() == 0) {
					Ext.Msg.alert('提示', '请选择要维护的考试名称记录！');
					return;
				}
				var id = me.selectNode.id;
				var record = selected.get(0);
				var name = record.get('examName');
				me.showExamReviewer(id,name);
			}
		
		});
		this.otherOperaters.push({
				xtype : 'button',//按钮类型
				icon : 'resources/icons/fam/organ.gif',//按钮图标
				text : "设置考点",//按钮标题
				handler : function() {//按钮事件
					if (me.selectNode==null) {
						Ext.Msg.alert('提示', '请单击记录！');
						return;
					}
					var id = me.selectNode.id;
					var record = me.selectNode;
					var state = me.selectNode.state;
					//状态 5:保存10:上报15:发布20:打回
					if(state==null || state=='' || state === '5' || state === '20'){
						me.showExamRoot(id,state);
					}else{
						Ext.Msg.alert('提示', '非保存或打回状态，不能进行修改！');
					}
				}
			
		});*/
		
		/*this.otherOperaters.push({
				xtype : 'button',//按钮类型
				icon : 'resources/icons/fam/folder_wrench.png',//按钮图标
				text : "试卷初始化",//按钮标题
				handler : function() {//按钮事件
					//Ext.Msg.alert('提示', '建设中！');
					var selected = me.getSelectionModel().selected;
					if (selected.getCount() == 0) {
						Ext.Msg.alert('提示', '请选择记录！');
						return;
					}
					var id = me.selectNode.id;
					var record = selected.get(0);
					EapAjax.request({
						method : 'POST',
						url : 'exam/exampaper/saveExamTestPaperForExampaperFormAction!saveExamTestPaper.action?id='+id,
						async : true,
						success : function(response) {
							var result = Ext.decode(response.responseText);
							if (Ext.isArray(result)&&result[0].errors) {
								var msg = result[0].errors;
								Ext.Msg.alert('错误', msg);
							} else {
								Ext.Msg.alert('信息', '操作成功！');
								//me.termQueryFun(false,'flash');
							}
						},
						failure : function() {
							Ext.Msg.alert('信息', '后台未响应，网络异常！');
						}
					});
				}
			
		});
		this.otherOperaters.push({
				xtype : 'button',//按钮类型
				icon : 'resources/icons/fam/save.gif',//按钮图标
				text : "考后答案预处理",//按钮标题
				handler : function() {//按钮事件
					Ext.Ajax.request({
							method : 'POST',
							url : "exam/exampaper/saveAnsFileToDbForExampaperFormAction!saveAnsFileToDb.action",
							success : function(response) {
								var result = Ext.decode(response.responseText);
								if(result.success==true)
									Ext.Msg.alert('信息', result.msg, function(btn) {
										
									});
								else
								   Ext.Msg.alert('错误提示', result[0].errors);
							},
							failure : function(response) {
								var result = Ext.decode(response.responseText);
								if (result && result.length > 0)
									Ext.Msg.alert('错误提示', result[0].errors);
								else
									Ext.Msg.alert('信息', '后台未响应，网络异常！');
							}
					});
				}
			
		});*/
		this.otherOperaters.push({
				xtype : 'button',//按钮类型
			    text: "考试成绩", 
			    icon : 'resources/icons/fam/cog_edit.png',//按钮图标
			    menu: 
			    { 
			        items: [{
				        		icon : 'resources/icons/fam/grid.png',//按钮图标
								text : "成绩预览",//按钮标题
								handler : function() {//按钮事件
									var selected = me.getSelectionModel().selected;
									if (selected.getCount() == 0) {
										Ext.Msg.alert('提示', '请选择记录！');
										return;
									}
									var id = "";
									var record = null;
									var examName = "";
									var examId = "";
									for (var i = 0; i < selected.getCount(); i++) {
										record = selected.get(i);
										id = record.get("pid");
										examId = record.get("examId");
										examName = record.get("examName");
									}
									var state = record.get("state");
									var examEndTime = record.get("examEndTime");
									//if(examEndTime.getTime()>(new Date()).getTime()){
										//Ext.Msg.alert('提示', '考试还未结束，不能发布！');
									//}else{
									 	me.examScoreView = ClassCreate('modules.hnjtamis.exampaper.ExamScoreView',{examId:examId,examName:examName});
										
										//表单窗口
										var examScoreWin = new WindowObject({
											layout:'fit',
											title:'成绩预览',
											height:600,
											width:1000,
											border:false,
											frame:false,
											modal:true,
											//autoScroll:true,
											bodyStyle:'overflow-x:auto;overflow-y:hidden;',
											closeAction:'hide',
											items:[me.examScoreView]
										});
										examScoreWin.show();
									//}
								}
			        		},{
								//xtype : 'button',//按钮类型
								icon : 'resources/icons/fam/grid.png',//按钮图标
								text : "成绩发布",//按钮标题
								handler : function() {//按钮事件
									//Ext.Msg.alert('提示', '建设中！');
									//var ids = ;
									var selected = me.getSelectionModel().selected;
									if (selected.getCount() == 0) {
										Ext.Msg.alert('提示', '请选择记录！');
										return;
									}
									var id = "";
									var record = null;
									for (var i = 0; i < selected.getCount(); i++) {
										record = selected.get(i);
										id = record.get("pid");
									}
									var state = record.get("state");
									var examEndTime = record.get("examEndTime");
									if(examEndTime.getTime()>(new Date()).getTime()){
										Ext.Msg.alert('提示', '考试还未结束，不能发布！');
									}else if(state === '15'){
										var examMaskTip = EapMaskTip(document.body);
										examMaskTip.show();
										Ext.Ajax.request({
											method : 'POST',
											url : "exam/exampaper/publicExamForExampaperListAction!publicExam.action",
											timeout: 60000,
											success : function(response) {
												examMaskTip.hide();
												var result = Ext.decode(response.responseText);
												var selected = me.getSelectionModel().selected;
												if(result.success==true)
												Ext.Msg.alert('信息', result.msg, function(btn) {
													me.termQueryFun()
												});
												else
												   Ext.Msg.alert('错误提示', result[0].errors);
											},
											failure : function(response) {
												examMaskTip.hide();
												var result = Ext.decode(response.responseText);
												if (result && result.length > 0)
													Ext.Msg.alert('错误提示', result[0].errors);
												else
													Ext.Msg.alert('信息', '后台未响应，网络异常！');
											},
											params : "id=" + id
										});
									}else if(state === '30'){
										Ext.Msg.alert('提示', '该考试已发布！');
									}else{
										Ext.Msg.alert('提示', '请确认已经审核通过，并进行了考试以及阅卷！');
									}
								}
						  }
					]
			    }
		});
		
		
		/*this.otherOperaters.push({
				xtype : 'button',//按钮类型
				icon : 'resources/icons/fam/save.gif',//按钮图标
				text : "模拟测试",//按钮标题
				handler : function() {//按钮事件
					Ext.Ajax.request({
							method : 'POST',
							url : "exam/exampaper/saveMoniExamForExampaperFormAction!saveMoniExam.action",
							success : function(response) {
								var result = Ext.decode(response.responseText);
								if(result.success==true)
									Ext.Msg.alert('信息', result.msg, function(btn) {
										
									});
								else
								   Ext.Msg.alert('错误提示', result[0].errors);
							},
							failure : function(response) {
								var result = Ext.decode(response.responseText);
								if (result && result.length > 0)
									Ext.Msg.alert('错误提示', result[0].errors);
								else
									Ext.Msg.alert('信息', '后台未响应，网络异常！');
							}
					});
				}
			
		});*/
		this.jsonParemeterName = 'examList';
		//this.readerRoot = 'examList';
		this.listUrl = "exam/exampaper/listForExampaperListAction!list.action?op="+me.cfg;// 列表请求地址
		this.deleteUrl = "exam/exampaper/deleteForExampaperListAction!delete.action";// 删除请求地址
		//this.childColumnName = 'examList';// 子集合的属性名

		//打开表单
		this.openFormWin = function(id,callback,readOnly,data,term,oper){
			callback = function (){me.termQueryFun();}
			me.listType = '';
			var formConfig = {};
			var readOnly = readOnly || false;
			formConfig.readOnly = readOnly;
			formConfig.fileUpload = true;
			formConfig.clearButtonEnabled = false;
			formConfig.saveButtonEnabled = false;
			formConfig.formUrl = "exam/exampaper/saveForExampaperFormAction!save.action?op="+oper;//保存
			formConfig.findUrl = "exam/exampaper/findForExampaperFormAction!find.action";
			formConfig.callback = callback;
			formConfig.columnSize = 2;
			formConfig.jsonParemeterName = 'form';
			formConfig.items = new Array();
			formConfig.oper = oper;// 复制操作类型变量
			formConfig.items.push({
				xtype:'hidden',
				name:'examArrangeId'
			});
			formConfig.items.push({
				xtype:'hidden',
				name:'state'
			});
			formConfig.items.push({
				xtype:'hidden',
				name:'isUse'
			});
			me.InitFlag = false;
			formConfig.items.push({
				colspan:1,
				/*xtype : 'select',
				name:'publicId',
				fieldLabel:"信息发布名称",
				
				valueField:'publicId',
				displayField:'examTitle',
				jsonParemeterName:'examPublicList',
				labelWidth : 142,
				width:350,
				readOnly : readOnly,
				allowBlank : false,
				
				*/
				fieldLabel:"信息发布名称",
				labelWidth : 142,
				width:350,
				name:'publicId',
				checked : false,
				allowBlank : false,
				addPickerWidth:100,
				xtype : 'selecttree',
				nameKey : 'testpaperId',
				nameLable : 'testpaperName',
				readerRoot : 'children',
				keyColumnName : 'id',
				titleColumnName : 'title',
				childColumnName : 'children',
				selectType:'examPublic',
				selectTypeName:'考试安排',
				readOnly : oper=='updateLess'?true:readOnly,
				editorType:'str',//编辑类型为字符串，不是对象
				selectUrl:'exam/exampaper/queryExamPublicTreeForExampaperListAction!queryExamPublicTree.action?op=create',
				selectEventFun:function(combo){
					if(me.InitFlag){
						me.setExamPropertyText(oper);
					}
				}
			});
			
			var examArrangeNameVlFlag = false;
			formConfig.items.push({
				colspan:1,
				fieldLabel:"考试名称",
				name:'examArrangeName',
				xtype:'textfield',
				allowBlank:false,
				readOnly : oper=='updateLess'?true:readOnly,
				maxLength : 30,
				width:330,
				labelWidth : 142,
				listeners:{  
					'change':function(){
						/*var examCode = me.form.getForm().findField("examCode").getValue();
						if(me.InitFlag && (oper == 'add' || examCode=='' || examCode=='null')){
							var examArrangeName = me.form.getForm().findField("examArrangeName").getValue();
							Ext.Ajax.request({
									url : 'exam/exampaper/getExamCodePyForExampaperListAction!getExamCodePy.action',
									method : 'post',
									timeout: 60000,
									params : {
									    examArrangeName : examArrangeName
									},
									success:function(response){
										var re = Ext.decode(response.responseText);
										me.form.getForm().findField("examCode").setValue(re.examArrangeCode)		
									},
									failure:function(){
										Ext.Msg.alert("信息","未能与服务器取得通讯");
									}
							});
						}*/
					 }  
				},
				validator : function(thisText){
							if(oper=='updateLess'){
								me.IsExsit = true;
							}else if(!!thisText && examArrangeNameVlFlag){ //不为空
									me.code_examArrangeId = me.form.getForm().findField('examArrangeId').getValue();
									if(me.code_examArrangeId ==null || me.code_examArrangeId==""  || me.code_examArrangeId=="null"){
										me.code_examArrangeId = "11111111111111111111111111111111111";
									}
									Ext.Ajax.request({
										url : 'exam/exampaper/querylistInExamArrangeNameForExampaperListAction!querylistInExamArrangeName.action',
										method : 'post',
										timeout: 30000,
										async : true,//是否异步
										params : {
											examArrangeName : thisText,
												id	  : me.code_examArrangeId
										},
										success:function(response){
											var re = Ext.decode(response.responseText);
											//console.log(re);
											if(re['isExist']=='0'){
												ReturnValue(true);
											}else{
												ReturnValue('此考试名称已存在');
												//me.form.getForm().findField('examTitle').markInvalid('此名称已存在');
											}
										},
										failure:function(){
											Ext.Msg.alert("信息","未能与服务器取得通讯");
										}
									});
							}
							if(me.IsExsitFlag){
								me.IsExsitFlag = false;
								me.IsExsit = true;
							}
							function ReturnValue(ok){
							   me.IsExsit = ok;
							}
							return me.IsExsit;
				}	
			});
			
			formConfig.items.push({
				colspan:1,
				fieldLabel:"是否达标考试",
				xtype:'displayfield',
				name:'examPropertyText',
				readOnly : true,
				labelWidth : 142
			});
			
			formConfig.items.push({
				xtype:'hidden',
				name:'examProperty'
			});
			
			formConfig.items.push({
				colspan:1,
				fieldLabel:"考试类型(性质)",
				xtype:'displayfield',
				name:'examTypeIdText',
				readOnly : true,
				labelWidth : 142
			});
			
			formConfig.items.push({
				xtype:'hidden',
				name:'examTypeId'
			});
			/*formConfig.items.push({
				colspan:1,
				xtype : 'select',
				name:'examTypeId',
				fieldLabel:"考试类型",
				selectUrl:'exam/exampaper/getDicKslxForExampaperListAction!getDicKslx.action',
				valueField:'dataKey',
				displayField:'dataName',
				jsonParemeterName:'examKslxList',
				labelWidth : 100,
				width:300,
				readOnly : readOnly,
				allowBlank : false
			});*/
			
			
			//me.IsExsit=false;
			//me.IsExsitFlag=true;
			
			formConfig.items.push({
				xtype:'hidden',
				name:'examCode'
			});
			/*formConfig.items.push({
				colspan:1,
				fieldLabel:"考试编码",
				name:'examCode',
				xtype:'textfield',
				allowBlank:false,
				readOnly : true,
				maxLength : 50,
				labelWidth : 100,
				width:300,
				validator : function(thisText){
							if(!!thisText){ //不为空
									me.code_examArrangeId = me.form.getForm().findField('examArrangeId').getValue();
									if(me.code_examArrangeId ==null || me.code_examArrangeId==""  || me.code_examArrangeId=="null"){
										me.code_examArrangeId = "11111111111111111111111111111111111";
									}
									Ext.Ajax.request({
										url : 'exam/exampaper/querylistInExamCodeForExampaperListAction!querylistInExamCode.action',
										method : 'post',
										timeout: 30000,
										params : {
											examCode : thisText,
												id	  : me.code_examArrangeId
										},
										success:function(response){
											var re = Ext.decode(response.responseText);
											//console.log(re);
											if(re['isExist']=='0'){
												ReturnValue(true);
											}else{
												ReturnValue('此考试编码已存在');
												//me.form.getForm().findField('examTitle').markInvalid('此名称已存在');
											}
										},
										failure:function(){
											Ext.Msg.alert("信息","未能与服务器取得通讯");
										}
									});
							}
							if(me.IsExsitFlag){
								me.IsExsitFlag = false;
								me.IsExsit = true;
							}
							function ReturnValue(ok){
							   me.IsExsit = ok;
							}
							return me.IsExsit;
				}
			});*/
			//,[30,'培训测试'],[40,'模拟考试']
			/*formConfig.items.push({
				colspan : 1,
				fieldLabel:"考试类型",
				xtype : 'select',
				allowBlank : false,
				readOnly : readOnly,
				labelWidth : 100,
				width:300,
				name:'examProperty',
				data:[[10,'达标考试'],[20,'竞赛考试']],
				defaultValue : 10
			});*/
			
			
			
			
			/*formConfig.items.push({
				fieldLabel:"是否发布成绩",
				xtype:'radiogroup',
				allowBlank:false,
				readOnly : readOnly,
				maxLength : 50,
				labelWidth : 100,
				width:200,
				columns:2,
				items:[{boxLabel:'否',name:'isPublic',inputValue:'5',readOnly : readOnly},{boxLabel:'是',name:'isPublic',inputValue:'10',readOnly : readOnly}]
			});*/
			formConfig.items.push({
				xtype:'hidden',
				name:'isPublic',
				value : '5'
			});
			
			
			
			
			formConfig.items.push({
				xtype:'hidden',
				name : 'examId'
			});
			formConfig.items.push({
				name : 'examName',
				fieldLabel : '科目',
				xtype : 'hidden'
			});
		formConfig.items.push({
			colspan : 2,
			labelWidth : 142,
			width:713,
			name : 'testpaper',
			fieldLabel : '试卷',
			checked : false,
			allowBlank : false,
			//addPickerWidth:100,
			xtype : 'selecttree',
			nameKey : 'testpaperId',
			nameLable : 'testpaperName',
			readerRoot : 'children',
			keyColumnName : 'id',
			titleColumnName : 'title',
			childColumnName : 'children',
			selectType:'testpaper',
			selectTypeName:'试卷',
			selectUrl : "exam/testpaper/testExamPaperTreeListListForTestpaperListAction!testExamPaperTreeList.action",
			readOnly : oper=='updateLess'?true:readOnly
		});	
		formConfig.items.push({
				colspan : 1,
				labelWidth : 142,
				name : 'examStartTime',
				fieldLabel : '考试开始时间',
				xtype: 'datetimefield',
				format : 'Y-m-d H:i:s',
				allowBlank : false,
				width:330,
				readOnly : readOnly				
		});
		formConfig.items.push({
			colspan : 1,
			labelWidth : 142,
			name : 'examEndTime',
			fieldLabel : '考试结束时间',
			xtype: 'datetimefield',
			format : 'Y-m-d H:i:s',
			allowBlank : false,
			width:330,
			readOnly : readOnly		
		});
		formConfig.items.push({
				colspan : 1,
				labelWidth : 142,
				name : 'examPaperType',
				fieldLabel : '出题方式',
				xtype :'select',
				data:[[10,'同试卷打乱'],[20,'同试卷同顺序']],//,['30','按模版随机抽题']
				defaultValue:10,
				allowBlank : false,
				readOnly : readOnly
		 });
		 formConfig.items.push({
			colspan : 1,
			labelWidth : 142,
			name : 'passScore',
			fieldLabel : '及格分数线(%)',
			xtype:'numberfield',
			allowNegative:false, //不允许输入负数
			allowDecimals:false, //不允许输入小数  
			allowBlank : false,
			minValue: 0 ,   //最小值
			nanText:"请输入正数",
			maxLength : 5,
			readOnly : readOnly				
		});
		formConfig.items.push({
			colspan : 1,
			labelWidth : 142,
			name : 'beforeTime',
			fieldLabel : '提前进入时间(分)',
			xtype:'numberfield',
			allowNegative:false, //不允许输入负数
			allowDecimals:false,               //不允许输入小数  
			allowBlank : false,
			minValue: 0 ,   //最小值
			nanText:"请输入正整数",
			maxLength : 3,
			readOnly : readOnly			
		});
		formConfig.items.push({
			colspan : 1,
			labelWidth : 142,
			name : 'banTime',
			fieldLabel : '禁止进入时间(分)',
			xtype:'numberfield',
			allowNegative:false, //不允许输入负数
			allowDecimals:false,               //不允许输入小数   
			allowBlank : false,
			minValue: 0 ,   //最小值
			nanText:"请输入正整数",
			maxLength : 3,
			readOnly : readOnly			
		}); 
			formConfig.items.push({
				colspan : 1,
				fieldLabel : '达标有效期(≥)',
				labelWidth : 142,
				name : 'scoreStartTime',
				xtype: oper=='view'?'displayfield':'hidden'
			});
			formConfig.items.push({
				colspan : 1,
				fieldLabel : '达标有效期(＜)',
				labelWidth : 142,
				name : 'scoreEndTime',
				xtype: oper=='view'?'displayfield':'hidden',
				readOnly : true
			});
			formConfig.items.push({
				colspan : 2,
				fieldLabel : '说明',
				labelWidth : 142,
				xtype: 'displayfield',
				hidden:oper=='view'?false:true,
				value:'“达标有效期”为达标考试合格后，考生成绩的有效期，由“信息发布”模块中指定，如不是达标考试，则不存在有效期。'
			});	
		formConfig.items.push({
				colspan:2,
				fieldLabel:"备注",
				name:'remark',
				xtype:'textfield',
				readOnly : readOnly,
				maxLength : 250,
				labelWidth : 142,
				width:713
			});
			//formConfig.items.push(me.setExamForm(readOnly));
			
			
			formConfig.otherOperaters = [];//其它扩展按钮操作
			/*formConfig.otherOperaters.push({
					boxLabel:'保存&自动审核 ',
	                fieldLabel:' ',
	                labelSeparator :'',
	                name:'publicChk',
	                inputValue : true,
	                checked : true,
	                xtype : 'checkbox'
			});*/
			if(oper == 'audit'){
				/*formConfig.otherOperaters.push({
					xtype : 'button',//按钮类型
					icon : 'resources/icons/fam/user_comment.png',//按钮图标
					text : "审核通过",//按钮标题
					timeout: 60000,
					handler : function() {//按钮事件
						//Ext.Msg.alert('提示', '建设中！');
						//状态 5:保存10:上报15:发布20:打回
						me.form.getForm().findField("state").setValue("15");
						me.form.submit();
					}
				});
				formConfig.otherOperaters.push({
					xtype : 'button',//按钮类型
					icon : 'resources/icons/fam/folder_go.png',//按钮图标
					text : "审核打回",//按钮标题
					timeout: 60000,
					handler : function() {//按钮事件
						//Ext.Msg.alert('提示', '建设中！');
						//状态 5:保存10:上报15:发布20:打回
						me.form.getForm().findField("state").setValue("20");
						me.form.submit();
					}
				});*/
			}else{
				formConfig.otherOperaters.push({
					text : eap_operate_save,
					iconCls : 'save',
					tabIndex:900,
					timeout: 6000000,
					hidden:oper=='updateLess'?true:false,
					handler : function() {
						if (me.form.getForm().isValid()&&
						   (!me.form.validate||me.form.validate(me.form.getForm().getValues(false)))){
						   	//if(me.form.getForm().findField("publicChk").getValue() == true){
								//me.form.getForm().findField("state").setValue("15");
							//}else{
								me.form.getForm().findField("state").setValue("5");
							//}
							me.form.submit();
						}
					}
				});
				formConfig.otherOperaters.push({
					text : oper=='updateLess'?"重新发布":"发布",//按钮标题
					iconCls : 'save',
					tabIndex:900,
					timeout: 6000000,
					handler : function() {
						if (me.form.getForm().isValid()&&
						   (!me.form.validate||me.form.validate(me.form.getForm().getValues(false)))){
						   	//if(me.form.getForm().findField("publicChk").getValue() == true){
								me.form.getForm().findField("state").setValue("15");
							//}else{
								//me.form.getForm().findField("state").setValue("5");
							//}
							me.form.submit();
						}
					}
				});
				/*formConfig.otherOperaters.push({
					xtype : 'button',//按钮类型
					icon : 'resources/icons/fam/user_comment.png',//按钮图标
					text : "上报",//按钮标题
					timeout: 60000,
					handler : function() {//按钮事件
						//Ext.Msg.alert('提示', '建设中！');
						//状态 5:保存10:上报15:发布20:打回
						me.form.getForm().findField("state").setValue("10");
						me.form.submit();
					}
				});*/
			}
			/*if(oper != 'add'){
				formConfig.otherOperaters.push({
					xtype : 'button',//按钮类型
					icon : 'resources/icons/fam/organ.gif',//按钮图标
					text : "考生信息维护",//按钮标题
					timeout: 60000,
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
						var state = record.get("state")
						var isExam = record.get("isExam");
						if(isExam!=10){
							Ext.Msg.alert('提示', '请选择科目！');
							return;
						}
						//状态 5:保存10:上报15:发布20:打回
						if(state==null || state=='' || state === '5' || state === '10' || state === '15' || state === '20'){
							me.formWinUpdate.hide();
							me.getExamineeForm(id,state,me);
						}else{
							Ext.Msg.alert('提示', '该考试安排已发布，不能进行修改！');
						}
					}
				});
			}*/
			me.form = ClassCreate('base.model.Form',formConfig);
			me.form.parentWindow = this;
			
			//表单窗口
			me.formWinUpdate = new WindowObject({
				layout:'fit',
				title:'考试安排',
				//height:410,
				width:760,
				border:false,
				frame:false,
				modal:true,
				//autoScroll:true,
				bodyStyle:'overflow-x:auto;overflow-y:hidden;',
				closeAction:'hide',
				items:[me.form]
			});
			me.form.submit = function(){
					var meForm = me.form;
					if (me.yz() && meForm.getForm().isValid()&&
					   (!meForm.validate||meForm.validate(meForm.getForm().getValues(false)))){
						meForm.MaskTip.show();
						meForm.getForm().findField('json').setValue("");
						meForm.getForm().findField('json').setValue(Ext.encode(meForm.getForm().getValues(false)));
						var state = me.form.getForm().findField("state").getValue();
						meForm.getForm().submit({
							success : function(form, action) {
								var msg = action.result.msg;
								if (!msg)
									msg = '操作成功！';
							    meForm.MaskTip.hide();
							    if(state == '15'){//加载试卷到客户端
							        //me.jzUserExamClient(action.result.examIds);
							    }
							   
								Ext.Msg.alert('提示', msg,function(){
									meForm.close();
									//console.log(meForm.callback)
									if(meForm.callback) 
									   meForm.callback(Ext.encode(meForm.getForm().getValues(false)),action.result);
									//if(callback) callback(Ext.encode(meForm.getForm().getValues(false)),action.result);
								});
							},
							failure : function(form, action) {
								if (action.result && action.result.length > 0)
									Ext.Msg.alert('错误提示',
											action.result[0].errors);
								else
									Ext.Msg.alert('信息', '后台未响应，网络异常！');
							    meForm.MaskTip.hide();
							},
							timeout: 6000000
						});
					}
			}
		
			me.formWinUpdate.show();
			me.form.setFormData(id,function(result){
				me.InitFlag = true;
				examArrangeNameVlFlag = true;
				if(oper == 'add'){	
					//增加操作
					me.form.getForm().findField("state").setValue("5");
					//me.form.getForm().findField("examProperty").setValue("10");
					me.form.getForm().findField("isPublic").setValue("5");
					me.form.getForm().findField("isUse").setValue(10);//是否使用 5：否,10：是
					
					
					me.form.getForm().findField("banTime").setValue(999);
					me.form.getForm().findField("beforeTime").setValue(10);
					me.form.getForm().findField("passScore").setValue(60);
					me.form.getForm().findField("examPaperType").setValue(10);
				}else if(oper == 'update'){
					//更新操作
					/*var examProperty = me.form.getForm().findField("examProperty").getValue();
					if(examProperty == '10'){
						me.form.getForm().findField("scoreStartTime").show();
						me.form.getForm().findField("scoreStartTime").allowBlank = false;
						me.form.getForm().findField("scoreEndTime").show();
						me.form.getForm().findField("scoreEndTime").allowBlank = false;
					}else{
						me.form.getForm().findField("scoreStartTime").hide();
						me.form.getForm().findField("scoreStartTime").allowBlank = true;
						me.form.getForm().findField("scoreEndTime").hide();
						me.form.getForm().findField("scoreEndTime").allowBlank = true;
					}*/
				}
				me.form.getForm().findField("publicId").store.on("load",function(){
					me.oldpublicId = me.form.getForm().findField("publicId").getValue();
					if(me.oldpublicId==undefined || me.oldpublicId == null){
						me.oldpublicId = '';
					}
					me.setExamPropertyText(oper);
				});
				me.setExamPropertyText(oper);
			});
		};
		this.callParent();
		
		me.stCreateNowIds = "";
		me.examTaskRunner = new Ext.util.TaskRunner();  
		me.examTask = {
		    run: function(){
		    	if(me.stCreateNowIds!=""){
			        EapAjax.request({
						timeout: 10000,
						url : 'exam/exampaper/getExamStCreateNowForExampaperListAction!getExamStCreateNow.action',
						async : false,
						params : {id : me.stCreateNowIds},
						success : function(response) {
							var result = Ext.decode(response.responseText);
							if(result.scexers ==undefined || result.scexers==null || result.scexers==''){
								me.examTaskRunner.stop(me.examTask);
								result.scexers="";
							}else{
								me.stCreateNowIds = result.scexers;
							}
							for(var i=0;i<me.store.getCount();i++){
								var rs = me.store.getAt(i);
								var stCreateNow = rs.get("stCreateNow");
								if(stCreateNow=='T' && result.scexers.indexOf(rs.get("examId")) ==-1){
									rs.set("stCreateNow","F");
									me.getView().refreshNode(i); 
								}
							}
						},failure : function() {
							me.examTaskRunner.stop(me.examTask);
						}
					});
		    	}else{
		    		me.examTaskRunner.stop(me.examTask);
		    	}
		    },
		    interval: 30000
		}
		
		me.store.on("load",function(){
			me.stCreateNowIds = "";
			for(var i=0;i<me.store.getCount();i++){
				var rs = me.store.getAt(i);
				if(rs.get("stCreateNow") == 'T'){
					me.stCreateNowIds+=rs.get("examId")+",";
				}
			}
			if(me.stCreateNowIds!=""){
				me.examTaskRunner.start(me.examTask);
			}else{
				me.examTaskRunner.stop(me.examTask);
			}
		});
		//me.expandAll();
	},
	cancelledData:function(ids){
		var me = this;
		Ext.Ajax.request({
			method : 'POST',
			url : "exam/exampaper/cancelledForExampaperListAction!cancelled.action",
			success : function(response) {
				var result = Ext.decode(response.responseText);
				var selected = me.getSelectionModel().selected;
				if(result.success==true)
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
				else
				   Ext.Msg.alert('错误提示', result[0].errors);
			},
			failure : function(response) {
				var result = Ext.decode(response.responseText);
				if (result && result.length > 0)
					Ext.Msg.alert('错误提示', result[0].errors);
				else
					Ext.Msg.alert('信息', '后台未响应，网络异常！');
			},
			params : "id=" + ids
		});
	},
	yz : function(){
		var me = this;
		var examStartTimeValue = me.form.getForm().findField("examStartTime").getValue();
		var examEndTimeValue = me.form.getForm().findField("examEndTime").getValue();
		var nowDate = new Date();
		if(examStartTimeValue == undefined || examStartTimeValue == null){
			Ext.Msg.alert('信息', '考试开始时间不能为空！');
			return false;
		}
		if( examEndTimeValue == undefined || examEndTimeValue == null){
			Ext.Msg.alert('信息', '考试结束时间不能为空！');
			return false;
		}else if(examStartTimeValue.getTime()>=examEndTimeValue.getTime()){
			Ext.Msg.alert('信息', '考试开始时间必须小于考试结束时间！');
			return false;
		}/*else if(!(examStartTimeValue.getFullYear() == examEndTimeValue.getFullYear()
			&& examStartTimeValue.getMonth() == examEndTimeValue.getMonth()
			&& examStartTimeValue.getDate() == examEndTimeValue.getDate())){
			Ext.Msg.alert('信息', '考试开始时间与结束时间的日期必须相同，只能在一天！');
			return false;
		}*/else if(examEndTimeValue.getTime()<nowDate.getTime()){
			Ext.Msg.alert('信息', '您设置的考试结束时间小于当前时间！');
			return false;
		}
		return true;
		
	},
	setExamPropertyText : function(oper){
		var me = this;
		var _publicId = me.form.getForm().findField("publicId").getValue();
		if(_publicId!=undefined && _publicId!=null && _publicId!=""){
			Ext.Ajax.request({
				url : 'base/exampublic/findExamPublicBaseForExamPublicFormAction!findExamPublicBase.action',
				method : 'post',
				timeout: 60000,
				params : {
				   id : _publicId
				},
				success:function(response){
					var re = Ext.decode(response.responseText);
					var examProperty = re.form.examProperty;
					var examArrangeNameObj = me.form.getForm().findField("examArrangeName");
					if(oper == 'add' || examArrangeNameObj.getValue()==undefined || examArrangeNameObj.getValue() == ''){
						examArrangeNameObj.setValue(re.form.examTitle);
					}
					me.form.getForm().findField("examProperty").setValue(examProperty);
					me.form.getForm().findField("examTypeId").setValue(re.form.examTypeId);
					
					var examStartTimeObj = me.form.getForm().findField("examStartTime");
					if(oper == 'add' || examStartTimeObj.getValue()==undefined || examStartTimeObj.getValue() == ''){
						examStartTimeObj.setValue(re.form.planExamTime);
					}
					if(me.oldpublicId != _publicId){
						me.form.getForm().findField("scoreStartTime").setValue(re.form.scoreStartTime);
						me.form.getForm().findField("scoreEndTime").setValue(re.form.scoreEndTime);
					}
					/*if(examProperty == '10'){
						me.form.getForm().findField("scoreStartTime").allowBlank = false;
						me.form.getForm().findField("scoreEndTime").allowBlank = false;
						me.form.getForm().findField("scoreStartTime").show();
						me.form.getForm().findField("scoreEndTime").show();
					}else{
						me.form.getForm().findField("scoreStartTime").allowBlank = true;
						//me.form.getForm().findField("scoreEndTime").hide();
						me.form.getForm().findField("scoreEndTime").allowBlank = true;
						me.form.getForm().findField("scoreStartTime").readonly = true;
						me.form.getForm().findField("scoreEndTime").readonly = true;
					}*/
					
					Ext.Ajax.request({
						url : 'exam/exampaper/getDicKslxForExampaperListAction!getDicKslx.action',
						method : 'post',
						success:function(response){
							var examKslxList = Ext.decode(response.responseText).examKslxList;
							if(examKslxList!=undefined){
								for(var i=0;i<examKslxList.length;i++){
									if(examKslxList[i].dataKey == re.form.examTypeId){
									     me.form.getForm().findField("examTypeIdText").setValue(examKslxList[i].dataName);
									     break;
									}
								}
							}		
						},
						failure:function(){}
					});			
					if(examProperty == '10'){
						me.form.getForm().findField("examPropertyText").setValue("达标考试")		
					}else if(examProperty == '20'){
						me.form.getForm().findField("examPropertyText").setValue("竞赛考试")		
					}else{
						me.form.getForm().findField("examPropertyText").setValue("")		
					}
					me.oldpublicId = _publicId;
					/*var examTypeId = examTypeIdObj.getValue();
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
							   isNotSetFlag = false;
							   break;
							}
						}
					}else{
						examTypeIdObj.setValue("");
					}
					});*/
				},
				failure:function(){}
			});
			
			
		}
	},
	formatTime : function(value){
		if(value=='' || value == null)return value;
		Date.prototype.Format = function (fmt) { //author: meizz 
				var o = {
					"M+": this.getMonth() + 1, //月份 
					"d+": this.getDate(), //日 
					"h+": this.getHours(), //小时 
					"m+": this.getMinutes(), //分 
					"s+": this.getSeconds(), //秒 
					"q+": Math.floor((this.getMonth() + 3) / 3), //季度 
					"S": this.getMilliseconds() //毫秒 
				};
				if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
				for (var k in o)
				if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
				return fmt;
		}	
		if(value instanceof Date){
			return (new Date(value)).Format("yyyy-MM-dd hh:mm:ss");
		}else{
		
			return value;
		}
	},
	getExamineeForm : function(id,state,parentMe){
			var me = this;
			me.listType = '';
			var formConfig = {};
			var readOnly = readOnly || false;
			formConfig.readOnly = readOnly;
			formConfig.fileUpload = true;
			formConfig.clearButtonEnabled = false;
			formConfig.saveButtonEnabled = false;
			formConfig.formUrl = "exam/exampaper/saveExamUserForExampaperFormAction!saveExamUser.action";//保存
			formConfig.findUrl = "exam/exampaper/findExamUserForExampaperFormAction!findExamUser.action";
			formConfig.callback = function() {};
			formConfig.columnSize = 1;
			formConfig.jsonParemeterName = 'examForm';
			formConfig.items = new Array();
			formConfig.items.push({
				xtype:'hidden',
				name:'examId'
			});
			me.selectExamineeTree = new base.core.SelectTree({
					checked : true,
					allowBlank : true,
					fieldLabel:"选择考生",
					xtype : 'selecttree',
					nameKey : 'userId',
					nameLable : 'userName',
					name:'examUserFormList',
					readerRoot : 'children',
					keyColumnName : 'id',
					titleColumnName : 'title',
					childColumnName : 'children',
					selectType:'exam',
					selectTypeName:'选择考生',
					selectUrl : "exam/exampaper/queryExamineeTreeForExampaperListAction!queryExamineeTree.action?id="+id,
					labelWidth : 80,
					width : 985
			});
			formConfig.items.push(me.selectExamineeTree);
			
			me.examUserTree = me.getExamUserTreeGrid(id);
			me.examUserAllPanel = new Ext.Panel({ 
				 frame : true,
				 bodyStyle : "border:0;padding:0px 0px 0px 0px;overflow-x:hidden; overflow-y:auto",
				 defaultType : 'textfield',
				 margins : '0 0 0 0',
				 height:parentMe.cfg != 'base'?471:530,
				 colspan : 1,
				 forceFit : true,// 自动填充列宽,根据宽度比例
				 allowMaxSize : -1,// 允许最大行数,-1为不受限制
				 layout: 'border',
				 items : [ 
				 	  me.examUserTree,
			    	  me.setExamUserTable(id,parentMe)
		    	  ]
	    	});
			formConfig.items.push(me.examUserAllPanel);
			
			if(parentMe.cfg != 'base'){
				formConfig.items.push(new Ext.form.Label({
						text: '提示：(1)“阅卷状态”一栏，移到考试对应单元格内可以查看相应阅卷情况。' +
								'(2)“登陆访问验证清理”一栏，用于清理准考证多地点登陆验证信息，如退出浏览器后，在其它机子登陆不了，则点击进行清理。' +
								'(3)“操作”一栏，“初始化”按钮用于初始化单个考生试题，“删除”按钮用于删除单个考生，“加载”按钮用于加载单个考生试题到内存供考生考试使用。',
						margins : '0 0 0 20',
						style : {
							color:'red'
						}
				}));
			}
			
			
			formConfig.otherOperaters = [];//其它扩展按钮操作
			
			formConfig.otherOperaters.push({
					boxLabel:'考生信息保存后自动加载到内存&nbsp; ',
	                fieldLabel:' ',
	                labelSeparator :'',
	                name:'zdChk',
	                inputValue : true,
	                checked : false,
	                xtype : 'checkbox',
	                hidden:parentMe.cfg == 'base'
			});
			
			formConfig.otherOperaters.push({
				xtype : 'button',//按钮类型
				icon : 'resources/icons/fam/save.gif',//按钮图标
				text : "考生信息保存",//按钮标题
				timeout: 6000000,
				handler : function() {
					me.form2.submit();
				}
			});
				
			formConfig.otherOperaters.push({
		            	icon : 'resources/icons/fam/folder_wrench.png',//按钮图标
						text : "考生试卷初始化",//按钮标题
						hidden:parentMe.cfg == 'base',
						handler : function() {//按钮事件
							//状态 5:保存10:上报15:发布20:打回
							if(state==null || state=='' || state === '5' || state === '10' || state === '15' || state === '20'){
								var examMaskTip = EapMaskTip(me.form2);
								examMaskTip.show();
								//var record = selected.get(0);
								EapAjax.request({
									method : 'POST',
									timeout: 300000,
									url : 'exam/exampaper/saveExamTestPaperForExampaperFormAction!saveExamTestPaper.action?id='+id,
									async : true,
									success : function(response) {
										var result = Ext.decode(response.responseText);
										examMaskTip.hide();
										//me.queryExamUserTable(id)
										me.examUserTree.store.load();
										if (Ext.isArray(result)&&result[0].errors) {
											var msg = result[0].errors;
											Ext.Msg.alert('错误', msg);
										} else {
											Ext.Msg.alert('信息', '操作成功！');
											//me.termQueryFun(false,'flash');
										}
									},
									failure : function() {
										examMaskTip.hide();
										Ext.Msg.alert('信息', '后台未响应，网络异常！');
									}
								});
							}else{
								Ext.Msg.alert('提示', '该考试安排已发布，不能进行修改！');
								//me.queryExamUserTable(id)
								me.examUserTree.store.load();
							}
						}
		            });
		    formConfig.otherOperaters.push({
		    	icon : 'resources/icons/fam/folder_go.gif',//按钮图标
				text : "加载到内存",//按钮标题
				tooltip : '将所有考试全部加载到内存，等待考试',
				hidden:parentMe.cfg == 'base',
				handler : function() {
					me.addAllUserInKhd(id);
				}
			});
			formConfig.otherOperaters.push({
				iconCls : 'exportXls',
				text : "导出考生信息",//按钮标题
				tooltip : '导出考生信息',
				handler : function() {
					window.location = 'exam/exampaper/excelExamineeInfoListForExampaperExportExcelAction!excelExamineeInfoList.action?id='+id;
				}
			});
			
			
			formConfig.otherOperaters.push({
				xtype : 'button',//按钮类型
				icon : 'resources/icons/fam/application_view_list.png',//按钮图标
				text : "考试安排维护",//按钮标题
				hidden:parentMe.cfg == 'base',
				handler : function() {//按钮事件
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
					var state = record.get("state")
					//状态 5:保存10:上报15:发布20:打回
					if(state==null || state=='' || state === '5' || state === '20' || state === '15'){
						if(me.termForm)
								 queryTerm = me.termForm.getValues(false);
						me.userformWin.hide();
						me.openFormWin(id, function() {me.termQueryFun();},false,record.data,queryTerm,'update');
					}else{
						Ext.Msg.alert('提示', '上报或发布状态，不能进行修改！');
					}
				}
			});
			
		    me.form2 = ClassCreate('base.model.Form',formConfig);
			me.form2.parentWindow = this;
			me.form2.submit = function(){
					var meForm = me.form2;
					if (meForm.getForm().isValid()&&
					   (!meForm.validate||meForm.validate(meForm.getForm().getValues(false)))){
						var examMaskTip = EapMaskTip(me.form2);
						examMaskTip.show();
						meForm.getForm().findField('json').setValue("");
						meForm.getForm().findField('json').setValue(Ext.encode(meForm.getForm().getValues(false)));
						meForm.getForm().submit({
							success : function(form, action) {
								var isFlag = false;
								if(//meForm.getForm().findField("zdChk").getValue() == true || 
									parentMe.cfg == 'base'){
									me.addAllUserInKhd(id);
									isFlag = true;
								}
								var msg = action.result.msg;
								if (!msg)
									msg = '操作成功！';
							    examMaskTip.hide();
							    if(isFlag){
							    	msg+="同时，考生考试的最新信息加载成功！";
							    }
								Ext.Msg.alert('提示', msg,function(){
									//me.queryExamUserTable(id);
									me.examUserTree.store.load();
									var examUserValueTmp = [];
									var examUserValue = me.selectExamineeTree.getValue();
									for(var i =0;i<examUserValue.length;i++){
										//if(examUserValue[i].userId!=userId){
											examUserValueTmp[examUserValueTmp.length] =examUserValue[i];
										//}
									}
									me.selectExamineeTree.reflash("exam/exampaper/queryExamineeTreeForExampaperListAction!queryExamineeTree.action?id="+id,true);
									me.selectExamineeTree.setValue(examUserValueTmp);
									EapAjax.request({
										url : 'exam/exampaper/initExamFileForExampaperListAction!initExamFile.action',
										params : {id : id}
									});
								});
							},
							failure : function(form, action) {
								if (action.result && action.result.length > 0)
									Ext.Msg.alert('错误提示',
											action.result[0].errors);
								else
									Ext.Msg.alert('信息', '后台未响应，网络异常！');
							    examMaskTip.hide();
							    //me.queryExamUserTable(id)
							    me.examUserTree.store.load();
							},
							timeout: 6000000
						});
					}
			} 
			
			//表单窗口
			me.userformWin = new WindowObject({
				layout:'fit',
				title:'[科目 - 考生] 设置',
				height:630,
				width:1020,
				border:false,
				frame:false,
				modal:true,
				//autoScroll:true,
				bodyStyle:'overflow-x:auto;overflow-y:hidden;',
				closeAction:'hide',
				items:[me.form2]
			});
			
			me.userformWin.show();
			me.form2.setFormData(id,function(result){
				//me.queryExamUserTable(id);
				me.examUserTreeStore.getRootNode().expand(false,false);
			});
		
	},
	 getExamUserTreeGrid : function(id){
		var me = this;
		me.examUserTreeStore = Ext.create('Ext.data.TreeStore',{
			fields : [{
						name : 'id',
						type : 'string'
					 },{
						name : 'text',
						mapping: 'title',
						type : 'string'
					 },{
						name : 'closeIcon',
						type : 'string'
					 },{
						name : 'icon',
						type : 'string'
					 },{
						name : 'leaf',
						type : 'bool'
					 },{
						name : 'type',
						type : 'string'
					 },{
						name : 'tagName',
						type : 'string'
					}],
			root: { expanded: false },
			autoLoad: false,
			proxy : {
				type : 'ajax',
				url :  "exam/exampaper/queryExamineeOrganTreeForExampaperListAction!queryExamineeOrganTree.action?id="+id,
				reader : {
					type : 'json'
				}
			},
			//nodeParam : 'parentId',
			defaultRootId : ''
		});
		me.examUserTree = new Ext.tree.TreePanel({
				store : me.examUserTreeStore,
				//id: 'leftTree',
				title : '筛选条件',
				region: 'west',
				split : false,
				collapsible : true,
				rootVisible : false,
				useArrows : true,
				autoScroll : true,
				width : 260,
				minSize : 80,
				maxSize : 200,
				listeners : {
					itemclick : function(view, record, item, index, e) {
						me.nodeText =record.raw.title;
						me.nodeType = record.raw.type;
						me.nodeId = record.raw.id;
						if(me.nodeType == 'organ' || me.nodeType == 'quarter'){
							me.inticketArray = [];
							me.isMyTest = false;
							me.queryExamUserTable(id);
						}
					}
				}
		});
		me.parentTypeName = '';
		me.examUserTreeStore.on("load", function(mystore,node,success) {
			var record = mystore.getRootNode().childNodes[0];
			//record.expand();
			//record = record.childNodes[0];
			me.nodeText =record.raw.title;
			me.nodeType = record.raw.type;
			me.nodeId = record.raw.id;
			if(me.nodeType == 'organ' || me.nodeType == 'quarter'){
				me.inticketArray = [];
				me.isMyTest = false;
				me.queryExamUserTable(id);
			}
		});
		return me.examUserTree;
	},
	setExamUserTable : function (id,parentMe){
		var me = this;
		me.examUserTableStore = new Ext.data.Store({
			fields:[{name:"userTestpaperId"},{name:"yjstateRemark"},{name:"userId"},{name:"titleFileString"},
				{name:"gzServer"},
				{name:"ansFileString"},{name:"userName"},{name:"inticket"},{name:"idNumber"},
				{name:"examPassword"},{name:"titleFile"},{name:"ansFile"},
				{name:"userAnsFile"},{name:"yjstate"},{name:"initType"},{name : "cleanKeyClean"},
				{name:"optBt"},{name:"iocpUrl"}],
			autoLoad :false,
			proxy:{
				type : 'ajax',
				actionMethods : "POST",
				timeout: 120000,
				url:'',
	            reader : {
					type : 'json',
					root : 'examineeInfoList',
					totalProperty : "examineeInfoListTotal"
				}
			},
			remoteSort : false
		}); 
		/*me.examUserTableStore.on('load',function(pa1,pa2){
			console.log(pa1);
			console.log(pa2);
		});*/
	    me.examUserTableGrid = new Ext.grid.GridPanel({
    	 	store:me.examUserTableStore,
    	 	autoScroll :true,
			//autoHeight:true,
    	 	height:parentMe.cfg != 'base'?432:491,
    	 	colspan:2,
    	 	autoWidth:true, 
    	 	region: 'center',
			stripeRows:true, //斑马线效果          
    	 	columnLines : true,
    	 	forceFit: true,
    	 	collapsible : false,
    	 	//title: '考生信息',
	    	columns:[
	    		{header:"userTestpaperId",dataIndex:"userTestpaperId",hidden:true},
	    		{header:"userId",dataIndex:"userId",hidden:true},
	    		{header:"titleFileString",dataIndex:"titleFileString",hidden:true},
	    		{header:"ansFileString",dataIndex:"ansFileString",hidden:true},
	    		{header:"yjstateRemark",dataIndex:"yjstateRemark",hidden:true},
	    		{header:"iocpUrl",dataIndex:"iocpUrl",hidden:true},
	    		new Ext.grid.RowNumberer({text:"序号",width:45,align:"center"}),
	    		{header:"姓名",dataIndex:"userName",sortable:false, menuDisabled:true,width:23,align:"center",
	    		renderer:function(value, metadata, record){metadata.style="white-space: normal !important;"; return value;}},
	    		{header:"准考证",dataIndex:"inticket",sortable:false, menuDisabled:true,width:80,align:"center",
	    		renderer:function(value, metadata, record){metadata.style="white-space: normal !important;"; return value;}},
	    		{header:"身份证",dataIndex:"idNumber",sortable:false, menuDisabled:true,width:65,align:"center",
	    		renderer:function(value, metadata, record){metadata.style="white-space: normal !important;"; return value;}},
	    		{header:"密码",dataIndex:"examPassword",sortable:false, menuDisabled:true,width:24,align:"center",
	    		renderer:function(value, metadata, record){metadata.style="white-space: normal !important;"; return value;}},
	    		{header:"试题<br>加载",dataIndex:"gzServer",sortable:false, menuDisabled:true,width:20,align:"center",
	    			hidden:parentMe.cfg == 'base',
	    			renderer:function(value){
	    				if(value == '已加载'){
	    					return '<img src="resources/icons/fam/tick.png">';
	    				}else{
	    					return '<img src="resources/icons/fam/cross.gif">';
	    				}
	    				return "-";
	    			}
	    		},
	    		{header:"系统生<br>成试卷",dataIndex:"titleFile",sortable:false, menuDisabled:true,width:20,align:"center",
	    		hidden:parentMe.cfg == 'base',
	    		renderer:function(value,v1,v2){
	    			if(value > 0){
	    				try{
	    					Ext.decode(v2.get("titleFileString"));
	    					return '<img src="resources/icons/fam/tick.png">';
	    				}catch(e){
	    				    return '<font color=red>校验失败</font>';
	    				}
	    			}else{
	    			 	return '<img src="resources/icons/fam/cross.gif">';
	    			}
	    		}},
	    		{header:"系统生<br>成正确<br>答案",dataIndex:"ansFile",sortable:false, menuDisabled:true,width:30,align:"center",
	    		hidden:parentMe.cfg == 'base',
	    		renderer:function(value){
	    			if(value == -1){
	    			   return '不提供';
	    			}else if(value > 0){
	    			  try{
	    					Ext.decode(v2.get("ansFileString"));
	    					return '<img src="resources/icons/fam/tick.png">';
	    				}catch(e){
	    				    return '<font color=red>校验失败</font>';
	    				}
	    			}else{
	    			 	return '<img src="resources/icons/fam/cross.gif">';
	    			}
	    		}},
	    		{header:"考生<br>答案",dataIndex:"userAnsFile",sortable:false, menuDisabled:true,width:20,align:"center",
	    		hidden:parentMe.cfg == 'base',
	    		renderer:function(value){
	    			if(value > 0){
	    			   return '<img src="resources/icons/fam/tick.png">';
	    			}else{
	    			 	return '<img src="resources/icons/fam/cross.gif">';
	    			}
	    		}},
	    		{header:"阅卷状态",dataIndex:"yjstate",sortable:false, menuDisabled:true,width:38,align:"center",
	    		hidden:parentMe.cfg == 'base',
		    		renderer:function(value, metadata, record){
		    			 metadata.style="white-space: normal !important;";
		    			var yjstateRemark = record.get("yjstateRemark");
		    			if(yjstateRemark!=undefined && yjstateRemark!=null && yjstateRemark!=""){
				    		metadata.tdAttr = " data-qtip = '"+yjstateRemark+"'";
				    	}else{
				    		metadata.tdAttr = " data-qtip = '未查询到数据'";
				    	}
		    			if(value == '10'){
		    				return "<font color = 'red'>未完成，移<br>到此处查看</font>";
		    			}else if(value == '20'){
		    				return  "<font color = 'green'>已完成</font>";
		    			}else{
		    				return  "--";
		    			}
		    			
		    		}
	    		},
	    		{header:"状态",dataIndex:"initType",sortable:false, menuDisabled:true,width:25,align:"center",hidden:parentMe.cfg == 'base'},
	    		{header:"登陆访问<br>验证清理",dataIndex:"cleanKeyClean",sortable:false, menuDisabled:true,width:30,align:"center",
	    			renderer:function(v1,v2,v3){
	    				  v2.style="white-space: normal !important;";
	    				  var iocpUrl = v3.get("iocpUrl");
	    				  cleanKeyFun = function(inticket){
	    				  	 var examMaskTip = EapMaskTip(me.form2);
						     examMaskTip.show();
	    				  	 Ext.data.JsonP.request({
		                	    url: iocpUrl+'/clearcode?',
		                	    timeout: 300000,
		                	    params: { 
		                	    	inticket: inticket,
		                	    	examid:id
		                	    },
		                	    callbackKey: "jsonpcallback",
		                	    success: function(result) {
		                	    	examMaskTip.hide();
		                	    	if(result.code == '00001'){
										Ext.Msg.alert('提示', '处理成功！');
									}else{
										Ext.Msg.alert('提示', result.message);
									}
		                	    },
		                	    failure : function() {
		                	    	examMaskTip.hide();
									Ext.Msg.alert('提示', '网络异常！');
								}
							});
		    				  
	    				 };
						return '<input type="button" value="清理" onclick="cleanKeyFun(\''+v3.get("inticket")+'\')" />';
	    			}
	    		},
	    		{header:"操作",dataIndex:"optBt",sortable:false, menuDisabled:true,width:70,align:"center",hidden:parentMe.cfg == 'base',renderer :
	    			function(v1,v2,v3){
	    				initUserExam = function(userTestpaperId,inticket){
	    					var examMaskTip = EapMaskTip(me.form2);
							examMaskTip.show();
							EapAjax.request({
								timeout: 60000,
								url : 'exam/exampaper/initUserExamFileForExampaperFormAction!initUserExamFile.action',
								params : {
									id : userTestpaperId
								},
								success : function(response) {
									examMaskTip.hide();
									var result = Ext.decode(response.responseText);
									var msg = result.msg;
									//me.queryExamUserTable(id);
									me.examUserTree.store.load();
									me.addUserInKhd(id,inticket,null);
									if(msg==undefined || msg == null  || msg==''){
										Ext.Msg.alert('提示', '处理成功！');
									}else{
										Ext.Msg.alert('提示', msg);
									}
									
								},
								failure : function() {
									examMaskTip.hide();
									Ext.Msg.alert('提示', '网络异常！');
								}
							});
						};
						cleanUserExam = function(userTestpaperId,userId){
							var examUserValue = me.selectExamineeTree.getValue();
							var examMaskTip = EapMaskTip(me.form2);
							examMaskTip.show();
							EapAjax.request({
								timeout: 60000,
								url : 'exam/exampaper/cleanUserExamFileForExampaperFormAction!cleanUserExamFile.action',
								params : {
									id : userTestpaperId
								},
								success : function(response) {
									examMaskTip.hide();
									var result = Ext.decode(response.responseText);
									var msg = result.msg;
									if(msg==undefined || msg == null  || msg==''){
										Ext.Msg.alert('提示', '处理成功！');
									}else{
										Ext.Msg.alert('提示', msg);
									}
									me.selectExamineeTree.reflash("exam/exampaper/queryExamineeTreeForExampaperListAction!queryExamineeTree.action?id="+id,true);
									var examUserValueTmp = [];
									for(var i =0;i<examUserValue.length;i++){
										if(examUserValue[i].userId!=userId){
											examUserValueTmp[examUserValueTmp.length] =examUserValue[i];
										}
									}
									
									me.selectExamineeTree.setValue(examUserValueTmp);
									//me.queryExamUserTable(id);
									me.examUserTree.store.load();
								},
								failure : function() {
									examMaskTip.hide();
									Ext.Msg.alert('提示', '网络异常！');
								}
							});
						};
						gzUserExam = function(userTestpaperId,inticket){
							me.addUserInKhd(id,inticket,null);
						};
	    				var userTestpaperId = v3.get("userTestpaperId");
	    				var userId = v3.get("userId");
	    				var inticket = v3.get("inticket");
	    				return '<input type="button" value="初始化" title="加载试题到客户端" onclick="initUserExam(\''+userTestpaperId+'\',\''+inticket+'\')">&nbsp;' +
	    					'<input type="button" value="删除" title="删除考生" onclick="cleanUserExam(\''+userTestpaperId+'\',\''+userId+'\')">&nbsp;' +
	    					'<input type="button" value="加载" title="加载试题到客户端" onclick="gzUserExam(\''+userTestpaperId+'\',\''+inticket+'\')">'
	    			}
	    		}]
    	});
    	
    	me.examUserTableGrid.store.on("load", function() {
    		if(parentMe.cfg != 'base'){
    			me.testUser(id);
    		}
    	});
    	return me.examUserTableGrid;
	},
	queryExamUserTable : function (id){
		var me = this;
		me.inticketArray = [];
		var url = "exam/exampaper/queryExamineeInfoListForExampaperListAction!queryExamineeInfoList.action?id="+id;
		url+="&nodeId="+me.nodeId+"&nodeType="+me.nodeType+"&op="+me.cfg;
		me.examUserTableGrid.store.proxy.url = url;
	   	me.examUserTableGrid.store.load();
	   //	console.log(me.examUserTableGrid.store);
	},
	 encode:function(strIn) {
	    var intLen = strIn.length;
	    var strOut = "";
	    var strTemp;
	    for (var i = 0; i < intLen; i++) {
	        strTemp = strIn.charCodeAt(i);
	        if (strTemp > 255) {
	            tmp = strTemp.toString(16);
	            for (var j = tmp.length; j < 4; j++) tmp = "0" + tmp;
	            strOut = strOut + "^" + tmp;
	        } else {
	            if (strTemp < 48 || (strTemp > 57 && strTemp < 65) || (strTemp > 90 && strTemp < 97) || strTemp > 122) {
	                tmp = strTemp.toString(16);
	                for (var j = tmp.length; j < 2; j++) tmp = "0" + tmp;
	                strOut = strOut + "~" + tmp;
	            } else {
	                strOut = strOut + strIn.charAt(i);
	            }
	        }
	    }
	    return (strOut);
	},
	/*
	 * 设置考点信息
	 */
	showExamRoot:function(id,name){
		var me = this;
		var formConfig = {};
		formConfig.clearButtonEnabled = false;
		name = me.encode(name);
		formConfig.formUrl = "base/examroot/saveArrangeForExamRootFormAction!saveArrange.action?id="+id;//保存地址
		formConfig.findUrl = "base/examroot/findArrangeForExamRootFormAction!findArrange.action?id="+id;//查询地址
		formConfig.callback = function() {};
		formConfig.columnSize = 2;
		formConfig.jsonParemeterName = 'arrangeForm';
		formConfig.items = new Array();
		
		formConfig.items.push({
			xtype:'hidden',
			name:'examArrangeId'
		});
		formConfig.items.push({
			colspan:1,
			fieldLabel:"考试名称",
			name:'examArrangeName',
			xtype:'textfield',
			readOnly : true,
			width:250,
			labelWidth : 100
		});
		formConfig.items.push({
			colspan:1,
			xtype : 'select',
			name:'publicId',
			fieldLabel:"信息发布名称",
			selectUrl:'exam/exampaper/queryExamPublicListForExampaperListAction!queryExamPublicList.action?op=create',
			valueField:'publicId',
			displayField:'examTitle',
			jsonParemeterName:'examPublicList',
			labelWidth : 100,
			width:250,
			readOnly : true
		});
		formConfig.items.push({
			colspan:1,
			xtype : 'select',
			name:'examTypeId',
			fieldLabel:"考试类型",
			selectUrl:'exam/exampaper/getDicKslxForExampaperListAction!getDicKslx.action',
			valueField:'dataKey',
			displayField:'dataName',
			jsonParemeterName:'examKslxList',
			labelWidth : 100,
			width:250,
			readOnly : true
		});
		formConfig.items.push({
			colspan:1,
			fieldLabel:"考试编码",
			name:'examCode',
			xtype:'textfield',
			readOnly : true,
			maxLength : 50,
			labelWidth : 100,
			width:250
		});
		formConfig.items.push({
			colspan : 2,
			fieldLabel:"考试类型",
			xtype : 'select',
			readOnly : true,
			labelWidth : 100,
			width:250,
			name:'examProperty',
			data:[[10,'达标考试'],[20,'竞赛考试']],
			defaultValue : 10
		});
	    
		var editorExamName = Ext.widget('myselect',{	
			emptyText :'请选择考试科目',
			selectUrl :'base/examroot/queryExamComForExamRootFormAction!queryExamCom.action?id='+id,
			valueField:'examId',
			displayField:'examName',
			elseField :'examId',
			allowBlank : false,
			jsonParemeterName : 'queryExamCom'
		});
		
		var editorRootName = Ext.widget('myselect',{	
			emptyText :'请选择考点',
			selectUrl :'base/examroot/queryRootComForExamRootListAction!queryRootCom.action',
			valueField:'examRootId',
			displayField:'examRootPlace',
			elseField :'examRootName',
			allowBlank : false,
			jsonParemeterName : 'queryRootCom'
		});
		
	    var editTable = Ext.widget('editlist',{
			colspan : 2,
			fieldLabel : '考点维护',
			name : 'examRootList',
			xtype : 'editlist',
			addOperater : true,
			deleteOperater : true,
			enableMoveButton:false,
			viewConfig:{height:160,width:'99%'},
			columns : [{
						width : 0,
						name : 'examExamrootId'
					   },/*{
						name : 'exam',
						header : '考试科目',
						editor : editorExamName,
						renderer:function(value,cellmeta,record,rowIndex,columnIndex,store){
							console.log("---");
							console.log(value);
							console.log(editorExamName.store.getCount());
							console.log("---");
							var rtValue = "";
							var idValue = "";
							for(var i = 0 ; i< editorExamName.store.getCount();i++){
								var rs = editorExamName.store.getAt(i);
								console.log(rs);
								if(rs['data']['examId'] == value['examId']){
									rtValue = rs['data']['examName'];
									//idValue = rs.get("empId");
									break;
								}
							}
							
							return rtValue;
						},
						width : 1
					},{
						name : 'examRoot',
						header : '考点',
						editor : editorRootName,
						renderer:function(value,cellmeta,record,rowIndex,columnIndex,store){
							console.log(value);
							var rtValue = "";
							var idValue = "";
							for(var i = 0 ; i< editorRootName.store.getCount();i++){
								var rs = editorRootName.store.getAt(i);
								if(rs['data']['examRootId'] == value['examRootId']){
									rtValue = rs['data']['examRootPlace'];
									//idValue = rs.get("empId");
									break;
								}
							}
							
							return rtValue;
						},*/
					   {
							name : 'exam',
							header : '考试科目',
							editor : {
										xtype: 'selectobject',
										selectUrl:'base/examroot/queryExamComForExamRootFormAction!queryExamCom.action?id='+id,
										valueField : 'examId',
										displayField : 'examName',
										readerRoot : 'queryExamCom'
							},
							renderer:function(value,cellmeta,record,rowIndex,columnIndex,store){
								return value['examName'];
							},
						width : 1
					   },{
							name : 'examRoot',
							header : '考点',
							editor : {
										xtype: 'selectobject',
										selectUrl:'base/examroot/queryRootComForExamRootListAction!queryRootCom.action',
										valueField : 'examRootId',
										displayField : 'examRootPlace',
										readerRoot : 'queryRootCom'
							},
							renderer:function(value,cellmeta,record,rowIndex,columnIndex,store){
								return value['examRootPlace'];
							},
						width : 1
					},{
						name : 'actualNum',
						header : '实际人数',
						editor : {
							xtype: 'numberfield'
						},
					width : 1
				},{
					name : 'seatArrangement',
					header : '座位安排方式',
					editor : {
						xtype : 'select',
						data:[[5,'人工'],[10,'随机'],[15,'部门间隔'],[20,'机构']]
					},
					renderer:function(value,cellmeta,record,rowIndex,columnIndex,store){
						switch(value){
						case '5' :
						case 5 :
							return '人工';
						case '10' :
						case 10 :
							return '随机';
						case '15' :
						case 15 :
							return '部门间隔';
						case '20' :
						case 20 :
							return '机构';
						default:
							return '';
						}
					},
				width : 1
				}]
		});
	    
	    formConfig.items.push(editTable);
	    
	    var rootForm = ClassCreate('base.model.Form',formConfig);
		
		var rootWin = new WindowObject({
			layout:'fit',
			title:'考点 设置',
			height:350,
			width:700,
			border:false,
			frame:false,
			modal:true,
			closeAction:'hide',
			items:[rootForm]
		});
		rootWin.show();
		rootForm.setFormData(id,function(result){
		});
	},
	
	/*
	 * 设置阅卷人信息
	 */
	showExamReviewer:function(id,name){
		var me = this;
		
		//专业选择框
	    me.editorZY = Ext.widget('selecttree',{	
					checked : true,
					nameKey : 'specialityid',
					nameLable : 'specialityname',
					readerRoot : 'children',
					keyColumnName : 'id',
					titleColumnName : 'title',
					childColumnName : 'children',
					selectType : 'speciality',
					selectTypeName : '专业',
					selectUrl : 'baseinfo/speciality/treeForSpecialityListAction!tree.action',
					listeners:{
						collapse:function(){
						me.editTable.getView().refresh();
						}
					}
	    });
		/*me.editorYJR = Ext.widget('myselect',{	
			emptyText :'请选择阅卷人',
			selectUrl :'base/exammarkpeopleinfo/queryZJReviewerComForExamMarkPeopleInfoListAction!queryZJReviewerCom.action',
			valueField:'employee.employeeId',
			displayField:'name',
			elseField :'employee.employeeId',
			allowBlank : false,
			width : 60,
			jsonParemeterName : 'queryZJReviewerCom'
		});*/
		
		me.editorYJR = Ext.widget('selecttree',{
					name : 'examMarkEmp',
					selectTypeName : '阅卷人',
					//addPickerWidth : 200,
					xtype : 'selecttree',
					nameKey : 'employeeId',
					nameLable : 'employeeName',
					readerRoot : 'children',
					selectType :'employee',//指定可选择的节点
					keyColumnName : 'id',
					titleColumnName : 'title',
					childColumnName : 'children',
					allowBlank : false,
					//selectUrl : "base/exammarkpeopleinfo/queryReviewerTreeForExamMarkPeopleInfoListAction!queryReviewerTree.action",//?organId="+base.Login.userSession.currentOrganId,
					selectUrl : "base/exammarkpeopleinfo/queryExamReviewerExTreeForExamMarkPeopleInfoListAction!queryExamReviewerExTree.action?examId="+id,//?organId="+base.Login.userSession.currentOrganId,
					selectEventFun:function(combo){
						
					}
		});
		
		me.editorExammarkpeople = true;
		me.editorYJR.store.on("load",function(){
			if(me.editorExammarkpeople){
				me.editorExammarkpeople = false;
				var formConfig = {};
				formConfig.clearButtonEnabled = false;
				name = me.encode(name);
				formConfig.formUrl = "base/exammarkpeopleinfo/saveReviewerForExamMarkPeopleInfoFormAction!saveReviewer.action?examName="+name;//保存地址
				formConfig.findUrl = "base/exammarkpeopleinfo/findReviewerForExamMarkPeopleInfoFormAction!findReviewer.action?examName="+name;//查询地址
				formConfig.callback = function() {me.termQueryFun(false,'flash');};
				formConfig.columnSize = 2;
				formConfig.jsonParemeterName = 'reviewerForm';
				formConfig.items = new Array();
				
				formConfig.items.push({
					name : 'examId',
					xtype : 'hidden'
				});
				formConfig.items.push({
					colspan : 2,
					fieldLabel : '考试名称',
					name : 'examName',
					xtype : 'textfield',
					width : 570,
					labelWidth : 90,
					readOnly : true
				});
				/*formConfig.items.push({
					colspan : 1,
					fieldLabel : '开始时间',
					name : 'startTime',
					xtype : 'datetimefield',
					format : 'Y-m-d H:i',
					allowBlank : false,
					labelWidth : 125,
					width:280
				});
				formConfig.items.push({
					colspan : 1,
					fieldLabel : '结束时间',
					name : 'endTime',
					xtype : 'datetimefield',
					format : 'Y-m-d H:i',
					allowBlank : false,
					labelWidth : 125,
					width:280
				});*/

			    
			    me.editTable = ClassCreate('base.core.EditList',{
					colspan : 2,
					fieldLabel : '阅卷人维护',
					name : 'reviewerChilds',
					xtype : 'editlist',
					addOperater : true,
					deleteOperater : true,
					enableMoveButton:false,
					viewConfig:{height:234,autoScroll:true},
					columns : [{
								width : 0,
								name : 'examMarkpeopleId'
							   },{
									width : 0,
									name : 'reviewerName'
							   },{
									width : 0,
									name : 'empId'
							   },{
								name : 'examMarkEmp',
								header : '阅卷人',
								allowBlank : false,
								editor : me.editorYJR,
								width : 3,
								sortable:false,
								menuDisabled:true,
								titleAlign:"center",
								renderer:function(value,cellmeta,record,rowIndex,columnIndex,store){
									/*var rtValue = "";
									var idValue = "";
									for(var i = 0 ; i< me.editorYJR.store.getCount();i++){
										var rs = me.editorYJR.store.getAt(i);
										if(rs.get("employee.employeeId") == value){
											rtValue = rs.get("name");
											idValue = rs.get("employee.employeeId");
											break;
										}
									}*/
									/*var flag = true;
									for(var i=0;i<store['data']['items'].length;i++){
										var tmp = store['data']['items'][i];
										if(tmp['data']['empId']==idValue && idValue!=''){
											Ext.Msg.alert("提示","已选择过此阅卷人，请另行选择");
											flag = false;
											record['data']['examMarkpeopleInfo']='';
											record['data']['reviewerName'] = '';
											record['data']['empId'] = '';
											rtValue="";
											break;
										}
									}*/
									//if(flag && idValue!=''){
										//record['data']['reviewerName'] = rtValue;
										//record['data']['empId'] = idValue;
									//}
									//console.log(value)
									return value['employeeName'];
								}
							},{
									width : 0,
									name : 'isMain',
									defaultValue : '0',
									value : '0'
							 }/*,{
								name : 'isMain',
								header : '是否主阅卷',
								allowBlank : false,
								width : 1,
								sortable:false,
								menuDisabled:true,
								titleAlign:"center",
								editor : {
									xtype : 'select',
									data:[['5','是'],['0','否']]
								},
								defaultValue : '0',
								renderer:function(value){
									if(value==5){
										return '是';
									}else if(value==0){
										return '否';
									}
								}
							}*//*,{
								name : 'themeTypes',
								header : '涉及题型',
								allowBlank : false,
								width : 2.7,
								addPickerWidth : 50,
								sortable:false,
								menuDisabled:true,
								titleAlign:"center",
								editor : {
									xtype : 'selecttree',
									nameKey : 'themeTypeId',
									nameLable : 'themeTypeName',
									readerRoot : 'themeTypeCom',
									keyColumnName : 'themeTypeId',
									titleColumnName : 'themeTypeName',
									checked: true,
									childColumnName : '',
									selectUrl : 'base/themetype/themeTypeComForThemeTypeListAction!themeTypeCom.action'
								},
								renderer:function(value,cellmeta,record,rowIndex,columnIndex,store){
									var v = "";
									//自动换行
									cellmeta.style="white-space: normal !important;";
									Ext.Array.each(value,function(item){
										if(v=="") v = item['themeTypeName'];
										else v += ","+item['themeTypeName'];
									});
									return v;
								}
							}*//* {
								name : 'themeTypes',
								header : '涉及题型',
								editor : {
									xtype : 'select',
									emptyText :'请选择题型',
									selectUrl :'base/themetype/themeTypeComForThemeTypeListAction!themeTypeCom.action',
									valueField:'themeTypeId',
									displayField:'themeTypeName',
									jsonParemeterName : 'themeTypeCom'
								},
								width : 1
							}*//*,{
								name : 'specialitys',
								header : '涉及专业',
								editor : me.editorZY ,
								allowBlank : false,
								width : 2.7,
								addPickerWidth : 50,
								sortable:false,
								menuDisabled:true,
								titleAlign:"center",
								renderer:function(value,cellmeta,record,rowIndex,columnIndex,store){
									var v = "";
									//自动换行
									cellmeta.style="white-space: normal !important;";
									Ext.Array.each(value,function(item){
										if(v=="") v = item['specialityname'];
										else v += ","+item['specialityname'];
									});
									return v;
								}
							}*/]
				});
			    formConfig.items.push(me.editTable);
			    
			    var reviewerForm = ClassCreate('base.model.Form',formConfig);
			    
			    
			    reviewerForm.validate = function(meform){
			    	var liststore =  me.editTable.getStore();
			    	for(var i=0;i<liststore.getCount();i++){
			    		var rd1 = liststore.getAt(i);
			    		if(rd1.get("examMarkEmp")["employeeId"] == undefined   || rd1.get("examMarkEmp")["employeeId"] == 'null' || rd1.get("examMarkEmp")["employeeId"] == ''){
			    			Ext.Msg.alert('错误', "阅卷人不能为空！");
				    		return false;
			    		}
			    		
			    		var isSame = false;
				    	for(var j=i+1;j<liststore.getCount();j++){
				    		var rd2 = liststore.getAt(j);
				    		if(rd2.get("examMarkEmp")["employeeId"] == undefined  || rd2.get("examMarkEmp")["employeeId"] == 'null' || rd2.get("examMarkEmp")["employeeId"] == ''){
				    			Ext.Msg.alert('错误', "阅卷人不能为空！");
					    		return false;
			    			}
				    		if(rd1.get("examMarkEmp")["employeeId"] == rd2.get("examMarkEmp")["employeeId"]){
					    		isSame = true;
					    		break;
				    		}
				    	}
				    	if(isSame){
				    		Ext.Msg.alert('错误', "选择阅卷人不能相同！");
				    		return false;
				    	}
			    	}
			    	return true;
			    }
				
				var reviewerWin = new WindowObject({
					layout:'fit',
					title:'阅卷人 设置',
					height:400,
					width:600,
					border:false,
					frame:false,
					modal:true,
					closeAction:'hide',
					items:[reviewerForm]
				});
				reviewerWin.show();
				reviewerForm.setFormData(id,function(result){
					
				});
			
			}
		});
	},
	addAllUserInKhd :  function(examid){
		var me = this;
		for(var i = 0 ;i<me.examUserTableGrid.store.getCount();i++){
			var rec = me.examUserTableGrid.store.getAt(i);
			var inticket = rec.get("inticket");
			me.addUserInKhd(examid,inticket,rec);
		}
		Ext.Msg.alert('提示', '加载完成！');
	},
	addUserInKhd :  function(examid,inticket,rec){
		var me = this;
		if(me.requestHttpUrl == undefined || me.requestHttpUrl == null){
			EapAjax.request({
				timeout: 60000,
				url : 'exam/exampaper/getRequestHttpForExampaperListAction!getRequestHttp.action',
				async : false,
				success : function(response) {
					var result = Ext.decode(response.responseText);
					me.requestHttpUrl = result.requestHttpUrl;
					me.toUserInKhd(examid,inticket,rec);
				}
			});
		}else{
			me.toUserInKhd(examid,inticket,rec);
		}
	},
	toUserInKhd :  function(examid,inticket,rec){
		var me = this;
		var url = me.requestHttpUrl + "/getuser?examid="+examid+"&inticket="+inticket;
		if((rec==undefined || rec==null) 
				&& me.examUserTableGrid!=undefined 
				&& me.examUserTableGrid.store!=undefined 
				&& me.examUserTableGrid.store.getCount()>0){
			for(var i = 0 ;i<me.examUserTableGrid.store.getCount();i++){
				var rec_ = me.examUserTableGrid.store.getAt(i);
				if(rec_.get("inticket") == inticket){
					rec = rec_;
					break;
				}
			}
		}
		Ext.data.JsonP.request({
			timeout: 60000,
			url : url,
			async : false,
			callbackKey: "jsonpcallback",
			success: function(result) {
				if(me.isMyTest){
					if(result!=undefined && result!=null && result.code == '00001'){
						if(rec!=undefined && rec!=null)rec.set("gzServer",'已加载');
					}else{
					    if(rec!=undefined && rec!=null)rec.set("gzServer",'未加载');
					}
				}
			},
			failure : function() {
				if(rec!=undefined && rec!=null)rec.set("gzServer",'加载失败');
			}
		});
	},
	testUser :  function(examid){
		var me = this;
		me.inticketArray = [];
		var _index = 0;
		me.isMyTest = false;
		for(var i = 0 ;i<me.examUserTableGrid.store.getCount();i++){
			var rec = me.examUserTableGrid.store.getAt(i);
			var inticket = rec.get("inticket");
			if(me.requestHttpUrl == undefined || me.requestHttpUrl == null){
				EapAjax.request({
					timeout: 60000,
					async : false,
					url : 'exam/exampaper/getRequestHttpForExampaperListAction!getRequestHttp.action',
					success : function(response) {
						var result = Ext.decode(response.responseText);
						me.requestHttpUrl = result.requestHttpUrl;
						//me.testOneUser(examid,inticket,rec);
					}
				});
			}else{
				//me.testOneUser(examid,inticket,rec);
			}
			me.inticketArray[i] = [];
			me.inticketArray[i][0] = examid;
			me.inticketArray[i][1] = inticket;
			me.inticketArray[i][2] = rec;
		}
		me.isMyTest = true;
		me.testAllUser(_index);
	},
	testAllUser : function(_index){
		var me = this;
		if(me.inticketArray[_index]!=undefined && me.isMyTest){
			Ext.data.JsonP.request({
				timeout: 60000,
				url : me.requestHttpUrl + '/gettitle',
				callbackKey: "jsonpcallback",
				async : false,
				params : {
					examid : me.inticketArray[_index][0],
					inticket : me.inticketArray[_index][1]
				},
				success : function(result) {
					if(result!=undefined && result!=null && result.code == '00001'){
						//return result.code;//'00001'表示成功
						me.inticketArray[_index][2].set("gzServer",'已加载');
				    }else{
					    me.inticketArray[_index][2].set("gzServer",'加载失败');
					}
					_index++;
					if(_index<me.inticketArray.length && me.isMyTest){
						me.testAllUser(_index);
					}
					
				},failure : function() {
					me.inticketArray[_index][2].set("gzServer",'加载失败');
					_index++;
					if(_index<me.inticketArray.length){
						me.testAllUser(_index);
					}
				}
			});
		}
	},
	testOneUser : function(examid,inticket,rec){
		var me = this;
		Ext.data.JsonP.request({
			timeout: 60000,
			url : me.requestHttpUrl + '/gettitle',
			callbackKey: "jsonpcallback",
			async : false,
			params : {
				examid : examid,
				inticket : inticket
			},
			success : function(result) {
				if(result!=undefined && result!=null && result.code == '00001'){
					//return result.code;//'00001'表示成功
					rec.set("gzServer",'已加载');
			    }else{
				    rec.set("gzServer",'加载失败');
				}
			},failure : function() {
				rec.set("gzServer",'加载失败');
			}
		});
	},
	jzUserExamClient : function(jzexamId){
		var me = this;
		if(me.requestHttpUrl == undefined || me.requestHttpUrl == null){
			EapAjax.request({
					timeout: 60000,
					url : 'exam/exampaper/getRequestHttpForExampaperListAction!getRequestHttp.action',
					async : false,
					success : function(response) {
						me.requestHttpUrl = Ext.decode(response.responseText).requestHttpUrl;
					}
			});
		}	
												
		Ext.Ajax.request({
			method : 'POST',
			url : "exam/exampaper/queryExeExamUserListForExampaperListAction!queryExeExamUserList.action",
			timeout: 120000,
			async : false,
			success : function(response) {
				var result = Ext.decode(response.responseText);
				var examUserList = result.examUserList;							
				if(examUserList!=undefined && examUserList!=null && examUserList.length>0){
					for(var i=0;i<examUserList.length;i++){
						var examId = examUserList[i].examId;
						if(jzexamId==undefined || jzexamId == null || jzexamId == '' || jzexamId.indexOf(examId+",")!=-1){
							var inticket = examUserList[i].inticket;
							Ext.data.JsonP.request({
								timeout: 60000,
								url : me.requestHttpUrl + "/getuser",
								params : {
									examid : examId,
									inticket : inticket
								},
								async : false,
								callbackKey: "jsonpcallback",
								success: function(result) {},
								failure : function() {}
							});
						}
					}
				}
			},failure : function() {
			}
		});
	}
});