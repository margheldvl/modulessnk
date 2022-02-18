package br.com.sankhya.pamcard.model;

public enum EnumStatusParcela {
	
	PENDENTE("Entrada/Sa�da"),
	LIBERADA("Entrada/Sa�da"),
	BLOQUEADA("Entrada*/Sa�da"),
	EXCLUIDA("Entrada*/Sa�da"),
	EFETIVADA("Sa�da"),
	SEM_FUNDO("Sa�da"),
	ERRO("Sa�da"),
	EM_EFETIVA�AO("Sa�da"),
	CONSUMIDO("Sa�da"),
	PROCESSAR_D�BITO("Sa�da"),
	ENVIADO_D�BITO("Sa�da"),
	ERRO_D�BITO("Sa�da"),
	PROCESSAR_CR�DITO("Sa�da"),
	ENVIADO_CR�DITO("Sa�da"),
	ERRO_CR�DITO("Sa�da"),
	AUTORIZADA("Sa�da"),
	ESTORNADA("Sa�da"),
	PROCESSAR_ESTORNO("Sa�da"),
	ENVIADO_ESTORNO("Sa�da"),
	ERRO_ESTORNO("Sa�da");
 
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
