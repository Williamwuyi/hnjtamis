/**
 * 考生报名管理的模块类
 */
ClassDefine('modules.hnjtamis.base.examreview.CheckImportScoreForSysOut', {
	extend : 'base.model.List',
	initComponent : function() {
		var me = this;
		me.tip = Ext.create('Ext.tip.ToolTip', {
		    title : '导入信息',
		    closable :true,
		    autoHide : false,
		    autoScroll : true,
		    maxHeight : 300,
		    width : 650,
		    html: ''
		});
		this.columnLines = true;
		this.listeners = {
            	select:function(obj,record,index,eOpts){
            		//if(record['childNodes'].length>0){
            			//me.importBtn.setDisabled(true);
            			//me.exportBtn.setDisabled(true);
            		//}else{
            			me.importBtn.setDisabled(false);
            			me.exportBtn.setDisabled(false);
            		//}
            		var needReviewCount = record.data['needReviewCount'];//阅卷人
            		if(needReviewCount=='' || needReviewCount== null || needReviewCount==0){
            			me.sysOutBt.setDisabled(true);
            		}else{
            			me.sysOutBt.setDisabled(false);
            		}
          /*
            		var allowReview = record.data['allowReview'];//阅卷人
            		var reviewState = record.data['state'];//状态
            		var needReviewCount = record.data['needReviewCount'];
            		if(me.isNotNull(reviewState) && allowReview && reviewState=='1' && needReviewCount!='0'){
            			me.updateBt.setDisabled(false);
            			me.multiReview.setDisabled(false);
            			me.clearReviewing.setDisabled(false);
            		}else{
            			me.updateBt.setDisabled(true);
            			me.multiReview.setDisabled(true);
            			me.clearReviewing.setDisabled(true);
            		}
           */
            	}
        };
		this.columns = [
		    {name:'examId',width:0},
		    {name:'currentTime',width:0},
		    {name:'allowReview',width:0},
			{header:'考试名称',name:'examName',width:8,sortable:false,menuDisabled:true},
			{header:'考试开始时间',name:'examStartTime',width:3,sortable:false,menuDisabled:true,type : 'date', format:'Y-m-d H:i:s'},
			{header:'考试结束时间',name:'examEndTime',width:3,sortable:false,menuDisabled:true,type : 'date', format:'Y-m-d H:i:s'},	
			/*{header:'阅卷开始时间',name:'reviewStartTime',width:3,sortable:false,menuDisabled:true},	
			{header:'阅卷结束时间',name:'reviewEndTime',width:3,sortable:false,menuDisabled:true},	*/
			{header:'待阅复核题数',name:'needReviewCount',width:3,sortable:false,menuDisabled:true}
		]; 
		
		this.showTermSize = 1;//设定查询条件出现的个数
		this.terms = [{
			xtype : 'textfield',
			name : 'examNameTerm',
			fieldLabel : '考试名称',
			labelWidth : 85,
			width:290
		},{
			xtype : 'textfield',
			name : 'testpaperNameTerm',
			fieldLabel : '试卷名称',
			labelWidth : 85,
			width:280
		}];
		this.keyColumnName = "examId";// 主健属性名
		this.otherOperaters = [];//其它扩展按钮操作
		
		me.sysOutBt = Ext.create("Ext.Button", {
			iconCls : 'update',
			text : '系统外得分复核',
			resourceCode:me.updateResourceCode,
			handler : function() {
				var selected = me.getSelectionModel().selected;
				if (selected.getCount() == 0) {
					Ext.Msg.alert('提示', '请选择记录！');
					return;
				}
				var id = "";
				var record = null;
				for (var i = 0; i < selected.getCount(); i++) {
					record = selected.get(i);
					id = record.get(me.keyColumnName);
				}
				var state = record.get("state");
				var url = 'base/examreview/showSysOutScoreViewForExamReviewListAction!showSysOutScoreView.action?examId='+id;
				var t = Ext.create('Ext.window.Window', {
					id:'mytest',
				    title: '系统外得分复核',
				    height: 600,
				    width: 600,
				    layout: 'fit',
				    modal:true,
				    html:"<iframe src='"+url+"' width='100%' height='100%' frameborder=0 scrolling=auto></iframe>",
				    closeAction : 'destroy'
				});
				t.show();
				t.on("close",function(){
					me.termQueryFun();
				});
			}
		});
		this.otherOperaters.push(me.sysOutBt);
		
		me.exportBtn = Ext.create("Ext.Button",{
            xtype : 'button',
			icon : 'resources/icons/fam/export.gif',
			text : '系统外得分模版导出',
			handler : function() {
				var selected = me.getSelectionModel().selected;
				if (selected.getCount() == 0) {
					Ext.Msg.alert('提示', '请选择一门考试科目');
					return;
				}
				var id = "";
				var record = null;
				for (var i = 0; i < selected.getCount(); i++) {
					record = selected.get(i);
					id = record.get(me.keyColumnName);
				}
				window.location = 'base/examreview/exportXlsForExamReviewListAction!exportXls.action?examId='+id;
			}
		});
		this.otherOperaters.push(me.exportBtn);
		
		me.importBtn = Ext.create("Ext.Button",{
            xtype : 'button',
			icon : 'resources/icons/fam/import.gif',
			text : '统外得分导入',
			handler : function() {
				var selected = me.getSelectionModel().selected;
				if (selected.getCount() == 0) {
					Ext.Msg.alert('提示', '请选择一门考试科目');
					return;
				}
				var id = "";
				var record = null;
				for (var i = 0; i < selected.getCount(); i++) {
					record = selected.get(i);
					id = record.get(me.keyColumnName);
				}
				
				var importForm = new Ext.FormPanel({
			        frame:false,
			        fileUpload : true,//指定带文件上传
			        bodyStyle:'padding:10px 5px 0', 
			        defaultType:'textfield',
			        defaults: {
			           labelWidth:90,
			           labelAlign :'right'
			        },
			        monitorValid:true,
			        items:[{ 
			                fieldLabel:'导入XLS文件', 
			                xtype:'fileuploadfield',
			                buttonText:'浏览',
			                name:'xls',
			                emptyText:'请选择EXCEL文件',
			                colspan : 2,
			                width : 300,
			                allowBlank:false
			            }],
			        buttons:['->',{ 
			                text:'确定',
			                formBind: true, 
			                handler:function(){ 
			                    importForm.getForm().submit({ 
			                        method:'POST', 
			                        waitTitle:'导入提示', 
			                        waitMsg:'正在导入,请稍候...',
			                        url:'base/examreview/importXlsForExamReviewListAction!importXls.action?examId='+id,
			                        success:function(form, action){
			                        	var resultInfo = action.result['resultInfo'];
			                        	var myContentHtml = "";
			                        	for(var i=0;i<resultInfo.length;i++){
			                        		myContentHtml += "<div class='conTentDiv' >"+resultInfo[i]+"</div>"
			                        	}
			                        	var tmpPosition = importWin.getPosition();
			                        	importWin.hide();
		                                delete importWin;
		                                me.termQueryFun();
		                                   
			                        	me.tip.update(myContentHtml);
			                        	me.tip.showAt(tmpPosition,true);
			                        },
			                        failure:function(form, action){
			                            if(action.failureType == 'server'){
			                                var obj = Ext.decode(action.response.responseText);
			                                if(Ext.isArray(obj)) obj = obj[0];
			                                Ext.Msg.alert('导入失败', obj.errors,function(){
			                                        form.findField('xls').focus();
			                                }); 
			                            }else{ 
			                                Ext.Msg.alert('警告', '网络出现问题！'); 
			                            }                            
			                        } 
			                    }); 
			                } 
			            }]
			    });
				var importWin = new WindowObject({
							layout : 'form',
							title : '导入系统外得分',
							autoHeight : true,
							width : 400,
							border : false,
							frame : false,
							modal : true,//模态
							closeAction : 'hide',
							items : [importForm]
						});
				importWin.show();
			}
		});
		this.otherOperaters.push(me.importBtn);
		this.jsonParemeterName = 'examReviewList';
		//this.readerRoot = 'examReviewList';
		this.listUrl = "exam/exampaper/checkImportSysScoreListForExampaperListAction!checkImportSysScoreList.action?isAdmin=true";// 列表请求地址
		this.deleteUrl = "exam/exampaper/deleteForExampaperListAction!delete.action";// 删除请求地址
		//this.childColumnName = 'examReviewList';// 子集合的属性名
		this.callParent();
		//me.expandAll();
	},
	encode:function(strIn) {
	    var intLen = strIn.length;
	    var strOut = "";
	    var strTemp;
	    for (var i = 0; i < intLen; i++) {
	        strTemp = strIn.charCodeAt(i);
	        if (strTemp > 255) {
	            tmp = strTemp.toString(16);
	            for (var j = tmp.length; j < 4; j++) tmp = "0" + tmp;
	            strOut = strOut + "^" + tmp;
	        } else {
	            if (strTemp < 48 || (strTemp > 57 && strTemp < 65) || (strTemp > 90 && strTemp < 97) || strTemp > 122) {
	                tmp = strTemp.toString(16);
	                for (var j = tmp.length; j < 2; j++) tmp = "0" + tmp;
	                strOut = strOut + "~" + tmp;
	            } else {
	                strOut = strOut + strIn.charAt(i);
	            }
	        }
	    }
	    return (strOut);
	},
	isNotNull:function(value){
		var me = this;
		if(value!=null && me.trim(value)!=''){
			return true;
		}else{
			return false;
		}
	},
	trim:function(str){
	    str = str.replace(/^(\s|\u00A0)+/,'');
	    for(var i=str.length-1; i>=0; i--){
	        if(/\S/.test(str.charAt(i))){
	            str = str.substring(0, i+1);
	            break;
	        }
	    }
	    return str;
	}
});