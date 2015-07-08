package rt.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import rt.util.ReadProperties;

public class DBManager {
	static Logger log = Logger.getLogger(DBManager.class);
//	// 定义数据库驱动程序   
//	public static final String DBDRIVER = "oracle.jdbc.driver.OracleDriver";
//	// 定义数据库的连接地址   
//	public static final String DBURL = "jdbc:oracle:thin:@(DESCRIPTION =(ADDRESS_LIST =(ADDRESS = (PROTOCOL = TCP)(HOST = 10.132.118.151)(PORT = 1521))(ADDRESS = (PROTOCOL = TCP)(HOST = 10.132.118.153)(PORT = 1521))(load_balance=no)(failover=yes))(CONNECT_DATA =(SERVICE_NAME = mesdb)))";
//	// 数据库的连接用户名   
//	public static final String DBUSER = "sj";
//	// 数据库的连接密码   
//	public static final String DBPASS = "sj";
	
	
	public static final String DBDRIVER = "oracle.jdbc.driver.OracleDriver";
	// 定义数据库的连接地址   
	public static final String DBURL = "jdbc:oracle:thin:@10.132.118.31:1521:mesprimary";
	// 数据库的连接用户名   
	public static final String DBUSER = "sajet";
	// 数据库的连接密码   
	public static final String DBPASS = "owen";
	public DBManager(){

	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) throws SQLException {
	    
	}
	
	public synchronized Connection GetConnection(int o){		
		Connection conn = null;
		try {
//			Class.forName(ReadProperties.ReadProprety("oracle.db.driver"));
//			conn = DriverManager.getConnection(ReadProperties.ReadProprety("oralce.db.url"), ReadProperties.ReadProprety("oracle.db.username"), ReadProperties.ReadProprety("oracle.db.password"));
			Class.forName(DBDRIVER);
			conn = DriverManager.getConnection(DBURL, DBUSER, DBPASS);
		
		} catch (ClassNotFoundException e) {
			log.error("没有找到数据驱动:"+e.getMessage());
		} catch (SQLException e) {
			log.error("加载数据连接异常："+e.getMessage());
		}
		return conn;
	}
	
	public Connection GetConnection(){		
		Connection conn = null;
		try {
//			Class.forName(DBDRIVER);
//			conn = DriverManager.getConnection(DBURL, DBUSER, DBPASS);
			Class.forName(ReadProperties.ReadProprety("oracle.db.driver"));
			conn = DriverManager.getConnection(ReadProperties.ReadProprety("oralce.db.url"), ReadProperties.ReadProprety("oracle.db.username"), ReadProperties.ReadProprety("oracle.db.password"));
		} catch (ClassNotFoundException e) {
			log.error("没有找到数据驱动:"+e.getMessage());
		} catch (SQLException e) {
			log.error("加载数据连接异常："+e.getMessage());
		}
		return conn;
	}
	
	/**
	 * 关闭连接
	 * @param connection
	 * @param rs
	 */
	public void closeConnection (Connection connection,ResultSet rs){
		try {
			rs.close();
			connection.close();
		} catch (SQLException e) {
			log.error("关闭数据连接异常,错误码："+e.getErrorCode()+"错误信息："+e.getMessage());
		}		
	}
	
	/**
	 * 关闭连接
	 * @param connection 数据库连接对象
	 * @param rs 结果集对象
	 * @param pstmt 预编译对象
	 */
	public void closeConnection (Connection connection, ResultSet rs, PreparedStatement pstmt){
		try {
			if(null != pstmt)
				pstmt.close();
			if(null != rs)
				rs.close();
			if(null != connection)
				connection.close();
		} catch (SQLException e) {
			log.error("关闭数据连接异常,错误码："+e.getErrorCode()+"错误信息："+e.getMessage());
		}
	}
}
