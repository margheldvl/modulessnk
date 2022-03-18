package br.com.sankhya.servico.api;

public class ResponseService {
	
	private ResponseMobileLoginSucesso responseBodySucess;
	private ResponseServiceErro responseBodyErro;
	private  ResponseIncluirNota responseIncluirNota;
	
	public ResponseMobileLoginSucesso getResponseBodySucess() {
		return responseBodySucess;
	}
	public void setResponseBodySucess(ResponseMobileLoginSucesso responseBodySucess) {
		this.responseBodySucess = responseBodySucess;
	}
	public ResponseServiceErro getResponseBodyErro() {
		return responseBodyErro;
	}
	public void setResponseBodyErro(ResponseServiceErro responseBodyErro) {
		this.responseBodyErro = responseBodyErro;
	}
	public ResponseIncluirNota getResponseIncluirNota() {
		return responseIncluirNota;
	}
	public void setResponseIncluirNota(ResponseIncluirNota responseIncluirNota) {
		this.responseIncluirNota = responseIncluirNota;
	}

}
