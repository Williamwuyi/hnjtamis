/**
 * 题库管理的模块类
 */
ClassDefine('modules.hnjtamis.base.themebank.ThemeBank', {
	extend : 'base.model.List',
	//requires: ['modules.baseinfo.Dictionary'],
	initComponent : function() {
		//var Dictionary = modules.baseinfo.Dictionary;//类别名
		//this.organTypes = Dictionary.getDictionaryList('ORGAN_TYPE');
		var me = this;
		me.op =  me.op || 'dc';
		me.bankType = me.bankType || '10';
		this.listeners = {
			//select:function(obj,record,index,eOpts){}	
		};
		
		showThemeInBankById = function(bankId){
			//console.log(bankId)
			//var me = this;
			me.alertMask = new Ext.LoadMask(me, {  
			    msg     : '数据加载中,请稍候',  
			    removeMask  : true// 完成后移除  
			}); 
			me.alertMask.show();
		
			var formConfig = {};
			var readOnly = readOnly || false;
			formConfig.readOnly = readOnly;
			formConfig.fileUpload = true;
			formConfig.clearButtonEnabled = false;
			formConfig.saveButtonEnabled = false;
			formConfig.callback = function() {};
			formConfig.columnSize = 1;
			formConfig.jsonParemeterName = 'themeShowForm';
			formConfig.items = new Array();
			formConfig.oper = 'view';// 复制操作类型变量
			me.addThemeListStore = new Ext.data.Store({
				fields:[{name:"sortIndex"},{name:"themeTypeName"},{name:"themeName"},{name:"defaultScore"},{name:"answerkeyStr"}],
				autoLoad :false,
				proxy:{
					type : 'ajax',
					actionMethods : "POST",
					timeout: 300000,
					url:"exam/testpaper/getHandThemeListForTestpaperListAction!getHandThemeList.action?selectThemeIds=&themeBankIds="+bankId,
		            reader : {
						type : 'json',
						root : 'themeInTemplateList',
						totalProperty : "themeInTemplateListTotal"
					}
				},
				remoteSort : false
			}); 
		    me.addThemeListGrid = new Ext.grid.GridPanel({
	    	 	store:me.addThemeListStore,
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
		    		]
	    	});
			formConfig.items.push(me.addThemeListGrid);
			me.addThemeListStore.load();
			
			formConfig.otherOperaters = [];//其它扩展按钮操作
			
		    me.form2 = ClassCreate('base.model.Form',formConfig);
			me.form2.parentWindow = this;
			
			me.isInit = true;
			me.addThemeListStore.on("load", function() {
				me.alertMask.hide();
				if(me.isInit){
	    			//表单窗口
					me.handThemeformWin = new WindowObject({
						layout:'fit',
						title:'查看试题',
						height:612,
						width:920,
						border:false,
						frame:false,
						modal:true,
						//autoScroll:true,
						bodyStyle:'overflow-x:auto;overflow-y:hidden;',
						closeAction:'destroy',
						items:[me.form2]
					});
					
					me.handThemeformWin.show();
	    			me.isInit = false;
				}
			});
			
			
	};
		
		// 模块列表对象
		this.columns = [{
					name : 'themeBankId',
					width : 0
				}, /*{
					name : 'themeBank.themeBankName',
					header : '父题库名称',
					width : 4,
					renderer:function(value, metadata, record){
						return value;
					}
				}, */{
					name : 'themeBankName',
					header : '题库名称',
					width : 4,
					renderer:function(value, metadata, record){
						var themeNum = record.get("themeNum");
						var themeBankCode = record.get("themeBankCode");
						var showValue = value.replace("("+themeBankCode+")","");
						showValue = showValue.replace("（"+themeBankCode+"）","");
						if(themeNum!=undefined && themeNum!=null && themeNum>0){
							var themeBankId = record.get("themeBankId");
							return "<a href='javascript:showThemeInBankById(\""+themeBankId+"\")'>"+showValue+"</a>";
						}else{
							return showValue;
						}
					}
				}, {
					name : 'themeBankCode',
					header : '编码',
					width : 1.2
				},  {
					name : 'themeNum',
					header : '题数',
					width : 0.8
				},{
					name : 'bankPublic',
					header : '是否私有',
					width : 0.8,
					renderer:function(value, metadata, record){
						return value == '10'?'公有':'电厂私有';
					}
				}, 
				{
					name : 'organName',
					header : '创建机构',
					width : 1
				}, /* {
					name : 'publicName',
					header : '发布人',
					width : 1
				}, 
				{
					name : 'publicTime',
					header : '发布时间',
					width : 1
				},*/
				{
					name : 'isL',
					header : '是否有效',
					width : 0.8,
					renderer : function(value){
						if(value == 5){
							return '否';
						}else if(value == 10){
							return '是';
						}else{
							return '';
						}
					}
				}/*,{
					name : 'remark',
					header : '备注',
					width : 2
				}*/];
		// 模块查询条件对象
		this.terms = [{
					xtype : 'textfield',
					name : 'nameTerm',
					width:270,
					fieldLabel : '名称'
				},{
					xtype :'select',
					fieldLabel : '是否有效',
					name : 'validStr',
					data:[['','所有'],[5,'否'],[10,'是']],
					width:200,
					defaultValue:10
				},{
					name : 'createOrganIdTerm',
					fieldLabel : '创建机构',
					xtype : 'selecttree',
					addPickerWidth:300,
					nameKey : 'organId',
					nameLable : 'organName',
					readerRoot : 'organs',
					allowBlank : true,
					width:270,
					keyColumnName : 'organId',
					titleColumnName : 'organName',
					childColumnName : 'organs',
					editorType:'str',//编辑类型为字符串，不是对象
					selectUrl : "organization/organ/listForOrganListAction!list.action?topOrganTerm="+base.Login.userSession.organId
				}];
		this.keyColumnName = "themeBankId";// 主健属性名
		this.viewOperater = true;
		this.addOperater = me.op=='query'?false:true;
		this.deleteOperater = me.op=='query'?false:true;
		this.updateOperater = me.op=='query'?false:true;
		this.jsonParemeterName = 'themeBankList';
		this.otherOperaters = [];//其它扩展按钮操作
		this.listUrl = "base/themebank/queryThemeBankListForThemeBankListAction!queryThemeBankList.action?op="+me.op+"&bankType="+me.bankType;// 列表请求地址
		this.deleteUrl = "base/themebank/deleteForThemeBankListAction!delete.action?op="+me.op+"&bankType="+me.bankType;// 删除请求地址
		//this.childColumnName = 'themeBanks';// 子集合的属性名
		if(me.op!='query'){
			var sortConfig = {};
			//列属性配置复制
			sortConfig.columns = new Array();
			for(var i=0,j=0;i<this.columns.length;i++){
				if(this.columns[i].name == 'themeBankId' || this.columns[i].name == 'themeBankName'){
				   sortConfig.columns[j] = Ext.clone(this.columns[i]);
				   delete sortConfig.columns[j].xtype;
				   delete sortConfig.columns[j].defaultValue;
				   j++;
				}
			}
			
			/*me.extThemeBtGroup = Ext.create("Ext.Button", {
				    text: "导入", 
				    icon : 'resources/icons/fam/xls.gif',//按钮图标
				    menu: 
				    { 
				        items: [{
						icon : 'resources/icons/fam/import.gif',
						text : '导入审核人',
						handler : function() {
							var importForm = new Ext.FormPanel({
						        frame:false,
						        fileUpload : true,//指定带文件上传
						        bodyStyle:'padding:10px 5px 0', 
						        defaultType:'textfield',
						        defaults: {
						           labelWidth:90,
						           labelAlign :'right'
						        },
						        monitorValid:true,
						        items:[{ 
						                fieldLabel:'导入文件', 
						                xtype:'fileuploadfield',
						                buttonText:'浏览',
						                name:'xls',
						                emptyText:'请选择EXCEL文件。',
						                colspan : 2,
						                width : 540,
						                allowBlank:false
						            }],
						        buttons:['->',{ 
						                text:'确定',
						                formBind: true, 
						                handler:function(){ 
						                    importForm.getForm().submit({ 
						                        method:'POST', 
						                        waitTitle:'导入提示', 
						                        waitMsg:'正在导入,请稍候...',
						                        url:'base/themebank/importXlsForThemeBankListAction!importXls.action',
						                        success:function(form, action){
						                        	var msg = action.result.msg; 
						                        	if (!msg) msg = '操作成功！';
						                        	Ext.Msg.alert('信息', msg,function(){
						                                 importWin.hide();
						                                 delete importWin;
						                                 me.termQueryFun();
						                            }); 
						                        	var tmpPosition = importWin.getPosition();
						                        	importWin.hide();
					                                delete importWin;
					                                me.termQueryFun();
						                        },
						                        failure:function(form, action){
						                            if(action.failureType == 'server'){
						                                var obj = Ext.decode(action.response.responseText);
						                                if(Ext.isArray(obj)) obj = obj[0];
						                                Ext.Msg.alert('导入失败', obj.errors,function(){
						                                        form.findField('xls').focus();
						                                }); 
						                            }else{ 
						                                Ext.Msg.alert('警告', '网络出现问题！'); 
						                            }                            
						                        } 
						                    }); 
						                } 
						            }]
						    });
							var importWin = new WindowObject({
										layout : 'form',
										title : '导入专业  - [导入文件必须是.xls结尾的Excel文件]',
										autoHeight : true,
										width : 600,
										border : false,
										frame : false,
										modal : true,//模态
										closeAction : 'hide',
										items : [importForm]
									});
							importWin.show();
						}
					},{
						icon : 'resources/icons/fam/export.gif',//按钮图标
						text : "导出审核人模版",//按钮标题
						handler : function() {//按钮事件
							window.open("./modules/hnjtamis/base/themebank/downMasterplate.jsp");
						}
					}
				]}
			 });
			
			this.otherOperaters.push(me.extThemeBtGroup);*/
			//打开排序页面
			this.openSortWin = function(record,term,store,callback){
				var me = this;
				sortConfig.keyColumnName = me.keyColumnName;
				sortConfig.sortlistUrl = "base/themebank/subListForThemeBankListAction!subList.action?op="+me.op+"&bankType="+me.bankType+"&parentId="+(record?record.themeBankId:'');
				sortConfig.jsonParemeterName = 'themeBanks';
				sortConfig.saveSortUrl = 'base/themebank/saveSortForThemeBankFormAction!saveSort.action?op='+me.op+"&bankType="+me.bankType;
				sortConfig.callback = callback;
				var sortPanel = ClassCreate('base.model.SortList', Ext.clone(sortConfig));
				// 表单窗口
				var sortWin = new WindowObject({
							layout : 'fit',
							title : '题库排序',
							height : 500,
							width : 700,
							border : false,
							frame : false,
							modal : true,// 模态
							closeAction : 'hide',
							items : [sortPanel]
						});
				sortPanel.store.on("load",function(){sortWin.show();});
			}
		}
		// 打开表单页面方法
		this.openFormWin = function(id, callback, readOnly,data,term,oper) {
			var me = this;
			var formConfig = {};
			var readOnly = readOnly || false;
			formConfig.readOnly = readOnly;
			formConfig.fileUpload = true;
			formConfig.formUrl = "base/themebank/saveForThemeBankFormAction!save.action?op="+me.op+"&bankType="+me.bankType;
			formConfig.findUrl = "base/themebank/findForThemeBankFormAction!find.action?op="+me.op+"&bankType="+me.bankType;
			formConfig.callback = callback;
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
						readOnly : true,//oper == 'add'? false : true,
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
						selectUrl : "base/themebank/treeForThemeBankListAction!tree.action?op="+me.op+"&bankType="+me.bankType+"&notHaveIds="+(oper=='update'&&data?data.themeBankId:''),
						width : 560,
						labelWidth:110
					});
			formConfig.items.push(parentThemeBank);
			
			formConfig.items.push({
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
					});
			
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
					if(term){
					   me.form.getForm().setValues({'themeBank':{'themeBankId':(data?data.themeBankId:''),'themeBankName':(data?data.themeBankName:'')}});
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
				}
			});
			formWin.show();
		};
		this.callParent();
	}
});