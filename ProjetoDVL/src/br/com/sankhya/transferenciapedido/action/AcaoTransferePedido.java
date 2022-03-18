package br.com.sankhya.transferenciapedido.action;

import br.com.sankhya.extensions.actionbutton.AcaoRotinaJava;
import br.com.sankhya.extensions.actionbutton.ContextoAcao;
import br.com.sankhya.transferenciapedido.wbean.TransferenciaPedidoDVLWBean;

//import br.com.sankhya.modelcore.util.EntityFacadeFactory;
//import br.com.sankhya.modelcore.util.MGECoreParameter;
//import com.sankhya.util.StringUtils;
//import com.sankhya.util.TimeUtils;
//import java.math.BigDecimal;

public class AcaoTransferePedido implements AcaoRotinaJava {

	public void doAction(final ContextoAcao ctx) throws Exception {
		StringBuffer mensagem = new StringBuffer();

		TransferenciaPedidoDVLWBean transferenciaBean = new TransferenciaPedidoDVLWBean();
		mensagem.append(transferenciaBean.InsereTransferenciaPedidoWBean());

		ctx.setMensagemRetorno(mensagem.toString());

	}

}
