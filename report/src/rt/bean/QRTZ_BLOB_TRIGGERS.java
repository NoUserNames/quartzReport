package rt.bean;

import java.sql.Blob;

public class QRTZ_BLOB_TRIGGERS {
	private String SCHED_NAME;
	private String TRIGGER_NAME;
	private String TRIGGER_GROUP;
	private Blob BLOB_DATA;
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
	public Blob getBLOB_DATA() {
		return BLOB_DATA;
	}
	public void setBLOB_DATA(Blob blob_data) {
		BLOB_DATA = blob_data;
	}
}
