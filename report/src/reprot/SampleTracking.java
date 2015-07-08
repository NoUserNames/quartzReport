package reprot;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import rt.bean.TimeOut;
import rt.connection.DBManager;
import rt.util.ReadProperties;
import rt.util.TUtil;

/**
 * @author Qiang1_Zhang样品追踪与转借记录报表  按机种分sheet
 */
public class SampleTracking implements Job{
	private static Logger log = Logger.getLogger(SampleTracking.class);

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		log.info("样品还回后未流动报表开始");
		String source = ReadProperties.ReadProprety("sampleTracking.reprot");
		new SampleTracking().getData(TUtil.getURL()+source);
		log.info("样品还回后未流动报表生成完毕");
		if(!TUtil.transferToServer("sampleTracking.derectory","sampleTracking.reprot","yyyy-MM-dd")){
			TUtil.sendFaultMail(this.getClass().getName());
			log.error("样品还回报表上传失败");
		}else
			log.info("样品还回报表上传完毕");
	}
	
	/**
	 * 生成報表
	 * @param FileOutPath 報表路徑
	 */
	public void getData(String FileOutPath){
		String sql ="select distinct(tmp.serial_number)serial_number,tmp.emp_name,tmp.emp_no,hr.dept_name,tmp.terminal_name,tmp.lastScanTime,tmp.timeouts,p.model_name from ("+
		" select distinct(s.serial_number),"+
		       " e.emp_name,"+
		       " e.emp_no,"+
		       " t.terminal_name,"+
		       " to_char(s.out_process_time, 'yyyy-mm-dd hh24:mi:ss') lastScanTime,"+
		       " round((sysdate - cast(s.out_process_time as date)) * 24, 1) timeouts,"+
		       " s.model_id"+
		  " from (select serial_number from ("+
		       " select serial_number,status,exp_time,"+
		       " row_number() over(partition by serial_number order by exp_time desc) indexs"+
		       " from sajet.g_ht_at1_status"+
		       " group by serial_number, status, exp_time)where indexs =1 and status =1) a,"+
		       "sajet.g_sn_status     s,"+
		       "sajet.sys_emp         e,"+
		       "sajet.sys_terminal    t"+
		 " where s.terminal_id in"+
		       " (select t.terminal_id"+
		          " from sajet.sys_terminal t"+
		         " where t.process_id in"+
		               " (select process_id"+
		                  " from sajet.sys_process t"+
		                 " where t.process_name like '%Product tracking%'"+
		                   " and t.process_name not like '%Rio%'))"+
		   " and a.serial_number = s.serial_number"+
		  // " and a.status <> 2"+
		   //" and s.carton_no ='N/A'"+
		   " and s.emp_id = e.emp_id"+
		   " and s.terminal_id = t.terminal_id"+
		      " and s.out_process_time >="+
		       " to_date('2014-01-01 00:00', 'yyyy-mm-dd hh24:mi'))tmp,sajet.sys_hr_emp hr,sajet.sys_part p"+
		       " where tmp.emp_no = hr.emp_no"+
		       " and tmp.model_id = p.part_id ";
		DBManager db = new DBManager();
		Connection conn = db.GetConnection(0);
		ResultSet rs = null;
		List<String> dep = new ArrayList<String>();
		List<TimeOut> strTimeout = null;
		try {
			rs = conn.createStatement().executeQuery(sql);
			strTimeout = new ArrayList<TimeOut>();
			
			TimeOut timeout = null;
			while(rs.next()){				
				timeout = new TimeOut();
				timeout.setModel_name(rs.getString("model_name"));
				timeout.setDept_name(rs.getString("dept_name"));
				timeout.setEmp_name(rs.getString("emp_name"));
				timeout.setEmp_no(rs.getString("emp_no"));
				timeout.setLastscantime(rs.getString("lastScanTime"));
				timeout.setSerial_number(rs.getString("serial_number"));
				timeout.setTerminal_name(rs.getString("terminal_name"));
				timeout.setTimeouts(rs.getFloat("timeouts")/24);
				strTimeout.add(timeout);
				dep.add(rs.getString("model_name"));
			}
			db.closeConnection(conn, rs);
		} catch (SQLException e) {
			log.error("執行查詢報表SQL出錯："+e.getMessage());
		}
		List<String> listModelName = new LinkedList<String>();//不重复的部门名称
		for(int i=0;i<dep.size();i++){
			if(!listModelName.contains(dep.get(i))){
				listModelName.add(dep.get(i));
			}
		}
		
		List<TimeOut> listA = new ArrayList<TimeOut>();
		List<TimeOut> listAV9 = new ArrayList<TimeOut>();
		List<TimeOut> listBV = new ArrayList<TimeOut>();
		List<TimeOut> listE5A = new ArrayList<TimeOut>();
		List<TimeOut> listMILAN = new ArrayList<TimeOut>();
		List<TimeOut> listLICOLN = new ArrayList<TimeOut>();
		List<TimeOut> listPARIS = new ArrayList<TimeOut>();
		List<TimeOut> listVenice = new ArrayList<TimeOut>();
		List<TimeOut> listHulK = new ArrayList<TimeOut>();
		List<TimeOut> listTiger = new ArrayList<TimeOut>();
		List<TimeOut> listBentley = new ArrayList<TimeOut>();
		
		for (int j = 0; j < strTimeout.size(); j++) {
			for (String str : listModelName) {
				if (str.equals(strTimeout.get(j).getModel_name())) {
					if (str.startsWith("A")) {
						if(str.indexOf("AV4") != -1){
							listA.add(strTimeout.get(j));
						}
						if(str.indexOf("AV9") != -1){
							listAV9.add(strTimeout.get(j));
						}
					}else if (str.startsWith("B")) {
						if(str.indexOf("BV") != -1){
							listBV.add(strTimeout.get(j));
						}
						if(str.indexOf("Ben") != -1){
							listBentley.add(strTimeout.get(j));
						}						
					}else if (str.startsWith("E")) {						
						listE5A.add(strTimeout.get(j));
					}else if (str.startsWith("M")) {
						listMILAN.add(strTimeout.get(j));
					}else if (str.startsWith("Lin")) {						
						listLICOLN.add(strTimeout.get(j));
					}else if (str.startsWith("P")) {						
						listPARIS.add(strTimeout.get(j));
					}else if (str.startsWith("V")){
						listVenice.add(strTimeout.get(j));
					}else if (str.startsWith("H")){
						listHulK.add(strTimeout.get(j));	
					}else if (str.startsWith("T")){
						listTiger.add(strTimeout.get(j));
					}
				}
			}
		}
		//2015.4.8 Seven_Chen Update
	 /*	HSSFWorkbook wb1 = new HSSFWorkbook();
		String str="AV4,AV9,BV,E5A,MILAN,LICLON,PARIS,VENICE,HULK";
		StringTokenizer st = new StringTokenizer(str, ",");
		int strLeng = st.countTokens();
		String[] strArray = new String[st.countTokens()];
		for(int i=0;i<strLeng;i++){
		   strArray[i]=st.nextToken();  
		   createSheet(wb1,listA,strArray[i]);
		}*/
	    ///////////	
		XSSFWorkbook wb = new XSSFWorkbook();
		final String AV4 ="AV4",AV9="AV9", B ="BV",E ="E5A", M ="MILAN", L ="LICLON", P ="PARIS",V="VENICE",H="HULK",T="Tiger",Ben="Bentley";
		
		createSheet(wb,listA,AV4);
		createSheet(wb,listAV9,AV9);
		createSheet(wb,listBV,B);
		createSheet(wb,listE5A,E);
		createSheet(wb,listMILAN,M);
		createSheet(wb,listLICOLN,L);
		createSheet(wb,listPARIS,P);
		createSheet(wb,listVenice,V);
		createSheet(wb,listHulK,H);
		createSheet(wb,listTiger,T);
		createSheet(wb,listBentley,Ben);
		try {
			FileOutputStream out = new FileOutputStream(FileOutPath);
			wb.write(out);
			out.close();
		} catch (FileNotFoundException e) {
			log.error("找不到输入目录："+e.getMessage());
		} catch (IOException e) {
			log.error("输入报表时出错："+e.getMessage());
		}
	}

	/**
	 * 创建sheet
	 * @param HSSFWorkbook wb 工作簿
	 * @param List<TimeOut> strTimeout 数据集合
	 * @param String sheetName sheet名称
	 */
	public void createSheet(XSSFWorkbook wb,List<TimeOut> strTimeout,String sheetName){
		XSSFSheet sheet = null;
		XSSFRow row = null;
		sheet = wb.createSheet(sheetName);//以机种名创建新sheet
		row = sheet.createRow(0);
		createColumn(row);
		for(int i=0;i<strTimeout.size();i++){						
			//创建行
			row = sheet.createRow(i+1);

			//依次创建列
			row.createCell(0).setCellValue(strTimeout.get(i).getModel_name());
			row.createCell(1).setCellValue(strTimeout.get(i).getSerial_number());
			row.createCell(2).setCellValue(strTimeout.get(i).getEmp_no());
			row.createCell(3).setCellValue(strTimeout.get(i).getEmp_name());
			row.createCell(4).setCellValue(strTimeout.get(i).getDept_name());
			row.createCell(5).setCellValue(strTimeout.get(i).getTerminal_name());
			row.createCell(6).setCellValue(strTimeout.get(i).getLastscantime());
			float out = strTimeout.get(i).getTimeouts();
			if(out <=3){
				row.createCell(7).setCellValue("0~3");
			}
			if(out>3 && out <=10){
				row.createCell(7).setCellValue("3~10");
			}
			if(out>10 && out <=30){
				row.createCell(7).setCellValue("10~30");
			}
			if(out > 30){
				row.createCell(7).setCellValue(">30");
			}
		}		
	}
	
	/**
	 * 創建首行標題欄
	 * @param row 行對象
	 */
	public void createColumn(XSSFRow row){
		row.createCell(0).setCellValue("幾種");
		row.createCell(1).setCellValue("產品SN");
		row.createCell(2).setCellValue("責任人工號");
		row.createCell(3).setCellValue("掃描責任人");
		row.createCell(4).setCellValue("責任人單位");
		row.createCell(5).setCellValue("樣品還回滯留站點");
		row.createCell(6).setCellValue("最後掃描時間");
		row.createCell(7).setCellValue("超時區段");
	}
}