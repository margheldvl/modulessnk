package br.com.dvl.action.pix.cobranca.listeners;

import java.math.BigDecimal;

import br.com.action.pix.service.RegistraCobranca;
import br.com.sankhya.extensions.eventoprogramavel.EventoProgramavelJava;
import br.com.sankhya.jape.EntityFacade;
import br.com.sankhya.jape.event.PersistenceEvent;
import br.com.sankhya.jape.event.TransactionContext;
import br.com.sankhya.jape.vo.DynamicVO;
import br.com.sankhya.jape.wrapper.JapeFactory;
import br.com.sankhya.jape.wrapper.JapeWrapper;
import br.com.sankhya.modelcore.util.DynamicEntityNames;
import br.com.sankhya.modelcore.util.EntityFacadeFactory;

public class CobrancaImediataPixEvent implements EventoProgramavelJava {

	@Override
	public void afterDelete(PersistenceEvent arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterInsert(PersistenceEvent arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterUpdate(PersistenceEvent event) throws Exception {
		
		EntityFacade dwfEntityFacade = EntityFacadeFactory.getDWFFacade();

		DynamicVO newVO = (DynamicVO) event.getVo();			
		BigDecimal codTipVenda = newVO.asBigDecimal("CODTIPVENDA");
		if ( codTipVenda != null  ) {
			DynamicVO tipVendaVO = (DynamicVO) dwfEntityFacade.findEntityByPrimaryKeyAsVO("TipoNegociacao", new Object[]{newVO.asBigDecimal("CODTIPVENDA"), newVO.asTimestamp("DHTIPVENDA")});

			if (  ( tipVendaVO.asString("AD_PIX") != null &&  tipVendaVO.asString("AD_PIX").equals("S") )  &&  
				  ( newVO.asString("STATUSNFE")   != null &&  newVO.asString("STATUSNFE").equals("A") ) && 
		          ( newVO.asString("STATUSNOTA")  != null &&  newVO.asString("STATUSNOTA").equals("L") )
		       ) {
					RegistraCobranca cob = new RegistraCobranca(); 
				    cob.setCobrancaTLS(newVO.asBigDecimal("NUNOTA"));			
			     }
		}
	}

	@Override
	public void beforeCommit(TransactionContext arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeDelete(PersistenceEvent arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeInsert(PersistenceEvent arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeUpdate(PersistenceEvent arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
