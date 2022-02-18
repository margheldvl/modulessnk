package br.com.sankhya.pamcard.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Conversoes {

	public String converteDataDDMMYYYY(Date data) {

//		SimpleDateFormat formatter = new SimpleDateFormat("DD/MM/YYYY");
//		String dataFormatada = formatter.format(data);
		
		SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");    
		Date dataFormatada = data;	
		
		String dataStr = fmt.format(dataFormatada);

		return dataStr;

	}
	
	public String converteDataDDMMYY(Date data) {

		SimpleDateFormat formatter = new SimpleDateFormat("DD/MM/YY");
		String dataFormatada = formatter.format(data);

		return dataFormatada;

	}

	public Date converteDataDDMMYYYY(String dataStr) {
		
		SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");    
		Date dataFormatada = null;
		try {
			dataFormatada = fmt.parse(dataStr);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
//		String str = fmt.format(dataFormatada);

//		String formato = "DD/MM/YYYY";
//		Date dataFormatada = null;
//		try {
//			dataFormatada = new SimpleDateFormat(formato).parse(dataStr);
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

		return dataFormatada;
	}
	
	public Date converteDataDDMMYY(String dataStr) {

		String formato = "DD/MM/YY";
		Date dataFormatada = null;
		try {
			dataFormatada = new SimpleDateFormat(formato).parse(dataStr);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return dataFormatada;
	}
	
	public String converteDataMMYYYY(Date data) {

		SimpleDateFormat formatter = new SimpleDateFormat("MM/YYYY");
		String dataFormatada = formatter.format(data);

		return dataFormatada;

	}
	
	public Double strDouble(String str) {
	    double num = 0;
	    double num2 = 0;
	    int idForDot = str.indexOf('.');
	    boolean isNeg = false;
	    String st;
	    int start = 0;
	    //int end = str.length();

	    if (idForDot != -1) {
	        st = str.substring(0, idForDot);
	        for (int i = str.length() - 1; i >= idForDot + 1; i--) {
	            num2 = (num2 + str.charAt(i) - '0') / 10;
	        }
	    } else {
	        st = str;
	    }

	    if (st.charAt(0) == '-') {
	        isNeg = true;
	        start++;
	    } else if (st.charAt(0) == '+') {
	        start++;
	    }

	    for (int i = start; i < st.length(); i++) {
	        if (st.charAt(i) == ',') {
	            continue;
	        }
	        num *= 10;
	        num += st.charAt(i) - '0';
	    }

	    num = num + num2;
	    if (isNeg) {
	        num = -1 * num;
	    }
	    
	    return  num;
	}
	
	public String formatDouble2CasasPonto(double x) {
	    return String.format("%.2f",  x).replace(',', '.');
	}

	
	public String getDataHoraAtual() {
		
		Date agora = new Date();
		String pattern = "dd/MM/yyyy HH:mm:ss";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		String dataFormatada = simpleDateFormat.format(agora);
		
		return dataFormatada + "h";
	} 
	
}
