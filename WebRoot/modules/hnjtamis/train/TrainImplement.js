/**
 * 课件库
 */
ClassDefine('modules.hnjtamis.train.TrainImplement', {
	extend : 'base.model.List',
	requires: [
	           'modules.baseinfo.Dictionary','base.model.Tree'
	       ],
	initComponent : function() {
		var Dictionary = modules.baseinfo.Dictionary;//类别名
		this.categorys = Dictionary.getDictionaryList('TRAIN_CATEGORY');
		this.quarterTypes = Dictionary.getDictionaryList('TRAIN_QUARTER_TYPE');
		var me = this;
		// 模块列表对象
		this.columns = [{
			name : 'id',
			width : 0
		}, {
			name : 'subject',
			header : '课程名称',
			width : 3
		}, {
			name : 'monthPlan.title',
			header : '对应计划',
			width : 2
		}, {
			name : 'category',
			header : '培训类别',
			width : 1,
			renderer : function(value){
				var display = "";
				Ext.Array.each(me.categorys.datas, function(item) {
					if (item['dataKey']==value){
						display = item['dataName'];
					}
				});
				return display;
			}
		}, {
			name : 'quarterType',
			header : '岗位类别',
			width : 1,
			renderer : function(value){
				var display = "";
				Ext.Array.each(me.quarterTypes.datas, function(item) {
					if (item['dataKey']==value){
						display = item['dataName'];
					}
				});
				return display;
			}
		},{
			name : 'startDate',
			header : '开始日期',
			width : 1
		}, {
			name : 'endDate',
			header : '结束日期',
			width : 1
		},{
			name : 'duration',
			header : '课时',
			width : 1
		},{
			name : 'credit',
			header : '学分',
			width : 1
		},{
			name : 'trainMode',
			header : '培训方式',
			width : 1,
			renderer : function(value) {
				return value == "1"?"在线培训":("2"?"集中培训":"委培");
			}
		}, {
			name : 'place',
			header : '培训地点',
			width : 1
		}, {
			name : 'teacher',
			header : '授课人',
			width : 1
		}, {
			name : 'trainManagerName',
			header : '培训负责人',
			width : 1
		}, /*{
			name : 'examineManagerName',
			header : '考核负责人',
			width : 1
		},*/ {
			name : 'isAudited',
			header : '是否审核',
			width : 1,
			renderer : function(value){
				return value==false?"否":"是";
			}
		},{
			name : 'auditerName',
			header : '审核人',
			width : 1
		}];
		// 模块查询条件对象
		this.terms = [{
					xtype : 'textfield',
					name : 'subjectTerm',
					fieldLabel : '课程名称'
				}];
		this.keyColumnName = "id";// 主健属性名
		this.viewOperater = true;
		this.addOperater = true;
		this.deleteOperater = false;
		this.updateOperater = false;
		this.otherOperaters = [];//其它扩展按钮操作
		this.otherOperaters.push(
			{
	            xtype : 'button',
				icon : 'resources/icons/fam/pencil.png',
				text : '修改',
				handler : function() {
					var id = me.getSelectIds();
					if(id==""||id.split(",").length>1){
						Ext.Msg.alert('提示', '请选择一条记录！');
						return;
					}
					var selected = me.getSelectionModel().selected;					
					var record = selected.get(0);
					var isAudited = record.get("isAudited");
					if(isAudited != 1){
						var queryTerm = {};
						if(me.termForm)
							queryTerm = me.termForm.getValues(false);
						me.openFormWin(id, function() {
									me.termQueryFun(false,'flash');//me.pagingToolbar.doRefresh();
								},false,record.data,queryTerm,'update');
					}else{
						Ext.Msg.alert('提示', '该条记录已经审核，不能进行修改！');
					}
				}
			},
			{
	            xtype : 'button',
				icon : 'resources/icons/fam/delete.gif',
				text : '删除',
				handler : function() {
					var id = me.getSelectIds();
					if(id==""||id.split(",").length>1){
						Ext.Msg.alert('提示', '请选择一条记录！');
						return;
					}
					var selected = me.getSelectionModel().selected;					
					var record = selected.get(0);
					var isAudited = record.get("isAudited");
					if(isAudited != 1){
						Ext.MessageBox.confirm('询问', '确定删除该条记录吗？',function(btn){
							if(btn!='yes') return;
							EapAjax.request({
								method : 'GET',
								url : 'train/impl/deleteForTrainImplementListAction!delete.action?id='+id,
								async : true,
								success : function(response) {
									var result = Ext.decode(response.responseText);
									if (Ext.isArray(result)&&result[0].errors) {
										var msg = result[0].errors;
										Ext.Msg.alert('错误', msg);
									} else {
										Ext.Msg.alert('信息', '删除成功！');
										me.termQueryFun(false,'flash');
									}
								},
								failure : function() {
									Ext.Msg.alert('信息', '后台未响应，网络异常！');
								}
							});
						});
					}else{
						Ext.Msg.alert('提示', '该条记录已经审核，不能删除！');
					}
				}
			},
			{
            xtype : 'button',
			icon : 'resources/icons/fam/accept.png',
			text : '审核',
			handler : function() {
				var id = me.getSelectIds();
				if(id==""||id.split(",").length>1){
					Ext.Msg.alert('提示', '请选择一条记录！');
					return;
				}
				var selected = me.getSelectionModel().selected;					
				var record = selected.get(0);
				var isAudited = record.get("isAudited");
				if (isAudited == 1) {
					Ext.Msg.alert('提示', '该记录已经审核！');
					return;
				}
				Ext.MessageBox.confirm('询问', '确定审核通过吗？',function(btn){
					if(btn!='yes') return;
					EapAjax.request({
						method : 'GET',
						url : 'train/impl/auditForTrainImplementListAction!audit.action?id='+id,
						async : true,
						success : function(response) {
							var result = Ext.decode(response.responseText);
							if (Ext.isArray(result)&&result[0].errors) {
								var msg = result[0].errors;
								Ext.Msg.alert('错误', msg);
							} else {
								Ext.Msg.alert('信息', '操作成功！');
								me.termQueryFun(false,'flash');
							}
						},
						failure : function() {
							Ext.Msg.alert('信息', '后台未响应，网络异常！');
						}
					});
				});
			}
		},{
            xtype : 'button',
			icon : 'resources/icons/fam/theme.gif',
			text : '分配题库',
			handler : function() {
				var id = me.getSelectIds();
				if(id==""||id.split(",").length>1){
					Ext.Msg.alert('提示', '请选择一条记录！');
					return;
				}
				var config = {
					  width:500,
					  height:500,
					  //数据提取地址
	    		      selectUrl:"train/impl/findThemeBankTreeForTrainImplementListAction!findThemeBankTree.action?id="+id,
	    		      checked:true,
	    		      selectType:'theme',
	    		      selectTypeName:'题库',
	    		      levelAffect:'20',
				      keyColumnName : 'id',
					  titleColumnName : 'title',
					  childColumnName : 'children',
					  title:'分配题库',
					  selectObject:[]
	    		};
	    		//配置、回调函数
				base.model.Tree.openWin(config,function(ids,selectObject){//ID数组，对象数组
				    var eapMaskTip = EapMaskTip(document.body);
					Ext.Ajax.request({
						method : 'get',
						url : "train/impl/saveThemeBankForTrainImplementFormAction!saveThemeBank.action?id="+id+"&themeBankIds="+ids,
						success : function(response) {
							var ret = Ext.decode(response.responseText);
							if(Ext.isArray(ret)) ret = ret[0];
							if(ret.success)
							   Ext.Msg.alert('信息', '分配成功！');
							else
							   Ext.Msg.alert('信息', ret.errors);
							eapMaskTip.hide();
						},
						failure : function() {
							Ext.Msg.alert('信息', '后台未响应，网络异常！');
							eapMaskTip.hide();
						}
					});
				});
			}
		});
		
		this.listUrl = "train/impl/listForTrainImplementListAction!list.action?organId="+base.Login.userSession.currentOrganId;// 列表请求地址
		this.deleteUrl = "train/impl/deleteForTrainImplementListAction!delete.action?organId="+base.Login.userSession.currentOrganId;// 删除请求地址
		// 打开表单页面方法
		this.openFormWin = function(id, callback, readOnly) {
			var formConfig = {};
			var readOnly = readOnly || false;
			formConfig.readOnly = readOnly;
			//formConfig.fileUpload = true;
			formConfig.formUrl = "train/impl/saveForTrainImplementFormAction!save.action";
			formConfig.findUrl = "train/impl/findForTrainImplementFormAction!find.action";
			formConfig.callback = callback;
			formConfig.columnSize = 2;
			formConfig.jsonParemeterName = 'form';
			formConfig.items = new Array();
			formConfig.items.push({
						colspan : 2,
						xtype : 'hidden',
						name : 'id'
					});
			formConfig.items.push({
						colspan : 2,
						fieldLabel : '主题',
						name : 'subject',
						xtype : 'textfield',
						allowBlank : false,
						readOnly : readOnly,
						size:70,
						maxLength : 32
			});
			var nowyear = new Date();
			formConfig.items.push({
				colspan : 2,
				fieldLabel : '对应月度计划',
				name : 'monthPlan',
				xtype : 'selectobject',
				addPickerWidth:70,
				size:70,
				allowBlank : true,
				readOnly : readOnly,
				valueField : 'id',
				displayField : 'title',
				readerRoot : 'list',
				selectUrl : 'train/monthplan/allForMonthPlanListAction!all.action?statusTerm=3'
			});
			formConfig.items.push(
					modules.baseinfo.Dictionary.createDictionarySelectPanel(
			              '培训类别','TRAIN_CATEGORY','category',true,null,true,readOnly)
			);
			formConfig.items.push(
					modules.baseinfo.Dictionary.createDictionarySelectPanel(
			              '岗位类型','TRAIN_QUARTER_TYPE','quarterType',true,null,true,readOnly)
			);
			formConfig.items.push({
				colspan :1,
				fieldLabel : '开始日期',
				name : 'startDate',
				xtype : 'datefield',
				format:'Y-m-d',
				allowBlank : false,
				readOnly : readOnly,
				maxLength : 20
			});
			formConfig.items.push({
				colspan :1,
				fieldLabel : '结束日期',
				name : 'endDate',
				xtype : 'datefield',
				format:'Y-m-d',
				allowBlank : false,
				readOnly : readOnly,
				maxLength : 20
			});
			formConfig.items.push({
				colspan : 1,
				fieldLabel : '课时',
				name : 'duration',
				xtype : 'numberfield',
				allowBlank : false,
				readOnly : readOnly,
				//size:70,
				maxLength : 4
			});
			formConfig.items.push({
				colspan : 1,
				fieldLabel : '学分',
				name : 'credit',
				xtype : 'numberfield',
				allowBlank : false,
				readOnly : readOnly,
				//size:70,
				maxLength : 4
			});
			formConfig.items.push({
				colspan : 1,
				fieldLabel : '培训方式',
				name : 'trainMode',
				xtype : 'select',
				data:[['1','在线培训'],['2','集中培训'],['3','委培']],
				readOnly : readOnly,
				defaultValue : '1'
			});
			formConfig.items.push({
				colspan :1,
				fieldLabel : '授课人',
				name : 'teacher',
				xtype : 'textfield',
				allowBlank : true,
				readOnly : readOnly,
				maxLength : 20
			});
			formConfig.items.push({
				colspan :1,
				fieldLabel : '上课地点',
				name : 'place',
				xtype : 'textfield',
				allowBlank : true,
				//size:70,
				readOnly : readOnly,
				maxLength : 30
			});
			formConfig.items.push({
				colspan :1,
				fieldLabel : '培训负责人',
				name : 'trainManagerName',
				xtype : 'textfield',
				allowBlank : true,
				readOnly : readOnly,
				maxLength : 20
			});
			/*formConfig.items.push({
				colspan :1,
				fieldLabel : '考核负责人',
				name : 'examineManagerName',
				xtype : 'textfield',
				allowBlank : true,
				readOnly : readOnly,
				maxLength : 20
			});*/
			formConfig.items.push({
				colspan : 2,
				fieldLabel : '课程描述',
				name : 'remark',
				xtype : 'textarea',
				allowBlank : false,
				readOnly : readOnly,
				maxLength : 250,
				width:570,
				height : 80
			});
			formConfig.items.push({
				colspan : 2,
				fieldLabel : '学习目标',
				name : 'learningTarget',
				xtype : 'textarea',
				allowBlank : false,
				readOnly : readOnly,
				maxLength : 250,
				width:570,
				height : 80
			});
			
			var editTable = Ext.widget('editlist',{
				colspan : 2,
				fieldLabel : '选择课件',
				keyColumnName : 'id',
				name : 'trainImplementCourses',
				xtype : 'editlist',
				addOperater : false,
				deleteOperater : true,
				enableMoveButton: true,
				otherOperaters:[{
		            xtype : 'button',
					icon : 'resources/icons/fam/application_view_list.png',
					text : '选择课件',
					handler : function() {
						editTable.getSelectionModel().selectAll();
						var selected = editTable.getSelectionModel().selected;
						var selids = "";
						for (var i = 0; i < selected.getCount(); i++) {
							var record = selected.get(i);
							if (i != 0)
								selids += "," + (record.get("coursewareDistribute"))["id"];
							else
								selids += (record.get("coursewareDistribute"))["id"];
						}
						
						var config = {
							  width:500,
							  height:500,
							  //数据提取地址
			    		      selectUrl:"kb/coursedistribute/findDistributeTreeForCoursewareDistributeListAction!findDistributeTree.action?selectIds=" + selids + "&organId="+base.Login.userSession.currentOrganId,
			    		      checked:true,//是否复选
			    		      selectType:'course',//只有角色结点数据才算
			    		      selectTypeName:'课件',
			    		      levelAffect:'20',//上下级复选框的影响策略
						      keyColumnName : 'id',
							  titleColumnName : 'title',
							  childColumnName : 'children',
							  title:'批量选择课件',
							  selectObject:[]//选择的结点数组,支持ID数组及对象数组
			    		};
						
			    		//配置、回调函数
						base.model.Tree.openWin(config,function(ids,selectObject){//ID数组，对象数组
						    var eapMaskTip = EapMaskTip(document.body);
						    //首先清除表格中所有行
						    for (var i = 0; i <editTable.store.getCount(); i++) {
						    	editTable.store.remove(editTable.store.getAt(i--));
						    }
						    
						    if (ids != null && ids != "") {
							    var idarray = ids.split(',');
							    for(var i = 0; i < idarray.length; i++){
							    	var rowIndex = editTable.store.getCount();
									if (rowIndex >= editTable.allowMaxSize
											&& editTable.allowMaxSize > 0) {
										Ext.Msg.alert('提示','超过最大记录数'+ editTable.allowMaxSize+ '！');
										return;
									}
									Ext.Ajax.request({
										method : 'get',
										url : 'kb/coursedistribute/findForCoursewareDistributeFormAction!find.action?id=' + idarray[i],
										async : false,//同步
										success : function(response) {
											var result = Ext.decode(response.responseText);
											//根据所选节点动态添加行并赋值
											var row = {};
											row['coursewareDistribute'] = {'id':result.form.id,'courseTitle':result.form.courseTitle};
											row['quarter'] = {'quarterId':result.form.quarter.quarterId,'quarterName':result.form.quarter.quarterName};
											row['speciality'] = {'specialityid':result.form.speciality.specialityid,'specialityname':result.form.speciality.specialityname};
											editTable.store.insert(rowIndex, row);
										},
										failure : function() {
											Ext.Msg.alert('信息', '后台未响应，网络异常！');
										}
									});
							    }
							    editTable.getView().refresh();
						    }
						    eapMaskTip.hide();
						});
					}
				}],
				viewConfig:{height:200},//高度
				columns : [{
							width : 0,
							name : 'id'
						}, {
							name : 'coursewareDistribute',
							header : '课件',
							editor : {
								checked : false,
								allowBlank : false,
								//addPickerWidth:100,
								xtype : 'selecttree',
								readOnly : true,
								nameKey : 'id',
								nameLable : 'courseTitle',
								readerRoot : 'children',
								keyColumnName : 'id',
								titleColumnName : 'title',
								childColumnName : 'children',
								selectType:'course',
								selectTypeName:'课件',
								selectUrl : "kb/coursedistribute/findDistributeTreeForCoursewareDistributeListAction!findDistributeTree.action",
								selectEventFun:function(combo,record,index){
									//var td = editTable.getSelectionModel().selected.getAt(0);
									//td.data['quarter'] ={'quarterId':'4028e9864c4eba69014c4ec2202f0044','quarterName':'经理'};
									//editTable.getView().refresh();
								}
						  },
						  renderer:function(value){
						    return value.courseTitle;
						  },
						  width : 2
						}, {
							name : 'quarter',
							header : '岗位',
							editor : {
								checked : false,
								allowBlank : false,
								addPickerWidth:200,
								xtype : 'selecttree',
								readOnly : readOnly,
								nameKey : 'quarterId',
								nameLable : 'quarterName',
								//readerRoot : 'quarters',
								keyColumnName : 'id',
								titleColumnName : 'title',
								childColumnName : 'children',
								selectType:'quarter',
								selectTypeName:'岗位',
								selectUrl : "organization/quarter/odqtreeForQuarterListAction!odqtree.action?organTerm="+base.Login.userSession.currentOrganId
							},
							renderer:function(value){
							    return value.quarterName;
						    },
							width : 2
						}, {
							name : 'speciality',
							header : '专业',
							editor : {
								checked : false,
								allowBlank : false,
								//addPickerWidth:100,
								xtype : 'selecttree',
								readOnly : readOnly,
								nameKey : 'specialityid',
								nameLable : 'specialityname',
								//readerRoot : 'quarters',
								keyColumnName : 'id',
								titleColumnName : 'title',
								childColumnName : 'children',
								selectType:'speciality',
								selectTypeName:'专业',
								selectUrl : "baseinfo/speciality/treeForSpecialityListAction!tree.action?jobidTerm="
							},
							renderer:function(value){
							    return value.specialityname;
						    },
							width : 2
						}],
				readOnly : readOnly
			});
			formConfig.items.push(editTable);
			
			var form = ClassCreate('base.model.Form', formConfig);
			form.parentWindow = this;
			// 表单窗口
			var formWin = new WindowObject({
						layout : 'fit',
						title : '培训课程安排',
						height : 600,
						width : 660,
						border : false,
						frame : false,
						modal : true,// 模态
						closeAction : 'hide',
						items : [form]
					});	
			formWin.show();
			form.setFormData(id);
		};
		this.callParent();
	}
});