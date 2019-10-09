/**
 * 用户的模块类
 */
ClassDefine('modules.power.User', {
	extend : 'base.model.List',
	requires : ['base.model.Tree','modules.baseinfo.Dictionary'], 
	statics:{
		saveTheme:function(userId,theme){
			EapAjax.request({
				method : 'GET',
				url : 'baseinfo/dic/saveThemeForUserFormAction!saveTheme.action',
				async : false,
				params:'id='+userId+"&roleIds="+theme
			});
		}
	},
	numberColumnWidth:40,
	columnLines : true,//显示表格竖线
	enableMerge : false,
	initComponent : function() {
		var me = this;
		me.rt = me.rt || 'all';
		// 模块列表对象
		this.columns = [{
					name : 'userId',
					width : 0
				},{
					name : 'dept.deptId',
					width : 0
				}, {
					name : 'account',
					header : '账号',
					width : 1.5
				}, {
					name : 'employee.employeeName',
					header : '员工',
					width : 1
				}, {
					name : 'grant',
					header : '是否授权',
					width : 1,
					renderer : function(value){
						return value==false?"未授权":"授权";
					}
				},{
				    name : 'userRoles',
					header : '对应角色',
					width : 1.5,
					renderer : function(value){
						var ret = "";
						for(var i=0;i<value.length;i++){
							if(ret=="")
							    ret = value[i].roleName;
							else
							    ret += ","+value[i].roleName;
						}
						return ret;
					}
				}, {
					name : 'validation',
					header : '是否有效',
					width : 1,
					renderer : function(value){
						return value==false?"无效":"有效";
					}
				}, {
					name : 'app.appName',
					header : '缺省登录系统',
					width : 2
				}, {
					name : 'proxyEmployee.employeeName',
					header : '代理用户',
					width : 1
				}, {
					name : 'organ.organName',
					header : '所在机构',
					width : 2.5
				}, {
					name : 'organ.organId',
					width : 0
				}];
		// 模块查询条件对象
		this.terms = [{
					xtype : 'textfield',
					name : 'accountTerm',
					fieldLabel : '账号'
				},{
					xtype : 'textfield',
					name : 'employeeTerm',
					fieldLabel : '姓名'
				},{
					name : 'deptTerm',
					fieldLabel : '机构部门',
					xtype : 'selecttree',
					nameKey : 'deptId',
					nameLable : 'deptName',
					readerRoot : 'children',
					keyColumnName : 'id',
					addPickerWidth : 200,
					titleColumnName : 'title',
					childColumnName : 'children',
					editorType:'str',//编辑类型为字符串，不是对象
					selectUrl : "organization/dept/odtreeForDeptListAction!odtree.action?organTerm="+base.Login.userSession.currentOrganId,
					//selectType : 'dept',
					//selectTypeName : '部门',
					selectEventFun:function(combo){
						/*if(combo.type=='organ'){
							this.selectObject = {};
							me.termForm.getForm().findField('organTerm').setValue(combo.value);
						}else
						    me.termForm.getForm().findField('organTerm').setValue('');*/
					}
				},{
					name : 'roleTerm',
					fieldLabel : '角色',
					xtype : 'selecttree',
					nameKey : 'roleId',
					nameLable : 'roleName',
					readerRoot : 'children',
					addPickerWidth : 50,
					selectType:'role',
					selectTypeName:'角色',
					keyColumnName : 'id',
					titleColumnName : 'title',
					childColumnName : 'children',
					editorType:'str',//编辑类型为字符串，不是对象
					selectUrl : "power/role/treeForRoleListAction!tree.action?userId="+base.Login.userSession.userId
				},{
					name : 'resourceTerm',
					fieldLabel : '功能资源',
					xtype : 'selecttree',
					nameKey : 'resourceId',
					nameLable : 'resourceName',
					readerRoot : 'children',
					addPickerWidth : 50,
					selectType:'resource',
					keyColumnName : 'id',
					titleColumnName : 'title',
					childColumnName : 'children',
					editorType:'str',//编辑类型为字符串，不是对象
					selectUrl : "funres/module/findAMRTreeForModuleListAction!findAMRTree.action?userId="
								  +base.Login.userSession.userId
				}, {
					xtype :'select',
					fieldLabel : '是否授权',
					name : 'grantTerm',
					data:[['','所有'],['0','否'],['1','是']]
				}, {
					xtype :'select',
					fieldLabel : '是否有效',
					name : 'validStr',
					data:[['','所有'],['0','否'],['1','是']]
				}];
		this.keyColumnName = "userId";// 主健属性名
		this.viewOperater = true;
		this.addOperater = true;
		this.deleteOperater = true;
		this.updateOperater = true;
		this.otherOperaters = [];//其它扩展按钮操作
		this.otherOperaters.push({
			                xtype : 'button',
							icon : 'resources/icons/fam/user_edit.png',
							text : eap_operate_retpass,
							handler : function() {
								Ext.MessageBox.confirm('询问', '你真要重置此用户密码为888888吗？',function(btn){
									if(btn!='yes') return;
									var id = me.getSelectIds();
									if(id==""||id.split(",").length>1){
										Ext.Msg.alert('提示', '请选择一条记录！');
										return;
									}
									EapAjax.request({
										method : 'GET',
										url : 'power/user/resetPasswordForUserListAction!resetPassword.action?id='+id,
										async : true,
										success : function(response) {
											var result = Ext.decode(response.responseText);
											if (Ext.isArray(result)&&result[0].errors) {
												var msg = result[0].errors;
												Ext.Msg.alert('错误', msg);
											} else {
												Ext.Msg.alert('信息', '重置成功！');
											}
										},
										failure : function() {
											Ext.Msg.alert('信息', '后台未响应，网络异常！');
										}
									});
								});
							}
						});
		this.otherOperaters.push({
			                xtype : 'button',
							icon : 'resources/icons/fam/user_edit.png',
							text : eap_operate_takerole,
							handler : function() {
								var id = me.getSelectIds();
								if(id==""){
									Ext.Msg.alert('提示', '至少选择一条记录！');
									return;
								}
								var config = {
									  width:500,
									  height:500,
									  //数据提取地址
					    		      selectUrl:"power/role/treeForRoleListAction!tree.action?userId="+
					    		                 base.Login.userSession.userId+(id.indexOf(",")==-1?("&allotUserid="+id):"")+"&rt="+me.rt,
					    		      checked:true,//是否复选
					    		      selectType:'role',//只有角色结点数据才算
					    		      selectTypeName:'角色',
					    		      levelAffect:'20',//上下级复选框的影响策略
								      keyColumnName : 'id',
									  titleColumnName : 'title',
									  childColumnName : 'children',
									  title:'分配角色',
									  selectObject:[]//选择的结点数组,支持ID数组及对象数组
					    		};
					    		//配置、回调函数
								base.model.Tree.openWin(config,function(ids,selectObject){//ID数组，对象数组
								    var eapMaskTip = EapMaskTip(document.body);
									Ext.Ajax.request({
										method : 'get',
										url : "power/user/savePopdomForUserFormAction!savePopdom.action?userIds="+
					    		                 id+"&roleIds="+ids,
										success : function(response) {
											var ret = Ext.decode(response.responseText);
											if(Ext.isArray(ret)) ret = ret[0];
											if(ret.success)
											   Ext.Msg.alert('信息', '分配成功！');
											else
											   Ext.Msg.alert('信息', ret.errors);
											eapMaskTip.hide();
											me.termQueryFun(true,'flash');
										},
										failure : function() {
											Ext.Msg.alert('信息', '后台未响应，网络异常！');
											eapMaskTip.hide();
										}
									});
								});
							}
						});
		this.otherOperaters.push({
                xtype : 'button',
				icon : 'resources/icons/fam/connect.gif',
				text : eap_operate_viewpopdem,
				handler : function() {
					var id = me.getSelectIds();
					if(id==""||id.split(",").length>1){
						Ext.Msg.alert('提示', '请选择一条记录！');
						return;
					}
					var formConfig = {keyColumnName : 'id',
						titleColumnName : 'title',
						childColumnName : 'children',width:'100%',height:'100%',					
					    selectUrl : 'power/user/userResourceTreeForUserListAction!userResourceTree.action?id='+id};
					var treepanel = ClassCreate('base.core.PageTree', formConfig);
					//表单窗口
					var formWin = new WindowObject({
								layout : 'fit',
								title : '查看权限',
								height : 500,
								width : 300,
								border : false,
								frame : false,
								modal : true,// 模态
								closeAction : 'hide',
								draggable:true,//拖动
								resizable:false, //变大小 
								items : [treepanel]
							});
					formWin.show();
				}
			});
		this.listUrl = "power/user/listForUserListAction!list.action?organTerm="+base.Login.userSession.currentOrganId;// 列表请求地址
		this.deleteUrl = "power/user/deleteForUserListAction!delete.action";// 删除请求地址
		// 打开表单页面方法
		this.openFormWin = function(id, callback, readOnly,data,term,oper) {
			var me = this;
			var formConfig = {};
			var readOnly = readOnly || false;
			formConfig.readOnly = readOnly;
			formConfig.fileUpload = false;
			formConfig.formUrl = "power/user/saveForUserFormAction!save.action";
			formConfig.findUrl = "power/user/findForUserFormAction!find.action";
			formConfig.callback = callback;
			formConfig.columnSize = 2;
			formConfig.jsonParemeterName = 'form';
			formConfig.items = new Array();
			formConfig.oper = oper;// 复制操作类型变量
			formConfig.items.push({
						colspan : 2,
						xtype : 'hidden',
						name : 'userId'
					});
			formConfig.items.push({
						colspan : 1,
						fieldLabel : '账号',
						name : 'account',
						xtype : 'textfield',
						allowBlank : false,
						readOnly : readOnly,
						maxLength : 32
					});
			formConfig.items.push({
						colspan : 1,
						fieldLabel : '机构',
						name : 'organ',
						xtype : 'selecttree',
						allowBlank : false,
						addPickerWidth:200,
						readOnly : readOnly,
						nameKey : 'organId',
						nameLable : 'organName',
						readerRoot : 'organs',
						keyColumnName : 'organId',
						titleColumnName : 'organName',
						childColumnName : 'organs',
						selectUrl : "organization/organ/listForOrganListAction!list.action?topOrganTerm="+base.Login.userSession.currentOrganId,
						selectEventFun:function(combo,record,index){
							var deptField = form.getForm().findField('dept');
							deptField.reflash("organization/dept/listForDeptListAction!list.action?organTerm="+(combo.value));
							var employeeField = form.getForm().findField('employee');
							employeeField.reflash('organization/employee/listForEmployeeListAction!list.action?organTerm='+combo.value+"&limit=999");
						}
					});
			formConfig.items.push({
						colspan : 1,
						fieldLabel : '部门',
						name : 'dept',
						xtype : 'selecttree',
						readOnly : readOnly,
						addPickerWidth:200,
						nameKey : 'deptId',
						nameLable : 'deptName',
						readerRoot : 'depts',
						keyColumnName : 'deptId',
						titleColumnName : 'deptName',
						childColumnName : 'depts',
						selectUrl : "organization/dept/listForDeptListAction!list.action?organTerm="+(data['organ.organId']?data['organ.organId']:base.Login.userSession.currentOrganId),
						selectEventFun:function(combo,record,index){
							var employeeField = form.getForm().findField('employee');
							employeeField.reflash("organization/employee/listForEmployeeListAction!list.action?deptTerm="+(combo.value||'')
							                     +'&organTerm='+form.getForm().findField('organ').getValue().organId+"&limit=999");
						}
					});
			formConfig.items.push({
						colspan : 1,
						fieldLabel : '员工',
						name : 'employee',
						xtype : 'selectobject',
						readOnly : readOnly,
						valueField : 'employeeId',
						displayField : 'employeeName',
						readerRoot : 'list',
						enableSelectOne : false,//缺省不选择员工
						selectUrl : 'organization/employee/listForEmployeeListAction!list.action?organTerm='
						           +(data['organ.organId']?data['organ.organId']:base.Login.userSession.currentOrganId)
						           +'&deptTerm='+data['dept.deptId']+"&limit=999"
					});
			formConfig.items.push({
						colspan : 1,
						fieldLabel : '缺省登录系统',
						name : 'app',
						xtype : 'selectobject',
						allowBlank : false,
						valueField : 'appId',
						displayField : 'appName',
						jsonParemeterName : 'list',
						selectUrl : 'funres/app/allForAppListAction!all.action',
						readOnly : readOnly
					});	
			formConfig.items.push({
						colspan : 1,
						fieldLabel : '允许重复登录',
						name : 'allowRepeatLogin',
						xtype : 'select',
						data:[[false,'禁止'],[true,'允许']],
						readOnly : readOnly,
						defaultValue : false
					});
			formConfig.items.push({
						colspan : 1,
						fieldLabel : '首登密码修改提示',
						name : 'tig',
						xtype : 'select',
						data:[[false,'不需要'],[true,'必须']],
						readOnly : readOnly,
						defaultValue : false
					});
		    formConfig.items.push({
						colspan : 1,
						fieldLabel : '是否有效',
						name : 'validation',
						xtype : 'select',
						data:[[false,'否'],[true,'是']],
						readOnly : readOnly,
						defaultValue : true
					});
			formConfig.items.push({
						colspan : 2,
						width : 540,
						fieldLabel : '用户角色',
						checked : true,
						name : 'userRoles',
						xtype : 'selecttree',
						readOnly : readOnly,
						nameKey : 'roleId',
						nameLable : 'roleName',
						//readerRoot : 'children',
						selectType:'role',
						keyColumnName : 'id',
						titleColumnName : 'title',
						childColumnName : 'children',
						selectUrl : "power/role/treeForRoleListAction!tree.action?userId="+base.Login.userSession.userId+"&allotUserid="+id
					});
			if(oper == 'update'){
				formConfig.items.push({
							colspan : 1,
							fieldLabel : '代理员工',
							name : 'proxyEmployee',
							xtype : 'selectobject',
							readOnly : readOnly,
							valueField : 'employeeId',
							displayField : 'employeeName',
							readerRoot : 'list',
							enableSelectOne : false,//缺省不选择代理员工
							selectUrl : 'organization/employee/listForEmployeeListAction!list.action?deptTerm='+data['dept.deptId']+"&limit=999"
						});
			    formConfig.items.push({
							colspan : 1,
							fieldLabel : '代理终止日期',
							name : 'proxyDate',
							xtype : 'datefield',
							readOnly : readOnly,
							format:'Y-m-d',
							value : 0
						});
				formConfig.items.push({
							colspan : 2,
							width : 540,
							fieldLabel : '代理功能',
							checked : true,
							name : 'proxyResources',
							xtype : 'selecttree',
							readOnly : readOnly,
							nameKey : 'resourceId',
							nameLable : 'resourceName',
							//readerRoot : 'children',
							selectType:'resource',
							selectTypeName:'功能资源',
							keyColumnName : 'id',
							titleColumnName : 'title',
							childColumnName : 'children',
							selectUrl : "funres/module/findAMRTreeForModuleListAction!findAMRTree.action?userId="+id+"&proxy=true"
						});
				formConfig.items.push(modules.baseinfo.Dictionary.createDictionarySelectPanel(
			              '主题类型','THEME_TYPE','theme',true,null,true,readOnly));
				//自定义验证方法
			    formConfig.validate=function(form){
					if(form.proxyEmployee.employeeId&&
					   form.proxyEmployee.employeeId==form.employee.employeeId){
						Ext.Msg.alert('错误', '代理用户不能为本人！');
						return false;
					}
					return true;
				}
			}
            formConfig.items.push({
						colspan : 1,
						name : 'orderNo',
						xtype : 'hidden',
						value : 0
					});
		    formConfig.items.push({
						colspan : 1,
						name : 'levelCode',
						xtype : 'hidden'
					});
			var form = ClassCreate('base.model.Form', formConfig);
			// 表单窗口
			var formWin = new WindowObject({
						layout : 'fit',
						title : '用户',
						//height : 500,
						autoHeight : true,
						width : 600,
						border : false,
						frame : false,
						modal : true,// 模态
						closeAction : 'hide',
						draggable:true,//拖动
						resizable:false, //变大小 
						items : [form]
					});
			formWin.show();
			form.setFormData(id,function(result){
				if(oper == 'add'){
					if(term){
					   var v = {'organ':{'organId':term.organTerm,'organName':''}};
					   form.getForm().setValues(v);
					}
				}else if(oper == 'update'){
				    form.getForm().findField("proxyDate").show();
				    form.getForm().findField("proxyResources").show();
				    form.getForm().findField("proxyEmployee").show();
			    }
			});
		};
		me.callParent();
	}
});