package reprot.wipstage;

import org.apache.log4j.Logger;
import org.quartz.InterruptableJob;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.UnableToInterruptJobException;

import reprot.template.WIPStageReportNewModel;

import rt.util.ReadProperties;
import rt.util.TUtil;


public class WIPStageReport_Hulk implements InterruptableJob{

	private Logger log = Logger.getLogger(WIPStageReport_Hulk.class);
	Thread selfThread = null;

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		selfThread = Thread.currentThread();
		
		//生成报表
		log.info("WIPStageReport_Hulk开始执行");
		String filePath =ReadProperties.ReadProprety("wip.hulk.template");
		String reportPath =ReadProperties.ReadProprety("wip.hulk.report");
		
		new WIPStageReportNewModel().ExportExcel(filePath,reportPath,new WIPStageReportNewModel().GetData(filePath,0),0);
		log.info("WIPStageReport_Hulk报表生成完毕");
		
		if(!TUtil.transferToServer("wip.directory","wip.hulk.report","yyyy-MM-dd")){
			TUtil.sendFaultMail(ReadProperties.ReadProprety("mail.fault.to"));
			log.error("WIPStageReport_Hulk报表上传失败");
		}else
			log.info("WIPStageReport_Hulk报表上传完毕");
	}

	@Override
	public void interrupt() throws UnableToInterruptJobException {
		if(null != selfThread)
			this.selfThread.interrupt();
	}
}
