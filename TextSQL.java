package com.lu;

import java.sql.ResultSet;

public class TextSQL {
	private String name;
	private int age;
	private int marital;
	private ConnectSQL consql;
	public TextSQL() {
		consql = new ConnectSQL();
	}
	public void setInfo(String name, int age, boolean marital) {
		this.name = name;
		this.age = age;
		if (marital == true) {
			this.marital = 1;
		} else if (marital == false) {
			this.marital = 0;
		}
	}

	private String insertSQLCommand() {
		String cmd1 = "INSERT INTO person(name,age,marital)";
		String cmd2 = " VALUES('"+name+"',"+age+","+marital+")";
		String cmd = cmd1 + cmd2;
		return cmd;
	}
	public ResultSet getQuery() {
		return consql.getQuery();
	}
	public StringBuffer getErrorMessage() {
		return consql.getErrorMessage();
	}
	public boolean insertSQL() {
		boolean succ = consql.setUpdateCommand(insertSQLCommand());
		return succ;
	}
	private String deleteSQLCommand(int id) {
		String cmd = "DELETE FROM person WHERE id="+id;
		return cmd;
	}
	public boolean deleteSQL(int id) {
		boolean succ = consql.setUpdateCommand(deleteSQLCommand(id));
		return succ;
	}
}
