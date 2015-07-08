package rt.bean;

/**
 * @author Qiang1_Zhang
 * 设备利用率实体类
 */
public class DeviceAvailability {
	private int terminalID;
	private String terminalName;
	private String lasttime;
	public int getTerminalID() {
		return terminalID;
	}
	public void setTerminalID(int terminalID) {
		this.terminalID = terminalID;
	}
	public String getTerminalName() {
		return terminalName;
	}
	public void setTerminalName(String terminalName) {
		this.terminalName = terminalName;
	}
	public String getLasttime() {
		return lasttime;
	}
	public void setLasttime(String lasttime) {
		this.lasttime = lasttime;
	}
}