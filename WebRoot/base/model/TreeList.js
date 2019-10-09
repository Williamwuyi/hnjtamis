/**
 * 标准模板的列表树页面类
 */
Ext.define('base.model.TreeList', {
	extend : 'Ext.tree.TreePanel',
	region : 'center',
	margins : '2 2 2 2',
	loadMask : true,
	rootVisible : false,// 是否显示根结点
	autoscroll : true,// 自动滚动
	animate : true,// 展开,收缩动画
	autoHeight : true,// 自动高度,默认为false
	enableDrag : true,// 树的节点可以拖动Drag(效果上是),注意不 是Draggable
	enableDD : true,// 不仅可以拖动,还可以通过Drag改变节点的 层次结构(drap和drop)
	enableDrop : true,// 仅仅drop 6.lines:true//节点间的虚线条
	trackMouseOver : false,// false则mouseover无效果
	useArrows : true,// 小箭头
	selectNode:null,//选择的结点
	checkedNodes:[],//复选的结点
	titleColumnName:'',//标题字段名（树形显示的字段）
	expandState:[],//保存结点的展开状态
	enableRowNumber : true,//是否带序号
	setOtherAttribute:Ext.emptyFn,//设置结点的其它扩展属性
	enableExportXls:true,//是否导出EXCEL
	enableExportExpand:true,//是否导出展开的
	forceFit : true,// 自动填充列宽,根据宽度比例
	// 控制可改变列宽
    enableColumnMove : true,
	enableColumnResize : true,
	stripeRows : true,// 班马线效果
	timeout: 120000,
	initComponent : function() {
		var me = this;
		me.fields = new Array();
		function getField(columns){
			for (var i = 0; i < columns.length; i++) {
				var c = columns[i];
				if(c.columns)
				   getField(c.columns);
				else{
					var f = {
						name : c.name,
						type : c.type
					};
					if (c.type == 'date'){
						f.dateFormat = 'Y-m-d H:i:s';
					}
					if(c.xtype=='treecolumn')
		               me.titleColumnName = c.name;
					f.width=c.width;
					me.fields[me.fields.length] = f;
				}
			}
		}
		getField(me.columns);
		me.fields[me.fields.length] = 'errors';
		me.store = new Ext.data.TreeStore({
					fields : me.fields,
					proxy : {
						type : 'ajax',
						actionMethods : "POST",
						url : me.listUrl,// 后台请求地址
						reader : {
							type : 'json',
							root : me.readerRoot,
							idProperty:me.keyColumnName
						}
					},
					root : {expanded : false},
					autoLoad : false,
					nodeParam:'parentId',
					defaultRootId:'',
					defaultRootProperty:me.childColumnName,
					rootProperty:me.childColumnName
				});
		//监听后台查询方法，有错误就提示！
		me.store.on("load", function(mystore,records,success) {
			        var record = records.childNodes[0];
			        /*Ext.Array.each(records.childNodes,function(item){
			        	item.data.icon='resources/icons/fam/plugin.gif';
			        });*/
					if (record) {
						var errorinfo = record.data["errors"];
						if (errorinfo != undefined && errorinfo != ""){
							Ext.MessageBox.alert("错误提示！", errorinfo);
							records.childNodes[0].remove();//删除此结点
						}
					}
					me.initState();
					me.getView().refresh();
					if(me.maskTip)
					me.maskTip.hide();
				});
	    function getColumns(array,level){
			var rowNumber = false;//是否处理行号
			for (var i = 0; i < array.length; i++) {
				var f = array[i];
				if(!f.titleAlign)
			       f.titleAlign="center";
				if(f.columns){
					getColumns(f.columns,++level);
				}else{
					if (i == 0&&level==0&&!rowNumber){
						if(!me.enableRowNumber){
							Ext.Array.remove(array,f);
						    i--;
						    rowNumber=true;
						    continue;
						}else{
							f = new　Ext.grid.RowNumberer({
									　　 header　:　"序号",
									　　 width　:　me.grabl_tree_numberColumnWidth|| grabl_tree_numberColumnWidth||40,
									    resizable : false,
									    align : 'right',
									　　 renderer:function(value,metadata,record,rowIndex){
									　　       var parent = record.parentNode,data = record.data;
									         var index = 1,st=false;
										     Ext.Array.each(parent.childNodes,function(n){
											    	if(n.data[me.keyColumnName]==data[me.keyColumnName])
											    	   st = true;
											    	if(!st)
											    	index++;
											 });
									         index = (parent.data.index==0?"":(parent.data.index+"."))+index;
									         data.index = index;
									　　　     return　index;
									　　 }
									});
							rowNumber=true;
						}
					}
					if(f.sortable==undefined)
					   f.sortable = false;
					f.dataIndex = f.name;
					if (f.type == 'date'){
						if(!f.format) f.format = 'Y-m-d';
						f.renderer = Ext.util.Format.dateRenderer(f.format);
					}
					if (f.type == 'number'){
						if(!f.format) f.format = '0.00';
						f.align="right";
						f.renderer = Ext.util.Format.numberRenderer(f.format);
					}
					if (f.width == 0){
						Ext.Array.remove(array,f);
						i--;
					}else
					    array[i]=f;
				}
			}
		}
		getColumns(me.columns,0);
		me.defineColumns = me.columns;
		/*var columns = new Array();
		for (var i = 0; i < me.columns.length; i++) {
			if (i == 0&&me.rowIndexEnable){
				columns[0] = 
		    }
			var f = me.columns[i];
			f.dataIndex = f.name;
			if (me.columns[i].type == 'date')
				f.renderer = Ext.util.Format.dateRenderer('Y-m-d');
			if (me.columns[i].type == 'number'){
				if(!f.format) f.format = '0.00';
				r.align="right";
				f.renderer = Ext.util.Format.numberRenderer(f.format);
			}
			if (f.width != 0)
				columns[columns.length] = f;
		}
		me.columns = columns;*/
		var termFormItmes = new Array();
		var defaultFormItemType;
		if(me.terms){
			for (var i = 0; i < me.terms.length; i++) {
				var f = me.terms[i];
				if (i == 0) {
					defaultFormItemType = f.xtype;
					f.xtype = 'hidden';
				}
				termFormItmes[termFormItmes.length] = f;
			}
		}
		me.termForm = new Ext.form.FormPanel({
					fieldDefaults : {
						labelAlign : 'right',
						labelWidth : 70
					},
					border : false,
					frame : false,
					defaultType : 'textfield',
					margins : '2 2 2 2',
					items : termFormItmes
				});
		me.tbar = [];
		if(me.enableExportXls)
		me.bbar = ['->',Ext.create("Ext.Button", {
							iconCls : 'exportXls',
							text : '',
							tooltip : '导出EXCEL',
							handler : function() {
								me.exportXls();
							}
						})];
		if (termFormItmes.length > 0) {
			termFormItmes[0].xtype = defaultFormItemType;
			me.termFormWin = new Ext.Window({
						layout : 'fit',
						bodyStyle : "border:0",
						border : false,
						frame : false,
						closeAction : 'hide',
						resizable : false,
						closable : false,
						draggable : false,
						modal : true,
						tbar : ["->", Ext.create("Ext.Button", {
											iconCls : 'confirm',
											tooltip : '确认查询',
											handler : function() {
												me.termFormWin.close();
												me.termQueryFun();
											}
										}), Ext.create("Ext.Button", {
											iconCls : 'clear',
											tooltip : '清除条件',
											handler : function() {
												me.termForm.getForm().reset();
												Ext.getCmp(me.defaultTermId)
														.setValue("");
											}
										}), Ext.create("Ext.Button", {
											iconCls : 'close',
											tooltip : '关闭小窗口',
											align : 'right',
											handler : function() {
												me.termFormWin.close();
											}
										})],
						items : [me.termForm]
					});
			me.defaultTermId = "defaultTermId"
					+ parseInt(Math.random() * 1000000);
			me.tbar.push(Ext.applyIf(me.terms[0], {
				                labelAlign : 'right',
						        labelWidth : 70,
								id : me.defaultTermId
							}));
		    me.tbar.push("-");
			if(termFormItmes.length > 1)
			   me.tbar.push(Ext.create("Ext.Button", {
								text : eap_operate_otherterm,//更多条件
								handler : function() {
									me.termFormWin.pageX = this.getX();
									me.termFormWin.pageY = this.getY()
											+ this.getHeight() + 2;
									me.termFormWin.show();
									//event.stopPropagation();
									if(typeof event.stopPropagation === 'function'){
										event.stopPropagation();
									}else{
										event.cancelBubble=true;
									}
									/*Ext.getDoc().on('click', function(e,t){
										if(!e.within(this.id,false,true)&&e.target.type!='')
										 this.close();
								    }, me.termFormWin);*/
								}
							}));
			me.tbar.push(Ext.create("Ext.Button", {
								iconCls : 'query',
								text:eap_operate_query,
								handler : function() {
									me.termQueryFun();
								}
							}));
		}
		me.tbar.push('->');
	    if(me.viewOperater)
	      me.tbar.push(Ext.create("Ext.Button", {
							iconCls : 'view',
							text : eap_operate_view,
							handler : function() {
								if (me.selectNode==null) {
									Ext.Msg.alert('提示', '请单击记录！');
									return;
								}
								var queryTerm = {};
								if(me.termForm)
								  queryTerm = me.termForm.getValues(false);
								me.openFormWin(me.selectNode.id, function() {},true,me.selectNode,queryTerm,'view');
							}
						}));
        if(me.addOperater)
		  me.tbar.push(Ext.create("Ext.Button", {
							iconCls : 'add',
							text : eap_operate_add,
							resourceCode:me.addResourceCode,
							handler : function() {
							    var queryTerm = {};
								if(me.termForm)
								  queryTerm = me.termForm.getValues(false);
								me.openFormWin('', function() {
											me.termQueryFun();
										},false,me.selectNode,queryTerm,'add');
							}
						}));
	   if(me.deleteOperater)
	     me.tbar.push(Ext.create("Ext.Button", {
							iconCls : 'remove',
							text : eap_operate_delete,
							resourceCode:me.deleteResourceCode,
							handler : function() {
								if (me.selectNode==null) {
									Ext.Msg.alert('提示', '请单击记录！');
									return;
								}
								var confirmFn = function(btn) {
									if (btn == 'yes')
										me.deleteData(me.selectNode.id);
								};
								Ext.MessageBox.confirm('询问', '你真要删除这些数据吗？',
										confirmFn);
							}
						}));
	   if(me.updateOperater)
	     me.tbar.push(Ext.create("Ext.Button", {
							iconCls : 'update',
							text : eap_operate_update,
							resourceCode:me.updateResourceCode,
							handler : function() {
								if (me.selectNode==null) {
									Ext.Msg.alert('提示', '请单击记录！');
									return;
								}
								var queryTerm = {};
								if(me.termForm)
								  queryTerm = me.termForm.getValues(false);
								me.openFormWin(me.selectNode.id, function() {
											me.termQueryFun();
										},false,me.selectNode,queryTerm,'update');
							}
						}));
	   if(me.openSortWin)
	     me.tbar.push(Ext.create("Ext.Button", {
							iconCls : 'sort',
							text : eap_operate_order,
							handler : function() {
								var queryTerm = {};
								if(me.termForm)
								  queryTerm = me.termForm.getValues(false);
								me.openSortWin(me.selectNode,queryTerm,me.store, function() {
									me.termQueryFun();
								});
							}
						}));
	   if(me.otherOperaters){
		   for(var i=0;i<me.otherOperaters.length;i++){
		   		me.tbar.push(me.otherOperaters[i]);
		   }
	   }
		me.callParent();
		me.on("itemclick",me.treeItemClickFn);
		me.on("itemexpand",function(node){
		    var me = this;
		    var find = false;
		    Ext.Array.each(me.expandState,function(n){
		    	if(node.data[me.keyColumnName]==n.id){
		    		n.expanded=true;
		    		find = true;
		    	}
		    });
		    if(!find){
		    	var n = {};
		    	n.id = node.data[me.keyColumnName];
		    	n.expanded = true;
		    	me.expandState.push(n);
		    }
		});
		me.on("itemcollapse",function(node){
		    var me = this;
		    var find = false;
		    Ext.Array.each(me.expandState,function(n){
		    	if(node.data[me.keyColumnName]==n.id){
		    		n.expanded=false;
		    		find = true;
		    	}
		    });
		    if(!find){
		    	var n = {};
		    	n.id = node.data[me.keyColumnName];
		    	n.expanded = false;
		    	me.expandState.push(n);
		    }
		});
		me.on('afterrender',function(){
			me.maskTip = EapMaskTip(document.body);
			window.setTimeout(function(){
				if(me.terms&&me.terms.length>0)me.termForm.items.items[0].setValue(Ext.getCmp(me.defaultTermId).getValue());
		        Ext.apply(me.store.proxy.extraParams, me.termForm.getValues(false));
			    me.store.getRootNode().expand();
			},1000);
		});
		//Ext.Function.defer(me.init, 10, me)
		me.setGridColumnHeaderAlign();
	},//头对齐
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
			//结点单面事件
	treeItemClickFn: function(node, item, index, e){
		var me = this;
		me.selectNode = item.data;
		Ext.applyIf(me.selectNode,{id:item.data[me.keyColumnName],text:item.data[me.titleColumnName]});
		if(node.checked){
			me.checkedNodes.push({id:node.id,text:node.text});
		}
		if(false==node.checked){
			Ext.Array.remove(me.checkedNodes,{id:node.id,text:node.text});
		}
	},//展开事件
	treeBeforeexpandnodeFn:function(node, deep, animal){
	},// 条件查询方法
	termQueryFun : function() {
		var me = this;
		me.maskTip.show();
		if(me.terms&&me.terms.length>0){
			me.termForm.items.items[0].setValue(Ext.getCmp(me.defaultTermId).getValue());
			me.termFormWin.hide();
		}
		Ext.apply(me.store.proxy.extraParams, me.termForm.getValues(false));
		me.store.load();
		me.selectNode = null;
	},//初始结点的展开或收缩状态
	initState:function(trees){
		var me = this;
	    if(!trees){
	      trees = me.store.getRootNode().childNodes;
	    }
	    Ext.Array.each(trees,function(n){
		    	Ext.Array.each(me.expandState,function(p){
		    		if(n.data[me.keyColumnName]==p.id){
		    			if(p.expanded)
		    		      n.expand();
		    		    //else n.collapse();
		    		}
		    	});
		    	if(me.setOtherAttribute) 
		    	    me.setOtherAttribute(n.data)
		    	me.initState(n.childNodes);
		});
	},// 删除操作
	deleteData : function(ids) {
		var me = this;
		Ext.Ajax.request({
			method : 'POST',
			url : me.deleteUrl,
			success : function(response) {
				var result = Ext.decode(response.responseText);
				if(result.success==true)
					Ext.Msg.alert('信息', result.msg, function(btn) {
						me.termQueryFun();
					});
			    else
			        Ext.Msg.alert('错误提示', result[0].errors);
			},
			failure : function(response) {
				var result = Ext.decode(response.responseText);
				if (result && result.length > 0)
					Ext.Msg.alert('错误提示', result[0].errors);
				else
					Ext.Msg.alert('信息', '后台未响应，网络异常！');
			},
			params : "id=" + ids
		});
	},//导出Excel
	exportXls : function(){
		var me = this;
		var vExportContent = me.getExcelXml(); // 获取数据
        if (Ext.isIE8||Ext.isIE6 || Ext.isIE7 || Ext.isSafari
                || Ext.isSafari2 || Ext.isSafari3) { // 判断浏览器 
            var fd = Ext.get('frmDummy');
            if (!fd) {
                fd = Ext.DomHelper.append(
                        Ext.getBody(), {
                            tag : 'form',
                            method : 'post',
                            id : 'frmDummy',
                            action : 'base/core/exportXls.jsp',
                            target : '_blank',
                            name : 'frmDummy',
                            cls : 'x-hidden',
                            cn : [ {
                                tag : 'input',
                                name : 'exportContent',
                                id : 'exportContent',
                                type : 'hidden'
                            } ]
                        }, true);                 
            }
            fd.child('#exportContent').set( {
                value : vExportContent
            });
            fd.dom.submit();
        } else {
            document.location = 'data:application/vnd.ms-excel;base64,' + Base64
                    .encode(vExportContent);
        }
	}
});