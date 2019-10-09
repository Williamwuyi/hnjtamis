/**
 * 下拉列表控件
 */
Ext.define('modules.hnjtamis.base.exammarkpeopleinfo.MySelect', {
	extend : 'Ext.form.ComboBox',
	alias: 'widget.myselect',
	readOnly:false,//是否只读
	data:[],//数据,无配置的静态数据，则用运程数据
	emptyText:'请选择',
	mode:'local',
	valueField:'value',//值字段名
	displayField:'text',//显示字段名
	elseField:'',//其它备用字段
	triggerAction:'all',
	editable:false,//不可编辑值
	//pageSize:100,//分页记录数
	selectEventFun:Ext.emptyFn,
	store:{},
	enableSelectOne : true,
	initComponent : function() {
		var me = this;
		me.hiddenName = me.valueField+"_id";
		me.fields  = [{name:me.valueField},{name:me.displayField},{name:me.elseField}];
		me.value = me.value||me.defaultValue;
		if(me.data.length>0){
		    me.store = new Ext.data.ArrayStore({
				fields : me.fields,
				data:me.data
			});
		    me.store.load();
		}else{
			me.mode = 'remote';
			me.store = new Ext.data.ArrayStore({
				fields : me.fields,
				autoLoad : false,
	            proxy : {
					type : 'ajax',
					actionMethods : "GET",
					url : me.selectUrl,// 后台请求地址
					reader : {
						type : 'json',
						root : me.jsonParemeterName
					}
			   }
			});
			me.store.on("load", function() {
					var record = this.getAt(0);
					if (record) {
						var errorinfo = record.get("errors");
						if (errorinfo != undefined && errorinfo != "")
							Ext.MessageBox.alert("错误提示！", errorinfo);
					    else if(!me.value&&me.enableSelectOne)
					        me.select(record.data[me.valueField]);
					}else{
					 	me.select('');
					}
			});
			me.store.load();
	    }
		me.callParent();
		me.on('select',me.selectEventFun);
		me.on('afterrender',function(){
			/*this.keyNav = Ext.create('Ext.util.KeyNav', this.el, {  
                backspace: function(){},//清除回退事件
                scope: this  
            });*/  
			if(me.enableSelectOne&&me.data.length>0) {
			    me.select(me.store.getAt(0).get(me.valueField));
			}
			if(me.defaultValue)//缺省值
			   me.setValue(me.defaultValue);
		});
	},
	setSelectData:function(p){
		var me = this;
		//me.store.loadData(p,false);
		var array = new Array();
		Ext.Array.each(p, function(r) {
			if(Ext.isArray(r))
			   array.push(r);
			else
			   array.push([r[me.valueField],r[me.displayField],r[me.elseField]]);
		});
		me.store = new Ext.data.ArrayStore({
			fields : me.fields,
			data:array
		});
	    me.store.load();
	},//刷新后台数据
    reflash:function(url){
    	var self = this;
    	self.store.proxy.url = url;
    	self.store.load();
    }
});

