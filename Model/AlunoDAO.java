package Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class AlunoDAO {
	
	private static Connection connection;
	
	public AlunoDAO() {
		connection = new ConnectionFactory().getConnection();
	}

	public Boolean create(Aluno a) throws SQLException {
		
		Boolean state = false;
		Integer id = getId();
		
		String sql = "INSERT INTO Aluno (id, nome, rg, cpf, email, telefone, status, logradouro, numero, cep, bairro, cidade, estado, ra) " +
				"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
		
		if (id != null) {
			try {
				
				PreparedStatement stmt = connection.prepareStatement(sql);
				stmt.setInt(1, id);
				stmt.setString(2, a.getNome());
				stmt.setString(3, a.getRg());
				stmt.setString(4, a.getCpf());
				stmt.setString(5, a.getEmail());
				stmt.setString(6, a.getTelefone());
				stmt.setString(7, a.getStatus());
				stmt.setString(8, a.getLogradouro());
				stmt.setInt(9, a.getNumero());
				stmt.setString(10, a.getCep());
				stmt.setString(11, a.getBairro());
				stmt.setString(12, a.getCidade());
				stmt.setString(13, a.getEstado());
				stmt.setInt(14, a.getRa());
				
				stmt.execute();
				stmt.close();
				
				state = true;
				
			} catch (SQLException e) {
				if (e.getMessage().toString().contains("UN_ALUNO_CPF")) 
					InfoAlert.errorAlert("Erro ao cadastrar", "O CPF informado já está cadastrado.");
				else if (e.getMessage().toString().contains("UN_ALUNO_RA"))
					InfoAlert.errorAlert("Erro ao cadastrar", "O RA informado já está cadastrado.");
				else {
					InfoAlert.errorAlert("Erro.", "Erro ao cadastrar o aluno. \nLog de erro: " + e);
				}
			}		
		}
		
		return state;
	}
	
	@SuppressWarnings("null")
	public static Aluno readWithRa (Integer ra) throws SQLException {
		
		/* Foi necessário abrir a conexão neste método pois a IDE não estava reconhecendo o Path da mesma */
		connection = new ConnectionFactory().getConnection();
		
		PreparedStatement stmt = null;
		Aluno aluno = null;

		try {
			
			String sql = "SELECT * FROM Aluno WHERE ra = ?";
			stmt = connection.prepareStatement(sql);
			stmt.setInt(1, ra);
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next()) {
				Aluno a = new Aluno();
				
				a.setId(rs.getInt("id"));
				a.setNome(rs.getString("nome"));
				a.setRg(rs.getString("rg"));
				a.setCpf(rs.getString("cpf"));
				a.setEmail(rs.getString("email"));
				a.setTelefone(rs.getString("telefone"));
				a.setStatus(rs.getString("status"));
				a.setLogradouro(rs.getString("logradouro"));
				a.setNumero(rs.getInt("numero"));
				a.setCep(rs.getString("cep"));
				a.setBairro(rs.getString("bairro"));
				a.setCidade(rs.getString("cidade"));
				a.setEstado(rs.getString("estado"));
				a.setRa(rs.getInt("ra"));
				
				aluno = a;
			}			
		} catch (SQLException e) {
			InfoAlert.errorAlert("Erro.", "Erro ao consultar aluno. \nLog de erro: " + e);
		} finally {
			stmt.close();
		}
		
		return aluno;
	}
	
	public static Boolean update(Aluno a) {
		
		Boolean state = false;
		
		String sql = "UPDATE Aluno SET nome = ?, rg = ?, cpf = ?, email = ?, telefone = ?, "
				+ "status = ?, logradouro = ?, numero = ?, cep = ?, bairro = ?, cidade = ?, estado = ?, ra = ? " +
				"WHERE id = ?";
		
		try {
			
			PreparedStatement stmt = connection.prepareStatement(sql);
			stmt.setString(1, a.getNome());
			stmt.setString(2, a.getRg());
			stmt.setString(3, a.getCpf());
			stmt.setString(4, a.getEmail());
			stmt.setString(5, a.getTelefone());
			stmt.setString(6, a.getStatus());
			stmt.setString(7, a.getLogradouro());
			stmt.setInt(8, a.getNumero());
			stmt.setString(9, a.getCep());
			stmt.setString(10, a.getBairro());
			stmt.setString(11, a.getCidade());
			stmt.setString(12, a.getEstado());
			stmt.setInt(13, a.getRa());
			stmt.setInt(14, a.getId());
			
			stmt.execute();
			stmt.close();
			
			state = true;
			
		} catch (SQLException e) {
			if (e.getMessage().toString().contains("UN_ALUNO_CPF")) 
				InfoAlert.errorAlert("Erro ao atualizar", "O CPF informado já está cadastrado.");
			else if (e.getMessage().toString().contains("UN_ALUNO_RA"))
				InfoAlert.errorAlert("Erro ao atualizar", "O RA informado já está cadastrado.");
			else {
				InfoAlert.errorAlert("Erro.", "Erro ao atualizar o aluno. \nLog de erro: " + e);
			}
		}
		return state;
	}
	
	public static Boolean delete(Aluno a) throws SQLException {
		
		Boolean state = false;
		
		String sql = "DELETE FROM Aluno WHERE id = ?";
		PreparedStatement stmt = connection.prepareStatement(sql);
		
		try {						
			stmt.setInt(1, a.getId());
			stmt.execute();
			stmt.close();
			state = true;
		} catch (SQLException e) {
			if (e.getMessage().toString().contains("FK_RESERVA_ALUNO")) {
				InfoAlert.errorAlert("Não é possível excluir o aluno", "O aluno está vinculado a uma reserva/empréstimo.");
			} else
				InfoAlert.errorAlert("Erro.", "Erro excluir o aluno. \nLog de erro: " + e);
		} 
		
		return state;
	}
	
	public static ObservableList<Aluno> listAll() throws SQLException {
		
		ObservableList<Aluno> list = FXCollections.observableArrayList(); 
		
		/* Foi necessário abrir a conexão neste método pois a IDE não estava reconhecendo o Path da mesma */
		connection = new ConnectionFactory().getConnection();
		PreparedStatement stmt = null;
		
		try {
						
			String sql = "SELECT * FROM Aluno";
			stmt = connection.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next()) {
				Aluno a = new Aluno();
				
				a.setId(rs.getInt("id"));
				a.setNome(rs.getString("nome"));
				a.setRg(rs.getString("rg"));
				a.setCpf(rs.getString("cpf"));
				a.setEmail(rs.getString("email"));
				a.setTelefone(rs.getString("telefone"));
				a.setStatus(rs.getString("status"));
				a.setLogradouro(rs.getString("logradouro"));
				a.setNumero(rs.getInt("numero"));
				a.setCep(rs.getString("cep"));
				a.setBairro(rs.getString("bairro"));
				a.setCidade(rs.getString("cidade"));
				a.setEstado(rs.getString("estado"));
				a.setRa(rs.getInt("ra"));
				
				list.add(a);
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
	
	public static ObservableList<Aluno> listByName(String nome) throws SQLException {
		
		ObservableList<Aluno> list = FXCollections.observableArrayList(); 
		
		PreparedStatement stmt = null;
		
		try {
						
			String sql = "SELECT * FROM Aluno WHERE nome LIKE ?";
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, "%" + nome + "%");
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next()) {
				Aluno a = new Aluno();
				
				a.setId(rs.getInt("id"));
				a.setNome(rs.getString("nome"));
				a.setRg(rs.getString("rg"));
				a.setCpf(rs.getString("cpf"));
				a.setEmail(rs.getString("email"));
				a.setTelefone(rs.getString("telefone"));
				a.setStatus(rs.getString("status"));
				a.setLogradouro(rs.getString("logradouro"));
				a.setNumero(rs.getInt("numero"));
				a.setCep(rs.getString("cep"));
				a.setBairro(rs.getString("bairro"));
				a.setCidade(rs.getString("cidade"));
				a.setEstado(rs.getString("estado"));
				a.setRa(rs.getInt("ra"));
				
				list.add(a);
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
	
	public static ObservableList<Aluno> listBlocked() throws SQLException {
		
		ObservableList<Aluno> list = FXCollections.observableArrayList(); 
		
		PreparedStatement stmt = null;
		
		try {
						
			String sql = "SELECT * FROM Aluno WHERE status = 'Bloqueado'";
			stmt = connection.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next()) {
				Aluno a = new Aluno();
				
				a.setId(rs.getInt("id"));
				a.setNome(rs.getString("nome"));
				a.setRg(rs.getString("rg"));
				a.setCpf(rs.getString("cpf"));
				a.setEmail(rs.getString("email"));
				a.setTelefone(rs.getString("telefone"));
				a.setStatus(rs.getString("status"));
				a.setLogradouro(rs.getString("logradouro"));
				a.setNumero(rs.getInt("numero"));
				a.setCep(rs.getString("cep"));
				a.setBairro(rs.getString("bairro"));
				a.setCidade(rs.getString("cidade"));
				a.setEstado(rs.getString("estado"));
				a.setRa(rs.getInt("ra"));
				
				list.add(a);
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
	
	private Integer getId() throws SQLException {
		PreparedStatement stmt = null;
		Integer id = null;

		try {
			
			String sql = "SELECT * FROM Aluno WHERE id = (SELECT MAX(id) FROM Aluno)";
			stmt = connection.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next()) {				
				id = rs.getInt("id");
			}			
		} catch (SQLException e) {
			InfoAlert.errorAlert("Erro.", "Erro ao retornar ID para cadastro de Aluno. \nLog de erro: " + e);
		} finally {
			if (id == null)
				id = 0;
			stmt.close();
		}
		
		return (id+1);
	}
}
