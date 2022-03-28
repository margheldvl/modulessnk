package br.com.dvl.action;

import br.com.dvl.wbean.ProgramacaoVisitaWBean;
import br.com.sankhya.extensions.actionbutton.AcaoRotinaJava;
import br.com.sankhya.extensions.actionbutton.ContextoAcao;

public class AcaoAtualizaRoteirizacaoVisita implements AcaoRotinaJava {
	

/* 
* => ARQUIVOS NECESSÁRIOS PARA GERAR JAR 
* 	 - C:\Projeto GitHub DVL\modulessnk\ProjetoDVL\src\br\com\dvl\action\AcaoAtualizaRoteirizacaoVisita.java
* 	 - C:\Projeto GitHub DVL\modulessnk\ProjetoDVL\src\br\com\dvl\dao\ProgramacaoVisitaDAO.java
* 	 - C:\Projeto GitHub DVL\modulessnk\ProjetoDVL\src\br\com\dvl\model\ProgramacaoVisita.java
* 	 - C:\Projeto GitHub DVL\modulessnk\ProjetoDVL\src\br\com\dvl\service\ProgramacaoVisitaService.java
* 	 - C:\Projeto GitHub DVL\modulessnk\ProjetoDVL\src\br\com\dvl\wbean\ProgramacaoVisitaWBean.java
*/
	public void doAction(ContextoAcao ctx) throws Exception {

		StringBuffer mensagem = new StringBuffer();

		ProgramacaoVisitaWBean programacaoVisitaWBean = new ProgramacaoVisitaWBean();
		mensagem.append(programacaoVisitaWBean.insereProgramacaoVisita(ctx));

		ctx.setMensagemRetorno(mensagem.toString());

	}

}