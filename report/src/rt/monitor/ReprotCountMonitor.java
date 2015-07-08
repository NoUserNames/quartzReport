package rt.monitor;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import rt.mail.MailUtils;
import rt.util.ReadProperties;
import rt.util.TUtil;

/**
 * 报表数量监控
 * @author Qiang1_Zhang
 *
 */
public class ReprotCountMonitor implements Job{

	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		new ReprotCountMonitor().getFile();
		String root = "\\\\10.131.18.8\\rt3生產機種\\ProductionReprot";
		File filePath = new File(root);
		File[] list = filePath.listFiles();
		
		for(File file : list){
			System.out.println(file.getParent()+"=="+file.getName());
		}
	}
	
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		MailUtils mail = new MailUtils();
		mail.setTo(ReadProperties.ReadProprety("mail.daily.to"));
		mail.setCC(ReadProperties.ReadProprety("mail.daily.cc"));
		String content = getEveryDay();
		mail.setContent(content);		
		mail.sendMail();
	}
	
	/**
	 * 检索当天生成的报表
	 * @return int 生成报表总数量
	 */
	public int getFile(){
		String root = "\\\\10.131.18.8\\rt3生產機種\\ProductionReprot";
		File filePath = new File(root);
		File[] list = filePath.listFiles();
		int total=0;
		for(File file : list){
			//找到的报表
			if(new File(file.getAbsolutePath()+"\\"+TUtil.format("yyyy-MM-dd")+".xls").exists()){
				total++;
			}
			if(file.getName().equals("TraceAlterReprot-reprot-AV9")){//Lincoln报表，特殊统计
				File fileT = new File("\\\\10.131.18.8\\rt3生產機種\\ProductionReprot\\TraceAlterReprot-reprot-AV9\\"+TUtil.format("yyyy-MM-dd"));
				int tCount = fileT.listFiles().length;
				total += tCount;
			}			
		}
		return total;
	}

	/**
	 * 获取当天报表运行状况
	 * @return 当天报表运行总结
	 */
	public String getEveryDay(){
		Map<String,Integer> map = new HashMap<String,Integer>();
		String[] daily = ReadProperties.ReadProprety("daily.count").split(",");
		map.put("星期一", Integer.parseInt(daily[0]));
		map.put("星期二", Integer.parseInt(daily[1]));
		map.put("星期三", Integer.parseInt(daily[2]));
		map.put("星期四", Integer.parseInt(daily[3]));
		map.put("星期五", Integer.parseInt(daily[4]));
		map.put("星期六", Integer.parseInt(daily[5]));
		map.put("星期日", Integer.parseInt(daily[6]));
		
		int expectCount = map.get(TUtil.format("EE"));//预期数量
		int actualNumber = getFile();//实际数量
		
		String result = (expectCount == actualNumber) ? "正常" :"異常";
		String conntent = "Dear All:\n\t今天是"+TUtil.format("yyyy-MM-dd EE")+",預期報表數量為："+expectCount+
		",實際發出報表數量為："+actualNumber+".今日報表運行："+result+"\n\n注：該郵件由系統自動發送，請勿回復！\nTracebility 系統";

		return conntent;
	}
}