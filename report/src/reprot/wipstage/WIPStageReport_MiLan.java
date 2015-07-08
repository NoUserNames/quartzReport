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
 * WIPMILAN 报表
 */
public class WIPStageReport_MiLan implements Job{
	private static Logger log = Logger.getLogger(WIPStageReport_MiLan.class);

	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		log.info("MiLan_WIPStageReport开始执行");
		
		String filePath =ReadProperties.ReadProprety("wip.milan.template");
		String reportPath =ReadProperties.ReadProprety("wip.milan.report");

		new WIPStageReportNewModel().ExportExcel(filePath,reportPath,new WIPStageReportNewModel().GetData(filePath,0),0);
		log.info("制程段完毕："+TUtil.format("HH:mm:ss"));
		new WIPStageReportNewModel().ExportExcel(reportPath,reportPath,new WIPStageReportNewModel().GetTerminalData(reportPath,2),2);
		log.info("站点段完毕："+TUtil.format("HH:mm:ss"));
		
		log.info("MiLan_WIPStageReport报表生成完毕");
		if(!TUtil.transferToServer("wip.directory","wip.milan.report","yyyy-MM-dd")){
			TUtil.sendFaultMail(ReadProperties.ReadProprety("mail.fault.to"));
			log.error("MiLan_WIPStageReport报表上传失败");
		}else
			log.info("MiLan_WIPStageReport报表上传完毕");
	}
}