/**
 * 岗位标准管理 
 * Date : 2015.3.27
 * Desc : 岗位标准模块-岗位指定专业维护
 * Author: Wangyong
 */
ClassDefine('modules.hnjtamis.jobstandard.JobUnionSpeciality', {
	extend : 'base.model.List',
	requires : ['base.model.Tree','modules.baseinfo.Dictionary'], 
	initComponent : function() {
		var me = this;
		// 模块列表对象
		this.columns = [{
					name : 'jusid',
					width : 0
				}, {
					name : 'jobscode',
					width : 0
				},{
					name : 'jobsname',
					header : '岗位名称',
					width : 2
				}, {
					name : 'speciality.specialityname',
					header : '专业',
					width : 2
				}, {
					name : 'isavailable',
					header : '是否有效',
					width : 1,
					renderer : function(value){
						return value==0?"无效":"有效";
					}
				}, {
					name : 'remarks',
					header : '备注',
					width : 3
				}, {
					name : 'creationDate',
					header : '创建时间',
					width : 1
				}, {
					name : 'createdBy',
					header : '创建人员',
					width : 1
				}, {
					name : 'lastUpdatedBy',
					header : '最后修改人员',
					width : 1
				}];
		// 模块查询条件对象
		this.terms = [{
					name : 'nameTerm',
					fieldLabel : '岗位',
					xtype : 'selecttree',
					nameKey : 'quarterId',
					nameLable : 'quarterName',
					readerRoot : 'children',
					selectType :'quarter',//指定可选择的节点
					selectTypeName:'岗位',
					keyColumnName : 'id',
					addPickerWidth : 200,
					titleColumnName : 'title',
					childColumnName : 'children',
					editorType:'str',//编辑类型为字符串，不是对象
					selectUrl : "organization/quarter/odqtreeForQuarterListAction!odqtree.action?organTerm="+base.Login.userSession.currentOrganId,
					//selectType : 'dept',
					//selectTypeName : '部门',
					selectEventFun:function(combo){
						/*if(combo.type=='organ'){
							this.selectObject = {};
							me.termForm.getForm().findField('organTerm').setValue(combo.value);
						}else
						    me.termForm.getForm().findField('organTerm').setValue('');*/
					}
				}];
		this.keyColumnName = "jusid";// 主健属性名
		this.viewOperater = true;
		this.addOperater = true;
		this.deleteOperater = true;
		this.updateOperater = true;
		this.listUrl = "jobstandard/jobunionspeciality/listForJobUnionSpecialityListAction!list.action";// 列表请求地址
		this.deleteUrl = "jobstandard/jobunionspeciality/listForJobUnionSpecialityListAction!delete.action";// 删除请求地址
		/*
		this.otherOperaters = [];//其它扩展按钮操作
		this.otherOperaters.push({
			                xtype : 'button',
							icon : 'resources/icons/fam/user_edit.png',
							text : eap_operate_add,
							handler : function() {
								var jobsobj = Ext.getCmp(me.defaultTermId).value;
								//var id = me.getSelectIds();
								var id = jobsobj.quarterId;
								//alert(jobsname.quarterId+","+jobsname.quarterName);
								//alert(id);
								if(id=="" || typeof id=="undefined"){
									////alert(me.termForm.findField("jobsname"));
									Ext.Msg.alert('提示', '请选择岗位！');
									return;
								}
								var config = {
									  width:500,
									  height:500,
									  //数据提取地址
					    		      selectUrl:"baseinfo/speciality/treeForSpecialityListAction!tree.action?jobidTerm="+id,
					    		      checked:true,//是否复选
					    		      selectType:'speciality',//指定可选择的节点
					    		      selectTypeName:'专业',
					    		      levelAffect:'20',//上下级复选框的影响策略
								      keyColumnName : 'id',
									  titleColumnName : 'title',
									  childColumnName : 'children',
									  title:'设定岗位关联专业',
									  selectObject:[]//选择的结点数组,支持ID数组及对象数组
					    		};
					    		//配置、回调函数
								base.model.Tree.openWin(config,function(ids,selectObject){//ID数组，对象数组
								    var eapMaskTip = EapMaskTip(document.body);
									Ext.Ajax.request({
										method : 'get',
										url : "jobstandard/jobunionspeciality/saveStandardSpecialitiesForJobUnionSpecialityFormAction!saveStandardSpecialities.action?jobId="+
					    		                 id+"&specialityIds="+ids,
										success : function(response) {
											var ret = Ext.decode(response.responseText);
											if(Ext.isArray(ret)) ret = ret[0];
											if(ret.success)
											   Ext.Msg.alert('信息', '设置成功！');
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
		*/
		// 打开表单页面方法
		this.openFormWin = function(id, callback, readOnly,data,term,oper) {
			var me = this;
			var formConfig = {};
			var readOnly = readOnly || false;
			formConfig.readOnly = readOnly;
			formConfig.fileUpload = true;           
			formConfig.formUrl = "jobstandard/jobunionspeciality/saveForJobUnionSpecialityFormAction!save.action";
			formConfig.findUrl = "jobstandard/jobunionspeciality/findForJobUnionSpecialityFormAction!find.action";
			formConfig.callback = callback;
			formConfig.columnSize = 1;
			formConfig.jsonParemeterName = 'form';
			formConfig.items = new Array();
			formConfig.oper = oper;// 复制操作类型变量
			formConfig.items.push({
						colspan : 1,
						xtype : 'hidden',
						name : 'jusid'
					});
			formConfig.items.push({
						colspan : 1,
						fieldLabel : '岗位',
						name : 'thisquarter',
						xtype : 'selecttree',
						nameKey : 'quarterId',
						nameLable : 'quarterName',
						readerRoot : 'children',
						selectType :'quarter',//指定可选择的节点
						selectTypeName:'岗位',
						keyColumnName : 'id',
						addPickerWidth : 200,
						allowBlank : false,
						titleColumnName : 'title',
						childColumnName : 'children',
						///editorType:'str',//编辑类型为字符串，不是对象
						selectUrl : "organization/quarter/odqtreeForQuarterListAction!odqtree.action?organTerm="+base.Login.userSession.currentOrganId,
					});
			/*
			formConfig.items.push({
						colspan : 1,
						fieldLabel : '专业',
						name : 'speciality',
						addPickerWidth:100,
						xtype : 'selecttree',
						readOnly : readOnly,
						nameKey : 'specialityid',
						nameLable : 'specialityname',
						readerRoot : 'children',
						keyColumnName : 'id',
						titleColumnName : 'title',
						childColumnName : 'children',
						selectType : 'speciality',
						selectTypeName : '专业',
						selectUrl : 'baseinfo/speciality/treeForSpecialityListAction!tree.action'
					});
			*/
		    formConfig.items.push({
						colspan : 1,
						fieldLabel : '是否有效',
						name : 'isavailable',
						xtype : 'select',
						data:[[0,'否'],[1,'是']],
						readOnly : readOnly,
						defaultValue : 1
					});	
		    formConfig.items.push({
						colspan : 1,
						fieldLabel : '备注',
						name : 'remarks',
						xtype : 'textarea',
						readOnly : readOnly,
						maxLength : 1000,
						width : 538,
						height : 70
					});
			formConfig.items.push({
						colspan : 1,
						name : 'jobscode',
						xtype : 'hidden',
						value : 0
					});
            formConfig.items.push({
						colspan : 1,
						name : 'orderno',
						xtype : 'hidden',
						value : 0
					});
		    formConfig.items.push({
						colspan : 1,
						name : 'levelno',
						xtype : 'hidden'
					});
			formConfig.items.push({
						colspan : 1,
						name : 'status',
						xtype : 'hidden'
					});
			formConfig.items.push({
						colspan : 1,
						name : 'lastUpdateDate',
						xtype : 'hidden'
					});
			formConfig.items.push({
						colspan : 1,
						name : 'lastUpdatedBy',
						xtype : 'hidden'
					});
			formConfig.items.push({
						colspan : 1,
						name : 'creationDate',
						xtype : 'hidden'
					});
			formConfig.items.push({
						colspan : 1,
						name : 'createdBy',
						xtype : 'hidden'
					});
			formConfig.items.push({
						colspan : 1,
						name : 'organid',
						xtype : 'hidden'
					});
			
			var editTable = Ext.widget('editlist',{
				colspan : 2,
				fieldLabel : '选择专业',
				keyColumnName : 'specialityid',
				name : 'specialities',
				xtype : 'editlist',
				addOperater : false,
				deleteOperater : true,
				enableMoveButton: false,
				otherOperaters:[{
		            xtype : 'button',
					icon : 'resources/icons/fam/application_view_list.png',
					text : '选择专业',
					handler : function() {
						editTable.getSelectionModel().selectAll();
						var selected = editTable.getSelectionModel().selected;
						var selids = "";
						for (var i = 0; i < selected.getCount(); i++) {
							var record = selected.get(i);
							
						}
						
						var config = {
							  		  width:500,
									  height:500,
									  //数据提取地址
					    		      selectUrl:"baseinfo/speciality/treeForSpecialityListAction!tree.action?jobidTerm="+data.jobscode,
					    		      checked:true,//是否复选
					    		      selectType:'speciality',//指定可选择的节点
					    		      selectTypeName:'专业',
					    		      levelAffect:'20',//上下级复选框的影响策略
								      keyColumnName : 'id',
									  titleColumnName : 'title',
									  childColumnName : 'children',
									  title:'设定岗位关联专业',
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
									////表格赋值
									var row = {};
									row['specialityid']=idarray[i];
									row['specialityname'] = selectObject[i].title;
									///row['contents'] = "f";
									////row['creationDate'] = "";
									editTable.store.insert(rowIndex, row);
									/*
									Ext.Ajax.request({
										method : 'get',
										url : "jobstandard/jobunionstandard/saveStandardTermsForJobUnionStandardFormAction!saveStandardTerms.action?jobId="+
					    		                 id+"&standardtermIds="+ids,
										async : false,//同步
										success : function(response) {
											var result = Ext.decode(response.responseText);
											//根据所选节点动态添加行并赋值
											var row = {};
											row['standardname'] = result.form.;
											row['contents'] = {'quarterId':result.form.quarter.quarterId,'quarterName':result.form.quarter.quarterName};
											row['creationDate'] = {'specialityid':result.form.speciality.specialityid,'specialityname':result.form.speciality.specialityname};
											editTable.store.insert(rowIndex, row);
										},
										failure : function() {
											Ext.Msg.alert('信息', '后台未响应，网络异常！');
										}
									});
									*/
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
							name : 'specialityid'
						}, {
							name : 'specialityname',
							header : '专业名称',
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
						title : '岗位关联专业',
						//height : 500,
						autoHeight : true,
						width : 600,
						border : false,
						frame : false,
						modal : true,// 模态
						closeAction : 'hide',
						items : [form]
					});
			formWin.show();
			form.setFormData(id,function(result){
				if(oper == 'add'){
					if(term){
					   //var v = {'dept':{'deptId':term.deptTerm,'deptName':''}};
					   //form.getForm().findField('quarter').reflash(
					       //"organization/quarter/listForQuarterListAction!list.action?deptTerm="+term.deptTerm);
					   ///form.getForm().setValues(v);
					}
				}else
				if(oper == 'update'){
					//form.getForm().findField('quarter').reflash(
					//       "organization/quarter/listForQuarterListAction!list.action?deptTerm="+result.form.dept.deptId,false);
				}
			});
		};
		me.callParent();
	}
});