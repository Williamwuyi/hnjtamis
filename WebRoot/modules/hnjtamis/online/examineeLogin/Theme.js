Ext.define('modules.hnjtamis.online.examineeLogin.Theme', {
	extend : 'modules.hnjtamis.online.examineeLogin.ThemeBase',
 	//获取考题
 	initExamTheme : function(){
 		var me = this;
 		if(me.isInitRead){
 			me.isInitRead = false;
	 		Ext.data.JsonP.request({
				timeout: 40000,
				url : me.requestHttp + '/gettitle',
				callbackKey: "jsonpcallback",
				params : {
					examid : me.examUser.examId,
					inticket : me.examUser.inticket
				},
				success : function(result) {
					if(result.result == undefined || result.result =='' || result.result ==null){
						if(result.code=='00001'){
			    			Ext.Msg.alert('提示',"获取试题失败，请与管理员联系！",function(){
			    				window.location.reload();
			    			});
				    	}else{
			    			Ext.Msg.alert('提示',result.message,function(){
			    				window.location.reload();
			    			});
				    	}
		    			//me.initExamThemeFlag = false;
			    	}else{
			    		Ext.data.JsonP.request({
						timeout: 15000,
						url : me.requestHttp + '/getansbak',
						callbackKey: "jsonpcallback",
						params : {
							examid : me.examUser.examId,
							inticket : me.examUser.inticket
						},
						success : function(ans) {
							me.setInitExamTheme(result.result,ans.result)
						},
						failure : function() {
							Ext.Msg.alert('提示','获取答案失败，请与管理员联系！');
							me.initExamThemeFlag = false;
						}
			    		});
			    	}
			    	me.isInitRead = true;
			    	me.finTitleLoad = true;
				},
				failure : function() {
					Ext.Msg.alert('提示','获取试题失败，请与管理员联系！');
					me.initExamThemeFlag = false;
					me.isInitRead = true;
				}
			});
 		}
 	},
 	//初始化系统时间
 	initSystemTime : function(){
 		var me = this;
  		Ext.data.JsonP.request({
 			timeout: 15000,
			url : me.requestHttp+'/gettime',
			callbackKey: "jsonpcallback",
			success : function(result) {
				me.systemTime =  result.result.date;
				if(me.handleTime!=null){
		 			clearInterval(me.handleTime);
		 		}
		 		me.handleTime = setInterval(function(){me.handleTimeFun();},1000);
		 		setInterval(function(){
		 			me.syncSystemTime();
		 			me.saveAnswer('autoSave');
		 		},me.syncSystemRandomTime);
			}
		});
 	},
 	//同步系统时间
 	syncSystemTime : function(){
 		var me = this;
 		Ext.data.JsonP.request({
 			timeout: 15000,
			url : me.requestHttp+'/gettime',
			callbackKey: "jsonpcallback",
			success : function(result) {
				me.systemTime =  result.result.date;
			}
		});
 	},
 	//发送考生答案
 	sendAnswer : function(type){
 		var me = this;
 		var sorts = "";
 		var ans = "";
 		for(var i=0;i<me.sendAnswerIndexs.length;i++){
 			if(me.sendAnswerIndexs[i]!=null){
	 			sorts+=(me.sendAnswerIndexs[i]+1)+"@$@";
	 			ans+=me.fmtAns(me.getTheme(me.sendAnswerIndexs[i]))+"@$@";
 			}
 		}
 		if(sorts!="")sorts = sorts.substring(0,sorts.length-3);
 		if(ans!="")ans = ans.substring(0,ans.length-3);
 		Ext.data.JsonP.request({
 			timeout: 15000,
			url : me.requestHttp+'/saveans',
			callbackKey: "jsonpcallback",
			params : {
				examid : me.examUser.examId,
				inticket : me.examUser.inticket,
				no : sorts,
				ans : ans
			},
			success : function(result) {
				if(type == 'autoSave'){
					
				}else if(type == 'save'){
					if(result.code!='00001'){
		    			Ext.Msg.alert('提示',result.message);
			    	}else{
			    		Ext.Msg.alert('提示',"暂存成功！");
			    	}
				}else if(type == 'submit'){
					if(result.code!='00001'){
		    			Ext.Msg.alert('提示',result.message);
			    	}else{
			    		Ext.data.JsonP.request({
				 			timeout: 15000,
							url : me.requestHttp+'/exam',
							callbackKey: "jsonpcallback",
							params : {
								examid : me.examUser.examId,
								inticket : me.examUser.inticket
							},
							success : function(result) {
								Ext.Msg.alert('提示',"交卷成功！",function(){
									me.isSubmitFlag = 2;
									window.location.reload();
								});
							},
							failure : function() {
								Ext.Msg.alert('提示', '交卷提交失败，网络异常！');
							}
						});
			    	}
		    	}
			},
			failure : function() {
				Ext.Msg.alert('提示', '答案提交失败，网络异常！');
			}
		});
 	}
 });
