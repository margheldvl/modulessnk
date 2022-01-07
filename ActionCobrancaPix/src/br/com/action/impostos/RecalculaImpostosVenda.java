package br.com.action.impostos;

import java.sql.ResultSet;
import java.sql.SQLException;

import br.com.sankhya.jape.EntityFacade;
import br.com.sankhya.jape.dao.JdbcWrapper;
import br.com.sankhya.jape.sql.NativeSql;
import br.com.sankhya.modelcore.util.EntityFacadeFactory;

public class RecalculaImpostosVenda {

	public RecalculaImpostosVenda() {
	}
	
	public void recalculaImpostosPISPeriodo( String dataIni, String datafim ) throws Exception {
		execCalculoPIS(dataIni,datafim);
	}
	
	public void recalculaImpostosPISFretePeriodo( String dataIni, String datafim ) throws Exception {
		execCalculoPISFrete(dataIni,datafim);
	}
	
	private void execCalculoPIS( String dataIni, String datafim ) throws Exception {
		
		JdbcWrapper jdbc = null;
		EntityFacade entity = EntityFacadeFactory.getDWFFacade();

		jdbc = entity.getJdbcWrapper();
		
		try {
			jdbc.openSession();
			NativeSql sqlRegistros = new NativeSql(jdbc);
			sqlRegistros.appendSql("with cte_devolucoes as"); 
			sqlRegistros.appendSql("(");
			sqlRegistros.appendSql("select tgfdin.*");
			sqlRegistros.appendSql("  from tgfcab");
			sqlRegistros.appendSql("  join tgfite on tgfite.nunota = tgfcab.nunota");
			sqlRegistros.appendSql("  join tgfdin on tgfdin.nunota = tgfite.nunota and tgfdin.sequencia = tgfite.sequencia");
			sqlRegistros.appendSql("  where codtipoper in (171,912,932)");  
			sqlRegistros.appendSql("    and tipmov = 'V'"); 
			sqlRegistros.appendSql("    and codcfo = 5102");
			sqlRegistros.appendSql("    and CAST(dtentsai AS DATE) >= CAST(:DATINI AS DATE) ");
			sqlRegistros.appendSql("    and CAST(dtentsai) <= CAST(:DATFIM AS DATE ) ");
			sqlRegistros.appendSql("), cte_imposto_valor_base as ");
			sqlRegistros.appendSql("(");
			sqlRegistros.appendSql("    select *"); 
			sqlRegistros.appendSql("      from cte_devolucoes"); 
			sqlRegistros.appendSql("      where codimp = 1 		");			
			sqlRegistros.appendSql("), cte_devolucoes_cst_50 as ");
			sqlRegistros.appendSql("(");
			sqlRegistros.appendSql("			   select * from cte_devolucoes where cst = 50 and codinc <> 3");
			sqlRegistros.appendSql("), cte_final as ");
			sqlRegistros.appendSql("(");
			sqlRegistros.appendSql("select cte_devolucoes_cst_50.nunota,");
			sqlRegistros.appendSql("       cte_devolucoes_cst_50.sequencia,");
			sqlRegistros.appendSql("       cte_devolucoes_cst_50.codimp,");
			sqlRegistros.appendSql("       cte_devolucoes_cst_50.base \"base_cst50\",");
			sqlRegistros.appendSql("       cte_devolucoes_cst_50.basered \"basered_cst50\","); 
			sqlRegistros.appendSql("       cte_devolucoes_cst_50.aliquota, ");
			sqlRegistros.appendSql("       cte_devolucoes_cst_50.codinc \"codinc_cst50\",");
			sqlRegistros.appendSql("       cte_imposto_valor_base.base \"base_imp1\",");
			sqlRegistros.appendSql("       cte_imposto_valor_base.valor \"valor_imp1\",");
			sqlRegistros.appendSql("       cte_imposto_valor_base.codinc, ");
			sqlRegistros.appendSql("       cte_devolucoes_cst_50.base - cte_imposto_valor_base.valor  \"BaseRedNova\"");
			sqlRegistros.appendSql("  from cte_devolucoes_cst_50 ");
			sqlRegistros.appendSql("  join cte_imposto_valor_base on cte_imposto_valor_base.nunota = cte_devolucoes_cst_50.nunota "); 
			sqlRegistros.appendSql("                              and cte_devolucoes_cst_50.sequencia = cte_imposto_valor_base.sequencia");
			sqlRegistros.appendSql("  where cte_imposto_valor_base.codinc <> 3");
			sqlRegistros.appendSql(")");
			sqlRegistros.appendSql("SELECT * FROM cte_final");			
			sqlRegistros.setNamedParameter("DATINI", dataIni);
            sqlRegistros.setNamedParameter("DATFIM", datafim);

            ResultSet rs = sqlRegistros.executeQuery();
            while(rs.next()) {
    
    			NativeSql sqlUpdate = new NativeSql(jdbc);   			
    			sqlUpdate.appendSql(" UPDATE TGFDIN SET BASERED = :BASEREDNOVA, ");
    			sqlUpdate.appendSql("VALOR = ( :BASEREDNOVA * (ALIQUOTA / 100) ), ");
    			sqlUpdate.appendSql("VLRCRED = ( :BASEREDNOVA * (ALIQUOTA / 100) )"); 
    			sqlUpdate.appendSql("WHERE TGFDIN.NUNOTA = :NUNOTA");
    			sqlUpdate.appendSql("  AND TGFDIN.SEQUENCIA = :SEQUENCIA ");
    			sqlUpdate.appendSql("  AND TGFDIN.CODIMP = :CODIMP ");
    			sqlUpdate.appendSql("  AND TGFDIN.CODINC <> 3");
    			  
    			sqlUpdate.setNamedParameter("BASEREDNOVA", rs.getBigDecimal("\"BaseRedNova\""));
    			sqlUpdate.setNamedParameter("NUNOTA", rs.getString("NUNOTA"));
    			sqlUpdate.setNamedParameter("SEQUENCIA", rs.getString("SEQUENCIA"));
    			sqlUpdate.setNamedParameter("CODIMP", rs.getString("CODIMP"));
    	//		String sql = sqlUpdate.toString();
    			sqlUpdate.executeUpdate();            	
            }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	
	private void execCalculoPISFrete( String dataIni, String datafim ) throws Exception {
		
		JdbcWrapper jdbc = null;
		EntityFacade entity = EntityFacadeFactory.getDWFFacade();

		jdbc = entity.getJdbcWrapper();
		
		try {
			jdbc.openSession();
			NativeSql sqlRegistros = new NativeSql(jdbc);
			sqlRegistros.appendSql("with cte_devolucoes as"); 
			sqlRegistros.appendSql("(");
			sqlRegistros.appendSql("select tgfdin.*");
			sqlRegistros.appendSql("  from tgfcab");
			sqlRegistros.appendSql("  join tgfite on tgfite.nunota = tgfcab.nunota");
			sqlRegistros.appendSql("  join tgfdin on tgfdin.nunota = tgfite.nunota and tgfdin.sequencia = tgfite.sequencia");
			sqlRegistros.appendSql("  where codtipoper in (171,912,932)");  
			sqlRegistros.appendSql("    and tipmov = 'V'"); 
			sqlRegistros.appendSql("    and codcfo = 5102");
			sqlRegistros.appendSql("    and CAST(dtentsai AS DATE) >= CAST(:DATINI AS DATE) ");
			sqlRegistros.appendSql("    and CAST(dtentsai) <= CAST(:DATFIM AS DATE ) ");
			sqlRegistros.appendSql("), cte_imposto_valor_base as ");
			sqlRegistros.appendSql("(");
			sqlRegistros.appendSql("    select *"); 
			sqlRegistros.appendSql("      from cte_devolucoes"); 
			sqlRegistros.appendSql("      where codimp = 1 		");			
			sqlRegistros.appendSql("), cte_devolucoes_cst_50 as ");
			sqlRegistros.appendSql("(");
			sqlRegistros.appendSql("			   select * from cte_devolucoes where cst = 50 and codinc = 3");
			sqlRegistros.appendSql("), cte_final as ");
			sqlRegistros.appendSql("(");
			sqlRegistros.appendSql("select cte_devolucoes_cst_50.nunota,");
			sqlRegistros.appendSql("       cte_devolucoes_cst_50.sequencia,");
			sqlRegistros.appendSql("       cte_devolucoes_cst_50.codimp,");
			sqlRegistros.appendSql("       cte_devolucoes_cst_50.base \"base_cst50\",");
			sqlRegistros.appendSql("       cte_devolucoes_cst_50.basered \"basered_cst50\","); 
			sqlRegistros.appendSql("       cte_devolucoes_cst_50.aliquota, ");
			sqlRegistros.appendSql("       cte_devolucoes_cst_50.codinc \"codinc_cst50\",");
			sqlRegistros.appendSql("       cte_imposto_valor_base.base \"base_imp1\",");
			sqlRegistros.appendSql("       cte_imposto_valor_base.valor \"valor_imp1\",");
			sqlRegistros.appendSql("       cte_imposto_valor_base.codinc, ");
			sqlRegistros.appendSql("       cte_devolucoes_cst_50.base - cte_imposto_valor_base.valor  \"BaseRedNova\"");
			sqlRegistros.appendSql("  from cte_devolucoes_cst_50 ");
			sqlRegistros.appendSql("  join cte_imposto_valor_base on cte_imposto_valor_base.nunota = cte_devolucoes_cst_50.nunota "); 
			sqlRegistros.appendSql("                              and cte_devolucoes_cst_50.sequencia = cte_imposto_valor_base.sequencia");
			sqlRegistros.appendSql("  where cte_imposto_valor_base.codinc = 3");
			sqlRegistros.appendSql(")");
			sqlRegistros.appendSql("SELECT * FROM cte_final");			
			sqlRegistros.setNamedParameter("DATINI", dataIni);
            sqlRegistros.setNamedParameter("DATFIM", datafim);

            ResultSet rs = sqlRegistros.executeQuery();
            while(rs.next()) {
    
    			NativeSql sqlUpdate = new NativeSql(jdbc);   			
    			sqlUpdate.appendSql(" UPDATE TGFDIN SET BASERED = :BASEREDNOVA, ");
    			sqlUpdate.appendSql("VALOR = ( :BASEREDNOVA * (ALIQUOTA / 100) ), ");
    			sqlUpdate.appendSql("VLRCRED = ( :BASEREDNOVA * (ALIQUOTA / 100) )"); 
    			sqlUpdate.appendSql("WHERE TGFDIN.NUNOTA = :NUNOTA");
    			sqlUpdate.appendSql("  AND TGFDIN.SEQUENCIA = :SEQUENCIA ");
    			sqlUpdate.appendSql("  AND TGFDIN.CODIMP = :CODIMP ");
    			sqlUpdate.appendSql("  AND TGFDIN.CODINC = 3");
    			  
    			sqlUpdate.setNamedParameter("BASEREDNOVA", rs.getBigDecimal("\"BaseRedNova\""));
    			sqlUpdate.setNamedParameter("NUNOTA", rs.getString("NUNOTA"));
    			sqlUpdate.setNamedParameter("SEQUENCIA", rs.getString("SEQUENCIA"));
    			sqlUpdate.setNamedParameter("CODIMP", rs.getString("CODIMP"));
    //			String sql = sqlUpdate.toString();
    			sqlUpdate.executeUpdate();            	
            }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
		
	

}
