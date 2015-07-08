package rt.bean;

import java.sql.Blob;

public class QRTZ_calendars {
	private String SCHED_NAME;
	private String CALENDAR_NAME;
	private Blob CALENDAR;
	public String getSCHED_NAME() {
		return SCHED_NAME;
	}
	public void setSCHED_NAME(String sched_name) {
		SCHED_NAME = sched_name;
	}
	public String getCALENDAR_NAME() {
		return CALENDAR_NAME;
	}
	public void setCALENDAR_NAME(String calendar_name) {
		CALENDAR_NAME = calendar_name;
	}
	public Blob getCALENDAR() {
		return CALENDAR;
	}
	public void setCALENDAR(Blob calendar) {
		CALENDAR = calendar;
	}
}
