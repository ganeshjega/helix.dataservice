package com.helixbeat.provider.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class RestClient {

	Logger LOGGER = Logger.getLogger(RestClient.class);

	private static final String USER_AGENT = "Mozilla/5.0";

	protected String baseURL = null;
	
	public RestClient(String baseURL) {
		setBaseURL(baseURL);
	}
	
	public static RestClient getInstance(String url) {
		return new RestClient(url);
	}
	
	public static String executeGet(String url) {
		return executeGet(url, new HashMap<String,String>());
	}
	
	public static String executeGet(String url, HashMap<String,String> headers) {
		StringBuffer response = new StringBuffer();
		try {
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("User-Agent", USER_AGENT);
			for (String key : headers.keySet()) {
				con.setRequestProperty(key, headers.get(key));
			}
			int responseCode = con.getResponseCode();
			System.out.println("GET Response Code :: " + responseCode);
			if (responseCode == HttpURLConnection.HTTP_OK) {
				BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
				String inputLine;

				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();
				// print result
				System.out.println(response.toString());
			} else {
				System.out.println("GET request not worked");
			}
		} catch (IOException ioex) {
			ioex.printStackTrace();
		}
		return response.toString();
	}
	
	public static String executePost(String input, String URLString) {
		return executePost(input, URLString, null);
	}
	
	public static String executePostAndDownload(String input, String URLString, String path) {
		JSONObject outputObject = new JSONObject();
		try {
			Utils.putBytes(executePost(input, URLString).getBytes(), path);
			outputObject.put("success", "true");
		} catch (Exception ex) {
			ex.printStackTrace();
			outputObject.put("success", "false");
			outputObject.put("msg", ex.getMessage());
		}
		return outputObject.toString();
	}
	
	public static String executePost(String input, String URLString, HashMap<String,String> headers) {
		String result = "";
		try {
			URL url = new URL(URLString);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			if (headers != null) {
				for (String key : headers.keySet()) {
					conn.setRequestProperty(key, headers.get(key));
				}
			}
			if (input.startsWith("<?xml version=")) {
				conn.setRequestProperty("Content-Type", "text/xml");
			} else {
				conn.setRequestProperty("Content-Type", "application/json");
			}

			OutputStream os = conn.getOutputStream();
			os.write(input.getBytes());
			os.flush();
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String output;
//			System.out.println("Output from Server .... \n");
			while ((output = br.readLine()) != null) {
				result += output;
//				System.out.println(output);
			}
			conn.disconnect();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	public String sendPostRequest(String input, String URLString) {
		String result = "";
		try {
			URL url = new URL(URLString);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			if (input.startsWith("<?xml version=")) {
				conn.setRequestProperty("Content-Type", "text/xml");
			} else {
				conn.setRequestProperty("Content-Type", "application/json");
			}

			OutputStream os = conn.getOutputStream();
			os.write(input.getBytes());
			os.flush();
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String output;
//			System.out.println("Output from Server .... \n");
			while ((output = br.readLine()) != null) {
				result += output;
//				System.out.println(output);
			}
			conn.disconnect();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public JSONObject executeQuery(String input) {
		String response = sendPostRequest(input, baseURL + "/OrionLOVService/executeQuery");
debug("RestClient.executeQuery() " + response);
		JSONObject outputObject = JSONUtils.parseObjectFromString(response);
		return outputObject;
	}

	public JSONObject executeQuery2(String input) {
		String response = sendPostRequest(input, baseURL + "/OrionLOVService/executeQuery2");
debug("RestClient.executeQuery2() " + response);
		JSONObject outputObject = JSONUtils.parseObjectFromString(response);
		return outputObject;
	}

	public JSONObject executeSingleRecordQuery(String input) {
debug("RestClient.executeSingleRecordQuery() Input [" + input + "]");
		String response = sendPostRequest(input, baseURL + "/OrionLOVService/executeSingleRecordQuery");
debug("RestClient.executeSingleRecordQuery() Response [" + response + "]");
		JSONObject outputObject = JSONUtils.parseObjectFromString(response);
		return outputObject;
	}

	public Integer getSingleRecordValueAsInt(String input) {
debug("RestClient.getSingleRecordValueAsInt() Input [" + input + "]");
		String response = sendPostRequest(input, baseURL + "/OrionLOVService/executeSingleRecordQuery");
debug("RestClient.getSingleRecordValueAsInt() Response [" + response + "]");
		JSONObject outputObject = JSONUtils.parseObjectFromString(response);
		for (Object key : outputObject.keySet()) {
			Object value = outputObject.get(key);
			if (value != null) {
				return Integer.valueOf((String) value);
			}
		}
		return -1;
	}

	public String getSingleRecordValue(String input) {
debug("RestClient.getSingleRecordValue() Input [" + input + "]");
		String response = sendPostRequest(input, baseURL + "/OrionLOVService/executeSingleRecordQuery");
debug("RestClient.getSingleRecordValue() Response [" + response + "]");
		JSONObject outputObject = JSONUtils.parseObjectFromString(response);
		for (Object key : outputObject.keySet()) {
			Object value = outputObject.get(key);
			if (value != null) {
				return ((String) value);
			}
		}
		return null;
	}

	public JSONObject executeDMLStatement(String input) {
		String response = sendPostRequest(input, baseURL + "/OrionTransactionService/executeDMLStatement");
debug("RestClient.executeDMLStatement() " + response);
		JSONObject outputObject = JSONUtils.parseObjectFromString(response);
		return outputObject;
	}

	public JSONObject executeDMLStatementWithNamedParameters(String input) {
		String response = sendPostRequest(input, baseURL + "/OrionTransactionService/executeDMLStatementWithNamedParameters");
debug("RestClient.executeDMLStatementWithNamedParameters() " + response);
		JSONObject outputObject = JSONUtils.parseObjectFromString(response);
		return outputObject;
	}

	public void debug(String msg) {
//		System.out.println(msg);
		LOGGER.info(msg);
	}
	public String getBaseURL() {
		return baseURL;
	}

	public void setBaseURL(String baseURL) {
		this.baseURL = baseURL;
	}
}