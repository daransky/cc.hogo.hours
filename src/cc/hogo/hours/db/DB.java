package cc.hogo.hours.db;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.daro.common.ui.UIError;

public class DB {

	private Connection	connection;
	
	private static DB impl;
	
	public void close() {
		if (impl != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				UIError.showError("DB Fehler", e);
			}
		}

		impl = null;
	}

	public Field[]	getFields(Class<?> entity) { 
		Field[] fields = entity.getDeclaredFields();
		return fields;
	}
	
	public Connection test(String url, String user, String password) throws Exception {
		return DriverManager.getConnection(url, user, password);
	}

	public static DB open() throws Exception {
		if (impl != null)
			return impl;
		
		Properties properties = new Properties();
		File file = new File("./hours.ini");
		if( !file.exists() ) { 
			
			properties.put("db", "localhost/hogo");
			properties.put("user", "postgres");
			properties.put("password", "Passw0rd");
			properties.store(new FileWriter(file), null);
			
		}
		properties.load(new FileReader(file));
		
		String db = properties.getProperty("db", "localhost/hogo");
		String user = properties.getProperty("user", "postgres");
		String password = properties.getProperty("password", "Passw0rd");
		
		impl = new DB();
		impl.connection = impl.test("jdbc:postgresql://"+db, user, password);
		impl.connection.setAutoCommit(false);
		return impl;
	}
	
	public Connection getConnection() {
		return connection;
	}

	public static DB instance() { 
		return impl;
	}

}
