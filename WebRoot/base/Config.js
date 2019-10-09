/**
 * 属性读取类
 */
Ext.define('base.Config',{
 	singleton: true,
	initComponent : function(){
	    var me = this;
    	me.callParent(arguments);
	},
    getQueryParam : function(name) {
        var regex = RegExp('[?&]' + name + '=([^&]*)');

        var match = regex.exec(location.search);
        return match && decodeURIComponent(match[1]);
    },
	getProperty:function(key){
		var ret = "";
		Ext.Ajax.request({
				method : 'get',
				async : false,
				url : "baseinfo/affiche/readConfigForAfficheFormAction!readConfig.action?key="+key,
				success : function(response) {
					var result = Ext.decode(response.responseText);
					ret = result.content;
				},
				failure : function() {
				}
			});
	    return ret;
	},
	writeConfig:function(key,value){
		var para = {key:key,value:value};
		Ext.Ajax.request({
				method : 'post',
				async : false,
				url : "baseinfo/affiche/writeConfigForAfficheFormAction!writeConfig.action",
				params:para
			});
	}
});