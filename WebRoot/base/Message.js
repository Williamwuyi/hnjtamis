/**
 * 桌面消息
 */
Ext.define('base.Message',{
 	singleton: true,
	constructor: function(conf){  
        
    },
    /**
     * 显示桌面消息notice通知
     * @param "" msg 消息内容
     * @param {} fn 点击消息内容处理函数
     * @param {} closeFn 消息框关闭事件处理函数
     * @param {} scope 以上两个方法的作用域
     */
    width : 226,
    height : 160,
	desktopMessage:function(msg,fn,closeFn,scope){
		var me = this;
		var id = 'desktopmessage_'+Math.random()*10000;
		var leftX = me.getLeftValue();
		if(!me.disabled)
        var tipWin = new Ext.Window({
            width:base.Message.width,  
            height:base.Message.height,  
            layout:'fit',  
            modal : false,  
            plain: true,  
            shadow:false, //去除阴影  
            draggable:false, //默认不可拖拽  
            resizable:false,  
            closable: true,  //可以关闭
            x: leftX, 
            //closeAction:'hide', //默认关闭为隐藏  
            //autoHide:15, //15秒后自动隐藏，false则不自动隐藏 
            autoHide : me.autoHide || false,
            title : '消息提醒',
            items:[{html:'<div valign="top" width="100%" height="100%"><a id="'+id+'">'+msg+'</a></div>'}],
            bbar:['->',{
			                xtype : 'button',
							text : '取消提示',
							handler :function(){
								me.disabled=true;
								tipWin.close();
							}}],							
            initEvents: function() {
            	this.initPosition(true);
            	this.on('beforeclose',function(){
            		if(closeFn)
            		closeFn.apply(scope);
            	});
                Ext.Window.superclass.initEvents.call(this);  
               
                this.on('beforeshow', this.showTips);  
                this.on('beforehide', this.hideTips);
                //window大小改变时，重新设置坐标  
                Ext.EventManager.onWindowResize(this.initPosition, this);
                //window移动滚动条时，重新设置坐标  
                Ext.EventManager.on(window, 'scroll', this.initPosition, this);
                
                 //自动隐藏  
                if(false !== this.autoHide){  
                    var task = new Ext.util.DelayedTask(this.close, this), 
                    second = (parseInt(this.autoHide) || 3) * 1000;  
                    this.on('show', function(self) {  
                        task.delay(second);  
                    });  
                }  
            },  
            //参数flag为true时强制更新位置  
            initPosition: function(flag) {
               //不可见时，不调整坐标
                if(true !== flag && this.hidden){  
                    return false;  
                }
                var doc = document, bd = (doc.body || doc.documentElement);  
                //Ext取可视范围宽高(与上面方法取的值相同), 加上滚动坐标 
                var left = bd.scrollLeft + Ext.getBody().getWidth()-4-this.width;  
                var top = bd.scrollTop + Ext.getBody().getHeight()-4-this.height-grabl_tabPageHeight;  
                this.setPosition(left, top);  
            },  
            showTips: function() {  
                var self = this;  
                if(!self.hidden){return false;}  
                //初始化坐标
                self.initPosition(true);   
                self.el.slideIn('b', {  
                    callback: function() {   
                   //显示完成后,手动触发show事件,并将hidden属性设置false,否则将不能触发hide事件   
                        self.fireEvent('show', self);  
                        self.hidden = false;  
                    }  
                });
                //不执行默认的show
                return false;   
            },  
            hideTips: function() {  
                var self = this;  
                if(self.hidden){return false;}  
                self.el.slideOut('b', {  
                    callback: function() {  
                        //渐隐动作执行完成时,手动触发hide事件,并将hidden属性设置true  
                        self.fireEvent('hide', self);  
                        self.hidden = true;  
                    }  
                });
                //不执行默认的hide
                return false;  
            }
            }).show();
            Ext.get(id).on('click',function(){
            	if(fn){
            	   fn.apply(scope);
            	   tipWin.close();
            	}
            });
            return tipWin;
},
	getLeftValue : function(){
		var doc = document, bd = (doc.body || doc.documentElement);  
       //Ext取可视范围宽高(与上面方法取的值相同), 加上滚动坐标 
       var left = bd.scrollLeft + Ext.getBody().getWidth()-4-base.Message.width;  
       return left;
	}
});