package rt.bean;

import java.sql.Blob;

public class QRTZ_job_details {
	private String SCHED_NAME;
	private String JOB_NAME;
	private String JOB_GROUP;
	private String DESCRIPTION;
	private String JOB_CLASS_NAME;
	private String IS_DURABLE;
	private String IS_NONCONCURRENT;
	private String IS_UPDATE_DATA;
	private String REQUESTS_RECOVERY;
	private Blob JOB_DATA;
	public String getSCHED_NAME() {
		return SCHED_NAME;
	}
	public void setSCHED_NAME(String sched_name) {
		SCHED_NAME = sched_name;
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
	public String getJOB_CLASS_NAME() {
		return JOB_CLASS_NAME;
	}
	public void setJOB_CLASS_NAME(String job_class_name) {
		JOB_CLASS_NAME = job_class_name;
	}
	public String getIS_DURABLE() {
		return IS_DURABLE;
	}
	public void setIS_DURABLE(String is_durable) {
		IS_DURABLE = is_durable;
	}
	public String getIS_NONCONCURRENT() {
		return IS_NONCONCURRENT;
	}
	public void setIS_NONCONCURRENT(String is_nonconcurrent) {
		IS_NONCONCURRENT = is_nonconcurrent;
	}
	public String getIS_UPDATE_DATA() {
		return IS_UPDATE_DATA;
	}
	public void setIS_UPDATE_DATA(String is_update_data) {
		IS_UPDATE_DATA = is_update_data;
	}
	public String getREQUESTS_RECOVERY() {
		return REQUESTS_RECOVERY;
	}
	public void setREQUESTS_RECOVERY(String requests_recovery) {
		REQUESTS_RECOVERY = requests_recovery;
	}
	public Blob getJOB_DATA() {
		return JOB_DATA;
	}
	public void setJOB_DATA(Blob job_data) {
		JOB_DATA = job_data;
	}
}
