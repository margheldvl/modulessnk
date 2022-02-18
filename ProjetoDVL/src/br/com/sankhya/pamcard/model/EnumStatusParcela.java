package br.com.sankhya.pamcard.model;

public enum EnumStatusParcela {
	
	PENDENTE("Entrada/Saída"),
	LIBERADA("Entrada/Saída"),
	BLOQUEADA("Entrada*/Saída"),
	EXCLUIDA("Entrada*/Saída"),
	EFETIVADA("Saída"),
	SEM_FUNDO("Saída"),
	ERRO("Saída"),
	EM_EFETIVAÇAO("Saída"),
	CONSUMIDO("Saída"),
	PROCESSAR_DÉBITO("Saída"),
	ENVIADO_DÉBITO("Saída"),
	ERRO_DÉBITO("Saída"),
	PROCESSAR_CRÉDITO("Saída"),
	ENVIADO_CRÉDITO("Saída"),
	ERRO_CRÉDITO("Saída"),
	AUTORIZADA("Saída"),
	ESTORNADA("Saída"),
	PROCESSAR_ESTORNO("Saída"),
	ENVIADO_ESTORNO("Saída"),
	ERRO_ESTORNO("Saída");
 
	private String descricao;
	private Integer indice;
	
	EnumStatusParcela(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

	public Integer getIndice() {
		return indice;
	}

	public void setIndice(Integer indice) {
		this.indice = indice;
	}
	
	 public static EnumStatusParcela getEnumStatusParcela(int ord) {
         return EnumStatusParcela.values()[ord -1];
     }

	

}
