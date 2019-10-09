/**
 * 角色与机构对应
 */
ClassDefine('modules.hnjtamis.baseinfo.roleConfig.RoleConfig', {
	extend : 'base.model.List',
	requires : ['base.model.Tree','modules.baseinfo.Dictionary'], 
	initComponent : function() {
		var me = this;						
		this.columns = [
		    {name:'roleAssobjectId',width:0},
		    {header:'机构名',name:'organName',width:4,sortable:false,menuDisabled:true,titleAlign:"center",
		   		 renderer:function(value, metadata, record){ 
				    if(value!=undefined && value!=null && value!=""){
				    	metadata.tdAttr = " data-qtip = '"+value+"'";
				    }
				    metadata.style="white-space: normal !important;";
				    return value;
			}},
		    {header:'角色',name:'roleNames',width:8,sortable:false,menuDisabled:true,titleAlign:"center",
		    	renderer:function(value, metadata, record){ 
				    if(value!=undefined && value!=null && value!=""){
				    	metadata.tdAttr = " data-qtip = '"+value+"'";
				    }
				    metadata.style="white-space: normal !important;";
				    return value; 
			}}
		];
		me.pageRecordSize = 500;
		this.showTermSize = 0;//设定查询条件出现的个数
		this.terms = [];
		this.keyColumnName = "roleAssobjectId";// 主健属性名
		this.jsonParemeterName = "list";
		this.pageRecordSize = 10;
		this.viewOperater = false;
		this.addOperater = false;
		this.updateOperater = false;
		this.deleteOperater = false;
		this.otherOperaters = [];//其它扩展按钮操作
		this.listUrl = "baseinfo/roleConfig/listForRoleConfigListAction!list.action";// 列表请求地址
		this.deleteUrl = "baseinfo/roleConfig/deleteForRoleConfigListAction!delete.action";// 删除请求地址
		
		
		this.otherOperaters.push({
			 xtype : 'button',
			icon : 'resources/icons/fam/user_edit.png',
			text : eap_operate_takerole,
			handler : function() {
				var id = me.getSelectIds();
				if(id==""){
					Ext.Msg.alert('提示', '至少选择一条记录！');
					return;
				}
				var config = {
					width:500,
					height:500,
					 //数据提取地址
					selectUrl:"baseinfo/roleConfig/treeForRoleConfigListAction!tree.action?id="+id,
					checked:true,//是否复选
					selectType:'role',//只有角色结点数据才算
					selectTypeName:'角色',
					levelAffect:'20',//上下级复选框的影响策略
					keyColumnName : 'id',
					titleColumnName : 'title',
					childColumnName : 'children',
					title:'分配角色',
					selectObject:[]//选择的结点数组,支持ID数组及对象数组
				};
				//配置、回调函数
				base.model.Tree.openWin(config,function(ids,selectObject){//ID数组，对象数组
					var eapMaskTip = EapMaskTip(document.body);
					Ext.Ajax.request({
						method : 'get',
						url : "baseinfo/roleConfig/saveRoleForRoleConfigFormAction!saveRole.action?id="+id+"&roleIds="+ids,
						success : function(response) {
							var ret = Ext.decode(response.responseText);
							if(Ext.isArray(ret)) ret = ret[0];
							if(ret.success)
								Ext.Msg.alert('信息', '保存成功！');
							else
								Ext.Msg.alert('信息', ret.errors);
							eapMaskTip.hide();
							me.termQueryFun(true,'flash');
						},failure : function() {
							Ext.Msg.alert('信息', '后台未响应，网络异常！');
							eapMaskTip.hide();
						}
					});
				});
			 }
		});
		
		this.callParent();
	}
});