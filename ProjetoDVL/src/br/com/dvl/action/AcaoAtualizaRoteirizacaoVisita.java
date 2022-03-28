package br.com.dvl.action;

import br.com.dvl.wbean.ProgramacaoVisitaWBean;
import br.com.sankhya.extensions.actionbutton.AcaoRotinaJava;
import br.com.sankhya.extensions.actionbutton.ContextoAcao;

public class AcaoAtualizaRoteirizacaoVisita implements AcaoRotinaJava {

	public void doAction(ContextoAcao ctx) throws Exception {

		StringBuffer mensagem = new StringBuffer();

		ProgramacaoVisitaWBean programacaoVisitaWBean = new ProgramacaoVisitaWBean();
		mensagem.append(programacaoVisitaWBean.insereProgramacaoVisita(ctx));

		ctx.setMensagemRetorno(mensagem.toString());

	}

}