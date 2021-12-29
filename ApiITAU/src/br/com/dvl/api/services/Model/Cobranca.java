package br.com.dvl.api.services.Model;

import com.google.gson.Gson;

public class Cobranca {
	
	public Devedor devedor;

	public Cobranca() {
		// TODO Auto-generated constructor stub
	}
	
	public void preencheCobranca( Devedor devedor ) {
		this.devedor = devedor; 
	}
	
	public Gson getJSONCobranca() {
	    Gson gson = new Gson();
        return gson;
	}

}
