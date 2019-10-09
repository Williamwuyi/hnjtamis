/**
 * desc :岗位培训达标系统-考试结果统计 统计时间区间内的所有考试（到科目）的报名人数，通过人数，及格率
 * date :2015.4.23
 * author:wangyong
 */
Ext.define('modules.hnjtamis.statistics.ExamStatisticsChart1', {
    extend: 'Ext.Panel',
    xtype: 'dashboard-chart',
    requires: [
        'Ext.form.Panel',
        'Ext.form.FieldSet',
        'Ext.form.field.Number'
    ],
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
			id:'yearTermStatics1'
		})
		/// 添加查询按钮
		me.tbar.push({
			xtype: 'button',
	  	  	iconCls : 'query', 
	  	  	text: eap_operate_query,
	  	  	handler :function(){	
	  	  		///alert(Ext.getCmp('yearTerm').getValue());
	  	  		ds.load({
	  	  			params:{
	  	  				yearTerm:Ext.getCmp('yearTermStatics1').getValue()
	  	  			}
	  	  		})
	  	  	} 
		})
		///me.margins = '2 2 2 2'; // 为了不要与容器的边框重叠，设定2px的间距
        function perc(v) {
            return v + '%';
        }

        // Loads fresh records into the radar store based upon the passed company record

        updateRadarChart = function(rec) {
            radarStore.loadData([
                { 'Name': '报名人数', 'Data': rec.get('planpeoplenum') },
                { 'Name': '实到人数', 'Data': rec.get('factpeoplenum') },
                { 'Name': '及格人数', 'Data': rec.get('passpeoplenum') },
                { 'Name': '平均得分', 'Data': rec.get('avgscore') },
                { 'Name': '及格率 %', 'Data': rec.get('passrate') }
            ]);
        };

        var form = false,
            selectedRec = false,
            //performs the highlight of an item in the bar series
            highlightCompanyPriceBar = function(storeItem) {
                var name = storeItem.get('examname'),
                    series = barChart.series.get(0),
                    i, items, l;

                series.highlight = true;
                series.unHighlightItem();
                series.cleanHighlights();
                for (i = 0, items = series.items, l = items.length; i < l; i++) {
                    if (name == items[i].storeItem.get('examname')) {
                        series.highlightItem(items[i]);
                        break;
                    }
                }
                series.highlight = false;
            };

        var myData = [
            ['3M Co'],
            ['American Express'],
            ['AT&T Inc'],
            ['Boeing Co.'],
            ['Caterpillar Inc.'],
            ['Citigroup, Inc.'],
            ['Coca-Cola'],
            ['General Electric'],
            ['General Motors'],
            ['Home Depot'],
            ['Honeywell'],
            ['IBM'],
            ['Intel'],
            ['McDonald\'s'],
            ['Microsoft'],
            ['Pfizer Inc'],
            ['Procter & Gamble'],
            ['Verizon'],
            ['Wal-Mart']
        ];

        for (var i = 0, l = myData.length, rand = Math.random; i < l; i++) {
            var data = myData[i];
            data[1] = Ext.util.Format.number(((rand() * 10000) >> 0) / 100, "0");
            data[2] = ((rand() * 10000) >> 0) / 100;
            data[3] = ((rand() * 10000) >> 0) / 100;
            data[4] = ((rand() * 10000) >> 0) / 100;
            data[5] = ((rand() * 10000) >> 0) / 100;
        }

        //create data store to be shared among the grid and bar series.
        var ds = Ext.create('Ext.data.ArrayStore', {
            fields: [
                {name: 'examname' },
                {name: 'planpeoplenum',   type: 'int'},
                {name: 'factpeoplenum',   type: 'int'},
                {name: 'passpeoplenum',   type: 'int'},
                {name: 'avgscore', type: 'float'},
                {name: 'passrate',  type: 'float'}
            ],
            ///data: myData,
            proxy : {
				type : 'ajax',
				actionMethods : "POST",
				url : 'statistics/exam/listForExamStatisticsListAction!list.action',// 后台请求地址
				reader : {
					type : 'json',
					root : 'list',
					totalProperty : "total"
				}
			},
            listeners: {
                beforesort: function() {
                    if (barChart) {
                        var a = barChart.animate;
                        barChart.animate = false;
                        barChart.series.get(0).unHighlightItem();
                        barChart.animate = a;
                    }
                },
                //add listener to (re)select bar item after sorting or refreshing the dataset.
                refresh: {
                    fn: function() {
                        if (selectedRec) {
                            highlightCompanyPriceBar(selectedRec);
                        }
                    },
                    // Jump over the chart's refresh listener
                    delay: 1
                }
            }
        });
		
        // Radar chart will render information for a selected company in the
        // list. Selection can also be done via clicking on the bars in the series.

        // create radar store.
        var radarStore = Ext.create('Ext.data.JsonStore', {
            fields: ['Name', 'Data'],
            data: [
                { 'Name': '报名人数', 'Data': 100 },
                { 'Name': '实到人数','Data': 100 },
                { 'Name': '及格人数', 'Data': 100 },
                { 'Name': '平均得分', 'Data': 100 },
                { 'Name': '及格率 %', 'Data': 100 }
            ]
		            
        });

        var radarChart = Ext.create('Ext.chart.Chart', {
            margin: '0 0 0 0',
//            insetPadding: 0,
            flex: 1,
            animate: true,
            store: radarStore,
            theme: 'Blue',
            axes: [{
                steps: 5,
                type: 'Radial',
                position: 'radial',
                maximum: 100
            }],
            series: [{
                type: 'radar',
                xField: 'Name',
                yField: 'Data',
                showInLegend: false,
                showMarkers: true,
                markerConfig: {
                    radius: 4,
                    size: 4,
                    fill: 'rgb(69,109,159)'
                },
                style: {
                    fill: 'rgb(194,214,240)',
                    opacity: 0.5,
                    'stroke-width': 0.5
                }
            }]
        });

    //create a grid that will list the dataset items.
    var gridPanel = Ext.create('Ext.grid.Panel', {
        ///id: 'examname-form',
        flex: 6,
        store: ds,
        forceFit: true,
        stripeRows : true,// 班马线效果
        autoScroll:true,
        autoHeight: true,
        defaults: {
            sortable: true
        },
        columns: [
            {
                text: '名称',
                width: 200,
                dataIndex: 'examname'
            },
            {
                text: '报名人数',
                dataIndex: 'planpeoplenum',
                align: 'right'
            },
            {
                text: '实到人数',
                align: 'right',
                dataIndex: 'factpeoplenum'
            },
            {
                text: '及格人数',
                align: 'right',
                dataIndex: 'passpeoplenum'
            },
            {
                text: '平均得分',
                align: 'right',
                dataIndex: 'avgscore'
            },
            {
                text: '及格率',
                align: 'right',
                dataIndex: 'passrate',
                renderer: perc
            }
        ],

        listeners: {
            selectionchange: function(model, records) {
                var fields;
                if (records[0]) {
                    selectedRec = records[0];
                    if (!form) {
                        form = this.up('panel').down('form').getForm();
                        fields = form.getFields();
                        fields.each(function(field){
                            if (field.name != 'examname') {
                                field.setDisabled(false);
                            }
                        });
                    } else {
                        fields = form.getFields();
                    }
                    
                    // prevent change events from firing
                    form.suspendEvents();
                    form.loadRecord(selectedRec);
                    this.up('panel').down('fieldset').setTitle(selectedRec.get('examname'));
                    form.resumeEvents();
                    highlightCompanyPriceBar(selectedRec);
                }
            }
        }
    });
	
	var colors = ['rgb(212, 40, 40)',  
						'rgb(180, 216, 42)',  
						'rgb(43, 221, 115)',  
						'rgb(45, 117, 226)',  
						'rgb(187, 45, 222)']; 
    //create a bar series to be at the top of the panel.
    var barChart = Ext.create('Ext.chart.Chart', {
        height: 200,
        margin: '0 0 3 0',
        theme: 'Sky',
        shadow: true,
        animate: true,
        style:  {
            border: 0
        },
        store: gridPanel.store,
        axes: [{
            type: 'Numeric',
            position: 'left',
            fields: 'avgscore',
            minimum: 0,
            hidden: true
        }, {
            type: 'Category',
            position: 'bottom',
            fields: ['examname'],
            label: {
                renderer: function(v) {
                    return Ext.String.ellipsis(v, 15, false);
                },
                font: '11px Arial',
                rotate: {
                    degrees: -45
                }
            }
        }],
        series: [{
            type: 'column',
            axis: 'left',
            style: {
                fill: '#456d9f'
            },
            highlight: {
                fill: '#000',
                'stroke-width': 2,
                stroke: '#fff'
            },
            label: {
                contrast: true,
                display: 'insideEnd',
                field: 'avgscore',
                orientation: 'vertical',
                'text-anchor': 'middle'
            },
            listeners: {
                itemmouseup: function(item) {
                     var series = barChart.series.get(0);
                     gridPanel.getSelectionModel().select(Ext.Array.indexOf(series.items, item));
                }
            },
            renderer:function(sprite, storeItem, barAttr, i, store) {  
					barAttr.fill = colors[i % colors.length];  
					return barAttr;  
				},
            xField: 'name',
            yField: ['avgscore']
        }]
    });
	
        me.items = [{
            xtype: 'panel',
            width: '100%',
            height: 670,
            fieldDefaults: {
                labelAlign: 'left',
                msgTarget: 'side'
            },
    
            layout: {
                type: 'vbox',
                align: 'stretch'
            },
        
            items: [
                barChart,
            {
                xtype: 'container',
                layout: {
                    type: 'hbox',
                    align: 'stretch'
                },
                flex: 3,
                items: [
                    gridPanel,
                {
                    xtype: 'form',
                    flex: 3,
                    layout: {
                        type: 'vbox',
                        align:'stretch'
                    },
                    margin: '0 0 0 5',
                    items: [{
                        margin: '2',
                        xtype: 'fieldset',
                       /// flex: 1,
                        title: '未选择任何记录',
                        defaults: {
                            disabled: true,
                            // min/max will be ignored by the text field
                            //maxValue: 100,
                            minValue: 0,
                            anchor: '100%',
                            enforceMaxLength: true,
                            maxLength: 5,
                            bubbleEvents: ['change']
                        },
                        defaultType: 'numberfield',
                        items: [{
                            fieldLabel: '报名人数',
                            name: 'planpeoplenum'
                        }, {
                            fieldLabel: '初到人数',
                            name: 'factpeoplenum'
                        }, {
                            fieldLabel: '通过率',
                            name: 'passpeoplenum'
                        }, {
                            fieldLabel: '平均得分',
                            name: 'avgscore'
                        }, {
                            fieldLabel: '及格率 %',
                            name: 'passrate'
                        }]
                    },
                        radarChart
                    ],
                    listeners: {
                        // buffer so we don't refire while the user is still typing
                        buffer: 200,
                        change: function(field, newValue, oldValue, listener) {
                            if (selectedRec && form) {
                                if (newValue > field.maxValue) {
                                    field.setValue(field.maxValue);
                                } else {
                                    if (form.isValid()) {
                                        form.updateRecord(selectedRec);
                                        updateRadarChart(selectedRec);
                                    }
                                }
                            }
                        }
                    }
                }]
            }]
        }];
        
        
		gridPanel.store.load();
		////barChart.store.load();
        this.callParent();
    }
});
