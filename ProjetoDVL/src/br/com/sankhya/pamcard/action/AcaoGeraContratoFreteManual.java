package br.com.sankhya.pamcard.action;

import java.util.HashMap;
import java.util.Map;

import br.com.sankhya.extensions.actionbutton.AcaoRotinaJava;
import br.com.sankhya.extensions.actionbutton.ContextoAcao;
import br.com.sankhya.extensions.actionbutton.Registro;
import br.com.sankhya.pamcard.model.Conversoes;
import br.com.sankhya.pamcard.model.EnumTipoOperacao;
import br.com.sankhya.pamcard.wbean.ContratoFreteWBean;

public class AcaoGeraContratoFreteManual implements AcaoRotinaJava {

	ContratoFreteWBean contratoFreteWBean;

	public void doAction(final ContextoAcao ctx) throws Exception {
		
		final Registro[] linhas = ctx.getLinhas();
		
		
		StringBuffer mensagem = new StringBuffer();
		ContratoFreteWBean contratoFrete = new ContratoFreteWBean();
		Conversoes conversoes = new Conversoes();
		// List<CIOT> listCIOT = new ArrayList<CIOT>();

		String arqGerado = "";
		Map<String, String> mapLogCIOT = new HashMap<String, String>();

		String dataAtual = "";

		int i = 0;
		String numCIOT = "";
		
		while (i < linhas.length)  {
			
			numCIOT = linhas[i].getCampo("NUCIOT").toString();
			contratoFrete = new ContratoFreteWBean();

			try {
				mensagem.append("CIOT número " + numCIOT + ".").append("\n");
				arqGerado = contratoFrete.geraArquivo(EnumTipoOperacao.INSERIR_CONTRATO_DE_FRETE.getTipoOperacao().toString(), Long.valueOf(numCIOT));
			} finally {

				dataAtual = conversoes.getDataHoraAtual();

				if (arqGerado.equals("")) {
					mapLogCIOT.put(numCIOT, new String(dataAtual + " => Problema ao gerar o CIOT número " + numCIOT + ", o contrato de frete não será integrado."));
				} else {
					mapLogCIOT.put(numCIOT, new String(dataAtual + " => Arquivo " + arqGerado + " criado com sucesso. Aguardando integração..."));
					Thread.sleep(1000);
				}
			}
			
			i++;

		}

		contratoFrete.atualizaDadosIntegracaoCIOT(mapLogCIOT);
		
		Thread.sleep(3000);
		
		String mensgem = contratoFrete.buscaArquivoRetorno();

		//ctx.setMensagemRetorno(mensagem.toString());
		
		if(!mensagem.equals("")) {
			mensagem.append("Procedimento concluído, verifique o campo mensagem na aba protocolo por favor!");
		}else {
			mensagem.append("Arquivo não encontrado!");
		}
		
		ctx.setMensagemRetorno(mensagem.toString());

	}

}
