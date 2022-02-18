package br.com.sankhya.pamcard.model;

public class Item {

	private Integer tipo;
	private Integer tarifaQuantidade;
	private Double valor;

	public Integer getTipo() {
		return tipo;
	}

	public void setTipo(Integer tipo) {
		this.tipo = tipo;
	}

	public Integer getTarifaQuantidade() {
		return tarifaQuantidade;
	}

	public void setTarifaQuantidade(Integer tarifaQuantidade) {
		this.tarifaQuantidade = tarifaQuantidade;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

}
