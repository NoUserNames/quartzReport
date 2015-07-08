<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">

<title>编辑任务计划</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<link rel="stylesheet" type="text/css" href="./css/styles.css">
<script type="text/javascript" src="./js/jquery-2.1.1.min.js"></script>
<script language="javascript" type="text/javascript"
	src="datepicker/WdatePicker.js"></script>
<script type="text/javascript">
	function checkkey(){                  
		 var params= $("#TRIGGER_NAME").val();
		 //var params = document.getElementById('TRIGGER_NAME').value; 
		$.ajax({
         type: 'get',                                 //请求方式为post方式
         url: 'getTriggerRule.action',                         //请求地址
         dataType: 'text',                          //服务器返回类型为JSON类型
         data:{'params':params},                               //发送到服务器的数据
         success:function(data){
         	document.getElementById('CRON_EXPRESSION').value = data;
         },error:function (data){
         	alert('获取触发规则出错');
         }
      	}); 
	} 
  	function updateCronExpression(){                    
		 var params= $("#CRON_EXPRESSION").val();
		 //var params = document.getElementById('TRIGGER_NAME').value; 
		$.ajax({
         type: 'post',                                 //请求方式为post方式
         url: 'updateCronExpression.action',                         //请求地址
         dataType: 'text',                          //服务器返回类型为JSON类型
         data:{'params':params},                               //发送到服务器的数据
         success:function(data){
         	alert(data);
         },error:function (data){
         	alert('获取触发规则出错');
         }
      	}); 
	}
	function goQuery(){
		document.getElementById("myForm").action="query.action";
		document.getElementById("myForm").submit();
	}
	</script>
<style type="text/css">
</style>
</head>

<body id="aa">
	<!-- onload="checkkey()"  -->
	<center>
		<h1>任务明细</h1>
		<s:form action="updateCronExpression" namespace="/" method="post"
			name="detail" id="myForm">
			<s:iterator value="qrtz_triggers" id="test">
				<table id="mytable" cellspacing="0"
					style="width: 500px; border-style: hidden" border="0">
					<tr>
						<td>工作名称</td>
						<td><s:textfield readonly="true" cssClass="input"
								name="qrtz_triggers.JOB_NAME" value="%{#test.JOB_NAME}"></s:textfield>
							<s:hidden name="qrtz_triggers.JOB_GROUP"
								value="%{#test.JOB_GROUP}"></s:hidden></td>
					</tr>
					<!-- 
  		<tr>
  			<td>工作描述</td>
  			<td>
  				<s:textarea cols="54" rows="5" name="qrtz_triggers.DESCRIPTION" value="%{#test.DESCRIPTION}"></s:textarea>
  			</td>
  		</tr>
  		 -->
					<tr>
						<td>触发器组</td>
						<td><s:textfield readonly="true" cssClass="input"
								name="qrtz_triggers.TRIGGER_GROUP"
								value="%{#test.TRIGGER_GROUP}"></s:textfield></td>
					</tr>
					<tr>
						<td>触发器名称</td>
						<td><s:textfield readonly="true" cssClass="input"
								id="TRIGGER_NAME" name="qrtz_triggers.TRIGGER_NAME"
								value="%{#test.TRIGGER_NAME}"></s:textfield></td>
					</tr>
					<tr>
						<td>触发器表达式</td>
						<td><s:textfield cssClass="input" id="CRON_EXPRESSION"
								name="qrtz_triggers.qrtz_cron_triggers.CRON_EXPRESSION"
								value="%{#test.qrtz_cron_triggers.CRON_EXPRESSION}"></s:textfield>
						</td>
					</tr>
					<tr>
						<td>上次执行时间</td>
						<td><s:textfield cssClass="input" readonly="true"
								name="qrtz_triggers.PREV_FIRE_TIME"
								value="%{#test.PREV_FIRE_TIME}" /></td>
					</tr>
					<tr>
						<td>下次执行时间</td>
						<td><s:textfield cssClass="input" readonly="true"
								name="qrtz_triggers.NEXT_FIRE_TIME"
								value="%{#test.NEXT_FIRE_TIME}" /></td>
					</tr>
					<tr>
						<td>触发器状态</td>
						<td><s:textfield readonly="true" cssClass="input"
								name="qrtz_triggers.TRIGGER_STATE"
								value="%{#test.TRIGGER_STATE}"></s:textfield></td>
					</tr>
				</table>
			</s:iterator>
			<!-- <button onclick="updateCronExpression()">update</button> -->
			<s:submit value="更新" />
			<button onclick="goQuery()">返回</button>
		</s:form>
	</center>
</body>
</html>
