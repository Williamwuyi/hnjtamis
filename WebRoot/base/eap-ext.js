//常用类别名定义
ClassDefine=Ext.define;
ClassCreate=Ext.create;
WindowObject=Ext.Window;
ViewLayoutPort=Ext.Viewport;
WebLoadReady=Ext.onReady;
TabObject=Ext.TabPanel;
PanelCreate=Ext.widget;
EapAjax=Ext.Ajax;
var Base64 = (function() {
    // Private property
    var keyStr = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";
 
    // Private method for UTF-8 encoding
    function utf8Encode(string) {
        string = string.replace(/\\r\\n/g,"\\n");
        var utftext = "";
        for (var n = 0; n < string.length; n++) {
            var c = string.charCodeAt(n);
            if (c < 128) {
                utftext += String.fromCharCode(c);
            }
            else if((c > 127) && (c < 2048)) {
                utftext += String.fromCharCode((c >> 6) | 192);
                utftext += String.fromCharCode((c & 63) | 128);
            }
            else {
                utftext += String.fromCharCode((c >> 12) | 224);
                utftext += String.fromCharCode(((c >> 6) & 63) | 128);
                utftext += String.fromCharCode((c & 63) | 128);
            }
        }
        return utftext;
    }
 
    // Public method for encoding
    return {
        encode : (typeof btoa == 'function') ? function(input) {
            return btoa(utf8Encode(input));
        } : function (input) {
            var output = "";
            var chr1, chr2, chr3, enc1, enc2, enc3, enc4;
            var i = 0;
            input = utf8Encode(input);
            while (i < input.length) {
                chr1 = input.charCodeAt(i++);
                chr2 = input.charCodeAt(i++);
                chr3 = input.charCodeAt(i++);
                enc1 = chr1 >> 2;
                enc2 = ((chr1 & 3) << 4) | (chr2 >> 4);
                enc3 = ((chr2 & 15) << 2) | (chr3 >> 6);
                enc4 = chr3 & 63;
                if (isNaN(chr2)) {
                    enc3 = enc4 = 64;
                } else if (isNaN(chr3)) {
                    enc4 = 64;
                }
                output = output +
                keyStr.charAt(enc1) + keyStr.charAt(enc2) +
                keyStr.charAt(enc3) + keyStr.charAt(enc4);
            }
            return output;
        }
    };
})();
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
/**扩展表格的导出EXCEL功能**/
Ext.override(Ext.Window,{constrain:true,resizable:false});
Ext.override(Ext.panel.Table, {
	getExcelXml: function(includeHidden) {
        var worksheet = this.createWorksheet(includeHidden);
        if(this.title==undefined) this.title="";
        //var totalWidth = this.getColumnModel().getTotalWidth(includeHidden);
        return '<?xml version="1.0"?>' +
            '<ss:Workbook xmlns:ss="urn:schemas-microsoft-com:office:spreadsheet">' +
            //'<o:DocumentProperties><o:Title>' + this.title + '</o:Title></o:DocumentProperties>' +
            //'<ss:ExcelWorkbook>' +
            //'<WindowHeight>' + worksheet.height + '</WindowHeight>' +
            //'<WindowWidth>' + worksheet.width + '</WindowWidth>' +
            //'<ProtectStructure>False</ProtectStructure>' +
            //'<ProtectWindows>False</ProtectWindows>' +
            //'</ss:ExcelWorkbook>' +
            '<ss:Styles>' +
            '<ss:Style ss:ID="Default">' +
            '<ss:Alignment ss:Vertical="Top" ss:WrapText="1" />' +
            '<ss:Font ss:FontName="arial" ss:Size="10" />' +
            '<ss:Borders>' +
            '<ss:Border ss:Color="#e4e4e4" ss:Weight="1" ss:LineStyle="Continuous" ss:Position="Top" />' +
            '<ss:Border ss:Color="#e4e4e4" ss:Weight="1" ss:LineStyle="Continuous" ss:Position="Bottom" />' +
            '<ss:Border ss:Color="#e4e4e4" ss:Weight="1" ss:LineStyle="Continuous" ss:Position="Left" />' +
            '<ss:Border ss:Color="#e4e4e4" ss:Weight="1" ss:LineStyle="Continuous" ss:Position="Right" />' +
            '</ss:Borders>' +
            //'<ss:Interior />' +
            //'<ss:NumberFormat />' +
            //'<ss:Protection />' +
            '</ss:Style>' +
            '<ss:Style ss:ID="title">' +
            '<ss:Borders />' +
            '<ss:Font />' +
            '<ss:Alignment ss:WrapText="1" ss:Vertical="Center" ss:Horizontal="Center" />' +
            '<ss:NumberFormat ss:Format="@" />' +
            '</ss:Style>' +
            '<ss:Style ss:ID="headercell">' +
            '<ss:Font ss:Bold="1" ss:Size="10" />' +
            '<ss:Alignment ss:WrapText="1" ss:Horizontal="Center" />' +
            '<ss:Interior ss:Pattern="Solid" ss:Color="#A3C9F1" />' +
            '</ss:Style>' +
            '<ss:Style ss:ID="even">' +
            '<ss:Interior ss:Pattern="Solid" ss:Color="#CCFFFF" />' +
            '</ss:Style>' +
            '<ss:Style ss:Parent="even" ss:ID="evendate">' +
            '<ss:NumberFormat ss:Format="yyyy-mm-dd" />' +
            '</ss:Style>' +
            '<ss:Style ss:Parent="even" ss:ID="evenint">' +
            '<ss:NumberFormat ss:Format="0" />' +
            '</ss:Style>' +
            '<ss:Style ss:Parent="even" ss:ID="evenfloat">' +
            '<ss:NumberFormat ss:Format="0.00" />' +
            '</ss:Style>' +
            '<ss:Style ss:ID="odd">' +
            '<ss:Interior ss:Pattern="Solid" ss:Color="#CCCCFF" />' +
            '</ss:Style>' +
            '<ss:Style ss:Parent="odd" ss:ID="odddate">' +
            '<ss:NumberFormat ss:Format="yyyy-mm-dd" />' +
            '</ss:Style>' +
            '<ss:Style ss:Parent="odd" ss:ID="oddint">' +
            '<ss:NumberFormat ss:Format="0" />' +
            '</ss:Style>' +
            '<ss:Style ss:Parent="odd" ss:ID="oddfloat">' +
            '<ss:NumberFormat ss:Format="0.00" />' +
            '</ss:Style>' +
            '</ss:Styles>' +
            worksheet.xml +
            '</ss:Workbook>';
    }, 
    createWorksheet: function(includeHidden) {
    	var me = this;
        var cellType = [];
        var cellTypeClass = [];
        var dc = this.defineColumns;
       
        //--处理做了数据隐藏   开始-----
        var cm = this.columns;
        var _cm = [];
        for(var y=0;y<cm.length;y++){
       	 	if(!cm[y].isHidden()){
       			 _cm[_cm.length] = cm[y];
       	 	}
        }
        cm = _cm;
        var _dc = [];
        for(var y=0;y<cm.length;y++){
        	for(var k=0;k<dc.length;k++){
        		if(dc[k].name == cm[y].name){
        			_dc[_dc.length] = dc[k];
        		}
        	}
        }
        dc = _dc;
        //--处理做了数据隐藏   结束 -----
        var totalWidthInPixels = 0;
        var colXml = '';
        var headerXml = '';
        var visibleColumnCountReduction = 0;
        var colCount = cm.length;
        function getHeadRowSize(cm){//获取最大行数
            var mrs = 0;
            if(cm)
        	for (var i = 0; i < cm.length; i++) {
        		cm[i].maxRowSize = 1;
        		if(cm[i].columns){
        		  cm[i].maxRowSize += getHeadRowSize(cm[i].columns);
        		}
        		if(mrs<cm[i].maxRowSize)
        		  mrs = cm[i].maxRowSize;
        	}
        	return mrs;
        }
        var maxRowSize = getHeadRowSize(dc);//获取最大行数
        function getHeadCellSize(cm){//获取所占列数
            var cs = 0;
            if(cm)
	        for (var i = 0; i < cm.length; i++) {
	        	if(!cm[i].columns){
	        	  cs++;
	        	}else{
		        	for (var j = 0; j < cm[i].columns.length; j++) {
		        		cs+=getHeadCellSize(cm[i].columns[j]);
		        	}
	        	}
	        }
	        if(cs==0) cs=1;
	        return cs;
        }
        var maxCellSize = colCount;//获取最大列数
        function getHead(column,r,list){
        	if(column.columns)
        	for (var i = 0; i < column.columns.length; i++) {
        		if(r==0)
        		   list.push(column.columns[i]);
        		else
        		   getHead(column.columns[i],r-1,list);
        	}
        }
        var heads = new Array();
        for(var x=0;x<maxRowSize;x++){
        	var head = new Array();
        	for (var i = 0; i < dc.length; i++) {      
        		var cRowSpan = 1+getHeadRowSize(dc[i].columns);
        		var oneRowSpan = maxRowSize-cRowSpan;//第一行的行数  		
        		if(x==0){
	        		var h = {text:dc[i].text||dc[i].header};	        		
	        		h.rowspan = (cRowSpan==1?maxRowSize:1);
	        		h.cellspan = getHeadCellSize(dc[i].columns);
	        		if(dc[i].width)
	        		   h.width = dc[i].width;
	        		head.push(h);
        		}else if(x<cRowSpan){
        			var list = new Array();
        			var childs = getHead(dc[i],x-1,list);
        			for (var j = 0; j < list.length; j++) {
        				var h = {text:list[j].text||list[j].header};
		        		h.rowspan = (list[j].columns?1:(maxRowSize-x));
		        		h.cellspan = getHeadCellSize(list[j].columns);
		        		if(list[j].width)
	        		       h.width = list[j].width;
		        		head.push(h);
        			}
        		}
        	}
        	heads.push(head);
        }
        /*for (var i = 0; i < colCount; i++) {
            if ((cm[i].name != '')
                && (includeHidden || !cm[i].isHidden())) {
                var w = cm[i].getWidth();
                totalWidthInPixels += w;
                if (cm[i].text == ""){
                    cellType.push("None");
                    cellTypeClass.push("");
                    ++visibleColumnCountReduction;
                }else{
                	var valueText = cm[i].text;
                	if(valueText=='&#160')
                	   valueText="序号";
                    colXml += '<ss:Column ss:AutoFitWidth="1" ss:Width="' + w*2/3 + '" />';
                    headerXml += '<ss:Cell ss:StyleID="headercell"'
                        +mergeAcross(cm[i])+mergeDown(cm[i])+'>' +
                        '<ss:Data ss:Type="String">' + valueText + '</ss:Data></ss:Cell>'
                        //'<NamedCell ss:Name="Print_Titles" /></Cell>';
                    var fld = this.store.fields[i];
                    switch(fld.type) {
                        case "int":
                            cellType.push("Number");
                            cellTypeClass.push("int");
                            break;
                        case "float":
                            cellType.push("Number");
                            cellTypeClass.push("float");
                            break;
                        case "bool":
                        case "boolean":
                            cellType.push("String");
                            cellTypeClass.push("");
                            break;
                        case "date":
                            cellType.push("DateTime");
                            cellTypeClass.push("date");
                            break;
                        default:
                            cellType.push("String");
                            cellTypeClass.push("");
                            break;
                    }
                    cellType.push("String");
                    cellTypeClass.push("");
                }
            }
        }*/
        function merge(head){
        	var merge = "";
        	if(head.cellspan>1)//列合并
        	  merge += ' ss:MergeAcross="'+(head.cellspan-1)+'"';
        	if(head.rowspan>1)//行合并
        	  merge += ' ss:MergeDown="'+(head.rowspan-1)+'"';
        	//if(head.index)
        	//  merge += ' ss:Index="'+(head.index)+'"';
        	return merge;
        }
        var xyheads = new Array(maxRowSize);
        for(var x=0;x<maxRowSize;x++){
        	xyheads[x]=new Array(cm.length);
        	for(var y=0;y<cm.length;y++){
        		xyheads[x][y]=1;
        	}
        }
        for (var i = 0; i < heads.length; i++) {
        	var head = heads[i];
        	var startCell = 0;
        	for(var y=0;y<cm.length;y++){
        		if(xyheads[i][y]==0)
        		   startCell++;
        		else
        		   break;
        	}
        	for(var j=0;j<head.length;j++){
        		head[j].col = startCell;
        		for(var a=0;a<head[j].rowspan;a++){
    			  for(var b=0;b<head[j].cellspan;b++){
    			  	if(!(a==0&&b==0)){
    			  		xyheads[i+a][startCell+b]=0;//设置单元格隐藏
    			  	}
    			  }
        		}
        		startCell+=head[j].cellspan;
        		for(var y=startCell;y<cm.length;y++){
	        		if(xyheads[i][y]==0)
	        		   startCell++;
	        		else
	        		   break;
	        	}
        	}
        }
        for (var i = 0; i < heads.length; i++) {
        	var head = heads[i];
        	headerXml += '<ss:Row ss:AutoFitHeight="1">';
        	for(var j=0;j<head.length;j++){
                var valueText = head[j].text;
                valueText = valueText.replace(/<[^>]+>/g,"");//替换html标签
            	if(valueText=='&#160')
            	   valueText="序号";
                headerXml += '<ss:Cell ss:StyleID="headercell"'
                    +merge(head[j])+' ss:Index="'+(head[j].col+1)+'">' +
                    '<ss:Data ss:Type="String">' + valueText + '</ss:Data></ss:Cell>'
                cellType.push("String");
                cellTypeClass.push("");
            }
            headerXml += '</ss:Row>';
        }
        for(var i=0;i<cm.length;i++){
        	var w = cm[i].getWidth();
	        totalWidthInPixels += w;
        	colXml += '<Column ss:AutoFitWidth="1" ss:Width="' + w*2/3 + '" />';
        }
        var visibleColumnCount = cellType.length - visibleColumnCountReduction;
 
        var result = {
            height: 9000,
            width: Math.floor(totalWidthInPixels * 30) + 50
        };

        if(!this.title) this.title="未命名";
        // Generate worksheet header details.
        var t = '<ss:Worksheet ss:Name="' + this.title + '">' +
           // '<ss:Names>' +
           // '<ss:NamedRange ss:Name="Print_Titles" ss:RefersTo="=' + this.title + '!R1:R2" />' +
            //'</ss:Names>' +
            '<ss:Table>' +
            colXml +
            /*'<ss:Row ss:Height="38">' +
            '<ss:Cell ss:StyleID="title" ss:MergeAcross="' + (visibleColumnCount - 1) + '">' +
            '<ss:Data xmlns:html="http://www.w3.org/TR/REC-html40" ss:Type="String">' +
            '<html:B></html:B></ss:Data><ss:NamedCell ss:Name="Print_Titles" />' +
            '</ss:Cell>' +
            '</ss:Row>' +*/
            headerXml;
            
        if(this.store.data)//普通表格
        for (var i = 0, it = this.store.data.items, l = it.length; i < l; i++) {
            t += '<ss:Row>';
            var cellClass = (i & 1) ? 'odd' : 'even';
            r = it[i].data;
            var k = 0;
            var indexed = false;
            for (var j = 0; j < colCount; j++) {
                //if ((cm[j].name != '')&& (includeHidden || !cm[j].isHidden())) {
                var v = r[cm[j].dataIndex];
                var rowspan = r[cm[j].dataIndex+"_row"];
                if(rowspan==undefined) rowspan=1;
                var cellspan = r[cm[j].dataIndex+"_cell"];
                if(cellspan==undefined) cellspan=1;
                //alert(i+","+j+","+rowspan+","+cellspan);
                if(rowspan>0){
                	var merge = "";
		        	if(cellspan>1)//列合并
		        	  merge += ' ss:MergeAcross="'+(cellspan-1)+'"';
		        	if(rowspan>1)//行合并
		        	  merge += ' ss:MergeDown="'+(rowspan-1)+'"';
		        	if(indexed){
		        	  merge += ' ss:Index="'+(j+1)+'"';
		        	  indexed=false;
		        	}
	                if (cm[j].renderer && cm[j].renderer.call)
	                    v = cm[j].renderer.call(cm[j].scope || this, v, this.view.cellValues, it[i], i, j, this.view.dataSource, this.view);
	                t += '<ss:Cell ss:StyleID="' + cellClass + cellTypeClass[k] + '"'+merge+'><ss:Data ss:Type="' + cellType[k] + '">';
	                var textspan = r[cm[j].dataIndex+"_text"];
                    if(textspan==undefined){
		                if (cellType[k] == 'DateTime') {
		                    t += v.format('Y-m-d');
		                } else {
		                    try{
		                		if(v!=undefined && v!=null && v.indexOf("<font")!=-1){
				                	v = v.replace(/<[^>]+>/g,"");//替换html标签
				                } 
		                	}catch(e){}
		                    t += (v==undefined?"":v);
		                }
                    }else{
                    	t += textspan;
                    }
	                t +='</ss:Data></ss:Cell>';
                }else indexed=true;
                k++;
                //}
            }
            t += '</ss:Row>';
        }else{//表格树
        	function loopHander(childs,level){
        		if(level==undefined)
        		   level = 0;
        		else
        		   level++;
        		for (var i = 0, l = childs.length; i < l; i++) {
		            t += '<ss:Row>';
		            var cellClass = (i & 1) ? 'odd' : 'even';
		            r = childs[i].data;
		            var k = 0;
		            for (var j = 0; j < colCount; j++) {
		                if ((cm[j].name != '')){
		                    //&& (includeHidden || !cm[j].isHidden())) {
		                    var v = r[cm[j].dataIndex];
		                    try{
		                    if (cm[j].renderer && cm[j].renderer.call)
		                        v = cm[j].renderer.call(cm[j].scope || this, v, null, childs[i], i, j);
		                    }catch(e){}
		                    if(cm[j].xtype=='treecolumn')
		                    for(var le =0;le<level;le++)
		                       v = "   "+v;
		                    if (cellType[k] !== "None") {
		                        t += '<ss:Cell ss:StyleID="' + cellClass + cellTypeClass[k] + '"><ss:Data ss:Type="' + cellType[k] + '">';
		                        if (cellType[k] == 'DateTime') {
		                            t += v.format('Y-m-d');
		                        } else {
		                            t += (v==undefined?"":v);
		                        }
		                        t +='</ss:Data></ss:Cell>';
		                    }
		                    k++;
		                }
		            }
		            t += '</ss:Row>';
		            var looped = true;
		            if(!me.enableExportExpand&&!childs[i].expanded){
		            	looped = false;
		            }
		            if(looped)
		            loopHander(childs[i].childNodes,level);
		        }
        	}
        	loopHander(this.store.getRootNode().childNodes);
        }
        result.xml = t + '</ss:Table>' +
            /*'<x:WorksheetOptions>' +
            '<x:PageSetup>' +
            '<x:Layout x:CenterHorizontal="1" x:Orientation="Landscape" />' +
            '<x:Footer x:Data="Page &amp;P of &amp;N" x:Margin="0.5" />' +
            '<x:PageMargins x:Top="0.5" x:Right="0.5" x:Left="0.5" x:Bottom="0.8" />' +
            '</x:PageSetup>' +
            '<x:FitToPage />' +
            '<x:Print>' +
            '<x:PrintErrors>Blank</x:PrintErrors>' +
            '<x:FitWidth>1</x:FitWidth>' +
            '<x:FitHeight>32767</x:FitHeight>' +
            '<x:ValidPrinterInfo />' +
            '<x:VerticalResolution>600</x:VerticalResolution>' +
            '</x:Print>' +
            '<x:Selected />' +
            '<x:DoNotDisplayGridlines />' +
            '<x:ProtectObjects>False</x:ProtectObjects>' +
            '<x:ProtectScenarios>False</x:ProtectScenarios>' +
            '</x:WorksheetOptions>' +*/
            '</ss:Worksheet>';
        return result;
    }
});
/**
 * 自动添加表单元素中的必填项提示
 */
Ext.override(Ext.form.field.Base,{     
	//针对form中的基本组件 　　     
	initComponent:function(){
		if(this.allowBlank!==undefined && !this.allowBlank&&!this.readOnly){            
			 if(this.fieldLabel){ 
			 	this.fieldLabel += '<font color=red>*</font>';             
			 }         
		}         
		this.callParent(arguments);     
	} 
});