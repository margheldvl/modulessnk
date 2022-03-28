package br.com.dvl.wbean;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import br.com.dvl.model.ProgramacaoVisita;
import br.com.dvl.service.ProgramacaoVisitaService;
import br.com.sankhya.extensions.actionbutton.ContextoAcao;

public class ProgramacaoVisitaWBean {

	private static Integer qdeErros;
	private static String msgResult = "";
	private static ProgramacaoVisitaService programacaoVisitaService;
	private static final String ARQUIVO_IMPORTACAO_PROGRAMACAO_VISITAS = "Nova Programacao de Visitas.csv";

	public String insereProgramacaoVisita(ContextoAcao ctx) throws Exception {
		String msg = "";
		Long codUsu = ctx.getUsuarioLogado().longValue();
		String arquivo_log = codUsu + " - " + " Log Nova Programacao de Visitas.csv";
		programacaoVisitaService = new ProgramacaoVisitaService();

		String chaveArquivo = programacaoVisitaService.buscaChave(codUsu, ARQUIVO_IMPORTACAO_PROGRAMACAO_VISITAS,
				arquivo_log);

		if (chaveArquivo.equals("-1")) {
			msg = msg + "Você deve anexar apenas um arquivo para realizar a atualizar a Programação de Visitas.";
		} else if (chaveArquivo.equals("0")) {
			msg = msg + "O arquivo \"" + ARQUIVO_IMPORTACAO_PROGRAMACAO_VISITAS + "\" não foi anexado.";
		} else if (chaveArquivo.equals("S")) {
			msg = msg
					+ "Data do arquivo anexado anterior a data atual, favor anexar novo arquivo para atualizar a Programação de Visitas.";
		} else {

			String dirIntegracao = javax.swing.filechooser.FileSystemView.getFileSystemView().getDefaultDirectory()
					.toString() + "/.sw_file_repository/Sistema/Anexos/TGFPRG";

			String nomeArqSaida = arquivo_log;
			String arquivoEntrada = dirIntegracao + "/" + chaveArquivo;
			String caminhoCompletoArqSaida = dirIntegracao + "/" + nomeArqSaida;

			File dir = new File(dirIntegracao);

			boolean existe = false;

			if (!dir.isDirectory()) {
				msgResult = msgResult + " / Diretório " + "Integracao" + " não encontrado na pasta Documentos!";
			} else {
				existe = true;
			}

			if (existe) {

				File file = new File(caminhoCompletoArqSaida);

				file.delete();

				if (!file.isFile()) {

					file = new File(caminhoCompletoArqSaida);

					List<String> listArqsaida = new ArrayList<String>();
					qdeErros = 0;
					listArqsaida = GeraScriptSqlAtuaTGFVIS(arquivoEntrada);

					if (listArqsaida != null) {
						criarArq(caminhoCompletoArqSaida, listArqsaida);
						programacaoVisitaService.insereLog(codUsu, ARQUIVO_IMPORTACAO_PROGRAMACAO_VISITAS, arquivo_log);
					}

				} else {
					msg = "Não foi possível excluir o arquivo " + nomeArqSaida + " na pasta Documentos.";
				}

			}
		}

		return msgResult + "\n" + msg;

	}

	private static void criarArq(String caminhoCompletoArqSaida, List<String> listArq) {

		FileWriter arq = null;
		PrintWriter gravarArq = null;

		try {

			arq = new FileWriter(caminhoCompletoArqSaida);

			for (int i = 0; i < listArq.size(); i++) {
				gravarArq = new PrintWriter(arq);
				gravarArq.printf(listArq.get(i) + "%n");
			}
			arq.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static List<String> GeraScriptSqlAtuaTGFVIS(String caminhoCompeltoArqEntrada) {

		List<String> listArqsaida = new ArrayList<String>();
		List<ProgramacaoVisita> listaProgramacaoVisita = new ArrayList<ProgramacaoVisita>();

		String arquivo = caminhoCompeltoArqEntrada;

		Integer numberLine = 0;

		String[] list = null;
		String colLayoutArq = "CODPARC,TIPO,VALOR,SEQVISITA,CODVEND,CODREG";
		String colArq = colLayoutArq + ",";
		Integer qdeCol = 6;
		msgResult = "";
		Map<String, Integer> mapImport = new HashMap<String, Integer>();

		try (Scanner scanner = new Scanner(new File(arquivo));) {
			while (scanner.hasNextLine() && list == null) {
				numberLine++;
				String line = scanner.nextLine();

				list = line.split(";");

				if (mapImport.get(0) == null) {

					for (int i = 0; i < list.length; i++) {

						if (!list[i].replace(" ", "").equals("")) {

							if (list[i].equals("CODPARC")) {
								mapImport.put(new String(list[i]), i);
								colArq = colArq.replace(list[i] + ",", "");
								qdeCol--;
							} else if (list[i].equals("TIPO")) {
								mapImport.put(new String(list[i]), i);
								colArq = colArq.replace(list[i] + ",", "");
								qdeCol--;
							} else if (list[i].equals("VALOR")) {
								mapImport.put(new String(list[i]), i);
								colArq = colArq.replace(list[i] + ",", "");
								qdeCol--;
							} else if (list[i].equals("SEQVISITA")) {
								mapImport.put(new String(list[i]), i);
								colArq = colArq.replace(list[i] + ",", "");
								qdeCol--;
							} else if (list[i].equals("CODVEND")) {
								mapImport.put(new String(list[i]), i);
								colArq = colArq.replace(list[i] + ",", "");
								qdeCol--;
							} else if (list[i].equals("CODREG")) {
								mapImport.put(new String(list[i]), i);
								colArq = colArq.replace(list[i] + ",", "");
								qdeCol--;
							}
						}

					}
				}

			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		if (qdeCol > 0) {
			msgResult = qdeCol == 1 ? "Coluna " + colArq + " não encontrada!"
					: "Colunas " + colArq + " não encontradas!";
		} else if (mapImport.size() > 6) {
			msgResult = msgResult
					+ "Existem colunas no arquivo que não podem ser importadas. As colunas do layout atual são: "
					+ colLayoutArq + ".";
		} else {

			list = null;
			Integer linha = 0;
			qdeErros = 0;
			numberLine = 0;

			String codParc;
			String tipo;
			String valor;
			String codVend;
			String codReg;
			String seqVisita;
			boolean incluiuScript = true;

			try (Scanner scanner = new Scanner(new File(arquivo));) {
				while (scanner.hasNextLine()) {

					numberLine++;
					String line = scanner.nextLine();

					codParc = "";
					tipo = "";
					valor = "";
					codVend = "";
					codReg = "";
					seqVisita = "";
					incluiuScript = true;
					String msg = "";

					list = line.split(";");

					if (linha > 0) {

						codParc = list[mapImport.get("CODPARC")];
						tipo = list[mapImport.get("TIPO")];
						valor = list[mapImport.get("VALOR")];
						codVend = list[mapImport.get("CODVEND")];
						codReg = list[mapImport.get("CODREG")];
						seqVisita = list[mapImport.get("SEQVISITA")];

						ProgramacaoVisita programacaoVisita = new ProgramacaoVisita();
						incluiuScript = true;
						try {
							programacaoVisita.setCodparc(Long.valueOf(codParc));
							programacaoVisita.setValor(Long.valueOf(valor));
							programacaoVisita.setCodvend(Long.valueOf(codVend));
							programacaoVisita.setCodreg(Long.valueOf(codReg));
							programacaoVisita.setSeqvisita(Long.valueOf(seqVisita));
						} catch (Exception e) {
							msg = " => [ Informe apenas valores inteiros para os campos (TIPOVALOR / CODVEND / CODREG / SEQVISITA) ] ";
							incluiuScript = false;
						}

						if (tipo.length() > 0) {
							programacaoVisita.setTipo(tipo);
						} else {
							msg = msg + " => [ Verifique o campo CODPARC ] ";
							incluiuScript = false;
						}

						if (incluiuScript) {

							listaProgramacaoVisita.add(programacaoVisita);

						} else {
							listArqsaida.add("Atualizado;" + line + ";" + msg);
							qdeErros++;
						}

					}
					linha++;

				}

				if (listaProgramacaoVisita != null && qdeErros == 0) {

					ProgramacaoVisitaService programacaoVisitaService = new ProgramacaoVisitaService();

					boolean excluiuTodos = programacaoVisitaService
							.ExcluirProgramVisNativeSQLWithBatch(listaProgramacaoVisita);

					if (excluiuTodos) {

						listArqsaida = programacaoVisitaService
								.IncluirProgramVisNativeSQLWithBatch(listaProgramacaoVisita);
						if (listArqsaida != null) {
							msgResult = msgResult
									+ "Não foi possível inserir a Programação de Visitas para todos os parceiros. "
									+ "\n";
						} else {
							msgResult = msgResult + "Procedimento concluído!";
						}
					} else {
						msgResult = msgResult
								+ "Não foi possível realizar a exclusão de todos os parceiros para inserir a nova Programação de Visitas. "
								+ "\n";
					}

				} else {
					msgResult = msgResult
							+ "O procedimento será cancelado. Verifique a coluna \" Mensagem \" do arquivo \"Log - Atualizacao de Programacao de Visitas.csv\" na pasta documentos e realize as correções necessárias. "
							+ "\n";
				}

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

		}

		return listArqsaida;
	}

}
