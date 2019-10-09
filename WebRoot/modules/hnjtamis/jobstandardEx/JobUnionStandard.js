/**
 * 根据岗位选择标准
 */
ClassDefine('modules.hnjtamis.jobstandardEx.JobUnionStandard', {
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
		me.treeGrid = me.getTreeGrid();
		me.standardTermList = me.getStandardTermList();
		me.standardTermQuarter = me.getSpeciality();
		
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
							me.queryStandardTerm(me.nodeId,me.titleStr,me.nodeType);
						}
					}),
					'->',
					Ext.create("Ext.Button", {
						iconCls : 'view',
						text : eap_operate_view,
						resourceCode:me.viewResourceCode,
						handler : function() {
							var selected = me.standardTermGrid.getSelectionModel().selected;
							if (selected.getCount() == 0) {
									Ext.Msg.alert('提示', '请选择记录！');
									return;
							}
							var id = "";
							var record = null;
							for (var i = 0; i < selected.getCount(); i++) {
								record = selected.get(i);
								id = record.get("standardid");
							}
							me.openStandardTermFormWin(id,function(){}, false, 'view');
						}
					}),
					Ext.create("Ext.Button", {
						icon : 'resources/icons/fam/add.png',//按钮图标
						text : '添加岗位标准条款',
						hidden : me.op=='view',
						handler : function() {
							me.addStandardInQuarter();
						}
					}),
					Ext.create("Ext.Button", {
						iconCls : 'remove',
						text : eap_operate_delete,
						resourceCode:me.deleteResourceCode,
						hidden: me.op=='view',
						handler : function() {
							var selected = "";
							/*var standardTermGridStore = me.standardTermGrid.store;
							for(var i=0;i<standardTermGridStore.getCount();i++){
								var record = standardTermGridStore.getAt(i);
								var standardidChk = record.get("standardidChk");
								if(standardidChk){
									selected+= record.get("standardid")+",";
								}
							}*/
							var standTermsChkbox = Ext.query("input[name=ustandTermsChkbox]");
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
												url : 'jobstandard/jobunionstandardEx/deleteForJobUnionStandardExListAction!delete.action',
												timeout: 600000,
												success : function(response) {
													me.alertDelMask.hide();
													var result = Ext.decode(response.responseText);
													if(result.success==true)
													Ext.Msg.alert('信息', result.msg, function(btn) {
														me.queryStandardTerm(me.nodeId,me.titleStr,me.nodeType);
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
												params : "id=" + selected+"&quarterId="+me.nodeId
											});
										}
									}
							 );
						}
					}),
					Ext.create("Ext.Button", {
						colspan : 1,
						iconCls : 'exportXls',
						text : 'Excel导出',
						handler : function() {
							window.location = 'jobstandard/jobunionstandardEx/expExcelForJobUnionStandardExportExcelAction!expExcel.action?typeId='+me.nodeId+"&nodeType=quarter";
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
	 getTreeGrid : function(){
		var me = this;
		me.standardQuarterTreeStore = Ext.create('Ext.data.TreeStore',{
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
				url :  "jobstandard/termsEx/getQuarterStandardForStandardTermsExListAction!getQuarterStandard.action?op="+me.op,
				reader : {
					type : 'json'
				}
			},
			//nodeParam : 'parentId',
			defaultRootId : ''
		});
		me.standardQuarterTree = new Ext.tree.TreePanel({
				store : me.standardQuarterTreeStore,
				//id: 'leftTree',
				title : '岗位',
				region: 'west',
				split : false,
				collapsible : true,
				rootVisible : false,
				useArrows : true,
				autoScroll : true,
				width : 240,
				minSize : 80,
				maxSize : 200,
				listeners : {
					itemclick : function(view, record, item, index, e) {
						me.nodeText =record.raw.title;
						me.nodeType = record.raw.type;
						me.nodeId = record.raw.id;
						me.titleStr = '';
						if(me.nodeType == 'quarter'){
							me.nodeId = (me.nodeId.split("@"))[1];
							me.parentTypeName = me.nodeText;
							me.titleStr = me.nodeText;
							me.queryStandardTerm(me.nodeId,me.titleStr,me.nodeType);
						}
					
					}
				}
		});
		me.parentTypeName = '';
		me.standardQuarterTreeStore.on("load", function(mystore,node,success) {
			var record = mystore.getRootNode().childNodes[0];
			//record.expand();
			record = record.childNodes[0];;
			me.nodeType = record.raw.type;
			if(me.nodeType == 'quarter'){
				me.nodeText =record.raw.title;
				me.nodeId = record.raw.id;
				me.nodeId = (me.nodeId.split("@"))[1];
				me.parentTypeName = me.nodeText;
				me.titleStr = me.nodeText;
				me.queryStandardTerm(me.nodeId,me.titleStr,me.nodeType);
			}
		});
		me.standardQuarterTreeStore.getRootNode().expand(false,false);
		return me.standardQuarterTree;
	},
	queryStandardTerm : function(typeId,nodeText,nodeType){
		var me = this;
		me.quarterId = typeId;
		me.quarterName = nodeText;
		me.standardTermGrid.setTitle('岗位标准条款 - '+nodeText);
		var url = 'jobstandard/termsEx/listForStandardTermsExListAction!list.action?typeId='+typeId+"&nodeType=quarter";
		url+="&standardnameTerm="+me.standardnameTerm.getValue();
		me.standardTermGrid.store.proxy.url = url;
		me.standardTermGrid.store.load();
		if(me.rfSTQHight){//刷新右边的高度
			me.standardTermGrid.setHeight(me.standardQuarterTree.getHeight()/2);
			me.specialityGrid.setHeight(me.standardQuarterTree.getHeight() - me.standardTermGrid.getHeight() - 122);
			me.rfSTQHight =false;
		}
	},
	querySpeciality : function(){
		var me = this;
		me.specialityLabel.setText('涉及专业 - '+me.quarterName);
		me.specialityGrid.store.proxy.url = 'jobstandard/jobunionstandardEx/specialitylistForJobUnionStandardExListAction!specialitylist.action?quarterId='+me.quarterId;
		me.specialityGrid.store.load();
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
						 },{
							name : 'parentTrainQuarterId',
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
		me.standardTermGrid = new Ext.grid.GridPanel({
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
				minSize : 80,
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
					{
					 dataIndex:'parentTrainQuarterId',
					 hidden:true
					},
					new Ext.grid.RowNumberer({text:"序号",width:50,align:"center"}),
					{
					 header:'选择',
					 dataIndex:'standardidChk',
					 sortable:false,
					 menuDisabled:true,
					 align:'center',
					 hidden:me.op=='view',
					 width:4,
					 //xtype: 'checkcolumn'
					 renderer:function(value, metadata, record){
					 	var parentTrainQuarterId= record.get("parentTrainQuarterId");
					 	if(parentTrainQuarterId!=undefined && parentTrainQuarterId!='' && parentTrainQuarterId!='null'){
					 		return '<input type="checkbox" name="ustandTermsChkbox" value="'+record.get("standardid")+'" disabled="true" />';
					 	}else{
					 		return '<input type="checkbox" name="ustandTermsChkbox" value="'+record.get("standardid")+'" />';
					 	}
					 }
					},
					{
			    	 dataIndex : 'parentTypeName',
			    	 header:"类别",
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
				   	
				   }
				}
	    });
    	me.standardTermGrid.store.on("load", function() {
    		 if(me.op == 'view'){
	    	    base.core.TableMergeCells.mergeCells(me.standardTermGrid,[2,3,5],null);
	    	 }else{
	    	    base.core.TableMergeCells.mergeCells(me.standardTermGrid,[3,4],null);
	    	 }
			
    		 me.standardTermGrid.setTitle('岗位标准条款 - '+me.titleStr+ '（共'+me.standardTermGrid.store.getCount()+'项）');
    		 me.querySpeciality();
		});
    	return me.standardTermGrid;
	},
	getSpeciality : function(){
		var me = this;
		me.specialityLabel = new Ext.form.Label({
			labelAlign:'left',
			labelWidth:70,
			style : 'font-weight:bold'
		});
		me.specialityTermForm = new Ext.Toolbar({
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
					me.specialityLabel,
					'->',
					Ext.create("Ext.Button", {
						icon : 'resources/icons/fam/book.png',//按钮图标
						text : '选择专业',
						hidden : true,//me.op=='view',
						handler : function() {
						me.specialityConfig = {
							width:500,
							height:500,
							//数据提取地址
					    	selectUrl:"baseinfo/speciality/treeForSpecialityListAction!tree.action?jobidTerm="+me.quarterId,
					    	checked:true,//是否复选
					    	selectType:'speciality',//指定可选择的节点
					    	selectTypeName:'专业',
					    	levelAffect:'20',//上下级复选框的影响策略
							keyColumnName : 'id',
							titleColumnName : 'title',
							childColumnName : 'children',
							title:'设定岗位关联专业',
							selectObject:[]//选择的结点数组,支持ID数组及对象数组
			    		};
						
			    		//配置、回调函数
						base.model.Tree.openWin(me.specialityConfig,function(ids,selectObject){//ID数组，对象数组
						      var eapMaskTip = EapMaskTip(document.body);
						      Ext.Ajax.request({
								  method : 'POST',
								  url : 'jobstandard/jobunionstandardEx/saveSpecialityForJobUnionStandardExFormAction!saveSpeciality.action',
								  timeout: 600000,
								  success : function(response) {
									  eapMaskTip.hide();
									  var result = Ext.decode(response.responseText);
									  if(result.success==true)
									      Ext.Msg.alert('信息', result.msg, function(btn) {
											me.querySpeciality();
						      				
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
								  params : "id=" + ids+"&quarterId="+me.quarterId
							  });
							});
						}
					})
					]
		});
		
		
		me.specialityStore = new Ext.data.Store({
					autoLoad : false,
					fields : [{
						    name : 'typename',
						    type : 'string'
						 },{
							name : 'specialityname',
							type : 'string'
						 }],
					proxy : {
						type : 'ajax',
						actionMethods : "POST",
						timeout: 120000,
						url : '',
	                    reader : {
							type : 'json',
							root : 'specialitys',
							totalProperty : "specialitysTotal"
						}
					},
					remoteSort : false
				});
				
		me.specialityGrid = new Ext.grid.GridPanel({
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
				store : me.specialityStore,
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
			    	 dataIndex : 'typename',
			    	 header:"专业类型",
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
		    		{dataIndex : 'specialityname',
					 header : '专业',
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
					}
		    	],
		    	listeners:{
				   'itemdblclick' : function(grid,record,option){

				   },
				   'itemclick' : function(grid,record,option){
					 
				   }
				}
	    });
    	me.specialityGrid.store.on("load", function() {
    		/*if(me.op == 'view'){
	    	    base.core.TableMergeCells.mergeCells(me.specialityGrid,[2],null);
	    	}else{
	    	    base.core.TableMergeCells.mergeCells(me.specialityGrid,[2],null);
	    	}*/
    		me.specialityLabel.setText('涉及专业  -  '+me.quarterName+'（共'+me.specialityGrid.store.getCount()+"项）");
		});
		
		
		
		me.specialityGridPanel = new Ext.Panel({ 
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
			      me.specialityTermForm,
		    	  me.specialityGrid
	    	  ],
	    	  listeners : {
				 "collapse" : function(panel){//收拢
				     me.standardTermGrid.setHeight(me.standardQuarterTree.getHeight()-75);
				     me.specialityGridPanel.setTitle(me.quarterName+' - 涉及专业  （共'+me.specialityGrid.store.getCount()+"项）");
				 },
				 "expand" : function(panel){//展开
				 	me.specialityGridPanel.setTitle('');
				    me.standardTermGrid.setHeight(me.standardQuarterTree.getHeight()/2);
				 } 
			 }
	    });
	    
    	return me.specialityGridPanel;
	},
	replaceString : function(str,reallyDo,replaceWith) { 
		var e=new RegExp(reallyDo,"g"); 
		words = str.replace(e, replaceWith); 
		return words; 
	},
	addStandardInQuarter : function(){
			var me = this;
			me.addQuerterMask = new Ext.LoadMask(me, {  
			    msg     : '数据正在处理,请稍候',  
			    removeMask  : true// 完成后移除  
			});
			me.addQuerterMask.show();
			var formConfig = {};
			formConfig.readOnly = false;
			formConfig.fileUpload = true;
			formConfig.clearButtonEnabled = false;
			formConfig.saveButtonEnabled = false;
			formConfig.closeButtonEnabled = false;
			formConfig.callback = function() {};
			formConfig.columnSize = 1;
			formConfig.jsonParemeterName = 'standardQuarterForm';
			formConfig.items = new Array();
			
			formConfig.items.push({
				xtype:'hidden',
				name:'checkHandIds'
			});
			
			me.qtypenameTerm = new base.core.SelectTree({
				name : 'typenameTerm',
				fieldLabel : '模块',
				xtype : 'selecttree',
				nameKey : 'jstypeid',
				nameLable : 'typename',
				readerRoot : 'children',
				keyColumnName : 'id',
				titleColumnName : 'title',
				childColumnName : 'children',
				selectType : 'standardtype',
				selectTypeName : '模块',
				allowBlank : true,
				editorType:'str',
				selectUrl : 'jobstandard/terms/modeltreeForStandardTermsListAction!modeltree.action',
				labelWidth : 40,
				width:180,
				addPickerWidth : 180
			});
					
			me.qstandardnameTerm = new Ext.form.TextField({
				xtype : 'textfield',
				name  : 'standardnameTerm',
				fieldLabel : '子模块',
				labelWidth : 60,
				width:180
			});
		
			me.StandardInQuarterTermform = new Ext.Toolbar({
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
						me.qtypenameTerm,
						me.qstandardnameTerm,
						new Ext.Button({
							colspan : 1,
							iconCls : 'query',
							text:'查询',
							handler : function() {
								me.form2.qqueryStandardTerm();
							}
						}),
						'->',
						Ext.create("Ext.Button", {
							icon : 'resources/icons/fam/tick.png',//按钮图标
							text : "全部选择",//按钮标题
							handler : function() {
								  for(var i=0;i<me.addStandardInQuarterStore.getCount();i++){
									  var record = me.addStandardInQuarterStore.getAt(i);
									  record.set("standardidChk",true);
								  }
							}
						}),
						Ext.create("Ext.Button", {
							icon : 'resources/icons/fam/delete.gif',//按钮图标
							text : "全部取消",//按钮标题
							handler : function() {
								  for(var i=0;i<me.addStandardInQuarterStore.getCount();i++){
									  var record = me.addStandardInQuarterStore.getAt(i);
									  record.set("standardidChk",false);
								  }
							}
						}),
						Ext.create("Ext.Button", {
							icon : 'resources/icons/fam/folder_wrench.png',//按钮图标
							text : "确定添加",//按钮标题
							handler : function() {
								  var ids = "";
								  for(var i=0;i<me.addStandardInQuarterStore.getCount();i++){
									  var record = me.addStandardInQuarterStore.getAt(i);
									  var standardidChk = record.get("standardidChk");
									  if(standardidChk){
										  ids+= record.get("standardid")+",";
									  }
								  }
								  if(ids==null || ids==""){
								  	Ext.Msg.alert('错误提示', '请选择要添加的标准！');
								  }else{
									  var eapMaskTip = EapMaskTip(document.body);
								      Ext.Ajax.request({
										  method : 'POST',
										  url : 'jobstandard/jobunionstandardEx/addStandardInQuarterForJobUnionStandardExFormAction!addStandardInQuarter.action',
										  timeout: 600000,
										  success : function(response) {
											  eapMaskTip.hide();
											  var result = Ext.decode(response.responseText);
											  if(result.success==true)
											      Ext.Msg.alert('信息', result.msg, function(btn) {
											      	   me.form2.qqueryStandardTerm();
													   me.queryStandardTerm(me.nodeId,me.titleStr,me.nodeType);
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
										  params : "id=" + ids+"&quarterId="+me.quarterId
									  });
								  }
							}
						}),
						Ext.create("Ext.Button", {
							text : eap_operate_close,
							iconCls : 'close',
							tabIndex:902,
							handler : function() {
								me.form2.close();
							}
						})]
			});
			formConfig.items.push(me.StandardInQuarterTermform);
			
			me.addStandardInQuarterStore = new Ext.data.Store({
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
				autoLoad :false,
				proxy:{
					type : 'ajax',
					actionMethods : "POST",
					timeout: 120000,
					url: 'jobstandard/termsEx/listForStandardTermsExListAction!list.action?nodeType=unquarter&typeId='+me.quarterId,
		            reader : {
						type : 'json',
						root : 'list',
						totalProperty : "total"
					}
				},
				remoteSort : false
			}); 
		    me.addStandardInQuarterListGrid = new Ext.grid.GridPanel({
	    	 	store:me.addStandardInQuarterStore,
	    	 	autoScroll :true,
				//autoHeight:true,
	    	 	height:536,
	    	 	colspan:2,
	    	 	autoWidth:true, 
				stripeRows:true, //斑马线效果          
	    	 	columnLines : true,
	    	 	forceFit: true,
	    	 	collapsible : false,
	    	 //	title: '试题',
		    	columns:[
		    		/*{header:"themeId",dataIndex:"themeId",hidden:true},
		    		{header:"testpaperThemeId",dataIndex:"testpaperThemeId",hidden:true},
		    		{header:"选择",dataIndex:"checkOpt",sortable:false, menuDisabled:true,width:15,titleAlign:"center",
		    		renderer:function(value,v1,v2){
		    			setCheckHandThemeIds = function(obj){
		    				var tmpvalue = me.form2.getForm().findField("checkHandIds").getValue();
		    				if(tmpvalue==undefined || tmpvalue ==null || tmpvalue=="null" || tmpvalue==""){
		    					tmpvalue=",";
		    				}
		    				if(obj.checked){
		    					tmpvalue+=obj.value+",";
		    				}else{
		    					tmpvalue=tmpvalue.replace((obj.value+","),"");
		    				}
		    				me.form2.getForm().findField("checkHandIds").setValue(tmpvalue);
		    			}
		    			
		    			return '<input type="checkbox" value="'+(v2.get("themeId"))+'" onclick="setCheckHandThemeIds(this)"/>';
		    		}},
		    		{header:"序号",dataIndex:"sortIndex",sortable:false, menuDisabled:true,width:15,titleAlign:"center"},
		    		{header:"题型",dataIndex:"themeTypeName",sortable:false, menuDisabled:true,width:35,titleAlign:"center"},
		    		{header:"试题",dataIndex:"themeName",sortable:false, menuDisabled:true,width:150,titleAlign:"center",	
		    		 renderer:function(value, metadata, record){
		    			metadata.style="white-space: normal !important;";
		    			return value;
		    		}},
		    		{header:"默认分值",dataIndex:"defaultScore",sortable:false, menuDisabled:true,width:25,titleAlign:"center"},
		    		{header:"答案",dataIndex:"answerkeyStr",sortable:false, menuDisabled:true,width:150,titleAlign:"center",
		    		 renderer:function(value, metadata, record){
		    			metadata.style="white-space: normal !important;";
		    			return value;
		    		}}
		    		
		    		*/{
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
					 xtype: 'checkcolumn'
					},
					{
			    	 dataIndex : 'parentTypeName',
			    	 header:"类别",
					 width : 8,
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
			    	 dataIndex : 'typename',
			    	 header:"模块",
					 width : 8,
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
		    		]
	    	});
			formConfig.items.push(me.addStandardInQuarterListGrid);
			me.addStandardInQuarterStore.load();
			
			formConfig.otherOperaters = [];//其它扩展按钮操作
			
		    me.form2 = ClassCreate('base.model.Form',formConfig);
			me.form2.parentWindow = this;
			
			me.form2.qqueryStandardTerm = function(){
				var standardnameTerm = me.qstandardnameTerm.getValue();
				if(standardnameTerm==undefined)standardnameTerm = "";
				var modelIdTerm = me.qtypenameTerm.getValue();
				if(modelIdTerm==undefined)modelIdTerm = "";
				var url = 'jobstandard/termsEx/listForStandardTermsExListAction!list.action?nodeType=unquarter&typeId='+me.quarterId;
				url+="&standardnameTerm="+standardnameTerm;
				url+="&modelIdTerm="+modelIdTerm;
				me.addStandardInQuarterListGrid.store.proxy.url = url;
				me.addStandardInQuarterListGrid.store.load();
			}
			
			//表单窗口
			me.handStandardInQuarterformWin = new WindowObject({
				layout:'fit',
				title:'添加岗位标准条款  - '+me.quarterName,
				width:950,
				border:false,
				frame:false,
				modal:true,
				//autoScroll:true,
				bodyStyle:'overflow-x:auto;overflow-y:hidden;',
				closeAction:'hide',
				items:[me.form2]
			});
			
			me.addStandardInQuarterListGrid.store.on("load",function(){
				me.addQuerterMask.hide();
				me.handStandardInQuarterformWin.show();
			});
			
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
			formConfig.formUrl = "jobstandard/termsEx/saveForStandardTermsExFormAction!save.action";
			formConfig.findUrl = "jobstandard/termsEx/findForStandardTermsExFormAction!find.action";
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
						width : 320
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
						width : 320
					});
		    
			formConfig.items.push({
					colspan : 1,
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
					width : 320
			});
			formConfig.items.push({
						colspan : 1,
						fieldLabel : '是否有效',
						name : 'isavailable',
						xtype : 'select',
						data:[[0,'否'],[1,'是']],
						readOnly : readOnly,
						defaultValue : 1,
						width : 320
					});
			
			  formConfig.items.push({
						colspan : 1,
						fieldLabel : '有效期',
						name : 'efficient',
						xtype : 'textfield',
						allowBlank : false,
						readOnly : readOnly,
						maxLength : 10,
						width : 320
					});
			   formConfig.items.push({
						colspan : 1,
						fieldLabel : '参考学分',
						name : 'refScore',
						xtype : 'textfield',
						allowBlank : false,
						readOnly : readOnly,
						maxLength : 10,
						width : 320
					});
				formConfig.items.push({
						colspan : 1,
						fieldLabel : '达标标准(分)',
						name : 'upStandardScore',
						xtype : 'textfield',
						allowBlank : false,
						readOnly : readOnly,
						maxLength : 10,
						width : 320
					});
				formConfig.items.push({
						colspan : 1,
						fieldLabel : '考核方式',
						name : 'examTypeName',
						xtype : 'textfield',
						allowBlank : false,
						readOnly : readOnly,
						maxLength : 10,
						width : 320
					});
		    formConfig.items.push({
						colspan : 2,
						fieldLabel : '详细内容',
						name : 'contents',
						xtype : 'textarea',
						readOnly : readOnly,
						maxLength : 1000,
						width : 662,
						height : 170
					});
			 formConfig.items.push({
						colspan : 2,
						fieldLabel : '参考资料',
						name : 'referenceBook',
						xtype : 'textarea',
						readOnly : readOnly,
						maxLength : 1000,
						width : 662,
						height : 170
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
			formWin.show();
			form.setFormData(id,function(result){
				if(oper == 'add'){
					form.getForm().findField("standardTypes").setValue({"typename":me.nodeText,"jstypeid":me.nodeId});
				}
			});
	}
});