package com.shri.db.explorer.connection.bo;

import java.sql.Connection;

public class Column extends Entity {

	public Column(Connection connection, String name) {
		super(connection, name);
	}

	public Column(Connection connection, String name, String type) {
		super(connection, name);
		this.type = type;
	}

	private String type;

	public String getType() {
		return type;
	}

	@Override
	public String toString() {
		return "Column [type=" + type + ", name=" + getName() + "]";
	}
	
	
}
