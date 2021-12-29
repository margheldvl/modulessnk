package br.com.dvl.action.pix.cobranca.listeners;

import br.com.action.pix.service.*;
import br.com.sankhya.extensions.actionbutton.AcaoRotinaJava;
import br.com.sankhya.extensions.actionbutton.ContextoAcao;
import br.com.sankhya.extensions.actionbutton.Registro;


	public class CobrancaImediataPix implements AcaoRotinaJava {
	@Override
	public void doAction(ContextoAcao contexto) throws Exception {

		Registro[] linhaSelecionada = contexto.getLinhas(); 
		Number nuNota = 0;
		for ( Registro linha : linhaSelecionada ) {
		   nuNota = (Number) linha.getCampo("NUNOTA");	
		}
        
		RegistraCobranca cob = new RegistraCobranca(); 
	    cob.setCobrancaTLS(nuNota);
        contexto.setMensagemRetorno( "Cobrança gerada com sucesso!" );
	}

}