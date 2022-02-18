package br.com.sankhya.pamcard.model;

import java.util.Date;

public class CIOT {

	private Long codMotorista;
	private Long codProprietario;
	private Long codveiculo;
	private Long nuFin;
	private String status;
	private Double vlrPago;
	private Long numero;
	private String protocolo;
	private String id;
	private String digito;
	private Date dataInic;
	private Date dataFim;
	private String msgIntegra;
	private String nomeArquivo;
	private Date dataGeracao;
	private Integer StatusParcela;
	
	public Integer getStatusParcela() {
		return StatusParcela;
	}

	public void setStatusParcela(Integer statusParcela) {
		StatusParcela = statusParcela;
	}

	public Date getDataGeracao() {
		return dataGeracao;
	}

	public void setDataGeracao(Date dataGeracao) {
		this.dataGeracao = dataGeracao;
	}

	public Long getCodMotorista() {
		return codMotorista;
	}

	public void setCodMotorista(Long codMotorista) {
		this.codMotorista = codMotorista;
	}

	public Long getCodProprietario() {
		return codProprietario;
	}

	public void setCodProprietario(Long codProprietario) {
		this.codProprietario = codProprietario;
	}

	public Long getCodveiculo() {
		return codveiculo;
	}

	public void setCodveiculo(Long codveiculo) {
		this.codveiculo = codveiculo;
	}

	public Long getNuFin() {
		return nuFin;
	}

	public void setNuFin(Long nuFin) {
		this.nuFin = nuFin;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Double getVlrPago() {
		return vlrPago;
	}

	public void setVlrPago(Double vlrPago) {
		this.vlrPago = vlrPago;
	}

	public Long getNumero() {
		return numero;
	}

	public void setNumero(Long numero) {
		this.numero = numero;
	}

	public String getProtocolo() {
		return protocolo;
	}

	public void setProtocolo(String protocolo) {
		this.protocolo = protocolo;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDigito() {
		return digito;
	}

	public void setDigito(String digito) {
		this.digito = digito;
	}

	public Date getDataInic() {
		return dataInic;
	}

	public void setDataInic(Date dataInic) {
		this.dataInic = dataInic;
	}

	public Date getDataFim() {
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}

	public String getMsgIntegra() {
		return msgIntegra;
	}

	public void setMsgIntegra(String msgIntegra) {
		this.msgIntegra = msgIntegra;
	}

	public String getNomeArquivo() {
		return nomeArquivo;
	}

	public void setNomeArquivo(String nomeArquivo) {
		this.nomeArquivo = nomeArquivo;
	}	

}
