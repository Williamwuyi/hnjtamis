/**
 * 无设置查询条件 页签
 */
ClassDefine('base.core.TableMergeCells', {
	extend : 'Ext.TabPanel',
	initComponent : function() {
		var me = this;
		me.region = 'center';// 对齐属性
		me.margins = '2 2 2 2'; // 为了不要与容器的边框重叠，设定2px的间距
		//me.loadMask = true;// 开启提示数据下载
		me.activeTab = 0;              //默认激活第一个tab页 
		me.animScroll = true;           //使用动画滚动效果 
		me.enableTabScroll = true;       //tab标签超宽时自动出现滚动按钮 
		//me.showTermSize = me.showTermSize||1; //默认显示查询条件的个数
		me.customMergeWord = "此单元格合并"; //自定义合并单元格关键字
		me.callParent(); //自己定义的类，需要调用这个 以便在窗口中弹出
	},
		/** 
		* 合并单元格 
		* @param {} grid  要合并单元格的grid对象 
		* @param {} cols  要合并哪几列 [1,2,4] 
		*/  
	statics :{
      mergeCells : function( grid , cols ,preferences)
			{   
    	  		var me = this;
				if(document.getElementById(grid.getId()+"-body")
				&& document.getElementById(grid.getId()+"-body").firstChild
				&& document.getElementById(grid.getId()+"-body").firstChild.firstChild
				&& document.getElementById(grid.getId()+"-body").firstChild.firstChild.lastChild
				&& document.getElementById(grid.getId()+"-body").firstChild.firstChild.lastChild.getElementsByTagName('tr')){
						//==>ExtJs4.2的<tbody>改到上层<table>的lastChild . <tbody>是各个<tr>的集合
					    var arrayTr = document.getElementById(grid.getId()+"-body").firstChild.firstChild.lastChild.getElementsByTagName('tr');
					    var trCount = arrayTr.length;  //<tr>总行数
					     
					    var arrayTd;  
					    var td;  
					
						//==>显示层将目标格的样式改为.display='none';      
						var merge = function( rowspanObj , removeObjs ,colIndex,mySplitFlag)//定义合并函数  
					    { 
					    
					        if( 1!= rowspanObj.rowspan ){  
					            arrayTd = arrayTr[ rowspanObj.tr ].getElementsByTagName("td"); //合并行  
					            td = arrayTd[rowspanObj.td-1];  
					            td.rowSpan = rowspanObj.rowspan;  
					            td.vAlign = "middle";
					            //td.firstChild.style.marginTop=((rowspanObj.rowspan*21/2-10)+'px');
					            
					            try{
					            	var marginTopValue = rowspanObj.rowspan*21/2-(td.firstChild.offsetHeight/2);
					            	marginTopValue = marginTopValue<0?(rowspanObj.rowspan*21/2-10):marginTopValue;
					            	td.firstChild.style.marginTop=(marginTopValue+'px');
					            }catch(e){
					            	 td.firstChild.style.marginTop=((rowspanObj.rowspan*21/2-10)+'px');
					            }
					            //td.style.backgroundColor='white';
					            /*
					            td.style.borderRight='1px solid #ededed';
					            if(colIndex==5){
					            	td.style.borderLeft='1px solid #ededed';
					            	//console.log(td.firstChild.style);
					            }*/
					            if(!!mySplitFlag){
					            	var curInnerHTML = td.firstChild.innerHTML;
					            	if(curInnerHTML.indexOf(mySplitFlag)!=-1){
					            		td.firstChild.innerHTML = curInnerHTML.split(mySplitFlag)[0];
					            	}
					            }
					             //隐身被合并的单元格  
					            Ext.each(removeObjs,function(obj)
					            {
					                arrayTd = arrayTr[obj.tr].getElementsByTagName("td");  
					                arrayTd[obj.td-1].style.display='none';                           
					            });  
					        }else{
					        	
					        	if(!!mySplitFlag){
					        		arrayTd = arrayTr[ rowspanObj.tr ].getElementsByTagName("td"); //合并行  
					                td = arrayTd[rowspanObj.td-1]; 
					        		var curInnerHTML = td.firstChild.innerHTML;
					        		if(curInnerHTML.indexOf(mySplitFlag)!=-1){
					            		td.firstChild.innerHTML = curInnerHTML.split(mySplitFlag)[0];
					            	}
					            }
					        	 
					        }     
					    };    
						//==>显示层将目标格的样式改为.display='none';     
					
					    var rowspanObj = {}; //要进行跨列操作的td对象{tr:1,td:2,rowspan:5}      
					    var removeObjs = []; //要进行删除的td对象[{tr:2,td:2},{tr:3,td:2}]  
					    var col;  
					    //==>逐列靠表内具体数值去合并各个<tr> (表内数值一样则合并) 
					    Ext.each( cols , function( colIndex ){ 
					    	var mySplitFlag = !!preferences?preferences.get(colIndex):null;
					        var rowspan = 1;  
					        var divHtml = null;//单元格内的数值          
					        for( var i=0 ; i < trCount ; i++)//==>从第一行数据0开始
					        {  
					        	//==>一个arrayTr[i]是一整行的所有数据, 一个arrayTd是 <td xxxx ><div>具体数值</div></td> ,
					            arrayTd = arrayTr[i].getElementsByTagName("td");  
					            var cold=0;  
					            Ext.each(arrayTd,function(Td){ //获取RowNumber列和check列  
								     if(Td!=undefined && Td!=null && Td.getAttribute("class")!=undefined && Td.getAttribute("class")!=null && Td.getAttribute("class").indexOf(" x-grid-td") != -1) {
								       Td.style.borderRight='1px solid #ededed';
					                   //Td.style.borderLeft='1px solid #ededed';
								     } else if(Td.className!=undefined && Td.className!=null && Td.className.indexOf(" x-grid-td") != -1){
								          Td.style.borderRight='1px solid #ededed';
								     }
								                               
								});
								//          Ext.each(arrayTd,function(Td){ //获取RowNumber列和check列  
								//              if(Td.getAttribute("class").indexOf("x-grid-cell-special") != -1)  
								//                  cold++;                               
								//          });  
					            col = colIndex + cold;//跳过RowNumber列和check列  
					           
					            if( !divHtml )
					            {  
					                divHtml = arrayTd[col-1].innerHTML;  
					                //console.log(arrayTd[col-1].firstChild.innerHTML);
					                //divHtml = $(divHtml).text(); //==>拿到真正数值,相比Ext4.1多了一层<div>
					                divHtml = arrayTd[col-1].firstChild.innerHTML
					                if(i==trCount-1){//最后一行  不合并的情况
				                 		if(!!mySplitFlag && !!arrayTd[col-1].firstChild.innerHTML){
				                 			arrayTd = arrayTr[i].getElementsByTagName("td");
				                 			arrayTd[col-1].firstChild.innerHTML = arrayTd[col-1].firstChild.innerHTML.split(mySplitFlag)[0];
				                 		}
				                	}
					                rowspanObj = { tr:i,td:col,rowspan:rowspan }  
					            }
					            else
					            {  
					               // var cellText = arrayTd[col-1].innerHTML;  
					                //console.log(cellText);
					                
					                //cellText = $(cellText).text();//==>拿到真正数值 
					               var cellText = arrayTd[col-1].firstChild.innerHTML;
					               
					               var addf = function()
					 				{   
					                    rowspanObj["rowspan"] = rowspanObj["rowspan"]+1;  
					                    removeObjs.push({ tr:i,td:col });  
					                    if( i == trCount-1)  
					                    {   
					                    	merge(rowspanObj,removeObjs,colIndex,mySplitFlag);//执行合并函数  
					                    }
					                };  
					                var mergef = function()
					                {  
					                    merge(rowspanObj,removeObjs,colIndex,mySplitFlag);//执行合并函数  
					                    
					                    divHtml = cellText;  
					                    rowspanObj = {tr:i,td:col,rowspan:rowspan}  
					                    removeObjs = [];  
					                };  
					               
					                if( cellText == divHtml || cellText==me.customMergeWord || divHtml==me.customMergeWord){  
					                	
						                    if( colIndex != cols[0] ){   
						                    	
							                        var leftDisplay = arrayTd[col-2].style.display;//判断左边单元格值是否已display  
							                        if( leftDisplay == 'none' ){    
							                        	addf();   
							                        }else{   
							                        	//mergef(); //网上教程调用的是    mergef() 不过调用此方法 不适用 隔列合并
							                        	addf();
							                        }                  
							                    }
						                    else  
						                    {   
						                     		addf();          
						                    }                                 
					                }
					                else  
					                {   
					                 	mergef();
					                 	if(i==trCount-1){//最后一行  不合并的情况
					                 		if(!!mySplitFlag){
					                 			arrayTd = arrayTr[i].getElementsByTagName("td");
					                 			arrayTd[col-1].firstChild.innerHTML = cellText.split(mySplitFlag)[0];
					                 		}
					                	}
					                }    
					            }  
					        }  
					    });   
				}
			},
		isNotNull : function (value){
			if(value!=null && value!='null' && value!=''){
				return true;
			}else{
				return false;
			}
		},
		/*
		 * 获取访问路径中 传递的参数
		 * name: 路径中参数的名称
		 * requestSrc: 请求的路径
		 */
		 getSrcParam : function(name, requestSrc) {
		    var match = RegExp(name + '=([^&]*)').exec(requestSrc);
		    return match && decodeURIComponent(match[1]);
		},
		Map : function(){
				var struct = function(key, value) {
					  this.key = key;
					  this.value = value;
					 }
				 
					 var put = function(key, value){
					  for (var i = 0; i < this.arr.length; i++) {
					   if ( this.arr[i].key === key ) {
					    this.arr[i].value = value;
					    return;
					   }
					  }
					   this.arr[this.arr.length] = new struct(key, value);
					 }
				 
					 var get = function(key) {
					  for (var i = 0; i < this.arr.length; i++) {
					   if ( this.arr[i].key === key ) {
					     return this.arr[i].value;
					   }
					  }
					  return null;
					 }
				 
					 var remove = function(key) {
					  var v;
					  for (var i = 0; i < this.arr.length; i++) {
					   v = this.arr.pop();
					   if ( v.key === key ) {
					    continue;
					   }
					   this.arr.unshift(v);
					  }
					 }
				 
					 var size = function() {
					  return this.arr.length;
					 }
				 
					 var isEmpty = function() {
					  return this.arr.length <= 0;
					 } 
					 this.arr = new Array();
					 this.get = get;
					 this.put = put;
					 this.remove = remove;
					 this.size = size;
					 this.isEmpty = isEmpty;
			},
			setGridColumnHeaderAlign : function(){
				if(Ext.grid.Column){
		        	Ext.grid.Column.override({
		        		afterRender: function() {
		        	        var me = this,
		        	            triggerEl = me.triggerEl,
		        	            triggerWidth;

		        	        me.callParent(arguments);
		        	        if (!Ext.isIE8 || !Ext.isStrict) {
		        	            me.mon(me.getFocusEl(), {
		        	                focus: me.onTitleMouseOver,
		        	                blur: me.onTitleMouseOut,
		        	                scope: me
		        	            });
		        	        }
		        	        if (triggerEl && me.self.triggerElWidth === undefined) {
		        	            triggerEl.setStyle('display', 'block');
		        	            me.self.triggerElWidth = triggerEl.getWidth();
		        	            triggerEl.setStyle('display', '');
		        	        }
		        	        me.keyNav = new Ext.util.KeyNav(me.el, {
		        	            enter: me.onEnterKey,
		        	            down: me.onDownKey,
		        	            scope: me
		        	        });
		        	        me.el.addCls(Ext.baseCSSPrefix + 'column-header-align-' + me.align).addClsOnOver(me.overCls);
		        	        me.titleAlign = me.titleAlign || me.align;
		        	        me.el.addCls(Ext.baseCSSPrefix + 'column-header-align-' + me.titleAlign).addClsOnOver(me.overCls);
		        	    }
		        	});
		        }
			}
	}
});