package com.shri.db.explorer.rest.service.bo;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.shri.db.explorer.connection.bo.DBType;

@XmlRootElement
public class DBConnect {

	@XmlElement
	private DBType dbType;
	@XmlElement
	private String host;
	@XmlElement
	private int port;
	@XmlElement
	private String name;
	@XmlElement
	private String username;
	@XmlElement
	private String password;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public DBType getDbType() {
		return dbType;
	}

	public void setDbType(DBType dbType) {
		this.dbType = dbType;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
