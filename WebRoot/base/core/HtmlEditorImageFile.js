/**
* @Description  Html编辑器 插入图片控件
* @author       张川(cr10210206@163.com)
*/
Ext.define('base.core.HtmlEditorImageFile', {
    extend: 'Ext.util.Observable',
    alias: 'widget.HtmlEditorImageFile',
    langTitle: '插入图片',
    imageWidth:'',
    imageHight:'',
    //langIconCls: 'heditImgIcon',
    init: function (view) {
        var scope = this;
        view.on('render', function () {
            scope.onRender(view);
        });
    },
 
    /**
    * 添加"插入图片"按钮
    * */
    onRender: function (view) {
        var scope = this;
        view.getToolbar().add({
            //iconCls: scope.langIconCls,
           icon : 'resources/icons/fam/image_add.png',
            tooltip: {
                title: scope.langTitle,
                width: 60
            },
            handler: function () {
                scope.showImgWindow(view);
            }
        });
    },
 
    /**
    * 显示"插入图片"窗体
    * */
    showImgWindow: function (view) {
        var scope = this;
        
        scope.form = ClassCreate('Ext.form.FormPanel',{
                        xtype: 'form',
                        layout: 'column',
                        autoScroll: true,
                        border: false,
                        defaults: {
                            columnWidth: 1,
                            labelWidth: 70,
                            labelAlign: 'right',
                            padding: '5 5 5 5',
                            allowBlank: false
                        },
                        items: [{
                        	xtype:'hidden',
                        	name:"json"
                        },{
                            xtype: 'fileuploadfield',
                            fieldLabel: '选择文件',
                            //beforeLabelTextTpl: this.getStar(),
                            allowBlank: true,
                            buttonText: '请选择...',
                            name: 'uploadImg',
                            emptyText: '请选择图片',
                            blankText: '图片不能为空',
                            listeners: {
                                change: function (view, value, eOpts) {
                                    //scope.uploadImgCheck();
                                }
                            }
                        }, {
                            xtype: 'textfield',
                            fieldLabel: '图片说明',
                            name: 'content',
                            allowBlank: true,
                            maxLength: 100,
                            emptyText: '简短的图片说明'
                        }, {
                            columnWidth: 1,
                            xtype: 'fieldset',
                            title: '上传须知',
                            layout: {
                                type: 'table',
                                columns: 1
                            },
                            collapsible: false, //是否可折叠
                            defaultType: 'label', //默认的Form表单组件
                            items: [{
                                html: '1.上传图片不超过100KB'
                            }, {
                                html: '2.为了保证图片能正常使用，我们支持以下格式的图片上传'
                            }, {
                                html: '   jpg,jpeg,png,gif'
                            }]
                        }],
                        buttons: [{
                            text: '保存',
                            action: 'btn_save',
                            icon: 'resources/icons/fam/save.gif',
                            handler: function (btn) {
                                scope.saveUploadImg(btn, view);
                            }
                        }, {
                            text: '取消',
                            icon: 'resources/icons/fam/cross.gif',
                            handler: function (btn) {
                                btn.up('window').close();
                            }
                        }]
                    });
        
        scope.winObject=new WindowObject({
            width: 400,
            height: 305,
            title: scope.langTitle,
            layout: 'fit',
            autoShow: true,
            modal: true,
            resizable: false,
            maximizable: false,
            constrain: true,
            plain: true,
            enableTabScroll: true,
            //bodyPadding: '1 1 1 1',
            closeAction:'hide',
            border: false,
            frame:true,
            items: [{
                xtype: 'tabpanel',
                enableTabScroll: true,
                bodyPadding: '1 1 1 1',
                items: [{
                    title: '上传本地图片',
                    items: [scope.form]
                }]
            }]
        });
    },
 
    /**
    * 上传图片验证
    **/
    uploadImgCheck: function (fileObj, fileName) {
        var scope = this;
        if(fileName == undefined || fileName == null || fileName==''){
        	 Ext.Msg.alert('提示', '请选中本地的一张图片');
        	 return false;
        }
        //图片类型验证
        if (!(scope.getImgTypeCheck(scope.getImgHZ(fileName)))) {
            Ext.Msg.alert('提示', '上传图片类型有误');
            fileObj.reset(); //清空上传内容
            return;
        }
    },
    
    /**
    * 获取图片后缀(小写)
    * */
    getImgHZ: function (imgName) {
        //后缀
        var hz = '';
        //图片名称中最后一个.的位置
        var index = imgName.lastIndexOf('.');
        if (index != -1) {
            //后缀转成小写
            hz = imgName.substr(index + 1).toLowerCase();
        }
        return hz;
    },
 
    /**
    * 图片类型验证
    * */
    getImgTypeCheck: function (hz) {
        var typestr = 'jpg,jpeg,png,gif,JPG,JPEG,PNG,GIF';
        var types = typestr.split(','); //图片类型
        for (var i = 0; i < types.length; i++) {
            if (hz == types[i]) {
                return true;
            }
        }
        return false;
    },
 
    /**
    * 上传图片
    * */
    saveUploadImg: function (btn, view) {
        var scope = this;
        var windowObj = scope.winObject; //获取Window对象
        var formObj = scope.form; //获取Form对象
        var fileName = formObj.getForm().findField('uploadImg').getValue();
        if(fileName == undefined || fileName == null || fileName==''){
        	 Ext.Msg.alert('提示', '请选中本地的一张图片');
        	 return false;
        }else if (!scope.uploadImgCheck(formObj.getForm().findField('uploadImg'), formObj.getForm().findField('uploadImg').getValue())&& 
		    formObj.isValid()) {
	        formObj.getForm().findField('json').setValue("");
			formObj.getForm().findField('json').setValue(Ext.encode(formObj.getForm().getValues(false)));
			formObj.getForm().submit({
				method:'POST', 
				waitTitle:'上传提示', 
				waitMsg:'正在上传,请稍候...',
				timeout: 600000,
				url:'exam/base/theme/saveImgageForThemeFormAction!saveImgage.action',
				success:function(form, action){
					var msg = action.result.msg; 
					if (!msg) msg = '操作成功！';
					Ext.Msg.alert('信息', msg,function(){
						//console.log(action.result.themeImageForm)
						scope.insertImg(view, action.result);
	                    windowObj.close(); //关闭窗体
					});
				},
				failure:function(form, action){
					if(action.failureType == 'server'){
						var obj = Ext.decode(action.response.responseText);
						if(Ext.isArray(obj)) obj = obj[0];
						 Ext.Msg.alert('上传失败', obj.errors,function(){
						       formObj.getForm().findField('uploadImg').focus();
						 }); 
					}else{ 
						Ext.Msg.alert('警告', '网络出现问题！'); 
					}                   
				} 
			});
		}			
        /*if (formObj.isValid()) { //验证Form表单
            formObj.form.doAction('submit', {
                url: this.projectLaction() + '/',
                method: 'POST',
                submitEmptyText: false,
                waitMsg: '正在上传图片,请稍候...',
                timeout: 60000, // 60s
                success: function (response, options) {
                    var result = options.result;
                    if (!result.success) {
                        Ext.MessageBox.alert('温馨提示', result.msg);
                        return;
                    }
                    scope.insertImg(view, result.data);
                    windowObj.close(); //关闭窗体
                },
                failure: function (response, options) {
                    Ext.MessageBox.alert('温馨提示', options.result.msg);
                }
            });
        }*/
    },
 
    /**
    * 保存远程的图片
    * */
    saveRemoteImg: function (btn, view) {
        var scope = this;
        var windowObj = scope.winObject; //获取Window对象
        var formObj = scope.form; //获取Form对象
        if (formObj.isValid()) {//验证Form表单
            var values = formObj.getValues(); //获取Form表单的值
            scope.insertImg(view, values);
            windowObj.close(); //关闭窗体
        }
    },
 
    /**
    * 插入图片
    * */
    insertImg: function (view, data) {
        var url = data.imageFilePath;
        var content = data.imageContent;
        var width = data.width;
        var height = data.height;
        var str = '<img src="' + url + '" border="0" ';
        if (content != undefined && content != null && content != '') {
            str += ' title="' + content + '" ';
        }
        if (width != undefined && width != null && width != 0) {
            str += ' width="' + width + '" ';
        }
        if (height != undefined && height != null && height != 0) {
            str += ' height="' + height + '" ';
        }
        str += ' />';
        view.insertAtCursor(str);
    },
    /**
	* 获取项目根路径
	* */
	 projectLaction : function () {
	    var curWwwPath = window.document.location.href;
	    var pathName = window.document.location.pathname;
	    var pos = curWwwPath.indexOf(pathName);
	    var localhostPath = curWwwPath.substring(0, pos);
	    var projectName = pathName.substring(0, pathName.substr(1).indexOf('/') + 1);
	    //console.log(localhostPath + projectName)
	    return (localhostPath + projectName);
	},
	/**
	* 获取小星星
	* */
	 getStar : function () {
	    return '<span style="color:#FF0000;">*</span>';
	}

});
