package Controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import Model.InfoAlert;
import Model.Login;
import Model.LoginDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.control.TextField;

public class FXMLTelaLoginController {

	@FXML
    private PasswordField txtPassword;
    @FXML
    private TextField txtUser;
    @FXML
    private Label labelLogin;
    @FXML
    private Button btnEntrar;
    @FXML
    private Button btnFechar;
    
	@FXML
	void fechar(ActionEvent event) {
		Stage stage = (Stage) btnFechar.getScene().getWindow();
		stage.close();
	}

	@FXML
	public void entrar() throws IOException, SQLException {
		Boolean state = false;
		ArrayList<Login> list = LoginDAO.listAll();
		
		for (Login l : list) {
			if (txtUser.getText().equals(l.getUsuario().toString()) && 
					txtPassword.getText().equals(l.getSenha().toString())) {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/FXMLTelaConfig.fxml"));
				Pane root = loader.load();

				
				Scene scene = new Scene(root);
				Stage stage = new Stage();

				stage.setResizable(false);
				stage.setTitle("BIBLIOTECA UNIVERSITÁRIA");
				stage.setScene(scene);
				stage.show();
				btnEntrar.getScene().getWindow().hide();
				
				state = true;
			}		
		}
		if (!state) {
			InfoAlert.errorAlert("Erro ao logar", "Usuário e/ou senha inválidos.");
		}		
	}
}