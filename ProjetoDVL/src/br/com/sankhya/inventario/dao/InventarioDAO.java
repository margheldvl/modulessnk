package br.com.sankhya.inventario.dao;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import br.com.sankhya.jape.EntityFacade;
import br.com.sankhya.jape.dao.JdbcWrapper;
import br.com.sankhya.jape.sql.NativeSql;
import br.com.sankhya.modelcore.util.EntityFacadeFactory;

public class InventarioDAO {

	public List<String> achouApenasUmaOrdemCarga(List<String> listNota, String ordemCarga) throws Exception {
	//	public String achouApenasUmaOrdemCarga(List<String> listNota, String ordemCarga) throws Exception {

		JdbcWrapper jdbc = null;
		EntityFacade entity = EntityFacadeFactory.getDWFFacade();

		jdbc = entity.getJdbcWrapper();
		
		
		String ordemCargaEnc = "";

		String log = "";
		
		jdbc.openSession();
		
		NativeSql sql = new NativeSql(jdbc);
		
	
			sql.appendSql("SELECT CASE ");
			sql.appendSql("         WHEN( COUNT(DISTINCT ORDEMCARGA) = 1 ) THEN 'S' ");
			sql.appendSql("         ELSE LISTAGG('NF: ' ");
			sql.appendSql("                      || NUMNOTA ");
			sql.appendSql("                      || ' - Ordem de Carga: ' ");
			sql.appendSql("                      || ORDEMCARGA ");
			sql.appendSql("                      , ', ') ");
			sql.appendSql("       END enc_apenas_uma_ordem_carga ");
			sql.appendSql("FROM   TGFCAB CAB ");
			sql.appendSql("WHERE  ORDEMCARGA = :ORDCARGA ");
			sql.appendSql("       AND NUMNOTA IN "
					+ listNota.toString().replace(" ", "").replace("[", "(").replace("]", ")"));
			sql.setNamedParameter("ORDCARGA", ordemCarga);
			
			log = sql.toString(); 

			try {
				ResultSet rs = sql.executeQuery();
				while (rs.next()) {
					ordemCargaEnc = rs.getString("enc_apenas_uma_ordem_carga");
				}

			} catch (Exception e) {
				log = log + "aqui"; 
				//e.printStackTrace();
			}


			List<String> listaOrdemCarga = new ArrayList<String>();
			
			if (ordemCargaEnc != null) {
				String[] strSplit = ordemCargaEnc.split(",");

				ArrayList<String> strList = new ArrayList<String>(Arrays.asList(strSplit));

				

				for (String s : strList) {
					listaOrdemCarga.add(s);

				}
			}

		return listaOrdemCarga;

		//return log;
	}

	public List<String> notasNaoEncontradas(List<String> listNota, String ordemCarga) throws Exception {

		JdbcWrapper jdbc = null;
		EntityFacade entity = EntityFacadeFactory.getDWFFacade();

		jdbc = entity.getJdbcWrapper();

		String listNotaEnc = "";

		jdbc.openSession();
		NativeSql sql = new NativeSql(jdbc);

		sql.appendSql("SELECT LISTAGG(numnota,',')  lista_nota ");
		sql.appendSql("FROM   TGFCAB CAB ");
		sql.appendSql("WHERE  ORDEMCARGA =  :ORDCARGA ");
		sql.appendSql(
				"       AND NUMNOTA IN " + listNota.toString().replace(" ", "").replace("[", "(").replace("]", ")"));

		sql.setNamedParameter("ORDCARGA", ordemCarga);

		try {
			ResultSet rs = sql.executeQuery();
			while (rs.next()) {
				listNotaEnc = rs.getString("lista_nota");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		String[] strSplit = listNotaEnc.split(",");

		ArrayList<String> strList = new ArrayList<String>(Arrays.asList(strSplit));

		List<String> listaNotasNaoEnc = new ArrayList<String>();

		for (int i = 0; i < listNota.size(); i++) {

			boolean enc = false;

			for (String s : strList) {
				if (s.equals(listNota.get(i))) {
					enc = true;
				}

			}

			if (!enc) {
				listaNotasNaoEnc.add(listNota.get(i));
			}

		}

		return listaNotasNaoEnc;

	}

	public String insereRetornoMercadorias(List<String> listNota, String ordemCarga, String codUser) throws Exception {

		JdbcWrapper jdbc = null;
		EntityFacade entity = EntityFacadeFactory.getDWFFacade();

		jdbc = entity.getJdbcWrapper();
		String log = null;
		
			jdbc.openSession();
			NativeSql sql = new NativeSql(jdbc);

			sql.appendSql(
					"SELECT DISTINCT 'Insert into ad_tolpdm (NROUNICO,CODPARC,DTINI,DTFIN,DISPONIVEL,PENDENTE,CODVEI,DTDVL,DHSAIDA,CODUSULIB,LIBERADO,DHLIB,CODUSU) values (''' ");
			sql.appendSql("                || (SELECT MAX(NROUNICO) + 1 ");
			sql.appendSql("                    FROM   AD_TOLPDM) ");
			sql.appendSql("                || ''' , ' ");
			sql.appendSql("                || '' ");
			sql.appendSql("                || (SELECT CODPARCTRANSP codmotorista ");
			sql.appendSql("                    FROM   TGFORD ");
			sql.appendSql("                    WHERE  ORDEMCARGA = CAB.ORDEMCARGA) ");
			sql.appendSql("                ||'' ");
			sql.appendSql("                || ' , ' ");
			sql.appendSql("                || '''' ");
			sql.appendSql("                || TO_DATE(SYSDATE, 'DD/MM/RR') ");
			sql.appendSql("                || '''' ");
			sql.appendSql("                || ', NULL ,''S'',''N'', ' ");
			sql.appendSql("                || '''' ");
			sql.appendSql("                || (SELECT CODVEICULO ");
			sql.appendSql("                    FROM   TGFORD ");
			sql.appendSql("                    WHERE  ORDEMCARGA = CAB.ORDEMCARGA) ");
			sql.appendSql("                || '''' ");
			sql.appendSql("                ||', ''' ");
			sql.appendSql("                || TO_DATE(SYSDATE, 'DD/MM/RR') ");
			sql.appendSql("                || '''' ");
			sql.appendSql("                ||', ''' ");
			sql.appendSql("                || TO_DATE(SYSDATE, 'DD/MM/RR') ");
			sql.appendSql("                || '''' ");
			sql.appendSql("                || ', ''0'',''S'', NULL,''' ");
			sql.appendSql("                || '0'') ' SQL ");
			sql.appendSql("FROM   TGFCAB CAB ");
			sql.appendSql("WHERE  ORDEMCARGA =  :ORDCARGA ");
			sql.appendSql("       AND NUMNOTA IN "
					+ listNota.toString().replace(" ", "").replace("[", "(").replace("]", ")"));
			sql.appendSql("UNION ALL ");
			sql.appendSql(
					"SELECT DISTINCT 'Insert into AD_TIETCM (NROUNICO,CODPARC,DTINC,CODUSU,DTENC,DESTINO) values (' ");
			sql.appendSql("                || '''' ");
			sql.appendSql("                || (SELECT MAX(NROUNICO) + 1 ");
			sql.appendSql("                    FROM   AD_TIETCM) ");
			sql.appendSql("                || '''' ");
			sql.appendSql("                || ', ' ");
			sql.appendSql("                || (SELECT CODPARCTRANSP codmotorista ");
			sql.appendSql("                    FROM   TGFORD ");
			sql.appendSql("                    WHERE  ORDEMCARGA = CAB.ORDEMCARGA) ");
			sql.appendSql("                ||'' ");
			sql.appendSql("                || ' , ' ");
			sql.appendSql("                || '''' ");
			sql.appendSql("                || TO_DATE(SYSDATE, 'DD/MM/RR') ");
			sql.appendSql("                || '''' ");
			sql.appendSql("                || ', ''" + codUser + "''' ");
			sql.appendSql("                || ', ' ");
			sql.appendSql("                || 'NULL, ' ");
			//sql.appendSql("                ||'''B'')' ");
			sql.appendSql("                ||'''E'')' ");
			sql.appendSql("FROM   TGFCAB CAB ");
			sql.appendSql("WHERE  ORDEMCARGA =  :ORDCARGA ");
			sql.appendSql("       AND NUMNOTA IN "
					+ listNota.toString().replace(" ", "").replace("[", "(").replace("]", ")"));
			sql.appendSql("UNION ALL ");
			sql.appendSql(
					"SELECT ' Insert into AD_TIETNF (NROUNICO,NUNOTA,ORDEMCARGA,OS,DEVOLVER,CODPARC,NUMNOTA,FINALIZADO) values (' ");
			sql.appendSql("       || '''' ");
			sql.appendSql("       || (SELECT MAX(NROUNICO) + 1 ");
			sql.appendSql("           FROM   AD_TIETCM) ");
			sql.appendSql("       || '''' ");
			sql.appendSql("       ||', ''' ");
			sql.appendSql("       || NUNOTA ");
			sql.appendSql("       || '''' ");
			sql.appendSql("       ||', ' ");
			sql.appendSql("       || CAB.ORDEMCARGA ");
			sql.appendSql("       ||'' ");
			sql.appendSql("       ||', ''S''' ");
			sql.appendSql("       ||', ''N''' ");
			sql.appendSql("       ||', ''' ");
			sql.appendSql("       || CODPARC ");
			sql.appendSql("       || '''' ");
			sql.appendSql("       ||', ''' ");
			sql.appendSql("       || NUMNOTA ");
			sql.appendSql("       || '''' ");
			sql.appendSql("       ||', NULL)' ");
			sql.appendSql("FROM   TGFCAB CAB ");
			sql.appendSql("WHERE  ORDEMCARGA =  :ORDCARGA ");
			sql.appendSql("       AND NUMNOTA IN "
					+ listNota.toString().replace(" ", "").replace("[", "(").replace("]", ")"));
			sql.appendSql("UNION ALL ");
			sql.appendSql(
					"SELECT DISTINCT 'UPDATE ad_tolpdmd SET TIPO = ''DEV'', ORDEMCARGA =  ORDEMCARGA || '','' || ''ORDEMCARGNOVA'' WHERE NROUNICO = ' ");
			sql.appendSql("       || (SELECT MAX(NROUNICO) + 1 ");
			sql.appendSql("           FROM   AD_TOLPDM) ");
			sql.appendSql("       ||' AND ORDEMCARGA NOT LIKE ''%' || ordemcarga || '%'' AND tipo = ''DEV''' ");
			sql.appendSql("FROM   TGFCAB CAB ");
			sql.appendSql("WHERE  ORDEMCARGA =  :ORDCARGA ");
			sql.appendSql("       AND NUMNOTA IN "
					+ listNota.toString().replace(" ", "").replace("[", "(").replace("]", ")"));

			sql.setNamedParameter("ORDCARGA", ordemCarga);

			try {

				ResultSet rs = sql.executeQuery();

				NativeSql execScript = new NativeSql(jdbc);

				while (rs.next()) {
					
					execScript.executeUpdate(rs.getString("SQL"));

				}

			} catch (IOException | SQLException e) {				
				log = e.getMessage();
				//throw e;
			}	
		
		return log;

	}

}
