/**
 * desc :岗位培训达标系统-考试结果统计 统计前2名人员成绩、不及格人员成绩、按分数段统计
 * date :2015.4.28
 * author:wangyong
 */
Ext.define('modules.hnjtamis.statistics.ExamStatisticsTopThreeChart', {
    extend: 'Ext.Panel',
    yearArray:[],
	tbar: [],
    initComponent: function() {
        var me = this;
        var date = new Date();
		for(var i=2014;i<=date.getFullYear();i++){
			var item = new Array();
			item.push(i);item.push(i);
			this.yearArray[this.yearArray.length] = item;
		}
		/// 添加年份查询条件
		me.tbar.push({
			xtype: 'select',
	  	  	name : 'yearTerm',
			fieldLabel : '年份',
			labelAlign : 'right',
			labelWidth : 70,
			data : this.yearArray,
			defaultValue : new Date().getFullYear(),
			id:'yearTermStatics2'
		});
		/// 添加查询按钮
		me.tbar.push({
			xtype: 'button',
	  	  	iconCls : 'query', 
	  	  	text: eap_operate_query,
	  	  	handler :function(){	
	  	  		///alert(Ext.getCmp('yearTerm').getValue());
	  	  		ds.load({
	  	  			params:{
	  	  				yearTerm:Ext.getCmp('yearTermStatics2').getValue()
	  	  			}
	  	  		})
	  	  	} 
		});
		me.titleName = new Ext.form.Label({
			text: '',
			margins : '0 0 0 20'
		});
		me.tbar.push(me.titleName);
		
		//// 前三名人员成绩dataset
		var ds = Ext.create('Ext.data.ArrayStore', {
            fields: [
            	{name: 'examid' },
                {name: 'examname' },
                {name: 'firstscore'},
                {name: 'secondscore'},
                {name: 'thirdscore'}
            ],
            ///data: myData,
            proxy : {
				type : 'ajax',
				actionMethods : "POST",
				url : 'statistics/exam/topthreelistForExamStatisticsListAction!topthreelist.action',// 后台请求地址
				reader : {
					type : 'json',
					root : 'topthreelist',
					totalProperty : "total"
				}
			},
            listeners: {
                beforesort: function() {
                    //
                },
                //add listener to (re)select bar item after sorting or refreshing the dataset.
                refresh: {
                    fn: function() {
                        //
                    },
                    // Jump over the chart's refresh listener
                    delay: 1
                }
            }
        });
        /// 不及格人员数据集
        var dsfail = Ext.create('Ext.data.ArrayStore', {
            fields: [
                {name: 'examname' },
                {name: 'employeename'},
                {name: 'score'}
            ],
            ///data: myData,
            proxy : {
				type : 'ajax',
				actionMethods : "POST",
				url : 'statistics/exam/faillistForExamStatisticsListAction!faillist.action',// 后台请求地址
				reader : {
					type : 'json',
					root : 'faillist',
					totalProperty : "total"
				}
			},
            listeners: {
                beforesort: function() {
                    //
                },
                //add listener to (re)select bar item after sorting or refreshing the dataset.
                refresh: {
                    fn: function() {
                        //
                    },
                    // Jump over the chart's refresh listener
                    delay: 1
                }
            }
        });
        
        //图表数据集
        var chartStore = Ext.create('Ext.data.JsonStore', {
            fields: ['sections', 'nums'],
            proxy : {
				type : 'ajax',
				actionMethods : "POST",
				url : 'statistics/exam/sectionslistForExamStatisticsListAction!sectionslist.action',// 后台请求地址
				reader : {
					type : 'json',
					root : 'sectionslist',
					totalProperty : "total"
				}
			}
		            
        });
        
        var gridPanel = Ext.create('Ext.grid.Panel', {
	        ///id: 'examtopthreelist',
	        flex: 4,
	        title:'年内所有考试前三名成绩',
	        store: ds,
	        forceFit: true,
	        padding: '0 2 0 0',
	        width:'50%',
	        height:'100%',
	        defaults: {
	            sortable: true
	        },
	        columns: [
	            {
	                text: '考试名称',
	                width: 200,
	                dataIndex: 'examname'
	            },
	            {
	                text: '第1名',
	                dataIndex: 'firstscore'
	            },
	            {
	                text: '第2名',
	                dataIndex: 'secondscore'
	            },
	            {
	                text: '第3名',
	                dataIndex: 'thirdscore'
	            }
	        ],
	
	        listeners: {
	            selectionchange: function(model, records) {
	                var fields;
	                if (records[0]) {
	                	//alert(records[0].data.examid);
	                	dsfail.load({
			  	  			params:{
			  	  				examIdTerm:records[0].data.examid
			  	  			}
			  	  		});
			  	  		chartStore.load({
			  	  			params:{
			  	  				examIdTerm:records[0].data.examid
			  	  			}
			  	  		});
			  	  		me.titleName.setText("当前选择考试名称："+ records[0].data.examname);
	                }
	            }
	        }
	    });
	    
	    var gridfailPanel = Ext.create('Ext.grid.Panel', {
	        ///id: 'examfaillist',
	        flex: 4,
	        store: dsfail,
	        title:'每场考试不及格人员成绩',
	        forceFit: true,
	        width:'50%',
	        height:'100%',
	        defaults: {
	            sortable: true
	        },
	        columns: [
	            {
	                text: '考试名称',
	                width: 100,
	                dataIndex: 'examname'
	            },
	            {
	                text: '姓名',
	                dataIndex: 'employeename'
	            },
	            {
	                text: '得分',
	                dataIndex: 'score'
	            }
	        ],
	
	        listeners: {
	            selectionchange: function(model, records) {
	                var fields;
	                
	            }
	        }
	    });
	    var colors = ['rgb(212, 40, 40)',  
						'rgb(180, 216, 42)',  
						'rgb(43, 221, 115)',  
						'rgb(45, 117, 226)',  
						'rgb(187, 45, 222)']; 
	    
	    var barChart = Ext.create('Ext.chart.Chart', {
	        height: 280,
	        width:'50%',
	        title:'按分数段统计人数',
	        style: 'background: #fff',
            padding: '10 0 0 0',
            insetPadding: 40,
            animate: true,
            shadow: false,
	        theme: 'Blue',
	        legend:{
	        	position:'bottom'
	        },
	        store: chartStore,
	        items: [{
                type  : 'text',
                text  : '按分数段统计人数',
                font  : '22px Helvetica',
                width : 100,
                height: 30,
                x : 40, //the sprite x position
                y : 12  //the sprite y position
            }],
	        
	        axes: [{
                type: 'Numeric',
                position: 'left',
                fields: ['nums'],
                label: {
                    renderer: function(value) { 
	                    var temValue = Math.round(value); 
	                    if (temValue >= value) {
	                        if ((temValue - value) > 0.00001) {
	                            return "";
	                        } else {
	                            return temValue;
	                        }
	                    } else {
	                        return "";
	                    } 
	                }
                },
                grid: true,
                minimum: 0
            }, {
                type: 'Category',
                position: 'bottom',
                fields: ['sections'],
                grid: true,
                label: {
                    rotate: {
                        degrees: -45
                    }
                }
            }],
	        series: [{
                type: 'column',
                axis: 'left',
                xField: 'sections',
                yField: 'nums',
                style: {
                    opacity: 0.80
                },
                highlight: {
                    fill: '#000',
                    'stroke-width': 20,
                    stroke: '#fff'
                },
                tips: {
                    trackMouse: true,
                    style: 'background: #FFF',
                    height: 20,
                    renderer: function(storeItem, item) {
                        this.setTitle(storeItem.get('nums') + '人');
                    }
                },
                renderer:function(sprite, storeItem, barAttr, i, store) {  
					///barAttr.fill = colors[i % colors.length];  动态填充颜色，根据定义的colors
					return barAttr;  
				}
            }]
	    });
	    
	    
	    
	    var pieChart = Ext.create('Ext.chart.Chart', {
            margin: '2 2 2 2',
			width:'50%',
			height: '100%',
            animate: true,
            shadow: false,
            store: chartStore,
            ////theme: 'Blue',
            insetPadding: 40,
            padding: '0 0 0 0',
            style: 'background: #fff',
            legend: {
                field: 'sections',
                position: 'bottom',
                boxStrokeWidth: 0,
                labelFont: '12px Helvetica'
            },
            series: [{
                type: 'pie',
                angleField: 'nums',
                position:'center',
                label: {
                    field: 'sections',
                    display: 'outside',
                    calloutLine: true
                },
                showInLegend: true,
                highlight: true,
                highlightCfg: {
                    fill: '#000',
                    'stroke-width': 20,
                    stroke: '#fff'
                },
                tips: {
                    trackMouse: true,
                    renderer: function(storeItem, item) {
                        this.setTitle(storeItem.get('nums') + '人');
                    }
                }
            }]
        });
	    
		////barChart.store.load();
        me.items = [{
            xtype: 'panel',
            width: '100%',
            height: '100%',
            layout:{ 
        		type:'vbox'
    		},
    		padding:2,
            items: [
            {
            	margin:'2,2,2,2', 
                xtype:'container', 
		        width:'100%',  
		        height:310, 
		        layout:{
		        	type:'hbox'
		        },
		        items:[barChart,pieChart]
            },	
            {
                margin:'2,2,2,2', 
                xtype:'container', 
		        width:'100%',  
		        height:310, 
		        layout:{
		        	type:'hbox'
		        },
		        items:[gridPanel,gridfailPanel]
            }]
        }];
		gridPanel.store.load();
		gridfailPanel.store.load();
		//barChart.store.load();
		//pieChart.store.load();
		gridPanel.store.on("load", function() {
	    	if(gridPanel.store.getCount()>0){
		    	var record = gridPanel.store.getAt(0);
		    	if(record!=undefined){
			    	var examid = record.get("examid");
			    	me.titleName.setText("当前选择考试名称："+record.get("examname"));
			    	if(examid!=undefined){
				    	dsfail.load({
						  	  	params:{
						  	  		examIdTerm:examid
						  	  	}
						});
						chartStore.load({
						  	  params:{
						  	  	examIdTerm:examid
						  	  }
						});
						
			    	}
		    	}
	    	}
		});
        this.callParent();
    }
});
