/**
 * 表格的分页记录数下拉插件
 */
Ext.define('base.core.PageSizePlugin',{
	extend: 'Ext.form.ComboBox',
	store: new Ext.data.SimpleStore({
        fields: ['text', 'value'],
        data: [['10', 10], ['20', 20], ['50', 50], ['100', 100],['200', 200],['500', 500]]
    }),
    mode: 'local',
    displayField: 'text',
    valueField: 'value',
    editable: false,
    allowBlank: false,
    triggerAction: 'all',
    width: 60,
    //插件的初始方法
	init: function(paging) {
        paging.on('render', this.onInitView, this);
    },
    onInitView: function(paging) {
        paging.insert(9,[',每页',this,'条']);
        this.setValue(paging.pageSize);
        this.on('select', this.onPageSizeChanged, paging);
        if(paging.ownerCt.enableExportXls)
        paging.insert(14,Ext.create("Ext.Button", {
							iconCls : 'exportXls',
							text : '',
							tooltip : '导出EXCEL',
							handler : function() {
								paging.ownerCt.exportXls();
							}
						}));
    },
    onPageSizeChanged: function(combo) {
        this.pageSize = parseInt(combo.getValue());
		this.store.pageSize = this.pageSize;
		this.moveFirst();
    }
});