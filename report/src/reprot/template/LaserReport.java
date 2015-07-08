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
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import rt.connection.DBManager;
import rt.excel.PoiExcel2k3Helper;
import rt.excel.PoiExcelHelper;
/**
 * @author Qiang1_Zhang
 * 镭射类报表基础类
 */
public class LaserReport {
	private static Logger log = Logger.getLogger(LaserReport.class);
	
	/**
	 * @param template
	 * @param reprot
	 * @param safeTime
	 * @param Sproscess
	 */
	public void ExportReport(String template,String reprot,int safeTime,String ...Sproscess){
		StringBuffer sql = new StringBuffer();
		sql.append("select c.process_name,count(b.serial_number) CNT From sajet.g_sn_travel a, sajet.g_sn_status b, sajet.sys_process c  where a.process_id in (");
		String c ="";
		for(String str : Sproscess){
			c += "'"+str+"'"+",";
		}
		c = c.substring(0,c.lastIndexOf("'")+1);
		sql.append(c);
		sql.append(")  and a.out_process_time>=to_date(to_char(sysdate-"+safeTime+",'yyyy-mm-dd')|| '08:28','yyyy-mm-dd hh24:mi')  and a.out_process_time<to_date(to_char(sysdate-"+safeTime+",'yyyy-mm-dd')|| '20:28','yyyy-mm-dd hh24:mi') and a.serial_number=b.serial_number and b.ENABLED is null and  c.enabled='Y' and b.process_id=c.process_id group by c.process_name");
		Map<String, String> map = new HashMap<String,String>();
		try {
			DBManager dbManger = new DBManager();
			Connection conn = dbManger.GetConnection(0);
			ResultSet rs = conn.createStatement().executeQuery(sql.toString());
			while (rs.next()){
				map.put(rs.getString("process_name"), rs.getString("CNT"));
			}
			rs.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			
		}
		PoiExcelHelper helper = new PoiExcel2k3Helper();
		// 读取A两列数据
		ArrayList<ArrayList<String>> dataList = helper.readExcel(template, 0, new String[]{"a"});
		InputStream ins =null;
		XSSFWorkbook wb = null;
		FileOutputStream out = null;
		XSSFSheet sheet = null;
		XSSFRow row = null;
		XSSFCell cell = null;
		String[] sheet2 = null;
		List<String[]> listSheet2 = new ArrayList<String[]>();
		Connection conn= null;
		ResultSet rs = null;
		DBManager dbManger =null;
		try {
			ins = new FileInputStream(template);;
			wb = new XSSFWorkbook(ins);
			sheet = wb.getSheetAt(0);
			PreparedStatement pstmt = null;
			//写第1个sheet
			for (int i=1;i<dataList.size();i++){
				//查到数据
				if(map.get(dataList.get(i).get(0))!=null){
					row = sheet.getRow(i);
					cell=row.createCell(1);
					cell.setCellValue(Double.parseDouble(map.get(dataList.get(i).get(0)).toString()));
					String sqlList ="select b.serial_number,"+
				       " c.process_name,"+
					   " d.terminal_name,"+
					   " to_char(b.out_process_time, 'yyyy-mm-dd hh24:mi') as out_process_time,"+
						"  sysdate,"+
						"  b.CURRENT_STATUS"+
						"  From sajet.g_sn_travel  a,"+
						"   sajet.g_sn_status  b,"+
						"   sajet.sys_process  c,"+
						"  sajet.sys_terminal d"+
						"  where a.process_id in (?";
					for(int j=0;j<Sproscess.length -1 ;j++){
						sqlList +=",?";
					}
					sqlList +=" )  and a.out_process_time >="+
						"    to_date(to_char(sysdate - 8, 'yyyy-mm-dd') || '08:28',"+
						"       'yyyy-mm-dd hh24:mi')"+
						"  and a.out_process_time <"+
						"  to_date(to_char(sysdate - 8, 'yyyy-mm-dd') || '20:28',"+
						"   'yyyy-mm-dd hh24:mi')"+
						"  and a.serial_number = b.serial_number"+
						"  and b.terminal_id = d.terminal_id"+
						"  and c.process_name = ?"+
						"  and b.ENABLED is null"+
						"  and b.process_id = c.process_id";
					dbManger = new DBManager();

					conn = dbManger.GetConnection(0);
					
					pstmt = conn.prepareStatement(sqlList);
					for(int j=1;j<=Sproscess.length;j++){
						pstmt.setString(j, Sproscess[j-1]);
					}
					pstmt.setString(Sproscess.length +1, dataList.get(i).get(0));
					rs = pstmt.executeQuery();
					pstmt.clearParameters();
					while (rs.next()){
						int sheet2Index=0;
						sheet2 =new String[]{"","","","","",""};
						sheet2[sheet2Index] = rs.getString(sheet2Index+1);
						sheet2[sheet2Index+1] = rs.getString(sheet2Index+2);
						sheet2[sheet2Index+2] = rs.getString(sheet2Index+3);
						sheet2[sheet2Index+3] = rs.getString(sheet2Index+4);
						sheet2[sheet2Index+4] = rs.getString(sheet2Index+5);
						sheet2[sheet2Index+5] = rs.getString(sheet2Index+6);
						listSheet2.add(sheet2);
						sheet2Index++;
					}
					pstmt.close();
					rs.close();
					conn.close();
				}
			}
			sheet.setForceFormulaRecalculation(true);
			//写第2个sheet	
			sheet = wb.getSheetAt(1);
			for(int i=1;i<=listSheet2.size();i++){
				row = sheet.createRow(i);
				for (int col =0;col<6;col++){
					cell=row.createCell(col);
					cell.setCellValue(listSheet2.get(i-1)[col]);
				}
			}
			//输出报表
			out = new FileOutputStream(reprot);
			wb.write(out);
			ins.close();
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			log.error("没有找到输入模板！"+e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			log.error("生成报表文件时出错！"+e.getMessage());
		} catch (SQLException e) {
			e.printStackTrace();
			log.error("拉取清单执行SQL时出错！"+e.getMessage());
		}finally{
		}
	}
}