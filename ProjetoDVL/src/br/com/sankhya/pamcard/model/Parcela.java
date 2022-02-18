package br.com.sankhya.pamcard.model;

import java.util.Date;

public class Parcela {

	private Date data;
	private Integer efetivacaoTipo;
	private Integer favorecidoId;
	private Integer numeroCliente;
	private Integer statusId;
	private Integer tipo;
	private Double valor;
	private Integer subTipo;
	private Integer favorecidoTipo;

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Integer getEfetivacaoTipo() {
		return efetivacaoTipo;
	}

	public void setEfetivacaoTipo(Integer efetivacaoTipo) {
		this.efetivacaoTipo = efetivacaoTipo;
	}

	public Integer getFavorecidoId() {
		return favorecidoId;
	}

	public void setFavorecidoId(Integer favorecidoId) {
		this.favorecidoId = favorecidoId;
	}

	public Integer getNumeroCliente() {
		return numeroCliente;
	}

	public void setNumeroCliente(Integer numeroCliente) {
		this.numeroCliente = numeroCliente;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public Integer getTipo() {
		return tipo;
	}

	public void setTipo(Integer tipo) {
		this.tipo = tipo;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public Integer getSubTipo() {
		return subTipo;
	}

	public void setSubTipo(Integer subTipo) {
		this.subTipo = subTipo;
	}

	public Integer getFavorecidoTipo() {
		return favorecidoTipo;
	}

	public void setFavorecidoTipo(Integer favorecidoTipo) {
		this.favorecidoTipo = favorecidoTipo;
	}

//	
}
