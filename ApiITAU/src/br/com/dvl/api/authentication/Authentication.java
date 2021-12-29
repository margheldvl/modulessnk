package br.com.dvl.api.authentication;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Authentication {
    
	public static void authentication() {
	}
	
	public String token() throws Exception {
		return getToken();
	}
	private String getToken() throws Exception {
		OkHttpClient client = new OkHttpClient().newBuilder().build();		
		MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
		RequestBody body = RequestBody.create(mediaType, "grant_type=client_credentials&client_id=70082ca0-64c2-4097-8c2f-3005a62ecd13&client_secret=0b22f27e-8bd1-47c5-a48f-0aaea521a018");
		Request request = new Request.Builder().url("https://api.itau.com.br/sandbox/api/oauth/token")
				  .method("POST", body)
				  .addHeader("Content-Type", "application/x-www-form-urlencoded")
				  .build();
			Response response = client.newCall(request).execute();
	        return response.body().string();
	}
}
