package Controller;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import Model.Aluno;
import Model.AlunoDAO;
import Model.InfoAlert;
import Model.Livro;
import Model.LivroDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.text.Text;

public class FXMLTelaConfigController {

	@FXML
	private Menu mnAluno;
	@FXML
	private MenuItem mnCadastrarAluno;
	@FXML
	private MenuItem mnBuscarAluno;
	@FXML
	private Menu mnLivros;
	@FXML
	private MenuItem mnCadastrarLivro;
	@FXML
	private MenuItem mnBuscarLivros;
	@FXML
	private Menu mnEmprestimo;
	@FXML
	private MenuItem mnEmpLivro;
	@FXML
	private MenuItem mnBuscarEmp;
	@FXML
	private Menu mnReserva;
	@FXML
	private MenuItem mnFazerReserva;
	@FXML
	private MenuItem mnBuscarReserva;
	@FXML
	private Menu mnRelatórios;
	@FXML
	private MenuItem mnRelatorioQuitacao;
	@FXML
	private MenuItem mnEmprestimosPeriodo;
	@FXML
	private MenuItem mnAlunosCadastrados;
	@FXML
	private MenuItem mnLivrosCadastrados;
	@FXML
	private MenuItem mnLivrosDisponiveis;
	@FXML
	private MenuItem mnLivrosEmprestados;
	@FXML
	private MenuItem mnAlunosBloqueados;
	@FXML
	private Text txtAux;

	// Menu: Sair
	@FXML
	void sair() throws IOException {
		Stage stage = (Stage) txtAux.getScene().getWindow();
		stage.close();
	}

	// Menu: Cadastrar Aluno
	@FXML
	void cadAluno() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/FXMLTelaCadastroUsuario.fxml"));
		Pane root = loader.load();

		Scene scene = new Scene(root);
		Stage stage = new Stage();

		stage.setResizable(false);
		stage.initModality(Modality.WINDOW_MODAL);
		stage.initOwner(txtAux.getScene().getWindow());
		stage.setTitle("BIBLIOTECA UNIVERSITÁRIA - Cadastro de Aluno");
		stage.setScene(scene);
		stage.show();
	}

	// Menu: Gerenciar Aluno
	@FXML
	void buscarAluno() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/FXMLTelaBuscarUsuario.fxml"));
		Pane root = loader.load();

		Scene scene = new Scene(root);
		Stage stage = new Stage();

		stage.setResizable(false);
		stage.initModality(Modality.WINDOW_MODAL);
		stage.initOwner(txtAux.getScene().getWindow());
		stage.setTitle("BIBLIOTECA UNIVERSITÁRIA - Gerenciamento de Aluno");
		stage.setScene(scene);
		stage.show();
	}

	// Menu: Cadastrar Livro
	@FXML
	void cadLivro() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/FXMLTelaCadastroLivro.fxml"));
		Pane root = loader.load();

		Scene scene = new Scene(root);
		Stage stage = new Stage();

		stage.setResizable(false);
		stage.initModality(Modality.WINDOW_MODAL);
		stage.initOwner(txtAux.getScene().getWindow());
		stage.setTitle("BIBLIOTECA UNIVERSITÁRIA - Cadastro de Livro");
		stage.setScene(scene);
		stage.show();
	}

	// Menu: Gerenciar Livro
	@FXML
	void buscarLivro() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/FXMLTelaBuscarLivro.fxml"));
		Pane root = loader.load();

		Scene scene = new Scene(root);
		Stage stage = new Stage();

		stage.setResizable(false);
		stage.initModality(Modality.WINDOW_MODAL);
		stage.initOwner(txtAux.getScene().getWindow());
		stage.setTitle("BIBLIOTECA UNIVERSITÁRIA - Gerenciamento de Livro");
		stage.setScene(scene);
		stage.show();
	}

	// Menu: Emprestar Livro
	@FXML
	void empLivro() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/FXMLTelaEmprestimo.fxml"));
		Pane root = loader.load();

		Scene scene = new Scene(root);
		Stage stage = new Stage();

		stage.setResizable(false);
		stage.initModality(Modality.WINDOW_MODAL);
		stage.initOwner(txtAux.getScene().getWindow());
		stage.setTitle("BIBLIOTECA UNIVERSITÁRIA - Empréstimo de Livro");
		stage.setScene(scene);
		stage.show();
	}

	// Menu: Baixa de Empréstimo
	@FXML
	void buscarEmprestimo() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/FXMLTelaBuscarEmprestimo.fxml"));
		Pane root = loader.load();

		Scene scene = new Scene(root);
		Stage stage = new Stage();

		stage.setResizable(false);
		stage.initModality(Modality.WINDOW_MODAL);
		stage.initOwner(txtAux.getScene().getWindow());
		stage.setTitle("BIBLIOTECA UNIVERSITÁRIA - Baixa de Empréstimo");
		stage.setScene(scene);
		stage.show();
	}

	// Menu: Fazer Reserva
	@FXML
	void fazReserva() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/FXMLTelaReserva.fxml"));
		Pane root = loader.load();

		Scene scene = new Scene(root);
		Stage stage = new Stage();

		stage.setResizable(false);
		stage.initModality(Modality.WINDOW_MODAL);
		stage.initOwner(txtAux.getScene().getWindow());
		stage.setTitle("BIBLIOTECA UNIVERSITÁRIA - Reserva de Livro");
		stage.setScene(scene);
		stage.show();
	}

	// Menu: Baixa de Reserva
	@FXML
	void buscarReserva() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/FXMLTelaBuscarReserva.fxml"));
		Pane root = loader.load();

		Scene scene = new Scene(root);
		Stage stage = new Stage();

		stage.setResizable(false);
		stage.initModality(Modality.WINDOW_MODAL);
		stage.initOwner(txtAux.getScene().getWindow());
		stage.setTitle("BIBLIOTECA UNIVERSITÁRIA - Baixa de Reserva");
		stage.setScene(scene);
		stage.show();
	}

	// Menu: Relatório de Quitação
	@FXML
	void relatorioQuitacao() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/FXMLTelaRelatorioQuitacao.fxml"));
		Pane root = loader.load();

		Scene scene = new Scene(root);
		Stage stage = new Stage();

		stage.setResizable(false);
		stage.initModality(Modality.WINDOW_MODAL);
		stage.initOwner(txtAux.getScene().getWindow());
		stage.setTitle("BIBLIOTECA UNIVERSITÁRIA - Relatório de Quitação");
		stage.setScene(scene);
		stage.show();
	}

	// Menu: Empréstimos por período
	@FXML
	void emprestimosPeriodo() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/FXMLTelaEmprestimosPeriodo.fxml"));
		Pane root = loader.load();

		Scene scene = new Scene(root);
		Stage stage = new Stage();

		stage.setResizable(false);
		stage.initModality(Modality.WINDOW_MODAL);
		stage.initOwner(txtAux.getScene().getWindow());
		stage.setTitle("BIBLIOTECA UNIVERSITÁRIA - Empréstimos por período");
		stage.setScene(scene);
		stage.show();
	}

	// Menu: Relatório de alunos cadastrados
	@FXML
	void alunosCadastrados() throws IOException, SQLException {
		Alert alert = InfoAlert.confirmationAlert("Deseja emitir o relatório?",
				"Confirme a emissão do relatório de alunos cadastrados.");
		Optional<ButtonType> result = alert.showAndWait();

		if (result.get() == ButtonType.OK) {
			emitirRelatorioAlunos(false);
		}
	}

	// Menu: Relatório de alunos bloqueados
	@FXML
	void alunosBloqueados() throws SQLException {
		Alert alert = InfoAlert.confirmationAlert("Deseja emitir o relatório?",
				"Confirme a emissão do relatório de alunos bloqueados.");
		Optional<ButtonType> result = alert.showAndWait();

		if (result.get() == ButtonType.OK) {
			emitirRelatorioAlunos(true);
		}
	}

	// Menu: Relatório de livros cadastrados
	@FXML
	void livrosCadastrados() throws SQLException {
		Alert alert = InfoAlert.confirmationAlert("Deseja emitir o relatório?",
				"Confirme a emissão do relatório de livros cadastrados.");
		Optional<ButtonType> result = alert.showAndWait();

		if (result.get() == ButtonType.OK) {
			emitirRelatorioLivros(null);
		}
	}

	// Menu: Relatório de livros disponíveis
	@FXML
	void livrosDisponiveis() throws SQLException {
		Alert alert = InfoAlert.confirmationAlert("Deseja emitir o relatório?",
				"Confirme a emissão do relatório de livros disponíveis.");
		Optional<ButtonType> result = alert.showAndWait();

		if (result.get() == ButtonType.OK) {
			emitirRelatorioLivros("Disponível");
		}
	}

	// Menu: Relatório de livros emprestados
	@FXML
	void livrosEmprestados() throws SQLException {
		Alert alert = InfoAlert.confirmationAlert("Deseja emitir o relatório?",
				"Confirme a emissão do relatório de livros emprestados.");
		Optional<ButtonType> result = alert.showAndWait();

		if (result.get() == ButtonType.OK) {
			emitirRelatorioLivros("Emprestado");
		}
	}

	private void emitirRelatorioAlunos(Boolean bloqueado) throws SQLException {
		ObservableList<Aluno> list = FXCollections.observableArrayList();

		if (bloqueado) {
			list = AlunoDAO.listBlocked();
		} else {
			list = AlunoDAO.listAll();
		}

		if (!list.isEmpty()) {
			Document doc = new Document();
			String arquivoPdf;

			if (bloqueado) {
				arquivoPdf = "relatorio_alunos.pdf";
			} else {
				arquivoPdf = "relatorio_alunos_bloqueados.pdf";
			}

			try {
				PdfWriter.getInstance(doc, new FileOutputStream(arquivoPdf));
				doc.open();

				doc.setPageSize(PageSize.A4.rotate());

				String cabecalho;

				if (bloqueado) {
					cabecalho = "ALUNOS BLOQUEADOS";
				} else {
					cabecalho = "ALUNOS CADASTRADOS";
				}

				Paragraph p = new Paragraph(cabecalho);
				p.setAlignment(1);
				doc.add(p);
				p = new Paragraph("  ");
				doc.add(p);
				p = new Paragraph("  ");
				doc.add(p);

				PdfPTable table = new PdfPTable(5);

				PdfPCell cell1 = new PdfPCell(new Paragraph("Nome"));
				PdfPCell cell2 = new PdfPCell(new Paragraph("RG"));
				PdfPCell cell3 = new PdfPCell(new Paragraph("CPF"));
				PdfPCell cell4 = new PdfPCell(new Paragraph("E-mail"));
				PdfPCell cell5 = new PdfPCell(new Paragraph("RA"));

				table.addCell(cell1);
				table.addCell(cell2);
				table.addCell(cell3);
				table.addCell(cell4);
				table.addCell(cell5);

				for (Aluno a : list) {
					cell1 = new PdfPCell(new Paragraph(a.getNome()));
					cell2 = new PdfPCell(new Paragraph(a.getRg()));
					cell3 = new PdfPCell(new Paragraph(a.getCpf()));
					cell4 = new PdfPCell(new Paragraph(a.getEmail()));
					cell5 = new PdfPCell(new Paragraph(a.getRa() + ""));

					table.addCell(cell1);
					table.addCell(cell2);
					table.addCell(cell3);
					table.addCell(cell4);
					table.addCell(cell5);
				}

				doc.add(table);

				doc.close();

				Desktop.getDesktop().open(new File(arquivoPdf));
			} catch (Exception e) {
				InfoAlert.errorAlert("Erro", "Erro ao gerar relatório de alunos.");
			}
		} else
			InfoAlert.errorAlert("Listagem de alunos vazia",
					"O sistema não possui alunos cadastrados com essa situação.");
	}

	private void emitirRelatorioLivros(String situacao) throws SQLException {
		ObservableList<Livro> list = FXCollections.observableArrayList();

		if (situacao == null) {
			list = LivroDAO.listAll();
		} else {
			list = LivroDAO.listBySituation(situacao, null);
		}

		if (!list.isEmpty()) {
			Document doc = new Document();
			String arquivoPdf;

			if (situacao == null) {
				arquivoPdf = "relatorio_livros.pdf";
			} else if (situacao.equals("Emprestado")) {
				arquivoPdf = "relatorio_livros_emprestados.pdf";
			} else {
				arquivoPdf = "relatorio_livros_disponíveis.pdf";
			}

			try {
				PdfWriter.getInstance(doc, new FileOutputStream(arquivoPdf));
				doc.open();

				doc.setPageSize(PageSize.A4.rotate());

				String cabecalho;

				if (situacao == null) {
					cabecalho = "LIVROS CADASTRADOS";
				} else if (situacao.equals("Emprestado")) {
					cabecalho = "LIVROS EMPRESTADOS";
				} else {
					cabecalho = "LIVROS DISPONÍVEIS";
				}

				Paragraph p = new Paragraph(cabecalho);
				p.setAlignment(1);
				doc.add(p);
				p = new Paragraph("  ");
				doc.add(p);
				p = new Paragraph("  ");
				doc.add(p);

				PdfPTable table = new PdfPTable(5);

				PdfPCell cell1 = new PdfPCell(new Paragraph("Nome"));
				PdfPCell cell2 = new PdfPCell(new Paragraph("Autor"));
				PdfPCell cell3 = new PdfPCell(new Paragraph("Editora"));
				PdfPCell cell4 = new PdfPCell(new Paragraph("Edição"));
				PdfPCell cell5 = new PdfPCell(new Paragraph("ISBN"));

				table.addCell(cell1);
				table.addCell(cell2);
				table.addCell(cell3);
				table.addCell(cell4);
				table.addCell(cell5);

				for (Livro l : list) {
					cell1 = new PdfPCell(new Paragraph(l.getNome()));
					cell2 = new PdfPCell(new Paragraph(l.getAutor()));
					cell3 = new PdfPCell(new Paragraph(l.getEditora()));
					cell4 = new PdfPCell(new Paragraph(l.getNroEdicao() + ""));
					cell5 = new PdfPCell(new Paragraph(l.getIsbn()));

					table.addCell(cell1);
					table.addCell(cell2);
					table.addCell(cell3);
					table.addCell(cell4);
					table.addCell(cell5);
				}

				doc.add(table);

				doc.close();

				Desktop.getDesktop().open(new File(arquivoPdf));
			} catch (Exception e) {
				InfoAlert.errorAlert("Erro", "Erro ao gerar relatório de livros.");
			}
		} else
			InfoAlert.errorAlert("Listagem de livros vazia",
					"O sistema não possui livros cadastrados com essa situação.");
	}
}
