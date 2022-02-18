package br.com.sankhya.pamcard.model;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

public class Favorecido {

	private Integer tipo;
	private Integer documentoQtde;
	private List<Documento> listDocumento = new ArrayList<Documento>();
	private String nome;
	private Date dataNascimento;
	private Endereco endereco;
	private Telefone telefone;
	private String email;
	private String meioPagamento;
	Conta conta;
	private Integer nacionalidade_id;
	private Cidade naturalidadeCidadeIbge;
	private String sexo;
	private String cartao;
	private Empresa empresa;

	public String getCartao() {
		return cartao;
	}

	public void setCartao(String cartao) {
		this.cartao = cartao;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public Integer getNacionalidade_id() {
		return nacionalidade_id;
	}

	public void setNacionalidade_id(Integer nacionalidade_id) {
		this.nacionalidade_id = nacionalidade_id;
	}

	public Cidade getNaturalidadeCidadeIbge() {
		return naturalidadeCidadeIbge;
	}

	public void setNaturalidadeCidadeIbge(Cidade naturalidadeCidadeIbge) {
		this.naturalidadeCidadeIbge = naturalidadeCidadeIbge;
	}

	public String getSexo() {
		return sexo;
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	public Integer getTipo() {
		return tipo;
	}

	public void setTipo(Integer tipo) {
		this.tipo = tipo;
	}

	public Integer getDocumentoQtde() {
		return documentoQtde;
	}

	public void setDocumentoQtde(Integer documentoQtde) {
		this.documentoQtde = documentoQtde;
		this.documentoQtde = this.listDocumento.size();
	}

	public List<Documento> getListDocumento() {
		return listDocumento;
	}

	public void setListDocumento(List<Documento> listDocumento) {
		this.listDocumento = listDocumento;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Date getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(Date dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	public Telefone getTelefone() {
		return telefone;
	}

	public void setTelefone(Telefone telefone) {
		this.telefone = telefone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMeioPagamento() {
		return meioPagamento;
	}

	public void setMeioPagamento(String meioPagamento) {
		this.meioPagamento = meioPagamento;
	}

	public Conta getConta() {
		return conta;
	}

	public void setConta(Conta conta) {
		this.conta = conta;
	}

}
