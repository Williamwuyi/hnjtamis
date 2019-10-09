<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib uri="/WEB-INF/tld/c.tld" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<c:set var="request" scope="page" value="${pageContext.request}" />
	<c:set var="base" scope="page"
		value="${request.scheme}://${request.serverName}:${request.serverPort}" />
	<c:set var="contextPath" scope="page" value="${request.contextPath}" />
	<c:set var="basePath" scope="page" value="${base}${contextPath}/" />
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=uft-8">
		<link rel="stylesheet" type="text/css" href="${basePath}resources/css/ext-all.css"/>
		<script type="text/javascript" src="${basePath}base/ext/ext-all.js"></script>
		<title>Insert title here</title>
		<script type="text/javascript">
		 Ext.onReady(function(){
             //Ext.BLANK_IMAGE_URL = 'http://www.cnblogs.com/extjs2.0/resources/images/default/s.gif';
             var courseInfoTab = "{'id':'CourseInfo','text':'课程首页','url':'${basePath}train/online/showCourseInfoForTrainOnlineListAction!showCourseInfo.action?courseId=${param.courseId}&"+Math.random()+"'}";
             var testBeforeTab = "{'id':'TestBefore','text':'课前评测','url':'${basePath}train/online/testBeforeForTrainOnlineListAction!testBefore.action?courseId=${param.courseId}&"+Math.random()+"'}";
             var studyTab = "{'id':'Study','text':'课程学习','url':'${basePath}train/online/showStudyForTrainOnlineListAction!showStudy.action?courseId=${param.courseId}&"+Math.random()+"'}";
             var testAfterTab = "{'id':'TestAfter','text':'课后考试','url':'${basePath}train/online/testAfterForTrainOnlineListAction!testAfter.action?courseId=${param.courseId}&"+Math.random()+"'}";
             var studyRecordTab = "{'id':'StudyRecord','text':'学习记录','url':'${basePath}train/online/findStudyRecordForTrainOnlineListAction!findStudyRecord.action?courseId=${param.courseId}&"+Math.random()+"'}";
             var strTabs = "[" + courseInfoTab + "," + testBeforeTab + "," + studyTab + "," + testAfterTab + "," + studyRecordTab +"]";
             var oTabs = eval('(' + strTabs + ')');
             if (oTabs != null && oTabs.length > 0) {
                 document.getElementById("frmContent").src = oTabs[0].url;
                 var tabs = new Ext.TabPanel({
                     id: 'TabFrame',
                     renderTo: 'tabsContent',
                     activeTab: 0,
                     collapsed: true,
                     items: [{
                         id: oTabs[0].id,
                         title: oTabs[0].text,
                         contentEl: 'tabItem'
                     }]
                 });
                 
                 for (var j = 1; j < oTabs.length; j++) {
                     var oItem = {};
                     oItem.id = oTabs[j].id;
                     oItem.title = oTabs[j].text;
                     oItem.contentEl = 'tabItem';                      
                     tabs.add(oItem);
                 }
                 
                 tabs.addListener("tabchange", function(tabs, nowtab){
                     document.getElementById("frmContent").src = "";
                     document.getElementById("frmContent").height = window.parent.document.body.clientHeight - 125;
                     for (var s = 0; s < oTabs.length; s++) {
                         if (oTabs[s].id == nowtab.id) {
                             document.getElementById("frmContent").src = oTabs[s].url;
                             break;
                         }
                     }
                 });
             }
             else {
                 document.getElementById("tabsContent").style.display = "none";
             }
         });

		function setFrameHeight(){
			/*var ifm= document.getElementById("frmContent");
			var subWeb = document.frames ? document.frames["frmContent"].document : ifm.contentDocument;
			
			if(ifm != null && subWeb != null) {
				ifm.height = subWeb.body.scrollHeight;
				if(subWeb.body.scrollHeight == 0) {
					ifm.height = window.parent.document.body.clientHeight - 125;
				}
			}*/
			SetWinHeight();
		}


	    function SetWinHeight() {
		    var win = document.getElementById("frmContent");
		    if (document.getElementById) {
			    if (win && !window.opera) {
				    if (win.contentDocument && win.contentDocument.body.offsetHeight) {
					    win.height = win.contentDocument.body.offsetHeight;
					} else if(win.Document && win.Document.body.scrollHeight) {
						win.height = win.Document.body.scrollHeight;
					}
					//alert(win.height);
				}
			}
		}

		function doActiveTab(tabId) {
			Ext.getCmp('TabFrame').setActiveTab(Ext.getCmp(tabId));			
		}
		         
	</script>
    </head>
    <body style="margin-left: 2px">
        <div id="tabsContent" style="margin-top: 2px;">
            <div id="tabItem" style="width: 0px; height: 0px;">
            </div>
        </div>
        <div id="tabItemsRender">
            <iframe id="frmContent" name="frmContent" src="" frameborder="0" height="100%" width="100%" scrolling="no" onload="setFrameHeight();">
            </iframe>
        </div>
    </body>
</html>
<script>
	//setFrameHeight;
</script>