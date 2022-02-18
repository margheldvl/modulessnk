package br.com.sankhya.pamcard.model;

import java.util.Date;

public class Endereco {

	private String logradouro;
	private Integer numero;
	private String bairro;
	private String complemento;
	private Integer cep;
	private Cidade cidade;
	private Integer tipoPropriedade;
	private Date resideDesde;

	public Date getResideDesde() {
		return resideDesde;
	}

	public void setResideDesde(Date resideDesde) {
		this.resideDesde = resideDesde;
	}

	public Integer getTipoPropriedade() {
		return tipoPropriedade;
	}

	public void setTipoPropriedade(Integer tipoPropriedade) {
		this.tipoPropriedade = tipoPropriedade;
	}

	public String getLogradouro() {
		return logradouro;
	}

	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}

	public Integer getNumero() {
		return numero;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	public Integer getCep() {
		return cep;
	}

	public void setCep(Integer cep) {
		this.cep = cep;
	}

	public Cidade getCidade() {
		return cidade;
	}

	public void setCidade(Cidade cidade) {
		this.cidade = cidade;
	}


}
