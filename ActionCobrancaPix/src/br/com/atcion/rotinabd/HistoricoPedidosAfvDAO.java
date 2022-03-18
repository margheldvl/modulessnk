package br.com.atcion.rotinabd;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.cuckoo.core.ScheduledAction;
import org.cuckoo.core.ScheduledActionContext;

import br.com.sankhya.jape.EntityFacade;
import br.com.sankhya.jape.dao.JdbcWrapper;
import br.com.sankhya.jape.sql.NativeSql;
import br.com.sankhya.modelcore.util.EntityFacadeFactory;

public class HistoricoPedidosAfvDAO {

	private static Integer qdeDiasExport = 30;

	public void deletaCabecalhoHistoricoPedAFV() throws Exception {

		JdbcWrapper jdbc = null;
		EntityFacade entity = EntityFacadeFactory.getDWFFacade();

		jdbc = entity.getJdbcWrapper();

		jdbc.openSession();
		NativeSql sql = new NativeSql(jdbc);

		sql.appendSql("DELETE FROM AFV_ANDROID.TE_HISTPEDIDO@PROVIDER ");
		sql.appendSql("WHERE  NUMPEDIDOEMP IN (SELECT NUMPEDIDOEMP ");
		sql.appendSql("                        FROM   AFV_ANDROID.TE_HISTPEDIDO@PROVIDER PEDPROV ");
		sql.appendSql("                               INNER JOIN (SELECT NRO_AFV, ");
		sql.appendSql("                                                  MAX(HST2.NUNOTA) ");
		sql.appendSql(
				"                                                    KEEP (DENSE_RANK FIRST ORDER BY HST2.SEQUENCIA DESC) NUNOTA ");
		sql.appendSql("                                           FROM   SANKHYA.VGFHISTPED HST2 ");
		sql.appendSql("                                                  LEFT JOIN SANKHYA.TGFCAB CAB2 ");
		sql.appendSql("                                                         ON CAB2.NUNOTA = HST2.NUNOTA ");
		sql.appendSql("                                           WHERE  CAB2.TIPMOV = 'P' ");
		sql.appendSql("                                                  AND HST2.DTOCORRENCIA >= SYSDATE - "
				+ qdeDiasExport + " ");
		sql.appendSql("                                                  AND HST2.STATUS IS NOT NULL ");
		sql.appendSql("                                           GROUP  BY HST2.NRO_AFV ");
		sql.appendSql("                                           HAVING MIN(HST2.NUNOTA) = MAX(HST2.NUNOTA)) P ");
		sql.appendSql("                                       ON( P.NRO_AFV = PEDPROV.NUMPEDIDOEMP ))");
		// sql.appendSql(" WHERE ORIGEMPEDIDO LIKE '%EXP. %') ");

		try {
			sql.executeUpdate();
		} catch (IOException | SQLException e) {
			throw e;
		}
	}

	public void deletaItensCabecalhoHistoricoPedAFV() throws Exception {

		JdbcWrapper jdbc = null;
		EntityFacade entity = EntityFacadeFactory.getDWFFacade();

		jdbc = entity.getJdbcWrapper();

		jdbc.openSession();
		NativeSql sql = new NativeSql(jdbc);

		sql.appendSql("DELETE FROM AFV_ANDROID.TE_HISTPEDIDOITEM@PROVIDER HSTITE ");
		sql.appendSql("WHERE  NUMPEDIDOEMP NOT IN(SELECT NUMPEDIDOEMP ");
		sql.appendSql("                           FROM   AFV_ANDROID.TE_HISTPEDIDO@PROVIDER PEDPROV ");
		sql.appendSql("                           WHERE  PEDPROV.NUMPEDIDOEMP = HSTITE.NUMPEDIDOEMP) ");

		try {
			sql.executeUpdate();
		} catch (IOException | SQLException e) {
			throw e;
		}
	}

	public void insereCabecHistoricoPedidosAFV() throws Exception {

		JdbcWrapper jdbc = null;
		EntityFacade entity = EntityFacadeFactory.getDWFFacade();

		jdbc = entity.getJdbcWrapper();

		jdbc.openSession();
		NativeSql sql = new NativeSql(jdbc);

		sql.appendSql("INSERT INTO AFV_ANDROID.TE_HISTPEDIDO@PROVIDER ");
		sql.appendSql("SELECT CAST (HST.NRO_AFV AS VARCHAR (30))                        AS NumPedidoEmp, ");
		sql.appendSql("       CAST (HST.NRO_AFV AS VARCHAR (30))                        AS NumPedidoAFV, ");
		sql.appendSql("       CAST (HST.CODPARC AS VARCHAR (20))                        AS CodigoCliente, ");
		sql.appendSql("       SUBSTR (HST.STATUS, 1, 30)                                AS StatusPedido, ");
		sql.appendSql("       CAST (HST.VLRNOTA AS NUMBER (18, 6))                      AS ValorPedido, ");
		sql.appendSql(
				"       CAST ((SELECT ROUND (SUM (( ITE.VLRUNIT - ( ITE.VLRDESC / ITE.QTDNEG ) ) * ITE.QTDENTREGUE), 2) ");
		sql.appendSql("              FROM   TGFITE ITE ");
		sql.appendSql("              WHERE  ITE.NUNOTA = HST.NUNOTA) AS NUMBER (18, 6)) AS ValorFaturado, ");
		sql.appendSql("       HST.DTOCORRENCIA                                          AS DataPedido, ");
		sql.appendSql("       NULL                                                      AS DataPrevistaEntrega, ");
		sql.appendSql("       'EXP. SNK'                                               AS OrigemPedido, ");
		sql.appendSql("       CAST (CAB.CODTIPOPER AS VARCHAR (10))                     AS CodigoTipoPedido, ");
		sql.appendSql("       CAST (HST.CODTIPVENDA AS VARCHAR (10))                    AS CodigoCondPagto, ");
		sql.appendSql("       DECODE (CAB.CIF_FOB, 'C', 'CIF', ");
		sql.appendSql("                            'F', 'FOB')                          AS DescricaoFrete, ");
		sql.appendSql("       CAST (0 AS NUMBER (18, 6))                                AS DescontoCondPagto, ");
		sql.appendSql("       CAST (NVL (EPF.CODVEND, EXC.CODVEND) AS VARCHAR2 (10))    AS CodigoVendedorEsp, ");
		sql.appendSql("       NULL                                                      AS CODIGOVENDEDOR, ");
		sql.appendSql("       NULL                                                      AS CodigoEmpresaEsp, ");
		sql.appendSql("       NULL                                                      CODIGOEMPRESA, ");
		sql.appendSql("       NULL                                                      DATAATUALIZACAO, ");
		sql.appendSql("       NULL                                                      AS FlagStatus, ");
		sql.appendSql("       CAST (0 AS NUMBER (6))                                    AS NumPedidoAfvserver, ");
		sql.appendSql("       CAST (TPV.AD_CODFRMPGPLM AS VARCHAR (10))                 AS CEsp_CodigoFormaPagto, ");
		sql.appendSql("       CAST (CAB.CODEMP AS VARCHAR (10))                         AS CEsp_CodigoUnidFat, ");
		sql.appendSql("       CASE ");
		sql.appendSql("         WHEN CAB.CODEMP <> 1 THEN 'PE' ");
		sql.appendSql("         ELSE 'PV' ");
		sql.appendSql("       END                                                       AS CEsp_Modalidade, ");
		sql.appendSql("       CASE ");
		sql.appendSql("         WHEN HST.SEQUENCIA IN ( 3, 4 ) THEN 'S' ");
		sql.appendSql("         ELSE 'N' ");
		sql.appendSql("       END                                                       AS CESP_PEDDISP ");
		sql.appendSql("FROM   VGFHISTPED HST ");
		sql.appendSql("       LEFT JOIN TGFCAB CAB ");
		sql.appendSql("              ON CAB.NUNOTA = HST.NUNOTA ");
		sql.appendSql("       LEFT JOIN TGFCAB_EXC EXC ");
		sql.appendSql("              ON EXC.NUNOTA = HST.NUNOTA ");
		sql.appendSql("       LEFT JOIN TSIUSU USU ");
		sql.appendSql("              ON USU.CODUSU = CAB.CODUSUINC ");
		sql.appendSql("       LEFT JOIN TGFTPV TPV ");
		sql.appendSql("              ON TPV.CODTIPVENDA = CAB.CODTIPVENDA ");
		sql.appendSql("                 AND CAB.DHTIPVENDA = TPV.DHALTER ");
		sql.appendSql("       LEFT JOIN AD_TGFEPF EPF ");
		sql.appendSql("              ON SUBSTR (EPF.CODREG, 2, 3) = CAB.CODEMPNEGOC ");
		sql.appendSql("                 AND EPF.ATIVO = 'S' ");
		sql.appendSql("       INNER JOIN AFV_ANDROID.TE_CLIENTE@PROVIDER CLI ");
		sql.appendSql("               ON CLI.CODIGO = HST.CODPARC ");
		sql.appendSql("       LEFT JOIN AFV_ANDROID.TE_HISTPEDIDO@PROVIDER PEDPROV ");
		sql.appendSql("              ON( PEDPROV.NUMPEDIDOEMP = CAB.AD_PEDPALM ) ");
		sql.appendSql("       INNER JOIN (SELECT MIN(HST2.NUNOTA)                                       MENORNOTA, ");
		sql.appendSql("                          MAX(HST2.NUNOTA) ");
		sql.appendSql("                            KEEP (DENSE_RANK FIRST ORDER BY HST2.SEQUENCIA DESC) NUNOTA ");
		sql.appendSql("                   FROM   SANKHYA.VGFHISTPED HST2 ");
		sql.appendSql("                          LEFT JOIN SANKHYA.TGFCAB CAB2 ");
		sql.appendSql("                                 ON CAB2.NUNOTA = HST2.NUNOTA ");
		sql.appendSql("                   WHERE  CAB2.TIPMOV = 'P' ");
		sql.appendSql("                          AND HST2.DTOCORRENCIA >= SYSDATE - " + qdeDiasExport + "  ");
		sql.appendSql("                          AND HST2.STATUS IS NOT NULL ");
		sql.appendSql("                   GROUP  BY HST2.NRO_AFV ");
		sql.appendSql("                   HAVING MIN(HST2.NUNOTA) = MAX(HST2.NUNOTA)) P ");
		sql.appendSql("               ON( P.NUNOTA = HST.NUNOTA ) ");
		sql.appendSql("WHERE  HST.DTOCORRENCIA >= SYSDATE - " + qdeDiasExport + "  ");
		sql.appendSql("       AND HST.STATUS IS NOT NULL ");
		sql.appendSql("       AND HST.TIPMOV = 'P' ");
		sql.appendSql("       AND HST.CODTIPVENDA NOT IN ( 304, 372 ) ");
		sql.appendSql("       AND HST.SEQUENCIA = (SELECT MAX (SEQUENCIA) ");
		sql.appendSql("                            FROM   VGFHISTPED ");
		sql.appendSql("                            WHERE  NUNOTA = HST.NUNOTA ");
		sql.appendSql("                                   AND TIPMOV = 'P' ");
		sql.appendSql("                                   AND STATUS IS NOT NULL) ");
		sql.appendSql("       AND NOT EXISTS (SELECT 1 ");
		sql.appendSql("                       FROM   AFV_ANDROID.TE_HISTPEDIDO@PROVIDER ");
		sql.appendSql("                       WHERE  NUMPEDIDOEMP = HST.NRO_AFV ");
		sql.appendSql("                              AND STATUSPEDIDO = HST.STATUS ");
		sql.appendSql("                              AND CODIGOVENDEDORESP = NVL (EPF.CODVEND, EXC.CODVEND) ");
		sql.appendSql("                              AND CESP_PEDDISP = CASE ");
		sql.appendSql("                                                   WHEN HST.SEQUENCIA IN ( 3, 4 ) THEN 'S' ");
		sql.appendSql("                                                   ELSE 'N' ");
		sql.appendSql("                                                 END) ");
		sql.appendSql("       AND PEDPROV.NUMPEDIDOEMP IS NULL  ");

		try {
			sql.executeUpdate();
		} catch (IOException | SQLException e) {
			throw e;
		}

	}

	public void insereCabecHistoricoPedidosRefaturadosAFV() throws Exception {

		JdbcWrapper jdbc = null;
		EntityFacade entity = EntityFacadeFactory.getDWFFacade();

		jdbc = entity.getJdbcWrapper();

		jdbc.openSession();
		NativeSql sql = new NativeSql(jdbc);

		sql.appendSql("INSERT INTO AFV_ANDROID.TE_HISTPEDIDO@PROVIDER ");
		sql.appendSql("SELECT CAST (HST.NRO_AFV AS VARCHAR (30))                             AS NumPedidoEmp, ");
		sql.appendSql("       CAST (HST.NRO_AFV AS VARCHAR (30))                             AS NumPedidoAFV, ");
		sql.appendSql("       CAST (HST.CODPARC AS VARCHAR (20))                             AS CodigoCliente, ");
		sql.appendSql("       SUBSTR (HST.STATUS, 1, 30)                                     AS StatusPedido, ");
		sql.appendSql("       CAST (SUM(HST.VLRNOTA) AS NUMBER (18, 6))                      AS ValorPedido, ");
		sql.appendSql(
				"       CAST (SUM((SELECT ROUND (SUM (( ITE.VLRUNIT - ( ITE.VLRDESC / ITE.QTDNEG ) ) * ITE.QTDENTREGUE), 2) ");
		sql.appendSql("                  FROM   TGFITE ITE ");
		sql.appendSql("                  WHERE  ITE.NUNOTA = HST.NUNOTA)) AS NUMBER (18, 6)) AS ValorFaturado, ");
		sql.appendSql("       HST.DTOCORRENCIA                                               AS DataPedido, ");
		sql.appendSql("       NULL                                                           AS DataPrevistaEntrega, ");
		sql.appendSql("       'EXP. SNK'                                                    AS OrigemPedido, ");
		sql.appendSql("       CAST (CAB.CODTIPOPER AS VARCHAR (10))                          AS CodigoTipoPedido, ");
		sql.appendSql("       CAST (HST.CODTIPVENDA AS VARCHAR (10))                         AS CodigoCondPagto, ");
		sql.appendSql("       DECODE (CAB.CIF_FOB, 'C', 'CIF', ");
		sql.appendSql("                            'F', 'FOB')                               AS DescricaoFrete, ");
		sql.appendSql("       CAST (0 AS NUMBER (18, 6))                                     AS DescontoCondPagto, ");
		sql.appendSql("       CAST (NVL (EPF.CODVEND, EXC.CODVEND) AS VARCHAR2 (10))         AS CodigoVendedorEsp, ");
		sql.appendSql("       NULL                                                           AS CODIGOVENDEDOR, ");
		sql.appendSql("       NULL                                                           AS CodigoEmpresaEsp, ");
		sql.appendSql("       NULL                                                           CODIGOEMPRESA, ");
		sql.appendSql("       NULL                                                           DATAATUALIZACAO, ");
		sql.appendSql("       NULL                                                           AS FlagStatus, ");
		sql.appendSql("       CAST (0 AS NUMBER (6))                                         AS NumPedidoAfvserver, ");
		sql.appendSql(
				"       CAST (TPV.AD_CODFRMPGPLM AS VARCHAR (10))                      AS CEsp_CodigoFormaPagto, ");
		sql.appendSql("       CAST (CAB.CODEMP AS VARCHAR (10))                              AS CEsp_CodigoUnidFat, ");
		sql.appendSql("       CASE ");
		sql.appendSql("         WHEN CAB.CODEMP <> 1 THEN 'PE' ");
		sql.appendSql("         ELSE 'PV' ");
		sql.appendSql("       END                                                            AS CEsp_Modalidade, ");
		sql.appendSql("       CASE ");
		sql.appendSql("         WHEN HST.SEQUENCIA IN ( 3, 4 ) THEN 'S' ");
		sql.appendSql("         ELSE 'N' ");
		sql.appendSql("       END                                                            AS CESP_PEDDISP ");
		sql.appendSql("FROM   VGFHISTPED HST ");
		sql.appendSql("       LEFT JOIN TGFCAB CAB ");
		sql.appendSql("              ON CAB.NUNOTA = HST.NUNOTA ");
		sql.appendSql("       LEFT JOIN TGFCAB_EXC EXC ");
		sql.appendSql("              ON EXC.NUNOTA = HST.NUNOTA ");
		sql.appendSql("       LEFT JOIN TSIUSU USU ");
		sql.appendSql("              ON USU.CODUSU = CAB.CODUSUINC ");
		sql.appendSql("       LEFT JOIN TGFTPV TPV ");
		sql.appendSql("              ON TPV.CODTIPVENDA = CAB.CODTIPVENDA ");
		sql.appendSql("                 AND CAB.DHTIPVENDA = TPV.DHALTER ");
		sql.appendSql("       LEFT JOIN AD_TGFEPF EPF ");
		sql.appendSql("              ON SUBSTR (EPF.CODREG, 2, 3) = CAB.CODEMPNEGOC ");
		sql.appendSql("                 AND EPF.ATIVO = 'S' ");
		sql.appendSql("       INNER JOIN AFV_ANDROID.TE_CLIENTE@PROVIDER CLI ");
		sql.appendSql("               ON CLI.CODIGO = HST.CODPARC ");
		sql.appendSql("       INNER JOIN (SELECT MIN(HST2.NUNOTA)                                       MENORNOTA, ");
		sql.appendSql("                          HST2.NRO_AFV, ");
		sql.appendSql("                          MAX(HST2.NUNOTA) ");
		sql.appendSql("                            KEEP (DENSE_RANK FIRST ORDER BY HST2.SEQUENCIA DESC) NUNOTA ");
		sql.appendSql("                   FROM   SANKHYA.VGFHISTPED HST2 ");
		sql.appendSql("                          LEFT JOIN SANKHYA.TGFCAB CAB2 ");
		sql.appendSql("                                 ON CAB2.NUNOTA = HST2.NUNOTA ");
		sql.appendSql("                   WHERE  CAB2.TIPMOV = 'P' ");
		sql.appendSql("                          AND HST2.DTOCORRENCIA >= SYSDATE - 22 ");
		sql.appendSql("                          AND HST2.STATUS IS NOT NULL ");
		sql.appendSql("                   GROUP  BY HST2.NRO_AFV ");
		sql.appendSql("                   HAVING MIN(HST2.NUNOTA) <> MAX(HST2.NUNOTA)) P ");
		sql.appendSql("               ON( P.NRO_AFV = HST.NRO_AFV ) ");
		sql.appendSql("WHERE  HST.DTOCORRENCIA >= SYSDATE - 22 ");
		sql.appendSql("       AND HST.STATUS IS NOT NULL ");
		sql.appendSql("       AND HST.TIPMOV = 'P' ");
		sql.appendSql("       AND HST.CODTIPVENDA NOT IN ( 304, 372 ) ");
		sql.appendSql("       AND HST.SEQUENCIA = (SELECT MAX (SEQUENCIA) ");
		sql.appendSql("                            FROM   VGFHISTPED ");
		sql.appendSql("                            WHERE  NUNOTA = HST.NUNOTA ");
		sql.appendSql("                                   AND TIPMOV = 'P' ");
		sql.appendSql("                                   AND STATUS IS NOT NULL) ");
		sql.appendSql("       AND NOT EXISTS (SELECT 1 ");
		sql.appendSql("                       FROM   AFV_ANDROID.TE_HISTPEDIDO@PROVIDER ");
		sql.appendSql("                       WHERE  NUMPEDIDOEMP = HST.NRO_AFV ");
		sql.appendSql("                              AND STATUSPEDIDO = HST.STATUS ");
		sql.appendSql("                              AND CODIGOVENDEDORESP = NVL (EPF.CODVEND, EXC.CODVEND) ");
		sql.appendSql("                              AND CESP_PEDDISP = CASE ");
		sql.appendSql("                                                   WHEN HST.SEQUENCIA IN ( 3, 4 ) THEN 'S' ");
		sql.appendSql("                                                   ELSE 'N' ");
		sql.appendSql("                                                 END) ");
		sql.appendSql("       AND HST.SEQUENCIA IN( 3, 4 ) ");
		sql.appendSql("GROUP  BY HST.NRO_AFV, ");
		sql.appendSql("          HST.CODPARC, ");
		sql.appendSql("          HST.STATUS, ");
		sql.appendSql("          HST.DTOCORRENCIA, ");
		sql.appendSql("          CAB.CODTIPOPER, ");
		sql.appendSql("          HST.CODTIPVENDA, ");
		sql.appendSql("          HST.SEQUENCIA, ");
		sql.appendSql("          CAB.CIF_FOB, ");
		sql.appendSql("          NVL(EPF.CODVEND, EXC.CODVEND), ");
		sql.appendSql("          TPV.AD_CODFRMPGPLM, ");
		sql.appendSql("          CAB.CODEMP, ");
		sql.appendSql("          CAB.CODEMP  ");

		try {
			sql.executeUpdate();
		} catch (IOException | SQLException e) {
			throw e;
		}

	}

	public void insereItemHistoricoPedidosAFV() throws Exception {

		JdbcWrapper jdbc = null;
		EntityFacade entity = EntityFacadeFactory.getDWFFacade();

		jdbc = entity.getJdbcWrapper();

		jdbc.openSession();
		NativeSql sql = new NativeSql(jdbc);

		sql.appendSql("INSERT INTO AFV_ANDROID.TE_HISTPEDIDOITEM@PROVIDER ");
		sql.appendSql("SELECT HST.NUMPEDIDOEMP                                   AS NumPedidoEmp, ");
		sql.appendSql("       ITE.CODPROD                                        AS CodigoProduto, ");
		sql.appendSql("       PRO.DESCRPROD                                      AS DescricaoProduto, ");
		sql.appendSql("       ITE.QTDNEG                                         AS QtdeVenda, ");
		sql.appendSql("       NULL                                               AS QtdeBonificada, ");
		sql.appendSql("       CAST(ITE.QTDNEG - ITE.QTDENTREGUE AS FLOAT)        AS QtdeCortada, ");
		sql.appendSql("       ITE.PERCDESC                                       AS Desconto, ");
		sql.appendSql("       ITE.VLRTOT                                         AS ValorVenda, ");
		sql.appendSql("       NULL                                               AS CodigoCondPagto, ");
		sql.appendSql("       NULL                                               AS CodigoEmpresaEsp, ");
		// sql.appendSql(" NULL AS CodigoEmpresa, ");
		sql.appendSql("       HST.CODIGOVENDEDOR                                 AS CodigoEmpresa, ");
		sql.appendSql("       NULL                                               AS DATAATUALIZACAO, ");
		sql.appendSql("       CASE ");
		sql.appendSql("         WHEN ( ITE.QTDNEG - ITE.QTDENTREGUE ) = 0 THEN '' ");
		sql.appendSql("         ELSE 'SEM ESTOQUE' ");
		sql.appendSql("       END                                                AS MotivoCorte, ");
		sql.appendSql("       '0'                                                AS ValorDesconto, ");
		sql.appendSql("       CAST(( ITE.QTDENTREGUE * PRO.PESOBRUTO ) AS FLOAT) AS TotalPeso, ");
		sql.appendSql("       NULL                                               AS VrTotalComissao, ");
		sql.appendSql("       NULL                                               AS SaldoQtdeVenda, ");
		sql.appendSql("       NULL                                               AS FLAG_GERARVERBA ");
		sql.appendSql("FROM   AFV_ANDROID.TE_HISTPEDIDO@PROVIDER HST ");
		sql.appendSql("       INNER JOIN SANKHYA.TGFCAB CAB ");
		sql.appendSql("               ON CAB.AD_PEDPALM = HST.NUMPEDIDOAFV ");
		sql.appendSql("                  AND CAB.TIPMOV = 'P' ");
		sql.appendSql("       INNER JOIN SANKHYA.TGFITE ITE ");
		sql.appendSql("               ON CAB.NUNOTA = ITE.NUNOTA ");
		sql.appendSql("       INNER JOIN SANKHYA.TGFPRO PRO ");
		sql.appendSql("               ON PRO.CODPROD = ITE.CODPROD ");
		sql.appendSql("       LEFT JOIN AFV_ANDROID.TE_HISTPEDIDOITEM@PROVIDER ITEPROVIDER ");
		sql.appendSql("              ON( ITEPROVIDER.NUMPEDIDOEMP = HST.NUMPEDIDOEMP ");
		sql.appendSql("                  AND ITEPROVIDER.CODIGOPRODUTO = ITE.CODPROD ) ");
		sql.appendSql("       INNER JOIN (SELECT MIN(HST2.NUNOTA)                                       MENORNOTA, ");
		sql.appendSql("                          MAX(HST2.NUNOTA) ");
		sql.appendSql("                            KEEP (DENSE_RANK FIRST ORDER BY HST2.SEQUENCIA DESC) NUNOTA ");
		sql.appendSql("                   FROM   SANKHYA.VGFHISTPED HST2 ");
		sql.appendSql("                          LEFT JOIN SANKHYA.TGFCAB CAB2 ");
		sql.appendSql("                                 ON CAB2.NUNOTA = HST2.NUNOTA ");
		sql.appendSql("                   WHERE  CAB2.TIPMOV = 'P' ");
		sql.appendSql("                          AND HST2.DTOCORRENCIA >= SYSDATE - 30 ");
		sql.appendSql("                          AND HST2.STATUS IS NOT NULL ");
		sql.appendSql("                   GROUP  BY HST2.NRO_AFV ");
		sql.appendSql("                   HAVING MIN(HST2.NUNOTA) = MAX(HST2.NUNOTA)) P ");
		sql.appendSql("               ON( P.NUNOTA = CAB.NUNOTA ) ");
		sql.appendSql("WHERE  HST.DATAPEDIDO >= SYSDATE - 30 ");
		sql.appendSql("       AND ORIGEMPEDIDO = 'EXP. SNK' ");
		sql.appendSql("       AND ITEPROVIDER.NUMPEDIDOEMP IS NULL ");
		sql.appendSql("       AND ITEPROVIDER.CODIGOPRODUTO IS NULL  ");

		try {
			sql.executeUpdate();
		} catch (IOException | SQLException e) {
			throw e;
		}

	}

	public void insereItemHistoricoPedidosRefaturadosAFV() throws Exception {

		JdbcWrapper jdbc = null;
		EntityFacade entity = EntityFacadeFactory.getDWFFacade();

		jdbc = entity.getJdbcWrapper();

		jdbc.openSession();
		NativeSql sql = new NativeSql(jdbc);

		sql.appendSql("INSERT INTO AFV_ANDROID.TE_HISTPEDIDOITEM@PROVIDER ");
		sql.appendSql("SELECT HST.NUMPEDIDOEMP                                   AS NumPedidoEmp, ");
		sql.appendSql("       ITE.CODPROD                                        AS CodigoProduto, ");
		sql.appendSql("       '(EXP)' ");
		sql.appendSql("       || PRO.DESCRPROD                                   AS DescricaoProduto, ");
		sql.appendSql("       ITE.QTDNEG                                         AS QtdeVenda, ");
		sql.appendSql("       NULL                                               AS QtdeBonificada, ");
		sql.appendSql("       CAST(ITE.QTDNEG - ITE.QTDENTREGUE AS FLOAT)        AS QtdeCortada, ");
		sql.appendSql("       ITE.PERCDESC                                       AS Desconto, ");
		sql.appendSql("       ITE.VLRTOT                                         AS ValorVenda, ");
		sql.appendSql("       NULL                                               AS CodigoCondPagto, ");
		sql.appendSql("       NULL                                               AS CodigoEmpresaEsp, ");
		sql.appendSql("       NULL                                               AS CodigoEmpresa, ");
		sql.appendSql("       NULL                                               AS DATAATUALIZACAO, ");
		sql.appendSql("       CASE ");
		sql.appendSql("         WHEN ( ITE.QTDNEG - ITE.QTDENTREGUE ) = 0 THEN '' ");
		sql.appendSql("         ELSE 'SEM ESTOQUE' ");
		sql.appendSql("       END                                                AS MotivoCorte, ");
		sql.appendSql("       '0'                                                AS ValorDesconto, ");
		sql.appendSql("       CAST(( ITE.QTDENTREGUE * PRO.PESOBRUTO ) AS FLOAT) AS TotalPeso, ");
		sql.appendSql("       NULL                                               AS VrTotalComissao, ");
		sql.appendSql("       NULL                                               AS SaldoQtdeVenda, ");
		sql.appendSql("       NULL                                               AS FLAG_GERARVERBA ");
		sql.appendSql("FROM   AFV_ANDROID.TE_HISTPEDIDO@PROVIDER HST ");
		sql.appendSql("       INNER JOIN SANKHYA.TGFCAB CAB ");
		sql.appendSql("               ON CAB.AD_PEDPALM = HST.NUMPEDIDOAFV ");
		sql.appendSql("                  AND CAB.TIPMOV = 'P' ");
		sql.appendSql("       INNER JOIN SANKHYA.TGFITE ITE ");
		sql.appendSql("               ON CAB.NUNOTA = ITE.NUNOTA ");
		sql.appendSql("       INNER JOIN SANKHYA.TGFPRO PRO ");
		sql.appendSql("               ON PRO.CODPROD = ITE.CODPROD ");
		sql.appendSql("       LEFT JOIN AFV_ANDROID.TE_HISTPEDIDOITEM@PROVIDER ITEPROVIDER ");
		sql.appendSql("              ON( ITEPROVIDER.NUMPEDIDOEMP = HST.NUMPEDIDOEMP ");
		sql.appendSql("                  AND ITEPROVIDER.CODIGOPRODUTO = ITE.CODPROD ) ");
		sql.appendSql("WHERE  HST.DATAPEDIDO >= SYSDATE - " + qdeDiasExport + "  ");
		sql.appendSql("       AND ORIGEMPEDIDO = 'EXP. SNK' ");
		sql.appendSql("       AND ITEPROVIDER.NUMPEDIDOEMP IS NULL ");
		// sql.appendSql(" AND CAB.AD_PEDPALM = '16419242035961' ");
		sql.appendSql("GROUP  BY HST.NUMPEDIDOEMP, ");
		sql.appendSql("          ITE.CODPROD, ");
		sql.appendSql("          PRO.DESCRPROD, ");
		sql.appendSql("          ITE.QTDNEG, ");
		sql.appendSql("          ITE.QTDNEG, ");
		sql.appendSql("          ITE.QTDENTREGUE, ");
		sql.appendSql("          ITE.PERCDESC, ");
		sql.appendSql("          ITE.VLRTOT, ");
		sql.appendSql("          ITE.QTDNEG, ");
		sql.appendSql("          ITE.QTDENTREGUE, ");
		sql.appendSql("          ITE.QTDENTREGUE, ");
		sql.appendSql("          PRO.PESOBRUTO  ");

		try {
			sql.executeUpdate();
		} catch (IOException | SQLException e) {
			throw e;
		}

	}

	public void insereHistoricoCabecPedidosDuplicPorRegiaoEcom() throws Exception {

		JdbcWrapper jdbc = null;
		EntityFacade entity = EntityFacadeFactory.getDWFFacade();

		jdbc = entity.getJdbcWrapper();

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
		sqlGeraScript.appendSql("                   AND CAB2.DTNEG >= ( SYSDATE - 20 ) ");
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
		sqlGeraScript
				.appendSql("                  AND SUBSTR(EPF.CODREG, 2, LENGTH(EPF.CODREG) - 1) = CAB.CODEMPNEGOC ) ");
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

		} catch (IOException | SQLException e) {
			throw e;
		}

	}

	public void insereHistoricoItemPedidosDuplicPorRegiaoEcom() throws Exception {

		JdbcWrapper jdbc = null;
		EntityFacade entity = EntityFacadeFactory.getDWFFacade();

		jdbc = entity.getJdbcWrapper();

		jdbc.openSession();
		NativeSql sqlGeraScript = new NativeSql(jdbc);

		sqlGeraScript.appendSql("SELECT CAB.NUNOTA, ");
		sqlGeraScript.appendSql("       PED.NUMPEDIDOEMP, ");
		sqlGeraScript.appendSql("       CAB.AD_PEDPALM, ");
		sqlGeraScript.appendSql(
				"'Insert into AFV_ANDROID.te_histpedidoitem@PROVIDER (NUMPEDIDOEMP,CODIGOPRODUTO,DESCRICAOPRODUTO,QTDEVENDA,QTDEBONIFICADA,QTDECORTADA,DESCONTO,VALORVENDA,CODIGOCONDPAGTO,CODIGOEMPRESAESP,CODIGOEMPRESA,DATAATUALIZACAO,MOTIVOCORTE,VALORDESCONTO,TOTALPESO,VRTOTALCOMISSAO,SALDOQTDEVENDA,FLAG_GERARVERBA)  values ( ' ");
		sqlGeraScript.appendSql("       || '''' ");
		sqlGeraScript.appendSql("       || PED.NUMPEDIDOEMP ");
		sqlGeraScript.appendSql("       || '''' ");
		sqlGeraScript.appendSql("       || ' , ' ");
		sqlGeraScript.appendSql("       || '''' ");
		sqlGeraScript.appendSql("       || ITE.CODPROD ");
		sqlGeraScript.appendSql("       || '''' ");
		sqlGeraScript.appendSql("       || ' , ''' ");
		sqlGeraScript.appendSql("       || PROD.DESCRPROD ");
		sqlGeraScript.appendSql("       || ' (' ");
		sqlGeraScript.appendSql("       || ITE.CODVOL ");
		sqlGeraScript.appendSql("       || ')' ");
		sqlGeraScript.appendSql("       || ''' , ''' ");
		sqlGeraScript.appendSql("       || ITE.QTDNEG ");
		sqlGeraScript.appendSql("       || ''' , ''' ");
		sqlGeraScript.appendSql("       || 0 ");
		sqlGeraScript.appendSql("       || ''' , ''' ");
		sqlGeraScript.appendSql("       || 0 ");
		sqlGeraScript.appendSql("       || ''' , ''' ");
		sqlGeraScript.appendSql("       || ITE.VLRDESC ");
		sqlGeraScript.appendSql("       || ''' , ''' ");
		sqlGeraScript
				.appendSql("       || NVL(Round(Sum(( ITE.VLRTOT - ITE.VLRDESC ) * ITE.ATUALESTOQUE *- 1), 2), 0) ");
		sqlGeraScript.appendSql("       || ''' , ' ");
		sqlGeraScript.appendSql("       || 'null' ");
		sqlGeraScript.appendSql("       || ' , ' ");
		sqlGeraScript.appendSql("       || 'null' ");
		sqlGeraScript.appendSql("       || ' , ' ");
		sqlGeraScript.appendSql("       || 'null' ");
		sqlGeraScript.appendSql("       || ' , ''' ");
		sqlGeraScript.appendSql("       || PED.DATAATUALIZACAO ");
		sqlGeraScript.appendSql("       || ''' , ' ");
		sqlGeraScript.appendSql("       || 'NULL' ");
		sqlGeraScript.appendSql("       || ' , ''' ");
		sqlGeraScript.appendSql("       || ITE.VLRDESC ");
		sqlGeraScript.appendSql("       || ''' , ''' ");
		sqlGeraScript.appendSql("       || NVL(ITE.PESO, 0) ");
		sqlGeraScript.appendSql("       || ''' , ''' ");
		sqlGeraScript.appendSql("       || 0 ");
		sqlGeraScript.appendSql("       || ''' , ''' ");
		sqlGeraScript.appendSql("       || 0 ");
		sqlGeraScript.appendSql("       || ''' , ' ");
		sqlGeraScript.appendSql("       || 'NULL' ");
		sqlGeraScript.appendSql("       || ') ' SQL ");
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
		sqlGeraScript.appendSql("                                            AND CAB.DTNEG >= ( SYSDATE - 20 ) ");
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

		} catch (IOException | SQLException e) {
			throw e;
		}

	}

}
