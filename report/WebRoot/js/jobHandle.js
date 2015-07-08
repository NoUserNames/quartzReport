
function doPause(formName) {
	document.getElementById(formName).action = "doPauseJob.action";
	document.getElementById(formName).submit();
}
function doQuery(formName) {
	document.getElementById(formName).action = "FindJobByName.action";
	document.getElementById(formName).submit();
}
function resumeJobNow(formName) {
	document.getElementById(formName).action = "resumeJobNow.action";
	document.getElementById(formName).submit();
}
function doDelete(formName) {
	document.getElementById(formName).action = "deleteJob.action";
	document.getElementById(formName).submit();
}
function runJobNow(formName) {
	document.getElementById(formName).action = "runJobNow.action";
	document.getElementById(formName).submit();
}
function interrptJobNow(formName) {
	document.getElementById(formName).action = "interruptJob.action";
	document.getElementById(formName).submit();
}

function test2(name) {
	if (document.all) {
		document.getElementById(name).click();
	} else {
		var evt = document.createEvent("MouseEvents");
		evt.initEvent("click", true, true);
		document.getElementById(name).dispatchEvent(evt);
	}
}

