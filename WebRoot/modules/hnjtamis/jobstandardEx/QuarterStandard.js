/**
 * 根据岗位选择标准
 */
ClassDefine('modules.hnjtamis.jobstandardEx.QuarterStandard', {
	 extend : 'Ext.Panel',
	 requires : ['base.model.Tree'], 
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
		me.op = me.op || 'ogview';//stinput-标准岗位维护  oginput-电厂管理员维护   stview-标准岗位查看 ogview-电厂查看
		me.treeGrid = me.getTreeGrid();
		me.standardTermList = me.getQuarteStandardList();
		
		me.quarternameTerm = new Ext.form.TextField({
			xtype : 'textfield',
			name  : 'quarternameTerm',
			fieldLabel : '&nbsp;&nbsp;岗位',
			labelWidth : 60,
			width:200
			
		});
		me.queryTypeTerm= new base.core.Select({
			fieldLabel:"&nbsp;&nbsp;查询方式",
			name:'queryTypeTerm',
			hidden : me.op=='stview' || me.op=='stinput',
			xtype : 'select',
			data:[[null,'全部'],['10','仅标准岗位'],['20','仅公司私有']],
			labelWidth : 80,
			width:200
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
					me.quarternameTerm,
					me.queryTypeTerm,
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
							var selected = me.quarteStandardGrid.getSelectionModel().selected;
							if (selected.getCount() == 0) {
									Ext.Msg.alert('提示', '请选择记录！');
									return;
							}
							var id = "";
							var record = null;
							me.standardnums = 0;
							for (var i = 0; i < selected.getCount(); i++) {
								record = selected.get(i);
								id = record.get("quarterId");
								me.standardnums = record.get("standardnums");
							}
							me.openFormWin(id, function() {},true,record.data,{},'view');
						}
					}),
					Ext.create("Ext.Button", {
						icon : 'resources/icons/fam/add.png',//按钮图标
						text : '新增',
						hidden : me.op=='stview' || me.op=='ogview',
						handler : function() {
								var queryTerm = {};
								if(me.termForm)
								  queryTerm = me.termForm.getValues(false);
								var record = {'data':{}};
								var selected = me.quarteStandardGrid.getSelectionModel().selected;
								me.standardnums = 0;
								for (var i = 0; i < selected.getCount(); i++) {
									record = selected.get(i);
								}
								me.openFormWin('', function() {
											me.queryStandardTerm(me.nodeId,me.titleStr,me.nodeType);
										},false,record.data,queryTerm,'add');
						}
					}),
					Ext.create("Ext.Button", {
							iconCls : 'update',
							text : eap_operate_update,
							resourceCode:me.updateResourceCode,
							hidden : me.op=='stview' || me.op=='ogview',
							handler : function() {
								var selected = me.quarteStandardGrid.getSelectionModel().selected;
								if (selected.getCount() == 0) {
									Ext.Msg.alert('提示', '请选择记录！');
									return;
								}
								var id = "";
								var record = null;
								var belongType = "";
								me.standardnums = 0;
								for (var i = 0; i < selected.getCount(); i++) {
									record = selected.get(i);
									id = record.get("quarterId");
									belongType = record.get("belongType");
									me.standardnums = record.get("standardnums");
								}
								if(belongType == '10' && me.op=='oginput'){
									Ext.Msg.alert('提示', '您不能维护系统定义的标准岗位！');
									return;
								}else if(me.op=='stinput' || me.op=='oginput'){
									var queryTerm = {};
									if(me.termForm)
										queryTerm = me.termForm.getValues(false);
									me.openFormWin(id, function() {
												me.queryStandardTerm(me.nodeId,me.titleStr,me.nodeType);
											},false,record.data,queryTerm,'update');
								}
								
							}
					}),
					Ext.create("Ext.Button", {
						iconCls : 'remove',
						text : eap_operate_delete,
						resourceCode:me.deleteResourceCode,
						hidden : me.op=='stview' || me.op=='ogview',
						handler : function() {
							var selected = "";
							/*var quarteStandardGridStore = me.quarteStandardGrid.store;
							for(var i=0;i<quarteStandardGridStore.getCount();i++){
								var record = quarteStandardGridStore.getAt(i);
								var standardidChk = record.get("standardidChk");
								if(standardidChk){
									selected+= record.get("standardid")+",";
								}
							}*/
							var standQtIdChkbox = Ext.query("input[name=ustandQtIdChkbox]");
							if(standQtIdChkbox && standQtIdChkbox.length){
								for(var i=0;i<standQtIdChkbox.length;i++){
									if(standQtIdChkbox[i].checked){
										selected+= standQtIdChkbox[i].value+",";
									}
									
								}	
							}else if(standQtIdChkbox && standQtIdChkbox.checked){
								selected+= standQtIdChkbox.value+",";
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
												url : 'baseinfo/quarterStandard/deleteForQuarterStandardListAction!delete.action?op='+me.op,
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
												params : "id=" + selected
											});
										}
									}
							 );
						}
					})/*,
					Ext.create("Ext.Button", {
						colspan : 1,
						iconCls : 'exportXls',
						text : 'Excel导出',
						handler : function() {
							//window.location = 'jobstandard/jobunionstandardEx/expExcelForJobUnionStandardExportExcelAction!expExcel.action?typeId='+me.nodeId+"&nodeType=quarter";
						}
					})*/]
		});
		
		me.quarteStandardPanel = new Ext.Panel({ 
			 region : 'center',
			 frame : true,
			 bodyStyle : "border:0;padding:0px 0px 0px 0px;overflow-x:hidden; overflow-y:auto",
			 defaultType : 'textfield',
			 margins : '0 0 0 0',
			 colspan : 1,
			 forceFit : true,// 自动填充列宽,根据宽度比例
			 allowMaxSize : -1,// 允许最大行数,-1为不受限制
			 tbar: me.termform,
			 layout: 'border',
			 items : [ 
		    	  me.standardTermList
	    	  ]
	    });
	    
		me.items = [
	    	me.treeGrid,
	    	me.quarteStandardPanel
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
				url :  "baseinfo/quarterStandard/getQuarterStandardDeptForQuarterStandardListAction!getQuarterStandardDept.action?"+me.op,
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
				title : '部门',
				region: 'west',
				split : false,
				collapsible : true,
				rootVisible : false,
				useArrows : true,
				autoScroll : true,
				width : 180,
				minSize : 80,
				maxSize : 200,
				listeners : {
					itemclick : function(view, record, item, index, e) {
						me.nodeText =record.raw.title;
						me.nodeType = record.raw.type;
						me.nodeId = record.raw.id;
						me.titleStr = '';
						if(me.nodeType == 'dept'){
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
			//record = record.childNodes[0];;
			me.nodeType = record.raw.type;
			if(me.nodeType == 'dept'){
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
		me.quarteStandardGrid.setTitle(nodeText+ '（共0项）');
		var url = 'baseinfo/quarterStandard/listForQuarterStandardListAction!list.action';
		//url+="&nameTerm="+me.quarternameTerm.getValue();
		//url+="&queryType="+me.queryTypeTerm.getValue();
		me.quarteStandardGrid.store.proxy.url = url;
		me.quarteStandardGrid.store.proxy.extraParams = { 
			deptName : me.quarterName,
			op : me.op,
			nameTerm : me.quarternameTerm.getValue(),
			queryType:me.queryTypeTerm.getValue()
		};
		me.quarteStandardGrid.store.load();
		
	},
	getQuarteStandardList : function(){
		var me = this;
		me.quarteStandardStore = new Ext.data.Store({
					autoLoad : false,
					fields : [{
						    name : 'quarterId',
						    type : 'string'
						 },{
							name : 'quarterName',
							type : 'string'
						 },{
							name : 'quarterCode',
							type : 'string'
						 },{
							name : 'quarterCode',
							type : 'string'
						 },{
							name : 'specialityName',
							type : 'string'
						 },{
							name : 'organName',
							type : 'string'
						 },{
							name : 'parentQuarterName',
							type : 'string'
						 },{
							name : 'belongType',
							type : 'string'
						 },{
							name : 'standardnums',
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
		me.quarteStandardGrid = new Ext.grid.GridPanel({	
				autoScroll :true,
				autoHeight:true,
				store : me.quarteStandardStore,
				stripeRows:true, //斑马线效果 
				forceFit: true,
				title: '岗位标准条款',
				region: 'center',
				collapsible : false,
				viewConfig:{autoScroll:false},//高度
				bodyStyle : "padding:0px 0px 0px 0px;overflow-x:hidden; overflow-y:hidden",
				layout:'fit',
				columns:[
					{
					 dataIndex:'quarterId',
					 hidden:true
					},{
					 dataIndex:'standardnums',
					 hidden:true
					},
					new Ext.grid.RowNumberer({text:"序号",width:50,align:"center"}),
					{
					 header:'选择',
					 dataIndex:'standardidChk',
					 sortable:false,
					 menuDisabled:true,
					 align:'center',
					 hidden:me.op=='ogview' || me.op=='stview',
					 width:3,
					 //xtype: 'checkcolumn'
					 renderer:function(value, metadata, record){
					 	if(record.get("belongType") == '10' && me.op=='oginput'){
					 		return '<input type="checkbox" disabled = "true" />';
					 	}else if(me.op=='stinput' || me.op=='oginput'){
					 		return '<input type="checkbox" name="ustandQtIdChkbox" value="'+record.get("quarterId")+'" />';
					 	}else{
					 		return "&nbsp;";
					 	}
						 
					 }
					},{
			    	 dataIndex : 'quarterName',
			    	 header:"岗位",
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
			    	 dataIndex : 'quarterCode',
			    	 header:"编码",
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
			    	 dataIndex : 'specialityName',
			    	 header:"专业",
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
			    	 dataIndex : 'organName',
			    	 header:"所属机构",
					 width : 10,
					 sortable:false,
					 menuDisabled:true,
					 //align:"left",
					 titleAlign:"center",
					 renderer:function(value, metadata, record){
					 	 var belongType = record.get("belongType");
						 if(belongType!=undefined && belongType!=null && belongType!=""){
						    metadata.tdAttr = " data-qtip = '"+(belongType=='10'?'所有机构':value)+"'";
						 }
						 metadata.style="white-space: normal !important;";
						 return belongType=='10'?'<font color=blue>所有机构</font>':'<font color=green>'+value+'</font>';
					 }
		    		},{
			    	 dataIndex : 'parentQuarterName',
			    	 header:"继承岗位",
					 width : 10,
					 sortable:false,
					 menuDisabled:true,
					// align:"left",
					 titleAlign:"center",
					 renderer:function(value, metadata, record){
					 	 //value = me.replaceString(value,"\n","<br>");
						 if(value!=undefined && value!=null && value!=""){
						    metadata.tdAttr = " data-qtip = '"+value+"'";
						 }
						 metadata.style="white-space: normal !important;";
						 return value;
					 }
		    		},{
			    	 dataIndex : 'belongType',
			    	 header:"所属类型",
					 width : 5,
					 sortable:false,
					 menuDisabled:true,
					 align:"center",
					 renderer:function(value, metadata, record){
						 if(value!=undefined && value!=null && value!=""){
						    metadata.tdAttr = " data-qtip = '"+(value=='10'?'系统标准岗位':'公司私有岗位')+"'";
						 }
						 metadata.style="white-space: normal !important;";
						 return value=='10'?'<font color=blue>系统标准岗位</font>':'<font color=green>公司私有岗位</font>';
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
    	me.quarteStandardGrid.store.on("load", function() {
    		 me.quarteStandardGrid.setTitle(me.titleStr+ '（共'+me.quarteStandardGrid.store.getCount()+'项）');
		});
    	return me.quarteStandardGrid;
	},
	replaceString : function(str,reallyDo,replaceWith) { 
		var e=new RegExp(reallyDo,"g"); 
		words = str.replace(e, replaceWith); 
		return words; 
	},
	// 打开表单页面方法
	openFormWin :  function(id,callback,readOnly,data,term,oper){
			var me = this;
			var formConfig = {};
			var readOnly = readOnly || false;
			formConfig.readOnly = readOnly;
			formConfig.fileUpload = true;           
			formConfig.formUrl = "baseinfo/quarterStandard/saveForQuarterStandardFormAction!save.action?op="+me.op;
			formConfig.findUrl = "baseinfo/quarterStandard/findForQuarterStandardFormAction!find.action?op="+me.op;
			formConfig.callback = callback;
			formConfig.columnSize = 2;
			formConfig.jsonParemeterName = 'form';
			formConfig.bodyStyle = "padding-top:5px;";
			formConfig.items = new Array();
			formConfig.oper = oper;// 复制操作类型变量
			formConfig.items.push({
						colspan : 1,
						xtype : 'hidden',
						name : 'quarterId'
					});
			if(me.op=='oginput' || me.op=='ogview'){
			formConfig.items.push({
						colspan : 2,
						checked : true,
						fieldLabel : '继承岗位',
						name : 'parentQuarterStandard',
						//addPickerWidth:100,
						xtype : 'selecttree',
						nameKey : 'quarterId',
						nameLable : 'quarterName',
						readerRoot : 'children',
						keyColumnName : 'id',
						titleColumnName : 'title',
						childColumnName : 'children',
						selectType : 'quarter',
						selectTypeName : '岗位',
						allowBlank : true,
						readOnly : readOnly,
						selectUrl : 'baseinfo/quarterStandard/getSysStandardQuarterForQuarterStandardListAction!getSysStandardQuarter.action?notQid='+id,
						width : 600
					});
			}
			formConfig.items.push({
						colspan : 1,
						fieldLabel : '岗位名称',
						name : 'quarterName',
						xtype : 'textfield',
						allowBlank : false,
						readOnly : readOnly,
						maxLength : 32,
						width : 280,
						validator : function(thisText){
							if(!!thisText){ //不为空
										if(base.Login.userSession.currentOrganId == null){
											me.IsExsit = "未登录平台!";
										}else{
											me.code_quarterId = me.form.getForm().findField('quarterId').getValue();
											if(oper == 'add'){
												me.code_quarterId = "11111111111111111111111111111111111";
											}
											if(me.code_quarterId!=undefined &&  me.code_quarterId != null 
											      && me.code_quarterId!="" && me.code_quarterId!="null"){
												Ext.Ajax.request({
													url : 'baseinfo/quarterStandard/existQuarterStrandardNameForQuarterStandardListAction!existQuarterStrandardName.action',
													method : 'post',
													timeout: 30000,
													params : {
														nameTerm : thisText,
														id	  : me.code_quarterId,
														op : me.op,
														organ: base.Login.userSession.currentOrganId,
														deptName : me.quarterName 
													},
													success:function(response){
														var re = Ext.decode(response.responseText);
														//console.log(re);
														if(re['isExist']=='0'){
															me.IsExsit = true;
														}else{
															me.IsExsit = '岗位名称已存在';
															//me.form.getForm().findField('examTitle').markInvalid('此名称已存在');
														}
													},
													failure:function(){
														Ext.Msg.alert("信息","未能与服务器取得通讯");
													}
												});
											}else{
												me.IsExsitFlag = false;
												me.IsExsit = true;
											}
										}
							}
							if(me.IsExsitFlag){
								me.IsExsitFlag = false;
								me.IsExsit = true;
							}
							return me.IsExsit;
						}
					});
			if(me.standardnums == 0){
			formConfig.items.push({
						colspan : 1,
						fieldLabel : '编码',
						name : 'quarterCode',
						xtype : 'textfield',
						allowBlank : false,
						readOnly : readOnly,
						maxLength : 32,
						width : 280,
						validator : function(thisText1){
							if(!!thisText1){ //不为空
										if(base.Login.userSession.currentOrganId == null){
											me.IsExsit1 = "未登录平台!";
										}else{
											me.code_quarterId = me.form.getForm().findField('quarterId').getValue();
											if(oper == 'add'){
												me.code_quarterId = "11111111111111111111111111111111111";
											}
											if(me.code_quarterId!=undefined &&  me.code_quarterId != null 
											      && me.code_quarterId!="" && me.code_quarterId!="null"){
												Ext.Ajax.request({
													url : 'baseinfo/quarterStandard/existQuarterStrandardCodeForQuarterStandardListAction!existQuarterStrandardCode.action',
													method : 'post',
													timeout: 30000,
													params : {
														nameTerm : thisText1,
														id	  : me.code_quarterId,
														op : me.op,
														organ: base.Login.userSession.currentOrganId,
														deptName : me.quarterName 
													},
													success:function(response){
														var re = Ext.decode(response.responseText);
														//console.log(re);
														if(re['isExist']=='0'){
															me.IsExsit1 = true;
														}else{
															me.IsExsit1 = '岗位编码已存在';
															//me.form.getForm().findField('examTitle').markInvalid('此名称已存在');
														}
													},
													failure:function(){
														Ext.Msg.alert("信息","未能与服务器取得通讯");
													}
												});
											}else{
												me.IsExsit1Flag = false;
												me.IsExsit1 = true;
											}
										}
							}
							if(me.IsExsit1Flag){
								me.IsExsit1Flag = false;
								me.IsExsit1 = true;
							}
							return me.IsExsit1;
						}
					});
			}else{
				formConfig.items.push({
						colspan : 1,
						xtype : 'hidden',
						name : 'quarterCode'
					});
				formConfig.items.push({
						colspan : 1,
						fieldLabel : '编码',
						name : 'quarterCodeShow',
						xtype : 'displayfield'
					});
			}
			formConfig.items.push({
						colspan : 2,
						fieldLabel : '部门',
						name : 'deptName',
						addPickerWidth:100,
						xtype : 'selecttree',
						nameKey : 'id',
						nameLable : 'title',
						readerRoot : 'children',
						keyColumnName : 'id',
						titleColumnName : 'title',
						childColumnName : 'children',
						selectType : 'dept',
						selectTypeName : '模块',
						allowBlank : false,
						readOnly : readOnly,
						editorType:'str',
						selectUrl : 'baseinfo/quarterStandard/getQuarterStandardDeptForQuarterStandardListAction!getQuarterStandardDept.action',
						width : 280
					});
			formConfig.items.push({
						colspan : 1,
						xtype : 'hidden',
						name : 'specialityName'
					});
			/*formConfig.items.push({
						colspan : 1,
						fieldLabel : '专业',
						name : 'specialityName',
						addPickerWidth:100,
						xtype : 'selecttree',
						nameKey : 'id',
						nameLable : 'title',
						readerRoot : 'children',
						keyColumnName : 'id',
						titleColumnName : 'title',
						childColumnName : 'children',
						selectType : 'speciality',
						selectTypeName : '专业',
						allowBlank : true,
						readOnly : readOnly,
						editorType:'str',
						selectUrl : 'baseinfo/quarterStandard/getQuarterSpecialityForQuarterStandardListAction!getQuarterSpeciality.action',
						width : 280
					});*/
		    if(oper == 'view'){
		    	formConfig.items.push({
						colspan : 1,
						fieldLabel : '所属类型',
						name : 'belongType',
						xtype : 'select',
						data:[['10','系统标准岗位'],['20','公司私有岗位']],
						readOnly : readOnly,
						defaultValue : '20',
						width : 280
					});
		    }else if(me.op=='stinput'){
		    		formConfig.items.push({
						colspan : 1,
						name : 'belongType',
						xtype : 'hidden',
						value : '10'
					});
					formConfig.items.push({
						colspan : 1,
						fieldLabel : '所属类型',
						name : 'belongTypeShow',
						xtype : 'displayfield',
						value : '系统标准岗位'
					});
		    }else if(me.op=='oginput'){
		   			 formConfig.items.push({
						colspan : 1,
						name : 'belongType',
						xtype : 'hidden',
						value : '20'
					});
					formConfig.items.push({
						colspan : 1,
						fieldLabel : '所属类型',
						name : 'belongTypeShow',
						xtype : 'displayfield',
						value : '公司私有岗位'
					});
		    }
			formConfig.items.push({
						colspan : 1,
						fieldLabel : '已关联标准',
						name : 'standardnumsShow',
						xtype : 'displayfield',
						value : me.standardnums
					});
            formConfig.items.push({
						colspan : 1,
						name : 'deptId',
						xtype : 'hidden'
					});
		    
			formConfig.items.push({
						colspan : 1,
						name : 'specialityCode',
						xtype : 'hidden'
					});
			formConfig.items.push({
						colspan : 1,
						name : 'remark',
						xtype : 'hidden'
					});
			formConfig.items.push({
						colspan : 1,
						name : 'dcType',
						xtype : 'hidden'
					});
			formConfig.items.push({
						colspan : 1,
						name : 'sortNum',
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
						name : 'organId',
						xtype : 'hidden'
					});
			formConfig.items.push({
						colspan : 1,
						name : 'organName',
						xtype : 'hidden'
					});
			
			formConfig.items.push({
						colspan : 1,
						name : 'parentQuarterId',
						xtype : 'hidden'
					});
			formConfig.items.push({
						colspan : 1,
						name : 'parentQuarterName',
						xtype : 'hidden'
					});
			formConfig.items.push({
						colspan : 1,
						name : 'parentQuarterCode',
						xtype : 'hidden'
					});
			me.form = ClassCreate('base.model.Form', formConfig);
			me.form.parentWindow = this;
			// 表单窗口
			var formWin = new WindowObject({
						layout : 'fit',
						title : '标准岗位维护',
						//height : 500,
						autoHeight : true,
						width : 650,
						border : false,
						frame : false,
						modal : true,// 模态
						closeAction : 'hide',
						items : [me.form]
					});
			me.form.setFormData(id,function(result){
				if(oper == 'add'){
					me.form.getForm().findField("deptName").setValue(me.quarterName);
				}else{
					
				}
				me.form.getForm().findField("standardnumsShow").setValue(me.standardnums);
				var quarterCodeShowObject = me.form.getForm().findField("quarterCodeShow");
				if(quarterCodeShowObject){
					quarterCodeShowObject.setValue(me.form.getForm().findField("quarterCode").getValue());
				}
				if(!(oper == 'add' || oper == 'view')){
					setTimeout(function(){
						me.form.getForm().isValid();
						formWin.show();
					},300);
				}
			});
			if(oper == 'add' || oper == 'view'){
				formWin.show();
			}
			
	}
});