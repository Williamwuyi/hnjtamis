/**
 * 手机页面框架
 */
Ext.define('base.Phone', {
    extend: 'Ext.panel.Panel',
    layout : {type : 'table',columns : 1},
    border : false,
    frame : false,
    closable: false,
    closeAction : 'hide',
    resizable: false,
    draggable: false,
    modal : true,// 模态
    plain: false,
    initComponent: function () {
        var me = this; 
        me.items.push('<center><img width="100%" height="354" src="resources/images/APP_01.jpg"/></center>');
        me.items.push('');
    }
});