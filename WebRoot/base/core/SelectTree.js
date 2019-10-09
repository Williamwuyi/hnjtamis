/**
 * 下拉树控件
 */
Ext.define('base.core.SelectTree', {
    extend : Ext.form.field.Picker,
    mixins: {
        field: 'Ext.form.field.Field'
    },
    alias: 'widget.selecttree', 
    requires : [Ext.tree.Panel], 
    tree: {},
    store :{},
    checked:false,//是否可复选
    nameKey : 'id',
    nameLable : 'name',
    keyColumnName:'id',//主健字段名(树形结构的）
    titleColumnName:'name',//标题字段名（树形显示的字段）
    childColumnName:'childrens',//子集合字段名（树形结构的)
    editable:false,//不可编辑值
    selectUrl:'',// 后台请求地址
    selectEventFun:Ext.emptyFn,
    addPickerWidth:0,//下拉框增加的宽度 ，缺省为0
    stepLoad:true,//是否分级加载
    config: { 
        maxPickerWidth: 200, 
        maxPickerHeight: 200, 
        minPickerHeight: 100
    },
    /**
     * 选中上下级复选级联操作，第一位代表向下，每二位代表向上，
     * 分别由0表示不处理，1表示选中或取消级联直属上下级，
     * 2表示选中或取消级联所有上下级，3表示只选中级联直属上下级，
     * 4表示只选中级联所有上下级，5表示只取消级联直属上下级，
     * 6表示只取消级联所有上下级
     */
    levelAffect:'20',
    decollator:',',//分割符 
    selectObject : {},
    editorType : 'object',//编辑类型，分对象(object)或数据标识(str)、显示值(title)，缺省为对象
    expandModel : 'select',//展开模式，select为选择的展开，all为所有展开
    initComponent : function() {
        var self = this;
        if(self.checked&&!Ext.isArray(self.selectObject)) 
            self.selectObject = [];
        var fields = [self.keyColumnName,self.titleColumnName,'errors','type'];
        self.store =  new Ext.data.TreeStore({
			autoLoad : false,
			fields : fields,
			proxy : {
				type : 'ajax',
				actionMethods : "GET",
				url : self.selectUrl,// 后台请求地址
				reader : {
					type : 'json',
					root : self.jsonParemeterName?self.jsonParemeterName:self.readerRoot,
					idProperty:self.keyColumnName
				}
			},
			nodeParam:'parentId',
			defaultRootId:'',
			defaultRootProperty:self.childColumnName,
			rootProperty:self.childColumnName
		});
		//监听后台查询方法，有错误就提示！
		self.store.on("load", function(mystore,node,success) {
			/*if(!self.dynamicLoading){
				Ext.Array.each(node.childNodes,function(c){
					if(self.checked){
						if(c.data['checked']==undefined)
						   c.data['checked'] = false;
					}
					c.data['leaf'] = true;
				});
			}*/
	        var record = node.childNodes[0];
			if (record) {
				var errorinfo = record.data["errors"];
				if (errorinfo != undefined && errorinfo != ""){
					Ext.MessageBox.alert("错误提示！", errorinfo);
					node.childNodes[0].remove();//删除此结点
				}
			}
			var v = null;
			if(Ext.isObject(self.selectObject)){
			    v = self.selectObject[self.nameKey];
			}
			self.iteraterSetRaxValue(node.childNodes,v);			
			self.tree.getView().refresh();
			self.setValue(self.value);
		});
		var treeTbar = [];
		self.nameTermWidth = self.nameTermWidth || 130;
		treeTbar.push({name : 'nameTerm',xtype:'textfield',labelAlign : 'right',labelWidth : 30,width:self.nameTermWidth});
		treeTbar.push(Ext.create("Ext.Button", {iconCls : 'query',text : '',
									handler : function() {
										self.store.proxy.extraParams = {nameTerm:this.previousSibling().value};
										self.store.load();
									}
		}));
		if(self.checked){
			treeTbar.push(Ext.create("Ext.Button", {iconCls : 'confirm',text : '',
									  handler : function() {
											self.collapse();
									  }
			}));
			treeTbar.push(Ext.create("Ext.Button", {text : '全选',
									  handler : function() {
									  	  var old_levelAffect = self.levelAffect;
										  self.levelAffect = '20';
									  	  if(this.text=='全选'){
										  	  this.setText('全消');
											  Ext.Array.each(self.tree.getRootNode().childNodes,function(c){
													c.set('checked',true);
													self.checkEventFun(c,true,0,0);
											  });														
									  	  }else{
									      	 Ext.Array.each(self.picker.getView().getChecked(),function(item){
									      	 	item.set('checked',false);
									      	 });
									      	 self.setRawValue("");
									      	 self.selectObject=[];
									      	 this.setText('全选');
									      }
										  self.levelAffect = old_levelAffect;
									  }
			}));
			treeTbar.push(Ext.create("Ext.Button", {text : '反选',
									  handler : function() {
									  	  self.backSelect(null,false);
									  }
			}));
		}
		if(self.allowBlank)
		treeTbar.push(Ext.create("Ext.Button", {iconCls : 'close',text : '',
								  handler : function() {
								  	   if(self.checked){
										  self.selectEventFun([]);
										  self.selectObject = [];
								  	   }else{
									      self.selectEventFun({});
									      self.selectObject = {};
								  	   }
								  	   self.setRawValue(null);
								  }
		}));
        self.tree= new Ext.tree.Panel({
            rootVisible: false, 
            width: self.maxPickerWidth, 
            height:self.maxPickerHeight,
            autoScroll : true,
            floating : true,
            focusOnToFront : false,
            shadow : false,
            useArrows : true,
            store : self.store,
            lines : true,
            tbar : treeTbar,
            displayField:self.titleColumnName
        });
        self.callParent();
        self.tree.on('checkChange',function(item,checked){//复选框事件
             if(Ext.isObject(self.selectObject))
    		     self.selectObject = [];
             self.checkEventFun(item,checked,0,0);
             //Ext.EventObjectImpl.stopEvent();//停止事件向上转递
        });
        self.tree.on('beforeitemexpand',function(node,opts){
            self.tree.store.proxy.extraParams.parentType=node.data.type;
        });
        self.tree.on('itemclick', function (view, record) {
        	if(!self.checked){//单选方式
        	    if(self.selectType&&record.get("type")!=self.selectType){
	        		Ext.Msg.alert('提示', '请选择'+self.selectTypeName+'类型！');
	        		return;
	        	}
        		self.selectObject = {};
	        	self.selectObject[self.nameLable] = record.get(self.titleColumnName);
	        	self.selectObject[self.nameKey] = record.get(self.keyColumnName);
	        	self.setRawValue(record.get(self.titleColumnName));
	            self.collapse();//self.picker.hide();
	            self.selectEventFun({'value':self.selectObject[self.nameKey],
	                 'title':self.selectObject[self.nameLable],'type':record.get("type")});
        	}//else
        	    //Ext.EventObjectImpl.stopPropagation();//停止事件向上转递
        });
        var contextmenu = new Ext.menu.Menu({
        	items:[{
        	   text:'反选',
        	   handler:function(){
        	   	   self.backSelect(contextmenu.node,false);
        	   	   self.picker.hide = self.picker.oldHideFun;
        	   }
        	}]
        });
        self.tree.on('itemcontextmenu',function(view,record,item,index,e){
            e.stopPropagation();
            e.preventDefault();
            contextmenu.node = record;
            contextmenu.showAt(e.getXY());
            self.picker.oldHideFun = self.picker.hide;
            self.picker.hide = function(){
            	this.callParent();
            };
        });
    },//反选方法
    backSelect:function(node,iterationed){
    	var self = this;
    	if(!self.old_levelAffect){
    	   self.old_levelAffect = self.levelAffect;
		   self.levelAffect = '00';
    	}
    	if(!node||node==null){
    	    node = self.tree.getRootNode();
    	}
    	Ext.Array.each(node.childNodes,function(c){
			c.set('checked',!c.data['checked']);
			if(!self.selectType||self.selectType==c.data['type']){
				if(c.data['checked']){
				    var v = {};
		    		v[self.nameLable] = c.data[self.titleColumnName];
		        	v[self.nameKey] = c.data[self.keyColumnName];
		    		self.selectObject.push(v);
				}else{
					for(var i=0;i<self.selectObject.length;i++) {
			        	var r = self.selectObject[i];
						if (c.data[self.keyColumnName]==r[self.nameKey]){
							self.selectObject.splice(i,1);
							break;
						}
					}
				}
	 		}
	 		self.backSelect(c,true);
		});	
		if(!iterationed){
			var names = [];
		     Ext.Array.each(self.selectObject, function(rec) {
                    names.push(rec[self.nameLable]);
             });
			//self.setValue(self.selectObject);
			self.setRawValue(names.join(self.decollator));
			self.levelAffect = self.old_levelAffect;
			delete self.old_levelAffect;
			node.set('checked',!node.data['checked']);
		}
    },//下拉展开事件
    expand:function(){
    	var self = this;
    	//if(self.checked)
	    self.iteraterSetRaxValue(self.tree.getRootNode().childNodes,null);
	    self.callParent();
	    self.tree.getView().refresh();
    },
    reflash:function(url,clear){
    	var self = this;
    	self.store.proxy.url = url;
    	self.store.load();
    	if(clear==undefined||clear==true)
    	   self.setValue(null);
    },
    /**
     * 结点复选选中方法
     * item 结点对象
     * checked 选中还是取消
     * iter 迭代次数
     * checkType 复选类型，为0时表示选中或取消都处理，
     * 为1只处理选中的，为-1只处理取消的，为级联操作使用
     */
    checkEventFun:function(item,checked,iter,checkType){
    	 var self = this;
    	 if(item.data['checked']==undefined||(iter!=0&&checked==item.data['checked'])) return;
		 if(checked){
		 	if(checkType==0||checkType==1){
		 		if(!self.selectType||self.selectType==item.data['type']){
		 			var find = false;
		 			for(var i=0;i<self.selectObject.length;i++) {
			        	var r = self.selectObject[i];
						if (item.data[self.keyColumnName]==r[self.nameKey]){
							find = true;
							break;
						}
					}
					if(!find){
						var v = {};
			    		v[self.nameLable] = item.data[self.titleColumnName];
			        	v[self.nameKey] = item.data[self.keyColumnName];
			    		self.selectObject.push(v);
					}
		 		}
		 		if(iter!=0) item.set("checked", true);
		 	}
		 }else{
		 	if(checkType==0||checkType==-1){
		 		if(!self.selectType||self.selectType==item.data['type']){
					for(var i=0;i<self.selectObject.length;i++) {
			        	var r = self.selectObject[i];
						if (item.data[self.keyColumnName]==r[self.nameKey]){
							self.selectObject.splice(i,1);
							break;
						}
					}
		 		}
		 		if(iter!=0) item.set("checked", false);
		 	}
		 }
		 var iterUp = iter;
		 if(iterUp>=0){
			 var buttomAffect = parseInt(self.levelAffect.substr(0,1));//向下
			 if(buttomAffect!=0&&((buttomAffect%2==1&&iterUp==0)||buttomAffect%2==0)){
			 	 iterUp++;
	             Ext.Array.each(item.childNodes, function(r) {
					 self.checkEventFun(r,checked,iterUp,
					 ((buttomAffect==1||buttomAffect==2)?0:((buttomAffect==3||buttomAffect==4)?1:-1)));
				 });	
	         }
		 }
		 var iterButtom = iter;
		 if(iterButtom<=0){
	         var topAffect = parseInt(self.levelAffect.substr(1)); //向上
			 if(topAffect!=0&&((topAffect%2==1&&iterButtom==0)||topAffect%2==0)){
			      self.checkEventFun(item.parentNode,checked,iterButtom--,
			      ((topAffect==1||topAffect==2)?0:((topAffect==3||topAffect==4)?1:-1)));
	         }
		 }
         if(iter==0){
		     //self.setValue(self.selectObject);
		     self.selectEventFun(self.selectObject);
		     var names = [];
		     Ext.Array.each(self.selectObject, function(rec) {
                names.push(rec[self.nameLable]);
             });
             self.setRawValue(names.join(self.decollator));
         }
    },//根据VALUE获取TEXT
    getTextByValue:function(values){
    	var self = this;
    	var texts = new Array(values.length);    	 
    	var node = self.tree.getRootNode();
    	function getTextByValueLoop(node,values,texts){
	    	Ext.Array.each(node.childNodes,function(c){
	    		for(var x=0;x<values.length;x++){
	    			if(values[x]==c.data[self.keyColumnName])
	    			   texts[x]=c.data[self.titleColumnName];
	    		}
	    		getTextByValueLoop(c,values,texts);
	    	});
    	}
    	getTextByValueLoop(node,values,texts);
    	return texts;
    },//根据TEXT获取VALUE
    getValueByText:function(texts){
    	var self = this;
    	var values = new Array(texts.length);    	 
    	var node = self.tree.getRootNode();
    	function getValueByTextLoop(node,values,texts){
	    	Ext.Array.each(node.childNodes,function(c){
	    		for(var x=0;x<texts.length;x++){
	    			if(texts[x]==c.data[self.titleColumnName])
	    			   values[x]=c.data[self.keyColumnName];
	    		}
	    		getValueByTextLoop(c,values,texts);
	    	});
    	}
    	getValueByTextLoop(node,values,texts);
    	return values;
    },//设置值
    setValue:function(value){
    	var self = this;
    	self.value = value;
    	if(self.checked) 
		  self.selectObject=[];
		else 
		  self.selectObject={};
    	if(!value||value==''){
    		if(self.readOnly&&self.bodyEl)
		       self.bodyEl.dom.innerHTML='';
    	    return;
    	}
    	if(value==undefined){
    		if(self.readOnly&&self.bodyEl)
		       self.bodyEl.dom.innerHTML='';
    		self.setRawValue("");
    		self.value = null;
    		return;
    	}
    	if(self.checked&&value.length==0){
    		if(self.readOnly&&self.bodyEl)
		       self.bodyEl.dom.innerHTML='';
    		self.setRawValue("");
    		self.value = null;
    		return;
    	}
    	if(!self.checked){//单选
    		if(self.editorType=='str'||self.editorType=='string'){
	    	    var v = {};
	    	    if(Ext.isObject(value))
	    	       value = value[self.nameKey];
	    	    v[self.nameKey]=value;
	    	    v[self.nameLable]=self.getTextByValue([value])[0];
	    	    self.setRawValue(v[self.nameLable]);
	    	    self.selectObject =  v;
			}else if(self.editorType=='title'||self.editorType=='titles'){
	    	    var v = {};
	    	    v[self.nameLable]=value;
	    	    v[self.nameKey]=self.getValueByText([value])[0];
	    	    self.setRawValue(v[self.nameLable]);
	    	    self.selectObject = v;
			}else{
				if(value[self.nameLable]==undefined||value[self.nameLable]==''){
				   value[self.nameLable]=self.getTextByValue([value[self.nameKey]])[0];
				}
				self.selectObject = value;
				self.setRawValue(value[self.nameLable]);
			}
    	}else{//多选
	    	if(self.editorType=='str'){//ID数组类型	    		
	    		if(!Ext.isArray(value)) value = [value];
	    		var names = null;
	    		Ext.Array.each(value,function(item){
	    			var v = {};
		    	    v[self.nameKey]=item;
		    	    v[self.nameLable]=self.getTextByValue([item])[0];
		    	    self.selectObject.push(v);
		    	    if(names==null)
		    	      names = v[self.nameLable];
		    	    else
		    	      names+=self.decollator+v[self.nameLable];
	    		});
	    		self.setRawValue(names);
			}else if(self.editorType=='string'){//ID字符串类型				
	    		var vs = value.split(self.decollator);
	    	    var names = null;
	    		Ext.Array.each(vs,function(item){
	    			var v = {};
		    	    v[self.nameKey]=item;
		    	    v[self.nameLable]=self.getTextByValue([item])[0];
		    	    self.selectObject.push(v);
		    	    //console.log(v);
		    	    if(names==null)
		    	      names = v[self.nameLable];
		    	    else
		    	      names+=self.decollator+v[self.nameLable];
	    		});
	    		//console.log(names);
	    		self.setRawValue(names);
			}else if(self.editorType=='title'){//名称字符串
	    	    var v = {};
	    	    var vs = value.split(self.decollator);
	    	    var names = null;
	    		Ext.Array.each(vs,function(item){
	    			var v = {};
		    	    v[self.nameKey]=self.getValueByText([item])[0];
		    	    v[self.nameLable]=item;
		    	    self.selectObject.push(v);
		    	    if(names==null)
		    	      names = v[self.nameLable];
		    	    else
		    	      names+=self.decollator+v[self.nameLable];
	    		});
	    		self.setRawValue(names);
			}else if(self.editorType=='titles'){//名称数组
	    	    var v = {};
	    		Ext.Array.each(value,function(item){
	    			var v = {};
		    	    v[self.nameKey]=self.getValueByText([item])[0];
		    	    v[self.nameLable]=item;
		    	    self.selectObject.push(v);
	    		});
	    		self.setRawValue(value);
			}else{//对象数组
				if(!Ext.isArray(value)) value = [value];
				var rv = "";
				if(value[0][self.nameLable]==undefined||value[0][self.nameLable]==''){
					var ids = [];
					for(var i=0;i<value.length;i++){
						ids.push(value[i][self.nameKey]);
					}
					var names = self.getTextByValue(ids);
					for(var i=0;i<names.length;i++){
						value[i][self.nameLable]=names[i];
					}
				    rv=names.join(self.decollator);
				}else
				for(var i=0;i<value.length;i++){
					if(i==0)
					  rv = value[i][self.nameLable];
					else
					  rv+=self.decollator+value[i][self.nameLable];
				}
				self.selectObject = value;
				self.setRawValue(rv);
			}
    	}
    	if(self.readOnly&&self.bodyEl)
		   self.bodyEl.dom.innerHTML='<div class="x-form-display-field" aria-invalid="false" data-errorqtip="">'+self.getRawValue()+"</div>";
    },//针对设置时，只加了ID，没有显示值 时，在load事件中进行设置
    iteraterSetRaxValue:function(list,v){
    	var self = this;
    	var haveExpaned = false;
    	for(var i=0;i<list.length;i++){
    		var item = list[i].data;
    		if(v&&self.selectObjectParentFinded){
    			self.setRawValue(item[self.titleColumnName]);
	    	    self.selectObject[self.nameLable] = item[self.titleColumnName];
	    	    self.selectObject[self.nameKey] = item[self.keyColumnName];
	    	    delete self.selectObjectParentFinded;
	    	    return;
    		}
    		if(v&&item[self.keyColumnName]==v){
    			if(!self.selectType||item.type==self.selectType){
	    			self.setRawValue(item[self.titleColumnName]);
		    	    self.selectObject[self.nameLable] = item[self.titleColumnName];
		    	    self.selectObject[self.nameKey] = item[self.keyColumnName];
		    	    return;
    			}else
    			  self.selectObjectParentFinded = true;
    		}
    		if(!self.checked){
    			item.checked = null;
    		}else{//设置复选框状态
    		    var find = false;
    			if(Ext.isArray(self.selectObject)){    				
    				Ext.Array.each(self.selectObject, function(r) {
						if(r[self.nameKey]&&item[self.keyColumnName]==r[self.nameKey])
							find = true;
						if(!r[self.nameKey]&&item[self.titleColumnName]==r[self.nameLable])
							find = true;
					});					
    			}
    			item.checked=find;
    			if(list[i].childNodes.length==0&&!self.stepLoad)
    			   item.leaf = true;
    		}
    		var expand = self.iteraterSetRaxValue(list[i].childNodes,v);
    		if(list[i].childNodes.length==0){
    			if(self.expandModel=='all')
    			   expand = true;
    			else if(self.checked)
    		       expand = item.checked;
    		    else
    		       expand = (item[self.keyColumnName]==self.selectObject[self.nameKey]);
    		}else if(expand)
    		  list[i].expand();
    		if(expand)    		   
    		   haveExpaned = true;
    	}
    	return haveExpaned;
    },
    getValue: function() {
        var self = this;
        if(self.editorType=='object')
           return self.selectObject;
        else if(self.editorType=='str'){//ID数组类型
           if(!self.checked) return self.selectObject[self.nameKey];
           var rs = [];
           Ext.Array.each(self.selectObject,function(item){
           	  rs.push(item[self.nameKey]);
           });
           return rs;
        }else if(self.editorType=='string'){//ID字符串类型
          if(!self.checked) return self.selectObject[self.nameKey];
           var rs = null;
           Ext.Array.each(self.selectObject,function(item){
           	  if(rs==null)
           	     rs=item[self.nameKey];
           	  else
           	     rs+=self.decollator+item[self.nameKey];
           });
           return rs;
        }else if(self.editorType=='title'){//名称字符串
           if(!self.checked) return self.selectObject[self.nameLable];
           var rs = null;
           Ext.Array.each(self.selectObject,function(item){
           	  if(rs==null)
           	     rs=item[self.nameLable];
           	  else
           	     rs+=self.decollator+item[self.nameLable];
           });
           return rs;
        }else{//名称数组
           if(!self.checked) return self.selectObject[self.nameLable];
           var rs = [];
           Ext.Array.each(self.selectObject,function(item){
           	  rs.push(item[self.nameLable]);
           });
           return rs;
        }
    },//提交数据
	getSubmitData:function(){
		var self = this;
		var data = {};
		data[self.getName()]=self.getValue();
		return data;
	},//创建下拉窗口
    createPicker : function() {
        var self = this;
        self.picker = self.tree;
        /*self.picker.on({
            checkchange : function() {
                var records = self.picker.getView().getChecked(), names = [], values = [];
                Ext.Array.each(records, function(rec) {
                    names.push(rec.get(self.titleColumnName));//rec.get('text')
                    values.push(rec.get(self.keyColumnName));
                });
                self.setRawValue(names.join(self.decollator));// 隐藏值
                //self.setValue(values.join(decollator));// 显示值
                if(!self.checked)
                  self.picker.hide();                 //[目前单选,该批次代码、tree的itemclick事件去掉则多选]
                Ext.Array.each(records, function(record) {  //[目前单选,该批次代码、tree的itemclick事件去掉则多选]
                    record.set('checked', false);       //[目前单选,该批次代码、tree的itemclick事件去掉则多选]
                });                   //[目前单选,该批次代码、tree的itemclick事件去掉则多选]
            }
        });**/
        return self.picker;
    },
    alignPicker : function() {
        var me = this, picker, isAbove, aboveSfx = '-above';
        if (this.isExpanded) {
            picker = me.getPicker();
            if (me.matchFieldWidth) {
                picker.setWidth(me.bodyEl.getWidth()+me.addPickerWidth);
            }
            if (picker.isFloating()) {
            	me.doAlign();
            	/*me.pickerOffset = 100;
                picker.alignTo(me.inputEl,'bl' , me.pickerOffset);// ->tl
                isAbove = picker.el.getY() < me.inputEl.getY();
                me.bodyEl[isAbove ? 'addCls' : 'removeCls'](me.openCls+ aboveSfx);
                picker.el[isAbove ? 'addCls' : 'removeCls'](picker.baseCls + aboveSfx);*/
            }
        }
    },
    collapse:function(){
       //alert(1);
       this.callParent(arguments);
    },
    collapseIf: function(e) {
    	//alert(1);
    	this.callParent(arguments);
    }
});