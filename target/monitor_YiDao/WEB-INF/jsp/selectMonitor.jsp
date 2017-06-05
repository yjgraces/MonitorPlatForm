<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    
    <title>监控项相信信息</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

  </head>
  
  <body>
  <div class="act-detail">
    <div class="act-detail-con">
    <h3>监控项基本信息</h3>
        <table class="detail-items" width="100%" cellspacing="0" cellpadding="0" border="0">
            <colgroup>
                <col class="detail-col-1">
                <col class="detail-col-1">
                <col class="detail-col-1">
                <col class="detail-col-1">
            </colgroup>
            <tr>
                <td>监控项名称</td>
                <td>${monitorItems.monitorName}</td>
                <td>所属模块</td>
                <td>${monitorItems.monitorSystem}</td>
            </tr>
            <tr>
                <td>Crontab定时执行</td>
                <td>${monitorItems.monitorCrontab}</td>
                <td>当前状态</td>
                <td>
                <c:if test="${monitorItems.monitorStart==1}">监控已启动</c:if>
                <c:if test="${monitorItems.monitorStart==0}">监控已关闭</c:if>
                </td>
            </tr>
            <tr>
                <td>请求类型</td>
                <td>${monitorItems.requestAgreement}</td>
                <td>超时设定</td>
                <td>${monitorItems.monitorTimeout} ms</td>
            </tr>
            <c:choose>
			   <c:when test="${monitorItems.requestAgreement=='psf' || monitorItems.requestAgreement=='PSF'}">  
		            <tr>
		                <td>psf-uri</td>
		                <td colspan="3">${monitorItems.psfUri}</td>
		            </tr>
		            <tr>
		                <td>psf-data</td>
		                <td colspan="3">${monitorItems.psfData}</td>
		            </tr>
		            <tr>
		                <td>psf-service-type</td>
		                <td>${monitorItems.psfServiceType}</td>
		                <td>结果断言</td>
		                <td>${monitorItems.monitorAssert}</td>
		            </tr>    
			   </c:when>
			   <c:otherwise> 
		            <tr>
		                <td>结果断言</td>
		                <td colspan="3">${monitorItems.monitorAssert}</td>
		            </tr>
		            <tr>
		                <td>http-url</td>
		                <td colspan="3">${monitorItems.httpUrl}</td>
		            </tr>
		            <tr>
		                <td>http-param</td>
		                <td colspan="3">${monitorItems.httpParam}</td>
		            </tr>
			   </c:otherwise>
			</c:choose>
            <tr>
                <td>监控项创建时间</td>
                <td>${monitorItemsCreateTime}</td>
                <td>最后修改时间</td>
                <td>${monitorItemsLastUpdateTime}</td>
            </tr>
        </table>
      <c:if test="${monitorException != 0}">
		     <h3>最近10次报警记录</h3>
      </c:if>
      <c:forEach var="monitorException" items="${monitorExceptionByMonitorId}">
        <table class="detail-items" width="100%" cellspacing="0" cellpadding="0" border="0">
            <colgroup>
                <col class="detail-col-1">
                <col class="detail-col-2">
                <col class="detail-col-1">
                <col class="detail-col-1">
            </colgroup>
            <tr>
                <td>异常类型</td>
                <td>${monitorException.exceptionType }</td>
                <td>异常名称</td>
                <td>${monitorException.exceptionName }</td>
            </tr>
            <tr>
                <td>异常所属host</td>
                <td>${monitorException.exceptionHost }</td>
                <td>接口耗时</td>
                <td>${monitorException.exceptionTimeConsuming }</td>
            </tr>
            <tr>
                <td>接口返回</td>
                <td colspan="3">${monitorException.exceptionResult }</td>
            </tr>
            <tr>
                <td>报警时间戳</td>
                <td colspan="3">${monitorException.exceptionTime }</td>
            </tr>
        </table>
       </c:forEach>
    </div>
</div>
  </body>
</html>
