<%@page import="java.net.URLEncoder"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
<title>Host信息</title>

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

			function delHost(id,system) {
				UI.confirm({
					content:'<div class="tip">确认删除吗</div>'
			        },function(){
			        	del(id,system);
			        });
			}
			
			function del(id,system){
						$.get("/monitor_YiDao/hosts/delHostItem", 
							{'hostId':id},
							 function(data) {
								if (data == "success") {
									UI.alert("删除成功");
									location.reload();
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
			<h3 class="act-result-title">Host列表</h3>
			<span class="add-reward"> <a
				href="/monitor_YiDao/scheduler/listTest?monitorSystem=${monitorSystem }"
				class="btn white-btn">返回列表</a> </span>
			<span class="add-reward"> <a
				href="/monitor_YiDao/hosts/toAddHostItems?hostSystem=${monitorSystem }"
				class="btn white-btn">添加host</a> </span>

		</div>
		<c:if test="${psfHostCount > 0 }">	
		<table width="100%" cellspacing="0" cellpadding="0" border="0"
			class="actlist-infor" id="psf-list">
				<tr>
					<th style="text-align: center">所属系统</th>
					<th style="text-align: center">PSF-Hosts</th>
					<th style="text-align: center">状态</th>
					<th style="text-align: center">操作</th>
				</tr>
			<c:forEach var="psfHostArray" items="${psfHostArray}" >
				<tr>
					<td>${psfHostArray.monitorSystem}</td>
					<td>${psfHostArray.monitorHost}</td>
					<td>
					    <c:if test="${psfHostArray.hostStart==1}">已启用</c:if>
                		<c:if test="${psfHostArray.hostStart==0}">未启用</c:if>
					</td>
					<td>
						<a href='/monitor_YiDao/hosts/toUpdateHostItems?hostId=${psfHostArray.hostId}'>编辑</a>
						<a href='#' onclick="javascript:delHost('${psfHostArray.hostId}','${psfHostArray.monitorSystem }')">删除</a>
					</td>
				</tr>
			</c:forEach>
			<tr>
				<td colspan="3">
				</td>
				<td style="text-align: center;">
					Showing ${psfHostCount } rows
				</td>
			</tr>
		</table>
		</c:if>
		<c:if test="${httpHostCount > 0 }">	
		<table width="100%" cellspacing="0" cellpadding="0" border="0"
			class="actlist-infor" id="http-list">
				<tr>
					<th style="text-align: center">所属系统</th>
					<th style="text-align: center">Http-Hosts</th>
					<th style="text-align: center">状态</th>
					<th style="text-align: center">操作</th>
				</tr>
			<c:forEach var="httpHostArray" items="${httpHostArray}" >
				<tr>
					<td>${httpHostArray.monitorSystem }</td>
					<td>${httpHostArray.monitorHost}</td>
					<td>
					    <c:if test="${httpHostArray.hostStart==1}">已启用</c:if>
                		<c:if test="${httpHostArray.hostStart==0}">未启用</c:if>
					</td>
					<td>
						<a href='/monitor_YiDao/hosts/toUpdateHostItems?hostId=${httpHostArray.hostId}'>编辑</a>
						<a href='#' onclick="javascript:delHost('${httpHostArray.hostId}','${httpHostArray.monitorSystem }')">删除</a>
					</td>
				</tr>
			</c:forEach>
			<tr>
				<td colspan="3">
				</td>
				<td style="text-align: center;">
					Showing ${httpHostCount } rows
				</td>
			</tr>
		</table>
		</c:if>
	</div>
</body>
</html>
