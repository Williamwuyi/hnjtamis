/**
 * 题型管理的模块类
 */
ClassDefine('modules.hnjtamis.base.themetype.ThemeType', {
	extend : 'base.model.List',
	initComponent : function() {
		var me = this;
		// 模块列表对象
		this.columns = [{
					name : 'themeTypeId',
					width : 0
				}, {
					name : 'themeTypeName',
					header : '题型名称',
					width : 3
				}, {
					name : 'themeType',
					header : '类型',
					width : 2,
					renderer : function(value){
						switch(value){
						case '5' :
							return '单选';
						case '10' :
							return '多选';
						case '15' :
							return '填空';
						case '20' :
							return '问答';
						case '25' :
							return '判断';
						case '30' :
							return '视听';
						case '35' :
							return '其它';
						case '40' :
							return '论述';
						case '45' :
							return '计算';
						case '50' :
							return '画图';
						default:
							return '';
						}
					}
				}, {
					name : 'score',	
					header : '预设分数',
					width : 2
				}, {
					name : 'judge',
					header : '评卷方式',
					width : 2,
					renderer : function(value){
						if(value == 5){
							return '手动评卷';
						}else if(value == 10){
							return '自动评卷';
						}else{
							return '';
						}
					}
				}, {
					name : 'isUse',
					header : '是否有效',
					width : 2,
					renderer : function(value){
						if(value == 5){
							return '否';
						}else if(value == 10){
							return '是';
						}else{
							return '';
						}
					}
				}];
		// 模块查询条件对象
		this.terms = [{
					xtype : 'textfield',
					name : 'titleTerm',
					labelWidth : 80,
					fieldLabel : '题型名称'
				}];
		this.keyColumnName = "themeTypeId";// 主健属性名
		this.viewOperater = true;
		this.addOperater = true;
		this.deleteOperater = true;
		this.updateOperater = true;
		this.otherOperaters = [];//其它扩展按钮操作
		this.otherOperaters.push("");
		this.listUrl = "base/themetype/listForThemeTypeListAction!list.action";// 列表请求地址
		this.deleteUrl = "base/themetype/deleteForThemeTypeListAction!delete.action";// 删除请求地址
		
		var sortConfig = {};
		//列属性配置复制
		sortConfig.columns = new Array(this.columns.length);
		for(var i=0;i<this.columns.length;i++){
		   sortConfig.columns[i] = Ext.clone(this.columns[i]);
		   delete sortConfig.columns[i].xtype;
		}
		//打开排序页面
		this.openSortWin = function(record,term,store,callback){
			var me = this;
			sortConfig.keyColumnName = me.keyColumnName;
			sortConfig.sortlistUrl = me.listUrl+"?"+me.termForm.getValues(true)+"&limit=0";
			sortConfig.saveSortUrl = 'base/themetype/saveSortForThemeTypeFormAction!saveSort.action';
			sortConfig.callback = callback;
			var sortPanel = ClassCreate('base.model.SortList', Ext.clone(sortConfig));
			// 表单窗口
			var sortWin = new WindowObject({
						layout : 'fit',
						title : '题型排序',
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
		
		// 打开表单页面方法
		this.openFormWin = function(id, callback, readOnly,data,term,oper) {
			var formConfig = {};
			var readOnly = readOnly || false;
			formConfig.readOnly = readOnly;
			//formConfig.fileUpload = true;
			formConfig.formUrl = "base/themetype/saveForThemeTypeFormAction!save.action";
			formConfig.findUrl = "base/themetype/findForThemeTypeFormAction!find.action";
			formConfig.callback = callback;
			formConfig.columnSize = 2;
			formConfig.jsonParemeterName = 'form';
			formConfig.clearButtonEnabled = false;
			formConfig.oper = oper;// 复制操作类型变量
			formConfig.items = new Array();
			formConfig.items.push({
						xtype : 'hidden',
						name : 'themeTypeId'
					});
			//=====================================================================
			formConfig.items.push({
						colspan : 2,
						fieldLabel : '题型名称',
						name : 'themeTypeName',
						xtype : 'textfield',
						allowBlank : false,
						readOnly : readOnly,
						size:65,
						maxLength : 32,
						width : 598
					});
			formConfig.items.push({
						colspan : 1,
						fieldLabel : '类型',
						name : 'themeType',
						xtype : 'select',
						data:[['5','单选'],['10','多选'],['15','填空'],['20','问答'],['40','论述'],['25','判断'],['45','计算'],['50','画图'],['30','视听'],['35','其它']],
						readOnly : readOnly
					});
			formConfig.items.push({
						colspan : 1,
						fieldLabel : '预设分数',
						name : 'score',
						xtype : 'numberfield',
						allowBlank : false,
						readOnly : readOnly
					});
			formConfig.items.push({
					colspan : 1,
					xtype : 'fieldcontainer',
					fieldLabel : '评卷方式',
					defaultType:'radiofield',
					defaults:{
						flex:1
					},
					layout:'hbox',
					items:[
					       {
					    	   boxLabel : '手动评卷',
					    	   name : 'judge',
						       inputValue :5,
						       readOnly : readOnly,
						       width:150
						    },
						    {
						    	boxLabel : '自动评卷',
						    	name : 'judge',
							    inputValue :10,
							    checked:true,
							    readOnly : readOnly,
							    width:150
						    }
					       ],
					width:300
				});
			formConfig.items.push({
					colspan : 1,
					xtype : 'fieldcontainer',
					fieldLabel : '是否有效',
					defaultType : 'radiofield',
					defaults:{
						flex:1
					},
					layout:'hbox',
					items : [
					         {
					        	 boxLabel : '是',
					        	 name : 'isUse',
					        	 inputValue : 10,
					        	 checked:true,
					        	 readOnly : readOnly,
					        	  width:150
					         },{
					        	 boxLabel : '否',
					        	 name : 'isUse',
					        	 inputValue : 5,
					        	 readOnly : readOnly,
					        	  width:150
					         }
					         
					         ],
					width : 300
					
				});
			formConfig.items.push({
						colspan : 2,
						fieldLabel : '备注',
						name : 'remark',
						xtype : 'textarea',
						readOnly : readOnly,
						maxLength : 500,
						width : 598,
						height : 70
					});
			var form = ClassCreate('base.model.Form', formConfig);
			form.parentWindow = this;
			// 表单窗口
			var formWin = new WindowObject({
						layout : 'fit',
						title : '题型维护',
						width : 660,
						border : false,
						frame : false,
						modal : true,// 模态
						closeAction : 'hide',
						items : [form]
					});	
			
			form.setFormData(id,function(result){
			});
			formWin.show();
		};
		this.callParent();
	}
});