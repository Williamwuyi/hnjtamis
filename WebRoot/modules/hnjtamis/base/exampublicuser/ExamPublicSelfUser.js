/**
 * 考生自主报名 模块类
 */
ClassDefine('modules.hnjtamis.base.exampublicuser.ExamPublicSelfUser', {
	extend : 'modules.hnjtamis.base.exampublicuser.ExamPublicUserList',
	initComponent : function() {
		var me = this;
		me.form = null;
		
		me.Wi = [ 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2, 1 ];    // 加权因子   
		me.ValideCode = [ 1, 0, 10, 9, 8, 7, 6, 5, 4, 3, 2 ];            // 身份证验证位值.10代表X  
		me.addText = '';//控制增加按钮的名称
		me.addText = '我要报名';
		me.enroll = 'self';
		me.adminflag = true;//管理员标识
		me.updateBtn = null;
		this.listeners = {
            	select:function(obj,record,index,eOpts){
            		
            		var userId = record.data['userId'];
            		if(userId==null || userId==''){
            			me.updateBtn.setDisabled(true);
            		}else{
            			me.updateBtn.setDisabled(false);
            		}
            	}
        };
		
		// 模块列表对象
		this.columns = [{
					name : 'userId',
					width : 0
				},{
					name : 'publicId',
					width : 0
				},{
					name : 'isDeadLine',
					width :0
						
				},{
					name : 'examTitle',
					header : '考试名称',
					width : 1
				},{
					name : 'examStartTime',
					header : '报名开始时间',
					width : 1
				},{
					name : 'examEndTime',
					header : '报名结束时间',
					width : 1
				},{
					name : 'isDel',
					header : '参加报名',
					renderer : function(value){
						if(value == 0){
							return '<font color="green">是</font>';
						}else if(value == 1){
							return '<font color="red">否</font>';
						}
					},
					width : 1
				}];
		// 模块查询条件对象
		this.terms = [
		        /*{
					xtype : 'select',
					name : 'titleTerm',
					fieldLabel : '考试信息名称',
					selectUrl:'base/exampublic/comboExamPublishForExamPublicListAction!comboExamPublish.action',
					valueField:'examTitle',
					displayField:'examTitle',
					width : 340,
					labelWidth :90,
					jsonParemeterName:'comboExamPublish'
				}*/{
					xtype : 'select',
					fieldLabel : '是否截止报名',
					name:'passDeadLineTerm',
					data:[['2','全部'],['1','已截止'],['0','未截止']],
     		        labelWidth : 120,
					width:340
				},{
					xtype : 'datefield',
					name : 'startTerm',
					format : 'Y-m-d',
					fieldLabel : '报名起始时间',
					labelWidth : 100,
					width:280
				},{
					xtype : 'datefield',
					name : 'endTerm',
					format : 'Y-m-d',
					fieldLabel : '报名截止时间',
					labelWidth : 100,
					width:280
				},/*{
					xtype : 'radiogroup',
					fieldLabel : '审核状态',
					items : [
					         {
					        	 boxLabel :'保存',
					        	 name : 'stateTerm',
					        	 inputValue : '10'
					         },
					         {
					        	 boxLabel :'已通过',
					        	 name : 'stateTerm',
					        	 inputValue : '20'
					         }
					         ],
					 width:240
				},*/{
					xtype : 'radiogroup',
					fieldLabel : '是否报名',
					items : [
					         {
								 boxLabel :'全部',
								 name : 'takeInTerm',
								 inputValue : '2',
								 checked: true
					         },
					         {
					        	 boxLabel :'已报名',
					        	 name : 'takeInTerm',
					        	 inputValue : '1'
					         },
					         {
					        	 boxLabel :'未报名',
					        	 name : 'takeInTerm',
					        	 inputValue : '0'
					         }
					         ],
					 labelWidth : 100,
					 width:280
				}];
		this.keyColumnName = "userId";// 主健属性名
		this.jsonParemeterName = "selfList";
		this.viewOperater = true;
		//this.addOperater = true;
		
		this.otherOperaters = [];//其它扩展按钮操作
		//增加
		me.addBtn = Ext.create('Ext.button.Button',{
			iconCls : 'add',
			text : me.addText,
			resourceCode:me.addResourceCode,
			handler : function() {
				var selected = me.getSelectionModel().selected;
				if (selected.getCount() == 0) {
					Ext.Msg.alert('提示', '请选择记录！');
					return;
				}
				var id = "";
				var publicId="";
				var isDeadLine = false;
				var record = null;
				for (var i = 0; i < selected.getCount(); i++) {
					record = selected.get(i);
					id = record.get(me.keyColumnName);
					publicId = record.get("publicId");
					isDeadLine = record.get('isDeadLine');
				}
				if(isDeadLine){
					Ext.Msg.alert('提示','已过报名时间');
					return;
				}
				var queryTerm = {};
				if(me.termForm)
					queryTerm = me.termForm.getValues(false);
				me.openFormWin(id, function() {
							me.termQueryFun(false,'flash');//me.pagingToolbar.doRefresh();
						},false,record.data,queryTerm,'add',publicId);
			}
		});
		//修改
		me.updateBtn = Ext.create('Ext.button.Button',{
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
				var publicId="";
				var record = null;
				for (var i = 0; i < selected.getCount(); i++) {
					record = selected.get(i);
					id = record.get(me.keyColumnName);
					publicId = record.get("publicId");
				}
				var queryTerm = {};
				if(me.termForm)
					queryTerm = me.termForm.getValues(false);
				me.openFormWin(id, function() {
							me.termQueryFun(false,'flash');//me.pagingToolbar.doRefresh();
						},false,record.data,queryTerm,'update',publicId);
			}
		});
		this.otherOperaters.push(me.addBtn);
		this.otherOperaters.push(me.updateBtn);
		
		
		this.listUrl = "base/exampublicuser/selfListForExamPublicUserListAction!selfList.action";// 列表请求地址
		this.deleteUrl = "base/exampublicuser/deleteForExamPublicUserListAction!delete.action";// 删除请求地址
		 
		var IsExsit=false;
		var IsExsitFlag=true;
		// 打开表单页面方法
		this.openFormWin = function(id, callback, readOnly,data,term,oper,publicId) {
			var formConfig = {};
			var readOnly = readOnly || false;
			formConfig.readOnly = readOnly;
			//formConfig.fileUpload = true;
			formConfig.formUrl = "base/exampublicuser/saveForExamPublicUserFormAction!save.action?enroll="+me.enroll+"&oper="+oper+"&publicId="+publicId;
			formConfig.findUrl = "base/exampublicuser/findForExamPublicUserFormAction!find.action?enroll="+me.enroll+"&oper="+oper+"&publicId="+publicId;
			formConfig.callback = callback;
			formConfig.clearButtonEnabled = false;
			formConfig.columnSize = 2;
			formConfig.jsonParemeterName = 'form';
			formConfig.items = new Array();
			formConfig.oper = oper;// 复制操作类型变量
			formConfig.items.push({
						xtype : 'hidden',
						name : 'userId'
					});
			//=====================================================================
			
			formConfig.items.push({
					colspan : 2,
					xtype : 'select',
					name : 'examPublicId',
					fieldLabel : '考试信息名称',
					selectUrl:'base/exampublic/queryComboBoxSelfForExamPublicListAction!queryComboBoxSelf.action',
					valueField:'publicId',
					displayField:'examTitle',
					jsonParemeterName:'comboExamPublish',
					allowBlank : false,
					readOnly:true,
					width : 537
			});
			
			formConfig.items.push({
				colspan : 1,
				fieldLabel : '机构',
				name : 'organ',
				xtype : 'selecttree',
				allowBlank : false,
				addPickerWidth:200,
				readOnly : true,
				nameKey : 'organId',
				nameLable : 'organName',
				readerRoot : 'organs',
				keyColumnName : 'organId',
				titleColumnName : 'organName',
				childColumnName : 'organs',
				selectUrl : "organization/organ/listForOrganListAction!list.action?topOrganTerm="+base.Login.userSession.currentOrganId,
				selectEventFun:function(combo,record,index){
					var deptField = me.form.getForm().findField('dept');
					deptField.reflash("organization/dept/listForDeptListAction!list.action?organTerm="+(combo.value));
					var employeeField = me.form.getForm().findField('employee');
					employeeField.reflash('organization/employee/listForEmployeeListAction!list.action?organTerm='+combo.value+"&limit=999");
				}
			});
			formConfig.items.push({
				colspan : 1,
				fieldLabel : '部门',
				name : 'dept',
				xtype : 'selecttree',
				readOnly : true,
				addPickerWidth:200,
				nameKey : 'deptId',
				nameLable : 'deptName',
				readerRoot : 'depts',
				allowBlank : false,
				keyColumnName : 'deptId',
				titleColumnName : 'deptName',
				childColumnName : 'depts',
				selectUrl : "organization/dept/listForDeptListAction!list.action?organTerm="+(data['organId']?data['organId']:base.Login.userSession.currentOrganId),
				selectEventFun:function(combo,record,index){
					var employeeField = me.form.getForm().findField('employee');
					employeeField.reflash("organization/employee/listForEmployeeListAction!list.action?deptTerm="+(combo.value||'')
					                     +'&organTerm='+me.form.getForm().findField('organ').getValue().organId+"&limit=999");
					var quarterField = me.form.getForm().findField('quarter');
					quarterField.reflash("organization/quarter/listForQuarterListAction!list.action?deptTerm="+(combo.value));
				}
			});
			formConfig.items.push({
				colspan : 1,
				fieldLabel : '岗位',
				name : 'quarter',
				addPickerWidth:200,
				xtype : 'selecttree',
				readOnly : true,
				allowBlank : false,
				nameKey : 'quarterId',
				nameLable : 'quarterName',
				readerRoot : 'quarters',
				keyColumnName : 'quarterId',
				titleColumnName : 'quarterName',
				childColumnName : 'quarters',
				selectUrl : "organization/quarter/listForQuarterListAction!list.action?deptTerm="+(data['userDeptId']?data['userDeptId']:base.Login.userSession.currentDeptId)
							+"&organTerm="+(data['organId']?data['organId']:base.Login.userSession.currentOrganId),
				selectEventFun:function(combo,record,index){
					var employeeField = me.form.getForm().findField('employee');
					employeeField.reflash("organization/employee/listForEmployeeListAction!list.action?deptTerm="+me.form.getForm().findField('dept').getValue().deptId
					                     +'&organTerm='+me.form.getForm().findField('organ').getValue().organId
					                     +'&quarterTerm='+(combo.value))+"&limit=999";
					
				}
			});
			
			formConfig.items.push({
				colspan : 1,
				fieldLabel : '姓名',
				name : 'employee',
				xtype : 'selectobject',
				readOnly : true,
				valueField : 'employeeId',
				displayField : 'employeeName',
				readerRoot : 'list',
				allowBlank : false,
				enableSelectOne : false,//缺省不选择员工
				selectUrl : 'organization/employee/listForEmployeeListAction!list.action?organTerm='
				           +(data['organId']?data['organId']:base.Login.userSession.currentOrganId)
				           +'&deptTerm='+(data['userDeptId']?data['userDeptId']:base.Login.userSession.currentDeptId)+"&limit=999"
			});
			
			formConfig.items.push({
				colspan : 1,
				fieldLabel : '性别',
				xtype : 'radiogroup',
				items:[
				       {
				    	   boxLabel : '男',
				    	   name : 'userSex',
				    	   inputValue :'1'
				       },
				       {
				    	   boxLabel : '女',
				    	   name : 'userSex',
				    	   inputValue :'2'
				       }
				       ],
				width:240,
				allowBlank : false,
				readOnly : readOnly
			});
			formConfig.items.push({
				colspan : 1,
				name : 'userBirthday',
				fieldLabel : '出生年月',
				xtype : 'datefield',
				format : 'Y-m-d',
				readOnly : readOnly
			});
			formConfig.items.push({
				colspan : 1,
				name : 'userNation',
				fieldLabel : '名族',
				xtype : 'textfield',
				readOnly : readOnly
			});
			formConfig.items.push({
				colspan : 1,
				name : 'userPhone',
				fieldLabel : '联系电话',
				xtype : 'textfield',
				readOnly : readOnly
			});
			formConfig.items.push({
				colspan : 2,
				name : 'userAddr',
				fieldLabel : '住址',
				xtype : 'textfield',
				readOnly : readOnly,
				width : 537
			});
			formConfig.items.push({
				colspan : 2,
				name : 'userInfo',
				fieldLabel : '考生信息',
				xtype : 'textarea',
				readOnly : readOnly,
				width : 537
			});
			formConfig.items.push({
				colspan : 1,
				name : 'inticket',
				fieldLabel : '准考证号',
				xtype : 'hidden',
				readOnly : true
			});
			formConfig.items.push({
				colspan : 1,
				name : 'examPassword',
				fieldLabel : '考生密码',
				xtype : 'hidden',
				readOnly : true
			});
			formConfig.items.push({
				colspan : 1,
				name : 'idNumber',
				fieldLabel : '身份证号',
				allowBlank : false,
				xtype : 'textfield',
				validator : function(thisText){
					if(!!thisText){ //不为空
						if(!me.IdCardValidate(thisText)){
							return '身份证格式错误';
						}else{
							//return true;
							var examPublicIdTerm = me.form.getForm().findField('examPublicId').getValue();
							var userIdTerm = me.form.getForm().findField('userId').getValue();
							Ext.Ajax.request({
								url : 'base/exampublicuser/isRegisterExamForExamPublicUserListAction!isRegisterExam.action',
								method : 'post',
								params : {
									examPublicIdTerm : examPublicIdTerm,
									idNumberTerm : thisText,
									userIdTerm : userIdTerm
								},
								success:function(response){
									var re = Ext.decode(response.responseText);
									if(re['countResult']=='0'){
										ReturnValue(true);
									}else{
										ReturnValue('已使用此身份证报名该此考试');
									}
								},
								failure:function(){
									Ext.Msg.alert("信息","未能与服务器取得通讯");
								}
							});
						}
					}
					if(IsExsitFlag){
						IsExsitFlag = false;
						IsExsit = true;
					}
					function ReturnValue(ok){
						   IsExsit = ok;
					}
					return IsExsit;
				},
				readOnly : readOnly
			});
			formConfig.items.push({
				colspan : 1,
				name : 'score',
				fieldLabel : '总得分',
				xtype : 'numberfield',
				readOnly : true
			});
			
			formConfig.items.push({
				colspan : 1,
				fieldLabel : '状态',
				xtype : 'radiogroup',
				items:[
				       {
				    	   boxLabel:'未审核',
				    	   name : 'state',
				    	   inputValue : '10',
				    	   checked :true
				       },
				       {
				    	   boxLabel:'审核通过',
				    	   name : 'state',
				    	   inputValue : '20'
				       }],
				 width:240,
				 hidden : true,
				 readOnly : readOnly
			});
			formConfig.items.push({
				colspan : 2,
				fieldLabel : '参加报名',
				xtype : 'radiogroup',
				items:[
				       {
				    	   boxLabel:'是',
				    	   name : 'isDel',
				    	   inputValue : '0',
				    	   checked :true
				       },
				       {
				    	   boxLabel:'否',
				    	   name : 'isDel',
				    	   inputValue : '1'
				       }],
				 width:240,
				 readOnly : readOnly
			});
			formConfig.items.push({
				colspan : 2,
				name : 'remark',
				fieldLabel : '备注',
				xtype : 'textarea',
				readOnly : readOnly,
				width : 537
			});
			
			var editTable = Ext.widget('editlist',{
				colspan : 2,
				fieldLabel : '登录信息',
				name : 'pwdList',
				xtype : 'editlist',
				enableMoveButton:false,
				viewConfig:{
					height:150,
					autoScroll:true,
					enableTextSelection: 'true'  //本文是否允许选中
				},
				columns : [{
							width : 1,
							header : '考试科目',
							name : 'exam',
							renderer:function(value){
								return value.examName;
							}
						   },
				           {
							width : 1,
							header : '准考证号',
							name : 'inticket'
						   },{
								width : 1,
								header : '登录密码',
								name : 'examPassword'
						  }]
			});
		    
		    formConfig.items.push(editTable);
			
			me.form = ClassCreate('modules.hnjtamis.base.exampublicuser.ExamPublicUserForm', formConfig);
			me.form.parentWindow = this;
			// 表单窗口
			var formWin = new WindowObject({
						layout : 'fit',
						title : '报名',
						width : 600,
						border : false,
						frame : false,
						modal : true,// 模态
						closeAction : 'hide',
						items : [me.form]
					});	
			formWin.show();
			me.form.setFormData(id,function(result){
				
			});
		};
		this.callParent();
	},
	IdCardValidate : function(idCard){
		var me = this;
		idCard = me.trim(idCard.replace(/ /g, ""));               //去掉字符串头尾空格                     
	    if (idCard.length == 15) {   
	        return me.isValidityBrithBy15IdCard(idCard);       //进行15位身份证的验证    
	    } else if (idCard.length == 18) {   
	        var a_idCard = idCard.split("");                // 得到身份证数组   
	        if(me.isValidityBrithBy18IdCard(idCard)&&me.isTrueValidateCodeBy18IdCard(a_idCard)){   //进行18位身份证的基本验证和第18位的验证
	            return true;   
	        }else {   
	            return false;   
	        }   
	    } else {   
	        return false;   
	    }   
	},
	/**  
	 * 判断身份证号码为18位时最后的验证位是否正确  
	 * @param a_idCard 身份证号码数组  
	 * @return  
	 */  
	isTrueValidateCodeBy18IdCard : function(a_idCard){
		var me = this;
		var sum = 0;                             // 声明加权求和变量   
	    if (a_idCard[17].toLowerCase() == 'x') {   
	        a_idCard[17] = 10;                    // 将最后位为x的验证码替换为10方便后续操作   
	    }   
	    for ( var i = 0; i < 17; i++) {   
	        sum += me.Wi[i] * a_idCard[i];            // 加权求和   
	    }   
	    var valCodePosition = sum % 11;                // 得到验证码所位置   
	    if (a_idCard[17] == me.ValideCode[valCodePosition]) {   
	        return true;   
	    } else {   
	        return false;   
	    }   
	},
	/**  
	  * 验证18位数身份证号码中的生日是否是有效生日  
	  * @param idCard 18位书身份证字符串  
	  * @return  
	  */ 
	isValidityBrithBy18IdCard : function(idCard18){
		var me = this;
		var year =  idCard18.substring(6,10);   
	    var month = idCard18.substring(10,12);   
	    var day = idCard18.substring(12,14);   
	    var temp_date = new Date(year,parseFloat(month)-1,parseFloat(day));   
	    // 这里用getFullYear()获取年份，避免千年虫问题   
	    if(temp_date.getFullYear()!=parseFloat(year)   
	          ||temp_date.getMonth()!=parseFloat(month)-1   
	          ||temp_date.getDate()!=parseFloat(day)){   
	            return false;   
	    }else{   
	        return true;   
	    }   
	},
	/**  
	   * 验证15位数身份证号码中的生日是否是有效生日  
	   * @param idCard15 15位书身份证字符串  
	   * @return  
	   */  
	isValidityBrithBy15IdCard : function(idCard15){
		var me = this;
		var year =  idCard15.substring(6,8);   
	      var month = idCard15.substring(8,10);   
	      var day = idCard15.substring(10,12);   
	      var temp_date = new Date(year,parseFloat(month)-1,parseFloat(day));   
	      // 对于老身份证中的你年龄则不需考虑千年虫问题而使用getYear()方法   
	      if(temp_date.getYear()!=parseFloat(year)   
	              ||temp_date.getMonth()!=parseFloat(month)-1   
	              ||temp_date.getDate()!=parseFloat(day)){   
	                return false;   
	        }else{   
	            return true;   
	      }   
	},
	trim:function(str){
		return str.replace(/(^\s*)|(\s*$)/g, "");
	}
});