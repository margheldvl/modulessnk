package br.com.sankhya.pamcard.dao;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import br.com.sankhya.jape.EntityFacade;
import br.com.sankhya.jape.dao.JdbcWrapper;
import br.com.sankhya.jape.sql.NativeSql;
import br.com.sankhya.modelcore.util.EntityFacadeFactory;
import br.com.sankhya.pamcard.model.CIOT;
import br.com.sankhya.pamcard.model.Carga;
import br.com.sankhya.pamcard.model.Cidade;
import br.com.sankhya.pamcard.model.Contratante;
import br.com.sankhya.pamcard.model.Contrato;
import br.com.sankhya.pamcard.model.Conversoes;
import br.com.sankhya.pamcard.model.Documento;
import br.com.sankhya.pamcard.model.DocumentoContratante;
import br.com.sankhya.pamcard.model.DocumentoPessoFiscal;
import br.com.sankhya.pamcard.model.DocumentoViagem;
import br.com.sankhya.pamcard.model.Empresa;
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

public class ContratoFrete {

	@SuppressWarnings("unused")
	private static Paths paths = new Paths();
	String caminhoCompletoArq = "";

	public String geraArquivo(String tipoOperacao, Long numCiot) throws Exception {

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
			PAMCARD.append("viagem.carga.peso=" + conversoes.formatDouble2CasasPonto(viagem.getCarga().getPeso())).append("\n");

			PAMCARD.append("viagem.documento.qtde=" + viagem.getDocumentoQtde()).append("\n");

			List<DocumentoViagem> listDocViagem = new ArrayList<DocumentoViagem>();
			listDocViagem = viagem.getListDocumentoViagem();

			if (listDocViagem != null) {

				Integer numDocViag = 0;

				for (int i = 0; i < listDocViagem.size(); i++) {

					numDocViag++;

					if (numDocViag == 1) {

						DocumentoViagem doc = new DocumentoViagem();
						doc = listDocViagem.get(i);

						PAMCARD.append("viagem.documento" + numDocViag + ".tipo=" + doc.getTipo()).append("\n");
						PAMCARD.append("viagem.documento" + numDocViag + ".numero=" + doc.getNumero()).append("\n");
						PAMCARD.append("viagem.documento" + numDocViag + ".pessoafiscal.qtde=" + listDocViagem.size() /* doc.getQuantidade() */).append("\n");
					}
				}

			}

			List<PessoaFiscal> listPFiscal = new ArrayList<PessoaFiscal>();
			listPFiscal = viagem.getListPessoaFiscal();

			try {

				if (listPFiscal != null) {

					Integer numPFiscal = 0;
					Integer numDoc = 1;

					for (int i = 0; i < listPFiscal.size(); i++) {

						PessoaFiscal pessoaFiscal = new PessoaFiscal();
						pessoaFiscal = listPFiscal.get(i);

						numPFiscal++;

						DocumentoPessoFiscal documentoPessoFiscal = new DocumentoPessoFiscal();
						documentoPessoFiscal = pessoaFiscal.getDocumentoPessoFiscal();

						EnderecoPessoaFiscal enderecoPessoaFiscal = new EnderecoPessoaFiscal();
						enderecoPessoaFiscal = pessoaFiscal.getEnderecoPessoaFiscal();

						Cidade cidade = new Cidade();
						cidade = enderecoPessoaFiscal.getCidade();
						PAMCARD.append("viagem.documento" + numDoc + ".pessoafiscal" + numPFiscal + ".tipo=" + pessoaFiscal.getTipo()).append("\n");
						PAMCARD.append("viagem.documento" + numDoc + ".pessoafiscal" + numPFiscal + ".documento.tipo=" + documentoPessoFiscal.getTipo()).append("\n");
						PAMCARD.append("viagem.documento" + numDoc + ".pessoafiscal" + numPFiscal + ".documento.numero=" + documentoPessoFiscal.getNumero()).append("\n");
						PAMCARD.append("viagem.documento" + numDoc + ".pessoafiscal" + numPFiscal + ".nome=" + pessoaFiscal.getNome()).append("\n");
						PAMCARD.append("viagem.documento" + numDoc + ".pessoafiscal" + numPFiscal + ".endereco.logradouro=" + enderecoPessoaFiscal.getLogradouro()).append("\n");
						PAMCARD.append("viagem.documento" + numDoc + ".pessoafiscal" + numPFiscal + ".endereco.numero=" + enderecoPessoaFiscal.getNumero()).append("\n");
						PAMCARD.append("viagem.documento" + numDoc + ".pessoafiscal" + numPFiscal + ".endereco.bairro=" + enderecoPessoaFiscal.getBairro()).append("\n");
						PAMCARD.append("viagem.documento" + numDoc + ".pessoafiscal" + numPFiscal + ".endereco.cidade.ibge=" + cidade.getIbge()).append("\n");
						PAMCARD.append("viagem.documento" + numDoc + ".pessoafiscal" + numPFiscal + ".endereco.cep=" + enderecoPessoaFiscal.getCep()).append("\n");

					}

				}
			} catch (Exception e) {
				e.printStackTrace();
			}

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

			Date agora = new Date();

			String pattern = "ddMMyyyyHHmm";
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
			String dataFormatada = simpleDateFormat.format(agora);

			String nomeArqExt = "PAMCARD_" + numCiot + "_" + dataFormatada + ".txt";
			String caminhoCompletoArq = Paths.getDIRETORIO_MGEWEB_CIOT_OUT() + nomeArqExt;

			try {

				String linha = "";
				arquivo = new FileWriter(new File(caminhoCompletoArq));
				Scanner scan = new Scanner(PAMCARD.toString());

				while (scan.hasNextLine()) {
					linha = scan.nextLine() + "\r\n";
					arquivo.write(linha);
				}
				arquivo.close();

			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}

		} catch (Exception e) {

		}

		return caminhoCompletoArq;

	}

	public Viagem carregaViagem(Long ciotNum) throws Exception {

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
			listFavorecido = buscaFavorecido(ciotNum.toString());
			viagem.setListFavorecido(listFavorecido);

			List<Veiculo> listVeic = new ArrayList<Veiculo>();
			listVeic = buscaVeiculo(ciotNum.toString());
			viagem.setVeiculoQtde(listVeic.size());
			viagem.setListVeiculo(listVeic);

			Viagem viagemVia = new Viagem();
			viagemVia = buscaVIA(ciotNum.toString());
			viagem.setDistanciaKM(viagemVia.getDistanciaKM());
			viagem.setDataPartida(viagemVia.getDataPartida());
			viagem.setDataTermino(viagemVia.getDataTermino());
			viagem.setCarga(viagemVia.getCarga());
			viagem.setOrigemCidade(viagemVia.getOrigemCidade());
			viagem.setDestinoCidade(viagemVia.getDestinoCidade());
			viagem.setDocumentoQtde(viagemVia.getDocumentoQtde());

			List<DocumentoViagem> listDocumentoViagem = new ArrayList<DocumentoViagem>();
			listDocumentoViagem = buscaDocumentoViagem(ciotNum);
			viagem.setListDocumentoViagem(listDocumentoViagem);

			List<PessoaFiscal> listPessoaFiscal = new ArrayList<PessoaFiscal>();
			listPessoaFiscal = buscaPessoaFiscal(ciotNum.toString());
			viagem.setListPessoaFiscal(listPessoaFiscal);

			List<Parcela> listParcela = new ArrayList<Parcela>();
			listParcela = buscaParcela(ciotNum);
			viagem.setListParcela(listParcela);

			Frete frete = new Frete();
			frete = buscaFrete(ciotNum);
			viagem.setFrete(frete);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return viagem;

	}

	public List<Favorecido> buscaFavorecido(String NuCIOT) throws Exception {

		JdbcWrapper jdbc = null;
		EntityFacade entity = EntityFacadeFactory.getDWFFacade();

		jdbc = entity.getJdbcWrapper();

		jdbc.openSession();
		NativeSql sql = new NativeSql(jdbc);

		sql.appendSql("SELECT DESCRICAO_TIPO, ");
		sql.appendSql("       NUCIOT, ");
		sql.appendSql("       TIPO, ");
		sql.appendSql("       DOCUMENTO_QTDE, ");
		sql.appendSql("       CPF_TIPO, ");
		sql.appendSql("       CPF_NUMERO, ");
		sql.appendSql("       RG_TIPO, ");
		sql.appendSql("       RG_NUMERO, ");
		sql.appendSql("       RG_UF, ");
		sql.appendSql("       RG_EMISSOR_ID, ");
		sql.appendSql("       RG_EMISSAO_DATA, ");
		sql.appendSql("       RNTRC_TIPO, ");
		sql.appendSql("       RNTRC_NUMERO, ");
		sql.appendSql("       NOME, ");
		sql.appendSql("       DATA_NASCIMENTO, ");
		sql.appendSql("       NACIONALIDADE_ID, ");
		sql.appendSql("       NATURALIDADE_IBGE, ");
		sql.appendSql("       SEXO, ");
		sql.appendSql("       ENDERECO_LOGRADOURO, ");
		sql.appendSql("       ENDERECO_NUMERO, ");
		sql.appendSql("       ENDERECO_BAIRRO, ");
		sql.appendSql("       END_CIDADE_IBGE, ");
		sql.appendSql("       ENDERECO_CEP, ");
		sql.appendSql("       END_PROP_TIPO_ID, ");
		sql.appendSql("       END_RESIDE_DESDE, ");
		sql.appendSql("       TELEFONE_DDD, ");
		sql.appendSql("       TELEFONE_NUMERO, ");
		sql.appendSql("       MEIO_PAGTO, ");
		sql.appendSql("       CARTAO_NUMERO, ");
		sql.appendSql("       EMPRESA_NOME, ");
		sql.appendSql("       EMPRESA_CNPJ, ");
		sql.appendSql("       EMPRESA_RNTRC ");
		sql.appendSql("FROM   (SELECT 'Contratado'                                           AS descricao_tipo, ");
		sql.appendSql("               CIOT.NUCIOT                                            AS nuciot   , ");
		sql.appendSql("               1                                                      AS tipo, ");
		sql.appendSql("               '3'                                                    AS documento_qtde, ");
		sql.appendSql("               CASE ");
		sql.appendSql("                 WHEN PROP.TIPPESSOA = 'J' THEN 1 ");
		sql.appendSql("                 ELSE 2 ");
		sql.appendSql("               END                                                    AS cpf_tipo, ");
		sql.appendSql("               PROP.CGC_CPF                                           AS cpf_numero, ");
		sql.appendSql("               CASE ");
		sql.appendSql("                 WHEN PROP.TIPPESSOA = 'J' THEN 4 ");
		sql.appendSql("                 ELSE 3 ");
		sql.appendSql("               END                                                    AS rg_tipo, ");
		sql.appendSql("               NVL (PROP.IDENTINSCESTAD, 'ISENTA')                    AS rg_numero, ");
		sql.appendSql("               UFS.UF                                                 AS rg_uf, ");
		sql.appendSql("               '1'                                                    AS rg_emissor_id, ");
		sql.appendSql("               NVL (TO_CHAR (FUN.DTRG, 'DD/MM/YYYY'), '01/01/1900')   AS rg_emissao_data, ");
		sql.appendSql("               '5'                                                    AS rntrc_tipo, ");
		sql.appendSql("               PROP.AD_RNTRC                                          AS rntrc_numero, ");
		sql.appendSql("               CASE ");
		sql.appendSql("                 WHEN PROP.TIPPESSOA = 'J' THEN PROP.RAZAOSOCIAL ");
		sql.appendSql("                 ELSE FUN.NOMEFUNC ");
		sql.appendSql("               END                                                    AS nome, ");
		sql.appendSql("               NVL (TO_CHAR (FUN.DTNASC, 'DD/MM/YYYY'), '01/01/1995') AS data_nascimento, ");
		sql.appendSql("               '1'                                                    AS nacionalidade_id, ");
		sql.appendSql("               CID.CODMUNFIS                                          AS naturalidade_ibge, ");
		sql.appendSql("               NVL (FUN.SEXO, 'M')                                    AS sexo, ");
		sql.appendSql("               END.NOMEEND                                            AS endereco_logradouro, ");
		sql.appendSql("               PROP.NUMEND                                            AS endereco_numero, ");
		sql.appendSql("               RET_END_CLIENTE (PROP.CODPARC, 'BAI')                  AS endereco_bairro, ");
		sql.appendSql("               CID.CODMUNFIS                                          AS END_cidade_ibge, ");
		sql.appendSql("               PROP.CEP                                               AS endereco_cep, ");
		sql.appendSql("               '1'                                                    AS END_prop_tipo_id, ");
		sql.appendSql("               TO_CHAR (PROP.DTCAD, 'DD/MM/YYYY')                        AS END_reside_desde, ");
		sql.appendSql("               '0' ");
		sql.appendSql("               || SUBSTR (PROP.TELEFONE, 1, 2)                        AS telefone_ddd, ");
		sql.appendSql("               SUBSTR(FORMATATELEFONE (PROP.TELEFONE), 5, INSTR (FORMATATELEFONE (PROP.TELEFONE), '-') - 5) ");
		sql.appendSql("               || SUBSTR (FORMATATELEFONE (PROP.TELEFONE), INSTR (FORMATATELEFONE (PROP.TELEFONE), '-') ");
		sql.appendSql("                                                           + 1, 4)    AS telefone_numero, ");
		sql.appendSql("               '1'                                                    AS MEIO_PAGTO, ");
		sql.appendSql("               PROP.AD_CARTAOPAMCARD                                  AS cartao_numero, ");
		sql.appendSql("               CASE ");
		sql.appendSql("                 WHEN PROP.TIPPESSOA = 'J' THEN PROP.RAZAOSOCIAL ");
		sql.appendSql("                 ELSE '' ");
		sql.appendSql("               END                                                    AS empresa_nome, ");
		sql.appendSql("               CASE ");
		sql.appendSql("                 WHEN PROP.TIPPESSOA = 'J' THEN PROP.CGC_CPF ");
		sql.appendSql("                 ELSE '' ");
		sql.appendSql("               END                                                    AS empresa_cnpj, ");
		sql.appendSql("               CASE ");
		sql.appendSql("                 WHEN PROP.TIPPESSOA = 'J' THEN RTRIM (PROP.AD_RNTRC) ");
		sql.appendSql("                 ELSE '' ");
		sql.appendSql("               END                                                    AS empresa_rntrc ");
		sql.appendSql("        FROM   AD_TOLCIOT CIOT ");
		sql.appendSql("               INNER JOIN TGFPAR PROP ");
		sql.appendSql("                       ON CIOT.CODPROPRIETARIO = PROP.CODPARC ");
		sql.appendSql("               INNER JOIN TSICID CID ");
		sql.appendSql("                       ON PROP.CODCID = CID.CODCID ");
		sql.appendSql("               INNER JOIN TSIEND END ");
		sql.appendSql("                       ON PROP.CODEND = END.CODEND ");
		sql.appendSql("               INNER JOIN TSIUFS UFS ");
		sql.appendSql("                       ON CID.UF = UFS.CODUF ");
		sql.appendSql("               INNER JOIN TGFVEI VEI ");
		sql.appendSql("                       ON CIOT.CODVEICULO = VEI.CODVEICULO ");
		sql.appendSql("               LEFT JOIN TFPFUN FUN ");
		sql.appendSql("                      ON PROP.CODPARC = FUN.CODPARC ");
		sql.appendSql("                         AND ( FUN.CODEMP = 1 ");
		sql.appendSql("                                OR FUN.CODEMP = 101 ) ");
		sql.appendSql("        WHERE  PROP.TIPPESSOA = 'F' AND NUCIOT = :NuCIOT ");
		sql.appendSql("        UNION ALL ");
		sql.appendSql("        SELECT 'Motorista'                                          descricao_tipo, ");
		sql.appendSql("               CIOT.NUCIOT, ");
		sql.appendSql("               CASE ");
		sql.appendSql("                 WHEN PROP.TIPPESSOA = 'J' THEN 1 ");
		sql.appendSql("                 ELSE 3 ");
		sql.appendSql("               END                                                  AS tipo, ");
		sql.appendSql("               TRANSLATE (PROP.TIPPESSOA, 'FJ', '23')               AS documento_qtde, ");
		sql.appendSql("               2                                                    AS cpf_tipo, ");
		sql.appendSql("               MOT.CGC_CPF                                          AS cpf_numero, ");
		sql.appendSql("               3                                                    AS rg_tipo, ");
		sql.appendSql("               MOT.IDENTINSCESTAD                                   AS rg_numero, ");
		sql.appendSql("               UFS.UF                                               AS rg_uf, ");
		sql.appendSql("               '1'                                                  AS rg_emissor_id, ");
		sql.appendSql("               NVL (TO_CHAR (FUN.DTRG, 'DD/MM/YYYY'), '01/01/1900') AS rg_emissao_data, ");
		sql.appendSql("               CASE ");
		sql.appendSql("                 WHEN PROP.TIPPESSOA = 'J' THEN '5' ");
		sql.appendSql("                 ELSE NULL ");
		sql.appendSql("               END                                                  AS rntrc_tipo, ");
		sql.appendSql("               CASE ");
		sql.appendSql("                 WHEN PROP.TIPPESSOA = 'J' THEN MOT.AD_RNTRC ");
		sql.appendSql("                 ELSE NULL ");
		sql.appendSql("               END                                                  AS rntrc_numero, ");
		sql.appendSql("               FUN.NOMEFUNC                                         AS nome, ");
		sql.appendSql("               TO_CHAR (FUN.DTNASC, 'DD/MM/YYYY')                   AS data_nascimento, ");
		sql.appendSql("               '1'                                                  AS nacionalidade_id, ");
		sql.appendSql("               CID.CODMUNFIS                                        AS naturalidade_ibge, ");
		sql.appendSql("               FUN.SEXO                                             AS sexo, ");
		sql.appendSql("               END.NOMEEND                                          AS endereco_logradouro, ");
		sql.appendSql("               MOT.NUMEND                                           AS endereco_numero, ");
		sql.appendSql("               RET_END_CLIENTE (MOT.CODPARC, 'BAI')                 AS endereco_bairro, ");
		sql.appendSql("               CID.CODMUNFIS                                        AS END_cidade_ibge, ");
		sql.appendSql("               MOT.CEP                                              AS endereco_cep, ");
		sql.appendSql("               '1'                                                  AS end_mot_tipo_id, ");
		sql.appendSql("               TO_CHAR (MOT.DTCAD, 'DD/MM/YYYY')                       AS end_reside_desde, ");
		sql.appendSql("               '0' ");
		sql.appendSql("               || SUBSTR (MOT.TELEFONE, 1, 2)                       AS telefone_ddd, ");
		sql.appendSql("               SUBSTR (FORMATATELEFONE (MOT.TELEFONE), 5, INSTR (FORMATATELEFONE (MOT.TELEFONE), '-') - 5) ");
		sql.appendSql("               || SUBSTR (FORMATATELEFONE (MOT.TELEFONE), INSTR (FORMATATELEFONE (MOT.TELEFONE), '-') ");
		sql.appendSql("                                                          + 1, 4)   AS telefone_numero, ");
		sql.appendSql("               '1'                                                  AS MEIO_PAGTO, ");
		sql.appendSql("               MOT.AD_CARTAOPAMCARD                                 AS cartao_numero, ");
		sql.appendSql("               CASE ");
		sql.appendSql("                 WHEN PROP.TIPPESSOA = 'J' THEN REMOVEESPACO (REMOVE_ACENTOS (PROP.RAZAOSOCIAL)) ");
		sql.appendSql("                 ELSE NULL ");
		sql.appendSql("               END                                                  AS empresa_nome, ");
		sql.appendSql("               CASE ");
		sql.appendSql("                 WHEN PROP.TIPPESSOA = 'J' THEN PROP.CGC_CPF ");
		sql.appendSql("                 ELSE NULL ");
		sql.appendSql("               END                                                  AS empresa_cnpj, ");
		sql.appendSql("               CASE ");
		sql.appendSql("                 WHEN PROP.TIPPESSOA = 'J' THEN PROP.AD_RNTRC ");
		sql.appendSql("                 ELSE NULL ");
		sql.appendSql("               END                                                  AS empresa_rntrc ");
		sql.appendSql("        FROM   AD_TOLCIOT CIOT ");
		sql.appendSql("               INNER JOIN TGFPAR MOT ");
		sql.appendSql("                       ON CIOT.CODMOTORISTA = MOT.CODPARC ");
		sql.appendSql("               INNER JOIN TGFPAR PROP ");
		sql.appendSql("                       ON CIOT.CODPROPRIETARIO = PROP.CODPARC ");
		sql.appendSql("               INNER JOIN TSICID CID ");
		sql.appendSql("                       ON MOT.CODCID = CID.CODCID ");
		sql.appendSql("               LEFT JOIN TFPFUN FUN ");
		sql.appendSql("                      ON MOT.CODPARC = FUN.CODPARC ");
		sql.appendSql("                         AND ( FUN.CODEMP = 1 ");
		sql.appendSql("                                OR FUN.CODEMP = 101 ) ");
		sql.appendSql("               INNER JOIN TSIEND END ");
		sql.appendSql("                       ON MOT.CODEND = END.CODEND ");
		sql.appendSql("               INNER JOIN TSIUFS UFS ");
		sql.appendSql("                       ON CID.UF = UFS.CODUF ");
		sql.appendSql("               INNER JOIN TGFVEI VEI ");
		sql.appendSql("                       ON CIOT.CODVEICULO = VEI.CODVEICULO ");
		sql.appendSql("WHERE  NUCIOT = :NuCIOT ");
		sql.appendSql(") FAV ");
		sql.appendSql("ORDER BY tipo ");

		sql.setNamedParameter("NuCIOT", NuCIOT);

		List<Favorecido> listFavorecido = new ArrayList<Favorecido>();

		try {

			ResultSet rs = sql.executeQuery();

			Conversoes conversoes = new Conversoes();

			while (rs.next()) {

				Favorecido favorecido = new Favorecido();

				favorecido.setTipo(Integer.parseInt(rs.getString("tipo")));
				favorecido.setDocumentoQtde(Integer.parseInt(rs.getString("documento_qtde")));

				List<Documento> listDocumento = new ArrayList<Documento>();

				if (rs.getString("cpf_tipo") != null) {

					Documento documentoCPF = new Documento();
					documentoCPF.setNumero(rs.getString("cpf_numero"));
					documentoCPF.setTipo(Integer.parseInt(rs.getString("cpf_tipo")));
					listDocumento.add(documentoCPF);
				}

				if (rs.getString("rg_tipo") != null) {
					Documento documentoRG = new Documento();
					documentoRG.setNumero(rs.getString("rg_numero"));
					documentoRG.setTipo(Integer.parseInt(rs.getString("rg_tipo")));
					documentoRG.setUf(rs.getString("rg_uf"));
					documentoRG.setEmissorId(Integer.parseInt(rs.getString("rg_emissor_id")));
					documentoRG.setDataEmissao(conversoes.converteDataDDMMYYYY(rs.getString("rg_emissao_data")));
					listDocumento.add(documentoRG);
				}

				if (rs.getString("rntrc_tipo") != null) {

					Documento documentoRntrc = new Documento();
					documentoRntrc.setNumero(rs.getString("rntrc_numero"));
					documentoRntrc.setTipo(Integer.parseInt(rs.getString("rntrc_tipo")));

					listDocumento.add(documentoRntrc);
				}

				favorecido.setListDocumento(listDocumento);
				favorecido.setNome(rs.getString("nome"));
				favorecido.setDataNascimento(conversoes.converteDataDDMMYYYY(rs.getString("data_nascimento")));
				favorecido.setNacionalidade_id(Integer.parseInt(rs.getString("nacionalidade_id")));

				Cidade cidNatural = new Cidade();
				cidNatural.setIbge(Integer.parseInt(rs.getString("naturalidade_ibge")));
				favorecido.setNaturalidadeCidadeIbge(cidNatural);

				favorecido.setSexo(rs.getString("sexo"));

				Endereco endereco = new Endereco();
				endereco.setLogradouro(rs.getString("endereco_logradouro"));
				endereco.setNumero(Integer.parseInt(rs.getString("endereco_numero")));
				endereco.setBairro(rs.getString("endereco_bairro"));
				Cidade cidadeEndereco = new Cidade();
				cidadeEndereco.setIbge(Integer.parseInt(rs.getString("end_cidade_ibge")));
				endereco.setCidade(cidadeEndereco);
				endereco.setCep(Integer.parseInt(rs.getString("endereco_cep")));
				endereco.setTipoPropriedade(Integer.parseInt(rs.getString("end_prop_tipo_id")));
				endereco.setResideDesde(conversoes.converteDataDDMMYYYY(rs.getString("end_reside_desde")));
				favorecido.setEndereco(endereco);

				Telefone telefone = new Telefone();
				telefone.setTelefoneDDD(rs.getString("telefone_ddd"));
				telefone.setTelefoneNumero(Integer.parseInt(rs.getString("telefone_numero")));
				favorecido.setTelefone(telefone);

				favorecido.setMeioPagamento(rs.getString("meio_pagto"));
				favorecido.setCartao(rs.getString("cartao_numero"));

				Empresa empresa = new Empresa();
				empresa.setNome(rs.getString("empresa_nome"));
				empresa.setCnpj(rs.getString("empresa_cnpj"));
				empresa.setEmpresa_rntrc(rs.getString("empresa_rntrc"));
				favorecido.setEmpresa(empresa);

				listFavorecido.add(favorecido);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return listFavorecido;

	}

	public List<Veiculo> buscaVeiculo(String NuCIOT) throws Exception {

		JdbcWrapper jdbc = null;
		EntityFacade entity = EntityFacadeFactory.getDWFFacade();

		jdbc = entity.getJdbcWrapper();

		jdbc.openSession();
		NativeSql sql = new NativeSql(jdbc);

		sql.appendSql("SELECT CIOT.NUCIOT, ");
		sql.appendSql("       '1'              AS qtde, ");
		sql.appendSql("       VEI.PLACA        AS placa, ");
		sql.appendSql("       VEI.ANTT         AS rntrc, ");
		sql.appendSql("       VEI.AD_CATEGORIA AS categoria ");
		sql.appendSql("FROM   AD_TOLCIOT CIOT ");
		sql.appendSql("       INNER JOIN TGFVEI VEI ");
		sql.appendSql("               ON CIOT.CODVEICULO = VEI.CODVEICULO ");
		sql.appendSql("                  AND NUCIOT = :nuCIOT ");

		sql.setNamedParameter("NuCIOT", NuCIOT);

		List<Veiculo> listVeic = new ArrayList<Veiculo>();

		try {

			ResultSet rs = sql.executeQuery();

			while (rs.next()) {
				Veiculo veic = new Veiculo();
				veic.setCategoria(rs.getString("categoria"));
				veic.setPlaca(rs.getString("placa"));
				veic.setRntrc(rs.getString("rntrc"));
				listVeic.add(veic);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return listVeic;

	}

	public Viagem buscaVIA(String NuCIOT) throws Exception {

		JdbcWrapper jdbc = null;
		EntityFacade entity = EntityFacadeFactory.getDWFFacade();

		jdbc = entity.getJdbcWrapper();

		jdbc.openSession();
		NativeSql sql = new NativeSql(jdbc);

		sql.appendSql("SELECT CIOT.NUCIOT, ");
		sql.appendSql("       (SELECT ROUND(OCR.KMTOT, 0) ");
		sql.appendSql("        FROM   TGFCIOT COT ");
		sql.appendSql("               INNER JOIN TGFORD ORD ");
		sql.appendSql("                       ON COT.NUVIAG = ORD.NUVIAG ");
		sql.appendSql("                          AND ORD.CODEMP = 1 ");
		sql.appendSql("               INNER JOIN AD_TGFOCR OCR ");
		sql.appendSql("                       ON ORD.ORDEMCARGA = OCR.ORDEMCARGA ");
		sql.appendSql("        WHERE  COT.CIOT = CIOT.NUCIOT)            AS distancia_km, ");
		sql.appendSql("       TO_CHAR (TRUNC (SYSDATE), 'DD/MM/YYYY')    AS data_partida, ");
		sql.appendSql("       TO_CHAR (CIOT.DTINICIOT + 7, 'DD/MM/YYYY') AS data_termino, ");
		sql.appendSql("       '3157807'                                  AS origem_cidade_ibge, ");
		sql.appendSql("       '3157807'                                  AS destino_cidade_ibge, ");
		sql.appendSql("       '3405'                                     AS carga_natureza, ");
		sql.appendSql("       '100'                                      AS carga_peso, ");
		sql.appendSql("       '5'                                        AS carga_tipo, ");
		sql.appendSql("       '1'                                        AS documento_qtde ");
		sql.appendSql("FROM   AD_TOLCIOT CIOT ");
		sql.appendSql("WHERE  NUCIOT = :NuCIOT  ");
		sql.setNamedParameter("NuCIOT", NuCIOT);

		Viagem viag = new Viagem();

		try {

			ResultSet rs = sql.executeQuery();

			Conversoes conversoes = new Conversoes();

			while (rs.next()) {

				Cidade cidadeOrig = new Cidade();
				cidadeOrig.setIbge(Integer.parseInt(rs.getString("origem_cidade_ibge")));
				viag.setOrigemCidade(cidadeOrig);

				Cidade cidadeDest = new Cidade();
				cidadeDest.setIbge(Integer.parseInt(rs.getString("destino_cidade_ibge")));
				viag.setDestinoCidade(cidadeDest);

				Carga carga = new Carga();
				carga.setNatureza(Integer.parseInt(rs.getString("carga_natureza")));
				carga.setPeso(Double.parseDouble(rs.getString("carga_peso")));
				carga.setTipo(Integer.parseInt(rs.getString("carga_tipo")));
				viag.setCarga(carga);

				viag.setDocumentoQtde(Integer.parseInt(rs.getString("documento_qtde")));
				viag.setDataPartida(conversoes.converteDataDDMMYYYY(rs.getString("data_partida")));
				viag.setDataTermino(conversoes.converteDataDDMMYYYY(rs.getString("data_termino")));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return viag;

	}

	public List<DocumentoViagem> buscaDocumentoViagem(Long numCIOT) throws Exception {

		JdbcWrapper jdbc = null;
		EntityFacade entity = EntityFacadeFactory.getDWFFacade();

		jdbc = entity.getJdbcWrapper();

		jdbc.openSession();
		NativeSql sql = new NativeSql(jdbc);

		sql.appendSql("SELECT CIOT.NUCIOT, ");
		sql.appendSql("       '1'         AS documento_tipo, ");
		sql.appendSql("       MDF.NUMMDFE AS documento_numero, ");
		sql.appendSql("       '2'         AS documento_pf_qtde ");
		sql.appendSql("FROM   AD_TOLCIOT CIOT ");
		sql.appendSql("       INNER JOIN TGFCIOT COT ");
		sql.appendSql("               ON CIOT.NUCIOT = COT.CIOT ");
		sql.appendSql("       INNER JOIN TGFVIAG VIA ");
		sql.appendSql("               ON COT.NUVIAG = VIA.NUVIAG ");
		sql.appendSql("       INNER JOIN TSIEMP EMP ");
		sql.appendSql("               ON VIA.CODEMP = EMP.CODEMP ");
		sql.appendSql("       LEFT JOIN TGFMDFE MDF ");
		sql.appendSql("              ON VIA.NUVIAG = MDF.NUVIAG ");
		sql.appendSql("       LEFT JOIN TGFMME MOT ");
		sql.appendSql("              ON VIA.NUVIAG = MOT.NUVIAG ");
		sql.appendSql("       LEFT JOIN TGFPAR PAR ");
		sql.appendSql("              ON MOT.CODPARC = PAR.CODPARC ");
		sql.appendSql("       LEFT JOIN TSIEND END ");
		sql.appendSql("              ON PAR.CODEND = END.CODEND ");
		sql.appendSql("       LEFT JOIN TSICID CDM ");
		sql.appendSql("              ON PAR.CODCID = CDM.CODCID ");
		sql.appendSql("       LEFT JOIN TGFVEI VEI ");
		sql.appendSql("              ON VIA.CODVEIPRIN = VEI.CODVEICULO ");
		sql.appendSql("                 AND PAR.CODPARC = VEI.CODPARC ");
		sql.appendSql("       LEFT JOIN TFPFUN FUN ");
		sql.appendSql("              ON PAR.CODPARC = FUN.CODPARC ");
		sql.appendSql("       LEFT JOIN TSIUFS UF ");
		sql.appendSql("              ON FUN.UFRG = UF.CODUF ");
		sql.appendSql("       LEFT JOIN TSICID CID ");
		sql.appendSql("              ON FUN.CODCID = CID.CODCID ");
		sql.appendSql("WHERE  NUCIOT = :NuCIOT ");

		sql.setNamedParameter("NuCIOT", numCIOT);

		List<DocumentoViagem> listDocumentoViagem = new ArrayList<DocumentoViagem>();

		try {

			ResultSet rs = sql.executeQuery();

			while (rs.next()) {

				DocumentoViagem documentoViagem = new DocumentoViagem();
				documentoViagem.setTipo(Integer.parseInt(rs.getString("documento_tipo")));
				documentoViagem.setNumero(rs.getString("documento_numero"));
				listDocumentoViagem.add(documentoViagem);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return listDocumentoViagem;

	}

	public List<PessoaFiscal> buscaPessoaFiscal(String numCIOT) throws Exception {

		JdbcWrapper jdbc = null;
		EntityFacade entity = EntityFacadeFactory.getDWFFacade();

		jdbc = entity.getJdbcWrapper();

		jdbc.openSession();
		NativeSql sql = new NativeSql(jdbc);

		sql.appendSql("SELECT CIOT.NUCIOT, ");
		sql.appendSql("       '1'                                             AS documento_pf_tipo, ");
		sql.appendSql("       '1'                                             AS documento_pf_doc_tipo, ");
		sql.appendSql("       EMP.CGC                                         AS documento_pf_doc_numero, ");
		sql.appendSql("       REMOVEESPACO (REMOVE_ACENTOS (EMP.RAZAOSOCIAL)) AS documento_pf_nome, ");
		sql.appendSql("       'AV DOUTOR ANGELO TEIXEIRA DA COSTA'            AS documento_pf_endereco, ");
		sql.appendSql("       '888'                                           AS documento_pf_END_numero, ");
		sql.appendSql("       'FRIMIZA'                                       AS documento_pf_END_bairro, ");
		sql.appendSql("       3157807                                        AS documento_pf_END_CID_ibge, ");
		sql.appendSql("       '33045170'                                      AS documento_pf_END_cep ");
		sql.appendSql("FROM   AD_TOLCIOT CIOT ");
		sql.appendSql("       INNER JOIN TGFCIOT COT ");
		sql.appendSql("               ON CIOT.NUCIOT = COT.CIOT ");
		sql.appendSql("       INNER JOIN TGFVIAG VIA ");
		sql.appendSql("               ON COT.NUVIAG = VIA.NUVIAG ");
		sql.appendSql("       INNER JOIN TSIEMP EMP ");
		sql.appendSql("               ON VIA.CODEMP = EMP.CODEMP ");
		sql.appendSql("       LEFT JOIN TGFMDFE MDF ");
		sql.appendSql("              ON VIA.NUVIAG = MDF.NUVIAG ");
		sql.appendSql("       LEFT JOIN TGFMME MOT ");
		sql.appendSql("              ON VIA.NUVIAG = MOT.NUVIAG ");
		sql.appendSql("       LEFT JOIN TGFPAR PAR ");
		sql.appendSql("              ON MOT.CODPARC = PAR.CODPARC ");
		sql.appendSql("       LEFT JOIN TSIEND END ");
		sql.appendSql("              ON PAR.CODEND = END.CODEND ");
		sql.appendSql("       LEFT JOIN TSICID CDM ");
		sql.appendSql("              ON PAR.CODCID = CDM.CODCID ");
		sql.appendSql("       LEFT JOIN TGFVEI VEI ");
		sql.appendSql("              ON VIA.CODVEIPRIN = VEI.CODVEICULO ");
		sql.appendSql("                 AND PAR.CODPARC = VEI.CODPARC ");
		sql.appendSql("       LEFT JOIN TFPFUN FUN ");
		sql.appendSql("              ON PAR.CODPARC = FUN.CODPARC ");
		sql.appendSql("       LEFT JOIN TSIUFS UF ");
		sql.appendSql("              ON FUN.UFRG = UF.CODUF ");
		sql.appendSql("       LEFT JOIN TSICID CID ");
		sql.appendSql("              ON FUN.CODCID = CID.CODCID ");
		sql.appendSql("WHERE  NUCIOT = :NuCIOT ");
		sql.appendSql("GROUP  BY CIOT.NUCIOT, ");
		sql.appendSql("          EMP.CGC, ");
		sql.appendSql("          EMP.RAZAOSOCIAL ");
		sql.appendSql("UNION ALL ");
		sql.appendSql("SELECT CIOT.NUCIOT, ");
		sql.appendSql("       '2'                                                                  AS documento_pf_tipo, ");
		sql.appendSql("       '2'                                                                  AS documento_pf_doc_tipo, ");
		sql.appendSql("       PAR.CGC_CPF                                                          AS documento_pf_doc_numero, ");
		sql.appendSql("       REMOVEESPACO (REMOVE_ACENTOS (PAR.RAZAOSOCIAL))                      AS documento_pf_nome, ");
		sql.appendSql("       REMOVEESPACO (REMOVE_ACENTOS (RET_END_CLIENTE (PAR.CODPARC, 'END'))) AS documento_pf_endereco, ");
		sql.appendSql("       PAR.NUMEND                                                           AS documento_pf_END_numero, ");
		sql.appendSql("       REMOVEESPACO (REMOVE_ACENTOS (RET_END_CLIENTE (PAR.CODPARC, 'BAI'))) AS documento_pf_END_bairro, ");
		sql.appendSql("       CID.CODMUNFIS                                                        AS documento_pf_END_CID_ibge, ");
		sql.appendSql("       PAR.CEP                                                              AS documento_pf_END_cep ");
		sql.appendSql("FROM   AD_TOLCIOT CIOT ");
		sql.appendSql("       INNER JOIN TGFCIOT COT ");
		sql.appendSql("               ON CIOT.NUCIOT = COT.CIOT ");
		sql.appendSql("       INNER JOIN TGFVIAG VIA ");
		sql.appendSql("               ON COT.NUVIAG = VIA.NUVIAG ");
		sql.appendSql("       INNER JOIN TSIEMP EMP ");
		sql.appendSql("               ON VIA.CODEMP = EMP.CODEMP ");
		sql.appendSql("       LEFT JOIN TGFMDFE MDF ");
		sql.appendSql("              ON VIA.NUVIAG = MDF.NUVIAG ");
		sql.appendSql("       LEFT JOIN TGFMME MOT ");
		sql.appendSql("              ON VIA.NUVIAG = MOT.NUVIAG ");
		sql.appendSql("       LEFT JOIN TGFPAR PAR ");
		sql.appendSql("              ON MOT.CODPARC = PAR.CODPARC ");
		sql.appendSql("       LEFT JOIN TSIEND END ");
		sql.appendSql("              ON PAR.CODEND = END.CODEND ");
		sql.appendSql("       LEFT JOIN TSICID CDM ");
		sql.appendSql("              ON PAR.CODCID = CDM.CODCID ");
		sql.appendSql("       LEFT JOIN TGFVEI VEI ");
		sql.appendSql("              ON VIA.CODVEIPRIN = VEI.CODVEICULO ");
		sql.appendSql("                 AND PAR.CODPARC = VEI.CODPARC ");
		sql.appendSql("       LEFT JOIN TFPFUN FUN ");
		sql.appendSql("              ON PAR.CODPARC = FUN.CODPARC ");
		sql.appendSql("       LEFT JOIN TSIUFS UF ");
		sql.appendSql("              ON FUN.UFRG = UF.CODUF ");
		sql.appendSql("       LEFT JOIN TSICID CID ");
		sql.appendSql("              ON FUN.CODCID = CID.CODCID ");
		sql.appendSql("WHERE  NUCIOT = :NuCIOT  ");
		sql.appendSql("GROUP  BY CIOT.NUCIOT, ");
		sql.appendSql("          PAR.CGC_CPF, ");
		sql.appendSql("          PAR.RAZAOSOCIAL, ");
		sql.appendSql("          PAR.CODPARC, ");
		sql.appendSql("          PAR.NUMEND, ");
		sql.appendSql("          PAR.CODPARC, ");
		sql.appendSql("          CID.CODMUNFIS, ");
		sql.appendSql("          PAR.CEP  ");

		sql.setNamedParameter("NuCIOT", numCIOT);

		List<PessoaFiscal> listPessoaFiscal = new ArrayList<PessoaFiscal>();

		try {

			ResultSet rs = sql.executeQuery();

			while (rs.next()) {

				PessoaFiscal pessoaFiscal = new PessoaFiscal();
				EnderecoPessoaFiscal enderecoPessoaFiscal = new EnderecoPessoaFiscal();
				Cidade cidade = new Cidade();

				pessoaFiscal.setTipo(Integer.parseInt(rs.getString("documento_pf_tipo")));

				DocumentoPessoFiscal documentoPessoFiscal = new DocumentoPessoFiscal();
				documentoPessoFiscal.setNumero(rs.getString("documento_pf_doc_numero"));
				documentoPessoFiscal.setTipo(Integer.parseInt(rs.getString("documento_pf_doc_tipo")));

				pessoaFiscal.setNome(rs.getString("documento_pf_nome"));

				enderecoPessoaFiscal.setLogradouro(rs.getString("documento_pf_endereco"));
				enderecoPessoaFiscal.setNumero(rs.getString("documento_pf_END_numero"));
				enderecoPessoaFiscal.setBairro(rs.getString("documento_pf_END_bairro"));
				cidade.setIbge(Integer.parseInt(rs.getString("documento_pf_END_CID_ibge")));
				enderecoPessoaFiscal.setCidade(cidade);
				enderecoPessoaFiscal.setCep(Integer.parseInt(rs.getString("documento_pf_END_cep")));

				pessoaFiscal.setDocumentoPessoFiscal(documentoPessoFiscal);
				pessoaFiscal.setEnderecoPessoaFiscal(enderecoPessoaFiscal);

				listPessoaFiscal.add(pessoaFiscal);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return listPessoaFiscal;

	}

	public List<Parcela> buscaParcela(Long numCIOT) throws Exception {

		JdbcWrapper jdbc = null;
		EntityFacade entity = EntityFacadeFactory.getDWFFacade();

		jdbc = entity.getJdbcWrapper();

		jdbc.openSession();
		NativeSql sql = new NativeSql(jdbc);

		sql.appendSql("SELECT CIOT.NUCIOT, ");
		sql.appendSql("       '1'                            AS parcela_qtde, ");
		sql.appendSql("       '2'                            AS parcela_efetivacao_tipo, ");
		sql.appendSql("       '29.96'                        AS parcela_valor, ");
		sql.appendSql("       '1'                            AS parcela_subtipo, ");
		sql.appendSql("       '2'                            AS parcela_status_id, ");
		sql.appendSql("       TO_CHAR (SYSDATE, 'DD/MM/YYYY') AS parcela_data, ");
		sql.appendSql("       MOT.CODPARC                    AS parcela_favorecido_id, ");
		sql.appendSql("       CASE ");
		sql.appendSql("         WHEN PROP.TIPPESSOA = 'J' THEN 1 ");
		sql.appendSql("         ELSE 3 ");
		sql.appendSql("       END                            AS parcela_favorecido_tipo_id, ");
		sql.appendSql("       '1'                            AS parcela_numero_cliente ");
		sql.appendSql("FROM   AD_TOLCIOT CIOT ");
		sql.appendSql("       INNER JOIN TGFCIOT COT ");
		sql.appendSql("               ON CIOT.NUCIOT = COT.CIOT ");
		sql.appendSql("       INNER JOIN TGFVIAG VIA ");
		sql.appendSql("               ON COT.NUVIAG = VIA.NUVIAG ");
		sql.appendSql("       INNER JOIN TSIEMP EMP ");
		sql.appendSql("               ON VIA.CODEMP = EMP.CODEMP ");
		sql.appendSql("       LEFT JOIN TGFMDFE MDF ");
		sql.appendSql("              ON VIA.NUVIAG = MDF.NUVIAG ");
		sql.appendSql("       LEFT JOIN TGFMME MOT ");
		sql.appendSql("              ON VIA.NUVIAG = MOT.NUVIAG ");
		sql.appendSql("       INNER JOIN TGFPAR PROP ");
		sql.appendSql("               ON CIOT.CODPROPRIETARIO = PROP.CODPARC ");
		sql.appendSql("       LEFT JOIN TGFPAR PAR ");
		sql.appendSql("              ON MOT.CODPARC = PAR.CODPARC ");
		sql.appendSql("       LEFT JOIN TSIEND END ");
		sql.appendSql("              ON PAR.CODEND = END.CODEND ");
		sql.appendSql("       LEFT JOIN TSICID CDM ");
		sql.appendSql("              ON PAR.CODCID = CDM.CODCID ");
		sql.appendSql("       LEFT JOIN TGFVEI VEI ");
		sql.appendSql("              ON VIA.CODVEIPRIN = VEI.CODVEICULO ");
		sql.appendSql("                 AND PAR.CODPARC = VEI.CODPARC ");
		sql.appendSql("       LEFT JOIN TFPFUN FUN ");
		sql.appendSql("              ON PAR.CODPARC = FUN.CODPARC ");
		sql.appendSql("       LEFT JOIN TSIUFS UF ");
		sql.appendSql("              ON FUN.UFRG = UF.CODUF ");
		sql.appendSql("       LEFT JOIN TSICID CID ");
		sql.appendSql("              ON FUN.CODCID = CID.CODCID ");
		sql.appendSql("WHERE  NUCIOT = :NuCIOT  ");
		sql.appendSql("GROUP BY CIOT.NUCIOT ");
		sql.appendSql("         ,MOT.CODPARC ");
		sql.appendSql("         ,PROP.TIPPESSOA ");

		sql.setNamedParameter("NuCIOT", numCIOT);

		List<Parcela> listParcela = new ArrayList<Parcela>();
		Parcela parcela = new Parcela();

		try {

			ResultSet rs = sql.executeQuery();
			Conversoes conversoes = new Conversoes();
			while (rs.next()) {

				parcela.setEfetivacaoTipo(Integer.parseInt(rs.getString("parcela_efetivacao_tipo")));
				parcela.setValor(Double.parseDouble(rs.getString("parcela_valor")));
				parcela.setSubTipo(Integer.parseInt(rs.getString("parcela_subtipo")));
				parcela.setStatusId(Integer.parseInt(rs.getString("parcela_status_id")));
				parcela.setData(conversoes.converteDataDDMMYYYY(rs.getString("parcela_data")));
				parcela.setFavorecidoId(Integer.parseInt(rs.getString("parcela_favorecido_id")));
				parcela.setFavorecidoTipo(Integer.parseInt(rs.getString("parcela_favorecido_tipo_id")));
				parcela.setNumeroCliente(Integer.parseInt(rs.getString("parcela_numero_cliente")));

				listParcela.add(parcela);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return listParcela;

	}

	public Frete buscaFrete(Long numCIOT) throws Exception {

		JdbcWrapper jdbc = null;
		EntityFacade entity = EntityFacadeFactory.getDWFFacade();

		jdbc = entity.getJdbcWrapper();

		jdbc.openSession();
		NativeSql sql = new NativeSql(jdbc);

		sql.appendSql("SELECT distinct CIOT.NUCIOT, ");
		sql.appendSql("       '29.96' AS frete_valor_bruto, ");
		sql.appendSql("       '2'     AS frete_item_qtde, ");
		sql.appendSql("       '315'   AS frete_item_tipo, ");
		sql.appendSql("       '4'     AS frete_item_tar_qtd, ");
		sql.appendSql("       '19.96' AS frete_item_valor, ");
		sql.appendSql("       '316'   AS frete_item2_tipo, ");
		sql.appendSql("       '4'     AS frete_item2_tar_qtd, ");
		sql.appendSql("       '10.00' AS frete_item2_valor ");
		sql.appendSql("FROM   AD_TOLCIOT CIOT ");
		sql.appendSql("       INNER JOIN TGFCIOT COT ");
		sql.appendSql("               ON CIOT.NUCIOT = COT.CIOT ");
		sql.appendSql("       INNER JOIN TGFVIAG VIA ");
		sql.appendSql("               ON COT.NUVIAG = VIA.NUVIAG ");
		sql.appendSql("       INNER JOIN TSIEMP EMP ");
		sql.appendSql("               ON VIA.CODEMP = EMP.CODEMP ");
		sql.appendSql("       LEFT JOIN TGFMDFE MDF ");
		sql.appendSql("              ON VIA.NUVIAG = MDF.NUVIAG ");
		sql.appendSql("       LEFT JOIN TGFMME MOT ");
		sql.appendSql("              ON VIA.NUVIAG = MOT.NUVIAG ");
		sql.appendSql("       LEFT JOIN TGFPAR PAR ");
		sql.appendSql("              ON MOT.CODPARC = PAR.CODPARC ");
		sql.appendSql("       LEFT JOIN TSIEND END ");
		sql.appendSql("              ON PAR.CODEND = END.CODEND ");
		sql.appendSql("       LEFT JOIN TSICID CDM ");
		sql.appendSql("              ON PAR.CODCID = CDM.CODCID ");
		sql.appendSql("       LEFT JOIN TGFVEI VEI ");
		sql.appendSql("              ON VIA.CODVEIPRIN = VEI.CODVEICULO ");
		sql.appendSql("                 AND PAR.CODPARC = VEI.CODPARC ");
		sql.appendSql("       LEFT JOIN TFPFUN FUN ");
		sql.appendSql("              ON PAR.CODPARC = FUN.CODPARC ");
		sql.appendSql("       LEFT JOIN TSIUFS UF ");
		sql.appendSql("              ON FUN.UFRG = UF.CODUF ");
		sql.appendSql("       LEFT JOIN TSICID CID ");
		sql.appendSql("              ON FUN.CODCID = CID.CODCID ");
		sql.appendSql("WHERE  NUCIOT = :NuCIOT  ");

		sql.setNamedParameter("NuCIOT", numCIOT);

		Frete frete = new Frete();
		List<Item> listItem = new ArrayList<Item>();

		try {

			ResultSet rs = sql.executeQuery();

			Conversoes conversoes = new Conversoes();

			while (rs.next()) {

				Item item = new Item();
				item.setTarifaQuantidade(Integer.parseInt(rs.getString("frete_item_tar_qtd")));
				item.setTipo(Integer.parseInt(rs.getString("frete_item_tipo")));
				item.setValor(conversoes.strDouble(rs.getString("frete_item_valor")));

				listItem.add(item);

				item = new Item();
				item.setTarifaQuantidade(Integer.parseInt(rs.getString("frete_item2_tar_qtd")));
				item.setTipo(Integer.parseInt(rs.getString("frete_item2_tipo")));
				item.setValor(conversoes.strDouble(rs.getString("frete_item2_valor")));

				listItem.add(item);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		frete.setListItem(listItem);

		return frete;

	}

	public List<CIOT> buscaListaCiotPorSttus(String status) throws Exception {

		JdbcWrapper jdbc = null;
		EntityFacade entity = EntityFacadeFactory.getDWFFacade();

		jdbc = entity.getJdbcWrapper();

		jdbc.openSession();
		NativeSql sql = new NativeSql(jdbc);

		sql.appendSql("SELECT CIOT.NUCIOT num_ciot, ");
		sql.appendSql("       CIOT.DTINICIOT data_inic, ");
		sql.appendSql("       CIOT.DTFINCIOT data_fim, ");
		sql.appendSql("       CIOT.STATUS status_ciot, ");
		sql.appendSql("       CIOT.ID id_ciot, ");
		sql.appendSql("       CIOT.PROTOCOLO protocolo_ciot, ");
		sql.appendSql("       CIOT.DIGITO digito_ciot, ");
		sql.appendSql("       CIOT.MSGINTEGRA msg_integra, ");
		sql.appendSql("       CASE WHEN(INSTR(UPPER(MSGINTEGRA),'.TXT',1,1) > 0) THEN SUBSTR(MSGINTEGRA, INSTR(UPPER(MSGINTEGRA),'PAMCARD_',1,1), INSTR(UPPER(MSGINTEGRA),'.TXT',1,1) - INSTR(UPPER(MSGINTEGRA),'PAMCARD_',1,1) + 4) ELSE 'N' END nome_arq ");
		sql.appendSql("FROM   AD_TOLCIOT CIOT ");
		sql.appendSql("       INNER JOIN TGFPAR MOT ");
		sql.appendSql("               ON CIOT.CODMOTORISTA = MOT.CODPARC ");
		sql.appendSql("       INNER JOIN TGFPAR PROP ");
		sql.appendSql("               ON CIOT.CODPROPRIETARIO = PROP.CODPARC ");
		sql.appendSql("WHERE  STATUS = :Status ");
		// sql.appendSql(" AND CIOT.DTINICIOT >= '10/02/2022' ");
		sql.appendSql("       AND NUCIOT IS NOT NULL ");

		sql.setNamedParameter("Status", status);

		List<CIOT> listCIOT = new ArrayList<CIOT>();

		try {

			ResultSet rs = sql.executeQuery();

			Conversoes conversoes = new Conversoes();

			while (rs.next()) {

				CIOT ciot = new CIOT();

				Long numero = Long.valueOf(rs.getString("num_ciot"));
				Date dataInic = rs.getString("data_inic") != null ? conversoes.converteDataDDMMYYYY(rs.getString("data_inic")) : null;
				Date dataFim = rs.getString("data_fim") != null ? conversoes.converteDataDDMMYYYY(rs.getString("data_fim")) : null;
				String statusCiot = rs.getString("status_ciot");
				String idCiot = rs.getString("id_ciot");
				String protocolo = rs.getString("protocolo_ciot");
				String digito = rs.getString("digito_ciot");
				String msgIntegra = rs.getString("msg_integra");
				String nomeArq = rs.getString("nome_arq");

				ciot.setNumero(numero);

				if (dataInic != null) {
					ciot.setDataInic(dataInic);
				}
				if (dataFim != null) {
					ciot.setDataFim(dataFim);
				}

				ciot.setStatus(statusCiot);
				ciot.setId(idCiot);
				ciot.setProtocolo(protocolo);
				ciot.setDigito(digito);
				ciot.setMsgIntegra(msgIntegra);
				ciot.setNomeArquivo(nomeArq);

				listCIOT.add(ciot);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return listCIOT;

	}

	public List<CIOT> buscaListaCiotPorNumero(List<String> listNumCiot) throws Exception {

		JdbcWrapper jdbc = null;
		EntityFacade entity = EntityFacadeFactory.getDWFFacade();

		jdbc = entity.getJdbcWrapper();

		jdbc.openSession();
		NativeSql sql = new NativeSql(jdbc);

		sql.appendSql("SELECT CIOT.NUCIOT num_ciot, ");
		sql.appendSql("       CIOT.DTINICIOT data_inic, ");
		sql.appendSql("       CIOT.DTFINCIOT data_fim, ");
		sql.appendSql("       CIOT.STATUS status_ciot, ");
		sql.appendSql("       CIOT.ID id_ciot, ");
		sql.appendSql("       CIOT.PROTOCOLO protocolo_ciot, ");
		sql.appendSql("       CIOT.DIGITO digito_ciot, ");
		sql.appendSql("       CIOT.MSGINTEGRA msg_integra, ");
		sql.appendSql("       CASE WHEN(INSTR(UPPER(MSGINTEGRA),'.TXT',1,1) > 0) THEN SUBSTR(MSGINTEGRA, INSTR(UPPER(MSGINTEGRA),'PAMCARD_',1,1), INSTR(UPPER(MSGINTEGRA),'.TXT',1,1) - INSTR(UPPER(MSGINTEGRA),'PAMCARD_',1,1) + 4) ELSE 'N' END nome_arq ");
		sql.appendSql("FROM   AD_TOLCIOT CIOT ");
		sql.appendSql("       INNER JOIN TGFPAR MOT ");
		sql.appendSql("               ON CIOT.CODMOTORISTA = MOT.CODPARC ");
		sql.appendSql("       INNER JOIN TGFPAR PROP ");
		sql.appendSql("               ON CIOT.CODPROPRIETARIO = PROP.CODPARC ");
		sql.appendSql("WHERE  NUCIOT IN :LISTNUMCIOT ");
		sql.appendSql(" AND CIOT.DTINICIOT >= '10/02/2022' ");

		sql.setNamedParameter("LISTNUCIOT", listNumCiot.toString());

		List<CIOT> listCIOT = new ArrayList<CIOT>();

		try {

			ResultSet rs = sql.executeQuery();

			Conversoes conversoes = new Conversoes();

			while (rs.next()) {

				CIOT ciot = new CIOT();

				Long numero = Long.valueOf(rs.getString("num_ciot"));
				Date dataInic = rs.getString("data_inic") != null ? conversoes.converteDataDDMMYYYY(rs.getString("data_inic")) : null;
				Date dataFim = rs.getString("data_fim") != null ? conversoes.converteDataDDMMYYYY(rs.getString("data_fim")) : null;
				String statusCiot = rs.getString("status_ciot");
				String idCiot = rs.getString("id_ciot");
				String protocolo = rs.getString("protocolo_ciot");
				String digito = rs.getString("digito_ciot");
				String msgIntegra = rs.getString("msg_integra");
				String nomeArq = rs.getString("nome_arq");

				ciot.setNumero(numero);

				if (dataInic != null) {
					ciot.setDataInic(dataInic);
				}
				if (dataFim != null) {
					ciot.setDataFim(dataFim);
				}

				ciot.setStatus(statusCiot);
				ciot.setId(idCiot);
				ciot.setProtocolo(protocolo);
				ciot.setDigito(digito);
				ciot.setMsgIntegra(msgIntegra);
				ciot.setNomeArquivo(nomeArq);

				listCIOT.add(ciot);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return listCIOT;

	}

	public List<CIOT> pesquisaCiotLiberado(String status) throws Exception {
		List<CIOT> listCIOT = new ArrayList<CIOT>();
		listCIOT = buscaListaCiotPorSttus(status);
		return listCIOT;
	}

	public String atualizaDadosIntegracaoCIOT(Map<String, String> mapLogCIOT) throws Exception {

		JdbcWrapper jdbc = null;
		EntityFacade entity = EntityFacadeFactory.getDWFFacade();

		String log = "";
		jdbc = entity.getJdbcWrapper();
		jdbc.openSession();
		NativeSql execScript = new NativeSql(jdbc);

		for (String key : mapLogCIOT.keySet()) {

			// NativeSql execScript = new NativeSql(jdbc);

			String value = mapLogCIOT.get(key);

			Long nuCiot = Long.valueOf(key);

			execScript = new NativeSql(jdbc);

			execScript.appendSql("UPDATE AD_TOLCIOT SET MSGINTEGRA = '" + value.toString() + "\n" + "' || MSGINTEGRA WHERE NUCIOT = " + nuCiot);

			log = log + " UPDATE AD_TOLCIOT SET MSGINTEGRA = '" + value.toString() + "' WHERE NUCIOT = '" + nuCiot + "';";

			try {

				execScript.executeUpdate();

			} finally {

			}

		}

		return log;

	}

	public String atualizaCIOT(List<CIOT> listCIOT) throws Exception {

		JdbcWrapper jdbc = null;
		EntityFacade entity = EntityFacadeFactory.getDWFFacade();

		List<CIOT> lista = new ArrayList<CIOT>();

		lista = listCIOT;

		String log = "";

		jdbc = entity.getJdbcWrapper();

		jdbc.openSession();

		NativeSql execScript = new NativeSql(jdbc);

		CIOT ciot;

		String p1 = "", p2 = "", p3 = "", msg = "";

		for (int i = 0; i < lista.size(); i++) {

			ciot = new CIOT();

			ciot = lista.get(i);

			p1 = "";
			p2 = "";
			p3 = "";
			msg = "";
			execScript = new NativeSql(jdbc);

			p1 = "UPDATE AD_TOLCIOT SET ";

			if (ciot.getId() != null) {

				p2 = "NUMERO = '" + ciot.getNumero() + "'  , PROTOCOLO = '" + ciot.getProtocolo() + "', DIGITO = '" + ciot.getDigito() + "', ID = '" + ciot.getId() + "', STATUS = 'P', ";

			}

			msg = " MSGINTEGRA = '" + ciot.getMsgIntegra() + "\n" + "'  || MSGINTEGRA ";

			p3 = " WHERE STATUS = 'L' AND NUCIOT = " + ciot.getNumero();

			log = log + p1 + p2 + msg + p3 + "\n";

			try {

				execScript.executeUpdate(p1 + p2 + msg + p3);

			} finally {

			}

		}

		return log;

	}

}
