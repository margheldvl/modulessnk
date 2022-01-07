package br.com.dvl.action.pix.cobranca.listeners;

import br.com.action.impostos.RecalculaImpostosVenda;
import br.com.sankhya.extensions.actionbutton.AcaoRotinaJava;
import br.com.sankhya.extensions.actionbutton.ContextoAcao;

public class RecalculaImpPISVendaFreteAction  implements AcaoRotinaJava {

	@Override
	public void doAction(ContextoAcao ctx) throws Exception {
		String dataIni = ctx.getParam("DATAINI").toString();
		String dataFim = ctx.getParam("DATAFIM").toString();
		RecalculaImpostosVenda recDev = new RecalculaImpostosVenda();
		
        recDev.recalculaImpostosPISFretePeriodo(dataIni, dataFim);
        ctx.setMensagemRetorno("Impostos atualizados com sucesso!");
	}

}
