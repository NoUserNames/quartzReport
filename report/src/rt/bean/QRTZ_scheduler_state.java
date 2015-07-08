package rt.bean;

public class QRTZ_scheduler_state {
	private String SCHED_NAME;
	private String INSTANCE_NAME;
	private int LAST_CHECKIN_TIME;
	private int CHECKIN_INTERVAL;
	public String getSCHED_NAME() {
		return SCHED_NAME;
	}
	public void setSCHED_NAME(String sched_name) {
		SCHED_NAME = sched_name;
	}
	public String getINSTANCE_NAME() {
		return INSTANCE_NAME;
	}
	public void setINSTANCE_NAME(String instance_name) {
		INSTANCE_NAME = instance_name;
	}
	public int getLAST_CHECKIN_TIME() {
		return LAST_CHECKIN_TIME;
	}
	public void setLAST_CHECKIN_TIME(int last_checkin_time) {
		LAST_CHECKIN_TIME = last_checkin_time;
	}
	public int getCHECKIN_INTERVAL() {
		return CHECKIN_INTERVAL;
	}
	public void setCHECKIN_INTERVAL(int checkin_interval) {
		CHECKIN_INTERVAL = checkin_interval;
	}
}
