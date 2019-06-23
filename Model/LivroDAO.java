package Model;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class LivroDAO {

private static Connection connection;
	
	public LivroDAO() {
		connection = new ConnectionFactory().getConnection();
	}

	public Boolean create(Livro l) throws SQLException {
		
		Boolean state = false;
		Integer id = getId();
		
		String sql = "INSERT INTO Livro (id, nome, autor, publicacao, editora, nro_edicao, isbn, situacao) " +
				"VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
		
		if (id != null) {
			try {
				
				PreparedStatement stmt = connection.prepareStatement(sql);
				stmt.setInt(1, id);
				stmt.setString(2, l.getNome());
				stmt.setString(3, l.getAutor());
				
				Date publicacao = convertToDate(l.getPublicacao()); 
				
				stmt.setDate(4, new java.sql.Date(publicacao.getTime()));
				stmt.setString(5, l.getEditora());
				stmt.setInt(6, l.getNroEdicao());
				stmt.setString(7, l.getIsbn());
				stmt.setString(8, l.getSituacao());
				
				stmt.execute();
				stmt.close();
				
				state = true;
				
			} catch (SQLException e) {
				/* Não há um erro específico para Livro, portanto, teremos uma mensagem genérica para erro ao inserir no BD */
				InfoAlert.errorAlert("Erro.", "Erro ao cadastrar o livro. \nLog de erro: " + e);
			}		
		}
		
		return state;
	}
	
	public static Boolean update(Livro l) {
		
		Boolean state = false;
		
		String sql = "UPDATE Livro SET nome = ?, autor = ?, publicacao = ?, editora = ?, nro_edicao = ?, "
				+ "isbn = ?, situacao = ? WHERE id = ?";
		
		try {
			
			PreparedStatement stmt = connection.prepareStatement(sql);
			stmt.setString(1, l.getNome());
			stmt.setString(2, l.getAutor());
			
			Date publicacao = convertToDate(l.getPublicacao()); 
			
			stmt.setDate(3, publicacao);
			stmt.setString(4, l.getEditora());
			stmt.setInt(5, l.getNroEdicao());
			stmt.setString(6, l.getIsbn());
			stmt.setString(7, l.getSituacao());
			stmt.setInt(8, l.getId());
			
			stmt.execute();
			stmt.close();
			
			state = true;
			
		} catch (SQLException e) {
			InfoAlert.errorAlert("Erro.", "Erro atualizar o livro. \nLog de erro: " + e);;
		}
		return state;
	}
	
	public static Boolean delete(Livro l) throws SQLException {
		
		Boolean state = false;
		
		String sql = "DELETE FROM Livro WHERE id = ?";
		PreparedStatement stmt = connection.prepareStatement(sql);
		
		try {						
			stmt.setInt(1, l.getId());
			stmt.execute();
			stmt.close();
			state = true;
		} catch (SQLException e) {
			if (e.getMessage().toString().contains("FK_RESERVA_LIVRO")) {
				InfoAlert.errorAlert("Não é possível excluir o livro", "O livro está vinculado a uma reserva/empréstimo ou já foi emprestado.");
			} else
				InfoAlert.errorAlert("Erro.", "Erro excluir o livro. \nLog de erro: " + e);
		} 
		
		return state;
	}
	
	public static ObservableList<Livro> listAll() throws SQLException {
		
		ObservableList<Livro> list = FXCollections.observableArrayList(); 
		
		/* Foi necessário abrir a conexão neste método pois a IDE não estava reconhecendo o Path da mesma */
		connection = new ConnectionFactory().getConnection();
		PreparedStatement stmt = null;
		
		try {
						
			String sql = "SELECT * FROM Livro";
			stmt = connection.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next()) {
				Livro l = new Livro();
				
				l.setId(rs.getInt("id"));
				l.setNome(rs.getString("nome"));
				l.setAutor(rs.getString("autor"));				
				l.setPublicacao((rs.getDate("publicacao").toLocalDate()));
				l.setEditora(rs.getString("editora"));
				l.setNroEdicao(rs.getInt("nro_edicao"));
				l.setIsbn(rs.getString("isbn"));
				l.setSituacao(rs.getString("situacao"));
				
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
	
	public static ObservableList<Livro> listByName(String nome) throws SQLException {
		
		ObservableList<Livro> list = FXCollections.observableArrayList(); 
		
		PreparedStatement stmt = null;
		
		try {
						
			String sql = "SELECT * FROM Livro WHERE nome LIKE ?";
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, "%" + nome + "%");
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next()) {
				Livro l = new Livro();
				
				l.setId(rs.getInt("id"));
				l.setNome(rs.getString("nome"));
				l.setAutor(rs.getString("autor"));				
				l.setPublicacao((rs.getDate("publicacao").toLocalDate()));
				l.setEditora(rs.getString("editora"));
				l.setNroEdicao(rs.getInt("nro_edicao"));
				l.setIsbn(rs.getString("isbn"));
				l.setSituacao(rs.getString("situacao"));
				
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
	
	public static ObservableList<Livro> listBySituation(String situacao, String nome) throws SQLException {
		
		ObservableList<Livro> list = FXCollections.observableArrayList(); 
		
		/* Foi necessário abrir a conexão neste método pois a IDE não estava reconhecendo o Path da mesma */
		connection = new ConnectionFactory().getConnection();
		PreparedStatement stmt = null;
		
		try {
			
			if (nome == null) {
				String sql = "SELECT * FROM Livro WHERE situacao = ?";
				stmt = connection.prepareStatement(sql);
				stmt.setString(1, situacao);
			}				
			else {
				String sql = "SELECT * FROM Livro WHERE situacao = ? and nome LIKE ?";
				stmt = connection.prepareStatement(sql);
				stmt.setString(1, situacao);
				stmt.setString(2, "%" + nome + "%");				
			}
			
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next()) {
				Livro l = new Livro();
				
				l.setId(rs.getInt("id"));
				l.setNome(rs.getString("nome"));
				l.setAutor(rs.getString("autor"));				
				l.setPublicacao((rs.getDate("publicacao").toLocalDate()));
				l.setEditora(rs.getString("editora"));
				l.setNroEdicao(rs.getInt("nro_edicao"));
				l.setIsbn(rs.getString("isbn"));
				l.setSituacao(rs.getString("situacao"));
				
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
	
	private Integer getId() throws SQLException {
		PreparedStatement stmt = null;
		Integer id = null;

		try {
			
			String sql = "SELECT * FROM Livro WHERE id = (SELECT MAX(id) FROM Livro)";
			stmt = connection.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next()) {				
				id = rs.getInt("id");
			}			
		} catch (SQLException e) {
			InfoAlert.errorAlert("Erro.", "Erro ao retornar ID para cadastro de Livro. \nLog de erro: " + e);
		} finally {
			if (id == null)
				id = 0;
			stmt.close();
		}
		
		return (id+1);
	}
	
	private static Date convertToDate(LocalDate locDate) {
		return (locDate == null ? null : Date.valueOf(locDate));
	}
}
