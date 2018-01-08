package com.shri.db.explorer.connection;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.shri.db.explorer.connection.bo.DBType;
import com.shri.db.explorer.connection.bo.Database;

public final class SQLExecutor {

	public static final String SEMICOLON = ";";

	private static final String DERBY_SELECT = " OFFSET %s ROWS FETCH NEXT %s ROWS ONLY";
	private static final int ROW_COUNT = 10;

	private static final SQLExecutor EXECUTOR = new SQLExecutor();

	public static final SQLExecutor getInstance() {
		return EXECUTOR;
	}

	private SQLExecutor() {
	}

	public final String selectQuery(DBType dbType, String selectQuery, int index) {
		if (dbType == null || selectQuery == null || selectQuery.isEmpty()) {
			return null;
		}
		selectQuery = selectQuery.trim().toUpperCase();

		StringBuilder finalQuery = new StringBuilder(selectQuery);
		if (selectQuery.endsWith(SEMICOLON)) {
			finalQuery = new StringBuilder(selectQuery.substring(0, selectQuery.length() - 1));
		}

		switch (dbType) {
		case DERBY:
			final String sufix = "";//String.format(DERBY_SELECT, ROW_COUNT * index, ROW_COUNT);
			finalQuery.append(sufix);
			break;
		default:
			break;
		}

		return finalQuery.toString();
	}

	public ResultSet executeSelectOn(Database database, String selectQuery) throws SQLException {
		return database.getConnection().prepareStatement(selectQuery).executeQuery();
	}
}
