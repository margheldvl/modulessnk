package br.com.dvl.action.pix.cobranca.listeners;

import br.com.sankhya.jape.EntityFacade;

import java.sql.SQLException;

import org.cuckoo.core.ScheduledAction;
import org.cuckoo.core.ScheduledActionContext;

import br.com.sankhya.jape.dao.JdbcWrapper;
import br.com.sankhya.jape.sql.NativeSql;
import br.com.sankhya.modelcore.util.EntityFacadeFactory;

public class ConfirmaPagamentoPIX implements ScheduledAction{

	@Override
	public void onTime(ScheduledActionContext arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public void buscaDadosPIX() {
		JdbcWrapper jdbc = null;
		EntityFacade entity = EntityFacadeFactory.getDWFFacade();

		jdbc = entity.getJdbcWrapper();
		try {
			jdbc.openSession();
			NativeSql sqlpix = new NativeSql(jdbc);
            sqlpix.appendSql("SELECT TXID FROM AD_PXPG WHERE ");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
