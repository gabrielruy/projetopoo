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
				InfoAlert.infoAlert("Aluno bloqueado", "O aluno inserido está bloqueado.");
				txtRa.clear();				
			} else if (!empAtivos.isEmpty()) {
				InfoAlert.infoAlert("Aluno possui empréstimos", "O aluno inserido possui empréstimos ativos.");
				txtRa.clear();
			} else {
				Document doc = new Document();
				String arquivoPdf = "relatorio_quitacao.pdf";

				try {
					PdfWriter.getInstance(doc, new FileOutputStream(arquivoPdf));
					doc.open();

					Paragraph p = new Paragraph("DECLARAÇÃO");
					p.setAlignment(1);
					doc.add(p);
					p = new Paragraph("  ");
					doc.add(p);
					p = new Paragraph("  ");
					doc.add(p);

					p = new Paragraph("        Declaramos para os devidos fins que o aluno(a) " + a.getNome()
							+ ", matrícula " + a.getRa().toString() + ", "
							+ "não possui pendências com a biblioteca do Instituto Federal de São Paulo - "
							+ "Campus São Carlos. Declaramos também que a partir da presente data, "
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

					p = new Paragraph("São Carlos, " + dataString);
					p.setAlignment(2);
					doc.add(p);

					doc.close();

					Desktop.getDesktop().open(new File(arquivoPdf));
					
					fechaStage();

				} catch (Exception e) {
					InfoAlert.errorAlert("Erro", "Erro ao gerar relatório de quitação.");
				}
			}			
		} else {
			InfoAlert.infoAlert("RA inválido", "O RA inserido não é válido.");
			txtRa.clear();
		}
	}

	private void fechaStage() {
		Stage stage = (Stage) txtRa.getScene().getWindow();
		stage.close();
	}
}
