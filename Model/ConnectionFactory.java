package Model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
	
	public Connection getConnection() {
		try {
			return DriverManager.getConnection("jdbc:hsqldb:hsql://127.0.0.1:9137/myServerDB", "sa", "");
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

}