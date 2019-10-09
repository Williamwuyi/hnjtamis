/**
 * 动态列表控件
 */
Ext.define('base.core.AutoList', {
	extend : 'Ext.grid.GridPanel',
	alias : 'widget.autolist',
	loadMask : true,
	viewConfig : {
		autoScroll : true,
		height : 200
	},
	rendererFun: Ext.emptyFn,
	initComponent : function() {
		var me = this;
		var fields = new Array();
		Ext.Ajax.request({
			method : 'GET',
			url : me.findUrl,
			success : function(response) {
				var result = Ext.decode(response.responseText);
				var msg = result[0].errors;
				if(msg)
				    Ext.Msg.alert('错误', msg);
				else{
					me.initPage(result);
				}
				me.callParent();
			},
			failure : function() {
				Ext.Msg.alert('信息', '后台未响应，网络异常！');
			}
		});
	},
	initPage : function(result) {
		var me = this;
		var fields = [];
		for(var c in result[0]){
			fields.push({name:c});
		}
		me.store = new Ext.data.Store({
			fields : fields
		});
		me.forceFit = true;// 自动填充列宽,根据宽度比例
		// 控制可改变列宽
		me.enableColumnMove = true;
		me.enableColumnResize = true;
		me.stripeRows = true;// 班马线效果
		var columns = new Array();
		columns[0] = new Ext.grid.RowNumberer();
		for (var c in result[0]) {
			var f = {};
			f.dataIndex = c;
			f.renderer = me.rendererFun;
			f.header = result[0][c];
			columns[columns.length] = f;
		}
		me.columns = columns;
		for(var i=1;i<result.length;i++){
			me.store.insert(i-1,result[i]);
		}
	}
});