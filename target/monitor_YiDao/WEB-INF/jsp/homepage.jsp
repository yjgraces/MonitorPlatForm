<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    
    <title>易道监控系统平台</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/reset.css" type="text/css"></link>
	<link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/top.css" type="text/css"></link>
	<link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/left.css" type="text/css"></link>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/dialog.css"/>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/manage.css"/>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/lib/bootstrap-table/bootstrap-table.css"/>

    <script type="text/javascript" src="${pageContext.request.contextPath}/static/js/jquery.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/static/js/dialog.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/static/js/base.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/static/lib/bootstrap-table/bootstrap-table.js"></script>

	 <script>
        $(function(){
        
        	$('.nav-item').on('click',function(){
			    $(this).addClass('cur_kinds').siblings().removeClass('cur_kinds');
			});
        
            $(".menu_title").click(function() {
                var sib=$(this).siblings('.nav-item-list');
                if (sib.css("display")=="block") {
                    $(sib).slideUp();
                } else {
                    $(sib).slideDown();
                }
            });
        });
        
        //讨厌起方法名!
        function listTest(monitorSystem) {
			document.getElementById("iframe1").src = "/monitor_YiDao/scheduler/listTest?monitorSystem="+monitorSystem; 
        };
         
        function hostsItems(monitorSystem) {
			document.getElementById("iframe1").src = "/monitor_YiDao/hosts/hostsItems?monitorSystem="+monitorSystem; 
        };
        
        function toContactsList(monitorSystem) {
			document.getElementById("iframe1").src = "/monitor_YiDao/contacts/toContactsList?monitorSystem="+monitorSystem; 
        };
        
        function toAddPage(){
			document.getElementById("iframe1").src = "/monitor_YiDao/system/toAddSystem";
        }
        function toListPage(){
			document.getElementById("iframe1").src = "/monitor_YiDao/system/systemList";
        }
        
    </script>
	
  </head>
  <body>
	<body>
		<div class="wrap_w header">
		    <div class="top_nav wrap">
		        <div class="left_logo">
		            <a class="logo">logo</a>
		            <span class="text_info">监控系统平台</span>
		        </div>
		    </div>
		</div>
		<div class="content-wrap">
		<div id="leftNav" class="menu_wrap">
		    <div class="menu_content">
		    	<dl class="menu_list">
		    		<dt class="menu_title">模块管理</dt>
		    		<div class="nav-item-list" style="display: none;">
		                <a class="nav-item" href="#" onclick="javascript:toAddPage()"> 添加模块</a>
		                <a class="nav-item" href="#" onclick="javascript:toListPage()"> 移除模块</a>
		            </div>
		    	</dl>
		    	<c:forEach var="system" items="${startMonitorSystem}">
			        <dl class="menu_list">
			            <dt class="menu_title">${system.systemName}</dt>
			            <div class="nav-item-list" style="display: none;">
			                <a class="nav-item" href="#" onclick="javascript:listTest('${system.systemId}')"> 接口列表</a>
			                <a class="nav-item" href="#" onclick="javascript:hostsItems('${system.systemId}')">Host管理</a>
			                <a class="nav-item" href="#" onclick="javascript:toContactsList('${system.systemId}')"> 报警组</a>
			            </div>
			        </dl>
		        </c:forEach>
		    </div>
		</div>
		<div class="content-c">
		    <div class="section-center">
				<iframe id="iframe1" frameborder="0" width="100%" height="100%" scrolling="yes"></iframe>
		    </div>
		</div>
		</div>
  </body>
</html>
