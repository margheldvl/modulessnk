package br.com.action.marcacaonota;

import br.com.sankhya.jape.EntityFacade;
import br.com.sankhya.jape.dao.JdbcWrapper;
import br.com.sankhya.jape.sql.NativeSql;
import br.com.sankhya.modelcore.util.EntityFacadeFactory;

public class MarcacaoNotaFerrero {

	public MarcacaoNotaFerrero() {
		// TODO Auto-generated constructor stub
	}
	
	public void marcarNota(String dataref, String NuMarcacao ) {
		
		JdbcWrapper jdbc = null;
		EntityFacade entity = EntityFacadeFactory.getDWFFacade();

		jdbc = entity.getJdbcWrapper();
		
		NativeSql sqlUpdate = new NativeSql(jdbc);   			
		
		sqlUpdate.appendSql("	UPDATE TGFCAB SET AD_NUEISID = :NUMARC "); 
		sqlUpdate.appendSql("				WHERE NUNOTA IN ( ");
		sqlUpdate.appendSql("				select  TGFCAB.nunota ");
		sqlUpdate.appendSql("				  from AD_TGFSIDN  ");
		sqlUpdate.appendSql("				  JOIN TGFCAB on TGFCAB.NUNOTA = AD_TGFSIDN.NUNOTA ");
	    sqlUpdate.appendSql("				  where CAST(AD_TGFSIDN.dtref AS DATE ) = CAST(:DTREF AS DATE) and AD_NUEISID is null ) ");
		sqlUpdate.setNamedParameter("NUMARC", NuMarcacao);
		sqlUpdate.setNamedParameter("DTREF", dataref);
		try {
			sqlUpdate.executeUpdate();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}

}
