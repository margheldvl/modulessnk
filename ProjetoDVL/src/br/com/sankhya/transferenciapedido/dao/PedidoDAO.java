package br.com.sankhya.transferenciapedido.dao;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.sankhya.jape.EntityFacade;
import br.com.sankhya.jape.dao.JdbcWrapper;
import br.com.sankhya.jape.sql.NativeSql;
import br.com.sankhya.modelcore.util.EntityFacadeFactory;
import br.com.sankhya.transferenciapedido.model.CabecalhoPedidoDVL;

public class PedidoDAO {

	public List<CabecalhoPedidoDVL> buscaCabecalhoPedidoDVL() throws Exception {

		String ret = "";

		JdbcWrapper jdbc = null;
		EntityFacade entity = EntityFacadeFactory.getDWFFacade();

		jdbc = entity.getJdbcWrapper();

		jdbc.openSession();
		NativeSql sql = new NativeSql(jdbc);

		sql.appendSql("SELECT CAB.NUMNOTA NUMNOTA, ");
		sql.appendSql("       CAB.CODPARC, ");
		sql.appendSql("       CAB.DTNEG, ");
		sql.appendSql("       32         CODTIPOPER, ");
		sql.appendSql("       0 CODTIPVENDA, ");
		sql.appendSql("       CAB.CODVEND, ");
		sql.appendSql("       CAB.CODEMP, ");
		sql.appendSql("       'T'        TIPMOV, ");
		sql.appendSql("       11208      CODNAT, ");
		sql.appendSql("       1          CODUSU, ");
		sql.appendSql("       CAB.NUNOTA AD_NUNOTADUP ");
		sql.appendSql("FROM   TGFCAB CAB ");
		sql.appendSql("       LEFT JOIN TGFCAB CABTRANSF ");
		sql.appendSql("              ON( CABTRANSF.CODTIPOPER = 32 ");
		sql.appendSql("                  AND CAB.NUNOTA = CABTRANSF.AD_NUNOTADUP ) ");
		sql.appendSql("WHERE  CAB.CODTIPOPER = 239 ");
		sql.appendSql("       AND CABTRANSF.AD_NUNOTADUP IS NULL ");
		sql.appendSql("       AND CAB.DTNEG >= '17/03/2022' ");
		sql.appendSql("       AND CAB.CODPARC = '872'  ");

		List<CabecalhoPedidoDVL> listaCabecalhoPed = new ArrayList<CabecalhoPedidoDVL>();

		try {
			ResultSet rs = sql.executeQuery();

			while (rs.next()) {

				CabecalhoPedidoDVL cab = new CabecalhoPedidoDVL();

				String nroNota = " ";
				String numNota = rs.getString("NUMNOTA");
				String codParc = rs.getString("CODPARC");
				String codTipOper = rs.getString("CODTIPOPER");
				String codTipVenda = rs.getString("CODTIPVENDA");
				String codVend = rs.getString("CODVEND");
				String codEmp = rs.getString("CODEMP");
				String tipMov = rs.getString("TIPMOV");
				String codNat = rs.getString("CODNAT");
				String codUsu = rs.getString("CODUSU");
				String adNumNotaDup = rs.getString("AD_NUNOTADUP");

				ret = ret + adNumNotaDup + "\n";

				cab.setNuNota(nroNota);
				cab.setAdNuNotaDup(adNumNotaDup);
				cab.setCodEmp(codEmp);
				cab.setCodNat(codNat);
				cab.setCodParc(codParc);
				cab.setCodTipOper(codTipOper);
				cab.setCodTipVenda(codTipVenda);
				cab.setCodVend(codVend);
				cab.setNumNota(numNota);
				cab.setTipMov(tipMov);
				cab.setCodUsu(codUsu);

				Date agora = new Date();
				SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");
				Date dataFormatada = agora;

				String dataAtu = fmt.format(dataFormatada);
				cab.setDtNeg(dataAtu);

				listaCabecalhoPed.add(cab);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return listaCabecalhoPed;

	}

	public List<String> buscaDestinatariosEmail(String strListaParc) throws Exception {

		JdbcWrapper jdbc = null;
		EntityFacade entity = EntityFacadeFactory.getDWFFacade();

		jdbc = entity.getJdbcWrapper();

		jdbc.openSession();
		NativeSql sql = new NativeSql(jdbc);

		sql.appendSql("SELECT EMAIL ");
		sql.appendSql("FROM   TSIUSU  USU ");
		sql.appendSql("WHERE USU.CODUSU IN (" + strListaParc + ") ");

		List<String> listParc = new ArrayList<>();

		String destEmail = "";
		try {
			ResultSet rs = sql.executeQuery();

			while (rs.next()) {
				destEmail = rs.getString("EMAIL");
				listParc.add(destEmail);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return listParc;

	}
	
	public boolean deletaCabecalhoNota(Long nuNota) throws Exception {
		JdbcWrapper jdbc = null;
		EntityFacade entity = EntityFacadeFactory.getDWFFacade();

		jdbc = entity.getJdbcWrapper();

		jdbc.openSession();
		NativeSql sql = new NativeSql(jdbc);

		boolean cabExcluido = true;

		sql.appendSql("DELETE FROM TGFCAB ");
		sql.appendSql("WHERE  NUNOTA = :NUNOTA ");
		sql.setNamedParameter("NUNOTA", nuNota);

		try {
			sql.executeUpdate();
			cabExcluido = true;

		} catch (IOException | SQLException e) {
			throw e;
		}

		return cabExcluido;
	}
	
	public boolean atualizaLibLimNota(Long nuNota) throws Exception {
		JdbcWrapper jdbc = null;
		EntityFacade entity = EntityFacadeFactory.getDWFFacade();

		jdbc = entity.getJdbcWrapper();

		jdbc.openSession();
		NativeSql sql = new NativeSql(jdbc);

		boolean atualizou = false;

		sql.appendSql("UPDATE TSILIB ");
		sql.appendSql("SET    CODUSULIB = 706, ");
		sql.appendSql("       DHLIB = SYSDATE, ");
		sql.appendSql("       VLRLIBERADO = VLRATUAL ");
		sql.appendSql("WHERE  NUCHAVE = :NUNOTA  ");
		sql.setNamedParameter("NUNOTA", nuNota);

		try {
			sql.executeUpdate();
			atualizou = true;

		} catch (IOException | SQLException e) {
			throw e;
		}

		return atualizou;
	}
	
	public String notaConfirmada(Long nuNota) throws Exception {
		JdbcWrapper jdbc = null;
		EntityFacade entity = EntityFacadeFactory.getDWFFacade();

		jdbc = entity.getJdbcWrapper();

		jdbc.openSession();
		NativeSql sql = new NativeSql(jdbc);

		String notaConfirmada = "Não";

		sql.appendSql("SELECT CASE ");
		sql.appendSql("         WHEN TGFCAB.STATUSNOTA = 'L' THEN 'Sim' ");
		sql.appendSql("         ELSE 'Não' ");
		sql.appendSql("       END CONFIRMADA ");
		sql.appendSql("FROM   TGFCAB ");
		sql.appendSql("WHERE  NUNOTA = :NUNOTA ");
		sql.setNamedParameter("NUNOTA", nuNota);

		try {
			ResultSet rs = sql.executeQuery();

			while (rs.next()) {
				notaConfirmada = rs.getString("CONFIRMADA");				
			}			

		} catch (IOException | SQLException e) {
			throw e;
		}

		return notaConfirmada;
	}
}
