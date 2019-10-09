

ClassDefine('modules.organization.Example', {
	extend : 'base.model.List',
	initComponent : function() {
		var me = this;
		me.op = me.op || 'organ';
		// 模块列表对象
		this.columns = [{
					name : 'employeeId',
					width : 0
				}, {
					name : 'employeeName',
					header : '姓名',
					width : 2
				},{
					name : 'dept.deptName',
					header : '所在部门',
					width : 3
				},{
					name : 'quarterTrainName',
					header : '培训岗位',
					width : 2
				}];
		// 打开表单页面方法
		this.terms = [];
		this.keyColumnName = "employeeId";// 主健属性名
		this.listUrl = "";
		this.otherOperaters = [];// 其它扩展按钮操作
		this.otherOperaters.push(Ext.create("Ext.Button",
				{
					icon : 'resources/icons/fam/user_edit.png',
					text : '选择',
					handler : function() {
						var selected = me.getSelectionModel().selected;
						var record = [];
						for (var i = 0; i < selected.getCount(); i++) {
							record[record.length] = selected.get(i);
						}
						me.openSelectWin(record);
					}
	   }));
		me.callParent();
	},
	openSelectWin : function(selectRows) {
		var me = this;
		var formConfig = {};
		var readOnly = false;
		formConfig.readOnly = readOnly;
		formConfig.backFun = function(myform,addlist){
			for(var i=0;i<addlist.length;i++){
				me.store.insert(me.store.getCount(),addlist[i]);
			}
			myform.close();
		};
		formConfig.selectRows = selectRows;
		me.form = ClassCreate('modules.organization.Forms',formConfig);
		// 表单窗口
		var formWin = new WindowObject({
			layout : 'fit',
			title : '维护操作',
			autoHeight : false,
			width : 930,
			height:630,
			border : false,
			frame : false,
			modal : true,// 模态
			closeAction : 'destroy',//'hide',
			draggable:true,//拖动
			resizable:false, //变大小 
			items : [ me.form ]
		});
		formWin.show();
	},
	close:function(){
		var me = this;
		me.formWin.close();
	}
});