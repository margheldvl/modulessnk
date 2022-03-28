package br.com.dvl.model;

public class ProgramacaoVisita {
	
	private Long codparc;
	private String tipo;
	private Long valor;
	private Long seqvisita;
	private Long codvend;
	private Long codreg;
	
	private transient boolean procedRealizado;

	public Long getCodparc() {
		return codparc;
	}

	public String getTipo() {
		return tipo;
	}

	public Long getValor() {
		return valor;
	}

	public Long getSeqvisita() {
		return seqvisita;
	}

	public Long getCodvend() {
		return codvend;
	}

	public Long getCodreg() {
		return codreg;
	}

	public void setCodparc(Long codparc) {
		this.codparc = codparc;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public void setValor(Long valor) {
		this.valor = valor;
	}

	public void setSeqvisita(Long seqvisita) {
		this.seqvisita = seqvisita;
	}

	public void setCodvend(Long codvend) {
		this.codvend = codvend;
	}

	public void setCodreg(Long codreg) {
		this.codreg = codreg;
	}

	public boolean isProcedRealizado() {
		return procedRealizado;
	}

	public void setProcedRealizado(boolean procedRealizado) {
		this.procedRealizado = procedRealizado;
	}
}