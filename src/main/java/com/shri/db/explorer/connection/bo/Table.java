package com.shri.db.explorer.connection.bo;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Table extends Entity {

	public Table(Connection connection, String name) {
		super(connection, name);
	}

	private List<Column> columns;

	public List<Column> getColumns(boolean refresh) throws SQLException {

		if (columns == null || refresh) {
			columns = new ArrayList<Column>();
			final ResultSet resultSet = getConnection().getMetaData().getColumns(null, null, getName(), null);
			while (resultSet.next()) {
				final String name = resultSet.getString("COLUMN_NAME");
				final String type = resultSet.getString("TYPE_NAME");
				// final int size = resultSet.getInt("COLUMN_SIZE");

				final Column column = new Column(getConnection(), name, type);

				columns.add(column);
			}
		}
		return columns;
	}

}
