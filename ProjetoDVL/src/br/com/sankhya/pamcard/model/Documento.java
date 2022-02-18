package br.com.sankhya.pamcard.model;

import java.util.Date;

public class Documento {

	private Integer tipo;
	private String numero;
	private String uf;
	private Integer emissorId;
	private Date dataEmissao;

	public Integer getEmissorId() {
		return emissorId;
	}

	public void setEmissorId(Integer emissorId) {
		this.emissorId = emissorId;
	}

	public Date getDataEmissao() {
		return dataEmissao;
	}

	public void setDataEmissao(Date dataEmissao) {
		this.dataEmissao = dataEmissao;
	}

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

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

}
