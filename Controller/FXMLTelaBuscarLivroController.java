package Controller;

import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

import Model.InfoAlert;
import Model.Livro;
import Model.LivroDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class FXMLTelaBuscarLivroController implements Initializable  {

    @FXML
    private TextField txtBuscarLivro;
    @FXML
    private Button bntExcluir;
    @FXML
    private Button bntBuscar;
    @FXML
    private Button bntAtulaizar;
    @FXML
    private Button bntVoltar;
    @FXML
    private TextField txtNomeLivro;
    @FXML
    private TextField txtAutor;
    @FXML
    private TextField txtEditora;
    @FXML
    private TextField txtISBN;
    @FXML
    private DatePicker datePublicacao;
    @FXML
    private TextField txtEdicao;
    @FXML
    private ComboBox<String> cbStatus;
    
    @FXML
    private TableView<Livro> grdBuscarLivro;
    @FXML
    private TableColumn<Livro, Integer> clmnId;
    @FXML
    private TableColumn<Livro, String> clmnNome;
    @FXML
    private TableColumn<Livro, Integer> clmnEdicao;

    @Override
	public void initialize(URL arg0, ResourceBundle arg1) {
    	ObservableList<String> listStatus = listarStatus();
		cbStatus.getItems().removeAll(cbStatus.getItems());
		cbStatus.setItems(listStatus);
    	
    	try {
			populaTabela(null);
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
    
    // Voltar
    @FXML
    private void voltar() {
    	fechaStage();
    }
    
    // Buscar 
    @FXML
    private void buscar() throws SQLException {
    	if(txtBuscarLivro.getText().trim().isEmpty()) {
			populaTabela(null);
		} else {
			populaTabela(txtBuscarLivro.getText());
		}
    }
    
    // Atualizar
    @FXML
    private void atualizar() throws SQLException {
    	String situacao = null;
    	if (grdBuscarLivro.getSelectionModel().getSelectedItem() != null) {
    		situacao = grdBuscarLivro.getSelectionModel().getSelectedItem().getSituacao();
    	}
    	
    	if(txtNomeLivro.getText().trim().isEmpty()) {
			InfoAlert.infoAlert("Alerta", "Selecione um livro para atualizar.");
		} else if (statusBloqueante() && !situacao.equals(cbStatus.getValue().toString())) {
			InfoAlert.infoAlert("Não é possível atualizar o livro.", "O livro está emprestado/reservado. "
					+ "\nNão é possível atualizar o status do mesmo."
					+ "\nDê baixa no empréstimo/reserva para proceder.");
		} else if (cbStatus.getValue().toString().equals("Emprestado") || cbStatus.getValue().toString().equals("Reservado")) {
			InfoAlert.infoAlert("Não é possível atualizar o livro.", "O livro não pode ser atualizado para emprestado/reservado");
		} else if (!isInteger(txtEdicao.getText())) {
			InfoAlert.infoAlert("Não é possível atualizar o livro", "A edição informada deve ser um número inteiro");
		} else {
			Alert alert = InfoAlert.confirmationAlert("Deseja atualizar o livro?", "Você tem certeza que deseja atualizar este livro?");
			Optional<ButtonType> result = alert.showAndWait();
			
			if (result.get() == ButtonType.OK) {
				Livro l = getDTO();
				if(LivroDAO.update(l)) {
					buscar();
					InfoAlert.infoAlert("Livro atualizado", "Livro atualizado com sucesso.");
				}
			}
		}
    }   

    // Excluir
    @FXML
    private void excluir() throws SQLException {
    	if(txtNomeLivro.getText().trim().isEmpty()) {
			InfoAlert.infoAlert("Alerta", "Selecione um livro para excluir.");
		} else if (statusBloqueante()) {
			InfoAlert.infoAlert("Não é possível excluir o livro.", "O livro está emprestado/reservado. "
					+ "\nDê baixa no empréstimo/reserva para proceder.");
		} else {
			Alert alert = InfoAlert.confirmationAlert("Deseja excluir o livro?", "Você tem certeza que deseja excluir este livro?");
			Optional<ButtonType> result = alert.showAndWait();
			
			if (result.get() == ButtonType.OK) {
				if(LivroDAO.delete(grdBuscarLivro.getSelectionModel().getSelectedItem())) {
					populaTabela(null);
					limparTextBox();
					InfoAlert.infoAlert("Livro excluído", "Livro excluído com sucesso.");
				}
			}
		}	
    } 
    
    // Click na Table
    @FXML
    private void tableSelect(MouseEvent event) {
    	exibeLivro();
    }
    
    private void populaTabela(String filtro) throws SQLException {
		ObservableList<Livro> list = FXCollections.observableArrayList();
		
		grdBuscarLivro.getItems().removeAll(grdBuscarLivro.getItems());
		
		if	(filtro == null) {
			try {
				list = LivroDAO.listAll();
			} catch (SQLException e) {
				e.printStackTrace();
			} 
		} else {
			list = LivroDAO.listByName(txtBuscarLivro.getText().toString());
		}
				
		clmnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		clmnNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
		clmnEdicao.setCellValueFactory(new PropertyValueFactory<>("nroEdicao"));
		
		grdBuscarLivro.setItems(list);
	}
    
    private void exibeLivro() {
		Livro l = grdBuscarLivro.getSelectionModel().getSelectedItem();
		
		setDTO(l);
	}
	
	private void setDTO(Livro l) {
		txtNomeLivro.setText(l.getNome());
		txtAutor.setText(l.getAutor());
		txtEditora.setText(l.getEditora());
		txtISBN.setText(l.getIsbn());
		txtEdicao.setText(l.getNroEdicao().toString());
		datePublicacao.setValue(l.getPublicacao());
		cbStatus.setValue(l.getSituacao());
	}
	
	private Livro getDTO() {
		Livro l = new Livro();
		
		l.setId((grdBuscarLivro.getSelectionModel().getSelectedItem()).getId());
		l.setNome(txtNomeLivro.getText());
		l.setAutor(txtAutor.getText());
		l.setEditora(txtEditora.getText());
		l.setIsbn(txtISBN.getText());
		l.setNroEdicao(Integer.parseInt(txtEdicao.getText()));
		l.setPublicacao(datePublicacao.getValue());
		l.setSituacao(cbStatus.getValue().toString());	
		
		return l;
	}
	
	private void limparTextBox() {
		txtNomeLivro.clear();
		txtAutor.clear();
		txtEditora.clear();
		txtISBN.clear();
		txtEdicao.clear();
		datePublicacao.getEditor().clear();
		cbStatus.getSelectionModel().clearSelection();
	}
	
	private Boolean statusBloqueante() {
		String situacao = grdBuscarLivro.getSelectionModel().getSelectedItem().getSituacao();
		
		if (situacao.equals("Emprestado") || situacao.equals("Reservado")) {
			return true;
		}		
		return false;
	}
	
	private Boolean isInteger(String s) {
		try {
			Integer.parseInt(s);
			return true;
		} catch (NumberFormatException ex) {
			return false;
		}
	}
	
	private ObservableList<String> listarStatus() {
		ObservableList<String> list = 
				FXCollections.observableArrayList("Indisponível", "Disponível", "Emprestado", "Reservado");
		return list;
	}
    
    private void fechaStage() {
    	Stage stage = (Stage) txtBuscarLivro.getScene().getWindow();
		stage.close();
    }
}