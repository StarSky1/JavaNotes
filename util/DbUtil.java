

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.*;

import org.apache.commons.dbcp2.BasicDataSourceFactory;

//* dbcp构建sql server数据库连接池需要的jar包 commons-logging-1.2.jar commons-pool2-2.3.jar
//* sqljdbc42.jar commons-dbcp2-2.5.0.jar
public class DbUtil {


	//构建sql server 数据库连接池
	private static DataSource ds = null;

	static {
		try {
			File file = new File(System.getProperty("user.dir") + File.separator + "config" + File.separator + "dbcp.properties");
			if (file.exists()) {
//                System.out.println("dbcp配置文件路径为==>" + file.getCanonicalPath());
				LOGINFO.info("dbcp配置文件路径为==>" + file.getCanonicalPath());
			}
			InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "UTF-8");
			Properties prop = new Properties();
			prop.load(new BufferedReader(isr));
			ds = BasicDataSourceFactory.createDataSource(prop);
		} catch (Exception e) {
			e.printStackTrace();
			LOGERROR.error(e.getLocalizedMessage());
			LOGINFO.info(e.getLocalizedMessage());
		}
	}

	public static Connection getConnection() throws SQLException {
		if (ds == null) {
			return null;
		} else {
			return ds.getConnection();
		}
	}

	/**
	 * 将查询结果封装成List。<br>
	 * List中元素类型为封装一行数据的Map，Map key为字段名（大写），value为相应字段值
	 * @param rs ResultSet
	 * @return List
	 * @throws SQLException
	 */
	public static List resultSetToList(ResultSet rs) throws SQLException{
		if (rs==null) return Collections.EMPTY_LIST;


		ResultSetMetaData md = rs.getMetaData();
		int columnCount = md.getColumnCount();


		List list = new ArrayList();
		Map rowData;
		while (rs.next()){
			rowData = new HashMap(columnCount);
			for (int i=1; i<=columnCount; i++){
				rowData.put(md.getColumnName(i),rs.getObject(i));
			}
			list.add(rowData);
		}
		return list;
	}

	/**
	 * 关闭ResultSet、Statement和Connection
	 * @param rs ResultSet to be closed
	 * @param stmt Statement or PreparedStatement  to be closed
	 * @param conn Connection  to be closed
	 */
	public static void close(ResultSet rs, PreparedStatement stmt, Connection conn){
		if (rs != null) try{
			rs.close();
		}catch(SQLException ex){
			ex.printStackTrace();
		}
		if (stmt != null) try{
			stmt.close();
		}catch(SQLException ex){
			ex.printStackTrace();
		}
		if (conn != null) try{
			conn.close();
		}catch(SQLException ex){
			ex.printStackTrace();
		}
	}
}