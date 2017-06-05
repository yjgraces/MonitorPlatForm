<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html">
<html>
  <head>
    
    <title>添加页面</title>
    
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
				$.get("/monitor_YiDao/hosts/addHosts", 
					$('#submitForm').serialize(), function(data) {
						if (data == "success") {
							UI.alert("添加成功");
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
									<span class="city-list">添加Host信息</span>
								</h3></th>
						</tr>
						
						<tr>
							<td><span class="name-act">Host所属模块</span></td>
							<td>
							 <input type="hidden" name="monitorSystem" value="${hostSystem}">
							 ${hostSystem}
							</td>
						</tr>
						<tr>
							<td><span class="name-act">Host</span></td>
							<td style="vertical-align:middle;"><span class="act-name-i"><input type="text"
									name="monitorHost">
							</span> </td>
						</tr>
						<tr>
							<td><span class="name-act">host类型</span></td>
							<td style="vertical-align:middle;">
								<span class="argu-list"> <b> <select 
								name="serverType" class="check-option">
										<option value="psf">psf</option>
										<option value="http">http</option>
								</select> </b> </span>
							</td>
						</tr>
						<tr>
							<td><span class="name-act">状态</span></td>
							<td style="vertical-align:middle;">
						   	<input type="radio" name="hostStart" value="1" checked="checked"><span>启用</span>
							<input type="radio" name="hostStart" value="0"><span>不启用</span>
							</td>
						</tr>
					</table>
				</form>
			<div class="submit-btn"  style="text-align:center;">
				<a href="javascript:;" class="btn red-btn disable-btn js_submit" onclick="javascript:subForm()">保存添加</a>
			</div>
  </body>
</html>
