package reprot.wipstage;

import org.apache.log4j.Logger;
import org.quartz.InterruptableJob;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.UnableToInterruptJobException;

import reprot.template.Trace_Alert_Report;
import rt.util.ReadProperties;
import rt.util.TUtil;

public class Trace_Alert_Report_Venice implements InterruptableJob{
	
	private static Logger log = Logger.getLogger(Trace_Alert_Report_Venice.class);
	Thread selfThread = null;
	
	@Override
	public void interrupt() throws UnableToInterruptJobException {
		if(null != selfThread)
			this.selfThread.interrupt();
	}

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		selfThread = Thread.currentThread();
		//生成报表
		log.info("TraceAlterReprotVenice开始执行");
		String modelPath = TUtil.getURL()+ReadProperties.ReadProprety("TraceAlterReprotVenice.template");
		String reprotPath = TUtil.getURL()+ReadProperties.ReadProprety("TraceAlterReprotVenice.report");
		new Trace_Alert_Report().getData(modelPath,reprotPath);
		log.info("TraceAlterReprotVenice报表生成完毕");
		
		if(!TUtil.transferToServer("trace.directory","TraceAlterReprotVenice.report","yyyy-MM-dd_HH")){
			TUtil.sendFaultMail(ReadProperties.ReadProprety("mail.fault.to"));
			log.error(this.getClass().getName()+"上传失败");
		}else
			log.info(this.getClass().getName()+"上传完毕");
	}

}
