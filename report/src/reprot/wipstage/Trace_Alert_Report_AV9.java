package reprot.wipstage;

import org.apache.log4j.Logger;
import org.quartz.InterruptableJob;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.UnableToInterruptJobException;

import reprot.template.Trace_Alert_Report_New;
import rt.util.ReadProperties;
import rt.util.TUtil;

public class Trace_Alert_Report_AV9 implements InterruptableJob{
	
	private static Logger log = Logger.getLogger(Trace_Alert_Report_AV9.class);
	Thread selfThread = null;
	
	public static void main(String[] args) {
		try {
			new Trace_Alert_Report_AV9().execute(null);
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
		log.info("TraceAlterReprotAV9开始执行");
		String modelPath = TUtil.getURL()+ReadProperties.ReadProprety("TraceAlterReprotAV9.template");
		String reprotPath = TUtil.getURL()+ReadProperties.ReadProprety("TraceAlterReprotAV9.report");
		String fileName = TUtil.format("yyyy-MM-dd_HH")+".xlsx";
		new Trace_Alert_Report_New().getData(modelPath,reprotPath,fileName);
		log.info("TraceAlterReprotAV9报表生成完毕");
		
		if(!TUtil.transferToServer("trace.directory","TraceAlterReprotAV9.report","yyyy-MM-dd_HH")){
			TUtil.sendFaultMail(ReadProperties.ReadProprety("mail.fault.to"));
			log.error(this.getClass().getName()+"上传失败");
		}else
			log.info(this.getClass().getName()+"上传完毕");
	}
}