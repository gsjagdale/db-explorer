package com.shri.db.explorer.connection.bo;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class Entity {

	private Connection connection;

	private String name;

	protected Connection getConnection() {
		return connection;
	}

	public String getName() {
		return name;
	}

	public Entity(final Connection connection, String name) {
		assert connection != null;
		assert name != null;
		assert !name.isEmpty();

		this.connection = connection;
		this.name = name;
	}

	public void close() throws SQLException {
		this.connection.close();
	}

	public boolean isClosed() throws SQLException {
		return connection.isClosed();
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "[name=" + name + "]";
	}
}
