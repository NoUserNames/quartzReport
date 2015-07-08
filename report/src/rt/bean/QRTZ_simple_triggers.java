package rt.bean;

public class QRTZ_simple_triggers {
	private String SCHED_NAME;
	private String TRIGGER_NAME;
	private String TRIGGER_GROUP;
	private int REPEAT_COUNT;
	private int REPEAT_INTERVAL;
	private int TIMES_TRIGGERED;
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
	public int getREPEAT_COUNT() {
		return REPEAT_COUNT;
	}
	public void setREPEAT_COUNT(int repeat_count) {
		REPEAT_COUNT = repeat_count;
	}
	public int getREPEAT_INTERVAL() {
		return REPEAT_INTERVAL;
	}
	public void setREPEAT_INTERVAL(int repeat_interval) {
		REPEAT_INTERVAL = repeat_interval;
	}
	public int getTIMES_TRIGGERED() {
		return TIMES_TRIGGERED;
	}
	public void setTIMES_TRIGGERED(int times_triggered) {
		TIMES_TRIGGERED = times_triggered;
	}
}
