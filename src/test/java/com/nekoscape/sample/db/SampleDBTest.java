package com.nekoscape.sample.db;

import static org.junit.Assert.fail;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.junit.BeforeClass;
import org.junit.Test;

import com.nekosacpe.sample.db.SampleDB;


public class SampleDBTest {
	
	private static IDatabaseConnection conn = null;

	@BeforeClass
	public static void before() {
		System.out.println("@Before");
		System.setProperty(SampleDB.PROPERTY_DB_DRIVER, "org.h2.Driver");
		System.setProperty(SampleDB.PROPERTY_JDBC_URI, "jdbc:h2:~/test");
		
		createTestTable();
	}

	@Test
	public void testSQL_SELECT() {
		SampleDB sampleDB = SampleDB.getInstance();

		try {
			IDataSet dataSet = new FlatXmlDataSet(new File("src/test/resources/xml/dataset_test.xml"));
			DatabaseOperation.CLEAN_INSERT.execute(conn, dataSet);

			sampleDB.executeQuery();

			// may be assert result

		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	private static void createTestTable(){
		try {
			Class.forName(System.getProperty(SampleDB.PROPERTY_DB_DRIVER));
			
			Connection con = DriverManager.getConnection(System.getProperty(SampleDB.PROPERTY_JDBC_URI));
			conn = new DatabaseConnection(con);
			Statement stm = con.createStatement();
			
			// あとでファイルから読み込む
			stm.execute("create table IF NOT EXISTS TEST (ID CHAR(10), NAME CHAR(10));");
			
			con.commit();
			
			
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

}
