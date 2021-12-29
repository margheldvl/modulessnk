package br.com.dvl.api.services;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.json.Json;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gwt.dev.util.collect.HashMap;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;

import br.com.dvl.api.services.Model.Cobranca;
import br.com.dvl.api.services.Model.Devedor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Services {
	public static final MediaType JSON  = MediaType.parse("application/json");
	
	public void services() {
	}
	
	public String setRegistroCobranca(Devedor value,String token) throws IOException {
		return  registraCobranca(value,token);
	}
	
	private String registraCobranca(Devedor devedor, String token) throws IOException {


		    
	        JsonObject devedorObject = new JsonObject();
	        devedorObject.addProperty("cpf", devedor.cpf);
	        devedorObject.addProperty("nome", devedor.nome);
	        
	        JsonObject rootObject = new JsonObject();
		    rootObject.add("devedor", devedorObject);

              		    
		    JsonObject locObject = new JsonObject();
		    locObject.addProperty("id", "0");
		    rootObject.add("loc", locObject);
		    
		    JsonObject valorObject = new JsonObject();
		    valorObject.addProperty("original", "1.0");
		    valorObject.addProperty("modalidadeAlteracao", 0);
		    rootObject.add("valor", valorObject);
		    rootObject.addProperty("chave", "12312321312");


		    JsonObject calendarioObject = new JsonObject();
		    calendarioObject.addProperty("expiracao", 3600);
		    rootObject.add("calendario",calendarioObject);
		    
		    Gson gson = new Gson();
		    String json = gson.toJson(rootObject);
		    
            token = token.replace("\n", "");
            Gson gsonToken = new Gson(); 
            Map<String,Object> map = new HashMap<String,Object>();
            map = (Map<String,Object>) gsonToken.fromJson(token, map.getClass());
            String tokenAuthorization = map.get("access_token").toString();
		
				
		OkHttpClient client = new OkHttpClient().newBuilder().build();		

		RequestBody body = RequestBody.create(json,JSON);
		Request request = new Request.Builder().url("https://api.itau.com.br/sandbox/sandbox/pix_recebimentos/v2/cob/6lkKwHFSXjs7xgdHysT79rvpLE")
				  .method("PUT", body)
				  .addHeader("Content-Type", "application/json")
				  .addHeader("x-correlationID", "96cfb7b5-9af2-445f-98f9-c1b1ba269f15")
				  .addHeader("Authorization","Bearer "+tokenAuthorization).build();
			Response response = client.newCall(request).execute();
			System.out.println(response.message());
	        return response.message();
		
		}
	
	private void buscaQrCode(Integer txid) {
		
    }

}
