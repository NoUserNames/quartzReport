package rt.bean;

import java.sql.Blob;

public class QRTZ_triggers {
	private String SCHED_NAME;
	private String TRIGGER_NAME;
	private String TRIGGER_GROUP;
	private String JOB_NAME;
	private String JOB_GROUP;
	private String DESCRIPTION;
	private String NEXT_FIRE_TIME;
	private String PREV_FIRE_TIME;
	private int PRIORITY;
	private String TRIGGER_STATE;
	private String TRIGGER_TYPE;
	private int START_TIME;
	private int END_TIME;
	private String CALENDAR_NAME;
	private int MISFIRE_INSTR;
	private Blob JOB_DATA;
	private QRTZ_job_details qrtz_job_details = new QRTZ_job_details();
	private QRTZ_cron_triggers qrtz_cron_triggers =new QRTZ_cron_triggers();
	public String getSCHED_NAME() {
		return SCHED_NAME;
	}
	public void setSCHED_NAME(String sched_name) {
		SCHED_NAME = sched_name;
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
	public String getDESCRIPTION() {
		return DESCRIPTION;
	}
	public void setDESCRIPTION(String description) {
		DESCRIPTION = description;
	}
	public String getNEXT_FIRE_TIME() {
		return NEXT_FIRE_TIME;
	}
	public void setNEXT_FIRE_TIME(String next_fire_time) {
		NEXT_FIRE_TIME = next_fire_time;
	}
	public String getPREV_FIRE_TIME() {
		return PREV_FIRE_TIME;
	}
	public void setPREV_FIRE_TIME(String prev_fire_time) {
		PREV_FIRE_TIME = prev_fire_time;
	}
	public int getPRIORITY() {
		return PRIORITY;
	}
	public void setPRIORITY(int priority) {
		PRIORITY = priority;
	}
	public String getTRIGGER_STATE() {
		return TRIGGER_STATE;
	}
	public void setTRIGGER_STATE(String trigger_state) {
		TRIGGER_STATE = trigger_state;
	}
	public String getTRIGGER_TYPE() {
		return TRIGGER_TYPE;
	}
	public void setTRIGGER_TYPE(String trigger_type) {
		TRIGGER_TYPE = trigger_type;
	}
	public int getSTART_TIME() {
		return START_TIME;
	}
	public void setSTART_TIME(int start_time) {
		START_TIME = start_time;
	}
	public int getEND_TIME() {
		return END_TIME;
	}
	public void setEND_TIME(int end_time) {
		END_TIME = end_time;
	}
	public String getCALENDAR_NAME() {
		return CALENDAR_NAME;
	}
	public void setCALENDAR_NAME(String calendar_name) {
		CALENDAR_NAME = calendar_name;
	}
	public int getMISFIRE_INSTR() {
		return MISFIRE_INSTR;
	}
	public void setMISFIRE_INSTR(int misfire_instr) {
		MISFIRE_INSTR = misfire_instr;
	}
	public Blob getJOB_DATA() {
		return JOB_DATA;
	}
	public void setJOB_DATA(Blob job_data) {
		JOB_DATA = job_data;
	}
	public QRTZ_job_details getQrtz_job_details() {
		return qrtz_job_details;
	}
	public void setQrtz_job_details(QRTZ_job_details qrtz_job_details) {
		this.qrtz_job_details = qrtz_job_details;
	}
	public QRTZ_cron_triggers getQrtz_cron_triggers() {
		return qrtz_cron_triggers;
	}
	public void setQrtz_cron_triggers(QRTZ_cron_triggers qrtz_cron_triggers) {
		this.qrtz_cron_triggers = qrtz_cron_triggers;
	}
}
