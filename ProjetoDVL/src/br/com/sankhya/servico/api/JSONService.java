package br.com.sankhya.servico.api;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.Gson;

public class JSONService {

	private static final String URL_API = "http://snk.dvl.com.br/";
	private static final String URL_GET_API = "http://snk.dvl.com.br/";
	private static String mge = "mge/";
	private static String mgecom = "mgecom/";

	@SuppressWarnings("unused")
	private static void sendGetRequest(String idSessao, String serviceName, String jsonStr) throws Exception {

		String sessionId = "JSESSIONID=" + idSessao;
		String service = "service.sbr?serviceName=" + serviceName + "&outputType=json";

		try {

			URL url = new URL(URL_GET_API + service);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();

			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Cookie", sessionId);
			connection.setRequestProperty("Accept", "application/json");
			connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
			OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");

			String postJsonData = jsonStr;

			writer.write(postJsonData);
			writer.close();
			BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			StringBuffer jsonString = new StringBuffer();
			String line;
			while ((line = br.readLine()) != null) {
				jsonString.append(line);
			}
			br.close();
			connection.disconnect();
			
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}

	}

	public static ResponseService sendPostRequest(String idSessao, String serviceName, String jsonStr, String lnkmge)
			throws Exception {
		String resp = "";
		String postJsonData = "";
		

		String outputType = "outputType=json";

		String sessionId = "";

		if (!idSessao.equals("")) {
			sessionId = "mgeSession=" + idSessao + "&";
		}

		String service = "service.sbr?serviceName=" + serviceName + "&";
		StringBuffer jsonString = new StringBuffer();
		ResponseService respService = new ResponseService();
		try {

			URL url = new URL(URL_API + lnkmge + service + sessionId + outputType);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();

			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Accept", "application/json");
			//connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
			//OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
			connection.setRequestProperty("Content-Type", "application/json");
			OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());

			
			postJsonData = jsonStr;

			writer.write(postJsonData);
			writer.close();
			BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));

			String line;
			while ((line = br.readLine()) != null) {
				jsonString.append(line);
			}
			br.close();
			connection.disconnect();

			resp = jsonString.toString();

			

			if (!serviceName.equals("MobileLoginSP.logout")) {
				Integer posStatus = resp.indexOf("status") + 9;
				String status = resp.substring(posStatus, posStatus + 1);

				resp = resp.replace("\":{\"$\"", "").replace("},\"", ",").replace("\":\"", ":\"")
						.replace("\",\"", "\",").replace("{\"", "{").replace("responseBody\":{", "");

				if (status.equals("0")) {
					resp = resp.replace("tsError\":{", "");
				}
				while (resp.substring(resp.length() - 1, resp.length()).equals("}")) {
					resp = resp.substring(0, resp.length() - 1);
				}
				resp = resp + "}";

				

				if (serviceName.equals("CACSP.incluirNota")) {
					resp = resp.replace("pk\":{", "");
					

				}

				Gson gson = new Gson();

				// Converte String JSON para objeto Java
				if (status.equals("1")) {
					if (serviceName.equals("CACSP.incluirNota")) {
						
						ResponseIncluirNota obj = gson.fromJson(resp, ResponseIncluirNota.class);
						respService.setResponseIncluirNota(obj);
						
					} else {
						ResponseMobileLoginSucesso obj = gson.fromJson(resp, ResponseMobileLoginSucesso.class);
						respService.setResponseBodySucess(obj);
					}
				} else {
					ResponseServiceErro obj = gson.fromJson(resp, ResponseServiceErro.class);
					respService.setResponseBodyErro(obj);

				}

			}
			

		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
		return respService;

	}

	public static String leitor(String path) throws IOException {
		BufferedReader buffRead = new BufferedReader(new FileReader(path));
		String linha = "";
		String texto = "";
		while (true) {
			if (linha != null) {
				texto = texto + linha + "\n";
			} else
				break;
			linha = buffRead.readLine();
		}
		buffRead.close();
		return texto;
	}

	public String LoginSnkDVL(String usuario, String senha) throws Exception {

		String idSessionActive = "";
		String idSessionAtu = "";
		String serviceName = "MobileLoginSP.login";

		String postJsonData = "{\r\n" + "   \"serviceName\": \"MobileLoginSP.login\",\r\n"
				+ "      \"requestBody\": {\r\n" + "           \"NOMUSU\": {\r\n" + "               \"$\": \"" + usuario
				+ "\"\r\n" + "           },\r\n" + "           \"INTERNO\":{\r\n" + "              \"$\":\"" + senha
				+ "\"\r\n" + "           },\r\n" + "          \"KEEPCONNECTED\": {\r\n"
				+ "              \"$\": \"S\"\r\n" + "          }\r\n" + "      }\r\n" + "  }";

		ResponseService resp = new ResponseService();
		resp = sendPostRequest(idSessionAtu, serviceName, postJsonData, mge);

		if (resp.getResponseBodyErro() != null) {
			System.out.println("Erro ao tentar efetuar login: " + resp.getResponseBodyErro().getStatusMessage());

		} else {
			System.out.println("sessao: " + resp.getResponseBodySucess().getJsessionid());

			if (resp.getResponseBodySucess().getJsessionid() != null) {
				idSessionActive = resp.getResponseBodySucess().getJsessionid();
			}
		}

		return idSessionActive;

	}

	@SuppressWarnings("unused")
	public void LogoutSnkDVL() throws Exception {

		String idSessionAtu = "";
		String serviceName = "MobileLoginSP.logout";

		String postJsonData = "  {\r\n" + "     \"serviceName\":\"MobileLoginSP.logout\",\r\n"
				+ "     \"status\":\"1\",\r\n" + "     \"pendingPrinting\":\"false\"\r\n" + "  }";

		ResponseService resp = new ResponseService();
		resp = sendPostRequest(idSessionAtu, serviceName, postJsonData, mge);

	}

	public String ConfirmaNotaSnkDVL(String idSessionAtu, Long numNota) throws Exception {

		String msg = "";
		String serviceName = "CACSP.confirmarNota";

		String postJsonData = "{\r\n" + "	\"serviceName\": \"CACSP.confirmarNota\",\r\n" + "	\"requestBody\": {\r\n"
				+ "		\"nota\": {\r\n" + "			\"confirmacaoCentralNota\": \"true\",\r\n"
				+ "			\"ehPedidoWeb\": \"false\",\r\n"
				+ "			\"atualizaPrecoItemPedCompra\": \"false\",\r\n"
				+ "			\"ownerServiceCall\": \"CentralNotas\",\r\n" + "			\"NUNOTA\": {\r\n"
				+ "				\"$\": \"" + Long.valueOf(numNota) + "\"\r\n" + "			}\r\n" + "		}\r\n"
				+ "	}\r\n" + "}";

		try {
			ResponseService resp = new ResponseService();
			resp = sendPostRequest(idSessionAtu, serviceName, postJsonData, mgecom);

			if (resp.getResponseBodyErro() != null) {

				msg = ("Nro Ùnico " + numNota + ": " + resp.getResponseBodyErro().getStatusMessage());

			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());			
		}

		return msg;

	}

	
	public ResponseService IncluirNotaSnkDVL(String idSessionAtu, String postJsonData, Long numNota) throws Exception {

		String serviceName = "CACSP.incluirNota";

		ResponseService resp = new ResponseService();
		resp = sendPostRequest(idSessionAtu, serviceName, postJsonData, mgecom);

		return resp;

	}

}
