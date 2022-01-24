package br.com.dvl.action.pix.cobranca.listeners;

import br.com.action.pix.service.Token;
import br.com.sankhya.extensions.actionbutton.AcaoRotinaJava;
import br.com.sankhya.extensions.actionbutton.ContextoAcao;
import br.com.sankhya.modelcore.assinaturadigital.DigitalSignatureManager;

public class DirectoryCertificado implements AcaoRotinaJava {

	@Override
	public void doAction(ContextoAcao arg0) throws Exception {
		// TODO Auto-generated method stub
		Token tk = new Token();
		arg0.setMensagemRetorno(tk.getToken());
	}

}
