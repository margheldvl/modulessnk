package br.com.sankhya.transferenciapedido.service;

import java.util.List;

import br.com.sankhya.transferenciapedido.dao.ItemPedidoDAO;
import br.com.sankhya.transferenciapedido.dao.PedidoDAO;
import br.com.sankhya.transferenciapedido.model.CabecalhoPedidoDVL;
import br.com.sankhya.transferenciapedido.model.EstoqueDVL;
import br.com.sankhya.transferenciapedido.model.ItemPedidoDVL;

public class ServiceTransferenciaPedido {

	PedidoDAO pedidoDAO = new PedidoDAO();
	ItemPedidoDAO itemPedidoDAO = new ItemPedidoDAO();

	public List<CabecalhoPedidoDVL> buscaCabecalhoPedidoDVL() throws Exception {
		return pedidoDAO.buscaCabecalhoPedidoDVL();
	}

	public List<ItemPedidoDVL> buscaItemPedidoDVL(Long nuNota) throws Exception {
		return itemPedidoDAO.buscaItemPedidoDVL(nuNota);
	}

	public List<EstoqueDVL> buscaEstoqueIndisp(Long nuNota) throws Exception {
		return itemPedidoDAO.buscaEstoqueIndisp(nuNota);
	}

	public List<String> buscaDestinatariosEmail(String listaParc) throws Exception {
		return pedidoDAO.buscaDestinatariosEmail(listaParc);
	}

	public boolean deletaItensNota(Long nuNota, Long codProd) throws Exception {
		return itemPedidoDAO.deletaItensNota(nuNota, codProd);
	}

	public boolean deletaCabecalhoNota(Long nuNota) throws Exception {
		return pedidoDAO.deletaCabecalhoNota(nuNota);
	}

	public List<EstoqueDVL> buscaItensValorComSemEstoqNota(Long nuNota) throws Exception {
		return itemPedidoDAO.buscaItensValorComSemEstoqNota(nuNota);
	}

	public boolean atualizaLibLimNota(Long nuNota) throws Exception {
		return pedidoDAO.atualizaLibLimNota(nuNota);
	}

	public String notaConfirmada(Long nuNota) throws Exception {
		return pedidoDAO.notaConfirmada(nuNota);

	}
}
