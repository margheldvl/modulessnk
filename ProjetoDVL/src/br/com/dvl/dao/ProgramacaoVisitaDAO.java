package br.com.dvl.dao;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.dvl.model.ProgramacaoVisita;
import br.com.sankhya.jape.EntityFacade;
import br.com.sankhya.jape.core.JapeSession;
import br.com.sankhya.jape.dao.JdbcWrapper;
import br.com.sankhya.jape.sql.NativeSql;
import br.com.sankhya.modelcore.util.EntityFacadeFactory;

public class ProgramacaoVisitaDAO {

	public List<String> IncluirProgramVisNativeSQLWithBatch(List<ProgramacaoVisita> listaProgramacaoVisita) {

		JapeSession.SessionHandle hnd = null;
		JdbcWrapper jdbc = null;

		String codParc = "";

		List<String> listResul = new ArrayList<String>();

		try {

			hnd = JapeSession.open();
			jdbc = EntityFacadeFactory.getDWFFacade().getJdbcWrapper();
			jdbc.openSession();

			NativeSql queryUpd = new NativeSql(jdbc);

			queryUpd.appendSql(
					"INSERT INTO TGFVIS  (CODPARC,TIPO,VALOR,SEQVISITA,CODVEND,CODREG) VALUES (:CODPARC,:TIPO,:VALOR,:SEQVISITA,:CODVEND,:CODREG)");
			queryUpd.setReuseStatements(true);
			queryUpd.setBatchUpdateSize(1);

			ProgramacaoVisita programVis;

			for (int i = 0; i < listaProgramacaoVisita.size(); i++) {

				programVis = new ProgramacaoVisita();
				programVis = listaProgramacaoVisita.get(i);

				codParc = programVis.getCodparc().toString();

				queryUpd.setNamedParameter("CODPARC", programVis.getCodparc());
				queryUpd.setNamedParameter("TIPO", programVis.getTipo());
				queryUpd.setNamedParameter("VALOR", programVis.getValor());
				queryUpd.setNamedParameter("SEQVISITA", programVis.getSeqvisita());
				queryUpd.setNamedParameter("CODVEND", programVis.getCodvend());
				queryUpd.setNamedParameter("CODREG", programVis.getCodreg());

				try {
					queryUpd.addBatch();
					queryUpd.cleanParameters();
					listResul.add(codParc + "; Atualizado");

				} catch (Exception e) {
					listResul.add(codParc + "; " + e.getMessage());
				}

			}

			queryUpd.flushBatchTail();
			NativeSql.releaseResources(queryUpd);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JdbcWrapper.closeSession(jdbc);
			JapeSession.close(hnd);
		}

		return listResul;
	}

	public boolean ExcluirProgramVisNativeSQLWithBatch(List<ProgramacaoVisita> listaProgramacaoVisita) {

		JapeSession.SessionHandle hnd = null;
		JdbcWrapper jdbc = null;

		boolean deletouTodos = true;

		try {
			hnd = JapeSession.open();
			jdbc = EntityFacadeFactory.getDWFFacade().getJdbcWrapper();
			jdbc.openSession();

			NativeSql queryUpd = new NativeSql(jdbc);

			queryUpd.appendSql("DELETE FROM TGFVIS WHERE CODPARC = :CODPARC");

			queryUpd.setReuseStatements(true);
			queryUpd.setBatchUpdateSize(500);

			ProgramacaoVisita programVis;

			for (int i = 0; i < listaProgramacaoVisita.size(); i++) {

				programVis = new ProgramacaoVisita();
				programVis = listaProgramacaoVisita.get(i);

				queryUpd.setNamedParameter("CODPARC", programVis.getCodparc());

				queryUpd.addBatch();
				queryUpd.cleanParameters();

			}

			queryUpd.flushBatchTail();
			NativeSql.releaseResources(queryUpd);

		} catch (Exception e) {
			deletouTodos = false;
			e.printStackTrace();
			// throwExceptionRollingBack(e);
		} finally {
			JdbcWrapper.closeSession(jdbc);
			JapeSession.close(hnd);
		}

		return deletouTodos;

	}

	public String buscaChave(Long codUsu, String arquivoImportacao, String arquivoLog) throws Exception {

		JdbcWrapper jdbc = null;
		EntityFacade entity = EntityFacadeFactory.getDWFFacade();

		jdbc = entity.getJdbcWrapper();

		jdbc.openSession();
		NativeSql sql = new NativeSql(jdbc);

		sql.appendSql("SELECT nvl(ANX.NUATTACH,0) NUATTACH, ");
		sql.appendSql("       CASE ");
		sql.appendSql("         WHEN( Upper(ANX.CHAVEARQUIVO) = ");
		sql.appendSql("               UPPER(:ARQLOG) ) THEN 'LOG' ");
		sql.appendSql("         ELSE 'IMPORT' ");
		sql.appendSql("       END TIPO, ");
		sql.appendSql("       ANX2.QDEENC, ");
		sql.appendSql("       CHAVEARQUIVO, ");
		sql.appendSql("               CASE WHEN( Upper(ANX.CHAVEARQUIVO) <>  ");
		sql.appendSql(
				"		               UPPER(:ARQLOG) AND TO_DATE(TO_CHAR(DHALTER,'DD/MM/YYYY'),'DD/MM/YYYY') >  TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY')) THEN 'S' ELSE 'N' END ENVIA_NOVO_ARQ ");
		sql.appendSql("FROM   TSIANX ANX ");
		sql.appendSql("       OUTER APPLY(SELECT Count(1) QDEENC ");
		sql.appendSql("                   FROM   TSIANX ANX2 ");
		sql.appendSql("                   WHERE  ANX2.CODUSU = :CODUSU ");
		sql.appendSql("                          AND ( Upper(ANX2.NOMEARQUIVO) = UPPER(:ARQIMPORT) ) ");
		sql.appendSql("                          AND NOMEINSTANCIA = 'TGFPRG') ANX2 ");
		sql.appendSql("WHERE  CODUSU = :CODUSU ");
		sql.appendSql("       AND ( Upper(NOMEARQUIVO) = UPPER(:ARQIMPORT) ");
		sql.appendSql("              OR Upper(CHAVEARQUIVO) = ");
		sql.appendSql("                 UPPER(:ARQLOG) ");
		sql.appendSql("           ) ");
		sql.appendSql("       AND NOMEINSTANCIA = 'TGFPRG'  ");

		sql.setNamedParameter("CODUSU", codUsu);
		sql.setNamedParameter("ARQIMPORT", arquivoImportacao);
		sql.setNamedParameter("ARQLOG", arquivoLog);

		Integer qdeArqImpEnc = 0;
		Long nuAnexoDelete = 0l;
		String chveArquivo = "";

		try {

			ResultSet rs = sql.executeQuery();

			while (rs.next()) {

				String tipo = rs.getString("TIPO");
				String enviaNovoArq = rs.getString("ENVIA_NOVO_ARQ");
				String chveArq = rs.getString("CHAVEARQUIVO");
				Long nuAnexo = Long.valueOf(rs.getString("NUATTACH"));
				Integer qdeArqEnc = Integer.parseInt(rs.getString("QDEENC"));

				if (tipo.equals("LOG") && nuAnexoDelete == 0l) {
					nuAnexoDelete = nuAnexo;
				} else {
					if (enviaNovoArq.equals("N")) {
						chveArquivo = chveArq;
					} else {
						chveArquivo = enviaNovoArq;
					}
					qdeArqImpEnc = qdeArqEnc;
				}

			}

			if (nuAnexoDelete != 0l) {
				sql = new NativeSql(jdbc);
				sql.appendSql("DELETE FROM TSIANX ");
				sql.appendSql("WHERE  NUATTACH = :NUATTACH ");
				sql.setNamedParameter("NUATTACH", nuAnexoDelete);
				sql.executeUpdate();
			}

		} catch (IOException | SQLException e) {
			throw e;
		} finally {
			JdbcWrapper.closeSession(jdbc);
			jdbc.closeSession();
		}

		chveArquivo = qdeArqImpEnc != 1 ? qdeArqImpEnc > 1 ? "-1" : "0" : chveArquivo;

		return chveArquivo;
	}

	public boolean insereLog(Long codUsu, String arquivoImportacao, String arquivoLog) throws Exception {

		JdbcWrapper jdbc = null;
		EntityFacade entity = EntityFacadeFactory.getDWFFacade();

		jdbc = entity.getJdbcWrapper();

		jdbc.openSession();
		NativeSql sql = new NativeSql(jdbc);

		sql.appendSql("INSERT INTO TSIANX ");
		sql.appendSql("            (NUATTACH, ");
		sql.appendSql("             NOMEINSTANCIA, ");
		sql.appendSql("             CHAVEARQUIVO, ");
		sql.appendSql("             NOMEARQUIVO, ");
		sql.appendSql("             DESCRICAO, ");
		sql.appendSql("             RESOURCEID, ");
		sql.appendSql("             TIPOAPRES, ");
		sql.appendSql("             TIPOACESSO, ");
		sql.appendSql("             CODUSU, ");
		sql.appendSql("             DHALTER, ");
		sql.appendSql("             PKREGISTRO, ");
		sql.appendSql("             CODUSUALT, ");
		sql.appendSql("             DHCAD, ");
		sql.appendSql("             LINK) ");
		sql.appendSql("VALUES      ((SELECT MAX(NUATTACH) + 1 FROM TSIANX), ");
		sql.appendSql("             'TGFPRG', ");
		sql.appendSql("             :ARQLOG, ");
		sql.appendSql("             :ARQLOG, ");
		sql.appendSql("             :ARQLOG, ");
		sql.appendSql("             'br.com.sankhya.menu.adicional.TGFPRG', ");
		sql.appendSql("             'LOC', ");
		sql.appendSql("             'USU', ");
		sql.appendSql("             :CODUSU, ");
		sql.appendSql("             TO_CHAR(SYSDATE, 'DD/MM/RR'), ");
		sql.appendSql("             :PKREGISTRO, ");
		sql.appendSql("             :CODUSU, ");
		sql.appendSql("             TO_CHAR(SYSDATE, 'DD/MM/RR'), ");
		sql.appendSql("             NULL)  ");
		sql.setNamedParameter("CODUSU", codUsu);

		sql.setNamedParameter("CODUSU", codUsu);
		sql.setNamedParameter("ARQLOG", arquivoLog);
		sql.setNamedParameter("ARQIMPORT", arquivoImportacao);

		String pkRegistro = buscapkRegistro(codUsu, arquivoImportacao);

		sql.setNamedParameter("PKREGISTRO", pkRegistro);

		boolean ok = false;

		try {
			sql.executeUpdate();
			ok = true;
		} catch (Exception e) {
			throw e;
		}

		return ok;

	}

	private String buscapkRegistro(Long codUsu, String arquivoImportacao) throws Exception {

		JdbcWrapper jdbc = null;
		EntityFacade entity = EntityFacadeFactory.getDWFFacade();

		jdbc = entity.getJdbcWrapper();

		jdbc.openSession();
		NativeSql sql = new NativeSql(jdbc);

		sql.appendSql("SELECT ANX.PKREGISTRO ");
		sql.appendSql("FROM   TSIANX ANX ");
		sql.appendSql("WHERE  CODUSU = :CODUSU ");
		sql.appendSql("       AND ( UPPER(NOMEARQUIVO) = UPPER(:ARQIMPORT) ) ");
		sql.appendSql("       AND NOMEINSTANCIA = 'TGFPRG'  ");

		sql.setNamedParameter("CODUSU", codUsu);
		sql.setNamedParameter("ARQIMPORT", arquivoImportacao);

		String pkRegistro = "";

		try {

			ResultSet rs = sql.executeQuery();

			while (rs.next()) {
				pkRegistro = rs.getString("PKREGISTRO");
			}

		} catch (IOException | SQLException e) {
			throw e;
		} finally {
			JdbcWrapper.closeSession(jdbc);
			jdbc.closeSession();
		}

		return pkRegistro;

	}

}
