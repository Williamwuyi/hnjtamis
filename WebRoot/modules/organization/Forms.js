Ext.define('modules.organization.Forms', {
 	extend : 'base.model.Form',
 	requires : [ 'base.model.Tree', 'modules.baseinfo.Dictionary' ],
	initComponent : function() {
		var me = this;
		me.clearButtonEnabled = false,
		me.saveButtonEnabled = false;
		me.columnSize = 2;
		me.otherOperaters = [];
		me.otherOperaters.push({
				name : 'deptTerm',
				xtype:'textfield',
				labelAlign : 'left',
				labelWidth : 30,
				width:150,
				listeners : {
	                specialkey : function(field, e) {
	                    if (e.getKey() == Ext.EventObject.ENTER) {//13
	                       document.getElementById("buttonId").click();
	                    }
	                }
	            },
				emptyText:'部门'
			},{
				name : 'employeeTerm',
				xtype:'textfield',
				labelAlign : 'left',
				labelWidth : 30,
				width:150,
				listeners : {
	                specialkey : function(field, e) {
	                    if (e.getKey() == Ext.EventObject.ENTER) {//13
	                       document.getElementById("buttonId").click();
	                    }
	                }
	            },
				emptyText:'员工名'
			},
			{
				name : 'postTerm',
				xtype:'textfield',
				labelAlign : 'left',
				labelWidth : 30,
				width:150,
				listeners : {
	                specialkey : function(field, e) {
	                    if (e.getKey() == Ext.EventObject.ENTER) {//13
	                       document.getElementById("buttonId").click();
	                    }
	                }
	            },
				emptyText:'岗位'
			});
		me.otherOperaters.push(Ext.create("Ext.Button", {
			iconCls : 'query',
			text : '查询',
			id:"buttonId",
			style:'margin-right:250px;',
			handler : function() {
				var deptTerm = me.form.findField('deptTerm').value;
				var employeeTerm = me.form.findField('employeeTerm').value;
				var postTerm = me.form.findField('postTerm').value;
				
				if(deptTerm==undefined){
					deptTerm = "";
				}
				if(employeeTerm==undefined){
					employeeTerm = "";
				}
				if(postTerm==undefined){
					postTerm = "";
					
				}
					me.treestore.proxy.url = 'organization/example/odtreeForExampleListAction!odtree.action?organId='+base.Login.userSession.currentOrganId+"&deptTerm="+deptTerm+"&employeeTerm="+employeeTerm+"&postTerm="+postTerm;
					me.treestore.load();
				
				if(deptTerm!=null&&deptTerm!=""			//都为true
						||employeeTerm!=null&&employeeTerm!=""
						||postTerm!=null&&postTerm!=""){
					
					me.modelRightTop.proxy.url='organization/example/listForExampleListAction!list.action';
					me.modelRightTop.load({
						params : {
							deptTerm:deptTerm,
							employeeTerm:employeeTerm,
							postTerm:postTerm,
						}
					});
				}
				
					me.form.findField('employeeTerm').setValue(null);
					me.form.findField('deptTerm').setValue(null);
					me.form.findField('postTerm').setValue(null);
			}
		}));
		me.otherOperaters.push(Ext.create("Ext.Button", {
			xtype : 'button',
			text : '确认选择',
			handler : function() {
				/*var store = me.reightList.getComponent('righttop');
	    		var p= store.ownerCt.getComponent('rightlower');
	    		var datas = p.getStore().data.items;
	    		var json=[];
	    		Ext.each(datas,function(item) {
	    			json.push(item.data);
				});*/
				var addlist = [];
				for(var i=0;i<me.store.getCount();i++){
					addlist[addlist.length] = me.store.getAt(i);
				}
	    		if(me.backFun){
	    			me.backFun(me,addlist);
	    		}
			}
		}));
		//右上数据加载
		me.modelRightTop = Ext.create(
				'Ext.data.JsonStore',
				{
					fields : [ 'employeeId','employeeName','employeeCode','dept','dept.deptName','quarterTrainName','quarter','quarterTrainId','quarterTrainName','validation'],
					//'employeeId','employeeName','employeeCode','sex','dept.deptId','dept.deptName','dept.organ.organId','dept.organ.organName','quarterTrainName','sex','quarter.quarterId','quarter.quarterId','quarterTrainId','quarterTrainName','validation'
					proxy : {
						type : 'ajax',
						url:'organization/example/listForExampleListAction!list.action',//?op="+me.op+"&organTerm='+base.Login.userSession.currentOrganId,
						//url:'organization/employee/listForEmployeeListAction!list.action',
						reader : {
							type : 'json',
							root : 'list'//
						}
					}
				});
//右下空数据集
		me.store = new Ext.data.Store({
			autoLoad : false,
			fields : [ 'employeeId','employeeName','employeeCode','dept','dept.deptName','quarterTrainName','quarter','quarterTrainId','quarterTrainName','validation'],
			proxy : {
				type : 'ajax',
				actionMethods : "POST",
				url : null,
				reader : {
					type : 'json',
					root : 'list',
				}
			},
			remoteSort : false // 默认前端可排序 2018.9.25
		});
		// 树形配置开始
		Ext.define('OrganDeptModel', {
				extend : 'Ext.data.Model',
				fields : [ {
					name : 'id',
					type : 'string'
				}, {
					name : 'text',
					mapping: 'title',
					type : 'string'
				}, {
					name : 'closeIcon',
					type : 'string'
				}, {
					name : 'icon',
					type : 'string'
				}, {
					name : 'leaf',
					type : 'bool'
				}, {
					name : 'type',
					type : 'string'
				}, {
					name : 'tagName',
					type : 'string'
				} ]
			});
		me.treestore = Ext.create(
				'Ext.data.TreeStore',{
					model: OrganDeptModel,
					root: { expanded: false },
					autoLoad: false,
					proxy : {
						type : 'ajax',
						//url : 'organization/dept/odtreeForDeptListAction!odtree.action?organTerm='+base.Login.userSession.currentOrganId+'&nameTerm='+"多经",+'&deptTerm='+deptTerm
						url : 'organization/example/odtreeForExampleListAction!odtree.action?organId='+base.Login.userSession.currentOrganId,
						reader : {
							type : 'json'
						}
					},
					nodeParam : 'parentId',
					defaultRootId : ''
				});
//右侧树点击
		var tree = new Ext.tree.TreePanel(
				{
					store : me.treestore,
					id: 'treeId',
					//title : '机构部门岗位',
					//region : 'west',
					//split : true,
					//collapsible : true,
					rootVisible : false,
					useArrows : true,
					autoScroll : true,
					height:560,
					//minSize : 80,
					//maxSize : 200,
					listeners : {
						itemclick : function(view, record, item, index, e) {
							var type = record.raw.type;
							var id = record.raw.id;
							var selectIds = '';
							for(var i=0;i<me.store.getCount();i++){
								var record = me.store.getAt(i);
								var employeeId = record.get("employeeId");
								if(i<me.store.getCount()-1){
									selectIds+=employeeId+",";
								}else{
									selectIds+=employeeId;
								}
							}
							me.modelRightTop.load({
								params : {
									op : type,
									deptId:id,
									selectIds:selectIds
								}
							});

						}
					}
				});
		me.treestore.on("load", function(mystore,node,success) {
	        var record = mystore.getRootNode().childNodes[0];
	        if(record!=undefined){
	        	record.expand();
	        	tree.getView().refresh();
	        }
		});
		me.treestore.getRootNode().expand(false,false);
//左侧树
		me.leftTreeList = new Ext.Panel({
			region:'west',
			split:true,
			width:300,
			height:670,
			style:'margin-top:-4px;',
			collapsible:false,
			margins : '3 0 3 3',
			items:[tree]
		});
//右侧列表
		me.reightList = new Ext.panel.Panel({
			layout:{
				type:'vbox',//上下布局
				align:'stretch',
				pack:'top'
			},
			region:'center',
			margins : '3 3 3 0',
			width:615,
			height:670,
			style:'margin-top:-4px;',
//双击添加下列表
			items : [{
				xtype : 'grid',
				store : me.modelRightTop,
				itemId : 'righttop',
				flex:1,
				selModel: Ext.create("Ext.selection.CheckboxModel", {
				    injectCheckbox: 1,//checkbox位于哪一列，默认值为0
				    mode: "multi",//multi,simple,single；默认为多选multi
				    checkOnly: true,//如果值为true，则只用点击checkbox列才能选中此条记录
				    allowDeselect: true,//如果值true，并且mode值为单选（single）时，可以通过点击checkbox取消对其的选择
				    enableKeyNav: true,
				}),tbar : [{
					xtype:'text',
					text:'未选中'
						}],
 				columns : [{
					dataIndex : 'employeeId',
					width : 0,
					groupable : false,
					flex : 0
				}, 
	    		new Ext.grid.RowNumberer({text: "序号", width: 40,align: "right",titleAlign: "center"}),
					{
					text : '员工名',
					dataIndex : 'employeeName',
					flex : 4,
					groupable : false,
				}, {
					text : '部门',
					dataIndex : 'dept.deptName',
					flex : 4,
					groupable : false,
				} , {
					text : '岗位',
					dataIndex : 'quarterTrainName',
					flex : 4,
					groupable : false,
				} ],
//右上双击事件				
				listeners:{
					"itemdblclick":function(view, record, item, index){
						DownExtends(record);
				}},
			},
//右下列表
			{
				xtype : 'grid',
				store : me.store,
				itemId : 'rightlower',
				flex:1,
				selModel: Ext.create("Ext.selection.CheckboxModel", {
				    injectCheckbox: 1,//checkbox位于哪一列，默认值为0
				    mode: "multi",//multi,simple,single；默认为多选multi
				    checkOnly: true,//如果值为true，则只用点击checkbox列才能选中此条记录
				    allowDeselect: true,//如果值true，并且mode值为单选（single）时，可以通过点击checkbox取消对其的选择
				    enableKeyNav: true,
				}),
				tbar : [{
					xtype:'text',
					text:'已选中'
				},{
					xtype : 'button',
					icon : 'resources/icons/fam/down_1.png',
					style:'margin-left:150px;',
					handler : function() {
						me.Down();
					}
				}, {
					xtype : 'button',
					icon : 'resources/icons/fam/down_2.png',
					style:'margin-left:50px;',
					handler : function() {
						me.DownAll();
					}
				},{
					xtype : 'button',
					icon : 'resources/icons/fam/up_1.png',
					style:'margin-left:50px;',
					handler : function() {
						me.Up();
					}
				}, {
					xtype : 'button',
					icon : 'resources/icons/fam/up_2.png',
					style:'margin-left:50px;',
					handler : function() {
						me.UpAll();
					}
				} ],
				columns : [ {
					dataIndex : 'employeeId',
					width : 0,
					groupable : false,
					flex : 0
				}, 
				new Ext.grid.RowNumberer({text: "序号", width: 40,align: "right",titleAlign: "center"}),
				{
					text : '员工名',
					dataIndex : 'employeeName',
					flex : 4,
					groupable : false,
				}, {
					text : '部门',
					dataIndex : 'dept.deptName',
					flex : 4,
					groupable : false,
				} , {
					text : '岗位',
					dataIndex : 'quarterTrainName',
					flex : 4,
					groupable : false,
				}],
				listeners:{
					"itemdblclick":function(view, record, item, index){
						UpExtends(record);
				}},
			} ]
		});
		
//通过列表选中添加到from右下列表中(openWin,传列表record选中的数据)
		if(me.selectRows.length>=1){
			Ext.each(me.selectRows,function(item) {
				me.store.insert(me.store.getCount(),item.data);//部署到下空数据集
			});
		}
//添加数据到右下的继承方法
		var DownExtends = function(typeStore){
			Ext.each(typeStore,function(item) {
				me.store.insert(me.store.getCount(),item.data);//部署到下空数据集
			});
			var employeeId="";
			Ext.each(typeStore,function(item) {
				employeeId +=item.data.employeeId+',';
			});
			//var treeId = me.leftTreeList.getComponent('treeId');
			//左侧树被选中的
			//var leftTree = treeId.getSelectionModel().selected.items[0].data;
			if(employeeId!=''){
				for(var i=me.modelRightTop.getCount()-1;i>=0;i--){
					var record = me.modelRightTop.getAt(i);
					var _employeeId = record.get("employeeId");
					if(employeeId.indexOf(_employeeId)!=-1){
						me.modelRightTop.remove(record);
					}	
				}
			}
		};
//右上数据添加到右下
		me.Down = function(){
			var righttop = me.reightList.getComponent('righttop');
			var selected = righttop.getSelectionModel().selected;
			if (selected.getCount() == 0) {
				Ext.Msg.alert('提示','至少选中一条记录');
				return;
			}
			var selectStore = selected.items;
			//删除已选中的
			DownExtends(selectStore);
			
		};
//右上数据全部添加到右下
		me.DownAll = function(){
			var righttop = me.reightList.getComponent('righttop');
			var store = righttop.getStore().data.items;
			//删除已选中的
			DownExtends(store);
		};
//添加到右上的继承方法		
		var UpExtends = function(typeStore){
			/*var rightlower = me.reightList.getComponent('rightlower');
			var selected = rightlower.getSelectionModel().selected;
			*/
			//var reightLowerId = typeStore.data.dept.deptId;
			var treeId = me.leftTreeList.getComponent('treeId').getSelectionModel().selected.items[0];
			//左侧树被选中的
			if(treeId!=undefined){
				var leftTree = treeId.data.id;
			}
			
			Ext.each(typeStore,function(item) {
				if(item.data.dept.deptId==leftTree){
					me.modelRightTop.insert(me.modelRightTop.getCount(),item.data);//部署到下空数据集
				}
				//else{
				//	Ext.Msg.alert('提示','请选择同一部门下');
				//}
			});
			var employeeId="";
			Ext.each(typeStore,function(item) {
				employeeId +=item.data.employeeId+',';
			});
			if(employeeId!=''){
				for(var i=me.store.getCount()-1;i>=0;i--){
					var record = me.store.getAt(i);
					var _employeeId = record.get("employeeId");
					//if(record.data.dept.deptId==leftTree.id){
						if(employeeId.indexOf(_employeeId)!=-1){
							me.store.remove(record);
						}	
					//}
				}
			}
		};
		
//右下数据添加到右上
		me.Up = function(){
			var rightlower = me.reightList.getComponent('rightlower');
			var selected = rightlower.getSelectionModel().selected;
			if (selected.getCount() == 0) {
				Ext.Msg.alert('提示','至少选中一条记录');
				return;
			}
			var selectStore = selected.items;
			UpExtends(selectStore);
		};
//右下全部数据添加到右上
		me.UpAll =function(){
			var rightlower = me.reightList.getComponent('rightlower');
			var store = rightlower.getStore().data.items;
			UpExtends(store);
			//me.store.removeAll();
		};
		//布局
		me.items = [me.leftTreeList,me.reightList];
		me.callParent();
	},
});