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
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
//import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import rt.bean.WIP;
import rt.connection.DBManager;
import rt.excel.PoiExcel2k3Helper;
import rt.excel.PoiExcelHelper;
import rt.util.TUtil;

public class LostCountReport {
	private static Logger log = Logger.getLogger(LaserReport.class);
	public static void main(String[] args){
		String filePath ="d:/Lostcount_Hulk.xlsx";
		String exportPath = "d:/LostCountReport_Hulk.xlsx";
		TUtil.print("开始执行："+TUtil.format("HH:mm:ss"));		
		new LostCountReport().ExportExcel(filePath,exportPath,new LostCountReport().GetData(filePath,0),0);
		TUtil.print("制程段完毕："+TUtil.format("HH:mm:ss"));
	}
	
	public List<WIP> GetData(String modelPath,int sheetIndex){

		String filePath = modelPath;
		PoiExcelHelper helper =  new PoiExcel2k3Helper();
		// 读取B/C两列数据，b→安全时间 c→制程名
		ArrayList<ArrayList<String>> dataList = helper.readExcel(filePath, sheetIndex, new String[]{"b"});
		
		String sFirstDay=TUtil.GetDay(-7);
		String sSecDay=TUtil.GetDay(-6);
		String sThiDay=TUtil.GetDay(-5);
		String sFourDay=TUtil.GetDay(-4);
		String sFifDay=TUtil.GetDay(-3);
		String sSixDay=TUtil.GetDay(-2);
		String sSuday=TUtil.GetDay(-1);
		String sEday=TUtil.GetDay(0);
			
		List<WIP> list = new ArrayList<WIP>();
		DBManager dbManger = null;
		Connection connection = null;
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		
//		for(s=0;s<sj.length;s++){
		  for(int i = 2; i< dataList.size();i++){
			  
		   String processName = dataList.get(i).get(0);
		  
		   String sql ="select * from"+ 
			" (select nvl(sum(m.travelcount), 0) iallcounts "+
            "from sajet.g_travel_count m,sajet.sys_process b,sajet.g_wo_base c  "+
            "where b.process_name = '"+processName+"' "+
            "and m.work_order=c.work_order "+
            "and c.wo_type<>'DOE' "+
            "and m.process_id=b.process_id "+
            "and m.update_time>=to_char(to_date('"+sFirstDay+" 08','YYYY-MM-DD HH24'),'yyyymmddhh24')  "+
            "and m.update_time<to_char(to_date('"+sSecDay+" 08','YYYY-MM-DD HH24'),'yyyymmddhh24') )ts, "+ 
//			" and m.update_time>=to_date('2015-04-24 08','YYYY-MM-DD HH24')  "+
//			" and m.update_time<to_date('2015-04-25 08','YYYY-MM-DD HH24')) ts,"+ //";
			"(select nvl(count(distinct(trev_type)),0)  iwipcounts ,nvl(sum(count(trev_type)),0) ingcounts from sajet.g_trev_log a,sajet.sys_process b "+
			"where b.process_name = '"+processName+"'  "+
			"and a.trev_type like 'DYH%'  "+
			"and a.process_id=b.process_id   "+
			"and a.update_time>=to_date('"+sFirstDay+" 08','YYYY-MM-DD HH24')  "+
			"and a.update_time<to_date('"+sSecDay+" 08','YYYY-MM-DD HH24') "+ 
			"group by a.trev_type ) a,"+			
//		  TUtil.print(sql);  
//			//2
			"(select nvl(sum(m.travelcount),0) iallcount0 "+
			"from sajet.g_travel_count m,sajet.sys_process b,sajet.g_wo_base c  "+
			"where b.process_name= '"+processName+"' "+
			"and m.work_order=c.work_order "+
			"and c.wo_type<>'DOE' "+
			"and m.process_id=b.process_id "+
			"and m.update_time>=to_char(to_date('"+sSecDay+" 08','YYYY-MM-DD HH24'),'yyyymmddhh24') "+
			"and m.update_time<to_char(to_date('"+sThiDay+" 08','YYYY-MM-DD HH24'),'yyyymmddhh24'))t0,"+
	        "(select nvl(count(distinct(trev_type)),0)  iwipcount0 ,nvl(sum(count(trev_type)),0) ingcount0 from sajet.g_trev_log a,sajet.sys_process b "+
	        "where b.process_name = '"+processName+"'  "+
	        "and a.trev_type like 'DYH%'  "+
	        "and a.process_id=b.process_id   "+
	        "and a.update_time>=to_date('"+sSecDay+" 08','YYYY-MM-DD HH24')  "+
	        "and a.update_time<to_date('"+sThiDay+" 08','YYYY-MM-DD HH24') "+
	        "group by a.trev_type ) a0,"+
			//3
			"(select nvl(sum(m.travelcount),0) iallcount1 "+
			"from sajet.g_travel_count m,sajet.sys_process b,sajet.g_wo_base c  "+
			"where b.process_name= '"+processName+"' "+
			"and m.work_order=c.work_order "+
			"and c.wo_type<>'DOE' "+
			"and m.process_id=b.process_id "+
			"and m.update_time>=to_char(to_date('"+sThiDay+" 08','YYYY-MM-DD HH24'),'yyyymmddhh24') "+
			"and m.update_time<to_char(to_date('"+sFourDay+" 08','YYYY-MM-DD HH24'),'yyyymmddhh24')) t1,"+
			"(select nvl(count(distinct(trev_type)),0)  iwipcount1 ,nvl(sum(count(trev_type)),0) ingcount1 from sajet.g_trev_log a,sajet.sys_process b "+
	        "where b.process_name = '"+processName+"'  "+
	        "and a.trev_type like 'DYH%'  "+
	        "and a.process_id=b.process_id   "+
	        "and a.update_time>=to_date('"+sThiDay+" 08','YYYY-MM-DD HH24')  "+
	        "and a.update_time<to_date('"+sFourDay+" 08','YYYY-MM-DD HH24') "+
	        "group by a.trev_type ) a1,"+
	        //4
	        "(select nvl(sum(m.travelcount),0) iallcount2 "+
            "from sajet.g_travel_count m,sajet.sys_process b,sajet.g_wo_base c  "+
            "where b.process_name= '"+processName+"' "+
            "and m.work_order=c.work_order "+
            "and c.wo_type<>'DOE' "+
            "and m.process_id=b.process_id "+
            "and m.update_time>=to_char(to_date('"+sFourDay+" 08','YYYY-MM-DD HH24'),'yyyymmddhh24')  "+
            "and m.update_time<to_char(to_date('"+sFifDay+" 08','YYYY-MM-DD HH24'),'yyyymmddhh24')) t2,"+
	        "(select nvl(count(distinct(trev_type)),0)  iwipcount2 ,nvl(sum(count(trev_type)),0) ingcount2 from sajet.g_trev_log a,sajet.sys_process b "+
	        "where b.process_name = '"+processName+"'  "+
	        "and a.trev_type like 'DYH%'  "+
	        "and a.process_id=b.process_id   "+
	        "and a.update_time>=to_date('"+sFourDay+" 08','YYYY-MM-DD HH24')  "+
	        "and a.update_time<to_date('"+sFifDay+" 08','YYYY-MM-DD HH24') "+
	        "group by a.trev_type ) a2,"+
	        //5
	        "(select nvl(sum(m.travelcount),0) iallcount3 "+
            "from sajet.g_travel_count m,sajet.sys_process b,sajet.g_wo_base c  "+
            "where b.process_name= '"+processName+"' "+
            "and m.work_order=c.work_order "+
            "and c.wo_type<>'DOE' "+
            "and m.process_id=b.process_id "+
            "and m.update_time>=to_char(to_date('"+sFifDay+" 08','YYYY-MM-DD HH24'),'yyyymmddhh24')  "+
            "and m.update_time<to_char(to_date('"+sSixDay+" 08','YYYY-MM-DD HH24'),'yyyymmddhh24')) t3,"+
	        "(select nvl(count(distinct(trev_type)),0)  iwipcount3 ,nvl(sum(count(trev_type)),0) ingcount3 from sajet.g_trev_log a,sajet.sys_process b "+
	        "where b.process_name = '"+processName+"'  "+
	        "and a.trev_type like 'DYH%'  "+
	        "and a.process_id=b.process_id   "+
	        " and a.update_time>=to_date('"+sFifDay+" 08','YYYY-MM-DD HH24')  "+
	        " and a.update_time<to_date('"+sSixDay+" 08','YYYY-MM-DD HH24') "+
	        "group by a.trev_type ) a3,"+
	        // 6
	        " (select nvl(sum(m.travelcount),0) iallcount4 "+
            "from sajet.g_travel_count m,sajet.sys_process b,sajet.g_wo_base c  "+
            "where b.process_name= '"+processName+"' "+
            "and m.work_order=c.work_order "+
            "and c.wo_type<>'DOE' "+
            "and m.process_id=b.process_id "+
            "and m.update_time>=to_char(to_date('"+sSixDay+" 08','YYYY-MM-DD HH24'),'yyyymmddhh24')  "+
            "and m.update_time<to_char(to_date('"+sSuday+" 08','YYYY-MM-DD HH24'),'yyyymmddhh24')) t4,"+
	        "(select nvl(count(distinct(trev_type)),0)  iwipcount4 ,nvl(sum(count(trev_type)),0) ingcount4 from sajet.g_trev_log a,sajet.sys_process b "+
	        "where b.process_name = '"+processName+"'  "+
	        "and a.trev_type like 'DYH%'  "+
	        "and a.process_id=b.process_id   "+
	        "and a.update_time>=to_date('"+sSixDay+" 08','YYYY-MM-DD HH24')   "+
	        "and a.update_time<to_date('"+sSuday+" 08','YYYY-MM-DD HH24') "+
	        "group by a.trev_type ) a4,"+
             //7
	        "(select nvl(sum(m.travelcount),0) iallcount5 "+
            "from sajet.g_travel_count m,sajet.sys_process b,sajet.g_wo_base c  "+
            "where b.process_name= '"+processName+"' "+
            "and m.work_order=c.work_order "+
            "and c.wo_type<>'DOE' "+
            "and m.process_id=b.process_id "+
            "and m.update_time>=to_char(to_date('"+sSuday+" 08','YYYY-MM-DD HH24'),'yyyymmddhh24')  "+
            "and m.update_time<to_char(to_date('"+sEday+" 08','YYYY-MM-DD HH24'),'yyyymmddhh24')) t5,"+
	        "(select nvl(count(distinct(trev_type)),0)  iwipcount5 ,nvl(sum(count(trev_type)),0) ingcount5 from sajet.g_trev_log a,sajet.sys_process b "+
	        "where b.process_name = '"+processName+"'  "+
	        "and a.trev_type like 'DYH%'  "+
	        "and a.process_id=b.process_id   "+
	        "and a.update_time>=to_date('"+sSuday+" 08','YYYY-MM-DD HH24')  "+
	        "and a.update_time<to_date('"+sEday+" 08','YYYY-MM-DD HH24') "+
	        "group by a.trev_type ) a5";
//		   TUtil.print(sql);
	try {
			dbManger = new DBManager();
			connection = dbManger.GetConnection(0);
			
			pstmt = connection.prepareCall(sql);
			rs = pstmt.executeQuery();
			
			
			while (rs.next()){
				WIP wip = new WIP();
				
				wip.setIallcountS(rs.getInt("iallcounts"));	
				wip.setIallcountle1(rs.getInt("iallcount0"));	
				wip.setIallcount0_3(rs.getInt("iallcount1"));	
				wip.setIallcount3_10(rs.getInt("iallcount2"));	
				wip.setIallcount10_30(rs.getInt("iallcount3"));	
				wip.setIallcount30(rs.getInt("iallcount4"));	
				wip.setIallcount30_1(rs.getInt("iallcount5"));
				
				wip.setIwipcountS(rs.getInt("iwipcounts"));	
				if((wip.getIallcountS()+wip.getIwipcountS())!=0){
					wip.setIratioS((double)wip.getIwipcountS()/(wip.getIallcountS()+wip.getIwipcountS()));
				}else
				{
					wip.setIratioS(0.00);
				}
				wip.setIngcountS(rs.getInt("ingcounts"));
				wip.setIokcountS(wip.getIngcountS()-wip.getIwipcountS()); 

				//2
				wip.setIwipcountle1(rs.getInt("iwipcount0"));
				if((wip.getIallcountle1()+wip.getIwipcountle1()) !=0){
					wip.setIratiole1((double)wip.getIwipcountle1()/(wip.getIallcountle1()+wip.getIwipcountle1()));
				}else
				{
					wip.setIratiole1(0.00);
				}
				wip.setIngcountle1(rs.getInt("ingcount0"));
				wip.setIokcountle1(wip.getIngcountle1()-wip.getIwipcountle1()); 			
				//3
				wip.setIwipcount0_3(rs.getInt("iwipcount1"));
				if((wip.getIallcount0_3()+wip.getIwipcount0_3()) !=0){
					wip.setIratio0_3((double)wip.getIwipcount0_3()/(wip.getIallcount0_3()+wip.getIwipcount0_3()));
				}else
				{
					wip.setIratio0_3(0.00);
				}
				wip.setIngcount0_3(rs.getInt("ingcount1"));
				wip.setIokcount0_3(wip.getIngcount0_3()-wip.getIwipcount0_3());
				//4
				wip.setIwipcount3_10(rs.getInt("iwipcount2"));
				if((wip.getIallcount3_10()+wip.getIwipcount3_10()) !=0){
					wip.setIratio3_10((double)wip.getIwipcount3_10()/(wip.getIallcount3_10()+wip.getIwipcount3_10()));
//					TUtil.print(wip.getIallcount3_10());
//					TUtil.print(wip.getIwipcount3_10());
//					TUtil.print(((double)wip.getIwipcount3_10()/(wip.getIallcount3_10()+wip.getIwipcount3_10()))*100);
				}else
				{			
					wip.setIratio3_10(0.00);
				}
				wip.setIngcount3_10(rs.getInt("ingcount2"));
				wip.setIokcount3_10(wip.getIngcount3_10()-wip.getIwipcount3_10());
				//5
				wip.setIwipcount10_30(rs.getInt("iwipcount3"));
				if((wip.getIallcount10_30()+wip.getIwipcount10_30()) !=0){
					wip.setIratio10_30((double)wip.getIwipcount10_30()/(wip.getIallcount10_30()+wip.getIwipcount10_30()));
				}else
				{
					wip.setIratio10_30(0.00);
				}
				wip.setIngcount10_30(rs.getInt("ingcount3"));
				wip.setIokcount10_30(wip.getIngcount10_30()-wip.getIwipcount10_30());
				//6
				wip.setIwipcountbl30(rs.getInt("iwipcount4"));
				if((wip.getIallcount30()+wip.getIwipcountbl30()) !=0){
					wip.setIratio30((double)wip.getIwipcountbl30()/(wip.getIallcount30()+wip.getIwipcountbl30()));				
				}else
				{
					wip.setIratio30(0.00);
				}
				wip.setIngcountbl30(rs.getInt("ingcount4"));
				wip.setIokcountbl30(wip.getIngcountbl30()-wip.getIwipcountbl30());
				//7
				wip.setIwipcount30_1(rs.getInt("iwipcount5"));
				if((wip.getIallcount30_1()+wip.getIwipcount30_1()) !=0){
					wip.setIratio30_1((double)wip.getIwipcount30_1()/(wip.getIallcount30_1()+wip.getIwipcount30_1()));		
				}else
				{
					wip.setIratio30_1(0.00);
				}
				wip.setIngcount30_1(rs.getInt("ingcount5"));
				wip.setIokcount30_1(wip.getIngcount30_1()-wip.getIwipcount30_1());
							
				list.add(wip);
			}						
		} catch (SQLException e) {
				e.printStackTrace();
		}finally{
				dbManger.closeConnection(connection, rs, pstmt);
			   }
  }
		return list;		
}
	//生成报表
public boolean ExportExcel(String filePath,String exportPath, List<WIP> list,int sheetIndex){

		InputStream ins = null;
		XSSFWorkbook wb = null;
		try {
			ins = new FileInputStream(filePath);
			wb = new XSSFWorkbook(ins);
		} catch (FileNotFoundException e2) {
			e2.printStackTrace();
			log.error("没有找到模板"+filePath+e2.getMessage());
			return false;
		}catch (IOException e) {
			e.printStackTrace();
			log.error("读取模板文件出错："+e.getMessage());
			return false;
		}
		XSSFSheet sheet = wb.getSheetAt(sheetIndex);
		XSSFRow row = null;
		row = sheet.getRow(0);
        int k=7;
		for(int j=2;j<34;j+=5){
		  row.createCell(j).setCellValue(TUtil.GetDay(-k));	
		  k--;
		}
		
		
//		for(int s=2;s<38;s+=5){
//			for(int i=2;i<8;i++){
//			Cell cell = row.getCell(i);
//			CellStyle css =wb.createCellStyle();			
//			css.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
//			css.setFillPattern(CellStyle.SOLID_FOREGROUND);
//			css.setLeftBorderColor(HSSFColor.BLACK.index);
//			css.setBorderLeft(HSSFCellStyle.BORDER_THIN);
//			css.setRightBorderColor(HSSFColor.BLACK.index);
//			css.setBorderRight(HSSFCellStyle.BORDER_THIN);
//			css.setBottomBorderColor(HSSFColor.BLACK.index);
//			css.setBorderBottom(HSSFCellStyle.BORDER_THIN);
//			css.setBorderTop(HSSFCellStyle.BORDER_THIN);		
//			cell.setCellStyle(css);	
//			}  
//		
//	    }
	   //填充报表
		int rowNum = list.size();
//		TUtil.print(rowNum);
		for (int i=0;i<rowNum;i++){
			row = sheet.getRow(i+2);
//			row.getCell(2).setCellValue(value);
			row.getCell(2).setCellValue(list.get(i).getIallcountS());
			row.getCell(3).setCellValue(list.get(i).getIwipcountS());			
			Cell cell = row.getCell(4);
			double rate =Double.parseDouble(list.get(i).getIratioS()+"");
			cell.setCellValue(rate);
			CellStyle css =wb.createCellStyle();
			css.setFillForegroundColor(IndexedColors.YELLOW.getIndex());			
			css.setFillPattern(CellStyle.SOLID_FOREGROUND);
			css.setLeftBorderColor(HSSFColor.BLACK.index);
			css.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			css.setRightBorderColor(HSSFColor.BLACK.index);
			css.setBorderRight(HSSFCellStyle.BORDER_THIN);
			css.setBottomBorderColor(HSSFColor.BLACK.index);
			css.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			css.setBorderTop(HSSFCellStyle.BORDER_THIN);

			css.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00%"));//.getFormat("#,##0.00")			
			cell.setCellStyle(css);			
			row.getCell(5).setCellValue(list.get(i).getIokcountS());
					
			row.getCell(7).setCellValue(list.get(i).getIallcountle1());
			row.getCell(8).setCellValue(list.get(i).getIwipcountle1());
			Cell cell1 = row.getCell(9);
			double rate1 =Double.parseDouble(list.get(i).getIratiole1()+"");
			cell1.setCellValue(rate1);
			CellStyle css1 = wb.createCellStyle();
			css1.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());			
			css1.setFillPattern(CellStyle.SOLID_FOREGROUND);
			css1.setLeftBorderColor(HSSFColor.BLACK.index);
			css1.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			css1.setRightBorderColor(HSSFColor.BLACK.index);
			css1.setBorderRight(HSSFCellStyle.BORDER_THIN);
			css1.setBottomBorderColor(HSSFColor.BLACK.index);
			css1.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			css1.setBorderTop(HSSFCellStyle.BORDER_THIN);
			css1.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00%"));//.getFormat("#,##0.00")
			cell1.setCellStyle(css1);		
			row.getCell(10).setCellValue(list.get(i).getIokcountle1());
			
			row.getCell(12).setCellValue(list.get(i).getIallcount0_3());
			row.getCell(13).setCellValue(list.get(i).getIwipcount0_3());
			Cell cell2 = row.createCell(14);
			double rate2 =Double.parseDouble(list.get(i).getIratio0_3()+"");
			cell2.setCellValue(rate2);
			CellStyle css2 = wb.createCellStyle();
			css2.setFillForegroundColor(IndexedColors.YELLOW.getIndex());			
			css2.setFillPattern(CellStyle.SOLID_FOREGROUND);
			css2.setLeftBorderColor(HSSFColor.BLACK.index);
			css2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			css2.setRightBorderColor(HSSFColor.BLACK.index);
			css2.setBorderRight(HSSFCellStyle.BORDER_THIN);
			css2.setBottomBorderColor(HSSFColor.BLACK.index);
			css2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			css2.setBorderTop(HSSFCellStyle.BORDER_THIN);
			css2.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00%"));//.getFormat("#,##0.00")
			cell2.setCellStyle(css2);		
			row.getCell(15).setCellValue(list.get(i).getIokcount0_3());
			
			row.getCell(17).setCellValue(list.get(i).getIallcount3_10());
			row.getCell(18).setCellValue(list.get(i).getIwipcount3_10());
			Cell cell3 = row.createCell(19);
			double rate3 =Double.parseDouble(list.get(i).getIratio3_10()+"");
//			TUtil.print(list.get(i).getIratio3_10());
			cell3.setCellValue(rate3);
			CellStyle css3 = wb.createCellStyle();
			css3.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());			
			css3.setFillPattern(CellStyle.SOLID_FOREGROUND);
			css3.setLeftBorderColor(HSSFColor.BLACK.index);
			css3.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			css3.setRightBorderColor(HSSFColor.BLACK.index);
			css3.setBorderRight(HSSFCellStyle.BORDER_THIN);
			css3.setBottomBorderColor(HSSFColor.BLACK.index);
			css3.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			css3.setBorderTop(HSSFCellStyle.BORDER_THIN);
			css3.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00%"));//.getFormat("#,##0.00")
			cell3.setCellStyle(css3);
			row.getCell(20).setCellValue(list.get(i).getIokcount3_10());
			
			row.getCell(22).setCellValue(list.get(i).getIallcount10_30());
			row.getCell(23).setCellValue(list.get(i).getIwipcount10_30());
			Cell cell4 = row.createCell(24);
			double rate4 =Double.parseDouble(list.get(i).getIratio10_30()+"");
			cell4.setCellValue(rate4);
			CellStyle css4 = wb.createCellStyle();
			css4.setFillForegroundColor(IndexedColors.YELLOW.getIndex());			
			css4.setFillPattern(CellStyle.SOLID_FOREGROUND);
			css4.setLeftBorderColor(HSSFColor.BLACK.index);
			css4.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			css4.setRightBorderColor(HSSFColor.BLACK.index);
			css4.setBorderRight(HSSFCellStyle.BORDER_THIN);
			css4.setBottomBorderColor(HSSFColor.BLACK.index);
			css4.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			css4.setBorderTop(HSSFCellStyle.BORDER_THIN);
			css4.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00%"));//.getFormat("#,##0.00")
			cell4.setCellStyle(css4);		
			row.getCell(25).setCellValue(list.get(i).getIokcount10_30());
			
			row.getCell(27).setCellValue(list.get(i).getIallcount30());
			row.getCell(28).setCellValue(list.get(i).getIwipcountbl30());
			Cell cell5 = row.createCell(29);
			double rate5 =Double.parseDouble(list.get(i).getIratio30()+"");
			cell5.setCellValue(rate5);
			CellStyle css5 = wb.createCellStyle();
			css5.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());			
			css5.setFillPattern(CellStyle.SOLID_FOREGROUND);
			css5.setLeftBorderColor(HSSFColor.BLACK.index);
			css5.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			css5.setRightBorderColor(HSSFColor.BLACK.index);
			css5.setBorderRight(HSSFCellStyle.BORDER_THIN);
			css5.setBottomBorderColor(HSSFColor.BLACK.index);
			css5.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			css5.setBorderTop(HSSFCellStyle.BORDER_THIN);
			css5.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00%"));//.getFormat("#,##0.00")
			cell5.setCellStyle(css5);		
			row.getCell(30).setCellValue(list.get(i).getIokcountbl30());
			
			row.getCell(32).setCellValue(list.get(i).getIallcount30_1());
			row.getCell(33).setCellValue(list.get(i).getIwipcount30_1());
			Cell cell6 = row.createCell(34);
			double rate6 =Double.parseDouble(list.get(i).getIratio30_1()+"");
			cell6.setCellValue(rate6);
			CellStyle css6 = wb.createCellStyle();
			css6.setFillForegroundColor(IndexedColors.YELLOW.getIndex());			
			css6.setFillPattern(CellStyle.SOLID_FOREGROUND);
			css6.setLeftBorderColor(HSSFColor.BLACK.index);
			css6.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			css6.setRightBorderColor(HSSFColor.BLACK.index);
			css6.setBorderRight(HSSFCellStyle.BORDER_THIN);
			css6.setBottomBorderColor(HSSFColor.BLACK.index);
			css6.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			css6.setBorderTop(HSSFCellStyle.BORDER_THIN);
			css6.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00%"));//.getFormat("#,##0.00")
			cell6.setCellStyle(css6);		
			row.getCell(35).setCellValue(list.get(i).getIokcount30_1());
			
		}
		//Excel表格力的表达式重新计算
		int sheetCount = wb.getNumberOfSheets();
		for (int i=0;i<sheetCount;i++){
			sheet = wb.getSheetAt(i);
			sheet.setForceFormulaRecalculation(true);
		}
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

