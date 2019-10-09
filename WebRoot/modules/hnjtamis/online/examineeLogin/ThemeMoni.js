Ext.define('modules.hnjtamis.online.examineeLogin.ThemeMoni', {
	extend : 'modules.hnjtamis.online.examineeLogin.ThemeBase',
 	//获取考题
 	initExamTheme : function(){
 		var me = this;
 		if(me.isInitRead){
 			me.isInitRead = false;
	 		Ext.Ajax.request({
				url : me.requestHttp+'/onlineExam/gettitleForOnlineListAction!gettitle.action',
				params : {
					id : me.examUser.testPaperId
				},
				timeout: 60000,
				success: function(response) {
		    		var result = Ext.decode(response.responseText).rsContent;
		    		result = (Ext.decode(result));
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
			    	}else{
			    		Ext.Ajax.request({
						url : me.requestHttp+'/onlineExam/getansbakForOnlineListAction!getansbak.action',
						params : {
							id : me.examUser.testPaperId
						},
						timeout: 20000,
						success: function(ansresponse) {
				    		var ans = Ext.decode(ansresponse.responseText).rsContent;
				    		ans = (Ext.decode(ans));
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
  		Ext.Ajax.request({
			url : me.requestHttp+'/onlineExam/gettimeForOnlineListAction!gettime.action',
			timeout: 10000,
			success: function(response) {
	    		var result = Ext.decode(response.responseText).rsContent;
	    		result = (Ext.decode(result));
				me.systemTime =  result.result.date;
				if(me.handleTime!=null){
		 			clearInterval(me.handleTime);
		 		}
		 		me.handleTime = setInterval(function(){me.handleTimeFun();},1000);
		 		if(me.randomTimeSave!=null){
		 			clearInterval(me.randomTimeSave);
		 		}
		 		me.randomTimeSave = setInterval(function(){
		 			me.syncSystemTime();
		 			me.saveAnswer('autoSave');
		 		},me.syncSystemRandomTime);
			}
		});
 	},
 	//同步系统时间
 	syncSystemTime : function(){
 		var me = this;
 		Ext.Ajax.request({
			url : me.requestHttp+'/onlineExam/gettimeForOnlineListAction!gettime.action',
			timeout: 15000,
			success: function(response) {
	    		var result = Ext.decode(response.responseText).rsContent;
	    		if(result){
	    			result = (Ext.decode(result));
					me.systemTime =  result.result.date;
	    		}
			}
		});
 	},
 	//查看正确答案
 	rightAnsShow : function(){
 		var me = this;
 		if(me.rtAns == null){
 			me.rtAns = [];
 			for(var i=0;i<me.userThemes.length;i++){
 				me.rtAns[i] = null;
 			}
 		}
 		if(me.rtAns[me.currIndex-1] != null){
 			Ext.Msg.alert('正确答案', me.rtAns[me.currIndex-1]);
 		}else{
	 		Ext.Ajax.request({
				url : me.requestHttp+'/onlineExam/getansForOnlineListAction!getans.action',
				params : {
					id : me.examUser.testPaperId,
					currIndex:me.currIndex
				},
				timeout: 15000,
				success : function(response) {
					var result = Ext.decode(response.responseText).rsContent;
		    		result = (Ext.decode(result));
		    		var ans = result.result.answer;
		    		var ansStr = "";
		    		if(ans!=undefined){
		    			if(me.userTheme.themeType.type == '5' || me.userTheme.themeType.type == '10' || me.userTheme.themeType.type == '25'){
				    		for(var i=0;i<ans.length;i++){
				    			ansStr+=me.fmtStr(me.answerShowSort[parseInt(ans[i].ansSort)-1])+"、"+ans[i].value+" ";
				    		}
		    			}else{
		    				for(var i=0;i<ans.length;i++){
				    			ansStr+=(i+1)+"、"+ans[i].value+"<br>";
				    		}
		    			}
		    			me.rtAns[me.currIndex-1] = ansStr;
		    		}else{
		    			ansStr = "暂无正确答案！";
		    		}
		    		Ext.Msg.alert('正确答案', ansStr);
		    	},
				failure : function() {
					Ext.Msg.alert('提示', '答案获取失败，网络异常！');
				}
			});
 		}
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
 		if(type == 'submit' && (me.onlineMoniBodyMask != undefined && me.onlineMoniBodyMask!=null)){
 			me.onlineMoniBodyMask.show();
 		}
 		Ext.Ajax.request({
			url : me.requestHttp+'/onlineExam/saveansForOnlineListAction!saveans.action',
			params : {
				id : me.examUser.userTestPaperId,
				no : sorts,
				ans : ans,
				submitType : type
			},
			timeout: 120000,
			success : function(response) {
				var result = Ext.decode(response.responseText).rsContent;
	    		result = (Ext.decode(result));
				if(type == 'autoSave'){
					
				}else if(type == 'save'){
					if(result.code!='00001'){
		    			Ext.Msg.alert('提示',result.message);
			    	}else{
			    		Ext.Msg.alert('提示',"暂存成功！");
			    	}
				}else if(type == 'submit'){
					if(me.onlineMoniBodyMask != undefined && me.onlineMoniBodyMask!=null){
						me.onlineMoniBodyMask.hide();
					}
					if(result.code=='00001'){
						if(me.tabid!=undefined && me.tabid!=null && me.tabid!='' && me.tabid!='null'){
							Ext.Msg.show({
								title:'提示',
								msg:"交卷成功,本次考试总分"+result.sumscore+"分,得分 "+result.score+" 分,得分率"+result.dfl+"%！",
								buttonText: {yes:'查看答案', no:'关闭'},
								fn: function (btn) {
									window.parent.grablEapLogin.moniKsTableId = null;
									if(btn=='yes'){
										var url = me.requestHttp+'base/scorequery/showExamPaperDetailForExamScoreQueryListAction!showExamPaperDetail.action?testPaperId='+result.examTestpaperId+"&showExamTitle=0";
										var yjWin = Ext.create('Ext.window.Window', {
											id:'mytest',
										    title: '查看试题答案',
										    height: 600,
										    width: 800,
										    layout: 'fit',
										    modal:true,
										    html:"<iframe src='"+url+"' width='100%' height='100%' frameborder=0 scrolling=auto></iframe>",
										    closeAction : 'destroy',
										    listeners: {
											    close : function(){
											       window.parent.grablEapLogin.closeWindow(me.tabid);
											    }
											}
										});
										yjWin.show();
									}else{
										window.parent.grablEapLogin.closeWindow(me.tabid);
									}
							     }
							});
						}else{
							Ext.Msg.show({
								title:'提示',
								msg:"交卷成功,本次考试总分"+result.sumscore+"分,得分 "+result.score+" 分,得分率"+result.dfl+"%！",
								buttonText: {yes:'查看答案', no:'关闭'},
								fn: function (btn) {
									if(btn=='yes'){
										var url = me.requestHttp+'base/scorequery/showExamPaperDetailForExamScoreQueryListAction!showExamPaperDetail.action?testPaperId='+result.examTestpaperId+"&showExamTitle=0";
										var yjWin = Ext.create('Ext.window.Window', {
											id:'mytest',
										    title: '查看试题答案',
										    height: 600,
										    width: 800,
										    layout: 'fit',
										    modal:true,
										    html:"<iframe src='"+url+"' width='100%' height='100%' frameborder=0 scrolling=auto></iframe>",
										    closeAction : 'destroy',
										    listeners: {
											    close : function(){
											      var url = window.location.href.replace("clean=1","clean=0");
									    		  url = url.replace("addExam=1","addExam=0");
												  window.location = url;
											    }
											}
										});
										yjWin.show();
									}else{
										var url = window.location.href.replace("clean=1","clean=0");
						    			url = url.replace("addExam=1","addExam=0");
										window.location = url;
									}
							     }
							});
						}
		    			
			    	}else{
			    		Ext.Msg.alert('提示',result.message);
			    	}
		    	}
			},
			failure : function() {
				if(me.onlineMoniBodyMask != undefined && me.onlineMoniBodyMask!=null){
					me.onlineMoniBodyMask.hide();
				}
				Ext.Msg.alert('提示', '答案提交失败，网络异常！');
			}
		});
 	}
 });
