package br.com.sankhya.pamcard.model;

public class Telefone {

	private String telefoneDDD;
	private Integer telefoneNumero;
	private Integer celularDDD;
	private Integer celularNumero;

	public String getTelefoneDDD() {
		return telefoneDDD;
	}

	public Integer getTelefoneNumero() {
		return telefoneNumero;
	}

	public void setTelefoneNumero(Integer telefoneNumero) {
		this.telefoneNumero = telefoneNumero;
	}

	public Integer getCelularDDD() {
		return celularDDD;
	}

	public void setCelularDDD(Integer celularDDD) {
		this.celularDDD = celularDDD;
	}

	public Integer getCelularNumero() {
		return celularNumero;
	}

	public void setCelularNumero(Integer celularNumero) {
		this.celularNumero = celularNumero;
	}

	public void setTelefoneDDD(String telefoneDDD) {
		this.telefoneDDD = telefoneDDD;
	}

}
