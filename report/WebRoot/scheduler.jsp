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

<title>增加任务计划</title>

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
	
	</script>
<style type="text/css">
</style>
</head>

<body>
	<center>
		<h1>新增任务</h1>
		<s:form action="CreateJob" namespace="/" method="post">

			<table id="mytable" style="width: 500px;">
				<tr>
					<td>Job名称</td>
					<td><s:textfield cssClass="input"
							name="qrtz_triggers.JOB_NAME"></s:textfield></td>
				</tr>
				<tr>
					<td>Job组</td>
					<td><s:textfield cssClass="input"
							name="qrtz_triggers.JOB_GROUP"></s:textfield></td>
				</tr>
				<!-- 
  		<tr>
  			<td>Job描述</td>
  			<td>
  				<s:textarea cssClass="input" cols="54" rows="5" name="qrtz_triggers.DESCRIPTION"></s:textarea>
  			</td>
  		</tr>
  		 -->
				<tr>
					<td>job类名</td>
					<td><s:textfield cssClass="input"
							name="qrtz_triggers.qrtz_job_details.JOB_CLASS_NAME"></s:textfield>
					</td>
				</tr>
				<tr>
					<td>触发器名称</td>
					<td><s:textfield cssClass="input" id="TRIGGER_NAME"
							name="qrtz_triggers.TRIGGER_NAME"></s:textfield></td>
				</tr>
				<tr>
					<td>触发器组</td>
					<td><s:textfield cssClass="input"
							name="qrtz_triggers.TRIGGER_GROUP"></s:textfield></td>
				</tr>
				<tr>
					<td>触发器表达式</td>
					<td><s:textfield cssClass="input" id="CRON_EXPRESSION"
							name="qrtz_triggers.qrtz_cron_triggers.CRON_EXPRESSION"></s:textfield>
					</td>
				</tr>

			</table>

			<s:submit value="增加任务" />
		</s:form>
	</center>
</body>
</html>