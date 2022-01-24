package br.com.action.pix.service;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

import br.com.sankhya.jape.EntityFacade;
import br.com.sankhya.jape.core.JapeSession;
import br.com.sankhya.jape.core.JapeSession.SessionHandle;
import br.com.sankhya.jape.dao.JdbcWrapper;
import br.com.sankhya.jape.sql.NativeSql;
import br.com.sankhya.jape.vo.DynamicVO;
import br.com.sankhya.jape.wrapper.JapeFactory;
import br.com.sankhya.jape.wrapper.JapeWrapper;
import br.com.sankhya.modelcore.MGEModelException;
import br.com.sankhya.modelcore.util.DynamicEntityNames;
import br.com.sankhya.modelcore.util.EntityFacadeFactory;

public class PIXDAO {

	public PIXDAO() {
		// TODO Auto-generated constructor stub
	}
    
	public void criaCobrancaDAO(String pixResponse, Number nuNota ) throws Exception {
         setCobrancaDAO(pixResponse, nuNota); 		
	}
	
	public void atualizaCobrancaDAO(String pixResponse, String txid ) throws Exception {
		updateCobrancaDAO(pixResponse,txid);
	}
	
	
	@SuppressWarnings("unchecked")
	private void setCobrancaDAO( String pixResponse, Number nuNota ) throws Exception {

		
		JdbcWrapper jdbc = null;
		NativeSql sql = null;
		SessionHandle hnd = null;
		
		DynamicVO notaVO;
		
 		JapeWrapper notaDAO = JapeFactory.dao(DynamicEntityNames.CABECALHO_NOTA);
		notaVO = notaDAO.findByPK(nuNota);
		
        Gson gsonToken = new Gson(); 
        Map<String,Object> map = new HashMap<String,Object>();
        Map<String,Object> mapV = new HashMap<String,Object>();
        Map<String,Object> mapVQ = new HashMap<String,Object>();

         
        map  = (Map<String,Object>) gsonToken.fromJson(pixResponse, map.getClass());
        mapV  = (Map<String,Object>) gsonToken.fromJson( map.get("valor").toString(), mapV.getClass()); 
        BuscaDadosPIX qrcAPI = new BuscaDadosPIX();
        String qrcodeJson = qrcAPI.getQRC(map.get("txid").toString());
        mapVQ = (Map<String,Object>) gsonToken.fromJson(qrcodeJson, mapVQ.getClass()); 
        
        try {
			  hnd = JapeSession.open();
		      hnd.setFindersMaxRows(-1);
			  EntityFacade entity = EntityFacadeFactory.getDWFFacade();
			  jdbc = entity.getJdbcWrapper();
			  jdbc.openSession();
			  sql = new NativeSql(jdbc);
			  sql.appendSql("INSERT INTO AD_PXPG ( NUNOTA, VRTOT, STATUS, TXID, PIXCC, QRC64 ) VALUES "
			  		      + "(:NUNOTA, :VRTOT, :STATUS, :TXID, :PIXCC, :QRC64 )");

			  sql.setNamedParameter("NUNOTA", notaVO.asBigDecimal("NUNOTA"));
			  sql.setNamedParameter("VRTOT", mapV.get("original"));
			  sql.setNamedParameter("STATUS", map.get("status").toString());
			  sql.setNamedParameter("TXID", map.get("txid").toString());
			  sql.setNamedParameter("PIXCC", map.get("pixCopiaECola"));
			  sql.setNamedParameter("QRC64", mapVQ.get("imagem_base64"));

			 
			  sql.executeUpdate();
			
        } catch (Exception e) {
			MGEModelException.throwMe(e);
		} finally {
			NativeSql.releaseResources(sql);
			JdbcWrapper.closeSession(jdbc);
			JapeSession.close(hnd);

		}

			
	}
	
@SuppressWarnings("unchecked")
private void updateCobrancaDAO( String pixResponse,String txid ) throws Exception {
	   JdbcWrapper jdbc = null;
	   NativeSql sql = null;

	
        Gson gsonToken = new Gson(); 
        Map<String,Object> map = new HashMap<String,Object>();
                  
        map  = (Map<String,Object>) gsonToken.fromJson(pixResponse, map.getClass());
        
        if ( map.get("pix").toString() != "[]" ) {     
        	
        	  EntityFacade entity = EntityFacadeFactory.getDWFFacade();
			  jdbc = entity.getJdbcWrapper();
			  jdbc.openSession();
			  sql = new NativeSql(jdbc);
			  sql.appendSql("UPDATE AD_PXPG SET STATUS = :STATUS WHERE TXID = :TXID");
			  sql.setNamedParameter("STATUS", "PAGO");
			  sql.setNamedParameter("TXID", txid);
			  sql.executeUpdate();

        }
	}
}
