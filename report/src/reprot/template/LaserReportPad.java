package reprot.template;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import rt.connection.DBManager;
import rt.excel.PoiExcel2k3Helper;
import rt.excel.PoiExcelHelper;
//import rt.util.TUtil;
//import rt.util.TUtil;

public class LaserReportPad {
	public static void main(String[] args){
//		String t = "D:/LaserOutputReport_Venice.xlsx";
//		LaserReportPad n = new LaserReportPad();
//		n.expLaserOutput(t, "d://venice.xlsx", "'604-01104','604-01213'", "'VeniceWB Barcode Check','VeniceCB Barcode Check'", 2);
		//LaserOutputReport_Tiger604-00690,604-00691
		String t = "D:/LaserOutputReport_Tiger.xlsx";
		LaserReportPad n = new LaserReportPad();
		n.expLaserOutput(t, "d://Tiger.xlsx", "'604-00690','604-00691'", "'Barcode Check'", 6);

//		String t = "f://AV4镭雕投入产出报表模板.xlsx";
//		LaserReportNew n = new LaserReportNew();
//		n.expLaserOutput(t, "f://AV4镭雕投入产出报表.xlsx", "613-1325,604-4275", "AV4 TC Barcode Check,AV4 DH Barcode Check", 6);

	}
	
	public static Logger log = Logger.getLogger(LaserReportPad.class);
	DBManager dbManger = null;
	Connection connection = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;

	/**
	 * 生成报表
	 * @param template 模板路径
	 * @param reprot 报表路径
	 * @param part_no 料号
	 * @param process 镭雕制程名称
	 */
	public void expLaserOutput(String template, String reprot, String part_no, String process, int safeTime){
		PoiExcelHelper helper = new PoiExcel2k3Helper();
		
		// 读取数据
		ArrayList<ArrayList<String>> dataList = null;
		//工作区  
		XSSFWorkbook wb = null;
		//输入流
		InputStream ins =null;
		//输出流
		FileOutputStream out = null;
		XSSFSheet sheet= null;
		XSSFSheet sheetNew= null;
		XSSFRow row = null;
		XSSFRow rowNew = null;

		try {
			ins = new FileInputStream(template);//读取模板
			wb = new XSSFWorkbook(ins);//转换为工作区对象
		} catch (FileNotFoundException e) {
			log.error(""+e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			log.error("读入模板错误："+e.getMessage());
			e.printStackTrace();  
		}

		//循环处理sheet
			dataList = helper.readExcel(template, 0, new String[]{"a","c"});
			List<Map<String,Integer>> cntMap = wipDesc(part_no, process, safeTime);
//			TUtil.print(cntMap.size());
			for(int k=0;k<cntMap.size();k++){//
				for(int j=1;j<dataList.size();j++){
					if(cntMap.get(k).get(dataList.get(j).get(0)+""+dataList.get(j).get(1)) != null){
						sheet = wb.getSheetAt(0);//取要写值的sheet
						row = sheet.getRow(j);//要写值的行
						row.getCell(3).setCellValue(cntMap.get(k).get(dataList.get(j).get(0)+""+dataList.get(j).get(1)));//滞留总数					
						sheet.setForceFormulaRecalculation(true);
					}
				}
			}

		//写明细
		List<Map<String,String>> cntMapInput = inputDesc(part_no, process, safeTime);
		
		sheetNew = wb.createSheet(wb.getSheetName(0)+"明细");
		rowNew = sheetNew.createRow(0);
		rowNew.createCell(0).setCellValue("产品序列号");
		rowNew.createCell(1).setCellValue("当前停留制程");
		rowNew.createCell(2).setCellValue("当前停留站点");
		rowNew.createCell(3).setCellValue("最后扫描时间");
		for(int j=0; j<cntMapInput.size(); j++){
			rowNew = sheetNew.createRow(j+1);

			rowNew.createCell(0).setCellValue(cntMapInput.get(j).get("serial_number"));
			rowNew.createCell(1).setCellValue(cntMapInput.get(j).get("process_name"));
			rowNew.createCell(2).setCellValue(cntMapInput.get(j).get("terminal_name"));
			rowNew.createCell(3).setCellValue(cntMapInput.get(j).get("out_process_time"));
		}
		sheetNew.autoSizeColumn(0);
		sheetNew.autoSizeColumn(1);
		sheetNew.autoSizeColumn(2);
		sheetNew.autoSizeColumn(3);

		//制程清单
		
		try {
			out = new FileOutputStream(reprot);
			wb.write(out);
			out.flush();
			out.close();
			ins.close();
			wb.close();
		} catch (FileNotFoundException e) {
			log.error("未找到生成报表的路径"+e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			log.error("生成报表时输出流出错："+e.getMessage());
			e.printStackTrace();
		}
	}
	
	//各制程滞留数统计
	public List<Map<String,Integer>> wipDesc(String part_no, String process, int safeTime){
		//按制程汇总
		StringBuffer sql = new StringBuffer("select count(p.process_name) cnt, p.process_name,t.part_no"+
				" from sajet.g_sn_status s"+
				" inner join sajet.sys_process p on s.process_id = p.process_id"+
				" inner join sajet.sys_part t on s.model_id = t.part_id"+
				" where s.serial_number in"+
				" (select a.serial_number"+
				" From sajet.g_sn_travel a,"+
				" sajet.sys_process      b,"+
				" sajet.sys_part         c,"+
				" sajet.sys_route_detail d,"+
				" sajet.g_wo_base        g,"+
				" sajet.g_sn_status      k"+
				" where a.process_id = b.process_id"+
				" and a.serial_number = k.serial_number"+
				" and c.part_no in ("+part_no+") and k.model_id = c.part_id"+
				" and d.route_id = c.route_id"+
				" and k.ENABLED is null"+
				" and b.process_id = d.next_process_id"+
				" and a.work_order = g.work_order"+
				" and b.process_name in ("+process+")  and a.out_process_time >= to_date(to_char(sysdate - "+safeTime+", 'yyyy-mm-dd') || '08:00','yyyy-mm-dd hh24:mi')"+
				" and a.out_process_time < to_date(to_char(sysdate - "+(safeTime-1)+", 'yyyy-mm-dd') || '08:00','yyyy-mm-dd hh24:mi')"+
				" group by a.serial_number, b.process_name, g.wo_type, d.seq )"+
				" group by p.process_name,t.part_no");
//		TUtil.print(sql.toString());
//		log.info(sql.toString());
		List<Map<String,Integer>> cntMap = new ArrayList<Map<String,Integer>>();
		try {
			dbManger = new DBManager();
			connection = dbManger.GetConnection();
			rs = connection.createStatement().executeQuery(sql.toString());
			
			while(rs.next()){
				Map<String,Integer> map = new HashMap<String,Integer>();
				map.put(rs.getString("part_no")+""+rs.getString("process_name"), rs.getInt("cnt"));
				cntMap.add(map);
			}
		} catch (SQLException e) {
			log.error("查询镭雕投入数异常："+e.getMessage());
			return null;
		}finally{
			dbManger.closeConnection(connection, rs, pstmt);
		}
		return cntMap;
		
	}
	
	/**
	 * 投料清单
	 * @param part_no 料号
	 * @param process 制程
	 * @param safeTime 时间
	 */
	public List<Map<String,String>> inputDesc(String part_no, String process, int safeTime){
		//投料明细
		StringBuffer sql1 = new StringBuffer("select s.serial_number,"+
				" p.process_name,"+
				" t.terminal_name,"+
				" to_char(s.out_process_time, 'yyyy-mm-dd hh24:mi:ss') out_process_time,part.model_name"+
				" from sajet.g_sn_status s"+
				" inner join sajet.sys_route_detail d on s.process_id = d.next_process_id  and s.route_id = d.route_id"+// and s.route_id = d.route_id
				" inner join sajet.sys_process p on p.process_id = d.next_process_id"+// and p.process_name='"+process2+"'
				" inner join sajet.sys_terminal t on s.terminal_id = t.terminal_id"+
				" inner join sajet.sys_part part on s.model_id = part.part_id"+
				" where s.serial_number in"+
				" (select a.serial_number"+
				" From sajet.g_sn_travel a,"+
				" sajet.sys_process      b,"+
				" sajet.sys_part         c,"+
				" sajet.sys_route_detail d,"+
				" sajet.g_wo_base        g,"+
				" sajet.g_sn_status      k"+
				" where a.process_id = b.process_id"+
				" and a.serial_number = k.serial_number"+
				" and c.part_no in ("+part_no+") and k.model_id = c.part_id"+
				" and d.route_id = c.route_id"+
				" and k.ENABLED is null"+
				" and b.process_id = d.next_process_id"+
				" and a.work_order = g.work_order"+
						" and b.process_name in ("+process+") and a.out_process_time >= to_date(to_char(sysdate - "+safeTime+", 'yyyy-mm-dd') || '08:00','yyyy-mm-dd hh24:mi')"+
				" and a.out_process_time < to_date(to_char(sysdate - "+(safeTime-1)+", 'yyyy-mm-dd') || '08:00','yyyy-mm-dd hh24:mi')"+
				" group by a.serial_number, b.process_name, g.wo_type, d.seq)"+
				" order by d.seq");
		//TUtil.print(sql1.toString());
		List<Map<String,String>> inputs = new ArrayList<Map<String,String>>();
		Map<String,String> map = null;
		try {
			dbManger = new DBManager();
			connection = dbManger.GetConnection();
			rs = connection.createStatement().executeQuery(sql1.toString());
			while(rs.next()){
				map = new HashMap<String,String>();
				map.put("serial_number", rs.getString("serial_number"));
				map.put("process_name", rs.getString("process_name"));
				map.put("terminal_name", rs.getString("terminal_name"));
				map.put("out_process_time", rs.getString("out_process_time"));
				map.put("model_name", rs.getString("model_name"));
				inputs.add(map);
			}
		} catch (SQLException e) {
			log.error("查询投料清单异常："+e.getMessage());
			return null;
		}finally{
			dbManger.closeConnection(connection, rs, pstmt);
		}
		return inputs;
	}
}