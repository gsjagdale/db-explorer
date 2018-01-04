package com.shri.db.explorer.connection.bo;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Schema extends Entity {

	private static final String[] ONLYTABLES = { "TABLE", "SYSTEM TABLE" };

	protected Schema(Connection connection, String name) {
		super(connection, name);
	}

	private List<Table> tables;

	public List<Table> getTables() throws SQLException {
		return getTables(false);
	}

	public List<Table> getTables(boolean refresh) throws SQLException {
		if (tables == null || refresh) {
			tables = new ArrayList<Table>();

			final ResultSet resultSet = getConnection().getMetaData().getTables(null, getName(), "%", ONLYTABLES);
			while (resultSet.next()) {
				// System.out.println(resultSet.getString(1) +
				// resultSet.getString(2) + resultSet.getString(3));
				tables.add(new Table(getConnection(), resultSet.getString(3)));
			}
		}
		return tables;
	}

	@Override
	public String toString() {
		return "Schema [name=" + getName() + "]";
	}
}
