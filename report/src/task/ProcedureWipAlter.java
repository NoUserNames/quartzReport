/**
 * 
 */
package task;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.quartz.InterruptableJob;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.UnableToInterruptJobException;

import rt.connection.DBManager;
import rt.util.TUtil;

/**
 * 现场找料报表定时导数据存储过程
 * @author 张强
 *
 */
public class ProcedureWipAlter implements InterruptableJob{

	private static Logger log = Logger.getLogger(ProcedureWipAlter.class);
	
	Thread thread = null;
	
	public static void main(String[] args){
		try {
			new ProcedureWipAlter().execute(null);
		} catch (JobExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		thread = Thread.currentThread();
		DBManager db = new DBManager();
		CallableStatement cs = null;
		Connection connection = db.GetConnection();
		try {
			cs = connection.prepareCall("{call SAJET.sj_insert_2h_wip_Lincoln}");
			cs.execute();
			log.info("存储过程 SAJET.sj_insert_2h_wip_Lincoln 执行成功。");
		} catch (SQLException e) {
			log.error("存储过程 SAJET.sj_insert_2h_wip_Lincoln 执行异常："+e.getMessage());
//			e.printStackTrace();
			TUtil.sendFaultMail("SAJET.sj_insert_2h_wip_Lincoln");
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				log.error("ProcedureWipAlter关闭数据库连接异常："+e.getMessage());
				e.printStackTrace();
			}
		}
	}

	@Override
	public void interrupt() throws UnableToInterruptJobException {
		if(null != thread)
			this.thread.interrupt();
	}

}
