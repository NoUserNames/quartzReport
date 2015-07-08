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
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import rt.connection.*;
import rt.excel.PoiExcel2k3Helper;
import rt.excel.PoiExcelHelper;
import rt.util.*;
import rt.bean.*;

/**
 * @author 张强
 * WIP报表
 */
public class WIPStageReport {
	static Logger log = Logger.getLogger((WIPStageReport.class));
	public static void main(String[] args){
		String filePath = "F:\\TemplateAV9.xls";
		String exportPath = "F:/report/av9.xls";
		TUtil.print(TUtil.format("HH:mm:ss"));
		new WIPStageReport().ExportExcel(filePath,exportPath,"AV9");
		TUtil.print(TUtil.format("HH:mm:ss"));
	}
	
	public List<WIP> GetData(String modelPath,String part_no){
		String filePath = modelPath;
		PoiExcelHelper helper = new PoiExcel2k3Helper();
		// 读取A/B两列数据
		ArrayList<ArrayList<String>> dataList = helper.readExcel(filePath, 0, new String[]{"c"});
		//0~3
		String sSday1 = TUtil.GetDay(-3);
		String sEday1 = TUtil.GetDay(0);
		//3~10
		String sSday2 = TUtil.GetDay(-10);
		String sEday2 = TUtil.GetDay(-3);
		//>30
		String sSday3 = TUtil.GetDay(-30);
		
		List<WIP> listWIPStage = new ArrayList<WIP>();
		WIP wip = null;
		DBManager dbManger = null;
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		
		for(int i = 2; i< dataList.size();i++){
			String sql="select * from"+
				" (select count(iwipcount) - count(decode(iwipcount, 0, 0)) iwipcount1,"+
				" count(ingcount) - count(decode(ingcount, 0, 0)) ingcount1,"+
				" count(iscarpcount) - count(decode(iscarpcount, 0, 0)) iscarpcount1"+
				" from (select count(1) iwipcount, nvl(sum(s.current_status), 0) ingcount, nvl(sum(s.work_flag), 0) iscarpcount"+
				" from sajet.g_sn_status s, sajet.g_wo_base c,sajet.sys_part p"+
				" where s.work_order = c.work_order and s.enabled is null and s.model_id = p.part_id"+
				" and p.part_no in (select part_no from sajet.sys_part where model_name like ?)"+
				"and c.wo_type <> 'DOE'"+
				" and s.process_id =(select process_id from sajet.sys_process where process_name = ? and enabled = 'Y')"+
				" and s.out_process_time >= to_date(?, ' YYYY - MM - DD HH24 ') and s.out_process_time <= to_date(?, ' YYYY - MM - DD HH24 ')"+
				" group by s.serial_number))b, "+
				
				" (select count(iwipcount) - count(decode(iwipcount, 0, 0)) iwipcount2,count(ingcount) - count(decode(ingcount, 0, 0)) ingcount2, count(iscarpcount) - count(decode(iscarpcount, 0, 0)) iscarpcount2"+
				" from (select count(1) iwipcount,nvl(sum(s.current_status), 0) ingcount, nvl(sum(s.work_flag), 0) iscarpcount"+
				" from sajet.g_sn_status s, sajet.g_wo_base c,sajet.sys_part p"+
				" where s.work_order = c.work_order and s.enabled is null and s.model_id = p.part_id"+
				" and p.part_no in (select part_no from sajet.sys_part where model_name like ?)"+
				" and c.wo_type <> 'DOE'"+
				" and s.process_id =(select process_id from sajet.sys_process where process_name = ? and enabled = 'Y')"+
				" and s.out_process_time >= to_date(?, ' YYYY - MM - DD HH24 ') and s.out_process_time <= to_date(?, ' YYYY - MM - DD HH24 ')"+
				" group by s.serial_number))b2,"+
				
				" (select count(iwipcount) - count(decode(iwipcount, 0, 0)) iwipcount3, count(ingcount) - count(decode(ingcount, 0, 0)) ingcount3, count(iscarpcount) - count(decode(iscarpcount, 0, 0)) iscarpcount3"+
				" from (select count(1) iwipcount, nvl(sum(s.current_status), 0) ingcount, nvl(sum(s.work_flag), 0) iscarpcount"+
				" from sajet.g_sn_status s, sajet.g_wo_base c,sajet.sys_part p"+
				" where s.work_order = c.work_order and s.enabled is null and s.model_id = p.part_id"+
				" and p.part_no in (select part_no from sajet.sys_part where model_name like ?)"+
				" and c.wo_type <> 'DOE'"+
				" and s.process_id =(select process_id from sajet.sys_process where process_name = ? and enabled = 'Y')"+
				" and s.out_process_time >= to_date(?, ' YYYY - MM - DD HH24 ') and s.out_process_time < to_date(?, ' YYYY - MM - DD HH24 ')"+
				" group by s.serial_number))b3,"+
				
				" (select count(iwipcount) - count(decode(iwipcount, 0, 0)) iwipcount4, count(ingcount) - count(decode(ingcount, 0, 0)) ingcount4,count(iscarpcount) - count(decode(iscarpcount, 0, 0)) iscarpcount4"+
				" from (select count(1) iwipcount, nvl(sum(s.current_status), 0) ingcount, nvl(sum(s.work_flag), 0) iscarpcount"+
				" from sajet.g_sn_status s, sajet.g_wo_base c,sajet.sys_part p"+
				" where s.work_order = c.work_order and s.enabled is null and s.model_id = p.part_id"+
				" and p.part_no in (select part_no from sajet.sys_part where model_name like ?)"+
				" and c.wo_type <> 'DOE'"+
				" and s.process_id =(select process_id from sajet.sys_process where process_name = ? and enabled = 'Y')"+
				" and s.out_process_time < to_date(?, ' YYYY - MM - DD HH24 ') and s.out_process_time >= to_date('2014-01-01 00', ' YYYY - MM - DD HH24 ')"+
				" group by s.serial_number))b4";

			try {
				dbManger = new DBManager();
				conn = dbManger.GetConnection(0);
				pstmt = conn.prepareStatement(sql);
				String processName = dataList.get(i).get(0);
				pstmt.setString(1, "%"+part_no+"%");
				pstmt.setString(2, processName);
				pstmt.setString(3, sSday1+" 08");
				pstmt.setString(4, sEday1+" 08");
				//3-10
				pstmt.setString(5, "%"+part_no+"%");
				pstmt.setString(6, processName);
				pstmt.setString(7, sSday2+" 08");
				pstmt.setString(8, sEday2+" 08");
				//10-30
				pstmt.setString(9, "%"+part_no+"%");
				pstmt.setString(10, processName);
				pstmt.setString(11, sSday3+" 08");
				pstmt.setString(12, sSday2+" 08");
				//>30
				pstmt.setString(13, "%"+part_no+"%");
				pstmt.setString(14, processName);
				pstmt.setString(15, sSday3+" 08");

				rs = pstmt.executeQuery();
				pstmt.clearParameters();
				while (rs.next()){
					wip = new WIP();
					wip.setIwipcount0_3(rs.getInt("iwipcount1"));
					wip.setIngcount0_3(rs.getInt("ingcount1"));
					wip.setIscarpcount0_3(rs.getInt("iscarpcount1"));
					if (wip.getIngcount0_3() >= wip.getIscarpcount0_3()){
						wip.setIngcount0_3(wip.getIngcount0_3() - wip.getIscarpcount0_3());
						wip.setIokcount0_3(wip.getIwipcount0_3() - wip.getIngcount0_3()-wip.getIscarpcount0_3());
					}else
						wip.setIokcount0_3(wip.getIwipcount0_3() - wip.getIscarpcount0_3());
					
					wip.setIwipcount3_10(rs.getInt("iwipcount2"));
					wip.setIscarpcount3_10(rs.getInt("iscarpcount2"));
					wip.setIngcount3_10(rs.getInt("ingcount2"));
					if (wip.getIngcount3_10() >= wip.getIscarpcount3_10()){
						wip.setIngcount3_10(wip.getIngcount3_10() - wip.getIscarpcount3_10());
						wip.setIokcount3_10(wip.getIwipcount3_10() - wip.getIngcount3_10()-wip.getIscarpcount3_10());
					}else
						wip.setIokcount3_10(wip.getIwipcount3_10() - wip.getIscarpcount3_10());
					
					wip.setIwipcount10_30(rs.getInt("iwipcount3"));
					wip.setIscarpcount10_30(rs.getInt("iscarpcount3"));
					wip.setIngcount10_30(rs.getInt("ingcount3"));
					if (wip.getIngcount10_30() >= wip.getIscarpcount10_30()){
						wip.setIngcount10_30(wip.getIngcount10_30() - wip.getIscarpcount10_30());
						wip.setIokcount10_30(wip.getIwipcount10_30() - wip.getIngcount10_30()-wip.getIscarpcount10_30());
					}else
						wip.setIokcount10_30(wip.getIwipcount10_30() - wip.getIscarpcount10_30());
										
					wip.setIwipcountbl30(rs.getInt("iwipcount4"));
					wip.setIscarpcountbl30(rs.getInt("iscarpcount4"));
					wip.setIngcountbl30(rs.getInt("ingcount4"));
					if (wip.getIngcountbl30() >= wip.getIscarpcountbl30()){
						wip.setIngcountbl30(wip.getIngcountbl30() - wip.getIscarpcountbl30());
						wip.setIokcountbl30(wip.getIwipcountbl30() - wip.getIngcountbl30()-wip.getIscarpcountbl30());
					}else
						wip.setIokcountbl30(wip.getIwipcountbl30() - wip.getIscarpcountbl30());
					
					listWIPStage.add(wip);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}finally{
				dbManger.closeConnection(conn, rs, pstmt);
			}
		}
		return listWIPStage;
	}
	
	public boolean ExportExcel(String fileName,String exportPath,String part_no){
		InputStream ins = null;
		HSSFWorkbook wb = null;
		try {
			ins = new FileInputStream(fileName);
			wb = new HSSFWorkbook(ins);
		} catch (FileNotFoundException e2) {
			e2.printStackTrace();
			log.error("没有找到模板文件："+fileName+",原因是："+e2.getMessage());
			return false;
		}catch (IOException e) {
			e.printStackTrace();
			log.error("处理文件"+exportPath+"时出错,原因是："+e.getMessage());
			return false;
		}finally{
			try {
				wb.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		HSSFSheet sheet = wb.getSheetAt(0);
		
		List<WIP> listWIP = GetData(fileName,part_no);
		int rowNum = listWIP.size();
		HSSFRow row = null;
		row = sheet.getRow(0);
		row.createCell(2).setCellValue(TUtil.format("yyyy-MM-dd"));
		for (int i=0;i<rowNum;i++){
			row = sheet.getRow(i+2);
			
			row.createCell(5).setCellValue(listWIP.get(i).getIwipcount0_3());
			row.createCell(6).setCellValue(listWIP.get(i).getIscarpcount0_3());
			row.createCell(7).setCellValue(listWIP.get(i).getIngcount0_3());
			row.createCell(8).setCellValue(listWIP.get(i).getIokcount0_3());
			
			row.createCell(9).setCellValue(listWIP.get(i).getIwipcount3_10());
			row.createCell(10).setCellValue(listWIP.get(i).getIscarpcount3_10());
			row.createCell(11).setCellValue(listWIP.get(i).getIngcount3_10());
			row.createCell(12).setCellValue(listWIP.get(i).getIokcount3_10());
			
			row.createCell(13).setCellValue(listWIP.get(i).getIwipcount10_30());
			row.createCell(14).setCellValue(listWIP.get(i).getIscarpcount10_30());
			row.createCell(15).setCellValue(listWIP.get(i).getIngcount10_30());
			row.createCell(16).setCellValue(listWIP.get(i).getIokcount10_30());
			
			row.createCell(17).setCellValue(listWIP.get(i).getIwipcountbl30());
			row.createCell(18).setCellValue(listWIP.get(i).getIscarpcountbl30());
			row.createCell(19).setCellValue(listWIP.get(i).getIngcountbl30());
			row.createCell(20).setCellValue(listWIP.get(i).getIokcountbl30());
		}
		int sheetCount = wb.getNumberOfSheets();
		for (int i=0;i<sheetCount;i++){
			sheet = wb.getSheetAt(i);
			sheet.setForceFormulaRecalculation(true);
		}

		FileOutputStream out = null;
		try {
			out = new FileOutputStream(exportPath);
			wb.write(out);
			out.close();
			wb.close();
		} catch (FileNotFoundException e1) {
			log.error("找不到输出文件目录"+exportPath+",原因是："+e1.getMessage());
		} catch (IOException e) {
			log.error("输出报表文件出错，"+e.getMessage());
		}
		return true;
	}
}