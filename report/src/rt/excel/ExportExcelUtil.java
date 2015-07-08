package rt.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExportExcelUtil {

	private static Logger log = Logger.getLogger(ExportExcelUtil.class);
	private XSSFWorkbook wb = null;
	private XSSFSheet sheet = null;
	private XSSFRow row = null;
	
	private FileOutputStream out = null;
	
	/**
	 * 利用List<LinkedHashMap>导出Excel，保证顺序
	 * @param filePath 名字
	 * @param listMaps Map类数据集
	 * @return 输出filePath 读入文件目录，fileName 文件显示名称
	 */
	public boolean exportExcelOrderly(String filePath, List<LinkedHashMap<String,Object>> listMaps,List<String> column){
		wb = new XSSFWorkbook();
		sheet = wb.createSheet();
		row = sheet.createRow(0);
		createColumnTitle(row,column);

		int columns = 0;
		try {
			for(int i = 1;i<=listMaps.size(); i++){
				row = sheet.createRow(i);
				LinkedHashMap<String,Object> map = listMaps.get(i-1);
				Set<String> set = map.keySet();
				int index =0;
				for(Iterator<String> iter = set.iterator(); iter.hasNext();){//取所有键
					String key = (String)iter.next();
					String value = (String)map.get(key);//根据键取值
					row.createCell(index).setCellValue(value);//设置到cell中的值
					index ++;
				}
				columns = index;
			}
			for(int i =0;i<columns ;i++){//设置自动列宽
				sheet.autoSizeColumn(i);
			}

			out = new FileOutputStream(filePath);
			wb.write(out);
			out.close();
			wb.close();
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return false;
		}
	}
	
	/**
	 * 利用List<LinkedHashMap>导出Excel，保证顺序
	 * @param filePath 名字
	 * @param listMaps Map类数据集
	 * @param sheetName sheet名称
	 * @return 输出filePath 读入文件目录，fileName 文件显示名称
	 */
	public boolean exportExcelOrderly(String filePath, List<LinkedHashMap<String,Object>> listMaps,List<String> column,String sheetName){
		wb = new XSSFWorkbook();
		sheet = wb.createSheet(sheetName);
		row = sheet.createRow(0);
		createColumnTitle(row,column);

		int columns = 0;
		try {
			for(int i = 1;i<=listMaps.size(); i++){
				row = sheet.createRow(i);
				LinkedHashMap<String,Object> map = listMaps.get(i-1);
				Set<String> set = map.keySet();
				int index =0;
				for(Iterator<String> iter = set.iterator(); iter.hasNext();){//取所有键
					String key = (String)iter.next();
					String value = (String)map.get(key);//根据键取值
					row.createCell(index).setCellValue(value);//设置到cell中的值
					index ++;
				}
				columns = index;
			}
			for(int i =0;i<columns ;i++){//设置自动列宽
				sheet.autoSizeColumn(i);
			}

			out = new FileOutputStream(filePath);
			wb.write(out);
			out.close();
			wb.close();
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return false;
		}
	}
	
	/**
	 * 创建Excel。如果已经存在文件，会自动自增sheet。如果文件不存在会新建Excel。
	 * @param report 报表文件
	 * @param listMaps 值集合
	 * @param column 列头
	 * @return
	 */
	public boolean exportExcelAutoSheet(String report, List<LinkedHashMap<String,Object>> listMaps,List<String> column,String sheetName){
		
		InputStream ins = null;
		if(!new File(report).exists()){
			System.out.println("文件不存在，以创建");
			wb = new XSSFWorkbook();
		} else {
			System.out.println("文件存在，读取流");
			try {
				ins = new FileInputStream(report);
				wb = new XSSFWorkbook(ins);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		sheet = wb.createSheet(sheetName);
		row = sheet.createRow(0);
		createColumnTitle(row,column);

		int columns = 0;
		try {
			for(int i = 1;i<=listMaps.size(); i++){
				row = sheet.createRow(i);
				LinkedHashMap<String,Object> map = listMaps.get(i-1);
				Set<String> set = map.keySet();
				int index =0;
				for(Iterator<String> iter = set.iterator(); iter.hasNext();){//取所有键
					String key = (String)iter.next();
					String value = (String)map.get(key);//根据键取值
					row.createCell(index).setCellValue(value);//设置到cell中的值
					index ++;
				}
				columns = index;
			}
			for(int i =0;i<columns ;i++){//设置自动列宽
				sheet.autoSizeColumn(i);
			}

			out = new FileOutputStream(report);
			wb.write(out);
			out.close();
			wb.close();
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return false;
		}
	}
	
	/**
	 * 创建Excel。创建首页
	 * @param report 报表文件
	 * @param listMaps 值集合
	 * @param column 列头
	 * @param sheetIndex sheet索引
	 * @return
	 */
	public boolean exportExcelAutoSheet(String report, List<LinkedHashMap<String,Object>> listMaps,List<String> column,int sheetIndex){
		
		InputStream ins = null;
		if(!new File(report).exists()){
			System.out.println("文件不存在，以创建");
			wb = new XSSFWorkbook();
		} else {
			System.out.println("文件存在，读取流");
			try {
				ins = new FileInputStream(report);
				wb = new XSSFWorkbook(ins);
			} catch (FileNotFoundException e) {
				log.info(""+e.getMessage());
				e.printStackTrace();
			} catch (IOException e) {
				log.info(""+e.getMessage());
				e.printStackTrace();
			}
		}
		
		sheet = wb.createSheet("index");
		wb.setSheetOrder(sheet.getSheetName(), sheetIndex);
		row = sheet.createRow(0);
		createColumnTitle(row,column);

		int columns = 0;
		try {
			for(int i = 1;i<=listMaps.size(); i++){
				row = sheet.createRow(i);
				LinkedHashMap<String,Object> map = listMaps.get(i-1);
				Set<String> set = map.keySet();
				int index =0;
				for(Iterator<String> iter = set.iterator(); iter.hasNext();){//取所有键
					String key = (String)iter.next();
					String value = (String)map.get(key);//根据键取值
					row.createCell(index).setCellValue(value);//设置到cell中的值
					index ++;
				}
				columns = index;
			}
			for(int i =0;i<columns ;i++){//设置自动列宽
				sheet.autoSizeColumn(i);
			}

			out = new FileOutputStream(report);
			wb.write(out);
			out.close();
			wb.close();
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return false;
		}
	}
	
	/**
	 * 创建标题列
	 * @param row 标题行
	 * @param columns 标头集合
	 */
	protected void createColumnTitle(XSSFRow row,List<String> columns){
		for(int i = 0; i < columns.size(); i++){
			row.createCell(i).setCellValue(columns.get(i));
		}		
	}
}