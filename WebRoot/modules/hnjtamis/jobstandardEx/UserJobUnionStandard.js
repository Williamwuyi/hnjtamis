/**
 * 根据岗位选择标准
 */
ClassDefine('modules.hnjtamis.jobstandardEx.UserJobUnionStandard', {
	 extend : 'Ext.Panel',
	 requires : ['base.model.Tree','base.core.TableMergeCells'], 
	 border : false,
	 frame : true,
	 bodyStyle : "border:0;padding:0px 0px 0px 0px;overflow-x:hidden; overflow-y:hidden",
	 defaultType : 'textfield',
	 margins : '0 0 0 0',
	 colspan : 2,
	 forceFit : true,// 自动填充列宽,根据宽度比例
	 allowMaxSize : -1,// 允许最大行数,-1为不受限制
	 layout: 'fit',
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

		me.standardnameTerm = new Ext.form.TextField({
			xtype : 'textfield',
			name  : 'standardnameTerm',
			fieldLabel : '子模块',
			labelWidth : 60,
			width:180
			
		});
		me.termform = new Ext.Toolbar({
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
							me.queryStandardTerm();
						}
					})]
		});
		me.standardTermList = me.getStandardTermList();
		me.items = [
	    	me.standardTermList
	    ];
		me.callParent();
		me.queryStandardTerm();
	 },
	queryStandardTerm : function(){
		var me = this;
		var url = 'jobstandard/termsEx/listForStandardTermsExListAction!list.action?nodeType=userquarter';
		url+="&standardnameTerm="+me.standardnameTerm.getValue();
		me.standardTermGrid.store.proxy.url = url;
		me.standardTermGrid.store.load();
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
		me.standardTermGrid = new Ext.grid.GridPanel({
	    	 	//colspan : 2,
				//fieldLabel : ' ',
				//xtype : 'editlist',
				viewConfig:{autoScroll:true,height:300},//高度
				//addOperater : false,
				//deleteOperater : true,
				//enableMoveButton : true,
				//enableCheck : true,
				//readOnly : false,
				//otherOperaters:[],
				//store : me.unSelectStore,
			    tbar : [
			        me.termform,
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
						colspan : 1,
						iconCls : 'exportXls',
						text : 'Excel导出',
						handler : function() {
							window.location = 'jobstandard/jobunionstandardEx/expExcelForJobUnionStandardExportExcelAction!expExcel.action?nodeType=userquarter';
						}
					})],
				autoScroll :true,
				minSize : 80,
				autoHeight:true,
				store : me.standardTermStore,
				stripeRows:true, //斑马线效果  
				//columnLines : true,
				forceFit: true,
				//selType: 'cellmodel',  
				//title: '岗位标准条款',
				//plugins:[  
				  //Ext.create('Ext.grid.plugin.CellEditing',{  
				   // clicksToEdit:0 //设置单击单元格编辑  
				  //})  
				//],  
				//region: 'north',
				//bodyStyle:'border:0;',
				collapsible : false,
				columns:[
					{
					 dataIndex:'standardid',
					 hidden:true
					},
					new Ext.grid.RowNumberer({text:"序号",width:50,align:"center"}),
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
    		 base.core.TableMergeCells.mergeCells(me.standardTermGrid,[2,3,5],null);
    		// me.standardTermGrid.setTitle('岗位标准条款 -（共'+me.standardTermGrid.store.getCount()+'项）');
		});
    	return me.standardTermGrid;
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