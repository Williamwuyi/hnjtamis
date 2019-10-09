/**
 * 树选择页面
 */
Ext.define('base.model.Tree', {
    extend : Ext.tree.TreePanel,
    keyColumnName:'id',//主健字段名(树形结构的）
    titleColumnName:'name',//标题字段名（树形显示的字段）
    childColumnName:'childrens',//子集合字段名（树形结构的)
    editable:false,//不可编辑值
    selectUrl:'',// 后台请求地址
    autoScroll : true,
    rootVisible: false,
    /**
     * 选中上下级复选级联操作，第一位代表向下，每二位代表向上，
     * 分别由0表示不处理，1表示选中或取消级联直属上下级，
     * 2表示选中或取消级联所有上下级，3表示只选中级联直属上下级，
     * 4表示只选中级联所有上下级，5表示只取消级联直属上下级，
     * 6表示只取消级联所有上下级
     */
    levelAffect:'20',
    selectObject : {},
    callback:Ext.emptyFn,
    displaySelectTextArea:null,
    allExpanded:true,//是否全部展开
    statics:{
    	openWin:function(config,callback){
    		config.callback = callback;
    		var tree = ClassCreate('base.model.Tree',config);
    		//delete tree.height;
    		tree.height='100%';
    		tree.flex=2;
    		var winConfig = {
					layout : {type:'hbox'},
					title : config.title,
					height : config.height,
					width : config.width,
					border : false,
					frame : false,
					modal : true,// 模态
					closeAction : 'hide'
				};
		    winConfig.tbar = [];
			winConfig.tbar.push({name : 'nameTerm',xtype:'textfield',labelAlign : 'right',labelWidth : 30,width:180,emptyText:eap_tree_filterterm_emptyText});
			winConfig.tbar.push(Ext.create("Ext.Button", {iconCls : 'query',text : '',
										handler : function() {
											tree.store.proxy.url=tree.selectUrl;
											tree.store.proxy.extraParams = {nameTerm:this.previousSibling().value};
											tree.store.load();
										}
			}));
			if(tree.checked){
				//确认选择
				winConfig.tbar.push(Ext.create("Ext.Button", {iconCls : 'confirm',text : eap_operate_ok,
						  handler : function() {
						  	 if(tree.callback){
						  	 	var ids = null;
						  	 	Ext.Array.each(tree.selectObject,function(c){
						  	 		if(ids==null)
						  	 		   ids = c[tree.keyColumnName];
						  	 	    else
									   ids +=","+c[tree.keyColumnName];
								});
							    tree.callback(ids,tree.selectObject);
						  	 }
							 tree.ownerCt.close();
						  }
				}));
				winConfig.tbar.push(Ext.create("Ext.Button", {text : eap_operate_all_select,
						  handler : function() {
						  	  var old_levelAffect = tree.levelAffect;
							  tree.levelAffect = '20';
						  	  if(this.text==eap_operate_all_select){
							  	  this.setText(eap_operate_all_clear);
								  Ext.Array.each(tree.getRootNode().childNodes,function(c){
										c.set('checked',true);
										tree.checkEventFun(c,true,0,0);
								  });														
						  	  }else{
						      	 Ext.Array.each(tree.getView().getChecked(),function(item){
						      	 	item.set('checked',false);
						      	 });
						      	 tree.selectObject=[];
						      	 this.setText(eap_operate_all_select);
						      }
							  tree.levelAffect = old_levelAffect;
							  tree.initSelectTextArea();
						  }
				}));
				winConfig.tbar.push(Ext.create("Ext.Button", {text : eap_operate_back_select,
									  handler : function() {
									  	  tree.backSelect(null,false);
									  	  tree.initSelectTextArea();
									  }
				}));
			}
			if(!tree.checked)
			winConfig.tbar.push(Ext.create("Ext.Button", {iconCls : 'close',text : '',
									  handler : function() {
									  	   if(tree.checked){
											  tree.setValue([]);
											  tree.selectEventFun([]);
									  	   }else{
										      tree.setValue({});
										      tree.selectEventFun({});
									  	   }
									  }
			}));
		    if(config.checked){
		    	tree.displaySelectTextArea = Ext.widget('textarea',
		        	{emptyText:eap_tree_select_emptyText,readOnly:true,
		        	 flex:1.5,grow:true,height:'100%'});
		        winConfig.items = [tree,tree.displaySelectTextArea];
		    }else
		        winConfig.items = [tree];
    		var formWin = new WindowObject(winConfig);
		    formWin.show();
    	}
    },//把选择的数据考到文本域中
    initSelectTextArea : function(){
    	var self = this;
    	if(self.displaySelectTextArea){
    		var text="";
	    	Ext.Array.each(self.getView().getChecked(),function(item){
	    		if(self.selectType&&item.get("type")==self.selectType)
				  text+=item.get(self.titleColumnName)+"\n";
			});
			self.displaySelectTextArea.setValue(text);
    	}
    },
    initComponent : function() {
        var self = this;
        self.title="";
        var fields = [self.keyColumnName,self.titleColumnName,'errors','type'];
        self.displayField = self.titleColumnName;
        self.store =  new Ext.data.TreeStore({
			autoLoad : false,
			fields : fields,
			proxy : {
				type : 'ajax',
				actionMethods : "GET",
				url : self.selectUrl,
				extraParams:{name:'type'},
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
	        var record = node.childNodes[0];
			if (record) {
				var errorinfo = record.data["errors"];
				if (errorinfo != undefined && errorinfo != ""){
					Ext.MessageBox.alert("错误提示！", errorinfo);
					node.childNodes[0].remove();//删除此结点
				}
			}
			//初始化结点状态
		    function initCheckState(list){
		    	for(var i=0;i<list.length;i++){
		    		var item = list[i].data;
		    		//item.expanded=true;
		    		if(!self.checked){
		    			item.checked = null;
		    		}else{//设置复选框状态
						var find = false;
	    				Ext.Array.each(self.selectObject, function(r) {
	    					var indexNo = Ext.Array.indexOf(self.selectObject,r);
							if(item[self.keyColumnName]==r[self.keyColumnName]
							   ||item[self.keyColumnName]==r){
								find = true;
								if(!Ext.isObject(r)){
								   self.selectObject[indexNo]={};
								   self.selectObject[indexNo][self.keyColumnName]=item[self.keyColumnName];
								   self.selectObject[indexNo][self.titleColumnName]=item[self.titleColumnName];
								}
							}
						});
						if(!find&&item.checked){
						   find = true;
						   var data={};
						   data[self.keyColumnName]=item[self.keyColumnName];
						   data[self.titleColumnName]=item[self.titleColumnName];
						   self.selectObject.push(data);
						}
						list[i].set("checked", find);
		    			//if(list[i].childNodes.length==0)
		    			  //item.leaf = true;
		    		}
		    		if(list[i].childNodes.length>0)
		    		   list[i].data.expanded=true;
		    		initCheckState(list[i].childNodes);
		    	}
		    }
		    initCheckState(node.childNodes);
		    if(self.displaySelectTextArea&&self.displaySelectTextArea.getValue()=="")
		       self.initSelectTextArea();
		});
        self.on('checkChange',function(item,checked){//复选框事件
             if(Ext.isObject(self.selectObject))
    		     self.selectObject = [];
             self.checkEventFun(item,checked,0,0);
             //Ext.EventObjectImpl.stopEvent();//停止事件向上转递
             self.initSelectTextArea();
        });
        self.on('itemclick', function (view, record) {
        	if(!self.checked){//单选方式
        	    if(self.selectType&&record.get("type")!=self.selectType){
	        		Ext.Msg.alert('提示', '请选择'+self.selectTypeName+'类型！');
	        		return;
	        	}
        		self.selectObject = {};
	        	self.selectObject[self.titleColumnName] = record.get(self.titleColumnName);
	        	self.selectObject[self.keyColumnName] = record.get(self.keyColumnName);
	        	if(self.callback)
				    self.callback(record.get(self.keyColumnName),self.selectObject);
				 self.ownerCt.close();
        	}
        });
        
        self.callParent();
        self.store.getRootNode().expand(false,false);
        var contextmenu = new Ext.menu.Menu({
        	items:[{
        	   text:eap_operate_back_select,
        	   handler:function(){
        	   	   self.backSelect(contextmenu.node,false);
        	   	   self.initSelectTextArea();
        	   }
        	}]
        });
        self.on('itemcontextmenu',function(view,record,item,index,e){
            e.stopPropagation();
            e.preventDefault();
            contextmenu.node = record;
            contextmenu.showAt(e.getXY());
        });
        self.on('beforeitemexpand',function(node,opts){
            self.store.proxy.extraParams.parentType=node.data.type;
        });
    },//反选方法
    backSelect:function(node,iterationed){
    	var self = this;
    	if(!self.old_levelAffect){
    	   self.old_levelAffect = self.levelAffect;
		   self.levelAffect = '00';
    	}
    	if(!node||node==null){
    	    node = self.getRootNode();
    	}
    	Ext.Array.each(node.childNodes,function(c){
			c.set('checked',!c.data['checked']);
			if(!self.selectType||self.selectType==c.data['type']){
				if(c.data['checked']){
				    var v = {};
		    		v[self.titleColumnName] = c.data[self.titleColumnName];
		        	v[self.keyColumnName] = c.data[self.keyColumnName];
		    		self.selectObject.push(v);
				}else{
					for(var i=0;i<self.selectObject.length;i++) {
			        	var r = self.selectObject[i];
						if (c.data[self.keyColumnName]==r[self.keyColumnName]){
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
                 names.push(rec[self.titleColumnName]);
             });
			self.levelAffect = self.old_levelAffect;
			delete self.old_levelAffect;
			node.set('checked',!node.data['checked']);
		}
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
						if (item.data[self.keyColumnName]==r[self.keyColumnName]){
							find = true;
							break;
						}
					}
					if(!find){
						var v = {};
			    		v[self.titleColumnName] = item.data[self.titleColumnName];
			        	v[self.keyColumnName] = item.data[self.keyColumnName];
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
						if (item.data[self.keyColumnName]==r[self.keyColumnName]){
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
		     //self.selectEventFun(self.selectObject);
		     var names = [];
		     Ext.Array.each(self.selectObject, function(rec) {
                    names.push(rec[self.titleColumnName]);
             });
             //self.setRawValue(names.join(self.decollator));
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