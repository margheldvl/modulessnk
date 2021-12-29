package br.com.pix.certificado;


import org.slf4j.Logger;
 

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;


public class CriaCRT {

	public CriaCRT() {
		// TODO Auto-generated constructor stub
	}
	
	public String setCRT() {
      
		HttpURLConnection httpConnection = null;
		SSLContext ctx = null;

		try {
			ctx = SSLContext.getInstance("TLS");
		} catch (NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		}
		try {
			ctx.init(new KeyManager[0], new TrustManager[] {getDefaultTrustManager()}, new SecureRandom());
		} catch (KeyManagementException e1) {
			e1.printStackTrace();
		}
	  
		SSLContext.setDefault(ctx);
		
		try {
			String tokencrt = "eyJraWQiOiIxNDZlNTY1Yy02ZjQ4LTRhN2EtOTU3NS1kYjg2MjE5YTc5N2MucHJkLmdlbi4xNTk3NjAwMTI1ODQ4Lmp3dCIsImFsZyI6IlJTMjU2In0.eyJzdWIiOiI1YzgwYjcxOS00Y2IwLTRjZTgtYmFmMS1lMDE1ZmRhODA2MmEiLCJhdXQiOiJNQVIiLCJ2ZXIiOiJ2MS4wIiwiaXNzIjoiaHR0cHM6XC9cL29wZW5pZC5pdGF1LmNvbS5iclwvYXBpXC9vYXV0aFwvdG9rZW4iLCJBY2Nlc3NfVG9rZW4iOiIyYWEzZmU5NS43ZTA0ZTk0Ni00M2UwLTQ3MzUtOTdlNC1mMDA4MzM3NGE3MTgiLCJzb3VyY2UiOiJFWFQiLCJlbnYiOiJQIiwic2l0ZSI6ImN0bW0xIiwidXNyIjoibnVsbCIsIm1iaSI6InRydWUiLCJ1c2VyX2lkIjoiNWM4MGI3MTktNGNiMC00Y2U4LWJhZjEtZTAxNWZkYTgwNjJhIiwic2NvcGUiOiJwYXltZW50c3Byb3ZpZGVyXC9yZWNlaXB0c1wvcXJjb2RlIHBheW1lbnRzXC9waXgucmVhZCB3ZWJob29rLndyaXRlIGNhc2gtbWFuYWdlbWVudFwvcmVjZWlwdHNcL3FyY29kZS5wb3N0IGNhc2gtbWFuYWdlbWVudFwvcmVjZWlwdHNcL3FyY29kZS5yZWFkIHRlc3RlX2NhcmdhX29ucHJlbWlzc2VzX3JhbWxcL2RpY3QgcGF5bWVudHNwcm92aWRlclwvZGljdC5yZWFkIHBheW1lbnRzXC9kaWN0LnJlYWQgY2FzaC1tYW5hZ2VtZW50XC9yZWNlaXB0c1wvcmVmdW5kLnJlYWQgcGF5bWVudHNcL2RpY3QgdGVzdGVfY2FyZ2Ffb25wcmVtaXNzZXNfcmFtbFwvZGljdC5yZWFkIGNhc2gtbWFuYWdlbWVudFwvcmVjZWlwdHNcL3FyY29kZSBjb2J2LnJlYWQgcGF5bWVudHNcL3BpeC5wb3N0IGNvYnYud3JpdGUgcGF5bG9hZGxvY2F0aW9uLnJlYWQgY29iLndyaXRlIGNhc2gtbWFuYWdlbWVudFwvc2lzcGFnXC9wYXltZW50IHBheW1lbnRzXC93ZWJob29rLndyaXRlIHRlc3RlX2NhcmdhX29ucHJlbWlzc2VzX3JhbWxcL2RpY3Qud3JpdGUgY2FzaC1tYW5hZ2VtZW50XC9yZWNlaXB0c1wvcGl4LnJlYWQgcGF5bWVudHNwcm92aWRlclwvcGl4LnJlYWQgbG90ZWNvYnYud3JpdGUgcGF5bWVudHNwcm92aWRlclwvcGl4IHBheW1lbnRzXC9waXggcGl4LndyaXRlIHBheW1lbnRzcHJvdmlkZXJcL2RpY3Qud3JpdGUgcGF5bG9hZGxvY2F0aW9uLndyaXRlIHBheW1lbnRzXC9waXgucmVmdW5kIGxvdGVjb2J2LnJlYWQgd2ViaG9vay5yZWFkIHBheW1lbnRzXC9kaWN0LndyaXRlIGNvYi5yZWFkIGNhc2gtbWFuYWdlbWVudFwvc2lzcGFnXC9wYXltZW50LnJlYWQgcGl4LnJlYWQgcGF5bWVudHNwcm92aWRlclwvZGljdCBjYXNoLW1hbmFnZW1lbnRcL3JlY2VpcHRzXC9yZWZ1bmQgY2FzaC1tYW5hZ2VtZW50XC9waXgucmVmdW5kIHBheW1lbnRzXC93ZWJob29rLnJlYWQiLCJleHAiOjE2Mzg4MDI4MzgsImlhdCI6MTYzODE5ODAzOCwiZmxvdyI6IkNDIn0.GZHy1kx6xxRU0XGwl1TM2zcg9pPdzDAr5mk2_T9925dkeRLhJLYUi8i9EXwagByMOeTWbitqqad2a2js4eqSWhDIIElEhfOfgqVi8ulAcxuadxVyYNseH6PkiUyfKZc9aHt4hAkli9VTtRSJ8jvBKy9rBIn9iw8soLBPUT5nwXrvrm6rB8JI_Kd90mley5A1bDyh3Add0tncqElV77N-nRhRB_u5qJK06Nz8njVZs5c6Xb7ASdiXYf5I7mzfiaMtUBgsm-HnNkBcI0acITHopKQS-Q96Pqu2rhkUe65oR0_cTQYZQ9AtdUwEUwYecKJ7fsKIe9U5PDxtm4N58ScgEQ";
			URL targetUrl = new URL("https://sts.itau.com.br/seguranca/v2/certificado/solicitacao");

			httpConnection = (HttpURLConnection) targetUrl.openConnection();
			httpConnection.setDoOutput(true);
			httpConnection.setRequestMethod("POST");
			httpConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			httpConnection.setRequestProperty("Content-Type", "text/plain");
			httpConnection.setRequestProperty("Authorization","Bearer "+ tokencrt);

			DataOutputStream wr = new DataOutputStream(httpConnection.getOutputStream());
			wr.writeBytes("/home/marghel/DVL/Sankhya/certificado/dvlpixpub.csr");
			wr.flush();
			wr.close();

			if (httpConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
				throw new Exception("Erro na geracao do certificado");
			}
			
			StringBuilder crt = new StringBuilder();
						
			try (Reader reader = new BufferedReader(new InputStreamReader
					(httpConnection.getInputStream(), Charset.forName(StandardCharsets.UTF_8.name())))) {
					int c = 0;
					while ((c = reader.read()) != -1) {
						if(c != 1)
							crt.append((char) c);
					}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			String crtBody = crt.delete(0, crt.indexOf("\n") + 1).toString();
			System.out.println("****CERTIFICADO****");
			System.out.println(crtBody);
			
			httpConnection.disconnect();
			
		} catch (Exception e) {
			e.printStackTrace();
			//TODO
		} finally {
			if (httpConnection != null) {
				httpConnection.disconnect();
			}
		}
		return null;
	}
	
	  private static X509TrustManager getDefaultTrustManager() {
		    return new X509TrustManager() {
		      @Override
		      public void checkClientTrusted( X509Certificate[] certs, String param ) throws CertificateException {
		        throw new CertificateException( "Client Authentication not implemented" );
		      }

		      @Override
		      public void checkServerTrusted( X509Certificate[] certs, String param ) throws CertificateException {
		        for ( X509Certificate cert : certs ) {
		          cert.checkValidity(); // validate date
		          // cert.verify( key ); // check by Public key
		          // cert.getBasicConstraints()!=-1 // check by CA
		        }
		      }

		      @Override
		      public X509Certificate[] getAcceptedIssuers() {
		        return new X509Certificate[0];
		      }
		    };
		  }

}
