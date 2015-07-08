<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">

<title>报表任务执行清单</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">

<link rel="stylesheet" type="text/css" href="./css/styles.css">
<link href="themes/default/style.css" rel="stylesheet" type="text/css"
	media="screen" />
<link href="themes/css/core.css" rel="stylesheet" type="text/css"
	media="screen" />
<link href="themes/css/print.css" rel="stylesheet" type="text/css"
	media="print" />
<link href="uploadify/css/uploadify.css" rel="stylesheet"
	type="text/css" media="screen" />
<!--[if IE]>
		<link href="themes/css/ieHack.css" rel="stylesheet" type="text/css" media="screen"/>
		<![endif]-->

<!--[if lte IE 9]>
		<script src="js/speedup.js" type="text/javascript"></script>
		<![endif]-->
<script language="javascript" type="text/javascript"
	src="datepicker/WdatePicker.js"></script>
<script language="javascript" type="text/javascript"
	src="js/jobHandle.js"></script>

</head>

<body>
	<div
		style='border: 0px; padding: 3px; PADDING: 0px; width: 100%; height: 100%; LINE-HEIGHT: 20px; OVERFLOW: auto;'>
		<table class="table" width="100%" layoutH="300" border="1px">

			<thead>
				<tr>
					<th width="150px">任务名称</th>
					<th width="120px">任务组</th>
					<th width="90px">任务状态</th>
					<th width="150px">上次执行时间</th>
					<th width="150px">下次执行时间</th>
					<th>操作</th>
				</tr>
			</thead>

			<tbody>
				<s:iterator value="list" id="test" status="index">
					<s:hidden value="%{#index.index}"></s:hidden>

					<s:form id="jobName%{#index.index}" method="post"
						namespace="/">
						<tr align="center">
							<td><s:property value="#test.JOB_NAME" /> <s:hidden
									name="qrtz_triggers.JOB_NAME" value="%{#test.JOB_NAME}"></s:hidden>
							</td>

							<td><s:property value="#test.JOB_GROUP" /> <s:hidden
									name="qrtz_triggers.JOB_GROUP" value="%{#test.JOB_GROUP}"></s:hidden>
							</td>

							<td
								<s:if test="#test.TRIGGER_STATE == 'PAUSED'">bgcolor="yellow"</s:if>>
								<s:property value="#test.TRIGGER_STATE" />
							</td>
							<td><s:property value="%{#test.PREV_FIRE_TIME}" /></td>
							<td><s:property value="%{#test.NEXT_FIRE_TIME}" /></td>
							<td>
								<button onclick="doQuery('jobName'+${index.index})">查看</button>
								<button onclick="doPause('jobName'+${index.index})">暂停</button>
								<button onclick="doDelete('jobName'+${index.index})">删除</button>
								<button onclick="resumeJobNow('jobName'+${index.index})">恢复</button>
								<button onclick="runJobNow('jobName'+${index.index})">立即执行</button>
								<button onclick="interrptJobNow('jobName'+${index.index})">立即终止</button>
							</td>
						</tr>
					</s:form>
				</s:iterator>
			</tbody>
		</table>
	</div>
</body>
</html>