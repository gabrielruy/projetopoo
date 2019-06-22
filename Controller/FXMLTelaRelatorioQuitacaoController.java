package Controller;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import Model.Aluno;
import Model.AlunoDAO;
import Model.InfoAlert;
import Model.ReservaDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class FXMLTelaRelatorioQuitacaoController {

	@FXML
	private TextField txtRa;
	@FXML
	private Label lblRa;
	@FXML
	private Button btnGerar;

	@FXML
	private void gerar() throws NumberFormatException, SQLException {
		Aluno a = AlunoDAO.readWithRa(Integer.parseInt(txtRa.getText()));
		ArrayList<Integer> empAtivos = ReservaDAO.emprestimosAtivos(Integer.parseInt(txtRa.getText()));

		if (a != null) {
			if (a.getStatus().toString().equals("Bloqueado")) {
				InfoAlert.infoAlert("Aluno bloqueado", "O aluno inserido est� bloqueado.");
				txtRa.clear();				
			} else if (!empAtivos.isEmpty()) {
				InfoAlert.infoAlert("Aluno possui empr�stimos", "O aluno inserido possui empr�stimos ativos.");
				txtRa.clear();
			} else {
				Document doc = new Document();
				String arquivoPdf = "relatorio_quitacao.pdf";

				try {
					PdfWriter.getInstance(doc, new FileOutputStream(arquivoPdf));
					doc.open();

					Paragraph p = new Paragraph("DECLARA��O");
					p.setAlignment(1);
					doc.add(p);
					p = new Paragraph("  ");
					doc.add(p);
					p = new Paragraph("  ");
					doc.add(p);

					p = new Paragraph("        Declaramos para os devidos fins que o aluno(a) " + a.getNome()
							+ ", matr�cula " + a.getRa().toString() + ", "
							+ "n�o possui pend�ncias com a biblioteca do Instituto Federal de S�o Paulo - "
							+ "Campus S�o Carlos. Declaramos tamb�m que a partir da presente data, "
							+ "essa conta encontra-se encerrada.");
					doc.add(p);

					p = new Paragraph("  ");
					doc.add(p);
					p = new Paragraph("  ");
					doc.add(p);
					p = new Paragraph("  ");
					doc.add(p);
					p = new Paragraph("  ");
					doc.add(p);

					Date data = new Date();
					SimpleDateFormat df;
					df = new SimpleDateFormat("dd/MM/yyyy");
					String dataString = df.format(data);

					p = new Paragraph("S�o Carlos, " + dataString);
					p.setAlignment(2);
					doc.add(p);

					doc.close();

					Desktop.getDesktop().open(new File(arquivoPdf));
					
					fechaStage();

				} catch (Exception e) {
					InfoAlert.errorAlert("Erro", "Erro ao gerar relat�rio de quita��o.");
				}
			}			
		} else {
			InfoAlert.infoAlert("RA inv�lido", "O RA inserido n�o � v�lido.");
			txtRa.clear();
		}
	}

	private void fechaStage() {
		Stage stage = (Stage) txtRa.getScene().getWindow();
		stage.close();
	}
}
