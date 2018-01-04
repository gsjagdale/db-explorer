package com.shri.db.explorer.connection.bo;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Database extends Entity {

	private final Date creationTime = new Date();
	
	public Date getCreationTime() {
		return creationTime;
	}

	public Database(final Connection connection, String name) {
		super(connection, name);
	}

	private List<Schema> schemas;

	public List<Schema> listSchemas(boolean refresh) throws SQLException {
		if (schemas == null || refresh) {
			DatabaseMetaData metaData = getConnection().getMetaData();
			schemas = new ArrayList<Schema>();

			final ResultSet resultSet = metaData.getSchemas();
			while (resultSet.next()) {
				schemas.add(new Schema(getConnection(), resultSet.getString(1)));
			}
		}
		return schemas;
	}

}
