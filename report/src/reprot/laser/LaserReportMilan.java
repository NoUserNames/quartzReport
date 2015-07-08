package reprot.laser;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import reprot.template.LaserReport;
import rt.util.ReadProperties;
import rt.util.TUtil;

public class LaserReportMilan implements Job {

	private static Logger log = Logger.getLogger(LaserReportMilan.class);
	
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		log.info("LaserReportMiLan");
		String filePath =TUtil.getURL() + ReadProperties.ReadProprety("laser.milan.template");
		String reprotPath = TUtil.getURL() + ReadProperties.ReadProprety("laser.milan.report");
		String process = ReadProperties.ReadProprety("SprocessA.milan.name")+","+ReadProperties.ReadProprety("SprocessB.milan.name");
		int safeTime = Integer.parseInt(ReadProperties.ReadProprety("Sproscess.milan.time"));
		new LaserReport().ExportReport(filePath,reprotPath,safeTime,process.split(","));
		
		if(!TUtil.transferToServer("laser.directory","laser.milan.report","yyyy-MM-dd")){
			TUtil.sendFaultMail(this.getClass().getName());
			log.error(this.getClass().getName()+"上传失败");
		}else
			log.info(this.getClass().getName()+"上传完毕");
	}	
}
