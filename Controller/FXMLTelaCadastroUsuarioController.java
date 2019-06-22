package Controller;

import java.io.IOException;
import java.sql.SQLException;

import Model.Aluno;
import Model.AlunoDAO;
import Model.InfoAlert;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class FXMLTelaCadastroUsuarioController {

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
	private TextField txtBairro;
	@FXML
	private TextField txtRa;
	@FXML
	private Button btnSalvar;
	@FXML
	private Button btnCancelar;
	@FXML
	private TextField txtCidade;
	@FXML
	private ComboBox<String> cbEstado;
	
	private AlunoDAO dao = new AlunoDAO();
	
	@FXML
	public void initialize() {
		ObservableList<String> list = listarEstados();
		cbEstado.getItems().removeAll(cbEstado.getItems());
		cbEstado.setItems(list);
	}

	// Cancelar
	@FXML
	public void cancelar() throws IOException {
		fechaStage();
	}

	// Salvar
	@FXML
	public void salvar() throws IOException, SQLException {
		Boolean estaPreenchido = estaPreenchido();
		
		if(estaPreenchido) {
			if (!isInteger(txtRa.getText()) || !isInteger(txtNumero.getText())) {
				InfoAlert.infoAlert("Não é possível cadastrar o aluno", "O RA/número do endereço informado deve ser um número inteiro");
			} else {
				Aluno a = getDTO();
				
				if(dao.create(a)) {
					InfoAlert.infoAlert("Aluno cadastrado", "Aluno cadastrado com sucesso");				
					fechaStage();
				} else
					InfoAlert.errorAlert("Erro ao cadastrar", "Não foi possível cadastrar o aluno");
			}		
		} else
			InfoAlert.errorAlert("Erro ao cadastrar", "Preencha todos os campos");
	}

	private Aluno getDTO() {
		Aluno a = new Aluno();

		a.setNome(txtNome.getText());
		a.setRg(txtRg.getText());
		a.setCpf(txtCpf.getText());
		a.setEmail(txtEmail.getText());
		a.setTelefone(txtTelefone.getText());
		a.setStatus("Ativo"); // Sempre que o aluno for criado, automaticamente estará ativo
		a.setLogradouro(txtRua.getText());
		a.setNumero(Integer.parseInt(txtNumero.getText().toString()));
		a.setCep(txtCep.getText());
		a.setBairro(txtBairro.getText());
		a.setCidade(txtCidade.getText());
		a.setRa(Integer.parseInt(txtRa.getText()));
		a.setEstado(cbEstado.getValue().toString());

		return a;
	}
	
	private Boolean estaPreenchido() {
		if (!txtNome.getText().trim().isEmpty() &&
				!txtRg.getText().trim().isEmpty() &&
				!txtCpf.getText().trim().isEmpty() &&
				!txtEmail.getText().trim().isEmpty() &&
				!txtCpf.getText().trim().isEmpty() &&
				!txtEmail.getText().trim().isEmpty() &&
				!txtTelefone.getText().trim().isEmpty() &&
				!txtRua.getText().trim().isEmpty() &&
				!txtNumero.getText().trim().isEmpty() &&
				!txtCep.getText().trim().isEmpty() &&
				!txtBairro.getText().trim().isEmpty() &&
				!txtCidade.getText().trim().isEmpty() &&
				!txtRua.getText().trim().isEmpty() &&
				!cbEstado.getSelectionModel().isEmpty())
			return true;
		
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
	
	private ObservableList<String> listarEstados() {
		ObservableList<String> list = 
				FXCollections.observableArrayList("AC", "AL", "AP", "AM", "BA", "CE", "DF",
													"ES", "GO", "MA", "MT", "MS", "MG", "PA",
													"PB", "PR", "PE", "PI", "RJ", "RN", "RS",
													"RO", "RR", "SC", "SP", "SE", "TO");
		return list;
	}
	
	private void fechaStage() {	
		Stage stage = (Stage) txtNome.getScene().getWindow();
		stage.close();
	}
}
