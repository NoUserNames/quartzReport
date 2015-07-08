package reprot.laser;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import reprot.template.LaserReport;
import rt.util.ReadProperties;
import rt.util.TUtil;

public class LaserReprotLincoln implements Job {
	private static Logger log = Logger.getLogger(LaserReprotLincoln.class);
	
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		log.info(this.getClass().getName()+TUtil.format("yyyy-MM-dd HH:mm:ss"));
		String filePath = TUtil.getURL() + ReadProperties.ReadProprety("laser.lincoln.template");
		String reprotPath = TUtil.getURL() + ReadProperties.ReadProprety("laser.lincoln.report");
		String process = ReadProperties.ReadProprety("SproscessA.Lincoln.name")+","+ReadProperties.ReadProprety("SproscessB.Lincoln.name");
		int safeTime = Integer.parseInt(ReadProperties.ReadProprety("Sproscess.Lincoln.time"));
		new LaserReport().ExportReport(filePath,reprotPath,safeTime,process.split(","));
		
		if(!TUtil.transferToServer("laser.directory","laser.lincoln.report","yyyy-MM-dd")){
			TUtil.sendFaultMail(this.getClass().getName());
			log.error(this.getClass().getName()+"上传失败");
		}else
			log.info(this.getClass().getName()+"上传完毕");
	}
}