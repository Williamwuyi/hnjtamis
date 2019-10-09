/**
 * 成绩查询
 */
ClassDefine('modules.hnjtamis.exampaper.ExamScoreView', {
	extend : 'modules.hnjtamis.base.examScoreQuery.myList',
	initComponent : function() {
		var me = this;
		// 模块列表对象
		/*this.listeners = {
            	select:function(obj,record,index,eOpts){
            		
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
					width : 0
				}, {
					name : 'userName',
					header : '考生姓名',
					align:"center",
					width : 1.5
				}, {
					name : 'firstScore',	
					header : '总得分',
					align:"center",
					width : 1
				}, {
					name : 'danxuan',	
					header : '单选题',
					align:"center",
					width : 1
				}, {
					name : 'duoxuan',	
					header : '多选题',
					align:"center",
					width : 1
				}, {
					name : 'tiankong',	
					header : '填空题',
					align:"center",
					width : 1
				}, {
					name : 'panduan',	
					header : '判断题',
					align:"center",
					width : 1
				}, {
					name : 'wenda',	
					header : '问答题',
					align:"center",
					width : 1
				}, {
					name : 'shiting',	
					header : '视听题',
					align:"center",
					width : 1
				},   {
					name : 'lst',	
					header : '论述题',
					align:"center",
					width : 1
				},  {
					name : 'jst',	
					header : '计算题',
					align:"center",
					width : 1
				},  {
					name : 'htt',	
					header : '画图题',
					align:"center",
					width : 1
				},{
					name : 'qita',	
					header : '其它',
					align:"center",
					width : 1
				}, {
					name : 'keguan',	
					header : '客观题',
					align:"center",
					width : 1
				}, {
					name : 'zhuguan',	
					header : '主观题',
					align:"center",
					width : 1
				}, {
					name : 'xtw',	
					header : '系统外',
					align:"center",
					width : 1
				}];
		// 模块查询条件对象
		this.terms = [{
			xtype : 'textfield',
			name:'examName',
			fieldLabel:'考试名称',
			value:me.examName,
			readOnly:true,
			labelWidth:90
		}];
		this.keyColumnName = "testPaperId";// 主健属性名
		this.viewOperater = false;
		this.addOperater = false;
		this.deleteOperater = false;
		this.updateOperater = false;
		this.otherOperaters = [];//其它扩展按钮操作
		this.otherOperaters.push("");
		this.listUrl = "base/scorequery/listForExamScoreQueryListAction!list.action?vt=all&examId="+me.examId;// 列表请求地址
		this.deleteUrl = "";// 删除请求地址
		this.enableExportXls = false;
		this.otherOperaters = [];//其它扩展按钮操作
		this.otherOperaters.push(Ext.create("Ext.Button", {
			iconCls : 'view',
			text : eap_operate_view,
			sortIndex : 100,
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
				if(record)me.viewTestPaper(record);
			}
		}));
		this.otherOperaters.push({
			xtype : 'button',//按钮类型
			iconCls : 'exportXls',
			text : "成绩导出",//按钮标题
			handler : function() {//按钮事件
				if(me.examId!=undefined && me.examId!=null && me.examId!=''){
					window.location = 'base/scorequery/exportXlsForExamScoreQueryExcelAction!exportXls.action?examId='+me.examId;
				}else{
					Ext.Msg.alert('提示', '请选择考试！');
				}
			}
		
		});
		this.enableCheck = false;
		this.callParent();
	},
	viewTestPaper:function(record){
		var testPaperId = record.data['testPaperId'];
        //显示试卷详细情况 
        var url = 'base/scorequery/showExamPaperDetailForExamScoreQueryListAction!showExamPaperDetail.action?testPaperId='+testPaperId;
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
	}
});