package br.com.sankhya.servico.api;

public class ResponseServiceErro {

	private String callID;
	private String jsessionid;
	private String kID;
	private String idusu;
	private String tsErrorCode;
	private String tsErrorLevel;
	private String statusMessage;

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

	public String getTsErrorCode() {
		return tsErrorCode;
	}

	public void setTsErrorCode(String tsErrorCode) {
		this.tsErrorCode = tsErrorCode;
	}

	public String getTsErrorLevel() {
		return tsErrorLevel;
	}

	public void setTsErrorLevel(String tsErrorLevel) {
		this.tsErrorLevel = tsErrorLevel;
	}

	public String getStatusMessage() {
		return statusMessage;
	}

	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}

//	@SuppressWarnings("serial")
//	private List<String> listaInformacoes = new ArrayList<String>() {
//		{
//			add("String 1");
//			add("String 2");
//			add("String 3");
//		}
//	};

//	@Override
//	public String toString() {
//		return "Informacoes [Informação 1 =" + informacao1 + ", Informação 2 =" + informacao2 + ", list="
//				+ listaInformacoes + "]";
//	}
//
//	public int getInformacao1() {
//		return informacao1;
//	}
//
//	public void setInformacao1(int informacao1) {
//		this.informacao1 = informacao1;
//	}
//
//	public String getInformacao2() {
//		return informacao2;
//	}
//
//	public void setInformacao2(String informacao2) {
//		this.informacao2 = informacao2;
//	}
//
//	public List<String> getListaInformacoes() {
//		return listaInformacoes;
//	}
//
//	public void setListaInformacoes(List<String> listaInformacoes) {
//		this.listaInformacoes = listaInformacoes;
//	}
}