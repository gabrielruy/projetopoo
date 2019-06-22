package Controller;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

import Model.Aluno;
import Model.AlunoDAO;
import Model.InfoAlert;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class FXMLTelaBuscarUsuarioController implements Initializable {
    
	@FXML
    private TextField txtBuscarAluno;
    @FXML
    private Button btnExcluir;
    @FXML
    private Button btnBuscar;
    @FXML
    private Button btnAtualizar;
    @FXML
    private Button btnVoltar;
	@FXML
	private Button bntEditar;
	@FXML
    private TextField txtNome;
    @FXML
    private TextField txtRg;
    @FXML
    private TextField txtCpf;
    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtTelefone;
    @FXML
    private TextField txtRua;
    @FXML
    private TextField txtNumero;
    @FXML
    private TextField txtCep;
    @FXML
    private TextField txtCidade;
    @FXML
    private TextField txtBairro;
    @FXML
    private TextField txtRa;
    @FXML
    private ComboBox<String> cbEstado;
    @FXML
    private ComboBox<String> cbStatus;

	@FXML
	private TableView<Aluno> grdBuscarAluno;   
	@FXML
	private TableColumn<Aluno, Integer> clmnId;
	@FXML
	private TableColumn<Aluno, String> clmnNome;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		ObservableList<String> listEstados = listarEstados();
		cbEstado.getItems().removeAll(cbEstado.getItems());
		cbEstado.setItems(listEstados);
		
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
	private void voltar() throws IOException {
		fechaStage();
	}

	// Buscar
	@FXML
	public void buscar() throws IOException, SQLException {
		if(txtBuscarAluno.getText().trim().isEmpty()) {
			populaTabela(null);
		} else {
			populaTabela(txtBuscarAluno.getText());
		}
	}

	// Atualizar
	@FXML
	public void atualizar() throws IOException, SQLException {
		if(txtNome.getText().trim().isEmpty()) {
			InfoAlert.infoAlert("Alerta", "Selecione um aluno para atualizar.");
		} else if (!isInteger(txtRa.getText()) || !isInteger(txtNumero.getText())) {
			InfoAlert.infoAlert("Não é possível atualizar o aluno", "O RA/número do endereço informado deve ser um número inteiro");
		} else {
			Alert alert = InfoAlert.confirmationAlert("Deseja atualizar o aluno?", "Você tem certeza que deseja atualizar este aluno?");
			Optional<ButtonType> result = alert.showAndWait();
			
			if (result.get() == ButtonType.OK) {
				Aluno a = getDTO();
				if(AlunoDAO.update(a)) {
					buscar();
					InfoAlert.infoAlert("Aluno atualizado", "Aluno atualizado com sucesso.");
				}
			}
		}
	}

	// Excluir
	@FXML
	public void excluir() throws IOException, SQLException {
		if(txtNome.getText().trim().isEmpty()) {
			InfoAlert.infoAlert("Alerta", "Selecione um aluno para excluir.");
		} else {
			Alert alert = InfoAlert.confirmationAlert("Deseja excluir o aluno?", "Você tem certeza que deseja excluir este aluno?");
			Optional<ButtonType> result = alert.showAndWait();
			
			if (result.get() == ButtonType.OK) {
				if(AlunoDAO.delete(grdBuscarAluno.getSelectionModel().getSelectedItem())) {
					populaTabela(null);
					limparTextBox();
					InfoAlert.infoAlert("Aluno excluído", "Aluno excluído com sucesso.");
				}
			}
		}	
	}
	
	// Click na Table	
	@FXML
	public void tableSelect(MouseEvent event) {
		exibeAluno();
    }

	private void populaTabela(String filtro) throws SQLException {
		ObservableList<Aluno> list = FXCollections.observableArrayList();
		
		grdBuscarAluno.getItems().removeAll(grdBuscarAluno.getItems());
		
		if	(filtro == null) {
			try {
				list = AlunoDAO.listAll();
			} catch (SQLException e) {
				e.printStackTrace();
			} 
		} else {
			list = AlunoDAO.listByName(txtBuscarAluno.getText().toString());
		}
				
		clmnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		clmnNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
		
		grdBuscarAluno.setItems(list);
	}
	
	private void exibeAluno() {
		Aluno a = grdBuscarAluno.getSelectionModel().getSelectedItem();
		
		setDTO(a);
	}
	
	private void setDTO(Aluno a) {
		txtNome.setText(a.getNome());
		txtRg.setText(a.getRg());
		txtCpf.setText(a.getCpf());
		txtEmail.setText(a.getEmail());
		txtTelefone.setText(a.getTelefone());
		txtRua.setText(a.getLogradouro());
		txtNumero.setText(a.getNumero().toString());
		txtCep.setText(a.getCep());
		txtCidade.setText(a.getCidade());
		txtBairro.setText(a.getBairro());
		txtRa.setText(a.getRa().toString());
		cbEstado.setValue(a.getEstado());
		cbStatus.setValue(a.getStatus());
	}
	
	private Aluno getDTO() {
		Aluno a = new Aluno();
		
		a.setId((grdBuscarAluno.getSelectionModel().getSelectedItem()).getId());
		a.setNome(txtNome.getText());
		a.setRg(txtRg.getText());
		a.setCpf(txtCpf.getText());
		a.setEmail(txtEmail.getText());
		a.setTelefone(txtTelefone.getText());
		a.setStatus(cbStatus.getValue().toString());
		a.setLogradouro(txtRua.getText());
		a.setNumero(Integer.parseInt(txtNumero.getText().toString()));
		a.setCep(txtCep.getText());
		a.setBairro(txtBairro.getText());
		a.setRa(Integer.parseInt(txtRa.getText()));
		a.setCidade(txtCidade.getText());
		a.setEstado(cbEstado.getValue().toString());		
		
		return a;
	}
	
	private void limparTextBox() {
		txtNome.clear();
		txtRg.clear();
		txtCpf.clear();
		txtEmail.clear();
		txtTelefone.clear();
		txtRua.clear();
		txtNumero.clear();
		txtCep.clear();
		txtCidade.clear();
		txtBairro.clear();
		txtRa.clear();
		cbEstado.getSelectionModel().clearSelection();
		cbStatus.getSelectionModel().clearSelection();
	}
	
	private Boolean isInteger(String s) {
		try {
			Integer.parseInt(s);
			return true;
		} catch (NumberFormatException ex) {
			return false;
		}
	}
	
	private ObservableList<String> listarEstados() {
		ObservableList<String> list = 
				FXCollections.observableArrayList("AC", "AL", "AP", "AM", "BA", "CE", "DF",
													"ES", "GO", "MA", "MT", "MS", "MG", "PA",
													"PB", "PR", "PE", "PI", "RJ", "RN", "RS",
													"RO", "RR", "SC", "SP", "SE", "TO");
		return list;
	}
	
	private ObservableList<String> listarStatus() {
		ObservableList<String> list = 
				FXCollections.observableArrayList("Ativo", "Inativo", "Bloqueado");
		return list;
	}

	private void fechaStage() {
		Stage stage = (Stage) txtBuscarAluno.getScene().getWindow();
		stage.close();
	}
}
