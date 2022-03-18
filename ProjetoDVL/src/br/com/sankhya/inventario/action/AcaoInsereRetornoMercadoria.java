package br.com.sankhya.inventario.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.com.sankhya.extensions.actionbutton.AcaoRotinaJava;
import br.com.sankhya.extensions.actionbutton.ContextoAcao;
import br.com.sankhya.extensions.actionbutton.Registro;
import br.com.sankhya.inventario.service.InventarioService;

public class AcaoInsereRetornoMercadoria implements AcaoRotinaJava {

	private InventarioService inventarioService;

	public AcaoInsereRetornoMercadoria() {
		inventarioService = new InventarioService();
	}

	public void doAction(final ContextoAcao ctx) throws Exception {

		final Registro[] linhas = ctx.getLinhas();

		int i = 0;
		String ordemCarga = "";
		String paramListaNumNota;

		StringBuffer mensagem = new StringBuffer();

		ordemCarga = ctx.getParam("ORDEMCARGA").toString();
		paramListaNumNota = ctx.getParam("LISTNUMNOTAS").toString();

		String codUser = "";

		while (i < linhas.length) {
			codUser = linhas[i].getCampo("CODUSU").toString();
			i++;
			break;
		}

		String[] strSplit = paramListaNumNota.split(",");

		ArrayList<String> strList = new ArrayList<String>(Arrays.asList(strSplit));

		List<String> listaNotas = new ArrayList<String>();

		for (String s : strList) {
			listaNotas.add(s);
		}

		Integer qdeNotasOrdensCargaEnc = 0;
		List<String> listOrdemCargaEncont = null;
		qdeNotasOrdensCargaEnc = 0;

		listOrdemCargaEncont = inventarioService.achouApenasUmaOrdemCarga(listaNotas, ordemCarga);

		qdeNotasOrdensCargaEnc = listOrdemCargaEncont != null ? listOrdemCargaEncont.size() : 0;

		if (qdeNotasOrdensCargaEnc > 0) {

			boolean achouApenasUmaOrdemCarga = qdeNotasOrdensCargaEnc <= 1 ? true : false;

			if (achouApenasUmaOrdemCarga) {
				List<String> listNotasNaoEnc = null;
				listNotasNaoEnc = inventarioService.notasNaoEncontradas(listaNotas, ordemCarga);
				if (listNotasNaoEnc.size() == 0) {

					String retorno = inventarioService.insereRetornoMercadorias(listaNotas, ordemCarga, codUser);

					if (retorno != null) {
						mensagem.append("Mensagem: " + retorno).append("\n");
						mensagem.append("O Procedimento será cancelado!").append("\n");
					} else {
						mensagem.append("Procedimento concluído!").append("\n");
					}
				} else {
					mensagem.append("Notas não localizadas, favor conferir.").append("\n");
					for (int j = 0; j < listNotasNaoEnc.size(); j++) {
						mensagem.append("Nota Número: " + listNotasNaoEnc.get(j)).append("\n");
					}

				}

			} else {

				mensagem.append("Foram encontradas mais de uma ordem de carga para as notas informadas:").append("\n");

				for (int j = 0; j < listOrdemCargaEncont.size(); j++) {
					mensagem.append(listOrdemCargaEncont.get(j)).append("\n");
				}

			}

		} else {
			mensagem.append("Nenhuma nota informada está vinculada na ordem de carga " + ordemCarga + ".");
		}

		ctx.setMensagemRetorno(mensagem.toString());

	}

}
