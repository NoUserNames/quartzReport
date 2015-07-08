package rt.quartz;

import org.apache.log4j.Logger;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;

import rt.util.TUtil;

public class QuartzManage {
	private static Logger log = Logger.getLogger(QuartzManage.class);
	
	public static void main(String[] a){
//					updateJobCron("triggertest2", "triggertestGroup", "0 0/10 * 1/1 * ? *");
//		resumeJobNow();
//		pauseJob("Jobtest", "tg");
		runJobNow("test", "job.test");
	}
	
	/**
	 * create a job
	 * @param jobClassName 任务执行类完整路径
	 * @param jobName 任务名
	 * @param jobGroup 任务组
	 * @param triggerName 触发器名
	 * @param triggerGroup 触发器组
	 * @param cronExpression 触发器表达式
	 * @return true || false
	 */
	public boolean createJob(String jobClassName,String jobName,String jobGroup,String triggerName,String triggerGroup,String cronExpression){
		SchedulerFactory factory = new StdSchedulerFactory();
		try {
			Scheduler scheduler = factory.getScheduler();
//			JobListener jobListener = new QuartzListener("quartz-listener");
//			scheduler.getListenerManager().addJobListener(jobListener);
			Class c = Class.forName(jobClassName);

			JobDetail jobDetail = JobBuilder.newJob(c).withIdentity(jobName, jobGroup).build();
			CronTrigger triggerAV4 = TriggerBuilder.newTrigger().withIdentity(
					triggerName, triggerGroup).withSchedule(CronScheduleBuilder.cronSchedule(cronExpression)).build();			
			
			JobKey jobKey = new  JobKey(jobName,jobGroup);
			
			TriggerKey triggerKey = new TriggerKey(triggerName,triggerGroup);
			
			if (scheduler.checkExists(jobKey)){
				log.error("已存在 "+jobName);
				return false;
			}
			if (scheduler.checkExists(triggerKey)){
				log.error("已存在 "+triggerName);
				return false;
			}
			
			scheduler.scheduleJob(jobDetail, triggerAV4);
			scheduler.start();
		} catch (SchedulerException e) {
			log.error("获取Scheduler失败"+e.getMessage());
		} catch (ClassNotFoundException e) {
			log.error("没有找到job类"+jobClassName+e.getMessage());
		}
		return true;
	}
	
	public static boolean deleteJob(String jobName,String jobGroup){
		SchedulerFactory factory = new StdSchedulerFactory();
		 Scheduler scheduler;
		try {
			scheduler = factory.getScheduler();
			JobKey jobKey = JobKey.jobKey(jobName, jobGroup);  
	        scheduler.deleteJob(jobKey);
	        return true;
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public static boolean pauseJob(String jobName,String jobGroup){
		SchedulerFactory factory = new StdSchedulerFactory();
		 Scheduler scheduler;
		try {
			scheduler = factory.getScheduler();
			JobKey jobKey = JobKey.jobKey(jobName, jobGroup);  
	        scheduler.pauseJob(jobKey);
	        return true;
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public static boolean runJobNow(String jobName,String jobGroup){
		SchedulerFactory factory = new StdSchedulerFactory();
		 Scheduler scheduler;
		try {
			scheduler = factory.getScheduler();
			JobKey jobKey = JobKey.jobKey(jobName, jobGroup);  
	        scheduler.triggerJob(jobKey);
	        return true;
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public static boolean resumeJobNow(String jobName,String jobGroup){
		SchedulerFactory factory = new StdSchedulerFactory();
		Scheduler scheduler;
		try {
			scheduler = factory.getScheduler();
			JobKey jobKey = JobKey.jobKey(jobName, jobGroup);  
	        scheduler.resumeJob(jobKey);
	        return true;
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	 public static boolean updateJobCron(String triggerName,String triggerGroupName, String cronExpression) {
		SchedulerFactory factory = new StdSchedulerFactory();
		Scheduler scheduler;
		try {
			scheduler = factory.getScheduler();
			TriggerKey triggerKey = TriggerKey.triggerKey(triggerName,triggerGroupName);

			CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);

			CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);
			
			trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
			
			scheduler.rescheduleJob(triggerKey, trigger);
			return true;
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	 
		return false;
	}

	/**
	 * 立即终止正在运行的任务
	 * @param jobName
	 * @param jobGroup
	 * @return
	 */
	public static boolean interruptJob(String jobName, String jobGroup) {
		SchedulerFactory factory = new StdSchedulerFactory();
		Scheduler scheduler;
		try {
			scheduler = factory.getScheduler();
			TUtil.print("jobName="+jobName+"\tjobGroup="+jobGroup);
			JobKey jobKey = JobKey.jobKey(jobName, jobGroup);  
	        scheduler.interrupt(jobKey);
	        return true;
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
}