package br.com.sankhya.pamcard.model;

import java.util.ArrayList;
import java.util.List;

public class DocumentoViagem {

	private Integer tipo;
	private String numero;
	private String serie;
	private Double quantidade;
	private String especie;
	private Double cubagem;
	private Integer natureza;
	private Double peso;
	private Double mercadoriaValor;	
	private Integer pessoaFiscalQtde;	
	private List<PessoaFiscal> listPessoaFiscal = new ArrayList<PessoaFiscal>();

	public Integer getTipo() {
		return tipo;
	}

	public void setTipo(Integer tipo) {
		this.tipo = tipo;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getSerie() {
		return serie;
	}

	public void setSerie(String serie) {
		this.serie = serie;
	}

	public Double getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Double quantidade) {
		this.quantidade = quantidade;
	}

	public String getEspecie() {
		return especie;
	}

	public void setEspecie(String especie) {
		this.especie = especie;
	}

	public Double getCubagem() {
		return cubagem;
	}

	public void setCubagem(Double cubagem) {
		this.cubagem = cubagem;
	}

	public Integer getNatureza() {
		return natureza;
	}

	public void setNatureza(Integer natureza) {
		this.natureza = natureza;
	}

	public Double getPeso() {
		return peso;
	}

	public void setPeso(Double peso) {
		this.peso = peso;
	}

	public Double getMercadoriaValor() {
		return mercadoriaValor;
	}

	public void setMercadoriaValor(Double mercadoriaValor) {
		this.mercadoriaValor = mercadoriaValor;
	}

	public Integer getPessoaFiscalQtde() {
		return pessoaFiscalQtde;
	}

	public void setPessoaFiscalQtde(Integer pessoaFiscalQtde) {
		this.pessoaFiscalQtde = pessoaFiscalQtde;
	}

	public List<PessoaFiscal> getListPessoaFiscal() {
		return listPessoaFiscal;
	}

	public void setListPessoaFiscal(List<PessoaFiscal> listPessoaFiscal) {
		this.listPessoaFiscal = listPessoaFiscal;
		pessoaFiscalQtde = this.listPessoaFiscal.size();
	}

}
