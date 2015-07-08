package rt.excel;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Excel 读取（97-2003/07+格式）
 */
public class PoiExcel2k3Helper extends PoiExcelHelper {
	
	private Workbook wb;

	public PoiExcelHelper getPoiExcelHelper(String filePath){
		PoiExcelHelper helper = new PoiExcel2k3Helper();
		return helper;
	}
	
	/**
	 * 获取WorkBook
	 * @param filePath
	 * @return
	 */
	public Workbook getWorkBook(String filePath){
		try {
	            wb = new XSSFWorkbook(filePath);
	        } catch (Exception ex) {
	            try {
	            	wb = new HSSFWorkbook(new FileInputStream(filePath));
				} catch (IOException e) {
					e.printStackTrace();
				}
	        }
		return wb;
	}
	
	/** 获取sheet列表 */
	public ArrayList<String> getSheetList(String filePath) {
		ArrayList<String> sheetList = new ArrayList<String>(0);
		try {
			wb = getWorkBook(filePath);
			int i = 0;
			while (true) {
				try {
					String name = wb.getSheetName(i);
					sheetList.add(name);
					i++;
				} catch (Exception e) {
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sheetList;
	}

	/** 读取Excel文件内容 */
	public ArrayList<ArrayList<String>> readExcel(String filePath, int sheetIndex, String rows, String columns) {
		ArrayList<ArrayList<String>> dataList = new ArrayList<ArrayList<String>> ();
		try {
			wb = getWorkBook(filePath);
			Sheet sheet = wb.getSheetAt(sheetIndex);
			
			dataList = readExcel(sheet, rows, getColumnNumber(sheet, columns));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dataList;
	}
	
	/** 读取Excel文件内容 */
	public ArrayList<ArrayList<String>> readExcel(String filePath, int sheetIndex, String rows, int[] cols) {
		ArrayList<ArrayList<String>> dataList = new ArrayList<ArrayList<String>> ();
		try {
			wb = getWorkBook(filePath);
			Sheet sheet = wb.getSheetAt(sheetIndex);
			
			dataList = readExcel(sheet, rows, cols);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dataList;
	}
}

