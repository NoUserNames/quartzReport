package rt.bean;

public class QRTZ_cron_triggers {
	private String SCHED_NAME;
	private String TRIGGER_NAME;
	private String TRIGGER_GROUP;
	private String CRON_EXPRESSION;
	private String TIME_ZONE_ID;
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
	public String getCRON_EXPRESSION() {
		return CRON_EXPRESSION;
	}
	public void setCRON_EXPRESSION(String cron_expression) {
		CRON_EXPRESSION = cron_expression;
	}
	public String getTIME_ZONE_ID() {
		return TIME_ZONE_ID;
	}
	public void setTIME_ZONE_ID(String time_zone_id) {
		TIME_ZONE_ID = time_zone_id;
	}
}
