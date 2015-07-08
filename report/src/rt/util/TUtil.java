package rt.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import rt.mail.MailUtils;
import rt.sbmtransfer.TransferTheReport;
/**
 * 基础类
 * @author Qiang1_Zhang
 */
public class TUtil {
	static Logger log = Logger.getLogger(TUtil.class);

//	public static void main(String[] a){
//		List a1 = new ArrayList();
//		a1.add(1);
//		a1.add(2);
//		List a2 = new ArrayList();
//		a2.add(3);
//		a2.add(4);
//		
//		a2.addAll(a1);
//		System.out.println(a2);
//	}

	/**
	 * 日期转换函数
	 * @param format 需要转换的格式
	 * @return 转换后的日期
	 */
	public static String format(String format){
		
		return new SimpleDateFormat(format).format(new Date());
	}
	
	/**
	 * 日期转换函数
	 * @param format 需要转换的格式
	 * @return 转换后的日期
	 */
	public static String format(Date date,String format){
		return new SimpleDateFormat(format).format(date);
	}
	
	/**
	 * 打印函数
	 * @param str 对象类型
	 */
	public static void print(Object str){
		System.out.println(str);
	}
	
	/**
	 * 计算距今指定天数的日期
	 * @param day 相差的天数，可为负数
	 * @return 计算之后的日期
	 */
	public static String GetDay(int day){
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
        cal.setTime(new Date());//设置日历时间   
        cal.add(Calendar.DAY_OF_MONTH,day);//天数  
        String strDate = sdf.format(cal.getTime());//得到你想要的天数

		return strDate;
	}
	
	/**
	 * 获取报表模板路径
	 * @return 
	 */
	public static String getURL(){
		String dir =System.getProperty("user.dir");
		dir=dir.substring(0,dir.lastIndexOf("\\"));
		String filePath =dir;
		return filePath;
	}
	
	/**
	 * String类型日期转换为长整型
	 * @param date String类型日期
	 * @param format 日期格式
	 * @return long
	 */
	public static long strDateToLong(String date,String ...format){
		String format1 = null ;
		if (format.length!=0){
			format1 = format[0];
		}else{
			format1 ="yyyy-MM-dd HH:mm:ss";
		}
		String sDt = date;
		SimpleDateFormat sdf= new SimpleDateFormat(format1);
		long lTime = 0;
		try {
			Date dt2 = sdf.parse(sDt);
			lTime = dt2.getTime();
			print(lTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return lTime;
	}
	
	public static void sendFaultMail(String reprot){
		MailUtils mail = new MailUtils();
		mail.setTo(ReadProperties.ReadProprety("mail.fault.to"));
		mail.setSubject(reprot+" 未正常执行");
		mail.setContent("警告： "+reprot+" 未正常执行！");
		mail.sendMail();
	}
	
	/**
	 * 報表上傳到服務器
	 * @param category 报表类别 键
	 * @param property 報表屬性配置 鍵
	 * @param reportName 报表命名格式 时间格式
	 */
	public static boolean transferToServer(String category,String property,String reportName){
		TransferTheReport transfer = new TransferTheReport();
		String source = ReadProperties.ReadProprety(property);
		String fileName = source.substring(source.lastIndexOf("/")+1);
		fileName = fileName.substring(0, fileName.indexOf("."));
		String suffix = source.substring(source.lastIndexOf("."));
		String dest = ReadProperties.ReadProprety("server.upload.report") + ReadProperties.ReadProprety(category) + fileName + "/"+ TUtil.format(reportName)+suffix;

		return transfer.writeTOsmb(source, dest);
	}
	
	/**
	 * 設置郵件正文
	 * @param subject 主題
	 * @param source 報表目錄
	 * @return 郵件正文
	 */
	public static String getContent(String subject,String source){
		String fileName = source.substring(source.lastIndexOf("/")+1);
		fileName = fileName.substring(0, fileName.indexOf("."));
		String suffix = source.substring(source.lastIndexOf("."));
		String dest = ReadProperties.ReadProprety("server.report.path")+fileName+"/"+TUtil.format("yyyy-MM-dd")+suffix;
		String content ="Dear All:\n\t這是"+subject+"，\n\t如您使用WINDOWS系統，請點擊後面鏈接獲取報表："+dest+"\n\t如您使用MAC系統，請到上述路徑下查閱此報表！\n\n注：該郵件由系統自動發送，請勿回復！\nTracebility 系統";
		return content;
	}
	
	/**
	 * 获取文件创建时间
	 * @param file 文件目录
	 */
	public static String getCreateTime(File file){
//		file = new File("e:/1.xls");
		String date ="";
//		file.lastModified();
		try {
			Process process = Runtime.getRuntime().exec("cmd.exe /c dir " + file.getAbsolutePath() + "/tc");
			InputStream is = process.getInputStream();
			BufferedReader br = new BufferedReader(
			new InputStreamReader(is));
			for (int i = 0; i < 5; i++) {//前五行是其他的信息
				br.readLine();
			}
			String createDateLine = br.readLine();
			StringTokenizer tokenizer = new StringTokenizer(createDateLine);
			date = tokenizer.nextToken() + " " +tokenizer.nextToken();
			br.close();
//			print(date);
		} catch (IOException e) {
			log.error(""+e.getMessage());
		}
		return date;
	}
	
	/**
	 * 获取文件最后修改时间
	 * @param filePath 文件目录
	 */
	public static void getLastModifyTime(File filePath){
		filePath = new File("\\\\10.131.18.8\\rt3生產機種\\ProductionReprot\\TraceAlterReprot-reprot");
		File[] list = filePath.listFiles();
//		for(File file : list){
//			print(file.getAbsolutePath()+"\tcreate time:"+getCreateTime(file));
//		}
		for(File file : list){
			Date date = new Date(file.lastModified());
			print(format(date,"yyyy-MM-dd"));
		}
	}

	public static void getFile (){
		String root = "\\\\10.131.18.8\\rt3生產機種\\ProductionReprot";
		File filePath = new File(root);
		File[] list = filePath.listFiles();
		for(File file : list){
			print(file.getName()+"\t"+new File(file.getAbsolutePath()+"\\"+TUtil.format("yyyy-MM-dd")+".xls").exists());
		}
	}
	
	static void test(){
		String today = TUtil.format("yyyy-MM-dd");
		String dest =ReadProperties.ReadProprety("server.report.path")+"TraceAlterReprot-reprot"+"\\"+today+"\\"; 
		print(dest);
		File dir =new File(dest);//创建当天目录
		if(!dir.exists()){
			dir.mkdir();
		}
	}
	
	/**
	 * 获取随机索引
	 * @param max 最大范围，从0开始
	 * @param count 获取的数量
	 * @return 索引集合
	 */
	public List<Integer> getRandomIndex(int max,double count){
		int[] size = new int[max];
		List<Integer> indexs = new ArrayList<Integer>();
		for(int i=0;i<size.length;i++){
			size[i] = i;
		}
		for(int k = 0;k<count;k++){
			Random random = new Random();
			int num = random.nextInt(size.length);
			if(!indexs.contains(num))
				indexs.add(num);
			else{
				num = random.nextInt(size.length);
				indexs.add(num);
			}
		}
        return indexs;
	}
	
	/**
	 * 获取四舍五入的整数
	 * @param input 乘数
	 * @param rate 比率
	 * @return 取整后的结果
	 */
	public double getRound(int input,double rate){
		double tmp = input * rate;
		return Math.round(tmp);
	}
	
	/**
	 * 获取四舍五入的整数
	 * @param input 乘数
	 * @param rate 比率
	 * @return 取整后的结果
	 */
	public double ceil(int input,double rate){
		double tmp = input * rate;
		return Math.ceil(tmp);
	}
}