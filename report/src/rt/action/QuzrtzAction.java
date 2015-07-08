package rt.action;

import java.io.IOException;
import java.util.List;

import org.apache.struts2.ServletActionContext;

import rt.bean.Jobs;
import rt.bean.QRTZ_triggers;
import rt.dao.IScheduleDAO;
import rt.dao.ScheduleDAOImpl;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

public class QuzrtzAction extends ActionSupport implements ModelDriven<QRTZ_triggers>{

	List<Jobs> jobList=null;
	List<QRTZ_triggers> list =null;
	private QRTZ_triggers qrtz_triggers = new QRTZ_triggers();
	private static final long serialVersionUID = 1L;
	private IScheduleDAO schedule =null;
	private String params;


	/**
	 * 初始化调度器
	 * @return
	 */
	public String InitQuartz(){
//		QuartzManage quartzManage = new QuartzManage();
		//执行调度任务
//		System.out.println("InitQuartz");
		
		return SUCCESS;
	}
	
	/**
	 * 查询所有任务
	 * @return
	 */
	public String InitJobs(){
		schedule = new ScheduleDAOImpl();
//		list = schedule.QuerySchedules();
		list = schedule.getAllJobs();
		return SUCCESS;
	}
	
	/**
	 * 查询单个触发器
	 * @return
	 */
	public String FindJobByName(){
		schedule = new ScheduleDAOImpl();
//		System.out.println("jobName="+qrtz_triggers.getJOB_NAME()+"\tgroup="+qrtz_triggers.getJOB_GROUP());
//		qrtz_triggers = schedule.FindByName(qrtz_triggers.getJOB_NAME());
		qrtz_triggers = schedule.findTriggerByTriggerKey(qrtz_triggers.getJOB_NAME(),qrtz_triggers.getJOB_GROUP());
		return SUCCESS;
	}
	
	/**
	 * 仅查询触发器表达式
	 */
	public void getTriggerRule(){
		schedule = new ScheduleDAOImpl();
		try {			
			String str = schedule.getTriggerRule(params);
			
			ServletActionContext.getResponse().getWriter().write(str);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 创建job
	 * @return
	 */
	public String CreateJob(){
		schedule = new ScheduleDAOImpl();
		if (schedule.CreateJob(qrtz_triggers))
			return SUCCESS;
		else
			return INPUT;
	}
	
	public String updateCronExpression(){
		schedule = new ScheduleDAOImpl();

		schedule.updateCronExpression(qrtz_triggers.getTRIGGER_NAME(), qrtz_triggers.getTRIGGER_GROUP(), qrtz_triggers.getQrtz_cron_triggers().getCRON_EXPRESSION());

//		qrtz_triggers = schedule.FindByName(qrtz_triggers.getJOB_NAME());
		qrtz_triggers = schedule.findTriggerByTriggerKey(qrtz_triggers.getJOB_NAME(),qrtz_triggers.getJOB_GROUP());
//		try {
//			ServletActionContext.getResponse().getWriter().write("OK");
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		return SUCCESS;
	}
	
	public String doPauseJob(){
		schedule = new ScheduleDAOImpl();

		if (schedule.pauseJob(qrtz_triggers.getJOB_NAME(), qrtz_triggers.getJOB_GROUP()))
			return SUCCESS;
		else
			return INPUT;
	}
	
	public String resumeJobNow(){
		schedule = new ScheduleDAOImpl();

		if (schedule.resumeJobNow(qrtz_triggers.getJOB_NAME(), qrtz_triggers.getJOB_GROUP()))
			return SUCCESS;
		else
			return INPUT;
	}
	
	public String deleteJob(){
		schedule = new ScheduleDAOImpl();

		if (schedule.deleteJob(qrtz_triggers.getJOB_NAME(), qrtz_triggers.getJOB_GROUP()))
			return SUCCESS;
		else
			return INPUT;
	}
	
	public String runJobNow(){
		schedule = new ScheduleDAOImpl();
		if (schedule.runJobNow(qrtz_triggers.getJOB_NAME(), qrtz_triggers.getJOB_GROUP()))
			return SUCCESS;
		else
			return INPUT;
	}

	public String interruptJob(){
		schedule = new ScheduleDAOImpl();
		System.out.println("job_name:"+qrtz_triggers.getJOB_NAME());
		if (schedule.interruptJob(qrtz_triggers.getJOB_NAME(), qrtz_triggers.getJOB_GROUP()))
			return SUCCESS;
		else
			return INPUT;
	}
	
	public List<Jobs> getJobList() {
		return jobList;
	}

	public void setJobList(List<Jobs> jobList) {
		this.jobList = jobList;
	}

	public List<QRTZ_triggers> getList() {
		return list;
	}

	public void setList(List<QRTZ_triggers> list) {
		this.list = list;
	}

	public QRTZ_triggers getQrtz_triggers() {
		return qrtz_triggers;
	}

	public void setQrtz_triggers(QRTZ_triggers qrtz_triggers) {
		this.qrtz_triggers = qrtz_triggers;
	}

	@Override
	public QRTZ_triggers getModel() {
		// TODO Auto-generated method stub
		return qrtz_triggers;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}
}
