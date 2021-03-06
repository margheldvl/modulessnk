package br.com.dvl.action.pix.cobranca.listeners;

import br.com.action.pix.service.BuscaDadosPIX;
import br.com.action.pix.service.PIXDAO;
import br.com.sankhya.jape.EntityFacade;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.cuckoo.core.ScheduledAction;
import org.cuckoo.core.ScheduledActionContext;

import br.com.sankhya.jape.dao.JdbcWrapper;
import br.com.sankhya.jape.sql.NativeSql;
import br.com.sankhya.modelcore.util.EntityFacadeFactory;

public class ConfirmaPagamentoPIX implements ScheduledAction{

	@Override
	public void onTime(ScheduledActionContext arg0) {
        try {
			buscaDadosPIX();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
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
