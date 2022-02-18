package br.com.sankhya.pamcard.model;

public class Empresa {
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getCnpj() {
		return cnpj;
	}
	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}
	public String getEmpresa_rntrc() {
		return empresa_rntrc;
	}
	public void setEmpresa_rntrc(String empresa_rntrc) {
		this.empresa_rntrc = empresa_rntrc;
	}
	private String nome;
	private String cnpj;
	private String empresa_rntrc;

}
