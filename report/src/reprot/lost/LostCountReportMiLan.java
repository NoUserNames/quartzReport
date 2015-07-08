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
 * MiLan卡關異常報表
 * @author 陳素林
 *
 */
public class LostCountReportMiLan  implements InterruptableJob{
	private static Logger log = Logger.getLogger(LostCountReportMiLan.class);
	
	private Thread LostCountReportMiLan;
	
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		log.info(this.getClass().getName()+"开始执行");
		this.LostCountReportMiLan = Thread.currentThread();

		String template = ReadProperties.ReadProprety("lost.milan.template");//模板路径
		
		String reprot = ReadProperties.ReadProprety("lost.milan.report");//报表路径
		
		log.info("template="+template+"\treprot="+reprot);
		
		log.info(this.getClass().getName()+"报表开始生成。"+TUtil.format("HH:mm:ss"));		
		new LostCountReport().ExportExcel(template,reprot,new LostCountReport().GetData(template,0),0);
		
		log.info(this.getClass().getName()+"报表生成完毕。"+TUtil.format("HH:mm:ss"));
		if(!TUtil.transferToServer("lostScan.directory","lost.milan.report","yyyy-MM-dd")){
			TUtil.sendFaultMail(ReadProperties.ReadProprety("mail.fault.to"));
			log.error(this.getClass().getName()+"上传失败");
		}else
			log.info(this.getClass().getName()+"上传完毕");
	}
	@Override
	public void interrupt() throws UnableToInterruptJobException {
		if(this.LostCountReportMiLan != null){
			this.LostCountReportMiLan.interrupt();
		}
	}
}