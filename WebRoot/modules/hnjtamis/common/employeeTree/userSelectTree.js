/**
 * 下拉树控件
 */
Ext.define('modules.hnjtamis.common.employeeTree.userSelectTree', {
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

		/*me.alertSelectMask = new Ext.LoadMask(me, {  
		    msg     : '数据正在处理,请稍候',  
		    removeMask  : true// 完成后移除  
		});
		me.alertSelectMask.show();*/
		
		me.deptTreeGrid = me.getDeptTreeGrid();
		me.unSelectEmp = me.getUnSelectEmp();
		me.selectEmp = me.getSelectEmp();
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
			items : [
					'->',
					Ext.create("Ext.Button", {
						icon : 'resources/icons/fam/add.gif',//按钮图标
						text : '确定选择',
						handler : function() {
							var addList = [];
							var empListGridStore = me.empListGrid.store;
							for(var i=0;i<empListGridStore.getCount();i++){
								var record = empListGridStore.getAt(i);
								var newrecord = record.copy();
								addList.push(newrecord);
							 }
							 me.callBackFun(addList);
						}
					}),
					Ext.create("Ext.Button",{
						text : eap_operate_close,
						iconCls : 'close',
						handler : function() {
							me.closeFun();
						}
					})
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
		    	  me.unSelectEmp,
		    	  {
			        region: 'center',
			        xtype: "panel",
			        //html: "子元素2",
			        //collapsible : false,
			        //titleCollapse : false,
			        bodyStyle : "border:0;padding:5px 5px 5px 5px;text-align:center;",
			        items : [
			        	Ext.create("Ext.Button", {
							icon : 'resources/icons/fam/down_1.png',//按钮图标
							text : '选择员工',
							tooltip: '选择选中的人员',
							handler : function() {	
								me.selectChkEmp(me.unEmpListGrid.store,me.empListGrid.store,false);
							}
						}),
						Ext.create("Ext.form.Label",{
							text: ' ',
							style : 'padding-left:25px'
						}),
						Ext.create("Ext.Button",{
							icon : 'resources/icons/fam/down_2.png',//按钮图标
							text : '全部员工',
							tooltip : '选择全部人员',
							handler : function() {
								me.selectAllEmp(me.unEmpListGrid.store,me.empListGrid.store,false);
							}
						}),
						Ext.create("Ext.form.Label",{
							text: ' ',
							style : 'padding-left:100px'
						}),
						Ext.create("Ext.Button", {
							icon : 'resources/icons/fam/up_1.png',//按钮图标
							text: '取消选择',
							tooltip : '取消选中的人员',
							handler : function() {	
								me.selectChkEmp(me.empListGrid.store,me.unEmpListGrid.store,true);
							}
						}),
						Ext.create("Ext.form.Label",{
							text: '',
							style : 'padding-left:25px'
						}),
						Ext.create("Ext.Button",{
							icon : 'resources/icons/fam/up_2.png',//按钮图标
							text: '取消全部',
							tooltip : '取消全部的人员',
							handler : function() {
								me.selectAllEmp(me.empListGrid.store,me.unEmpListGrid.store,true);
							}
						})
					]
		    	  },
		    	  me.selectEmp
	    	  ]
	    });
		me.items = [
			{
		        region: 'north', //子元素的方位：north、west、east、center、south
		        //collapsible : false,
		        //titleCollapse : false,
		        xtype: "panel",
		        //html: "子元素1",
		        //height: 70,
		        items : [me.termform]
	    	}, 
	    	me.deptTreeGrid,
	    	me.selectPanel,
	    	me.titleMsg
	    	/*{
		        region: 'south',
		        //title: '南',
		        xtype: "panel",
		        //html: "子元素4",
		        height: 30,
		        item : []
	    	} */
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
				url : 'base/employeeTree/queryEmployeeDeptTreeForEmployeeTreeListAction!queryEmployeeDeptTree.action?organId='+base.Login.userSession.currentOrganId,
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
						if(me.nodeType =='dept' || me.nodeType =='quarter'){
							me.nodeId = record.raw.id;
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
	getUnSelectEmp : function(){
		var me = this;
		me.unSelectStore = new Ext.data.Store({
					autoLoad : false,
					fields : [{
						    name : 'employeeId',
						    type : 'string'
						 },{
							name : 'employeeName',
							type : 'string'
						 },{
							name : 'organName',
							type : 'string'
						 },{
							name : 'deptName',
							type : 'string'
						 },{
							name : 'quarterName',
							type : 'string'
						 },{
					        name : 'userSex',
						    type : 'string'
					     },{
							name : 'userBirthday',
							type : 'string'
						 },{
							name : 'idNumber',
							type : 'string'
						 },{
							name : 'userNation',
							type : 'string'
						 },{
							name : 'userPhone',
							type : 'string'
						 },{
							name : 'userAddr',
							type : 'string'
						 },{
							name : 'organId',
							type : 'string'
						 },{
							name : 'deptId',
							type : 'string'
						 },{
							name : 'quarterId',
							type : 'string'
						 }],
					proxy : {
						type : 'ajax',
						actionMethods : "POST",
						timeout: 120000,
						url : 'base/employeeTree/queryUnSelectEmployeeForEmployeeTreeListAction!queryUnSelectEmployee.action',
	                    reader : {
							type : 'json',
							root : 'unSelectEmpList',
							totalProperty : "unSelectEmpTotal"
						}
					},
					remoteSort : false
				});
		me.unEmpListGrid = new Ext.grid.GridPanel({
	    	 	//colspan : 2,
				//fieldLabel : ' ',
				//xtype : 'editlist',
				//viewConfig:{autoScroll:true,height:300},//高度
				//addOperater : false,
				//deleteOperater : true,
				//enableMoveButton : true,
				//enableCheck : true,
				//readOnly : false,
				//otherOperaters:[],
				//store : me.unSelectStore,
				autoScroll :true,
				height:200,
				autoHeight:true,
				store : me.unSelectStore,
				stripeRows:true, //斑马线效果  
				//columnLines : true,
				forceFit: true,
				//selType: 'cellmodel',  
				title: '可以选择的员工[按机构部门岗位筛选]',
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
					 width:10,
					 xtype: 'checkcolumn'
					},
					{
					 dataIndex:'employeeId',
					 hidden:true
					},{
			    	 dataIndex : 'employeeName',
			    	 header:"人员",
					 width : 15,
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
		    		{dataIndex : 'organName',
					 header : '机构',
					 align:'center',
					 width : 30 ,
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
					{dataIndex : 'deptName',
					 header : '部门',
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
					{header:"岗位",
					 dataIndex:"quarterName",
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
				      dataIndex : 'userSex',
					  header : '性别',
					  hidden: true,
					  width : 15 
				    },
				    {
						dataIndex : 'userBirthday',
						header : '出生年月',
						hidden: true,
					  width : 15 
					},
					{
						dataIndex : 'idNumber',
						header : '身份证号',
						hidden: true,
					  width : 15 
					},
					{
						dataIndex : 'userNation',
						header : '民族',
						hidden: true,
					  width : 15 
					},
					{
						dataIndex : 'userPhone',
						header : '联系电话',
						hidden: true,
					  width : 15 
					},
					{
						dataIndex : 'userAddr',
						header : '住址',
						hidden: true,
					  width : 15 
					},{
						dataIndex : 'organId',
						hidden: true
					},{
						dataIndex : 'deptId',
						hidden: true
					},{
						dataIndex : 'quarterId',
						hidden: true
					}
		    	],
		    	listeners:{
				   'itemdblclick' : function(grid,record,option){
					  me.empListGrid.store.insert(me.empListGrid.store.getCount(), record);
					  grid.store.remove(record);
				   }
				}
	    });
    	me.unEmpListGrid.store.on("load", function() {
    		
		});
		
    	return me.unEmpListGrid;
	},
	getSelectEmp : function(){
		var me = this;
		me.selectStore = new Ext.data.Store({
					autoLoad : true,
					fields : [{
						    name : 'employeeId',
						    type : 'string'
						 },{
							name : 'employeeName',
							type : 'string'
						 },{
							name : 'organName',
							type : 'string'
						 },{
							name : 'deptName',
							type : 'string'
						 },{
							name : 'quarterName',
							type : 'string'
						 },{
					        name : 'userSex',
						    type : 'string'
					     },{
							name : 'userBirthday',
							type : 'string'
						 },{
							name : 'idNumber',
							type : 'string'
						 },{
							name : 'userNation',
							type : 'string'
						 },{
							name : 'userPhone',
							type : 'string'
						 },{
							name : 'userAddr',
							type : 'string'
						 },{
							name : 'organId',
							type : 'string'
						 },{
							name : 'deptId',
							type : 'string'
						 },{
							name : 'quarterId',
							type : 'string'
						 }],
					proxy : {
						type : 'ajax',
						actionMethods : "POST",
						timeout: 120000,
						url : 'base/employeeTree/querySelectEmployeeForEmployeeTreeListAction!querySelectEmployee.action?&selectEmpIds='+me.selectEmpIds,
	                    reader : {
							type : 'json',
							root : 'selectEmpList',
							totalProperty : "selectEmpTotal"
						}
					},
					remoteSort : false
				});
		me.empListGrid = new Ext.grid.GridPanel({
				autoScroll :true,
				height: 318,
				autoHeight:true,
				store : me.selectStore,
				stripeRows:true, //斑马线效果  
				forceFit: true,
				title: '您已经选择的员工',  
				region: 'south',
				columns:[
					{
					 header:'选择',
					 dataIndex:'employeeIdChk',
					 sortable:false,
					 menuDisabled:true,
					 align:'center',
					 width:10,
					 xtype: 'checkcolumn'
					},
					{
					 dataIndex:'employeeId',
					 hidden:true
					},{
			    	 dataIndex : 'employeeName',
			    	 header:"人员",
					 width : 15,
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
		    		{dataIndex : 'organName',
					 header : '机构',
					 align:'center',
					 width : 30 ,
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
					{dataIndex : 'deptName',
					 header : '部门',
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
					{header:"岗位",
					 dataIndex:"quarterName",
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
				      dataIndex : 'userSex',
					  header : '性别',
					  hidden: true,
					  width : 15 
				    },
				    {
						dataIndex : 'userBirthday',
						header : '出生年月',
						hidden: true,
					    width : 15 
					},
					{
						dataIndex : 'idNumber',
						header : '身份证号',
						hidden: true,
					    width : 15 
					},
					{
						dataIndex : 'userNation',
						header : '民族',
						hidden: true,
					    width : 15 
					},
					{
						dataIndex : 'userPhone',
						header : '联系电话',
						hidden: true,
					    width : 15 
					},
					{
						dataIndex : 'userAddr',
						header : '住址',
						hidden: true,
					    width : 15 
					},{
						dataIndex : 'organId',
						hidden: true
					},{
						dataIndex : 'deptId',
						hidden: true
					},{
						dataIndex : 'quarterId',
						hidden: true
					}
		    	],
		    	listeners:{
				   'itemdblclick' : function(grid,record,option){
					  //me.unEmpListGrid.store.insert(me.unEmpListGrid.store.getCount(), record);
					  grid.store.remove(record);
					  me.flushSelectEmpIds();
					  me.queryDeptTree();
				   }
				}
	    });
    	me.empListGrid.store.on("load", function() {
    		me.flushSelectEmpIds();
		});
    	return me.empListGrid;
	},
	//将选择的员工从 sourceStore -> targetStore ，并从sourceStore清除
	selectChkEmp : function(sourceStore,targetStore,isFlushFlag){
		var me = this;
		var len = sourceStore.getCount();
		var dellist = new Array();
		for(var i=0;i<len;i++){
			var record = sourceStore.getAt(i);
			var employeeIdChk = record.get("employeeIdChk");
			if(employeeIdChk){
				record.set("employeeIdChk",false);
				if(!isFlushFlag)targetStore.insert(targetStore.getCount(), record);
				dellist[dellist.length] = i;
			}
		}
		if(dellist.length == 0){
			Ext.Msg.alert('错误', "您最少需要选择一个员工！");
		}else{
			for(var i=dellist.length-1;i>=0;i--){
				sourceStore.removeAt(dellist[i]);
			}
			if(isFlushFlag){
				me.flushSelectEmpIds();
				me.queryDeptTree();
			}
		}
	},
	//全选员工从 sourceStore -> targetStore ，并从sourceStore清除
	selectAllEmp : function(sourceStore,targetStore,isFlushFlag){
		var me = this;
		if(isFlushFlag){//主要用于对上面未选的进行处理
			sourceStore.removeAll();
			me.flushSelectEmpIds();
			me.queryDeptTree();
		}else{
			var len = sourceStore.getCount();
			for(var i=0;i<len;i++){
				var record = sourceStore.getAt(i);
				targetStore.insert(targetStore.getCount(), record);
			}
			sourceStore.removeAll();
			me.flushSelectEmpIds();
		}
	},
	//根据部门员工树进行筛选未选中的员工
	queryDeptTree : function(){
		var me = this;
		var url = "base/employeeTree/queryUnSelectEmployeeForEmployeeTreeListAction!queryUnSelectEmployee.action?id=" 
			+ (me.nodeId) + "&type="+(me.nodeType)+"&selectEmpIds="+me.selectEmpIds;
		me.unEmpListGrid.store.proxy.url = 	url								
		me.unEmpListGrid.store.load();
	},
	flushSelectEmpIds : function(){
		var me = this;
		var sourceStore = me.empListGrid.store;
		me.selectEmpIds = "";
		var len = sourceStore.getCount();
		for(var i=0;i<len;i++){
			var record = sourceStore.getAt(i);
			me.selectEmpIds+=record.get("employeeId")+",";
		}
	}
});