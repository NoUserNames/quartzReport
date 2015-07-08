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
 * Venice WIP 超时报警
 */
public class WIPStageReprotVenice implements Job{
	private Logger log = Logger.getLogger(WIPStageReprotVenice.class);
	
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		log.info("Venice_WIPStageReport开始执行");

		String filePath = ReadProperties.ReadProprety("wip.venice.template");
		String reportPath = ReadProperties.ReadProprety("wip.venice.report");

		new WIPStageReportNewModel().ExportExcel(filePath,reportPath,new WIPStageReportNewModel().GetData(filePath,0),0);
		log.info("制程段完毕");
		new WIPStageReportNewModel().ExportExcel(reportPath,reportPath,new WIPStageReportNewModel().GetTerminalData(reportPath,2),2);
		log.info("站点段完毕");
		
		log.info("Venice_WIPStageReport报表生成完毕");
		if(!TUtil.transferToServer("wip.directory","wip.venice.report","yyyy-MM-dd")){
			TUtil.sendFaultMail(ReadProperties.ReadProprety("mail.fault.to"));
			log.error("Venice_WIPStageReport报表上传失败");
		}else
			log.info("Venice_WIPStageReport报表上传完毕");
	}
}