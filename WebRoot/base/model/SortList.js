/**
 * 标准模板的排序列表页面类
 */
Ext.define('base.model.SortList', {
	extend : 'Ext.grid.GridPanel',
	numberColumnWidth:30,
	showFlag : true,
	initComponent : function() {
		var me = this;
		me.region = 'center';// 对齐属性
		me.margins = '2 2 2 2'; // 为了不要与容器的边框重叠，设定2px的间距
		me.loadMask = true;// 开启提示数据下载
		me.fields = [];
		for (var i = 0; i < me.columns.length; i++) {
			var f = {
				name : me.columns[i].name,
				type : me.columns[i].type
			};
			if (me.columns[i].type == 'date') {
				f.dateFormat = 'Y-m-d H:i:s';
			}
			me.fields[me.fields.length] = f;
		}
		me.fields[me.fields.length] = 'errors';
		me.store = new Ext.data.Store({
					autoLoad : false,
					fields : me.fields,
					proxy : {
						type : 'ajax',
						actionMethods : "POST",
						url : me.sortlistUrl,// 后台请求地址
						reader : {
							type : 'json',
							root : me.jsonParemeterName || 'list',
							totalProperty : "total"
						}
					},
					remoteSort : true
				});
		me.store.on("load", function() {
				var record = this.getAt(0);
				if (record) {
					var errorinfo = record.get("errors");
					if (errorinfo != undefined && errorinfo != "")
						Ext.MessageBox.alert("错误提示！", errorinfo);
				}
				if(me.showFlag && me.showWin){
					 me.showWin();
					 me.showFlag = false;
				}
			});
		me.forceFit = true;// 自动填充列宽,根据宽度比例
		// 控制可改变列宽
		//me.enableColumnMove = false;
		//me.enableColumnResize = false;
		me.stripeRows = true;// 班马线效果
		var columns = new Array();
		for (var i = 0; i < me.columns.length; i++) {
			if (i == 0){
				columns[0] = new Ext.grid.RowNumberer();
				columns[0].width = me.numberColumnWidth;
			}
			var f = me.columns[i];
			f.dataIndex = f.name;
			delete f.name;
			if (me.columns[i].type == 'date') {
				if (!f.format)
					f.format = 'Y-m-d H:i:s';
				f.renderer = Ext.util.Format.dateRenderer(f.format);
			}
			if(!f.sortable)
			   f.sortable = false;
			if(!f.titleAlign)
			   f.titleAlign="center";
			if (f.width != 0)
			   columns[columns.length] = f;
		}
		me.columns = columns;
		me.tbar = ['->', Ext.create("Ext.Button", {
							iconCls : 'moveTop',
							text : eap_operate_movetop,
							handler : function() {
								me.moveToTop();
							}
						}), Ext.create("Ext.Button", {
							iconCls : 'moveUp',
							text : eap_operate_moveup,
							handler : function() {
								me.moveToUpper();
							}
						}), Ext.create("Ext.Button", {
							iconCls : 'moveDown',
							text : eap_operate_movedown,
							handler : function() {
								me.moveToDown();
							}
						}), Ext.create("Ext.Button", {
							iconCls : 'moveButtom',
							text : eap_operate_movebuttom,
							handler : function() {
								me.moveToBottom();
							}
						}),Ext.create("Ext.Button", {
							iconCls : 'moveTo',
							text : eap_operate_moveto,
							tooltip:'首先选择要移动的行，然后点击此按钮，最后再点击要移动的目的行，即完成操作！',
							handler : function() {
								me.moveTo();
							}
						}), Ext.create("Ext.Button", {
							iconCls : 'save',
							text : eap_operate_saveorder,
							handler : function() {
								me.saveSort();
							}
						}), Ext.create("Ext.Button", {
							iconCls : 'close',
							text : eap_operate_close,
							handler : function() {
								me.ownerCt.close();
							}
						})];
		me.callParent();
		me.store.load({callback: function(records, operation, success) {
		        //alert(records.length+"："+success);
		    }}
		);
		me.setGridColumnHeaderAlign();
	},// 头对齐
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
	moveToTop : function() {
		var me = this;
		var selected = me.getSelectionModel().selected;
		if (selected.getCount() == 0) {
			Ext.MessageBox.alert("提示！", "请选择要移动的行！");
			return;
		}
		for (var i = 0; i < selected.getCount(); i++) {
			var record = selected.get(i);
			var index = me.store.indexOf(record);
			if (index == 0) {
				Ext.MessageBox.alert("提示！", "已经是最高行数！");
				return;
			}
			me.store.remove(record);
			me.store.insert(0, record);
			me.getSelectionModel().select(0);
		}
	},// 向上移一行
	moveToUpper : function() {
		var me = this;
		var selected = me.getSelectionModel().selected;
		if (selected.getCount() == 0) {
			Ext.MessageBox.alert("提示！", "请选择要移动的行！");
			return;
		}
		for (var i = 0; i < selected.getCount(); i++) {
			var record = selected.get(i);
			var index = me.store.indexOf(record);
			if (index == 0) {
				Ext.MessageBox.alert("提示！", "已经是最高行！");
				return;
			}
			me.store.remove(record);
			me.store.insert(index - 1, record);
			me.getSelectionModel().select(index-1);
		}
	},// 向下移一行
	moveToDown : function() {
		var me = this;
		var selected = me.getSelectionModel().selected;
		var rowSize = me.store.getCount();
		if (selected.getCount() == 0) {
			Ext.MessageBox.alert("提示！", "请选择要移动的行！");
			return;
		}
		for (var i = 0; i < selected.getCount(); i++) {
			var record = selected.get(i);
			var index = me.store.indexOf(record);
			if (index == (rowSize - 1)) {
				Ext.MessageBox.alert("提示！", "已经是最底行！");
				return;
			}
			me.store.remove(record);
			me.store.insert(index + 1, record);
			me.getSelectionModel().select(index+1);
		}
	},// 移到底端
	moveToBottom : function() {
		var me = this;
		var selected = me.getSelectionModel().selected;
		var rowSize = me.store.getCount();
		if (selected.getCount() == 0) {
			Ext.MessageBox.alert("提示！", "请选择要移动的行！");
			return;
		}
		for (var i = 0; i < selected.getCount(); i++) {
			var record = selected.get(i);
			var index = me.store.indexOf(record);
			if (index == (rowSize - 1)) {
				Ext.MessageBox.alert("提示！", "已经是最底行！");
				return;
			}
			me.store.remove(record);
			me.store.insert(rowSize - 1, record);
			me.getSelectionModel().select(rowSize-1);
		}
	},//移至
	moveTo : function(){
		var me = this;
		var selected = me.getSelectionModel().selected;
		var rowSize = me.store.getCount();
		if (selected.getCount() == 0) {
			Ext.MessageBox.alert("提示！", "请选择要移动的行！");
			return;
		}
		me.eapSelectRecord = selected.get(0);
	},// 保存排序
	saveSort : function() {
		var me = this;
		var ids = new Array();
		for (var i = 0; i < me.store.getCount(); i++) {
			var record = me.store.getAt(i);
			ids.push(record.data[me.keyColumnName]);
		}
		var maskTip = EapMaskTip(document.body);
		Ext.Ajax.request({
					method : 'POST',
					url : me.saveSortUrl,
					success : function(response) {
						var result = Ext.decode(response.responseText);
						if (Ext.isArray(result)) {
							var msg = result[0].errors;
							Ext.Msg.alert('错误', msg);
						} else {
							Ext.Msg.alert('提示', '保存成功！');
							me.callback();
							me.ownerCt.close();
						}
						maskTip.hide();
					},
					failure : function() {
						Ext.Msg.alert('信息', '后台未响应，网络异常！');
						maskTip.hide();
					},
					params : {
						'sortIds' : ids
					}
				});
	},listeners: {  
        itemclick: function(grid, record, item,index,e){  
            var me = this;
            if(me.eapSelectRecord){
            	me.store.remove(me.eapSelectRecord);
				me.store.insert(index, me.eapSelectRecord);
				delete me.eapSelectRecord;
				me.getSelectionModel().select(index);
            }
        }  
    }
});