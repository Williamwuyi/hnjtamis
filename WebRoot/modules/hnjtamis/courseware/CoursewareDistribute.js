/**
 * 课件库分配
 */
ClassDefine('modules.hnjtamis.courseware.CoursewareDistribute', {
	extend : 'base.model.List',
	initComponent : function() {
		var me = this;
		// 模块列表对象
		this.columns = [{
			name : 'id',
			width : 0
		}, {
			name : 'courseware.title',
			header : '标题',
			width : 6
		}, {
			name : 'courseware.type',
			header : '类型',
			width : 2,
			renderer: function(value) {
				return value == 1 ? "课件" : "教材";
			}
		}, {
			name : 'speciality.specialityname',
			header : '专业',
			width : 2
		}, {
			name : 'quarter.quarterName',
			header : '岗位',
			width : 2
		}, {
			name : 'courseware.uploaderName',
			header : '分配人',
			width : 2
		}, {
			name : 'courseware.uploadTime',
			header : '分配时间',
			width : 2
		}, {
			name : 'courseware.isAudited',
			header : '是否审核',
			width : 2,
			renderer : function(value){
				return value==false?"否":"是";
			}
		},{
			name : 'courseware.auditer',
			header : '审核人',
			width : 1
		}/*, {
			name : 'organ.organName',
			header : '所在机构',
			width : 2
		}*/
		];
		// 模块查询条件对象
		this.terms = [{
					xtype : 'textfield',
					name : 'titleTerm',
					fieldLabel : '标题'
				}, {
					fieldLabel : '类型',
					name : 'typeTerm'
				}];
		this.keyColumnName = "id";// 主健属性名
		this.viewOperater = true;
		//this.addOperater = true;
		//this.deleteOperater = true;
		//this.updateOperater = true;
		//this.otherOperaters = [];//其它扩展按钮操作
		/*this.otherOperaters.push({
            xtype : 'button',
			icon : 'resources/icons/fam/accept.png',
			text : '审核',
			handler : function() {
				Ext.MessageBox.confirm('询问', '确定审核通过吗？',function(btn){
					if(btn!='yes') return;
					var id = me.getSelectIds();
					if(id==""||id.split(",").length>1){
						Ext.Msg.alert('提示', '请选择一条记录！');
						return;
					}
					EapAjax.request({
						method : 'GET',
						url : 'kb/coursedistribute/auditForCoursewareDistributeListAction!audit.action?id='+id,
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
		});*/
		
		this.listUrl = "kb/coursedistribute/listForCoursewareDistributeListAction!list.action?organId="+base.Login.userSession.currentOrganId;// 列表请求地址
		this.deleteUrl = "kb/coursedistribute/deleteForCoursewareDistributeListAction!delete.action?organId="+base.Login.userSession.currentOrganId;// 删除请求地址
		// 打开表单页面方法
		this.openFormWin = function(id, callback, readOnly) {
			var formConfig = {};
			var readOnly = readOnly || false;
			formConfig.readOnly = readOnly;
			formConfig.fileUpload = true;
			formConfig.formUrl = "kb/coursedistribute/saveForCoursewareDistributeFormAction!save.action";
			formConfig.findUrl = "kb/coursedistribute/findForCoursewareDistributeFormAction!find.action";
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
				colspan : 1,
				fieldLabel : '类型',
				name : 'typeTerm',
				xtype : 'select',
				data:[[1,'课件'],[2,'教材']],
				readOnly : readOnly,
				defaultValue : 1
			});
			formConfig.items.push({
						colspan : 1,
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
				checked : false,
				fieldLabel : '岗位',
				name : 'quarter',
				addPickerWidth:100,
				xtype : 'selecttree',
				readOnly : readOnly,
				nameKey : 'quarterId',
				nameLable : 'quarterName',
				//readerRoot : 'quarters',
				keyColumnName : 'id',
				titleColumnName : 'title',
				childColumnName : 'children',
				selectUrl : "organization/quarter/odqtreeForQuarterListAction!odqtree.action?organTerm="+base.Login.userSession.currentOrganId
			});
			var form = ClassCreate('base.model.Form', formConfig);
			form.parentWindow = this;
			// 表单窗口
			var formWin = new WindowObject({
						layout : 'fit',
						title : '课件教材分配',
						height : 400,
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