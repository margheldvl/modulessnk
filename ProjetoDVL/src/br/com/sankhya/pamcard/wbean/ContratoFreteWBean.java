package br.com.sankhya.pamcard.wbean;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import br.com.sankhya.pamcard.model.CIOT;
import br.com.sankhya.pamcard.model.Cidade;
import br.com.sankhya.pamcard.model.Contratante;
import br.com.sankhya.pamcard.model.Contrato;
import br.com.sankhya.pamcard.model.Conversoes;
import br.com.sankhya.pamcard.model.Documento;
import br.com.sankhya.pamcard.model.DocumentoContratante;
import br.com.sankhya.pamcard.model.DocumentoPessoFiscal;
import br.com.sankhya.pamcard.model.DocumentoViagem;
import br.com.sankhya.pamcard.model.Endereco;
import br.com.sankhya.pamcard.model.EnderecoPessoaFiscal;
import br.com.sankhya.pamcard.model.Favorecido;
import br.com.sankhya.pamcard.model.Frete;
import br.com.sankhya.pamcard.model.Item;
import br.com.sankhya.pamcard.model.Parcela;
import br.com.sankhya.pamcard.model.Paths;
import br.com.sankhya.pamcard.model.PessoaFiscal;
import br.com.sankhya.pamcard.model.Telefone;
import br.com.sankhya.pamcard.model.Veiculo;
import br.com.sankhya.pamcard.model.Viagem;
import br.com.sankhya.pamcard.service.ServicePAMCARD;

public class ContratoFreteWBean {

	ServicePAMCARD servicePAMCARD;

	@SuppressWarnings("unused")
	private static Paths paths = new Paths();
	String caminhoCompletoArq = "";
	String caminhoCompletoArqLog = "";

	public ContratoFreteWBean() {
		servicePAMCARD = new ServicePAMCARD();
	}

	public String geraArquivoConsultaFrete(String tipoOperacao, Long numCiot) throws Exception {

		boolean arq_gerado = false;

		String nomeArqExt = "";

		Viagem viagem = new Viagem();
		viagem = carregaViagem(numCiot);
		
		StringBuffer PAMCARD = new StringBuffer();

		try {

			PAMCARD.append("transacional.operacao=" + tipoOperacao).append("\n");
			PAMCARD.append("viagem.contratante.documento.numero=" + viagem.getContratante().getDocumentoContratante().getNumero()).append("\n");
			PAMCARD.append("viagem.id.cliente=" + viagem.getIdCliente()).append("\n");
			
			FileWriter arquivo = null;

			Date agora = new Date();

			String pattern = "ddMMyyyyHHmm";
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
			String dataFormatada = simpleDateFormat.format(agora);

			nomeArqExt = "PAMCARD_" + numCiot + "_" + dataFormatada + ".txt";
			caminhoCompletoArq = Paths.getDIRETORIO_MGEWEB_CIOT_IN() + nomeArqExt;
			
			try {

				String linha = "";
				arquivo = new FileWriter(new File(caminhoCompletoArq));
				Scanner scan = new Scanner(PAMCARD.toString());

				while (scan.hasNextLine()) {
					linha = scan.nextLine() + "\r\n";
					arquivo.write(linha);
				}
				arquivo.close();

				arq_gerado = true;

			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}

		} catch (Exception e) {

			e.printStackTrace();

		}

		String retorno = nomeArqExt;

		if (!arq_gerado) {
			retorno = "";
		}

		return retorno;

	}

	
	public String geraArquivo(String tipoOperacao, Long numCiot) throws Exception {

		boolean arq_gerado = false;

		String nomeArqExt = "";

		Viagem viagem = new Viagem();
		viagem = carregaViagem(numCiot);
		Conversoes conversoes = new Conversoes();

		StringBuffer PAMCARD = new StringBuffer();

		try {

			PAMCARD.append("transacional.operacao=" + tipoOperacao).append("\n");
			PAMCARD.append("viagem.contratante.documento.numero=" + viagem.getContratante().getDocumentoContratante().getNumero()).append("\n");
			PAMCARD.append("viagem.id.cliente=" + viagem.getIdCliente()).append("\n");
			PAMCARD.append("viagem.favorecido.qtde=" + viagem.getFavorecidoQtde()).append("\n");
			PAMCARD.append("viagem.contrato.numero=" + viagem.getContrato().getNumero()).append("\n");

            List<Favorecido> listFavGeracao = new ArrayList<Favorecido>();
			listFavGeracao = viagem.getListFavorecido();

			viagem.setFavorecidoQtde(listFavGeracao.size());
			
			if (listFavGeracao != null) {

				Integer numFav = 0;

				for (int i = 0; i < listFavGeracao.size(); i++) {

					Favorecido fav = new Favorecido();
					fav = listFavGeracao.get(i);

					List<Documento> listDocumento = new ArrayList<Documento>();

					listDocumento = fav.getListDocumento();

					Integer tipoFavorecido = fav.getTipo();

					// CONTRATATO
					if (tipoFavorecido == 1) {

						numFav = 1;

						PAMCARD.append("viagem.favorecido" + numFav + ".tipo=" + fav.getTipo()).append("\n");
						PAMCARD.append("viagem.favorecido" + numFav + ".documento.qtde=" + listDocumento.size() /* fav.getDocumentoQtde() */).append("\n");

						Integer numDoc = 0;

						for (int j = 0; j < listDocumento.size(); j++) {

							Documento doc = new Documento();
							doc = listDocumento.get(j);

							numDoc++;

							PAMCARD.append("viagem.favorecido" + numFav + ".documento" + numDoc + ".tipo=" + doc.getTipo()).append("\n");
							PAMCARD.append("viagem.favorecido" + numFav + ".documento" + numDoc + ".numero=" + doc.getNumero()).append("\n");

							if (doc.getTipo() == 3) {

								PAMCARD.append("viagem.favorecido" + numFav + ".documento" + numDoc + ".uf=" + doc.getUf()).append("\n");

								PAMCARD.append("viagem.favorecido" + numFav + ".documento" + numDoc + ".emissor.id=" + doc.getEmissorId()).append("\n");

								PAMCARD.append("viagem.favorecido" + numFav + ".documento" + numDoc + ".emissao.data=" + conversoes.converteDataDDMMYYYY(doc.getDataEmissao())).append("\n");
							}

						}

						PAMCARD.append("viagem.favorecido" + numFav + ".nome=" + fav.getNome()).append("\n");
						PAMCARD.append("viagem.favorecido" + numFav + ".data.nascimento=" + conversoes.converteDataDDMMYYYY(fav.getDataNascimento())).append("\n");
						PAMCARD.append("viagem.favorecido" + numFav + ".nacionalidade.id=" + fav.getNacionalidade_id()).append("\n");
						PAMCARD.append("viagem.favorecido" + numFav + ".naturalidade.ibge=" + fav.getNaturalidadeCidadeIbge().getIbge()).append("\n");
						PAMCARD.append("viagem.favorecido" + numFav + ".sexo=" + fav.getSexo()).append("\n");

						Endereco ender = new Endereco();
						ender = fav.getEndereco();

						PAMCARD.append("viagem.favorecido" + numFav + ".endereco.logradouro=" + ender.getLogradouro()).append("\n");
						PAMCARD.append("viagem.favorecido" + numFav + ".endereco.numero=" + ender.getNumero()).append("\n");
						PAMCARD.append("viagem.favorecido" + numFav + ".endereco.bairro=" + ender.getBairro()).append("\n");
						PAMCARD.append("viagem.favorecido" + numFav + ".endereco.cidade.ibge=" + ender.getCidade().getIbge()).append("\n");
						PAMCARD.append("viagem.favorecido" + numFav + ".endereco.cep=" + ender.getCep()).append("\n");
						PAMCARD.append("viagem.favorecido" + numFav + ".endereco.propriedade.tipo.id=" + ender.getTipoPropriedade()).append("\n");
						PAMCARD.append("viagem.favorecido" + numFav + ".endereco.reside.desde=" + conversoes.converteDataMMYYYY(ender.getResideDesde())).append("\n");

						Telefone telefone = new Telefone();
						telefone = fav.getTelefone();

						PAMCARD.append("viagem.favorecido" + numFav + ".telefone.ddd=" + telefone.getTelefoneDDD()).append("\n");
						PAMCARD.append("viagem.favorecido" + numFav + ".telefone.numero=" + telefone.getTelefoneNumero()).append("\n");

						PAMCARD.append("viagem.favorecido" + numFav + ".meio.pagamento=" + fav.getMeioPagamento()).append("\n");
						PAMCARD.append("viagem.favorecido" + numFav + ".cartao.numero=" + fav.getCartao()).append("\n");

						if (fav.getEmpresa().getCnpj() != null) {
							PAMCARD.append("viagem.favorecido" + numFav + ".empresa.cnpj=" + fav.getEmpresa().getCnpj()).append("\n");
							PAMCARD.append("viagem.favorecido" + numFav + ".empresa.rntrc=" + fav.getEmpresa().getEmpresa_rntrc()).append("\n");
						}

					}

					// MOTORISTA
					if (tipoFavorecido == 3) {

						numFav = 2;

						PAMCARD.append("viagem.favorecido" + numFav + ".tipo=" + fav.getTipo()).append("\n");
						PAMCARD.append("viagem.favorecido" + numFav + ".documento.qtde=" + listDocumento.size() /* fav.getDocumentoQtde() */).append("\n");

						Integer numDoc = 0;

						for (int j = 0; j < listDocumento.size(); j++) {

							Documento doc = new Documento();
							doc = listDocumento.get(j);

							numDoc++;

							PAMCARD.append("viagem.favorecido" + numFav + ".documento" + numDoc + ".tipo=" + doc.getTipo()).append("\n");
							PAMCARD.append("viagem.favorecido" + numFav + ".documento" + numDoc + ".numero=" + doc.getNumero()).append("\n");

							if (doc.getTipo() == 3) {

								PAMCARD.append("viagem.favorecido" + numFav + ".documento" + numDoc + ".uf=" + doc.getUf()).append("\n");

								PAMCARD.append("viagem.favorecido" + numFav + ".documento" + numDoc + ".emissor.id=" + doc.getEmissorId()).append("\n");

								PAMCARD.append("viagem.favorecido" + numFav + ".documento" + numDoc + ".emissao.data=" + conversoes.converteDataDDMMYYYY(doc.getDataEmissao())).append("\n");
							}

						}

						PAMCARD.append("viagem.favorecido" + numFav + ".nome=" + fav.getNome()).append("\n");
						PAMCARD.append("viagem.favorecido" + numFav + ".data.nascimento=" + conversoes.converteDataDDMMYYYY(fav.getDataNascimento())).append("\n");
						PAMCARD.append("viagem.favorecido" + numFav + ".nacionalidade.id=" + fav.getNacionalidade_id()).append("\n");
						PAMCARD.append("viagem.favorecido" + numFav + ".naturalidade.ibge=" + fav.getNaturalidadeCidadeIbge().getIbge()).append("\n");
						PAMCARD.append("viagem.favorecido" + numFav + ".sexo=" + fav.getSexo()).append("\n");

						Endereco ender = new Endereco();
						ender = fav.getEndereco();

						PAMCARD.append("viagem.favorecido" + numFav + ".endereco.logradouro=" + ender.getLogradouro()).append("\n");
						PAMCARD.append("viagem.favorecido" + numFav + ".endereco.numero=" + ender.getNumero()).append("\n");
						PAMCARD.append("viagem.favorecido" + numFav + ".endereco.bairro=" + ender.getBairro()).append("\n");
						PAMCARD.append("viagem.favorecido" + numFav + ".endereco.cidade.ibge=" + ender.getCidade().getIbge()).append("\n");
						PAMCARD.append("viagem.favorecido" + numFav + ".endereco.cep=" + ender.getCep()).append("\n");
						PAMCARD.append("viagem.favorecido" + numFav + ".endereco.propriedade.tipo.id=" + ender.getTipoPropriedade()).append("\n");
						PAMCARD.append("viagem.favorecido" + numFav + ".endereco.reside.desde=" + conversoes.converteDataMMYYYY(ender.getResideDesde())).append("\n");

						Telefone telefone = new Telefone();
						telefone = fav.getTelefone();

						PAMCARD.append("viagem.favorecido" + numFav + ".telefone.ddd=" + telefone.getTelefoneDDD()).append("\n");
						PAMCARD.append("viagem.favorecido" + numFav + ".telefone.numero=" + String.valueOf(telefone.getTelefoneNumero())).append("\n");

						if (fav.getEmpresa().getCnpj() != null) {
							PAMCARD.append("viagem.favorecido" + numFav + ".empresa.cnpj=" + fav.getEmpresa().getCnpj()).append("\n");
							PAMCARD.append("viagem.favorecido" + numFav + ".empresa.rntrc=" + fav.getEmpresa().getEmpresa_rntrc()).append("\n");
						}

						PAMCARD.append("viagem.favorecido" + numFav + ".meio.pagamento=" + fav.getMeioPagamento()).append("\n");
						PAMCARD.append("viagem.favorecido" + numFav + ".cartao.numero=" + fav.getCartao()).append("\n");

					}

				}

			}

           PAMCARD.append("viagem.veiculo.qtde=" + viagem.getVeiculoQtde()).append("\n");

			List<Veiculo> listVeic = new ArrayList<Veiculo>();
			listVeic = viagem.getListVeiculo();

			String veiCategoria = "";

			if (listVeic != null) {

				Integer numVeic = 0;

				veiCategoria = listVeic.get(0).getCategoria();

				for (int i = 0; i < listVeic.size(); i++) {

					numVeic++;

					Veiculo veic = new Veiculo();
					veic = listVeic.get(i);

					PAMCARD.append("viagem.veiculo" + numVeic + ".placa=" + veic.getPlaca()).append("\n");
					PAMCARD.append("viagem.veiculo" + numVeic + ".rntrc=" + veic.getRntrc()).append("\n");

				}

			}

			PAMCARD.append("viagem.veiculo.categoria=" + veiCategoria).append("\n");
			PAMCARD.append("viagem.data.partida=" + conversoes.converteDataDDMMYYYY(viagem.getDataPartida())).append("\n");
			PAMCARD.append("viagem.data.termino=" + conversoes.converteDataDDMMYYYY(viagem.getDataTermino())).append("\n");
			PAMCARD.append("viagem.origem.cidade.ibge=" + viagem.getOrigemCidade().getIbge()).append("\n");
			PAMCARD.append("viagem.destino.cidade.ibge=" + viagem.getDestinoCidade().getIbge()).append("\n");
			PAMCARD.append("viagem.carga.natureza=" + String.valueOf(viagem.getCarga().getNatureza())).append("\n");
			
			String peso = Math.round(viagem.getCarga().getPeso()) != viagem.getCarga().getPeso() ? conversoes.formatDouble2CasasPonto(viagem.getCarga().getPeso()) : String.valueOf(viagem.getCarga().getPeso().intValue());
			PAMCARD.append("viagem.carga.peso=" + peso).append("\n");
			
			// PAMCARD.append("viagem.documento.qtde=" + viagem.getDocumentoQtde()).append("\n");
			
			List<PessoaFiscal> listPFiscal = new ArrayList<PessoaFiscal>();
			listPFiscal = viagem.getListPessoaFiscal();
			
			List<DocumentoViagem> listDocViagem = new ArrayList<DocumentoViagem>();
			listDocViagem = viagem.getListDocumentoViagem();
			
			
			
			Integer numDocViag = 0;

			if (listDocViagem != null) {

				PAMCARD.append("viagem.documento.qtde=" + listDocViagem.size()).append("\n");

				for (int i = 0; i < listDocViagem.size(); i++) {

					numDocViag++;

					//if (numDocViag == 1) {

						DocumentoViagem doc = new DocumentoViagem();
						doc = listDocViagem.get(i);

						PAMCARD.append("viagem.documento" + numDocViag + ".tipo=" + doc.getTipo()).append("\n");
						PAMCARD.append("viagem.documento" + numDocViag + ".numero=" + doc.getNumero()).append("\n");
						//PAMCARD.append("viagem.documento" + numDocViag + ".pessoafiscal.qtde=" + listDocViagem.size() /* doc.getQuantidade() */).append("\n");
						PAMCARD.append("viagem.documento" + numDocViag + ".pessoafiscal.qtde=" + listPFiscal.size()).append("\n");
					//}
						
				

						

								Integer numPFiscal = 0;
								//Integer numDoc = 1;

								for (int k = 0; k < listPFiscal.size(); k++) {

									PessoaFiscal pessoaFiscal = new PessoaFiscal();
									pessoaFiscal = listPFiscal.get(k);

									numPFiscal++;

									DocumentoPessoFiscal documentoPessoFiscal = new DocumentoPessoFiscal();
									documentoPessoFiscal = pessoaFiscal.getDocumentoPessoFiscal();

									EnderecoPessoaFiscal enderecoPessoaFiscal = new EnderecoPessoaFiscal();
									enderecoPessoaFiscal = pessoaFiscal.getEnderecoPessoaFiscal();

									Cidade cidade = new Cidade();
									cidade = enderecoPessoaFiscal.getCidade();
									PAMCARD.append("viagem.documento" + numDocViag + ".pessoafiscal" + numPFiscal + ".tipo=" + pessoaFiscal.getTipo()).append("\n");
									PAMCARD.append("viagem.documento" + numDocViag + ".pessoafiscal" + numPFiscal + ".documento.tipo=" + documentoPessoFiscal.getTipo()).append("\n");
									PAMCARD.append("viagem.documento" + numDocViag + ".pessoafiscal" + numPFiscal + ".documento.numero=" + documentoPessoFiscal.getNumero()).append("\n");
									PAMCARD.append("viagem.documento" + numDocViag + ".pessoafiscal" + numPFiscal + ".nome=" + pessoaFiscal.getNome()).append("\n");
									PAMCARD.append("viagem.documento" + numDocViag + ".pessoafiscal" + numPFiscal + ".endereco.logradouro=" + enderecoPessoaFiscal.getLogradouro()).append("\n");
									PAMCARD.append("viagem.documento" + numDocViag + ".pessoafiscal" + numPFiscal + ".endereco.numero=" + enderecoPessoaFiscal.getNumero()).append("\n");
									PAMCARD.append("viagem.documento" + numDocViag + ".pessoafiscal" + numPFiscal + ".endereco.bairro=" + enderecoPessoaFiscal.getBairro()).append("\n");
									PAMCARD.append("viagem.documento" + numDocViag + ".pessoafiscal" + numPFiscal + ".endereco.cidade.ibge=" + cidade.getIbge()).append("\n");
									PAMCARD.append("viagem.documento" + numDocViag + ".pessoafiscal" + numPFiscal + ".endereco.cep=" + enderecoPessoaFiscal.getCep()).append("\n");

								}

							
					
						
						
				}

			}
			
			//PAMCARD.append("viagem.documento" + numDocViag + ".pessoafiscal.qtde=" + listPFiscal.size() /* doc.getQuantidade() */).append("\n");

//			List<PessoaFiscal> listPFiscal = new ArrayList<PessoaFiscal>();
//			listPFiscal = viagem.getListPessoaFiscal();

//			try {
//
//				if (listPFiscal != null) {
//
//					Integer numPFiscal = 0;
//					Integer numDoc = 1;
//
//					for (int i = 0; i < listPFiscal.size(); i++) {
//
//						PessoaFiscal pessoaFiscal = new PessoaFiscal();
//						pessoaFiscal = listPFiscal.get(i);
//
//						numPFiscal++;
//
//						DocumentoPessoFiscal documentoPessoFiscal = new DocumentoPessoFiscal();
//						documentoPessoFiscal = pessoaFiscal.getDocumentoPessoFiscal();
//
//						EnderecoPessoaFiscal enderecoPessoaFiscal = new EnderecoPessoaFiscal();
//						enderecoPessoaFiscal = pessoaFiscal.getEnderecoPessoaFiscal();
//
//						Cidade cidade = new Cidade();
//						cidade = enderecoPessoaFiscal.getCidade();
//						PAMCARD.append("viagem.documento" + numDoc + ".pessoafiscal" + numPFiscal + ".tipo=" + pessoaFiscal.getTipo()).append("\n");
//						PAMCARD.append("viagem.documento" + numDoc + ".pessoafiscal" + numPFiscal + ".documento.tipo=" + documentoPessoFiscal.getTipo()).append("\n");
//						PAMCARD.append("viagem.documento" + numDoc + ".pessoafiscal" + numPFiscal + ".documento.numero=" + documentoPessoFiscal.getNumero()).append("\n");
//						PAMCARD.append("viagem.documento" + numDoc + ".pessoafiscal" + numPFiscal + ".nome=" + pessoaFiscal.getNome()).append("\n");
//						PAMCARD.append("viagem.documento" + numDoc + ".pessoafiscal" + numPFiscal + ".endereco.logradouro=" + enderecoPessoaFiscal.getLogradouro()).append("\n");
//						PAMCARD.append("viagem.documento" + numDoc + ".pessoafiscal" + numPFiscal + ".endereco.numero=" + enderecoPessoaFiscal.getNumero()).append("\n");
//						PAMCARD.append("viagem.documento" + numDoc + ".pessoafiscal" + numPFiscal + ".endereco.bairro=" + enderecoPessoaFiscal.getBairro()).append("\n");
//						PAMCARD.append("viagem.documento" + numDoc + ".pessoafiscal" + numPFiscal + ".endereco.cidade.ibge=" + cidade.getIbge()).append("\n");
//						PAMCARD.append("viagem.documento" + numDoc + ".pessoafiscal" + numPFiscal + ".endereco.cep=" + enderecoPessoaFiscal.getCep()).append("\n");
//
//					}
//
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
			
			List<Parcela> listParc = new ArrayList<Parcela>();
			listParc = viagem.getListParcela();

			if (listParc != null) {

				PAMCARD.append("viagem.parcela.qtde=" + listParc.size()).append("\n");
				Integer numParc = 0;

				for (int i = 0; i < listParc.size(); i++) {

					numParc++;

					Parcela parcela = new Parcela();
					parcela = listParc.get(i);

					PAMCARD.append("viagem.parcela1.efetivacao.tipo=" + parcela.getEfetivacaoTipo()).append("\n");
					PAMCARD.append("viagem.parcela1.valor=" + parcela.getValor()).append("\n");
					PAMCARD.append("viagem.parcela1.subtipo=" + parcela.getSubTipo()).append("\n");
					PAMCARD.append("viagem.parcela1.status.id=" + parcela.getStatusId()).append("\n");
					PAMCARD.append("viagem.parcela1.data=" + conversoes.converteDataDDMMYYYY(parcela.getData())).append("\n");
					PAMCARD.append("viagem.parcela1.favorecido.tipo.id=" + parcela.getFavorecidoTipo()).append("\n");
					PAMCARD.append("viagem.parcela1.numero.cliente=" + parcela.getNumeroCliente()).append("\n");
				}

			}
			
			Frete fret = new Frete();
			fret = viagem.getFrete();

			List<Item> listItem = new ArrayList<Item>();
			listItem = fret.getListItem();

			Integer qdeItens = listItem.size();

			Double valorBruto = 0d;

			for (int i = 0; i < listItem.size(); i++) {
				valorBruto += listItem.get(i).getValor();
			}

			PAMCARD.append("viagem.frete.valor.bruto=" + valorBruto).append("\n");
			PAMCARD.append("viagem.frete.item.qtde=" + qdeItens).append("\n");

			Integer numItem = 0;

			for (int i = 0; i < listItem.size(); i++) {

				numItem++;

				Item ite = new Item();
				ite = listItem.get(i);
				PAMCARD.append("viagem.frete.item" + numItem + ".tipo=" + ite.getTipo()).append("\n");
				PAMCARD.append("viagem.frete.item" + numItem + ".valor=" + conversoes.formatDouble2CasasPonto(ite.getValor())).append("\n");

			}

            FileWriter arquivo = null;
            FileWriter arquivoLog = null;

			Date agora = new Date();

			String pattern = "ddMMyyyyHHmm";
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
			String dataFormatada = simpleDateFormat.format(agora);

			nomeArqExt = "PAMCARD_" + numCiot + "_" + dataFormatada + ".txt";
			caminhoCompletoArq = Paths.getDIRETORIO_MGEWEB_CIOT_IN() + nomeArqExt;
			caminhoCompletoArqLog = Paths.getDIRETORIO_MGEWEB_CIOT_OUT() + "Ultimo ciot gerado.txt" ;

			try {

				String linha = "";
				arquivo = new FileWriter(new File(caminhoCompletoArq));
				arquivoLog = new FileWriter(new File(caminhoCompletoArqLog));
				Scanner scan = new Scanner(PAMCARD.toString());

				while (scan.hasNextLine()) {
					linha = scan.nextLine() + "\r\n";
					arquivo.write(linha);
					arquivoLog.write(linha);
				}
				
				arquivo.close();
				arquivoLog.close();

				arq_gerado = true;

			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}

		} catch (Exception e) {

			e.printStackTrace();

		}

		String retorno = nomeArqExt;

		if (!arq_gerado) {
			retorno = "";
		}

		return retorno;

	}

	private Viagem carregaViagem(Long ciotNum) throws Exception {

		Viagem viagem;
		viagem = new Viagem();

		try {

			Contratante contrat = new Contratante();

			DocumentoContratante documentoContratante = new DocumentoContratante();
			documentoContratante = new DocumentoContratante();
			documentoContratante.setNumero(25681529000149L);

			contrat.setDocumentoContratante(documentoContratante);
			viagem.setContratante(contrat);

			viagem.setIdCliente(ciotNum);

			Contrato contrato = new Contrato();
			contrato = new Contrato();
			contrato.setNumero(ciotNum.toString());
			viagem.setContrato(contrato);

			List<Favorecido> listFavorecido = new ArrayList<Favorecido>();
			listFavorecido = servicePAMCARD.buscaFavorecido(ciotNum.toString());
			viagem.setListFavorecido(listFavorecido);

			List<Veiculo> listVeic = new ArrayList<Veiculo>();
			listVeic = servicePAMCARD.buscaVeiculo(ciotNum.toString());
			viagem.setVeiculoQtde(listVeic.size());
			viagem.setListVeiculo(listVeic);

			Viagem viagemVia = new Viagem();
			viagemVia = servicePAMCARD.buscaVIA(ciotNum.toString());
			viagem.setDistanciaKM(viagemVia.getDistanciaKM());
			viagem.setDataPartida(viagemVia.getDataPartida());
			viagem.setDataTermino(viagemVia.getDataTermino());
			viagem.setCarga(viagemVia.getCarga());
			viagem.setOrigemCidade(viagemVia.getOrigemCidade());
			viagem.setDestinoCidade(viagemVia.getDestinoCidade());
			viagem.setDocumentoQtde(viagemVia.getDocumentoQtde());

			List<DocumentoViagem> listDocumentoViagem = new ArrayList<DocumentoViagem>();
			listDocumentoViagem = servicePAMCARD.buscaDocumentoViagem(ciotNum);
			viagem.setListDocumentoViagem(listDocumentoViagem);

			List<PessoaFiscal> listPessoaFiscal = new ArrayList<PessoaFiscal>();
			listPessoaFiscal = servicePAMCARD.buscaPessoaFiscal(ciotNum.toString());
			viagem.setListPessoaFiscal(listPessoaFiscal);

			List<Parcela> listParcela = new ArrayList<Parcela>();
			listParcela = servicePAMCARD.buscaParcela(ciotNum);
			viagem.setListParcela(listParcela);

			Frete frete = new Frete();
			frete = servicePAMCARD.buscaFrete(ciotNum);
			viagem.setFrete(frete);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return viagem;

	}

	public List<CIOT> pesquisaCiotLiberado(String status) throws Exception {
		List<CIOT> listCIOT = new ArrayList<CIOT>();
		listCIOT = servicePAMCARD.pesquisaCiotLiberado(status);
		return listCIOT;
	}

	public String atualizaDadosIntegracaoCIOT(Map<String, String> mapLogCIOT) throws Exception {
		return servicePAMCARD.atualizaDadosIntegracaoCIOT(mapLogCIOT);
	}

	public String buscaArquivoRetorno() {

		List<CIOT> listCIOT = new ArrayList<CIOT>();
		List<CIOT> listUpdateCIOT = new ArrayList<CIOT>();
		Conversoes conversoes = new Conversoes();
		CIOT ciot;

		try {
			listCIOT = pesquisaCiotLiberado("L");
		} catch (Exception e2) {
			e2.printStackTrace();
		}

		String log = "";
		Scanner scan = null;
		String viagCodigo = null;
		String viagMsgDescricao = null;
		String viagAnttCiotNumero = null;
		String viagAnttCiotProtocolo = null;
		String viagDigito = null;
		String viagId = null;
		String nomeArq = "";
		
		Boolean podeAtu = false;

		for (int i = 0; i < listCIOT.size(); i++) {

			viagCodigo = null;
			viagMsgDescricao = null;
			viagAnttCiotNumero = null;
			viagAnttCiotProtocolo = null;
			viagDigito = null;
			viagId = null;
			nomeArq = "";
			
			ciot = new CIOT();

			ciot = listCIOT.get(i);
			
			podeAtu = false;

			if (!listCIOT.get(i).getNomeArquivo().equals("N")) {
				
				podeAtu = true;

				nomeArq = ciot.getNomeArquivo();
				String fileName = Paths.getDIRETORIO_MGEWEB_CIOT_OUT() + nomeArq; 
				
				try {
					scan = new Scanner(new File(fileName));

					while (scan.hasNextLine()) {

						String line = scan.nextLine();

						if (line.length() >= 16 && line.substring(0, 16).toUpperCase().equals("MENSAGEM.CODIGO=")) {
							viagCodigo = line.substring(16, line.length());
						}

						if (line.length() >= 24 && line.substring(0, 24).toUpperCase().equals("VIAGEM.ANTT.CIOT.NUMERO=")) {
							viagAnttCiotNumero = line.substring(16, line.length());
						}

						if (line.length() >= 22 && line.substring(0, 22).toUpperCase().equals("VIAGEM.ANTT.PROTOCOLO=")) {

							viagAnttCiotProtocolo = line.substring(22, line.length());

						}

						if (line.length() >= 14 && line.substring(0, 14).toUpperCase().equals("VIAGEM.DIGITO=")) {
							viagDigito = line.substring(14, line.length());
						}

						if (line.length() >= 10 && line.substring(0, 10).toUpperCase().equals("VIAGEM.ID=")) {
							viagId = line.substring(10, line.length());
						}

						if (line.length() >= 19 && line.substring(0, 19).toUpperCase().equals("MENSAGEM.DESCRICAO=")) {
							viagMsgDescricao = line.substring(19, line.length());

						}

					}

				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}

				if (viagId != null) {

					ciot.setProtocolo(viagAnttCiotProtocolo);
					ciot.setDigito(viagDigito);
					ciot.setId(viagId);
					ciot.setNumero(Long.valueOf(viagAnttCiotNumero));
					ciot.setStatus("P");
				}
			}

			if (viagCodigo == null && podeAtu) {
				viagMsgDescricao = "Arquivo de retorno não encontrado, favor verificar a disponibilidade de serviço.";
			}
			
			if(podeAtu) {
				
				String dataAtual = conversoes.getDataHoraAtual();
				ciot.setMsgIntegra(dataAtual + " => " + viagMsgDescricao);
				listUpdateCIOT.add(ciot);	
			}			
			
		}

		try {
			if (podeAtu = true) {
				//log = servicePAMCARD.atualizaCIOT(listCIOT);
				log = servicePAMCARD.atualizaCIOT(listUpdateCIOT);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return log;

	}
	
	public CIOT buscaArquivoRetornoPorArquivoGerado(String arquivo) {

		Scanner scan = null;
		String viagCodigo = null;
		String viagMsgDescricao = null;
		String viagAnttCiotNumero = null;
		String viagAnttCiotProtocolo = null;
		String viagDigito = null;
		String viagId = null;
		String viagDataGeracao = null;
		String viagStatusParcela = null;
		String nomeArq = "";
        Conversoes conv = new Conversoes();
        		
		
		CIOT ciot = new CIOT();

		nomeArq = arquivo;
		String fileName = Paths.getDIRETORIO_MGEWEB_CIOT_OUT() + nomeArq; // "PAMCARD_8543_110220220739.txt";
		try {
			scan = new Scanner(new File(fileName));

			while (scan.hasNextLine()) {

				String line = scan.nextLine();

				if (line.length() >= 16 && line.substring(0, 16).toUpperCase().equals("MENSAGEM.CODIGO=")) {
					viagCodigo = line.substring(16, line.length());
				}

				if (line.length() >= 24 && line.substring(0, 24).toUpperCase().equals("VIAGEM.ANTT.CIOT.NUMERO=")) {
					viagAnttCiotNumero = line.substring(24, line.length());
				}

				if (line.length() >= 22 && line.substring(0, 22).toUpperCase().equals("VIAGEM.ANTT.PROTOCOLO=")) {

					viagAnttCiotProtocolo = line.substring(22, line.length());

				}

				if (line.length() >= 14 && line.substring(0, 14).toUpperCase().equals("VIAGEM.DIGITO=")) {
					viagDigito = line.substring(14, line.length());
				}

				if (line.length() >= 10 && line.substring(0, 10).toUpperCase().equals("VIAGEM.ID=")) {
					viagId = line.substring(10, line.length());
				}

				if (line.length() >= 19 && line.substring(0, 19).toUpperCase().equals("MENSAGEM.DESCRICAO=")) {
					viagMsgDescricao = line.substring(19, line.length());

				}
				
				if (line.length() >= 30 && line.substring(0, 30).toUpperCase().equals("VIAGEM.ANTT.CIOT.GERACAO.DATA=")) {
					viagDataGeracao = line.substring(30, line.length());
				}
				
				if (line.length() >= 14 && line.substring(0, 14).toUpperCase().equals("VIAGEM.STATUS=")) {
					viagStatusParcela = line.substring(14, line.length());
				}
				
				

			}

		} catch (FileNotFoundException e) {			
			e.printStackTrace();
		}

		if (viagId != null) {
			ciot.setProtocolo(viagAnttCiotProtocolo);
			ciot.setDigito(viagDigito);
			ciot.setId(viagId);
			ciot.setMsgIntegra(viagMsgDescricao);
			Date dataGeracao = conv.converteDataDDMMYYYY(viagDataGeracao);
  			ciot.setDataGeracao(dataGeracao);
			ciot.setStatusParcela(Integer.parseInt(viagStatusParcela));
			ciot.setNumero(Long.valueOf(viagAnttCiotNumero));
		}

		if (viagCodigo == null) {
			viagMsgDescricao = "Arquivo de retorno não encontrado, favor verificar a disponibilidade de serviço.";
		}

		ciot.setMsgIntegra(viagMsgDescricao);

		return ciot;

	}
	
	
}
