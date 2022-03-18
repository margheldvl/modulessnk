package br.com.afv.historicopedidos;

import org.cuckoo.core.ScheduledAction;
import org.cuckoo.core.ScheduledActionContext;

public class AcaoAgendadaHistoricoVendaEcommerce implements ScheduledAction {
	
	/*
	 * GERAR JAR "AcaoinserePedidoTransferencia.jar" EXPORTANDO OS PACOTES:
	 * br.com.afv.historicopedidos 
	 * br.com.sankhya.notificacao
	 */

	ServiceHistoricoPedidosAfv serviceHistoricoPedidosAfv;

	@Override
	public void onTime(ScheduledActionContext arg0) {

		try {
			serviceHistoricoPedidosAfv.deletaCabecalhoHistoricoPedAFV();
			serviceHistoricoPedidosAfv.deletaItensCabecalhoHistoricoPedAFV();
			serviceHistoricoPedidosAfv.insereCabecHistoricoPedidosAFV();
			serviceHistoricoPedidosAfv.insereItemHistoricoPedidosAFV();
			serviceHistoricoPedidosAfv.insereCabecHistoricoPedidosRefaturadosAFV();
			serviceHistoricoPedidosAfv.insereItemHistoricoPedidosRefaturadosAFV();
			serviceHistoricoPedidosAfv.insereHistoricoCabecPedidosDuplicPorRegiaoEcom();
			serviceHistoricoPedidosAfv.insereHistoricoItemPedidosDuplicPorRegiaoEcom();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
