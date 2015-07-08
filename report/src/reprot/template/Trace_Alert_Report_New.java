package reprot.template;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import rt.bean.TraceAlterReprotPOJO;
import rt.connection.DBManager;
import rt.excel.PoiExcel2k3Helper;
import rt.excel.PoiExcelHelper;
import rt.util.TUtil;

/**
 * 现场找料报表
 * @author Qiang1_Zhang
 */
public class Trace_Alert_Report_New{
	private static Logger log = Logger.getLogger(Trace_Alert_Report_New.class);
	
	public static void main(String[] args){
		Trace_Alert_Report_New t = new Trace_Alert_Report_New();
		String modelPath = "F://AV9找料模板.xlsx";
		String report = "F://report//"+TUtil.format("yyyy-MM-dd_HH")+".xlsx";
		t.getData(modelPath, report,TUtil.format("yyyy-MM-dd_HH")+".xlsx");
	}
	
	/**
	 * 获取数据
	 * @param modelPath 模板路径
	 * @param report 报表路径
	 */
	public void getData(String modelPath,String report,String fileName){
		PoiExcelHelper helper = new PoiExcel2k3Helper().getPoiExcelHelper(modelPath);
		
		ArrayList<ArrayList<String>> dataList = null;

		Workbook wb = null;
		try {
			wb = new XSSFWorkbook(new FileInputStream(modelPath));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			log.error("没有找到模板文件："+e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			log.error("获取模板文件流出错："+e.getMessage());
		}
		int sheetNum = wb.getNumberOfSheets();
		System.out.println("sheet总数："+sheetNum);
		for(int c = 1;c < sheetNum;c++){
			System.out.println("正在处理第"+(c+1)+"个sheet");
			Sheet sheet = wb.getSheetAt(c);
			
			List<TraceAlterReprotPOJO> list =null ;
			List<TraceAlterReprotPOJO> listTmp = null;
			List<TraceAlterReprotPOJO> tmp = null;
			// 读取A B列数据
			dataList = helper.readExcel(modelPath, c, new String[]{"a","b"});
			for (int i = 2;i < dataList.size();i++){//遍历行数据
				String processName = dataList.get(i).get(0);
				
				DBManager db = new DBManager();
				ResultSet rs = null;
							
				try {
					String sql = "";				
					int param[][] =new int[4][2];
					param[0][0] = 24;  param[0][1] = 0;
					param[1][0] = 72;  param[1][1] = 24;
					param[2][0] = 120; param[2][1] = 72;
					param[3][0] = 120; param[3][1] = 0;
					int index=0;
					list = new ArrayList<TraceAlterReprotPOJO>();
					TraceAlterReprotPOJO traceAlterReprot = null;
					Row row = sheet.getRow(i);

					int total = 0;
					int findCNT = 0;
					for (int[] str: param){//四个区段
						sql = getByg_report_2h_wip(processName, dataList.get(i).get(1), str[0], str[1]);
						System.out.println("sql="+sql);
						Connection conn = db.GetConnection(0);
						rs = conn.createStatement().executeQuery(sql);
						
						int rowCount = 0;
						listTmp = new ArrayList<TraceAlterReprotPOJO>();
						
						while(rs.next()){
							traceAlterReprot = new TraceAlterReprotPOJO();
							traceAlterReprot.setCartonNO(rs.getString("carton_no"));
							traceAlterReprot.setLastTime(rs.getString("last_time"));
							traceAlterReprot.setPdlineName(rs.getString("pdline_name"));
							traceAlterReprot.setProcessName(rs.getString("process_name"));
							traceAlterReprot.setSerialNumber(rs.getString("serial_number"));
							traceAlterReprot.setUserName(rs.getString("user_name"));
							if((120 == str[0]) && (0 == str[1])){
								traceAlterReprot.setTimeZone(">120");
							}else{
								traceAlterReprot.setTimeZone(str[1]+"-"+str[0]);
							}						
							traceAlterReprot.setCurrentStatus(rs.getString("current_status"));
							rowCount++;
							listTmp.add(traceAlterReprot);
						}
						rs.close();
						conn.close();
						row.createCell(index+2).setCellValue(rowCount);//写入每个区段总数量
						total += rowCount;
//						System.out.println("index="+index+"\trowCount="+rowCount);
						tmp = new ArrayList<TraceAlterReprotPOJO>();
						TUtil util = new TUtil();
						//随机计算
						switch (index){
							case 1 :{
								double random = util.ceil(rowCount, 0.1);
								findCNT += random;
								List<Integer> l = util.getRandomIndex(rowCount, random);
								for(int j = 0;j<l.size();j++){
									tmp.add(listTmp.get(l.get(j)));
								}
								break;
							}
							case 2 :{
								double random = util.ceil(rowCount, 0.8);
								findCNT += random;
								List<Integer> l = util.getRandomIndex(rowCount, random);
								for(int j = 0;j<l.size();j++){
									tmp.add(listTmp.get(l.get(j)));
								}
								break;
							}
							case 3 :{
								findCNT += listTmp.size();
								list.addAll(listTmp);
								break;
							}
							default :{
								break;
							}
						}
						index++;
						list.addAll(tmp);
					}//四个区段完毕
					row.createCell(6).setCellValue(total);//滞留数量
					row.createCell(8).setCellValue(findCNT);//找料数量
					
					if(findCNT > 0){//有数量，才创建sheet
						createSheet(wb,list);
						//获取当前workbook的sheet总数，以此+1命名要新创建的sheet
						int sheetCNT = wb.getNumberOfSheets();
						//创建超链接
						row.getCell(7).setCellFormula(("HYPERLINK(\"["+fileName+"]'Sheet"+sheetCNT+"'!A1\",\"找料明细\")"));
						//创建超链接样式
						CellStyle linkStyle = wb.createCellStyle();
						Font cellFont= wb.createFont();
						cellFont.setUnderline((byte) 1);
						cellFont.setColor(HSSFColor.BLUE.index);
						linkStyle.setFont(cellFont);
						//为超链接单元格添加样式
						row.getCell(7).setCellStyle(linkStyle);
					}
					wb.setActiveSheet(c);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			wb.getSheetAt(c).setForceFormulaRecalculation(true);
		}
		try {
			FileOutputStream out = new FileOutputStream(report);
			wb.write(out);
			out.close();
			wb.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			log.error("生成报表时找不到路径："+e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			log.error("输出报表流时出错："+e.getMessage());
		}
	}
	
	/**
	 * 非清洗站的制程
	 * @param String process_name 制程名称
	 * @param String asafeTimefeTime 安全时间
	 * @param int hour1 时间2
	 * @param int hour2 时间1
	 */
	public String getByg_report_2h_wip(String process_name,String safeTime,int hour2,int hour1){
		String sql ="select g.process_name process_name,g.serial_number serial_number,g.user_name user_name,g.last_time last_time,g.current_status current_status,g.pdline_name pdline_name,s.carton_no carton_no"+
		  " from sajet.g_report_2h_wip g"+
		  " left join sajet.g_sn_status s on s.serial_number = g.serial_number"+
		  " where process_name = '"+process_name+"'";
		if (hour2 == 120 && hour1 ==0){
			sql +=  " and LAST_TIME < to_date('"+TUtil.format("yyyy-MM-dd HH")+"', 'YYYY-MM-DD HH24') - ('"+safeTime+"' +120)/ 24";
		}else{
			sql += " and LAST_TIME >= to_date('"+TUtil.format("yyyy-MM-dd HH")+"', 'YYYY-MM-DD HH24') - ('"+safeTime+"' + "+hour2+") / 24";
			sql +=  " and LAST_TIME < to_date('"+TUtil.format("yyyy-MM-dd HH")+"', 'YYYY-MM-DD HH24') - ('"+safeTime+"' + "+hour1+")/ 24";
		}
		return sql;
	}
	
	/**
	 * 清洗、重工站的SQL
	 * @param process_name 制程名字
	 * @param safeTime 安全时间
	 * @param hour2 时间2
	 * @param hour1 时间1
	 * @return SQL
	 */
//	public String getCleanSQL(String process_name,String safeTime,int hour2,int hour1){
//		String model = process_name.substring(0,8);
//		String process = process_name.substring(10);
//		String sql="select d.process_name process_name,c.serial_number serial_number,f.emp_name user_name,c.out_process_time last_time,c.current_status current_status,c.pdline_id pdline_id,c.carton_no carton_no"+
//		 " from sajet.g_sn_status@SCANDB a"+
//		 " inner join sajet.sys_process@SCANDB b on a.process_id = b.process_id"+
//		 " inner join sajet.g_sn_status c on c.serial_number = a.serial_number"+
//		 " inner join sajet.sys_process d on c.process_id = d.process_id"+
//		 " inner join sajet.sys_part e on c.model_id = e.part_id"+
//		 " inner join sajet.sys_emp@SCANDB f on a.emp_id = f.emp_id"+
//		 " inner join sajet.sys_pdline@SCANDB g on g.pdline_id = a.pdline_id"+
//		 " where b.process_name = '"+process+"'";
//
//		if (hour2 == 12 && hour1 ==0){
//			sql +=  " and a.out_process_time < to_date('"+TUtil.format("yyyy-MM-dd HH")+"', 'YYYY-MM-DD HH24') - ('"+safeTime+"' +12)/ 24";
//		}else{
//			sql += " and a.out_process_time >= to_date('"+TUtil.format("yyyy-MM-dd HH")+"', 'YYYY-MM-DD HH24') - ('"+safeTime+"' + "+hour2+") / 24";
//			sql +=  " and a.out_process_time < to_date('"+TUtil.format("yyyy-MM-dd HH")+"', 'YYYY-MM-DD HH24') - ('"+safeTime+"' + "+hour1+")/ 24";
//		}
//
//	   sql += " and e.model_name = '"+model+"'"+
//	   " and d.process_name not in"+
//	       " ('LincolnWB Barcode Check', 'LincolnCB Barcode Check')";
//
//		return sql;
//	}
	
	/**
	 * 创建sheet
	 * @param wb workBook对象
	 * @param list sheet中要输入的数据集合
	 */
	public void createSheet(Workbook wb,List<TraceAlterReprotPOJO> list){
		Sheet sheet = null;
		Row row = null;
		sheet = wb.createSheet("Sheet"+(wb.getNumberOfSheets()+1));//创建新sheet，名字sheet加序号
		row = sheet.createRow(0);
		createColumnTitle(row);
		for(int i=0;i<list.size();i++){
			row = sheet.createRow(i+1);
			row.createCell(0).setCellValue(list.get(i).getProcessName());
			row.createCell(1).setCellValue(list.get(i).getSerialNumber());
			row.createCell(2).setCellValue(list.get(i).getUserName());
			row.createCell(3).setCellValue(list.get(i).getLastTime());
			String status = list.get(i).getCurrentStatus();
			status = status.equals("0") ? "OK" : "NG";
			row.createCell(4).setCellValue(status);
			row.createCell(5).setCellValue(list.get(i).getPdlineName());
			row.createCell(6).setCellValue(list.get(i).getCartonNO());
			row.createCell(7).setCellValue(list.get(i).getTimeZone());
		}
	}
	
	/**
	 * 创建标题列
	 * @param row 标题行
	 */
	public void createColumnTitle(Row row){
		row.createCell(0).setCellValue("製程");
		row.createCell(1).setCellValue("序列號");
		row.createCell(2).setCellValue("責任人");
		row.createCell(3).setCellValue("最後出現時間");
		row.createCell(4).setCellValue("狀態");
		row.createCell(5).setCellValue("線別");
		row.createCell(6).setCellValue("箱號");
		row.createCell(7).setCellValue("超時區段(小时)");		
	}
}