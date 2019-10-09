/*!
 * 主界面（桌面）类
 */
Ext.define('eap.EapApp', {
    extend: 'Ext.ux.desktop.App',

    requires: [
        'Ext.window.MessageBox',
        'base.Login',
        'base.Message'
    ],
    
    login:{},

    init: function() {
    	/*var account = base.Config.getQueryParam("account");
	    var password = base.Config.getQueryParam("password");
	    if(account&&password){
	    	var url = "power/login/loginForLoginAction!login.action?account="+account+"&password="+password;
	    	EapAjax.request({method : 'GET',url : url,async : false})
	    }*/
        this.callParent();
    },

    getModules : function(){//要改成通过后台权限获得
        return [];
    },
    /**
     * 获取桌面配置
     * @return {}
     */
    getDesktopConfig: function () {
        var me = this, ret = me.callParent();
        return Ext.apply(ret, {
            //cls: 'ux-desktop-black',
            contextMenuItems: [//桌面右键菜单
                { text: '背景切换', handler: me.onSettings, scope: me }
            ],
            /*shortcuts: Ext.create('Ext.data.Store', {
                model: 'Ext.ux.desktop.ShortcutModel',
                data: [
                    { name: 'Grid Window', iconCls: 'grid-shortcut', module: 'grid-win' },
                    { name: 'Accordion Window', iconCls: 'accordion-shortcut', module: 'acc-win' },
                    { name: 'Notepad', iconCls: 'notepad-shortcut', module: 'notepad' },
                    { name: 'System Status', iconCls: 'cpu-shortcut', module: 'systemstatus'}
                ]
            }),*/
            //桌面背景图
            wallpaper: 'resources/wallpapers/Blue-Sencha.jpg',
            wallpaperStretch: true//背景是否拉伸
        });
    },
    /**
     * 配置开始菜单
     * @return {}
     */
    getStartConfig : function() {
        var me = this, ret = me.callParent();
        return Ext.apply(ret, {
            title: '功能菜单',
            iconCls: 'user',
            height: 300,
            toolConfig: {
                width: 100,
                items: [
                    {
                        text:'我的权限',
                        iconCls:'settings',
                        handler: me.onUserInfo,
                        scope: me
                    },
                    '-',
                    {
                        text:'注  销',
                        iconCls:'logout',
                        handler: me.onLogout,
                        scope: me
                    },
                    '-',
                    {
                        text:'退  出',
                        iconCls:'close',
                        handler: me.onQuit,
                        scope: me
                    }
                ]
            }
        });
    },
    /**
     * 配置工具条
     * @return {}
     * */
    getTaskbarConfig: function () {
        var ret = this.callParent();

        return Ext.apply(ret, {
            /*quickStart: [
                { name: 'Accordion Window', iconCls: 'accordion', module: 'acc-win' },
                { name: 'Grid Window', iconCls: 'icon-grid', module: 'grid-win' }
            ],*/
            trayItems: [
                { xtype: 'trayclock', flex: 1 }
            ]
        });
    },
    /**
     * 注销方法
     */
    onLogout: function () {
    	var me = this;
        Ext.Msg.confirm('提示', '你真要注销当前用户吗?',function(bt){
            if(bt=='yes'){
            	me.login.logout();
            }
        });
    },
    /**
     * 退出方法
     */
    onQuit: function(){
    	var me = this;
        Ext.Msg.confirm('提示', '你真要退出系统吗?',function(bt){
            if(bt=='yes'){
            	me.login.quit();
            }
        });
    },
    /**
     * 桌面设置
     */
    onSettings: function () {
        var dlg = Ext.create('Ext.ux.desktop.Settings',{
            desktop: this.desktop
        });
        dlg.show();
    },
    setMenu:function(){
    	var me = this;
    	me.login = new base.Login();
    	me.login.app = me;
    	me.login.desktop = me.desktop;
    	me.login.findUserSession();
    	grablEapLogin  = me.login;
    },
    /**
     * 权限设置
     */
    onUserInfo:function(){
    	var userInfoform = Ext.create('modules.power.UserInfo');
		// 表单窗口
		var userInfoWin = new WindowObject({
					layout : 'fit',
					title : '我的设置',
					//height : 500,
					autoHeight : true,
					width : 600,
					border : false,
					frame : false,
					modal : true,// 模态
					closeAction : 'hide',
					draggable:true,//拖动
					resizable:false, //变大小 
					items : [userInfoform]
				});
		userInfoWin.show();
		userInfoform.setFormData(base.Login.userSession.userId,function(result){});
    }
});
