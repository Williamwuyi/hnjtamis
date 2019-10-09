/**
 * 下拉树控件
 */
Ext.define('modules.hnjtamis.common.employeeTree.employeeFormPanel', {
    extend : 'Ext.form.FormPanel',
	border : false,
	frame : false,
	bodyStyle : "border:0;padding:0px 0px 0px 0px;overflow-x:hidden; overflow-y:auto",
	defaultType : 'textfield',
	margins : '0 0 0 0',
	colspan : 2,
	height:200,
	forceFit : true,// 自动填充列宽,根据宽度比例
	allowMaxSize : -1,// 允许最大行数,-1为不受限制
	name : 'employeeFormPanel',
	initComponent : function() {
		var me = this;
		me.alertMask = new Ext.LoadMask(me, {  
		    msg     : '数据加载中,请稍候',  
		    removeMask  : true// 完成后移除  
		}); 
		me.Wi = [ 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2, 1 ];    // 加权因子   
		me.ValideCode = [ 1, 0, 10, 9, 8, 7, 6, 5, 4, 3, 2 ];            // 身份证验证位值.10代表X  
		//Ext.QuickTips.init();
		String.prototype.trim = function()
		{
			return this.replace( /(^\s*)|(\s*$)/g, '' ) ;
		};
		
		
		me.height = me.height || 200,
		me.layout = {
			type : 'table',
			columns : me.columnSize || 1,
			tableAttrs : {
				style : {
					width : '100%'
				}
			}
		};
		me.items= [];
		me.items.push({
			xtype:'hidden',
			name:'selectEmpIds'
		});
		me.items.push(me.setEmplist());
		me.callParent();
	},
	setEmplist : function (){
		var me = this;
		me.empListOthoptBt = [];
		me.empListOthoptBt.push({
			xtype : 'button',//按钮类型
			colspan : 2,
			icon : 'resources/icons/fam/add.gif',//按钮图标
			text : '选择人员',
			handler : function() {
					var selectEmpIds = "";
					for(var i=0;i<me.empListGrid.store.getCount();i++){
						var record = me.empListGrid.store.getAt(i);
						selectEmpIds+=record.get("employeeId")+",";
					}
					me.getForm().findField("selectEmpIds").setValue(selectEmpIds);
					me.selectform = Ext.create('modules.hnjtamis.common.employeeTree.pxUserSelectTree',{
						"selectEmpIds" : selectEmpIds 
					});
					me.selectform.selectEmpIdsObj = me.getForm().findField("selectEmpIds");
					me.selectform.callBackFun = function(addList){
					    //var index= 1;
					    me.empListGrid.store.removeAll();
					    //	{"selectParentForm":me.form,"selectformWin":me.selectformWin,"selectParentGrid":me.empListGrid});
						if(addList.length>0){
							var sortIndex = 0;
							/*if(me.empListGrid.store.getCount()>0){
								var record = me.empListGrid.store.getAt(me.empListGrid.store.getCount()-1);
								sortIndex = Number(record.get("sortIndex"))+1;
							}*/
							for(var i=0;i<addList.length;i++){
								var tmpRd = addList[i];
								//tmpRd.set("sortIndex",sortIndex);
								//tmpRd.set("kcgtId",me.kcgtId);
								//tmpRd.set("kcgtmc",me.kcgtMc);
								me.empListGrid.store.insert(me.empListGrid.store.getCount(), tmpRd);
								//sortIndex++;
							}
							me.empListGrid.getView().refresh();
							Ext.Msg.alert('提示', '添加成功，当前操作未保存人员信息到数据库，请点击“保存”按钮进行数据保存！',function(){
								me.selectformWin.hide();
							});
						}else{
							me.empListGrid.getView().refresh();
							Ext.Msg.alert('提示', '没有选择添加的保存人员！',function(){
								//me.selectformWin.hide();
							});
						}
					}
					me.selectform.closeFun = function(){
						me.selectformWin.hide();
					}
					
					me.selectformWin = new WindowObject({
						layout:'fit',
						title:'选择人员',
						height:670,
						width:900,
						border:false,
						frame:false,
						modal:true,
						//autoScroll:true,
						bodyStyle:'overflow-x:auto;overflow-y:hidden;',
						closeAction:'destroy',
						items:[me.selectform]
				  });
				  me.selectformWin.show();	
			}
		});
		
		me.empListGrid =ClassCreate('base.core.EditList',{
	    	 	colspan : 2,
				fieldLabel : ' ',
				name : 'employeeList',
				xtype : 'editlist',
				viewConfig:{height:me.height - 65,autoScroll:true},//高度
				addOperater : false,
				deleteOperater : true,
				enableMoveButton : true,
				enableCheck : true,
				readOnly : false,
				fieldLabel : '考生信息',
				otherOperaters:me.empListOthoptBt,
		    	columns:[
		    		{
					 name:'employeeId',
					 hidden:true
					},{
					 header:"人员",
			    	 name : 'employeeName',
					 width : 15,
					 sortable:false,
					 menuDisabled:true,
					 align:"center",
					 renderer:function(value, metadata, record){
						 if(value!=undefined && value!=null && value!=""){
						    metadata.tdAttr = " data-qtip = '"+value+"'";
						 }
						 return value;
					 }
		    		},
		    		{
		    		 name : 'organName',
					 header : '机构',
					 align:'center',
					 width : 25 ,
					 sortable:false,
					 menuDisabled:true,
					 align:"center",
					 renderer:function(value, metadata, record){
						 if(value!=undefined && value!=null && value!=""){
						    metadata.tdAttr = " data-qtip = '"+value+"'";
						 }
						 return value;
					 }
					},
					{
					 name : 'deptName',
					 header : '部门',
					 align:'center',
					 width : 20 ,
					 sortable:false,
					 menuDisabled:true,
					 align:"center",
					 renderer:function(value, metadata, record){
						 if(value!=undefined && value!=null && value!=""){
						    metadata.tdAttr = " data-qtip = '"+value+"'";
						 }
						 return value;
					 }
					},
					{
					 header:"岗位",
					 name:"quarterName",
					 sortable:false, 
					 menuDisabled:true,
					 width:20,
					 align:"center",
				     renderer:function(value, metadata, record){
						 if(value!=undefined && value!=null && value!=""){
						    metadata.tdAttr = " data-qtip = '"+value+"'";
						 }
						 return value;
					 }
				    },
				    {
				      name : 'userSex',
					  header : '性别',
					  width : 12 ,
					  align:'center',
					  sortable:false,
					  menuDisabled:true,
					  titleAlign:"center",
					 /* editor : {
						xtype :'select',
						data:[['1','男'],['2','女']],
						//defaultValue:'10',
						allowBlank : true,
						readOnly : true
					  },*/
					  renderer:function(value){
						if(value == '1'){
							return '男';
						}else if(value == '2'){
							return '女';
						}else{
							return "";
						}
					  }
				    },
				    {
						name : 'userBirthday',
						header : '出生年月',
						width : 15 ,
						sortable:false,
					    menuDisabled:true,
					    titleAlign:"center",
					   /* editor : {
					       xtype : 'datefield',
						   format : 'Y-m-d',
						   allowBlank : true,
						   readOnly : true
					    },*/
					     renderer:function(value, metadata, record){
						   if(value!=undefined && value!=null && value!=""){
						      metadata.tdAttr = " data-qtip = '"+value+"'";
						   }
							return me.formatMyDate(value);
						}
					},
					{
						name : 'idNumber',
						header : '身份证号',
						width : 25 ,
						sortable:false,
					    menuDisabled:true,
					    titleAlign:"center",
					    /*editor : {
					    	xtype : 'textfield',
							readOnly : true,
					    	allowBlank : true,
					    	validator : function(thisText){
								if(!!thisText){ //不为空
									if(!me.IdCardValidate(thisText)){
										return '身份证格式错误';
									}else{
										return true;
										var examPublicIdTerm = me.getForm().findField('examPublicId').getValue();
										var userIdTerm = me.getForm().findField('userId').getValue();
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
								}else{
									return '请录入身份证';
								}
							}
					    },*/
					    renderer:function(value, metadata, record){
						 if(value!=undefined && value!=null && value!=""){
						    metadata.tdAttr = " data-qtip = '"+value+"'";
						 }
						 return value;
					 	}
					},
					{
						name : 'userNation',
						header : '民族',
						width : 0 ,
						sortable:false,
					    menuDisabled:true,
					    titleAlign:"center",
					    /*editor : {
					    	xtype : 'textfield',
					    	 allowBlank : true,
							 readOnly : true
					    },*/
					    renderer:function(value, metadata, record){
						   if(value!=undefined && value!=null && value!=""){
						      metadata.tdAttr = " data-qtip = '"+value+"'";
						   }
						   return value;
						 }
					},
					{
						name : 'userPhone',
						header : '联系电话',
						width : 19 ,
						sortable:false,
					    menuDisabled:true, 
					    titleAlign:"center",
					   /* editor : {
					      xtype : 'textfield',
					      allowBlank : true
					    },*/
					    renderer:function(value, metadata, record){
						   if(value!=undefined && value!=null && value!=""){
						      metadata.tdAttr = " data-qtip = '"+value+"'";
						   }
						   return value;
					    }
					},
					{
						name : 'userAddr',
						header : '住址',
						width : 0 ,
						sortable:false,
					    menuDisabled:true,
					    titleAlign:"center",
					    /*editor : {
					      xtype : 'textfield',
					      allowBlank : true
					    },*/
					    renderer:function(value, metadata, record){
						   if(value!=undefined && value!=null && value!=""){
						       metadata.tdAttr = " data-qtip = '"+value+"'";
						   }
						   return value;
					     }
					},{
						name : 'organId',
						width : 0 
					},{
						name : 'deptId',
						width : 0 
					},{
						name : 'quarterId',
						width : 0 
					}
		    	]
	    });
    	me.empListGrid.store.on("load", function() {
    		
		});
    	return me.empListGrid;
	},
	formatMyDate : function(value){//时间格式化
		if(value=='' || value == null || value==undefined)return '';
		if(Object.prototype.toString.call(value) === "[object String]" 
			&& value.indexOf("T00:00:00")!=-1){
			value =value.substring(0,10);
			return value;
		}
		Date.prototype.Format = function (fmt) { //author: meizz 
				var o = {
					"M+": this.getMonth() + 1, //月份 
					"d+": this.getDate(), //日 
					"h+": this.getHours(), //小时 
					"m+": this.getMinutes(), //分 
					"s+": this.getSeconds(), //秒 
					"q+": Math.floor((this.getMonth() + 3) / 3), //季度 
					"S": this.getMilliseconds() //毫秒 
				};
				if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
				for (var k in o)
				if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
				return fmt;
		}	
		if(value instanceof Date){
			return (new Date(value)).Format("yyyy-MM-dd");
		}else{
			return value;
		}
	},
	queryEmpList : function(){//查询已有的人员信息
		var me = this;
		
		me.alertMask.show();
		var examPublicId = me.ownerCt.getForm().findField("examPublicId").getValue();
		EapAjax.request({
			timeout: 60000,
			url : "base/exampublicuser/queryMoreEmpListForExamPublicUserFormAction!queryMoreEmpList.action",
			params : {
				id : examPublicId
			},
			success : function(response) {
				me.alertMask.hide();
				me.empListGrid.store.removeAll();
				var result = Ext.decode(response.responseText);
				if(result!=undefined && result!=null 
					&& result.employeeList!=undefined 
					&& result.employeeList!=null
					&& result.employeeList.length>0){
					var employeeList = result.employeeList;
					for(var i=0;i<employeeList.length;i++){
						var row = employeeList[i];
						row["userBirthday"] = me.formatMyDate(row["userBirthday"]);
						me.empListGrid.store.insert(i, row);
					}
				}
				me.empListGrid.getView().refresh();
				
			},
			failure : function() {
				me.alertMask.hide();
				Ext.Msg.alert('提示', '网络异常！');
			}
		});	
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