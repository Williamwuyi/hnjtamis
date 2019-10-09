/**
 * 编辑列表控件
 */
Ext.define('base.core.EditList', {
	extend : 'Ext.grid.GridPanel',
	mixins : {
		field : 'Ext.form.field.Field'
	},
	alias : 'widget.editlist',
	loadMask : true,
	fieldLabel : '编辑列表',// 标题
	readOnly : false,// 是否只读
	allowMaxSize : -1,// 允许最大行数,-1为不受限制
	viewConfig:{
		autoScroll:true
		//height:200
	},
	forceFit : true,// 自动填充列宽,根据宽度比例
	enableMoveButton:true,
	keyColumnName:'',//列表中唯一标示的属性名
	enableCheck:true,
	initComponent : function() {
		var me = this;
		if(!me.readOnly)
	        me.plugins = [Ext.create('Ext.grid.plugin.CellEditing', {
				clicksToEdit : 1
			})];
		var fields = new Array();
		for (var i = 0; i < me.columns.length; i++) {
			var f = {
				name : me.columns[i].name,
				type : me.columns[i].type
			};
			if (me.columns[i].type == 'date')
				f.dateFormat = 'Y-m-dTH:i:s';
			fields[fields.length] = f;
		}
		me.store = new Ext.data.Store({
					fields : fields
				});
		// 控制可改变列宽
		me.enableColumnMove = true;
		me.enableColumnResize = true;
		me.stripeRows = true;// 班马线效果
		var columns = new Array();
		for (var i = 0; i < me.columns.length; i++) {
			if (i == 0){
				columns[0] = new Ext.grid.RowNumberer();
				columns[0].width=me.numberColumnWidth||grabl_numberColumnWidth||60;
				columns[0].titleAlign="center";
				columns[0].text = '序号';
			}
			var f = me.columns[i];
			f.dataIndex = f.name;
			if(f.editor) f.allowBlank = f.editor.allowBlank;
			var dateFormat = me.dateFormat || 'Y-m-d';
			if (me.columns[i].type == 'date')
				f.renderer = Ext.util.Format.dateRenderer(dateFormat);
		    if(!f.titleAlign)
			   f.titleAlign="center";
			if (f.width != 0)
				columns[columns.length] = f;
		}
		me.columns = columns;
		if (me.enableCheck&&!me.readOnly)
			me.selModel = new Ext.selection.CheckboxModel({
						checkOnly : false
			});
		me.tbar = [me.fieldLabel,'->'];
		/*if(me.otherOperaters && !me.readOnly){
		   for(var i=0;i<me.otherOperaters.length;i++){
		   		me.tbar.push(me.otherOperaters[i]);
		   }
	    }*/
		if(me.readOnly){
			me.otherOperaters = [];
		}else if(me.otherOperaters){
		  	for(var i=0;i<me.otherOperaters.length;i++){
					if(me.otherOperaters[i].sortIndex == undefined
					 || me.otherOperaters[i].sortIndex == null){
					 	if(i == 0){
					 		me.otherOperaters[i].sortIndex = 0;
					 	}else{
					 		me.otherOperaters[i].sortIndex = me.otherOperaters[i-1].sortIndex+1;
					 	}
					}
			}
		}else{
			me.otherOperaters = [];
		}
		if (me.addOperater && !me.readOnly)
			me.otherOperaters.push(Ext.create("Ext.Button", {
						iconCls : 'add',
						text : eap_operate_addrow,
						sortIndex : 100,
						handler : function() {
							me.addRecord();
						}
					}));
		if (me.deleteOperater && !me.readOnly)
			me.otherOperaters.push(Ext.create("Ext.Button", {
						iconCls : 'remove',
						text : eap_operate_delete,
						sortIndex : 110,
						handler : function() {
							var selected = me.getSelectionModel().selected;
							if (selected.getCount() == 0) {
								Ext.Msg.alert('提示', '请选择记录！');
								return;
							}
							for (var i=0;i<selected.length;i++) {
								me.store.remove(selected.getAt(i--));
							}
						}
					}));
	    if(!me.readOnly&&me.enableMoveButton){
			me.otherOperaters.push(Ext.create("Ext.Button", {
				iconCls : 'moveUp',
				text : eap_operate_moveup,
				sortIndex : 120,
				handler : function() {
					me.moveToUpper();
				}
			}));
			me.otherOperaters.push(Ext.create("Ext.Button", {
				iconCls : 'moveDown',
				text : eap_operate_movedown,
				sortIndex : 130,
				handler : function() {
					me.moveToDown();
				}
			}));
	    }
	    if( me.otherOperaters &&  me.otherOperaters.length>0){
		    me.otherOperaters.sort(function(a,b){
			   			if(a.sortIndex == undefined && b.sortIndex == undefined){
			   				return 0;
			   			}else if(a.sortIndex == undefined){
			   				return 1;
			   			}else if(b.sortIndex == undefined){
			   				return -1;
			   			}else{
			   				return Number(a.sortIndex)-Number(b.sortIndex);
			   			}
		            	
		    });
			for(var i=0;i<me.otherOperaters.length;i++){
				me.tbar.push(me.otherOperaters[i]);
			}
	    }
		me.callParent();
		me.setGridColumnHeaderAlign();
	},//头对齐
	addRecord : function(){
		var me = this;
		var rowIndex = me.store.getCount();
		if (rowIndex >= me.allowMaxSize && me.allowMaxSize > 0) {
			Ext.Msg.alert('提示','超过最大记录数'+ me.allowMaxSize+ '！');
			return;
		}
		var row = {};
		for (var i = 0; i < me.columns.length; i++) {
			row[me.columns[i].name] = me.columns[i].defaultValue;
		}
		var insertIndex = me.store.indexOf(me.getSelectionModel().getLastSelected())+1;
		if(insertIndex==0)
		   insertIndex = rowIndex;
		me.store.insert(insertIndex, row);
		me.getSelectionModel().select(insertIndex);
	},
	setGridColumnHeaderAlign : function(){
				if(Ext.grid.Column){
		        	Ext.grid.Column.override({
		        		afterRender: function() {
		        	        var me = this,
		        	            triggerEl = me.triggerEl,
		        	            triggerWidth;

		        	        me.callParent(arguments);
		        	        if (!Ext.isIE8 || !Ext.isStrict) {
		        	            me.mon(me.getFocusEl(), {
		        	                focus: me.onTitleMouseOver,
		        	                blur: me.onTitleMouseOut,
		        	                scope: me
		        	            });
		        	        }
		        	        if (triggerEl && me.self.triggerElWidth === undefined) {
		        	            triggerEl.setStyle('display', 'block');
		        	            me.self.triggerElWidth = triggerEl.getWidth();
		        	            triggerEl.setStyle('display', '');
		        	        }
		        	        me.keyNav = new Ext.util.KeyNav(me.el, {
		        	            enter: me.onEnterKey,
		        	            down: me.onDownKey,
		        	            scope: me
		        	        });
		        	        me.el.addCls(Ext.baseCSSPrefix + 'column-header-align-' + me.align).addClsOnOver(me.overCls);
		        	        me.titleAlign = me.titleAlign || me.align;
		        	        me.el.addCls(Ext.baseCSSPrefix + 'column-header-align-' + me.titleAlign).addClsOnOver(me.overCls);
		        	    }
		        	});
		        }
			},
	setValue : function(value) {
		var me = this;
		if(value){			
			var index = 0;
			me.store.removeAll();
			for (var i=0;i<value.length;i++){
				if(value[i]){
				  me.store.insert(index, value[i]);
				  index++;
				}
			}
		}
	},// 获取控件数据
	getSubmitData : function() {
		var me = this;
		var datas = new Array();
		for (var i = 0; i < me.store.getCount(); i++) {
			var record = me.store.getAt(i);
			datas[datas.length] = record.data;
		}
		var data = {};
		data[me.getName()] = datas;
		return data;
	},
	validate:function(value){//数据验证
		var me = this;
		for (var i = 0; i < me.store.getCount(); i++) {
			var record = me.store.getAt(i);
			for(var j = 0;j<me.columns.length;j++){
				if(me.columns[j].editor){
					var allowBlank = me.columns[j].editor.allowBlank;
					if(!allowBlank)
					    allowBlank=me.columns[j].allowBlank;
					if(allowBlank==false&&record.data[me.columns[j].name]==''){
					   Ext.Msg.alert('提示', (me.columns[j].text)+'必填！');
					   return false;
					}
				}else if(me.columns[j] && me.columns[j].name != undefined
					&& me.columns[j].allowBlank==false &&record.data[me.columns[j].name]==''){
						Ext.Msg.alert('提示', (me.columns[j].text)+'必填！');
					   return false;
				}
			}
		}
		return true;
	},moveToUpper : function() {
		var me = this;
		var selected = me.getSelectionModel().selected;
		if(selected.getCount()==0){
			Ext.MessageBox.alert("提示！", "请选择要移动的行！");
			return;
		}
		for (var i = 0; i < selected.getCount(); i++) {
			var record = selected.get(i);
			var index = me.store.indexOf(record);
			if(index==0){
				Ext.MessageBox.alert("提示！", "已经是最高行！");
				return;
			}
			me.store.remove(record);
            me.store.insert(index-1, record);
            //me.getSelectionModel().selectRow(index-1);
            me.getSelectionModel().select(index-1);
		}
	},moveToDown : function() {
		var me = this;
		var selected = me.getSelectionModel().selected;
        var rowSize = me.store.getCount();
		if(selected.getCount()==0){
			Ext.MessageBox.alert("提示！", "请选择要移动的行！");
			return;
		}
		for (var i = 0; i < selected.getCount(); i++) {
			var record = selected.get(i);
			var index = me.store.indexOf(record);
			if(index==(rowSize-1)){
				Ext.MessageBox.alert("提示！", "已经是最底行！");
				return;
			}
			me.store.remove(record);
            me.store.insert(index+1, record);
            me.getSelectionModel().select(index+1);
		}
	},// 获取选择ID
	getSelectIds : function() {
		var me = this;
		if(!me.keyColumnName){
			Ext.MessageBox.alert("提示！", "未定义列表的keyColumnName！");
			return;
		}
		var selected = me.getSelectionModel().selected;
		var ids = "";
		for (var i = 0; i < selected.getCount(); i++) {
			var record = selected.get(i);
			if (i != 0)
				ids += "," + record.get(me.keyColumnName);
			else
				ids += record.get(me.keyColumnName);
		}
		return ids;
	},//获得所有数据
	getAll:function(){
		var me = this;
		var data = new Array();
		for (var i = 0; i < me.store.getCount(); i++) {
			var record = me.store.getAt(i);
			data.push(record.data);
		}
		return data;
	},//获得选择的数据
	getSelect:function(){
		var me = this;
		var data = new Array();
		var selected = me.getSelectionModel().selected;
		for (var i = 0; i < selected.getCount(); i++) {
			var record = selected.get(i);
			data.push(record);
		}
		return data;
	},//获取选择的行号,返回数组
	getSelectRowIndexs:function(){
		var me = this;
		var data = new Array();
		var selected = me.getSelectionModel().selected;
		for (var i = 0; i < selected.getCount(); i++) {
			var record = selected.get(i);
			var index = me.store.indexOf(record);
			data.push(index);
		}
		return data;
	},//设置某行指定数据的值
	setFieldData:function(rowIndex,fieldName,fieldValue){
		for (var i = 0; i < me.store.getCount(); i++) {
			var record = me.store.getAt(i);
			if(i==rowIndex){
				record.data[fieldName]=fieldValue;
			}
		}
	},//获取某行指定数据的值
	getFieldData:function(rowIndex,fieldName){
		for (var i = 0; i < me.store.getCount(); i++) {
			var record = me.store.getAt(i);
			if(i==rowIndex){
				return record.data[fieldName];
			}
		}
		return null;
	},//增加或修改数据
	addUpdateData:function(data){
		var me = this;
		if(!me.keyColumnName){
			Ext.MessageBox.alert("提示！", "未定义列表的keyColumnName！");
			return;
		}
		if(!Ext.isArray(data)){
			Ext.MessageBox.alert("提示！", "参数data必须是数组类型！");
			return;
		}
		for(var j=0;j<data.length;j++){
			var have = false;//是否找到
		    for (var i = 0; i < me.store.getCount(); i++) {
			    var record = me.store.getAt(i);
			    if(record.data[me.keyColumnName]==data[j][me.keyColumnName]){
			    	have = true;
			    	record.data = data[j];
			    }
			}
			if(!have){
				insert(data[j]);
			}
		}
		me.getView().refresh();
	},//删除行方法
	deleteById:function(id){
		var me = this;
		if(!me.keyColumnName){
			Ext.MessageBox.alert("提示！", "未定义列表的keyColumnName！");
			return;
		}
		for (var i = 0; i < me.store.getCount(); i++) {
		    var record = me.store.getAt(i);
		    if(record.data[me.keyColumnName]==id){
		    	me.store.remove(record);
		    }
		}
		me.getView().refresh();
	}
});