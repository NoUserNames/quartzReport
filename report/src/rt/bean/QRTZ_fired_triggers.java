package rt.bean;

public class QRTZ_fired_triggers {
	private String SCHED_NAME;
	private String ENTRY_ID;
	private String TRIGGER_NAME;
	private String TRIGGER_GROUP;
	private String INSTANCE_NAME;
	private int FIRED_TIME;
	private int SCHED_TIME;
	private int PRIORITY;
	private String STATE;
	private String JOB_NAME;
	private String JOB_GROUP;
	private String IS_NONCONCURRENT;
	private String REQUESTS_RECOVERY;
	public int getFIRED_TIME() {
		return FIRED_TIME;
	}
	public void setFIRED_TIME(int fired_time) {
		FIRED_TIME = fired_time;
	}
	public int getSCHED_TIME() {
		return SCHED_TIME;
	}
	public void setSCHED_TIME(int sched_time) {
		SCHED_TIME = sched_time;
	}
	public int getPRIORITY() {
		return PRIORITY;
	}
	public void setPRIORITY(int priority) {
		PRIORITY = priority;
	}
	public String getSTATE() {
		return STATE;
	}
	public void setSTATE(String state) {
		STATE = state;
	}
	public String getJOB_NAME() {
		return JOB_NAME;
	}
	public void setJOB_NAME(String job_name) {
		JOB_NAME = job_name;
	}
	public String getJOB_GROUP() {
		return JOB_GROUP;
	}
	public void setJOB_GROUP(String job_group) {
		JOB_GROUP = job_group;
	}
	public String getIS_NONCONCURRENT() {
		return IS_NONCONCURRENT;
	}
	public void setIS_NONCONCURRENT(String is_nonconcurrent) {
		IS_NONCONCURRENT = is_nonconcurrent;
	}
	public String getREQUESTS_RECOVERY() {
		return REQUESTS_RECOVERY;
	}
	public void setREQUESTS_RECOVERY(String requests_recovery) {
		REQUESTS_RECOVERY = requests_recovery;
	}
	public String getSCHED_NAME() {
		return SCHED_NAME;
	}
	public void setSCHED_NAME(String sched_name) {
		SCHED_NAME = sched_name;
	}
	public String getENTRY_ID() {
		return ENTRY_ID;
	}
	public void setENTRY_ID(String entry_id) {
		ENTRY_ID = entry_id;
	}
	public String getTRIGGER_NAME() {
		return TRIGGER_NAME;
	}
	public void setTRIGGER_NAME(String trigger_name) {
		TRIGGER_NAME = trigger_name;
	}
	public String getTRIGGER_GROUP() {
		return TRIGGER_GROUP;
	}
	public void setTRIGGER_GROUP(String trigger_group) {
		TRIGGER_GROUP = trigger_group;
	}
	public String getINSTANCE_NAME() {
		return INSTANCE_NAME;
	}
	public void setINSTANCE_NAME(String instance_name) {
		INSTANCE_NAME = instance_name;
	}
}
