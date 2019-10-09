/**
 * 课件库
 */
ClassDefine('modules.hnjtamis.courseware.Teaching', {
	extend : 'base.model.List',
	requires: [
        'modules.baseinfo.Accessory'
    ],
	initComponent : function() {
		var me = this;
		// 模块列表对象
		this.columns = [{
			name : 'id',
			width : 0
		}, {
			name : 'title',
			header : '标题',
			width : 5
		}, {
			name : 'editor',
			header : '编著者',
			width : 2
		}, {
			name : 'press',
			header : '出版社',
			width : 2
		}, {
			name : 'uploaderName',
			header : '上传人',
			width : 1
		}, {
			name : 'isAnnounced',
			header : '是否共享',
			width : 1,
			renderer : function(value){
				return value==0?"否":"是";
			}
		}, {
			name : 'isAudited',
			header : '是否审核',
			width : 1,
			renderer : function(value){
				return value==0?"否":"是";
			}
		}, {
			name : 'auditer',
			header : '审核人',
			width : 1
		}, {
			name : 'originalOrganId',
			header : '原机构',
			width : 2,
			renderer : function(value,cellmeta,record,rowIndex,columnIndex,store){
			var v = "";
			if (value != "") {
				EapAjax.request({
					method : 'GET',
					url : 'organization/organ/findForOrganFormAction!find.action?id=' + value,
					async : false,
					success : function(response) {
						var result = Ext.decode(response.responseText);	
						v = result.form.organName;
					},
					failure : function() {
						//Ext.Msg.alert('信息', '后台未响应，网络异常！');
					}
				});
			}
			return v;
		}
		}, {
			name : 'organ.organName',
			header : '所在机构',
			width : 2
		}];
		// 模块查询条件对象
		this.terms = [{
					xtype : 'textfield',
					name : 'titleTerm',
					fieldLabel : '标题'
				}, {
					fieldLabel : '上传人',
					name : 'uploaderTerm'
				}];
		this.keyColumnName = "id";// 主健属性名
		this.viewOperater = true;
		this.addOperater = true;
		this.deleteOperater = true;
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
									me.termQueryFun(false,'flash');
								},false,record.data,queryTerm,'update');
					}else{
						Ext.Msg.alert('提示', '该条记录已经审核，不能进行修改！');
					}
				}
			},
			{
	            xtype : 'button',
				icon : 'resources/icons/fam/image_add.png',
				text : '设置封面',
				handler : function() {
					var id = me.getSelectIds();
					if(id==""||id.split(",").length>1){
						Ext.Msg.alert('提示', '请选择一条记录！');
						return;
					}
					me.openPosterFormWin(id);
				}
			},
			{
	            xtype : 'button',
				icon : 'resources/icons/fam/connect.png',
				text : '共享',
				handler : function() {
					var id = me.getSelectIds();
					if(id==""||id.split(",").length>1){
						Ext.Msg.alert('提示', '请选择一条记录！');
						return;
					}
					var selected = me.getSelectionModel().selected;					
					var record = selected.get(0);
					var isAudited = record.get("isAudited");
					if (isAudited != 1) {
						Ext.Msg.alert('提示', '该记录尚未审核！');
						return;
					}
					Ext.MessageBox.confirm('询问', '确定将次课件共享吗？',function(btn){
						if(btn!='yes') return;
						EapAjax.request({
							method : 'GET',
							url : 'kb/course/shareForCoursewareListAction!share.action?id='+id,
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
						url : 'kb/course/auditForCoursewareListAction!audit.action?id='+id,
						async : true,
						success : function(response) {
							var result = Ext.decode(response.responseText);
							if (Ext.isArray(result)&&result[0].errors) {
								var msg = result[0].errors;
								Ext.Msg.alert('错误', msg);
							} else {
								Ext.Msg.alert('信息', '操作成功！');
								me.termQueryFun(false,'flash');
								//审核完成通知后台进行文件转换
								me.callConvert(id, result.msg);
							}
						},
						failure : function() {
							Ext.Msg.alert('信息', '后台未响应，网络异常！');
						}
					});
				});
			}
		});
		
		me.callConvert = function(id, url) {
			Ext.data.JsonP.request({
		     	timeout: 10000,
		  	 	url : url + '/convert?fileid='+id,
		   		callbackKey: "jsonpcallback",
		   		success : function(result) {
		   			//alert("成功："+result.code);
		   		},
        	    failure: function(result) {
		   			//alert("失败："+result.code);
        	    	//console.log(result);
        	    }
		 });
		};
		
		this.listUrl = "kb/course/listForCoursewareListAction!list.action?typeTerm=2&organId="+base.Login.userSession.currentOrganId;// 列表请求地址
		this.deleteUrl = "kb/course/deleteForCoursewareListAction!delete.action?organId="+base.Login.userSession.currentOrganId;// 删除请求地址
		// 打开表单页面方法
		this.openFormWin = function(id, callback, readOnly) {
			var formConfig = {};
			var readOnly = readOnly || false;
			formConfig.readOnly = readOnly;
			formConfig.fileUpload = true;
			formConfig.formUrl = "kb/course/saveForCoursewareFormAction!save.action";
			formConfig.findUrl = "kb/course/findForCoursewareFormAction!find.action";
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
				xtype : 'hidden',
				name : 'type',
				value: 2
			});
			formConfig.items.push({
				colspan : 2,
				xtype : 'hidden',
				name : 'uploaderId'
			});
			formConfig.items.push({
						colspan : 2,
						fieldLabel : '标题',
						name : 'title',
						xtype : 'textfield',
						allowBlank : false,
						readOnly : readOnly,
						size:65,
						maxLength : 32
					});
			formConfig.items.push({
				colspan : 1,
				fieldLabel : '编著者',
				name : 'editor',
				xtype : 'textfield',
				allowBlank : false,
				readOnly : readOnly,
				//size:65,
				maxLength : 32
			});
			formConfig.items.push({
				colspan : 1,
				fieldLabel : '出版社',
				name : 'press',
				xtype : 'textfield',
				allowBlank : false,
				readOnly : readOnly,
				//size:65,
				maxLength : 32
			});
			formConfig.items.push({
				colspan : 1,
				fieldLabel : '机构',
				name : 'organ',
				xtype : 'selecttree',
				readOnly : true,
				addPickerWidth:100,
				nameKey : 'organId',
				nameLable : 'organName',
				readerRoot : 'organs',
				keyColumnName : 'organId',
				titleColumnName : 'organName',
				childColumnName : 'organs'
			});
			formConfig.items.push({
				colspan : 1,
				fieldLabel : '是否共享',
				name : 'isAnnounced',
				xtype : 'select',
				data:[[0,'否'],[1,'是']],
				readOnly : true,
				defaultValue : 0
			});
			formConfig.items.push({
				colspan : 1,
				fieldLabel : '上传人',
				name : 'uploaderName',
				xtype : 'textfield',
				allowBlank : true,
				readOnly : true,
				//size:65,
				maxLength : 32//,value: base.Login.userSession.employeeName
			});
			formConfig.items.push({
				colspan : 1,
				fieldLabel : '上传时间',
				name : 'uploadTime',
				xtype : 'textfield',
				allowBlank : true,
				readOnly : true,
				//size:65,
				maxLength : 32
			});
			formConfig.items.push({
				colspan : 2,
				fieldLabel : '描述',
				name : 'description',
				xtype : 'textarea',
				allowBlank : true,
				readOnly : readOnly,
				maxLength : 250,
				width:540,
				height : 80
			});
			formConfig.items.push({
						colspan : 2,
						fieldLabel : '附件',
						name : 'accessories',
						xtype : 'accessory',
						readOnly : readOnly,
						accessoryType : 'single',
						allowMaxSize : 5,
						viewConfig:{height:120},//高度
					});
			var editTable = Ext.widget('editlist',{
				colspan : 2,
				fieldLabel : '课件分配',
				name : 'coursewareDistributes',
				xtype : 'editlist',
				addOperater : true,
				deleteOperater : true,
				enableMoveButton: false,
				viewConfig:{height:150},//高度
				columns : [{
							width : 0,
							name : 'id'
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
								selectUrl : "organization/quarter/odqtreeForQuarterListAction!odqtree.action?organTerm="+base.Login.userSession.currentOrganId,
								selectEventFun:function(combo,record,index){
									//根据所选岗位ID刷新专业列表
									var record = editTable.getSelectionModel().selected.get(0);
									var specialityField = record.get("speciality");
									specialityField.reflash("baseinfo/speciality/treeForSpecialityListAction!tree.action?jobidTerm=" + combo.value);
								}
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
						}, {
							name : 'isRequired',
							header : '是否必修',
							editor : {
								xtype : 'select',
								allowBlank : true,
								data:[[0,'选修'],[1,'必修']]
							},
							width : 1,
							defaultValue:1,
							renderer:function(value){
								return value==1?"必修":"选修";
						    }
						}],
				readOnly : readOnly
				
			});
			formConfig.items.push(editTable);
			var form = ClassCreate('base.model.Form', formConfig);
			form.parentWindow = this;
			// 表单窗口
			var formWin = new WindowObject({
						layout : 'fit',
						title : '教材管理',
						height : 600,
						width : 600,
						border : false,
						frame : false,
						modal : true,// 模态
						closeAction : 'hide',
						items : [form]
					});	
			formWin.show();
			form.setFormData(id);
		};
		
		me.openPosterFormWin = function(id) {
			var formConfig = {};
			formConfig.fileUpload = true;
			formConfig.formUrl = "kb/course/savePosterForCoursewareFormAction!savePoster.action";
			formConfig.findUrl = "kb/course/findForCoursewareFormAction!find.action";
			//formConfig.callback = callback;
			formConfig.columnSize = 1;
			formConfig.jsonParemeterName = 'form';
			formConfig.items = new Array();
			formConfig.items.push({
						colspan : 3,
						xtype : 'hidden',
						name : 'id'
					});
			var file = Ext.create('Ext.form.field.File',{
				 name:'posterFile',
				 clearOnSubmit:false,
				 buttonText:'浏览',
				 allowBlank : false,
				 emptyText: '请选择一张图片',
				 width: 550
			});
			formConfig.items.push(file);
			formConfig.otherOperaters = [];
			var form = ClassCreate('base.model.Form', formConfig);
			form.parentWindow = this;
			// 表单窗口
			var formWin = new WindowObject({
						layout : 'fit',
						title : '监考日志填写',
						height : 100,
						width : 600,
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