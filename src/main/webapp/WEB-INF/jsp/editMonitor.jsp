<%@page import="java.net.URLEncoder"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
<title>编辑监控项</title>

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
	$(function() {
	
		var a = 0;
		/*
		 ***  表单验证
		 */
		$('#submitForm').validate(
				{
					errorElement : "span",
					errorPlacement : function(error, element) {
						error.appendTo(element.closest('tr').find('.err-item')
								.html(''));
					},
					rules : {
						address : 'required',
						requestAgreement : 'required',
						url : 'required',
						params : 'required',
						psf : {
							required : {
								depends : function(element) {
									return $("#requestAgreement").val() == 1;
								}
							}
						}
					},
					messages : {
						monitorName : '请填写',
						monitorCrontab : '请填写',
						monitorStart : '请选择',
						monitorTimeout : '请填写',
						monitorAssert : '请填写'
					},
					submitHandler : function(form) {
						if(a==1){
							$.get("/monitor_YiDao/scheduler/debugging", 
								$('#submitForm').serialize(),
								function(json){
									if(json.code==0 || json.code==1){
										$("#result").html(json.result);
										$("#time").html(json.time +" ms");
									}else{
										$("#result").html(json.result);
										$("#time").html(json.time +" ms");
									}
								});
						}
						if(a==2){
							$.get("/monitor_YiDao/scheduler/updateItems", 
								$('#submitForm').serialize(),
								function(json){
									if(json.code==0){
										UI.alert("保存成功");
									}else{
										UI.alert("错误  "+json.msg);
									}
								});
						}
					}
				});

		$('.js_debug').click(function() {
			a=1;
			$('#submitForm').submit();
		});

		$('.js_submit').click(function() {
			a=2;			
			$('#submitForm').submit();
		});
		$('#requestAgreement').change(function() {
			var s = $(this).val();
			if(s=="psf"){
				$('.psf_text').show();
				$('.http_text').hide();
			}else{
				$('.http_text').show();
				$('.psf_text').hide();
			}
		});
	});
	
</script>
</head>
<body>
				<form action="" id="submitForm">
					<input type="hidden" name="monitorId" value="${monitorItems.monitorId}">
					<table width="100%" cellspacing="0" cellpadding="0" border="0"
						class="set-infor">
						<colgroup>
							<col class="col-1">
							<col>
						</colgroup>
						<tr>
							<th colspan="2" class="reward-title">
								<h3>
									<span class="city-list">编辑接口信息</span>
								</h3></th>
						</tr>
						
						<tr>
							<td><span class="name-act">监控项名称</span></td>
							<td><span class="act-name-i"><input type="text"
									name="monitorName" value="${monitorItems.monitorName }">
							</span> <span class="err-item"></span></td>
						</tr>
						<tr>
							<td><span class="name-act">所属系统模块</span></td>
							<td><span class="act-name-i">
							${monitorItems.monitorSystem }
							<input type="hidden" name="monitorSystem" value="${monitorItems.monitorSystem }">
							</span> <span class="err-item"></span></td>
						</tr>
						<tr>
							<td><span class="name-act">Crontab定时执行</span></td>
							<td><span class="act-name-i"><input type="text"
									name="monitorCrontab" value="${monitorItems.monitorCrontab }">
							</span> <span class="err-item"></span></td>
						</tr>
						<tr>
							<td><span class="name-act">当前状态</span></td>
							<td>
						<c:choose>				
						   <c:when test="${monitorItems.monitorStart==0}">  
						   		<input type="radio" name="monitorStart" value="1">开启
								<input type="radio" name="monitorStart" value="0" checked="checked">关闭
						   </c:when>
						   <c:otherwise> 
						   		<input type="radio" name="monitorStart" value="1" checked="checked">开启
								<input type="radio" name="monitorStart" value="0">关闭
						   </c:otherwise>
						</c:choose>
							<span class="err-item"></span></td>
						</tr>
						<tr>
							<td><span class="name-act">超时设定（ms）</span></td>
							<td><span class="act-name-i"><input type="text"
									name="monitorTimeout" value="${monitorItems.monitorTimeout }">
							</span> <span class="err-item"></span></td>
						</tr>
						<tr>
							<td><span class="name-act">结果断言</span></td>
							<td><span class="act-name-i">
							<textarea name="monitorAssert" >${monitorItems.monitorAssert }</textarea>
							</span> <span class="err-item"></span></td>
						</tr>
						
						<tr>
							<td><span class="name-act">请求类型</span></td>
							<td>
								<div class="set-argument">
									<span class="argu-list"> <b> <select name="requestAgreement"
											id="requestAgreement" class="check-option">
												<option value="psf" <c:if test="${monitorItems.requestAgreement=='psf' || monitorItems.requestAgreement=='PSF'}">selected = "selected"</c:if>>psf</option>
												<option value="http-get" <c:if test="${monitorItems.requestAgreement=='http-get' || monitorItems.requestAgreement=='http-GET'}">selected = "selected"</c:if>>http-get</option>
												<option value="http-post" <c:if test="${monitorItems.requestAgreement=='http-post' || monitorItems.requestAgreement=='http-POST'}">selected = "selected"</c:if>>http-post</option>
												<option value="http-post-body" <c:if test="${monitorItems.requestAgreement=='http-post-body'}">selected = "selected"</c:if>>http-post-body</option>
										</select> </b> </span>
								</div></td>
						</tr>
  						<tr class="psf_text" <c:if test="${monitorItems.requestAgreement!='psf' && monitorItems.requestAgreement!='PSF'}">style="display:none"</c:if> >
							<td><span class="name-act">psf-service-type</span></td>
							<td><span class="act-name-i"><input type="text"
									name="psfServiceType" value="${monitorItems.psfServiceType}">
							</span> <span class="err-item"></span></td>
						</tr>
						<tr class="psf_text"  <c:if test="${monitorItems.requestAgreement!='psf' && monitorItems.requestAgreement!='PSF'}">style="display:none"</c:if> >
							<td><span class="name-act">psf-uri</span></td>
							<td><span class="act-name-i">
							 <input type="text"	name="psfUri" value="${monitorItems.psfUri }">
							</span> <span class="err-item"></span></td>
						</tr>
						<tr class="psf_text"  <c:if test="${monitorItems.requestAgreement!='psf' && monitorItems.requestAgreement!='PSF'}">style="display:none"</c:if> >
							<td><span class="name-act">psf-uri-param (get格式参数)</span></td>
							<td><span class="act-name-i"> 
							<!-- 
							<input type="text" name="psfUriParam"	value=${monitorItems.psfUriParam}> 
							 -->
							 <textarea name="psfUriParam"  style="height: 80px">${monitorItems.psfUriParam}</textarea>
							</span> <span class="err-item"></span>
							</td>
						</tr>
						<tr class="psf_text"  <c:if test="${monitorItems.requestAgreement!='psf' && monitorItems.requestAgreement!='PSF'}">style="display:none"</c:if> >
							<td><span class="name-act"> psf-data (json格式参数)</span></td>
							<td><span class="act-name-i">
							<textarea name="psfData"  style="height: 80px">${monitorItems.psfData}</textarea>
							<!-- 
							  <input type="text" name="psfData"  value=${monitorItems.psfData}> 
							 -->
							</span> <span class="err-item"></span>
							</td>
						</tr>
						<tr class="http_text" <c:if test="${monitorItems.requestAgreement=='psf' || monitorItems.requestAgreement=='PSF'}">style="display:none"</c:if> >
							<td><span class="name-act">http-url</span></td>
							<td><span class="act-name-i"><input type="text"
									name="httpUrl" value="${monitorItems.httpUrl }">
							</span> <span class="err-item"></span></td>
						</tr>
						<tr class="http_text" <c:if test="${monitorItems.requestAgreement=='psf' || monitorItems.requestAgreement=='PSF'}">style="display:none"</c:if> >
							<td><span class="name-act">http-param</span></td>
							<td><span class="act-name-i">
							<!-- 
							<input type="text"
									name="httpParam" value="${monitorItems.httpParam }">
							 -->
							<textarea name="httpParam" style="height: 80px">${monitorItems.httpParam}</textarea>
							</span> <span class="err-item"></span></td>
						</tr>
						<tr class="http_text" <c:if test="${monitorItems.requestAgreement=='psf' || monitorItems.requestAgreement=='PSF'}">style="display:none"</c:if> >
							<td><span class="name-act">http调试接口 host</span></td>
							<td><span class="argu-list"> <b> <select name="testHost" id="testHost" class="check-option" style="width: 150px">
								<c:forEach var="host" items="${MonitorHosts}">
									<option value="${host}">${host}</option>
								</c:forEach>
							</select> </b> </span></td>
						</tr>
					</table>
				</form>
			<div class="submit-btn">
				<a href="javascript:;" class="btn red-btn disable-btn js_submit">保存编辑</a> <a
					href="javascript:;" class="btn red-btn js_debug">接口调试</a>
			</div>
				<div class="section-center" style="width:100%;overflow: hidden;">
				<table width="100%" cellspacing="0" cellpadding="0" border="0" class="set-infor">
					<tr>
						<th colspan="2" class="reward-title">
							<h3>
								<span class="city-list">接口返回信息
								</span>
							</h3>
						</th>
					</tr>
					<tr>
						<th class="rewardname"><div class="name">接口耗时</div></th>
						<td><span id="time"></span></td>
					</tr>
					<tr>
						<th class="rewardname"><div class="name">返回结果</div></th>
						<td><span id="result" class="result-area"></span></td>
					</tr>
				</table>
			</div>
</body>
</html>
