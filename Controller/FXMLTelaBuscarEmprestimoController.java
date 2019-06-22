package Controller;

import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class FXMLTelaBuscarEmprestimoController implements Initializable {

    @FXML
    private Button btnBuscarEmp;
    @FXML
    private TextField txtBuscarEmp;
    @FXML
    private Button btnDevolverLivro;
    @FXML
    private Button btnCancelar;
    @FXML
    private Button btnVoltar;
    @FXML
    private Label lblNomeLivro;
    @FXML
    private Label lblNomeAutor;
    @FXML
    private Label lblNomeAluno;
    @FXML
    private Label lblRaAluno;
    @FXML
    private Label lblDataRetirada;
    @FXML
    private Label lblDataDevolucao;
    
    @FXML
    private TableView<Livro> grdBuscarEmp;
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
    
    @FXML
    private void voltar() {
    	fechaStage();
    }
    
    @FXML
    private void buscar() throws SQLException {
    	if(txtBuscarEmp.getText().trim().isEmpty()) {
			populaTabela(null);
		} else {
			populaTabela(txtBuscarEmp.getText());
		}
    }

    @FXML
    private void cancelar() {
    	txtBuscarEmp.clear();
    	lblNomeLivro.setText("");
    	lblNomeAutor.setText("");
    	lblNomeAluno.setText("");
    	lblRaAluno.setText("");
    	lblDataRetirada.setText("");
    	lblDataDevolucao.setText("");
    }

    @FXML
    private void devolverLivro() throws NumberFormatException, SQLException {
    	if (lblNomeLivro.getText().trim().isEmpty()) {
    		InfoAlert.infoAlert("Erro", "Selecione um empréstimo para devolução.");
    	} else {
    		Alert alert = InfoAlert.confirmationAlert("Deseja baixar o empréstimo?", "Você tem certeza que deseja baixar este empréstimo?");
			Optional<ButtonType> result = alert.showAndWait();
			
			if (result.get() == ButtonType.OK) { 
				Reserva r = getDTO();
	    		if(ReservaDAO.update(r) && LivroDAO.update(r.getLivro())) {
					InfoAlert.infoAlert("Empréstimo baixado", "Empréstimo baixado com sucesso");				
					fechaStage();
				} else
					InfoAlert.errorAlert("Erro ao baixar empréstimo", "Não foi possível baixar o empréstimo");
			}   		
    	}
    }    
    
    @FXML
    private void tableSelect(MouseEvent event) throws SQLException {
    	exibeEmprestimo();
    }
    
    private void populaTabela(String filtro) throws SQLException {
		ObservableList<Livro> list = FXCollections.observableArrayList();
		
		grdBuscarEmp.getItems().removeAll(grdBuscarEmp.getItems());
		
		if	(filtro == null) {
			try {
				list = LivroDAO.listBySituation("Emprestado", null);
			} catch (SQLException e) {
				e.printStackTrace();
			} 
		} else {
			list = LivroDAO.listBySituation("Emprestado", txtBuscarEmp.getText().toString());
		}
				
		clmnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		clmnNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
		clmnAutor.setCellValueFactory(new PropertyValueFactory<>("autor"));
		clmnEdicao.setCellValueFactory(new PropertyValueFactory<>("nroEdicao"));
		
		grdBuscarEmp.setItems(list);
	}
    
    private void exibeEmprestimo() throws SQLException {
		Livro l = grdBuscarEmp.getSelectionModel().getSelectedItem();
		
		setDTO(l);
	}
    
    private void setDTO(Livro l) throws SQLException {
    	Reserva r = ReservaDAO.read(l.getId());
    	lblNomeLivro.setText(r.getLivro().getNome());
    	lblNomeAutor.setText(r.getLivro().getAutor());
    	lblNomeAluno.setText(r.getAluno().getNome());
    	lblRaAluno.setText(r.getAluno().getRa().toString());;
    	lblDataRetirada.setText(r.getDataRetirada().toString());
    	lblDataDevolucao.setText(r.getDataDevolucao().toString());
	}
    
    private Reserva getDTO() throws NumberFormatException, SQLException {   	
    	Livro l = grdBuscarEmp.getSelectionModel().getSelectedItem();
    	Reserva r = ReservaDAO.read(l.getId());
    	
    	l.setSituacao("Disponível");
    	
    	r.setLivro(l);
    	r.setTipo("Empréstimo");
    	r.setAtivo(false); // Controla qual reserva está em vigor
    	
    	return r;
    }
    
    private void fechaStage() {
    	Stage stage = (Stage) txtBuscarEmp.getScene().getWindow();
		stage.close();
    }
}
