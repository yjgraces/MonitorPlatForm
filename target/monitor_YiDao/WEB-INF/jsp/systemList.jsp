<%@page import="java.net.URLEncoder"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
<title>移除模块</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/reset.css"/>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/top.css"/>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/left.css"/>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/dialog.css"/>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/manage.css"/>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/js/jquery.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/js/dialog.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/js/base.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/lib/jquery-validate/jquery.validate.min.js"></script>
</head>

<script>

			function delSystem(id) {
				UI.confirm({
					content:'<div class="tip">确认删除吗</div>'
			        },function(){
			        	del(id);
			        });
			}
			
			function del(id){
						$.get("/monitor_YiDao/system/delSystem", 
							{'systemId':id},
							 function(data) {
								if (data == "success") {
								var dia = UI.alert("移除成功");
								dia.cont.find('.js_ok').click(function(){
									parent.location.reload();
								});
								} else {
									UI.alert("错误   " + data);
								}
						});
			}

</script>
</head>
<body>
	<div class="act-items">
		<div class="act-result-c">
			<h3 class="act-result-title">模块列表</h3>
		</div>
		<table width="100%" cellspacing="0" cellpadding="0" border="0"
			class="actlist-infor" id="http-list">
				<tr>
					<th style="text-align: center">模块</th>
					<th style="text-align: center">描述</th>
					<th style="text-align: center">操作</th>
				</tr>
			<c:forEach var="monitorSystem" items="${monitorSystem}" >
				<tr>
					<td>${monitorSystem.systemId }</td>
					<td>${monitorSystem.systemName}</td>
					<td>
						<a href='#' onclick="javascript:delSystem('${monitorSystem.systemId}')">移除</a>
					</td>
				</tr>
			</c:forEach>
			<tr>
				<td colspan="2">
				</td>
				<td style="text-align: center;">
					Showing ${monitorSystemCount } rows
				</td>
			</tr>
		</table>
	</div>
</body>
</html>
