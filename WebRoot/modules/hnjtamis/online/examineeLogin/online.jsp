<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/tld/c.tld" prefix="c"%>
<!DOCTYPE html>
<html>
<c:set var="request" scope="page" value="${pageContext.request}" />
<c:set var="serverPort" value="${request.serverPort}" />
<c:set var="base" scope="page" value="${request.scheme}://${request.serverName}:${serverPort}" />
<c:set var="contextPath" scope="page" value="${request.contextPath}" />
<c:set var="basePath" scope="page" value="${base}${contextPath}/" />
<c:set var="requestHttp" scope="page" value="${request.scheme}://${request.serverName}:80" />
<c:if test="${requestHttpUrl!=null && requestHttpUrl!=''}">
<c:set var="requestHttp" scope="page" value="${requestHttpUrl}" />
</c:if>
<head>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="Access-Control-Allow-Origin" content="*">
<title>考试系统</title>
<script type="text/javascript" src="${basePath }base/ext/ext-all.js"></script>
<link href="${basePath}resources/css/ext-all.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="${basePath}modules/hnjtamis/online/examineeLogin/ThemeBase.js"></script>
<script type="text/javascript" src="${basePath}modules/hnjtamis/online/examineeLogin/Theme.js"></script>
<link href="${basePath}modules/hnjtamis/online/examineeLogin/online.css" rel="stylesheet" type="text/css">
<script type="text/javascript">
//判断是否为IE
var isIE = navigator.userAgent.toLocaleLowerCase().indexOf('msie') !== -1;

// 判断是否为IE5678
var isLteIE8 = isIE && !+[1,];


// 用于防止因通过IE8+的文档兼容性模式设置文档模式，导致版本判断失效
var dm = document.documentMode, 
　　isIE5, isIE6, isIE7, isIE8, isIE9, isIE10, isIE11;
if (dm){
　　isIE5 = dm === 5;
　　isIE6 = dm === 6;
　　isIE7 = dm === 7;
　　isIE8 = dm === 8;
　　isIE9 = dm === 9;
　　isIE10 = dm === 10;
　　isIE11 = dm === 11;
}
else{
    // 判断是否为IE5，IE5的文本模式为怪异模式(quirks),真实的IE5.5浏览器中没有document.compatMode属性
isIE5 = (isLteIE8 && (!document.compatMode || document.compatMode === 'BackCompat'));

　　// 判断是否为IE6，IE7开始有XMLHttpRequest对象
　　isIE6 = isLteIE8 && !isIE5 && !XMLHttpRequest;

　　// 判断是否为IE7，IE8开始有document.documentMode属性
　　isIE7 = isLteIE8 && !isIE6 && !document.documentMode;

　　// 判断是否IE8
　　isIE8 = isLteIE8 && document.documentMode;

　　// 判断IE9，IE10开始支持严格模式，严格模式中函数内部this为undefined
　　isIE9 = !isLteIE8 && (function(){
  　　"use strict";
　　    return !!this;
　　}());

　　// 判断IE10，IE11开始移除了attachEvent属性
　　isIE10 = isIE && !!document.attachEvent && (function(){
　　  "use strict";
　　    return !this;
　　}());
    
　　// 判断IE11
　　isIE11 = isIE && !document.attachEvent;
}
String.prototype.trim = function()
{
	return this.replace( /(^\s*)|(\s*$)/g, '' ) ;
};
var theme = null;
Ext.onReady(function(){
	var examlist = new Array();
	<c:if test="${examList ne null}">
	<c:forEach items="${examList}" var="li">
	examlist[examlist.length]={'value':'${li.examId}','text':'${li.examName}'};
	</c:forEach>
	</c:if>
	var loginForm = new Ext.FormPanel({ 
		x:152,
		y:260,
		width:727,
		height:180,
        layout : {
			type : 'table',
			columns : 2,
			tableAttrs : {
				style : {
					width : '100%'
				}
			}
		},
        frame:false,
        defaults: {
           labelWidth:90,
           labelAlign :'right'
        },
        bodyStyle:'padding:10px 5px 0;background-color:#1e73f4;padding-top: 40px;text-align:center;padding-left:60px;padding-right:60px;', 
        defaultType:'textfield',
        monitorValid:true,
        items:[{ 
	            labelStyle:'color:white;font-size: 16px;',
	            fieldLabel:'验证方式', 
	            id:'loginType',
	            emptyText:'请选择验证方式',
	            colspan : 1,
	            width : 270,
	            xtype : 'combo',
	            store:Ext.create('Ext.data.Store', {
	                fields:[{name:'text'},{name: 'value'} ],
	                data :[{'value':0,'text':'用户名'},{'value':1,'text':'身份证'},{'value':2,'text':'准考证'}],
	                autoLoad : true}),
	            allowBlank:false,//不允许为空     
	            mode:'local',
	        	valueField:'value',//值字段名
	        	displayField:'text',//显示字段名
	        	triggerAction:'all',
	        	editable:false,//不可编辑值
	        	listeners: {
	              afterRender: function(combo){combo.setValue( combo.store.getAt(0).get("value"));},
	              select: function(combo) {
                      var v = combo.getValue();
                      var _loginInticket = Ext.getCmp('loginInticket');
                      var _examId = Ext.getCmp('examId');
                      if(v == 2){
                    	  //combo.colspan = 2;
	    		    	  _examId.allowBlank = true;
	    		    	  _examId.disable();
                    	  _loginInticket.setFieldLabel('准考证号');
                    	  _loginInticket.emptyText = '请输入准考证号';
                      }else{
                    	  if(v == 0){
                    		  _loginInticket.setFieldLabel('用户名');
                    		  _loginInticket.emptyText = '请输入用户名';
                    	  }else{
                    		  _loginInticket.setFieldLabel('身份证');
                    		  _loginInticket.emptyText = '请输入身份证';
                    	  }
                    	  //combo.colspan = 1;
                    	  _examId.allowBlank = false;
                    	  _examId.enable();                   	  
                      }
                      _loginInticket.setRawValue(_loginInticket.emptyText);
                      _loginInticket.show();
                  }
	            }
	        },{ 
	            labelStyle:'color:white;font-size: 16px;',
	            fieldLabel:'考试', 
	            id:'examId',
	            emptyText:'请选择考试',
	            colspan : 1,
	            width : 270,
	            xtype : 'combo',
	            store: Ext.create('Ext.data.Store', {
	            	fields:[{name:'text'},{name: 'value'} ],
	                data : examlist,
	                autoLoad : true}),
	            allowBlank:false,//不允许为空     
	            mode:'local',
	        	valueField:'value',//值字段名
	        	displayField:'text',//显示字段名
	        	triggerAction:'all',
	        	editable:false,//不可编辑值
	        	listeners: {
	              afterRender: function(combo) {if(combo.store.getCount()>0)combo.setValue( combo.store.getAt(0).get("value"));} 
	            }
	        },{ 
                labelStyle:'color:white;font-size: 16px;',
                fieldLabel:'用户名', 
                id:'loginInticket',
                emptyText:'请输入用户名',
                colspan : 1,
                width : 270,
                //style:'background-color:red;',
    			//fieldStyle:'background:none; border-right: 0px solid;border-top: 0px solid;border-left: 0px solid;border-bottom: #000000 1px solid;',
                allowBlank:false,
                blankText:"请输入用户名",
                listeners: {
                "focus" : function(){
                	var _loginInticket = Ext.getCmp('loginInticket');
                	if(_loginInticket.emptyText == _loginInticket.getRawValue()){
                		_loginInticket.setValue('');
                	}
                },	
                "blur":function(){
                	var loginType = Ext.getCmp('loginType').getValue();
                	if(loginType==0 || loginType==1){
	                	var loginInticket = Ext.getCmp('loginInticket').getValue();
	                	if(loginInticket!=undefined && loginInticket!=null && loginInticket!=''){
		                	Ext.Ajax.request({
		            		    url: '${basePath}onlineExam/findExamForOnlineListAction!findExam.action',
		            		    timeout: 10000,
		            		    params: { 
		            		    	inticket: loginInticket
		            		    },
		            		    success: function(response) {
		            		    	var result = Ext.decode(response.responseText).rsContent;
		            		    	result = (Ext.decode(result));
		            		    	if(result.result == undefined || result.result == 'undefined' || result.result =='' || result.result ==null){
		            		    		Ext.Msg.alert('提示',result.message);
		            		    	}else{
		            		    		var examlist = result.result.examlist;
		            		    		var store = Ext.getCmp('examId').store;
		            		    		if(store.getCount()>0){
		            		    			for(var i=store.getCount()-1;i>=0;i--){
		            		    				var record = store.getAt(i);
		            		    				store.remove(record);
		            		    			}
		            		    		}
		            		    		var defaultValue = '';
		            		    		var defaultValue_old = Ext.getCmp('examId').getValue();
		            		    		for(var i=0;i<examlist.length;i++){
		            		    			var row = {};
		            		    			row["text"] = examlist[i].text;
		            		    			row["value"] = examlist[i].value;
		            		    			store.insert(i, row);
		            		    			if(i==0){
		            		    				defaultValue = examlist[i].value;
		            		    			}
		            		    			if(defaultValue_old == examlist[i].value){
		            		    				defaultValue = defaultValue_old;
		            		    			}
		            		    		}
		            		    		Ext.getCmp('examId').setValue(defaultValue);
		            		    	}
		            		    },
		            		    failure: function(result) {
		            		    	Ext.Msg.alert('提示','获取人员信息出现错误，请与管理员联系！');
		            		    }
		            		});
	                	}else{
	                		var store = Ext.getCmp('examId').store;
	    		    		if(store.getCount()>0){
	    		    			for(var i=store.getCount()-1;i>=0;i--){
	    		    				var record = store.getAt(i);
	    		    				store.remove(record);
	    		    			}
	    		    		}
	    		    		var defaultValue = '';
	    		    		var defaultValue_old = Ext.getCmp('examId').getValue();
	    		    		for(var i=0;i<examlist.length;i++){
	    		    			var row = {};
	    		    			row["text"] = examlist[i].text;
	    		    			row["value"] = examlist[i].value;
	    		    			store.insert(i, row);
	    		    			if(i==0){
	    		    				defaultValue = examlist[i].value;
	    		    			}
	    		    			if(defaultValue_old == examlist[i].value){
	    		    				defaultValue = defaultValue_old;
	    		    			}
	    		    		}
	    		    		Ext.getCmp('examId').setValue(defaultValue);
	                	}
                   }
                }
               }
            },{ 
            	labelStyle:'color:white;font-size: 16px;',
                fieldLabel:'密码', 
                id:'loginPassword', 
                colspan : 1,
                inputType:'password', 
                width : 270,
                allowBlank:false,
                blankText:"请输入密码"
            },{ 
                text:(isIE6||isIE7||isIE8)?'登录':'',
                xtype:'button',
                id:'loginButtom',
                style:'background-image:url(${basePath }modules/hnjtamis/online/examineeLogin/images/login_btn1.png)',
                width : 83,
                height: 34,
                colspan : 2,
                formBind: true,
                margin : '20 0 0 0',
                fieldLabel:'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;', 
                handler:function(){ 
                	var loginInticket = Ext.getCmp('loginInticket').getValue();
                	var loginPassword = Ext.getCmp('loginPassword').getValue();
                	var loginType = Ext.getCmp('loginType').getValue();
                	if(loginType==0 || loginType==1){
                		var examId = Ext.getCmp('examId').getValue();
                		Ext.Ajax.request({
                		    url: '${basePath}onlineExam/findExamIntickeForOnlineListAction!findExamInticke.action',
                		    timeout: 10000,
                		    params: { 
                		    	inticket: loginInticket,
                		    	pwd: loginPassword,
                		    	id : examId
                		    },
                		    success: function(response) {
                		    	var result = Ext.decode(response.responseText).rsContent;
                		    	result = (Ext.decode(result));
                		    	if(result.result == undefined || result.result == 'undefined' || result.result =='' || result.result ==null){
                		    		Ext.Msg.alert('提示',result.message);
                		    	}else{
	                		    	loginForm.loginFormFun(result.result.loginInticket,result.result.loginPassword);
                		    	}
                		    },
                		    failure: function(result) {
                		    	Ext.Msg.alert('提示','获取人员信息出现错误，请与管理员联系！');
                		    }
                		});
                	}else{
                		loginForm.loginFormFun(loginInticket,loginPassword);
                	}
                } 
            }],
            buttons:[],
            listeners: {  
		        afterRender: function(thisForm, options){  
		            this.keyNav = Ext.create('Ext.util.KeyNav', this.el, {  
		                enter: function(){  
		                    // 筛选表格  
		                    var btn = Ext.getCmp('loginButtom');  
		                    btn.handler() ; 
		                },  
		                scope: this  
		            });  
		        }  
		    },
		    loginFormFun : function(loginInticket,loginPassword){
		    	var statecp = new Ext.state.CookieProvider();//cookie对象
            	Ext.state.Manager.setProvider(statecp);
            	var inticketKeyName = loginInticket+'_KEY';
            	var inticketKey = statecp.get(inticketKeyName);
            	if(inticketKey==null || inticketKey==undefined){
            		inticketKey = '';
            	}
            	loginInticket = loginInticket.trim();
            	loginPassword = loginPassword.trim();
            	Ext.data.JsonP.request({
            	    url: '${requestHttp}'+'/login?',
            	    timeout: 5000,
            	    params: { 
            	    	inticket: loginInticket,
            	    	password:loginPassword,
            	    	rancode : inticketKey
            	    },
            	    callbackKey: "jsonpcallback",
            	    success: function(result) {
            	    	if(result['code']=='00001'){
            	    		statecp.set(inticketKeyName,result['result']['loginCode']);
            	    		//登录 
            	    		if(result && result.result){
	    		   				theme = Ext.create('modules.hnjtamis.online.examineeLogin.Theme',
                	    				{requestHttp:'${requestHttp}',examUser : result.result});
	    		   				document.onkeydown = function(){theme.themeOnKey();}
	    		   				win.close();
            	    		}else{
            	    			Ext.Msg.alert('提示','登录存在问题，请联系管理员！');
            	    		}
            	    	}else{
                	    	Ext.Msg.alert('提示',result['message']);
            	    	}
            	    },
            	    failure: function(result) {
            	    	Ext.Msg.alert('提示','服务器连接失败，请稍后再试！');
            	    }
            	});
		    }
    });
	
		    var win = new Ext.Window({
		        title:'在线考试 - 考生登录',  
		        layout : {type : 'table',columns : 1},
		        border : false,
			    frame : false,
			    width:1024,
			    height:640,
		        closable: false,
		        closeAction : 'hide',
		        resizable: false,
		        draggable: true,
		        modal : true,// 模态
		        plain: false,
		        bodyStyle:'background-image:url(${basePath }modules/hnjtamis/online/examineeLogin/images/newbg.jpg)',
		        items: [loginForm]
		    });    
		    win.show();
});
window.onbeforeunload = function(event) { 
	try{
		if(theme!=null){
			if(theme.isSubmitFlag == 1){
				theme.saveAnswer('autoSave');
			}
			Ext.data.JsonP.request({
		     	timeout: 10000,
		  	 	url : '${requestHttp}'+'/clearcode',
		   		callbackKey: "jsonpcallback",
		   		params : {
					examid : theme.examUser.examId,
					inticket : theme.examUser.inticket
				},
		   		success : function(result) {}
			});	
		}
	}catch(e){}
} 	
</script>
</head>
<body bgcolor="#e0f0fe" text="#000000" style="padding-left: 5px;padding-right: 5px;overflow: hidden;overflow-y: auto;">
<table  width="100%" height="100%" border="0" cellspacing="4" cellpadding="0" align="center">
  <tr><!--系统顶部 区域-->
    <td colspan="2" style="height:50px; background:#558f17 url(${basePath }modules/hnjtamis/online/examineeLogin/images/ks_top01.jpg); margin:0px;">
        <table border="0" cellspacing="0" cellpadding="0" align="left">
          <tr>
            <td><img src="${basePath }modules/hnjtamis/online/examineeLogin/images/ks_toplogo.jpg" width="393" height="49" /></td>
            <td class="logodiv" valign="bottom" id="examName">&nbsp;</td>
          </tr>
        </table>
    </td>
  </tr>
  <tr>
    <td height="42" style=" background:#fff url(${basePath }modules/hnjtamis/online/examineeLogin/images/ks_left01.jpg) left bottom repeat-x; width: 202px; border: 1px solid #a3a3a3;">
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td align="right" valign="bottom" style=" line-height:24px;font-size: 12px;" id="minuteLabel">&nbsp;</td>
        <td style="font-size: 28px;width:130px;" id="minute">&nbsp;</td>
      </tr>
    </table>
    </td>
    <td id="table1" height="auto" rowspan="2" align="center" valign="top" background="${basePath }modules/hnjtamis/online/examineeLogin/images/ks_bg.jpg" 
    	style="border: 1px solid #a3a3a3;width:100%;font-size: 12px;">
    <form id="form1" name="form1" method="post" action="">
    <table width="100%" height="100%" id="table2" border="0" cellspacing="8" cellpadding="0">
      <tr>
        <td valign="top">
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr align="left"><!--系统题型、交卷按钮 区域-->
            <td width="34%" height="40" style="font-weight: bold; font-size: 14px; border-bottom: 1px dashed #555555; color: #000;" id="themeTypeLabel">&nbsp;</td>
            <td width="66%" align="right" style=" border-bottom: 1px dashed #555555;" >
            	<input type="button" name="savebutton" id="savebutton" class="R_btn01" value=" " alt="暂存" onclick="theme.saveAnswer('save')" disabled="disabled"/>
            	<input type="button" name="submitbutton" id="submitbutton" class="R_btn02" value=" " alt="交卷" onclick="theme.saveAnswer('submit')" disabled="disabled" />
            </td>
          </tr>
        </table>
        <!--问题区 开始-->
        <div id="themeTable">
        <table width="97%" border="0" cellpadding="0" cellspacing="0" class="R_answer" align="center" >
          <tr>
            <th colspan="2" class="R_timu">&nbsp;</th>
          </tr>
          <tr>
            <td height="32">&nbsp;</td>
            <td>&nbsp;</td>
          </tr>
          <tr>
            <td height="32">&nbsp;</td>
            <td>&nbsp;</td>
          </tr>
          <tr>
            <td height="32">&nbsp;</td>
            <td>&nbsp;</td>
          </tr>
             <tr>
            <td height="32">&nbsp;</td>
            <td>&nbsp;</td>
          </tr>
        </table>
        </div>
        <!--问题区 结束-->
        </td>
      </tr>
      <tr>
        <td valign="bottom" align="center">
        <table width="97%" border="0" cellspacing="0" cellpadding="0">
          <!--答题区 开始-->
          <tr>
            <td style="text-align: left; font-size: 14px; color: #000;" id="userAns"><span  class="R_answer_yx" >&nbsp;</span></td>
            <td width="60%" height="40" align="right" id="userAnsSelectedBt" style="text-align: right;">&nbsp;</td>
          </tr>
          <tr>
            <td height="39" colspan="2" style="text-align: right; text-indent: 7px; font-size: 14px; color: #000;">
              <input type="button" name="jdHideBt" id="jdHideBt" class="R_btn04" value="隐藏进度" onclick="theme.jdTrHide()"/>
              <input type="button" name="signFlagBt" id="signFlagBt" class="R_btn04" value="标记" onclick="theme.setSignFlag()" disabled="disabled"/>
              <input type="button" name="prevThemeBt" id="prevThemeBt" class="R_btn04" value="上一题" onclick="theme.prevTheme()" disabled="disabled"/>
              <input type="button" name="nextThemeBt" id="nextThemeBt" class="R_btn04" value="下一题" onclick="theme.nextTheme()" disabled="disabled"/>
            </td>
          </tr>
          <!--答题区 结束-->
          <!--进度条 开始-->
          <tr>
            <td height="9" colspan="2" align="left" id="jdBfbTd"><div class="R_pro"><img id="jdBfb" src="${basePath }modules/hnjtamis/online/examineeLogin/images/ks_pro01.jpg" height="9" width="0%" style="padding-bottom: 4px;" /></div></td>
          </tr>
          <tr>
            <td height="20" colspan="2" align="left"><img src="${basePath }modules/hnjtamis/online/examineeLogin/images/ks_pro02.jpg" align="absbottom" style="display:inline;"/><span style="font-size: 12px; color: #000;" id="themeJdContent">进度：0% (0/0)</span></td>
          </tr>
          <!--进度条 结束-->
          <tr id="jdTr">
            <td align="left" colspan="2">
            <!--进度页签列表 开始-->
            <table border="0" cellspacing="0" cellpadding="0" style="display:inline;">
              <tr>
                <td id="themeSortTable" style="height: 55px;">&nbsp;</td>
                <td align="left" valign="top" id="themeSortTab">&nbsp;</td>
              </tr>
            </table>
            <!--进度页签列表 开始-->
          </td>
          </tr>
          </table>
        </td>
      </tr>
    </table></form>
    </td>
  </tr>
  <tr width="202">
    <td height="auto" valign="top" background="${basePath}modules/hnjtamis/online/examineeLogin/images/ks_bg.jpg" style="border:1px solid #a3a3a3;width:202;">
    <table width="202" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td align="center"><img src="${basePath}modules/hnjtamis/online/examineeLogin/images/ks_left02.jpg" /></td>
      </tr>
      <tr>
        <td height="336" valign="top" background="${basePath }modules/hnjtamis/online/examineeLogin/images/ks_left03.jpg">
   	    <!--考生信息 开始-->
        <table width="202" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td height="22" class="left_zi1">姓名</td>
          </tr>
          <tr>
            <td height="20" class="left_zi2" id="userName">&nbsp;</td>
          </tr>
          <tr>
            <td height="22" class="left_zi1">身份证</td>
          </tr>
          <tr>
            <td height="20" class="left_zi2" id="idNumber">&nbsp;</td>
          </tr>
          <tr>
            <td height="22" class="left_zi1">准考证号</td>
          </tr>
          <tr>
            <td height="20" class="left_zi2" id="inticket">&nbsp;</td>
          </tr>
          <tr>
            <td height="22" class="left_zi1">机构</td>
          </tr>
          <tr>
            <td height="20" class="left_zi2" id="organName">&nbsp;</td>
          </tr>
          <tr>
            <td height="22" class="left_zi1">开始时间</td>
          </tr>
          <tr>
            <td height="20" class="left_zi2" id="examStartTime">&nbsp;</td>
          </tr>
          <tr>
            <td height="22" class="left_zi1">结束时间</td>
          </tr>
          <tr>
            <td height="20" class="left_zi2" id="examEndTime">&nbsp;</td>
          </tr>
          <tr>
            <td height="22" class="left_zi1">&nbsp;</td>
          </tr>
          <tr>
            <td height="20" class="left_zi2" id="synchroTime">&nbsp;</td>
          </tr>
          <tr>
            <td height="22" class="left_zi1">&nbsp;</td>
          </tr>
          <tr>
            <td height="20" class="left_zi2" id="nextsynchroTime">&nbsp;</td>
          </tr>
        </table>
   	    <!--考生信息 结束-->
        </td>
      </tr>
    </table>
      <img src="${basePath }modules/hnjtamis/online/examineeLogin/images/ks_left04.jpg" width="184" height="36" style="margin:40px 0px 0px 9px;cursor:pointer;"
	       onclick="openWin('examHelp','${basePath}modules/hnjtamis/online/examineeLogin/examhelp.html')" /></td>
  </tr>
  <tr><!--系统底部 区域-->
    <td colspan="2" style="height:24px; background-color:#b5d2f0;">&nbsp;</td>
  </tr>
</table>
</body>
</html>