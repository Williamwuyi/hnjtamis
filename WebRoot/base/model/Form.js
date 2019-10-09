/**
 *  标准模板的表单页面类
 */
Ext.define('base.model.Form', {
	extend : 'Ext.form.FormPanel',
	border : false,
	frame : false,
	bodyStyle : "border:0",
	defaultType : 'textfield',
	margins : '2 2 2 2',
	fieldDefaults : {
		labelAlign : 'right',
		labelWidth : 90
	},
	clearButtonEnabled : true,
	saveButtonEnabled :true,
	closeButtonEnabled : true,
	initComponent : function() {
		var me = this;
		me.groupItems = [];
		me.url = me.formUrl;
		me.layout = {
			type : 'table',
			columns : me.columnSize || 1,
			tableAttrs : {
				style : {
					width : '100%'
				}
			}
		};
		if(me.bodyStyle == undefined || me.bodyStyle==''){
			me.bodyStyle = "padding-top:5px;";
		}else{
		me.bodyStyle = "padding-top:5px;"+me.bodyStyle;
		}
		
		me.items.push({name : 'json',
						xtype : 'hidden'
					});
		//处理查看时加边框、编辑框改成只显示内容
	    var manyLevel=false;//是否有多级
	    function addBolder(item,parent){
			//加边框
			if(me.oper=='view' || me.operViewReadOnly==true){
			   if(item&&item.layout&&me.ifAddTableLine!=0){
			      item.layout.tableAttrs={style:'width:100%;border-top:1px solid #dfe8f6;border-left:1px solid #dfe8f6'};
	              item.layout.tdAttrs={style: 'border-right:1px solid #dfe8f6;border-bottom:1px solid #dfe8f6'};
			   }
			   function changeEditor(it,p){
			   	  if(!it) return;
			   	  if(it.xtype=='htmleditor'||it.xtype=='textarea'){
			   	     p.autoScroll = true;
				     p.maxHeight=500;
				     delete it.height;
			   	  }
			   	  if(it.xtype=='password' || it.inputType=='password'){
			   	  	it.xtype='hidden';
			   	  }else if(it.xtype=='textfield'||it.xtype=='textarea'||it.xtype=='numberfield'
			   	    ||it.xtype=='datefield'||it.xtype=='datetimefield'||it.xtype=='htmleditor')
	           	     it.xtype='displayfield';
	           	  if(it.plugins)
	           	     delete it.plugins;
	           	  //it.style='border-right:1px solid #dfe8f6';
			   }
	           //编辑框改成只显示内容
			   if(item&&item.items)
	           Ext.Array.each(item.items,function(it){
	           	    changeEditor(it,item);
	           });
	           if(item&&!item.layout&&!item.items) 
	              changeEditor(item,parent);
			}else if(item)
	           item.bodyStyle='padding-top:5px';//不让表单第一排元素项头
		}
		function addBorderLoop(items,parent){
			Ext.Array.each(items,function(item){
				if((item&&item.layout&&item.layout.type=='table')||(item&&item.xtype=='tabpanel')){
					addBolder(item,parent);
				}else if(item&&item.items&&!(item.xtype=='editlist' || item.xtype=='radiogroup')){
				   addBorderLoop(item.items,item);
				   manyLevel=true;
				}
				
				if(me.oper=='view' || me.operViewReadOnly==true){
					if(item&&item.xtype=='radiogroup'){
						var ss = "";
						me.groupItems[me.groupItems.length] = item;
						for(var i = 0;i<item.items.length;i++){
							if(item.items[i].checked){
								ss = item.items[i].boxLabel;
								break;
							}
						}
						item.name = item.items[0].name;
						item.xtype='displayfield';
						
						
					}
				}
			});
			if(!manyLevel)
			Ext.Array.each(items,function(item){
				if((me.oper=='view'|| me.operViewReadOnly==true)&&me.ifAddTableLine!=0){
				   me.layout.tableAttrs.style='width:100%;border-top:1px solid #dfe8f6;border-left:1px solid #dfe8f6';
	               me.layout.tdAttrs={style:'border-right:1px solid #dfe8f6;border-bottom:1px solid #dfe8f6'};
				}
				addBolder(item,parent);
			});
		}
		addBorderLoop(me.items,me);
		me.tbar = ['->'];
		if (!me.readOnly) {
			if(me.otherOperaters){
				for(var i=0;i<me.otherOperaters.length;i++)
				  me.tbar.push(me.otherOperaters[i])
			}
			if(me.saveButtonEnabled)
			me.tbar.push({
				text : eap_operate_save,
				iconCls : 'save',
				tabIndex:900,
				handler : function() {					
					me.submit();
				}
			});
			if(me.clearButtonEnabled)
			me.tbar.push({
						text : eap_operate_clear,
						iconCls : 'clear',
						tabIndex:901,
						handler : function() {
							me.getForm().reset();
						}
					});
		}else if(me.readOnly && me.otherViewOperaters){
			for(var i=0;i<me.otherViewOperaters.length;i++)
				  me.tbar.push(me.otherViewOperaters[i])
		}
		if(me.closeButtonEnabled)
		me.tbar.push({
					text : eap_operate_close,
					iconCls : 'close',
					tabIndex:902,
					handler : function() {
						me.close();
					}
				});
	    me.on('afterrender',me.afterRenderEventFun);
		me.callParent();
		me.MaskTip = EapMaskTip(document.body);
		me.MaskTip.hide();
	},
	afterRenderEventFun : function(){
	},//提交方法
	submit:function(callback){
		var me = this;
		if (me.getForm().isValid()&&
		   (!me.validate||me.validate(me.getForm().getValues(false)))){
			me.MaskTip.show();
			me.getForm().findField('json').setValue("");
			me.getForm().findField('json').setValue(Ext.encode(me.getForm().getValues(false)));
			me.getForm().submit({
				success : function(form, action) {
					var msg = action.result.msg;
					if (!msg)
						msg = '操作成功！';
				    me.MaskTip.hide();
					Ext.Msg.alert('提示', msg,function(){
						me.close();
						if(me.callback) 
						   me.callback(Ext.encode(me.getForm().getValues(false)),action.result);
						if(callback) callback(Ext.encode(me.getForm().getValues(false)),action.result);
					});
				},
				failure : function(form, action) {
					if (action.result && action.result.length > 0)
						Ext.Msg.alert('错误提示',
								action.result[0].errors);
					else
						Ext.Msg.alert('信息', '后台未响应，网络异常！');
				    me.MaskTip.hide();
				}
			});
		}
	},//关闭
	close : function() {
		var me = this;
		me.ownerCt.close();
		if(me.MaskTip) me.MaskTip.hide();
		delete me.ownerCt;
		delete me;
	},
	//取数据方法
	setFormData : function(id,callback) {
		var me = this;
		var eapMaskTip = EapMaskTip(document.body);
		if (id != undefined && id != '') {
			Ext.Ajax.request({
						method : 'get',
						url : me.findUrl,
						success : function(response) {
							var result = Ext.decode(response.responseText);
							if (Ext.isArray(result)) {
								var msg = result[0].errors;
								Ext.Msg.alert('错误', msg);
								eapMaskTip.hide();
								me.ownerCt.close();
								return;
							} else {
								if(me.jsonParemeterName)
								   me.getForm().setValues(result[me.jsonParemeterName]);
								else
								   me.getForm().setValues(result);
									if(me.groupItems.length>0){
										for(var j=0;j<me.groupItems.length;j++){
											var item = me.groupItems[j];
											var item_Name = me.groupItems[j].items[0].name;
											var ss="";
											for(var i = 0;i<item.items.length;i++){
												if(item.items[i].inputValue == result['form'][item_Name]){
													ss = item.items[i].boxLabel;
													break;
												}
											}
							           	    me.getForm().findField(item_Name).setValue(ss);
											if(item.plugins)
		           	     						delete item.plugins;
										}
									}
								
								
								if(callback)
								  callback(result);
							}
							eapMaskTip.hide();
						},
						failure : function() {
							Ext.Msg.alert('信息', '后台未响应，网络异常！');
							eapMaskTip.hide();
							me.ownerCt.close();
						},
						params : "id=" + id
					});
		} else {
			if(callback) callback();
			eapMaskTip.hide();
		}
	}
}); 