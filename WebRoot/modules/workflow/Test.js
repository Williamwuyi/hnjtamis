/**
 * 工作流的测试类
 */
ClassDefine('modules.workflow.Test', {
	extend : 'base.model.List',
	requires : ['modules.workflow.StartFlow','modules.workflow.ExcuteFlow'],
	statics:{
		/**
		 * 返回指定流程、指定员工的功能按钮集合
		 * @param grid 业务模块的列表对象
		 * @param str workflowCode 流程编码
		 * @param width 按钮点击时的窗口宽度
		 * @param height 按钮点击时的窗口高度
		 */
		getFlowTaskButtons:function(grid,flowCode,width,height,ifUndo){
			var buttons = new Array();
			Ext.Ajax.request({
				method : 'get',
				url : 'workflow/nodeMapForFlowTestAction!nodeMap.action?flowCode='+flowCode+"&random="+Math.random(),
				async : false,//是否异步
				success : function(response) {
					var result = Ext.decode(response.responseText);
					Ext.Array.each(result,function(s){
						buttons.push(Ext.widget('button',{icon : 'resources/icons/fam/cog.png',
							text : s.text,
							value:s.value,
							type:'flow',
							handler : function() {
								var selected = grid.getSelectionModel().selected;
								if (selected.getCount() == 0) {
									Ext.Msg.alert('提示', '请选择记录！');
									return;
								}
								var id = "";
								var record = null;
								for (var i = 0; i < selected.getCount(); i++) {
									record = selected.get(i);
									id = record.get(grid.keyColumnName);
								}
								var url="workflow/urlForFlowTestAction!url.action?id="+id+"&employeeId="+base.Login.userSession.employeeId+"&taskCode="+s.value
								        +"&random="+Math.random();
								EapAjax.request({method : 'GET',url : url,async : true,success:function(response){
						    		var ret = Ext.decode(response.responseText);
						    		if(Ext.isArray(ret)&&undefined!=ret[0].errors){
							    		Ext.Msg.alert('错误', ret[0].errors); 
						    		}else{
						    			var excuteId=ret.taskUrl.split("excuteId=")[1];
					    				var flowConfig = {title:s.text,excuteId:excuteId,
									    				  buttons:ret.param,url:ret.taskUrl};
					    				grid.openFormWin(id,function(submitData){
					    					//刷新原列表
					    					grid.termQueryFun(true,'flash');				    					
					    				},false,record.data,null,'flow',flowConfig);
						    		}						    		   
						    	}});
						   }}));
					});
					buttons.push(Ext.widget('button',{icon : 'resources/icons/fam/cross.gif',
						text : '撤消',
						handler : function() {
							var selected = grid.getSelectionModel().selected;
							if (selected.getCount() == 0) {
								Ext.Msg.alert('提示', '请选择记录！');
								return;
							}
							var id = "";
							var record = null;
							for (var i = 0; i < selected.getCount(); i++) {
								record = selected.get(i);
								id = record.get(grid.keyColumnName);
							}
							if(!grid.undo){
								Ext.Msg.alert('提示', '请定义撤消方法undo(id)！');
								return;
							}
							Ext.MessageBox.confirm('询问', '你真要撤消这次操作吗？',function(btn){
								if (btn == 'yes'){
								    grid.undo(id);
								    grid.termQueryFun(true,'flash');	
								}
							});
					   }}));
				},
				failure : function() {
					Ext.Msg.alert('信息', '后台未响应，网络异常！');
				}
			});
			return buttons;
		},
		/**
		 * 获取审核状态下拉框及列表显示转换方法
		 */
		getStateSelect:function(fieldLabel,flowCode,name){
			var select = Ext.widget('select',{
					name : name,
					fieldLabel : fieldLabel,
					selectUrl:'workflow/stateMapForFlowTestAction!stateMap.action?flowCode='+flowCode,
					enableSelectOne:false,
					renderer:function(value){
						for (var i = 0; i < select.store.getCount(); i++) {
							var record = select.store.getAt(i);
							if(record.data["value"]==value)
							   return record.data["text"];
						}  
					}
			});
			return select;
		},
		/**
		 * 刷新流程功能按钮
		 * @param {} flowCode
		 * @param {} buttons
		 */
		showHideButtons:function(flowCode,buttons){//显示或隐藏功能按钮，根据权限
				Ext.Ajax.request({
						method : 'get',
						url : 'workflow/nodeMapForFlowTestAction!nodeMap.action?flowCode='+flowCode+"&employeeId="+base.Login.userSession.employeeId+"&random="+Math.random(),
						async : false,//是否异步
						success : function(response) {
							var result = Ext.decode(response.responseText);
				    		Ext.Array.each(buttons,function(b){//循环功能按钮
				    		    if(b.type=='flow'){
					    		    var have = false;
						    		Ext.Array.each(result,function(s){//没有权限的要隐藏
						    			if(s.value==b.value){
						    				have = true;
						    			}
						    		});
						    		if(have)
						    		   b.show();
						    		else
						    		   b.hide();
				    		    }
				    		});
						},
						failure : function() {
							Ext.Msg.alert('信息', '后台未响应，网络异常！');
						}
					});	
			},
			/**
			 * 增加工作流操作按钮
			 */
			addWorkFlowOperaterButton:function(operaterButtons,flowConfig,form){
				//增加流程操作按钮
				if(!operaterButtons)
				operaterButtons = new Array();
				Ext.Array.each(flowConfig.buttons,function(s){
					operaterButtons.push(Ext.widget('button',{icon : 'resources/icons/fam/connect.gif',
						text : s.text,
						handler : function() {
							var form = form||this.ownerCt.ownerCt;//表单
							var toCode = form.getForm().findField("toCode");
							if(!toCode){
								form.add({xtype : 'hidden',name : 'toCode'});
								toCode = form.getForm().findField("toCode");
							}
							toCode.setValue(s.value);
							var excuteId = form.getForm().findField("excuteId");
							if(!excuteId){
								form.add({xtype : 'hidden',name : 'excuteId'});
								excuteId = form.getForm().findField("excuteId");
							}
							excuteId.setValue(flowConfig.excuteId);
						    form.submit(function(){
						    	toCode.setValue(null);
						        excuteId.setValue(null);
						    });						    
						}
					}));
				});
			},
			/**
			 * 增加历史审核信息
			 * @param {} id
			 * @param {} flowCode
			 * @param {} colspan
			 * @param {} height
			 * @return {}
			 */
			addWorkFlowAuditInfo:function(id,flowCode,colspan,height){
				return ClassCreate('base.model.List', {colspan:colspan,
							      fieldLabel : '审核历史信息',
							      readOnly : true,
								  //viewConfig:{height:height||100},//高度
							      columns : [{xtype : 'hidden',name:'excuteId'},
							              {name : 'createTime',header : '创建时间',width : 2}, 
							  	          {name : 'taskName',header : '任务名称',width : 2}, 
							  	          {name : 'employeeName',header : '执行者',width : 1}, 
							  	          {name : 'time',header : '执行时间',width : 2}, 
							  	          {name : 'to',header : '执行结果',width : 1}, 
							  	          {name : 'advice',header : '审核意见',width : 2	}],
								  enableExportXls:false,
								  enablePaging:false,
								  enableColumnMove:false,
								  enableColumnResize:true,
								  enableCheck:false,
								  enableMerge:false,
								  viewOperater:false,
								  addOperater:false,
								  deleteOperater:false,
								  updateOperater:false,
								  autoHeight : true,
								  listUrl:'workflow/auditInfoForFlowTestAction!auditInfo.action?id='+id+'&flowCode='+flowCode
							});
			},
			/**
			 * 获取流程的状态类型（NO、START、PROCESS、END）
			 * @param {} flowCode 流程编码
			 * @param {} id 业务主健
			 */
			stateType:function(flowCode,id,state){
				var type='NO';
				Ext.Ajax.request({
						method : 'get',
						url : 'workflow/stateTypeForFlowTestAction!stateType.action?flowCode='+flowCode+"&id="+id+"&state="+state+"&random="+Math.random(),
						async : false,//是否异步
						success : function(response) {
							var result = Ext.decode(response.responseText);
				    		type = result.msg;
						},
						failure : function() {
							Ext.Msg.alert('信息', '后台未响应，网络异常！');
						}
					});	
			    return type;
			}
	},
	initComponent : function() {
		var me = this;
		//模块列表对象
		this.columns = [{
					name : 'excuteId',
					width : 0
				}, {
					name : 'workFlowNode.workFlow.name',
					header : '工作流名称',
					width : 3
				}, {
					name : 'workFlowNode.name',
					header : '任务名称',
					width : 3
				} ,{
					name : 'createTime',
					header : '创建时间',
					format:'Y-m-d H:i',
					width : 2
				}, {
					name : 'quarterName',
					header : '执行岗位',
					width : 2
				}, {
					name : 'employeeName',
					header : '执行员工',
					width : 2
				}, {
					name : 'excuteTiime',
					header : '执行时间',
					width : 2
				}, {
					name : 'workFlowTo.state',
					header : '执行结果',
					width : 2
				}, {
					name : 'ifUndo',
					header : '是否撤消',
					width : 2,
					renderer:function(value){
						if(value) 
						   return "是";
						else
						   return "否";   
					}
				}];
		//模块查询条件对象
		this.terms = [{
						xtype : 'select',
						name : 'flowCode',
						fieldLabel : '工作流',
						selectUrl:'workflow/flowMapForFlowTestAction!flowMap.action',
						enableSelectOne:false,
						selectEventFun:function(combo){
							var nodeselect = me.termForm.getForm().findField('taskCode');
							nodeselect.reflash("workflow/nodeMapForFlowTestAction!nodeMap.action?employeeId=&flowCode="+(combo.value));
						}
						/*xtype: "radiogroupex",
					    fieldLabel: "工作流",
					    name:'flowCode',
					    columns: 3,
					    flex: 1,
					    defaultValue:'1',
					    editorType:'title',
					    selectUrl:'workflow/flowMapForFlowTestAction!flowMap.action',
					    selectEventFun:function(src,value){
							alert(Ext.encode(value));
						}*/
					},{
						xtype : 'select',
						name : 'taskCode',
						fieldLabel : '任务',
						selectUrl:'workflow/nodeMapForFlowTestAction!nodeMap.action',
						enableSelectOne:false
					},{
						xtype:'selecttree',
						name:'employeeId',
						checked : false,
						fieldLabel:'员工',
						nameKey : 'employeeId',
						nameLable : 'employeeName',
						//readerRoot : 'nodes',
						keyColumnName : 'id',
						addPickerWidth : 200,
						allowBlank : true,
						titleColumnName : 'title',
						childColumnName : 'children',
						selectType:'employee',
						editorType:'str',
						selectUrl : "organization/employee/odetreeForEmployeeListAction!odetree.action?organTerm="+base.Login.userSession.organId
				    },{
				    	fieldLabel : '创建开始时间',
						name : 'startDate',
						xtype : 'datetimefield',
						allowBlank : false,
						format : 'Y-m-d H:i'
				     },{
				    	fieldLabel : '创建结束时间',
						name : 'endDate',
						xtype : 'datetimefield',
						allowBlank : false,
						format : 'Y-m-d H:i'
				     },{
				     	fieldLabel : '是否完成',
						xtype : 'radiogroup',
						defaultType : 'radio',
						items : [
							{boxLabel : '是', name : 'complet', inputValue : true},
							{boxLabel : '否', name : 'complet', inputValue : false, checked : true}
						]
				     }];
		this.keyColumnName = "excuteId";//主健属性名
		this.viewOperater = false;
        this.addOperater = false;
        this.deleteOperater = false;
        this.updateOperater = false;
		this.listUrl = "workflow/testListForFlowTestAction!testList.action";//列表请求地址
		this.otherOperaters = [];//其它扩展按钮操作
        this.otherOperaters.push({
	                xtype : 'button',
					icon : 'resources/icons/fam/connect.png',
					text : '启动流程',
					handler : function() {
						base.Login.openJsWindow("modules.workflow.StartFlow",480,330,'启动流程',me);
					}
				});
	    this.otherOperaters.push({
	                xtype : 'button',
					icon : 'resources/icons/fam/user.png',
					text : '接收任务',
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
						var url="workflow/takeForFlowTestAction!take.action?id="+id+"&employeeId="+me.termForm.getForm().findField("employeeId").getValue();
						EapAjax.request({method : 'GET',url : url,async : true,success:function(response){
				    		var ret = Ext.decode(response.responseText);
				    		if(Ext.isArray(ret)) ret = ret[0];
				    		if(ret.success){
					    		Ext.Msg.alert('信息', '接收成功！');
				    		}else
				    		    Ext.Msg.alert('错误', ret.errors); 
				    	}});
					}
				});
		this.otherOperaters.push({
	                xtype : 'button',
					icon : 'resources/icons/fam/cross.gif',
					text : '撤消任务',
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
						var url="workflow/undoForFlowTestAction!undo.action?id="+id+"&employeeId="+me.termForm.getForm().findField("employeeId").getValue();
						EapAjax.request({method : 'GET',url : url,async : true,success:function(response){
				    		var ret = Ext.decode(response.responseText);
				    		if(Ext.isArray(ret)) ret = ret[0];
				    		if(ret.success){
					    		Ext.Msg.alert('信息', '撤消成功！');
				    		}else
				    		    Ext.Msg.alert('错误', ret.errors); 
				    	}});
					}
				});
		var excuteButton = Ext.widget('button',{
					icon : 'resources/icons/fam/cog.png',
					text : '执行任务',
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
						base.Login.openJsWindow("modules.workflow.ExcuteFlow?excuteId="+id,540,550,'执行任务',me);
					}
				});
		this.otherOperaters.push(excuteButton);
		this.callParent();
	}
});