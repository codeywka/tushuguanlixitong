package javabean;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class JDBCBean {
	private static final String driver = DBConstants.driver;
	private static final String url = DBConstants.url;
	private static final String username = DBConstants.username;
	private static final String password = DBConstants.password;
	private Connection conn = null;
	private Statement stmt = null;

	public JDBCBean() {
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, username, password);
			stmt = conn.createStatement();
			System.out.println("同数据库建立连接！");
		} catch (Exception ex) {
			System.out.println("无法同数据库建立连接！");
		}
	}

	public int executeUpdate(String s) {
		int result = 0;
		try {
			System.out.println(s + "------" + stmt + "-----");
			result = stmt.executeUpdate(s);
		} catch (Exception e) {
			System.out.println("执行更新错误！");
		}
		return result;
	}

	public ResultSet executeQuery(String s) {
		ResultSet rs = null;
		try {
			rs = stmt.executeQuery(s);
		} catch (Exception e) {
			System.out.println("执行查询错误！ " + e.getMessage());
		}
		return rs;
	}

	public void close() {
		try {
			stmt.close();
			conn.close();
		} catch (Exception e) {

		}
	}
}
