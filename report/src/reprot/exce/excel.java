package reprot.exce;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.List;

import org.apache.log4j.Logger;
//import org.apache.poi.hssf.usermodel.HSSFCell;
//import org.apache.poi.hssf.usermodel.HSSFCellStyle;
//import org.apache.poi.hssf.usermodel.HSSFDataFormat;
//import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
//import org.apache.poi.xssf.usermodel.XSSFCell;
//import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import reprot.template.LaserReport;
//import reprot.template.LostCountReport;
//import rt.bean.WIP;
//import rt.connection.DBManager;
//import rt.excel.PoiExcel2k3Helper;
//import rt.excel.PoiExcelHelper;
import rt.util.TUtil;

public class excel {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String filePath ="d:/1.xlsx";
		String exportPath = "d:/2.xlsx";
		TUtil.print("开始执行："+TUtil.format("HH:mm:ss"));		
		new excel().ExportExcel(filePath,exportPath,0);
		TUtil.print("完毕："+TUtil.format("HH:mm:ss"));
	
	}
	
	private static Logger log = Logger.getLogger(LaserReport.class);
	
	//生成报表
public boolean ExportExcel(String filePath,String exportPath,int sheetIndex){

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
		Cell cell = row.getCell(1);
		// Aqua background
		CellStyle style = wb.createCellStyle();
		style.setFillForegroundColor(IndexedColors.AQUA.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);	
		cell.setCellValue("X1");
		cell.setCellStyle(style);


//		}
	   //填充报表

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
