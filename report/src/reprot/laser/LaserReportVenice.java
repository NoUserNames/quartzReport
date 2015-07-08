/**
 * 
 */
package reprot.laser;

import org.apache.log4j.Logger;
import org.quartz.InterruptableJob;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.UnableToInterruptJobException;

import reprot.template.LaserReportPad;
import rt.util.ReadProperties;
import rt.util.TUtil;

/**
 * Venice镭雕投入产出报表
 * @author 张强
 *
 */
public class LaserReportVenice  implements InterruptableJob{
	private static Logger log = Logger.getLogger(LaserReportVenice.class);
	
	private Thread laserReportvenice;
	
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		log.info(this.getClass().getName()+"开始执行");
		this.laserReportvenice = Thread.currentThread();

		String template = TUtil.getURL() + ReadProperties.ReadProprety("laser.venice.template");//模板路径
		
		String reprot = TUtil.getURL() + ReadProperties.ReadProprety("laser.venice.report");//报表路径
		
		String part_no = ReadProperties.ReadProprety("laser.venice.part_no");//料号
		
		String process = ReadProperties.ReadProprety("laser.venice.process");//制程
		log.info("template="+template+"\treprot="+reprot+"\tpart_no="+part_no+"\tprocess="+process);
		
		int time = Integer.parseInt(ReadProperties.ReadProprety("laser.venice.time"));//安全时间
		log.info(this.getClass().getName()+"报表开始生成。");
		
		new LaserReportPad().expLaserOutput(template,reprot,part_no,process,time);

		log.info(this.getClass().getName()+"报表生成完毕。");
		
		if(!TUtil.transferToServer("laser.directory","laser.venice.report","yyyy-MM-dd")){
			TUtil.sendFaultMail(this.getClass().getName());
			log.error(this.getClass().getName()+"上传失败");
		}else
			log.info(this.getClass().getName()+"上传完毕");
	}
	@Override
	public void interrupt() throws UnableToInterruptJobException {
		if(this.laserReportvenice != null){
			this.laserReportvenice.interrupt();
		}
	}
}