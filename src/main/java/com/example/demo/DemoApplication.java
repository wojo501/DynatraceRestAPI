package com.example.demo;


import com.example.exceptions.DateFormatException;
import com.example.exceptions.LackDataException;
import com.example.exceptions.ParametersException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;

@SpringBootApplication
@RestController
public class DemoApplication {
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
	private int MAX_NUMBER = 255;
	private int MIN_NUMBER = 0;
	private String[] TABLES = {"a", "b", "c"};

	private boolean isValidDate(String date) {
		String regex = "\\d{4}-\\d{2}-\\d{2}";
		return date.matches(regex);
	}

	@GetMapping("/task1")
	public ResponseEntity<Object> task1(@RequestParam String date, @RequestParam String code) throws IOException, LackDataException, DateFormatException {
		String s = "{\"response\": \"not responding\"}";
		if(!isValidDate(date)){
			throw new DateFormatException("YYYY-MM-DD");
		}
		//looping through all avaliable tables in Bank API with wanted info
		for (int i = 0; i < TABLES.length-1; i++) {
			URL url = new URL("http://api.nbp.pl/api/exchangerates/rates/"+TABLES[i]+"/"+code+"/"+date+"/");
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			int responseCode = connection.getResponseCode();

			if (responseCode == 200) { // positive response
				InputStream input = connection.getInputStream();
				s = new BufferedReader(new InputStreamReader(input)).lines().collect(Collectors.joining("\n"));
				break; // exit loop if successful response
			}
		}

		//parsing response object into JSON and getting result data
		Double mid = null;
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = objectMapper.readTree(s);
		JsonNode ratesArr = jsonNode.get("rates");
		if(ratesArr==null){
			throw new LackDataException("No such data");
		}
		mid = ratesArr.get(0).get("mid").asDouble();

		return new ResponseEntity(code +": "+ mid, HttpStatus.OK);
	}

	@GetMapping("/task2")
	public ResponseEntity<Object> task2(@RequestParam String code, @RequestParam int number) throws ParametersException, LackDataException {
		if (number > MAX_NUMBER || number < MIN_NUMBER){
			throw new ParametersException("number", "error", MIN_NUMBER, MAX_NUMBER);
		}
		// default response outside try-catch block
		String s = "{\"response\": \"not responding\"}";
		try {
			for (int i = 0; i < TABLES.length-1; i++) {
				URL url = new URL("http://api.nbp.pl/api/exchangerates/rates/"+TABLES[i]+"/"+code+"/last/"+number+"/");
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();
				connection.setRequestMethod("GET");
				int responseCode = connection.getResponseCode();
				if (responseCode == 200) { // positive response
					InputStream input = connection.getInputStream();
					s = new BufferedReader(new InputStreamReader(input)).lines().collect(Collectors.joining("\n"));
					break; // exit loop if successful response
				}
			}

			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode jsonNode = objectMapper.readTree(s);
			JsonNode ratesArr = jsonNode.get("rates");
			if(ratesArr==null){
				throw new LackDataException("No such data");
			}
			//taking biggest diffrence between variables
			Double max = Double.valueOf(0);
			Double min = Double.POSITIVE_INFINITY;
			for (JsonNode rate: ratesArr) {
				if(rate.get("mid").asDouble()>max){
					max = rate.get("mid").asDouble();
				}
				if (rate.get("mid").asDouble()<min){
					min = rate.get("mid").asDouble();
				}
			}
			String result = "Min: "+min+" Max: "+max;
			return new ResponseEntity(result, HttpStatus.OK);
		} catch (IOException e) {
			Map<String, String> errorMap = new HashMap<>();
			errorMap.put("error", "Error occurred while processing the request");
			return new ResponseEntity<>(errorMap, HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/task3")
	public ResponseEntity<Object> task3(@RequestParam String code, @RequestParam int number) throws ParametersException, LackDataException {
		if (number > MAX_NUMBER || number < MIN_NUMBER){
			throw new ParametersException("number", "wrong number", MIN_NUMBER, MAX_NUMBER);
		}

		try {
			URL url = new URL("http://api.nbp.pl/api/exchangerates/rates/c/" + code + "/last/" + number + "/");
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			int responseCode = connection.getResponseCode();
			String responseMessage = connection.getResponseMessage();

			if (responseCode == HttpURLConnection.HTTP_NOT_FOUND || responseCode == HttpURLConnection.HTTP_BAD_REQUEST) {
				Map<String, String> errorMap = new HashMap<>();
				errorMap.put("error", responseMessage);
				return new ResponseEntity<>(errorMap, HttpStatus.BAD_REQUEST);
			}

			InputStream input = connection.getInputStream();
			String s = new BufferedReader(new InputStreamReader(input)).lines().collect(Collectors.joining("\n"));

			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode jsonNode = objectMapper.readTree(s);
			JsonNode ratesArr = jsonNode.get("rates");
			if(ratesArr==null){
				throw new LackDataException("No such data");
			}
			Double maxDiff = Double.valueOf(0);
			for (JsonNode rate : ratesArr) {
				Double currDiff = Math.abs(rate.get("bid").asDouble() - rate.get("ask").asDouble());
				if (currDiff > maxDiff) {
					maxDiff = currDiff;
				}
			}
			return new ResponseEntity<>("Max ratio: " + maxDiff, HttpStatus.OK);
		} catch (IOException e) {
			Map<String, String> errorMap = new HashMap<>();
			errorMap.put("error", "Error occurred while processing the request");
			return new ResponseEntity<>(errorMap, HttpStatus.BAD_REQUEST);
		}
	}

}
