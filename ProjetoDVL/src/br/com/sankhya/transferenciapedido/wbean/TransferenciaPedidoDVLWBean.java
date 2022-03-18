package br.com.sankhya.transferenciapedido.wbean;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.google.gson.Gson;
import com.ibm.icu.text.DecimalFormat;
import com.ibm.icu.text.DecimalFormatSymbols;

import br.com.sankhya.modelcore.auth.AuthenticationInfo;
import br.com.sankhya.modelcore.comercial.BarramentoRegra;
import br.com.sankhya.modelcore.comercial.CentralFaturamento;
import br.com.sankhya.modelcore.comercial.ConfirmacaoNotaHelper;
import br.com.sankhya.modelcore.comercial.LiberacaoAlcadaHelper;
import br.com.sankhya.modelcore.util.MGECoreParameter;
import br.com.sankhya.notificacao.NotificaUsuario;
import br.com.sankhya.notificacao.NotificaUsuarioDAO;
import br.com.sankhya.servico.api.JSONService;
import br.com.sankhya.servico.api.ResponseService;
import br.com.sankhya.transferenciapedido.model.Cabecalho;
import br.com.sankhya.transferenciapedido.model.CabecalhoPedidoDVL;
import br.com.sankhya.transferenciapedido.model.CorpoNota;
import br.com.sankhya.transferenciapedido.model.EstoqueDVL;
import br.com.sankhya.transferenciapedido.model.ItemNota;
import br.com.sankhya.transferenciapedido.model.ItemPedidoDVL;
import br.com.sankhya.transferenciapedido.model.Nota;
import br.com.sankhya.transferenciapedido.service.ServiceTransferenciaPedido;

public class TransferenciaPedidoDVLWBean {

	/*
	 * GERAR JAR "AcaoinserePedidoTransferencia.jar" EXPORTANDO OS PACOTES:
	 * br.com.sankhya.notificacao br.com.sankhya.servico.api
	 * br.com.sankhya.transferenciapedido.action
	 * br.com.sankhya.transferenciapedido.dao
	 * br.com.sankhya.transferenciapedido.model
	 * br.com.sankhya.transferenciapedido.service
	 * br.com.sankhya.transferenciapedido.wbean
	 */

	ServiceTransferenciaPedido serviceTransfPed;
	private String msgNotaNaoGerada = "";
	private String msgTela = "";
	
	public String InsereTransferenciaPedidoWBean() throws Exception {

		serviceTransfPed = new ServiceTransferenciaPedido();
		String msgErroService = "";
		String msgNotificacaoSnk = "";
		String msgMail = "";
		String erroServicoeIncluriNota = "";

		List<CabecalhoPedidoDVL> listaCabPed = new ArrayList<CabecalhoPedidoDVL>();
		listaCabPed = serviceTransfPed.buscaCabecalhoPedidoDVL();

		for (int i = 0; i < listaCabPed.size(); i++) {

			msgNotaNaoGerada = "";
			
			if (listaCabPed.get(i).getAdNuNotaDup() != null) {

				CabecalhoPedidoDVL cabPed = new CabecalhoPedidoDVL();
				cabPed = listaCabPed.get(i);
				Long nuNota = Long.valueOf(cabPed.getAdNuNotaDup().toString());
				Long numNotaorigem = Long.valueOf(cabPed.getNumNota().toString());
				cabPed.setNumNota("0");

				ItemNota itemsNota = new ItemNota();

				List<ItemPedidoDVL> listaItensPedido = new ArrayList<ItemPedidoDVL>();
				listaItensPedido = serviceTransfPed.buscaItemPedidoDVL(nuNota);

				if (listaItensPedido != null) {

					Cabecalho cabecalho = new Cabecalho();

					itemsNota.setINFORMARPRECO("False");
					itemsNota.setItem(listaItensPedido);

					cabecalho.setCabecalho(cabPed);
					CorpoNota corpoNota = new CorpoNota();
					corpoNota.setCabecalho(cabPed);
					corpoNota.setItens(itemsNota);

					Nota nota = new Nota();
					nota.setNota(corpoNota);

					Gson gson = new Gson();
					String json = gson.toJson(nota);

					String jsonStr = "\n" + json.replace("\",", "\"},").replace(":\"", ":{\"$\":\"")
							.replace("\"},{\"", "\"}},{\"").replace("}]", "}}]}")
							.replace("{\"nota\":{\"cabecalho\"",
									"{\r\n" + "   \"serviceName\":\"CACSP.incluirNota\",\r\n"
											+ "   \"requestBody\":{\"nota\":{\"cabecalho\"")
							.replace("\"$\":\" \"", "").replace("\"CODPROD\":{", "\"NUNOTA\": {},\"CODPROD\":{")
							.replace("},\"itens\"", "}},\"itens\"");

					Long numNotaTransf = 0l;

					JSONService jsonService = new JSONService();
					String idSessionNew = "";

					boolean podeContinuar = true;

					try {
						jsonService.LogoutSnkDVL();
						idSessionNew = jsonService.LoginSnkDVL("UserAPI", "TiDvl@Snk#");

						ResponseService resp = new ResponseService();

						resp = jsonService.IncluirNotaSnkDVL(idSessionNew, jsonStr,
								Long.valueOf(cabPed.getAdNuNotaDup()));

						if (resp.getResponseBodyErro() != null) {
							podeContinuar = false;
							erroServicoeIncluriNota = "<br><h2><font color=\"#000099\">Mensagem: "
									+ resp.getResponseBodyErro().getStatusMessage() + "</font><h2>";
						} else {
							if (resp.getResponseIncluirNota() != null
									&& resp.getResponseIncluirNota().getNunota() != null) {
								numNotaTransf = Long.valueOf(resp.getResponseIncluirNota().getNunota());
							}
						}

					} catch (Exception e) {
						e.printStackTrace();
					}

					if (numNotaTransf > 0l) {

						List<EstoqueDVL> listaItensValorComSemEstoque = new ArrayList<EstoqueDVL>();
						listaItensValorComSemEstoque = serviceTransfPed.buscaItensValorComSemEstoqNota(numNotaTransf);

						if (listaItensValorComSemEstoque != null && listaItensValorComSemEstoque.size() > 0) {

							EstoqueDVL estoque;

							boolean prodExcluido = false;

							for (int j = 0; j < listaItensValorComSemEstoque.size(); j++) {

								estoque = new EstoqueDVL();
								estoque = listaItensValorComSemEstoque.get(j);
								prodExcluido = false;

								if (estoque.getQdeDiferenca() < 0) {

									Long codProd = estoque.getCodprod();

									prodExcluido = serviceTransfPed.deletaItensNota(numNotaTransf, codProd);

									if (!prodExcluido) {
										if (podeContinuar) {
											podeContinuar = false;
											msgNotaNaoGerada = msgNotaNaoGerada
													+ "<br><h1><b>TRANSFERÊNCIA P/ IMPRÓPRIO NÃO REALIZADA P/ O NÚMERO DE DOCUMENTO "
													+ numNotaorigem
													+ " [ Bipagem p/ Cód. de Barras - Local 1101 ]</b></h1>";
										}
									}
								}

								listaItensValorComSemEstoque.get(j).setProdExcluido(prodExcluido);

							}

							msgNotificacaoSnk = msgNotificacaoSnk + msgNotaNaoGerada
									+ "<br>[Número do documento - Bipagem: " + numNotaorigem
									+ "] [Número único - Nota de Transferência: " + numNotaTransf;

							msgMail = msgMail + montaMensagemEmail(numNotaorigem, nuNota, numNotaTransf,
									listaItensValorComSemEstoque);

						}

					} else {
						msgTela = msgTela + "<hr/>" + msgNotaNaoGerada
								+ "<br><h1><font color=\"#B22222\">TRANSFERÊNCIA P/ IMPRÓPRIO NÃO REALIZADA P/ O NÚMERO DE DOCUMENTO: "
								+ numNotaorigem + " [ Bipagem p/ Cód. de Barras - Local 1101 ]</font></h1>"
								+ erroServicoeIncluriNota;

						msgMail = msgMail + msgTela;
						msgNotificacaoSnk = msgNotificacaoSnk + msgTela;
					}

					Boolean confirmouNota = false;
					msgErroService = msgErroService + "(1) ";

					if (podeContinuar) {
						try {

							if (numNotaTransf != null && numNotaTransf > 0) {
								
                                // Gera liberação na TSILIB 
								//jsonService.ConfirmaNotaSnkDVL(idSessionNew, Long.valueOf(numNotaTransf));
								jsonService.LogoutSnkDVL();
								
								
								BarramentoRegra barramentoConfirmacao1 = BarramentoRegra.build(CentralFaturamento.class,"regrasConfirmacaoSilenciosa.xml", AuthenticationInfo.getCurrent());
								barramentoConfirmacao1.setValidarSilencioso(true);
                                ConfirmacaoNotaHelper.confirmarNota(BigDecimal.valueOf(numNotaTransf), barramentoConfirmacao1);
								 ConfirmacaoNotaHelper.confirmarNota(BigDecimal.valueOf(numNotaTransf),
										 barramentoConfirmacao1);
								
								Thread.sleep(1000);

								// Insere código do usuário para liberação na TSILIB
								serviceTransfPed.atualizaLibLimNota(numNotaTransf);
								
								Thread.sleep(1000);
								
								// Confirma a Nota
								//jsonService.ConfirmaNotaSnkDVL(idSessionNew, Long.valueOf(numNotaTransf));

								 BarramentoRegra barramentoConfirmacao = BarramentoRegra.build(CentralFaturamento.class,"regrasConfirmacaoSilenciosa.xml", AuthenticationInfo.getCurrent());
                                 barramentoConfirmacao.setValidarSilencioso(true);
                                 ConfirmacaoNotaHelper.confirmarNota(BigDecimal.valueOf(numNotaTransf), barramentoConfirmacao);
								 ConfirmacaoNotaHelper.confirmarNota(BigDecimal.valueOf(numNotaTransf),
								 barramentoConfirmacao);
								
								 Thread.sleep(1000);
								
								String confirmada = serviceTransfPed.notaConfirmada(numNotaTransf);

								if (confirmada.equals("Não")) {

									msgMail = msgMail + "<br><h1><font color=\"#B22222\">NOTA CONFIRMADA: "
											+ confirmada.toUpperCase() + "</font><h1>";

								} else {
									msgMail = msgMail + "<br><h1><font color=\"#32CD32\">NOTA CONFIRMADA: "
											+ confirmada.toUpperCase() + "</font><h1>";
									confirmouNota = true;
								}
								
							}

						} catch (Exception e) {
							e.printStackTrace();
						}
						if (!confirmouNota) {
							msgNotaNaoGerada = msgNotaNaoGerada
									+ "<br><h2>NOTA DE TRANSFERÊNCIA P/ IMPRÓPRIO NÃO CONFIRMADA: NÚMERO ÚNICO: "
									+ numNotaTransf + " <br>[ Cód. de Barras - Local 1102 ]</h2>" + "<br> "
									+ msgErroService;
						}
					}

					jsonService.LogoutSnkDVL();

					if (!podeContinuar || !confirmouNota) {
						if (numNotaTransf != null) {
							// serviceTransfPed.deletaItensNota(numNotaTransf, null);
							// serviceTransfPed.deletaCabecalhoNota(numNotaTransf);
							msgErroService = msgErroService + "\n" + " A nota de transferência não será gerada";
						}
					}
				}

			}
		}

		if (msgMail.length() > 1) {
			notificaUserPorEmail(msgMail, false);
		}

		if (msgNotificacaoSnk.length() > 1) {
			notificaUserSnk(msgNotificacaoSnk);
		}

		return "Procedimento concluído! " + "\n" + msgTela;

	}
	
	private String montaMensagemEmail(Long numNotaOrigem, Long nuUnicoNotaOrig, Long nuUnicoNotaDest, List<EstoqueDVL> listaItensValorComSemEstoq) throws Exception {		
		
		DecimalFormat df = new DecimalFormat("###,###0.00");
	    DecimalFormatSymbols dfs = new DecimalFormatSymbols();

	    dfs.setDecimalSeparator(',');
	    dfs.setGroupingSeparator('.');
	    df.setDecimalFormatSymbols(dfs);
		
		String titulo = "";
		if((nuUnicoNotaDest == null || nuUnicoNotaDest == 0l) && msgNotaNaoGerada.length() > 0) {
			titulo = msgNotaNaoGerada;
		}else{
			titulo =  "<hr/><h1><b>Transferência de Produtos Impróprios - Local 1102 - Nota de Número Único " + nuUnicoNotaDest + "</b></h1><hr/>" 
					+"<h3><b>Bipagem por Cód. de Barras - Local 1101: Número do documento " + numNotaOrigem +" / Número único " + nuUnicoNotaOrig + "</h3><br>";		            
		}		
		
		String tituloSemEstoque = "<h4>[Estoque Insuficiente]</h4>"; 
		String cabTabelaHtml = "<table border>\r\n";
		String colunaTabelaHtml = 
		          "   <tr bgcolor=\"#CCCCCC\">\r\n"
				+ "      <td align=center><b>&nbsp;&nbsp;Código&nbsp;&nbsp;</b></td>\r\n"
				+ "      <td align=center><b>&nbsp;&nbsp;Produto&nbsp;&nbsp;</b></td>\r\n"
				+ "      <td align=center><b>&nbsp;&nbsp;Local Origem&nbsp;&nbsp;</b></td>\r\n"
				+ "      <td align=center><b>&nbsp;&nbsp;Local Destino&nbsp;&nbsp;</b></td>\r\n"
				+ "      <td align=center><b>&nbsp;&nbsp;Código Volume&nbsp;&nbsp;</b></td>\r\n"
				+ "      <td align=center><b>&nbsp;&nbsp;Quantidade&nbsp;&nbsp;</b></td>\r\n"
				+ "      <td align=center><b>&nbsp;&nbsp;Valor Unit.&nbsp;&nbsp;</b></td>\r\n"
				+ "      <td align=center><b>&nbsp;&nbsp;Valor Total&nbsp;&nbsp;</b></td>\r\n"
				+ "      <td align=center><b>&nbsp;&nbsp;Est. Reservado&nbsp;&nbsp;</b></td>\r\n"
				+ "      <td align=center><b>&nbsp;&nbsp;Estoque&nbsp;&nbsp;</b></td>\r\n"
				+ "      <td align=center><b>&nbsp;&nbsp;Diferença&nbsp;&nbsp;</b></td>\r\n" 
				+ "      <td align=center><b>&nbsp;&nbsp;Prod. Excluído&nbsp;&nbsp;</b></td>\r\n"
				+ "   </tr>\r\n";
		
		String tituloComEstoque = "<h4>[Itens Baixados no Estoque]</h4>"; 
		String linhasTabelaItensSemEstoqueMsg = "";		
		
		Double vlrTotalNota = 0d; 
		Integer qdePordNotaSemEstoq = 0;
		
		for (int i = 0; i < listaItensValorComSemEstoq.size(); i++) {
			EstoqueDVL estoqueDVL = new EstoqueDVL();
			estoqueDVL = listaItensValorComSemEstoq.get(i);			
			
			if(estoqueDVL.isProdExcluido()) {				

			    String valorUnit = df.format(estoqueDVL.getVlrunit());
			    String valorTotal = df.format(estoqueDVL.getVlrtotal());
			    vlrTotalNota += estoqueDVL.getVlrtotal(); 
			    qdePordNotaSemEstoq++;			    
				
				linhasTabelaItensSemEstoqueMsg = linhasTabelaItensSemEstoqueMsg 
						+ "   <tr>\r\n" 
						+ "      <td align=center>" + estoqueDVL.getCodprod() + "      </td>\r\n" 
						+ "      <td align=center>" + estoqueDVL.getDescricaoprod() + "</td>\r\n"
						+ "      <td align=center>" + "1101" + " </td>\r\n" 
						+ "      <td align=center>" + "1102" + " </td>\r\n" 
						+ "      <td align=center>" + estoqueDVL.getCodvol() + "      </td>\r\n" 
						+ "      <td align=center>" + estoqueDVL.getQdeBaixa() + "</td>\r\n"
						+ "      <td align=center>" + valorUnit + "</td>\r\n"
						+ "      <td align=center>" + valorTotal + "</td>\r\n"
						+ "      <td align=center>" + estoqueDVL.getReservado() + "</td>\r\n" 
						+ "      <td align=center>" + estoqueDVL.getEstoque() + "</td>\r\n" 
						+ "      <td align=center>" + estoqueDVL.getQdeDiferenca() + "</td>\r\n" 
						+ "      <td align=center>" + (!estoqueDVL.isProdExcluido() ? "Não" : "Sim")  + "</td>\r\n" 							
						+ "      <td></td>\r\n" 
						+ "   </tr>\r\n";
			}

			
		}	
		
		String totalizacaoItensSemEstoque = "<h4>Total de itens: " + qdePordNotaSemEstoq + "</h4>"
		                                  + "<h4>Valor Total: R$ " + df.format(vlrTotalNota) + "</h4>";	
		
		
		String linhasTabelaItensComEstoqueMsg = "";
		vlrTotalNota = 0d; 
		Integer qdePordNotaComEstoq = 0;
		
		for (int i = 0; i < listaItensValorComSemEstoq.size(); i++) {
			EstoqueDVL estoqueDVL = new EstoqueDVL();
			estoqueDVL = listaItensValorComSemEstoq.get(i);
			
			if(!estoqueDVL.isProdExcluido()) {
				
				String valorUnit = df.format(estoqueDVL.getVlrunit());
			    String valorTotal = df.format(estoqueDVL.getVlrtotal());
			    qdePordNotaComEstoq++;
			    vlrTotalNota += estoqueDVL.getVlrtotal(); 
			    
				linhasTabelaItensComEstoqueMsg = linhasTabelaItensComEstoqueMsg 
						+ "   <tr>\r\n" 
						+ "      <td align=center>" + estoqueDVL.getCodprod() + "      </td>\r\n" 
						+ "      <td align=center>" + estoqueDVL.getDescricaoprod() + "</td>\r\n"
						+ "      <td align=center>" + "1101" + " </td>\r\n" 
						+ "      <td align=center>" + "1102" + " </td>\r\n" 
						+ "      <td align=center>" + estoqueDVL.getCodvol() + "      </td>\r\n" 
						+ "      <td align=center>" + estoqueDVL.getQdeBaixa() + "</td>\r\n"
						+ "      <td align=center>" + valorUnit + "</td>\r\n"
						+ "      <td align=center>" + valorTotal + "</td>\r\n"
						+ "      <td align=center>" + estoqueDVL.getReservado() + "</td>\r\n" 
						+ "      <td align=center>" + estoqueDVL.getEstoque() + "</td>\r\n" 
						+ "      <td align=center>" + estoqueDVL.getQdeDiferenca() + "</td>\r\n" 
						+ "      <td align=center>" + (!estoqueDVL.isProdExcluido() ? "Não" : "Sim")  + "</td>\r\n" 							
						+ "      <td></td>\r\n" 
						+ "   </tr>\r\n";
			}

			
		}
		
		String totalizacaoItensComEstoque = "<h4>Total de itens: " + qdePordNotaComEstoq + "</h4>"
                                           + "<h4>Valor Total: R$ " + df.format(vlrTotalNota) + "</h4></br></br>";
		
		String rodapeTabelaHtml = "</table>\r\n";
		
		
		String textoHtml = titulo;

		if (qdePordNotaSemEstoq > 0) {
			String tabelaItensSemEstoqueHtml = cabTabelaHtml + colunaTabelaHtml + linhasTabelaItensSemEstoqueMsg
					+ rodapeTabelaHtml + totalizacaoItensSemEstoque;
			textoHtml = textoHtml + tituloSemEstoque + tabelaItensSemEstoqueHtml;
		}
		if (qdePordNotaComEstoq > 0) {
			String tabelaItensComEstoqueHtml = cabTabelaHtml + colunaTabelaHtml + linhasTabelaItensComEstoqueMsg
					+ rodapeTabelaHtml + totalizacaoItensComEstoque;
			textoHtml = textoHtml + "<br><br>" + tituloComEstoque + tabelaItensComEstoqueHtml;
		}
		
		return textoHtml;

	}
	
	public void notificaUserPorEmail(String msgMail, boolean msgErroServic ) throws Exception {

		String parametro = "";
		parametro = MGECoreParameter.getParameterAsString("EMAILAVARIAS");

		List<String> listEmailDest = serviceTransfPed.buscaDestinatariosEmail(parametro);

		String msg = "";

		if (!msgErroServic) {
			DateFormat dfmt = new SimpleDateFormat("EEEE, d 'de' MMMM 'de' yyyy");
			Date hoje = Calendar.getInstance(Locale.getDefault()).getTime();
			msg = "<h3><b>" + dfmt.format(hoje) + "</b></h3></br>";
		}

		msg = msg + msgMail;

		String assunto = "Transferência Imprório - Produtos Avariados";

		for (int i = 0; i < listEmailDest.size(); i++) {

			String emailParc = listEmailDest.get(i);
			LiberacaoAlcadaHelper.enviaEmail(emailParc, assunto, msg);
		}

	}

	public void notificaUserSnk(String msgNotificacaoSnk) throws Exception {

		String parametroNotificaAvarias = "";
		parametroNotificaAvarias = MGECoreParameter.getParameterAsString("NOTIFICAAVARIAS");
		String s = parametroNotificaAvarias;
		String[] strArr = s.split("\\s+");

		NotificaUsuarioDAO notificaUsuarioDAO = new NotificaUsuarioDAO();
		NotificaUsuario notificaUser;

		for (int i = 0; i < strArr.length; i++) {

			notificaUser = new NotificaUsuario();
			notificaUser.setCodgrupo(4l);
			notificaUser.setCodusuremetente(0l);
			notificaUser.setDescricao(msgNotificacaoSnk);
			notificaUser.setDhcriacao(null);
			notificaUser.setIdentificador("PERSONALIZADO");
			notificaUser.setImportancia(3l);
			notificaUser.setTipo("P");
			notificaUser.setTitulo("Transferência Imprório - Produtos Avariados");

			notificaUsuarioDAO.insereNotificacaoUsuario(notificaUser);
		}

	}
	

}
