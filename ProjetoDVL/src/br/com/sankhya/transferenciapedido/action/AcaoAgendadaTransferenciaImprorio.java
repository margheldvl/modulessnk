package br.com.sankhya.transferenciapedido.action;

import org.cuckoo.core.ScheduledAction;
import org.cuckoo.core.ScheduledActionContext;

import br.com.sankhya.transferenciapedido.wbean.TransferenciaPedidoDVLWBean;

//import br.com.sankhya.modelcore.util.EntityFacadeFactory;
//import br.com.sankhya.modelcore.util.MGECoreParameter;
//import com.sankhya.util.StringUtils;
//import com.sankhya.util.TimeUtils;
//import java.math.BigDecimal;

public class AcaoAgendadaTransferenciaImprorio implements ScheduledAction {

	@Override
	public void onTime(ScheduledActionContext arg0) {

		try {
			
			TransferenciaPedidoDVLWBean transferenciaBean = new TransferenciaPedidoDVLWBean();
			transferenciaBean.InsereTransferenciaPedidoWBean();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
