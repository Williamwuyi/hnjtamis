/**
 * 待办事宜列表类
 */
ClassDefine('modules.workflow.TodoList', {
	extend : 'base.model.List',
	requires:['modules.workflow.MessageTip'],
	enableCheck:false,
	initComponent : function() {
		var me = this;
		// 模块列表对象
		this.columns = [{
		            name : 'excuteId',
					width : 0
				}, {
					name : 'createTime',
					header : '创建时间',
					width : 1
				}, {
					name : 'flowName',
					header : '业务名称',
					width : 2
				}, {
					name : 'param.title',
					header : '业务标题',
					width : 4
				}, {
					name : 'taskName',
					header : '任务名称',
					width : 2
				},{name:'param',width:0},
				  {name:'workFlowNode',width:0}];
	    if(this.complet=='true'){
	       this.columns.push({width:1,name:'toName',header:'操作结果'});
	    }
		this.keyColumnName = "excuteId";// 主健属性名
		this.viewOperater = false;
		this.addOperater = false;
		this.deleteOperater = false;
		this.updateOperater = false;
		this.readerRoot = 'list';
		this.listUrl = "workflow/testListForFlowTestAction!testList.action?employeeId="
		           +base.Login.userSession.employeeId+"&complet="+this.complet;// 列表请求地址
		this.otherOperaters = [];//其它扩展按钮操作
		this.callParent();
	},
	listeners:{
		'cellclick':function(thisGrid,td,cellIndex,record,tr,rowIndex,e,opts){
			var me = this;
			var eapMaskTip = EapMaskTip(document.body);
			var excuteId = record.data.excuteId;
			var id = record.data.param.id;
			var title = record.data.param['title'];
			var taskCode = record.data.workFlowNode.code;
			var nodeUrl = record.data.workFlowNode.url;
			if(!nodeUrl){
				Ext.Msg.alert('错误', '任务无打开页面！'); 
				eapMaskTip.hide();
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
			if(this.complet=="true"){
				var grid = ClassCreate(nodeUrl,config);
				grid.openFormWin(id,null,true,null,null,'view');
				eapMaskTip.hide();
			}else{
				var url="workflow/urlForFlowTestAction!url.action?id="+id+"&employeeId="+base.Login.userSession.employeeId
				                +"&taskCode="+taskCode+"&random="+Math.random();
				EapAjax.request({method : 'GET',url : url,async : true,success:function(response){
		    		var ret = Ext.decode(response.responseText);
		    		if(Ext.isArray(ret)&&undefined!=ret[0].errors){
			    		Ext.Msg.alert('错误', ret[0].errors);
			    		eapMaskTip.hide();
		    		}else{
		    			var excuteId=ret.taskUrl.split("excuteId=")[1];
	    				var flowConfig = {title:record.data.workFlowNode.name,excuteId:excuteId,
					    				  buttons:ret.param,url:ret.taskUrl};
					    var grid = ClassCreate(nodeUrl,config);
	    				grid.openFormWin(id,function(submitData){
	    					//刷新原列表
	    					me.termQueryFun(true,'flash');				    					
	    				},false,null,null,'flow',flowConfig);
	    				eapMaskTip.hide();
		    		}						    		   
		    	}});
			}
		}
	}
});