/**
 * 桌面基础类
 */
Ext.define('Ext.ux.desktop.Outlook', {
	extend : 'Ext.panel.Panel',

	alias : 'widget.outlook',

	uses : ['Ext.util.MixedCollection', 'Ext.menu.Menu',
			'Ext.view.View', // dataview
			'Ext.window.Window', 'Ext.ux.desktop.TaskBar',
			'Ext.ux.desktop.Wallpaper'],

	layout : 'border',
	defaults : {
		style : {
			borderStyle : 'solid',
			borderWidth : '0px',
			border : 0
		}
	},
	border : false,

	headerHeight : 54,
	footerHeight : 28,
	showmsgnum : 0,
	app : null,

	tabPanel : null,

	/**
	 * @cfg {Object} taskbarConfig The config object for the TaskBar.
	 */
	taskbarConfig : null,
	// 主题切换
	addSwitchThmem : function(menu) {
		var me = this;
		// 判断是否有新公告
		/*Ext.require('modules.baseinfo.Affiche', function() {
					var have = modules.baseinfo.Affiche.haveAffice();
					if (have)
						me.taskbar.add(6, {
									xtype : 'button',
									icon : 'resources/icons/fam/information.png',
									text : '查看公告',
									handler : function() {
										modules.baseinfo.Affiche
												.displayAffice();
									},
									scope : this.app
								});
				});*/
		/*me.taskbar.add(3, {
					xtype : 'button',
					tooltip : '主题切换',
					icon : 'resources/icons/fam/theme.gif',
					menu : {
						items : menu.menu,
						width : '90px'
					},
					 * handler:function(bu,event){ for(var i=0;i<menu.menu.length;i++){
					 * menu.menu[i].xtype='button'; } var themeWin = new
					 * Ext.Window({ layout : 'table', layoutConfig:{ columns:2 },
					 * bodyStyle:'padding:10 10 10 10', border : false, frame :
					 * false, closeAction : 'hide', resizable : false, closable :
					 * false, draggable : false, modal : false, items:menu.menu
					 * }); themeWin.pageX = this.getX()-100; themeWin.pageY =
					 * this.getY()-40; themeWin.show(); event.stopPropagation();
					 * Ext.getDoc().on('click', function(e,t){
					 * if(!e.within(this.id,false,true)&&e.target.type!='')
					 * this.close(); }, themeWin); },
					 
					menuAlign : 'bl-tl'
				});*/
	},
	// 增加机构切换
	addSwitchOrgan : function(p_menu) {
		var me = this;
		//me.taskbar.add(4, "-");
		me.taskbar.add(0, {
					xtype : 'button',
					text : '<div style="font-size:15px;"><img src="resources/icons/fam/user.png" />'+(base.Login.userSession.currentOrganName + "("
							+ base.Login.userSession.currentDeptName + ")")+'</div>',
					tooltip : '机构部门切换',
					menu : (base.Login.userSession.manger ? {
						items : p_menu.menu
					} : null),
					menuAlign : 'bl-tl'
				});
		me.taskbar.add(1, "->");
	},
	// 增加系统切换
	addSwitchSystem : function(menu) {
		var me = this;
		var i=4;
		if(menu.menu.length>0)
		me.taskbar.add(i++, {
					xtype : 'button',
					tooltip : '系统切换',
					icon : 'resources/icons/fam/cog.gif',
					menu : {
						items : menu.menu
					},
					menuAlign : 'bl-tl'
				});
		me.taskbar.add(i,
				'<a href="http://www.ite.com.cn" target="_new"><img src="resources/images/ITE_logo.png" width="120"/></a>');
	},
	// 增加功能菜单
	addModuleMenu : function(menus) {
		var me = this;
		for (var i = 0; i < menus.length; i++) {
			me.systemMenu.add(new Ext.tree.TreePanel({
						title : '<div style="font-weight: normal">'+menus[i].text+"</div>",
						icon : menus[i].icon,
						style : {
							background : '#ffc',
							height : 40,
							fontSize:'15px'
						},
						viewConfig:{
							rowTpl: [
						        '{%',
						            'var dataRowCls = values.recordIndex === -1 ? "" : " ' + Ext.baseCSSPrefix + 'grid-data-row";',
						        '%}',
						        '<tr role="row" {[values.rowId ? ("id=\\"" + values.rowId + "\\"") : ""]} ',
						            'data-boundView="{view.id}" ',
						            'data-recordId="{record.internalId}" ',
						            'data-recordIndex="{recordIndex}" ',
						            'class="{[values.itemClasses.join(" ")]} {[values.rowClasses.join(" ")]}{[dataRowCls]}" ',
						            '{rowAttr:attributes} tabIndex="-1">',
						            '<tpl for="columns">' +
						                '{%',
						                    'parent.view.renderCell(values, parent.record, parent.recordIndex, xindex - 1, out, parent)',
						                 '%}',
						            '</tpl>',
						        '</tr>',
						        {
						            priority: 0
						        }
						    ],
						    cellTpl: [
						        '<td role="gridcell" class="{tdCls}" {tdAttr} id="{[Ext.id()]}" style="padding:10px;font-size:15px">',
						            '<div {unselectableAttr}',
						                'style="text-align:{align}">{value}</div>',
						        '</td>', {
						            priority: 0
						        }
						    ],
						    renderCell: function(column, record, recordIndex, columnIndex, out) {
							        var me = this,
							            selModel = me.selModel,
							            cellValues = me.cellValues,
							            classes = cellValues.classes,
							            fieldValue = record.data[column.dataIndex],
							            cellTpl = me.cellTpl,
							            value, clsInsertPoint;
							        cellValues.record = record;
							        cellValues.column = column;
							        cellValues.recordIndex = recordIndex;
							        cellValues.columnIndex = columnIndex;
							        cellValues.cellIndex = columnIndex;
							        cellValues.align = column.align;
							        cellValues.tdCls = column.tdCls;
							        cellValues.innerCls = column.innerCls;
							        cellValues.style = cellValues.tdAttr = "";
							        cellValues.unselectableAttr = me.enableTextSelection ? '' : 'unselectable="on"';							
							        if (column.renderer && column.renderer.call) {
							            value = column.renderer.call(column.scope || me.ownerCt, fieldValue, cellValues, record, recordIndex, columnIndex, me.dataSource, me);
							            if (cellValues.css) {
							                // This warning attribute is used by the compat layer
							                // TODO: remove when compat layer becomes deprecated
							                record.cssWarning = true;
							                cellValues.tdCls += ' ' + cellValues.css;
							                delete cellValues.css;
							            }
							        } else {
							            value = fieldValue;
							        }
							        cellValues.value = (value == null || value === '') ? '&#160;' : value;							
							        // Calculate classes to add to cell
							        classes[1] = Ext.baseCSSPrefix + 'grid-cell-' + column.getItemId();							            
							        // On IE8, array[len] = 'foo' is twice as fast as array.push('foo')
							        // So keep an insertion point and use assignment to help IE!
							        clsInsertPoint = 2;							
							        if (column.tdCls) {
							            classes[clsInsertPoint++] = column.tdCls;
							        }
							        if (me.markDirty && record.isModified(column.dataIndex)) {
							            classes[clsInsertPoint++] = me.dirtyCls;
							        }
							        if (column.isFirstVisible) {
							            classes[clsInsertPoint++] = me.firstCls;
							        }
							        if (column.isLastVisible) {
							            classes[clsInsertPoint++] = me.lastCls;
							        }
							        if (!me.enableTextSelection) {
							            classes[clsInsertPoint++] = Ext.baseCSSPrefix + 'unselectable';
							        }							
							        classes[clsInsertPoint++] = cellValues.tdCls;
							        if (selModel && selModel.isCellSelected && selModel.isCellSelected(me, recordIndex, columnIndex)) {
							            classes[clsInsertPoint++] = (me.selectedCellCls);
							        }							
							        // Chop back array to only what we've set
							        classes.length = clsInsertPoint;							
							        cellValues.tdCls = classes.join(' ');
							        cellValues.value = cellValues.value.replace("x-tree-node-text","");
							        //添加此段，用于菜单名过长换行后，第二行开始顶格显示了，所以改成2个div显示
							        var imglen = ((","+cellValues.value+",").split("<img")).length-1;
							        cellValues.value = cellValues.value.replace("<span class=\" \">","</div><div class=\" \" style=\"display:inline; float:left;width:"+(202-imglen*20)+"px;vertical-align: middle;\">");
							        cellValues.value = cellValues.value.replace("</span>","</div>");
							        cellValues.value = "<div class=\" \" style=\"display:inline; float:left;width:"+(imglen*20)+"px;vertical-align: middle;\">"+cellValues.value+"</div>";
							        cellTpl.applyOut(cellValues, out);							        
							        // Dereference objects since cellValues is a persistent var in the XTemplate's scope chain
							        cellValues.column = null;
							    }
						},
						border : false,
						floating : false,
						rootVisible:false,
						lines:false,
						useArrows:true,
						store : new Ext.data.TreeStore({
							fields:['menuId','tabName','url','text','icon'],
							root:{
								text:'',
								children:menus[i].children
							}
						}),
						listeners:{
						    'itemclick':function(view,record,item){
						    	if(record.data.menuId){
						    	   if(record.data.menuId=='快捷'){
						    	   	  var list = ClassCreate('modules.funres.QuickMenu');
									  var formWin = new WindowObject({
												layout : 'fit',
												title : ('管理快捷菜单,注意只取前'+(base.Config.getProperty("grabl_quick_menu_max"))+'个菜单！'),
												height : 400,
												width : 700,
												border : false,
												frame : false,
												modal : true,// 模态
												closeAction : 'hide',
												items : [list]
											});	
									   formWin.show();
						    	   }else
						    	      grablEapLogin.createWindow(record.data.menuId,record.data.tabName,record.data.icon,record.data.url);
						    	}else if(record.data.expanded)
						    	   record.collapse();
						    	else
						    	   record.expand();
						    }
						}
					}));
		}
	},
	setLoginTitle : function(title) {
		//this.taskbar.add(1, "-");
		/*this.taskbar.add(2, {
					xtype : 'button',
					icon : 'resources/icons/fam/user.gif',
					text : '修改密码',
					handler : this.app.onUserInfo,
					scope : this.app
				});*/
	    var us = base.Login.userSession;
		userInfoTd.innerHTML='<font color="#fd4c2a" style="font-size: 16px;">'+title+'</font>&nbsp;'
		    +(us.quarterName==undefined?'':us.quarterName)+"<br/><font style='font-size: 14px;'>"+this.getTimeFormat()+"</font>";
		//this.taskbar.add(3, {html : title});
	},
	openAffiche:function(){
		var me = this;
		Ext.require('modules.baseinfo.Affiche',function(){
			modules.baseinfo.Affiche.displayAffice(function(){
				 var obj = document.getElementById("msgImgPng");
	  			 if(obj!=undefined){
					Ext.Ajax.request({
						method : 'GET',
						url : 'baseinfo/affiche/afficheImgForAfficheListAction!afficheImg.action',
						params:{},
						success:function(response){
							var re = Ext.decode(response.responseText);
							if(re && re.msg == 'true' && base.Login.userSession.userId!=undefined){
								obj.src="upload/msgpng/top_menu021_"+base.Login.userSession.userId+".png?"+new Date().getTime();
							}
						},
						failure:function(){}
					});
				}
			});
		});
	},
	openMoniExam : function(){
		window.location = "mainPageEx/listForMainExListAction!list.action";
	},
	refreshMsgImg : function(){//更新消息图片
	   var me = this;
	   var obj = document.getElementById("msgImgPng");
	   if(obj!=undefined){
	   		Ext.Ajax.request({
				method : 'GET',
				url : 'baseinfo/affiche/afficheImgForAfficheListAction!afficheImg.action',
				params:{},
				success:function(response){
					var re = Ext.decode(response.responseText);
					if(re && re.msg == 'true' && base.Login.userSession.userId!=undefined){
						obj.src="upload/msgpng/top_menu021_"+base.Login.userSession.userId+".png?"+new Date().getTime();//更新消息图片
						if(me.showmsgnum==0 && Number(re.msgNum)>0){
							ooMainshowMsg = function(){me.openAffiche();}
							var msg = '<table width="100%"><tr><td align="left"><a href="javascript:this.ooMainshowMsg()">您有'+re.msgNum+'条新消息，点击查看。</a></td></tr></table>';
						    base.Message.autoHide = 30;
						    base.Message.desktopMessage(msg,function(){},function(){});
							me.showmsgnum = me.showmsgnum+1;
						}
					}
				},
				failure:function(){}
			});
	   }
	},
	initComponent : function() {
		var me = this;
		me.items = [];
		var headhtml = '<table width="100%" border="0" cellspacing="0" cellpadding="0" bgcolor="#0b82dd">'
				+ '<tr><td height="54" align="left"><img src="resources/images/top_logo.jpg"/></td>'
				+ '<td width="90" align="center" onclick="grablEapLogin.desktop.openIndex()" onmouseover="this.className=\'top_menu\'" onmouseout="this.className=\'\' "><img src="resources/images/top_menu01.png"/></td>'
				//+ '<td width="90" align="center" onclick="grablEapLogin.desktop.openMoniExam();" onmouseover="this.className=\'top_menu\'" onmouseout="this.className=\'\' "><img src="resources/images/top_menu05.png" /></td>'
				+ '<td width="90" align="center" onclick="grablEapLogin.desktop.openAffiche();" onmouseover="this.className=\'top_menu\'" onmouseout="this.className=\'\' "><img id="msgImgPng" src="resources/images/top_menu02.png" /></td>'
				+ '<td width="90" align="center" onclick="grablEapLogin.app.onUserInfo()" onmouseover="this.className=\'top_menu\'" onmouseout="this.className=\'\' "><img src="resources/images/top_menu03.png" /></td>'
				+ '<td width="90" align="center" onclick="grablEapLogin.app.onLogout()" onmouseover="this.className=\'top_menu\'" onmouseout="this.className=\'\' "><img src="resources/images/top_menu04.png" /></td></tr></table>';
		me.items.push({
					region : 'north',
					html : headhtml,
					border : false
				});// 界面顶部
		// 开始菜单
		me.taskbar = new Ext.ux.desktop.TaskBar(me.taskbarConfig);
		//me.taskbar.remove(0);
		/*me.taskbar.add(0, {
					xtype : 'button',
					text : '注  销',
					iconCls : 'logout',
					handler : me.app.onLogout,
					scope : me.app
				});*/
		// me.taskbar.startMenu.menu.show();
		me.systemMenu = Ext.create("Ext.panel.Panel", {
					width : 240,
					height : me.getMenuHeight(eapClientHeight),
					layout : 'accordion',
					border : false
				});
		
		me.systemUserTitle = {
			xtype : 'panel',
			border : false,
			height : 60,
			html : '<table width="100%" border="0" cellspacing="0" cellpadding="0" style="background:#fafafa;">'
					+ '<tr><td width="57" height="60" align="center"><img src="resources/images/portrait.png"/></td>'
					+ '<td width="161" align="left" id="userInfoTd"><font color="#fd4c2a" style="font-size: 16px;">管理员</font>&nbsp;一值值长<br/><font style="font-size: 14px;">'
					+ me.getTimeFormat()+'</font></td>' 
					+ '<td width="22" align="right" valign="top"><a href="javascript:grablEapLogin.desktop.collapseLeftMemu();">' 
					+ '<img src="resources/images/btn_shrink.gif" onmouseover="this.width=26;this.height=36;" ' +
							'onmouseout="this.width=19;this.height=29;" style="margin-top:16px;" title="收缩主菜单"/></a></td></tr>'
		};
		Ext.EventManager.onWindowResize(function(width, height) {
					me.systemMenu.height = me.getMenuHeight(height);
				});
		me.systemLeft = {
			xtype : 'panel',
			region : 'west',
			layout : 'vbox',
			items : [me.systemUserTitle, me.systemMenu]
		};
		me.items.push(me.systemLeft); // 界面左则部分
		me.items.push({
					region : 'south',
					collapsible : false,
					split : false,
					height : me.footerHeight,
					items : me.taskbar,
					/*html: '<table width="100%" border="0" cellspacing="0" cellpadding="0" style="background:#0b82dd"><tr>'+
                          '<td align="center" class="bottom_zi">耒阳电厂（发电部）</td>'+
                          '<td width="auto" align="left" class="bottom_zi" style="text-indent: 20px;">当前在线人数5人    累计登录次数20次</td>'+
                          '<td width="75" align="left" style="text-indent: 15px;"><a href="www.ite.com.cn" target="new"><img src="resources/images/style.png" /></a></td>'+
                          '<td width="120" align="right"><img src="resources/images/ITE_logo.png" /></td></tr></table>',*/
					border : false
				}); // 界面下部
		me.tabPanel = new Ext.TabPanel({
					region : 'center',
					activeTab : 0,
					border:false,
					enableTabScroll : true
				});
		me.items.push(me.tabPanel);
		me.windows = new Ext.util.MixedCollection();
		/***********************************************************************
		 * 根据窗口大小自动适应 Ext.EventManager.onWindowResize(function(w,h){
		 * me.windows.each(function(win) {
		 * win.setHeight(h-me.headerHeight-me.footerHeight-grabl_tabPageHeight);
		 * }); },this,true);
		 **********************************************************************/
		me.callParent();
		//setTimeout(me.refreshMsgImg(),1000);
		//用于刷新消息图片
		me.msgTask = {
		    run: function(){
		       me.refreshMsgImg();
		    },
		    interval: 1800000 //30*60*1000
		}
		me.msgRunner = new Ext.util.TaskRunner();  
		me.msgRunner.start(me.msgTask);
	},
	getTimeFormat:function(){
			var myDate = new Date();
			var w="";
			switch(myDate.getDay())
			{
			   case 0:w="日";break;
			   case 1:w="一";break;
			   case 2:w="二";break;
			   case 3:w="三";break;
			   case 4:w="四";break;
			   case 5:w="五";break;
			   case 6:w="六";break;
			};
			return myDate.getFullYear()+"年"+
			       (myDate.getMonth()+1)+"月"+
			       myDate.getDate()+"日&nbsp;&nbsp;星期"+w;
	},
	collapseLeftMemu:function(){
		if(!this.left)
		  this.left = this.items.items[1];
		this.left.collapse();
	},
	getMenuHeight : function(h) {
		return h - 60 - 54 - 28;
	},
	openIndex:function(){
		this.getWindow('menu_system_index_url');
	},
	// 打开菜单项
	createWindow : function(config, cls) {
		var me = this, win, cfg = config;
		if(cfg!=undefined && (cfg.title.indexOf('正式在线考试')!=-1 || cfg.title.indexOf('在线考试')!=-1 || (cfg.html!=undefined && cfg.html!=null && cfg.html.indexOf('online.html')!=-1))){
			var oNewWindow  = window.open("online.html","onlineExamZs",'width='+(window.screen.availWidth-10)+',height='+(window.screen.availHeight-30)+ ',z-look=yes,top=0,left=0,toolbar=yes,menubar=yes,scrollbars=yes, resizable=yes,location=yes, status=yes');
			if(oNewWindow){
				oNewWindow.focus();
			}
			return null;
		}
		if(cfg.html!=undefined && cfg.html!=null && cfg.html.indexOf('listForMainExListAction!list.action')!=-1){
			window.location = "mainPageEx/listForMainExListAction!list.action";
			return null;
		}
		if (me.windows.length > grabl_tabPageMaxSize) {
			Ext.Msg.alert('警告', '最大只能打开' + grabl_tabPageMaxSize + '个窗口！');
		}
		cls = cls || Ext.Panel;
		delete cfg.icon;
		delete cfg.width;
		delete cfg.height;
		delete cfg.animCollapse;
		delete cfg.constrainHeader;
		delete cfg.maximized;
		cfg.border = false;

		win = new cls(cfg);
		var title = win.title;
		delete win.title;
		win.height = me.tabPanel.getHeight() - grabl_tabPageHeight;
		var closable = (title != "首页");
		var tabPage = me.tabPanel.add({
					id : 'tab-' + win.id,
					title : '<div style="font-size:15px;font-weight: normal">'+title+'</div>',
					border : false,
					layout : 'fit',
					items : win,
					closable : closable,
					listeners : {
						'beforeclose' : function(a, b) {
							me.windows.remove(a);
						}
					}
				});
		me.tabPanel.setActiveTab(tabPage);
		me.windows.add(me.tabPanel.getActiveTab());
		return null;
	},
	getWindow : function(id) {
		var win = this.windows.get('tab-' + id);
		if (win)
			this.tabPanel.setActiveTab(win);
		return win;
	},
	closeWindow : function(id) {
		var me = this;
		if (!id)
			me.tabPanel.getActiveTab().close();
		else
			Ext.Array.each(me.tabPanel.items.items, function(tab) {
						if (tab.id == ('tab-menu_' + id))
							tab.close();
					});
	}/*
		 * , listeners: { afterRender: function(thisForm, options){ this.keyNav =
		 * Ext.create('Ext.util.KeyNav', this.el, { backspace:
		 * function(){},//清除回退事件 scope: this }); } }
		 */
});