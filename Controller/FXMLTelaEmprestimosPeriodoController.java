package Controller;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.SQLException;
import java.util.ArrayList;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import Model.InfoAlert;
import Model.Reserva;
import Model.ReservaDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;

public class FXMLTelaEmprestimosPeriodoController {

    @FXML
    private Button btnGerar;
    @FXML
    private DatePicker dateInicio;
    @FXML
    private DatePicker dateFim;

    @FXML
    private void gerar() throws SQLException {
    	if (estaPreenchido()) {
    		if (periodoValido()) {
    			ArrayList<Reserva> list = ReservaDAO.listByPeriod(dateInicio.getValue(), dateFim.getValue());
    			
    			if (!list.isEmpty()) {
    				Document doc = new Document();
    				String arquivoPdf = "relatorio_emprestimos.pdf";

    				try {
    					PdfWriter.getInstance(doc, new FileOutputStream(arquivoPdf));
    					doc.open();
    					
    					doc.setPageSize(PageSize.A4.rotate());

    					Paragraph p = new Paragraph("EMPRÉSTIMOS POR PERÍODO");
    					p.setAlignment(1);
    					doc.add(p);
    					p = new Paragraph("  ");
    					doc.add(p);
    					p = new Paragraph("  ");
    					doc.add(p);

    					PdfPTable table = new PdfPTable(7);
    					
    					PdfPCell cell1 = new PdfPCell(new Paragraph("ID Livro"));
    					PdfPCell cell2 = new PdfPCell(new Paragraph("Nome do Livro"));
    					PdfPCell cell3 = new PdfPCell(new Paragraph("Autor"));
    					PdfPCell cell4 = new PdfPCell(new Paragraph("Nome do Aluno"));
    					PdfPCell cell5 = new PdfPCell(new Paragraph("RA"));
    					PdfPCell cell6 = new PdfPCell(new Paragraph("Data de Retirada"));
    					PdfPCell cell7 = new PdfPCell(new Paragraph("Data de Devolução"));
    					
    					table.addCell(cell1);
    					table.addCell(cell2);
    					table.addCell(cell3);
    					table.addCell(cell4);
    					table.addCell(cell5);
    					table.addCell(cell6);
    					table.addCell(cell7);
    					
    					for (Reserva r : list) {
        					cell1 = new PdfPCell(new Paragraph(r.getLivro().getId()+""));
        					cell2 = new PdfPCell(new Paragraph(r.getLivro().getNome()));
        					cell3 = new PdfPCell(new Paragraph(r.getLivro().getAutor()));
        					cell4 = new PdfPCell(new Paragraph(r.getAluno().getNome()));
        					cell5 = new PdfPCell(new Paragraph(r.getAluno().getRa()+""));
        					cell6 = new PdfPCell(new Paragraph(r.getDataRetirada().toString()));
        					cell7 = new PdfPCell(new Paragraph(r.getDataDevolucao().toString()));
        					
        					table.addCell(cell1);
        					table.addCell(cell2);
        					table.addCell(cell3);
        					table.addCell(cell4);
        					table.addCell(cell5);
        					table.addCell(cell6);
        					table.addCell(cell7);
    					}

    					doc.add(table);
    					
    					doc.close();

    					Desktop.getDesktop().open(new File(arquivoPdf));
    					
    					fechaStage();

    				} catch (Exception e) {
    					InfoAlert.errorAlert("Erro", "Erro ao gerar relatório de empréstimos.");
    				}
    			} else
    				InfoAlert.errorAlert("Período vazio", "O período informado não possui empréstimos.");
    		} else
    			InfoAlert.errorAlert("Erro", "O período informado não é válido");
    	} else 
    		InfoAlert.errorAlert("Erro", "Preencha todos os campos");
    }
    
    private Boolean estaPreenchido() {
    	if (dateInicio.getValue() == null || dateFim.getValue() ==  null) 
    		return false;
    	return true;
    }
    
    private Boolean periodoValido() {
    	if (dateInicio.getValue().isAfter(dateFim.getValue()))
    		return false;
    	return true;
    }
    
    private void fechaStage() {
		Stage stage = (Stage) btnGerar.getScene().getWindow();
		stage.close();
	}
}
