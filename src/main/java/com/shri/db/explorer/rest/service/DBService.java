package com.shri.db.explorer.rest.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.shri.db.explorer.connection.ConnectionCreator;
import com.shri.db.explorer.connection.LambdaHelper;
import com.shri.db.explorer.connection.SQLExecutor;
import com.shri.db.explorer.connection.bo.Column;
import com.shri.db.explorer.connection.bo.DBType;
import com.shri.db.explorer.connection.bo.Database;
import com.shri.db.explorer.connection.bo.Schema;
import com.shri.db.explorer.connection.bo.Table;
import com.shri.db.explorer.rest.service.bo.DBConnect;
import com.shri.db.explorer.rest.service.bo.RSasJson;

@Path("/db")
public class DBService {

	private static final String USER_URL = "USER_URL";
	private static final List<DBType> TABLE_TYPES = Arrays.asList(DBType.values());

	@Context
	private HttpServletRequest request;

	private static final boolean isValid(Object val) {
		if (val != null) {
			String val1 = val.toString();
			if (!val1.isEmpty()) {
				return true;
			}
		}
		return false;
	}

	@GET
	@Path("/listSchemas")
	@Produces(MediaType.APPLICATION_JSON)
	public Response listSchemas(@QueryParam("refresh") final boolean refresh) throws SQLException {
		Response response = null;

		Database database = getDatabase();
		if (database != null) {
			final List<Schema> schemas = database.listSchemas(refresh);
			final List<String> schemaNames = LambdaHelper.listNames(schemas);

			response = Response.ok().entity(schemaNames).build();
		} else {
			response = Response.status(Status.UNAUTHORIZED).build();
		}

		return response;
	}

	@GET
	@Path("/listTables/{schema}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response listTables(@PathParam("schema") final String schema, @QueryParam("refresh") final boolean refresh) throws SQLException {
		Response response = null;

		Database database = getDatabase();
		if (database != null) {
			final List<Table> tables = LambdaHelper.selectOneByName(database.listSchemas(false), schema).getTables(refresh);
			final List<String> tableNames = LambdaHelper.listNames(tables);

			response = Response.ok().entity(tableNames).build();
		} else {
			response = Response.status(Status.UNAUTHORIZED).build();
		}

		return response;
	}

	@GET
	@Path("/listColumns/{schema}/{table}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response listTables(@PathParam("schema") final String schema, @PathParam("table") final String tableName,
			@QueryParam("refresh") final boolean refresh) throws SQLException {
		Response response = null;

		Database database = getDatabase();
		if (database != null) {
			final List<Table> tables = LambdaHelper.selectOneByName(database.listSchemas(false), schema).getTables(false);
			final List<Column> columns = LambdaHelper.selectOneByName(tables, tableName).getColumns(refresh);

			final List<String> columnNames = LambdaHelper.listNames(columns);

			response = Response.ok().entity(columnNames).build();
		} else {
			response = Response.status(Status.UNAUTHORIZED).build();
		}

		return response;
	}

	private Database getDatabase() {
		if (isValid(request.getSession().getAttribute(USER_URL))) {
			String connUrl = request.getSession().getAttribute(USER_URL).toString();
			return DB_MAP.get(connUrl);
		}
		return null;
	}

	@GET
	@Path("/dbTypes")
	@Produces(MediaType.APPLICATION_JSON)
	public Response listDBTypes() {
		return Response.ok().entity(TABLE_TYPES).build();
	}

	private static final Map<String, Database> DB_MAP = new HashMap<String, Database>();

	@POST
	@Path("/connect")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response connectToDB(DBConnect dbConnect) throws ClassNotFoundException {

		final String connUrl = ConnectionCreator.prepareConnString(dbConnect.getDbType(), dbConnect.getHost(), dbConnect.getPort(), dbConnect.getName());
		try {
			if (DB_MAP.containsKey(connUrl)) {
				Database database = getDatabase();
				if (database == null) {
					request.getSession().setAttribute(USER_URL, connUrl);
				} else {
					database = DB_MAP.get(connUrl);
					if (database.isClosed()) {
						final Connection connection = ConnectionCreator.getConnection(connUrl, dbConnect.getUsername(), dbConnect.getPassword());
						if (connection != null) {
							request.getSession().setAttribute(USER_URL, connUrl);
							DB_MAP.put(connUrl, new Database(connection, dbConnect.getName(), dbConnect.getDbType()));
						}
					}
				}
			} else {
				Connection connection = ConnectionCreator.getConnection(connUrl, dbConnect.getUsername(), dbConnect.getPassword());

				if (connection != null) {
					request.getSession().setAttribute(USER_URL, connUrl);
					DB_MAP.put(connUrl, new Database(connection, dbConnect.getName(), dbConnect.getDbType()));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(new ConnectResponse(e.getMessage())).build();
		}
		return Response.ok().entity(new ConnectResponse("Connected")).build();
	}

	@POST
	@Path("/executeSql")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)
	public Response executeSql(String inputQuery) throws ClassNotFoundException {
		RSasJson json = null;
		try {
			Database database = getDatabase();
			String query = SQLExecutor.getInstance().selectQuery(database.getDbType(), inputQuery, 0);

			ResultSet resultSet = SQLExecutor.getInstance().executeSelectOn(database, query);
			int colCount = resultSet.getMetaData().getColumnCount();
			List<String> columns = new ArrayList<String>();

			for (int i = 1; i <= colCount; i++) {
				columns.add(resultSet.getMetaData().getColumnName(i));
			}
			json = new RSasJson(columns);

			while (resultSet.next()) {
				final String[] data = json.addNewData();
				for (int i = 1; i <= colCount; i++) {
					data[i - 1] = resultSet.getString(i);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(new ConnectResponse(e.getMessage())).build();
		}
		return Response.ok().entity(json).build();
	}

	@XmlRootElement
	class ConnectResponse {
		@XmlElement
		private String message;

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		public ConnectResponse(String message) {
			this.message = message;
		}
	}
}
