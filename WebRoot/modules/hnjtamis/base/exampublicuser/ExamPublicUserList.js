/**
 * 考生报名管理标准模板的列表页面类
 */
Ext.define('modules.hnjtamis.base.exampublicuser.ExamPublicUserList', {
	extend : 'Ext.grid.GridPanel',
	numberColumnWidth:50,
	enableRowNumber:true,
	enableExportXls:true,//是否导出EXCEL	
	enablePaging:true,//是否分页
	forceFit : true,// 自动填充列宽,根据宽度比例
		// 控制可改变列宽
    enableColumnMove : true,
	enableColumnResize : true,
	stripeRows : true,// 班马线效果
	enableCheck : true,//是否启用复选功能
	enableMerge:false,//是否单元格合并
	initComponent : function() {
		var me = this;
		if(me.enableCheck)
		me.selModel = new Ext.selection.CheckboxModel({
					checkOnly : false
				});
	    if(me.enableMerge){
	    	me.enableColumnMove = false;
	        me.enableColumnResize = false;
	    }
		me.region = 'center';// 对齐属性
		me.margins = '2 2 2 2'; // 为了不要与容器的边框重叠，设定2px的间距
		me.loadMask = true;// 开启提示数据下载
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
					f.width=c.width;
					me.fields[me.fields.length] = f;
				}
			}
		}
		getField(me.columns);
		me.fields[me.fields.length] = 'errors';
		me.store = new Ext.data.Store({
			autoLoad : false,
			pageSize : me.pageRecordSize||grabl_pageRecordSize,
			fields : me.fields,
			proxy : {
				type : 'ajax',
				actionMethods : "POST",
				url : me.listUrl,// 后台请求地址
				reader : {
					type : 'json',
					root : me.jsonParemeterName||'list',
					totalProperty : "total"
				}
			},
			remoteSort : true
		});
		me.store.on("load", function(mystore,records,success) {
			var record = this.getAt(0);
			if (record) {
				var errorinfo = record.get("errors");
				if (errorinfo != undefined && errorinfo != "")
					Ext.MessageBox.alert("错误提示！", errorinfo);
			}
			if(me.enableMerge)
			me.mergeCells(records);//处理单元格合并
		});
		function getColumns(array,level){
			var rowNumber = false;//是否处理行号
			for (var i = 0; i < array.length; i++) {
				var f = array[i];
				if(f.columns){
					getColumns(f.columns,level++);
				}else{
					if (i == 0&&level==0&&!rowNumber){
						if(!me.enableRowNumber){
							Ext.Array.remove(array,f);
						    i--;
						    rowNumber=true;
						    continue;
						}else{
							f = new Ext.grid.RowNumberer(f);
							f.text="序号";
							f.width = me.numberColumnWidth;
							rowNumber=true;
						}
					}
					if(f.sortable==undefined)
					   f.sortable = false;
					if(me.enableMerge){//合并单元格时，不需要列下拉菜单
				        f.menuDisabled=true;
				    }
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
		if(me.enablePaging)
		me.pagingToolbar = me.bbar = new Ext.PagingToolbar({// 分页
			plugins : Ext.create('base.core.PageSizePlugin'),
			pageSize : me.pageRecordSize||grabl_pageRecordSize,
			store : me.store,
			displayInfo : true,
			displayMsg : eap_list_displayMsg,
			emptyMsg : eap_list_emptyMsg
		});
		else if(me.enableExportXls)
		   me.bbar=['->',Ext.create("Ext.Button", {
							iconCls : 'exportXls',
							text : '',
							tooltip : '导出EXCEL',
							handler : function() {
								me.exportXls();
							}
						})];
		var termFormItmes = new Array();
		var defaultFormItemType;
		if(me.terms)
		for (var i = 0; i < me.terms.length; i++) {
			var f = me.terms[i];
			if (i == 0) {
				defaultFormItemType = f.xtype;
				f.xtype = 'hidden';
			}
			termFormItmes[termFormItmes.length] = f;
		}
		me.termForm = new Ext.form.FormPanel({
					fieldDefaults : {
						labelAlign : 'right',
						labelWidth : 100
					},
					border : false,
					frame : false,
					defaultType : 'textfield',
					margins : '2 2 2 2',
					items : termFormItmes
				});
		me.tbar = [];
		if (termFormItmes.length > 0) {
			termFormItmes[0].xtype = defaultFormItemType;
			me.termFormWin = new Ext.Window({
						layout : 'fit',
						bodyStyle : "border:0",
						border : false,
						frame : false,
						width:300,
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
												me.termQueryFun(true,'first');
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
									text : eap_operate_otherterm,
									handler : function(bu,event) {
										me.termFormWin.pageX = this.getX();
										me.termFormWin.pageY = this.getY()
												+ this.getHeight() + 2;
										me.termFormWin.show();
										event.stopPropagation();
										/*Ext.getDoc().on('click', function(e,t){
											//if(e.browserEvent.clientX<this.x||e.browserEvent.clientX>this.x+this.el.dom.clientWidth
											//||e.browserEvent.clientY<this.y||e.browserEvent.clientY>this.y+this.el.dom.clientHeight)
											if(!e.within(this.id,false,true)&&e.target.type!='')
											 this.close();
									    }, me.termFormWin);*/
									    //Ext.menu.Manager.register(me.termFormWin)
									}
								}));
				    me.tbar.push(Ext.create("Ext.Button", {
							iconCls : 'query',
							text:eap_operate_query,
							handler : function() {
								me.termQueryFun(true,'first');
							}
						}));
		}
		me.tbar.push('->');
	    if(me.viewOperater)
	      me.tbar.push(Ext.create("Ext.Button", {
							iconCls : 'view',
							text : eap_operate_view,
							handler : function() {
								var selected = me.getSelectionModel().selected;
								if (selected.getCount() == 0) {
									Ext.Msg.alert('提示', '请选择记录！');
									return;
								}
								var id = "";
								var record = null;
								for (var i = 0; i < selected.getCount(); i++) {
									record = selected.get(i);
									id = record.get(me.keyColumnName);
								}
								var queryTerm = {};
								if(me.termForm)
								  queryTerm = me.termForm.getValues(false);
								me.openFormWin(id, function() {},true,record.data,queryTerm,'view');
							}
						}));
        if(me.addOperater)
		  me.tbar.push(Ext.create("Ext.Button", {
							iconCls : 'add',
							text : me.addText,
							resourceCode:me.addResourceCode,
							handler : function() {
								var queryTerm = {};
								if(me.termForm)
								  queryTerm = me.termForm.getValues(false);
								var record = {'data':{}};
								var selected = me.getSelectionModel().selected;
								for (var i = 0; i < selected.getCount(); i++) {
									record = selected.get(i);
								}
								me.openFormWin('', function() {
											me.termQueryFun(false,'flash');
										},false,record.data,queryTerm,'add');
							}
						}));
	   if(me.deleteOperater)
	     me.tbar.push(Ext.create("Ext.Button", {
							iconCls : 'remove',
							text : eap_operate_delete,
							resourceCode:me.deleteResourceCode,
							handler : function() {
								var selected = me.getSelectionModel().selected;
								if (selected.getCount() == 0) {
									Ext.Msg.alert('提示', '请选择记录！');
									return;
								}
								var confirmFn = function(btn) {
									if (btn == 'yes')
										me.deleteData(me.getSelectIds());
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
								var selected = me.getSelectionModel().selected;
								if (selected.getCount() == 0) {
									Ext.Msg.alert('提示', '请选择记录！');
									return;
								}
								var id = "";
								var record = null;
								for (var i = 0; i < selected.getCount(); i++) {
									record = selected.get(i);
									id = record.get(me.keyColumnName);
								}
								var queryTerm = {};
								if(me.termForm)
									queryTerm = me.termForm.getValues(false);
								me.openFormWin(id, function() {
											me.termQueryFun(false,'flash');//me.pagingToolbar.doRefresh();
										},false,record.data,queryTerm,'update');
							}
						}));
	   if(me.openSortWin)
	     me.tbar.push(Ext.create("Ext.Button", {
							iconCls : 'sort',
							text : eap_operate_order,
							handler : function() {
								var selected = me.getSelectionModel().selected;
								var id = '';
								var record = {};
								for (var i = 0; i < selected.getCount(); i++) {
									record = selected.get(i);
								}
								var queryTerm = {};
								if(me.termForm)
									queryTerm = me.termForm.getValues(false);
								me.openSortWin(record.data,queryTerm,me.store, function() {
									me.termQueryFun(true,'first');//me.pagingToolbar.doRefresh();
								});
							}
						}));
	   if(me.otherOperaters){
		   for(var i=0;i<me.otherOperaters.length;i++){
		   		me.tbar.push(me.otherOperaters[i]);
		   }
	   }
		me.on('afterrender',function(){
			window.setTimeout(function(){
				me.termQueryFun(true,'first');
			},1000);
		});
		me.callParent();
	},
	// 获取选择ID
	getSelectIds : function() {
		var me = this;
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
	},
	/**
	 * 条件查询方法
	 * @param {} setvalued 是否重新设置参数
	 * @param {} type 从第1页刷新flash当前页刷新previous上一页刷新
	 */
	termQueryFun : function(setvalued,type) {
		var me = this;
		if(setvalued==undefined) setvalued=true;
		if(type==undefined) type='first';
		if(setvalued){
			if(me.termForm.items.length>0){
			   me.termForm.items.items[0].setValue(Ext.getCmp(me.defaultTermId).getValue());
			   me.termFormWin.hide();
			}
			Ext.apply(me.store.proxy.extraParams, me.termForm.getValues(false));
		}
		if(me.pagingToolbar){
			if(type=='first')
			   me.pagingToolbar.moveFirst();
			else if(type=='flash')
			   me.pagingToolbar.doRefresh();
			else
			   me.pagingToolbar.movePrevious();
		}else
		    me.store.load();
	},// 删除操作
	deleteData : function(ids) {
		var me = this;
		Ext.Ajax.request({
			method : 'POST',
			url : me.deleteUrl,
			success : function(response) {
				var result = Ext.decode(response.responseText);
				var selected = me.getSelectionModel().selected;
				if(result.success==true)
				Ext.Msg.alert('信息', result.msg, function(btn) {
					if(me.pagingToolbar){
						var pageData = me.pagingToolbar.getPageData();
						if (pageData.currentPage < pageData.pageCount)
							me.termQueryFun(false,'flash');//me.pagingToolbar.doRefresh();
						else {
							if (pageData.currentPage==1||
							(pageData.toRecord - pageData.fromRecord + 1) > selected.getCount())
								me.termQueryFun(false,'flash');//me.pagingToolbar.doRefresh();
							else
								me.termQueryFun(false,'previous');//me.pagingToolbar.movePrevious();
						}
					}else
					   me.store.load();
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
		function isIE() { //判断是否为IE  
		    if (!!window.ActiveXObject || "ActiveXObject" in window)  
		        return true;  
		    else  
		        return false;  
		}		
		var me = this;
		var vExportContent = me.getExcelXml(); // 获取数据
        if (isIE || Ext.isSafari
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
	},
	/**
	 * 左右合并的单元格判断接口方法
	 * @param {} records 表格数据
	 * @param int row 当前行数,从0开始
	 * @param int cell 当前列数,从1开始,不包括复选列
	 * @param string field 字段名
	 * @return 返回no表示不合并，empty表示遇空合并,equal表示相等合并，ee表格遇空或相等合并，all表示合并且内容合并显示
	 */
	leftMerge:function(records,row,cell,field){
		return "ee";
	},
	/**
	 * 上下合并的单元格判断接口方法
	 * @param {} records 表格数据
	 * @param int row 当前行数,从0开始
	 * @param int cell 当前列数,从1开始,不包括复选列
	 * @param string field 字段名
	 * @return 返回no表示不合并，empty表示遇空合并,equal表示相等合并，ee表格遇空或相等合并，all表示合并且内容合并显示
	 */
	upperMerge:function(records,row,cell,field){
		return "ee";
	},
	//合并显示分割字符
	mergeSplitChar:',',
	//使能合并单元格操作
	mergeEnabled:false,
	/**
	 * 合并单元格
	 * @param records 表格数据
	 */
	mergeCells : function(records) {
	    var grid = this;
	    grid.mergeData = [];//存储各单元格的合并信息
		var arrayTr = document.getElementById(grid.getId() + "-body").firstChild.firstChild.lastChild.getElementsByTagName('tr');
		var trCount = arrayTr.length;
		var tdCount = arrayTr[0].getElementsByTagName("td").length;
		var arrayTd;
		var td;
		var merge = function(rowspanObj, removeObjs) { // 定义合并函数
			if (rowspanObj.cellspan != 1) {
				arrayTd = arrayTr[rowspanObj.tr].getElementsByTagName("td"); // 合并行
				td = arrayTd[rowspanObj.td];
				td.rowSpan = rowspanObj.rowspan;
				td.colSpan = rowspanObj.cellspan;
				td.vAlign = "middle";
				td.firstChild.style.marginTop=((rowspanObj.rowspan*21/2-10)+'px');
				td.firstChild.innerHTML = rowspanObj.html;
				Ext.each(removeObjs, function(obj) { // 隐身被合并的单元格
					arrayTd = arrayTr[obj.tr].getElementsByTagName("td");
					arrayTd[obj.td].style.display = 'none';
				});
			}
		};
		var rowspanObj = {}; // 要进行跨列操作的td对象{tr:1,td:2,rowspan:5,cellspan:2,html:''}
		var removeObjs = []; // 要进行删除的td对象[{tr:2,td:2},{tr:3,td:2}]
		var divHtml = null;// 单元格内的数值
		for (var i = 0; i < trCount; i++) {// 逐行操作
			var rowspan = 1;
			var cellspan = 1;
			records[i].mergeData={};
			//行内合并
			for(var col=(grid.enableCheck?1:0);col<tdCount;col++) { // 逐列操作
				var field = grid.columns[col-(grid.enableCheck?1:0)].name;//字段名
		        var fieldValue = records[i].data[field];//字段值
				arrayTd = arrayTr[i].getElementsByTagName("td");
				var cellText = arrayTd[col].firstChild.innerHTML;//显示内容
				if (!divHtml) {
					divHtml = arrayTd[col].firstChild.innerHTML;
					rowspanObj = {
						tr : i,
						td : col,
						rowspan : rowspan,
						cellspan : cellspan,
						html:divHtml
					}
					records[i].mergeData[field]=rowspanObj;
				} else {
					var addf = function() {
						rowspanObj["cellspan"] = rowspanObj["cellspan"]+ 1;
						removeObjs.push({
									tr : i,
									td : col
								});
						if (col == tdCount - 1){
							merge(rowspanObj, removeObjs);// 执行合并函数
							rowspanObj = {};
		                    removeObjs = [];
		                    divHtml=null;
						}
					};
					var mergef = function() {
						merge(rowspanObj, removeObjs);// 执行合并函数
						divHtml = cellText;
						rowspanObj = {
							tr : i,
							td : col,
							rowspan : rowspan,
							cellspan:1,
							html:cellText
						}
						removeObjs = [];
						records[i].mergeData[field]=rowspanObj;
					};
					var mergeRule = grid.leftMerge(records,i,col,field);
					var merged=false;
					if (mergeRule!='no'){
						if(mergeRule=="equal"&&cellText==divHtml)
						    merged=true;
						if(mergeRule=="empty"&&(cellText==""||cellText=="&nbsp;"))
						    merged=true;
						if(mergeRule=="ee"&&(cellText==divHtml||cellText==""||cellText=="&nbsp;"))
						    merged=true;
						if(mergeRule=="all"){
						    merged=true;
						    if(cellText!=null&&cellText!="&nbsp;"&&cellText!="")
						    rowspanObj.html+=grid.mergeSplitChar+cellText;
						}
					}
					if (merged) {
						addf();
					} else
						mergef();
				}
			}
			//列合并
			if(i>0){
				//var preRecord = records[i-1];
				for(var col=(grid.enableCheck?1:0);col<tdCount;col++) { // 逐列操作
					var field = grid.columns[col-(grid.enableCheck?1:0)].name;//字段名				
			        var fieldValue = records[i].data[field];//字段值
					arrayTd = arrayTr[i].getElementsByTagName("td");
					var cellText = arrayTd[col].firstChild.innerHTML;//显示内容
					var mergeData = records[i].mergeData[field];//本格的合并信息
					if(!mergeData)
					    continue;
					var pre = i-1;
					while(!records[pre].mergeData[field]&&pre>=0){
						pre--;
					}
			        if(pre>=0){
			        	var preMergeData = records[pre].mergeData[field];//上一格的合并信息
			        	if(preMergeData.cellspan==mergeData.cellspan){
			        		var mergeRule = grid.upperMerge(records,i,col,field);
			        		var merged=false;
							if (mergeRule!='no'){
								if(mergeRule=="equal"&&preMergeData.html==cellText)
								    merged=true;
								if(mergeRule=="empty"&&(cellText==""||cellText=="&nbsp;"))
								    merged=true;
								if(mergeRule=="ee"&&(preMergeData.html==cellText||cellText==""||cellText=="&nbsp;"))
						            merged=true;
								if(mergeRule=="all"){
								    merged=true;
								    if(cellText!=null&&cellText!=""&&cellText!="&nbsp;")
								    preMergeData.html+=grid.mergeSplitChar+cellText;
								}
							}
							if (merged) {
								records[i].mergeData[field]=null;
								preMergeData.rowspan++;
								arrayTd = arrayTr[preMergeData.tr].getElementsByTagName("td"); // 合并行
								td = arrayTd[preMergeData.td];
								td.rowSpan=td.rowSpan+1;
								td.vAlign = "middle";
				                td.firstChild.style.marginTop=((td.rowSpan*21/2-10)+'px');
				                arrayTr[i].getElementsByTagName("td")[col].style.display = 'none';
				                td.firstChild.innerHTML=preMergeData.html;
							}
			        	}
			        }
				}
			}
		}
	}
});