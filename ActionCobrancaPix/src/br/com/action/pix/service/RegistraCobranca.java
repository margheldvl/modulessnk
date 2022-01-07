package br.com.action.pix.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyStore;
import java.security.SecureRandom;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;

import org.apache.commons.lang.RandomStringUtils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import br.com.sankhya.jape.vo.DynamicVO;
import br.com.sankhya.jape.wrapper.JapeFactory;
import br.com.sankhya.jape.wrapper.JapeWrapper;
import br.com.sankhya.modelcore.MGEModelException;
import br.com.sankhya.modelcore.assinaturadigital.DigitalSignatureManager;
import br.com.sankhya.modelcore.util.DynamicEntityNames;

public class RegistraCobranca {
    final String urlCobranca = "https://secure.api.itau/pix_recebimentos/v2/cob/";
                                
    public RegistraCobranca() {
		
	}

       
    public void setCobrancaTLS(Number nuNota) {
    	HttpURLConnection connection = null;
    	String pixJson = "";
    	try {
       		String randonKey = RandomStringUtils.randomAlphabetic(26);

    		URL url = new URL(urlCobranca+randonKey);
 
       		 
            Token token = new Token();
            String tokens = token.getToken();
  
    		connection = (HttpsURLConnection) url.openConnection();
    		connection.addRequestProperty("Content-Type", "application/json");
    		connection.setRequestMethod("PUT");
    		connection.setDoOutput(true);
    		connection.setRequestProperty("Authorization","Bearer "+tokens);
    	    

    		// Add certificate
    		File p12 = new File(DigitalSignatureManager.getBaseFolder() + "dvlpix.p12");
    		String p12password = "dvlpix";

    		InputStream keyInput = new FileInputStream(p12);

    		KeyStore keyStore = KeyStore.getInstance("PKCS12");
    		keyStore.load(keyInput, p12password.toCharArray());
    		keyInput.close();

    		KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
    		keyManagerFactory.init(keyStore, p12password.toCharArray());

    		SSLContext context = SSLContext.getInstance("TLS");
    		context.init(keyManagerFactory.getKeyManagers(), null, new SecureRandom());

    		SSLSocketFactory socketFactory = context.getSocketFactory();
    		if (connection instanceof HttpsURLConnection)
    			((HttpsURLConnection) connection).setSSLSocketFactory(socketFactory);
    		//

     		JsonObject jsonCobranca = montaJsonCobranca(nuNota);
    	 		
            Gson gson = new Gson();    		
    		String body = gson.toJson(jsonCobranca);
    	
    		OutputStream outputStream = connection.getOutputStream();
    		outputStream.write(body.toString().getBytes("utf-8"));
    		outputStream.close();
    	    
    		BufferedReader bufferedReader =
    	    new BufferedReader(new InputStreamReader(connection.getInputStream()));

    		StringBuilder response = new StringBuilder();
    		String line = null;
    		while ((line = bufferedReader.readLine()) != null) {
    			response.append(line);
    		}

    		bufferedReader.close();

    		pixJson = response.toString();

    	} catch (Exception e) {
    		e.printStackTrace();
    	} finally {
    		if (connection != null)
    			connection.disconnect();
    	}
		PIXDAO pixDAO = new PIXDAO();
		try {
			pixDAO.criaCobrancaDAO(pixJson, nuNota);
		} catch (MGEModelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	
	private JsonObject montaJsonCobranca( Number nuNota ) throws Exception {
		
		    //Inicia a montagem dos objetos 
		    
   		    DynamicVO parceiroVO;
   		    DynamicVO notaVO;
   		    
			JapeWrapper notaDAO = JapeFactory.dao(DynamicEntityNames.CABECALHO_NOTA);

			notaVO = notaDAO.findByPK(nuNota);
		
			
			JapeWrapper parceiroDAO = JapeFactory.dao(DynamicEntityNames.PARCEIRO);
			
			parceiroVO = parceiroDAO.findByPK(notaVO.asBigDecimal("CODPARC"));

            JsonObject rootObject = new JsonObject();

	    	JsonObject calendarioObject = new JsonObject();
	    	calendarioObject.addProperty("expiracao", 864000);
	    	rootObject.add("calendario",calendarioObject);
		
		    JsonObject devedorObject = new JsonObject();

		    String cnpj = "";
		    String cpf = "";
		   
		    if ( parceiroVO.asString("TIPPESSOA").equals("J") ) {
		    	cnpj = parceiroVO.asString("CGC_CPF");
		    	devedorObject.addProperty("cnpj", cnpj );
		    } else  {
		    	cpf  = parceiroVO.asString("CGC_CPF");
		        devedorObject.addProperty("cpf", cpf );
		    }
		    		    
	        devedorObject.addProperty("nome", parceiroVO.asString("RAZAOSOCIAL") );
		    rootObject.add("devedor", devedorObject);
         		    		    
		    JsonObject valorObject = new JsonObject();
		    valorObject.addProperty("original", "1.0");
		    rootObject.add("valor", valorObject);
		    //CNPJ ou chave PIX
		    rootObject.addProperty("chave", "25681529000149");
		    rootObject.addProperty("solicitacaoPagador", "NFe: "+ nuNota.toString() + " - Pagamento referente a nota fiscal.");
		    
		    JsonArray infArray = new JsonArray();		    
		    JsonObject inf1 = new JsonObject();   
		    inf1.addProperty("nome", "Razão Social");
		    inf1.addProperty("valor", parceiroVO.asString("RAZAOSOCIAL") );
		    
		    JsonObject inf2 = new JsonObject();   
		    inf2.addProperty("nome", "Emissor");
		    inf2.addProperty("valor","DVL - Distribuidora Via Láctea" );

		    JsonObject inf3 = new JsonObject();   
		    inf3.addProperty("nome", "Observações");
		    inf3.addProperty("valor","Em caso de dúvida entre em contato:  (31) 3649-1000");

		    infArray.add(inf1);
		    infArray.add(inf2);
		    infArray.add(inf3);
		    
            rootObject.add("infoAdicionais", infArray);	
            
		    return rootObject;
	}

}
