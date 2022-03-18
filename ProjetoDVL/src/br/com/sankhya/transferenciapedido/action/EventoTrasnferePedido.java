package br.com.sankhya.transferenciapedido.action;

import br.com.sankhya.extensions.eventoprogramavel.EventoProgramavelJava;
//import br.com.sankhya.jape.EntityFacade;
import br.com.sankhya.jape.event.PersistenceEvent;
import br.com.sankhya.jape.event.TransactionContext;
//import br.com.sankhya.jape.util.JapeSessionContext;
import br.com.sankhya.jape.vo.DynamicVO;
//import br.com.sankhya.jape.vo.EntityVO;
import br.com.sankhya.jape.vo.PrePersistEntityState;
import br.com.sankhya.modelcore.MGEModelException;
import br.com.sankhya.modelcore.comercial.ContextoRegra;
import br.com.sankhya.modelcore.util.DynamicEntityNames;
//import br.com.sankhya.modelcore.util.EntityFacadeFactory;
//import br.com.sankhya.modelcore.util.MGECoreParameter;
//import com.sankhya.util.StringUtils;
//import com.sankhya.util.TimeUtils;
//import java.math.BigDecimal;

// TODO  - NÃO ESTÁ EM USO  ***** NÃO ESTÁ EM USO  ***** NÃO ESTÁ EM USO  ***** NÃO ESTÁ EM USO  *****
// TODO  - NÃO ESTÁ EM USO  ***** NÃO ESTÁ EM USO  ***** NÃO ESTÁ EM USO  ***** NÃO ESTÁ EM USO  *****
// TODO  - NÃO ESTÁ EM USO  ***** NÃO ESTÁ EM USO  ***** NÃO ESTÁ EM USO  ***** NÃO ESTÁ EM USO  *****


public abstract class EventoTrasnferePedido implements EventoProgramavelJava {
	@Override
	public void beforeDelete(PersistenceEvent event) throws Exception {
		// TODO: Antes de excluir um registro.
	}

	public void beforeInsert(PersistenceEvent event) throws Exception {
		// TODO: Antes de inserir um registro.
	}

	public void beforeUpdate(PersistenceEvent event) throws Exception {
		// TODO: Antes de atualizar um registro.
	}

	public void afterInsert(PersistenceEvent event) throws Exception {
		// TODO: Depois de inserir um registro.
	}

//  public void afterUpdate(PersistenceEvent event) throws Exception {
//      //TODO: Depois de atualizar um registro.
//  }

	public void afterUpdate(ContextoRegra ctx) throws Exception {

		String statusNota = ctx.getPrePersistEntityState().getNewVO().asString("STATUSNOTA");

		if (statusNota.equals("L")) {

			throw new MGEModelException("Confirmando nota");
		}

		PrePersistEntityState state = ctx.getPrePersistEntityState();

		final DynamicVO oldVO = state.getOldVO();
		final boolean isCabecalho = oldVO.getValueObjectID().indexOf(DynamicEntityNames.CABECALHO_NOTA) > -1;
		final boolean isItem = oldVO.getValueObjectID().indexOf(DynamicEntityNames.ITEM_NOTA) > -1;
		final boolean isFinanceiro = oldVO.getValueObjectID().indexOf(DynamicEntityNames.FINANCEIRO) > -1;

		if (isCabecalho) {
			// BarramentoRegra.clearSituacaoLaudo(oldVO);
		}

		if (isItem) {

		}

		if (isFinanceiro) {

		}
	}

	public void afterDelete(PersistenceEvent event) throws Exception {
		// TODO: Depois de excluir um registro.
	}

	public void beforeCommit(TransactionContext tranCtx) throws Exception {
		// TODO: Antes de "commitar" um registro.

	}

}