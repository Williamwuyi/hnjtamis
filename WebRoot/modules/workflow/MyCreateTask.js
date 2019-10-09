/**
 * 流程监控类
 */
ClassDefine('modules.workflow.MyCreateTask', {
	extend : 'base.model.List',
	enableCheck:false,
	pageRecordSize:20,
	initComponent : function() {
		var me = this;
		// 模块列表对象
		this.columns = [{
		            name : 'excuteId',
					width : 0
				}, {
					name : 'flowName',
					header : '业务名称',
					width : 2
				}, {
					name : 'flowTitle',
					header : '业务标题',
					width : 2
				}, {
					name : 'task',
					header : '当前任务',
					width : 1
				}, {
					name : 'inMan',
					header : '参与者',
					width : 3
				}, {
					name : 'createTime',
					header : '创建时间',
					type : 'time',
					format:'Y-m-d H:i',
					width : 1
				}, {
					name : 'endTime',
					header : '结束时间',
					width : 1
				},{name:'funId',width:0},
				  {name:'url',width:0}];
	    this.terms = [{
						xtype : 'select',
						name : 'flowCode',
						fieldLabel : '流程',
						selectUrl:'workflow/flowMapForFlowTestAction!flowMap.action',
						enableSelectOne:false,
						selectEventFun:function(combo){
							var nodeselect = me.termForm.getForm().findField('taskCode');
							nodeselect.reflash("workflow/nodeMapForFlowTestAction!nodeMap.action?employeeId=&flowCode="+(combo.value));
						}
					},{
				    	fieldLabel : '创建开始时间',
						name : 'startDate',
						xtype : 'datefield',
						format : 'Y-m-d'
				     },{
				    	fieldLabel : '创建结束时间',
						name : 'endDate',
						xtype : 'datefield',
						format : 'Y-m-d'
				     },{
				     	fieldLabel : '参与者',
						xtype : 'textfield',
						name:'man'
				     },{
				     	fieldLabel : '是否完成',
						xtype : 'radiogroup',
						defaultType : 'radio',
						items : [
						    {boxLabel : '全部', name : 'completType',inputValue : -1},
							{boxLabel : '是', name : 'completType', inputValue : 1},
							{boxLabel : '否', name : 'completType', inputValue : 0,checked : true}
						]
				     }];
		this.keyColumnName = "busId";// 主健属性名
		this.viewOperater = false;
		this.addOperater = false;
		this.deleteOperater = false;
		this.updateOperater = false;
		this.jsonParemeterName = 'flowInfos';
		this.listUrl = "workflow/flowMoniterForFlowTestAction!flowMoniter.action?employeeId="
		           +base.Login.userSession.employeeId;// 列表请求地址
		this.otherOperaters = [];//其它扩展按钮操作
		this.callParent();
	},
	listeners:{
		'cellclick':function(thisGrid,td,cellIndex,record,tr,rowIndex,e,opts){
			var me = this;
			var id = record.data.funId;
			var nodeUrl = record.data.url;
			if(!nodeUrl){
				Ext.Msg.alert('错误', '任务无打开页面！');
				return;
			}
			if(nodeUrl.indexOf("?")<0)
	           nodeUrl+="?random=1";
			nodeUrl = nodeUrl.replace(".js?","?");
			var match = nodeUrl.split(RegExp("[?&]"));
			var config = {};
			for(var i=1;i<match.length;i++){
			    var p = match[i].split("=");
			    if(p[0]!='random')
			      config[p[0]]=p[1];
			}
			nodeUrl = match[0];
			nodeUrl = nodeUrl.replace(RegExp('/','g'),".");
			var grid = ClassCreate(nodeUrl,config);
		    grid.openFormWin(id,null,true,null,null,'view');
		}
	}
});