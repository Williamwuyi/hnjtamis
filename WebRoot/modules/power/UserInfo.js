/**
 * 当前用户的权限设置
 */
ClassDefine('modules.power.UserInfo', {
	extend : 'base.model.Form',
	initComponent : function() {
		var me = this;
		me.keyColumnName = "userId";// 主健属性名
		me.columnSize = 2;
		me.jsonParemeterName = 'form';
		me.findUrl = "power/user/findForUserFormAction!find.action";
		me.formUrl = "power/user/saveMyForUserFormAction!saveMy.action";
		me.items = new Array();
		me.items.push({
					colspan : 2,
					xtype : 'hidden',
					name : 'userId'
				});
		me.items.push({
					colspan : 2,
					fieldLabel : '账号',
					name : 'account',
					xtype : 'textfield',
					allowBlank : false,
					maxLength : 32,
					labelWidth:100,
					readOnly:true
				});
		/*me.items.push({
					colspan : 1,
					fieldLabel : '缺省登录系统',
					name : 'app',
					xtype : 'selectobject',
					allowBlank : false,
					valueField : 'appId',
					displayField : 'appName',
					jsonParemeterName : 'list',
					labelWidth:120,
					selectUrl : 'funres/app/allForAppListAction!all.action'
				});	*/
	    me.items.push({
					colspan : 1,
					fieldLabel : '新密码',
					name : 'password1',
					xtype : 'textfield',
					inputType:'password',
					labelWidth:100,
					maxLength : 32
				});
	    me.items.push({
					colspan : 1,
					fieldLabel : '确认密码',
					name : 'password2',
					xtype : 'textfield',
					inputType:'password',
					labelWidth:120,
					maxLength : 32
				});
		/*me.items.push({
					colspan : 1,
					fieldLabel : '代理员工',
					name : 'proxyEmployee',
					xtype : 'selectobject',
					valueField : 'employeeId',
					displayField : 'employeeName',
					jsonParemeterName : 'list',
					enableSelectOne : false,//缺省不选择代理员工
					labelWidth:100,
					selectUrl : 'organization/employee/listForEmployeeListAction!list.action?deptTerm='+base.Login.userSession.deptId+"&limit=999"
				});
	    me.items.push({
					colspan : 1,
					fieldLabel : '代理终止日期',
					name : 'proxyDate',
					xtype : 'datefield',
					format:'Y-m-d',
					labelWidth:120,
					value : 0
				});
		me.items.push({
					colspan : 2,
					width : 560,
					fieldLabel : '代理功能',
					checked : true,
					name : 'proxyResources',
					xtype : 'selecttree',
					nameKey : 'resourceId',
					nameLable : 'resourceName',
					//readerRoot : 'children',
					selectType:'resource',
					keyColumnName : 'id',
					titleColumnName : 'title',
					childColumnName : 'children',
					labelWidth:100,
					selectUrl : "funres/module/findAMRTreeForModuleListAction!findAMRTree.action?userId="+base.Login.userSession.userId+"&proxy=true"
				});*/
		/*me.ztlx = modules.baseinfo.Dictionary.createDictionarySelectPanel(
			              '主题类型','THEME_TYPE','theme',true,null,true,false);
		me.ztlx.labelWidth = 100;            
		me.items.push(me.ztlx);*/
	    me.clearButtonEnabled = false;
		me.callParent();
	}
});