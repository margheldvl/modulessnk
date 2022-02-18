package br.com.sankhya.pamcard.action;

import java.util.HashMap;
import java.util.Map;

import br.com.sankhya.extensions.actionbutton.AcaoRotinaJava;
import br.com.sankhya.extensions.actionbutton.ContextoAcao;
import br.com.sankhya.extensions.actionbutton.Registro;
import br.com.sankhya.pamcard.model.CIOT;
import br.com.sankhya.pamcard.model.Conversoes;
import br.com.sankhya.pamcard.model.EnumStatusParcela;
import br.com.sankhya.pamcard.model.EnumTipoOperacao;
import br.com.sankhya.pamcard.wbean.ContratoFreteWBean;

public class AcaoConsultaContratoFretePAMCARD implements AcaoRotinaJava {

	ContratoFreteWBean contratoFreteWBean;

	public void doAction(final ContextoAcao ctx) throws Exception {

		final Registro[] linhas = ctx.getLinhas();

		int i = 0;
		String numCIOT = "";

		StringBuffer mensagem = new StringBuffer();

		String arqGerado = "";
		ContratoFreteWBean contratoFrete = new ContratoFreteWBean();
		Map<String, String> mapLogCIOT = new HashMap<String, String>();

		while (i < linhas.length) {
			numCIOT = linhas[i].getCampo("NUCIOT").toString();
			contratoFrete = new ContratoFreteWBean();

			try {
				arqGerado = contratoFrete.geraArquivoConsultaFrete(EnumTipoOperacao.CONSULTAR_CONTRATO_DE_FRETE.getTipoOperacao().toString(), Long.valueOf(numCIOT));
				Thread.sleep(1000);
			} finally {

				if (arqGerado.equals("")) {
					mapLogCIOT.put(numCIOT.toString(), new String("Problema ao gerar o arquivo para consulta do CIOT número " + numCIOT + ", o contrato de frete não será integrado."));
				} else {
					mapLogCIOT.put(numCIOT.toString(), new String(arqGerado));
					Thread.sleep(1000);
				}

			}

			i++;

		}

		Thread.sleep(3000);

		for (String key : mapLogCIOT.keySet()) {

			String arquivo = mapLogCIOT.get(key);

			Long nuCiot = Long.valueOf(key);

			CIOT ciot = new CIOT();
			ciot = contratoFrete.buscaArquivoRetornoPorArquivoGerado(arquivo);
			Conversoes conversoes = new Conversoes();
			contratoFrete = new ContratoFreteWBean();
			mensagem.append("CIOT: " + nuCiot).append("\n");
			mensagem.append("Mensagem descrição: " + ciot.getMsgIntegra()).append("\n");

			if (ciot.getId() != null) {
				mensagem.append("Viagem antt ciot geração data: " + conversoes.converteDataDDMMYYYY(ciot.getDataGeracao())).append("\n");
				mensagem.append("Viagem antt ciot número: " + ciot.getNumero()).append("\n");
				mensagem.append("viagem antt protocolo: " + ciot.getProtocolo()).append("\n");
				mensagem.append("Viagem dígito: " + ciot.getDigito()).append("\n");
				mensagem.append("Viagem id: " + ciot.getId()).append("\n");
				mensagem.append("Viagem status: " + EnumStatusParcela.getEnumStatusParcela(ciot.getStatusParcela())).append("\n");
				mensagem.append(" ").append("\n");
				mensagem.append(" ").append("\n");
			}

		}

		ctx.setMensagemRetorno(mensagem.toString());

	}

}
