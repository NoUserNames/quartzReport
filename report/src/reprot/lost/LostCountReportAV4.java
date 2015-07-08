/**
 * 
 */
package reprot.lost;

import org.apache.log4j.Logger;
import org.quartz.InterruptableJob;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.UnableToInterruptJobException;


import reprot.template.LostCountReport;
import rt.util.ReadProperties;
import rt.util.TUtil;

/**
 * AV4卡關異常報表
 * @author 陳素林
 *
 */
public class LostCountReportAV4  implements InterruptableJob{
	private static Logger log = Logger.getLogger(LostCountReportAV4.class);
	
	private Thread LostCountReportAV4;
	
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		log.info(this.getClass().getName()+"开始执行");
		this.LostCountReportAV4 = Thread.currentThread();

		String template = ReadProperties.ReadProprety("lost.av4.template");//模板路径
		
		String reprot = ReadProperties.ReadProprety("lost.av4.report");//报表路径
		
		log.info("template="+template+"\treprot="+reprot);
		
		log.info(this.getClass().getName()+"报表开始生成。");		
		new LostCountReport().ExportExcel(template,reprot,new LostCountReport().GetData(template,0),0);
		
		log.info(this.getClass().getName()+"报表生成完毕。");
		
		if(!TUtil.transferToServer("lostScan.directory","lost.av4.report","yyyy-MM-dd")){
			TUtil.sendFaultMail(ReadProperties.ReadProprety("mail.fault.to"));
			log.error(this.getClass().getName()+"上传失败");
		}else
			log.info(this.getClass().getName()+"上传完毕");
	}
	@Override
	public void interrupt() throws UnableToInterruptJobException {
		if(this.LostCountReportAV4 != null){
			this.LostCountReportAV4.interrupt();
		}
	}
}