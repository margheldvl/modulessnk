package br.com.atcion.rotinabd;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import br.com.sankhya.jape.EntityFacade;
import br.com.sankhya.jape.dao.JdbcWrapper;
import br.com.sankhya.jape.sql.NativeSql;
import br.com.sankhya.modelcore.util.EntityFacadeFactory;

public class NotificaUsuarioDAO {

	public String insereNotificacaoUsuario(NotificaUsuario notificaUser) throws Exception {

		JdbcWrapper jdbc = null;
		EntityFacade entity = EntityFacadeFactory.getDWFFacade();

		jdbc = entity.getJdbcWrapper();

		String log = "";

		try {
			jdbc.openSession();
			NativeSql sql = new NativeSql(jdbc);

			sql.appendSql("INSERT INTO TSIAVI ");
			sql.appendSql("            (NUAVISO, ");
			sql.appendSql("             TITULO, ");
			sql.appendSql("             DESCRICAO, ");
			sql.appendSql("             SOLUCAO, ");
			sql.appendSql("             IDENTIFICADOR, ");
			sql.appendSql("             IMPORTANCIA, ");
			sql.appendSql("             CODUSU, ");
			sql.appendSql("             CODGRUPO, ");
			sql.appendSql("             TIPO, ");
			sql.appendSql("             DHCRIACAO, ");
			sql.appendSql("             CODUSUREMETENTE, ");
			sql.appendSql("             NUAVISOPAI, ");
			sql.appendSql("             DTEXPIRACAO, ");
			sql.appendSql("             DTNOTIFICACAO, ");
			sql.appendSql("             ORDEM) ");
			sql.appendSql("VALUES      ((SELECT MAX(NUAVISO) + 1 proximo_nu_aviso FROM TSIAVI ), ");
			sql.appendSql("             :Titulo, ");
			sql.appendSql("             :Descricao, ");
			sql.appendSql("             :Solucao, ");
			// sql.appendSql(" 'PERSONALIZADO', ");
			sql.appendSql("             :Identificador, ");
			sql.appendSql("             :Importancia, ");
			sql.appendSql("             :Codusu, ");
			sql.appendSql("             :CodGrupo, ");
			// sql.appendSql(" 'P', ");
			sql.appendSql("             :Tipo, ");
			sql.appendSql("             TO_DATE(:DhCriacao, 'DD/MM/RR HH24:MI:SS'), ");
			sql.appendSql("             :Codusuremetente, ");
			sql.appendSql("             :Nuavisopai, ");
			sql.appendSql("             NULL, ");
			sql.appendSql("             TO_DATE(:Dtnotificacao, 'DD/MM/RR HH24:MI:SS'), ");
			// sql.appendSql(" NULL, ");
			sql.appendSql("             NULL)  ");

			sql.setNamedParameter("Titulo", notificaUser.getTitulo());
			sql.setNamedParameter("Descricao", notificaUser.getDescricao());
			sql.setNamedParameter("Solucao", notificaUser.getSolucao());
			sql.setNamedParameter("Identificador", notificaUser.getIdentificador());
			sql.setNamedParameter("Importancia", notificaUser.getImportancia());
			sql.setNamedParameter("Codusu", notificaUser.getCodusu());
			sql.setNamedParameter("CodGrupo", notificaUser.getCodgrupo());
			sql.setNamedParameter("Tipo", notificaUser.getTipo());

			Date agora = new Date();

			String pattern = "dd/MM/yyyy HH:mm:ss";
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
			String dataFormatada = simpleDateFormat.format(agora);

			sql.setNamedParameter("DhCriacao", dataFormatada);
			sql.setNamedParameter("Codusuremetente", notificaUser.getCodusuremetente());
			sql.setNamedParameter("Nuavisopai", notificaUser.getNuavisopai());
			// sql.setNamedParameter("Dtexpiracao", notificaUser.getDtexpiracao());
			sql.setNamedParameter("Dtnotificacao", dataFormatada);
			// sql.setNamedParameter("Ordem", notificaUser.getOrdem());

			try {

				sql.executeUpdate();

			} catch (Exception e) {
				log = sql.toString() + " Mensagem: " + e.getMessage();
				// e.printStackTrace();
			}

		} catch (SQLException e) {
			log = log + " / " + log + " Mensagem: " + e.getMessage();
			// e.printStackTrace();
		}
		return log;

	}

}
