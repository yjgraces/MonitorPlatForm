<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
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
    function pause (id,start){
	    if(start==1){
	        UI.confirm({
		            content:'<div class="tip">确认暂停吗</div>'
	        },function(){
	       		jax(id,start);
	        })
	    }else{
	    	jax(id,start);
	    }
    }
    function del (id){
        UI.confirm({
            content:'<div class="tip">确认删除吗</div>'
        },function(){
            $.ajax({
                url:'/monitor_YiDao/scheduler/delItems',
                dataType:'json',
                data:{monitorId:id},
                success:function(res){
                    if(res.code==0){
                        $('#main-list').bootstrapTable('refresh');        
                    }else{
                        UI.alert(res.msg||'请稍后再试！');
                    }    
                },
            })            
        })    
    }
    $(function(){
        var tableData = [
			{ title:"所属模块", field: 'monitorSystem'},		
            { title:"接口名称", field: 'monitorName'},
            { title:"请求方式", field: 'requestAgreement'},
			{ title:"url/uri", 
				formatter:function(value, rows){
					if(rows.requestAgreement.toLowerCase()=="psf"){
						return rows.psfUri;
					}else{
						return rows.httpUrl;
					}                    
                },
			},
			{ title:"当前状态", 
				formatter:function(value, rows){
					if(rows.monitorStart==1){
						return "正在运行";
					}else if(rows.monitorStart==2){
						return "任务已暂停";
					}else{
						return "已关毕";
					}                    
                },			
			},
            { 
                title:"操作",
                formatter:function(value, rows){
                	var stsp = "暂停";
                	if(rows.monitorStart==2){
                		stsp = "恢复";
                	}
                    return "<a href='javascript:;' class='js_view'>查看</a><a href='javascript:;' class='js_pause' id='start-stop'>"+stsp+"</a><a href='/monitor_YiDao/scheduler/editMonitor?monitorId="+rows.monitorId+"' class='view js_edit'>编辑</a><a href='javascript:;' class='js_del'>删除</a>";
                },
                events:{
                    'click .js_view': function (e, value, row, index) {
                        UI.view(this,{
                            box:{width:830},
                            type:"get",
                            url:"/monitor_YiDao/scheduler/selectMonitor",
                            data:{monitorId:row.monitorId}
                        })
                    },
                    'click .js_pause': function (e, value, row, index) {
                        pause(row.monitorId,row.monitorStart);    
                    },
                    'click .js_del': function (e, value, row, index) {
                        del(row.monitorId);    
                    }
                }
            }
        ]
        var datagrid = {
            init:function(){
                var self = this;
                self.loadTable();
            },
            loadTable:function(){
                var self = this;
                $('#main-list').bootstrapTable({
                    url: '/monitor_YiDao/scheduler/listItems?monitorSystem=${monitorSystem}',
                    method : "get",
                    columns:tableData,
                    onlyInfoPagination:true,
                    paginationLoop:false,
                    onLoadSuccess : function(){
                        
                    }
                });
            }
        }
        datagrid.init();
    });
    
    function jax(id,start){
    	 $.ajax({
                url:'/monitor_YiDao/scheduler/pauseRecovery',
                dataType:'json',
                data:{'monitorId':id,'start':start},
                success:function(res){
                    if(res.code==0){
                        $('#main-list').bootstrapTable('refresh'); 
                        $('#start-stop').html("恢复");
                    }else{
                        UI.alert(res.msg||'请稍后再试！');
                    }    
                }
            })
    }
    </script>
</head>
<body>
	<div class="act-items">
		<div class="act-result-c">
			<h3 class="act-result-title">详细接口列表</h3>
			<span class="add-reward"> <a
				href="/monitor_YiDao/scheduler/toAddMonitor?monitorSystem=${monitorSystem}"
				class="btn white-btn">添加接口</a> </span>
		</div>
		<table width="100%" cellspacing="0" cellpadding="0" border="0"
			class="actlist-infor" id="main-list"></table>
	</div>
</body>
</html>
