package br.com.sankhya.inventario.service;

import java.util.List;

import br.com.sankhya.inventario.dao.InventarioDAO;

public class InventarioService {

	InventarioDAO inventarioDAO = new InventarioDAO();

	public List<String> achouApenasUmaOrdemCarga(List<String> listNota, String ordemCarga) throws Exception {
		return inventarioDAO.achouApenasUmaOrdemCarga(listNota, ordemCarga);
	}

	public List<String> notasNaoEncontradas(List<String> listNota, String ordemCarga) throws Exception {
		return inventarioDAO.notasNaoEncontradas(listNota, ordemCarga);
	}

	public String insereRetornoMercadorias(List<String> listNota, String ordemCarga, String codUser) throws Exception {
		return inventarioDAO.insereRetornoMercadorias(listNota, ordemCarga, codUser);
	}

}
