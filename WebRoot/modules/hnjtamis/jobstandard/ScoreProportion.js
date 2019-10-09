/**
 * 岗位标准管理 
 * Date : 2015.3.27
 * Desc : 岗位标准模块-岗位指定专业维护
 * Author: Wangyong
 */
ClassDefine('modules.hnjtamis.jobstandard.ScoreProportion', {
	extend : 'base.model.List',
	requires : ['base.model.Tree','modules.baseinfo.Dictionary'], 
	
	initComponent : function() {
		var me = this;
		var Dictionary = modules.baseinfo.Dictionary;//类别名
		this.promotionTypes = Dictionary.getDictionaryList('PROMOTION_TYPE');
		// 模块列表对象
		this.columns = [{
					name : 'scoresetid',
					width : 0
				}, {
					name : 'jobsname',
					header : '岗位名称',
					width : 2
				}, {
					name : 'proportionType1',
					header : '学时达标占比',
					width : 1
				}, {
					name : 'proportionType2',
					header : '考试达标占比',
					width : 1
				},{
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
					name : 'jobsname',
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
		this.keyColumnName = "scoresetid";// 主健属性名
		this.viewOperater = true;
		this.addOperater = true;
		this.deleteOperater = true;
		this.updateOperater = true;
		this.listUrl = "jobstandard/scoreproportion/listForScoreProportionListAction!list.action";// 列表请求地址
		this.deleteUrl = "jobstandard/scoreproportion/listForScoreProportionListAction!delete.action";// 删除请求地址
		
		
		// 打开表单页面方法
		this.openFormWin = function(id, callback, readOnly,data,term,oper) {
			var me = this;
			var formConfig = {};
			var readOnly = readOnly || false;
			formConfig.readOnly = readOnly;
			formConfig.fileUpload = true;           
			formConfig.formUrl = "jobstandard/scoreproportion/saveForScoreProportionFormAction!save.action";
			formConfig.findUrl = "jobstandard/scoreproportion/findForScoreProportionFormAction!find.action";
			formConfig.callback = callback;
			formConfig.columnSize = 1;
			formConfig.jsonParemeterName = 'form';
			formConfig.items = new Array();
			formConfig.oper = oper;// 复制操作类型变量
			formConfig.items.push({
						colspan : 1,
						xtype : 'hidden',
						name : 'scoresetid'
					});
			formConfig.items.push({
						colspan : 1,
						checked : true,
						fieldLabel : '岗位',
						name : 'scoreproportionQuarters',
						addPickerWidth:200,
						xtype : 'selecttree',
						readOnly : readOnly,
						nameKey : 'quarterId',
						nameLable : 'quarterName',
						//readerRoot : 'quarters',
						selectType :'quarter',//指定可选择的节点
						selectTypeName:'岗位',
						keyColumnName : 'id',
						titleColumnName : 'title',
						childColumnName : 'children',
						selectUrl : "organization/quarter/odqtreeForQuarterListAction!odqtree.action?organTerm="+base.Login.userSession.currentOrganId
					});
			formConfig.items.push({
						colspan : 1,
						fieldLabel : '学时达标占比(%)',
						name : 'proportionType1',
						xtype : 'numberfield',
						allowBlank : false,
						readOnly : readOnly,
						maxLength : 32
					});
			formConfig.items.push({
						colspan : 1,
						fieldLabel : '考试达标占比(%)',
						name : 'proportionType2',
						xtype : 'numberfield',
						allowBlank : false,
						readOnly : readOnly,
						maxLength : 32
					});
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
						name : 'proportionType3',
						xtype : 'hidden'
					});
			formConfig.items.push({
						colspan : 1,
						name : 'proportionType4',
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
					
			var form = ClassCreate('base.model.Form', formConfig);
			// 表单窗口
			var formWin = new WindowObject({
						layout : 'fit',
						title : '岗位晋升通道',
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
					   var v = {'dept':{'deptId':term.deptTerm,'deptName':''}};
					   //form.getForm().findField('quarter').reflash(
					       //"organization/quarter/listForQuarterListAction!list.action?deptTerm="+term.deptTerm);
					   form.getForm().setValues(v);
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