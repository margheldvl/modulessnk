package br.com.sankhya.pamcard.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cuckoo.core.ScheduledAction;
import org.cuckoo.core.ScheduledActionContext;

import br.com.sankhya.pamcard.model.CIOT;
import br.com.sankhya.pamcard.model.Conversoes;
import br.com.sankhya.pamcard.model.EnumTipoOperacao;
import br.com.sankhya.pamcard.wbean.ContratoFreteWBean;

public class AcaoAgendadaGeraCiot implements ScheduledAction {

	@Override
	public void onTime(ScheduledActionContext arg0) {

		try {

			geraCIOTs();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void geraCIOTs() throws Exception {

		StringBuffer mensagem = new StringBuffer();
		ContratoFreteWBean contratoFrete = new ContratoFreteWBean();
		Conversoes conversoes = new Conversoes();
		List<CIOT> listCIOT = new ArrayList<CIOT>();

		contratoFrete.buscaArquivoRetorno();

		listCIOT = contratoFrete.pesquisaCiotLiberado("L");
		String arqGerado = "";
		Map<String, String> mapLogCIOT = new HashMap<String, String>();

		String dataAtual = "";

		for (int i = 0; i < listCIOT.size(); i++) {

			try {
				mensagem.append("CIOT número " + listCIOT.get(i).getNumero() + ".").append("\n");
				arqGerado = contratoFrete.geraArquivo(
						EnumTipoOperacao.INSERIR_CONTRATO_DE_FRETE.getTipoOperacao().toString(),
						listCIOT.get(i).getNumero());
			} finally {

				dataAtual = conversoes.getDataHoraAtual();

				if (arqGerado.equals("")) {
					mapLogCIOT.put(listCIOT.get(i).getNumero().toString(),
							new String(dataAtual + " => Problema ao gerar o CIOT número " + listCIOT.get(i).getNumero()
									+ ", o contrato de frete não será integrado."));
				} else {
					mapLogCIOT.put(listCIOT.get(i).getNumero().toString(), new String(
							dataAtual + " => Arquivo " + arqGerado + " criado com sucesso. Aguardando integração..."));
					Thread.sleep(1000);
				}
			}

		}

		contratoFrete.atualizaDadosIntegracaoCIOT(mapLogCIOT);

		Thread.sleep(10000);

		contratoFrete.buscaArquivoRetorno();

	}

}
