package reprot.template;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import rt.connection.*;
import rt.excel.PoiExcel2k3Helper;
import rt.excel.PoiExcelHelper;
import rt.util.*;
import rt.bean.*;

/**
 * @author 张强
 * 新 WIP报表
 */
public class WIPWareHouse {
	private static Logger log = Logger.getLogger(LaserReport.class);
	public static void main(String[] args){
//		String filePath =TUtil.getURL()+ReadProperties.ReadProprety("wip.milan.template");
		String filePath = "F:\\template\\Tigerwip模板最新.xlsx";
		String exportPath = "F:/report/Tiger.xlsx";
		TUtil.print(TUtil.format("HH:mm:ss"));
//		new WIPStageReportNew().ExportExcel(filePath,ReadProperties.ReadProprety("wip.milan.reprot"));
		TUtil.print(filePath);
		new WIPWareHouse().ExportExcel(filePath,exportPath);
		TUtil.print(TUtil.format("HH:mm:ss"));
	}
	
	public List<WIP> GetData(String modelPath){
		String filePath = modelPath;
		PoiExcelHelper helper =  new PoiExcel2k3Helper();
		// 读取B/C两列数据，b→安全时间 c→制程名
		ArrayList<ArrayList<String>> dataList = helper.readExcel(filePath, 2, new String[]{"b","c","e"});
		
		//0-1
		String sSday4 = TUtil.GetDay(-1);
		String sEday4 = TUtil.GetDay(0);
		//1~3
		String sSday1 = TUtil.GetDay(-3);
		String sEday1 = TUtil.GetDay(-1);
		//3~10
		String sSday2 = TUtil.GetDay(-10);
		String sEday2 = TUtil.GetDay(-3);
		//>30
		String sSday3 = TUtil.GetDay(-30);

		List<WIP> list = new ArrayList<WIP>();
		DBManager dbManger = null;
		Connection connection = null;
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		for(int i = 2; i< dataList.size();i++){
			String sql ="select * from"+  //安全时间内
			" (select  count(distinct n.serial_number) iwipcounts,nvl(sum(n.current_status),0) ingcounts,nvl(sum(n.work_flag),0) iscarpcounts"+
	        " from sajet.sys_terminal b,sajet.g_sn_status n,sajet.g_wo_base c ,sajet.sys_part p where"+
	        " b.terminal_id = ? and n.model_id = p.part_id"+
	        " and p.part_no = ?"+
	        " and n.work_order = c.work_order  and n.enabled is null "+
	        " and c.wo_type <> 'DOE' "+
	        " and b.process_id = n.process_id "+
	        " and n.out_process_time >= to_date(?, 'YYYY-MM-DD HH24')+?/24  "+
	        " and n.out_process_time < to_date(?, 'YYYY-MM-DD HH24')) ts,"+
	        
	        //  0~1
	        " (select  count(distinct n.serial_number) iwipcount0,nvl(sum(n.current_status),0) ingcount0,nvl(sum(n.work_flag),0) iscarpcount0"+
	        " from sajet.sys_terminal b,sajet.g_sn_status n,sajet.g_wo_base c,sajet.sys_part p where"+
	        " b.terminal_id = ? and n.model_id = p.part_id"+
	        " and p.part_no = ?"+
	        " and n.work_order=c.work_order  and n.enabled is null "+
	        " and c.wo_type<>'DOE'  "+
	        " and b.process_id=n.process_id "+
	        " and n.out_process_time>=to_date(?,'YYYY-MM-DD HH24')+ ?/24  "+
	        " and n.out_process_time<to_date(?,'YYYY-MM-DD HH24')+ ?/24 "+") t0,"+
			//1~3
	        " (select  count(distinct n.serial_number) iwipcount1,nvl(sum(n.current_status),0) ingcount1,nvl(sum(n.work_flag),0) iscarpcount1"+
	        " from sajet.sys_terminal b,sajet.g_sn_status n,sajet.g_wo_base c ,sajet.sys_part p where"+
	        " b.terminal_id = ? and n.model_id = p.part_id"+
	        " and p.part_no = ?"+
	        " and n.work_order=c.work_order  and n.enabled is null "+
	        " and c.wo_type <> 'DOE'  "+
	        " and b.process_id=n.process_id "+
	        " and n.out_process_time>=to_date(?,'YYYY-MM-DD HH24')+?/24  "+
	        " and n.out_process_time<to_date(?,'YYYY-MM-DD HH24')+?/24  "+") t2,"+
	        //3~10
	        " (select  count(distinct n.serial_number) iwipcount2,nvl(sum(n.current_status),0) ingcount2,nvl(sum(n.work_flag),0) iscarpcount2"+
	        " from sajet.sys_terminal b,sajet.g_sn_status n,sajet.g_wo_base c ,sajet.sys_part p where"+
	        " b.terminal_id = ? and n.model_id = p.part_id"+
	        " and p.part_no = ?"+
	        " and n.work_order=c.work_order  and n.enabled is null "+
	        " and c.wo_type<>'DOE'  "+
	        " and b.process_id=n.process_id "+
	        " and n.out_process_time>=to_date(?,'YYYY-MM-DD HH24')+ ?/24  "+
	        " and n.out_process_time<to_date(?,'YYYY-MM-DD HH24')+ ?/24  "+") t3,"+
	        //10~30
	        " (select  count(distinct n.serial_number) iwipcount3,nvl(sum(n.current_status),0) ingcount3,nvl(sum(n.work_flag),0) iscarpcount3"+
	        " from sajet.sys_terminal b,sajet.g_sn_status n,sajet.g_wo_base c ,sajet.sys_part p where"+
	        " b.terminal_id = ? and n.model_id = p.part_id"+
	        " and p.part_no = ?"+
	        " and n.work_order=c.work_order  and n.enabled is null "+
	        " and c.wo_type<>'DOE'  "+
	        " and b.process_id=n.process_id "+
	        " and n.out_process_time>=to_date(?,'YYYY-MM-DD HH24')+ ?/24  "+
	        " and n.out_process_time<to_date(?,'YYYY-MM-DD HH24')+ ?/24  "+") t4,"+
	        // >30
	        " (select  count(distinct n.serial_number) iwipcount4,nvl(sum(n.current_status),0) ingcount4,nvl(sum(n.work_flag),0) iscarpcount4"+
	        " from sajet.sys_terminal b,sajet.g_sn_status n,sajet.g_wo_base c ,sajet.sys_part p where"+
	        " b.terminal_id = ? and n.model_id = p.part_id"+
	        " and p.part_no = ?"+
	        " and n.work_order=c.work_order  and n.enabled is null "+
	        " and c.wo_type<>'DOE'  "+
	        " and b.process_id=n.process_id "+
	        " and n.out_process_time<to_date(?,'YYYY-MM-DD HH24')+?/24  "+
	        " and n.out_process_time>=to_date('2014-01-01 08','YYYY-MM-DD HH24')+?/24  "+") t5";
//			TUtil.print(sql);
			try {
				dbManger = new DBManager();
				connection = dbManger.GetConnection(0);
				pstmt = connection.prepareCall(sql);
				int safeTime = Integer.parseInt(dataList.get(i).get(0));
				String terminalID = dataList.get(i).get(2);
				String part_no = dataList.get(i).get(1);
				//之内
				pstmt.setString(1, terminalID);
				pstmt.setString(2, part_no);
				pstmt.setString(3, sEday4+" 08");
				pstmt.setInt(4, safeTime);
				pstmt.setString(5, sEday4+" 08");
				//0-1
				pstmt.setString(6, terminalID);
				pstmt.setString(7, part_no);
				pstmt.setString(8, sSday4+" 08");
				pstmt.setInt(9, safeTime);
				pstmt.setString(10, sEday4+" 08");
				pstmt.setInt(11, safeTime);
				//1-3
				pstmt.setString(12, terminalID);
				pstmt.setString(13, part_no);
				pstmt.setString(14, sSday1+" 08");
				pstmt.setInt(15, safeTime);
				pstmt.setString(16, sEday1+" 08");
				pstmt.setInt(17, safeTime);
				//3-10
				pstmt.setString(18, terminalID);
				pstmt.setString(19, part_no);
				pstmt.setString(20, sSday2+" 08");
				pstmt.setInt(21, safeTime);
				pstmt.setString(22, sEday2+" 08");
				pstmt.setInt(23, safeTime);
				//10-30
				pstmt.setString(24, terminalID);
				pstmt.setString(25, part_no);
				pstmt.setString(26, sSday3+" 08");
				pstmt.setInt(27, safeTime);
				pstmt.setString(28, sSday2+" 08");
				pstmt.setInt(29, safeTime);
				//>30
				pstmt.setString(30, terminalID);
				pstmt.setString(31, part_no);
				pstmt.setString(32, sSday3+" 08");
				pstmt.setInt(33, safeTime);
				pstmt.setInt(34, safeTime);

				rs = pstmt.executeQuery();
				while (rs.next()){
					WIP wip = new WIP();
					wip.setIwipcountS(rs.getInt("iwipcounts"));
					wip.setIngcountS(rs.getInt("ingcounts"));
					wip.setIscarpcountS(rs.getInt("iscarpcounts"));
					if (wip.getIngcountS() >= wip.getIscarpcountS()){
						wip.setIngcountS(wip.getIngcountS() - wip.getIscarpcountS());
						wip.setIokcountS(wip.getIwipcountS() - wip.getIngcountS()-wip.getIscarpcountS());
					}
					else
						wip.setIokcountle1(wip.getIwipcountS() - wip.getIscarpcountS());
					
					wip.setIwipcountle1(rs.getInt("iwipcount0"));
					wip.setIngcountle1(rs.getInt("ingcount0"));
					wip.setIscarpcountle1(rs.getInt("iscarpcount0"));
					if (wip.getIngcountle1() >= wip.getIscarpcountle1()){
						wip.setIngcountle1(wip.getIngcountle1() - wip.getIscarpcountle1());
						wip.setIokcountle1(wip.getIwipcountle1() - wip.getIngcountle1()-wip.getIscarpcountle1());
					}
					else
						wip.setIokcountle1(wip.getIwipcountle1() - wip.getIscarpcountle1());
					
					wip.setIwipcount0_3(rs.getInt("iwipcount1"));
					wip.setIngcount0_3(rs.getInt("ingcount1"));
					wip.setIscarpcount0_3(rs.getInt("iscarpcount1"));
					if (wip.getIngcount0_3() >= wip.getIscarpcount0_3()){
						wip.setIngcount0_3(wip.getIngcount0_3() - wip.getIscarpcount0_3());
						wip.setIokcount0_3(wip.getIwipcount0_3() - wip.getIngcount0_3()-wip.getIscarpcount0_3());
					}
					else
						wip.setIokcount0_3(wip.getIwipcount0_3() - wip.getIscarpcount0_3());
					
					wip.setIwipcount3_10(rs.getInt("iwipcount2"));
					wip.setIscarpcount3_10(rs.getInt("iscarpcount2"));
					wip.setIngcount3_10(rs.getInt("ingcount2"));
					if (wip.getIngcount3_10() >= wip.getIscarpcount3_10()){
						wip.setIngcount3_10(wip.getIngcount3_10() - wip.getIscarpcount3_10());
						wip.setIokcount3_10(wip.getIwipcount3_10() - wip.getIngcount3_10()-wip.getIscarpcount3_10());
					}
					else
						wip.setIokcount3_10(wip.getIwipcount3_10() - wip.getIscarpcount3_10());
					
					wip.setIwipcount10_30(rs.getInt("iwipcount3"));
					wip.setIscarpcount10_30(rs.getInt("iscarpcount3"));
					wip.setIngcount10_30(rs.getInt("ingcount3"));
					if (wip.getIngcount10_30() >= wip.getIscarpcount10_30()){
						wip.setIngcount10_30(wip.getIngcount10_30() - wip.getIscarpcount10_30());
						wip.setIokcount10_30(wip.getIwipcount10_30() - wip.getIngcount10_30()-wip.getIscarpcount10_30());
					}
					else
						wip.setIokcount10_30(wip.getIwipcount10_30() - wip.getIscarpcount10_30());
										
					wip.setIwipcountbl30(rs.getInt("iwipcount4"));
					wip.setIscarpcountbl30(rs.getInt("iscarpcount4"));
					wip.setIngcountbl30(rs.getInt("ingcount4"));
					if (wip.getIngcountbl30() >= wip.getIscarpcountbl30()){
						wip.setIngcountbl30(wip.getIngcountbl30() - wip.getIscarpcountbl30());
						wip.setIokcountbl30(wip.getIwipcountbl30() - wip.getIngcountbl30()-wip.getIscarpcountbl30());
					}
					else
						wip.setIokcountbl30(wip.getIwipcountbl30() - wip.getIscarpcountbl30());
					list.add(wip);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}finally{
				dbManger.closeConnection(connection, rs, pstmt);;
			}
		}
		return list;
	}
	
	public boolean ExportExcel(String fileName,String exportPath){
		InputStream ins = null;
		XSSFWorkbook wb = null;
		try {
			ins = new FileInputStream(fileName);
			wb = new XSSFWorkbook(ins);
		} catch (FileNotFoundException e2) {
			e2.printStackTrace();
			log.error("没有找到模板"+fileName+e2.getMessage());
			return false;
		}catch (IOException e) {
			e.printStackTrace();
			log.error("读取模板文件出错："+e.getMessage());
			return false;
		}
		XSSFSheet sheet = wb.getSheetAt(2);
		
		List<WIP> list = new WIPWareHouse().GetData(fileName);
		int rowNum = list.size();
		XSSFRow row = null;
		row = sheet.getRow(0);
		row.getCell(0).setCellValue(TUtil.format("yyyy-MM-dd"));
		for (int i=0;i<rowNum;i++){
			row = sheet.getRow(i+2);
			row.getCell(8).setCellValue(list.get(i).getIwipcountS());
			row.getCell(9).setCellValue(list.get(i).getIscarpcountS());
			row.getCell(10).setCellValue(list.get(i).getIngcountS());
			row.getCell(11).setCellValue(list.get(i).getIokcountS());
			
			row.getCell(12).setCellValue(list.get(i).getIwipcountle1());
			row.getCell(13).setCellValue(list.get(i).getIscarpcountle1());
			row.getCell(14).setCellValue(list.get(i).getIngcountle1());
			row.getCell(15).setCellValue(list.get(i).getIokcountle1());
			
			row.getCell(16).setCellValue(list.get(i).getIwipcount0_3());
			row.getCell(17).setCellValue(list.get(i).getIscarpcount0_3());
			row.getCell(18).setCellValue(list.get(i).getIngcount0_3());
			row.getCell(19).setCellValue(list.get(i).getIokcount0_3());
			
			row.getCell(20).setCellValue(list.get(i).getIwipcount3_10());
			row.getCell(21).setCellValue(list.get(i).getIscarpcount3_10());
			row.getCell(22).setCellValue(list.get(i).getIngcount3_10());
			row.getCell(23).setCellValue(list.get(i).getIokcount3_10());
			
			row.getCell(24).setCellValue(list.get(i).getIwipcount10_30());
			row.getCell(25).setCellValue(list.get(i).getIscarpcount10_30());
			row.getCell(26).setCellValue(list.get(i).getIngcount10_30());
			row.getCell(27).setCellValue(list.get(i).getIokcount10_30());
			
			row.getCell(28).setCellValue(list.get(i).getIwipcountbl30());
			row.getCell(29).setCellValue(list.get(i).getIscarpcountbl30());
			row.getCell(30).setCellValue(list.get(i).getIngcountbl30());
			row.getCell(31).setCellValue(list.get(i).getIokcountbl30());
		}
//		int sheetCount = wb.getNumberOfSheets();
//		for (int i=0;i<sheetCount;i++){
			sheet = wb.getSheetAt(2);
//			sheet.setForceFormulaRecalculation(true);
//		}

		try {
			OutputStream out = new FileOutputStream(exportPath);
			wb.write(out);
			out.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			log.error("创建输出报表时出错："+e1.getMessage());
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			log.error("输出流报表时报错："+e.getMessage());
			return false;
		}finally{
			try {
				wb.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return true;
	}
}