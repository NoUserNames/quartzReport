package reprot.wipstage;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import reprot.template.WIPStageReport;
import rt.util.ReadProperties;
import rt.util.TUtil;

/**
 * @author Qiang1_Zhang
 * WIPMILAN 报表
 */
public class WIPStageReport_Paris implements Job{
	private static Logger log = Logger.getLogger(WIPStageReport_Paris.class);
	
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		log.info("Paris_WIPStageReport开始执行");
		String filePath = TUtil.getURL()+ReadProperties.ReadProprety("wip.paris.template");
		String reportPath = TUtil.getURL()+ReadProperties.ReadProprety("wip.paris.report");
		String part_no = ReadProperties.ReadProprety("wip.paris.part_no");
		new WIPStageReport().ExportExcel(filePath,reportPath,part_no);
		log.info("Paris_WIPStageReport报表生成完毕");
		
		if(!TUtil.transferToServer("wip.directory","wip.paris.report","yyyy-MM-dd")){
			TUtil.sendFaultMail(this.getClass().getName());
			log.error("Paris_WIPStageReport报表上传失败");
		}else
			log.info("Paris_WIPStageReport报表上传完毕");
	}	
}