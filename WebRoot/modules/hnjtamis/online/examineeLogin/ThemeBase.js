Ext.define('modules.hnjtamis.online.examineeLogin.ThemeBase', {
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
 		me.initCss();
 		Ext.state.Manager.setProvider(me.cp);
 		me.setExamUserInfo();//初始化人员信息
 		me.initSystemTime();
 	},
 	//获取考题
 	initExamTheme : function(){
 		//子方法实现
 	},
 	//初始化页面样式
 	initCss : function(){
 		document.getElementById ("table2").height = document.getElementById ("table1").offsetHeight;
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

					 		me.userThemes[userThemesIndex].value = me.decode(me.userThemes[userThemesIndex].value);
					 		if(me.userThemes[userThemesIndex].answer){
						 		for(var kk = 0;kk<me.userThemes[userThemesIndex].answer.length;kk++){
						 			(me.userThemes[userThemesIndex].answer)[kk].ansValue = me.decode((me.userThemes[userThemesIndex].answer)[kk].ansValue);
						 		}
					 		}
					 	}
					 }
					 if((me.userThemes.length) < (3*me.pageColumnMax)){
					      me.pageRowMax = 3;
					 }else if(me.userThemes.length < me.pageRowMax*me.pageColumnMax){
					 	if(me.userThemes.length%me.pageColumnMax == 0){
					 		me.pageRowMax = (me.userThemes.length)/me.pageColumnMax;
					 	}else{
					 		me.pageRowMax = (me.userThemes.length)/me.pageColumnMax;
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
				 me.initThemeSortTabHtml();//初始化页签与试题
 			}
 	},
 	//初始化系统时间
 	initSystemTime : function(){
 		//子方法实现
 	},
 	//同步系统时间
 	syncSystemTime : function(){
 		//子方法实现
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
 		}else if(parseInt(date1[0])==parseInt(date2[0]) && parseInt(date1[1])==parseInt(date2[1]) && parseInt(date1[2])==parseInt(date2[2])){
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
 			if(ts1.length>1){
	 			time1 = ts1[1].replace(/(^\s*)|(\s*$)/g,"");
	 		}
	 		if(ts2.length>1){
	 			time2 = ts2[1].replace(/(^\s*)|(\s*$)/g,"");
	 		}
 			var t1 = time1.split(":"); 
	 		var t2 = time2.split(":"); 
 			var objDate1=new Date(date1[0]+"/"+date1[1]+'/'+date1[2]+' '+t1[0]+":"+t1[1]+":"+t1[2]);
		    var objDate2=new Date(date2[0]+"/"+date2[1]+'/'+date2[2]+' '+t2[0]+":"+t2[1]+":"+t2[2]);    
	 		var tt1 = objDate1.getTime()/1000;
	 		var tt2 = objDate2.getTime()/1000;
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
		 		if(parseInt(hh)>24){
		 			return ['>24',mm,ss,-1];
		 		}else{
		 			return [hh,mm,ss,-1];
		 		}
	 		}
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
	  			if(me.currIndex >= (this.userThemes.length-1)){
	  			 	document.getElementById("nextThemeBt").disabled = true;
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
		  			if(me.currIndex >= (this.userThemes.length-1)){
		  			 	document.getElementById("nextThemeBt").disabled = true;
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
 			if(i<(me.pageTabMax-1)){
 				me.themeSortTabStr += '<input type="button" name="button2" id="themeTab'+i+'" class="'+tabClassName+'" value="'+v+'" onclick ="theme.setThemeSortHtml('+i+')" /></br>';
 			}else{
 				me.themeSortTabStr += '<input type="button" name="button2" id="themeTab'+i+'" class="'+tabClassName+'" value="'+v+'" onclick ="theme.setThemeSortHtml('+i+')" />';
 			}
 		}
 		document.getElementById("themeSortTab").innerHTML=me.themeSortTabStr;
 		me.setThemeSortHtml(0);
 	},
 	//显示底下题号选择表格
 	oldSortTab : null,
 	setThemeSortHtml : function(pageIndex){
 		var me = this;
 		me.tdwidth = 39;
 		if(me.oldSortTab!=null){
 			me.oldSortTab.className = "R_list_tab02";
 		}
 		document.getElementById("themeTab"+pageIndex).className = "R_list_tab01";
 		me.oldSortTab = document.getElementById("themeTab"+pageIndex);
 		var themeSortTableWidth = document.getElementById ("jdBfbTd").offsetWidth-document.getElementById ("themeSortTab").offsetWidth-8;
 		document.getElementById("themeSortTable").width = themeSortTableWidth;
 		me.tdwidth = themeSortTableWidth/me.pageColumnMax;

 		if(me.pageIndex == undefined || me.pageIndex !=pageIndex){
	 		me.pageIndex = pageIndex;
	 		me.pageIndexStart = me.pageIndex * me.pageRowMax * me.pageColumnMax;
	 		me.currIndex = parseInt(me.pageIndexStart);
	 		me.pageSortHtmlStr = '<table  border="1" bordercolor="#858585" cellspacing="0" cellpadding="0" bgcolor="#FFFFFF" style="border-collapse:collapse;width:'+themeSortTableWidth+'px;">';
	 		for(var i=0;i<me.pageRowMax;i++){
	 			me.pageSortHtmlStr += '<tr align="center">';
	 			for(var k=0;k<me.pageColumnMax;k++){
	 				var sortHtml = (me.pageIndexStart+i*me.pageColumnMax+k+1);
	 				if(sortHtml>me.userThemes.length){
	 					me.pageSortHtmlStr += '<td width="'+me.tdwidth+'" height="25">&nbsp;</td>';
	 				}else if(me.currIndex == (sortHtml-1)){//当前题目
	 					me.pageSortHtmlStr += '<td width="'+me.tdwidth+'" height="25" id="themeBt_'+(sortHtml-1)+'" class="R_list_dangq" onclick="theme.setThemeHtml('+(sortHtml-1)+')">'+sortHtml+'</td>';
	 				}else if(me.userThemes[sortHtml-1].signFlag == true){//标记
	 					me.pageSortHtmlStr += '<td width="'+me.tdwidth+'" height="25" id="themeBt_'+(sortHtml-1)+'" class="R_list_biaoj" onclick="theme.setThemeHtml('+(sortHtml-1)+')">'+sortHtml+'</td>';
	 				}else if(me.userThemes[sortHtml-1].userAnswer!=undefined 
	 						&& me.userThemes[sortHtml-1].userAnswer!=null 
	 						&& me.userThemes[sortHtml-1].userAnswer!='null' 
	 						&& me.userThemes[sortHtml-1].userAnswer!=''){//已答
	 					me.pageSortHtmlStr += '<td width="'+me.tdwidth+'" height="25" id="themeBt_'+(sortHtml-1)+'" class="R_list_yida" onclick="theme.setThemeHtml('+(sortHtml-1)+')">'+sortHtml+'</td>';
	 				}else if(me.userThemes[sortHtml-1].userAnswer && me.userThemes[sortHtml-1].userAnswer!=''){//
	 					me.pageSortHtmlStr += '<td width="'+me.tdwidth+'" height="25" id="themeBt_'+(sortHtml-1)+'" class="R_list_td" onclick="theme.setThemeHtml('+(sortHtml-1)+')">'+sortHtml+'</td>';
	 				}else{
	 					me.pageSortHtmlStr += '<td width="'+me.tdwidth+'" height="25" id="themeBt_'+(sortHtml-1)+'" onclick="theme.setThemeHtml('+(sortHtml-1)+')">'+sortHtml+'</td>';
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
		 		if(me.userTheme.signFlag == true){//标记
		 			document.getElementById("themeBt_"+me.currIndex).className = "R_list_biaoj";
		 		}else if(me.userTheme.userAnswer && me.userTheme.userAnswer!='null' && me.userTheme.userAnswer!=''){//有答案
		 			document.getElementById("themeBt_"+me.currIndex).className = "R_list_yida";
		 		}else{
		 			document.getElementById("themeBt_"+me.currIndex).className = "R_list_td";
		 		}
 			}
	 		me.userTheme = me.getTheme(index);
	 		var ss = document.getElementById("themeBt_"+index);
	 		if(ss)ss.className = "R_list_dangq";
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
		 		if(explain!=undefined && explain!="" && explain!="null" && explain!=null){
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
			 			|| (parseInt(me.userTheme.eachline) > 0 && i>0 && parseInt((i+1)%(parseInt(me.userTheme.eachline)))==0)){
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
				me.themeTableStr = me.replaceImgPath(me.themeTableStr);
			    document.getElementById("themeTable").innerHTML = me.themeTableStr;
			    //选择题显示按钮与考生答案
			 	//类型 5：单选；10：多选 15：填空；20：问答；25：判断；30：视听 35：其他	
		 		me.userAnsSelectedBtStr ="";
		 		me.userAnsStr = "";
		 		
			    for(var i = 0;i<me.userTheme.answer.length;i++){
			    	var themeClass = 'R_btn03';
			    	for(var j=0;j<me.userTheme.userAnswerArr.length;j++){
			    		if(me.answerShowSort[i] == me.userTheme.userAnswerArr[j][0]){
			    			themeClass = 'R_btn03_up';
			    			break;
			    		}
			    	}
		 			me.userAnsSelectedBtStr +=	"<input type='button' name='ans' class='"+themeClass+"' onclick='theme.setAnswer(this,"+i+")' value='"+me.answerShowSort[i]+"' />";
		 		}
		 		me.userTheme.userAnswer = "";
		 		if(me.userTheme.userAnswerArr && me.userTheme.userAnswerArr.length>0){
				 	for(var j=0;j<me.userTheme.userAnswerArr.length;j++){
					   me.userTheme.userAnswer+=me.userTheme.userAnswerArr[j][0]+",";
					}
					if(me.userTheme.userAnswer!="")me.userTheme.userAnswer=me.userTheme.userAnswer.substring(0,me.userTheme.userAnswer.length-1);
		 		}
		 		me.userAnsStr = "您的答案：<span  class=\"R_answer_yx\" >"+me.fmtStr(me.userTheme.userAnswer)+"</span>";
				me.userAnsStr = me.replaceImgPath(me.userAnsStr);
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
	 				ansValue_ = me.decode(ansValue_);
	 				if(i<tmpArr.length-1){
	 					tmpArrStr+=tmpArr[i]+"<input  type='text' class='inputThemeCss' maxlength='100'name='ansText' onblur='theme.setAnswer(this,"+i+")' value='"+ansValue_+"' />";
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
	 			if(explain!=undefined && explain!="" && explain!="null" && explain!=null){
					me.themeTableStr += "<tr>";
		 			me.themeTableStr += "<td class=\"R_timu\" style=\"font-size:12px;\">";
					me.themeTableStr +="（注解："+explain+"）";	
					me.themeTableStr += "</td>";
					me.themeTableStr+="</tr>";
				}
			    me.themeTableStr += "</table>";
	 			me.themeTableStr = me.replaceImgPath(me.themeTableStr);
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
		 		if(explain!=undefined && explain!="" && explain!="null" && explain!=null){
		 			me.themeTableStr += "<tr>";
		 			me.themeTableStr += "<td class=\"R_timu\" style=\"font-size:12px;\">";
					me.themeTableStr +="（注解："+explain+"）";
					me.themeTableStr += "</td>";
					me.themeTableStr+="</tr>";
				}
				me.themeTableStr += "<tr>";
		 		me.themeTableStr += "<td class=\"R_timu\">";
		 		
		 		var ansValue_ = "";
	 			if(me.userTheme.userAnswerArr.length>0 && me.userTheme.userAnswerArr[0]!=null && me.userTheme.userAnswerArr[0]!="null"){
	 				ansValue_ = me.userTheme.userAnswerArr[0];
	 				ansValue_ = me.decode(ansValue_);
	 			}
	 			
				me.themeTableStr += "<textarea  rows='10' cols='100' name='ansText' onblur='theme.setAnswer(this,"+0+")'>"+ansValue_+"</textarea>";
				me.themeTableStr += "</td>";
				me.themeTableStr+="</tr>";
			    me.themeTableStr += "</table>";
				me.themeTableStr = me.replaceImgPath(me.themeTableStr);
	 			document.getElementById("themeTable").innerHTML = me.themeTableStr;
	 			document.getElementById("userAnsSelectedBt").innerHTML = "";
	 			document.getElementById("userAns").innerHTML = "";
	 		}
	 		me.currIndex = index;
	 		me.setjd();
 		}
 	},
 	replaceImgPath : function(imagePath){
 		return imagePath.replace(/src=\"upload/g ,'src="../upload');;
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
 			var ans = document.getElementsByName("ans");
 			var answer = ans[inputIndex].value;
 			if(ans[inputIndex].className == 'R_btn03'){
 				ans[inputIndex].className = 'R_btn03_up';
 			}else{
 				ans[inputIndex].className = 'R_btn03';
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
 			//单选与判断处理，只能选择一个
 			if(me.userTheme.themeType.type == '5' || me.userTheme.themeType.type == '25'){
 				
 				for(var i=0;i<ans.length;i++){
 					if(inputIndex != i){
 						ans[i].className = 'R_btn03';
 					}
 				}
 				for(var i = 0;i<me.userTheme.userAnswerArr.length;i++){
					if(me.userTheme.userAnswerArr[i]!=null
						&& me.userTheme.userAnswerArr[i][1] != (inputIndex+1)){
	 					me.userTheme.userAnswerArr[i] = null;
					}
 				}
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
 			//var newArr = new Array();
 			
 			
 			document.getElementById("userAns").innerHTML = "您的答案：<span  class=\"R_answer_yx\" >"+me.userTheme.userAnswer+"</span>";
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
 		this.setjd();
 		
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
 	rightAnsShow : function(){
 		//子方面实现
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
 		//子方法实现
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
  		if(me.currIndex >= (this.userThemes.length-1)){
  			document.getElementById("nextThemeBt").disabled = true;
  		}else{
  			document.getElementById("nextThemeBt").disabled = false;
  		}
  	},
  	//快捷键
  	themeOnKey : function(){
  			var me = this;
  			var tihtml = document.getElementById("minuteLabel").innerHTML;
			if(me.isHidden!=1 && (tihtml == '等待时间' || tihtml == '&nbsp;')){
				return;
			}
  			if(me.userTheme!=undefined && me.userTheme!=null){
				var keCodeTemp = event.keyCode;
				 //类型 5：单选；10：多选 15：填空；20：问答；25：判断；30：视听 35：其他
	 		    if(me.userTheme.themeType.type == '5' || me.userTheme.themeType.type == '10' || me.userTheme.themeType.type == '25'){
	 				switch(keCodeTemp){
	   					case 37:
	   						me.prevTheme();
	   						break;
	   					case 39:
	   						me.nextTheme();
	   						break;
						case 38:
							if((me.pageIndex-1)>=0)
	   							me.setThemeSortHtml((me.pageIndex-1));
	   						break;
	   					case 40:
	   						if((me.pageIndex+1)<me.pageTabMax)
	   							me.setThemeSortHtml((me.pageIndex+1));
	   						break;
	   					case 49:
	   					case 97:
	   					case 65:
	   						//alerttmp = "1键  a键";
	   						theme.setAnswer(this,0);
	   						break;
	   					case 50:
	   					case 98:
	   					case 66:
	   						//alerttmp = "2键 b键";
	   						theme.setAnswer(this,1);
	   						break;
	   					case 51:
	   					case 99:
	   					case 67:
	   						//alerttmp = "3键 c键";
	   						theme.setAnswer(this,2);
	   						break;
	   					case 52:
	   					case 100:
	   					case 68:
	   						//alerttmp = "4键 d键";
	   						theme.setAnswer(this,3);
	   						break;
	   					case 53:
	   					case 101:
	   					case 69:
	   						//alerttmp = "5键 e键";
	   						theme.setAnswer(this,4);
	   						break;
	   					case 54:
	   					case 102:
	   					case 70:
	   						//alerttmp = "6键 f键";
	   						theme.setAnswer(this,5);
	   						break;
	   					case 55:
	   					case 103:
	   					case 71:
	   						//alerttmp = "7键 g键";
	   						theme.setAnswer(this,6);
	   						break;
   					}
	 		    }else{
	 		    	switch(keCodeTemp){
	   					case 37:
	   						me.prevTheme();
	   						break;
	   					case 39:
	   						me.nextTheme();
	   						break;
						case 38:
							if((me.pageIndex-1)>=0)
	   							me.setThemeSortHtml((me.pageIndex-1));
	   						break;
	   					case 40:
	   						if((me.pageIndex+1)<me.pageTabMax)
	   							me.setThemeSortHtml((me.pageIndex+1));
	   						break;
   					}
	 		    }
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
