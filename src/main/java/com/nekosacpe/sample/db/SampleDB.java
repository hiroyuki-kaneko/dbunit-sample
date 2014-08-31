package com.nekosacpe.sample.db;

import java.io.Closeable;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class SampleDB {

	private static final String DEFAULT_DB_DRIVER = "com.mysql.jdbc.Driver";

	private static final String PROPERTY_DB_DRIVER = "com.nekoscape.db.driver";

	private static final String DEFAULT_JDBC_URI = "jdbc:mysql://localhost/test";

	private static final String PROPERTY_JDBC_URI = "com.nekoscape.db.uri";

	private static final String SQL_SELECT_ALL = "SELECT * FROM TEST";

	private SampleDB() {
		String driverClass = System.getProperty(PROPERTY_DB_DRIVER,
				DEFAULT_DB_DRIVER);
		// Load Driver
		try {
			Class.forName(driverClass);
		} catch (ClassNotFoundException e) {
			// curners cutting
			throw new RuntimeException(e);
		}
	}

	public static SampleDB getInstance() {
		return new SampleDB();
	}

	public Connection getConnection() throws SQLException {
		return DriverManager.getConnection(System.getProperty(
				PROPERTY_JDBC_URI, DEFAULT_JDBC_URI));
	}

	public void executeQuery() throws SQLException {
		Connection con = getConnection();

		Statement stm = con.createStatement();
		try {
			ResultSet resultSet = stm.executeQuery(SQL_SELECT_ALL);
			ResultSetMetaData metaData = resultSet.getMetaData();

			int num = metaData.getColumnCount();
			while (resultSet.next()) {
				StringBuilder builder = new StringBuilder();
				for (int i = 1; i <= num; i++) {
					builder.append(resultSet.getString(i)).append(",");
				}
				builder.deleteCharAt(builder.length() - 1);
				System.out.println(builder.toString());
			}

		} finally {
			closeQuietry(stm);
			closeQuietry(con);
		}
	}

	private void closeQuietry(Statement stm) {
		if (stm != null) {
			try {
				stm.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void closeQuietry(Connection con) {
		if (con != null) {
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
