package rt.excel;


import java.util.ArrayList;

public class PoiExcelTest {
	// *************************************************
	// ================以下为测试代码====================
	// *************************************************
	public static void main(String[] args){
		String file="F:/Tigerwip.xlsx";
		// 获取Excel文件的sheet列表
//		testGetSheetList(file);
		
		// 获取Excel文件的第1个sheet的内容
//		testReadExcel(file, 0);
		
		// 获取Excel文件的第2个sheet的第2、4-7行和第10行及以后的内容
//		testReadExcel(file, 1, "2,4-7,10-");
		
		// 获取Excel文件的第3个sheet中a,b,g,h,i,j等列的所有内容
		testReadExcel(file, 2, new String[] {"b","c","e"});
		
		// 获取Excel文件的第4个sheet的第2、4-7行和第10行及以后，a,b,g,h,i,j等列的内容
//		testReadExcel(file, 3, "2,4-7,10-", new String[] {"a","b","g","h","i","j"});
	}
	
	// 测试获取sheet列表
	private static void testGetSheetList(String filePath) {
		PoiExcelHelper helper = getPoiExcelHelper(filePath);
		
		// 获取Sheet列表
		ArrayList<String> sheets = helper.getSheetList(filePath);
		
		// 打印Excel的Sheet列表
		printList(filePath, sheets);
	}
	
	// 获取Excel文件的第N个sheet的内容
	private static void testReadExcel(String filePath, int sheetIndex) {
		PoiExcelHelper helper = getPoiExcelHelper(filePath);
		
		// 读取excel文件数据
		ArrayList<ArrayList<String>> dataList = helper.readExcel(filePath, sheetIndex);
		
		// 打印单元格数据
		printBody(dataList);
	}
	
	// 测试Excel读取
	private static void testReadExcel(String filePath, int sheetIndex, String rows) {
		PoiExcelHelper helper = getPoiExcelHelper(filePath);
		
		// 读取excel文件数据
		ArrayList<ArrayList<String>> dataList = helper.readExcel(filePath, sheetIndex, rows);
		
		// 打印单元格数据
		printBody(dataList);
	}
	
	// 获取Excel文件的第3个sheet中a,b,g,h,i,j等列的所有内容
	/**
	 * @param filePath 路径
	 * @param sheetIndex sheet下标
	 * @param columns 列数
	 */
	public static void testReadExcel(String filePath, int sheetIndex, String[] columns) {
		PoiExcelHelper helper = getPoiExcelHelper(filePath);
		
		// 读取excel文件数据
		ArrayList<ArrayList<String>> dataList = helper.readExcel(filePath, sheetIndex, columns);
		
		// 打印列标题
		printHeader(columns);
		
		// 打印单元格数据
		printBody(dataList);
	}
	
	// 测试Excel读取
	private static void testReadExcel(String filePath, int sheetIndex, String rows, String[] columns) {
		PoiExcelHelper helper = getPoiExcelHelper(filePath);
		
		// 读取excel文件数据
		ArrayList<ArrayList<String>> dataList = helper.readExcel(filePath, sheetIndex, rows, columns);
		
		// 打印列标题
		printHeader(columns);
		
		// 打印单元格数据
		printBody(dataList);
	}
	
	// 获取Excel处理类
	public static PoiExcelHelper getPoiExcelHelper() {
		PoiExcelHelper helper;
//		if(filePath.indexOf(".xlsx")!=-1) {
//			helper = new PoiExcel2k7Helper();
//		}else {
			helper = new PoiExcel2k3Helper();
//		}
		return helper;
	}
	
	// 获取Excel处理类
	public static PoiExcelHelper getPoiExcelHelper(String filePath) {
		PoiExcelHelper helper;
//		if(filePath.indexOf(".xlsx")!=-1) {
//			helper = new PoiExcel2k7Helper();
//		}else {
			helper = new PoiExcel2k3Helper();
//		}
		return helper;
	}

	// 打印Excel的Sheet列表
	private static void printList(String filePath, ArrayList<String> sheets) {
		System.out.println();
		for(String sheet : sheets) {
			System.out.println(filePath + " ==> " + sheet);
		}
	}

	// 打印列标题
	private static void printHeader(String[] columns) {
		System.out.println();
		for(String column : columns) {
			System.out.print("\t\t" + column.toUpperCase());
		}
	}

	// 打印单元格数据
	private static void printBody(ArrayList<ArrayList<String>> dataList) {
		int index = 0;
		for(ArrayList<String> data : dataList) {
			index ++;
			System.out.println();
			System.out.print(index);
			for(String v : data) {
				System.out.print("\t\t" + v);
			}
		}
	}
}