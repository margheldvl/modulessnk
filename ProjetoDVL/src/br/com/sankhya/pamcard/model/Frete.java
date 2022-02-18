package br.com.sankhya.pamcard.model;

import java.util.ArrayList;
import java.util.List;

public class Frete {

	private Double valorBruto;
	private Integer itemQtde;
	List<Item> listItem = new ArrayList<Item>();

	public Double getValorBruto() {
		return valorBruto;
	}

	public void setValorBruto(Double valorBruto) {
		this.valorBruto = valorBruto;
	}

	public Integer getItemQtde() {
		return itemQtde;
	}

	public void setItemQtde(Integer itemQtde) {
		this.itemQtde = itemQtde;
	}

	public List<Item> getListItem() {
		return listItem;
	}

	public void setListItem(List<Item> listItem) {
		this.listItem = listItem;
		itemQtde = listItem.size();
	}

}
