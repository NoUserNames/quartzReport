package rt.bean;

public class QRTZ_locks {
	private String SCHED_NAME;
	private String LOCK_NAME;
	public String getSCHED_NAME() {
		return SCHED_NAME;
	}
	public void setSCHED_NAME(String sched_name) {
		SCHED_NAME = sched_name;
	}
	public String getLOCK_NAME() {
		return LOCK_NAME;
	}
	public void setLOCK_NAME(String lock_name) {
		LOCK_NAME = lock_name;
	}
}
