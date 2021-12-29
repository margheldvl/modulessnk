package br.com.action.pix.service;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;

import br.com.sankhya.modelcore.assinaturadigital.DigitalSignatureManager;
import com.google.gson.Gson;

public class Token {
    private final String clientId = "5c80b719-4cb0-4ce8-baf1-e015fda8062a" ;
    private final String clientSecret = "05dae797-8026-489d-a8ef-4f64d87ead9f";
    public Token() {
	}
	
	public String getToken() throws Exception {
	   return buscaTokenTLS();	
	}
	
    @SuppressWarnings("unchecked")
	public String buscaTokenTLS() {
    	HttpURLConnection connection = null;
    	String tokenStr = "";
    	try {
    		URL url = new URL("https://sts.itau.com.br/api/oauth/token");

    		connection = (HttpsURLConnection) url.openConnection();
    		connection.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
    		connection.setRequestMethod("POST");
    		connection.setDoOutput(true);

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

    		String body = "grant_type=client_credentials"
    				+ "&client_id=" + clientId 
    				+ "&client_secret=" + clientSecret;
    	
    		OutputStream outputStream = connection.getOutputStream();
    		outputStream.write(body.toString().getBytes());
    		outputStream.close();
    	    
    		BufferedReader bufferedReader =
    	    new BufferedReader(new InputStreamReader(connection.getInputStream()));

    		StringBuilder response = new StringBuilder();
    		String line = null;
    		while ((line = bufferedReader.readLine()) != null) {
    			response.append(line);
    		}

    		bufferedReader.close();

      	        Gson gsonToken = new Gson(); 
    	        Map<String,Object> map = new HashMap<String,Object>();
    	        map = (Map<String,Object>) gsonToken.fromJson(response.toString(), map.getClass());
    	        tokenStr = map.get("access_token").toString();
  
    	} catch (Exception e) {
    		e.printStackTrace();
    	} finally {
    		if (connection != null)
    			connection.disconnect();
    	}
		return tokenStr;
    }
		
}
 
