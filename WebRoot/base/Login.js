/**
 * 登录面板
 */
Ext.define('base.Login',{
   statics:{
   	  //用户session
   	  userSession:{},
   	  //按钮权限判断
   	  judgePopdom:function(resourceCode){
   	  	 if(!resourceCode) return true;
   	  	 var find = false;
   	  	 Ext.Array.each(base.Login.userSession.authoritys,function(code){
   	  	 	 if(code==resourceCode) find = true;
   	  	 });
   	  	 return find;
   	  },
   	  //处理面板中按钮的权限控制（显示与否）
   	  handlePopdomButtom:function(panel){
   	  	  var buttons = panel.query('button');
   	  	  Ext.Array.each(buttons,function(button){
   	  	 	  var resourceCode = button.resourceCode;
   	  	 	  if(resourceCode){
   	  	 	  	  if(base.Login.judgePopdom(resourceCode))
   	  	 	  	      button.show();
   	  	 	  	  else
   	  	 	  	      button.hide();
   	  	 	  }
   	  	  });
   	  },openJsWindow : function(url,width,height,title,panel,config){//打开指定地址的窗口,url是JS类路径，后接？与&加参数
        	var match = url.split(RegExp("[?&]"));
        	var config = config||{};
			for(var i=1;i<match.length;i++){
			    var p = match[i].split("=");
			    config[p[0]]=p[1];
			} 
       	    var form = eval("new "+match[0]+"(config)");	
			var formWin = new WindowObject({
					layout : 'fit',
					title : title,
					height : height,
					width : width,
					border : false,
					frame : false,
					modal : true,// 模态
					closeAction : 'hide',
					draggable:true,//拖动
					resizable:false, //变大小 
					items : [form]
				});
			formWin.parentPanel=panel;
			formWin.show();
	    }
   },
   cp : new Ext.state.CookieProvider(),//cookie对象
   initComponent : function(){
   	  Ext.state.Manager.setProvider(me.cp);
   },
   openLognPage:function(){
   	     var me = this;
   	     var cp = me.cp;
	     var loginForm = new Ext.FormPanel({ 
	        layout : {
				type : 'table',
				columns : 2,
				tableAttrs : {
					style : {
						width : '100%'
					}
				}
			},
	        frame:false,
	        defaults: {
	           labelWidth:90,
	           labelAlign :'right'
	        },
	        bodyStyle:'padding:10px 5px 0', 
	        defaultType:'textfield',
	        monitorValid:true,
	        items:[{ 
	                fieldLabel:'用户帐号', 
	                name:'account',
	                emptyText:'请输入用户帐号',
	                colspan : 2,
	                width : 240,
	                value : cp.get('account'),
	                allowBlank:false
	            },{ 
	                fieldLabel:'用户密码', 
	                name:'password', 
	                colspan : 2,
	                inputType:'password', 
	                value : cp.get('password'),
	                width : 240,
	                allowBlank:false 
	            },{ 
	                boxLabel:'保存账号密码',
	                fieldLabel:' ',
	                labelSeparator :'',
	                name:'save',
	                colspan : 2,
	                inputValue : true,
	                checked : (cp.get('account')!=null),
	                xtype : 'checkbox'
	            },{ 
	                boxLabel:'代理模式', 
	                fieldLabel:' ',
	                labelSeparator :'',
	                name:'proxy',
	                colspan : 2,
	                inputValue : true,
	                xtype : 'checkbox'
	            }],
	        buttons:[{ 
	                text:'登录',
	                id:'loginButtom',
	                formBind: true, 
	                handler:function(){ 
	                    loginForm.getForm().submit({ 
	                        method:'POST', 
	                        waitTitle:'系统提示', 
	                        waitMsg:'正在登录,请稍候...',
	                        url:'power/login/loginForLoginAction!login.action',
	                        success:function(form, action){
	                        	if(form.findField('save').value){
		                        	cp.set('account',form.findField('account').value);
		                        	cp.set('password',form.findField('password').value);
	                        	}else{
	                        		cp.set('account',null);
		                        	cp.set('password',null);
	                        	}
	                        	var obj = Ext.decode(action.response.responseText);
	                        	var pe = obj.proxyEmployee;//多个被代理用户
	                        	if(pe){	                        		
	                        		Ext.Msg.alert('提示', '存在多个被代理用户，请选择！',function(bt){
	                        		   if(bt=='ok'){
	                        		   	  var array = new Array();
										  Ext.Array.each(pe, function(r) {
											  array.push([r['userId'],r['userName']]);
										  });
	                        		   	  var byProxyUserSelect = Ext.widget('select',{ 
								            	fieldLabel:'被代理用户', 
								            	xtype : 'select',
								                name:'byProxyUserId', 
								                valueField : 'userId',
												displayField : 'userName',
												emptyText : '',
												colspan : 2,
												width : 240,
												labelWidth:90,
	                                            labelAlign :'right',
												data : array
								            });
								            loginForm.add(byProxyUserSelect);
	                        		   	    //proxySelect.setVisible(true);
	                        		   	    //proxySelect.setSelectData(pe);
	                        		   }
	                        		}); 
	                        	}else{
	                                var redirect = 'index.jsp';//?theme='+obj.userSession.theme; 
	                                window.location = redirect;
	                        	}
	                        },
	                        failure:function(form, action){
	                            if(action.failureType == 'server'){
	                                var obj = Ext.decode(action.response.responseText);
	                                if(Ext.isArray(obj)) obj = obj[0];
	                                Ext.Msg.alert('登录失败', obj.errors,function(){
	                                        form.findField('account').focus();
	                                }); 
	                            }else{ 
	                                Ext.Msg.alert('警告', '网络出现问题！'); 
	                            }
	                        } 
	                    });
	                } 
	            }],
	            listeners: {  
			        afterRender: function(thisForm, options){  
			            this.keyNav = Ext.create('Ext.util.KeyNav', this.el, {  
			                enter: function(){  
			                    // 筛选表格  
			                    var btn = Ext.getCmp('loginButtom');  
			                    btn.handler() ; 
			                },  
			                scope: this  
			            });  
			        }  
			    }
	    });
	    var logoPanel = new Ext.Panel({
                html:'<center><img width="400" height="100" src="resources/images/logo.jpg"/></center>',
                id : 'login-logo',
                width:'100%',
                region : 'center'
            });
	    var win = new Ext.Window({
	        title:'用户登录', 
	        layout : {type : 'table',columns : 1},
	        border : false,
		    frame : false,
	        closable: false,
	        closeAction : 'hide',
	        resizable: false,
	        draggable: false,
	        modal : true,// 模态
	        plain: false,
	        items: [logoPanel,loginForm]
	    });    
	    win.show();  
	},//迭代装配主菜单
	iteraterMenu:function(trees,parent){
		var me = this;
		Ext.Array.each(trees, function(tree) {
			if(tree.type=='separator'){
				//parent.menu.push("-");
			}else{
				var menu = {};
				menu.text = tree.title;
				menu.icon = tree.icon;
				if(!tree.leaf&&tree.children.length>0){
				   menu.hideOnClick = false;
				   menu.children = [];
				   me.iteraterMenu(tree.children,menu);
				}else{
				   menu.menuId=tree.id;
				   menu.tabName=tree.tagName;
				   menu.url=tree.url;
				   //menu.openItem = function(){Ext.bind(me.createWindow, me, [tree.id,tree.tagName,tree.icon,tree.url])};
				   menu.leaf=true;
				}
				if(parent!=null)
				   parent.children.push(menu);
				if(parent==null){
					me.functionMenu.push(menu);
				}
			}
		});
	},//装配主菜单
	makeMenu:function(){
		var me = this;
		//主题切换
		var Dictionary = modules.baseinfo.Dictionary;//类别名
        var themeSwitchMenu = {};
		themeSwitchMenu.text = '主题切换';
		themeSwitchMenu.icon = 'resources/icons/fam/theme.png';
		themeSwitchMenu.menu = [];		
        var dicData = Dictionary.getDictionaryList('THEME_TYPE').datas;
        //从数据字典中取主题数据
		Ext.Array.each(dicData,function(item){
			 var childMenu = {text:item.dataName,icon:''};			 
			 if(Ext.themeName==item.dataKey)
			     return ;//childMenu.icon="resources/icons/fam/tick.png";
			 else
			     childMenu.handler = Ext.bind(me.switchTheme, me, [item.dataKey]);
		     themeSwitchMenu.menu.push(childMenu);
		});		
		me.desktop.addSwitchThmem(themeSwitchMenu);
		//系统切换菜单
		EapAjax.request({
			method : 'GET',
			url : 'power/login/appListForLoginAction!appList.action',
			async : false,
			success : function(response) {
				var result = Ext.decode(response.responseText);
				var systemSwitchMenu = {};
				systemSwitchMenu.text = '系统切换';
				systemSwitchMenu.icon = 'resources/icons/fam/folder_wrench.png';
				systemSwitchMenu.menu = [];		
				var defaultSet = true;
				if(base.Login.userSession.appId==undefined)
				  defaultSet = false;
				Ext.Array.each(result, function(tree) {
					var childMenu = {text:'<span style="font-size:15px;">'+tree.appName+"</span>",icon:tree.smallPic};
					childMenu.handler = Ext.bind(me.switchSystem, me, [tree.appId]);
					if(tree.appId==base.Login.userSession.appId||!defaultSet){
					    childMenu.icon = "resources/icons/fam/tick.png";
					    if(!defaultSet)
					       base.Login.userSession.appId = tree.appId;
					    return;//defaultSet = true;
					}
					systemSwitchMenu.menu.push(childMenu);
				});
				me.desktop.addSwitchSystem(systemSwitchMenu);
			},
			failure:function(form, action){ Ext.Msg.alert('警告', '网络出现问题！');}
		});
		function iteraterODMenu(ods,pmenu){//机构部门树的迭代处理
			    var start_dept = null;
				Ext.Array.each(ods, function(tree) {
					var childMenu = {text:'<span style="font-size:15px;">'+tree.title+"</span>",
					              icon:tree.icon};
					if(tree.type=='dept'){
						if(start_dept==0)
						  start_dept = 1;
					    childMenu.handler = Ext.bind(me.switchOt, me, ['dept',tree.id]);
					    if(tree.id==base.Login.userSession.currentDeptId)
					       childMenu.icon = "resources/icons/fam/dept_select.gif";
					    else
					       childMenu.icon = "resources/icons/fam/dept.gif";
					}else{
						start_dept = 0;
						childMenu.handler = Ext.bind(me.switchOt, me, ['organ',tree.id]);
						if(tree.id==base.Login.userSession.currentOrganId)
					       childMenu.icon = "resources/icons/fam/organ_select.gif";
					    else
					       childMenu.icon = "resources/icons/fam/organ.gif";
					}
					if(!pmenu.menu)
					   pmenu.menu = [];
					if(start_dept==1){
					   pmenu.menu.push("-");			   
					   start_dept = null;
					}
					pmenu.menu.push(childMenu);
					iteraterODMenu(tree.children,childMenu);
				});
			};
		//机构部门切换菜单
		EapAjax.request({
			method : 'GET',
			url : 'organization/dept/odSwitchtreeForDeptListAction!odSwitchtree.action?organTerm='
			          +base.Login.userSession.organId+"&id="+base.Login.userSession.deptId,
			async : false,
			success : function(response) {
				var result = Ext.decode(response.responseText);
				if(result.length>0){
					var odSwitchMenu = {};
					odSwitchMenu.text = '机构部门切换';
					odSwitchMenu.icon = 'resources/icons/fam/user_comment.png';
					iteraterODMenu(result,odSwitchMenu);
					me.desktop.addSwitchOrgan(odSwitchMenu);
				}
			},
			failure:function(form, action){ Ext.Msg.alert('警告', '网络出现问题！');}
		});
		//功能菜单
		me.functionMenu = [];
		EapAjax.request({
			method : 'GET',
			url : 'power/login/findMenuForLoginAction!findMenu.action',
			async : false,
			success : function(response) {
				//加快捷菜单
				/*Ext.Ajax.request({
					method : 'get',
					async : false,
					url : "funres/menu/findMenuRecordForMenuListAction!findMenuRecord.action",
					success : function(response) {
						if(!response.responseText) return;
						var result = Ext.decode(response.responseText);
						if(!result) return;
						result = result.appMenus;
						if(!result||result.length==0) return;
				        var shortcutMenus = {};
						shortcutMenus.text = '快捷菜单';
						shortcutMenus.icon = 'resources/icons/fam/plugin.gif';
						shortcutMenus.children = [{text:'管理快捷菜单',icon:'resources/icons/fam/cog_edit.png',leaf:true,
						                       menuId:'快捷'}];
						Ext.Array.each(result,function(item){
							 var childMenu = {text:item.menuName,icon:item.icon?item.icon:'resources/icons/fam/grid.png'};
							 childMenu.menuId=item.menuId;
							 childMenu.tabName=item.tabName;
							 childMenu.url=item.url;
							 //childMenu.openItem = function(){Ext.bind(me.createWindow, me, [item.menuId,item.tabName,item.icon,item.url]);};
							 childMenu.leaf=true;
						     shortcutMenus.children.push(childMenu);
						});	
						me.functionMenu.push(shortcutMenus);
					}
				});*/
				var result = Ext.decode(response.responseText);
				me.iteraterMenu(result,null);				
			},
			failure:function(form, action){
                if(action.failureType == 'server'){
                    var obj = Ext.decode(action.response.responseText);
                    if(Ext.isArray(obj)) obj = obj[0];
                    Ext.Msg.alert('错误', obj.errors); 
                }else{ 
                    Ext.Msg.alert('警告', '网络出现问题！'); 
                }
                window.close();
            }
		});
		me.desktop.addModuleMenu(me.functionMenu);
	},//查询用户session
	findUserSession:function(){
		var me = this;
		EapAjax.request({
			method : 'GET',
			url : 'power/login/findUserSessionForLoginAction!findUserSession.action',
			async : false,
			success : function(response) {
				var result = Ext.decode(response.responseText);
				var us = base.Login.userSession = result.userSession;
			    //console.log(us);
				if(!us||us.rppeatLogin){
					//me.openLognPage();
					window.location = "main.jsp";
				}else if(me.desktop){
				   me.desktop.setLoginTitle(us.userId=='admin'?'超级管理员':(us.userId=='guest'?'游客':us.employeeName));
				   me.makeMenu();
				   if(us.passwordUpdateTip){
				   	   Ext.Msg.alert('警告', '你还没有修改缺省密码，请修改！',function(bt){
				   	   	   if(bt=='ok'){
				   	   	   	   me.app.onUserInfo();
				   	   	   }
				   	   }); 
				   }
				   me.openClientSession(us.basePath,us.appName);
				   //打开首页
				   me.createWindow('system_index_url','首页','',us.basePath+'/'+us.indexUrl);
				}
			},
			failure:function(form, action){
                if(action.failureType == 'server'){
                    var obj = Ext.decode(action.response.responseText);
                    if(Ext.isArray(obj)) obj = obj[0];
                    Ext.Msg.alert('错误', obj.errors); 
                }else{ 
                    Ext.Msg.alert('警告', '网络出现问题！'); 
                }
                window.close();
            }
		});
	},//缺省地址替换
	replaceUrl:function(url){
		url = url.substr(0,7)+url.substr(7).replace(RegExp('/+','g'),"/");//去掉多个/
		var ps = location.pathname.split("/");
		url = url.replace('localhost:8080/eap',window.location.host+"/"+ps[1]);
		url = url.replace('localhost:8080',window.location.host);
		return url = url.replace('localhost',window.location.hostname);
	},
	closeWindow:function(id){
		this.desktop.closeWindow(id);
	},//打开菜单功能
	createWindow : function(id,title,icon,url){
        var me = this;
        url = me.replaceUrl(url);
        var winId = "menu_"+id;
        var win = me.desktop.getWindow(winId);
        if(win) return;//已经打开则返回
        var url_ = url;
        if(url.indexOf("?")<0)
           url_+="?random="+Math.random();
        else
           url_+="&random="+Math.random();
        if(url_.indexOf(".js?")>0||url_.indexOf("outjs=true")>=0){
        	var match = url.split(RegExp("[?&]"));
        	var basePath = match[0].substr(0,match[0].indexOf("/",match[0].indexOf("/",7)+1));
        	var jsClass = match[0].substr(basePath.length+1);
		    jsClass = jsClass.substr(0,jsClass.length-3).replace(RegExp('/','g'),".");
		    if(location.href.indexOf(basePath)!=0){
				url = me.addJessionId(url);
				url = "requestForward.action?"+url;//通过平台中传
		    }//url="http://localhost:8080/ydkh/modules/baseinfo/Log.js?1=1"
        	Ext.Loader.syncRequire(jsClass,
        	function(){
	    		  	var config = {};
	    		  	if(location.href.indexOf(basePath)!=0){
					    config['clientBasePath']=basePath;
					}
					for(var i=1;i<match.length;i++){
					    var p = match[i].split("=");
					    config[p[0]]=p[1];
					}
					try{    
			       	    var form = eval("new "+jsClass+"(config)");	
						var windowConfig = {
			                id: winId,
			                title:title,
			                icon: icon,
			                animCollapse:false,
			                constrainHeader:true,
			                maximized : true,
			                layout: 'fit',
			                items: [form]
			            };
			            if(form.windowConfig)
						windowConfig = Ext.apply(windowConfig,form.windowConfig);
			            win = me.desktop.createWindow(windowConfig);
			            //菜单记录
			            Ext.Ajax.request({
							method : 'get',
							url : "funres/menu/addMenuRecordForMenuFormAction!addMenuRecord.action",
							params:"id="+id
						});
					}catch(error){
						alert(error);
						win = me.desktop.createWindow({id:winId,maximized : true,title:title,layout:'fit',
						      html:'<div style="height:100%" align="center" valign="middle"><img src="resources/images/pageError.png"/></div>'});
					}
        	},this);
        }else{
            win = me.desktop.createWindow({
                id: winId,
                title:title,
                width:740,
                height : 520,
                icon: icon,
                animCollapse:false,
                constrainHeader:true,
                layout: 'fit',
                html:"<iframe src='"+url_+"' width='100%' height='100%' frameborder=0 scrolling=auto></iframe>"
            });
        }
        return win;
    },
    //主题切换
    switchTheme:function(theme){
    	var param = {"theme":theme};
    	var queryString = Ext.Object.toQueryString(
            Ext.apply(Ext.Object.fromQueryString(location.search), param)
        );
        Ext.require('modules.power.User',function(){
        	modules.power.User.saveTheme(base.Login.userSession.userId,theme);
            location.search = queryString;
        });        
    },//系统切换
    switchSystem:function(appId){
    	if(base.Login.userSession.appId==appId) return;
    	var me = this;
    	EapAjax.request({
			method : 'GET',
			url : 'power/login/switchSystemForLoginAction!switchSystem.action?appId='+appId,
			async : false,
			success:function(response){me.reLoginFailure(response,'s');},
			failure:function(form, action){me.reLoginFailure(action.response,'f');}});
    },//机构部门切换
    switchOt:function(type,id){
    	if(type=='dept'&&base.Login.userSession.currentDeptId==id) return;
    	if(type=='organ'&&base.Login.userSession.currentOrganId==id) return;
    	var me = this;
    	EapAjax.request({
			method : 'GET',
			url : 'power/login/switchOtForLoginAction!switchOt.action?id='+id+"&type="+type,
			async : false,
			success:function(response){me.reLoginFailure(response,'s');},
			failure:function(form, action){me.reLoginFailure(action.response,'f');}});
    },//平台退出
    quit:function(){
    	this.clearClientSession(base.Login.userSession.basePath);
    	EapAjax.request({
			method : 'GET',
			url : 'power/login/quitForLoginAction!quit.action',
			async : false});
	    window.close();
    },//平台注销
    logout:function(){
    	this.clearClientSession(base.Login.userSession.basePath);
    	//清除平台session
    	EapAjax.request({
			method : 'GET',
			url : 'power/login/quitForLoginAction!quit.action',
			async : false});
		//重新打开系统主界面
		var redirect = 'main.jsp';//'index.jsp'; 
	    window.location = redirect;
    },//统一的提示重新登录的错误
    reLoginFailure:function(response,t){
    	var me = this;
        if(t = 's'){
        	me.clearClientSession(base.Login.userSession.basePath);
            var obj = Ext.decode(response.responseText);
            if(Ext.isArray(obj)) obj = obj[0];
            if(obj.errors)
	            Ext.Msg.alert('错误', obj.errors,function(){
	            	me.openLognPage();
	            });
	        else
	           window.location.reload();
        }else{ 
            Ext.Msg.alert('警告', '网络出现问题！'); 
        }
    },openClientSession:function(url,appName){//打开客户端的SESSION
    	var me = this;
    	me.sessionArray = me.sessionArray||[];
    	if(!url) return;
    	var basePath = me.replaceUrl(url);
    	if(location.href.indexOf(basePath)==0) return;
    	url = basePath+"/openClientSession.action?eapSessionId="+base.Login.userSession.sessionId;
    	url = "requestForward.action?"+url;//通过平台中传
    	url+="&$type=control";//控制类型的请求
    	EapAjax.request({method : 'GET',url : url,async : true,success:function(response){
    		var ret = Ext.decode(response.responseText);
    		if(Ext.isArray(ret)) ret = ret[0];
    		if(ret.success){
	    		var map = {};
			    map['path']=basePath;
			    map['jsessionId']=ret.jsessionId;
			    me.sessionArray.push(map);
    		}else
    		    Ext.Msg.alert('错误', "此系统'"+appName+"'未启动或地址'"+basePath+"'不正确!"); 
    	}});
    },clearClientSession:function(url){//关闭客户端的SESSION
    	var me = this;
    	if(location.href.indexOf(url)==0) return;//和平台在同一应用下无需清除SESSION
    	me.sessionArray = me.sessionArray||[];
    	if(!url) return;
    	var basePath = url;
    	url+="/clearClientSession.action";
    	url = me.addJessionId(url);
    	if(url==null) return;
    	url = "requestForward.action?"+url;//通过平台中传
    	EapAjax.request({method : 'GET',url : url,async : true});
    },addJessionId:function(url){
    	var me = this;
    	var match = url.split(RegExp("[?&]"));
        var basePath = match[0].substr(0,match[0].indexOf("/",match[0].indexOf("/",7)+1));
    	var jsessionId = null;
    	Ext.Array.each(me.sessionArray,function(s){
    		if(s['path']==basePath)
    		  jsessionId = s['jsessionId'];
    	});
    	if(jsessionId==null) return url;
    	url = match[0] + (jsessionId!=null?(";jsessionid="+jsessionId):"");
    	for(var i=1;i<match.length;i++)
    	  url+=(i==1?"?":"&")+match[i];
    	return url;
    },webClientUrl:function(url,panel){
    	var me = this;
		if(panel.clientBasePath){
		   url = panel.clientBasePath+"/"+url;
		   url = me.addJessionId(url);
		   return "requestForward.action?"+url;
		}else
		   return url;
	}
});