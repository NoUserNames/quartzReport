package reprot.wipstage;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import reprot.template.WIPStageReportNewModel;
import rt.util.ReadProperties;
import rt.util.TUtil;

/**
 * @author Qiang1_Zhang
 * Tiger WIP 超时报警
 */
public class WIPStageReport_AV9 implements Job{
	private Logger log = Logger.getLogger(WIPStageReport_AV9.class);
	
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		log.info("WIPStageReprotAV9开始执行");

		String filePath =ReadProperties.ReadProprety("wip.av9.template");
		String reportPath =ReadProperties.ReadProprety("wip.av9.report");

		new WIPStageReportNewModel().ExportExcel(filePath,reportPath,new WIPStageReportNewModel().GetData(filePath,0),0);
		log.info("制程段完毕");
		new WIPStageReportNewModel().ExportExcel(reportPath,reportPath,new WIPStageReportNewModel().GetTerminalData(reportPath,4),4);
		log.info("站点段完毕");

		log.info("WIPStageReprotAV9报表生成完毕");

		log.info("WIPStageReprotAV9报表生成完毕");
		if(!TUtil.transferToServer("wip.directory","wip.av9.report","yyyy-MM-dd")){
			TUtil.sendFaultMail(this.getClass().getName());
			log.error("WIPStageReprotAV9报表上传失败");
		}else
			log.info("WIPStageReprotAV9报表上传完毕");
	}
}