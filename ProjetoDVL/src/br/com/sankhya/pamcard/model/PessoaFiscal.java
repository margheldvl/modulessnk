package br.com.sankhya.pamcard.model;

public class PessoaFiscal {

	private Integer tipo;
	DocumentoPessoFiscal documentoPessoFiscal;
	String nome;
	private EnderecoPessoaFiscal enderecoPessoaFiscal;

	public Integer getTipo() {
		return tipo;
	}

	public void setTipo(Integer tipo) {
		this.tipo = tipo;
	}

	public DocumentoPessoFiscal getDocumentoPessoFiscal() {
		return documentoPessoFiscal;
	}

	public void setDocumentoPessoFiscal(DocumentoPessoFiscal documentoPessoFiscal) {
		this.documentoPessoFiscal = documentoPessoFiscal;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public EnderecoPessoaFiscal getEnderecoPessoaFiscal() {
		return enderecoPessoaFiscal;
	}

	public void setEnderecoPessoaFiscal(EnderecoPessoaFiscal enderecoPessoaFiscal) {
		this.enderecoPessoaFiscal = enderecoPessoaFiscal;
	}

}
