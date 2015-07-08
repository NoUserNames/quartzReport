package reprot;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
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
 * @author Qiang1_Zhang
 * DOE物料追踪实例
 */
public class DOETrace implements InterruptableJob{
	static Logger log = Logger.getLogger(DOETrace.class);	
	Thread selfThread = null;
	
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		selfThread = Thread.currentThread();
		log.info("DOE WIP报表开始执行");
		new File(ReadProperties.ReadProprety("wip.doe.report")).delete();

		String source = ReadProperties.ReadProprety("wip.doe.report");
		doeReport(source);
		log.info("DOE WIP报表生成完成");
		
		if(!TUtil.transferToServer("wip.doe.directory","wip.doe.report","yyyy-MM-dd")){
			TUtil.sendFaultMail(ReadProperties.ReadProprety("mail.fault.to"));
			log.error("DOE报表上传失败");
		}else
			log.info("DOE报表上传完毕");
	}

	@Override
	public void interrupt() throws UnableToInterruptJobException {
		if(null != selfThread)
			this.selfThread.interrupt();
	}
	
	/**
	 * 获取DOE源数据,生成报表
	 * @param report 报表路径
	 */
	public boolean doeReport(String report){
		String sql ="select m.model_name,e.emp_no,"+
		   " n.serial_number DOE_SN,"+
	       " n.current_status,"+
	       " n.work_flag,"+
			" to_char(min(v.in_process_time), 'yyyy-mm-dd hh24:mi:ss') as LaserTime,"+
			" t.terminal_name as NowTerminal,"+
			" to_char(max(n.out_process_time), 'yyyy-mm-dd hh24:mi:ss') as LastScanTime,"+
			" e.emp_name as SacnEmp"+
			" from sajet.sys_process  b,"+
			" sajet.g_sn_status  n,"+
			" sajet.g_wo_base    c,"+
			" sajet.sys_emp      e,"+
			" sajet.sys_terminal t,"+
			" sajet.g_sn_travel  v,"+
			" sajet.sys_part m"+
			" where b.process_id in (select process_id from sajet.sys_process where upper(process_name) not like '%SHIPPING%' and upper(process_name) not like '%WAREHOUSE%' and upper(process_name) not like '%PRODUCT TRACKING%' and enabled ='Y')"+
			" and n.work_order = c.work_order"+
			" and n.enabled is null"+
			" and c.wo_type = 'DOE'"+
			" and b.process_id = n.process_id"+
			" and e.emp_id = n.emp_id"+
			" and t.terminal_id = n.terminal_id"+
			" and v.serial_number = n.serial_number"+
			" and n.model_id = m.part_id"+
			" and n.out_process_time >= to_date('2015-01-01', 'YYYY-MM-DD')"+
			" and n.out_process_time < to_date('"+TUtil.format("yyyy-MM-dd")+"', 'YYYY-MM-DD')"+
			" group by n.serial_number,n.current_status,n.work_flag, e.emp_name, t.terminal_name,m.model_name,e.emp_no"+
			" order by m.model_name";
		//TUtil.print(sql);
		DBManager db = new DBManager();
		Connection connection  = db.GetConnection(0);
		List<LinkedHashMap<String,Object>> doeList = new ArrayList<LinkedHashMap<String,Object>>();
		List<String> models = new ArrayList<String>();
		ResultSet rs = null;
		try {
			rs = connection.createStatement().executeQuery(sql);
			while(rs.next()){
				LinkedHashMap<String,Object> doe = new LinkedHashMap<String,Object>();
				doe.put("model_name",rs.getString("model_name"));
				models.add(rs.getString("model_name"));
				doe.put("DOE_SN",rs.getString("DOE_SN"));
				String curr_status ="0",work_flag = "0";
				curr_status = rs.getString("current_status");
				work_flag = rs.getString("work_flag");
				if(curr_status.equals("0") && work_flag.equals("0")){
					doe.put("status","OK");
				}
				if(curr_status.equals("1") && work_flag.equals("0")){
					doe.put("status","NG");
				}
				if(curr_status.equals("1") && work_flag.equals("1")){
					doe.put("status","SCRAP");
				}
				doe.put("LaserTime",rs.getString("LaserTime"));
				doe.put("SacnEmp",rs.getString("SacnEmp"));
				doe.put("NowTerminal",rs.getString("NowTerminal"));
				doe.put("LastScanTime",rs.getString("LastScanTime"));
				doeList.add(doe);

			}
			
			//取出机种清单
			HashSet<String> h  = new  HashSet<String>(models); 
			models.clear(); 
			models.addAll(h); 
			Collections.sort(models);
			log.info("models="+models);
			//列头清单
			List<String> columns = new ArrayList<String>();
			columns.add("机种信息");
			columns.add("产品序列号");
			columns.add("当前状态");
			columns.add("镭雕时间");
			columns.add("扫描员工");
			columns.add("当前站点");
			columns.add("最后扫描时间");
			List<LinkedHashMap<String,Object>> listSub = new ArrayList<LinkedHashMap<String,Object>>();
			for(String sheetName : models){//机种
				List<LinkedHashMap<String,Object>> tmpList = new ArrayList<LinkedHashMap<String,Object>>();
				for(LinkedHashMap<String,Object> doeMap : doeList){//数据集合
					if(sheetName.equals(doeMap.get("model_name").toString())){//匹配当前机种
						tmpList.add(doeMap);
					}
				}
				LinkedHashMap<String,Object> sub = new LinkedHashMap<String,Object>();
				sub.put("modelName", sheetName);
				sub.put("size", tmpList.size()+"");
				listSub.add(sub);
				new ExportExcelUtil().exportExcelAutoSheet(report, tmpList, columns, sheetName);
			}
			columns = new ArrayList<String>();
			columns.add("料号");
			columns.add("数量");
			new ExportExcelUtil().exportExcelAutoSheet(report, listSub, columns, 0);
		} catch (SQLException e) {
			log.error("获取DOE物料报表数据出错:"+e.getMessage());
			e.printStackTrace();
			return false;
		} finally {
			db.closeConnection(connection, rs);
		}
		return true;
	}
	
	
}