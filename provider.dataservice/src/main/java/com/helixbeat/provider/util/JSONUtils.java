package com.helixbeat.provider.util;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class JSONUtils {

	final static Logger log = Logger.getLogger(JSONUtils.class);
	
	public static JSONObject parseObjectFromStream(String path) {
		JSONParser jsonParser = new JSONParser();
		debug("filePath in JsonUtils() ===="+path);
		InputStream in = Utils.getResourceAsStream(path);
		InputStreamReader inr = new InputStreamReader(in);
		debug("input stream reader ===="+inr);
		JSONObject jsonObject = new JSONObject();
		
		try {
			jsonObject = (JSONObject) jsonParser.parse(inr);
			debug("Json Object ===="+jsonObject);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			jsonObject.put("PARSE_ERROR", e.getMessage());
			debugError(e);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			jsonObject.put("PARSE_ERROR", e.getMessage());
			debugError(e);
		}
		return jsonObject;
	}

	public static JSONArray parseArrayFromStream(String path) {
		JSONParser jsonParser = new JSONParser();
		InputStream in = Utils.getResourceAsStream(path);
		InputStreamReader inr = new InputStreamReader(in);
		JSONArray jsonArray = new JSONArray();
		try {
			jsonArray = (JSONArray) jsonParser.parse(inr);
		} catch (IOException e) {
			e.printStackTrace();
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("PARSE_ERROR", e.getMessage());
			jsonArray.add(jsonObject);
		} catch (Exception e) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("PARSE_ERROR", e.getMessage());
			jsonArray.add(jsonObject);
		}
		return jsonArray;
	}

	public static JSONObject parseObjectFromString(String contents) {
		JSONParser jsonParser = new JSONParser();
		InputStream in = new ByteArrayInputStream(contents.getBytes());
		InputStreamReader inr = new InputStreamReader(in);
		JSONObject jsonObject = new JSONObject();
		
		try {
			jsonObject = (JSONObject) jsonParser.parse(inr);
//			debug("Json Object ===="+jsonObject);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			jsonObject.put("PARSE_ERROR", e.getMessage());
			debugError(e);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			jsonObject.put("PARSE_ERROR", e.getMessage());
			debugError(e);
		}
		return jsonObject;
	}

	public static JSONArray parseArrayFromString(String contents) {
		JSONParser jsonParser = new JSONParser();
		InputStream in = new ByteArrayInputStream(contents.getBytes());
		InputStreamReader inr = new InputStreamReader(in);
		JSONArray jsonArray = new JSONArray();
		try {
			jsonArray = (JSONArray) jsonParser.parse(inr);
		} catch (IOException e) {
			e.printStackTrace();
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("PARSE_ERROR", e.getMessage());
			jsonArray.add(jsonObject);
		} catch (Exception e) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("PARSE_ERROR", e.getMessage());
			jsonArray.add(jsonObject);
		}
		return jsonArray;
	}
	
	public static JSONObject parseObjectFromFileSystem(String path) {
		JSONParser jsonParser = new JSONParser();
		debug("filePath in JsonUtils() ===="+path);
		InputStream in;
		JSONObject jsonObject = new JSONObject();
		try {
			in = new FileInputStream(path);
			InputStreamReader inr = new InputStreamReader(in);
			debug("input stream reader ===="+inr);
		
			jsonObject = (JSONObject) jsonParser.parse(inr);
			debug("Json Object ===="+jsonObject);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			jsonObject.put("PARSE_ERROR", e.getMessage());
			debugError(e);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			jsonObject.put("PARSE_ERROR", e.getMessage());
			debugError(e);
		}
		return jsonObject;
	}

	public static JSONArray parseArrayFromFileSystem(String path) {
		JSONParser jsonParser = new JSONParser();
		JSONArray jsonArray = new JSONArray();
		try {
			InputStream in = new FileInputStream(path);
			InputStreamReader inr = new InputStreamReader(in);
			debug("input stream reader ===="+inr);
			jsonArray = (JSONArray) jsonParser.parse(inr);
		} catch (IOException e) {
			e.printStackTrace();
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("PARSE_ERROR", e.getMessage());
			jsonArray.add(jsonObject);
		} catch (Exception e) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("PARSE_ERROR", e.getMessage());
			jsonArray.add(jsonObject);
		}
		return jsonArray;
	}
	
	public static JSONObject buildFromStringArray(String[] attributes) {
		JSONObject outputObject = new JSONObject();
		for (int i=0;i<attributes.length;i+=2) {
			outputObject.put(attributes[i], attributes[i+1]);
		}
		return outputObject;
	}

	public static Properties convert2Props(JSONObject jsonObject) {
		Properties properties = new Properties();
		for (Object key : jsonObject.keySet()) {
			if (jsonObject.get(key) instanceof JSONArray || jsonObject.get(key) instanceof JSONObject) {
				continue;
			}
			properties.setProperty(String.valueOf(key), String.valueOf(jsonObject.get(key)));
		}
		return properties;
	}

	public static JSONObject transform(JSONObject input, Properties fieldMap) {
		JSONObject output = new JSONObject();
		Enumeration<String> names = (Enumeration<String>) fieldMap.propertyNames();
		String name = null;
		while (names.hasMoreElements()) {
			name = names.nextElement();
			output.put(fieldMap.getProperty(name), (String) input.get(name));
		}
		return output;
	}

	public static JSONObject merge(JSONObject from, JSONObject to) {
		return merge(from, to, true);
	}
	
	public static JSONObject merge(JSONObject from, JSONObject to, boolean overwrite) {
		for (Object key : from.keySet()) {
			if (to.containsKey(key)) {
				if (overwrite) {
					to.put(key, from.get(key));
				}
			} else {
				to.put(key, from.get(key));
			}
		}
		return to;
	}

	public static JSONObject copyAttributes(JSONObject from, JSONObject to, String[] attributeNames) {
		for (String attributeName : attributeNames) {
			if (!to.containsKey(attributeName)) {
				to.put(attributeName, from.get(attributeName));
			}
		}
		return to;
	}

	public static void debug(String message) {
//		log.info(message);
	}

	public static void debugError(Throwable throwable) {
//		log.error(throwable);
	}

}