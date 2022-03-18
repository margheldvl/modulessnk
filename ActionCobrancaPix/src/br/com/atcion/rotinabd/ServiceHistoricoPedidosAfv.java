package br.com.atcion.rotinabd;

public class ServiceHistoricoPedidosAfv {

	HistoricoPedidosAfvDAO daoHstPedidosAfv;

	public void deletaCabecalhoHistoricoPedAFV() throws Exception {
		daoHstPedidosAfv.deletaCabecalhoHistoricoPedAFV();
	}

	public void deletaItensCabecalhoHistoricoPedAFV() throws Exception {
		daoHstPedidosAfv.deletaItensCabecalhoHistoricoPedAFV();

	}

	public void insereCabecHistoricoPedidosAFV() throws Exception {
		daoHstPedidosAfv.insereCabecHistoricoPedidosAFV();
	}

	public void insereCabecHistoricoPedidosRefaturadosAFV() throws Exception {
		daoHstPedidosAfv.insereCabecHistoricoPedidosRefaturadosAFV();
	}

	public void insereItemHistoricoPedidosAFV() throws Exception {
		daoHstPedidosAfv.insereItemHistoricoPedidosAFV();
	}

	public void insereItemHistoricoPedidosRefaturadosAFV() throws Exception {
		daoHstPedidosAfv.insereItemHistoricoPedidosRefaturadosAFV();
	}

	public void insereHistoricoCabecPedidosDuplicPorRegiaoEcom() throws Exception {
		daoHstPedidosAfv.insereHistoricoCabecPedidosDuplicPorRegiaoEcom();

	}

	public void insereHistoricoItemPedidosDuplicPorRegiaoEcom() throws Exception {
		daoHstPedidosAfv.insereHistoricoItemPedidosDuplicPorRegiaoEcom();

	}

}
