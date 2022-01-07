package br.com.dvl.action.pix.cobranca.listeners;

import br.com.action.marcacaonota.MarcacaoNotaFerrero;
import br.com.sankhya.extensions.actionbutton.AcaoRotinaJava;
import br.com.sankhya.extensions.actionbutton.ContextoAcao;

public class MarcacaoNotaFerreroAction  implements AcaoRotinaJava {

	@Override
	public void doAction(ContextoAcao ctx) throws Exception {
		String dataref = ctx.getParam("DTREF").toString();
		String numarc = ctx.getParam("NUMARC").toString();
		MarcacaoNotaFerrero mF = new MarcacaoNotaFerrero();
		
        mF.marcarNota(dataref, numarc);
        ctx.setMensagemRetorno("Notas atualizadas com sucesso!");
	}

}
