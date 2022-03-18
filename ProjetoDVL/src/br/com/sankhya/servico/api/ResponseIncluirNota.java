package br.com.sankhya.servico.api;

public class ResponseIncluirNota {

	private String serviceName;
	private String status;
	private String pendingPrinting;
	private String transactionId;
	private String NUNOTA;

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPendingPrinting() {
		return pendingPrinting;
	}

	public void setPendingPrinting(String pendingPrinting) {
		this.pendingPrinting = pendingPrinting;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getNunota() {
		return NUNOTA;
	}

	public void setNunota(String nunota) {
		this.NUNOTA = nunota;
	}

}
