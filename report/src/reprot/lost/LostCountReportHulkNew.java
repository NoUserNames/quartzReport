/**
 * 
 */
package reprot.lost;

import org.apache.log4j.Logger;
import org.quartz.InterruptableJob;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.UnableToInterruptJobException;

import reprot.template.LostCountReportNew;
import rt.util.ReadProperties;
import rt.util.TUtil;

/**
 * Hulk卡關異常報表
 * @author 陳素林
 *
 */
public class LostCountReportHulkNew  implements InterruptableJob{
	private static Logger log = Logger.getLogger(LostCountReportHulkNew.class);
	
	private Thread LostCountReportHulkNew;
	
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		log.info(this.getClass().getName()+"开始执行");
		this.LostCountReportHulkNew = Thread.currentThread();

		String template = ReadProperties.ReadProprety("lost.hulk.template");//模板路径
		
		String reprot = ReadProperties.ReadProprety("lost.hulk.report");//报表路径
		
		log.info("template="+template+"\treprot="+reprot);
		
		log.info(this.getClass().getName()+"报表开始生成。"+TUtil.format("HH:mm:ss"));		
		new LostCountReportNew().ExportExcel(template,reprot,new LostCountReportNew().GetData(template,0),0);
		
		log.info(this.getClass().getName()+"报表生成完毕。"+TUtil.format("HH:mm:ss"));
		
		if(!TUtil.transferToServer("lostScan.directory","lost.hulk.report","yyyy-MM-dd")){
			TUtil.sendFaultMail(this.getClass().getName());
			log.error(this.getClass().getName()+"上传失败");
		}else
			log.info(this.getClass().getName()+"上传完毕");
	}
	@Override
	public void interrupt() throws UnableToInterruptJobException {
		if(this.LostCountReportHulkNew != null){
			this.LostCountReportHulkNew.interrupt();
		}
	}
}