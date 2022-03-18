package br.com.afv.historicopedidos;

import br.com.sankhya.extensions.actionbutton.AcaoRotinaJava;
import br.com.sankhya.extensions.actionbutton.ContextoAcao;

public class AcaoInsereHistoricoVendasAFV implements AcaoRotinaJava {
	
	
	/*
	 * GERAR JAR "AcaoinserePedidoTransferencia.jar" EXPORTANDO OS PACOTES:
	 * br.com.afv.historicopedidos 
	 * br.com.sankhya.notificacao
	 */

	ServiceHistoricoPedidosAfv serviceHistoricoPedidosAfv;

	public void doAction(final ContextoAcao ctx) throws Exception {
		StringBuffer mensagem = new StringBuffer();
		
		ServiceHistoricoPedidosAfv serviceHistoricoPedidosAfv = new ServiceHistoricoPedidosAfv();

		serviceHistoricoPedidosAfv.deletaCabecalhoHistoricoPedAFV();
		serviceHistoricoPedidosAfv.deletaItensCabecalhoHistoricoPedAFV();
		serviceHistoricoPedidosAfv.insereCabecHistoricoPedidosAFV();
		serviceHistoricoPedidosAfv.insereItemHistoricoPedidosAFV();
		serviceHistoricoPedidosAfv.insereCabecHistoricoPedidosRefaturadosAFV();
		serviceHistoricoPedidosAfv.insereItemHistoricoPedidosRefaturadosAFV();
		serviceHistoricoPedidosAfv.insereHistoricoCabecPedidosDuplicPorRegiaoEcom();
		serviceHistoricoPedidosAfv.insereHistoricoItemPedidosDuplicPorRegiaoEcom();
		ctx.setMensagemRetorno(mensagem.toString());

	}

}
