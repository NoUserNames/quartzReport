<%@ page language="java" pageEncoding="UTF-8"%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>

		<title>Tracebility-报表管理</title>

		<link href="themes/default/style.css" rel="stylesheet" type="text/css" media="screen"/>
		<link href="themes/css/core.css" rel="stylesheet" type="text/css" media="screen"/>
		<link href="themes/css/print.css" rel="stylesheet" type="text/css" media="print"/>
		<link href="uploadify/css/uploadify.css" rel="stylesheet" type="text/css" media="screen"/>
		<!--
		<link href="themes/css/ieHack.css" rel="stylesheet" type="text/css" media="screen"/>
		<//-->
		
		<!--
		<script src="js/speedup.js" type="text/javascript"></script>
		<//-->
		
		<script src="js/jquery-1.7.2.js" type="text/javascript"></script>
		<script src="js/jquery.cookie.js" type="text/javascript"></script>
		<script src="js/jquery.validate.js" type="text/javascript"></script>
		<script src="js/jquery.bgiframe.js" type="text/javascript"></script>
		<script src="xheditor/xheditor-1.2.1.min.js" type="text/javascript"></script>
		<script src="xheditor/xheditor_lang/zh-cn.js" type="text/javascript"></script>
		<script src="uploadify/scripts/jquery.uploadify.js" type="text/javascript"></script>
		
		<script type="text/javascript" src="chart/raphael.js"></script>
		<script type="text/javascript" src="chart/g.raphael.js"></script>
		<script type="text/javascript" src="chart/g.bar.js"></script>
		<script type="text/javascript" src="chart/g.line.js"></script>
		<script type="text/javascript" src="chart/g.pie.js"></script>
		<script type="text/javascript" src="chart/g.dot.js"></script>
		
		<script src="bin/dwz.min.js" type="text/javascript"></script>
		<script src="js/jobHandle.js" type="text/javascript"/></script>
		<script src="js/dwz.regional.zh.js" type="text/javascript"></script>
		
		
		<script type="text/javascript">
		$(function(){
			DWZ.init("dwz.frag.xml", {
				//loginUrl:"login_dialog.html", loginTitle:"登录",
				loginUrl:"login.jsp",
				statusCode:{ok:200, error:300, timeout:301},
				pageInfo:{pageNum:"pageNum", numPerPage:"numPerPage", orderField:"orderField", orderDirection:"orderDirection"},
				keys: {statusCode:"statusCode", message:"message"},
				ui:{hideMode:'offsets'},
				debug:false,
				callback:function(){
					initEnv();
					$("#themeList").theme({themeBase:"themes"}); // themeBase 相对于index页面的主题base路径
				}
			});
		});
		
		</script>
	</head>

<body scroll="no">
	<!-- onDragStart="return false" onload="test2('azure');"
		oncontextmenu="return false" onSelectStart="return false" -->
	<div id="layout">
		<div id="header">
			<div class="headerNav">
				<a class="logo">标志</a>
				<ul class="nav">
					<li><a href="changepwd.html" target="dialog">设置</a></li>
					<li><a href="#">退出</a></li>
				</ul>
				<ul class="themeList" id="themeList">
					<!--<li theme="default">
							<div>
								蓝色
							</div>
						</li>
						-->
					<li theme="green">
						<div>绿色</div>
					</li>
					<!--<li theme="red"><div>红色</div></li> -->
					<li theme="purple">
						<div>紫色</div>
					</li>
					<li theme="silver">
						<div>银色</div>
					</li>
					<li theme="azure">
						<div id='azure' class="selected" onclick="">天蓝</div>
					</li>
				</ul>
			</div>
		</div>
		<div id="leftside">
			<div id="sidebar_s">
				<div class="collapse">
					<div class="toggleCollapse">
						<div></div>
					</div>
				</div>
			</div>
			<div id="sidebar">
				<div class="toggleCollapse">
					<h2>主菜单</h2>
					<div>收缩</div>
				</div>

				<div class="accordion" fillSpace="sidebar">
					<div class="accordionHeader">
						<h2>
							<span>Folder</span>报表以及任务
						</h2>
					</div>
					<div class="accordionContent">
						<ul class="tree treeFolder">
							<li><a href="tabsPage.html">报表</a> <!-- target="navTab" target不设置就是无链接标签-->
								<ul>
									<li><a href="query" target="navTab" rel="page"
										external="true">查看所有报表任务</a></li>
									<li><a href="scheduler.jsp" target="navTab"
										external="true">增加新任务</a></li>

								</ul></li>

						</ul>
					</div>
					<div class="accordionHeader">
						<h2>
							<span>Folder</span>TSG
						</h2>
					</div>
					<div class="accordionContent">
						<ul class="tree treeFolder">
							<li><a href="tabsPage.html">设备使用率</a>
								<ul>
									<li><a href="lastScanTime" target="navTab" rel="page2"
										external="false">最后扫描时间</a></li>
									<li><a href="DeviceAvailability.jsp" target="navTab"
										rel="page3" external="false">设备扫描量</a></li>

								</ul></li>
						</ul>
					</div>
					<div class="accordionHeader">
						<h2>
							<span>Folder</span>系统管理
						</h2>
					</div>
					<div class="accordionContent">
						<ul class="tree">
							<li><a href="newPage1.html" target="dialog" rel="dlg_page">菜单管理</a>
							</li>
							<li><a href="newPage1.html" target="dialog" rel="dlg_page2">列表</a>
							</li>
						</ul>
					</div>
				</div>
			</div>
		</div>
		<div id="container">
			<div id="navTab" class="tabsPage">
				<div class="tabsPageHeader">
					<div class="tabsPageHeaderContent">
						<!-- 显示左右控制时添加 class="tabsPageHeaderMargin" -->
						<ul class="navTab-tab">
							<li tabid="main" class="main"><a href="javascript:;"><span><span
										class="home_icon">我的主页</span> </span> </a></li>
						</ul>
					</div>
					<div class="tabsLeft">left</div>
					<!-- 禁用只需要添加一个样式 class="tabsLeft tabsLeftDisabled" -->
					<div class="tabsRight">right</div>
					<!-- 禁用只需要添加一个样式 class="tabsRight tabsRightDisabled" -->
					<div class="tabsMore">more</div>
				</div>
				<ul class="tabsMoreList">
					<li><a href="javascript:;">我的主页</a></li>
				</ul>
				<div class="navTab-panel tabsPageContent layoutBox">
					<div class="page unitBox">
						<div class="accountInfo">
							<div class="alertInfo">
								<p>
									<span>GP系統</span>
								</p>
								<p>
									<a href="http://ehr.casetekcorp.com:8092/" target="_blank"
										style="line-height: 19px"><span>EHR系統</span> </a>
								</p>
							</div>
							<div class="right">
								<p style="color: red">
									SPC系統 <a href="http://spc.casetekcorp.com/" target="_blank">http://spc.casetekcorp.com/</a>
								</p>
							</div>
							<p>
								<span>凱勝控股有限公司</span>
							</p>
							<p>
								綜合管理系統: <a href="http://oa.casetekcorp.com/" target="_blank">http://oa.casetekcorp.com/</a>
							</p>
						</div>

						<div class="pageFormContent" layoutH="80">

							<br />
							<div class="divider"></div>
							<h2>常见问题及解决办法:</h2>
							<br /> 常见问题及解决 <br />
							<br />
							<div class="divider"></div>
							<br />
							<h2>
								问题反馈( <span style="color: red;">使用中有任何问题请联系MIS</span>):
							</h2>
							<br /> 问题反馈

						</div>

					</div>
				</div>
			</div>

		</div>

		<div id="footer">Copyright &copy; 2014</div>
	</div>
</body>
</html>