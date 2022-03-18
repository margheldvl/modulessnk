package br.com.sankhya.transferenciapedido.model;

public class CorpoNota {
    
	private CabecalhoPedidoDVL cabecalho;
    private ItemNota itens;

	

	public ItemNota getItens() {
		return itens;
	}

	public void setItens(ItemNota itens) {
		this.itens = itens;
	}

	public CabecalhoPedidoDVL getCabecalho() {
		return cabecalho;
	}

	public void setCabecalho(CabecalhoPedidoDVL cabecalho) {
		this.cabecalho = cabecalho;
	}

	

	
}
