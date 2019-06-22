package Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class LoginDAO {

	private static Connection connection;
	
	public LoginDAO() {
		connection = new ConnectionFactory().getConnection();
	}
	
	public static ArrayList<Login> listAll() throws SQLException {
		
		ArrayList<Login> list = new ArrayList<Login>(); 
		
		/* Foi necessário abrir a conexão neste método pois a IDE não estava reconhecendo o Path da mesma */
		connection = new ConnectionFactory().getConnection();
		PreparedStatement stmt = null;
		
		try {
						
			String sql = "SELECT * FROM Login";
			stmt = connection.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next()) {
				Login l = new Login();
				
				l.setId(rs.getInt("id"));
				l.setUsuario(rs.getString("usuario"));
				l.setSenha(rs.getString("senha"));
				
				list.add(l);
			}
			
			rs.close();
			
			return list;
		} catch (SQLException e) {
			InfoAlert.errorAlert("Erro.", "Erro ao retornar a lista. \nLog de erro: " + e);
		} finally {
			stmt.close();
		}
		
		return null;
	}
}
