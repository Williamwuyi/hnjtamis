<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/tld/c.tld" prefix="c"%>
<!DOCTYPE html>
<html>

<c:set var="request" scope="page" value="${pageContext.request}" />
<c:set var="serverPort" value="${request.serverPort}" />
<c:set var="base" scope="page" value="${request.scheme}://${request.serverName}:${serverPort}" />
<c:set var="contextPath" scope="page" value="${request.contextPath}" />
<c:set var="basePath" scope="page" value="${base}${contextPath}/" />

<head>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="Access-Control-Allow-Origin" content="*">
<title>亿泰企业管理平台</title>
    <script type="text/javascript" src="${basePath }base/ext/ext-all.js"></script>
	<link href="${basePath}resources/css/ext-all.css" rel="stylesheet" type="text/css">
	<style type="text/css">
		.myTest{
			font-size: 24px;
		}
	</style>
	<script type="text/javascript" >
	Ext.onReady(function(){
		var loginForm = new Ext.FormPanel({ 
			x:152,
			y:260,
			width:727,
			height:120,
	        layout : {
				type : 'table',
				columns : 3,
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
	        bodyStyle:'padding:10px 5px 0;background-color:#1e73f4;padding-top: 40px;', 
	        defaultType:'textfield',
	        monitorValid:true,
	        items:[{ 
	                fieldLabel:'准考证号', 
	                id:'inticket',
	                emptyText:'请输入准考证号',
	                colspan : 1,
	                width : 240,
	                //style:'background-color:red;',
	                labelStyle:'color:white;font-size: 16px;',
        			//fieldStyle:'background:none; border-right: 0px solid;border-top: 0px solid;border-left: 0px solid;border-bottom: #000000 1px solid;',
	                allowBlank:false
	            },{ 
	            	labelStyle:'color:white;font-size: 16px;',
	                fieldLabel:'密码', 
	                id:'password', 
	                colspan : 1,
	                inputType:'password', 
	                width : 240,
	                allowBlank:false 
	            },{ 
	                //text:'登录',
	                xtype:'button',
	                id:'loginButtom',
	                style:'background-image:url(images/login_btn1.png)',
	                width : 83,
	                height: 34,
	                formBind: true, 
	                handler:function(){ 
	                	var inticket = Ext.getCmp('inticket').getValue();
	                	var password = Ext.getCmp('password').getValue();
	                	
	                	Ext.data.JsonP.request({
	                	    url: 'http://127.0.0.1:80/',
	                	    timeout: 300000,
	                	    params: { 
	                	    	inticket: inticket,
	                	    	reqfunc:'login',
	                	    	password:password
	                	    },
	                	    callbackKey: "jsonpcallback",
	                	    success: function(result) {
	                	    	console.log(result);
	                	    },
	                	    failure: function(result) {
	                	    	console.log(result);
	                	    }
	                	});
	                } 
	            },{ 
	            	xtype:'hidden',
	                name:'reqfunc', 
	                value : 'login'
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
			    }
	    });
		/* var logoPanel = new Ext.Panel({
            html:'<center><img width="400" height="100" src="resources/mylogin.png"/></center>',
            id : 'login-logo',
            width:'100%',
            region : 'center'
        }); */
	    var win = new Ext.Window({
	        //title:'考生登录',  
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
	        bodyStyle:'background-image:url(images/newbg.jpg)',
	        items: [loginForm]
	    });    
	    win.show();  
	});
	
	//--------------以下为在线考试使用---------------
	</script>
</head>
<body>
<body bgcolor="#ebf5d2" text="#000000" onload="autoInit();">
<form id="examex">
<table width="1000" height="100%" border="0" cellspacing="4" cellpadding="0" align="center">
  <tr><!--系统顶部 区域-->
    <td colspan="2" style="height:50px; background:#558f17 url(imagesNew/ks_top01.jpg); margin:0px;">
        <table border="0" cellspacing="0" cellpadding="0" align="left">
          <tr>
            <td><img src="${ksToplogo}" width="393" height="49" /></td>
            <td class="logodiv" valign="bottom">-- ${onlineloginform.examname}</td>
          </tr>
        </table>
    </td>
  </tr>
  <tr>
    <td height="42" style=" background:#fff url(imagesNew/ks_left01.jpg) left bottom repeat-x; width: 202px; border: 1px solid #a3a3a3;">
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td align="right" valign="bottom" style=" line-height:24px;font-size: 12px;" id="sjth"><c:if test="${onlineloginform.beforetime == '0'}">剩余</c:if><c:if test="${onlineloginform.beforetime != '0'}">等待</c:if>时间</td>
        <td style="font-size: 28px;">
        	<c:if test="${type eq 'kaoshi'}">
        	<div id="minute" style="width: 50%;float:left; display:inline; text-align:right">
					<c:if test="${onlineloginform.beforetime == '0'}">${onlineloginform.surplustime}</c:if>
					<c:if test="${onlineloginform.beforetime != '0'}">${onlineloginform.beforetime}</c:if>
			</div>
		    <div style="width: 10px;float:left; display:inline;">:</div>
		    <div id="second" style="float:left; display:inline;text-align:left;">00</div>
		    </c:if>
		    <c:if test="${type eq 'send'}">&nbsp;</c:if>
        </td>
      </tr>
    </table>

    </td>
    <td height="auto" rowspan="2" align="center" valign="top" background="imagesNew/ks_bg.jpg" style="border: 1px solid #a3a3a3; width: 796px; font-size: 12px;">
    <table width="100%" height="100%" id="table2" border="0" cellspacing="8" cellpadding="0">
    <form id="form1" name="form1" method="post" action="">
      <tr>
        <td valign="top">
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr align="left"><!--系统题型、交卷按钮 区域-->
            <td width="34%" height="40" style="font-weight: bold; font-size: 14px; border-bottom: 1px dashed #555555; color: #000;" id="typetd">&nbsp;</td>
            <td width="66%" align="right" style=" border-bottom: 1px dashed #555555;" >
            	<input name="btn_save" type="button" onclick="tempSave('hand')" class="R_btn01" value=" " alt="暂存" />
            	<c:if test="${testtype ne 'true'}">
            	<input name="btn_kaikao" type="button" onclick="if(confirm('提交后会关闭当前窗口！是否继续？'))submitTestPaper()" class="R_btn02" value=" " alt="交卷" />
            	</c:if>
            </td>
          </tr>
        </table>
        <!--问题区 开始-->
        <table width="97%" border="0" cellpadding="0" cellspacing="0" class="R_answer" align="center" >
          <tr>
            <th colspan="2" class="R_timu" id="themeContent">&nbsp;</th>
          </tr>
          <%-- %>tr>
            <td height="32">A、答案一</td>
            <td>E、答案五</td>
          </tr>
          <tr>
            <td height="32">B、答案二</td>
            <td>F、答案六</td>
          </tr>
          <tr>
            <td height="32">C、答案三</td>
            <td>&nbsp;</td>
          </tr>
             <tr>
            <td height="32">D、答案四</td>
            <td>&nbsp;</td>
          </tr --%>
        </table>
        <!--问题区 结束-->
        </td>
      </tr>
      <tr>
        <td valign="bottom" align="center">
        <table width="97%" border="0" cellspacing="0" cellpadding="0">
          <!--答题区 开始-->
          <tr>
            <td style="text-align: left; font-size: 14px; color: #000;" nowrap="nowrap">
            	<span id="ndda">您的答案：</span><span class="answerbtn" id="answerkeyNo" ondblclick="cleanA();" title="双击清空答案"></span>
            </td>
            <td width="396" height="40" align="right" id="answerkeyButton">
              <input name="" type="button" class="R_btn03" value="A">
              <input name="" type="button" class="R_btn03" value="B">
              <input name="" type="button" class="R_btn03" value="C">
              <input name="" type="button" class="R_btn03" value="D">
              <input name="" type="button" class="R_btn03" value="E">
              <input name="" type="button" class="R_btn03" value="F">
             </td>
          </tr>
          <tr>
            <td style="text-align: left; font-size: 14px; color: #000;" nowrap="nowrap" >
            	<font color=red>注：按方向键可以选择题目，按数字键可以选择答案。</font>
            </td>
            <td height="39" nowrap="nowrap"  style="text-align: right; text-indent: 7px; font-size: 14px; color: #000;"><input type="button" name="diplaylistTableBt" id="diplaylistTableBt" onclick="setListTable()" class="R_btn04" value="进度隐藏" />
              <c:if test="${type eq 'kaoshi'}"><input type="button" name="markBt" id="markBt" class="R_btn04" onclick="mark(this)" value="标记" /></c:if>
              <input type="button" name="prevBt" id="prevBt" class="R_btn04" onclick="up()" value="上一题(←)" title="请按←自动执行上一题" />
              <input type="button" name="nextBt" id="nextBt" class="R_btn04" onclick="next()" value="下一题(→)" title="请按→自动执行下一题" />
            </td>
          </tr>
          <!--答题区 结束-->
          <!--进度条 开始-->
          <tr>
            <td height="9" colspan="2" align="left" ><div class="R_pro" id="lxjd"><img id="lxjd1" src="imagesNew/ks_pro01.jpg" height="9" width="90%" /></div></td>
          </tr>
          <tr>
            <td height="20" colspan="2" align="left">
	            <img src="imagesNew/ks_pro02.jpg" align="absbottom" style="display:inline;"/>
	            <span id="lxjdContent" style="font-size: 12px; color: #000;">进度:0%&nbsp;</span>&nbsp;
	            <span id="jddesc" style="font-size: 12px; color: #000;"></span>
            </td>
          </tr>
          <!--进度条 结束-->
          <tr id="listTable">
            <td align="left" colspan="2">
            <!--进度页签列表 开始-->
            <table border="0" cellspacing="0" cellpadding="0" style="display:inline;">
              <tr>
                <td width="673" id ="themeSizeTd">
                <table border="1" bordercolor="#858585" cellspacing="0" cellpadding="0" bgcolor="#FFFFFF" style="border-collapse:collapse;" >
                  <tr align="center">
                    <td width="34" height="17" class="R_list_dangq">1</td>
                    <td width="34" class="R_list_yida">2</td>
                    <td width="34" class="R_list_biaoj">3</td>
                    <td width="34">4</td>
                    <td width="34">5</td>
                    <td width="34">6</td>
                    <td width="34">7</td>
                    <td width="34">8</td>
                    <td width="34">9</td>
                    <td width="34">10</td>
                    <td width="34">11</td>
                    <td width="34">12</td>
                    <td width="34">13</td>
                    <td width="34">14</td>
                    <td width="34">15</td>
                    <td width="34">16</td>
                    <td width="34">17</td>
                    <td width="34">18</td>
                    <td width="34">19</td>
                    <td width="34">20</td>
                  </tr>
                  <tr align="center">
                    <td height="17">21</td>
                    <td>22</td>
                    <td>23</td>
                    <td>24</td>
                    <td>25</td>
                    <td>26</td>
                    <td>27</td>
                    <td>28</td>
                    <td>29</td>
                    <td>30</td>
                    <td>31</td>
                    <td>32</td>
                    <td>33</td>
                    <td>34</td>
                    <td>35</td>
                    <td>36</td>
                    <td>37</td>
                    <td>38</td>
                    <td>39</td>
                    <td>40</td>
                  </tr>
                  <tr align="center">
                    <td height="17">41</td>
                    <td>42</td>
                    <td>43</td>
                    <td>44</td>
                    <td>45</td>
                    <td>46</td>
                    <td>47</td>
                    <td>48</td>
                    <td>49</td>
                    <td>50</td>
                    <td>51</td>
                    <td>52</td>
                    <td>53</td>
                    <td>54</td>
                    <td>55</td>
                    <td>56</td>
                    <td>57</td>
                    <td>58</td>
                    <td>59</td>
                    <td>60</td>
                  </tr>
                  <tr align="center">
                    <td height="17">61</td>
                    <td>62</td>
                    <td>63</td>
                    <td>64</td>
                    <td>65</td>
                    <td>66</td>
                    <td>67</td>
                    <td>68</td>
                    <td>69</td>
                    <td>70</td>
                    <td>71</td>
                    <td>72</td>
                    <td>73</td>
                    <td>74</td>
                    <td>75</td>
                    <td>76</td>
                    <td>77</td>
                    <td>78</td>
                    <td>79</td>
                    <td>80</td>
                  </tr>
                  <tr align="center">
                    <td height="17">81</td>
                    <td>82</td>
                    <td>83</td>
                    <td>84</td>
                    <td>85</td>
                    <td>86</td>
                    <td>87</td>
                    <td>88</td>
                    <td>89</td>
                    <td>90</td>
                    <td>91</td>
                    <td>92</td>
                    <td>93</td>
                    <td>94</td>
                    <td>95</td>
                    <td>96</td>
                    <td>97</td>
                    <td>98</td>
                    <td>99</td>
                    <td>100</td>
                  </tr>
                </table>
                </td>
                <td align="left" valign="top" id="themeSizelist" style="display:<c:if test="${onlineloginform.totaltheme < 101}"> none;</c:if>;">
                	
                </td>
              </tr>
            </table>
            <!--进度页签列表 开始-->
          </td>
          </tr>
          </table>
        </td>
      </tr>
     </form>
    </table>
    </td>
  </tr>
  <tr>
    <td height="auto" valign="top" background="imagesNew/ks_bg.jpg" style="border:1px solid #a3a3a3;">
    <table width="202" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td align="center"><img src="imagesNew/ks_left02.jpg" /></td>
      </tr>
      <tr>
        <td height="336" valign="top" background="imagesNew/ks_left03.jpg">
   	    <!--考生信息 开始-->
        <table width="202" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td height="22" class="left_zi1">姓名</td>
          </tr>
          <tr>
            <td height="20" class="left_zi2">${onlineloginform.peopleName}</td>
          </tr>
          <tr>
            <td height="22" class="left_zi1">身份证</td>
          </tr>
          <tr>
            <td height="20" class="left_zi2">${onlineloginform.idNumber}</td>
          </tr>
          <tr>
            <td height="22" class="left_zi1">准考证号</td>
          </tr>
          <tr>
            <td height="20" class="left_zi2">${onlineloginform.inticket}</td>
          </tr>
          <tr>
            <td height="22" class="left_zi1">机构</td>
          </tr>
          <tr>
            <td height="20" class="left_zi2">${onlineloginform.organ}</td>
          </tr>
          <tr>
            <td height="22" class="left_zi1">开始时间</td>
          </tr>
          <tr>
            <td height="20" class="left_zi2">${onlineloginform.examdate}</td>
          </tr>
          <tr>
            <td height="22" class="left_zi1" >结束时间</td>
          </tr>
          <tr>
            <td height="20" class="left_zi2">${onlineloginform.beginend}</td>
          </tr>
          <tr>
            <td height="22" class="left_zi1">同步时间</td>
          </tr>
          <tr>
            <td height="20" class="left_zi2" id="syncTitle">--</td>
          </tr>
          <tr>
            <td height="22" class="left_zi1">下次同步时间</td>
          </tr>
          <tr>
            <td height="20" class="left_zi2" id="syncTitleNext">--</td>
          </tr>
        </table>
   	    <!--考生信息 结束-->
        </td>
      </tr>
    </table>
    <img src="imagesNew/ks_left04.jpg" width="184" height="36" style="margin:40px 0px 0px 9px;cursor:pointer;" onclick="window.open('${basePath}/pages/online/examhelp.html')"/>
    </td>
  </tr>
  <tr><!--系统底部 区域-->
    <td colspan="2" style="height:24px; background-color:#c9e1a7;">&nbsp;</td>
  </tr>
</table>
</body>
</form>
<form action="${basePath}/onlineSaveAction.action?action=save" id="saveForm" name="saveForm" method="post">
<input type="hidden" name="examXmlValue" id="examXmlValue" value='${examTitleXml}'/>
<input type="hidden" name="examRightAnswerXml" id="examRightAnswerXml" value='${examRightAnswerXml}'/>
<input type="hidden" name="examAnswerXml" id="examAnswerXml" value='${examAnswerXml}'/>
<input type="hidden" name="exampeopleidValue" id="exampeopleidValue" value='${onlineloginform.exampeopleid}'/>
<input type="hidden" name="examPeopleId" id="examPeopleId" value='${examPeopleId}'/>
<input type="hidden" name="examid" id="examid" value='${examid}'/>
<input type="hidden" name="fullmark" id="fullmark" value='${onlineloginform.fullmark}'/>
</form>
</body>
</body>
</html>