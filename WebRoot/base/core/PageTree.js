/**
 * 页面树控件
 */
Ext.define('base.core.PageTree', {
    extend : 'Ext.tree.TreePanel',
    rootVisible: false,
    initComponent : function() {
        var self = this;
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
			root:{text:'我是根'},
			nodeParam:'parentId',
			defaultRootId:'',
			defaultRootProperty:self.childColumnName,
			rootProperty:self.childColumnName
		});
		//监听后台查询方法，有错误就提示！
		self.store.on("load", function(mystore,node,success) {
			if(node.childNodes.length==0){
				Ext.MessageBox.alert("错误提示！", "没有找到任何数据！",function(){
					self.ownerCt.close();
				});				
				retrun;
			}
	        var record = node.childNodes[0];
			if (record) {
				var errorinfo = record.data["errors"];
				if (errorinfo != undefined && errorinfo != ""){
					Ext.MessageBox.alert("错误提示！", errorinfo);
					node.childNodes[0].remove();//删除此结点
					return;
				}
			}
		});
		self.displayField = self.titleColumnName;//显示的字段名
		if(self.queryEnabled)
        self.tbar = [{name : 'nameTerm',xtype:'textfield',labelAlign : 'right',labelWidth : 30,width:100},
                    Ext.create("Ext.Button", {iconCls : 'query',text : '',
												handler : function() {
													self.store.proxy.extraParams = {nameTerm:this.previousSibling().value};
													self.store.load();
												}
					}),
					Ext.create("Ext.Button", {iconCls : 'confirm',text : '',
					                          style:('display:'+(self.checked?"block":"none")),
											  handler : function() {
												self.collapse();
											  }
					})];
        /**
         * 复选框事件
         */
        self.on('checkChange',function(item,checked){
             if(Ext.isObject(self.selectObject))
    		     self.selectObject = [];
             self.checkEventFun(item,checked,0,0);
        });
        /**
         * 单选事件
         */
        self.on('itemclick', function (view, record) {
        	if(self.selectType&&record.get("type")!=self.selectType){
        		Ext.Msg.alert('提示', '请选择'+self.selectTypeName+'类型！');
        		return;
        	}
        	if(!self.checked){
        		self.selectObject = {};
	        	self.selectObject[self.nameLable] = record.get(self.titleColumnName);
	        	self.selectObject[self.nameKey] = record.get(self.keyColumnName);
	        	self.setValue(self.selectObject);
	            self.selectEventFun({'value':self.selectObject[self.nameKey],
	                 'title':self.selectObject[self.nameLable],'type':record.get("type")});
        	}
        });
        /**
         * 右键菜单
         */
        self.on('itemcontextmenu',function(view,record,item,index,e){
        	e.preventDefault();
        	var contextmenu = new Ext.menu.Menu({
        		items : [{
		        			text:eap_operate_back_select,
		        			handler : function(){
		        				var rootNode = self.getRootNode;
		        				rootNode.data.checked = !rootNode.data.checked;
		        				Ext.Array.each(rootNode.childNodes, function(r) {
									 r.data.checked = !r.data.checked;
								});
		        			}
		        		}]
        	});
        	contextmenu.showAt(e.getXY());
        });
        self.callParent();
        self.store.getRootNode().expand(true,false);
    },
    reflash:function(url){
    	var self = this;
    	self.store.proxy.url = url;
    	self.store.load();
    },
    /**
     * 结点复选选中取消方法
     * item 结点对象
     * checked 选中还是取消
     * iter 迭代次数
     * checkType 复选类型，为0时表示选中或取消都处理，
     * 为1只处理选中的，为-1只处理取消的，为级联操作使用
     */
    checkEventFun:function(item,checked,iter,checkType){
    	 var self = this;
    	 if(item.data['checked']==undefined) return;
    	 if(iter!=0) item.data['checked'] = checked;
		 if(checked){
		 	if(checkType==0||checkType==1){
				var v = {};
	    		v[self.nameLable] = item.data[self.titleColumnName];
	        	v[self.nameKey] = item.data[self.keyColumnName];
	    		self.selectObject.push(v);
		 	}
		 }else{
		 	if(checkType==0||checkType==-1){
		        for(var i=0;i<self.selectObject.length;i++) {
		        	var r = self.selectObject[i];
					if (item.data[self.keyColumnName]==r[self.nameKey]){
						self.selectObject.splice(i,1);
						break;
					}
				}
		 	}
		 }
		 if(iter>=0){
			 var buttomAffect = parseInt(self.levelAffect.substr(0,1));
			 if(buttomAffect!=0&&((buttomAffect%2==1&&iter==0)||buttomAffect%2==0)){
			 	 iter++;
	             Ext.Array.each(item.childNodes, function(r) {
					 checkEventFun(r,checked,iter,
					 ((buttomAffect==1||buttomAffect==2)?0:((buttomAffect==3||buttomAffect==4)?1:-1)));
				 });	
	         }
		 }
		 if(iter<=0){
	         var topAffect = parseInt(self.levelAffect.substr(1)); 
			 if(topAffect!=0&&((topAffect%2==1&&iter==0)||topAffect%2==0)){
			      checkEventFun(item.parentNode,checked,iter--,
			      ((topAffect==1||topAffect==2)?0:((topAffect==3||topAffect==4)?1:-1)));
	         }
		 }
    },
    /**
     * 复选状态设置
     * @param {} ids ID数据
     */
    setCheckState:function(ids){
    	var self = this;
    	if(!Ext.isArray(ids)){
    		ids = ids.split(self.decollator)
    	}
    	self.selectObject = [];
    	self.iteraterSetRaxValue(self.getRootNode.childNodes,ids);
    },
    /**
     * 迭代树数据
     * @param {} list
     */
    iteraterSetRaxValue:function(list,ids){
    	var self = this;
    	for(var i=0;i<list.length;i++){
    		var item = list[i].data;
    		if(!self.checked){//全局控制是否带复选框
    			item.checked = null;
    		}else if(ids){//设置复选框状态
    			if(item.checked!=undefined){
    				var find = false;
    				Ext.Array.each(ids, function(r) {
						if (item[self.keyColumnName]==r){
							self.selectObject.push(item);
							find = true;
						}
					});
					item.checked = find;
    			}
    			self.iteraterSetRaxValue(list[i].childNodes,ids);
    		}
    	}
    }
});