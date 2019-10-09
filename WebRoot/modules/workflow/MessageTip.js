/**
 * 流程执行的消息提醒
 */
Ext.define('modules.workflow.MessageTip', {
			singleton : true,//单例模式
			constructor : function(conf) {
				var excuteIds = [];//存放未处理的任务ID，在未重新登录时不再提示
				var task = { //Ext的定时器，每隔2秒执行
					run : function() {
						    Ext.TaskManager.stop(task);
							Ext.Ajax.request({
								method : 'get',
								url : 'workflow/taskTipForFlowTestAction!taskTip.action?employeeId='
								       +base.Login.userSession.employeeId,
								async : true,//是否异步
								success : function(response) {
									var result = Ext.decode(response.responseText);
									var have = false;
						    		Ext.Array.each(result.list,function(item){
						    			if(Ext.Array.contains(excuteIds,item.excuteId))
				                           return;
				                        if(have) return;
						    			var msg = '<table width="100%"><tr><td width="30%" align="right">事项：</td><td width="70%" style="word-wrap:break-word">'+
						    			         (item.param.title||item.taskName)+'</td></tr><tr><td align="right">结果：</td><td>'+item.toName+
						    			         '</td></tr><tr><td align="right">经办：</td><td>'+item.employeeName+'</td></tr></table>';
						    			have=true;
						    			base.Message.desktopMessage(msg,function(){
						    				Ext.Ajax.request({
												method : 'get',
												url : 'workflow/canelTipForFlowTestAction!canelTip.action?excuteId='
												       +item.excuteId,
												async : true//是否异步
				                            });
				                            var id = item.param.id;
						    				var nodeUrl = item.workFlowNode.url;
											if(!nodeUrl){
												Ext.Msg.alert('错误', '任务无打开页面！');
												return;
											}
											if(nodeUrl.indexOf("?")<0)
									           nodeUrl+="?random=1";
											nodeUrl = nodeUrl.replace(".js?","?");
											var match = nodeUrl.split(RegExp("[?&]"));
											var config = {};
											for(var i=1;i<match.length;i++){
											    var p = match[i].split("=");
											    if(p[0]!='random')
											      config[p[0]]=p[1];
											}
											nodeUrl = match[0];
											nodeUrl = nodeUrl.replace(RegExp('/','g'),".");
						    				var grid = ClassCreate(nodeUrl,config);
				                            grid.openFormWin(id,null,true,null,null,'view');
						    			},function(){Ext.TaskManager.start(task);excuteIds.push(item.excuteId);});
						    		});
								},
								failure : function() {
									Ext.Msg.alert('信息', '后台未响应，网络异常！');
								}
							});	
					},
					interval : 2000
				}
				Ext.TaskManager.start(task);//启动定时器
			}
		});