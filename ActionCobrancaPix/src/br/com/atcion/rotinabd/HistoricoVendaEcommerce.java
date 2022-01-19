package br.com.atcion.rotinabd;

import java.sql.ResultSet;
import java.sql.SQLException;

import br.com.sankhya.extensions.actionbutton.AcaoRotinaJava;
import br.com.sankhya.extensions.actionbutton.ContextoAcao;
import br.com.sankhya.jape.EntityFacade;
import br.com.sankhya.jape.dao.JdbcWrapper;
import br.com.sankhya.jape.sql.NativeSql;
import br.com.sankhya.modelcore.util.EntityFacadeFactory;

public class HistoricoVendaEcommerce implements AcaoRotinaJava {

	@Override
	public void doAction(ContextoAcao arg0) throws Exception {
		insereHistoricoVendaAFV();
	}

	private void insereHistoricoVendaAFV() throws Exception {
		insereHistoricoCabecPedidosAFV();
		insereHistoricoItemPedidosAFV();
	}

	private void insereHistoricoCabecPedidosAFV() throws Exception {

		JdbcWrapper jdbc = null;
		EntityFacade entity = EntityFacadeFactory.getDWFFacade();

		jdbc = entity.getJdbcWrapper();
		try {
			jdbc.openSession();
			NativeSql sqlGeraScript = new NativeSql(jdbc);

			sqlGeraScript.appendSql("SELECT VEN.CODVEND,");
			sqlGeraScript.appendSql("       CODPARCAFV,");
			sqlGeraScript.appendSql("       CAB.CODPARC,");
			sqlGeraScript.appendSql("       CAB.CODEMPNEGOC,");
			sqlGeraScript.appendSql("       CAB.NUNOTA,");
			sqlGeraScript.appendSql("       CAB.DTNEG,");
			sqlGeraScript.appendSql(
					"       'Insert into AFV_ANDROID.te_histpedido@PROVIDER (NUMPEDIDOEMP,NUMPEDIDOAFV,CODIGOCLIENTE,STATUSPEDIDO,VALORPEDIDO,VALORFATURADO,DATAPEDIDO,DATAPREVISTAENTREGA,ORIGEMPEDIDO,CODIGOTIPOPEDIDO,CODIGOCONDPAGTO,DESCRICAOFRETE,DESCONTOCONDPAGTO,CODIGOVENDEDORESP,CODIGOVENDEDOR,CODIGOEMPRESAESP,CODIGOEMPRESA,DATAATUALIZACAO,FLAGSTATUS,NUMPEDIDOAFVSERVER,CESP_CODIGOFORMAPAGTO,CESP_CODIGOUNIDFAT,CESP_MODALIDADE,CESP_PEDDISP) values ('");
			sqlGeraScript.appendSql("       || '''E'");
			sqlGeraScript.appendSql(
					"       || Cast(( SYSDATE - TO_DATE('01-JAN-1970', 'DD-MON-YYYY') ) * ( 24 * 60 * 60 * 1000 ) + TO_NUMBER(TO_CHAR(SYSTIMESTAMP, 'FF3')) AS VARCHAR(20))");
			sqlGeraScript.appendSql("       || ROWNUM");
			sqlGeraScript.appendSql("       || ''', '");
			sqlGeraScript.appendSql("       || '''E'");
			sqlGeraScript.appendSql(
					"       || Cast(( SYSDATE - TO_DATE('01-JAN-1970', 'DD-MON-YYYY') ) * ( 24 * 60 * 60 * 1000 ) + TO_NUMBER(TO_CHAR(SYSTIMESTAMP, 'FF3')) AS VARCHAR(20))");
			sqlGeraScript.appendSql("       || ROWNUM");
			sqlGeraScript.appendSql("       || ''', '''");
			sqlGeraScript.appendSql("       || CAB.CODPARC");
			sqlGeraScript.appendSql("       || ''', '");
			sqlGeraScript.appendSql("       || CASE");
			sqlGeraScript.appendSql("               WHEN( CAB.TIPMOV = 'V' ) THEN '''VENDA'''");
			sqlGeraScript.appendSql("               WHEN( CAB.TIPMOV = 'D' ) THEN '''DEVOLUÇÃO'''");
			sqlGeraScript.appendSql("          END");
			sqlGeraScript.appendSql("       || ', '''");
			sqlGeraScript.appendSql("       || VLRNOTA");
			sqlGeraScript.appendSql("       || ''', '''");
			sqlGeraScript.appendSql("       || VLRNOTA");
			sqlGeraScript.appendSql("       || ''', '''");
			sqlGeraScript.appendSql("       || DTNEG");
			sqlGeraScript.appendSql("       || ''', '''");
			sqlGeraScript.appendSql("       || DTENTSAI");
			sqlGeraScript.appendSql("       || ''', '''");
			sqlGeraScript.appendSql("       || AD_PEDPALM");
			sqlGeraScript.appendSql("       || ''', '''");
			sqlGeraScript.appendSql("       || 974");
			sqlGeraScript.appendSql("       || ''', '''");
			sqlGeraScript.appendSql("       || NVL((SELECT CODIGO");
			sqlGeraScript.appendSql("        FROM   AFV_ANDROID.T_CONDPAGTO@PROVIDER");
			sqlGeraScript.appendSql("        WHERE  CODIGO = CODTIPVENDA), 135)");
			sqlGeraScript.appendSql("       || ''', '''");
			sqlGeraScript.appendSql("       || TIPFRETE");
			sqlGeraScript.appendSql("       || ''', '''");
			sqlGeraScript.appendSql("       || VLRDESCTOT");
			sqlGeraScript.appendSql("       || ''', '''");
			sqlGeraScript.appendSql("       || VEN.CODVEND");
			sqlGeraScript.appendSql("       || ''', '''");
			sqlGeraScript.appendSql("       || EMPAFV.CODIGO /*CAB.CODEMPNEGOC*/");
			sqlGeraScript.appendSql("       || ''', '");
			sqlGeraScript.appendSql("       || 'NULL'");
			sqlGeraScript.appendSql("       || ', '''");
			sqlGeraScript.appendSql("       || CAB.CODEMP");
			sqlGeraScript.appendSql("       || ''', '''");
			sqlGeraScript.appendSql("       || TO_DATE(SYSDATE, 'DD/MM/RR')");
			sqlGeraScript.appendSql("       || ''', '");
			sqlGeraScript.appendSql("       || 'NULL'");
			sqlGeraScript.appendSql("       || ', '");
			sqlGeraScript.appendSql("       || 'NULL'");
			sqlGeraScript.appendSql("       || ', '''");
			sqlGeraScript.appendSql("       || CODTIPVENDA");
			sqlGeraScript.appendSql("       || ''', '");
			sqlGeraScript.appendSql("       || 'NULL'");
			sqlGeraScript.appendSql("       || ', '");
			sqlGeraScript.appendSql("       || '''PV'''");
			sqlGeraScript.appendSql("       || ', '");
			sqlGeraScript.appendSql("       || '''N'''");
			sqlGeraScript.appendSql("       || ') ' SQL ");
			sqlGeraScript.appendSql("FROM   TGFCAB CAB ");
			sqlGeraScript.appendSql("       INNER JOIN(SELECT CAB2.AD_NUNOTADUP, ");
			sqlGeraScript.appendSql("                         CAB2.NUNOTA,");
			sqlGeraScript.appendSql("                         CLIENTE.CODIGOCLIENTEPAI CODPARCAFV, ");
			sqlGeraScript.appendSql("                         CODEMPNEGOC");
			sqlGeraScript.appendSql("                  FROM   TGFCAB CAB2");
			sqlGeraScript.appendSql("                         LEFT JOIN AFV_ANDROID.TE_HISTPEDIDO@PROVIDER HISTPED ");
			sqlGeraScript.appendSql("                                ON( ORIGEMPEDIDO = CAB2.AD_PEDPALM ) ");
			sqlGeraScript.appendSql("                         LEFT JOIN AFV_ANDROID.TE_CLIENTE@PROVIDER CLIENTE ");
			sqlGeraScript.appendSql("                                ON( CLIENTE.CODIGOCLIENTEPAI = CAB2.CODPARC ) ");
			sqlGeraScript.appendSql("                  WHERE CAB2.AD_NUNOTADUP IS NOT NULL ");
			sqlGeraScript.appendSql("                   AND CAB2.DTNEG >= ( SYSDATE - 2 ) ");
			sqlGeraScript.appendSql("                   AND ORIGEMPEDIDO IS NULL ");
			sqlGeraScript.appendSql("                   AND CLIENTE.CODIGOCLIENTEPAI IS NOT NULL ");
			sqlGeraScript.appendSql("                  GROUP  BY CAB2.AD_NUNOTADUP, ");
			sqlGeraScript.appendSql("                            CLIENTE.CODIGOCLIENTEPAI, ");
			sqlGeraScript.appendSql("                            CODEMPNEGOC, ");
			sqlGeraScript.appendSql("                            CAB2.NUNOTA ");
			sqlGeraScript.appendSql("                  ORDER  BY AD_NUNOTADUP)VENDADUPLIC ");
			sqlGeraScript.appendSql("               ON ( VENDADUPLIC.NUNOTA = CAB.NUNOTA ");
			sqlGeraScript.appendSql("                    AND VENDADUPLIC.CODEMPNEGOC = CAB.CODEMPNEGOC ) ");
			sqlGeraScript.appendSql("       LEFT JOIN AD_TGFEPF EPF ");
			sqlGeraScript.appendSql("              ON( EPF.ANALITICO = 'S' ");
			sqlGeraScript.appendSql("                  AND EPF.ATIVO = 'S' ");
			sqlGeraScript.appendSql(
					"                  AND SUBSTR(EPF.CODREG, 2, LENGTH(EPF.CODREG) - 1) = CAB.CODEMPNEGOC ) ");
			sqlGeraScript.appendSql("       LEFT JOIN TGFVEN VEN ");
			sqlGeraScript.appendSql("              ON ( EPF.CODVEND = VEN.CODVEND ");
			sqlGeraScript.appendSql("                   AND ( VEN.TIPVEND = 'V' ");
			sqlGeraScript.appendSql("                          OR VEN.TIPVEND = 'S' ");
			sqlGeraScript.appendSql("                          OR VEN.TIPVEND IS NULL ) ) ");
			sqlGeraScript.appendSql("       LEFT JOIN AFV_ANDROID.T_EMPRESA@PROVIDER EMPAFV ");
			sqlGeraScript.appendSql("              ON( EMPAFV.CODIGOEMPRESAESP = VEN.CODVEND ) ");
			sqlGeraScript.appendSql("WHERE  CAB.TIPMOV IN( 'V', 'D' ) ");
			sqlGeraScript.appendSql("       AND EMPAFV.CODIGOEMPRESAESP IS NOT NULL ");
			sqlGeraScript.appendSql("ORDER BY CAB.NUNOTA ");

			try {

				ResultSet rs = sqlGeraScript.executeQuery();

				NativeSql execScript = new NativeSql(jdbc);
				while (rs.next()) {
					execScript = new NativeSql(jdbc);
					execScript.appendSql(rs.getString("SQL"));
					execScript.executeUpdate();
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	private void insereHistoricoItemPedidosAFV() throws Exception {

		JdbcWrapper jdbc = null;
		EntityFacade entity = EntityFacadeFactory.getDWFFacade();

		jdbc = entity.getJdbcWrapper();

		try {
			jdbc.openSession();
			NativeSql sqlGeraScript = new NativeSql(jdbc);

			sqlGeraScript.appendSql("SELECT CAB.NUNOTA, ");
			sqlGeraScript.appendSql("       PED.NUMPEDIDOEMP, ");
			sqlGeraScript.appendSql("       CAB.AD_PEDPALM, ");
			sqlGeraScript.appendSql(
					"'Insert into AFV_ANDROID.te_histpedidoitem@PROVIDER (NUMPEDIDOEMP,CODIGOPRODUTO,DESCRICAOPRODUTO,QTDEVENDA,QTDEBONIFICADA,QTDECORTADA,DESCONTO,VALORVENDA,CODIGOCONDPAGTO,CODIGOEMPRESAESP,CODIGOEMPRESA,DATAATUALIZACAO,MOTIVOCORTE,VALORDESCONTO,TOTALPESO,VRTOTALCOMISSAO,SALDOQTDEVENDA,FLAG_GERARVERBA)  values ( ' ");
			sqlGeraScript.appendSql("|| '''' ");
			sqlGeraScript.appendSql("|| PED.NUMPEDIDOEMP ");
			sqlGeraScript.appendSql("|| '''' ");
			sqlGeraScript.appendSql("|| ' , ' ");
			sqlGeraScript.appendSql("|| '''' ");
			sqlGeraScript.appendSql("|| ITE.CODPROD ");
			sqlGeraScript.appendSql("|| '''' ");
			sqlGeraScript.appendSql("|| ' , ''' ");
			sqlGeraScript.appendSql("|| PROD.DESCRPROD ");
			sqlGeraScript.appendSql("|| ' (' ");
			sqlGeraScript.appendSql("|| ITE.CODVOL ");
			sqlGeraScript.appendSql("|| ')' ");
			sqlGeraScript.appendSql("|| ''' , ''' ");
			sqlGeraScript.appendSql("|| ITE.QTDNEG ");
			sqlGeraScript.appendSql("|| ''' , ''' ");
			sqlGeraScript.appendSql("|| 0 ");
			sqlGeraScript.appendSql("|| ''' , ''' ");
			sqlGeraScript.appendSql("|| 0 ");
			sqlGeraScript.appendSql("|| ''' , ''' ");
			sqlGeraScript.appendSql("|| ITE.VLRDESC ");
			sqlGeraScript.appendSql("|| ''' , ''' ");
			sqlGeraScript.appendSql("|| NVL(Round(Sum(( ITE.VLRTOT - ITE.VLRDESC ) * ITE.ATUALESTOQUE *- 1), 2), 0) ");
			sqlGeraScript.appendSql("|| ''' , ' ");
			sqlGeraScript.appendSql("|| 'null' ");
			sqlGeraScript.appendSql("|| ' , ' ");
			sqlGeraScript.appendSql("|| 'null' ");
			sqlGeraScript.appendSql("|| ' , ' ");
			sqlGeraScript.appendSql("|| 'null' ");
			sqlGeraScript.appendSql("|| ' , ''' ");
			sqlGeraScript.appendSql("|| PED.DATAATUALIZACAO ");
			sqlGeraScript.appendSql("|| ''' , ' ");
			sqlGeraScript.appendSql("|| 'NULL' ");
			sqlGeraScript.appendSql("|| ' , ''' ");
			sqlGeraScript.appendSql("|| ITE.VLRDESC ");
			sqlGeraScript.appendSql("|| ''' , ''' ");
			sqlGeraScript.appendSql("|| NVL(ITE.PESO, 0) ");
			sqlGeraScript.appendSql("|| ''' , ''' ");
			sqlGeraScript.appendSql("|| 0 ");
			sqlGeraScript.appendSql("|| ''' , ''' ");
			sqlGeraScript.appendSql("|| 0 ");
			sqlGeraScript.appendSql("|| ''' , ' ");
			sqlGeraScript.appendSql("|| 'NULL' ");
			sqlGeraScript.appendSql("|| ') ' SQL ");
			sqlGeraScript.appendSql("FROM   TGFITE ITE ");
			sqlGeraScript.appendSql("       LEFT JOIN TGFPRO PROD ");
			sqlGeraScript.appendSql("              ON( PROD.CODPROD = ITE.CODPROD ) ");
			sqlGeraScript.appendSql("       LEFT JOIN TGFCAB CAB ");
			sqlGeraScript.appendSql("              ON( CAB.NUNOTA = ITE.NUNOTA ) ");
			sqlGeraScript.appendSql("       INNER JOIN(SELECT HISTPED.NUMPEDIDOAFV, ");
			sqlGeraScript.appendSql("                         HISTPED.NUMPEDIDOEMP, ");
			sqlGeraScript.appendSql("                         HISTPED.ORIGEMPEDIDO, ");
			sqlGeraScript.appendSql("                         AD_NUNOTADUP, ");
			sqlGeraScript.appendSql("                         QDEITENSENC, ");
			sqlGeraScript.appendSql("                         HISTPED.DATAATUALIZACAO ");
			sqlGeraScript.appendSql("                  FROM   AFV_ANDROID.TE_HISTPEDIDO@PROVIDER HISTPED ");
			sqlGeraScript.appendSql("                         OUTER APPLY(SELECT AD_PEDPALM, ");
			sqlGeraScript.appendSql("                                            AD_NUNOTADUP ");
			sqlGeraScript.appendSql("                                     FROM   TGFCAB CAB ");
			sqlGeraScript.appendSql("                                     WHERE  ORIGEMPEDIDO IS NOT NULL ");
			sqlGeraScript.appendSql("                                            AND ORIGEMPEDIDO = CAB.AD_PEDPALM ");
			sqlGeraScript.appendSql("                                            AND CAB.DTNEG >= ( SYSDATE - 2 ) ");
			sqlGeraScript.appendSql("                                            AND AD_NUNOTADUP IS NOT NULL ");
			sqlGeraScript.appendSql("                                     GROUP  BY AD_PEDPALM, ");
			sqlGeraScript.appendSql("                                               AD_NUNOTADUP)CAB2 ");
			sqlGeraScript.appendSql("                         OUTER APPLY(SELECT Count(1) QDEITENSENC ");
			sqlGeraScript.appendSql(
					"                                     FROM   AFV_ANDROID.TE_HISTPEDIDOITEM@PROVIDER HISTPEDITEM ");
			sqlGeraScript.appendSql(
					"                                     WHERE  HISTPEDITEM.NUMPEDIDOEMP = HISTPED.NUMPEDIDOEMP)ITEPEDAFV ");
			sqlGeraScript.appendSql("                  WHERE  ORIGEMPEDIDO IS NOT NULL ");
			sqlGeraScript.appendSql("                         AND HISTPED.NUMPEDIDOAFV LIKE 'E%') PED ");
			sqlGeraScript.appendSql("               ON( PED.AD_NUNOTADUP = CAB.NUNOTA ) ");
			sqlGeraScript.appendSql("WHERE  PED.AD_NUNOTADUP = CAB.NUNOTA ");
			sqlGeraScript.appendSql("  AND  QDEITENSENC = 0   ");
			sqlGeraScript.appendSql("GROUP  BY ITE.NUNOTA, ");
			sqlGeraScript.appendSql("          PED.NUMPEDIDOEMP, ");
			sqlGeraScript.appendSql("          PED.NUMPEDIDOAFV, ");
			sqlGeraScript.appendSql("          PROD.DESCRPROD, ");
			sqlGeraScript.appendSql("          ITE.QTDNEG, ");
			sqlGeraScript.appendSql("          ITE.VLRDESC, ");
			sqlGeraScript.appendSql("          PED.DATAATUALIZACAO, ");
			sqlGeraScript.appendSql("          ITE.VLRDESC, ");
			sqlGeraScript.appendSql("          ITE.PESO, ");
			sqlGeraScript.appendSql("          CAB.NUNOTA, ");
			sqlGeraScript.appendSql("          CAB.AD_PEDPALM, ");
			sqlGeraScript.appendSql("          ITE.CODPROD, ");
			sqlGeraScript.appendSql("          ITE.CODVOL  ");

			try {

				ResultSet rs = sqlGeraScript.executeQuery();

				NativeSql execScript = new NativeSql(jdbc);
				while (rs.next()) {
					execScript = new NativeSql(jdbc);
					execScript.appendSql(rs.getString("SQL"));
					execScript.executeUpdate();
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}
