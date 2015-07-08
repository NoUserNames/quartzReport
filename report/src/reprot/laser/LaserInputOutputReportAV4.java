/**
 * 
 */
package reprot.laser;

import org.apache.log4j.Logger;
import org.quartz.InterruptableJob;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.UnableToInterruptJobException;

import reprot.template.LaserReportNew;
import rt.util.ReadProperties;
import rt.util.TUtil;

/**
 * AV4镭雕投入产出报表
 * @author 张强
 *
 */
public class LaserInputOutputReportAV4  implements InterruptableJob{
	private static Logger log = Logger.getLogger(LaserInputOutputReportAV4.class);
	
	private Thread laserReportav4;
	
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		log.info(this.getClass().getName()+"开始执行");
		this.laserReportav4 = Thread.currentThread();

		String template = TUtil.getURL() + ReadProperties.ReadProprety("laser.av4.template");//模板路径
		
		String reprot = TUtil.getURL() + ReadProperties.ReadProprety("laser.av4.report");//报表路径
		
		String part_no = ReadProperties.ReadProprety("laser.av4.part_no");//料号
		
		String process = ReadProperties.ReadProprety("laser.av4.process");//制程
		log.info("template="+template+"\treprot="+reprot+"\tpart_no="+part_no+"\tprocess="+process);
		
		int time = Integer.parseInt(ReadProperties.ReadProprety("laser.av4.time"));//安全时间
		log.info(this.getClass().getName()+"报表开始生成。");
		
		new LaserReportNew().expLaserOutput(template, reprot, part_no, process, time);//生成报表
		log.info(this.getClass().getName()+"报表生成完毕。"+ReadProperties.ReadProprety("laser.directory")+"\t"+ReadProperties.ReadProprety("laser.av4.report"));
		
		if(!TUtil.transferToServer("laser.directory","laser.av4.report","yyyy-MM-dd")){
			TUtil.sendFaultMail(ReadProperties.ReadProprety("mail.fault.to"));
			log.error("AV4镭雕投入产出报表上传失败");
		}else
			log.info("AV4镭雕投入产出报表上传完毕");
	}
	@Override
	public void interrupt() throws UnableToInterruptJobException {
		if(this.laserReportav4 != null){
			this.laserReportav4.interrupt();
		}
	}
}