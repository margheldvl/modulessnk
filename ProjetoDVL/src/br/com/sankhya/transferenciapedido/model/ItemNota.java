package br.com.sankhya.transferenciapedido.model;

import java.util.List;

public class ItemNota {

	private String INFORMARPRECO;
	private List<ItemPedidoDVL> item;

	public List<ItemPedidoDVL> getItem() {
		return item;
	}

	public void setItem(List<ItemPedidoDVL> item) {
		this.item = item;
	}

	public String getINFORMARPRECO() {
		return INFORMARPRECO;
	}

	public void setINFORMARPRECO(String iNFORMARPRECO) {
		INFORMARPRECO = iNFORMARPRECO;
	}

	


}
