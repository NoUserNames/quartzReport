package rt.dao;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.CronTrigger;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;

import rt.bean.QRTZ_triggers;
import rt.connection.DBManager;
import rt.quartz.QuartzManage;
import rt.util.TUtil;

public class ScheduleDAOImpl implements IScheduleDAO{
	private static Logger log = Logger.getLogger(ScheduleDAOImpl.class);
	
	public static void main(String[] args){
		new ScheduleDAOImpl().getAllJobs();
	}
	
	//ACQUIRED（运行中） WAITING(运行中)
	public List<QRTZ_triggers> QuerySchedules(String ...name){
		DBManager dbManger = new DBManager();
		
		Connection connection  = dbManger.GetConnection(0);
		String sql="select * from qrtz_triggers where 1=1";
		if (name.length!=0){
			sql += " and job_name='"+name[0]+"'";
		}
		List<QRTZ_triggers> list = new ArrayList<QRTZ_triggers>();
		try {
			ResultSet rs = connection.createStatement().executeQuery(sql);
			while (rs.next()){
				QRTZ_triggers qrtzTriggers = new QRTZ_triggers();
				qrtzTriggers.setJOB_NAME(rs.getString("JOB_NAME"));
				qrtzTriggers.setJOB_GROUP(rs.getString("JOB_GROUP"));
				qrtzTriggers.setPREV_FIRE_TIME(TUtil.format(new Date(rs.getLong("PREV_FIRE_TIME")), "yyyy-MM-dd HH:mm:ss"));
				qrtzTriggers.setNEXT_FIRE_TIME(TUtil.format(new Date(rs.getLong("NEXT_FIRE_TIME")), "yyyy-MM-dd HH:mm:ss"));
				qrtzTriggers.setDESCRIPTION(rs.getString("DESCRIPTION"));
				qrtzTriggers.setTRIGGER_STATE(rs.getString("TRIGGER_STATE"));
				qrtzTriggers.setTRIGGER_GROUP(rs.getString("TRIGGER_GROUP"));
				qrtzTriggers.setTRIGGER_NAME(rs.getString("TRIGGER_NAME"));
				list.add(qrtzTriggers);
			}
			dbManger.closeConnection(connection, rs);
		} catch (SQLException e) {
			log.error("获取所有任务时出错"+e.getMessage());
			e.printStackTrace();
		}
		return list;
	}
	
	@Override
	public QRTZ_triggers FindByName(String name) {
		return QuerySchedules(name).get(0);
	}

	@Override
	public String getTriggerRule(String triggerName) {
		DBManager dbManger = new DBManager();		
		Connection connection  = dbManger.GetConnection(0);
		String sql ="select cron_expression from qrtz_cron_triggers where trigger_name='"+triggerName+"'";

		ResultSet rs= null;
		try {
			rs = connection.createStatement().executeQuery(sql);
			if (rs.next()){
				return rs.getString("cron_expression");
			}
		} catch (SQLException e) {
			log.error("获取触发器规则失败："+e.getMessage());
			e.printStackTrace();
		}finally{
			dbManger.closeConnection(connection, rs);
		}
		return null;
	}

	@Override
	public boolean CreateJob(QRTZ_triggers qrtz_triggers) {
		QuartzManage qrtz = new QuartzManage();
		return qrtz.createJob(qrtz_triggers.getQrtz_job_details().getJOB_CLASS_NAME(), qrtz_triggers.getJOB_NAME(), qrtz_triggers.getJOB_GROUP(), 
				qrtz_triggers.getTRIGGER_NAME(), qrtz_triggers.getTRIGGER_GROUP(), qrtz_triggers.getQrtz_cron_triggers().getCRON_EXPRESSION());
	}
	
	
    /**
     * 判断数据库是否支持事物
     * @param con 数据库连接
     * @return True || False
     */
    public boolean JudgeTransaction(Connection con) {  
        try {  
            // 获取数据库的元数据  
            DatabaseMetaData md = con.getMetaData();  
            // 获取事务处理支持情况  
            return md.supportsTransactions();  
        } catch (SQLException e) {  
            e.printStackTrace();  
        }  
        return false;  
    }

	@Override
	public boolean updateCronExpression(String triggerName,
			String triggerGroupName, String cronExpression) {
		return QuartzManage.updateJobCron(triggerName, triggerGroupName, cronExpression);
	}

	@Override
	public boolean pauseJob(String jobName, String jobGroup) {
		return QuartzManage.pauseJob(jobName,jobGroup);
	}

	@Override
	public boolean deleteJob(String jobName, String jobGroup) {
		// TODO Auto-generated method stub
		return QuartzManage.deleteJob(jobName,jobGroup);
	}

	@Override
	public boolean resumeJobNow(String jobName, String jobGroup) {
		// TODO Auto-generated method stub
		return QuartzManage.resumeJobNow(jobName, jobGroup);
	}

	@Override
	public boolean runJobNow(String jobName, String jobGroup) {
		// TODO Auto-generated method stub
		return QuartzManage.runJobNow(jobName, jobGroup);
	}

	@Override
	public List<QRTZ_triggers> getAllJobs() {
		SchedulerFactory factory = new StdSchedulerFactory();
		Scheduler scheduler;
		List<QRTZ_triggers> list = new ArrayList<QRTZ_triggers>();
		try {
			scheduler = factory.getScheduler();
			for (String groupName : scheduler.getJobGroupNames()) {
				for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {  
					List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
					String jobName = jobKey.getName(); 
					
					String jobGroup = jobKey.getGroup();
//					String cronExpression ="";
//					Date nextFireTime =null;
//					String triggerName="";
//					String triggerGroup="";
//					String s ="";
					QRTZ_triggers qrtzTriggers = new QRTZ_triggers();
					qrtzTriggers.setJOB_NAME(jobName);
					qrtzTriggers.setJOB_GROUP(jobGroup);
					qrtzTriggers.setTRIGGER_STATE(scheduler.getTriggerState(triggers.get(0).getKey()).toString());
//					String pre = qrtzTriggers.getPREV_FIRE_TIME();
					Date pre = triggers.get(0).getPreviousFireTime();
//					pre = (pre!=null) ? pre :(TUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
					pre = (pre!=null) ? pre :(new Date());
					qrtzTriggers.setPREV_FIRE_TIME(TUtil.format(pre, "yyyy-MM-dd HH:mm:ss"));
					qrtzTriggers.setNEXT_FIRE_TIME(TUtil.format(triggers.get(0).getNextFireTime(), "yyyy-MM-dd HH:mm:ss"));
//					nextFireTime = qrtzTriggers.getNEXT_FIRE_TIME();
//					for (Trigger trigger : triggers) {
//						if (trigger instanceof CronTrigger) {
//							CronTrigger cronTrigger = (CronTrigger) trigger;
////							cronExpression = cronTrigger.getCronExpression();
//							nextFireTime = trigger.getPreviousFireTime();
//							
////							trigger.getDescription();
////							scheduler.getTriggerState(trigger.getKey()).name();
////							s = triggerName = trigger.getKey().getName();
////							triggerGroup = trigger.getKey().getGroup();
////							qrtzTriggers.setTRIGGER_STATE(s);
//							
//						}
//					}
					
					list.add(qrtzTriggers);
//					qrtzTriggers.setNEXT_FIRE_TIME(trigger.)
//					System.out.println("[jobName] : " + jobName + " [groupName] : " + jobGroup +" - "+pre+" - " +nextFireTime+" - "+cronExpression+" - "+triggers.get(0).getJobKey().getName()+" - "+triggerGroup+" - "+s);  
				} 
			}
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	
	public QRTZ_triggers findTriggerByTriggerKey(String jobName,String jobGroup){
		SchedulerFactory factory = new StdSchedulerFactory();
		Scheduler scheduler;
		Trigger trigger = null;
		QRTZ_triggers qrtz_triggers = new QRTZ_triggers();
		try {
			scheduler = factory.getScheduler();
			JobKey jobkey = JobKey.jobKey(jobName, jobGroup);
//			TriggerKey triggerKey = TriggerKey.triggerKey(jobkey, jobGroup); 
//			trigger = scheduler.getTrigger(triggerKey);
			trigger = scheduler.getTriggersOfJob(jobkey).get(0);
			qrtz_triggers.setTRIGGER_NAME(trigger.getKey().getName());
			qrtz_triggers.setTRIGGER_GROUP(trigger.getKey().getGroup());
			CronTrigger cronTrigger = (CronTrigger) trigger;
			qrtz_triggers.getQrtz_cron_triggers().setCRON_EXPRESSION(cronTrigger.getCronExpression());
			qrtz_triggers.setJOB_NAME(jobName);
			qrtz_triggers.setJOB_GROUP(jobGroup);
			Date pre = trigger.getPreviousFireTime();
			pre = (pre!=null) ? pre :(new Date());
			
			qrtz_triggers.setNEXT_FIRE_TIME(TUtil.format(trigger.getNextFireTime(), "yyyy-MM-dd HH:mm:ss"));
			qrtz_triggers.setPREV_FIRE_TIME(TUtil.format(pre, "yyyy-MM-dd HH:mm:ss"));
			qrtz_triggers.setTRIGGER_STATE(scheduler.getTriggerState(trigger.getKey()).name());
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return qrtz_triggers;
	}

	/** 立即终止正在运行的任务
	 * 
	 **/
	@Override
	public boolean interruptJob(String jobName, String jobGroup) {		
		return QuartzManage.interruptJob(jobName, jobGroup);
	}
}