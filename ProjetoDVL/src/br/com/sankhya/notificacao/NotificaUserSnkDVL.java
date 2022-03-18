package br.com.sankhya.notificacao;

import java.math.BigDecimal;

import com.sankhya.util.TimeUtils;

import br.com.sankhya.extensions.actionbutton.ContextoAcao;
import br.com.sankhya.extensions.actionbutton.Registro;

public class NotificaUserSnkDVL {
	public void InsereNoticacao(final ContextoAcao contexto) throws Exception {
		Registro avisoSistema = contexto.novaLinha("TSIAVI");
		avisoSistema.setCampo("NUAVISO",null);
		avisoSistema.setCampo("CODUSUEMETENTE", BigDecimal.valueOf(0)); // -1 exibirá para todos os usuários
		avisoSistema.setCampo("DESCRICAO","Mensagem de erro" + "<br>" + "mensagem");
		avisoSistema.setCampo("CODUSU",contexto.getUsuarioLogado().toString());
		avisoSistema.setCampo("DHCRIACAO",TimeUtils.getNow());
		avisoSistema.setCampo("IDENTIFICADOR","PERSONALIZADO");
		avisoSistema.setCampo("IMPORTANCIA",BigDecimal.valueOf(3));
		avisoSistema.setCampo("SOLUCAO","Mensagem para solucionar o problema");
		avisoSistema.setCampo("TIPO","P");
		avisoSistema.setCampo("TITULO","Produção de Equipamentos - Atualização de Custos");
		avisoSistema.save();	
	}	

}
