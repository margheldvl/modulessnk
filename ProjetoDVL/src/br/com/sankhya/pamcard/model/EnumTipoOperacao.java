package br.com.sankhya.pamcard.model;

public enum EnumTipoOperacao {

	INSERIR_CONTRATO_DE_FRETE(26), 
	CONSULTAR_CONTRATO_DE_FRETE(27);

	private Integer tipoOperacao;
	
	
	EnumTipoOperacao(Integer tipoOperacao) {
		this.tipoOperacao = tipoOperacao;
	}

	public static EnumStatusParcela getEnumStatusParcela(int ord) {
		return EnumStatusParcela.values()[ord - 1];
	}

	public Integer getTipoOperacao() {
		return tipoOperacao;
	}

	public void setTipoOperacao(Integer tipoOperacao) {
		this.tipoOperacao = tipoOperacao;
	}

	

}
