/**
 * 试题
 */
ClassDefine('modules.hnjtamis.base.downloadTemplate.DownloadTemplate', {
	extend : 'base.model.List',
	requires: [
        'modules.baseinfo.Accessory'
    ],
	initComponent : function() {
		var me = this;
		this.columns = [
		    {name:'templateId',width:0},
		    {header:'模版名',name:'templateName',width:7,sortable:false,menuDisabled:true},
		    {header:'附件数量',name:'acceNum',width:1,sortable:false,menuDisabled:true},
			{header:'所属机构',name:'organName',width:2,sortable:false,menuDisabled:true},
			{header:'创建人',name:'createdBy',width:2,sortable:false,menuDisabled:true},
 			{header:'创建时间',name:'creationDate',width:2,sortable:false,menuDisabled:true,type : 'date', format:'Y-m-d'}
		]; 
		
		this.showTermSize = 1;//设定查询条件出现的个数
		this.terms = [];
		this.keyColumnName = "templateId";// 主健属性名
		this.jsonParemeterName = "templateList";
		this.pageRecordSize = 10;
		this.viewOperater = true;
		this.addOperater = true;
		this.updateOperater = true;
		this.deleteOperater = true;
		this.otherOperaters = [];//其它扩展按钮操作
		this.listUrl = "downloadTemplate/listForDownloadTemplateListAction!list.action";// 列表请求地址
		this.deleteUrl = "downloadTemplate/deleteForDownloadTemplateListAction!delete.action";// 删除请求地址
		
		
		this.otherOperaters.push({
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
				modules.baseinfo.Accessory.openWin(id,true,5,'single',function(id){});
			}
		});
		
		this.otherOperaters.push({
			xtype : 'button',//按钮类型
			icon : 'resources/icons/fam/folder_go.gif',//按钮图标
			text : "复制代码",//按钮标题
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
				var clipBoardContent = " <font style='color:blue;font-weight: bold; font-size: 15px;'>requires引用：</font><br>" +
						" requires: ['modules.baseinfo.Accessory'] <br><br>" +
						"  <font style='color:blue;font-weight: bold; font-size: 15px;'>方法调用：</font><br>" +
						" var id = '"+id+"';<br>" +
						" modules.baseinfo.Accessory.openWin(id,true,5,'single',function(id){}); "
				me.viewTheme = new WindowObject({
						layout:'fit',
						title:'复制代码',
						height:200,
						width:520,
						border:false,
						frame:false,
						modal:true,
						autoScroll:true,
						bodyStyle:'overflow-x:auto;overflow-y:hidden;background:#FFFFFF;padding:15px;font-size:15px',
						closeAction:'hide',
						html:clipBoardContent
				});
				me.viewTheme.show();
				
				
			}
		});
		
		//打开表单
		this.openFormWin = function(id,callback,readOnly,data,term,oper){
			var type = "single"
			var allowMaxSize = 5;
			
			var formConfig = {};
			var readOnly = readOnly || false;
			formConfig.readOnly = readOnly;
			formConfig.fileUpload = true;
			formConfig.clearButtonEnabled = false;
			//formConfig.formUrl = "downloadTemplate/saveForDownloadTemplateFormAction!save.action";//保存
			//formConfig.findUrl = "downloadTemplate/findForDownloadTemplateFormAction!find.action";
			formConfig.formUrl = "baseinfo/affiche/saveFileForAfficheFormAction!saveFile.action?itemId="+id;
			formConfig.findUrl = "baseinfo/affiche/findFileForAfficheFormAction!findFile.action";
			formConfig.callback = callback;
			formConfig.columnSize = 2;
			//formConfig.jsonParemeterName = 'form';
			formConfig.items = new Array();
			formConfig.oper = oper;// 复制操作类型变量
			formConfig.saveButtonEnabled = false;
			formConfig.items.push({
				colspan: 1,
				xtype:'hidden',
				name:'templateId'
			});
			formConfig.items.push({
				colspan:2,
				fieldLabel:"模版名",
				name:'templateName',
				xtype:'textfield',
				allowBlank:false,
				readOnly : readOnly,
				maxLength : 50,
				labelWidth : 90,
				width:800
			});
			formConfig.items.push({
				colspan:2,
				fieldLabel:"关联类型",
				name:'relationType',
				xtype:'textfield',
				readOnly : readOnly,
				maxLength : 50,
				labelWidth : 90,
				width:800
			});
			formConfig.items.push({
				colspan:2,
				fieldLabel:"关联ID",
				name:'relationId',
				xtype:'textfield',
				readOnly : readOnly,
				maxLength : 150,
				labelWidth : 90,
				width:800
			});
			formConfig.items.push({
				colspan:2,
				fieldLabel:"备注",
				name:'remark',
				xtype:'textfield',
				maxLength : 50,
				labelWidth : 90,
				width : 800
			});
			
			var file = Ext.widget('accessory',{
					colspan : 2,
					name : 'accessories',
					readOnly : readOnly,
					accessoryType : type,
					allowMaxSize : allowMaxSize
			});
		    formConfig.items.push(file);
		    
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
		    
		    
			me.form = ClassCreate('base.model.Form',formConfig);
			me.form.parentWindow = this;
			
			//表单窗口
			var formWin = new WindowObject({
				layout:'fit',
				title:'模版管理',
				height:643,
				width:830,
				border:false,
				frame:false,
				modal:true,
				//autoScroll:true,
				bodyStyle:'overflow-x:auto;overflow-y:hidden;',
				closeAction:'hide',
				items:[me.form]
			});
			
			formWin.show();
			me.form.setFormData(id,function(result){
				if(oper == 'add'){	
					//增加操作
					//me.initRadiogroup(form,'isUse',5);
					
				}else if(oper == 'update' || oper == 'view'){
					//更新操作
					//me.form.getForm().findField("state").setValue("5");
					Ext.Ajax.request({
			 			timeout: 10000,
						url : "downloadTemplate/findForDownloadTemplateFormAction!find.action",//保存
						params : {
							id : id
						},
						success : function(response) {
							var rs = Ext.decode(response.responseText);
							me.form.getForm().findField("templateId").setValue(rs.form.templateId);
							me.form.getForm().findField("templateName").setValue(rs.form.templateName);
							me.form.getForm().findField("relationId").setValue(rs.form.relationId);
							me.form.getForm().findField("relationType").setValue(rs.form.relationType);
							me.form.getForm().findField("remark").setValue(rs.form.remark);
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
	save:function(){
		var me = this;
		if (me.form.getForm().isValid()&&
		   (!me.validate||me.validate(me.form.getForm().getValues(false)))){
			me.form.MaskTip.show();
			var itemId = me.form.getForm().findField('templateId').getValue();
			if(itemId==undefined || itemId==null || itemId=="" || itemId=="null"){
				itemId = me.randomId(40);
				me.form.getForm().findField('templateId').setValue(itemId);
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
						url : "downloadTemplate/saveForDownloadTemplateFormAction!save.action",//保存
						params : {
							json : me.form.getForm().findField('json').getValue()
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