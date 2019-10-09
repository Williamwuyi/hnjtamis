/**
 * 下拉树控件
 */
Ext.define('modules.hnjtamis.exampaper.ExamUserManage', {
    extend : 'Ext.Panel',
	border : false,
	frame : true,
	bodyStyle : "border:0;padding:0px 0px 0px 0px;overflow-x:hidden; overflow-y:auto",
	defaultType : 'textfield',
	margins : '0 0 0 0',
	colspan : 2,
	height:200,
	forceFit : true,// 自动填充列宽,根据宽度比例
	allowMaxSize : -1,// 允许最大行数,-1为不受限制
	layout: 'border',
    defaults: {
        //split: true,                 //是否有分割线
        //collapsible: true,           //是否可以折叠
        bodyStyle: 'padding:0px'
    },
	initComponent : function() {
		//Ext.QuickTips.init();
		String.prototype.trim = function()
		{
			return this.replace( /(^\s*)|(\s*$)/g, '' ) ;
		};
		
		var me = this;
		me.selectEmpIds =  me.selectEmpIds || '';
		me.nodeId = base.Login.userSession.currentOrganId;
		me.organId = base.Login.userSession.currentOrganId;
		me.organName = base.Login.userSession.currentOrganName;
		me.nodeType = "organ";
		
		
		
		
		me.startDate = Ext.create("Ext.form.DateField",{
			fieldLabel : '&nbsp;&nbsp;&nbsp;有效期(≥)',
			name : 'startDate',
			xtype : 'datefield',
			format : 'Y-m-d',
			labelWidth:100,
			width:220,
			value : Ext.util.Format.date(Ext.Date.add(new Date(),Ext.Date.MONTH,0),"Y-m-d")
		});
		
		me.endDate = Ext.create("Ext.form.DateField",{
			fieldLabel : '&nbsp;&nbsp;&nbsp;有效期(≤)',
			name : 'endDate',
			xtype : 'datefield',
			format : 'Y-m-d',
			labelWidth:100,
			width:220,
			value : Ext.util.Format.date(Ext.Date.add(new Date(),Ext.Date.MONTH,12),"Y-m-d")//Ext.Date.format(new Date(),'Y-m-d')
		});
		
		me.deptTreeGrid = me.getDeptTreeGrid();
		me.examUser = me.getExamUser();
		me.examTjxx  = me.getExamTjxx();
		
		me.termform = new Ext.Toolbar({
			columnSize : 4,
			fieldDefaults:{
				labelAlign:'right',
				labelWidth:70
			},
			border : false,
			frame : false,
			defaultType : 'textfield',
			margins : '2 2 2 2',
			items : [me.startDate,
					 me.endDate,
					Ext.create("Ext.Button", {
						icon : 'resources/icons/fam/zoom.png',//按钮图标
						text : '查询',
						handler : function() {
							me.queryDeptTree();
						}
					}),
					'->',
					{
						xtype : 'button',//按钮类型
						iconCls : 'exportXls',
						text : "导出Excel",//按钮标题
						handler : function() {//按钮事件
							var url = "exam/exampaper/toExcelForExamUserManageExcelAction!toExcel.action?qid=" 
							+ (base.Login.userSession.currentOrganId) 
							+ "&qtype=organ&qstartDay="+me.formatDate(me.startDate.getValue())
							+"&qendDay="+me.formatDate(me.endDate.getValue());
							window.open(url+"&random="+Math.random());
						}
					
					}
					]
		});
		me.titleMsg = new Ext.Panel({
			//frame:false,//渲染面板
			//collapsible : false,//允许展开和收缩
			//autoScroll : false,//自动显示滚动条
			region: 'south',
			html:'提示：请选择一个岗位。',
			height: 30,//设置面板的高度
			bodyStyle:'background-color:#FFFFFF;padding:2px;'//设置面板体的背景色
		});
		
		me.selectPanel = new Ext.Panel({ 
			 region : 'center',
			 frame : true,
			 bodyStyle : "border:0;padding:0px 0px 0px 0px;overflow-x:hidden; overflow-y:auto",
			 defaultType : 'textfield',
			 margins : '0 0 0 0',
			 colspan : 1,
			 forceFit : true,// 自动填充列宽,根据宽度比例
			 allowMaxSize : -1,// 允许最大行数,-1为不受限制
			 layout: 'border',
			 items : [ 
		    	  me.examTjxx,
		    	  me.examUser
	    	 ]
	    });
		me.items = [
			{
		        region: 'north', //子元素的方位：north、west、east、center、south
		        xtype: "panel",
		        items : [me.termform]
	    	}, 
	    	me.deptTreeGrid,
	    	me.selectPanel,
	    	me.titleMsg
	    ];
		me.callParent();
	},
	getDeptTreeGrid : function(){
		var me = this;
		me.deptTreestore = Ext.create('Ext.data.TreeStore',{
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
				url : 'base/employeeTree/queryPxEmployeeDeptTreeForEmployeeTreeListAction!queryPxEmployeeDeptTree.action?organId='+base.Login.userSession.currentOrganId,
				//url : 'organization/quarter/odqtreeForQuarterListAction!odqtree.action',
				reader : {
					type : 'json'
				}
			},
			nodeParam : 'parentId',
			defaultRootId : ''
		});
		me.deptTree = new Ext.tree.TreePanel({
				store : me.deptTreestore,
				//id: 'leftTree',
				title : '机构部门岗位',
				region: 'west',
				split : false,
				collapsible : false,
				rootVisible : false,
				useArrows : true,
				autoScroll : true,
				width : 260,
				minSize : 80,
				maxSize : 200,
				listeners : {
					itemclick : function(view, record, item, index, e) {
						me.nodeType = record.raw.type;
						me.nodeId = "";
						me.nodeName = record.raw.title;
						if(me.nodeType =='dept' || me.nodeType =='quarter' || me.nodeType =='organ'){
							me.nodeId = record.raw.id;
							if(me.nodeType =='organ'){
								me.organId = record.raw.id;
								me.organName = record.raw.title;
								me.setExamUserTjxxGridTitle();
							}else if(me.nodeType =='dept' || me.nodeType =='quarter'){
								me.nodeName = record.raw.title;
								me.setExamUserTjxxGridTitle();
							}
						}
						me.queryDeptTree();
					}
				}
		});
		me.deptTreestore.on("load", function(mystore,node,success) {
		    var record = mystore.getRootNode().childNodes[0];
		    record.expand();
		    me.deptTree.getView().refresh();
		});
		
		me.deptTreestore.getRootNode().expand(false,false);
		return me.deptTree;
	},
	getExamTjxx : function(){
		var me = this;
		me.examUserTjxxStore = new Ext.data.Store({
					autoLoad : true,
					fields : [{
						    name : 'sortIndex',
						    type : 'string'
						 },{
						    name : 'organId',
						    type : 'string'
						 },{
							name : 'organName',
							type : 'string'
						 },{
							name : 'deptId',
							type : 'string'
						 },{
							name : 'deptName',
							type : 'string'
						 },{
							name : 'showDeptName',
							type : 'string'
						 },{
							name : 'passCount',
							type : 'string'
						 },{
					        name : 'unPassCount',
						    type : 'string'
					     },{
					        name : 'zzxxrsCount',
						    type : 'string'
					     },{
							name : 'passStateCount',
							type : 'string'
						 },{
						 	name : 'xxrsCount',
							type : 'string'
						 }],
					proxy : {
						type : 'ajax',
						actionMethods : "POST",
						timeout: 120000,
						url : "exam/exampaper/queryExamTjxxForExampaperListAction!queryExamTjxx.action?qid=" 
						+ (base.Login.userSession.currentOrganId) + "&qtype=organ&qstartDay="+me.formatDate(me.startDate.getValue())+"&qendDay="+me.formatDate(me.endDate.getValue()),
	                    reader : {
							type : 'json',
							root : 'examUserList',
							totalProperty : "examUserTotal"
						}
					},
					remoteSort : false
				});
				
		me.examUserTjxxGrid = new Ext.grid.GridPanel({
				autoScroll :true,
				height:'40%',
				autoHeight:true,
				store : me.examUserTjxxStore,
				stripeRows:true, //斑马线效果  
				//columnLines : true,
				forceFit: true,
				//selType: 'cellmodel',  
				title: '部门达标情况',
				//plugins:[  
				  //Ext.create('Ext.grid.plugin.CellEditing',{  
				   // clicksToEdit:0 //设置单击单元格编辑  
				  //})  
				//],  
				region: 'north',
				//bodyStyle:'border:0;',
				columns:[
					{
					 header:'选择',
					 dataIndex:'employeeIdChk',
					 sortable:false,
					 menuDisabled:true,
					 align:'center',
					 width:5,
					 xtype: 'checkcolumn',
					 hidden:true
					},{
					 header:'序号',
					 dataIndex:'sortIndex',
					 sortable:false,
					 menuDisabled:true,
					 align:'center',
					 width:5
					},{
					 dataIndex:'deptId',
					 hidden:true
					},{
					 dataIndex:'deptName',
					 hidden:true
					},{
			    	 dataIndex : 'showDeptName',
			    	 header:"部门",
					 width : 15,
					 sortable:false,
					 menuDisabled:true,
					 titleAlign:"center",
					 renderer:function(value, metadata, record){
					 	 var deptName = record.get("deptName");
						 if(deptName!=undefined && deptName!=null && deptName!=""){
						    metadata.tdAttr = " data-qtip = '"+deptName+"'";
						 }
						 return value;
					 }
		    		},{
				      dataIndex : 'passStateCount',
					  header : '总人数',
					  sortable:false, 
					  menuDisabled:true,
					  width:7,
					  align:"center",
					  renderer:function(value, metadata, record){
						 if(value!=undefined && value!=null && value!="" && Number(value)>0){
						    metadata.tdAttr = " data-qtip = '"+value+"'";
						 	 return "<font color=blue>"+value+"</font>";
						 }else{
						 	return value;
						 }
					 }
				    },{
				      dataIndex : 'xxrsCount',
					  header : '已完成学习人数',
					  sortable:false, 
					  menuDisabled:true,
					  width:7,
					  align:"center",
					  renderer:function(value, metadata, record){
						 if(value!=undefined && value!=null && value!="" && Number(value)>0){
						    metadata.tdAttr = " data-qtip = '"+value+"'";
						 	 return "<font color=blue>"+value+"</font>";
						 }else{
						 	return value;
						 }
					 }
				    },{
				      dataIndex : 'zzxxrsCount',
					  header : '在学习人数',
					  sortable:false, 
					  menuDisabled:true,
					  width:7,
					  align:"center",
					  renderer:function(value, metadata, record){
						 if(value!=undefined && value!=null && value!="" && Number(value)>0){
						    metadata.tdAttr = " data-qtip = '"+value+"'";
						 	 return "<font color=blue>"+value+"</font>";
						 }else{
						 	return value;
						 }
					 }
				    },
				    {
				      dataIndex : 'passCount',
					  header : '达标人数',
					  sortable:false, 
					  menuDisabled:true,
					  align:"center",
					  width : 7 ,
					  renderer:function(value, metadata, record){
						 if(value!=undefined && value!=null && value!="" && Number(value)>0){
						    metadata.tdAttr = " data-qtip = '"+value+"'";
						    return "<font color=green>"+value+"</font>";
						 }else{
						  	return value;
						 }
						
					 }
				    },
				    {
				      dataIndex : 'unPassCount',
					  header : '未达标人数',
					  sortable:false, 
					  menuDisabled:true,
					  width:7,
					  align:"center",
					  renderer:function(value, metadata, record){
						 if(value!=undefined && value!=null && value!="" && Number(value)>0){
						    metadata.tdAttr = " data-qtip = '"+value+"'";
						    return "<font color=red>"+value+"</font>";
						 }else{
						 	return value;
						 }
						 
					  }
					 }
		    	],
		    	listeners:{
				   'itemdblclick' : function(grid,record,option){
					 
				   }
				}
	    });
	    me.setExamUserTjxxGridTitle();
    	me.examUserTjxxGrid.store.on("load", function() {
    		
		});
		
    	return me.examUserTjxxGrid;
	},
	
	getExamUser : function(){
		var me = this;
		me.examUserStore = new Ext.data.Store({
					autoLoad : true,
					fields : [{
						    name : 'sortIndex',
						    type : 'string'
						 },{
						    name : 'userTestpaperId',
						    type : 'string'
						 },{
							name : 'examId',
							type : 'string'
						 },{
							name : 'examName',
							type : 'string'
						 },{
							name : 'userName',
							type : 'string'
						 },{
							name : 'employeeId',
							type : 'string'
						 },{
					        name : 'employeeName',
						    type : 'string'
					     },{
							name : 'userOrganId',
							type : 'string'
						 },{
							name : 'userOrganName',
							type : 'string'
						 },{
							name : 'userDeptId',
							type : 'string'
						 },{
							name : 'userDeptName',
							type : 'string'
						 },{
							name : 'quarterName',
							type : 'string'
						 },{
							name : 'quarterId',
							type : 'string'
						 },{
							name : 'passState',
							type : 'string'
						 },{
							name : 'scoreStartTime',
							type : 'string'
						 },{
							name : 'scoreEndTime',
							type : 'string'
						 },{
							name : 'publicTime',
							type : 'string'
						 }],
					proxy : {
						type : 'ajax',
						actionMethods : "POST",
						timeout: 120000,
						url : "exam/exampaper/queryExamUserForExampaperListAction!queryExamUser.action?qid=" 
							+ (base.Login.userSession.currentOrganId) + "&qtype=organ&qstartDay="+me.formatDate(me.startDate.getValue())+"&qendDay="+me.formatDate(me.endDate.getValue()),
	                    reader : {
							type : 'json',
							root : 'examUserList',
							totalProperty : "examUserTotal"
						}
					},
					remoteSort : false
				});
				
		me.nodeId = base.Login.userSession.currentOrganId;
		me.nodeType = "organ";
				
		me.examUserGrid = new Ext.grid.GridPanel({
				autoScroll :true,
				height:'60%',
				autoHeight:true,
				store : me.examUserStore,
				stripeRows:true, //斑马线效果  
				//columnLines : true,
				forceFit: true,
				//selType: 'cellmodel',  
				title: '达标考试成绩 - [ '+(base.Login.userSession.currentOrganName)+' ]',
				//plugins:[  
				  //Ext.create('Ext.grid.plugin.CellEditing',{  
				   // clicksToEdit:0 //设置单击单元格编辑  
				  //})  
				//],  
				region: 'south',
				//bodyStyle:'border:0;',
				columns:[
					{
					 header:'选择',
					 dataIndex:'employeeIdChk',
					 sortable:false,
					 menuDisabled:true,
					 align:'center',
					 width:5,
					 xtype: 'checkcolumn',
					 hidden:true
					},{
					 header:'序号',
					 dataIndex:'sortIndex',
					 sortable:false,
					 menuDisabled:true,
					 align:'center',
					 width:8
					},
					{
					 dataIndex:'userTestpaperId',
					 hidden:true
					},{
					 dataIndex:'employeeId',
					 hidden:true
					},{
			    	 dataIndex : 'userName',
			    	 header:"人员",
					 width : 12,
					 sortable:false,
					 menuDisabled:true,
					 align:"center",
					 renderer:function(value, metadata, record){
						 if(value!=undefined && value!=null && value!=""){
						    metadata.tdAttr = " data-qtip = '"+value+"'";
						 }
						 return value;
					 }
		    		},{
		    		 dataIndex : 'userOrganName',
					 header : '机构',
					 align:'center',
					 width : 15 ,
					 sortable:false,
					 menuDisabled:true,
					 align:"center",
					 renderer:function(value, metadata, record){
						 if(value!=undefined && value!=null && value!=""){
						    metadata.tdAttr = " data-qtip = '"+value+"'";
						 }
						 return value;
					 }
					},
					{dataIndex : 'userDeptName',
					 header : '部门',
					 align:'center',
					 width : 12 ,
					 sortable:false,
					 menuDisabled:true,
					 align:"center",
					 renderer:function(value, metadata, record){
						 if(value!=undefined && value!=null && value!=""){
						    metadata.tdAttr = " data-qtip = '"+value+"'";
						 }
						 return value;
					 }
					},
					{header:"岗位",
					 dataIndex:"quarterName",
					 sortable:false, 
					 menuDisabled:true,
					 width:12,
					 align:"center",
				     renderer:function(value, metadata, record){
				    	 if(value!=undefined && value!=null && value!=""){
						    metadata.tdAttr = " data-qtip = '"+value+"'";
						 }
				    	 //metadata.style="white-space: normal !important;";
				    	 return value;
				     }
				    },
				    {header:"考试科目",
					 dataIndex:"examName",
					 sortable:false, 
					 menuDisabled:true,
					 width:15,
					 align:"center",
				     renderer:function(value, metadata, record){
				    	 if(value!=undefined && value!=null && value!=""){
						    metadata.tdAttr = " data-qtip = '"+value+"'";
						 }
				    	 //metadata.style="white-space: normal !important;";
				    	 return value;
				     }
				    },
				    {
				      dataIndex : 'passState',
					  header : '是否合格',
					  sortable:false, 
					  menuDisabled:true,
					  width:10,
					  align:"center",
					  renderer:function(value, metadata, record){
				    	 if(value == 'T'){
				    	 	return "<font color=green>合格</font>";
				    	 }else  if(value == 'F'){
				    	 	return "<font color=red>不合格</font>";
				    	 }else{
				    	 	return "";
				    	 }
				    	 //metadata.style="white-space: normal !important;";
				    	 return value;
				     }
				    },
				    {
				      dataIndex : 'scoreStartTime',
					  header : '有效期（开始）',
					  sortable:false, 
					  menuDisabled:true,
					  align:"center",
					  width : 12 ,
					  renderer:function(value, metadata, record){
						 if(value!=undefined && value!=null && value!=""){
						    metadata.tdAttr = " data-qtip = '"+value+"'";
						 }
						 return value;
					 }
				    },
				    {
				      dataIndex : 'scoreEndTime',
					  header : '有效期（结束）',
					  sortable:false, 
					  menuDisabled:true,
					  align:"center",
					  width : 12 ,
					  renderer:function(value, metadata, record){
						 if(value!=undefined && value!=null && value!=""){
						    metadata.tdAttr = " data-qtip = '"+value+"'";
						 }
						 return value;
					 }
				    },
				    {
				      dataIndex : 'publicTime',
					  header : '发布时间',
					  sortable:false, 
					  menuDisabled:true,
					  width:12,
					  align:"center",
					  renderer:function(value, metadata, record){
						 if(value!=undefined && value!=null && value!="" && value.length>10){
						    metadata.tdAttr = " data-qtip = '"+value+"'";
						     return value.substring(0,10);
						 }else{
						 	 return value;
						 }
						
					 }
				    }
		    	],
		    	listeners:{
				   'itemdblclick' : function(grid,record,option){
					 
				   }
				}
	    });
    	me.examUserGrid.store.on("load", function() {
    		
		});
		
    	return me.examUserGrid;
	},
	setExamUserTjxxGridTitle : function(){
		var me = this;
		if(me.nodeType =='organ'){
			me.examUserTjxxGrid.setTitle("部门达标情况 - [ "+me.organName+" ]");
		}else if(me.nodeType =='dept'){
			me.examUserTjxxGrid.setTitle("部门达标情况 - [ "+me.nodeName+" ]");
		}else if(me.nodeType =='quarter'){
			var deptIdArr = me.nodeId.split("@");
			EapAjax.request( {
				method : 'GET',
				url : 'organization/dept/findForDeptFormAction!find.action?id=' + (deptIdArr.length>1?deptIdArr[1] :me.nodeId),
				async : true,
				success : function(response) {
					var result = Ext.decode(response.responseText);
					if(result.form) {
						me.examUserTjxxGrid.setTitle("部门达标情况 - [ "+result.form.deptName+" ]");	
					}
				},
				failure : function() {
					me.examUserTjxxGrid.setTitle("部门达标情况 ");
				}
			});
			
			
		}
	},
	queryDeptTree : function(){
		var me = this;
		
		//me.nodeType =='dept' || me.nodeType =='quarter' || me.nodeType =='organ'
		var url1 = "";
		
		if(me.nodeType =='organ'){
			url1 = "exam/exampaper/queryExamTjxxForExampaperListAction!queryExamTjxx.action?qid=" 
				+ (me.nodeId) + "&qexamId=&qstartDay="+me.formatDate(me.startDate.getValue())+"&qendDay="+me.formatDate(me.endDate.getValue());
		}else if(me.nodeType =='dept'){
			url1 = "exam/exampaper/queryExamInTopDeptTjxxForExampaperListAction!queryExamInTopDeptTjxx.action?qid=" 
				+ (me.nodeId) + "&qexamId=&qstartDay="+me.formatDate(me.startDate.getValue())+"&qendDay="+me.formatDate(me.endDate.getValue());
		}else if(me.nodeType =='quarter'){
			var deptIdArr = me.nodeId.split("@");
			//url1 = "exam/exampaper/queryExamInDeptTjxxByQuarterForExampaperListAction!queryExamInDeptTjxxByQuarter.action?qid=" 
				//+ (deptIdArr.length>1?deptIdArr[1] :me.nodeId) + "&qexamId=&qstartDay="+me.formatDate(me.startDate.getValue())+"&qendDay="+me.formatDate(me.endDate.getValue());
			url1 = "exam/exampaper/queryExamInTopDeptTjxxForExampaperListAction!queryExamInTopDeptTjxx.action?qid=" 
				+ (deptIdArr.length>1?deptIdArr[1] :me.nodeId) + "&qexamId=&qstartDay="+me.formatDate(me.startDate.getValue())+"&qendDay="+me.formatDate(me.endDate.getValue());
		}
		me.examUserTjxxGrid.store.proxy.url = 	url1;						
		me.examUserTjxxGrid.store.load();
		
		
		var url = "exam/exampaper/queryExamUserForExampaperListAction!queryExamUser.action?qid=" 
			+ (me.nodeId) + "&qtype="+(me.nodeType)+"&qexamId=&qstartDay="+me.formatDate(me.startDate.getValue())+"&qendDay="+me.formatDate(me.endDate.getValue());
		me.examUserGrid.store.proxy.url = 	url								
		me.examUserGrid.store.load();
		me.examUserGrid.setTitle("达标考试成绩 - [ "+me.nodeName+" ]");
	},
	formatDate : function (value){
		return Ext.Date.format(value,'Y-m-d')
	}
});