/**
 * 下拉列表控件
 */
Ext.define('base.core.SelectObject', {
	extend : 'Ext.form.ComboBox',
	alias: 'widget.selectobject',
	readOnly:false,//是否只读
	data:[],//数据,无配置的静态数据，则用运程数据
	emptyText:'--全部--',
	mode:'local',
	valueField:'value',//值字段名
	displayField:'text',//显示字段名
	triggerAction:'all',
	editable:false,//不可编辑值
	selectEventFun:Ext.emptyFn,
	//pageSize:100,//分页记录数
	emptyItemName:'--全部--',
	enableSelectOne : true,
	initComponent : function() {
		var me = this;
		me.selectObject = {};
		me.value = me.defaultValue;//缺省值
		me.hiddenName = me.valueField+"_id";
		var fields  = [{name:me.valueField},{name:me.displayField}];
		me.mode = 'remote';
		me.store = new Ext.data.ArrayStore({
			fields : fields,
            proxy : {
			type : 'ajax',
			actionMethods : "GET",
			url : me.selectUrl,// 后台请求地址
			reader : {
				type : 'json',
				root : me.jsonParemeterName?me.jsonParemeterName:me.readerRoot
			}
		}});
		me.store.on("load", function() {
			var record = this.getAt(0);
			if (record) {
				var errorinfo = record.get("errors");
				if (errorinfo != undefined && errorinfo != "")
					Ext.MessageBox.alert("错误提示！", errorinfo);
				else if(me.enableSelectOne&&!me.value)
				    me.select(record.data);
			}
			if(me.allowBlank){
				var emtyData = {};
				emtyData[me.valueField]='';
				emtyData[me.displayField]=me.emptyItemName;
				me.store.insert(0,emtyData);
			}
			if(!me.value)
			me.setValue('');
		});
		me.store.load();
		me.callParent();
		me.on('select',function(combo,record,index){
			me.setValue(combo.value);
			me.selectEventFun(combo,record,index);
		});
	},//刷新数据
    reflash:function(url){
    	var me = this;
    	me.store.proxy.url = url;
    	me.store.load();
    },
	setValue:function(value, doSelect){
		var me = this;
		var ret = base.core.SelectObject.superclass.setValue.call(this,value,doSelect);
		if(Ext.isArray(value))
		    value = value[0].data;
		if(!Ext.isObject(value)){
		   var old = value;
		   value = {};
		   value[me.valueField]=old;
		   me.value = value;
		}
		if(value&&Ext.isObject(value)){
			me.setRawValue(value[me.displayField]);
			me.value = value[me.valueField];
			for(var i=0;i<me.store.getCount();i++){
				var record = me.store.getAt(i);
				if(record.data[me.valueField]==value[me.valueField]){
				   me.setRawValue(record.data[me.displayField]);
				   me.value = value;
				   me.selectObject[me.valueField] = value[me.valueField];
				   me.selectObject[me.displayField] = record.data[me.displayField];
			    }
			}
		}
		if(me.readOnly&&me.bodyEl)
		   me.bodyEl.dom.innerHTML='<div class="x-form-display-field" aria-invalid="false" data-errorqtip="">'+me.getRawValue()+"</div>";
		return ret;
	},//提交数据
	getSubmitData:function(){
		var me = this;
		var data = {};
		data[me.name] = me.selectObject;
        return data;
	},//获取数据
	getValue:function(){
		return this.selectObject;
	}
});