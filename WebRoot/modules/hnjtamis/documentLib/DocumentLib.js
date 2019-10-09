/**
 * 文档库管理
 */
ClassDefine('modules.hnjtamis.documentLib.DocumentLib', {
	extend : 'base.model.List',
	requires: [
        'modules.baseinfo.Accessory',
        'base.model.Tree'
    ],
	initComponent : function() {
		var me = this;
		me.op = me.op || 'input';
		var url = location.href;
		var match = url.split(RegExp("[?&]"));
        var basePath = match[0].substr(0,match[0].indexOf("/",match[0].indexOf("/",7)+1));
		var clientBasePath = basePath;
		//if(me.ownerCt.parentWindow&&me.ownerCt.parentWindow.clientBasePath)
		  //  clientBasePath = me.ownerCt.parentWindow.clientBasePath;
		favoriteDocument = function(dcid){
			var ss = document.getElementById("favorite_"+dcid).src;
			if(ss.indexOf("unfavorite.png")!=-1){
				type = "add";
			}else{
			    type = "del";
			}
			EapAjax.request({
					timeout: 1000,
					url : 'documentLib/favoriteDocumentForDocumentLibListAction!favoriteDocument.action?id='+dcid+"&favoriteType="+type,
					success : function(response) {
						var result = Ext.decode(response.responseText);
						if(result && result.msg){
							if(type == 'add'){
								document.getElementById("favorite_"+dcid).src = "resources/images/favorite.png";
							}else{
								document.getElementById("favorite_"+dcid).src = "resources/images/unfavorite.png";
							}
							Ext.Msg.alert('信息', result.msg);
						}else{
							Ext.Msg.alert('错误提示', "操作失败");
						}
					},
					failure : function(response) {
						var result = Ext.decode(response.responseText);
						if (result && result.errors)
							Ext.Msg.alert('错误提示', result.errors);
						else
							Ext.Msg.alert('信息', '后台未响应，网络异常！');
					}
			});
		};							
		this.columns = [
		    {name:'documentId',width:0},
		    {name:'afficheId',width:0},
		    {name:'createdBy',width:0},
		    {name:'lastUpdateDate',width:0},
		    {header:'文档类型',name:'documentType',width:1.5,sortable:false,menuDisabled:true,align:"center"},
		    {header:'专业',name:'specialityNames',width:3,sortable:false,menuDisabled:true,align:"center",
				renderer:function(value, metadata, record){ 
				    if(value!=undefined && value!=null && value!=""){
				    	metadata.tdAttr = " data-qtip = '"+value+"'";
				    }
				    metadata.style="white-space: normal !important;";
				    return value; 
				}},
		    {header:'文档名',name:'documentName',width:7,sortable:false,menuDisabled:true,titleAlign:"center",
		   		 renderer:function(value, metadata, record){ 
				    if(value!=undefined && value!=null && value!=""){
				    	metadata.tdAttr = " data-qtip = '"+value+"'";
				    }
				    var afficheId = record.get("afficheId");
				    //metadata.style="white-space: normal !important;";
				    //return "<a href='javascript:this.showSDoc(\""+record.get("documentId")+"\")'>"+value+"</a>";; 
				    if(afficheId!=undefined && afficheId!=null){
				       return "<a href='"+clientBasePath+"/baseinfo/affiche/outputFileForAfficheFormAction!outputFile.action?id="+afficheId+"' target='_new'>"+value+"</a>";
				    }else{
				    	return value;
				   	}
			}},
			{header:'发行日期',name:'fxDay',width:2,sortable:false,menuDisabled:true,align:"center",
				renderer:function(value, metadata, record){ 
				    if(value!=undefined && value!=null && value!=""){
				    	metadata.tdAttr = " data-qtip = '"+value+"'";
				    }
				    //metadata.style="white-space: normal !important;";
				    return value; 
				}},
		    {header:'编著人',name:'writerUser',width:2,sortable:false,menuDisabled:true,align:"center",
		    	renderer:function(value, metadata, record){ 
				    if(value!=undefined && value!=null && value!=""){
				    	metadata.tdAttr = " data-qtip = '"+value+"'";
				    }
				    //metadata.style="white-space: normal !important;";
				    return value; 
				}},
		    {header:'出版社',name:'publishers',width:0,sortable:false,menuDisabled:true,align:"center",
		    	renderer:function(value, metadata, record){ 
				    if(value!=undefined && value!=null && value!=""){
				    	metadata.tdAttr = " data-qtip = '"+value+"'";
				    }
				    //metadata.style="white-space: normal !important;";
				    return value; 
				}},
			{header:'发布单位',name:'originalOrganName',width:2,sortable:false,menuDisabled:true,align:"center",
				renderer:function(value, metadata, record){ 
				    if(value!=undefined && value!=null && value!=""){
				    	metadata.tdAttr = " data-qtip = '"+value+"'";
				    }
				    metadata.style="white-space: normal !important;";
				    return value; 
				}},
			/*{header:'最后修改人',name:'lastUpdatedBy',width:1.8,sortable:false,menuDisabled:true,align:"center",
				renderer:function(value, metadata, record){
					if(value!=undefined && value!=null && value!=""){
						return value;
					}else{
						return record.get("createdBy");
					}
			}},
			{header:'最后修改时间',name:'lastUpdateDate',width:3,sortable:false,menuDisabled:true,align:"center",
				renderer:function(value, metadata, record){
					if(value!=undefined && value!=null && value!=""){
						return value;
					}else{
						return record.get("creationDate");
					}
			}},*/
			{header:'是否发布',name:'isAnnounced',width:me.op=='view'?0:1.5,sortable:false,menuDisabled:true,align:"center",
				renderer:function(value, metadata, record){ 
				    if(value == 10){
				    	return "<font color=red>草稿</font>";
				    }else{
				    	return "<font color=blue>发布</font>";
				    }
				    return ""; 
			}},
			{header:'发布时间',name:'creationDate',width:2,sortable:false,menuDisabled:true,align:"center",
				renderer:function(value, metadata, record){ 
					 var isAnnounced = record.get("isAnnounced");
					 if(isAnnounced == 10){
					 	return "-";
					 }else{
						var lastUpdateDate = record.get("lastUpdateDate");
					    if(lastUpdateDate!=undefined && lastUpdateDate!=null && lastUpdateDate!='' && lastUpdateDate.length>9){
					    	return lastUpdateDate.substring(0,10);
					    }else if(value!=undefined && value!=null && value!='' && value.length>9){
					    	return value.substring(0,10);
					    }else{
					    	return "-";
					    }
					 }
			}},
			{header:'收藏',name:'tofavorite',width:me.op=='view'?1:0,sortable:false,menuDisabled:true,align:"center",
				renderer:function(value, metadata, record){ 
					var documentId = record.get("documentId");
				    if(value == 'true'){
				    	return "<a href='javascript:favoriteDocument(\""+documentId+"\")'><img id='favorite_"+documentId+"' src='resources/images/favorite.png' /></a>";
				    }else{
				    	return "<a href='javascript:favoriteDocument(\""+documentId+"\")'><img id='favorite_"+documentId+"' src='resources/images/unfavorite.png' /></a>";
				    }
			}}
		]; 
		showSDoc = function(id){
			me.openFormWin(id, function() {},true,null,{},'view');
		};
		this.showTermSize = 1;//设定查询条件出现的个数
		me.queryTypeSelect = Ext.widget('select',{
			fieldLabel:"查询方式",
			name:'queryTypeTerm',
			xtype : 'select',
			data:[[null,'全部'],['favoriteType','仅显示收藏文档']],
			labelWidth : 75,
			width:270
		});
		me.documentTypeSelect = Ext.widget('select',{
			fieldLabel:"文档类型",
			name:'documentTypeTerm',
			xtype : 'select',
			data:[[null,'全部'],['10','课件'],['20','教材'],['40','视频'],['50','题库'],['30','其他']],
			labelWidth : 75,
			width:270
		});
		this.terms = [{
						xtype : 'textfield',
						name : 'documentNameTerm',
						fieldLabel : '文档名',
						labelWidth : 75,
						width:270
					},{
						name : 'organTerm',
						fieldLabel : '发布单位',
						xtype : 'selecttree',
						addPickerWidth:200,
						nameKey : 'organId',
						nameLable : 'organName',
						readerRoot : 'organs',
						labelWidth : 75,
						width:270,
						keyColumnName : 'organId',
						titleColumnName : 'organName',
						childColumnName : 'organs',
						editorType:'str',//编辑类型为字符串，不是对象
						selectUrl : "organization/organ/listForOrganListAction!list.action?topOrganTerm="
					},{
						name : 'specialityTerm',
						fieldLabel : '归属专业',
						addPickerWidth : 40,
						xtype : 'selecttree',
						nameKey : 'specialityid',
						nameLable : 'specialityname',
						// readerRoot : 'quarters',
						keyColumnName : 'id',
						titleColumnName : 'title',
						childColumnName : 'children',
						editorType : 'str',// 编辑类型为字符串，不是对象
						labelWidth : 75,
						width:270,
						selectUrl : "baseinfo/speciality/treeForSpecialityListAction!tree.action?jobidTerm="
					},me.documentTypeSelect,me.queryTypeSelect];
		this.keyColumnName = "documentId";// 主健属性名
		this.jsonParemeterName = "list";
		this.pageRecordSize = 10;
		this.viewOperater = true;
		this.addOperater = me.op=='input'||me.op=='admin'?true:false;
		this.updateOperater = false;
		this.deleteOperater = me.op=='input'||me.op=='admin'?true:false;
		this.otherOperaters = [];//其它扩展按钮操作
		this.listUrl = "documentLib/listForDocumentLibListAction!list.action?op="+me.op;// 列表请求地址
		this.deleteUrl = "documentLib/deleteForDocumentLibListAction!delete.action?op="+me.op;// 删除请求地址
		
		if(me.op=='input' || me.op=='admin'){
				this.otherOperaters.push(Ext.create("Ext.Button", {
							iconCls : 'update',
							text : eap_operate_update,
							resourceCode:me.updateResourceCode,
							handler : function() {
								var selected = me.getSelectionModel().selected;
								if (selected.getCount() == 0) {
									Ext.Msg.alert('提示', '请选择记录！');
									return;
								}
								var id = "";
								var record = null;
								var state = null
								for (var i = 0; i < selected.getCount(); i++) {
									record = selected.get(i);
									id = record.get(me.keyColumnName);
									state= record.get("isAnnounced");
								}
								if(state == '20' && base.Login.userSession.account !='admin'){
									Ext.Msg.alert("提示","该文档已经发布，不能进行修改！");
									return false;
								}else{
									var queryTerm = {};
									if(me.termForm)
										queryTerm = me.termForm.getValues(false);
									me.openFormWin(id, function() {
												me.termQueryFun(false,'flash');//me.pagingToolbar.doRefresh();
											},false,record.data,queryTerm,'update');
								}
							}
						}));
		}
		
		if(me.op == 'view'){
			me.queryFatBt = Ext.create("Ext.Button", {
				xtype : 'button',//按钮类型
				iconCls : 'query',
				text : "仅收藏的文档",//按钮标题
				timeout: 60000,
				handler : function() {
					me.termFormWin.close();
					me.queryTypeSelect.setValue('favoriteType');
					me.termQueryFun(true,'first');	
				}
			});
			this.otherOperaters.push(me.queryFatBt);
			
			me.queryAllBt = Ext.create("Ext.Button", {
				xtype : 'button',//按钮类型
				icon : 'resources/icons/fam/table_refresh.png',//按钮图标
				text : "显示全部文档",//按钮标题
				timeout: 60000,
				handler : function() {
					me.termFormWin.close();
					me.queryTypeSelect.setValue(null);
					me.termQueryFun(true,'first');	
				}
			});
			this.otherOperaters.push(me.queryAllBt);
		}
		/*this.otherOperaters.push({
			xtype : 'button',//按钮类型
			icon : 'resources/icons/fam/folder_go.gif',//按钮图标
			text : "查看附件",//按钮标题
			handler : function() {//按钮事件
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
				modules.baseinfo.Accessory.openWin(id,true,1,'single',function(id){});
			}
		});*/
		
		
		//打开表单
		this.openFormWin = function(id,callback,readOnly,data,term,oper){
			var type = "single"
			var allowMaxSize = 1;
			
			var formConfig = {};
			var readOnly = readOnly || false;
			formConfig.readOnly = readOnly;
			formConfig.fileUpload = true;
			formConfig.clearButtonEnabled = false;
			formConfig.formUrl = "baseinfo/affiche/saveFileForAfficheFormAction!saveFile.action?itemId="+id;
			formConfig.findUrl = "baseinfo/affiche/findFileForAfficheFormAction!findFile.action";
			formConfig.callback = callback;
			formConfig.columnSize = 2;
			//formConfig.jsonParemeterName = 'form';
			formConfig.items = new Array();
			formConfig.oper = oper;// 复制操作类型变量
			formConfig.saveButtonEnabled = false;
			formConfig.items.push({
				xtype:'hidden',
				name:'documentId'
			});
			formConfig.items.push({
				xtype:'hidden',
				name:'isAnnounced'
			});
			formConfig.items.push({
				colspan: 1,
				xtype:'hidden',
				name:'isAudited'
			});
			formConfig.items.push({
				colspan:2,
				fieldLabel:"文档名",
				name:'documentName',
				xtype:readOnly?'displayfield':'textfield',
				allowBlank:false,
				readOnly : readOnly,
				maxLength : 50,
				labelWidth : 90,
				width:652
			});
			if(readOnly || oper=='view'){
				formConfig.items.push({
					colspan:1,
					fieldLabel:"文档类型",
					xtype:'displayfield',
					labelWidth : 90,
					columns:3,
					width:250,
					readOnly : readOnly,
					name:'documentType'
				});
			}else{
				formConfig.items.push({
					xtype:'hidden',
					name:'documentType'
				});
				/*me.documentTypeId = Ext.create("Ext.form.RadioGroup",{
					colspan:1,
					fieldLabel:"文档类型",
					xtype:'radiogroup',
					allowBlank:false,
					labelWidth : 90,
					columns:3,
					width:250,
					readOnly : readOnly,
					items:[{boxLabel:'课件',name:'documentTypeId',inputValue:"10",readOnly : readOnly},
						{boxLabel:'教材',name:'documentTypeId',inputValue:"20",readOnly : readOnly},
						{boxLabel:'其他',name:'documentTypeId',inputValue:"30",readOnly : readOnly}]
				});
				formConfig.items.push(me.documentTypeId);*/
				
				me.documentTypeId = Ext.widget('select',{
					fieldLabel:"文档类型",
					allowBlank:false,
					name:'documentTypeId',
					xtype : 'select',
					data:[['10','课件'],['20','教材'],['40','视频'],['50','题库'],['30','其他']],
					labelWidth : 90
				});
				formConfig.items.push(me.documentTypeId);
			}
			formConfig.items.push({
				colspan:1,
				fieldLabel:"编著人",
				name:'writerUser',
				xtype:readOnly?'displayfield':'textfield',
				readOnly : readOnly,
				maxLength : 10,
				labelWidth : 90
			});
			formConfig.items.push({
				colspan:1,
				fieldLabel:"出版社",
				name:'publishers',
				xtype:readOnly?'displayfield':'textfield',
				readOnly : readOnly,
				maxLength : 20,
				labelWidth : 90
			});
			formConfig.items.push({
				colspan:1,
				fieldLabel:"发行时间",
				name:'fxDay',
				xtype:readOnly?'displayfield':'textfield',
				maxLength : 10,
				labelWidth : 90
			});
			formConfig.items.push({
				name : 'specialityList',
				colspan:2,
				checked : true,
				fieldLabel : '专业',
				//addPickerWidth : 80,
				xtype : 'selecttree',
				nameKey : 'specialityid',
				nameLable : 'specialityname',
				// readerRoot : 'quarters',
				keyColumnName : 'id',
				titleColumnName : 'title',
				childColumnName : 'children',
				readOnly : readOnly,
				selectUrl : "baseinfo/speciality/treeForSpecialityListAction!tree.action?jobidTerm=",
				allowBlank:false,
				labelWidth : 90,
				width:652
			});
			formConfig.items.push({
				colspan:2,
				fieldLabel:"描述",
				name:'description',
				xtype:readOnly?'displayfield':'textfield',
				readOnly : readOnly,
				maxLength : 150,
				labelWidth : 90,
				width:652
			});
			formConfig.items.push({
				colspan:2,
				fieldLabel:"备注",
				name:'remark',
				xtype:readOnly?'displayfield':'textfield',
				readOnly : readOnly,
				maxLength : 150,
				labelWidth : 90,
				width:652
			});
			me.file = Ext.widget('accessory',{
					colspan : 2,
					name : 'accessories',
					readOnly : readOnly,
					accessoryType : type,
					allowMaxSize : allowMaxSize,
					addButtonEnabled : false,
					updateButtonEnabled : true
			});
			me.file.removeAccessory = function(){
				var selected = me.file.getSelectionModel().selected;
				for(var i=0;i<selected.getCount();){
			         var record = selected.get(0);
			         if(record.fileField){
			            //Ext.form.field.File.superclass.destroy.call(record.fileField);
			            record.fileField.fireEvent('blur');
			         }
			         me.file.store.remove(record);
				 }
				  me.file.addAccessory();
			};
			
		    formConfig.items.push(me.file);
		    
		    me.otherOperatersConfig = [];
		    if(!readOnly){
		    	me.otherOperatersConfig.push(Ext.create("Ext.Button", {
						icon : 'resources/icons/fam/book.png',//按钮图标
						text : '选择岗位',
						handler : function() {
							var selectIds = "";
							for (var i = 0; i <me.listForm.store.getCount(); i++) {
								var record = me.listForm.store.getAt(i);
								selectIds+= record.get("deptName")+"@"+record.get("quarterTrainCode")+",";
							}
							me.QConfig = {
								width:700,
								height:500,
								//数据提取地址
						    	selectUrl:"jobstandard/termsEx/getQuarterStandardForStandardTermsExListAction!getQuarterStandard.action?selectIds="+selectIds,
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
							base.model.Tree.openWin(me.QConfig,function(ids,selectObject){//ID数组，对象数组
							       var eapMaskTip = EapMaskTip(document.body);
								    for (var i = 0; i <me.listForm.store.getCount(); i++) {
								    	me.listForm.store.remove(me.listForm.store.getAt(i--));
								    }
								    if (ids != null && ids != "") {
									    var idarray = ids.split(',');
									    for(var i = 0; i < idarray.length; i++){
									    	var rowIndex = me.listForm.store.getCount();
											var row = {};
											var ss = idarray[i].split("@");
											row['deptName']=ss[0];
											row['quarterTrainCode']=ss[1];
											row['quarterTrainName'] = selectObject[i].title;
											me.listForm.store.insert(rowIndex, row);
											
									    }
									    me.listForm.getView().refresh();
								    }
								    eapMaskTip.hide();
								});
						}
					}));	
		    }
		    me.listForm = ClassCreate('base.core.EditList',{
				colspan : 2,
				fieldLabel : '限定的岗位 [注：如需要限定岗位阅览则设置，否则为全部岗位均允许阅览]',
				name : 'documentSearchkeies',
				xtype : 'editlist',
				addOperater : false,
				deleteOperater : false,
				enableMoveButton:false,
				readOnly : readOnly,
				otherOperaters : me.otherOperatersConfig,
				viewConfig:{height:150,autoScroll:true},//高度
				columns : [
							{
								name : 'deptName',
								header : '部门',
								align:'center',
								width : 1 ,
								sortable:false,
								menuDisabled:true,
								titleAlign:"center"
							},{
								name : 'quarterTrainName',
								header : '岗位',
								align:'center',
								width : 1 ,
								sortable:false,
								menuDisabled:true,
								titleAlign:"center"
							},{
								name : 'quarterTrainCode',
								header : '岗位编码',
								align:'center',
								width : 1,
								sortable:false,
								menuDisabled:true,
								titleAlign:"center"
							}
				           ]
			});
		    formConfig.items.push(me.listForm);
		    
		    formConfig.otherOperaters = [];
		    formConfig.otherOperaters.push({
					xtype : 'button',//按钮类型
					icon : 'resources/icons/fam/user_comment.png',//按钮图标
					text : eap_operate_save,
					iconCls : 'save',
					tabIndex:900,
					handler : function() {					
						me.save();
					}
			});
			formConfig.otherOperaters.push({
					xtype : 'button',//按钮类型
					icon : 'resources/icons/fam/user_comment.png',//按钮图标
					text : "发布",//按钮标题
					tabIndex:910,
					handler : function() {
						Ext.Msg.confirm('提示', '确认保存当前信息，并发布文档信息吗，操作完成后将不能再修改文档信息？',function(bt){
	                    if(bt=='yes'){
							me.form.getForm().findField("isAnnounced").setValue("20");
							me.save();
	                    }
						});
					}
			});
		    
		    
			me.form = ClassCreate('base.model.Form',formConfig);
			me.form.parentWindow = this;
			
			//表单窗口
			var formWin = new WindowObject({
				layout:'fit',
				title:'模版管理',
				width:700,
				border:false,
				frame:false,
				modal:true,
				//autoScroll:true,
				bodyStyle:'overflow-x:auto;overflow-y:hidden;padding:5px;',
				closeAction:'hide',
				items:[me.form]
			});
			
			formWin.show();
			me.form.setFormData(id,function(result){
				if(oper == 'add'){
					me.form.getForm().findField('documentTypeId').setValue("30");
					me.form.getForm().findField('isAnnounced').setValue("10");
					//增加操作
					 me.file.addAccessory();
				}else if(oper == 'update' || oper == 'view'){
					//更新操作
					Ext.Ajax.request({
			 			timeout: 10000,
						url : "documentLib/findForDocumentLibFormAction!find.action",//保存
						async : false,
						params : {
							id : id
						},
						success : function(response) {
							var rs = Ext.decode(response.responseText);
							//console.log(rs.form);
							if(rs.form)
								me.form.getForm().setValues(rs.form);
						},
						failure : function() {
							Ext.Msg.alert('信息', '后台未响应，网络异常！');
							me.form.MaskTip.hide();
						}
					});
					
					
					
				}
			});
		};
		this.callParent();
	},
	setDocumentTypeValue : function(){
		var me = this;
		me.form.getForm().findField('documentType').setValue(me.documentTypeId.rawValue);
	},
	save:function(){
		var me = this;
		if (me.form.getForm().isValid()&&
		   (!me.validate||me.validate(me.form.getForm().getValues(false)))){
			me.form.MaskTip.show();	
			me.setDocumentTypeValue();
			var itemId = me.form.getForm().findField('documentId').getValue();
			if(itemId==undefined || itemId==null || itemId=="" || itemId=="null"){
				itemId = me.randomId(40);
				me.form.getForm().findField('documentId').setValue(itemId);
			}
			me.form.getForm().url = "baseinfo/affiche/saveFileForAfficheFormAction!saveFile.action?itemId="+itemId;
			me.form.getForm().findField('json').setValue("");
			me.form.getForm().findField('json').setValue(Ext.encode(me.form.getForm().getValues(false)));
			me.form.getForm().submit({
				success : function(form, action) {
					me.form.getForm().findField('json').setValue("");
					me.form.getForm().findField('json').setValue(Ext.encode(me.form.getForm().getValues(false)));
					Ext.Ajax.request({
			 			timeout: 10000,
						url : "documentLib/saveForDocumentLibFormAction!save.action",//保存
						params : {
							json : me.form.getForm().findField('json').getValue(),
							id : itemId
						},
						success : function(response) {
							var msg = action.result.msg;
							if (!msg)
								msg = '操作成功！';
						    me.form.MaskTip.hide();
							Ext.Msg.alert('提示', msg,function(){
								me.form.close();
								me.termQueryFun(false,'flash');
							});
						},
						failure : function() {
							Ext.Msg.alert('信息', '后台未响应，网络异常！');
							me.form.MaskTip.hide();
						}
					});
					
				},
				failure : function(form, action) {
					if (action.result && action.result.length > 0)
						Ext.Msg.alert('错误提示',
								action.result[0].errors);
					else
						Ext.Msg.alert('信息', '后台未响应，网络异常！');
				    me.form.MaskTip.hide();
				}
			});
		}
	},
	randomId : function(n) {
		var chars = ['0','1','2','3','4','5','6','7','8','9',
					'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z',
					'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'];
	     var res = "";
	     var charsLen = chars.length-1;
	     for(var i = 0; i < n ; i ++) {
	         var id = Math.ceil(Math.random()*charsLen);
	         res += chars[id];
	     }
	     return res;
	}
});