package Controller;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

import Model.Aluno;
import Model.AlunoDAO;
import Model.InfoAlert;
import Model.Livro;
import Model.LivroDAO;
import Model.Reserva;
import Model.ReservaDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class FXMLTelaEmprestimoController implements Initializable {

    @FXML
    private Button btnBuscar;
    @FXML
    private TextField txtRaAluno;
    @FXML
    private DatePicker dateRetirada;
    @FXML
    private Button bntEfetuarEmp;
    @FXML
    private Button btnCancelar;
    @FXML
    private TextField txtBuscarLivroEmp;
    @FXML
    private DatePicker dateDevolução;
    @FXML
    private Label lblNomeAluno;
    
    @FXML
    private TableView<Livro> grdEmpLivro;
    @FXML
    private TableColumn<Livro, Integer> clmnId;
    @FXML
    private TableColumn<Livro, String> clmnNome;
    @FXML
    private TableColumn<Livro, String> clmnAutor;
    @FXML
    private TableColumn<Livro, Integer> clmnEdicao;
    
    @Override
   	public void initialize(URL arg0, ResourceBundle arg1) {       	
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
    
    // Cancelar
    @FXML
    private void cancelar() throws SQLException {
    	txtRaAluno.clear();
    	lblNomeAluno.setText("");
    	txtBuscarLivroEmp.clear();
    	dateRetirada.getEditor().clear();
    	dateDevolução.getEditor().clear();
    	
    	populaTabela(null);
    }
    
    // Buscar    
    @FXML
    private void buscar() throws SQLException {
    	if(txtBuscarLivroEmp.getText().trim().isEmpty()) {
			populaTabela(null);
		} else {
			populaTabela(txtBuscarLivroEmp.getText());
		}
    }

    // Enter no TextField do RA
    @FXML
    private void raInserido() throws NumberFormatException, SQLException {
    	Aluno a = AlunoDAO.readWithRa(Integer.parseInt(txtRaAluno.getText()));
    	
    	if (a != null) {
    		if (a.getStatus().toString().equals("Bloqueado")) {
    			InfoAlert.infoAlert("Aluno bloqueado", "O aluno inserido está bloqueado.");
    			txtRaAluno.clear();
    			lblNomeAluno.setText("");
    		} else 
    			lblNomeAluno.setText(a.getNome().toString());
    	} else {
    		InfoAlert.infoAlert("RA inválido", "O RA inserido não é válido.");
    		txtRaAluno.clear();
    		lblNomeAluno.setText("");
    	}
    }

    // Efetuar Empréstimo
    @FXML
    private void salvarEmp() throws NumberFormatException, SQLException {
    	Boolean estaPreenchido = estaPreenchido();
    	Livro l = grdEmpLivro.getSelectionModel().getSelectedItem();
    	
		if(estaPreenchido) {
			if(periodoValido()) {
				if (l != null) {
					Alert alert = InfoAlert.confirmationAlert("Deseja cadastrar o empréstimo?", "Você tem certeza que deseja cadastrar este empréstimo?");
					Optional<ButtonType> result = alert.showAndWait();
					
					if (result.get() == ButtonType.OK) { 
						Reserva r = getDTO();
						if(ReservaDAO.create(r) && LivroDAO.update(r.getLivro())) {
							InfoAlert.infoAlert("Empréstimo cadastrado", "Empréstimo cadastrado com sucesso");				
							fechaStage();
						} else
							InfoAlert.errorAlert("Erro ao cadastrar", "Não foi possível cadastrar o empréstimo");
					}				
				} else
					InfoAlert.errorAlert("Erro ao cadastrar", "Selecione um livro para efetuar o empréstimo.");
			} else 
				InfoAlert.errorAlert("Erro ao cadastrar", "Selecione um período válido.");				
		} else
			InfoAlert.errorAlert("Erro ao cadastrar", "Preencha todos os campos");
    }
    
    private void populaTabela(String filtro) throws SQLException {
		ObservableList<Livro> list = FXCollections.observableArrayList();
		
		grdEmpLivro.getItems().removeAll(grdEmpLivro.getItems());
		
		if	(filtro == null) {
			try {
				list = LivroDAO.listBySituation("Disponível", null);
			} catch (SQLException e) {
				e.printStackTrace();
			} 
		} else {
			list = LivroDAO.listBySituation("Disponível", txtBuscarLivroEmp.getText().toString());
		}
				
		clmnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		clmnNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
		clmnAutor.setCellValueFactory(new PropertyValueFactory<>("autor"));
		clmnEdicao.setCellValueFactory(new PropertyValueFactory<>("nroEdicao"));
		
		grdEmpLivro.setItems(list);
	}
    
    private Boolean estaPreenchido() {
		if (!txtRaAluno.getText().isEmpty() &&
				dateDevolução.getValue() != null &&
				dateRetirada.getValue() != null)
			return true;
		
		return false;
	}
    
    private Boolean periodoValido() {
    	LocalDate today = LocalDate.now();
    	LocalDate dataRetirada = dateRetirada.getValue();
    	if (dataRetirada.isAfter(dateDevolução.getValue()) || dataRetirada.isEqual(dateDevolução.getValue()) || 
    			(dataRetirada.isEqual(today) || dataRetirada.isBefore(today)))
    		return false;
    	return true;
    }
    
    private Reserva getDTO() throws NumberFormatException, SQLException {
    	Reserva r = new Reserva();
    	
    	Aluno a = AlunoDAO.readWithRa(Integer.parseInt(txtRaAluno.getText()));
    	Livro l = grdEmpLivro.getSelectionModel().getSelectedItem();
    	
    	l.setSituacao("Emprestado");
    	
    	r.setAluno(a);
    	r.setLivro(l);
    	r.setDataDevolucao(dateDevolução.getValue());
    	r.setDataRetirada(dateRetirada.getValue());
    	r.setTipo("Empréstimo"); // Neste controller estamos cadastrando empréstimos
    	r.setAtivo(true); // Controla qual empréstimo está em vigor
    	
    	return r;
    }

    private void fechaStage() {
    	Stage stage = (Stage) txtBuscarLivroEmp.getScene().getWindow();
		stage.close();
    }
}
