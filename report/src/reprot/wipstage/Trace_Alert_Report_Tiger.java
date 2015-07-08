package reprot.wipstage;

import org.apache.log4j.Logger;
import org.quartz.InterruptableJob;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.UnableToInterruptJobException;

import reprot.template.Trace_Alert_Report_NewModel;
import rt.util.ReadProperties;
import rt.util.TUtil;

/**
 * Tiger现场超时找料报表
 * @author 张强
 *
 */
public class Trace_Alert_Report_Tiger implements InterruptableJob{
	
	private static Logger log = Logger.getLogger(Trace_Alert_Report_Tiger.class);
	Thread selfThread = null;
	
	public static void main(String[] args) {
		try {
			new Trace_Alert_Report_Tiger().execute(null);
		} catch (JobExecutionException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void interrupt() throws UnableToInterruptJobException {
		if(null != selfThread)
			this.selfThread.interrupt();
	}

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		selfThread = Thread.currentThread();
		//生成报表
		log.info("TraceAlterReprotTiger开始执行");
		String modelPath = TUtil.getURL()+ReadProperties.ReadProprety("TraceAlterReprotTiger.template");
		String reprotPath = TUtil.getURL()+ReadProperties.ReadProprety("TraceAlterReprotTiger.report");
		new Trace_Alert_Report_NewModel().getData(modelPath,reprotPath);
		log.info("TraceAlterReprotTiger报表生成完毕");
		
		if(!TUtil.transferToServer("trace.directory","TraceAlterReprotTiger.report","yyyy-MM-dd_HH")){
			TUtil.sendFaultMail(ReadProperties.ReadProprety("mail.fault.to"));
			log.error(this.getClass().getName()+"上传失败");
		}else
			log.info(this.getClass().getName()+"上传完毕");
	}
}