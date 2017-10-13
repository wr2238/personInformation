package com.lu;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectSQL {
	private StringBuffer ErrorMessage = new StringBuffer();
	private Connection con;
	private Statement sql;
	private String url = "jdbc:mysql://localhost:3306/jdb?useSSL=false";
	private String driver = "com.mysql.jdbc.Driver";

	public ConnectSQL() {
		init();
	}

	private void init() {
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			System.out.println(e);
		}
		try {
			con = DriverManager.getConnection(url, "root", "12345678");
			sql = con.createStatement();
		} catch (SQLException e) {
			ErrorMessage = ErrorMessage.append("数据库连接失败！请检查数据库连接！\n");
		}
	}

	protected boolean setUpdateCommand(String command) {
		class SQLNotFoundException extends Exception {
			SQLNotFoundException(String err) {
				super(err);
			}
		}
		try {
			int i = sql.executeUpdate(command);
			if (i > 0) {
				return true;
			} else if (i == 0) {
				throw new SQLNotFoundException("没有找到此记录！");
			}

			else {
				return false;
			}
		} catch (NullPointerException e) {
			ErrorMessage = ErrorMessage.append("空指针异常，数据库命令执行失败！\n");
			return false;
		} catch (SQLException e) {
			ErrorMessage = ErrorMessage.append("数据库异常，数据库命令执行失败！\n");
			return false;
		}catch(SQLNotFoundException e) {
			ErrorMessage = ErrorMessage.append("没有找到此记录！\n");
			return false;
		}
	}

	public ResultSet getQuery() {
		return QuerySQL();
	}

	private ResultSet QuerySQL() {
		try {
			return sql.executeQuery("SELECT * FROM person");
		} catch (NullPointerException e) {
			ErrorMessage = ErrorMessage.append("查询数据库失败！\n");
			return null;
		} catch (SQLException e) {
			ErrorMessage = ErrorMessage.append("查询数据库失败！\n");
			return null;
		} catch (Exception e) {
			ErrorMessage = ErrorMessage.append("查询数据库失败！\n");
			return null;
		}
	}

	public StringBuffer getErrorMessage() {
		return ErrorMessage;
	}
}