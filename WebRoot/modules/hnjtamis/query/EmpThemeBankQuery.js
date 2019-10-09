/**
 * 试题
 */
ClassDefine('modules.hnjtamis.query.EmpThemeBankQuery', {
	extend : 'base.model.List',
	initComponent : function() {
		//Ext.QuickTips.init();
		String.prototype.trim = function()
		{
			return this.replace( /(^\s*)|(\s*$)/g, '' ) ;
		};
		
		var me = this;
		me.op =  me.op || 'd';
		this.columns = [
		    {name:'employeeId',width:0},
		    {name:'deptId',width:0},
		    {header:'部门名称',name:'deptName',width:4,sortable:false,menuDisabled:true,
			    renderer:function(value, metadata, record){ 
				    if(value!=undefined && value!=null && value!=""){
				    	metadata.tdAttr = " data-qtip = '"+value+"'";
				    }
				    metadata.style="white-space: normal !important;";
				    return value; 
				},titleAlign:"center"
		    },
		    {header:'姓名',name:'employeeName',width:2,sortable:false,menuDisabled:true,
			    renderer:function(value, metadata, record){ 
				    if(value!=undefined && value!=null && value!=""){
				    	metadata.tdAttr = " data-qtip = '"+value+"'";
				    }
				    metadata.style="white-space: normal !important;";
				    return value; 
				},align:"center"
		    },
			
			{header:'题库数目',name:'themebanknum',width:2,sortable:false,menuDisabled:true,align:"right",titleAlign:"center"},
			/*{header:'已学习题库',name:'xxthemebanknum',width:2,sortable:false,menuDisabled:true,align:"right",titleAlign:"center"},*/
			{header:'未学习题库',name:'wxxthemebanknum',width:2,sortable:false,menuDisabled:true,align:"right",titleAlign:"center"},
			{header:'在学习题库',name:'yxxthemebanknum',width:2,sortable:false,menuDisabled:true,align:"right",titleAlign:"center"},
			{header:'已学完题库',name:'yxwthemebanknum',width:2,sortable:false,menuDisabled:true,align:"right",titleAlign:"center"},
			{header:'题库',name:'themeBankName',width:5,sortable:false,menuDisabled:true,
				renderer:function(value, metadata, record){
					if(value == undefined || value==null || value=='' || value=='null'){
						return '<font color=red>未安排题库<red>';
					}else{
						if(value!=undefined && value!=null && value!=""){
					    	metadata.tdAttr = " data-qtip = '"+value+"'";
					    }
					    metadata.style="white-space: normal !important;";
					    return value; 
					}
				},titleAlign:"center"
			},
			{header:'总试题数',name:'themeNum',width:2,sortable:false,menuDisabled:true,align:"right",titleAlign:"center"},
			{header:'已学习题数',name:'themeFinNum',width:2,sortable:false,menuDisabled:true,align:"right",titleAlign:"center"},
			{header:'学习比率',name:'themeFinNumBL',width:2,sortable:false,menuDisabled:true,align:"right",titleAlign:"center",
			renderer:function(value, metadata, record){
				 	 var themeNum = Number(record.get("themeNum"));
					 var themeFinNum = Number(record.get("themeFinNum")); 
					 if(themeNum>0){
					    return (themeFinNum/themeNum*100.0).toFixed(2)+"%"; 
					 }else{
					    return "0.00%"; 
					 }
			}}
		];
		this.showTermSize = 1;//设定查询条件出现的个数
		this.terms = [{
						xtype : 'textfield',
						name : 'employeeTerm',
						fieldLabel : '姓名',
						labelWidth : 90,
						width:260
					}
			];
		if(me.op == 'o' || me.op == 'xxog'){
			me.deptTermObj = Ext.widget('selecttree',{
					name : 'deptTerm',
					fieldLabel : '机构部门',
					addPickerWidth:200,
					width:260,
					labelWidth : 90,
					xtype : 'selecttree',
					nameKey : 'deptId',
					nameLable : 'deptName',
					readerRoot : 'children',
					keyColumnName : 'id',
					titleColumnName : 'title',
					childColumnName : 'children',
					editorType:'str',//编辑类型为字符串，不是对象
					//selectType : 'dept',
					//selectTypeName : '部门',
					selectUrl : "organization/dept/ownerOrganTreeForDeptListAction!ownerOrganTree.action?organTerm="+base.Login.userSession.currentOrganId,
					nameTermWidth : 250,
					selectEventFun:function(combo){
						
					}
				});
			this.terms.push(me.deptTermObj);
		}
		
		me.learnTerm = Ext.widget('select',{
			xtype :'select',
			fieldLabel : '学习情况',
			name : 'learnTerm',
			data:[['all','全部'],['unLearn','未学习'],['exLearn','已学习'],['finLearn','已学完']],
			labelWidth : 90,
			width:260,
			defaultValue:'all',
			selectEventFun:function(combo,record,index){
			}
		});
		this.terms.push(me.learnTerm);
		this.keyColumnName = "employeeId";// 主健属性名
		this.jsonParemeterName = "list";
		//this.pageRecordSize = 10;
		this.viewOperater = false;
		this.addOperater = false;
		this.updateOperater = false;
		this.deleteOperater = false;
		this.enableExportXls = true;
		this.otherOperaters = [];//其它扩展按钮操作
		
		me.qtitleLabel = Ext.create("Ext.form.Label",{
			text: '',
			style : 'padding-left:25px'
		});
		this.otherOperaters.push(me.qtitleLabel);
		var qbtn = Ext.create("Ext.Button", {
			xtype : 'button',//按钮类型
			icon : 'resources/icons/fam/table_refresh.png',//按钮图标
			text : me.op=='xxog' || me.op == 'xxdp' ? '切换到明细查询' : '切换到汇总查询',//按钮标题
			handler : function() {
				me.termFormWin.close();
				if(me.op == 'xxog'){
					me.op = 'o';
					qbtn.setText('切换到汇总查询');
				}else if(me.op == 'o'){
					me.op = 'xxog';
					qbtn.setText('切换到明细查询');
				}else if(me.op == 'xxdp'){
					me.op = 'd';
					qbtn.setText('切换到汇总查询');
				}else{
					me.op = 'xxdp'
					qbtn.setText('切换到明细查询');
				}
				me.columnsOpt();
				me.store.proxy.url = "query/empThemeBank/listForEmployeeThemeBankListAction!list.action?op="+me.op;
				
				me.termQueryFun(true,'first');	
				me.getView().refresh();
				
			}
		})
		this.otherOperaters.push(qbtn);
		this.otherOperaters.push({
			xtype : 'button',//按钮类型
			iconCls : 'exportXls',
			text : "导出Excel",//按钮标题
			handler : function() {//按钮事件
				window.open("query/empThemeBank/toExcelForEmployeeThemeBankExcelAction!toExcel.action?op="+me.op+"&random="+Math.random());
			}
		
		});
		this.listUrl = "query/empThemeBank/listForEmployeeThemeBankListAction!list.action?op="+me.op;// 列表请求地址
		this.deleteUrl = "";// 删除请求地址
		this.callParent();
		var isinit = true;
		me.store.proxy.timeout = 120000;
		me.store.on("load",function(){
			if(isinit){
				isinit = false;
			}
			me.columnsOpt();
			me.queryEndTime();
		})
	},
	columnsOpt:function(){
		var me = this;
		var learnTerm = me.learnTerm.getValue();
		//console.log(learnTerm)
		for (var i = 0; i < me.columns.length; i++) {
					if(me.columns[i].name == 'themebanknum' 
						|| me.columns[i].name == 'xxthemebanknum'){
						if(me.op == 'xxog' || me.op == 'xxdp'){
							me.columns[i].show();
						}else{
							me.columns[i].hide();
						}
					}
					if(me.columns[i].name == 'wxxthemebanknum'
						|| me.columns[i].name == 'yxxthemebanknum'
						|| me.columns[i].name == 'yxwthemebanknum'){
						if(me.op == 'xxog' || me.op == 'xxdp'){
							if(learnTerm == 'all'){
								me.columns[i].show();
							}else if(learnTerm == 'unLearn'  && me.columns[i].name == 'wxxthemebanknum'){
								me.columns[i].show();
							}else if(learnTerm == 'exLearn'){
								me.columns[i].show();
							}else if(learnTerm == 'finLearn' && me.columns[i].name == 'yxwthemebanknum'){
								me.columns[i].show();
							}else{
								me.columns[i].hide();
							}
						}else{
							me.columns[i].hide();
						}
					}
					if(me.columns[i].name == 'themeBankName'
					//|| me.columns[i].name == 'themeNum' 
					//|| me.columns[i].name == 'themeFinNum' 
					|| me.columns[i].name == 'themeFinNumBL'){
						if(me.op == 'o' || me.op == 'd'){
							me.columns[i].show();
						}else{
							me.columns[i].hide();
						}
					}
				}
	
	},
	queryEndTime : function(){
		var me = this;
			Ext.Ajax.request({
			timeout: 10000,
			url : "query/empThemeBank/queryTjEndTimeForEmployeeThemeBankListAction!queryTjEndTime.action?op="+me.op,
			params : {
				
			},
			success: function(response) {
				var result = Ext.decode(response.responseText);
				if(result && result.employeeBankCreateTime){
					me.qtitleLabel.setText('最后统计时间：'+result.employeeBankCreateTime);
				}else{
					me.qtitleLabel.setText('');
				}
			},
			failure : function() {
				me.qtitleLabel.setText('');
			}
		});
		
		
	}
});