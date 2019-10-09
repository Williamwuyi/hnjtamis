/**
 * 专业管理 
 * Date : 2015.3.27
 * Desc : 岗位标准模块-标标条款信息维护
 * Author: Wangyong
 */
ClassDefine('modules.hnjtamis.jobstandard.StandardTerms', {
	extend : 'base.model.List',
	initComponent : function() {
		var me = this;
		// 模块列表对象
		this.columns = [{
					name : 'standardid',
					width : 0
				},{
					name : 'standardTypes.parentName',
					header : '类别',
					width : 1.5,
					align : 'center'
				}, {
					name : 'standardTypes.typename',
					header : '模块',
					width : 1.5,
					align : 'center'
				}, {
					name : 'standardname',
					header : '子模块',
					width : 1.5,
					align : 'center'
				},  {
					name : 'contents',
					header : '详细内容',
					width : 4,
					renderer:function(value, metadata, record){ 
						value = me.replaceString(value,"\n","<br>");
					 	if(value!=undefined && value!=null && value!=""){
				    		metadata.tdAttr = " data-qtip = '"+value+"'";
					    }
					    metadata.style="white-space: normal !important;";
					    return value; 
					}
				}, {
					name : 'efficient',
					header : '有效期',
					width : 1,
					align : 'center'
				},  {
					name : 'refScore',
					header : '参考学分',
					width : 1,
					align : 'center'
				}, {
					name : 'upStandardScore',
					header : '达标标准(分)',
					width : 1,
					align : 'center'
				}, {
					name : 'examTypeName',
					header : '考核方式',
					width : 1,
					align : 'center'
				}, {
					name : 'isavailable',
					header : '是否有效',
					width : 0.8,
					renderer : function(value){
						return value==0?"无效":"有效";
					},
					align : 'center'
				}/*,{
					name : 'creationDate',
					header : '创建时间',
					width : 1,
					align : 'center'
				}, {
					name : 'createdBy',
					header : '创建人员',
					width : 1,
					align : 'center'
				}, {
					name : 'lastUpdatedBy',
					header : '最后修改人员',
					width : 1,
					align : 'center'
				}*/
				];
		// 模块查询条件对象
		this.terms = [{
						xtype : 'textfield',
						name  : 'nameTerm',
						fieldLabel : '子模块'
					},{
						name : 'parentNameTerm',
						fieldLabel : '类别',
						xtype : 'selecttree',
						nameKey : 'jstypeid',
						nameLable : 'typename',
						readerRoot : 'children',
						keyColumnName : 'id',
						titleColumnName : 'title',
						childColumnName : 'children',
						selectType : 'parentType',
						selectTypeName : '类别',
						allowBlank : true,
						editorType:'str',
						//selectUrl : 'jobstandard/standardtype/treeForStandardTypeListAction!tree.action',
						selectUrl : 'jobstandard/terms/modeltreeForStandardTermsListAction!modeltree.action?onlyParent=1',
						width:300
					}, {
						name : 'typenameTerm',
						fieldLabel : '模块',
						xtype : 'selecttree',
						nameKey : 'jstypeid',
						nameLable : 'typename',
						readerRoot : 'children',
						keyColumnName : 'id',
						titleColumnName : 'title',
						childColumnName : 'children',
						selectType : 'standardtype',
						selectTypeName : '模块',
						allowBlank : true,
						editorType:'str',
						//selectUrl : 'jobstandard/standardtype/treeForStandardTypeListAction!tree.action',
						selectUrl : 'jobstandard/terms/modeltreeForStandardTermsListAction!modeltree.action',
						width:300
					},{
						width : 300,
						fieldLabel : '培训标准岗位',
						checked : false,
						name : 'quarterStandardIdTerm',
						xtype : 'selecttree',
						nameKey : 'quarterTrainCode',
						nameLable : 'quarterTrainName',
						//readerRoot : 'children',
						keyColumnName : 'id',
						titleColumnName : 'title',
						childColumnName : 'children',
						selectUrl : "organization/quarter/getQuarterStandardForQuarterListAction!getQuarterStandard.action",
						editorType:'str',
						allowBlank : true
					},{
						name : 'quarterIdTerm',
						fieldLabel:"电厂岗位",
						addPickerWidth:300,
						xtype : 'selecttree',
						nameKey : 'jobscode',
						nameLable : 'jobsname',
						readerRoot : 'children',
						keyColumnName : 'id',
						titleColumnName : 'title',
						childColumnName : 'children',
						selectType : 'quarter',
						selectUrl : "organization/quarter/odqtreeForQuarterListAction!odqtree.action?organTerm="+base.Login.userSession.currentOrganId,
						editorType:'str',
						selectTypeName : '岗位',
						allowBlank : true,
						width:300
					}
				
				];
		this.keyColumnName = "standardid";// 主健属性名
		this.viewOperater = true;
		this.addOperater = true;
		this.deleteOperater = true;
		this.updateOperater = true;
		this.listUrl = "jobstandard/terms/listForStandardTermsListAction!list.action";// 列表请求地址
		this.deleteUrl = "jobstandard/terms/listForStandardTermsListAction!delete.action";// 删除请求地址
		
		
		var sortConfig = {};
		//列属性配置复制
		sortConfig.columns = [];
		for(var i=0;i<this.columns.length;i++){
		   if(i==0 || i==1 || i==2 || i==3 || i==4){
		   	   var sortCIndex = sortConfig.columns.length;
			   sortConfig.columns[sortCIndex] = Ext.clone(this.columns[i]);
			   delete sortConfig.columns[sortCIndex].xtype;
		   }
		}
		//打开排序页面
		this.openSortWin = function(record,term,store,callback){
			var me = this;
			sortConfig.keyColumnName = me.keyColumnName;
			sortConfig.sortlistUrl = "jobstandard/terms/sortlistForStandardTermsListAction!sortlist.action";
			sortConfig.jsonParemeterName = 'list';
			sortConfig.saveSortUrl = 'jobstandard/terms/saveSortForStandardTermsFormAction!saveSort.action';
			sortConfig.callback = callback;
			var sortPanel = ClassCreate('base.model.SortList', Ext.clone(sortConfig));
			// 表单窗口
			var sortWin = new WindowObject({
						layout : 'fit',
						title : '排序',
						height : 500,
						width : 700,
						border : false,
						frame : false,
						modal : true,// 模态
						closeAction : 'hide',
						items : [sortPanel]
					});
			sortWin.show();
		}
		
		// 打开表单页面方法
		this.openFormWin = function(id, callback, readOnly,data,term,oper) {
			var me = this;
			var formConfig = {};
			var readOnly = readOnly || false;
			formConfig.readOnly = readOnly;
			formConfig.fileUpload = true;           
			formConfig.formUrl = "jobstandard/terms/saveForStandardTermsFormAction!save.action";
			formConfig.findUrl = "jobstandard/terms/findForStandardTermsFormAction!find.action";
			formConfig.callback = callback;
			formConfig.columnSize = 2;
			formConfig.jsonParemeterName = 'form';
			formConfig.bodyStyle = "padding-top:5px;";
			formConfig.items = new Array();
			formConfig.oper = oper;// 复制操作类型变量
			formConfig.items.push({
						colspan : 1,
						xtype : 'hidden',
						name : 'standardid'
					});
			formConfig.items.push({
						colspan : 1,
						fieldLabel : '子模块',
						name : 'standardname',
						xtype : 'textfield',
						allowBlank : false,
						readOnly : readOnly,
						maxLength : 32,
						width : 320
					});
			formConfig.items.push({
						colspan : 1,
						fieldLabel : '模块',
						name : 'standardTypes',
						addPickerWidth:100,
						xtype : 'selecttree',
						readOnly : readOnly,
						nameKey : 'jstypeid',
						nameLable : 'typename',
						readerRoot : 'children',
						keyColumnName : 'id',
						titleColumnName : 'title',
						childColumnName : 'children',
						selectType : 'standardtype',
						selectTypeName : '模块',
						allowBlank : false,
						//selectUrl : 'jobstandard/standardtype/treeForStandardTypeListAction!tree.action',
						selectUrl : 'jobstandard/terms/modeltreeForStandardTermsListAction!modeltree.action',
						width : 320
					});
		    
			formConfig.items.push({
					colspan : 1,
					checked : false,
					allowBlank : true,
					fieldLabel:"题库",
					xtype : 'selecttree',
					name : 'themeBank',
					nameKey : 'themeBankId',
					nameLable : 'themeBankName',
					readerRoot : 'themeBanks',
					keyColumnName : 'themeBankId',
					titleColumnName : 'themeBankName',
					childColumnName : 'themeBanks',
					selectUrl : 'base/themebank/listForThemeBankListAction!list.action',
					readOnly : readOnly,
					width : 320
			});
			formConfig.items.push({
						colspan : 1,
						fieldLabel : '是否有效',
						name : 'isavailable',
						xtype : 'select',
						data:[[0,'否'],[1,'是']],
						readOnly : readOnly,
						defaultValue : 1,
						width : 320
					});
			
			  formConfig.items.push({
						colspan : 1,
						fieldLabel : '有效期',
						name : 'efficient',
						xtype : 'textfield',
						allowBlank : false,
						readOnly : readOnly,
						maxLength : 10,
						width : 320
					});
			   formConfig.items.push({
						colspan : 1,
						fieldLabel : '参考学分',
						name : 'refScore',
						xtype : 'textfield',
						allowBlank : false,
						readOnly : readOnly,
						maxLength : 10,
						width : 320
					});
				formConfig.items.push({
						colspan : 1,
						fieldLabel : '达标标准(分)',
						name : 'upStandardScore',
						xtype : 'textfield',
						allowBlank : false,
						readOnly : readOnly,
						maxLength : 10,
						width : 320
					});
				formConfig.items.push({
						colspan : 1,
						fieldLabel : '考核方式',
						name : 'examTypeName',
						xtype : 'textfield',
						allowBlank : false,
						readOnly : readOnly,
						maxLength : 10,
						width : 320
					});
					
					
					
			/*formConfig.items.push({
					colspan : 2,
					checked : true,
					allowBlank : true,
					fieldLabel:"涉及岗位",
					xtype : 'selecttree',
					name : 'jobsUnionStandards',
					nameKey : 'jobscode',
					nameLable : 'jobsname',
					readerRoot : 'children',
					keyColumnName : 'id',
					titleColumnName : 'title',
					childColumnName : 'children',
					selectType : 'quarter',
					selectUrl : "organization/quarter/odqtreeForQuarterListAction!odqtree.action?organTerm="+base.Login.userSession.currentOrganId,
					readOnly : readOnly,
					width : 662
			});	*/
			if(oper != 'view'){
				formConfig.items.push({
							colspan : 2,
							width : 662,
							fieldLabel : '培训标准岗位',
							checked : true,
							name : 'jobsStandardQuarters',
							xtype : 'selecttree',
							readOnly : readOnly,
							nameKey : 'quarterTrainCode',
							nameLable : 'quarterTrainName',
							//readerRoot : 'children',
							keyColumnName : 'id',
							titleColumnName : 'title',
							childColumnName : 'children',
							selectUrl : "organization/quarter/getQuarterStandardForQuarterListAction!getQuarterStandard.action",
							allowBlank : false
				});
			}		
		    formConfig.items.push({
						colspan : 2,
						fieldLabel : '详细内容',
						name : 'contents',
						xtype : 'textarea',
						readOnly : readOnly,
						maxLength : 1000,
						width : 662,
						height : 170
					});
			 formConfig.items.push({
						colspan : 2,
						fieldLabel : '参考资料',
						name : 'referenceBook',
						xtype : 'textarea',
						readOnly : readOnly,
						maxLength : 1000,
						width : 662,
						height : 50
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
			if(oper == 'view'){
				me.jobsStandardQuartersForm = ClassCreate('base.core.EditList',{
					colspan : 2,
					fieldLabel : '',
					name : 'jobsStandardQuarters',
					xtype : 'editlist',
					addOperater : false,
					deleteOperater : false,
					readOnly : readOnly,
					viewConfig:{height:100},//高度
					columns : [
								{
									name : 'deptName',
									header : '部门',
									width : 1,
									align:'center',
									sortable:false,
									menuDisabled:true,
									titleAlign:"center"
								},{
									name : 'quarterTrainName',
									header : '岗位',
									width : 1,
									align:'center',
									sortable:false,
									menuDisabled:true,
									titleAlign:"center"
								},
								{
									name : 'quarterTrainCode',
									header : '岗位编码',
									width : 1,
									align:'center',
									sortable:false,
									menuDisabled:true,
									titleAlign:"center"
								}
					 ]
				});
				formConfig.items.push(me.jobsStandardQuartersForm);
			}
			var form = ClassCreate('base.model.Form', formConfig);
			// 表单窗口
			var formWin = new WindowObject({
						layout : 'fit',
						title : '岗位标准条款',
						//height : 500,
						autoHeight : true,
						width : 700,
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
		me.termFormWin.width = 320;
	},
	replaceString : function(str,reallyDo,replaceWith) { 
		var e=new RegExp(reallyDo,"g"); 
		words = str.replace(e, replaceWith); 
		return words; 
	}
});