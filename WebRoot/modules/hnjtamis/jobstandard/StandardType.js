/**
 * 岗位标准条款类型管理 
 * Date : 2015.3.25
 * Desc : 基础信息模块-岗位标准条款类型维护
 * Author: Wangyong
 */
ClassDefine('modules.hnjtamis.jobstandard.StandardType', {
	extend : 'base.model.List',
	initComponent : function() {
		var me = this;
		// 模块列表对象
		this.columns = [{
					name : 'jstypeid',
					width : 0
				}, {
					name : 'parentName',
					header : '上级分类',
					width : 2
				}, {
					name : 'typename',
					header : '分类名称',
					width : 2
				}, {
					name : 'isavailable',
					header : '是否有效',
					width : 1,
					renderer : function(value){
						return value==0?"无效":"有效";
					}
				},/* {
					name : 'remarks',
					header : '备注',
					width : 3
				},*/ {
					name : 'creationDate',
					header : '创建时间',
					width : 1.5
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
		me.isavailableTermChkObj = new Ext.form.Hidden({
			xtype : 'hidden',
			name : 'isavailableTermChk',
			value : "false"
		});
		this.terms = [{
						xtype : 'textfield',
						name  : 'typenameTerm',
						width : 200,
						labelWidth : 80,
						fieldLabel : '类型名称'
					},{
						//boxLabel:'显示无效的条款类型 ',
		                fieldLabel:'显示无效的条款类型',
		                labelSeparator :'',
		                name:'stTypeTermChk',
		                inputValue : 1,
		                //checked : false,
		                xtype : 'checkbox',
		                labelWidth : 150,
		                width : 200,
		                listeners: {
                    		change: function (comp) {
                    			me.isavailableTermChkObj.setValue(comp.checked?"true":"false");
                    		}
               			 }
					},
					me.isavailableTermChkObj
				];
		this.keyColumnName = "jstypeid";// 主健属性名
		this.viewOperater = true;
		this.addOperater = true;
		this.deleteOperater = true;
		this.updateOperater = true;
		this.listUrl = "jobstandard/standardtype/listForStandardTypeListAction!list.action";// 列表请求地址
		this.deleteUrl = "jobstandard/standardtype/listForStandardTypeListAction!delete.action";// 删除请求地址
		
		
		// 打开表单页面方法
		this.openFormWin = function(id, callback, readOnly,data,term,oper) {
			var me = this;
			var formConfig = {};
			var readOnly = readOnly || false;
			formConfig.readOnly = readOnly;
			formConfig.fileUpload = true;           
			formConfig.formUrl = "jobstandard/standardtype/saveForStandardTypeFormAction!save.action";
			formConfig.findUrl = "jobstandard/standardtype/findForStandardTypeFormAction!find.action";
			formConfig.callback = callback;
			formConfig.columnSize = 1;
			formConfig.jsonParemeterName = 'form';
			formConfig.items = new Array();
			formConfig.bodyStyle = "padding-top:5px;";
			formConfig.oper = oper;// 复制操作类型变量
			formConfig.items.push({
						colspan : 1,
						xtype : 'hidden',
						name : 'jstypeid'
					});
		    formConfig.items.push({
					checked : false,
					allowBlank : true,
					fieldLabel:"上级分类",
					xtype : 'selecttree',
					name : 'parentSpeciltype',
					nameKey : 'jstypeid',
					nameLable : 'typename',
					readerRoot : 'children',
					keyColumnName : 'id',
					titleColumnName : 'title',
					childColumnName : 'children',
					selectType:'standardType',
					selectTypeName:"上级分类",
					selectUrl : "jobstandard/standardtype/standardTypeListTreeForStandardTypeListAction!standardTypeListTree.action?id="+id,
					readOnly : readOnly,
					labelWidth:120,
					width : 300
			});
			formConfig.items.push({
						colspan : 1,
						fieldLabel : '类型名称',
						name : 'typename',
						xtype : 'textfield',
						allowBlank : false,
						readOnly : readOnly,
						maxLength : 32,
						labelWidth:120,
						width : 300
					});
			formConfig.items.push({
						colspan : 2,
						fieldLabel : '题库关联编码',
						name : 'bankMapCode',
						xtype : 'textfield',
						readOnly : readOnly,
						labelWidth:120,
						maxLength : 10,
						width : 300
					});
		    formConfig.items.push({
						colspan : 1,
						fieldLabel : '是否有效',
						name : 'isavailable',
						xtype : 'select',
						data:[[0,'否'],[1,'是']],
						readOnly : readOnly,
						defaultValue : 1,
						labelWidth:120,
						width : 300
					});
					
		    formConfig.items.push({
						colspan : 1,
						fieldLabel : '备注',
						name : 'remarks',
						xtype : 'textarea',
						readOnly : readOnly,
						maxLength : 100,
						width : 538,
						labelWidth:120,
						height : 70
					});
            formConfig.items.push({
						colspan : 1,
						name : 'orderno',
						xtype : 'hidden',
						value : 0
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
						title : '岗位条款类型',
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
					   /// form.getForm().setValues(v);
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