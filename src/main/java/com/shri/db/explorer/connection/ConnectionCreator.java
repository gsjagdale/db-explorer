package com.shri.db.explorer.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.shri.db.explorer.connection.bo.DBType;

public class ConnectionCreator {

	private ConnectionCreator() {

	}

	public static final Connection getConnection(String connString, String userName, String password) throws SQLException {
		return DriverManager.getConnection(connString, userName, password);
	}

	public static final String prepareConnString(DBType dbType, String host, int port, String dbName) throws ClassNotFoundException {
		StringBuilder builder = new StringBuilder();
		switch (dbType) {
		case DERBY:
			Class.forName("org.apache.derby.jdbc.ClientDriver");
			builder.append("jdbc:derby://").append(host).append(":").append(port).append("/").append(dbName);
			break;
		case POSTGRES:
		    Class.forName("org.postgresql.Driver");
		    builder.append("jdbc:postgresql://").append(host).append(":").append(port).append("/").append(dbName).append("?sslmode=require");
		    break;
		default:
			break;
		}
		return builder.toString();
	}
}