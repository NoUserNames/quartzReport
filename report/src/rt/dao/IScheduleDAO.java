package rt.dao;

import java.util.List;

import rt.bean.QRTZ_triggers;

public interface IScheduleDAO {
	public List<QRTZ_triggers> QuerySchedules(String ...name);
	public QRTZ_triggers FindByName(String name);
	public String getTriggerRule(String triggerName);
	public boolean CreateJob(QRTZ_triggers QRTZ_triggers);
	public boolean updateCronExpression(String triggerName,String triggerGroupName, String cronExpression);
	public boolean pauseJob(String jobName, String jobGroup);
	public boolean resumeJobNow(String jobName, String jobGroup);
	public boolean deleteJob(String jobName, String jobGroup);
	public boolean runJobNow(String jobName, String jobGroup);
	public boolean interruptJob(String jobName, String jobGroup);
	public List<QRTZ_triggers> getAllJobs();
	public QRTZ_triggers findTriggerByTriggerKey(String triggerName,String triggerGroup);
}
