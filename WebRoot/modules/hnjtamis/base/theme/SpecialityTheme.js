/**
 * 根据岗位选择标准
 */
ClassDefine('modules.hnjtamis.base.theme.SpecialityTheme', {
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
     isInit:true,
	 initComponent : function() {
	 	String.prototype.trim = function()
		{
			return this.replace( /(^\s*)|(\s*$)/g, '' ) ;
		};
		var me = this;
		me.bankType = me.bankType || '20';
		me.op = me.op || 'admin';//'input';
		me.sf = me.sf || 'all';//'pro';
		me.treeGrid = me.getTreeGrid();
		me.themeList =  Ext.create('modules.hnjtamis.base.theme.Theme',{bankType : me.bankType,
					op:me.op, sf:me.sf, themeBankId:'aaa', parentMe:me});
		me.items = [
	    	me.treeGrid,
	    	me.themeList
	    ];
	    
		me.callParent();
	 },
	 getTreeGrid : function(){
		var me = this;
		me.treeStore = Ext.create('Ext.data.TreeStore',{
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
				url :  "base/themebank/treeForThemeBankListAction!tree.action?op="+me.op+"&bankType="+me.bankType+"&notHaveIds=",
				reader : {
					type : 'json'
				}
			},
			//nodeParam : 'parentId',
			defaultRootId : ''
		});
		me.tbarForm = [];
		me.tbarForm.push({name : 'nameTerm',xtype:'textfield',labelAlign : 'right',labelWidth : 30,width:130,emptyText:'请输入目录名'});
		me.tbarForm.push(Ext.create("Ext.Button", {iconCls : 'query',text : '查询',
			handler : function() {
				me.store.proxy.url=me.selectUrl;
				me.store.proxy.extraParams = {nameTerm:this.previousSibling().value,op:me.op,scope:me.scope};
				me.store.load();
			}
		}));
		me.tbarForm.push('->');
		//if(me.op!='view'){
			me.optBtGroup = Ext.create("Ext.Button", {
				    text: '', 
				    icon : 'resources/icons/fam/cog_edit.png',//按钮图标
				    menu: 
				    { 
				        items: [ 
				            { 
				                iconCls : 'add',
				                resourceCode:me.addResourceCode,
								text : "新增题库",//按钮标题
								handler : function() {//按钮事件
									//Ext.Msg.alert('提示', '新增目录！');
									me.configFormWin('','add');
								}
				            } ,{ 
				                iconCls : 'update',
				                resourceCode:me.updateResourceCode,
								text : "修改题库",//按钮标题
								handler : function() {//按钮事件
									if (me.nodeId!=undefined && me.nodeId!=null && me.nodeId!='' && me.nodeId!='null') {
										//Ext.Msg.alert('提示', '要修改目录‘'+me.nodeText+'’！');
										me.configFormWin(me.nodeId,'update');
									}else{
										Ext.Msg.alert('提示', '请选择记录！');
										return;
									}
								}
				            }, {  
				                iconCls : 'remove',
				                resourceCode:me.deleteResourceCode,
								text : "删除题库",//按钮标题
								handler : function() {//按钮事件
									if (me.nodeId!=undefined && me.nodeId!=null && me.nodeId!='' && me.nodeId!='null') {
										//Ext.Msg.alert('提示', '要删除目录‘'+me.nodeText+'’！');
										Ext.MessageBox.confirm('询问', '你真要删除目录‘'+me.nodeText+'吗？',
											function(btn) {
											if (btn == 'yes'){
												me.alertDelMask = new Ext.LoadMask(me, {  
												    msg     : '数据正在处理,请稍候',  
												    removeMask  : true// 完成后移除  
												});
												me.alertDelMask.show();
												Ext.Ajax.request({
													method : 'GET',
													url : "base/themebank/deleteForThemeBankListAction!delete.action?op="+me.op+"&bankType="+me.bankType,
													timeout: 600000,
													success : function(response) {
														me.alertDelMask.hide();
														var result = Ext.decode(response.responseText);
														if(result.success==true)
														Ext.Msg.alert('信息', result.msg, function(btn) {
															me.initFlag = true;
															me.treeStore.load();
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
													params : "id=" + me.nodeId
												});
											}
										});
									}else{
										Ext.Msg.alert('提示', '请选择记录！');
										return;
									}
								}
				            }
				        ] 
				    }
				});
			me.tbarForm.push(me.optBtGroup);
		//}
		me.standardQuarterTree = new Ext.tree.TreePanel({
				store : me.treeStore,
				//id: 'leftTree',
				title : '题库',
				region: 'west',
				tbar:me.tbarForm,
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
						if(record){
							me.nodeText =record.raw.title;
							me.nodeType = record.raw.type;
							me.nodeId = record.raw.id;
							me.titleStr = '';
							me.themeList.themeBankIdTerm.setValue(me.nodeId);
							me.themeList.termQueryFun(true,'first');
							/*if(me.nodeType == 'dept'){
								me.nodeId = (me.nodeId.split("@"))[1];
								me.parentTypeName = me.nodeText;
								me.titleStr = me.nodeText;
								me.queryStandardTerm(me.nodeId,me.titleStr,me.nodeType);
							}*/
						}
					}
				}
		});
		me.parentTypeName = '';
		me.treeStore.on("load", function(mystore,node,success) {
			if(me.isInit){
				var record = mystore.getRootNode().childNodes[0];
				if(record){
					//record.expand();
					//record = record.childNodes[0];;
					me.nodeType = record.raw.type;
					me.nodeText =record.raw.title;
					me.nodeId = record.raw.id;
					me.parentTypeName = me.nodeText;
					me.titleStr = me.nodeText;
					me.themeList.themeBankIdTerm.setValue(me.nodeId);
					me.themeList.termQueryFun(true,'first');
				}
				me.isInit = false;
			}
		});
		me.treeStore.getRootNode().expand(false,false);
		return me.standardQuarterTree;
	},
	configFormWin : function(id, oper){
			var me = this;
			var formConfig = {};
			var readOnly = readOnly || false;
			formConfig.readOnly = readOnly;
			formConfig.fileUpload = true;
			formConfig.formUrl = "base/themebank/saveForThemeBankFormAction!save.action?op="+me.op;
			formConfig.findUrl = "base/themebank/findForThemeBankFormAction!find.action?op="+me.op;
			formConfig.callback = function(){
				me.themeList.themeBankIdTerm.reflash(me.themeList.themeBankIdTerm.selectUrl,true);
				me.treeStore.load();
				me.themeList.themeBankIdTerm.setValue(me.nodeId);
			};
			formConfig.columnSize = 2;
			formConfig.jsonParemeterName = 'form';
			formConfig.clearButtonEnabled = false;
			formConfig.oper = oper;// 复制操作类型变量
			formConfig.items = new Array();
			formConfig.items.push({
						xtype : 'hidden',
						name : 'themeBankId'
					});
			formConfig.items.push({
						xtype : 'hidden',
						name : 'bankType'
					});
			formConfig.items.push({
						colspan : 2,
						fieldLabel : '题库名称',
						name : 'themeBankName',
						xtype : 'textfield',
						allowBlank : false,
						readOnly : readOnly,
						maxLength : 32,
						enableKeyEvents : true,
						width : 560,
						labelWidth:110,
						listeners:{  
							'keyup':function(){
								/*var themeBankCode = me.form.getForm().findField("themeBankCode").getValue();
								if(oper == 'add' ){//|| themeBankCode=='' || themeBankCode=='null'){
									var themeBankName = me.form.getForm().findField("themeBankName").getValue();
									Ext.Ajax.request({
											url : 'base/themebank/getThemeBankCodePyForThemeBankListAction!getThemeBankCodePy.action',
											method : 'post',
											timeout: 60000,
											params : {
											    themeBankName : themeBankName
											},
											success:function(response){
												var re = Ext.decode(response.responseText);
												me.form.getForm().findField("themeBankCode").setValue(re.themeBankCode)		
											},
											failure:function(){
												Ext.Msg.alert("信息","未能与服务器取得通讯");
											}
									});
								}*/
								//var themeBankCode = me.form.getForm().findField("themeBankCode").getValue();
								var standardTypeslist = me.form.getForm().findField("standardTypeslist").getValue();
								var typesId = standardTypeslist!=undefined && standardTypeslist.length>0?standardTypeslist[0].typesId : "";
								if(oper == 'add' && typesId==''){//|| themeBankCode=='' || themeBankCode=='null'){
									Ext.Ajax.request({
											url : 'base/themebank/getNewThemeBankCodeForThemeBankListAction!getNewThemeBankCode.action',
											method : 'post',
											timeout: 60000,
											params : {
											    standardId : typesId
											},
											success:function(response){
												var re = Ext.decode(response.responseText);
												me.form.getForm().findField("themeBankCode").setValue(re.themeBankCode)		
											},
											failure:function(){
												Ext.Msg.alert("信息","未能与服务器取得通讯");
											}
									});
								}
							}  
						}
					});
			formConfig.items.push({
						colspan : 1,
						fieldLabel : '题库编码',
						name : 'themeBankCode',
						xtype : 'textfield',
						allowBlank : false,
						readOnly :  oper == 'add'? false : true,
						maxLength : 32,
						labelWidth:110,
						validator : function(thisText){
							if(!!thisText){ //不为空
										me.code_themeBankId = me.form.getForm().findField('themeBankId').getValue();
										if(oper == 'add'){
											me.code_themeBankId = "11111111111111111111111111111111111";
										}
										if(me.code_themeBankId!=undefined &&  me.code_themeBankId != null 
										      && me.code_themeBankId!="" && me.code_themeBankId!="null"){
											Ext.Ajax.request({
												url : 'base/themebank/querylistInThemeBankCodeForThemeBankListAction!querylistInThemeBankCode.action',
												method : 'post',
												timeout: 30000,
												params : {
													themeBankCode : thisText,
													id	  : me.code_themeBankId
												},
												success:function(response){
													var re = Ext.decode(response.responseText);
													//console.log(re);
													if(re['isExist']=='0'){
														me.IsExsit = true;
													}else{
														me.IsExsit = '此题库编码已存在';
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
							if(me.IsExsitFlag){
								me.IsExsitFlag = false;
								me.IsExsit = true;
							}
							return me.IsExsit;
					}
			});
			formConfig.items.push({
						colspan : 1,
						fieldLabel : '是否有效',
						xtype : 'radiogroup',
						items : [
						         {
						        	 boxLabel:'是',
						        	 name : 'isL',
						        	 inputValue : 10,
						        	 width : 60,
						        	 checked : true,
						        	 readOnly : readOnly
						         },
						         {
						        	 boxLabel:'否',
						        	 name : 'isL',
						        	 inputValue : 5,
						        	 readOnly : readOnly,
						        	 width : 120
						         }
						         ],
						allowBlank : false,
						width : 240,
						labelWidth:110
					});
			formConfig.items.push({
						colspan : 2,
						fieldLabel : '题库编码说明',
						name : 'tkCodeRemark',
						xtype : 'displayfield',
						readOnly : readOnly,
						width : 560,
						labelWidth:110,
						value:'(1)若选择了标准条款，题库编码=模块编码+子模块编码+单元编码+登录人所属电厂编码。<br>(2)若未选择标准条款，题库编码=年月日+当天新增题库数序号+登录人所属电厂编码。'
					});
			if(me.op == 'admin'){
				formConfig.items.push({
					colspan : 1,
					fieldLabel : '是否私有',
					name : 'bankPublic',
					xtype : 'select',
					data:[['10','公有'],['20','电厂私有']],
					readOnly : readOnly,
					width : 240,
					labelWidth:110
				});
				formConfig.items.push({
						colspan : 1,
						fieldLabel : '所属机构',
						name : 'organName',
						xtype : 'displayfield',
						readOnly : readOnly,
						width : 240,
						labelWidth:110
					});
			}else if(me.op == 'dc'){
				formConfig.items.push({
					name : 'bankPublic',
					xtype : 'hidden'
				});
				
				formConfig.items.push({
						colspan : 1,
						fieldLabel : '是否私有',
						name : 'bankPublicTxt',
						xtype : 'displayfield',
						readOnly : readOnly,
						width : 240,
						labelWidth:110
					});
				formConfig.items.push({
						colspan : 1,
						fieldLabel : '所属机构',
						name : 'organName',
						xtype : 'displayfield',
						readOnly : readOnly,
						width : 240,
						labelWidth:110
					});
			}
			
			var parentThemeBank = Ext.widget('selecttree',{
						colspan : 2,
						fieldLabel : '父题库',
						name : 'themeBank',
						//addPickerWidth:100,
						xtype : 'selecttree',
						nameKey : 'themeBankId',
						nameLable : 'themeBankName',
						readerRoot : 'children',
						keyColumnName : 'id',
						titleColumnName : 'title',
						childColumnName : 'children',
						readOnly : readOnly,
						selectUrl : "base/themebank/treeForThemeBankListAction!tree.action?op="+me.op+"&bankType="+me.bankType+"&notHaveIds="+(oper=='update'&&me.nodeId?me.nodeId:''),
						width : 560,
						labelWidth:110
					});
			formConfig.items.push(parentThemeBank);
			
			/*formConfig.items.push({
						colspan : 2,
						fieldLabel : '关联模块',
						name : 'standardTypeslist',
						//addPickerWidth:100,
						checked : true,
						xtype : 'selecttree',
						readOnly : readOnly,
						nameKey : 'typesId',
						nameLable : 'typesName',
						readerRoot : 'children',
						keyColumnName : 'id',
						titleColumnName : 'title',
						childColumnName : 'children',
						selectType : 'stmodel',
						selectTypeName : '关联模块',
						allowBlank : true,
						width : 560,
						labelWidth:110,
						selectUrl : 'jobstandard/termsEx/getStandardModelTreeForStandardTermsExListAction!getStandardModelTree.action',
						selectEventFun : function(combo,record,index){
								//var themeBankCode = me.form.getForm().findField("themeBankCode").getValue();
								if(oper == 'add' ){//|| themeBankCode=='' || themeBankCode=='null'){
									var standardTypeslist = me.form.getForm().findField("standardTypeslist").getValue();
									var typesId = standardTypeslist.length>0?standardTypeslist[0].typesId : "";
									Ext.Ajax.request({
											url : 'base/themebank/getNewThemeBankCodeForThemeBankListAction!getNewThemeBankCode.action',
											method : 'post',
											timeout: 60000,
											params : {
											    standardId : typesId
											},
											success:function(response){
												var re = Ext.decode(response.responseText);
												me.form.getForm().findField("themeBankCode").setValue(re.themeBankCode)		
											},
											failure:function(){
												Ext.Msg.alert("信息","未能与服务器取得通讯");
											}
									});
								}
						}
					});*/
			
			/*if(oper == 'view'){
				formConfig.items.push({
					colspan : 2,
					fieldLabel : '专业',
					name : 'specialitys',
					xtype : 'selecttree',
					checked : true,
					readOnly : readOnly,
					nameKey : 'specialityid',
					nameLable : 'specialityname',
					readerRoot : 'children',
					keyColumnName : 'id',
					titleColumnName : 'title',
					childColumnName : 'children',
					selectType : 'speciality',
					selectTypeName : '专业',
					selectUrl : 'baseinfo/speciality/treeForSpecialityListAction!tree.action',
					width : 560,
					labelWidth:110
				});
				formConfig.items.push({
				    colspan:2,
				    checked : true,
				    readOnly : readOnly,
				    fieldLabel:"岗位",
				    xtype : 'selecttree',
				    nameKey : 'postId',
				    nameLable : 'postName',
				    name:'themePostFormList',
				    //readerRoot : 'children',
				    keyColumnName : 'id',
				    titleColumnName : 'title',
				    childColumnName : 'children',
				    selectUrl : "organization/quarter/odqtreeForQuarterListAction!odqtree.action",
				    labelWidth : 110,
				    width : 560
				});
			}*/
		    /*formConfig.items.push({
		    	colspan : 2,
		    	checked : true,
				name : 'auditEmps',
				fieldLabel : '审核(试题)人',
				xtype : 'selecttree',
				nameKey : 'employeeId',
				nameLable : 'employeeName',
				readerRoot : 'children',
				selectType :'employee',//指定可选择的节点
				keyColumnName : 'id',
				titleColumnName : 'title',
				childColumnName : 'children',
				readOnly : readOnly,
				//selectUrl : "base/exammarkpeopleinfo/queryReviewerTreeForExamMarkPeopleInfoListAction!queryReviewerTree.action",//?organId="+base.Login.userSession.currentOrganId,
				selectUrl : "organization/employee/opxdetreeForEmployeeListAction!opxdetree.action",
				selectEventFun:function(combo){
				},
				validator : function(thisText){
					if(!!thisText){
						var  auditEmps = me.form.getForm().findField("auditEmps").getValue();
						var maxLen = 5;
						if(auditEmps.length>maxLen){
							return "审核(试题)人最多能设置"+maxLen+"人";
						}else{
							return true;
						}
					}else{
						return true;
					}
				},
				width : 560,
				labelWidth:110
		    });*/
		    formConfig.items.push({
				colspan : 2,
				fieldLabel : '备注',
				name : 'remark',
				xtype : 'textarea',
				readOnly : readOnly,
				maxLength : 100,
				width : 560,
				labelWidth:110,
				height : 70
			});
			formConfig.items.push({
				colspan : 1,
				fieldLabel : '发布人',
				name : 'publicName',
				xtype : 'textfield',
				allowBlank : false,
				readOnly : readOnly,
				labelWidth:110
			});
			formConfig.items.push({
				colspan : 1,
				fieldLabel : '发布时间',
				name : 'publicTime',
				xtype : 'datefield',
				format : 'Y-m-d',
				allowBlank : false,
				readOnly : readOnly,
				width : 235,
				labelWidth:110
			});
			me.form = ClassCreate('base.model.Form', formConfig);
			me.form.parentWindow = this;
			
			// 表单窗口
			var formWin = new WindowObject({
						layout : 'fit',
						title : '题库维护',
						autoHeight : true,
						width : 630,
						border : false,
						frame : false,
						modal : true,// 模态
						closeAction : 'hide',
						items : [me.form]
					});
			me.form.setFormData(id,function(result){
				if(oper == 'add'){
					if(me.nodeId){
					   me.form.getForm().setValues({'themeBank':{'themeBankId':me.nodeId,'themeBankName':me.nodeText}});
					}
					if(me.bankType){
						 me.form.getForm().setValues({'bankType' : me.bankType});
					}
					if(me.op == 'admin'){
						me.form.getForm().setValues({"bankPublic":"10","organName":base.Login.userSession.currentOrganName});
					}else if(me.op == 'dc'){
						me.form.getForm().setValues({"bankPublic":"20","bankPublicTxt":"电厂私有","organName":base.Login.userSession.currentOrganName});
					}
					me.form.getForm().setValues({
					   	'publicName' :  base.Login.userSession.employeeName == null? base.Login.userSession.account:base.Login.userSession.employeeName,
					   	'publicTime' : Ext.Date.format(new Date(), 'Y-m-d')
					});
					Ext.Ajax.request({
											url : 'base/themebank/getNewThemeBankCodeForThemeBankListAction!getNewThemeBankCode.action',
											method : 'post',
											timeout: 60000,
											params : {
											    standardId : ''
											},
											success:function(response){
												var re = Ext.decode(response.responseText);
												me.form.getForm().findField("themeBankCode").setValue(re.themeBankCode)		
											},
											failure:function(){
												Ext.Msg.alert("信息","未能与服务器取得通讯");
											}
									});
				}
			});
			formWin.show();
	},
	replaceString : function(str,reallyDo,replaceWith) { 
		var e=new RegExp(reallyDo,"g"); 
		words = str.replace(e, replaceWith); 
		return words; 
	}
});