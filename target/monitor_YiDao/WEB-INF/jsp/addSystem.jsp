<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html">
<html>
  <head>
    
    <title>添加模块</title>
    
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
  
  <script type="text/javascript">

			function subForm() {
				$.get("/monitor_YiDao/system/addSystem", 
					$('#submitForm').serialize(), function(data) {
						if (data == "success") {
							var dia = UI.alert("添加成功");
							dia.cont.find('.js_ok').click(function(){
								parent.location.reload();
							});
							
						} else {
							UI.alert("错误   " + data);
						}
				});
			}
		</script>
  <body>
    				<form action="" id="submitForm">
					<table width="100%" cellspacing="0" cellpadding="0" border="0"
						class="set-infor">
						<colgroup>
							<col class="col-1">
							<col>
						</colgroup>
						<tr>
							<th colspan="2" class="reward-title">
								<h3>
									<span class="city-list">添加模块</span>
								</h3></th>
						</tr>
						
						<tr>
							<td><span class="name-act">模块</span></td>
							<td style="vertical-align:middle;"><span class="act-name-i"><input type="text"
									name="systemId">
							</span> </td>
						</tr>
						<tr>
							<td><span class="name-act">名称</span></td>
							<td style="vertical-align:middle;"><span class="act-name-i"><input type="text"
									name="systemName">
							</span> </td>
						</tr>
					<!-- 
						<tr>
							<td><span class="name-act">状态</span></td>
							<td style="vertical-align:middle;">
						   	<input type="radio" name="systemStart" value="1" checked="checked"><span>启用</span>
							<input type="radio" name="systemStart" value="0"><span>不启用</span>
							</td>
						</tr>
					-->
					</table>
					<input type="hidden" name="systemStart" value="1">
				</form>
			<div class="submit-btn"  style="text-align:center;">
				<a href="javascript:;" class="btn red-btn disable-btn js_submit" onclick="javascript:subForm()">保存添加</a>
			</div>
  </body>
</html>
