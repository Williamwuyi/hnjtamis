/**
 * 工作流维护功能的表单类
 */
ClassDefine('modules.workflow.Form', {
	extend : 'base.model.Form',	
	initComponent : function() {
		    var nodetype=[['START','开始'],['TASK','任务'],['JUDGE','判断'],['FORK','分支'],['DYNAMIC_FORK','动态分支'],
		                  ['JOIN','聚合'],['AUTO','自动'],['SUB_FLOW','子流程'],['END','结束']];
		    var excuteType = [[1,'手动选定'],[2,'前执行者上级岗位'],[3,'流程创建者'],[4,'参数指定']];
			var formConfig = this;
			formConfig.formUrl = "workflow/saveForWorkFlowFormAction!save.action";
			formConfig.findUrl = "workflow/findForWorkFlowFormAction!find.action";
			formConfig.columnSize = 2;
			formConfig.fieldDefaults = {
				labelAlign : 'right',
				labelWidth : 90
			}
			formConfig.jsonParemeterName = 'form';
			formConfig.items=[];
			formConfig.items.push({
						colspan : 2,
						xtype : 'hidden',
						name : 'flowId'
					});
			formConfig.items.push({
						colspan : 1,
						fieldLabel : '流程名称',
						name : 'name',
						xtype : this.oper=='view'?'displayfield':'textfield',
						allowBlank : false,
						readOnly : this.readOnly,
						maxLength : 32
					});
			formConfig.items.push({
						colspan : 1,
						fieldLabel : '流程编码',
						name : 'code',
						xtype : this.oper=='view'?'displayfield':'textfield',
						allowBlank : false,
						readOnly : this.readOnly,
						maxLength : 32
					});
			formConfig.items.push({
						colspan : 1,
						fieldLabel : '服务名',
						name : 'serviceName',
						xtype : this.oper=='view'?'displayfield':'textfield',
						allowBlank : true,
						readOnly : this.readOnly,
						maxLength : 32
					});
			formConfig.items.push({
						colspan : 1,
						fieldLabel : '版本',
						name : 'version',
						xtype : this.oper=='view'?'displayfield':'numberfield',
						allowBlank : false,
						readOnly : true,
						maxLength : 2,
						value : 1
					});
			formConfig.items.push({
						colspan : 2,
						fieldLabel : '描述',
						name : 'that',
						xtype : this.oper=='view'?'displayfield':'textarea',
						allowBlank : true,
						readOnly : this.readOnly,
						maxLength : 200,
						width:740
					});
			//岗位选择框
		    var quarterSelect = Ext.widget('selecttree',{checked : true,nameKey : 'quarterId',
								nameLable : 'quarterName',
								//readerRoot : 'nodes',
								keyColumnName : 'id',
								addPickerWidth : 200,
								allowBlank : true,
								levelAffect:'00',
								titleColumnName : 'title',
								childColumnName : 'children',
								selectType:'quarter',
								selectUrl : "organization/quarter/odqtreeForQuarterListAction!odqtree.action?organId="
								  +base.Login.userSession.organId,
								listeners:{collapse:function(){
									flowNode.getView().refresh();
								}}});
			
			var flowNode = Ext.widget('editlist',{
						colspan : 2,
						fieldLabel : '流程结点',
						name : 'workFlowNodes',
						addOperater : true,
						readOnly:this.readOnly,
						deleteOperater : true,
						viewConfig:{height:160},//高度
						columns : [{
									width : 0,
									name : 'nodeId'
								}, {
									name : 'code',
									header : '结点编码',
									editor : {
										xtype : 'textfield',
										allowBlank : false,
										name:'sub_code',
										maxLength : 32
									},
									width : 2
								}, {
									name : 'name',
									header : '结点名称',
									editor : {
										xtype : 'textfield',
										allowBlank : false,
										name:'sub_name',
										maxLength : 32
									},
									width : 3
								}, {
									name : 'type',
									header : '结点类型',
									editor : {
										xtype : 'select',
										allowBlank : false,
										data:nodetype,
										name:'sub_type'
									},
									width : 2,
									defaultValue:'START',//缺省为开始结点
									renderer:function(value){
										for(var i=0;i<nodetype.length;i++){
											if(value==nodetype[i][0])
											   return nodetype[i][1];
										}
								    }
								}, {
									name : 'url',
									header : '任务打开地址',
									editor : {
										xtype : 'textfield',
										allowBlank : true,
										maxLength : 200
									},
									width : 5
								}, {
									name : 'judgeExpress',
									header : '表达式',
									editor : {
										xtype : 'textfield',
										allowBlank : true,
										maxLength : 200
									},
									width : 4
								}, {
									name : 'excuteType',
									header : '执行者类型',
									editor : {
										xtype : 'select',
										allowBlank : false,
										data:excuteType
									},
									defaultValue:1,
									width : 2,
									renderer:function(value){
									    for(var i=0;i<excuteType.length;i++){
											if(value==excuteType[i][0])
											   return excuteType[i][1];
										}
								    }
								}, {
									header : '执行岗位选择',
									name : 'workFlowQuarters',
									editor:quarterSelect,
									width:5,
									renderer:function(value,cellmeta,record,rowIndex,columnIndex,store){
										var v = "";
										//自动换行
										cellmeta.style="white-space: normal !important;";
										Ext.Array.each(value,function(item){
											if(v=="") v = item['quarterName'];
											else v += ","+item['quarterName'];
										});
										return v;
									}
								}, {
									name : 'timer',
									header : '定时（分钟）',
									editor : {
										xtype : 'textfield',
										allowBlank : true,
									    regexText:"请输入有效数字", //无效数字提示
									    regex:/^[0-9]+$/,
										name:'sub_timer',
										maxLength : 32
									},
									width : 3
								}, {
									name : 'timerToCode',
									header : '定时导向编码',
									editor : {
										xtype : 'textfield',
										allowBlank : true,
										name:'sub_timerToCode',
										maxLength : 32
									},
									width : 3
								}, {
									name : 'orderNo',
									width : 0
								}],
						readOnly : this.readOnly
					});
		    formConfig.items.push(flowNode);
		    /*var getStoreData=function(){
		    	var s = flowNode.store;
		    	var data=[];
		    	for ( var i = 0; i < s.count(); i++){
		    		data.push(s.data.getAt(i).data);
		    	}
		    	return data;
		    }
		    var nodeSelect = Ext.widget('select',{
										valueField:'nodeId',
										displayField:'name',
										allowBlank : false,
										//data:getStoreData(),
										maxLength : 32,
										initComponent : function() {
		                                     var me = this;
		                                     me.data = getStoreData();
		                                     me.callParent();
										}
									});*/
		    formConfig.items.push({
						colspan : 2,
						fieldLabel : '结点导向',
						name : 'tos',
						readOnly:this.readOnly,
						xtype : 'editlist',
						addOperater : true,
						deleteOperater : true,
						viewConfig:{height:200},//高度
						columns : [{
									width : 0,
									name : 'toId'
								}, {
									name : 'srcNodeId',
									header : '源结点编码（入口）',
									editor : {
										xtype : 'textfield',
										allowBlank : false
									},
									width : 2
								}, {
									name : 'toNodeId',
									header : '目的结点编码（出口）',
									editor : {
										xtype : 'textfield',
										allowBlank : false
									},
									width : 2
								}, {
									name : 'code',
									header : '导向编码',
									editor : {
										xtype : 'textfield',
										allowBlank : false,
										name:'code2'
									},
									width : 2
								}, {
									name : 'name',
									header : '导向名称',
									editor : {
										xtype : 'textfield',
										allowBlank : false,
										maxLength : 200,
										name:'name2'
									},
									width : 3
								}, {
									name : 'state',
									header : '导向状态名',
									editor : {
										xtype : 'textfield',
										allowBlank : false,
										maxLength : 200
									},
									width : 3
								}],
						readOnly : this.readOnly
					});
			this.callParent();
		}
});