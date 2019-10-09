/**
 * 课件库
 */
ClassDefine('modules.hnjtamis.courseware.SharedCT', {
	extend : 'base.model.List',
	initComponent : function() {
		var me = this;
		// 模块列表对象
		this.columns = [{
			name : 'id',
			width : 0
		}, {
			name : 'title',
			header : '标题',
			width : 5
		}, {
			name : 'isAnnounced',
			header : '是否共享',
			width : 1,
			renderer : function(value){
				return value==0?"否":"是";
			}
		}, {
			name : 'organ.organName',
			header : '所在机构',
			width : 2
		}];
		// 模块查询条件对象
		this.terms = [{
					xtype : 'textfield',
					name : 'titleTerm',
					fieldLabel : '标题'
				}];
		this.keyColumnName = "id";// 主健属性名
		this.viewOperater = false;
		this.addOperater = false;
		this.deleteOperater = false;
		this.updateOperater = false;
		this.otherOperaters = [];//其它扩展按钮操作
		this.otherOperaters.push({
            xtype : 'button',
			icon : 'resources/icons/fam/accept.png',
			text : '引入',
			handler : function() {
				var id = me.getSelectIds();
				if(id==""||id.split(",").length>1){
					Ext.Msg.alert('提示', '请选择一条记录！');
					return;
				}
				Ext.MessageBox.confirm('询问', '确定引入本机构吗？',function(btn){
					if(btn!='yes') return;
					EapAjax.request({
						method : 'GET',
						url : 'kb/course/takeShareForCoursewareFormAction!takeShare.action?id='+id,
						async : true,
						success : function(response) {
							var result = Ext.decode(response.responseText);
							if (Ext.isArray(result)&&result[0].errors) {
								var msg = result[0].errors;
								Ext.Msg.alert('错误', msg);
							} else {
								Ext.Msg.alert('信息', result.msg);
								me.termQueryFun(false,'flash');
							}
						},
						failure : function() {
							Ext.Msg.alert('信息', '后台未响应，网络异常！');
						}
					});
				});
			}
		});
		
		this.listUrl = "kb/course/shareListForCoursewareListAction!shareList.action";// 列表请求地址
		this.deleteUrl = "";// 删除请求地址
		
		this.callParent();
	}
});