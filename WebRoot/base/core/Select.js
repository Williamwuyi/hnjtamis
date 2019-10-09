/**
 * 下拉列表控件
 */
Ext.define('base.core.Select', {
	extend : 'Ext.form.ComboBox',
	alias: 'widget.select',
	readOnly:false,//是否只读
	data:[],//数据,无配置的静态数据，则用运程数据
	emptyText:'--全部--',
	mode:'local',
	valueField:'value',//值字段名
	displayField:'text',//显示字段名
	triggerAction:'all',
	editable:false,//不可编辑值
	//pageSize:100,//分页记录数
	emptyItemName:'--全部--',
	selectEventFun:Ext.emptyFn,
	store:{},
	enableSelectOne : true,
	initComponent : function() {
		var me = this;
		me.hiddenName = me.valueField+"_id";
		me.fields  = [{name:me.valueField},{name:me.displayField}];
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
			me.store.on("load", function(store, records, options) {
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
					if(me.allowBlank){
						var empty = {};
						empty[me.valueField]='';
						empty[me.displayField]=me.emptyItemName; 
						store.insert(0,empty);
					}
					if(!me.value)
					   me.setValue('');
					if(me.defaultValue!=undefined)//缺省值
			  			 me.setValue(me.defaultValue);
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
	setValue:function(value){
		this.callParent(arguments);
		if(this.readOnly&&this.bodyEl)
		   this.bodyEl.dom.innerHTML='<div class="x-form-display-field" aria-invalid="false" data-errorqtip="">'+this.rawValue+"</div>";
		return this;
	},
	setSelectData:function(p){
		var me = this;
		//me.store.loadData(p,false);
		var array = new Array();
		Ext.Array.each(p, function(r) {
			if(Ext.isArray(r))
			   array.push(r);
			else
			   array.push([r[me.valueField],r[me.displayField]]);
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
Ext.override(Ext.form.RadioGroup,{     
	//针对form中的基本组件 　　     
	initComponent:function(){
		var me = this;
		if(me.readOnly){
			for(var i=0;i<me.items.length;i++){
				delete me.items[i].checked;
			}
		}
		this.callParent(arguments);  
		me.on("change", function(store, records, options) {
			if(me.readOnly&&me.bodyEl){
			    var displayValue="",boxes  = this.getBoxes();
		        for (var b = 0; b < boxes.length; b++) {
		            var box  = boxes[b];
		            var v = me.getValue();
		            if(v[box.name]==box.inputValue){
		               displayValue=box.boxLabel;
		            }
		        }
			   me.bodyEl.dom.innerHTML='<div class="x-form-display-field" aria-invalid="false" data-errorqtip="">'+displayValue+"</div>";
			}
		});
	} 
});
Ext.define('base.core.RadioGroup', {
	extend : 'Ext.form.RadioGroup',
	alias: 'widget.radiogroupex',
	selectUrl:'',//请求地址
	//name:'',//编辑元素名称
	valueField:'value',//值字段名
	displayField:'text',//显示字段
	selectEventFun:Ext.emptyFn,
	decollator:'_',//分割符 
	editorType:'str',//str只需要获取ID，title表示还包括显示值，与ID用decollator分隔
	initComponent:function(){
		var me = this;
		me.defaultType = 'radio';
		me.items=[];
		/**
		 * 以下为标准控件的示例
		 * {
				colspan:2,
				fieldLabel:"允许反馈",
				xtype:'radiogroup',
				allowBlank: false,
				width:480,
				labelWidth : 130,
				columns:2,
				readOnly : readOnly,
				items:[{boxLabel:'允许',name:'mytest',inputValue:'10',readOnly : readOnly},
					{boxLabel:'不允许',name:'mytest',inputValue:'20',readOnly : readOnly}],
				hidden : false
			}
			提到后台字段对应的是 items中的name:mytest 
		 * 
		 ***/
		var itemName = me.name;//RadioGroup使用的name存在于data里面，最后RadioGroup用的总name加s
		if(me.data){
			Ext.Array.each(me.data,function(s){
				var v = {};
				v['boxLabel']=s[1];
				v['inputValue']=s[0];
				v['name']=itemName;
				v['readOnly']=me.readOnly;
				if(!me.readOnly)
				v['checked']=(s[0]==me.defaultValue);
				me.items.push(v);
			});
		}else{
			Ext.Ajax.request({
				method : 'get',
				url : me.selectUrl,
				async : false,//是否异步
				success : function(response) {
					var result = Ext.decode(response.responseText);
					if(me.jsonParemeterName)
					   result = result[me.jsonParemeterName];
					Ext.Array.each(result,function(s){
						var v = {};
						v['boxLabel']=s[me.displayField];
						v['inputValue']=s[me.valueField];
						v['name']=itemName;
						v['readOnly']=me.readOnly;
						if(!me.readOnly)
						v['checked']=(s[me.valueField]==me.defaultValue);
						me.items.push(v);
					});
				},
				failure : function() {
					Ext.Msg.alert('信息', '后台未响应，网络异常！');
				}
			});	
		}
		me.name = me.name+'s';
		this.callParent();
		this.on('change',this.selectEventFun);
		me.on("change", function(store, records, options) {
			if(me.readOnly&&me.bodyEl){
			    var displayValue="",boxes  = this.getBoxes();
		        for (var b = 0; b < boxes.length; b++) {
		            var box  = boxes[b];
		            var v = me.getValue();
		            if(v[box.name]==box.inputValue){
		               displayValue=box.boxLabel;
		            }
		        }
			    me.bodyEl.dom.innerHTML='<div class="x-form-display-field" aria-invalid="false" data-errorqtip="">'+displayValue+"</div>";
			}
		});
		/*me.on("afterrender", function(store, records, options) {
			if(!me.value)
			   me.setValue('');
	    });*/
	}/*,//提交数据
	getValue: function() {
        var values = "",
            boxes  = this.getBoxes();
        for (var b = 0; b < boxes.length; b++) {
            var box  = boxes[b];
            if(!box.checked) continue;
            if(this.editorType=='str')
               values = box.inputValue;
            else
               values = box.inputValue+this.decollator+box.boxLabel;
        }
        return values;
    },//设置数据
    setValue: function(value) {
    	var me = this;
        var values = [],
            boxes  = this.getBoxes();
        var displayValue="";
        //me.batchChanges(function() {
        if(value)
        for (var b = 0; b < boxes.length; b++) {
            var box  = boxes[b];
            if(value==box.inputValue){
               box.setValue(true);
               displayValue=box.boxLabel;
            }else
               box.setValue(false);
        }
        //});
    	//me.callParent(arguments);
        if(me.readOnly&&me.bodyEl)
		   me.bodyEl.dom.innerHTML='<div class="x-form-display-field" aria-invalid="false" data-errorqtip="">'+displayValue+"</div>";
        return me;
    },//提交数据
	getSubmitData:function(){
		var self = this;
		var data = {};
		data[self.getName()] = self.getValue();
		return data;
	}*/
});
Ext.define('base.core.CheckBoxGroup', {
	extend : 'Ext.form.CheckboxGroup',
	alias: 'widget.checkboxgroupex',
	selectUrl:'',//请求地址
	name:'',//编辑元素名称
	valueField:'value',//值字段名
	displayField:'text',//显示字段
	selectEventFun:Ext.emptyFn,
	decollator:'_',//分割符 
	editorType:'str',//str只需要获取ID，title表示还包括显示值，与ID用decollator分隔
	initComponent:function(){
		var me = this;
		me.items=[];
		if(me.data){
			Ext.Array.each(me.data,function(s){
				var v = {};
				v['boxLabel']=s[1];
				v['inputValue']=s[0];
				v['readOnly']=me.readOnly;
				if(!Ext.isArray(me.defaultValue))
				   v['checked']=(s[0]==me.defaultValue);
				else{
					Ext.Array.each(me.defaultValue,function(dv){
						if(dv==s[0]) v['checked'] = true;
					});
				}
				me.items.push(v);
			});
		}else
		Ext.Ajax.request({
			method : 'get',
			url : me.selectUrl,
			async : false,//是否异步
			success : function(response) {
				var result = Ext.decode(response.responseText);
				if(me.jsonParemeterName)
				   result = result[me.jsonParemeterName];
				Ext.Array.each(result,function(s){
					var v = {};
					v['boxLabel']=s[me.displayField];
					v['inputValue']=s[me.valueField];
					//v['name']=me.name;
					v['readOnly']=me.readOnly;
					if(!Ext.isArray(me.defaultValue))
					   v['checked']=(s[me.valueField]==me.defaultValue);
					else{
						Ext.Array.each(me.defaultValue,function(dv){
							if(dv==s[me.valueField])
							   v['checked'] = true;
						});
					}
					me.items.push(v);
				});
			},
			failure : function() {
				Ext.Msg.alert('信息', '后台未响应，网络异常！');
			}
		});		
		this.callParent();
		this.on('change',this.selectEventFun);
		/*me.on("afterrender", function(store, records, options) {
			if(!me.value)
			   me.setValue('');
	    });*/
	},//提交数据
	getValue: function() {
        var values = [],
            boxes  = this.getBoxes();
        for (var b = 0; b < boxes.length; b++) {
            var box  = boxes[b];
            if(!box.checked) continue;
            if(this.editorType=='str')
               values.push(box.inputValue)
            else
               values.push(box.inputValue+this.decollator+box.boxLabel);
        }
        return values;
    },//设置数据
    setValue: function(value) {
    	var me = this;
    	if(!Ext.isArray(value)){
    		value=[value];
    	}
        var values = [],
            boxes  = this.getBoxes();
        var displayValue="";
        if(value)
        for (var b = 0; b < boxes.length; b++) {
            var box  = boxes[b];
            var checked = false;
            for(var a=0;a<value.length;a++){
            	var v = (""+value[a]).split(me.decollator)[0];
            	if(v==box.inputValue){
            	   checked = true;
            	   if(displayValue=="")
            	      displayValue=box.boxLabel;
            	   else
            	      displayValue+=','+box.boxLabel;
            	}
            }
            box.setValue(checked);
        }
        if(me.readOnly&&me.bodyEl)
		   me.bodyEl.dom.innerHTML='<div class="x-form-display-field" aria-invalid="false" data-errorqtip="">'+displayValue+"</div>";
        return me;
    },//提交数据
	getSubmitData:function(){
		var self = this;
		var data = {};
		data[self.getName()] = self.getValue();
		return data;
	}
});