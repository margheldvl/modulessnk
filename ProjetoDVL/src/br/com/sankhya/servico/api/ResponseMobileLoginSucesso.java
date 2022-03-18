package br.com.sankhya.servico.api;

public class ResponseMobileLoginSucesso {

	private String callID;
	private String jsessionid;
	private String kID;
	private String idusu;

	public String getCallID() {
		return callID;
	}

	public void setCallID(String callID) {
		this.callID = callID;
	}

	public String getJsessionid() {
		return jsessionid;
	}

	public void setJsessionid(String jsessionid) {
		this.jsessionid = jsessionid;
	}

	public String getkID() {
		return kID;
	}

	public void setkID(String kID) {
		this.kID = kID;
	}

	public String getIdusu() {
		return idusu;
	}

	public void setIdusu(String idusu) {
		this.idusu = idusu;
	}
}