/**
 * 岗位标准管理
 */
ClassDefine('modules.hnjtamis.jobstandardEx.StandardTerms', {
	 extend : 'Ext.Panel',
	 requires : ['base.model.Tree','base.core.TableMergeCells'], 
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
     rfSTQHight : true,
	 initComponent : function() {
	 	String.prototype.trim = function()
		{
			return this.replace( /(^\s*)|(\s*$)/g, '' ) ;
		};
		var me = this;
		me.op = me.op || 'update';
		me.sf = me.sf || 'onlydcsf';//onlydcsf onlyall
		me.treeGrid = me.getStandardTreeGrid();
		me.standardTermList = me.getStandardTermList();
		me.standardTermQuarter = me.getStandardTermQuarter();
		
		me.standardnameTerm = new Ext.form.TextField({
			xtype : 'textfield',
			name  : 'standardnameTerm',
			fieldLabel : '子模块',
			labelWidth : 60,
			width:180
			
		});
		me.termform = new Ext.Toolbar({
			//columnSize : 4,
			fieldDefaults:{
				labelAlign:'right',
				labelWidth:70
			},
			border : false,
			frame : false,
			defaultType : 'textfield',
			region: 'north',
			margins : '2 2 2 2',
			items : [
					me.standardnameTerm,
					new Ext.Button({
						colspan : 1,
						iconCls : 'query',
						text:'查询',
						handler : function() {
							me.queryStandardTermList(me.nodeId,me.titleStr,me.nodeType);
						}
					}),
					'->',
					Ext.create("Ext.Button", {
						iconCls : 'view',
						text : eap_operate_view,
						resourceCode:me.viewResourceCode,
						handler : function() {
							me.openStandardTermFormWin(me.standardid,function(){me.queryStandardTermList(me.nodeId,me.titleStr,me.nodeType);}, false, 'view');
						}
					}),
					Ext.create("Ext.Button", {
						iconCls : 'add',
						text : eap_operate_add,
						resourceCode:me.addResourceCode,
						hidden: me.op=='view' || me.sf == 'onlydcsf',
						handler : function() {
							me.openStandardTermFormWin(null, function(){me.queryStandardTermList(me.nodeId,me.titleStr,me.nodeType);}, false, 'add');
						}
					}),
					Ext.create("Ext.Button", {
						iconCls : 'update',
						text : eap_operate_update,
						resourceCode:me.updateResourceCode,
						hidden:  me.op=='view' || me.sf == 'onlydcsf',
						handler : function() {
							me.openStandardTermFormWin(me.standardid, function(){me.queryStandardTermList(me.nodeId,me.titleStr,me.nodeType);}, false, 'update');
						}
					}),
					Ext.create("Ext.Button", {
						iconCls : 'remove',
						text : eap_operate_delete,
						resourceCode:me.deleteResourceCode,
						hidden: me.op=='view'  || me.sf == 'onlydcsf',
						handler : function() {
							var selected = "";
							var standardTermGridStore = me.standardTermGridList.store;
							/*for(var i=0;i<standardTermGridStore.getCount();i++){
								var record = standardTermGridStore.getAt(i);
								var standardidChk = record.get("standardidChk");
								if(standardidChk){
									selected+= record.get("standardid")+",";
								}
							}*/
							var standTermsChkbox = Ext.query("input[name=standTermsChkbox]");
							if(standTermsChkbox && standTermsChkbox.length){
								for(var i=0;i<standTermsChkbox.length;i++){
									if(standTermsChkbox[i].checked){
										selected+= standTermsChkbox[i].value+",";
									}
									
								}	
							}else if(standTermsChkbox && standTermsChkbox.checked){
								selected+= standTermsChkbox.value+",";
							}
							if (selected.length==0) {
								Ext.Msg.alert('提示', '请选择记录！');
								return;
							}
							Ext.MessageBox.confirm('询问', '你真要删除这些数据吗？',
									function(btn) {
										if (btn == 'yes'){
											me.alertDelMask = new Ext.LoadMask(me, {  
											    msg     : '数据正在处理,请稍候',  
											    removeMask  : true// 完成后移除  
											});
											me.alertDelMask.show();
											Ext.Ajax.request({
												method : 'POST',
												url : 'jobstandard/termsEx/deleteForStandardTermsExListAction!delete.action',
												timeout: 600000,
												success : function(response) {
													me.alertDelMask.hide();
													var result = Ext.decode(response.responseText);
													if(result.success==true)
													Ext.Msg.alert('信息', result.msg, function(btn) {
														me.queryStandardTermList(me.nodeId,me.titleStr,me.nodeType);
													});
													else
													   Ext.Msg.alert('错误提示', result[0].errors);
												},
												failure : function(response) {
													me.alertDelMask.hide();
													var result = Ext.decode(response.responseText);
													if (result && result.length > 0)
														Ext.Msg.alert('错误提示', result[0].errors);
													else
														Ext.Msg.alert('信息', '后台未响应，网络异常！');
												},
												params : "id=" + selected
											});
										}
									}
							 );
						}
					}),
					Ext.create("Ext.Button", {
						icon : 'resources/icons/fam/pencil.png',//按钮图标
						text : '选择题库',
						hidden : me.op=='view' ||  me.sf == 'onlyall',
						handler : function() {
							Ext.Ajax.request({
									  method : 'POST',
									  url : 'jobstandard/termsEx/findBankIdsForStandardTermsExFormAction!findBankIds.action',
									  timeout: 600000,
									  success : function(response) {
									  	 var result = Ext.decode(response.responseText);
									  	 var bankIds = result.bankIds;
									  	 if(bankIds == undefined || bankIds == null){
									  	 	bankIds='';
									  	 }
									  	// if(result){
										  	 me.selectBanksConfig = {
												width:800,
												height:600,
												//数据提取地址
										    	selectUrl: 'base/themebank/specialityThemeBankTreeForThemeBankListAction!specialityThemeBankTree.action?standardid='+me.standardid+'&op='+me.sf,
										    	checked:true,//是否复选
										    	selectType:'bank',//指定可选择的节点
										    	selectTypeName:'题库',
										    	levelAffect:'20',//上下级复选框的影响策略
												keyColumnName : 'id',
												titleColumnName : 'title',
												childColumnName : 'children',
												readerRoot : 'children',
												title:'设定题库',
												selectObject:bankIds.split(",")//选择的结点数组,支持ID数组及对象数组
								    		};
											
								    		//配置、回调函数
											base.model.Tree.openWin(me.selectBanksConfig,function(ids,selectObject){//ID数组，对象数组
											      var eapMaskTip = EapMaskTip(document.body);
											      Ext.Ajax.request({
													  method : 'POST',
													  url : 'jobstandard/termsEx/saveBanksForStandardTermsExFormAction!saveBanks.action',
													  timeout: 600000,
													  success : function(response) {
														  eapMaskTip.hide();
														  var result = Ext.decode(response.responseText);
														  if(result.success==true)
														      Ext.Msg.alert('信息', result.msg, function(btn) {
																//me.querySpeciality();
														      });
														 else
															Ext.Msg.alert('错误提示', result[0].errors);
													  },
													  failure : function(response) {
														eapMaskTip.hide();
														var result = Ext.decode(response.responseText);
														if (result && result.length > 0)
															Ext.Msg.alert('错误提示', result[0].errors);
														else
															Ext.Msg.alert('信息', '后台未响应，网络异常！');
													  },
													  params : "bankIds=" + ids+"&id="+me.standardid+"&sf="+me.sf
												  });
												});
									  	 //}
									  },
									  failure : function(response) {
											Ext.Msg.alert('信息', '后台未响应，网络异常！');
									  },
									  params : "id="+me.standardid+"&sf="+me.sf
							 });	
							
							
						
						}
					}),
					Ext.create("Ext.Button", {
						iconCls : 'sort',
						text : eap_operate_order,
						hidden: me.op=='view'  || me.sf == 'onlydcsf',
						handler : function() {
							me.sortStandardTermList();
						}
					}),
					Ext.create("Ext.Button", {
						colspan : 1,
						iconCls : 'exportXls',
						text : 'Excel导出',
						handler : function() {
							window.location = 'jobstandard/termsEx/expExcelForStandardTermsExportExcelAction!expExcel.action';
						}
					})]
		});
		
		me.standardTermPanel = new Ext.Panel({ 
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
			      me.termform,
		    	  me.standardTermList,
		    	  me.standardTermQuarter
	    	  ]
	    });
	    
		me.items = [
	    	me.treeGrid,
	    	me.standardTermPanel
	    ];
	    
		me.callParent();
	 },
	 getStandardTreeGrid : function(){
		var me = this;
		me.standardtypeTreeStore = Ext.create('Ext.data.TreeStore',{
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
			autoLoad: true,
			proxy : {
				type : 'ajax',
				url : 'jobstandard/termsEx/standardtypeTreeForStandardTermsExListAction!standardtypeTree.action',
				reader : {
					type : 'json'
				}
			},
			//nodeParam : 'parentId',
			defaultRootId : ''
		});
		me.standardtypeTree = new Ext.tree.TreePanel({
				store : me.standardtypeTreeStore,
				//id: 'leftTree',
				title : '模块',
				region: 'west',
				split : false,
				collapsible : true,
				rootVisible : false,
				useArrows : true,
				autoScroll : true,
				width : 200,
				minSize : 80,
				maxSize : 200,
				listeners : {
					itemclick : function(view, record, item, index, e) {
						me.nodeText =record.raw.title;
						me.nodeType = record.raw.type;
						me.nodeId = record.raw.id;
						me.titleStr = '';
						if(me.nodeType == 'parentType'){
							me.parentTypeName = me.nodeText;
							me.titleStr = me.nodeText;
						}else{
							me.titleStr = me.parentTypeName+" - "+me.nodeText;
						}
						me.queryStandardTermList(me.nodeId,me.titleStr,me.nodeType);
					}
				}
		});
		me.parentTypeName = '';
		me.standardtypeTreeStore.on("load", function(mystore,node,success) {
		    var record1 = mystore.getRootNode().childNodes[0];
		   // record.expand();
		   // me.deptTree.getView().refresh();
		    me.nodeText =record1.raw.title;
			me.nodeType = record1.raw.type;
			me.nodeId = record1.raw.id;
			me.titleStr = '';
			if(me.nodeType == 'parentType'){
				me.parentTypeName = me.nodeText;
				me.titleStr = me.nodeText;
			}else{
				me.titleStr = me.parentTypeName+" - "+me.nodeText;
			}
			me.queryStandardTermList(me.nodeId,me.titleStr,me.nodeType);
		});
		me.standardtypeTreeStore.getRootNode().expand(false,false);
		return me.standardtypeTree;
	},
	sortStandardTermList : function(){
		var eapMaskTip = EapMaskTip(document.body);	
		var me = this;
		me.sortConfig = {};
		//列属性配置复制
		me.sortConfig.columns = [
					{
					name : 'standardid',
					width : 0
					}, {
			    	 dataIndex : 'parentTypeName',
			    	 name :'parentTypeName',
			    	 header:"类别",
					 width : 10,
					 sortable:false,
					 menuDisabled:true,
					 hidden : true,
					 titleAlign:"center",
					 renderer:function(value, metadata, record){
						 if(value!=undefined && value!=null && value!=""){
						    metadata.tdAttr = " data-qtip = '"+value+"'";
						 }
						 metadata.style="white-space: normal !important;";
						 return value;
					 }
		    		},{
			    	 dataIndex : 'typename',
			    	 name :'typename',
			    	 header:"模块",
					  width : 10,
					 sortable:false,
					 menuDisabled:true,
					 titleAlign:"center",
					 renderer:function(value, metadata, record){
						 if(value!=undefined && value!=null && value!=""){
						    metadata.tdAttr = " data-qtip = '"+value+"'";
						 }
						 metadata.style="white-space: normal !important;";
						 return value;
					 }
		    		},{
			    	 dataIndex : 'standardname',
			    	 name :'standardname',
			    	 header:"子模块",
					 width : 10,
					 sortable:false,
					 menuDisabled:true,
					 //align:"left",
					 titleAlign:"center",
					 renderer:function(value, metadata, record){
						 if(value!=undefined && value!=null && value!=""){
						    metadata.tdAttr = " data-qtip = '"+value+"'";
						 }
						 metadata.style="white-space: normal !important;";
						 return value;
					 }
		    		},{
			    	 dataIndex : 'contents',
			    	 name : 'contents',
			    	 header:"详细内容",
					 width : 30,
					 sortable:false,
					 menuDisabled:true,
					// align:"left",
					 titleAlign:"center",
					 renderer:function(value, metadata, record){
					 	 value = me.replaceString(value,"\n","<br>");
						 if(value!=undefined && value!=null && value!=""){
						    metadata.tdAttr = " data-qtip = '"+value+"'";
						 }
						 metadata.style="white-space: normal !important;";
						 return value;
					 }
		    		}
		
		];
		
		me.sortConfig.keyColumnName = "standardid";
		me.sortConfig.sortlistUrl = 'jobstandard/termsEx/listForStandardTermsExListAction!list.action?nodeType=all';
		me.sortConfig.jsonParemeterName = 'list';
		me.sortConfig.saveSortUrl = 'jobstandard/termsEx/saveSortForStandardTermsExFormAction!saveSort.action';
		me.sortConfig.callback = function(){
			me.queryStandardTermList(me.nodeId,me.titleStr,me.nodeType);
		};
		var sortPanel = ClassCreate('base.model.SortList', Ext.clone(me.sortConfig));
		// 表单窗口
		var sortWin = new WindowObject({
				layout : 'fit',
				title : '排序',
				height : 700,
				width : 900,
				border : false,
				frame : false,
				modal : true,// 模态
				closeAction : 'hide',
				items : [sortPanel]
		});
		eapMaskTip.hide();
		sortWin.show();
	},
	queryStandardTermList : function(typeId,nodeText,nodeType){
		var me = this;
		me.standardTermGridList.setTitle('岗位标准条款 - '+nodeText);
		var url = 'jobstandard/termsEx/listForStandardTermsExListAction!list.action?typeId='+typeId+"&nodeType="+nodeType;
		url+="&standardnameTerm="+me.standardnameTerm.getValue();
		me.standardTermGridList.store.proxy.url = url;
		me.standardTermGridList.store.load();
		if(me.rfSTQHight){//刷新右边的高度
			me.standardTermGridList.setHeight(me.standardtypeTree.getHeight()/2);
			me.standardTermQuarterGrid.setHeight(me.standardtypeTree.getHeight() - me.standardTermGridList.getHeight() - 117);
			me.rfSTQHight =false;
		}
	},
	queryStandardTermQuarter : function(standardid,standardname){
		var me = this;
		me.standardid = standardid;
		me.standardTermQuarterLabel.setText('岗位信息 - '+standardname);
		me.standardTermQuarterGrid.store.proxy.url = 'jobstandard/termsEx/getStandardTermQuarterListForStandardTermsExListAction!getStandardTermQuarterList.action?standardid='+standardid;
		me.standardTermQuarterGrid.store.load();
	},
	getStandardTermList : function(){
		var me = this;
		me.standardTermStore = new Ext.data.Store({
					autoLoad : false,
					fields : [{
						    name : 'standardid',
						    type : 'string'
						 },{
							name : 'parentTypeName',
							type : 'string'
						 },{
							name : 'typename',
							type : 'string'
						 },{
							name : 'standardname',
							type : 'string'
						 },{
							name : 'contents',
							type : 'string'
						 },{
							name : 'efficient',
							type : 'string'
						 },{
							name : 'refScore',
							type : 'string'
						 },{
							name : 'upStandardScore',
							type : 'string'
						 },{
							name : 'examTypeName',
							type : 'string'
						 }],
					proxy : {
						type : 'ajax',
						actionMethods : "POST",
						timeout: 120000,
						url : '',
	                    reader : {
							type : 'json',
							root : 'list',
							totalProperty : "total"
						}
					},
					remoteSort : false
				});
		me.standardTermGridList = new Ext.grid.GridPanel({
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
				height:0,
				autoHeight:true,
				store : me.standardTermStore,
				stripeRows:true, //斑马线效果  
				//columnLines : true,
				forceFit: true,
				//selType: 'cellmodel',  
				title: '岗位标准条款',
				//plugins:[  
				  //Ext.create('Ext.grid.plugin.CellEditing',{  
				   // clicksToEdit:0 //设置单击单元格编辑  
				  //})  
				//],  
				region: 'north',
				//bodyStyle:'border:0;',
				collapsible : false,
				columns:[
					{
					 dataIndex:'standardid',
					 hidden:true
					},
					new Ext.grid.RowNumberer({text:"序号",width:50,align:"center"}),
					{
					 header:'选择',
					 dataIndex:'standardidChk',
					 sortable:false,
					 menuDisabled:true,
					 align:'center',
					 width:4,
					 hidden:me.op=='view',
					 //xtype: 'checkcolumn'
					 renderer:function(value, metadata, record){
						 return '<input type="checkbox" name="standTermsChkbox" value="'+record.get("standardid")+'" />';
					 }
					},
					{
			    	 dataIndex : 'parentTypeName',
			    	 header:"类别",
					 width : 6,
					 sortable:false,
					 menuDisabled:true,
					 hidden : true,
					 titleAlign:"center",
					 renderer:function(value, metadata, record){
						 if(value!=undefined && value!=null && value!=""){
						    metadata.tdAttr = " data-qtip = '"+value+"'";
						 }
						 metadata.style="white-space: normal !important;";
						 return value;
					 }
		    		},{
			    	 dataIndex : 'typename',
			    	 header:"模块",
					  width : 10,
					 sortable:false,
					 menuDisabled:true,
					 titleAlign:"center",
					 renderer:function(value, metadata, record){
						 if(value!=undefined && value!=null && value!=""){
						    metadata.tdAttr = " data-qtip = '"+value+"'";
						 }
						 metadata.style="white-space: normal !important;";
						 return value;
					 }
		    		},{
			    	 dataIndex : 'standardname',
			    	 header:"子模块",
					 width : 10,
					 sortable:false,
					 menuDisabled:true,
					 //align:"left",
					 titleAlign:"center",
					 renderer:function(value, metadata, record){
						 if(value!=undefined && value!=null && value!=""){
						    metadata.tdAttr = " data-qtip = '"+value+"'";
						 }
						 metadata.style="white-space: normal !important;";
						 return value;
					 }
		    		},{
			    	 dataIndex : 'contents',
			    	 header:"详细内容",
					 width : 30,
					 sortable:false,
					 menuDisabled:true,
					// align:"left",
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
			    	 dataIndex : 'efficient',
			    	 header:"有效期",
					 width : 5,
					 sortable:false,
					 menuDisabled:true,
					 align:"center",
					 renderer:function(value, metadata, record){
						 if(value!=undefined && value!=null && value!=""){
						    metadata.tdAttr = " data-qtip = '"+value+"'";
						 }
						 metadata.style="white-space: normal !important;";
						 return value;
					 }
		    		},{
			    	 dataIndex : 'refScore',
			    	 header:"参考学分",
					 width : 5,
					 sortable:false,
					 menuDisabled:true,
					 align:"center",
					 renderer:function(value, metadata, record){
						 if(value!=undefined && value!=null && value!=""){
						    metadata.tdAttr = " data-qtip = '"+value+"'";
						 }
						 metadata.style="white-space: normal !important;";
						 return value;
					 }
		    		},{
			    	 dataIndex : 'upStandardScore',
			    	 header:"达标标准(分)",
					 width : 5,
					 sortable:false,
					 menuDisabled:true,
					 align:"center",
					 renderer:function(value, metadata, record){
						 if(value!=undefined && value!=null && value!=""){
						    metadata.tdAttr = " data-qtip = '"+value+"'";
						 }
						  metadata.style="white-space: normal !important;";
						  return value;
					 }
		    		},{
			    	 dataIndex : 'examTypeName',
			    	 header:"考核方式",
					 width : 5,
					 sortable:false,
					 menuDisabled:true,
					 align:"center",
					 renderer:function(value, metadata, record){
						 if(value!=undefined && value!=null && value!=""){
						    metadata.tdAttr = " data-qtip = '"+value+"'";
						 }
						  metadata.style="white-space: normal !important;";
						 return value;
					 }
		    		}
		    	],
		    	listeners:{
				   'itemdblclick' : function(grid,record,option){
					 
				   },
				   'itemclick' : function(grid,record,option){
				   		me.standardid = record.get("standardid");
				   		me.standardname = record.get("standardname");
					    me.queryStandardTermQuarter(me.standardid,me.standardname);
				   }
				}
	    });
    	me.standardTermGridList.store.on("load", function() {
    		//var preferences = new base.core.TableMergeCells.Map();
    		
    	    if(me.op == 'view'){
    	    	base.core.TableMergeCells.mergeCells(me.standardTermGridList,[2,3],null);
    	    }else{
    	    	base.core.TableMergeCells.mergeCells(me.standardTermGridList,[3,4],null);
    	    }
    		
    		var sstore = me.standardTermGridList.store;
    		if(sstore.getCount()>0){
    			me.standardTermGridList.setTitle('岗位标准条款 - '+me.titleStr+ '（共'+sstore.getCount()+'项）');
    			var record = sstore.getAt(0)
    			me.standardid = record.get("standardid");
				me.standardname = record.get("standardname");
			    me.queryStandardTermQuarter(me.standardid,me.standardname);
    		}
		});
    	return me.standardTermGridList;
	},
	getStandardTermQuarter : function(){
		var me = this;
		me.standardTermQuarterLabel = new Ext.form.Label({
			labelAlign:'left',
			labelWidth:70,
			style : 'font-weight:bold'
		});
		me.standardTermQuarterTermform = new Ext.Toolbar({
			//columnSize : 4,
			fieldDefaults:{
				labelAlign:'right',
				labelWidth:70
			},
			border : false,
			frame : false,
			defaultType : 'textfield',
			region: 'north',
			margins : '2 2 2 2',
			items : [
					me.standardTermQuarterLabel,
					'->',
					Ext.create("Ext.Button", {
						icon : 'resources/icons/fam/book.png',//按钮图标
						text : '选择岗位',
						hidden : me.op=='view' ||  me.sf == 'onlydcsf',
						handler : function() {
							me.StandardQuarterConfig = {
								width:700,
								height:500,
								//数据提取地址
						    	selectUrl:"jobstandard/termsEx/getQuarterStandardForStandardTermsExListAction!getQuarterStandard.action?standardid="+me.standardid+"&op="+me.op,
						    	checked:true,//是否复选
						    	selectType:'quarter',//指定可选择的节点
						    	selectTypeName:'岗位',
						    	levelAffect:'20',//上下级复选框的影响策略
								keyColumnName : 'id',
								titleColumnName : 'title',
								childColumnName : 'children',
								title:'设置标准对应的岗位',
								selectObject:[]//选择的结点数组,支持ID数组及对象数组
				    		};
							
				    		//配置、回调函数
							base.model.Tree.openWin(me.StandardQuarterConfig,function(ids,selectObject){//ID数组，对象数组
							      var eapMaskTip = EapMaskTip(document.body);
							      Ext.Ajax.request({
									  method : 'POST',
									  url : 'jobstandard/termsEx/saveStandardQuarterForStandardTermsExFormAction!saveStandardQuarter.action',
									  timeout: 600000,
									  success : function(response) {
									  	  eapMaskTip.hide();
										  var result = Ext.decode(response.responseText);
										  if(result.success==true)
										      Ext.Msg.alert('信息', result.msg, function(btn) {
												me.queryStandardTermQuarter(me.standardid,'');
							      				
										      });
										 else
											Ext.Msg.alert('错误提示', result[0].errors);
									  },
									  failure : function(response) {
									  	eapMaskTip.hide();
										var result = Ext.decode(response.responseText);
										if (result && result.length > 0)
											Ext.Msg.alert('错误提示', result[0].errors);
										else
											Ext.Msg.alert('信息', '后台未响应，网络异常！');
									  },
									  params : "id=" + ids+"&standardid="+me.standardid
								  });
							      
								});
						}
					})
					]
		});
		
		
		me.standardTermQuarterStore = new Ext.data.Store({
					autoLoad : false,
					fields : [{
						    name : 'deptName',
						    type : 'string'
						 },{
							name : 'quarterTrainName',
							type : 'string'
						 },{
							name : 'quarterTrainCode',
							type : 'string'
						 }],
					proxy : {
						type : 'ajax',
						actionMethods : "POST",
						timeout: 120000,
						url : '',
	                    reader : {
							type : 'json',
							root : 'jobsStandardQuarterList',
							totalProperty : "jobsStandardQuarterTotal"
						}
					},
					remoteSort : false
				});
				
		me.standardTermQuarterGrid = new Ext.grid.GridPanel({
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
				height:0,//me.treeHeight - 400,
				autoHeight:true,
				store : me.standardTermQuarterStore,
				stripeRows:true, //斑马线效果  
				//columnLines : true,
				forceFit: true,
				//selType: 'cellmodel',  
				//title: '岗位',
				//plugins:[  
				  //Ext.create('Ext.grid.plugin.CellEditing',{  
				   // clicksToEdit:0 //设置单击单元格编辑  
				  //})  
				//],  
				region: 'north',
				//bodyStyle:'border:0;',
				columns:[
				    new Ext.grid.RowNumberer({text:"序号",width:50,align:"center"}),
					{
			    	 dataIndex : 'deptName',
			    	 header:"部门",
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
		    		{dataIndex : 'quarterTrainName',
					 header : '岗位',
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
					{dataIndex : 'quarterTrainCode',
					 header : '岗位编码',
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
					}
		    	],
		    	listeners:{
				   'itemdblclick' : function(grid,record,option){

				   },
				   'itemclick' : function(grid,record,option){
					 
				   }
				}
	    });
    	me.standardTermQuarterGrid.store.on("load", function() {
    		me.standardTermQuarterLabel.setText('岗位信息 - '+me.standardname+'（共'+me.standardTermQuarterGrid.store.getCount()+"项）");
		});
		
		
		
		me.standardTermQuarterGridPanel = new Ext.Panel({ 
			 region : 'center',
			 frame : true,
			 bodyStyle : "border:0;padding:0px 0px 0px 0px;overflow-x:hidden; overflow-y:auto",
			 defaultType : 'textfield',
			 margins : '0 0 0 0',
			 colspan : 1,
			 forceFit : true,// 自动填充列宽,根据宽度比例
			 allowMaxSize : -1,// 允许最大行数,-1为不受限制
			 layout: 'border',
			 collapsible : true,
			 items : [ 
			      me.standardTermQuarterTermform,
		    	  me.standardTermQuarterGrid
	    	  ],
	    	  listeners : {
				 "collapse" : function(panel){//收拢
				     me.standardTermGridList.setHeight(me.standardtypeTree.getHeight()-75);
				     me.standardTermQuarterGridPanel.setTitle(me.standardname+' - 岗位信息  （共'+me.standardTermQuarterGrid.store.getCount()+"项）");
				 },
				 "expand" : function(panel){//展开
				 	me.standardTermQuarterGridPanel.setTitle('');
				    me.standardTermGridList.setHeight(me.standardtypeTree.getHeight()/2);
				 } 
			}
	    });
	    
    	return me.standardTermQuarterGridPanel;
	},
	replaceString : function(str,reallyDo,replaceWith) { 
		var e=new RegExp(reallyDo,"g"); 
		words = str.replace(e, replaceWith); 
		return words; 
	},
	// 打开表单页面方法
	openStandardTermFormWin : function(id, callback, readOnly, oper) {
			var me = this;
			var formConfig = {};
			var readOnly = readOnly || false;
			if(oper=='view'){
				readOnly = true;
			}
			formConfig.readOnly = readOnly;
			formConfig.fileUpload = true;           
			formConfig.formUrl = "jobstandard/termsEx/saveForStandardTermsExFormAction!save.action?sf="+me.sf;
			formConfig.findUrl = "jobstandard/termsEx/findForStandardTermsExFormAction!find.action?sf="+(me.op=='view' || oper=='view'?'all':me.sf);
			formConfig.callback = callback;
			formConfig.columnSize = 2;
			formConfig.jsonParemeterName = 'form';
			formConfig.bodyStyle = "padding-top:5px;";
			formConfig.items = new Array();
			formConfig.oper = oper;// 复制操作类型变量
			formConfig.items.push({
						colspan : 1,
						xtype : 'hidden',
						name : 'standardid'
					});
			formConfig.items.push({
						colspan : 1,
						fieldLabel : '子模块',
						name : 'standardname',
						xtype : 'textfield',
						allowBlank : false,
						readOnly : readOnly,
						maxLength : 32,
						width : 320,
						labelWidth:110
					});
			formConfig.items.push({
						colspan : 1,
						fieldLabel : '模块',
						name : 'standardTypes',
						addPickerWidth:100,
						xtype : 'selecttree',
						nameKey : 'jstypeid',
						nameLable : 'typename',
						readerRoot : 'children',
						keyColumnName : 'id',
						titleColumnName : 'title',
						childColumnName : 'children',
						selectType : 'standardtype',
						selectTypeName : '模块',
						allowBlank : false,
						readOnly : readOnly,
						//selectUrl : 'jobstandard/standardtype/treeForStandardTypeListAction!tree.action',
						selectUrl : 'jobstandard/terms/modeltreeForStandardTermsListAction!modeltree.action',
						width : 320,
						labelWidth:110
					});
		     formConfig.items.push({
						colspan : 2,
						fieldLabel : '题库关联编码',
						name : 'bankStandMapCode',
						xtype : 'textfield',
						readOnly : readOnly,
						labelWidth:110,
						width : 320,
						maxLength : 10
					});
			formConfig.items.push(/*{
					colspan : 2,
					checked : false,
					allowBlank : true,
					fieldLabel:"题库",
					xtype : 'selecttree',
					name : 'themeBank',
					nameKey : 'themeBankId',
					nameLable : 'themeBankName',
					readerRoot : 'themeBanks',
					keyColumnName : 'themeBankId',
					titleColumnName : 'themeBankName',
					childColumnName : 'themeBanks',
					selectUrl : 'base/themebank/listForThemeBankListAction!list.action',
					readOnly : readOnly,
					width : 662
			}*/
			{
					colspan : 2,
					checked : true,
					allowBlank : true,
					//addPickerWidth:300,
					fieldLabel:"题库",
					xtype : 'selecttree',
					name : 'jobsStandardThemebanks',
					nameKey : 'themeBankId',
					nameLable : 'themeBankName',
					readerRoot : 'children',
					keyColumnName : 'id',
					titleColumnName : 'title',
					childColumnName : 'children',
					selectType : 'bank',
					selectTypeName : '题库',
					selectUrl : 'base/themebank/specialityThemeBankTreeForThemeBankListAction!specialityThemeBankTree.action?op='+(me.op=='view' || oper=='view' ?'all':me.sf),
					readOnly : readOnly,
					width : 662,
					labelWidth:110
				});
			formConfig.items.push({
				colspan : 1,
				xtype : 'hidden',
				name : 'isavailable',
				value : 1
				
			});
			/*formConfig.items.push({
						colspan : 1,
						fieldLabel : '是否有效',
						name : 'isavailable',
						xtype : 'select',
						data:[[0,'否'],[1,'是']],
						readOnly : readOnly,
						defaultValue : 1,
						width : 320
					});*/
			
			  formConfig.items.push({
						colspan : 1,
						fieldLabel : '有效期',
						name : 'efficient',
						//xtype : 'textfield',
						allowBlank : false,
						readOnly : readOnly,
						//maxLength : 10,
						width : 320,
						labelWidth:110,
						xtype : 'numberfield',
						maxLength : 2,
						allowNegative:false, //不允许输入负数
						allowDecimals:false, //不允许输入小数   
						allowBlank : true,
						nanText:"请输入正整数",
						minValue: 0     //最小值
					});
			   formConfig.items.push({
						colspan : 1,
						fieldLabel : '参考学分',
						name : 'refScore',
						xtype : 'textfield',
						allowBlank : true,
						readOnly : readOnly,
						maxLength : 10,
						width : 320,
						labelWidth:110
					});
				formConfig.items.push({
						colspan : 1,
						fieldLabel : '达标标准(分)',
						name : 'upStandardScore',
						xtype : 'textfield',
						allowBlank : true,
						readOnly : readOnly,
						maxLength : 10,
						width : 320,
						labelWidth:110
					});
				formConfig.items.push({
						colspan : 1,
						fieldLabel : '考核方式',
						name : 'examTypeName',
						xtype : 'textfield',
						allowBlank : true,
						readOnly : readOnly,
						maxLength : 10,
						width : 320,
						labelWidth:110
					});
		    formConfig.items.push({
						colspan : 2,
						fieldLabel : '详细内容',
						name : 'contents',
						xtype : 'textarea',
						readOnly : readOnly,
						maxLength : 1000,
						width : 662,
						height : 170,
						labelWidth:110
					});
			 formConfig.items.push({
						colspan : 2,
						fieldLabel : '参考资料',
						name : 'referenceBook',
						xtype : 'textarea',
						readOnly : readOnly,
						maxLength : 1000,
						width : 662,
						height : 170,
						labelWidth:110
					});		
			
			
            formConfig.items.push({
						colspan : 1,
						name : 'orderno',
						xtype : 'hidden',
						value : 0
					});
		    formConfig.items.push({
						colspan : 1,
						name : 'levelno',
						xtype : 'hidden'
					});
			formConfig.items.push({
						colspan : 1,
						name : 'status',
						xtype : 'hidden'
					});
			formConfig.items.push({
						colspan : 1,
						name : 'lastUpdateDate',
						xtype : 'hidden'
					});
			formConfig.items.push({
						colspan : 1,
						name : 'lastUpdatedBy',
						xtype : 'hidden'
					});
			formConfig.items.push({
						colspan : 1,
						name : 'creationDate',
						xtype : 'hidden'
					});
			formConfig.items.push({
						colspan : 1,
						name : 'createdBy',
						xtype : 'hidden'
					});
			formConfig.items.push({
						colspan : 1,
						name : 'organid',
						xtype : 'hidden'
					});
			var form = ClassCreate('base.model.Form', formConfig);
			form.parentWindow = this;
			// 表单窗口
			var formWin = new WindowObject({
						layout : 'fit',
						title : '岗位标准条款',
						//height : 500,
						autoHeight : true,
						width : 700,
						border : false,
						frame : false,
						modal : true,// 模态
						closeAction : 'hide',
						items : [form]
					});
			form.setFormData(id,function(result){
				if(oper == 'add'){
					form.getForm().findField("standardTypes").setValue({"typename":me.nodeText,"jstypeid":me.nodeId});
				}
				if(oper == 'update'){
					setTimeout(function(){
						form.getForm().isValid();
					},300);
				}
			});
			formWin.show();
	}
});