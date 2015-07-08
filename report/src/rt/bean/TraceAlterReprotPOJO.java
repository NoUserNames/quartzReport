package rt.bean;

public class TraceAlterReprotPOJO {
	private String processName;
	private String serialNumber;
	private String userName;
	private String lastTime;
	private String pdlineName;
	private String cartonNO;
	private String currentStatus;
	private String timeZone;
	public String getTimeZone() {
		return timeZone;
	}
	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}
	public String getCurrentStatus() {
		return currentStatus;
	}
	public void setCurrentStatus(String currentStatus) {
		this.currentStatus = currentStatus;
	}
	public String getProcessName() {
		return processName;
	}
	public void setProcessName(String processName) {
		this.processName = processName;
	}
	public String getSerialNumber() {
		return serialNumber;
	}
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getLastTime() {
		return lastTime;
	}
	public void setLastTime(String lastTime) {
		this.lastTime = lastTime;
	}
	public String getPdlineName() {
		return pdlineName;
	}
	public void setPdlineName(String pdlineName) {
		this.pdlineName = pdlineName;
	}
	public String getCartonNO() {
		return cartonNO;
	}
	public void setCartonNO(String cartonNO) {
		this.cartonNO = cartonNO;
	}
}
