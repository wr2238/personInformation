package com.lu;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLParser {
	public static StringBuffer getText(ResultSet rs) {
		StringBuffer text = new StringBuffer();;
		text.append("编号\t姓名\t年龄\t婚姻状况\n");
		try {
			while(rs.next()) {
				String marital = "";
				if(rs.getInt(4)==0) {
					marital = "未婚";
				}else {
					marital = "已婚";
				}
				text.append(rs.getString(1)+"\t"+rs.getString(2)+"\t"+rs.getString(3)+"\t"+marital+"\n");
			}
		}catch(SQLException e) {
		}
		return text;
	}
	
}
