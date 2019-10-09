/**
 * 下拉图标列表控件
 */
Ext.define('base.core.SelectImage', {
	extend : 'Ext.form.ComboBox',
	alias: 'widget.selectimage',
	readOnly:false,//是否只读
	data:[],//数据,无配置的静态数据，则用运程数据
	emptyText:'--请选择--',
	mode:'local',
	valueField:'value',//值字段名
	displayField:'text',//显示字段名
	triggerAction:'all',
	editable:false,//不可编辑值
	imagePath:'resources/images/',
	imageSize : 32,//图片大小
	imageColumnSize : 3,
	defaults : {
	        anchor :"100%"
	},
	initComponent : function() {
		var me = this;
		me.value = me.defaultValue;//缺省值
		me.hiddenName = me.valueField+"_id";
		var fields = [{name:'name'},{name : 'url'}];
		me.mode = 'remote';
		me.tpl = '<table><tr><tpl for="."><td class="x-combo-list-item" onmouseover="this.style.backgroundColor=\'#FF0000\'" ' +
				'onmouseout="this.style.backgroundColor=\'#FFFFFF\'">' +
			    '<img width='+me.imageSize+' height='+me.imageSize+' src="{url}" onclick="Ext.getCmp(\''+
				    me.id+'\').clickFun(\'{url}\')"/></td><tpl if="xindex % '+me.imageColumnSize+' === 0"></tr>' +
				'<tr></tpl></tpl></tr></table>';
		me.store = new Ext.data.ArrayStore({
			fields : fields,
            proxy : {
				type : 'ajax',
				actionMethods : "POST",
				url : 'getImageFile.jsp',// 后台请求地址
				reader : {
					type : 'json'
				},
				extraParams:{'imagePath':me.imagePath}
			},
			reader:new Ext.data.JsonReader({},['name','url'])
		});
		me.callParent();
		me.on({render:
			   {scope:me, 
			       fn:function() { 
						var wrap = me.wrap = this.el.down('td.x-form-trigger-input-cell');
						wrap.applyStyles({border:'1px',height:me.imageSize+5});
						//wrap.dom.height = me.imageSize+5;
						//this.el.down('img.x-form-arrow-trigger').dom.style.height = me.imageSize+5;
						this.el.down('input.x-form-field').dom.style.display='none';
						//wrap.applyStyles({position:'relative'}); 
						//this.el.addClass('x-icon-combo-input'); 
						this.flag = Ext.DomHelper.append(wrap, { 
						     tag: 'img', src:this.value ,
						     width:this.imageSize,height:this.imageSize,
						     style:'display:none'
						});
						wrap.dom.title="注意：双击清除！";
						wrap.dom.ondblclick=function(){
							me.clickFun("");
						}
				   }
			   }
       }); 
	},//图片设置定方法
	setValue:function(value, doSelect){
		var me = this;
		if(me.flag)
		me.flag.src = value;
		if(value&&value!="")
		  me.flag.style.display='block';
		me.value = value;
		me.setRawValue(value);
		return value;
	},//图片选择事件方法
	clickFun:function(image){
		var me = this;
		me.value = image;
		me.setRawValue(image);
		me.flag.src = image;
		me.flag.style.display=(image&&image!=""?"block":"none");
		me.collapse();
	},
    alignPicker : function() {
        var me = this, picker, isAbove, aboveSfx = '-above';
        if (this.isExpanded) {
            picker = me.getPicker();
            if (me.matchFieldWidth) {
                picker.setWidth(me.imageColumnSize*(me.imageSize+5)+20);
            }
            picker.alignTo(me.wrap,'bl' , me.pickerOffset);
        }
    }/*,listeners: {  
        afterRender: function(thisForm, options){  
            this.keyNav = Ext.create('Ext.util.KeyNav', this.el, {  
                backspace: function(){},//清除回退事件
                scope: this  
            });  
        }  
    }*/
});