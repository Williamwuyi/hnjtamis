/**
 * 成绩查询
 */
ClassDefine('modules.hnjtamis.base.examScoreQuery.ExamSelfScoreQuery', {
	extend : 'base.model.List',
	initComponent : function() {
		var me = this;
		// 模块列表对象
		/*this.listeners = {
            	select:function(obj,record,index,eOpts){
            		var testPaperId = record.data['testPaperId'];
            		//显示试卷详细情况 
            		var url = 'base/scorequery/showExamPaperDetailForExamScoreQueryListAction!showExamPaperDetail.action?testPaperId='+testPaperId;
            		var t = Ext.create('Ext.window.Window',{
            			title:'试卷考题详情',
            			height:600,
            			width:600,
            			layout:'fit',
            			modal:true,
            			html:"<iframe src='"+url+"' width='100%' height='100%' frameborder=0 scrolling=auto></iframe>",
            			closeAction:'destroy'
            		});
            		t.show();
            	}
        };*/
		this.columns = [{
					name : 'testPaperId',
					width : 0
				},{
					name : 'typeList',
					width : 0
				}, {
					name : 'examName',
					header : '考试名称',
					align:"center",
					width : 1,
					renderer:function(value, metadata, record){
						return "<a href='javascript:this.showExamTestPaper()'>"+value+"</a>";
					}
				}, {
					name : 'userName',
					header : '考生姓名',
					align:"center",
					width : 1
				}, {
					name : 'firstScore',	
					header : '总得分',
					align:"center",
					width : 1,
					renderer:function(value){
						if(value==-1){
							return '<font color="blue">---</font>';
						}else{
							return value
						}
					}
				}, {
					name : 'danxuan',	
					header : '单选题',
					align:"center",
					width : 1,
					renderer:function(value){
						if(value==-1){
							return '<font color="blue">---</font>';
						}else{
							return value
						}
					}
				}, {
					name : 'duoxuan',	
					header : '多选题',
					align:"center",
					width : 1,
					renderer:function(value){
						if(value==-1){
							return '<font color="blue">---</font>';
						}else{
							return value
						}
					}
				}, {
					name : 'tiankong',	
					header : '填空题',
					align:"center",
					width : 1,
					renderer:function(value){
						if(value==-1){
							return '<font color="blue">---</font>';
						}else{
							return value
						}
					}
				}, {
					name : 'panduan',	
					header : '判断题',
					align:"center",
					width : 1,
					renderer:function(value){
						if(value==-1){
							return '<font color="blue">---</font>';
						}else{
							return value
						}
					}
				}, {
					name : 'wenda',	
					header : '问答题',
					align:"center",
					width : 1,
					renderer:function(value){
						if(value==-1){
							return '<font color="blue">---</font>';
						}else{
							return value
						}
					}
				}, {
					name : 'shiting',	
					header : '视听题',
					align:"center",
					width : 1,
					renderer:function(value){
						if(value==-1){
							return '<font color="blue">---</font>';
						}else{
							return value
						}
					}
				},  {
					name : 'lst',	
					header : '论述题',
					align:"center",
					width : 1,
					renderer:function(value){
						if(value==-1){
							return '<font color="blue">---</font>';
						}else{
							return value
						}
					}
				},  {
					name : 'jst',	
					header : '计算题',
					align:"center",
					width : 1,
					renderer:function(value){
						if(value==-1){
							return '<font color="blue">---</font>';
						}else{
							return value
						}
					}
				},  {
					name : 'htt',	
					header : '画图题',
					align:"center",
					width : 1,
					renderer:function(value){
						if(value==-1){
							return '<font color="blue">---</font>';
						}else{
							return value
						}
					}
				}, /*{
					name : 'qita',	
					header : '其它',
					align:"center",
					width : 1,
					renderer:function(value){
						if(value==-1){
							return '<font color="blue">---</font>';
						}else{
							return value
						}
					}
				}, */{
					name : 'keguan',	
					header : '客观题',
					align:"center",
					width : 1,
					renderer:function(value){
						if(value==-1){
							return '<font color="blue">---</font>';
						}else{
							return value
						}
					}
				}, {
					name : 'zhuguan',	
					header : '主观题',
					align:"center",
					width : 1,
					renderer:function(value){
						if(value==-1){
							return '<font color="blue">---</font>';
						}else{
							return value
						}
					}
				}, {
					name : 'xtw',	
					header : '系统外',
					align:"center",
					width : 1,
					renderer:function(value){
						if(value==-1){
							return '<font color="blue">---</font>';
						}else{
							return value
						}
					}
				}];
				
		showExamTestPaper =  function(){
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
					var url = 'base/scorequery/showExamPaperDetailForExamScoreQueryListAction!showExamPaperDetail.action?testPaperId='+id;
            		var t = Ext.create('Ext.window.Window',{
            			title:'试卷考题详情',
            			height:600,
            			width:800,
            			layout:'fit',
            			modal:true,
            			html:"<iframe src='"+url+"' width='100%' height='100%' frameborder=0 scrolling=auto></iframe>",
            			closeAction:'destroy'
            		});
            		t.show();
		};
		// 模块查询条件对象
		this.terms = [{
					xtype : 'selecttree',
					name:'examId',
					nameKey : 'examId',
					nameLable : 'examName',
					readerRoot : 'examList',
					keyColumnName : 'examId',
					titleColumnName : 'examName',
					childColumnName : 'examList',
					editorType:'str',
					selectUrl : 'exam/exampaper/empIdlistForExampaperListAction!empIdlist.action',
					width:400,
					selectEventFun:function(combo){
						me.examId = combo.value;
					}
				}];
		this.keyColumnName = "testPaperId";// 主健属性名
		this.viewOperater = false;
		this.addOperater = false;
		this.deleteOperater = false;
		this.updateOperater = false;
		this.enableExportXls = false;
		this.otherOperaters = [];//其它扩展按钮操作
		this.otherOperaters.push("");
		this.listUrl = "base/scorequery/singleListForExamScoreQueryListAction!singleList.action";// 列表请求地址
		this.deleteUrl = "";// 删除请求地址
		
		this.otherOperaters = [];//其它扩展按钮操作
		this.otherOperaters.push({
			xtype : 'button',//按钮类型
			iconCls : 'exportXls',
			text : "成绩导出",//按钮标题
			handler : function() {//按钮事件
				if(me.examId!=undefined && me.examId!=null && me.examId!=''){
					window.location = 'base/scorequery/exportSingleXlsForExamScoreQueryExcelAction!exportSingleXls.action?examId='+me.examId;
				}else{
					window.location = 'base/scorequery/exportSingleXlsForExamScoreQueryExcelAction!exportSingleXls.action';
				}
			}
		
		});
		this.callParent();
	}
});