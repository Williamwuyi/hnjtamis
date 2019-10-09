/**
 * 快捷菜单类
 */
ClassDefine('modules.funres.QuickMenu', {
	extend : 'base.model.List',
	enablePaging:false,
	enableCheck:false,
	enableExportXls:false,
	initComponent : function() {
		var me = this;
		// 模块列表对象
		this.columns = [{
					name : 'mrId',
					width : 0
				}, {
					name : 'appMenu.menuName',
					header : '菜单名称',
					width : 200
				}, {
					name : 'openTime',
					header : '最近访问时间',
					type : 'date',
					format:'Y-m-d H:i',
					width : 300
				}, {
					name : 'userSize',
					header : '访问次数',
					width : 400
				}];
		this.keyColumnName = "mrId";// 主健属性名
		this.jsonParemeterName = "quickList";
		this.viewOperater = false;
		this.addOperater = false;
		this.deleteOperater = true;
		this.updateOperater = false;
		this.listUrl = "funres/menu/quickListForMenuListAction!quickList.action";// 列表请求地址
		this.deleteUrl = "funres/menu/deleteQuickForMenuListAction!deleteQuick.action";// 删除请求地址
		var sortConfig = {};
		//列属性配置复制
		sortConfig.columns = new Array(this.columns.length);
		for(var i=0;i<this.columns.length;i++){
		   sortConfig.columns[i] = Ext.clone(this.columns[i]);
		   delete sortConfig.columns[i].xtype;
		}
		//打开排序页面,定义了些方法说明有排序功能
		this.openSortWin = function(record,term,store,callback){
			var me = this;
			sortConfig.keyColumnName = me.keyColumnName;
			sortConfig.sortlistUrl = "funres/menu/quickListForMenuListAction!quickList.action";
			sortConfig.jsonParemeterName = 'quickList';
			sortConfig.saveSortUrl = 'funres/menu/saveQuickSortForMenuFormAction!saveQuickSort.action';
			sortConfig.callback = callback;
			var sortPanel = ClassCreate('base.model.SortList', Ext.clone(sortConfig));
			// 表单窗口
			var sortWin = new WindowObject({
						layout : 'fit',
						title : '快捷菜单排序',
						height : 500,
						width : 700,
						border : false,
						frame : false,
						modal : true,// 模态
						closeAction : 'hide',
						items : [sortPanel]
					});
			sortWin.show();
		}
		this.callParent();
	}
});