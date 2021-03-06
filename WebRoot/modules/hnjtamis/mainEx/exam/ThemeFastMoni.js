Ext.define('modules.hnjtamis.mainEx.exam.ThemeFastMoni', {
	mixins: {
        observable: 'Ext.util.Observable'
    },
    requestHttp : 'http://127.0.0.1:80',//提交的地址
    examUser : {},//考生信息
 	userExamId : '',//科目ID
	userTestPaperId : '',//试卷ID
	userThemeTypes : [],//题目类型
	userThemes : [],//题目
	themeShowSort : ["一","二","三","四","五","六","七","八","九","十"],
	answerShowSort : ["A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"],//答案序号
	//jsonThemeTitle : '',//传递过来的JSON String
	themesTitle : {},//传递过来的JSON String转为对象
	finsize : 0,//完成题目的数量
	currIndex : 0,//当前选中题目
 	cp : new Ext.state.CookieProvider(),//cookie对象
 	sendAnswerIndexs : [],
 	sendAnswerFlag : true,//发送标识
	isHidden : 0,//部分样式的隐藏
	isSubmitFlag : 1,//是否进行了提交 1-不是 2-是 用于关闭时判断用
	isInitRead : true,//正常初始化读取,
	tabid : '',//打开页签ID
	finTitleLoad : false,
	rtAns : null,
 	constructor : function(config){//构造函数
 		var me = this;
 		me.mixins.observable.constructor.call(this, config);
 		me.initComponent();
 	},
 	//数据的初始化
 	initComponent : function() {
 		var me = this;
 		me.initExamThemeFlag = true;
 		me.finTitleLoad = false;
 		Ext.state.Manager.setProvider(me.cp);
 		me.initExamTheme();
 	},
 	//获取考题
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
 	//设置初始化试题
 	setInitExamTheme : function(themesTitle,themesAns){
 			var me = this;
 			if(me.initExamThemeFlag){
	 			me.initExamThemeFlag = false;
			    me.themesTitle = themesTitle;
			    me.userThemes = [];
			    //以下解析给页面对象方面处理
				//me.themesTitle = Ext.decode(me.jsonThemeTitle);
				me.userExamId = me.themesTitle.userExamId;
				me.userTestPaperId = me.themesTitle.testPaperId;
				if(me.themesTitle.themeType.length){
					 for(var i = 0 ;i<me.themesTitle.themeType.length;i++){
					 	me.userThemeTypes[i] = (me.themesTitle.themeType)[i];
					 	for(var j = 0;j<(me.themesTitle.themeType)[i].theme.length;j++){
					 		var userThemesIndex = me.userThemes.length;
					 		me.userThemes[userThemesIndex] = ((me.themesTitle.themeType)[i].theme)[j];
					 		me.userThemes[userThemesIndex].signFlag = false;
					 		me.userThemes[userThemesIndex].themeType = me.userThemeTypes[i];
					 		me.userThemes[userThemesIndex].themeTypeShowSort = me.themeShowSort[i];
					 		me.userThemes[userThemesIndex].themeTypeLen = (me.themesTitle.themeType)[i].theme.length;
					 		me.userThemes[userThemesIndex].userAnswerArr = [];//答案数组
					 		me.userThemes[userThemesIndex].userAnswer = '';
					 		me.userThemes[userThemesIndex].sortIndex = j+1;

					 		me.userThemes[userThemesIndex].value = me.decode(me.userThemes[userThemesIndex].value);
					 		for(var kk = 0;kk<me.userThemes[userThemesIndex].answer.length;kk++){
					 			(me.userThemes[userThemesIndex].answer)[kk].ansValue = me.decode((me.userThemes[userThemesIndex].answer)[kk].ansValue);
					 		}
					 	}
					 }
				 }
				 //原来数据的初始化
				 if(themesAns!=null && themesAns!='' && themesAns!=undefined){
			    	var noArr = themesAns.no.split("@$@");
			    	var ansArr = themesAns.ans.split("@$@");
			    	for(var i = 0;i<noArr.length;i++){
			    		if(noArr[i]!=null && noArr[i]!="" && ansArr[i]!=undefined && ansArr[i]!=null && ansArr[i]!=""){
			    			var themeIndex_ = parseInt(noArr[i])-1;
			    			me.userThemes[themeIndex_].userAnswerArr = ansArr[i].split("#*#");
			    			me.userThemes[themeIndex_].userAnswer =  ansArr[i];
			    			
			    			if(me.userThemes[themeIndex_].userAnswerArr == undefined){
			    			     me.userThemes[themeIndex_].userAnswerArr = [];
			    			     me.userThemes[themeIndex_].userAnswer = '';
			    			}else{
				    			//清理空数据
					 			for(var kk = 0;kk<me.userThemes[themeIndex_].userAnswerArr.length;){
									if(me.userThemes[themeIndex_].userAnswerArr[kk]==null ||  me.userThemes[themeIndex_].userAnswerArr[kk]=="null"){
										me.userThemes[themeIndex_].userAnswerArr.delItem(kk);
									}else{
										//注意，选择题（单选、多选、判断） 答案数组为(显示字符,序号(从0开始))
										if(me.userThemes[themeIndex_].themeType.type == '5' 
											|| me.userThemes[themeIndex_].themeType.type == '10' 
											|| me.userThemes[themeIndex_].themeType.type == '25'){
											for(var ttt=0;ttt<me.answerShowSort.length;ttt++){
												if(me.answerShowSort[ttt] == me.userThemes[themeIndex_].userAnswerArr[kk]){
													var tmpArr = new Array();
													tmpArr[0] = me.userThemes[themeIndex_].userAnswerArr[kk];
													tmpArr[1] = ttt+1;
													me.userThemes[themeIndex_].userAnswerArr[kk] = tmpArr;
													break;
												}
											}
										}
										kk++;
									}
								}
			    			}
			    		}
			    	}
			     }
			     
			     if(me.userThemes.length>0){
				     me.oldThemeTypeName = "";
				     var dataTable = document.getElementById("examTable");
				     var newRow = null;
				     for(var i=0;i<me.userThemes.length;i++){
				     	me.userTheme = me.userThemes[i]
				     	if(me.oldThemeTypeName != me.userTheme.themeType.themeTypeName){
				     		//题型
				     		//var htmlStr = "<tr><th colspan=\"2\">>";
								            
							var htmlStr=me.fmtStr(me.userTheme.themeTypeShowSort)+"、"+me.fmtStr(me.userTheme.themeType.themeTypeName);
							htmlStr+="（共"+(me.fmtStr(me.userTheme.themeTypeLen))+"题）";
							//htmlStr+="</th></tr>";
							
							newRow = dataTable.insertRow();
							var newCell = newRow.insertCell();
							newCell.innerHTML=htmlStr;
							newCell.className = "tdhead";
							newCell.colSpan = 2;
		 				    me.oldThemeTypeName = me.userTheme.themeType.themeTypeName;
				     	}
				     	newRow = dataTable.insertRow();
				 		me.setThemeHtml(i,me.userThemes[i],newRow);
				 	 }
				 	 
				 	 document.getElementById("savebutton").disabled = false;
		 		     document.getElementById("submitbutton").disabled = false;
		 		     setTimeout(me.setAllRightAns(),500)
			     }
 			}
 	},
 	//获取试题
 	getTheme : function (index){
 		return index < this.userThemes.length ? this.userThemes[index] : null;
 	},
 	//显示试题
 	setThemeHtml : function(index,mTheme,newRow){
 		var me = this;
 		me.userTheme = mTheme;
	 		//试题与答案
	 		var explain= me.userTheme.explain;
	 		 //类型 5：单选；10：多选 15：填空；20：问答；25：判断；30：视听 35：其他
	 		if(me.userTheme.themeType.type == '5' || me.userTheme.themeType.type == '10' || me.userTheme.themeType.type == '25'){
	 			me.themeTableStr = "<p>";
		 		me.themeTableStr +=	(me.userTheme.sortIndex)+"、"+me.fmtStr(me.userTheme.value);
		 		if(me.userTheme.fraction && me.userTheme.fraction!=null && me.userTheme.fraction!='null'){
		 			me.themeTableStr +="（"+(me.userTheme.fraction)+"分）";
		 		}
		 		if(explain!=undefined && explain!="" && explain!="null" && explain!=null){
					me.themeTableStr +="（注解："+explain+"）";
				}
				me.userTheme.userAnswer = "";
		 		if(me.userTheme.userAnswerArr && me.userTheme.userAnswerArr.length>0){
				 	for(var j=0;j<me.userTheme.userAnswerArr.length;j++){
					   me.userTheme.userAnswer+=me.userTheme.userAnswerArr[j][0]+",";
					}
					if(me.userTheme.userAnswer!="")me.userTheme.userAnswer=me.userTheme.userAnswer.substring(0,me.userTheme.userAnswer.length-1);
		 		}
		 		if(me.userTheme.themeType.type == '5' || me.userTheme.themeType.type == '25'){
		 			me.themeTableStr+="<br><font style='font-weight: bold;color:blue'>您选择的答案：</font><input  type='text' class='inputThemeCss' maxlength='50' name='ansText"+index+"' id='ansText"+index+"' value='"+me.userTheme.userAnswer+"' style='width:50px;text-align:center;' />";
		 		}else{
		 			me.themeTableStr+="<br><font style='font-weight: bold;color:blue'>您选择的答案：</font><input  type='text' class='inputThemeCss' maxlength='50' name='ansText"+index+"' id='ansText"+index+"' value='"+me.userTheme.userAnswer+"' style='width:110px;text-align:center;' />";
		 		}
	 			me.themeTableStr+="</p>";
				me.themeTableStr += "<ul>";
			    for(var i = 0;i<me.userTheme.answer.length;i++){
			    	var themeClass = 'normal';
			    	for(var j=0;j<me.userTheme.userAnswerArr.length;j++){
			    		if(me.answerShowSort[i] == me.userTheme.userAnswerArr[j][0]){
			    			themeClass = 'bold';
			    			break;
			    		}
			    	}
			    	me.themeTableStr += "<li name='ans"+index+"' onClick=\"theme.setAnswer(this,"+i+","+index+")\" style=\"font-weight:"+themeClass+";\">";
			 		me.themeTableStr += me.fmtStr(me.answerShowSort[i])+"、";
			 		me.themeTableStr +=	me.fmtStr((me.userTheme.answer)[i].ansValue);
			 		me.themeTableStr += "</li>";
			    }
			    me.themeTableStr += "</ul>";
			    
			    me.themeTableStr += "<div class=\"divAnswer\" style=\"display:none;\" id=\"rightAnsDiv"+index+"\"><div><span style=\"color:#999999\">正确答案：</span></div></div>";
			    
				me.themeTableStr = me.replaceImgPath(me.themeTableStr);
				var newCell = newRow.insertCell();
				newCell.innerHTML=me.themeTableStr;
				newCell.width="90%";
				newCell.valign="top" ;
				newCell.className="td1";
				
				
				newCell = newRow.insertCell();
				var ss = "";
				if(me.userTheme.signFlag == true){
	 				ss = "<div id=\"signdiv"+index+"\" class=\"divOK\" onClick=\"theme.setSignFlag("+index+",this,true)\">取消标记</div>";
	 		    }else{
	 				ss = "<div id=\"signdiv"+index+"\" class=\"div1\"  onClick=\"theme.setSignFlag("+index+",this,true)\">标记本题</div>";
	 		    }
	 		    if(me.relationType != 'mocs'){
	 		    	ss+="<div class=\"div2\" onClick=\"theme.rightAnsShow("+index+",this)\">查看答案</div>";
	 		    }
	 		    ss+="<div onClick=\"theme.fkThemeShow("+index+",this)\">试题反馈</div>";
				newCell.innerHTML=ss;
				newCell.width="10%";
				newCell.valign="bottom" ;
				newCell.className="td2";
	 		}else if(me.userTheme.themeType.type == '15'){
	 			me.themeTableStr = ' <p>';
		 		var themeStr = me.replaceString(me.userTheme.value,"（）","()");
	 			var tmpArr = (","+themeStr+",").split("()");
	 			var tmpArrStr = "";
	 			var inputIndex = 0;
	 			for(var i=0;i<tmpArr.length;i++){
	 				var ansValue_ = "";
	 				if(me.userTheme.userAnswerArr.length>0){
	 					ansValue_ = me.userTheme.userAnswerArr[inputIndex];
	 				}
	 				ansValue_ = me.decode(ansValue_);
	 				if(i<tmpArr.length-1){
	 					tmpArrStr+=tmpArr[i]+"<input  type='text' class='inputThemeCss' maxlength='100'  name='ansText"+index+"' onblur='theme.setAnswer(this,"+i+","+index+")' value='"+ansValue_+"' />";
	 					inputIndex++;
	 				}else{
	 					tmpArrStr+=tmpArr[i]
	 				}
	 			}
	 			tmpArrStr = tmpArrStr.substring(1,tmpArrStr.length-1);
	 			me.themeTableStr +=	(me.userTheme.sortIndex)+"、"+tmpArrStr;
	 			if(me.userTheme.fraction && me.userTheme.fraction!=null && me.userTheme.fraction!='null'){
	 				me.themeTableStr +="（"+(me.userTheme.fraction)+"分）";
	 			}
	 			if(explain!=undefined && explain!="" && explain!="null" && explain!=null){
					me.themeTableStr +="（注解："+explain+"）";	
				}
			    me.themeTableStr += " </p>";
			    me.themeTableStr += "<div class=\"divAnswer\" style=\"display:none;\" id=\"rightAnsDiv"+index+"\"><div><span style=\"color:#999999\">正确答案：</span></div></div>";
	 			me.themeTableStr = me.replaceImgPath(me.themeTableStr);

	 			var newCell = newRow.insertCell();
				newCell.innerHTML=me.themeTableStr;
				newCell.width="90%";
				newCell.valign="top" ;
				newCell.className="td1";
				newCell = newRow.insertCell();
				var ss = "";
				if(me.userTheme.signFlag == true){
	 				ss = "<div class=\"divOK\" onClick=\"theme.setSignFlag("+index+",this,true)\">取消标记</div>";
	 		    }else{
	 				ss = "<div class=\"div1\" onClick=\"theme.setSignFlag("+index+",this,true)\">标记本题</div>";
	 		    }
	 		    if(me.relationType != 'mocs'){
               		 ss+="<div class=\"div2\" onClick=\"theme.rightAnsShow("+index+",this)\">查看答案</div>";
	 		    }
	 		    ss+="<div onClick=\"theme.fkThemeShow("+index+",this)\">试题反馈</div>";
				newCell.innerHTML=ss;
				newCell.width="10%";
				newCell.valign="bottom" ;
				newCell.className="td2";
	 		}else if(me.userTheme.themeType.type != undefined){
	 			me.themeTableStr = '<p>';//<table width="97%" border="0" cellpadding="0" cellspacing="0" class="R_answer" align="center" >';
		 		me.themeTableStr +=	(me.userTheme.sortIndex)+"、"+me.fmtStr(me.userTheme.value);
	 			if(me.userTheme.fraction && me.userTheme.fraction!=null && me.userTheme.fraction!='null'){
		 			me.themeTableStr +="（"+(me.userTheme.fraction)+"分）";
		 			
		 		}
		 		if(explain!=undefined && explain!="" && explain!="null" && explain!=null){
					me.themeTableStr +="（注解："+explain+"）";	
				}
				me.themeTableStr += "</p>";
				me.themeTableStr += "<div class=\"divAnswer\" style=\"display:none;\" id=\"rightAnsDiv"+index+"\"><div><span style=\"color:#999999\">正确答案：</span></div></div>";
			    
		 		var ansValue_ = "";
	 			if(me.userTheme.userAnswerArr.length>0 && me.userTheme.userAnswerArr[0]!=null && me.userTheme.userAnswerArr[0]!="null"){
	 				ansValue_ = me.userTheme.userAnswerArr[0];
	 				ansValue_ = me.decode(ansValue_);
	 			}
				me.themeTableStr += "<textarea  rows='10' cols='100' name='ansText"+index+"' onblur='theme.setAnswer(this,"+0+","+index+")'>"+ansValue_+"</textarea>";
				me.themeTableStr = me.replaceImgPath(me.themeTableStr);
				newCell = newRow.insertCell();
				var ss = "";
				if(me.userTheme.signFlag == true){
	 				ss = "<div class=\"divOK\" onClick=\"theme.setSignFlag("+index+",this,true)\">取消标记</div>";
	 		    }else{
	 				ss = "<div class=\"div1\" onClick=\"theme.setSignFlag("+index+",this,true)\">标记本题</div>";
	 		    }
	 		    if(me.relationType != 'mocs'){
                	ss+="<div class=\"div2\" onClick=\"theme.rightAnsShow("+index+",this)\">查看答案</div>";
	 		    }
	 		    ss+="<div onClick=\"theme.fkThemeShow("+index+",this)\">试题反馈</div>";
				newCell.innerHTML=ss;
				newCell.width="10%";
				newCell.valign="bottom" ;
				newCell.className="td2";
	 		}
	 		me.currIndex = index;
 	},
 	replaceImgPath : function(imagePath){
 		return imagePath.replace(/src=\"upload/g ,'src="../upload');
 	},
 	//替换
 	replaceString : function(str,reallyDo,replaceWith) { 
		var e=new RegExp(reallyDo,"g"); 
		words = str.replace(e, replaceWith); 
		return words; 
	},
 	//格式化数据
 	fmtStr : function(value){
 		try{
	 		if(value==undefined || value == "" || value=="null" || value=="undefined"){
	 			return "";
	 		}else{
	 			return value;
	 		}
 		}catch(e){
 			return "";
 		}
 	},
 	getByName : function(Name){ 
	    var aEle=document.getElementsByTagName('*'); 
	    var arr=[];  
	    for(var i=0;i<aEle.length;i++){
	        if(aEle[i].getAttribute("name")==Name){
	            arr.push(aEle[i])
	         }
	     }
	     return arr; 
    },
 	//设置考生答案
 	setAnswer : function(answerObj,inputIndex,themeIndex){
 		var me = this;
 		me.currIndex = themeIndex;
 		me.userTheme = me.userThemes[themeIndex];
 		if(me.userTheme.themeType.type == '5' || me.userTheme.themeType.type == '10' || me.userTheme.themeType.type == '25'){
 			var ans = me.getByName("ans"+themeIndex);
 			var answer = me.answerShowSort[inputIndex];	
 			var ansAddFlag = true;
 			for(var i=0;i<me.userTheme.userAnswerArr.length;i++){
 				if(me.userTheme.userAnswerArr[i][0] == answer){
 					me.userTheme.userAnswerArr[i] = null;
 					ansAddFlag = false;
 					break
 				}
 			}
 			if(ansAddFlag){
 				var sort = -1;
 				for(var i =0;i<me.answerShowSort.length;i++){
 					if(me.answerShowSort[i] == answer){
 						sort = i+1;
 						break;
 					}
 				}
 				if(sort!=-1){
 					me.userTheme.userAnswerArr[me.userTheme.userAnswerArr.length] = [answer,sort];
 				}
 			}
 			//单选与判断处理，只能选择一个
 			if(me.userTheme.themeType.type == '5' || me.userTheme.themeType.type == '25'){
 				
 				for(var i=0;i<ans.length;i++){
 					ans[i].style.fontWeight = 'normal';
 				}
 				for(var i = 0;i<me.userTheme.userAnswerArr.length;i++){
					if(me.userTheme.userAnswerArr[i]!=null
						&& me.userTheme.userAnswerArr[i][1] != (inputIndex+1)){
	 					me.userTheme.userAnswerArr[i] = null;
					}
 				}
 			} 
 			if(answerObj.style.fontWeight == 'bold'){
 				answerObj.style.fontWeight = 'normal';
 			}else{
 				answerObj.style.fontWeight = 'bold';
 			}
 			
 			//清理空数据
 			for(var i = 0;i<me.userTheme.userAnswerArr.length;){
				if(me.userTheme.userAnswerArr[i]==null){
					me.userTheme.userAnswerArr.delItem(i);
				}else{
					i++;
				}
			}
			
			
 			//重新排序
			me.userTheme.userAnswer = "";
			if(me.userTheme.userAnswerArr!=undefined && me.userTheme.userAnswerArr.length>1){
	 			for(var i = 0;i<me.userTheme.userAnswerArr.length-1;i++){
		 			for(var j = i+1;j<me.userTheme.userAnswerArr.length;j++){
		 				if(me.userTheme.userAnswerArr[i][1] > me.userTheme.userAnswerArr[j][1]){
		 					var tmpArrValue = me.userTheme.userAnswerArr[i];
		 					me.userTheme.userAnswerArr[i] = me.userTheme.userAnswerArr[j];
		 					me.userTheme.userAnswerArr[j] = tmpArrValue;
		 				}
		 			}
	 			}
	 			for(var i = 0;i<me.userTheme.userAnswerArr.length;i++){
 					me.userTheme.userAnswer+=me.userTheme.userAnswerArr[i][0]+",";
 				}
 				if(me.userTheme.userAnswer!="")me.userTheme.userAnswer=me.userTheme.userAnswer.substring(0,me.userTheme.userAnswer.length-1);
			}else if(me.userTheme.userAnswerArr==undefined){
				me.userTheme.userAnswerArr = [];
			}else if(me.userTheme.userAnswerArr.length == 1){
				me.userTheme.userAnswer=me.userTheme.userAnswerArr[0][0];
			}
 			//清理空值重新加入
 	
 			document.getElementById("ansText"+themeIndex).value = me.userTheme.userAnswer;
 		}else{
 			var answer = answerObj.value;
 			//初始化
 			if(me.userTheme.userAnswerArr[inputIndex]==undefined){
 				for(var i=0;i<inputIndex+1;i++){
	 				if(me.userTheme.userAnswerArr[i]==undefined){
	 					me.userTheme.userAnswerArr[i] = '';
	 				}
 				}
 			}
 			me.userTheme.userAnswerArr[inputIndex]= me.encode(answer);
 			me.userTheme.userAnswer = "";
 			if(me.userTheme.userAnswerArr.length>0){
	 			me.userTheme.userAnswer = me.fmtAns(me.userTheme);
 			}
 		}
 		//添加需要发送处理的答案
 		me.addSendAnswerIndexs(me.currIndex);
 	},
	 encode : function (strIn) {
	 	if(strIn==undefined || strIn==null || strIn=='null'){
	 		return '';
	 	}
	    var intLen = strIn.length;
	    var strOut = "";
	    var strTemp;
	    for (var i = 0; i < intLen; i++) {
	        strTemp = strIn.charCodeAt(i);
	        if (strTemp > 255) {
	            tmp = strTemp.toString(16);
	            for (var j = tmp.length; j < 4; j++) tmp = "0" + tmp;
	            strOut = strOut + "^" + tmp;
	        } else {
	            if (strTemp < 48 || (strTemp > 57 && strTemp < 65) || (strTemp > 90 && strTemp < 97) || strTemp > 122) {
	                tmp = strTemp.toString(16);
	                for (var j = tmp.length; j < 2; j++) tmp = "0" + tmp;
	                strOut = strOut + "~" + tmp;
	            } else {
	                strOut = strOut + strIn.charAt(i);
	            }
	        }
	    }
	    return (strOut);
	},
	decode : function (strIn) {
		if(strIn==undefined || strIn==null || strIn=='null'){
	 		return '';
	 	}
	    var intLen = strIn.length;
	    var strOut = "";
	    var strTemp;
	    for (var i = 0; i < intLen; i++) {
	        strTemp = strIn.charAt(i);
	        switch (strTemp) {
	            case "~":{
	                strTemp = strIn.substring(i + 1, i + 3);
	                strTemp = parseInt(strTemp, 16);
	                strTemp = String.fromCharCode(strTemp);
	                strOut = strOut + strTemp;
	                i += 2;
	                break;
	            }
	            case "^":{
	                strTemp = strIn.substring(i + 1, i + 5);
	                strTemp = parseInt(strTemp, 16);
	                strTemp = String.fromCharCode(strTemp);
	                strOut = strOut + strTemp;
	                i += 4;
	                break;
	            }
	            default:{
	                strOut = strOut + strTemp;
	                break;
	            }
	        }
	    }
	    return (strOut);
	},
	//查看正确答案
 	setAllRightAns : function(themeIndex,obj){
 		var me = this;
 		if(me.rtAns == null){
 			me.rtAns = [];
 			for(var i=0;i<me.userThemes.length+1;i++){
 				me.rtAns[i] = null;
 			}
 		}
 		Ext.Ajax.request({
					url : me.requestHttp+'/onlineExam/getAllAnsForOnlineListAction!getAllAns.action',
					params : {
						id : me.examUser.testPaperId
					},
					timeout: 150000,
					success : function(response) {
						var result = Ext.decode(response.responseText).rsContent;
			    		result = (Ext.decode(result));
			    		var anslist = result.result;
			    		for(var t=0;t<anslist.length;t++){
			    			var rslt = anslist[t];
			    			//var ss = rslt.sort;
			    			var anss = rslt.rightAns;
			    			var sg = rslt.sg;
			    			var ansStr = "";
				    		if(ans!=undefined){
				    			var ans = anss.answer;
				    			me.userTheme = me.userThemes[t];
				    			if(me.userTheme.themeType.type == '5' || me.userTheme.themeType.type == '10' || me.userTheme.themeType.type == '25'){
						    		for(var i=0;i<ans.length;i++){
						    			ansStr+=me.fmtStr(me.answerShowSort[parseInt(ans[i].ansSort)-1])+"、"+ans[i].value+" ";
						    		}
				    			}else{
				    				for(var i=0;i<ans.length;i++){
						    			ansStr+=(i+1)+"、"+ans[i].value+"<br>";
						    		}
				    			}
				    			me.rtAns[t] = ansStr;
				    		}else{
				    			ansStr = "暂无正确答案！";
				    		}
				    		if(sg && sg=='t'){
				    			me.setSignFlag(t,document.getElementById("signdiv"+t),false);
				    		}
			    			
			    		}
			    	},
					failure : function() {}
				});
 	},
 	//试题反馈
 	fkThemeShow : function(themeIndex,obj){
 		var me = this;
 		me.userTheme = me.userThemes[themeIndex];
 		var _fkthemeId = me.userTheme.themeId;
 		Ext.Ajax.request({
				url : me.requestHttp+'/onlineExam/fkThemeInExamThemeShowForOnlineListAction!fkThemeInExamThemeShow.action',
				params : {
					id : _fkthemeId,
					currIndex:me.currIndex
				},
				timeout: 15000,
				method : 'POST',
				success : function(response) {
						var result = Ext.decode(response.responseText);
			    		var ansfktheme = result.fktheme;	
			    		var themeFkauditFormList = result.themeFkauditFormList;
			    		var htmlStr = "<div style='width:100%;overflow-x:auto;overflow-y:hidden;word-break: break-all; word-wrap:break-word;'>";
						if(ansfktheme.lastFkState=='20' || ansfktheme.lastFkState=='30' || ansfktheme.lastFkState=='40' || ansfktheme.lastFkState=='-40'){
							htmlStr+="<div style='font-weight: normal; font-size: 14px;width:100%;text-align:right;padding:10px;'>"
							htmlStr+="<input type=\"button\" class=\"R_btn04\" value=\" \" alt=\"关闭\" onclick=\"fkMoniThemeWin.hide();\" />";      
							htmlStr+="</div>";
							if(themeFkauditFormList && themeFkauditFormList.length && themeFkauditFormList.length>0){
								htmlStr+="<div style='overflow-x:auto;overflow-y:auto;height:260px;width:690px;font-weight: normal; font-size: 14px;word-break: break-all; word-wrap:break-word;padding:6px;'>";
								for(var i=0;i<themeFkauditFormList.length;i++){
									var fkauditRemark = themeFkauditFormList[i].fkauditRemark;
									var createdBy = themeFkauditFormList[i].createdBy;
									var creationDate = themeFkauditFormList[i].creationDate;
									var sssss = "<font color='#0000FF'>"+createdBy+"["+creationDate+"]：</font><br>"+fkauditRemark+"<br><br>";
									htmlStr+= me.replaceString(sssss,"\n","<br>");
								}
								htmlStr+="</div>";
							}
						}else{
							htmlStr+="<div style='font-weight: normal; font-size: 14px;width:100%;text-align:right;padding:10px;'>"
							htmlStr+="<input type=\"button\" class=\"R_btn03\" value=\" \" alt=\"提交反馈\" onclick=\"fkMoniThemeWin.sumitMoniFk();\"/>";
        					htmlStr+="<input type=\"button\" class=\"R_btn04\" value=\" \" alt=\"关闭\" onclick=\"fkMoniThemeWin.hide();\" />";      
							htmlStr+="</div>";
						
							htmlStr+="<div style='font-weight: normal; font-size: 20px;width:100%;padding:10px;padding-bottom:3px;padding-top:3px;'>反馈说明：</div>";
							htmlStr+="<div style='font-weight: normal; font-size: 13px;word-break: break-all; word-wrap:break-word;padding:10px;padding-top:3px;'>"
							htmlStr+="<textarea name='fkRemark_"+_fkthemeId+"' id='fkRemark_"+_fkthemeId+"'  rows='10' cols='300' style='width:650;height:220'></textarea></div>";	
						}
						htmlStr+="</div>";
						htmlStr+="<div style='width:100%;overflow-x:auto;overflow-y:hidden;word-break: break-all; word-wrap:break-word;padding-left:10px;padding-right:10px;'>";
						htmlStr+="<font color=red>注意：请详细填写反馈的问题，如：答案A.压强、速度和粘度；存在**问题，不要只反馈：答案A存在错误，因为您查看的答案顺序可能与系统中试题管理中的答案顺序不一致，系统在出题时会根据情况打乱答案顺序。</font>";
						htmlStr+="</div>";
						fkMoniThemeWin = new Ext.Window({
							layout:'fit',
							title:'试题反馈',
							height:400,
							width:700,
							border:false,
							frame:false,
							modal:true,
							autoScroll:true,
							bodyStyle:'overflow-x:auto;background:#FFFFFF;font-size:13px;padding:0px;height:500px;',
							closeAction:'hide',
							html:htmlStr
						});
						fkMoniThemeWin.sumitMoniFk = function(){
							var fkRemark_Code = "fkRemark_"+_fkthemeId;
							var fkRemark_Obj =Ext.get(fkRemark_Code);
							var fkRemark = fkRemark_Obj.getValue();
							if(fkRemark==null || fkRemark==''){
								 Ext.Msg.alert('提示', '反馈信息不能为空！');
							}else if(fkRemark.length>1200){
								Ext.Msg.alert('提示', '反馈信息不能大于1200个汉字！');
							}else{
								var jsonStr = "{\"themeId\":\""+ansfktheme.themeId+"\",\"fkRemark\":\""+fkRemark+"\"}";
							   	Ext.Ajax.request({
									url : me.requestHttp+'exam/base/theme/saveFkForThemeFormAction!saveFk.action?op=fk',
									params : {
										json : jsonStr
									},
									method : 'POST',
									timeout: 15000,
									success : function(response) {
										 var result = Ext.decode(response.responseText);
										 Ext.Msg.alert('提示', result.msg, function(){
										 	fkMoniThemeWin.hide();
										 });
									},
									failure : function() { }
								 });
								}
						}
						fkMoniThemeWin.show();
			    		
			    },
				failure : function() {
				}
			});
 	},
 	//查看正确答案
 	rightAnsShow : function(themeIndex,obj){
 		var me = this;
 		if(me.rtAns == null){
 			me.rtAns = [];
 			for(var i=0;i<me.userThemes.length+1;i++){
 				me.rtAns[i] = null;
 			}
 		}
 		me.userTheme = me.userThemes[themeIndex];
 		me.currIndex = themeIndex;
 		var rightAnsDiv = document.getElementById("rightAnsDiv"+themeIndex);
 		if(rightAnsDiv.style.display == 'none'){
 			if(me.rtAns[me.currIndex] != null){
	 			//Ext.Msg.alert('正确答案', me.rtAns[me.currIndex-1]);
	 			rightAnsDiv.childNodes[0].childNodes[0].innerHTML="正确答案："+me.rtAns[me.currIndex];
	 			rightAnsDiv.style.display = "";
	 			obj.className = "divAN";
	 			obj.innerHTML = "收起答案";
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
			    			me.rtAns[me.currIndex] = ansStr;
			    		}else{
			    			ansStr = "暂无正确答案！";
			    		}
			    		rightAnsDiv.childNodes[0].childNodes[0].innerHTML="正确答案："+ansStr;
	 					rightAnsDiv.style.display = "";
	 					obj.className = "divAN";
	 					obj.innerHTML = "收起答案";
			    		//Ext.Msg.alert('正确答案', ansStr);
			    	},
					failure : function() {
						rightAnsDiv.childNodes[0].childNodes[0].innerHTML='答案获取失败，网络异常！';
	 					rightAnsDiv.style.display = "";
	 					obj.className = "divAN";
	 					obj.innerHTML = "收起答案";
						//Ext.Msg.alert('提示', '答案获取失败，网络异常！');
					}
				});
	 		}
 		}else{
	 		rightAnsDiv.style.display = "none";
	 		obj.className = "div2";
	 		obj.innerHTML = "查看答案";
 		}
 	},
 	//保存答案
 	saveAnswer : function(type){
 		var me = this;
 		if(type == 'autoSave'){//自动 保存
 			if(me.sendAnswerFlag && me.finTitleLoad){
 				me.sendAnswerFlag = false;
	 			me.sendAnswer(type);
	 			me.sendAnswerFlag = true;
 			}
 		}else if(type == 'save'){//手工保存
 			if(me.sendAnswerFlag && me.finTitleLoad){
 				me.sendAnswerFlag = false;
 				me.sendAnswer(type);
 				me.sendAnswerFlag = true;
 			}else if(me.finTitleLoad == false){
 				Ext.Msg.alert('提示',"数据还在加载，请等待！");
 			}else{
 				Ext.Msg.alert('提示',"数据正在提交，请等待！");
 			}
 		}else if(type == 'submit'){
 			if(me.sendAnswerFlag && me.finTitleLoad){
 				me.sendAnswerFlag = false;
	 			Ext.Msg.confirm('提示', '您确认已经完成本场考试并提交答案吗？',function(bt){
		             if(bt=='yes'){
		                me.sendAnswerIndexs = [];
			 			for(var i=0;i<me.userThemes.length;i++){
			 				me.sendAnswerIndexs[i]=i;
			 			}
			 			me.sendAnswer(type);
		            }else{}
	 			});
	 			me.sendAnswerFlag = true;
 			}else if(me.finTitleLoad == false){
 				Ext.Msg.alert('提示',"数据还在加载，请等待！");
 			}else{
 				Ext.Msg.alert('提示',"数据正在提交，请等待！");
 			}
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
											      //window.location =   me.requestHttp+'mainPageEx/listForMainExListAction!list.action'
											    	me.backFun();
											    }
											}
										});
										yjWin.show();
									}else{
										//window.location =   me.requestHttp+'mainPageEx/listForMainExListAction!list.action'
										me.backFun();
									}
							     }
							});
		    			
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
 	},
 	//添加发送标识
 	addSendAnswerIndexs : function(addIndex){
 		var me = this;
 		var isAdd = true;
 		for(var i=0;i<me.sendAnswerIndexs.length;i++){
 			if(me.sendAnswerIndexs[i] === addIndex){
 				isAdd = false;
 				break;
 			}
 		}
 		if(isAdd){
 			me.sendAnswerIndexs[me.sendAnswerIndexs.length] = addIndex;
 		}
 	},
 	//设置标记
 	setSignFlag : function(themeIndex,obj,toDbFlag){
 		var me = this;
 		me.userTheme = me.userThemes[themeIndex];
 		me.currIndex = themeIndex;
 		if(me.userTheme.signFlag){
 			me.userTheme.signFlag = false;
 		}else{
 			me.userTheme.signFlag = true;
 		}
 		me.opty = '';
	 	if(me.userTheme.signFlag == true){//标记
	 		obj.className = "divOK";
	 		obj.innerHTML = "取消标记";
	 		me.opty = 'add';
	 	}else{
	 		obj.className = "div1";
	 		obj.innerHTML = "标记本题";
	 		me.opty = 'del';
	 	}
	 	if(toDbFlag){
	 			Ext.Ajax.request({
					url : me.requestHttp+'/onlineExam/signInMoniThemeForOnlineListAction!signInMoniTheme.action',
					params : {
						id : me.examUser.testPaperId,
						currIndex:me.currIndex,
						opty : me.opty
					},
					timeout: 1000,
					success : function(response) {
						var result = Ext.decode(response.responseText).rsContent;
						result = (Ext.decode(result));
			    		if(result && result.code && result.message && result.code!='00001'){
			    			Ext.Msg.alert('提示',result.message);
			    		}
			    	},
					failure : function() {Ext.Msg.alert('提示','提交失败');}
				});
	 	}
 	},
 	//答案格式化
 	fmtAns : function(themeFmt){
 		var ansTmp = "";
 		var userAnswerArrTmp = themeFmt.userAnswerArr;
 		if(userAnswerArrTmp!=undefined && userAnswerArrTmp!=null && userAnswerArrTmp!="undefined" && userAnswerArrTmp!="null"){
	 		for(var j=0;j<userAnswerArrTmp.length;j++){
	 			if(themeFmt.themeType.type == '5' || themeFmt.themeType.type == '10' || themeFmt.themeType.type == '25'){
		 			if(j==userAnswerArrTmp.length-1){
		 				ansTmp+=userAnswerArrTmp[j] && userAnswerArrTmp[j]!=undefined &&userAnswerArrTmp[j]!=null && userAnswerArrTmp[j].length>0 ? userAnswerArrTmp[j][0] : "null";
		 			}else{
		 				ansTmp+=userAnswerArrTmp[j] && userAnswerArrTmp[j]!=undefined && userAnswerArrTmp[j]!=null && userAnswerArrTmp[j].length>0 ? userAnswerArrTmp[j][0]+"#*#" : "null#*#";
		 			}
	 			}else{
	 				if(j==userAnswerArrTmp.length-1){
		 				ansTmp+=userAnswerArrTmp[j];
		 			}else{
		 				ansTmp+=userAnswerArrTmp[j]+"#*#";
		 			}
	 			}
	 			
	 		}
 		}else{
 			ansTmp="null";
 		}
 		if(ansTmp == "")ansTmp = "null";
 		return ansTmp;
 	},
 	replaceString : function(str,reallyDo,replaceWith) { 
		var e=new RegExp(reallyDo,"g"); 
		words = str.replace(e, replaceWith); 
		return words; 
	}
 });
Array.prototype.delItem=function(index){
   if(isNaN(index)||index>=this.length){return false;}
   for(var i=index;i<this.length-1;i++){
      this[i]=this[i+1];
   }
   this.length-=1;
};
function openWin(windowId,url){
	var reg=new RegExp("-","g"); 
	var newstr=windowId.replace(reg,"");
	var oNewWindow  = window.open(url,newstr,'width='+(window.screen.availWidth-10)+',height='+(window.screen.availHeight-30)+ ',z-look=yes,top=0,left=0,toolbar=yes,menubar=yes,scrollbars=yes, resizable=yes,location=yes, status=yes');
	if(oNewWindow){
	oNewWindow.focus();
	}
	return oNewWindow;
}
