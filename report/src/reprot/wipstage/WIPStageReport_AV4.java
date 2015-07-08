package reprot.wipstage;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import reprot.template.WIPStageReportNewModel;
import rt.util.ReadProperties;
import rt.util.TUtil;


public class WIPStageReport_AV4 implements Job {
	private static Logger log = Logger.getLogger(WIPStageReport_AV4.class);
	
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		String filePath = TUtil.getURL()+ReadProperties.ReadProprety("wip.av4.template");
		log.info("AV4 START");
		String reprotPath = TUtil.getURL()+ReadProperties.ReadProprety("wip.av4.report");

		new WIPStageReportNewModel().ExportExcel(filePath,reprotPath,new WIPStageReportNewModel().GetData(filePath,0),0);
		log.info("制程段完毕");
		
		log.info("WIPStageReprotAV4报表生成完毕");
		if(!TUtil.transferToServer("wip.directory","wip.av4.report","yyyy-MM-dd")){
			TUtil.sendFaultMail(this.getClass().getName());
			log.error("WIPStageReprotAV4报表上传失败");
		}else
			log.info("WIPStageReprotAV4报表上传完毕");
	}
}