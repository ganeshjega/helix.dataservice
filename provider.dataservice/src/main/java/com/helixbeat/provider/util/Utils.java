package com.helixbeat.provider.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

public class Utils {

	private static final Logger LOGGER = Logger
			.getLogger(Utils.class.getName());

	static String DEFAULT_DATE_FORMAT = "yyyy-MM-dd hh:mm:ss";
	static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	
	public static int DEBUG = getInteger(System.getProperty("orion.api.debug", "0"));

	public static int getInteger(String numberStr) {
		return getInteger(numberStr, -1);
	}

	public static int getInteger(String numberStr, int defaultValue) {
		try {
			return Integer.parseInt(numberStr);
		} catch (NullPointerException nex) {
			return defaultValue;
		} catch (NumberFormatException invalidNumber) {
			return defaultValue;
		}
	}

	public static long getLong(String numberStr) {
		return getLong(numberStr, -1L);
	}

	public static long getLong(String numberStr, long defaultValue) {
		try {
			return Long.parseLong(numberStr);
		} catch (NullPointerException nex) {
			return defaultValue;
		} catch (NumberFormatException invalidNumber) {
			return defaultValue;
		}
	}

	public static double getDouble(String numberStr) {
		return getDouble(numberStr, -1d);
	}

	public static double getDouble(String numberStr, double defaultValue) {
		try {
			return Double.parseDouble(numberStr);
		} catch (NullPointerException nex) {
			return defaultValue;
		} catch (NumberFormatException invalidNumber) {
			return defaultValue;
		}
	}

	public static Properties getProperties(File file) {
		return getProperties(file.getAbsolutePath());
	}

	public static Properties getProperties(String path) {
		if (path == null || "".equals(path.trim())) {
			throw new RuntimeException("Null path");
		}

		File file = new File(path);
		if (!file.exists()) {
			throw new RuntimeException("File not found (" + path + ")");
		}
		Properties properties = new Properties();
		try {
			byte[] contents = getBytes(path);
			ByteArrayInputStream in = new ByteArrayInputStream(contents);
			properties.load(in);
			in.close();
			contents = null;
		} catch (IOException ioex) {
			properties.setProperty("$ERROR$", ioex.getMessage());
		}
		return properties;
	}

	public static Properties getPropertiesFromResource(String path) {
		if (path == null || "".equals(path.trim())) {
			throw new RuntimeException("Null path");
		}

		Properties properties = new Properties();
		try {
			InputStream in = Utils.class.getResourceAsStream(path);
			properties.load(in);
			in.close();
		} catch (IOException ioex) {
			properties.setProperty("$ERROR$", ioex.getMessage());
		}
		return properties;
	}

	public static void saveProperties(Properties properties, String path) {
		try {
			FileOutputStream out = new FileOutputStream(path);
			properties.store(out, "");
			out.flush();
			out.close();
		} catch (IOException ioex) {
			throw new RuntimeException(ioex);
		}
	}

	public static byte[] getBytes(String path) {
		try {
			FileInputStream in = new FileInputStream(path);
			return getBytes(in);
		} catch (IOException ioex) {
			ioex.printStackTrace();
			throw new RuntimeException(ioex);
		}
	}

	public static byte[] getBytes(InputStream in) {
		try {
			byte[] buffer = new byte[in.available()];
			in.read(buffer);
			in.close();
			return buffer;
		} catch (IOException ioex) {
			ioex.printStackTrace();
			throw new RuntimeException(ioex);
		}
	}

	public static void putBytes(byte[] contents, String path) {
		try {
			FileOutputStream out = new FileOutputStream(path);
			out.write(contents);
			out.flush();
			out.close();
		} catch (IOException ioex) {
			ioex.printStackTrace();
			throw new RuntimeException(ioex);
		}
	}

	public static void copyAll(Properties first, Properties second, boolean fwd) {
		if (fwd) {
			second.putAll(first);
		} else {
			first.putAll(second);
		}
	}

	public static String toCamelCase(String inputString, String eliminate) {
		String result = "";
		if (inputString.length() == 0) {
			return result;
		}
		char firstChar = inputString.charAt(0);
		char firstCharToUpperCase = Character.toUpperCase(firstChar);
		result = result + firstCharToUpperCase;
		for (int i = 1; i < inputString.length(); i++) {
			char currentChar = inputString.charAt(i);
			char previousChar = inputString.charAt(i - 1);
			if (previousChar == '_') {
				char currentCharToUpperCase = Character
						.toUpperCase(currentChar);
				result = result + currentCharToUpperCase;
			} else {
				char currentCharToLowerCase = Character
						.toLowerCase(currentChar);
				result = result + currentCharToLowerCase;
			}
		}
		result = result.replace(eliminate, "");
		return result;
	}

	public static String firstCharToCaps(String inputString) {
		if (inputString == null || inputString.length() < 2) {
			return "" + Character.toUpperCase(inputString.charAt(0));
		}
		return Character.toUpperCase(inputString.charAt(0))
				+ inputString.substring(1);
	}

	public static String firstCharToLowerCase(String inputString) {
		if (inputString == null || inputString.length() < 2) {
			return "" + Character.toLowerCase(inputString.charAt(0));
		}
		return Character.toLowerCase(inputString.charAt(0))
				+ inputString.substring(1);
	}

	public static String multiply(String src, int count, String separator) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < count; i++) {
			builder.append(src);
			if (i < count - 1) {
				builder.append(separator);
			}
		}
		return builder.toString();
	}

	public static String getNextIntegerString(String source, int width) {
		int value = Integer.parseInt(source);
		value++;
		String returnValue = null;
		int currentLength = String.valueOf(value).length();
		if (width > String.valueOf(value).length()) {
			returnValue = multiply("0", width - currentLength, "") + value;
		} else {
			returnValue = String.valueOf(value);
		}
		return returnValue;
	}

	public static void main(String[] args) {
		System.out.println(getNextIntegerString(args[0],
				Integer.parseInt(args[1])));
	}

	public void test() {
		System.out.println("changed");
	}

	public static Object getSimpleProperty(Object inputObject, String property) {
		Object returnValue = null;
		try {
			Method method = inputObject.getClass().getMethod(
					"get" + Character.toUpperCase(property.charAt(0))
							+ property.substring(1), new Class[] {});
			returnValue = method.invoke(inputObject, new Object[] {});
		} catch (NoSuchMethodException | SecurityException
				| IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			e.printStackTrace();
		}
		return returnValue;
	}

	public static boolean isPartOf(String array[], String token) {
		return Arrays.binarySearch(array, token) > -1;
	}

	public static InputStream getResourceAsStream(String resourcePath) {
		try {
			InputStream in = Utils.class.getResourceAsStream(resourcePath);
			return in;
		} catch (Exception ex) {
			return null;
		}
	}

	public static Properties getPropertiesFromResourceStream(String resourcePath) {
		try {
			InputStream in = getResourceAsStream(resourcePath);
			Properties properties = getProperties(in);
			in.close();
			return properties;
		} catch (Exception ex) {
			return new Properties();
		}
	}

	public static Properties getProperties(InputStream in) {
		Properties properties = new Properties();
		try {
			properties.load(in);
			in.close();
		} catch (IOException ioex) {
			properties.setProperty("$ERROR$", ioex.getMessage());
		}
		return properties;
	}

	public static Timestamp parseTimestamp(String timestampStr) {
		try {
			return new Timestamp(dateFormat.parse(timestampStr).getTime());
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static Date parseDate(String dateStr) {
		try {
			return dateFormat.parse(dateStr);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static String formatDate(String formatStr, Date date) {
		try {
			if (formatStr.equals(DEFAULT_DATE_FORMAT)) {
				return dateFormat.format(date);
			} else {
				SimpleDateFormat format = new SimpleDateFormat(formatStr);
				return format.format(date);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static List<String> getEnclosed(String source,
			String enclosureStart, String enclosureEnd) {
		List<String> enclosedStrings = new ArrayList<String>();
		Matcher m = Pattern.compile(
				"\\" + enclosureStart + "([^]]+)\\" + enclosureEnd).matcher(
				source);
		while (m.find()) {
			enclosedStrings.add(m.group(1));
		}
		return enclosedStrings;
	}

	public static void debug(String msg) {
		if (DEBUG > 2) {
			System.out.println(msg);
			LOGGER.info(msg);
		}
	}

	public static void debug2(String msg) {
		System.out.println(msg);
		LOGGER.info(msg);
	}

	public static void debugTrace(String message, Throwable throwable) {
		if (DEBUG > 0) {
			LOGGER.error(message, throwable);
			throwable.printStackTrace();
		}
	}

	public static long getDateFromAnyFormat(String paramDate) {
		SimpleDateFormat sdf1;
		Date sqlStartDate = null;
		try {
			
		        sdf1 = new SimpleDateFormat("dd/MM/yyyy");
		     
		     Date date = sdf1.parse(paramDate);
		     
		     System.err.println("date2 : "+ date);		     
	
			
			
		//	sdf1 = new SimpleDateFormat("dd-MM-yyyy");
			//java.util.Date date = sdf1.parse(paramDate);
			sqlStartDate = new java.sql.Date(date.getTime());
			LOGGER.info("SQL START DATE : " + sqlStartDate);
			
		} catch (ParseException pEx) {
			try {
				sdf1 = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
				java.util.Date date = sdf1.parse(paramDate);
				sqlStartDate = new java.sql.Date(date.getTime());
			} catch (ParseException pEx1) {
				try {
					sdf1 = new SimpleDateFormat("yyyy-MM-dd");
					java.util.Date date = sdf1.parse(paramDate);
					sqlStartDate = new java.sql.Date(date.getTime());
				} catch (ParseException pex2) {
					try {
						sdf1 = new SimpleDateFormat("MM-dd-yyyy");
						java.util.Date date = sdf1.parse(paramDate);
						sqlStartDate = new java.sql.Date(date.getTime());
					} catch (ParseException pex3) {
						try {
							sdf1 = new SimpleDateFormat("MM-yyyy-dd");
							java.util.Date date = sdf1.parse(paramDate);
							sqlStartDate = new java.sql.Date(date.getTime());
						} catch (ParseException pex4) {
							try {
								sdf1 = new SimpleDateFormat("MM-yyyy-dd");
								java.util.Date date = sdf1.parse(paramDate);
								sqlStartDate = new java.sql.Date(date.getTime());
							} catch (ParseException pEx4) {
								try {
									sdf1 = new SimpleDateFormat("dd-yyyy-MM");
									java.util.Date date = sdf1.parse(paramDate);
									sqlStartDate = new java.sql.Date(
											date.getTime());
								} catch (ParseException pEx5) {
									System.out
											.println("Date Parsing Exception");
								}
							}
						}
					}
				}
			}
		}
		
		long d = sqlStartDate != null ? sqlStartDate.getTime() : 0;
		
		LOGGER.info("DATE UTIL PACKAGE : " + d);
		return sqlStartDate != null ? sqlStartDate.getTime() : 0;
	}
	
	public static int get_YN_Num(String yn) {
		try {
			Integer.parseInt(yn);
		} catch (NumberFormatException ex) {
			return "Y".equalsIgnoreCase(yn) ? 1 : 2; 
		}
		return Utils.getInteger(yn); 
	}

	public static String replaceEnv(String original, Properties env) {
		String keyString = null;
		for (Object key : env.keySet()) {
			keyString = (String) key;
			if (original.indexOf("${" + keyString + "}") != -1) {
				original = original.replace("${" + keyString + "}", env.getProperty(keyString));
			}
		}
		return original;
	}
}