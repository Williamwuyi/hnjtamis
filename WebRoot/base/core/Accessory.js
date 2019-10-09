/**
 * 附件
 */
 Ext.define('base.core.Accessory',{
	extend: 'Ext.grid.GridPanel',
	mixins: {
        field: 'Ext.form.field.Field'
    },
    requires: [
        'base.Config'
    ],
	alias: 'widget.accessory',
	fieldLabel:'附件',//标题
	readOnly:false,//是否只读
	allowDisplayImage:false,//是否允许显示图标
	allowMaxSize:5,//允许最大上传附件数量
	autoHeight:true,
	addButtonEnabled : true,
	updateButtonEnabled : true,
	initComponent : function(){
	    var me = this;
	    me.forceFit = true;
	    //me.margins = '2 2 2 2';
	    me.border=0;
	    me.enableColumnMove = false;
	    me.enableColumnResize = false;
	    me.enableHdMenu = false;
	    me.fileUploads = [];
	    me.stripeRows = true;
		me.store = new Ext.data.ArrayStore({
					fields: [
			            {name: 'acceId'},{name: 'acceType'},{name: 'filePath'},{name: 'fileName'},{name:'uploadDate',dateFormat: 'Y-m-d H:i:s'},
			            {name: 'fileSize'},{name: 'showImage'},{name: 'employeeid'},{name:'orderNo'},{name:'itemId'}
			        ]
				});
		me.plugins = [Ext.create('Ext.grid.plugin.CellEditing', {
				clicksToEdit : 1,
				listeners:{
					'beforeedit':function(editor,e, eOpts ){
						if(e.record.data['filePath']==""){
								var rowIndex = me.store.indexOf(e.record);		
								var random = parseInt(Math.random() * 1000000);
								var file = Ext.create('Ext.form.field.File',{
									 name:'fileMap.'+me.name+random,
									 clearOnSubmit:true,
									 buttonText:'浏览',
									 destroy:function(){},
								     onFileChange:function(button, e2, value){
					    	    	        this.lastValue = null;
						                    Ext.form.field.File.superclass.setValue.call(this, value);						                    
						                    var fileName = value.substr(value.lastIndexOf("\\")+1);
						                    e.record.data['filePath']=value;
								        	e.record.data['fileName']=fileName;
								        	e.record.data['acceType']=me.accessoryType;
									 },
									 listeners:{
									 	'render':function(){
									 		/*file.button.fileInputEl.dom.onchange=function(){
									 			alert(this.value);
									 			alert(this.outerHTML);
									 			alert(file.button.fileInputEl.dom.value);
									 			alert(file.button.fileInputEl.dom.outerHTML);
									 			var value = this.value;
									 			file.inputEl.dom.value=value;						                    
						                        var fileName = value.substr(value.lastIndexOf("\\")+1);
						                        e.record.data['filePath']=value;
								        	    e.record.data['fileName']=fileName;
								        	    e.record.data['acceType']=me.accessoryType;
									 		}
									 		//file.fireEvent('change');
									 		file.button.fileInputEl.dom.click();*/
									 	}
									 }
								});
								e.record.data['orderNo']=random;
								e.record.fileField=file;
							e.column.setEditor(e.record.fileField);
							//me.ownerCt.add(e.record.fileField);
							return true;
						}else
						    return false;
					}
				}
			})];
		me.columns = [  new Ext.grid.RowNumberer(),
		                {header: '附件',dataIndex: 'fileName',width:5,sortable:false,
				              renderer:me.renderColumn,editor:{}}, 
				        {header: '大小(K)',sortable:false,width:1,dataIndex:'fileSize'}];
	    if(!me.readOnly)
	         me.selModel = new Ext.selection.CheckboxModel({checkOnly:false});
	    if(!me.readOnly)
		   me.tbar = ["附件要求：文件个数上限"+me.allowMaxSize+",单个文件最大"+
		           base.Config.getProperty("accessory.type."+me.accessoryType+".size")+"k,<br>格式要求"+
		           base.Config.getProperty("accessory.type."+me.accessoryType+".extend").replace(RegExp('\\|','g'),"、")+")"];
		if(!me.readOnly){
			me.tbar.push('->');
			if(me.addButtonEnabled){
		       me.tbar.push({
		            text: '添加附件',
		            iconCls : 'add',
		            handler: function(){
		            	me.addAccessory()
		            }
		        });
			}
		   if(me.updateButtonEnabled){
		       me.tbar.push({
		            text: '删除附件',
		            iconCls : 'remove',
		            handler: function(){
		            	me.removeAccessory();
		            }
		        });
		   }
		}
	    me.callParent(arguments);
	},//调整文件数量
	addAccessory : function(){
		var me = this;
		var rowIndex = me.store.getCount();
		if(rowIndex>=me.allowMaxSize){
		      Ext.Msg.alert('提示', '超过最大上传附件数'+me.allowMaxSize+'！');
		      return;
		 }
		 var p = {
		     acceId:'',acceType:'',filePath:'',fileName:'',uploadDate:'',
		     fileSize:'0',showImage:false,employeeid:'',orderNo:1,itemId:''
		  };
		 me.store.insert(rowIndex, p);
		 me.plugins[0].startEdit(me.store.getAt(rowIndex),me.columns[1]);
	},
	removeAccessory : function(){
		var me = this;
		var selected = me.getSelectionModel().selected;
		for(var i=0;i<selected.getCount();){
	         var record = selected.get(0);
	         if(record.fileField){
	            //Ext.form.field.File.superclass.destroy.call(record.fileField);
	            record.fileField.fireEvent('blur');
	         }
	         me.store.remove(record);
		 }
	},
	getSize:function(){
		return this.store.getCount();
	},
	setValue:function(value){
		var me = this;
		for(var i=0;i<me.store.getCount();i++){
			var record = me.store.getAt(i);		
		}
		me.store.removeAll();
		var index = 0;
		for(a in value)
		if(value[a]){
			me.store.insert(index,value[a]);
			index++;
		}
		/*if(index==1){
			me.hideHeaders();
		}*/
		if(me.readOnly&&index==0){
			me.hide();
		}
	},
	validate:function(value){//数据验证
	    var me = this;
	    var wz = true;
		for(var i=0;i<me.store.getCount();i++){
			var record = me.store.getAt(i);
			if(record.data['filePath']==""){
				wz = false;
				break;
			}
		}
		if(!wz){
		   Ext.Msg.alert('提示', '附件上传不完整！');
		   return false;
		}else
		   return true;
	},//提交数据
	getSubmitData:function(){
		var me = this;
		var datas = new Array();
		for (var i = 0; i < me.store.getCount(); i++) {
		    var record = me.store.getAt(i);
		    datas[datas.length] = record.data;
		}
		var data = {};
        data[me.getName()] = datas;
        return data;
	},//渲染
	renderColumn:function(value,cellmeta,record,rowIndex,columnIndex,store){
		var me = this;
	    var url = location.href;
		var match = url.split(RegExp("[?&]"));
        var basePath = match[0].substr(0,match[0].indexOf("/",match[0].indexOf("/",7)+1));
		var clientBasePath = basePath;
		if(me.ownerCt.parentWindow&&me.ownerCt.parentWindow.clientBasePath)
		    clientBasePath = me.ownerCt.parentWindow.clientBasePath;
		if(record.data['acceId']=="")
		   value = record.data['filePath'];
		else
		   value = "<a href='"+clientBasePath+"/baseinfo/affiche/outputFileForAfficheFormAction!outputFile.action?id="+
		      record.data['acceId']+"' target='_new'>"+value+"</a>";
		if(value=="") value="请点击选择附件！";
		return value;
	}
});
/**
 * 等待提示框
 */
function EapMaskTip(target){
	var tip = new Ext.LoadMask(target,{
	   msg: '正在处理中,请稍后!',
	   removeMask: false //完成后移除
	});
	tip.show();
	return tip;
}