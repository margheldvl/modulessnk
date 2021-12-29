package br.com.dvl.action.pix.cobranca.listeners;

import java.sql.ResultSet;
import java.sql.SQLException;

import br.com.action.pix.service.BuscaDadosPIX;
import br.com.action.pix.service.PIXDAO;
import br.com.sankhya.extensions.actionbutton.AcaoRotinaJava;
import br.com.sankhya.extensions.actionbutton.ContextoAcao;
import br.com.sankhya.jape.EntityFacade;
import br.com.sankhya.jape.dao.JdbcWrapper;
import br.com.sankhya.jape.sql.NativeSql;
import br.com.sankhya.modelcore.util.EntityFacadeFactory;

public class ConfirmaPagamentoAction implements AcaoRotinaJava {

	@Override
	public void doAction(ContextoAcao arg0) throws Exception {
		buscaDadosPIX();		
	} 
	
	public void buscaDadosPIX() throws Exception {
		JdbcWrapper jdbc = null;
		EntityFacade entity = EntityFacadeFactory.getDWFFacade();

		jdbc = entity.getJdbcWrapper();
		try {
			jdbc.openSession();
			NativeSql sqlpix = new NativeSql(jdbc);
            sqlpix.appendSql("SELECT TXID FROM AD_PXPG WHERE STATUS = :STATUS");
            sqlpix.setNamedParameter("STATUS", "ATIVA");
           
            ResultSet rs = sqlpix.executeQuery();
            while(rs.next()) {
            	BuscaDadosPIX bp = new BuscaDadosPIX();
            	String pxr = bp.getStatusCobranca(rs.getString("TXID"));
            	String txid = rs.getString("TXID");
            	PIXDAO pd = new PIXDAO();
            	pd.atualizaCobrancaDAO(pxr,txid);
            }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	

}
