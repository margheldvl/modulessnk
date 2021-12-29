package br.com.action.pix.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;

import br.com.sankhya.modelcore.assinaturadigital.DigitalSignatureManager;

public class BuscaDadosPIX {
	
    final String urlCobranca = "https://secure.api.itau/pix_recebimentos/v2/cob/";
    final String urlStatusCobranca = "https://secure.api.itau/pix_recebimentos/v2/pix?inicio=";

	public BuscaDadosPIX() {
		// TODO Auto-generated constructor stub
	}
	
	public String getQRC(String txid) {
		return consomeAPI(txid);
	}
	
	public String getStatusCobranca( String txid ) {
		return statusCobranca(txid);
	}
	
	private String statusCobranca(String txid ) {
		
	    LocalDateTime dataT = LocalDateTime.now();
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
		String inicio = dataT.minusDays(10).format(formatter);
		String fim = dataT.format(formatter);
		
		HttpURLConnection connection = null;
    	String pixJson = "";
    	try {
    		URL url = new URL(urlStatusCobranca+inicio+"&fim="+fim+"&txid="+txid);
 
       		 
            Token token = new Token();
            String tokens = token.getToken();
  
    		connection = (HttpsURLConnection) url.openConnection();
    		connection.addRequestProperty("Content-Type", "application/json");
    		connection.setRequestMethod("GET");
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

		return pixJson;
	}
	
	private String consomeAPI( String txid ) {
		
		HttpURLConnection connection = null;
    	String pixJson = "";
    	try {
    		URL url = new URL(urlCobranca+txid+"/qrcode");
 
       		 
            Token token = new Token();
            String tokens = token.getToken();
  
    		connection = (HttpsURLConnection) url.openConnection();
    		connection.addRequestProperty("Content-Type", "application/json");
    		connection.setRequestMethod("GET");
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
		return pixJson;
	}

}
