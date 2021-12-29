package br.com.dvl.api;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.google.gdata.data.dublincore.Date;
import com.ibm.icu.text.SimpleDateFormat;


public class ApiMain {

	public static void main(String[] args) throws Exception {
            LocalDateTime dataT = LocalDateTime.now();
            final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

            System.out.println(dataT.format(formatter));
            System.out.println(dataT.minusDays(10).format(formatter));

	}

}
