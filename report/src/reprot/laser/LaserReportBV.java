package reprot.laser;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import reprot.template.LaserReport;
import rt.util.ReadProperties;
import rt.util.TUtil;


public class LaserReportBV implements Job {

	private static Logger log = Logger.getLogger(LaserReportBV.class);

	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		log.info("LaserReportBV");
		String filePath = TUtil.getURL() + ReadProperties.ReadProprety("laser.bv.template");
		String reprotPath = TUtil.getURL() + ReadProperties.ReadProprety("laser.bv.report");
		String process = ReadProperties.ReadProprety("SproscessA.bv.name")+","+ReadProperties.ReadProprety("SproscessB.bv.name");
		int safeTime = Integer.parseInt(ReadProperties.ReadProprety("Sproscess.bv.time"));
		
		log.info(this.getClass().getName()+"报表开始生成。");
		new LaserReport().ExportReport(filePath,reprotPath,safeTime,process.split(","));
		log.info(this.getClass().getName()+"报表生成完毕。");
		
		if(!TUtil.transferToServer("laser.directory","laser.bv.report","yyyy-MM-dd")){
			TUtil.sendFaultMail(this.getClass().getName());
			log.error("BV镭雕投入产出报表上传失败");
		}else
			log.info("BV镭雕投入产出报表上传完毕");
	}
}