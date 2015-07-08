package reprot.laser;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import reprot.template.LaserReport;
import rt.util.ReadProperties;
import rt.util.TUtil;

public class LaserReportE5 implements Job {
	
	private static Logger log = Logger.getLogger(LaserReportE5.class);
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		log.info("LaserReportE5");
		String filePath =TUtil.getURL() + ReadProperties.ReadProprety("laser.e5.template");
		String reprotPath = TUtil.getURL() + ReadProperties.ReadProprety("laser.e5.report");
		String process = ReadProperties.ReadProprety("SproscessA.e5.name")+","+ReadProperties.ReadProprety("SproscessB.e5.name");
		int safeTime = Integer.parseInt(ReadProperties.ReadProprety("Sproscess.e5.time"));
		new LaserReport().ExportReport(filePath,reprotPath,safeTime,process.split(","));
		
		if(!TUtil.transferToServer("laser.directory","laser.e5.report","yyyy-MM-dd")){
			TUtil.sendFaultMail(this.getClass().getName());
			log.error(this.getClass().getName()+"上传失败");
		}else
			log.info(this.getClass().getName()+"上传完毕");
	}

}
