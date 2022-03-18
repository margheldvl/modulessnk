package br.com.atcion.rotinabd;

import java.math.BigDecimal;

import com.sankhya.util.TimeUtils;

import br.com.sankhya.extensions.actionbutton.ContextoAcao;
import br.com.sankhya.extensions.actionbutton.Registro;

public class NotificaUserSnkDVL {
	public void InsereNoticacao(final ContextoAcao contexto) throws Exception {
		Registro avisoSistema = contexto.novaLinha("TSIAVI");
		avisoSistema.setCampo("NUAVISO",null);
		avisoSistema.setCampo("CODUSUEMETENTE", BigDecimal.valueOf(0)); // -1 exibir� para todos os usu�rios
		avisoSistema.setCampo("DESCRICAO","Mensagem de erro" + "<br>" + "mensagem");
		avisoSistema.setCampo("CODUSU",contexto.getUsuarioLogado().toString());
		avisoSistema.setCampo("DHCRIACAO",TimeUtils.getNow());
		avisoSistema.setCampo("IDENTIFICADOR","PERSONALIZADO");
		avisoSistema.setCampo("IMPORTANCIA",BigDecimal.valueOf(3));
		avisoSistema.setCampo("SOLUCAO","Mensagem para solucionar o problema");
		avisoSistema.setCampo("TIPO","P");
		avisoSistema.setCampo("TITULO","Produ��o de Equipamentos - Atualiza��o de Custos");
		avisoSistema.save();	
	}	

}
