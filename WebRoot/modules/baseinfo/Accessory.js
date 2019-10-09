/**
 * 附件单独维护页面
 */
 Ext.define('modules.baseinfo.Accessory',{
 	singleton: true,
	initComponent : function(){
	    var me = this;
    	me.callParent(arguments);
	},
	openWin:function(itemId,readOnly,allowMaxSize,type,callback){
		var me = this;
		var formConfig = {};
		formConfig.readOnly = readOnly;
		formConfig.fileUpload = true;
		formConfig.clearButtonEnabled = false;
		formConfig.formUrl = "baseinfo/affiche/saveFileForAfficheFormAction!saveFile.action?itemId="+itemId;
		formConfig.findUrl = "baseinfo/affiche/findFileForAfficheFormAction!findFile.action";
		formConfig.items = new Array();
		formConfig.columnSize = 2;
		var file = Ext.widget('accessory',{
					colspan : 2,
					name : 'accessories',
					readOnly : readOnly,
					accessoryType : type,
					allowMaxSize : allowMaxSize
				});
		formConfig.items.push(file);
		var form = ClassCreate('base.model.Form', formConfig);
		form.callback = callback;
		form.accessory = file;
		form.parentWindow = this;
		// 表单窗口
		var formWin = new WindowObject({
					layout : 'fit',
					title : eap_page_accessorymanger,
					height : 400,
					width : 700,
					border : false,
					frame : false,
					modal : true,// 模态
					closeAction : 'hide',
					items : [form]
				});	
		formWin.show();
		form.setFormData(itemId);
	}
})