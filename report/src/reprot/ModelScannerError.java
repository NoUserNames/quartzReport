/**
 * 
 */
package reprot;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.InterruptableJob;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.UnableToInterruptJobException;

import rt.connection.DBManager;
import rt.excel.ExportExcelUtil;
import rt.util.ReadProperties;
import rt.util.TUtil;

/**
 * 机种错扫报警报表
 * @author 张强
 *
 */
public class ModelScannerError implements InterruptableJob{

	private static Logger log = Logger.getLogger(ModelScannerError.class);
	Thread selfThread = null;
	
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		selfThread = Thread.currentThread();
		
		//生成报表
		log.info("ModelScannerError开始执行");

		String reportPath = TUtil.getURL()+ReadProperties.ReadProprety("modelScannerError.report");
		
		if(modelScannerError(reportPath)){
			log.info("ModelScannerError报表生成完毕");
			
			if(!TUtil.transferToServer("wip.scanErr.directory","modelScannerError.report","yyyy-MM-dd_HH_mm")){
				TUtil.sendFaultMail(this.getClass().getName());
				log.error(this.getClass().getName()+"上传失败");
			}else
				log.info(this.getClass().getName()+"上传完毕");
		} else {
			log.info("ModelScannerError没有发现错扫数据。");
		}
	}

	@Override
	public void interrupt() throws UnableToInterruptJobException {
		if(null != selfThread)
			this.selfThread.interrupt();
	}
	
	/**
	 * 查询是否有错扫数据
	 * @param reprot 报表目录
	 * @return true || false
	 */
	public boolean modelScannerError(String reprot){
		String sql ="select * "+
				  "from (select e.sdate, "+
				               "e.sn, "+
				               "f.model_name, "+
				               "e.pdline_name, "+
				               "e.terminal_name, "+
				               "e.datetime "+
				          "from (select a.trev_type sn, "+
				                       "c.pdline_name, "+
				                       "b.terminal_name, "+
				                       "to_char(a.update_time, 'yyyy/mm/dd hh24:mi:ss') as datetime, "+
				                       "to_char(a.update_time, 'yyyy-mm-dd') as sdate "+
				                  "from sajet.g_trev_log a "+
				                 "inner join sajet.sys_terminal b on a.terminal_id = b.terminal_id "+
				                 "inner join sajet.sys_pdline c on b.pdline_id = c.pdline_id "+
				                 "where c.enabled = 'Y' "+
				                   "and b.enabled = 'Y' "+
				                   "and a.update_time >= "+
				                       "to_date('"+TUtil.format(new Date(new Date().getTime() - 600000), "yyyy-M-dd HH:mm:ss")+"', 'yyyy/mm/dd hh24:mi:ss') "+
				                   "and a.trev like '%Not Match%' "+
				                 "order by a.update_time) e "+
				         "inner join sajet.g_sn_status d on e.sn = d.serial_number "+
				         "inner join sajet.sys_part f on d.model_id = f.part_id) "+
				 "where substr(model_name, 1, 2) <> substr(terminal_name, 1, 2) ";

		DBManager db = new DBManager();
		Connection connection = null;
		ResultSet rs = null;
		List<LinkedHashMap<String,Object>> mapList = new ArrayList<LinkedHashMap<String,Object>>();
		try {
			connection = db.GetConnection(0);
			rs = connection.prepareStatement(sql).executeQuery();
			while(rs.next()){
				LinkedHashMap<String,Object> map = new LinkedHashMap<String,Object>();
				map.put("sdate", rs.getString("sdate"));
				map.put("sn", rs.getString("sn"));
				map.put("model_name", rs.getString("model_name"));
				map.put("pdline_name", rs.getString("pdline_name"));
				map.put("terminal_name", rs.getString("terminal_name"));
				map.put("datetime", rs.getString("datetime"));

				mapList.add(map);
			}
		} catch (SQLException e) {
			log.error(""+e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				connection.close();
			} catch (SQLException e) {
				log.error(""+e.getMessage());
				e.printStackTrace();
			}
		}
		if(mapList.size()!=0){
			genernateReport(reprot, mapList);
			return true;
		}else {
			return false;
		}
	}
	
	/**
	 * 生成报表
	 * @param report
	 * @param mapList
	 */
	public void genernateReport(String report, List<LinkedHashMap<String,Object>> mapList){
		ExportExcelUtil excelUtil = new ExportExcelUtil();
		List<String> listColumn = new ArrayList<String>();
		listColumn.add("日期");
		listColumn.add("二維碼");
		listColumn.add("物料所屬機種");
		listColumn.add("線別");
		listColumn.add("被卡下的站點");
		listColumn.add("被卡下的時間");
		excelUtil.exportExcelOrderly(report, mapList, listColumn);
	}
}