package Model;

import java.time.LocalDate;

public class Livro {
	
	private Integer id;
	private String nome;	
	private String autor;
	private LocalDate publicacao;
	private String editora;
	private Integer nroEdicao;
	private String isbn;
	private String situacao;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}	
	public String getAutor() {
		return autor;
	}
	public void setAutor(String autor) {
		this.autor = autor;
	}
	public LocalDate getPublicacao() {
		return publicacao;
	}
	public void setPublicacao(LocalDate publicacao) {
		this.publicacao = publicacao;
	}
	public String getEditora() {
		return editora;
	}
	public void setEditora(String editora) {
		this.editora = editora;
	}
	public Integer getNroEdicao() {
		return nroEdicao;
	}
	public void setNroEdicao(Integer nroEdicao) {
		this.nroEdicao = nroEdicao;
	}
	public String getIsbn() {
		return isbn;
	}
	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}
	public String getSituacao() {
		return situacao;
	}
	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}
}
