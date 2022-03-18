package br.com.sankhya.transferenciapedido.dao;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.sankhya.jape.EntityFacade;
import br.com.sankhya.jape.dao.JdbcWrapper;
import br.com.sankhya.jape.sql.NativeSql;
import br.com.sankhya.modelcore.util.EntityFacadeFactory;
import br.com.sankhya.transferenciapedido.model.EstoqueDVL;
import br.com.sankhya.transferenciapedido.model.ItemPedidoDVL;

public class ItemPedidoDAO {

	public List<ItemPedidoDVL> buscaItemPedidoDVL(Long nuNota) throws Exception {
		JdbcWrapper jdbc = null;
		EntityFacade entity = EntityFacadeFactory.getDWFFacade();

		jdbc = entity.getJdbcWrapper();

		jdbc.openSession();
		NativeSql sql = new NativeSql(jdbc);

		sql.appendSql("SELECT  ");
		sql.appendSql("   DISTINCT ITE.NUNOTA ");
		sql.appendSql("    ,ITE.CODPROD ");
		sql.appendSql("    ,ITE.QTDNEG ");
		sql.appendSql("    ,ITE.CODLOCALORIG ");
		sql.appendSql("    ,1102 CODLOCALDEST ");
		sql.appendSql("    ,ITE.VLRUNIT ");
		sql.appendSql("    ,ITE.VLRTOT ");
		sql.appendSql("    ,ITE.CODVOL ");
		sql.appendSql("    ,ITE.VLRDESC ");
		sql.appendSql("    ,ITE.PERCDESC ");
		sql.appendSql("    ,EST.QDE_EST_VAL ");
		sql.appendSql("FROM TGFITE ITE  ");
		sql.appendSql("INNER JOIN TGFCAB CAB ON(CAB.NUNOTA = :NUNOTA)  ");
		sql.appendSql(
				"LEFT JOIN TGFCAB CABTRANSF ON(CABTRANSF.AD_NUNOTADUP = CAB.NUNOTA AND CABTRANSF.CODTIPOPER = 32) ");
		sql.appendSql(
				"LEFT JOIN TGFITE ITETRANSF ON(CABTRANSF.NUNOTA = ITETRANSF.NUNOTA AND ITETRANSF.CODPROD = ITE.CODPROD AND  ITETRANSF.CODLOCALORIG = 1102) ");
		sql.appendSql("OUTER APPLY(SELECT Count(1) QDE_EST_VAL ");
		sql.appendSql("                   FROM   TGFEST ESTOQ ");
		sql.appendSql("                   WHERE  ESTOQ.CODPROD = ITE.CODPROD ");
		sql.appendSql("                          AND CODLOCAL = 1101 ");
		sql.appendSql("                          AND ESTOQ.ESTOQUE - ESTOQ.RESERVADO >= ITE.QTDNEG ");
		sql.appendSql("                          AND ESTOQ.CONTROLE = ' ' ");
		sql.appendSql("                          AND ESTOQ.CODPARC = 0) EST ");
		sql.appendSql("WHERE ITE.NUNOTA = CAB.NUNOTA ");
		sql.appendSql("      AND CAB.CODTIPOPER = 239 ");
		sql.appendSql("      AND ITETRANSF.CODPROD IS NULL ");
		sql.appendSql("      AND NVL(CABTRANSF.AD_DUPLICADO,'N') <> 'S'  ");
		sql.appendSql("      AND CAB.NUNOTA = :NUNOTA  ");

		sql.setNamedParameter("NUNOTA", nuNota);

		List<ItemPedidoDVL> listaItemPedido = new ArrayList<ItemPedidoDVL>();
		//List<ItemPedidoDVL> listaItemPedidoEstoqIndisp = new ArrayList<ItemPedidoDVL>();

		try {
			ResultSet rs = sql.executeQuery();

			while (rs.next()) {

				ItemPedidoDVL itePed = new ItemPedidoDVL();

				String codProd = rs.getString("CODPROD");
				String qtdNeg = rs.getString("QTDNEG");
				String codLocalOrig = rs.getString("CODLOCALORIG");
				String codLocalDest = rs.getString("CODLOCALDEST");
				String codVol = rs.getString("CODVOL");
				//Integer estoqValido = Integer.parseInt(rs.getString("QDE_EST_VAL"));

				itePed.setCodLocalDest(codLocalDest);
				itePed.setCodLocalOrig(codLocalOrig);
				itePed.setCodProd(codProd);
				itePed.setCodVol(codVol);
				itePed.setQtdNeg(qtdNeg);
				listaItemPedido.add(itePed);

//				if (estoqValido > 0) {
//					listaItemPedido.add(itePed);
//				} else {
//					listaItemPedidoEstoqIndisp.add(itePed);
//				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return listaItemPedido;

	}

	public List<EstoqueDVL> buscaEstoqueIndisp(Long nuNota) throws Exception {

		JdbcWrapper jdbc = null;
		EntityFacade entity = EntityFacadeFactory.getDWFFacade();

		jdbc = entity.getJdbcWrapper();

		jdbc.openSession();
		NativeSql sql = new NativeSql(jdbc);

		sql.appendSql("SELECT  ");
		sql.appendSql("   DISTINCT ITE.NUNOTA ");
		sql.appendSql("    ,ITE.CODPROD ");
		sql.appendSql("    ,ITE.QTDNEG ");
		sql.appendSql("    ,ITE.CODLOCALORIG ");
		sql.appendSql("    ,1102 CODLOCALDEST ");
		sql.appendSql("    ,ITE.VLRUNIT ");
		sql.appendSql("    ,ITE.VLRTOT ");
		sql.appendSql("    ,ITE.CODVOL CODVOLITE ");
		sql.appendSql("    ,ITE.VLRDESC ");
		sql.appendSql("    ,ITE.PERCDESC ");
		sql.appendSql("    ,ESTOQATUAL.RESERVADO ");
		sql.appendSql("    ,ESTOQATUAL.ESTOQUE ");
		sql.appendSql("    ,CASE WHEN(ESTOQATUAL.ESTFIM < 0) THEN ESTOQATUAL.ESTFIM ELSE 0 END DIFERENCA ");
		sql.appendSql("    ,PROD.DESCRPROD ");
		sql.appendSql("    ,PROD.CODVOL ");
		sql.appendSql("FROM TGFITE ITE  ");
		sql.appendSql("INNER JOIN TGFCAB CAB ON(CAB.NUNOTA = :NUNOTA)  ");
		sql.appendSql(
				"LEFT JOIN TGFCAB CABTRANSF ON(CABTRANSF.AD_NUNOTADUP = CAB.NUNOTA AND CABTRANSF.CODTIPOPER = 32) ");
		sql.appendSql(
				"LEFT JOIN TGFITE ITETRANSF ON(CABTRANSF.NUNOTA = ITETRANSF.NUNOTA AND ITETRANSF.CODPROD = ITE.CODPROD AND  ITETRANSF.CODLOCALORIG = 1102) ");
		sql.appendSql("LEFT JOIN TGFPRO PROD ON(PROD.CODPROD = ITE.CODPROD) ");
		sql.appendSql("     OUTER APPLY(SELECT NVL(ESTOQ.RESERVADO,0) RESERVADO ");
		sql.appendSql("                        ,NVL(ESTOQ.ESTOQUE,0) ESTOQUE ");
		sql.appendSql("                        ,NVL((ESTOQ.ESTOQUE - ESTOQ.RESERVADO),0) - NVL(ITE.QTDNEG,0) ESTFIM ");
		sql.appendSql("                  FROM   TGFEST ESTOQ ");
		sql.appendSql("                  WHERE  ESTOQ.CODPROD = ITE.CODPROD ");
		sql.appendSql("                         AND CODLOCAL = 1101 ");
		sql.appendSql("                         AND ESTOQ.CONTROLE = ' ' ");
		sql.appendSql("                         AND ESTOQ.CODPARC = 0) ESTOQATUAL ");
		sql.appendSql("WHERE ITE.NUNOTA = CAB.NUNOTA ");
		sql.appendSql("      AND CAB.CODTIPOPER = 239 ");
		sql.appendSql("      AND ITETRANSF.CODPROD IS NULL ");
		sql.appendSql("      AND NVL(CABTRANSF.AD_DUPLICADO,'N') <> 'S'  ");
		sql.appendSql("      AND CAB.NUNOTA = :NUNOTA  ");
		sql.appendSql("      AND CASE WHEN(ESTOQATUAL.ESTFIM < 0) THEN ESTOQATUAL.ESTFIM ELSE 0 END < 0 ");

		sql.setNamedParameter("NUNOTA", nuNota);

		List<EstoqueDVL> listaEstoq = new ArrayList<EstoqueDVL>();

		try {
			ResultSet rs = sql.executeQuery();

			while (rs.next()) {

				EstoqueDVL estoq = new EstoqueDVL();

				String descProd = rs.getString("DESCRPROD");
				String codVol = rs.getString("CODVOL");
				Long codProd = Long.valueOf(rs.getString("CODPROD"));
				Double estReservado = Double.parseDouble(rs.getString("RESERVADO"));
				Double estAtual = Double.parseDouble(rs.getString("ESTOQUE"));
				Double diferenca = Double.parseDouble(rs.getString("DIFERENCA"));
				BigDecimal qdeBaixa = new BigDecimal(rs.getString("QTDNEG"));
				//Double qdeBaixa = Double.parseDouble(rs.getString("QTDNEG"));

				estoq.setCodprod(codProd);
				estoq.setCodvol(codVol);
				estoq.setEstoque(estAtual);
				estoq.setReservado(estReservado);
				estoq.setDescricaoprod(descProd);
				estoq.setQdeDiferenca(diferenca);
				estoq.setQdeBaixa(qdeBaixa);

				listaEstoq.add(estoq);

			}

		} catch (IOException | SQLException e) {

			throw e;
		}

		return listaEstoq;

	}
	
	public List<EstoqueDVL> buscaItensValorComSemEstoqNota(Long nuNota) throws Exception {

		JdbcWrapper jdbc = null;
		EntityFacade entity = EntityFacadeFactory.getDWFFacade();

		jdbc = entity.getJdbcWrapper();

		jdbc.openSession();
		NativeSql sql = new NativeSql(jdbc);

		sql.appendSql("SELECT DISTINCT ITE.NUNOTA, ");
		sql.appendSql("                ITE.CODPROD, ");
		sql.appendSql("                ITE.QTDNEG, ");
		sql.appendSql("                ITE.CODLOCALORIG, ");
		sql.appendSql("                1102       CODLOCALDEST, ");
		sql.appendSql("                NVL(ITE.VLRUNIT,0) VLRUNIT, ");
		sql.appendSql("                NVL(ITE.VLRTOT,0) VLRTOT, ");
		sql.appendSql("                ITE.CODVOL CODVOLITE, ");
		sql.appendSql("                ITE.VLRDESC, ");
		sql.appendSql("                ITE.PERCDESC, ");
		sql.appendSql("                ESTOQATUAL.RESERVADO, ");
		sql.appendSql("                ESTOQATUAL.ESTOQUE, ");
		sql.appendSql("                CASE ");
		sql.appendSql("                  WHEN( ESTOQATUAL.ESTFIM < 0 ) THEN ESTOQATUAL.ESTFIM ");
		sql.appendSql("                  ELSE 0 ");
		sql.appendSql("                END        DIFERENCA, ");
		sql.appendSql("                PROD.DESCRPROD, ");
		sql.appendSql("                PROD.CODVOL ");
		sql.appendSql("FROM   TGFITE ITE ");
		sql.appendSql("       INNER JOIN TGFCAB CAB ");
		sql.appendSql("               ON( CAB.NUNOTA = :NUNOTA ) ");
		sql.appendSql("       LEFT JOIN TGFCAB CABTRANSF ");
		sql.appendSql("              ON( CABTRANSF.AD_NUNOTADUP = CAB.NUNOTA ");
		sql.appendSql("                  AND CABTRANSF.CODTIPOPER = 32 ) ");
		sql.appendSql("       LEFT JOIN TGFITE ITETRANSF ");
		sql.appendSql("              ON( CABTRANSF.NUNOTA = ITETRANSF.NUNOTA ");
		sql.appendSql("                  AND ITETRANSF.CODPROD = ITE.CODPROD ");
		sql.appendSql("                  AND ITETRANSF.CODLOCALORIG = 1102 ) ");
		sql.appendSql("       LEFT JOIN TGFPRO PROD ");
		sql.appendSql("              ON( PROD.CODPROD = ITE.CODPROD ) ");
		sql.appendSql("       OUTER APPLY(SELECT NVL(ESTOQ.RESERVADO, 0) ");
		sql.appendSql("                          RESERVADO, ");
		sql.appendSql("                          NVL(ESTOQ.ESTOQUE, 0) ");
		sql.appendSql("                                                                  ESTOQUE ");
		sql.appendSql("                  , ");
		sql.appendSql("                          NVL(( ESTOQ.ESTOQUE - ESTOQ.RESERVADO ), 0) - ");
		sql.appendSql("                          NVL(ITE.QTDNEG, 0) ESTFIM ");
		sql.appendSql("                   FROM   TGFEST ESTOQ ");
		sql.appendSql("                   WHERE  ESTOQ.CODPROD = ITE.CODPROD ");
		sql.appendSql("                          AND CODEMP = 1 ");
		sql.appendSql("                          AND CODLOCAL = 1101 ");
		sql.appendSql("                          AND ESTOQ.CONTROLE = ' ' ");
		sql.appendSql("                          AND ESTOQ.CODPARC = 0) ESTOQATUAL ");
		sql.appendSql("WHERE  ITE.NUNOTA = CAB.NUNOTA ");
		sql.appendSql("       AND ITE.CODLOCALORIG = 1101  ");
		sql.appendSql("       AND CAB.NUNOTA = :NUNOTA  ");		

		sql.setNamedParameter("NUNOTA", nuNota);

		List<EstoqueDVL> listaEstoq = new ArrayList<EstoqueDVL>();

		try {
			ResultSet rs = sql.executeQuery();

			while (rs.next()) {

				EstoqueDVL estoq = new EstoqueDVL();

				String descProd = rs.getString("DESCRPROD");
				String codVol = rs.getString("CODVOL");
				Long codProd = Long.valueOf(rs.getString("CODPROD"));
				Double estReservado = Double.parseDouble(rs.getString("RESERVADO"));
				Double estAtual = Double.parseDouble(rs.getString("ESTOQUE"));
				Double diferenca = Double.parseDouble(rs.getString("DIFERENCA"));
				BigDecimal qdeBaixa = new BigDecimal(rs.getString("QTDNEG"));
				Double vlrUnit = Double.parseDouble(rs.getString("VLRUNIT"));
				Double vlrTot = Double.parseDouble(rs.getString("VLRTOT"));

				estoq.setCodprod(codProd);
				estoq.setCodvol(codVol);
				estoq.setEstoque(estAtual);
				estoq.setReservado(estReservado);
				estoq.setDescricaoprod(descProd);
				estoq.setQdeDiferenca(diferenca);
				estoq.setQdeBaixa(qdeBaixa);
				estoq.setVlrunit(vlrUnit);
				estoq.setVlrtotal(vlrTot);

				listaEstoq.add(estoq);

			}

		} catch (IOException | SQLException e) {

			throw e;
		}

		return listaEstoq;

	}
	
	public boolean deletaItensNota(Long nuNota, Long codProd) throws Exception {
		JdbcWrapper jdbc = null;
		EntityFacade entity = EntityFacadeFactory.getDWFFacade();

		jdbc = entity.getJdbcWrapper();

		jdbc.openSession();
		NativeSql sql = new NativeSql(jdbc);

		boolean prodExcluido = false;

		sql.appendSql("DELETE FROM TGFITE ");
		sql.appendSql("WHERE  NUNOTA = :NUNOTA ");

		if (codProd != null) {
			sql.appendSql("       AND CODPROD = :CODPROD  ");
		}

		sql.setNamedParameter("NUNOTA", nuNota);
		if (codProd != null) {
			sql.setNamedParameter("CODPROD", codProd);
		}

		try {
			sql.executeUpdate();
			prodExcluido = true;

		} catch (IOException | SQLException e) {
			throw e;
		}

		return prodExcluido;
	}
}
