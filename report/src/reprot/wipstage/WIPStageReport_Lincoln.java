package reprot.wipstage;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import reprot.template.WIPStageReportNewModel;

import rt.util.ReadProperties;
import rt.util.TUtil;

public class WIPStageReport_Lincoln implements Job {
	private static Logger log = Logger.getLogger(WIPStageReport_Lincoln.class);
	
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		log.info("Lincoln_WIPStageReport开始执行");
		
		String filePath =ReadProperties.ReadProprety("wip.lincoln.template");
		String reportPath =ReadProperties.ReadProprety("wip.lincoln.report");

		new WIPStageReportNewModel().ExportExcel(filePath,reportPath,new WIPStageReportNewModel().GetData(filePath,0),0);
		log.info("制程段完毕");
		new WIPStageReportNewModel().ExportExcel(reportPath,reportPath,new WIPStageReportNewModel().GetTerminalData(reportPath,2),2);
		log.info("站点段完毕");
		
		log.info("Lincoln_WIPStageReport报表生成完毕");
		
		if(!TUtil.transferToServer("wip.directory","wip.lincoln.report","yyyy-MM-dd")){
			TUtil.sendFaultMail(ReadProperties.ReadProprety("mail.fault.to"));
			log.error("Lincoln_WIPStageReport报表上传失败");
		}else
			log.info("Lincoln_WIPStageReport报表上传完毕");

	}
}