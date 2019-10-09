Ext.define('modules.hnjtamis.online.examineeLogin.ThemeMoni', {
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
 	systemTime : {},//系统时间
 	handleTime : null,
 	randomTimeSave : null,
 	sendAnswerIndexs : [],
 	syncSystemRandomTime : Math.floor(Math.random()*200000),//随机保存与同步时间（毫秒）
 	sendAnswerFlag : true,//发送标识
 	pageRowMax :  6,//一页显示最大行数
 	pageColumnMax : 17 ,//一页显示最大列数
 	isHidden : 0,//部分样式的隐藏
 	constructor : function(config){//构造函数
 		var me = this;
 		me.mixins.observable.constructor.call(this, config);
 		me.initComponent();
 	},
 	//数据的初始化
 	initComponent : function() {
 		var me = this;
 		me.initExamThemeFlag = true;
 		me.initCss();
 		Ext.state.Manager.setProvider(me.cp);
 		me.setExamUserInfo();//初始化人员信息
 		me.initSystemTime();
 		me.themeOnKey();
 	},
 	//获取考题
 	initExamTheme : function(){
 		var me = this;
 		Ext.Ajax.request({
 			timeout: 10000,
			url : me.requestHttp+'/onlineExam/gettitleForOnlineListAction!gettitle.action',
			params : {
				id : me.examUser.testPaperId
			},
			success: function(response) {
	    		var result = Ext.decode(response.responseText).rsContent;
	    		result = (Ext.decode(result));
				if(result.result == undefined || result.result =='' || result.result ==null){
	    			Ext.Msg.alert('提示',result.message);
		    	}else{
		    		Ext.Ajax.request({
		 			timeout: 10000,
					url : me.requestHttp+'/onlineExam/getansbakForOnlineListAction!getansbak.action',
					params : {
						id : me.examUser.testPaperId
					},
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
			},
			failure : function() {
				Ext.Msg.alert('提示','获取试题失败，请与管理员联系！');
				me.initExamThemeFlag = false;
			}
		});
 	},
 	//初始化页面样式
 	initCss : function(){
 		document.getElementById ("table2").height = document.getElementById ("table1").offsetHeight;
 	},
 	//设置初始化试题
 	setInitExamTheme : function(themesTitle,themesAns){
 			var me = this;
 			me.initExamThemeFlag = false;
		    me.themesTitle = themesTitle;
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
				 	}
				 }

				 if(me.themesTitle.themeType.length < 3*me.pageColumnMax){
				      me.pageRowMax = 3;
				 }else if(me.themesTitle.themeType.length < me.pageRowMax*me.pageColumnMax){
				 	if(me.themesTitle.themeType.length%me.pageColumnMax == 0){
				 		me.pageRowMax = me.themesTitle.themeType.length/me.pageColumnMax;
				 	}else{
				 		me.pageRowMax = me.themesTitle.themeType.length/me.pageColumnMax+1
				 	}
				 }
			 }
			 if(themesAns!=null && themesAns!='' && themesAns!=undefined){
		    	var noArr = themesAns.no.split("@$@");
		    	var ansArr = themesAns.ans.split("@$@");
		    	for(var i = 0;i<noArr.length;i++){
		    		if(noArr[i]!=null && noArr[i]!="" && ansArr[i]!=null && ansArr[i]!=""){
		    			var themeIndex_ = parseInt(noArr[i])-1;
		    			me.userThemes[themeIndex_].userAnswerArr = ansArr[i].split("#*#");
		    			me.userThemes[themeIndex_].userAnswer =  ansArr[i];
		    		}
		    	}
		     }
			 me.initThemeSortTabHtml();//初始化页签与试题
 	},
 	//初始化系统时间
 	initSystemTime : function(){
 		var me = this;
  		Ext.Ajax.request({
 			timeout: 10000,
			url : me.requestHttp+'/onlineExam/gettimeForOnlineListAction!gettime.action',
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
 			timeout: 10000,
			url : me.requestHttp+'/onlineExam/gettimeForOnlineListAction!gettime.action',
			success: function(response) {
	    		var result = Ext.decode(response.responseText).rsContent;
	    		result = (Ext.decode(result));
				me.systemTime =  result.result.date;
			}
		});
 	},
 	//比较时间
 	betweenTime : function (time1,time2){
 		var ts1 = time1.split(" ");
 		var ts2 = time2.split(" ");
 		
 		var date1 = ts1[0].split("-");
 		var date2 = ts2[0].split("-");
 		if(parseInt(date1[0])>parseInt(date2[0])
 			|| (parseInt(date1[0])==parseInt(date2[0]) && parseInt(date1[1])>parseInt(date2[1]))
 			|| (parseInt(date1[0])==parseInt(date2[0]) && parseInt(date1[1])==parseInt(date2[1]) && parseInt(date1[2])>parseInt(date2[2]))){
 			return [0,0,0,999];
 		}else if(parseInt(date1[0])==parseInt(date2[0]) && parseInt(date1[1])==parseInt(date2[1]) && parseInt(date1[2])&&parseInt(date2[2])){
	 		if(ts1.length>1){
	 			time1 = ts1[1].replace(/(^\s*)|(\s*$)/g,"");
	 		}
	 		if(ts2.length>1){
	 			time2 = ts2[1].replace(/(^\s*)|(\s*$)/g,"");
	 		}
	 		
			var t1 = time1.split(":"); 
	 		var t2 = time2.split(":"); 
	 		var tt1 = parseInt(t1[0])*3600+parseInt(t1[1])*60+parseInt(t1[2]);
	 		var tt2 = parseInt(t2[0])*3600+parseInt(t2[1])*60+parseInt(t2[2]);
	 		if(tt1 == tt2){
		 		return [0,0,0,0];
	 		}else if(tt1 > tt2){
		 		var hh=Math.floor((tt1 - tt2)/3600);
		 		var mm=Math.floor((tt1 - tt2 - hh*3600)/60);
				var ss=Math.floor(tt1 - tt2 - hh*3600 - mm*60);
				return [hh,mm,ss,1];
	 		}else{
		 		var hh=Math.floor((tt2 - tt1)/3600);
		 		var mm=Math.floor((tt2 - tt1 - hh*3600)/60);
				var ss=Math.floor(tt2 - tt1 - hh*3600 - mm*60);
				return [hh,mm,ss,-1];
	 		}
 		}else{
 			return [0,0,0,-999];
 		}	
	},
	//添加秒数
	addSeconds : function(dateTimeVar){  
		//dateTimeVar 格式  ："2012-11-28 10:25:46"
		var tmp = dateTimeVar.split(" ");
		if(tmp.length==1){
			var nowDateTime = new Date();
			tmp = new Array();
			tmp[0] = nowDateTime.getFullYear()+"-"+(nowDateTime.getMonth()+1)+"-"+nowDateTime.getDate();
			tmp[1] = dateTimeVar;
			dateTimeVar = tmp[0]+" "+tmp[1];
		}
		var currentDate = new Date(Date.parse(dateTimeVar.replace(/-/g,"/")));
		currentDate = new Date(currentDate.setSeconds(currentDate.getSeconds()+1)); 
		return tmp[0]+" "+currentDate.getHours()+":"+currentDate.getMinutes()+":"+currentDate.getSeconds();
	},
	//时间格式化
	formatToTimeHtml : function(p1,p2){
		if(p1 == null || p2 == null){
			document.getElementById("minuteLabel").innerHTML = "&nbsp;";
			document.getElementById("minute").innerHTML = "&nbsp;";
		}else{
			document.getElementById("minuteLabel").innerHTML = p1;
			document.getElementById("minute").innerHTML = (p2[0]<10 ? "0"+p2[0] : p2[0])+":"+(p2[1]<10 ? "0"+p2[1] : p2[1])+":"+(p2[2]<10 ? "0"+p2[2] : p2[2]);
		}
	},
	//进行倒计时，并按时提交
 	handleTimeFun : function(){
 		var me = this;
 		if(me.systemTime!=undefined && me.examUser!=undefined && me.examUser.examStartTime!=undefined){
	 		me.systemTime = me.addSeconds(me.systemTime);
	 		var btTime = this.betweenTime(me.systemTime,this.examUser.examStartTime);
	 		if(btTime[3] == 999 || btTime[3] == -999){
	 			document.getElementById("minuteLabel").innerHTML = "&nbsp;";
				document.getElementById("minute").innerHTML = "&nbsp;";
	 		}else if(btTime[3] == 0){
	 			btTime = this.betweenTime(me.systemTime,this.examUser.examEndTime);
	 		    this.formatToTimeHtml("剩余时间",btTime);
	 		    if(me.initExamThemeFlag){
	  				me.initExamTheme();
	  			}
	 		    document.getElementById("savebutton").disabled = false;
	 		    document.getElementById("submitbutton").disabled = false;
	 		    document.getElementById("signFlagBt").disabled = false;
	 		    if(me.currIndex <= 0){
	  				document.getElementById("prevThemeBt").disabled = true;
	  			}else{
	  			   document.getElementById("prevThemeBt").disabled = false;
	  			}
	  			if(me.currIndex >= this.userThemes.length-1){
	  			 	document.getElementById("nextThemeBt").disabled = false;
	  			}else{
	  				document.getElementById("nextThemeBt").disabled = false;
	  			}
	 		}else if(btTime[3] < 0){
	 			this.formatToTimeHtml("等待时间",btTime);
	 			document.getElementById("savebutton").disabled = true;
	 			document.getElementById("submitbutton").disabled = true;
	 			document.getElementById("signFlagBt").disabled = true;
	 			document.getElementById("prevThemeBt").disabled = true;
	 		    document.getElementById("nextThemeBt").disabled = true;
	 		} else {
	 			btTime = this.betweenTime(me.systemTime,this.examUser.examEndTime);
	 			if(btTime[3] == 0){
	 				//需要进行提交
	 				this.formatToTimeHtml("剩余时间",btTime);
	 				if(me.initExamThemeFlag){
		  				me.initExamTheme();
		  			}
	 				this.sendAnswer('submit');
	 				document.getElementById("savebutton").disabled = true;
	 				document.getElementById("submitbutton").disabled = true;
	 				document.getElementById("signFlagBt").disabled = true;
	 				document.getElementById("prevThemeBt").disabled = true;
	 		    	document.getElementById("nextThemeBt").disabled = true;
	 			}else if(btTime[3] < 0){
	 				this.formatToTimeHtml("剩余时间",btTime);
	 				if(me.initExamThemeFlag){
		  				me.initExamTheme();
		  			}
	 				document.getElementById("savebutton").disabled = false;
	 				document.getElementById("submitbutton").disabled = false;
	 				document.getElementById("signFlagBt").disabled = false;
	 				if(me.currIndex <= 0){
	  					document.getElementById("prevThemeBt").disabled = true;
		  			}else{
		  			   document.getElementById("prevThemeBt").disabled = false;
		  			}
		  			if(me.currIndex >= this.userThemes.length-1){
		  			 	document.getElementById("nextThemeBt").disabled = false;
		  			}else{
		  				document.getElementById("nextThemeBt").disabled = false;
		  			}
	 			}else {
	 				this.formatToTimeHtml(null,null);
	 				clearInterval(me.handleTime);
	 				document.getElementById("savebutton").disabled = true;
	 				document.getElementById("submitbutton").disabled = true;
	 				document.getElementById("signFlagBt").disabled = true;
	 				document.getElementById("prevThemeBt").disabled = true;
	 		    	document.getElementById("nextThemeBt").disabled = true;
	 			}
	 		}
 		}
 	},
 	//刷新用户信息
 	setExamUserInfo : function(){
 		document.getElementById("userName").innerHTML = this.fmtStr(this.examUser.userName);
 		document.getElementById("idNumber").innerHTML = this.fmtStr(this.examUser.idNumber);
 		document.getElementById("inticket").innerHTML = this.fmtStr(this.examUser.inticket);
 		document.getElementById("organName").innerHTML = this.fmtStr(this.examUser.organName);
 		document.getElementById("examStartTime").innerHTML = this.fmtStr(this.examUser.examStartTime);
 		document.getElementById("examEndTime").innerHTML = this.fmtStr(this.examUser.examEndTime);
 		document.getElementById("examName").innerHTML = "-- "+this.examUser.examName;
 	},
 	//获取试题
 	getTheme : function (index){
 		return index < this.userThemes.length ? this.userThemes[index] : null;
 	},
 	//初始化试题页签
 	initThemeSortTabHtml : function(){
 		var me = this;
 		me.pageTabMax = parseInt(me.userThemes.length/(me.pageRowMax*me.pageColumnMax))+1;
 		me.themeSortTabStr = '';
 		for(var i=0;i<me.pageTabMax;i++){
 			var _m=((i+1)*(me.pageRowMax*me.pageColumnMax));
 			var v = (i*(me.pageRowMax*me.pageColumnMax)+1)+"-"+(_m>me.userThemes.length?me.userThemes.length:_m);
 			var tabClassName = "R_list_tab02";
 			if(i==0){
 				tabClassName = "R_list_tab01";
 			}
 			if(i==me.pageTabMax-1){
 				me.themeSortTabStr += '<input type="button" name="button2" id="themeTab'+i+'" class="'+tabClassName+'" value="'+v+'" onclick ="theme.setThemeSortHtml('+i+')" /></br>';
 			}else{
 				me.themeSortTabStr += '<input type="button" name="button2" id="themeTab'+i+'" class="'+tabClassName+'" value="'+v+'" onclick ="theme.setThemeSortHtml('+i+')" />';
 			}
 		}
 		document.getElementById("themeSortTab").innerHTML=me.themeSortTabStr;
 		me.setThemeSortHtml(0);
 	},
 	//显示底下题号选择表格
 	setThemeSortHtml : function(pageIndex){
 		var me = this;
 		me.tdwidth = 39;
 		if(me.isHidden == 1){
 			var themeSortTableWidth = 860;
 			document.getElementById("themeSortTable").width = themeSortTableWidth;
 			me.tdwidth = themeSortTableWidth/me.pageColumnMax;
 			document.getElementById("table1").rowSpan = 1;
 		}
 		if(me.pageIndex == undefined || me.pageIndex !=pageIndex){
	 		me.pageIndex = pageIndex;
	 		me.pageIndexStart = me.pageIndex * me.pageRowMax * me.pageColumnMax;
	 		me.pageSortHtmlStr = '<table  border="1" bordercolor="#858585" cellspacing="0" cellpadding="0" bgcolor="#FFFFFF" style="border-collapse:collapse;">';
	 		for(var i=0;i<me.pageRowMax;i++){
	 			me.pageSortHtmlStr += '<tr align="center">';
	 			for(var k=0;k<me.pageColumnMax;k++){
	 				var sortHtml = (me.pageIndexStart+i*me.pageColumnMax+k+1);
	 				if(sortHtml>me.userThemes.length){
	 					me.pageSortHtmlStr += '<td width="'+me.tdwidth+'">&nbsp;</td>';
	 				}else if(me.currIndex == (sortHtml-1)){//当前题目
	 					me.pageSortHtmlStr += '<td width="'+me.tdwidth+'" id="themeBt_'+(sortHtml-1)+'" class="R_list_dangq" onclick="theme.setThemeHtml('+(sortHtml-1)+')">'+sortHtml+'</td>';
	 				}else if(me.userThemes[sortHtml-1].signFlag == true){//标记
	 					me.pageSortHtmlStr += '<td width="'+me.tdwidth+'" id="themeBt_'+(sortHtml-1)+'" class="R_list_biaoj" onclick="theme.setThemeHtml('+(sortHtml-1)+')">'+sortHtml+'</td>';
	 				}else if(me.userThemes[sortHtml-1].userAnswer && me.userThemes[sortHtml-1].userAnswer!=''){//已答
	 					me.pageSortHtmlStr += '<td width="'+me.tdwidth+'" id="themeBt_'+(sortHtml-1)+'" class="R_list_yida" onclick="theme.setThemeHtml('+(sortHtml-1)+')">'+sortHtml+'</td>';
	 				}else{
	 					me.pageSortHtmlStr += '<td width="'+me.tdwidth+'" id="themeBt_'+(sortHtml-1)+'" onclick="theme.setThemeHtml('+(sortHtml-1)+')">'+sortHtml+'</td>';
	 				}
	 				
	 			}
	 			me.pageSortHtmlStr += '</tr>';
	 		}
	 		me.pageSortHtmlStr +="</table>";
	 		document.getElementById("themeSortTable").innerHTML = me.pageSortHtmlStr;
	 		me.setThemeHtml(parseInt(me.pageIndexStart));
 		}
 	},
 	//刷新显示底下题号选择表格
 	flushTabAndHtml : function(){
 		var me = this;
 		var nowIndex = parseInt(me.currIndex / (me.pageRowMax * me.pageColumnMax));
 		if(me.pageIndex !=nowIndex){
 			document.getElementById("themeTab"+me.pageIndex).className = "R_list_tab02";
 			document.getElementById("themeTab"+nowIndex).className = "R_list_tab01";
 			me.setThemeSortHtml(nowIndex);
 		}
 	},
 	//显示试题
 	setThemeHtml : function(index){
 		var me = this;
 		if(parseInt(index) >= 0 && parseInt(index) < me.userThemes.length){
 			if(me.userTheme){//处理上一次的选择试题
		 		if(me.userTheme.userAnswer && me.userTheme.userAnswer!=''){//有答案
		 			document.getElementById("themeBt_"+me.currIndex).className = "R_list_yida";
		 		}else if(me.userTheme.signFlag == true){//标记
		 			document.getElementById("themeBt_"+me.currIndex).className = "R_list_biaoj";
		 			
		 		}else{
		 			document.getElementById("themeBt_"+me.currIndex).className = "";
		 		}
 			}
	 		me.userTheme = me.getTheme(index);
	 		document.getElementById("themeBt_"+index).className = "R_list_dangq";
	 		if(me.userTheme.signFlag == true){
	 			document.getElementById("signFlagBt").value = "取消标记";
	 		}else{
	 			document.getElementById("signFlagBt").value = "标记";
	 		}
	 		
	 		//题型
	 		document.getElementById("themeTypeLabel").innerHTML = 
	 			me.fmtStr(me.userTheme.themeTypeShowSort)+"、"+me.fmtStr(me.userTheme.themeType.themeTypeName)
	 			+" <span style=\"font-weight: normal; font-size: 12px;\">（共"+(me.fmtStr(me.userTheme.themeTypeLen))+"题）</span>";
	 		//试题与答案
	 		var explain= me.userTheme.explain;
	 		 //类型 5：单选；10：多选 15：填空；20：问答；25：判断；30：视听 35：其他
	 		if(me.userTheme.themeType.type == '5' || me.userTheme.themeType.type == '10' || me.userTheme.themeType.type == '25'){
	 			me.themeTableStr = '<table width="97%" border="0" cellpadding="0" cellspacing="0" class="R_answer" align="center" >';
		 		me.themeTableStr += "<tr>";
		 		me.themeTableStr += "<td class=\"R_timu\">";
		 		me.themeTableStr +=	(index+1)+"、"+me.fmtStr(me.userTheme.value);
		 		if(me.userTheme.fraction && me.userTheme.fraction!=null && me.userTheme.fraction!='null'){
		 			me.themeTableStr +="（"+(me.userTheme.fraction)+"分）";
		 		}
		 		me.themeTableStr += "</td>";
			    me.themeTableStr+="</tr>";
		 		if(explain!=undefined && explain!="" && explain!=null){
					me.themeTableStr += "<tr>";
					me.themeTableStr += "<td class=\"R_timu\" style=\"font-size:12px;\">";
					me.themeTableStr +="（注解："+explain+"）";	
					me.themeTableStr += "</td>";
				    me.themeTableStr+="</tr>";
				} 
		 		me.themeTableStr += "<tr>";
			 	me.themeTableStr += "<td height=\"32\">";
			    for(var i = 0;i<me.userTheme.answer.length;i++){
			 		me.themeTableStr += me.fmtStr(me.answerShowSort[i])+"、";
			 		me.themeTableStr +=	me.fmtStr((me.userTheme.answer)[i].ansValue);
			 		if( me.userTheme.eachline ==undefined || parseInt(me.userTheme.eachline)==1
			 			|| (parseInt(me.userTheme.eachline) > 0 && i>0 && parseInt(i%(parseInt(me.userTheme.eachline)-1))==0)){
			 			 me.themeTableStr += "</td>";
						 me.themeTableStr+="</tr>";
						 me.themeTableStr += "<tr>";
			 			 me.themeTableStr += "<td height=\"32\">";
			 		}else{
			 			me.themeTableStr +="&nbsp;&nbsp;&nbsp;&nbsp;";
			 		}
			    }
			    me.themeTableStr += "</td>";
				me.themeTableStr+="</tr>";
			    me.themeTableStr += "</table>";
			    document.getElementById("themeTable").innerHTML = me.themeTableStr;
			    //选择题显示按钮与考生答案
			 	//类型 5：单选；10：多选 15：填空；20：问答；25：判断；30：视听 35：其他	
		 		me.userAnsSelectedBtStr ="";
		 		me.userAnsStr = "";
		 		
			    for(var i = 0;i<me.userTheme.answer.length;i++){
			    	var themeClass = 'R_btn03';
			    	for(var j=0;j<me.userTheme.userAnswerArr.length;j++){
			    		if(me.answerShowSort[i] == me.userTheme.userAnswerArr[j]){
			    			themeClass = 'R_btn03_up';
			    			break;
			    		}
			    	}
		 			me.userAnsSelectedBtStr +=	"<input type='button' name='ans' class='"+themeClass+"' onclick='theme.setAnswer(this,"+i+")' value='"+me.answerShowSort[i]+"' />";
		 		}
		 		me.userTheme.userAnswer = "";
			 	for(var j=0;j<me.userTheme.userAnswerArr.length;j++){
				   me.userTheme.userAnswer+=me.userTheme.userAnswerArr[j]+",";
				}
				if(me.userTheme.userAnswer!="")me.userTheme.userAnswer=me.userTheme.userAnswer.substring(0,me.userTheme.userAnswer.length-1);
			    
		 		me.userAnsStr = "您的答案：<span  class=\"R_answer_yx\" >"+me.fmtStr(me.userTheme.userAnswer)+"</span>";
		 		document.getElementById("userAnsSelectedBt").innerHTML = me.userAnsSelectedBtStr;
	 			document.getElementById("userAns").innerHTML = me.userAnsStr;
	 		}else if(me.userTheme.themeType.type == '15'){
	 			me.themeTableStr = '<table width="97%" border="0" cellpadding="0" cellspacing="0" class="R_answer" align="center" >';
		 		me.themeTableStr += "<tr>";
		 		me.themeTableStr += "<td class=\"R_timu\">";
		 		var themeStr = me.replaceString(me.userTheme.value,"（）","()");
	 			var tmpArr = (","+themeStr+",").split("()");
	 			var tmpArrStr = "";
	 			var inputIndex = 0;
	 			for(var i=0;i<tmpArr.length;i++){
	 				var ansValue_ = "";
	 				if(me.userTheme.userAnswerArr.length>0){
	 					ansValue_ = me.userTheme.userAnswerArr[inputIndex];
	 				}
	 				if(i<tmpArr.length-1){
	 					tmpArrStr+=tmpArr[i]+"<input type='text' name='ansText' onblue='theme.setAnswer(this,"+i+")' value='"+ansValue_+"' />";
	 					inputIndex++;
	 				}else{
	 					tmpArrStr+=tmpArr[i]
	 				}
	 			}
	 			tmpArrStr = tmpArrStr.substring(1,tmpArrStr.length-1);
	 			me.themeTableStr +=	(index+1)+"、"+tmpArrStr;
	 			if(me.userTheme.fraction && me.userTheme.fraction!=null && me.userTheme.fraction!='null'){
	 				me.themeTableStr +="（"+(me.userTheme.fraction)+"分）";
	 			}
	 			me.themeTableStr += "</td>";
				me.themeTableStr+="</tr>";
	 			if(explain!=undefined && explain!="" && explain!=null){
					me.themeTableStr += "<tr>";
		 			me.themeTableStr += "<td class=\"R_timu\" style=\"font-size:12px;\">";
					me.themeTableStr +="（注解："+explain+"）";	
					me.themeTableStr += "</td>";
					me.themeTableStr+="</tr>";
				}
			    me.themeTableStr += "</table>";
	 			
	 			document.getElementById("themeTable").innerHTML = me.themeTableStr;
	 			document.getElementById("userAnsSelectedBt").innerHTML = "";
	 			document.getElementById("userAns").innerHTML = "";
	 		}else if(me.userTheme.themeType.type != undefined){
	 			me.themeTableStr = '<table width="97%" border="0" cellpadding="0" cellspacing="0" class="R_answer" align="center" >';
		 		me.themeTableStr += "<tr>";
		 		me.themeTableStr += "<td class=\"R_timu\">";
	 			me.themeTableStr +=	(index+1)+"、"+me.fmtStr(me.userTheme.value);
	 			if(me.userTheme.fraction && me.userTheme.fraction!=null && me.userTheme.fraction!='null'){
		 			me.themeTableStr +="（"+(me.userTheme.fraction)+"分）";
		 			
		 		}
		 		me.themeTableStr += "</td>";
				me.themeTableStr+="</tr>";
		 		if(explain!=undefined && explain!="" && explain!=null){
		 			me.themeTableStr += "<tr>";
		 			me.themeTableStr += "<td class=\"R_timu\" style=\"font-size:12px;\">";
					me.themeTableStr +="（注解："+explain+"）";
					me.themeTableStr += "</td>";
					me.themeTableStr+="</tr>";
				}
				me.themeTableStr += "<tr>";
		 		me.themeTableStr += "<td class=\"R_timu\">";
				me.themeTableStr += "<textarea  rows='15' cols='120' style='width:300;height:200' name='ansText' onblue='theme.setAnswer(this,"+i+")'></textarea>";
				me.themeTableStr += "</td>";
				me.themeTableStr+="</tr>";
			    me.themeTableStr += "</table>";
	 			document.getElementById("themeTable").innerHTML = me.themeTableStr;
	 			document.getElementById("userAnsSelectedBt").innerHTML = "";
	 			document.getElementById("userAns").innerHTML = "";
	 		}
	 		me.currIndex = index;
	 		me.setjd();
 		}
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
 	//设置考生答案
 	setAnswer : function(answerObj,inputIndex){
 		var me = this;
 		if(me.userTheme.themeType.type == '5' || me.userTheme.themeType.type == '10' || me.userTheme.themeType.type == '25'){
 			var answer = answerObj.value;
 			if(answerObj.className == 'R_btn03'){
 				answerObj.className = 'R_btn03_up';
 			}else{
 				answerObj.className = 'R_btn03';
 			}
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
 			//清理空数据
 			for(var i = 0;i<me.userTheme.userAnswerArr.length;i++){
				if(me.userTheme.userAnswerArr[i]==null){
					me.userTheme.userAnswerArr.splice(i,1);
				}else{
					i++;
				}
			}
		
 			//重新排序
 			for(var i = 0;i<me.userTheme.userAnswerArr.length-1;i++){
	 			for(var j = 1;j<me.userTheme.userAnswerArr.length;j++){
	 				if(me.userTheme.userAnswerArr[i] == null
	 				   ||  (me.userTheme.userAnswerArr[i]!=null 
	 				    && (me.userTheme.userAnswerArr[j] !=null &&
	 					me.userTheme.userAnswerArr[i][1] > me.userTheme.userAnswerArr[j][1]))){
	 					var tmpArrValue = me.userTheme.userAnswerArr[i];
	 					me.userTheme.userAnswerArr[i] = me.userTheme.userAnswerArr[j];
	 					me.userTheme.userAnswerArr[j] = tmpArrValue;
	 				}
	 			}
 			}
 			
 			//清理空值重新加入
 			var newArr = new Array();
 			me.userTheme.userAnswer = "";
 			for(var i = 0;i<me.userTheme.userAnswerArr.length;i++){
 				if(me.userTheme.userAnswerArr[i]!=null){
 					newArr[newArr.length] = me.userTheme.userAnswerArr[i];
 					me.userTheme.userAnswer+=me.userTheme.userAnswerArr[i][0]+",";
 				}
 			}
 			me.userTheme.userAnswerArr = newArr;
 			if(me.userTheme.userAnswer!="")me.userTheme.userAnswer=me.userTheme.userAnswer.substring(0,me.userTheme.userAnswer.length-1);
 			document.getElementById("userAns").innerHTML = "您的答案：<span  class=\"R_answer_yx\" >"+me.userTheme.userAnswer+"</span>";
 		}else{
 			this.getTheme(me.currIndex).userAnswerArr[inputIndex]= answer;
 			me.userTheme.userAnswer = "";
 			if(this.getTheme(me.currIndex).userAnswerArr.length>0){
	 			/*for(var i=0;i<this.getTheme(me.currIndex).userAnswerArr.length;i++){
	 				me.userTheme.userAnswer = me.fmtAns(this.getTheme(me.currIndex).userAnswerArr[i])+"#$#";
	 			}*/
	 			me.userTheme.userAnswer = me.fmtAns(this.getTheme(me.currIndex));//me.userTheme.userAnswer.substring(0,me.userTheme.userAnswer.length-3);
 			}
 		}
 		this.setjd();
 		
 		//添加需要发送处理的答案
 		me.addSendAnswerIndexs(me.currIndex);
 		
 	},
 	//查看正确答案
 	rightAnsShow : function(){
 		var me = this;
 		Ext.Ajax.request({
 			timeout: 10000,
			url : me.requestHttp+'/onlineExam/getansForOnlineListAction!getans.action',
			params : {
				id : me.examUser.testPaperId,
				currIndex:me.currIndex
			},
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
	    		}else{
	    			ansStr = "暂无正确答案！";
	    		}
	    		Ext.Msg.alert('正确答案', ansStr);
	    	},
			failure : function() {
				Ext.Msg.alert('提示', '答案获取失败，网络异常！');
			}
		});
 	},
 	//保存答案
 	saveAnswer : function(type){
 		var me = this;
 		if(type == 'autoSave'){//自动 保存
 			if(me.sendAnswerFlag){
 				me.sendAnswerFlag = false;
	 			me.sendAnswer(type);
	 			me.sendAnswerFlag = true;
 			}
 		}else if(type == 'save'){//手工保存
 			if(me.sendAnswerFlag){
 				me.sendAnswerFlag = false;
 				me.sendAnswer(type);
 				me.sendAnswerFlag = true;
 			}else{
 				Ext.Msg.alert('提示',"数据正在提交，请等待！");
 			}
 		}else if(type == 'submit'){
 			if(me.sendAnswerFlag){
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
 		Ext.Ajax.request({
 			timeout: 10000,
			url : me.requestHttp+'/onlineExam/saveansForOnlineListAction!saveans.action',
			params : {
				id : me.examUser.userTestPaperId,
				no : sorts,
				ans : ans,
				submitType : type
			},
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
					if(result.code=='00001'){
		    			Ext.Msg.alert('提示',"交卷成功,本次考试得分 "+result.score+" 分！",function(){
		    				var url = window.location.href.replace("clean=1","clean=0");
		    				url = url.replace("addExam=1","addExam=0");
							window.location = url;
						});
			    	}else{
			    		Ext.Msg.alert('提示',result.message);
			    	}
		    	}
			},
			failure : function() {
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
 	setSignFlag : function(){
 		var me = this;
 		if(me.userTheme.signFlag){
 			me.userTheme.signFlag = false;
 		}else{
 			me.userTheme.signFlag = true;
 		}
	 	if(me.userTheme.signFlag == true){//标记
	 		document.getElementById("themeBt_"+me.currIndex).className = "R_list_biaoj";
	 		document.getElementById("signFlagBt").value = "取消标记";
	 	}else{
	 		document.getElementById("themeBt_"+me.currIndex).className = "R_list_dangq";
	 		document.getElementById("signFlagBt").value = "标记";
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
 	//设置进度
 	setjd : function (){
 		this.finsize = 0;
  		for(var i=0;i<this.userThemes.length;i++){
  			if(this.userThemes[i].userAnswer!=undefined && this.userThemes[i].userAnswer!=''){
  				this.finsize = this.finsize + 1;
  			}
  		}
  		var maxsize = this.userThemes.length;
  		var bfb = Math.round( this.finsize / maxsize * 100 );
  		document.getElementById("themeJdContent").innerHTML = "进度:"+ bfb +"%&nbsp;"+"("+this.finsize+"/"+maxsize +")";
  		document.getElementById("jdBfb").style.width = bfb+"%";
  		this.setBtDisabled();
  		this.flushTabAndHtml();
  	},
  	//上一题
  	prevTheme : function(){
  		var me = this;
  		return me.setThemeHtml(me.currIndex - 1);
  	},
  	//下一题
  	nextTheme : function(){
  		var me = this;
  		return me.setThemeHtml(me.currIndex + 1);
  	},
  	//设置按钮只读
  	setBtDisabled : function(){
  		var me = this;
  		if(me.currIndex <= 0){
  			document.getElementById("prevThemeBt").disabled = true;
  		}else{
  			document.getElementById("prevThemeBt").disabled = false;
  		}
  		if(me.currIndex >= this.userThemes.length-1){
  			document.getElementById("nextThemeBt").disabled = true;
  		}else{
  			document.getElementById("nextThemeBt").disabled = false;
  		}
  	},
  	themeOnKey : function(){
  		var me = this;
  				var tihtml = document.getElementById("minuteLabel").innerHTML;
				if(tihtml == '等待时间' || tihtml == '&nbsp;'){
					return;
				}
				var keCodeTemp = event.keyCode;    
				if(me.userTheme.Type==5){
					switch(keCodeTemp){
   					case 37:
   						me.prevTheme();
   						//alert(onlineObject.getTheme().Type);
   						break;
   					case 39:
   						me.nextTheme();
   						//alert(onlineObject.getTheme().Type); 
   						break;
					case 38:
   						//前20题
   						for(var i=0;i<20;i++){
   							onlineObject.previousNode();
   						}
   						setContent();
  						setjd();
   						break;
   					case 40:
   						//后20题
   						for(var i=0;i<20;i++){
   							onlineObject.nextNode();
   						}
   						setContent();
  						setjd();
   						break;
   					}
				 	return;
				}
   				var alerttmp; 
   				switch(keCodeTemp){
   					case 37:
   						me.prevTheme();
   						//alert(onlineObject.getTheme().Type);
   						break;
   					case 39:
   						me.nextTheme();
   						//alert(onlineObject.getTheme().Type); 
   						break;
   					case 49:
   					case 97:
   						//alerttmp = "1键";
   						me.setAnswer(this,0);
   						break;
   					case 50:
   					case 98:
   						//alerttmp = "2键";
   						me.setAnswer(this,1);
   						break;
   					case 51:
   					case 99:
   						//alerttmp = "3键";
   						me.setAnswer(this,2);
   						break;
   					case 52:
   					case 100:
   						//alerttmp = "4键";
   						me.setAnswer(this,3);
   						break;
   					case 65:
   						//alerttmp = "a键";
   						me.setAnswer(this,0);
   						break;
   					case 66:
   						//alerttmp = "b键";
   						me.setAnswer(this,1);
   						break;
   					case 67:
   						//alerttmp = "c键";
   						me.setAnswer(this,2);
   						break;
   					case 68:
   						//alerttmp = "d键";
   						me.setAnswer(this,3);
   						break;
   					case 69:
   					case 53:
   					case 101:
   						//alerttmp = "e键";
   						me.setAnswer(this,4);
   						break;
   					case 38:
   						//前20题
   						for(var i=0;i<20;i++){
   							onlineObject.previousNode();
   						}
   						setContent();
  						setjd();
   						break;
   					case 40:
   						//后20题
   						for(var i=0;i<20;i++){
   							onlineObject.nextNode();
   						}
   						setContent();
  						setjd();
   						break;
   				}
  	},
  	//进度隐藏
  	jdTrHide : function(){
  		if(document.getElementById("jdTr").style.display == 'none'){
  			document.getElementById("jdTr").style.display = '';
  			document.getElementById("jdHideBt").value="隐藏进度";
  		}else{
  			document.getElementById("jdTr").style.display = 'none';
  			document.getElementById("jdHideBt").value="显示进度";
  		}
  	}
 });
